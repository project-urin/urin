/*
 * Copyright 2013 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.urin;

import org.junit.Test;

import java.util.ArrayList;

import static java.util.Arrays.asList;
import static net.sourceforge.urin.PercentEncodingPartial.PercentEncoding.percentEncodingString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class PercentEncodingUnaryValueTest {

    @Test
    public void canChainApplicationsOfPercentEncodingDelimitedValue() throws Exception {
        assertThat(
                PercentEncodingPartial.<Iterable<String>, String>percentEncodingDelimitedValue('a',
                        PercentEncodingPartial.<String, String>percentEncodingDelimitedValue('b'))
                        .apply(percentEncodingString(PercentEncoder.ENCODE_EVERYTHING))
                        .encode(new ArrayList<Iterable<String>>(2) {{
                            add(asList("c", "d"));
                            add(asList("c", "d"));
                        }}),
                equalTo("%63b%64a%63b%64"));
    }
}
