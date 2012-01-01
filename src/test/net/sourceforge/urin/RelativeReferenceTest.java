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

import java.net.URI;

import static net.sourceforge.urin.AuthorityBuilder.anAuthority;
import static net.sourceforge.urin.MoreRandomStringUtils.aStringIncluding;
import static net.sourceforge.urin.NullTest.assertThrowsNullPointerException;
import static net.sourceforge.urin.RelativeReference.relativeReference;
import static net.sourceforge.urin.RelativeReference.relativeReferenceAbsolute;
import static net.sourceforge.urin.Segment.DOT;
import static net.sourceforge.urin.Segment.segment;
import static net.sourceforge.urin.SegmentBuilder.aSegment;
import static net.sourceforge.urin.Segments.rootlessSegments;
import static net.sourceforge.urin.Segments.segments;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class RelativeReferenceTest {
    @Test
    public void aRelativeReferenceWithEmptyPathAsStringIsCorrect() throws Exception {
        assertThat(relativeReference().asString(), equalTo(""));
        assertThat(relativeReference().asUri(), equalTo(URI.create("")));
    }

    @Test
    public void aRelativeReferenceWithEmptyPathIsEqualToAnotherRelativeReferenceWithEmptyPath() throws Exception {
        assertThat(relativeReference(), equalTo(relativeReference()));
        assertThat(relativeReference().hashCode(), equalTo(relativeReference().hashCode()));
    }

    @Test
    public void aRelativeReferenceWithEmptyPathToStringIsCorrect() throws Exception {
        assertThat(relativeReference().toString(), equalTo("RelativeReference{segments=EmptySegments}"));
    }

    @Test
    public void aSimpleAbsolutePathAsStringReturnsThePath() throws Exception {
        Segment firstSegment = aSegment();
        Segment secondSegment = aSegment();
        assertThat(relativeReference(segments(firstSegment, secondSegment)).asString(), equalTo("/" + firstSegment.asString() + "/" + secondSegment.asString()));
        assertThat(relativeReference(segments(firstSegment, secondSegment)).asUri(), equalTo(URI.create("/" + firstSegment.asString() + "/" + secondSegment.asString())));
    }

    @Test
    public void aSimpleAbsolutePathRejectsAnEmptyFirstSegment() throws Exception {
        Segment firstSegment = segment("");
        Segment secondSegment = aSegment();
        try {
            relativeReference(segments(firstSegment, secondSegment)).asString();
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("If supplied, first segment must be non-empty"));
        }
    }

    @Test
    public void rejectsNullInFactoryForASimplePath() throws Exception {
        assertThrowsNullPointerException("Null first segment should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Segments segments = null;
                relativeReference(segments);
            }
        });
    }

    @Test
    public void aSimpleAbsolutePathIsEqualToAnotherWithTheSamePath() throws Exception {
        Segments segments = SegmentsBuilder.segments();
        assertThat(relativeReference(segments), equalTo(relativeReference(segments)));
        assertThat(relativeReference(segments).hashCode(), equalTo(relativeReference(segments).hashCode()));
    }

    @Test
    public void aSimpleAbsolutePathIsNotEqualToAnotherWithTheADifferentPath() throws Exception {
        assertThat(relativeReference(SegmentsBuilder.segments()), not(equalTo(relativeReference(SegmentsBuilder.segments()))));
    }

    @Test
    public void aSimpleAbsolutePathToStringIsCorrect() throws Exception {
        Segments segments = SegmentsBuilder.segments();
        assertThat(relativeReference(segments).toString(), equalTo("RelativeReference{segments=" + segments + "}"));
    }

    @Test
    public void aSimpleRootlessPathAsStringReturnsThePath() throws Exception {
        Segment firstSegment = aSegment();
        Segment secondSegment = aSegment();
        assertThat(relativeReference(rootlessSegments(firstSegment, secondSegment)).asString(), equalTo(firstSegment.asString() + "/" + secondSegment.asString()));
        assertThat(relativeReference(rootlessSegments(firstSegment, secondSegment)).asUri(), equalTo(URI.create(firstSegment.asString() + "/" + secondSegment.asString())));
    }

    @Test
    public void aSimpleRootlessPathRejectsAnEmptyFirstSegment() throws Exception {
        Segment firstSegment = segment("");
        Segment secondSegment = aSegment();
        try {
            relativeReference(rootlessSegments(firstSegment, secondSegment)).asString();
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("If supplied, first segment must be non-empty"));
        }
    }

    @Test
    public void aSimpleRootlessPathPrependsAColonInFirstSegmentWithDotSlash() throws Exception {
        Segment firstSegment = segment(aStringIncluding(':'));
        Segment secondSegment = aSegment();
        assertThat(relativeReference(rootlessSegments(firstSegment, secondSegment)), equalTo(relativeReference(rootlessSegments(DOT, firstSegment, secondSegment))));
    }

    @Test
    public void aSimpleRootlessPathPermitsAColonInTrailingSegments() throws Exception {
        relativeReference(rootlessSegments(aSegment(), segment(aStringIncluding(':')))).asString();
    }

    @Test
    public void aSimpleRootlessPathIsEqualToAnotherWithTheSamePath() throws Exception {
        Segments segments = SegmentsBuilder.segments();
        assertThat(relativeReference(segments), equalTo(relativeReference(segments)));
        assertThat(relativeReference(segments).hashCode(), equalTo(relativeReference(segments).hashCode()));
    }

    @Test
    public void aSimpleRootlessPathIsNotEqualToAnotherWithTheADifferentPath() throws Exception {
        assertThat(relativeReference(SegmentsBuilder.segments()), not(equalTo(relativeReference(SegmentsBuilder.segments()))));
    }

    @Test
    public void aSimpleRootlessPathToStringIsCorrect() throws Exception {
        Segments segments = SegmentsBuilder.segments();
        assertThat(relativeReference(segments).toString(), equalTo("RelativeReference{segments=" + segments + "}"));
    }

    @Test
    public void makesRelativeReferenceWithAuthorityAndEmptyPath() throws Exception {
        Authority authority = anAuthority();
        assertThat(relativeReference(authority).asString(), equalTo("//" + authority.asString()));
    }

    @Test
    public void rejectsNullInFactoryForRelativeReferenceWithAuthorityAndEmptyPath() throws Exception {
        assertThrowsNullPointerException("Null authority should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                relativeReference((Authority) null);
            }
        });
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndEmptyPathIsEqualToAnotherWithTheSameAuthority() throws Exception {
        Authority authority = anAuthority();
        assertThat(relativeReference(authority), equalTo(relativeReference(authority)));
        assertThat(relativeReference(authority).hashCode(), equalTo(relativeReference(authority).hashCode()));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndEmptyPathIsNotEqualToAnotherWithTheADifferentAuthority() throws Exception {
        assertThat(relativeReference(anAuthority()), not(equalTo(relativeReference(anAuthority()))));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndEmptyPathToStringIsCorrect() throws Exception {
        Authority authority = anAuthority();
        assertThat(relativeReference(authority).toString(), equalTo("RelativeReference{authority=" + authority + ", segments=EmptySegments}"));
    }

    @Test
    public void makesRelativeReferenceWithAuthorityAndNonEmptyPath() throws Exception {
        Authority authority = anAuthority();
        Segment firstSegment = aSegment();
        Segment secondSegment = aSegment();
        assertThat(relativeReferenceAbsolute(authority, firstSegment, secondSegment).asString(), equalTo("//" + authority.asString() + "/" + firstSegment.asString() + "/" + secondSegment.asString()));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndNonEmptyPathHasImmutableVarargs() throws Exception {
        Authority authority = anAuthority();
        Segment firstSegment = aSegment();
        Segment secondSegment = aSegment();
        Segment[] segments = {firstSegment, secondSegment};
        RelativeReference relativeReference = relativeReferenceAbsolute(authority, segments);
        segments[0] = aSegment();
        assertThat(relativeReference.asString(), equalTo("//" + authority.asString() + "/" + firstSegment.asString() + "/" + secondSegment.asString()));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndPathIsEqualToAnotherWithTheSameAuthorityAndPath() throws Exception {
        Authority authority = anAuthority();
        Segment firstSegment = aSegment();
        Segment secondSegment = aSegment();
        assertThat(relativeReferenceAbsolute(authority, firstSegment, secondSegment), equalTo(relativeReferenceAbsolute(authority, firstSegment, secondSegment)));
        assertThat(relativeReferenceAbsolute(authority, firstSegment, secondSegment).hashCode(), equalTo(relativeReferenceAbsolute(authority, firstSegment, secondSegment).hashCode()));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndPathIsNotEqualToAnotherWithTheADifferentAuthorityAndPath() throws Exception {
        assertThat(relativeReferenceAbsolute(anAuthority(), aSegment(), aSegment()), not(equalTo(relativeReferenceAbsolute(anAuthority(), aSegment(), aSegment()))));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndPathToStringIsCorrect() throws Exception {
        Authority authority = anAuthority();
        Segment firstSegment = aSegment();
        Segment secondSegment = aSegment();
        assertThat(relativeReferenceAbsolute(authority, firstSegment, secondSegment).toString(), equalTo("RelativeReference{authority=" + authority + ", segments=[" + firstSegment + ", " + secondSegment + "]}"));
    }

    @Test
    public void rejectsNullInFactoryForRelativeReferenceWithAuthorityAndPath() throws Exception {
        assertThrowsNullPointerException("Null authority should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Authority authority = null;
                relativeReferenceAbsolute(authority, aSegment(), aSegment());
            }
        });
        assertThrowsNullPointerException("Null first segment should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                relativeReferenceAbsolute(anAuthority(), null, aSegment());
            }
        });
        assertThrowsNullPointerException("Null second segment should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                relativeReferenceAbsolute(anAuthority(), aSegment(), null);
            }
        });
    }

}
