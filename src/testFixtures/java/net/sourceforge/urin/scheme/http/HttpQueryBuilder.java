/*
 * Copyright 2024 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.urin.scheme.http;

import java.util.Random;

import static net.sourceforge.urin.scheme.http.QueryParameterBuilder.aQueryParameter;

public final class HttpQueryBuilder {

    private static final Random RANDOM = new Random();

    private HttpQueryBuilder() {
    }

    public static HttpQuery anHttpQuery() {
        int numberOfParameters = RANDOM.nextInt(5);
        HttpQuery.QueryParameter[] queryParameters = new HttpQuery.QueryParameter[numberOfParameters];
        for (int i = 0; i < queryParameters.length; i++) {
            queryParameters[i] = aQueryParameter();
        }
        return HttpQuery.queryParameters(queryParameters);
    }
}