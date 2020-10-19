package com.github.meixuesong;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

@Mojo(name = "merge", defaultPhase = LifecyclePhase.POST_SITE)
public class MergeCpdPmdReport extends AbstractMojo {
    @Parameter(defaultValue = "${project.build.directory}", property = "outputDir", required = true)
    private File outputDirectory;

    public void execute() throws MojoExecutionException {
        getLog().info("Merge CPD report into PMD report.");
        File cpdReport = Paths.get(outputDirectory.getAbsolutePath(), "cpd.xml").toFile();
        if (! cpdReport.exists()) {
            getLog().error(cpdReport.getAbsolutePath() + " doesn't exists");
            return;
        }

        File pmdReport = Paths.get(outputDirectory.getAbsolutePath(), "pmd.xml").toFile();
        if (!pmdReport.exists()) {
            getLog().error(pmdReport.getAbsolutePath() + " doesn't exists");
            return;
        }

        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = saxParserFactory.newSAXParser();
            DuplicationHandler handler = new DuplicationHandler();
            saxParser.parse(cpdReport, handler);
            List<DuplicateFile> files = handler.getDuplicateFiles();
            appendCpd2Pmd(pmdReport, getCpdContent(files));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
//
//        File f = outputDirectory;
//
//        if (!f.exists()) {
//            f.mkdirs();
//        }
//
//        File touch = new File(f, "touch.txt");
//
//        FileWriter w = null;
//        try {
//            w = new FileWriter(touch);
//
//            w.write("touch.txt");
//        } catch (IOException e) {
//            throw new MojoExecutionException("Error creating cpdReport " + touch, e);
//        } finally {
//            if (w != null) {
//                try {
//                    w.close();
//                } catch (IOException e) {
//                    // ignore
//                }
//            }
//        }
    }

    private void appendCpd2Pmd(File pmdReport, String cpdContent) throws IOException {
        List<String> lines = Files.readAllLines(pmdReport.toPath(), StandardCharsets.UTF_8);
        for (int i = lines.size() - 1; i >= 0 ; i--) {
            String line = lines.get(i);
            if ("</pmd>".equalsIgnoreCase(line.trim())) {
                lines.remove(i);
            }
        }

        try(BufferedWriter writer =
                    Files.newBufferedWriter(pmdReport.toPath(), StandardCharsets.UTF_8,
                            StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING))
        {
            for (String line : lines) {
                writer.write(line + System.lineSeparator());
            }
            writer.write(cpdContent + System.lineSeparator());
            writer.write("</pmd>");
        }
    }

    private String getCpdContent(List<DuplicateFile> files) {
        StringBuilder result = new StringBuilder();
        for (DuplicateFile file : files) {
            result.append(file.toString() + System.lineSeparator());
        }

        return result.toString();
    }
}
