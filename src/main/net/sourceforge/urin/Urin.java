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

/**
 * A URI.
 * <p/>
 * RFC 3986 specifies that a URI is made up of mandatory scheme and hierarchical part components, and optional query
 * and fragment components.
 * <p/>
 * Immutable and threadsafe.
 *
 * @param <QUERY> The type of {@code Query} used by this URI.
 * @see <a href="http://tools.ietf.org/html/rfc3986#section-3">RFC 3986 - Syntax Components</a>
 */
public abstract class Urin<SEGMENT, QUERY extends Query> extends UrinReference<SEGMENT, QUERY> {

    Urin() {
        // deliberately empty
    }

    /**
     * Resolves the given {@code UrinReference} relative to this.
     *
     * @param urinReference a {@code UrinReference} to resolve relative to this.
     * @return the {@code Urin} resulting from resolving the give {@code UrinReference} relative to this.
     * @see <a href="http://tools.ietf.org/html/rfc3986#section-5">RFC 3986 - Reference Resolution</a>
     */
    public abstract Urin resolve(final UrinReference<SEGMENT, QUERY> urinReference);

    @Override
    Urin<SEGMENT, QUERY> resolve(final Scheme scheme, final Path path) {
        return this;
    }

    @Override
    Urin<SEGMENT, QUERY> resolve(final Scheme scheme, final Authority authority, final Path path) {
        return this;
    }

    @Override
    Urin<SEGMENT, QUERY> resolve(final Scheme scheme, final Path path, final Query query) {
        return this;
    }

    @Override
    Urin<SEGMENT, QUERY> resolve(final Scheme scheme, final Authority authority, final Path path, final Query query) {
        return this;
    }

    @Override
    Urin<SEGMENT, QUERY> resolve(final Scheme scheme, final Path path, final Query query, final Fragment fragment) {
        return this;
    }

    @Override
    Urin<SEGMENT, QUERY> resolve(final Scheme scheme, final Authority authority, final Path path, final Query query, final Fragment fragment) {
        return this;
    }

}
