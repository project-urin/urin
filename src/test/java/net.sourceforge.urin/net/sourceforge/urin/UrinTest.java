/*
 * Copyright 2019 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.urin;

import org.junit.jupiter.api.Test;

import static net.sourceforge.urin.Authority.authority;
import static net.sourceforge.urin.AuthorityBuilder.anAuthority;
import static net.sourceforge.urin.ExceptionAssert.assertThrowsException;
import static net.sourceforge.urin.FragmentBuilder.aFragment;
import static net.sourceforge.urin.HostBuilder.aHost;
import static net.sourceforge.urin.Path.PrefixWithDotSegmentCriteria.NEVER_PREFIX_WITH_DOT_SEGMENT;
import static net.sourceforge.urin.Path.PrefixWithDotSegmentCriteria.PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY;
import static net.sourceforge.urin.PathBuilder.*;
import static net.sourceforge.urin.PortBuilder.aPort;
import static net.sourceforge.urin.QueryBuilder.aQuery;
import static net.sourceforge.urin.Scheme.scheme;
import static net.sourceforge.urin.SchemeBuilder.aScheme;
import static net.sourceforge.urin.SchemeBuilder.aValidSchemeName;
import static net.sourceforge.urin.UrinBuilder.aUrin;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

public class UrinTest {

    @Test
    public void createsUrinWithAllParts() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anAbsolutePath();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(authority, path, query, fragment).asString(), equalTo(scheme.asString() + "://" + authority.asString() + path.asString(NEVER_PREFIX_WITH_DOT_SEGMENT) + "?" + query.asString() + "#" + fragment.asString()));
    }

    @Test
    public void urinWithAllPartsAuthorityIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anAbsolutePath();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        final Urin urin = scheme.urin(authority, path, query, fragment);
        assertThat(urin.hasAuthority(), equalTo(true));
        assertThat(urin.authority(), equalTo(authority));
    }

    @Test
    public void urinWithAllPartsPathIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anAbsolutePath();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(authority, path, query, fragment).path(), equalTo((Path) path));
    }

    @Test
    public void urinWithAllPartsQueryIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anAbsolutePath();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(authority, path, query, fragment).hasQuery(), equalTo(true));
        assertThat(scheme.urin(authority, path, query, fragment).query(), equalTo(query));
    }

    @Test
    public void urinWithAllPartsButAuthorityAuthorityIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Path<String> path = aPath();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        final Urin urin = scheme.urin(path, query, fragment);
        assertThat(urin.hasAuthority(), equalTo(false));
        assertThrowsException("Attempt to get authority from a UrinReference that does not have one.", UnsupportedOperationException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws UnsupportedOperationException {
                urin.authority();
            }
        });
    }

    @Test
    public void urinWithAllPartsButAuthorityQueryIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Path<String> path = aPath();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(path, query, fragment).hasQuery(), equalTo(true));
        assertThat(scheme.urin(path, query, fragment).query(), equalTo(query));
    }

    @Test
    public void urinWithAllPartsButPathQueryIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(authority, query, fragment).hasQuery(), equalTo(true));
        assertThat(scheme.urin(authority, query, fragment).query(), equalTo(query));
    }

    @Test
    public void urinWithAllPartsButAuthorityAndPathQueryIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(query, fragment).hasQuery(), equalTo(true));
        assertThat(scheme.urin(query, fragment).query(), equalTo(query));
    }

    @Test
    public void urinWithAllPartsFragmentIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anAbsolutePath();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(authority, path, query, fragment).hasFragment(), equalTo(true));
        assertThat(scheme.urin(authority, path, query, fragment).fragment(), equalTo(fragment));
    }

    @Test
    public void aUrinWithAllPartsIsNotEqualToAnotherWithTheADifferentParts() throws Exception {
        assertThat(aScheme().urin(anAuthority(), anAbsolutePath(), aQuery(), aFragment()), not(equalTo(aScheme().urin(anAuthority(), anAbsolutePath(), aQuery(), aFragment()))));
    }

    @Test
    public void aUrinWithAllPartsToStringIsCorrectWithHierarchicalPart() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anAbsolutePath();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(authority, path, query, fragment).toString(), equalTo(scheme.urin(authority, path, query, fragment).asString()));
    }


    @Test
    public void urinWithNoFragmentAuthorityIsCorrect() throws Exception {
        AbsolutePath<String> path = anAbsolutePath();
        final Authority authority = anAuthority();
        final Urin urin = aScheme().urin(authority, path, aQuery());
        assertThat(urin.hasAuthority(), equalTo(true));
        assertThat(urin.authority(), equalTo(authority));
    }

    @Test
    public void urinWithNoFragmentPathIsCorrect() throws Exception {
        AbsolutePath<String> path = anAbsolutePath();
        assertThat(aScheme().urin(anAuthority(), path, aQuery()).path(), equalTo((Path) path));
    }

    @Test
    public void urinWithNoFragmentOrPathAuthorityIsCorrect() throws Exception {
        final Urin urin = aScheme().urin(aQuery());
        assertThat(urin.hasAuthority(), equalTo(false));
        assertThrowsException("Attempt to get authority from a UrinReference that does not have one.", UnsupportedOperationException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws UnsupportedOperationException {
                urin.authority();
            }
        });
    }

    @Test
    public void urinWithNoFragmentOrPathPathIsCorrect() throws Exception {
        assertThat(aScheme().urin(aQuery()).path(), equalTo((Path) new EmptyPath()));
    }

    @Test
    public void urinWithNoFragmentOrAuthorityAuthorityIsCorrect() throws Exception {
        Path<String> path = aPath();
        final Urin urin = aScheme().urin(path, aQuery());
        assertThat(urin.hasAuthority(), equalTo(false));
        assertThrowsException("Attempt to get authority from a UrinReference that does not have one.", UnsupportedOperationException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws UnsupportedOperationException {
                urin.authority();
            }
        });
    }

    @Test
    public void urinWithNoFragmentOrAuthorityPathIsCorrect() throws Exception {
        Path<String> path = aPath();
        assertThat(aScheme().urin(path, aQuery()).path(), equalTo(path));
    }

    @Test
    public void urinWithNoFragmentOrAuthorityOrPathAuthorityIsCorrect() throws Exception {
        final Urin urin = aScheme().urin(aQuery());
        assertThat(urin.hasAuthority(), equalTo(false));
        assertThrowsException("Attempt to get authority from a UrinReference that does not have one.", UnsupportedOperationException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws UnsupportedOperationException {
                urin.authority();
            }
        });
    }

    @Test
    public void urinWithNoFragmentOrAuthorityOrPathPathIsCorrect() throws Exception {
        assertThat(aScheme().urin(aQuery()).path(), equalTo((Path) new EmptyPath()));
    }

    @Test
    public void urinWithNoFragmentOrAuthorityQueryIsCorrect() throws Exception {
        Query<String> query = aQuery();
        final Urin<String, Query<String>, Fragment<String>> urin = aScheme().urin(aPath(), query);
        assertThat(urin.hasQuery(), equalTo(true));
        assertThat(urin.query(), equalTo(query));
    }

    @Test
    public void urinWithNoFragmentOrPathQueryIsCorrect() throws Exception {
        Query<String> query = aQuery();
        final Urin<String, Query<String>, Fragment<String>> urin = aScheme().urin(anAuthority(), query);
        assertThat(urin.hasQuery(), equalTo(true));
        assertThat(urin.query(), equalTo(query));
    }

    @Test
    public void urinWithNoFragmentOrAuthorityOrPathQueryIsCorrect() throws Exception {
        Query<String> query = aQuery();
        final Urin<String, Query<String>, Fragment<String>> urin = aScheme().urin(query);
        assertThat(urin.hasQuery(), equalTo(true));
        assertThat(urin.query(), equalTo(query));
    }

    @Test
    public void urinWithNoFragmentQueryIsCorrect() throws Exception {
        Query<String> query = aQuery();
        final Urin<String, Query<String>, Fragment<String>> urin = aScheme().urin(anAuthority(), anAbsolutePath(), query);
        assertThat(urin.hasQuery(), equalTo(true));
        assertThat(urin.query(), equalTo(query));
    }

    @Test
    public void urinWithNoFragmentFragmentIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Query<String> query = aQuery();
        final Urin urin = scheme.urin(anAuthority(), anAbsolutePath(), query);
        assertThat(urin.hasFragment(), equalTo(false));
        assertThrowsException("Attempt to get fragment from a UrinReference that does not have one.", UnsupportedOperationException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws UnsupportedOperationException {
                urin.fragment();
            }
        });
    }

    @Test
    public void urinWithNoFragmentOrAuthorityFragmentIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Query<String> query = aQuery();
        final Urin urin = scheme.urin(aPath(), query);
        assertThat(urin.hasFragment(), equalTo(false));
        assertThrowsException("Attempt to get fragment from a UrinReference that does not have one.", UnsupportedOperationException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws UnsupportedOperationException {
                urin.fragment();
            }
        });
    }

    @Test
    public void urinWithNoFragmentOrPathFragmentIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Query<String> query = aQuery();
        final Urin urin = scheme.urin(anAuthority(), query);
        assertThat(urin.hasFragment(), equalTo(false));
        assertThrowsException("Attempt to get fragment from a UrinReference that does not have one.", UnsupportedOperationException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws UnsupportedOperationException {
                urin.fragment();
            }
        });
    }

    @Test
    public void urinWithNoFragmentOrAuthorityOrPathFragmentIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Query<String> query = aQuery();
        final Urin urin = scheme.urin(query);
        assertThat(urin.hasFragment(), equalTo(false));
        assertThrowsException("Attempt to get fragment from a UrinReference that does not have one.", UnsupportedOperationException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws UnsupportedOperationException {
                urin.fragment();
            }
        });
    }

    @Test
    public void aUrinWithNoFragmentIsNotEqualToAnotherWithTheADifferentParts() throws Exception {
        assertThat(aScheme().urin(aQuery()), not(equalTo(aScheme().urin(aQuery()))));
    }

    @Test
    public void createsUrinWithNoQuery() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anAbsolutePath();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(authority, path, fragment).asString(), equalTo(scheme.asString() + "://" + authority.asString() + path.asString(NEVER_PREFIX_WITH_DOT_SEGMENT) + "#" + fragment.asString()));
    }

    @Test
    public void urinWithNoQueryAuthorityIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anAbsolutePath();
        Fragment<String> fragment = aFragment();
        final Urin urin = scheme.urin(authority, path, fragment);
        assertThat(urin.hasAuthority(), equalTo(true));
        assertThat(urin.authority(), equalTo(authority));
    }

    @Test
    public void urinWithNoQueryPathIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anAbsolutePath();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(authority, path, fragment).path(), equalTo((Path) path));
    }

    @Test
    public void urinWithNoAuthorityNoQueryAuthorityIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Path<String> path = aPath();
        Fragment<String> fragment = aFragment();
        final Urin urin = scheme.urin(path, fragment);
        assertThat(urin.hasAuthority(), equalTo(false));
        assertThrowsException("Attempt to get authority from a UrinReference that does not have one.", UnsupportedOperationException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws UnsupportedOperationException {
                urin.authority();
            }
        });
    }

    @Test
    public void urinWithNoAuthorityNoQueryPathIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Path<String> path = aPath();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(path, fragment).path(), equalTo(path));
    }

    @Test
    public void urinWithNoPathNoQueryAuthorityIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        Fragment<String> fragment = aFragment();
        final Urin urin = scheme.urin(authority, fragment);
        assertThat(urin.hasAuthority(), equalTo(true));
        assertThat(urin.authority(), equalTo(authority));
    }

    @Test
    public void urinWithNoPathNoQueryPathIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(authority, fragment).path(), equalTo((Path) new EmptyPath()));
    }

    @Test
    public void urinWithNoAuthorityNoPathNoQueryAuthorityIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Fragment<String> fragment = aFragment();
        final Urin urin = scheme.urin(fragment);
        assertThat(urin.hasAuthority(), equalTo(false));
        assertThrowsException("Attempt to get authority from a UrinReference that does not have one.", UnsupportedOperationException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws UnsupportedOperationException {
                urin.authority();
            }
        });
    }

    @Test
    public void urinWithNoAuthorityNoPathNoQueryPathIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(fragment).path(), equalTo((Path) new EmptyPath()));
    }

    @Test
    public void urinWithNoQueryQueryIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anAbsolutePath();
        Fragment<String> fragment = aFragment();
        final Urin urin = scheme.urin(authority, path, fragment);
        assertThat(urin.hasQuery(), equalTo(false));
        assertThrowsException("Attempt to get query from a UrinReference that does not have one.", UnsupportedOperationException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws UnsupportedOperationException {
                urin.query();
            }
        });
    }

    @Test
    public void urinWithNoAuthorityNoQueryQueryIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Path<String> path = aPath();
        Fragment<String> fragment = aFragment();
        final Urin urin = scheme.urin(path, fragment);
        assertThat(urin.hasQuery(), equalTo(false));
        assertThrowsException("Attempt to get query from a UrinReference that does not have one.", UnsupportedOperationException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws UnsupportedOperationException {
                urin.query();
            }
        });
    }

    @Test
    public void urinWithNoPathNoQueryQueryIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        Fragment<String> fragment = aFragment();
        final Urin urin = scheme.urin(authority, fragment);
        assertThat(urin.hasQuery(), equalTo(false));
        assertThrowsException("Attempt to get query from a UrinReference that does not have one.", UnsupportedOperationException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws UnsupportedOperationException {
                urin.query();
            }
        });
    }

    @Test
    public void urinWithNoAuthorityNoPathNoQueryQueryIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Fragment<String> fragment = aFragment();
        final Urin urin = scheme.urin(fragment);
        assertThat(urin.hasQuery(), equalTo(false));
        assertThrowsException("Attempt to get query from a UrinReference that does not have one.", UnsupportedOperationException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws UnsupportedOperationException {
                urin.query();
            }
        });
    }

    @Test
    public void urinWithNoQueryFragmentIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anAbsolutePath();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(authority, path, fragment).hasFragment(), equalTo(true));
        assertThat(scheme.urin(authority, path, fragment).fragment(), equalTo(fragment));
    }

    @Test
    public void urinWithNoAuthorityNoQueryFragmentIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        AbsolutePath<String> path = anAbsolutePath();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(path, fragment).hasFragment(), equalTo(true));
        assertThat(scheme.urin(path, fragment).fragment(), equalTo(fragment));
    }

    @Test
    public void urinWithNoPathNoQueryFragmentIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(authority, fragment).hasFragment(), equalTo(true));
        assertThat(scheme.urin(authority, fragment).fragment(), equalTo(fragment));
    }

    @Test
    public void urinWithNoAuthorityNoPathNoQueryFragmentIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(fragment).hasFragment(), equalTo(true));
        assertThat(scheme.urin(fragment).fragment(), equalTo(fragment));
    }

    @Test
    public void createsUrinWithNoQueryAndNoFragment() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anAbsolutePath();
        assertThat(scheme.urin(authority, path).asString(), equalTo(scheme.asString() + "://" + authority.asString() + path.asString(NEVER_PREFIX_WITH_DOT_SEGMENT)));
    }

    @Test
    public void urinWithNoQueryAndNoFragmentAuthorityIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anAbsolutePath();
        final Urin urin = scheme.urin(authority, path);
        assertThat(urin.hasAuthority(), equalTo(true));
        assertThat(urin.authority(), equalTo(authority));
    }

    @Test
    public void urinWithNoQueryAndNoFragmentPathIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anAbsolutePath();
        final Urin urin = scheme.urin(authority, path);
        assertThat(urin.path(), equalTo((Path) path));
        assertThrowsException("Attempt to get fragment from a UrinReference that does not have one.", UnsupportedOperationException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws UnsupportedOperationException {
                urin.fragment();
            }
        });
    }

    @Test
    public void createsUrinWithNoPathNoQueryAndNoFragment() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        assertThat(scheme.urin(authority).asString(), equalTo(scheme.asString() + "://" + authority.asString()));
    }

    @Test
    public void urinWithNoPathNoQueryAndNoFragmentAuthorityIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        final Urin urin = scheme.urin(authority);
        assertThat(urin.hasAuthority(), equalTo(true));
        assertThat(urin.authority(), equalTo(authority));
    }

    @Test
    public void urinWithNoPathNoQueryAndNoFragmentPathIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        final Urin urin = scheme.urin(authority);
        assertThat(urin.path(), equalTo((Path) new EmptyPath()));
        assertThrowsException("Attempt to get fragment from a UrinReference that does not have one.", UnsupportedOperationException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws UnsupportedOperationException {
                urin.fragment();
            }
        });
    }

    @Test
    public void createsUrinWithNoAuthorityNoQueryAndNoFragment() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Path<String> path = aPath();
        assertThat(scheme.urin(path).asString(), equalTo(scheme.asString() + ":" + path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY)));
    }

    @Test
    public void urinWithNoAuthorityNoQueryAndNoFragmentAuthorityIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Path<String> path = aPath();
        final Urin<String, Query<String>, Fragment<String>> urin = scheme.urin(path);
        assertThat(urin.hasAuthority(), equalTo(false));
        assertThrowsException("Attempt to get authority from a UrinReference that does not have one.", UnsupportedOperationException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws UnsupportedOperationException {
                urin.authority();
            }
        });
    }

    @Test
    public void urinWithNoAuthorityNoQueryAndNoFragmentPathIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Path<String> path = aPath();
        final Urin<String, Query<String>, Fragment<String>> urin = scheme.urin(path);
        assertThat(urin.path(), equalTo(path));
        assertThrowsException("Attempt to get fragment from a UrinReference that does not have one.", UnsupportedOperationException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws UnsupportedOperationException {
                urin.fragment();
            }
        });
    }

    @Test
    public void createsUrinWithNoAuthorityNoPathNoQueryAndNoFragment() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        assertThat(scheme.urin().asString(), equalTo(scheme.asString() + ":"));
    }

    @Test
    public void urinWithNoAuthorityNoPathNoQueryAndNoFragmentAuthorityIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Urin urin = scheme.urin();
        assertThat(urin.hasAuthority(), equalTo(false));
        assertThrowsException("Attempt to get authority from a UrinReference that does not have one.", UnsupportedOperationException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws UnsupportedOperationException {
                urin.authority();
            }
        });
    }

    @Test
    public void urinWithNoAuthorityNoPathNoQueryAndNoFragmentPathIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Urin urin = scheme.urin();
        assertThat(urin.path(), equalTo((Path) new EmptyPath()));
        assertThrowsException("Attempt to get fragment from a UrinReference that does not have one.", UnsupportedOperationException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws UnsupportedOperationException {
                urin.fragment();
            }
        });
    }

    @Test
    public void urinWithNoQueryAndNoFragmentQueryIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anAbsolutePath();
        final Urin urin = scheme.urin(authority, path);
        assertThat(urin.hasQuery(), equalTo(false));
        assertThrowsException("Attempt to get query from a UrinReference that does not have one.", UnsupportedOperationException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws UnsupportedOperationException {
                urin.query();
            }
        });
    }

    @Test
    public void urinWithNoAuthorityNoQueryAndNoFragmentQueryIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Path<String> path = aPath();
        final Urin urin = scheme.urin(path);
        assertThat(urin.hasQuery(), equalTo(false));
        assertThrowsException("Attempt to get query from a UrinReference that does not have one.", UnsupportedOperationException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws UnsupportedOperationException {
                urin.query();
            }
        });
    }

    @Test
    public void urinWithNoPathNoQueryAndNoFragmentQueryIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        final Urin urin = scheme.urin(authority);
        assertThat(urin.hasQuery(), equalTo(false));
        assertThrowsException("Attempt to get query from a UrinReference that does not have one.", UnsupportedOperationException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws UnsupportedOperationException {
                urin.query();
            }
        });
    }

    @Test
    public void urinWithNoAuthorityNoPathNoQueryAndNoFragmentQueryIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Urin urin = scheme.urin();
        assertThat(urin.hasQuery(), equalTo(false));
        assertThrowsException("Attempt to get query from a UrinReference that does not have one.", UnsupportedOperationException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws UnsupportedOperationException {
                urin.query();
            }
        });
    }

    @Test
    public void urinWithNoQueryAndNoFragmentFragmentIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anAbsolutePath();
        assertThat(scheme.urin(authority, path).hasFragment(), equalTo(false));
    }

    @Test
    public void urinWithNoAuthorityNoQueryAndNoFragmentFragmentIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Path<String> path = aPath();
        assertThat(scheme.urin(path).hasFragment(), equalTo(false));
    }

    @Test
    public void urinWithNoPathNoQueryAndNoFragmentFragmentIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        assertThat(scheme.urin(authority).hasFragment(), equalTo(false));
    }

    @Test
    public void urinWithNoAuthorityNoPathNoQueryAndNoFragmentFragmentIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        assertThat(scheme.urin().hasFragment(), equalTo(false));
    }

    @Test
    public void aUrinWithNoQueryOrFragmentIsNotEqualToAnotherWithTheADifferentParts() throws Exception {
        assertThat(aScheme().urin(anAuthority(), anAbsolutePath()), not(equalTo(aScheme().urin(anAuthority(), anAbsolutePath()))));
    }

    @Test
    public void aUrinWithNoQueryOrFragmentOrAuthorityOrPathIsEqualToAnotherWithTheSameParts() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        assertThat(scheme.urin(), equalTo(scheme.urin()));
        assertThat(scheme.urin().hashCode(), equalTo(scheme.urin().hashCode()));
    }

    @Test
    public void aUrinWithNoQueryOrFragmentIsNotEqualToAnotherWithTheADifferentScheme() throws Exception {
        assertThat(aScheme().urin(), not(equalTo(aScheme().urin())));
    }

    @Test
    public void aUrinWithNoQueryOrFragmentOrAuthorityOrPathToStringIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        assertThat(scheme.urin().toString(), equalTo(scheme.urin().asString()));
    }

    @Test
    public void aUrinWithNoQueryOrFragmentOrAuthorityOrPathReplacedWithANewPathIsCorrect() throws Exception {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(scheme.urin().withPath(newPath), equalTo(scheme.urin(newPath)));
    }

    @Test
    public void aUrinWithNoQueryOrFragmentOrAuthorityIsEqualToAnotherWithTheSameParts() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Path<String> path = aPath();
        assertThat(scheme.urin(path), equalTo(scheme.urin(path)));
        assertThat(scheme.urin(path).hashCode(), equalTo(scheme.urin(path).hashCode()));
    }

    @Test
    public void aUrinWithNoQueryOrFragmentIsNotEqualToAnotherWithTheADifferentPath() throws Exception {
        assertThat(aScheme().urin(aPath()), not(equalTo(aScheme().urin(aPath()))));
    }

    @Test
    public void aUrinWithNoQueryOrFragmentOrAuthorityToStringIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Path<String> path = aPath();
        assertThat(scheme.urin(path).toString(), equalTo(scheme.urin(path).asString()));
    }

    @Test
    public void aUrinWithNoQueryOrFragmentOrAuthorityReplacedWithANewPathIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Path<String> path = aPath();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(scheme.urin(path).withPath(newPath), equalTo(scheme.urin(newPath)));
    }

    @Test
    public void rejectsNullInFactoryForAUrinWithNoQueryOrNoFragmentOrAuthority() throws Exception {
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Path<String> path = null;
                aScheme().urin(path);
            }
        });
    }

    @Test
    public void aUrinWithNoQueryOrFragmentOrPathIsEqualToAnotherWithTheSameParts() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
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
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        assertThat(scheme.urin(authority).toString(), equalTo(scheme.urin(authority).asString()));
    }

    @Test
    public void aUrinWithNoQueryOrFragmentOrPathReplacedWithANewPathIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(scheme.urin(authority).withPath(newPath), equalTo(scheme.urin(authority, newPath)));
    }

    @Test
    public void rejectsNullInFactoryForAUrinWithNoQueryOrNoFragmentOrPath() throws Exception {
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Authority authority = null;
                aScheme().urin(authority);
            }
        });
    }

    @Test
    public void aUrinWithNoQueryOrFragmentIsEqualToAnotherWithTheSameParts() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anAbsolutePath();
        assertThat(scheme.urin(authority, path), equalTo(scheme.urin(authority, path)));
        assertThat(scheme.urin(authority, path).hashCode(), equalTo(scheme.urin(authority, path).hashCode()));
    }

    @Test
    public void aUrinWithNoQueryOrFragmentIsNotEqualToAnotherWithTheADifferentAuthorityAndPath() throws Exception {
        assertThat(aScheme().urin(anAuthority(), anAbsolutePath()), not(equalTo(aScheme().urin(anAuthority(), anAbsolutePath()))));
    }

    @Test
    public void aUrinWithNoQueryOrFragmentToStringIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anAbsolutePath();
        assertThat(scheme.urin(authority, path).toString(), equalTo(scheme.urin(authority, path).asString()));
    }

    @Test
    public void aUrinWithNoQueryOrFragmentReplacedWithANewPathIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anAbsolutePath();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(scheme.urin(authority, path).withPath(newPath), equalTo(scheme.urin(authority, newPath)));
    }

    @Test
    public void rejectsNullInFactoryForAUrinWithNoQueryOrNoFragment() throws Exception {
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Authority authority = null;
                aScheme().urin(authority, anAbsolutePath());
            }
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                AbsolutePath<String> path = null;
                aScheme().urin(anAuthority(), path);
            }
        });
    }

    @Test
    public void aUrinWithNoQueryOrAuthorityOrPathIsEqualToAnotherWithTheSameParts() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(fragment), equalTo(scheme.urin(fragment)));
        assertThat(scheme.urin(fragment).hashCode(), equalTo(scheme.urin(fragment).hashCode()));
    }

    @Test
    public void aUrinWithNoQueryIsNotEqualToAnotherWithTheADifferentSchemeAndFragment() throws Exception {
        assertThat(aScheme().urin(aFragment()), not(equalTo(aScheme().urin(aFragment()))));
    }

    @Test
    public void aUrinWithNoQueryOrAuthorityOrPathToStringIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(fragment).toString(), equalTo(scheme.urin(fragment).asString()));
    }

    @Test
    public void aUrinWithNoQueryOrAuthorityOrPathReplacedWithANewPathIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Fragment<String> fragment = aFragment();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(scheme.urin(fragment).withPath(newPath), equalTo(scheme.urin(newPath, fragment)));
    }

    @Test
    public void rejectsNullInFactoryForAUrinWithNoQueryOrAuthorityOrPath() throws Exception {
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Fragment<String> fragment = null;
                aScheme().urin(fragment);
            }
        });
    }

    @Test
    public void aUrinWithNoQueryOrAuthorityIsEqualToAnotherWithTheSameParts() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Path<String> path = aPath();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(path, fragment), equalTo(scheme.urin(path, fragment)));
        assertThat(scheme.urin(path, fragment).hashCode(), equalTo(scheme.urin(path, fragment).hashCode()));
    }

    @Test
    public void aUrinWithNoQueryIsNotEqualToAnotherWithTheADifferentPathAndFragment() throws Exception {
        assertThat(aScheme().urin(aPath(), aFragment()), not(equalTo(aScheme().urin(aPath(), aFragment()))));
    }

    @Test
    public void aUrinWithNoQueryOrAuthorityToStringIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Path<String> path = aPath();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(path, fragment).toString(), equalTo(scheme.urin(path, fragment).asString()));
    }

    @Test
    public void aUrinWithNoQueryOrAuthorityReplacedWithANewPathIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Path<String> path = aPath();
        Fragment<String> fragment = aFragment();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(scheme.urin(path, fragment).withPath(newPath), equalTo(scheme.urin(newPath, fragment)));
    }

    @Test
    public void rejectsNullInFactoryForAUrinWithNoQueryOrAuthority() throws Exception {
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Path<String> path = null;
                aScheme().urin(path, aFragment());
            }
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Fragment<String> fragment = null;
                aScheme().urin(aPath(), fragment);
            }
        });
    }

    @Test
    public void aUrinWithNoQueryOrPathIsEqualToAnotherWithTheSameParts() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(authority, fragment), equalTo(scheme.urin(authority, fragment)));
        assertThat(scheme.urin(authority, fragment).hashCode(), equalTo(scheme.urin(authority, fragment).hashCode()));
    }

    @Test
    public void aUrinWithNoQueryIsNotEqualToAnotherWithTheADifferentAuthorityAndFragment() throws Exception {
        assertThat(aScheme().urin(anAuthority(), aFragment()), not(equalTo(aScheme().urin(anAuthority(), aFragment()))));
    }

    @Test
    public void aUrinWithNoQueryOrPathToStringIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(authority, fragment).toString(), equalTo(scheme.urin(authority, fragment).asString()));
    }

    @Test
    public void aUrinWithNoQueryOrPathReplacedWithANewPathIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        Fragment<String> fragment = aFragment();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(scheme.urin(authority, fragment).withPath(newPath), equalTo(scheme.urin(authority, newPath, fragment)));
    }

    @Test
    public void rejectsNullInFactoryForAUrinWithNoQueryOrPath() throws Exception {
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Authority authority = null;
                aScheme().urin(authority, aFragment());
            }
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Fragment<String> fragment = null;
                aScheme().urin(anAuthority(), fragment);
            }
        });
    }

    @Test
    public void aUrinWithNoQueryIsEqualToAnotherWithTheSameParts() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anAbsolutePath();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(authority, path, fragment), equalTo(scheme.urin(authority, path, fragment)));
        assertThat(scheme.urin(authority, path, fragment).hashCode(), equalTo(scheme.urin(authority, path, fragment).hashCode()));
    }

    @Test
    public void aUrinWithNoQueryOrFragmentIsNotEqualToAnotherWithTheADifferentAuthorityAndPathAndFragment() throws Exception {
        assertThat(aScheme().urin(anAuthority(), anAbsolutePath(), aFragment()), not(equalTo(aScheme().urin(anAuthority(), anAbsolutePath(), aFragment()))));
    }

    @Test
    public void aUrinWithNoQueryToStringIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anAbsolutePath();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(authority, path, fragment).toString(), equalTo(scheme.urin(authority, path, fragment).asString()));
    }

    @Test
    public void aUrinWithNoQueryReplacedWithANewPathIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anAbsolutePath();
        Fragment<String> fragment = aFragment();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(scheme.urin(authority, path, fragment).withPath(newPath), equalTo(scheme.urin(authority, newPath, fragment)));
    }

    @Test
    public void rejectsNullInFactoryForAUrinWithNoQuery() throws Exception {
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Authority authority = null;
                aScheme().urin(authority, anAbsolutePath(), aFragment());
            }
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                AbsolutePath<String> path = null;
                aScheme().urin(anAuthority(), path, aFragment());
            }
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Fragment<String> fragment = null;
                aScheme().urin(anAuthority(), anAbsolutePath(), fragment);
            }
        });
    }

    @Test
    public void aUrinWithNoFragmentOrAuthorityOrPathIsEqualToAnotherWithTheSameParts() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Query<String> query = aQuery();
        assertThat(scheme.urin(query), equalTo(scheme.urin(query)));
        assertThat(scheme.urin(query).hashCode(), equalTo(scheme.urin(query).hashCode()));
    }

    @Test
    public void aUrinWithNoFragmentIsNotEqualToAnotherWithTheADifferentSchemeAndQuery() throws Exception {
        assertThat(aScheme().urin(aQuery()), not(equalTo(aScheme().urin(aQuery()))));
    }

    @Test
    public void aUrinWithNoFragmentOrAuthorityOrPathToStringIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Query<String> query = aQuery();
        assertThat(scheme.urin(query).toString(), equalTo(scheme.urin(query).asString()));
    }

    @Test
    public void aUrinWithNoFragmentOrAuthorityOrPathReplacedWithANewPathIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Query<String> query = aQuery();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(scheme.urin(query).withPath(newPath), equalTo(scheme.urin(newPath, query)));
    }

    @Test
    public void rejectsNullInFactoryForAUrinWithNoFragmentOrAuthorityOrPath() throws Exception {
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Query<String> query = null;
                aScheme().urin(query);
            }
        });
    }

    @Test
    public void aUrinWithNoFragmentOrAuthorityIsEqualToAnotherWithTheSameParts() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Path<String> path = aPath();
        Query<String> query = aQuery();
        assertThat(scheme.urin(path, query), equalTo(scheme.urin(path, query)));
        assertThat(scheme.urin(path, query).hashCode(), equalTo(scheme.urin(path, query).hashCode()));
    }

    @Test
    public void aUrinWithNoFragmentIsNotEqualToAnotherWithTheADifferentPathAndQuery() throws Exception {
        assertThat(aScheme().urin(aPath(), aQuery()), not(equalTo(aScheme().urin(aPath(), aQuery()))));
    }

    @Test
    public void aUrinWithNoFragmentOrAuthorityToStringIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Path<String> path = aPath();
        Query<String> query = aQuery();
        assertThat(scheme.urin(path, query).toString(), equalTo(scheme.urin(path, query).asString()));
    }

    @Test
    public void aUrinWithNoFragmentOrAuthorityReplacedWithANewPathIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Path<String> path = aPath();
        Query<String> query = aQuery();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(scheme.urin(path, query).withPath(newPath), equalTo(scheme.urin(newPath, query)));
    }

    @Test
    public void rejectsNullInFactoryForAUrinWithNoFragmentOrAuthority() throws Exception {
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Path<String> path = null;
                aScheme().urin(path, aQuery());
            }
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Query<String> query = null;
                aScheme().urin(aPath(), query);
            }
        });
    }

    @Test
    public void aUrinWithNoFragmentOrPathIsEqualToAnotherWithTheSameParts() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        Query<String> query = aQuery();
        assertThat(scheme.urin(authority, query), equalTo(scheme.urin(authority, query)));
        assertThat(scheme.urin(authority, query).hashCode(), equalTo(scheme.urin(authority, query).hashCode()));
    }

    @Test
    public void aUrinWithNoFragmentIsNotEqualToAnotherWithTheADifferentAuthorityAndQuery() throws Exception {
        assertThat(aScheme().urin(anAuthority(), aQuery()), not(equalTo(aScheme().urin(anAuthority(), aQuery()))));
    }

    @Test
    public void aUrinWithNoFragmentOrPathToStringIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        Query<String> query = aQuery();
        assertThat(scheme.urin(authority, query).toString(), equalTo(scheme.urin(authority, query).asString()));
    }

    @Test
    public void aUrinWithNoFragmentOrPathReplacedWithANewPathIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        Query<String> query = aQuery();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(scheme.urin(authority, query).withPath(newPath), equalTo(scheme.urin(authority, newPath, query)));
    }

    @Test
    public void rejectsNullInFactoryForAUrinWithNoFragmentOrPath() throws Exception {
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Authority authority = null;
                aScheme().urin(authority, aQuery());
            }
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Query<String> query = null;
                aScheme().urin(anAuthority(), query);
            }
        });
    }

    @Test
    public void aUrinWithNoFragmentIsEqualToAnotherWithTheSameParts() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anAbsolutePath();
        Query<String> query = aQuery();
        assertThat(scheme.urin(authority, path, query), equalTo(scheme.urin(authority, path, query)));
        assertThat(scheme.urin(authority, path, query).hashCode(), equalTo(scheme.urin(authority, path, query).hashCode()));
    }

    @Test
    public void aUrinWithNoFragmentOrQueryIsNotEqualToAnotherWithTheADifferentAuthorityAndPathAndQuery() throws Exception {
        assertThat(aScheme().urin(anAuthority(), anAbsolutePath(), aQuery()), not(equalTo(aScheme().urin(anAuthority(), anAbsolutePath(), aQuery()))));
    }

    @Test
    public void aUrinWithNoFragmentToStringIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anAbsolutePath();
        Query<String> query = aQuery();
        assertThat(scheme.urin(authority, path, query).toString(), equalTo(scheme.urin(authority, path, query).asString()));
    }

    @Test
    public void aUrinWithNoFragmentReplacedWithANewPathIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anAbsolutePath();
        Query<String> query = aQuery();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(scheme.urin(authority, path, query).withPath(newPath), equalTo(scheme.urin(authority, newPath, query)));
    }

    @Test
    public void rejectsNullInFactoryForAUrinWithNoFragment() throws Exception {
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Authority authority = null;
                aScheme().urin(authority, anAbsolutePath(), aQuery());
            }
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                AbsolutePath<String> path = null;
                aScheme().urin(anAuthority(), path, aQuery());
            }
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Query<String> query = null;
                aScheme().urin(anAuthority(), anAbsolutePath(), query);
            }
        });
    }

    @Test
    public void aUrinWithNoAuthorityOrPathIsEqualToAnotherWithTheSameParts() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(query, fragment), equalTo(scheme.urin(query, fragment)));
        assertThat(scheme.urin(query, fragment).hashCode(), equalTo(scheme.urin(query, fragment).hashCode()));
    }

    @Test
    public void aUrinWithNoAuthorityOrPathIsNotEqualToAnotherWithTheADifferentSchemeAndQuery() throws Exception {
        assertThat(aScheme().urin(aQuery(), aFragment()), not(equalTo(aScheme().urin(aQuery(), aFragment()))));
    }

    @Test
    public void aUrinWithNoAuthorityOrPathToStringIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(query, fragment).toString(), equalTo(scheme.urin(query, fragment).asString()));
    }

    @Test
    public void aUrinWithNoAuthorityOrPathReplacedWithANewPathIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(scheme.urin(query, fragment).withPath(newPath), equalTo(scheme.urin(newPath, query, fragment)));
    }

    @Test
    public void rejectsNullInFactoryForAUrinWithNoAuthorityOrPath() throws Exception {
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Query<String> query = null;
                aScheme().urin(query, aFragment());
            }
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Fragment<String> fragment = null;
                aScheme().urin(aQuery(), fragment);
            }
        });
    }

    @Test
    public void aUrinWithNoAuthorityIsEqualToAnotherWithTheSameParts() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Path<String> path = aPath();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(path, query, fragment), equalTo(scheme.urin(path, query, fragment)));
        assertThat(scheme.urin(path, query, fragment).hashCode(), equalTo(scheme.urin(path, query, fragment).hashCode()));
    }

    @Test
    public void aUrinWithNoAuthorityIsNotEqualToAnotherWithTheADifferentPathAndQuery() throws Exception {
        assertThat(aScheme().urin(aPath(), aQuery(), aFragment()), not(equalTo(aScheme().urin(aPath(), aQuery(), aFragment()))));
    }

    @Test
    public void aUrinWithNoAuthorityToStringIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Path<String> path = aPath();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(path, query, fragment).toString(), equalTo(scheme.urin(path, query, fragment).asString()));
    }

    @Test
    public void aUrinWithNoAuthorityReplacedWithANewPathIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Path<String> path = aPath();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(scheme.urin(path, query, fragment).withPath(newPath), equalTo(scheme.urin(newPath, query, fragment)));
    }

    @Test
    public void rejectsNullInFactoryForAUrinWithNoAuthority() throws Exception {
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Path<String> path = null;
                aScheme().urin(path, aQuery(), aFragment());
            }
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Query<String> query = null;
                aScheme().urin(aPath(), query, aFragment());
            }
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Fragment<String> fragment = null;
                aScheme().urin(aPath(), aQuery(), fragment);
            }
        });
    }

    @Test
    public void aUrinWithNoPathIsEqualToAnotherWithTheSameParts() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(authority, query, fragment), equalTo(scheme.urin(authority, query, fragment)));
        assertThat(scheme.urin(authority, query, fragment).hashCode(), equalTo(scheme.urin(authority, query, fragment).hashCode()));
    }

    @Test
    public void aUrinWithNoPathIsNotEqualToAnotherWithTheADifferentAuthorityAndQuery() throws Exception {
        assertThat(aScheme().urin(anAuthority(), aQuery(), aFragment()), not(equalTo(aScheme().urin(anAuthority(), aQuery(), aFragment()))));
    }

    @Test
    public void aUrinWithNoPathToStringIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(authority, query, fragment).toString(), equalTo(scheme.urin(authority, query, fragment).asString()));
    }

    @Test
    public void aUrinWithNoPathToStringIsCorrectReplacedWithANewPathIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(scheme.urin(authority, query, fragment).withPath(newPath), equalTo(scheme.urin(authority, newPath, query, fragment)));
    }

    @Test
    public void rejectsNullInFactoryForAUrinWithNoPath() throws Exception {
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Authority authority = null;
                aScheme().urin(authority, aQuery(), aFragment());
            }
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Query<String> query = null;
                aScheme().urin(anAuthority(), query, aFragment());
            }
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Fragment<String> fragment = null;
                aScheme().urin(anAuthority(), aQuery(), fragment);
            }
        });
    }

    @Test
    public void aUrinWithAllPartsIsEqualToAnotherWithTheSameParts() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anAbsolutePath();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(authority, path, query, fragment), equalTo(scheme.urin(authority, path, query, fragment)));
        assertThat(scheme.urin(authority, path, query, fragment).hashCode(), equalTo(scheme.urin(authority, path, query, fragment).hashCode()));
    }

    @Test
    public void aUrinWithAllPartsIsNotEqualToAnotherWithTheADifferentAuthorityAndPathAndQuery() throws Exception {
        assertThat(aScheme().urin(anAuthority(), anAbsolutePath(), aQuery(), aFragment()), not(equalTo(aScheme().urin(anAuthority(), anAbsolutePath(), aQuery(), aFragment()))));
    }

    @Test
    public void aUrinWithAllPartsToStringIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anAbsolutePath();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(authority, path, query, fragment).toString(), equalTo(scheme.urin(authority, path, query, fragment).asString()));
    }

    @Test
    public void aUrinWithAllPartsReplacedWithANewPathIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anAbsolutePath();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(scheme.urin(authority, path, query, fragment).withPath(newPath), equalTo(scheme.urin(authority, newPath, query, fragment)));
    }

    @Test
    public void rejectsNullInFactoryForAUrinWithAllParts() throws Exception {
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Authority authority = null;
                aScheme().urin(authority, anAbsolutePath(), aQuery(), aFragment());
            }
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                AbsolutePath<String> path = null;
                aScheme().urin(anAuthority(), path, aQuery(), aFragment());
            }
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Query<String> query = null;
                aScheme().urin(anAuthority(), anAbsolutePath(), query, aFragment());
            }
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Fragment<String> fragment = null;
                aScheme().urin(anAuthority(), anAbsolutePath(), aQuery(), fragment);
            }
        });
    }

    @Test
    public void parsesUrinWithAllParts() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anUnpollutedAbsolutePath();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.parseUrin(scheme.asString() + "://" + authority.asString() + path.asString(NEVER_PREFIX_WITH_DOT_SEGMENT) + "?" + query.asString() + "#" + fragment.asString()), equalTo(scheme.urin(authority, path, query, fragment)));
    }

    @Test
    public void parsesUrinWithNoAuthority() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Path<String> path = anUnpollutedPath();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.parseUrin(scheme.asString() + ":" + path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY) + "?" + query.asString() + "#" + fragment.asString()), equalTo(scheme.urin(path, query, fragment)));
    }

    @Test
    public void parsesUrinWithEmptyPath() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.parseUrin(scheme.asString() + "://" + authority.asString() + "?" + query.asString() + "#" + fragment.asString()), equalTo(scheme.urin(authority, query, fragment)));
    }

    @Test
    public void parsesUrinWithNoAuthorityOrPath() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.parseUrin(scheme.asString() + ":" + "?" + query.asString() + "#" + fragment.asString()), equalTo(scheme.urin(query, fragment)));
    }

    @Test
    public void parsesUrinWithNoFragment() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anUnpollutedAbsolutePath();
        Query<String> query = aQuery();
        assertThat(scheme.parseUrin(scheme.asString() + "://" + authority.asString() + path.asString(NEVER_PREFIX_WITH_DOT_SEGMENT) + "?" + query.asString()), equalTo(scheme.urin(authority, path, query)));
    }

    @Test
    public void parsesUrinWithNoFragmentAndNoAuthority() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Path<String> path = anUnpollutedPath();
        Query<String> query = aQuery();
        assertThat(scheme.parseUrin(scheme.asString() + ":" + path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY) + "?" + query.asString()), equalTo(scheme.urin(path, query)));
    }

    @Test
    public void parsesUrinWithNoQuery() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anUnpollutedAbsolutePath();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.parseUrin(scheme.asString() + "://" + authority.asString() + path.asString(NEVER_PREFIX_WITH_DOT_SEGMENT) + "#" + fragment.asString()), equalTo(scheme.urin(authority, path, fragment)));
    }

    @Test
    public void parsesUrinWithNoQueryOrAuthority() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        AbsolutePath<String> path = anUnpollutedAbsolutePath();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.parseUrin(scheme.asString() + ":" + path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY) + "#" + fragment.asString()), equalTo(scheme.urin(path, fragment)));
    }

    @Test
    public void parsesUrinWithNoQueryAndNoFragment() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anUnpollutedAbsolutePath();
        assertThat(scheme.parseUrin(scheme.asString() + "://" + authority.asString() + path.asString(NEVER_PREFIX_WITH_DOT_SEGMENT)), equalTo(scheme.urin(authority, path)));
    }

    @Test
    public void parsesUrinWithNoQueryAndNoFragmentAndNoAuthorityAndNoPath() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        assertThat(scheme.parseUrin(scheme.asString() + ":"), equalTo(scheme.urin()));
    }

    @Test
    public void parsesUrinWithNoQueryAndNoFragmentAndEmptyPath() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        assertThat(scheme.parseUrin(scheme.asString() + "://" + authority.asString()), equalTo(scheme.urin(authority)));
    }

    @Test
    public void parsesUrinWithNoQueryAndNoFragmentAndNoAuthority() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        AbsolutePath<String> path = anUnpollutedAbsolutePath();
        assertThat(scheme.parseUrin(scheme.asString() + ":" + path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY)), equalTo(scheme.urin(path)));
    }

    @Test
    public void parsingEmptyStringThrowsParseException() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        assertThrowsException("Empty String should throw ParseException", ParseException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws ParseException {
                scheme.parseUrin("");
            }
        });
    }

    @Test
    public void parsingNullThrowsNullPointerException() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        assertThrowsException("Null value should throw NullPointerException in parser", NullPointerException.class, new ExceptionAssert.ExceptionThrower() {
            public void execute() throws Exception {
                scheme.parseUrin((String) null);
            }
        });
    }

    @Test
    public void resolvesAUrinToItself() throws Exception {
        Urin<String, Query<String>, Fragment<String>> urinReference = aUrin();
        assertThat(aUrin().resolve(urinReference), equalTo(urinReference));
    }

    @Test
    public void urinCreatedWithASchemeSpecifyingADefaultPortDifferentToThatUsedIsEqualToIdenticalUrinCreatedWithASchemeWithoutDefaultPort() throws Exception {
        String schemeName = aValidSchemeName();
        Authority authority = anAuthority();
        assertThat(scheme(schemeName, aPort()).urin(authority), equalTo(scheme(schemeName).urin(authority)));
    }

    @Test
    public void urinCreatedWithASchemeSpecifyingADefaultPortEqualToThatUsedIsEqualToIdenticalUrinCreatedWithASchemeWithoutDefaultPortAndWithoutPort() throws Exception {
        String schemeName = aValidSchemeName();
        Port port = aPort();
        Host host = aHost();
        assertThat(scheme(schemeName, port).urin(authority(host, port)), equalTo(scheme(schemeName).urin(authority(host))));
    }
}
