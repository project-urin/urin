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

import org.junit.jupiter.api.Test;

import static net.sourceforge.urin.CharacterSets.QUERY_AND_FRAGMENT_CHARACTERS;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

class QueryTest {

    @Test
    void asStringReturnsValueProvidedForUnreservedCharacters() {
        assertThat(Query.query(QUERY_AND_FRAGMENT_CHARACTERS).asString(), equalTo(QUERY_AND_FRAGMENT_CHARACTERS));
    }

    @Test
    void asStringPercentEncodesNonUnreservedCharacters() {
        assertThat(Query.query(".#.[.]. .").asString(), equalTo(".%23.%5B.%5D.%20."));
    }

    @Test
    void parsesUnreservedCharacters() throws Exception {
        assertThat(Query.parseQuery(QUERY_AND_FRAGMENT_CHARACTERS, Query.stringQueryMaker()), equalTo(Query.query(QUERY_AND_FRAGMENT_CHARACTERS)));
    }

    @Test
    void parsesNonUnreservedCharacters() throws Exception {
        assertThat(Query.parseQuery(".%23.%5B.%5D.%20.", Query.stringQueryMaker()), equalTo(Query.query(".#.[.]. .")));
    }

}
