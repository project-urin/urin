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

import net.sourceforge.urin.*;
import net.sourceforge.urin.scheme.Http;
import org.sourceforge.xazzle.xhtml.HtmlTag;

import java.net.URI;

import static net.sourceforge.urin.Authority.authority;
import static net.sourceforge.urin.Fragment.fragment;
import static net.sourceforge.urin.HierarchicalPart.hierarchicalPart;
import static net.sourceforge.urin.Host.registeredName;
import static net.sourceforge.urin.Path.path;
import static net.sourceforge.urin.Path.rootlessPath;
import static net.sourceforge.urin.Port.port;
import static net.sourceforge.urin.Query.query;
import static net.sourceforge.urin.RelativeReference.relativeReference;
import static net.sourceforge.urin.Scheme.scheme;
import static net.sourceforge.urin.Segment.DOT_DOT;
import static net.sourceforge.urin.Segment.segment;
import static net.sourceforge.urin.Urin.urin;
import static net.sourceforge.urin.documentation.UrinPage.*;
import static net.sourceforge.urin.scheme.Http.*;
import static org.sourceforge.xazzle.xhtml.Tags.*;

final class DocumentationPage {

    private DocumentationPage() {
    }

    @SuppressWarnings({"StaticMethodOnlyUsedInOneClass"})
    static HtmlTag documentationPage(final String version) {
        return aUrinPage(
                h2Tag(xhtmlText("Documentation")),
                h3Tag(xhtmlText("Introduction")),
                paragraphTag(
                        xhtmlText("This page provides an example-based guide to Urin. In-depth details of the API are available in the "),
                        anchorTag(xhtmlText("online javadoc")).withHref(href(relativeReference(rootlessPath("javadoc")))),
                        xhtmlText(", which can also be found in the "),
                        anchorTag(xhtmlText("standard jar")).withHref(href(standardJarUrin(version))),
                        xhtmlText(".")
                ),
                h3Tag(xhtmlText("Model of URIs and relative references in Urin")),
                paragraphTag(
                        xhtmlText("The model of URIs and relative references in Urin reflects that defined in "), RFC_3986, xhtmlText(".  The RFC " +
                        "defines two top level structures:")
                ),
                unorderedListTag(
                        listItemTag(quotationTag(xhtmlText("URI")), xhtmlText(" - for example "), codeSnippet(http(registeredName("www.example.com"), path("index")).asString())),
                        listItemTag(quotationTag(xhtmlText("Relative reference")), xhtmlText(" - sometimes loosely called a "), quotationTag(xhtmlText("relative URI")), xhtmlText("; for example "), codeSnippet(relativeReference(path("index")).asString()))
                ),
                paragraphTag(
                        xhtmlText("These are modelled in Urin as "), simpleNameOf(Urin.class), xhtmlText(" and "), simpleNameOf(RelativeReference.class),
                        xhtmlText(" respectively.  These classes provide factory methods that allow any valid URI or relative reference to be generated.")
                ),
                h3Tag(xhtmlText("Producing URIs and relative references")),
                paragraphTag(
                        xhtmlText("Producing a URI is simply a case of calling the relevant static factory method on the "), simpleNameOf(Urin.class),
                        xhtmlText(" class, for example:")
                ),
                codeBlock("urin(\n" +
                        "        scheme(\"ftp\"),\n" +
                        "        hierarchicalPart(\n" +
                        "                authority(registeredName(\"ftp.is.co.za\")),\n" +
                        "                path(\"rfc\", \"rfc1808.txt\"))\n" +
                        ").asString();"),
                paragraphTag(
                        xhtmlText("Generates the "), simpleNameOf(String.class), xhtmlText(" "), codeSnippet(simpleUrinExample()), xhtmlText(".")
                ),
                paragraphTag(
                        xhtmlText("It is also possible to generate an instance of "), canonicalNameOf(URI.class), xhtmlText(", like so:")
                ),
                codeBlock("urin(\n" +
                        "        scheme(\"mailto\"),\n" +
                        "        hierarchicalPart(\n" +
                        "                rootlessPath(\"John.Doe@example.com\")\n" +
                        "        )\n" +
                        ").asUri();"),
                paragraphTag(
                        xhtmlText("This produces "), codeSnippet(simpleUrinToUriExample().toString()),
                        xhtmlText(".  Note, however, that "), canonicalNameOf(URI.class), xhtmlText(" implements the obsoleted "),
                        anchorTag(xhtmlText("RFC 2396")).withHref(href(http(registeredName("tools.ietf.org"), path("html", "rfc2396")))),
                        xhtmlText(", meaning there are certain valid URIs which can be produced using Urin, but which can't be represented " +
                                "by "), canonicalNameOf(URI.class), xhtmlText(".")
                ),
                paragraphTag(
                        xhtmlText("Generating a relative reference follows the same pattern, for example:")
                ),
                codeBlock("relativeReference(\n" +
                        "        rootlessPath(DOT_DOT, segment(\"sibling\")),\n" +
                        "        query(\"some-query\")\n" +
                        ").asString();"),
                paragraphTag(
                        xhtmlText("This rerturns a "), simpleNameOf(String.class), xhtmlText(" containing "), codeSnippet(simpleRelativeReferenceExample()),
                        xhtmlText(". It is possible to retrieve this as a "), canonicalNameOf(URI.class), xhtmlText(" in the same way " +
                        "as for a "), canonicalNameOf(Urin.class), xhtmlText(", by calling the "), codeSnippet("asUri()"), xhtmlText("method.  " +
                        "Of note in this example:")
                ),
                unorderedListTag(
                        listItemTag(xhtmlText("The path component is created using the "), codeSnippet("rootlessPath"), xhtmlText(" method, to create a path without a leading '"),
                                codeSnippet("/"), xhtmlText("' character.")),
                        listItemTag(xhtmlText("The \""), codeSnippet(".."), xhtmlText("\" part of the output was generated using the "), codeSnippet("DOT_DOT"),
                                xhtmlText(" constant.  This is because we're using \""), codeSnippet(".."), xhtmlText("\" with the special meaning 'parent'."))
                ),
                h3Tag(xhtmlText("Producing HTTP and HTTPS URIs and relative references")),
                paragraphTag(
                        xhtmlText("Urin provides specific support for generating HTTP and HTTPS URIs for convenience, and to implement " +
                                "the rules and encoding specified in addition to those specified for general URIs.  These are implemented in " +
                                "the "), simpleNameOf(Http.class), xhtmlText(" class.  For example:")
                ),
                codeBlock("http(\n" +
                        "        registeredName(\"www.example.com\"),\n" +
                        "        port(80),\n" +
                        "        path(\"music\", \"AC/DC\", \"Back in Black\"),\n" +
                        "        queryParameters(\n" +
                        "                queryParameter(\"track\", \"Hells Bells\"),\n" +
                        "                queryParameter(\"version\", \"Radio edit\")\n" +
                        "        ),\n" +
                        "        fragment(\"verse 2\")\n" +
                        ").asString();"),
                paragraphTag(
                        xhtmlText("Generates the "), simpleNameOf(String.class), xhtmlText(" "), codeSnippet(httpExample()), xhtmlText(".  Notice " +
                        "a number of HTTP specific features:")
                ),
                unorderedListTag(
                        listItemTag(xhtmlText("The port has been elided because port 80 is the default for HTTP")),
                        listItemTag(xhtmlText("HTTP query parameter separators have been inserted in the query component")),
                        listItemTag(xhtmlText("Spaces in the query component have been encoded as \""), codeSnippet("+"),
                                xhtmlText("\", rather than \""), codeSnippet("%20"), xhtmlText("\""))
                ),
                paragraphTag(
                        xhtmlText("An equivalient set of methods for generating HTTPS URIs also exist on the "), simpleNameOf(Http.class),
                        xhtmlText(" class.")
                ),
                h3Tag(xhtmlText("Parsing")),
                paragraphTag(
                        xhtmlText("The "), simpleNameOf(UrinReference.class), xhtmlText(", "), simpleNameOf(Urin.class),
                        xhtmlText(", and "), simpleNameOf(RelativeReference.class), xhtmlText(" classes all implement a static "),
                        codeSnippet("parse"), xhtmlText(" method to produce an instance of their respective types from a "), simpleNameOf(String.class),
                        xhtmlText(".  For example, we can parse the URI "), codeSnippet(parseExample()), xhtmlText(" as follows:")
                ),
                codeBlock("try {\n" +
                        "    parsedUrin = Urin.parse(\"ldap://[2001:db8::7]/c=GB?objectClass?one\");\n" +
                        "} catch (ParseException e) {\n" +
                        "    // handle parse failure\n" +
                        "}"),
                paragraphTag(
                        xhtmlText("Where "), simpleNameOf(ParseException.class), xhtmlText(" would be thrown if the "), simpleNameOf(String.class),
                        xhtmlText(" wasn't a valid URI.")
                ),
                h3Tag(xhtmlText("Normalisation")),
                paragraphTag(RFC_3986, xhtmlText(" specifies a number of methods of URI normalisation, such as the handling of " +
                        "unnecessary encoding and non-preferred case, and the handling of "), codeSnippet("."), xhtmlText(" and "), codeSnippet("..")
                        , xhtmlText(" segments, which are applied " +
                        "by Urin.  For example:")),
                codeBlock("Urin.parse(\"HTTP://www.example.com/.././some%20pat%68\").asString()"),
                paragraphTag(xhtmlText("Returns the "), simpleNameOf(String.class), xhtmlText(" "), codeSnippet(normalisationExample()),
                        xhtmlText(", as a result of normalisation rules having been applied.")),
                h3Tag(xhtmlText("Resolution")),
                paragraphTag(xhtmlText("Relative references can be turned into URIs by resolving them, relative to a context URI.  " +
                        "In Urin, this is achieved using the "), codeSnippet("resolve"), xhtmlText(" method on "), simpleNameOf(Urin.class),
                        xhtmlText(", for example:")),
                codeBlock("urin(\n" +
                        "        scheme(\"http\"),\n" +
                        "        hierarchicalPart(\n" +
                        "                authority(registeredName(\"www.example.com\")),\n" +
                        "                path(\"child-1\")\n" +
                        "        )\n" +
                        ").resolve(\n" +
                        "        relativeReference(\n" +
                        "                rootlessPath(DOT_DOT, segment(\"child-2\")),\n" +
                        "                query(\"extra-query\")\n" +
                        "        )\n" +
                        ").asString();"),
                paragraphTag(xhtmlText("This returns the "), simpleNameOf(String.class), xhtmlText(" "), codeSnippet(resolutionExample()),
                        xhtmlText(".")),
                h3Tag(xhtmlText("Implementing scheme-specific rules")),
                paragraphTag(
                        xhtmlText("Urin also provides a mechanism for schemes with extra rules to be implemented, as demonstrated in the "),
                        simpleNameOf(Http.class), xhtmlText(" class, which handles the non-default encoding of the space character, and the " +
                        "encoding of parameters in the query component of the HTTP scheme.")
                ),
                paragraphTag(
                        xhtmlText("This is achieved by extending the "), simpleNameOf(Query.class), xhtmlText(" class.  " +
                        "The source code for "), codeSnippet("Http.HttpQuery"), xhtmlText(" has an example of this in action.")
                )
        );
    }

    private static String simpleUrinExample() {
        return
                urin(
                        scheme("ftp"),
                        hierarchicalPart(
                                authority(registeredName("ftp.is.co.za")),
                                path("rfc", "rfc1808.txt"))
                ).asString();
    }

    private static URI simpleUrinToUriExample() {
        return
                urin(
                        scheme("mailto"),
                        hierarchicalPart(
                                rootlessPath("John.Doe@example.com")
                        )
                ).asUri();
    }

    private static String simpleRelativeReferenceExample() {
        return
                relativeReference(
                        rootlessPath(DOT_DOT, segment("sibling")),
                        query("some-query")
                ).asString();
    }

    private static String httpExample() {
        return
                http(
                        registeredName("www.example.com"),
                        port(80),
                        path("music", "AC/DC", "Back in Black"),
                        queryParameters(
                                queryParameter("track", "Hells Bells"),
                                queryParameter("version", "Radio edit")
                        ),
                        fragment("verse 2")
                ).asString();
    }

    private static String parseExample() {
        Urin parsedUrin = null;
        try {
            parsedUrin = Urin.parse("ldap://[2001:db8::7]/c=GB?objectClass?one");
        } catch (ParseException e) {
            // handle parse failure
        }
        if (parsedUrin == null) {
            throw new DocumentationGenerationException("Something bad has happened - we didn't successfully generate a Urin.");
        } else {
            return parsedUrin.asString();
        }
    }

    private static String normalisationExample() {
        try {
            return
                    Urin.parse("HTTP://www.example.com/.././some%20pat%68").asString();
        } catch (ParseException e) {
            throw new DocumentationGenerationException(e);
        }
    }

    private static String resolutionExample() {
        return
urin(
        scheme("http"),
        hierarchicalPart(
                authority(registeredName("www.example.com")),
                path("child-1")
        )
).resolve(
        relativeReference(
                rootlessPath(DOT_DOT, segment("child-2")),
                query("extra-query")
        )
).asString();
    }
}