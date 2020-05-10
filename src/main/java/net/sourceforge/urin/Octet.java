/*
 * Copyright 2020 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.urin;

/**
 * An integer in the range 0 to 255.
 * Immutable and thread safe.
 */
public final class Octet extends UnaryStringValue {

    private Octet(final String octet) {
        super(octet);
    }

    /**
     * Factory method for creating {@code Octet}s.
     *
     * @param octet an {@code int} in the range 0 to 255.
     * @return an {@code Octet} representing the given {@code int}.
     * @throws IllegalArgumentException if the given {@code int} is outside the range 0 to 255.
     */
    public static Octet octet(final int octet) {
        return makeOctet(octet).orElseThrow(IllegalArgumentException::new);
    }

    private static AugmentedOptional<Octet> makeOctet(final int octet) {
        return octet < 0 || octet > 255
                ? AugmentedOptional.empty("Argument must be in the range 0-255 but was [" + octet + "]")
                : AugmentedOptional.of(new Octet(Integer.toString(octet)));
    }

    static AugmentedOptional<Octet> parses(final String octetString) {
        final int octetInt;
        try {
            octetInt = Integer.parseInt(octetString);
        } catch (NumberFormatException e) {
            return AugmentedOptional.empty("Invalid Octet String [" + octetString + "]");
        }
        return makeOctet(octetInt);
    }
}
