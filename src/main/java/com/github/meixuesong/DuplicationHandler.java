package com.github.meixuesong;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DuplicationHandler extends DefaultHandler {
    private List<DuplicateFile> allDuplicateFiles = new ArrayList<>();
    private List<DuplicateFile> duplicate = new ArrayList<>();
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase("file")) {
            DuplicateFile file = new DuplicateFile(
                    Integer.valueOf(attributes.getValue("column")),
                    Integer.valueOf(attributes.getValue("endcolumn")),
                    Integer.valueOf(attributes.getValue("line")),
                    Integer.valueOf(attributes.getValue("endline")),
                    attributes.getValue("path")
            );

            duplicate.add(file);
        } else if (qName.equalsIgnoreCase("duplication")) {
            duplicate = new ArrayList<>();
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("duplication")) {
            String duplicateFileNames = getDuplicateFileNames(duplicate);
            for (DuplicateFile file : duplicate) {
                file.setGroup(duplicateFileNames);
                allDuplicateFiles.add(file);
            }
        }
    }

    private String getDuplicateFileNames(List<DuplicateFile> files) {
        StringBuilder builder = new StringBuilder();
        for (DuplicateFile file : files) {
            builder.append(file.getFileName() + " ");
        }
        return builder.toString();
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        duplicate = new ArrayList<>();
        allDuplicateFiles = new ArrayList<>();
    }

    public List<DuplicateFile> getDuplicateFiles() {
        return allDuplicateFiles;
    }
}
