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

import static net.sourceforge.urin.AbsoluteSegments.absoluteSegments;
import static net.sourceforge.urin.EmptySegments.emptySegments;
import static net.sourceforge.urin.RelativeSegments.relativeSegments;

public abstract class RelativeReference {

    private RelativeReference() {
    }

    public abstract String asString();

    public URI asUri() {
        return URI.create(asString());
    }

    public static RelativeReference relativeReference() {
        return new RelativeReferenceNoAuthority(emptySegments());
    }

    public static RelativeReference relativeReference(final Authority authority) {
        return new RelativeReferenceWithAuthority(authority, emptySegments());
    }

    public static RelativeReference relativeReferenceRootless(final Segment... segments) {
        return relativeReferenceRootless(relativeSegments(segments));
    }

    public static RelativeReference relativeReferenceRootless(final RelativeSegments segments) {
        if (segments.firstPartIsSuppliedButIsEmpty()) {
            throw new IllegalArgumentException("If supplied, first segment must be non-empty");
        }
        Segments segmentsWithoutColonInFirstSegment = segments.firstPartIsSuppliedButContainsColon() ? segments.prefixWithDotSegment() : segments;
        return new RelativeReferenceNoAuthority(segmentsWithoutColonInFirstSegment);
    }

    public static RelativeReference relativeReferenceAbsolute(final Segment... segments) {
        return relativeReferenceAbsolute(absoluteSegments(segments));
    }

    public static RelativeReference relativeReferenceAbsolute(final AbsoluteSegments segments) {
        if (segments.firstPartIsSuppliedButIsEmpty()) {
            throw new IllegalArgumentException("If supplied, first segment must be non-empty");
        }
        return new RelativeReferenceNoAuthority(segments);
    }

    public static RelativeReference relativeReferenceAbsolute(final Authority authority, final Segment... segments) {
        return relativeReferenceAbsolute(authority, absoluteSegments(segments));
    }

    public static RelativeReference relativeReferenceAbsolute(final Authority authority, final AbsoluteSegments segments) {
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
            return segments.asString();
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
                    .append(segments.asString())
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
