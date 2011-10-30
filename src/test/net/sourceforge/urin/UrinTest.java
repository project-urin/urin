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

import static net.sourceforge.urin.FragmentBuilder.aFragment;
import static net.sourceforge.urin.HierarchicalPartBuilder.aHierarchicalPart;
import static net.sourceforge.urin.QueryBuilder.aQuery;
import static net.sourceforge.urin.SchemeBuilder.aScheme;
import static net.sourceforge.urin.Urin.urin;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class UrinTest {

    @Test
    public void createsUrinWithAllParts() throws Exception {
        Scheme scheme = aScheme();
        HierarchicalPart hierarchicalPart = aHierarchicalPart();
        Query query = aQuery();
        Fragment fragment = aFragment();
        assertThat(urin(scheme, hierarchicalPart, query, fragment).asString(), equalTo(scheme.asString() + ":" + hierarchicalPart.asString() + "?" + query.asString() + "#" + fragment.asString()));
    }

    @Test
    public void aUrinWithAllPartsIsEqualToAnotherWithTheSameParts() throws Exception {
        Scheme scheme = aScheme();
        HierarchicalPart hierarchicalPart = aHierarchicalPart();
        Query query = aQuery();
        Fragment fragment = aFragment();
        assertThat(urin(scheme, hierarchicalPart, query, fragment), equalTo(urin(scheme, hierarchicalPart, query, fragment)));
        assertThat(urin(scheme, hierarchicalPart, query, fragment).hashCode(), equalTo(urin(scheme, hierarchicalPart, query, fragment).hashCode()));
    }

    @Test
    public void anAuthorityWithNoPortIsNotEqualToAnotherWithTheADifferentHostAndUserInfo() throws Exception {
        assertThat(urin(aScheme(), aHierarchicalPart(), aQuery(), aFragment()), not(equalTo(urin(aScheme(), aHierarchicalPart(), aQuery(), aFragment()))));
    }

    @Test
    public void anAuthorityWithNoPortToStringIsCorrect() throws Exception {
        Scheme scheme = aScheme();
        HierarchicalPart hierarchicalPart = aHierarchicalPart();
        Query query = aQuery();
        Fragment fragment = aFragment();
        assertThat(urin(scheme, hierarchicalPart, query, fragment).toString(), equalTo("Urin{scheme=" + scheme + ", hierarchicalPart=" + hierarchicalPart + ", query=" + query + ", fragment=" + fragment + "}"));
    }

    @Test
    public void createsUrinWithNoFragment() throws Exception {
        Scheme scheme = aScheme();
        HierarchicalPart hierarchicalPart = aHierarchicalPart();
        Query query = aQuery();
        assertThat(urin(scheme, hierarchicalPart, query).asString(), equalTo(scheme.asString() + ":" + hierarchicalPart.asString() + "?" + query.asString()));
    }

    @Test
    public void createsUrinWithNoQuery() throws Exception {
        Scheme scheme = aScheme();
        HierarchicalPart hierarchicalPart = aHierarchicalPart();
        Fragment fragment = aFragment();
        assertThat(urin(scheme, hierarchicalPart, fragment).asString(), equalTo(scheme.asString() + ":" + hierarchicalPart.asString() + "#" + fragment.asString()));
    }

    @Test
    public void createsUrinWithNoQueryAndNoFragment() throws Exception {
        Scheme scheme = aScheme();
        HierarchicalPart hierarchicalPart = aHierarchicalPart();
        assertThat(urin(scheme, hierarchicalPart).asString(), equalTo(scheme.asString() + ":" + hierarchicalPart.asString()));
    }

}
