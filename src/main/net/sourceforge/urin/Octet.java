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

import static net.sourceforge.urin.ExceptionFactory.ILLEGAL_ARGUMENT_EXCEPTION_EXCEPTION_FACTORY;
import static net.sourceforge.urin.ExceptionFactory.PARSE_EXCEPTION_EXCEPTION_FACTORY;

/**
 * An integer in the range 0 to 255.
 * Immutable and threadsafe.
 */
public final class Octet extends UnaryStringValue {

    private Octet(final String octet) {
        super(octet);
    }

    /**
     * Factory method for creating <code>Octet</code>s.
     *
     * @param octet an <code>int</code> in the range 0 to 255.
     * @return an <code>Octet</code> representing the given <code>int</code>.
     * @throws IllegalArgumentException if the given <code>int</code> is outside the range 0 to 255.
     */
    public static Octet octet(final int octet) {
        return octet(octet, ILLEGAL_ARGUMENT_EXCEPTION_EXCEPTION_FACTORY);
    }

    private static <T extends Exception> Octet octet(final int octet, final ExceptionFactory<T> exceptionFactory) throws T {
        if (octet < 0 || octet > 255) {
            throw exceptionFactory.makeException("Argument must be in the range 0-255 but was [" + octet + "]");
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
        final int octetInt;
        try {
            octetInt = Integer.parseInt(octetString);
        } catch (NumberFormatException e) {
            throw new ParseException("Invalid Octet String [" + octetString + "]", e);
        }
        return octet(octetInt, PARSE_EXCEPTION_EXCEPTION_FACTORY);
    }
}
