package org.elissan916.en1125;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MainTest {

    @Test
    void mainPrintsRentalAgreementForFirstCheckoutEntry() {
        // Capture stdout
        PrintStream originalOut = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));


        try {
            Main.main(new String[0]);
            String output = baos.toString();
            String expectedOutput =
                    "Tool code: JAKR\n" +
                    "Tool type: Jackhammer\n" +
                    "Tool brand: Ridgid\n" +
                    "Rental days: 5\n" +
                    "Checkout date: 09/03/15\n" +
                    "Due date: 09/08/15\n" +
                    "Daily rental charge: $2.99\n" +
                    "Charge days: 2\n" +
                    "Pre-discount charge: $5.98\n" +
                    "Discount percent: 99%\n" +
                    "Discount amount: $5.92\n" +
                    "Final charge: $0.06";

            boolean b = output.stripTrailing().compareTo(expectedOutput) == 0;
            assertTrue(b);

        } finally {
            // restore stdout
            System.setOut(originalOut);
        }
    }
}

