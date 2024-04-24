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

import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static net.sourceforge.urin.MoreMatchers.contains;
import static net.sourceforge.urin.MoreRandomStringUtils.aString;
import static net.sourceforge.urin.Path.PrefixWithDotSegmentCriteria.NEVER_PREFIX_WITH_DOT_SEGMENT;
import static net.sourceforge.urin.Path.path;
import static net.sourceforge.urin.PathBuilder.aRootlessPath;
import static net.sourceforge.urin.PathBuilder.anAbsolutePath;
import static net.sourceforge.urin.Segment.segment;
import static net.sourceforge.urin.SegmentBuilder.aNonDotSegment;
import static net.sourceforge.urin.SegmentBuilder.aSegment;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

class AbsolutePathTest {
    @Test
    void anAbsolutePathIsEqualToAnotherAbsolutePathWithTheSameMembers() {
        final Segment<String> firstSegment = aSegment();
        final Segment<String> secondSegment = aSegment();
        assertThat(path(firstSegment, secondSegment), equalTo(path(firstSegment, secondSegment)));
        assertThat(path(firstSegment, secondSegment).hashCode(), equalTo(path(firstSegment, secondSegment).hashCode()));
    }

    @Test
    void aPathUsingVarargsFactoryIsEqualToWithTheSameMembersMadeUsingIterableFactory() {
        final Segment<String> firstSegment = aSegment();
        final Segment<String> secondSegment = aSegment();
        assertThat(path(firstSegment, secondSegment), equalTo(path(asList(firstSegment, secondSegment))));
        assertThat(path(firstSegment, secondSegment).hashCode(), equalTo(path(asList(firstSegment, secondSegment)).hashCode()));
    }

    @Test
    void aPathUsingSegmentVarargsFactoryIsEqualToWithTheSameMembersMadeUsingStringVarargsFactory() {
        final String firstSegmentValue = aString();
        final Segment<String> firstSegment = segment(firstSegmentValue);
        final String secondSegmentValue = aString();
        final Segment<String> secondSegment = segment(secondSegmentValue);
        assertThat(path(firstSegment, secondSegment), equalTo(path(firstSegmentValue, secondSegmentValue)));
        assertThat(path(firstSegment, secondSegment).hashCode(), equalTo(path(firstSegmentValue, secondSegmentValue).hashCode()));
    }

    @Test
    void aPathUsingSegmentVarargsIsImmutable() {
        final Segment<String> firstSegment = aNonDotSegment();
        final Segment<String> secondSegment = aNonDotSegment();
        @SuppressWarnings("rawtypes") final Segment[] segments = {firstSegment, secondSegment};
        @SuppressWarnings("unchecked") final AbsolutePath<String> absolutePath = path(segments);
        segments[0] = aSegment();
        assertThat(absolutePath.asString(NEVER_PREFIX_WITH_DOT_SEGMENT), equalTo("/" + firstSegment.asString() + "/" + secondSegment.asString()));
    }

    @Test
    void aPathIsNotEqualToAnotherPathWithDifferentMembers() {
        assertThat(path(aSegment(), aNonDotSegment()), not(equalTo(path(aSegment(), aNonDotSegment()))));
    }

    @Test
    void aPathToStringIsCorrect() {
        final Segment<String> firstSegment = aNonDotSegment();
        final Segment<String> secondSegment = aNonDotSegment();
        assertThat(path(firstSegment, secondSegment).toString(), equalTo("[" + firstSegment + ", " + secondSegment + "]"));
    }

    @Test
    void correctlyIdentifiesFirstPartAsBeingSuppliedButEmpty() {
        assertThat(path(Segment.empty(), aNonDotSegment()).firstPartIsSuppliedButIsEmpty(), equalTo(true));
    }

    @Test
    void correctlyIdentifiesFirstPartNotSupplied() {
        assertThat(path().firstPartIsSuppliedButIsEmpty(), equalTo(false));
    }

    @Test
    void correctlyIdentifiesFirstPartNonEmpty() {
        assertThat(path(aNonDotSegment()).firstPartIsSuppliedButIsEmpty(), equalTo(false));
    }

    @Test
    void removesDotPath() {
        final AbsolutePath<String> actual = path(segment("a"), segment("b"), segment("c"), Segment.dot(), Segment.dotDot(), Segment.dotDot(), segment("g"));
        assertThat(actual, equalTo(path(segment("a"), segment("g"))));
    }

    @Test
    void resolvesEmptyPath() {
        final AbsolutePath<String> path = anAbsolutePath();
        assertThat(path.resolveRelativeTo(new EmptyPath<>()), equalTo(path));
    }

    @Test
    void resolvesAbsolutePath() {
        final AbsolutePath<String> path = anAbsolutePath();
        final Path<String> basePath = anAbsolutePath();
        assertThat(path.resolveRelativeTo(basePath), equalTo(path));
    }

    @Test
    void resolvesRootlessPath() {
        final AbsolutePath<String> path = anAbsolutePath();
        final Path<String> basePath = aRootlessPath();
        assertThat(path.resolveRelativeTo(basePath), equalTo(path));
    }

    @Test
    void absolutePathIsAbsolute() {
        assertThat(anAbsolutePath().isAbsolute(), equalTo(true));
    }

    @Test
    void absolutePathIteratorContainsAllNonDotSegments() {
        final Segment<String> segmentOne = aNonDotSegment();
        final Segment<String> segmentTwo = aNonDotSegment();
        assertThat(path(segmentOne, segmentTwo), contains(segmentOne, segmentTwo));
    }

    @Test
    void absolutePathSegmentsContainsAllNonDotSegments() {
        final Segment<String> segmentOne = aNonDotSegment();
        final Segment<String> segmentTwo = aNonDotSegment();
        final AbsolutePath<String> path = path(segmentOne, segmentTwo);
        assertThat(path.segments(), contains(segmentOne, segmentTwo));
    }

    @Test
    void absolutePathSegmentsDoesNotExposeMutability() {
        final Segment<String> segmentOne = aNonDotSegment();
        final Segment<String> segmentTwo = aNonDotSegment();
        final Path<String> absolutePath = path(segmentOne, segmentTwo);
        absolutePath.segments().add(aSegment());
        assertThat(absolutePath.segments(), contains(segmentOne, segmentTwo));

    }

}
