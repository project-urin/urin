/*
 * Copyright 2012 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.urin.documentation;

import net.sourceforge.urin.UrinReference;
import org.sourceforge.xazzle.xhtml.HtmlTag;

import static net.sourceforge.urin.Path.rootlessPath;
import static net.sourceforge.urin.RelativeReference.relativeReference;
import static net.sourceforge.urin.documentation.UrinPage.*;
import static org.sourceforge.xazzle.xhtml.Tags.*;

final class DocumentationPage {

    private DocumentationPage() {
    }

    @SuppressWarnings({"StaticMethodOnlyUsedInOneClass"})
    static HtmlTag documentationPage(final String version) {
        UrinReference urinReference = standardJarUrin(version);
        return aUrinPage(
                h2Tag(xhtmlText("Documentation")),
                h3Tag(xhtmlText("Introduction")),
                paragraphTag(
                        xhtmlText("This page provides an example-based guide to Urin. In-depth details of the API are available in the "),
                        anchorTag(xhtmlText("online javadoc")).withHref(href(relativeReference(rootlessPath("javadoc")))),
                        xhtmlText(", which can also be found in the "),
                        anchorTag(xhtmlText("standard jar")).withHref(href(urinReference)),
                        xhtmlText(".")
                ),
                h3Tag(xhtmlText("Model of URIs and relative references in Urin")),
                paragraphTag(
                        xhtmlText("There are two top level structures defined in RFC 3986:")
                ),
                h3Tag(xhtmlText("Producing URIs and relative references")),
                paragraphTag(
                        xhtmlText("Lorem ipsum etc etc")
                )
        );
    }

}
