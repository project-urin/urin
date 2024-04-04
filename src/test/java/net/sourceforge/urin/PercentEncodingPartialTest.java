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

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.joining;
import static net.sourceforge.urin.PercentEncoder.ENCODE_EVERYTHING;
import static net.sourceforge.urin.PercentEncoder.ENCODE_NOTHING;
import static net.sourceforge.urin.PercentEncodingPartial.PercentEncoding.percentEncodingString;
import static net.sourceforge.urin.PercentEncodingPartial.noOp;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PercentEncodingPartialTest {

    @Test
    void additionallyEncodingNullThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> PercentEncodingPartial.additionallyEncoding(singletonList(null), noOp()));
    }

    @Test
    void additionallyEncodingEncodesASampleCharacterList() {
        final List<Character> additionallyEncoded = asList('!', '$', '&', '\'', '(', ')', '*', '+', ',', ';', '=', ':', '@');
        final PercentEncodingPartial.PercentEncoding<String> percentEncoding = PercentEncodingPartial.<String>additionallyEncoding(additionallyEncoded, noOp()).apply(percentEncodingString(ENCODE_NOTHING));
        final String additionallyEncodedString = additionallyEncoded.stream()
                .map(Object::toString)
                .collect(joining());
        assertThat(percentEncoding.encode(CharacterSets.UNRESERVED + additionallyEncodedString), equalTo(CharacterSets.UNRESERVED + "%21%24%26%27%28%29%2A%2B%2C%3B%3D%3A%40"));
    }

    @Test
    void additionallyEncodingAnAlreadyEncodedCharacterHasNoEffect() {
        final List<Character> additionallyEncoded = asList('!', '$', '&', '\'', '(', ')', '*', '+', ',', ';', '=', ':', '@');
        final PercentEncodingPartial.PercentEncoding<String> percentEncoding = PercentEncodingPartial.<String>additionallyEncoding(additionallyEncoded, noOp()).apply(percentEncodingString(ENCODE_EVERYTHING));
        final String additionallyEncodedString = additionallyEncoded.stream()
                .map(Object::toString)
                .collect(joining());
        assertThat(percentEncoding.encode(additionallyEncodedString), equalTo("%21%24%26%27%28%29%2A%2B%2C%3B%3D%3A%40"));
    }

    @Test
    void additionallyEncodingNothingHasNoEffect() {
        final PercentEncodingPartial.PercentEncoding<String> percentEncoding = PercentEncodingPartial.<String>additionallyEncoding(emptyList(), noOp()).apply(percentEncodingString(ENCODE_NOTHING));
        assertThat(percentEncoding.encode(CharacterSets.UNRESERVED), equalTo(CharacterSets.UNRESERVED));
    }
}