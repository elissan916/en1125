package org.elissan916.en1125.businesslogic;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;

public class RentalCalendarHelper {

    public RentalCalendarHelper() {

    }

    public LocalDate calculateDueDate(LocalDate checkoutDate, int rentalDays) {
        return checkoutDate.plusDays(rentalDays);
    }

    public int calculateChargeableDays(LocalDate checkoutDate, int rentalDays, boolean chargeWeekdays, boolean chargeWeekends, boolean chargeHolidays) {
        //TODO: error checking, throw exception as needed
        if (checkoutDate == null || rentalDays <= 0) {
            return 0;
        }

        LocalDate dueDate = calculateDueDate(checkoutDate, rentalDays);
        LocalDate day = checkoutDate.plusDays(1);
        int chargeable = 0;

        for (; !day.isAfter(dueDate); day = day.plusDays(1)) {
            if (isHoliday(day)) {
                if (chargeHolidays) {
                    chargeable++;
                }
                // if not charging holidays, skip regardless of weekday/weekend
                continue;
            }

            DayOfWeek dow = day.getDayOfWeek();
            boolean weekend = (dow == DayOfWeek.SATURDAY || dow == DayOfWeek.SUNDAY);

            if (weekend) {
                if (chargeWeekends) {
                    chargeable++;
                }
            } else {
                if (chargeWeekdays) {
                    chargeable++;
                }
            }
        }

        return chargeable;
    }

    // Central holiday predicate delegates to specific holiday checks
    private boolean isHoliday(LocalDate date) {
        if (date == null) {
            return false;
        }
        return isIndependenceDay(date) || isLaborDay(date);
    }

    // Independence Day (July 4). If July 4 falls on Saturday it's observed on Friday (3rd),
    // if it falls on Sunday it's observed on Monday (5th), otherwise observed on July 4.
    private boolean isIndependenceDay(LocalDate date) {
        LocalDate july4 = LocalDate.of(date.getYear(), Month.JULY, 4);
        DayOfWeek dow = july4.getDayOfWeek();
        LocalDate observed;
        if (dow == DayOfWeek.SATURDAY) {
            observed = july4.minusDays(1);
        } else if (dow == DayOfWeek.SUNDAY) {
            observed = july4.plusDays(1);
        } else {
            observed = july4;
        }
        return date.equals(observed);
    }

    // Labor Day: first Monday in September
    private boolean isLaborDay(LocalDate date) {
        LocalDate sept1 = LocalDate.of(date.getYear(), Month.SEPTEMBER, 1);
        int daysToAdd = (DayOfWeek.MONDAY.getValue() - sept1.getDayOfWeek().getValue() + 7) % 7;
        LocalDate laborDay = sept1.plusDays(daysToAdd);
        return date.equals(laborDay);
    }

}
