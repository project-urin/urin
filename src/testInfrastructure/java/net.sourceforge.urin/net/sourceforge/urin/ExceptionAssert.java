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

import static org.junit.jupiter.api.Assertions.fail;

public final class ExceptionAssert {
    public static <T extends Exception> void assertThrowsException(final String message, final Class<T> exceptionClass, final ExceptionThrower exceptionThrower) throws Exception {
        try {
            exceptionThrower.execute();
            fail(message);
        } catch (Exception e) {
            if (!exceptionClass.isInstance(e)) {
                throw e;
            }
        }
    }

    public interface ExceptionThrower {
        void execute() throws Exception;
    }
}
