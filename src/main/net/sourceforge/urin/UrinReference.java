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
     * Parses the given {@code String} as a URI reference.
     *
     * @param uriReferenceString a {@code String} that represents a URI reference.
     * @return a {@code UrinReference} representing the URI reference represented by the given {@code String}.
     * @throws ParseException if the given {@code String} is not a valid URI reference.
     */
    public static UrinReference parse(final String uriReferenceString) throws ParseException {
        if (Urin.isValidUrinString(uriReferenceString)) {
            return Urin.parse(uriReferenceString);
        } else if (RelativeReference.isValidRelativeReferenceString(uriReferenceString)) {
            return RelativeReference.parse(uriReferenceString);
        }
        throw new ParseException("Given String is neither a valid URI nor a valid relative reference [" + uriReferenceString + "].");
    }

    /**
     * Parses the given {@code URI} to produce a {@code UrinReference}.
     *
     * @param uriReference a {@code URI} to parse.
     * @return a {@code UrinReference} representing the RFC 3986 URI reference represented by the given {@code URI}.
     * @throws ParseException if the given {@code URI} is not a valid RFC 3986 URI reference.
     */
    public static UrinReference parse(final URI uriReference) throws ParseException {
        return parse(uriReference.toASCIIString());
    }

    abstract Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart);

    abstract Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query);

    abstract Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query, final Fragment fragment);
}
