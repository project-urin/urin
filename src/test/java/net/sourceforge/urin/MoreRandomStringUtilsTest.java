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

import static net.sourceforge.urin.MoreRandomStringUtils.aChar;
import static net.sourceforge.urin.MoreRandomStringUtils.aStringIncluding;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

class MoreRandomStringUtilsTest {

    @Test
    void randomIncludingDoesInclude() {
        final char aChar = aChar();
        final String actual = aStringIncluding(aChar);
        assertThat(actual, containsString(String.valueOf(aChar)));
    }

}
