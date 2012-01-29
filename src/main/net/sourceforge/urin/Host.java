/*
 * Copyright 2012 Mark Slater
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

    public static final Host LOCAL_HOST = registeredName("localhost");

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
        return new IpV6AddressWithTrailingIpV4Address(firstHexadectet, secondHexadectet, thirdHexadectet, fourthHexadectet, fifthHexadectet, sixthHexadectet, firstOctet, secondOctet, thirdOctet, fourthOctet);
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
        return new IpVFutureAddress(version, address);
    }

    static Host parse(final String hostString) throws ParseException {
        if (IpV4Address.isValid(hostString)) {
            return IpV4Address.parse(hostString);
        } else {
            throw new ParseException("Not a valid host :" + hostString);
        }
    }

    private static interface ElidableAsStringable {
        String asString();

        boolean isElidable();
    }

    private static final class RegisteredName extends Host {
        private final String registeredName;

        RegisteredName(final String registeredName) {
            this.registeredName = registeredName.toLowerCase(ENGLISH); // TODO determine what 'case insensitive means in the RFC w.r.t non-English characters
        }

        @Override
        String asString() {
            return PERCENT_ENCODER.encode(registeredName);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            RegisteredName that = (RegisteredName) o;
            return registeredName.equals(that.registeredName);
        }

        @Override
        public int hashCode() {
            return registeredName.hashCode();
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
            if (firstOctet == null) {
                throw new NullPointerException("Cannot instantiate HierarchicalPart with null firstOctet");
            }
            this.firstOctet = firstOctet;
            if (secondOctet == null) {
                throw new NullPointerException("Cannot instantiate HierarchicalPart with null secondOctet");
            }
            this.secondOctet = secondOctet;
            if (thirdOctet == null) {
                throw new NullPointerException("Cannot instantiate HierarchicalPart with null thirdOctet");
            }
            this.thirdOctet = thirdOctet;
            if (fourthOctet == null) {
                throw new NullPointerException("Cannot instantiate HierarchicalPart with null fourthOctet");
            }
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
            return firstOctet.equals(that.firstOctet)
                    && thirdOctet.equals(that.thirdOctet)
                    && secondOctet.equals(that.secondOctet)
                    && fourthOctet.equals(that.fourthOctet);
        }

        @Override
        public int hashCode() {
            int result = firstOctet.hashCode();
            result = 31 * result + secondOctet.hashCode();
            result = 31 * result + thirdOctet.hashCode();
            result = 31 * result + fourthOctet.hashCode();
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

        static boolean isValid(final String hostString) {
            String[] split = hostString.split("\\.");
            return split.length == 4 && Octet.isValid(split[0]) && Octet.isValid(split[1]) && Octet.isValid(split[2]) && Octet.isValid(split[3]);
        }

        static IpV4Address parse(final String hostString) throws ParseException {
            if (!isValid(hostString)) {
                throw new ParseException("Invalid Host String [" + hostString + "]");
            } else {
                String[] split = hostString.split("\\.");
                return new IpV4Address(Octet.parse(split[0]), Octet.parse(split[1]), Octet.parse(split[2]), Octet.parse(split[3]));
            }
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
            if (firstHexadectet == null) {
                throw new NullPointerException("Cannot instantiate HierarchicalPart with null firstHexadectet");
            }
            this.firstHexadectet = firstHexadectet;
            if (secondHexadectet == null) {
                throw new NullPointerException("Cannot instantiate HierarchicalPart with null secondHexadectet");
            }
            this.secondHexadectet = secondHexadectet;
            if (thirdHexadectet == null) {
                throw new NullPointerException("Cannot instantiate HierarchicalPart with null thirdHexadectet");
            }
            this.thirdHexadectet = thirdHexadectet;
            if (fourthHexadectet == null) {
                throw new NullPointerException("Cannot instantiate HierarchicalPart with null fourthHexadectet");
            }
            this.fourthHexadectet = fourthHexadectet;
            if (fifthHexadectet == null) {
                throw new NullPointerException("Cannot instantiate HierarchicalPart with null fifthHexadectet");
            }
            this.fifthHexadectet = fifthHexadectet;
            if (sixthHexadectet == null) {
                throw new NullPointerException("Cannot instantiate HierarchicalPart with null sixthHexadectet");
            }
            this.sixthHexadectet = sixthHexadectet;
            if (seventhHexadectet == null) {
                throw new NullPointerException("Cannot instantiate HierarchicalPart with null seventhHexadectet");
            }
            this.seventhHexadectet = seventhHexadectet;
            if (eighthHexadectet == null) {
                throw new NullPointerException("Cannot instantiate HierarchicalPart with null eighthHexadectet");
            }
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
            return firstHexadectet.equals(that.firstHexadectet)
                    && secondHexadectet.equals(that.secondHexadectet)
                    && thirdHexadectet.equals(that.thirdHexadectet)
                    && fourthHexadectet.equals(that.fourthHexadectet)
                    && fifthHexadectet.equals(that.fifthHexadectet)
                    && sixthHexadectet.equals(that.sixthHexadectet)
                    && seventhHexadectet.equals(that.seventhHexadectet)
                    && eighthHexadectet.equals(that.eighthHexadectet);
        }

        @Override
        public int hashCode() {
            int result = firstHexadectet.hashCode();
            result = 31 * result + secondHexadectet.hashCode();
            result = 31 * result + thirdHexadectet.hashCode();
            result = 31 * result + fourthHexadectet.hashCode();
            result = 31 * result + fifthHexadectet.hashCode();
            result = 31 * result + sixthHexadectet.hashCode();
            result = 31 * result + seventhHexadectet.hashCode();
            result = 31 * result + eighthHexadectet.hashCode();
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

    private static class IpV6AddressWithTrailingIpV4Address extends Host {
        private final Hexadectet firstHexadectet;
        private final Hexadectet secondHexadectet;
        private final Hexadectet thirdHexadectet;
        private final Hexadectet fourthHexadectet;
        private final Hexadectet fifthHexadectet;
        private final Hexadectet sixthHexadectet;
        private final Octet firstOctet;
        private final Octet secondOctet;
        private final Octet thirdOctet;
        private final Octet fourthOctet;

        public IpV6AddressWithTrailingIpV4Address(final Hexadectet firstHexadectet, final Hexadectet secondHexadectet, final Hexadectet thirdHexadectet, final Hexadectet fourthHexadectet, final Hexadectet fifthHexadectet, final Hexadectet sixthHexadectet, final Octet firstOctet, final Octet secondOctet, final Octet thirdOctet, final Octet fourthOctet) {
            if (firstHexadectet == null) {
                throw new NullPointerException("Cannot instantiate HierarchicalPart with null firstHexadectet");
            }
            this.firstHexadectet = firstHexadectet;
            if (secondHexadectet == null) {
                throw new NullPointerException("Cannot instantiate HierarchicalPart with null secondHexadectet");
            }
            this.secondHexadectet = secondHexadectet;
            if (thirdHexadectet == null) {
                throw new NullPointerException("Cannot instantiate HierarchicalPart with null thirdHexadectet");
            }
            this.thirdHexadectet = thirdHexadectet;
            if (fourthHexadectet == null) {
                throw new NullPointerException("Cannot instantiate HierarchicalPart with null fourthHexadectet");
            }
            this.fourthHexadectet = fourthHexadectet;
            if (fifthHexadectet == null) {
                throw new NullPointerException("Cannot instantiate HierarchicalPart with null fifthHexadectet");
            }
            this.fifthHexadectet = fifthHexadectet;
            if (sixthHexadectet == null) {
                throw new NullPointerException("Cannot instantiate HierarchicalPart with null sixthHexadectet");
            }
            this.sixthHexadectet = sixthHexadectet;
            if (firstOctet == null) {
                throw new NullPointerException("Cannot instantiate HierarchicalPart with null firstOctet");
            }
            this.firstOctet = firstOctet;
            if (secondOctet == null) {
                throw new NullPointerException("Cannot instantiate HierarchicalPart with null secondOctet");
            }
            this.secondOctet = secondOctet;
            if (thirdOctet == null) {
                throw new NullPointerException("Cannot instantiate HierarchicalPart with null thirdOctet");
            }
            this.thirdOctet = thirdOctet;
            if (fourthOctet == null) {
                throw new NullPointerException("Cannot instantiate HierarchicalPart with null fourthOctet");
            }
            this.fourthOctet = fourthOctet;
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

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            IpV6AddressWithTrailingIpV4Address that = (IpV6AddressWithTrailingIpV4Address) o;
            return firstHexadectet.equals(that.firstHexadectet)
                    && secondHexadectet.equals(that.secondHexadectet)
                    && thirdHexadectet.equals(that.thirdHexadectet)
                    && fourthHexadectet.equals(that.fourthHexadectet)
                    && fifthHexadectet.equals(that.fifthHexadectet)
                    && sixthHexadectet.equals(that.sixthHexadectet)
                    && firstOctet.equals(that.firstOctet)
                    && secondOctet.equals(that.secondOctet)
                    && thirdOctet.equals(that.thirdOctet)
                    && fourthOctet.equals(that.fourthOctet);
        }

        @Override
        public int hashCode() {
            int result = firstHexadectet.hashCode();
            result = 31 * result + secondHexadectet.hashCode();
            result = 31 * result + thirdHexadectet.hashCode();
            result = 31 * result + fourthHexadectet.hashCode();
            result = 31 * result + fifthHexadectet.hashCode();
            result = 31 * result + sixthHexadectet.hashCode();
            result = 31 * result + firstOctet.hashCode();
            result = 31 * result + secondOctet.hashCode();
            result = 31 * result + thirdOctet.hashCode();
            result = 31 * result + fourthOctet.hashCode();
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
                    ", firstOctet=" + firstOctet +
                    ", secondOctet=" + secondOctet +
                    ", thirdOctet=" + thirdOctet +
                    ", fourthOctet=" + fourthOctet +
                    '}';
        }
    }

    private static final class IpVFutureAddress extends Host {
        private final String version;
        private final String address;

        IpVFutureAddress(final String version, final String address) {
            this.version = version;
            this.address = address.toLowerCase(ENGLISH);
        }

        @Override
        String asString() {
            return new StringBuilder()
                    .append("[v")
                    .append(version)
                    .append('.')
                    .append(address)
                    .append(']')
                    .toString();
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            IpVFutureAddress that = (IpVFutureAddress) o;

            return address.equals(that.address)
                    && version.equals(that.version);

        }

        @Override
        public int hashCode() {
            int result = version.hashCode();
            result = 31 * result + address.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "Host{" +
                    "version='" + version + '\'' +
                    ", address='" + address + '\'' +
                    '}';
        }
    }
}
