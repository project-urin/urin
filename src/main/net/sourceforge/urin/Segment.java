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

import static net.sourceforge.urin.CharacterSetMembershipFunction.P_CHAR;
import static net.sourceforge.urin.PercentEncodingUnaryValue.PercentEncoding.percentEncodingString;
import static net.sourceforge.urin.PercentEncodingUnaryValue.PercentEncoding.specifiedValueEncoding;

/**
 * A segment of a URI's path.
 * <p/>
 * Note that the special segments "." and ".." are defined as constants.  Passing "." or ".." as an argument to the
 * factory method {@link #segment(String)} is not equivalent, as the argument to this method is a literal string, i.e.
 * subject to encoding where necessary.
 * <p/>
 * Immutable and threadsafe.
 *
 * @param <ENCODES> The type of value represented by the segment - {@code String} in the general case.
 * @see <a href="http://tools.ietf.org/html/rfc3986#section-3.3">RFC 3986 - Path</a>
 */
public abstract class Segment<ENCODES> {

    public static final Decoder<Segment<String>, String> BASE_SEGMENT_DECODER = new Decoder<Segment<String>, String>() {
        public Segment<String> decode(final String rawQuery) throws ParseException {
            return segment(SegmentEncodingUnaryValue.PERCENT_ENCODING.decode(rawQuery));
        }
    };

    public static class ValueSegment<ENCODES> extends Segment<ENCODES> {
        private final PercentEncodingUnaryValue<ENCODES> delegate;

        public ValueSegment(ENCODES value, PercentEncodingUnaryValue.PercentEncoding<ENCODES> percentEncoding) {
            this.delegate = new SegmentEncodingUnaryValue<ENCODES>(value, percentEncoding);
        }

        @Override
        public boolean hasValue() {
            return true;
        }

        @Override
        public ENCODES value() {
            return delegate.value;
        }

        public String asString() {
            return delegate.asString();
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ValueSegment that = (ValueSegment) o;
            return delegate.equals(that.delegate);
        }

        @Override
        public int hashCode() {
            return delegate.hashCode();
        }

        @Override
        public String toString() {
            return "Segment{value='" + delegate + "'}";
        }

    }

    private static class SegmentEncodingUnaryValue<ENCODES> extends PercentEncodingUnaryValue<ENCODES> {
        private static final PercentEncoder PERCENT_ENCODER = new PercentEncoder(P_CHAR);

        private static final PercentEncoding<String> PERCENT_ENCODING = specifiedValueEncoding(".",
                specifiedValueEncoding("..",
                        percentEncodingString(PERCENT_ENCODER)));

        public SegmentEncodingUnaryValue(ENCODES value, PercentEncoding<ENCODES> percentEncoding) {
            super(value, percentEncoding);
        }
    }

    /**
     * An empty segment
     */
    public static final Segment EMPTY = segment("");
    /**
     * The segment ".", referring to the current location in the path name hierarchy,
     */
    public static final Segment DOT = new Segment() {
        @Override
        public boolean hasValue() {
            return false;
        }

        @Override
        public String value() {
            throw new UnsupportedOperationException("Attempt to get value of . segment");
        }

        public String asString() {
            return ".";
        }
    };

    /**
     * The segment "..", referring to the parent location in the path name hierarchy,
     */
    public static final Segment DOT_DOT = new Segment<String>() {
        @Override
        public boolean hasValue() {
            return false;
        }

        @Override
        public String value() {
            throw new UnsupportedOperationException("Attempt to get value of .. segment");
        }

        public String asString() {
            return "..";
        }
    };

    private Segment() {
    }

    /**
     * Factory method for creating {@code Segment}s.
     *
     * @param segment any {@code String} to represent as a {@code Segment}.
     * @return a {@code Segment} representing the given {@code String}.
     */
    public static Segment<String> segment(final String segment) {
        return new ValueSegment<String>(segment, SegmentEncodingUnaryValue.PERCENT_ENCODING) {
            @Override
            public boolean hasValue() {
                return true;
            }

            @Override
            public String value() {
                return segment;
            }
        };
    }

    boolean containsColon() {
        return asString().indexOf(':') != -1;
    }

    abstract String asString();

    static <SEGMENT> Segment<SEGMENT> parse(final String encodedSegment, Decoder<Segment<SEGMENT>, String> segmentDecoder) throws ParseException {
        if (".".equals(encodedSegment)) {
            return DOT;
        } else if ("..".equals(encodedSegment)) {
            return
                    DOT_DOT;
        } else {
            return segmentDecoder.decode(encodedSegment);
        }
    }

    /**
     * Returns true if {@code value()} can be called on this {@code Segment}.  This method
     * returns false for . and .. segments.
     *
     * @return true if {@code value()} can be called on this {@code Segment}.
     */
    public abstract boolean hasValue();

    /**
     * Gets the (non-encoded) value of this segment as a {@code String}, if it is a type that has a value, or throws {@code UnsupportedOperationException} otherwise.
     * <p/>
     * Dot segments (. and ..) do not have values, and will throw {@code UnsupportedOperationException}.  This can be tested
     * by equality with the {@code DOT} and {@code DOT_DOT} constants, or by calling {@code hasValue()}.
     *
     * @return the (non-encoded) value of this segment as a {@code String}.
     * @throws UnsupportedOperationException if this is a segment that does not represent a value.
     */
    public abstract ENCODES value();
}
