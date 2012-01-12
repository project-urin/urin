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
import static net.sourceforge.urin.QueryBuilder.aQuery;
import static net.sourceforge.urin.RelativeReference.relativeReference;
import static net.sourceforge.urin.Segment.DOT;
import static net.sourceforge.urin.Segment.segment;
import static net.sourceforge.urin.SegmentBuilder.aSegment;
import static net.sourceforge.urin.Segments.rootlessSegments;
import static net.sourceforge.urin.Segments.segments;
import static net.sourceforge.urin.SegmentsBuilder.aSegments;
import static net.sourceforge.urin.SegmentsBuilder.anAbsoluteSegments;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

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
    public void aRelativeReferenceWithEmptyPathWithQueryAsStringIsCorrect() throws Exception {
        Query query = aQuery();
        assertThat(relativeReference(query).asString(), equalTo("?" + query.asString()));
        assertThat(relativeReference(query).asUri(), equalTo(URI.create("?" + query.asString())));
    }

    @Test
    public void aRelativeReferenceWithEmptyPathWithQueryIsEqualToAnotherRelativeReferenceWithEmptyPath() throws Exception {
        Query query = aQuery();
        assertThat(relativeReference(query), equalTo(relativeReference(query)));
        assertThat(relativeReference(query).hashCode(), equalTo(relativeReference(query).hashCode()));
    }

    @Test
    public void aRelativeReferenceWithEmptyPathWithQueryIsNotEqualToAnotherWithTheADifferentQuery() throws Exception {
        assertThat(relativeReference(aQuery()), not(equalTo(relativeReference(aQuery()))));
    }

    @Test
    public void aRelativeReferenceWithEmptyPathWithQueryToStringIsCorrect() throws Exception {
        Query query = aQuery();
        assertThat(relativeReference(query).toString(), equalTo("RelativeReference{segments=EmptySegments, query=" + query.toString() + "}"));
    }

    @Test
    public void rejectsNullInFactoryForARelativeReferenceWithEmptyPathWithQuery() throws Exception {
        assertThrowsNullPointerException("Null query should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Query query = null;
                relativeReference(query);
            }
        });
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
        assertThat(relativeReference(segments(firstSegment, secondSegment)).asString(), equalTo("/./" + firstSegment.asString() + "/" + secondSegment.asString()));
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
        Segments segments = SegmentsBuilder.aSegments();
        assertThat(relativeReference(segments), equalTo(relativeReference(segments)));
        assertThat(relativeReference(segments).hashCode(), equalTo(relativeReference(segments).hashCode()));
    }

    @Test
    public void aSimpleAbsolutePathIsNotEqualToAnotherWithTheADifferentPath() throws Exception {
        assertThat(relativeReference(SegmentsBuilder.aSegments()), not(equalTo(relativeReference(SegmentsBuilder.aSegments()))));
    }

    @Test
    public void aSimpleAbsolutePathToStringIsCorrect() throws Exception {
        Segments segments = SegmentsBuilder.aSegments();
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
        assertThat(relativeReference(rootlessSegments(firstSegment, secondSegment)).asString(), equalTo("./" + firstSegment.asString() + "/" + secondSegment.asString()));
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
        Segments segments = SegmentsBuilder.aSegments();
        assertThat(relativeReference(segments), equalTo(relativeReference(segments)));
        assertThat(relativeReference(segments).hashCode(), equalTo(relativeReference(segments).hashCode()));
    }

    @Test
    public void aSimpleRootlessPathIsNotEqualToAnotherWithTheADifferentPath() throws Exception {
        assertThat(relativeReference(SegmentsBuilder.aSegments()), not(equalTo(relativeReference(SegmentsBuilder.aSegments()))));
    }

    @Test
    public void aSimpleRootlessPathToStringIsCorrect() throws Exception {
        Segments segments = SegmentsBuilder.aSegments();
        assertThat(relativeReference(segments).toString(), equalTo("RelativeReference{segments=" + segments + "}"));
    }

    @Test
    public void aRelativeReferenceWithNoAuthorityResolveAuthorityToTheBase() throws Exception {
        Authority baseAuthority = anAuthority();
        assertThat(relativeReference(aSegments()).resolveAuthority(baseAuthority), equalTo(baseAuthority));
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
        assertThat(relativeReference(authority, segments(firstSegment, secondSegment)).asString(), equalTo("//" + authority.asString() + "/" + firstSegment.asString() + "/" + secondSegment.asString()));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndPathIsEqualToAnotherWithTheSameAuthorityAndPath() throws Exception {
        Authority authority = anAuthority();
        AbsoluteSegments absoluteSegments = anAbsoluteSegments();
        assertThat(relativeReference(authority, absoluteSegments), equalTo(relativeReference(authority, absoluteSegments)));
        assertThat(relativeReference(authority, absoluteSegments).hashCode(), equalTo(relativeReference(authority, absoluteSegments).hashCode()));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndPathIsNotEqualToAnotherWithTheADifferentAuthorityAndPath() throws Exception {
        assertThat(relativeReference(anAuthority(), anAbsoluteSegments()), not(equalTo(relativeReference(anAuthority(), anAbsoluteSegments()))));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndPathToStringIsCorrect() throws Exception {
        Authority authority = anAuthority();
        AbsoluteSegments absoluteSegments = anAbsoluteSegments();
        assertThat(relativeReference(authority, absoluteSegments).toString(), equalTo("RelativeReference{authority=" + authority + ", segments=" + absoluteSegments + "}"));
    }

    @Test
    public void aRelativeReferenceWithAuthorityResolveAuthorityToTheRelative() throws Exception {
        Authority relativeAuthority = anAuthority();
        assertThat(relativeReference(relativeAuthority, anAbsoluteSegments()).resolveAuthority(anAuthority()), equalTo(relativeAuthority));
    }

    @Test
    public void rejectsNullInFactoryForRelativeReferenceWithAuthorityAndPath() throws Exception {
        assertThrowsNullPointerException("Null authority should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Authority authority = null;
                relativeReference(authority, anAbsoluteSegments());
            }
        });
        assertThrowsNullPointerException("Null segments should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                AbsoluteSegments absoluteSegments = null;
                relativeReference(anAuthority(), absoluteSegments);
            }
        });
    }

}
