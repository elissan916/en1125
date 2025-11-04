package org.elissan916.en1125;

import org.elissan916.en1125.businesslogic.RentalAgreementGenerator;
import org.elissan916.en1125.businesslogic.RentalCalendarHelper;
import org.elissan916.en1125.data.CheckoutInfo;
import org.elissan916.en1125.data.RentalAgreement;
import org.elissan916.en1125.data.ToolInfo;
import org.elissan916.en1125.parser.JsonInputFileParser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings({"GrazieInspection", "SpellCheckingInspection"})
public class RentalAgreementGeneratorTest {

    static JsonInputFileParser parser;
    static RentalAgreementGenerator generator;
    static RentalCalendarHelper calendarHelper;

    @BeforeAll
    public static void setup() throws Exception {
        String base = "src/main/resources/";
        parser = new JsonInputFileParser(base + "ToolInfoInput.json", base + "ToolInput.json", base + "CheckoutInfoInput.json");
        // Parse tool maps only; the checkout input contains an intentionally invalid entry (101% discount)
        parser.parseToolInfoFile();
        parser.parseToolFile();
        generator = new RentalAgreementGenerator(parser);
        calendarHelper = new RentalCalendarHelper();
    }

    @Test
    public void testParserLoadedToolMaps() {
        assertNotNull(parser.getToolInfoMap());
        assertNotNull(parser.getToolMap());
    }

    @Test
    public void testParseCheckoutInfoFileThrowsForInvalidEntry() {
        // The CheckoutInfoInput.json contains one entry with discountPercent=101 which violates validation
        assertThrows(Exception.class, () -> parser.parseCheckoutInfoFile());
    }

    @SuppressWarnings("GrazieInspection")
    @Test
    public void testHolidayCalculations() {
        // Independence Day 2015 (July 4, 2015) was a Saturday -> observed on Friday July 3
        LocalDate observed2015 = LocalDate.of(2015, 7, 3);
        assertTrue(invokesIsHoliday(observed2015));

        // Independence Day 2020 (July 4, 2020) was Saturday -> observed on Friday July 3
        LocalDate observed2020 = LocalDate.of(2020, 7, 3);
        assertTrue(invokesIsHoliday(observed2020));

        // Labor Day 2015 is first Monday in Sept 2015 -> Sept 7, 2015
        LocalDate labor2015 = LocalDate.of(2015, 9, 7);
        assertTrue(invokesIsHoliday(labor2015));
    }

    // Helper uses a small reflection trick to call private isHoliday in RentalCalendarHelper
    private boolean invokesIsHoliday(LocalDate date) {
        try {
            var method = RentalCalendarHelper.class.getDeclaredMethod("isHoliday", LocalDate.class);
            method.setAccessible(true);
            return (Boolean) method.invoke(calendarHelper, date);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testChargeableDaysAndAgreementForValidCases() {
        // Build the valid test cases manually (exclude the 101% invalid case)
        List<CheckoutInfo> list = new ArrayList<>();
        list.add(new CheckoutInfo("LADW", 3, 10, LocalDate.of(2020, 7, 2)));
        list.add(new CheckoutInfo("CHNS", 5, 25, LocalDate.of(2015, 7, 2)));
        list.add(new CheckoutInfo("JAKD", 6, 0, LocalDate.of(2015, 9, 3)));
        list.add(new CheckoutInfo("JAKR", 9, 0, LocalDate.of(2015, 7, 2)));
        list.add(new CheckoutInfo("JAKR", 4, 50, LocalDate.of(2020, 7, 2)));

        for (CheckoutInfo ci : list) {
            RentalAgreement ra = generator.generateRentalAgreement(ci);
            assertNotNull(ra);

            // sanity
            assertEquals(ci.toolCode(), ra.toolCode());
            assertEquals(ci.rentalDays(), ra.rentalDays());
            assertEquals(ci.checkoutDate(), ra.checkoutDate());

            ToolInfo toolInfo = parser.getToolInfoMap().get(parser.getToolMap().get(ci.toolCode()).toolName());
            boolean weekdayCharge = toolInfo.weekendCharge();
            boolean holidayCharge = toolInfo.holidayCharge();
            boolean weekendCharge = toolInfo.weekendCharge();

            int expectedChargeDays = calendarHelper.calculateChargeableDays(ci.checkoutDate(), ci.rentalDays(), weekdayCharge, weekendCharge, holidayCharge);
            assertEquals(expectedChargeDays, ra.chargeDays());

            float dailyCharge = toolInfo.dailyCharge();
            float expectedPre = new BigDecimal(expectedChargeDays * dailyCharge).setScale(2, RoundingMode.HALF_UP).floatValue();
            assertEquals(expectedPre, ra.preDiscountCharge(), 0.001f);

            float expectedDiscountAmount = new BigDecimal((expectedPre * ci.discountPercent()) / 100f).setScale(2, RoundingMode.HALF_UP).floatValue();
            assertEquals(expectedDiscountAmount, ra.discountAmount(), 0.001f);

            float expectedFinal = expectedPre - expectedDiscountAmount;
            assertEquals(expectedFinal, ra.finalCharge(), 0.001f);
        }
    }

    @Test
    public void testInvalidCheckoutInfoConstruction() {
        // invalid rentalDays
        assertThrows(IllegalArgumentException.class, () -> new CheckoutInfo("JAKR", 0, 10, LocalDate.now()));
        // invalid discount
        assertThrows(IllegalArgumentException.class, () -> new CheckoutInfo("JAKR", 5, 101, LocalDate.now()));
        // null tool code
        assertThrows(IllegalArgumentException.class, () -> new CheckoutInfo(null, 5, 10, LocalDate.now()));
        // null date
        assertThrows(IllegalArgumentException.class, () -> new CheckoutInfo("JAKR", 5, 10, null));
    }
}
