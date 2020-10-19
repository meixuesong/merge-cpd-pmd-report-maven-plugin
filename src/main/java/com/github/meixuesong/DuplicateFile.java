package com.github.meixuesong;

import java.util.Arrays;
import java.util.List;

public class DuplicateFile {
    private int column;
    private int endcolumn;
    private int line;
    private int endline;
    private String path;
    private String group;

    public DuplicateFile(int column, int endcolumn, int line, int endline, String path) {
        this.column = column;
        this.endcolumn = endcolumn;
        this.line = line;
        this.endline = endline;
        this.path = path;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getEndcolumn() {
        return endcolumn;
    }

    public void setEndcolumn(int endcolumn) {
        this.endcolumn = endcolumn;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getEndline() {
        return endline;
    }

    public void setEndline(int endline) {
        this.endline = endline;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return String.format(
                "<file name=\"%s\">" + System.lineSeparator() +
                "    <violation beginline=\"%d\" endline=\"%d\" begincolumn=\"%d\" endcolumn=\"%d\" rule=\"DuplicateCode\" ruleset=\"Code\"" + System.lineSeparator() +
                "        package=\"%s\" class=\"%s\"" + System.lineSeparator() +
                "        externalInfoUrl=\"\" priority=\"3\">" + System.lineSeparator() +
                "Duplicate code, line: %d - %d, (%s)" + System.lineSeparator() +
                "    </violation>" + System.lineSeparator() +
                "</file>", path, line, endline, column, endcolumn, getPackageName(), getClassName(), line, endline, group);
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getGroup() {
        return group;
    }

    public String getFileName() {
        int i = path.lastIndexOf("/");
        if (i < 0) {
            i = path.lastIndexOf("\\");
        }

        if (i > -1) {
            return path.substring(i + 1);
        } else {
            return path;
        }
    }

    private String getClassName() {
        return getFileName().replace(".java", "");
    }

    private String getPackageName() {
        List<String> keywords = Arrays.asList("/src/main/java/", "/src/test/java/", "\\src\\main\\java\\", "\\src\\test\\java\\");
        for (String keyword : keywords) {
            int i = path.indexOf(keyword);
            if (i > -1) {
                return path.substring(i + 1, path.length() - getFileName().length() - 1)
                        .replace("\\", ".")
                        .replace("/", ".");
            }
        }

        return "";
    }
}
