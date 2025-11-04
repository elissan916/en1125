package org.elissan916.en1125;

import org.elissan916.en1125.businesslogic.RentalAgreementGenerator;
import org.elissan916.en1125.data.RentalAgreement;
import org.elissan916.en1125.parser.JsonInputFileParser;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static void main(String[] args) {
        JsonInputFileParser parser = new JsonInputFileParser("src/main/resources/ToolInfoInput.json", "src/main/resources/ToolInput.json", "src/main/resources/CheckoutInfoInput.json");
        parser.parseInputFiles();


        RentalAgreementGenerator rentalAgreementGenerator = new RentalAgreementGenerator(parser);
        RentalAgreement rentalAgreement = rentalAgreementGenerator.generateRentalAgreement(parser.getCheckoutInfoList().getFirst());
        System.out.println(rentalAgreement);

        //TODO: Add input list functionality??
        //List<CheckoutInfo> ciList = parser.getCheckoutInfoList();
    }

}