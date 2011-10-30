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

import static net.sourceforge.urin.AuthorityBuilder.anAuthority;
import static net.sourceforge.urin.HierarchicalPart.*;
import static net.sourceforge.urin.Segment.EMPTY;
import static net.sourceforge.urin.Segment.segment;
import static net.sourceforge.urin.SegmentBuilder.aSegment;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class HierarchicalPartTest {
    @Test
    public void aHierarchicalPartWithEmptyPathAsStringIsCorrect() throws Exception {
        assertThat(hierarchicalPart().asString(), equalTo(""));
    }

    @Test
    public void aHierarchicalPartWithEmptyPathIsEqualToAnotherHierarchicalPartWithEmptyPath() throws Exception {
        assertThat(hierarchicalPart(), equalTo(hierarchicalPart()));
        assertThat(hierarchicalPart().hashCode(), equalTo(hierarchicalPart().hashCode()));
    }

    @Test
    public void aHierarchicalPartWithEmptyPathToStringIsCorrect() throws Exception {
        assertThat(hierarchicalPart().toString(), equalTo("HierarchicalPart{segments=[]}"));
    }

    @Test
    public void aSimpleAbsolutePathAsStringReturnsThePath() throws Exception {
        Segment firstSegment = aSegment();
        Segment secondSegment = aSegment();
        assertThat(hierarchicalPartAbsolute(firstSegment, secondSegment).asString(), equalTo("/" + firstSegment.asString() + "/" + secondSegment.asString()));
    }

    @Test
    public void aSimpleAbsolutePathRejectsAnEmptyFirstSegment() throws Exception {
        Segment firstSegment = segment("");
        Segment secondSegment = aSegment();
        try {
            hierarchicalPartAbsolute(firstSegment, secondSegment).asString();
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("If supplied, first segment must be non-empty"));
        }
    }

    @Test
    public void aSimpleAbsolutePathIsEqualToAnotherWithTheSamePath() throws Exception {
        Segment firstSegment = aSegment();
        Segment secondSegment = aSegment();
        assertThat(hierarchicalPartAbsolute(firstSegment, secondSegment), equalTo(hierarchicalPartAbsolute(firstSegment, secondSegment)));
        assertThat(hierarchicalPartAbsolute(firstSegment, secondSegment).hashCode(), equalTo(hierarchicalPartAbsolute(firstSegment, secondSegment).hashCode()));
    }

    @Test
    public void aSimpleAbsolutePathIsNotEqualToAnotherWithTheADifferentPath() throws Exception {
        assertThat(hierarchicalPartAbsolute(aSegment(), aSegment()), not(equalTo(hierarchicalPartAbsolute(aSegment(), aSegment()))));
    }

    @Test
    public void aSimpleAbsolutePathToStringIsCorrect() throws Exception {
        Segment firstSegment = aSegment();
        Segment secondSegment = aSegment();
        assertThat(hierarchicalPartAbsolute(firstSegment, secondSegment).toString(), equalTo("HierarchicalPart{segments=[" + EMPTY + ", " + firstSegment + ", " + secondSegment + "]}"));
    }

    @Test
    public void aSimpleRootlessPathAsStringReturnsThePath() throws Exception {
        Segment firstSegment = aSegment();
        Segment secondSegment = aSegment();
        assertThat(hierarchicalPartRootless(firstSegment, secondSegment).asString(), equalTo(firstSegment.asString() + "/" + secondSegment.asString()));
    }

    @Test
    public void aSimpleRootlessPathAsStringHasImmutableVarargs() throws Exception {
        Segment firstSegment = aSegment();
        Segment secondSegment = aSegment();
        Segment[] segments = {firstSegment, secondSegment};
        HierarchicalPart hierarchicalPart = hierarchicalPartRootless(segments);
        segments[0] = aSegment();
        assertThat(hierarchicalPart.asString(), equalTo(firstSegment.asString() + "/" + secondSegment.asString()));
    }

    @Test
    public void aSimpleRootlessPathRejectsAnEmptyFirstSegment() throws Exception {
        Segment firstSegment = segment("");
        Segment secondSegment = aSegment();
        try {
            hierarchicalPartRootless(firstSegment, secondSegment).asString();
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("If supplied, first segment must be non-empty"));
        }
    }

    @Test
    public void aSimpleAbsolutePathAsStringHasImmutableVarargs() throws Exception {
        Segment firstSegment = aSegment();
        Segment secondSegment = aSegment();
        Segment[] segments = {firstSegment, secondSegment};
        HierarchicalPart hierarchicalPart = hierarchicalPartAbsolute(segments);
        segments[0] = aSegment();
        assertThat(hierarchicalPart.asString(), equalTo("/" + firstSegment.asString() + "/" + secondSegment.asString()));
    }

    @Test
    public void makesHierarchicalPartWithAuthorityAndEmptyPath() throws Exception {
        Authority authority = anAuthority();
        assertThat(hierarchicalPart(authority).asString(), equalTo("//" + authority.asString()));
    }

    @Test
    public void makesHierarchicalPartWithAuthorityAndNonEmptyPath() throws Exception {
        Authority authority = anAuthority();
        Segment firstSegment = aSegment();
        Segment secondSegment = aSegment();
        assertThat(hierarchicalPartAbsolute(authority, firstSegment, secondSegment).asString(), equalTo("//" + authority.asString() + "/" + firstSegment.asString() + "/" + secondSegment.asString()));
    }

    @Test
    public void aHierarchicalPartWithAuthorityAndNonEmptyPathHasImmutableVarargs() throws Exception {
        Authority authority = anAuthority();
        Segment firstSegment = aSegment();
        Segment secondSegment = aSegment();
        Segment[] segments = {firstSegment, secondSegment};
        HierarchicalPart hierarchicalPart = hierarchicalPartAbsolute(authority, segments);
        segments[0] = aSegment();
        assertThat(hierarchicalPart.asString(), equalTo("//" + authority.asString() + "/" + firstSegment.asString() + "/" + secondSegment.asString()));
    }

}
