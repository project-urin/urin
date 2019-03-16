/*
 * Copyright 2019 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.urin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static net.sourceforge.urin.PathHelper.appendSegmentsTo;
import static net.sourceforge.urin.Segment.dot;
import static net.sourceforge.urin.Segment.dotDot;

/**
 * A path that begins with a '/' - typically representing a path relative to root.
 *
 * To create instances, see {@link Path}.
 */
public final class AbsolutePath<T> extends Path<T> {

    private final Collection<Segment<T>> segments;

    AbsolutePath(final Iterable<Segment<T>> segments) {
        LinkedList<Segment<T>> newSegments = new LinkedList<>();
        Iterator<Segment<T>> segmentIterator = segments.iterator();
        while (segmentIterator.hasNext()) {
            Segment<T> segment = segmentIterator.next();
            if (segment == null) {
                throw new NullPointerException("Segment cannot be null");
            } else {
                if (!dot().equals(segment)) {
                    if (dotDot().equals(segment)) {
                        if (!newSegments.isEmpty()) {
                            newSegments.removeLast();
                            if (!segmentIterator.hasNext()) {
                                newSegments.add(Segment.<T>empty());
                            }
                        }
                    } else {
                        newSegments.add(segment.isEmpty() ? Segment.<T>empty() : segment);
                    }
                } else {
                    if (!segmentIterator.hasNext()) {
                        newSegments.add(Segment.<T>empty());
                    }
                }
            }
        }
        this.segments = newSegments.size() == 1 && newSegments.getFirst().isEmpty() ? new LinkedList<Segment<T>>() : newSegments;
    }


    boolean firstPartIsSuppliedButIsEmpty() {
        return !segments.isEmpty() && segments.iterator().next().isEmpty();
    }

    @Override
    boolean firstPartIsSuppliedButContainsColon() {
        return !segments.isEmpty() && segments.iterator().next().containsColon();
    }

    @Override
    AbsolutePath<T> resolveRelativeTo(final Path<T> basePath) {
        return this;
    }

    @Override
    Path<T> replaceLastSegmentWith(final Iterable<Segment<T>> segments) {
        return new AbsolutePath<>(appendSegmentsTo(this.segments, segments));
    }

    @Override
    public boolean isAbsolute() {
        return true;
    }

    @Override
    public List<Segment<T>> segments() {
        return new ArrayList<>(segments);
    }

    String asString(final PrefixWithDotSegmentCriteria prefixWithDotSegmentCriteria) {
        StringBuilder result = new StringBuilder("/");
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

    public Iterator<Segment<T>> iterator() {
        return segments.iterator();
    }
}
