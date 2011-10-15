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

import static net.sourceforge.urin.DecimalOctet.decimalOctet;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class DecimalOctetTest {
    @Test
    public void asStringReturnsTheArgument() throws Exception {
        assertThat(decimalOctet("123").asString(), equalTo("123"));
    }

    @Test
    public void asStringReturnsTheArgumentWhenUsingAnInt() throws Exception {
        assertThat(decimalOctet(0).asString(), equalTo("0"));
        assertThat(decimalOctet(255).asString(), equalTo("255"));
    }

    @Test
    public void rejectsIntegerArgumentsOutsideRange() throws Exception {
        try {
            decimalOctet(-1);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("Argument must be in the range 0-255 but was [-1]"));
        }
        try {
            decimalOctet(256);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("Argument must be in the range 0-255 but was [256]"));
        }
    }
}
