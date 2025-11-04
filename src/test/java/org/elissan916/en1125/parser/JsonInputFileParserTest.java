package org.elissan916.en1125.parser;

import org.elissan916.en1125.data.CheckoutInfo;
import org.elissan916.en1125.data.Tool;
import org.elissan916.en1125.data.ToolInfo;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Map;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("SpellCheckingInspection")
public class JsonInputFileParserTest {

    @Test
    void testParseFiles() throws Exception {
        Path tmpDir = Files.createTempDirectory("jsontest");

        String toolInfoJson = "[ { \"toolName\": \"Ladder\", \"dailyCharge\": 1.99, \"weekdayCharge\": true, \"weekendCharge\": true, \"holidayCharge\": false } ]";
        String toolJson = "[ { \"toolCode\": \"LADW\", \"toolName\": \"Ladder\", \"brand\": \"Werner\" } ]";
        String checkoutJson = "[ { \"toolCode\": \"LADW\", \"rentalDays\": 3, \"discountPercent\": 10, \"checkoutDate\": \"2020-09-03\" } ]";

        Path toolInfoFile = tmpDir.resolve("toolinfo.json");
        Path toolFile = tmpDir.resolve("tool.json");
        Path checkoutFile = tmpDir.resolve("checkout.json");

        Files.writeString(toolInfoFile, toolInfoJson);
        Files.writeString(toolFile, toolJson);
        Files.writeString(checkoutFile, checkoutJson);

        JsonInputFileParser parser = new JsonInputFileParser(toolInfoFile.toString(), toolFile.toString(), checkoutFile.toString());
        parser.parseInputFiles();

        Map<String, ToolInfo> toolInfoMap = parser.getToolInfoMap();
        Map<String, Tool> toolMap = parser.getToolMap();
        List<CheckoutInfo> checkoutInfoList = parser.getCheckoutInfoList();

        assertTrue(toolInfoMap.containsKey("Ladder"));
        ToolInfo ti = toolInfoMap.get("Ladder");
        assertEquals(1.99f, ti.dailyCharge());
        assertFalse(ti.holidayCharge());

        assertTrue(toolMap.containsKey("LADW"));
        Tool t = toolMap.get("LADW");
        assertEquals("Werner", t.brand());

        assertEquals(1, checkoutInfoList.size());
        CheckoutInfo ci = checkoutInfoList.getFirst();
        assertEquals("LADW", ci.toolCode());
        assertEquals(LocalDate.of(2020,9,3), ci.checkoutDate());
    }
}

