/*
 * Copyright 2011 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.urin;

import org.sourceforge.writexml.Document;
import org.sourceforge.writexml.JaxpXmlWriter;
import org.sourceforge.writexml.XmlWriterException;
import org.sourceforge.xazzle.xhtml.Href;
import org.sourceforge.xazzle.xhtml.HtmlTag;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import static net.sourceforge.urin.AbsoluteSegments.absoluteSegments;
import static net.sourceforge.urin.Host.registeredName;
import static net.sourceforge.urin.Query.query;
import static net.sourceforge.urin.scheme.Http.http;
import static net.sourceforge.urin.scheme.Http.https;
import static org.sourceforge.xazzle.xhtml.AlternateText.alternateText;
import static org.sourceforge.xazzle.xhtml.Href.href;
import static org.sourceforge.xazzle.xhtml.Id.id;
import static org.sourceforge.xazzle.xhtml.ImageSource.imageSource;
import static org.sourceforge.xazzle.xhtml.MetaContent.metaContent;
import static org.sourceforge.xazzle.xhtml.MetaName.metaName;
import static org.sourceforge.xazzle.xhtml.MimeType.mimeType;
import static org.sourceforge.xazzle.xhtml.Relationship.STYLESHEET;
import static org.sourceforge.xazzle.xhtml.Relationship.relationship;
import static org.sourceforge.xazzle.xhtml.Tags.*;
import static org.sourceforge.xazzle.xhtml.XhtmlDimension.pixels;

public class IndexPage {

    private static final Host SOURCEFORGE = registeredName("sourceforge.net");
    private static final Host W3_JIGSAW = registeredName("jigsaw.w3.org");
    private static final Host W3_WWW = registeredName("www.w3.org");

    public static void main(String[] args) throws IOException, XmlWriterException {
        Properties properties = new Properties();
        properties.load(new FileReader("version.properties"));
        final String version = properties.getProperty("urin.version.major") + "." + properties.getProperty("urin.version.minor");
        final Href projectSiteHref = href(http(SOURCEFORGE, absoluteSegments("projects", "urin")).asString());
        final HtmlTag indexPage = htmlTag(
                headTag(
                        titleTag("Urin - A Java library for making URIs"),
                        linkTag()
                                .withRelationships(STYLESHEET)
                                .withMimeType(mimeType("text/css"))
                                .withHref(href("urin.css")),
                        linkTag()
                                .withRelationships(relationship("icon"))
                                .withMimeType(mimeType("image/png"))
                                .withHref(href("favicon-32x32.png")),
                        metaTag(metaName("description"), metaContent("Urin is a replacement for Java's URI and URL classes.  It is free to download and use in your project.")),
                        scriptTag(mimeType("text/javascript"), xhtmlText("  var _gaq = _gaq || [];\n" +
                                "  _gaq.push(['_setAccount', 'UA-16431822-5']);\n" +
                                "  _gaq.push(['_trackPageview']);\n" +
                                "\n" +
                                "  (function() {\n" +
                                "    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;\n" +
                                "    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';\n" +
                                "    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);\n" +
                                "  })();"))
                ),
                bodyTag(
                        divTag(
                                divTag(
                                        h1Tag(xhtmlText("Urin"))
                                ).withId(id("header")),
                                divTag(
                                        unorderedListTag(
                                                listItemTag(
                                                        anchorTag(xhtmlText("Home"))
                                                                .withHref(href("index.html"))
                                                ),
                                                listItemTag(
                                                        anchorTag(xhtmlText("Project Site"))
                                                                .withHref(projectSiteHref)
                                                )
                                        )
                                ).withId(id("navigation")),
                                divTag(
                                        h2Tag(xhtmlText("Introduction")),
                                        paragraphTag(xhtmlText("Urin is an open source Java library for working with URIs. It exists to address some of the difficulties posed by working with Java's built in URI and URL classes. It's " +
                                                "currently in initial development.")),
                                        paragraphTag(
                                                xhtmlText("The latest version of Urin available for download is "),
                                                anchorTag(xhtmlText(version)).withHref(href(https(SOURCEFORGE, absoluteSegments("projects", "urin", "files", "latest")).asString())),
                                                xhtmlText(".  The "),
                                                anchorTag(xhtmlText("javadoc")).withHref(href("javadoc/")),
                                                xhtmlText(" is also available online.")
                                        )
                                ).withId(id("content")),
                                divTag(
                                        unorderedListTag(
                                                listItemTag(
                                                        anchorTag(
                                                                imageTag(
                                                                        imageSource(http(registeredName("sflogo.sourceforge.net"), absoluteSegments("sflogo.php"), query("group_id=605761&type=13")).asString()),
                                                                        alternateText("Get urin at SourceForge.net. Fast, secure and Free Open Source software downloads")
                                                                )
                                                                        .withHeight(pixels("30"))
                                                                        .withWidth(pixels("120"))
                                                        ).withHref(projectSiteHref)
                                                ),
                                                listItemTag(
                                                        anchorTag(
                                                                imageTag(
                                                                        imageSource(http(W3_JIGSAW, absoluteSegments("css-validator", "images", "vcss")).asString()),
                                                                        alternateText("Valid CSS!")
                                                                )
                                                                        .withHeight(pixels("31"))
                                                                        .withWidth(pixels("88"))
                                                        ).withHref(href(http(W3_JIGSAW, absoluteSegments("css-validator", "check", "referer")).asString()))
                                                ),
                                                listItemTag(
                                                        anchorTag(
                                                                imageTag(
                                                                        imageSource(http(W3_WWW, absoluteSegments("Icons", "valid-xhtml10")).asString()),
                                                                        alternateText("Valid XHTML 1.0 Strict")
                                                                )
                                                                        .withHeight(pixels("31"))
                                                                        .withWidth(pixels("88"))
                                                        ).withHref(href(http(registeredName("validator.w3.org"), absoluteSegments("check"), query("uri=referer")).asString()))
                                                )
                                        )
                                ).withId(id("footer"))
                        ).withId(id("root"))
                )
        );
        final File file = new File(args[0], "index.html");
        final FileWriter fileWriter = new FileWriter(file);
        new JaxpXmlWriter(fileWriter).write(new Document(indexPage.asWriteableToXml()));
    }

}
