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
import static net.sourceforge.urin.AbsoluteSegments.absoluteSegments;
import static net.sourceforge.urin.MoreRandomStringUtils.*;
import static net.sourceforge.urin.Segment.*;
import static net.sourceforge.urin.SegmentBuilder.aSegment;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class AbsoluteSegmentsTest {
    @Test
    public void anAbsoluteSegmentsIsEqualToAnotherAbsoluteSegmentsWithTheSameMembers() throws Exception {
        Segment firstSegment = aSegment();
        Segment secondSegment = aSegment();
        assertThat(absoluteSegments(firstSegment, secondSegment), equalTo(absoluteSegments(firstSegment, secondSegment)));
        assertThat(absoluteSegments(firstSegment, secondSegment).hashCode(), equalTo(absoluteSegments(firstSegment, secondSegment).hashCode()));
    }

    @Test
    public void aSegmentsUsingVarargsFactoryIsEqualToWithTheSameMembersMadeUsingIterableFactory() throws Exception {
        Segment firstSegment = aSegment();
        Segment secondSegment = aSegment();
        assertThat(absoluteSegments(firstSegment, secondSegment), equalTo(absoluteSegments(asList(firstSegment, secondSegment))));
        assertThat(absoluteSegments(firstSegment, secondSegment).hashCode(), equalTo(absoluteSegments(asList(firstSegment, secondSegment)).hashCode()));
    }

    @Test
    public void aSegmentsUsingSegmentVarargsFactoryIsEqualToWithTheSameMembersMadeUsingStringVarargsFactory() throws Exception {
        String firstSegmentValue = aString();
        Segment firstSegment = segment(firstSegmentValue);
        String secondSegmentValue = aString();
        Segment secondSegment = segment(secondSegmentValue);
        assertThat(absoluteSegments(firstSegment, secondSegment), equalTo(absoluteSegments(firstSegmentValue, secondSegmentValue)));
        assertThat(absoluteSegments(firstSegment, secondSegment).hashCode(), equalTo(absoluteSegments(firstSegmentValue, secondSegmentValue).hashCode()));
    }

    @Test
    public void aSegmentsIsNotEqualToAnotherSegmentsWithDifferentMembers() throws Exception {
        assertThat(absoluteSegments(aSegment(), aSegment()), not(equalTo(absoluteSegments(aSegment(), aSegment()))));
    }

    @Test
    public void aSegmentsToStringIsCorrect() throws Exception {
        Segment firstSegment = aSegment();
        Segment secondSegment = aSegment();
        assertThat(absoluteSegments(firstSegment, secondSegment).toString(), equalTo("[" + firstSegment + ", " + secondSegment + "]"));
    }

    @Test
    public void correctlyIdentifiesFirstPartAsBeingSuppliedButEmpty() throws Exception {
        assertThat(absoluteSegments(EMPTY).firstPartIsSuppliedButIsEmpty(), equalTo(true));
    }

    @Test
    public void correctlyIdentifiesFirstPartUnsupplied() throws Exception {
        assertThat(absoluteSegments().firstPartIsSuppliedButIsEmpty(), equalTo(false));
    }

    @Test
    public void correctlyIdentifiesFirstPartNonEmpty() throws Exception {
        assertThat(absoluteSegments(aSegment()).firstPartIsSuppliedButIsEmpty(), equalTo(false));
    }

    @Test
    public void correctlyIdentifiesFirstPartContainingColon() throws Exception {
        assertThat(absoluteSegments(segment(aStringIncluding(':'))).firstPartIsSuppliedButContainsColon(), equalTo(true));
    }

    @Test
    public void correctlyIdentifiesFirstPartDoesNotContainColon() throws Exception {
        assertThat(absoluteSegments(segment(aStringExcluding(':'))).firstPartIsSuppliedButContainsColon(), equalTo(false));
    }

    @Test
    public void addsAnEmptyPartOntoAnEmptySegments() throws Exception {
        assertThat(absoluteSegments().prefixWithEmptySegment(), equalTo(absoluteSegments(EMPTY)));
    }

    @Test
    public void addsAnEmptyPartOntoANonEmptySegments() throws Exception {
        Segment segment = aSegment();
        assertThat(absoluteSegments(segment).prefixWithEmptySegment(), equalTo(absoluteSegments(EMPTY, segment)));
    }

    @Test
    public void addsADotSegmentOntoSegments() throws Exception {
        Segment segment = aSegment();
        assertThat(absoluteSegments(segment).prefixWithDotSegment().asString(), equalTo("./" + segment.asString()));
    }

    @Test
    public void removesDotSegments() throws Exception {
        assertThat(absoluteSegments(segment("a"), segment("b"), segment("c"), DOT, DOT_DOT, DOT_DOT, segment("g")), equalTo(absoluteSegments(segment("a"), segment("g"))));
    }
}
