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

import static net.sourceforge.urin.CharacterSetMembershipFunction.*;

public final class Scheme {

    private static final CharacterSetMembershipFunction TRAILING_CHARACTER_MEMBERSHIP_FUNCTION = or(
            ALPHA_LOWERCASE,
            ALPHA_UPPERCASE,
            DIGIT,
            singleMemberCharacterSet('+'),
            singleMemberCharacterSet('-'),
            singleMemberCharacterSet('.')
    );

    private final String value;

    public Scheme(final String name) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Scheme must contain at least one character");
        }
        if (!ALPHA.isMember(name.charAt(0))) {
            throw new IllegalArgumentException("First character must be " + ALPHA.describe() + " in scheme [" + name + "]");
        }
        for (int i = 1; i < name.length(); i++) {
            if (!TRAILING_CHARACTER_MEMBERSHIP_FUNCTION.isMember(name.charAt(i))) {
                throw new IllegalArgumentException("Character " + (i + 1) + " must be " + TRAILING_CHARACTER_MEMBERSHIP_FUNCTION.describe() + " in scheme [" + name + "]");
            }
        }
        value = name.toLowerCase();
    }

    public String asString() {
        return value;
    }

}
