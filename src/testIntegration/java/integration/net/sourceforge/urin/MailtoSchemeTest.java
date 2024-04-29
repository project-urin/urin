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
import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static net.sourceforge.urin.Fragment.fragment;
import static net.sourceforge.urin.Path.rootlessPath;
import static net.sourceforge.urin.PercentEncodingPartial.percentEncodingDelimitedValue;
import static net.sourceforge.urin.Query.query;
import static net.sourceforge.urin.Segment.segment;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class MailtoSchemeTest {

    private static final MakingDecoder<Segment<Iterable<String>>, Iterable<String>, String> SEGMENT_MAKING_DECODER = new MakingDecoder<>(percentEncodingDelimitedValue(',')) {
        @Override
        protected Segment<Iterable<String>> makeOne(final Iterable<String> strings) {
            return segment(strings, percentEncodingDelimitedValue(','));
        }
    };
    private static final MakingDecoder<Query<?>, String, String> QUERY_MAKING_DECODER = new MakingDecoder<>(PercentEncodingPartial.noOp()) {
        @Override
        protected Query<String> makeOne(final String s) {
            return query(s);
        }
    };
    private static final MakingDecoder<Fragment<?>, String, String> FRAGMENT_MAKING_DECODER = new MakingDecoder<>(PercentEncodingPartial.noOp()) {
        @Override
        protected Fragment<String> makeOne(final String s) {
            return fragment(s);
        }
    };
    private static final SchemeWithDefaultPort<Iterable<String>, Query<?>, Fragment<?>> MAILTO = new SchemeWithDefaultPort<>("mailto", Port.port(25), SEGMENT_MAKING_DECODER, QUERY_MAKING_DECODER, FRAGMENT_MAKING_DECODER) {
    };


    @Test
    void canCreateAMailtoUri() throws ParseException {
        assertThat(MAILTO.urin(rootlessPath(segment(asList("mark@example.com", "elvis@example.com"), percentEncodingDelimitedValue(',')))).asString(), equalTo("mailto:mark@example.com,elvis@example.com"));
        final Urin<Iterable<String>, Query<?>, Fragment<?>> actual = MAILTO.parseUrin("mailto:mark@example.com,elvis@example.com");
        assertThat(actual, equalTo(MAILTO.urin(rootlessPath(segment(asList("mark@example.com", "elvis@example.com"), percentEncodingDelimitedValue(','))))));
    }
}
