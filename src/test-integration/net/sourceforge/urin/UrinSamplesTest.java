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
import static net.sourceforge.urin.HierarchicalPart.*;
import static net.sourceforge.urin.SchemeBuilder.aScheme;
import static net.sourceforge.urin.SegmentBuilder.aSegment;
import static net.sourceforge.urin.Urin.urin;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class UrinSamplesTest {
    @Test
    public void canMakeAUrinWithEmptyPath() throws Exception {
        Scheme scheme = aScheme();
        Authority authority = anAuthority();
        assertThat(urin(scheme, hierarchicalPart(authority)).asString(), equalTo(scheme.asString() + "://" + authority.asString()));
    }

    @Test
    public void canMakeAUrinWithAuthorityAndPathToRoot() throws Exception {
        Scheme scheme = aScheme();
        Authority authority = anAuthority();
        assertThat(urin(scheme, hierarchicalPartAbsolutePath(authority)).asString(), equalTo(scheme.asString() + "://" + authority.asString() + "/"));
    }

    @Test
    public void canMakeAUrinWithAuthorityAndAbsolutePath() throws Exception {
        Scheme scheme = aScheme();
        Authority authority = anAuthority();
        Segment segment = aSegment();
        assertThat(urin(scheme, hierarchicalPartAbsolutePath(authority, segment)).asString(), equalTo(scheme.asString() + "://" + authority.asString() + "/" + segment.asString()));
    }

    @Test
    public void canMakeAUrinWithPathToRoot() throws Exception {
        Scheme scheme = aScheme();
        assertThat(urin(scheme, hierarchicalPartAbsolute()).asString(), equalTo(scheme.asString() + ":/"));
    }
}
