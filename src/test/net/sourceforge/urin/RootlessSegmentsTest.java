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
import static net.sourceforge.urin.RootlessSegments.rootlessSegments;
import static net.sourceforge.urin.Segment.*;
import static net.sourceforge.urin.SegmentBuilder.aSegment;
import static net.sourceforge.urin.Segments.emptySegments;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class RootlessSegmentsTest {
    @Test
    public void aSegmentsIsEqualToAnotherSegmentsWithTheSameMembers() throws Exception {
        Segment firstSegment = aSegment();
        Segment secondSegment = aSegment();
        assertThat(rootlessSegments(firstSegment, secondSegment), equalTo(rootlessSegments(firstSegment, secondSegment)));
        assertThat(rootlessSegments(firstSegment, secondSegment).hashCode(), equalTo(rootlessSegments(firstSegment, secondSegment).hashCode()));
    }

    @Test
    public void aSegmentsUsingVarargsFactoryIsEqualToWithTheSameMembersMadeUsingIterableFactory() throws Exception {
        Segment firstSegment = aSegment();
        Segment secondSegment = aSegment();
        assertThat(rootlessSegments(firstSegment, secondSegment), equalTo(rootlessSegments(asList(firstSegment, secondSegment))));
        assertThat(rootlessSegments(firstSegment, secondSegment).hashCode(), equalTo(rootlessSegments(asList(firstSegment, secondSegment)).hashCode()));
    }

    @Test
    public void aSegmentsUsingSegmentVarargsFactoryIsEqualToWithTheSameMembersMadeUsingStringVarargsFactory() throws Exception {
        String firstSegmentValue = aString();
        Segment firstSegment = segment(firstSegmentValue);
        String secondSegmentValue = aString();
        Segment secondSegment = segment(secondSegmentValue);
        assertThat(rootlessSegments(firstSegment, secondSegment), equalTo(rootlessSegments(firstSegmentValue, secondSegmentValue)));
        assertThat(rootlessSegments(firstSegment, secondSegment).hashCode(), equalTo(rootlessSegments(firstSegmentValue, secondSegmentValue).hashCode()));
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
        assertThat(rootlessSegments(aSegment(), aSegment()), not(equalTo(rootlessSegments(aSegment(), aSegment()))));
    }

    @Test
    public void zeroRootlessSegmentsEqualToEmptySegments() throws Exception {
        assertThat(rootlessSegments(), equalTo(emptySegments()));
    }

    @Test
    public void aSegmentsToStringIsCorrect() throws Exception {
        Segment firstSegment = aSegment();
        Segment secondSegment = aSegment();
        assertThat(rootlessSegments(firstSegment, secondSegment).toString(), equalTo("[" + firstSegment + ", " + secondSegment + "]"));
    }

    @Test
    public void correctlyIdentifiesFirstPartAsBeingSuppliedButEmpty() throws Exception {
        assertThat(rootlessSegments(EMPTY).firstPartIsSuppliedButIsEmpty(), equalTo(true));
    }

    @Test
    public void correctlyIdentifiesFirstPartUnsupplied() throws Exception {
        assertThat(rootlessSegments().firstPartIsSuppliedButIsEmpty(), equalTo(false));
    }

    @Test
    public void correctlyIdentifiesFirstPartNonEmpty() throws Exception {
        assertThat(rootlessSegments(aSegment()).firstPartIsSuppliedButIsEmpty(), equalTo(false));
    }

    @Test
    public void addsADotSegmentOntoSegmentsWhenFirstSegmentContainsColonAndColonNotAllowed() throws Exception {
        Segment segment = segment(aStringIncluding(':'));
        assertThat(rootlessSegments(segment).asString(false), equalTo("./" + segment.asString()));
    }

    @Test
    public void doesNotAddADotSegmentOntoSegmentsWhenFirstSegmentContainsColonAndColonAllowed() throws Exception {
        Segment segment = segment(aStringIncluding(':'));
        assertThat(rootlessSegments(segment).asString(true), equalTo(segment.asString()));
    }

    @Test
    public void removesDotSegments() throws Exception {
        assertThat(rootlessSegments(segment("a"), segment("b"), segment("c"), DOT, DOT_DOT, DOT_DOT, segment("g")), equalTo(rootlessSegments(segment("a"), segment("g"))));
    }
}
