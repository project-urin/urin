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

import static java.util.Locale.ENGLISH;
import static net.sourceforge.urin.CharacterSetMembershipFunction.*;

/**
 * A scheme component of a URI.
 * Immutable and threadsafe.
 *
 * @see <a href="http://tools.ietf.org/html/rfc3986#section-3.1">RFC 3986 - Scheme</a>
 */
public final class Scheme extends UnaryStringValue {

    private static final CharacterSetMembershipFunction TRAILING_CHARACTER_MEMBERSHIP_FUNCTION = or(
            ALPHA_LOWERCASE,
            ALPHA_UPPERCASE,
            DIGIT,
            singleMemberCharacterSet('+'),
            singleMemberCharacterSet('-'),
            singleMemberCharacterSet('.')
    );

    private Scheme(final String scheme) {
        super(scheme);
    }

    /**
     * Factory method for creating {@code Scheme}s.  Schemes must be at least one character long, and are permitted
     * to contain any character in the Latin alphabet, any digit, and any of the characters '+', '-', and '.'.
     *
     * @param scheme a {@code String} containing at least one character, made up of any characters in the Latin alphabet, the digits, and the characters '+', '-', and '.'.
     * @return a {@code Scheme} representing the given {@code String}.
     * @throws IllegalArgumentException if the given {@code String} is empty or contains characters not in the Latin alphabet, the digits, or the characters '+', '-', and '.'.
     */
    public static Scheme scheme(final String scheme) {
        verify(scheme, ExceptionFactory.ILLEGAL_ARGUMENT_EXCEPTION_EXCEPTION_FACTORY);
        return new Scheme(scheme.toLowerCase(ENGLISH));
    }

    static Scheme parse(final String name) throws ParseException {
        verify(name, ExceptionFactory.PARSE_EXCEPTION_EXCEPTION_FACTORY);
        return scheme(name);
    }

    private static <T extends Exception> void verify(final String name, final ExceptionFactory<T> exceptionFactory) throws T {
        if (name.isEmpty()) {
            throw exceptionFactory.makeException("Scheme must contain at least one character");
        }
        CharacterSetMembershipFunction.verify(ALPHA, name, "scheme", 0, 1, exceptionFactory);
        CharacterSetMembershipFunction.verify(TRAILING_CHARACTER_MEMBERSHIP_FUNCTION, name, "scheme", 1, exceptionFactory);
    }
}
