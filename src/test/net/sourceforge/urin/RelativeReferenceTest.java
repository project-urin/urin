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
import static org.junit.Assert.assertThat;

public class RelativeReferenceTest {
    @Test
    public void aRelativeReferenceWithEmptyPathAsStringIsCorrect() throws Exception {
        assertThat(aScheme().relativeReference().asString(), equalTo(""));
        assertThat(aScheme().relativeReference().asUri(), equalTo(URI.create("")));
    }

    @Test
    public void aRelativeReferenceWithEmptyPathPathIsCorrect() throws Exception {
        assertThat(aScheme().relativeReference().path(), equalTo(Path.<String>rootlessPath()));
    }

    @Test
    public void aRelativeReferenceWithEmptyPathQueryIsCorrect() throws Exception {
        final RelativeReference relativeReference = aScheme().relativeReference();
        assertThat(relativeReference.hasQuery(), equalTo(false));
        assertThrowsException("Attempt to get query from a UrinReference that does not have one.", UnsupportedOperationException.class, new ExceptionAssert.ExceptionThrower<java.lang.UnsupportedOperationException>() {
            public void execute() throws UnsupportedOperationException {
                relativeReference.query();
            }
        });
    }

    @Test
    public void aRelativeReferenceWithEmptyPathFragmentIsCorrect() throws Exception {
        final RelativeReference relativeReference = aScheme().relativeReference();
        assertThat(relativeReference.hasFragment(), equalTo(false));
        assertThrowsException("Attempt to get fragment from a UrinReference that does not have one.", UnsupportedOperationException.class, new ExceptionAssert.ExceptionThrower<java.lang.UnsupportedOperationException>() {
            public void execute() throws UnsupportedOperationException {
                relativeReference.fragment();
            }
        });
    }

    @Test
    public void aRelativeReferenceWithEmptyPathIsEqualToAnotherRelativeReferenceWithEmptyPath() throws Exception {
        assertThat(aScheme().relativeReference(), equalTo(aScheme().relativeReference()));
        assertThat(aScheme().relativeReference().hashCode(), equalTo(aScheme().relativeReference().hashCode()));
    }

    @Test
    public void aRelativeReferenceWithEmptyPathToStringIsCorrect() throws Exception {
        assertThat(aScheme().relativeReference().toString(), equalTo("RelativeReference{path=EmptyPath}"));
    }

    @Test
    public void aRelativeReferenceWithEmptyPathWithQueryAsStringIsCorrect() throws Exception {
        Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(query).asString(), equalTo("?" + query.asString()));
        assertThat(aScheme().relativeReference(query).asUri(), equalTo(URI.create("?" + query.asString())));
    }

    @Test
    public void aRelativeReferenceWithEmptyPathWithQueryPathIsCorrect() throws Exception {
        Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(query).path(), equalTo(Path.<String>rootlessPath()));
    }

    @Test
    public void aRelativeReferenceWithEmptyPathWithQueryQueryIsCorrect() throws Exception {
        Query<String> query = aQuery();
        final RelativeReference<String, Query<String>, Fragment<String>> relativeReference = aScheme().relativeReference(query);
        assertThat(relativeReference.hasQuery(), equalTo(true));
        assertThat(relativeReference.query(), equalTo(query));
    }

    @Test
    public void aRelativeReferenceWithEmptyPathWithQueryFragmentIsCorrect() throws Exception {
        Query<String> query = aQuery();
        final RelativeReference relativeReference = aScheme().relativeReference(query);
        assertThat(relativeReference.hasFragment(), equalTo(false));
        assertThrowsException("Attempt to get fragment from a UrinReference that does not have one.", UnsupportedOperationException.class, new ExceptionAssert.ExceptionThrower<java.lang.UnsupportedOperationException>() {
            public void execute() throws UnsupportedOperationException {
                relativeReference.fragment();
            }
        });
    }

    @Test
    public void aRelativeReferenceWithEmptyPathWithQueryIsEqualToAnotherRelativeReferenceWithEmptyPath() throws Exception {
        Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(query), equalTo(aScheme().relativeReference(query)));
        assertThat(aScheme().relativeReference(query).hashCode(), equalTo(aScheme().relativeReference(query).hashCode()));
    }

    @Test
    public void aRelativeReferenceWithEmptyPathWithQueryIsNotEqualToAnotherWithTheADifferentQuery() throws Exception {
        assertThat(aScheme().relativeReference(aQuery()), not(equalTo(aScheme().relativeReference(aQuery()))));
    }

    @Test
    public void aRelativeReferenceWithEmptyPathWithQueryToStringIsCorrect() throws Exception {
        Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(query).toString(), equalTo("RelativeReference{path=EmptyPath, query=" + query.toString() + "}"));
    }

    @Test
    public void rejectsNullInFactoryForARelativeReferenceWithEmptyPathWithQuery() throws Exception {
        assertThrowsException("Null query should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Query<String> query = null;
                aScheme().relativeReference(query);
            }
        });
    }

    @Test
    public void aRelativeReferenceWithEmptyPathWithFragmentAsStringIsCorrect() throws Exception {
        Fragment fragment = aFragment();
        assertThat(aScheme().relativeReference(fragment).asString(), equalTo("#" + fragment.asString()));
        assertThat(aScheme().relativeReference(fragment).asUri(), equalTo(URI.create("#" + fragment.asString())));
    }

    @Test
    public void aRelativeReferenceWithEmptyPathWithFragmentPathIsCorrect() throws Exception {
        Fragment fragment = aFragment();
        assertThat(aScheme().relativeReference(fragment).path(), equalTo(Path.<String>rootlessPath()));
    }

    @Test
    public void aRelativeReferenceWithEmptyPathWithFragmentQueryIsCorrect() throws Exception {
        Fragment fragment = aFragment();
        final RelativeReference relativeReference = aScheme().relativeReference(fragment);
        assertThat(relativeReference.hasQuery(), equalTo(false));
        assertThrowsException("Attempt to get query from a UrinReference that does not have one.", UnsupportedOperationException.class, new ExceptionAssert.ExceptionThrower<java.lang.UnsupportedOperationException>() {
            public void execute() throws UnsupportedOperationException {
                relativeReference.query();
            }
        });
    }

    @Test
    public void aRelativeReferenceWithEmptyPathWithFragmentFragmentIsCorrect() throws Exception {
        Fragment fragment = aFragment();
        RelativeReference relativeReference = aScheme().relativeReference(fragment);
        assertThat(relativeReference.hasFragment(), equalTo(true));
        assertThat(relativeReference.fragment(), equalTo(fragment));
    }

    @Test
    public void aRelativeReferenceWithEmptyPathWithFragmentIsEqualToAnotherRelativeReferenceWithEmptyPath() throws Exception {
        Fragment fragment = aFragment();
        assertThat(aScheme().relativeReference(fragment), equalTo(aScheme().relativeReference(fragment)));
        assertThat(aScheme().relativeReference(fragment).hashCode(), equalTo(aScheme().relativeReference(fragment).hashCode()));
    }

    @Test
    public void aRelativeReferenceWithEmptyPathWithFragmentIsNotEqualToAnotherWithTheADifferentFragment() throws Exception {
        assertThat(aScheme().relativeReference(aFragment()), not(equalTo(aScheme().relativeReference(aFragment()))));
    }

    @Test
    public void aRelativeReferenceWithEmptyPathWithFragmentToStringIsCorrect() throws Exception {
        Fragment fragment = aFragment();
        assertThat(aScheme().relativeReference(fragment).toString(), equalTo("RelativeReference{path=EmptyPath, fragment=" + fragment.toString() + "}"));
    }

    @Test
    public void rejectsNullInFactoryForARelativeReferenceWithEmptyPathWithFragment() throws Exception {
        assertThrowsException("Null fragment should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Fragment fragment = null;
                aScheme().relativeReference(fragment);
            }
        });
    }

    @Test
    public void aRelativeReferenceWithEmptyPathWithQueryAndFragmentAsStringIsCorrect() throws Exception {
        Query<String> query = aQuery();
        Fragment fragment = aFragment();
        assertThat(aScheme().relativeReference(query, fragment).asString(), equalTo("?" + query.asString() + "#" + fragment.asString()));
        assertThat(aScheme().relativeReference(query, fragment).asUri(), equalTo(URI.create("?" + query.asString() + "#" + fragment.asString())));
    }

    @Test
    public void aRelativeReferenceWithEmptyPathWithQueryAndFragmentPathIsCorrect() throws Exception {
        Query<String> query = aQuery();
        Fragment fragment = aFragment();
        assertThat(aScheme().relativeReference(query, fragment).path(), equalTo(Path.<String>rootlessPath()));
    }

    @Test
    public void aRelativeReferenceWithEmptyPathWithQueryAndFragmentQueryIsCorrect() throws Exception {
        Query<String> query = aQuery();
        Fragment fragment = aFragment();
        RelativeReference<String, Query<String>, Fragment<String>> relativeReference = aScheme().relativeReference(query, fragment);
        assertThat(relativeReference.hasQuery(), equalTo(true));
        assertThat(relativeReference.query(), equalTo(query));
    }

    @Test
    public void aRelativeReferenceWithEmptyPathWithQueryAndFragmentFragmentIsCorrect() throws Exception {
        Query<String> query = aQuery();
        Fragment fragment = aFragment();
        RelativeReference relativeReference = aScheme().relativeReference(query, fragment);
        assertThat(relativeReference.hasFragment(), equalTo(true));
        assertThat(relativeReference.fragment(), equalTo(fragment));
    }

    @Test
    public void aRelativeReferenceWithEmptyPathWithQueryAndFragmentIsEqualToAnotherRelativeReferenceWithEmptyPath() throws Exception {
        Query<String> query = aQuery();
        Fragment fragment = aFragment();
        assertThat(aScheme().relativeReference(query, fragment), equalTo(aScheme().relativeReference(query, fragment)));
        assertThat(aScheme().relativeReference(query, fragment).hashCode(), equalTo(aScheme().relativeReference(query, fragment).hashCode()));
    }

    @Test
    public void aRelativeReferenceWithEmptyPathWithQueryAndFragmentIsNotEqualToAnotherWithTheADifferentQuery() throws Exception {
        Fragment fragment = aFragment();
        assertThat(aScheme().relativeReference(aQuery(), fragment), not(equalTo(aScheme().relativeReference(aQuery(), fragment))));
    }

    @Test
    public void aRelativeReferenceWithEmptyPathWithQueryAndFragmentIsNotEqualToAnotherWithTheADifferentFragment() throws Exception {
        Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(query, aFragment()), not(equalTo(aScheme().relativeReference(query, aFragment()))));
    }

    @Test
    public void aRelativeReferenceWithEmptyPathWithQueryAndFragmentToStringIsCorrect() throws Exception {
        Query<String> query = aQuery();
        Fragment fragment = aFragment();
        assertThat(aScheme().relativeReference(query, fragment).toString(), equalTo("RelativeReference{path=EmptyPath, query=" + query + ", fragment=" + fragment + "}"));
    }

    @Test
    public void rejectsNullInFactoryForARelativeReferenceWithEmptyPathWithQueryAndFragment() throws Exception {
        assertThrowsException("Null query should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Query<String> query = null;
                aScheme().relativeReference(query, aFragment());
            }
        });
        assertThrowsException("Null fragment should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Fragment fragment = null;
                aScheme().relativeReference(aQuery(), fragment);
            }
        });
    }

    @Test
    public void aRelativeReferenceWithPathAsStringIsCorrect() throws Exception {
        Path<String> path = aPath();
        assertThat(aScheme().relativeReference(path).asString(), equalTo(path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON)));
        assertThat(aScheme().relativeReference(path).asUri(), equalTo(URI.create(path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON))));
    }

    @Test
    public void aRelativeReferenceWithPathPathIsCorrect() throws Exception {
        Path<String> path = aPath();
        assertThat(aScheme().relativeReference(path).path(), equalTo(path));
    }

    @Test
    public void aRelativeReferenceWithPathQueryIsCorrect() throws Exception {
        Path<String> path = aPath();
        final RelativeReference relativeReference = aScheme().relativeReference(path);
        assertThat(relativeReference.hasQuery(), equalTo(false));
        assertThrowsException("Attempt to get query from a UrinReference that does not have one.", UnsupportedOperationException.class, new ExceptionAssert.ExceptionThrower<java.lang.UnsupportedOperationException>() {
            public void execute() throws UnsupportedOperationException {
                relativeReference.query();
            }
        });
    }

    @Test
    public void aRelativeReferenceWithPathFragmentIsCorrect() throws Exception {
        Path<String> path = aPath();
        final RelativeReference relativeReference = aScheme().relativeReference(path);
        assertThat(relativeReference.hasFragment(), equalTo(false));
        assertThrowsException("Attempt to get fragment from a UrinReference that does not have one.", UnsupportedOperationException.class, new ExceptionAssert.ExceptionThrower<java.lang.UnsupportedOperationException>() {
            public void execute() throws UnsupportedOperationException {
                relativeReference.fragment();
            }
        });
    }

    @Test
    public void aRelativeReferenceWithPathIsEqualToAnotherRelativeReferenceWithPathAndQuery() throws Exception {
        Path<String> path = aPath();
        assertThat(aScheme().relativeReference(path), equalTo(aScheme().relativeReference(path)));
        assertThat(aScheme().relativeReference(path).hashCode(), equalTo(aScheme().relativeReference(path).hashCode()));
    }

    @Test
    public void aRelativeReferenceWithPathIsNotEqualToAnotherWithTheADifferentPath() throws Exception {
        assertThat(aScheme().relativeReference(aPath()), not(equalTo(aScheme().relativeReference(aPath()))));
    }

    @Test
    public void aRelativeReferenceWithPathToStringIsCorrect() throws Exception {
        Path<String> path = aPath();
        assertThat(aScheme().relativeReference(path).toString(), equalTo("RelativeReference{path=" + path.toString() + "}"));
    }

    @Test
    public void rejectsNullInFactoryForARelativeReferenceWithPath() throws Exception {
        assertThrowsException("Null path should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                aScheme().relativeReference((Path<String>) null);
            }
        });
    }

    @Test
    public void aRelativeReferenceWithPathResolvesSchemeToTheBase() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        Authority baseAuthority = anAuthority();
        AbsolutePath<String> basePath = anAbsolutePath();
        Path<String> relativeReferencePath = aPath();
        assertThat(
                aScheme().relativeReference(relativeReferencePath).resolve(baseScheme, baseAuthority, basePath),
                equalTo(baseScheme.urin(baseAuthority, (AbsolutePath<String>) relativeReferencePath.resolveRelativeTo(basePath))));
    }

    @Test
    public void aRelativeReferenceWithPathResolvesSchemeAndAuthorityToTheBase() throws Exception {
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
    public void aRelativeReferenceWithPathResolvesSchemeAndAuthorityAndQueryAndFragmentToTheBase() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        Authority baseAuthority = anAuthority();
        AbsolutePath<String> basePath = anAbsolutePath();
        Query<String> baseQuery = aQuery();
        Fragment baseFragment = aFragment();
        Path<String> relativeReferencePath = aPath();
        assertThat(
                aScheme().relativeReference(relativeReferencePath).resolve(baseScheme, baseAuthority, basePath, baseQuery, baseFragment),
                equalTo(baseScheme.urin(baseAuthority, (AbsolutePath<String>) relativeReferencePath.resolveRelativeTo(basePath))));
    }

    @Test
    public void aRelativeReferenceWithPathAndQueryAsStringIsCorrect() throws Exception {
        Path<String> path = aPath();
        Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(path, query).asString(), equalTo(path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON) + "?" + query.asString()));
        assertThat(aScheme().relativeReference(path, query).asUri(), equalTo(URI.create(path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON) + "?" + query.asString())));
    }

    @Test
    public void aRelativeReferenceWithPathAndQueryPathIsCorrect() throws Exception {
        Path<String> path = aPath();
        Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(path, query).path(), equalTo(path));
    }

    @Test
    public void aRelativeReferenceWithPathAndQueryQueryIsCorrect() throws Exception {
        Path<String> path = aPath();
        Query<String> query = aQuery();
        final RelativeReference<String, Query<String>, Fragment<String>> relativeReference = aScheme().relativeReference(path, query);
        assertThat(relativeReference.hasQuery(), equalTo(true));
        assertThat(relativeReference.query(), equalTo(query));
    }

    @Test
    public void aRelativeReferenceWithPathAndQueryFragmentIsCorrect() throws Exception {
        Path<String> path = aPath();
        Query<String> query = aQuery();
        final RelativeReference relativeReference = aScheme().relativeReference(path, query);
        assertThat(relativeReference.hasFragment(), equalTo(false));
        assertThrowsException("Attempt to get fragment from a UrinReference that does not have one.", UnsupportedOperationException.class, new ExceptionAssert.ExceptionThrower<java.lang.UnsupportedOperationException>() {
            public void execute() throws UnsupportedOperationException {
                relativeReference.fragment();
            }
        });
    }

    @Test
    public void aRelativeReferenceWithPathAndQueryIsEqualToAnotherRelativeReferenceWithPathAndQuery() throws Exception {
        Path<String> path = aPath();
        Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(path, query), equalTo(aScheme().relativeReference(path, query)));
        assertThat(aScheme().relativeReference(path, query).hashCode(), equalTo(aScheme().relativeReference(path, query).hashCode()));
    }

    @Test
    public void aRelativeReferenceWithPathAndQueryIsNotEqualToAnotherWithTheADifferentPath() throws Exception {
        Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(aPath(), query), not(equalTo(aScheme().relativeReference(aPath(), query))));
    }

    @Test
    public void aRelativeReferenceWithPathAndQueryIsNotEqualToAnotherWithTheADifferentQuery() throws Exception {
        Path<String> path = aPath();
        assertThat(aScheme().relativeReference(path, aQuery()), not(equalTo(aScheme().relativeReference(path, aQuery()))));
    }

    @Test
    public void aRelativeReferenceWithPathAndQueryToStringIsCorrect() throws Exception {
        Path<String> path = aPath();
        Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(path, query).toString(), equalTo("RelativeReference{path=" + path.toString() + ", query=" + query.toString() + "}"));
    }

    @Test
    public void rejectsNullInFactoryForARelativeReferenceWithPathAndQuery() throws Exception {
        assertThrowsException("Null path should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                aScheme().relativeReference((Path<String>) null, aQuery());
            }
        });
        assertThrowsException("Null query should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                aScheme().relativeReference(aPath(), (Query<String>) null);
            }
        });
    }

    @Test
    public void aRelativeReferenceWithPathAndQueryResolvesSchemeToTheBase() throws Exception {
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
    public void aRelativeReferenceWithPathAndQueryResolvesSchemeAndAuthorityAndQueryToTheBase() throws Exception {
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
    public void aRelativeReferenceWithPathAndQueryResolvesSchemeAndAuthorityAndQueryAndFragmentToTheBase() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        Authority baseAuthority = anAuthority();
        AbsolutePath<String> basePath = anAbsolutePath();
        Query<String> baseQuery = aQuery();
        Fragment baseFragment = aFragment();
        Path<String> relativeReferencePath = aPath();
        Query<String> relativeReferenceQuery = aQuery();
        assertThat(
                aScheme().relativeReference(relativeReferencePath, relativeReferenceQuery).resolve(baseScheme, baseAuthority, basePath, baseQuery, baseFragment),
                equalTo(baseScheme.urin(baseAuthority, (AbsolutePath<String>) relativeReferencePath.resolveRelativeTo(basePath), relativeReferenceQuery)));
    }

    @Test
    public void aRelativeReferenceWithPathAndFragmentAsStringIsCorrect() throws Exception {
        Path<String> path = aPath();
        Fragment fragment = aFragment();
        assertThat(aScheme().relativeReference(path, fragment).asString(), equalTo(path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON) + "#" + fragment.asString()));
        assertThat(aScheme().relativeReference(path, fragment).asUri(), equalTo(URI.create(path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON) + "#" + fragment.asString())));
    }

    @Test
    public void aRelativeReferenceWithPathAndFragmentPathIsCorrect() throws Exception {
        Path<String> path = aPath();
        Fragment fragment = aFragment();
        assertThat(aScheme().relativeReference(path, fragment).path(), equalTo(path));
    }

    @Test
    public void aRelativeReferenceWithPathAndFragmentQueryIsCorrect() throws Exception {
        Path<String> path = aPath();
        Fragment fragment = aFragment();
        final RelativeReference relativeReference = aScheme().relativeReference(path, fragment);
        assertThat(relativeReference.hasQuery(), equalTo(false));
        assertThrowsException("Attempt to get query from a UrinReference that does not have one.", UnsupportedOperationException.class, new ExceptionAssert.ExceptionThrower<java.lang.UnsupportedOperationException>() {
            public void execute() throws UnsupportedOperationException {
                relativeReference.query();
            }
        });
    }

    @Test
    public void aRelativeReferenceWithPathAndFragmentFragmentIsCorrect() throws Exception {
        Path<String> path = aPath();
        Fragment fragment = aFragment();
        RelativeReference relativeReference = aScheme().relativeReference(path, fragment);
        assertThat(relativeReference.hasFragment(), equalTo(true));
        assertThat(relativeReference.fragment(), equalTo(fragment));
    }

    @Test
    public void aRelativeReferenceWithPathAndFragmentIsEqualToAnotherRelativeReferenceWithPathAndFragment() throws Exception {
        Path<String> path = aPath();
        Fragment fragment = aFragment();
        assertThat(aScheme().relativeReference(path, fragment), equalTo(aScheme().relativeReference(path, fragment)));
        assertThat(aScheme().relativeReference(path, fragment).hashCode(), equalTo(aScheme().relativeReference(path, fragment).hashCode()));
    }

    @Test
    public void aRelativeReferenceWithPathAndFragmentIsNotEqualToAnotherWithTheADifferentPath() throws Exception {
        Fragment fragment = aFragment();
        assertThat(aScheme().relativeReference(aPath(), fragment), not(equalTo(aScheme().relativeReference(aPath(), fragment))));
    }

    @Test
    public void aRelativeReferenceWithPathAndFragmentIsNotEqualToAnotherWithTheADifferentFragment() throws Exception {
        Path<String> path = aPath();
        assertThat(aScheme().relativeReference(path, aFragment()), not(equalTo(aScheme().relativeReference(path, aFragment()))));
    }

    @Test
    public void aRelativeReferenceWithPathAndFragmentToStringIsCorrect() throws Exception {
        Path<String> path = aPath();
        Fragment fragment = aFragment();
        assertThat(aScheme().relativeReference(path, fragment).toString(), equalTo("RelativeReference{path=" + path.toString() + ", fragment=" + fragment.toString() + "}"));
    }

    @Test
    public void rejectsNullInFactoryForARelativeReferenceWithPathAndFragment() throws Exception {
        assertThrowsException("Null path should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                aScheme().relativeReference((Path<String>) null, aFragment());
            }
        });
        assertThrowsException("Null fragment should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                aScheme().relativeReference(aPath(), (Fragment) null);
            }
        });
    }

    @Test
    public void aRelativeReferenceWithPathAndFragmentResolvesSchemeToTheBase() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        Authority baseAuthority = anAuthority();
        AbsolutePath<String> basePath = anAbsolutePath();
        Path<String> relativeReferencePath = aPath();
        Fragment relativeReferenceFragment = aFragment();
        assertThat(
                aScheme().relativeReference(relativeReferencePath, relativeReferenceFragment).resolve(baseScheme, baseAuthority, basePath),
                equalTo(baseScheme.urin(baseAuthority, (AbsolutePath<String>) relativeReferencePath.resolveRelativeTo(basePath), relativeReferenceFragment)));
    }

    @Test
    public void aRelativeReferenceWithPathAndFragmentResolvesSchemeAndAuthorityAndQueryToTheBase() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        Authority baseAuthority = anAuthority();
        AbsolutePath<String> basePath = anAbsolutePath();
        Query<String> baseQuery = aQuery();
        Path<String> relativeReferencePath = aPath();
        Fragment relativeReferenceFragment = aFragment();
        assertThat(
                aScheme().relativeReference(relativeReferencePath, relativeReferenceFragment).resolve(baseScheme, baseAuthority, basePath, baseQuery),
                equalTo(baseScheme.urin(baseAuthority, (AbsolutePath<String>) relativeReferencePath.resolveRelativeTo(basePath), relativeReferenceFragment)));
    }

    @Test
    public void aRelativeReferenceWithPathAndFragmentResolvesSchemeAndAuthorityAndQueryAndFragmentToTheBase() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        Authority baseAuthority = anAuthority();
        AbsolutePath<String> basePath = anAbsolutePath();
        Query<String> baseQuery = aQuery();
        Fragment baseFragment = aFragment();
        Path<String> relativeReferencePath = aPath();
        Fragment relativeReferenceFragment = aFragment();
        assertThat(
                aScheme().relativeReference(relativeReferencePath, relativeReferenceFragment).resolve(baseScheme, baseAuthority, basePath, baseQuery, baseFragment),
                equalTo(baseScheme.urin(baseAuthority, (AbsolutePath<String>) relativeReferencePath.resolveRelativeTo(basePath), relativeReferenceFragment)));
    }

    @Test
    public void aRelativeReferenceWithPathAndQueryAndFragmentAsStringIsCorrect() throws Exception {
        Path<String> path = aPath();
        Query<String> query = aQuery();
        Fragment fragment = aFragment();
        assertThat(aScheme().relativeReference(path, query, fragment).asString(), equalTo(path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON) + "?" + query.asString() + "#" + fragment.asString()));
        assertThat(aScheme().relativeReference(path, query, fragment).asUri(), equalTo(URI.create(path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON) + "?" + query.asString() + "#" + fragment.asString())));
    }

    @Test
    public void aRelativeReferenceWithPathAndQueryAndFragmentPathIsCorrect() throws Exception {
        Path<String> path = aPath();
        Query<String> query = aQuery();
        Fragment fragment = aFragment();
        assertThat(aScheme().relativeReference(path, query, fragment).path(), equalTo(path));
    }

    @Test
    public void aRelativeReferenceWithPathAndQueryAndFragmentQueryIsCorrect() throws Exception {
        Path<String> path = aPath();
        Query<String> query = aQuery();
        Fragment fragment = aFragment();
        RelativeReference<String, Query<String>, Fragment<String>> relativeReference = aScheme().relativeReference(path, query, fragment);
        assertThat(relativeReference.hasQuery(), equalTo(true));
        assertThat(relativeReference.query(), equalTo(query));
    }

    @Test
    public void aRelativeReferenceWithPathAndQueryAndFragmentFragmentIsCorrect() throws Exception {
        Path<String> path = aPath();
        Query<String> query = aQuery();
        Fragment fragment = aFragment();
        RelativeReference relativeReference = aScheme().relativeReference(path, query, fragment);
        assertThat(relativeReference.hasFragment(), equalTo(true));
        assertThat(relativeReference.fragment(), equalTo(fragment));
    }

    @Test
    public void aRelativeReferenceWithPathAndQueryAndFragmentIsEqualToAnotherRelativeReferenceWithPathAndFragment() throws Exception {
        Path<String> path = aPath();
        Query<String> query = aQuery();
        Fragment fragment = aFragment();
        assertThat(aScheme().relativeReference(path, query, fragment), equalTo(aScheme().relativeReference(path, query, fragment)));
        assertThat(aScheme().relativeReference(path, query, fragment).hashCode(), equalTo(aScheme().relativeReference(path, query, fragment).hashCode()));
    }

    @Test
    public void aRelativeReferenceWithPathAndQueryAndFragmentIsNotEqualToAnotherWithTheADifferentPath() throws Exception {
        Query<String> query = aQuery();
        Fragment fragment = aFragment();
        assertThat(aScheme().relativeReference(aPath(), query, fragment), not(equalTo(aScheme().relativeReference(aPath(), query, fragment))));
    }

    @Test
    public void aRelativeReferenceWithPathAndQueryAndFragmentIsNotEqualToAnotherWithTheADifferentQuery() throws Exception {
        Path<String> path = aPath();
        Fragment fragment = aFragment();
        assertThat(aScheme().relativeReference(path, aQuery(), fragment), not(equalTo(aScheme().relativeReference(path, aQuery(), fragment))));
    }

    @Test
    public void aRelativeReferenceWithPathAndQueryAndFragmentIsNotEqualToAnotherWithTheADifferentFragment() throws Exception {
        Path<String> path = aPath();
        Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(path, query, aFragment()), not(equalTo(aScheme().relativeReference(path, query, aFragment()))));
    }

    @Test
    public void aRelativeReferenceWithPathAndQueryAndFragmentToStringIsCorrect() throws Exception {
        Path<String> path = aPath();
        Query<String> query = aQuery();
        Fragment fragment = aFragment();
        assertThat(aScheme().relativeReference(path, query, fragment).toString(), equalTo("RelativeReference{path=" + path.toString() + ", query=" + query.toString() + ", fragment=" + fragment.toString() + "}"));
    }

    @Test
    public void rejectsNullInFactoryForARelativeReferenceWithPathAndQueryAndFragment() throws Exception {
        assertThrowsException("Null path should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                aScheme().relativeReference((Path<String>) null, aQuery(), aFragment());
            }
        });
        assertThrowsException("Null query should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                aScheme().relativeReference(aPath(), null, aFragment());
            }
        });
        assertThrowsException("Null fragment should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                aScheme().relativeReference(aPath(), aQuery(), null);
            }
        });
    }

    @Test
    public void aRelativeReferenceWithPathAndQueryAndFragmentResolvesSchemeToTheBase() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        Authority baseAuthority = anAuthority();
        AbsolutePath<String> basePath = anAbsolutePath();
        Path<String> relativeReferencePath = aPath();
        Query<String> relativeReferenceQuery = aQuery();
        Fragment relativeReferenceFragment = aFragment();
        assertThat(
                aScheme().relativeReference(relativeReferencePath, relativeReferenceQuery, relativeReferenceFragment).resolve(baseScheme, baseAuthority, basePath),
                equalTo(baseScheme.urin(baseAuthority, (AbsolutePath<String>) relativeReferencePath.resolveRelativeTo(basePath), relativeReferenceQuery, relativeReferenceFragment)));
    }

    @Test
    public void aRelativeReferenceWithPathAndQueryAndFragmentResolvesSchemeAndAuthorityAndQueryToTheBase() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        Authority baseAuthority = anAuthority();
        AbsolutePath<String> basePath = anAbsolutePath();
        Query<String> baseQuery = aQuery();
        Path<String> relativeReferencePath = aPath();
        Query<String> relativeReferenceQuery = aQuery();
        Fragment relativeReferenceFragment = aFragment();
        assertThat(
                aScheme().relativeReference(relativeReferencePath, relativeReferenceQuery, relativeReferenceFragment).resolve(baseScheme, baseAuthority, basePath, baseQuery),
                equalTo(baseScheme.urin(baseAuthority, (AbsolutePath<String>) relativeReferencePath.resolveRelativeTo(basePath), relativeReferenceQuery, relativeReferenceFragment)));
    }

    @Test
    public void aRelativeReferenceWithPathAndQueryAndFragmentResolvesSchemeAndAuthorityAndQueryAndFragmentToTheBase() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        Authority baseAuthority = anAuthority();
        AbsolutePath<String> basePath = anAbsolutePath();
        Query<String> baseQuery = aQuery();
        Fragment baseFragment = aFragment();
        Path<String> relativeReferencePath = aPath();
        Query<String> relativeReferenceQuery = aQuery();
        Fragment relativeReferenceFragment = aFragment();
        assertThat(
                aScheme().relativeReference(relativeReferencePath, relativeReferenceQuery, relativeReferenceFragment).resolve(baseScheme, baseAuthority, basePath, baseQuery, baseFragment),
                equalTo(baseScheme.urin(baseAuthority, (AbsolutePath<String>) relativeReferencePath.resolveRelativeTo(basePath), relativeReferenceQuery, relativeReferenceFragment)));
    }

    @Test
    public void aSimpleAbsolutePathAsStringReturnsThePath() throws Exception {
        Segment<String> firstSegment = aNonDotSegment();
        Segment<String> secondSegment = aNonDotSegment();
        assertThat(aScheme().relativeReference(Path.path(firstSegment, secondSegment)).asString(), equalTo("/" + firstSegment.asString() + "/" + secondSegment.asString()));
        assertThat(aScheme().relativeReference(Path.path(firstSegment, secondSegment)).asUri(), equalTo(URI.create("/" + firstSegment.asString() + "/" + secondSegment.asString())));
    }

    @Test
    public void aSimpleAbsolutePathPrefixesAnEmptyFirstSegmentWithADotSegment() throws Exception {
        Segment<String> firstSegment = segment("");
        Segment<String> secondSegment = aNonDotSegment();
        assertThat(aScheme().relativeReference(Path.path(firstSegment, secondSegment)).asString(), equalTo("/./" + firstSegment.asString() + "/" + secondSegment.asString()));
    }

    @Test
    public void rejectsNullInFactoryForASimplePath() throws Exception {
        assertThrowsException("Null first segment should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Path<String> path = null;
                aScheme().relativeReference(path);
            }
        });
    }

    @Test
    public void aSimpleAbsolutePathIsEqualToAnotherWithTheSamePath() throws Exception {
        Path<String> path = aPath();
        assertThat(aScheme().relativeReference(path), equalTo(aScheme().relativeReference(path)));
        assertThat(aScheme().relativeReference(path).hashCode(), equalTo(aScheme().relativeReference(path).hashCode()));
    }

    @Test
    public void aSimpleAbsolutePathIsNotEqualToAnotherWithTheADifferentPath() throws Exception {
        assertThat(aScheme().relativeReference(aPath()), not(equalTo(aScheme().relativeReference(aPath()))));
    }

    @Test
    public void aSimpleAbsolutePathPathIsCorrect() throws Exception {
        Path<String> path = aPath();
        assertThat(aScheme().relativeReference(path).path(), equalTo(path));
    }

    @Test
    public void aSimpleAbsolutePathQueryIsCorrect() throws Exception {
        Path<String> path = aPath();
        final RelativeReference relativeReference = aScheme().relativeReference(path);
        assertThat(relativeReference.hasQuery(), equalTo(false));
        assertThrowsException("Attempt to get query from a UrinReference that does not have one.", UnsupportedOperationException.class, new ExceptionAssert.ExceptionThrower<java.lang.UnsupportedOperationException>() {
            public void execute() throws UnsupportedOperationException {
                relativeReference.query();
            }
        });
    }

    @Test
    public void aSimpleAbsolutePathFragmentIsCorrect() throws Exception {
        Path<String> path = aPath();
        final RelativeReference relativeReference = aScheme().relativeReference(path);
        assertThat(relativeReference.hasFragment(), equalTo(false));
        assertThrowsException("Attempt to get fragment from a UrinReference that does not have one.", UnsupportedOperationException.class, new ExceptionAssert.ExceptionThrower<java.lang.UnsupportedOperationException>() {
            public void execute() throws UnsupportedOperationException {
                relativeReference.fragment();
            }
        });
    }

    @Test
    public void aSimpleAbsolutePathToStringIsCorrect() throws Exception {
        Path<String> path = aPath();
        assertThat(aScheme().relativeReference(path).toString(), equalTo("RelativeReference{path=" + path + "}"));
    }

    @Test
    public void aSimpleRootlessPathAsStringReturnsThePath() throws Exception {
        Segment<String> firstSegment = aNonDotSegment();
        Segment<String> secondSegment = aNonDotSegment();
        assertThat(aScheme().relativeReference(rootlessPath(firstSegment, secondSegment)).asString(), equalTo(firstSegment.asString() + "/" + secondSegment.asString()));
        assertThat(aScheme().relativeReference(rootlessPath(firstSegment, secondSegment)).asUri(), equalTo(URI.create(firstSegment.asString() + "/" + secondSegment.asString())));
    }

    @Test
    public void aSimpleRootlessPathRejectsAnEmptyFirstSegment() throws Exception {
        Segment<String> firstSegment = segment("");
        Segment<String> secondSegment = aNonDotSegment();
        assertThat(aScheme().relativeReference(rootlessPath(firstSegment, secondSegment)).asString(), equalTo("./" + firstSegment.asString() + "/" + secondSegment.asString()));
    }

    @Test
    public void aSimpleRootlessPathPrependsAColonInFirstSegmentWithDotSlash() throws Exception {
        Segment<String> firstSegment = segment(aStringIncluding(':'));
        Segment<String> secondSegment = aSegment();
        assertThat(aScheme().relativeReference(rootlessPath(firstSegment, secondSegment)), equalTo(aScheme().relativeReference(rootlessPath(Segment.<String>dot(), firstSegment, secondSegment))));
    }

    @Test
    public void aSimpleRootlessPathPermitsAColonInTrailingSegments() throws Exception {
        aScheme().relativeReference(rootlessPath(aSegment(), segment(aStringIncluding(':')))).asString();
    }

    @Test
    public void aSimpleRootlessPathIsEqualToAnotherWithTheSamePath() throws Exception {
        Path<String> path = aPath();
        assertThat(aScheme().relativeReference(path), equalTo(aScheme().relativeReference(path)));
        assertThat(aScheme().relativeReference(path).hashCode(), equalTo(aScheme().relativeReference(path).hashCode()));
    }

    @Test
    public void aSimpleRootlessPathIsNotEqualToAnotherWithTheADifferentPath() throws Exception {
        assertThat(aScheme().relativeReference(aPath()), not(equalTo(aScheme().relativeReference(aPath()))));
    }

    @Test
    public void aSimpleRootlessPathPathIsCorrect() throws Exception {
        Path<String> path = aPath();
        assertThat(aScheme().relativeReference(path).path(), equalTo(path));
    }

    @Test
    public void aSimpleRootlessPathQueryIsCorrect() throws Exception {
        final RelativeReference relativeReference = aScheme().relativeReference(aPath());
        assertThat(relativeReference.hasQuery(), equalTo(false));
        assertThrowsException("Attempt to get query from a UrinReference that does not have one.", UnsupportedOperationException.class, new ExceptionAssert.ExceptionThrower<java.lang.UnsupportedOperationException>() {
            public void execute() throws UnsupportedOperationException {
                relativeReference.query();
            }
        });
    }

    @Test
    public void aSimpleRootlessPathFragmentIsCorrect() throws Exception {
        final RelativeReference relativeReference = aScheme().relativeReference(aPath());
        assertThat(relativeReference.hasFragment(), equalTo(false));
        assertThrowsException("Attempt to get fragment from a UrinReference that does not have one.", UnsupportedOperationException.class, new ExceptionAssert.ExceptionThrower<java.lang.UnsupportedOperationException>() {
            public void execute() throws UnsupportedOperationException {
                relativeReference.fragment();
            }
        });
    }

    @Test
    public void aSimpleRootlessPathToStringIsCorrect() throws Exception {
        Path<String> path = aPath();
        assertThat(aScheme().relativeReference(path).toString(), equalTo("RelativeReference{path=" + path + "}"));
    }

    @Test
    public void aRelativeReferenceWithOnlyPathResolvesSchemeAndAuthorityToTheBase() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        Authority baseAuthority = anAuthority();
        AbsolutePath<String> basePath = anAbsolutePath();
        Path<String> relativeReferencePath = aPath();
        assertThat(
                aScheme().relativeReference(relativeReferencePath).resolve(baseScheme, baseAuthority, basePath),
                equalTo(baseScheme.urin(baseAuthority, (AbsolutePath<String>) relativeReferencePath.resolveRelativeTo(basePath))));
    }

    @Test
    public void aRelativeReferenceWithOnlyPathResolvesSchemeAndAuthorityAndQueryToTheBase() throws Exception {
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
    public void aRelativeReferenceWithOnlyPathResolvesSchemeAndAuthorityAndQueryAndFragmentToTheBase() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        Authority baseAuthority = anAuthority();
        AbsolutePath<String> basePath = anAbsolutePath();
        Path<String> relativeReferencePath = aPath();
        Query<String> baseQuery = aQuery();
        Fragment baseFragment = aFragment();
        assertThat(
                aScheme().relativeReference(relativeReferencePath).resolve(baseScheme, baseAuthority, basePath, baseQuery, baseFragment),
                equalTo(baseScheme.urin(baseAuthority, (AbsolutePath<String>) relativeReferencePath.resolveRelativeTo(basePath))));
    }

    @Test
    public void makesRelativeReferenceWithAuthorityAndEmptyPath() throws Exception {
        Authority authority = anAuthority();
        assertThat(aScheme().relativeReference(authority).asString(), equalTo("//" + authority.asString()));
    }

    @Test
    public void rejectsNullInFactoryForRelativeReferenceWithAuthorityAndEmptyPath() throws Exception {
        assertThrowsException("Null authority should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                aScheme().relativeReference((Authority) null);
            }
        });
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndEmptyPathIsEqualToAnotherWithTheSameAuthority() throws Exception {
        Authority authority = anAuthority();
        assertThat(aScheme().relativeReference(authority), equalTo(aScheme().relativeReference(authority)));
        assertThat(aScheme().relativeReference(authority).hashCode(), equalTo(aScheme().relativeReference(authority).hashCode()));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndEmptyPathIsNotEqualToAnotherWithTheADifferentAuthority() throws Exception {
        assertThat(aScheme().relativeReference(anAuthority()), not(equalTo(aScheme().relativeReference(anAuthority()))));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndEmptyPathPathIsCorrect() throws Exception {
        Authority authority = anAuthority();
        assertThat(aScheme().relativeReference(authority).path(), equalTo(Path.<String>rootlessPath()));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndEmptyPathQueryIsCorrect() throws Exception {
        Authority authority = anAuthority();
        final RelativeReference relativeReference = aScheme().relativeReference(authority);
        assertThat(relativeReference.hasQuery(), equalTo(false));
        assertThrowsException("Attempt to get query from a UrinReference that does not have one.", UnsupportedOperationException.class, new ExceptionAssert.ExceptionThrower<java.lang.UnsupportedOperationException>() {
            public void execute() throws UnsupportedOperationException {
                relativeReference.query();
            }
        });
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndEmptyPathFragmentIsCorrect() throws Exception {
        Authority authority = anAuthority();
        final RelativeReference relativeReference = aScheme().relativeReference(authority);
        assertThat(relativeReference.hasFragment(), equalTo(false));
        assertThrowsException("Attempt to get fragment from a UrinReference that does not have one.", UnsupportedOperationException.class, new ExceptionAssert.ExceptionThrower<java.lang.UnsupportedOperationException>() {
            public void execute() throws UnsupportedOperationException {
                relativeReference.fragment();
            }
        });
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndEmptyPathToStringIsCorrect() throws Exception {
        Authority authority = anAuthority();
        assertThat(aScheme().relativeReference(authority).toString(), equalTo("RelativeReference{authority=" + authority + ", path=EmptyPath}"));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndEmptyPathResolvesSchemeToTheBase() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        Authority baseAuthority = anAuthority();
        AbsolutePath<String> basePath = anAbsolutePath();
        Authority relativeReferenceAuthority = anAuthority();
        assertThat(
                aScheme().relativeReference(relativeReferenceAuthority).resolve(baseScheme, baseAuthority, basePath),
                equalTo(baseScheme.urin(relativeReferenceAuthority)));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndEmptyPathResolvesSchemeAndAuthorityAndQueryToTheBase() throws Exception {
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
    public void aRelativeReferenceWithAuthorityAndEmptyPathResolvesSchemeAndAuthorityAndQueryAndFragmentToTheBase() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        Authority baseAuthority = anAuthority();
        AbsolutePath<String> basePath = anAbsolutePath();
        Query<String> baseQuery = aQuery();
        Fragment baseFragment = aFragment();
        Authority relativeReferenceAuthority = anAuthority();
        assertThat(
                aScheme().relativeReference(relativeReferenceAuthority).resolve(baseScheme, baseAuthority, basePath, baseQuery, baseFragment),
                equalTo(baseScheme.urin(relativeReferenceAuthority)));
    }

    @Test
    public void makesRelativeReferenceWithAuthorityAndQuery() throws Exception {
        Authority authority = anAuthority();
        Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(authority, query).asString(), equalTo("//" + authority.asString() + "?" + query.asString()));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndQueryIsEqualToAnotherWithTheSameAuthorityAndQuery() throws Exception {
        Authority authority = anAuthority();
        Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(authority, query), equalTo(aScheme().relativeReference(authority, query)));
        assertThat(aScheme().relativeReference(authority, query).hashCode(), equalTo(aScheme().relativeReference(authority, query).hashCode()));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndQueryIsNotEqualToAnotherWithTheADifferentAuthority() throws Exception {
        Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(anAuthority(), query), not(equalTo(aScheme().relativeReference(anAuthority(), query))));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndQueryIsNotEqualToAnotherWithTheADifferentQuery() throws Exception {
        Authority authority = anAuthority();
        assertThat(aScheme().relativeReference(authority, aQuery()), not(equalTo(aScheme().relativeReference(authority, aQuery()))));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndQueryPathIsCorrect() throws Exception {
        Authority authority = anAuthority();
        Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(authority, query).path(), equalTo(Path.<String>rootlessPath()));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndQueryQueryIsCorrect() throws Exception {
        Authority authority = anAuthority();
        Query<String> query = aQuery();
        final RelativeReference<String, Query<String>, Fragment<String>> relativeReference = aScheme().relativeReference(authority, query);
        assertThat(relativeReference.hasQuery(), equalTo(true));
        assertThat(relativeReference.query(), equalTo(query));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndQueryFragmentIsCorrect() throws Exception {
        Authority authority = anAuthority();
        Query<String> query = aQuery();
        final RelativeReference relativeReference = aScheme().relativeReference(authority, query);
        assertThat(relativeReference.hasFragment(), equalTo(false));
        assertThrowsException("Attempt to get fragment from a UrinReference that does not have one.", UnsupportedOperationException.class, new ExceptionAssert.ExceptionThrower<java.lang.UnsupportedOperationException>() {
            public void execute() throws UnsupportedOperationException {
                relativeReference.fragment();
            }
        });
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndQueryToStringIsCorrect() throws Exception {
        Authority authority = anAuthority();
        Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(authority, query).toString(), equalTo("RelativeReference{authority=" + authority + ", path=EmptyPath, query=" + query + "}"));
    }

    @Test
    public void rejectsNullInFactoryForRelativeReferenceWithAuthorityAndQuery() throws Exception {
        assertThrowsException("Null authority should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                aScheme().relativeReference((Authority) null, aQuery());
            }
        });
        assertThrowsException("Null query should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                aScheme().relativeReference(anAuthority(), (Query<String>) null);
            }
        });
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndQueryResolvesSchemeToTheBase() throws Exception {
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
    public void aRelativeReferenceWithAuthorityAndQueryResolvesSchemeAndAuthorityToTheBase() throws Exception {
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
    public void aRelativeReferenceWithAuthorityAndQueryResolvesSchemeAndAuthorityAndQueryAndFragmentToTheBase() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        Authority baseAuthority = anAuthority();
        AbsolutePath<String> basePath = anAbsolutePath();
        Query<String> baseQuery = aQuery();
        Fragment baseFragment = aFragment();
        Authority relativeReferenceAuthority = anAuthority();
        Query<String> relativeReferenceQuery = aQuery();
        assertThat(
                aScheme().relativeReference(relativeReferenceAuthority, relativeReferenceQuery).resolve(baseScheme, baseAuthority, basePath, baseQuery, baseFragment),
                equalTo(baseScheme.urin(relativeReferenceAuthority, relativeReferenceQuery)));
    }

    @Test
    public void makesRelativeReferenceWithAuthorityAndFragment() throws Exception {
        Authority authority = anAuthority();
        Fragment fragment = aFragment();
        assertThat(aScheme().relativeReference(authority, fragment).asString(), equalTo("//" + authority.asString() + "#" + fragment.asString()));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndFragmentIsEqualToAnotherWithTheSameAuthorityAndFragment() throws Exception {
        Authority authority = anAuthority();
        Fragment fragment = aFragment();
        assertThat(aScheme().relativeReference(authority, fragment), equalTo(aScheme().relativeReference(authority, fragment)));
        assertThat(aScheme().relativeReference(authority, fragment).hashCode(), equalTo(aScheme().relativeReference(authority, fragment).hashCode()));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndFragmentIsNotEqualToAnotherWithTheADifferentAuthority() throws Exception {
        Fragment fragment = aFragment();
        assertThat(aScheme().relativeReference(anAuthority(), fragment), not(equalTo(aScheme().relativeReference(anAuthority(), fragment))));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndFragmentIsNotEqualToAnotherWithTheADifferentFragment() throws Exception {
        Authority authority = anAuthority();
        assertThat(aScheme().relativeReference(authority, aFragment()), not(equalTo(aScheme().relativeReference(authority, aFragment()))));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndFragmentPathIsCorrect() throws Exception {
        Authority authority = anAuthority();
        Fragment fragment = aFragment();
        assertThat(aScheme().relativeReference(authority, fragment).path(), equalTo(Path.<String>rootlessPath()));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndFragmentQueryIsCorrect() throws Exception {
        Authority authority = anAuthority();
        Fragment fragment = aFragment();
        final RelativeReference relativeReference = aScheme().relativeReference(authority, fragment);
        assertThat(relativeReference.hasQuery(), equalTo(false));
        assertThrowsException("Attempt to get query from a UrinReference that does not have one.", UnsupportedOperationException.class, new ExceptionAssert.ExceptionThrower<java.lang.UnsupportedOperationException>() {
            public void execute() throws UnsupportedOperationException {
                relativeReference.query();
            }
        });
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndFragmentFragmentIsCorrect() throws Exception {
        Authority authority = anAuthority();
        Fragment fragment = aFragment();
        RelativeReference relativeReference = aScheme().relativeReference(authority, fragment);
        assertThat(relativeReference.hasFragment(), equalTo(true));
        assertThat(relativeReference.fragment(), equalTo(fragment));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndFragmentToStringIsCorrect() throws Exception {
        Authority authority = anAuthority();
        Fragment fragment = aFragment();
        assertThat(aScheme().relativeReference(authority, fragment).toString(), equalTo("RelativeReference{authority=" + authority + ", path=EmptyPath, fragment=" + fragment + "}"));
    }

    @Test
    public void rejectsNullInFactoryForRelativeReferenceWithAuthorityAndFragment() throws Exception {
        assertThrowsException("Null authority should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                aScheme().relativeReference((Authority) null, aFragment());
            }
        });
        assertThrowsException("Null fragment should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                aScheme().relativeReference(anAuthority(), (Fragment) null);
            }
        });
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndFragmentResolvesSchemeToTheBase() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        Authority baseAuthority = anAuthority();
        AbsolutePath<String> basePath = anAbsolutePath();
        Authority relativeReferenceAuthority = anAuthority();
        Fragment relativeReferenceFragment = aFragment();
        assertThat(
                aScheme().relativeReference(relativeReferenceAuthority, relativeReferenceFragment).resolve(baseScheme, baseAuthority, basePath),
                equalTo(baseScheme.urin(relativeReferenceAuthority, relativeReferenceFragment)));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndFragmentResolvesSchemeAndAuthorityAndQueryToTheBase() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        Authority baseAuthority = anAuthority();
        AbsolutePath<String> basePath = anAbsolutePath();
        Query<String> baseQuery = aQuery();
        Authority relativeReferenceAuthority = anAuthority();
        Fragment relativeReferenceFragment = aFragment();
        assertThat(
                aScheme().relativeReference(relativeReferenceAuthority, relativeReferenceFragment).resolve(baseScheme, baseAuthority, basePath, baseQuery),
                equalTo(baseScheme.urin(relativeReferenceAuthority, relativeReferenceFragment)));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndFragmentResolvesSchemeAndAuthorityAndQueryAndFragmentToTheBase() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        Authority baseAuthority = anAuthority();
        AbsolutePath<String> basePath = anAbsolutePath();
        Query<String> baseQuery = aQuery();
        Fragment baseFragment = aFragment();
        Authority relativeReferenceAuthority = anAuthority();
        Fragment relativeReferenceFragment = aFragment();
        assertThat(
                aScheme().relativeReference(relativeReferenceAuthority, relativeReferenceFragment).resolve(baseScheme, baseAuthority, basePath, baseQuery, baseFragment),
                equalTo(baseScheme.urin(relativeReferenceAuthority, relativeReferenceFragment)));
    }

    @Test
    public void makesRelativeReferenceWithAuthorityAndQueryAndFragment() throws Exception {
        Authority authority = anAuthority();
        Query<String> query = aQuery();
        Fragment fragment = aFragment();
        assertThat(aScheme().relativeReference(authority, query, fragment).asString(), equalTo("//" + authority.asString() + "?" + query.asString() + "#" + fragment.asString()));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndQueryAndFragmentIsEqualToAnotherWithTheSameAuthorityAndQueryAndFragment() throws Exception {
        Authority authority = anAuthority();
        Query<String> query = aQuery();
        Fragment fragment = aFragment();
        assertThat(aScheme().relativeReference(authority, query, fragment), equalTo(aScheme().relativeReference(authority, query, fragment)));
        assertThat(aScheme().relativeReference(authority, query, fragment).hashCode(), equalTo(aScheme().relativeReference(authority, query, fragment).hashCode()));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndQueryAndFragmentIsNotEqualToAnotherWithTheADifferentAuthority() throws Exception {
        Query<String> query = aQuery();
        Fragment fragment = aFragment();
        assertThat(aScheme().relativeReference(anAuthority(), query, fragment), not(equalTo(aScheme().relativeReference(anAuthority(), query, fragment))));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndQueryAndFragmentIsNotEqualToAnotherWithTheADifferentQuery() throws Exception {
        Authority authority = anAuthority();
        Fragment fragment = aFragment();
        assertThat(aScheme().relativeReference(authority, aQuery(), fragment), not(equalTo(aScheme().relativeReference(authority, aQuery(), fragment))));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndQueryAndFragmentIsNotEqualToAnotherWithTheADifferentFragment() throws Exception {
        Authority authority = anAuthority();
        Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(authority, query, aFragment()), not(equalTo(aScheme().relativeReference(authority, query, aFragment()))));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndQueryAndFragmentPathIsCorrect() throws Exception {
        Authority authority = anAuthority();
        Query<String> query = aQuery();
        Fragment fragment = aFragment();
        assertThat(aScheme().relativeReference(authority, query, fragment).path(), equalTo(Path.<String>rootlessPath()));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndQueryAndFragmentQueryIsCorrect() throws Exception {
        Authority authority = anAuthority();
        Query<String> query = aQuery();
        Fragment fragment = aFragment();
        RelativeReference<String, Query<String>, Fragment<String>> relativeReference = aScheme().relativeReference(authority, query, fragment);
        assertThat(relativeReference.hasQuery(), equalTo(true));
        assertThat(relativeReference.query(), equalTo(query));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndQueryAndFragmentFragmentIsCorrect() throws Exception {
        Authority authority = anAuthority();
        Query<String> query = aQuery();
        Fragment fragment = aFragment();
        RelativeReference relativeReference = aScheme().relativeReference(authority, query, fragment);
        assertThat(relativeReference.hasFragment(), equalTo(true));
        assertThat(relativeReference.fragment(), equalTo(fragment));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndQueryAndFragmentToStringIsCorrect() throws Exception {
        Authority authority = anAuthority();
        Query<String> query = aQuery();
        Fragment fragment = aFragment();
        assertThat(aScheme().relativeReference(authority, query, fragment).toString(), equalTo("RelativeReference{authority=" + authority + ", path=EmptyPath, query=" + query + ", fragment=" + fragment + "}"));
    }

    @Test
    public void rejectsNullInFactoryForRelativeReferenceWithAuthorityAndQueryAndFragment() throws Exception {
        assertThrowsException("Null authority should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                aScheme().relativeReference((Authority) null, aQuery(), aFragment());
            }
        });
        assertThrowsException("Null query should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                Query<String> query = null;
                aScheme().relativeReference(anAuthority(), query, aFragment());
            }
        });
        assertThrowsException("Null fragment should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                aScheme().relativeReference(anAuthority(), aQuery(), null);
            }
        });
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndQueryAndFragmentResolvesSchemeToTheBase() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        Authority baseAuthority = anAuthority();
        AbsolutePath<String> basePath = anAbsolutePath();
        Authority relativeReferenceAuthority = anAuthority();
        Query<String> relativeReferenceQuery = aQuery();
        Fragment relativeReferenceFragment = aFragment();
        assertThat(
                aScheme().relativeReference(relativeReferenceAuthority, relativeReferenceQuery, relativeReferenceFragment).resolve(baseScheme, baseAuthority, basePath),
                equalTo(baseScheme.urin(relativeReferenceAuthority, relativeReferenceQuery, relativeReferenceFragment)));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndQueryAndFragmentResolvesSchemeAndAuthorityAndQueryToTheBase() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        Authority baseAuthority = anAuthority();
        AbsolutePath<String> basePath = anAbsolutePath();
        Query<String> baseQuery = aQuery();
        Authority relativeReferenceAuthority = anAuthority();
        Query<String> relativeReferenceQuery = aQuery();
        Fragment relativeReferenceFragment = aFragment();
        assertThat(
                aScheme().relativeReference(relativeReferenceAuthority, relativeReferenceQuery, relativeReferenceFragment).resolve(baseScheme, baseAuthority, basePath, baseQuery),
                equalTo(baseScheme.urin(relativeReferenceAuthority, relativeReferenceQuery, relativeReferenceFragment)));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndQueryAndFragmentResolvesSchemeAndAuthorityAndQueryAndFragmentToTheBase() throws Exception {
        Scheme<String, Query<String>, Fragment<String>> baseScheme = aScheme();
        Authority baseAuthority = anAuthority();
        AbsolutePath<String> basePath = anAbsolutePath();
        Query<String> baseQuery = aQuery();
        Fragment baseFragment = aFragment();
        Authority relativeReferenceAuthority = anAuthority();
        Query<String> relativeReferenceQuery = aQuery();
        Fragment relativeReferenceFragment = aFragment();
        assertThat(
                aScheme().relativeReference(relativeReferenceAuthority, relativeReferenceQuery, relativeReferenceFragment).resolve(baseScheme, baseAuthority, basePath, baseQuery, baseFragment),
                equalTo(baseScheme.urin(relativeReferenceAuthority, relativeReferenceQuery, relativeReferenceFragment)));
    }

    @Test
    public void makesRelativeReferenceWithAuthorityAndNonEmptyPath() throws Exception {
        Authority authority = anAuthority();
        Segment<String> firstSegment = aNonDotSegment();
        Segment<String> secondSegment = aNonDotSegment();
        assertThat(aScheme().relativeReference(authority, Path.path(firstSegment, secondSegment)).asString(), equalTo("//" + authority.asString() + "/" + firstSegment.asString() + "/" + secondSegment.asString()));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndPathIsEqualToAnotherWithTheSameAuthorityAndPath() throws Exception {
        Authority authority = anAuthority();
        AbsolutePath<String> absolutePath = anAbsolutePath();
        assertThat(aScheme().relativeReference(authority, absolutePath), equalTo(aScheme().relativeReference(authority, absolutePath)));
        assertThat(aScheme().relativeReference(authority, absolutePath).hashCode(), equalTo(aScheme().relativeReference(authority, absolutePath).hashCode()));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndPathIsNotEqualToAnotherWithTheADifferentAuthorityAndPath() throws Exception {
        assertThat(aScheme().relativeReference(anAuthority(), anAbsolutePath()), not(equalTo(aScheme().relativeReference(anAuthority(), anAbsolutePath()))));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndPathPathIsCorrect() throws Exception {
        Authority authority = anAuthority();
        AbsolutePath<String> absolutePath = anAbsolutePath();
        assertThat(aScheme().relativeReference(authority, absolutePath).path(), equalTo((Path<String>) absolutePath));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndPathQueryIsCorrect() throws Exception {
        Authority authority = anAuthority();
        AbsolutePath<String> absolutePath = anAbsolutePath();
        final RelativeReference relativeReference = aScheme().relativeReference(authority, absolutePath);
        assertThat(relativeReference.hasQuery(), equalTo(false));
        assertThrowsException("Attempt to get query from a UrinReference that does not have one.", UnsupportedOperationException.class, new ExceptionAssert.ExceptionThrower<java.lang.UnsupportedOperationException>() {
            public void execute() throws UnsupportedOperationException {
                relativeReference.query();
            }
        });
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndPathFragmentIsCorrect() throws Exception {
        Authority authority = anAuthority();
        AbsolutePath<String> absolutePath = anAbsolutePath();
        final RelativeReference relativeReference = aScheme().relativeReference(authority, absolutePath);
        assertThat(relativeReference.hasFragment(), equalTo(false));
        assertThrowsException("Attempt to get fragment from a UrinReference that does not have one.", UnsupportedOperationException.class, new ExceptionAssert.ExceptionThrower<java.lang.UnsupportedOperationException>() {
            public void execute() throws UnsupportedOperationException {
                relativeReference.fragment();
            }
        });
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndPathToStringIsCorrect() throws Exception {
        Authority authority = anAuthority();
        AbsolutePath<String> absolutePath = anAbsolutePath();
        assertThat(aScheme().relativeReference(authority, absolutePath).toString(), equalTo("RelativeReference{authority=" + authority + ", path=" + absolutePath + "}"));
    }

    @Test
    public void rejectsNullInFactoryForRelativeReferenceWithAuthorityAndPath() throws Exception {
        assertThrowsException("Null authority should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Authority authority = null;
                aScheme().relativeReference(authority, anAbsolutePath());
            }
        });
        assertThrowsException("Null path should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                AbsolutePath<String> absolutePath = null;
                aScheme().relativeReference(anAuthority(), absolutePath);
            }
        });
    }

    @Test
    public void makesRelativeReferenceWithAuthorityAndPathAndQuery() throws Exception {
        Authority authority = anAuthority();
        AbsolutePath<String> absolutePath = anAbsolutePath();
        Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(authority, absolutePath, query).asString(), equalTo("//" + authority.asString() + absolutePath.asString(NEVER_PREFIX_WITH_DOT_SEGMENT) + "?" + query.asString()));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndPathAndQueryIsEqualToAnotherWithTheSameAuthorityAndPathAndQuery() throws Exception {
        Authority authority = anAuthority();
        AbsolutePath<String> absolutePath = anAbsolutePath();
        Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(authority, absolutePath, query), equalTo(aScheme().relativeReference(authority, absolutePath, query)));
        assertThat(aScheme().relativeReference(authority, absolutePath, query).hashCode(), equalTo(aScheme().relativeReference(authority, absolutePath, query).hashCode()));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndPathIsNotEqualToAnotherWithTheADifferentAuthorityAndPathAndQuery() throws Exception {
        assertThat(aScheme().relativeReference(anAuthority(), anAbsolutePath(), aQuery()), not(equalTo(aScheme().relativeReference(anAuthority(), anAbsolutePath(), aQuery()))));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndPathAndQueryPathIsCorrect() throws Exception {
        Authority authority = anAuthority();
        AbsolutePath<String> absolutePath = anAbsolutePath();
        Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(authority, absolutePath, query).path(), equalTo((Path<String>) absolutePath));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndPathAndQueryQueryIsCorrect() throws Exception {
        Authority authority = anAuthority();
        AbsolutePath<String> absolutePath = anAbsolutePath();
        Query<String> query = aQuery();
        final RelativeReference<String, Query<String>, Fragment<String>> relativeReference = aScheme().relativeReference(authority, absolutePath, query);
        assertThat(relativeReference.hasQuery(), equalTo(true));
        assertThat(relativeReference.query(), equalTo(query));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndPathAndQueryFragmentIsCorrect() throws Exception {
        Authority authority = anAuthority();
        AbsolutePath<String> absolutePath = anAbsolutePath();
        Query<String> query = aQuery();
        final RelativeReference relativeReference = aScheme().relativeReference(authority, absolutePath, query);
        assertThat(relativeReference.hasFragment(), equalTo(false));
        assertThrowsException("Attempt to get fragment from a UrinReference that does not have one.", UnsupportedOperationException.class, new ExceptionAssert.ExceptionThrower<java.lang.UnsupportedOperationException>() {
            public void execute() throws UnsupportedOperationException {
                relativeReference.fragment();
            }
        });
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndPathAndQueryToStringIsCorrect() throws Exception {
        Authority authority = anAuthority();
        AbsolutePath<String> absolutePath = anAbsolutePath();
        Query<String> query = aQuery();
        assertThat(aScheme().relativeReference(authority, absolutePath, query).toString(), equalTo("RelativeReference{authority=" + authority + ", path=" + absolutePath + ", query=" + query + "}"));
    }

    @Test
    public void rejectsNullInFactoryForRelativeReferenceWithAuthorityAndPathAndQuery() throws Exception {
        assertThrowsException("Null authority should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                aScheme().relativeReference(null, anAbsolutePath(), aQuery());
            }
        });
        assertThrowsException("Null path should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                aScheme().relativeReference(anAuthority(), null, aQuery());
            }
        });
        assertThrowsException("Null query should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                aScheme().relativeReference(anAuthority(), anAbsolutePath(), (Query<String>) null);
            }
        });
    }

    @Test
    public void makesRelativeReferenceWithAuthorityAndPathAndFragment() throws Exception {
        Authority authority = anAuthority();
        AbsolutePath<String> absolutePath = anAbsolutePath();
        Fragment fragment = aFragment();
        assertThat(aScheme().relativeReference(authority, absolutePath, fragment).asString(), equalTo("//" + authority.asString() + absolutePath.asString(NEVER_PREFIX_WITH_DOT_SEGMENT) + "#" + fragment.asString()));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndPathAndFragmentIsEqualToAnotherWithTheSameAuthorityAndPathAndFragment() throws Exception {
        Authority authority = anAuthority();
        AbsolutePath<String> absolutePath = anAbsolutePath();
        Fragment fragment = aFragment();
        assertThat(aScheme().relativeReference(authority, absolutePath, fragment), equalTo(aScheme().relativeReference(authority, absolutePath, fragment)));
        assertThat(aScheme().relativeReference(authority, absolutePath, fragment).hashCode(), equalTo(aScheme().relativeReference(authority, absolutePath, fragment).hashCode()));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndPathIsNotEqualToAnotherWithTheADifferentAuthorityAndPathAndFragment() throws Exception {
        assertThat(aScheme().relativeReference(anAuthority(), anAbsolutePath(), aFragment()), not(equalTo(aScheme().relativeReference(anAuthority(), anAbsolutePath(), aFragment()))));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndPathAndFragmentPathIsCorrect() throws Exception {
        Authority authority = anAuthority();
        AbsolutePath<String> absolutePath = anAbsolutePath();
        Fragment fragment = aFragment();
        assertThat(aScheme().relativeReference(authority, absolutePath, fragment).path(), equalTo((Path<String>) absolutePath));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndPathAndFragmentQueryIsCorrect() throws Exception {
        Authority authority = anAuthority();
        AbsolutePath<String> absolutePath = anAbsolutePath();
        Fragment fragment = aFragment();
        final RelativeReference relativeReference = aScheme().relativeReference(authority, absolutePath, fragment);
        assertThat(relativeReference.hasQuery(), equalTo(false));
        assertThrowsException("Attempt to get query from a UrinReference that does not have one.", UnsupportedOperationException.class, new ExceptionAssert.ExceptionThrower<java.lang.UnsupportedOperationException>() {
            public void execute() throws UnsupportedOperationException {
                relativeReference.query();
            }
        });
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndPathAndFragmentFragmentIsCorrect() throws Exception {
        Authority authority = anAuthority();
        AbsolutePath<String> absolutePath = anAbsolutePath();
        Fragment fragment = aFragment();
        RelativeReference relativeReference = aScheme().relativeReference(authority, absolutePath, fragment);
        assertThat(relativeReference.hasFragment(), equalTo(true));
        assertThat(relativeReference.fragment(), equalTo(fragment));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndPathAndFragmentToStringIsCorrect() throws Exception {
        Authority authority = anAuthority();
        AbsolutePath<String> absolutePath = anAbsolutePath();
        Fragment fragment = aFragment();
        assertThat(aScheme().relativeReference(authority, absolutePath, fragment).toString(), equalTo("RelativeReference{authority=" + authority + ", path=" + absolutePath + ", fragment=" + fragment + "}"));
    }

    @Test
    public void rejectsNullInFactoryForRelativeReferenceWithAuthorityAndPathAndFragment() throws Exception {
        assertThrowsException("Null authority should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                aScheme().relativeReference(null, anAbsolutePath(), aFragment());
            }
        });
        assertThrowsException("Null path should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                aScheme().relativeReference(anAuthority(), (AbsolutePath<String>) null, aFragment());
            }
        });
        assertThrowsException("Null fragment should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                aScheme().relativeReference(anAuthority(), anAbsolutePath(), (Fragment) null);
            }
        });
    }

    @Test
    public void makesRelativeReferenceWithAuthorityAndPathAndQueryAndFragment() throws Exception {
        Authority authority = anAuthority();
        AbsolutePath<String> absolutePath = anAbsolutePath();
        Query<String> query = aQuery();
        Fragment fragment = aFragment();
        assertThat(aScheme().relativeReference(authority, absolutePath, query, fragment).asString(), equalTo("//" + authority.asString() + absolutePath.asString(NEVER_PREFIX_WITH_DOT_SEGMENT) + "?" + query.asString() + "#" + fragment.asString()));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndPathAndQueryAndFragmentIsEqualToAnotherWithTheSameAuthorityAndPathAndQueryAndFragment() throws Exception {
        Authority authority = anAuthority();
        AbsolutePath<String> absolutePath = anAbsolutePath();
        Query<String> query = aQuery();
        Fragment fragment = aFragment();
        assertThat(aScheme().relativeReference(authority, absolutePath, query, fragment), equalTo(aScheme().relativeReference(authority, absolutePath, query, fragment)));
        assertThat(aScheme().relativeReference(authority, absolutePath, query, fragment).hashCode(), equalTo(aScheme().relativeReference(authority, absolutePath, query, fragment).hashCode()));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndPathIsNotEqualToAnotherWithTheADifferentAuthorityAndPathAndQueryAndFragment() throws Exception {
        assertThat(aScheme().relativeReference(anAuthority(), anAbsolutePath(), aQuery(), aFragment()), not(equalTo(aScheme().relativeReference(anAuthority(), anAbsolutePath(), aQuery(), aFragment()))));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndPathAndQueryAndFragmentPathIsCorrect() throws Exception {
        Authority authority = anAuthority();
        AbsolutePath<String> absolutePath = anAbsolutePath();
        Query<String> query = aQuery();
        Fragment fragment = aFragment();
        assertThat(aScheme().relativeReference(authority, absolutePath, query, fragment).path(), equalTo((Path<String>) absolutePath));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndPathAndQueryAndFragmentQueryIsCorrect() throws Exception {
        Authority authority = anAuthority();
        AbsolutePath<String> absolutePath = anAbsolutePath();
        Query<String> query = aQuery();
        Fragment fragment = aFragment();
        RelativeReference<String, Query<String>, Fragment<String>> relativeReference = aScheme().relativeReference(authority, absolutePath, query, fragment);
        assertThat(relativeReference.hasQuery(), equalTo(true));
        assertThat(relativeReference.query(), equalTo(query));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndPathAndQueryAndFragmentFragmentIsCorrect() throws Exception {
        Authority authority = anAuthority();
        AbsolutePath<String> absolutePath = anAbsolutePath();
        Query<String> query = aQuery();
        Fragment fragment = aFragment();
        RelativeReference relativeReference = aScheme().relativeReference(authority, absolutePath, query, fragment);
        assertThat(relativeReference.hasFragment(), equalTo(true));
        assertThat(relativeReference.fragment(), equalTo(fragment));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndPathAndQueryAndFragmentToStringIsCorrect() throws Exception {
        Authority authority = anAuthority();
        AbsolutePath<String> absolutePath = anAbsolutePath();
        Query<String> query = aQuery();
        Fragment fragment = aFragment();
        assertThat(aScheme().relativeReference(authority, absolutePath, query, fragment).toString(), equalTo("RelativeReference{authority=" + authority + ", path=" + absolutePath + ", query=" + query + ", fragment=" + fragment + "}"));
    }

    @Test
    public void rejectsNullInFactoryForRelativeReferenceWithAuthorityAndPathAndQueryAndFragment() throws Exception {
        assertThrowsException("Null authority should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                aScheme().relativeReference(null, anAbsolutePath(), aQuery(), aFragment());
            }
        });
        assertThrowsException("Null path should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                aScheme().relativeReference(anAuthority(), null, aQuery(), aFragment());
            }
        });
        assertThrowsException("Null query should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                aScheme().relativeReference(anAuthority(), anAbsolutePath(), null, aFragment());
            }
        });
        assertThrowsException("Null fragment should throw NullPointerException in factory", NullPointerException.class, new ExceptionAssert.ExceptionThrower<NullPointerException>() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                aScheme().relativeReference(anAuthority(), anAbsolutePath(), aQuery(), null);
            }
        });
    }

    @Test
    public void parsesARelativeReferenceWithEmptyPath() throws Exception {
        assertThat(aScheme().parseRelativeReference(""), equalTo(aScheme().relativeReference()));
    }

    @Test
    public void parsesARelativeReferenceWithEmptyPathWithQuery() throws Exception {
        Query<String> query = aQuery();
        assertThat(aScheme().parseRelativeReference("?" + query.asString()), equalTo(aScheme().relativeReference(query)));
    }

    @Test
    public void parsesARelativeReferenceWithEmptyPathWithFragment() throws Exception {
        Fragment fragment = aFragment();
        assertThat(aScheme().parseRelativeReference("#" + fragment.asString()), equalTo(aScheme().relativeReference(fragment)));
    }

    @Test
    public void parsesARelativeReferenceWithEmptyPathWithQueryAndFragment() throws Exception {
        Query<String> query = aQuery();
        Fragment fragment = aFragment();
        assertThat(aScheme().parseRelativeReference("?" + query.asString() + "#" + fragment.asString()), equalTo(aScheme().relativeReference(query, fragment)));
    }

    @Test
    public void parsesARelativeReferenceWithPathAndQuery() throws Exception {
        Path<String> path = anUnpollutedPath();
        Query<String> query = aQuery();
        assertThat(aScheme().parseRelativeReference(path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON) + "?" + query.asString()), equalTo(aScheme().relativeReference(path, query)));
    }

    @Test
    public void parsesARelativeReferenceWithPathAndFragment() throws Exception {
        Path<String> path = anUnpollutedPath();
        Fragment fragment = aFragment();
        assertThat(aScheme().parseRelativeReference(path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON) + "#" + fragment.asString()), equalTo(aScheme().relativeReference(path, fragment)));
    }

    @Test
    public void parsesARelativeReferenceWithPathAndQueryAndFragment() throws Exception {
        Path<String> path = anUnpollutedPath();
        Query<String> query = aQuery();
        Fragment fragment = aFragment();
        assertThat(aScheme().parseRelativeReference(path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON) + "?" + query.asString() + "#" + fragment.asString()), equalTo(aScheme().relativeReference(path, query, fragment)));
    }

    @Test
    public void parsesASimpleAbsolutePath() throws Exception {
        Segment<String> firstSegment = aSegment();
        Segment<String> secondSegment = aSegment();
        assertThat(aScheme().parseRelativeReference("/" + firstSegment.asString() + "/" + secondSegment.asString()), equalTo(aScheme().relativeReference(Path.path(firstSegment, secondSegment))));
    }

    @Test
    public void parsesASimpleAbsolutePathPrefixedWithADotSegment() throws Exception {
        Segment<String> firstSegment = segment("");
        Segment<String> secondSegment = aSegment();
        assertThat(aScheme().parseRelativeReference("/./" + firstSegment.asString() + "/" + secondSegment.asString()), equalTo(aScheme().relativeReference(Path.path(firstSegment, secondSegment))));
    }

    @Test
    public void parsesASimpleRootlessPath() throws Exception {
        Segment<String> firstSegment = aSegment();
        Segment<String> secondSegment = aSegment();
        assertThat(aScheme().parseRelativeReference(firstSegment.asString() + "/" + secondSegment.asString()), equalTo(aScheme().relativeReference(rootlessPath(firstSegment, secondSegment))));
    }

    @Test
    public void parsesARelativeReferenceWithAuthorityAndEmptyPath() throws Exception {
        Authority authority = anAuthority();
        assertThat(aScheme().parseRelativeReference("//" + authority.asString()), equalTo(aScheme().relativeReference(authority)));
    }

    @Test
    public void parsesARelativeReferenceWithAuthorityAndQuery() throws Exception {
        Authority authority = anAuthority();
        Query<String> query = aQuery();
        assertThat(aScheme().parseRelativeReference("//" + authority.asString() + "?" + query.asString()), equalTo(aScheme().relativeReference(authority, query)));
    }

    @Test
    public void parsesARelativeReferenceWithAuthorityAndFragment() throws Exception {
        Authority authority = anAuthority();
        Fragment fragment = aFragment();
        assertThat(aScheme().parseRelativeReference("//" + authority.asString() + "#" + fragment.asString()), equalTo(aScheme().relativeReference(authority, fragment)));
    }

    @Test
    public void parsesARelativeReferenceWithAuthorityAndQueryAndFragment() throws Exception {
        Authority authority = anAuthority();
        Query<String> query = aQuery();
        Fragment fragment = aFragment();
        assertThat(aScheme().parseRelativeReference("//" + authority.asString() + "?" + query.asString() + "#" + fragment.asString()), equalTo(aScheme().relativeReference(authority, query, fragment)));
    }

    @Test
    public void parsesARelativeReferenceWithAuthorityAndNonEmptyPath() throws Exception {
        Authority authority = anAuthority();
        Segment<String> firstSegment = aSegment();
        Segment<String> secondSegment = aSegment();
        assertThat(aScheme().parseRelativeReference("//" + authority.asString() + "/" + firstSegment.asString() + "/" + secondSegment.asString()), equalTo(aScheme().relativeReference(authority, Path.path(firstSegment, secondSegment))));
    }

    @Test
    public void parsesRelativeReferenceWithAuthorityAndPathAndQuery() throws Exception {
        Authority authority = anAuthority();
        AbsolutePath<String> absolutePath = anUnpollutedAbsolutePath();
        Query<String> query = aQuery();
        assertThat(aScheme().parseRelativeReference("//" + authority.asString() + absolutePath.asString(NEVER_PREFIX_WITH_DOT_SEGMENT) + "?" + query.asString()), equalTo(aScheme().relativeReference(authority, absolutePath, query)));
    }

    @Test
    public void parsesRelativeReferenceWithAuthorityAndPathAndFragment() throws Exception {
        Authority authority = anAuthority();
        AbsolutePath<String> absolutePath = anUnpollutedAbsolutePath();
        Fragment fragment = aFragment();
        assertThat(aScheme().parseRelativeReference("//" + authority.asString() + absolutePath.asString(NEVER_PREFIX_WITH_DOT_SEGMENT) + "#" + fragment.asString()), equalTo(aScheme().relativeReference(authority, absolutePath, fragment)));
    }

    @Test
    public void parsesRelativeReferenceWithAuthorityAndPathAndQueryAndFragment() throws Exception {
        Authority authority = anAuthority();
        AbsolutePath<String> absolutePath = anUnpollutedAbsolutePath();
        Query<String> query = aQuery();
        Fragment fragment = aFragment();
        assertThat(aScheme().parseRelativeReference("//" + authority.asString() + absolutePath.asString(NEVER_PREFIX_WITH_DOT_SEGMENT) + "?" + query.asString() + "#" + fragment.asString()), equalTo(aScheme().relativeReference(authority, absolutePath, query, fragment)));
    }

    @Test
    public void parsesRelativeReferenceFromUri() throws Exception {
        Authority authority = authority(aRegisteredName());
        AbsolutePath<String> absolutePath = anUnpollutedAbsolutePath();
        Query<String> query = aQuery();
        Fragment fragment = aFragment();
        assertThat(aScheme().parseRelativeReference(URI.create("//" + authority.asString() + absolutePath.asString(NEVER_PREFIX_WITH_DOT_SEGMENT) + "?" + query.asString() + "#" + fragment.asString())), equalTo(aScheme().relativeReference(authority, absolutePath, query, fragment)));
    }

}
