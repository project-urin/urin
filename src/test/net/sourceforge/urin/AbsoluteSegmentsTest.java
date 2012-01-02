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
import static net.sourceforge.urin.AbsoluteSegments.segments;
import static net.sourceforge.urin.MoreRandomStringUtils.aString;
import static net.sourceforge.urin.Segment.*;
import static net.sourceforge.urin.SegmentBuilder.aSegment;
import static net.sourceforge.urin.Segments.PrefixWithDotSegmentCriteria.NEVER_PREFIX_WITH_DOT_SEGMENT;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class AbsoluteSegmentsTest {
    @Test
    public void anAbsoluteSegmentsIsEqualToAnotherAbsoluteSegmentsWithTheSameMembers() throws Exception {
        Segment firstSegment = aSegment();
        Segment secondSegment = aSegment();
        assertThat(AbsoluteSegments.segments(firstSegment, secondSegment), equalTo(AbsoluteSegments.segments(firstSegment, secondSegment)));
        assertThat(AbsoluteSegments.segments(firstSegment, secondSegment).hashCode(), equalTo(AbsoluteSegments.segments(firstSegment, secondSegment).hashCode()));
    }

    @Test
    public void aSegmentsUsingVarargsFactoryIsEqualToWithTheSameMembersMadeUsingIterableFactory() throws Exception {
        Segment firstSegment = aSegment();
        Segment secondSegment = aSegment();
        assertThat(AbsoluteSegments.segments(firstSegment, secondSegment), equalTo(segments(asList(firstSegment, secondSegment))));
        assertThat(AbsoluteSegments.segments(firstSegment, secondSegment).hashCode(), equalTo(segments(asList(firstSegment, secondSegment)).hashCode()));
    }

    @Test
    public void aSegmentsUsingSegmentVarargsFactoryIsEqualToWithTheSameMembersMadeUsingStringVarargsFactory() throws Exception {
        String firstSegmentValue = aString();
        Segment firstSegment = segment(firstSegmentValue);
        String secondSegmentValue = aString();
        Segment secondSegment = segment(secondSegmentValue);
        assertThat(AbsoluteSegments.segments(firstSegment, secondSegment), equalTo(AbsoluteSegments.segments(firstSegmentValue, secondSegmentValue)));
        assertThat(AbsoluteSegments.segments(firstSegment, secondSegment).hashCode(), equalTo(AbsoluteSegments.segments(firstSegmentValue, secondSegmentValue).hashCode()));
    }

    @Test
    public void aSegmentsUsingSegmentVarargsIsImmutable() throws Exception {
        Segment firstSegment = aSegment();
        Segment secondSegment = aSegment();
        Segment[] segments = {firstSegment, secondSegment};
        AbsoluteSegments absoluteSegments = Segments.segments(segments);
        segments[0] = aSegment();
        assertThat(absoluteSegments.asString(NEVER_PREFIX_WITH_DOT_SEGMENT), equalTo("/" + firstSegment.asString() + "/" + secondSegment.asString()));
    }

    @Test
    public void aSegmentsIsNotEqualToAnotherSegmentsWithDifferentMembers() throws Exception {
        assertThat(AbsoluteSegments.segments(aSegment(), aSegment()), not(equalTo(AbsoluteSegments.segments(aSegment(), aSegment()))));
    }

    @Test
    public void aSegmentsToStringIsCorrect() throws Exception {
        Segment firstSegment = aSegment();
        Segment secondSegment = aSegment();
        assertThat(AbsoluteSegments.segments(firstSegment, secondSegment).toString(), equalTo("[" + firstSegment + ", " + secondSegment + "]"));
    }

    @Test
    public void correctlyIdentifiesFirstPartAsBeingSuppliedButEmpty() throws Exception {
        assertThat(AbsoluteSegments.segments(EMPTY).firstPartIsSuppliedButIsEmpty(), equalTo(true));
    }

    @Test
    public void correctlyIdentifiesFirstPartUnsupplied() throws Exception {
        assertThat(AbsoluteSegments.segments().firstPartIsSuppliedButIsEmpty(), equalTo(false));
    }

    @Test
    public void correctlyIdentifiesFirstPartNonEmpty() throws Exception {
        assertThat(AbsoluteSegments.segments(aSegment()).firstPartIsSuppliedButIsEmpty(), equalTo(false));
    }

    @Test
    public void removesDotSegments() throws Exception {
        assertThat(AbsoluteSegments.segments(segment("a"), segment("b"), segment("c"), DOT, DOT_DOT, DOT_DOT, segment("g")), equalTo(AbsoluteSegments.segments(segment("a"), segment("g"))));
    }
}
