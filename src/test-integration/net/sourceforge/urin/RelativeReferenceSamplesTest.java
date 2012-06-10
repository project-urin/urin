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

import com.google.common.base.Function;
import org.junit.Test;

import static com.google.common.collect.Iterables.transform;
import static net.sourceforge.urin.Fragment.fragment;
import static net.sourceforge.urin.MoreRandomStringUtils.aStringIncluding;
import static net.sourceforge.urin.Path.rootlessPath;
import static net.sourceforge.urin.RelativeReference.parse;
import static net.sourceforge.urin.RelativeReference.relativeReference;
import static net.sourceforge.urin.Segment.segment;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

public class RelativeReferenceSamplesTest {
    @Test
    public void canMakeARelativeReferenceWithColonInTheFirstSegment() throws Exception {
        Segment segment = segment(aStringIncluding(':'));
        assertThat(relativeReference(rootlessPath(segment)).asString(), equalTo("./" + segment.asString()));
    }

    @Test
    public void canParseARelativeReferenceWithColonInTheFirstSegment() throws Exception {
        Segment segment = segment(aStringIncluding(':'));
        assertThat(parse("./" + segment.asString()), equalTo(relativeReference(rootlessPath(segment))));
    }

    @Test
    public void canParseARelativeReferenceAndRetrieveTheValuesOfTheSegments() throws Exception {
        RelativeReference relativeReference = parse("a/b%2Fc");
        assertThat(transform(relativeReference.path(), new Function<Segment, String>() {
            public String apply(final Segment segment) {
                return segment.value();
            }
        }), contains("a", "b/c"));
    }

    @Test
    public void canParseARelativeReferenceAndRetrieveTheValuesOfTheFragment() throws Exception {
        RelativeReference relativeReference = parse("a#foo:bar");
        assertThat(relativeReference.hasFragment(), equalTo(true));
        assertThat(relativeReference.fragment(), equalTo(fragment("foo:bar")));
        assertThat(relativeReference.fragment().value(), equalTo("foo:bar"));
    }
}
