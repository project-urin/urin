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
 * A URI reference - either a URI or a relative reference.
 * Immutable and threadsafe.
 *
 * @see <a href="http://tools.ietf.org/html/rfc3986#section-4.1">RFC 3986 - URI Reference</a>
 */
public abstract class UrinReference {

    UrinReference() {
        // deliberately empty
    }

    static Scheme anyScheme() {
        return new Scheme() {
            @Override
            Scheme withName(final String name) {
                return scheme(name);
            }

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

    /**
     * Generates the {@code String} representation of this URI reference.
     *
     * @return an RFC 3986 compliant {@code String} representation of this URI reference.
     */
    public abstract String asString();

    /**
     * Generates a {@code URI} representation of this URI reference.
     * <p/>
     * Note that this class represents URI references as defined in the current RFC 3986, but Java's {@code URI} class
     * represents URI references as defined in the obsolete RFC 2396.  RFC 3986 extends the definition of URI
     * references, so this class can represent URI references that cannot be represented by Java's {@code URI} class.
     *
     * @return a {@code URI} representation of this {@code UrinReference}.
     * @throws IllegalArgumentException if Java's {@code URI} class is unable to represent this URI reference.
     */
    public final URI asUri() {
        return URI.create(asString());
    }

    /**
     * Returns the path component of the URI reference this represents.  All URI references have a path, though this maybe
     * the empty path, and in some cases, the path is implicitly the empty path.
     *
     * @return a {@code Path} representing the path component of this URI reference.
     */
    public abstract Path path();

    /**
     * Returns true if {@code fragment()} can be called on this {@code UrinReference}.  This method
     * returns false for {@code UrinReference}s that do not have a fragment component.
     *
     * @return true if {@code fragment()} can be called on this {@code UrinReference}.
     */
    public abstract boolean hasFragment();

    /**
     * Gets the {@code Fragment} component of this {@code UrinReference}, if it has one, or throws {@code UnsupportedOperationException} otherwise.
     * <p/>
     * The existence of a {@code Fragment} component can be tested by calling {@code hasFragment()}.
     *
     * @return the {@code Fragment} component of this {@code UrinReference}.
     * @throws UnsupportedOperationException if this is a {@code UrinReference} that does not have a {@code Fragment} component.
     */
    public abstract Fragment fragment();

    /**
     * Returns true if {@code query()} can be called on this {@code UrinReference}.  This method
     * returns false for {@code UrinReference}s that do not have a query component.
     *
     * @return true if {@code query()} can be called on this {@code UrinReference}.
     */
    public abstract boolean hasQuery();

    /**
     * Gets the {@code Query} component of this {@code UrinReference}, if it has one, or throws {@code UnsupportedOperationException} otherwise.
     * <p/>
     * The existence of a {@code Query} component can be tested by calling {@code hasQuery()}.
     *
     * @return the {@code Query} component of this {@code UrinReference}.
     * @throws UnsupportedOperationException if this is a {@code UrinReference} that does not have a {@code Query} component.
     */
    public abstract Query query();

    abstract Urin resolve(final Scheme scheme, final Path path);

    abstract Urin resolve(final Scheme scheme, final Authority authority, final Path path);

    abstract Urin resolve(final Scheme scheme, final Path path, final Query query);

    abstract Urin resolve(final Scheme scheme, final Authority authority, final Path path, final Query query);

    abstract Urin resolve(final Scheme scheme, final Path path, final Query query, final Fragment fragment);

    abstract Urin resolve(final Scheme scheme, final Authority authority, final Path path, final Query query, final Fragment fragment);

}
