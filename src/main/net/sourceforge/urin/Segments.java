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

/**
 * An ordered collection of <code>Segment</code>s.  Segments can either be absolute (starting with '/'), or rootless (not starting with '/').
 */
public abstract class Segments {

    /**
     * Factory method for creating rootless <code>Segments</code> from <code>String</code>s.
     *
     * @param firstSegment a <code>String</code> representing the first segment.
     * @param segments     any further segments.
     * @return a <code>Segments</code> representing the given <code>String</code>s.
     */
    public static Segments rootlessSegments(final String firstSegment, final String... segments) {
        final List<Segment> segmentList = new ArrayList<Segment>(segments.length + 1);
        segmentList.add(segment(firstSegment));
        for (String segment : segments) {
            segmentList.add(segment(segment));
        }
        return rootlessSegments(segmentList);
    }

    /**
     * Factory method for creating rootless <code>Segments</code> from <code>Segment</code>s.
     *
     * @param segments <code>Segment</code>s that will be represented by this <code>Segments</code>.
     * @return a <code>Segments</code> representing the given <code>Segment</code>s.
     */
    public static Segments rootlessSegments(final Segment... segments) {
        return rootlessSegments(asList(segments));
    }

    /**
     * Factory method for creating rootless <code>Segments</code> from an <code>Iterable</code> of <code>Segment</code>s.
     *
     * @param segments <code>Iterable</code> of <code>Segment</code>s that will be represented by this <code>Segments</code>.
     * @return a <code>Segments</code> representing the given <code>Segment</code>s.
     */
    public static Segments rootlessSegments(final Iterable<Segment> segments) {
        if (segments.iterator().hasNext()) {
            return new RootlessSegments(segments);
        } else {
            return new EmptySegments();
        }
    }

    /**
     * Factory method for creating <code>AbsoluteSegments</code> from <code>String</code>s.
     *
     * @param firstSegment a <code>String</code> representing the first segment.
     * @param segments     any further segments.
     * @return a <code>AbsoluteSegments</code> representing the given <code>String</code>s.
     */
    public static AbsoluteSegments segments(final String firstSegment, final String... segments) {
        final List<Segment> segmentList = new ArrayList<Segment>(segments.length + 1);
        segmentList.add(segment(firstSegment));
        for (String segment : segments) {
            segmentList.add(segment(segment));
        }
        return segments(segmentList);
    }

    /**
     * Factory method for creating <code>AbsoluteSegments</code> from <code>Segment</code>s.
     *
     * @param segments <code>Segment</code>s that will be represented by this <code>AbsoluteSegments</code>.
     * @return a <code>AbsoluteSegments</code> representing the given <code>Segment</code>s.
     */
    public static AbsoluteSegments segments(final Segment... segments) {
        return segments(asList(segments));
    }

    /**
     * Factory method for creating <code>AbsoluteSegments</code> from an <code>Iterable</code> of <code>Segment</code>s.
     *
     * @param segments <code>Iterable</code> of <code>Segment</code>s that will be represented by this <code>AbsoluteSegments</code>.
     * @return a <code>AbsoluteSegments</code> representing the given <code>Segment</code>s.
     */
    public static AbsoluteSegments segments(final Iterable<Segment> segments) {
        return new AbsoluteSegments(segments);
    }

    static Segments parseRootlessSegments(final String rawPath) throws ParseException {
        return rootlessSegments(rawPath == null ? new ArrayList<Segment>() : new ArrayList<Segment>() {{
            for (String segmentString : rawPath.split("/")) {
                add(Segment.parse(segmentString));
            }
        }});
    }

    static AbsoluteSegments parseSegments(final String rawPath) throws ParseException {
        return segments(new ArrayList<Segment>() {{
            boolean isFirst = true;
            for (String segmentString : rawPath.split("/")) {
                if (!isFirst) {
                    add(Segment.parse(segmentString));
                }
                isFirst = false;
            }
        }});
    }

    abstract boolean firstPartIsSuppliedButIsEmpty();

    abstract String asString(final PrefixWithDotSegmentCriteria prefixWithDotSegmentCriteria);

    abstract boolean isEmpty();

    abstract boolean firstPartIsSuppliedButContainsColon();

    abstract Segments resolveRelativeTo(final Segments baseSegments);

    abstract Segments replaceLastSegmentWith(final Iterable<Segment> segments);

    static enum PrefixWithDotSegmentCriteria {
        NEVER_PREFIX_WITH_DOT_SEGMENT {
            @Override
            boolean matches(final Segments segments) {
                return false;
            }
        },
        PREFIX_WITH_DOT_SEGMENT_IF_FIRST_CONTAINS_COLON {
            @Override
            boolean matches(final Segments segments) {
                return segments.firstPartIsSuppliedButContainsColon();
            }
        },
        PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY {
            @Override
            boolean matches(final Segments segments) {
                return segments.firstPartIsSuppliedButIsEmpty();
            }
        },
        PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON {
            @Override
            boolean matches(final Segments segments) {
                return segments.firstPartIsSuppliedButIsEmpty() || segments.firstPartIsSuppliedButContainsColon();
            }
        };

        abstract boolean matches(final Segments segments);
    }
}
