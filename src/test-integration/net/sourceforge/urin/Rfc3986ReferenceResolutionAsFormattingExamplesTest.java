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

import org.junit.Test;

import static net.sourceforge.urin.Authority.authority;
import static net.sourceforge.urin.Fragment.fragment;
import static net.sourceforge.urin.HierarchicalPart.hierarchicalPart;
import static net.sourceforge.urin.Host.registeredName;
import static net.sourceforge.urin.Query.query;
import static net.sourceforge.urin.RelativeReference.relativeReference;
import static net.sourceforge.urin.Scheme.scheme;
import static net.sourceforge.urin.Segment.*;
import static net.sourceforge.urin.Segments.rootlessSegments;
import static net.sourceforge.urin.Segments.segments;
import static net.sourceforge.urin.Urin.urin;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class Rfc3986ReferenceResolutionAsFormattingExamplesTest {

    @Test
    public void normalExamples() throws Exception {
        assertThat(urin(scheme("g"), hierarchicalPart(rootlessSegments("h"))).asString(), equalTo("g:h"));
        assertThat(relativeReference(rootlessSegments(segment("g"))).asString(), equalTo("g"));
        assertThat(relativeReference(rootlessSegments(DOT, segment("g"))).asString(), equalTo("g"));
        assertThat(relativeReference(rootlessSegments("g", "")).asString(), equalTo("g/"));
        assertThat(relativeReference(segments("g")).asString(), equalTo("/g"));
        assertThat(relativeReference(authority(registeredName("g"))).asString(), equalTo("//g"));
        assertThat(relativeReference(query("y")).asString(), equalTo("?y"));
        assertThat(relativeReference(rootlessSegments("g"), query("y")).asString(), equalTo("g?y"));
        assertThat(relativeReference(fragment("s")).asString(), equalTo("#s"));
        assertThat(relativeReference(rootlessSegments("g"), fragment("s")).asString(), equalTo("g#s"));
        assertThat(relativeReference(rootlessSegments("g"), query("y"), fragment("s")).asString(), equalTo("g?y#s"));
        assertThat(relativeReference(rootlessSegments(";x")).asString(), equalTo(";x"));
        assertThat(relativeReference(rootlessSegments("g;x")).asString(), equalTo("g;x"));
        assertThat(relativeReference(rootlessSegments("g;x"), query("y"), fragment("s")).asString(), equalTo("g;x?y#s"));
        assertThat(relativeReference().asString(), equalTo(""));
        assertThat(relativeReference(rootlessSegments(DOT)).asString(), equalTo("."));
        assertThat(relativeReference(rootlessSegments(DOT, segment(""))).asString(), equalTo("./"));
        assertThat(relativeReference(rootlessSegments(DOT_DOT)).asString(), equalTo(".."));
        assertThat(relativeReference(rootlessSegments(DOT_DOT, segment(""))).asString(), equalTo("../"));
        assertThat(relativeReference(rootlessSegments(DOT_DOT, segment("g"))).asString(), equalTo("../g"));
        assertThat(relativeReference(rootlessSegments(DOT_DOT, DOT_DOT)).asString(), equalTo("../.."));
        assertThat(relativeReference(rootlessSegments(DOT_DOT, DOT_DOT, segment(""))).asString(), equalTo("../../"));
        assertThat(relativeReference(rootlessSegments(DOT_DOT, DOT_DOT, segment("g"))).asString(), equalTo("../../g"));
    }

    @Test
    public void abnormalExamples() throws Exception {
        assertThat(relativeReference(rootlessSegments(DOT_DOT, DOT_DOT, DOT_DOT, segment("g"))).asString(), equalTo("../../../g"));
        assertThat(relativeReference(rootlessSegments(DOT_DOT, DOT_DOT, DOT_DOT, DOT_DOT, segment("g"))).asString(), equalTo("../../../../g"));
        assertThat(relativeReference(segments(DOT, segment("g"))).asString(), equalTo("/g"));
        assertThat(relativeReference(segments(DOT_DOT, segment("g"))).asString(), equalTo("/g"));
        assertThat(relativeReference(rootlessSegments("g.")).asString(), equalTo("g."));
        assertThat(relativeReference(rootlessSegments(".g")).asString(), equalTo(".g"));
        assertThat(relativeReference(rootlessSegments("g..")).asString(), equalTo("g.."));
        assertThat(relativeReference(rootlessSegments("..g")).asString(), equalTo("..g"));
        assertThat(relativeReference(rootlessSegments(DOT, DOT_DOT, segment("g"))).asString(), equalTo("../g"));
        assertThat(relativeReference(rootlessSegments(DOT, segment("g"), DOT)).asString(), equalTo("g/"));
        assertThat(relativeReference(rootlessSegments(segment("g"), DOT, segment("h"))).asString(), equalTo("g/h"));
        assertThat(relativeReference(rootlessSegments(segment("g"), DOT_DOT, segment("h"))).asString(), equalTo("h"));
        assertThat(relativeReference(rootlessSegments(segment("g;x=1"), DOT, segment("y"))).asString(), equalTo("g;x=1/y"));
        assertThat(relativeReference(rootlessSegments(segment("g;x=1"), DOT_DOT, segment("y"))).asString(), equalTo("y"));
        assertThat(relativeReference(rootlessSegments("g"), query("y/./x")).asString(), equalTo("g?y/./x"));
        assertThat(relativeReference(rootlessSegments("g"), query("y/../x")).asString(), equalTo("g?y/../x"));
        assertThat(relativeReference(rootlessSegments("g"), fragment("s/./x")).asString(), equalTo("g#s/./x"));
        assertThat(relativeReference(rootlessSegments("g"), fragment("s/../x")).asString(), equalTo("g#s/../x"));
        assertThat(urin(scheme("http"), hierarchicalPart(rootlessSegments("g"))).asString(), equalTo("http:g"));
    }
}
