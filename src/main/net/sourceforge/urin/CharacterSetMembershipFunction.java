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

import java.util.Iterator;

import static java.util.Arrays.asList;

abstract class CharacterSetMembershipFunction {
    static final CharacterSetMembershipFunction ALPHA_UPPERCASE = new CharacterSetMembershipFunction() {
        @Override
        public boolean isMember(final char character) {
            return character >= 'A' && character <= 'Z';
        }

        @Override
        String describe() {
            return "A-Z";
        }
    };

    static final CharacterSetMembershipFunction ALPHA_LOWERCASE = new CharacterSetMembershipFunction() {
        @Override
        public boolean isMember(final char character) {
            return character >= 'a' && character <= 'z';
        }

        @Override
        String describe() {
            return "a-z";
        }
    };

    static final CharacterSetMembershipFunction ALPHA = or(ALPHA_LOWERCASE, ALPHA_UPPERCASE);

    static final CharacterSetMembershipFunction DIGIT = new CharacterSetMembershipFunction() {
        @Override
        public boolean isMember(final char character) {
            return character >= '0' && character <= '9';
        }

        @Override
        String describe() {
            return "0-9";
        }
    };

    static final CharacterSetMembershipFunction HEX_DIGIT = or(
            DIGIT,
            new CharacterSetMembershipFunction() {
                @Override
                boolean isMember(final char character) {
                    return character >= 'A' && character <= 'F';
                }

                @Override
                String describe() {
                    return "A-F";
                }
            }
    );

    static final CharacterSetMembershipFunction UNRESERVED = or(
            ALPHA_LOWERCASE,
            ALPHA_UPPERCASE,
            DIGIT,
            singleMemberCharacterSet('-'),
            singleMemberCharacterSet('.'),
            singleMemberCharacterSet('_'),
            singleMemberCharacterSet('~')
    );

    static final CharacterSetMembershipFunction SUB_DELIMITERS = or(
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
            singleMemberCharacterSet('=')
    );

    static final CharacterSetMembershipFunction P_CHAR = or(
            UNRESERVED,
            SUB_DELIMITERS,
            singleMemberCharacterSet(':'),
            singleMemberCharacterSet('@')
    );
    public static final CharacterSetMembershipFunction QUERY_AND_FRAGMENT_NON_PERCENT_ENCODED_CHARACTERS = or(
            P_CHAR,
            singleMemberCharacterSet('/'),
            singleMemberCharacterSet('?')
    );

    static CharacterSetMembershipFunction singleMemberCharacterSet(final char member) {
        return new CharacterSetMembershipFunction() {
            @Override
            public boolean isMember(final char character) {
                return character == member;
            }

            @Override
            String describe() {
                return Character.toString(member);
            }
        };
    }

    static CharacterSetMembershipFunction or(final CharacterSetMembershipFunction head, final CharacterSetMembershipFunction... tail) {
        return new CharacterSetMembershipFunction() {
            @Override
            boolean isMember(final char character) {
                boolean result = head.isMember(character);
                Iterator<CharacterSetMembershipFunction> characterSetMembershipFunctionIterator = asList(tail).iterator();
                while (characterSetMembershipFunctionIterator.hasNext() && !result) {
                    result = characterSetMembershipFunctionIterator.next().isMember(character);
                }
                return result;
            }

            @Override
            String describe() {
                StringBuilder result = new StringBuilder(head.describe());
                Iterator<CharacterSetMembershipFunction> characterSetMembershipFunctionIterator = asList(tail).iterator();
                while (characterSetMembershipFunctionIterator.hasNext()) {
                    String description = characterSetMembershipFunctionIterator.next().describe();
                    result.append(", ");
                    if (!characterSetMembershipFunctionIterator.hasNext()) {
                        result.append("or ");
                    }
                    result.append(description);
                }
                return result.toString();
            }
        };
    }

    CharacterSetMembershipFunction() {
    }

    static void verify(final CharacterSetMembershipFunction characterSetMembershipFunction, final String value, final String parameterName) {
        verify(characterSetMembershipFunction, value, parameterName, 0);
    }

    static void verify(final CharacterSetMembershipFunction characterSetMembershipFunction, final String value, final String parameterName, final int startIndex) {
        verify(characterSetMembershipFunction, value, parameterName, startIndex, value.length());
    }

    static void verify(final CharacterSetMembershipFunction characterSetMembershipFunction, final String value, final String parameterName, final int startIndex, final int endIndex) {
        for (int i = startIndex; i < endIndex; i++) {
            if (!characterSetMembershipFunction.isMember(value.charAt(i))) {
                throw new IllegalArgumentException("Character " + (i + 1) + " must be " + characterSetMembershipFunction.describe() + " in " + parameterName + " [" + value + "]");
            }
        }
    }

    abstract boolean isMember(char character);

    abstract String describe();
}
