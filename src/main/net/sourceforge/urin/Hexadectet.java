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

import java.util.Locale;

import static net.sourceforge.urin.ExceptionFactory.ILLEGAL_ARGUMENT_EXCEPTION_EXCEPTION_FACTORY;
import static net.sourceforge.urin.ExceptionFactory.PARSE_EXCEPTION_EXCEPTION_FACTORY;

public final class Hexadectet extends UnaryValue<Integer> {

    private static final Locale NO_LOCALISATION = null;
    public static final Hexadectet ZERO = hexadectet(0x0);

    private final boolean isElidable;

    public static Hexadectet hexadectet(final int hexadectet) {
        return hexadectet(hexadectet, ILLEGAL_ARGUMENT_EXCEPTION_EXCEPTION_FACTORY);
    }

    private static <T extends Exception> Hexadectet hexadectet(final int hexadectet, final ExceptionFactory<T> exceptionFactory) throws T {
        if (hexadectet < 0x0 || hexadectet > 0xFFFF) {
            String absoluteHexValue = Integer.toHexString(Math.abs(hexadectet));
            throw exceptionFactory.makeException("Argument must be in the range 0x0-0xFFFF but was [" + (hexadectet >= 0 ? "" : "-") + "0x" + absoluteHexValue + "]");
        }
        return new Hexadectet(hexadectet, hexadectet == 0);
    }

    static Hexadectet parse(final String hexadectetString) throws ParseException {
        final int hexadectetInt;
        try {
            hexadectetInt = Integer.parseInt(hexadectetString, 16);
        } catch (NumberFormatException e) {
            throw new ParseException("Invalid Hexadectet String [" + hexadectetString + "]", e);
        }
        return hexadectet(hexadectetInt, PARSE_EXCEPTION_EXCEPTION_FACTORY);
    }

    static boolean isValid(final String octetString) {
        final int hexadectetInt;
        try {
            hexadectetInt = Integer.parseInt(octetString, 16);
        } catch (NumberFormatException e) {
            return false;
        }
        return !(hexadectetInt < 0x0 || hexadectetInt > 0xFFFF);
    }

    private Hexadectet(final int hexadecimalHexadectet, final boolean isElidable) {
        super(hexadecimalHexadectet);
        this.isElidable = isElidable;
    }

    boolean isElidable() {
        return isElidable;
    }

    @Override
    String asString() {
        return Integer.toHexString(value);
    }

    @Override
    boolean isEmpty() {
        return false;
    }

    @Override
    public String toString() {
        return String.format(NO_LOCALISATION, "Hexadectet{value=0x%1$X}", value);
    }
}
