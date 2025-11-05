package org.elissan916.en1125.data;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests that domain constructors validate input and throw
 * {@link IllegalArgumentException} for invalid values.
 */
public class ConstructorValidationTest {

    @Test
    void testCheckoutInfoValidation() {
        LocalDate ld = LocalDate.of(2020,9,3);
        assertThrows(IllegalArgumentException.class, () -> new CheckoutInfo(null, 3, 10, ld));
        assertThrows(IllegalArgumentException.class, () -> new CheckoutInfo("", 3, 10, ld));
        assertThrows(IllegalArgumentException.class, () -> new CheckoutInfo("LADW", 0, 10, ld));
        assertThrows(IllegalArgumentException.class, () -> new CheckoutInfo("LADW", 3, -1, ld));
        assertThrows(IllegalArgumentException.class, () -> new CheckoutInfo("LADW", 3, 101, ld));
        assertThrows(IllegalArgumentException.class, () -> new CheckoutInfo("LADW", 3, 10, null));
    }

    @Test
    void testToolValidation() {
        assertThrows(IllegalArgumentException.class, () -> new Tool(null, "Ladder", "Werner"));
        assertThrows(IllegalArgumentException.class, () -> new Tool("", "Ladder", "Werner"));
        assertThrows(IllegalArgumentException.class, () -> new Tool("LADW", null, "Werner"));
        assertThrows(IllegalArgumentException.class, () -> new Tool("LADW", "", "Werner"));
        assertThrows(IllegalArgumentException.class, () -> new Tool("LADW", "Ladder", null));
        assertThrows(IllegalArgumentException.class, () -> new Tool("LADW", "Ladder", ""));
    }

    @Test
    void testToolInfoValidation() {
        assertThrows(IllegalArgumentException.class, () -> new ToolInfo(null, 1.99f, true, true, true));
        assertThrows(IllegalArgumentException.class, () -> new ToolInfo("", 1.99f, true, true, true));
        assertThrows(IllegalArgumentException.class, () -> new ToolInfo("Ladder", -1f, true, true, true));
        assertThrows(IllegalArgumentException.class, () -> new ToolInfo("Ladder", 1.99f, false, false, false));
    }
}
