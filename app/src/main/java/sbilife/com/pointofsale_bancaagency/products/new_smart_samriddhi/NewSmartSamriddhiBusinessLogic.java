package sbilife.com.pointofsale_bancaagency.products.new_smart_samriddhi;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import sbilife.com.pointofsale_bancaagency.common.CommonForAllProd;

public class NewSmartSamriddhiBusinessLogic {

    public CommonForAllProd commonForAllProd = null;
    public NewSmartSamriddhiBean smartSamriddhiBean = null;


    public CommonForAllProd cfap = null;
    public NewSmartSamriddhiProperties sgspProp = null;
    double TabularSAMFRate = 0;
    double GSVFactor = 0, TermToMaturity = 0, SSVFactor = 0;
    double monthvalue = 0;
    double PremiumAmount = 0;

    /*** modified by Akshaya on 20-MAY-16 end **/
    // Set ST
    public double setServiceTax(double premAmt, double premAmtWithST) {
        return (premAmtWithST - premAmt);
    }


    /* Mines logic starts from here */

    /********************* Backdate Mines logic start *******************************************************/


    public double getStaffRebate() {
        if (smartSamriddhiBean.isStaffDisc()
                || smartSamriddhiBean.isBancAssuranceDisc()) {
            return 0.06;
        } else {
            return 0;
        }
    }

    public String getStaffDiscCode() {
        if (smartSamriddhiBean.isStaffDisc())
            return "SBI";
        else
            return "None";
    }


    //##################

    public NewSmartSamriddhiBusinessLogic(
            NewSmartSamriddhiBean SGSPbean) {
        // TODO Auto-generated constructor stub

        cfap = new CommonForAllProd();
        sgspProp = new NewSmartSamriddhiProperties();
    }


    public double getPremiumAmount(String premfreq, Double PremAmount) {
        double PremiumAmount = 0;
        if (premfreq.equals("Yearly")) {
            PremiumAmount = PremAmount;
        } else {
            PremiumAmount = Double.parseDouble(cfap.getRoundOffLevel2New(cfap.getStringWithout_E(PremAmount * 0.085)));
        }

        return PremiumAmount;
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
        } else if (type.equals("KERALA")) {
            return Double.parseDouble(cfap.getRoundUp(String
                    .valueOf(premiumWithoutST * (sgspProp.kerlaServiceTax + 1))))
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
        } else if (type.equals("KERALA")) {
            return Double.parseDouble(cfap.getRoundUp(String
                    .valueOf(premiumWithoutST
                            * (sgspProp.kerlaServiceTaxSecondYear + 1))))
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

    public double setYearlyBasePremiumPaid11(int year_F, double premiumAmt, String premFreq) {
        if (year_F > sgspProp.premiumPayingTerm)
            return 0;
        else if (premFreq.equals("Monthly")) {

            return (premiumAmt * 0.085 * 12);

        } else {
            return premiumAmt;
        }

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
        NewSmartSamriddhiDB sgspDB = new NewSmartSamriddhiDB();
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
        NewSmartSamriddhiDB sgspDB = new NewSmartSamriddhiDB();
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

    public double settotalpremium(int Year_F) {
        double totalprem = 0;
        if (Year_F <= sgspProp.premiumPayingTerm) {
            totalprem = 29000;
        } else {
            return 0;
        }
        return totalprem;
    }

    //added by sujata 09/10/2020
    public double setGuaranteedSurrenderValue(
            double cummulativeGuaranteedAddition, double cummulativePremium,
            int year_F, double sumoftotalpremium) {
        NewSmartSamriddhiDB smartsamriddhiDB = new NewSmartSamriddhiDB();
        double[] gsvFactor = smartsamriddhiDB.getGSVFactors();
        double[] termMaturity = smartsamriddhiDB.getTermToMaturity();
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

//        temp1 = (GSVFactor * cummulativePremium) + (TermToMaturity / 100)
//                * cummulativeGuaranteedAddition;
//
//        return (GSVFactor * cummulativePremium + (TermToMaturity / 100)
//                * cummulativeGuaranteedAddition);
//		System.out.println("GSVFactor"+GSVFactor);
        System.out.println("\ncummulativePremium " + cummulativePremium);

        temp1 = (GSVFactor * cummulativePremium);

        return (GSVFactor * cummulativePremium);

    }

    public double setNonGuaranteedSurrenderValue(double sumAssured, double cummulativeGuaranteedAddition, int year_F) {

        double val, val2, val1;
        if (year_F > sgspProp.premiumPayingTerm) {
            val = 1;
        } else {
            val = year_F / sgspProp.premiumPayingTerm;
        }

        val1 = val * sumAssured;

        NewSmartSamriddhiDB smartsamriddhiDB = new NewSmartSamriddhiDB();

        double[] ssvFactor = smartsamriddhiDB.getSSVFactors();

        int position = 0;
        SSVFactor = 0;


        for (int policyYear = 1; policyYear <= 15; policyYear++) {
            if (policyYear == year_F) {
                SSVFactor = ssvFactor[position];
                break;
            }
            position++;
        }


        val2 = (val1 + cummulativeGuaranteedAddition) * SSVFactor;

        return (val1 + cummulativeGuaranteedAddition) * SSVFactor;
    }

    public double minimumguarrenteedSurrenderValue(double cumulativepremium, int year_F) {
        NewSmartSamriddhiDB smartsamriddhiDB = new NewSmartSamriddhiDB();
        double[] gsvFactor = smartsamriddhiDB.getGSVFactors();
        int position = 0;
        double temp1 = 0;
        GSVFactor = 0;
        for (int policyYear = 1; policyYear <= 15; policyYear++) {
            if (policyYear == year_F) {
                GSVFactor = gsvFactor[position];
                break;
            }
            position++;
        }

        if (year_F <= sgspProp.policyTerm) {
            temp1 = GSVFactor * cumulativepremium;
        }

        System.out.println("GSVFactor   " + GSVFactor);
        System.out.println("cumulativepremium   " + cumulativepremium);

        return (GSVFactor * cumulativepremium);
    }

    ///Added by Akash

    public double getTotalPremium(int year_f, double instalmentPremium) {
        double totalPremium = 0;
        if (year_f <= sgspProp.premiumPayingTerm) {
            totalPremium = instalmentPremium * monthvalue;
        } else {
            totalPremium = 0;
        }
        //System.out.println("totalPremium  "+totalPremium);
        return totalPremium;
    }

    //Added By Akash///
    public double getvalue(String premfreq) {

        if (premfreq.equals("Monthly")) {
            monthvalue = 12;
        } else {
            monthvalue = 1;
        }
        return monthvalue;
    }

    /************ Added By tushar kotian on 3/8/2017 **************/

    public double getBackDateInterest(double grossPremium, String bkDate) {

        try {

//			double indigoRate=7.5;
            /**
             * Modified by Akshaya. Rate Change from 1-APR-2015
             */

            /*** modified by Akshaya on 20-MAY-16 start **/
            //double indigoRate=10;

            //double indigoRate = 8.75; /**Changed By Machindra 10/04/2019**/
            double indigoRate = 6.50;
            double ServiceTaxValue = ((sgspProp.serviceTax + sgspProp.SBCServiceTax + sgspProp.KKCServiceTax) + 1) * 100;

            /*** modified by Akshaya on 20-MAY-16 start **/

            int compoundFreq = 2;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            SimpleDateFormat dateformat1 = new SimpleDateFormat("dd-MM-yyyy");

            Date dtBackdate = dateformat1.parse(bkDate);
            String strBackDate = dateFormat.format(dtBackdate);


            Calendar cal = Calendar.getInstance();
            String date = dateFormat.format(cal.getTime());

            double netPremWithoutST = Math.round((grossPremium * 100) / ServiceTaxValue);
            int days = cfap.setDate(date, strBackDate);
            double monthsBetween = cfap.roundDown((double) days / 365 * 12, 0);

            double interest = getCompoundAmount(monthsBetween, netPremWithoutST, indigoRate, compoundFreq);
            return interest;
        } catch (Exception e) {
            return 0;
        }
    }

    /********Smart Guranteed saving end ******************/

    public double getCompoundAmount(double monthsBetween,
                                    double netPremWithoutST, double indigoRate, int compoundFreq) {
        double compoundAmount = netPremWithoutST
                * Math.pow((1 + (indigoRate / (100 * compoundFreq))),
                (compoundFreq * (monthsBetween / 12)))
                - netPremWithoutST;
        return compoundAmount;
        // System.out.println("compoundAmount "+compoundAmount);
    }


    public double getMinesOccuInterest(double SumAssured_Basic) {
        return (SumAssured_Basic / 1000) * 2;
    }

    /************ Added By tushar kotian on 3/8/2017 **************/

    /*** Added by Priyanka Warekar - 31-08-2018 - start *******/
    public double getServiceTaxMines(double MinesOccuInterest) {
        return Double.parseDouble(cfap.getRoundUp(cfap.getRoundOffLevel2(cfap.getStringWithout_E(MinesOccuInterest * (sgspProp.serviceTax + sgspProp.SBCServiceTax)))));
    }

    /*** Added by Priyanka Warekar - 31-08-2018 - end *******/

//added by sujata on 28-11-2019
    public double getSpecialSurrender(int Year_F, double sumAssured, double premiumAmt, double cummulativeGuaranteedAddition) {
        double a = 0, b = 0, c = 0, ssvFactor = 0, specialSurrenderVal = 0;
        NewSmartSamriddhiDB smartSamriddhiDB = new NewSmartSamriddhiDB();
        double ppt = sgspProp.premiumPayingTerm;

        if (Year_F > sgspProp.premiumPayingTerm) {
            a = 1;
        } else {
            a = Year_F / ppt;
        }
        //				System.out.println("\n a "+a);

        b = (a * sumAssured);

//					System.out.println("b "+b);
//					System.out.println("sumAssured "+sumAssured);
//				    System.out.println("cummulativeGuaranteedAddition "+cummulativeGuaranteedAddition);
        double[] SSVFactor = smartSamriddhiDB.getSSVFactors();
        ssvFactor = SSVFactor[Year_F - 1];
        //	System.out.println("single temp "+ ssvFactor);
        int pos1 = 0;
        for (int i = 1; i <= Year_F; i++) {
            if (i == Year_F) {
                ssvFactor = SSVFactor[pos1];
                //	System.out.println("ssvFactor "+ssvFactor);
                break;
            }
            pos1++;
        }

        specialSurrenderVal = (b + cummulativeGuaranteedAddition) * ssvFactor;
        //	System.out.println("specialSurrenderVal "+specialSurrenderVal);
        return specialSurrenderVal;

    }

    public double getSumAssurredOnDeath(int year, double GuaranteedDeathBenefit, double GuaranteedAddition) {
        double SAOD = 0;

        SAOD = GuaranteedDeathBenefit - GuaranteedAddition;

        return SAOD;
    }


}
