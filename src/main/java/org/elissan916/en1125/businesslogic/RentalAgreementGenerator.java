package org.elissan916.en1125.businesslogic;

import org.elissan916.en1125.data.CheckoutInfo;
import org.elissan916.en1125.data.RentalAgreement;
import org.elissan916.en1125.data.Tool;
import org.elissan916.en1125.data.ToolInfo;
import org.elissan916.en1125.parser.JsonInputFileParser;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Business logic responsible for generating {@link RentalAgreement} instances
 * from input {@link CheckoutInfo} and the tool/tool-info data loaded by the
 * {@link JsonInputFileParser}.
 */
public class RentalAgreementGenerator {
    private final JsonInputFileParser parser;

    private Map<String,ToolInfo> toolInfoMap = new HashMap<>();
    private Map<String,Tool> toolMap= new HashMap<>();
    private final RentalCalendarHelper rentalCalendarHelper = new RentalCalendarHelper();

    /**
     * Construct a generator using the supplied JsonInputFileParser. The parser's maps are
     * read during construction and cached locally.
     *
     * @param parser a configured {@link JsonInputFileParser}
     */
    public RentalAgreementGenerator(JsonInputFileParser parser) {
        this.parser = parser;
        initDataFromParser();
    }

    private void initDataFromParser() {
        toolInfoMap = parser.getToolInfoMap();
        toolMap = parser.getToolMap();
    }

    /**
     * Round and compute the pre-discount charge - chargeable days times daily charge.
     * Rounds to two digits using HALF_UP rounding mode.
     */
    private float calculatePreDiscountCharge(int chargeableDays, float dailyCharge) {
        float preDiscountCharge = chargeableDays * dailyCharge;
        return new BigDecimal(preDiscountCharge).setScale(2, RoundingMode.HALF_UP).floatValue();
    }

    /**
     * Compute the rounded discount amount - pre-discount charge times discount percent.
     * Rounds to two digits using HALF_UP rounding mode.
     */
    private float calculateDiscountAmount(float preDiscountCharge, int discountPercent) {
        float discountAmount = (preDiscountCharge * discountPercent) / 100;
        return new BigDecimal(discountAmount).setScale(2, RoundingMode.HALF_UP).floatValue();
    }

    /**
     * Compute the final charge - pre-discount charge minus discount amount.
     */
    private float calculateFinalCharge(float preDiscountCharge, float discountAmount) {
        return preDiscountCharge - discountAmount;
    }


    /**
     * Generate a single {@link RentalAgreement} for the given checkout info.
     *
     * @param coInfo the checkout information to use (must be non-null and valid)
     * @return a populated RentalAgreement
     */
    public RentalAgreement generateRentalAgreement(CheckoutInfo coInfo) {

        LocalDate dueDate = rentalCalendarHelper.calculateDueDate(coInfo.checkoutDate(), coInfo.rentalDays());

        Tool tool = toolMap.get(coInfo.toolCode());
        ToolInfo toolInfo = toolInfoMap.get(tool.toolName());

        int chargeableDays = rentalCalendarHelper.calculateChargeableDays(coInfo.checkoutDate(),
                coInfo.rentalDays(),
                toolInfo.weekdayCharge(),
                toolInfo.weekendCharge(),
                toolInfo.holidayCharge());

        float preDiscountCharge = calculatePreDiscountCharge(chargeableDays, toolInfo.dailyCharge());

        float discountAmount =  calculateDiscountAmount(preDiscountCharge, coInfo.discountPercent());

        float finalCharge = calculateFinalCharge(preDiscountCharge, discountAmount);

        return new RentalAgreement(coInfo.toolCode(), //Tool Code
                toolMap.get(coInfo.toolCode()).toolName(), //Tool Name
                toolMap.get(coInfo.toolCode()).brand(), //Tool Brand
                coInfo.rentalDays(), //rental days
                coInfo.checkoutDate(), //checkout date
                dueDate, //Due date
                toolInfo.dailyCharge(), //daily rental charge
                chargeableDays, //charge days
                preDiscountCharge, //pre-discount charge
                coInfo.discountPercent(), //discount percent
                discountAmount, //discount amount
                finalCharge);//final charge
    }

    /**
     * Generate rental agreements for multiple checkout entries.
     * @param coInfoList list of checkout entries
     * @return list of RentalAgreements
     */
    @SuppressWarnings("unused")
    public List<RentalAgreement> generateRentalAgreements(List<CheckoutInfo> coInfoList) {
        return coInfoList.stream().map(this::generateRentalAgreement).collect(Collectors.toList());
    }
}
