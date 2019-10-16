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

class UrinTest {

    @Test
    void createsUrinWithAllParts() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anAbsolutePath();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(authority, path, query, fragment).asString(), equalTo(scheme.asString() + "://" + authority.asString() + path.asString(NEVER_PREFIX_WITH_DOT_SEGMENT) + "?" + query.asString() + "#" + fragment.asString()));
    }

    @Test
    void urinWithAllPartsAuthorityIsCorrect() {
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
    void urinWithAllPartsPathIsCorrect() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anAbsolutePath();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(authority, path, query, fragment).path(), equalTo((Path) path));
    }

    @Test
    void urinWithAllPartsQueryIsCorrect() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anAbsolutePath();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(authority, path, query, fragment).hasQuery(), equalTo(true));
        assertThat(scheme.urin(authority, path, query, fragment).query(), equalTo(query));
    }

    @Test
    void urinWithAllPartsButAuthorityAuthorityIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Path<String> path = aPath();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        final Urin urin = scheme.urin(path, query, fragment);
        assertThat(urin.hasAuthority(), equalTo(false));
        assertThrowsException("Attempt to get authority from a UrinReference that does not have one.", UnsupportedOperationException.class, urin::authority);
    }

    @Test
    void urinWithAllPartsButAuthorityQueryIsCorrect() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Path<String> path = aPath();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(path, query, fragment).hasQuery(), equalTo(true));
        assertThat(scheme.urin(path, query, fragment).query(), equalTo(query));
    }

    @Test
    void urinWithAllPartsButPathQueryIsCorrect() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(authority, query, fragment).hasQuery(), equalTo(true));
        assertThat(scheme.urin(authority, query, fragment).query(), equalTo(query));
    }

    @Test
    void urinWithAllPartsButAuthorityAndPathQueryIsCorrect() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(query, fragment).hasQuery(), equalTo(true));
        assertThat(scheme.urin(query, fragment).query(), equalTo(query));
    }

    @Test
    void urinWithAllPartsFragmentIsCorrect() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anAbsolutePath();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(authority, path, query, fragment).hasFragment(), equalTo(true));
        assertThat(scheme.urin(authority, path, query, fragment).fragment(), equalTo(fragment));
    }

    @Test
    void aUrinWithAllPartsIsNotEqualToAnotherWithTheADifferentParts() {
        assertThat(aScheme().urin(anAuthority(), anAbsolutePath(), aQuery(), aFragment()), not(equalTo(aScheme().urin(anAuthority(), anAbsolutePath(), aQuery(), aFragment()))));
    }

    @Test
    void aUrinWithAllPartsToStringIsCorrectWithHierarchicalPart() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anAbsolutePath();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(authority, path, query, fragment).toString(), equalTo(scheme.urin(authority, path, query, fragment).asString()));
    }


    @Test
    void urinWithNoFragmentAuthorityIsCorrect() {
        AbsolutePath<String> path = anAbsolutePath();
        final Authority authority = anAuthority();
        final Urin urin = aScheme().urin(authority, path, aQuery());
        assertThat(urin.hasAuthority(), equalTo(true));
        assertThat(urin.authority(), equalTo(authority));
    }

    @Test
    void urinWithNoFragmentPathIsCorrect() {
        AbsolutePath<String> path = anAbsolutePath();
        assertThat(aScheme().urin(anAuthority(), path, aQuery()).path(), equalTo((Path) path));
    }

    @Test
    void urinWithNoFragmentOrPathAuthorityIsCorrect() throws Exception {
        final Urin urin = aScheme().urin(aQuery());
        assertThat(urin.hasAuthority(), equalTo(false));
        assertThrowsException("Attempt to get authority from a UrinReference that does not have one.", UnsupportedOperationException.class, urin::authority);
    }

    @Test
    void urinWithNoFragmentOrPathPathIsCorrect() {
        assertThat(aScheme().urin(aQuery()).path(), equalTo(new EmptyPath()));
    }

    @Test
    void urinWithNoFragmentOrAuthorityAuthorityIsCorrect() throws Exception {
        Path<String> path = aPath();
        final Urin urin = aScheme().urin(path, aQuery());
        assertThat(urin.hasAuthority(), equalTo(false));
        assertThrowsException("Attempt to get authority from a UrinReference that does not have one.", UnsupportedOperationException.class, urin::authority);
    }

    @Test
    void urinWithNoFragmentOrAuthorityPathIsCorrect() {
        Path<String> path = aPath();
        assertThat(aScheme().urin(path, aQuery()).path(), equalTo(path));
    }

    @Test
    void urinWithNoFragmentOrAuthorityOrPathAuthorityIsCorrect() throws Exception {
        final Urin urin = aScheme().urin(aQuery());
        assertThat(urin.hasAuthority(), equalTo(false));
        assertThrowsException("Attempt to get authority from a UrinReference that does not have one.", UnsupportedOperationException.class, urin::authority);
    }

    @Test
    void urinWithNoFragmentOrAuthorityOrPathPathIsCorrect() {
        assertThat(aScheme().urin(aQuery()).path(), equalTo(new EmptyPath()));
    }

    @Test
    void urinWithNoFragmentOrAuthorityQueryIsCorrect() {
        Query<String> query = aQuery();
        final Urin<String, Query<String>, Fragment<String>> urin = aScheme().urin(aPath(), query);
        assertThat(urin.hasQuery(), equalTo(true));
        assertThat(urin.query(), equalTo(query));
    }

    @Test
    void urinWithNoFragmentOrPathQueryIsCorrect() {
        Query<String> query = aQuery();
        final Urin<String, Query<String>, Fragment<String>> urin = aScheme().urin(anAuthority(), query);
        assertThat(urin.hasQuery(), equalTo(true));
        assertThat(urin.query(), equalTo(query));
    }

    @Test
    void urinWithNoFragmentOrAuthorityOrPathQueryIsCorrect() {
        Query<String> query = aQuery();
        final Urin<String, Query<String>, Fragment<String>> urin = aScheme().urin(query);
        assertThat(urin.hasQuery(), equalTo(true));
        assertThat(urin.query(), equalTo(query));
    }

    @Test
    void urinWithNoFragmentQueryIsCorrect() {
        Query<String> query = aQuery();
        final Urin<String, Query<String>, Fragment<String>> urin = aScheme().urin(anAuthority(), anAbsolutePath(), query);
        assertThat(urin.hasQuery(), equalTo(true));
        assertThat(urin.query(), equalTo(query));
    }

    @Test
    void urinWithNoFragmentFragmentIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Query<String> query = aQuery();
        final Urin urin = scheme.urin(anAuthority(), anAbsolutePath(), query);
        assertThat(urin.hasFragment(), equalTo(false));
        assertThrowsException("Attempt to get fragment from a UrinReference that does not have one.", UnsupportedOperationException.class, urin::fragment);
    }

    @Test
    void urinWithNoFragmentOrAuthorityFragmentIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Query<String> query = aQuery();
        final Urin urin = scheme.urin(aPath(), query);
        assertThat(urin.hasFragment(), equalTo(false));
        assertThrowsException("Attempt to get fragment from a UrinReference that does not have one.", UnsupportedOperationException.class, urin::fragment);
    }

    @Test
    void urinWithNoFragmentOrPathFragmentIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Query<String> query = aQuery();
        final Urin urin = scheme.urin(anAuthority(), query);
        assertThat(urin.hasFragment(), equalTo(false));
        assertThrowsException("Attempt to get fragment from a UrinReference that does not have one.", UnsupportedOperationException.class, urin::fragment);
    }

    @Test
    void urinWithNoFragmentOrAuthorityOrPathFragmentIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Query<String> query = aQuery();
        final Urin urin = scheme.urin(query);
        assertThat(urin.hasFragment(), equalTo(false));
        assertThrowsException("Attempt to get fragment from a UrinReference that does not have one.", UnsupportedOperationException.class, urin::fragment);
    }

    @Test
    void aUrinWithNoFragmentIsNotEqualToAnotherWithTheADifferentParts() {
        assertThat(aScheme().urin(aQuery()), not(equalTo(aScheme().urin(aQuery()))));
    }

    @Test
    void createsUrinWithNoQuery() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anAbsolutePath();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(authority, path, fragment).asString(), equalTo(scheme.asString() + "://" + authority.asString() + path.asString(NEVER_PREFIX_WITH_DOT_SEGMENT) + "#" + fragment.asString()));
    }

    @Test
    void urinWithNoQueryAuthorityIsCorrect() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anAbsolutePath();
        Fragment<String> fragment = aFragment();
        final Urin urin = scheme.urin(authority, path, fragment);
        assertThat(urin.hasAuthority(), equalTo(true));
        assertThat(urin.authority(), equalTo(authority));
    }

    @Test
    void urinWithNoQueryPathIsCorrect() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anAbsolutePath();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(authority, path, fragment).path(), equalTo((Path) path));
    }

    @Test
    void urinWithNoAuthorityNoQueryAuthorityIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Path<String> path = aPath();
        Fragment<String> fragment = aFragment();
        final Urin urin = scheme.urin(path, fragment);
        assertThat(urin.hasAuthority(), equalTo(false));
        assertThrowsException("Attempt to get authority from a UrinReference that does not have one.", UnsupportedOperationException.class, urin::authority);
    }

    @Test
    void urinWithNoAuthorityNoQueryPathIsCorrect() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Path<String> path = aPath();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(path, fragment).path(), equalTo(path));
    }

    @Test
    void urinWithNoPathNoQueryAuthorityIsCorrect() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        Fragment<String> fragment = aFragment();
        final Urin urin = scheme.urin(authority, fragment);
        assertThat(urin.hasAuthority(), equalTo(true));
        assertThat(urin.authority(), equalTo(authority));
    }

    @Test
    void urinWithNoPathNoQueryPathIsCorrect() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(authority, fragment).path(), equalTo(new EmptyPath()));
    }

    @Test
    void urinWithNoAuthorityNoPathNoQueryAuthorityIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Fragment<String> fragment = aFragment();
        final Urin urin = scheme.urin(fragment);
        assertThat(urin.hasAuthority(), equalTo(false));
        assertThrowsException("Attempt to get authority from a UrinReference that does not have one.", UnsupportedOperationException.class, urin::authority);
    }

    @Test
    void urinWithNoAuthorityNoPathNoQueryPathIsCorrect() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(fragment).path(), equalTo(new EmptyPath()));
    }

    @Test
    void urinWithNoQueryQueryIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anAbsolutePath();
        Fragment<String> fragment = aFragment();
        final Urin urin = scheme.urin(authority, path, fragment);
        assertThat(urin.hasQuery(), equalTo(false));
        assertThrowsException("Attempt to get query from a UrinReference that does not have one.", UnsupportedOperationException.class, urin::query);
    }

    @Test
    void urinWithNoAuthorityNoQueryQueryIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Path<String> path = aPath();
        Fragment<String> fragment = aFragment();
        final Urin urin = scheme.urin(path, fragment);
        assertThat(urin.hasQuery(), equalTo(false));
        assertThrowsException("Attempt to get query from a UrinReference that does not have one.", UnsupportedOperationException.class, urin::query);
    }

    @Test
    void urinWithNoPathNoQueryQueryIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        Fragment<String> fragment = aFragment();
        final Urin urin = scheme.urin(authority, fragment);
        assertThat(urin.hasQuery(), equalTo(false));
        assertThrowsException("Attempt to get query from a UrinReference that does not have one.", UnsupportedOperationException.class, urin::query);
    }

    @Test
    void urinWithNoAuthorityNoPathNoQueryQueryIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Fragment<String> fragment = aFragment();
        final Urin urin = scheme.urin(fragment);
        assertThat(urin.hasQuery(), equalTo(false));
        assertThrowsException("Attempt to get query from a UrinReference that does not have one.", UnsupportedOperationException.class, urin::query);
    }

    @Test
    void urinWithNoQueryFragmentIsCorrect() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anAbsolutePath();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(authority, path, fragment).hasFragment(), equalTo(true));
        assertThat(scheme.urin(authority, path, fragment).fragment(), equalTo(fragment));
    }

    @Test
    void urinWithNoAuthorityNoQueryFragmentIsCorrect() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        AbsolutePath<String> path = anAbsolutePath();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(path, fragment).hasFragment(), equalTo(true));
        assertThat(scheme.urin(path, fragment).fragment(), equalTo(fragment));
    }

    @Test
    void urinWithNoPathNoQueryFragmentIsCorrect() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(authority, fragment).hasFragment(), equalTo(true));
        assertThat(scheme.urin(authority, fragment).fragment(), equalTo(fragment));
    }

    @Test
    void urinWithNoAuthorityNoPathNoQueryFragmentIsCorrect() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(fragment).hasFragment(), equalTo(true));
        assertThat(scheme.urin(fragment).fragment(), equalTo(fragment));
    }

    @Test
    void createsUrinWithNoQueryAndNoFragment() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anAbsolutePath();
        assertThat(scheme.urin(authority, path).asString(), equalTo(scheme.asString() + "://" + authority.asString() + path.asString(NEVER_PREFIX_WITH_DOT_SEGMENT)));
    }

    @Test
    void urinWithNoQueryAndNoFragmentAuthorityIsCorrect() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anAbsolutePath();
        final Urin urin = scheme.urin(authority, path);
        assertThat(urin.hasAuthority(), equalTo(true));
        assertThat(urin.authority(), equalTo(authority));
    }

    @Test
    void urinWithNoQueryAndNoFragmentPathIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anAbsolutePath();
        final Urin urin = scheme.urin(authority, path);
        assertThat(urin.path(), equalTo(path));
        assertThrowsException("Attempt to get fragment from a UrinReference that does not have one.", UnsupportedOperationException.class, urin::fragment);
    }

    @Test
    void createsUrinWithNoPathNoQueryAndNoFragment() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        assertThat(scheme.urin(authority).asString(), equalTo(scheme.asString() + "://" + authority.asString()));
    }

    @Test
    void urinWithNoPathNoQueryAndNoFragmentAuthorityIsCorrect() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        final Urin urin = scheme.urin(authority);
        assertThat(urin.hasAuthority(), equalTo(true));
        assertThat(urin.authority(), equalTo(authority));
    }

    @Test
    void urinWithNoPathNoQueryAndNoFragmentPathIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        final Urin urin = scheme.urin(authority);
        assertThat(urin.path(), equalTo(new EmptyPath()));
        assertThrowsException("Attempt to get fragment from a UrinReference that does not have one.", UnsupportedOperationException.class, urin::fragment);
    }

    @Test
    void createsUrinWithNoAuthorityNoQueryAndNoFragment() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Path<String> path = aPath();
        assertThat(scheme.urin(path).asString(), equalTo(scheme.asString() + ":" + path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY)));
    }

    @Test
    void urinWithNoAuthorityNoQueryAndNoFragmentAuthorityIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Path<String> path = aPath();
        final Urin<String, Query<String>, Fragment<String>> urin = scheme.urin(path);
        assertThat(urin.hasAuthority(), equalTo(false));
        assertThrowsException("Attempt to get authority from a UrinReference that does not have one.", UnsupportedOperationException.class, urin::authority);
    }

    @Test
    void urinWithNoAuthorityNoQueryAndNoFragmentPathIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Path<String> path = aPath();
        final Urin<String, Query<String>, Fragment<String>> urin = scheme.urin(path);
        assertThat(urin.path(), equalTo(path));
        assertThrowsException("Attempt to get fragment from a UrinReference that does not have one.", UnsupportedOperationException.class, urin::fragment);
    }

    @Test
    void createsUrinWithNoAuthorityNoPathNoQueryAndNoFragment() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        assertThat(scheme.urin().asString(), equalTo(scheme.asString() + ":"));
    }

    @Test
    void urinWithNoAuthorityNoPathNoQueryAndNoFragmentAuthorityIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Urin urin = scheme.urin();
        assertThat(urin.hasAuthority(), equalTo(false));
        assertThrowsException("Attempt to get authority from a UrinReference that does not have one.", UnsupportedOperationException.class, urin::authority);
    }

    @Test
    void urinWithNoAuthorityNoPathNoQueryAndNoFragmentPathIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Urin urin = scheme.urin();
        assertThat(urin.path(), equalTo(new EmptyPath()));
        assertThrowsException("Attempt to get fragment from a UrinReference that does not have one.", UnsupportedOperationException.class, urin::fragment);
    }

    @Test
    void urinWithNoQueryAndNoFragmentQueryIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anAbsolutePath();
        final Urin urin = scheme.urin(authority, path);
        assertThat(urin.hasQuery(), equalTo(false));
        assertThrowsException("Attempt to get query from a UrinReference that does not have one.", UnsupportedOperationException.class, urin::query);
    }

    @Test
    void urinWithNoAuthorityNoQueryAndNoFragmentQueryIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Path<String> path = aPath();
        final Urin urin = scheme.urin(path);
        assertThat(urin.hasQuery(), equalTo(false));
        assertThrowsException("Attempt to get query from a UrinReference that does not have one.", UnsupportedOperationException.class, urin::query);
    }

    @Test
    void urinWithNoPathNoQueryAndNoFragmentQueryIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        final Urin urin = scheme.urin(authority);
        assertThat(urin.hasQuery(), equalTo(false));
        assertThrowsException("Attempt to get query from a UrinReference that does not have one.", UnsupportedOperationException.class, urin::query);
    }

    @Test
    void urinWithNoAuthorityNoPathNoQueryAndNoFragmentQueryIsCorrect() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        final Urin urin = scheme.urin();
        assertThat(urin.hasQuery(), equalTo(false));
        assertThrowsException("Attempt to get query from a UrinReference that does not have one.", UnsupportedOperationException.class, urin::query);
    }

    @Test
    void urinWithNoQueryAndNoFragmentFragmentIsCorrect() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anAbsolutePath();
        assertThat(scheme.urin(authority, path).hasFragment(), equalTo(false));
    }

    @Test
    void urinWithNoAuthorityNoQueryAndNoFragmentFragmentIsCorrect() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Path<String> path = aPath();
        assertThat(scheme.urin(path).hasFragment(), equalTo(false));
    }

    @Test
    void urinWithNoPathNoQueryAndNoFragmentFragmentIsCorrect() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        assertThat(scheme.urin(authority).hasFragment(), equalTo(false));
    }

    @Test
    void urinWithNoAuthorityNoPathNoQueryAndNoFragmentFragmentIsCorrect() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        assertThat(scheme.urin().hasFragment(), equalTo(false));
    }

    @Test
    void aUrinWithNoQueryOrFragmentIsNotEqualToAnotherWithTheADifferentParts() {
        assertThat(aScheme().urin(anAuthority(), anAbsolutePath()), not(equalTo(aScheme().urin(anAuthority(), anAbsolutePath()))));
    }

    @Test
    void aUrinWithNoQueryOrFragmentOrAuthorityOrPathIsEqualToAnotherWithTheSameParts() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        assertThat(scheme.urin(), equalTo(scheme.urin()));
        assertThat(scheme.urin().hashCode(), equalTo(scheme.urin().hashCode()));
    }

    @Test
    void aUrinWithNoQueryOrFragmentIsNotEqualToAnotherWithTheADifferentScheme() {
        assertThat(aScheme().urin(), not(equalTo(aScheme().urin())));
    }

    @Test
    void aUrinWithNoQueryOrFragmentOrAuthorityOrPathToStringIsCorrect() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
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
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Path<String> path = aPath();
        assertThat(scheme.urin(path), equalTo(scheme.urin(path)));
        assertThat(scheme.urin(path).hashCode(), equalTo(scheme.urin(path).hashCode()));
    }

    @Test
    void aUrinWithNoQueryOrFragmentIsNotEqualToAnotherWithTheADifferentPath() {
        assertThat(aScheme().urin(aPath()), not(equalTo(aScheme().urin(aPath()))));
    }

    @Test
    void aUrinWithNoQueryOrFragmentOrAuthorityToStringIsCorrect() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Path<String> path = aPath();
        assertThat(scheme.urin(path).toString(), equalTo(scheme.urin(path).asString()));
    }

    @Test
    void aUrinWithNoQueryOrFragmentOrAuthorityReplacedWithANewPathIsCorrect() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Path<String> path = aPath();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(scheme.urin(path).withPath(newPath), equalTo(scheme.urin(newPath)));
    }

    @Test
    void rejectsNullInFactoryForAUrinWithNoQueryOrNoFragmentOrAuthority() throws Exception {
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, () -> {
            Path<String> path = null;
            aScheme().urin(path);
        });
    }

    @Test
    void aUrinWithNoQueryOrFragmentOrPathIsEqualToAnotherWithTheSameParts() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        assertThat(scheme.urin(authority), equalTo(scheme.urin(authority)));
        assertThat(scheme.urin(authority).hashCode(), equalTo(scheme.urin(authority).hashCode()));
    }

    @Test
    void aUrinWithNoQueryOrFragmentIsNotEqualToAnotherWithTheADifferentAuthority() {
        assertThat(aScheme().urin(anAuthority()), not(equalTo(aScheme().urin(anAuthority()))));
    }

    @Test
    void aUrinWithNoQueryOrFragmentOrPathToStringIsCorrect() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        assertThat(scheme.urin(authority).toString(), equalTo(scheme.urin(authority).asString()));
    }

    @Test
    void aUrinWithNoQueryOrFragmentOrPathReplacedWithANewPathIsCorrect() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(scheme.urin(authority).withPath(newPath), equalTo(scheme.urin(authority, newPath)));
    }

    @Test
    void rejectsNullInFactoryForAUrinWithNoQueryOrNoFragmentOrPath() throws Exception {
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, () -> {
            Authority authority = null;
            aScheme().urin(authority);
        });
    }

    @Test
    void aUrinWithNoQueryOrFragmentIsEqualToAnotherWithTheSameParts() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anAbsolutePath();
        assertThat(scheme.urin(authority, path), equalTo(scheme.urin(authority, path)));
        assertThat(scheme.urin(authority, path).hashCode(), equalTo(scheme.urin(authority, path).hashCode()));
    }

    @Test
    void aUrinWithNoQueryOrFragmentIsNotEqualToAnotherWithTheADifferentAuthorityAndPath() {
        assertThat(aScheme().urin(anAuthority(), anAbsolutePath()), not(equalTo(aScheme().urin(anAuthority(), anAbsolutePath()))));
    }

    @Test
    void aUrinWithNoQueryOrFragmentToStringIsCorrect() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anAbsolutePath();
        assertThat(scheme.urin(authority, path).toString(), equalTo(scheme.urin(authority, path).asString()));
    }

    @Test
    void aUrinWithNoQueryOrFragmentReplacedWithANewPathIsCorrect() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anAbsolutePath();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(scheme.urin(authority, path).withPath(newPath), equalTo(scheme.urin(authority, newPath)));
    }

    @Test
    void rejectsNullInFactoryForAUrinWithNoQueryOrNoFragment() throws Exception {
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, () -> {
            Authority authority = null;
            aScheme().urin(authority, anAbsolutePath());
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, () -> {
            AbsolutePath<String> path = null;
            aScheme().urin(anAuthority(), path);
        });
    }

    @Test
    void aUrinWithNoQueryOrAuthorityOrPathIsEqualToAnotherWithTheSameParts() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(fragment), equalTo(scheme.urin(fragment)));
        assertThat(scheme.urin(fragment).hashCode(), equalTo(scheme.urin(fragment).hashCode()));
    }

    @Test
    void aUrinWithNoQueryIsNotEqualToAnotherWithTheADifferentSchemeAndFragment() {
        assertThat(aScheme().urin(aFragment()), not(equalTo(aScheme().urin(aFragment()))));
    }

    @Test
    void aUrinWithNoQueryOrAuthorityOrPathToStringIsCorrect() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(fragment).toString(), equalTo(scheme.urin(fragment).asString()));
    }

    @Test
    void aUrinWithNoQueryOrAuthorityOrPathReplacedWithANewPathIsCorrect() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Fragment<String> fragment = aFragment();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(scheme.urin(fragment).withPath(newPath), equalTo(scheme.urin(newPath, fragment)));
    }

    @Test
    void rejectsNullInFactoryForAUrinWithNoQueryOrAuthorityOrPath() throws Exception {
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, () -> {
            Fragment<String> fragment = null;
            aScheme().urin(fragment);
        });
    }

    @Test
    void aUrinWithNoQueryOrAuthorityIsEqualToAnotherWithTheSameParts() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Path<String> path = aPath();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(path, fragment), equalTo(scheme.urin(path, fragment)));
        assertThat(scheme.urin(path, fragment).hashCode(), equalTo(scheme.urin(path, fragment).hashCode()));
    }

    @Test
    void aUrinWithNoQueryIsNotEqualToAnotherWithTheADifferentPathAndFragment() {
        assertThat(aScheme().urin(aPath(), aFragment()), not(equalTo(aScheme().urin(aPath(), aFragment()))));
    }

    @Test
    void aUrinWithNoQueryOrAuthorityToStringIsCorrect() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Path<String> path = aPath();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(path, fragment).toString(), equalTo(scheme.urin(path, fragment).asString()));
    }

    @Test
    void aUrinWithNoQueryOrAuthorityReplacedWithANewPathIsCorrect() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Path<String> path = aPath();
        Fragment<String> fragment = aFragment();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(scheme.urin(path, fragment).withPath(newPath), equalTo(scheme.urin(newPath, fragment)));
    }

    @Test
    void rejectsNullInFactoryForAUrinWithNoQueryOrAuthority() throws Exception {
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, () -> {
            Path<String> path = null;
            aScheme().urin(path, aFragment());
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, () -> {
            Fragment<String> fragment = null;
            aScheme().urin(aPath(), fragment);
        });
    }

    @Test
    void aUrinWithNoQueryOrPathIsEqualToAnotherWithTheSameParts() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(authority, fragment), equalTo(scheme.urin(authority, fragment)));
        assertThat(scheme.urin(authority, fragment).hashCode(), equalTo(scheme.urin(authority, fragment).hashCode()));
    }

    @Test
    void aUrinWithNoQueryIsNotEqualToAnotherWithTheADifferentAuthorityAndFragment() {
        assertThat(aScheme().urin(anAuthority(), aFragment()), not(equalTo(aScheme().urin(anAuthority(), aFragment()))));
    }

    @Test
    void aUrinWithNoQueryOrPathToStringIsCorrect() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(authority, fragment).toString(), equalTo(scheme.urin(authority, fragment).asString()));
    }

    @Test
    void aUrinWithNoQueryOrPathReplacedWithANewPathIsCorrect() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        Fragment<String> fragment = aFragment();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(scheme.urin(authority, fragment).withPath(newPath), equalTo(scheme.urin(authority, newPath, fragment)));
    }

    @Test
    void rejectsNullInFactoryForAUrinWithNoQueryOrPath() throws Exception {
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, () -> {
            Authority authority = null;
            aScheme().urin(authority, aFragment());
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, () -> {
            Fragment<String> fragment = null;
            aScheme().urin(anAuthority(), fragment);
        });
    }

    @Test
    void aUrinWithNoQueryIsEqualToAnotherWithTheSameParts() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anAbsolutePath();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(authority, path, fragment), equalTo(scheme.urin(authority, path, fragment)));
        assertThat(scheme.urin(authority, path, fragment).hashCode(), equalTo(scheme.urin(authority, path, fragment).hashCode()));
    }

    @Test
    void aUrinWithNoQueryOrFragmentIsNotEqualToAnotherWithTheADifferentAuthorityAndPathAndFragment() {
        assertThat(aScheme().urin(anAuthority(), anAbsolutePath(), aFragment()), not(equalTo(aScheme().urin(anAuthority(), anAbsolutePath(), aFragment()))));
    }

    @Test
    void aUrinWithNoQueryToStringIsCorrect() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anAbsolutePath();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(authority, path, fragment).toString(), equalTo(scheme.urin(authority, path, fragment).asString()));
    }

    @Test
    void aUrinWithNoQueryReplacedWithANewPathIsCorrect() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anAbsolutePath();
        Fragment<String> fragment = aFragment();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(scheme.urin(authority, path, fragment).withPath(newPath), equalTo(scheme.urin(authority, newPath, fragment)));
    }

    @Test
    void rejectsNullInFactoryForAUrinWithNoQuery() throws Exception {
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, () -> {
            Authority authority = null;
            aScheme().urin(authority, anAbsolutePath(), aFragment());
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, () -> {
            AbsolutePath<String> path = null;
            aScheme().urin(anAuthority(), path, aFragment());
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, () -> {
            Fragment<String> fragment = null;
            aScheme().urin(anAuthority(), anAbsolutePath(), fragment);
        });
    }

    @Test
    void aUrinWithNoFragmentOrAuthorityOrPathIsEqualToAnotherWithTheSameParts() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Query<String> query = aQuery();
        assertThat(scheme.urin(query), equalTo(scheme.urin(query)));
        assertThat(scheme.urin(query).hashCode(), equalTo(scheme.urin(query).hashCode()));
    }

    @Test
    void aUrinWithNoFragmentIsNotEqualToAnotherWithTheADifferentSchemeAndQuery() {
        assertThat(aScheme().urin(aQuery()), not(equalTo(aScheme().urin(aQuery()))));
    }

    @Test
    void aUrinWithNoFragmentOrAuthorityOrPathToStringIsCorrect() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Query<String> query = aQuery();
        assertThat(scheme.urin(query).toString(), equalTo(scheme.urin(query).asString()));
    }

    @Test
    void aUrinWithNoFragmentOrAuthorityOrPathReplacedWithANewPathIsCorrect() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Query<String> query = aQuery();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(scheme.urin(query).withPath(newPath), equalTo(scheme.urin(newPath, query)));
    }

    @Test
    void rejectsNullInFactoryForAUrinWithNoFragmentOrAuthorityOrPath() throws Exception {
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, () -> {
            Query<String> query = null;
            aScheme().urin(query);
        });
    }

    @Test
    void aUrinWithNoFragmentOrAuthorityIsEqualToAnotherWithTheSameParts() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Path<String> path = aPath();
        Query<String> query = aQuery();
        assertThat(scheme.urin(path, query), equalTo(scheme.urin(path, query)));
        assertThat(scheme.urin(path, query).hashCode(), equalTo(scheme.urin(path, query).hashCode()));
    }

    @Test
    void aUrinWithNoFragmentIsNotEqualToAnotherWithTheADifferentPathAndQuery() {
        assertThat(aScheme().urin(aPath(), aQuery()), not(equalTo(aScheme().urin(aPath(), aQuery()))));
    }

    @Test
    void aUrinWithNoFragmentOrAuthorityToStringIsCorrect() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Path<String> path = aPath();
        Query<String> query = aQuery();
        assertThat(scheme.urin(path, query).toString(), equalTo(scheme.urin(path, query).asString()));
    }

    @Test
    void aUrinWithNoFragmentOrAuthorityReplacedWithANewPathIsCorrect() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Path<String> path = aPath();
        Query<String> query = aQuery();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(scheme.urin(path, query).withPath(newPath), equalTo(scheme.urin(newPath, query)));
    }

    @Test
    void rejectsNullInFactoryForAUrinWithNoFragmentOrAuthority() throws Exception {
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, () -> {
            Path<String> path = null;
            aScheme().urin(path, aQuery());
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, () -> {
            Query<String> query = null;
            aScheme().urin(aPath(), query);
        });
    }

    @Test
    void aUrinWithNoFragmentOrPathIsEqualToAnotherWithTheSameParts() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        Query<String> query = aQuery();
        assertThat(scheme.urin(authority, query), equalTo(scheme.urin(authority, query)));
        assertThat(scheme.urin(authority, query).hashCode(), equalTo(scheme.urin(authority, query).hashCode()));
    }

    @Test
    void aUrinWithNoFragmentIsNotEqualToAnotherWithTheADifferentAuthorityAndQuery() {
        assertThat(aScheme().urin(anAuthority(), aQuery()), not(equalTo(aScheme().urin(anAuthority(), aQuery()))));
    }

    @Test
    void aUrinWithNoFragmentOrPathToStringIsCorrect() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        Query<String> query = aQuery();
        assertThat(scheme.urin(authority, query).toString(), equalTo(scheme.urin(authority, query).asString()));
    }

    @Test
    void aUrinWithNoFragmentOrPathReplacedWithANewPathIsCorrect() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        Query<String> query = aQuery();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(scheme.urin(authority, query).withPath(newPath), equalTo(scheme.urin(authority, newPath, query)));
    }

    @Test
    void rejectsNullInFactoryForAUrinWithNoFragmentOrPath() throws Exception {
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, () -> {
            Authority authority = null;
            aScheme().urin(authority, aQuery());
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, () -> {
            Query<String> query = null;
            aScheme().urin(anAuthority(), query);
        });
    }

    @Test
    void aUrinWithNoFragmentIsEqualToAnotherWithTheSameParts() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anAbsolutePath();
        Query<String> query = aQuery();
        assertThat(scheme.urin(authority, path, query), equalTo(scheme.urin(authority, path, query)));
        assertThat(scheme.urin(authority, path, query).hashCode(), equalTo(scheme.urin(authority, path, query).hashCode()));
    }

    @Test
    void aUrinWithNoFragmentOrQueryIsNotEqualToAnotherWithTheADifferentAuthorityAndPathAndQuery() {
        assertThat(aScheme().urin(anAuthority(), anAbsolutePath(), aQuery()), not(equalTo(aScheme().urin(anAuthority(), anAbsolutePath(), aQuery()))));
    }

    @Test
    void aUrinWithNoFragmentToStringIsCorrect() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anAbsolutePath();
        Query<String> query = aQuery();
        assertThat(scheme.urin(authority, path, query).toString(), equalTo(scheme.urin(authority, path, query).asString()));
    }

    @Test
    void aUrinWithNoFragmentReplacedWithANewPathIsCorrect() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anAbsolutePath();
        Query<String> query = aQuery();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(scheme.urin(authority, path, query).withPath(newPath), equalTo(scheme.urin(authority, newPath, query)));
    }

    @Test
    void rejectsNullInFactoryForAUrinWithNoFragment() throws Exception {
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, () -> {
            Authority authority = null;
            aScheme().urin(authority, anAbsolutePath(), aQuery());
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, () -> {
            AbsolutePath<String> path = null;
            aScheme().urin(anAuthority(), path, aQuery());
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, () -> {
            Query<String> query = null;
            aScheme().urin(anAuthority(), anAbsolutePath(), query);
        });
    }

    @Test
    void aUrinWithNoAuthorityOrPathIsEqualToAnotherWithTheSameParts() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(query, fragment), equalTo(scheme.urin(query, fragment)));
        assertThat(scheme.urin(query, fragment).hashCode(), equalTo(scheme.urin(query, fragment).hashCode()));
    }

    @Test
    void aUrinWithNoAuthorityOrPathIsNotEqualToAnotherWithTheADifferentSchemeAndQuery() {
        assertThat(aScheme().urin(aQuery(), aFragment()), not(equalTo(aScheme().urin(aQuery(), aFragment()))));
    }

    @Test
    void aUrinWithNoAuthorityOrPathToStringIsCorrect() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(query, fragment).toString(), equalTo(scheme.urin(query, fragment).asString()));
    }

    @Test
    void aUrinWithNoAuthorityOrPathReplacedWithANewPathIsCorrect() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(scheme.urin(query, fragment).withPath(newPath), equalTo(scheme.urin(newPath, query, fragment)));
    }

    @Test
    void rejectsNullInFactoryForAUrinWithNoAuthorityOrPath() throws Exception {
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, () -> {
            Query<String> query = null;
            aScheme().urin(query, aFragment());
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, () -> {
            Fragment<String> fragment = null;
            aScheme().urin(aQuery(), fragment);
        });
    }

    @Test
    void aUrinWithNoAuthorityIsEqualToAnotherWithTheSameParts() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Path<String> path = aPath();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(path, query, fragment), equalTo(scheme.urin(path, query, fragment)));
        assertThat(scheme.urin(path, query, fragment).hashCode(), equalTo(scheme.urin(path, query, fragment).hashCode()));
    }

    @Test
    void aUrinWithNoAuthorityIsNotEqualToAnotherWithTheADifferentPathAndQuery() {
        assertThat(aScheme().urin(aPath(), aQuery(), aFragment()), not(equalTo(aScheme().urin(aPath(), aQuery(), aFragment()))));
    }

    @Test
    void aUrinWithNoAuthorityToStringIsCorrect() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Path<String> path = aPath();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(path, query, fragment).toString(), equalTo(scheme.urin(path, query, fragment).asString()));
    }

    @Test
    void aUrinWithNoAuthorityReplacedWithANewPathIsCorrect() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Path<String> path = aPath();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(scheme.urin(path, query, fragment).withPath(newPath), equalTo(scheme.urin(newPath, query, fragment)));
    }

    @Test
    void rejectsNullInFactoryForAUrinWithNoAuthority() throws Exception {
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, () -> {
            Path<String> path = null;
            aScheme().urin(path, aQuery(), aFragment());
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, () -> {
            Query<String> query = null;
            aScheme().urin(aPath(), query, aFragment());
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, () -> {
            Fragment<String> fragment = null;
            aScheme().urin(aPath(), aQuery(), fragment);
        });
    }

    @Test
    void aUrinWithNoPathIsEqualToAnotherWithTheSameParts() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(authority, query, fragment), equalTo(scheme.urin(authority, query, fragment)));
        assertThat(scheme.urin(authority, query, fragment).hashCode(), equalTo(scheme.urin(authority, query, fragment).hashCode()));
    }

    @Test
    void aUrinWithNoPathIsNotEqualToAnotherWithTheADifferentAuthorityAndQuery() {
        assertThat(aScheme().urin(anAuthority(), aQuery(), aFragment()), not(equalTo(aScheme().urin(anAuthority(), aQuery(), aFragment()))));
    }

    @Test
    void aUrinWithNoPathToStringIsCorrect() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(authority, query, fragment).toString(), equalTo(scheme.urin(authority, query, fragment).asString()));
    }

    @Test
    void aUrinWithNoPathToStringIsCorrectReplacedWithANewPathIsCorrect() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(scheme.urin(authority, query, fragment).withPath(newPath), equalTo(scheme.urin(authority, newPath, query, fragment)));
    }

    @Test
    void rejectsNullInFactoryForAUrinWithNoPath() throws Exception {
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, () -> {
            Authority authority = null;
            aScheme().urin(authority, aQuery(), aFragment());
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, () -> {
            Query<String> query = null;
            aScheme().urin(anAuthority(), query, aFragment());
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, () -> {
            Fragment<String> fragment = null;
            aScheme().urin(anAuthority(), aQuery(), fragment);
        });
    }

    @Test
    void aUrinWithAllPartsIsEqualToAnotherWithTheSameParts() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anAbsolutePath();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(authority, path, query, fragment), equalTo(scheme.urin(authority, path, query, fragment)));
        assertThat(scheme.urin(authority, path, query, fragment).hashCode(), equalTo(scheme.urin(authority, path, query, fragment).hashCode()));
    }

    @Test
    void aUrinWithAllPartsIsNotEqualToAnotherWithTheADifferentAuthorityAndPathAndQuery() {
        assertThat(aScheme().urin(anAuthority(), anAbsolutePath(), aQuery(), aFragment()), not(equalTo(aScheme().urin(anAuthority(), anAbsolutePath(), aQuery(), aFragment()))));
    }

    @Test
    void aUrinWithAllPartsToStringIsCorrect() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anAbsolutePath();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.urin(authority, path, query, fragment).toString(), equalTo(scheme.urin(authority, path, query, fragment).asString()));
    }

    @Test
    void aUrinWithAllPartsReplacedWithANewPathIsCorrect() {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anAbsolutePath();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(scheme.urin(authority, path, query, fragment).withPath(newPath), equalTo(scheme.urin(authority, newPath, query, fragment)));
    }

    @Test
    void rejectsNullInFactoryForAUrinWithAllParts() throws Exception {
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, () -> {
            Authority authority = null;
            aScheme().urin(authority, anAbsolutePath(), aQuery(), aFragment());
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, () -> {
            AbsolutePath<String> path = null;
            aScheme().urin(anAuthority(), path, aQuery(), aFragment());
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, () -> {
            Query<String> query = null;
            aScheme().urin(anAuthority(), anAbsolutePath(), query, aFragment());
        });
        assertThrowsException("Null hierarchicalPart should throw NullPointerException in factory", NullPointerException.class, () -> {
            Fragment<String> fragment = null;
            aScheme().urin(anAuthority(), anAbsolutePath(), aQuery(), fragment);
        });
    }

    @Test
    void parsesUrinWithAllParts() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anUnpollutedAbsolutePath();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.parseUrin(scheme.asString() + "://" + authority.asString() + path.asString(NEVER_PREFIX_WITH_DOT_SEGMENT) + "?" + query.asString() + "#" + fragment.asString()), equalTo(scheme.urin(authority, path, query, fragment)));
    }

    @Test
    void parsesUrinWithNoAuthority() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Path<String> path = anUnpollutedPath();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.parseUrin(scheme.asString() + ":" + path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY) + "?" + query.asString() + "#" + fragment.asString()), equalTo(scheme.urin(path, query, fragment)));
    }

    @Test
    void parsesUrinWithEmptyPath() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.parseUrin(scheme.asString() + "://" + authority.asString() + "?" + query.asString() + "#" + fragment.asString()), equalTo(scheme.urin(authority, query, fragment)));
    }

    @Test
    void parsesUrinWithNoAuthorityOrPath() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.parseUrin(scheme.asString() + ":" + "?" + query.asString() + "#" + fragment.asString()), equalTo(scheme.urin(query, fragment)));
    }

    @Test
    void parsesUrinWithNoFragment() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anUnpollutedAbsolutePath();
        Query<String> query = aQuery();
        assertThat(scheme.parseUrin(scheme.asString() + "://" + authority.asString() + path.asString(NEVER_PREFIX_WITH_DOT_SEGMENT) + "?" + query.asString()), equalTo(scheme.urin(authority, path, query)));
    }

    @Test
    void parsesUrinWithNoFragmentAndNoAuthority() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Path<String> path = anUnpollutedPath();
        Query<String> query = aQuery();
        assertThat(scheme.parseUrin(scheme.asString() + ":" + path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY) + "?" + query.asString()), equalTo(scheme.urin(path, query)));
    }

    @Test
    void parsesUrinWithNoQuery() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anUnpollutedAbsolutePath();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.parseUrin(scheme.asString() + "://" + authority.asString() + path.asString(NEVER_PREFIX_WITH_DOT_SEGMENT) + "#" + fragment.asString()), equalTo(scheme.urin(authority, path, fragment)));
    }

    @Test
    void parsesUrinWithNoQueryOrAuthority() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        AbsolutePath<String> path = anUnpollutedAbsolutePath();
        Fragment<String> fragment = aFragment();
        assertThat(scheme.parseUrin(scheme.asString() + ":" + path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY) + "#" + fragment.asString()), equalTo(scheme.urin(path, fragment)));
    }

    @Test
    void parsesUrinWithNoQueryAndNoFragment() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        AbsolutePath<String> path = anUnpollutedAbsolutePath();
        assertThat(scheme.parseUrin(scheme.asString() + "://" + authority.asString() + path.asString(NEVER_PREFIX_WITH_DOT_SEGMENT)), equalTo(scheme.urin(authority, path)));
    }

    @Test
    void parsesUrinWithNoQueryAndNoFragmentAndNoAuthorityAndNoPath() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        assertThat(scheme.parseUrin(scheme.asString() + ":"), equalTo(scheme.urin()));
    }

    @Test
    void parsesUrinWithNoQueryAndNoFragmentAndEmptyPath() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        Authority authority = anAuthority();
        assertThat(scheme.parseUrin(scheme.asString() + "://" + authority.asString()), equalTo(scheme.urin(authority)));
    }

    @Test
    void parsesUrinWithNoQueryAndNoFragmentAndNoAuthority() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        AbsolutePath<String> path = anUnpollutedAbsolutePath();
        assertThat(scheme.parseUrin(scheme.asString() + ":" + path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY)), equalTo(scheme.urin(path)));
    }

    @Test
    void parsingEmptyStringThrowsParseException() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        assertThrowsException("Empty String should throw ParseException", ParseException.class, () -> scheme.parseUrin(""));
    }

    @Test
    void parsingNullThrowsNullPointerException() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        assertThrowsException("Null value should throw NullPointerException in parser", NullPointerException.class, () -> scheme.parseUrin((String) null));
    }

    @Test
    void resolvesAUrinToItself() {
        Urin<String, Query<String>, Fragment<String>> urinReference = aUrin();
        assertThat(aUrin().resolve(urinReference), equalTo(urinReference));
    }

    @Test
    void urinCreatedWithASchemeSpecifyingADefaultPortDifferentToThatUsedIsEqualToIdenticalUrinCreatedWithASchemeWithoutDefaultPort() {
        String schemeName = aValidSchemeName();
        Authority authority = anAuthority();
        assertThat(scheme(schemeName, aPort()).urin(authority), equalTo(scheme(schemeName).urin(authority)));
    }

    @Test
    void urinCreatedWithASchemeSpecifyingADefaultPortEqualToThatUsedIsEqualToIdenticalUrinCreatedWithASchemeWithoutDefaultPortAndWithoutPort() {
        String schemeName = aValidSchemeName();
        Port port = aPort();
        Host host = aHost();
        assertThat(scheme(schemeName, port).urin(authority(host, port)), equalTo(scheme(schemeName).urin(authority(host))));
    }
}
