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
import java.util.List;

import static java.util.Arrays.asList;
import static net.sourceforge.urin.Segment.segment;

public abstract class Segments {

    public static Segments rootlessSegments(final String firstSegment, final String... segments) {
        final List<Segment> segmentList = new ArrayList<Segment>(segments.length + 1);
        segmentList.add(segment(firstSegment));
        for (String segment : segments) {
            segmentList.add(segment(segment));
        }
        return rootlessSegments(segmentList);
    }

    public static Segments rootlessSegments(final Segment... segments) {
        return rootlessSegments(asList(segments));
    }

    public static Segments rootlessSegments(final Iterable<Segment> segments) {
        if (segments.iterator().hasNext()) {
            return new RootlessSegments(segments);
        } else {
            return new EmptySegments();
        }
    }

    public static AbsoluteSegments segments(final String firstSegment, final String... segments) {
        final List<Segment> segmentList = new ArrayList<Segment>(segments.length + 1);
        segmentList.add(segment(firstSegment));
        for (String segment : segments) {
            segmentList.add(segment(segment));
        }
        return segments(segmentList);
    }

    public static AbsoluteSegments segments(final Segment... segments) {
        return segments(asList(segments));
    }

    public static AbsoluteSegments segments(final Iterable<Segment> segments) {
        return new AbsoluteSegments(segments);
    }

    abstract boolean firstPartIsSuppliedButIsEmpty();

    abstract String asString(final boolean allowColonInFirstSegment);

    abstract boolean isEmpty();

}
