/*
 * Copyright 2017 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.urin.scheme.http;

import net.sourceforge.urin.ExceptionAssert;
import net.sourceforge.urin.Query;
import org.junit.Test;

import static java.util.Arrays.asList;
import static net.sourceforge.urin.ExceptionAssert.assertThrowsException;
import static net.sourceforge.urin.MoreRandomStringUtils.aString;
import static net.sourceforge.urin.scheme.http.HttpQuery.queryParameter;
import static net.sourceforge.urin.scheme.http.HttpQuery.queryParameters;
import static net.sourceforge.urin.scheme.http.QueryParameterBuilder.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

public class HttpQueryTest {
    @Test
    public void rejectsNullQueryParameter() throws Exception {
        assertThrowsException("Null name should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                HttpQuery.QueryParameter queryParameter = null;
                queryParameters(queryParameter);
            }
        });
    }

    @Test
    public void queryParametersUsingVarargsAreImmutable() throws Exception {
        HttpQuery.QueryParameter firstQueryParameter = aQueryParameter();
        HttpQuery.QueryParameter secondQueryParameter = aQueryParameter();
        HttpQuery.QueryParameter[] queryParameters = {firstQueryParameter, secondQueryParameter};
        Query query = queryParameters(queryParameters);
        queryParameters[0] = aQueryParameter();
        assertThat(query, equalTo((Query) queryParameters(firstQueryParameter, secondQueryParameter)));

    }

    @Test
    public void queryParametersIsEqualToAnotherWithTheSameMembers() throws Exception {
        HttpQuery.QueryParameter firstQueryParameter = aQueryParameter();
        HttpQuery.QueryParameter secondQueryParameter = aQueryParameter();
        assertThat(queryParameters(firstQueryParameter, secondQueryParameter), equalTo(queryParameters(firstQueryParameter, secondQueryParameter)));
        assertThat(queryParameters(firstQueryParameter, secondQueryParameter).hashCode(), equalTo(queryParameters(firstQueryParameter, secondQueryParameter).hashCode()));
    }

    @Test
    public void queryParametersIsNotEqualToAnotherWithDifferentMembers() throws Exception {
        assertThat(queryParameters(aQueryParameter(), aQueryParameter()), not(equalTo(queryParameters(aQueryParameter(), aQueryParameter()))));
    }

    @Test
    public void queryParametersToStringIsCorrect() throws Exception {
        HttpQuery.QueryParameter firstQueryParameter = aQueryParameter();
        HttpQuery.QueryParameter secondQueryParameter = aQueryParameter();
        assertThat(queryParameters(firstQueryParameter, secondQueryParameter).toString(), equalTo("HttpQuery{value='" + asList(firstQueryParameter, secondQueryParameter) + "'}"));
    }

    @Test
    public void rejectsNullNameOnlyQueryParameter() throws Exception {
        assertThrowsException("Null name should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                queryParameter(null);
            }
        });
    }

    @Test
    public void queryParameterWithNameOnlyIsEqualToAnotherWithTheSameName() throws Exception {
        String name = aString();
        assertThat(queryParameter(name), equalTo(queryParameter(name)));
        assertThat(queryParameter(name).hashCode(), equalTo(queryParameter(name).hashCode()));
    }

    @Test
    public void queryParameterWithNameOnlyIsNotEqualToAnotherWithDifferentName() throws Exception {
        assertThat(queryParameter(aString()), not(equalTo(queryParameter(aString()))));
    }

    @Test
    public void queryParameterWithNameOnlyToStringIsCorrect() throws Exception {
        String name = aString();
        assertThat(queryParameter(name).toString(), equalTo("QueryParameter{name='" + name + "'}"));
    }

    @Test
    public void queryParameterWithNameOnlyNameIsCorrect() throws Exception {
        String name = aString();
        assertThat(queryParameter(name).name(), equalTo(name));
    }

    @Test
    public void queryParameterWithNameOnlyHasValueIsCorrect() throws Exception {
        assertThat(aNameOnlyQueryParameter().hasValue(), equalTo(false));
    }

    @Test
    public void queryParameterWithNameOnlyValueThrowsException() throws Exception {
        assertThrowsException("QueryParameter with name only should throw exception when value is queried", UnsupportedOperationException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws UnsupportedOperationException {
                aNameOnlyQueryParameter().value();
            }
        });
    }

    @Test
    public void rejectsNullsInNameAndValueQueryParameter() throws Exception {
        assertThrowsException("Null name should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                queryParameter(null, aString());
            }
        });
        assertThrowsException("Null value should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                queryParameter(aString(), null);
            }
        });
    }

    @Test
    public void queryParameterWithNameAndValueIsEqualToAnotherWithTheSameNameAndValue() throws Exception {
        String name = aString();
        String value = aString();
        assertThat(queryParameter(name, value), equalTo(queryParameter(name, value)));
        assertThat(queryParameter(name, value).hashCode(), equalTo(queryParameter(name, value).hashCode()));
    }

    @Test
    public void queryParameterWithNameAndValueIsNotEqualToAnotherWithDifferentNameAndValue() throws Exception {
        assertThat(queryParameter(aString(), aString()), not(equalTo(queryParameter(aString(), aString()))));
    }

    @Test
    public void queryParameterWithNameAndValueToStringIsCorrect() throws Exception {
        String name = aString();
        String value = aString();
        assertThat(queryParameter(name, value).toString(), equalTo("QueryParameter{name='" + name + "', value='" + value + "'}"));
    }

    @Test
    public void queryParameterWithNameAndValueNameIsCorrect() throws Exception {
        String name = aString();
        assertThat(queryParameter(name, aString()).name(), equalTo(name));
    }

    @Test
    public void queryParameterWithNameAndValueHasValueIsCorrect() throws Exception {
        assertThat(aNameAndValueQueryParameter().hasValue(), equalTo(true));
    }

    @Test
    public void queryParameterWithNameAndValueValueIsCorrect() throws Exception {
        String value = aString();
        assertThat(queryParameter(aString(), value).value(), equalTo(value));
    }

    @Test
    public void queryParametersAreIterable() throws Exception {
        HttpQuery.QueryParameter firstQueryParameter = aQueryParameter();
        HttpQuery.QueryParameter secondQueryParameter = aQueryParameter();
        assertThat(queryParameters(firstQueryParameter, secondQueryParameter), contains(firstQueryParameter, secondQueryParameter));
    }

    @Test
    public void queryParametersValueIsCorrect() throws Exception {
        HttpQuery.QueryParameter firstQueryParameter = aQueryParameter();
        HttpQuery.QueryParameter secondQueryParameter = aQueryParameter();
        assertThat(queryParameters(firstQueryParameter, secondQueryParameter).value(), contains(firstQueryParameter, secondQueryParameter));
    }
}
