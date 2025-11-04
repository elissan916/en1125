package org.elissan916.en1125.businesslogic;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RentalCalendarHelperTest {

    private final RentalCalendarHelper helper = new RentalCalendarHelper();

    @Test
    void testIndependenceDayObservedOnFriday_excludedWhenChargeHolidaysFalse() {
        // July 4, 2015 is Saturday so observed on Friday July 3, 2015
        LocalDate checkout = LocalDate.of(2015, 7, 2);
        int rentalDays = 3; // charge days are July 3, 4, 5

        int chargeable = helper.calculateChargeableDays(checkout, rentalDays, true, true, false);
        // July 3 is holiday and not charged -> remaining days July 4 & 5 (weekend)
        assertEquals(2, chargeable);
    }

    @Test
    void testIndependenceDayObservedOnMonday_excludedWhenChargeHolidaysFalse() {
        // July 4, 2021 is Sunday so observed on Monday July 5, 2021
        LocalDate checkout = LocalDate.of(2021, 7, 3);
        int rentalDays = 2; // charge days are July 4 & 5

        int chargeable = helper.calculateChargeableDays(checkout, rentalDays, true, true, false);
        // July 5 observed holiday excluded; July 4 is weekend and charged (chargeWeekends=true)
        assertEquals(1, chargeable);
    }

    @Test
    void testLaborDay_excludedWhenChargeHolidaysFalse() {
        // Labor Day 2020 is Monday Sept 7
        LocalDate checkout = LocalDate.of(2020, 9, 4);
        int rentalDays = 4; // charge days: Sept 5,6,7,8

        int chargeable = helper.calculateChargeableDays(checkout, rentalDays, true, true, false);
        // Sept 7 is holiday and not charged; Sept5 & 6 (weekend) + Sept8 (weekday) => 3
        assertEquals(3, chargeable);
    }

    @Test
    void testWeekendNonCharge_chargeHolidaysTrue() {
        // Include a Labor Day in the range but do not charge weekends
        LocalDate checkout = LocalDate.of(2020, 9, 4);
        int rentalDays = 3; // Sept 5,6,7

        int chargeable = helper.calculateChargeableDays(checkout, rentalDays, true, false, true);
        // Weekends not charged (Sept5 & 6 excluded), Labor Day charged (chargeHolidays=true) => only Sept7
        assertEquals(1, chargeable);
    }

    @Test
    void testWeekdayNonCharge() {
        // Range has Fri, Sat, Sun and weekdays are not charged
        LocalDate checkout = LocalDate.of(2020, 9, 3);
        int rentalDays = 3; // Sept4 (Fri), Sept5 (Sat), Sept6 (Sun)

        int chargeable = helper.calculateChargeableDays(checkout, rentalDays, false, true, true);
        // Only weekend days Sept5 & Sept6 should be charged => 2
        assertEquals(2, chargeable);
    }

    @Test
    void testAllChargesTrue_countsAllDays() {
        // Period including observed Independence Day that would otherwise be excluded
        LocalDate checkout = LocalDate.of(2015, 7, 2);
        int rentalDays = 3; // July3,4,5

        int chargeable = helper.calculateChargeableDays(checkout, rentalDays, true, true, true);
        // All days charged regardless of holiday/weekend
        assertEquals(3, chargeable);
    }

    @Test
    void testInvalidInputs_returnZero() {
        assertEquals(0, helper.calculateChargeableDays(null, 5, true, true, true));
        LocalDate checkout = LocalDate.of(2020, 1, 1);
        assertEquals(0, helper.calculateChargeableDays(checkout, 0, true, true, true));
        assertEquals(0, helper.calculateChargeableDays(checkout, -1, true, true, true));
    }
}

