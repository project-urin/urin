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

import static net.sourceforge.urin.Hexadectet.hexadectet;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class HexadectetTest {
    @Test
    public void asStringReturnsGivenValue() throws Exception {
        assertThat(hexadectet(0x0).asString(), equalTo("0"));
        assertThat(hexadectet(0xFFFF).asString(), equalTo("ffff"));
    }

    @Test
    public void rejectsIntsOutsideValidRange() throws Exception {
        try {
            hexadectet(-0x1);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("Argument must be in the range 0x0-0xFFFF but was [-1]"));
        }
        try {
            hexadectet(0x10000);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("Argument must be in the range 0x0-0xFFFF but was [65536]"));
        }
    }
}
