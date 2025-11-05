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

/**
 * Parser for the project's JSON input files.
 *
 * <p>This helper loads three JSON files used by the application and exposes the
 * parsed results through simple getters:
 * <ul>
 *   <li>Tool info file: an array of {@link ToolInfo} objects (keyed by toolName)</li>
 *   <li>Tool file: an array of {@link Tool} objects (keyed by toolCode)</li>
 *   <li>Checkout info file: an array of {@link CheckoutInfo} objects (order-preserving list)</li>
 * </ul>
 *
 * The parser intentionally performs a modest amount of validation:
 * <ul>
 *   <li>Constructor argument validation (file names cannot be null/blank)</li>
 *   <li>Detection of duplicate map keys when collecting arrays into maps
 *       (throws {@link IllegalStateException})</li>
 *   <li>Cross-file consistency checks: tools must reference a known toolName,
 *       and checkout entries must reference a known toolCode (throws {@link IllegalStateException}).</li>
 * </ul>
 *
 * The public parse methods throw {@link IOException} for file/IO issues and may
 * throw {@link IllegalStateException} when semantic validation fails (duplicate
 * keys or inconsistent references).
 */
public class JsonInputFileParser {

    private final String toolInfoInputFileName;
    private final String toolInputFileName;
    private final String checkoutInfoInputFileName;

    private Map<String,ToolInfo> toolInfoMap = new HashMap<>();
    private Map<String,Tool> toolMap = new HashMap<>();
    private List<CheckoutInfo> checkoutInfoList = new ArrayList<>();

    /**
     * Return the parsed tool-info map keyed by toolName.
     *
     * @return map of toolName -> ToolInfo (may be empty if not yet parsed)
     */
    public Map<String,ToolInfo> getToolInfoMap(){
        return toolInfoMap;
    }

    /**
     * Return the parsed tool map keyed by toolCode.
     *
     * @return map of toolCode -> Tool (may be empty if not yet parsed)
     */
    public Map<String, Tool> getToolMap(){
        return toolMap;
    }

    /**
     * Return the parsed list of checkout instructions in the order they appear
     * in the JSON file.
     *
     * @return list of {@link CheckoutInfo} (may be empty if not yet parsed)
     */
    public List<CheckoutInfo> getCheckoutInfoList(){
        return checkoutInfoList;
    }

    /**
     * Construct a new parser that will read the files at the provided paths.
     *
     * @param toolInfoInputFileName path to a JSON file containing an array of {@link ToolInfo}
     * @param toolInputFileName path to a JSON file containing an array of {@link Tool}
     * @param checkoutInfoInputFileName path to a JSON file containing an array of {@link CheckoutInfo}
     * @throws IllegalArgumentException when any file name is null or blank
     */
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

    /**
     * Parse the tool info JSON file and populate the internal map keyed by
     * {@link ToolInfo#toolName()}.
     *
     * <p>Throws {@link IllegalStateException} when duplicate toolName values are
     * present, and {@link IOException} when the file cannot be read.
     *
     * @throws IllegalStateException when duplicate keys are detected
     * @throws IOException when file IO/parsing fails
     */
    public void parseToolInfoFile() throws IllegalStateException, IOException {
        Gson gson = new GsonBuilder().create();
        Path path = new File(toolInfoInputFileName).toPath();

        //Throws IllegalStateException if toolName is not unique, IOException if parsing is bad
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            this.toolInfoMap = Arrays.stream(gson.fromJson(reader, ToolInfo[].class))
                    .collect(Collectors.toMap(ToolInfo::toolName, ti -> ti));
        } catch (IllegalStateException ise) {
            throw new IllegalStateException("Duplicate toolName found in tool info input file", ise);
        } catch (IOException ioe) {
            throw new IOException("Error parsing tool info input file", ioe);
        }
    }

    /**
     * Parse the tools JSON file and populate the internal map keyed by
     * {@link Tool#toolCode()}.
     *
     * @throws IllegalStateException when duplicate toolCode values are detected
     * @throws IOException when file IO/parsing fails
     */
    public void parseToolFile() throws IllegalStateException, IOException {
        Path path = new File(this.toolInputFileName).toPath();
        Gson gson = new GsonBuilder().create();

        //Throws IllegalStateException if toolCode is not unique, IOException if parsing is bad
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            this.toolMap = Arrays.stream(gson.fromJson(reader, Tool[].class))
                    .collect(Collectors.toMap(Tool::toolCode, t -> t));
        } catch (IllegalStateException ise) {
            throw new IllegalStateException("Duplicate toolCode found in tool input file", ise);
        } catch (IOException ioe) {
            throw new IOException("Error parsing tool input file", ioe);
        }
    }

    /**
     * Parse the checkout info JSON file. Local dates are parsed using
     * {@link org.elissan916.en1125.util.LocalDateTypeAdapter} with the
     * {@code yyyy-MM-dd} format.
     *
     * @throws IOException when file IO/parsing fails
     */
    public void parseCheckoutInfoFile() throws IOException {
        Path path = new File(checkoutInfoInputFileName).toPath();
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter()).create();
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            this.checkoutInfoList = stream(gson.fromJson(reader, CheckoutInfo[].class))
                    .collect(Collectors.toList());

        } catch (IOException ioe) {
            throw new IOException("Error parsing checkout info input file", ioe);
        }
    }

    /**
     * Verify that every {@link Tool} entry references a known {@link ToolInfo}
     * by toolName. Throws {@link IllegalStateException} when a referenced
     * toolName is missing.
     *
     * @throws IllegalStateException when inconsistency is detected
     */
    private void validateToolDataConsistency() throws IllegalStateException {
        for (Tool tool : toolMap.values()) {
            if (!toolInfoMap.containsKey(tool.toolName())) {
                throw new IllegalStateException("Tool with code " + tool.toolCode() + " references unknown toolName " + tool.toolName());
            }
        }
    }

    /**
     * Verify that every {@link CheckoutInfo} entry references a known
     * {@link Tool} by toolCode. Throws {@link IllegalStateException} when a
     * referenced toolCode is missing.
     *
     * @throws IllegalStateException when inconsistency is detected
     */
    private void validateCheckoutDataConsistency() throws IllegalStateException {
        for (CheckoutInfo coInfo : checkoutInfoList) {
            if (!toolMap.containsKey(coInfo.toolCode())) {
                throw new IllegalStateException("CheckoutInfo references unknown toolCode " + coInfo.toolCode());
            }
        }
    }

    /**
     * Convenience method that parses all input files and runs cross-file
     * consistency validation. IO and state validation exceptions are propagated
     * to the caller.
     *
     * Note: methods need to be run in order since later methods depend on the results
     * of the prior method
     *
     * @throws IOException when file IO/parsing fails
     * @throws IllegalStateException when validation fails (duplicates or missing references)
     */
    public void parseInputFiles() throws IOException, IllegalStateException {
        parseToolInfoFile();
        parseToolFile();
        validateToolDataConsistency();
        parseCheckoutInfoFile();
        validateCheckoutDataConsistency();
    }
}
