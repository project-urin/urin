/*
 * Copyright 2020 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.urin;

import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Locale.ENGLISH;
import static java.util.Objects.requireNonNull;
import static net.sourceforge.urin.CharacterSetMembershipFunction.*;
import static net.sourceforge.urin.ExceptionFactory.ILLEGAL_ARGUMENT_EXCEPTION_EXCEPTION_FACTORY;
import static net.sourceforge.urin.ExceptionFactory.PARSE_EXCEPTION_EXCEPTION_FACTORY;
import static net.sourceforge.urin.Fragment.stringFragmentMaker;
import static net.sourceforge.urin.Path.PrefixWithDotSegmentCriteria.*;
import static net.sourceforge.urin.Query.stringQueryMaker;

/**
 * A name component of a URI.
 * Immutable and thread safe.
 *
 * @param <SEGMENT>  The type of {@code Segment} used by paths of this scheme.
 * @param <QUERY>    The type of {@code Query} used by this scheme.
 * @param <FRAGMENT> The type of {@code Fragment} used by this scheme.
 * @see <a href="http://tools.ietf.org/html/rfc3986#section-3.1">RFC 3986 - Scheme</a>
 */
public abstract class Scheme<SEGMENT, QUERY extends Query<?>, FRAGMENT extends Fragment<?>> {

    private static final Pattern RELATIVE_REFERENCE_PATTERN = Pattern.compile("^((//([^/?#]*))?([^?#]*))(\\?([^#]*))?(#(.*))?");

    private static final Pattern URI_PATTERN = Pattern.compile("^(([^:/?#]+):)((//([^/?#]*))?([^?#]*))(\\?([^#]*))?(#(.*))?");
    private static final CharacterSetMembershipFunction TRAILING_CHARACTER_MEMBERSHIP_FUNCTION = or(
            ALPHA_LOWERCASE,
            ALPHA_UPPERCASE,
            DIGIT,
            singleMemberCharacterSet('+'),
            singleMemberCharacterSet('-'),
            singleMemberCharacterSet('.')
    );

    private final MakingDecoder<Segment<SEGMENT>, ?, String> segmentMakingDecoder;
    private final MakingDecoder<QUERY, ?, String> queryMakingDecoder;
    private final MakingDecoder<FRAGMENT, ?, String> fragmentMakingDecoder;

    Scheme(final MakingDecoder<Segment<SEGMENT>, ?, String> segmentMakingDecoder, final MakingDecoder<QUERY, ?, String> queryMakingDecoder, MakingDecoder<FRAGMENT, ?, String> fragmentMakingDecoder) {
        this.segmentMakingDecoder = segmentMakingDecoder;
        this.queryMakingDecoder = queryMakingDecoder;
        this.fragmentMakingDecoder = fragmentMakingDecoder;
    }

    Scheme(final Scheme<SEGMENT, QUERY, FRAGMENT> prototype) {
        this.segmentMakingDecoder = prototype.segmentMakingDecoder;
        this.queryMakingDecoder = prototype.queryMakingDecoder;
        this.fragmentMakingDecoder = prototype.fragmentMakingDecoder;
    }

    /**
     * Factory method for creating {@code Scheme}s.  Schemes must be at least one character long, and are permitted
     * to contain any character in the Latin alphabet, any digit, and any of the characters '+', '-', and '.'.
     *
     * @param name a {@code String} containing at least one character, made up of any characters in the Latin alphabet, the digits, and the characters '+', '-', and '.'.
     * @return a {@code Scheme} representing the given {@code String}.
     * @throws IllegalArgumentException if the given {@code String} is empty or contains characters not in the Latin alphabet, the digits, or the characters '+', '-', and '.'.
     */
    public static Scheme<String, Query<String>, Fragment<String>> scheme(final String name) {
        verify(name, ILLEGAL_ARGUMENT_EXCEPTION_EXCEPTION_FACTORY);
        return new GenericScheme<>(name.toLowerCase(ENGLISH), Segment.STRING_SEGMENT_MAKING_DECODER, stringQueryMaker(), stringFragmentMaker());
    }

    /**
     * Factory method for creating {@code Scheme}s that have a default port.  Schemes must be at least one character long, and are permitted
     * to contain any character in the Latin alphabet, any digit, and any of the characters '+', '-', and '.'.
     *
     * An example of a name that defines a default port is http, which defaults to port 80, meaning {@code http://example.com} is equivalent to, and preferred to {@code http://example.com:80}.
     *
     * @param name        a {@code String} containing at least one character, made up of any characters in the Latin alphabet, the digits, and the characters '+', '-', and '.'.
     * @param defaultPort {@code Port} representing the default port for this name.
     * @return a {@code Scheme} representing the given {@code String}, using the given default port.
     * @throws IllegalArgumentException if the given {@code String} is empty or contains characters not in the Latin alphabet, the digits, or the characters '+', '-', and '.'.
     */
    public static Scheme<String, Query<String>, Fragment<String>> scheme(final String name, final Port defaultPort) {
        verify(name, ILLEGAL_ARGUMENT_EXCEPTION_EXCEPTION_FACTORY);
        return new SchemeWithDefaultPort<>(name.toLowerCase(ENGLISH), defaultPort, Segment.STRING_SEGMENT_MAKING_DECODER, stringQueryMaker(), stringFragmentMaker());
    }

    private Scheme<SEGMENT, QUERY, FRAGMENT> parse(final String name) throws ParseException {
        verify(name, PARSE_EXCEPTION_EXCEPTION_FACTORY);
        return withName(name);
    }

    abstract Scheme<SEGMENT, QUERY, FRAGMENT> withName(String name);

    private static <T extends Exception> void verify(final String name, final ExceptionFactory<T> exceptionFactory) throws T {
        if (name.isEmpty()) {
            throw exceptionFactory.makeException("Scheme must contain at least one character");
        }
        CharacterSetMembershipFunction.verify(ALPHA, name, "scheme", 0, 1, exceptionFactory);
        CharacterSetMembershipFunction.verify(TRAILING_CHARACTER_MEMBERSHIP_FUNCTION, name, "scheme", 1, exceptionFactory);
    }

    abstract String asString();

    abstract Authority normalise(final Authority authority);

    abstract Scheme<SEGMENT, QUERY, FRAGMENT> removeDefaultPort();

    /**
     * Factory method for creating {@code RelativeReference}s with just an empty path.
     *
     * @return a {@code RelativeReference} with an empty path.
     */
    public final RelativeReference<SEGMENT, QUERY, FRAGMENT> relativeReference() {
        return new RelativeReferenceNoAuthority<>(new EmptyPath<>());
    }

    /**
     * Factory method for creating {@code RelativeReference}s with an authority and an empty path.
     *
     * @param authority any {@code Authority} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Authority} and an empty path.
     */
    public final RelativeReference<SEGMENT, QUERY, FRAGMENT> relativeReference(final Authority authority) {
        return new RelativeReferenceWithAuthority<>(authority, new EmptyPath<>());
    }

    /**
     * Factory method for creating {@code RelativeReference}s with just a path.
     *
     * @param path any {@code Path} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Path}.
     */
    public final RelativeReference<SEGMENT, QUERY, FRAGMENT> relativeReference(final Path<SEGMENT> path) {
        return new RelativeReferenceNoAuthority<>(path);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with an authority and a path.
     *
     * @param authority any {@code Authority} to use in this {@code RelativeReference}.
     * @param path      any {@code AbsolutePath} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Authority} and {@code Path}.
     */
    public final RelativeReference<SEGMENT, QUERY, FRAGMENT> relativeReference(final Authority authority, final AbsolutePath<SEGMENT> path) {
        return new RelativeReferenceWithAuthority<>(authority, path);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with a query and an empty path.
     *
     * @param query any {@code Query} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Query} and an empty path.
     */
    public final RelativeReference<SEGMENT, QUERY, FRAGMENT> relativeReference(final QUERY query) {
        return new RelativeReferenceNoAuthorityWithQuery<>(new EmptyPath<>(), query);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with an authority, a query and an empty path.
     *
     * @param authority any {@code Authority} to use in this {@code RelativeReference}.
     * @param query     any {@code Query} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Authority} and {@code Query}, and an empty path.
     */
    public final RelativeReference<SEGMENT, QUERY, FRAGMENT> relativeReference(final Authority authority, final QUERY query) {
        return new RelativeReferenceWithAuthorityAndQuery<>(authority, new EmptyPath<>(), query);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with a path and a query.
     *
     * @param path  any {@code Path} to use in this {@code RelativeReference}.
     * @param query any {@code Query} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Path} and {@code Query}.
     */
    public final RelativeReference<SEGMENT, QUERY, FRAGMENT> relativeReference(final Path<SEGMENT> path, final QUERY query) {
        return new RelativeReferenceNoAuthorityWithQuery<>(path, query);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with an authority, a path, and a query.
     *
     * @param authority any {@code Authority} to use in this {@code RelativeReference}.
     * @param path      any {@code Path} to use in this {@code RelativeReference}.
     * @param query     any {@code Query} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Authority}, {@code Path} and {@code Query}.
     */
    public final RelativeReference<SEGMENT, QUERY, FRAGMENT> relativeReference(final Authority authority, final AbsolutePath<SEGMENT> path, final QUERY query) {
        return new RelativeReferenceWithAuthorityAndQuery<>(authority, path, query);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with a fragment and an empty path.
     *
     * @param fragment any {@code Fragment} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Fragment} and an empty path.
     */
    public final RelativeReference<SEGMENT, QUERY, FRAGMENT> relativeReference(final FRAGMENT fragment) {
        return new RelativeReferenceNoAuthorityWithFragment<>(new EmptyPath<>(), fragment);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with an authority, a fragment, and an empty path.
     *
     * @param authority any {@code Authority} to use in this {@code RelativeReference}.
     * @param fragment  any {@code Fragment} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Authority} and {@code Fragment}, and an empty path.
     */
    public final RelativeReference<SEGMENT, QUERY, FRAGMENT> relativeReference(final Authority authority, final FRAGMENT fragment) {
        return new RelativeReferenceWithAuthorityAndFragment<>(authority, new EmptyPath<>(), fragment);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with a path and a fragment.
     *
     * @param path     any {@code Path} to use in this {@code RelativeReference}.
     * @param fragment any {@code Fragment} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Path} and {@code Fragment}.
     */
    public final RelativeReference<SEGMENT, QUERY, FRAGMENT> relativeReference(final Path<SEGMENT> path, final FRAGMENT fragment) {
        return new RelativeReferenceNoAuthorityWithFragment<>(path, fragment);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with an authority, a path, and fragment.
     *
     * @param authority any {@code Authority} to use in this {@code RelativeReference}.
     * @param path      any {@code AbsolutePath} to use in this {@code RelativeReference}.
     * @param fragment  any {@code Fragment} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Authority}, {@code Path} and {@code Fragment}.
     */
    public final RelativeReference<SEGMENT, QUERY, FRAGMENT> relativeReference(final Authority authority, final AbsolutePath<SEGMENT> path, final FRAGMENT fragment) {
        return new RelativeReferenceWithAuthorityAndFragment<>(authority, path, fragment);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with a query, a fragment, and an empty path.
     *
     * @param query    any {@code Query} to use in this {@code RelativeReference}.
     * @param fragment any {@code Fragment} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Query} and {@code Fragment}, and an empty path.
     */
    public final RelativeReference<SEGMENT, QUERY, FRAGMENT> relativeReference(final QUERY query, final FRAGMENT fragment) {
        return new RelativeReferenceNoAuthorityWithQueryAndFragment<>(new EmptyPath<>(), query, fragment);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with an authority, a query, a fragment, and an empty path.
     *
     * @param authority any {@code Authority} to use in this {@code RelativeReference}.
     * @param query     any {@code Query} to use in this {@code RelativeReference}.
     * @param fragment  any {@code Fragment} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Authority}, {@code Query} and {@code Fragment}, and an empty path.
     */
    public final RelativeReference<SEGMENT, QUERY, FRAGMENT> relativeReference(final Authority authority, final QUERY query, final FRAGMENT fragment) {
        return new RelativeReferenceWithAuthorityAndQueryAndFragment<>(authority, new EmptyPath<>(), query, fragment);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with a path, a query, and a fragment.
     *
     * @param path     any {@code Path} to use in this {@code RelativeReference}.
     * @param query    any {@code Query} to use in this {@code RelativeReference}.
     * @param fragment any {@code Fragment} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Path}, {@code Query} and {@code Fragment}.
     */
    public final RelativeReference<SEGMENT, QUERY, FRAGMENT> relativeReference(final Path<SEGMENT> path, final QUERY query, final FRAGMENT fragment) {
        return new RelativeReferenceNoAuthorityWithQueryAndFragment<>(path, query, fragment);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with an authority, a path, a query, and a fragment.
     *
     * @param authority any {@code Authority} to use in this {@code RelativeReference}.
     * @param path      any {@code AbsolutePath} to use in this {@code RelativeReference}.
     * @param query     any {@code Query} to use in this {@code RelativeReference}.
     * @param fragment  any {@code Fragment} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Authority}, {@code Path}, {@code Query} and {@code Fragment}.
     */
    public final RelativeReference<SEGMENT, QUERY, FRAGMENT> relativeReference(final Authority authority, final AbsolutePath<SEGMENT> path, final QUERY query, final FRAGMENT fragment) {
        return new RelativeReferenceWithAuthorityAndQueryAndFragment<>(authority, path, query, fragment);
    }

    /**
     * Parses the given {@code String} as a relative reference.
     *
     * @param relativeReferenceString a {@code String} that represents a relative reference.
     * @return a {@code UrinReference} representing the relative reference represented by the given {@code String}.
     * @throws ParseException if the given {@code String} is not a valid relative reference.
     */
    public final RelativeReference<SEGMENT, QUERY, FRAGMENT> parseRelativeReference(final String relativeReferenceString) throws ParseException {
        final Matcher matcher = RELATIVE_REFERENCE_PATTERN.matcher(relativeReferenceString);
        if (!matcher.matches()) {
            throw new ParseException("[" + relativeReferenceString + "] is not a valid relative reference");
        }
        final String queryString = matcher.group(6);
        final String fragment = matcher.group(8);
        final RelativeReference<SEGMENT, QUERY, FRAGMENT> result;
        final String authorityString = matcher.group(3);
        final String pathString = matcher.group(4);
        if (authorityString == null) {
            if (pathString == null || "".equals(pathString)) {
                if (queryString == null) {
                    if (fragment == null) {
                        result = relativeReference();
                    } else {
                        result = relativeReference(Fragment.parseFragment(fragment, fragmentMakingDecoder));
                    }
                } else {
                    final QUERY query = Query.parseQuery(queryString, queryMakingDecoder);
                    if (fragment == null) {
                        result = relativeReference(query);
                    } else {
                        result = relativeReference(query, Fragment.parseFragment(fragment, fragmentMakingDecoder));
                    }
                }
            } else {
                final Path<SEGMENT> path = !pathString.startsWith("/") ? Path.parseRootlessPath(pathString, segmentMakingDecoder) : Path.parsePath(pathString, segmentMakingDecoder);
                if (queryString == null) {
                    if (fragment == null) {
                        result = relativeReference(path);
                    } else {
                        result = relativeReference(path, Fragment.parseFragment(fragment, fragmentMakingDecoder));
                    }
                } else {
                    final QUERY query = Query.parseQuery(queryString, queryMakingDecoder);
                    if (fragment == null) {
                        result = relativeReference(path, query);
                    } else {
                        result = relativeReference(path, query, Fragment.parseFragment(fragment, fragmentMakingDecoder));
                    }
                }
            }
        } else {
            final Authority authority = Authority.parse(authorityString);
            if (pathString == null || "".equals(pathString)) {
                if (queryString == null) {
                    if (fragment == null) {
                        result = relativeReference(authority);
                    } else {
                        result = relativeReference(authority, Fragment.parseFragment(fragment, fragmentMakingDecoder));
                    }
                } else {
                    final QUERY query = Query.parseQuery(queryString, queryMakingDecoder);
                    if (fragment == null) {
                        result = relativeReference(authority, query);
                    } else {
                        result = relativeReference(authority, query, Fragment.parseFragment(fragment, fragmentMakingDecoder));
                    }
                }
            } else {
                final AbsolutePath<SEGMENT> path = Path.parsePath(pathString, segmentMakingDecoder);
                if (queryString == null) {
                    if (fragment == null) {
                        result = relativeReference(authority, path);
                    } else {
                        result = relativeReference(authority, path, Fragment.parseFragment(fragment, fragmentMakingDecoder));
                    }
                } else {
                    final QUERY query = Query.parseQuery(queryString, queryMakingDecoder);
                    if (fragment == null) {
                        result = relativeReference(authority, path, query);
                    } else {
                        result = relativeReference(authority, path, query, Fragment.parseFragment(fragment, fragmentMakingDecoder));
                    }
                }
            }
        }
        return result;
    }

    /**
     * Parses the given {@code URI} to produce a {@code RelativeReference}.
     *
     * @param uri a {@code URI} representing a relative reference to parse.
     * @return a {@code RelativeReference} representing the RFC 3986 relative reference represented by the given {@code URI}.
     * @throws ParseException if the given {@code URI} is not a valid RFC 3986 relative reference.
     */
    public final RelativeReference<SEGMENT, QUERY, FRAGMENT> parseRelativeReference(URI uri) throws ParseException {
        return parseRelativeReference(uri.toASCIIString());
    }

    private static boolean isValidRelativeReferenceString(final String uriReferenceString) {
        return RELATIVE_REFERENCE_PATTERN.matcher(uriReferenceString).matches();
    }

    /**
     * Factory method for creating {@code Urin}s with just a scheme and empty path.
     *
     * @return a {@code Urin} with the given {@code Scheme} and an empty path.
     */
    public final Urin<SEGMENT, QUERY, FRAGMENT> urin() {
        return new UrinWithPath<>(removeDefaultPort(), new EmptyPath<>());
    }

    /**
     * Factory method for creating {@code Urin}s with just scheme and path components.
     *
     * @param path any {@code Path} to use in this {@code Urin}.
     * @return a {@code Urin} with the given {@code Scheme} and {@code Path}.
     */
    public final Urin<SEGMENT, QUERY, FRAGMENT> urin(final Path<SEGMENT> path) {
        return new UrinWithPath<>(removeDefaultPort(), path);
    }

    /**
     * Factory method for creating {@code Urin}s with just scheme, authority, and empty path components.
     *
     * @param authority any {@code Authority} to use in this {@code Urin}.
     * @return a {@code Urin} with the given {@code Scheme} and {@code Authority}, and an empty path.
     */
    public final Urin<SEGMENT, QUERY, FRAGMENT> urin(final Authority authority) {
        return new UrinWithAuthorityAndPath<>(removeDefaultPort(), normalise(authority), new EmptyPath<>());
    }

    /**
     * Factory method for creating {@code Urin}s with just scheme, authority, and path components.
     *
     * @param authority any {@code Authority} to use in this {@code Urin}.
     * @param path      any {@code AbsolutePath} to use in this {@code Urin}.
     * @return a {@code Urin} with the given {@code Scheme}, {@code Authority}, and {@code AbsolutePath}.
     */
    public final Urin<SEGMENT, QUERY, FRAGMENT> urin(final Authority authority, final AbsolutePath<SEGMENT> path) {
        return new UrinWithAuthorityAndPath<>(removeDefaultPort(), normalise(authority), path);
    }

    /**
     * Factory method for creating {@code Urin}s with just a scheme, fragment, and empty path.
     *
     * @param fragment any {@code Fragment} to use in this {@code Urin}.
     * @return a {@code Urin} with the given {@code Scheme}, the given {@code Fragment}, and an empty path.
     */
    public final Urin<SEGMENT, QUERY, FRAGMENT> urin(final FRAGMENT fragment) {
        return new UrinWithPathAndFragment<>(removeDefaultPort(), new EmptyPath<>(), fragment);
    }

    /**
     * Factory method for creating {@code Urin}s with just scheme, path, and fragment components.
     *
     * @param path     any {@code Path} to use in this {@code Urin}.
     * @param fragment any {@code Fragment} to use in this {@code Urin}.
     * @return a {@code Urin} with the given {@code Scheme}, {@code Fragment}, and {@code Path}.
     */
    public final Urin<SEGMENT, QUERY, FRAGMENT> urin(final Path<SEGMENT> path, final FRAGMENT fragment) {
        return new UrinWithPathAndFragment<>(removeDefaultPort(), path, fragment);
    }

    /**
     * Factory method for creating {@code Urin}s with just scheme, authority, fragment, and empty path components.
     *
     * @param authority any {@code Authority} to use in this {@code Urin}.
     * @param fragment  any {@code Fragment} to use in this {@code Urin}.
     * @return a {@code Urin} with the given {@code Scheme}, {@code Authority}, and {@code Fragment}, and an empty path.
     */
    public final Urin<SEGMENT, QUERY, FRAGMENT> urin(final Authority authority, final FRAGMENT fragment) {
        return new UrinWithAuthorityAndPathAndFragment<>(removeDefaultPort(), normalise(authority), new EmptyPath<>(), fragment);
    }

    /**
     * Factory method for creating {@code Urin}s with just scheme, authority, path, and fragment components.
     *
     * @param authority any {@code Authority} to use in this {@code Urin}.
     * @param path      any {@code AbsolutePath} to use in this {@code Urin}.
     * @param fragment  any {@code Fragment} to use in this {@code Urin}.
     * @return a {@code Urin} with the given {@code Scheme}, {@code Authority}, {@code AbsolutePath}, and {@code Fragment}.
     */
    public final Urin<SEGMENT, QUERY, FRAGMENT> urin(final Authority authority, final AbsolutePath<SEGMENT> path, final FRAGMENT fragment) {
        return new UrinWithAuthorityAndPathAndFragment<>(removeDefaultPort(), normalise(authority), path, fragment);
    }

    /**
     * Factory method for creating {@code Urin}s with just a scheme, query, and empty path.
     *
     * @param query any {@code Query} to use in this {@code Urin}.
     * @return a {@code Urin} with the given {@code Scheme}, the given {@code Query}, and an empty path.
     */
    public final Urin<SEGMENT, QUERY, FRAGMENT> urin(final QUERY query) {
        return new UrinWithPathAndQuery<>(removeDefaultPort(), new EmptyPath<>(), query);
    }

    /**
     * Factory method for creating {@code Urin}s with just scheme, path, and query components.
     *
     * @param path  any {@code Path} to use in this {@code Urin}.
     * @param query any {@code Query} to use in this {@code Urin}.
     * @return a {@code Urin} with the given {@code Scheme}, {@code Query}, and {@code Path}.
     */
    public final Urin<SEGMENT, QUERY, FRAGMENT> urin(final Path<SEGMENT> path, final QUERY query) {
        return new UrinWithPathAndQuery<>(removeDefaultPort(), path, query);
    }

    /**
     * Factory method for creating {@code Urin}s with just scheme, authority, query, and empty path components.
     *
     * @param authority any {@code Authority} to use in this {@code Urin}.
     * @param query     any {@code Query} to use in this {@code Urin}.
     * @return a {@code Urin} with the given {@code Scheme}, {@code Authority}, and {@code Query}, and an empty path.
     */
    public final Urin<SEGMENT, QUERY, FRAGMENT> urin(final Authority authority, final QUERY query) {
        return new UrinWithAuthorityAndPathAndQuery<>(removeDefaultPort(), normalise(authority), new EmptyPath<>(), query);
    }

    /**
     * Factory method for creating {@code Urin}s with just scheme, authority, path, and query components.
     *
     * @param authority any {@code Authority} to use in this {@code Urin}.
     * @param path      any {@code AbsolutePath} to use in this {@code Urin}.
     * @param query     any {@code Query} to use in this {@code Urin}.
     * @return a {@code Urin} with the given {@code Scheme}, {@code Authority}, {@code AbsolutePath}, and {@code Query}.
     */
    public final Urin<SEGMENT, QUERY, FRAGMENT> urin(final Authority authority, final AbsolutePath<SEGMENT> path, final QUERY query) {
        return new UrinWithAuthorityAndPathAndQuery<>(removeDefaultPort(), normalise(authority), path, query);
    }

    /**
     * Factory method for creating {@code Urin}s with scheme, empty path, query, and fragment components.
     *
     * @param query    any {@code Query} to use in this {@code Urin}.
     * @param fragment any {@code Fragment} to use in this {@code Urin}.
     * @return a {@code Urin} with the given {@code Scheme}, {@code Query}, and {@code Fragment}, and an empty path.
     */
    public final Urin<SEGMENT, QUERY, FRAGMENT> urin(final QUERY query, final FRAGMENT fragment) {
        return new UrinWithPathAndQueryAndFragment<>(removeDefaultPort(), new EmptyPath<>(), query, fragment);
    }

    /**
     * Factory method for creating {@code Urin}s with scheme, path, query, and fragment components.
     *
     * @param path     any {@code Path} to use in this {@code Urin}.
     * @param query    any {@code Query} to use in this {@code Urin}.
     * @param fragment any {@code Fragment} to use in this {@code Urin}.
     * @return a {@code Urin} with the given {@code Scheme}, {@code Path}, {@code Query}, and {@code Fragment}.
     */
    public final Urin<SEGMENT, QUERY, FRAGMENT> urin(final Path<SEGMENT> path, final QUERY query, final FRAGMENT fragment) {
        return new UrinWithPathAndQueryAndFragment<>(removeDefaultPort(), path, query, fragment);
    }

    /**
     * Factory method for creating {@code Urin}s with scheme, authority, empty path, query, and fragment components.
     *
     * @param authority any {@code Authority} to use in this {@code Urin}.
     * @param query     any {@code Query} to use in this {@code Urin}.
     * @param fragment  any {@code Fragment} to use in this {@code Urin}.
     * @return a {@code Urin} with the given {@code Scheme}, {@code Authority}, {@code Query}, and {@code Fragment}, and an empty path.
     */
    public final Urin<SEGMENT, QUERY, FRAGMENT> urin(final Authority authority, final QUERY query, final FRAGMENT fragment) {
        return new UrinWithAuthorityAndPathAndQueryAndFragment<>(removeDefaultPort(), normalise(authority), new EmptyPath<>(), query, fragment);
    }

    /**
     * Factory method for creating {@code Urin}s with scheme, authority, path, query, and fragment components.
     *
     * @param authority any {@code Authority} to use in this {@code Urin}.
     * @param path      any {@code AbsolutePath} to use in this {@code Urin}.
     * @param query     any {@code Query} to use in this {@code Urin}.
     * @param fragment  any {@code Fragment} to use in this {@code Urin}.
     * @return a {@code Urin} with the given {@code Scheme}, {@code Authority}, {@code AbsolutePath}, {@code Query}, and {@code Fragment}.
     */
    public final Urin<SEGMENT, QUERY, FRAGMENT> urin(final Authority authority, final AbsolutePath<SEGMENT> path, final QUERY query, final FRAGMENT fragment) {
        return new UrinWithAuthorityAndPathAndQueryAndFragment<>(removeDefaultPort(), normalise(authority), path, query, fragment);
    }

    /**
     * Parses the given {@code String} as a URI.
     *
     * @param uriString a {@code String} that represents a URI.
     * @return a {@code Urin} representing the URI represented by the given {@code String}.
     * @throws ParseException if the given {@code String} is not a valid URI.
     */
    public final Urin<SEGMENT, QUERY, FRAGMENT> parseUrin(final String uriString) throws ParseException {
        final Matcher matcher = URI_PATTERN.matcher(uriString);
        if (!matcher.matches()) {
            throw new ParseException("[" + uriString + "] is not a valid URI");
        }
        final Scheme<SEGMENT, QUERY, FRAGMENT> scheme = parse(matcher.group(2));
        final String authorityString = matcher.group(5);
        final String pathString = matcher.group(6);
        final String queryString = matcher.group(8);
        final String fragmentString = matcher.group(10);
        final Urin<SEGMENT, QUERY, FRAGMENT> result;
        if (authorityString == null) {
            final Path<SEGMENT> path = !pathString.startsWith("/") ? Path.parseRootlessPath(pathString, segmentMakingDecoder) : Path.parsePath(pathString, segmentMakingDecoder);
            if (queryString == null) {
                if (fragmentString == null) {
                    result = scheme.urin(
                            path
                    );
                } else {
                    result = scheme.urin(
                            path,
                            Fragment.parseFragment(fragmentString, fragmentMakingDecoder)
                    );
                }
            } else {
                final QUERY query = Query.parseQuery(queryString, queryMakingDecoder);
                if (fragmentString == null) {
                    result = scheme.urin(
                            path,
                            query
                    );
                } else {
                    result = scheme.urin(
                            path,
                            query,
                            Fragment.parseFragment(fragmentString, fragmentMakingDecoder)
                    );
                }
            }
        } else {
            final Authority authority = Authority.parse(authorityString);
            if (queryString == null) {
                if (fragmentString == null) {
                    if (pathString == null || "".equals(pathString)) {
                        result = scheme.urin(
                                authority
                        );
                    } else {
                        result = scheme.urin(
                                authority,
                                Path.parsePath(pathString, segmentMakingDecoder)
                        );
                    }
                } else {
                    final FRAGMENT fragment = Fragment.parseFragment(fragmentString, fragmentMakingDecoder);
                    if (pathString == null || "".equals(pathString)) {
                        result = scheme.urin(
                                authority,
                                fragment
                        );
                    } else {
                        result = scheme.urin(
                                authority,
                                Path.parsePath(pathString, segmentMakingDecoder),
                                fragment
                        );
                    }
                }
            } else {
                final QUERY query = Query.parseQuery(queryString, queryMakingDecoder);
                if (fragmentString == null) {
                    if (pathString == null || "".equals(pathString)) {
                        result = scheme.urin(
                                authority,
                                query
                        );
                    } else {
                        result = scheme.urin(
                                authority,
                                Path.parsePath(pathString, segmentMakingDecoder),
                                query
                        );
                    }
                } else {
                    final FRAGMENT fragment = Fragment.parseFragment(fragmentString, fragmentMakingDecoder);
                    if (pathString == null || "".equals(pathString)) {
                        result = scheme.urin(
                                authority,
                                query,
                                fragment
                        );
                    } else {
                        result = scheme.urin(
                                authority,
                                Path.parsePath(pathString, segmentMakingDecoder),
                                query,
                                fragment
                        );
                    }
                }
            }
        }
        return result;
    }

    /**
     * Parses the given {@code URI} to produce a {@code Urin}.
     *
     * @param uri a {@code URI} to parse.
     * @return a {@code Urin} representing the RFC 3986 URI represented by the given {@code URI}.
     * @throws ParseException if the given {@code URI} is not a valid RFC 3986 URI.
     */
    public final Urin<SEGMENT, QUERY, FRAGMENT> parseUrin(final URI uri) throws ParseException {
        return parseUrin(uri.toASCIIString());
    }

    private static boolean isValidUrinString(final String uriReferenceString) {
        return URI_PATTERN.matcher(uriReferenceString).matches();
    }

    /**
     * Parses the given {@code String} as a URI reference.
     *
     * @param uriReferenceString a {@code String} that represents a URI reference.
     * @return a {@code UrinReference} representing the URI reference represented by the given {@code String}.
     * @throws ParseException if the given {@code String} is not a valid URI reference.
     */
    public final UrinReference<SEGMENT, QUERY, FRAGMENT> parseUrinReference(final String uriReferenceString) throws ParseException {
        if (isValidUrinString(uriReferenceString)) {
            return parseUrin(uriReferenceString);
        } else if (isValidRelativeReferenceString(uriReferenceString)) {
            return parseRelativeReference(uriReferenceString);
        }
        throw new ParseException("Given String is neither a valid URI nor a valid relative reference [" + uriReferenceString + "].");
    }

    /**
     * Parses the given {@code URI} to produce a {@code UrinReference}.
     *
     * @param uriReference a {@code URI} to parse.
     * @return a {@code UrinReference} representing the RFC 3986 URI reference represented by the given {@code URI}.
     * @throws ParseException if the given {@code URI} is not a valid RFC 3986 URI reference.
     */
    public final UrinReference<SEGMENT, QUERY, FRAGMENT> parseUrinReference(final URI uriReference) throws ParseException {
        return parseUrinReference(uriReference.toASCIIString());
    }

    public static final class GenericScheme<SEGMENT, QUERY extends Query<?>, FRAGMENT extends Fragment<?>> extends Scheme<SEGMENT, QUERY, FRAGMENT> {
        private final String name;

        public GenericScheme(String name, MakingDecoder<Segment<SEGMENT>, ?, String> segmentMakingDecoder, MakingDecoder<QUERY, ?, String> queryMakingDecoder, MakingDecoder<FRAGMENT, ?, String> fragmentMakingDecoder) {
            super(segmentMakingDecoder, queryMakingDecoder, fragmentMakingDecoder);
            this.name = name;
        }

        GenericScheme(String name, Scheme<SEGMENT, QUERY, FRAGMENT> prototype) {
            super(prototype);
            this.name = name;
        }

        @Override
        GenericScheme<SEGMENT, QUERY, FRAGMENT> withName(final String name) {
            return new GenericScheme<>(name, this);
        }

        @Override
        String asString() {
            return name.toLowerCase(ENGLISH);
        }

        @Override
        Authority normalise(final Authority authority) {
            return authority;
        }

        @Override
        Scheme<SEGMENT, QUERY, FRAGMENT> removeDefaultPort() {
            return this;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            GenericScheme<?, ?, ?> that = (GenericScheme<?, ?, ?>) o;

            return name.equals(that.name);
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }

        @Override
        public String toString() {
            return "Scheme{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

    private static final class RelativeReferenceNoAuthority<SEGMENT, QUERY extends Query<?>, FRAGMENT extends Fragment<?>> extends RelativeReference<SEGMENT, QUERY, FRAGMENT> {
        private final Path<SEGMENT> path;

        private RelativeReferenceNoAuthority(final Path<SEGMENT> path) {
            this.path = requireNonNull(path, "Cannot instantiate RelativeReference with null path");
        }

        @Override
        public RelativeReference<SEGMENT, QUERY, FRAGMENT> withPath(final AbsolutePath<SEGMENT> path) {
            return new RelativeReferenceNoAuthority<>(path);
        }

        @Override
        public String asString() {
            return path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON);
        }

        @Override
        public Path<SEGMENT> path() {
            return path;
        }

        @Override
        public boolean hasFragment() {
            return false;
        }

        @Override
        public FRAGMENT fragment() {
            throw new UnsupportedOperationException("Attempt to get fragment from a UrinReference that does not have one.");
        }

        @Override
        public boolean hasQuery() {
            return false;
        }

        @Override
        public QUERY query() {
            throw new UnsupportedOperationException("Attempt to get query from a UrinReference that does not have one.");
        }

        @Override
        public boolean hasAuthority() {
            return false;
        }

        @Override
        public Authority authority() {
            throw new UnsupportedOperationException("Attempt to get authority from a UrinReference that does not have one.");
        }

        @Override
        Urin<SEGMENT, QUERY, FRAGMENT> resolve(final Scheme<SEGMENT, QUERY, FRAGMENT> scheme, final Path<SEGMENT> path) {
            return scheme.urin(path.resolveRelativeTo(this.path));
        }

        @Override
        Urin<SEGMENT, QUERY, FRAGMENT> resolve(final Scheme<SEGMENT, QUERY, FRAGMENT> scheme, final Authority authority, final Path<SEGMENT> path) {
            return new UrinWithAuthorityAndPath<>(scheme.removeDefaultPort(), scheme.normalise(authority), this.path.resolveRelativeTo(path));
        }

        @Override
        Urin<SEGMENT, QUERY, FRAGMENT> resolve(final Scheme<SEGMENT, QUERY, FRAGMENT> scheme, final Path<SEGMENT> path, final QUERY query) {
            if (this.path.isEmpty()) {
                return scheme.urin(path.resolveRelativeTo(this.path), query);
            } else {
                return scheme.urin(path.resolveRelativeTo(this.path));
            }
        }

        @Override
        Urin<SEGMENT, QUERY, FRAGMENT> resolve(final Scheme<SEGMENT, QUERY, FRAGMENT> scheme, final Authority authority, final Path<SEGMENT> path, final QUERY query) {
            if (this.path.isEmpty()) {
                return new UrinWithAuthorityAndPathAndQuery<>(scheme.removeDefaultPort(), scheme.normalise(authority), this.path.resolveRelativeTo(path), query);
            } else {
                return new UrinWithAuthorityAndPath<>(scheme.removeDefaultPort(), scheme.normalise(authority), this.path.resolveRelativeTo(path));
            }
        }

        @Override
        Urin<SEGMENT, QUERY, FRAGMENT> resolve(final Scheme<SEGMENT, QUERY, FRAGMENT> scheme, final Path<SEGMENT> path, final QUERY query, final FRAGMENT fragment) {
            if (this.path.isEmpty()) {
                return scheme.urin(this.path.resolveRelativeTo(path), query, fragment);
            } else {
                return scheme.urin(this.path.resolveRelativeTo(path));
            }
        }

        @Override
        Urin<SEGMENT, QUERY, FRAGMENT> resolve(final Scheme<SEGMENT, QUERY, FRAGMENT> scheme, final Authority authority, final Path<SEGMENT> path, final QUERY query, final FRAGMENT fragment) {
            if (this.path.isEmpty()) {
                return new UrinWithAuthorityAndPathAndQueryAndFragment<>(scheme.removeDefaultPort(), scheme.normalise(authority), this.path.resolveRelativeTo(path), query, fragment);
            } else {
                return new UrinWithAuthorityAndPath<>(scheme.removeDefaultPort(), scheme.normalise(authority), this.path.resolveRelativeTo(path));
            }
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            RelativeReferenceNoAuthority<?, ?, ?> that = (RelativeReferenceNoAuthority<?, ?, ?>) o;
            return path.equals(that.path);
        }

        @Override
        public int hashCode() {
            return path.hashCode();
        }
    }

    private static final class RelativeReferenceWithAuthority<SEGMENT, QUERY extends Query<?>, FRAGMENT extends Fragment<?>> extends RelativeReference<SEGMENT, QUERY, FRAGMENT> {
        private final Authority authority;
        private final Path<SEGMENT> path;

        private RelativeReferenceWithAuthority(final Authority authority, final Path<SEGMENT> path) {
            this.authority = requireNonNull(authority, "Cannot instantiate RelativeReference with null authority");
            this.path = requireNonNull(path, "Cannot instantiate RelativeReference with null path");
        }

        @Override
        public RelativeReference<SEGMENT, QUERY, FRAGMENT> withPath(final AbsolutePath<SEGMENT> path) {
            return new RelativeReferenceWithAuthority<>(authority, path);
        }

        @Override
        public String asString() {
            return "//" + authority.asString() + path.asString(NEVER_PREFIX_WITH_DOT_SEGMENT);
        }

        @Override
        public Path<SEGMENT> path() {
            return path;
        }

        @Override
        public boolean hasFragment() {
            return false;
        }

        @Override
        public FRAGMENT fragment() {
            throw new UnsupportedOperationException("Attempt to get fragment from a UrinReference that does not have one.");
        }

        @Override
        public boolean hasQuery() {
            return false;
        }

        @Override
        public QUERY query() {
            throw new UnsupportedOperationException("Attempt to get query from a UrinReference that does not have one.");
        }

        @Override
        public boolean hasAuthority() {
            return true;
        }

        @Override
        public Authority authority() {
            return authority;
        }

        @Override
        Urin<SEGMENT, QUERY, FRAGMENT> resolve(final Scheme<SEGMENT, QUERY, FRAGMENT> scheme, final Path<SEGMENT> path) {
            return new UrinWithAuthorityAndPath<>(scheme.removeDefaultPort(), scheme.normalise(authority), this.path);
        }

        @Override
        Urin<SEGMENT, QUERY, FRAGMENT> resolve(final Scheme<SEGMENT, QUERY, FRAGMENT> scheme, final Authority authority, final Path<SEGMENT> path) {
            return new UrinWithAuthorityAndPath<>(scheme.removeDefaultPort(), scheme.normalise(this.authority), this.path);
        }

        @Override
        Urin<SEGMENT, QUERY, FRAGMENT> resolve(final Scheme<SEGMENT, QUERY, FRAGMENT> scheme, final Path<SEGMENT> path, final QUERY query) {
            return new UrinWithAuthorityAndPath<>(scheme.removeDefaultPort(), scheme.normalise(authority), this.path);
        }

        @Override
        Urin<SEGMENT, QUERY, FRAGMENT> resolve(final Scheme<SEGMENT, QUERY, FRAGMENT> scheme, final Authority authority, final Path<SEGMENT> path, final QUERY query) {
            return new UrinWithAuthorityAndPath<>(scheme.removeDefaultPort(), scheme.normalise(this.authority), this.path);
        }

        @Override
        Urin<SEGMENT, QUERY, FRAGMENT> resolve(final Scheme<SEGMENT, QUERY, FRAGMENT> scheme, final Path<SEGMENT> path, final QUERY query, final FRAGMENT fragment) {
            return new UrinWithAuthorityAndPath<>(scheme.removeDefaultPort(), scheme.normalise(authority), this.path);
        }

        @Override
        Urin<SEGMENT, QUERY, FRAGMENT> resolve(final Scheme<SEGMENT, QUERY, FRAGMENT> scheme, final Authority authority, final Path<SEGMENT> path, final QUERY query, final FRAGMENT fragment) {
            return new UrinWithAuthorityAndPath<>(scheme.removeDefaultPort(), scheme.normalise(this.authority), this.path);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            RelativeReferenceWithAuthority<?, ?, ?> that = (RelativeReferenceWithAuthority<?, ?, ?>) o;
            return authority.equals(that.authority)
                    && path.equals(that.path);
        }

        @Override
        public int hashCode() {
            int result = authority.hashCode();
            result = 31 * result + path.hashCode();
            return result;
        }
    }

    private static final class RelativeReferenceNoAuthorityWithQuery<SEGMENT, QUERY extends Query<?>, FRAGMENT extends Fragment<?>> extends RelativeReference<SEGMENT, QUERY, FRAGMENT> {
        private final Path<SEGMENT> path;
        private final QUERY query;

        RelativeReferenceNoAuthorityWithQuery(final Path<SEGMENT> path, final QUERY query) {
            this.path = requireNonNull(path, "Cannot instantiate RelativeReference with null path");
            this.query = requireNonNull(query, "Cannot instantiate RelativeReference with null query");
        }

        @Override
        public RelativeReference<SEGMENT, QUERY, FRAGMENT> withPath(final AbsolutePath<SEGMENT> path) {
            return new RelativeReferenceNoAuthorityWithQuery<>(path, query);
        }

        @Override
        public String asString() {
            return path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON) + '?' + query.asString();
        }

        @Override
        public Path<SEGMENT> path() {
            return path;
        }

        @Override
        public boolean hasFragment() {
            return false;
        }

        @Override
        public FRAGMENT fragment() {
            throw new UnsupportedOperationException("Attempt to get fragment from a UrinReference that does not have one.");
        }

        @Override
        public boolean hasQuery() {
            return true;
        }

        @Override
        public QUERY query() {
            return query;
        }

        @Override
        public boolean hasAuthority() {
            return false;
        }

        @Override
        public Authority authority() {
            throw new UnsupportedOperationException("Attempt to get query from a UrinReference that does not have one.");
        }

        @Override
        Urin<SEGMENT, QUERY, FRAGMENT> resolve(final Scheme<SEGMENT, QUERY, FRAGMENT> scheme, final Path<SEGMENT> path) {
            return scheme.urin(this.path.resolveRelativeTo(path), query);
        }

        @Override
        Urin<SEGMENT, QUERY, FRAGMENT> resolve(final Scheme<SEGMENT, QUERY, FRAGMENT> scheme, final Authority authority, final Path<SEGMENT> path) {
            return new UrinWithAuthorityAndPathAndQuery<>(scheme.removeDefaultPort(), scheme.normalise(authority), this.path.resolveRelativeTo(path), query);
        }

        @Override
        Urin<SEGMENT, QUERY, FRAGMENT> resolve(final Scheme<SEGMENT, QUERY, FRAGMENT> scheme, final Path<SEGMENT> path, final QUERY query) {
            return scheme.urin(this.path.resolveRelativeTo(path), this.query);
        }

        @Override
        Urin<SEGMENT, QUERY, FRAGMENT> resolve(final Scheme<SEGMENT, QUERY, FRAGMENT> scheme, final Authority authority, final Path<SEGMENT> path, final QUERY query) {
            return new UrinWithAuthorityAndPathAndQuery<>(scheme.removeDefaultPort(), scheme.normalise(authority), this.path.resolveRelativeTo(path), this.query);
        }

        @Override
        Urin<SEGMENT, QUERY, FRAGMENT> resolve(final Scheme<SEGMENT, QUERY, FRAGMENT> scheme, final Path<SEGMENT> path, final QUERY query, final FRAGMENT fragment) {
            if (this.path.isEmpty()) {
                return scheme.urin(this.path.resolveRelativeTo(path), this.query, fragment);
            } else {
                return scheme.urin(this.path.resolveRelativeTo(path), this.query);
            }
        }

        @Override
        Urin<SEGMENT, QUERY, FRAGMENT> resolve(final Scheme<SEGMENT, QUERY, FRAGMENT> scheme, final Authority authority, final Path<SEGMENT> path, final QUERY query, final FRAGMENT fragment) {
            if (this.path.isEmpty()) {
                return new UrinWithAuthorityAndPathAndQueryAndFragment<>(scheme.removeDefaultPort(), scheme.normalise(authority), this.path.resolveRelativeTo(path), this.query, fragment);
            } else {
                return new UrinWithAuthorityAndPathAndQuery<>(scheme.removeDefaultPort(), scheme.normalise(authority), this.path.resolveRelativeTo(path), this.query);
            }
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            RelativeReferenceNoAuthorityWithQuery<?, ?, ?> that = (RelativeReferenceNoAuthorityWithQuery<?, ?, ?>) o;

            return query.equals(that.query) && path.equals(that.path);

        }

        @Override
        public int hashCode() {
            int result = path.hashCode();
            result = 31 * result + query.hashCode();
            return result;
        }
    }

    private static final class RelativeReferenceNoAuthorityWithFragment<SEGMENT, QUERY extends Query<?>, FRAGMENT extends Fragment<?>> extends RelativeReference<SEGMENT, QUERY, FRAGMENT> {
        private final Path<SEGMENT> path;
        private final FRAGMENT fragment;

        RelativeReferenceNoAuthorityWithFragment(final Path<SEGMENT> path, final FRAGMENT fragment) {
            this.path = requireNonNull(path, "Cannot instantiate RelativeReference with null path");
            this.fragment = requireNonNull(fragment, "Cannot instantiate RelativeReference with null fragment");
        }

        @Override
        public RelativeReference<SEGMENT, QUERY, FRAGMENT> withPath(final AbsolutePath<SEGMENT> path) {
            return new RelativeReferenceNoAuthorityWithFragment<>(path, fragment);
        }

        @Override
        public String asString() {
            return path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON) + '#' + fragment.asString();
        }

        @Override
        public Path<SEGMENT> path() {
            return path;
        }

        @Override
        public boolean hasFragment() {
            return true;
        }

        @Override
        public FRAGMENT fragment() {
            return fragment;
        }

        @Override
        public boolean hasQuery() {
            return false;
        }

        @Override
        public QUERY query() {
            throw new UnsupportedOperationException("Attempt to get query from a UrinReference that does not have one.");
        }

        @Override
        public boolean hasAuthority() {
            return false;
        }

        @Override
        public Authority authority() {
            throw new UnsupportedOperationException("Attempt to get query from a UrinReference that does not have one.");
        }

        @Override
        Urin<SEGMENT, QUERY, FRAGMENT> resolve(final Scheme<SEGMENT, QUERY, FRAGMENT> scheme, final Path<SEGMENT> path) {
            return scheme.urin(this.path.resolveRelativeTo(path), fragment);
        }

        @Override
        Urin<SEGMENT, QUERY, FRAGMENT> resolve(final Scheme<SEGMENT, QUERY, FRAGMENT> scheme, final Authority authority, final Path<SEGMENT> path) {
            return new UrinWithAuthorityAndPathAndFragment<>(scheme.removeDefaultPort(), scheme.normalise(authority), this.path.resolveRelativeTo(path), fragment);
        }

        @Override
        Urin<SEGMENT, QUERY, FRAGMENT> resolve(final Scheme<SEGMENT, QUERY, FRAGMENT> scheme, final Path<SEGMENT> path, final QUERY query) {
            if (this.path.isEmpty()) {
                return scheme.urin(this.path.resolveRelativeTo(path), query, fragment);
            } else {
                return scheme.urin(this.path.resolveRelativeTo(path), fragment);
            }
        }

        @Override
        Urin<SEGMENT, QUERY, FRAGMENT> resolve(final Scheme<SEGMENT, QUERY, FRAGMENT> scheme, final Authority authority, final Path<SEGMENT> path, final QUERY query) {
            if (this.path.isEmpty()) {
                return new UrinWithAuthorityAndPathAndQueryAndFragment<>(scheme.removeDefaultPort(), scheme.normalise(authority), this.path.resolveRelativeTo(path), query, fragment);
            } else {
                return new UrinWithAuthorityAndPathAndFragment<>(scheme.removeDefaultPort(), scheme.normalise(authority), this.path.resolveRelativeTo(path), fragment);
            }
        }

        @Override
        Urin<SEGMENT, QUERY, FRAGMENT> resolve(final Scheme<SEGMENT, QUERY, FRAGMENT> scheme, final Path<SEGMENT> path, final QUERY query, final FRAGMENT fragment) {
            if (this.path.isEmpty()) {
                return scheme.urin(this.path.resolveRelativeTo(path), query, this.fragment);
            } else {
                return scheme.urin(this.path.resolveRelativeTo(path), this.fragment);
            }
        }

        @Override
        Urin<SEGMENT, QUERY, FRAGMENT> resolve(final Scheme<SEGMENT, QUERY, FRAGMENT> scheme, final Authority authority, final Path<SEGMENT> path, final QUERY query, final FRAGMENT fragment) {
            if (this.path.isEmpty()) {
                return new UrinWithAuthorityAndPathAndQueryAndFragment<>(scheme.removeDefaultPort(), scheme.normalise(authority), this.path.resolveRelativeTo(path), query, this.fragment);
            } else {
                return new UrinWithAuthorityAndPathAndFragment<>(scheme.removeDefaultPort(), scheme.normalise(authority), this.path.resolveRelativeTo(path), this.fragment);
            }
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            RelativeReferenceNoAuthorityWithFragment<?, ?, ?> that = (RelativeReferenceNoAuthorityWithFragment<?, ?, ?>) o;

            return fragment.equals(that.fragment) && path.equals(that.path);

        }

        @Override
        public int hashCode() {
            int result = path.hashCode();
            result = 31 * result + fragment.hashCode();
            return result;
        }
    }

    private static final class RelativeReferenceNoAuthorityWithQueryAndFragment<SEGMENT, QUERY extends Query<?>, FRAGMENT extends Fragment<?>> extends RelativeReference<SEGMENT, QUERY, FRAGMENT> {
        private final Path<SEGMENT> path;
        private final FRAGMENT fragment;
        private final QUERY query;

        RelativeReferenceNoAuthorityWithQueryAndFragment(final Path<SEGMENT> path, final QUERY query, final FRAGMENT fragment) {
            this.path = requireNonNull(path, "Cannot instantiate RelativeReference with null path");
            this.query = requireNonNull(query, "Cannot instantiate RelativeReference with null query");
            this.fragment = requireNonNull(fragment, "Cannot instantiate RelativeReference with null fragment");
        }

        @Override
        public RelativeReference<SEGMENT, QUERY, FRAGMENT> withPath(final AbsolutePath<SEGMENT> path) {
            return new RelativeReferenceNoAuthorityWithQueryAndFragment<>(path, query, fragment);
        }

        @Override
        public String asString() {
            return path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY_OR_CONTAINS_COLON) + '?' + query.asString() + '#' + fragment.asString();
        }

        @Override
        public Path<SEGMENT> path() {
            return path;
        }

        @Override
        public boolean hasFragment() {
            return true;
        }

        @Override
        public FRAGMENT fragment() {
            return fragment;
        }

        @Override
        public boolean hasQuery() {
            return true;
        }

        @Override
        public QUERY query() {
            return query;
        }

        @Override
        public boolean hasAuthority() {
            return false;
        }

        @Override
        public Authority authority() {
            throw new UnsupportedOperationException("Attempt to get query from a UrinReference that does not have one.");
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            RelativeReferenceNoAuthorityWithQueryAndFragment<?, ?, ?> that = (RelativeReferenceNoAuthorityWithQueryAndFragment<?, ?, ?>) o;

            return fragment.equals(that.fragment) && query.equals(that.query) && path.equals(that.path);
        }

        @Override
        public int hashCode() {
            int result = path.hashCode();
            result = 31 * result + fragment.hashCode();
            result = 31 * result + query.hashCode();
            return result;
        }

        @Override
        Urin<SEGMENT, QUERY, FRAGMENT> resolve(final Scheme<SEGMENT, QUERY, FRAGMENT> scheme, final Path<SEGMENT> path) {
            return scheme.urin(this.path.resolveRelativeTo(path), query, fragment);
        }

        @Override
        Urin<SEGMENT, QUERY, FRAGMENT> resolve(final Scheme<SEGMENT, QUERY, FRAGMENT> scheme, final Authority authority, final Path<SEGMENT> path) {
            return new UrinWithAuthorityAndPathAndQueryAndFragment<>(scheme.removeDefaultPort(), scheme.normalise(authority), this.path.resolveRelativeTo(path), query, fragment);
        }

        @Override
        Urin<SEGMENT, QUERY, FRAGMENT> resolve(final Scheme<SEGMENT, QUERY, FRAGMENT> scheme, final Path<SEGMENT> path, final QUERY query) {
            return scheme.urin(this.path.resolveRelativeTo(path), this.query, fragment);
        }

        @Override
        Urin<SEGMENT, QUERY, FRAGMENT> resolve(final Scheme<SEGMENT, QUERY, FRAGMENT> scheme, final Authority authority, final Path<SEGMENT> path, final QUERY query) {
            return new UrinWithAuthorityAndPathAndQueryAndFragment<>(scheme.removeDefaultPort(), scheme.normalise(authority), this.path.resolveRelativeTo(path), this.query, fragment);
        }

        @Override
        Urin<SEGMENT, QUERY, FRAGMENT> resolve(final Scheme<SEGMENT, QUERY, FRAGMENT> scheme, final Path<SEGMENT> path, final QUERY query, final FRAGMENT fragment) {
            return scheme.urin(this.path.resolveRelativeTo(path), this.query, this.fragment);
        }

        @Override
        Urin<SEGMENT, QUERY, FRAGMENT> resolve(final Scheme<SEGMENT, QUERY, FRAGMENT> scheme, final Authority authority, final Path<SEGMENT> path, final QUERY query, final FRAGMENT fragment) {
            return new UrinWithAuthorityAndPathAndQueryAndFragment<>(scheme.removeDefaultPort(), scheme.normalise(authority), this.path.resolveRelativeTo(path), this.query, this.fragment);
        }
    }

    private static final class RelativeReferenceWithAuthorityAndQuery<SEGMENT, QUERY extends Query<?>, FRAGMENT extends Fragment<?>> extends RelativeReference<SEGMENT, QUERY, FRAGMENT> {
        private final Authority authority;
        private final Path<SEGMENT> path;
        private final QUERY query;

        RelativeReferenceWithAuthorityAndQuery(final Authority authority, final Path<SEGMENT> path, final QUERY query) {
            this.authority = requireNonNull(authority, "Cannot instantiate RelativeReference with null authority");
            this.path = requireNonNull(path, "Cannot instantiate RelativeReference with null path");
            this.query = requireNonNull(query, "Cannot instantiate RelativeReference with null query");
        }

        @Override
        public RelativeReference<SEGMENT, QUERY, FRAGMENT> withPath(final AbsolutePath<SEGMENT> path) {
            return new RelativeReferenceWithAuthorityAndQuery<>(authority, path, query);
        }

        @Override
        public String asString() {
            return "//" + authority.asString() + path.asString(NEVER_PREFIX_WITH_DOT_SEGMENT) + '?' + query.asString();
        }

        @Override
        public Path<SEGMENT> path() {
            return path;
        }

        @Override
        public boolean hasFragment() {
            return false;
        }

        @Override
        public FRAGMENT fragment() {
            throw new UnsupportedOperationException("Attempt to get fragment from a UrinReference that does not have one.");
        }

        @Override
        public boolean hasQuery() {
            return true;
        }

        @Override
        public QUERY query() {
            return query;
        }

        @Override
        public boolean hasAuthority() {
            return true;
        }

        @Override
        public Authority authority() {
            return authority;
        }

        @Override
        Urin<SEGMENT, QUERY, FRAGMENT> resolve(final Scheme<SEGMENT, QUERY, FRAGMENT> scheme, final Path<SEGMENT> path) {
            return new UrinWithAuthorityAndPathAndQuery<>(scheme.removeDefaultPort(), scheme.normalise(authority), this.path, query);
        }

        @Override
        Urin<SEGMENT, QUERY, FRAGMENT> resolve(final Scheme<SEGMENT, QUERY, FRAGMENT> scheme, final Authority authority, final Path<SEGMENT> path) {
            return new UrinWithAuthorityAndPathAndQuery<>(scheme.removeDefaultPort(), scheme.normalise(this.authority), this.path, query);
        }

        @Override
        Urin<SEGMENT, QUERY, FRAGMENT> resolve(final Scheme<SEGMENT, QUERY, FRAGMENT> scheme, final Path<SEGMENT> path, final QUERY query) {
            return new UrinWithAuthorityAndPathAndQuery<>(scheme.removeDefaultPort(), scheme.normalise(authority), this.path, this.query);
        }

        @Override
        Urin<SEGMENT, QUERY, FRAGMENT> resolve(final Scheme<SEGMENT, QUERY, FRAGMENT> scheme, final Authority authority, final Path<SEGMENT> path, final QUERY query) {
            return new UrinWithAuthorityAndPathAndQuery<>(scheme.removeDefaultPort(), scheme.normalise(this.authority), this.path, this.query);
        }

        @Override
        Urin<SEGMENT, QUERY, FRAGMENT> resolve(final Scheme<SEGMENT, QUERY, FRAGMENT> scheme, final Path<SEGMENT> path, final QUERY query, final FRAGMENT fragment) {
            return new UrinWithAuthorityAndPathAndQuery<>(scheme.removeDefaultPort(), scheme.normalise(authority), this.path, this.query);
        }

        @Override
        Urin<SEGMENT, QUERY, FRAGMENT> resolve(final Scheme<SEGMENT, QUERY, FRAGMENT> scheme, final Authority authority, final Path<SEGMENT> path, final QUERY query, final FRAGMENT fragment) {
            return new UrinWithAuthorityAndPathAndQuery<>(scheme.removeDefaultPort(), scheme.normalise(this.authority), this.path, this.query);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            RelativeReferenceWithAuthorityAndQuery<?, ?, ?> that = (RelativeReferenceWithAuthorityAndQuery<?, ?, ?>) o;

            return authority.equals(that.authority) && query.equals(that.query) && path.equals(that.path);
        }

        @Override
        public int hashCode() {
            int result = authority.hashCode();
            result = 31 * result + path.hashCode();
            result = 31 * result + query.hashCode();
            return result;
        }
    }

    private static final class RelativeReferenceWithAuthorityAndFragment<SEGMENT, QUERY extends Query<?>, FRAGMENT extends Fragment<?>> extends RelativeReference<SEGMENT, QUERY, FRAGMENT> {
        private final Authority authority;
        private final Path<SEGMENT> path;
        private final FRAGMENT fragment;

        RelativeReferenceWithAuthorityAndFragment(final Authority authority, final Path<SEGMENT> path, final FRAGMENT fragment) {
            this.authority = requireNonNull(authority, "Cannot instantiate RelativeReference with null authority");
            this.path = requireNonNull(path, "Cannot instantiate RelativeReference with null path");
            this.fragment = requireNonNull(fragment, "Cannot instantiate RelativeReference with null fragment");
        }

        @Override
        public RelativeReference<SEGMENT, QUERY, FRAGMENT> withPath(final AbsolutePath<SEGMENT> path) {
            return new RelativeReferenceWithAuthorityAndFragment<>(authority, path, fragment);
        }

        @Override
        public String asString() {
            return "//" + authority.asString() + path.asString(NEVER_PREFIX_WITH_DOT_SEGMENT) + '#' + fragment.asString();
        }

        @Override
        public Path<SEGMENT> path() {
            return path;
        }

        @Override
        public boolean hasFragment() {
            return true;
        }

        @Override
        public FRAGMENT fragment() {
            return fragment;
        }

        @Override
        public boolean hasQuery() {
            return false;
        }

        @Override
        public QUERY query() {
            throw new UnsupportedOperationException("Attempt to get query from a UrinReference that does not have one.");
        }

        @Override
        public boolean hasAuthority() {
            return true;
        }

        @Override
        public Authority authority() {
            return authority;
        }

        @Override
        Urin<SEGMENT, QUERY, FRAGMENT> resolve(final Scheme<SEGMENT, QUERY, FRAGMENT> scheme, final Path<SEGMENT> path) {
            return new UrinWithAuthorityAndPathAndFragment<>(scheme.removeDefaultPort(), scheme.normalise(authority), this.path, fragment);
        }

        @Override
        Urin<SEGMENT, QUERY, FRAGMENT> resolve(final Scheme<SEGMENT, QUERY, FRAGMENT> scheme, final Authority authority, final Path<SEGMENT> path) {
            return new UrinWithAuthorityAndPathAndFragment<>(scheme.removeDefaultPort(), scheme.normalise(this.authority), this.path, fragment);
        }

        @Override
        Urin<SEGMENT, QUERY, FRAGMENT> resolve(final Scheme<SEGMENT, QUERY, FRAGMENT> scheme, final Path<SEGMENT> path, final QUERY query) {
            return new UrinWithAuthorityAndPathAndFragment<>(scheme.removeDefaultPort(), scheme.normalise(authority), this.path, fragment);
        }

        @Override
        Urin<SEGMENT, QUERY, FRAGMENT> resolve(final Scheme<SEGMENT, QUERY, FRAGMENT> scheme, final Authority authority, final Path<SEGMENT> path, final QUERY query) {
            return new UrinWithAuthorityAndPathAndFragment<>(scheme.removeDefaultPort(), scheme.normalise(this.authority), this.path, fragment);
        }

        @Override
        Urin<SEGMENT, QUERY, FRAGMENT> resolve(final Scheme<SEGMENT, QUERY, FRAGMENT> scheme, final Path<SEGMENT> path, final QUERY query, final FRAGMENT fragment) {
            return new UrinWithAuthorityAndPathAndFragment<>(scheme.removeDefaultPort(), scheme.normalise(authority), this.path, this.fragment);
        }

        @Override
        Urin<SEGMENT, QUERY, FRAGMENT> resolve(final Scheme<SEGMENT, QUERY, FRAGMENT> scheme, final Authority authority, final Path<SEGMENT> path, final QUERY query, final FRAGMENT fragment) {
            return new UrinWithAuthorityAndPathAndFragment<>(scheme.removeDefaultPort(), scheme.normalise(this.authority), this.path, this.fragment);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            RelativeReferenceWithAuthorityAndFragment<?, ?, ?> that = (RelativeReferenceWithAuthorityAndFragment<?, ?, ?>) o;

            return authority.equals(that.authority) && fragment.equals(that.fragment) && path.equals(that.path);
        }

        @Override
        public int hashCode() {
            int result = authority.hashCode();
            result = 31 * result + path.hashCode();
            result = 31 * result + fragment.hashCode();
            return result;
        }
    }

    private static final class RelativeReferenceWithAuthorityAndQueryAndFragment<SEGMENT, QUERY extends Query<?>, FRAGMENT extends Fragment<?>> extends RelativeReference<SEGMENT, QUERY, FRAGMENT> {
        private final Authority authority;
        private final Path<SEGMENT> path;
        private final QUERY query;
        private final FRAGMENT fragment;

        RelativeReferenceWithAuthorityAndQueryAndFragment(final Authority authority, final Path<SEGMENT> path, final QUERY query, final FRAGMENT fragment) {
            this.authority = requireNonNull(authority, "Cannot instantiate RelativeReference with null authority");
            this.path = requireNonNull(path, "Cannot instantiate RelativeReference with null path");
            this.query = requireNonNull(query, "Cannot instantiate RelativeReference with null query");
            this.fragment = requireNonNull(fragment, "Cannot instantiate RelativeReference with null fragment");
        }

        @Override
        public RelativeReference<SEGMENT, QUERY, FRAGMENT> withPath(final AbsolutePath<SEGMENT> path) {
            return new RelativeReferenceWithAuthorityAndQueryAndFragment<>(authority, path, query, fragment);
        }

        @Override
        public String asString() {
            return "//" + authority.asString() + path.asString(NEVER_PREFIX_WITH_DOT_SEGMENT) + '?' + query.asString() + '#' + fragment.asString();
        }

        @Override
        public Path<SEGMENT> path() {
            return path;
        }

        @Override
        public boolean hasFragment() {
            return true;
        }

        @Override
        public FRAGMENT fragment() {
            return fragment;
        }

        @Override
        public boolean hasQuery() {
            return true;
        }

        @Override
        public QUERY query() {
            return query;
        }

        @Override
        public boolean hasAuthority() {
            return true;
        }

        @Override
        public Authority authority() {
            return authority;
        }

        @Override
        Urin<SEGMENT, QUERY, FRAGMENT> resolve(final Scheme<SEGMENT, QUERY, FRAGMENT> scheme, final Path<SEGMENT> path) {
            return new UrinWithAuthorityAndPathAndQueryAndFragment<>(scheme.removeDefaultPort(), scheme.normalise(authority), this.path, query, fragment);
        }

        @Override
        Urin<SEGMENT, QUERY, FRAGMENT> resolve(final Scheme<SEGMENT, QUERY, FRAGMENT> scheme, final Authority authority, final Path<SEGMENT> path) {
            return new UrinWithAuthorityAndPathAndQueryAndFragment<>(scheme.removeDefaultPort(), scheme.normalise(this.authority), this.path, query, fragment);
        }

        @Override
        Urin<SEGMENT, QUERY, FRAGMENT> resolve(final Scheme<SEGMENT, QUERY, FRAGMENT> scheme, final Path<SEGMENT> path, final QUERY query) {
            return new UrinWithAuthorityAndPathAndQueryAndFragment<>(scheme.removeDefaultPort(), scheme.normalise(authority), this.path, this.query, fragment);
        }

        @Override
        Urin<SEGMENT, QUERY, FRAGMENT> resolve(final Scheme<SEGMENT, QUERY, FRAGMENT> scheme, final Authority authority, final Path<SEGMENT> path, final QUERY query) {
            return new UrinWithAuthorityAndPathAndQueryAndFragment<>(scheme.removeDefaultPort(), scheme.normalise(this.authority), this.path, this.query, fragment);
        }

        @Override
        Urin<SEGMENT, QUERY, FRAGMENT> resolve(final Scheme<SEGMENT, QUERY, FRAGMENT> scheme, final Path<SEGMENT> path, final QUERY query, final FRAGMENT fragment) {
            return new UrinWithAuthorityAndPathAndQueryAndFragment<>(scheme.removeDefaultPort(), scheme.normalise(authority), this.path, this.query, this.fragment);
        }

        @Override
        Urin<SEGMENT, QUERY, FRAGMENT> resolve(final Scheme<SEGMENT, QUERY, FRAGMENT> scheme, final Authority authority, final Path<SEGMENT> path, final QUERY query, final FRAGMENT fragment) {
            return new UrinWithAuthorityAndPathAndQueryAndFragment<>(scheme.removeDefaultPort(), scheme.normalise(this.authority), this.path, this.query, this.fragment);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            RelativeReferenceWithAuthorityAndQueryAndFragment<?, ?, ?> that = (RelativeReferenceWithAuthorityAndQueryAndFragment<?, ?, ?>) o;

            return authority.equals(that.authority) && fragment.equals(that.fragment) && query.equals(that.query) && path.equals(that.path);
        }

        @Override
        public int hashCode() {
            int result = authority.hashCode();
            result = 31 * result + path.hashCode();
            result = 31 * result + query.hashCode();
            result = 31 * result + fragment.hashCode();
            return result;
        }
    }

    private static final class UrinWithPathAndQueryAndFragment<SEGMENT, QUERY extends Query<?>, FRAGMENT extends Fragment<?>> extends Urin<SEGMENT, QUERY, FRAGMENT> {
        private final Scheme<SEGMENT, QUERY, FRAGMENT> scheme;
        private final Path<SEGMENT> path;
        private final QUERY query;
        private final FRAGMENT fragment;

        UrinWithPathAndQueryAndFragment(final Scheme<SEGMENT, QUERY, FRAGMENT> scheme, final Path<SEGMENT> path, final QUERY query, final FRAGMENT fragment) {
            this.scheme = requireNonNull(scheme, "Cannot instantiate Urin with null scheme");
            this.path = requireNonNull(path, "Cannot instantiate Urin with null path");
            this.query = requireNonNull(query, "Cannot instantiate Urin with null query");
            this.fragment = requireNonNull(fragment, "Cannot instantiate Urin with null fragment");
        }

        @Override
        public Urin<SEGMENT, QUERY, FRAGMENT> withPath(final AbsolutePath<SEGMENT> path) {
            return new UrinWithPathAndQueryAndFragment<>(scheme, path, query, fragment);
        }

        @Override
        public String asString() {
            return scheme.asString() + ':' + path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY) + '?' + query.asString() + '#' + fragment.asString();
        }

        @Override
        public Path<SEGMENT> path() {
            return path;
        }

        @Override
        public boolean hasFragment() {
            return true;
        }

        @Override
        public FRAGMENT fragment() {
            return fragment;
        }

        @Override
        public boolean hasQuery() {
            return true;
        }

        @Override
        public QUERY query() {
            return query;
        }

        @Override
        public boolean hasAuthority() {
            return false;
        }

        @Override
        public Authority authority() {
            throw new UnsupportedOperationException("Attempt to get query from a UrinReference that does not have one.");
        }

        @Override
        public Urin<SEGMENT, QUERY, FRAGMENT> resolve(final UrinReference<SEGMENT, QUERY, FRAGMENT> urinReference) {
            return urinReference.resolve(scheme, path, query, fragment);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            UrinWithPathAndQueryAndFragment<?, ?, ?> that = (UrinWithPathAndQueryAndFragment<?, ?, ?>) o;
            return fragment.equals(that.fragment)
                    && path.equals(that.path)
                    && query.equals(that.query)
                    && scheme.equals(that.scheme);
        }

        @Override
        public int hashCode() {
            int result = scheme.hashCode();
            result = 31 * result + path.hashCode();
            result = 31 * result + query.hashCode();
            result = 31 * result + fragment.hashCode();
            return result;
        }
    }

    private static final class UrinWithAuthorityAndPathAndQueryAndFragment<SEGMENT, QUERY extends Query<?>, FRAGMENT extends Fragment<?>> extends Urin<SEGMENT, QUERY, FRAGMENT> {
        private final Scheme<SEGMENT, QUERY, FRAGMENT> scheme;
        private final Authority authority;
        private final Path<SEGMENT> path;
        private final QUERY query;
        private final FRAGMENT fragment;

        UrinWithAuthorityAndPathAndQueryAndFragment(final Scheme<SEGMENT, QUERY, FRAGMENT> scheme, final Authority authority, final Path<SEGMENT> path, final QUERY query, final FRAGMENT fragment) {
            this.scheme = requireNonNull(scheme, "Cannot instantiate Urin with null scheme");
            this.authority = requireNonNull(authority, "Cannot instantiate Urin with null authority");
            this.path = requireNonNull(path, "Cannot instantiate Urin with null path");
            this.query = requireNonNull(query, "Cannot instantiate Urin with null query");
            this.fragment = requireNonNull(fragment, "Cannot instantiate Urin with null fragment");
        }

        @Override
        public Urin<SEGMENT, QUERY, FRAGMENT> withPath(final AbsolutePath<SEGMENT> path) {
            return new UrinWithAuthorityAndPathAndQueryAndFragment<>(scheme, authority, path, query, fragment);
        }

        @Override
        public String asString() {
            return scheme.asString() + "://" + authority.asString() + path.asString(NEVER_PREFIX_WITH_DOT_SEGMENT) + '?' + query.asString() + '#' + fragment.asString();
        }

        @Override
        public Path<SEGMENT> path() {
            return path;
        }

        @Override
        public boolean hasFragment() {
            return true;
        }

        @Override
        public FRAGMENT fragment() {
            return fragment;
        }

        @Override
        public boolean hasQuery() {
            return true;
        }

        @Override
        public QUERY query() {
            return query;
        }

        @Override
        public boolean hasAuthority() {
            return true;
        }

        @Override
        public Authority authority() {
            return authority;
        }

        @Override
        public Urin<SEGMENT, QUERY, FRAGMENT> resolve(final UrinReference<SEGMENT, QUERY, FRAGMENT> urinReference) {
            return urinReference.resolve(scheme, authority, path, query, fragment);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            UrinWithAuthorityAndPathAndQueryAndFragment<?, ?, ?> that = (UrinWithAuthorityAndPathAndQueryAndFragment<?, ?, ?>) o;

            return authority.equals(that.authority) && fragment.equals(that.fragment) && path.equals(that.path) && query.equals(that.query) && scheme.equals(that.scheme);

        }

        @Override
        public int hashCode() {
            int result = scheme.hashCode();
            result = 31 * result + authority.hashCode();
            result = 31 * result + path.hashCode();
            result = 31 * result + query.hashCode();
            result = 31 * result + fragment.hashCode();
            return result;
        }
    }

    private static final class UrinWithPathAndQuery<SEGMENT, QUERY extends Query<?>, FRAGMENT extends Fragment<?>> extends Urin<SEGMENT, QUERY, FRAGMENT> {
        private final Scheme<SEGMENT, QUERY, FRAGMENT> scheme;
        private final Path<SEGMENT> path;
        private final QUERY query;

        UrinWithPathAndQuery(final Scheme<SEGMENT, QUERY, FRAGMENT> scheme, final Path<SEGMENT> path, final QUERY query) {
            this.scheme = requireNonNull(scheme, "cannot instantiate Urin with null scheme");
            this.path = requireNonNull(path, "cannot instantiate Urin with null path");
            this.query = requireNonNull(query, "cannot instantiate Urin with null query");
        }

        @Override
        public Urin<SEGMENT, QUERY, FRAGMENT> withPath(final AbsolutePath<SEGMENT> path) {
            return new UrinWithPathAndQuery<>(scheme, path, query);
        }

        @Override
        public String asString() {
            return scheme.asString() + ':' + path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY) + '?' + query.asString();
        }

        @Override
        public Path<SEGMENT> path() {
            return path;
        }

        @Override
        public boolean hasFragment() {
            return false;
        }

        @Override
        public FRAGMENT fragment() {
            throw new UnsupportedOperationException("Attempt to get fragment from a UrinReference that does not have one.");
        }

        @Override
        public boolean hasQuery() {
            return true;
        }

        @Override
        public QUERY query() {
            return query;
        }

        @Override
        public boolean hasAuthority() {
            return false;
        }

        @Override
        public Authority authority() {
            throw new UnsupportedOperationException("Attempt to get query from a UrinReference that does not have one.");
        }

        @Override
        public Urin<SEGMENT, QUERY, FRAGMENT> resolve(final UrinReference<SEGMENT, QUERY, FRAGMENT> urinReference) {
            return urinReference.resolve(scheme, path, query);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            UrinWithPathAndQuery<?, ?, ?> that = (UrinWithPathAndQuery<?, ?, ?>) o;
            return path.equals(that.path)
                    && query.equals(that.query)
                    && scheme.equals(that.scheme);
        }

        @Override
        public int hashCode() {
            int result = scheme.hashCode();
            result = 31 * result + path.hashCode();
            result = 31 * result + query.hashCode();
            return result;
        }
    }

    private static final class UrinWithAuthorityAndPathAndQuery<SEGMENT, QUERY extends Query<?>, FRAGMENT extends Fragment<?>> extends Urin<SEGMENT, QUERY, FRAGMENT> {
        private final Scheme<SEGMENT, QUERY, FRAGMENT> scheme;
        private final Authority authority;
        private final Path<SEGMENT> path;
        private final QUERY query;

        UrinWithAuthorityAndPathAndQuery(final Scheme<SEGMENT, QUERY, FRAGMENT> scheme, final Authority authority, final Path<SEGMENT> path, final QUERY query) {
            this.scheme = requireNonNull(scheme, "cannot instantiate Urin with null scheme");
            this.authority = requireNonNull(authority, "cannot instantiate Urin with null authority");
            this.path = requireNonNull(path, "cannot instantiate Urin with null path");
            this.query = requireNonNull(query, "cannot instantiate Urin with null query");
        }

        @Override
        public Urin<SEGMENT, QUERY, FRAGMENT> withPath(final AbsolutePath<SEGMENT> path) {
            return new UrinWithAuthorityAndPathAndQuery<>(scheme, authority, path, query);
        }

        @Override
        public String asString() {
            return scheme.asString() + "://" + authority.asString() + path.asString(NEVER_PREFIX_WITH_DOT_SEGMENT) + '?' + query.asString();
        }

        @Override
        public Path<SEGMENT> path() {
            return path;
        }

        @Override
        public boolean hasFragment() {
            return false;
        }

        @Override
        public FRAGMENT fragment() {
            throw new UnsupportedOperationException("Attempt to get fragment from a UrinReference that does not have one.");
        }

        @Override
        public boolean hasQuery() {
            return true;
        }

        @Override
        public QUERY query() {
            return query;
        }

        @Override
        public boolean hasAuthority() {
            return true;
        }

        @Override
        public Authority authority() {
            return authority;
        }

        @Override
        public Urin<SEGMENT, QUERY, FRAGMENT> resolve(final UrinReference<SEGMENT, QUERY, FRAGMENT> urinReference) {
            return urinReference.resolve(scheme, authority, path, query);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            UrinWithAuthorityAndPathAndQuery<?, ?, ?> that = (UrinWithAuthorityAndPathAndQuery<?, ?, ?>) o;

            return authority.equals(that.authority) && path.equals(that.path) && query.equals(that.query) && scheme.equals(that.scheme);

        }

        @Override
        public int hashCode() {
            int result = scheme.hashCode();
            result = 31 * result + authority.hashCode();
            result = 31 * result + path.hashCode();
            result = 31 * result + query.hashCode();
            return result;
        }
    }

    private static final class UrinWithAuthorityAndPathAndFragment<SEGMENT, QUERY extends Query<?>, FRAGMENT extends Fragment<?>> extends Urin<SEGMENT, QUERY, FRAGMENT> {
        private final Scheme<SEGMENT, QUERY, FRAGMENT> scheme;
        private final Authority authority;
        private final Path<SEGMENT> path;
        private final FRAGMENT fragment;

        UrinWithAuthorityAndPathAndFragment(final Scheme<SEGMENT, QUERY, FRAGMENT> scheme, final Authority authority, final Path<SEGMENT> path, final FRAGMENT fragment) {
            this.scheme = requireNonNull(scheme, "cannot instantiate Urin with null scheme");
            this.authority = requireNonNull(authority, "cannot instantiate Urin with null authority");
            this.path = requireNonNull(path, "cannot instantiate Urin with null path");
            this.fragment = requireNonNull(fragment, "cannot instantiate Urin with null fragment");
        }

        @Override
        public Urin<SEGMENT, QUERY, FRAGMENT> withPath(final AbsolutePath<SEGMENT> path) {
            return new UrinWithAuthorityAndPathAndFragment<>(scheme, authority, path, fragment);
        }

        @Override
        public String asString() {
            return scheme.asString() + "://" + authority.asString() + path.asString(NEVER_PREFIX_WITH_DOT_SEGMENT) + '#' + fragment.asString();
        }

        @Override
        public Path<SEGMENT> path() {
            return path;
        }

        @Override
        public boolean hasFragment() {
            return true;
        }

        @Override
        public FRAGMENT fragment() {
            return fragment;
        }

        @Override
        public boolean hasQuery() {
            return false;
        }

        @Override
        public QUERY query() {
            throw new UnsupportedOperationException("Attempt to get query from a UrinReference that does not have one.");
        }

        @Override
        public boolean hasAuthority() {
            return true;
        }

        @Override
        public Authority authority() {
            return authority;
        }

        @Override
        public Urin<SEGMENT, QUERY, FRAGMENT> resolve(final UrinReference<SEGMENT, QUERY, FRAGMENT> urinReference) {
            return urinReference.resolve(scheme, authority, path);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            UrinWithAuthorityAndPathAndFragment<?, ?, ?> that = (UrinWithAuthorityAndPathAndFragment<?, ?, ?>) o;

            return authority.equals(that.authority) && fragment.equals(that.fragment) && path.equals(that.path) && scheme.equals(that.scheme);

        }

        @Override
        public int hashCode() {
            int result = scheme.hashCode();
            result = 31 * result + authority.hashCode();
            result = 31 * result + path.hashCode();
            result = 31 * result + fragment.hashCode();
            return result;
        }
    }

    private static final class UrinWithPathAndFragment<SEGMENT, QUERY extends Query<?>, FRAGMENT extends Fragment<?>> extends Urin<SEGMENT, QUERY, FRAGMENT> {
        private final Scheme<SEGMENT, QUERY, FRAGMENT> scheme;
        private final Path<SEGMENT> path;
        private final FRAGMENT fragment;

        UrinWithPathAndFragment(final Scheme<SEGMENT, QUERY, FRAGMENT> scheme, final Path<SEGMENT> path, final FRAGMENT fragment) {
            this.scheme = requireNonNull(scheme, "cannot instantiate Urin with null scheme");
            this.path = requireNonNull(path, "cannot instantiate Urin with null path");
            this.fragment = requireNonNull(fragment, "cannot instantiate Urin with null fragment");
        }

        @Override
        public Urin<SEGMENT, QUERY, FRAGMENT> withPath(final AbsolutePath<SEGMENT> path) {
            return new UrinWithPathAndFragment<>(scheme, path, fragment);
        }

        @Override
        public String asString() {
            return scheme.asString() + ":" + path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY) + '#' + fragment.asString();
        }

        @Override
        public Path<SEGMENT> path() {
            return path;
        }

        @Override
        public boolean hasFragment() {
            return true;
        }

        @Override
        public FRAGMENT fragment() {
            return fragment;
        }

        @Override
        public boolean hasQuery() {
            return false;
        }

        @Override
        public QUERY query() {
            throw new UnsupportedOperationException("Attempt to get query from a UrinReference that does not have one.");
        }

        @Override
        public boolean hasAuthority() {
            return false;
        }

        @Override
        public Authority authority() {
            throw new UnsupportedOperationException("Attempt to get query from a UrinReference that does not have one.");
        }

        @Override
        public Urin<SEGMENT, QUERY, FRAGMENT> resolve(final UrinReference<SEGMENT, QUERY, FRAGMENT> urinReference) {
            return urinReference.resolve(scheme, path);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            UrinWithPathAndFragment<?, ?, ?> that = (UrinWithPathAndFragment<?, ?, ?>) o;

            return fragment.equals(that.fragment) && path.equals(that.path) && scheme.equals(that.scheme);

        }

        @Override
        public int hashCode() {
            int result = scheme.hashCode();
            result = 31 * result + path.hashCode();
            result = 31 * result + fragment.hashCode();
            return result;
        }
    }

    private static class UrinWithPath<SEGMENT, QUERY extends Query<?>, FRAGMENT extends Fragment<?>> extends Urin<SEGMENT, QUERY, FRAGMENT> {
        private final Scheme<SEGMENT, QUERY, FRAGMENT> scheme;
        private final Path<SEGMENT> path;

        UrinWithPath(final Scheme<SEGMENT, QUERY, FRAGMENT> scheme, final Path<SEGMENT> path) {
            this.scheme = requireNonNull(scheme, "cannot instantiate Urin with null scheme");
            this.path = requireNonNull(path, "cannot instantiate Urin with null path");
        }

        @Override
        public Urin<SEGMENT, QUERY, FRAGMENT> withPath(final AbsolutePath<SEGMENT> path) {
            return new UrinWithPath<>(scheme, path);
        }

        @Override
        public String asString() {
            return scheme.asString() + ':' + path.asString(PREFIX_WITH_DOT_SEGMENT_IF_FIRST_IS_EMPTY);
        }

        @Override
        public Path<SEGMENT> path() {
            return path;
        }

        @Override
        public boolean hasFragment() {
            return false;
        }

        @Override
        public FRAGMENT fragment() {
            throw new UnsupportedOperationException("Attempt to get fragment from a UrinReference that does not have one.");
        }

        @Override
        public boolean hasQuery() {
            return false;
        }

        @Override
        public QUERY query() {
            throw new UnsupportedOperationException("Attempt to get query from a UrinReference that does not have one.");
        }

        @Override
        public boolean hasAuthority() {
            return false;
        }

        @Override
        public Authority authority() {
            throw new UnsupportedOperationException("Attempt to get query from a UrinReference that does not have one.");
        }

        @Override
        public Urin<SEGMENT, QUERY, FRAGMENT> resolve(final UrinReference<SEGMENT, QUERY, FRAGMENT> urinReference) {
            return urinReference.resolve(scheme, path);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            UrinWithPath<?, ?, ?> that = (UrinWithPath<?, ?, ?>) o;

            return path.equals(that.path) && scheme.equals(that.scheme);

        }

        @Override
        public int hashCode() {
            int result = scheme.hashCode();
            result = 31 * result + path.hashCode();
            return result;
        }
    }

    private static class UrinWithAuthorityAndPath<SEGMENT, QUERY extends Query<?>, FRAGMENT extends Fragment<?>> extends Urin<SEGMENT, QUERY, FRAGMENT> {
        private final Scheme<SEGMENT, QUERY, FRAGMENT> scheme;
        private final Authority authority;
        private final Path<SEGMENT> path;

        UrinWithAuthorityAndPath(final Scheme<SEGMENT, QUERY, FRAGMENT> scheme, final Authority authority, final Path<SEGMENT> path) {
            this.scheme = requireNonNull(scheme, "cannot instantiate Urin with null scheme");
            this.authority = requireNonNull(authority, "cannot instantiate Urin with null authority");
            this.path = requireNonNull(path, "cannot instantiate Urin with null path");
        }

        @Override
        public Urin<SEGMENT, QUERY, FRAGMENT> withPath(final AbsolutePath<SEGMENT> path) {
            return new UrinWithAuthorityAndPath<>(scheme, authority, path);
        }

        @Override
        public String asString() {
            return scheme.asString() + "://" + authority.asString() + path.asString(NEVER_PREFIX_WITH_DOT_SEGMENT);
        }

        @Override
        public Path<SEGMENT> path() {
            return path;
        }

        @Override
        public boolean hasFragment() {
            return false;
        }

        @Override
        public FRAGMENT fragment() {
            throw new UnsupportedOperationException("Attempt to get fragment from a UrinReference that does not have one.");
        }

        @Override
        public boolean hasQuery() {
            return false;
        }

        @Override
        public QUERY query() {
            throw new UnsupportedOperationException("Attempt to get query from a UrinReference that does not have one.");
        }

        @Override
        public boolean hasAuthority() {
            return true;
        }

        @Override
        public Authority authority() {
            return authority;
        }

        @Override
        public Urin<SEGMENT, QUERY, FRAGMENT> resolve(final UrinReference<SEGMENT, QUERY, FRAGMENT> urinReference) {
            return urinReference.resolve(scheme, authority, path);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            UrinWithAuthorityAndPath<?, ?, ?> that = (UrinWithAuthorityAndPath<?, ?, ?>) o;

            return authority.equals(that.authority) && path.equals(that.path) && scheme.equals(that.scheme);

        }

        @Override
        public int hashCode() {
            int result = scheme.hashCode();
            result = 31 * result + authority.hashCode();
            result = 31 * result + path.hashCode();
            return result;
        }
    }
}
