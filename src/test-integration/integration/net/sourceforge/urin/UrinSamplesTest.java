/*
 * Copyright 2013 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package integration.net.sourceforge.urin;

import net.sourceforge.urin.*;
import org.junit.Test;

import java.util.Collections;

import static java.util.Arrays.asList;
import static net.sourceforge.urin.AccessBackDoors.asString;
import static net.sourceforge.urin.AuthorityBuilder.anAuthority;
import static net.sourceforge.urin.Fragment.fragment;
import static net.sourceforge.urin.Path.path;
import static net.sourceforge.urin.Query.query;
import static net.sourceforge.urin.SchemeBuilder.aScheme;
import static net.sourceforge.urin.Segment.segment;
import static net.sourceforge.urin.SegmentBuilder.aNonDotSegment;
import static net.sourceforge.urin.UrinAssert.assertAsStringAndParse;
import static net.sourceforge.urin.UrinAssert.assertAsStringAsUriAndParse;

public class UrinSamplesTest {
    @Test
    public void canMakeAUrinWithEmptyPath() throws Exception {
        Scheme scheme = aScheme();
        Authority authority = anAuthority();
        assertAsStringAndParse(aScheme(), asString(scheme) + "://" + asString(authority), scheme.urin(authority));
    }

    @Test
    public void canMakeAUrinWithNoAuthorityAndTwoEmptySegments() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        assertAsStringAndParse(aScheme(), asString(scheme) + ":/.//", scheme.urin(path("", "")));
    }

    @Test
    public void canMakeAUrinWithAuthorityAndPathToRoot() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        assertAsStringAndParse(aScheme(), asString(scheme) + "://" + asString(authority) + "/", scheme.urin(authority, Path.<String>path()));
    }

    @Test
    public void canMakeAUrinWithAuthorityAndAbsolutePath() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        Segment<String> segment = aNonDotSegment();
        assertAsStringAndParse(aScheme(), asString(scheme) + "://" + asString(authority) + "/" + asString(segment), scheme.urin(authority, Path.<String>path(segment)));
    }

    @Test
    public void canMakeAUrinWithPathToRoot() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        assertAsStringAsUriAndParse(aScheme(), asString(scheme) + ":/", scheme.urin(Path.<String>path()));
    }

    @Test
    public void emptyPathIsPreserved() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        assertAsStringAsUriAndParse(aScheme(), asString(scheme) + ":/?", scheme.urin(Path.<String>path(), query("")));
    }

    @Test
    public void emptyFragmentIsPreserved() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        assertAsStringAsUriAndParse(aScheme(), asString(scheme) + ":/#", scheme.urin(Path.<String>path(), fragment("")));
    }

    @Test
    public void canMakeAUrinWithSubencodedSegments() throws Exception {
        final PercentEncodingPartial<Iterable<String>, String> percentEncodingPartial = PercentEncodingPartial.percentEncodingDelimitedValue('!');
        Scheme<Iterable<String>, Query<String>, Fragment<String>> scheme = new Scheme.GenericScheme<>("xyz", new MakingDecoder<Segment<Iterable<String>>, Iterable<String>, String>(percentEncodingPartial) {
            @Override
            protected Segment<Iterable<String>> makeOne(Iterable<String> o) {
                return anIterableOfStringsSegment(o, percentEncodingPartial);
            }
        }, Query.stringQueryMaker(), Fragment.stringFragmentMaker());
        assertAsStringAsUriAndParse(scheme, asString(scheme) + ":/a!%21!c", scheme.urin(Path.<Iterable<String>>path(anIterableOfStringsSegment(asList("a", "!", "c"), percentEncodingPartial))));
    }

    @Test
    public void canMakeAUrinWithSubencodedEmptySegments() throws Exception {
        final PercentEncodingPartial<Iterable<String>, String> percentEncodingPartial = PercentEncodingPartial.percentEncodingDelimitedValue('!');
        Scheme<Iterable<String>, Query<String>, Fragment<String>> scheme = new Scheme.GenericScheme<>("xyz", new MakingDecoder<Segment<Iterable<String>>, Iterable<String>, String>(percentEncodingPartial) {
            @Override
            protected Segment<Iterable<String>> makeOne(Iterable<String> o) {
                return anIterableOfStringsSegment(o, percentEncodingPartial);
            }
        }, Query.stringQueryMaker(), Fragment.stringFragmentMaker());
        final Segment<Iterable<String>> emptySegment = anIterableOfStringsSegment(Collections.<String>emptyList(), percentEncodingPartial);
        assertAsStringAsUriAndParse(scheme, asString(scheme) + ":/.//", scheme.urin(Path.<Iterable<String>>path(emptySegment, emptySegment, Segment.<Iterable<String>>dotDot())));
    }

    private static Segment<Iterable<String>> anIterableOfStringsSegment(Iterable<String> o, PercentEncodingPartial<Iterable<String>, String> percentEncodingPartial) {
        return segment(o, percentEncodingPartial);
    }

    @Test
    public void canMakeAUrinWithSubencodedFragment() throws Exception {
        final PercentEncodingPartial<Iterable<String>, String> percentEncodingPartial = PercentEncodingPartial.percentEncodingDelimitedValue('!');
        Scheme<String, Query<String>, MyFragment> scheme = new Scheme.GenericScheme<>("xyz", Segment.STRING_SEGMENT_MAKING_DECODER, Query.stringQueryMaker(), new MakingDecoder<MyFragment, Iterable<String>, String>(percentEncodingPartial) {
            @Override
            protected MyFragment makeOne(Iterable<String> strings) {
                return new MyFragment(strings, percentEncodingPartial);
            }
        });
        assertAsStringAndParse(scheme, asString(scheme) + ":#a!%21!c", scheme.urin(new MyFragment(asList("a", "!", "c"), percentEncodingPartial)));
    }

    private static final class MyFragment extends Fragment<Iterable<String>> {
        private MyFragment(Iterable<String> value, PercentEncodingPartial<Iterable<String>, String> percentEncodingPartial) {
            super(value, percentEncodingPartial);
        }
    }
}
