package org.elissan916.en1125.data;

import java.time.LocalDate;

/**
 * Represents a single checkout request parsed from input. Validation is
 * performed in the compact canonical constructor.
 *
 * @param toolCode short tool code (must be non-null/non-blank)
 * @param rentalDays number of rental days (> 0)
 * @param discountPercent discount percent [0..100]
 * @param checkoutDate checkout date (must be non-null)
 */
public record CheckoutInfo(String toolCode, int rentalDays, int discountPercent, LocalDate checkoutDate) {
    public CheckoutInfo {
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
    }
}
