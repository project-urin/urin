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

import net.sourceforge.urin.Fragment;
import net.sourceforge.urin.Path;
import net.sourceforge.urin.Segment;
import net.sourceforge.urin.Urin;
import net.sourceforge.urin.scheme.http.Http;
import net.sourceforge.urin.scheme.http.HttpQuery;
import org.junit.Test;

import static net.sourceforge.urin.Authority.authority;
import static net.sourceforge.urin.Fragment.fragment;
import static net.sourceforge.urin.Host.registeredName;
import static net.sourceforge.urin.Path.path;
import static net.sourceforge.urin.Path.rootlessPath;
import static net.sourceforge.urin.Segment.segment;
import static net.sourceforge.urin.scheme.http.Http.HTTP;
import static net.sourceforge.urin.scheme.http.HttpQuery.queryParameter;
import static net.sourceforge.urin.scheme.http.HttpQuery.queryParameters;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class Rfc3986ReferenceResolutionExamplesTest {

    private static final Urin<String, HttpQuery, Fragment<String>> BASE_URI = Http.http(registeredName("a"), Path.path(segment("b"), segment("c"), segment("d;p")), queryParameters(queryParameter("q")));

    @Test
    public void normalExamples() throws Exception {
        // "g:h"           =  "g:h"
        // is g:h a valid relative reference?
//        assertThat(BASE_URI.resolve(urin(scheme("g"), hierarchicalPart(rootlessPath("h")))).asString(), equalTo("g:h"));
        assertThat(BASE_URI.resolve(HTTP.relativeReference(rootlessPath(segment("g")))).asString(), equalTo("http://a/b/c/g"));
        assertThat(BASE_URI.resolve(HTTP.relativeReference(Path.rootlessPath(Segment.<String>dot(), segment("g")))).asString(), equalTo("http://a/b/c/g"));
        assertThat(BASE_URI.resolve(HTTP.relativeReference(Path.rootlessPath("g", ""))).asString(), equalTo("http://a/b/c/g/"));
        assertThat(BASE_URI.resolve(HTTP.relativeReference(path("g"))).asString(), equalTo("http://a/g"));
        assertThat(BASE_URI.resolve(HTTP.relativeReference(authority(registeredName("g")))).asString(), equalTo("http://g"));
        assertThat(BASE_URI.resolve(HTTP.relativeReference(queryParameters(queryParameter("y")))).asString(), equalTo("http://a/b/c/d;p?y"));
        assertThat(BASE_URI.resolve(HTTP.relativeReference(Path.rootlessPath("g"), queryParameters(queryParameter("y")))).asString(), equalTo("http://a/b/c/g?y"));
        assertThat(BASE_URI.resolve(HTTP.relativeReference(fragment("s"))).asString(), equalTo("http://a/b/c/d;p?q#s"));
        assertThat(BASE_URI.resolve(HTTP.relativeReference(Path.rootlessPath("g"), fragment("s"))).asString(), equalTo("http://a/b/c/g#s"));
        assertThat(BASE_URI.resolve(HTTP.relativeReference(Path.rootlessPath("g"), queryParameters(queryParameter("y")), fragment("s"))).asString(), equalTo("http://a/b/c/g?y#s"));
        assertThat(BASE_URI.resolve(HTTP.relativeReference(Path.rootlessPath(";x"))).asString(), equalTo("http://a/b/c/;x"));
        assertThat(BASE_URI.resolve(HTTP.relativeReference(Path.rootlessPath("g;x"))).asString(), equalTo("http://a/b/c/g;x"));
        assertThat(BASE_URI.resolve(HTTP.relativeReference(Path.rootlessPath("g;x"), queryParameters(queryParameter("y")), fragment("s"))).asString(), equalTo("http://a/b/c/g;x?y#s"));
        assertThat(BASE_URI.resolve(HTTP.relativeReference()).asString(), equalTo("http://a/b/c/d;p?q"));
        assertThat(BASE_URI.resolve(HTTP.relativeReference(rootlessPath(Segment.<String>dot()))).asString(), equalTo("http://a/b/c/"));
        assertThat(BASE_URI.resolve(HTTP.relativeReference(rootlessPath(Segment.<String>dot(), segment("")))).asString(), equalTo("http://a/b/c/"));
        assertThat(BASE_URI.resolve(HTTP.relativeReference(rootlessPath(Segment.<String>dotDot()))).asString(), equalTo("http://a/b/"));
        assertThat(BASE_URI.resolve(HTTP.relativeReference(rootlessPath(Segment.<String>dotDot(), segment("")))).asString(), equalTo("http://a/b/"));
        assertThat(BASE_URI.resolve(HTTP.relativeReference(rootlessPath(Segment.<String>dotDot(), segment("g")))).asString(), equalTo("http://a/b/g"));
        assertThat(BASE_URI.resolve(HTTP.relativeReference(rootlessPath(Segment.<String>dotDot(), Segment.<String>dotDot()))).asString(), equalTo("http://a/"));
        assertThat(BASE_URI.resolve(HTTP.relativeReference(rootlessPath(Segment.<String>dotDot(), Segment.<String>dotDot(), segment("")))).asString(), equalTo("http://a/"));
        assertThat(BASE_URI.resolve(HTTP.relativeReference(rootlessPath(Segment.<String>dotDot(), Segment.<String>dotDot(), segment("g")))).asString(), equalTo("http://a/g"));
    }

    @Test
    public void abnormalExamples() throws Exception {
        assertThat(BASE_URI.resolve(HTTP.relativeReference(rootlessPath(Segment.<String>dotDot(), Segment.<String>dotDot(), Segment.<String>dotDot(), segment("g")))).asString(), equalTo("http://a/g"));
        assertThat(BASE_URI.resolve(HTTP.relativeReference(rootlessPath(Segment.<String>dotDot(), Segment.<String>dotDot(), Segment.<String>dotDot(), Segment.<String>dotDot(), segment("g")))).asString(), equalTo("http://a/g"));
        assertThat(BASE_URI.resolve(HTTP.relativeReference(Path.path(Segment.<String>dot(), segment("g")))).asString(), equalTo("http://a/g"));
        assertThat(BASE_URI.resolve(HTTP.relativeReference(Path.path(Segment.<String>dotDot(), segment("g")))).asString(), equalTo("http://a/g"));
        assertThat(BASE_URI.resolve(HTTP.relativeReference(Path.rootlessPath("g."))).asString(), equalTo("http://a/b/c/g."));
        assertThat(BASE_URI.resolve(HTTP.relativeReference(Path.rootlessPath(".g"))).asString(), equalTo("http://a/b/c/.g"));
        assertThat(BASE_URI.resolve(HTTP.relativeReference(Path.rootlessPath("g.."))).asString(), equalTo("http://a/b/c/g.."));
        assertThat(BASE_URI.resolve(HTTP.relativeReference(Path.rootlessPath("..g"))).asString(), equalTo("http://a/b/c/..g"));
        assertThat(BASE_URI.resolve(HTTP.relativeReference(rootlessPath(Segment.<String>dot(), Segment.<String>dotDot(), segment("g")))).asString(), equalTo("http://a/b/g"));
        assertThat(BASE_URI.resolve(HTTP.relativeReference(rootlessPath(Segment.<String>dot(), segment("g"), Segment.<String>dot()))).asString(), equalTo("http://a/b/c/g/"));
        assertThat(BASE_URI.resolve(HTTP.relativeReference(rootlessPath(segment("g"), Segment.<String>dot(), segment("h")))).asString(), equalTo("http://a/b/c/g/h"));
        assertThat(BASE_URI.resolve(HTTP.relativeReference(rootlessPath(segment("g"), Segment.<String>dotDot(), segment("h")))).asString(), equalTo("http://a/b/c/h"));
        assertThat(BASE_URI.resolve(HTTP.relativeReference(rootlessPath(segment("g;x=1"), Segment.<String>dot(), segment("y")))).asString(), equalTo("http://a/b/c/g;x=1/y"));
        assertThat(BASE_URI.resolve(HTTP.relativeReference(rootlessPath(segment("g;x=1"), Segment.<String>dotDot(), segment("y")))).asString(), equalTo("http://a/b/c/y"));
        assertThat(BASE_URI.resolve(HTTP.relativeReference(Path.rootlessPath("g"), queryParameters(queryParameter("y/./x")))).asString(), equalTo("http://a/b/c/g?y/./x"));
        assertThat(BASE_URI.resolve(HTTP.relativeReference(Path.rootlessPath("g"), queryParameters(queryParameter("y/../x")))).asString(), equalTo("http://a/b/c/g?y/../x"));
        assertThat(BASE_URI.resolve(HTTP.relativeReference(Path.rootlessPath("g"), fragment("s/./x"))).asString(), equalTo("http://a/b/c/g#s/./x"));
        assertThat(BASE_URI.resolve(HTTP.relativeReference(Path.rootlessPath("g"), fragment("s/../x"))).asString(), equalTo("http://a/b/c/g#s/../x"));
//        assertThat(BASE_URI.resolve(urin(scheme("http"), hierarchicalPart(rootlessPath("g")))).asString(), equalTo("http:g"));
    }
}