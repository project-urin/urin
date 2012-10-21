/*
 * Copyright 2012 Mark Slater
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
import java.util.regex.Pattern;

import static java.util.Arrays.asList;
import static java.util.regex.Pattern.quote;
import static net.sourceforge.urin.CharacterSetMembershipFunction.NO_CHARACTERS;

abstract class PercentEncodingUnaryValue<ENCODING> extends UnaryValue<ENCODING> {

    private final PercentEncoding<ENCODING> percentEncoding;

    PercentEncodingUnaryValue(final ENCODING value, final PercentEncoding<ENCODING> percentEncoding) {
        super(value);
        this.percentEncoding = percentEncoding;
    }

    final String asString() {
        return percentEncoding.encode(value);
    }


    /*
    Encoding Iterable<QueryParameter> -> String
    interface Encoder<FROM, TO> {
        TO encode(FROM from);
    }
    interface Encoding<FROM, TO> {
        Encoder<FROM, TO> build(Encoder< percentEncode);
    }
    QueryParametersEncoding<TO> implements Encoding<Iterable<QueryParameter, TO> {
        static queryParametersEncoding Encoding<Iterable<QueryParameter>, QueryParameter>() {
            return new Encoding<Iterable<QueryParameter>, QueryParameter>() {
                Encoder<Iterable<QueryParameter>, QueryParameter> build(PercentEncoder percentEncoder) {
                    return new Encoder<Iterable<QueryParameter>, QueryParameter> {
                        QueryPara
                    }
                }
            }
        }
        QueryParameterEncoding<Iterable<QueryParameter>, <TO>(Encoding<QueryParameter, TO> {
        }

        encode(PercentEncoder percentEncoder) {

        }
    }
     */

    protected static abstract class PercentEncodingPartial<ENCODES, CHILD_ENCODES> {
        public abstract PercentEncoding<ENCODES> apply(PercentEncoding<CHILD_ENCODES> childPercentEncoding);

        public final <SUPER_ENCODES> PercentEncodingPartial<SUPER_ENCODES, CHILD_ENCODES> chain(final PercentEncodingPartial<SUPER_ENCODES, ENCODES> superEncoder) {
            return new PercentEncodingPartial<SUPER_ENCODES, CHILD_ENCODES>() {
                @Override
                public PercentEncoding<SUPER_ENCODES> apply(PercentEncoding<CHILD_ENCODES> childPercentEncoding) {
                    return superEncoder.apply(PercentEncodingPartial.this.apply(childPercentEncoding));
                }
            };
        }
    }

    protected abstract static class PercentEncoding<ENCODES> {
        protected static PercentEncoding<String> percentEncodingString(final PercentEncoder percentEncoder) {
            return new PercentEncodingString(percentEncoder);
        }

        public static <T, V> PercentEncodingPartial<Iterable<T>, V> percentEncodingDelimitedValue(final char delimiter, PercentEncodingPartial<T, V> childPercentEncodingPartial) {
            return childPercentEncodingPartial.chain(new PercentEncodingPartial<Iterable<T>, T>() {
                public PercentEncoding<Iterable<T>> apply(PercentEncoding<T> childPercentEncoding) {
                    return new PercentEncodingDelimitedValue<T>(delimiter, childPercentEncoding);
                }
            });
        }

        public static <T> PercentEncoding<Iterable<T>> percentEncodingDelimitedValue(final char delimiter, final PercentEncoding<T> elementPercentEncoding) {
            return new PercentEncodingDelimitedValue<T>(delimiter, elementPercentEncoding);
        }

        public static PercentEncodingPartial<String, String> percentEncodingSubstitutedValue(final char originalCharacter, final char replacementCharacter) {
            return new PercentEncodingPartial<String, String>() {
                @Override
                public PercentEncoding<String> apply(PercentEncoding<String> childPercentEncoding) {
                    return new PercentEncodingSubstitutedValue(originalCharacter, replacementCharacter, childPercentEncoding);
                }
            };
        }

        static PercentEncoding<String> specifiedValueEncoding(final String encodedValue, PercentEncoding<String> percentEncoding) {
            return new SpecifiedValueEncoding(encodedValue, percentEncoding);
        }

        static PercentEncoding<String> nonEncoding() {
            return new NonEncodingPercentEncoding();
        }

        public abstract String encode(ENCODES notEncoded);

        public abstract ENCODES decode(String encoded) throws ParseException;

        public abstract PercentEncoding<ENCODES> additionallyEncoding(final char additionallyEncodedCharacter);

        private static final class PercentEncodingString extends PercentEncoding<String> {
            private final PercentEncoder percentEncoder;

            PercentEncodingString(final PercentEncoder percentEncoder) {
                if (percentEncoder == null) {
                    throw new NullPointerException("Cannot instantiate PercentEncodingString with null PercentEncoder");
                }
                this.percentEncoder = percentEncoder;
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
                Iterator<T> notEncodedIterator = notEncoded.iterator();
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
                final String[] components = encoded.split(quote(Character.toString(delimiter)));
                return new ArrayList<T>() {{
                    for (String component : components) {
                        add(percentEncoding.decode(component));
                    }
                }};
            }

            @Override
            public PercentEncoding<Iterable<T>> additionallyEncoding(final char additionallyEncodedCharacter) {
                return new PercentEncodingDelimitedValue<T>(delimiter, percentEncoding.additionallyEncoding(additionallyEncodedCharacter));
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
                StringBuilder result = new StringBuilder();
                Iterator<String> valuePartsIterator = asList(notEncoded.split(Pattern.quote(Character.toString(originalCharacter)), -1)).iterator();
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
                String[] split = encoded.split(Pattern.quote(Character.toString(replacementCharacter)));
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

            SpecifiedValueEncoding(final String encodedValue, PercentEncoding<String> percentEncoding) {
                if (encodedValue == null) {
                    throw new NullPointerException("Cannot instantiate SpecifiedValueEncoding with null encoded value");
                }
                this.encodedValue = encodedValue;
                if (percentEncoding == null) {
                    throw new NullPointerException("Cannot instantiate SpecifiedValueEncoding with null PercentEncoding");
                }
                this.percentEncoding = percentEncoding;
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

        private static class NonEncodingPercentEncoding extends PercentEncoding<String> {

            NonEncodingPercentEncoding() {
            }

            @Override
            public String encode(final String notEncoded) {
                return notEncoded;
            }

            @Override
            public String decode(final String encoded) throws ParseException {
                return encoded;
            }

            @Override
            public PercentEncoding<String> additionallyEncoding(final char additionallyEncodedCharacter) {
                return this;
            }

        }

    }
}
