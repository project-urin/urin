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

abstract class SingleEncodedValue {

    private final String value;
    private final PercentEncoder percentEncoder;

    SingleEncodedValue(final String value, final PercentEncoder percentEncoder) {
        this.value = value;
        this.percentEncoder = percentEncoder;
    }

    String asString() {
        return percentEncoder.encode(value);
    }

    boolean isEmpty() {
        return this.value.isEmpty();
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SingleEncodedValue that = (SingleEncodedValue) o;
        return !(value != null ? !value.equals(that.value) : that.value != null);
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{value='" + value + "'}";
    }
}
