/*
 * Copyright 2017 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package integration.net.sourceforge.urin.scheme.http;

import net.sourceforge.urin.AbsolutePath;
import net.sourceforge.urin.Fragment;
import net.sourceforge.urin.Urin;
import net.sourceforge.urin.scheme.http.Http;
import net.sourceforge.urin.scheme.http.HttpQuery;
import org.hamcrest.Matchers;
import org.junit.Test;

import static net.sourceforge.urin.AuthorityBuilder.anAuthority;
import static net.sourceforge.urin.Fragment.fragment;
import static net.sourceforge.urin.Host.registeredName;
import static net.sourceforge.urin.PathBuilder.anAbsolutePath;
import static net.sourceforge.urin.scheme.http.Http.http;
import static net.sourceforge.urin.scheme.http.HttpQuery.queryParameters;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class HttpExamplesTest {
    @Test
    public void canGenerateAnHttpUriWithoutQueryParameters() throws Exception {
        assertThat(http(registeredName("urin.sourceforge.net"), AbsolutePath.path("javadoc")).asString(), equalTo("http://urin.sourceforge.net/javadoc"));
    }

    @Test
    public void canGenerateAnHttpUriWithQueryParameters() throws Exception {
        assertThat(Http.http(registeredName("urin.sourceforge.net"), AbsolutePath.path("javadoc"), HttpQuery.queryParameters(HttpQuery.queryParameter("Bobby", "Dazzler"))).asString(), equalTo("http://urin.sourceforge.net/javadoc?Bobby=Dazzler"));
    }

    @Test
    public void aUrinWithEmptyVarargsHttpParametersHasEmptyQueryPart() throws Exception {
        final Urin<String, HttpQuery, Fragment<String>> uriWithEmptyParameters = http(
                anAuthority(),
                anAbsolutePath(),
                queryParameters(new HttpQuery.QueryParameter[]{})
        );
        assertThat(uriWithEmptyParameters.hasQuery(), equalTo(true));
        assertThat(uriWithEmptyParameters.query(), Matchers.<HttpQuery.QueryParameter>emptyIterable());
    }

    @Test
    public void canGenerateAnHttpUriWithAFragment() throws Exception {
        assertThat(http(registeredName("urin.sourceforge.net"), AbsolutePath.path("javadoc"), fragment("the first bit")).asString(), equalTo("http://urin.sourceforge.net/javadoc#the%20first%20bit"));
    }
}
