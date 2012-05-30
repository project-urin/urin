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
import static net.sourceforge.urin.Segment.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class RootlessPathTest {
    @Test
    public void aPathIsEqualToAnotherPathWithTheSameMembers() throws Exception {
        Segment firstSegment = SegmentBuilder.aSegment();
        Segment secondSegment = SegmentBuilder.aSegment();
        assertThat(rootlessPath(firstSegment, secondSegment), equalTo(rootlessPath(firstSegment, secondSegment)));
        assertThat(rootlessPath(firstSegment, secondSegment).hashCode(), equalTo(rootlessPath(firstSegment, secondSegment).hashCode()));
    }

    @Test
    public void aPathUsingVarargsFactoryIsEqualToWithTheSameMembersMadeUsingIterableFactory() throws Exception {
        Segment firstSegment = SegmentBuilder.aSegment();
        Segment secondSegment = SegmentBuilder.aSegment();
        assertThat(rootlessPath(firstSegment, secondSegment), equalTo(RootlessPath.rootlessPath(asList(firstSegment, secondSegment))));
        assertThat(rootlessPath(firstSegment, secondSegment).hashCode(), equalTo(RootlessPath.rootlessPath(asList(firstSegment, secondSegment)).hashCode()));
    }

    @Test
    public void aPathUsingSegmentVarargsFactoryIsEqualToWithTheSameMembersMadeUsingStringVarargsFactory() throws Exception {
        String firstSegmentValue = aString();
        Segment firstSegment = segment(firstSegmentValue);
        String secondSegmentValue = aString();
        Segment secondSegment = segment(secondSegmentValue);
        assertThat(rootlessPath(firstSegment, secondSegment), equalTo(RootlessPath.rootlessPath(firstSegmentValue, secondSegmentValue)));
        assertThat(rootlessPath(firstSegment, secondSegment).hashCode(), equalTo(RootlessPath.rootlessPath(firstSegmentValue, secondSegmentValue).hashCode()));
    }

    @Test
    public void aPathUsingSegmentVarargsIsImmutable() throws Exception {
        Segment firstSegment = SegmentBuilder.aNonDotSegment();
        Segment secondSegment = SegmentBuilder.aNonDotSegment();
        Segment[] segments = {firstSegment, secondSegment};
        Path relativePath = Path.rootlessPath(segments);
        segments[0] = SegmentBuilder.aSegment();
        assertThat(relativePath.asString(NEVER_PREFIX_WITH_DOT_SEGMENT), equalTo(firstSegment.asString() + "/" + secondSegment.asString()));
    }

    @Test
    public void aPathIsNotEqualToAnotherPathWithDifferentMembers() throws Exception {
        assertThat(rootlessPath(SegmentBuilder.aSegment(), SegmentBuilder.aSegment()), not(equalTo(rootlessPath(SegmentBuilder.aSegment(), SegmentBuilder.aSegment()))));
    }

    @Test
    public void zeroRootlessPathEqualToEmptyPath() throws Exception {
        assertThat(rootlessPath(), equalTo((Path) new EmptyPath()));
    }

    @Test
    public void aPathToStringIsCorrect() throws Exception {
        Segment firstSegment = SegmentBuilder.aNonDotSegment();
        Segment secondSegment = SegmentBuilder.aNonDotSegment();
        assertThat(rootlessPath(firstSegment, secondSegment).toString(), equalTo("[" + firstSegment + ", " + secondSegment + "]"));
    }

    @Test
    public void correctlyIdentifiesFirstPartAsBeingSuppliedButEmpty() throws Exception {
        assertThat(rootlessPath(EMPTY).firstPartIsSuppliedButIsEmpty(), equalTo(true));
    }

    @Test
    public void correctlyIdentifiesFirstPartUnsupplied() throws Exception {
        assertThat(rootlessPath().firstPartIsSuppliedButIsEmpty(), equalTo(false));
    }

    @Test
    public void correctlyIdentifiesFirstPartNonEmpty() throws Exception {
        assertThat(rootlessPath(SegmentBuilder.aSegment()).firstPartIsSuppliedButIsEmpty(), equalTo(false));
    }

    @Test
    public void addsADotSegmentOntoPathWhenFirstSegmentContainsColonAndColonNotAllowed() throws Exception {
        Segment segment = segment(aStringIncluding(':'));
        assertThat(rootlessPath(segment).asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_CONTAINS_COLON), equalTo("./" + segment.asString()));
    }

    @Test
    public void doesNotAddADotSegmentOntoPathWhenFirstSegmentContainsColonAndColonAllowed() throws Exception {
        Segment segment = segment(aStringIncluding(':'));
        assertThat(rootlessPath(segment).asString(NEVER_PREFIX_WITH_DOT_SEGMENT), equalTo(segment.asString()));
    }

    @Test
    public void removesDotSegments() throws Exception {
        assertThat(rootlessPath(segment("a"), segment("b"), segment("c"), DOT, DOT_DOT, DOT_DOT, segment("g")), equalTo(rootlessPath(segment("a"), segment("g"))));
    }

    @Test
    public void resolvesEmptyPath() throws Exception {
        Path path = aRootlessPath();
        assertThat(path.resolveRelativeTo(new EmptyPath()), equalTo(path));
    }

    @Test
    public void resolvesAbsolutePath() throws Exception {
        Segment rootlessSegmentOne = SegmentBuilder.aSegment();
        Segment rootlessSegmentTwo = SegmentBuilder.aSegment();
        Path path = rootlessPath(rootlessSegmentOne, rootlessSegmentTwo);
        Segment baseSegmentOne = SegmentBuilder.aSegment();
        Segment baseSegmentTwo = SegmentBuilder.aSegment();
        Path basePath = Path.path(baseSegmentOne, baseSegmentTwo);
        assertThat(path.resolveRelativeTo(basePath), equalTo((Path) Path.path(baseSegmentOne, rootlessSegmentOne, rootlessSegmentTwo)));
    }

    @Test
    public void resolvesRootlessPath() throws Exception {
        Segment rootlessSegmentOne = SegmentBuilder.aSegment();
        Segment rootlessSegmentTwo = SegmentBuilder.aSegment();
        Path path = rootlessPath(rootlessSegmentOne, rootlessSegmentTwo);
        Segment baseSegmentOne = SegmentBuilder.aSegment();
        Segment baseSegmentTwo = SegmentBuilder.aSegment();
        Path basePath = Path.path(baseSegmentOne, baseSegmentTwo);
        assertThat(path.resolveRelativeTo(basePath), equalTo((Path) Path.path(baseSegmentOne, rootlessSegmentOne, rootlessSegmentTwo)));
    }
}
