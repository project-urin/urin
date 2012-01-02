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

import static net.sourceforge.urin.AuthorityBuilder.anAuthority;
import static net.sourceforge.urin.HierarchicalPart.hierarchicalPart;
import static net.sourceforge.urin.NullTest.assertThrowsNullPointerException;
import static net.sourceforge.urin.Segment.segment;
import static net.sourceforge.urin.SegmentBuilder.aSegment;
import static net.sourceforge.urin.SegmentsBuilder.aSegments;
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
        assertThat(hierarchicalPart().toString(), equalTo("HierarchicalPart{segments=EmptySegments}"));
    }

    @Test
    public void aSimpleAbsolutePathAsStringReturnsThePath() throws Exception {
        Segments segments = aSegments();
        assertThat(hierarchicalPart(segments).asString(), equalTo(segments.asString(true)));
    }

    @Test
    public void aSimpleAbsolutePathRejectsAnEmptyFirstSegment() throws Exception {
        Segment firstSegment = segment("");
        Segment secondSegment = aSegment();
        try {
            hierarchicalPart(Segments.segments(firstSegment, secondSegment)).asString();
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("If supplied, first segment must be non-empty"));
        }
    }

    @Test
    public void rejectsNullInFactoryForASimplePath() throws Exception {
        assertThrowsNullPointerException("Null segments should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Segments segments = null;
                hierarchicalPart(segments);
            }
        });
    }

    @Test
    public void aSimpleAbsolutePathIsEqualToAnotherWithTheSamePath() throws Exception {
        Segments segments = aSegments();
        assertThat(hierarchicalPart(segments), equalTo(hierarchicalPart(segments)));
        assertThat(hierarchicalPart(segments).hashCode(), equalTo(hierarchicalPart(segments).hashCode()));
    }

    @Test
    public void aSimpleAbsolutePathIsNotEqualToAnotherWithTheADifferentPath() throws Exception {
        assertThat(hierarchicalPart(aSegments()), not(equalTo(hierarchicalPart(aSegments()))));
    }

    @Test
    public void aSimpleAbsolutePathToStringIsCorrect() throws Exception {
        Segments segments = aSegments();
        assertThat(hierarchicalPart(segments).toString(), equalTo("HierarchicalPart{segments=" + segments + "}"));
    }

    @Test
    public void aSimpleRootlessPathAsStringReturnsThePath() throws Exception {
        Segment firstSegment = aSegment();
        Segment secondSegment = aSegment();
        assertThat(hierarchicalPart(Segments.rootlessSegments(firstSegment, secondSegment)).asString(), equalTo(firstSegment.asString() + "/" + secondSegment.asString()));
    }

    @Test
    public void aSimpleRootlessPathRejectsAnEmptyFirstSegment() throws Exception {
        Segment firstSegment = segment("");
        Segment secondSegment = aSegment();
        try {
            hierarchicalPart(Segments.rootlessSegments(firstSegment, secondSegment)).asString();
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("If supplied, first segment must be non-empty"));
        }
    }

    @Test
    public void aSimpleRootlessPathIsEqualToAnotherWithTheSamePath() throws Exception {
        Segment firstSegment = aSegment();
        Segment secondSegment = aSegment();
        assertThat(hierarchicalPart(Segments.rootlessSegments(firstSegment, secondSegment)), equalTo(hierarchicalPart(Segments.rootlessSegments(firstSegment, secondSegment))));
        assertThat(hierarchicalPart(Segments.rootlessSegments(firstSegment, secondSegment)).hashCode(), equalTo(hierarchicalPart(Segments.rootlessSegments(firstSegment, secondSegment)).hashCode()));
    }

    @Test
    public void aSimpleRootlessPathIsNotEqualToAnotherWithTheADifferentPath() throws Exception {
        assertThat(hierarchicalPart(Segments.rootlessSegments(aSegment(), aSegment())), not(equalTo(hierarchicalPart(Segments.rootlessSegments(aSegment(), aSegment())))));
    }

    @Test
    public void aSimpleRootlessPathToStringIsCorrect() throws Exception {
        Segment firstSegment = aSegment();
        Segment secondSegment = aSegment();
        assertThat(hierarchicalPart(Segments.rootlessSegments(firstSegment, secondSegment)).toString(), equalTo("HierarchicalPart{segments=[" + firstSegment + ", " + secondSegment + "]}"));
    }

    @Test
    public void makesHierarchicalPartWithAuthorityAndEmptyPath() throws Exception {
        Authority authority = anAuthority();
        assertThat(hierarchicalPart(authority).asString(), equalTo("//" + authority.asString()));
    }

    @Test
    public void rejectsNullInFactoryForHierarchicalPartWithAuthorityAndEmptyPath() throws Exception {
        assertThrowsNullPointerException("Null authority should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Authority authority = null;
                hierarchicalPart(authority);
            }
        });
    }

    @Test
    public void aHierarchicalPartWithAuthorityAndEmptyPathIsEqualToAnotherWithTheSameAuthority() throws Exception {
        Authority authority = anAuthority();
        assertThat(hierarchicalPart(authority), equalTo(hierarchicalPart(authority)));
        assertThat(hierarchicalPart(authority).hashCode(), equalTo(hierarchicalPart(authority).hashCode()));
    }

    @Test
    public void aHierarchicalPartWithAuthorityAndEmptyPathIsNotEqualToAnotherWithTheADifferentAuthority() throws Exception {
        assertThat(hierarchicalPart(anAuthority()), not(equalTo(hierarchicalPart(anAuthority()))));
    }

    @Test
    public void aHierarchicalPartWithAuthorityAndEmptyPathToStringIsCorrect() throws Exception {
        Authority authority = anAuthority();
        assertThat(hierarchicalPart(authority).toString(), equalTo("HierarchicalPart{authority=" + authority + ", segments=EmptySegments}"));
    }

    @Test
    public void makesHierarchicalPartWithAuthorityAndNonEmptyPath() throws Exception {
        Authority authority = anAuthority();
        Segment firstSegment = aSegment();
        Segment secondSegment = aSegment();
        assertThat(hierarchicalPart(authority, Segments.segments(firstSegment, secondSegment)).asString(), equalTo("//" + authority.asString() + "/" + firstSegment.asString() + "/" + secondSegment.asString()));
    }

    @Test
    public void aHierarchicalPartWithAuthorityAndNonEmptyPathHasImmutableVarargs() throws Exception {
        Authority authority = anAuthority();
        Segment firstSegment = aSegment();
        Segment secondSegment = aSegment();
        Segment[] segments = {firstSegment, secondSegment};
        HierarchicalPart hierarchicalPart = hierarchicalPart(authority, Segments.segments(segments));
        segments[0] = aSegment();
        assertThat(hierarchicalPart.asString(), equalTo("//" + authority.asString() + "/" + firstSegment.asString() + "/" + secondSegment.asString()));
    }

    @Test
    public void aHierarchicalPartWithAuthorityAndPathIsEqualToAnotherWithTheSameAuthorityAndPath() throws Exception {
        Authority authority = anAuthority();
        AbsoluteSegments absoluteSegments = Segments.segments();
        assertThat(hierarchicalPart(authority, absoluteSegments), equalTo(hierarchicalPart(authority, absoluteSegments)));
        assertThat(hierarchicalPart(authority, absoluteSegments).hashCode(), equalTo(hierarchicalPart(authority, absoluteSegments).hashCode()));
    }

    @Test
    public void aHierarchicalPartWithAuthorityAndPathIsNotEqualToAnotherWithTheADifferentAuthorityAndPath() throws Exception {
        assertThat(hierarchicalPart(anAuthority(), Segments.segments()), not(equalTo(hierarchicalPart(anAuthority(), Segments.segments()))));
    }

    @Test
    public void aHierarchicalPartWithAuthorityAndPathToStringIsCorrect() throws Exception {
        Authority authority = anAuthority();
        AbsoluteSegments absoluteSegments = Segments.segments();
        assertThat(hierarchicalPart(authority, absoluteSegments).toString(), equalTo("HierarchicalPart{authority=" + authority + ", segments=" + absoluteSegments + "}"));
    }

    @Test
    public void rejectsNullInFactoryForHierarchicalPartWithAuthorityAndPath() throws Exception {
        assertThrowsNullPointerException("Null authority should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Authority authority = null;
                hierarchicalPart(authority, Segments.segments());
            }
        });
        assertThrowsNullPointerException("Null segments should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                hierarchicalPart(anAuthority(), null);
            }
        });
    }

}
