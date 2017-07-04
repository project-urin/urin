/*
 * Copyright 2017 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.urin;

/**
 * A parser of encoded URI components.
 *
 * @param <NON_ENCODED>   the class of the decoded objects produced.
 * @param <FACTORY_INPUT> the of decoded objects this class uses to construct the final output.
 * @param <ENCODED>       the class of the encoded objects parsed.
 */
public abstract class MakingDecoder<NON_ENCODED, FACTORY_INPUT, ENCODED> {

    private final PercentEncodingPartial<FACTORY_INPUT, ENCODED> percentEncodingPartial;

    protected MakingDecoder(PercentEncodingPartial<FACTORY_INPUT, ENCODED> percentEncodingPartial) {
        this.percentEncodingPartial = percentEncodingPartial;
    }

    protected abstract NON_ENCODED makeOne(FACTORY_INPUT input);

    final Maker<NON_ENCODED> toMaker(final PercentEncodingPartial.PercentEncoding<ENCODED> percentEncoding) {
        return new Maker<NON_ENCODED>() {
            public NON_ENCODED make(String encoded) throws ParseException {
                return makeOne(percentEncodingPartial.apply(percentEncoding).decode(encoded));
            }
        };
    }

}
