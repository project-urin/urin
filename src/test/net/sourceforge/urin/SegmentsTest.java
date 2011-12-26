/*
 * Copyright 2011 Mark Slater
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
import static net.sourceforge.urin.MoreRandomStringUtils.randomExcluding;
import static net.sourceforge.urin.MoreRandomStringUtils.randomIncluding;
import static net.sourceforge.urin.Segment.*;
import static net.sourceforge.urin.SegmentBuilder.aSegment;
import static net.sourceforge.urin.Segments.segments;
import static org.apache.commons.lang3.RandomStringUtils.random;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class SegmentsTest {
    @Test
    public void aSegmentsIsEqualToAnotherSegmentsWithTheSameMembers() throws Exception {
        Segment firstSegment = aSegment();
        Segment secondSegment = aSegment();
        assertThat(segments(firstSegment, secondSegment), equalTo(segments(firstSegment, secondSegment)));
        assertThat(segments(firstSegment, secondSegment).hashCode(), equalTo(segments(firstSegment, secondSegment).hashCode()));
    }

    @Test
    public void aSegmentsUsingVarargsFactoryIsEqualToWithTheSameMembersMadeUsingIterableFactory() throws Exception {
        Segment firstSegment = aSegment();
        Segment secondSegment = aSegment();
        assertThat(segments(firstSegment, secondSegment), equalTo(segments(asList(firstSegment, secondSegment))));
        assertThat(segments(firstSegment, secondSegment).hashCode(), equalTo(segments(asList(firstSegment, secondSegment)).hashCode()));
    }

    @Test
    public void aSegmentsUsingSegmentVarargsFactoryIsEqualToWithTheSameMembersMadeUsingStringVarargsFactory() throws Exception {
        String firstSegmentValue = random(5);
        Segment firstSegment = segment(firstSegmentValue);
        String secondSegmentValue = random(5);
        Segment secondSegment = segment(secondSegmentValue);
        assertThat(segments(firstSegment, secondSegment), equalTo(segments(firstSegmentValue, secondSegmentValue)));
        assertThat(segments(firstSegment, secondSegment).hashCode(), equalTo(segments(firstSegmentValue, secondSegmentValue).hashCode()));
    }

    @Test
    public void aSegmentsIsNotEqualToAnotherSegmentsWithDifferentMembers() throws Exception {
        assertThat(segments(aSegment(), aSegment()), not(equalTo(segments(aSegment(), aSegment()))));
    }

    @Test
    public void aSegmentsToStringIsCorrect() throws Exception {
        Segment firstSegment = aSegment();
        Segment secondSegment = aSegment();
        assertThat(segments(firstSegment, secondSegment).toString(), equalTo("[" + firstSegment + ", " + secondSegment + "]"));
    }

    @Test
    public void correctlyIdentifiesFirstPartAsBeingSuppliedButEmpty() throws Exception {
        assertThat(segments(EMPTY).firstPartIsSuppliedButIsEmpty(), equalTo(true));
    }

    @Test
    public void correctlyIdentifiesFirstPartUnsupplied() throws Exception {
        assertThat(segments().firstPartIsSuppliedButIsEmpty(), equalTo(false));
    }

    @Test
    public void correctlyIdentifiesFirstPartNonEmpty() throws Exception {
        assertThat(segments(aSegment()).firstPartIsSuppliedButIsEmpty(), equalTo(false));
    }

    @Test
    public void correctlyIdentifiesFirstPartContainingColon() throws Exception {
        assertThat(segments(segment(randomIncluding(':', 5))).firstPartIsSuppliedButContainsColon(), equalTo(true));
    }

    @Test
    public void correctlyIdentifiesFirstPartDoesNotContainColon() throws Exception {
        assertThat(segments(segment(randomExcluding(':', 5))).firstPartIsSuppliedButContainsColon(), equalTo(false));
    }

    @Test
    public void addsAnEmptyPartOntoAnEmptySegments() throws Exception {
        assertThat(segments().prefixWithEmptySegment(), equalTo(segments(EMPTY)));
    }

    @Test
    public void addsAnEmptyPartOntoANonEmptySegments() throws Exception {
        Segment segment = aSegment();
        assertThat(segments(segment).prefixWithEmptySegment(), equalTo(segments(EMPTY, segment)));
    }

    @Test
    public void addsADotSegmentOntoSegments() throws Exception {
        Segment segment = aSegment();
        assertThat(segments(segment).prefixWithDotSegment(), equalTo(segments(DOT, segment)));
    }

    @Test
    public void removesDotSegments() throws Exception {
        assertThat(segments(segment("a"), segment("b"), segment("c"), DOT, DOT_DOT, DOT_DOT, segment("g")), equalTo(segments(segment("a"), segment("g"))));
    }
}
