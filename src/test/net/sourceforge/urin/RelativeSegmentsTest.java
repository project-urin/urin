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
import static net.sourceforge.urin.RelativeSegments.rootlessSegments;
import static net.sourceforge.urin.Segment.*;
import static net.sourceforge.urin.SegmentBuilder.aSegment;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class RelativeSegmentsTest {
    @Test
    public void aSegmentsIsEqualToAnotherSegmentsWithTheSameMembers() throws Exception {
        Segment firstSegment = aSegment();
        Segment secondSegment = aSegment();
        assertThat(RelativeSegments.rootlessSegments(firstSegment, secondSegment), equalTo(RelativeSegments.rootlessSegments(firstSegment, secondSegment)));
        assertThat(RelativeSegments.rootlessSegments(firstSegment, secondSegment).hashCode(), equalTo(RelativeSegments.rootlessSegments(firstSegment, secondSegment).hashCode()));
    }

    @Test
    public void aSegmentsUsingVarargsFactoryIsEqualToWithTheSameMembersMadeUsingIterableFactory() throws Exception {
        Segment firstSegment = aSegment();
        Segment secondSegment = aSegment();
        assertThat(RelativeSegments.rootlessSegments(firstSegment, secondSegment), equalTo(RelativeSegments.rootlessSegments(asList(firstSegment, secondSegment))));
        assertThat(RelativeSegments.rootlessSegments(firstSegment, secondSegment).hashCode(), equalTo(RelativeSegments.rootlessSegments(asList(firstSegment, secondSegment)).hashCode()));
    }

    @Test
    public void aSegmentsUsingSegmentVarargsFactoryIsEqualToWithTheSameMembersMadeUsingStringVarargsFactory() throws Exception {
        String firstSegmentValue = aString();
        Segment firstSegment = segment(firstSegmentValue);
        String secondSegmentValue = aString();
        Segment secondSegment = segment(secondSegmentValue);
        assertThat(RelativeSegments.rootlessSegments(firstSegment, secondSegment), equalTo(rootlessSegments(firstSegmentValue, secondSegmentValue)));
        assertThat(RelativeSegments.rootlessSegments(firstSegment, secondSegment).hashCode(), equalTo(rootlessSegments(firstSegmentValue, secondSegmentValue).hashCode()));
    }

    @Test
    public void aSegmentsUsingSegmentVarargsIsImmutable() throws Exception {
        Segment firstSegment = aSegment();
        Segment secondSegment = aSegment();
        Segment[] segments = {firstSegment, secondSegment};
        Segments relativeSegments = Segments.rootlessSegments(segments);
        segments[0] = aSegment();
        assertThat(relativeSegments.asString(true), equalTo(firstSegment.asString() + "/" + secondSegment.asString()));
    }

    @Test
    public void aSegmentsIsNotEqualToAnotherSegmentsWithDifferentMembers() throws Exception {
        assertThat(RelativeSegments.rootlessSegments(aSegment(), aSegment()), not(equalTo(RelativeSegments.rootlessSegments(aSegment(), aSegment()))));
    }

    @Test
    public void aSegmentsToStringIsCorrect() throws Exception {
        Segment firstSegment = aSegment();
        Segment secondSegment = aSegment();
        assertThat(RelativeSegments.rootlessSegments(firstSegment, secondSegment).toString(), equalTo("[" + firstSegment + ", " + secondSegment + "]"));
    }

    @Test
    public void correctlyIdentifiesFirstPartAsBeingSuppliedButEmpty() throws Exception {
        assertThat(RelativeSegments.rootlessSegments(EMPTY).firstPartIsSuppliedButIsEmpty(), equalTo(true));
    }

    @Test
    public void correctlyIdentifiesFirstPartUnsupplied() throws Exception {
        assertThat(RelativeSegments.rootlessSegments().firstPartIsSuppliedButIsEmpty(), equalTo(false));
    }

    @Test
    public void correctlyIdentifiesFirstPartNonEmpty() throws Exception {
        assertThat(RelativeSegments.rootlessSegments(aSegment()).firstPartIsSuppliedButIsEmpty(), equalTo(false));
    }

    @Test
    public void addsADotSegmentOntoSegmentsWhenFirstSegmentContainsColonAndColonNotAllowed() throws Exception {
        Segment segment = segment(aStringIncluding(':'));
        assertThat(RelativeSegments.rootlessSegments(segment).asString(false), equalTo("./" + segment.asString()));
    }

    @Test
    public void doesNotAddADotSegmentOntoSegmentsWhenFirstSegmentContainsColonAndColonAllowed() throws Exception {
        Segment segment = segment(aStringIncluding(':'));
        assertThat(RelativeSegments.rootlessSegments(segment).asString(true), equalTo(segment.asString()));
    }

    @Test
    public void removesDotSegments() throws Exception {
        assertThat(RelativeSegments.rootlessSegments(segment("a"), segment("b"), segment("c"), DOT, DOT_DOT, DOT_DOT, segment("g")), equalTo(RelativeSegments.rootlessSegments(segment("a"), segment("g"))));
    }
}
