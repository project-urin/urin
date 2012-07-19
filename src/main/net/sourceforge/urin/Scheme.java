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
import static net.sourceforge.urin.HierarchicalPart.backdoorHierarchicalPart;
import static net.sourceforge.urin.HierarchicalPart.hierarchicalPart;
import static net.sourceforge.urin.Path.PrefixWithDotSegmentCriteria.*;

/**
 * A name component of a URI.
 * Immutable and threadsafe.
 *
 * @see <a href="http://tools.ietf.org/html/rfc3986#section-3.1">RFC 3986 - Scheme</a>
 */
public abstract class Scheme {

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

    Scheme() {
        // deliberately empty
    }

    /**
     * Factory method for creating {@code Scheme}s.  Schemes must be at least one character long, and are permitted
     * to contain any character in the Latin alphabet, any digit, and any of the characters '+', '-', and '.'.
     *
     * @param name a {@code String} containing at least one character, made up of any characters in the Latin alphabet, the digits, and the characters '+', '-', and '.'.
     * @return a {@code Scheme} representing the given {@code String}.
     * @throws IllegalArgumentException if the given {@code String} is empty or contains characters not in the Latin alphabet, the digits, or the characters '+', '-', and '.'.
     */
    public static Scheme scheme(final String name) {
        verify(name, ExceptionFactory.ILLEGAL_ARGUMENT_EXCEPTION_EXCEPTION_FACTORY);
        return new GenericScheme(name.toLowerCase(ENGLISH));
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
    public static Scheme scheme(final String name, final Port defaultPort) {
        verify(name, ExceptionFactory.ILLEGAL_ARGUMENT_EXCEPTION_EXCEPTION_FACTORY);
        return new SchemeWithDefaultPort(name.toLowerCase(ENGLISH), defaultPort);
    }

    private Scheme parse(final String name) throws ParseException {
        verify(name, ExceptionFactory.PARSE_EXCEPTION_EXCEPTION_FACTORY);
        return withName(name);
    }

    abstract Scheme withName(String name);

    private static <T extends Exception> void verify(final String name, final ExceptionFactory<T> exceptionFactory) throws T {
        if (name.isEmpty()) {
            throw exceptionFactory.makeException("Scheme must contain at least one character");
        }
        CharacterSetMembershipFunction.verify(ALPHA, name, "scheme", 0, 1, exceptionFactory);
        CharacterSetMembershipFunction.verify(TRAILING_CHARACTER_MEMBERSHIP_FUNCTION, name, "scheme", 1, exceptionFactory);
    }

    abstract String asString();

    abstract Authority normalise(final Authority authority);

    abstract Scheme removeDefaultPort();

    /**
     * Factory method for creating {@code RelativeReference}s with just an empty path.
     *
     * @return a {@code RelativeReference} with an empty path.
     */
    public final RelativeReference relativeReference() {
        return new RelativeReferenceNoAuthority(new EmptyPath());
    }

    /**
     * Factory method for creating {@code RelativeReference}s with an authority and an empty path.
     *
     * @param authority any {@code Authority} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Authority} and an empty path.
     */
    public final RelativeReference relativeReference(final Authority authority) {
        return new RelativeReferenceWithAuthority(authority, new EmptyPath());
    }

    /**
     * Factory method for creating {@code RelativeReference}s with just a path.
     *
     * @param path any {@code Path} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Path}.
     */
    public final RelativeReference relativeReference(final Path path) {
        return new RelativeReferenceNoAuthority(path);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with an authority and a path.
     *
     * @param authority any {@code Authority} to use in this {@code RelativeReference}.
     * @param path      any {@code AbsolutePath} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Authority} and {@code Path}.
     */
    public final RelativeReference relativeReference(final Authority authority, final AbsolutePath path) {
        return new RelativeReferenceWithAuthority(authority, path);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with a query and an empty path.
     *
     * @param query any {@code Query} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Query} and an empty path.
     */
    public final RelativeReference relativeReference(final Query query) {
        return new RelativeReferenceNoAuthorityWithQuery(new EmptyPath(), query);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with an authority, a query and an empty path.
     *
     * @param authority any {@code Authority} to use in this {@code RelativeReference}.
     * @param query     any {@code Query} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Authority} and {@code Query}, and an empty path.
     */
    public final RelativeReference relativeReference(final Authority authority, final Query query) {
        return new RelativeReferenceWithAuthorityAndQuery(authority, new EmptyPath(), query);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with a path and a query.
     *
     * @param path  any {@code Path} to use in this {@code RelativeReference}.
     * @param query any {@code Query} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Path} and {@code Query}.
     */
    public final RelativeReference relativeReference(final Path path, final Query query) {
        return new RelativeReferenceNoAuthorityWithQuery(path, query);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with an authority, a path, and a query.
     *
     * @param authority any {@code Authority} to use in this {@code RelativeReference}.
     * @param path      any {@code Path} to use in this {@code RelativeReference}.
     * @param query     any {@code Query} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Authority}, {@code Path} and {@code Query}.
     */
    public final RelativeReference relativeReference(final Authority authority, final AbsolutePath path, final Query query) {
        return new RelativeReferenceWithAuthorityAndQuery(authority, path, query);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with a fragment and an empty path.
     *
     * @param fragment any {@code Fragment} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Fragment} and an empty path.
     */
    public final RelativeReference relativeReference(final Fragment fragment) {
        return new RelativeReferenceNoAuthorityWithFragment(new EmptyPath(), fragment);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with an authority, a fragment, and an empty path.
     *
     * @param authority any {@code Authority} to use in this {@code RelativeReference}.
     * @param fragment  any {@code Fragment} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Authority} and {@code Fragment}, and an empty path.
     */
    public final RelativeReference relativeReference(final Authority authority, final Fragment fragment) {
        return new RelativeReferenceWithAuthorityAndFragment(authority, new EmptyPath(), fragment);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with a path and a fragment.
     *
     * @param path     any {@code Path} to use in this {@code RelativeReference}.
     * @param fragment any {@code Fragment} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Path} and {@code Fragment}.
     */
    public final RelativeReference relativeReference(final Path path, final Fragment fragment) {
        return new RelativeReferenceNoAuthorityWithFragment(path, fragment);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with an authority, a path, and fragment.
     *
     * @param authority any {@code Authority} to use in this {@code RelativeReference}.
     * @param path      any {@code AbsolutePath} to use in this {@code RelativeReference}.
     * @param fragment  any {@code Fragment} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Authority}, {@code Path} and {@code Fragment}.
     */
    public final RelativeReference relativeReference(final Authority authority, final AbsolutePath path, final Fragment fragment) {
        return new RelativeReferenceWithAuthorityAndFragment(authority, path, fragment);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with a query, a fragment, and an empty path.
     *
     * @param query    any {@code Query} to use in this {@code RelativeReference}.
     * @param fragment any {@code Fragment} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Query} and {@code Fragment}, and an empty path.
     */
    public final RelativeReference relativeReference(final Query query, final Fragment fragment) {
        return new RelativeReferenceNoAuthorityWithQueryAndFragment(new EmptyPath(), query, fragment);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with an authority, a query, a fragment, and an empty path.
     *
     * @param authority any {@code Authority} to use in this {@code RelativeReference}.
     * @param query     any {@code Query} to use in this {@code RelativeReference}.
     * @param fragment  any {@code Fragment} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Authority}, {@code Query} and {@code Fragment}, and an empty path.
     */
    public final RelativeReference relativeReference(final Authority authority, final Query query, final Fragment fragment) {
        return new RelativeReferenceWithAuthorityAndQueryAndFragment(authority, new EmptyPath(), query, fragment);
    }

    /**
     * Factory method for creating {@code RelativeReference}s with a path, a query, and a fragment.
     *
     * @param path     any {@code Path} to use in this {@code RelativeReference}.
     * @param query    any {@code Query} to use in this {@code RelativeReference}.
     * @param fragment any {@code Fragment} to use in this {@code RelativeReference}.
     * @return a {@code RelativeReference} with the given {@code Path}, {@code Query} and {@code Fragment}.
     */
    public final RelativeReference relativeReference(final Path path, final Query query, final Fragment fragment) {
        return new RelativeReferenceNoAuthorityWithQueryAndFragment(path, query, fragment);
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
    public final RelativeReference relativeReference(final Authority authority, final AbsolutePath path, final Query query, final Fragment fragment) {
        return new RelativeReferenceWithAuthorityAndQueryAndFragment(authority, path, query, fragment);
    }

    /**
     * Parses the given {@code String} as a relative reference.
     *
     * @param relativeReferenceString a {@code String} that represents a relative reference.
     * @return a {@code UrinReference} representing the relative reference represented by the given {@code String}.
     * @throws ParseException if the given {@code String} is not a valid relative reference.
     */
    public final RelativeReference parseRelativeReference(final String relativeReferenceString) throws ParseException {
        final Matcher matcher = RELATIVE_REFERENCE_PATTERN.matcher(relativeReferenceString);
        matcher.matches();
        final String queryString = matcher.group(6);
        final String fragment = matcher.group(8);
        final RelativeReference result;
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
                    final Query query = Query.parse(queryString);
                    if (fragment == null) {
                        result = relativeReference(query);
                    } else {
                        result = relativeReference(query, Fragment.parse(fragment));
                    }
                }
            } else {
                final Path path = !pathString.startsWith("/") ? Path.parseRootlessPath(pathString) : Path.parseParse(pathString);
                if (queryString == null) {
                    if (fragment == null) {
                        result = relativeReference(path);
                    } else {
                        result = relativeReference(path, Fragment.parse(fragment));
                    }
                } else {
                    final Query query = Query.parse(queryString);
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
                    final Query query = Query.parse(queryString);
                    if (fragment == null) {
                        result = relativeReference(authority, query);
                    } else {
                        result = relativeReference(authority, query, Fragment.parse(fragment));
                    }
                }
            } else {
                final AbsolutePath path = Path.parseParse(pathString);
                if (queryString == null) {
                    if (fragment == null) {
                        result = relativeReference(authority, path);
                    } else {
                        result = relativeReference(authority, path, Fragment.parse(fragment));
                    }
                } else {
                    final Query query = Query.parse(queryString);
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
    public final RelativeReference parseRelativeReference(URI uri) throws ParseException {
        return parseRelativeReference(uri.toASCIIString());
    }

    static boolean isValidRelativeReferenceString(final String uriReferenceString) {
        return RELATIVE_REFERENCE_PATTERN.matcher(uriReferenceString).matches();
    }

    /**
     * Factory method for creating {@code Urin}s with just scheme and hierarchical part components.
     *
     * @param hierarchicalPart any {@code HierarchicalPart} to use in this {@code Urin}.
     * @return a {@code Urin} with the given {@code Scheme} and {@code HierarchicalPart}.
     * @deprecated
     */
    public final Urin urin(final HierarchicalPart hierarchicalPart) {
        return new UrinWithHierarchicalPart(removeDefaultPort(), hierarchicalPart.normalisePort(this));
    }

    /**
     * Factory method for creating {@code Urin}s with just a scheme and empty path.
     *
     * @return a {@code Urin} with the given {@code Scheme} and an empty path.
     */
    public final Urin urin() {
        return new UrinWithPath(removeDefaultPort(), new EmptyPath());
    }

    /**
     * Factory method for creating {@code Urin}s with just scheme and path components.
     *
     * @param path any {@code Path} to use in this {@code Urin}.
     * @return a {@code Urin} with the given {@code Scheme} and {@code Path}.
     */
    public final Urin urin(final Path path) {
        return new UrinWithPath(removeDefaultPort(), path);
    }

    /**
     * Factory method for creating {@code Urin}s with just scheme, authority, and empty path components.
     *
     * @param authority any {@code Authority} to use in this {@code Urin}.
     * @return a {@code Urin} with the given {@code Scheme} and {@code Authority}, and an empty path.
     */
    public final Urin urin(final Authority authority) {
        return new UrinWithAuthorityAndPath(removeDefaultPort(), normalise(authority), new EmptyPath());
    }

    /**
     * Factory method for creating {@code Urin}s with just scheme, authority, and path components.
     *
     * @param authority any {@code Authority} to use in this {@code Urin}.
     * @param path      any {@code AbsolutePath} to use in this {@code Urin}.
     * @return a {@code Urin} with the given {@code Scheme}, {@code Authority}, and {@code AbsolutePath}.
     */
    public final Urin urin(final Authority authority, final AbsolutePath path) {
        return new UrinWithAuthorityAndPath(removeDefaultPort(), normalise(authority), path);
    }

    /**
     * Factory method for creating {@code Urin}s with scheme, hierarchical part, and fragment components.
     *
     * @param hierarchicalPart any {@code HierarchicalPart} to use in this {@code Urin}.
     * @param fragment         any {@code Fragment} to use in this {@code Urin}.
     * @return a {@code Urin} with the given {@code Scheme}, {@code HierarchicalPart}, and {@code Fragment}.
     * @deprecated
     */
    public final Urin urin(final HierarchicalPart hierarchicalPart, final Fragment fragment) {
        return new UrinWithHierarchicalPartAndFragment(removeDefaultPort(), hierarchicalPart.normalisePort(this), fragment);
    }

    /**
     * Factory method for creating {@code Urin}s with just a scheme, fragment, and empty path.
     *
     * @param fragment any {@code Fragment} to use in this {@code Urin}.
     * @return a {@code Urin} with the given {@code Scheme}, the given {@code Fragment}, and an empty path.
     */
    public final Urin urin(final Fragment fragment) {
        return new UrinWithPathAndFragment(removeDefaultPort(), new EmptyPath(), fragment);
    }

    /**
     * Factory method for creating {@code Urin}s with just scheme, path, and fragment components.
     *
     * @param path     any {@code Path} to use in this {@code Urin}.
     * @param fragment any {@code Fragment} to use in this {@code Urin}.
     * @return a {@code Urin} with the given {@code Scheme}, {@code Fragment}, and {@code Path}.
     */
    public final Urin urin(final Path path, final Fragment fragment) {
        return new UrinWithPathAndFragment(removeDefaultPort(), path, fragment);
    }

    /**
     * Factory method for creating {@code Urin}s with just scheme, authority, fragment, and empty path components.
     *
     * @param authority any {@code Authority} to use in this {@code Urin}.
     * @param fragment  any {@code Fragment} to use in this {@code Urin}.
     * @return a {@code Urin} with the given {@code Scheme}, {@code Authority}, and {@code Fragment}, and an empty path.
     */
    public final Urin urin(final Authority authority, final Fragment fragment) {
        return new UrinWithAuthorityAndPathAndFragment(removeDefaultPort(), normalise(authority), new EmptyPath(), fragment);
    }

    /**
     * Factory method for creating {@code Urin}s with just scheme, authority, path, and fragment components.
     *
     * @param authority any {@code Authority} to use in this {@code Urin}.
     * @param path      any {@code AbsolutePath} to use in this {@code Urin}.
     * @param fragment  any {@code Fragment} to use in this {@code Urin}.
     * @return a {@code Urin} with the given {@code Scheme}, {@code Authority}, {@code AbsolutePath}, and {@code Fragment}.
     */
    public final Urin urin(final Authority authority, final AbsolutePath path, final Fragment fragment) {
        return new UrinWithAuthorityAndPathAndFragment(removeDefaultPort(), normalise(authority), path, fragment);
    }

    /**
     * Factory method for creating {@code Urin}s with scheme, hierarchical part, and query components.
     *
     * @param hierarchicalPart any {@code HierarchicalPart} to use in this {@code Urin}.
     * @param query            any {@code Query} to use in this {@code Urin}.
     * @return a {@code Urin} with the given {@code Scheme}, {@code HierarchicalPart}, and {@code Query}.
     * @deprecated
     */
    public final Urin urin(final HierarchicalPart hierarchicalPart, final Query query) {
        return new UrinWithHierarchicalPartAndQuery(removeDefaultPort(), hierarchicalPart.normalisePort(this), query);
    }

    /**
     * Factory method for creating {@code Urin}s with just a scheme, query, and empty path.
     *
     * @param query any {@code Query} to use in this {@code Urin}.
     * @return a {@code Urin} with the given {@code Scheme}, the given {@code Query}, and an empty path.
     */
    public final Urin urin(final Query query) {
        return new UrinWithPathAndQuery(removeDefaultPort(), new EmptyPath(), query);
    }

    /**
     * Factory method for creating {@code Urin}s with just scheme, path, and query components.
     *
     * @param path  any {@code Path} to use in this {@code Urin}.
     * @param query any {@code Query} to use in this {@code Urin}.
     * @return a {@code Urin} with the given {@code Scheme}, {@code Query}, and {@code Path}.
     */
    public final Urin urin(final Path path, final Query query) {
        return new UrinWithPathAndQuery(removeDefaultPort(), path, query);
    }

    /**
     * Factory method for creating {@code Urin}s with just scheme, authority, query, and empty path components.
     *
     * @param authority any {@code Authority} to use in this {@code Urin}.
     * @param query     any {@code Query} to use in this {@code Urin}.
     * @return a {@code Urin} with the given {@code Scheme}, {@code Authority}, and {@code Query}, and an empty path.
     */
    public final Urin urin(final Authority authority, final Query query) {
        return new UrinWithAuthorityAndPathAndQuery(removeDefaultPort(), normalise(authority), new EmptyPath(), query);
    }

    /**
     * Factory method for creating {@code Urin}s with just scheme, authority, path, and query components.
     *
     * @param authority any {@code Authority} to use in this {@code Urin}.
     * @param path      any {@code AbsolutePath} to use in this {@code Urin}.
     * @param query     any {@code Query} to use in this {@code Urin}.
     * @return a {@code Urin} with the given {@code Scheme}, {@code Authority}, {@code AbsolutePath}, and {@code Query}.
     */
    public final Urin urin(final Authority authority, final AbsolutePath path, final Query query) {
        return new UrinWithAuthorityAndPathAndQuery(removeDefaultPort(), normalise(authority), path, query);
    }

    /**
     * Factory method for creating {@code Urin}s with scheme, hierarchical part, query, and fragment components.
     *
     * @param hierarchicalPart any {@code HierarchicalPart} to use in this {@code Urin}.
     * @param query            any {@code Query} to use in this {@code Urin}.
     * @param fragment         any {@code Fragment} to use in this {@code Urin}.
     * @return a {@code Urin} with the given {@code Scheme}, {@code HierarchicalPart}, {@code Query}, and {@code Fragment}.
     * @deprecated
     */
    public final Urin urin(final HierarchicalPart hierarchicalPart, final Query query, final Fragment fragment) {
        return new UrinWithHierarchicalPartAndQueryAndFragment(removeDefaultPort(), hierarchicalPart.normalisePort(this), query, fragment);
    }

    /**
     * Factory method for creating {@code Urin}s with scheme, empty path, query, and fragment components.
     *
     * @param query    any {@code Query} to use in this {@code Urin}.
     * @param fragment any {@code Fragment} to use in this {@code Urin}.
     * @return a {@code Urin} with the given {@code Scheme}, {@code Query}, and {@code Fragment}, and an empty path.
     */
    public final Urin urin(final Query query, final Fragment fragment) {
        return new UrinWithPathAndQueryAndFragment(removeDefaultPort(), new EmptyPath(), query, fragment);
    }

    /**
     * Factory method for creating {@code Urin}s with scheme, path, query, and fragment components.
     *
     * @param path     any {@code Path} to use in this {@code Urin}.
     * @param query    any {@code Query} to use in this {@code Urin}.
     * @param fragment any {@code Fragment} to use in this {@code Urin}.
     * @return a {@code Urin} with the given {@code Scheme}, {@code Path}, {@code Query}, and {@code Fragment}.
     */
    public final Urin urin(final Path path, final Query query, final Fragment fragment) {
        return new UrinWithPathAndQueryAndFragment(removeDefaultPort(), path, query, fragment);
    }

    /**
     * Factory method for creating {@code Urin}s with scheme, authority, empty path, query, and fragment components.
     *
     * @param authority any {@code Authority} to use in this {@code Urin}.
     * @param query     any {@code Query} to use in this {@code Urin}.
     * @param fragment  any {@code Fragment} to use in this {@code Urin}.
     * @return a {@code Urin} with the given {@code Scheme}, {@code Authority}, {@code Query}, and {@code Fragment}, and an empty path.
     */
    public final Urin urin(final Authority authority, final Query query, final Fragment fragment) {
        return new UrinWithAuthorityAndPathAndQueryAndFragment(removeDefaultPort(), normalise(authority), new EmptyPath(), query, fragment);
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
    public final Urin urin(final Authority authority, final AbsolutePath path, final Query query, final Fragment fragment) {
        return new UrinWithAuthorityAndPathAndQueryAndFragment(removeDefaultPort(), normalise(authority), path, query, fragment);
    }

    /**
     * Parses the given {@code String} as a URI.
     *
     * @param uriString a {@code String} that represents a URI.
     * @return a {@code Urin} representing the URI represented by the given {@code String}.
     * @throws ParseException if the given {@code String} is not a valid URI.
     */
    public final Urin parseUrin(final String uriString) throws ParseException {
        final Matcher matcher = URI_PATTERN.matcher(uriString);
        matcher.matches();
        final Scheme scheme = parse(matcher.group(2));
        final String authorityString = matcher.group(5);
        final String pathString = matcher.group(6);
        final String queryString = matcher.group(8);
        final String fragmentString = matcher.group(10);
        final Urin result;
        if (authorityString == null) {
            final Path path = !pathString.startsWith("/") ? Path.parseRootlessPath(pathString) : Path.parseParse(pathString);
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
                final Query query = Query.parse(queryString);
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
                                Path.parseParse(pathString)
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
                                Path.parseParse(pathString),
                                fragment
                        );
                    }
                }
            } else {
                final Query query = Query.parse(queryString);
                if (fragmentString == null) {
                    if (pathString == null || "".equals(pathString)) {
                        result = scheme.urin(
                                authority,
                                query
                        );
                    } else {
                        result = scheme.urin(
                                authority,
                                Path.parseParse(pathString),
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
                                Path.parseParse(pathString),
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
    public final Urin parseUrin(final URI uri) throws ParseException {
        return parseUrin(uri.toASCIIString());
    }

    static boolean isValidUrinString(final String uriReferenceString) {
        return URI_PATTERN.matcher(uriReferenceString).matches();
    }

    /**
     * Parses the given {@code String} as a URI reference.
     *
     * @param uriReferenceString a {@code String} that represents a URI reference.
     * @return a {@code UrinReference} representing the URI reference represented by the given {@code String}.
     * @throws ParseException if the given {@code String} is not a valid URI reference.
     */
    public final UrinReference parseUrinReference(final String uriReferenceString) throws ParseException {
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
    public final UrinReference parseUrinReference(final URI uriReference) throws ParseException {
        return parseUrinReference(uriReference.toASCIIString());
    }

    private static final class GenericScheme extends Scheme {
        private final String name;

        public GenericScheme(final String name) {
            this.name = name;
        }

        @Override
        GenericScheme withName(final String name) {
            return new GenericScheme(name);
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
        Scheme removeDefaultPort() {
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

    private static final class RelativeReferenceNoAuthority extends RelativeReference {
        private final Path path;

        private RelativeReferenceNoAuthority(final Path path) {
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
        public Path path() {
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
        public Query query() {
            throw new UnsupportedOperationException("Attempt to get query from a UrinReference that does not have one.");
        }

        @Override
        Urin resolve(final Scheme scheme, final Path path) {
            return scheme.urin(path.resolveRelativeTo(this.path));
        }

        @Override
        Urin resolve(final Scheme scheme, final Authority authority, final Path path) {
            return new UrinWithAuthorityAndPath(scheme.removeDefaultPort(), scheme.normalise(authority), this.path.resolveRelativeTo(path));
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart) {
            return scheme.urin(hierarchicalPart.resolve(path));
        }

        @Override
        Urin resolve(final Scheme scheme, final Path path, final Query query) {
            if (this.path.isEmpty()) {
                return scheme.urin(path.resolveRelativeTo(this.path), query);
            } else {
                return scheme.urin(path.resolveRelativeTo(this.path));
            }
        }

        @Override
        Urin resolve(final Scheme scheme, final Authority authority, final Path path, final Query query) {
            if (this.path.isEmpty()) {
                return new UrinWithAuthorityAndPathAndQuery(scheme.removeDefaultPort(), scheme.normalise(authority), this.path.resolveRelativeTo(path), query);
            } else {
                return new UrinWithAuthorityAndPath(scheme.removeDefaultPort(), scheme.normalise(authority), this.path.resolveRelativeTo(path));
            }
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query) {
            if (path.isEmpty()) {
                return scheme.urin(hierarchicalPart.resolve(path), query);
            } else {
                return scheme.urin(hierarchicalPart.resolve(path));
            }
        }

        @Override
        Urin resolve(final Scheme scheme, final Path path, final Query query, final Fragment fragment) {
            if (this.path.isEmpty()) {
                return scheme.urin(hierarchicalPart(path).resolve(this.path), query, fragment);
            } else {
                return scheme.urin(hierarchicalPart(path).resolve(this.path));
            }
        }

        @Override
        Urin resolve(final Scheme scheme, final Authority authority, final Path path, final Query query, final Fragment fragment) {
            if (this.path.isEmpty()) {
                return new UrinWithAuthorityAndPathAndQueryAndFragment(scheme.removeDefaultPort(), scheme.normalise(authority), this.path.resolveRelativeTo(path), query, fragment);
            } else {
                return new UrinWithAuthorityAndPath(scheme.removeDefaultPort(), scheme.normalise(authority), this.path.resolveRelativeTo(path));
            }
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query, final Fragment fragment) {
            if (path.isEmpty()) {
                return scheme.urin(hierarchicalPart.resolve(path), query, fragment);
            } else {
                return scheme.urin(hierarchicalPart.resolve(path));
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

    private static final class RelativeReferenceWithAuthority extends RelativeReference {
        private final Authority authority;
        private final Path path;

        private RelativeReferenceWithAuthority(final Authority authority, final Path path) {
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
        public Path path() {
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
        public Query query() {
            throw new UnsupportedOperationException("Attempt to get query from a UrinReference that does not have one.");
        }

        @Override
        Urin resolve(final Scheme scheme, final Path path) {
            return new UrinWithAuthorityAndPath(scheme.removeDefaultPort(), scheme.normalise(authority), this.path);
        }

        @Override
        Urin resolve(final Scheme scheme, final Authority authority, final Path path) {
            return new UrinWithAuthorityAndPath(scheme.removeDefaultPort(), scheme.normalise(this.authority), this.path);
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart) {
            return scheme.urin(hierarchicalPart.resolve(authority, path));
        }

        @Override
        Urin resolve(final Scheme scheme, final Path path, final Query query) {
            return scheme.urin(hierarchicalPart(path).resolve(authority, this.path));
        }

        @Override
        Urin resolve(final Scheme scheme, final Authority authority, final Path path, final Query query) {
            return new UrinWithAuthorityAndPath(scheme.removeDefaultPort(), scheme.normalise(this.authority), this.path);
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query) {
            return scheme.urin(hierarchicalPart.resolve(authority, path));
        }

        @Override
        Urin resolve(final Scheme scheme, final Path path, final Query query, final Fragment fragment) {
            return scheme.urin(hierarchicalPart(path).resolve(authority, this.path));
        }

        @Override
        Urin resolve(final Scheme scheme, final Authority authority, final Path path, final Query query, final Fragment fragment) {
            return new UrinWithAuthorityAndPath(scheme.removeDefaultPort(), scheme.normalise(this.authority), this.path);
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query, final Fragment fragment) {
            return scheme.urin(hierarchicalPart.resolve(authority, path));
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

    private static final class RelativeReferenceNoAuthorityWithQuery extends RelativeReference {
        private final Path path;
        private final Query query;

        RelativeReferenceNoAuthorityWithQuery(final Path path, final Query query) {
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
        public Path path() {
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
        public Query query() {
            return query;
        }

        @Override
        Urin resolve(final Scheme scheme, final Path path) {
            return scheme.urin(hierarchicalPart(path).resolve(this.path), query);
        }

        @Override
        Urin resolve(final Scheme scheme, final Authority authority, final Path path) {
            return new UrinWithAuthorityAndPathAndQuery(scheme.removeDefaultPort(), scheme.normalise(authority), this.path.resolveRelativeTo(path), query);
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart) {
            return scheme.urin(hierarchicalPart.resolve(path), query);
        }

        @Override
        Urin resolve(final Scheme scheme, final Path path, final Query query) {
            return scheme.urin(hierarchicalPart(path).resolve(this.path), this.query);
        }

        @Override
        Urin resolve(final Scheme scheme, final Authority authority, final Path path, final Query query) {
            return new UrinWithAuthorityAndPathAndQuery(scheme.removeDefaultPort(), scheme.normalise(authority), this.path.resolveRelativeTo(path), this.query);
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query) {
            return scheme.urin(hierarchicalPart.resolve(path), this.query);
        }

        @Override
        Urin resolve(final Scheme scheme, final Path path, final Query query, final Fragment fragment) {
            if (this.path.isEmpty()) {
                return scheme.urin(hierarchicalPart(path).resolve(this.path), this.query, fragment);
            } else {
                return scheme.urin(hierarchicalPart(path).resolve(this.path), this.query);
            }
        }

        @Override
        Urin resolve(final Scheme scheme, final Authority authority, final Path path, final Query query, final Fragment fragment) {
            if (this.path.isEmpty()) {
                return new UrinWithAuthorityAndPathAndQueryAndFragment(scheme.removeDefaultPort(), scheme.normalise(authority), this.path.resolveRelativeTo(path), this.query, fragment);
            } else {
                return new UrinWithAuthorityAndPathAndQuery(scheme.removeDefaultPort(), scheme.normalise(authority), this.path.resolveRelativeTo(path), this.query);
            }
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query, final Fragment fragment) {
            if (path.isEmpty()) {
                return scheme.urin(hierarchicalPart.resolve(path), this.query, fragment);
            } else {
                return scheme.urin(hierarchicalPart.resolve(path), this.query);
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

    private static final class RelativeReferenceNoAuthorityWithFragment extends RelativeReference {
        private final Path path;
        private final Fragment fragment;

        RelativeReferenceNoAuthorityWithFragment(final Path path, final Fragment fragment) {
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
        public Path path() {
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
        public Query query() {
            throw new UnsupportedOperationException("Attempt to get query from a UrinReference that does not have one.");
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart) {
            return scheme.urin(hierarchicalPart.resolve(path), fragment);
        }

        @Override
        Urin resolve(final Scheme scheme, final Path path) {
            return scheme.urin(hierarchicalPart(path).resolve(this.path), fragment);
        }

        @Override
        Urin resolve(final Scheme scheme, final Authority authority, final Path path) {
            return new UrinWithAuthorityAndPathAndFragment(scheme.removeDefaultPort(), scheme.normalise(authority), this.path.resolveRelativeTo(path), fragment);
        }

        @Override
        Urin resolve(final Scheme scheme, final Path path, final Query query) {
            if (this.path.isEmpty()) {
                return scheme.urin(this.path.resolveRelativeTo(path), query, fragment);
            } else {
                return scheme.urin(this.path.resolveRelativeTo(path), fragment);
            }
        }

        @Override
        Urin resolve(final Scheme scheme, final Authority authority, final Path path, final Query query) {
            if (this.path.isEmpty()) {
                return new UrinWithAuthorityAndPathAndQueryAndFragment(scheme.removeDefaultPort(), scheme.normalise(authority), this.path.resolveRelativeTo(path), query, fragment);
            } else {
                return new UrinWithAuthorityAndPathAndFragment(scheme.removeDefaultPort(), scheme.normalise(authority), this.path.resolveRelativeTo(path), fragment);
            }
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query) {
            if (path.isEmpty()) {
                return scheme.urin(hierarchicalPart.resolve(path), query, fragment);
            } else {
                return scheme.urin(hierarchicalPart.resolve(path), fragment);
            }
        }

        @Override
        Urin resolve(final Scheme scheme, final Path path, final Query query, final Fragment fragment) {
            if (this.path.isEmpty()) {
                return scheme.urin(hierarchicalPart(path).resolve(this.path), query, this.fragment);
            } else {
                return scheme.urin(hierarchicalPart(path).resolve(this.path), this.fragment);
            }
        }

        @Override
        Urin resolve(final Scheme scheme, final Authority authority, final Path path, final Query query, final Fragment fragment) {
            if (this.path.isEmpty()) {
                return new UrinWithAuthorityAndPathAndQueryAndFragment(scheme.removeDefaultPort(), scheme.normalise(authority), this.path.resolveRelativeTo(path), query, this.fragment);
            } else {
                return new UrinWithAuthorityAndPathAndFragment(scheme.removeDefaultPort(), scheme.normalise(authority), this.path.resolveRelativeTo(path), this.fragment);
            }
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query, final Fragment fragment) {
            if (path.isEmpty()) {
                return scheme.urin(hierarchicalPart.resolve(path), query, this.fragment);
            } else {
                return scheme.urin(hierarchicalPart.resolve(path), this.fragment);
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

    private static final class RelativeReferenceNoAuthorityWithQueryAndFragment extends RelativeReference {
        private final Path path;
        private final Fragment fragment;
        private final Query query;

        RelativeReferenceNoAuthorityWithQueryAndFragment(final Path path, final Query query, final Fragment fragment) {
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
        public Path path() {
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
        public Query query() {
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
        Urin resolve(final Scheme scheme, final Path path) {
            return scheme.urin(hierarchicalPart(path).resolve(this.path), query, fragment);
        }

        @Override
        Urin resolve(final Scheme scheme, final Authority authority, final Path path) {
            return new UrinWithAuthorityAndPathAndQueryAndFragment(scheme.removeDefaultPort(), scheme.normalise(authority), this.path.resolveRelativeTo(path), query, fragment);
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart) {
            return scheme.urin(hierarchicalPart.resolve(path), query, fragment);
        }

        @Override
        Urin resolve(final Scheme scheme, final Path path, final Query query) {
            return scheme.urin(hierarchicalPart(path).resolve(this.path), this.query, fragment);
        }

        @Override
        Urin resolve(final Scheme scheme, final Authority authority, final Path path, final Query query) {
            return new UrinWithAuthorityAndPathAndQueryAndFragment(scheme.removeDefaultPort(), scheme.normalise(authority), this.path.resolveRelativeTo(path), this.query, fragment);
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query) {
            return scheme.urin(hierarchicalPart.resolve(path), this.query, fragment);
        }

        @Override
        Urin resolve(final Scheme scheme, final Path path, final Query query, final Fragment fragment) {
            return scheme.urin(hierarchicalPart(path).resolve(this.path), this.query, this.fragment);
        }

        @Override
        Urin resolve(final Scheme scheme, final Authority authority, final Path path, final Query query, final Fragment fragment) {
            return new UrinWithAuthorityAndPathAndQueryAndFragment(scheme.removeDefaultPort(), scheme.normalise(authority), this.path.resolveRelativeTo(path), this.query, this.fragment);
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query, final Fragment fragment) {
            return scheme.urin(hierarchicalPart.resolve(path), this.query, this.fragment);
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

    private static final class RelativeReferenceWithAuthorityAndQuery extends RelativeReference {
        private final Authority authority;
        private final Path path;
        private final Query query;

        RelativeReferenceWithAuthorityAndQuery(final Authority authority, final Path path, final Query query) {
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
        public Path path() {
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
        public Query query() {
            return query;
        }

        @Override
        Urin resolve(final Scheme scheme, final Path path) {
            return scheme.urin(hierarchicalPart(path).resolve(authority, this.path), query);
        }

        @Override
        Urin resolve(final Scheme scheme, final Authority authority, final Path path) {
            return new UrinWithAuthorityAndPathAndQuery(scheme.removeDefaultPort(), scheme.normalise(this.authority), this.path, query);
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart) {
            return scheme.urin(hierarchicalPart.resolve(authority, path), query);
        }

        @Override
        Urin resolve(final Scheme scheme, final Path path, final Query query) {
            return scheme.urin(hierarchicalPart(path).resolve(authority, this.path), this.query);
        }

        @Override
        Urin resolve(final Scheme scheme, final Authority authority, final Path path, final Query query) {
            return new UrinWithAuthorityAndPathAndQuery(scheme.removeDefaultPort(), scheme.normalise(this.authority), this.path, this.query);
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query) {
            return scheme.urin(hierarchicalPart.resolve(authority, path), this.query);
        }

        @Override
        Urin resolve(final Scheme scheme, final Path path, final Query query, final Fragment fragment) {
            return scheme.urin(hierarchicalPart(path).resolve(authority, this.path), this.query);
        }

        @Override
        Urin resolve(final Scheme scheme, final Authority authority, final Path path, final Query query, final Fragment fragment) {
            return new UrinWithAuthorityAndPathAndQuery(scheme.removeDefaultPort(), scheme.normalise(this.authority), this.path, this.query);
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query, final Fragment fragment) {
            return scheme.urin(hierarchicalPart.resolve(authority, path), this.query);
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

    private static final class RelativeReferenceWithAuthorityAndFragment extends RelativeReference {
        private final Authority authority;
        private final Path path;
        private final Fragment fragment;

        RelativeReferenceWithAuthorityAndFragment(final Authority authority, final Path path, final Fragment fragment) {
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
        public Path path() {
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
        public Query query() {
            throw new UnsupportedOperationException("Attempt to get query from a UrinReference that does not have one.");
        }

        @Override
        Urin resolve(final Scheme scheme, final Path path) {
            return scheme.urin(hierarchicalPart(path).resolve(authority, this.path), fragment);
        }

        @Override
        Urin resolve(final Scheme scheme, final Authority authority, final Path path) {
            return new UrinWithAuthorityAndPathAndFragment(scheme.removeDefaultPort(), scheme.normalise(this.authority), this.path, fragment);
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart) {
            return scheme.urin(hierarchicalPart.resolve(authority, path), fragment);
        }

        @Override
        Urin resolve(final Scheme scheme, final Path path, final Query query) {
            return scheme.urin(hierarchicalPart(path).resolve(authority, this.path), fragment);
        }

        @Override
        Urin resolve(final Scheme scheme, final Authority authority, final Path path, final Query query) {
            return new UrinWithAuthorityAndPathAndFragment(scheme.removeDefaultPort(), scheme.normalise(this.authority), this.path, fragment);
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query) {
            return scheme.urin(hierarchicalPart.resolve(authority, path), fragment);
        }

        @Override
        Urin resolve(final Scheme scheme, final Path path, final Query query, final Fragment fragment) {
            return scheme.urin(hierarchicalPart(path).resolve(authority, this.path), this.fragment);
        }

        @Override
        Urin resolve(final Scheme scheme, final Authority authority, final Path path, final Query query, final Fragment fragment) {
            return scheme.urin(backdoorHierarchicalPart(authority, path).resolve(this.authority, this.path), this.fragment);
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query, final Fragment fragment) {
            return scheme.urin(hierarchicalPart.resolve(authority, path), this.fragment);
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

    private static final class RelativeReferenceWithAuthorityAndQueryAndFragment extends RelativeReference {
        private final Authority authority;
        private final Path path;
        private final Query query;
        private final Fragment fragment;

        RelativeReferenceWithAuthorityAndQueryAndFragment(final Authority authority, final Path path, final Query query, final Fragment fragment) {
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
        public Path path() {
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
        public Query query() {
            return query;
        }

        @Override
        Urin resolve(final Scheme scheme, final Path path) {
            return new UrinWithAuthorityAndPathAndQueryAndFragment(scheme.removeDefaultPort(), scheme.normalise(authority), this.path, query, fragment);
        }

        @Override
        Urin resolve(final Scheme scheme, final Authority authority, final Path path) {
            return scheme.urin(backdoorHierarchicalPart(authority, path).resolve(this.authority, this.path), query, fragment);
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart) {
            return scheme.urin(hierarchicalPart.resolve(authority, path), query, fragment);
        }

        @Override
        Urin resolve(final Scheme scheme, final Path path, final Query query) {
            return new UrinWithAuthorityAndPathAndQueryAndFragment(scheme.removeDefaultPort(), scheme.normalise(authority), this.path, this.query, fragment);
        }

        @Override
        Urin resolve(final Scheme scheme, final Authority authority, final Path path, final Query query) {
            return scheme.urin(backdoorHierarchicalPart(authority, path).resolve(this.authority, this.path), this.query, fragment);
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query) {
            return scheme.urin(hierarchicalPart.resolve(authority, path), this.query, fragment);
        }

        @Override
        Urin resolve(final Scheme scheme, final Path path, final Query query, final Fragment fragment) {
            return new UrinWithAuthorityAndPathAndQueryAndFragment(scheme.removeDefaultPort(), scheme.normalise(authority), this.path, this.query, this.fragment);
        }

        @Override
        Urin resolve(final Scheme scheme, final Authority authority, final Path path, final Query query, final Fragment fragment) {
            return scheme.urin(backdoorHierarchicalPart(authority, path).resolve(this.authority, this.path), this.query, this.fragment);
        }

        @Override
        Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query, final Fragment fragment) {
            return scheme.urin(hierarchicalPart.resolve(authority, path), this.query, this.fragment);
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

    private static final class UrinWithHierarchicalPartAndQueryAndFragment extends Urin {
        private final Scheme scheme;
        private final HierarchicalPart hierarchicalPart;
        private final Query query;
        private final Fragment fragment;

        UrinWithHierarchicalPartAndQueryAndFragment(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query, final Fragment fragment) {
            if (scheme == null) {
                throw new NullPointerException("Cannot instantiate Urin with null scheme");
            }
            this.scheme = scheme;
            if (hierarchicalPart == null) {
                throw new NullPointerException("Cannot instantiate Urin with null hierarchicalPart");
            }
            this.hierarchicalPart = hierarchicalPart;
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
            return scheme.asString() + ':' + hierarchicalPart.asString() + '?' + query.asString() + '#' + fragment.asString();
        }

        @Override
        public Path path() {
            return hierarchicalPart.path();
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
        public Query query() {
            return query;
        }

        @Override
        public Urin resolve(final UrinReference urinReference) {
            return urinReference.resolve(scheme, hierarchicalPart, query, fragment);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            UrinWithHierarchicalPartAndQueryAndFragment that = (UrinWithHierarchicalPartAndQueryAndFragment) o;
            return fragment.equals(that.fragment)
                    && hierarchicalPart.equals(that.hierarchicalPart)
                    && query.equals(that.query)
                    && scheme.equals(that.scheme);
        }

        @Override
        public int hashCode() {
            int result = scheme.hashCode();
            result = 31 * result + hierarchicalPart.hashCode();
            result = 31 * result + query.hashCode();
            result = 31 * result + fragment.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "Urin{" +
                    "scheme=" + scheme +
                    ", hierarchicalPart=" + hierarchicalPart +
                    ", query=" + query +
                    ", fragment=" + fragment +
                    '}';
        }
    }

    private static final class UrinWithPathAndQueryAndFragment extends Urin {
        private final Scheme scheme;
        private final Path path;
        private final Query query;
        private final Fragment fragment;

        UrinWithPathAndQueryAndFragment(final Scheme scheme, final Path path, final Query query, final Fragment fragment) {
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
        public Path path() {
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
        public Query query() {
            return query;
        }

        @Override
        public Urin resolve(final UrinReference urinReference) {
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

    private static final class UrinWithAuthorityAndPathAndQueryAndFragment extends Urin {
        private final Scheme scheme;
        private final Authority authority;
        private final Path path;
        private final Query query;
        private final Fragment fragment;

        UrinWithAuthorityAndPathAndQueryAndFragment(final Scheme scheme, final Authority authority, final Path path, final Query query, final Fragment fragment) {
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
        public Path path() {
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
        public Query query() {
            return query;
        }

        @Override
        public Urin resolve(final UrinReference urinReference) {
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

    private static final class UrinWithHierarchicalPartAndQuery extends Urin {
        private final Scheme scheme;
        private final HierarchicalPart hierarchicalPart;
        private final Query query;

        UrinWithHierarchicalPartAndQuery(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query) {
            if (scheme == null) {
                throw new NullPointerException("cannot instantiate Urin with null scheme");
            }
            this.scheme = scheme;
            if (hierarchicalPart == null) {
                throw new NullPointerException("cannot instantiate Urin with null hierarchicalPart");
            }
            this.hierarchicalPart = hierarchicalPart;
            if (query == null) {
                throw new NullPointerException("cannot instantiate Urin with null query");
            }
            this.query = query;
        }

        @Override
        public String asString() {
            return scheme.asString() + ':' + hierarchicalPart.asString() + '?' + query.asString();
        }

        @Override
        public Path path() {
            return hierarchicalPart.path();
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
        public Query query() {
            return query;
        }

        @Override
        public Urin resolve(final UrinReference urinReference) {
            return urinReference.resolve(scheme, hierarchicalPart, query);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            UrinWithHierarchicalPartAndQuery that = (UrinWithHierarchicalPartAndQuery) o;
            return hierarchicalPart.equals(that.hierarchicalPart)
                    && query.equals(that.query)
                    && scheme.equals(that.scheme);
        }

        @Override
        public int hashCode() {
            int result = scheme.hashCode();
            result = 31 * result + hierarchicalPart.hashCode();
            result = 31 * result + query.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "Urin{" +
                    "scheme=" + scheme +
                    ", hierarchicalPart=" + hierarchicalPart +
                    ", query=" + query +
                    '}';
        }
    }

    private static final class UrinWithPathAndQuery extends Urin {
        private final Scheme scheme;
        private final Path path;
        private final Query query;

        UrinWithPathAndQuery(final Scheme scheme, final Path path, final Query query) {
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
        public Path path() {
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
        public Query query() {
            return query;
        }

        @Override
        public Urin resolve(final UrinReference urinReference) {
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

    private static final class UrinWithAuthorityAndPathAndQuery extends Urin {
        private final Scheme scheme;
        private final Authority authority;
        private final Path path;
        private final Query query;

        UrinWithAuthorityAndPathAndQuery(final Scheme scheme, final Authority authority, final Path path, final Query query) {
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
        public Path path() {
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
        public Query query() {
            return query;
        }

        @Override
        public Urin resolve(final UrinReference urinReference) {
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

    private static final class UrinWithHierarchicalPartAndFragment extends Urin {
        private final Scheme scheme;
        private final HierarchicalPart hierarchicalPart;
        private final Fragment fragment;

        UrinWithHierarchicalPartAndFragment(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Fragment fragment) {
            if (scheme == null) {
                throw new NullPointerException("cannot instantiate Urin with null scheme");
            }
            this.scheme = scheme;
            if (hierarchicalPart == null) {
                throw new NullPointerException("cannot instantiate Urin with null hierarchicalPart");
            }
            this.hierarchicalPart = hierarchicalPart;
            if (fragment == null) {
                throw new NullPointerException("cannot instantiate Urin with null fragment");
            }
            this.fragment = fragment;
        }

        @Override
        public String asString() {
            return scheme.asString() + ':' + hierarchicalPart.asString() + '#' + fragment.asString();
        }

        @Override
        public Path path() {
            return hierarchicalPart.path();
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
        public Query query() {
            throw new UnsupportedOperationException("Attempt to get query from a UrinReference that does not have one.");
        }

        @Override
        public Urin resolve(final UrinReference urinReference) {
            return urinReference.resolve(scheme, hierarchicalPart);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            UrinWithHierarchicalPartAndFragment that = (UrinWithHierarchicalPartAndFragment) o;
            return fragment.equals(that.fragment)
                    && hierarchicalPart.equals(that.hierarchicalPart)
                    && scheme.equals(that.scheme);
        }

        @Override
        public int hashCode() {
            int result = scheme.hashCode();
            result = 31 * result + hierarchicalPart.hashCode();
            result = 31 * result + fragment.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "Urin{" +
                    "scheme=" + scheme +
                    ", hierarchicalPart=" + hierarchicalPart +
                    ", fragment=" + fragment +
                    '}';
        }
    }

    private static final class UrinWithAuthorityAndPathAndFragment extends Urin {
        private final Scheme scheme;
        private final Authority authority;
        private final Path path;
        private final Fragment fragment;

        UrinWithAuthorityAndPathAndFragment(final Scheme scheme, final Authority authority, final Path path, final Fragment fragment) {
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
        public Path path() {
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
        public Query query() {
            throw new UnsupportedOperationException("Attempt to get query from a UrinReference that does not have one.");
        }

        @Override
        public Urin resolve(final UrinReference urinReference) {
            return urinReference.resolve(scheme, authority, path);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            UrinWithAuthorityAndPathAndFragment that = (UrinWithAuthorityAndPathAndFragment) o;

            if (!authority.equals(that.authority)) return false;
            if (!fragment.equals(that.fragment)) return false;
            if (!path.equals(that.path)) return false;
            if (!scheme.equals(that.scheme)) return false;

            return true;
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

    private static final class UrinWithPathAndFragment extends Urin {
        private final Scheme scheme;
        private final Path path;
        private final Fragment fragment;

        UrinWithPathAndFragment(final Scheme scheme, final Path path, final Fragment fragment) {
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
        public Path path() {
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
        public Query query() {
            throw new UnsupportedOperationException("Attempt to get query from a UrinReference that does not have one.");
        }

        @Override
        public Urin resolve(final UrinReference urinReference) {
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

    private static class UrinWithHierarchicalPart extends Urin {
        private final Scheme scheme;
        private final HierarchicalPart hierarchicalPart;

        public UrinWithHierarchicalPart(final Scheme scheme, final HierarchicalPart hierarchicalPart) {
            if (scheme == null) {
                throw new NullPointerException("cannot instantiate Urin with null scheme");
            }
            this.scheme = scheme;
            if (hierarchicalPart == null) {
                throw new NullPointerException("cannot instantiate Urin with null hierarchicalPart");
            }
            this.hierarchicalPart = hierarchicalPart;
        }

        @Override
        public String asString() {
            return scheme.asString() + ':' + hierarchicalPart.asString();
        }

        @Override
        public Path path() {
            return hierarchicalPart.path();
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
        public Query query() {
            throw new UnsupportedOperationException("Attempt to get query from a UrinReference that does not have one.");
        }

        @Override
        public Urin resolve(final UrinReference urinReference) {
            return urinReference.resolve(scheme, hierarchicalPart);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            UrinWithHierarchicalPart that = (UrinWithHierarchicalPart) o;
            return hierarchicalPart.equals(that.hierarchicalPart)
                    && scheme.equals(that.scheme);
        }

        @Override
        public int hashCode() {
            int result = scheme.hashCode();
            result = 31 * result + hierarchicalPart.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "Urin{" +
                    "scheme=" + scheme +
                    ", hierarchicalPart=" + hierarchicalPart +
                    '}';
        }
    }

    private static class UrinWithPath extends Urin {
        private final Scheme scheme;
        private final Path path;

        UrinWithPath(final Scheme scheme, final Path path) {
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
        public Path path() {
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
        public Query query() {
            throw new UnsupportedOperationException("Attempt to get query from a UrinReference that does not have one.");
        }

        @Override
        public Urin resolve(final UrinReference urinReference) {
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

    private static class UrinWithAuthorityAndPath extends Urin {
        private final Scheme scheme;
        private final Authority authority;
        private final Path path;

        UrinWithAuthorityAndPath(final Scheme scheme, final Authority authority, final Path path) {
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
        public Path path() {
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
        public Query query() {
            throw new UnsupportedOperationException("Attempt to get query from a UrinReference that does not have one.");
        }

        @Override
        public Urin resolve(final UrinReference urinReference) {
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
