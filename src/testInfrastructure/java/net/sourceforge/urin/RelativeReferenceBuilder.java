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

import static net.sourceforge.urin.AuthorityBuilder.anAuthority;
import static net.sourceforge.urin.FragmentBuilder.aFragment;
import static net.sourceforge.urin.PathBuilder.anUnpollutedAbsolutePath;
import static net.sourceforge.urin.PathBuilder.anUnpollutedPath;
import static net.sourceforge.urin.QueryBuilder.aQuery;

public final class RelativeReferenceBuilder {

    private static final RandomSupplierSwitcher<RelativeReference<String, Query<String>, Fragment<String>>> RANDOM_UNPOLLUTED_RELATIVE_REFERENCE_SUPPLIER_SWITCHER = new RandomSupplierSwitcher<>(
            () -> SchemeBuilder.aScheme().relativeReference(),
            () -> SchemeBuilder.aScheme().relativeReference(aFragment()),
            () -> SchemeBuilder.aScheme().relativeReference(aQuery()),
            () -> SchemeBuilder.aScheme().relativeReference(aQuery(), aFragment()),
            () -> SchemeBuilder.aScheme().relativeReference(anUnpollutedPath()),
            () -> SchemeBuilder.aScheme().relativeReference(anUnpollutedPath(), aFragment()),
            () -> SchemeBuilder.aScheme().relativeReference(anUnpollutedPath(), aQuery()),
            () -> SchemeBuilder.aScheme().relativeReference(anUnpollutedPath(), aQuery(), aFragment()),
            () -> SchemeBuilder.aScheme().relativeReference(anAuthority()),
            () -> SchemeBuilder.aScheme().relativeReference(anAuthority(), aFragment()),
            () -> SchemeBuilder.aScheme().relativeReference(anAuthority(), aQuery()),
            () -> SchemeBuilder.aScheme().relativeReference(anAuthority(), aQuery(), aFragment()),
            () -> SchemeBuilder.aScheme().relativeReference(anAuthority(), anUnpollutedAbsolutePath()),
            () -> SchemeBuilder.aScheme().relativeReference(anAuthority(), anUnpollutedAbsolutePath(), aFragment()),
            () -> SchemeBuilder.aScheme().relativeReference(anAuthority(), anUnpollutedAbsolutePath(), aQuery()),
            () -> SchemeBuilder.aScheme().relativeReference(anAuthority(), anUnpollutedAbsolutePath(), aQuery(), aFragment())
    );

    private RelativeReferenceBuilder() {
    }

    public static RelativeReference<String, Query<String>, Fragment<String>> anUnpollutedRelativeReference() {
        return RANDOM_UNPOLLUTED_RELATIVE_REFERENCE_SUPPLIER_SWITCHER.get();
    }
}
