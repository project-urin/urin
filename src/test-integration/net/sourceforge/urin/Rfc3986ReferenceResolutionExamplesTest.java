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

import static net.sourceforge.urin.Host.registeredName;
import static net.sourceforge.urin.Query.query;
import static net.sourceforge.urin.RelativeReference.relativeReference;
import static net.sourceforge.urin.Segment.DOT;
import static net.sourceforge.urin.Segment.segment;
import static net.sourceforge.urin.Segments.rootlessSegments;
import static net.sourceforge.urin.Segments.segments;
import static net.sourceforge.urin.scheme.Http.http;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class Rfc3986ReferenceResolutionExamplesTest {

    private static final Urin BASE_URI = http(registeredName("a"), segments(segment("b"), segment("c"), segment("d;p")), query("q"));

    //    "g/"            =  "http://a/b/c/g/"
    //    "/g"            =  "http://a/g"
    //    "//g"           =  "http://g"
    //    "?y"            =  "http://a/b/c/d;p?y"
    //    "g?y"           =  "http://a/b/c/g?y"
    //    "#s"            =  "http://a/b/c/d;p?q#s"
    //    "g#s"           =  "http://a/b/c/g#s"
    //    "g?y#s"         =  "http://a/b/c/g?y#s"
    //    ";x"            =  "http://a/b/c/;x"
    //    "g;x"           =  "http://a/b/c/g;x"
    //    "g;x?y#s"       =  "http://a/b/c/g;x?y#s"
    //    ""              =  "http://a/b/c/d;p?q"
    //    "."             =  "http://a/b/c/"
    //    "./"            =  "http://a/b/c/"
    //    ".."            =  "http://a/b/"
    //    "../"           =  "http://a/b/"
    //    "../g"          =  "http://a/b/g"
    //    "../.."         =  "http://a/"
    //    "../../"        =  "http://a/"
    //    "../../g"       =  "http://a/g"

    @Test
    @Ignore
    public void normalExamples() throws Exception {
        // "g:h"           =  "g:h"
        // is g:h a valid relative reference?
        assertThat(BASE_URI.resolve(relativeReference(rootlessSegments(segment("g")))).asString(), equalTo("http://a/b/c/g"));
        assertThat(BASE_URI.resolve(relativeReference(rootlessSegments(DOT, segment("g")))).asString(), equalTo("http://a/b/c/g"));
    }

    //    "../../../g"    =  "http://a/g"
    //    "../../../../g" =  "http://a/g"
    //    "/./g"          =  "http://a/g"
    //    "/../g"         =  "http://a/g"
    //    "g."            =  "http://a/b/c/g."
    //    ".g"            =  "http://a/b/c/.g"
    //    "g.."           =  "http://a/b/c/g.."
    //    "..g"           =  "http://a/b/c/..g"
    //    "./../g"        =  "http://a/b/g"
    //    "./g/."         =  "http://a/b/c/g/"
    //    "g/./h"         =  "http://a/b/c/g/h"
    //    "g/../h"        =  "http://a/b/c/h"
    //    "g;x=1/./y"     =  "http://a/b/c/g;x=1/y"
    //    "g;x=1/../y"    =  "http://a/b/c/y"
    //    "g?y/./x"       =  "http://a/b/c/g?y/./x"
    //    "g?y/../x"      =  "http://a/b/c/g?y/../x"
    //    "g#s/./x"       =  "http://a/b/c/g#s/./x"
    //    "g#s/../x"      =  "http://a/b/c/g#s/../x"
    //    "http:g"        =  "http:g"         ; for strict parsers
    //                    /  "http://a/b/c/g" ; for backward compatibility
}
