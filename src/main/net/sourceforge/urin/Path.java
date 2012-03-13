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
 * An ordered collection of <code>Segment</code>s.
 * {@code Path}s can either be absolute (starting with '/'), or rootless (not starting with '/').
 * <p/>
 * Immutable and threadsafe.
 *
 * @see <a href="http://tools.ietf.org/html/rfc3986#section-3.3">RFC 3986 - Path</a>
 */
public abstract class Path {

    /**
     * Factory method for creating rootless <code>Path</code>s from <code>String</code>s.
     *
     * @param firstSegment a <code>String</code> representing the first segment.
     * @param segments     any further segments.
     * @return a <code>Path</code> representing the given <code>String</code>s.
     */
    public static Path rootlessPath(final String firstSegment, final String... segments) {
        final List<Segment> segmentList = new ArrayList<Segment>(segments.length + 1);
        segmentList.add(segment(firstSegment));
        for (String segment : segments) {
            segmentList.add(segment(segment));
        }
        return rootlessPath(segmentList);
    }

    /**
     * Factory method for creating rootless <code>Path</code>s from <code>Segment</code>s.
     *
     * @param segments <code>Segment</code>s that will be represented by this <code>Path</code>.
     * @return a <code>Path</code> representing the given <code>Segment</code>s.
     */
    public static Path rootlessPath(final Segment... segments) {
        return rootlessPath(asList(segments));
    }

    /**
     * Factory method for creating rootless <code>Path</code>s from an <code>Iterable</code> of <code>Segment</code>s.
     *
     * @param segments <code>Iterable</code> of <code>Segment</code>s that will be represented by this <code>Path</code>.
     * @return a <code>Path</code> representing the given <code>Segment</code>s.
     */
    public static Path rootlessPath(final Iterable<Segment> segments) {
        if (segments.iterator().hasNext()) {
            return new RootlessPath(segments);
        } else {
            return new EmptyPath();
        }
    }

    /**
     * Factory method for creating <code>AbsolutePath</code>s from <code>String</code>s.
     *
     * @param firstSegment a <code>String</code> representing the first segment.
     * @param segments     any further segments.
     * @return a <code>AbsolutePath</code> representing the given <code>String</code>s.
     */
    public static AbsolutePath path(final String firstSegment, final String... segments) {
        final List<Segment> segmentList = new ArrayList<Segment>(segments.length + 1);
        segmentList.add(segment(firstSegment));
        for (String segment : segments) {
            segmentList.add(segment(segment));
        }
        return path(segmentList);
    }

    /**
     * Factory method for creating <code>AbsolutePath</code>s from <code>Segment</code>s.
     *
     * @param segments <code>Segment</code>s that will be represented by this <code>AbsolutePath</code>.
     * @return a <code>AbsolutePath</code> representing the given <code>Segment</code>s.
     */
    public static AbsolutePath path(final Segment... segments) {
        return path(asList(segments));
    }

    /**
     * Factory method for creating <code>AbsolutePath</code>s from an <code>Iterable</code> of <code>Segment</code>s.
     *
     * @param segments <code>Iterable</code> of <code>Segment</code>s that will be represented by this <code>AbsolutePath</code>.
     * @return a <code>AbsolutePath</code> representing the given <code>Segment</code>s.
     */
    public static AbsolutePath path(final Iterable<Segment> segments) {
        return new AbsolutePath(segments);
    }

    static Path parseRootlessPath(final String rawPath) throws ParseException {
        return rootlessPath(rawPath == null ? new ArrayList<Segment>() : new ArrayList<Segment>() {{
            for (String segmentString : rawPath.split("/")) {
                add(Segment.parse(segmentString));
            }
        }});
    }

    static AbsolutePath parseParse(final String rawPath) throws ParseException {
        return path(new ArrayList<Segment>() {{
            boolean isFirst = true;
            for (String segmentString : rawPath.split("/")) {
                if (!isFirst) {
                    add(Segment.parse(segmentString));
                }
                isFirst = false;
            }
        }});
    }

    Path() {
    }

    abstract boolean firstPartIsSuppliedButIsEmpty();

    abstract String asString(final PrefixWithDotSegmentCriteria prefixWithDotSegmentCriteria);

    abstract boolean isEmpty();

    abstract boolean firstPartIsSuppliedButContainsColon();

    abstract Path resolveRelativeTo(final Path basePath);

    abstract Path replaceLastSegmentWith(final Iterable<Segment> segments);

    static enum PrefixWithDotSegmentCriteria {
        NEVER_PREFIX_WITH_DOT_SEGMENT {
            @Override
            boolean matches(final Path path) {
                return false;
            }
        },
        PREFIX_WITH_DOT_SEGMENT_IF_FIRST_CONTAINS_COLON {
            @Override
            boolean matches(final Path path) {
                return path.firstPartIsSuppliedButContainsColon();
            }
        },
        PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY {
            @Override
            boolean matches(final Path path) {
                return path.firstPartIsSuppliedButIsEmpty();
            }
        },
        PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON {
            @Override
            boolean matches(final Path path) {
                return path.firstPartIsSuppliedButIsEmpty() || path.firstPartIsSuppliedButContainsColon();
            }
        };

        abstract boolean matches(final Path path);
    }
}
