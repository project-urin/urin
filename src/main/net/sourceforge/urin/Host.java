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

import static java.util.Locale.ENGLISH;
import static net.sourceforge.urin.CharacterSetMembershipFunction.*;

public abstract class Host {

    private static final PercentEncoder PERCENT_ENCODER = new PercentEncoder(or(
            UNRESERVED,
            SUB_DELIMITERS
    ));

    private static final CharacterSetMembershipFunction ADDRESS_CHARACTER_SET_MEMBERSHIP_FUNCTION = or(
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
            singleMemberCharacterSet(':')
    );

    private Host() {
    }

    abstract String asString();

    public static Host registeredName(final String registeredName) {
        return new Host() {
            @Override
            String asString() {
                return PERCENT_ENCODER.encode(registeredName.toLowerCase(ENGLISH)); // TODO determine what 'case insensitive means in the RFC w.r.t non-English characters
            }
        };
    }

    public static Host ipV4Address(final Octet firstOctet, final Octet secondOctet, final Octet thirdOctet, final Octet fourthOctet) {
        return new Host() {
            @Override
            String asString() {
                return new StringBuilder(firstOctet.asString())
                        .append('.')
                        .append(secondOctet.asString())
                        .append('.')
                        .append(thirdOctet.asString())
                        .append('.')
                        .append(fourthOctet.asString())
                        .toString();
            }
        };
    }

    public static Host ipV6Address(final Hexadectet firstHexadectet, final Hexadectet secondHexadectet, final Hexadectet thirdHexadectet, final Hexadectet fourthHexadectet, final Hexadectet fifthHexadectet, final Hexadectet sixthHexadectet, final Hexadectet seventhHexadectet, final Hexadectet eighthHexadectet) {
        return new Host() {
            @Override
            String asString() {
                return ipV6String(
                        firstHexadectet,
                        secondHexadectet,
                        thirdHexadectet,
                        fourthHexadectet,
                        fifthHexadectet,
                        sixthHexadectet,
                        seventhHexadectet,
                        eighthHexadectet
                );
            }
        };
    }

    public static Host ipV6Address(final Hexadectet firstHexadectet, final Hexadectet secondHexadectet, final Hexadectet thirdHexadectet, final Hexadectet fourthHexadectet, final Hexadectet fifthHexadectet, final Hexadectet sixthHexadectet, final Octet firstOctet, final Octet secondOctet, final Octet thirdOctet, final Octet fourthOctet) {
        return new Host() {
            @Override
            String asString() {
                return ipV6String(
                        firstHexadectet,
                        secondHexadectet,
                        thirdHexadectet,
                        fourthHexadectet,
                        fifthHexadectet,
                        sixthHexadectet,
                        new ElidableAsStringable() {
                            public String asString() {
                                return ipV4Address(firstOctet, secondOctet, thirdOctet, fourthOctet).asString();
                            }

                            public boolean isElidable() {
                                return false;
                            }
                        }
                );
            }
        };
    }

    private static String ipV6String(ElidableAsStringable... elidableAsStringables) {
        int[] streakLength = new int[elidableAsStringables.length];
        for (int i = 0; i < elidableAsStringables.length; i++) {
            streakLength[i] = elidableAsStringables[i].isElidable()
                    ? (i == 0 ? 0 : streakLength[i - 1]) + 1
                    : 0;
        }
        int maximumStreakStartIndex = 0;
        int maximumStreakLength = 0;
        for (int i = 0; i < elidableAsStringables.length; i++) {
            if (streakLength[i] > maximumStreakLength) {
                maximumStreakLength = streakLength[i];
                maximumStreakStartIndex = i - (maximumStreakLength - 1);
            }
        }

        StringBuilder result = new StringBuilder()
                .append('[');
        for (int i = 0; i < elidableAsStringables.length; i++) {
            if (maximumStreakLength < 2 || !(i >= maximumStreakStartIndex && i < (maximumStreakStartIndex + maximumStreakLength))) {
                if (i > 0) {
                    result.append(':');
                }
                result.append(elidableAsStringables[i].asString());
            } else {
                if (i == maximumStreakStartIndex) {
                    result.append(':');
                }
            }
        }
        return result
                .append(']')
                .toString();
    }

    public static Host ipVFutureAddress(final String version, final String address) {
        if (version.isEmpty()) {
            throw new IllegalArgumentException("Version must contain at least one character");
        }
        verify(HEX_DIGIT, version, "version");
        if (address.isEmpty()) {
            throw new IllegalArgumentException("Address must contain at least one character");
        }
        verify(ADDRESS_CHARACTER_SET_MEMBERSHIP_FUNCTION, address, "address");
        return new Host() {
            @Override
            String asString() {
                return new StringBuilder()
                        .append("[v")
                        .append(version)
                        .append('.')
                        .append(address.toLowerCase(ENGLISH))
                        .append(']')
                        .toString();
            }
        };
    }

}
