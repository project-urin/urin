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
import static net.sourceforge.urin.Segments.PrefixWithDotSegmentCriteria.PREFIX_WITH_DOT_SEGMENT_IF_FIRST_CONTAINS_COLON;

public abstract class RelativeReference {

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
        if (segments.firstPartIsSuppliedButIsEmpty()) {
            throw new IllegalArgumentException("If supplied, first segment must be non-empty");
        }
        return new RelativeReferenceNoAuthority(segments);
    }

    public static RelativeReference relativeReference(final Authority authority, final AbsoluteSegments segments) {
        return new RelativeReferenceWithAuthority(authority, segments);
    }

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
            return segments.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_CONTAINS_COLON);
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

}
