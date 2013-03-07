/*
 * Copyright 2013 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package integration.net.sourceforge.urin;

import net.sourceforge.urin.Path;
import net.sourceforge.urin.Segment;
import org.junit.Test;

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
import static org.junit.Assert.assertThat;

public class Rfc3986ReferenceResolutionAsFormattingExamplesTest {

    @Test
    public void normalExamples() throws Exception {
        assertThat(scheme("g").urin(Path.rootlessPath("h")).asString(), equalTo("g:h"));
        assertThat(aScheme().relativeReference(rootlessPath(segment("g"))).asString(), equalTo("g"));
        assertThat(aScheme().relativeReference(Path.rootlessPath(Segment.<String>dot(), segment("g"))).asString(), equalTo("g"));
        assertThat(aScheme().relativeReference(Path.rootlessPath("g", "")).asString(), equalTo("g/"));
        assertThat(aScheme().relativeReference(path("g")).asString(), equalTo("/g"));
        assertThat(aScheme().relativeReference(authority(registeredName("g"))).asString(), equalTo("//g"));
        assertThat(aScheme().relativeReference(query("y")).asString(), equalTo("?y"));
        assertThat(aScheme().relativeReference(Path.rootlessPath("g"), query("y")).asString(), equalTo("g?y"));
        assertThat(aScheme().relativeReference(fragment("s")).asString(), equalTo("#s"));
        assertThat(aScheme().relativeReference(Path.rootlessPath("g"), fragment("s")).asString(), equalTo("g#s"));
        assertThat(aScheme().relativeReference(Path.rootlessPath("g"), query("y"), fragment("s")).asString(), equalTo("g?y#s"));
        assertThat(aScheme().relativeReference(Path.rootlessPath(";x")).asString(), equalTo(";x"));
        assertThat(aScheme().relativeReference(Path.rootlessPath("g;x")).asString(), equalTo("g;x"));
        assertThat(aScheme().relativeReference(Path.rootlessPath("g;x"), query("y"), fragment("s")).asString(), equalTo("g;x?y#s"));
        assertThat(aScheme().relativeReference().asString(), equalTo(""));
        assertThat(aScheme().relativeReference(rootlessPath(Segment.<String>dot())).asString(), equalTo("."));
        assertThat(aScheme().relativeReference(rootlessPath(Segment.<String>dot(), segment(""))).asString(), equalTo("./"));
        assertThat(aScheme().relativeReference(rootlessPath(Segment.<String>dotDot())).asString(), equalTo(".."));
        assertThat(aScheme().relativeReference(rootlessPath(Segment.<String>dotDot(), segment(""))).asString(), equalTo("../"));
        assertThat(aScheme().relativeReference(rootlessPath(Segment.<String>dotDot(), segment("g"))).asString(), equalTo("../g"));
        assertThat(aScheme().relativeReference(rootlessPath(Segment.<String>dotDot(), Segment.<String>dotDot())).asString(), equalTo("../.."));
        assertThat(aScheme().relativeReference(rootlessPath(Segment.<String>dotDot(), Segment.<String>dotDot(), segment(""))).asString(), equalTo("../../"));
        assertThat(aScheme().relativeReference(rootlessPath(Segment.<String>dotDot(), Segment.<String>dotDot(), segment("g"))).asString(), equalTo("../../g"));
    }

    @Test
    public void abnormalExamples() throws Exception {
        assertThat(aScheme().relativeReference(rootlessPath(Segment.<String>dotDot(), Segment.<String>dotDot(), Segment.<String>dotDot(), segment("g"))).asString(), equalTo("../../../g"));
        assertThat(aScheme().relativeReference(rootlessPath(Segment.<String>dotDot(), Segment.<String>dotDot(), Segment.<String>dotDot(), Segment.<String>dotDot(), segment("g"))).asString(), equalTo("../../../../g"));
        assertThat(aScheme().relativeReference(Path.path(Segment.<String>dot(), segment("g"))).asString(), equalTo("/g"));
        assertThat(aScheme().relativeReference(Path.path(Segment.<String>dotDot(), segment("g"))).asString(), equalTo("/g"));
        assertThat(aScheme().relativeReference(Path.rootlessPath("g.")).asString(), equalTo("g."));
        assertThat(aScheme().relativeReference(Path.rootlessPath(".g")).asString(), equalTo(".g"));
        assertThat(aScheme().relativeReference(Path.rootlessPath("g..")).asString(), equalTo("g.."));
        assertThat(aScheme().relativeReference(Path.rootlessPath("..g")).asString(), equalTo("..g"));
        assertThat(aScheme().relativeReference(rootlessPath(Segment.<String>dot(), Segment.<String>dotDot(), segment("g"))).asString(), equalTo("../g"));
        assertThat(aScheme().relativeReference(rootlessPath(Segment.<String>dot(), segment("g"), Segment.<String>dot())).asString(), equalTo("g/"));
        assertThat(aScheme().relativeReference(rootlessPath(segment("g"), Segment.<String>dot(), segment("h"))).asString(), equalTo("g/h"));
        assertThat(aScheme().relativeReference(rootlessPath(segment("g"), Segment.<String>dotDot(), segment("h"))).asString(), equalTo("h"));
        assertThat(aScheme().relativeReference(rootlessPath(segment("g;x=1"), Segment.<String>dot(), segment("y"))).asString(), equalTo("g;x=1/y"));
        assertThat(aScheme().relativeReference(rootlessPath(segment("g;x=1"), Segment.<String>dotDot(), segment("y"))).asString(), equalTo("y"));
        assertThat(aScheme().relativeReference(Path.rootlessPath("g"), query("y/./x")).asString(), equalTo("g?y/./x"));
        assertThat(aScheme().relativeReference(Path.rootlessPath("g"), query("y/../x")).asString(), equalTo("g?y/../x"));
        assertThat(aScheme().relativeReference(Path.rootlessPath("g"), fragment("s/./x")).asString(), equalTo("g#s/./x"));
        assertThat(aScheme().relativeReference(Path.rootlessPath("g"), fragment("s/../x")).asString(), equalTo("g#s/../x"));
        assertThat(scheme("http").urin(Path.rootlessPath("g")).asString(), equalTo("http:g"));
    }
}
