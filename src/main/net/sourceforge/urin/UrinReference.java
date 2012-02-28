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

public abstract class UrinReference {

    UrinReference() {
        // deliberately empty
    }

    public abstract String asString();

    public abstract URI asUri();

    public static UrinReference parse(final String uriReferenceString) throws ParseException {
        if (Urin.isValidUrinString(uriReferenceString)) {
            return Urin.parse(uriReferenceString);
        } else if (RelativeReference.isValidRelativeReferenceString(uriReferenceString)) {
            return RelativeReference.parse(uriReferenceString);
        }
        throw new ParseException("Given String is neither a valid URI nor a valid relative reference [" + uriReferenceString + "].");
    }

    public static UrinReference parse(final URI uriReference) throws ParseException {
        return parse(uriReference.toASCIIString());
    }

    abstract Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart);

    abstract Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query);

    abstract Urin resolve(final Scheme scheme, final HierarchicalPart hierarchicalPart, final Query query, final Fragment fragment);
}