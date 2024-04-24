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

import static net.sourceforge.urin.Fragment.fragment;
import static net.sourceforge.urin.Path.rootlessPath;
import static net.sourceforge.urin.Query.query;
import static net.sourceforge.urin.Segment.segment;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class MailtoSchemeTest {

    @Test
    void canCreateAMailtoUri() throws ParseException {
        final MakingDecoder<Segment<String>, String, String> segmentMakingDecoder = new MakingDecoder<>(PercentEncodingPartial.noOp()) {
            @Override
            protected Segment<String> makeOne(final String s) {
                return segment(s);
            }
        };
        final MakingDecoder<Query<?>, String, String> queryMakingDecoder = new MakingDecoder<>(PercentEncodingPartial.noOp()) {
            @Override
            protected Query<String> makeOne(final String s) {
                return query(s);
            }
        };
        final MakingDecoder<Fragment<?>, String, String> fragmentMakingDecoder = new MakingDecoder<>(PercentEncodingPartial.noOp()) {
            @Override
            protected Fragment<String> makeOne(final String s) {
                return fragment(s);
            }
        };
        final var scheme = new SchemeWithDefaultPort<>("mailto", Port.port(25), segmentMakingDecoder, queryMakingDecoder, fragmentMakingDecoder) {};
        assertThat(scheme.urin(rootlessPath(segment("mark@example.com"))).asString(), equalTo("mailto:mark@example.com"));
        final Urin<String, Query<?>, Fragment<?>> actual = scheme.parseUrin("mailto:mark@example.com");
        assertThat(actual, equalTo(scheme.urin(rootlessPath(segment("mark@example.com")))));
    }
}
