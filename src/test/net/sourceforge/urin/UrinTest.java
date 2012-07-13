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
        assertThat(scheme.urin(hierarchicalPart, query, fragment).asString(), equalTo(scheme.asString() + ":" + hierarchicalPart.asString() + "?" + query.asString() + "#" + fragment.asString()));
    }

    @Test
    public void urinWithAllPartsPathIsCorrect() throws Exception {
        Scheme scheme = aScheme();
        HierarchicalPart hierarchicalPart = aHierarchicalPart();
        Query query = aQuery();
        Fragment fragment = aFragment();
        assertThat(scheme.urin(hierarchicalPart, query, fragment).path(), equalTo(hierarchicalPart.path()));
    }

    @Test
    public void urinWithAllPartsQueryIsCorrect() throws Exception {
        Scheme scheme = aScheme();
        HierarchicalPart hierarchicalPart = aHierarchicalPart();
        Query query = aQuery();
        Fragment fragment = aFragment();
        assertThat(scheme.urin(hierarchicalPart, query, fragment).hasQuery(), equalTo(true));
        assertThat(scheme.urin(hierarchicalPart, query, fragment).query(), equalTo(query));
    }

    @Test
    public void urinWithAllPartsFragmentIsCorrect() throws Exception {
        Scheme scheme = aScheme();
        HierarchicalPart hierarchicalPart = aHierarchicalPart();
        Query query = aQuery();
        Fragment fragment = aFragment();
        assertThat(scheme.urin(hierarchicalPart, query, fragment).hasFragment(), equalTo(true));
        assertThat(scheme.urin(hierarchicalPart, query, fragment).fragment(), equalTo(fragment));
    }

    @Test
    public void aUrinWithAllPartsIsEqualToAnotherWithTheSamePartsWithHierarchicalPart() throws Exception {
        Scheme scheme = aScheme();
        HierarchicalPart hierarchicalPart = aHierarchicalPart();
        Query query = aQuery();
        Fragment fragment = aFragment();
        assertThat(scheme.urin(hierarchicalPart, query, fragment), equalTo(scheme.urin(hierarchicalPart, query, fragment)));
        assertThat(scheme.urin(hierarchicalPart, query, fragment).hashCode(), equalTo(scheme.urin(hierarchicalPart, query, fragment).hashCode()));
    }

    @Test
    public void aUrinWithAllPartsIsNotEqualToAnotherWithTheADifferentParts() throws Exception {
        assertThat(aScheme().urin(aHierarchicalPart(), aQuery(), aFragment()), not(equalTo(aScheme().urin(aHierarchicalPart(), aQuery(), aFragment()))));
    }

    @Test
    public void aUrinWithAllPartsToStringIsCorrectWithHierarchicalPart() throws Exception {
        Scheme scheme = aScheme();
        HierarchicalPart hierarchicalPart = aHierarchicalPart();
        Query query = aQuery();
        Fragment fragment = aFragment();
        assertThat(scheme.urin(hierarchicalPart, query, fragment).toString(), equalTo("Urin{scheme=" + scheme.removeDefaultPort() + ", hierarchicalPart=" + hierarchicalPart + ", query=" + query + ", fragment=" + fragment + "}"));
    }

    @Test
    public void rejectsNullInFactoryForAUrinWithAllPartsWithHierarchicalPart() throws Exception {
        assertThrowsException("Null scheme should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                urin(null, aHierarchicalPart(), aQuery(), aFragment());
            }
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                HierarchicalPart hierarchicalPart = null;
                aScheme().urin(hierarchicalPart, aQuery(), aFragment());
            }
        });
        assertThrowsException("Null query should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                aScheme().urin(aHierarchicalPart(), null, aFragment());
            }
        });
        assertThrowsException("Null fragment should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                aScheme().urin(aHierarchicalPart(), aQuery(), null);
            }
        });
    }

    @Test
    public void createsUrinWithNoFragment() throws Exception {
        Scheme scheme = aScheme();
        HierarchicalPart hierarchicalPart = aHierarchicalPart();
        Query query = aQuery();
        assertThat(scheme.urin(hierarchicalPart, query).asString(), equalTo(scheme.asString() + ":" + hierarchicalPart.asString() + "?" + query.asString()));
    }

    @Test
    public void urinWithNoFragmentPathIsCorrect() throws Exception {
        Scheme scheme = aScheme();
        HierarchicalPart hierarchicalPart = aHierarchicalPart();
        Query query = aQuery();
        assertThat(scheme.urin(hierarchicalPart, query).path(), equalTo(hierarchicalPart.path()));
    }

    @Test
    public void urinWithNoFragmentQueryIsCorrect() throws Exception {
        Scheme scheme = aScheme();
        HierarchicalPart hierarchicalPart = aHierarchicalPart();
        Query query = aQuery();
        final Urin urin = scheme.urin(hierarchicalPart, query);
        assertThat(urin.hasQuery(), equalTo(true));
        assertThat(scheme.urin(hierarchicalPart, query).query(), equalTo(query));
    }

    @Test
    public void urinWithNoFragmentFragmentIsCorrect() throws Exception {
        Scheme scheme = aScheme();
        HierarchicalPart hierarchicalPart = aHierarchicalPart();
        Query query = aQuery();
        final Urin urin = scheme.urin(hierarchicalPart, query);
        assertThat(urin.hasFragment(), equalTo(false));
        assertThrowsException("Attempt to get fragment from a UrinReference that does not have one.", UnsupportedOperationException.class, new ExceptionAssert.ExceptionThrower<java.lang.UnsupportedOperationException>() {
            public void execute() throws UnsupportedOperationException {
                urin.fragment();
            }
        });
    }

    @Test
    public void aUrinWithNoFragmentIsEqualToAnotherWithTheSamePartsWithHierarchicalPart() throws Exception {
        Scheme scheme = aScheme();
        HierarchicalPart hierarchicalPart = aHierarchicalPart();
        Query query = aQuery();
        assertThat(scheme.urin(hierarchicalPart, query), equalTo(scheme.urin(hierarchicalPart, query)));
        assertThat(scheme.urin(hierarchicalPart, query).hashCode(), equalTo(scheme.urin(hierarchicalPart, query).hashCode()));
    }

    @Test
    public void aUrinWithNoFragmentIsNotEqualToAnotherWithTheADifferentParts() throws Exception {
        assertThat(aScheme().urin(aHierarchicalPart(), aQuery()), not(equalTo(aScheme().urin(aHierarchicalPart(), aQuery()))));
    }

    @Test
    public void aUrinWithNoFragmentWithHierarchicalPartToStringIsCorrect() throws Exception {
        Scheme scheme = aScheme();
        HierarchicalPart hierarchicalPart = aHierarchicalPart();
        Query query = aQuery();
        assertThat(scheme.urin(hierarchicalPart, query).toString(), equalTo("Urin{scheme=" + scheme.removeDefaultPort() + ", hierarchicalPart=" + hierarchicalPart + ", query=" + query + "}"));
    }

    @Test
    public void rejectsNullInFactoryForAUrinWithNoFragmentWithHierarchicalPart() throws Exception {
        assertThrowsException("Null scheme should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                urin(null, aHierarchicalPart(), aQuery());
            }
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                HierarchicalPart hierarchicalPart = null;
                aScheme().urin(hierarchicalPart, aQuery());
            }
        });
        assertThrowsException("Null query should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Query query = null;
                aScheme().urin(aHierarchicalPart(), query);
            }
        });
    }

    @Test
    public void createsUrinWithNoQuery() throws Exception {
        Scheme scheme = aScheme();
        HierarchicalPart hierarchicalPart = aHierarchicalPart();
        Fragment fragment = aFragment();
        assertThat(scheme.urin(hierarchicalPart, fragment).asString(), equalTo(scheme.asString() + ":" + hierarchicalPart.asString() + "#" + fragment.asString()));
    }

    @Test
    public void urinWithNoQueryPathIsCorrect() throws Exception {
        Scheme scheme = aScheme();
        HierarchicalPart hierarchicalPart = aHierarchicalPart();
        Fragment fragment = aFragment();
        assertThat(scheme.urin(hierarchicalPart, fragment).path(), equalTo(hierarchicalPart.path()));
    }

    @Test
    public void urinWithNoQueryQueryIsCorrect() throws Exception {
        Scheme scheme = aScheme();
        HierarchicalPart hierarchicalPart = aHierarchicalPart();
        Fragment fragment = aFragment();
        final Urin urin = scheme.urin(hierarchicalPart, fragment);
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
        assertThat(scheme.urin(hierarchicalPart, fragment).hasFragment(), equalTo(true));
        assertThat(scheme.urin(hierarchicalPart, fragment).fragment(), equalTo(fragment));
    }

    @Test
    public void aUrinWithNoQueryIsEqualToAnotherWithTheSamePartsWithHierarchcialPart() throws Exception {
        Scheme scheme = aScheme();
        HierarchicalPart hierarchicalPart = aHierarchicalPart();
        Fragment fragment = aFragment();
        assertThat(scheme.urin(hierarchicalPart, fragment), equalTo(scheme.urin(hierarchicalPart, fragment)));
        assertThat(scheme.urin(hierarchicalPart, fragment).hashCode(), equalTo(scheme.urin(hierarchicalPart, fragment).hashCode()));
    }

    @Test
    public void aUrinWithNoQueryIsNotEqualToAnotherWithTheADifferentParts() throws Exception {
        assertThat(aScheme().urin(aHierarchicalPart(), aFragment()), not(equalTo(aScheme().urin(aHierarchicalPart(), aFragment()))));
    }

    @Test
    public void aUrinWithNoQueryWithHierarchcialPartToStringIsCorrect() throws Exception {
        Scheme scheme = aScheme();
        HierarchicalPart hierarchicalPart = aHierarchicalPart();
        Fragment fragment = aFragment();
        assertThat(scheme.urin(hierarchicalPart, fragment).toString(), equalTo("Urin{scheme=" + scheme.removeDefaultPort() + ", hierarchicalPart=" + hierarchicalPart + ", fragment=" + fragment + "}"));
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
                HierarchicalPart hierarchicalPart = null;
                aScheme().urin(hierarchicalPart, aFragment());
            }
        });
        assertThrowsException("Null fragment should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Fragment fragment = null;
                aScheme().urin(aHierarchicalPart(), fragment);
            }
        });
    }

    @Test
    public void createsUrinWithNoQueryAndNoFragment() throws Exception {
        Scheme scheme = aScheme();
        HierarchicalPart hierarchicalPart = aHierarchicalPart();
        assertThat(scheme.urin(hierarchicalPart).asString(), equalTo(scheme.asString() + ":" + hierarchicalPart.asString()));
    }

    @Test
    public void urinWithNoQueryAndNoFragmentPathIsCorrect() throws Exception {
        Scheme scheme = aScheme();
        HierarchicalPart hierarchicalPart = aHierarchicalPart();
        final Urin urin = scheme.urin(hierarchicalPart);
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
        final Urin urin = scheme.urin(hierarchicalPart);
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
        assertThat(scheme.urin(hierarchicalPart).hasFragment(), equalTo(false));
    }

    @Test
    public void aUrinWithNoQueryOrFragmentIsEqualToAnotherWithTheSamePartsUsingHierarchicalPart() throws Exception {
        Scheme scheme = aScheme();
        HierarchicalPart hierarchicalPart = aHierarchicalPart();
        assertThat(scheme.urin(hierarchicalPart), equalTo(scheme.urin(hierarchicalPart)));
        assertThat(scheme.urin(hierarchicalPart).hashCode(), equalTo(scheme.urin(hierarchicalPart).hashCode()));
    }

    @Test
    public void aUrinWithNoQueryOrFragmentIsNotEqualToAnotherWithTheADifferentParts() throws Exception {
        assertThat(aScheme().urin(aHierarchicalPart()), not(equalTo(aScheme().urin(aHierarchicalPart()))));
    }

    @Test
    public void aUrinWithNoQueryOrFragmentUsingHierarchicalPartToStringIsCorrect() throws Exception {
        Scheme scheme = aScheme();
        HierarchicalPart hierarchicalPart = aHierarchicalPart();
        assertThat(scheme.urin(hierarchicalPart).toString(), equalTo("Urin{scheme=" + scheme.removeDefaultPort() + ", hierarchicalPart=" + hierarchicalPart + "}"));
    }

    @Test
    public void rejectsNullInFactoryForAUrinWithNoQueryOrNoFragmentUsingHierarchicalPart() throws Exception {
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                HierarchicalPart hierarchicalPart = null;
                aScheme().urin(hierarchicalPart);
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
    public void aUrinWithNoFragmentOrAuthorityOrPathIsEqualToAnotherWithTheSameParts() throws Exception {
        Scheme scheme = aScheme();
        Query query = aQuery();
        assertThat(scheme.urin(query), equalTo(scheme.urin(query)));
        assertThat(scheme.urin(query).hashCode(), equalTo(scheme.urin(query).hashCode()));
    }

    @Test
    public void aUrinWithNoFragmentIsNotEqualToAnotherWithTheADifferentSchemeAndQuery() throws Exception {
        assertThat(aScheme().urin(aQuery()), not(equalTo(aScheme().urin(aQuery()))));
    }

    @Test
    public void aUrinWithNoFragmentOrAuthorityOrPathToStringIsCorrect() throws Exception {
        Scheme scheme = aScheme();
        Query query = aQuery();
        assertThat(scheme.urin(query).toString(), equalTo("Urin{scheme=" + scheme.removeDefaultPort() + ", hierarchicalPart=" + hierarchicalPart() + ", query=" + query + "}"));
    }

    @Test
    public void rejectsNullInFactoryForAUrinWithNoFragmentOrAuthorityOrPath() throws Exception {
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Query query = null;
                aScheme().urin(query);
            }
        });
    }

    @Test
    public void aUrinWithNoFragmentOrAuthorityIsEqualToAnotherWithTheSameParts() throws Exception {
        Scheme scheme = aScheme();
        Path path = aPath();
        Query query = aQuery();
        assertThat(scheme.urin(path, query), equalTo(scheme.urin(path, query)));
        assertThat(scheme.urin(path, query).hashCode(), equalTo(scheme.urin(path, query).hashCode()));
    }

    @Test
    public void aUrinWithNoFragmentIsNotEqualToAnotherWithTheADifferentPathAndQuery() throws Exception {
        assertThat(aScheme().urin(aPath(), aQuery()), not(equalTo(aScheme().urin(aPath(), aQuery()))));
    }

    @Test
    public void aUrinWithNoFragmentOrAuthorityToStringIsCorrect() throws Exception {
        Scheme scheme = aScheme();
        Path path = aPath();
        Query query = aQuery();
        assertThat(scheme.urin(path, query).toString(), equalTo("Urin{scheme=" + scheme.removeDefaultPort() + ", hierarchicalPart=" + hierarchicalPart(path) + ", query=" + query + "}"));
    }

    @Test
    public void rejectsNullInFactoryForAUrinWithNoFragmentOrAuthority() throws Exception {
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Path path = null;
                aScheme().urin(path, aQuery());
            }
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Query query = null;
                aScheme().urin(aPath(), query);
            }
        });
    }

    @Test
    public void aUrinWithNoFragmentOrPathIsEqualToAnotherWithTheSameParts() throws Exception {
        Scheme scheme = aScheme();
        Authority authority = anAuthority();
        Query query = aQuery();
        assertThat(scheme.urin(authority, query), equalTo(scheme.urin(authority, query)));
        assertThat(scheme.urin(authority, query).hashCode(), equalTo(scheme.urin(authority, query).hashCode()));
    }

    @Test
    public void aUrinWithNoFragmentIsNotEqualToAnotherWithTheADifferentAuthorityAndQuery() throws Exception {
        assertThat(aScheme().urin(anAuthority(), aQuery()), not(equalTo(aScheme().urin(anAuthority(), aQuery()))));
    }

    @Test
    public void aUrinWithNoFragmentOrPathToStringIsCorrect() throws Exception {
        Scheme scheme = aScheme();
        Authority authority = anAuthority();
        Query query = aQuery();
        assertThat(scheme.urin(authority, query).toString(), equalTo("Urin{scheme=" + scheme.removeDefaultPort() + ", hierarchicalPart=" + hierarchicalPart(authority) + ", query=" + query + "}"));
    }

    @Test
    public void rejectsNullInFactoryForAUrinWithNoFragmentOrPath() throws Exception {
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Authority authority = null;
                aScheme().urin(authority, aQuery());
            }
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Query query = null;
                aScheme().urin(anAuthority(), query);
            }
        });
    }

    @Test
    public void aUrinWithNoFragmentIsEqualToAnotherWithTheSameParts() throws Exception {
        Scheme scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath path = anAbsolutePath();
        Query query = aQuery();
        assertThat(scheme.urin(authority, path, query), equalTo(scheme.urin(authority, path, query)));
        assertThat(scheme.urin(authority, path, query).hashCode(), equalTo(scheme.urin(authority, path, query).hashCode()));
    }

    @Test
    public void aUrinWithNoFragmentOrQueryIsNotEqualToAnotherWithTheADifferentAuthorityAndPathAndQuery() throws Exception {
        assertThat(aScheme().urin(anAuthority(), anAbsolutePath(), aQuery()), not(equalTo(aScheme().urin(anAuthority(), anAbsolutePath(), aQuery()))));
    }

    @Test
    public void aUrinWithNoFragmentToStringIsCorrect() throws Exception {
        Scheme scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath path = anAbsolutePath();
        Query query = aQuery();
        assertThat(scheme.urin(authority, path, query).toString(), equalTo("Urin{scheme=" + scheme.removeDefaultPort() + ", hierarchicalPart=" + hierarchicalPart(authority, path) + ", query=" + query + "}"));
    }

    @Test
    public void rejectsNullInFactoryForAUrinWithNoFragment() throws Exception {
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Authority authority = null;
                aScheme().urin(authority, anAbsolutePath(), aQuery());
            }
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                AbsolutePath path = null;
                aScheme().urin(anAuthority(), path, aQuery());
            }
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Query query = null;
                aScheme().urin(anAuthority(), anAbsolutePath(), query);
            }
        });
    }

    @Test
    public void aUrinWithNoAuthorityOrPathIsEqualToAnotherWithTheSameParts() throws Exception {
        Scheme scheme = aScheme();
        Query query = aQuery();
        Fragment fragment = aFragment();
        assertThat(scheme.urin(query, fragment), equalTo(scheme.urin(query, fragment)));
        assertThat(scheme.urin(query, fragment).hashCode(), equalTo(scheme.urin(query, fragment).hashCode()));
    }

    @Test
    public void aUrinWithNoAuthorityOrPathIsNotEqualToAnotherWithTheADifferentSchemeAndQuery() throws Exception {
        assertThat(aScheme().urin(aQuery(), aFragment()), not(equalTo(aScheme().urin(aQuery(), aFragment()))));
    }

    @Test
    public void aUrinWithNoAuthorityOrPathToStringIsCorrect() throws Exception {
        Scheme scheme = aScheme();
        Query query = aQuery();
        Fragment fragment = aFragment();
        assertThat(scheme.urin(query, fragment).toString(), equalTo("Urin{scheme=" + scheme.removeDefaultPort() + ", hierarchicalPart=" + hierarchicalPart() + ", query=" + query + ", fragment=" + fragment + "}"));
    }

    @Test
    public void rejectsNullInFactoryForAUrinWithNoAuthorityOrPath() throws Exception {
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Query query = null;
                aScheme().urin(query, aFragment());
            }
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Fragment fragment = null;
                aScheme().urin(aQuery(), fragment);
            }
        });
    }

    @Test
    public void aUrinWithNoAuthorityIsEqualToAnotherWithTheSameParts() throws Exception {
        Scheme scheme = aScheme();
        Path path = aPath();
        Query query = aQuery();
        Fragment fragment = aFragment();
        assertThat(scheme.urin(path, query, fragment), equalTo(scheme.urin(path, query, fragment)));
        assertThat(scheme.urin(path, query, fragment).hashCode(), equalTo(scheme.urin(path, query, fragment).hashCode()));
    }

    @Test
    public void aUrinWithNoAuthorityIsNotEqualToAnotherWithTheADifferentPathAndQuery() throws Exception {
        assertThat(aScheme().urin(aPath(), aQuery(), aFragment()), not(equalTo(aScheme().urin(aPath(), aQuery(), aFragment()))));
    }

    @Test
    public void aUrinWithNoAuthorityToStringIsCorrect() throws Exception {
        Scheme scheme = aScheme();
        Path path = aPath();
        Query query = aQuery();
        Fragment fragment = aFragment();
        assertThat(scheme.urin(path, query, fragment).toString(), equalTo("Urin{scheme=" + scheme.removeDefaultPort() + ", hierarchicalPart=" + hierarchicalPart(path) + ", query=" + query + ", fragment=" + fragment + "}"));
    }

    @Test
    public void rejectsNullInFactoryForAUrinWithNoAuthority() throws Exception {
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Path path = null;
                aScheme().urin(path, aQuery(), aFragment());
            }
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Query query = null;
                aScheme().urin(aPath(), query, aFragment());
            }
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Fragment fragment = null;
                aScheme().urin(aPath(), aQuery(), fragment);
            }
        });
    }

    @Test
    public void aUrinWithNoPathIsEqualToAnotherWithTheSameParts() throws Exception {
        Scheme scheme = aScheme();
        Authority authority = anAuthority();
        Query query = aQuery();
        Fragment fragment = aFragment();
        assertThat(scheme.urin(authority, query, fragment), equalTo(scheme.urin(authority, query, fragment)));
        assertThat(scheme.urin(authority, query, fragment).hashCode(), equalTo(scheme.urin(authority, query, fragment).hashCode()));
    }

    @Test
    public void aUrinWithNoPathIsNotEqualToAnotherWithTheADifferentAuthorityAndQuery() throws Exception {
        assertThat(aScheme().urin(anAuthority(), aQuery(), aFragment()), not(equalTo(aScheme().urin(anAuthority(), aQuery(), aFragment()))));
    }

    @Test
    public void aUrinWithNoPathToStringIsCorrect() throws Exception {
        Scheme scheme = aScheme();
        Authority authority = anAuthority();
        Query query = aQuery();
        Fragment fragment = aFragment();
        assertThat(scheme.urin(authority, query, fragment).toString(), equalTo("Urin{scheme=" + scheme.removeDefaultPort() + ", hierarchicalPart=" + hierarchicalPart(authority) + ", query=" + query + ", fragment=" + fragment + "}"));
    }

    @Test
    public void rejectsNullInFactoryForAUrinWithNoPath() throws Exception {
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Authority authority = null;
                aScheme().urin(authority, aQuery(), aFragment());
            }
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Query query = null;
                aScheme().urin(anAuthority(), query, aFragment());
            }
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Fragment fragment = null;
                aScheme().urin(anAuthority(), aQuery(), fragment);
            }
        });
    }

    @Test
    public void aUrinWithAllPartsIsEqualToAnotherWithTheSameParts() throws Exception {
        Scheme scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath path = anAbsolutePath();
        Query query = aQuery();
        Fragment fragment = aFragment();
        assertThat(scheme.urin(authority, path, query, fragment), equalTo(scheme.urin(authority, path, query, fragment)));
        assertThat(scheme.urin(authority, path, query, fragment).hashCode(), equalTo(scheme.urin(authority, path, query, fragment).hashCode()));
    }

    @Test
    public void aUrinWithAllPartsIsNotEqualToAnotherWithTheADifferentAuthorityAndPathAndQuery() throws Exception {
        assertThat(aScheme().urin(anAuthority(), anAbsolutePath(), aQuery(), aFragment()), not(equalTo(aScheme().urin(anAuthority(), anAbsolutePath(), aQuery(), aFragment()))));
    }

    @Test
    public void aUrinWithAllPartsToStringIsCorrect() throws Exception {
        Scheme scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath path = anAbsolutePath();
        Query query = aQuery();
        Fragment fragment = aFragment();
        assertThat(scheme.urin(authority, path, query, fragment).toString(), equalTo("Urin{scheme=" + scheme.removeDefaultPort() + ", hierarchicalPart=" + hierarchicalPart(authority, path) + ", query=" + query + ", fragment=" + fragment + "}"));
    }

    @Test
    public void rejectsNullInFactoryForAUrinWithAllParts() throws Exception {
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Authority authority = null;
                aScheme().urin(authority, anAbsolutePath(), aQuery(), aFragment());
            }
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                AbsolutePath path = null;
                aScheme().urin(anAuthority(), path, aQuery(), aFragment());
            }
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Query query = null;
                aScheme().urin(anAuthority(), anAbsolutePath(), query, aFragment());
            }
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Fragment fragment = null;
                aScheme().urin(anAuthority(), anAbsolutePath(), aQuery(), fragment);
            }
        });
    }

    @Test
    public void parsesUrinWithAllParts() throws Exception {
        Scheme scheme = aScheme();
        HierarchicalPart hierarchicalPart = aHierarchicalPart();
        Query query = aQuery();
        Fragment fragment = aFragment();
        assertThat(Urin.parse(scheme.asString() + ":" + hierarchicalPart.asString() + "?" + query.asString() + "#" + fragment.asString()), equalTo(scheme.urin(hierarchicalPart, query, fragment)));
    }

    @Test
    public void parsesUrinWithNoFragment() throws Exception {
        Scheme scheme = aScheme();
        HierarchicalPart hierarchicalPart = aHierarchicalPart();
        Query query = aQuery();
        assertThat(parse(scheme.asString() + ":" + hierarchicalPart.asString() + "?" + query.asString()), equalTo(scheme.urin(hierarchicalPart, query)));
    }

    @Test
    public void parsesUrinWithNoQuery() throws Exception {
        Scheme scheme = aScheme();
        HierarchicalPart hierarchicalPart = aHierarchicalPart();
        Fragment fragment = aFragment();
        assertThat(parse(scheme.asString() + ":" + hierarchicalPart.asString() + "#" + fragment.asString()), equalTo(scheme.urin(hierarchicalPart, fragment)));
    }

    @Test
    public void parsesUrinWithNoQueryAndNoFragment() throws Exception {
        Scheme scheme = aScheme();
        HierarchicalPart hierarchicalPart = aHierarchicalPart();
        assertThat(parse(scheme.asString() + ":" + hierarchicalPart.asString()), equalTo(scheme.urin(hierarchicalPart)));
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
        assertThat(scheme(schemeName, aPort()).urin(hierarchicalPart), equalTo(scheme(schemeName).urin(hierarchicalPart)));
    }

    @Test
    public void UrinCreatedWithASchemeSpecifyingADefaultPortEqualToThatUsedIsEqualToIdenticalUrinCreatedWithASchemeWithoutDefaultPortAndWithoutPort() throws Exception {
        String schemeName = aValidSchemeName();
        Port port = aPort();
        Host host = aHost();
        assertThat(scheme(schemeName, port).urin(hierarchicalPart(authority(host, port))), equalTo(scheme(schemeName).urin(hierarchicalPart(authority(host)))));
    }
}
