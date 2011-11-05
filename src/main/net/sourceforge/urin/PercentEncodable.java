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

public abstract class PercentEncodable {

    private PercentEncodable() {
        // deliberately empty
    }

    abstract String encode(PercentEncoder encoder);

    abstract boolean isEmpty();

    public static PercentEncodable percentEncodableString(final String value) {
        return new PercentEncodableString(value);
    }

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
}
