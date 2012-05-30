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

import static net.sourceforge.urin.AuthorityBuilder.anAuthority;
import static net.sourceforge.urin.FragmentBuilder.aFragment;
import static net.sourceforge.urin.HierarchicalPart.hierarchicalPart;
import static net.sourceforge.urin.MoreRandomStringUtils.aStringIncluding;
import static net.sourceforge.urin.NullTest.assertThrowsNullPointerException;
import static net.sourceforge.urin.Path.PrefixWithDotSegmentCriteria.NEVER_PREFIX_WITH_DOT_SEGMENT;
import static net.sourceforge.urin.Path.PrefixWithDotSegmentCriteria.PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON;
import static net.sourceforge.urin.Path.rootlessPath;
import static net.sourceforge.urin.PathBuilder.aPath;
import static net.sourceforge.urin.PathBuilder.anAbsolutePath;
import static net.sourceforge.urin.QueryBuilder.aQuery;
import static net.sourceforge.urin.RelativeReference.parse;
import static net.sourceforge.urin.RelativeReference.relativeReference;
import static net.sourceforge.urin.SchemeBuilder.aScheme;
import static net.sourceforge.urin.Segment.DOT;
import static net.sourceforge.urin.Segment.segment;
import static net.sourceforge.urin.SegmentBuilder.aNonDotSegment;
import static net.sourceforge.urin.SegmentBuilder.aSegment;
import static net.sourceforge.urin.Urin.urin;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class RelativeReferenceTest {
    @Test
    public void aRelativeReferenceWithEmptyPathAsStringIsCorrect() throws Exception {
        assertThat(relativeReference().asString(), equalTo(""));
        assertThat(relativeReference().asUri(), equalTo(URI.create("")));
    }

    @Test
    public void aRelativeReferenceWithEmptyPathIsEqualToAnotherRelativeReferenceWithEmptyPath() throws Exception {
        assertThat(relativeReference(), equalTo(relativeReference()));
        assertThat(relativeReference().hashCode(), equalTo(relativeReference().hashCode()));
    }

    @Test
    public void aRelativeReferenceWithEmptyPathToStringIsCorrect() throws Exception {
        assertThat(relativeReference().toString(), equalTo("RelativeReference{path=EmptyPath}"));
    }

    @Test
    public void aRelativeReferenceWithEmptyPathWithQueryAsStringIsCorrect() throws Exception {
        Query query = aQuery();
        assertThat(relativeReference(query).asString(), equalTo("?" + query.asString()));
        assertThat(relativeReference(query).asUri(), equalTo(URI.create("?" + query.asString())));
    }

    @Test
    public void aRelativeReferenceWithEmptyPathWithQueryIsEqualToAnotherRelativeReferenceWithEmptyPath() throws Exception {
        Query query = aQuery();
        assertThat(relativeReference(query), equalTo(relativeReference(query)));
        assertThat(relativeReference(query).hashCode(), equalTo(relativeReference(query).hashCode()));
    }

    @Test
    public void aRelativeReferenceWithEmptyPathWithQueryIsNotEqualToAnotherWithTheADifferentQuery() throws Exception {
        assertThat(relativeReference(aQuery()), not(equalTo(relativeReference(aQuery()))));
    }

    @Test
    public void aRelativeReferenceWithEmptyPathWithQueryToStringIsCorrect() throws Exception {
        Query query = aQuery();
        assertThat(relativeReference(query).toString(), equalTo("RelativeReference{path=EmptyPath, query=" + query.toString() + "}"));
    }

    @Test
    public void rejectsNullInFactoryForARelativeReferenceWithEmptyPathWithQuery() throws Exception {
        assertThrowsNullPointerException("Null query should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Query query = null;
                relativeReference(query);
            }
        });
    }

    @Test
    public void aRelativeReferenceWithEmptyPathWithFragmentAsStringIsCorrect() throws Exception {
        Fragment fragment = aFragment();
        assertThat(relativeReference(fragment).asString(), equalTo("#" + fragment.asString()));
        assertThat(relativeReference(fragment).asUri(), equalTo(URI.create("#" + fragment.asString())));
    }

    @Test
    public void aRelativeReferenceWithEmptyPathWithFragmentIsEqualToAnotherRelativeReferenceWithEmptyPath() throws Exception {
        Fragment fragment = aFragment();
        assertThat(relativeReference(fragment), equalTo(relativeReference(fragment)));
        assertThat(relativeReference(fragment).hashCode(), equalTo(relativeReference(fragment).hashCode()));
    }

    @Test
    public void aRelativeReferenceWithEmptyPathWithFragmentIsNotEqualToAnotherWithTheADifferentFragment() throws Exception {
        assertThat(relativeReference(aFragment()), not(equalTo(relativeReference(aFragment()))));
    }

    @Test
    public void aRelativeReferenceWithEmptyPathWithFragmentToStringIsCorrect() throws Exception {
        Fragment fragment = aFragment();
        assertThat(relativeReference(fragment).toString(), equalTo("RelativeReference{path=EmptyPath, fragment=" + fragment.toString() + "}"));
    }

    @Test
    public void rejectsNullInFactoryForARelativeReferenceWithEmptyPathWithFragment() throws Exception {
        assertThrowsNullPointerException("Null fragment should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Fragment fragment = null;
                relativeReference(fragment);
            }
        });
    }

    @Test
    public void aRelativeReferenceWithEmptyPathWithQueryAndFragmentAsStringIsCorrect() throws Exception {
        Query query = aQuery();
        Fragment fragment = aFragment();
        assertThat(relativeReference(query, fragment).asString(), equalTo("?" + query.asString() + "#" + fragment.asString()));
        assertThat(relativeReference(query, fragment).asUri(), equalTo(URI.create("?" + query.asString() + "#" + fragment.asString())));
    }

    @Test
    public void aRelativeReferenceWithEmptyPathWithQueryAndFragmentIsEqualToAnotherRelativeReferenceWithEmptyPath() throws Exception {
        Query query = aQuery();
        Fragment fragment = aFragment();
        assertThat(relativeReference(query, fragment), equalTo(relativeReference(query, fragment)));
        assertThat(relativeReference(query, fragment).hashCode(), equalTo(relativeReference(query, fragment).hashCode()));
    }

    @Test
    public void aRelativeReferenceWithEmptyPathWithQueryAndFragmentIsNotEqualToAnotherWithTheADifferentQuery() throws Exception {
        Fragment fragment = aFragment();
        assertThat(relativeReference(aQuery(), fragment), not(equalTo(relativeReference(aQuery(), fragment))));
    }

    @Test
    public void aRelativeReferenceWithEmptyPathWithQueryAndFragmentIsNotEqualToAnotherWithTheADifferentFragment() throws Exception {
        Query query = aQuery();
        assertThat(relativeReference(query, aFragment()), not(equalTo(relativeReference(query, aFragment()))));
    }

    @Test
    public void aRelativeReferenceWithEmptyPathWithQueryAndFragmentToStringIsCorrect() throws Exception {
        Query query = aQuery();
        Fragment fragment = aFragment();
        assertThat(relativeReference(query, fragment).toString(), equalTo("RelativeReference{path=EmptyPath, query=" + query + ", fragment=" + fragment + "}"));
    }

    @Test
    public void rejectsNullInFactoryForARelativeReferenceWithEmptyPathWithQueryAndFragment() throws Exception {
        assertThrowsNullPointerException("Null query should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Query query = null;
                relativeReference(query, aFragment());
            }
        });
        assertThrowsNullPointerException("Null fragment should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Fragment fragment = null;
                relativeReference(aQuery(), fragment);
            }
        });
    }

    @Test
    public void aRelativeReferenceWithPathAndQueryAsStringIsCorrect() throws Exception {
        Path path = aPath();
        Query query = aQuery();
        assertThat(relativeReference(path, query).asString(), equalTo(path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON) + "?" + query.asString()));
        assertThat(relativeReference(path, query).asUri(), equalTo(URI.create(path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON) + "?" + query.asString())));
    }

    @Test
    public void aRelativeReferenceWithPathAndQueryIsEqualToAnotherRelativeReferenceWithPathAndQuery() throws Exception {
        Path path = aPath();
        Query query = aQuery();
        assertThat(relativeReference(path, query), equalTo(relativeReference(path, query)));
        assertThat(relativeReference(path, query).hashCode(), equalTo(relativeReference(path, query).hashCode()));
    }

    @Test
    public void aRelativeReferenceWithPathAndQueryIsNotEqualToAnotherWithTheADifferentPath() throws Exception {
        Query query = aQuery();
        assertThat(relativeReference(aPath(), query), not(equalTo(relativeReference(aPath(), query))));
    }

    @Test
    public void aRelativeReferenceWithPathAndQueryIsNotEqualToAnotherWithTheADifferentQuery() throws Exception {
        Path path = aPath();
        assertThat(relativeReference(path, aQuery()), not(equalTo(relativeReference(path, aQuery()))));
    }

    @Test
    public void aRelativeReferenceWithPathAndQueryToStringIsCorrect() throws Exception {
        Path path = aPath();
        Query query = aQuery();
        assertThat(relativeReference(path, query).toString(), equalTo("RelativeReference{path=" + path.toString() + ", query=" + query.toString() + "}"));
    }

    @Test
    public void rejectsNullInFactoryForARelativeReferenceWithPathAndQuery() throws Exception {
        assertThrowsNullPointerException("Null path should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                relativeReference((Path) null, aQuery());
            }
        });
        assertThrowsNullPointerException("Null query should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                relativeReference(aPath(), (Query) null);
            }
        });
    }

    @Test
    public void aRelativeReferenceWithPathAndQueryResolvesSchemeToTheBase() throws Exception {
        Scheme baseScheme = aScheme();
        HierarchicalPart baseHierarchicalPart = hierarchicalPart(anAuthority(), anAbsolutePath());
        Path relativeReferencePath = aPath();
        Query relativeReferenceQuery = aQuery();
        assertThat(
                relativeReference(relativeReferencePath, relativeReferenceQuery).resolve(baseScheme, baseHierarchicalPart),
                equalTo(urin(baseScheme, baseHierarchicalPart.resolve(relativeReferencePath), relativeReferenceQuery)));
    }

    @Test
    public void aRelativeReferenceWithPathAndQueryResolvesSchemeAndAuthorityAndQueryToTheBase() throws Exception {
        Scheme baseScheme = aScheme();
        HierarchicalPart baseHierarchicalPart = hierarchicalPart(anAuthority(), anAbsolutePath());
        Query baseQuery = aQuery();
        Path relativeReferencePath = aPath();
        Query relativeReferenceQuery = aQuery();
        assertThat(
                relativeReference(relativeReferencePath, relativeReferenceQuery).resolve(baseScheme, baseHierarchicalPart, baseQuery),
                equalTo(urin(baseScheme, baseHierarchicalPart.resolve(relativeReferencePath), relativeReferenceQuery)));
    }

    @Test
    public void aRelativeReferenceWithPathAndQueryResolvesSchemeAndAuthorityAndQueryAndFragmentToTheBase() throws Exception {
        Scheme baseScheme = aScheme();
        HierarchicalPart baseHierarchicalPart = hierarchicalPart(anAuthority(), anAbsolutePath());
        Query baseQuery = aQuery();
        Fragment baseFragment = aFragment();
        Path relativeReferencePath = aPath();
        Query relativeReferenceQuery = aQuery();
        assertThat(
                relativeReference(relativeReferencePath, relativeReferenceQuery).resolve(baseScheme, baseHierarchicalPart, baseQuery, baseFragment),
                equalTo(urin(baseScheme, baseHierarchicalPart.resolve(relativeReferencePath), relativeReferenceQuery)));
    }

    @Test
    public void aRelativeReferenceWithPathAndFragmentAsStringIsCorrect() throws Exception {
        Path path = aPath();
        Fragment fragment = aFragment();
        assertThat(relativeReference(path, fragment).asString(), equalTo(path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON) + "#" + fragment.asString()));
        assertThat(relativeReference(path, fragment).asUri(), equalTo(URI.create(path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON) + "#" + fragment.asString())));
    }

    @Test
    public void aRelativeReferenceWithPathAndFragmentIsEqualToAnotherRelativeReferenceWithPathAndFragment() throws Exception {
        Path path = aPath();
        Fragment fragment = aFragment();
        assertThat(relativeReference(path, fragment), equalTo(relativeReference(path, fragment)));
        assertThat(relativeReference(path, fragment).hashCode(), equalTo(relativeReference(path, fragment).hashCode()));
    }

    @Test
    public void aRelativeReferenceWithPathAndFragmentIsNotEqualToAnotherWithTheADifferentPath() throws Exception {
        Fragment fragment = aFragment();
        assertThat(relativeReference(aPath(), fragment), not(equalTo(relativeReference(aPath(), fragment))));
    }

    @Test
    public void aRelativeReferenceWithPathAndFragmentIsNotEqualToAnotherWithTheADifferentFragment() throws Exception {
        Path path = aPath();
        assertThat(relativeReference(path, aFragment()), not(equalTo(relativeReference(path, aFragment()))));
    }

    @Test
    public void aRelativeReferenceWithPathAndFragmentToStringIsCorrect() throws Exception {
        Path path = aPath();
        Fragment fragment = aFragment();
        assertThat(relativeReference(path, fragment).toString(), equalTo("RelativeReference{path=" + path.toString() + ", fragment=" + fragment.toString() + "}"));
    }

    @Test
    public void rejectsNullInFactoryForARelativeReferenceWithPathAndFragment() throws Exception {
        assertThrowsNullPointerException("Null path should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                relativeReference((Path) null, aFragment());
            }
        });
        assertThrowsNullPointerException("Null fragment should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                relativeReference(aPath(), (Fragment) null);
            }
        });
    }

    @Test
    public void aRelativeReferenceWithPathAndFragmentResolvesSchemeToTheBase() throws Exception {
        Scheme baseScheme = aScheme();
        HierarchicalPart baseHierarchicalPart = hierarchicalPart(anAuthority(), anAbsolutePath());
        Path relativeReferencePath = aPath();
        Fragment relativeReferenceFragment = aFragment();
        assertThat(
                relativeReference(relativeReferencePath, relativeReferenceFragment).resolve(baseScheme, baseHierarchicalPart),
                equalTo(urin(baseScheme, baseHierarchicalPart.resolve(relativeReferencePath), relativeReferenceFragment)));
    }

    @Test
    public void aRelativeReferenceWithPathAndFragmentResolvesSchemeAndAuthorityAndQueryToTheBase() throws Exception {
        Scheme baseScheme = aScheme();
        HierarchicalPart baseHierarchicalPart = hierarchicalPart(anAuthority(), anAbsolutePath());
        Query baseQuery = aQuery();
        Path relativeReferencePath = aPath();
        Fragment relativeReferenceFragment = aFragment();
        assertThat(
                relativeReference(relativeReferencePath, relativeReferenceFragment).resolve(baseScheme, baseHierarchicalPart, baseQuery),
                equalTo(urin(baseScheme, baseHierarchicalPart.resolve(relativeReferencePath), relativeReferenceFragment)));
    }

    @Test
    public void aRelativeReferenceWithPathAndFragmentResolvesSchemeAndAuthorityAndQueryAndFragmentToTheBase() throws Exception {
        Scheme baseScheme = aScheme();
        HierarchicalPart baseHierarchicalPart = hierarchicalPart(anAuthority(), anAbsolutePath());
        Query baseQuery = aQuery();
        Fragment baseFragment = aFragment();
        Path relativeReferencePath = aPath();
        Fragment relativeReferenceFragment = aFragment();
        assertThat(
                relativeReference(relativeReferencePath, relativeReferenceFragment).resolve(baseScheme, baseHierarchicalPart, baseQuery, baseFragment),
                equalTo(urin(baseScheme, baseHierarchicalPart.resolve(relativeReferencePath), relativeReferenceFragment)));
    }

    @Test
    public void aRelativeReferenceWithPathAndQueryAndFragmentAsStringIsCorrect() throws Exception {
        Path path = aPath();
        Query query = aQuery();
        Fragment fragment = aFragment();
        assertThat(relativeReference(path, query, fragment).asString(), equalTo(path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON) + "?" + query.asString() + "#" + fragment.asString()));
        assertThat(relativeReference(path, query, fragment).asUri(), equalTo(URI.create(path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON) + "?" + query.asString() + "#" + fragment.asString())));
    }

    @Test
    public void aRelativeReferenceWithPathAndQueryAndFragmentIsEqualToAnotherRelativeReferenceWithPathAndFragment() throws Exception {
        Path path = aPath();
        Query query = aQuery();
        Fragment fragment = aFragment();
        assertThat(relativeReference(path, query, fragment), equalTo(relativeReference(path, query, fragment)));
        assertThat(relativeReference(path, query, fragment).hashCode(), equalTo(relativeReference(path, query, fragment).hashCode()));
    }

    @Test
    public void aRelativeReferenceWithPathAndQueryAndFragmentIsNotEqualToAnotherWithTheADifferentPath() throws Exception {
        Query query = aQuery();
        Fragment fragment = aFragment();
        assertThat(relativeReference(aPath(), query, fragment), not(equalTo(relativeReference(aPath(), query, fragment))));
    }

    @Test
    public void aRelativeReferenceWithPathAndQueryAndFragmentIsNotEqualToAnotherWithTheADifferentQuery() throws Exception {
        Path path = aPath();
        Fragment fragment = aFragment();
        assertThat(relativeReference(path, aQuery(), fragment), not(equalTo(relativeReference(path, aQuery(), fragment))));
    }

    @Test
    public void aRelativeReferenceWithPathAndQueryAndFragmentIsNotEqualToAnotherWithTheADifferentFragment() throws Exception {
        Path path = aPath();
        Query query = aQuery();
        assertThat(relativeReference(path, query, aFragment()), not(equalTo(relativeReference(path, query, aFragment()))));
    }

    @Test
    public void aRelativeReferenceWithPathAndQueryAndFragmentToStringIsCorrect() throws Exception {
        Path path = aPath();
        Query query = aQuery();
        Fragment fragment = aFragment();
        assertThat(relativeReference(path, query, fragment).toString(), equalTo("RelativeReference{path=" + path.toString() + ", query=" + query.toString() + ", fragment=" + fragment.toString() + "}"));
    }

    @Test
    public void rejectsNullInFactoryForARelativeReferenceWithPathAndQueryAndFragment() throws Exception {
        assertThrowsNullPointerException("Null path should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                relativeReference((Path) null, aQuery(), aFragment());
            }
        });
        assertThrowsNullPointerException("Null query should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                relativeReference(aPath(), null, aFragment());
            }
        });
        assertThrowsNullPointerException("Null fragment should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                relativeReference(aPath(), aQuery(), null);
            }
        });
    }

    @Test
    public void aRelativeReferenceWithPathAndQueryAndFragmentResolvesSchemeToTheBase() throws Exception {
        Scheme baseScheme = aScheme();
        HierarchicalPart baseHierarchicalPart = hierarchicalPart(anAuthority(), anAbsolutePath());
        Path relativeReferencePath = aPath();
        Query relativeReferenceQuery = aQuery();
        Fragment relativeReferenceFragment = aFragment();
        assertThat(
                relativeReference(relativeReferencePath, relativeReferenceQuery, relativeReferenceFragment).resolve(baseScheme, baseHierarchicalPart),
                equalTo(urin(baseScheme, baseHierarchicalPart.resolve(relativeReferencePath), relativeReferenceQuery, relativeReferenceFragment)));
    }

    @Test
    public void aRelativeReferenceWithPathAndQueryAndFragmentResolvesSchemeAndAuthorityAndQueryToTheBase() throws Exception {
        Scheme baseScheme = aScheme();
        HierarchicalPart baseHierarchicalPart = hierarchicalPart(anAuthority(), anAbsolutePath());
        Query baseQuery = aQuery();
        Path relativeReferencePath = aPath();
        Query relativeReferenceQuery = aQuery();
        Fragment relativeReferenceFragment = aFragment();
        assertThat(
                relativeReference(relativeReferencePath, relativeReferenceQuery, relativeReferenceFragment).resolve(baseScheme, baseHierarchicalPart, baseQuery),
                equalTo(urin(baseScheme, baseHierarchicalPart.resolve(relativeReferencePath), relativeReferenceQuery, relativeReferenceFragment)));
    }

    @Test
    public void aRelativeReferenceWithPathAndQueryAndFragmentResolvesSchemeAndAuthorityAndQueryAndFragmentToTheBase() throws Exception {
        Scheme baseScheme = aScheme();
        HierarchicalPart baseHierarchicalPart = hierarchicalPart(anAuthority(), anAbsolutePath());
        Query baseQuery = aQuery();
        Fragment baseFragment = aFragment();
        Path relativeReferencePath = aPath();
        Query relativeReferenceQuery = aQuery();
        Fragment relativeReferenceFragment = aFragment();
        assertThat(
                relativeReference(relativeReferencePath, relativeReferenceQuery, relativeReferenceFragment).resolve(baseScheme, baseHierarchicalPart, baseQuery, baseFragment),
                equalTo(urin(baseScheme, baseHierarchicalPart.resolve(relativeReferencePath), relativeReferenceQuery, relativeReferenceFragment)));
    }

    @Test
    public void aSimpleAbsolutePathAsStringReturnsThePath() throws Exception {
        Segment firstSegment = aNonDotSegment();
        Segment secondSegment = aNonDotSegment();
        assertThat(relativeReference(Path.path(firstSegment, secondSegment)).asString(), equalTo("/" + firstSegment.asString() + "/" + secondSegment.asString()));
        assertThat(relativeReference(Path.path(firstSegment, secondSegment)).asUri(), equalTo(URI.create("/" + firstSegment.asString() + "/" + secondSegment.asString())));
    }

    @Test
    public void aSimpleAbsolutePathPrefixesAnEmptyFirstSegmentWithADotSegment() throws Exception {
        Segment firstSegment = segment("");
        Segment secondSegment = aNonDotSegment();
        assertThat(relativeReference(Path.path(firstSegment, secondSegment)).asString(), equalTo("/./" + firstSegment.asString() + "/" + secondSegment.asString()));
    }

    @Test
    public void rejectsNullInFactoryForASimplePath() throws Exception {
        assertThrowsNullPointerException("Null first segment should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Path path = null;
                relativeReference(path);
            }
        });
    }

    @Test
    public void aSimpleAbsolutePathIsEqualToAnotherWithTheSamePath() throws Exception {
        Path path = aPath();
        assertThat(relativeReference(path), equalTo(relativeReference(path)));
        assertThat(relativeReference(path).hashCode(), equalTo(relativeReference(path).hashCode()));
    }

    @Test
    public void aSimpleAbsolutePathIsNotEqualToAnotherWithTheADifferentPath() throws Exception {
        assertThat(relativeReference(aPath()), not(equalTo(relativeReference(aPath()))));
    }

    @Test
    public void aSimpleAbsolutePathToStringIsCorrect() throws Exception {
        Path path = aPath();
        assertThat(relativeReference(path).toString(), equalTo("RelativeReference{path=" + path + "}"));
    }

    @Test
    public void aSimpleRootlessPathAsStringReturnsThePath() throws Exception {
        Segment firstSegment = aNonDotSegment();
        Segment secondSegment = aNonDotSegment();
        assertThat(relativeReference(rootlessPath(firstSegment, secondSegment)).asString(), equalTo(firstSegment.asString() + "/" + secondSegment.asString()));
        assertThat(relativeReference(rootlessPath(firstSegment, secondSegment)).asUri(), equalTo(URI.create(firstSegment.asString() + "/" + secondSegment.asString())));
    }

    @Test
    public void aSimpleRootlessPathRejectsAnEmptyFirstSegment() throws Exception {
        Segment firstSegment = segment("");
        Segment secondSegment = aNonDotSegment();
        assertThat(relativeReference(rootlessPath(firstSegment, secondSegment)).asString(), equalTo("./" + firstSegment.asString() + "/" + secondSegment.asString()));
    }

    @Test
    public void aSimpleRootlessPathPrependsAColonInFirstSegmentWithDotSlash() throws Exception {
        Segment firstSegment = segment(aStringIncluding(':'));
        Segment secondSegment = aSegment();
        assertThat(relativeReference(rootlessPath(firstSegment, secondSegment)), equalTo(relativeReference(rootlessPath(DOT, firstSegment, secondSegment))));
    }

    @Test
    public void aSimpleRootlessPathPermitsAColonInTrailingSegments() throws Exception {
        relativeReference(rootlessPath(aSegment(), segment(aStringIncluding(':')))).asString();
    }

    @Test
    public void aSimpleRootlessPathIsEqualToAnotherWithTheSamePath() throws Exception {
        Path path = aPath();
        assertThat(relativeReference(path), equalTo(relativeReference(path)));
        assertThat(relativeReference(path).hashCode(), equalTo(relativeReference(path).hashCode()));
    }

    @Test
    public void aSimpleRootlessPathIsNotEqualToAnotherWithTheADifferentPath() throws Exception {
        assertThat(relativeReference(aPath()), not(equalTo(relativeReference(aPath()))));
    }

    @Test
    public void aSimpleRootlessPathToStringIsCorrect() throws Exception {
        Path path = aPath();
        assertThat(relativeReference(path).toString(), equalTo("RelativeReference{path=" + path + "}"));
    }

    @Test
    public void aRelativeReferenceWithOnlyPathResolvesSchemeAndAuthorityToTheBase() throws Exception {
        Scheme baseScheme = aScheme();
        HierarchicalPart baseHierarchicalPart = hierarchicalPart(anAuthority(), anAbsolutePath());
        Path relativeReferencePath = aPath();
        assertThat(
                relativeReference(relativeReferencePath).resolve(baseScheme, baseHierarchicalPart),
                equalTo(urin(baseScheme, baseHierarchicalPart.resolve(relativeReferencePath))));
    }

    @Test
    public void aRelativeReferenceWithOnlyPathResolvesSchemeAndAuthorityAndQueryToTheBase() throws Exception {
        Scheme baseScheme = aScheme();
        HierarchicalPart baseHierarchicalPart = hierarchicalPart(anAuthority(), anAbsolutePath());
        Path relativeReferencePath = aPath();
        Query baseQuery = aQuery();
        assertThat(
                relativeReference(relativeReferencePath).resolve(baseScheme, baseHierarchicalPart, baseQuery),
                equalTo(urin(baseScheme, baseHierarchicalPart.resolve(relativeReferencePath))));
    }

    @Test
    public void aRelativeReferenceWithOnlyPathResolvesSchemeAndAuthorityAndQueryAndFragmentToTheBase() throws Exception {
        Scheme baseScheme = aScheme();
        HierarchicalPart baseHierarchicalPart = hierarchicalPart(anAuthority(), anAbsolutePath());
        Path relativeReferencePath = aPath();
        Query baseQuery = aQuery();
        Fragment baseFragment = aFragment();
        assertThat(
                relativeReference(relativeReferencePath).resolve(baseScheme, baseHierarchicalPart, baseQuery, baseFragment),
                equalTo(urin(baseScheme, baseHierarchicalPart.resolve(relativeReferencePath))));
    }

    @Test
    public void makesRelativeReferenceWithAuthorityAndEmptyPath() throws Exception {
        Authority authority = anAuthority();
        assertThat(relativeReference(authority).asString(), equalTo("//" + authority.asString()));
    }

    @Test
    public void rejectsNullInFactoryForRelativeReferenceWithAuthorityAndEmptyPath() throws Exception {
        assertThrowsNullPointerException("Null authority should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                relativeReference((Authority) null);
            }
        });
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndEmptyPathIsEqualToAnotherWithTheSameAuthority() throws Exception {
        Authority authority = anAuthority();
        assertThat(relativeReference(authority), equalTo(relativeReference(authority)));
        assertThat(relativeReference(authority).hashCode(), equalTo(relativeReference(authority).hashCode()));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndEmptyPathIsNotEqualToAnotherWithTheADifferentAuthority() throws Exception {
        assertThat(relativeReference(anAuthority()), not(equalTo(relativeReference(anAuthority()))));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndEmptyPathToStringIsCorrect() throws Exception {
        Authority authority = anAuthority();
        assertThat(relativeReference(authority).toString(), equalTo("RelativeReference{authority=" + authority + ", path=EmptyPath}"));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndEmptyPathResolvesSchemeToTheBase() throws Exception {
        Scheme baseScheme = aScheme();
        HierarchicalPart baseHierarchicalPart = hierarchicalPart(anAuthority(), anAbsolutePath());
        Authority relativeReferenceAuthority = anAuthority();
        assertThat(
                relativeReference(relativeReferenceAuthority).resolve(baseScheme, baseHierarchicalPart),
                equalTo(urin(baseScheme, baseHierarchicalPart.resolve(relativeReferenceAuthority, new EmptyPath()))));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndEmptyPathResolvesSchemeAndAuthorityAndQueryToTheBase() throws Exception {
        Scheme baseScheme = aScheme();
        HierarchicalPart baseHierarchicalPart = hierarchicalPart(anAuthority(), anAbsolutePath());
        Query baseQuery = aQuery();
        Authority relativeReferenceAuthority = anAuthority();
        assertThat(
                relativeReference(relativeReferenceAuthority).resolve(baseScheme, baseHierarchicalPart, baseQuery),
                equalTo(urin(baseScheme, baseHierarchicalPart.resolve(relativeReferenceAuthority, new EmptyPath()))));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndEmptyPathResolvesSchemeAndAuthorityAndQueryAndFragmentToTheBase() throws Exception {
        Scheme baseScheme = aScheme();
        HierarchicalPart baseHierarchicalPart = hierarchicalPart(anAuthority(), anAbsolutePath());
        Query baseQuery = aQuery();
        Fragment baseFragment = aFragment();
        Authority relativeReferenceAuthority = anAuthority();
        assertThat(
                relativeReference(relativeReferenceAuthority).resolve(baseScheme, baseHierarchicalPart, baseQuery, baseFragment),
                equalTo(urin(baseScheme, baseHierarchicalPart.resolve(relativeReferenceAuthority, new EmptyPath()))));
    }

    @Test
    public void makesRelativeReferenceWithAuthorityAndQuery() throws Exception {
        Authority authority = anAuthority();
        Query query = aQuery();
        assertThat(relativeReference(authority, query).asString(), equalTo("//" + authority.asString() + "?" + query.asString()));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndQueryIsEqualToAnotherWithTheSameAuthorityAndQuery() throws Exception {
        Authority authority = anAuthority();
        Query query = aQuery();
        assertThat(relativeReference(authority, query), equalTo(relativeReference(authority, query)));
        assertThat(relativeReference(authority, query).hashCode(), equalTo(relativeReference(authority, query).hashCode()));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndQueryIsNotEqualToAnotherWithTheADifferentAuthority() throws Exception {
        Query query = aQuery();
        assertThat(relativeReference(anAuthority(), query), not(equalTo(relativeReference(anAuthority(), query))));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndQueryIsNotEqualToAnotherWithTheADifferentQuery() throws Exception {
        Authority authority = anAuthority();
        assertThat(relativeReference(authority, aQuery()), not(equalTo(relativeReference(authority, aQuery()))));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndQueryToStringIsCorrect() throws Exception {
        Authority authority = anAuthority();
        Query query = aQuery();
        assertThat(relativeReference(authority, query).toString(), equalTo("RelativeReference{authority=" + authority + ", path=EmptyPath, query=" + query + "}"));
    }

    @Test
    public void rejectsNullInFactoryForRelativeReferenceWithAuthorityAndQuery() throws Exception {
        assertThrowsNullPointerException("Null authority should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                relativeReference((Authority) null, aQuery());
            }
        });
        assertThrowsNullPointerException("Null query should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                relativeReference(anAuthority(), (Query) null);
            }
        });
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndQueryResolvesSchemeToTheBase() throws Exception {
        Scheme baseScheme = aScheme();
        HierarchicalPart baseHierarchicalPart = hierarchicalPart(anAuthority(), anAbsolutePath());
        Authority relativeReferenceAuthority = anAuthority();
        Query relativeReferenceQuery = aQuery();
        assertThat(
                relativeReference(relativeReferenceAuthority, relativeReferenceQuery).resolve(baseScheme, baseHierarchicalPart),
                equalTo(urin(baseScheme, baseHierarchicalPart.resolve(relativeReferenceAuthority, new EmptyPath()), relativeReferenceQuery)));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndQueryResolvesSchemeAndAuthorityToTheBase() throws Exception {
        Scheme baseScheme = aScheme();
        HierarchicalPart baseHierarchicalPart = hierarchicalPart(anAuthority(), anAbsolutePath());
        Query baseQuery = aQuery();
        Authority relativeReferenceAuthority = anAuthority();
        Query relativeReferenceQuery = aQuery();
        assertThat(
                relativeReference(relativeReferenceAuthority, relativeReferenceQuery).resolve(baseScheme, baseHierarchicalPart, baseQuery),
                equalTo(urin(baseScheme, baseHierarchicalPart.resolve(relativeReferenceAuthority, new EmptyPath()), relativeReferenceQuery)));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndQueryResolvesSchemeAndAuthorityAndQueryAndFragmentToTheBase() throws Exception {
        Scheme baseScheme = aScheme();
        HierarchicalPart baseHierarchicalPart = hierarchicalPart(anAuthority(), anAbsolutePath());
        Query baseQuery = aQuery();
        Fragment baseFragment = aFragment();
        Authority relativeReferenceAuthority = anAuthority();
        Query relativeReferenceQuery = aQuery();
        assertThat(
                relativeReference(relativeReferenceAuthority, relativeReferenceQuery).resolve(baseScheme, baseHierarchicalPart, baseQuery, baseFragment),
                equalTo(urin(baseScheme, baseHierarchicalPart.resolve(relativeReferenceAuthority, new EmptyPath()), relativeReferenceQuery)));
    }

    @Test
    public void makesRelativeReferenceWithAuthorityAndFragment() throws Exception {
        Authority authority = anAuthority();
        Fragment fragment = aFragment();
        assertThat(relativeReference(authority, fragment).asString(), equalTo("//" + authority.asString() + "#" + fragment.asString()));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndFragmentIsEqualToAnotherWithTheSameAuthorityAndFragment() throws Exception {
        Authority authority = anAuthority();
        Fragment fragment = aFragment();
        assertThat(relativeReference(authority, fragment), equalTo(relativeReference(authority, fragment)));
        assertThat(relativeReference(authority, fragment).hashCode(), equalTo(relativeReference(authority, fragment).hashCode()));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndFragmentIsNotEqualToAnotherWithTheADifferentAuthority() throws Exception {
        Fragment fragment = aFragment();
        assertThat(relativeReference(anAuthority(), fragment), not(equalTo(relativeReference(anAuthority(), fragment))));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndFragmentIsNotEqualToAnotherWithTheADifferentFragment() throws Exception {
        Authority authority = anAuthority();
        assertThat(relativeReference(authority, aFragment()), not(equalTo(relativeReference(authority, aFragment()))));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndFragmentToStringIsCorrect() throws Exception {
        Authority authority = anAuthority();
        Fragment fragment = aFragment();
        assertThat(relativeReference(authority, fragment).toString(), equalTo("RelativeReference{authority=" + authority + ", path=EmptyPath, fragment=" + fragment + "}"));
    }

    @Test
    public void rejectsNullInFactoryForRelativeReferenceWithAuthorityAndFragment() throws Exception {
        assertThrowsNullPointerException("Null authority should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                relativeReference((Authority) null, aFragment());
            }
        });
        assertThrowsNullPointerException("Null fragment should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                relativeReference(anAuthority(), (Fragment) null);
            }
        });
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndFragmentResolvesSchemeToTheBase() throws Exception {
        Scheme baseScheme = aScheme();
        HierarchicalPart baseHierarchicalPart = hierarchicalPart(anAuthority(), anAbsolutePath());
        Authority relativeReferenceAuthority = anAuthority();
        Fragment relativeReferenceFragment = aFragment();
        assertThat(
                relativeReference(relativeReferenceAuthority, relativeReferenceFragment).resolve(baseScheme, baseHierarchicalPart),
                equalTo(urin(baseScheme, baseHierarchicalPart.resolve(relativeReferenceAuthority, new EmptyPath()), relativeReferenceFragment)));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndFragmentResolvesSchemeAndAuthorityAndQueryToTheBase() throws Exception {
        Scheme baseScheme = aScheme();
        HierarchicalPart baseHierarchicalPart = hierarchicalPart(anAuthority(), anAbsolutePath());
        Query baseQuery = aQuery();
        Authority relativeReferenceAuthority = anAuthority();
        Fragment relativeReferenceFragment = aFragment();
        assertThat(
                relativeReference(relativeReferenceAuthority, relativeReferenceFragment).resolve(baseScheme, baseHierarchicalPart, baseQuery),
                equalTo(urin(baseScheme, baseHierarchicalPart.resolve(relativeReferenceAuthority, new EmptyPath()), relativeReferenceFragment)));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndFragmentResolvesSchemeAndAuthorityAndQueryAndFragmentToTheBase() throws Exception {
        Scheme baseScheme = aScheme();
        HierarchicalPart baseHierarchicalPart = hierarchicalPart(anAuthority(), anAbsolutePath());
        Query baseQuery = aQuery();
        Fragment baseFragment = aFragment();
        Authority relativeReferenceAuthority = anAuthority();
        Fragment relativeReferenceFragment = aFragment();
        assertThat(
                relativeReference(relativeReferenceAuthority, relativeReferenceFragment).resolve(baseScheme, baseHierarchicalPart, baseQuery, baseFragment),
                equalTo(urin(baseScheme, baseHierarchicalPart.resolve(relativeReferenceAuthority, new EmptyPath()), relativeReferenceFragment)));
    }

    @Test
    public void makesRelativeReferenceWithAuthorityAndQueryAndFragment() throws Exception {
        Authority authority = anAuthority();
        Query query = aQuery();
        Fragment fragment = aFragment();
        assertThat(relativeReference(authority, query, fragment).asString(), equalTo("//" + authority.asString() + "?" + query.asString() + "#" + fragment.asString()));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndQueryAndFragmentIsEqualToAnotherWithTheSameAuthorityAndQueryAndFragment() throws Exception {
        Authority authority = anAuthority();
        Query query = aQuery();
        Fragment fragment = aFragment();
        assertThat(relativeReference(authority, query, fragment), equalTo(relativeReference(authority, query, fragment)));
        assertThat(relativeReference(authority, query, fragment).hashCode(), equalTo(relativeReference(authority, query, fragment).hashCode()));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndQueryAndFragmentIsNotEqualToAnotherWithTheADifferentAuthority() throws Exception {
        Query query = aQuery();
        Fragment fragment = aFragment();
        assertThat(relativeReference(anAuthority(), query, fragment), not(equalTo(relativeReference(anAuthority(), query, fragment))));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndQueryAndFragmentIsNotEqualToAnotherWithTheADifferentQuery() throws Exception {
        Authority authority = anAuthority();
        Fragment fragment = aFragment();
        assertThat(relativeReference(authority, aQuery(), fragment), not(equalTo(relativeReference(authority, aQuery(), fragment))));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndQueryAndFragmentIsNotEqualToAnotherWithTheADifferentFragment() throws Exception {
        Authority authority = anAuthority();
        Query query = aQuery();
        assertThat(relativeReference(authority, query, aFragment()), not(equalTo(relativeReference(authority, query, aFragment()))));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndQueryAndFragmentToStringIsCorrect() throws Exception {
        Authority authority = anAuthority();
        Query query = aQuery();
        Fragment fragment = aFragment();
        assertThat(relativeReference(authority, query, fragment).toString(), equalTo("RelativeReference{authority=" + authority + ", path=EmptyPath, query=" + query + ", fragment=" + fragment + "}"));
    }

    @Test
    public void rejectsNullInFactoryForRelativeReferenceWithAuthorityAndQueryAndFragment() throws Exception {
        assertThrowsNullPointerException("Null authority should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                relativeReference((Authority) null, aQuery(), aFragment());
            }
        });
        assertThrowsNullPointerException("Null query should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                Query query = null;
                relativeReference(anAuthority(), query, aFragment());
            }
        });
        assertThrowsNullPointerException("Null fragment should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                relativeReference(anAuthority(), aQuery(), null);
            }
        });
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndQueryAndFragmentResolvesSchemeToTheBase() throws Exception {
        Scheme baseScheme = aScheme();
        HierarchicalPart baseHierarchicalPart = hierarchicalPart(anAuthority(), anAbsolutePath());
        Authority relativeReferenceAuthority = anAuthority();
        Query relativeReferenceQuery = aQuery();
        Fragment relativeReferenceFragment = aFragment();
        assertThat(
                relativeReference(relativeReferenceAuthority, relativeReferenceQuery, relativeReferenceFragment).resolve(baseScheme, baseHierarchicalPart),
                equalTo(urin(baseScheme, baseHierarchicalPart.resolve(relativeReferenceAuthority, new EmptyPath()), relativeReferenceQuery, relativeReferenceFragment)));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndQueryAndFragmentResolvesSchemeAndAuthorityAndQueryToTheBase() throws Exception {
        Scheme baseScheme = aScheme();
        HierarchicalPart baseHierarchicalPart = hierarchicalPart(anAuthority(), anAbsolutePath());
        Query baseQuery = aQuery();
        Authority relativeReferenceAuthority = anAuthority();
        Query relativeReferenceQuery = aQuery();
        Fragment relativeReferenceFragment = aFragment();
        assertThat(
                relativeReference(relativeReferenceAuthority, relativeReferenceQuery, relativeReferenceFragment).resolve(baseScheme, baseHierarchicalPart, baseQuery),
                equalTo(urin(baseScheme, baseHierarchicalPart.resolve(relativeReferenceAuthority, new EmptyPath()), relativeReferenceQuery, relativeReferenceFragment)));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndQueryAndFragmentResolvesSchemeAndAuthorityAndQueryAndFragmentToTheBase() throws Exception {
        Scheme baseScheme = aScheme();
        HierarchicalPart baseHierarchicalPart = hierarchicalPart(anAuthority(), anAbsolutePath());
        Query baseQuery = aQuery();
        Fragment baseFragment = aFragment();
        Authority relativeReferenceAuthority = anAuthority();
        Query relativeReferenceQuery = aQuery();
        Fragment relativeReferenceFragment = aFragment();
        assertThat(
                relativeReference(relativeReferenceAuthority, relativeReferenceQuery, relativeReferenceFragment).resolve(baseScheme, baseHierarchicalPart, baseQuery, baseFragment),
                equalTo(urin(baseScheme, baseHierarchicalPart.resolve(relativeReferenceAuthority, new EmptyPath()), relativeReferenceQuery, relativeReferenceFragment)));
    }

    @Test
    public void makesRelativeReferenceWithAuthorityAndNonEmptyPath() throws Exception {
        Authority authority = anAuthority();
        Segment firstSegment = aNonDotSegment();
        Segment secondSegment = aNonDotSegment();
        assertThat(relativeReference(authority, Path.path(firstSegment, secondSegment)).asString(), equalTo("//" + authority.asString() + "/" + firstSegment.asString() + "/" + secondSegment.asString()));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndPathIsEqualToAnotherWithTheSameAuthorityAndPath() throws Exception {
        Authority authority = anAuthority();
        AbsolutePath absolutePath = anAbsolutePath();
        assertThat(relativeReference(authority, absolutePath), equalTo(relativeReference(authority, absolutePath)));
        assertThat(relativeReference(authority, absolutePath).hashCode(), equalTo(relativeReference(authority, absolutePath).hashCode()));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndPathIsNotEqualToAnotherWithTheADifferentAuthorityAndPath() throws Exception {
        assertThat(relativeReference(anAuthority(), anAbsolutePath()), not(equalTo(relativeReference(anAuthority(), anAbsolutePath()))));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndPathToStringIsCorrect() throws Exception {
        Authority authority = anAuthority();
        AbsolutePath absolutePath = anAbsolutePath();
        assertThat(relativeReference(authority, absolutePath).toString(), equalTo("RelativeReference{authority=" + authority + ", path=" + absolutePath + "}"));
    }

    @Test
    public void rejectsNullInFactoryForRelativeReferenceWithAuthorityAndPath() throws Exception {
        assertThrowsNullPointerException("Null authority should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Authority authority = null;
                relativeReference(authority, anAbsolutePath());
            }
        });
        assertThrowsNullPointerException("Null path should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                AbsolutePath absolutePath = null;
                relativeReference(anAuthority(), absolutePath);
            }
        });
    }

    @Test
    public void makesRelativeReferenceWithAuthorityAndPathAndQuery() throws Exception {
        Authority authority = anAuthority();
        AbsolutePath absolutePath = anAbsolutePath();
        Query query = aQuery();
        assertThat(relativeReference(authority, absolutePath, query).asString(), equalTo("//" + authority.asString() + absolutePath.asString(NEVER_PREFIX_WITH_DOT_SEGMENT) + "?" + query.asString()));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndPathAndQueryIsEqualToAnotherWithTheSameAuthorityAndPathAndQuery() throws Exception {
        Authority authority = anAuthority();
        AbsolutePath absolutePath = anAbsolutePath();
        Query query = aQuery();
        assertThat(relativeReference(authority, absolutePath, query), equalTo(relativeReference(authority, absolutePath, query)));
        assertThat(relativeReference(authority, absolutePath, query).hashCode(), equalTo(relativeReference(authority, absolutePath, query).hashCode()));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndPathIsNotEqualToAnotherWithTheADifferentAuthorityAndPathAndQuery() throws Exception {
        assertThat(relativeReference(anAuthority(), anAbsolutePath(), aQuery()), not(equalTo(relativeReference(anAuthority(), anAbsolutePath(), aQuery()))));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndPathAndQueryToStringIsCorrect() throws Exception {
        Authority authority = anAuthority();
        AbsolutePath absolutePath = anAbsolutePath();
        Query query = aQuery();
        assertThat(relativeReference(authority, absolutePath, query).toString(), equalTo("RelativeReference{authority=" + authority + ", path=" + absolutePath + ", query=" + query + "}"));
    }

    @Test
    public void rejectsNullInFactoryForRelativeReferenceWithAuthorityAndPathAndQuery() throws Exception {
        assertThrowsNullPointerException("Null authority should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                relativeReference(null, anAbsolutePath(), aQuery());
            }
        });
        assertThrowsNullPointerException("Null path should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                relativeReference(anAuthority(), null, aQuery());
            }
        });
        assertThrowsNullPointerException("Null query should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                relativeReference(anAuthority(), anAbsolutePath(), (Query) null);
            }
        });
    }

    @Test
    public void makesRelativeReferenceWithAuthorityAndPathAndFragment() throws Exception {
        Authority authority = anAuthority();
        AbsolutePath absolutePath = anAbsolutePath();
        Fragment fragment = aFragment();
        assertThat(relativeReference(authority, absolutePath, fragment).asString(), equalTo("//" + authority.asString() + absolutePath.asString(NEVER_PREFIX_WITH_DOT_SEGMENT) + "#" + fragment.asString()));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndPathAndFragmentIsEqualToAnotherWithTheSameAuthorityAndPathAndFragment() throws Exception {
        Authority authority = anAuthority();
        AbsolutePath absolutePath = anAbsolutePath();
        Fragment fragment = aFragment();
        assertThat(relativeReference(authority, absolutePath, fragment), equalTo(relativeReference(authority, absolutePath, fragment)));
        assertThat(relativeReference(authority, absolutePath, fragment).hashCode(), equalTo(relativeReference(authority, absolutePath, fragment).hashCode()));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndPathIsNotEqualToAnotherWithTheADifferentAuthorityAndPathAndFragment() throws Exception {
        assertThat(relativeReference(anAuthority(), anAbsolutePath(), aFragment()), not(equalTo(relativeReference(anAuthority(), anAbsolutePath(), aFragment()))));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndPathAndFragmentToStringIsCorrect() throws Exception {
        Authority authority = anAuthority();
        AbsolutePath absolutePath = anAbsolutePath();
        Fragment fragment = aFragment();
        assertThat(relativeReference(authority, absolutePath, fragment).toString(), equalTo("RelativeReference{authority=" + authority + ", path=" + absolutePath + ", fragment=" + fragment + "}"));
    }

    @Test
    public void rejectsNullInFactoryForRelativeReferenceWithAuthorityAndPathAndFragment() throws Exception {
        assertThrowsNullPointerException("Null authority should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                relativeReference(null, anAbsolutePath(), aFragment());
            }
        });
        assertThrowsNullPointerException("Null path should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                relativeReference(anAuthority(), (AbsolutePath) null, aFragment());
            }
        });
        assertThrowsNullPointerException("Null fragment should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                relativeReference(anAuthority(), anAbsolutePath(), (Fragment) null);
            }
        });
    }

    @Test
    public void makesRelativeReferenceWithAuthorityAndPathAndQueryAndFragment() throws Exception {
        Authority authority = anAuthority();
        AbsolutePath absolutePath = anAbsolutePath();
        Query query = aQuery();
        Fragment fragment = aFragment();
        assertThat(relativeReference(authority, absolutePath, query, fragment).asString(), equalTo("//" + authority.asString() + absolutePath.asString(NEVER_PREFIX_WITH_DOT_SEGMENT) + "?" + query.asString() + "#" + fragment.asString()));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndPathAndQueryAndFragmentIsEqualToAnotherWithTheSameAuthorityAndPathAndQueryAndFragment() throws Exception {
        Authority authority = anAuthority();
        AbsolutePath absolutePath = anAbsolutePath();
        Query query = aQuery();
        Fragment fragment = aFragment();
        assertThat(relativeReference(authority, absolutePath, query, fragment), equalTo(relativeReference(authority, absolutePath, query, fragment)));
        assertThat(relativeReference(authority, absolutePath, query, fragment).hashCode(), equalTo(relativeReference(authority, absolutePath, query, fragment).hashCode()));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndPathIsNotEqualToAnotherWithTheADifferentAuthorityAndPathAndQueryAndFragment() throws Exception {
        assertThat(relativeReference(anAuthority(), anAbsolutePath(), aQuery(), aFragment()), not(equalTo(relativeReference(anAuthority(), anAbsolutePath(), aQuery(), aFragment()))));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndPathAndQueryAndFragmentToStringIsCorrect() throws Exception {
        Authority authority = anAuthority();
        AbsolutePath absolutePath = anAbsolutePath();
        Query query = aQuery();
        Fragment fragment = aFragment();
        assertThat(relativeReference(authority, absolutePath, query, fragment).toString(), equalTo("RelativeReference{authority=" + authority + ", path=" + absolutePath + ", query=" + query + ", fragment=" + fragment + "}"));
    }

    @Test
    public void rejectsNullInFactoryForRelativeReferenceWithAuthorityAndPathAndQueryAndFragment() throws Exception {
        assertThrowsNullPointerException("Null authority should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                relativeReference(null, anAbsolutePath(), aQuery(), aFragment());
            }
        });
        assertThrowsNullPointerException("Null path should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                relativeReference(anAuthority(), null, aQuery(), aFragment());
            }
        });
        assertThrowsNullPointerException("Null query should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                relativeReference(anAuthority(), anAbsolutePath(), null, aFragment());
            }
        });
        assertThrowsNullPointerException("Null fragment should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                relativeReference(anAuthority(), anAbsolutePath(), aQuery(), null);
            }
        });
    }

    @Test
    public void parsesARelativeReferenceWithEmptyPath() throws Exception {
        assertThat(parse(""), equalTo(relativeReference()));
    }

    @Test
    public void parsesARelativeReferenceWithEmptyPathWithQuery() throws Exception {
        Query query = aQuery();
        assertThat(parse("?" + query.asString()), equalTo(relativeReference(query)));
    }

    @Test
    public void parsesARelativeReferenceWithEmptyPathWithFragment() throws Exception {
        Fragment fragment = aFragment();
        assertThat(parse("#" + fragment.asString()), equalTo(relativeReference(fragment)));
    }

    @Test
    public void parsesARelativeReferenceWithEmptyPathWithQueryAndFragment() throws Exception {
        Query query = aQuery();
        Fragment fragment = aFragment();
        assertThat(parse("?" + query.asString() + "#" + fragment.asString()), equalTo(relativeReference(query, fragment)));
    }

    @Test
    public void parsesARelativeReferenceWithPathAndQuery() throws Exception {
        Path path = aPath();
        Query query = aQuery();
        assertThat(parse(path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON) + "?" + query.asString()), equalTo(relativeReference(path, query)));
    }

    @Test
    public void parsesARelativeReferenceWithPathAndFragment() throws Exception {
        Path path = aPath();
        Fragment fragment = aFragment();
        assertThat(parse(path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON) + "#" + fragment.asString()), equalTo(relativeReference(path, fragment)));
    }

    @Test
    public void parsesARelativeReferenceWithPathAndQueryAndFragment() throws Exception {
        Path path = aPath();
        Query query = aQuery();
        Fragment fragment = aFragment();
        assertThat(parse(path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON) + "?" + query.asString() + "#" + fragment.asString()), equalTo(relativeReference(path, query, fragment)));
    }

    @Test
    public void parsesASimpleAbsolutePath() throws Exception {
        Segment firstSegment = aSegment();
        Segment secondSegment = aSegment();
        assertThat(parse("/" + firstSegment.asString() + "/" + secondSegment.asString()), equalTo(relativeReference(Path.path(firstSegment, secondSegment))));
    }

    @Test
    public void parsesASimpleAbsolutePathPrefixedWithADotSegment() throws Exception {
        Segment firstSegment = segment("");
        Segment secondSegment = aSegment();
        assertThat(parse("/./" + firstSegment.asString() + "/" + secondSegment.asString()), equalTo(relativeReference(Path.path(firstSegment, secondSegment))));
    }

    @Test
    public void parsesASimpleRootlessPath() throws Exception {
        Segment firstSegment = aSegment();
        Segment secondSegment = aSegment();
        assertThat(parse(firstSegment.asString() + "/" + secondSegment.asString()), equalTo(relativeReference(rootlessPath(firstSegment, secondSegment))));
    }

    @Test
    public void parsesARelativeReferenceWithAuthorityAndEmptyPath() throws Exception {
        Authority authority = anAuthority();
        assertThat(parse("//" + authority.asString()), equalTo(relativeReference(authority)));
    }

    @Test
    public void parsesARelativeReferenceWithAuthorityAndQuery() throws Exception {
        Authority authority = anAuthority();
        Query query = aQuery();
        assertThat(parse("//" + authority.asString() + "?" + query.asString()), equalTo(relativeReference(authority, query)));
    }

    @Test
    public void parsesARelativeReferenceWithAuthorityAndFragment() throws Exception {
        Authority authority = anAuthority();
        Fragment fragment = aFragment();
        assertThat(parse("//" + authority.asString() + "#" + fragment.asString()), equalTo(relativeReference(authority, fragment)));
    }

    @Test
    public void parsesARelativeReferenceWithAuthorityAndQueryAndFragment() throws Exception {
        Authority authority = anAuthority();
        Query query = aQuery();
        Fragment fragment = aFragment();
        assertThat(parse("//" + authority.asString() + "?" + query.asString() + "#" + fragment.asString()), equalTo(relativeReference(authority, query, fragment)));
    }

    @Test
    public void parsesARelativeReferenceWithAuthorityAndNonEmptyPath() throws Exception {
        Authority authority = anAuthority();
        Segment firstSegment = aSegment();
        Segment secondSegment = aSegment();
        assertThat(parse("//" + authority.asString() + "/" + firstSegment.asString() + "/" + secondSegment.asString()), equalTo(relativeReference(authority, Path.path(firstSegment, secondSegment))));
    }

    @Test
    public void parsesRelativeReferenceWithAuthorityAndPathAndQuery() throws Exception {
        Authority authority = anAuthority();
        AbsolutePath absolutePath = anAbsolutePath();
        Query query = aQuery();
        assertThat(parse("//" + authority.asString() + absolutePath.asString(NEVER_PREFIX_WITH_DOT_SEGMENT) + "?" + query.asString()), equalTo(relativeReference(authority, absolutePath, query)));
    }

    @Test
    public void parsesRelativeReferenceWithAuthorityAndPathAndFragment() throws Exception {
        Authority authority = anAuthority();
        AbsolutePath absolutePath = anAbsolutePath();
        Fragment fragment = aFragment();
        assertThat(parse("//" + authority.asString() + absolutePath.asString(NEVER_PREFIX_WITH_DOT_SEGMENT) + "#" + fragment.asString()), equalTo(relativeReference(authority, absolutePath, fragment)));
    }

    @Test
    public void parsesRelativeReferenceWithAuthorityAndPathAndQueryAndFragment() throws Exception {
        Authority authority = anAuthority();
        AbsolutePath absolutePath = anAbsolutePath();
        Query query = aQuery();
        Fragment fragment = aFragment();
        assertThat(parse("//" + authority.asString() + absolutePath.asString(NEVER_PREFIX_WITH_DOT_SEGMENT) + "?" + query.asString() + "#" + fragment.asString()), equalTo(relativeReference(authority, absolutePath, query, fragment)));
    }

}
