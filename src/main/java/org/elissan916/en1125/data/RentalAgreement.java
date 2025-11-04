package org.elissan916.en1125.data;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public record RentalAgreement(String toolCode, String toolType, String brand, int rentalDays,
                              LocalDate checkoutDate, LocalDate dueDate, float dailyRentalCharge,
                              int chargeDays, float preDiscountCharge, int discountPercent,
                              float discountAmount, float finalCharge) {

    public RentalAgreement(String toolCode, String toolType, String brand, int rentalDays,
                           LocalDate checkoutDate, LocalDate dueDate, float dailyRentalCharge,
                           int chargeDays, float preDiscountCharge, int discountPercent,
                           float discountAmount, float finalCharge) {

        if (toolCode == null || toolCode.isBlank()) {
            throw new IllegalArgumentException("Tool code cannot be null or blank");
        }

        if (toolType == null || toolType.isBlank()) {
            throw new IllegalArgumentException("Tool type cannot be null or blank");
        }

        if (brand == null || brand.isBlank()) {
            throw new IllegalArgumentException("Brand cannot be null or blank");
        }

        if (rentalDays <= 0) {
            throw new IllegalArgumentException("Rental days must be greater than zero");
        }

        if (checkoutDate == null) {
            throw new IllegalArgumentException("Checkout date cannot be null");
        }

        if (dueDate == null) {
            throw new IllegalArgumentException("Due date cannot be null");
        }

        if (dailyRentalCharge < 0) {
            throw new IllegalArgumentException("Daily rental charge cannot be negative");
        }

        if (chargeDays < 0) {
            throw new IllegalArgumentException("Charge days cannot be negative");
        }

        if (preDiscountCharge < 0) {
            throw new IllegalArgumentException("Pre-discount charge cannot be negative");
        }

        if (discountPercent < 0 || discountPercent > 100) {
            throw new IllegalArgumentException("Discount percent specified must be between 0 and 100");
        }

        if (discountAmount < 0) {
            throw new IllegalArgumentException("Discount amount cannot be negative");
        }

        if (finalCharge < 0) {
            throw new IllegalArgumentException("Final charge cannot be negative");
        }

        this.toolCode = toolCode;
        this.toolType = toolType;
        this.brand = brand;
        this.rentalDays = rentalDays;
        this.checkoutDate = checkoutDate;
        this.dueDate = dueDate;
        this.dailyRentalCharge = dailyRentalCharge;
        this.chargeDays = chargeDays;
        this.preDiscountCharge = preDiscountCharge;
        this.discountPercent = discountPercent;
        this.discountAmount = discountAmount;
        this.finalCharge = finalCharge;
    }

    @Override
    public String toString() {
        DecimalFormat format = new DecimalFormat("#,##0.00");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/uu");

        return "Tool code: " + toolCode + "\n" +
                "Tool type: " + toolType + "\n" +
                "Tool brand: " + brand + "\n" +
                "Rental days: " + rentalDays + "\n" +
                "Checkout date: " + checkoutDate.format(formatter) + "\n" +
                "Due date: " + dueDate.format(formatter) + "\n" +
                "Daily rental charge: " + format.format(dailyRentalCharge) + "\n" +
                "Charge days: " + chargeDays + "\n" +
                "Pre-discount charge: $" + format.format(preDiscountCharge) + "\n" +
                "Discount percent: " + discountPercent + "%%\n" +
                "Discount amount: $" + format.format(discountAmount) + "\n" +
                "Final charge: $" + format.format(finalCharge) + "\n";
    }
}
