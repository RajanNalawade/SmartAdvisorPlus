package sbilife.com.pointofsale_bancaagency.smartguaranteedsavings;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import sbilife.com.pointofsale_bancaagency.common.CommonForAllProd;

class SmartGuaranteedSavingsPlanBusinessLogic {

    private CommonForAllProd commonForAllProd = null;
    private SmartGuaranteedSavingsPlanBean smartGuranteedSavingPlanBean = null;

    private CommonForAllProd cfap = null;
    private SmartGuaranteedSavingsPlanProperties sgspProp = null;
    private double TabularSAMFRate = 0;
    private double GSVFactor = 0;
    private double TermToMaturity = 0;

    /*** modified by Akshaya on 20-MAY-16 end **/
    // Set ST
    public double setServiceTax(double premAmt, double premAmtWithST) {
        return (premAmtWithST - premAmt);
    }

    /* Mines logic starts from here */

    /********************* Backdate Mines logic start *******************************************************/

    public double getMinesOccuInterest(int SumAssured_Basic) {
        return (SumAssured_Basic / 1000) * 2;
    }

    // /******************* Modified by Akshaya on 03-MAR-2015 start **********/
    // public double getBackDateInterest(double grossPremium, String bkDate) {
    //
    // try {
    //
    // double indigoRate = 7.5;
    // int compoundFreq = 2;
    // SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
    // SimpleDateFormat dateformat1 = new SimpleDateFormat("dd-MM-yyyy");
    //
    // Date dtBackdate = dateformat1.parse(bkDate);
    // String strBackDate = dateFormat.format(dtBackdate);
    //
    // Calendar cal = Calendar.getInstance();
    // // System.out.println("cal "+cal);
    // // System.out.println("cal "+cal.getTime());
    // String date = dateFormat.format(cal.getTime());
    // // System.out.println("date "+date);
    //
    // double netPremWithoutST = Math.round((grossPremium * 100) / 103.09);
    // // System.out.println("netPremWithoutST "+netPremWithoutST);
    // int days = commonForAllProd.setDate(date, strBackDate);
    // // System.out.println("no of days "+days);
    // double monthsBetween = commonForAllProd.roundDown(
    // (double) days / 365 * 12, 0);
    // // System.out.println("aaaaaaaaa "+(double)79/365);
    // // System.out.println("month "+monthsBetween);
    //
    // double interest = getCompoundAmount(monthsBetween,
    // netPremWithoutST, indigoRate, compoundFreq);
    // // System.out.println("onterest "+interest);
    // return interest;
    // }
    //
    // catch (Exception e) {
    // return 0;
    // }
    // }

    /*************** Smart Guranteed Saving start ****************/

    public double getBackDateInterest(double grossPremium, String bkDate) {

        try {

            // double indigoRate=7.5;
            /**
             * Modified by Akshaya. Rate Change from 1-APR-2015
             */

            /*** modified by Akshaya on 20-MAY-16 start **/
            double indigoRate = 6.50;
            double ServiceTaxValue = ((sgspProp.serviceTax
                    + sgspProp.SBCServiceTax + sgspProp.KKCServiceTax) + 1) * 100;

            /*** modified by Akshaya on 20-MAY-16 start **/

            int compoundFreq = 2;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            SimpleDateFormat dateformat1 = new SimpleDateFormat("dd-MM-yyyy");

            Date dtBackdate = dateformat1.parse(bkDate);
            String strBackDate = dateFormat.format(dtBackdate);

            Calendar cal = Calendar.getInstance();
            String date = dateFormat.format(cal.getTime());

            double netPremWithoutST = Math.round((grossPremium * 100)
                    / ServiceTaxValue);
            int days = commonForAllProd.setDate(date, strBackDate);
            double monthsBetween = commonForAllProd.roundDown(
                    (double) days / 365 * 12, 0);

            return getCompoundAmount(monthsBetween,
                    netPremWithoutST, indigoRate, compoundFreq);
        } catch (Exception e) {
            return 0;
        }
    }

    /******** Smart Guranteed saving end ******************/

    private double getCompoundAmount(double monthsBetween,
                                     double netPremWithoutST, double indigoRate, int compoundFreq) {
        return netPremWithoutST
                * Math.pow((1 + (indigoRate / (100 * compoundFreq))),
                (compoundFreq * (monthsBetween / 12)))
                - netPremWithoutST;
        // System.out.println("compoundAmount "+compoundAmount);
    }

    public double getStaffRebate() {
        if (smartGuranteedSavingPlanBean.isStaffDisc()
                || smartGuranteedSavingPlanBean.isBancAssuranceDisc()) {
            return 0.06;
        } else {
            return 0;
        }
    }

    public String getStaffDiscCode() {
        if (smartGuranteedSavingPlanBean.isStaffDisc())
            return "SBI";
        else
            return "None";
    }

    // ##################

    public SmartGuaranteedSavingsPlanBusinessLogic(
            SmartGuaranteedSavingsPlanBean SGSPbean) {
        // TODO Auto-generated constructor stub

        cfap = new CommonForAllProd();
        sgspProp = new SmartGuaranteedSavingsPlanProperties();
    }

    /*** modified by Akshaya on 20-MAY-16 start **/
    public double getServiceTax(double premiumWithoutST, String type) {
        if (type.equals("basic")) {
            return Double.parseDouble(cfap.getRoundUp(String
                    .valueOf(premiumWithoutST * (sgspProp.serviceTax + 1))))
                    - premiumWithoutST;
        } else if (type.equals("SBC")) {
            return Double.parseDouble(cfap.getRoundUp(String
                    .valueOf(premiumWithoutST * (sgspProp.SBCServiceTax + 1))))
                    - premiumWithoutST;
        } else // KKC
        {
            return Double.parseDouble(cfap.getRoundUp(String
                    .valueOf(premiumWithoutST * (sgspProp.KKCServiceTax + 1))))
                    - premiumWithoutST;
        }

    }

    public double getServiceTaxSecondYear(double premiumWithoutST, String type) {
        if (type.equals("basic")) {
            return Double.parseDouble(cfap.getRoundUp(String
                    .valueOf(premiumWithoutST
                            * (sgspProp.serviceTaxSecondYear + 1))))
                    - premiumWithoutST;
        } else if (type.equals("SBC")) {
            return Double.parseDouble(cfap.getRoundUp(String
                    .valueOf(premiumWithoutST
                            * (sgspProp.SBCServiceTaxSecondYear + 1))))
                    - premiumWithoutST;
        } else // KKC
        {
            return Double.parseDouble(cfap.getRoundUp(String
                    .valueOf(premiumWithoutST
                            * (sgspProp.KKCServiceTaxSecondYear + 1))))
                    - premiumWithoutST;
        }

    }

    /*** modified by Akshaya on 20-MAY-16 end **/

    public double setYearlyBasePremiumPaid(int year_F, double premiumAmt) {
        if (year_F > sgspProp.premiumPayingTerm)
            return 0;
        else
            return premiumAmt;

    }

    public double setGuaranteedAddition(double premiumAmt,
                                        double cummulativePremium) {
        double a = 0;
        if (premiumAmt >= 30000)
            a = 0.06;
        else
            a = 0.055;
        return (a * cummulativePremium);

    }

    public double setSumAssured(double premiumAmt, int age) {
        SmartGuaranteedSavingsPlanDB sgspDB = new SmartGuaranteedSavingsPlanDB();
        double[] samfArr = sgspDB.getSAMFRates();
        int position = 0;
        TabularSAMFRate = 0;
        for (int ageCount = 18; ageCount <= 60; ageCount++) {
            if (ageCount == age) {
                TabularSAMFRate = samfArr[position];
                break;
            }
            position++;
        }
        return (premiumAmt * TabularSAMFRate);
    }

    public double setGuaranteedDeathBenefit(double sumAssured,
                                            double premiumAmt, double cummulativeGuaranteedAddition,
                                            double cummulativePremium) {
        double max = Math.max(10 * premiumAmt, sumAssured);
        max = Math.max(max, 1.05 * cummulativePremium);
        return (cummulativeGuaranteedAddition + max);
    }

    public double setGuaranteedMaturityBenefit(double sumAssured,
                                               double cummulativeGuaranteedAddition, int year_F) {
        if (year_F == sgspProp.policyTerm)
            return (sumAssured + cummulativeGuaranteedAddition);
        else
            return 0;
    }

    public double setSAMFRate(int age) {
        SmartGuaranteedSavingsPlanDB sgspDB = new SmartGuaranteedSavingsPlanDB();
        double[] samfArr = sgspDB.getSAMFRates();
        int position = 0;
        double TabularSAMFRate = 0;
        for (int ageCount = 18; ageCount <= 60; ageCount++) {
            if (ageCount == age) {
                TabularSAMFRate = samfArr[position];
                break;
            }
            position++;
        }

        return TabularSAMFRate;
    }

    public double setSumAssured(double premiumAmt, double SAMFRate) {
        // SmartGuaranteedSavingsPlanDB sgspDB=new
        // SmartGuaranteedSavingsPlanDB();
        // double [] samfArr=sgspDB.getSAMFRates();
        // int position=0;
        // TabularSAMFRate=0;
        // for (int ageCount=18; ageCount<=60; ageCount++)
        // {
        // if(ageCount==age)
        // {
        // TabularSAMFRate=samfArr[position];
        // break;
        // }
        // position++;
        // }
        return (premiumAmt * SAMFRate);
    }

    public double setGuaranteedSurrenderValue(
            double cummulativeGuaranteedAddition, double cummulativePremium,
            int year_F) {
        SmartGuaranteedSavingsPlanDB sgspDB = new SmartGuaranteedSavingsPlanDB();
        double[] gsvFactor = sgspDB.getGSVFactors();
        double[] termMaturity = sgspDB.getTermToMaturity();
        @SuppressWarnings("unused")
        double temp1;
        int position = 0;
        GSVFactor = 0;
        TermToMaturity = 0;
        for (int policyYear = 1; policyYear <= 15; policyYear++) {
            if (policyYear == (sgspProp.policyTerm - year_F + 1)) {
                TermToMaturity = termMaturity[position];
                break;
            }
            position++;
        }

        position = 0;

        for (int policyYear = 1; policyYear <= 15; policyYear++) {
            if (policyYear == year_F) {
                GSVFactor = gsvFactor[position];
                break;
            }
            position++;
        }

        temp1 = (GSVFactor * cummulativePremium) + (TermToMaturity / 100)
                * cummulativeGuaranteedAddition;

        return (GSVFactor * cummulativePremium + (TermToMaturity / 100)
                * cummulativeGuaranteedAddition);
    }

}
