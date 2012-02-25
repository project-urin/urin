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

import static net.sourceforge.urin.Segments.PrefixWithDotSegmentCriteria.NEVER_PREFIX_WITH_DOT_SEGMENT;
import static net.sourceforge.urin.Segments.PrefixWithDotSegmentCriteria.PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON;
import static net.sourceforge.urin.Urin.urin;

public abstract class RelativeReference extends UrinReference {

    private RelativeReference() {
    }

    public abstract String asString();

    public URI asUri() {
        return URI.create(asString());
    }

    public static RelativeReference relativeReference() {
        return new RelativeReferenceNoAuthority(new EmptySegments());
    }

    public static RelativeReference relativeReference(final Authority authority) {
        return new RelativeReferenceWithAuthority(authority, new EmptySegments());
    }

    public static RelativeReference relativeReference(final Segments segments) {
        return new RelativeReferenceNoAuthority(segments);
    }

    public static RelativeReference relativeReference(final Authority authority, final AbsoluteSegments segments) {
        return new RelativeReferenceWithAuthority(authority, segments);
    }

    public static RelativeReference relativeReference(final Query query) {
        return new RelativeReferenceNoAuthorityWithQuery(new EmptySegments(), query);
    }

    public static RelativeReference relativeReference(final Authority authority, final Query query) {
        return new RelativeReferenceWithAuthorityAndQuery(authority, new EmptySegments(), query);
    }

    public static RelativeReference relativeReference(final Segments segments, final Query query) {
        return new RelativeReferenceNoAuthorityWithQuery(segments, query);
    }

    public static RelativeReference relativeReference(final Authority authority, final AbsoluteSegments segments, final Query query) {
        return new RelativeReferenceWithAuthorityAndQuery(authority, segments, query);
    }

    public static RelativeReference relativeReference(final Fragment fragment) {
        return new RelativeReferenceNoAuthorityWithFragment(new EmptySegments(), fragment);
    }

    public static RelativeReference relativeReference(final Authority authority, final Fragment fragment) {
        return new RelativeReferenceWithAuthorityAndFragment(authority, new EmptySegments(), fragment);
    }

    public static RelativeReference relativeReference(final Segments segments, final Fragment fragment) {
        return new RelativeReferenceNoAuthorityWithFragment(segments, fragment);
    }

    public static RelativeReference relativeReference(final Authority authority, final AbsoluteSegments segments, final Fragment fragment) {
        return new RelativeReferenceWithAuthorityAndFragment(authority, segments, fragment);
    }

    public static RelativeReference relativeReference(final Query query, final Fragment fragment) {
        return new RelativeReferenceNoAuthorityWithQueryAndFragment(new EmptySegments(), query, fragment);
    }

    public static RelativeReference relativeReference(final Authority authority, final Query query, final Fragment fragment) {
        return new RelativeReferenceWithAuthorityAndQueryAndFragment(authority, new EmptySegments(), query, fragment);
    }

    public static RelativeReference relativeReference(final Segments segments, final Query query, final Fragment fragment) {
        return new RelativeReferenceNoAuthorityWithQueryAndFragment(segments, query, fragment);
    }

    public static RelativeReference relativeReference(final Authority authority, final AbsoluteSegments segments, final Query query, final Fragment fragment) {
        return new RelativeReferenceWithAuthorityAndQueryAndFragment(authority, segments, query, fragment);
    }

    abstract Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart);

    abstract Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query);

    abstract Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query, final Fragment fragment);

    private static final class RelativeReferenceNoAuthority extends RelativeReference {
        private final Segments segments;

        RelativeReferenceNoAuthority(final Segments segments) {
            if (segments == null) {
                throw new NullPointerException("Cannot instantiate RelativeReference with null segments");
            }
            this.segments = segments;
        }

        @Override
        public String asString() {
            return segments.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON);
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart) {
            return urin(scheme, hierarchicalPart.resolve(segments));
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query) {
            if (segments.isEmpty()) {
                return urin(scheme, hierarchicalPart.resolve(segments), query);
            } else {
                return urin(scheme, hierarchicalPart.resolve(segments));
            }
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query, final Fragment fragment) {
            if (segments.isEmpty()) {
                return urin(scheme, hierarchicalPart.resolve(segments), query, fragment);
            } else {
                return urin(scheme, hierarchicalPart.resolve(segments));
            }
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            RelativeReferenceNoAuthority that = (RelativeReferenceNoAuthority) o;
            return segments.equals(that.segments);
        }

        @Override
        public int hashCode() {
            return segments.hashCode();
        }

        @Override
        public String toString() {
            return "RelativeReference{" +
                    "segments=" + segments +
                    '}';
        }
    }

    private static final class RelativeReferenceNoAuthorityWithQuery extends RelativeReference {
        private final Segments segments;
        private final Query query;

        RelativeReferenceNoAuthorityWithQuery(final Segments segments, final Query query) {
            if (segments == null) {
                throw new NullPointerException("Cannot instantiate RelativeReference with null segments");
            }
            this.segments = segments;
            if (query == null) {
                throw new NullPointerException("Cannot instantiate RelativeReference with null query");
            }
            this.query = query;
        }

        @Override
        public String asString() {
            return new StringBuilder(segments.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON))
                    .append('?')
                    .append(query.asString())
                    .toString();
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart) {
            return urin(scheme, hierarchicalPart.resolve(segments), query);
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query) {
            return urin(scheme, hierarchicalPart.resolve(segments), this.query);
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query, final Fragment fragment) {
            if (segments.isEmpty()) {
                return urin(scheme, hierarchicalPart.resolve(segments), this.query, fragment);
            } else {
                return urin(scheme, hierarchicalPart.resolve(segments), this.query);
            }
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            RelativeReferenceNoAuthorityWithQuery that = (RelativeReferenceNoAuthorityWithQuery) o;

            return query.equals(that.query) && segments.equals(that.segments);

        }

        @Override
        public int hashCode() {
            int result = segments.hashCode();
            result = 31 * result + query.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "RelativeReference{" +
                    "segments=" + segments +
                    ", query=" + query +
                    '}';
        }
    }

    private static final class RelativeReferenceNoAuthorityWithFragment extends RelativeReference {
        private final Segments segments;
        private final Fragment fragment;

        RelativeReferenceNoAuthorityWithFragment(final Segments segments, final Fragment fragment) {
            if (segments == null) {
                throw new NullPointerException("Cannot instantiate RelativeReference with null segments");
            }
            this.segments = segments;
            if (fragment == null) {
                throw new NullPointerException("Cannot instantiate RelativeReference with null fragment");
            }
            this.fragment = fragment;
        }

        @Override
        public String asString() {
            return new StringBuilder(segments.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON))
                    .append('#')
                    .append(fragment.asString())
                    .toString();
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart) {
            return urin(scheme, hierarchicalPart.resolve(segments), fragment);
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query) {
            if (segments.isEmpty()) {
                return urin(scheme, hierarchicalPart.resolve(segments), query, fragment);
            } else {
                return urin(scheme, hierarchicalPart.resolve(segments), fragment);
            }
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query, final Fragment fragment) {
            if (segments.isEmpty()) {
                return urin(scheme, hierarchicalPart.resolve(segments), query, this.fragment);
            } else {
                return urin(scheme, hierarchicalPart.resolve(segments), this.fragment);
            }
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            RelativeReferenceNoAuthorityWithFragment that = (RelativeReferenceNoAuthorityWithFragment) o;

            return fragment.equals(that.fragment) && segments.equals(that.segments);

        }

        @Override
        public int hashCode() {
            int result = segments.hashCode();
            result = 31 * result + fragment.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "RelativeReference{" +
                    "segments=" + segments +
                    ", fragment=" + fragment +
                    '}';
        }
    }

    private static final class RelativeReferenceNoAuthorityWithQueryAndFragment extends RelativeReference {
        private final Segments segments;
        private final Fragment fragment;
        private final Query query;

        RelativeReferenceNoAuthorityWithQueryAndFragment(final Segments segments, final Query query, final Fragment fragment) {
            if (segments == null) {
                throw new NullPointerException("Cannot instantiate RelativeReference with null segments");
            }
            this.segments = segments;
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
            return new StringBuilder(segments.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON))
                    .append('?')
                    .append(query.asString())
                    .append('#')
                    .append(fragment.asString())
                    .toString();
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            RelativeReferenceNoAuthorityWithQueryAndFragment that = (RelativeReferenceNoAuthorityWithQueryAndFragment) o;

            return fragment.equals(that.fragment) && query.equals(that.query) && segments.equals(that.segments);
        }

        @Override
        public int hashCode() {
            int result = segments.hashCode();
            result = 31 * result + fragment.hashCode();
            result = 31 * result + query.hashCode();
            return result;
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart) {
            return urin(scheme, hierarchicalPart.resolve(segments), query, fragment);
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query) {
            return urin(scheme, hierarchicalPart.resolve(segments), this.query, fragment);
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query, final Fragment fragment) {
            return urin(scheme, hierarchicalPart.resolve(segments), this.query, this.fragment);
        }

        @Override
        public String toString() {
            return "RelativeReference{" +
                    "segments=" + segments +
                    ", query=" + query +
                    ", fragment=" + fragment +
                    '}';
        }
    }

    private static final class RelativeReferenceWithAuthority extends RelativeReference {
        private final Authority authority;
        private final Segments segments;

        RelativeReferenceWithAuthority(final Authority authority, final Segments segments) {
            if (authority == null) {
                throw new NullPointerException("Cannot instantiate RelativeReference with null authority");
            }
            this.authority = authority;
            if (segments == null) {
                throw new NullPointerException("Cannot instantiate RelativeReference with null segments");
            }
            this.segments = segments;
        }

        @Override
        public String asString() {
            return new StringBuilder("//")
                    .append(authority.asString())
                    .append(segments.asString(NEVER_PREFIX_WITH_DOT_SEGMENT))
                    .toString();
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart) {
            return urin(scheme, hierarchicalPart.resolve(authority, segments));
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query) {
            return urin(scheme, hierarchicalPart.resolve(authority, segments));
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query, final Fragment fragment) {
            return urin(scheme, hierarchicalPart.resolve(authority, segments));
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            RelativeReferenceWithAuthority that = (RelativeReferenceWithAuthority) o;
            return authority.equals(that.authority)
                    && segments.equals(that.segments);
        }

        @Override
        public int hashCode() {
            int result = authority.hashCode();
            result = 31 * result + segments.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "RelativeReference{" +
                    "authority=" + authority +
                    ", segments=" + segments +
                    '}';
        }
    }

    private static final class RelativeReferenceWithAuthorityAndQuery extends RelativeReference {
        private final Authority authority;
        private final Segments segments;
        private final Query query;

        RelativeReferenceWithAuthorityAndQuery(final Authority authority, final Segments segments, final Query query) {
            if (authority == null) {
                throw new NullPointerException("Cannot instantiate RelativeReference with null authority");
            }
            this.authority = authority;
            if (segments == null) {
                throw new NullPointerException("Cannot instantiate RelativeReference with null segments");
            }
            this.segments = segments;
            if (query == null) {
                throw new NullPointerException("Cannot instantiate RelativeReference with null query");
            }
            this.query = query;
        }

        @Override
        public String asString() {
            return new StringBuilder("//")
                    .append(authority.asString())
                    .append(segments.asString(NEVER_PREFIX_WITH_DOT_SEGMENT))
                    .append('?')
                    .append(query.asString())
                    .toString();
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart) {
            return urin(scheme, hierarchicalPart.resolve(authority, segments), query);
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query) {
            return urin(scheme, hierarchicalPart.resolve(authority, segments), this.query);
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query, final Fragment fragment) {
            return urin(scheme, hierarchicalPart.resolve(authority, segments), this.query);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            RelativeReferenceWithAuthorityAndQuery that = (RelativeReferenceWithAuthorityAndQuery) o;

            return authority.equals(that.authority) && query.equals(that.query) && segments.equals(that.segments);
        }

        @Override
        public int hashCode() {
            int result = authority.hashCode();
            result = 31 * result + segments.hashCode();
            result = 31 * result + query.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "RelativeReference{" +
                    "authority=" + authority +
                    ", segments=" + segments +
                    ", query=" + query +
                    '}';
        }
    }

    private static final class RelativeReferenceWithAuthorityAndFragment extends RelativeReference {
        private final Authority authority;
        private final Segments segments;
        private final Fragment fragment;

        RelativeReferenceWithAuthorityAndFragment(final Authority authority, final Segments segments, final Fragment fragment) {
            if (authority == null) {
                throw new NullPointerException("Cannot instantiate RelativeReference with null authority");
            }
            this.authority = authority;
            if (segments == null) {
                throw new NullPointerException("Cannot instantiate RelativeReference with null segments");
            }
            this.segments = segments;
            if (fragment == null) {
                throw new NullPointerException("Cannot instantiate RelativeReference with null fragment");
            }
            this.fragment = fragment;
        }

        @Override
        public String asString() {
            return new StringBuilder("//")
                    .append(authority.asString())
                    .append(segments.asString(NEVER_PREFIX_WITH_DOT_SEGMENT))
                    .append('#')
                    .append(fragment.asString())
                    .toString();
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart) {
            return urin(scheme, hierarchicalPart.resolve(authority, segments), fragment);
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query) {
            return urin(scheme, hierarchicalPart.resolve(authority, segments), fragment);
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query, final Fragment fragment) {
            return urin(scheme, hierarchicalPart.resolve(authority, segments), this.fragment);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            RelativeReferenceWithAuthorityAndFragment that = (RelativeReferenceWithAuthorityAndFragment) o;

            return authority.equals(that.authority) && fragment.equals(that.fragment) && segments.equals(that.segments);
        }

        @Override
        public int hashCode() {
            int result = authority.hashCode();
            result = 31 * result + segments.hashCode();
            result = 31 * result + fragment.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "RelativeReference{" +
                    "authority=" + authority +
                    ", segments=" + segments +
                    ", fragment=" + fragment +
                    '}';
        }
    }

    private static final class RelativeReferenceWithAuthorityAndQueryAndFragment extends RelativeReference {
        private final Authority authority;
        private final Segments segments;
        private final Query query;
        private final Fragment fragment;

        RelativeReferenceWithAuthorityAndQueryAndFragment(final Authority authority, final Segments segments, final Query query, final Fragment fragment) {
            if (authority == null) {
                throw new NullPointerException("Cannot instantiate RelativeReference with null authority");
            }
            this.authority = authority;
            if (segments == null) {
                throw new NullPointerException("Cannot instantiate RelativeReference with null segments");
            }
            this.segments = segments;
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
                    .append(segments.asString(NEVER_PREFIX_WITH_DOT_SEGMENT))
                    .append('?')
                    .append(query.asString())
                    .append('#')
                    .append(fragment.asString())
                    .toString();
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart) {
            return urin(scheme, hierarchicalPart.resolve(authority, segments), query, fragment);
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query) {
            return urin(scheme, hierarchicalPart.resolve(authority, segments), this.query, fragment);
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query, final Fragment fragment) {
            return urin(scheme, hierarchicalPart.resolve(authority, segments), this.query, this.fragment);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            RelativeReferenceWithAuthorityAndQueryAndFragment that = (RelativeReferenceWithAuthorityAndQueryAndFragment) o;

            return authority.equals(that.authority) && fragment.equals(that.fragment) && query.equals(that.query) && segments.equals(that.segments);
        }

        @Override
        public int hashCode() {
            int result = authority.hashCode();
            result = 31 * result + segments.hashCode();
            result = 31 * result + query.hashCode();
            result = 31 * result + fragment.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "RelativeReference{" +
                    "authority=" + authority +
                    ", segments=" + segments +
                    ", query=" + query +
                    ", fragment=" + fragment +
                    '}';
        }
    }

}
