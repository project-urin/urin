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

import static net.sourceforge.urin.CharacterSets.P_CHARS;
import static net.sourceforge.urin.Segment.DOT;
import static net.sourceforge.urin.Segment.DOT_DOT;
import static net.sourceforge.urin.SegmentBuilder.aNonDotSegment;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class SegmentTest {

    @Test
    public void asStringReturnsValueProvidedForUnreservedCharacters() throws Exception {
        assertThat(Segment.segment(P_CHARS).asString(), equalTo(P_CHARS));
    }

    @Test
    public void asStringPercentEncodesNonUnreservedCharacters() throws Exception {
        assertThat(Segment.segment(".#.[.]. .").asString(), equalTo(".%23.%5B.%5D.%20."));
    }

    @Test
    public void asStringPercentEncodesDotSegment() throws Exception {
        assertThat(Segment.segment(".").asString(), equalTo("%2E"));
    }

    @Test
    public void asStringPercentEncodesDotDotSegment() throws Exception {
        assertThat(Segment.segment("..").asString(), equalTo("%2E%2E"));
    }

    @Test
    public void explicitDotSegmentIsNotEncoded() throws Exception {
        assertThat(DOT.asString(), equalTo("."));
    }

    @Test
    public void explicitDotDotSegmentIsNotEncoded() throws Exception {
        assertThat(DOT_DOT.asString(), equalTo(".."));
    }

    @Test
    public void parsesUnreservedCharacters() throws Exception {
        assertThat(Segment.parse(P_CHARS), equalTo(Segment.segment(P_CHARS)));
    }

    @Test
    public void parsePercentDecodesNonUnreservedCharacters() throws Exception {
        assertThat(Segment.parse(".%23.%5B.%5D.%20."), equalTo(Segment.segment(".#.[.]. .")));
    }

    @Test
    public void parsesPercentEncodedDotSegment() throws Exception {
        assertThat(Segment.parse("%2E"), equalTo(Segment.segment(".")));
    }

    @Test
    public void parsesPercentEncodedDotDotSegment() throws Exception {
        assertThat(Segment.parse("%2E%2E"), equalTo(Segment.segment("..")));
    }

    @Test
    public void unencodedDotBecomesExplicitDotSegment() throws Exception {
        assertThat(Segment.parse("."), equalTo(DOT));
    }

    @Test
    public void unencodedDotDotBecomesExplicitDotDotSegment() throws Exception {
        assertThat(Segment.parse(".."), equalTo(DOT_DOT));
    }

    @Test
    public void dotHasNoValue() throws Exception {
        assertThat(DOT.hasValue(), equalTo(false));
    }

    @Test
    public void dotDotHasNoValue() throws Exception {
        assertThat(DOT_DOT.hasValue(), equalTo(false));
    }

    @Test
    public void nonDotSegmentHasValue() throws Exception {
        assertThat(aNonDotSegment().hasValue(), equalTo(true));
    }
}
