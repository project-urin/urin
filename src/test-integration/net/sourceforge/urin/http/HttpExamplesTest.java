/*
 * Copyright 2011 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.urin.http;

import net.sourceforge.urin.scheme.Http;
import org.junit.Ignore;
import org.junit.Test;

import static net.sourceforge.urin.Host.registeredName;
import static net.sourceforge.urin.Segments.segments;
import static net.sourceforge.urin.scheme.Http.http;
import static net.sourceforge.urin.scheme.Http.queryParameter;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class HttpExamplesTest {
    @Test
    public void canGenerateAnHttpUriWithoutQueryParameters() throws Exception {
        assertThat(http(registeredName("urin.sourceforge.net"), segments("javadoc")).asString(), equalTo("http://urin.sourceforge.net/javadoc"));
    }

    @Test
    @Ignore
    public void canGenerateAnHttpUriWithQueryParameters() throws Exception {
        assertThat(http(registeredName("urin.sourceforge.net"), segments("javadoc"), Http.queryParameters(queryParameter("Bobby", "Dazzler"))).asString(), equalTo("http://urin.sourceforge.net/javadoc?Bobby=Dazzler"));
    }
}
