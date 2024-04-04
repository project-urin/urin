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

import static net.sourceforge.urin.CharacterSets.QUERY_AND_FRAGMENT_CHARACTERS;
import static net.sourceforge.urin.Fragment.fragment;
import static net.sourceforge.urin.FragmentBuilder.aFragment;
import static net.sourceforge.urin.MoreRandomStringUtils.aString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

class FragmentTest {

    @Test
    void asStringReturnsValueProvidedForUnreservedCharacters() {
        assertThat(fragment(QUERY_AND_FRAGMENT_CHARACTERS).asString(), equalTo(QUERY_AND_FRAGMENT_CHARACTERS));
    }

    @Test
    void asStringPercentEncodesNonUnreservedCharacters() {
        assertThat(fragment(".#.[.]. .").asString(), equalTo(".%23.%5B.%5D.%20."));
    }

    @Test
    void roundTripsAString() throws Exception {
        Fragment fragment = aFragment();
        assertThat(Fragment.parseFragment(fragment.asString(), Fragment.stringFragmentMaker()), equalTo(fragment));
    }

    @Test
    void valueGetsTheValueCorrectly() {
        String value = aString();
        assertThat(fragment(value).value(), equalTo(value));
    }
}
