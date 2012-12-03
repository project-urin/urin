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
import static net.sourceforge.urin.Path.PrefixWithDotSegmentCriteria.NEVER_PREFIX_WITH_DOT_SEGMENT;
import static net.sourceforge.urin.Path.path;
import static net.sourceforge.urin.PathBuilder.aRootlessPath;
import static net.sourceforge.urin.PathBuilder.anAbsolutePath;
import static net.sourceforge.urin.Segment.EMPTY;
import static net.sourceforge.urin.Segment.segment;
import static net.sourceforge.urin.SegmentBuilder.aNonDotSegment;
import static net.sourceforge.urin.SegmentBuilder.aSegment;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

public class AbsolutePathTest {
    @Test
    public void anAbsolutePathIsEqualToAnotherAbsolutePathWithTheSameMembers() throws Exception {
        Segment<String> firstSegment = aSegment();
        Segment<String> secondSegment = aSegment();
        assertThat(AbsolutePath.path(firstSegment, secondSegment), equalTo(AbsolutePath.path(firstSegment, secondSegment)));
        assertThat(AbsolutePath.path(firstSegment, secondSegment).hashCode(), equalTo(AbsolutePath.path(firstSegment, secondSegment).hashCode()));
    }

    @Test
    public void aPathUsingVarargsFactoryIsEqualToWithTheSameMembersMadeUsingIterableFactory() throws Exception {
        Segment<String> firstSegment = aSegment();
        Segment<String> secondSegment = aSegment();
        assertThat(AbsolutePath.path(firstSegment, secondSegment), equalTo(AbsolutePath.path(asList(firstSegment, secondSegment))));
        assertThat(AbsolutePath.path(firstSegment, secondSegment).hashCode(), equalTo(AbsolutePath.path(asList(firstSegment, secondSegment)).hashCode()));
    }

    @Test
    public void aPathUsingSegmentVarargsFactoryIsEqualToWithTheSameMembersMadeUsingStringVarargsFactory() throws Exception {
        String firstSegmentValue = aString();
        Segment<String> firstSegment = segment(firstSegmentValue);
        String secondSegmentValue = aString();
        Segment<String> secondSegment = segment(secondSegmentValue);
        assertThat(AbsolutePath.path(firstSegment, secondSegment), equalTo(AbsolutePath.path(firstSegmentValue, secondSegmentValue)));
        assertThat(AbsolutePath.path(firstSegment, secondSegment).hashCode(), equalTo(AbsolutePath.path(firstSegmentValue, secondSegmentValue).hashCode()));
    }

    @Test
    public void aPathUsingSegmentVarargsIsImmutable() throws Exception {
        Segment<String> firstSegment = aNonDotSegment();
        Segment<String> secondSegment = aNonDotSegment();
        Segment[] segments = {firstSegment, secondSegment};
        @SuppressWarnings("unchecked") AbsolutePath<String> absolutePath = Path.path(segments);
        segments[0] = aSegment();
        assertThat(absolutePath.asString(NEVER_PREFIX_WITH_DOT_SEGMENT), equalTo("/" + firstSegment.asString() + "/" + secondSegment.asString()));
    }

    @Test
    public void aPathIsNotEqualToAnotherPathWithDifferentMembers() throws Exception {
        assertThat(AbsolutePath.path(aSegment(), aNonDotSegment()), not(equalTo(AbsolutePath.path(aSegment(), aNonDotSegment()))));
    }

    @Test
    public void aPathToStringIsCorrect() throws Exception {
        Segment<String> firstSegment = aNonDotSegment();
        Segment<String> secondSegment = aNonDotSegment();
        assertThat(AbsolutePath.path(firstSegment, secondSegment).toString(), equalTo("[" + firstSegment + ", " + secondSegment + "]"));
    }

    @Test
    public void correctlyIdentifiesFirstPartAsBeingSuppliedButEmpty() throws Exception {
        assertThat(AbsolutePath.path(EMPTY, aNonDotSegment()).firstPartIsSuppliedButIsEmpty(), equalTo(true));
    }

    @Test
    public void correctlyIdentifiesFirstPartNotSupplied() throws Exception {
        assertThat(AbsolutePath.path().firstPartIsSuppliedButIsEmpty(), equalTo(false));
    }

    @Test
    public void correctlyIdentifiesFirstPartNonEmpty() throws Exception {
        assertThat(AbsolutePath.path(aNonDotSegment()).firstPartIsSuppliedButIsEmpty(), equalTo(false));
    }

    @Test
    public void removesDotPath() throws Exception {
        final AbsolutePath<String> actual = AbsolutePath.path(segment("a"), segment("b"), segment("c"), Segment.<String>dot(), Segment.<String>dotDot(), Segment.<String>dotDot(), segment("g"));
        assertThat(actual, equalTo(AbsolutePath.path(segment("a"), segment("g"))));
    }

    @Test
    public void resolvesEmptyPath() throws Exception {
        AbsolutePath<String> path = anAbsolutePath();
        assertThat(path.resolveRelativeTo(new EmptyPath<String>()), equalTo((Path) path));
    }

    @Test
    public void resolvesAbsolutePath() throws Exception {
        AbsolutePath<String> path = anAbsolutePath();
        Path<String> basePath = anAbsolutePath();
        assertThat(path.resolveRelativeTo(basePath), equalTo((Path) path));
    }

    @Test
    public void resolvesRootlessPath() throws Exception {
        AbsolutePath<String> path = anAbsolutePath();
        Path<String> basePath = aRootlessPath();
        assertThat(path.resolveRelativeTo(basePath), equalTo((Path) path));
    }

    @Test
    public void absolutePathIsAbsolute() throws Exception {
        assertThat(anAbsolutePath().isAbsolute(), equalTo(true));
    }

    @Test
    public void absolutePathIteratorContainsAllNonDotSegments() throws Exception {
        Segment<String> segmentOne = aNonDotSegment();
        Segment<String> segmentTwo = aNonDotSegment();
        assertThat(path(segmentOne, segmentTwo), contains(segmentOne, segmentTwo));
    }

    @Test
    public void absolutePathSegmentsContainsAllNonDotSegments() throws Exception {
        Segment<String> segmentOne = aNonDotSegment();
        Segment<String> segmentTwo = aNonDotSegment();
        final AbsolutePath<String> path = path(segmentOne, segmentTwo);
        assertThat(path.segments(), contains(segmentOne, segmentTwo));
    }

    @Test
    public void absolutePathSegmentsDoesNotExposeMutability() throws Exception {
        Segment<String> segmentOne = aNonDotSegment();
        Segment<String> segmentTwo = aNonDotSegment();
        Path<String> absolutePath = path(segmentOne, segmentTwo);
        absolutePath.segments().add(aSegment());
        assertThat(absolutePath.segments(), contains(segmentOne, segmentTwo));
    }

}
