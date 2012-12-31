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

import java.net.URI;
import java.net.URISyntaxException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class UrinAssert {
    public static void assertAsStringAsUriAndParse(Scheme scheme, final String stringRepresentation, final Urin urinRepresentation) throws URISyntaxException, ParseException {
        assertThat(urinRepresentation.asString(), equalTo(stringRepresentation));
        assertThat(urinRepresentation.asUri(), equalTo(new URI(stringRepresentation)));
        assertThat(scheme.parseUrin(stringRepresentation), equalTo(urinRepresentation));
        assertThat(scheme.parseUrin(new URI(stringRepresentation)), equalTo(urinRepresentation));
    }

    public static void assertAsStringAndParse(final Scheme scheme, final String stringRepresentation, final Urin urinRepresentation) throws ParseException {
        assertThat(urinRepresentation.asString(), equalTo(stringRepresentation));
        assertThat(scheme.parseUrin(stringRepresentation), equalTo(urinRepresentation));
    }
}
