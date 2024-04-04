/*
 * Copyright 2024 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.urin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;
import static java.util.regex.Pattern.quote;
import static net.sourceforge.urin.CharacterSetMembershipFunction.NO_CHARACTERS;

public abstract class PercentEncodingPartial<ENCODES, CHILD_ENCODES> {
    PercentEncodingPartial() {
    }

    public static <T> PercentEncodingPartial<T, T> noOp() {
        return new PercentEncodingPartial<T, T>() {
            @Override
            public PercentEncoding<T> apply(final PercentEncoding<T> childPercentEncoding) {
                return childPercentEncoding;
            }
        };
    }

    public static <T> PercentEncodingPartial<T, T> additionallyEncoding(final Iterable<Character> characters, final PercentEncodingPartial<T, T> childPercentEncodingPartial) {
        characters.forEach(character -> requireNonNull(character, "Cannot additionally encode null Character"));
        return childPercentEncodingPartial.chain(new PercentEncodingPartial<T, T>() {
            @Override
            public PercentEncoding<T> apply(final PercentEncoding<T> childPercentEncoding) {
                PercentEncoding<T> result = childPercentEncoding;
                for (final char character : characters) {
                    result = result.additionallyEncoding(character);
                }
                return result;
            }
        });
    }

    public static <T, V> PercentEncodingPartial<Iterable<T>, V> percentEncodingDelimitedValue(final char delimiter, final PercentEncodingPartial<T, V> childPercentEncodingPartial) {
        return childPercentEncodingPartial.chain(new PercentEncodingPartial<Iterable<T>, T>() {
            @Override
            public PercentEncoding<Iterable<T>> apply(final PercentEncoding<T> childPercentEncoding) {
                return new PercentEncoding.PercentEncodingDelimitedValue<>(delimiter, childPercentEncoding);
            }
        });
    }

    public static PercentEncodingPartial<Iterable<String>, String> percentEncodingDelimitedValue(final char delimiter) {
        return new PercentEncodingPartial<Iterable<String>, String>() {
            @Override
            public PercentEncoding<Iterable<String>> apply(final PercentEncoding<String> childPercentEncoding) {
                return new PercentEncoding.PercentEncodingDelimitedValue<>(delimiter, childPercentEncoding);
            }
        };
    }

    public static PercentEncodingPartial<String, String> percentEncodingSubstitutedValue(final char originalCharacter, final char replacementCharacter) {
        return new PercentEncodingPartial<String, String>() {
            @Override
            public PercentEncoding<String> apply(final PercentEncoding<String> childPercentEncoding) {
                return new PercentEncoding.PercentEncodingSubstitutedValue(originalCharacter, replacementCharacter, childPercentEncoding);
            }
        };
    }

    public static <T, U, V> PercentEncodingPartial<T, V> transformingPercentEncodingPartial(final PercentEncodingPartial<U, V> childPercentEncodingPartial, final Transformer<T, U> transformer) {
        return childPercentEncodingPartial.chain(new PercentEncodingPartial<T, U>() {
            @Override
            public PercentEncoding<T> apply(final PercentEncoding<U> childPercentEncoding) {
                return new PercentEncoding<T>() {
                    @Override
                    public String encode(final T notEncoded) {
                        return childPercentEncoding.encode(transformer.encode(notEncoded));
                    }

                    @Override
                    public T decode(final String encoded) throws ParseException {
                        return transformer.decode(childPercentEncoding.decode(encoded));
                    }

                    @Override
                    public PercentEncoding<T> additionallyEncoding(final char additionallyEncodedCharacter) {
                        return apply(childPercentEncoding.additionallyEncoding(additionallyEncodedCharacter));
                    }
                };
            }
        });
    }

    abstract PercentEncoding<ENCODES> apply(PercentEncoding<CHILD_ENCODES> childPercentEncoding);

    final <SUPER_ENCODES> PercentEncodingPartial<SUPER_ENCODES, CHILD_ENCODES> chain(final PercentEncodingPartial<SUPER_ENCODES, ENCODES> superEncoder) {
        return new PercentEncodingPartial<SUPER_ENCODES, CHILD_ENCODES>() {
            @Override
            public PercentEncoding<SUPER_ENCODES> apply(final PercentEncoding<CHILD_ENCODES> childPercentEncoding) {
                return superEncoder.apply(PercentEncodingPartial.this.apply(childPercentEncoding));
            }
        };
    }

    abstract static class PercentEncoding<ENCODES> {
        static PercentEncoding<String> percentEncodingString(final PercentEncoder percentEncoder) {
            return new PercentEncodingString(percentEncoder);
        }

        static <T> PercentEncoding<Iterable<T>> percentEncodingDelimitedValue(final char delimiter, final PercentEncoding<T> elementPercentEncoding) {
            return new PercentEncodingDelimitedValue<>(delimiter, elementPercentEncoding);
        }

        static PercentEncoding<String> specifiedValueEncoding(final String encodedValue, final PercentEncoding<String> percentEncoding) {
            return new SpecifiedValueEncoding(encodedValue, percentEncoding);
        }

        public abstract String encode(ENCODES notEncoded);

        public abstract ENCODES decode(String encoded) throws ParseException;

        public abstract PercentEncoding<ENCODES> additionallyEncoding(char additionallyEncodedCharacter);

        private static final class PercentEncodingString extends PercentEncoding<String> {
            private final PercentEncoder percentEncoder;

            PercentEncodingString(final PercentEncoder percentEncoder) {
                this.percentEncoder = requireNonNull(percentEncoder, "Cannot instantiate PercentEncodingString with null PercentEncoder");
            }

            @Override
            public String encode(final String notEncoded) {
                return percentEncoder.encode(notEncoded);
            }

            @Override
            public String decode(final String encoded) throws ParseException {
                return percentEncoder.decode(encoded);
            }

            @Override
            public PercentEncoding<String> additionallyEncoding(final char additionallyEncodedCharacter) {
                return new PercentEncodingString(percentEncoder.additionallyEncoding(additionallyEncodedCharacter));
            }

        }

        private static final class PercentEncodingDelimitedValue<T> extends PercentEncoding<Iterable<T>> {
            private final char delimiter;
            private final PercentEncoding<T> percentEncoding;

            PercentEncodingDelimitedValue(final char delimiter, final PercentEncoding<T> percentEncoding) {
                this.delimiter = delimiter;
                this.percentEncoding = percentEncoding.additionallyEncoding(delimiter);
            }

            @Override
            public String encode(final Iterable<T> notEncoded) {
                final Iterator<T> notEncodedIterator = notEncoded.iterator();
                final StringBuilder result = new StringBuilder();
                while (notEncodedIterator.hasNext()) {
                    result.append(percentEncoding.encode(notEncodedIterator.next()));
                    if (notEncodedIterator.hasNext()) {
                        result.append(delimiter);
                    }
                }
                return result.toString();
            }

            @Override
            public Iterable<T> decode(final String encoded) throws ParseException {
                final String[] components = encoded.split(quote(Character.toString(delimiter)), -1);
                final List<T> result = new ArrayList<>(components.length);
                for (final String component : components) {
                    result.add(percentEncoding.decode(component));
                }
                return result;
            }

            @Override
            public PercentEncoding<Iterable<T>> additionallyEncoding(final char additionallyEncodedCharacter) {
                return new PercentEncodingDelimitedValue<>(delimiter, percentEncoding.additionallyEncoding(additionallyEncodedCharacter));
            }

        }

        private static class PercentEncodingSubstitutedValue extends PercentEncoding<String> {
            private final char originalCharacter;
            private final char replacementCharacter;
            private final PercentEncoding<String> percentEncoding;

            PercentEncodingSubstitutedValue(final char originalCharacter, final char replacementCharacter, final PercentEncoding<String> percentEncoding) {
                this.originalCharacter = originalCharacter;
                this.replacementCharacter = replacementCharacter;
                this.percentEncoding = percentEncoding.additionallyEncoding(replacementCharacter);
            }

            @Override
            public String encode(final String notEncoded) {
                final StringBuilder result = new StringBuilder();
                final Iterator<String> valuePartsIterator = asList(notEncoded.split(quote(Character.toString(originalCharacter)), -1)).iterator();
                while (valuePartsIterator.hasNext()) {
                    result.append(percentEncoding.encode(valuePartsIterator.next()));
                    if (valuePartsIterator.hasNext()) {
                        result.append(replacementCharacter);
                    }
                }
                return result.toString();
            }

            @Override
            public String decode(final String encoded) throws ParseException {
                final StringBuilder result = new StringBuilder();
                final String[] split = encoded.split(quote(Character.toString(replacementCharacter)));
                for (int i = 0; i < split.length; i++) {
                    if (i > 0) {
                        result.append(originalCharacter);
                    }
                    result.append(percentEncoding.decode(split[i]));
                }
                return result.toString();
            }

            @Override
            public PercentEncoding<String> additionallyEncoding(final char additionallyEncodedCharacter) {
                return new PercentEncodingSubstitutedValue(originalCharacter, replacementCharacter, percentEncoding.additionallyEncoding(additionallyEncodedCharacter));
            }

        }

        private static class SpecifiedValueEncoding extends PercentEncoding<String> {

            private final String encodedValue;
            private final PercentEncoding<String> percentEncoding;

            SpecifiedValueEncoding(final String encodedValue, final PercentEncoding<String> percentEncoding) {
                this.encodedValue = requireNonNull(encodedValue, "Cannot instantiate SpecifiedValueEncoding with null encoded value");
                this.percentEncoding = requireNonNull(percentEncoding, "Cannot instantiate SpecifiedValueEncoding with null PercentEncoding");
            }

            @Override
            public String encode(final String notEncoded) {
                if (encodedValue.equals(notEncoded)) {
                    return new PercentEncoder(NO_CHARACTERS).encode(notEncoded);
                } else {
                    return percentEncoding.encode(notEncoded);
                }
            }

            @Override
            public String decode(final String encoded) throws ParseException {
                return percentEncoding.decode(encoded);
            }

            @Override
            public PercentEncoding<String> additionallyEncoding(final char additionallyEncodedCharacter) {
                return new SpecifiedValueEncoding(encodedValue, percentEncoding.additionallyEncoding(additionallyEncodedCharacter));
            }

        }

    }
}
