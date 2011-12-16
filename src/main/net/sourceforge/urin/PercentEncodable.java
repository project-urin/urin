/*
 * Copyright 2011 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.urin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static java.util.Arrays.asList;

public abstract class PercentEncodable {

    private PercentEncodable() {
        // deliberately empty
    }

    abstract String encode(PercentEncoder encoder);

    abstract boolean isEmpty();

    public static PercentEncodable percentEncodableString(final String value) {
        return new PercentEncodableString(value);
    }

    public static PercentEncodable percentEncodableSubstitutedValue(final char original, final char replacement, final String value) {
        return new PercentEncodableSubstitutedValue(original, replacement, value);
    }

    public static PercentEncodable percentEncodableDelimitedValue(final char delimiter, final PercentEncodable... values) {
        return percentEncodableDelimitedValue(delimiter, asList(values));
    }

    public static PercentEncodable percentEncodableDelimitedValue(final char delimiter, final Collection<PercentEncodable> values) {
        return new PercentEncodableDelimitedValue(delimiter, values);
    }

    abstract boolean containsColon();

    private static final class PercentEncodableString extends PercentEncodable {
        private final String value;

        PercentEncodableString(final String value) {
            if (value == null) {
                throw new NullPointerException("Cannot instantiate PercentEncodable with null value");
            }
            this.value = value;
        }

        @Override
        String encode(final PercentEncoder encoder) {
            return encoder.encode(value);
        }

        @Override
        boolean isEmpty() {
            return value.isEmpty();
        }

        @Override
        boolean containsColon() {
            return value.indexOf(':') != -1;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PercentEncodableString that = (PercentEncodableString) o;
            return value.equals(that.value);
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }

        @Override
        public String toString() {
            return "PercentEncodable{" +
                    "value='" + value + '\'' +
                    '}';
        }
    }

    private static final class PercentEncodableDelimitedValue extends PercentEncodable {
        private final char delimiter;
        private final Collection<PercentEncodable> values;

        PercentEncodableDelimitedValue(final char delimiter, final Collection<PercentEncodable> values) {
            final List<PercentEncodable> percentEncodableList = new ArrayList<PercentEncodable>(values.size());
            for (PercentEncodable value : values) {
                if (value == null) {
                    throw new NullPointerException("Cannot instantiate PercentEncodable with null value");
                }
                percentEncodableList.add(value);
            }
            this.delimiter = delimiter;
            this.values = percentEncodableList;
        }

        @Override
        String encode(final PercentEncoder encoder) {
            PercentEncoder subEncoder = encoder.additionallyEncoding(delimiter);
            Iterator<PercentEncodable> percentEncodableIterator = values.iterator();
            final StringBuilder result = new StringBuilder();
            while (percentEncodableIterator.hasNext()) {
                result.append(percentEncodableIterator.next().encode(subEncoder));
                if (percentEncodableIterator.hasNext()) {
                    result.append(delimiter);
                }
            }
            return result.toString();
        }

        @Override
        boolean isEmpty() {
            return values.isEmpty();
        }

        @Override
        boolean containsColon() {
            for (final PercentEncodable value : values) {
                if (value.containsColon()) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PercentEncodableDelimitedValue that = (PercentEncodableDelimitedValue) o;
            return delimiter == that.delimiter && values.equals(that.values);
        }

        @Override
        public int hashCode() {
            int result = (int) delimiter;
            result = 31 * result + values.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "PercentEncodable{" +
                    "delimiter=" + delimiter +
                    ", values=" + values +
                    '}';
        }
    }

    private static class PercentEncodableSubstitutedValue extends PercentEncodable {
        private final char originalCharacter;
        private final char replacementCharacter;
        private final String value;

        public PercentEncodableSubstitutedValue(final char originalCharacter, final char replacementCharacter, final String value) {
            this.originalCharacter = originalCharacter;
            this.replacementCharacter = replacementCharacter;
            if (value == null) {
                throw new NullPointerException("Cannot instantiate PercentEncodable with null value");
            }
            this.value = value;
        }

        @Override
        String encode(final PercentEncoder encoder) {
            StringBuilder result = new StringBuilder();
            PercentEncoder modifiedPercentEncoder = encoder.additionallyEncoding(replacementCharacter);
            Iterator<String> valuePartsIterator = asList(value.split(Character.toString(originalCharacter))).iterator();
            while (valuePartsIterator.hasNext()) {
                result.append(modifiedPercentEncoder.encode(valuePartsIterator.next()));
                if (valuePartsIterator.hasNext()) {
                    result.append(replacementCharacter);
                }
            }
            return result.toString();
        }

        @Override
        boolean isEmpty() {
            return value.isEmpty();
        }

        @Override
        boolean containsColon() {
            return value.indexOf(':') != -1;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PercentEncodableSubstitutedValue that = (PercentEncodableSubstitutedValue) o;
            return originalCharacter == that.originalCharacter
                    && replacementCharacter == that.replacementCharacter
                    && value.equals(that.value);
        }

        @Override
        public int hashCode() {
            int result = (int) originalCharacter;
            result = 31 * result + (int) replacementCharacter;
            result = 31 * result + value.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "PercentEncodable{" +
                    "originalCharacter=" + originalCharacter +
                    ", replacementCharacter=" + replacementCharacter +
                    ", value='" + value + '\'' +
                    '}';
        }
    }
}
