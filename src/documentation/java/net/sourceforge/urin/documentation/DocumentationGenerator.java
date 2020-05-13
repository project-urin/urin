/*
 * Copyright 2020 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.urin.documentation;

import net.sourceforge.writexml.CompactXmlFormatter;
import net.sourceforge.writexml.XmlWriteException;
import net.sourceforge.xazzle.xhtml.HtmlTag;

import java.io.*;
import java.util.Properties;

import static java.nio.charset.StandardCharsets.UTF_8;
import static net.sourceforge.urin.documentation.DocumentationPage.documentationPage;
import static net.sourceforge.urin.documentation.DownloadsPage.downloadsPage;
import static net.sourceforge.urin.documentation.IndexPage.indexPage;
import static net.sourceforge.urin.documentation.SupportPage.supportPage;

public final class DocumentationGenerator {

    public static final CompactXmlFormatter XML_FORMATTER = new CompactXmlFormatter();

    private DocumentationGenerator() {
    }

    public static void main(final String... args) throws IOException, XmlWriteException {
        final File destination = new File(args[0]);
        final String version = versionString();

        writePage(indexPage(version), destination, "index.html");
        writePage(downloadsPage(version), destination, "downloads.html");
        writePage(documentationPage(version), destination, "documentation.html");
        writePage(supportPage(), destination, "support.html");
    }

    private static String versionString() throws IOException {
        Properties properties = new Properties();
        try (InputStreamReader reader = new InputStreamReader(new FileInputStream("gradle.properties"), UTF_8)) {
            properties.load(reader);
        }
        return properties.getProperty("majorVersion") + "." + properties.getProperty("minorVersion");
    }

    private static void writePage(final HtmlTag urinPage, final File destination, final String fileName) throws IOException, XmlWriteException {
        final File file = new File(destination, fileName);
        try (Writer fileWriter = new OutputStreamWriter(new FileOutputStream(file), UTF_8)) {
            XML_FORMATTER.write(urinPage.asDocument(), fileWriter);
        }
    }

}
