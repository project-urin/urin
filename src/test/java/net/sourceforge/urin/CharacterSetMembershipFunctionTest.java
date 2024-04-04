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

import java.util.HashSet;
import java.util.Set;

import static net.sourceforge.urin.MoreRandomStringUtils.aString;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CharacterSetMembershipFunctionTest {
    @Test
    void areMembersAppliesToAllCharactersInAString() {
        String aString = aString();
        assertTrue(testCharacterSetMembershipFunction(aString).areMembers(aString));
        assertFalse(testCharacterSetMembershipFunction("").areMembers(aString));
    }

    @Test
    void anEmptyStringIsAlwaysAMember() {
        assertTrue(testCharacterSetMembershipFunction("").areMembers(""));
    }

    private static CharacterSetMembershipFunction testCharacterSetMembershipFunction(final String members) {
        final Set<Character> membersSet = new HashSet<>();
        for (char character : members.toCharArray()) {
            membersSet.add(character);
        }
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
