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

import org.junit.jupiter.api.Test;

import static net.sourceforge.urin.ExceptionAssert.assertThrowsException;

class UnaryStringValueTest {
    @Test
    void valueCannotBeNull() throws Exception {
        //noinspection ConstantConditions
        assertThrowsException("Null value should throw NullPointerException in constructor", NullPointerException.class, () -> new UnaryStringValue(null) {
        });
    }
}
