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

import java.util.*;

import static java.util.Arrays.asList;
import static net.sourceforge.urin.Segment.*;

public class RelativeSegments extends Segments {

    private final Collection<Segment> segments;

    public static RelativeSegments relativeSegments(final String firstSegment, final String... segments) {
        final List<Segment> segmentList = new ArrayList<Segment>(segments.length + 1);
        segmentList.add(segment(firstSegment));
        for (String segment : segments) {
            segmentList.add(segment(segment));
        }
        return new RelativeSegments(segmentList);
    }

    public static RelativeSegments relativeSegments(final Segment... segments) {
        return new RelativeSegments(asList(segments));
    }

    public static RelativeSegments relativeSegments(final Iterable<Segment> segments) {
        return new RelativeSegments(segments);
    }

    private RelativeSegments(final Iterable<Segment> segments) {
        LinkedList<Segment> newSegments = new LinkedList<Segment>();
        for (Segment segment : segments) {
            if (segment == null) {
                throw new NullPointerException("Segment cannot be null");
            } else {
                if (!DOT.equals(segment)) {
                    if (DOT_DOT.equals(segment)) {
                        if ((newSegments.size() == 1 && newSegments.getLast().isEmpty())) {

                        } else if (newSegments.size() > 1) {
                            newSegments.removeLast();
                        }
                    } else {
                        newSegments.add(segment);
                    }
                }
            }
        }
        this.segments = newSegments;
    }

    boolean firstPartIsSuppliedButIsEmpty() {
        return !segments.isEmpty() && EMPTY.equals(segments.iterator().next());
    }

    private boolean firstPartIsSuppliedButContainsColon() {
        return !segments.isEmpty() && segments.iterator().next().containsColon();
    }

    String asString(final boolean allowColonInFirstSegment) {
        StringBuilder result = new StringBuilder();
        if (firstPartIsSuppliedButContainsColon() && !allowColonInFirstSegment) {
            result.append("./");
        }
        Iterator<Segment> segmentIterator = segments.iterator();
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

        RelativeSegments segments1 = (RelativeSegments) o;
        return segments.equals(segments1.segments);
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
