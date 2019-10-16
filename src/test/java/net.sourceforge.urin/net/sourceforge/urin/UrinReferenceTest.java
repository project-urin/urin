/*
 * Copyright 2019 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.urin;

import org.junit.jupiter.api.Test;

import java.net.URI;

import static net.sourceforge.urin.ExceptionAssert.assertThrowsException;
import static net.sourceforge.urin.RelativeReferenceBuilder.anUnpollutedRelativeReference;
import static net.sourceforge.urin.SchemeBuilder.aScheme;
import static net.sourceforge.urin.UrinBuilder.anUnpollutedUrin;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class UrinReferenceTest {

    @Test
    public void aUriAsStringParsesToAUrin() throws Exception {
        Urin<String, Query<String>, Fragment<String>> urin = anUnpollutedUrin();
        assertThat(aScheme().parseUrinReference(urin.asString()), equalTo((UrinReference<String, Query<String>, Fragment<String>>) urin));
    }

    @Test
    public void aUriRoundTripsAsUrinReference() throws Exception {
        URI uriReference = URI.create("http://some.where/some/thing");
        assertThat(aScheme().parseUrinReference(uriReference).asUri(), equalTo(uriReference));
    }

    @Test
    public void aRelativeReferenceAsStringParsesToARelativeReference() throws Exception {
        RelativeReference<String, Query<String>, Fragment<String>> relativeReference = anUnpollutedRelativeReference();
        assertThat(aScheme().parseUrinReference(relativeReference.asString()), equalTo((UrinReference<String, Query<String>, Fragment<String>>) relativeReference));
    }

    @Test
    public void anInvalidStringThrowsAParseException() throws Exception {
        assertThrowsException("Invalid scheme should throw parse exception", ParseException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws ParseException {
                aScheme().parseUrinReference("cache_object://");
            }
        });
    }

    @Test
    public void parsingNullThrowsNullPointerException() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        assertThrowsException("Null value should throw NullPointerException in parser", NullPointerException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws Exception {
                scheme.parseUrinReference((String) null);
            }
        });
    }

    @Test
    public void anEmptyStringParsesToARelativeReferenceOfEmptyPath() throws Exception {
        final UrinReference<String, Query<String>, Fragment<String>> urinReference = aScheme().parseUrinReference("");
        assertThat(urinReference.asString(), equalTo(""));
    }
}
