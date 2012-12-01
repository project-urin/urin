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

import org.junit.Test;

import static java.util.Arrays.asList;
import static net.sourceforge.urin.MoreRandomStringUtils.aString;
import static net.sourceforge.urin.MoreRandomStringUtils.aStringIncluding;
import static net.sourceforge.urin.Path.PrefixWithDotSegmentCriteria.NEVER_PREFIX_WITH_DOT_SEGMENT;
import static net.sourceforge.urin.Path.PrefixWithDotSegmentCriteria.PREFIX_WITH_DOT_SEGMENT_IF_FIRST_CONTAINS_COLON;
import static net.sourceforge.urin.PathBuilder.aRootlessPath;
import static net.sourceforge.urin.RootlessPath.rootlessPath;
import static net.sourceforge.urin.Segment.EMPTY;
import static net.sourceforge.urin.Segment.segment;
import static net.sourceforge.urin.SegmentBuilder.aNonDotSegment;
import static net.sourceforge.urin.SegmentBuilder.aSegment;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

public class RootlessPathTest {
    @Test
    public void aPathIsEqualToAnotherPathWithTheSameMembers() throws Exception {
        Segment firstSegment = aSegment();
        Segment secondSegment = aSegment();
        assertThat(rootlessPath(firstSegment, secondSegment), equalTo(rootlessPath(firstSegment, secondSegment)));
        assertThat(rootlessPath(firstSegment, secondSegment).hashCode(), equalTo(rootlessPath(firstSegment, secondSegment).hashCode()));
    }

    @Test
    public void aPathUsingVarargsFactoryIsEqualToWithTheSameMembersMadeUsingIterableFactory() throws Exception {
        Segment<String> firstSegment = aSegment();
        Segment<String> secondSegment = aSegment();
        assertThat(rootlessPath(firstSegment, secondSegment), equalTo(RootlessPath.rootlessPath(asList(firstSegment, secondSegment))));
        assertThat(rootlessPath(firstSegment, secondSegment).hashCode(), equalTo(RootlessPath.rootlessPath(asList(firstSegment, secondSegment)).hashCode()));
    }

    @Test
    public void aPathUsingSegmentVarargsFactoryIsEqualToWithTheSameMembersMadeUsingStringVarargsFactory() throws Exception {
        String firstSegmentValue = aString();
        Segment<String> firstSegment = segment(firstSegmentValue);
        String secondSegmentValue = aString();
        Segment<String> secondSegment = segment(secondSegmentValue);
        assertThat(rootlessPath(firstSegment, secondSegment), equalTo(RootlessPath.rootlessPath(firstSegmentValue, secondSegmentValue)));
        assertThat(rootlessPath(firstSegment, secondSegment).hashCode(), equalTo(RootlessPath.rootlessPath(firstSegmentValue, secondSegmentValue).hashCode()));
    }

    @Test
    public void aPathUsingSegmentVarargsIsImmutable() throws Exception {
        Segment<String> firstSegment = SegmentBuilder.aNonDotSegment();
        Segment secondSegment = SegmentBuilder.aNonDotSegment();
        Segment[] segments = {secondSegment};
        Path relativePath = Path.rootlessPath(firstSegment, segments);
        segments[0] = aSegment();
        assertThat(relativePath.asString(NEVER_PREFIX_WITH_DOT_SEGMENT), equalTo(firstSegment.asString() + "/" + secondSegment.asString()));
    }

    @Test
    public void aPathIsNotEqualToAnotherPathWithDifferentMembers() throws Exception {
        assertThat(rootlessPath(aNonDotSegment(), aNonDotSegment()), not(equalTo(rootlessPath(aNonDotSegment(), aNonDotSegment()))));
    }

    @Test
    public void zeroRootlessPathEqualToEmptyPath() throws Exception {
        assertThat(rootlessPath(), equalTo((Path) new EmptyPath()));
    }

    @Test
    public void aPathToStringIsCorrect() throws Exception {
        Segment<String> firstSegment = SegmentBuilder.aNonDotSegment();
        Segment<String> secondSegment = SegmentBuilder.aNonDotSegment();
        assertThat(rootlessPath(firstSegment, secondSegment).toString(), equalTo("[" + firstSegment + ", " + secondSegment + "]"));
    }

    @Test
    public void correctlyIdentifiesFirstPartAsBeingSuppliedButEmpty() throws Exception {
        assertThat(rootlessPath(EMPTY).firstPartIsSuppliedButIsEmpty(), equalTo(true));
    }

    @Test
    public void correctlyIdentifiesFirstPartNotSupplied() throws Exception {
        assertThat(rootlessPath().firstPartIsSuppliedButIsEmpty(), equalTo(false));
    }

    @Test
    public void correctlyIdentifiesFirstPartNonEmpty() throws Exception {
        assertThat(rootlessPath(aSegment()).firstPartIsSuppliedButIsEmpty(), equalTo(false));
    }

    @Test
    public void addsADotSegmentOntoPathWhenFirstSegmentContainsColonAndColonNotAllowed() throws Exception {
        Segment segment = segment(aStringIncluding(':'));
        assertThat(rootlessPath(segment).asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_CONTAINS_COLON), equalTo("./" + segment.asString()));
    }

    @Test
    public void doesNotAddADotSegmentOntoPathWhenFirstSegmentContainsColonAndColonAllowed() throws Exception {
        Segment<String> segment = segment(aStringIncluding(':'));
        final Path<String> path = Path.rootlessPath(segment);
        assertThat(path.asString(NEVER_PREFIX_WITH_DOT_SEGMENT), equalTo(segment.asString()));
    }

    @Test
    public void removesDotSegments() throws Exception {
        final Path<String> actual = rootlessPath(segment("a"), segment("b"), segment("c"), Segment.<String>dot(), Segment.<String>dotDot(), Segment.<String>dotDot(), segment("g"));
        assertThat(actual, equalTo(rootlessPath(segment("a"), segment("g"))));
    }

    @Test
    public void resolvesEmptyPath() throws Exception {
        Path path = aRootlessPath();
        assertThat(path.resolveRelativeTo(new EmptyPath()), equalTo(path));
    }

    @Test
    public void resolvesAbsolutePath() throws Exception {
        Segment<String> segmentOne = aNonDotSegment();
        Segment<String> segmentTwo = aNonDotSegment();
        Path path = rootlessPath(segmentOne, segmentTwo);
        Segment baseSegmentOne = aNonDotSegment();
        Segment baseSegmentTwo = aNonDotSegment();
        Path basePath = Path.path(baseSegmentOne, baseSegmentTwo);
        assertThat(path.resolveRelativeTo(basePath), equalTo((Path) Path.path(baseSegmentOne, segmentOne, segmentTwo)));
    }

    @Test
    public void resolvesRootlessPath() throws Exception {
        Segment<String> rootlessSegmentOne = aNonDotSegment();
        Segment<String> rootlessSegmentTwo = aNonDotSegment();
        Path path = rootlessPath(rootlessSegmentOne, rootlessSegmentTwo);
        Segment baseSegmentOne = aNonDotSegment();
        Segment baseSegmentTwo = aNonDotSegment();
        Path basePath = Path.path(baseSegmentOne, baseSegmentTwo);
        assertThat(path.resolveRelativeTo(basePath), equalTo((Path) Path.path(baseSegmentOne, rootlessSegmentOne, rootlessSegmentTwo)));
    }

    @Test
    public void rootlessPathIsNotAbsolute() throws Exception {
        assertThat(aRootlessPath().isAbsolute(), equalTo(false));
    }

    @Test
    public void rootlessPathIteratorContainsAllNonDotSegments() throws Exception {
        Segment<String> segmentOne = aNonDotSegment();
        Segment<String> segmentTwo = aNonDotSegment();
        assertThat(rootlessPath(segmentOne, segmentTwo), contains(segmentOne, segmentTwo));
    }

    @Test
    public void rootlessPathSegmentsContainsAllNonDotSegments() throws Exception {
        Segment<String> segmentOne = aNonDotSegment();
        Segment<String> segmentTwo = aNonDotSegment();
        assertThat(rootlessPath(segmentOne, segmentTwo).segments(), contains(segmentOne, segmentTwo));
    }

    @Test
    public void rootlessPathSegmentsDoesNotExposeMutability() throws Exception {
        Segment<String> segmentOne = aNonDotSegment();
        Segment<String> segmentTwo = aNonDotSegment();
        Path<String> rootlessPath = rootlessPath(segmentOne, segmentTwo);
        rootlessPath.segments().add(aSegment());
        assertThat(rootlessPath.segments(), contains(segmentOne, segmentTwo));
    }
}
