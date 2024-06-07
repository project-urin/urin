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

import net.sourceforge.urin.scheme.http.Http;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

@State(Scope.Benchmark)
public class ParserBenchmark {

    @Benchmark
    public void httpUriParse(final Blackhole blackhole) throws ParseException {
        blackhole.consume(Http.parseHttpUrin("http://example.com/foo/bar/baz/qux?a=b;c#d"));
    }

    @Benchmark
    public void httpUriReferenceParse(final Blackhole blackhole) throws ParseException {
        blackhole.consume(Http.parseHttpUrinReference("http://example.com/foo/bar/baz/qux?a=b;c#d"));
        blackhole.consume(Http.parseHttpUrinReference("://example.com/foo/bar/baz/qux?a=b;c#d"));
    }

}
