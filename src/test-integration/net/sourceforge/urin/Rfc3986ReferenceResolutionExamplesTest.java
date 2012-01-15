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

import org.junit.Ignore;
import org.junit.Test;

import static net.sourceforge.urin.Fragment.fragment;
import static net.sourceforge.urin.Host.registeredName;
import static net.sourceforge.urin.Query.query;
import static net.sourceforge.urin.RelativeReference.relativeReference;
import static net.sourceforge.urin.Segment.*;
import static net.sourceforge.urin.Segments.rootlessSegments;
import static net.sourceforge.urin.Segments.segments;
import static net.sourceforge.urin.scheme.Http.http;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class Rfc3986ReferenceResolutionExamplesTest {

    private static final Urin BASE_URI = http(registeredName("a"), segments(segment("b"), segment("c"), segment("d;p")), query("q"));

    @Test
    @Ignore
    public void normalExamples() throws Exception {
        // "g:h"           =  "g:h"
        // is g:h a valid relative reference?
//        assertThat(BASE_URI.resolve(urin(scheme("g"), hierarchicalPart(rootlessSegments("h")))).asString(), equalTo("g:h"));
        assertThat(BASE_URI.resolve(relativeReference(rootlessSegments(segment("g")))).asString(), equalTo("http://a/b/c/g"));
        assertThat(BASE_URI.resolve(relativeReference(rootlessSegments(DOT, segment("g")))).asString(), equalTo("http://a/b/c/g"));
        assertThat(BASE_URI.resolve(relativeReference(rootlessSegments("g", ""))).asString(), equalTo("http://a/b/c/g/"));
        assertThat(BASE_URI.resolve(relativeReference(segments("g"))).asString(), equalTo("http://a/g"));
        assertThat(BASE_URI.resolve(relativeReference(segments("", "g"))).asString(), equalTo("http://g"));
        assertThat(BASE_URI.resolve(relativeReference(query("y"))).asString(), equalTo("http://a/b/c/d;p?y"));
        assertThat(BASE_URI.resolve(relativeReference(rootlessSegments("g"), query("y"))).asString(), equalTo("http://a/b/c/g?y"));
        assertThat(BASE_URI.resolve(relativeReference(fragment("s"))).asString(), equalTo("http://a/b/c/d;p?q#s"));
        assertThat(BASE_URI.resolve(relativeReference(rootlessSegments("g"), fragment("s"))).asString(), equalTo("http://a/b/c/g#s"));
        assertThat(BASE_URI.resolve(relativeReference(rootlessSegments("g"), query("y"), fragment("s"))).asString(), equalTo("http://a/b/c/g?y#s"));
        assertThat(BASE_URI.resolve(relativeReference(rootlessSegments(";x"))).asString(), equalTo("http://a/b/c/;x"));
        assertThat(BASE_URI.resolve(relativeReference(rootlessSegments("g;x"))).asString(), equalTo("http://a/b/c/g;x"));
        assertThat(BASE_URI.resolve(relativeReference(rootlessSegments("g;x"), query("y"), fragment("s"))).asString(), equalTo("http://a/b/c/g;x?y#s"));
        assertThat(BASE_URI.resolve(relativeReference()).asString(), equalTo("http://a/b/c/d;p?q"));
        assertThat(BASE_URI.resolve(relativeReference(rootlessSegments(DOT))).asString(), equalTo("http://a/b/c/"));
        assertThat(BASE_URI.resolve(relativeReference(rootlessSegments(DOT, segment("")))).asString(), equalTo("http://a/b/c/"));
        assertThat(BASE_URI.resolve(relativeReference(rootlessSegments(DOT_DOT))).asString(), equalTo("http://a/b/"));
        assertThat(BASE_URI.resolve(relativeReference(rootlessSegments(DOT_DOT, segment("")))).asString(), equalTo("http://a/b/"));
        assertThat(BASE_URI.resolve(relativeReference(rootlessSegments(DOT_DOT, segment("g")))).asString(), equalTo("http://a/b/g"));
        assertThat(BASE_URI.resolve(relativeReference(rootlessSegments(DOT_DOT, DOT_DOT))).asString(), equalTo("http://a/"));
        assertThat(BASE_URI.resolve(relativeReference(rootlessSegments(DOT_DOT, DOT_DOT, segment("")))).asString(), equalTo("http://a/"));
        assertThat(BASE_URI.resolve(relativeReference(rootlessSegments(DOT_DOT, DOT_DOT, segment("g")))).asString(), equalTo("http://a/g"));
    }

    @Test
    @Ignore
    public void abnormalExamples() throws Exception {
        assertThat(BASE_URI.resolve(relativeReference(rootlessSegments(DOT_DOT, DOT_DOT, DOT_DOT, segment("g")))).asString(), equalTo("http://a/g"));
        assertThat(BASE_URI.resolve(relativeReference(rootlessSegments(DOT_DOT, DOT_DOT, DOT_DOT, DOT_DOT, segment("g")))).asString(), equalTo("http://a/g"));
        assertThat(BASE_URI.resolve(relativeReference(segments(DOT, segment("g")))).asString(), equalTo("http://a/g"));
        assertThat(BASE_URI.resolve(relativeReference(segments(DOT_DOT, segment("g")))).asString(), equalTo("http://a/g"));
        assertThat(BASE_URI.resolve(relativeReference(rootlessSegments("g."))).asString(), equalTo("http://a/b/c/g."));
        assertThat(BASE_URI.resolve(relativeReference(rootlessSegments(".g"))).asString(), equalTo("http://a/b/c/.g"));
        assertThat(BASE_URI.resolve(relativeReference(rootlessSegments("g.."))).asString(), equalTo("http://a/b/c/g.."));
        assertThat(BASE_URI.resolve(relativeReference(rootlessSegments("..g"))).asString(), equalTo("http://a/b/c/..g"));
        assertThat(BASE_URI.resolve(relativeReference(rootlessSegments(DOT, DOT_DOT, segment("g")))).asString(), equalTo("http://a/b/g"));
        assertThat(BASE_URI.resolve(relativeReference(rootlessSegments(DOT, segment("g"), DOT))).asString(), equalTo("http://a/b/c/g/"));
        assertThat(BASE_URI.resolve(relativeReference(rootlessSegments(segment("g"), DOT, segment("h")))).asString(), equalTo("http://a/b/c/g/h"));
        assertThat(BASE_URI.resolve(relativeReference(rootlessSegments(segment("g"), DOT_DOT, segment("h")))).asString(), equalTo("http://a/b/c/h"));
        assertThat(BASE_URI.resolve(relativeReference(rootlessSegments(segment("g;x=1"), DOT, segment("y")))).asString(), equalTo("http://a/b/c/g;x=1/y"));
        assertThat(BASE_URI.resolve(relativeReference(rootlessSegments(segment("g;x=1"), DOT_DOT, segment("y")))).asString(), equalTo("http://a/b/c/y"));
        assertThat(BASE_URI.resolve(relativeReference(rootlessSegments("g"), query("y/./x"))).asString(), equalTo("http://a/b/c/g?y/./x"));
        assertThat(BASE_URI.resolve(relativeReference(rootlessSegments("g"), query("y/../x"))).asString(), equalTo("http://a/b/c/g?y/../x"));
        assertThat(BASE_URI.resolve(relativeReference(rootlessSegments("g"), fragment("s/./x"))).asString(), equalTo("http://a/b/c/g#s/./x"));
        assertThat(BASE_URI.resolve(relativeReference(rootlessSegments("g"), fragment("s/../x"))).asString(), equalTo("http://a/b/c/g#s/../x"));
//        assertThat(BASE_URI.resolve(urin(scheme("http"), hierarchicalPart(rootlessSegments("g")))).asString(), equalTo("http:g"));
    }
}
