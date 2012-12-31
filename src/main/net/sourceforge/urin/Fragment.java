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

import static net.sourceforge.urin.CharacterSetMembershipFunction.QUERY_AND_FRAGMENT_NON_PERCENT_ENCODED_CHARACTERS;
import static net.sourceforge.urin.PercentEncodingUnaryValue.PercentEncoding.percentEncodingString;

/**
 * A fragment component of a URI.
 * Immutable and threadsafe.
 *
 * @param <ENCODES> The type of value represented by the fragment - {@code String} in the general case.
 * @see <a href="http://tools.ietf.org/html/rfc3986#section-3.5">RFC 3986 - Fragment</a>
 */
public class Fragment<ENCODES> extends PercentEncodingUnaryValue<ENCODES> {

    private static final PercentEncoding<String> PERCENT_ENCODING = percentEncodingString(new PercentEncoder(QUERY_AND_FRAGMENT_NON_PERCENT_ENCODED_CHARACTERS));

    private Fragment(final ENCODES fragment, final PercentEncoding<ENCODES> percentEncoding) {
        super(fragment, percentEncoding);
    }

    /**
     * Constructor for subclasses of {@code Fragment} with scheme specific percent encoding of characters beyond that specified for generic URI {@code Fragment}s.
     *
     * @param value                  the (non encoded) value this object represents.
     * @param percentEncodingPartial the {@code PercentEncodingPartial} this subclass will use.
     */
    protected Fragment(final ENCODES value, final PercentEncodingPartial<ENCODES, String> percentEncodingPartial) {
        this(value, percentEncodingPartial.apply(PERCENT_ENCODING));
    }

    /**
     * Factory method for creating {@code Fragment}s.
     *
     * @param fragment any {@code String} to represent as a {@code Fragment}.
     * @return a {@code Fragment} representing the given {@code String}.
     */
    public static Fragment<String> fragment(final String fragment) {
        return new Fragment<>(fragment, PERCENT_ENCODING);
    }

    public static MakingDecoder<Fragment<String>, String, String> stringFragmentMaker() {
        return new MakingDecoder<Fragment<String>, String, String>(PercentEncodingPartial.<String>noOp()) {
            @Override
            protected Fragment<String> makeOne(String value) {
                return fragment(value);
            }
        };
    }

    static <FRAGMENT extends Fragment> FRAGMENT parseFragment(final String fragmentString, MakingDecoder<FRAGMENT, ?, String> fragmentMakingDecoder) throws ParseException {
        return fragmentMakingDecoder.toMaker(PERCENT_ENCODING).make(fragmentString);
    }

    /**
     * Gets the (non-encoded) value of this fragment.
     *
     * @return the (non-encoded) value of this fragment.
     */
    public final ENCODES value() {
        return value;
    }

}
