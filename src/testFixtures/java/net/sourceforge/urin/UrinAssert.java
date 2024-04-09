/*
 * Copyright 2024 Mark Slater
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
import static org.hamcrest.MatcherAssert.assertThat;

public final class UrinAssert {

    private UrinAssert() {
    }

    public static <SEGMENT, QUERY extends Query<?>, FRAGMENT extends Fragment<?>> void assertAsStringAndParse(final Scheme<SEGMENT, QUERY, FRAGMENT> scheme, final String stringRepresentation, final Urin<SEGMENT, QUERY, FRAGMENT> urinRepresentation) throws ParseException {
        assertThat(urinRepresentation.asString(), equalTo(stringRepresentation));
        assertThat(scheme.parseUrin(stringRepresentation), equalTo(urinRepresentation));
    }

    public static <SEGMENT, QUERY extends Query<?>, FRAGMENT extends Fragment<?>> void assertAsStringAsUriAndParse(final Scheme<SEGMENT, QUERY, FRAGMENT> scheme, final String stringRepresentation, final Urin<SEGMENT, QUERY, FRAGMENT> urinRepresentation) throws URISyntaxException, ParseException {
        assertAsStringAndParse(scheme, stringRepresentation, urinRepresentation);
        assertThat(urinRepresentation.asUri(), equalTo(new URI(stringRepresentation)));
        assertThat(scheme.parseUrin(new URI(stringRepresentation)), equalTo(urinRepresentation));
    }
}
