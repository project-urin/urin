/*
 * Copyright 2020 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.urin;

import java.util.*;

import static java.util.Objects.requireNonNull;
import static net.sourceforge.urin.PathHelper.appendSegmentsTo;
import static net.sourceforge.urin.Segment.dot;
import static net.sourceforge.urin.Segment.dotDot;

final class RootlessPath<T> extends Path<T> {

    private final Collection<Segment<T>> segments;

    RootlessPath(final Iterable<Segment<T>> segments) {
        LinkedList<Segment<T>> newSegments = new LinkedList<>();
        Iterator<Segment<T>> segmentIterator = segments.iterator();
        while (segmentIterator.hasNext()) {
            Segment<T> segment = requireNonNull(segmentIterator.next(), "Segment cannot be null");
            if (!dot().equals(segment)) {
                if (dotDot().equals(segment) && !newSegments.isEmpty() && !dotDot().equals(newSegments.getLast())) {
                    Segment<T> removedSegment = newSegments.removeLast();
                    if (dot().equals(removedSegment)) {
                        if (!newSegments.isEmpty() && !dotDot().equals(newSegments.getLast())) {
                            newSegments.removeLast();
                            if (!segmentIterator.hasNext()) {
                                newSegments.add(Segment.empty());
                            }
                        } else {
                            newSegments.add(Segment.dotDot());
                        }
                    } else {
                        if (!segmentIterator.hasNext()) {
                            newSegments.add(Segment.empty());
                        }
                    }
                } else {
                    if (!newSegments.isEmpty() && dot().equals(newSegments.getLast())) {
                        newSegments.removeLast();
                    }
                    newSegments.add(segment.isEmpty() ? Segment.empty() : segment);
                }
            } else {
                if (newSegments.isEmpty()) {
                    newSegments.add(Segment.dot());
                } else {
                    if (!segmentIterator.hasNext()) {
                        newSegments.add(Segment.empty());
                    }
                }
            }
        }
        this.segments = newSegments;
    }

    boolean firstPartIsSuppliedButIsEmpty() {
        return !segments.isEmpty() && segments.iterator().next().isEmpty();
    }

    @Override
    boolean firstPartIsSuppliedButContainsColon() {
        return !segments.isEmpty() && segments.iterator().next().containsColon();
    }

    @Override
    Path<T> resolveRelativeTo(final Path<T> basePath) {
        return basePath.replaceLastSegmentWith(segments);
    }

    @Override
    RootlessPath<T> replaceLastSegmentWith(final Iterable<Segment<T>> segments) {
        return new RootlessPath<>(appendSegmentsTo(this.segments, segments));
    }

    @Override
    public boolean isAbsolute() {
        return false;
    }

    @Override
    public List<Segment<T>> segments() {
        return new ArrayList<>(segments);
    }

    String asString(final PrefixWithDotSegmentCriteria prefixWithDotSegmentCriteria) {
        StringBuilder result = new StringBuilder();
        if (prefixWithDotSegmentCriteria.matches(this)) {
            result.append("./");
        }
        Iterator<Segment<T>> segmentIterator = segments.iterator();
        while (segmentIterator.hasNext()) {
            result.append(segmentIterator.next().asString());
            if (segmentIterator.hasNext()) {
                result.append('/');
            }
        }
        return result.toString();
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        RootlessPath<?> rootlessPath = (RootlessPath<?>) object;
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

    public Iterator<Segment<T>> iterator() {
        return segments.iterator();
    }
}
