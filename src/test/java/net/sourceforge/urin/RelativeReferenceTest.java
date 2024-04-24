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

import java.net.URI;

import static net.sourceforge.urin.Authority.authority;
import static net.sourceforge.urin.AuthorityBuilder.anAuthority;
import static net.sourceforge.urin.FragmentBuilder.aFragment;
import static net.sourceforge.urin.HostBuilder.aRegisteredName;
import static net.sourceforge.urin.MoreRandomStringUtils.aStringIncluding;
import static net.sourceforge.urin.Path.PrefixWithDotSegmentCriteria.NEVER_PREFIX_WITH_DOT_SEGMENT;
import static net.sourceforge.urin.Path.PrefixWithDotSegmentCriteria.PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON;
import static net.sourceforge.urin.Path.rootlessPath;
import static net.sourceforge.urin.PathBuilder.aPath;
import static net.sourceforge.urin.PathBuilder.anAbsolutePath;
import static net.sourceforge.urin.PathBuilder.anUnpollutedAbsolutePath;
import static net.sourceforge.urin.PathBuilder.anUnpollutedPath;
import static net.sourceforge.urin.QueryBuilder.aQuery;
import static net.sourceforge.urin.SchemeBuilder.aScheme;
import static net.sourceforge.urin.Segment.dot;
import static net.sourceforge.urin.Segment.segment;
import static net.sourceforge.urin.SegmentBuilder.aNonDotSegment;
import static net.sourceforge.urin.SegmentBuilder.aSegment;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RelativeReferenceTest {
    @Test
    void aRelativeReferenceWithEmptyPathAsStringIsCorrect() {
        assertThat(aScheme().relativeReference().asString(), equalTo(""));
        assertThat(aScheme().relativeReference().asUri(), equalTo(URI.create("")));
    }

    @Test
    void aRelativeReferenceWithEmptyPathAuthorityIsCorrect() {
        final RelativeReference<?, ?, ?> relativeReference = aScheme().relativeReference();
        assertThat(relativeReference.hasAuthority(), equalTo(false));
        assertThrows(UnsupportedOperationException.class, relativeReference::authority, "Attempt to get authority from a UrinReference that does not have one.");
    }

    @Test
    void aRelativeReferenceWithEmptyPathPathIsCorrect() {
        assertThat(aScheme().relativeReference().path(), equalTo(Path.<String>rootlessPath()));
    }

    @Test
    void aRelativeReferenceWithEmptyPathQueryIsCorrect() {
        final RelativeReference<?, ?, ?> relativeReference = aScheme().relativeReference();
        assertThat(relativeReference.hasQuery(), equalTo(false));
        assertThrows(UnsupportedOperationException.class, relativeReference::query, "Attempt to get query from a UrinReference that does not have one.");
    }

    @Test
    void aRelativeReferenceWithEmptyPathFragmentIsCorrect() {
        final RelativeReference<?, ?, ?> relativeReference = aScheme().relativeReference();
        assertThat(relativeReference.hasFragment(), equalTo(false));
        assertThrows(UnsupportedOperationException.class, relativeReference::fragment, "Attempt to get fragment from a UrinReference that does not have one.");
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
        final Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(query).asString(), equalTo("?" + query.asString()));
        assertThat(aScheme().relativeReference(query).asUri(), equalTo(URI.create("?" + query.asString())));
    }

    @Test
    void aRelativeReferenceWithEmptyPathWithQueryAuthorityIsCorrect() {
        final RelativeReference<?, ?, ?> relativeReference = aScheme().relativeReference(aQuery());
        assertThat(relativeReference.hasAuthority(), equalTo(false));
        assertThrows(UnsupportedOperationException.class, relativeReference::authority, "Attempt to get authority from a UrinReference that does not have one.");
    }

    @Test
    void aRelativeReferenceWithEmptyPathWithQueryPathIsCorrect() {
        final Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(query).path(), equalTo(Path.<String>rootlessPath()));
    }

    @Test
    void aRelativeReferenceWithEmptyPathWithQueryQueryIsCorrect() {
        final Query<String> query = aQuery();
        final RelativeReference<String, Query<String>, Fragment<String>> relativeReference = aScheme().relativeReference(query);
        assertThat(relativeReference.hasQuery(), equalTo(true));
        assertThat(relativeReference.query(), equalTo(query));
    }

    @Test
    void aRelativeReferenceWithEmptyPathWithQueryFragmentIsCorrect() {
        final Query<String> query = aQuery();
        final RelativeReference<?, ?, ?> relativeReference = aScheme().relativeReference(query);
        assertThat(relativeReference.hasFragment(), equalTo(false));
        assertThrows(UnsupportedOperationException.class, relativeReference::fragment, "Attempt to get fragment from a UrinReference that does not have one.");
    }

    @Test
    void aRelativeReferenceWithEmptyPathWithQueryIsEqualToAnotherRelativeReferenceWithEmptyPath() {
        final Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(query), equalTo(aScheme().relativeReference(query)));
        assertThat(aScheme().relativeReference(query).hashCode(), equalTo(aScheme().relativeReference(query).hashCode()));
    }

    @Test
    void aRelativeReferenceWithEmptyPathWithQueryIsNotEqualToAnotherWithTheADifferentQuery() {
        assertThat(aScheme().relativeReference(aQuery()), not(equalTo(aScheme().relativeReference(aQuery()))));
    }

    @Test
    void aRelativeReferenceWithEmptyPathWithQueryToStringIsCorrect() {
        final Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(query).toString(), equalTo(aScheme().relativeReference(query).asString()));
    }

    @Test
    void aRelativeReferenceWithEmptyPathWithQueryReplacedWithANewPathIsCorrect() {
        final Query<String> query = aQuery();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(aScheme().relativeReference(query).withPath(newPath), equalTo(aScheme().relativeReference(newPath, query)));
    }

    @Test
    @SuppressWarnings("ConstantValue")
    void rejectsNullInFactoryForARelativeReferenceWithEmptyPathWithQuery() {
        assertThrows(NullPointerException.class, () -> {
            final Query<String> query = null;
            aScheme().relativeReference(query);
        }, "Null query should throw NullPointerException in factory");
    }

    @Test
    void aRelativeReferenceWithEmptyPathWithFragmentAsStringIsCorrect() {
        final Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(fragment).asString(), equalTo("#" + fragment.asString()));
        assertThat(aScheme().relativeReference(fragment).asUri(), equalTo(URI.create("#" + fragment.asString())));
    }

    @Test
    void aRelativeReferenceWithEmptyPathWithFragmentAuthorityIsCorrect() {
        final Fragment<String> fragment = aFragment();
        final RelativeReference<?, ?, ?> relativeReference = aScheme().relativeReference(fragment);
        assertThat(relativeReference.hasAuthority(), equalTo(false));
        assertThrows(UnsupportedOperationException.class, relativeReference::authority, "Attempt to get authority from a UrinReference that does not have one.");

    }

    @Test
    void aRelativeReferenceWithEmptyPathWithFragmentPathIsCorrect() {
        final Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(fragment).path(), equalTo(Path.<String>rootlessPath()));
    }

    @Test
    void aRelativeReferenceWithEmptyPathWithFragmentQueryIsCorrect() {
        final Fragment<String> fragment = aFragment();
        final RelativeReference<?, ?, ?> relativeReference = aScheme().relativeReference(fragment);
        assertThat(relativeReference.hasQuery(), equalTo(false));
        assertThrows(UnsupportedOperationException.class, relativeReference::query, "Attempt to get query from a UrinReference that does not have one.");
    }

    @Test
    void aRelativeReferenceWithEmptyPathWithFragmentFragmentIsCorrect() {
        final Fragment<String> fragment = aFragment();
        final RelativeReference<?, ?, Fragment<String>> relativeReference = aScheme().relativeReference(fragment);
        assertThat(relativeReference.hasFragment(), equalTo(true));
        assertThat(relativeReference.fragment(), equalTo(fragment));
    }

    @Test
    void aRelativeReferenceWithEmptyPathWithFragmentIsEqualToAnotherRelativeReferenceWithEmptyPath() {
        final Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(fragment), equalTo(aScheme().relativeReference(fragment)));
        assertThat(aScheme().relativeReference(fragment).hashCode(), equalTo(aScheme().relativeReference(fragment).hashCode()));
    }

    @Test
    void aRelativeReferenceWithEmptyPathWithFragmentIsNotEqualToAnotherWithTheADifferentFragment() {
        assertThat(aScheme().relativeReference(aFragment()), not(equalTo(aScheme().relativeReference(aFragment()))));
    }

    @Test
    void aRelativeReferenceWithEmptyPathWithFragmentToStringIsCorrect() {
        final Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(fragment).toString(), equalTo(aScheme().relativeReference(fragment).asString()));
    }

    @Test
    void aRelativeReferenceWithEmptyPathWithFragmentReplacedWithANewPathIsCorrect() {
        final Fragment<String> fragment = aFragment();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(aScheme().relativeReference(fragment).withPath(newPath), equalTo(aScheme().relativeReference(newPath, fragment)));
    }

    @Test
    @SuppressWarnings("ConstantValue")
    void rejectsNullInFactoryForARelativeReferenceWithEmptyPathWithFragment() {
        assertThrows(NullPointerException.class, () -> {
            final Fragment<String> fragment = null;
            aScheme().relativeReference(fragment);
        }, "Null fragment should throw NullPointerException in factory");
    }

    @Test
    void aRelativeReferenceWithEmptyPathWithQueryAndFragmentAsStringIsCorrect() {
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(query, fragment).asString(), equalTo("?" + query.asString() + "#" + fragment.asString()));
        assertThat(aScheme().relativeReference(query, fragment).asUri(), equalTo(URI.create("?" + query.asString() + "#" + fragment.asString())));
    }

    @Test
    void aRelativeReferenceWithEmptyPathWithQueryAndFragmentAuthorityIsCorrect() {
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        final RelativeReference<?, ?, ?> relativeReference = aScheme().relativeReference(query, fragment);
        assertThat(relativeReference.hasAuthority(), equalTo(false));
        assertThrows(UnsupportedOperationException.class, relativeReference::authority, "Attempt to get authority from a UrinReference that does not have one.");

    }

    @Test
    void aRelativeReferenceWithEmptyPathWithQueryAndFragmentPathIsCorrect() {
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(query, fragment).path(), equalTo(Path.<String>rootlessPath()));
    }

    @Test
    void aRelativeReferenceWithEmptyPathWithQueryAndFragmentQueryIsCorrect() {
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        final RelativeReference<String, Query<String>, Fragment<String>> relativeReference = aScheme().relativeReference(query, fragment);
        assertThat(relativeReference.hasQuery(), equalTo(true));
        assertThat(relativeReference.query(), equalTo(query));
    }

    @Test
    void aRelativeReferenceWithEmptyPathWithQueryAndFragmentFragmentIsCorrect() {
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        final RelativeReference<?, ?, Fragment<String>> relativeReference = aScheme().relativeReference(query, fragment);
        assertThat(relativeReference.hasFragment(), equalTo(true));
        assertThat(relativeReference.fragment(), equalTo(fragment));
    }

    @Test
    void aRelativeReferenceWithEmptyPathWithQueryAndFragmentIsEqualToAnotherRelativeReferenceWithEmptyPath() {
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(query, fragment), equalTo(aScheme().relativeReference(query, fragment)));
        assertThat(aScheme().relativeReference(query, fragment).hashCode(), equalTo(aScheme().relativeReference(query, fragment).hashCode()));
    }

    @Test
    void aRelativeReferenceWithEmptyPathWithQueryAndFragmentIsNotEqualToAnotherWithTheADifferentQuery() {
        final Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(aQuery(), fragment), not(equalTo(aScheme().relativeReference(aQuery(), fragment))));
    }

    @Test
    void aRelativeReferenceWithEmptyPathWithQueryAndFragmentIsNotEqualToAnotherWithTheADifferentFragment() {
        final Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(query, aFragment()), not(equalTo(aScheme().relativeReference(query, aFragment()))));
    }

    @Test
    void aRelativeReferenceWithEmptyPathWithQueryAndFragmentToStringIsCorrect() {
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(query, fragment).toString(), equalTo(aScheme().relativeReference(query, fragment).asString()));
    }

    @Test
    void aRelativeReferenceWithEmptyPathWithQueryAndFragmentReplacedWithANewPathIsCorrect() {
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(aScheme().relativeReference(query, fragment).withPath(newPath), equalTo(aScheme().relativeReference(newPath, query, fragment)));
    }

    @Test
    @SuppressWarnings("ConstantValue")
    void rejectsNullInFactoryForARelativeReferenceWithEmptyPathWithQueryAndFragment() {
        assertThrows(NullPointerException.class, () -> {
            final Query<String> query = null;
            aScheme().relativeReference(query, aFragment());
        }, "Null query should throw NullPointerException in factory");
        assertThrows(NullPointerException.class, () -> {
            final Fragment<String> fragment = null;
            aScheme().relativeReference(aQuery(), fragment);
        }, "Null fragment should throw NullPointerException in factory");
    }

    @Test
    void aRelativeReferenceWithPathAsStringIsCorrect() {
        final Path<String> path = aPath();
        assertThat(aScheme().relativeReference(path).asString(), equalTo(path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON)));
        assertThat(aScheme().relativeReference(path).asUri(), equalTo(URI.create(path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON))));
    }

    @Test
    void aRelativeReferenceWithPathAuthorityIsCorrect() {
        final Path<String> path = aPath();
        final RelativeReference<?, ?, ?> relativeReference = aScheme().relativeReference(path);
        assertThat(relativeReference.hasAuthority(), equalTo(false));
        assertThrows(UnsupportedOperationException.class, relativeReference::authority, "Attempt to get authority from a UrinReference that does not have one.");

    }

    @Test
    void aRelativeReferenceWithPathPathIsCorrect() {
        final Path<String> path = aPath();
        assertThat(aScheme().relativeReference(path).path(), equalTo(path));
    }

    @Test
    void aRelativeReferenceWithPathQueryIsCorrect() {
        final Path<String> path = aPath();
        final RelativeReference<?, ?, ?> relativeReference = aScheme().relativeReference(path);
        assertThat(relativeReference.hasQuery(), equalTo(false));
        assertThrows(UnsupportedOperationException.class, relativeReference::query, "Attempt to get query from a UrinReference that does not have one.");
    }

    @Test
    void aRelativeReferenceWithPathFragmentIsCorrect() {
        final Path<String> path = aPath();
        final RelativeReference<?, ?, ?> relativeReference = aScheme().relativeReference(path);
        assertThat(relativeReference.hasFragment(), equalTo(false));
        assertThrows(UnsupportedOperationException.class, relativeReference::fragment, "Attempt to get fragment from a UrinReference that does not have one.");
    }

    @Test
    void aRelativeReferenceWithPathIsEqualToAnotherRelativeReferenceWithPathAndQuery() {
        final Path<String> path = aPath();
        assertThat(aScheme().relativeReference(path), equalTo(aScheme().relativeReference(path)));
        assertThat(aScheme().relativeReference(path).hashCode(), equalTo(aScheme().relativeReference(path).hashCode()));
    }

    @Test
    void aRelativeReferenceWithPathIsNotEqualToAnotherWithTheADifferentPath() {
        assertThat(aScheme().relativeReference(aPath()), not(equalTo(aScheme().relativeReference(aPath()))));
    }

    @Test
    void aRelativeReferenceWithPathToStringIsCorrect() {
        final Path<String> path = aPath();
        assertThat(aScheme().relativeReference(path).toString(), equalTo(aScheme().relativeReference(path).asString()));
    }

    @Test
    void aRelativeReferenceWithPathReplacedWithANewPathIsCorrect() {
        final Path<String> path = aPath();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(aScheme().relativeReference(path).withPath(newPath), equalTo(aScheme().relativeReference(newPath)));
    }

    @Test
    void rejectsNullInFactoryForARelativeReferenceWithPath() {
        assertThrows(NullPointerException.class, () -> aScheme().relativeReference((Path<String>) null), "Null path should throw NullPointerException in factory");
    }

    @Test
    void aRelativeReferenceWithPathResolvesSchemeToTheBase() {
        final Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        final Authority baseAuthority = anAuthority();
        final AbsolutePath<String> basePath = anAbsolutePath();
        final Path<String> relativeReferencePath = aPath();
        assertThat(
                aScheme().relativeReference(relativeReferencePath).resolve(baseScheme, baseAuthority, basePath),
                equalTo(baseScheme.urin(baseAuthority, (AbsolutePath<String>) relativeReferencePath.resolveRelativeTo(basePath))));
    }

    @Test
    void aRelativeReferenceWithPathResolvesSchemeAndAuthorityToTheBase() {
        final Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        final Authority baseAuthority = anAuthority();
        final AbsolutePath<String> basePath = anAbsolutePath();
        final Query<String> baseQuery = aQuery();
        final Path<String> relativeReferencePath = aPath();
        assertThat(
                aScheme().relativeReference(relativeReferencePath).resolve(baseScheme, baseAuthority, basePath, baseQuery),
                equalTo(baseScheme.urin(baseAuthority, (AbsolutePath<String>) relativeReferencePath.resolveRelativeTo(basePath))));
    }

    @Test
    void aRelativeReferenceWithPathResolvesSchemeAndAuthorityAndQueryAndFragmentToTheBase() {
        final Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        final Authority baseAuthority = anAuthority();
        final AbsolutePath<String> basePath = anAbsolutePath();
        final Query<String> baseQuery = aQuery();
        final Fragment<String> baseFragment = aFragment();
        final Path<String> relativeReferencePath = aPath();
        assertThat(
                aScheme().relativeReference(relativeReferencePath).resolve(baseScheme, baseAuthority, basePath, baseQuery, baseFragment),
                equalTo(baseScheme.urin(baseAuthority, (AbsolutePath<String>) relativeReferencePath.resolveRelativeTo(basePath))));
    }

    @Test
    void aRelativeReferenceWithPathAndQueryAsStringIsCorrect() {
        final Path<String> path = aPath();
        final Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(path, query).asString(), equalTo(path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON) + "?" + query.asString()));
        assertThat(aScheme().relativeReference(path, query).asUri(), equalTo(URI.create(path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON) + "?" + query.asString())));
    }

    @Test
    void aRelativeReferenceWithPathAndQueryAuthorityIsCorrect() {
        final RelativeReference<?, ?, ?> relativeReference = aScheme().relativeReference(aPath(), aQuery());
        assertThat(relativeReference.hasAuthority(), equalTo(false));
        assertThrows(UnsupportedOperationException.class, relativeReference::authority, "Attempt to get authority from a UrinReference that does not have one.");

    }

    @Test
    void aRelativeReferenceWithPathAndQueryPathIsCorrect() {
        final Path<String> path = aPath();
        final Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(path, query).path(), equalTo(path));
    }

    @Test
    void aRelativeReferenceWithPathAndQueryQueryIsCorrect() {
        final Path<String> path = aPath();
        final Query<String> query = aQuery();
        final RelativeReference<String, Query<String>, Fragment<String>> relativeReference = aScheme().relativeReference(path, query);
        assertThat(relativeReference.hasQuery(), equalTo(true));
        assertThat(relativeReference.query(), equalTo(query));
    }

    @Test
    void aRelativeReferenceWithPathAndQueryFragmentIsCorrect() {
        final Path<String> path = aPath();
        final Query<String> query = aQuery();
        final RelativeReference<?, ?, ?> relativeReference = aScheme().relativeReference(path, query);
        assertThat(relativeReference.hasFragment(), equalTo(false));
        assertThrows(UnsupportedOperationException.class, relativeReference::fragment, "Attempt to get fragment from a UrinReference that does not have one.");
    }

    @Test
    void aRelativeReferenceWithPathAndQueryIsEqualToAnotherRelativeReferenceWithPathAndQuery() {
        final Path<String> path = aPath();
        final Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(path, query), equalTo(aScheme().relativeReference(path, query)));
        assertThat(aScheme().relativeReference(path, query).hashCode(), equalTo(aScheme().relativeReference(path, query).hashCode()));
    }

    @Test
    void aRelativeReferenceWithPathAndQueryIsNotEqualToAnotherWithTheADifferentPath() {
        final Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(aPath(), query), not(equalTo(aScheme().relativeReference(aPath(), query))));
    }

    @Test
    void aRelativeReferenceWithPathAndQueryIsNotEqualToAnotherWithTheADifferentQuery() {
        final Path<String> path = aPath();
        assertThat(aScheme().relativeReference(path, aQuery()), not(equalTo(aScheme().relativeReference(path, aQuery()))));
    }

    @Test
    void aRelativeReferenceWithPathAndQueryToStringIsCorrect() {
        final Path<String> path = aPath();
        final Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(path, query).toString(), equalTo(aScheme().relativeReference(path, query).asString()));
    }

    @Test
    void aRelativeReferenceWithPathAndQueryReplacedWithANewPathIsCorrect() {
        final Path<String> path = aPath();
        final Query<String> query = aQuery();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(aScheme().relativeReference(path, query).withPath(newPath), equalTo(aScheme().relativeReference(newPath, query)));
    }

    @Test
    void rejectsNullInFactoryForARelativeReferenceWithPathAndQuery() {
        assertThrows(NullPointerException.class, () -> aScheme().relativeReference((Path<String>) null, aQuery()), "Null path should throw NullPointerException in factory");
        assertThrows(NullPointerException.class, () -> aScheme().relativeReference(aPath(), (Query<String>) null), "Null query should throw NullPointerException in factory");
    }

    @Test
    void aRelativeReferenceWithPathAndQueryResolvesSchemeToTheBase() {
        final Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        final Authority baseAuthority = anAuthority();
        final AbsolutePath<String> basePath = anAbsolutePath();
        final Path<String> relativeReferencePath = aPath();
        final Query<String> relativeReferenceQuery = aQuery();
        assertThat(
                aScheme().relativeReference(relativeReferencePath, relativeReferenceQuery).resolve(baseScheme, baseAuthority, basePath),
                equalTo(baseScheme.urin(baseAuthority, (AbsolutePath<String>) relativeReferencePath.resolveRelativeTo(basePath), relativeReferenceQuery)));
    }

    @Test
    void aRelativeReferenceWithPathAndQueryResolvesSchemeAndAuthorityAndQueryToTheBase() {
        final Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        final Authority baseAuthority = anAuthority();
        final AbsolutePath<String> basePath = anAbsolutePath();
        final Query<String> baseQuery = aQuery();
        final Path<String> relativeReferencePath = aPath();
        final Query<String> relativeReferenceQuery = aQuery();
        assertThat(
                aScheme().relativeReference(relativeReferencePath, relativeReferenceQuery).resolve(baseScheme, baseAuthority, basePath, baseQuery),
                equalTo(baseScheme.urin(baseAuthority, (AbsolutePath<String>) relativeReferencePath.resolveRelativeTo(basePath), relativeReferenceQuery)));
    }

    @Test
    void aRelativeReferenceWithPathAndQueryResolvesSchemeAndAuthorityAndQueryAndFragmentToTheBase() {
        final Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        final Authority baseAuthority = anAuthority();
        final AbsolutePath<String> basePath = anAbsolutePath();
        final Query<String> baseQuery = aQuery();
        final Fragment<String> baseFragment = aFragment();
        final Path<String> relativeReferencePath = aPath();
        final Query<String> relativeReferenceQuery = aQuery();
        assertThat(
                aScheme().relativeReference(relativeReferencePath, relativeReferenceQuery).resolve(baseScheme, baseAuthority, basePath, baseQuery, baseFragment),
                equalTo(baseScheme.urin(baseAuthority, (AbsolutePath<String>) relativeReferencePath.resolveRelativeTo(basePath), relativeReferenceQuery)));
    }

    @Test
    void aRelativeReferenceWithPathAndFragmentAsStringIsCorrect() {
        final Path<String> path = aPath();
        final Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(path, fragment).asString(), equalTo(path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON) + "#" + fragment.asString()));
        assertThat(aScheme().relativeReference(path, fragment).asUri(), equalTo(URI.create(path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON) + "#" + fragment.asString())));
    }

    @Test
    void aRelativeReferenceWithPathAndFragmentAuthorityIsCorrect() {
        final RelativeReference<?, ?, ?> relativeReference = aScheme().relativeReference(aPath(), aFragment());
        assertThat(relativeReference.hasAuthority(), equalTo(false));
        assertThrows(UnsupportedOperationException.class, relativeReference::authority, "Attempt to get authority from a UrinReference that does not have one.");
    }

    @Test
    void aRelativeReferenceWithPathAndFragmentPathIsCorrect() {
        final Path<String> path = aPath();
        final Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(path, fragment).path(), equalTo(path));
    }

    @Test
    void aRelativeReferenceWithPathAndFragmentQueryIsCorrect() {
        final Path<String> path = aPath();
        final Fragment<String> fragment = aFragment();
        final RelativeReference<?, ?, ?> relativeReference = aScheme().relativeReference(path, fragment);
        assertThat(relativeReference.hasQuery(), equalTo(false));
        assertThrows(UnsupportedOperationException.class, relativeReference::query, "Attempt to get query from a UrinReference that does not have one.");
    }

    @Test
    void aRelativeReferenceWithPathAndFragmentFragmentIsCorrect() {
        final Path<String> path = aPath();
        final Fragment<String> fragment = aFragment();
        final RelativeReference<?, ?, Fragment<String>> relativeReference = aScheme().relativeReference(path, fragment);
        assertThat(relativeReference.hasFragment(), equalTo(true));
        assertThat(relativeReference.fragment(), equalTo(fragment));
    }

    @Test
    void aRelativeReferenceWithPathAndFragmentIsEqualToAnotherRelativeReferenceWithPathAndFragment() {
        final Path<String> path = aPath();
        final Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(path, fragment), equalTo(aScheme().relativeReference(path, fragment)));
        assertThat(aScheme().relativeReference(path, fragment).hashCode(), equalTo(aScheme().relativeReference(path, fragment).hashCode()));
    }

    @Test
    void aRelativeReferenceWithPathAndFragmentIsNotEqualToAnotherWithTheADifferentPath() {
        final Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(aPath(), fragment), not(equalTo(aScheme().relativeReference(aPath(), fragment))));
    }

    @Test
    void aRelativeReferenceWithPathAndFragmentIsNotEqualToAnotherWithTheADifferentFragment() {
        final Path<String> path = aPath();
        assertThat(aScheme().relativeReference(path, aFragment()), not(equalTo(aScheme().relativeReference(path, aFragment()))));
    }

    @Test
    void aRelativeReferenceWithPathAndFragmentToStringIsCorrect() {
        final Path<String> path = aPath();
        final Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(path, fragment).toString(), equalTo(aScheme().relativeReference(path, fragment).asString()));
    }

    @Test
    void aRelativeReferenceWithPathAndFragmentReplacedWithANewPathIsCorrect() {
        final Path<String> path = aPath();
        final Fragment<String> fragment = aFragment();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(aScheme().relativeReference(path, fragment).withPath(newPath), equalTo(aScheme().relativeReference(newPath, fragment)));
    }

    @Test
    void rejectsNullInFactoryForARelativeReferenceWithPathAndFragment() {
        assertThrows(NullPointerException.class, () -> aScheme().relativeReference((Path<String>) null, aFragment()), "Null path should throw NullPointerException in factory");
        assertThrows(NullPointerException.class, () -> aScheme().relativeReference(aPath(), (Fragment<String>) null), "Null fragment should throw NullPointerException in factory");
    }

    @Test
    void aRelativeReferenceWithPathAndFragmentResolvesSchemeToTheBase() {
        final Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        final Authority baseAuthority = anAuthority();
        final AbsolutePath<String> basePath = anAbsolutePath();
        final Path<String> relativeReferencePath = aPath();
        final Fragment<String> relativeReferenceFragment = aFragment();
        assertThat(
                aScheme().relativeReference(relativeReferencePath, relativeReferenceFragment).resolve(baseScheme, baseAuthority, basePath),
                equalTo(baseScheme.urin(baseAuthority, (AbsolutePath<String>) relativeReferencePath.resolveRelativeTo(basePath), relativeReferenceFragment)));
    }

    @Test
    void aRelativeReferenceWithPathAndFragmentResolvesSchemeAndAuthorityAndQueryToTheBase() {
        final Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        final Authority baseAuthority = anAuthority();
        final AbsolutePath<String> basePath = anAbsolutePath();
        final Query<String> baseQuery = aQuery();
        final Path<String> relativeReferencePath = aPath();
        final Fragment<String> relativeReferenceFragment = aFragment();
        assertThat(
                aScheme().relativeReference(relativeReferencePath, relativeReferenceFragment).resolve(baseScheme, baseAuthority, basePath, baseQuery),
                equalTo(baseScheme.urin(baseAuthority, (AbsolutePath<String>) relativeReferencePath.resolveRelativeTo(basePath), relativeReferenceFragment)));
    }

    @Test
    void aRelativeReferenceWithPathAndFragmentResolvesSchemeAndAuthorityAndQueryAndFragmentToTheBase() {
        final Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        final Authority baseAuthority = anAuthority();
        final AbsolutePath<String> basePath = anAbsolutePath();
        final Query<String> baseQuery = aQuery();
        final Fragment<String> baseFragment = aFragment();
        final Path<String> relativeReferencePath = aPath();
        final Fragment<String> relativeReferenceFragment = aFragment();
        assertThat(
                aScheme().relativeReference(relativeReferencePath, relativeReferenceFragment).resolve(baseScheme, baseAuthority, basePath, baseQuery, baseFragment),
                equalTo(baseScheme.urin(baseAuthority, (AbsolutePath<String>) relativeReferencePath.resolveRelativeTo(basePath), relativeReferenceFragment)));
    }

    @Test
    void aRelativeReferenceWithPathAndQueryAndFragmentAsStringIsCorrect() {
        final Path<String> path = aPath();
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(path, query, fragment).asString(), equalTo(path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON) + "?" + query.asString() + "#" + fragment.asString()));
        assertThat(aScheme().relativeReference(path, query, fragment).asUri(), equalTo(URI.create(path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON) + "?" + query.asString() + "#" + fragment.asString())));
    }

    @Test
    void aRelativeReferenceWithPathAndQueryAndFragmentAuthorityIsCorrect() {
        final RelativeReference<?, ?, ?> relativeReference = aScheme().relativeReference(aPath(), aQuery(), aFragment());
        assertThat(relativeReference.hasAuthority(), equalTo(false));
        assertThrows(UnsupportedOperationException.class, relativeReference::authority, "Attempt to get authority from a UrinReference that does not have one.");

    }

    @Test
    void aRelativeReferenceWithPathAndQueryAndFragmentPathIsCorrect() {
        final Path<String> path = aPath();
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(path, query, fragment).path(), equalTo(path));
    }

    @Test
    void aRelativeReferenceWithPathAndQueryAndFragmentQueryIsCorrect() {
        final Path<String> path = aPath();
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        final RelativeReference<String, Query<String>, Fragment<String>> relativeReference = aScheme().relativeReference(path, query, fragment);
        assertThat(relativeReference.hasQuery(), equalTo(true));
        assertThat(relativeReference.query(), equalTo(query));
    }

    @Test
    void aRelativeReferenceWithPathAndQueryAndFragmentFragmentIsCorrect() {
        final Path<String> path = aPath();
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        final RelativeReference<?, ?, Fragment<String>> relativeReference = aScheme().relativeReference(path, query, fragment);
        assertThat(relativeReference.hasFragment(), equalTo(true));
        assertThat(relativeReference.fragment(), equalTo(fragment));
    }

    @Test
    void aRelativeReferenceWithPathAndQueryAndFragmentIsEqualToAnotherRelativeReferenceWithPathAndFragment() {
        final Path<String> path = aPath();
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(path, query, fragment), equalTo(aScheme().relativeReference(path, query, fragment)));
        assertThat(aScheme().relativeReference(path, query, fragment).hashCode(), equalTo(aScheme().relativeReference(path, query, fragment).hashCode()));
    }

    @Test
    void aRelativeReferenceWithPathAndQueryAndFragmentIsNotEqualToAnotherWithTheADifferentPath() {
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(aPath(), query, fragment), not(equalTo(aScheme().relativeReference(aPath(), query, fragment))));
    }

    @Test
    void aRelativeReferenceWithPathAndQueryAndFragmentIsNotEqualToAnotherWithTheADifferentQuery() {
        final Path<String> path = aPath();
        final Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(path, aQuery(), fragment), not(equalTo(aScheme().relativeReference(path, aQuery(), fragment))));
    }

    @Test
    void aRelativeReferenceWithPathAndQueryAndFragmentIsNotEqualToAnotherWithTheADifferentFragment() {
        final Path<String> path = aPath();
        final Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(path, query, aFragment()), not(equalTo(aScheme().relativeReference(path, query, aFragment()))));
    }

    @Test
    void aRelativeReferenceWithPathAndQueryAndFragmentToStringIsCorrect() {
        final Path<String> path = aPath();
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(path, query, fragment).toString(), equalTo(aScheme().relativeReference(path, query, fragment).asString()));
    }

    @Test
    void aRelativeReferenceWithPathAndQueryAndFragmentReplacedWithANewPathIsCorrect() {
        final Path<String> path = aPath();
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(aScheme().relativeReference(path, query, fragment).withPath(newPath), equalTo(aScheme().relativeReference(newPath, query, fragment)));
    }

    @Test
    void rejectsNullInFactoryForARelativeReferenceWithPathAndQueryAndFragment() {
        assertThrows(NullPointerException.class, () -> aScheme().relativeReference((Path<String>) null, aQuery(), aFragment()), "Null path should throw NullPointerException in factory");
        assertThrows(NullPointerException.class, () -> aScheme().relativeReference(aPath(), null, aFragment()), "Null query should throw NullPointerException in factory");
        assertThrows(NullPointerException.class, () -> aScheme().relativeReference(aPath(), aQuery(), null), "Null fragment should throw NullPointerException in factory");
    }

    @Test
    void aRelativeReferenceWithPathAndQueryAndFragmentResolvesSchemeToTheBase() {
        final Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        final Authority baseAuthority = anAuthority();
        final AbsolutePath<String> basePath = anAbsolutePath();
        final Path<String> relativeReferencePath = aPath();
        final Query<String> relativeReferenceQuery = aQuery();
        final Fragment<String> relativeReferenceFragment = aFragment();
        assertThat(
                aScheme().relativeReference(relativeReferencePath, relativeReferenceQuery, relativeReferenceFragment).resolve(baseScheme, baseAuthority, basePath),
                equalTo(baseScheme.urin(baseAuthority, (AbsolutePath<String>) relativeReferencePath.resolveRelativeTo(basePath), relativeReferenceQuery, relativeReferenceFragment)));
    }

    @Test
    void aRelativeReferenceWithPathAndQueryAndFragmentResolvesSchemeAndAuthorityAndQueryToTheBase() {
        final Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        final Authority baseAuthority = anAuthority();
        final AbsolutePath<String> basePath = anAbsolutePath();
        final Query<String> baseQuery = aQuery();
        final Path<String> relativeReferencePath = aPath();
        final Query<String> relativeReferenceQuery = aQuery();
        final Fragment<String> relativeReferenceFragment = aFragment();
        assertThat(
                aScheme().relativeReference(relativeReferencePath, relativeReferenceQuery, relativeReferenceFragment).resolve(baseScheme, baseAuthority, basePath, baseQuery),
                equalTo(baseScheme.urin(baseAuthority, (AbsolutePath<String>) relativeReferencePath.resolveRelativeTo(basePath), relativeReferenceQuery, relativeReferenceFragment)));
    }

    @Test
    void aRelativeReferenceWithPathAndQueryAndFragmentResolvesSchemeAndAuthorityAndQueryAndFragmentToTheBase() {
        final Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        final Authority baseAuthority = anAuthority();
        final AbsolutePath<String> basePath = anAbsolutePath();
        final Query<String> baseQuery = aQuery();
        final Fragment<String> baseFragment = aFragment();
        final Path<String> relativeReferencePath = aPath();
        final Query<String> relativeReferenceQuery = aQuery();
        final Fragment<String> relativeReferenceFragment = aFragment();
        assertThat(
                aScheme().relativeReference(relativeReferencePath, relativeReferenceQuery, relativeReferenceFragment).resolve(baseScheme, baseAuthority, basePath, baseQuery, baseFragment),
                equalTo(baseScheme.urin(baseAuthority, (AbsolutePath<String>) relativeReferencePath.resolveRelativeTo(basePath), relativeReferenceQuery, relativeReferenceFragment)));
    }

    @Test
    void aSimpleAbsolutePathAsStringReturnsThePath() {
        final Segment<String> firstSegment = aNonDotSegment();
        final Segment<String> secondSegment = aNonDotSegment();
        assertThat(aScheme().relativeReference(Path.path(firstSegment, secondSegment)).asString(), equalTo("/" + firstSegment.asString() + "/" + secondSegment.asString()));
        assertThat(aScheme().relativeReference(Path.path(firstSegment, secondSegment)).asUri(), equalTo(URI.create("/" + firstSegment.asString() + "/" + secondSegment.asString())));
    }

    @Test
    void aSimpleAbsolutePathPrefixesAnEmptyFirstSegmentWithADotSegment() {
        final Segment<String> firstSegment = segment("");
        final Segment<String> secondSegment = aNonDotSegment();
        assertThat(aScheme().relativeReference(Path.path(firstSegment, secondSegment)).asString(), equalTo("/./" + firstSegment.asString() + "/" + secondSegment.asString()));
    }

    @Test
    @SuppressWarnings("ConstantValue")
    void rejectsNullInFactoryForASimplePath() {
        assertThrows(NullPointerException.class, () -> {
final             Path<String> path = null;
            aScheme().relativeReference(path);
        }, "Null first segment should throw NullPointerException in factory");
    }

    @Test
    void aSimpleAbsolutePathIsEqualToAnotherWithTheSamePath() {
        final Path<String> path = aPath();
        assertThat(aScheme().relativeReference(path), equalTo(aScheme().relativeReference(path)));
        assertThat(aScheme().relativeReference(path).hashCode(), equalTo(aScheme().relativeReference(path).hashCode()));
    }

    @Test
    void aSimpleAbsolutePathIsNotEqualToAnotherWithTheADifferentPath() {
        assertThat(aScheme().relativeReference(aPath()), not(equalTo(aScheme().relativeReference(aPath()))));
    }

    @Test
    void aSimpleAbsolutePathAuthorityIsCorrect() {
        final RelativeReference<?, ?, ?> relativeReference = aScheme().relativeReference(aPath());
        assertThat(relativeReference.hasAuthority(), equalTo(false));
        assertThrows(UnsupportedOperationException.class, relativeReference::authority, "Attempt to get authority from a UrinReference that does not have one.");

    }

    @Test
    void aSimpleAbsolutePathPathIsCorrect() {
        final Path<String> path = aPath();
        assertThat(aScheme().relativeReference(path).path(), equalTo(path));
    }

    @Test
    void aSimpleAbsolutePathQueryIsCorrect() {
        final Path<String> path = aPath();
        final RelativeReference<?, ?, ?> relativeReference = aScheme().relativeReference(path);
        assertThat(relativeReference.hasQuery(), equalTo(false));
        assertThrows(UnsupportedOperationException.class, relativeReference::query, "Attempt to get query from a UrinReference that does not have one.");
    }

    @Test
    void aSimpleAbsolutePathFragmentIsCorrect() {
        final Path<String> path = aPath();
        final RelativeReference<?, ?, ?> relativeReference = aScheme().relativeReference(path);
        assertThat(relativeReference.hasFragment(), equalTo(false));
        assertThrows(UnsupportedOperationException.class, relativeReference::fragment, "Attempt to get fragment from a UrinReference that does not have one.");
    }

    @Test
    void aSimpleAbsolutePathToStringIsCorrect() {
        final Path<String> path = aPath();
        assertThat(aScheme().relativeReference(path).toString(), equalTo(aScheme().relativeReference(path).asString()));
    }

    @Test
    void aSimpleAbsolutePathReplacedWithANewPathIsCorrect() {
        final Path<String> path = aPath();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(aScheme().relativeReference(path).withPath(newPath), equalTo(aScheme().relativeReference(newPath)));
    }

    @Test
    void aSimpleRootlessPathAsStringReturnsThePath() {
        final Segment<String> firstSegment = aNonDotSegment();
        final Segment<String> secondSegment = aNonDotSegment();
        assertThat(aScheme().relativeReference(rootlessPath(firstSegment, secondSegment)).asString(), equalTo(firstSegment.asString() + "/" + secondSegment.asString()));
        assertThat(aScheme().relativeReference(rootlessPath(firstSegment, secondSegment)).asUri(), equalTo(URI.create(firstSegment.asString() + "/" + secondSegment.asString())));
    }

    @Test
    void aSimpleRootlessPathRejectsAnEmptyFirstSegment() {
        final Segment<String> firstSegment = segment("");
        final Segment<String> secondSegment = aNonDotSegment();
        assertThat(aScheme().relativeReference(rootlessPath(firstSegment, secondSegment)).asString(), equalTo("./" + firstSegment.asString() + "/" + secondSegment.asString()));
    }

    @Test
    void aSimpleRootlessPathPrependsAColonInFirstSegmentWithDotSlash() {
        final Segment<String> firstSegment = segment(aStringIncluding(':'));
        final Segment<String> secondSegment = aSegment();
        assertThat(aScheme().relativeReference(rootlessPath(firstSegment, secondSegment)), equalTo(aScheme().relativeReference(rootlessPath(dot(), firstSegment, secondSegment))));
    }

    @Test
    void aSimpleRootlessPathPermitsAColonInTrailingSegments() {
        aScheme().relativeReference(rootlessPath(aSegment(), segment(aStringIncluding(':')))).asString();
    }

    @Test
    void aSimpleRootlessPathIsEqualToAnotherWithTheSamePath() {
        final Path<String> path = aPath();
        assertThat(aScheme().relativeReference(path), equalTo(aScheme().relativeReference(path)));
        assertThat(aScheme().relativeReference(path).hashCode(), equalTo(aScheme().relativeReference(path).hashCode()));
    }

    @Test
    void aSimpleRootlessPathIsNotEqualToAnotherWithTheADifferentPath() {
        assertThat(aScheme().relativeReference(aPath()), not(equalTo(aScheme().relativeReference(aPath()))));
    }

    @Test
    void aSimpleRootlessPathAuthorityIsCorrect() {
        final RelativeReference<?, ?, ?> relativeReference = aScheme().relativeReference(aPath());
        assertThat(relativeReference.hasAuthority(), equalTo(false));
        assertThrows(UnsupportedOperationException.class, relativeReference::authority, "Attempt to get authority from a UrinReference that does not have one.");
    }

    @Test
    void aSimpleRootlessPathPathIsCorrect() {
        final Path<String> path = aPath();
        assertThat(aScheme().relativeReference(path).path(), equalTo(path));
    }

    @Test
    void aSimpleRootlessPathQueryIsCorrect() {
        final RelativeReference<?, ?, ?> relativeReference = aScheme().relativeReference(aPath());
        assertThat(relativeReference.hasQuery(), equalTo(false));
        assertThrows(UnsupportedOperationException.class, relativeReference::query, "Attempt to get query from a UrinReference that does not have one.");
    }

    @Test
    void aSimpleRootlessPathFragmentIsCorrect() {
        final RelativeReference<?, ?, ?> relativeReference = aScheme().relativeReference(aPath());
        assertThat(relativeReference.hasFragment(), equalTo(false));
        assertThrows(UnsupportedOperationException.class, relativeReference::fragment, "Attempt to get fragment from a UrinReference that does not have one.");
    }

    @Test
    void aSimpleRootlessPathToStringIsCorrect() {
        final Path<String> path = aPath();
        assertThat(aScheme().relativeReference(path).toString(), equalTo(aScheme().relativeReference(path).asString()));
    }

    @Test
    void aSimpleRootlessPathReplacedWithANewPathIsCorrect() {
        final Path<String> path = aPath();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(aScheme().relativeReference(path).withPath(newPath), equalTo(aScheme().relativeReference(newPath)));
    }

    @Test
    void aRelativeReferenceWithOnlyPathResolvesSchemeAndAuthorityToTheBase() {
        final Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        final Authority baseAuthority = anAuthority();
        final AbsolutePath<String> basePath = anAbsolutePath();
        final Path<String> relativeReferencePath = aPath();
        assertThat(
                aScheme().relativeReference(relativeReferencePath).resolve(baseScheme, baseAuthority, basePath),
                equalTo(baseScheme.urin(baseAuthority, (AbsolutePath<String>) relativeReferencePath.resolveRelativeTo(basePath))));
    }

    @Test
    void aRelativeReferenceWithOnlyPathResolvesSchemeAndAuthorityAndQueryToTheBase() {
        final Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        final Authority baseAuthority = anAuthority();
        final AbsolutePath<String> basePath = anAbsolutePath();
        final Path<String> relativeReferencePath = aPath();
        final Query<String> baseQuery = aQuery();
        assertThat(
                aScheme().relativeReference(relativeReferencePath).resolve(baseScheme, baseAuthority, basePath, baseQuery),
                equalTo(baseScheme.urin(baseAuthority, (AbsolutePath<String>) relativeReferencePath.resolveRelativeTo(basePath))));
    }

    @Test
    void aRelativeReferenceWithOnlyPathResolvesSchemeAndAuthorityAndQueryAndFragmentToTheBase() {
        final Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        final Authority baseAuthority = anAuthority();
        final AbsolutePath<String> basePath = anAbsolutePath();
        final Path<String> relativeReferencePath = aPath();
        final Query<String> baseQuery = aQuery();
        final Fragment<String> baseFragment = aFragment();
        assertThat(
                aScheme().relativeReference(relativeReferencePath).resolve(baseScheme, baseAuthority, basePath, baseQuery, baseFragment),
                equalTo(baseScheme.urin(baseAuthority, (AbsolutePath<String>) relativeReferencePath.resolveRelativeTo(basePath))));
    }

    @Test
    void makesRelativeReferenceWithAuthorityAndEmptyPath() {
        final Authority authority = anAuthority();
        assertThat(aScheme().relativeReference(authority).asString(), equalTo("//" + authority.asString()));
    }

    @Test
    void rejectsNullInFactoryForRelativeReferenceWithAuthorityAndEmptyPath() {
        assertThrows(NullPointerException.class, () -> aScheme().relativeReference((Authority) null), "Null authority should throw NullPointerException in factory");
    }

    @Test
    void aRelativeReferenceWithAuthorityAndEmptyPathIsEqualToAnotherWithTheSameAuthority() {
        final Authority authority = anAuthority();
        assertThat(aScheme().relativeReference(authority), equalTo(aScheme().relativeReference(authority)));
        assertThat(aScheme().relativeReference(authority).hashCode(), equalTo(aScheme().relativeReference(authority).hashCode()));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndEmptyPathIsNotEqualToAnotherWithTheADifferentAuthority() {
        assertThat(aScheme().relativeReference(anAuthority()), not(equalTo(aScheme().relativeReference(anAuthority()))));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndEmptyPathAuthorityIsCorrect() {
        final Authority authority = anAuthority();
        final RelativeReference<?, ?, ?> relativeReference = aScheme().relativeReference(authority);
        assertThat(relativeReference.hasAuthority(), equalTo(true));
        assertThat(relativeReference.authority(), equalTo(authority));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndEmptyPathPathIsCorrect() {
        final Authority authority = anAuthority();
        assertThat(aScheme().relativeReference(authority).path(), equalTo(Path.<String>rootlessPath()));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndEmptyPathQueryIsCorrect() {
        final Authority authority = anAuthority();
        final RelativeReference<?, ?, ?> relativeReference = aScheme().relativeReference(authority);
        assertThat(relativeReference.hasQuery(), equalTo(false));
        assertThrows(UnsupportedOperationException.class, relativeReference::query, "Attempt to get query from a UrinReference that does not have one.");
    }

    @Test
    void aRelativeReferenceWithAuthorityAndEmptyPathFragmentIsCorrect() {
        final Authority authority = anAuthority();
        final RelativeReference<?, ?, ?> relativeReference = aScheme().relativeReference(authority);
        assertThat(relativeReference.hasFragment(), equalTo(false));
        assertThrows(UnsupportedOperationException.class, relativeReference::fragment, "Attempt to get fragment from a UrinReference that does not have one.");
    }

    @Test
    void aRelativeReferenceWithAuthorityAndEmptyPathToStringIsCorrect() {
        final Authority authority = anAuthority();
        assertThat(aScheme().relativeReference(authority).toString(), equalTo(aScheme().relativeReference(authority).asString()));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndEmptyPathReplacedWithANewPathIsCorrect() {
        final Authority authority = anAuthority();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(aScheme().relativeReference(authority).withPath(newPath), equalTo(aScheme().relativeReference(authority, newPath)));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndEmptyPathResolvesSchemeToTheBase() {
        final Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        final Authority baseAuthority = anAuthority();
        final AbsolutePath<String> basePath = anAbsolutePath();
        final Authority relativeReferenceAuthority = anAuthority();
        assertThat(
                aScheme().relativeReference(relativeReferenceAuthority).resolve(baseScheme, baseAuthority, basePath),
                equalTo(baseScheme.urin(relativeReferenceAuthority)));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndEmptyPathResolvesSchemeAndAuthorityAndQueryToTheBase() {
        final Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        final Authority baseAuthority = anAuthority();
        final AbsolutePath<String> basePath = anAbsolutePath();
        final Query<String> baseQuery = aQuery();
        final Authority relativeReferenceAuthority = anAuthority();
        assertThat(
                aScheme().relativeReference(relativeReferenceAuthority).resolve(baseScheme, baseAuthority, basePath, baseQuery),
                equalTo(baseScheme.urin(relativeReferenceAuthority)));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndEmptyPathResolvesSchemeAndAuthorityAndQueryAndFragmentToTheBase() {
        final Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        final Authority baseAuthority = anAuthority();
        final AbsolutePath<String> basePath = anAbsolutePath();
        final Query<String> baseQuery = aQuery();
        final Fragment<String> baseFragment = aFragment();
        final Authority relativeReferenceAuthority = anAuthority();
        assertThat(
                aScheme().relativeReference(relativeReferenceAuthority).resolve(baseScheme, baseAuthority, basePath, baseQuery, baseFragment),
                equalTo(baseScheme.urin(relativeReferenceAuthority)));
    }

    @Test
    void makesRelativeReferenceWithAuthorityAndQuery() {
        final Authority authority = anAuthority();
        final Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(authority, query).asString(), equalTo("//" + authority.asString() + "?" + query.asString()));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndQueryIsEqualToAnotherWithTheSameAuthorityAndQuery() {
        final Authority authority = anAuthority();
        final Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(authority, query), equalTo(aScheme().relativeReference(authority, query)));
        assertThat(aScheme().relativeReference(authority, query).hashCode(), equalTo(aScheme().relativeReference(authority, query).hashCode()));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndQueryIsNotEqualToAnotherWithTheADifferentAuthority() {
        final Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(anAuthority(), query), not(equalTo(aScheme().relativeReference(anAuthority(), query))));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndQueryIsNotEqualToAnotherWithTheADifferentQuery() {
        final Authority authority = anAuthority();
        assertThat(aScheme().relativeReference(authority, aQuery()), not(equalTo(aScheme().relativeReference(authority, aQuery()))));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndQueryAuthorityIsCorrect() {
        final Authority authority = anAuthority();
        final Query<String> query = aQuery();
        final RelativeReference<?, ?, ?> relativeReference = aScheme().relativeReference(authority, query);
        assertThat(relativeReference.hasAuthority(), equalTo(true));
        assertThat(relativeReference.authority(), equalTo(authority));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndQueryPathIsCorrect() {
        final Authority authority = anAuthority();
        final Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(authority, query).path(), equalTo(Path.<String>rootlessPath()));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndQueryQueryIsCorrect() {
        final Authority authority = anAuthority();
        final Query<String> query = aQuery();
        final RelativeReference<String, Query<String>, Fragment<String>> relativeReference = aScheme().relativeReference(authority, query);
        assertThat(relativeReference.hasQuery(), equalTo(true));
        assertThat(relativeReference.query(), equalTo(query));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndQueryFragmentIsCorrect() {
        final Authority authority = anAuthority();
        final Query<String> query = aQuery();
        final RelativeReference<?, ?, ?> relativeReference = aScheme().relativeReference(authority, query);
        assertThat(relativeReference.hasFragment(), equalTo(false));
        assertThrows(UnsupportedOperationException.class, relativeReference::fragment, "Attempt to get fragment from a UrinReference that does not have one.");
    }

    @Test
    void aRelativeReferenceWithAuthorityAndQueryToStringIsCorrect() {
        final Authority authority = anAuthority();
        final Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(authority, query).toString(), equalTo(aScheme().relativeReference(authority, query).asString()));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndQueryReplacedWithANewPathIsCorrect() {
        final Authority authority = anAuthority();
        final Query<String> query = aQuery();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(aScheme().relativeReference(authority, query).withPath(newPath), equalTo(aScheme().relativeReference(authority, newPath, query)));
    }

    @Test
    void rejectsNullInFactoryForRelativeReferenceWithAuthorityAndQuery() {
        assertThrows(NullPointerException.class, () -> aScheme().relativeReference((Authority) null, aQuery()), "Null authority should throw NullPointerException in factory");
        assertThrows(NullPointerException.class, () -> aScheme().relativeReference(anAuthority(), (Query<String>) null), "Null query should throw NullPointerException in factory");
    }

    @Test
    void aRelativeReferenceWithAuthorityAndQueryResolvesSchemeToTheBase() {
        final Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        final Authority baseAuthority = anAuthority();
        final AbsolutePath<String> basePath = anAbsolutePath();
        final Authority relativeReferenceAuthority = anAuthority();
        final Query<String> relativeReferenceQuery = aQuery();
        assertThat(
                aScheme().relativeReference(relativeReferenceAuthority, relativeReferenceQuery).resolve(baseScheme, baseAuthority, basePath),
                equalTo(baseScheme.urin(relativeReferenceAuthority, relativeReferenceQuery)));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndQueryResolvesSchemeAndAuthorityToTheBase() {
        final Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        final Authority baseAuthority = anAuthority();
        final AbsolutePath<String> basePath = anAbsolutePath();
        final Query<String> baseQuery = aQuery();
        final Authority relativeReferenceAuthority = anAuthority();
        final Query<String> relativeReferenceQuery = aQuery();
        assertThat(
                aScheme().relativeReference(relativeReferenceAuthority, relativeReferenceQuery).resolve(baseScheme, baseAuthority, basePath, baseQuery),
                equalTo(baseScheme.urin(relativeReferenceAuthority, relativeReferenceQuery)));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndQueryResolvesSchemeAndAuthorityAndQueryAndFragmentToTheBase() {
        final Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        final Authority baseAuthority = anAuthority();
        final AbsolutePath<String> basePath = anAbsolutePath();
        final Query<String> baseQuery = aQuery();
        final Fragment<String> baseFragment = aFragment();
        final Authority relativeReferenceAuthority = anAuthority();
        final Query<String> relativeReferenceQuery = aQuery();
        assertThat(
                aScheme().relativeReference(relativeReferenceAuthority, relativeReferenceQuery).resolve(baseScheme, baseAuthority, basePath, baseQuery, baseFragment),
                equalTo(baseScheme.urin(relativeReferenceAuthority, relativeReferenceQuery)));
    }

    @Test
    void makesRelativeReferenceWithAuthorityAndFragment() {
        final Authority authority = anAuthority();
        final Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(authority, fragment).asString(), equalTo("//" + authority.asString() + "#" + fragment.asString()));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndFragmentIsEqualToAnotherWithTheSameAuthorityAndFragment() {
        final Authority authority = anAuthority();
        final Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(authority, fragment), equalTo(aScheme().relativeReference(authority, fragment)));
        assertThat(aScheme().relativeReference(authority, fragment).hashCode(), equalTo(aScheme().relativeReference(authority, fragment).hashCode()));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndFragmentIsNotEqualToAnotherWithTheADifferentAuthority() {
        final Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(anAuthority(), fragment), not(equalTo(aScheme().relativeReference(anAuthority(), fragment))));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndFragmentIsNotEqualToAnotherWithTheADifferentFragment() {
        final Authority authority = anAuthority();
        assertThat(aScheme().relativeReference(authority, aFragment()), not(equalTo(aScheme().relativeReference(authority, aFragment()))));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndFragmentAuthorityIsCorrect() {
        final Authority authority = anAuthority();
        final Fragment<String> fragment = aFragment();
        final RelativeReference<?, ?, ?> relativeReference = aScheme().relativeReference(authority, fragment);
        assertThat(relativeReference.hasAuthority(), equalTo(true));
        assertThat(relativeReference.authority(), equalTo(authority));

    }

    @Test
    void aRelativeReferenceWithAuthorityAndFragmentPathIsCorrect() {
        final Authority authority = anAuthority();
        final Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(authority, fragment).path(), equalTo(Path.<String>rootlessPath()));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndFragmentQueryIsCorrect() {
        final Authority authority = anAuthority();
        final Fragment<String> fragment = aFragment();
        final RelativeReference<?, ?, ?> relativeReference = aScheme().relativeReference(authority, fragment);
        assertThat(relativeReference.hasQuery(), equalTo(false));
        assertThrows(UnsupportedOperationException.class, relativeReference::query, "Attempt to get query from a UrinReference that does not have one.");
    }

    @Test
    void aRelativeReferenceWithAuthorityAndFragmentFragmentIsCorrect() {
        final Authority authority = anAuthority();
        final Fragment<String> fragment = aFragment();
        final RelativeReference<?, ?, Fragment<String>> relativeReference = aScheme().relativeReference(authority, fragment);
        assertThat(relativeReference.hasFragment(), equalTo(true));
        assertThat(relativeReference.fragment(), equalTo(fragment));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndFragmentToStringIsCorrect() {
        final Authority authority = anAuthority();
        final Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(authority, fragment).toString(), equalTo(aScheme().relativeReference(authority, fragment).asString()));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndFragmentReplacedWithANewPathIsCorrect() {
        final Authority authority = anAuthority();
        final Fragment<String> fragment = aFragment();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(aScheme().relativeReference(authority, fragment).withPath(newPath), equalTo(aScheme().relativeReference(authority, newPath, fragment)));
    }

    @Test
    void rejectsNullInFactoryForRelativeReferenceWithAuthorityAndFragment() {
        assertThrows(NullPointerException.class, () -> aScheme().relativeReference((Authority) null, aFragment()), "Null authority should throw NullPointerException in factory");
        assertThrows(NullPointerException.class, () -> aScheme().relativeReference(anAuthority(), (Fragment<String>) null), "Null fragment should throw NullPointerException in factory");
    }

    @Test
    void aRelativeReferenceWithAuthorityAndFragmentResolvesSchemeToTheBase() {
        final Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        final Authority baseAuthority = anAuthority();
        final AbsolutePath<String> basePath = anAbsolutePath();
        final Authority relativeReferenceAuthority = anAuthority();
        final Fragment<String> relativeReferenceFragment = aFragment();
        assertThat(
                aScheme().relativeReference(relativeReferenceAuthority, relativeReferenceFragment).resolve(baseScheme, baseAuthority, basePath),
                equalTo(baseScheme.urin(relativeReferenceAuthority, relativeReferenceFragment)));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndFragmentResolvesSchemeAndAuthorityAndQueryToTheBase() {
        final Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        final Authority baseAuthority = anAuthority();
        final AbsolutePath<String> basePath = anAbsolutePath();
        final Query<String> baseQuery = aQuery();
        final Authority relativeReferenceAuthority = anAuthority();
        final Fragment<String> relativeReferenceFragment = aFragment();
        assertThat(
                aScheme().relativeReference(relativeReferenceAuthority, relativeReferenceFragment).resolve(baseScheme, baseAuthority, basePath, baseQuery),
                equalTo(baseScheme.urin(relativeReferenceAuthority, relativeReferenceFragment)));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndFragmentResolvesSchemeAndAuthorityAndQueryAndFragmentToTheBase() {
        final Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        final Authority baseAuthority = anAuthority();
        final AbsolutePath<String> basePath = anAbsolutePath();
        final Query<String> baseQuery = aQuery();
        final Fragment<String> baseFragment = aFragment();
        final Authority relativeReferenceAuthority = anAuthority();
        final Fragment<String> relativeReferenceFragment = aFragment();
        assertThat(
                aScheme().relativeReference(relativeReferenceAuthority, relativeReferenceFragment).resolve(baseScheme, baseAuthority, basePath, baseQuery, baseFragment),
                equalTo(baseScheme.urin(relativeReferenceAuthority, relativeReferenceFragment)));
    }

    @Test
    void makesRelativeReferenceWithAuthorityAndQueryAndFragment() {
        final Authority authority = anAuthority();
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(authority, query, fragment).asString(), equalTo("//" + authority.asString() + "?" + query.asString() + "#" + fragment.asString()));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndQueryAndFragmentIsEqualToAnotherWithTheSameAuthorityAndQueryAndFragment() {
        final Authority authority = anAuthority();
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(authority, query, fragment), equalTo(aScheme().relativeReference(authority, query, fragment)));
        assertThat(aScheme().relativeReference(authority, query, fragment).hashCode(), equalTo(aScheme().relativeReference(authority, query, fragment).hashCode()));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndQueryAndFragmentIsNotEqualToAnotherWithTheADifferentAuthority() {
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(anAuthority(), query, fragment), not(equalTo(aScheme().relativeReference(anAuthority(), query, fragment))));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndQueryAndFragmentIsNotEqualToAnotherWithTheADifferentQuery() {
        final Authority authority = anAuthority();
        final Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(authority, aQuery(), fragment), not(equalTo(aScheme().relativeReference(authority, aQuery(), fragment))));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndQueryAndFragmentIsNotEqualToAnotherWithTheADifferentFragment() {
        final Authority authority = anAuthority();
        final Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(authority, query, aFragment()), not(equalTo(aScheme().relativeReference(authority, query, aFragment()))));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndQueryAndFragmentAuthorityIsCorrect() {
        final Authority authority = anAuthority();
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        final RelativeReference<?, ?, ?> relativeReference = aScheme().relativeReference(authority, query, fragment);
        assertThat(relativeReference.hasAuthority(), equalTo(true));
        assertThat(relativeReference.authority(), equalTo(authority));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndQueryAndFragmentPathIsCorrect() {
        final Authority authority = anAuthority();
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(authority, query, fragment).path(), equalTo(Path.<String>rootlessPath()));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndQueryAndFragmentQueryIsCorrect() {
        final Authority authority = anAuthority();
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        final RelativeReference<String, Query<String>, Fragment<String>> relativeReference = aScheme().relativeReference(authority, query, fragment);
        assertThat(relativeReference.hasQuery(), equalTo(true));
        assertThat(relativeReference.query(), equalTo(query));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndQueryAndFragmentFragmentIsCorrect() {
        final Authority authority = anAuthority();
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        final RelativeReference<?, ?, Fragment<String>> relativeReference = aScheme().relativeReference(authority, query, fragment);
        assertThat(relativeReference.hasFragment(), equalTo(true));
        assertThat(relativeReference.fragment(), equalTo(fragment));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndQueryAndFragmentToStringIsCorrect() {
        final Authority authority = anAuthority();
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(authority, query, fragment).toString(), equalTo(aScheme().relativeReference(authority, query, fragment).asString()));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndQueryAndFragmentReplacedWithANewPathIsCorrect() {
        final Authority authority = anAuthority();
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(aScheme().relativeReference(authority, query, fragment).withPath(newPath), equalTo(aScheme().relativeReference(authority, newPath, query, fragment)));
    }

    @Test
    @SuppressWarnings("ConstantValue")
    void rejectsNullInFactoryForRelativeReferenceWithAuthorityAndQueryAndFragment() {
        assertThrows(NullPointerException.class, () -> aScheme().relativeReference((Authority) null, aQuery(), aFragment()), "Null authority should throw NullPointerException in factory");
        assertThrows(NullPointerException.class, () -> {
            final Query<String> query = null;
            aScheme().relativeReference(anAuthority(), query, aFragment());
        }, "Null query should throw NullPointerException in factory");
        assertThrows(NullPointerException.class, () -> aScheme().relativeReference(anAuthority(), aQuery(), null), "Null fragment should throw NullPointerException in factory");
    }

    @Test
    void aRelativeReferenceWithAuthorityAndQueryAndFragmentResolvesSchemeToTheBase() {
        final Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        final Authority baseAuthority = anAuthority();
        final AbsolutePath<String> basePath = anAbsolutePath();
        final Authority relativeReferenceAuthority = anAuthority();
        final Query<String> relativeReferenceQuery = aQuery();
        final Fragment<String> relativeReferenceFragment = aFragment();
        assertThat(
                aScheme().relativeReference(relativeReferenceAuthority, relativeReferenceQuery, relativeReferenceFragment).resolve(baseScheme, baseAuthority, basePath),
                equalTo(baseScheme.urin(relativeReferenceAuthority, relativeReferenceQuery, relativeReferenceFragment)));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndQueryAndFragmentResolvesSchemeAndAuthorityAndQueryToTheBase() {
        final Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        final Authority baseAuthority = anAuthority();
        final AbsolutePath<String> basePath = anAbsolutePath();
        final Query<String> baseQuery = aQuery();
        final Authority relativeReferenceAuthority = anAuthority();
        final Query<String> relativeReferenceQuery = aQuery();
        final Fragment<String> relativeReferenceFragment = aFragment();
        assertThat(
                aScheme().relativeReference(relativeReferenceAuthority, relativeReferenceQuery, relativeReferenceFragment).resolve(baseScheme, baseAuthority, basePath, baseQuery),
                equalTo(baseScheme.urin(relativeReferenceAuthority, relativeReferenceQuery, relativeReferenceFragment)));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndQueryAndFragmentResolvesSchemeAndAuthorityAndQueryAndFragmentToTheBase() {
        final Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        final Authority baseAuthority = anAuthority();
        final AbsolutePath<String> basePath = anAbsolutePath();
        final Query<String> baseQuery = aQuery();
        final Fragment<String> baseFragment = aFragment();
        final Authority relativeReferenceAuthority = anAuthority();
        final Query<String> relativeReferenceQuery = aQuery();
        final Fragment<String> relativeReferenceFragment = aFragment();
        assertThat(
                aScheme().relativeReference(relativeReferenceAuthority, relativeReferenceQuery, relativeReferenceFragment).resolve(baseScheme, baseAuthority, basePath, baseQuery, baseFragment),
                equalTo(baseScheme.urin(relativeReferenceAuthority, relativeReferenceQuery, relativeReferenceFragment)));
    }

    @Test
    void makesRelativeReferenceWithAuthorityAndNonEmptyPath() {
        final Authority authority = anAuthority();
        final Segment<String> firstSegment = aNonDotSegment();
        final Segment<String> secondSegment = aNonDotSegment();
        assertThat(aScheme().relativeReference(authority, Path.path(firstSegment, secondSegment)).asString(), equalTo("//" + authority.asString() + "/" + firstSegment.asString() + "/" + secondSegment.asString()));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathIsEqualToAnotherWithTheSameAuthorityAndPath() {
        final Authority authority = anAuthority();
        final AbsolutePath<String> absolutePath = anAbsolutePath();
        assertThat(aScheme().relativeReference(authority, absolutePath), equalTo(aScheme().relativeReference(authority, absolutePath)));
        assertThat(aScheme().relativeReference(authority, absolutePath).hashCode(), equalTo(aScheme().relativeReference(authority, absolutePath).hashCode()));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathIsNotEqualToAnotherWithTheADifferentAuthorityAndPath() {
        assertThat(aScheme().relativeReference(anAuthority(), anAbsolutePath()), not(equalTo(aScheme().relativeReference(anAuthority(), anAbsolutePath()))));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathAuthorityIsCorrect() {
        final Authority authority = anAuthority();
        final AbsolutePath<String> absolutePath = anAbsolutePath();
        final RelativeReference<String, Query<String>, Fragment<String>> relativeReference = aScheme().relativeReference(authority, absolutePath);
        assertThat(relativeReference.hasAuthority(), equalTo(true));
        assertThat(relativeReference.authority(), equalTo(authority));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathPathIsCorrect() {
        final Authority authority = anAuthority();
        final AbsolutePath<String> absolutePath = anAbsolutePath();
        assertThat(aScheme().relativeReference(authority, absolutePath).path(), equalTo(absolutePath));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathQueryIsCorrect() {
        final Authority authority = anAuthority();
        final AbsolutePath<String> absolutePath = anAbsolutePath();
        final RelativeReference<?, ?, ?> relativeReference = aScheme().relativeReference(authority, absolutePath);
        assertThat(relativeReference.hasQuery(), equalTo(false));
        assertThrows(UnsupportedOperationException.class, relativeReference::query, "Attempt to get query from a UrinReference that does not have one.");
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathFragmentIsCorrect() {
        final Authority authority = anAuthority();
        final AbsolutePath<String> absolutePath = anAbsolutePath();
        final RelativeReference<?, ?, ?> relativeReference = aScheme().relativeReference(authority, absolutePath);
        assertThat(relativeReference.hasFragment(), equalTo(false));
        assertThrows(UnsupportedOperationException.class, relativeReference::fragment, "Attempt to get fragment from a UrinReference that does not have one.");
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathToStringIsCorrect() {
        final Authority authority = anAuthority();
        final AbsolutePath<String> absolutePath = anAbsolutePath();
        assertThat(aScheme().relativeReference(authority, absolutePath).toString(), equalTo(aScheme().relativeReference(authority, absolutePath).asString()));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathReplacedWithANewPathIsCorrect() {
        final Authority authority = anAuthority();
        final AbsolutePath<String> absolutePath = anAbsolutePath();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(aScheme().relativeReference(authority, absolutePath).withPath(newPath), equalTo(aScheme().relativeReference(authority, newPath)));
    }

    @Test
    @SuppressWarnings("ConstantValue")
    void rejectsNullInFactoryForRelativeReferenceWithAuthorityAndPath() {
        assertThrows(NullPointerException.class, () -> {
            final Authority authority = null;
            aScheme().relativeReference(authority, anAbsolutePath());
        }, "Null authority should throw NullPointerException in factory");
        assertThrows(NullPointerException.class, () -> {
            final AbsolutePath<String> absolutePath = null;
            aScheme().relativeReference(anAuthority(), absolutePath);
        }, "Null path should throw NullPointerException in factory");
    }

    @Test
    void makesRelativeReferenceWithAuthorityAndPathAndQuery() {
        final Authority authority = anAuthority();
        final AbsolutePath<String> absolutePath = anAbsolutePath();
        final Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(authority, absolutePath, query).asString(), equalTo("//" + authority.asString() + absolutePath.asString(NEVER_PREFIX_WITH_DOT_SEGMENT) + "?" + query.asString()));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathAndQueryIsEqualToAnotherWithTheSameAuthorityAndPathAndQuery() {
        final Authority authority = anAuthority();
        final AbsolutePath<String> absolutePath = anAbsolutePath();
        final Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(authority, absolutePath, query), equalTo(aScheme().relativeReference(authority, absolutePath, query)));
        assertThat(aScheme().relativeReference(authority, absolutePath, query).hashCode(), equalTo(aScheme().relativeReference(authority, absolutePath, query).hashCode()));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathIsNotEqualToAnotherWithTheADifferentAuthorityAndPathAndQuery() {
        assertThat(aScheme().relativeReference(anAuthority(), anAbsolutePath(), aQuery()), not(equalTo(aScheme().relativeReference(anAuthority(), anAbsolutePath(), aQuery()))));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathAndQueryAuthorityIsCorrect() {
        final Authority authority = anAuthority();
        final AbsolutePath<String> absolutePath = anAbsolutePath();
        final Query<String> query = aQuery();
        final RelativeReference<?, ?, ?> relativeReference = aScheme().relativeReference(authority, absolutePath, query);
        assertThat(relativeReference.hasAuthority(), equalTo(true));
        assertThat(relativeReference.authority(), equalTo(authority));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathAndQueryPathIsCorrect() {
        final Authority authority = anAuthority();
        final AbsolutePath<String> absolutePath = anAbsolutePath();
        final Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(authority, absolutePath, query).path(), equalTo(absolutePath));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathAndQueryQueryIsCorrect() {
        final Authority authority = anAuthority();
        final AbsolutePath<String> absolutePath = anAbsolutePath();
        final Query<String> query = aQuery();
        final RelativeReference<String, Query<String>, Fragment<String>> relativeReference = aScheme().relativeReference(authority, absolutePath, query);
        assertThat(relativeReference.hasQuery(), equalTo(true));
        assertThat(relativeReference.query(), equalTo(query));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathAndQueryFragmentIsCorrect() {
        final Authority authority = anAuthority();
        final AbsolutePath<String> absolutePath = anAbsolutePath();
        final Query<String> query = aQuery();
        final RelativeReference<?, ?, ?> relativeReference = aScheme().relativeReference(authority, absolutePath, query);
        assertThat(relativeReference.hasFragment(), equalTo(false));
        assertThrows(UnsupportedOperationException.class, relativeReference::fragment, "Attempt to get fragment from a UrinReference that does not have one.");
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathAndQueryToStringIsCorrect() {
        final Authority authority = anAuthority();
        final AbsolutePath<String> absolutePath = anAbsolutePath();
        final Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(authority, absolutePath, query).toString(), equalTo(aScheme().relativeReference(authority, absolutePath, query).asString()));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathAndQueryReplacedWithANewPathIsCorrect() {
        final Authority authority = anAuthority();
        final AbsolutePath<String> absolutePath = anAbsolutePath();
        final Query<String> query = aQuery();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(aScheme().relativeReference(authority, absolutePath, query).withPath(newPath), equalTo(aScheme().relativeReference(authority, newPath, query)));
    }

    @Test
    void rejectsNullInFactoryForRelativeReferenceWithAuthorityAndPathAndQuery() {
        assertThrows(NullPointerException.class, () -> aScheme().relativeReference(null, anAbsolutePath(), aQuery()), "Null authority should throw NullPointerException in factory");
        assertThrows(NullPointerException.class, () -> aScheme().relativeReference(anAuthority(), null, aQuery()), "Null path should throw NullPointerException in factory");
        assertThrows(NullPointerException.class, () -> aScheme().relativeReference(anAuthority(), anAbsolutePath(), (Query<String>) null), "Null query should throw NullPointerException in factory");
    }

    @Test
    void makesRelativeReferenceWithAuthorityAndPathAndFragment() {
        final Authority authority = anAuthority();
        final AbsolutePath<String> absolutePath = anAbsolutePath();
        final Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(authority, absolutePath, fragment).asString(), equalTo("//" + authority.asString() + absolutePath.asString(NEVER_PREFIX_WITH_DOT_SEGMENT) + "#" + fragment.asString()));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathAndFragmentIsEqualToAnotherWithTheSameAuthorityAndPathAndFragment() {
        final Authority authority = anAuthority();
        final AbsolutePath<String> absolutePath = anAbsolutePath();
        final Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(authority, absolutePath, fragment), equalTo(aScheme().relativeReference(authority, absolutePath, fragment)));
        assertThat(aScheme().relativeReference(authority, absolutePath, fragment).hashCode(), equalTo(aScheme().relativeReference(authority, absolutePath, fragment).hashCode()));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathIsNotEqualToAnotherWithTheADifferentAuthorityAndPathAndFragment() {
        assertThat(aScheme().relativeReference(anAuthority(), anAbsolutePath(), aFragment()), not(equalTo(aScheme().relativeReference(anAuthority(), anAbsolutePath(), aFragment()))));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathAndFragmentAuthorityIsCorrect() {
        final Authority authority = anAuthority();
        final AbsolutePath<String> absolutePath = anAbsolutePath();
        final Fragment<String> fragment = aFragment();
        final RelativeReference<?, ?, ?> relativeReference = aScheme().relativeReference(authority, absolutePath, fragment);
        assertThat(relativeReference.hasAuthority(), equalTo(true));
        assertThat(relativeReference.authority(), equalTo(authority));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathAndFragmentPathIsCorrect() {
        final Authority authority = anAuthority();
        final AbsolutePath<String> absolutePath = anAbsolutePath();
        final Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(authority, absolutePath, fragment).path(), equalTo(absolutePath));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathAndFragmentQueryIsCorrect() {
        final Authority authority = anAuthority();
        final AbsolutePath<String> absolutePath = anAbsolutePath();
        final Fragment<String> fragment = aFragment();
        final RelativeReference<?, ?, ?> relativeReference = aScheme().relativeReference(authority, absolutePath, fragment);
        assertThat(relativeReference.hasQuery(), equalTo(false));
        assertThrows(UnsupportedOperationException.class, relativeReference::query, "Attempt to get query from a UrinReference that does not have one.");
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathAndFragmentFragmentIsCorrect() {
        final Authority authority = anAuthority();
        final AbsolutePath<String> absolutePath = anAbsolutePath();
        final Fragment<String> fragment = aFragment();
        final RelativeReference<?, ?, Fragment<String>> relativeReference = aScheme().relativeReference(authority, absolutePath, fragment);
        assertThat(relativeReference.hasFragment(), equalTo(true));
        assertThat(relativeReference.fragment(), equalTo(fragment));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathAndFragmentToStringIsCorrect() {
        final Authority authority = anAuthority();
        final AbsolutePath<String> absolutePath = anAbsolutePath();
        final Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(authority, absolutePath, fragment).toString(), equalTo(aScheme().relativeReference(authority, absolutePath, fragment).asString()));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathAndFragmentReplacedWithANewPathIsCorrect() {
        final Authority authority = anAuthority();
        final AbsolutePath<String> absolutePath = anAbsolutePath();
        final Fragment<String> fragment = aFragment();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(aScheme().relativeReference(authority, absolutePath, fragment).withPath(newPath), equalTo(aScheme().relativeReference(authority, newPath, fragment)));
    }

    @Test
    void rejectsNullInFactoryForRelativeReferenceWithAuthorityAndPathAndFragment() {
        assertThrows(NullPointerException.class, () -> aScheme().relativeReference(null, anAbsolutePath(), aFragment()), "Null authority should throw NullPointerException in factory");
        assertThrows(NullPointerException.class, () -> aScheme().relativeReference(anAuthority(), (AbsolutePath<String>) null, aFragment()), "Null path should throw NullPointerException in factory");
        assertThrows(NullPointerException.class, () -> aScheme().relativeReference(anAuthority(), anAbsolutePath(), (Fragment<String>) null), "Null fragment should throw NullPointerException in factory");
    }

    @Test
    void makesRelativeReferenceWithAuthorityAndPathAndQueryAndFragment() {
        final Authority authority = anAuthority();
        final AbsolutePath<String> absolutePath = anAbsolutePath();
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(authority, absolutePath, query, fragment).asString(), equalTo("//" + authority.asString() + absolutePath.asString(NEVER_PREFIX_WITH_DOT_SEGMENT) + "?" + query.asString() + "#" + fragment.asString()));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathAndQueryAndFragmentIsEqualToAnotherWithTheSameAuthorityAndPathAndQueryAndFragment() {
        final Authority authority = anAuthority();
        final AbsolutePath<String> absolutePath = anAbsolutePath();
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(authority, absolutePath, query, fragment), equalTo(aScheme().relativeReference(authority, absolutePath, query, fragment)));
        assertThat(aScheme().relativeReference(authority, absolutePath, query, fragment).hashCode(), equalTo(aScheme().relativeReference(authority, absolutePath, query, fragment).hashCode()));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathIsNotEqualToAnotherWithTheADifferentAuthorityAndPathAndQueryAndFragment() {
        assertThat(aScheme().relativeReference(anAuthority(), anAbsolutePath(), aQuery(), aFragment()), not(equalTo(aScheme().relativeReference(anAuthority(), anAbsolutePath(), aQuery(), aFragment()))));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathAndQueryAndFragmentAuthorityIsCorrect() {
        final Authority authority = anAuthority();
        final AbsolutePath<String> absolutePath = anAbsolutePath();
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        final RelativeReference<?, ?, ?> relativeReference = aScheme().relativeReference(authority, absolutePath, query, fragment);
        assertThat(relativeReference.hasAuthority(), equalTo(true));
        assertThat(relativeReference.authority(), equalTo(authority));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathAndQueryAndFragmentPathIsCorrect() {
        final Authority authority = anAuthority();
        final AbsolutePath<String> absolutePath = anAbsolutePath();
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(authority, absolutePath, query, fragment).path(), equalTo(absolutePath));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathAndQueryAndFragmentQueryIsCorrect() {
        final Authority authority = anAuthority();
        final AbsolutePath<String> absolutePath = anAbsolutePath();
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        final RelativeReference<String, Query<String>, Fragment<String>> relativeReference = aScheme().relativeReference(authority, absolutePath, query, fragment);
        assertThat(relativeReference.hasQuery(), equalTo(true));
        assertThat(relativeReference.query(), equalTo(query));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathAndQueryAndFragmentFragmentIsCorrect() {
        final Authority authority = anAuthority();
        final AbsolutePath<String> absolutePath = anAbsolutePath();
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        final RelativeReference<?, ?, Fragment<String>> relativeReference = aScheme().relativeReference(authority, absolutePath, query, fragment);
        assertThat(relativeReference.hasFragment(), equalTo(true));
        assertThat(relativeReference.fragment(), equalTo(fragment));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathAndQueryAndFragmentToStringIsCorrect() {
        final Authority authority = anAuthority();
        final AbsolutePath<String> absolutePath = anAbsolutePath();
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        assertThat(aScheme().relativeReference(authority, absolutePath, query, fragment).toString(), equalTo(aScheme().relativeReference(authority, absolutePath, query, fragment).asString()));
    }

    @Test
    void aRelativeReferenceWithAuthorityAndPathAndQueryAndFragmentReplacedWithANewPathIsCorrect() {
        final Authority authority = anAuthority();
        final AbsolutePath<String> absolutePath = anAbsolutePath();
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        final AbsolutePath<String> newPath = anAbsolutePath();
        assertThat(aScheme().relativeReference(authority, absolutePath, query, fragment).withPath(newPath), equalTo(aScheme().relativeReference(authority, newPath, query, fragment)));
    }

    @Test
    void rejectsNullInFactoryForRelativeReferenceWithAuthorityAndPathAndQueryAndFragment() {
        assertThrows(NullPointerException.class, () -> aScheme().relativeReference(null, anAbsolutePath(), aQuery(), aFragment()), "Null authority should throw NullPointerException in factory");
        assertThrows(NullPointerException.class, () -> aScheme().relativeReference(anAuthority(), null, aQuery(), aFragment()), "Null path should throw NullPointerException in factory");
        assertThrows(NullPointerException.class, () -> aScheme().relativeReference(anAuthority(), anAbsolutePath(), null, aFragment()), "Null query should throw NullPointerException in factory");
        assertThrows(NullPointerException.class, () -> aScheme().relativeReference(anAuthority(), anAbsolutePath(), aQuery(), null), "Null fragment should throw NullPointerException in factory");
    }

    @Test
    void parsesARelativeReferenceWithEmptyPath() throws Exception {
        assertThat(aScheme().parseRelativeReference(""), equalTo(aScheme().relativeReference()));
    }

    @Test
    void parsesARelativeReferenceWithEmptyPathWithQuery() throws Exception {
        final Query<String> query = aQuery();
        assertThat(aScheme().parseRelativeReference("?" + query.asString()), equalTo(aScheme().relativeReference(query)));
    }

    @Test
    void parsesARelativeReferenceWithEmptyPathWithFragment() throws Exception {
        final Fragment<String> fragment = aFragment();
        assertThat(aScheme().parseRelativeReference("#" + fragment.asString()), equalTo(aScheme().relativeReference(fragment)));
    }

    @Test
    void parsesARelativeReferenceWithEmptyPathWithQueryAndFragment() throws Exception {
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        assertThat(aScheme().parseRelativeReference("?" + query.asString() + "#" + fragment.asString()), equalTo(aScheme().relativeReference(query, fragment)));
    }

    @Test
    void parsesARelativeReferenceWithPathAndQuery() throws Exception {
        final Path<String> path = anUnpollutedPath();
        final Query<String> query = aQuery();
        assertThat(aScheme().parseRelativeReference(path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON) + "?" + query.asString()), equalTo(aScheme().relativeReference(path, query)));
    }

    @Test
    void parsesARelativeReferenceWithPathAndFragment() throws Exception {
        final Path<String> path = anUnpollutedPath();
        final Fragment<String> fragment = aFragment();
        assertThat(aScheme().parseRelativeReference(path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON) + "#" + fragment.asString()), equalTo(aScheme().relativeReference(path, fragment)));
    }

    @Test
    void parsesARelativeReferenceWithPathAndQueryAndFragment() throws Exception {
        final Path<String> path = anUnpollutedPath();
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        assertThat(aScheme().parseRelativeReference(path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON) + "?" + query.asString() + "#" + fragment.asString()), equalTo(aScheme().relativeReference(path, query, fragment)));
    }

    @Test
    void parsesASimpleAbsolutePath() throws Exception {
        final Segment<String> firstSegment = aSegment();
        final Segment<String> secondSegment = aSegment();
        assertThat(aScheme().parseRelativeReference("/" + firstSegment.asString() + "/" + secondSegment.asString()), equalTo(aScheme().relativeReference(Path.path(firstSegment, secondSegment))));
    }

    @Test
    void parsesASimpleAbsolutePathPrefixedWithADotSegment() throws Exception {
        final Segment<String> firstSegment = segment("");
        final Segment<String> secondSegment = aSegment();
        assertThat(aScheme().parseRelativeReference("/./" + firstSegment.asString() + "/" + secondSegment.asString()), equalTo(aScheme().relativeReference(Path.path(firstSegment, secondSegment))));
    }

    @Test
    void parsesASimpleRootlessPath() throws Exception {
        final Segment<String> firstSegment = aSegment();
        final Segment<String> secondSegment = aSegment();
        assertThat(aScheme().parseRelativeReference(firstSegment.asString() + "/" + secondSegment.asString()), equalTo(aScheme().relativeReference(rootlessPath(firstSegment, secondSegment))));
    }

    @Test
    void parsesARelativeReferenceWithAuthorityAndEmptyPath() throws Exception {
        final Authority authority = anAuthority();
        assertThat(aScheme().parseRelativeReference("//" + authority.asString()), equalTo(aScheme().relativeReference(authority)));
    }

    @Test
    void parsesARelativeReferenceWithAuthorityAndQuery() throws Exception {
        final Authority authority = anAuthority();
        final Query<String> query = aQuery();
        assertThat(aScheme().parseRelativeReference("//" + authority.asString() + "?" + query.asString()), equalTo(aScheme().relativeReference(authority, query)));
    }

    @Test
    void parsesARelativeReferenceWithAuthorityAndFragment() throws Exception {
        final Authority authority = anAuthority();
        final Fragment<String> fragment = aFragment();
        assertThat(aScheme().parseRelativeReference("//" + authority.asString() + "#" + fragment.asString()), equalTo(aScheme().relativeReference(authority, fragment)));
    }

    @Test
    void parsesARelativeReferenceWithAuthorityAndQueryAndFragment() throws Exception {
        final Authority authority = anAuthority();
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        assertThat(aScheme().parseRelativeReference("//" + authority.asString() + "?" + query.asString() + "#" + fragment.asString()), equalTo(aScheme().relativeReference(authority, query, fragment)));
    }

    @Test
    void parsesARelativeReferenceWithAuthorityAndNonEmptyPath() throws Exception {
        final Authority authority = anAuthority();
        final Segment<String> firstSegment = aSegment();
        final Segment<String> secondSegment = aSegment();
        assertThat(aScheme().parseRelativeReference("//" + authority.asString() + "/" + firstSegment.asString() + "/" + secondSegment.asString()), equalTo(aScheme().relativeReference(authority, Path.path(firstSegment, secondSegment))));
    }

    @Test
    void parsesRelativeReferenceWithAuthorityAndPathAndQuery() throws Exception {
        final Authority authority = anAuthority();
        final AbsolutePath<String> absolutePath = anUnpollutedAbsolutePath();
        final Query<String> query = aQuery();
        assertThat(aScheme().parseRelativeReference("//" + authority.asString() + absolutePath.asString(NEVER_PREFIX_WITH_DOT_SEGMENT) + "?" + query.asString()), equalTo(aScheme().relativeReference(authority, absolutePath, query)));
    }

    @Test
    void parsesRelativeReferenceWithAuthorityAndPathAndFragment() throws Exception {
        final Authority authority = anAuthority();
        final AbsolutePath<String> absolutePath = anUnpollutedAbsolutePath();
        final Fragment<String> fragment = aFragment();
        assertThat(aScheme().parseRelativeReference("//" + authority.asString() + absolutePath.asString(NEVER_PREFIX_WITH_DOT_SEGMENT) + "#" + fragment.asString()), equalTo(aScheme().relativeReference(authority, absolutePath, fragment)));
    }

    @Test
    void parsesRelativeReferenceWithAuthorityAndPathAndQueryAndFragment() throws Exception {
        final Authority authority = anAuthority();
        final AbsolutePath<String> absolutePath = anUnpollutedAbsolutePath();
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        assertThat(aScheme().parseRelativeReference("//" + authority.asString() + absolutePath.asString(NEVER_PREFIX_WITH_DOT_SEGMENT) + "?" + query.asString() + "#" + fragment.asString()), equalTo(aScheme().relativeReference(authority, absolutePath, query, fragment)));
    }

    @Test
    void parsesRelativeReferenceFromUri() throws Exception {
        final Authority authority = authority(aRegisteredName());
        final AbsolutePath<String> absolutePath = anUnpollutedAbsolutePath();
        final Query<String> query = aQuery();
        final Fragment<String> fragment = aFragment();
        assertThat(aScheme().parseRelativeReference(URI.create("//" + authority.asString() + absolutePath.asString(NEVER_PREFIX_WITH_DOT_SEGMENT) + "?" + query.asString() + "#" + fragment.asString())), equalTo(aScheme().relativeReference(authority, absolutePath, query, fragment)));
    }

    @Test
    void parsingNullThrowsNullPointerException() {
        final Scheme<String, Query<String>, Fragment<String>> scheme = aScheme();
        assertThrows(NullPointerException.class, () -> scheme.parseRelativeReference((String) null), "Null value should throw NullPointerException in parser");
    }

}
