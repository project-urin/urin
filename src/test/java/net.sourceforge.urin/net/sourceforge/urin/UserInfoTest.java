/*
 * Copyright 2019 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.urin;

import org.junit.jupiter.api.Test;

import static net.sourceforge.urin.CharacterSets.SUB_DELIMS;
import static net.sourceforge.urin.CharacterSets.UNRESERVED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

class UserInfoTest {
    @Test
    void asStringReturnsValueProvidedForUnreservedCharacters() {
        String nonPercentEncodedCharacters = UNRESERVED + SUB_DELIMS + ":";
        assertThat(UserInfo.userInfo(nonPercentEncodedCharacters).asString(), equalTo(nonPercentEncodedCharacters));
    }

    @Test
    void asStringPercentEncodesNonUnreservedCharacters() {
        assertThat(UserInfo.userInfo(".@.#.[.]. .").asString(), equalTo(".%40.%23.%5B.%5D.%20."));
    }

    @Test
    void parsesUnreservedCharacters() throws Exception {
        String nonPercentEncodedCharacters = UNRESERVED + SUB_DELIMS + ":";
        assertThat(UserInfo.parse(nonPercentEncodedCharacters), equalTo(UserInfo.userInfo(nonPercentEncodedCharacters)));
    }

    @Test
    void parsesNonUnreservedCharacters() throws Exception {
        assertThat(UserInfo.parse(".%40.%23.%5B.%5D.%20."), equalTo(UserInfo.userInfo(".@.#.[.]. .")));
    }

}
