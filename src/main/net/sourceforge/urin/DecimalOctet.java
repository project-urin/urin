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

public class DecimalOctet {
    private final String decimalOctet;

    private DecimalOctet(final String decimalOctet) {
        this.decimalOctet = decimalOctet;
    }

    public static DecimalOctet decimalOctet(final int decimalOctet) {
        if (decimalOctet < 0 || decimalOctet > 255) {
            throw new IllegalArgumentException("Argument must be in the range 0-255 but was [" + decimalOctet + "]");
        }
        return new DecimalOctet(Integer.toString(decimalOctet));
    }

    public String asString() {
        return decimalOctet;
    }
}
