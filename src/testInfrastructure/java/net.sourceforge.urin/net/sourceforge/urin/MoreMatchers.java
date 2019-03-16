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

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import java.util.ArrayList;

public class MoreMatchers {
    private MoreMatchers() {
        // deliberately empty
    }

    @SafeVarargs
    public static <T> Matcher<Iterable<? extends T>> contains(final T... contents) {
        return Matchers.contains(new ArrayList<Matcher<? super T>>() {{
            for (T content : contents) {
                add(Matchers.equalTo(content));
            }
        }});
    }
}
