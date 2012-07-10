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
import static net.sourceforge.urin.Path.PrefixWithDotSegmentCriteria.NEVER_PREFIX_WITH_DOT_SEGMENT;
import static net.sourceforge.urin.Path.PrefixWithDotSegmentCriteria.PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON;
import static net.sourceforge.urin.Urin.urin;

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

    Scheme() {
        // deliberately empty
    }

    /**
     * Factory method for creating {@code Scheme}s.  Schemes must be at least one character long, and are permitted
     * to contain any character in the Latin alphabet, any digit, and any of the characters '+', '-', and '.'.
     *
     * @param name a {@code String} containing at least one character, made up of any characters in the Latin alphabet, the digits, and the characters '+', '-', and '.'.
     * @return a {@code Scheme} representing the given {@code String}.
     * @throws IllegalArgumentException if the given {@code String} is empty or contains characters not in the Latin alphabet, the digits, or the characters '+', '-', and '.'.
     */
    public static Scheme scheme(final String name) {
        verify(name, ExceptionFactory.ILLEGAL_ARGUMENT_EXCEPTION_EXCEPTION_FACTORY);
        return new GenericScheme(name.toLowerCase(ENGLISH));
    }

    /**
     * Factory method for creating {@code Scheme}s that have a default port.  Schemes must be at least one character long, and are permitted
     * to contain any character in the Latin alphabet, any digit, and any of the characters '+', '-', and '.'.
     * <p/>
     * An example of a name that defines a default port is http, which defaults to port 80, meaning {@code http://example.com} is equivalent to, and preferred to {@code http://example.com:80}.
     *
     * @param name        a {@code String} containing at least one character, made up of any characters in the Latin alphabet, the digits, and the characters '+', '-', and '.'.
     * @param defaultPort {@code Port} representing the default port for this name.
     * @return a {@code Scheme} representing the given {@code String}, using the given default port.
     * @throws IllegalArgumentException if the given {@code String} is empty or contains characters not in the Latin alphabet, the digits, or the characters '+', '-', and '.'.
     */
    public static Scheme scheme(final String name, final Port defaultPort) {
        verify(name, ExceptionFactory.ILLEGAL_ARGUMENT_EXCEPTION_EXCEPTION_FACTORY);
        return new SchemeWithDefaultPort(name.toLowerCase(ENGLISH), defaultPort);
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

    /**
     * Factory method for creating {@code RelativeReference}s with just an empty path.
     *
     * @return a {@code RelativeReference} with an empty path.
     */
    public final RelativeReference relativeReference() {
        return new RelativeReferenceNoAuthority(new EmptyPath());
    }

    /**
     * Factory method for creating {@code RelativeReference}s with an authority and an empty path.
     *
     * @param authority any {@code Authority} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Authority} and an empty path.
     */
    public final RelativeReference relativeReference(final Authority authority) {
        return new RelativeReferenceWithAuthority(authority, new EmptyPath());
    }

    /**
     * Factory method for creating {@code RelativeReference}s with just a path.
     *
     * @param path any {@code Path} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Path}.
     */
    public final RelativeReference relativeReference(final Path path) {
        return new RelativeReferenceNoAuthority(path);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with an authority and a path.
     *
     * @param authority any {@code Authority} to use in this {@code RelativeReference}.
     * @param path      any {@code AbsolutePath} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Authority} and {@code Path}.
     */
    public final RelativeReference relativeReference(final Authority authority, final AbsolutePath path) {
        return new RelativeReferenceWithAuthority(authority, path);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with a query and an empty path.
     *
     * @param query any {@code Query} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Query} and an empty path.
     */
    public final RelativeReference relativeReference(final Query query) {
        return new RelativeReferenceNoAuthorityWithQuery(new EmptyPath(), query);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with an authority, a query and an empty path.
     *
     * @param authority any {@code Authority} to use in this {@code RelativeReference}.
     * @param query     any {@code Query} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Authority} and {@code Query}, and an empty path.
     */
    public final RelativeReference relativeReference(final Authority authority, final Query query) {
        return new RelativeReferenceWithAuthorityAndQuery(authority, new EmptyPath(), query);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with a path and a query.
     *
     * @param path  any {@code Path} to use in this {@code RelativeReference}.
     * @param query any {@code Query} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Path} and {@code Query}.
     */
    public final RelativeReference relativeReference(final Path path, final Query query) {
        return new RelativeReferenceNoAuthorityWithQuery(path, query);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with an authority, a path, and a query.
     *
     * @param authority any {@code Authority} to use in this {@code RelativeReference}.
     * @param path      any {@code Path} to use in this {@code RelativeReference}.
     * @param query     any {@code Query} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Authority}, {@code Path} and {@code Query}.
     */
    public final RelativeReference relativeReference(final Authority authority, final AbsolutePath path, final Query query) {
        return new RelativeReferenceWithAuthorityAndQuery(authority, path, query);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with a fragment and an empty path.
     *
     * @param fragment any {@code Fragment} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Fragment} and an empty path.
     */
    public final RelativeReference relativeReference(final Fragment fragment) {
        return new RelativeReferenceNoAuthorityWithFragment(new EmptyPath(), fragment);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with an authority, a fragment, and an empty path.
     *
     * @param authority any {@code Authority} to use in this {@code RelativeReference}.
     * @param fragment  any {@code Fragment} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Authority} and {@code Fragment}, and an empty path.
     */
    public final RelativeReference relativeReference(final Authority authority, final Fragment fragment) {
        return new RelativeReferenceWithAuthorityAndFragment(authority, new EmptyPath(), fragment);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with a path and a fragment.
     *
     * @param path     any {@code Path} to use in this {@code RelativeReference}.
     * @param fragment any {@code Fragment} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Path} and {@code Fragment}.
     */
    public final RelativeReference relativeReference(final Path path, final Fragment fragment) {
        return new RelativeReferenceNoAuthorityWithFragment(path, fragment);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with an authority, a path, and fragment.
     *
     * @param authority any {@code Authority} to use in this {@code RelativeReference}.
     * @param path      any {@code AbsolutePath} to use in this {@code RelativeReference}.
     * @param fragment  any {@code Fragment} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Authority}, {@code Path} and {@code Fragment}.
     */
    public final RelativeReference relativeReference(final Authority authority, final AbsolutePath path, final Fragment fragment) {
        return new RelativeReferenceWithAuthorityAndFragment(authority, path, fragment);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with a query, a fragment, and an empty path.
     *
     * @param query    any {@code Query} to use in this {@code RelativeReference}.
     * @param fragment any {@code Fragment} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Query} and {@code Fragment}, and an empty path.
     */
    public final RelativeReference relativeReference(final Query query, final Fragment fragment) {
        return new RelativeReferenceNoAuthorityWithQueryAndFragment(new EmptyPath(), query, fragment);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with an authority, a query, a fragment, and an empty path.
     *
     * @param authority any {@code Authority} to use in this {@code RelativeReference}.
     * @param query     any {@code Query} to use in this {@code RelativeReference}.
     * @param fragment  any {@code Fragment} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Authority}, {@code Query} and {@code Fragment}, and an empty path.
     */
    public final RelativeReference relativeReference(final Authority authority, final Query query, final Fragment fragment) {
        return new RelativeReferenceWithAuthorityAndQueryAndFragment(authority, new EmptyPath(), query, fragment);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with a path, a query, and a fragment.
     *
     * @param path     any {@code Path} to use in this {@code RelativeReference}.
     * @param query    any {@code Query} to use in this {@code RelativeReference}.
     * @param fragment any {@code Fragment} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Path}, {@code Query} and {@code Fragment}.
     */
    public final RelativeReference relativeReference(final Path path, final Query query, final Fragment fragment) {
        return new RelativeReferenceNoAuthorityWithQueryAndFragment(path, query, fragment);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with an authority, a path, a query, and a fragment.
     *
     * @param authority any {@code Authority} to use in this {@code RelativeReference}.
     * @param path      any {@code AbsolutePath} to use in this {@code RelativeReference}.
     * @param query     any {@code Query} to use in this {@code RelativeReference}.
     * @param fragment  any {@code Fragment} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Authority}, {@code Path}, {@code Query} and {@code Fragment}.
     */
    public final RelativeReference relativeReference(final Authority authority, final AbsolutePath path, final Query query, final Fragment fragment) {
        return new RelativeReferenceWithAuthorityAndQueryAndFragment(authority, path, query, fragment);
    }

    public static final class GenericScheme extends Scheme {
        private final String name;

        public GenericScheme(final String name) {
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

            GenericScheme that = (GenericScheme) o;

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

    private static final class RelativeReferenceNoAuthority extends RelativeReference {
        private final Path path;

        private RelativeReferenceNoAuthority(final Path path) {
            if (path == null) {
                throw new NullPointerException("Cannot instantiate RelativeReference with null path");
            }
            this.path = path;
        }

        @Override
        public String asString() {
            return path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON);
        }

        @Override
        public Path path() {
            return path;
        }

        @Override
        public boolean hasFragment() {
            return false;
        }

        @Override
        public Fragment fragment() {
            throw new UnsupportedOperationException("Attempt to get fragment from a UrinReference that does not have one.");
        }

        @Override
        public boolean hasQuery() {
            return false;
        }

        @Override
        public Query query() {
            throw new UnsupportedOperationException("Attempt to get query from a UrinReference that does not have one.");
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart) {
            return urin(scheme, hierarchicalPart.resolve(path));
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query) {
            if (path.isEmpty()) {
                return urin(scheme, hierarchicalPart.resolve(path), query);
            } else {
                return urin(scheme, hierarchicalPart.resolve(path));
            }
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query, final Fragment fragment) {
            if (path.isEmpty()) {
                return urin(scheme, hierarchicalPart.resolve(path), query, fragment);
            } else {
                return urin(scheme, hierarchicalPart.resolve(path));
            }
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            RelativeReferenceNoAuthority that = (RelativeReferenceNoAuthority) o;
            return path.equals(that.path);
        }

        @Override
        public int hashCode() {
            return path.hashCode();
        }

        @Override
        public String toString() {
            return "RelativeReference{" +
                    "path=" + path +
                    '}';
        }
    }

    private static final class RelativeReferenceWithAuthority extends RelativeReference {
        private final Authority authority;
        private final Path path;

        private RelativeReferenceWithAuthority(final Authority authority, final Path path) {
            if (authority == null) {
                throw new NullPointerException("Cannot instantiate RelativeReference with null authority");
            }
            this.authority = authority;
            if (path == null) {
                throw new NullPointerException("Cannot instantiate RelativeReference with null path");
            }
            this.path = path;
        }

        @Override
        public String asString() {
            return "//" + authority.asString() + path.asString(NEVER_PREFIX_WITH_DOT_SEGMENT);
        }

        @Override
        public Path path() {
            return path;
        }

        @Override
        public boolean hasFragment() {
            return false;
        }

        @Override
        public Fragment fragment() {
            throw new UnsupportedOperationException("Attempt to get fragment from a UrinReference that does not have one.");
        }

        @Override
        public boolean hasQuery() {
            return false;
        }

        @Override
        public Query query() {
            throw new UnsupportedOperationException("Attempt to get query from a UrinReference that does not have one.");
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart) {
            return urin(scheme, hierarchicalPart.resolve(authority, path));
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query) {
            return urin(scheme, hierarchicalPart.resolve(authority, path));
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query, final Fragment fragment) {
            return urin(scheme, hierarchicalPart.resolve(authority, path));
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            RelativeReferenceWithAuthority that = (RelativeReferenceWithAuthority) o;
            return authority.equals(that.authority)
                    && path.equals(that.path);
        }

        @Override
        public int hashCode() {
            int result = authority.hashCode();
            result = 31 * result + path.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "RelativeReference{" +
                    "authority=" + authority +
                    ", path=" + path +
                    '}';
        }
    }

    private static final class RelativeReferenceNoAuthorityWithQuery extends RelativeReference {
        private final Path path;
        private final Query query;

        RelativeReferenceNoAuthorityWithQuery(final Path path, final Query query) {
            if (path == null) {
                throw new NullPointerException("Cannot instantiate RelativeReference with null path");
            }
            this.path = path;
            if (query == null) {
                throw new NullPointerException("Cannot instantiate RelativeReference with null query");
            }
            this.query = query;
        }

        @Override
        public String asString() {
            return path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON) + '?' + query.asString();
        }

        @Override
        public Path path() {
            return path;
        }

        @Override
        public boolean hasFragment() {
            return false;
        }

        @Override
        public Fragment fragment() {
            throw new UnsupportedOperationException("Attempt to get fragment from a UrinReference that does not have one.");
        }

        @Override
        public boolean hasQuery() {
            return true;
        }

        @Override
        public Query query() {
            return query;
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart) {
            return urin(scheme, hierarchicalPart.resolve(path), query);
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query) {
            return urin(scheme, hierarchicalPart.resolve(path), this.query);
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query, final Fragment fragment) {
            if (path.isEmpty()) {
                return urin(scheme, hierarchicalPart.resolve(path), this.query, fragment);
            } else {
                return urin(scheme, hierarchicalPart.resolve(path), this.query);
            }
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            RelativeReferenceNoAuthorityWithQuery that = (RelativeReferenceNoAuthorityWithQuery) o;

            return query.equals(that.query) && path.equals(that.path);

        }

        @Override
        public int hashCode() {
            int result = path.hashCode();
            result = 31 * result + query.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "RelativeReference{" +
                    "path=" + path +
                    ", query=" + query +
                    '}';
        }
    }

    private static final class RelativeReferenceNoAuthorityWithFragment extends RelativeReference {
        private final Path path;
        private final Fragment fragment;

        RelativeReferenceNoAuthorityWithFragment(final Path path, final Fragment fragment) {
            if (path == null) {
                throw new NullPointerException("Cannot instantiate RelativeReference with null path");
            }
            this.path = path;
            if (fragment == null) {
                throw new NullPointerException("Cannot instantiate RelativeReference with null fragment");
            }
            this.fragment = fragment;
        }

        @Override
        public String asString() {
            return path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON) + '#' + fragment.asString();
        }

        @Override
        public Path path() {
            return path;
        }

        @Override
        public boolean hasFragment() {
            return true;
        }

        @Override
        public Fragment fragment() {
            return fragment;
        }

        @Override
        public boolean hasQuery() {
            return false;
        }

        @Override
        public Query query() {
            throw new UnsupportedOperationException("Attempt to get query from a UrinReference that does not have one.");
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart) {
            return urin(scheme, hierarchicalPart.resolve(path), fragment);
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query) {
            if (path.isEmpty()) {
                return urin(scheme, hierarchicalPart.resolve(path), query, fragment);
            } else {
                return urin(scheme, hierarchicalPart.resolve(path), fragment);
            }
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query, final Fragment fragment) {
            if (path.isEmpty()) {
                return urin(scheme, hierarchicalPart.resolve(path), query, this.fragment);
            } else {
                return urin(scheme, hierarchicalPart.resolve(path), this.fragment);
            }
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            RelativeReferenceNoAuthorityWithFragment that = (RelativeReferenceNoAuthorityWithFragment) o;

            return fragment.equals(that.fragment) && path.equals(that.path);

        }

        @Override
        public int hashCode() {
            int result = path.hashCode();
            result = 31 * result + fragment.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "RelativeReference{" +
                    "path=" + path +
                    ", fragment=" + fragment +
                    '}';
        }
    }

    private static final class RelativeReferenceNoAuthorityWithQueryAndFragment extends RelativeReference {
        private final Path path;
        private final Fragment fragment;
        private final Query query;

        RelativeReferenceNoAuthorityWithQueryAndFragment(final Path path, final Query query, final Fragment fragment) {
            if (path == null) {
                throw new NullPointerException("Cannot instantiate RelativeReference with null path");
            }
            this.path = path;
            if (query == null) {
                throw new NullPointerException("Cannot instantiate RelativeReference with null query");
            }
            this.query = query;
            if (fragment == null) {
                throw new NullPointerException("Cannot instantiate RelativeReference with null fragment");
            }
            this.fragment = fragment;
        }

        @Override
        public String asString() {
            return path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON) + '?' + query.asString() + '#' + fragment.asString();
        }

        @Override
        public Path path() {
            return path;
        }

        @Override
        public boolean hasFragment() {
            return true;
        }

        @Override
        public Fragment fragment() {
            return fragment;
        }

        @Override
        public boolean hasQuery() {
            return true;
        }

        @Override
        public Query query() {
            return query;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            RelativeReferenceNoAuthorityWithQueryAndFragment that = (RelativeReferenceNoAuthorityWithQueryAndFragment) o;

            return fragment.equals(that.fragment) && query.equals(that.query) && path.equals(that.path);
        }

        @Override
        public int hashCode() {
            int result = path.hashCode();
            result = 31 * result + fragment.hashCode();
            result = 31 * result + query.hashCode();
            return result;
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart) {
            return urin(scheme, hierarchicalPart.resolve(path), query, fragment);
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query) {
            return urin(scheme, hierarchicalPart.resolve(path), this.query, fragment);
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query, final Fragment fragment) {
            return urin(scheme, hierarchicalPart.resolve(path), this.query, this.fragment);
        }

        @Override
        public String toString() {
            return "RelativeReference{" +
                    "path=" + path +
                    ", query=" + query +
                    ", fragment=" + fragment +
                    '}';
        }
    }

    private static final class RelativeReferenceWithAuthorityAndQuery extends RelativeReference {
        private final Authority authority;
        private final Path path;
        private final Query query;

        RelativeReferenceWithAuthorityAndQuery(final Authority authority, final Path path, final Query query) {
            if (authority == null) {
                throw new NullPointerException("Cannot instantiate RelativeReference with null authority");
            }
            this.authority = authority;
            if (path == null) {
                throw new NullPointerException("Cannot instantiate RelativeReference with null path");
            }
            this.path = path;
            if (query == null) {
                throw new NullPointerException("Cannot instantiate RelativeReference with null query");
            }
            this.query = query;
        }

        @Override
        public String asString() {
            return "//" + authority.asString() + path.asString(NEVER_PREFIX_WITH_DOT_SEGMENT) + '?' + query.asString();
        }

        @Override
        public Path path() {
            return path;
        }

        @Override
        public boolean hasFragment() {
            return false;
        }

        @Override
        public Fragment fragment() {
            throw new UnsupportedOperationException("Attempt to get fragment from a UrinReference that does not have one.");
        }

        @Override
        public boolean hasQuery() {
            return true;
        }

        @Override
        public Query query() {
            return query;
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart) {
            return urin(scheme, hierarchicalPart.resolve(authority, path), query);
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query) {
            return urin(scheme, hierarchicalPart.resolve(authority, path), this.query);
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query, final Fragment fragment) {
            return urin(scheme, hierarchicalPart.resolve(authority, path), this.query);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            RelativeReferenceWithAuthorityAndQuery that = (RelativeReferenceWithAuthorityAndQuery) o;

            return authority.equals(that.authority) && query.equals(that.query) && path.equals(that.path);
        }

        @Override
        public int hashCode() {
            int result = authority.hashCode();
            result = 31 * result + path.hashCode();
            result = 31 * result + query.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "RelativeReference{" +
                    "authority=" + authority +
                    ", path=" + path +
                    ", query=" + query +
                    '}';
        }
    }

    private static final class RelativeReferenceWithAuthorityAndFragment extends RelativeReference {
        private final Authority authority;
        private final Path path;
        private final Fragment fragment;

        RelativeReferenceWithAuthorityAndFragment(final Authority authority, final Path path, final Fragment fragment) {
            if (authority == null) {
                throw new NullPointerException("Cannot instantiate RelativeReference with null authority");
            }
            this.authority = authority;
            if (path == null) {
                throw new NullPointerException("Cannot instantiate RelativeReference with null path");
            }
            this.path = path;
            if (fragment == null) {
                throw new NullPointerException("Cannot instantiate RelativeReference with null fragment");
            }
            this.fragment = fragment;
        }

        @Override
        public String asString() {
            return "//" + authority.asString() + path.asString(NEVER_PREFIX_WITH_DOT_SEGMENT) + '#' + fragment.asString();
        }

        @Override
        public Path path() {
            return path;
        }

        @Override
        public boolean hasFragment() {
            return true;
        }

        @Override
        public Fragment fragment() {
            return fragment;
        }

        @Override
        public boolean hasQuery() {
            return false;
        }

        @Override
        public Query query() {
            throw new UnsupportedOperationException("Attempt to get query from a UrinReference that does not have one.");
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart) {
            return urin(scheme, hierarchicalPart.resolve(authority, path), fragment);
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query) {
            return urin(scheme, hierarchicalPart.resolve(authority, path), fragment);
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query, final Fragment fragment) {
            return urin(scheme, hierarchicalPart.resolve(authority, path), this.fragment);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            RelativeReferenceWithAuthorityAndFragment that = (RelativeReferenceWithAuthorityAndFragment) o;

            return authority.equals(that.authority) && fragment.equals(that.fragment) && path.equals(that.path);
        }

        @Override
        public int hashCode() {
            int result = authority.hashCode();
            result = 31 * result + path.hashCode();
            result = 31 * result + fragment.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "RelativeReference{" +
                    "authority=" + authority +
                    ", path=" + path +
                    ", fragment=" + fragment +
                    '}';
        }
    }

    private static final class RelativeReferenceWithAuthorityAndQueryAndFragment extends RelativeReference {
        private final Authority authority;
        private final Path path;
        private final Query query;
        private final Fragment fragment;

        RelativeReferenceWithAuthorityAndQueryAndFragment(final Authority authority, final Path path, final Query query, final Fragment fragment) {
            if (authority == null) {
                throw new NullPointerException("Cannot instantiate RelativeReference with null authority");
            }
            this.authority = authority;
            if (path == null) {
                throw new NullPointerException("Cannot instantiate RelativeReference with null path");
            }
            this.path = path;
            if (query == null) {
                throw new NullPointerException("Cannot instantiate RelativeReference with null query");
            }
            this.query = query;
            if (fragment == null) {
                throw new NullPointerException("Cannot instantiate RelativeReference with null fragment");
            }
            this.fragment = fragment;
        }

        @Override
        public String asString() {
            return "//" + authority.asString() + path.asString(NEVER_PREFIX_WITH_DOT_SEGMENT) + '?' + query.asString() + '#' + fragment.asString();
        }

        @Override
        public Path path() {
            return path;
        }

        @Override
        public boolean hasFragment() {
            return true;
        }

        @Override
        public Fragment fragment() {
            return fragment;
        }

        @Override
        public boolean hasQuery() {
            return true;
        }

        @Override
        public Query query() {
            return query;
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart) {
            return urin(scheme, hierarchicalPart.resolve(authority, path), query, fragment);
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query) {
            return urin(scheme, hierarchicalPart.resolve(authority, path), this.query, fragment);
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query, final Fragment fragment) {
            return urin(scheme, hierarchicalPart.resolve(authority, path), this.query, this.fragment);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            RelativeReferenceWithAuthorityAndQueryAndFragment that = (RelativeReferenceWithAuthorityAndQueryAndFragment) o;

            return authority.equals(that.authority) && fragment.equals(that.fragment) && query.equals(that.query) && path.equals(that.path);
        }

        @Override
        public int hashCode() {
            int result = authority.hashCode();
            result = 31 * result + path.hashCode();
            result = 31 * result + query.hashCode();
            result = 31 * result + fragment.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "RelativeReference{" +
                    "authority=" + authority +
                    ", path=" + path +
                    ", query=" + query +
                    ", fragment=" + fragment +
                    '}';
        }
    }
}
