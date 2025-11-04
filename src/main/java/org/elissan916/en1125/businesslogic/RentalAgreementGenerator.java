package org.elissan916.en1125.businesslogic;

import org.elissan916.en1125.data.CheckoutInfo;
import org.elissan916.en1125.data.RentalAgreement;
import org.elissan916.en1125.data.Tool;
import org.elissan916.en1125.data.ToolInfo;
import org.elissan916.en1125.parser.JsonInputFileParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.StreamSupport.stream;

public class RentalAgreementGenerator {
    private final JsonInputFileParser parser;

    private Map<String,ToolInfo> toolInfoMap = new HashMap<String,ToolInfo>();
    private Map<String,Tool> toolMap= new HashMap<String,Tool>();
    private List<CheckoutInfo> checkoutInfoList = new ArrayList<>();
    private RentalCalendarHelper rentalCalendarHelper = new RentalCalendarHelper();

    public RentalAgreementGenerator(JsonInputFileParser parser) {
        this.parser = parser;
        initDataFromParser();
    }

    private void initDataFromParser() {
        this.toolInfoMap = parser.getToolInfoMap();
        this.toolMap = parser.getToolMap();
        this.checkoutInfoList = parser.getCheckoutInfoList();
    }


    public List<RentalAgreement> generateRentalAgreements() {
        List<RentalAgreement> rentalAgreementList = new ArrayList<>();

      /*  for (int i = 0; i < checkoutInfoArray.length; i++) {
            CheckoutInfo checkoutInfo = checkoutInfoArray[i];
            Tool tool = findToolByCode(checkoutInfo.toolCode());
            ToolInfo toolInfo = findToolInfoByName(tool.toolName());

            rentalAgreements[i] = new RentalAgreement(tool, toolInfo, checkoutInfo);
        } */

        rentalAgreementList = checkoutInfoList.stream().map( coInfo ->
                new RentalAgreement(coInfo.toolCode(), toolMap.get(coInfo.toolCode()).toolName(),
                        toolMap.get(coInfo.toolCode()).brand(),
                        toolInfoMap.get(toolMap.get(coInfo.toolCode()).toolName(
                        ))
            (()-> )

        return rentalAgreementList;
    }
}
