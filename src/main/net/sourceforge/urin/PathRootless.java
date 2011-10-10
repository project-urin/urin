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

public class PathRootless {
    private static final CharacterSetMembershipFunction NON_PERCENT_ENCODED_CHARACTER_SET = or(
            ALPHA_LOWERCASE,
            ALPHA_UPPERCASE,
            DIGIT,
            singleMemberCharacterSet('-'),
            singleMemberCharacterSet('.'),
            singleMemberCharacterSet('_'),
            singleMemberCharacterSet('~'),

            singleMemberCharacterSet('!'),
            singleMemberCharacterSet('$'),
            singleMemberCharacterSet('&'),
            singleMemberCharacterSet('\''),
            singleMemberCharacterSet('('),
            singleMemberCharacterSet(')'),
            singleMemberCharacterSet('*'),
            singleMemberCharacterSet('+'),
            singleMemberCharacterSet(','),
            singleMemberCharacterSet(';'),
            singleMemberCharacterSet('='),


            singleMemberCharacterSet(':'),
            singleMemberCharacterSet('@')
    );

    private final String firstSegment;
    private final String[] segments;

    public PathRootless(final String firstSegment, final String... segments) {
        this.firstSegment = firstSegment;
        this.segments = segments.clone();
    }

    public String asString() {
        StringBuilder result = new StringBuilder(percentEncode(firstSegment));
        for (String pathSegment : segments) {
            result.append("/").append(pathSegment);
        }
        return result.toString();
    }

    private static String percentEncode(final String segment) {
        StringBuilder result = new StringBuilder();
        for (char character : segment.toCharArray()) {
            if (NON_PERCENT_ENCODED_CHARACTER_SET.isMember(character)) {
                result.append(character);
            } else {
                result.append(percentEncode(character));
            }
        }
        return result.toString();
    }

    private static String percentEncode(final Character character) {
        return "%" + Integer.toHexString(character).toUpperCase();
    }
}
