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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A URI.
 * <p/>
 * RFC 3986 specifies that a URI is made up of mandatory scheme and hierarchical part components, and optional query
 * and fragment components.
 * <p/>
 * Immutable and threadsafe.
 *
 * @see <a href="http://tools.ietf.org/html/rfc3986#section-3">RFC 3986 - Syntax Components</a>
 */
public abstract class Urin extends UrinReference {

    private static final Pattern URI_PATTERN = Pattern.compile("^(([^:/?#]+):)((//([^/?#]*))?([^?#]*))(\\?([^#]*))?(#(.*))?");

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
    public abstract Urin resolve(final UrinReference urinReference);

    /**
     * Factory method for creating {@code Urin}s with just scheme and hierarchical part components.
     *
     * @param scheme           any {@code Scheme} to use in this {@code Urin}.
     * @param hierarchicalPart any {@code HierarchicalPart} to use in this {@code Urin}.
     * @return a {@code Urin} with the given {@code Scheme} and {@code HierarchicalPart}.
     * @deprecated use {@link net.sourceforge.urin.Scheme#urin(HierarchicalPart)}
     */
    public static Urin urin(final Scheme scheme, final HierarchicalPart hierarchicalPart) {
        return scheme.urin(hierarchicalPart);
    }

    /**
     * Factory method for creating {@code Urin}s with scheme, hierarchical part, and fragment components.
     *
     * @param scheme           any {@code Scheme} to use in this {@code Urin}.
     * @param hierarchicalPart any {@code HierarchicalPart} to use in this {@code Urin}.
     * @param fragment         any {@code Fragment} to use in this {@code Urin}.
     * @return a {@code Urin} with the given {@code Scheme}, {@code HierarchicalPart}, and {@code Fragment}.
     * @deprecated use {@link net.sourceforge.urin.Scheme#urin(HierarchicalPart, Fragment)}
     */
    public static Urin urin(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Fragment fragment) {
        return scheme.urin(hierarchicalPart, fragment);
    }

    /**
     * Factory method for creating {@code Urin}s with scheme, hierarchical part, and query components.
     *
     * @param scheme           any {@code Scheme} to use in this {@code Urin}.
     * @param hierarchicalPart any {@code HierarchicalPart} to use in this {@code Urin}.
     * @param query            any {@code Query} to use in this {@code Urin}.
     * @return a {@code Urin} with the given {@code Scheme}, {@code HierarchicalPart}, and {@code Query}.
     * @deprecated use {@link net.sourceforge.urin.Scheme#urin(HierarchicalPart, Query)}
     */
    public static Urin urin(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query) {
        return scheme.urin(hierarchicalPart, query);
    }

    /**
     * Factory method for creating {@code Urin}s with scheme, hierarchical part, query, and fragment components.
     *
     * @param scheme           any {@code Scheme} to use in this {@code Urin}.
     * @param hierarchicalPart any {@code HierarchicalPart} to use in this {@code Urin}.
     * @param query            any {@code Query} to use in this {@code Urin}.
     * @param fragment         any {@code Fragment} to use in this {@code Urin}.
     * @return a {@code Urin} with the given {@code Scheme}, {@code HierarchicalPart}, {@code Query}, and {@code Fragment}.
     * @deprecated use {@link net.sourceforge.urin.Scheme#urin(HierarchicalPart, Query, Fragment)}
     */
    public static Urin urin(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query, final Fragment fragment) {
        return scheme.urin(hierarchicalPart, query, fragment);
    }

    /**
     * Parses the given {@code String} as a URI.
     *
     * @param uriString a {@code String} that represents a URI.
     * @return a {@code Urin} representing the URI represented by the given {@code String}.
     * @throws ParseException if the given {@code String} is not a valid URI.
     */
    public static Urin parse(final String uriString) throws ParseException {
        final Matcher matcher = URI_PATTERN.matcher(uriString);
        matcher.matches();
        final Scheme scheme = Scheme.parse(matcher.group(2));
        final HierarchicalPart hierarchicalPart = HierarchicalPart.parse(matcher.group(3));
        final String queryString = matcher.group(8);
        final String fragment = matcher.group(10);
        final Urin result;
        if (queryString == null) {
            if (fragment == null) {
                result = urin(
                        scheme,
                        hierarchicalPart
                );
            } else {
                result = urin(
                        scheme,
                        hierarchicalPart,
                        Fragment.parse(fragment)
                );
            }
        } else {
            final Query query = Query.parse(queryString);
            if (fragment == null) {
                result = urin(
                        scheme,
                        hierarchicalPart,
                        query
                );
            } else {
                result = urin(
                        scheme,
                        hierarchicalPart,
                        query,
                        Fragment.parse(fragment)
                );
            }
        }
        return result;
    }

    /**
     * Parses the given {@code URI} to produce a {@code Urin}.
     *
     * @param uri a {@code URI} to parse.
     * @return a {@code Urin} representing the RFC 3986 URI represented by the given {@code URI}.
     * @throws ParseException if the given {@code URI} is not a valid RFC 3986 URI.
     */
    public static Urin parse(final URI uri) throws ParseException {
        return parse(uri.toASCIIString());
    }

    static boolean isValidUrinString(final String uriReferenceString) {
        return URI_PATTERN.matcher(uriReferenceString).matches();
    }

    @Override
    final Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart) {
        return this;
    }

    @Override
    final Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query) {
        return this;
    }

    @Override
    final Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query, final Fragment fragment) {
        return this;
    }

}
