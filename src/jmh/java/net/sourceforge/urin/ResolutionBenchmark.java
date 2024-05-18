/*
 * Copyright 2024 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.urin;

import net.sourceforge.urin.scheme.http.HttpQuery;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import static net.sourceforge.urin.Authority.authority;
import static net.sourceforge.urin.Fragment.fragment;
import static net.sourceforge.urin.Host.registeredName;
import static net.sourceforge.urin.Path.path;
import static net.sourceforge.urin.scheme.http.Http.HTTP;
import static net.sourceforge.urin.scheme.http.Http.http;
import static net.sourceforge.urin.scheme.http.HttpQuery.queryParameter;
import static net.sourceforge.urin.scheme.http.HttpQuery.queryParameters;

@State(Scope.Benchmark)
public class ResolutionBenchmark {

    public static final Urin<String, HttpQuery, Fragment<String>> BASE_URI = http(
            authority(registeredName("example.com"))
    );
    public static final Urin<String, HttpQuery, Fragment<String>> RELATIVE_REFERENCE = HTTP.urin(
            path("foo", "bar", "baz", "qux"),
            queryParameters(
                    queryParameter("a", "b"),
                    queryParameter("c")
            ),
            fragment("d")
    );

    @Benchmark
    public void httpUriResolve(final Blackhole blackhole) {
        blackhole.consume(BASE_URI.resolve(RELATIVE_REFERENCE));
    }

}
