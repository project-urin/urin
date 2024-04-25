/*
 * Copyright 2024 Mark Slater
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
import static org.junit.jupiter.api.Assertions.assertThrows;

class UrinTest {

    @Test
    void createsUrinWithAllParts() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Authority authority = anAuthority();
        final AbsolutePath<String> path = anAbsolutePath();
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(authority, path, query, fragment).asString(), equalTo(scheme.asString() + "://" + authority.asString() + path.asString(NEVER_PREFIX_WITH_DOT_SEGMENT) + "?" + query.asString() + "#" + fragment.asString()));
    }

    @Test
    void urinWithAllPartsAuthorityIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Authority authority = anAuthority();
        final AbsolutePath<String> path = anAbsolutePath();
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        final var urin = scheme.urin(authority, path, query, fragment);
        assertThat(urin.hasAuthority(), equalTo(true));
        assertThat(urin.authority(), equalTo(authority));
    }

    @Test
    void urinWithAllPartsPathIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Authority authority = anAuthority();
        final AbsolutePath<String> path = anAbsolutePath();
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(authority, path, query, fragment).path(), equalTo(path));
    }

    @Test
    void urinWithAllPartsQueryIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Authority authority = anAuthority();
        final AbsolutePath<String> path = anAbsolutePath();
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(authority, path, query, fragment).hasQuery(), equalTo(true));
        assertThat(scheme.urin(authority, path, query, fragment).query(), equalTo(query));
    }

    @Test
    void urinWithAllPartsButAuthorityAuthorityIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Path<String> path = aPath();
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        final var urin = scheme.urin(path, query, fragment);
        assertThat(urin.hasAuthority(), equalTo(false));
        assertThrows(UnsupportedOperationException.class, urin::authority, "Attempt to get authority from a UrinReference that does not have one.");
    }

    @Test
    void urinWithAllPartsButAuthorityQueryIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Path<String> path = aPath();
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(path, query, fragment).hasQuery(), equalTo(true));
        assertThat(scheme.urin(path, query, fragment).query(), equalTo(query));
    }

    @Test
    void urinWithAllPartsButPathQueryIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Authority authority = anAuthority();
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(authority, query, fragment).hasQuery(), equalTo(true));
        assertThat(scheme.urin(authority, query, fragment).query(), equalTo(query));
    }

    @Test
    void urinWithAllPartsButAuthorityAndPathQueryIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(query, fragment).hasQuery(), equalTo(true));
        assertThat(scheme.urin(query, fragment).query(), equalTo(query));
    }

    @Test
    void urinWithAllPartsFragmentIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Authority authority = anAuthority();
        final AbsolutePath<String> path = anAbsolutePath();
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(authority, path, query, fragment).hasFragment(), equalTo(true));
        assertThat(scheme.urin(authority, path, query, fragment).fragment(), equalTo(fragment));
    }

    @Test
    void aUrinWithAllPartsIsNotEqualToAnotherWithTheADifferentParts() {
        assertThat(aScheme().urin(anAuthority(), anAbsolutePath(), aQuery(), aFragment()), not(equalTo(aScheme().urin(anAuthority(), anAbsolutePath(), aQuery(), aFragment()))));
    }

    @Test
    void aUrinWithAllPartsToStringIsCorrectWithHierarchicalPart() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Authority authority = anAuthority();
        final AbsolutePath<String> path = anAbsolutePath();
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(authority, path, query, fragment).toString(), equalTo(scheme.urin(authority, path, query, fragment).asString()));
    }


    @Test
    void urinWithNoFragmentAuthorityIsCorrect() {
        final AbsolutePath<String> path = anAbsolutePath();
        final Authority authority = anAuthority();
        final var urin = aScheme().urin(authority, path, aQuery());
        assertThat(urin.hasAuthority(), equalTo(true));
        assertThat(urin.authority(), equalTo(authority));
    }

    @Test
    void urinWithNoFragmentPathIsCorrect() {
        final AbsolutePath<String> path = anAbsolutePath();
        assertThat(aScheme().urin(anAuthority(), path, aQuery()).path(), equalTo(path));
    }

    @Test
    void urinWithNoFragmentOrPathAuthorityIsCorrect() {
        final var urin = aScheme().urin(aQuery());
        assertThat(urin.hasAuthority(), equalTo(false));
        assertThrows(UnsupportedOperationException.class, urin::authority, "Attempt to get authority from a UrinReference that does not have one.");
    }

    @Test
    void urinWithNoFragmentOrPathPathIsCorrect() {
        assertThat(aScheme().urin(aQuery()).path(), equalTo(new EmptyPath<>()));
    }

    @Test
    void urinWithNoFragmentOrAuthorityAuthorityIsCorrect() {
        final Path<String> path = aPath();
        final var urin = aScheme().urin(path, aQuery());
        assertThat(urin.hasAuthority(), equalTo(false));
        assertThrows(UnsupportedOperationException.class, urin::authority, "Attempt to get authority from a UrinReference that does not have one.");
    }

    @Test
    void urinWithNoFragmentOrAuthorityPathIsCorrect() {
        final Path<String> path = aPath();
        assertThat(aScheme().urin(path, aQuery()).path(), equalTo(path));
    }

    @Test
    void urinWithNoFragmentOrAuthorityOrPathAuthorityIsCorrect() {
        final var urin = aScheme().urin(aQuery());
        assertThat(urin.hasAuthority(), equalTo(false));
        assertThrows(UnsupportedOperationException.class, urin::authority, "Attempt to get authority from a UrinReference that does not have one.");
    }

    @Test
    void urinWithNoFragmentOrAuthorityOrPathPathIsCorrect() {
        assertThat(aScheme().urin(aQuery()).path(), equalTo(new EmptyPath<>()));
    }

    @Test
    void urinWithNoFragmentOrAuthorityQueryIsCorrect() {
        final Query<String> query = aQuery();
        final var urin = aScheme().urin(aPath(), query);
        assertThat(urin.hasQuery(), equalTo(true));
        assertThat(urin.query(), equalTo(query));
    }

    @Test
    void urinWithNoFragmentOrPathQueryIsCorrect() {
        final Query<String> query = aQuery();
        final var urin = aScheme().urin(anAuthority(), query);
        assertThat(urin.hasQuery(), equalTo(true));
        assertThat(urin.query(), equalTo(query));
    }

    @Test
    void urinWithNoFragmentOrAuthorityOrPathQueryIsCorrect() {
        final Query<String> query = aQuery();
        final var urin = aScheme().urin(query);
        assertThat(urin.hasQuery(), equalTo(true));
        assertThat(urin.query(), equalTo(query));
    }

    @Test
    void urinWithNoFragmentQueryIsCorrect() {
        final Query<String> query = aQuery();
        final var urin = aScheme().urin(anAuthority(), anAbsolutePath(), query);
        assertThat(urin.hasQuery(), equalTo(true));
        assertThat(urin.query(), equalTo(query));
    }

    @Test
    void urinWithNoFragmentFragmentIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Query<String> query = aQuery();
        final var urin = scheme.urin(anAuthority(), anAbsolutePath(), query);
        assertThat(urin.hasFragment(), equalTo(false));
        assertThrows(UnsupportedOperationException.class, urin::fragment, "Attempt to get fragment from a UrinReference that does not have one.");
    }

    @Test
    void urinWithNoFragmentOrAuthorityFragmentIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Query<String> query = aQuery();
        final var urin = scheme.urin(aPath(), query);
        assertThat(urin.hasFragment(), equalTo(false));
        assertThrows(UnsupportedOperationException.class, urin::fragment, "Attempt to get fragment from a UrinReference that does not have one.");
    }

    @Test
    void urinWithNoFragmentOrPathFragmentIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Query<String> query = aQuery();
        final var urin = scheme.urin(anAuthority(), query);
        assertThat(urin.hasFragment(), equalTo(false));
        assertThrows(UnsupportedOperationException.class, urin::fragment, "Attempt to get fragment from a UrinReference that does not have one.");
    }

    @Test
    void urinWithNoFragmentOrAuthorityOrPathFragmentIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Query<String> query = aQuery();
        final var urin = scheme.urin(query);
        assertThat(urin.hasFragment(), equalTo(false));
        assertThrows(UnsupportedOperationException.class, urin::fragment, "Attempt to get fragment from a UrinReference that does not have one.");
    }

    @Test
    void aUrinWithNoFragmentIsNotEqualToAnotherWithTheADifferentParts() {
        assertThat(aScheme().urin(aQuery()), not(equalTo(aScheme().urin(aQuery()))));
    }

    @Test
    void createsUrinWithNoQuery() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Authority authority = anAuthority();
        final AbsolutePath<String> path = anAbsolutePath();
        final Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(authority, path, fragment).asString(), equalTo(scheme.asString() + "://" + authority.asString() + path.asString(NEVER_PREFIX_WITH_DOT_SEGMENT) + "#" + fragment.asString()));
    }

    @Test
    void urinWithNoQueryAuthorityIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Authority authority = anAuthority();
        final AbsolutePath<String> path = anAbsolutePath();
        final Fragment<String> fragment = aFragment();
        final var urin = scheme.urin(authority, path, fragment);
        assertThat(urin.hasAuthority(), equalTo(true));
        assertThat(urin.authority(), equalTo(authority));
    }

    @Test
    void urinWithNoQueryPathIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Authority authority = anAuthority();
        final AbsolutePath<String> path = anAbsolutePath();
        final Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(authority, path, fragment).path(), equalTo(path));
    }

    @Test
    void urinWithNoAuthorityNoQueryAuthorityIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Path<String> path = aPath();
        final Fragment<String> fragment = aFragment();
        final var urin = scheme.urin(path, fragment);
        assertThat(urin.hasAuthority(), equalTo(false));
        assertThrows(UnsupportedOperationException.class, urin::authority, "Attempt to get authority from a UrinReference that does not have one.");
    }

    @Test
    void urinWithNoAuthorityNoQueryPathIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Path<String> path = aPath();
        final Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(path, fragment).path(), equalTo(path));
    }

    @Test
    void urinWithNoPathNoQueryAuthorityIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Authority authority = anAuthority();
        final Fragment<String> fragment = aFragment();
        final var urin = scheme.urin(authority, fragment);
        assertThat(urin.hasAuthority(), equalTo(true));
        assertThat(urin.authority(), equalTo(authority));
    }

    @Test
    void urinWithNoPathNoQueryPathIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Authority authority = anAuthority();
        final Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(authority, fragment).path(), equalTo(new EmptyPath<>()));
    }

    @Test
    void urinWithNoAuthorityNoPathNoQueryAuthorityIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Fragment<String> fragment = aFragment();
        final var urin = scheme.urin(fragment);
        assertThat(urin.hasAuthority(), equalTo(false));
        assertThrows(UnsupportedOperationException.class, urin::authority, "Attempt to get authority from a UrinReference that does not have one.");
    }

    @Test
    void urinWithNoAuthorityNoPathNoQueryPathIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(fragment).path(), equalTo(new EmptyPath<>()));
    }

    @Test
    void urinWithNoQueryQueryIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Authority authority = anAuthority();
        final AbsolutePath<String> path = anAbsolutePath();
        final Fragment<String> fragment = aFragment();
        final var urin = scheme.urin(authority, path, fragment);
        assertThat(urin.hasQuery(), equalTo(false));
        assertThrows(UnsupportedOperationException.class, urin::query, "Attempt to get query from a UrinReference that does not have one.");
    }

    @Test
    void urinWithNoAuthorityNoQueryQueryIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Path<String> path = aPath();
        final Fragment<String> fragment = aFragment();
        final var urin = scheme.urin(path, fragment);
        assertThat(urin.hasQuery(), equalTo(false));
        assertThrows(UnsupportedOperationException.class, urin::query, "Attempt to get query from a UrinReference that does not have one.");
    }

    @Test
    void urinWithNoPathNoQueryQueryIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Authority authority = anAuthority();
        final Fragment<String> fragment = aFragment();
        final var urin = scheme.urin(authority, fragment);
        assertThat(urin.hasQuery(), equalTo(false));
        assertThrows(UnsupportedOperationException.class, urin::query, "Attempt to get query from a UrinReference that does not have one.");
    }

    @Test
    void urinWithNoAuthorityNoPathNoQueryQueryIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Fragment<String> fragment = aFragment();
        final var urin = scheme.urin(fragment);
        assertThat(urin.hasQuery(), equalTo(false));
        assertThrows(UnsupportedOperationException.class, urin::query, "Attempt to get query from a UrinReference that does not have one.");
    }

    @Test
    void urinWithNoQueryFragmentIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Authority authority = anAuthority();
        final AbsolutePath<String> path = anAbsolutePath();
        final Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(authority, path, fragment).hasFragment(), equalTo(true));
        assertThat(scheme.urin(authority, path, fragment).fragment(), equalTo(fragment));
    }

    @Test
    void urinWithNoAuthorityNoQueryFragmentIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final AbsolutePath<String> path = anAbsolutePath();
        final Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(path, fragment).hasFragment(), equalTo(true));
        assertThat(scheme.urin(path, fragment).fragment(), equalTo(fragment));
    }

    @Test
    void urinWithNoPathNoQueryFragmentIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Authority authority = anAuthority();
        final Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(authority, fragment).hasFragment(), equalTo(true));
        assertThat(scheme.urin(authority, fragment).fragment(), equalTo(fragment));
    }

    @Test
    void urinWithNoAuthorityNoPathNoQueryFragmentIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(fragment).hasFragment(), equalTo(true));
        assertThat(scheme.urin(fragment).fragment(), equalTo(fragment));
    }

    @Test
    void createsUrinWithNoQueryAndNoFragment() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Authority authority = anAuthority();
        final AbsolutePath<String> path = anAbsolutePath();
        assertThat(scheme.urin(authority, path).asString(), equalTo(scheme.asString() + "://" + authority.asString() + path.asString(NEVER_PREFIX_WITH_DOT_SEGMENT)));
    }

    @Test
    void urinWithNoQueryAndNoFragmentAuthorityIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Authority authority = anAuthority();
        final AbsolutePath<String> path = anAbsolutePath();
        final var urin = scheme.urin(authority, path);
        assertThat(urin.hasAuthority(), equalTo(true));
        assertThat(urin.authority(), equalTo(authority));
    }

    @Test
    void urinWithNoQueryAndNoFragmentPathIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Authority authority = anAuthority();
        final AbsolutePath<String> path = anAbsolutePath();
        final var urin = scheme.urin(authority, path);
        assertThat(urin.path(), equalTo(path));
        assertThrows(UnsupportedOperationException.class, urin::fragment, "Attempt to get fragment from a UrinReference that does not have one.");
    }

    @Test
    void createsUrinWithNoPathNoQueryAndNoFragment() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Authority authority = anAuthority();
        assertThat(scheme.urin(authority).asString(), equalTo(scheme.asString() + "://" + authority.asString()));
    }

    @Test
    void urinWithNoPathNoQueryAndNoFragmentAuthorityIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Authority authority = anAuthority();
        final var urin = scheme.urin(authority);
        assertThat(urin.hasAuthority(), equalTo(true));
        assertThat(urin.authority(), equalTo(authority));
    }

    @Test
    void urinWithNoPathNoQueryAndNoFragmentPathIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Authority authority = anAuthority();
        final var urin = scheme.urin(authority);
        assertThat(urin.path(), equalTo(new EmptyPath<>()));
        assertThrows(UnsupportedOperationException.class, urin::fragment, "Attempt to get fragment from a UrinReference that does not have one.");
    }

    @Test
    void createsUrinWithNoAuthorityNoQueryAndNoFragment() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Path<String> path = aPath();
        assertThat(scheme.urin(path).asString(), equalTo(scheme.asString() + ":" + path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY)));
    }

    @Test
    void urinWithNoAuthorityNoQueryAndNoFragmentAuthorityIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Path<String> path = aPath();
        final var urin = scheme.urin(path);
        assertThat(urin.hasAuthority(), equalTo(false));
        assertThrows(UnsupportedOperationException.class, urin::authority, "Attempt to get authority from a UrinReference that does not have one.");
    }

    @Test
    void urinWithNoAuthorityNoQueryAndNoFragmentPathIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Path<String> path = aPath();
        final var urin = scheme.urin(path);
        assertThat(urin.path(), equalTo(path));
        assertThrows(UnsupportedOperationException.class, urin::fragment, "Attempt to get fragment from a UrinReference that does not have one.");
    }

    @Test
    void createsUrinWithNoAuthorityNoPathNoQueryAndNoFragment() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        assertThat(scheme.urin().asString(), equalTo(scheme.asString() + ":"));
    }

    @Test
    void urinWithNoAuthorityNoPathNoQueryAndNoFragmentAuthorityIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final var urin = scheme.urin();
        assertThat(urin.hasAuthority(), equalTo(false));
        assertThrows(UnsupportedOperationException.class, urin::authority, "Attempt to get authority from a UrinReference that does not have one.");
    }

    @Test
    void urinWithNoAuthorityNoPathNoQueryAndNoFragmentPathIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final var urin = scheme.urin();
        assertThat(urin.path(), equalTo(new EmptyPath<>()));
        assertThrows(UnsupportedOperationException.class, urin::fragment, "Attempt to get fragment from a UrinReference that does not have one.");
    }

    @Test
    void urinWithNoQueryAndNoFragmentQueryIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Authority authority = anAuthority();
        final AbsolutePath<String> path = anAbsolutePath();
        final var urin = scheme.urin(authority, path);
        assertThat(urin.hasQuery(), equalTo(false));
        assertThrows(UnsupportedOperationException.class, urin::query, "Attempt to get query from a UrinReference that does not have one.");
    }

    @Test
    void urinWithNoAuthorityNoQueryAndNoFragmentQueryIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Path<String> path = aPath();
        final var urin = scheme.urin(path);
        assertThat(urin.hasQuery(), equalTo(false));
        assertThrows(UnsupportedOperationException.class, urin::query, "Attempt to get query from a UrinReference that does not have one.");
    }

    @Test
    void urinWithNoPathNoQueryAndNoFragmentQueryIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Authority authority = anAuthority();
        final var urin = scheme.urin(authority);
        assertThat(urin.hasQuery(), equalTo(false));
        assertThrows(UnsupportedOperationException.class, urin::query, "Attempt to get query from a UrinReference that does not have one.");
    }

    @Test
    void urinWithNoAuthorityNoPathNoQueryAndNoFragmentQueryIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final var urin = scheme.urin();
        assertThat(urin.hasQuery(), equalTo(false));
        assertThrows(UnsupportedOperationException.class, urin::query, "Attempt to get query from a UrinReference that does not have one.");
    }

    @Test
    void urinWithNoQueryAndNoFragmentFragmentIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Authority authority = anAuthority();
        final AbsolutePath<String> path = anAbsolutePath();
        assertThat(scheme.urin(authority, path).hasFragment(), equalTo(false));
    }

    @Test
    void urinWithNoAuthorityNoQueryAndNoFragmentFragmentIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Path<String> path = aPath();
        assertThat(scheme.urin(path).hasFragment(), equalTo(false));
    }

    @Test
    void urinWithNoPathNoQueryAndNoFragmentFragmentIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Authority authority = anAuthority();
        assertThat(scheme.urin(authority).hasFragment(), equalTo(false));
    }

    @Test
    void urinWithNoAuthorityNoPathNoQueryAndNoFragmentFragmentIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        assertThat(scheme.urin().hasFragment(), equalTo(false));
    }

    @Test
    void aUrinWithNoQueryOrFragmentIsNotEqualToAnotherWithTheADifferentParts() {
        assertThat(aScheme().urin(anAuthority(), anAbsolutePath()), not(equalTo(aScheme().urin(anAuthority(), anAbsolutePath()))));
    }

    @Test
    void aUrinWithNoQueryOrFragmentOrAuthorityOrPathIsEqualToAnotherWithTheSameParts() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        assertThat(scheme.urin(), equalTo(scheme.urin()));
        assertThat(scheme.urin().hashCode(), equalTo(scheme.urin().hashCode()));
    }

    @Test
    void aUrinWithNoQueryOrFragmentIsNotEqualToAnotherWithTheADifferentScheme() {
        assertThat(aScheme().urin(), not(equalTo(aScheme().urin())));
    }

    @Test
    void aUrinWithNoQueryOrFragmentOrAuthorityOrPathToStringIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        assertThat(scheme.urin().toString(), equalTo(scheme.urin().asString()));
    }

    @Test
    void aUrinWithNoQueryOrFragmentOrAuthorityOrPathReplacedWithANewPathIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(scheme.urin().withPath(newPath), equalTo(scheme.urin(newPath)));
    }

    @Test
    void aUrinWithNoQueryOrFragmentOrAuthorityIsEqualToAnotherWithTheSameParts() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Path<String> path = aPath();
        assertThat(scheme.urin(path), equalTo(scheme.urin(path)));
        assertThat(scheme.urin(path).hashCode(), equalTo(scheme.urin(path).hashCode()));
    }

    @Test
    void aUrinWithNoQueryOrFragmentIsNotEqualToAnotherWithTheADifferentPath() {
        assertThat(aScheme().urin(aPath()), not(equalTo(aScheme().urin(aPath()))));
    }

    @Test
    void aUrinWithNoQueryOrFragmentOrAuthorityToStringIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Path<String> path = aPath();
        assertThat(scheme.urin(path).toString(), equalTo(scheme.urin(path).asString()));
    }

    @Test
    void aUrinWithNoQueryOrFragmentOrAuthorityReplacedWithANewPathIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Path<String> path = aPath();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(scheme.urin(path).withPath(newPath), equalTo(scheme.urin(newPath)));
    }

    @Test
    @SuppressWarnings("ConstantValue")
    void rejectsNullInFactoryForAUrinWithNoQueryOrNoFragmentOrAuthority() {
        assertThrows(NullPointerException.class, () -> {
            final Path<String> path = null;
            aScheme().urin(path);
        }, "Null hierarchicalPart should throw NullPointerException in factory");
    }

    @Test
    void aUrinWithNoQueryOrFragmentOrPathIsEqualToAnotherWithTheSameParts() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Authority authority = anAuthority();
        assertThat(scheme.urin(authority), equalTo(scheme.urin(authority)));
        assertThat(scheme.urin(authority).hashCode(), equalTo(scheme.urin(authority).hashCode()));
    }

    @Test
    void aUrinWithNoQueryOrFragmentIsNotEqualToAnotherWithTheADifferentAuthority() {
        assertThat(aScheme().urin(anAuthority()), not(equalTo(aScheme().urin(anAuthority()))));
    }

    @Test
    void aUrinWithNoQueryOrFragmentOrPathToStringIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Authority authority = anAuthority();
        assertThat(scheme.urin(authority).toString(), equalTo(scheme.urin(authority).asString()));
    }

    @Test
    void aUrinWithNoQueryOrFragmentOrPathReplacedWithANewPathIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Authority authority = anAuthority();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(scheme.urin(authority).withPath(newPath), equalTo(scheme.urin(authority, newPath)));
    }

    @Test
    @SuppressWarnings("ConstantValue")
    void rejectsNullInFactoryForAUrinWithNoQueryOrNoFragmentOrPath() {
        assertThrows(NullPointerException.class, () -> {
            final Authority authority = null;
            aScheme().urin(authority);
        }, "Null hierarchicalPart should throw NullPointerException in factory");
    }

    @Test
    void aUrinWithNoQueryOrFragmentIsEqualToAnotherWithTheSameParts() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Authority authority = anAuthority();
        final AbsolutePath<String> path = anAbsolutePath();
        assertThat(scheme.urin(authority, path), equalTo(scheme.urin(authority, path)));
        assertThat(scheme.urin(authority, path).hashCode(), equalTo(scheme.urin(authority, path).hashCode()));
    }

    @Test
    void aUrinWithNoQueryOrFragmentIsNotEqualToAnotherWithTheADifferentAuthorityAndPath() {
        assertThat(aScheme().urin(anAuthority(), anAbsolutePath()), not(equalTo(aScheme().urin(anAuthority(), anAbsolutePath()))));
    }

    @Test
    void aUrinWithNoQueryOrFragmentToStringIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Authority authority = anAuthority();
        final AbsolutePath<String> path = anAbsolutePath();
        assertThat(scheme.urin(authority, path).toString(), equalTo(scheme.urin(authority, path).asString()));
    }

    @Test
    void aUrinWithNoQueryOrFragmentReplacedWithANewPathIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Authority authority = anAuthority();
        final AbsolutePath<String> path = anAbsolutePath();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(scheme.urin(authority, path).withPath(newPath), equalTo(scheme.urin(authority, newPath)));
    }

    @Test
    @SuppressWarnings("ConstantValue")
    void rejectsNullInFactoryForAUrinWithNoQueryOrNoFragment() {
        assertThrows(NullPointerException.class, () -> {
            final Authority authority = null;
            aScheme().urin(authority, anAbsolutePath());
        }, "Null hierarchicalPart should throw NullPointerException in factory");
        assertThrows(NullPointerException.class, () -> {
            final AbsolutePath<String> path = null;
            aScheme().urin(anAuthority(), path);
        }, "Null hierarchicalPart should throw NullPointerException in factory");
    }

    @Test
    void aUrinWithNoQueryOrAuthorityOrPathIsEqualToAnotherWithTheSameParts() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(fragment), equalTo(scheme.urin(fragment)));
        assertThat(scheme.urin(fragment).hashCode(), equalTo(scheme.urin(fragment).hashCode()));
    }

    @Test
    void aUrinWithNoQueryIsNotEqualToAnotherWithTheADifferentSchemeAndFragment() {
        assertThat(aScheme().urin(aFragment()), not(equalTo(aScheme().urin(aFragment()))));
    }

    @Test
    void aUrinWithNoQueryOrAuthorityOrPathToStringIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(fragment).toString(), equalTo(scheme.urin(fragment).asString()));
    }

    @Test
    void aUrinWithNoQueryOrAuthorityOrPathReplacedWithANewPathIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Fragment<String> fragment = aFragment();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(scheme.urin(fragment).withPath(newPath), equalTo(scheme.urin(newPath, fragment)));
    }

    @Test
    @SuppressWarnings("ConstantValue")
    void rejectsNullInFactoryForAUrinWithNoQueryOrAuthorityOrPath() {
        assertThrows(NullPointerException.class, () -> {
            final Fragment<String> fragment = null;
            aScheme().urin(fragment);
        }, "Null hierarchicalPart should throw NullPointerException in factory");
    }

    @Test
    void aUrinWithNoQueryOrAuthorityIsEqualToAnotherWithTheSameParts() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Path<String> path = aPath();
        final Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(path, fragment), equalTo(scheme.urin(path, fragment)));
        assertThat(scheme.urin(path, fragment).hashCode(), equalTo(scheme.urin(path, fragment).hashCode()));
    }

    @Test
    void aUrinWithNoQueryIsNotEqualToAnotherWithTheADifferentPathAndFragment() {
        assertThat(aScheme().urin(aPath(), aFragment()), not(equalTo(aScheme().urin(aPath(), aFragment()))));
    }

    @Test
    void aUrinWithNoQueryOrAuthorityToStringIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Path<String> path = aPath();
        final Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(path, fragment).toString(), equalTo(scheme.urin(path, fragment).asString()));
    }

    @Test
    void aUrinWithNoQueryOrAuthorityReplacedWithANewPathIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Path<String> path = aPath();
        final Fragment<String> fragment = aFragment();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(scheme.urin(path, fragment).withPath(newPath), equalTo(scheme.urin(newPath, fragment)));
    }

    @Test
    @SuppressWarnings("ConstantValue")
    void rejectsNullInFactoryForAUrinWithNoQueryOrAuthority() {
        assertThrows(NullPointerException.class, () -> {
            final Path<String> path = null;
            aScheme().urin(path, aFragment());
        }, "Null hierarchicalPart should throw NullPointerException in factory");
        assertThrows(NullPointerException.class, () -> {
            final Fragment<String> fragment = null;
            aScheme().urin(aPath(), fragment);
        }, "Null hierarchicalPart should throw NullPointerException in factory");
    }

    @Test
    void aUrinWithNoQueryOrPathIsEqualToAnotherWithTheSameParts() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Authority authority = anAuthority();
        final Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(authority, fragment), equalTo(scheme.urin(authority, fragment)));
        assertThat(scheme.urin(authority, fragment).hashCode(), equalTo(scheme.urin(authority, fragment).hashCode()));
    }

    @Test
    void aUrinWithNoQueryIsNotEqualToAnotherWithTheADifferentAuthorityAndFragment() {
        assertThat(aScheme().urin(anAuthority(), aFragment()), not(equalTo(aScheme().urin(anAuthority(), aFragment()))));
    }

    @Test
    void aUrinWithNoQueryOrPathToStringIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Authority authority = anAuthority();
        final Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(authority, fragment).toString(), equalTo(scheme.urin(authority, fragment).asString()));
    }

    @Test
    void aUrinWithNoQueryOrPathReplacedWithANewPathIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Authority authority = anAuthority();
        final Fragment<String> fragment = aFragment();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(scheme.urin(authority, fragment).withPath(newPath), equalTo(scheme.urin(authority, newPath, fragment)));
    }

    @Test
    @SuppressWarnings("ConstantValue")
    void rejectsNullInFactoryForAUrinWithNoQueryOrPath() {
        assertThrows(NullPointerException.class, () -> {
            final Authority authority = null;
            aScheme().urin(authority, aFragment());
        }, "Null hierarchicalPart should throw NullPointerException in factory");
        assertThrows(NullPointerException.class, () -> {
            final Fragment<String> fragment = null;
            aScheme().urin(anAuthority(), fragment);
        }, "Null hierarchicalPart should throw NullPointerException in factory");
    }

    @Test
    void aUrinWithNoQueryIsEqualToAnotherWithTheSameParts() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Authority authority = anAuthority();
        final AbsolutePath<String> path = anAbsolutePath();
        final Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(authority, path, fragment), equalTo(scheme.urin(authority, path, fragment)));
        assertThat(scheme.urin(authority, path, fragment).hashCode(), equalTo(scheme.urin(authority, path, fragment).hashCode()));
    }

    @Test
    void aUrinWithNoQueryOrFragmentIsNotEqualToAnotherWithTheADifferentAuthorityAndPathAndFragment() {
        assertThat(aScheme().urin(anAuthority(), anAbsolutePath(), aFragment()), not(equalTo(aScheme().urin(anAuthority(), anAbsolutePath(), aFragment()))));
    }

    @Test
    void aUrinWithNoQueryToStringIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Authority authority = anAuthority();
        final AbsolutePath<String> path = anAbsolutePath();
        final Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(authority, path, fragment).toString(), equalTo(scheme.urin(authority, path, fragment).asString()));
    }

    @Test
    void aUrinWithNoQueryReplacedWithANewPathIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Authority authority = anAuthority();
        final AbsolutePath<String> path = anAbsolutePath();
        final Fragment<String> fragment = aFragment();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(scheme.urin(authority, path, fragment).withPath(newPath), equalTo(scheme.urin(authority, newPath, fragment)));
    }

    @Test
    @SuppressWarnings("ConstantValue")
    void rejectsNullInFactoryForAUrinWithNoQuery() {
        assertThrows(NullPointerException.class, () -> {
            final Authority authority = null;
            aScheme().urin(authority, anAbsolutePath(), aFragment());
        }, "Null hierarchicalPart should throw NullPointerException in factory");
        assertThrows(NullPointerException.class, () -> {
            final AbsolutePath<String> path = null;
            aScheme().urin(anAuthority(), path, aFragment());
        }, "Null hierarchicalPart should throw NullPointerException in factory");
        assertThrows(NullPointerException.class, () -> {
            final Fragment<String> fragment = null;
            aScheme().urin(anAuthority(), anAbsolutePath(), fragment);
        }, "Null hierarchicalPart should throw NullPointerException in factory");
    }

    @Test
    void aUrinWithNoFragmentOrAuthorityOrPathIsEqualToAnotherWithTheSameParts() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Query<String> query = aQuery();
        assertThat(scheme.urin(query), equalTo(scheme.urin(query)));
        assertThat(scheme.urin(query).hashCode(), equalTo(scheme.urin(query).hashCode()));
    }

    @Test
    void aUrinWithNoFragmentIsNotEqualToAnotherWithTheADifferentSchemeAndQuery() {
        assertThat(aScheme().urin(aQuery()), not(equalTo(aScheme().urin(aQuery()))));
    }

    @Test
    void aUrinWithNoFragmentOrAuthorityOrPathToStringIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Query<String> query = aQuery();
        assertThat(scheme.urin(query).toString(), equalTo(scheme.urin(query).asString()));
    }

    @Test
    void aUrinWithNoFragmentOrAuthorityOrPathReplacedWithANewPathIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Query<String> query = aQuery();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(scheme.urin(query).withPath(newPath), equalTo(scheme.urin(newPath, query)));
    }

    @Test
    @SuppressWarnings("ConstantValue")
    void rejectsNullInFactoryForAUrinWithNoFragmentOrAuthorityOrPath() {
        assertThrows(NullPointerException.class, () -> {
            final Query<String> query = null;
            aScheme().urin(query);
        }, "Null hierarchicalPart should throw NullPointerException in factory");
    }

    @Test
    void aUrinWithNoFragmentOrAuthorityIsEqualToAnotherWithTheSameParts() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Path<String> path = aPath();
        final Query<String> query = aQuery();
        assertThat(scheme.urin(path, query), equalTo(scheme.urin(path, query)));
        assertThat(scheme.urin(path, query).hashCode(), equalTo(scheme.urin(path, query).hashCode()));
    }

    @Test
    void aUrinWithNoFragmentIsNotEqualToAnotherWithTheADifferentPathAndQuery() {
        assertThat(aScheme().urin(aPath(), aQuery()), not(equalTo(aScheme().urin(aPath(), aQuery()))));
    }

    @Test
    void aUrinWithNoFragmentOrAuthorityToStringIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Path<String> path = aPath();
        final Query<String> query = aQuery();
        assertThat(scheme.urin(path, query).toString(), equalTo(scheme.urin(path, query).asString()));
    }

    @Test
    void aUrinWithNoFragmentOrAuthorityReplacedWithANewPathIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Path<String> path = aPath();
        final Query<String> query = aQuery();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(scheme.urin(path, query).withPath(newPath), equalTo(scheme.urin(newPath, query)));
    }

    @Test
    @SuppressWarnings("ConstantValue")
    void rejectsNullInFactoryForAUrinWithNoFragmentOrAuthority() {
        assertThrows(NullPointerException.class, () -> {
            final Path<String> path = null;
            aScheme().urin(path, aQuery());
        }, "Null hierarchicalPart should throw NullPointerException in factory");
        assertThrows(NullPointerException.class, () -> {
            final Query<String> query = null;
            aScheme().urin(aPath(), query);
        }, "Null hierarchicalPart should throw NullPointerException in factory");
    }

    @Test
    void aUrinWithNoFragmentOrPathIsEqualToAnotherWithTheSameParts() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Authority authority = anAuthority();
        final Query<String> query = aQuery();
        assertThat(scheme.urin(authority, query), equalTo(scheme.urin(authority, query)));
        assertThat(scheme.urin(authority, query).hashCode(), equalTo(scheme.urin(authority, query).hashCode()));
    }

    @Test
    void aUrinWithNoFragmentIsNotEqualToAnotherWithTheADifferentAuthorityAndQuery() {
        assertThat(aScheme().urin(anAuthority(), aQuery()), not(equalTo(aScheme().urin(anAuthority(), aQuery()))));
    }

    @Test
    void aUrinWithNoFragmentOrPathToStringIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Authority authority = anAuthority();
        final Query<String> query = aQuery();
        assertThat(scheme.urin(authority, query).toString(), equalTo(scheme.urin(authority, query).asString()));
    }

    @Test
    void aUrinWithNoFragmentOrPathReplacedWithANewPathIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Authority authority = anAuthority();
        final Query<String> query = aQuery();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(scheme.urin(authority, query).withPath(newPath), equalTo(scheme.urin(authority, newPath, query)));
    }

    @Test
    @SuppressWarnings("ConstantValue")
    void rejectsNullInFactoryForAUrinWithNoFragmentOrPath() {
        assertThrows(NullPointerException.class, () -> {
            final Authority authority = null;
            aScheme().urin(authority, aQuery());
        }, "Null hierarchicalPart should throw NullPointerException in factory");
        assertThrows(NullPointerException.class, () -> {
            final Query<String> query = null;
            aScheme().urin(anAuthority(), query);
        }, "Null hierarchicalPart should throw NullPointerException in factory");
    }

    @Test
    void aUrinWithNoFragmentIsEqualToAnotherWithTheSameParts() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Authority authority = anAuthority();
        final AbsolutePath<String> path = anAbsolutePath();
        final Query<String> query = aQuery();
        assertThat(scheme.urin(authority, path, query), equalTo(scheme.urin(authority, path, query)));
        assertThat(scheme.urin(authority, path, query).hashCode(), equalTo(scheme.urin(authority, path, query).hashCode()));
    }

    @Test
    void aUrinWithNoFragmentOrQueryIsNotEqualToAnotherWithTheADifferentAuthorityAndPathAndQuery() {
        assertThat(aScheme().urin(anAuthority(), anAbsolutePath(), aQuery()), not(equalTo(aScheme().urin(anAuthority(), anAbsolutePath(), aQuery()))));
    }

    @Test
    void aUrinWithNoFragmentToStringIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Authority authority = anAuthority();
        final AbsolutePath<String> path = anAbsolutePath();
        final Query<String> query = aQuery();
        assertThat(scheme.urin(authority, path, query).toString(), equalTo(scheme.urin(authority, path, query).asString()));
    }

    @Test
    void aUrinWithNoFragmentReplacedWithANewPathIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Authority authority = anAuthority();
        final AbsolutePath<String> path = anAbsolutePath();
        final Query<String> query = aQuery();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(scheme.urin(authority, path, query).withPath(newPath), equalTo(scheme.urin(authority, newPath, query)));
    }

    @Test
    @SuppressWarnings("ConstantValue")
    void rejectsNullInFactoryForAUrinWithNoFragment() {
        assertThrows(NullPointerException.class, () -> {
            final Authority authority = null;
            aScheme().urin(authority, anAbsolutePath(), aQuery());
        }, "Null hierarchicalPart should throw NullPointerException in factory");
        assertThrows(NullPointerException.class, () -> {
            final AbsolutePath<String> path = null;
            aScheme().urin(anAuthority(), path, aQuery());
        }, "Null hierarchicalPart should throw NullPointerException in factory");
        assertThrows(NullPointerException.class, () -> {
            final Query<String> query = null;
            aScheme().urin(anAuthority(), anAbsolutePath(), query);
        }, "Null hierarchicalPart should throw NullPointerException in factory");
    }

    @Test
    void aUrinWithNoAuthorityOrPathIsEqualToAnotherWithTheSameParts() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(query, fragment), equalTo(scheme.urin(query, fragment)));
        assertThat(scheme.urin(query, fragment).hashCode(), equalTo(scheme.urin(query, fragment).hashCode()));
    }

    @Test
    void aUrinWithNoAuthorityOrPathIsNotEqualToAnotherWithTheADifferentSchemeAndQuery() {
        assertThat(aScheme().urin(aQuery(), aFragment()), not(equalTo(aScheme().urin(aQuery(), aFragment()))));
    }

    @Test
    void aUrinWithNoAuthorityOrPathToStringIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(query, fragment).toString(), equalTo(scheme.urin(query, fragment).asString()));
    }

    @Test
    void aUrinWithNoAuthorityOrPathReplacedWithANewPathIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(scheme.urin(query, fragment).withPath(newPath), equalTo(scheme.urin(newPath, query, fragment)));
    }

    @Test
    @SuppressWarnings("ConstantValue")
    void rejectsNullInFactoryForAUrinWithNoAuthorityOrPath() {
        assertThrows(NullPointerException.class, () -> {
            final Query<String> query = null;
            aScheme().urin(query, aFragment());
        }, "Null hierarchicalPart should throw NullPointerException in factory");
        assertThrows(NullPointerException.class, () -> {
            final Fragment<String> fragment = null;
            aScheme().urin(aQuery(), fragment);
        }, "Null hierarchicalPart should throw NullPointerException in factory");
    }

    @Test
    void aUrinWithNoAuthorityIsEqualToAnotherWithTheSameParts() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Path<String> path = aPath();
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(path, query, fragment), equalTo(scheme.urin(path, query, fragment)));
        assertThat(scheme.urin(path, query, fragment).hashCode(), equalTo(scheme.urin(path, query, fragment).hashCode()));
    }

    @Test
    void aUrinWithNoAuthorityIsNotEqualToAnotherWithTheADifferentPathAndQuery() {
        assertThat(aScheme().urin(aPath(), aQuery(), aFragment()), not(equalTo(aScheme().urin(aPath(), aQuery(), aFragment()))));
    }

    @Test
    void aUrinWithNoAuthorityToStringIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Path<String> path = aPath();
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(path, query, fragment).toString(), equalTo(scheme.urin(path, query, fragment).asString()));
    }

    @Test
    void aUrinWithNoAuthorityReplacedWithANewPathIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Path<String> path = aPath();
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(scheme.urin(path, query, fragment).withPath(newPath), equalTo(scheme.urin(newPath, query, fragment)));
    }

    @Test
    @SuppressWarnings("ConstantValue")
    void rejectsNullInFactoryForAUrinWithNoAuthority() {
        assertThrows(NullPointerException.class, () -> {
            final Path<String> path = null;
            aScheme().urin(path, aQuery(), aFragment());
        }, "Null hierarchicalPart should throw NullPointerException in factory");
        assertThrows(NullPointerException.class, () -> {
            final Query<String> query = null;
            aScheme().urin(aPath(), query, aFragment());
        }, "Null hierarchicalPart should throw NullPointerException in factory");
        assertThrows(NullPointerException.class, () -> {
            final Fragment<String> fragment = null;
            aScheme().urin(aPath(), aQuery(), fragment);
        }, "Null hierarchicalPart should throw NullPointerException in factory");
    }

    @Test
    void aUrinWithNoPathIsEqualToAnotherWithTheSameParts() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Authority authority = anAuthority();
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(authority, query, fragment), equalTo(scheme.urin(authority, query, fragment)));
        assertThat(scheme.urin(authority, query, fragment).hashCode(), equalTo(scheme.urin(authority, query, fragment).hashCode()));
    }

    @Test
    void aUrinWithNoPathIsNotEqualToAnotherWithTheADifferentAuthorityAndQuery() {
        assertThat(aScheme().urin(anAuthority(), aQuery(), aFragment()), not(equalTo(aScheme().urin(anAuthority(), aQuery(), aFragment()))));
    }

    @Test
    void aUrinWithNoPathToStringIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Authority authority = anAuthority();
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(authority, query, fragment).toString(), equalTo(scheme.urin(authority, query, fragment).asString()));
    }

    @Test
    void aUrinWithNoPathToStringIsCorrectReplacedWithANewPathIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Authority authority = anAuthority();
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(scheme.urin(authority, query, fragment).withPath(newPath), equalTo(scheme.urin(authority, newPath, query, fragment)));
    }

    @Test
    @SuppressWarnings("ConstantValue")
    void rejectsNullInFactoryForAUrinWithNoPath() {
        assertThrows(NullPointerException.class, () -> {
            final Authority authority = null;
            aScheme().urin(authority, aQuery(), aFragment());
        }, "Null hierarchicalPart should throw NullPointerException in factory");
        assertThrows(NullPointerException.class, () -> {
            final Query<String> query = null;
            aScheme().urin(anAuthority(), query, aFragment());
        }, "Null hierarchicalPart should throw NullPointerException in factory");
        assertThrows(NullPointerException.class, () -> {
            final Fragment<String> fragment = null;
            aScheme().urin(anAuthority(), aQuery(), fragment);
        }, "Null hierarchicalPart should throw NullPointerException in factory");
    }

    @Test
    void aUrinWithAllPartsIsEqualToAnotherWithTheSameParts() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Authority authority = anAuthority();
        final AbsolutePath<String> path = anAbsolutePath();
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(authority, path, query, fragment), equalTo(scheme.urin(authority, path, query, fragment)));
        assertThat(scheme.urin(authority, path, query, fragment).hashCode(), equalTo(scheme.urin(authority, path, query, fragment).hashCode()));
    }

    @Test
    void aUrinWithAllPartsIsNotEqualToAnotherWithTheADifferentAuthorityAndPathAndQuery() {
        assertThat(aScheme().urin(anAuthority(), anAbsolutePath(), aQuery(), aFragment()), not(equalTo(aScheme().urin(anAuthority(), anAbsolutePath(), aQuery(), aFragment()))));
    }

    @Test
    void aUrinWithAllPartsToStringIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Authority authority = anAuthority();
        final AbsolutePath<String> path = anAbsolutePath();
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(authority, path, query, fragment).toString(), equalTo(scheme.urin(authority, path, query, fragment).asString()));
    }

    @Test
    void aUrinWithAllPartsReplacedWithANewPathIsCorrect() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Authority authority = anAuthority();
        final AbsolutePath<String> path = anAbsolutePath();
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(scheme.urin(authority, path, query, fragment).withPath(newPath), equalTo(scheme.urin(authority, newPath, query, fragment)));
    }

    @Test
    @SuppressWarnings("ConstantValue")
    void rejectsNullInFactoryForAUrinWithAllParts() {
        assertThrows(NullPointerException.class, () -> {
            final Authority authority = null;
            aScheme().urin(authority, anAbsolutePath(), aQuery(), aFragment());
        }, "Null hierarchicalPart should throw NullPointerException in factory");
        assertThrows(NullPointerException.class, () -> {
            final AbsolutePath<String> path = null;
            aScheme().urin(anAuthority(), path, aQuery(), aFragment());
        }, "Null hierarchicalPart should throw NullPointerException in factory");
        assertThrows(NullPointerException.class, () -> {
            final Query<String> query = null;
            aScheme().urin(anAuthority(), anAbsolutePath(), query, aFragment());
        }, "Null hierarchicalPart should throw NullPointerException in factory");
        assertThrows(NullPointerException.class, () -> {
            final Fragment<String> fragment = null;
            aScheme().urin(anAuthority(), anAbsolutePath(), aQuery(), fragment);
        }, "Null hierarchicalPart should throw NullPointerException in factory");
    }

    @Test
    void parsesUrinWithAllParts() throws Exception {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Authority authority = anAuthority();
        final AbsolutePath<String> path = anUnpollutedAbsolutePath();
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        assertThat(scheme.parseUrin(scheme.asString() + "://" + authority.asString() + path.asString(NEVER_PREFIX_WITH_DOT_SEGMENT) + "?" + query.asString() + "#" + fragment.asString()), equalTo(scheme.urin(authority, path, query, fragment)));
    }

    @Test
    void parsesUrinWithNoAuthority() throws Exception {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Path<String> path = anUnpollutedPath();
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        assertThat(scheme.parseUrin(scheme.asString() + ":" + path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY) + "?" + query.asString() + "#" + fragment.asString()), equalTo(scheme.urin(path, query, fragment)));
    }

    @Test
    void parsesUrinWithEmptyPath() throws Exception {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Authority authority = anAuthority();
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        assertThat(scheme.parseUrin(scheme.asString() + "://" + authority.asString() + "?" + query.asString() + "#" + fragment.asString()), equalTo(scheme.urin(authority, query, fragment)));
    }

    @Test
    void parsesUrinWithNoAuthorityOrPath() throws Exception {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        assertThat(scheme.parseUrin(scheme.asString() + ":" + "?" + query.asString() + "#" + fragment.asString()), equalTo(scheme.urin(query, fragment)));
    }

    @Test
    void parsesUrinWithNoFragment() throws Exception {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Authority authority = anAuthority();
        final AbsolutePath<String> path = anUnpollutedAbsolutePath();
        final Query<String> query = aQuery();
        assertThat(scheme.parseUrin(scheme.asString() + "://" + authority.asString() + path.asString(NEVER_PREFIX_WITH_DOT_SEGMENT) + "?" + query.asString()), equalTo(scheme.urin(authority, path, query)));
    }

    @Test
    void parsesUrinWithNoFragmentAndNoAuthority() throws Exception {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Path<String> path = anUnpollutedPath();
        final Query<String> query = aQuery();
        assertThat(scheme.parseUrin(scheme.asString() + ":" + path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY) + "?" + query.asString()), equalTo(scheme.urin(path, query)));
    }

    @Test
    void parsesUrinWithNoQuery() throws Exception {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Authority authority = anAuthority();
        final AbsolutePath<String> path = anUnpollutedAbsolutePath();
        final Fragment<String> fragment = aFragment();
        assertThat(scheme.parseUrin(scheme.asString() + "://" + authority.asString() + path.asString(NEVER_PREFIX_WITH_DOT_SEGMENT) + "#" + fragment.asString()), equalTo(scheme.urin(authority, path, fragment)));
    }

    @Test
    void parsesUrinWithNoQueryOrAuthority() throws Exception {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final AbsolutePath<String> path = anUnpollutedAbsolutePath();
        final Fragment<String> fragment = aFragment();
        assertThat(scheme.parseUrin(scheme.asString() + ":" + path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY) + "#" + fragment.asString()), equalTo(scheme.urin(path, fragment)));
    }

    @Test
    void parsesUrinWithNoQueryAndNoFragment() throws Exception {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Authority authority = anAuthority();
        final AbsolutePath<String> path = anUnpollutedAbsolutePath();
        assertThat(scheme.parseUrin(scheme.asString() + "://" + authority.asString() + path.asString(NEVER_PREFIX_WITH_DOT_SEGMENT)), equalTo(scheme.urin(authority, path)));
    }

    @Test
    void parsesUrinWithNoQueryAndNoFragmentAndNoAuthorityAndNoPath() throws Exception {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        assertThat(scheme.parseUrin(scheme.asString() + ":"), equalTo(scheme.urin()));
    }

    @Test
    void parsesUrinWithNoQueryAndNoFragmentAndEmptyPath() throws Exception {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Authority authority = anAuthority();
        assertThat(scheme.parseUrin(scheme.asString() + "://" + authority.asString()), equalTo(scheme.urin(authority)));
    }

    @Test
    void parsesUrinWithNoQueryAndNoFragmentAndNoAuthority() throws Exception {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final AbsolutePath<String> path = anUnpollutedAbsolutePath();
        assertThat(scheme.parseUrin(scheme.asString() + ":" + path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY)), equalTo(scheme.urin(path)));
    }

    @Test
    void parsingEmptyStringThrowsParseException() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        assertThrows(ParseException.class, () -> scheme.parseUrin(""), "Empty String should throw ParseException");
    }

    @Test
    void parsingNullThrowsNullPointerException() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        assertThrows(NullPointerException.class, () -> scheme.parseUrin((String) null), "Null value should throw NullPointerException in parser");
    }

    @Test
    void resolvesAUrinToItself() {
        final var urinReference = aUrin();
        assertThat(aUrin().resolve(urinReference), equalTo(urinReference));
    }

    @Test
    void urinCreatedWithASchemeSpecifyingADefaultPortDifferentToThatUsedIsEqualToIdenticalUrinCreatedWithASchemeWithoutDefaultPort() {
        final String schemeName = aValidSchemeName();
        final Authority authority = anAuthority();
        assertThat(scheme(schemeName, aPort()).urin(authority), equalTo(scheme(schemeName).urin(authority)));
    }

    @Test
    void urinCreatedWithASchemeSpecifyingADefaultPortEqualToThatUsedIsEqualToIdenticalUrinCreatedWithASchemeWithoutDefaultPortAndWithoutPort() {
        final String schemeName = aValidSchemeName();
        final Port port = aPort();
        final Host host = aHost();
        assertThat(scheme(schemeName, port).urin(authority(host, port)), equalTo(scheme(schemeName).urin(authority(host))));
    }
}
