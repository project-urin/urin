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
import java.util.Collection;
import java.util.Iterator;

final class SegmentsHelper {

    private SegmentsHelper() {
        // deliberately empty
    }

    static Iterable<Segment> appendSegmentsTo(final Collection<Segment> baseSegments, final Iterable<Segment> appendedSegments) {
        return new ArrayList<Segment>() {{
            Iterator<Segment> baseSegmentsIterator = baseSegments.iterator();
            while (baseSegmentsIterator.hasNext()) {
                Segment baseSegment = baseSegmentsIterator.next();
                if (baseSegmentsIterator.hasNext()) {
                    add(baseSegment);
                }
            }
            for (Segment segment : appendedSegments) {
                add(segment);
            }
        }};
    }
}
