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

import com.google.common.base.Supplier;
import net.sourceforge.urin.RandomSupplierSwitcher;

import static net.sourceforge.urin.MoreRandomStringUtils.aString;

public class QueryParameterBuilder {

    private static final RandomSupplierSwitcher<HttpQuery.QueryParameter> RANDOM_SUPPLIER_SWITCHER = new RandomSupplierSwitcher<>(
            new Supplier<HttpQuery.QueryParameter>() {
                public HttpQuery.QueryParameter get() {
                    return aNameOnlyQueryParameter();
                }
            },
            new Supplier<HttpQuery.QueryParameter>() {
                public HttpQuery.QueryParameter get() {
                    return aNameAndValueQueryParameter();
                }
            }
    );

    public static HttpQuery.QueryParameter aNameAndValueQueryParameter() {
        return HttpQuery.queryParameter(aString(), aString());
    }

    public static HttpQuery.QueryParameter aNameOnlyQueryParameter() {
        return HttpQuery.queryParameter(aString());
    }


    static HttpQuery.QueryParameter aQueryParameter() {
        return RANDOM_SUPPLIER_SWITCHER.get();
    }
}
