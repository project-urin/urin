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
    public void aUrinWithAllPartsIsNotEqualToAnotherWithTheADifferentParts() throws Exception {
        assertThat(urin(aScheme(), aHierarchicalPart(), aQuery(), aFragment()), not(equalTo(urin(aScheme(), aHierarchicalPart(), aQuery(), aFragment()))));
    }

    @Test
    public void aUrinWithAllPartsToStringIsCorrect() throws Exception {
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
    public void aUrinWithNoFragmentIsEqualToAnotherWithTheSameParts() throws Exception {
        Scheme scheme = aScheme();
        HierarchicalPart hierarchicalPart = aHierarchicalPart();
        Query query = aQuery();
        assertThat(urin(scheme, hierarchicalPart, query), equalTo(urin(scheme, hierarchicalPart, query)));
        assertThat(urin(scheme, hierarchicalPart, query).hashCode(), equalTo(urin(scheme, hierarchicalPart, query).hashCode()));
    }

    @Test
    public void aUrinWithNoFragmentIsNotEqualToAnotherWithTheADifferentParts() throws Exception {
        assertThat(urin(aScheme(), aHierarchicalPart(), aQuery()), not(equalTo(urin(aScheme(), aHierarchicalPart(), aQuery()))));
    }

    @Test
    public void aUrinWithNoFragmentToStringIsCorrect() throws Exception {
        Scheme scheme = aScheme();
        HierarchicalPart hierarchicalPart = aHierarchicalPart();
        Query query = aQuery();
        assertThat(urin(scheme, hierarchicalPart, query).toString(), equalTo("Urin{scheme=" + scheme + ", hierarchicalPart=" + hierarchicalPart + ", query=" + query + "}"));
    }

    @Test
    public void createsUrinWithNoQuery() throws Exception {
        Scheme scheme = aScheme();
        HierarchicalPart hierarchicalPart = aHierarchicalPart();
        Fragment fragment = aFragment();
        assertThat(urin(scheme, hierarchicalPart, fragment).asString(), equalTo(scheme.asString() + ":" + hierarchicalPart.asString() + "#" + fragment.asString()));
    }

    @Test
    public void aUrinWithNoQueryIsEqualToAnotherWithTheSameParts() throws Exception {
        Scheme scheme = aScheme();
        HierarchicalPart hierarchicalPart = aHierarchicalPart();
        Fragment fragment = aFragment();
        assertThat(urin(scheme, hierarchicalPart, fragment), equalTo(urin(scheme, hierarchicalPart, fragment)));
        assertThat(urin(scheme, hierarchicalPart, fragment).hashCode(), equalTo(urin(scheme, hierarchicalPart, fragment).hashCode()));
    }

    @Test
    public void aUrinWithNoQueryIsNotEqualToAnotherWithTheADifferentParts() throws Exception {
        assertThat(urin(aScheme(), aHierarchicalPart(), aFragment()), not(equalTo(urin(aScheme(), aHierarchicalPart(), aFragment()))));
    }

    @Test
    public void aUrinWithNoQueryToStringIsCorrect() throws Exception {
        Scheme scheme = aScheme();
        HierarchicalPart hierarchicalPart = aHierarchicalPart();
        Fragment fragment = aFragment();
        assertThat(urin(scheme, hierarchicalPart, fragment).toString(), equalTo("Urin{scheme=" + scheme + ", hierarchicalPart=" + hierarchicalPart + ", fragment=" + fragment + "}"));
    }

    @Test
    public void createsUrinWithNoQueryAndNoFragment() throws Exception {
        Scheme scheme = aScheme();
        HierarchicalPart hierarchicalPart = aHierarchicalPart();
        assertThat(urin(scheme, hierarchicalPart).asString(), equalTo(scheme.asString() + ":" + hierarchicalPart.asString()));
    }

    @Test
    public void aUrinWithNoQueryOrFragmentIsEqualToAnotherWithTheSameParts() throws Exception {
        Scheme scheme = aScheme();
        HierarchicalPart hierarchicalPart = aHierarchicalPart();
        assertThat(urin(scheme, hierarchicalPart), equalTo(urin(scheme, hierarchicalPart)));
        assertThat(urin(scheme, hierarchicalPart).hashCode(), equalTo(urin(scheme, hierarchicalPart).hashCode()));
    }

    @Test
    public void aUrinWithNoQueryOrFragmentIsNotEqualToAnotherWithTheADifferentParts() throws Exception {
        assertThat(urin(aScheme(), aHierarchicalPart()), not(equalTo(urin(aScheme(), aHierarchicalPart()))));
    }

    @Test
    public void aUrinWithNoQueryOrFragmentToStringIsCorrect() throws Exception {
        Scheme scheme = aScheme();
        HierarchicalPart hierarchicalPart = aHierarchicalPart();
        assertThat(urin(scheme, hierarchicalPart).toString(), equalTo("Urin{scheme=" + scheme + ", hierarchicalPart=" + hierarchicalPart + "}"));
    }

}
