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
        Segment<String> firstSegment = aSegment();
        Segment<String> secondSegment = aSegment();
        assertThat(AbsolutePath.path(firstSegment, secondSegment), equalTo(AbsolutePath.path(firstSegment, secondSegment)));
        assertThat(AbsolutePath.path(firstSegment, secondSegment).hashCode(), equalTo(AbsolutePath.path(firstSegment, secondSegment).hashCode()));
    }

    @Test
    void aPathUsingVarargsFactoryIsEqualToWithTheSameMembersMadeUsingIterableFactory() {
        Segment<String> firstSegment = aSegment();
        Segment<String> secondSegment = aSegment();
        assertThat(AbsolutePath.path(firstSegment, secondSegment), equalTo(AbsolutePath.path(asList(firstSegment, secondSegment))));
        assertThat(AbsolutePath.path(firstSegment, secondSegment).hashCode(), equalTo(AbsolutePath.path(asList(firstSegment, secondSegment)).hashCode()));
    }

    @Test
    void aPathUsingSegmentVarargsFactoryIsEqualToWithTheSameMembersMadeUsingStringVarargsFactory() {
        String firstSegmentValue = aString();
        Segment<String> firstSegment = segment(firstSegmentValue);
        String secondSegmentValue = aString();
        Segment<String> secondSegment = segment(secondSegmentValue);
        assertThat(AbsolutePath.path(firstSegment, secondSegment), equalTo(AbsolutePath.path(firstSegmentValue, secondSegmentValue)));
        assertThat(AbsolutePath.path(firstSegment, secondSegment).hashCode(), equalTo(AbsolutePath.path(firstSegmentValue, secondSegmentValue).hashCode()));
    }

    @Test
    void aPathUsingSegmentVarargsIsImmutable() {
        Segment<String> firstSegment = aNonDotSegment();
        Segment<String> secondSegment = aNonDotSegment();
        Segment[] segments = {firstSegment, secondSegment};
        @SuppressWarnings("unchecked") AbsolutePath<String> absolutePath = Path.path(segments);
        segments[0] = aSegment();
        assertThat(absolutePath.asString(NEVER_PREFIX_WITH_DOT_SEGMENT), equalTo("/" + firstSegment.asString() + "/" + secondSegment.asString()));
    }

    @Test
    void aPathIsNotEqualToAnotherPathWithDifferentMembers() {
        assertThat(AbsolutePath.path(aSegment(), aNonDotSegment()), not(equalTo(AbsolutePath.path(aSegment(), aNonDotSegment()))));
    }

    @Test
    void aPathToStringIsCorrect() {
        Segment<String> firstSegment = aNonDotSegment();
        Segment<String> secondSegment = aNonDotSegment();
        assertThat(AbsolutePath.path(firstSegment, secondSegment).toString(), equalTo("[" + firstSegment + ", " + secondSegment + "]"));
    }

    @Test
    void correctlyIdentifiesFirstPartAsBeingSuppliedButEmpty() {
        assertThat(AbsolutePath.path(Segment.empty(), aNonDotSegment()).firstPartIsSuppliedButIsEmpty(), equalTo(true));
    }

    @Test
    void correctlyIdentifiesFirstPartNotSupplied() {
        assertThat(AbsolutePath.path().firstPartIsSuppliedButIsEmpty(), equalTo(false));
    }

    @Test
    void correctlyIdentifiesFirstPartNonEmpty() {
        assertThat(AbsolutePath.path(aNonDotSegment()).firstPartIsSuppliedButIsEmpty(), equalTo(false));
    }

    @Test
    void removesDotPath() {
        final AbsolutePath<String> actual = AbsolutePath.path(segment("a"), segment("b"), segment("c"), Segment.dot(), Segment.dotDot(), Segment.dotDot(), segment("g"));
        assertThat(actual, equalTo(AbsolutePath.path(segment("a"), segment("g"))));
    }

    @Test
    void resolvesEmptyPath() {
        AbsolutePath<String> path = anAbsolutePath();
        assertThat(path.resolveRelativeTo(new EmptyPath<>()), equalTo((Path) path));
    }

    @Test
    void resolvesAbsolutePath() {
        AbsolutePath<String> path = anAbsolutePath();
        Path<String> basePath = anAbsolutePath();
        assertThat(path.resolveRelativeTo(basePath), equalTo((Path) path));
    }

    @Test
    void resolvesRootlessPath() {
        AbsolutePath<String> path = anAbsolutePath();
        Path<String> basePath = aRootlessPath();
        assertThat(path.resolveRelativeTo(basePath), equalTo((Path) path));
    }

    @Test
    void absolutePathIsAbsolute() {
        assertThat(anAbsolutePath().isAbsolute(), equalTo(true));
    }

    @Test
    void absolutePathIteratorContainsAllNonDotSegments() {
        Segment<String> segmentOne = aNonDotSegment();
        Segment<String> segmentTwo = aNonDotSegment();
        assertThat(path(segmentOne, segmentTwo), contains(segmentOne, segmentTwo));
    }

    @Test
    void absolutePathSegmentsContainsAllNonDotSegments() {
        Segment<String> segmentOne = aNonDotSegment();
        Segment<String> segmentTwo = aNonDotSegment();
        final AbsolutePath<String> path = path(segmentOne, segmentTwo);
        assertThat(path.segments(), contains(segmentOne, segmentTwo));
    }

    @Test
    void absolutePathSegmentsDoesNotExposeMutability() {
        final Segment<String> segmentOne = aNonDotSegment();
        final Segment<String> segmentTwo = aNonDotSegment();
        Path<String> absolutePath = path(segmentOne, segmentTwo);
        absolutePath.segments().add(aSegment());
        assertThat(absolutePath.segments(), contains(segmentOne, segmentTwo));

    }

}
