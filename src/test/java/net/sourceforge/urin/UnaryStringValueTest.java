/*
 * Copyright 2017 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.urin;

import org.junit.Test;

import static net.sourceforge.urin.ExceptionAssert.assertThrowsException;

public class UnaryStringValueTest {
    @Test
    public void valueCannotBeNull() throws Exception {
        assertThrowsException("Null value should throw NullPointerException in constructor", NullPointerException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws NullPointerException {
                new UnaryStringValue(null) {
                };
            }
        });
    }
}
