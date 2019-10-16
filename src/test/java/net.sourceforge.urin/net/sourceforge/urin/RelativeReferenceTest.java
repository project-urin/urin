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

import java.net.URI;

import static net.sourceforge.urin.Authority.authority;
import static net.sourceforge.urin.AuthorityBuilder.anAuthority;
import static net.sourceforge.urin.ExceptionAssert.assertThrowsException;
import static net.sourceforge.urin.FragmentBuilder.aFragment;
import static net.sourceforge.urin.HostBuilder.aRegisteredName;
import static net.sourceforge.urin.MoreRandomStringUtils.aStringIncluding;
import static net.sourceforge.urin.Path.PrefixWithDotSegmentCriteria.NEVER_PREFIX_WITH_DOT_SEGMENT;
import static net.sourceforge.urin.Path.PrefixWithDotSegmentCriteria.PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON;
import static net.sourceforge.urin.Path.rootlessPath;
import static net.sourceforge.urin.PathBuilder.*;
import static net.sourceforge.urin.QueryBuilder.aQuery;
import static net.sourceforge.urin.SchemeBuilder.aScheme;
import static net.sourceforge.urin.Segment.segment;
import static net.sourceforge.urin.SegmentBuilder.aNonDotSegment;
import static net.sourceforge.urin.SegmentBuilder.aSegment;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

class RelativeReferenceTest {
    @Test
    void aRelativeReferenceWithEmptyPathAsStringIsCorrect() {
        assertThat(aScheme().relativeReference().asString(), equalTo(""));
        assertThat(aScheme().relativeReference().asUri(), equalTo(URI.create("")));
    }

    @Test
    void aRelativeReferenceWithEmptyPathAuthorityIsCorrect() throws Exception {
        final RelativeReference relativeReference = aScheme().relativeReference();
        assertThat(relativeReference.hasAuthority(), equalTo(false));
        assertThrowsException("Attempt to get authority from a UrinReference that does not have one.", UnsupportedOperationException.class, relativeReference::authority);
    }

    @Test
    void aRelativeReferenceWithEmptyPathPathIsCorrect() {
        assertThat(aScheme().relativeReference().path(), equalTo(Path.<String>rootlessPath()));
    }

    @Test
    void aRelativeReferenceWithEmptyPathQueryIsCorrect() throws Exception {
        final RelativeReference relativeReference = aScheme().relativeReference();
        assertThat(relativeReference.hasQuery(), equalTo(false));
        assertThrowsException("Attempt to get query from a UrinReference that does not have one.", UnsupportedOperationException.class, relativeReference::query);
    }

    @Test
    void aRelativeReferenceWithEmptyPathFragmentIsCorrect() throws Exception {
        final RelativeReference relativeReference = aScheme().relativeReference();
        assertThat(relativeReference.hasFragment(), equalTo(false));
        assertThrowsException("Attempt to get fragment from a UrinReference that does not have one.", UnsupportedOperationException.class, relativeReference::fragment);
    }

    @Test
    void aRelativeReferenceWithEmptyPathIsEqualToAnotherRelativeReferenceWithEmptyPath() {
        assertThat(aScheme().relativeReference(), equalTo(aScheme().relativeReference()));
        assertThat(aScheme().relativeReference().hashCode(), equalTo(aScheme().relativeReference().hashCode()));
    }

    @Test
    void aRelativeReferenceWithEmptyPathToStringIsCorrect() {
        assertThat(aScheme().relativeReference().toString(), equalTo(""));
    }

    @Test
    void aRelativeReferenceWithEmptyPathReplacedWithANewPathIsCorrect() {
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(aScheme().relativeReference().withPath(newPath), equalTo(aScheme().relativeReference(newPath)));
    }

    @Test
    void aRelativeReferenceWithEmptyPathWithQueryAsStringIsCorrect() {
        Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(query).asString(), equalTo("?" + query.asString()));
        assertThat(aScheme().relativeReference(query).asUri(), equalTo(URI.create("?" + query.asString())));
    }

    @Test
    void aRelativeReferenceWithEmptyPathWithQueryAuthorityIsCorrect() throws Exception {
        final RelativeReference relativeReference = aScheme().relativeReference(aQuery());
        assertThat(relativeReference.hasAuthority(), equalTo(false));
        assertThrowsException("Attempt to get authority from a UrinReference that does not have one.", UnsupportedOperationException.class, relativeReference::authority);
    }

    @Test
    void aRelativeReferenceWithEmptyPathWithQueryPathIsCorrect() {
        Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(query).path(), equalTo(Path.<String>rootlessPath()));
    }

    @Test
    void aRelativeReferenceWithEmptyPathWithQueryQueryIsCorrect() {
        Query<String> query = aQuery();
        final RelativeReference<String, Query<String>, Fragment<String>> relativeReference = aScheme().relativeReference(query);
        assertThat(relativeReference.hasQuery(), equalTo(true));
        assertThat(relativeReference.query(), equalTo(query));
    }

    @Test
    void aRelativeReferenceWithEmptyPathWithQueryFragmentIsCorrect() throws Exception {
        Query<String> query = aQuery();
        final RelativeReference relativeReference = aScheme().relativeReference(query);
        assertThat(relativeReference.hasFragment(), equalTo(false));
        assertThrowsException("Attempt to get fragment from a UrinReference that does not have one.", UnsupportedOperationException.class, relativeReference::fragment);
    }

    @Test
    void aRelativeReferenceWithEmptyPathWithQueryIsEqualToAnotherRelativeReferenceWithEmptyPath() {
        Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(query), equalTo(aScheme().relativeReference(query)));
        assertThat(aScheme().relativeReference(query).hashCode(), equalTo(aScheme().relativeReference(query).hashCode()));
    }

    @Test
    void aRelativeReferenceWithEmptyPathWithQueryIsNotEqualToAnotherWithTheADifferentQuery() {
        assertThat(aScheme().relativeReference(aQuery()), not(equalTo(aScheme().relativeReference(aQuery()))));
    }

    @Test
    void aRelativeReferenceWithEmptyPathWithQueryToStringIsCorrect() {
        Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(query).toString(), equalTo(aScheme().relativeReference(query).asString()));
    }

    @Test
    void aRelativeReferenceWithEmptyPathWithQueryReplacedWithANewPathIsCorrect() {
        Query<String> query = aQuery();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(aScheme().relativeReference(query).withPath(newPath), equalTo(aScheme().relativeReference(newPath, query)));
    }

    @Test
    void rejectsNullInFactoryForARelativeReferenceWithEmptyPathWithQuery() throws Exception {
        assertThrowsException("Null query should throw NullPointerException in factory", NullPointerException.class, () -> {
            Query<String> query = null;
            aScheme().relativeReference(query);
        });
    }

    @Test
    void aRelativeReferenceWithEmptyPathWithFragmentAsStringIsCorrect() {
        Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(fragment).asString(), equalTo("#" + fragment.asString()));
        assertThat(aScheme().relativeReference(fragment).asUri(), equalTo(URI.create("#" + fragment.asString())));
    }

    @Test
    void aRelativeReferenceWithEmptyPathWithFragmentAuthorityIsCorrect() throws Exception {
        Fragment<String> fragment = aFragment();
        final RelativeReference relativeReference = aScheme().relativeReference(fragment);
        assertThat(relativeReference.hasAuthority(), equalTo(false));
        assertThrowsException("Attempt to get authority from a UrinReference that does not have one.", UnsupportedOperationException.class, relativeReference::authority);

    }

    @Test
    void aRelativeReferenceWithEmptyPathWithFragmentPathIsCorrect() {
        Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(fragment).path(), equalTo(Path.<String>rootlessPath()));
    }

    @Test
    void aRelativeReferenceWithEmptyPathWithFragmentQueryIsCorrect() throws Exception {
        Fragment<String> fragment = aFragment();
        final RelativeReference relativeReference = aScheme().relativeReference(fragment);
        assertThat(relativeReference.hasQuery(), equalTo(false));
        assertThrowsException("Attempt to get query from a UrinReference that does not have one.", UnsupportedOperationException.class, relativeReference::query);
    }

    @Test
    void aRelativeReferenceWithEmptyPathWithFragmentFragmentIsCorrect() {
        Fragment<String> fragment = aFragment();
        RelativeReference<?, ?, Fragment<String>> relativeReference = aScheme().relativeReference(fragment);
        assertThat(relativeReference.hasFragment(), equalTo(true));
        assertThat(relativeReference.fragment(), equalTo(fragment));
    }

    @Test
    void aRelativeReferenceWithEmptyPathWithFragmentIsEqualToAnotherRelativeReferenceWithEmptyPath() {
        Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(fragment), equalTo(aScheme().relativeReference(fragment)));
        assertThat(aScheme().relativeReference(fragment).hashCode(), equalTo(aScheme().relativeReference(fragment).hashCode()));
    }

    @Test
    void aRelativeReferenceWithEmptyPathWithFragmentIsNotEqualToAnotherWithTheADifferentFragment() {
        assertThat(aScheme().relativeReference(aFragment()), not(equalTo(aScheme().relativeReference(aFragment()))));
    }

    @Test
    void aRelativeReferenceWithEmptyPathWithFragmentToStringIsCorrect() {
        Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(fragment).toString(), equalTo(aScheme().relativeReference(fragment).asString()));
    }

    @Test
    void aRelativeReferenceWithEmptyPathWithFragmentReplacedWithANewPathIsCorrect() {
        Fragment<String> fragment = aFragment();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(aScheme().relativeReference(fragment).withPath(newPath), equalTo(aScheme().relativeReference(newPath, fragment)));
    }

    @Test
    void rejectsNullInFactoryForARelativeReferenceWithEmptyPathWithFragment() throws Exception {
        assertThrowsException("Null fragment should throw NullPointerException in factory", NullPointerException.class, () -> {
            Fragment<String> fragment = null;
            aScheme().relativeReference(fragment);
        });
    }

    @Test
    void aRelativeReferenceWithEmptyPathWithQueryAndFragmentAsStringIsCorrect() {
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(query, fragment).asString(), equalTo("?" + query.asString() + "#" + fragment.asString()));
        assertThat(aScheme().relativeReference(query, fragment).asUri(), equalTo(URI.create("?" + query.asString() + "#" + fragment.asString())));
    }

    @Test
    void aRelativeReferenceWithEmptyPathWithQueryAndFragmentAuthorityIsCorrect() throws Exception {
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        final RelativeReference relativeReference = aScheme().relativeReference(query, fragment);
        assertThat(relativeReference.hasAuthority(), equalTo(false));
        assertThrowsException("Attempt to get authority from a UrinReference that does not have one.", UnsupportedOperationException.class, relativeReference::authority);

    }

    @Test
    void aRelativeReferenceWithEmptyPathWithQueryAndFragmentPathIsCorrect() {
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(query, fragment).path(), equalTo(Path.<String>rootlessPath()));
    }

    @Test
    void aRelativeReferenceWithEmptyPathWithQueryAndFragmentQueryIsCorrect() {
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        RelativeReference<String, Query<String>, Fragment<String>> relativeReference = aScheme().relativeReference(query, fragment);
        assertThat(relativeReference.hasQuery(), equalTo(true));
        assertThat(relativeReference.query(), equalTo(query));
    }

    @Test
    void aRelativeReferenceWithEmptyPathWithQueryAndFragmentFragmentIsCorrect() {
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        RelativeReference<?, ?, Fragment<String>> relativeReference = aScheme().relativeReference(query, fragment);
        assertThat(relativeReference.hasFragment(), equalTo(true));
        assertThat(relativeReference.fragment(), equalTo(fragment));
    }

    @Test
    void aRelativeReferenceWithEmptyPathWithQueryAndFragmentIsEqualToAnotherRelativeReferenceWithEmptyPath() {
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(query, fragment), equalTo(aScheme().relativeReference(query, fragment)));
        assertThat(aScheme().relativeReference(query, fragment).hashCode(), equalTo(aScheme().relativeReference(query, fragment).hashCode()));
    }

    @Test
    void aRelativeReferenceWithEmptyPathWithQueryAndFragmentIsNotEqualToAnotherWithTheADifferentQuery() {
        Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(aQuery(), fragment), not(equalTo(aScheme().relativeReference(aQuery(), fragment))));
    }

    @Test
    void aRelativeReferenceWithEmptyPathWithQueryAndFragmentIsNotEqualToAnotherWithTheADifferentFragment() {
        Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(query, aFragment()), not(equalTo(aScheme().relativeReference(query, aFragment()))));
    }

    @Test
    void aRelativeReferenceWithEmptyPathWithQueryAndFragmentToStringIsCorrect() {
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(query, fragment).toString(), equalTo(aScheme().relativeReference(query, fragment).asString()));
    }

    @Test
    void aRelativeReferenceWithEmptyPathWithQueryAndFragmentReplacedWithANewPathIsCorrect() {
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(aScheme().relativeReference(query, fragment).withPath(newPath), equalTo(aScheme().relativeReference(newPath, query, fragment)));
    }

    @Test
    void rejectsNullInFactoryForARelativeReferenceWithEmptyPathWithQueryAndFragment() throws Exception {
        assertThrowsException("Null query should throw NullPointerException in factory", NullPointerException.class, () -> {
            Query<String> query = null;
            aScheme().relativeReference(query, aFragment());
        });
        assertThrowsException("Null fragment should throw NullPointerException in factory", NullPointerException.class, () -> {
            Fragment<String> fragment = null;
            aScheme().relativeReference(aQuery(), fragment);
        });
    }

    @Test
    void aRelativeReferenceWithPathAsStringIsCorrect() {
        Path<String> path = aPath();
        assertThat(aScheme().relativeReference(path).asString(), equalTo(path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON)));
        assertThat(aScheme().relativeReference(path).asUri(), equalTo(URI.create(path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON))));
    }

    @Test
    void aRelativeReferenceWithPathAuthorityIsCorrect() throws Exception {
        Path<String> path = aPath();
        final RelativeReference relativeReference = aScheme().relativeReference(path);
        assertThat(relativeReference.hasAuthority(), equalTo(false));
        assertThrowsException("Attempt to get authority from a UrinReference that does not have one.", UnsupportedOperationException.class, relativeReference::authority);

    }

    @Test
    void aRelativeReferenceWithPathPathIsCorrect() {
        Path<String> path = aPath();
        assertThat(aScheme().relativeReference(path).path(), equalTo(path));
    }

    @Test
    void aRelativeReferenceWithPathQueryIsCorrect() throws Exception {
        Path<String> path = aPath();
        final RelativeReference relativeReference = aScheme().relativeReference(path);
        assertThat(relativeReference.hasQuery(), equalTo(false));
        assertThrowsException("Attempt to get query from a UrinReference that does not have one.", UnsupportedOperationException.class, relativeReference::query);
    }

    @Test
    void aRelativeReferenceWithPathFragmentIsCorrect() throws Exception {
        Path<String> path = aPath();
        final RelativeReference relativeReference = aScheme().relativeReference(path);
        assertThat(relativeReference.hasFragment(), equalTo(false));
        assertThrowsException("Attempt to get fragment from a UrinReference that does not have one.", UnsupportedOperationException.class, relativeReference::fragment);
    }

    @Test
    void aRelativeReferenceWithPathIsEqualToAnotherRelativeReferenceWithPathAndQuery() {
        Path<String> path = aPath();
        assertThat(aScheme().relativeReference(path), equalTo(aScheme().relativeReference(path)));
        assertThat(aScheme().relativeReference(path).hashCode(), equalTo(aScheme().relativeReference(path).hashCode()));
    }

    @Test
    void aRelativeReferenceWithPathIsNotEqualToAnotherWithTheADifferentPath() {
        assertThat(aScheme().relativeReference(aPath()), not(equalTo(aScheme().relativeReference(aPath()))));
    }

    @Test
    void aRelativeReferenceWithPathToStringIsCorrect() {
        Path<String> path = aPath();
        assertThat(aScheme().relativeReference(path).toString(), equalTo(aScheme().relativeReference(path).asString()));
    }

    @Test
    void aRelativeReferenceWithPathReplacedWithANewPathIsCorrect() {
        Path<String> path = aPath();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(aScheme().relativeReference(path).withPath(newPath), equalTo(aScheme().relativeReference(newPath)));
    }

    @Test
    void rejectsNullInFactoryForARelativeReferenceWithPath() throws Exception {
        assertThrowsException("Null path should throw NullPointerException in factory", NullPointerException.class, () -> aScheme().relativeReference((Path<String>) null));
    }

    @Test
    void aRelativeReferenceWithPathResolvesSchemeToTheBase() {
        Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        Authority baseAuthority = anAuthority();
        AbsolutePath<String> basePath = anAbsolutePath();
        Path<String> relativeReferencePath = aPath();
        assertThat(
                aScheme().relativeReference(relativeReferencePath).resolve(baseScheme, baseAuthority, basePath),
                equalTo(baseScheme.urin(baseAuthority, (AbsolutePath<String>) relativeReferencePath.resolveRelativeTo(basePath))));
    }

    @Test
    void aRelativeReferenceWithPathResolvesSchemeAndAuthorityToTheBase() {
        Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        Authority baseAuthority = anAuthority();
        AbsolutePath<String> basePath = anAbsolutePath();
        Query<String> baseQuery = aQuery();
        Path<String> relativeReferencePath = aPath();
        assertThat(
                aScheme().relativeReference(relativeReferencePath).resolve(baseScheme, baseAuthority, basePath, baseQuery),
                equalTo(baseScheme.urin(baseAuthority, (AbsolutePath<String>) relativeReferencePath.resolveRelativeTo(basePath))));
    }

    @Test
    void aRelativeReferenceWithPathResolvesSchemeAndAuthorityAndQueryAndFragmentToTheBase() {
        Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        Authority baseAuthority = anAuthority();
        AbsolutePath<String> basePath = anAbsolutePath();
        Query<String> baseQuery = aQuery();
        Fragment<String> baseFragment = aFragment();
        Path<String> relativeReferencePath = aPath();
        assertThat(
                aScheme().relativeReference(relativeReferencePath).resolve(baseScheme, baseAuthority, basePath, baseQuery, baseFragment),
                equalTo(baseScheme.urin(baseAuthority, (AbsolutePath<String>) relativeReferencePath.resolveRelativeTo(basePath))));
    }

    @Test
    void aRelativeReferenceWithPathAndQueryAsStringIsCorrect() {
        Path<String> path = aPath();
        Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(path, query).asString(), equalTo(path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON) + "?" + query.asString()));
        assertThat(aScheme().relativeReference(path, query).asUri(), equalTo(URI.create(path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON) + "?" + query.asString())));
    }

    @Test
    void aRelativeReferenceWithPathAndQueryAuthorityIsCorrect() throws Exception {
        final RelativeReference relativeReference = aScheme().relativeReference(aPath(), aQuery());
        assertThat(relativeReference.hasAuthority(), equalTo(false));
        assertThrowsException("Attempt to get authority from a UrinReference that does not have one.", UnsupportedOperationException.class, relativeReference::authority);

    }

    @Test
    void aRelativeReferenceWithPathAndQueryPathIsCorrect() {
        Path<String> path = aPath();
        Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(path, query).path(), equalTo(path));
    }

    @Test
    void aRelativeReferenceWithPathAndQueryQueryIsCorrect() {
        Path<String> path = aPath();
        Query<String> query = aQuery();
        final RelativeReference<String, Query<String>, Fragment<String>> relativeReference = aScheme().relativeReference(path, query);
        assertThat(relativeReference.hasQuery(), equalTo(true));
        assertThat(relativeReference.query(), equalTo(query));
    }

    @Test
    void aRelativeReferenceWithPathAndQueryFragmentIsCorrect() throws Exception {
        Path<String> path = aPath();
        Query<String> query = aQuery();
        final RelativeReference relativeReference = aScheme().relativeReference(path, query);
        assertThat(relativeReference.hasFragment(), equalTo(false));
        assertThrowsException("Attempt to get fragment from a UrinReference that does not have one.", UnsupportedOperationException.class, relativeReference::fragment);
    }

    @Test
    void aRelativeReferenceWithPathAndQueryIsEqualToAnotherRelativeReferenceWithPathAndQuery() {
        Path<String> path = aPath();
        Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(path, query), equalTo(aScheme().relativeReference(path, query)));
        assertThat(aScheme().relativeReference(path, query).hashCode(), equalTo(aScheme().relativeReference(path, query).hashCode()));
    }

    @Test
    void aRelativeReferenceWithPathAndQueryIsNotEqualToAnotherWithTheADifferentPath() {
        Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(aPath(), query), not(equalTo(aScheme().relativeReference(aPath(), query))));
    }

    @Test
    void aRelativeReferenceWithPathAndQueryIsNotEqualToAnotherWithTheADifferentQuery() {
        Path<String> path = aPath();
        assertThat(aScheme().relativeReference(path, aQuery()), not(equalTo(aScheme().relativeReference(path, aQuery()))));
    }

    @Test
    void aRelativeReferenceWithPathAndQueryToStringIsCorrect() {
        Path<String> path = aPath();
        Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(path, query).toString(), equalTo(aScheme().relativeReference(path, query).asString()));
    }

    @Test
    void aRelativeReferenceWithPathAndQueryReplacedWithANewPathIsCorrect() {
        Path<String> path = aPath();
        Query<String> query = aQuery();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(aScheme().relativeReference(path, query).withPath(newPath), equalTo(aScheme().relativeReference(newPath, query)));
    }

    @Test
    void rejectsNullInFactoryForARelativeReferenceWithPathAndQuery() throws Exception {
        assertThrowsException("Null path should throw NullPointerException in factory", NullPointerException.class, () -> aScheme().relativeReference((Path<String>) null, aQuery()));
        assertThrowsException("Null query should throw NullPointerException in factory", NullPointerException.class, () -> aScheme().relativeReference(aPath(), (Query<String>) null));
    }

    @Test
    void aRelativeReferenceWithPathAndQueryResolvesSchemeToTheBase() {
        Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        Authority baseAuthority = anAuthority();
        AbsolutePath<String> basePath = anAbsolutePath();
        Path<String> relativeReferencePath = aPath();
        Query<String> relativeReferenceQuery = aQuery();
        assertThat(
                aScheme().relativeReference(relativeReferencePath, relativeReferenceQuery).resolve(baseScheme, baseAuthority, basePath),
                equalTo(baseScheme.urin(baseAuthority, (AbsolutePath<String>) relativeReferencePath.resolveRelativeTo(basePath), relativeReferenceQuery)));
    }

    @Test
    void aRelativeReferenceWithPathAndQueryResolvesSchemeAndAuthorityAndQueryToTheBase() {
        Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        Authority baseAuthority = anAuthority();
        AbsolutePath<String> basePath = anAbsolutePath();
        Query<String> baseQuery = aQuery();
        Path<String> relativeReferencePath = aPath();
        Query<String> relativeReferenceQuery = aQuery();
        assertThat(
                aScheme().relativeReference(relativeReferencePath, relativeReferenceQuery).resolve(baseScheme, baseAuthority, basePath, baseQuery),
                equalTo(baseScheme.urin(baseAuthority, (AbsolutePath<String>) relativeReferencePath.resolveRelativeTo(basePath), relativeReferenceQuery)));
    }

    @Test
    void aRelativeReferenceWithPathAndQueryResolvesSchemeAndAuthorityAndQueryAndFragmentToTheBase() {
        Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        Authority baseAuthority = anAuthority();
        AbsolutePath<String> basePath = anAbsolutePath();
        Query<String> baseQuery = aQuery();
        Fragment<String> baseFragment = aFragment();
        Path<String> relativeReferencePath = aPath();
        Query<String> relativeReferenceQuery = aQuery();
        assertThat(
                aScheme().relativeReference(relativeReferencePath, relativeReferenceQuery).resolve(baseScheme, baseAuthority, basePath, baseQuery, baseFragment),
                equalTo(baseScheme.urin(baseAuthority, (AbsolutePath<String>) relativeReferencePath.resolveRelativeTo(basePath), relativeReferenceQuery)));
    }

    @Test
    void aRelativeReferenceWithPathAndFragmentAsStringIsCorrect() {
        Path<String> path = aPath();
        Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(path, fragment).asString(), equalTo(path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON) + "#" + fragment.asString()));
        assertThat(aScheme().relativeReference(path, fragment).asUri(), equalTo(URI.create(path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON) + "#" + fragment.asString())));
    }

    @Test
    void aRelativeReferenceWithPathAndFragmentAuthorityIsCorrect() throws Exception {
        final RelativeReference relativeReference = aScheme().relativeReference(aPath(), aFragment());
        assertThat(relativeReference.hasAuthority(), equalTo(false));
        assertThrowsException("Attempt to get authority from a UrinReference that does not have one.", UnsupportedOperationException.class, relativeReference::authority);
    }

    @Test
    void aRelativeReferenceWithPathAndFragmentPathIsCorrect() {
        Path<String> path = aPath();
        Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(path, fragment).path(), equalTo(path));
    }

    @Test
    void aRelativeReferenceWithPathAndFragmentQueryIsCorrect() throws Exception {
        Path<String> path = aPath();
        Fragment<String> fragment = aFragment();
        final RelativeReference relativeReference = aScheme().relativeReference(path, fragment);
        assertThat(relativeReference.hasQuery(), equalTo(false));
        assertThrowsException("Attempt to get query from a UrinReference that does not have one.", UnsupportedOperationException.class, relativeReference::query);
    }

    @Test
    void aRelativeReferenceWithPathAndFragmentFragmentIsCorrect() {
        Path<String> path = aPath();
        Fragment<String> fragment = aFragment();
        RelativeReference<?, ?, Fragment<String>> relativeReference = aScheme().relativeReference(path, fragment);
        assertThat(relativeReference.hasFragment(), equalTo(true));
        assertThat(relativeReference.fragment(), equalTo(fragment));
    }

    @Test
    void aRelativeReferenceWithPathAndFragmentIsEqualToAnotherRelativeReferenceWithPathAndFragment() {
        Path<String> path = aPath();
        Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(path, fragment), equalTo(aScheme().relativeReference(path, fragment)));
        assertThat(aScheme().relativeReference(path, fragment).hashCode(), equalTo(aScheme().relativeReference(path, fragment).hashCode()));
    }

    @Test
    void aRelativeReferenceWithPathAndFragmentIsNotEqualToAnotherWithTheADifferentPath() {
        Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(aPath(), fragment), not(equalTo(aScheme().relativeReference(aPath(), fragment))));
    }

    @Test
    void aRelativeReferenceWithPathAndFragmentIsNotEqualToAnotherWithTheADifferentFragment() {
        Path<String> path = aPath();
        assertThat(aScheme().relativeReference(path, aFragment()), not(equalTo(aScheme().relativeReference(path, aFragment()))));
    }

    @Test
    void aRelativeReferenceWithPathAndFragmentToStringIsCorrect() {
        Path<String> path = aPath();
        Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(path, fragment).toString(), equalTo(aScheme().relativeReference(path, fragment).asString()));
    }

    @Test
    void aRelativeReferenceWithPathAndFragmentReplacedWithANewPathIsCorrect() {
        Path<String> path = aPath();
        Fragment<String> fragment = aFragment();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(aScheme().relativeReference(path, fragment).withPath(newPath), equalTo(aScheme().relativeReference(newPath, fragment)));
    }

    @Test
    void rejectsNullInFactoryForARelativeReferenceWithPathAndFragment() throws Exception {
        assertThrowsException("Null path should throw NullPointerException in factory", NullPointerException.class, () -> aScheme().relativeReference((Path<String>) null, aFragment()));
        assertThrowsException("Null fragment should throw NullPointerException in factory", NullPointerException.class, () -> aScheme().relativeReference(aPath(), (Fragment<String>) null));
    }

    @Test
    void aRelativeReferenceWithPathAndFragmentResolvesSchemeToTheBase() {
        Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        Authority baseAuthority = anAuthority();
        AbsolutePath<String> basePath = anAbsolutePath();
        Path<String> relativeReferencePath = aPath();
        Fragment<String> relativeReferenceFragment = aFragment();
        assertThat(
                aScheme().relativeReference(relativeReferencePath, relativeReferenceFragment).resolve(baseScheme, baseAuthority, basePath),
                equalTo(baseScheme.urin(baseAuthority, (AbsolutePath<String>) relativeReferencePath.resolveRelativeTo(basePath), relativeReferenceFragment)));
    }

    @Test
    void aRelativeReferenceWithPathAndFragmentResolvesSchemeAndAuthorityAndQueryToTheBase() {
        Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        Authority baseAuthority = anAuthority();
        AbsolutePath<String> basePath = anAbsolutePath();
        Query<String> baseQuery = aQuery();
        Path<String> relativeReferencePath = aPath();
        Fragment<String> relativeReferenceFragment = aFragment();
        assertThat(
                aScheme().relativeReference(relativeReferencePath, relativeReferenceFragment).resolve(baseScheme, baseAuthority, basePath, baseQuery),
                equalTo(baseScheme.urin(baseAuthority, (AbsolutePath<String>) relativeReferencePath.resolveRelativeTo(basePath), relativeReferenceFragment)));
    }

    @Test
    void aRelativeReferenceWithPathAndFragmentResolvesSchemeAndAuthorityAndQueryAndFragmentToTheBase() {
        Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        Authority baseAuthority = anAuthority();
        AbsolutePath<String> basePath = anAbsolutePath();
        Query<String> baseQuery = aQuery();
        Fragment<String> baseFragment = aFragment();
        Path<String> relativeReferencePath = aPath();
        Fragment<String> relativeReferenceFragment = aFragment();
        assertThat(
                aScheme().relativeReference(relativeReferencePath, relativeReferenceFragment).resolve(baseScheme, baseAuthority, basePath, baseQuery, baseFragment),
                equalTo(baseScheme.urin(baseAuthority, (AbsolutePath<String>) relativeReferencePath.resolveRelativeTo(basePath), relativeReferenceFragment)));
    }

    @Test
    void aRelativeReferenceWithPathAndQueryAndFragmentAsStringIsCorrect() {
        Path<String> path = aPath();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(path, query, fragment).asString(), equalTo(path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON) + "?" + query.asString() + "#" + fragment.asString()));
        assertThat(aScheme().relativeReference(path, query, fragment).asUri(), equalTo(URI.create(path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON) + "?" + query.asString() + "#" + fragment.asString())));
    }

    @Test
    void aRelativeReferenceWithPathAndQueryAndFragmentAuthorityIsCorrect() throws Exception {
        final RelativeReference relativeReference = aScheme().relativeReference(aPath(), aQuery(), aFragment());
        assertThat(relativeReference.hasAuthority(), equalTo(false));
        assertThrowsException("Attempt to get authority from a UrinReference that does not have one.", UnsupportedOperationException.class, relativeReference::authority);

    }

    @Test
    void aRelativeReferenceWithPathAndQueryAndFragmentPathIsCorrect() {
        Path<String> path = aPath();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(path, query, fragment).path(), equalTo(path));
    }

    @Test
    void aRelativeReferenceWithPathAndQueryAndFragmentQueryIsCorrect() {
        Path<String> path = aPath();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        RelativeReference<String, Query<String>, Fragment<String>> relativeReference = aScheme().relativeReference(path, query, fragment);
        assertThat(relativeReference.hasQuery(), equalTo(true));
        assertThat(relativeReference.query(), equalTo(query));
    }

    @Test
    void aRelativeReferenceWithPathAndQueryAndFragmentFragmentIsCorrect() {
        Path<String> path = aPath();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        RelativeReference<?, ?, Fragment<String>> relativeReference = aScheme().relativeReference(path, query, fragment);
        assertThat(relativeReference.hasFragment(), equalTo(true));
        assertThat(relativeReference.fragment(), equalTo(fragment));
    }

    @Test
    void aRelativeReferenceWithPathAndQueryAndFragmentIsEqualToAnotherRelativeReferenceWithPathAndFragment() {
        Path<String> path = aPath();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(path, query, fragment), equalTo(aScheme().relativeReference(path, query, fragment)));
        assertThat(aScheme().relativeReference(path, query, fragment).hashCode(), equalTo(aScheme().relativeReference(path, query, fragment).hashCode()));
    }

    @Test
    void aRelativeReferenceWithPathAndQueryAndFragmentIsNotEqualToAnotherWithTheADifferentPath() {
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(aPath(), query, fragment), not(equalTo(aScheme().relativeReference(aPath(), query, fragment))));
    }

    @Test
    void aRelativeReferenceWithPathAndQueryAndFragmentIsNotEqualToAnotherWithTheADifferentQuery() {
        Path<String> path = aPath();
        Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(path, aQuery(), fragment), not(equalTo(aScheme().relativeReference(path, aQuery(), fragment))));
    }

    @Test
    void aRelativeReferenceWithPathAndQueryAndFragmentIsNotEqualToAnotherWithTheADifferentFragment() {
        Path<String> path = aPath();
        Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(path, query, aFragment()), not(equalTo(aScheme().relativeReference(path, query, aFragment()))));
    }

    @Test
    void aRelativeReferenceWithPathAndQueryAndFragmentToStringIsCorrect() {
        Path<String> path = aPath();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(path, query, fragment).toString(), equalTo(aScheme().relativeReference(path, query, fragment).asString()));
    }

    @Test
    void aRelativeReferenceWithPathAndQueryAndFragmentReplacedWithANewPathIsCorrect() {
        Path<String> path = aPath();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(aScheme().relativeReference(path, query, fragment).withPath(newPath), equalTo(aScheme().relativeReference(newPath, query, fragment)));
    }

    @Test
    void rejectsNullInFactoryForARelativeReferenceWithPathAndQueryAndFragment() throws Exception {
        assertThrowsException("Null path should throw NullPointerException in factory", NullPointerException.class, () -> aScheme().relativeReference((Path<String>) null, aQuery(), aFragment()));
        assertThrowsException("Null query should throw NullPointerException in factory", NullPointerException.class, () -> aScheme().relativeReference(aPath(), null, aFragment()));
        assertThrowsException("Null fragment should throw NullPointerException in factory", NullPointerException.class, () -> aScheme().relativeReference(aPath(), aQuery(), null));
    }

    @Test
    void aRelativeReferenceWithPathAndQueryAndFragmentResolvesSchemeToTheBase() {
        Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        Authority baseAuthority = anAuthority();
        AbsolutePath<String> basePath = anAbsolutePath();
        Path<String> relativeReferencePath = aPath();
        Query<String> relativeReferenceQuery = aQuery();
        Fragment<String> relativeReferenceFragment = aFragment();
        assertThat(
                aScheme().relativeReference(relativeReferencePath, relativeReferenceQuery, relativeReferenceFragment).resolve(baseScheme, baseAuthority, basePath),
                equalTo(baseScheme.urin(baseAuthority, (AbsolutePath<String>) relativeReferencePath.resolveRelativeTo(basePath), relativeReferenceQuery, relativeReferenceFragment)));
    }

    @Test
    void aRelativeReferenceWithPathAndQueryAndFragmentResolvesSchemeAndAuthorityAndQueryToTheBase() {
        Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        Authority baseAuthority = anAuthority();
        AbsolutePath<String> basePath = anAbsolutePath();
        Query<String> baseQuery = aQuery();
        Path<String> relativeReferencePath = aPath();
        Query<String> relativeReferenceQuery = aQuery();
        Fragment<String> relativeReferenceFragment = aFragment();
        assertThat(
                aScheme().relativeReference(relativeReferencePath, relativeReferenceQuery, relativeReferenceFragment).resolve(baseScheme, baseAuthority, basePath, baseQuery),
                equalTo(baseScheme.urin(baseAuthority, (AbsolutePath<String>) relativeReferencePath.resolveRelativeTo(basePath), relativeReferenceQuery, relativeReferenceFragment)));
    }

    @Test
    void aRelativeReferenceWithPathAndQueryAndFragmentResolvesSchemeAndAuthorityAndQueryAndFragmentToTheBase() {
        Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        Authority baseAuthority = anAuthority();
        AbsolutePath<String> basePath = anAbsolutePath();
        Query<String> baseQuery = aQuery();
        Fragment<String> baseFragment = aFragment();
        Path<String> relativeReferencePath = aPath();
        Query<String> relativeReferenceQuery = aQuery();
        Fragment<String> relativeReferenceFragment = aFragment();
        assertThat(
                aScheme().relativeReference(relativeReferencePath, relativeReferenceQuery, relativeReferenceFragment).resolve(baseScheme, baseAuthority, basePath, baseQuery, baseFragment),
                equalTo(baseScheme.urin(baseAuthority, (AbsolutePath<String>) relativeReferencePath.resolveRelativeTo(basePath), relativeReferenceQuery, relativeReferenceFragment)));
    }

    @Test
    void aSimpleAbsolutePathAsStringReturnsThePath() {
        Segment<String> firstSegment = aNonDotSegment();
        Segment<String> secondSegment = aNonDotSegment();
        assertThat(aScheme().relativeReference(Path.path(firstSegment, secondSegment)).asString(), equalTo("/" + firstSegment.asString() + "/" + secondSegment.asString()));
        assertThat(aScheme().relativeReference(Path.path(firstSegment, secondSegment)).asUri(), equalTo(URI.create("/" + firstSegment.asString() + "/" + secondSegment.asString())));
    }

    @Test
    void aSimpleAbsolutePathPrefixesAnEmptyFirstSegmentWithADotSegment() {
        Segment<String> firstSegment = segment("");
        Segment<String> secondSegment = aNonDotSegment();
        assertThat(aScheme().relativeReference(Path.path(firstSegment, secondSegment)).asString(), equalTo("/./" + firstSegment.asString() + "/" + secondSegment.asString()));
    }

    @Test
    void rejectsNullInFactoryForASimplePath() throws Exception {
        assertThrowsException("Null first segment should throw NullPointerException in factory", NullPointerException.class, () -> {
            Path<String> path = null;
            aScheme().relativeReference(path);
        });
    }

    @Test
    void aSimpleAbsolutePathIsEqualToAnotherWithTheSamePath() {
        Path<String> path = aPath();
        assertThat(aScheme().relativeReference(path), equalTo(aScheme().relativeReference(path)));
        assertThat(aScheme().relativeReference(path).hashCode(), equalTo(aScheme().relativeReference(path).hashCode()));
    }

    @Test
    void aSimpleAbsolutePathIsNotEqualToAnotherWithTheADifferentPath() {
        assertThat(aScheme().relativeReference(aPath()), not(equalTo(aScheme().relativeReference(aPath()))));
    }

    @Test
    void aSimpleAbsolutePathAuthorityIsCorrect() throws Exception {
        final RelativeReference relativeReference = aScheme().relativeReference(aPath());
        assertThat(relativeReference.hasAuthority(), equalTo(false));
        assertThrowsException("Attempt to get authority from a UrinReference that does not have one.", UnsupportedOperationException.class, relativeReference::authority);

    }

    @Test
    void aSimpleAbsolutePathPathIsCorrect() {
        Path<String> path = aPath();
        assertThat(aScheme().relativeReference(path).path(), equalTo(path));
    }

    @Test
    void aSimpleAbsolutePathQueryIsCorrect() throws Exception {
        Path<String> path = aPath();
        final RelativeReference relativeReference = aScheme().relativeReference(path);
        assertThat(relativeReference.hasQuery(), equalTo(false));
        assertThrowsException("Attempt to get query from a UrinReference that does not have one.", UnsupportedOperationException.class, relativeReference::query);
    }

    @Test
    void aSimpleAbsolutePathFragmentIsCorrect() throws Exception {
        Path<String> path = aPath();
        final RelativeReference relativeReference = aScheme().relativeReference(path);
        assertThat(relativeReference.hasFragment(), equalTo(false));
        assertThrowsException("Attempt to get fragment from a UrinReference that does not have one.", UnsupportedOperationException.class, relativeReference::fragment);
    }

    @Test
    void aSimpleAbsolutePathToStringIsCorrect() {
        Path<String> path = aPath();
        assertThat(aScheme().relativeReference(path).toString(), equalTo(aScheme().relativeReference(path).asString()));
    }

    @Test
    void aSimpleAbsolutePathReplacedWithANewPathIsCorrect() {
        Path<String> path = aPath();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(aScheme().relativeReference(path).withPath(newPath), equalTo(aScheme().relativeReference(newPath)));
    }

    @Test
    void aSimpleRootlessPathAsStringReturnsThePath() {
        Segment<String> firstSegment = aNonDotSegment();
        Segment<String> secondSegment = aNonDotSegment();
        assertThat(aScheme().relativeReference(rootlessPath(firstSegment, secondSegment)).asString(), equalTo(firstSegment.asString() + "/" + secondSegment.asString()));
        assertThat(aScheme().relativeReference(rootlessPath(firstSegment, secondSegment)).asUri(), equalTo(URI.create(firstSegment.asString() + "/" + secondSegment.asString())));
    }

    @Test
    void aSimpleRootlessPathRejectsAnEmptyFirstSegment() {
        Segment<String> firstSegment = segment("");
        Segment<String> secondSegment = aNonDotSegment();
        assertThat(aScheme().relativeReference(rootlessPath(firstSegment, secondSegment)).asString(), equalTo("./" + firstSegment.asString() + "/" + secondSegment.asString()));
    }

    @Test
    void aSimpleRootlessPathPrependsAColonInFirstSegmentWithDotSlash() {
        Segment<String> firstSegment = segment(aStringIncluding(':'));
        Segment<String> secondSegment = aSegment();
        assertThat(aScheme().relativeReference(rootlessPath(firstSegment, secondSegment)), equalTo(aScheme().relativeReference(rootlessPath(Segment.dot(), firstSegment, secondSegment))));
    }

    @Test
    void aSimpleRootlessPathPermitsAColonInTrailingSegments() {
        aScheme().relativeReference(rootlessPath(aSegment(), segment(aStringIncluding(':')))).asString();
    }

    @Test
    void aSimpleRootlessPathIsEqualToAnotherWithTheSamePath() {
        Path<String> path = aPath();
        assertThat(aScheme().relativeReference(path), equalTo(aScheme().relativeReference(path)));
        assertThat(aScheme().relativeReference(path).hashCode(), equalTo(aScheme().relativeReference(path).hashCode()));
    }

    @Test
    void aSimpleRootlessPathIsNotEqualToAnotherWithTheADifferentPath() {
        assertThat(aScheme().relativeReference(aPath()), not(equalTo(aScheme().relativeReference(aPath()))));
    }

    @Test
    void aSimpleRootlessPathAuthorityIsCorrect() throws Exception {
        final RelativeReference relativeReference = aScheme().relativeReference(aPath());
        assertThat(relativeReference.hasAuthority(), equalTo(false));
        assertThrowsException("Attempt to get authority from a UrinReference that does not have one.", UnsupportedOperationException.class, relativeReference::authority);
    }

    @Test
    void aSimpleRootlessPathPathIsCorrect() {
        Path<String> path = aPath();
        assertThat(aScheme().relativeReference(path).path(), equalTo(path));
    }

    @Test
    void aSimpleRootlessPathQueryIsCorrect() throws Exception {
        final RelativeReference relativeReference = aScheme().relativeReference(aPath());
        assertThat(relativeReference.hasQuery(), equalTo(false));
        assertThrowsException("Attempt to get query from a UrinReference that does not have one.", UnsupportedOperationException.class, relativeReference::query);
    }

    @Test
    void aSimpleRootlessPathFragmentIsCorrect() throws Exception {
        final RelativeReference relativeReference = aScheme().relativeReference(aPath());
        assertThat(relativeReference.hasFragment(), equalTo(false));
        assertThrowsException("Attempt to get fragment from a UrinReference that does not have one.", UnsupportedOperationException.class, relativeReference::fragment);
    }

    @Test
    void aSimpleRootlessPathToStringIsCorrect() {
        Path<String> path = aPath();
        assertThat(aScheme().relativeReference(path).toString(), equalTo(aScheme().relativeReference(path).asString()));
    }

    @Test
    void aSimpleRootlessPathReplacedWithANewPathIsCorrect() {
        Path<String> path = aPath();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(aScheme().relativeReference(path).withPath(newPath), equalTo(aScheme().relativeReference(newPath)));
    }

    @Test
    void aRelativeReferenceWithOnlyPathResolvesSchemeAndAuthorityToTheBase() {
        Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        Authority baseAuthority = anAuthority();
        AbsolutePath<String> basePath = anAbsolutePath();
        Path<String> relativeReferencePath = aPath();
        assertThat(
                aScheme().relativeReference(relativeReferencePath).resolve(baseScheme, baseAuthority, basePath),
                equalTo(baseScheme.urin(baseAuthority, (AbsolutePath<String>) relativeReferencePath.resolveRelativeTo(basePath))));
    }

    @Test
    void aRelativeReferenceWithOnlyPathResolvesSchemeAndAuthorityAndQueryToTheBase() {
        Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        Authority baseAuthority = anAuthority();
        AbsolutePath<String> basePath = anAbsolutePath();
        Path<String> relativeReferencePath = aPath();
        Query<String> baseQuery = aQuery();
        assertThat(
                aScheme().relativeReference(relativeReferencePath).resolve(baseScheme, baseAuthority, basePath, baseQuery),
                equalTo(baseScheme.urin(baseAuthority, (AbsolutePath<String>) relativeReferencePath.resolveRelativeTo(basePath))));
    }

    @Test
    void aRelativeReferenceWithOnlyPathResolvesSchemeAndAuthorityAndQueryAndFragmentToTheBase() {
        Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        Authority baseAuthority = anAuthority();
        AbsolutePath<String> basePath = anAbsolutePath();
        Path<String> relativeReferencePath = aPath();
        Query<String> baseQuery = aQuery();
        Fragment<String> baseFragment = aFragment();
        assertThat(
                aScheme().relativeReference(relativeReferencePath).resolve(baseScheme, baseAuthority, basePath, baseQuery, baseFragment),
                equalTo(baseScheme.urin(baseAuthority, (AbsolutePath<String>) relativeReferencePath.resolveRelativeTo(basePath))));
    }

    @Test
    void makesRelativeReferenceWithAuthorityAndEmptyPath() {
        Authority authority = anAuthority();
        assertThat(aScheme().relativeReference(authority).asString(), equalTo("//" + authority.asString()));
    }

    @Test
    void rejectsNullInFactoryForRelativeReferenceWithAuthorityAndEmptyPath() throws Exception {
        assertThrowsException("Null authority should throw NullPointerException in factory", NullPointerException.class, () -> aScheme().relativeReference((Authority) null));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndEmptyPathIsEqualToAnotherWithTheSameAuthority() {
        Authority authority = anAuthority();
        assertThat(aScheme().relativeReference(authority), equalTo(aScheme().relativeReference(authority)));
        assertThat(aScheme().relativeReference(authority).hashCode(), equalTo(aScheme().relativeReference(authority).hashCode()));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndEmptyPathIsNotEqualToAnotherWithTheADifferentAuthority() {
        assertThat(aScheme().relativeReference(anAuthority()), not(equalTo(aScheme().relativeReference(anAuthority()))));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndEmptyPathAuthorityIsCorrect() {
        Authority authority = anAuthority();
        final RelativeReference relativeReference = aScheme().relativeReference(authority);
        assertThat(relativeReference.hasAuthority(), equalTo(true));
        assertThat(relativeReference.authority(), equalTo(authority));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndEmptyPathPathIsCorrect() {
        Authority authority = anAuthority();
        assertThat(aScheme().relativeReference(authority).path(), equalTo(Path.<String>rootlessPath()));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndEmptyPathQueryIsCorrect() throws Exception {
        Authority authority = anAuthority();
        final RelativeReference relativeReference = aScheme().relativeReference(authority);
        assertThat(relativeReference.hasQuery(), equalTo(false));
        assertThrowsException("Attempt to get query from a UrinReference that does not have one.", UnsupportedOperationException.class, relativeReference::query);
    }

    @Test
    void aRelativeReferenceWithAuthorityAndEmptyPathFragmentIsCorrect() throws Exception {
        Authority authority = anAuthority();
        final RelativeReference relativeReference = aScheme().relativeReference(authority);
        assertThat(relativeReference.hasFragment(), equalTo(false));
        assertThrowsException("Attempt to get fragment from a UrinReference that does not have one.", UnsupportedOperationException.class, relativeReference::fragment);
    }

    @Test
    void aRelativeReferenceWithAuthorityAndEmptyPathToStringIsCorrect() {
        Authority authority = anAuthority();
        assertThat(aScheme().relativeReference(authority).toString(), equalTo(aScheme().relativeReference(authority).asString()));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndEmptyPathReplacedWithANewPathIsCorrect() {
        Authority authority = anAuthority();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(aScheme().relativeReference(authority).withPath(newPath), equalTo(aScheme().relativeReference(authority, newPath)));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndEmptyPathResolvesSchemeToTheBase() {
        Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        Authority baseAuthority = anAuthority();
        AbsolutePath<String> basePath = anAbsolutePath();
        Authority relativeReferenceAuthority = anAuthority();
        assertThat(
                aScheme().relativeReference(relativeReferenceAuthority).resolve(baseScheme, baseAuthority, basePath),
                equalTo(baseScheme.urin(relativeReferenceAuthority)));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndEmptyPathResolvesSchemeAndAuthorityAndQueryToTheBase() {
        Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        Authority baseAuthority = anAuthority();
        AbsolutePath<String> basePath = anAbsolutePath();
        Query<String> baseQuery = aQuery();
        Authority relativeReferenceAuthority = anAuthority();
        assertThat(
                aScheme().relativeReference(relativeReferenceAuthority).resolve(baseScheme, baseAuthority, basePath, baseQuery),
                equalTo(baseScheme.urin(relativeReferenceAuthority)));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndEmptyPathResolvesSchemeAndAuthorityAndQueryAndFragmentToTheBase() {
        Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        Authority baseAuthority = anAuthority();
        AbsolutePath<String> basePath = anAbsolutePath();
        Query<String> baseQuery = aQuery();
        Fragment<String> baseFragment = aFragment();
        Authority relativeReferenceAuthority = anAuthority();
        assertThat(
                aScheme().relativeReference(relativeReferenceAuthority).resolve(baseScheme, baseAuthority, basePath, baseQuery, baseFragment),
                equalTo(baseScheme.urin(relativeReferenceAuthority)));
    }

    @Test
    void makesRelativeReferenceWithAuthorityAndQuery() {
        Authority authority = anAuthority();
        Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(authority, query).asString(), equalTo("//" + authority.asString() + "?" + query.asString()));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndQueryIsEqualToAnotherWithTheSameAuthorityAndQuery() {
        Authority authority = anAuthority();
        Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(authority, query), equalTo(aScheme().relativeReference(authority, query)));
        assertThat(aScheme().relativeReference(authority, query).hashCode(), equalTo(aScheme().relativeReference(authority, query).hashCode()));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndQueryIsNotEqualToAnotherWithTheADifferentAuthority() {
        Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(anAuthority(), query), not(equalTo(aScheme().relativeReference(anAuthority(), query))));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndQueryIsNotEqualToAnotherWithTheADifferentQuery() {
        Authority authority = anAuthority();
        assertThat(aScheme().relativeReference(authority, aQuery()), not(equalTo(aScheme().relativeReference(authority, aQuery()))));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndQueryAuthorityIsCorrect() {
        Authority authority = anAuthority();
        Query<String> query = aQuery();
        final RelativeReference relativeReference = aScheme().relativeReference(authority, query);
        assertThat(relativeReference.hasAuthority(), equalTo(true));
        assertThat(relativeReference.authority(), equalTo(authority));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndQueryPathIsCorrect() {
        Authority authority = anAuthority();
        Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(authority, query).path(), equalTo(Path.<String>rootlessPath()));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndQueryQueryIsCorrect() {
        Authority authority = anAuthority();
        Query<String> query = aQuery();
        final RelativeReference<String, Query<String>, Fragment<String>> relativeReference = aScheme().relativeReference(authority, query);
        assertThat(relativeReference.hasQuery(), equalTo(true));
        assertThat(relativeReference.query(), equalTo(query));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndQueryFragmentIsCorrect() throws Exception {
        Authority authority = anAuthority();
        Query<String> query = aQuery();
        final RelativeReference relativeReference = aScheme().relativeReference(authority, query);
        assertThat(relativeReference.hasFragment(), equalTo(false));
        assertThrowsException("Attempt to get fragment from a UrinReference that does not have one.", UnsupportedOperationException.class, relativeReference::fragment);
    }

    @Test
    void aRelativeReferenceWithAuthorityAndQueryToStringIsCorrect() {
        Authority authority = anAuthority();
        Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(authority, query).toString(), equalTo(aScheme().relativeReference(authority, query).asString()));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndQueryReplacedWithANewPathIsCorrect() {
        Authority authority = anAuthority();
        Query<String> query = aQuery();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(aScheme().relativeReference(authority, query).withPath(newPath), equalTo(aScheme().relativeReference(authority, newPath, query)));
    }

    @Test
    void rejectsNullInFactoryForRelativeReferenceWithAuthorityAndQuery() throws Exception {
        assertThrowsException("Null authority should throw NullPointerException in factory", NullPointerException.class, () -> aScheme().relativeReference((Authority) null, aQuery()));
        assertThrowsException("Null query should throw NullPointerException in factory", NullPointerException.class, () -> aScheme().relativeReference(anAuthority(), (Query<String>) null));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndQueryResolvesSchemeToTheBase() {
        Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        Authority baseAuthority = anAuthority();
        AbsolutePath<String> basePath = anAbsolutePath();
        Authority relativeReferenceAuthority = anAuthority();
        Query<String> relativeReferenceQuery = aQuery();
        assertThat(
                aScheme().relativeReference(relativeReferenceAuthority, relativeReferenceQuery).resolve(baseScheme, baseAuthority, basePath),
                equalTo(baseScheme.urin(relativeReferenceAuthority, relativeReferenceQuery)));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndQueryResolvesSchemeAndAuthorityToTheBase() {
        Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        Authority baseAuthority = anAuthority();
        AbsolutePath<String> basePath = anAbsolutePath();
        Query<String> baseQuery = aQuery();
        Authority relativeReferenceAuthority = anAuthority();
        Query<String> relativeReferenceQuery = aQuery();
        assertThat(
                aScheme().relativeReference(relativeReferenceAuthority, relativeReferenceQuery).resolve(baseScheme, baseAuthority, basePath, baseQuery),
                equalTo(baseScheme.urin(relativeReferenceAuthority, relativeReferenceQuery)));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndQueryResolvesSchemeAndAuthorityAndQueryAndFragmentToTheBase() {
        Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        Authority baseAuthority = anAuthority();
        AbsolutePath<String> basePath = anAbsolutePath();
        Query<String> baseQuery = aQuery();
        Fragment<String> baseFragment = aFragment();
        Authority relativeReferenceAuthority = anAuthority();
        Query<String> relativeReferenceQuery = aQuery();
        assertThat(
                aScheme().relativeReference(relativeReferenceAuthority, relativeReferenceQuery).resolve(baseScheme, baseAuthority, basePath, baseQuery, baseFragment),
                equalTo(baseScheme.urin(relativeReferenceAuthority, relativeReferenceQuery)));
    }

    @Test
    void makesRelativeReferenceWithAuthorityAndFragment() {
        Authority authority = anAuthority();
        Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(authority, fragment).asString(), equalTo("//" + authority.asString() + "#" + fragment.asString()));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndFragmentIsEqualToAnotherWithTheSameAuthorityAndFragment() {
        Authority authority = anAuthority();
        Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(authority, fragment), equalTo(aScheme().relativeReference(authority, fragment)));
        assertThat(aScheme().relativeReference(authority, fragment).hashCode(), equalTo(aScheme().relativeReference(authority, fragment).hashCode()));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndFragmentIsNotEqualToAnotherWithTheADifferentAuthority() {
        Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(anAuthority(), fragment), not(equalTo(aScheme().relativeReference(anAuthority(), fragment))));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndFragmentIsNotEqualToAnotherWithTheADifferentFragment() {
        Authority authority = anAuthority();
        assertThat(aScheme().relativeReference(authority, aFragment()), not(equalTo(aScheme().relativeReference(authority, aFragment()))));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndFragmentAuthorityIsCorrect() {
        Authority authority = anAuthority();
        Fragment<String> fragment = aFragment();
        final RelativeReference relativeReference = aScheme().relativeReference(authority, fragment);
        assertThat(relativeReference.hasAuthority(), equalTo(true));
        assertThat(relativeReference.authority(), equalTo(authority));

    }

    @Test
    void aRelativeReferenceWithAuthorityAndFragmentPathIsCorrect() {
        Authority authority = anAuthority();
        Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(authority, fragment).path(), equalTo(Path.<String>rootlessPath()));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndFragmentQueryIsCorrect() throws Exception {
        Authority authority = anAuthority();
        Fragment<String> fragment = aFragment();
        final RelativeReference relativeReference = aScheme().relativeReference(authority, fragment);
        assertThat(relativeReference.hasQuery(), equalTo(false));
        assertThrowsException("Attempt to get query from a UrinReference that does not have one.", UnsupportedOperationException.class, relativeReference::query);
    }

    @Test
    void aRelativeReferenceWithAuthorityAndFragmentFragmentIsCorrect() {
        Authority authority = anAuthority();
        Fragment<String> fragment = aFragment();
        RelativeReference<?, ?, Fragment<String>> relativeReference = aScheme().relativeReference(authority, fragment);
        assertThat(relativeReference.hasFragment(), equalTo(true));
        assertThat(relativeReference.fragment(), equalTo(fragment));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndFragmentToStringIsCorrect() {
        Authority authority = anAuthority();
        Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(authority, fragment).toString(), equalTo(aScheme().relativeReference(authority, fragment).asString()));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndFragmentReplacedWithANewPathIsCorrect() {
        Authority authority = anAuthority();
        Fragment<String> fragment = aFragment();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(aScheme().relativeReference(authority, fragment).withPath(newPath), equalTo(aScheme().relativeReference(authority, newPath, fragment)));
    }

    @Test
    void rejectsNullInFactoryForRelativeReferenceWithAuthorityAndFragment() throws Exception {
        assertThrowsException("Null authority should throw NullPointerException in factory", NullPointerException.class, () -> aScheme().relativeReference((Authority) null, aFragment()));
        assertThrowsException("Null fragment should throw NullPointerException in factory", NullPointerException.class, () -> aScheme().relativeReference(anAuthority(), (Fragment<String>) null));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndFragmentResolvesSchemeToTheBase() {
        Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        Authority baseAuthority = anAuthority();
        AbsolutePath<String> basePath = anAbsolutePath();
        Authority relativeReferenceAuthority = anAuthority();
        Fragment<String> relativeReferenceFragment = aFragment();
        assertThat(
                aScheme().relativeReference(relativeReferenceAuthority, relativeReferenceFragment).resolve(baseScheme, baseAuthority, basePath),
                equalTo(baseScheme.urin(relativeReferenceAuthority, relativeReferenceFragment)));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndFragmentResolvesSchemeAndAuthorityAndQueryToTheBase() {
        Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        Authority baseAuthority = anAuthority();
        AbsolutePath<String> basePath = anAbsolutePath();
        Query<String> baseQuery = aQuery();
        Authority relativeReferenceAuthority = anAuthority();
        Fragment<String> relativeReferenceFragment = aFragment();
        assertThat(
                aScheme().relativeReference(relativeReferenceAuthority, relativeReferenceFragment).resolve(baseScheme, baseAuthority, basePath, baseQuery),
                equalTo(baseScheme.urin(relativeReferenceAuthority, relativeReferenceFragment)));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndFragmentResolvesSchemeAndAuthorityAndQueryAndFragmentToTheBase() {
        Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        Authority baseAuthority = anAuthority();
        AbsolutePath<String> basePath = anAbsolutePath();
        Query<String> baseQuery = aQuery();
        Fragment<String> baseFragment = aFragment();
        Authority relativeReferenceAuthority = anAuthority();
        Fragment<String> relativeReferenceFragment = aFragment();
        assertThat(
                aScheme().relativeReference(relativeReferenceAuthority, relativeReferenceFragment).resolve(baseScheme, baseAuthority, basePath, baseQuery, baseFragment),
                equalTo(baseScheme.urin(relativeReferenceAuthority, relativeReferenceFragment)));
    }

    @Test
    void makesRelativeReferenceWithAuthorityAndQueryAndFragment() {
        Authority authority = anAuthority();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(authority, query, fragment).asString(), equalTo("//" + authority.asString() + "?" + query.asString() + "#" + fragment.asString()));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndQueryAndFragmentIsEqualToAnotherWithTheSameAuthorityAndQueryAndFragment() {
        Authority authority = anAuthority();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(authority, query, fragment), equalTo(aScheme().relativeReference(authority, query, fragment)));
        assertThat(aScheme().relativeReference(authority, query, fragment).hashCode(), equalTo(aScheme().relativeReference(authority, query, fragment).hashCode()));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndQueryAndFragmentIsNotEqualToAnotherWithTheADifferentAuthority() {
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(anAuthority(), query, fragment), not(equalTo(aScheme().relativeReference(anAuthority(), query, fragment))));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndQueryAndFragmentIsNotEqualToAnotherWithTheADifferentQuery() {
        Authority authority = anAuthority();
        Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(authority, aQuery(), fragment), not(equalTo(aScheme().relativeReference(authority, aQuery(), fragment))));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndQueryAndFragmentIsNotEqualToAnotherWithTheADifferentFragment() {
        Authority authority = anAuthority();
        Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(authority, query, aFragment()), not(equalTo(aScheme().relativeReference(authority, query, aFragment()))));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndQueryAndFragmentAuthorityIsCorrect() {
        Authority authority = anAuthority();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        final RelativeReference relativeReference = aScheme().relativeReference(authority, query, fragment);
        assertThat(relativeReference.hasAuthority(), equalTo(true));
        assertThat(relativeReference.authority(), equalTo(authority));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndQueryAndFragmentPathIsCorrect() {
        Authority authority = anAuthority();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(authority, query, fragment).path(), equalTo(Path.<String>rootlessPath()));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndQueryAndFragmentQueryIsCorrect() {
        Authority authority = anAuthority();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        RelativeReference<String, Query<String>, Fragment<String>> relativeReference = aScheme().relativeReference(authority, query, fragment);
        assertThat(relativeReference.hasQuery(), equalTo(true));
        assertThat(relativeReference.query(), equalTo(query));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndQueryAndFragmentFragmentIsCorrect() {
        Authority authority = anAuthority();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        RelativeReference<?, ?, Fragment<String>> relativeReference = aScheme().relativeReference(authority, query, fragment);
        assertThat(relativeReference.hasFragment(), equalTo(true));
        assertThat(relativeReference.fragment(), equalTo(fragment));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndQueryAndFragmentToStringIsCorrect() {
        Authority authority = anAuthority();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(authority, query, fragment).toString(), equalTo(aScheme().relativeReference(authority, query, fragment).asString()));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndQueryAndFragmentReplacedWithANewPathIsCorrect() {
        Authority authority = anAuthority();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(aScheme().relativeReference(authority, query, fragment).withPath(newPath), equalTo(aScheme().relativeReference(authority, newPath, query, fragment)));
    }

    @Test
    void rejectsNullInFactoryForRelativeReferenceWithAuthorityAndQueryAndFragment() throws Exception {
        assertThrowsException("Null authority should throw NullPointerException in factory", NullPointerException.class, () -> aScheme().relativeReference((Authority) null, aQuery(), aFragment()));
        assertThrowsException("Null query should throw NullPointerException in factory", NullPointerException.class, () -> {
            Query<String> query = null;
            aScheme().relativeReference(anAuthority(), query, aFragment());
        });
        assertThrowsException("Null fragment should throw NullPointerException in factory", NullPointerException.class, () -> aScheme().relativeReference(anAuthority(), aQuery(), null));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndQueryAndFragmentResolvesSchemeToTheBase() {
        Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        Authority baseAuthority = anAuthority();
        AbsolutePath<String> basePath = anAbsolutePath();
        Authority relativeReferenceAuthority = anAuthority();
        Query<String> relativeReferenceQuery = aQuery();
        Fragment<String> relativeReferenceFragment = aFragment();
        assertThat(
                aScheme().relativeReference(relativeReferenceAuthority, relativeReferenceQuery, relativeReferenceFragment).resolve(baseScheme, baseAuthority, basePath),
                equalTo(baseScheme.urin(relativeReferenceAuthority, relativeReferenceQuery, relativeReferenceFragment)));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndQueryAndFragmentResolvesSchemeAndAuthorityAndQueryToTheBase() {
        Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        Authority baseAuthority = anAuthority();
        AbsolutePath<String> basePath = anAbsolutePath();
        Query<String> baseQuery = aQuery();
        Authority relativeReferenceAuthority = anAuthority();
        Query<String> relativeReferenceQuery = aQuery();
        Fragment<String> relativeReferenceFragment = aFragment();
        assertThat(
                aScheme().relativeReference(relativeReferenceAuthority, relativeReferenceQuery, relativeReferenceFragment).resolve(baseScheme, baseAuthority, basePath, baseQuery),
                equalTo(baseScheme.urin(relativeReferenceAuthority, relativeReferenceQuery, relativeReferenceFragment)));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndQueryAndFragmentResolvesSchemeAndAuthorityAndQueryAndFragmentToTheBase() {
        Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        Authority baseAuthority = anAuthority();
        AbsolutePath<String> basePath = anAbsolutePath();
        Query<String> baseQuery = aQuery();
        Fragment<String> baseFragment = aFragment();
        Authority relativeReferenceAuthority = anAuthority();
        Query<String> relativeReferenceQuery = aQuery();
        Fragment<String> relativeReferenceFragment = aFragment();
        assertThat(
                aScheme().relativeReference(relativeReferenceAuthority, relativeReferenceQuery, relativeReferenceFragment).resolve(baseScheme, baseAuthority, basePath, baseQuery, baseFragment),
                equalTo(baseScheme.urin(relativeReferenceAuthority, relativeReferenceQuery, relativeReferenceFragment)));
    }

    @Test
    void makesRelativeReferenceWithAuthorityAndNonEmptyPath() {
        Authority authority = anAuthority();
        Segment<String> firstSegment = aNonDotSegment();
        Segment<String> secondSegment = aNonDotSegment();
        assertThat(aScheme().relativeReference(authority, Path.path(firstSegment, secondSegment)).asString(), equalTo("//" + authority.asString() + "/" + firstSegment.asString() + "/" + secondSegment.asString()));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathIsEqualToAnotherWithTheSameAuthorityAndPath() {
        Authority authority = anAuthority();
        AbsolutePath<String> absolutePath = anAbsolutePath();
        assertThat(aScheme().relativeReference(authority, absolutePath), equalTo(aScheme().relativeReference(authority, absolutePath)));
        assertThat(aScheme().relativeReference(authority, absolutePath).hashCode(), equalTo(aScheme().relativeReference(authority, absolutePath).hashCode()));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathIsNotEqualToAnotherWithTheADifferentAuthorityAndPath() {
        assertThat(aScheme().relativeReference(anAuthority(), anAbsolutePath()), not(equalTo(aScheme().relativeReference(anAuthority(), anAbsolutePath()))));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathAuthorityIsCorrect() {
        Authority authority = anAuthority();
        AbsolutePath<String> absolutePath = anAbsolutePath();
        final RelativeReference<String, Query<String>, Fragment<String>> relativeReference = aScheme().relativeReference(authority, absolutePath);
        assertThat(relativeReference.hasAuthority(), equalTo(true));
        assertThat(relativeReference.authority(), equalTo(authority));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathPathIsCorrect() {
        Authority authority = anAuthority();
        AbsolutePath<String> absolutePath = anAbsolutePath();
        assertThat(aScheme().relativeReference(authority, absolutePath).path(), equalTo(absolutePath));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathQueryIsCorrect() throws Exception {
        Authority authority = anAuthority();
        AbsolutePath<String> absolutePath = anAbsolutePath();
        final RelativeReference relativeReference = aScheme().relativeReference(authority, absolutePath);
        assertThat(relativeReference.hasQuery(), equalTo(false));
        assertThrowsException("Attempt to get query from a UrinReference that does not have one.", UnsupportedOperationException.class, relativeReference::query);
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathFragmentIsCorrect() throws Exception {
        Authority authority = anAuthority();
        AbsolutePath<String> absolutePath = anAbsolutePath();
        final RelativeReference relativeReference = aScheme().relativeReference(authority, absolutePath);
        assertThat(relativeReference.hasFragment(), equalTo(false));
        assertThrowsException("Attempt to get fragment from a UrinReference that does not have one.", UnsupportedOperationException.class, relativeReference::fragment);
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathToStringIsCorrect() {
        Authority authority = anAuthority();
        AbsolutePath<String> absolutePath = anAbsolutePath();
        assertThat(aScheme().relativeReference(authority, absolutePath).toString(), equalTo(aScheme().relativeReference(authority, absolutePath).asString()));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathReplacedWithANewPathIsCorrect() {
        Authority authority = anAuthority();
        AbsolutePath<String> absolutePath = anAbsolutePath();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(aScheme().relativeReference(authority, absolutePath).withPath(newPath), equalTo(aScheme().relativeReference(authority, newPath)));
    }

    @Test
    void rejectsNullInFactoryForRelativeReferenceWithAuthorityAndPath() throws Exception {
        assertThrowsException("Null authority should throw NullPointerException in factory", NullPointerException.class, () -> {
            Authority authority = null;
            aScheme().relativeReference(authority, anAbsolutePath());
        });
        assertThrowsException("Null path should throw NullPointerException in factory", NullPointerException.class, () -> {
            AbsolutePath<String> absolutePath = null;
            aScheme().relativeReference(anAuthority(), absolutePath);
        });
    }

    @Test
    void makesRelativeReferenceWithAuthorityAndPathAndQuery() {
        Authority authority = anAuthority();
        AbsolutePath<String> absolutePath = anAbsolutePath();
        Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(authority, absolutePath, query).asString(), equalTo("//" + authority.asString() + absolutePath.asString(NEVER_PREFIX_WITH_DOT_SEGMENT) + "?" + query.asString()));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathAndQueryIsEqualToAnotherWithTheSameAuthorityAndPathAndQuery() {
        Authority authority = anAuthority();
        AbsolutePath<String> absolutePath = anAbsolutePath();
        Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(authority, absolutePath, query), equalTo(aScheme().relativeReference(authority, absolutePath, query)));
        assertThat(aScheme().relativeReference(authority, absolutePath, query).hashCode(), equalTo(aScheme().relativeReference(authority, absolutePath, query).hashCode()));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathIsNotEqualToAnotherWithTheADifferentAuthorityAndPathAndQuery() {
        assertThat(aScheme().relativeReference(anAuthority(), anAbsolutePath(), aQuery()), not(equalTo(aScheme().relativeReference(anAuthority(), anAbsolutePath(), aQuery()))));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathAndQueryAuthorityIsCorrect() {
        Authority authority = anAuthority();
        AbsolutePath<String> absolutePath = anAbsolutePath();
        Query<String> query = aQuery();
        final RelativeReference relativeReference = aScheme().relativeReference(authority, absolutePath, query);
        assertThat(relativeReference.hasAuthority(), equalTo(true));
        assertThat(relativeReference.authority(), equalTo(authority));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathAndQueryPathIsCorrect() {
        Authority authority = anAuthority();
        AbsolutePath<String> absolutePath = anAbsolutePath();
        Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(authority, absolutePath, query).path(), equalTo(absolutePath));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathAndQueryQueryIsCorrect() {
        Authority authority = anAuthority();
        AbsolutePath<String> absolutePath = anAbsolutePath();
        Query<String> query = aQuery();
        final RelativeReference<String, Query<String>, Fragment<String>> relativeReference = aScheme().relativeReference(authority, absolutePath, query);
        assertThat(relativeReference.hasQuery(), equalTo(true));
        assertThat(relativeReference.query(), equalTo(query));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathAndQueryFragmentIsCorrect() throws Exception {
        Authority authority = anAuthority();
        AbsolutePath<String> absolutePath = anAbsolutePath();
        Query<String> query = aQuery();
        final RelativeReference relativeReference = aScheme().relativeReference(authority, absolutePath, query);
        assertThat(relativeReference.hasFragment(), equalTo(false));
        assertThrowsException("Attempt to get fragment from a UrinReference that does not have one.", UnsupportedOperationException.class, relativeReference::fragment);
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathAndQueryToStringIsCorrect() {
        Authority authority = anAuthority();
        AbsolutePath<String> absolutePath = anAbsolutePath();
        Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(authority, absolutePath, query).toString(), equalTo(aScheme().relativeReference(authority, absolutePath, query).asString()));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathAndQueryReplacedWithANewPathIsCorrect() {
        Authority authority = anAuthority();
        AbsolutePath<String> absolutePath = anAbsolutePath();
        Query<String> query = aQuery();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(aScheme().relativeReference(authority, absolutePath, query).withPath(newPath), equalTo(aScheme().relativeReference(authority, newPath, query)));
    }

    @Test
    void rejectsNullInFactoryForRelativeReferenceWithAuthorityAndPathAndQuery() throws Exception {
        assertThrowsException("Null authority should throw NullPointerException in factory", NullPointerException.class, () -> aScheme().relativeReference(null, anAbsolutePath(), aQuery()));
        assertThrowsException("Null path should throw NullPointerException in factory", NullPointerException.class, () -> aScheme().relativeReference(anAuthority(), null, aQuery()));
        assertThrowsException("Null query should throw NullPointerException in factory", NullPointerException.class, () -> aScheme().relativeReference(anAuthority(), anAbsolutePath(), (Query<String>) null));
    }

    @Test
    void makesRelativeReferenceWithAuthorityAndPathAndFragment() {
        Authority authority = anAuthority();
        AbsolutePath<String> absolutePath = anAbsolutePath();
        Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(authority, absolutePath, fragment).asString(), equalTo("//" + authority.asString() + absolutePath.asString(NEVER_PREFIX_WITH_DOT_SEGMENT) + "#" + fragment.asString()));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathAndFragmentIsEqualToAnotherWithTheSameAuthorityAndPathAndFragment() {
        Authority authority = anAuthority();
        AbsolutePath<String> absolutePath = anAbsolutePath();
        Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(authority, absolutePath, fragment), equalTo(aScheme().relativeReference(authority, absolutePath, fragment)));
        assertThat(aScheme().relativeReference(authority, absolutePath, fragment).hashCode(), equalTo(aScheme().relativeReference(authority, absolutePath, fragment).hashCode()));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathIsNotEqualToAnotherWithTheADifferentAuthorityAndPathAndFragment() {
        assertThat(aScheme().relativeReference(anAuthority(), anAbsolutePath(), aFragment()), not(equalTo(aScheme().relativeReference(anAuthority(), anAbsolutePath(), aFragment()))));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathAndFragmentAuthorityIsCorrect() {
        Authority authority = anAuthority();
        AbsolutePath<String> absolutePath = anAbsolutePath();
        Fragment<String> fragment = aFragment();
        final RelativeReference relativeReference = aScheme().relativeReference(authority, absolutePath, fragment);
        assertThat(relativeReference.hasAuthority(), equalTo(true));
        assertThat(relativeReference.authority(), equalTo(authority));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathAndFragmentPathIsCorrect() {
        Authority authority = anAuthority();
        AbsolutePath<String> absolutePath = anAbsolutePath();
        Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(authority, absolutePath, fragment).path(), equalTo(absolutePath));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathAndFragmentQueryIsCorrect() throws Exception {
        Authority authority = anAuthority();
        AbsolutePath<String> absolutePath = anAbsolutePath();
        Fragment<String> fragment = aFragment();
        final RelativeReference relativeReference = aScheme().relativeReference(authority, absolutePath, fragment);
        assertThat(relativeReference.hasQuery(), equalTo(false));
        assertThrowsException("Attempt to get query from a UrinReference that does not have one.", UnsupportedOperationException.class, relativeReference::query);
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathAndFragmentFragmentIsCorrect() {
        Authority authority = anAuthority();
        AbsolutePath<String> absolutePath = anAbsolutePath();
        Fragment<String> fragment = aFragment();
        RelativeReference<?, ?, Fragment<String>> relativeReference = aScheme().relativeReference(authority, absolutePath, fragment);
        assertThat(relativeReference.hasFragment(), equalTo(true));
        assertThat(relativeReference.fragment(), equalTo(fragment));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathAndFragmentToStringIsCorrect() {
        Authority authority = anAuthority();
        AbsolutePath<String> absolutePath = anAbsolutePath();
        Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(authority, absolutePath, fragment).toString(), equalTo(aScheme().relativeReference(authority, absolutePath, fragment).asString()));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathAndFragmentReplacedWithANewPathIsCorrect() {
        Authority authority = anAuthority();
        AbsolutePath<String> absolutePath = anAbsolutePath();
        Fragment<String> fragment = aFragment();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(aScheme().relativeReference(authority, absolutePath, fragment).withPath(newPath), equalTo(aScheme().relativeReference(authority, newPath, fragment)));
    }

    @Test
    void rejectsNullInFactoryForRelativeReferenceWithAuthorityAndPathAndFragment() throws Exception {
        assertThrowsException("Null authority should throw NullPointerException in factory", NullPointerException.class, () -> aScheme().relativeReference(null, anAbsolutePath(), aFragment()));
        assertThrowsException("Null path should throw NullPointerException in factory", NullPointerException.class, () -> aScheme().relativeReference(anAuthority(), (AbsolutePath<String>) null, aFragment()));
        assertThrowsException("Null fragment should throw NullPointerException in factory", NullPointerException.class, () -> aScheme().relativeReference(anAuthority(), anAbsolutePath(), (Fragment<String>) null));
    }

    @Test
    void makesRelativeReferenceWithAuthorityAndPathAndQueryAndFragment() {
        Authority authority = anAuthority();
        AbsolutePath<String> absolutePath = anAbsolutePath();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(authority, absolutePath, query, fragment).asString(), equalTo("//" + authority.asString() + absolutePath.asString(NEVER_PREFIX_WITH_DOT_SEGMENT) + "?" + query.asString() + "#" + fragment.asString()));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathAndQueryAndFragmentIsEqualToAnotherWithTheSameAuthorityAndPathAndQueryAndFragment() {
        Authority authority = anAuthority();
        AbsolutePath<String> absolutePath = anAbsolutePath();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(authority, absolutePath, query, fragment), equalTo(aScheme().relativeReference(authority, absolutePath, query, fragment)));
        assertThat(aScheme().relativeReference(authority, absolutePath, query, fragment).hashCode(), equalTo(aScheme().relativeReference(authority, absolutePath, query, fragment).hashCode()));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathIsNotEqualToAnotherWithTheADifferentAuthorityAndPathAndQueryAndFragment() {
        assertThat(aScheme().relativeReference(anAuthority(), anAbsolutePath(), aQuery(), aFragment()), not(equalTo(aScheme().relativeReference(anAuthority(), anAbsolutePath(), aQuery(), aFragment()))));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathAndQueryAndFragmentAuthorityIsCorrect() {
        Authority authority = anAuthority();
        AbsolutePath<String> absolutePath = anAbsolutePath();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        final RelativeReference relativeReference = aScheme().relativeReference(authority, absolutePath, query, fragment);
        assertThat(relativeReference.hasAuthority(), equalTo(true));
        assertThat(relativeReference.authority(), equalTo(authority));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathAndQueryAndFragmentPathIsCorrect() {
        Authority authority = anAuthority();
        AbsolutePath<String> absolutePath = anAbsolutePath();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(authority, absolutePath, query, fragment).path(), equalTo(absolutePath));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathAndQueryAndFragmentQueryIsCorrect() {
        Authority authority = anAuthority();
        AbsolutePath<String> absolutePath = anAbsolutePath();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        RelativeReference<String, Query<String>, Fragment<String>> relativeReference = aScheme().relativeReference(authority, absolutePath, query, fragment);
        assertThat(relativeReference.hasQuery(), equalTo(true));
        assertThat(relativeReference.query(), equalTo(query));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathAndQueryAndFragmentFragmentIsCorrect() {
        Authority authority = anAuthority();
        AbsolutePath<String> absolutePath = anAbsolutePath();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        RelativeReference<?, ?, Fragment<String>> relativeReference = aScheme().relativeReference(authority, absolutePath, query, fragment);
        assertThat(relativeReference.hasFragment(), equalTo(true));
        assertThat(relativeReference.fragment(), equalTo(fragment));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathAndQueryAndFragmentToStringIsCorrect() {
        Authority authority = anAuthority();
        AbsolutePath<String> absolutePath = anAbsolutePath();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(authority, absolutePath, query, fragment).toString(), equalTo(aScheme().relativeReference(authority, absolutePath, query, fragment).asString()));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathAndQueryAndFragmentReplacedWithANewPathIsCorrect() {
        Authority authority = anAuthority();
        AbsolutePath<String> absolutePath = anAbsolutePath();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(aScheme().relativeReference(authority, absolutePath, query, fragment).withPath(newPath), equalTo(aScheme().relativeReference(authority, newPath, query, fragment)));
    }

    @Test
    void rejectsNullInFactoryForRelativeReferenceWithAuthorityAndPathAndQueryAndFragment() throws Exception {
        assertThrowsException("Null authority should throw NullPointerException in factory", NullPointerException.class, () -> aScheme().relativeReference(null, anAbsolutePath(), aQuery(), aFragment()));
        assertThrowsException("Null path should throw NullPointerException in factory", NullPointerException.class, () -> aScheme().relativeReference(anAuthority(), null, aQuery(), aFragment()));
        assertThrowsException("Null query should throw NullPointerException in factory", NullPointerException.class, () -> aScheme().relativeReference(anAuthority(), anAbsolutePath(), null, aFragment()));
        assertThrowsException("Null fragment should throw NullPointerException in factory", NullPointerException.class, () -> aScheme().relativeReference(anAuthority(), anAbsolutePath(), aQuery(), null));
    }

    @Test
    void parsesARelativeReferenceWithEmptyPath() throws Exception {
        assertThat(aScheme().parseRelativeReference(""), equalTo(aScheme().relativeReference()));
    }

    @Test
    void parsesARelativeReferenceWithEmptyPathWithQuery() throws Exception {
        Query<String> query = aQuery();
        assertThat(aScheme().parseRelativeReference("?" + query.asString()), equalTo(aScheme().relativeReference(query)));
    }

    @Test
    void parsesARelativeReferenceWithEmptyPathWithFragment() throws Exception {
        Fragment<String> fragment = aFragment();
        assertThat(aScheme().parseRelativeReference("#" + fragment.asString()), equalTo(aScheme().relativeReference(fragment)));
    }

    @Test
    void parsesARelativeReferenceWithEmptyPathWithQueryAndFragment() throws Exception {
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(aScheme().parseRelativeReference("?" + query.asString() + "#" + fragment.asString()), equalTo(aScheme().relativeReference(query, fragment)));
    }

    @Test
    void parsesARelativeReferenceWithPathAndQuery() throws Exception {
        Path<String> path = anUnpollutedPath();
        Query<String> query = aQuery();
        assertThat(aScheme().parseRelativeReference(path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON) + "?" + query.asString()), equalTo(aScheme().relativeReference(path, query)));
    }

    @Test
    void parsesARelativeReferenceWithPathAndFragment() throws Exception {
        Path<String> path = anUnpollutedPath();
        Fragment<String> fragment = aFragment();
        assertThat(aScheme().parseRelativeReference(path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON) + "#" + fragment.asString()), equalTo(aScheme().relativeReference(path, fragment)));
    }

    @Test
    void parsesARelativeReferenceWithPathAndQueryAndFragment() throws Exception {
        Path<String> path = anUnpollutedPath();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(aScheme().parseRelativeReference(path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON) + "?" + query.asString() + "#" + fragment.asString()), equalTo(aScheme().relativeReference(path, query, fragment)));
    }

    @Test
    void parsesASimpleAbsolutePath() throws Exception {
        Segment<String> firstSegment = aSegment();
        Segment<String> secondSegment = aSegment();
        assertThat(aScheme().parseRelativeReference("/" + firstSegment.asString() + "/" + secondSegment.asString()), equalTo(aScheme().relativeReference(Path.path(firstSegment, secondSegment))));
    }

    @Test
    void parsesASimpleAbsolutePathPrefixedWithADotSegment() throws Exception {
        Segment<String> firstSegment = segment("");
        Segment<String> secondSegment = aSegment();
        assertThat(aScheme().parseRelativeReference("/./" + firstSegment.asString() + "/" + secondSegment.asString()), equalTo(aScheme().relativeReference(Path.path(firstSegment, secondSegment))));
    }

    @Test
    void parsesASimpleRootlessPath() throws Exception {
        Segment<String> firstSegment = aSegment();
        Segment<String> secondSegment = aSegment();
        assertThat(aScheme().parseRelativeReference(firstSegment.asString() + "/" + secondSegment.asString()), equalTo(aScheme().relativeReference(rootlessPath(firstSegment, secondSegment))));
    }

    @Test
    void parsesARelativeReferenceWithAuthorityAndEmptyPath() throws Exception {
        Authority authority = anAuthority();
        assertThat(aScheme().parseRelativeReference("//" + authority.asString()), equalTo(aScheme().relativeReference(authority)));
    }

    @Test
    void parsesARelativeReferenceWithAuthorityAndQuery() throws Exception {
        Authority authority = anAuthority();
        Query<String> query = aQuery();
        assertThat(aScheme().parseRelativeReference("//" + authority.asString() + "?" + query.asString()), equalTo(aScheme().relativeReference(authority, query)));
    }

    @Test
    void parsesARelativeReferenceWithAuthorityAndFragment() throws Exception {
        Authority authority = anAuthority();
        Fragment<String> fragment = aFragment();
        assertThat(aScheme().parseRelativeReference("//" + authority.asString() + "#" + fragment.asString()), equalTo(aScheme().relativeReference(authority, fragment)));
    }

    @Test
    void parsesARelativeReferenceWithAuthorityAndQueryAndFragment() throws Exception {
        Authority authority = anAuthority();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(aScheme().parseRelativeReference("//" + authority.asString() + "?" + query.asString() + "#" + fragment.asString()), equalTo(aScheme().relativeReference(authority, query, fragment)));
    }

    @Test
    void parsesARelativeReferenceWithAuthorityAndNonEmptyPath() throws Exception {
        Authority authority = anAuthority();
        Segment<String> firstSegment = aSegment();
        Segment<String> secondSegment = aSegment();
        assertThat(aScheme().parseRelativeReference("//" + authority.asString() + "/" + firstSegment.asString() + "/" + secondSegment.asString()), equalTo(aScheme().relativeReference(authority, Path.path(firstSegment, secondSegment))));
    }

    @Test
    void parsesRelativeReferenceWithAuthorityAndPathAndQuery() throws Exception {
        Authority authority = anAuthority();
        AbsolutePath<String> absolutePath = anUnpollutedAbsolutePath();
        Query<String> query = aQuery();
        assertThat(aScheme().parseRelativeReference("//" + authority.asString() + absolutePath.asString(NEVER_PREFIX_WITH_DOT_SEGMENT) + "?" + query.asString()), equalTo(aScheme().relativeReference(authority, absolutePath, query)));
    }

    @Test
    void parsesRelativeReferenceWithAuthorityAndPathAndFragment() throws Exception {
        Authority authority = anAuthority();
        AbsolutePath<String> absolutePath = anUnpollutedAbsolutePath();
        Fragment<String> fragment = aFragment();
        assertThat(aScheme().parseRelativeReference("//" + authority.asString() + absolutePath.asString(NEVER_PREFIX_WITH_DOT_SEGMENT) + "#" + fragment.asString()), equalTo(aScheme().relativeReference(authority, absolutePath, fragment)));
    }

    @Test
    void parsesRelativeReferenceWithAuthorityAndPathAndQueryAndFragment() throws Exception {
        Authority authority = anAuthority();
        AbsolutePath<String> absolutePath = anUnpollutedAbsolutePath();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(aScheme().parseRelativeReference("//" + authority.asString() + absolutePath.asString(NEVER_PREFIX_WITH_DOT_SEGMENT) + "?" + query.asString() + "#" + fragment.asString()), equalTo(aScheme().relativeReference(authority, absolutePath, query, fragment)));
    }

    @Test
    void parsesRelativeReferenceFromUri() throws Exception {
        Authority authority = authority(aRegisteredName());
        AbsolutePath<String> absolutePath = anUnpollutedAbsolutePath();
        Query<String> query = aQuery();
        Fragment<String> fragment = aFragment();
        assertThat(aScheme().parseRelativeReference(URI.create("//" + authority.asString() + absolutePath.asString(NEVER_PREFIX_WITH_DOT_SEGMENT) + "?" + query.asString() + "#" + fragment.asString())), equalTo(aScheme().relativeReference(authority, absolutePath, query, fragment)));
    }

    @Test
    void parsingNullThrowsNullPointerException() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        assertThrowsException("Null value should throw NullPointerException in parser", NullPointerException.class, () -> scheme.parseRelativeReference((String) null));
    }

}
