/*
 * Copyright 2011 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.urin;

import org.junit.Test;

import static net.sourceforge.urin.AuthorityBuilder.anAuthority;
import static net.sourceforge.urin.HierarchicalPart.hierarchicalPart;
import static net.sourceforge.urin.HierarchicalPart.hierarchicalPartAbsolutePath;
import static net.sourceforge.urin.NonEmptySegmentBuilder.aNonEmptySegment;
import static net.sourceforge.urin.SegmentBuilder.aSegment;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class HierarchicalPartTest {
    @Test
    public void aSimplePathAsStringReturnsThePath() throws Exception {
        NonEmptySegment firstSegment = aNonEmptySegment();
        Segment secondSegment = aSegment();
        assertThat(hierarchicalPart(firstSegment, secondSegment).asString(), equalTo(firstSegment.asString() + "/" + secondSegment.asString()));
    }

    @Test
    public void makesHierarchicalPartWithAuthorityAndEmptyPath() throws Exception {
        Authority authority = anAuthority();
        assertThat(hierarchicalPart(authority).asString(), equalTo("//" + authority.asString()));
    }

    @Test
    public void makesHierarchicalPartWithAuthorityAndNonEmptyPath() throws Exception {
        Authority authority = anAuthority();
        Segment firstSegment = aSegment();
        Segment secondSegment = aSegment();
        assertThat(hierarchicalPartAbsolutePath(authority, firstSegment, secondSegment).asString(), equalTo("//" + authority.asString() + "/" + firstSegment.asString() + "/" + secondSegment.asString()));
    }

}
