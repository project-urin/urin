/*
 * Copyright 2011 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.urin.scheme;

import net.sourceforge.urin.Segments;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import static net.sourceforge.urin.HostBuilder.aHost;
import static net.sourceforge.urin.scheme.Http.http;

public class QueryParametersMatcher {
    public static Matcher<Http.QueryParameters> convertsToQueryString(final Matcher<String> expected) {
        return new TypeSafeDiagnosingMatcher<Http.QueryParameters>() {
            @Override
            protected boolean matchesSafely(final Http.QueryParameters queryParameters, final Description description) {
                String rawQuery = http(aHost(), Segments.segments(), queryParameters).asUri().getRawQuery();
                boolean matches = expected.matches(rawQuery);
                if (!matches) {
                    description.appendText("got a QueryParameters that as uri String is ").appendValue(rawQuery);
                }
                return matches;
            }

            public void describeTo(final Description description) {
                description.appendText("a QueryParameters that as a uri String is ").appendDescriptionOf(expected);
            }
        };
    }
}
