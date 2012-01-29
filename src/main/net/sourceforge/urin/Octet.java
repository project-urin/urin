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

public final class Octet extends UnaryStringValue {

    private Octet(final String octet) {
        super(octet);
    }

    public static Octet octet(final int octet) {
        if (octet < 0 || octet > 255) {
            throw new IllegalArgumentException("Argument must be in the range 0-255 but was [" + octet + "]");
        }
        return new Octet(Integer.toString(octet));
    }

    static boolean isValid(final String octetString) {
        final int octetInt;
        try {
            octetInt = Integer.parseInt(octetString);
        } catch (NumberFormatException e) {
            return false;
        }
        return !(octetInt < 0 || octetInt > 255);
    }

    static Octet parse(final String octetString) throws ParseException {
        if (isValid(octetString)) {
            return new Octet(octetString);
        } else {
            throw new ParseException("Invalid Octet String [" + octetString + "]");
        }
    }
}
