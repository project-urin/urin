/*
 * Copyright 2024 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.urin;

import org.junit.jupiter.api.Test;

import static net.sourceforge.urin.CharacterSets.P_CHARS;
import static net.sourceforge.urin.MoreRandomStringUtils.aString;
import static net.sourceforge.urin.Segment.*;
import static net.sourceforge.urin.SegmentBuilder.aNonDotSegment;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SegmentTest {

    @Test
    void asStringReturnsValueProvidedForUnreservedCharacters() {
        assertThat(segment(P_CHARS).asString(), equalTo(P_CHARS));
    }

    @Test
    void asStringPercentEncodesNonUnreservedCharacters() {
        assertThat(segment(".#.[.]. .").asString(), equalTo(".%23.%5B.%5D.%20."));
    }

    @Test
    void asStringPercentEncodesDotSegment() {
        assertThat(segment(".").asString(), equalTo("%2E"));
    }

    @Test
    void asStringPercentEncodesDotDotSegment() {
        assertThat(segment("..").asString(), equalTo("%2E%2E"));
    }

    @Test
    void explicitDotSegmentIsNotEncoded() {
        assertThat(dot().asString(), equalTo("."));
    }

    @Test
    void explicitDotDotSegmentIsNotEncoded() {
        assertThat(dotDot().asString(), equalTo(".."));
    }

    @Test
    void emptySegmentIsNotEncoded() {
        assertThat(empty().asString(), equalTo(""));
    }

    @Test
    void parsesUnreservedCharacters() throws Exception {
        assertThat(parse(P_CHARS, STRING_SEGMENT_MAKING_DECODER), equalTo(segment(P_CHARS)));
    }

    @Test
    void parsePercentDecodesNonUnreservedCharacters() throws Exception {
        assertThat(parse(".%23.%5B.%5D.%20.", STRING_SEGMENT_MAKING_DECODER), equalTo(segment(".#.[.]. .")));
    }

    @Test
    void parsesPercentEncodedDotSegment() throws Exception {
        assertThat(parse("%2E", STRING_SEGMENT_MAKING_DECODER), equalTo(segment(".")));
    }

    @Test
    void parsesPercentEncodedDotDotSegment() throws Exception {
        assertThat(parse("%2E%2E", STRING_SEGMENT_MAKING_DECODER), equalTo(segment("..")));
    }

    @Test
    void unencodedDotBecomesExplicitDotSegment() throws Exception {
        assertThat(parse(".", STRING_SEGMENT_MAKING_DECODER), equalTo(Segment.<String>dot()));
    }

    @Test
    void unencodedDotDotBecomesExplicitDotDotSegment() throws Exception {
        assertThat(parse("..", STRING_SEGMENT_MAKING_DECODER), equalTo(Segment.<String>dotDot()));
    }

    @Test
    void unencodedEmptyBecomesExplicitEmptySegment() throws Exception {
        assertThat(parse("", STRING_SEGMENT_MAKING_DECODER), equalTo(Segment.<String>empty()));
    }

    @Test
    void dotHasNoValue() {
        assertThat(dot().hasValue(), equalTo(false));
    }

    @Test
    void dotDotHasNoValue() {
        assertThat(dotDot().hasValue(), equalTo(false));
    }

    @Test
    void emptyHasNoValue() {
        assertThat(empty().hasValue(), equalTo(false));
    }

    @Test
    void nonDotSegmentHasValue() {
        assertThat(aNonDotSegment().hasValue(), equalTo(true));
    }

    @Test
    void dotThrowsUnsupportedOperationOnValueRetrieval() {
        final UnsupportedOperationException unsupportedOperationException = assertThrows(UnsupportedOperationException.class, () -> dot().value());
        assertThat(unsupportedOperationException.getMessage(), equalTo("Attempt to get value of . segment"));
    }

    @Test
    void dotDotThrowsUnsupportedOperationOnValueRetrieval() {
        final UnsupportedOperationException unsupportedOperationException = assertThrows(UnsupportedOperationException.class, () -> dotDot().value());
        assertThat(unsupportedOperationException.getMessage(), equalTo("Attempt to get value of .. segment"));
    }

    @Test
    void emptyThrowsUnsupportedOperationOnValueRetrieval() {
        final UnsupportedOperationException unsupportedOperationException = assertThrows(UnsupportedOperationException.class, () -> empty().value());
        assertThat(unsupportedOperationException.getMessage(), equalTo("Attempt to get value of empty segment"));
    }

    @Test
    void nonDotSegmentReturnsValue() {
        final String value = aString();
        assertThat(segment(value).value(), equalTo(value));
    }
}
