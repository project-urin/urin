/*
 * Copyright 2024 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package integration.net.sourceforge.urin;

import net.sourceforge.urin.Fragment;
import net.sourceforge.urin.Path;
import net.sourceforge.urin.RelativeReference;
import net.sourceforge.urin.Segment;
import org.junit.jupiter.api.Test;

import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;
import static net.sourceforge.urin.AccessBackDoors.asString;
import static net.sourceforge.urin.Fragment.fragment;
import static net.sourceforge.urin.MoreRandomStringUtils.aStringIncluding;
import static net.sourceforge.urin.Path.rootlessPath;
import static net.sourceforge.urin.SchemeBuilder.aScheme;
import static net.sourceforge.urin.Segment.segment;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

class RelativeReferenceSamplesTest {
    @Test
    void canMakeARelativeReferenceWithColonInTheFirstSegment() {
        final Segment<String> segment = segment(aStringIncluding(':'));
        assertThat(aScheme().relativeReference(rootlessPath(segment)).asString(), equalTo("./" + asString(segment)));
    }

    @Test
    void canParseARelativeReferenceWithColonInTheFirstSegment() throws Exception {
        final Segment<String> segment = segment(aStringIncluding(':'));
        assertThat(aScheme().parseRelativeReference("./" + asString(segment)), equalTo(aScheme().relativeReference(rootlessPath(segment))));
    }

    @Test
    void canParseARelativeReferenceAndRetrieveTheValuesOfTheSegments() throws Exception {
        final RelativeReference<String, ?, ?> relativeReference = aScheme().parseRelativeReference("a/b%2Fc");
        final Path<String> path = relativeReference.path();
        assertThat(stream(path.spliterator(), false).map(Segment::value).collect(toList()), contains("a", "b/c"));
    }

    @Test
    void canParseARelativeReferenceAndRetrieveTheValuesOfTheFragment() throws Exception {
        final RelativeReference<?, ?, Fragment<String>> relativeReference = aScheme().parseRelativeReference("a#foo:bar");
        assertThat(relativeReference.hasFragment(), equalTo(true));
        assertThat(relativeReference.fragment(), equalTo(fragment("foo:bar")));
        assertThat(relativeReference.fragment().value(), equalTo("foo:bar"));
    }
}
