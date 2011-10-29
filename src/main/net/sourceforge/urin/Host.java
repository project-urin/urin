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
        return new RegisteredName(registeredName);
    }

    public static Host ipV4Address(final Octet firstOctet, final Octet secondOctet, final Octet thirdOctet, final Octet fourthOctet) {
        return new IpV4Address(firstOctet, secondOctet, thirdOctet, fourthOctet);
    }

    public static Host ipV6Address(final Hexadectet firstHexadectet, final Hexadectet secondHexadectet, final Hexadectet thirdHexadectet, final Hexadectet fourthHexadectet, final Hexadectet fifthHexadectet, final Hexadectet sixthHexadectet, final Hexadectet seventhHexadectet, final Hexadectet eighthHexadectet) {
        return new IpV6Address(firstHexadectet, secondHexadectet, thirdHexadectet, fourthHexadectet, fifthHexadectet, sixthHexadectet, seventhHexadectet, eighthHexadectet);
    }

    public static Host ipV6Address(final Hexadectet firstHexadectet, final Hexadectet secondHexadectet, final Hexadectet thirdHexadectet, final Hexadectet fourthHexadectet, final Hexadectet fifthHexadectet, final Hexadectet sixthHexadectet, final Octet firstOctet, final Octet secondOctet, final Octet thirdOctet, final Octet fourthOctet) {
        return new Host() {
            @Override
            String asString() {
                return ipV6String(
                        asElidableAsStringable(firstHexadectet),
                        asElidableAsStringable(secondHexadectet),
                        asElidableAsStringable(thirdHexadectet),
                        asElidableAsStringable(fourthHexadectet),
                        asElidableAsStringable(fifthHexadectet),
                        asElidableAsStringable(sixthHexadectet),
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

    private static ElidableAsStringable asElidableAsStringable(final Hexadectet hexadectet) {
        return new ElidableAsStringable() {

            public String asString() {
                return hexadectet.asString();

            }

            public boolean isElidable() {
                return hexadectet.isElidable();
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

    private static interface ElidableAsStringable {
        String asString();

        boolean isElidable();
    }

    private static final class RegisteredName extends Host {
        private final String registeredName;

        RegisteredName(final String registeredName) {
            this.registeredName = registeredName;
        }

        @Override
        String asString() {
            return PERCENT_ENCODER.encode(registeredName.toLowerCase(ENGLISH)); // TODO determine what 'case insensitive means in the RFC w.r.t non-English characters
        }

        @Override
        public int hashCode() {
            return registeredName.hashCode();
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            RegisteredName that = (RegisteredName) o;
            return !(registeredName != null ? !registeredName.equals(that.registeredName) : that.registeredName != null);
        }

        @Override
        public String toString() {
            return "Host{" +
                    "registeredName='" + registeredName + '\'' +
                    '}';

        }
    }

    private static final class IpV4Address extends Host {
        private final Octet firstOctet;
        private final Octet secondOctet;
        private final Octet thirdOctet;
        private final Octet fourthOctet;

        IpV4Address(final Octet firstOctet, final Octet secondOctet, final Octet thirdOctet, final Octet fourthOctet) {
            this.firstOctet = firstOctet;
            this.secondOctet = secondOctet;
            this.thirdOctet = thirdOctet;
            this.fourthOctet = fourthOctet;
        }

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

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            IpV4Address that = (IpV4Address) o;

            return !(firstOctet != null ? !firstOctet.equals(that.firstOctet) : that.firstOctet != null)
                    && !(thirdOctet != null ? !thirdOctet.equals(that.thirdOctet) : that.thirdOctet != null)
                    && !(secondOctet != null ? !secondOctet.equals(that.secondOctet) : that.secondOctet != null)
                    && !(fourthOctet != null ? !fourthOctet.equals(that.fourthOctet) : that.fourthOctet != null);

        }

        @Override
        public int hashCode() {
            int result = firstOctet != null ? firstOctet.hashCode() : 0;
            result = 31 * result + (secondOctet != null ? secondOctet.hashCode() : 0);
            result = 31 * result + (thirdOctet != null ? thirdOctet.hashCode() : 0);
            result = 31 * result + (fourthOctet != null ? fourthOctet.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "Host{" +
                    "firstOctet=" + firstOctet +
                    ", secondOctet=" + secondOctet +
                    ", thirdOctet=" + thirdOctet +
                    ", fourthOctet=" + fourthOctet +
                    '}';
        }
    }

    private static final class IpV6Address extends Host {
        private final Hexadectet firstHexadectet;
        private final Hexadectet secondHexadectet;
        private final Hexadectet thirdHexadectet;
        private final Hexadectet fourthHexadectet;
        private final Hexadectet fifthHexadectet;
        private final Hexadectet sixthHexadectet;
        private final Hexadectet seventhHexadectet;
        private final Hexadectet eighthHexadectet;

        IpV6Address(final Hexadectet firstHexadectet, final Hexadectet secondHexadectet, final Hexadectet thirdHexadectet, final Hexadectet fourthHexadectet, final Hexadectet fifthHexadectet, final Hexadectet sixthHexadectet, final Hexadectet seventhHexadectet, final Hexadectet eighthHexadectet) {
            this.firstHexadectet = firstHexadectet;
            this.secondHexadectet = secondHexadectet;
            this.thirdHexadectet = thirdHexadectet;
            this.fourthHexadectet = fourthHexadectet;
            this.fifthHexadectet = fifthHexadectet;
            this.sixthHexadectet = sixthHexadectet;
            this.seventhHexadectet = seventhHexadectet;
            this.eighthHexadectet = eighthHexadectet;
        }

        @Override
        String asString() {
            return ipV6String(
                    asElidableAsStringable(firstHexadectet),
                    asElidableAsStringable(secondHexadectet),
                    asElidableAsStringable(thirdHexadectet),
                    asElidableAsStringable(fourthHexadectet),
                    asElidableAsStringable(fifthHexadectet),
                    asElidableAsStringable(sixthHexadectet),
                    asElidableAsStringable(seventhHexadectet),
                    asElidableAsStringable(eighthHexadectet)
            );
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            IpV6Address that = (IpV6Address) o;

            return !(firstHexadectet != null ? !firstHexadectet.equals(that.firstHexadectet) : that.firstHexadectet != null)
                    && !(secondHexadectet != null ? !secondHexadectet.equals(that.secondHexadectet) : that.secondHexadectet != null)
                    && !(thirdHexadectet != null ? !thirdHexadectet.equals(that.thirdHexadectet) : that.thirdHexadectet != null)
                    && !(fourthHexadectet != null ? !fourthHexadectet.equals(that.fourthHexadectet) : that.fourthHexadectet != null)
                    && !(fifthHexadectet != null ? !fifthHexadectet.equals(that.fifthHexadectet) : that.fifthHexadectet != null)
                    && !(sixthHexadectet != null ? !sixthHexadectet.equals(that.sixthHexadectet) : that.sixthHexadectet != null)
                    && !(seventhHexadectet != null ? !seventhHexadectet.equals(that.seventhHexadectet) : that.seventhHexadectet != null)
                    && !(eighthHexadectet != null ? !eighthHexadectet.equals(that.eighthHexadectet) : that.eighthHexadectet != null);
        }

        @Override
        public int hashCode() {
            int result = firstHexadectet != null ? firstHexadectet.hashCode() : 0;
            result = 31 * result + (secondHexadectet != null ? secondHexadectet.hashCode() : 0);
            result = 31 * result + (thirdHexadectet != null ? thirdHexadectet.hashCode() : 0);
            result = 31 * result + (fourthHexadectet != null ? fourthHexadectet.hashCode() : 0);
            result = 31 * result + (fifthHexadectet != null ? fifthHexadectet.hashCode() : 0);
            result = 31 * result + (sixthHexadectet != null ? sixthHexadectet.hashCode() : 0);
            result = 31 * result + (seventhHexadectet != null ? seventhHexadectet.hashCode() : 0);
            result = 31 * result + (eighthHexadectet != null ? eighthHexadectet.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "Host{" +
                    "firstHexadectet=" + firstHexadectet +
                    ", secondHexadectet=" + secondHexadectet +
                    ", thirdHexadectet=" + thirdHexadectet +
                    ", fourthHexadectet=" + fourthHexadectet +
                    ", fifthHexadectet=" + fifthHexadectet +
                    ", sixthHexadectet=" + sixthHexadectet +
                    ", seventhHexadectet=" + seventhHexadectet +
                    ", eighthHexadectet=" + eighthHexadectet +
                    '}';
        }
    }
}
