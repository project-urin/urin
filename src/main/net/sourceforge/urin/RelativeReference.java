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

import java.net.URI;

/**
 * A relative reference.
 * <p/>
 * A relative reference has a mandatory relative part component, that is made up of an optional authority, and a
 * mandatory path, and optional query and fragment parts.  The mandatory path may implicitly be the empty path.
 *
 * @see <a href="http://tools.ietf.org/html/rfc3986#section-4.2">RFC 3986 - Relative Reference</a>
 */
public abstract class RelativeReference extends UrinReference {

    RelativeReference() {
    }

    /**
     * Factory method for creating {@code RelativeReference}s with just an empty path.
     *
     * @return a {@code RelativeReference} with an empty path.
     * @deprecated use {@link net.sourceforge.urin.Scheme#relativeReference()}
     */
    public static RelativeReference relativeReference() {
        return anyScheme().relativeReference();
    }

    /**
     * Factory method for creating {@code RelativeReference}s with an authority and an empty path.
     *
     * @param authority any {@code Authority} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Authority} and an empty path.
     * @deprecated use {@link net.sourceforge.urin.Scheme#relativeReference(Authority)}
     */
    public static RelativeReference relativeReference(final Authority authority) {
        return anyScheme().relativeReference(authority);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with just a path.
     *
     * @param path any {@code Path} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Path}.
     * @deprecated use {@link net.sourceforge.urin.Scheme#relativeReference(Path)}
     */
    public static RelativeReference relativeReference(final Path path) {
        return anyScheme().relativeReference(path);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with an authority and a path.
     *
     * @param authority any {@code Authority} to use in this {@code RelativeReference}.
     * @param path      any {@code AbsolutePath} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Authority} and {@code Path}.
     * @deprecated use {@link net.sourceforge.urin.Scheme#relativeReference(Authority, AbsolutePath)}
     */
    public static RelativeReference relativeReference(final Authority authority, final AbsolutePath path) {
        return anyScheme().relativeReference(authority, path);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with a query and an empty path.
     *
     * @param query any {@code Query} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Query} and an empty path.
     * @deprecated use {@link net.sourceforge.urin.Scheme#relativeReference(Query)}
     */
    public static RelativeReference relativeReference(final Query query) {
        return anyScheme().relativeReference(query);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with an authority, a query and an empty path.
     *
     * @param authority any {@code Authority} to use in this {@code RelativeReference}.
     * @param query     any {@code Query} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Authority} and {@code Query}, and an empty path.
     * @deprecated use {@link net.sourceforge.urin.Scheme#relativeReference(Authority, Query)}
     */
    public static RelativeReference relativeReference(final Authority authority, final Query query) {
        return anyScheme().relativeReference(authority, query);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with a path and a query.
     *
     * @param path  any {@code Path} to use in this {@code RelativeReference}.
     * @param query any {@code Query} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Path} and {@code Query}.
     * @deprecated use {@link net.sourceforge.urin.Scheme#relativeReference(Path, Query)}
     */
    public static RelativeReference relativeReference(final Path path, final Query query) {
        return anyScheme().relativeReference(path, query);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with an authority, a path, and a query.
     *
     * @param authority any {@code Authority} to use in this {@code RelativeReference}.
     * @param path      any {@code Path} to use in this {@code RelativeReference}.
     * @param query     any {@code Query} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Authority}, {@code Path} and {@code Query}.
     * @deprecated use {@link net.sourceforge.urin.Scheme#relativeReference(Authority, AbsolutePath, Query)}
     */
    public static RelativeReference relativeReference(final Authority authority, final AbsolutePath path, final Query query) {
        return anyScheme().relativeReference(authority, path, query);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with a fragment and an empty path.
     *
     * @param fragment any {@code Fragment} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Fragment} and an empty path.
     * @deprecated use {@link net.sourceforge.urin.Scheme#relativeReference(Fragment)}
     */
    public static RelativeReference relativeReference(final Fragment fragment) {
        return anyScheme().relativeReference(fragment);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with an authority, a fragment, and an empty path.
     *
     * @param authority any {@code Authority} to use in this {@code RelativeReference}.
     * @param fragment  any {@code Fragment} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Authority} and {@code Fragment}, and an empty path.
     * @deprecated use {@link net.sourceforge.urin.Scheme#relativeReference(Authority, Fragment)}
     */
    public static RelativeReference relativeReference(final Authority authority, final Fragment fragment) {
        return anyScheme().relativeReference(authority, fragment);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with a path and a fragment.
     *
     * @param path     any {@code Path} to use in this {@code RelativeReference}.
     * @param fragment any {@code Fragment} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Path} and {@code Fragment}.
     * @deprecated use {@link net.sourceforge.urin.Scheme#relativeReference(Path, Fragment)}
     */
    public static RelativeReference relativeReference(final Path path, final Fragment fragment) {
        return anyScheme().relativeReference(path, fragment);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with an authority, a path, and fragment.
     *
     * @param authority any {@code Authority} to use in this {@code RelativeReference}.
     * @param path      any {@code AbsolutePath} to use in this {@code RelativeReference}.
     * @param fragment  any {@code Fragment} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Authority}, {@code Path} and {@code Fragment}.
     * @deprecated use {@link net.sourceforge.urin.Scheme#relativeReference(Authority, AbsolutePath, Fragment)}
     */
    public static RelativeReference relativeReference(final Authority authority, final AbsolutePath path, final Fragment fragment) {
        return anyScheme().relativeReference(authority, path, fragment);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with a query, a fragment, and an empty path.
     *
     * @param query    any {@code Query} to use in this {@code RelativeReference}.
     * @param fragment any {@code Fragment} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Query} and {@code Fragment}, and an empty path.
     * @deprecated use {@link net.sourceforge.urin.Scheme#relativeReference(Query, Fragment)}
     */
    public static RelativeReference relativeReference(final Query query, final Fragment fragment) {
        return anyScheme().relativeReference(query, fragment);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with an authority, a query, a fragment, and an empty path.
     *
     * @param authority any {@code Authority} to use in this {@code RelativeReference}.
     * @param query     any {@code Query} to use in this {@code RelativeReference}.
     * @param fragment  any {@code Fragment} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Authority}, {@code Query} and {@code Fragment}, and an empty path.
     * @deprecated use {@link net.sourceforge.urin.Scheme#relativeReference(Authority, Query, Fragment)
     */
    public static RelativeReference relativeReference(final Authority authority, final Query query, final Fragment fragment) {
        return anyScheme().relativeReference(authority, query, fragment);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with a path, a query, and a fragment.
     *
     * @param path     any {@code Path} to use in this {@code RelativeReference}.
     * @param query    any {@code Query} to use in this {@code RelativeReference}.
     * @param fragment any {@code Fragment} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Path}, {@code Query} and {@code Fragment}.
     * @deprecated use {@link net.sourceforge.urin.Scheme#relativeReference(Path, Query, Fragment)}
     */
    public static RelativeReference relativeReference(final Path path, final Query query, final Fragment fragment) {
        return anyScheme().relativeReference(path, query, fragment);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with an authority, a path, a query, and a fragment.
     *
     * @param authority any {@code Authority} to use in this {@code RelativeReference}.
     * @param path      any {@code AbsolutePath} to use in this {@code RelativeReference}.
     * @param query     any {@code Query} to use in this {@code RelativeReference}.
     * @param fragment  any {@code Fragment} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Authority}, {@code Path}, {@code Query} and {@code Fragment}.
     * @deprecated use {@link net.sourceforge.urin.Scheme#relativeReference(Authority, AbsolutePath, Query, Fragment)}
     */
    public static RelativeReference relativeReference(final Authority authority, final AbsolutePath path, final Query query, final Fragment fragment) {
        return anyScheme().relativeReference(authority, path, query, fragment);
    }

    /**
     * Parses the given {@code String} as a relative reference.
     *
     * @param relativeReferenceString a {@code String} that represents a relative reference.
     * @return a {@code UrinReference} representing the relative reference represented by the given {@code String}.
     * @throws ParseException if the given {@code String} is not a valid relative reference.
     * @deprecated use {@link net.sourceforge.urin.Scheme#parseRelativeReference(String)}
     */
    public static RelativeReference parse(final String relativeReferenceString) throws ParseException {
        return anyScheme().parseRelativeReference(relativeReferenceString);
    }

    /**
     * Parses the given {@code URI} to produce a {@code RelativeReference}.
     *
     * @param uri a {@code URI} representing a relative reference to parse.
     * @return a {@code RelativeReference} representing the RFC 3986 relative reference represented by the given {@code URI}.
     * @throws ParseException if the given {@code URI} is not a valid RFC 3986 relative reference.
     * @deprecated use {@link net.sourceforge.urin.Scheme#parseRelativeReference(java.net.URI)}
     */
    public static RelativeReference parse(URI uri) throws ParseException {
        return anyScheme().parseRelativeReference(uri);
    }

    /**
     * @deprecated use {@link net.sourceforge.urin.Scheme#isValidRelativeReferenceString(String)}
     */
    static boolean isValidRelativeReferenceString(final String uriReferenceString) {
        return Scheme.isValidRelativeReferenceString(uriReferenceString);
    }

    private static Scheme anyScheme() {
        return new Scheme() {
            @Override
            String asString() {
                return "";
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
                return this == o || !(o == null || getClass() != o.getClass());

            }

            @Override
            public int hashCode() {
                return 37;
            }

            @Override
            public String toString() {
                return "Scheme{}";
            }
        };
    }

}
