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

    @Override
    final boolean isEmpty() {
        return percentEncoding.isEmpty(value);
    }

    @Override
    final String asString() {
        return percentEncoding.encode(value);
    }

    protected static PercentEncoding<String> percentEncodingString(final PercentEncoder percentEncoder) {
        return new PercentEncoding.PercentEncodingString(percentEncoder);
    }

    protected static <T> PercentEncoding<Iterable<T>> percentEncodingDelimitedValue(final char delimiter, final PercentEncoding<T> elementPercentEncoding) {
        return new PercentEncoding.PercentEncodingDelimitedValue<T>(delimiter, elementPercentEncoding);
    }

    protected static PercentEncoding<String> percentEncodingSubstitutedValue(final char originalCharacter, final char replacementCharacter, final PercentEncoding<String> percentEncoding) {
        return new PercentEncoding.PercentEncodableSubstitutedValue(originalCharacter, replacementCharacter, percentEncoding);
    }

    protected static PercentEncoding<String> specifiedValueEncoding(final String encodedValue, PercentEncoding<String> percentEncoding) {
        return new PercentEncoding.SpecifiedValueEncoding(encodedValue, percentEncoding);
    }

    protected static PercentEncoding<String> nonEncoding() {
        return new PercentEncoding.NonEncodingPercentEncoding();
    }

    protected abstract static class PercentEncoding<ENCODES> {
        public abstract String encode(ENCODES notEncoded);

        public abstract ENCODES decode(String encoded) throws ParseException;

        public abstract PercentEncoding<ENCODES> additionallyEncoding(final char additionallyEncodedCharacter);

        protected abstract boolean isEmpty(final ENCODES value);

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

            @Override
            protected boolean isEmpty(final String value) {
                return value.isEmpty();
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

            @Override
            protected boolean isEmpty(final Iterable<T> value) {
                return value.iterator().hasNext();
            }
        }

        private static class PercentEncodableSubstitutedValue extends PercentEncoding<String> {
            private final char originalCharacter;
            private final char replacementCharacter;
            private final PercentEncoding<String> percentEncoding;

            PercentEncodableSubstitutedValue(final char originalCharacter, final char replacementCharacter, final PercentEncoding<String> percentEncoding) {
                this.originalCharacter = originalCharacter;
                this.replacementCharacter = replacementCharacter;
                this.percentEncoding = percentEncoding.additionallyEncoding(replacementCharacter);
            }

            @Override
            public String encode(final String notEncoded) {
                StringBuilder result = new StringBuilder();
                Iterator<String> valuePartsIterator = asList(notEncoded.split(Character.toString(originalCharacter))).iterator();
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
                return new PercentEncodableSubstitutedValue(originalCharacter, replacementCharacter, percentEncoding.additionallyEncoding(additionallyEncodedCharacter));
            }

            @Override
            protected boolean isEmpty(final String value) {
                return value.isEmpty();
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

            @Override
            protected boolean isEmpty(final String value) {
                return value.isEmpty();
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

            @Override
            protected boolean isEmpty(final String value) {
                return value.isEmpty();
            }

        }

    }
}
