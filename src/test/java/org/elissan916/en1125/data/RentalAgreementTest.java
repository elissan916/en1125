package org.elissan916.en1125.data;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("SpellCheckingInspection")
public class RentalAgreementTest {

    @Test
    void testToStringFormatting() {
        RentalAgreement ra = new RentalAgreement(
                "JAKR",
                "Jackhammer",
                "Ridgid",
                5,
                LocalDate.of(2020, 9, 3),
                LocalDate.of(2020, 9, 8),
                2.50f,
                3,
                7.50f,
                10,
                0.75f,
                6.75f
        );

        String expected = "Tool code: JAKR\n" +
                "Tool type: Jackhammer\n" +
                "Tool brand: Ridgid\n" +
                "Rental days: 5\n" +
                "Checkout date: 09/03/20\n" +
                "Due date: 09/08/20\n" +
                "Daily rental charge: 2.50\n" +
                "Charge days: 3\n" +
                "Pre-discount charge: $7.50\n" +
                "Discount percent: 10%\n" +
                "Discount amount: $0.75\n" +
                "Final charge: $6.75\n";

        assertEquals(expected, ra.toString());
    }
}

