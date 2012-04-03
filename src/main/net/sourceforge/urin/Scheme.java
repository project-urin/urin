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
 * A name component of a URI.
 * Immutable and threadsafe.
 *
 * @see <a href="http://tools.ietf.org/html/rfc3986#section-3.1">RFC 3986 - Scheme</a>
 */
public abstract class Scheme {

    private static final CharacterSetMembershipFunction TRAILING_CHARACTER_MEMBERSHIP_FUNCTION = or(
            ALPHA_LOWERCASE,
            ALPHA_UPPERCASE,
            DIGIT,
            singleMemberCharacterSet('+'),
            singleMemberCharacterSet('-'),
            singleMemberCharacterSet('.')
    );

    private Scheme() {
        // deliberately empty
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
        return new SchemeWithNoDefaultPort(scheme.toLowerCase(ENGLISH));
    }

    /**
     * Factory method for creating {@code Scheme}s that have a default port.  Schemes must be at least one character long, and are permitted
     * to contain any character in the Latin alphabet, any digit, and any of the characters '+', '-', and '.'.
     * <p/>
     * An example of a name that defines a default port is http, which defaults to port 80, meaning {@code http://example.com} is equivalent to, and preferred to {@code http://example.com:80}.
     *
     * @param scheme      a {@code String} containing at least one character, made up of any characters in the Latin alphabet, the digits, and the characters '+', '-', and '.'.
     * @param defaultPort {@code Port} representing the default port for this name.
     * @return a {@code Scheme} representing the given {@code String}, using the given default port.
     * @throws IllegalArgumentException if the given {@code String} is empty or contains characters not in the Latin alphabet, the digits, or the characters '+', '-', and '.'.
     */
    public static Scheme scheme(final String scheme, final Port defaultPort) {
        verify(scheme, ExceptionFactory.ILLEGAL_ARGUMENT_EXCEPTION_EXCEPTION_FACTORY);
        return new SchemeWithDefaultPort(scheme.toLowerCase(ENGLISH), defaultPort);
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

    abstract String asString();

    abstract Authority normalise(final Authority authority);

    abstract Scheme removeDefaultPort();

    private static final class SchemeWithNoDefaultPort extends Scheme {
        private final String name;

        public SchemeWithNoDefaultPort(final String name) {
            this.name = name;
        }

        @Override
        String asString() {
            return name.toLowerCase(ENGLISH);
        }

        @Override
        Authority normalise(final Authority authority) {
            return authority;
        }

        @Override
        Scheme removeDefaultPort() {
            return this;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            SchemeWithNoDefaultPort that = (SchemeWithNoDefaultPort) o;

            return name.equals(that.name);

        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }

        @Override
        public String toString() {
            return "Scheme{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

    private static final class SchemeWithDefaultPort extends Scheme {
        private final String name;
        private final Port defaultPort;

        public SchemeWithDefaultPort(final String name, final Port defaultPort) {
            this.name = name;
            if (defaultPort == null) {
                throw new NullPointerException("Cannot instantiate Scheme with null default port");
            }
            this.defaultPort = defaultPort;
        }

        @Override
        String asString() {
            return name.toLowerCase(ENGLISH);
        }

        @Override
        Authority normalise(final Authority authority) {
            return authority.removePort(defaultPort);
        }

        @Override
        Scheme removeDefaultPort() {
            return scheme(name);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            SchemeWithDefaultPort that = (SchemeWithDefaultPort) o;

            return defaultPort.equals(that.defaultPort)
                    && name.equals(that.name);

        }

        @Override
        public int hashCode() {
            int result = name.hashCode();
            result = 31 * result + defaultPort.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "Scheme{" +
                    "name='" + name + '\'' +
                    ", defaultPort=" + defaultPort +
                    '}';
        }
    }
}
