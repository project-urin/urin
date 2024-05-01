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
import net.sourceforge.urin.Scheme.GenericScheme;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.*;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Map.entry;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;
import static net.sourceforge.urin.Fragment.fragment;
import static net.sourceforge.urin.Path.rootlessPath;
import static net.sourceforge.urin.PercentEncodingPartial.noOp;
import static net.sourceforge.urin.PercentEncodingPartial.percentEncodingDelimitedValue;
import static net.sourceforge.urin.Segment.segment;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class MailtoSchemeTest {

    private static <T> PercentEncodingPartial<Iterable<QueryParameter>, T> encodeQueryParameters(final PercentEncodingPartial<Iterable<QueryParameter>, T> childPercentEncodingPartial) {
        return PercentEncodingPartial.transformingPercentEncodingPartial(childPercentEncodingPartial, new Transformer<>() {
            @Override
            public Iterable<QueryParameter> encode(final Iterable<QueryParameter> queryParameters) {
                final List<QueryParameter> result = new ArrayList<>();
                for (final QueryParameter queryParameter : queryParameters) {
                    result.add(queryParameter);
                }
                return result;
            }

            @Override
            public Iterable<QueryParameter> decode(final Iterable<QueryParameter> queryParameters) {
                final List<QueryParameter> result = new ArrayList<>();
                for (final QueryParameter queryParameter : queryParameters) {
                    result.add(queryParameter);
                }
                return result;
            }
        });
    }

    private static <T> PercentEncodingPartial<QueryParameter, T> percentEncodedQueryParameter(final PercentEncodingPartial<Iterable<String>, T> childPercentEncodingPartial) {
        return PercentEncodingPartial.transformingPercentEncodingPartial(childPercentEncodingPartial, new Transformer<>() {
            @Override
            public Iterable<String> encode(final QueryParameter queryParameter) {
                return queryParameter.encoded();
            }

            @Override
            public QueryParameter decode(final Iterable<String> strings) throws ParseException {
                final Iterator<String> iterator = strings.iterator();
                if (!iterator.hasNext()) {
                    throw new ParseException("Invalid query parameter String [" + strings + "]");
                }
                final QueryParameter result;
                final String name = iterator.next();
                if (iterator.hasNext()) {
                    result = new QueryParameter(name, iterator.next());
                    if (iterator.hasNext()) {
                        throw new ParseException("Invalid query parameter - expected exactly two elements in [" + strings + "]");
                    }
                } else {
                    throw new ParseException("Invalid query parameter - expected exactly two elements in [" + strings + "]");
                }
                return result;
            }
        });
    }

    @Test
    void canRoundTripAMailtoUri() throws ParseException {
        assertThat(Mailto.mailto(asList("mark@example.com", "elvis@example.com")).asString(), equalTo("mailto:mark@example.com,elvis@example.com"));
        assertThat(Mailto.parse("mailto:mark@example.com,elvis@example.com"), equalTo(Mailto.mailto(asList("mark@example.com", "elvis@example.com"))));
        assertThat(Mailto.parse("mailto:mark@example.com,elvis@example.com").addresses(), equalTo(asList("mark@example.com", "elvis@example.com")));
    }

    @Test
    void canCreateAMailtoUriWithSubject() throws ParseException {
        assertThat(Mailto.mailto(asList("mark@example.com", "elvis@example.com")).withSubject("Hello, World!").asString(), equalTo("mailto:mark@example.com,elvis@example.com?subject=Hello,%20World!"));
        assertThat(Mailto.parse("mailto:mark@example.com,elvis@example.com?subject=Hello,%20World!"), equalTo(Mailto.mailto(asList("mark@example.com", "elvis@example.com")).withSubject("Hello, World!")));
    }

    @ParameterizedTest
    @ArgumentsSource(Rfc6068ExamplesArgumentsProvider.class)
    void canMakeRfc6068Examples(final String stringRepresentation, final Mailto mailtoRepresentation) throws ParseException {
        assertThat("can generate " + stringRepresentation, mailtoRepresentation.asString(), equalTo(stringRepresentation));
        assertThat("can parse " + stringRepresentation, Mailto.parse(stringRepresentation), equalTo(mailtoRepresentation));
    }

    @Test
    void rfc6068ErratumDemonstration() throws ParseException {
        final Mailto roundTrippedMailto = Mailto.parse(Mailto.mailto(List.of("Mike&family@example.org")).withSubject("Use of &").withBody("Should be fine in addr-spec").asString());
        assertThat(roundTrippedMailto.addresses, equalTo(List.of("Mike&family@example.org")));
        assertThat(roundTrippedMailto.subject, equalTo(Optional.of("Use of &")));
        assertThat(roundTrippedMailto.body, equalTo(Optional.of("Should be fine in addr-spec")));
    }

    private static final class Mailto {
        private static final PercentEncodingPartial<Iterable<String>, String> ADDRESS_PERCENT_ENCODING_PARTIAL = percentEncodingDelimitedValue(',');
        private static final Scheme<Iterable<String>, MailtoQuery, Fragment<?>> SCHEME = new GenericScheme<>(
                "mailto",
                new MakingDecoder<>(ADDRESS_PERCENT_ENCODING_PARTIAL) {
                    @Override
                    protected Segment<Iterable<String>> makeOne(final Iterable<String> strings) {
                        return segment(strings, ADDRESS_PERCENT_ENCODING_PARTIAL);
                    }
                },
                new MakingDecoder<>(MailtoQuery.MAILTO_QUERY_PERCENT_ENCODING_PARTIAL) {
                    @Override
                    protected MailtoQuery makeOne(final Iterable<QueryParameter> queryParameters) {
                        return new MailtoQuery(queryParameters);
                    }
                },
                new MakingDecoder<>(noOp()) {
                    @Override
                    protected Fragment<String> makeOne(final String value) {
                        return fragment(value);
                    }
                }
        );

        private final Collection<String> addresses;
        @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
        private final Optional<String> subject;
        @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
        private final Optional<String> body;
        @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
        private final Optional<String> inReplyTo;
        @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
        private final Optional<String> cc;

        @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
        private Mailto(final Collection<String> addresses, final Optional<String> subject, final Optional<String> body, final Optional<String> inReplyTo, final Optional<String> cc) {
            this.addresses = addresses;
            this.subject = subject;
            this.body = body;
            this.inReplyTo = inReplyTo;
            this.cc = cc;
        }

        static Mailto parse(final String uri) throws ParseException {
            final Urin<Iterable<String>, MailtoQuery, Fragment<?>> urin = SCHEME.parseUrin(uri);
            return new Mailto(
                    stream(urin.path().segments().get(0).value().spliterator(), false).collect(toList()),
                    urin.hasQuery() ? stream(urin.query().value().spliterator(), false).filter(queryParameter -> "subject".equalsIgnoreCase(queryParameter.name)).findFirst().map(subject -> subject.value) : Optional.empty(),
                    urin.hasQuery() ? stream(urin.query().value().spliterator(), false).filter(queryParameter -> "body".equalsIgnoreCase(queryParameter.name)).findFirst().map(subject -> subject.value) : Optional.empty(),
                    urin.hasQuery() ? stream(urin.query().value().spliterator(), false).filter(queryParameter -> "in-reply-to".equalsIgnoreCase(queryParameter.name)).findFirst().map(subject -> subject.value) : Optional.empty(),
                    urin.hasQuery() ? stream(urin.query().value().spliterator(), false).filter(queryParameter -> "cc".equalsIgnoreCase(queryParameter.name)).findFirst().map(subject -> subject.value) : Optional.empty());
        }

        static Mailto mailto(final List<String> addresses) {
            return new Mailto(new ArrayList<>(addresses), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
        }

        private Urin<Iterable<String>, MailtoQuery, Fragment<?>> urin() {
            final List<QueryParameter> queryParameters = Stream.concat(Stream.concat(Stream.concat(
                                    cc.stream().map(subject -> new QueryParameter("cc", subject)),
                                    subject.stream().map(subject -> new QueryParameter("subject", subject))),
                            body.stream().map(body -> new QueryParameter("body", body))),
                    inReplyTo.stream().map(body -> new QueryParameter("In-Reply-To", body))
            ).collect(toList());
            return queryParameters.isEmpty() ? SCHEME.urin(rootlessPath(segment(addresses, ADDRESS_PERCENT_ENCODING_PARTIAL))) : SCHEME.urin(rootlessPath(segment(addresses, ADDRESS_PERCENT_ENCODING_PARTIAL)), new MailtoQuery(queryParameters));
        }

        Mailto withSubject(final String subject) {
            return new Mailto(addresses, Optional.of(subject), body, inReplyTo, cc);
        }

        Mailto withBody(final String body) {
            return new Mailto(addresses, subject, Optional.of(body), inReplyTo, cc);
        }

        Mailto withInReplyTo(@SuppressWarnings("SameParameterValue") final String messageId) {
            return new Mailto(addresses, subject, body, Optional.of(messageId), cc);
        }

        Mailto withCc(@SuppressWarnings("SameParameterValue") final String cc) {
            return new Mailto(addresses, subject, body, inReplyTo, Optional.of(cc));
        }

        Iterable<String> addresses() {
            return new ArrayList<>(addresses);
        }

        String asString() {
            return urin().asString();
        }

        @Override
        public boolean equals(final Object that) {
            if (this == that) {
                return true;
            } else if (that == null || getClass() != that.getClass()) {
                return false;
            }

            final Mailto thatMailto = (Mailto) that;
            return this.addresses.equals(thatMailto.addresses) && this.subject.equals(thatMailto.subject) && this.body.equals(thatMailto.body) && this.inReplyTo.equals(thatMailto.inReplyTo) && this.cc.equals(thatMailto.cc);
        }

        @Override
        public int hashCode() {
            return Objects.hash(addresses, subject, body, inReplyTo, cc);
        }
    }

    private static final class MailtoQuery extends Query<Iterable<QueryParameter>> {
        private static final PercentEncodingPartial<Iterable<QueryParameter>, String> MAILTO_QUERY_PERCENT_ENCODING_PARTIAL = encodeQueryParameters(
                percentEncodingDelimitedValue(
                        '&',
                                percentEncodedQueryParameter(percentEncodingDelimitedValue(
                                        '='))));


        MailtoQuery(final Iterable<QueryParameter> queryParameters) {
            super(queryParameters, MAILTO_QUERY_PERCENT_ENCODING_PARTIAL);
        }
    }

    private static final class QueryParameter {

        private final String name;
        private final String value;

        public QueryParameter(final String name, final String value) {
            this.name = name;
            this.value = value;
        }

        public Iterable<String> encoded() {
            return asList(name, value);
        }

        @Override
        public boolean equals(final Object that) {
            if (this == that) {
                return true;
            } else if (that == null || getClass() != that.getClass()) {
                return false;
            }

            final QueryParameter thatQueryParameter = (QueryParameter) that;
            return name.equals(thatQueryParameter.name) && value.equals(thatQueryParameter.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, value);
        }
    }

    static final class Rfc6068ExamplesArgumentsProvider implements ArgumentsProvider {
        @Override
        @SuppressWarnings("SpellCheckingInspection")
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    entry("mailto:chris@example.com", Mailto.mailto(List.of("chris@example.com"))),
                    entry("mailto:infobot@example.com?subject=current-issue", Mailto.mailto(List.of("infobot@example.com")).withSubject("current-issue")),
                    entry("mailto:infobot@example.com?body=send%20current-issue", Mailto.mailto(List.of("infobot@example.com")).withBody("send current-issue")),
                    entry("mailto:infobot@example.com?body=send%20current-issue%0D%0Asend%20index", Mailto.mailto(List.of("infobot@example.com")).withBody("send current-issue\r\nsend index")),
                    entry("mailto:list@example.org?In-Reply-To=%3C3469A91.D10AF4C@example.com%3E", Mailto.mailto(List.of("list@example.org")).withInReplyTo("<3469A91.D10AF4C@example.com>")),
                    entry("mailto:majordomo@example.com?body=subscribe%20bamboo-l", Mailto.mailto(List.of("majordomo@example.com")).withBody("subscribe bamboo-l")),
                    entry("mailto:joe@example.com?cc=bob@example.com&body=hello", Mailto.mailto(List.of("joe@example.com")).withCc("bob@example.com").withBody("hello")),
                    entry("mailto:gorby%25kremvax@example.com", Mailto.mailto(List.of("gorby%kremvax@example.com"))),
                    entry("mailto:unlikely%3Faddress@example.com", Mailto.mailto(List.of("unlikely?address@example.com")))
//                    , entry("mailto:Mike%26family@example.org", Mailto.mailto(List.of("Mike&family@example.org"))) // TODO see https://www.rfc-editor.org/errata/eid7919
            ).map(entry -> Arguments.arguments(entry.getKey(), entry.getValue()));
        }
    }

}
