/*
 * Copyright 2011 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.urin;

import static net.sourceforge.urin.AbsoluteSegments.absoluteSegments;
import static net.sourceforge.urin.Segments.segments;

public abstract class HierarchicalPart {

    private HierarchicalPart() {
    }

    abstract String asString();

    public static HierarchicalPart hierarchicalPart() {
        return new HierarchicalPartNoAuthority(segments());
    }

    public static HierarchicalPart hierarchicalPart(final Authority authority) {
        return new HierarchicalPartWithAuthority(authority, segments());
    }

    public static HierarchicalPart hierarchicalPartRootless(final Segment... segments) {
        return hierarchicalPartRootless(segments(segments));
    }

    public static HierarchicalPart hierarchicalPartRootless(final Segments segments) {
        if (segments.firstPartIsSuppliedButIsEmpty()) {
            throw new IllegalArgumentException("If supplied, first segment must be non-empty");
        }
        return new HierarchicalPartNoAuthority(segments);
    }

    public static HierarchicalPart hierarchicalPartAbsolute(final Segment... segments) {
        return hierarchicalPartAbsolute(segments(segments));
    }

    public static HierarchicalPart hierarchicalPartAbsolute(final Segments segments) {
        if (segments.firstPartIsSuppliedButIsEmpty()) {
            throw new IllegalArgumentException("If supplied, first segment must be non-empty");
        }
        final Segments absolutisedSegments;
        if (segments.isEmpty()) {
            absolutisedSegments = segments.prefixWithEmptySegment().prefixWithEmptySegment();
        } else {
            absolutisedSegments = segments.prefixWithEmptySegment();
        }
        return new HierarchicalPartNoAuthority(absolutisedSegments);
    }

    public static HierarchicalPart hierarchicalPartAbsolute(final Authority authority, final Segment... segments) {
        return hierarchicalPartAbsolute(authority, absoluteSegments(segments));
    }

    public static HierarchicalPart hierarchicalPartAbsolute(final Authority authority, final AbsoluteSegments segments) {
        final Segments absolutisedSegments;
        if (segments.isEmpty()) {
            absolutisedSegments = segments.prefixWithEmptySegment().prefixWithEmptySegment();
        } else {
            absolutisedSegments = segments.prefixWithEmptySegment();
        }
        return new HierarchicalPartWithAuthority(authority, absolutisedSegments);
    }

    private static final class HierarchicalPartNoAuthority extends HierarchicalPart {
        private final Segments segments;

        HierarchicalPartNoAuthority(final Segments segments) {
            if (segments == null) {
                throw new NullPointerException("Cannot instantiate HierarchicalPart with null segments");
            }
            this.segments = segments;
        }

        @Override
        String asString() {
            return segments.asString();
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            HierarchicalPartNoAuthority that = (HierarchicalPartNoAuthority) o;
            return segments.equals(that.segments);
        }

        @Override
        public int hashCode() {
            return segments.hashCode();
        }

        @Override
        public String toString() {
            return "HierarchicalPart{" +
                    "segments=" + segments +
                    '}';
        }
    }

    private static final class HierarchicalPartWithAuthority extends HierarchicalPart {
        private final Authority authority;
        private final Segments segments;

        HierarchicalPartWithAuthority(final Authority authority, final Segments segments) {
            if (authority == null) {
                throw new NullPointerException("Cannot instantiate HierarchicalPart with null authority");
            }
            this.authority = authority;
            if (segments == null) {
                throw new NullPointerException("Cannot instantiate HierarchicalPart with null segments");
            }
            this.segments = segments;
        }

        @Override
        String asString() {
            return new StringBuilder("//")
                    .append(authority.asString())
                    .append(segments.asString())
                    .toString();
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            HierarchicalPartWithAuthority that = (HierarchicalPartWithAuthority) o;
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
            return "HierarchicalPart{" +
                    "authority=" + authority +
                    ", segments=" + segments +
                    '}';
        }
    }

}
