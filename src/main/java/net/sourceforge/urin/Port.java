/*
 * Copyright 2019 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.urin;

import java.math.BigInteger;

import static net.sourceforge.urin.CharacterSetMembershipFunction.DIGIT;
import static net.sourceforge.urin.ExceptionFactory.ILLEGAL_ARGUMENT_EXCEPTION_EXCEPTION_FACTORY;
import static net.sourceforge.urin.ExceptionFactory.PARSE_EXCEPTION_EXCEPTION_FACTORY;

/**
 * A non-negative integer port.
 * Immutable and threadsafe.
 *
 * @see <a href="http://tools.ietf.org/html/rfc3986#section-3.2.3">RFC 3986 - Port</a>
 */
public final class Port extends UnaryStringValue {

    private Port(final String port) {
        super(port);
    }

    /**
     * Factory method for creating ports from non-negative {@code int}s.  Note that RFC 3986 permits any non-negative
     * integer here, but this method limits you to integers up to {@code java.lang.Integer.MAX_VALUE}.  For
     * integers greater than this, use {@link #Port(String)}.
     *
     * @param port a non-negative {@code int}.
     * @return a {@code Port} representing the given {@code int}.
     * @throws IllegalArgumentException if the given {@code int} is negative.
     */
    public static Port port(final int port) {
        return port(Integer.toString(port));
    }

    /**
     * Factory method for creating ports from {@code String} representations of non-negative integers.  This means
     * {@code String}s consisting solely of digits in the range 0-9.
     *
     * @param port a {@code String} consisting solely of digits in the range 0-9.
     * @return a Port representing the given {@code String}.
     * @throws IllegalArgumentException if the given {@code String} contains characters outside the range 0-9.
     */
    public static Port port(final String port) {
        return port(port, ILLEGAL_ARGUMENT_EXCEPTION_EXCEPTION_FACTORY);
    }

    private static <T extends Exception> Port port(final String port, final ExceptionFactory<T> exceptionFactory) throws T {
        for (int i = 0; i < port.length(); i++) {
            if (!DIGIT.isMember(port.charAt(i))) {
                throw exceptionFactory.makeException("Character " + (i + 1) + " must be " + DIGIT.describe() + " in port [" + port + "]");
            }
        }
        return new Port(port.isEmpty() ? port : new BigInteger(port).toString());
    }

    static Port parse(final String port) throws ParseException {
        return port(port, PARSE_EXCEPTION_EXCEPTION_FACTORY);
    }
}
