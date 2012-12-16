/*
 * Copyright 2012 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.urin;

import org.junit.Test;

import static java.util.Arrays.asList;
import static net.sourceforge.urin.AuthorityBuilder.anAuthority;
import static net.sourceforge.urin.PercentEncodingUnaryValue.PercentEncodingPartial.percentEncodingDelimitedValue;
import static net.sourceforge.urin.SchemeBuilder.aScheme;
import static net.sourceforge.urin.SegmentBuilder.aNonDotSegment;
import static net.sourceforge.urin.UrinAssert.assertAsStringAndParse;
import static net.sourceforge.urin.UrinAssert.assertAsStringAsUriAndParse;

public class UrinSamplesTest {
    @Test
    public void canMakeAUrinWithEmptyPath() throws Exception {
        Scheme scheme = aScheme();
        Authority authority = anAuthority();
        assertAsStringAndParse(scheme.asString() + "://" + authority.asString(), scheme.urin(authority));
    }

    @Test
    public void canMakeAUrinWithAuthorityAndPathToRoot() throws Exception {
        Scheme<String, Query> scheme = aScheme();
        Authority authority = anAuthority();
        assertAsStringAndParse(scheme.asString() + "://" + authority.asString() + "/", scheme.urin(authority, Path.<String>path()));
    }

    @Test
    public void canMakeAUrinWithAuthorityAndAbsolutePath() throws Exception {
        Scheme<String, Query> scheme = aScheme();
        Authority authority = anAuthority();
        Segment<String> segment = aNonDotSegment();
        assertAsStringAndParse(scheme.asString() + "://" + authority.asString() + "/" + segment.asString(), scheme.urin(authority, Path.<String>path(segment)));
    }

    @Test
    public void canMakeAUrinWithPathToRoot() throws Exception {
        Scheme<String, Query> scheme = aScheme();
        assertAsStringAsUriAndParse(aScheme(), scheme.asString() + ":/", scheme.urin(Path.<String>path()));
    }

    @Test
    public void canMakeAUrinWithSubencodedSegments() throws Exception {
        final PercentEncodingUnaryValue.PercentEncodingPartial<Iterable<String>, String> percentEncodingPartial = percentEncodingDelimitedValue('!');
        Scheme<Iterable<String>, Query> scheme = new Scheme.GenericScheme<>("xyz", new MakingDecoder<Segment<Iterable<String>>, Iterable<String>, String>(percentEncodingPartial) {
            @Override
            protected Segment<Iterable<String>> makeOne(Iterable<String> o) {
                return anIterableOfStringsSegment(o, percentEncodingPartial);
            }
        }, aScheme().queryMakingDecoder);
        assertAsStringAsUriAndParse(scheme, scheme.asString() + ":/a!b!c", scheme.urin(Path.<Iterable<String>>path(anIterableOfStringsSegment(asList("a", "b", "c"), percentEncodingPartial))));
    }

    private static Segment.ValueSegment<Iterable<String>> anIterableOfStringsSegment(Iterable<String> o, PercentEncodingUnaryValue.PercentEncodingPartial<Iterable<String>, String> percentEncodingPartial) {
        return new Segment.ValueSegment<>(o, percentEncodingPartial);
    }
}
