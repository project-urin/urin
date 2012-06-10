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

import static net.sourceforge.urin.Path.PrefixWithDotSegmentCriteria.NEVER_PREFIX_WITH_DOT_SEGMENT;
import static net.sourceforge.urin.Path.PrefixWithDotSegmentCriteria.PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON;
import static net.sourceforge.urin.Urin.urin;

/**
 * A relative reference.
 * <p/>
 * A relative reference has a mandatory relative part component, that is made up of an optional authority, and a
 * mandatory path, and optional query and fragment parts.  The mandatory path may implicitly be the empty path.
 *
 * @see <a href="http://tools.ietf.org/html/rfc3986#section-4.2">RFC 3986 - Relative Reference</a>
 */
public abstract class RelativeReference extends UrinReference {

    private static final Pattern RELATIVE_REFERENCE_PATTERN = Pattern.compile("^((//([^/?#]*))?([^?#]*))(\\?([^#]*))?(#(.*))?");

    private RelativeReference() {
    }

    /**
     * Factory method for creating {@code RelativeReference}s with just an empty path.
     *
     * @return a {@code RelativeReference} with an empty path.
     */
    public static RelativeReference relativeReference() {
        return new RelativeReferenceNoAuthority(new EmptyPath());
    }

    /**
     * Factory method for creating {@code RelativeReference}s with an authority and an empty path.
     *
     * @param authority any {@code Authority} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Authority} and an empty path.
     */
    public static RelativeReference relativeReference(final Authority authority) {
        return new RelativeReferenceWithAuthority(authority, new EmptyPath());
    }

    /**
     * Factory method for creating {@code RelativeReference}s with just a path.
     *
     * @param path any {@code Path} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Path}.
     */
    public static RelativeReference relativeReference(final Path path) {
        return new RelativeReferenceNoAuthority(path);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with an authority and a path.
     *
     * @param authority any {@code Authority} to use in this {@code RelativeReference}.
     * @param path      any {@code AbsolutePath} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Authority} and {@code Path}.
     */
    public static RelativeReference relativeReference(final Authority authority, final AbsolutePath path) {
        return new RelativeReferenceWithAuthority(authority, path);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with a query and an empty path.
     *
     * @param query any {@code Query} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Query} and an empty path.
     */
    public static RelativeReference relativeReference(final Query query) {
        return new RelativeReferenceNoAuthorityWithQuery(new EmptyPath(), query);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with an authority, a query and an empty path.
     *
     * @param authority any {@code Authority} to use in this {@code RelativeReference}.
     * @param query     any {@code Query} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Authority} and {@code Query}, and an empty path.
     */
    public static RelativeReference relativeReference(final Authority authority, final Query query) {
        return new RelativeReferenceWithAuthorityAndQuery(authority, new EmptyPath(), query);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with a path and a query.
     *
     * @param path  any {@code Path} to use in this {@code RelativeReference}.
     * @param query any {@code Query} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Path} and {@code Query}.
     */
    public static RelativeReference relativeReference(final Path path, final Query query) {
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
    public static RelativeReference relativeReference(final Authority authority, final AbsolutePath path, final Query query) {
        return new RelativeReferenceWithAuthorityAndQuery(authority, path, query);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with a fragment and an empty path.
     *
     * @param fragment any {@code Fragment} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Fragment} and an empty path.
     */
    public static RelativeReference relativeReference(final Fragment fragment) {
        return new RelativeReferenceNoAuthorityWithFragment(new EmptyPath(), fragment);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with an authority, a fragment, and an empty path.
     *
     * @param authority any {@code Authority} to use in this {@code RelativeReference}.
     * @param fragment  any {@code Fragment} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Authority} and {@code Fragment}, and an empty path.
     */
    public static RelativeReference relativeReference(final Authority authority, final Fragment fragment) {
        return new RelativeReferenceWithAuthorityAndFragment(authority, new EmptyPath(), fragment);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with a path and a fragment.
     *
     * @param path     any {@code Path} to use in this {@code RelativeReference}.
     * @param fragment any {@code Fragment} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Path} and {@code Fragment}.
     */
    public static RelativeReference relativeReference(final Path path, final Fragment fragment) {
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
    public static RelativeReference relativeReference(final Authority authority, final AbsolutePath path, final Fragment fragment) {
        return new RelativeReferenceWithAuthorityAndFragment(authority, path, fragment);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with a query, a fragment, and an empty path.
     *
     * @param query    any {@code Query} to use in this {@code RelativeReference}.
     * @param fragment any {@code Fragment} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Query} and {@code Fragment}, and an empty path.
     */
    public static RelativeReference relativeReference(final Query query, final Fragment fragment) {
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
    public static RelativeReference relativeReference(final Authority authority, final Query query, final Fragment fragment) {
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
    public static RelativeReference relativeReference(final Path path, final Query query, final Fragment fragment) {
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
    public static RelativeReference relativeReference(final Authority authority, final AbsolutePath path, final Query query, final Fragment fragment) {
        return new RelativeReferenceWithAuthorityAndQueryAndFragment(authority, path, query, fragment);
    }

    /**
     * Parses the given {@code String} as a relative reference.
     *
     * @param relativeReferenceString a {@code String} that represents a relative reference.
     * @return a {@code UrinReference} representing the relative reference represented by the given {@code String}.
     * @throws ParseException if the given {@code String} is not a valid relative reference.
     */
    public static RelativeReference parse(final String relativeReferenceString) throws ParseException {
        final Matcher matcher = RELATIVE_REFERENCE_PATTERN.matcher(relativeReferenceString);
        matcher.matches();
        final String queryString = matcher.group(6);
        final String fragment = matcher.group(8);
        final RelativeReference result;
        final String authorityString = matcher.group(3);
        final String pathString = matcher.group(4);
        if (authorityString == null) {
            if (pathString == null || "".equals(pathString)) {
                if (queryString == null) {
                    if (fragment == null) {
                        result = relativeReference();
                    } else {
                        result = relativeReference(Fragment.parse(fragment));
                    }
                } else {
                    final Query query = Query.parse(queryString);
                    if (fragment == null) {
                        result = relativeReference(query);
                    } else {
                        result = relativeReference(query, Fragment.parse(fragment));
                    }
                }
            } else {
                final Path path = !pathString.startsWith("/") ? Path.parseRootlessPath(pathString) : Path.parseParse(pathString);
                if (queryString == null) {
                    if (fragment == null) {
                        result = relativeReference(path);
                    } else {
                        result = relativeReference(path, Fragment.parse(fragment));
                    }
                } else {
                    final Query query = Query.parse(queryString);
                    if (fragment == null) {
                        result = relativeReference(path, query);
                    } else {
                        result = relativeReference(path, query, Fragment.parse(fragment));
                    }
                }
            }
        } else {
            final Authority authority = Authority.parse(authorityString);
            if (pathString == null || "".equals(pathString)) {
                if (queryString == null) {
                    if (fragment == null) {
                        result = relativeReference(authority);
                    } else {
                        result = relativeReference(authority, Fragment.parse(fragment));
                    }
                } else {
                    final Query query = Query.parse(queryString);
                    if (fragment == null) {
                        result = relativeReference(authority, query);
                    } else {
                        result = relativeReference(authority, query, Fragment.parse(fragment));
                    }
                }
            } else {
                final AbsolutePath path = Path.parseParse(pathString);
                if (queryString == null) {
                    if (fragment == null) {
                        result = relativeReference(authority, path);
                    } else {
                        result = relativeReference(authority, path, Fragment.parse(fragment));
                    }
                } else {
                    final Query query = Query.parse(queryString);
                    if (fragment == null) {
                        result = relativeReference(authority, path, query);
                    } else {
                        result = relativeReference(authority, path, query, Fragment.parse(fragment));
                    }
                }
            }
        }
        return result;
    }

    /**
     * Parses the given {@code URI} to produce a {@code RelativeReference}.
     *
     * @param uri a {@code URI} representing a relative reference to parse.
     * @return a {@code RelativeReference} representing the RFC 3986 relative reference represented by the given {@code URI}.
     * @throws ParseException if the given {@code URI} is not a valid RFC 3986 relative reference.
     */
    public static RelativeReference parse(URI uri) throws ParseException {
        return parse(uri.toASCIIString());
    }

    @Override
    public boolean hasFragment() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Fragment fragment() {
        throw new UnsupportedOperationException("Not done yet.");
    }

    static boolean isValidRelativeReferenceString(final String uriReferenceString) {
        return RELATIVE_REFERENCE_PATTERN.matcher(uriReferenceString).matches();
    }

    private static final class RelativeReferenceNoAuthority extends RelativeReference {
        private final Path path;

        RelativeReferenceNoAuthority(final Path path) {
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
            return new StringBuilder(path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON))
                    .append('?')
                    .append(query.asString())
                    .toString();
        }

        @Override
        public Path path() {
            return path;
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
            return new StringBuilder(path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON))
                    .append('#')
                    .append(fragment.asString())
                    .toString();
        }

        @Override
        public Path path() {
            return path;
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
            return new StringBuilder(path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON))
                    .append('?')
                    .append(query.asString())
                    .append('#')
                    .append(fragment.asString())
                    .toString();
        }

        @Override
        public Path path() {
            return path;
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

    private static final class RelativeReferenceWithAuthority extends RelativeReference {
        private final Authority authority;
        private final Path path;

        RelativeReferenceWithAuthority(final Authority authority, final Path path) {
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
            return new StringBuilder("//")
                    .append(authority.asString())
                    .append(path.asString(NEVER_PREFIX_WITH_DOT_SEGMENT))
                    .toString();
        }

        @Override
        public Path path() {
            return path;
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
            return new StringBuilder("//")
                    .append(authority.asString())
                    .append(path.asString(NEVER_PREFIX_WITH_DOT_SEGMENT))
                    .append('?')
                    .append(query.asString())
                    .toString();
        }

        @Override
        public Path path() {
            return path;
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
            return new StringBuilder("//")
                    .append(authority.asString())
                    .append(path.asString(NEVER_PREFIX_WITH_DOT_SEGMENT))
                    .append('#')
                    .append(fragment.asString())
                    .toString();
        }

        @Override
        public Path path() {
            return path;
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
            return new StringBuilder("//")
                    .append(authority.asString())
                    .append(path.asString(NEVER_PREFIX_WITH_DOT_SEGMENT))
                    .append('?')
                    .append(query.asString())
                    .append('#')
                    .append(fragment.asString())
                    .toString();
        }

        @Override
        public Path path() {
            return path;
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
