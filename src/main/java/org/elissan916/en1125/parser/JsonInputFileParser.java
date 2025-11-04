package org.elissan916.en1125.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.elissan916.en1125.data.Tool;
import org.elissan916.en1125.data.ToolInfo;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class JsonInputFileParser {

    private final String toolInfoInputFileName;
    private final String toolInputFileName;

    private ToolInfo[] toolInfos = null;
    private Tool[] tools = null;

    public ToolInfo[] getToolInfoArray(){
        return toolInfos;
    }

    public Tool[] getToolArray(){
        return tools;
    }

    public JsonInputFileParser(String toolInfoInputFileName, String toolInputFileName) {
        super();

        if (toolInfoInputFileName == null || toolInfoInputFileName.isBlank()) {
            throw new IllegalArgumentException("ToolInfo input info file name cannot be null or blank");
        }
        this.toolInfoInputFileName = toolInfoInputFileName;;

        if (toolInputFileName == null || toolInputFileName.isBlank()) {
            throw new IllegalArgumentException("Tool input file name cannot be null or blank");
        }
        this.toolInputFileName = toolInputFileName;
    }

    public boolean parseToolInfoFile() throws IOException {
        Gson gson = new GsonBuilder().create();
        Path path = new File(toolInfoInputFileName).toPath();

        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            this.toolInfos = gson.fromJson(reader, ToolInfo[].class);
        }
        return true;
    }

    public boolean parseToolFile() throws IOException {
        Path path = new File(this.toolInputFileName).toPath();
        Gson gson = new GsonBuilder().create();

        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            this.tools = gson.fromJson(reader, Tool[].class);
        }
        return true;
    }

}
