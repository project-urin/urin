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

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.sourceforge.urin.Segments.PrefixWithDotSegmentCriteria.NEVER_PREFIX_WITH_DOT_SEGMENT;
import static net.sourceforge.urin.Segments.PrefixWithDotSegmentCriteria.PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY;

public abstract class HierarchicalPart {

    private static final Pattern HIERARCHICAL_PART_REFERENCE_PATTERN = Pattern.compile("^(//([^/?#]*))?([^?#]*)");

    private HierarchicalPart() {
    }

    static HierarchicalPart parse(final String hierarchicalPartString) throws ParseException {
        final Matcher matcher = HIERARCHICAL_PART_REFERENCE_PATTERN.matcher(hierarchicalPartString);
        matcher.matches();
        final String authorityString = matcher.group(2);
        final String path = matcher.group(3);
        HierarchicalPart hierarchicalPart;
        if (authorityString == null) {
            if (path == null || "".equals(path)) {
                hierarchicalPart = hierarchicalPart();
            } else {
                hierarchicalPart = hierarchicalPart(!path.startsWith("/") ? parseRootlessSegments(path) : parseSegments(path));
            }
        } else {
            Authority authority = Authority.parse(authorityString);
            hierarchicalPart = (path == null || "".equals(path)) ? hierarchicalPart(authority) : hierarchicalPart(authority, parseSegments(path));
        }
        return hierarchicalPart;
    }

    static Segments parseRootlessSegments(final String rawPath) {
        return Segments.rootlessSegments(rawPath == null ? new ArrayList<Segment>() : new ArrayList<Segment>() {{
            for (String segmentString : rawPath.split("/")) {
                add(Segment.parse(segmentString));
            }
        }});
    }

    static AbsoluteSegments parseSegments(final String rawPath) {
        return Segments.segments(new ArrayList<Segment>() {{
            boolean isFirst = true;
            for (String segmentString : rawPath.split("/")) {
                if (!isFirst) {
                    add(Segment.parse(segmentString));
                }
                isFirst = false;
            }
        }});
    }

    abstract String asString();

    public static HierarchicalPart hierarchicalPart() {
        return new HierarchicalPartNoAuthority(new EmptySegments());
    }

    public static HierarchicalPart hierarchicalPart(final Authority authority) {
        return new HierarchicalPartWithAuthority(authority, new EmptySegments());
    }

    public static HierarchicalPart hierarchicalPart(final Segments segments) {
        return new HierarchicalPartNoAuthority(segments);
    }

    public static HierarchicalPart hierarchicalPart(final Authority authority, final AbsoluteSegments segments) {
        return new HierarchicalPartWithAuthority(authority, segments);
    }

    abstract HierarchicalPart resolve(final Segments relativeReferenceSegments);

    final HierarchicalPart resolve(final Authority relativeReferenceAuthority, final Segments relativeReferenceSegments) {
        return new HierarchicalPartWithAuthority(relativeReferenceAuthority, relativeReferenceSegments);
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
            return segments.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY);
        }

        @Override
        HierarchicalPart resolve(final Segments relativeReferenceSegments) {
            return hierarchicalPart(relativeReferenceSegments.resolveRelativeTo(segments));
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
                    .append(segments.asString(NEVER_PREFIX_WITH_DOT_SEGMENT))
                    .toString();
        }

        @Override
        HierarchicalPart resolve(final Segments relativeReferenceSegments) {
            return new HierarchicalPartWithAuthority(authority, relativeReferenceSegments.resolveRelativeTo(segments));
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
