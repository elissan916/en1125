package org.elissan916.en1125.data;

import java.time.LocalDate;

public record CheckoutInfo(String toolCode, int rentalDays, int discountPercent, LocalDate checkoutDate) {
    public CheckoutInfo(String toolCode, int rentalDays, int discountPercent, LocalDate checkoutDate) {
        if (toolCode == null || toolCode.isBlank()) {
            throw new IllegalArgumentException("Tool code cannot be null or blank");
        }

        if (rentalDays <= 0) {
            throw new IllegalArgumentException("Rental days must be greater than zero");
        }

        if (discountPercent < 0 || discountPercent > 100) {
            throw new IllegalArgumentException("Discount percent must be between 0 and 100");
        }

        if (checkoutDate == null) {
            throw new IllegalArgumentException("Checkout date cannot be null");
        }

        this.toolCode = toolCode;
        this.rentalDays = rentalDays;
        this.discountPercent = discountPercent;
        this.checkoutDate = checkoutDate;
    }
}
