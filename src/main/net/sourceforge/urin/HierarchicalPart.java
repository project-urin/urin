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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static java.util.Arrays.asList;

public abstract class HierarchicalPart {

    private HierarchicalPart() {
    }

    abstract String asString();

    public static HierarchicalPart hierarchicalPart() {
        return new HierarchicalPartNoAuthority(Collections.<Segment>emptyList());
    }

    public static HierarchicalPart hierarchicalPart(final Authority authority) {
        return new HierarchicalPartWithAuthority(authority, Collections.<Segment>emptyList());
    }

    public static HierarchicalPart hierarchicalPartRootless(final Segment... segments) {
        if (segments.length > 0 && segments[0].isEmpty()) {
            throw new IllegalArgumentException("If supplied, first segment must be non-empty");
        }
        final Iterable<Segment> segmentIterable = new ArrayList<Segment>(asList(segments));
        return new HierarchicalPartNoAuthority(segmentIterable);
    }

    public static HierarchicalPart hierarchicalPartAbsolute(final Segment... segments) {
        if (segments.length > 0 && segments[0].isEmpty()) {
            throw new IllegalArgumentException("If supplied, first segment must be non-empty");
        }
        final List<Segment> segmentList = new ArrayList<Segment>(segments.length + 1);
        segmentList.add(Segment.EMPTY);
        if (segments.length == 0) {
            segmentList.add(Segment.EMPTY);
        } else {
            segmentList.addAll(asList(segments));
        }

        return new HierarchicalPartNoAuthority(segmentList);
    }

    public static HierarchicalPart hierarchicalPartAbsolute(final Authority authority, final Segment... segments) {
        final List<Segment> segmentList = new ArrayList<Segment>(segments.length + 1);
        segmentList.add(Segment.EMPTY);
        if (segments.length == 0) {
            segmentList.add(Segment.EMPTY);
        } else {
            segmentList.addAll(asList(segments));
        }
        return new HierarchicalPartWithAuthority(authority, segmentList);
    }

    private static String segmentsAsString(final Iterable<Segment> segments) {
        StringBuilder result = new StringBuilder();
        Iterator<Segment> segmentIterator = segments.iterator();
        while (segmentIterator.hasNext()) {
            result.append(segmentIterator.next().asString());
            if (segmentIterator.hasNext()) {
                result.append('/');
            }
        }
        return result.toString();
    }

    private static final class HierarchicalPartNoAuthority extends HierarchicalPart {
        private final Iterable<Segment> segments;

        HierarchicalPartNoAuthority(final Iterable<Segment> segments) {
            this.segments = segments;
        }

        @Override
        String asString() {
            return segmentsAsString(segments);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            HierarchicalPartNoAuthority that = (HierarchicalPartNoAuthority) o;

            return !(segments != null ? !segments.equals(that.segments) : that.segments != null);

        }

        @Override
        public int hashCode() {
            return segments != null ? segments.hashCode() : 0;
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
        private final List<Segment> segments;

        HierarchicalPartWithAuthority(final Authority authority, final List<Segment> segments) {
            this.authority = authority;
            this.segments = segments;
        }

        @Override
        String asString() {
            return new StringBuilder("//")
                    .append(authority.asString())
                    .append(segmentsAsString(segments))
                    .toString();
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            HierarchicalPartWithAuthority that = (HierarchicalPartWithAuthority) o;
            return !(authority != null ? !authority.equals(that.authority) : that.authority != null)
                    && !(segments != null ? !segments.equals(that.segments) : that.segments != null);

        }

        @Override
        public int hashCode() {
            int result = authority != null ? authority.hashCode() : 0;
            result = 31 * result + (segments != null ? segments.hashCode() : 0);
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
