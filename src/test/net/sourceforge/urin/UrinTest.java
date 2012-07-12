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
import static net.sourceforge.urin.AuthorityBuilder.anAuthority;
import static net.sourceforge.urin.ExceptionAssert.assertThrowsException;
import static net.sourceforge.urin.FragmentBuilder.aFragment;
import static net.sourceforge.urin.HierarchicalPart.hierarchicalPart;
import static net.sourceforge.urin.HierarchicalPartBuilder.aHierarchicalPart;
import static net.sourceforge.urin.HostBuilder.aHost;
import static net.sourceforge.urin.PathBuilder.aPath;
import static net.sourceforge.urin.PathBuilder.anAbsolutePath;
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
    public void urinWithAllPartsQueryIsCorrect() throws Exception {
        Scheme scheme = aScheme();
        HierarchicalPart hierarchicalPart = aHierarchicalPart();
        Query query = aQuery();
        Fragment fragment = aFragment();
        assertThat(urin(scheme, hierarchicalPart, query, fragment).hasQuery(), equalTo(true));
        assertThat(urin(scheme, hierarchicalPart, query, fragment).query(), equalTo(query));
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
    public void urinWithNoFragmentQueryIsCorrect() throws Exception {
        Scheme scheme = aScheme();
        HierarchicalPart hierarchicalPart = aHierarchicalPart();
        Query query = aQuery();
        final Urin urin = urin(scheme, hierarchicalPart, query);
        assertThat(urin.hasQuery(), equalTo(true));
        assertThat(urin(scheme, hierarchicalPart, query).query(), equalTo(query));
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
    public void urinWithNoQueryQueryIsCorrect() throws Exception {
        Scheme scheme = aScheme();
        HierarchicalPart hierarchicalPart = aHierarchicalPart();
        Fragment fragment = aFragment();
        final Urin urin = urin(scheme, hierarchicalPart, fragment);
        assertThat(urin.hasQuery(), equalTo(false));
        assertThrowsException("Attempt to get query from a UrinReference that does not have one.", UnsupportedOperationException.class, new ExceptionAssert.ExceptionThrower<java.lang.UnsupportedOperationException>() {
            public void execute() throws UnsupportedOperationException {
                urin.query();
            }
        });
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
    public void aUrinWithNoQueryIsEqualToAnotherWithTheSamePartsWithHierarchcialPart() throws Exception {
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
    public void aUrinWithNoQueryWithHierarchcialPartToStringIsCorrect() throws Exception {
        Scheme scheme = aScheme();
        HierarchicalPart hierarchicalPart = aHierarchicalPart();
        Fragment fragment = aFragment();
        assertThat(urin(scheme, hierarchicalPart, fragment).toString(), equalTo("Urin{scheme=" + scheme.removeDefaultPort() + ", hierarchicalPart=" + hierarchicalPart + ", fragment=" + fragment + "}"));
    }

    @Test
    public void rejectsNullInFactoryForAUrinWithNoQueryWithHierarchcialPart() throws Exception {
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
    public void urinWithNoQueryAndNoFragmentQueryIsCorrect() throws Exception {
        Scheme scheme = aScheme();
        HierarchicalPart hierarchicalPart = aHierarchicalPart();
        final Urin urin = urin(scheme, hierarchicalPart);
        assertThat(urin.hasQuery(), equalTo(false));
        assertThrowsException("Attempt to get query from a UrinReference that does not have one.", UnsupportedOperationException.class, new ExceptionAssert.ExceptionThrower<java.lang.UnsupportedOperationException>() {
            public void execute() throws UnsupportedOperationException {
                urin.query();
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
    public void aUrinWithNoQueryOrFragmentIsEqualToAnotherWithTheSamePartsUsingHierarchicalPart() throws Exception {
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
    public void aUrinWithNoQueryOrFragmentUsingHierarchicalPartToStringIsCorrect() throws Exception {
        Scheme scheme = aScheme();
        HierarchicalPart hierarchicalPart = aHierarchicalPart();
        assertThat(urin(scheme, hierarchicalPart).toString(), equalTo("Urin{scheme=" + scheme.removeDefaultPort() + ", hierarchicalPart=" + hierarchicalPart + "}"));
    }

    @Test
    public void rejectsNullInFactoryForAUrinWithNoQueryOrNoFragmentUsingHierarchicalPart() throws Exception {
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
    public void aUrinWithNoQueryOrFragmentOrAuthorityOrPathIsEqualToAnotherWithTheSameParts() throws Exception {
        Scheme scheme = aScheme();
        assertThat(scheme.urin(), equalTo(scheme.urin()));
        assertThat(scheme.urin().hashCode(), equalTo(scheme.urin().hashCode()));
    }

    @Test
    public void aUrinWithNoQueryOrFragmentIsNotEqualToAnotherWithTheADifferentScheme() throws Exception {
        assertThat(aScheme().urin(), not(equalTo(aScheme().urin())));
    }

    @Test
    public void aUrinWithNoQueryOrFragmentOrAuthorityOrPathToStringIsCorrect() throws Exception {
        Scheme scheme = aScheme();
        assertThat(scheme.urin().toString(), equalTo("Urin{scheme=" + scheme.removeDefaultPort() + ", hierarchicalPart=" + hierarchicalPart() + "}"));
    }

    @Test
    public void aUrinWithNoQueryOrFragmentOrAuthorityIsEqualToAnotherWithTheSameParts() throws Exception {
        Scheme scheme = aScheme();
        Path path = aPath();
        assertThat(scheme.urin(path), equalTo(scheme.urin(path)));
        assertThat(scheme.urin(path).hashCode(), equalTo(scheme.urin(path).hashCode()));
    }

    @Test
    public void aUrinWithNoQueryOrFragmentIsNotEqualToAnotherWithTheADifferentPath() throws Exception {
        assertThat(aScheme().urin(aPath()), not(equalTo(aScheme().urin(aPath()))));
    }

    @Test
    public void aUrinWithNoQueryOrFragmentOrAuthorityToStringIsCorrect() throws Exception {
        Scheme scheme = aScheme();
        Path path = aPath();
        assertThat(scheme.urin(path).toString(), equalTo("Urin{scheme=" + scheme.removeDefaultPort() + ", hierarchicalPart=" + hierarchicalPart(path) + "}"));
    }

    @Test
    public void rejectsNullInFactoryForAUrinWithNoQueryOrNoFragmentOrAuthority() throws Exception {
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Path path = null;
                aScheme().urin(path);
            }
        });
    }

    @Test
    public void aUrinWithNoQueryOrFragmentOrPathIsEqualToAnotherWithTheSameParts() throws Exception {
        Scheme scheme = aScheme();
        Authority authority = anAuthority();
        assertThat(scheme.urin(authority), equalTo(scheme.urin(authority)));
        assertThat(scheme.urin(authority).hashCode(), equalTo(scheme.urin(authority).hashCode()));
    }

    @Test
    public void aUrinWithNoQueryOrFragmentIsNotEqualToAnotherWithTheADifferentAuthority() throws Exception {
        assertThat(aScheme().urin(anAuthority()), not(equalTo(aScheme().urin(anAuthority()))));
    }

    @Test
    public void aUrinWithNoQueryOrFragmentOrPathToStringIsCorrect() throws Exception {
        Scheme scheme = aScheme();
        Authority authority = anAuthority();
        assertThat(scheme.urin(authority).toString(), equalTo("Urin{scheme=" + scheme.removeDefaultPort() + ", hierarchicalPart=" + hierarchicalPart(authority) + "}"));
    }

    @Test
    public void rejectsNullInFactoryForAUrinWithNoQueryOrNoFragmentOrPath() throws Exception {
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Authority authority = null;
                aScheme().urin(authority);
            }
        });
    }

    @Test
    public void aUrinWithNoQueryOrFragmentIsEqualToAnotherWithTheSameParts() throws Exception {
        Scheme scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath path = anAbsolutePath();
        assertThat(scheme.urin(authority, path), equalTo(scheme.urin(authority, path)));
        assertThat(scheme.urin(authority, path).hashCode(), equalTo(scheme.urin(authority, path).hashCode()));
    }

    @Test
    public void aUrinWithNoQueryOrFragmentIsNotEqualToAnotherWithTheADifferentAuthorityAndPath() throws Exception {
        assertThat(aScheme().urin(anAuthority(), anAbsolutePath()), not(equalTo(aScheme().urin(anAuthority(), anAbsolutePath()))));
    }

    @Test
    public void aUrinWithNoQueryOrFragmentToStringIsCorrect() throws Exception {
        Scheme scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath path = anAbsolutePath();
        assertThat(scheme.urin(authority, path).toString(), equalTo("Urin{scheme=" + scheme.removeDefaultPort() + ", hierarchicalPart=" + hierarchicalPart(authority, path) + "}"));
    }

    @Test
    public void rejectsNullInFactoryForAUrinWithNoQueryOrNoFragment() throws Exception {
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Authority authority = null;
                aScheme().urin(authority, anAbsolutePath());
            }
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                AbsolutePath path = null;
                aScheme().urin(anAuthority(), path);
            }
        });
    }

    @Test
    public void aUrinWithNoQueryOrAuthorityOrPathIsEqualToAnotherWithTheSameParts() throws Exception {
        Scheme scheme = aScheme();
        Fragment fragment = aFragment();
        assertThat(scheme.urin(fragment), equalTo(scheme.urin(fragment)));
        assertThat(scheme.urin(fragment).hashCode(), equalTo(scheme.urin(fragment).hashCode()));
    }

    @Test
    public void aUrinWithNoQueryIsNotEqualToAnotherWithTheADifferentSchemeAndFragment() throws Exception {
        assertThat(aScheme().urin(aFragment()), not(equalTo(aScheme().urin(aFragment()))));
    }

    @Test
    public void aUrinWithNoQueryOrAuthorityOrPathToStringIsCorrect() throws Exception {
        Scheme scheme = aScheme();
        Fragment fragment = aFragment();
        assertThat(scheme.urin(fragment).toString(), equalTo("Urin{scheme=" + scheme.removeDefaultPort() + ", hierarchicalPart=" + hierarchicalPart() + ", fragment=" + fragment + "}"));
    }

    @Test
    public void rejectsNullInFactoryForAUrinWithNoQueryOrAuthorityOrPath() throws Exception {
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Fragment fragment = null;
                aScheme().urin(fragment);
            }
        });
    }

    @Test
    public void aUrinWithNoQueryOrAuthorityIsEqualToAnotherWithTheSameParts() throws Exception {
        Scheme scheme = aScheme();
        Path path = aPath();
        Fragment fragment = aFragment();
        assertThat(scheme.urin(path, fragment), equalTo(scheme.urin(path, fragment)));
        assertThat(scheme.urin(path, fragment).hashCode(), equalTo(scheme.urin(path, fragment).hashCode()));
    }

    @Test
    public void aUrinWithNoQueryIsNotEqualToAnotherWithTheADifferentPathAndFragment() throws Exception {
        assertThat(aScheme().urin(aPath(), aFragment()), not(equalTo(aScheme().urin(aPath(), aFragment()))));
    }

    @Test
    public void aUrinWithNoQueryOrAuthorityToStringIsCorrect() throws Exception {
        Scheme scheme = aScheme();
        Path path = aPath();
        Fragment fragment = aFragment();
        assertThat(scheme.urin(path, fragment).toString(), equalTo("Urin{scheme=" + scheme.removeDefaultPort() + ", hierarchicalPart=" + hierarchicalPart(path) + ", fragment=" + fragment + "}"));
    }

    @Test
    public void rejectsNullInFactoryForAUrinWithNoQueryOrAuthority() throws Exception {
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Path path = null;
                aScheme().urin(path, aFragment());
            }
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Fragment fragment = null;
                aScheme().urin(aPath(), fragment);
            }
        });
    }

    @Test
    public void aUrinWithNoQueryOrPathIsEqualToAnotherWithTheSameParts() throws Exception {
        Scheme scheme = aScheme();
        Authority authority = anAuthority();
        Fragment fragment = aFragment();
        assertThat(scheme.urin(authority, fragment), equalTo(scheme.urin(authority, fragment)));
        assertThat(scheme.urin(authority, fragment).hashCode(), equalTo(scheme.urin(authority, fragment).hashCode()));
    }

    @Test
    public void aUrinWithNoQueryIsNotEqualToAnotherWithTheADifferentAuthorityAndFragment() throws Exception {
        assertThat(aScheme().urin(anAuthority(), aFragment()), not(equalTo(aScheme().urin(anAuthority(), aFragment()))));
    }

    @Test
    public void aUrinWithNoQueryOrPathToStringIsCorrect() throws Exception {
        Scheme scheme = aScheme();
        Authority authority = anAuthority();
        Fragment fragment = aFragment();
        assertThat(scheme.urin(authority, fragment).toString(), equalTo("Urin{scheme=" + scheme.removeDefaultPort() + ", hierarchicalPart=" + hierarchicalPart(authority) + ", fragment=" + fragment + "}"));
    }

    @Test
    public void rejectsNullInFactoryForAUrinWithNoQueryOrPath() throws Exception {
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Authority authority = null;
                aScheme().urin(authority, aFragment());
            }
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Fragment fragment = null;
                aScheme().urin(anAuthority(), fragment);
            }
        });
    }

    @Test
    public void aUrinWithNoQueryIsEqualToAnotherWithTheSameParts() throws Exception {
        Scheme scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath path = anAbsolutePath();
        Fragment fragment = aFragment();
        assertThat(scheme.urin(authority, path, fragment), equalTo(scheme.urin(authority, path, fragment)));
        assertThat(scheme.urin(authority, path, fragment).hashCode(), equalTo(scheme.urin(authority, path, fragment).hashCode()));
    }

    @Test
    public void aUrinWithNoQueryOrFragmentIsNotEqualToAnotherWithTheADifferentAuthorityAndPathAndFragment() throws Exception {
        assertThat(aScheme().urin(anAuthority(), anAbsolutePath(), aFragment()), not(equalTo(aScheme().urin(anAuthority(), anAbsolutePath(), aFragment()))));
    }

    @Test
    public void aUrinWithNoQueryToStringIsCorrect() throws Exception {
        Scheme scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath path = anAbsolutePath();
        Fragment fragment = aFragment();
        assertThat(scheme.urin(authority, path, fragment).toString(), equalTo("Urin{scheme=" + scheme.removeDefaultPort() + ", hierarchicalPart=" + hierarchicalPart(authority, path) + ", fragment=" + fragment + "}"));
    }

    @Test
    public void rejectsNullInFactoryForAUrinWithNoQuery() throws Exception {
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Authority authority = null;
                aScheme().urin(authority, anAbsolutePath(), aFragment());
            }
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                AbsolutePath path = null;
                aScheme().urin(anAuthority(), path, aFragment());
            }
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Fragment fragment = null;
                aScheme().urin(anAuthority(), anAbsolutePath(), fragment);
            }
        });
    }

    @Test
    public void aUrinWithNoFrgmtOrAuthorityOrPathIsEqualToAnotherWithTheSameParts() throws Exception {
        Scheme scheme = aScheme();
        Fragment fragment = aFragment();
        assertThat(scheme.urin(fragment), equalTo(scheme.urin(fragment)));
        assertThat(scheme.urin(fragment).hashCode(), equalTo(scheme.urin(fragment).hashCode()));
    }

    @Test
    public void aUrinWithNoFrgmtIsNotEqualToAnotherWithTheADifferentSchemeAndFragment() throws Exception {
        assertThat(aScheme().urin(aFragment()), not(equalTo(aScheme().urin(aFragment()))));
    }

    @Test
    public void aUrinWithNoFrgmtOrAuthorityOrPathToStringIsCorrect() throws Exception {
        Scheme scheme = aScheme();
        Fragment fragment = aFragment();
        assertThat(scheme.urin(fragment).toString(), equalTo("Urin{scheme=" + scheme.removeDefaultPort() + ", hierarchicalPart=" + hierarchicalPart() + ", fragment=" + fragment + "}"));
    }

    @Test
    public void rejectsNullInFactoryForAUrinWithNoFrgmtOrAuthorityOrPath() throws Exception {
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Fragment fragment = null;
                aScheme().urin(fragment);
            }
        });
    }

    @Test
    public void aUrinWithNoFrgmtOrAuthorityIsEqualToAnotherWithTheSameParts() throws Exception {
        Scheme scheme = aScheme();
        Path path = aPath();
        Fragment fragment = aFragment();
        assertThat(scheme.urin(path, fragment), equalTo(scheme.urin(path, fragment)));
        assertThat(scheme.urin(path, fragment).hashCode(), equalTo(scheme.urin(path, fragment).hashCode()));
    }

    @Test
    public void aUrinWithNoFrgmtIsNotEqualToAnotherWithTheADifferentPathAndFragment() throws Exception {
        assertThat(aScheme().urin(aPath(), aFragment()), not(equalTo(aScheme().urin(aPath(), aFragment()))));
    }

    @Test
    public void aUrinWithNoFrgmtOrAuthorityToStringIsCorrect() throws Exception {
        Scheme scheme = aScheme();
        Path path = aPath();
        Fragment fragment = aFragment();
        assertThat(scheme.urin(path, fragment).toString(), equalTo("Urin{scheme=" + scheme.removeDefaultPort() + ", hierarchicalPart=" + hierarchicalPart(path) + ", fragment=" + fragment + "}"));
    }

    @Test
    public void rejectsNullInFactoryForAUrinWithNoFrgmtOrAuthority() throws Exception {
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Path path = null;
                aScheme().urin(path, aFragment());
            }
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Fragment fragment = null;
                aScheme().urin(aPath(), fragment);
            }
        });
    }

    @Test
    public void aUrinWithNoFrgmtOrPathIsEqualToAnotherWithTheSameParts() throws Exception {
        Scheme scheme = aScheme();
        Authority authority = anAuthority();
        Fragment fragment = aFragment();
        assertThat(scheme.urin(authority, fragment), equalTo(scheme.urin(authority, fragment)));
        assertThat(scheme.urin(authority, fragment).hashCode(), equalTo(scheme.urin(authority, fragment).hashCode()));
    }

    @Test
    public void aUrinWithNoFrgmtIsNotEqualToAnotherWithTheADifferentAuthorityAndFragment() throws Exception {
        assertThat(aScheme().urin(anAuthority(), aFragment()), not(equalTo(aScheme().urin(anAuthority(), aFragment()))));
    }

    @Test
    public void aUrinWithNoFrgmtOrPathToStringIsCorrect() throws Exception {
        Scheme scheme = aScheme();
        Authority authority = anAuthority();
        Fragment fragment = aFragment();
        assertThat(scheme.urin(authority, fragment).toString(), equalTo("Urin{scheme=" + scheme.removeDefaultPort() + ", hierarchicalPart=" + hierarchicalPart(authority) + ", fragment=" + fragment + "}"));
    }

    @Test
    public void rejectsNullInFactoryForAUrinWithNoFrgmtOrPath() throws Exception {
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Authority authority = null;
                aScheme().urin(authority, aFragment());
            }
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Fragment fragment = null;
                aScheme().urin(anAuthority(), fragment);
            }
        });
    }

    @Test
    public void aUrinWithNoFrgmtIsEqualToAnotherWithTheSameParts() throws Exception {
        Scheme scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath path = anAbsolutePath();
        Fragment fragment = aFragment();
        assertThat(scheme.urin(authority, path, fragment), equalTo(scheme.urin(authority, path, fragment)));
        assertThat(scheme.urin(authority, path, fragment).hashCode(), equalTo(scheme.urin(authority, path, fragment).hashCode()));
    }

    @Test
    public void aUrinWithNoFrgmtOrFragmentIsNotEqualToAnotherWithTheADifferentAuthorityAndPathAndFragment() throws Exception {
        assertThat(aScheme().urin(anAuthority(), anAbsolutePath(), aFragment()), not(equalTo(aScheme().urin(anAuthority(), anAbsolutePath(), aFragment()))));
    }

    @Test
    public void aUrinWithNoFrgmtToStringIsCorrect() throws Exception {
        Scheme scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath path = anAbsolutePath();
        Fragment fragment = aFragment();
        assertThat(scheme.urin(authority, path, fragment).toString(), equalTo("Urin{scheme=" + scheme.removeDefaultPort() + ", hierarchicalPart=" + hierarchicalPart(authority, path) + ", fragment=" + fragment + "}"));
    }

    @Test
    public void rejectsNullInFactoryForAUrinWithNoFrgmt() throws Exception {
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Authority authority = null;
                aScheme().urin(authority, anAbsolutePath(), aFragment());
            }
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                AbsolutePath path = null;
                aScheme().urin(anAuthority(), path, aFragment());
            }
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Fragment fragment = null;
                aScheme().urin(anAuthority(), anAbsolutePath(), fragment);
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
