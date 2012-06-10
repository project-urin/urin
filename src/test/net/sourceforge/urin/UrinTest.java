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

import static net.sourceforge.urin.Authority.authority;
import static net.sourceforge.urin.ExceptionAssert.assertThrowsException;
import static net.sourceforge.urin.FragmentBuilder.aFragment;
import static net.sourceforge.urin.HierarchicalPart.hierarchicalPart;
import static net.sourceforge.urin.HierarchicalPartBuilder.aHierarchicalPart;
import static net.sourceforge.urin.HostBuilder.aHost;
import static net.sourceforge.urin.PortBuilder.aPort;
import static net.sourceforge.urin.QueryBuilder.aQuery;
import static net.sourceforge.urin.Scheme.scheme;
import static net.sourceforge.urin.SchemeBuilder.aScheme;
import static net.sourceforge.urin.SchemeBuilder.aValidSchemeName;
import static net.sourceforge.urin.Urin.parse;
import static net.sourceforge.urin.Urin.urin;
import static net.sourceforge.urin.UrinBuilder.aUrin;
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
    public void urinWithAllPartsPathIsCorrect() throws Exception {
        Scheme scheme = aScheme();
        HierarchicalPart hierarchicalPart = aHierarchicalPart();
        Query query = aQuery();
        Fragment fragment = aFragment();
        assertThat(urin(scheme, hierarchicalPart, query, fragment).path(), equalTo(hierarchicalPart.path()));
    }

    @Test
    public void urinWithAllPartsFragmentIsCorrect() throws Exception {
        Scheme scheme = aScheme();
        HierarchicalPart hierarchicalPart = aHierarchicalPart();
        Query query = aQuery();
        Fragment fragment = aFragment();
        assertThat(urin(scheme, hierarchicalPart, query, fragment).hasFragment(), equalTo(true));
        assertThat(urin(scheme, hierarchicalPart, query, fragment).fragment(), equalTo(fragment));
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
        assertThat(urin(scheme, hierarchicalPart, query, fragment).toString(), equalTo("Urin{scheme=" + scheme.removeDefaultPort() + ", hierarchicalPart=" + hierarchicalPart + ", query=" + query + ", fragment=" + fragment + "}"));
    }

    @Test
    public void rejectsNullInFactoryForAUrinWithAllParts() throws Exception {
        assertThrowsException("Null scheme should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                urin(null, aHierarchicalPart(), aQuery(), aFragment());
            }
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                urin(aScheme(), null, aQuery(), aFragment());
            }
        });
        assertThrowsException("Null query should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                urin(aScheme(), aHierarchicalPart(), null, aFragment());
            }
        });
        assertThrowsException("Null fragment should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                urin(aScheme(), aHierarchicalPart(), aQuery(), null);
            }
        });
    }

    @Test
    public void createsUrinWithNoFragment() throws Exception {
        Scheme scheme = aScheme();
        HierarchicalPart hierarchicalPart = aHierarchicalPart();
        Query query = aQuery();
        assertThat(urin(scheme, hierarchicalPart, query).asString(), equalTo(scheme.asString() + ":" + hierarchicalPart.asString() + "?" + query.asString()));
    }

    @Test
    public void urinWithNoFragmentPathIsCorrect() throws Exception {
        Scheme scheme = aScheme();
        HierarchicalPart hierarchicalPart = aHierarchicalPart();
        Query query = aQuery();
        assertThat(urin(scheme, hierarchicalPart, query).path(), equalTo(hierarchicalPart.path()));
    }

    @Test
    public void urinWithNoFragmentFragmentIsCorrect() throws Exception {
        Scheme scheme = aScheme();
        HierarchicalPart hierarchicalPart = aHierarchicalPart();
        Query query = aQuery();
        final Urin urin = urin(scheme, hierarchicalPart, query);
        assertThat(urin.hasFragment(), equalTo(false));
        assertThrowsException("Attempt to get fragment from a UrinReference that does not have one.", UnsupportedOperationException.class, new ExceptionAssert.ExceptionThrower<java.lang.UnsupportedOperationException>() {
            public void execute() throws UnsupportedOperationException {
                urin.fragment();
            }
        });
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
        assertThat(urin(scheme, hierarchicalPart, query).toString(), equalTo("Urin{scheme=" + scheme.removeDefaultPort() + ", hierarchicalPart=" + hierarchicalPart + ", query=" + query + "}"));
    }

    @Test
    public void rejectsNullInFactoryForAUrinWithNoFragment() throws Exception {
        assertThrowsException("Null scheme should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                urin(null, aHierarchicalPart(), aQuery());
            }
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                urin(aScheme(), null, aQuery());
            }
        });
        assertThrowsException("Null query should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Query query = null;
                urin(aScheme(), aHierarchicalPart(), query);
            }
        });
    }

    @Test
    public void createsUrinWithNoQuery() throws Exception {
        Scheme scheme = aScheme();
        HierarchicalPart hierarchicalPart = aHierarchicalPart();
        Fragment fragment = aFragment();
        assertThat(urin(scheme, hierarchicalPart, fragment).asString(), equalTo(scheme.asString() + ":" + hierarchicalPart.asString() + "#" + fragment.asString()));
    }

    @Test
    public void urinWithNoQueryPathIsCorrect() throws Exception {
        Scheme scheme = aScheme();
        HierarchicalPart hierarchicalPart = aHierarchicalPart();
        Fragment fragment = aFragment();
        assertThat(urin(scheme, hierarchicalPart, fragment).path(), equalTo(hierarchicalPart.path()));
    }

    @Test
    public void urinWithNoQueryFragmentIsCorrect() throws Exception {
        Scheme scheme = aScheme();
        HierarchicalPart hierarchicalPart = aHierarchicalPart();
        Fragment fragment = aFragment();
        assertThat(urin(scheme, hierarchicalPart, fragment).hasFragment(), equalTo(true));
        assertThat(urin(scheme, hierarchicalPart, fragment).fragment(), equalTo(fragment));
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
        assertThat(urin(scheme, hierarchicalPart, fragment).toString(), equalTo("Urin{scheme=" + scheme.removeDefaultPort() + ", hierarchicalPart=" + hierarchicalPart + ", fragment=" + fragment + "}"));
    }

    @Test
    public void rejectsNullInFactoryForAUrinWithNoQuery() throws Exception {
        assertThrowsException("Null scheme should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                urin(null, aHierarchicalPart(), aFragment());
            }
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                urin(aScheme(), null, aFragment());
            }
        });
        assertThrowsException("Null fragment should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Fragment fragment = null;
                urin(aScheme(), aHierarchicalPart(), fragment);
            }
        });
    }

    @Test
    public void createsUrinWithNoQueryAndNoFragment() throws Exception {
        Scheme scheme = aScheme();
        HierarchicalPart hierarchicalPart = aHierarchicalPart();
        assertThat(urin(scheme, hierarchicalPart).asString(), equalTo(scheme.asString() + ":" + hierarchicalPart.asString()));
    }

    @Test
    public void urinWithNoQueryAndNoFragmentPathIsCorrect() throws Exception {
        Scheme scheme = aScheme();
        HierarchicalPart hierarchicalPart = aHierarchicalPart();
        final Urin urin = urin(scheme, hierarchicalPart);
        assertThat(urin.path(), equalTo(hierarchicalPart.path()));
        assertThrowsException("Attempt to get fragment from a UrinReference that does not have one.", UnsupportedOperationException.class, new ExceptionAssert.ExceptionThrower<java.lang.UnsupportedOperationException>() {
            public void execute() throws UnsupportedOperationException {
                urin.fragment();
            }
        });
    }

    @Test
    public void urinWithNoQueryAndNoFragmentFragmentIsCorrect() throws Exception {
        Scheme scheme = aScheme();
        HierarchicalPart hierarchicalPart = aHierarchicalPart();
        assertThat(urin(scheme, hierarchicalPart).hasFragment(), equalTo(false));
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
        assertThat(urin(scheme, hierarchicalPart).toString(), equalTo("Urin{scheme=" + scheme.removeDefaultPort() + ", hierarchicalPart=" + hierarchicalPart + "}"));
    }

    @Test
    public void rejectsNullInFactoryForAUrinWithNoQueryOrNoFragment() throws Exception {
        assertThrowsException("Null scheme should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                urin(null, aHierarchicalPart());
            }
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                urin(aScheme(), null);
            }
        });
    }

    @Test
    public void parsesUrinWithAllParts() throws Exception {
        Scheme scheme = aScheme();
        HierarchicalPart hierarchicalPart = aHierarchicalPart();
        Query query = aQuery();
        Fragment fragment = aFragment();
        assertThat(Urin.parse(scheme.asString() + ":" + hierarchicalPart.asString() + "?" + query.asString() + "#" + fragment.asString()), equalTo(urin(scheme, hierarchicalPart, query, fragment)));
    }

    @Test
    public void parsesUrinWithNoFragment() throws Exception {
        Scheme scheme = aScheme();
        HierarchicalPart hierarchicalPart = aHierarchicalPart();
        Query query = aQuery();
        assertThat(parse(scheme.asString() + ":" + hierarchicalPart.asString() + "?" + query.asString()), equalTo(urin(scheme, hierarchicalPart, query)));
    }

    @Test
    public void parsesUrinWithNoQuery() throws Exception {
        Scheme scheme = aScheme();
        HierarchicalPart hierarchicalPart = aHierarchicalPart();
        Fragment fragment = aFragment();
        assertThat(parse(scheme.asString() + ":" + hierarchicalPart.asString() + "#" + fragment.asString()), equalTo(urin(scheme, hierarchicalPart, fragment)));
    }

    @Test
    public void parsesUrinWithNoQueryAndNoFragment() throws Exception {
        Scheme scheme = aScheme();
        HierarchicalPart hierarchicalPart = aHierarchicalPart();
        assertThat(parse(scheme.asString() + ":" + hierarchicalPart.asString()), equalTo(urin(scheme, hierarchicalPart)));
    }

    @Test
    public void ResolvesAUrinToItself() throws Exception {
        Urin urinReference = aUrin();
        assertThat(aUrin().resolve(urinReference), equalTo(urinReference));
    }

    @Test
    public void UrinCreatedWithASchemeSpecifyingADefaultPortDifferentToThatUsedIsEqualToIdenticalUrinCreatedWithASchemeWithoutDefaultPort() throws Exception {
        String schemeName = aValidSchemeName();
        HierarchicalPart hierarchicalPart = aHierarchicalPart();
        assertThat(urin(scheme(schemeName, aPort()), hierarchicalPart), equalTo(urin(scheme(schemeName), hierarchicalPart)));
    }

    @Test
    public void UrinCreatedWithASchemeSpecifyingADefaultPortEqualToThatUsedIsEqualToIdenticalUrinCreatedWithASchemeWithoutDefaultPortAndWithoutPort() throws Exception {
        String schemeName = aValidSchemeName();
        Port port = aPort();
        Host host = aHost();
        assertThat(urin(scheme(schemeName, port), hierarchicalPart(authority(host, port))), equalTo(urin(scheme(schemeName), hierarchicalPart(authority(host)))));
    }
}
