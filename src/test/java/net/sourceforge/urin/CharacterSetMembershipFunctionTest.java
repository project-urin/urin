/*
 * Copyright 2017 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.urin;

import org.junit.Test;

import java.util.HashSet;

import static net.sourceforge.urin.MoreRandomStringUtils.aString;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CharacterSetMembershipFunctionTest {
    @Test
    public void areMembersAppliesToAllCharactersInAString() throws Exception {
        String aString = aString();
        assertTrue(testCharacterSetMembershipFunction(aString).areMembers(aString));
        assertFalse(testCharacterSetMembershipFunction("").areMembers(aString));
    }

    @Test
    public void anEmptyStringIsAlwaysAMember() throws Exception {
        assertTrue(testCharacterSetMembershipFunction("").areMembers(""));
    }

    private static CharacterSetMembershipFunction testCharacterSetMembershipFunction(final String members) {
        final HashSet<Character> membersSet = new HashSet<Character>() {{
            for (char character : members.toCharArray()) {
                add(character);
            }
        }};
        return new CharacterSetMembershipFunction() {

            @Override
            boolean isMember(final char character) {
                return membersSet.contains(character);
            }

            @Override
            String describe() {
                return "the characters [" + membersSet + "]";
            }
        };
    }
}
