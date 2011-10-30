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

import java.util.*;

import static java.util.Arrays.asList;
import static net.sourceforge.urin.Segment.EMPTY;

public final class Segments {

    private final Collection<Segment> segments;

    public static Segments segments(final String firstSegment, final String... segments) {
        final List<Segment> segmentList = new ArrayList<Segment>(segments.length + 1);
        segmentList.add(Segment.segment(firstSegment));
        for (String segment : segments) {
            segmentList.add(Segment.segment(segment));
        }
        return new Segments(segmentList);
    }

    public static Segments segments(final Segment... segments) {
        return new Segments(asList(segments));
    }

    public static Segments segments(final Collection<Segment> segments) {
        return new Segments(segments);
    }

    private Segments(final Collection<Segment> segments) {
        this.segments = new ArrayList<Segment>(segments);
    }

    Iterator<Segment> iterator() {
        return segments.iterator();
    }

    boolean firstPartIsSuppliedButIsEmpty() {
        return !segments.isEmpty() && EMPTY.equals(segments.iterator().next());
    }

    Segments prefixWithEmptySegment() {
        LinkedList<Segment> newSegments = new LinkedList<Segment>(segments);
        newSegments.offerFirst(EMPTY);
        return new Segments(newSegments);
    }

    String asString() {
        StringBuilder result = new StringBuilder();
        Iterator<Segment> segmentIterator = iterator();
        while (segmentIterator.hasNext()) {
            result.append(segmentIterator.next().asString());
            if (segmentIterator.hasNext()) {
                result.append('/');
            }
        }
        return result.toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Segments that = (Segments) o;
        return segments.equals(that.segments);

    }

    @Override
    public int hashCode() {
        return segments.hashCode();
    }

    boolean isEmpty() {
        return segments.isEmpty();
    }

    @Override
    public String toString() {
        return segments.toString();
    }
}
