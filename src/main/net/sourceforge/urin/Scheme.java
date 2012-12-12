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

import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Locale.ENGLISH;
import static net.sourceforge.urin.CharacterSetMembershipFunction.*;
import static net.sourceforge.urin.ExceptionFactory.ILLEGAL_ARGUMENT_EXCEPTION_EXCEPTION_FACTORY;
import static net.sourceforge.urin.ExceptionFactory.PARSE_EXCEPTION_EXCEPTION_FACTORY;
import static net.sourceforge.urin.Path.PrefixWithDotSegmentCriteria.*;
import static net.sourceforge.urin.Query.stringQueryMaker;
import static net.sourceforge.urin.Segment.stringSegmentMaker;

/**
 * A name component of a URI.
 * Immutable and threadsafe.
 *
 * @param <QUERY> The type of {@code Query} used by this scheme.
 * @see <a href="http://tools.ietf.org/html/rfc3986#section-3.1">RFC 3986 - Scheme</a>
 */
public abstract class Scheme<SEGMENT, QUERY extends Query> {

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

    protected final MakingDecoder<Segment<SEGMENT>, ?, String> segmentMakingDecoder;
    protected final MakingDecoder<QUERY, ?, String> queryMakingDecoder;

    Scheme(final MakingDecoder<Segment<SEGMENT>, ?, String> segmentMakingDecoder, final MakingDecoder<QUERY, ?, String> queryMakingDecoder) {
        this.segmentMakingDecoder = segmentMakingDecoder;
        this.queryMakingDecoder = queryMakingDecoder;
    }

    /**
     * Factory method for creating {@code Scheme}s.  Schemes must be at least one character long, and are permitted
     * to contain any character in the Latin alphabet, any digit, and any of the characters '+', '-', and '.'.
     *
     * @param name a {@code String} containing at least one character, made up of any characters in the Latin alphabet, the digits, and the characters '+', '-', and '.'.
     * @return a {@code Scheme} representing the given {@code String}.
     * @throws IllegalArgumentException if the given {@code String} is empty or contains characters not in the Latin alphabet, the digits, or the characters '+', '-', and '.'.
     */
    public static Scheme<String, Query<String>> scheme(final String name) {
        verify(name, ILLEGAL_ARGUMENT_EXCEPTION_EXCEPTION_FACTORY);
        return new GenericScheme<>(name.toLowerCase(ENGLISH), stringSegmentMaker(), stringQueryMaker());
    }

    /**
     * Factory method for creating {@code Scheme}s that have a default port.  Schemes must be at least one character long, and are permitted
     * to contain any character in the Latin alphabet, any digit, and any of the characters '+', '-', and '.'.
     * <p/>
     * An example of a name that defines a default port is http, which defaults to port 80, meaning {@code http://example.com} is equivalent to, and preferred to {@code http://example.com:80}.
     *
     * @param name        a {@code String} containing at least one character, made up of any characters in the Latin alphabet, the digits, and the characters '+', '-', and '.'.
     * @param defaultPort {@code Port} representing the default port for this name.
     * @return a {@code Scheme} representing the given {@code String}, using the given default port.
     * @throws IllegalArgumentException if the given {@code String} is empty or contains characters not in the Latin alphabet, the digits, or the characters '+', '-', and '.'.
     */
    public static Scheme<String, Query<String>> scheme(final String name, final Port defaultPort) {
        verify(name, ILLEGAL_ARGUMENT_EXCEPTION_EXCEPTION_FACTORY);
        return new SchemeWithDefaultPort<>(name.toLowerCase(ENGLISH), defaultPort, stringSegmentMaker(), stringQueryMaker());
    }

    private Scheme<SEGMENT, QUERY> parse(final String name) throws ParseException {
        verify(name, PARSE_EXCEPTION_EXCEPTION_FACTORY);
        return withName(name);
    }

    abstract Scheme<SEGMENT, QUERY> withName(String name);

    private static <T extends Exception> void verify(final String name, final ExceptionFactory<T> exceptionFactory) throws T {
        if (name.isEmpty()) {
            throw exceptionFactory.makeException("Scheme must contain at least one character");
        }
        CharacterSetMembershipFunction.verify(ALPHA, name, "scheme", 0, 1, exceptionFactory);
        CharacterSetMembershipFunction.verify(TRAILING_CHARACTER_MEMBERSHIP_FUNCTION, name, "scheme", 1, exceptionFactory);
    }

    abstract String asString();

    abstract Authority normalise(final Authority authority);

    abstract Scheme<SEGMENT, QUERY> removeDefaultPort();

    /**
     * Factory method for creating {@code RelativeReference}s with just an empty path.
     *
     * @return a {@code RelativeReference} with an empty path.
     */
    public final RelativeReference<SEGMENT, QUERY> relativeReference() {
        return new RelativeReferenceNoAuthority<>(new EmptyPath<SEGMENT>());
    }

    /**
     * Factory method for creating {@code RelativeReference}s with an authority and an empty path.
     *
     * @param authority any {@code Authority} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Authority} and an empty path.
     */
    public final RelativeReference<SEGMENT, QUERY> relativeReference(final Authority authority) {
        return new RelativeReferenceWithAuthority<>(authority, new EmptyPath<SEGMENT>());
    }

    /**
     * Factory method for creating {@code RelativeReference}s with just a path.
     *
     * @param path any {@code Path} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Path}.
     */
    public final RelativeReference<SEGMENT, QUERY> relativeReference(final Path<SEGMENT> path) {
        return new RelativeReferenceNoAuthority<>(path);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with an authority and a path.
     *
     * @param authority any {@code Authority} to use in this {@code RelativeReference}.
     * @param path      any {@code AbsolutePath} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Authority} and {@code Path}.
     */
    public final RelativeReference<SEGMENT, QUERY> relativeReference(final Authority authority, final AbsolutePath<SEGMENT> path) {
        return new RelativeReferenceWithAuthority<>(authority, path);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with a query and an empty path.
     *
     * @param query any {@code Query} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Query} and an empty path.
     */
    public final RelativeReference<SEGMENT, QUERY> relativeReference(final QUERY query) {
        return new RelativeReferenceNoAuthorityWithQuery<>(new EmptyPath<SEGMENT>(), query);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with an authority, a query and an empty path.
     *
     * @param authority any {@code Authority} to use in this {@code RelativeReference}.
     * @param query     any {@code Query} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Authority} and {@code Query}, and an empty path.
     */
    public final RelativeReference<SEGMENT, QUERY> relativeReference(final Authority authority, final QUERY query) {
        return new RelativeReferenceWithAuthorityAndQuery<>(authority, new EmptyPath<SEGMENT>(), query);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with a path and a query.
     *
     * @param path  any {@code Path} to use in this {@code RelativeReference}.
     * @param query any {@code Query} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Path} and {@code Query}.
     */
    public final RelativeReference<SEGMENT, QUERY> relativeReference(final Path<SEGMENT> path, final QUERY query) {
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
    public final RelativeReference<SEGMENT, QUERY> relativeReference(final Authority authority, final AbsolutePath<SEGMENT> path, final QUERY query) {
        return new RelativeReferenceWithAuthorityAndQuery<>(authority, path, query);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with a fragment and an empty path.
     *
     * @param fragment any {@code Fragment} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Fragment} and an empty path.
     */
    public final RelativeReference<SEGMENT, QUERY> relativeReference(final Fragment fragment) {
        return new RelativeReferenceNoAuthorityWithFragment<>(new EmptyPath<SEGMENT>(), fragment);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with an authority, a fragment, and an empty path.
     *
     * @param authority any {@code Authority} to use in this {@code RelativeReference}.
     * @param fragment  any {@code Fragment} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Authority} and {@code Fragment}, and an empty path.
     */
    public final RelativeReference<SEGMENT, QUERY> relativeReference(final Authority authority, final Fragment fragment) {
        return new RelativeReferenceWithAuthorityAndFragment<>(authority, new EmptyPath<SEGMENT>(), fragment);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with a path and a fragment.
     *
     * @param path     any {@code Path} to use in this {@code RelativeReference}.
     * @param fragment any {@code Fragment} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Path} and {@code Fragment}.
     */
    public final RelativeReference<SEGMENT, QUERY> relativeReference(final Path<SEGMENT> path, final Fragment fragment) {
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
    public final RelativeReference<SEGMENT, QUERY> relativeReference(final Authority authority, final AbsolutePath<SEGMENT> path, final Fragment fragment) {
        return new RelativeReferenceWithAuthorityAndFragment<>(authority, path, fragment);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with a query, a fragment, and an empty path.
     *
     * @param query    any {@code Query} to use in this {@code RelativeReference}.
     * @param fragment any {@code Fragment} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Query} and {@code Fragment}, and an empty path.
     */
    public final RelativeReference<SEGMENT, QUERY> relativeReference(final QUERY query, final Fragment fragment) {
        return new RelativeReferenceNoAuthorityWithQueryAndFragment<>(new EmptyPath<SEGMENT>(), query, fragment);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with an authority, a query, a fragment, and an empty path.
     *
     * @param authority any {@code Authority} to use in this {@code RelativeReference}.
     * @param query     any {@code Query} to use in this {@code RelativeReference}.
     * @param fragment  any {@code Fragment} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Authority}, {@code Query} and {@code Fragment}, and an empty path.
     */
    public final RelativeReference<SEGMENT, QUERY> relativeReference(final Authority authority, final QUERY query, final Fragment fragment) {
        return new RelativeReferenceWithAuthorityAndQueryAndFragment<>(authority, new EmptyPath<SEGMENT>(), query, fragment);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with a path, a query, and a fragment.
     *
     * @param path     any {@code Path} to use in this {@code RelativeReference}.
     * @param query    any {@code Query} to use in this {@code RelativeReference}.
     * @param fragment any {@code Fragment} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Path}, {@code Query} and {@code Fragment}.
     */
    public final RelativeReference<SEGMENT, QUERY> relativeReference(final Path<SEGMENT> path, final QUERY query, final Fragment fragment) {
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
    public final RelativeReference<SEGMENT, QUERY> relativeReference(final Authority authority, final AbsolutePath<SEGMENT> path, final QUERY query, final Fragment fragment) {
        return new RelativeReferenceWithAuthorityAndQueryAndFragment<>(authority, path, query, fragment);
    }

    /**
     * Parses the given {@code String} as a relative reference.
     *
     * @param relativeReferenceString a {@code String} that represents a relative reference.
     * @return a {@code UrinReference} representing the relative reference represented by the given {@code String}.
     * @throws ParseException if the given {@code String} is not a valid relative reference.
     */
    public final RelativeReference<SEGMENT, QUERY> parseRelativeReference(final String relativeReferenceString) throws ParseException {
        final Matcher matcher = RELATIVE_REFERENCE_PATTERN.matcher(relativeReferenceString);
        matcher.matches();
        final String queryString = matcher.group(6);
        final String fragment = matcher.group(8);
        final RelativeReference<SEGMENT, QUERY> result;
        final String authorityString = matcher.group(3);
        final String pathString = matcher.group(4);
        if (authorityString == null) {
            if (pathString == null || "".equals(pathString)) {
                if (queryString == null) {
                    if (fragment == null) {
                        result = relativeReference();
                    } else {
                        result = relativeReference(Fragment.parse(fragment));
                    }
                } else {
                    final QUERY query = Query.parseQuery(queryString, queryMakingDecoder);
                    if (fragment == null) {
                        result = relativeReference(query);
                    } else {
                        result = relativeReference(query, Fragment.parse(fragment));
                    }
                }
            } else {
                final Path<SEGMENT> path = !pathString.startsWith("/") ? Path.parseRootlessPath(pathString, segmentMakingDecoder) : Path.parsePath(pathString, segmentMakingDecoder);
                if (queryString == null) {
                    if (fragment == null) {
                        result = relativeReference(path);
                    } else {
                        result = relativeReference(path, Fragment.parse(fragment));
                    }
                } else {
                    final QUERY query = Query.parseQuery(queryString, queryMakingDecoder);
                    if (fragment == null) {
                        result = relativeReference(path, query);
                    } else {
                        result = relativeReference(path, query, Fragment.parse(fragment));
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
                        result = relativeReference(authority, Fragment.parse(fragment));
                    }
                } else {
                    final QUERY query = Query.parseQuery(queryString, queryMakingDecoder);
                    if (fragment == null) {
                        result = relativeReference(authority, query);
                    } else {
                        result = relativeReference(authority, query, Fragment.parse(fragment));
                    }
                }
            } else {
                final AbsolutePath<SEGMENT> path = Path.parsePath(pathString, segmentMakingDecoder);
                if (queryString == null) {
                    if (fragment == null) {
                        result = relativeReference(authority, path);
                    } else {
                        result = relativeReference(authority, path, Fragment.parse(fragment));
                    }
                } else {
                    final QUERY query = Query.parseQuery(queryString, queryMakingDecoder);
                    if (fragment == null) {
                        result = relativeReference(authority, path, query);
                    } else {
                        result = relativeReference(authority, path, query, Fragment.parse(fragment));
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
    public final RelativeReference<SEGMENT, QUERY> parseRelativeReference(URI uri) throws ParseException {
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
    public final Urin<SEGMENT, QUERY> urin() {
        return new UrinWithPath<>(removeDefaultPort(), new EmptyPath<SEGMENT>());
    }

    /**
     * Factory method for creating {@code Urin}s with just scheme and path components.
     *
     * @param path any {@code Path} to use in this {@code Urin}.
     * @return a {@code Urin} with the given {@code Scheme} and {@code Path}.
     */
    public final Urin<SEGMENT, QUERY> urin(final Path<SEGMENT> path) {
        return new UrinWithPath<>(removeDefaultPort(), path);
    }

    /**
     * Factory method for creating {@code Urin}s with just scheme, authority, and empty path components.
     *
     * @param authority any {@code Authority} to use in this {@code Urin}.
     * @return a {@code Urin} with the given {@code Scheme} and {@code Authority}, and an empty path.
     */
    public final Urin<SEGMENT, QUERY> urin(final Authority authority) {
        return new UrinWithAuthorityAndPath<>(removeDefaultPort(), normalise(authority), new EmptyPath<SEGMENT>());
    }

    /**
     * Factory method for creating {@code Urin}s with just scheme, authority, and path components.
     *
     * @param authority any {@code Authority} to use in this {@code Urin}.
     * @param path      any {@code AbsolutePath} to use in this {@code Urin}.
     * @return a {@code Urin} with the given {@code Scheme}, {@code Authority}, and {@code AbsolutePath}.
     */
    public final Urin<SEGMENT, QUERY> urin(final Authority authority, final AbsolutePath<SEGMENT> path) {
        return new UrinWithAuthorityAndPath<>(removeDefaultPort(), normalise(authority), path);
    }

    /**
     * Factory method for creating {@code Urin}s with just a scheme, fragment, and empty path.
     *
     * @param fragment any {@code Fragment} to use in this {@code Urin}.
     * @return a {@code Urin} with the given {@code Scheme}, the given {@code Fragment}, and an empty path.
     */
    public final Urin<SEGMENT, QUERY> urin(final Fragment fragment) {
        return new UrinWithPathAndFragment<>(removeDefaultPort(), new EmptyPath<SEGMENT>(), fragment);
    }

    /**
     * Factory method for creating {@code Urin}s with just scheme, path, and fragment components.
     *
     * @param path     any {@code Path} to use in this {@code Urin}.
     * @param fragment any {@code Fragment} to use in this {@code Urin}.
     * @return a {@code Urin} with the given {@code Scheme}, {@code Fragment}, and {@code Path}.
     */
    public final Urin<SEGMENT, QUERY> urin(final Path<SEGMENT> path, final Fragment fragment) {
        return new UrinWithPathAndFragment<>(removeDefaultPort(), path, fragment);
    }

    /**
     * Factory method for creating {@code Urin}s with just scheme, authority, fragment, and empty path components.
     *
     * @param authority any {@code Authority} to use in this {@code Urin}.
     * @param fragment  any {@code Fragment} to use in this {@code Urin}.
     * @return a {@code Urin} with the given {@code Scheme}, {@code Authority}, and {@code Fragment}, and an empty path.
     */
    public final Urin<SEGMENT, QUERY> urin(final Authority authority, final Fragment fragment) {
        return new UrinWithAuthorityAndPathAndFragment<>(removeDefaultPort(), normalise(authority), new EmptyPath<SEGMENT>(), fragment);
    }

    /**
     * Factory method for creating {@code Urin}s with just scheme, authority, path, and fragment components.
     *
     * @param authority any {@code Authority} to use in this {@code Urin}.
     * @param path      any {@code AbsolutePath} to use in this {@code Urin}.
     * @param fragment  any {@code Fragment} to use in this {@code Urin}.
     * @return a {@code Urin} with the given {@code Scheme}, {@code Authority}, {@code AbsolutePath}, and {@code Fragment}.
     */
    public final Urin<SEGMENT, QUERY> urin(final Authority authority, final AbsolutePath<SEGMENT> path, final Fragment fragment) {
        return new UrinWithAuthorityAndPathAndFragment<>(removeDefaultPort(), normalise(authority), path, fragment);
    }

    /**
     * Factory method for creating {@code Urin}s with just a scheme, query, and empty path.
     *
     * @param query any {@code Query} to use in this {@code Urin}.
     * @return a {@code Urin} with the given {@code Scheme}, the given {@code Query}, and an empty path.
     */
    public final Urin<SEGMENT, QUERY> urin(final QUERY query) {
        return new UrinWithPathAndQuery<>(removeDefaultPort(), new EmptyPath<SEGMENT>(), query);
    }

    /**
     * Factory method for creating {@code Urin}s with just scheme, path, and query components.
     *
     * @param path  any {@code Path} to use in this {@code Urin}.
     * @param query any {@code Query} to use in this {@code Urin}.
     * @return a {@code Urin} with the given {@code Scheme}, {@code Query}, and {@code Path}.
     */
    public final Urin<SEGMENT, QUERY> urin(final Path<SEGMENT> path, final QUERY query) {
        return new UrinWithPathAndQuery<>(removeDefaultPort(), path, query);
    }

    /**
     * Factory method for creating {@code Urin}s with just scheme, authority, query, and empty path components.
     *
     * @param authority any {@code Authority} to use in this {@code Urin}.
     * @param query     any {@code Query} to use in this {@code Urin}.
     * @return a {@code Urin} with the given {@code Scheme}, {@code Authority}, and {@code Query}, and an empty path.
     */
    public final Urin<SEGMENT, QUERY> urin(final Authority authority, final QUERY query) {
        return new UrinWithAuthorityAndPathAndQuery<>(removeDefaultPort(), normalise(authority), new EmptyPath<SEGMENT>(), query);
    }

    /**
     * Factory method for creating {@code Urin}s with just scheme, authority, path, and query components.
     *
     * @param authority any {@code Authority} to use in this {@code Urin}.
     * @param path      any {@code AbsolutePath} to use in this {@code Urin}.
     * @param query     any {@code Query} to use in this {@code Urin}.
     * @return a {@code Urin} with the given {@code Scheme}, {@code Authority}, {@code AbsolutePath}, and {@code Query}.
     */
    public final Urin<SEGMENT, QUERY> urin(final Authority authority, final AbsolutePath<SEGMENT> path, final QUERY query) {
        return new UrinWithAuthorityAndPathAndQuery<>(removeDefaultPort(), normalise(authority), path, query);
    }

    /**
     * Factory method for creating {@code Urin}s with scheme, empty path, query, and fragment components.
     *
     * @param query    any {@code Query} to use in this {@code Urin}.
     * @param fragment any {@code Fragment} to use in this {@code Urin}.
     * @return a {@code Urin} with the given {@code Scheme}, {@code Query}, and {@code Fragment}, and an empty path.
     */
    public final Urin<SEGMENT, QUERY> urin(final QUERY query, final Fragment fragment) {
        return new UrinWithPathAndQueryAndFragment<>(removeDefaultPort(), new EmptyPath<SEGMENT>(), query, fragment);
    }

    /**
     * Factory method for creating {@code Urin}s with scheme, path, query, and fragment components.
     *
     * @param path     any {@code Path} to use in this {@code Urin}.
     * @param query    any {@code Query} to use in this {@code Urin}.
     * @param fragment any {@code Fragment} to use in this {@code Urin}.
     * @return a {@code Urin} with the given {@code Scheme}, {@code Path}, {@code Query}, and {@code Fragment}.
     */
    public final Urin<SEGMENT, QUERY> urin(final Path<SEGMENT> path, final QUERY query, final Fragment fragment) {
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
    public final Urin<SEGMENT, QUERY> urin(final Authority authority, final QUERY query, final Fragment fragment) {
        return new UrinWithAuthorityAndPathAndQueryAndFragment<>(removeDefaultPort(), normalise(authority), new EmptyPath<SEGMENT>(), query, fragment);
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
    public final Urin<SEGMENT, QUERY> urin(final Authority authority, final AbsolutePath<SEGMENT> path, final QUERY query, final Fragment fragment) {
        return new UrinWithAuthorityAndPathAndQueryAndFragment<>(removeDefaultPort(), normalise(authority), path, query, fragment);
    }

    /**
     * Parses the given {@code String} as a URI.
     *
     * @param uriString a {@code String} that represents a URI.
     * @return a {@code Urin} representing the URI represented by the given {@code String}.
     * @throws ParseException if the given {@code String} is not a valid URI.
     */
    public final Urin<SEGMENT, QUERY> parseUrin(final String uriString) throws ParseException {
        final Matcher matcher = URI_PATTERN.matcher(uriString);
        matcher.matches();
        final Scheme<SEGMENT, QUERY> scheme = parse(matcher.group(2));
        final String authorityString = matcher.group(5);
        final String pathString = matcher.group(6);
        final String queryString = matcher.group(8);
        final String fragmentString = matcher.group(10);
        final Urin<SEGMENT, QUERY> result;
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
                            Fragment.parse(fragmentString)
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
                            Fragment.parse(fragmentString)
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
                    final Fragment fragment = Fragment.parse(fragmentString);
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
                    final Fragment fragment = Fragment.parse(fragmentString);
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
    public final Urin<SEGMENT, QUERY> parseUrin(final URI uri) throws ParseException {
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
    public final UrinReference<SEGMENT, QUERY> parseUrinReference(final String uriReferenceString) throws ParseException {
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
    public final UrinReference<SEGMENT, QUERY> parseUrinReference(final URI uriReference) throws ParseException {
        return parseUrinReference(uriReference.toASCIIString());
    }

    static final class GenericScheme<SEGMENT, QUERY extends Query> extends Scheme<SEGMENT, QUERY> {
        private final String name;

        public GenericScheme(String name, MakingDecoder<Segment<SEGMENT>, ?, String> segmentMakingDecoder, MakingDecoder<QUERY, ?, String> queryMakingDecoder) {
            super(segmentMakingDecoder, queryMakingDecoder);
            this.name = name;
        }

        @Override
        GenericScheme<SEGMENT, QUERY> withName(final String name) {
            return new GenericScheme<>(name, segmentMakingDecoder, queryMakingDecoder);
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
        Scheme<SEGMENT, QUERY> removeDefaultPort() {
            return this;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            GenericScheme that = (GenericScheme) o;

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

    private static final class RelativeReferenceNoAuthority<SEGMENT, Q extends Query> extends RelativeReference<SEGMENT, Q> {
        private final Path<SEGMENT> path;

        private RelativeReferenceNoAuthority(final Path<SEGMENT> path) {
            if (path == null) {
                throw new NullPointerException("Cannot instantiate RelativeReference with null path");
            }
            this.path = path;
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
        public Fragment fragment() {
            throw new UnsupportedOperationException("Attempt to get fragment from a UrinReference that does not have one.");
        }

        @Override
        public boolean hasQuery() {
            return false;
        }

        @Override
        public Q query() {
            throw new UnsupportedOperationException("Attempt to get query from a UrinReference that does not have one.");
        }

        @Override
        Urin<SEGMENT, Q> resolve(final Scheme<SEGMENT, Q> scheme, final Path<SEGMENT> path) {
            return scheme.urin(path.resolveRelativeTo(this.path));
        }

        @Override
        Urin<SEGMENT, Q> resolve(final Scheme<SEGMENT, Q> scheme, final Authority authority, final Path<SEGMENT> path) {
            return new UrinWithAuthorityAndPath<>(scheme.removeDefaultPort(), scheme.normalise(authority), this.path.resolveRelativeTo(path));
        }

        @Override
        Urin<SEGMENT, Q> resolve(final Scheme<SEGMENT, Q> scheme, final Path<SEGMENT> path, final Q query) {
            if (this.path.isEmpty()) {
                return scheme.urin(path.resolveRelativeTo(this.path), query);
            } else {
                return scheme.urin(path.resolveRelativeTo(this.path));
            }
        }

        @Override
        Urin<SEGMENT, Q> resolve(final Scheme<SEGMENT, Q> scheme, final Authority authority, final Path<SEGMENT> path, final Q query) {
            if (this.path.isEmpty()) {
                return new UrinWithAuthorityAndPathAndQuery<>(scheme.removeDefaultPort(), scheme.normalise(authority), this.path.resolveRelativeTo(path), query);
            } else {
                return new UrinWithAuthorityAndPath<>(scheme.removeDefaultPort(), scheme.normalise(authority), this.path.resolveRelativeTo(path));
            }
        }

        @Override
        Urin<SEGMENT, Q> resolve(final Scheme<SEGMENT, Q> scheme, final Path<SEGMENT> path, final Q query, final Fragment fragment) {
            if (this.path.isEmpty()) {
                return scheme.urin(this.path.resolveRelativeTo(path), query, fragment);
            } else {
                return scheme.urin(this.path.resolveRelativeTo(path));
            }
        }

        @Override
        Urin<SEGMENT, Q> resolve(final Scheme<SEGMENT, Q> scheme, final Authority authority, final Path<SEGMENT> path, final Q query, final Fragment fragment) {
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

            RelativeReferenceNoAuthority that = (RelativeReferenceNoAuthority) o;
            return path.equals(that.path);
        }

        @Override
        public int hashCode() {
            return path.hashCode();
        }

        @Override
        public String toString() {
            return "RelativeReference{" +
                    "path=" + path +
                    '}';
        }
    }

    private static final class RelativeReferenceWithAuthority<SEGMENT, Q extends Query> extends RelativeReference<SEGMENT, Q> {
        private final Authority authority;
        private final Path<SEGMENT> path;

        private RelativeReferenceWithAuthority(final Authority authority, final Path<SEGMENT> path) {
            if (authority == null) {
                throw new NullPointerException("Cannot instantiate RelativeReference with null authority");
            }
            this.authority = authority;
            if (path == null) {
                throw new NullPointerException("Cannot instantiate RelativeReference with null path");
            }
            this.path = path;
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
        public Fragment fragment() {
            throw new UnsupportedOperationException("Attempt to get fragment from a UrinReference that does not have one.");
        }

        @Override
        public boolean hasQuery() {
            return false;
        }

        @Override
        public Q query() {
            throw new UnsupportedOperationException("Attempt to get query from a UrinReference that does not have one.");
        }

        @Override
        Urin<SEGMENT, Q> resolve(final Scheme<SEGMENT, Q> scheme, final Path<SEGMENT> path) {
            return new UrinWithAuthorityAndPath<>(scheme.removeDefaultPort(), scheme.normalise(authority), this.path);
        }

        @Override
        Urin<SEGMENT, Q> resolve(final Scheme<SEGMENT, Q> scheme, final Authority authority, final Path<SEGMENT> path) {
            return new UrinWithAuthorityAndPath<>(scheme.removeDefaultPort(), scheme.normalise(this.authority), this.path);
        }

        @Override
        Urin<SEGMENT, Q> resolve(final Scheme<SEGMENT, Q> scheme, final Path<SEGMENT> path, final Q query) {
            return new UrinWithAuthorityAndPath<>(scheme.removeDefaultPort(), scheme.normalise(authority), this.path);
        }

        @Override
        Urin<SEGMENT, Q> resolve(final Scheme<SEGMENT, Q> scheme, final Authority authority, final Path<SEGMENT> path, final Q query) {
            return new UrinWithAuthorityAndPath<>(scheme.removeDefaultPort(), scheme.normalise(this.authority), this.path);
        }

        @Override
        Urin<SEGMENT, Q> resolve(final Scheme<SEGMENT, Q> scheme, final Path<SEGMENT> path, final Q query, final Fragment fragment) {
            return new UrinWithAuthorityAndPath<>(scheme.removeDefaultPort(), scheme.normalise(authority), this.path);
        }

        @Override
        Urin<SEGMENT, Q> resolve(final Scheme<SEGMENT, Q> scheme, final Authority authority, final Path<SEGMENT> path, final Q query, final Fragment fragment) {
            return new UrinWithAuthorityAndPath<>(scheme.removeDefaultPort(), scheme.normalise(this.authority), this.path);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            RelativeReferenceWithAuthority that = (RelativeReferenceWithAuthority) o;
            return authority.equals(that.authority)
                    && path.equals(that.path);
        }

        @Override
        public int hashCode() {
            int result = authority.hashCode();
            result = 31 * result + path.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "RelativeReference{" +
                    "authority=" + authority +
                    ", path=" + path +
                    '}';
        }
    }

    private static final class RelativeReferenceNoAuthorityWithQuery<SEGMENT, Q extends Query> extends RelativeReference<SEGMENT, Q> {
        private final Path<SEGMENT> path;
        private final Q query;

        RelativeReferenceNoAuthorityWithQuery(final Path<SEGMENT> path, final Q query) {
            if (path == null) {
                throw new NullPointerException("Cannot instantiate RelativeReference with null path");
            }
            this.path = path;
            if (query == null) {
                throw new NullPointerException("Cannot instantiate RelativeReference with null query");
            }
            this.query = query;
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
        public Fragment fragment() {
            throw new UnsupportedOperationException("Attempt to get fragment from a UrinReference that does not have one.");
        }

        @Override
        public boolean hasQuery() {
            return true;
        }

        @Override
        public Q query() {
            return query;
        }

        @Override
        Urin<SEGMENT, Q> resolve(final Scheme<SEGMENT, Q> scheme, final Path<SEGMENT> path) {
            return scheme.urin(this.path.resolveRelativeTo(path), query);
        }

        @Override
        Urin<SEGMENT, Q> resolve(final Scheme<SEGMENT, Q> scheme, final Authority authority, final Path<SEGMENT> path) {
            return new UrinWithAuthorityAndPathAndQuery<>(scheme.removeDefaultPort(), scheme.normalise(authority), this.path.resolveRelativeTo(path), query);
        }

        @Override
        Urin<SEGMENT, Q> resolve(final Scheme<SEGMENT, Q> scheme, final Path<SEGMENT> path, final Q query) {
            return scheme.urin(this.path.resolveRelativeTo(path), this.query);
        }

        @Override
        Urin<SEGMENT, Q> resolve(final Scheme<SEGMENT, Q> scheme, final Authority authority, final Path<SEGMENT> path, final Q query) {
            return new UrinWithAuthorityAndPathAndQuery<>(scheme.removeDefaultPort(), scheme.normalise(authority), this.path.resolveRelativeTo(path), this.query);
        }

        @Override
        Urin<SEGMENT, Q> resolve(final Scheme<SEGMENT, Q> scheme, final Path<SEGMENT> path, final Q query, final Fragment fragment) {
            if (this.path.isEmpty()) {
                return scheme.urin(this.path.resolveRelativeTo(path), this.query, fragment);
            } else {
                return scheme.urin(this.path.resolveRelativeTo(path), this.query);
            }
        }

        @Override
        Urin<SEGMENT, Q> resolve(final Scheme<SEGMENT, Q> scheme, final Authority authority, final Path<SEGMENT> path, final Q query, final Fragment fragment) {
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

            RelativeReferenceNoAuthorityWithQuery that = (RelativeReferenceNoAuthorityWithQuery) o;

            return query.equals(that.query) && path.equals(that.path);

        }

        @Override
        public int hashCode() {
            int result = path.hashCode();
            result = 31 * result + query.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "RelativeReference{" +
                    "path=" + path +
                    ", query=" + query +
                    '}';
        }
    }

    private static final class RelativeReferenceNoAuthorityWithFragment<SEGMENT, Q extends Query> extends RelativeReference<SEGMENT, Q> {
        private final Path<SEGMENT> path;
        private final Fragment fragment;

        RelativeReferenceNoAuthorityWithFragment(final Path<SEGMENT> path, final Fragment fragment) {
            if (path == null) {
                throw new NullPointerException("Cannot instantiate RelativeReference with null path");
            }
            this.path = path;
            if (fragment == null) {
                throw new NullPointerException("Cannot instantiate RelativeReference with null fragment");
            }
            this.fragment = fragment;
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
        public Fragment fragment() {
            return fragment;
        }

        @Override
        public boolean hasQuery() {
            return false;
        }

        @Override
        public Q query() {
            throw new UnsupportedOperationException("Attempt to get query from a UrinReference that does not have one.");
        }

        @Override
        Urin<SEGMENT, Q> resolve(final Scheme<SEGMENT, Q> scheme, final Path<SEGMENT> path) {
            return scheme.urin(this.path.resolveRelativeTo(path), fragment);
        }

        @Override
        Urin<SEGMENT, Q> resolve(final Scheme<SEGMENT, Q> scheme, final Authority authority, final Path<SEGMENT> path) {
            return new UrinWithAuthorityAndPathAndFragment<>(scheme.removeDefaultPort(), scheme.normalise(authority), this.path.resolveRelativeTo(path), fragment);
        }

        @Override
        Urin<SEGMENT, Q> resolve(final Scheme<SEGMENT, Q> scheme, final Path<SEGMENT> path, final Q query) {
            if (this.path.isEmpty()) {
                return scheme.urin(this.path.resolveRelativeTo(path), query, fragment);
            } else {
                return scheme.urin(this.path.resolveRelativeTo(path), fragment);
            }
        }

        @Override
        Urin<SEGMENT, Q> resolve(final Scheme<SEGMENT, Q> scheme, final Authority authority, final Path<SEGMENT> path, final Q query) {
            if (this.path.isEmpty()) {
                return new UrinWithAuthorityAndPathAndQueryAndFragment<>(scheme.removeDefaultPort(), scheme.normalise(authority), this.path.resolveRelativeTo(path), query, fragment);
            } else {
                return new UrinWithAuthorityAndPathAndFragment<>(scheme.removeDefaultPort(), scheme.normalise(authority), this.path.resolveRelativeTo(path), fragment);
            }
        }

        @Override
        Urin<SEGMENT, Q> resolve(final Scheme<SEGMENT, Q> scheme, final Path<SEGMENT> path, final Q query, final Fragment fragment) {
            if (this.path.isEmpty()) {
                return scheme.urin(this.path.resolveRelativeTo(path), query, this.fragment);
            } else {
                return scheme.urin(this.path.resolveRelativeTo(path), this.fragment);
            }
        }

        @Override
        Urin<SEGMENT, Q> resolve(final Scheme<SEGMENT, Q> scheme, final Authority authority, final Path<SEGMENT> path, final Q query, final Fragment fragment) {
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

            RelativeReferenceNoAuthorityWithFragment that = (RelativeReferenceNoAuthorityWithFragment) o;

            return fragment.equals(that.fragment) && path.equals(that.path);

        }

        @Override
        public int hashCode() {
            int result = path.hashCode();
            result = 31 * result + fragment.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "RelativeReference{" +
                    "path=" + path +
                    ", fragment=" + fragment +
                    '}';
        }
    }

    private static final class RelativeReferenceNoAuthorityWithQueryAndFragment<SEGMENT, Q extends Query> extends RelativeReference<SEGMENT, Q> {
        private final Path<SEGMENT> path;
        private final Fragment fragment;
        private final Q query;

        RelativeReferenceNoAuthorityWithQueryAndFragment(final Path<SEGMENT> path, final Q query, final Fragment fragment) {
            if (path == null) {
                throw new NullPointerException("Cannot instantiate RelativeReference with null path");
            }
            this.path = path;
            if (query == null) {
                throw new NullPointerException("Cannot instantiate RelativeReference with null query");
            }
            this.query = query;
            if (fragment == null) {
                throw new NullPointerException("Cannot instantiate RelativeReference with null fragment");
            }
            this.fragment = fragment;
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
        public Fragment fragment() {
            return fragment;
        }

        @Override
        public boolean hasQuery() {
            return true;
        }

        @Override
        public Q query() {
            return query;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            RelativeReferenceNoAuthorityWithQueryAndFragment that = (RelativeReferenceNoAuthorityWithQueryAndFragment) o;

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
        Urin<SEGMENT, Q> resolve(final Scheme<SEGMENT, Q> scheme, final Path<SEGMENT> path) {
            return scheme.urin(this.path.resolveRelativeTo(path), query, fragment);
        }

        @Override
        Urin<SEGMENT, Q> resolve(final Scheme<SEGMENT, Q> scheme, final Authority authority, final Path<SEGMENT> path) {
            return new UrinWithAuthorityAndPathAndQueryAndFragment<>(scheme.removeDefaultPort(), scheme.normalise(authority), this.path.resolveRelativeTo(path), query, fragment);
        }

        @Override
        Urin<SEGMENT, Q> resolve(final Scheme<SEGMENT, Q> scheme, final Path<SEGMENT> path, final Q query) {
            return scheme.urin(this.path.resolveRelativeTo(path), this.query, fragment);
        }

        @Override
        Urin<SEGMENT, Q> resolve(final Scheme<SEGMENT, Q> scheme, final Authority authority, final Path<SEGMENT> path, final Q query) {
            return new UrinWithAuthorityAndPathAndQueryAndFragment<>(scheme.removeDefaultPort(), scheme.normalise(authority), this.path.resolveRelativeTo(path), this.query, fragment);
        }

        @Override
        Urin<SEGMENT, Q> resolve(final Scheme<SEGMENT, Q> scheme, final Path<SEGMENT> path, final Q query, final Fragment fragment) {
            return scheme.urin(this.path.resolveRelativeTo(path), this.query, this.fragment);
        }

        @Override
        Urin<SEGMENT, Q> resolve(final Scheme<SEGMENT, Q> scheme, final Authority authority, final Path<SEGMENT> path, final Q query, final Fragment fragment) {
            return new UrinWithAuthorityAndPathAndQueryAndFragment<>(scheme.removeDefaultPort(), scheme.normalise(authority), this.path.resolveRelativeTo(path), this.query, this.fragment);
        }

        @Override
        public String toString() {
            return "RelativeReference{" +
                    "path=" + path +
                    ", query=" + query +
                    ", fragment=" + fragment +
                    '}';
        }
    }

    private static final class RelativeReferenceWithAuthorityAndQuery<SEGMENT, Q extends Query> extends RelativeReference<SEGMENT, Q> {
        private final Authority authority;
        private final Path<SEGMENT> path;
        private final Q query;

        RelativeReferenceWithAuthorityAndQuery(final Authority authority, final Path<SEGMENT> path, final Q query) {
            if (authority == null) {
                throw new NullPointerException("Cannot instantiate RelativeReference with null authority");
            }
            this.authority = authority;
            if (path == null) {
                throw new NullPointerException("Cannot instantiate RelativeReference with null path");
            }
            this.path = path;
            if (query == null) {
                throw new NullPointerException("Cannot instantiate RelativeReference with null query");
            }
            this.query = query;
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
        public Fragment fragment() {
            throw new UnsupportedOperationException("Attempt to get fragment from a UrinReference that does not have one.");
        }

        @Override
        public boolean hasQuery() {
            return true;
        }

        @Override
        public Q query() {
            return query;
        }

        @Override
        Urin<SEGMENT, Q> resolve(final Scheme<SEGMENT, Q> scheme, final Path<SEGMENT> path) {
            return new UrinWithAuthorityAndPathAndQuery<>(scheme.removeDefaultPort(), scheme.normalise(authority), this.path, query);
        }

        @Override
        Urin<SEGMENT, Q> resolve(final Scheme<SEGMENT, Q> scheme, final Authority authority, final Path<SEGMENT> path) {
            return new UrinWithAuthorityAndPathAndQuery<>(scheme.removeDefaultPort(), scheme.normalise(this.authority), this.path, query);
        }

        @Override
        Urin<SEGMENT, Q> resolve(final Scheme<SEGMENT, Q> scheme, final Path<SEGMENT> path, final Q query) {
            return new UrinWithAuthorityAndPathAndQuery<>(scheme.removeDefaultPort(), scheme.normalise(authority), this.path, this.query);
        }

        @Override
        Urin<SEGMENT, Q> resolve(final Scheme<SEGMENT, Q> scheme, final Authority authority, final Path<SEGMENT> path, final Q query) {
            return new UrinWithAuthorityAndPathAndQuery<>(scheme.removeDefaultPort(), scheme.normalise(this.authority), this.path, this.query);
        }

        @Override
        Urin<SEGMENT, Q> resolve(final Scheme<SEGMENT, Q> scheme, final Path<SEGMENT> path, final Q query, final Fragment fragment) {
            return new UrinWithAuthorityAndPathAndQuery<>(scheme.removeDefaultPort(), scheme.normalise(authority), this.path, this.query);
        }

        @Override
        Urin<SEGMENT, Q> resolve(final Scheme<SEGMENT, Q> scheme, final Authority authority, final Path<SEGMENT> path, final Q query, final Fragment fragment) {
            return new UrinWithAuthorityAndPathAndQuery<>(scheme.removeDefaultPort(), scheme.normalise(this.authority), this.path, this.query);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            RelativeReferenceWithAuthorityAndQuery that = (RelativeReferenceWithAuthorityAndQuery) o;

            return authority.equals(that.authority) && query.equals(that.query) && path.equals(that.path);
        }

        @Override
        public int hashCode() {
            int result = authority.hashCode();
            result = 31 * result + path.hashCode();
            result = 31 * result + query.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "RelativeReference{" +
                    "authority=" + authority +
                    ", path=" + path +
                    ", query=" + query +
                    '}';
        }
    }

    private static final class RelativeReferenceWithAuthorityAndFragment<SEGMENT, Q extends Query> extends RelativeReference<SEGMENT, Q> {
        private final Authority authority;
        private final Path<SEGMENT> path;
        private final Fragment fragment;

        RelativeReferenceWithAuthorityAndFragment(final Authority authority, final Path<SEGMENT> path, final Fragment fragment) {
            if (authority == null) {
                throw new NullPointerException("Cannot instantiate RelativeReference with null authority");
            }
            this.authority = authority;
            if (path == null) {
                throw new NullPointerException("Cannot instantiate RelativeReference with null path");
            }
            this.path = path;
            if (fragment == null) {
                throw new NullPointerException("Cannot instantiate RelativeReference with null fragment");
            }
            this.fragment = fragment;
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
        public Fragment fragment() {
            return fragment;
        }

        @Override
        public boolean hasQuery() {
            return false;
        }

        @Override
        public Q query() {
            throw new UnsupportedOperationException("Attempt to get query from a UrinReference that does not have one.");
        }

        @Override
        Urin<SEGMENT, Q> resolve(final Scheme<SEGMENT, Q> scheme, final Path<SEGMENT> path) {
            return new UrinWithAuthorityAndPathAndFragment<>(scheme.removeDefaultPort(), scheme.normalise(authority), this.path, fragment);
        }

        @Override
        Urin<SEGMENT, Q> resolve(final Scheme<SEGMENT, Q> scheme, final Authority authority, final Path<SEGMENT> path) {
            return new UrinWithAuthorityAndPathAndFragment<>(scheme.removeDefaultPort(), scheme.normalise(this.authority), this.path, fragment);
        }

        @Override
        Urin<SEGMENT, Q> resolve(final Scheme<SEGMENT, Q> scheme, final Path<SEGMENT> path, final Q query) {
            return new UrinWithAuthorityAndPathAndFragment<>(scheme.removeDefaultPort(), scheme.normalise(authority), this.path, fragment);
        }

        @Override
        Urin<SEGMENT, Q> resolve(final Scheme<SEGMENT, Q> scheme, final Authority authority, final Path<SEGMENT> path, final Q query) {
            return new UrinWithAuthorityAndPathAndFragment<>(scheme.removeDefaultPort(), scheme.normalise(this.authority), this.path, fragment);
        }

        @Override
        Urin<SEGMENT, Q> resolve(final Scheme<SEGMENT, Q> scheme, final Path<SEGMENT> path, final Q query, final Fragment fragment) {
            return new UrinWithAuthorityAndPathAndFragment<>(scheme.removeDefaultPort(), scheme.normalise(authority), this.path, this.fragment);
        }

        @Override
        Urin<SEGMENT, Q> resolve(final Scheme<SEGMENT, Q> scheme, final Authority authority, final Path<SEGMENT> path, final Q query, final Fragment fragment) {
            return new UrinWithAuthorityAndPathAndFragment<>(scheme.removeDefaultPort(), scheme.normalise(this.authority), this.path, this.fragment);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            RelativeReferenceWithAuthorityAndFragment that = (RelativeReferenceWithAuthorityAndFragment) o;

            return authority.equals(that.authority) && fragment.equals(that.fragment) && path.equals(that.path);
        }

        @Override
        public int hashCode() {
            int result = authority.hashCode();
            result = 31 * result + path.hashCode();
            result = 31 * result + fragment.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "RelativeReference{" +
                    "authority=" + authority +
                    ", path=" + path +
                    ", fragment=" + fragment +
                    '}';
        }
    }

    private static final class RelativeReferenceWithAuthorityAndQueryAndFragment<SEGMENT, Q extends Query> extends RelativeReference<SEGMENT, Q> {
        private final Authority authority;
        private final Path<SEGMENT> path;
        private final Q query;
        private final Fragment fragment;

        RelativeReferenceWithAuthorityAndQueryAndFragment(final Authority authority, final Path<SEGMENT> path, final Q query, final Fragment fragment) {
            if (authority == null) {
                throw new NullPointerException("Cannot instantiate RelativeReference with null authority");
            }
            this.authority = authority;
            if (path == null) {
                throw new NullPointerException("Cannot instantiate RelativeReference with null path");
            }
            this.path = path;
            if (query == null) {
                throw new NullPointerException("Cannot instantiate RelativeReference with null query");
            }
            this.query = query;
            if (fragment == null) {
                throw new NullPointerException("Cannot instantiate RelativeReference with null fragment");
            }
            this.fragment = fragment;
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
        public Fragment fragment() {
            return fragment;
        }

        @Override
        public boolean hasQuery() {
            return true;
        }

        @Override
        public Q query() {
            return query;
        }

        @Override
        Urin<SEGMENT, Q> resolve(final Scheme<SEGMENT, Q> scheme, final Path<SEGMENT> path) {
            return new UrinWithAuthorityAndPathAndQueryAndFragment<>(scheme.removeDefaultPort(), scheme.normalise(authority), this.path, query, fragment);
        }

        @Override
        Urin<SEGMENT, Q> resolve(final Scheme<SEGMENT, Q> scheme, final Authority authority, final Path<SEGMENT> path) {
            return new UrinWithAuthorityAndPathAndQueryAndFragment<>(scheme.removeDefaultPort(), scheme.normalise(this.authority), this.path, query, fragment);
        }

        @Override
        Urin<SEGMENT, Q> resolve(final Scheme<SEGMENT, Q> scheme, final Path<SEGMENT> path, final Q query) {
            return new UrinWithAuthorityAndPathAndQueryAndFragment<>(scheme.removeDefaultPort(), scheme.normalise(authority), this.path, this.query, fragment);
        }

        @Override
        Urin<SEGMENT, Q> resolve(final Scheme<SEGMENT, Q> scheme, final Authority authority, final Path<SEGMENT> path, final Q query) {
            return new UrinWithAuthorityAndPathAndQueryAndFragment<>(scheme.removeDefaultPort(), scheme.normalise(this.authority), this.path, this.query, fragment);
        }

        @Override
        Urin<SEGMENT, Q> resolve(final Scheme<SEGMENT, Q> scheme, final Path<SEGMENT> path, final Q query, final Fragment fragment) {
            return new UrinWithAuthorityAndPathAndQueryAndFragment<>(scheme.removeDefaultPort(), scheme.normalise(authority), this.path, this.query, this.fragment);
        }

        @Override
        Urin<SEGMENT, Q> resolve(final Scheme<SEGMENT, Q> scheme, final Authority authority, final Path<SEGMENT> path, final Q query, final Fragment fragment) {
            return new UrinWithAuthorityAndPathAndQueryAndFragment<>(scheme.removeDefaultPort(), scheme.normalise(this.authority), this.path, this.query, this.fragment);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            RelativeReferenceWithAuthorityAndQueryAndFragment that = (RelativeReferenceWithAuthorityAndQueryAndFragment) o;

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

        @Override
        public String toString() {
            return "RelativeReference{" +
                    "authority=" + authority +
                    ", path=" + path +
                    ", query=" + query +
                    ", fragment=" + fragment +
                    '}';
        }
    }

    private static final class UrinWithPathAndQueryAndFragment<SEGMENT, Q extends Query> extends Urin<SEGMENT, Q> {
        private final Scheme<SEGMENT, Q> scheme;
        private final Path<SEGMENT> path;
        private final Q query;
        private final Fragment fragment;

        UrinWithPathAndQueryAndFragment(final Scheme<SEGMENT, Q> scheme, final Path<SEGMENT> path, final Q query, final Fragment fragment) {
            if (scheme == null) {
                throw new NullPointerException("Cannot instantiate Urin with null scheme");
            }
            this.scheme = scheme;
            if (path == null) {
                throw new NullPointerException("Cannot instantiate Urin with null path");
            }
            this.path = path;
            if (query == null) {
                throw new NullPointerException("Cannot instantiate Urin with null query");
            }
            this.query = query;
            if (fragment == null) {
                throw new NullPointerException("Cannot instantiate Urin with null fragment");
            }
            this.fragment = fragment;
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
        public Fragment fragment() {
            return fragment;
        }

        @Override
        public boolean hasQuery() {
            return true;
        }

        @Override
        public Q query() {
            return query;
        }

        @Override
        public Urin<SEGMENT, Q> resolve(final UrinReference<SEGMENT, Q> urinReference) {
            return urinReference.resolve(scheme, path, query, fragment);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            UrinWithPathAndQueryAndFragment that = (UrinWithPathAndQueryAndFragment) o;
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

        @Override
        public String toString() {
            return "Urin{" +
                    "scheme=" + scheme +
                    ", path=" + path +
                    ", query=" + query +
                    ", fragment=" + fragment +
                    '}';
        }
    }

    private static final class UrinWithAuthorityAndPathAndQueryAndFragment<SEGMENT, Q extends Query> extends Urin<SEGMENT, Q> {
        private final Scheme<SEGMENT, Q> scheme;
        private final Authority authority;
        private final Path<SEGMENT> path;
        private final Q query;
        private final Fragment fragment;

        UrinWithAuthorityAndPathAndQueryAndFragment(final Scheme<SEGMENT, Q> scheme, final Authority authority, final Path<SEGMENT> path, final Q query, final Fragment fragment) {
            if (scheme == null) {
                throw new NullPointerException("Cannot instantiate Urin with null scheme");
            }
            this.scheme = scheme;
            if (authority == null) {
                throw new NullPointerException("Cannot instantiate Urin with null authority");
            }
            this.authority = authority;
            if (path == null) {
                throw new NullPointerException("Cannot instantiate Urin with null path");
            }
            this.path = path;
            if (query == null) {
                throw new NullPointerException("Cannot instantiate Urin with null query");
            }
            this.query = query;
            if (fragment == null) {
                throw new NullPointerException("Cannot instantiate Urin with null fragment");
            }
            this.fragment = fragment;
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
        public Fragment fragment() {
            return fragment;
        }

        @Override
        public boolean hasQuery() {
            return true;
        }

        @Override
        public Q query() {
            return query;
        }

        @Override
        public Urin<SEGMENT, Q> resolve(final UrinReference<SEGMENT, Q> urinReference) {
            return urinReference.resolve(scheme, authority, path, query, fragment);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            UrinWithAuthorityAndPathAndQueryAndFragment that = (UrinWithAuthorityAndPathAndQueryAndFragment) o;

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

        @Override
        public String toString() {
            return "Urin{" +
                    "scheme=" + scheme +
                    ", authority=" + authority +
                    ", path=" + path +
                    ", query=" + query +
                    ", fragment=" + fragment +
                    '}';
        }
    }

    private static final class UrinWithPathAndQuery<SEGMENT, Q extends Query> extends Urin<SEGMENT, Q> {
        private final Scheme<SEGMENT, Q> scheme;
        private final Path<SEGMENT> path;
        private final Q query;

        UrinWithPathAndQuery(final Scheme<SEGMENT, Q> scheme, final Path<SEGMENT> path, final Q query) {
            if (scheme == null) {
                throw new NullPointerException("cannot instantiate Urin with null scheme");
            }
            this.scheme = scheme;
            if (path == null) {
                throw new NullPointerException("cannot instantiate Urin with null path");
            }
            this.path = path;
            if (query == null) {
                throw new NullPointerException("cannot instantiate Urin with null query");
            }
            this.query = query;
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
        public Fragment fragment() {
            throw new UnsupportedOperationException("Attempt to get fragment from a UrinReference that does not have one.");
        }

        @Override
        public boolean hasQuery() {
            return true;
        }

        @Override
        public Q query() {
            return query;
        }

        @Override
        public Urin<SEGMENT, Q> resolve(final UrinReference<SEGMENT, Q> urinReference) {
            return urinReference.resolve(scheme, path, query);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            UrinWithPathAndQuery that = (UrinWithPathAndQuery) o;
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

        @Override
        public String toString() {
            return "Urin{" +
                    "scheme=" + scheme +
                    ", path=" + path +
                    ", query=" + query +
                    '}';
        }
    }

    private static final class UrinWithAuthorityAndPathAndQuery<SEGMENT, Q extends Query> extends Urin<SEGMENT, Q> {
        private final Scheme<SEGMENT, Q> scheme;
        private final Authority authority;
        private final Path<SEGMENT> path;
        private final Q query;

        UrinWithAuthorityAndPathAndQuery(final Scheme<SEGMENT, Q> scheme, final Authority authority, final Path<SEGMENT> path, final Q query) {
            if (scheme == null) {
                throw new NullPointerException("cannot instantiate Urin with null scheme");
            }
            this.scheme = scheme;
            if (authority == null) {
                throw new NullPointerException("cannot instantiate Urin with null authority");
            }
            this.authority = authority;
            if (path == null) {
                throw new NullPointerException("cannot instantiate Urin with null path");
            }
            this.path = path;
            if (query == null) {
                throw new NullPointerException("cannot instantiate Urin with null query");
            }
            this.query = query;
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
        public Fragment fragment() {
            throw new UnsupportedOperationException("Attempt to get fragment from a UrinReference that does not have one.");
        }

        @Override
        public boolean hasQuery() {
            return true;
        }

        @Override
        public Q query() {
            return query;
        }

        @Override
        public Urin<SEGMENT, Q> resolve(final UrinReference<SEGMENT, Q> urinReference) {
            return urinReference.resolve(scheme, authority, path, query);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            UrinWithAuthorityAndPathAndQuery that = (UrinWithAuthorityAndPathAndQuery) o;

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

        @Override
        public String toString() {
            return "Urin{" +
                    "scheme=" + scheme +
                    ", authority=" + authority +
                    ", path=" + path +
                    ", query=" + query +
                    '}';
        }
    }

    private static final class UrinWithAuthorityAndPathAndFragment<SEGMENT, Q extends Query> extends Urin<SEGMENT, Q> {
        private final Scheme<SEGMENT, Q> scheme;
        private final Authority authority;
        private final Path<SEGMENT> path;
        private final Fragment fragment;

        UrinWithAuthorityAndPathAndFragment(final Scheme<SEGMENT, Q> scheme, final Authority authority, final Path<SEGMENT> path, final Fragment fragment) {
            if (scheme == null) {
                throw new NullPointerException("cannot instantiate Urin with null scheme");
            }
            this.scheme = scheme;
            if (authority == null) {
                throw new NullPointerException("cannot instantiate Urin with null authority");
            }
            this.authority = authority;
            if (path == null) {
                throw new NullPointerException("cannot instantiate Urin with null path");
            }
            this.path = path;
            if (fragment == null) {
                throw new NullPointerException("cannot instantiate Urin with null fragment");
            }
            this.fragment = fragment;
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
        public Fragment fragment() {
            return fragment;
        }

        @Override
        public boolean hasQuery() {
            return false;
        }

        @Override
        public Q query() {
            throw new UnsupportedOperationException("Attempt to get query from a UrinReference that does not have one.");
        }

        @Override
        public Urin<SEGMENT, Q> resolve(final UrinReference<SEGMENT, Q> urinReference) {
            return urinReference.resolve(scheme, authority, path);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            UrinWithAuthorityAndPathAndFragment that = (UrinWithAuthorityAndPathAndFragment) o;

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

        @Override
        public String toString() {
            return "Urin{" +
                    "scheme=" + scheme +
                    ", authority=" + authority +
                    ", path=" + path +
                    ", fragment=" + fragment +
                    '}';
        }
    }

    private static final class UrinWithPathAndFragment<SEGMENT, Q extends Query> extends Urin<SEGMENT, Q> {
        private final Scheme<SEGMENT, Q> scheme;
        private final Path<SEGMENT> path;
        private final Fragment fragment;

        UrinWithPathAndFragment(final Scheme<SEGMENT, Q> scheme, final Path<SEGMENT> path, final Fragment fragment) {
            if (scheme == null) {
                throw new NullPointerException("cannot instantiate Urin with null scheme");
            }
            this.scheme = scheme;
            if (path == null) {
                throw new NullPointerException("cannot instantiate Urin with null path");
            }
            this.path = path;
            if (fragment == null) {
                throw new NullPointerException("cannot instantiate Urin with null fragment");
            }
            this.fragment = fragment;
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
        public Fragment fragment() {
            return fragment;
        }

        @Override
        public boolean hasQuery() {
            return false;
        }

        @Override
        public Q query() {
            throw new UnsupportedOperationException("Attempt to get query from a UrinReference that does not have one.");
        }

        @Override
        public Urin<SEGMENT, Q> resolve(final UrinReference<SEGMENT, Q> urinReference) {
            return urinReference.resolve(scheme, path);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            UrinWithPathAndFragment that = (UrinWithPathAndFragment) o;

            return fragment.equals(that.fragment) && path.equals(that.path) && scheme.equals(that.scheme);

        }

        @Override
        public int hashCode() {
            int result = scheme.hashCode();
            result = 31 * result + path.hashCode();
            result = 31 * result + fragment.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "Urin{" +
                    "scheme=" + scheme +
                    ", path=" + path +
                    ", fragment=" + fragment +
                    '}';
        }
    }

    private static class UrinWithPath<SEGMENT, Q extends Query> extends Urin<SEGMENT, Q> {
        private final Scheme<SEGMENT, Q> scheme;
        private final Path<SEGMENT> path;

        UrinWithPath(final Scheme<SEGMENT, Q> scheme, final Path<SEGMENT> path) {
            if (scheme == null) {
                throw new NullPointerException("cannot instantiate Urin with null scheme");
            }
            this.scheme = scheme;
            if (path == null) {
                throw new NullPointerException("cannot instantiate Urin with null path");
            }
            this.path = path;
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
        public Fragment fragment() {
            throw new UnsupportedOperationException("Attempt to get fragment from a UrinReference that does not have one.");
        }

        @Override
        public boolean hasQuery() {
            return false;
        }

        @Override
        public Q query() {
            throw new UnsupportedOperationException("Attempt to get query from a UrinReference that does not have one.");
        }

        @Override
        public Urin<SEGMENT, Q> resolve(final UrinReference<SEGMENT, Q> urinReference) {
            return urinReference.resolve(scheme, path);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            UrinWithPath that = (UrinWithPath) o;

            return path.equals(that.path) && scheme.equals(that.scheme);

        }

        @Override
        public int hashCode() {
            int result = scheme.hashCode();
            result = 31 * result + path.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "Urin{" +
                    "scheme=" + scheme +
                    ", path=" + path +
                    '}';
        }
    }

    private static class UrinWithAuthorityAndPath<SEGMENT, Q extends Query> extends Urin<SEGMENT, Q> {
        private final Scheme<SEGMENT, Q> scheme;
        private final Authority authority;
        private final Path<SEGMENT> path;

        UrinWithAuthorityAndPath(final Scheme<SEGMENT, Q> scheme, final Authority authority, final Path<SEGMENT> path) {
            if (scheme == null) {
                throw new NullPointerException("cannot instantiate Urin with null scheme");
            }
            this.scheme = scheme;
            if (authority == null) {
                throw new NullPointerException("cannot instantiate Urin with null authority");
            }
            this.authority = authority;
            if (path == null) {
                throw new NullPointerException("cannot instantiate Urin with null path");
            }
            this.path = path;
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
        public Fragment fragment() {
            throw new UnsupportedOperationException("Attempt to get fragment from a UrinReference that does not have one.");
        }

        @Override
        public boolean hasQuery() {
            return false;
        }

        @Override
        public Q query() {
            throw new UnsupportedOperationException("Attempt to get query from a UrinReference that does not have one.");
        }

        @Override
        public Urin<SEGMENT, Q> resolve(final UrinReference<SEGMENT, Q> urinReference) {
            return urinReference.resolve(scheme, authority, path);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            UrinWithAuthorityAndPath that = (UrinWithAuthorityAndPath) o;

            return authority.equals(that.authority) && path.equals(that.path) && scheme.equals(that.scheme);

        }

        @Override
        public int hashCode() {
            int result = scheme.hashCode();
            result = 31 * result + authority.hashCode();
            result = 31 * result + path.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "Urin{" +
                    "scheme=" + scheme +
                    ", authority=" + authority +
                    ", path=" + path +
                    '}';
        }
    }
}
