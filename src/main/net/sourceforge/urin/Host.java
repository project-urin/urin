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

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Arrays.asList;
import static java.util.Locale.ENGLISH;
import static net.sourceforge.urin.CharacterSetMembershipFunction.*;
import static net.sourceforge.urin.ExceptionFactory.ILLEGAL_ARGUMENT_EXCEPTION_EXCEPTION_FACTORY;
import static net.sourceforge.urin.ExceptionFactory.PARSE_EXCEPTION_EXCEPTION_FACTORY;
import static net.sourceforge.urin.Hexadectet.ZERO;
import static net.sourceforge.urin.Hexadectet.hexadectet;
import static net.sourceforge.urin.Octet.octet;

/**
 * A host component of a URI.
 * <p/>
 * RFC 3986 specifies four forms of host - registered name, IP version 4 address, IP version 6 address, and a future IP version address.
 * Note that as pointed out in the RFC, there is an overlap between what is considered a valid IP version 4 address, and what is considered
 * a valid registered name - for example, {@code Host.registeredName("127.0.0.1")} renders identically in a URI to
 * {@code ipV4Address(octet(127), octet(0), octet(0), octet(1))}.  In keeping with the RFC, such a registered name is considered
 * to be an equivalent IP version 4 address.
 * <p/>
 * Immutable and threadsafe.
 *
 * @see <a href="http://tools.ietf.org/html/rfc3986#section-3.2.2">RFC 3986 - Host</a>
 */
public abstract class Host {

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

    /**
     * The registered name "localhost".
     */
    public static final Host LOCAL_HOST = registeredName("localhost");

    /**
     * The loopback address in IP v4 format, in other words 127.0.0.1
     */
    public static final Host LOOPBACK_ADDRESS_IP_V4 = ipV4Address(octet(127), octet(0), octet(0), octet(1));

    /**
     * The loopback address in IP v6 format, in other words ::1
     */
    public static final Host LOOPBACK_ADDRESS_IP_V6 = ipV6Address(ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, hexadectet(1));

    /**
     * Factory method for creating registered name type {@code Host}s.
     *
     * @param registeredName any {@code String} to represent as a {@code Host}.
     * @return a {@code Host} representing the given {@code String} as a registered name.
     */
    public static Host registeredName(final String registeredName) {
        if (IpV4Address.isValid(registeredName)) {
            try {
                return IpV4Address.parse(registeredName);
            } catch (ParseException e) {
                throw new IllegalArgumentException("Unexpectedly unable to convert registered name [" + registeredName + "] to an equivalent ipV4Address", e);
            }
        }
        return new RegisteredName(registeredName);
    }

    /**
     * Factory method for creating IP version 4 type {@code Host}s.
     * IP version 4 {@code Host}s are made up of four {@code Octet}s.
     *
     * @return a {@code Host} representing the given {@code Octet}s as an IP version 4 address.
     */
    public static Host ipV4Address(final Octet firstOctet, final Octet secondOctet, final Octet thirdOctet, final Octet fourthOctet) {
        return new IpV4Address(firstOctet, secondOctet, thirdOctet, fourthOctet);
    }

    /**
     * Factory method for creating IP version 6 type {@code Host}s with all parts specified as {@code Hexadectet}s.
     *
     * @return a {@code Host} representing the given {@code Hexadectet}s as an IP version 6 address.
     */
    public static Host ipV6Address(final Hexadectet firstHexadectet, final Hexadectet secondHexadectet, final Hexadectet thirdHexadectet, final Hexadectet fourthHexadectet, final Hexadectet fifthHexadectet, final Hexadectet sixthHexadectet, final Hexadectet seventhHexadectet, final Hexadectet eighthHexadectet) {
        return new IpV6Address(firstHexadectet, secondHexadectet, thirdHexadectet, fourthHexadectet, fifthHexadectet, sixthHexadectet, seventhHexadectet, eighthHexadectet);
    }

    /**
     * Factory method for creating IP version 6 type {@code Host}s with the least significant 32 bits specified as in IP version 4 address format.
     *
     * @return a {@code Host} representing the given {@code Hexadectet}s and {@code Octet}s as an IP version 6 address.
     */
    public static Host ipV6Address(final Hexadectet firstHexadectet, final Hexadectet secondHexadectet, final Hexadectet thirdHexadectet, final Hexadectet fourthHexadectet, final Hexadectet fifthHexadectet, final Hexadectet sixthHexadectet, final Octet firstOctet, final Octet secondOctet, final Octet thirdOctet, final Octet fourthOctet) {
        return new IpV6AddressWithTrailingIpV4Address(firstHexadectet, secondHexadectet, thirdHexadectet, fourthHexadectet, fifthHexadectet, sixthHexadectet, firstOctet, secondOctet, thirdOctet, fourthOctet);
    }

    /**
     * Factory method for creating IP version future type {@code Host}s.
     *
     * @param version a {@code String} consisting of at least one character, made up solely of hexadecimal digits, namely 0-9 and A-F.
     * @param address a {@code String} consisting of at least one character, made up solely of characters from the Latin alphabet, the digits, and any of '-', '.', '_' , '~' , '!' , '$' , '&' , ''' , '(' , ')' , '*' , '+' , ',' , ';' , '=' , ':'.
     * @return a {@code Host} representing the given {@code Hexadectet}s and {@code Octet}s as an IP version 6 address.
     * @throws IllegalArgumentException if the given version or address are empty of contain characters outside the valid set.
     */
    public static Host ipVFutureAddress(final String version, final String address) {
        return IpVFutureAddress.makeIpVFutureAddress(version, address, ILLEGAL_ARGUMENT_EXCEPTION_EXCEPTION_FACTORY);
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

    static Host parse(final String hostString) throws ParseException {
        if (IpV4Address.isValid(hostString)) {
            return IpV4Address.parse(hostString);
        } else if (IpV6Address.isValid(hostString)) {
            return IpV6Address.parse(hostString);
        } else if (IpV6AddressWithTrailingIpV4Address.isValid(hostString)) {
            return IpV6AddressWithTrailingIpV4Address.parse(hostString);
        } else if (IpVFutureAddress.isValid(hostString)) {
            return IpVFutureAddress.parse(hostString);
        } else if (RegisteredName.isValid(hostString)) {
            return RegisteredName.parse(hostString);
        } else {
            throw new ParseException("Not a valid host :" + hostString);
        }
    }

    private static interface ElidableAsStringable {
        String asString();

        boolean isElidable();
    }

    private static final class RegisteredName extends Host {
        private static final CharacterSetMembershipFunction REGISTERED_NAME_CHARACTER_SET = or(
                UNRESERVED,
                SUB_DELIMITERS
        );
        private static final PercentEncoder PERCENT_ENCODER = new PercentEncoder(REGISTERED_NAME_CHARACTER_SET);
        private static final PercentDecoder PERCENT_DECODER = new PercentDecoder(REGISTERED_NAME_CHARACTER_SET);
        private final String registeredName;

        RegisteredName(final String registeredName) { // TODO determine whether empty String is a valid registered name
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

        static RegisteredName parse(final String hostString) throws ParseException {
            return new RegisteredName(PERCENT_DECODER.decode(hostString));
        }

        public static boolean isValid(final String hostString) {
            return PERCENT_DECODER.isMember(hostString);
        }
    }

    private static final class IpV4Address extends Host {
        private final Octet firstOctet;
        private final Octet secondOctet;
        private final Octet thirdOctet;
        private final Octet fourthOctet;

        IpV4Address(final Octet firstOctet, final Octet secondOctet, final Octet thirdOctet, final Octet fourthOctet) {
            if (firstOctet == null) {
                throw new NullPointerException("Cannot instantiate Host with null firstOctet");
            }
            this.firstOctet = firstOctet;
            if (secondOctet == null) {
                throw new NullPointerException("Cannot instantiate Host with null secondOctet");
            }
            this.secondOctet = secondOctet;
            if (thirdOctet == null) {
                throw new NullPointerException("Cannot instantiate Host with null thirdOctet");
            }
            this.thirdOctet = thirdOctet;
            if (fourthOctet == null) {
                throw new NullPointerException("Cannot instantiate Host with null fourthOctet");
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
                throw new NullPointerException("Cannot instantiate Host with null firstHexadectet");
            }
            this.firstHexadectet = firstHexadectet;
            if (secondHexadectet == null) {
                throw new NullPointerException("Cannot instantiate Host with null secondHexadectet");
            }
            this.secondHexadectet = secondHexadectet;
            if (thirdHexadectet == null) {
                throw new NullPointerException("Cannot instantiate Host with null thirdHexadectet");
            }
            this.thirdHexadectet = thirdHexadectet;
            if (fourthHexadectet == null) {
                throw new NullPointerException("Cannot instantiate Host with null fourthHexadectet");
            }
            this.fourthHexadectet = fourthHexadectet;
            if (fifthHexadectet == null) {
                throw new NullPointerException("Cannot instantiate Host with null fifthHexadectet");
            }
            this.fifthHexadectet = fifthHexadectet;
            if (sixthHexadectet == null) {
                throw new NullPointerException("Cannot instantiate Host with null sixthHexadectet");
            }
            this.sixthHexadectet = sixthHexadectet;
            if (seventhHexadectet == null) {
                throw new NullPointerException("Cannot instantiate Host with null seventhHexadectet");
            }
            this.seventhHexadectet = seventhHexadectet;
            if (eighthHexadectet == null) {
                throw new NullPointerException("Cannot instantiate Host with null eighthHexadectet");
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

        static boolean isValid(final String hostString) {
            if (!(hostString.startsWith("[") && hostString.endsWith("]"))) {
                return false;
            } else {
                String[] elidableHexadectetStrings = hostString.substring(1, hostString.length() - 1).split(":");
                int elidedSectionCount = 0;
                for (String elidableHexadectetString : elidableHexadectetStrings) {
                    if (elidableHexadectetString.isEmpty()) {
                        elidedSectionCount++;
                    }
                }
                if (elidedSectionCount > 1) {
                    return false;
                }
                if (elidedSectionCount == 0 && elidableHexadectetStrings.length != 8) {
                    return false;
                }
                if (elidedSectionCount == 1 && elidableHexadectetStrings.length > 8) {
                    return false;
                }
                for (String elidableHexadectetString : elidableHexadectetStrings) {
                    if (!elidableHexadectetString.isEmpty() && !Hexadectet.isValid(elidableHexadectetString)) {
                        return false;
                    }
                }
                return true;
            }
        }

        static IpV6Address parse(final String hostString) throws ParseException {
            String[] elidableHexadectetStrings = hostString.substring(1, hostString.length() - 1).split(":");
            Hexadectet[] hexadectets = new Hexadectet[8];
            int i = 0;
            boolean gotElidedPart = false;
            for (String hexadectetString : elidableHexadectetStrings) {
                if (hexadectetString.isEmpty()) {
                    if (gotElidedPart) {
                        throw new ParseException("Invalid IP v6 String [" + hostString + "]: more than one elided part");
                    }
                    gotElidedPart = true;
                    final int elidedTotal = 8 - elidableHexadectetStrings.length;
                    for (int elided = 0; elided <= elidedTotal; elided++) {
                        hexadectets[i] = ZERO;
                        i++;
                    }
                } else {
                    hexadectets[i] = Hexadectet.parse(hexadectetString);
                    i++;
                }
            }
            return new IpV6Address(
                    hexadectets[0],
                    hexadectets[1],
                    hexadectets[2],
                    hexadectets[3],
                    hexadectets[4],
                    hexadectets[5],
                    hexadectets[6],
                    hexadectets[7]
            );
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
                throw new NullPointerException("Cannot instantiate Host with null firstHexadectet");
            }
            this.firstHexadectet = firstHexadectet;
            if (secondHexadectet == null) {
                throw new NullPointerException("Cannot instantiate Host with null secondHexadectet");
            }
            this.secondHexadectet = secondHexadectet;
            if (thirdHexadectet == null) {
                throw new NullPointerException("Cannot instantiate Host with null thirdHexadectet");
            }
            this.thirdHexadectet = thirdHexadectet;
            if (fourthHexadectet == null) {
                throw new NullPointerException("Cannot instantiate Host with null fourthHexadectet");
            }
            this.fourthHexadectet = fourthHexadectet;
            if (fifthHexadectet == null) {
                throw new NullPointerException("Cannot instantiate Host with null fifthHexadectet");
            }
            this.fifthHexadectet = fifthHexadectet;
            if (sixthHexadectet == null) {
                throw new NullPointerException("Cannot instantiate Host with null sixthHexadectet");
            }
            this.sixthHexadectet = sixthHexadectet;
            if (firstOctet == null) {
                throw new NullPointerException("Cannot instantiate Host with null firstOctet");
            }
            this.firstOctet = firstOctet;
            if (secondOctet == null) {
                throw new NullPointerException("Cannot instantiate Host with null secondOctet");
            }
            this.secondOctet = secondOctet;
            if (thirdOctet == null) {
                throw new NullPointerException("Cannot instantiate Host with null thirdOctet");
            }
            this.thirdOctet = thirdOctet;
            if (fourthOctet == null) {
                throw new NullPointerException("Cannot instantiate Host with null fourthOctet");
            }
            this.fourthOctet = fourthOctet;
        }

        static boolean isValid(final String hostString) {
            if (!(hostString.startsWith("[") && hostString.endsWith("]"))) {
                return false;
            } else {
                String[] elidableHexadectetStrings = hostString.substring(1, hostString.length() - 1).split(":");
                int elidedSectionCount = 0;
                for (String elidableHexadectetString : elidableHexadectetStrings) {
                    if (elidableHexadectetString.isEmpty()) {
                        elidedSectionCount++;
                    }
                }
                if (elidedSectionCount > 1) {
                    return false;
                }
                if (elidedSectionCount == 0 && elidableHexadectetStrings.length != 7) {
                    return false;
                }
                if (elidedSectionCount == 1 && elidableHexadectetStrings.length > 7) {
                    return false;
                }
                Iterator<String> elidableHexadectetStringsIterator = asList(elidableHexadectetStrings).iterator();
                while (elidableHexadectetStringsIterator.hasNext()) {
                    String elidableHexadectetString = elidableHexadectetStringsIterator.next();
                    if (!elidableHexadectetStringsIterator.hasNext()) {
                        return IpV4Address.isValid(elidableHexadectetString);
                    } else if (!elidableHexadectetString.isEmpty() && !Hexadectet.isValid(elidableHexadectetString)) {
                        return false;
                    }
                }
                return true;
            }
        }

        static IpV6AddressWithTrailingIpV4Address parse(final String hostString) throws ParseException {
            String[] elidableHexadectetStrings = hostString.substring(1, hostString.length() - 1).split(":");
            Hexadectet[] hexadectets = new Hexadectet[6];
            int i = 0;
            boolean gotElidedPart = false;
            IpV4Address ipV4AddressPart = null;
            Iterator<String> elidableHexadectetStringsIterator = asList(elidableHexadectetStrings).iterator();
            while (elidableHexadectetStringsIterator.hasNext()) {
                String hexadectetString = elidableHexadectetStringsIterator.next();
                if (!elidableHexadectetStringsIterator.hasNext()) {
                    ipV4AddressPart = IpV4Address.parse(hexadectetString);
                } else if (hexadectetString.isEmpty()) {
                    if (gotElidedPart) {
                        throw new ParseException("Invalid IP v6 String [" + hostString + "]: more than one elided part");
                    }
                    gotElidedPart = true;
                    final int elidedTotal = 7 - elidableHexadectetStrings.length;
                    for (int elided = 0; elided <= elidedTotal; elided++) {
                        hexadectets[i] = ZERO;
                        i++;
                    }
                } else {
                    hexadectets[i] = Hexadectet.parse(hexadectetString);
                    i++;
                }
            }
            if (ipV4AddressPart == null) {
                throw new ParseException("Invalid IP v6 String [" + hostString + "]: no trailing IP v4 address.");
            }
            return new IpV6AddressWithTrailingIpV4Address(
                    hexadectets[0],
                    hexadectets[1],
                    hexadectets[2],
                    hexadectets[3],
                    hexadectets[4],
                    hexadectets[5],
                    ipV4AddressPart.firstOctet,
                    ipV4AddressPart.secondOctet,
                    ipV4AddressPart.thirdOctet,
                    ipV4AddressPart.fourthOctet
            );
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
        private static final Pattern IP_V_FUTURE_ADDRESS_REFERENCE_PATTERN = Pattern.compile("^\\[v([\\dABCDEF]+)\\.([^/?#]+)\\]");

        private final String version;
        private final String address;

        private IpVFutureAddress(final String version, final String address) {
            this.version = version;
            this.address = address.toLowerCase(ENGLISH);
        }

        private static <T extends Exception> IpVFutureAddress makeIpVFutureAddress(final String version, final String address, final ExceptionFactory<T> exceptionFactory) throws T {
            if (version.isEmpty()) {
                throw exceptionFactory.makeException("Version must contain at least one character");
            }
            verify(HEX_DIGIT, version, "version");
            if (address.isEmpty()) {
                throw exceptionFactory.makeException("Address must contain at least one character");
            }
            verify(ADDRESS_CHARACTER_SET_MEMBERSHIP_FUNCTION, address, "address", exceptionFactory);
            return new IpVFutureAddress(version, address);
        }

        static boolean isValid(final String hostString) {
            Matcher matcher = IP_V_FUTURE_ADDRESS_REFERENCE_PATTERN.matcher(hostString);
            return matcher.matches()
                    && CharacterSetMembershipFunction.HEX_DIGIT.areMembers(matcher.group(1))
                    && ADDRESS_CHARACTER_SET_MEMBERSHIP_FUNCTION.areMembers(matcher.group(2));
        }

        static IpVFutureAddress parse(final String hostString) throws ParseException {
            Matcher matcher = IP_V_FUTURE_ADDRESS_REFERENCE_PATTERN.matcher(hostString);
            matcher.matches();
            return makeIpVFutureAddress(matcher.group(1), matcher.group(2), PARSE_EXCEPTION_EXCEPTION_FACTORY);
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
