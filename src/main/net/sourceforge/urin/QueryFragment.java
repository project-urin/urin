/*
 * Copyright 2011 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.urin;

import static net.sourceforge.urin.CharacterSetMembershipFunction.QUERY_AND_FRAGMENT_NON_PERCENT_ENCODED_CHARACTERS;

abstract class QueryFragment {

    private static final PercentEncoder PERCENT_ENCODER = new PercentEncoder(QUERY_AND_FRAGMENT_NON_PERCENT_ENCODED_CHARACTERS);

    private final String content;

    QueryFragment(final String content) {
        this.content = content;
    }

    public String asString() {
        return PERCENT_ENCODER.encode(content);
    }
}