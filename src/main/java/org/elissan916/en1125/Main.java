package org.elissan916.en1125;

import org.elissan916.en1125.businesslogic.RentalAgreementGenerator;
import org.elissan916.en1125.data.RentalAgreement;
import org.elissan916.en1125.parser.JsonInputFileParser;


public class Main {

    public static void main(String[] args) {
        JsonInputFileParser parser = new JsonInputFileParser("src/main/resources/ToolInfoInput.json", "src/main/resources/ToolInput.json", "src/main/resources/CheckoutInfoInput.json");

        try {
            parser.parseInputFiles();
        } catch (Exception e) {
            System.err.println("Error parsing input files: " + e.getMessage());
            return;
        }

        RentalAgreementGenerator rentalAgreementGenerator = new RentalAgreementGenerator(parser);
        RentalAgreement rentalAgreement = rentalAgreementGenerator.generateRentalAgreement(parser.getCheckoutInfoList().getFirst());
        System.out.println(rentalAgreement);

    }

}