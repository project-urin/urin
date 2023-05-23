/*
 * Copyright 2023 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.urin;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Locale.ENGLISH;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;
import static net.sourceforge.urin.CharacterSetMembershipFunction.*;
import static net.sourceforge.urin.Hexadectet.ZERO;
import static net.sourceforge.urin.Hexadectet.hexadectet;
import static net.sourceforge.urin.Octet.octet;

/**
 * A host component of a URI.
 * <p>
 * RFC 3986 specifies four forms of host - registered name, IP version 4 address, IP version 6 address, and a future IP version address.
 * Note that as pointed out in the RFC, there is an overlap between what is considered a valid IP version 4 address, and what is considered
 * a valid registered name - for example, {@code Host.registeredName("127.0.0.1")} renders identically in a URI to
 * {@code ipV4Address(octet(127), octet(0), octet(0), octet(1))}.  In keeping with the RFC, such a registered name is considered
 * to be an equivalent IP version 4 address.
 * <p>
 * Immutable and thread safe.
 *
 * @see <a href="http://tools.ietf.org/html/rfc3986#section-3.2.2">RFC 3986 - Host</a>
 */
public abstract class Host {

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

    public static final Host UNSPECIFIED_ADDRESS_IP_V6 = ipV6Address(ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO);

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
     * Factory method for creating registered name type {@code Host}s.
     *
     * @param registeredName any {@code String} to represent as a {@code Host}.
     * @return a {@code Host} representing the given {@code String} as a registered name.
     */
    public static Host registeredName(final String registeredName) {
        return IpV4Address.parses(registeredName).flatMap(AugmentedOptional::<Host>of) // https://tools.ietf.org/html/rfc3986#section-3.2.2 "If host matches the rule for IPv4address, then it should be considered an IPv4 address literal and not a reg-name."
                .orElseGet(() -> new RegisteredName(registeredName));
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
     * Factory method for creating IP version 4 type {@code Host}s.
     * IP version 4 {@code Host}s are made up of four {@code Octet}s, which are created from the given {@code int}s.
     *
     * @return a {@code Host} representing the given {@code int}s as an IP version 4 address.
     */
    public static Host ipV4Address(final int firstOctet, final int secondOctet, final int thirdOctet, final int fourthOctet) {
        return new IpV4Address(octet(firstOctet), octet(secondOctet), octet(thirdOctet), octet(fourthOctet));
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
     * @param address a {@code String} consisting of at least one character, made up solely of characters from the Latin alphabet, the digits, and any of '-', '.', '_' , '~' , '!' , '$' , '&amp;' , ''' , '(' , ')' , '*' , '+' , ',' , ';' , '=' , ':'.
     * @return a {@code Host} representing the given {@code Hexadectet}s and {@code Octet}s as an IP version 6 address.
     * @throws IllegalArgumentException if the given version or address are empty of contain characters outside the valid set.
     */
    public static Host ipVFutureAddress(final String version, final String address) {
        return IpVFutureAddress.makeIpVFutureAddress(version, address).orElseThrow(IllegalArgumentException::new);
    }

    private static final class Elidable {
        private final String content;
        private final boolean isElided;

        static Elidable elided() {
            return new Elidable("", true);
        }

        static Elidable nonElided(final String content) {
            return new Elidable(content, false);
        }

        Elidable(final String content, final boolean isElided) {
            this.content = content;
            this.isElided = isElided;
        }
    }

    private static Deque<Elidable> elide(final Hexadectet... hexadectets) {
        int maximumStreakLength = 0;
        int maximumStreakEnd = 0;
        for (int i = 0, streakLengthToHere = 0; i < hexadectets.length; i++) {
            Hexadectet hexadectet = hexadectets[i];
            streakLengthToHere = hexadectet.isElidable() ? streakLengthToHere + 1 : 0;
            if (streakLengthToHere > maximumStreakLength) {
                maximumStreakLength = streakLengthToHere;
                maximumStreakEnd = i;
            }
        }

        Deque<Elidable> result = new LinkedList<>();
        for (int i = 0; i < hexadectets.length; i++) {
            Hexadectet hexadectet = hexadectets[i];
            if (maximumStreakLength <= 1 || i <= maximumStreakEnd - maximumStreakLength || i > maximumStreakEnd) {
                result.add(Elidable.nonElided(hexadectet.asString()));
            } else if (i == 0 || i == maximumStreakEnd) {
                result.add(Elidable.elided());
            }
        }
        return result;
    }

    static Host parse(final String hostString) throws ParseException {
        return AugmentedOptional.<Host>empty("Not a valid host :" + hostString)
                .or(() -> IpV4Address.parses(hostString))
                .or(() -> IpV6Address.parses(hostString))
                .or(() -> IpV6AddressWithTrailingIpV4Address.parses(hostString))
                .or(() -> IpVFutureAddress.parses(hostString))
                .or(() -> RegisteredName.parses(hostString))
                .orElseThrow(ParseException::new);
    }

    private static final class RegisteredName extends Host {
        private static final CharacterSetMembershipFunction REGISTERED_NAME_CHARACTER_SET = or(
                UNRESERVED,
                SUB_DELIMITERS
        );
        private static final PercentEncoder PERCENT_ENCODER = new PercentEncoder(REGISTERED_NAME_CHARACTER_SET);
        private final String registeredName;

        RegisteredName(final String registeredName) { // TODO determine whether empty String is a valid registered name
            this.registeredName = registeredName.toLowerCase(ENGLISH); // TODO determine what 'case insensitive means in the RFC w.r.t non-English characters
        }

        @Override
        String asString() {
            return PERCENT_ENCODER.encode(registeredName);
        }

        @Override
        public boolean equals(final Object object) {
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;

            RegisteredName that = (RegisteredName) object;
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

        static AugmentedOptional<RegisteredName> parses(final String hostString) {
            final String decode;
            try {
                decode = PERCENT_ENCODER.decode(hostString);
            } catch (ParseException e) {
                return AugmentedOptional.empty("Not a valid registered name; invalid percent encoding in:" + hostString + " - " + e.getMessage());
            }
            return AugmentedOptional.of(new RegisteredName(decode));
        }

        /**
         * @deprecated this was inadvertently public
         */
        @Deprecated
        public static boolean isValid(final String hostString) { // TODO delete this
            return PERCENT_ENCODER.isMember(hostString);
        }
    }

    private static final class IpV4Address extends Host {
        private final Octet firstOctet;
        private final Octet secondOctet;
        private final Octet thirdOctet;
        private final Octet fourthOctet;

        IpV4Address(final Octet firstOctet, final Octet secondOctet, final Octet thirdOctet, final Octet fourthOctet) {
            this.firstOctet = requireNonNull(firstOctet, "Cannot instantiate Host with null firstOctet");
            this.secondOctet = requireNonNull(secondOctet, "Cannot instantiate Host with null secondOctet");
            this.thirdOctet = requireNonNull(thirdOctet, "Cannot instantiate Host with null thirdOctet");
            this.fourthOctet = requireNonNull(fourthOctet, "Cannot instantiate Host with null fourthOctet");
        }

        @Override
        String asString() {
            return firstOctet.asString() +
                    '.' + secondOctet.asString() +
                    '.' + thirdOctet.asString() +
                    '.' + fourthOctet.asString();
        }

        @Override
        public boolean equals(final Object object) {
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;

            IpV4Address that = (IpV4Address) object;
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

        static AugmentedOptional<IpV4Address> parses(final String hostString) {
            String[] split = hostString.split("\\.");
            if (split.length != 4) {
                return AugmentedOptional.empty("Invalid Host String [" + hostString + "]");
            }
            return Octet.parses(split[0]).flatMap(
                    firstOctet -> Octet.parses(split[1]).flatMap(
                            secondOctet -> Octet.parses(split[2]).flatMap(
                                    thirdOctet -> Octet.parses(split[3]).flatMap(
                                            fourthOctet -> AugmentedOptional.of(new IpV4Address(firstOctet, secondOctet, thirdOctet, fourthOctet))
                                    ))));
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
            this.firstHexadectet = requireNonNull(firstHexadectet, "Cannot instantiate Host with null firstHexadectet");
            this.secondHexadectet = requireNonNull(secondHexadectet, "Cannot instantiate Host with null secondHexadectet");
            this.thirdHexadectet = requireNonNull(thirdHexadectet, "Cannot instantiate Host with null thirdHexadectet");
            this.fourthHexadectet = requireNonNull(fourthHexadectet, "Cannot instantiate Host with null fourthHexadectet");
            this.fifthHexadectet = requireNonNull(fifthHexadectet, "Cannot instantiate Host with null fifthHexadectet");
            this.sixthHexadectet = requireNonNull(sixthHexadectet, "Cannot instantiate Host with null sixthHexadectet");
            this.seventhHexadectet = requireNonNull(seventhHexadectet, "Cannot instantiate Host with null seventhHexadectet");
            this.eighthHexadectet = requireNonNull(eighthHexadectet, "Cannot instantiate Host with null eighthHexadectet");
        }

        @Override
        String asString() {
            final Deque<Elidable> elided = Host.elide(firstHexadectet, secondHexadectet, thirdHexadectet, fourthHexadectet, fifthHexadectet, sixthHexadectet, seventhHexadectet, eighthHexadectet);
            return "[" + elided.stream().map(elidable -> elidable.content).collect(joining(":")) + (elided.getLast().isElided ? ":" : "") + "]";
        }

        @Override
        public boolean equals(final Object object) {
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;

            IpV6Address that = (IpV6Address) object;
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

        static AugmentedOptional<IpV6Address> parses(final String hostString) {
            if (!(hostString.startsWith("[") && hostString.endsWith("]"))) {
                return AugmentedOptional.empty("Invalid IP V6 Address, must start with [ and end with ] :" + hostString);
            }
            final String expandedIpV6Address = expandElision(hostString.substring(1, hostString.length() - 1));
            String[] hexadectetStrings = expandedIpV6Address.split(":", -1);
            return parseHexadectets(hostString, hexadectetStrings, 8).flatMap(hexadectets ->
                    hexadectets.get(0).flatMap(first ->
                            hexadectets.get(1).flatMap(second ->
                                    hexadectets.get(2).flatMap(third ->
                                            hexadectets.get(3).flatMap(fourth ->
                                                    hexadectets.get(4).flatMap(fifth ->
                                                            hexadectets.get(5).flatMap(sixth ->
                                                                    hexadectets.get(6).flatMap(seventh ->
                                                                            hexadectets.get(7).flatMap(eighth ->
                                                                                    AugmentedOptional.of(new IpV6Address(first, second, third, fourth, fifth, sixth, seventh, eighth)))))))))));
        }

        static String expandElision(final String ipV6String) {
            if (!ipV6String.contains("::")) {
                return ipV6String;
            } else if ("::".equals(ipV6String)) {
                return "0:0:0:0:0:0:0:0";
            } else {
                final long colonCount = countColons(ipV6String);
                if (ipV6String.startsWith("::")) {
                    final StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < 9 - colonCount; i++) {
                        stringBuilder.append("0:");
                    }
                    return ipV6String.replaceFirst("::", stringBuilder.toString()); // TODO dump in the string builder at the end
                } else if (ipV6String.endsWith("::")) {
                    final StringBuilder stringBuilder = new StringBuilder(ipV6String.substring(0, ipV6String.length() - 2));
                    for (int i = 0; i < 9 - colonCount; i++) {
                        stringBuilder.append(":0");
                    }
                    return stringBuilder.toString();
                } else {
                    final StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < 8 - colonCount; i++) {
                        stringBuilder.append(":0");
                    }
                    stringBuilder.append(':');
                    return ipV6String.replaceFirst("::", stringBuilder.toString());
                }
            }
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

        IpV6AddressWithTrailingIpV4Address(final Hexadectet firstHexadectet, final Hexadectet secondHexadectet, final Hexadectet thirdHexadectet, final Hexadectet fourthHexadectet, final Hexadectet fifthHexadectet, final Hexadectet sixthHexadectet, final Octet firstOctet, final Octet secondOctet, final Octet thirdOctet, final Octet fourthOctet) {
            this.firstHexadectet = requireNonNull(firstHexadectet, "Cannot instantiate Host with null firstHexadectet");
            this.secondHexadectet = requireNonNull(secondHexadectet, "Cannot instantiate Host with null secondHexadectet");
            this.thirdHexadectet = requireNonNull(thirdHexadectet, "Cannot instantiate Host with null thirdHexadectet");
            this.fourthHexadectet = requireNonNull(fourthHexadectet, "Cannot instantiate Host with null fourthHexadectet");
            this.fifthHexadectet = requireNonNull(fifthHexadectet, "Cannot instantiate Host with null fifthHexadectet");
            this.sixthHexadectet = requireNonNull(sixthHexadectet, "Cannot instantiate Host with null sixthHexadectet");
            this.firstOctet = requireNonNull(firstOctet, "Cannot instantiate Host with null firstOctet");
            this.secondOctet = requireNonNull(secondOctet, "Cannot instantiate Host with null secondOctet");
            this.thirdOctet = requireNonNull(thirdOctet, "Cannot instantiate Host with null thirdOctet");
            this.fourthOctet = requireNonNull(fourthOctet, "Cannot instantiate Host with null fourthOctet");
        }

        static AugmentedOptional<IpV6AddressWithTrailingIpV4Address> parses(final String hostString) {
            if (!(hostString.startsWith("[") && hostString.endsWith("]"))) {
                return AugmentedOptional.empty("Invalid IP V6 Address, must start with [ and end with ] :" + hostString);
            }
            final String ipV6Address = hostString.substring(1, hostString.length() - 1);
            final String[] elidableHexadectetStrings = ipV6Address.split(":", -1);
            final AugmentedOptional<IpV4Address> ipV4AddressPart = elidableHexadectetStrings.length == 0
                    ? AugmentedOptional.empty("Invalid IP v6 String [" + hostString + "]: no trailing IP v4 address.")
                    : IpV4Address.parses(elidableHexadectetStrings[elidableHexadectetStrings.length - 1]);
            final String ipV6AddressPart = ipV6Address.substring(0, ipV6Address.lastIndexOf(':') + 1);
            final String expandedIpv6AddressPart = expandElision(ipV6AddressPart); // TODO expand elision on whole address (might be able to share method with full IPv6 address...

            final String[] hexadectetStrings = expandedIpv6AddressPart.split(":", -1);
            return parseHexadectets(hostString, Arrays.copyOf(hexadectetStrings, hexadectetStrings.length - 1), 6).flatMap(hexadectets ->
                    ipV4AddressPart.flatMap(ipV4Address ->
                            hexadectets.get(0).flatMap(first ->
                                    hexadectets.get(1).flatMap(second ->
                                            hexadectets.get(2).flatMap(third ->
                                                    hexadectets.get(3).flatMap(fourth ->
                                                            hexadectets.get(4).flatMap(fifth ->
                                                                    hexadectets.get(5).flatMap(sixth ->
                                                                            AugmentedOptional.of(new IpV6AddressWithTrailingIpV4Address(first, second, third, fourth, fifth, sixth, ipV4Address.firstOctet, ipV4Address.secondOctet, ipV4Address.thirdOctet, ipV4Address.fourthOctet))
                                                                    ))))))));
        }

        static String expandElision(final String ipV6String) {
            if (!ipV6String.contains("::")) {
                return ipV6String;
            } else if ("::".equals(ipV6String)) {
                return "0:0:0:0:0:0:";
            } else {
                final long colonCount = countColons(ipV6String);
                if (ipV6String.startsWith("::")) {
                    final StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < 8 - colonCount; i++) {
                        stringBuilder.append("0:");
                    }
                    return ipV6String.replaceFirst("::", stringBuilder.toString()); // TODO dump in the string builder at the end
                } else {
                    final StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < 7 - colonCount; i++) {
                        stringBuilder.append(":0");
                    }
                    stringBuilder.append(':');
                    return ipV6String.replaceFirst("::", stringBuilder.toString());
                }
            }
        }

        @Override
        String asString() {
            final Deque<Elidable> elided = Host.elide(firstHexadectet, secondHexadectet, thirdHexadectet, fourthHexadectet, fifthHexadectet, sixthHexadectet);
            return "[" + elided.stream().map(elidable -> elidable.content).collect(joining(":")) + ":" + ipV4Address(firstOctet, secondOctet, thirdOctet, fourthOctet).asString() + "]";
        }

        @Override
        public boolean equals(final Object object) {
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;

            IpV6AddressWithTrailingIpV4Address that = (IpV6AddressWithTrailingIpV4Address) object;
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

    private static int countColons(final String string) {
        int count = 0;
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == ':') {
                count++;
            }
        }
        return count;
    }

    private static AugmentedOptional<List<AugmentedOptional<Hexadectet>>> parseHexadectets(final String hostString, final String[] hexadectetStrings, final int requiredLength) {
        List<AugmentedOptional<Hexadectet>> hexadectets = new ArrayList<>(requiredLength);
        for (String hexadectetString : hexadectetStrings) {
            hexadectets.add(Hexadectet.parses(hexadectetString));
        }
        if (hexadectets.size() != requiredLength) {
            return AugmentedOptional.empty("Invalid host string [" + hostString + "]");
        }
        return AugmentedOptional.of(hexadectets);
    }

    private static final class IpVFutureAddress extends Host {
        private static final Pattern IP_V_FUTURE_ADDRESS_REFERENCE_PATTERN = Pattern.compile("^\\[v([\\dABCDEFabcdef]+)\\.([^/?#]+)]");

        private final String version;
        private final String address;

        private IpVFutureAddress(final String version, final String address) {
            this.version = version;
            this.address = address.toLowerCase(ENGLISH);
        }

        private static AugmentedOptional<IpVFutureAddress> makeIpVFutureAddress(final String version, final String address) {
            if (version.isEmpty()) {
                return AugmentedOptional.empty("Version must contain at least one character");
            }
            for (int i = 0; i < version.length(); i++) {
                if (!HEX_DIGIT.isMember(version.charAt(i))) {
                    return AugmentedOptional.empty("Character " + (i + 1) + " must be " + HEX_DIGIT.describe() + " in version [" + version + "]");
                }
            }
            if (address.isEmpty()) {
                return AugmentedOptional.empty("Address must contain at least one character");
            }
            for (int i = 0; i < address.length(); i++) {
                if (!ADDRESS_CHARACTER_SET_MEMBERSHIP_FUNCTION.isMember(address.charAt(i))) {
                    return AugmentedOptional.empty("Character " + (i + 1) + " must be " + ADDRESS_CHARACTER_SET_MEMBERSHIP_FUNCTION.describe() + " in address [" + address + "]");
                }
            }
            return AugmentedOptional.of(new IpVFutureAddress(version, address));
        }

        static AugmentedOptional<IpVFutureAddress> parses(final String hostString) {
            Matcher matcher = IP_V_FUTURE_ADDRESS_REFERENCE_PATTERN.matcher(hostString);
            if (matcher.matches()
                    && CharacterSetMembershipFunction.HEX_DIGIT.areMembers(matcher.group(1))
                    && ADDRESS_CHARACTER_SET_MEMBERSHIP_FUNCTION.areMembers(matcher.group(2))) {
                return makeIpVFutureAddress(matcher.group(1), matcher.group(2));
            } else {
                return AugmentedOptional.empty("Not a valid IP vFuture address :" + hostString);
            }
        }

        @Override
        String asString() {
            return "[v" + version + '.' + address + ']';
        }

        @Override
        public boolean equals(final Object object) {
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;

            IpVFutureAddress that = (IpVFutureAddress) object;

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
