/*
 * Copyright 2012 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.urin.scheme.http;

import net.sourceforge.urin.ExceptionAssert;
import org.junit.Test;

import static junit.framework.Assert.fail;
import static net.sourceforge.urin.ExceptionAssert.assertThrowsException;
import static net.sourceforge.urin.MoreRandomStringUtils.aString;
import static net.sourceforge.urin.scheme.http.HttpQuery.queryParameter;
import static net.sourceforge.urin.scheme.http.HttpQuery.queryParameters;

public class HttpQueryTest {
    @Test
    public void rejectsNullQueryParameter() throws Exception {
        assertThrowsException("Null name should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                HttpQuery.QueryParameter queryParameter = null;
                queryParameters(queryParameter);
            }
        });
    }

    @Test
    public void rejectsNullNameOnlyQueryParameter() throws Exception {
        assertThrowsException("Null name should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                queryParameter(null);
            }
        });
    }

    @Test
    public void rejectsNullsInNameAndValueQueryParameter() throws Exception {
        assertThrowsException("Null name should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                queryParameter(null, aString());
            }
        });
        assertThrowsException("Null value should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                queryParameter(aString(), null);
            }
        });
    }

    @Test
    public void equalsToStringHashCode() throws Exception {
        fail("Not implemented yet");
    }
}
