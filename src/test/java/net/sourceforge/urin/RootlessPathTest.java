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
import static net.sourceforge.urin.MoreRandomStringUtils.aStringIncluding;
import static net.sourceforge.urin.Path.PrefixWithDotSegmentCriteria.NEVER_PREFIX_WITH_DOT_SEGMENT;
import static net.sourceforge.urin.Path.PrefixWithDotSegmentCriteria.PREFIX_WITH_DOT_SEGMENT_IF_FIRST_CONTAINS_COLON;
import static net.sourceforge.urin.PathBuilder.aRootlessPath;
import static net.sourceforge.urin.Segment.*;
import static net.sourceforge.urin.SegmentBuilder.aNonDotSegment;
import static net.sourceforge.urin.SegmentBuilder.aSegment;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

class RootlessPathTest {
    @Test
    void aPathIsEqualToAnotherPathWithTheSameMembers() {
        final Segment<String> firstSegment = aSegment();
        final Segment<String> secondSegment = aSegment();
        assertThat(RootlessPath.rootlessPath(firstSegment, secondSegment), equalTo(RootlessPath.rootlessPath(firstSegment, secondSegment)));
        assertThat(RootlessPath.rootlessPath(firstSegment, secondSegment).hashCode(), equalTo(RootlessPath.rootlessPath(firstSegment, secondSegment).hashCode()));
    }

    @Test
    void aPathUsingVarargsFactoryIsEqualToWithTheSameMembersMadeUsingIterableFactory() {
        final Segment<String> firstSegment = aSegment();
        final Segment<String> secondSegment = aSegment();
        assertThat(RootlessPath.rootlessPath(firstSegment, secondSegment), equalTo(RootlessPath.rootlessPath(asList(firstSegment, secondSegment))));
        assertThat(RootlessPath.rootlessPath(firstSegment, secondSegment).hashCode(), equalTo(RootlessPath.rootlessPath(asList(firstSegment, secondSegment)).hashCode()));
    }

    @Test
    void aPathUsingSegmentVarargsFactoryIsEqualToWithTheSameMembersMadeUsingStringVarargsFactory() {
        final String firstSegmentValue = aString();
        final Segment<String> firstSegment = segment(firstSegmentValue);
        final String secondSegmentValue = aString();
        final Segment<String> secondSegment = segment(secondSegmentValue);
        assertThat(RootlessPath.rootlessPath(firstSegment, secondSegment), equalTo(RootlessPath.rootlessPath(firstSegmentValue, secondSegmentValue)));
        assertThat(RootlessPath.rootlessPath(firstSegment, secondSegment).hashCode(), equalTo(RootlessPath.rootlessPath(firstSegmentValue, secondSegmentValue).hashCode()));
    }

    @Test
    void aPathUsingSegmentVarargsIsImmutable() {
        final Segment<String> firstSegment = aNonDotSegment();
        final Segment<String> secondSegment = aNonDotSegment();
        @SuppressWarnings("rawtypes") final Segment[] segments = {firstSegment, secondSegment};
        @SuppressWarnings("unchecked") final Path<String> relativePath = Path.rootlessPath(segments);
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
        final Segment<String> firstSegment = aNonDotSegment();
        final Segment<String> secondSegment = aNonDotSegment();
        assertThat(RootlessPath.rootlessPath(firstSegment, secondSegment).toString(), equalTo("[" + firstSegment + ", " + secondSegment + "]"));
    }

    @Test
    void correctlyIdentifiesFirstPartAsBeingSuppliedButEmpty() {
        assertThat(RootlessPath.rootlessPath(empty(), aNonDotSegment()).firstPartIsSuppliedButIsEmpty(), equalTo(true));
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
        final Segment<String> segment = segment(aStringIncluding(':'));
        assertThat(RootlessPath.rootlessPath(segment).asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_CONTAINS_COLON), equalTo("./" + segment.asString()));
    }

    @Test
    void doesNotAddADotSegmentOntoPathWhenFirstSegmentContainsColonAndColonAllowed() {
        final Segment<String> segment = segment(aStringIncluding(':'));
        final Path<String> path = Path.rootlessPath(segment);
        assertThat(path.asString(NEVER_PREFIX_WITH_DOT_SEGMENT), equalTo(segment.asString()));
    }

    @Test
    void removesDotSegments() {
        final Path<String> actual = RootlessPath.rootlessPath(segment("a"), segment("b"), segment("c"), dot(), dotDot(), dotDot(), segment("g"));
        assertThat(actual, equalTo(RootlessPath.rootlessPath(segment("a"), segment("g"))));
    }

    @Test
    void resolvesEmptyPath() {
        final Path<String> path = aRootlessPath();
        assertThat(path.resolveRelativeTo(new EmptyPath<>()), equalTo(path));
    }

    @Test
    void resolvesAbsolutePath() {
        final Segment<String> segmentOne = aNonDotSegment();
        final Segment<String> segmentTwo = aNonDotSegment();
        final Path<String> path = RootlessPath.rootlessPath(segmentOne, segmentTwo);
        final Segment<String> baseSegmentOne = aNonDotSegment();
        final Segment<String> baseSegmentTwo = aNonDotSegment();
        final Path<String> basePath = Path.path(baseSegmentOne, baseSegmentTwo);
        assertThat(path.resolveRelativeTo(basePath), equalTo(Path.path(baseSegmentOne, segmentOne, segmentTwo)));
    }

    @Test
    void resolvesRootlessPath() {
        final Segment<String> rootlessSegmentOne = aNonDotSegment();
        final Segment<String> rootlessSegmentTwo = aNonDotSegment();
        final Path<String> path = RootlessPath.rootlessPath(rootlessSegmentOne, rootlessSegmentTwo);
        final Segment<String> baseSegmentOne = aNonDotSegment();
        final Segment<String> baseSegmentTwo = aNonDotSegment();
        final Path<String> basePath = Path.path(baseSegmentOne, baseSegmentTwo);
        assertThat(path.resolveRelativeTo(basePath), equalTo(Path.path(baseSegmentOne, rootlessSegmentOne, rootlessSegmentTwo)));
    }

    @Test
    void rootlessPathIsNotAbsolute() {
        assertThat(aRootlessPath().isAbsolute(), equalTo(false));
    }

    @Test
    void rootlessPathIteratorContainsAllNonDotSegments() {
        final Segment<String> segmentOne = aNonDotSegment();
        final Segment<String> segmentTwo = aNonDotSegment();
        assertThat(RootlessPath.rootlessPath(segmentOne, segmentTwo), contains(segmentOne, segmentTwo));
    }

    @Test
    void rootlessPathSegmentsContainsAllNonDotSegments() {
        final Segment<String> segmentOne = aNonDotSegment();
        final Segment<String> segmentTwo = aNonDotSegment();
        assertThat(RootlessPath.rootlessPath(segmentOne, segmentTwo).segments(), contains(segmentOne, segmentTwo));
    }

    @Test
    void rootlessPathSegmentsDoesNotExposeMutability() {
        final Segment<String> segmentOne = aNonDotSegment();
        final Segment<String> segmentTwo = aNonDotSegment();
        final Path<String> rootlessPath = RootlessPath.rootlessPath(segmentOne, segmentTwo);
        rootlessPath.segments().add(aSegment());
        assertThat(rootlessPath.segments(), contains(segmentOne, segmentTwo));
    }

    @Test
    void normalisesDotSlashDot() {
        final Path<String> rootlessPath = RootlessPath.rootlessPath(dot(), dot());
        assertThat(rootlessPath.segments(), contains(dot()));
    }

    @Test
    void normalisesDotSlashDotDot() {
        final Path<String> rootlessPath = RootlessPath.rootlessPath(dot(), dotDot());
        assertThat(rootlessPath.segments(), contains(dotDot()));
    }

    @Test
    void normalisesDotSlashEmpty() {
        final Path<String> rootlessPath = RootlessPath.rootlessPath(dot(), empty());
        assertThat(rootlessPath.segments(), contains(empty()));
    }

    @Test
    void normalisesDotSlashValue() {
        final Segment<String> valueSegment = aNonDotSegment();
        final Path<String> rootlessPath = RootlessPath.rootlessPath(dot(), valueSegment);
        assertThat(rootlessPath.segments(), contains(valueSegment));
    }

    @Test
    void normalisesDotDotSlashDot() {
        final Path<String> rootlessPath = RootlessPath.rootlessPath(dotDot(), dot());
        assertThat(rootlessPath.segments(), contains(dotDot(), empty()));
    }

    @Test
    void normalisesDotDotSlashDotDot() {
        final Path<String> rootlessPath = RootlessPath.rootlessPath(dotDot(), dotDot());
        assertThat(rootlessPath.segments(), contains(dotDot(), dotDot()));
    }

    @Test
    void normalisesDotDotSlashEmpty() {
        final Path<String> rootlessPath = RootlessPath.rootlessPath(dotDot(), empty());
        assertThat(rootlessPath.segments(), contains(dotDot(), empty()));
    }

    @Test
    void normalisesDotDotSlashValue() {
        final Segment<String> valueSegment = aNonDotSegment();
        final Path<String> rootlessPath = RootlessPath.rootlessPath(dotDot(), valueSegment);
        assertThat(rootlessPath.segments(), contains(dotDot(), valueSegment));
    }

    @Test
    void normalisesEmptySlashDot() {
        final Path<String> rootlessPath = RootlessPath.rootlessPath(empty(), dot());
        assertThat(rootlessPath.segments(), contains(empty(), empty()));
    }

    @Test
    void normalisesEmptySlashDotSlashDot() {
        final Path<String> rootlessPath = RootlessPath.rootlessPath(empty(), dot(), dot());
        assertThat(rootlessPath.segments(), contains(empty(), empty()));
    }

    @Test
    void normalisesEmptySlashDotDot() {
        final Path<String> rootlessPath = RootlessPath.rootlessPath(empty(), dotDot());
        assertThat(rootlessPath.segments(), contains(dot()));
    }

    @Test
    void normalisesEmptySlashEmpty() {
        final Path<String> rootlessPath = RootlessPath.rootlessPath(empty(), empty());
        assertThat(rootlessPath.segments(), contains(empty(), empty()));
    }

    @Test
    void normalisesEmptySlashValue() {
        final Segment<String> valueSegment = aNonDotSegment();
        final Path<String> rootlessPath = RootlessPath.rootlessPath(empty(), valueSegment);
        assertThat(rootlessPath.segments(), contains(empty(), valueSegment));
    }

    @Test
    void normalisesValueSlashDot() {
        final Segment<String> valueSegment = aNonDotSegment();
        final Path<String> rootlessPath = RootlessPath.rootlessPath(valueSegment, dot());
        assertThat(rootlessPath.segments(), contains(valueSegment, empty()));
    }

    @Test
    void normalisesValueSlashDotDot() {
        final Path<String> rootlessPath = RootlessPath.rootlessPath(aNonDotSegment(), dotDot());
        assertThat(rootlessPath.segments(), contains(dot()));
    }

    @Test
    void normalisesValueSlashEmpty() {
        final Segment<String> valueSegment = aNonDotSegment();
        final Path<String> rootlessPath = RootlessPath.rootlessPath(valueSegment, empty());
        assertThat(rootlessPath.segments(), contains(valueSegment, empty()));
    }

    @Test
    void normalisesValueSlashValue() {
        final Segment<String> firstValueSegment = aNonDotSegment();
        final Segment<String> secondValueSegment = aNonDotSegment();
        final Path<String> rootlessPath = RootlessPath.rootlessPath(firstValueSegment, secondValueSegment);
        assertThat(rootlessPath.segments(), contains(firstValueSegment, secondValueSegment));
    }
}
