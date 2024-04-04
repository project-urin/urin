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

final class PathHelper {

    private PathHelper() {
        // deliberately empty
    }

    static <T> Iterable<Segment<T>> appendSegmentsTo(final Collection<Segment<T>> baseSegments, final Iterable<Segment<T>> appendedSegments) {
        final List<Segment<T>> result = new ArrayList<>();
        final Iterator<Segment<T>> baseSegmentsIterator = baseSegments.iterator();
        while (baseSegmentsIterator.hasNext()) {
            final Segment<T> baseSegment = baseSegmentsIterator.next();
            if (baseSegmentsIterator.hasNext()) {
                result.add(baseSegment);
            }
        }
        for (final Segment<T> segment : appendedSegments) {
            result.add(segment);
        }
        return result;
    }
}
