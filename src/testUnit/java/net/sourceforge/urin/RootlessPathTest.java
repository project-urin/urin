/*
 * Copyright 2020 Mark Slater
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
import static net.sourceforge.urin.MoreRandomStringUtils.aStringIncluding;
import static net.sourceforge.urin.Path.PrefixWithDotSegmentCriteria.NEVER_PREFIX_WITH_DOT_SEGMENT;
import static net.sourceforge.urin.Path.PrefixWithDotSegmentCriteria.PREFIX_WITH_DOT_SEGMENT_IF_FIRST_CONTAINS_COLON;
import static net.sourceforge.urin.PathBuilder.aRootlessPath;
import static net.sourceforge.urin.Segment.empty;
import static net.sourceforge.urin.Segment.segment;
import static net.sourceforge.urin.SegmentBuilder.aNonDotSegment;
import static net.sourceforge.urin.SegmentBuilder.aSegment;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

class RootlessPathTest {
    @Test
    void aPathIsEqualToAnotherPathWithTheSameMembers() {
        Segment<String> firstSegment = aSegment();
        Segment<String> secondSegment = aSegment();
        assertThat(RootlessPath.rootlessPath(firstSegment, secondSegment), equalTo(RootlessPath.rootlessPath(firstSegment, secondSegment)));
        assertThat(RootlessPath.rootlessPath(firstSegment, secondSegment).hashCode(), equalTo(RootlessPath.rootlessPath(firstSegment, secondSegment).hashCode()));
    }

    @Test
    void aPathUsingVarargsFactoryIsEqualToWithTheSameMembersMadeUsingIterableFactory() {
        Segment<String> firstSegment = aSegment();
        Segment<String> secondSegment = aSegment();
        assertThat(RootlessPath.rootlessPath(firstSegment, secondSegment), equalTo(RootlessPath.rootlessPath(asList(firstSegment, secondSegment))));
        assertThat(RootlessPath.rootlessPath(firstSegment, secondSegment).hashCode(), equalTo(RootlessPath.rootlessPath(asList(firstSegment, secondSegment)).hashCode()));
    }

    @Test
    void aPathUsingSegmentVarargsFactoryIsEqualToWithTheSameMembersMadeUsingStringVarargsFactory() {
        String firstSegmentValue = aString();
        Segment<String> firstSegment = segment(firstSegmentValue);
        String secondSegmentValue = aString();
        Segment<String> secondSegment = segment(secondSegmentValue);
        assertThat(RootlessPath.rootlessPath(firstSegment, secondSegment), equalTo(RootlessPath.rootlessPath(firstSegmentValue, secondSegmentValue)));
        assertThat(RootlessPath.rootlessPath(firstSegment, secondSegment).hashCode(), equalTo(RootlessPath.rootlessPath(firstSegmentValue, secondSegmentValue).hashCode()));
    }

    @Test
    void aPathUsingSegmentVarargsIsImmutable() {
        Segment<String> firstSegment = aNonDotSegment();
        Segment<String> secondSegment = aNonDotSegment();
        @SuppressWarnings("rawtypes") Segment[] segments = {firstSegment, secondSegment};
        @SuppressWarnings("unchecked") Path<String> relativePath = Path.rootlessPath(segments);
        segments[0] = aSegment();
        assertThat(relativePath.asString(NEVER_PREFIX_WITH_DOT_SEGMENT), equalTo(firstSegment.asString() + "/" + secondSegment.asString()));
    }

    @Test
    void aPathIsNotEqualToAnotherPathWithDifferentMembers() {
        assertThat(RootlessPath.rootlessPath(aNonDotSegment(), aNonDotSegment()), not(equalTo(RootlessPath.rootlessPath(aNonDotSegment(), aNonDotSegment()))));
    }

    @Test
    void zeroRootlessPathEqualToEmptyPath() {
        assertThat(RootlessPath.rootlessPath(), equalTo(new EmptyPath<>()));
    }

    @Test
    void aPathToStringIsCorrect() {
        Segment<String> firstSegment = aNonDotSegment();
        Segment<String> secondSegment = aNonDotSegment();
        assertThat(RootlessPath.rootlessPath(firstSegment, secondSegment).toString(), equalTo("[" + firstSegment + ", " + secondSegment + "]"));
    }

    @Test
    void correctlyIdentifiesFirstPartAsBeingSuppliedButEmpty() {
        assertThat(RootlessPath.rootlessPath(empty()).firstPartIsSuppliedButIsEmpty(), equalTo(true));
    }

    @Test
    void correctlyIdentifiesFirstPartNotSupplied() {
        assertThat(RootlessPath.rootlessPath().firstPartIsSuppliedButIsEmpty(), equalTo(false));
    }

    @Test
    void correctlyIdentifiesFirstPartNonEmpty() {
        assertThat(RootlessPath.rootlessPath(aSegment()).firstPartIsSuppliedButIsEmpty(), equalTo(false));
    }

    @Test
    void addsADotSegmentOntoPathWhenFirstSegmentContainsColonAndColonNotAllowed() {
        Segment<String> segment = segment(aStringIncluding(':'));
        assertThat(RootlessPath.rootlessPath(segment).asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_CONTAINS_COLON), equalTo("./" + segment.asString()));
    }

    @Test
    void doesNotAddADotSegmentOntoPathWhenFirstSegmentContainsColonAndColonAllowed() {
        Segment<String> segment = segment(aStringIncluding(':'));
        final Path<String> path = Path.rootlessPath(segment);
        assertThat(path.asString(NEVER_PREFIX_WITH_DOT_SEGMENT), equalTo(segment.asString()));
    }

    @Test
    void removesDotSegments() {
        final Path<String> actual = RootlessPath.rootlessPath(segment("a"), segment("b"), segment("c"), Segment.dot(), Segment.dotDot(), Segment.dotDot(), segment("g"));
        assertThat(actual, equalTo(RootlessPath.rootlessPath(segment("a"), segment("g"))));
    }

    @Test
    void resolvesEmptyPath() {
        Path<String> path = aRootlessPath();
        assertThat(path.resolveRelativeTo(new EmptyPath<>()), equalTo(path));
    }

    @Test
    void resolvesAbsolutePath() {
        Segment<String> segmentOne = aNonDotSegment();
        Segment<String> segmentTwo = aNonDotSegment();
        Path<String> path = RootlessPath.rootlessPath(segmentOne, segmentTwo);
        Segment<String> baseSegmentOne = aNonDotSegment();
        Segment<String> baseSegmentTwo = aNonDotSegment();
        Path<String> basePath = Path.path(baseSegmentOne, baseSegmentTwo);
        assertThat(path.resolveRelativeTo(basePath), equalTo(Path.path(baseSegmentOne, segmentOne, segmentTwo)));
    }

    @Test
    void resolvesRootlessPath() {
        Segment<String> rootlessSegmentOne = aNonDotSegment();
        Segment<String> rootlessSegmentTwo = aNonDotSegment();
        Path<String> path = RootlessPath.rootlessPath(rootlessSegmentOne, rootlessSegmentTwo);
        Segment<String> baseSegmentOne = aNonDotSegment();
        Segment<String> baseSegmentTwo = aNonDotSegment();
        Path<String> basePath = Path.path(baseSegmentOne, baseSegmentTwo);
        assertThat(path.resolveRelativeTo(basePath), equalTo(Path.path(baseSegmentOne, rootlessSegmentOne, rootlessSegmentTwo)));
    }

    @Test
    void rootlessPathIsNotAbsolute() {
        assertThat(aRootlessPath().isAbsolute(), equalTo(false));
    }

    @Test
    void rootlessPathIteratorContainsAllNonDotSegments() {
        Segment<String> segmentOne = aNonDotSegment();
        Segment<String> segmentTwo = aNonDotSegment();
        assertThat(RootlessPath.rootlessPath(segmentOne, segmentTwo), contains(segmentOne, segmentTwo));
    }

    @Test
    void rootlessPathSegmentsContainsAllNonDotSegments() {
        Segment<String> segmentOne = aNonDotSegment();
        Segment<String> segmentTwo = aNonDotSegment();
        assertThat(RootlessPath.rootlessPath(segmentOne, segmentTwo).segments(), contains(segmentOne, segmentTwo));
    }

    @Test
    void rootlessPathSegmentsDoesNotExposeMutability() {
        Segment<String> segmentOne = aNonDotSegment();
        Segment<String> segmentTwo = aNonDotSegment();
        Path<String> rootlessPath = RootlessPath.rootlessPath(segmentOne, segmentTwo);
        rootlessPath.segments().add(aSegment());
        assertThat(rootlessPath.segments(), contains(segmentOne, segmentTwo));
    }

}
