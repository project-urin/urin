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

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import static net.sourceforge.urin.PathHelper.appendSegmentsTo;
import static net.sourceforge.urin.Segment.*;

public final class AbsolutePath extends Path {

    private final Collection<Segment> segments;

    AbsolutePath(final Iterable<Segment> segments) {
        LinkedList<Segment> newSegments = new LinkedList<Segment>();
        Iterator<Segment> segmentIterator = segments.iterator();
        while (segmentIterator.hasNext()) {
            Segment segment = segmentIterator.next();
            if (segment == null) {
                throw new NullPointerException("Segment cannot be null");
            } else {
                if (!DOT.equals(segment)) {
                    if (DOT_DOT.equals(segment)) {
                        if (!newSegments.isEmpty()) {
                            newSegments.removeLast();
                            if (!segmentIterator.hasNext()) {
                                newSegments.add(EMPTY);
                            }
                        }
                    } else {
                        newSegments.add(segment);
                    }
                } else {
                    if (!segmentIterator.hasNext()) {
                        newSegments.add(EMPTY);
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
    AbsolutePath resolveRelativeTo(final Path basePath) {
        return this;
    }

    @Override
    Path replaceLastSegmentWith(final Iterable<Segment> segments) {
        return new AbsolutePath(appendSegmentsTo(this.segments, segments));
    }

    String asString(final PrefixWithDotSegmentCriteria prefixWithDotSegmentCriteria) {
        StringBuilder result = new StringBuilder("/");
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

        AbsolutePath path = (AbsolutePath) o;
        return segments.equals(path.segments);
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
