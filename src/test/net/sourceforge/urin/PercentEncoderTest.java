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

import org.junit.Test;

import static net.sourceforge.urin.CharacterSetMembershipFunction.UNRESERVED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class PercentEncoderTest {

    private static final PercentEncoder NON_UNRESERVED_PERCENT_ENCODER = new PercentEncoder(UNRESERVED);

    @Test
    public void canEncodeRfc3986Examples() throws Exception {
        assertThat(NON_UNRESERVED_PERCENT_ENCODER.encode("A"), equalTo("A"));
        assertThat(NON_UNRESERVED_PERCENT_ENCODER.encode("\u00C0"), equalTo("%C3%80"));
        assertThat(NON_UNRESERVED_PERCENT_ENCODER.encode("\u30A2"), equalTo("%E3%82%A2"));
    }
}
