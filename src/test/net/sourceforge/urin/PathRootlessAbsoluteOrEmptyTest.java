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

import static net.sourceforge.urin.CharacterSets.P_CHARS;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class PathRootlessAbsoluteOrEmptyTest {

    @Test
    public void asStringReturnsValueProvidedForUnreservedCharacters() throws Exception {
        assertThat(new PathRootlessAbsoluteOrEmpty(P_CHARS).asString(), equalTo(P_CHARS));
    }

    @Test
    public void asStringPercentEncodesNonUnreservedCharacters() throws Exception {
        assertThat(new PathRootlessAbsoluteOrEmpty("./.?.#.[.]. .").asString(), equalTo(".%2F.%3F.%23.%5B.%5D.%20."));
    }

    @Test
    public void encodesMultipleSegments() throws Exception {
        assertThat(new PathRootlessAbsoluteOrEmpty("/", "/").asString(), equalTo("%2F/%2F"));
    }
}
