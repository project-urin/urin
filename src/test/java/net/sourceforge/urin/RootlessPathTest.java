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
import static net.sourceforge.urin.Segment.dot;
import static net.sourceforge.urin.Segment.dotDot;
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
        final Path<String> actual = RootlessPath.rootlessPath(segment("a"), segment("b"), segment("c"), dot(), dotDot(), dotDot(), segment("g"));
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

    @Test
    void normalisesDotSlashDot() {
        Path<String> rootlessPath = RootlessPath.rootlessPath(dot(), dot());
        assertThat(rootlessPath.segments(), contains(dot()));
    }

    @Test
    void normalisesDotSlashDotDot() {
        Path<String> rootlessPath = RootlessPath.rootlessPath(dot(), dotDot());
        assertThat(rootlessPath.segments(), contains(dotDot()));
    }

    @Test
    void normalisesDotSlashEmpty() {
        Path<String> rootlessPath = RootlessPath.rootlessPath(dot(), empty());
        assertThat(rootlessPath.segments(), contains(empty()));
    }

    @Test
    void normalisesDotSlashValue() {
        final Segment<String> valueSegment = aNonDotSegment();
        Path<String> rootlessPath = RootlessPath.rootlessPath(dot(), valueSegment);
        assertThat(rootlessPath.segments(), contains(valueSegment));
    }

    @Test
    void normalisesDotDotSlashDot() {
        Path<String> rootlessPath = RootlessPath.rootlessPath(dotDot(), dot());
        assertThat(rootlessPath.segments(), contains(dotDot(), empty()));
    }

    @Test
    void normalisesDotDotSlashDotDot() {
        Path<String> rootlessPath = RootlessPath.rootlessPath(dotDot(), dotDot());
        assertThat(rootlessPath.segments(), contains(dotDot(), dotDot()));
    }

    @Test
    void normalisesDotDotSlashEmpty() {
        Path<String> rootlessPath = RootlessPath.rootlessPath(dotDot(), empty());
        assertThat(rootlessPath.segments(), contains(dotDot(), empty()));
    }

    @Test
    void normalisesDotDotSlashValue() {
        final Segment<String> valueSegment = aNonDotSegment();
        Path<String> rootlessPath = RootlessPath.rootlessPath(dotDot(), valueSegment);
        assertThat(rootlessPath.segments(), contains(dotDot(), valueSegment));
    }

    @Test
    void normalisesEmptySlashDot() {
        Path<String> rootlessPath = RootlessPath.rootlessPath(empty(), dot());
        assertThat(rootlessPath.segments(), contains(empty(), empty()));
    }

    @Test
    void normalisesEmptySlashDotSlashDot() {
        Path<String> rootlessPath = RootlessPath.rootlessPath(empty(), dot(), dot());
        assertThat(rootlessPath.segments(), contains(empty(), empty()));
    }

    @Test
    void normalisesEmptySlashDotDot() {
        Path<String> rootlessPath = RootlessPath.rootlessPath(empty(), dotDot());
        assertThat(rootlessPath.segments(), contains(dot()));
    }

    @Test
    void normalisesEmptySlashEmpty() {
        Path<String> rootlessPath = RootlessPath.rootlessPath(empty(), empty());
        assertThat(rootlessPath.segments(), contains(empty(), empty()));
    }

    @Test
    void normalisesEmptySlashValue() {
        final Segment<String> valueSegment = aNonDotSegment();
        Path<String> rootlessPath = RootlessPath.rootlessPath(empty(), valueSegment);
        assertThat(rootlessPath.segments(), contains(empty(), valueSegment));
    }

    @Test
    void normalisesValueSlashDot() {
        final Segment<String> valueSegment = aNonDotSegment();
        Path<String> rootlessPath = RootlessPath.rootlessPath(valueSegment, dot());
        assertThat(rootlessPath.segments(), contains(valueSegment, empty()));
    }

    @Test
    void normalisesValueSlashDotDot() {
        Path<String> rootlessPath = RootlessPath.rootlessPath(aNonDotSegment(), dotDot());
        assertThat(rootlessPath.segments(), contains(dot()));
    }

    @Test
    void normalisesValueSlashEmpty() {
        final Segment<String> valueSegment = aNonDotSegment();
        Path<String> rootlessPath = RootlessPath.rootlessPath(valueSegment, empty());
        assertThat(rootlessPath.segments(), contains(valueSegment, empty()));
    }

    @Test
    void normalisesValueSlashValue() {
        final Segment<String> firstValueSegment = aNonDotSegment();
        final Segment<String> secondValueSegment = aNonDotSegment();
        Path<String> rootlessPath = RootlessPath.rootlessPath(firstValueSegment, secondValueSegment);
        assertThat(rootlessPath.segments(), contains(firstValueSegment, secondValueSegment));
    }
}
