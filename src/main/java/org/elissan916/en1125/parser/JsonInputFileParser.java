package org.elissan916.en1125.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.elissan916.en1125.data.CheckoutInfo;
import org.elissan916.en1125.data.Tool;
import org.elissan916.en1125.data.ToolInfo;
import org.elissan916.en1125.util.LocalDateTypeAdapter;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public class JsonInputFileParser {

    private final String toolInfoInputFileName;
    private final String toolInputFileName;
    private final String checkoutInfoInputFileName;

    //TODO: fix declarations
    private Map<String,ToolInfo> toolInfoMap = new HashMap<>();
    private Map<String,Tool> toolMap = new HashMap<>();
    private List<CheckoutInfo> checkoutInfoList = new ArrayList<>();

    public Map<String,ToolInfo> getToolInfoMap(){
        return toolInfoMap;
    }

    public Map<String, Tool> getToolMap(){
        return toolMap;
    }

    public List<CheckoutInfo> getCheckoutInfoList(){
        return checkoutInfoList;
    }

    public JsonInputFileParser(String toolInfoInputFileName, String toolInputFileName, String checkoutInfoInputFileName) {
        if (toolInfoInputFileName == null || toolInfoInputFileName.isBlank()) {
            throw new IllegalArgumentException("ToolInfo input info file name cannot be null or blank");
        }
        this.toolInfoInputFileName = toolInfoInputFileName;

        if (toolInputFileName == null || toolInputFileName.isBlank()) {
            throw new IllegalArgumentException("Tool input file name cannot be null or blank");
        }
        this.toolInputFileName = toolInputFileName;

        if (checkoutInfoInputFileName == null || checkoutInfoInputFileName.isBlank()) {
            throw new IllegalArgumentException("CheckoutInfo input file name cannot be null or blank");
        }
        this.checkoutInfoInputFileName = checkoutInfoInputFileName;
    }

    public void parseToolInfoFile() throws IOException {
        Gson gson = new GsonBuilder().create();
        Path path = new File(toolInfoInputFileName).toPath();

        //TODO: Explicitly causing IllegalStateException if toolName is not unique
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            this.toolInfoMap = Arrays.stream(gson.fromJson(reader, ToolInfo[].class))
                    .collect(Collectors.toMap(ToolInfo::toolName, ti -> ti));
        }
    }

    public void parseToolFile() throws IOException {
        Path path = new File(this.toolInputFileName).toPath();
        Gson gson = new GsonBuilder().create();

        //TODO: Explicitly causing IllegalStateException if toolCode is not unique
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            this.toolMap = Arrays.stream(gson.fromJson(reader, Tool[].class))
                    .collect(Collectors.toMap(Tool::toolCode, t -> t));
        }
    }

    public void parseCheckoutInfoFile() throws IOException {
        Path path = new File(checkoutInfoInputFileName).toPath();
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter()).create();
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            this.checkoutInfoList = stream(gson.fromJson(reader, CheckoutInfo[].class))
                    .collect(Collectors.toList());

        }
    }

    public void parseInputFiles() {
        try {
            parseToolInfoFile();
            parseToolFile();
            parseCheckoutInfoFile();
        } catch (IOException e) {
            throw new RuntimeException("Error parsing input files", e);
        }
    }
}
