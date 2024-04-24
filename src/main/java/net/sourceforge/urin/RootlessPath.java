/*
 * Copyright 2024 Mark Slater
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
import java.util.List;

import static net.sourceforge.urin.PathHelper.appendSegmentsTo;

final class RootlessPath<T> extends Path<T> {

    private final Collection<Segment<T>> segments;

    RootlessPath(final Iterable<Segment<T>> segments) {
        this.segments = normaliseRootless(segments);
    }

    @Override
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

    @Override
    String asString(final PrefixWithDotSegmentCriteria prefixWithDotSegmentCriteria) {
        final StringBuilder result = new StringBuilder();
        if (prefixWithDotSegmentCriteria.matches(this)) {
            result.append("./");
        }
        final Iterator<Segment<T>> segmentIterator = segments.iterator();
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
        if (this == object) {
            return true;
        } else if (object == null || getClass() != object.getClass()) {
            return false;
        }

        final RootlessPath<?> rootlessPath = (RootlessPath<?>) object;
        return segments.equals(rootlessPath.segments);
    }

    @Override
    public int hashCode() {
        return segments.hashCode();
    }

    @Override
    boolean isEmpty() {
        return segments.isEmpty();
    }

    @Override
    public String toString() {
        return segments.toString();
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public Iterator<Segment<T>> iterator() {
        return segments.iterator();
    }
}
