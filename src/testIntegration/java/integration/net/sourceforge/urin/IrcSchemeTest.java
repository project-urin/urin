/*
 * Copyright 2024 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package integration.net.sourceforge.urin;

import net.sourceforge.urin.*;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.List;
import java.util.stream.Stream;

import static java.util.Map.entry;
import static java.util.stream.Collectors.toList;
import static net.sourceforge.urin.Authority.authority;
import static net.sourceforge.urin.Fragment.fragment;
import static net.sourceforge.urin.Hexadectet.ZERO;
import static net.sourceforge.urin.Hexadectet.hexadectet;
import static net.sourceforge.urin.Host.*;
import static net.sourceforge.urin.Path.path;
import static net.sourceforge.urin.PercentEncodingPartial.noOp;
import static net.sourceforge.urin.PercentEncodingPartial.percentEncodingDelimitedValue;
import static net.sourceforge.urin.Port.port;
import static net.sourceforge.urin.Query.query;
import static net.sourceforge.urin.Segment.segment;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class IrcSchemeTest {

    @ParameterizedTest
    @ArgumentsSource(IrcSpecExamplesArgumentsProvider.class)
    void canMakeIrcSpecExamples(final String stringRepresentation, final Irc mailtoRepresentation) throws ParseException {
        assertThat("can generate " + stringRepresentation, mailtoRepresentation.asString(), equalTo(stringRepresentation));
        assertThat("can parse " + stringRepresentation, Irc.parse(stringRepresentation), equalTo(mailtoRepresentation));
    }

    private static final class Irc {
        private static final PercentEncodingPartial<Iterable<String>, String> CHANNEL_PERCENT_ENCODING_PARTIAL = percentEncodingDelimitedValue(',');
        private static final MakingDecoder<Segment<Iterable<String>>, Iterable<String>, String> STRING_MAKING_DECODER = new MakingDecoder<>(CHANNEL_PERCENT_ENCODING_PARTIAL) {
            @Override
            protected Segment<Iterable<String>> makeOne(final Iterable<String> strings) {
                return segment(strings, CHANNEL_PERCENT_ENCODING_PARTIAL);
            }
        };
        private static final MakingDecoder<Query<?>, String, String> QUERY_MAKING_DECODER = new MakingDecoder<>(noOp()) {
            @Override
            protected Query<?> makeOne(final String value) {
                return query(value);
            }
        };
        private static final MakingDecoder<Fragment<?>, String, String> FRAGMENT_MAKING_DECODER = new MakingDecoder<>(noOp()) {
            @Override
            protected Fragment<String> makeOne(final String value) {
                return fragment(value);
            }
        };
        private static final Scheme<Iterable<String>, Query<?>, Fragment<?>> SCHEME = new SchemeWithDefaultPort<>(
                "irc",
                port(6667),
                STRING_MAKING_DECODER,
                QUERY_MAKING_DECODER,
                FRAGMENT_MAKING_DECODER
        ) {};

        private final Urin<Iterable<String>, Query<?>, Fragment<?>> urin;

        private Irc(final Urin<Iterable<String>, Query<?>, Fragment<?>> urin) {
            this.urin = urin;
        }

        static Irc parse(final String uri) throws ParseException {
            return new Irc(SCHEME.parseUrin(uri));
        }

        static Irc irc(final Authority authority) {
            return new Irc(SCHEME.urin(authority));
        }

        private static Irc irc(final Authority authority, final List<String> channels) {
            return new Irc(SCHEME.urin(authority, path(segment(channels.stream().map(channel -> "#" + channel).collect(toList()), CHANNEL_PERCENT_ENCODING_PARTIAL))));
        }

        static Irc irc(final Authority authority, final String firstChannel) {
            return irc(authority, List.of(firstChannel));
        }

        @SuppressWarnings("SameParameterValue")
        static Irc irc(final Authority authority, final String firstChannel, final String secondChannel) {
            return irc(authority, List.of(firstChannel, secondChannel));
        }

        String asString() {
            return urin.asString();
        }

        @Override
        public boolean equals(final Object that) {
            if (this == that) {
                return true;
            } else if (that == null || getClass() != that.getClass()) {
                return false;
            }

            final Irc thatIrc = (Irc) that;
            return this.urin.equals(thatIrc.urin);
        }

        @Override
        public int hashCode() {
            return urin.hashCode();
        }
    }

    static final class IrcSpecExamplesArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    entry("irc://irc.example.com", Irc.irc(authority(registeredName("irc.example.com")))),
                    entry("irc://irc.example.com:6668", Irc.irc(authority(registeredName("irc.example.com"), port(6668)))),
                    entry("irc://irc.example.com/%23channel", Irc.irc(authority(registeredName("irc.example.com")), "channel")),
                    entry("irc://127.0.0.1", Irc.irc(authority(LOOPBACK_ADDRESS_IP_V4))),
                    entry("irc://127.0.0.1:6668", Irc.irc(authority(LOOPBACK_ADDRESS_IP_V4, port(6668)))),
                    entry("irc://127.0.0.1/%23chanel", Irc.irc(authority(LOOPBACK_ADDRESS_IP_V4), "chanel")),
                    entry("irc://[fda4:22f1:8d20::]", Irc.irc(authority(ipV6Address(hexadectet(0xfda4), hexadectet(0x22f1), hexadectet(0x8d20), ZERO, ZERO, ZERO, ZERO, ZERO)))),
                    entry("irc://[fda4:22f1:8d20::]:6668", Irc.irc(authority(ipV6Address(hexadectet(0xfda4), hexadectet(0x22f1), hexadectet(0x8d20), ZERO, ZERO, ZERO, ZERO, ZERO), port(6668)))),
                    entry("irc://[fda4:22f1:8d20::]/%23chanel", Irc.irc(authority(ipV6Address(hexadectet(0xfda4), hexadectet(0x22f1), hexadectet(0x8d20), ZERO, ZERO, ZERO, ZERO, ZERO)), "chanel")),
                    entry("irc://irc.example.com/%23channel1,%23channel2", Irc.irc(authority(registeredName("irc.example.com")), "channel1", "channel2"))
            ).map(entry -> Arguments.arguments(entry.getKey(), entry.getValue()));
        }
    }

}
