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

import java.net.URI;

import static net.sourceforge.urin.RelativeReferenceBuilder.anUnpollutedRelativeReference;
import static net.sourceforge.urin.SchemeBuilder.aScheme;
import static net.sourceforge.urin.UrinBuilder.anUnpollutedUrin;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.fail;

public class UrinReferenceTest {

    @Test
    public void aUriAsStringParsesToAUrin() throws Exception {
        Urin<String, Query<String>> urin = anUnpollutedUrin();
        assertThat(aScheme().parseUrinReference(urin.asString()), equalTo((UrinReference<String, Query<String>>) urin));
    }

    @Test
    public void aUriRoundTripsAsUrinReference() throws Exception {
        URI uriReference = URI.create("http://some.where/some/thing");
        assertThat(aScheme().parseUrinReference(uriReference).asUri(), equalTo(uriReference));
    }

    @Test
    public void aRelativeReferenceAsStringParsesToARelativeReference() throws Exception {
        RelativeReference<String, Query<String>> relativeReference = anUnpollutedRelativeReference();
        assertThat(aScheme().parseUrinReference(relativeReference.asString()), equalTo((UrinReference<String, Query<String>>) relativeReference));
    }

    @Test
    public void anInvalidStringThrowsAParseException() throws Exception {
        try {
            aScheme().parseUrinReference("cache_object://");
            fail("Should have thrown a ParseException");
        } catch (ParseException e) {
        }
    }
}
