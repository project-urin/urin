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

import java.util.Locale;

import static net.sourceforge.urin.CharacterSetMembershipFunction.HEX_DIGIT;

/**
 * An integer in the range 0 to FFFF (0 to 65535 in decimal).
 * Immutable and thread safe.
 */
public final class Hexadectet extends UnaryValue<Integer> {

    /**
     * A {@code Hexadectet} representing 0.
     */
    public static final Hexadectet ZERO = hexadectet(0x0);
    private static final Locale NO_LOCALISATION = null;

    private Hexadectet(final int hexadecimalHexadectet) {
        super(hexadecimalHexadectet);
    }

    /**
     * Factory method for creating {@code Hexadectets}.
     *
     * @param hexadectet an {@code int} in the range 0 to FFFF (0 to 65535 in decimal).
     * @return a {@code Hexadectet} representing the given {@code int}.
     * @throws IllegalArgumentException if the given {@code int} is outside the range 0 to FFFF.
     */
    public static Hexadectet hexadectet(final int hexadectet) {
        return makeHexadectet(hexadectet).orElseThrow(IllegalArgumentException::new);
    }

    private static AugmentedOptional<Hexadectet> makeHexadectet(final int hexadectet) {
        if (hexadectet < 0x0 || hexadectet > 0xFFFF) {
            final String absoluteHexValue = Integer.toHexString(Math.abs(hexadectet));
            return AugmentedOptional.empty("Argument must be in the range 0x0-0xFFFF but was [" + (hexadectet >= 0 ? "" : "-") + "0x" + absoluteHexValue + "]");
        }
        return AugmentedOptional.of(new Hexadectet(hexadectet));
    }

    static AugmentedOptional<Hexadectet> parses(final String hexadectetString) {
        if (hexadectetString == null || !HEX_DIGIT.areMembers(hexadectetString)) {
            return AugmentedOptional.empty("Invalid Hexadectet String [" + hexadectetString + "]");
        }
        final int hexadectetInt;
        try {
            hexadectetInt = Integer.parseInt(hexadectetString, 16);
        } catch (NumberFormatException e) {
            return AugmentedOptional.empty("Invalid Hexadectet String [" + hexadectetString + "]");
        }
        return makeHexadectet(hexadectetInt);
    }

    boolean isElidable() {
        return value == 0;
    }

    String asString() {
        return Integer.toHexString(value);
    }

    @Override
    public String toString() {
        return String.format(NO_LOCALISATION, "Hexadectet{value=0x%1$X}", value);
    }
}
