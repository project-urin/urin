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

import java.net.URI;

import static net.sourceforge.urin.NullTest.assertThrowsNullPointerException;
import static net.sourceforge.urin.RelativeReference.relativeReference;
import static net.sourceforge.urin.SegmentBuilder.aSegment;
import static net.sourceforge.urin.Segments.segments;
import static net.sourceforge.urin.SegmentsBuilder.aSegments;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class RelativeReferenceTest {

    @Test
    public void relativeReferenceWithNoQueryOrFragmentAsStringIsCorrect() throws Exception {
        assertThat(relativeReference(segments()).asString(), equalTo(""));
    }

    @Test
    public void relativeReferenceWithNoQueryOrFragmentAsUriIsCorrect() throws Exception {
        assertThat(relativeReference(segments()).asUri(), equalTo(URI.create("")));
    }

    @Test
    public void relativeReferenceRejectsNullSegments() throws Exception {
        assertThrowsNullPointerException("Null segments should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Segments segments = null;
                relativeReference(segments);
            }
        });
    }

    @Test
    public void relativeReferenceRejectsNullFirstSegment() throws Exception {
        assertThrowsNullPointerException("Null first segment should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Segment firstSegment = null;
                relativeReference(firstSegment, aSegment());
            }
        });
    }

    @Test
    public void relativeReferenceRejectsNullTrailingSegment() throws Exception {
        assertThrowsNullPointerException("Null first segment should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Segment secondSegmentSegment = null;
                relativeReference(aSegment(), secondSegmentSegment);
            }
        });
    }

    @Test
    public void varargsSegmentsAreImmutable() throws Exception {
        @SuppressWarnings({"MismatchedReadAndWriteOfArray"})
        Segment[] segments = new Segment[]{aSegment(), aSegment()};
        RelativeReference relativeReference = relativeReference(aSegment(), aSegment());
        String originalRelativeReferenceAsString = relativeReference.asString();
        segments[0] = aSegment();
        assertThat(originalRelativeReferenceAsString, equalTo(relativeReference.asString()));
    }

    @Test
    public void relativeReferenceWithEmptyPathToStringIsCorrect() throws Exception {
        assertThat(relativeReference().toString(), equalTo("RelativeReference{segments=[]}"));
    }

    @Test
    public void relativeReferenceWithPathToStringIsCorrect() throws Exception {
        Segments segments = aSegments();
        assertThat(relativeReference(segments).toString(), equalTo("RelativeReference{segments=" + segments + "}"));
    }

    @Test
    public void aRelativeReferenceWithEmptyPathIsEqualToAnotherRelativeReferenceWithEmptyPath() throws Exception {
        assertThat(relativeReference(), equalTo(relativeReference()));
        assertThat(relativeReference().hashCode(), equalTo(relativeReference().hashCode()));
    }

}
