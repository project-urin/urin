/*
 * Copyright 2024 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.urin.scheme.http;

import net.sourceforge.urin.AbsolutePath;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import static net.sourceforge.urin.Authority.authority;
import static net.sourceforge.urin.HostBuilder.anIpV4Address;
import static net.sourceforge.urin.scheme.http.Http.HTTP;

public final class QueryMatcher {
    private QueryMatcher() {
    }

    public static Matcher<HttpQuery> convertsToQueryString(final Matcher<String> expected) {
        return new TypeSafeDiagnosingMatcher<>() {
            @Override
            protected boolean matchesSafely(final HttpQuery query, final Description description) {
                final String rawQuery = HTTP.urin(authority(anIpV4Address()), AbsolutePath.path(), query).asUri().getRawQuery();
                final boolean matches = expected.matches(rawQuery);
                if (!matches) {
                    description.appendText("got a Query that as uri String is ").appendValue(rawQuery);
                }
                return matches;
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("a Query that as a uri String is ").appendDescriptionOf(expected);
            }
        };
    }
}
