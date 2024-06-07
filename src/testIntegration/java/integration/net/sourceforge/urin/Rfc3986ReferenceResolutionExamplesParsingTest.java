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

import net.sourceforge.urin.ParseException;
import org.junit.jupiter.api.Test;

import static net.sourceforge.urin.scheme.http.Http.HTTP;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

class Rfc3986ReferenceResolutionExamplesParsingTest {

    @Test
    void normalExamples() throws ParseException {
        assertThat(HTTP.parseUrin("http://a/b/c/d;p?q").resolve(HTTP.parseUrinReference("g:h")), equalTo(HTTP.parseUrin("g:h")));
        assertThat(HTTP.parseUrin("http://a/b/c/d;p?q").resolve(HTTP.parseUrinReference("g")), equalTo(HTTP.parseUrin("http://a/b/c/g")));
        assertThat(HTTP.parseUrin("http://a/b/c/d;p?q").resolve(HTTP.parseUrinReference("./g")), equalTo(HTTP.parseUrin("http://a/b/c/g")));
        assertThat(HTTP.parseUrin("http://a/b/c/d;p?q").resolve(HTTP.parseUrinReference("g/")), equalTo(HTTP.parseUrin("http://a/b/c/g/")));
        assertThat(HTTP.parseUrin("http://a/b/c/d;p?q").resolve(HTTP.parseUrinReference("/g")), equalTo(HTTP.parseUrin("http://a/g")));
        assertThat(HTTP.parseUrin("http://a/b/c/d;p?q").resolve(HTTP.parseUrinReference("//g")), equalTo(HTTP.parseUrin("http://g")));
        assertThat(HTTP.parseUrin("http://a/b/c/d;p?q").resolve(HTTP.parseUrinReference("?y")), equalTo(HTTP.parseUrin("http://a/b/c/d;p?y")));
        assertThat(HTTP.parseUrin("http://a/b/c/d;p?q").resolve(HTTP.parseUrinReference("g?y")), equalTo(HTTP.parseUrin("http://a/b/c/g?y")));
        assertThat(HTTP.parseUrin("http://a/b/c/d;p?q").resolve(HTTP.parseUrinReference("#s")), equalTo(HTTP.parseUrin("http://a/b/c/d;p?q#s")));
        assertThat(HTTP.parseUrin("http://a/b/c/d;p?q").resolve(HTTP.parseUrinReference("g#s")), equalTo(HTTP.parseUrin("http://a/b/c/g#s")));
        assertThat(HTTP.parseUrin("http://a/b/c/d;p?q").resolve(HTTP.parseUrinReference("g?y#s")), equalTo(HTTP.parseUrin("http://a/b/c/g?y#s")));
        assertThat(HTTP.parseUrin("http://a/b/c/d;p?q").resolve(HTTP.parseUrinReference(";x")), equalTo(HTTP.parseUrin("http://a/b/c/;x")));
        assertThat(HTTP.parseUrin("http://a/b/c/d;p?q").resolve(HTTP.parseUrinReference("g;x")), equalTo(HTTP.parseUrin("http://a/b/c/g;x")));
        assertThat(HTTP.parseUrin("http://a/b/c/d;p?q").resolve(HTTP.parseUrinReference("g;x?y#s")), equalTo(HTTP.parseUrin("http://a/b/c/g;x?y#s")));
        assertThat(HTTP.parseUrin("http://a/b/c/d;p?q").resolve(HTTP.parseUrinReference("")), equalTo(HTTP.parseUrin("http://a/b/c/d;p?q")));
        assertThat(HTTP.parseUrin("http://a/b/c/d;p?q").resolve(HTTP.parseUrinReference(".")), equalTo(HTTP.parseUrin("http://a/b/c/")));
        assertThat(HTTP.parseUrin("http://a/b/c/d;p?q").resolve(HTTP.parseUrinReference("./")), equalTo(HTTP.parseUrin("http://a/b/c/")));
        assertThat(HTTP.parseUrin("http://a/b/c/d;p?q").resolve(HTTP.parseUrinReference("..")), equalTo(HTTP.parseUrin("http://a/b/")));
        assertThat(HTTP.parseUrin("http://a/b/c/d;p?q").resolve(HTTP.parseUrinReference("../")), equalTo(HTTP.parseUrin("http://a/b/")));
        assertThat(HTTP.parseUrin("http://a/b/c/d;p?q").resolve(HTTP.parseUrinReference("../g")), equalTo(HTTP.parseUrin("http://a/b/g")));
        assertThat(HTTP.parseUrin("http://a/b/c/d;p?q").resolve(HTTP.parseUrinReference("../..")), equalTo(HTTP.parseUrin("http://a/")));
        assertThat(HTTP.parseUrin("http://a/b/c/d;p?q").resolve(HTTP.parseUrinReference("../../")), equalTo(HTTP.parseUrin("http://a/")));
        assertThat(HTTP.parseUrin("http://a/b/c/d;p?q").resolve(HTTP.parseUrinReference("../../g")), equalTo(HTTP.parseUrin("http://a/g")));
    }

    @Test
    void abnormalExamples() throws ParseException {
        assertThat(HTTP.parseUrin("http://a/b/c/d;p?q").resolve(HTTP.parseUrinReference("../../../g")), equalTo(HTTP.parseUrin("http://a/g")));
        assertThat(HTTP.parseUrin("http://a/b/c/d;p?q").resolve(HTTP.parseUrinReference("../../../../g")), equalTo(HTTP.parseUrin("http://a/g")));
        assertThat(HTTP.parseUrin("http://a/b/c/d;p?q").resolve(HTTP.parseUrinReference("/./g")), equalTo(HTTP.parseUrin("http://a/g")));
        assertThat(HTTP.parseUrin("http://a/b/c/d;p?q").resolve(HTTP.parseUrinReference("/../g")), equalTo(HTTP.parseUrin("http://a/g")));
        assertThat(HTTP.parseUrin("http://a/b/c/d;p?q").resolve(HTTP.parseUrinReference("g.")), equalTo(HTTP.parseUrin("http://a/b/c/g.")));
        assertThat(HTTP.parseUrin("http://a/b/c/d;p?q").resolve(HTTP.parseUrinReference(".g")), equalTo(HTTP.parseUrin("http://a/b/c/.g")));
        assertThat(HTTP.parseUrin("http://a/b/c/d;p?q").resolve(HTTP.parseUrinReference("g..")), equalTo(HTTP.parseUrin("http://a/b/c/g..")));
        assertThat(HTTP.parseUrin("http://a/b/c/d;p?q").resolve(HTTP.parseUrinReference("..g")), equalTo(HTTP.parseUrin("http://a/b/c/..g")));
        assertThat(HTTP.parseUrin("http://a/b/c/d;p?q").resolve(HTTP.parseUrinReference("./../g")), equalTo(HTTP.parseUrin("http://a/b/g")));
        assertThat(HTTP.parseUrin("http://a/b/c/d;p?q").resolve(HTTP.parseUrinReference("./g/.")), equalTo(HTTP.parseUrin("http://a/b/c/g/")));
        assertThat(HTTP.parseUrin("http://a/b/c/d;p?q").resolve(HTTP.parseUrinReference("g/./h")), equalTo(HTTP.parseUrin("http://a/b/c/g/h")));
        assertThat(HTTP.parseUrin("http://a/b/c/d;p?q").resolve(HTTP.parseUrinReference("g/../h")), equalTo(HTTP.parseUrin("http://a/b/c/h")));
        assertThat(HTTP.parseUrin("http://a/b/c/d;p?q").resolve(HTTP.parseUrinReference("g;x=1/./y")), equalTo(HTTP.parseUrin("http://a/b/c/g;x=1/y")));
        assertThat(HTTP.parseUrin("http://a/b/c/d;p?q").resolve(HTTP.parseUrinReference("g;x=1/../y")), equalTo(HTTP.parseUrin("http://a/b/c/y")));
        assertThat(HTTP.parseUrin("http://a/b/c/d;p?q").resolve(HTTP.parseUrinReference("g?y/./x")), equalTo(HTTP.parseUrin("http://a/b/c/g?y/./x")));
        assertThat(HTTP.parseUrin("http://a/b/c/d;p?q").resolve(HTTP.parseUrinReference("g?y/../x")), equalTo(HTTP.parseUrin("http://a/b/c/g?y/../x")));
        assertThat(HTTP.parseUrin("http://a/b/c/d;p?q").resolve(HTTP.parseUrinReference("g#s/./x")), equalTo(HTTP.parseUrin("http://a/b/c/g#s/./x")));
        assertThat(HTTP.parseUrin("http://a/b/c/d;p?q").resolve(HTTP.parseUrinReference("g#s/../x")), equalTo(HTTP.parseUrin("http://a/b/c/g#s/../x")));
        assertThat(HTTP.parseUrin("http://a/b/c/d;p?q").resolve(HTTP.parseUrinReference("http:g")), equalTo(HTTP.parseUrin("http:g")));
    }

}
