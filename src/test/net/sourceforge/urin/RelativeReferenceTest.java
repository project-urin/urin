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
import static net.sourceforge.urin.QueryBuilder.aQuery;
import static net.sourceforge.urin.RelativeReference.relativeReference;
import static net.sourceforge.urin.SchemeBuilder.aScheme;
import static net.sourceforge.urin.Segment.DOT;
import static net.sourceforge.urin.Segment.segment;
import static net.sourceforge.urin.SegmentBuilder.aSegment;
import static net.sourceforge.urin.Segments.PrefixWithDotSegmentCriteria.PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON;
import static net.sourceforge.urin.Segments.rootlessSegments;
import static net.sourceforge.urin.Segments.segments;
import static net.sourceforge.urin.SegmentsBuilder.aSegments;
import static net.sourceforge.urin.SegmentsBuilder.anAbsoluteSegments;
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
        assertThat(relativeReference().toString(), equalTo("RelativeReference{segments=EmptySegments}"));
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
        assertThat(relativeReference(query).toString(), equalTo("RelativeReference{segments=EmptySegments, query=" + query.toString() + "}"));
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
        assertThat(relativeReference(fragment).toString(), equalTo("RelativeReference{segments=EmptySegments, fragment=" + fragment.toString() + "}"));
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
        assertThat(relativeReference(query, fragment).toString(), equalTo("RelativeReference{segments=EmptySegments, query=" + query + ", fragment=" + fragment + "}"));
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
        Segments segments = aSegments();
        Query query = aQuery();
        assertThat(relativeReference(segments, query).asString(), equalTo(segments.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON) + "?" + query.asString()));
        assertThat(relativeReference(segments, query).asUri(), equalTo(URI.create(segments.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON) + "?" + query.asString())));
    }

    @Test
    public void aRelativeReferenceWithPathAndQueryIsEqualToAnotherRelativeReferenceWithPathAndQuery() throws Exception {
        Segments segments = aSegments();
        Query query = aQuery();
        assertThat(relativeReference(segments, query), equalTo(relativeReference(segments, query)));
        assertThat(relativeReference(segments, query).hashCode(), equalTo(relativeReference(segments, query).hashCode()));
    }

    @Test
    public void aRelativeReferenceWithPathAndQueryIsNotEqualToAnotherWithTheADifferentPath() throws Exception {
        Query query = aQuery();
        assertThat(relativeReference(aSegments(), query), not(equalTo(relativeReference(aSegments(), query))));
    }

    @Test
    public void aRelativeReferenceWithPathAndQueryIsNotEqualToAnotherWithTheADifferentQuery() throws Exception {
        Segments segments = aSegments();
        assertThat(relativeReference(segments, aQuery()), not(equalTo(relativeReference(segments, aQuery()))));
    }

    @Test
    public void aRelativeReferenceWithPathAndQueryToStringIsCorrect() throws Exception {
        Segments segments = aSegments();
        Query query = aQuery();
        assertThat(relativeReference(segments, query).toString(), equalTo("RelativeReference{segments=" + segments.toString() + ", query=" + query.toString() + "}"));
    }

    @Test
    public void rejectsNullInFactoryForARelativeReferenceWithPathAndQuery() throws Exception {
        assertThrowsNullPointerException("Null segments should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                relativeReference((Segments) null, aQuery());
            }
        });
        assertThrowsNullPointerException("Null query should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                relativeReference(aSegments(), (Query) null);
            }
        });
    }

    @Test
    public void aRelativeReferenceWithPathAndFragmentAsStringIsCorrect() throws Exception {
        Segments segments = aSegments();
        Fragment fragment = aFragment();
        assertThat(relativeReference(segments, fragment).asString(), equalTo(segments.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON) + "#" + fragment.asString()));
        assertThat(relativeReference(segments, fragment).asUri(), equalTo(URI.create(segments.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON) + "#" + fragment.asString())));
    }

    @Test
    public void aRelativeReferenceWithPathAndFragmentIsEqualToAnotherRelativeReferenceWithPathAndFragment() throws Exception {
        Segments segments = aSegments();
        Fragment fragment = aFragment();
        assertThat(relativeReference(segments, fragment), equalTo(relativeReference(segments, fragment)));
        assertThat(relativeReference(segments, fragment).hashCode(), equalTo(relativeReference(segments, fragment).hashCode()));
    }

    @Test
    public void aRelativeReferenceWithPathAndFragmentIsNotEqualToAnotherWithTheADifferentPath() throws Exception {
        Fragment fragment = aFragment();
        assertThat(relativeReference(aSegments(), fragment), not(equalTo(relativeReference(aSegments(), fragment))));
    }

    @Test
    public void aRelativeReferenceWithPathAndFragmentIsNotEqualToAnotherWithTheADifferentFragment() throws Exception {
        Segments segments = aSegments();
        assertThat(relativeReference(segments, aFragment()), not(equalTo(relativeReference(segments, aFragment()))));
    }

    @Test
    public void aRelativeReferenceWithPathAndFragmentToStringIsCorrect() throws Exception {
        Segments segments = aSegments();
        Fragment fragment = aFragment();
        assertThat(relativeReference(segments, fragment).toString(), equalTo("RelativeReference{segments=" + segments.toString() + ", fragment=" + fragment.toString() + "}"));
    }

    @Test
    public void rejectsNullInFactoryForARelativeReferenceWithPathAndFragment() throws Exception {
        assertThrowsNullPointerException("Null segments should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                relativeReference((Segments) null, aFragment());
            }
        });
        assertThrowsNullPointerException("Null fragment should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                relativeReference(aSegments(), (Fragment) null);
            }
        });
    }

    @Test
    public void aRelativeReferenceWithPathAndQueryAndFragmentAsStringIsCorrect() throws Exception {
        Segments segments = aSegments();
        Query query = aQuery();
        Fragment fragment = aFragment();
        assertThat(relativeReference(segments, query, fragment).asString(), equalTo(segments.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON) + "?" + query.asString() + "#" + fragment.asString()));
        assertThat(relativeReference(segments, query, fragment).asUri(), equalTo(URI.create(segments.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON) + "?" + query.asString() + "#" + fragment.asString())));
    }

    @Test
    public void aRelativeReferenceWithPathAndQueryAndFragmentIsEqualToAnotherRelativeReferenceWithPathAndFragment() throws Exception {
        Segments segments = aSegments();
        Query query = aQuery();
        Fragment fragment = aFragment();
        assertThat(relativeReference(segments, query, fragment), equalTo(relativeReference(segments, query, fragment)));
        assertThat(relativeReference(segments, query, fragment).hashCode(), equalTo(relativeReference(segments, query, fragment).hashCode()));
    }

    @Test
    public void aRelativeReferenceWithPathAndQueryAndFragmentIsNotEqualToAnotherWithTheADifferentPath() throws Exception {
        Query query = aQuery();
        Fragment fragment = aFragment();
        assertThat(relativeReference(aSegments(), query, fragment), not(equalTo(relativeReference(aSegments(), query, fragment))));
    }

    @Test
    public void aRelativeReferenceWithPathAndQueryAndFragmentIsNotEqualToAnotherWithTheADifferentQuery() throws Exception {
        Segments segments = aSegments();
        Fragment fragment = aFragment();
        assertThat(relativeReference(segments, aQuery(), fragment), not(equalTo(relativeReference(segments, aQuery(), fragment))));
    }

    @Test
    public void aRelativeReferenceWithPathAndQueryAndFragmentIsNotEqualToAnotherWithTheADifferentFragment() throws Exception {
        Segments segments = aSegments();
        Query query = aQuery();
        assertThat(relativeReference(segments, query, aFragment()), not(equalTo(relativeReference(segments, query, aFragment()))));
    }

    @Test
    public void aRelativeReferenceWithPathAndQueryAndFragmentToStringIsCorrect() throws Exception {
        Segments segments = aSegments();
        Query query = aQuery();
        Fragment fragment = aFragment();
        assertThat(relativeReference(segments, query, fragment).toString(), equalTo("RelativeReference{segments=" + segments.toString() + ", query=" + query.toString() + ", fragment=" + fragment.toString() + "}"));
    }

    @Test
    public void rejectsNullInFactoryForARelativeReferenceWithPathAndQueryAndFragment() throws Exception {
        assertThrowsNullPointerException("Null segments should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                relativeReference((Segments) null, aQuery(), aFragment());
            }
        });
        assertThrowsNullPointerException("Null query should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                relativeReference(aSegments(), null, aFragment());
            }
        });
        assertThrowsNullPointerException("Null fragment should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                relativeReference(aSegments(), aQuery(), null);
            }
        });
    }

    @Test
    public void aSimpleAbsolutePathAsStringReturnsThePath() throws Exception {
        Segment firstSegment = aSegment();
        Segment secondSegment = aSegment();
        assertThat(relativeReference(segments(firstSegment, secondSegment)).asString(), equalTo("/" + firstSegment.asString() + "/" + secondSegment.asString()));
        assertThat(relativeReference(segments(firstSegment, secondSegment)).asUri(), equalTo(URI.create("/" + firstSegment.asString() + "/" + secondSegment.asString())));
    }

    @Test
    public void aSimpleAbsolutePathPrefixesAnEmptyFirstSegmentWithADotSegment() throws Exception {
        Segment firstSegment = segment("");
        Segment secondSegment = aSegment();
        assertThat(relativeReference(segments(firstSegment, secondSegment)).asString(), equalTo("/./" + firstSegment.asString() + "/" + secondSegment.asString()));
    }

    @Test
    public void rejectsNullInFactoryForASimplePath() throws Exception {
        assertThrowsNullPointerException("Null first segment should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Segments segments = null;
                relativeReference(segments);
            }
        });
    }

    @Test
    public void aSimpleAbsolutePathIsEqualToAnotherWithTheSamePath() throws Exception {
        Segments segments = aSegments();
        assertThat(relativeReference(segments), equalTo(relativeReference(segments)));
        assertThat(relativeReference(segments).hashCode(), equalTo(relativeReference(segments).hashCode()));
    }

    @Test
    public void aSimpleAbsolutePathIsNotEqualToAnotherWithTheADifferentPath() throws Exception {
        assertThat(relativeReference(aSegments()), not(equalTo(relativeReference(aSegments()))));
    }

    @Test
    public void aSimpleAbsolutePathToStringIsCorrect() throws Exception {
        Segments segments = aSegments();
        assertThat(relativeReference(segments).toString(), equalTo("RelativeReference{segments=" + segments + "}"));
    }

    @Test
    public void aSimpleRootlessPathAsStringReturnsThePath() throws Exception {
        Segment firstSegment = aSegment();
        Segment secondSegment = aSegment();
        assertThat(relativeReference(rootlessSegments(firstSegment, secondSegment)).asString(), equalTo(firstSegment.asString() + "/" + secondSegment.asString()));
        assertThat(relativeReference(rootlessSegments(firstSegment, secondSegment)).asUri(), equalTo(URI.create(firstSegment.asString() + "/" + secondSegment.asString())));
    }

    @Test
    public void aSimpleRootlessPathRejectsAnEmptyFirstSegment() throws Exception {
        Segment firstSegment = segment("");
        Segment secondSegment = aSegment();
        assertThat(relativeReference(rootlessSegments(firstSegment, secondSegment)).asString(), equalTo("./" + firstSegment.asString() + "/" + secondSegment.asString()));
    }

    @Test
    public void aSimpleRootlessPathPrependsAColonInFirstSegmentWithDotSlash() throws Exception {
        Segment firstSegment = segment(aStringIncluding(':'));
        Segment secondSegment = aSegment();
        assertThat(relativeReference(rootlessSegments(firstSegment, secondSegment)), equalTo(relativeReference(rootlessSegments(DOT, firstSegment, secondSegment))));
    }

    @Test
    public void aSimpleRootlessPathPermitsAColonInTrailingSegments() throws Exception {
        relativeReference(rootlessSegments(aSegment(), segment(aStringIncluding(':')))).asString();
    }

    @Test
    public void aSimpleRootlessPathIsEqualToAnotherWithTheSamePath() throws Exception {
        Segments segments = aSegments();
        assertThat(relativeReference(segments), equalTo(relativeReference(segments)));
        assertThat(relativeReference(segments).hashCode(), equalTo(relativeReference(segments).hashCode()));
    }

    @Test
    public void aSimpleRootlessPathIsNotEqualToAnotherWithTheADifferentPath() throws Exception {
        assertThat(relativeReference(aSegments()), not(equalTo(relativeReference(aSegments()))));
    }

    @Test
    public void aSimpleRootlessPathToStringIsCorrect() throws Exception {
        Segments segments = aSegments();
        assertThat(relativeReference(segments).toString(), equalTo("RelativeReference{segments=" + segments + "}"));
    }

    @Test
    public void aRelativeReferenceWithOnlySegmentsResolvesSchemeAndAuthorityToTheBase() throws Exception {
        Scheme baseScheme = aScheme();
        HierarchicalPart baseHierarchicalPart = hierarchicalPart(anAuthority(), anAbsoluteSegments());
        Segments relativeReferenceSegments = aSegments();
        assertThat(
                relativeReference(relativeReferenceSegments).resolve(baseScheme, baseHierarchicalPart),
                equalTo(urin(baseScheme, baseHierarchicalPart.resolve(relativeReferenceSegments))));
    }

    @Test
    public void aRelativeReferenceWithOnlySegmentsResolvesSchemeAndAuthorityAndQueryToTheBase() throws Exception {
        Scheme baseScheme = aScheme();
        HierarchicalPart baseHierarchicalPart = hierarchicalPart(anAuthority(), anAbsoluteSegments());
        Segments relativeReferenceSegments = aSegments();
        Query baseQuery = aQuery();
        assertThat(
                relativeReference(relativeReferenceSegments).resolve(baseScheme, baseHierarchicalPart, baseQuery),
                equalTo(urin(baseScheme, baseHierarchicalPart.resolve(relativeReferenceSegments), baseQuery)));
    }

    @Test
    public void aRelativeReferenceWithOnlySegmentsResolvesSchemeAndAuthorityAndFragmentToTheBase() throws Exception {
        Scheme baseScheme = aScheme();
        HierarchicalPart baseHierarchicalPart = hierarchicalPart(anAuthority(), anAbsoluteSegments());
        Segments relativeReferenceSegments = aSegments();
        Fragment baseFragment = aFragment();
        assertThat(
                relativeReference(relativeReferenceSegments).resolve(baseScheme, baseHierarchicalPart, baseFragment),
                equalTo(urin(baseScheme, baseHierarchicalPart.resolve(relativeReferenceSegments), baseFragment)));
    }

    @Test
    public void aRelativeReferenceWithOnlySegmentsResolvesSchemeAndAuthorityAndQueryAndFragmentToTheBase() throws Exception {
        Scheme baseScheme = aScheme();
        HierarchicalPart baseHierarchicalPart = hierarchicalPart(anAuthority(), anAbsoluteSegments());
        Segments relativeReferenceSegments = aSegments();
        Query baseQuery = aQuery();
        Fragment baseFragment = aFragment();
        assertThat(
                relativeReference(relativeReferenceSegments).resolve(baseScheme, baseHierarchicalPart, baseQuery, baseFragment),
                equalTo(urin(baseScheme, baseHierarchicalPart.resolve(relativeReferenceSegments), baseQuery, baseFragment)));
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
        assertThat(relativeReference(authority).toString(), equalTo("RelativeReference{authority=" + authority + ", segments=EmptySegments}"));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndEmptyPathResolvesSchemeToTheBase() throws Exception {
        Scheme baseScheme = aScheme();
        HierarchicalPart baseHierarchicalPart = hierarchicalPart(anAuthority(), anAbsoluteSegments());
        Authority relativeReferenceAuthority = anAuthority();
        assertThat(
                relativeReference(relativeReferenceAuthority).resolve(baseScheme, baseHierarchicalPart),
                equalTo(urin(baseScheme, baseHierarchicalPart.resolve(relativeReferenceAuthority, new EmptySegments()))));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndEmptyPathResolvesSchemeAndAuthorityAndQueryToTheBase() throws Exception {
        Scheme baseScheme = aScheme();
        HierarchicalPart baseHierarchicalPart = hierarchicalPart(anAuthority(), anAbsoluteSegments());
        Query baseQuery = aQuery();
        Authority relativeReferenceAuthority = anAuthority();
        assertThat(
                relativeReference(relativeReferenceAuthority).resolve(baseScheme, baseHierarchicalPart, baseQuery),
                equalTo(urin(baseScheme, baseHierarchicalPart.resolve(relativeReferenceAuthority, new EmptySegments()), baseQuery)));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndEmptyPathResolvesSchemeAndAuthorityAndFragmentToTheBase() throws Exception {
        Scheme baseScheme = aScheme();
        HierarchicalPart baseHierarchicalPart = hierarchicalPart(anAuthority(), anAbsoluteSegments());
        Fragment baseFragment = aFragment();
        Authority relativeReferenceAuthority = anAuthority();
        assertThat(
                relativeReference(relativeReferenceAuthority).resolve(baseScheme, baseHierarchicalPart, baseFragment),
                equalTo(urin(baseScheme, baseHierarchicalPart.resolve(relativeReferenceAuthority, new EmptySegments()), baseFragment)));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndEmptyPathResolvesSchemeAndAuthorityAndQueryAndFragmentToTheBase() throws Exception {
        Scheme baseScheme = aScheme();
        HierarchicalPart baseHierarchicalPart = hierarchicalPart(anAuthority(), anAbsoluteSegments());
        Query baseQuery = aQuery();
        Fragment baseFragment = aFragment();
        Authority relativeReferenceAuthority = anAuthority();
        assertThat(
                relativeReference(relativeReferenceAuthority).resolve(baseScheme, baseHierarchicalPart, baseQuery, baseFragment),
                equalTo(urin(baseScheme, baseHierarchicalPart.resolve(relativeReferenceAuthority, new EmptySegments()), baseQuery, baseFragment)));
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
        assertThat(relativeReference(authority, query).toString(), equalTo("RelativeReference{authority=" + authority + ", segments=EmptySegments, query=" + query + "}"));
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
        assertThat(relativeReference(authority, fragment).toString(), equalTo("RelativeReference{authority=" + authority + ", segments=EmptySegments, fragment=" + fragment + "}"));
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
        assertThat(relativeReference(authority, query, fragment).toString(), equalTo("RelativeReference{authority=" + authority + ", segments=EmptySegments, query=" + query + ", fragment=" + fragment + "}"));
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
    public void makesRelativeReferenceWithAuthorityAndNonEmptyPath() throws Exception {
        Authority authority = anAuthority();
        Segment firstSegment = aSegment();
        Segment secondSegment = aSegment();
        assertThat(relativeReference(authority, segments(firstSegment, secondSegment)).asString(), equalTo("//" + authority.asString() + "/" + firstSegment.asString() + "/" + secondSegment.asString()));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndPathIsEqualToAnotherWithTheSameAuthorityAndPath() throws Exception {
        Authority authority = anAuthority();
        AbsoluteSegments absoluteSegments = anAbsoluteSegments();
        assertThat(relativeReference(authority, absoluteSegments), equalTo(relativeReference(authority, absoluteSegments)));
        assertThat(relativeReference(authority, absoluteSegments).hashCode(), equalTo(relativeReference(authority, absoluteSegments).hashCode()));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndPathIsNotEqualToAnotherWithTheADifferentAuthorityAndPath() throws Exception {
        assertThat(relativeReference(anAuthority(), anAbsoluteSegments()), not(equalTo(relativeReference(anAuthority(), anAbsoluteSegments()))));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndPathToStringIsCorrect() throws Exception {
        Authority authority = anAuthority();
        AbsoluteSegments absoluteSegments = anAbsoluteSegments();
        assertThat(relativeReference(authority, absoluteSegments).toString(), equalTo("RelativeReference{authority=" + authority + ", segments=" + absoluteSegments + "}"));
    }

    @Test
    public void rejectsNullInFactoryForRelativeReferenceWithAuthorityAndPath() throws Exception {
        assertThrowsNullPointerException("Null authority should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                Authority authority = null;
                relativeReference(authority, anAbsoluteSegments());
            }
        });
        assertThrowsNullPointerException("Null segments should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                AbsoluteSegments absoluteSegments = null;
                relativeReference(anAuthority(), absoluteSegments);
            }
        });
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndPathAndQueryIsEqualToAnotherWithTheSameAuthorityAndPathAndQuery() throws Exception {
        Authority authority = anAuthority();
        AbsoluteSegments absoluteSegments = anAbsoluteSegments();
        Query query = aQuery();
        assertThat(relativeReference(authority, absoluteSegments, query), equalTo(relativeReference(authority, absoluteSegments, query)));
        assertThat(relativeReference(authority, absoluteSegments, query).hashCode(), equalTo(relativeReference(authority, absoluteSegments, query).hashCode()));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndPathIsNotEqualToAnotherWithTheADifferentAuthorityAndPathAndQuery() throws Exception {
        assertThat(relativeReference(anAuthority(), anAbsoluteSegments(), aQuery()), not(equalTo(relativeReference(anAuthority(), anAbsoluteSegments(), aQuery()))));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndPathAndQueryToStringIsCorrect() throws Exception {
        Authority authority = anAuthority();
        AbsoluteSegments absoluteSegments = anAbsoluteSegments();
        Query query = aQuery();
        assertThat(relativeReference(authority, absoluteSegments, query).toString(), equalTo("RelativeReference{authority=" + authority + ", segments=" + absoluteSegments + ", query=" + query + "}"));
    }

    @Test
    public void rejectsNullInFactoryForRelativeReferenceWithAuthorityAndPathAndQuery() throws Exception {
        assertThrowsNullPointerException("Null authority should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                relativeReference(null, anAbsoluteSegments(), aQuery());
            }
        });
        assertThrowsNullPointerException("Null segments should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                relativeReference(anAuthority(), null, aQuery());
            }
        });
        assertThrowsNullPointerException("Null query should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                relativeReference(anAuthority(), anAbsoluteSegments(), (Query) null);
            }
        });
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndPathAndFragmentIsEqualToAnotherWithTheSameAuthorityAndPathAndFragment() throws Exception {
        Authority authority = anAuthority();
        AbsoluteSegments absoluteSegments = anAbsoluteSegments();
        Fragment fragment = aFragment();
        assertThat(relativeReference(authority, absoluteSegments, fragment), equalTo(relativeReference(authority, absoluteSegments, fragment)));
        assertThat(relativeReference(authority, absoluteSegments, fragment).hashCode(), equalTo(relativeReference(authority, absoluteSegments, fragment).hashCode()));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndPathIsNotEqualToAnotherWithTheADifferentAuthorityAndPathAndFragment() throws Exception {
        assertThat(relativeReference(anAuthority(), anAbsoluteSegments(), aFragment()), not(equalTo(relativeReference(anAuthority(), anAbsoluteSegments(), aFragment()))));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndPathAndFragmentToStringIsCorrect() throws Exception {
        Authority authority = anAuthority();
        AbsoluteSegments absoluteSegments = anAbsoluteSegments();
        Fragment fragment = aFragment();
        assertThat(relativeReference(authority, absoluteSegments, fragment).toString(), equalTo("RelativeReference{authority=" + authority + ", segments=" + absoluteSegments + ", fragment=" + fragment + "}"));
    }

    @Test
    public void rejectsNullInFactoryForRelativeReferenceWithAuthorityAndPathAndFragment() throws Exception {
        assertThrowsNullPointerException("Null authority should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                relativeReference(null, anAbsoluteSegments(), aFragment());
            }
        });
        assertThrowsNullPointerException("Null segments should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                relativeReference(anAuthority(), (AbsoluteSegments) null, aFragment());
            }
        });
        assertThrowsNullPointerException("Null fragment should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                relativeReference(anAuthority(), anAbsoluteSegments(), (Fragment) null);
            }
        });
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndPathAndQueryAndFragmentIsEqualToAnotherWithTheSameAuthorityAndPathAndQueryAndFragment() throws Exception {
        Authority authority = anAuthority();
        AbsoluteSegments absoluteSegments = anAbsoluteSegments();
        Query query = aQuery();
        Fragment fragment = aFragment();
        assertThat(relativeReference(authority, absoluteSegments, query, fragment), equalTo(relativeReference(authority, absoluteSegments, query, fragment)));
        assertThat(relativeReference(authority, absoluteSegments, query, fragment).hashCode(), equalTo(relativeReference(authority, absoluteSegments, query, fragment).hashCode()));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndPathIsNotEqualToAnotherWithTheADifferentAuthorityAndPathAndQueryAndFragment() throws Exception {
        assertThat(relativeReference(anAuthority(), anAbsoluteSegments(), aQuery(), aFragment()), not(equalTo(relativeReference(anAuthority(), anAbsoluteSegments(), aQuery(), aFragment()))));
    }

    @Test
    public void aRelativeReferenceWithAuthorityAndPathAndQueryAndFragmentToStringIsCorrect() throws Exception {
        Authority authority = anAuthority();
        AbsoluteSegments absoluteSegments = anAbsoluteSegments();
        Query query = aQuery();
        Fragment fragment = aFragment();
        assertThat(relativeReference(authority, absoluteSegments, query, fragment).toString(), equalTo("RelativeReference{authority=" + authority + ", segments=" + absoluteSegments + ", query=" + query + ", fragment=" + fragment + "}"));
    }

    @Test
    public void rejectsNullInFactoryForRelativeReferenceWithAuthorityAndPathAndQueryAndFragment() throws Exception {
        assertThrowsNullPointerException("Null authority should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                relativeReference(null, anAbsoluteSegments(), aQuery(), aFragment());
            }
        });
        assertThrowsNullPointerException("Null segments should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                relativeReference(anAuthority(), null, aQuery(), aFragment());
            }
        });
        assertThrowsNullPointerException("Null query should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                relativeReference(anAuthority(), anAbsoluteSegments(), null, aFragment());
            }
        });
        assertThrowsNullPointerException("Null fragment should throw NullPointerException in factory", new NullTest.NullPointerExceptionThrower() {
            public void execute() throws NullPointerException {
                //noinspection NullableProblems
                relativeReference(anAuthority(), anAbsoluteSegments(), aQuery(), null);
            }
        });
    }

}
