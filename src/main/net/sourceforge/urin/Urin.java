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

    private Urin() {
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
     */
    public static Urin urin(final Scheme scheme, final HierarchicalPart hierarchicalPart) {
        return new UrinWithHierarchicalPart(scheme.removeDefaultPort(), hierarchicalPart.normalisePort(scheme));
    }

    /**
     * Factory method for creating {@code Urin}s with scheme, hierarchical part, and fragment components.
     *
     * @param scheme           any {@code Scheme} to use in this {@code Urin}.
     * @param hierarchicalPart any {@code HierarchicalPart} to use in this {@code Urin}.
     * @param fragment         any {@code Fragment} to use in this {@code Urin}.
     * @return a {@code Urin} with the given {@code Scheme}, {@code HierarchicalPart}, and {@code Fragment}.
     */
    public static Urin urin(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Fragment fragment) {
        return new UrinWithHierarchicalPartAndFragment(scheme.removeDefaultPort(), hierarchicalPart.normalisePort(scheme), fragment);
    }

    /**
     * Factory method for creating {@code Urin}s with scheme, hierarchical part, and query components.
     *
     * @param scheme           any {@code Scheme} to use in this {@code Urin}.
     * @param hierarchicalPart any {@code HierarchicalPart} to use in this {@code Urin}.
     * @param query            any {@code Query} to use in this {@code Urin}.
     * @return a {@code Urin} with the given {@code Scheme}, {@code HierarchicalPart}, and {@code Query}.
     */
    public static Urin urin(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query) {
        return new UrinWithHierarchicalPartAndQuery(scheme.removeDefaultPort(), hierarchicalPart.normalisePort(scheme), query);
    }

    /**
     * Factory method for creating {@code Urin}s with scheme, hierarchical part, query, and fragment components.
     *
     * @param scheme           any {@code Scheme} to use in this {@code Urin}.
     * @param hierarchicalPart any {@code HierarchicalPart} to use in this {@code Urin}.
     * @param query            any {@code Query} to use in this {@code Urin}.
     * @param fragment         any {@code Fragment} to use in this {@code Urin}.
     * @return a {@code Urin} with the given {@code Scheme}, {@code HierarchicalPart}, {@code Query}, and {@code Fragment}.
     */
    public static Urin urin(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query, final Fragment fragment) {
        return new UrinWithHierarchicalPartAndQueryAndFragment(scheme.removeDefaultPort(), hierarchicalPart.normalisePort(scheme), query, fragment);
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

    private static final class UrinWithHierarchicalPartAndQueryAndFragment extends Urin {
        private final Scheme scheme;
        private final HierarchicalPart hierarchicalPart;
        private final Query query;
        private final Fragment fragment;

        UrinWithHierarchicalPartAndQueryAndFragment(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query, final Fragment fragment) {
            if (scheme == null) {
                throw new NullPointerException("Cannot instantiate Urin with null scheme");
            }
            this.scheme = scheme;
            if (hierarchicalPart == null) {
                throw new NullPointerException("Cannot instantiate Urin with null hierarchicalPart");
            }
            this.hierarchicalPart = hierarchicalPart;
            if (query == null) {
                throw new NullPointerException("Cannot instantiate Urin with null query");
            }
            this.query = query;
            if (fragment == null) {
                throw new NullPointerException("Cannot instantiate Urin with null fragment");
            }
            this.fragment = fragment;
        }

        @Override
        public String asString() {
            return scheme.asString() + ':' + hierarchicalPart.asString() + '?' + query.asString() + '#' + fragment.asString();
        }

        @Override
        public Path path() {
            return hierarchicalPart.path();
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
        public Urin resolve(final UrinReference urinReference) {
            return urinReference.resolve(scheme, hierarchicalPart, query, fragment);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            UrinWithHierarchicalPartAndQueryAndFragment that = (UrinWithHierarchicalPartAndQueryAndFragment) o;
            return fragment.equals(that.fragment)
                    && hierarchicalPart.equals(that.hierarchicalPart)
                    && query.equals(that.query)
                    && scheme.equals(that.scheme);
        }

        @Override
        public int hashCode() {
            int result = scheme.hashCode();
            result = 31 * result + hierarchicalPart.hashCode();
            result = 31 * result + query.hashCode();
            result = 31 * result + fragment.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "Urin{" +
                    "scheme=" + scheme +
                    ", hierarchicalPart=" + hierarchicalPart +
                    ", query=" + query +
                    ", fragment=" + fragment +
                    '}';
        }
    }

    private static final class UrinWithHierarchicalPartAndQuery extends Urin {
        private final Scheme scheme;
        private final HierarchicalPart hierarchicalPart;
        private final Query query;

        UrinWithHierarchicalPartAndQuery(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query) {
            if (scheme == null) {
                throw new NullPointerException("cannot instantiate Urin with null scheme");
            }
            this.scheme = scheme;
            if (hierarchicalPart == null) {
                throw new NullPointerException("cannot instantiate Urin with null hierarchicalPart");
            }
            this.hierarchicalPart = hierarchicalPart;
            if (query == null) {
                throw new NullPointerException("cannot instantiate Urin with null query");
            }
            this.query = query;
        }

        @Override
        public String asString() {
            return scheme.asString() + ':' + hierarchicalPart.asString() + '?' + query.asString();
        }

        @Override
        public Path path() {
            return hierarchicalPart.path();
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
        public Urin resolve(final UrinReference urinReference) {
            return urinReference.resolve(scheme, hierarchicalPart, query);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            UrinWithHierarchicalPartAndQuery that = (UrinWithHierarchicalPartAndQuery) o;
            return hierarchicalPart.equals(that.hierarchicalPart)
                    && query.equals(that.query)
                    && scheme.equals(that.scheme);
        }

        @Override
        public int hashCode() {
            int result = scheme.hashCode();
            result = 31 * result + hierarchicalPart.hashCode();
            result = 31 * result + query.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "Urin{" +
                    "scheme=" + scheme +
                    ", hierarchicalPart=" + hierarchicalPart +
                    ", query=" + query +
                    '}';
        }
    }

    private static final class UrinWithHierarchicalPartAndFragment extends Urin {
        private final Scheme scheme;
        private final HierarchicalPart hierarchicalPart;
        private final Fragment fragment;

        UrinWithHierarchicalPartAndFragment(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Fragment fragment) {
            if (scheme == null) {
                throw new NullPointerException("cannot instantiate Urin with null scheme");
            }
            this.scheme = scheme;
            if (hierarchicalPart == null) {
                throw new NullPointerException("cannot instantiate Urin with null hierarchicalPart");
            }
            this.hierarchicalPart = hierarchicalPart;
            if (fragment == null) {
                throw new NullPointerException("cannot instantiate Urin with null fragment");
            }
            this.fragment = fragment;
        }

        @Override
        public String asString() {
            return scheme.asString() + ':' + hierarchicalPart.asString() + '#' + fragment.asString();
        }

        @Override
        public Path path() {
            return hierarchicalPart.path();
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
        public Urin resolve(final UrinReference urinReference) {
            return urinReference.resolve(scheme, hierarchicalPart);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            UrinWithHierarchicalPartAndFragment that = (UrinWithHierarchicalPartAndFragment) o;
            return fragment.equals(that.fragment)
                    && hierarchicalPart.equals(that.hierarchicalPart)
                    && scheme.equals(that.scheme);
        }

        @Override
        public int hashCode() {
            int result = scheme.hashCode();
            result = 31 * result + hierarchicalPart.hashCode();
            result = 31 * result + fragment.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "Urin{" +
                    "scheme=" + scheme +
                    ", hierarchicalPart=" + hierarchicalPart +
                    ", fragment=" + fragment +
                    '}';
        }
    }

    private static class UrinWithHierarchicalPart extends Urin {
        private final Scheme scheme;
        private final HierarchicalPart hierarchicalPart;

        public UrinWithHierarchicalPart(final Scheme scheme, final HierarchicalPart hierarchicalPart) {
            if (scheme == null) {
                throw new NullPointerException("cannot instantiate Urin with null scheme");
            }
            this.scheme = scheme;
            if (hierarchicalPart == null) {
                throw new NullPointerException("cannot instantiate Urin with null hierarchicalPart");
            }
            this.hierarchicalPart = hierarchicalPart;
        }

        @Override
        public String asString() {
            return scheme.asString() + ':' + hierarchicalPart.asString();
        }

        @Override
        public Path path() {
            return hierarchicalPart.path();
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
        public Urin resolve(final UrinReference urinReference) {
            return urinReference.resolve(scheme, hierarchicalPart);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            UrinWithHierarchicalPart that = (UrinWithHierarchicalPart) o;
            return hierarchicalPart.equals(that.hierarchicalPart)
                    && scheme.equals(that.scheme);
        }

        @Override
        public int hashCode() {
            int result = scheme.hashCode();
            result = 31 * result + hierarchicalPart.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "Urin{" +
                    "scheme=" + scheme +
                    ", hierarchicalPart=" + hierarchicalPart +
                    '}';
        }
    }
}
