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

import static net.sourceforge.urin.PathHelper.appendSegmentsTo;
import static net.sourceforge.urin.Segment.*;

final class RootlessPath extends Path {

    private final Collection<Segment> segments;

    RootlessPath(final Iterable<Segment> segments) {
        LinkedList<Segment> newSegments = new LinkedList<Segment>();
        Iterator<Segment> segmentIterator = segments.iterator();
        while (segmentIterator.hasNext()) {
            Segment segment = segmentIterator.next();
            if (segment == null) {
                throw new NullPointerException("Segment cannot be null");
            } else {
                if (!DOT.equals(segment)) {
                    if (DOT_DOT.equals(segment) && !newSegments.isEmpty() && !DOT_DOT.equals(newSegments.getLast())) {
                        Segment removedSegment = newSegments.removeLast();
                        if (DOT.equals(removedSegment)) {
                            if (!newSegments.isEmpty() && !DOT_DOT.equals(newSegments.getLast())) {
                                newSegments.removeLast();
                                if (!segmentIterator.hasNext()) {
                                    newSegments.add(EMPTY);
                                }
                            } else {
                                newSegments.add(DOT_DOT);
                            }
                        } else {
                            if (!segmentIterator.hasNext()) {
                                newSegments.add(EMPTY);
                            }
                        }
                    } else {
                        if (!newSegments.isEmpty() && DOT.equals(newSegments.getLast())) {
                            newSegments.removeLast();
                        }
                        newSegments.add(segment);
                    }
                } else {
                    if (newSegments.isEmpty()) {
                        newSegments.add(DOT);
                    } else {
                        if (!segmentIterator.hasNext()) {
                            newSegments.add(EMPTY);
                        }
                    }
                }
            }
        }
        this.segments = newSegments;
    }

    boolean firstPartIsSuppliedButIsEmpty() {
        return !segments.isEmpty() && EMPTY.equals(segments.iterator().next());
    }

    @Override
    boolean firstPartIsSuppliedButContainsColon() {
        return !segments.isEmpty() && segments.iterator().next().containsColon();
    }

    @Override
    Path resolveRelativeTo(final Path basePath) {
        return basePath.replaceLastSegmentWith(segments);
    }

    @Override
    RootlessPath replaceLastSegmentWith(final Iterable<Segment> segments) {
        return new RootlessPath(appendSegmentsTo(this.segments, segments));
    }

    @Override
    public boolean isAbsolute() {
        return false;
    }

    @Override
    public List<Segment> segments() {
        return new ArrayList<Segment>(segments);
    }

    String asString(final PrefixWithDotSegmentCriteria prefixWithDotSegmentCriteria) {
        StringBuilder result = new StringBuilder();
        if (prefixWithDotSegmentCriteria.matches(this)) {
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

        RootlessPath rootlessPath = (RootlessPath) o;
        return segments.equals(rootlessPath.segments);
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

    public Iterator<Segment> iterator() {
        return segments.iterator();
    }
}
