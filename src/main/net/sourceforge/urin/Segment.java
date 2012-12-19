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
 * Note that the special segments "." and ".." are obtained via the factory methods {@link #dot()} and {@link #dotDot()}
 * respectively.  Passing "." or ".." as an argument to the factory method {@link #segment(String)} is not equivalent,
 * as the argument to this method is a literal string, i.e. subject to encoding where necessary.
 * <p/>
 * Immutable and threadsafe.
 *
 * @param <ENCODES> The type of value represented by the segment - {@code String} in the general case.
 * @see <a href="http://tools.ietf.org/html/rfc3986#section-3.3">RFC 3986 - Path</a>
 */
public abstract class Segment<ENCODES> {

    private static final PercentEncodingUnaryValue.PercentEncoding<String> PERCENT_ENCODING = specifiedValueEncoding(".",
            specifiedValueEncoding("..",
                    percentEncodingString(new PercentEncoder(P_CHAR))));

    /**
     * The segment ".", referring to the current location in the path name hierarchy,
     */
    public static <ENCODES> Segment<ENCODES> dot() {
        return new Segment<ENCODES>() {
            @Override
            public final boolean hasValue() {
                return false;
            }

            @Override
            public final ENCODES value() {
                throw new UnsupportedOperationException("Attempt to get value of . segment");
            }

            final String asString() {
                return ".";
            }

            @Override
            final boolean isEmpty() {
                return false;
            }

            @Override
            public final boolean equals(final Object o) {
                return this == o || !(o == null || getClass() != o.getClass());
            }

            @Override
            public final int hashCode() {
                return 37;
            }

            @Override
            public final String toString() {
                return "Segment{.}";
            }

        };
    }

    /**
     * The segment "..", referring to the parent location in the path name hierarchy,
     */
    public static <ENCODES> Segment<ENCODES> dotDot() {
        return new Segment<ENCODES>() {
            @Override
            public final boolean hasValue() {
                return false;
            }

            @Override
            public final ENCODES value() {
                throw new UnsupportedOperationException("Attempt to get value of .. segment");
            }

            public final String asString() {
                return "..";
            }

            @Override
            final boolean isEmpty() {
                return false;
            }

            @Override
            public final boolean equals(final Object o) {
                return this == o || !(o == null || getClass() != o.getClass());
            }

            @Override
            public final int hashCode() {
                return 17;
            }

            @Override
            public final String toString() {
                return "Segment{..}";
            }

        };
    }

    /**
     * An empty segment - one that is encoded as "" in a URI.
     */
    public static <ENCODES> Segment<ENCODES> empty() {
        return new Segment<ENCODES>() {
            @Override
            public final boolean hasValue() {
                return false;
            }

            @Override
            public final ENCODES value() {
                throw new UnsupportedOperationException("Attempt to get value of empty segment");
            }

            public final String asString() {
                return "";
            }

            @Override
            final boolean isEmpty() {
                return true;
            }

            @Override
            public final boolean equals(final Object o) {
                return this == o || !(o == null || getClass() != o.getClass());
            }

            @Override
            public final int hashCode() {
                return 19;
            }

            @Override
            public final String toString() {
                return "Segment{empty}";
            }

        };
    }

    public static MakingDecoder<Segment<String>, String, String> stringSegmentMaker() {
        return new MakingDecoder<Segment<String>, String, String>(PercentEncodingUnaryValue.PercentEncodingPartial.<String>noOp()) {
            @Override
            protected Segment<String> makeOne(String value) {
                return segment(value);
            }
        };
    }

    public static class ValueSegment<ENCODES> extends Segment<ENCODES> {
        private final PercentEncodingUnaryValue<ENCODES> delegate;

        protected ValueSegment(ENCODES value, PercentEncodingUnaryValue.PercentEncodingPartial<ENCODES, String> percentEncodingPartial) {
            final PercentEncodingUnaryValue.PercentEncoding<ENCODES> apply = percentEncodingPartial.apply(PERCENT_ENCODING);
            this.delegate = new SegmentEncodingUnaryValue<>(value, apply);
        }

        @Override
        public final boolean hasValue() {
            return true;
        }

        @Override
        public final ENCODES value() {
            return delegate.value;
        }

        public final String asString() {
            return delegate.asString();
        }

        @Override
        final boolean isEmpty() {
            return "".equals(asString());
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

    private static final class SegmentEncodingUnaryValue<ENCODES> extends PercentEncodingUnaryValue<ENCODES> {
        public SegmentEncodingUnaryValue(final ENCODES value, final PercentEncoding<ENCODES> percentEncoding) {
            super(value, percentEncoding);
        }
    }

    private Segment() {
    }

    /**
     * Factory method for creating {@code Segment}s.
     *
     * @param segment any {@code String} to represent as a {@code Segment}.
     * @return a {@code Segment} representing the given {@code String}.
     */
    public static Segment<String> segment(final String segment) {
        return new ValueSegment<String>(segment, PercentEncodingUnaryValue.PercentEncodingPartial.<String>noOp()) {
        };
    }

    final boolean containsColon() {
        return asString().indexOf(':') != -1;
    }

    abstract String asString();

    abstract boolean isEmpty();

    static <SEGMENT> Segment<SEGMENT> parse(final String encodedSegment, final MakingDecoder<Segment<SEGMENT>, ?, String> segmentMakingDecoder) throws ParseException {
        switch (encodedSegment) {
            case "":
                return empty();
            case ".":
                return dot();
            case "..":
                return dotDot();
            default:
                return segmentMakingDecoder.toMaker(PERCENT_ENCODING).make(encodedSegment);
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
