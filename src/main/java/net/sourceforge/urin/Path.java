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
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static net.sourceforge.urin.Segment.*;

/**
 * An iterable of {@code Segment}s.
 * {@code Path}s can either be absolute (starting with '/'), or rootless (not starting with '/').
 * <p>
 * Immutable and thread safe.
 *
 * @see <a href="http://tools.ietf.org/html/rfc3986#section-3.3">RFC 3986 - Path</a>
 */
public abstract class Path<T> implements Iterable<Segment<T>> {

    Path() {
    }

    /**
     * Factory method for creating rootless {@code Path}s from {@code String}s.
     *
     * @param firstSegment a {@code String} representing the first segment.
     * @param segments     any further segments.
     * @return a {@code Path} representing the given {@code String}s.
     */
    public static Path<String> rootlessPath(final String firstSegment, final String... segments) {
        final List<Segment<String>> segmentList = new ArrayList<>(segments.length + 1);
        segmentList.add(segment(firstSegment));
        for (final String segment : segments) {
            segmentList.add(segment(segment));
        }
        return rootlessPath(segmentList);
    }

    /**
     * Factory method for creating empty rootless {@code Path}s.
     *
     * @param <ENCODES> The type of value represented by the path segments - {@code String} in the general case.
     * @return an empty {@code Path}.
     */
    public static <ENCODES> Path<ENCODES> rootlessPath() {
        return rootlessPath(emptyList());
    }

    /**
     * Factory method for creating rootless {@code Path}s from {@code Segment}s.
     *
     * @param segments  {@code Segment}s that will be represented by this {@code Path}.
     * @param <ENCODES> The type of value represented by the path segments - {@code String} in the general case.
     * @return a {@code Path} representing the given {@code Segment}s.
     */
    @SafeVarargs
    public static <ENCODES> Path<ENCODES> rootlessPath(final Segment<ENCODES>... segments) {
        return rootlessPath(asList(segments));
    }

    /**
     * Factory method for creating rootless {@code Path}s from an {@code Iterable} of {@code Segment}s.
     *
     * @param segments  {@code Iterable} of {@code Segment}s that will be represented by this {@code Path}.
     * @param <ENCODES> The type of value represented by the path segments - {@code String} in the general case.
     * @return a {@code Path} representing the given {@code Segment}s.
     */
    public static <ENCODES> Path<ENCODES> rootlessPath(final Iterable<Segment<ENCODES>> segments) {
        if (segments.iterator().hasNext()) {
            return new RootlessPath<>(segments);
        } else {
            return new EmptyPath<>();
        }
    }

    /**
     * Factory method for creating {@code AbsolutePath}s from {@code String}s.
     *
     * @param firstSegment a {@code String} representing the first segment.
     * @param segments     any further segments.
     * @return a {@code AbsolutePath} representing the given {@code String}s.
     */
    public static AbsolutePath<String> path(final String firstSegment, final String... segments) {
        final List<Segment<String>> segmentList = new ArrayList<>(segments.length + 1);
        segmentList.add(segment(firstSegment));
        for (final String segment : segments) {
            segmentList.add(segment(segment));
        }
        return path(segmentList);
    }

    /**
     * Factory method for creating an empty {@code AbsolutePath}.
     *
     * @param <ENCODES> The type of value represented by the path segments - {@code String} in the general case.
     * @return an empty {@code AbsolutePath}.
     */
    public static <ENCODES> AbsolutePath<ENCODES> path() {
        return path(emptyList());
    }

    /**
     * Factory method for creating {@code AbsolutePath}s from {@code Segment}s.
     *
     * @param segments  {@code Segment}s that will be represented by this {@code AbsolutePath}.
     * @param <ENCODES> The type of value represented by the path segments - {@code String} in the general case.
     * @return a {@code AbsolutePath} representing the given {@code Segment}s.
     */
    @SafeVarargs
    public static <ENCODES> AbsolutePath<ENCODES> path(final Segment<ENCODES>... segments) {
        return path(asList(segments));
    }

    /**
     * Factory method for creating {@code AbsolutePath}s from an {@code Iterable} of {@code Segment}s.
     *
     * @param segments  {@code Iterable} of {@code Segment}s that will be represented by this {@code AbsolutePath}.
     * @param <ENCODES> The type of value represented by the path segments - {@code String} in the general case.
     * @return a {@code AbsolutePath} representing the given {@code Segment}s.
     */
    public static <ENCODES> AbsolutePath<ENCODES> path(final Iterable<Segment<ENCODES>> segments) {
        return new AbsolutePath<>(segments);
    }

    static <SEGMENT> Path<SEGMENT> parseRootlessPath(final String rawPath, final MakingDecoder<Segment<SEGMENT>, ?, String> segmentMakingDecoder) throws ParseException {
        if (rawPath == null || rawPath.isEmpty()) {
            return rootlessPath(emptyList());
        } else {
            final String[] segmentStrings = rawPath.split("/", -1);
            final List<Segment<SEGMENT>> result = new ArrayList<>(segmentStrings.length);
            for (final String segmentString : segmentStrings) {
                result.add(parse(segmentString, segmentMakingDecoder));
            }
            return rootlessPath(result);
        }
    }

    static <SEGMENT> AbsolutePath<SEGMENT> parsePath(final String rawPath, final MakingDecoder<Segment<SEGMENT>, ?, String> segmentMakingDecoder) throws ParseException {
        if ("/".equals(rawPath)) {
            return path(emptyList());
        } else {
            final String[] segmentStrings = rawPath.split("/", -1);
            final List<Segment<SEGMENT>> result = new ArrayList<>(segmentStrings.length - 1);
            boolean isFirst = true;
            for (final String segmentString : segmentStrings) {
                if (!isFirst) {
                    result.add(parse(segmentString, segmentMakingDecoder));
                }
                isFirst = false;
            }
            return path(result);
        }
    }

    static <T> Deque<Segment<T>> normaliseRootless(final Iterable<Segment<T>> segments) { // TODO consider whether we should normalise by effect, e.g. the relative references . and ./ are equivalent, as are .. and ../ (we'd call the latter of each "empty")
        final Deque<Segment<T>> normalisedSegments = new LinkedList<>();
        for (final Segment<T> next : segments) {
            if (normalisedSegments.isEmpty()) {
                normalisedSegments.add(next);
            } else {
                Segment<T> current = normalisedSegments.removeLast();
                if (dot().equals(current) && !normalisedSegments.isEmpty()) {
                    current = normalisedSegments.removeLast();
                }
                normalisedSegments.addAll(current.incorporate(next));
            }
        }
        if (normalisedSegments.size() > 1 && dot().equals(normalisedSegments.getLast())) {
            normalisedSegments.removeLast();
            normalisedSegments.add(empty());
        }
        return normalisedSegments;
    }

    abstract boolean firstPartIsSuppliedButIsEmpty();

    abstract String asString(PrefixWithDotSegmentCriteria prefixWithDotSegmentCriteria);

    abstract boolean isEmpty();

    abstract boolean firstPartIsSuppliedButContainsColon();

    abstract Path<T> resolveRelativeTo(Path<T> basePath);

    abstract Path<T> replaceLastSegmentWith(Iterable<Segment<T>> segments);

    /**
     * Indicates whether this path is absolute (begins with '/') or not.
     *
     * @return whether this path is absolute (begins with '/') or not.
     */
    public abstract boolean isAbsolute();

    /**
     * Returns the list of {@code Segment}s that this path represents.
     * Note that this {@code List} contains the same elements as returned by calling {@code iterator()} on {@code this}.
     *
     * @return the list of {@code Segment}s that this path represents.
     */
    public abstract List<Segment<T>> segments();

    enum PrefixWithDotSegmentCriteria {
        NEVER_PREFIX_WITH_DOT_SEGMENT {
            @Override
            boolean matches(final Path<?> path) {
                return false;
            }
        },
        PREFIX_WITH_DOT_SEGMENT_IF_FIRST_CONTAINS_COLON {
            @Override
            boolean matches(final Path<?> path) {
                return path.firstPartIsSuppliedButContainsColon();
            }
        },
        PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY {
            @Override
            boolean matches(final Path<?> path) {
                return path.firstPartIsSuppliedButIsEmpty();
            }
        },
        PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON {
            @Override
            boolean matches(final Path<?> path) {
                return path.firstPartIsSuppliedButIsEmpty() || path.firstPartIsSuppliedButContainsColon();
            }
        };

        abstract boolean matches(Path<?> path);
    }
}
