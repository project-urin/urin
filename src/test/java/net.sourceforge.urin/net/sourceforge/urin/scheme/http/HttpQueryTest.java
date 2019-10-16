/*
 * Copyright 2019 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.urin.scheme.http;

import net.sourceforge.urin.Query;
import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static net.sourceforge.urin.MoreRandomStringUtils.aString;
import static net.sourceforge.urin.scheme.http.HttpQuery.queryParameter;
import static net.sourceforge.urin.scheme.http.HttpQuery.queryParameters;
import static net.sourceforge.urin.scheme.http.QueryParameterBuilder.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HttpQueryTest {
    @Test
    void rejectsNullQueryParameter() {
        assertThrows(NullPointerException.class, () -> {
            HttpQuery.QueryParameter queryParameter = null;
            queryParameters(queryParameter);
        }, "Null name should throw NullPointerException in factory");
    }

    @Test
    void queryParametersUsingVarargsAreImmutable() {
        HttpQuery.QueryParameter firstQueryParameter = aQueryParameter();
        HttpQuery.QueryParameter secondQueryParameter = aQueryParameter();
        HttpQuery.QueryParameter[] queryParameters = {firstQueryParameter, secondQueryParameter};
        Query query = queryParameters(queryParameters);
        queryParameters[0] = aQueryParameter();
        assertThat(query, equalTo(queryParameters(firstQueryParameter, secondQueryParameter)));

    }

    @Test
    void queryParametersIsEqualToAnotherWithTheSameMembers() {
        HttpQuery.QueryParameter firstQueryParameter = aQueryParameter();
        HttpQuery.QueryParameter secondQueryParameter = aQueryParameter();
        assertThat(queryParameters(firstQueryParameter, secondQueryParameter), equalTo(queryParameters(firstQueryParameter, secondQueryParameter)));
        assertThat(queryParameters(firstQueryParameter, secondQueryParameter).hashCode(), equalTo(queryParameters(firstQueryParameter, secondQueryParameter).hashCode()));
    }

    @Test
    void queryParametersIsNotEqualToAnotherWithDifferentMembers() {
        assertThat(queryParameters(aQueryParameter(), aQueryParameter()), not(equalTo(queryParameters(aQueryParameter(), aQueryParameter()))));
    }

    @Test
    void queryParametersToStringIsCorrect() {
        HttpQuery.QueryParameter firstQueryParameter = aQueryParameter();
        HttpQuery.QueryParameter secondQueryParameter = aQueryParameter();
        assertThat(queryParameters(firstQueryParameter, secondQueryParameter).toString(), equalTo("HttpQuery{value='" + asList(firstQueryParameter, secondQueryParameter) + "'}"));
    }

    @Test
    void rejectsNullNameOnlyQueryParameter() {
        assertThrows(NullPointerException.class, () -> queryParameter(null), "Null name should throw NullPointerException in factory");
    }

    @Test
    void queryParameterWithNameOnlyIsEqualToAnotherWithTheSameName() {
        String name = aString();
        assertThat(queryParameter(name), equalTo(queryParameter(name)));
        assertThat(queryParameter(name).hashCode(), equalTo(queryParameter(name).hashCode()));
    }

    @Test
    void queryParameterWithNameOnlyIsNotEqualToAnotherWithDifferentName() {
        assertThat(queryParameter(aString()), not(equalTo(queryParameter(aString()))));
    }

    @Test
    void queryParameterWithNameOnlyToStringIsCorrect() {
        String name = aString();
        assertThat(queryParameter(name).toString(), equalTo("QueryParameter{name='" + name + "'}"));
    }

    @Test
    void queryParameterWithNameOnlyNameIsCorrect() {
        String name = aString();
        assertThat(queryParameter(name).name(), equalTo(name));
    }

    @Test
    void queryParameterWithNameOnlyHasValueIsCorrect() {
        assertThat(aNameOnlyQueryParameter().hasValue(), equalTo(false));
    }

    @Test
    void queryParameterWithNameOnlyValueThrowsException() {
        assertThrows(UnsupportedOperationException.class, () -> aNameOnlyQueryParameter().value(), "QueryParameter with name only should throw exception when value is queried");
    }

    @Test
    void rejectsNullsInNameAndValueQueryParameter() {
        assertThrows(NullPointerException.class, () -> queryParameter(null, aString()), "Null name should throw NullPointerException in factory");
        assertThrows(NullPointerException.class, () -> queryParameter(aString(), null), "Null value should throw NullPointerException in factory");
    }

    @Test
    void queryParameterWithNameAndValueIsEqualToAnotherWithTheSameNameAndValue() {
        String name = aString();
        String value = aString();
        assertThat(queryParameter(name, value), equalTo(queryParameter(name, value)));
        assertThat(queryParameter(name, value).hashCode(), equalTo(queryParameter(name, value).hashCode()));
    }

    @Test
    void queryParameterWithNameAndValueIsNotEqualToAnotherWithDifferentNameAndValue() {
        assertThat(queryParameter(aString(), aString()), not(equalTo(queryParameter(aString(), aString()))));
    }

    @Test
    void queryParameterWithNameAndValueToStringIsCorrect() {
        String name = aString();
        String value = aString();
        assertThat(queryParameter(name, value).toString(), equalTo("QueryParameter{name='" + name + "', value='" + value + "'}"));
    }

    @Test
    void queryParameterWithNameAndValueNameIsCorrect() {
        String name = aString();
        assertThat(queryParameter(name, aString()).name(), equalTo(name));
    }

    @Test
    void queryParameterWithNameAndValueHasValueIsCorrect() {
        assertThat(aNameAndValueQueryParameter().hasValue(), equalTo(true));
    }

    @Test
    void queryParameterWithNameAndValueValueIsCorrect() {
        String value = aString();
        assertThat(queryParameter(aString(), value).value(), equalTo(value));
    }

    @Test
    void queryParametersAreIterable() {
        HttpQuery.QueryParameter firstQueryParameter = aQueryParameter();
        HttpQuery.QueryParameter secondQueryParameter = aQueryParameter();
        assertThat(queryParameters(firstQueryParameter, secondQueryParameter), contains(firstQueryParameter, secondQueryParameter));
    }

    @Test
    void queryParametersValueIsCorrect() {
        HttpQuery.QueryParameter firstQueryParameter = aQueryParameter();
        HttpQuery.QueryParameter secondQueryParameter = aQueryParameter();
        assertThat(queryParameters(firstQueryParameter, secondQueryParameter).value(), contains(firstQueryParameter, secondQueryParameter));
    }
}
