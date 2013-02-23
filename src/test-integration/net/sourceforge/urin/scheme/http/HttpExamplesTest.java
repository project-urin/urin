/*
 * Copyright 2013 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.urin.scheme.http;

import net.sourceforge.urin.AbsolutePath;
import org.junit.Test;

import static net.sourceforge.urin.Fragment.fragment;
import static net.sourceforge.urin.Host.registeredName;
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
        assertThat(http(registeredName("urin.sourceforge.net"), AbsolutePath.path("javadoc"), HttpQuery.queryParameters(HttpQuery.queryParameter("Bobby", "Dazzler"))).asString(), equalTo("http://urin.sourceforge.net/javadoc?Bobby=Dazzler"));
    }

    @Test
    public void aUrinWithEmptyVarargsHttpParametersHasNoQueryPart() throws Exception {
        assertThat(http(registeredName("urin.sourceforge.net"), AbsolutePath.path("javadoc"), queryParameters(new HttpQuery.QueryParameter[]{})).asString(), equalTo("http://urin.sourceforge.net/javadoc"));
    }

    @Test
    public void canGenerateAnHttpUriWithAFragment() throws Exception {
        assertThat(http(registeredName("urin.sourceforge.net"), AbsolutePath.path("javadoc"), fragment("the first bit")).asString(), equalTo("http://urin.sourceforge.net/javadoc#the%20first%20bit"));
    }
}
