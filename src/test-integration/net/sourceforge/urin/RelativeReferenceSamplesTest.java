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

import static net.sourceforge.urin.MoreRandomStringUtils.aStringIncluding;
import static net.sourceforge.urin.RelativeReference.parse;
import static net.sourceforge.urin.RelativeReference.relativeReference;
import static net.sourceforge.urin.Segment.segment;
import static net.sourceforge.urin.Segments.rootlessSegments;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class RelativeReferenceSamplesTest {
    @Test
    public void canMakeARelativeReferenceWithColonInTheFirstSegment() throws Exception {
        Segment segment = segment(aStringIncluding(':'));
        assertThat(relativeReference(rootlessSegments(segment)).asString(), equalTo("./" + segment.asString()));
    }

    @Test
    public void canParseARelativeReferenceWithColonInTheFirstSegment() throws Exception {
        Segment segment = segment(aStringIncluding(':'));
        assertThat(parse("./" + segment.asString()), equalTo(relativeReference(rootlessSegments(segment))));
    }
}
