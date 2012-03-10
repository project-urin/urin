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

import org.sourceforge.xazzle.xhtml.HtmlTag;

import static net.sourceforge.urin.Host.registeredName;
import static net.sourceforge.urin.Segments.segments;
import static net.sourceforge.urin.documentation.UrinPage.aUrinPage;
import static net.sourceforge.urin.documentation.UrinPage.standardJarUrin;
import static net.sourceforge.urin.scheme.Http.http;
import static org.sourceforge.xazzle.xhtml.Href.href;
import static org.sourceforge.xazzle.xhtml.Tags.*;

final class IndexPage {

    private IndexPage() {
    }

    public static HtmlTag indexPage(final String version) {
        return aUrinPage(
                h2Tag(xhtmlText("Introduction")),
                paragraphTag(xhtmlText("Urin is a URI generator and parser for Java.  It is written to make the dynamic generation of " +
                        "URIs easier than it is with Java's built-in URI and URL classes, and to provide support for the current URI standard, "),
                        anchorTag(xhtmlText("RFC 3986")).withHref(href(http(registeredName("tools.ietf.org"), segments("html", "rfc3986")).asString())),
                        xhtmlText(".  It is open source, and free for you to use.")),
                paragraphTag(
                        xhtmlText("The latest version of Urin available for download is "),
                        anchorTag(xhtmlText(version)).withHref(href(standardJarUrin(version).asString())),
                        xhtmlText(".  The "),
                        anchorTag(xhtmlText("javadoc")).withHref(href("javadoc/")),
                        xhtmlText(" is also available online.")
                ),
                h2Tag(xhtmlText("Example")),
                paragraphTag(xhtmlText("A brief example demonstrates the generation of an HTTP URI:")),
                UrinPage.codeBlock("http(\n" +
                        "    registeredName(\"www.example.com\"), \n" +
                        "    segments(\"music\", \"AC/DC\", \"Back in Black\")\n" +
                        ").asString();"),
                paragraphTag(xhtmlText("This produces the "), UrinPage.simpleNameOf(String.class), xhtmlText(" \""), UrinPage.codeSnippet(acDcString()),
                        xhtmlText("\".  Note that the '"), UrinPage.codeSnippet("/"), xhtmlText("' character in \""), UrinPage.codeSnippet("AC/DC"),
                        xhtmlText("\" has been encoded as \""), UrinPage.codeSnippet("%2F"), xhtmlText("\", and that the space characters in \""),
                        UrinPage.codeSnippet("Back in Black"), xhtmlText("\" have been encoded as \""), UrinPage.codeSnippet("%20"), xhtmlText("\".")),
                paragraphTag(xhtmlText("In the above example, the registered name and segments could be any Java "), UrinPage.simpleNameOf(String.class),
                        xhtmlText("s we choose; the library will encode them appropriately to the part of the URI where they appear."))
        );
    }

    private static String acDcString() {
        return
                http(
                        registeredName("www.example.com"),
                        segments("music", "AC/DC", "Back in Black")
                ).asString();
    }

}
