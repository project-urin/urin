/*
 * Copyright 2020 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package integration.net.sourceforge.urin;

import net.sourceforge.urin.Segment;
import org.junit.jupiter.api.Test;

import static net.sourceforge.urin.Authority.authority;
import static net.sourceforge.urin.Fragment.fragment;
import static net.sourceforge.urin.Host.registeredName;
import static net.sourceforge.urin.Path.path;
import static net.sourceforge.urin.Path.rootlessPath;
import static net.sourceforge.urin.Query.query;
import static net.sourceforge.urin.Scheme.scheme;
import static net.sourceforge.urin.SchemeBuilder.aScheme;
import static net.sourceforge.urin.Segment.segment;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

class Rfc3986ReferenceResolutionAsFormattingExamplesTest {

    @Test
    void normalExamples() {
        assertThat(scheme("g").urin(rootlessPath("h")).asString(), equalTo("g:h"));
        assertThat(aScheme().relativeReference(rootlessPath(segment("g"))).asString(), equalTo("g"));
        assertThat(aScheme().relativeReference(rootlessPath(Segment.dot(), segment("g"))).asString(), equalTo("g"));
        assertThat(aScheme().relativeReference(rootlessPath("g", "")).asString(), equalTo("g/"));
        assertThat(aScheme().relativeReference(path("g")).asString(), equalTo("/g"));
        assertThat(aScheme().relativeReference(authority(registeredName("g"))).asString(), equalTo("//g"));
        assertThat(aScheme().relativeReference(query("y")).asString(), equalTo("?y"));
        assertThat(aScheme().relativeReference(rootlessPath("g"), query("y")).asString(), equalTo("g?y"));
        assertThat(aScheme().relativeReference(fragment("s")).asString(), equalTo("#s"));
        assertThat(aScheme().relativeReference(rootlessPath("g"), fragment("s")).asString(), equalTo("g#s"));
        assertThat(aScheme().relativeReference(rootlessPath("g"), query("y"), fragment("s")).asString(), equalTo("g?y#s"));
        assertThat(aScheme().relativeReference(rootlessPath(";x")).asString(), equalTo(";x"));
        assertThat(aScheme().relativeReference(rootlessPath("g;x")).asString(), equalTo("g;x"));
        assertThat(aScheme().relativeReference(rootlessPath("g;x"), query("y"), fragment("s")).asString(), equalTo("g;x?y#s"));
        assertThat(aScheme().relativeReference().asString(), equalTo(""));
        assertThat(aScheme().relativeReference(rootlessPath(Segment.dot())).asString(), equalTo("."));
        assertThat(aScheme().relativeReference(rootlessPath(Segment.dot(), segment(""))).asString(), equalTo("./"));
        assertThat(aScheme().relativeReference(rootlessPath(Segment.dotDot())).asString(), equalTo(".."));
        assertThat(aScheme().relativeReference(rootlessPath(Segment.dotDot(), segment(""))).asString(), equalTo("../"));
        assertThat(aScheme().relativeReference(rootlessPath(Segment.dotDot(), segment("g"))).asString(), equalTo("../g"));
        assertThat(aScheme().relativeReference(rootlessPath(Segment.dotDot(), Segment.dotDot())).asString(), equalTo("../.."));
        assertThat(aScheme().relativeReference(rootlessPath(Segment.dotDot(), Segment.dotDot(), segment(""))).asString(), equalTo("../../"));
        assertThat(aScheme().relativeReference(rootlessPath(Segment.dotDot(), Segment.dotDot(), segment("g"))).asString(), equalTo("../../g"));
    }

    @Test
    void abnormalExamples() {
        assertThat(aScheme().relativeReference(rootlessPath(Segment.dotDot(), Segment.dotDot(), Segment.dotDot(), segment("g"))).asString(), equalTo("../../../g"));
        assertThat(aScheme().relativeReference(rootlessPath(Segment.dotDot(), Segment.dotDot(), Segment.dotDot(), Segment.dotDot(), segment("g"))).asString(), equalTo("../../../../g"));
        assertThat(aScheme().relativeReference(path(Segment.dot(), segment("g"))).asString(), equalTo("/g"));
        assertThat(aScheme().relativeReference(path(Segment.dotDot(), segment("g"))).asString(), equalTo("/g"));
        assertThat(aScheme().relativeReference(rootlessPath("g.")).asString(), equalTo("g."));
        assertThat(aScheme().relativeReference(rootlessPath(".g")).asString(), equalTo(".g"));
        assertThat(aScheme().relativeReference(rootlessPath("g..")).asString(), equalTo("g.."));
        assertThat(aScheme().relativeReference(rootlessPath("..g")).asString(), equalTo("..g"));
        assertThat(aScheme().relativeReference(rootlessPath(Segment.dot(), Segment.dotDot(), segment("g"))).asString(), equalTo("../g"));
        assertThat(aScheme().relativeReference(rootlessPath(Segment.dot(), segment("g"), Segment.dot())).asString(), equalTo("g/"));
        assertThat(aScheme().relativeReference(rootlessPath(segment("g"), Segment.dot(), segment("h"))).asString(), equalTo("g/h"));
        assertThat(aScheme().relativeReference(rootlessPath(segment("g"), Segment.dotDot(), segment("h"))).asString(), equalTo("h"));
        assertThat(aScheme().relativeReference(rootlessPath(segment("g;x=1"), Segment.dot(), segment("y"))).asString(), equalTo("g;x=1/y"));
        assertThat(aScheme().relativeReference(rootlessPath(segment("g;x=1"), Segment.dotDot(), segment("y"))).asString(), equalTo("y"));
        assertThat(aScheme().relativeReference(rootlessPath("g"), query("y/./x")).asString(), equalTo("g?y/./x"));
        assertThat(aScheme().relativeReference(rootlessPath("g"), query("y/../x")).asString(), equalTo("g?y/../x"));
        assertThat(aScheme().relativeReference(rootlessPath("g"), fragment("s/./x")).asString(), equalTo("g#s/./x"));
        assertThat(aScheme().relativeReference(rootlessPath("g"), fragment("s/../x")).asString(), equalTo("g#s/../x"));
        assertThat(scheme("http").urin(rootlessPath("g")).asString(), equalTo("http:g"));
    }
}
