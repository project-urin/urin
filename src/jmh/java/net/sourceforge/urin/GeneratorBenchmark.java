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

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import static net.sourceforge.urin.Authority.authority;
import static net.sourceforge.urin.Fragment.fragment;
import static net.sourceforge.urin.Host.registeredName;
import static net.sourceforge.urin.Path.path;
import static net.sourceforge.urin.scheme.http.Http.http;
import static net.sourceforge.urin.scheme.http.HttpQuery.queryParameter;
import static net.sourceforge.urin.scheme.http.HttpQuery.queryParameters;

@State(Scope.Benchmark)
public class GeneratorBenchmark {

    @Benchmark
    public void httpUriGenerate(final Blackhole blackhole) {
        blackhole.consume(http(
                authority(registeredName("example.com")),
                path("foo", "bar", "baz", "qux"),
                queryParameters(
                        queryParameter("a", "b"),
                        queryParameter("c")
                ),
                fragment("d")
        ).asString());
    }

}
