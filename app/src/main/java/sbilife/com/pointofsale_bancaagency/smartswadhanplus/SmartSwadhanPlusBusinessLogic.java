package sbilife.com.pointofsale_bancaagency.smartswadhanplus;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import sbilife.com.pointofsale_bancaagency.common.CommonForAllProd;

class SmartSwadhanPlusBusinessLogic {

    private CommonForAllProd cfap = null;
    private SmartSwadhanPlusBean smartSwadhanPlusBean = null;
    private SmartSwadhanPlusProperties prop = null;

    public SmartSwadhanPlusBusinessLogic(
            SmartSwadhanPlusBean smartSwadhanPlusBean) {
        // TODO Auto-generated constructor stub
        cfap = new CommonForAllProd();
        this.smartSwadhanPlusBean = smartSwadhanPlusBean;
        prop = new SmartSwadhanPlusProperties();
    }

    /*** modified by Akshaya on 20-MAY-16 start **/

    public double getMinesOccuInterest(double SumAssured_Basic) {
        return (SumAssured_Basic / 1000) * 2;
    }

    public double getBackDateInterest(double grossPremium, String bkDate) {

        try {

            // double indigoRate=7.5;
            /**
             * Modified by Akshaya. Rate Change from 1-APR-2015
             */

            /******************* Modified by Akshaya on 14-APR-2015 start **********/
            //double indigoRate = 8.50; /**Changed By Pranprit 07/09/2018**/

            double indigoRate = 6.50; /**Changed By Machindra 10/04/2019**/
            double SeriviceTax = 0;
            if (smartSwadhanPlusBean.getJKResidentDisc())
                SeriviceTax = prop.JKServiceTax;
            else
                SeriviceTax = prop.serviceTax + prop.SBCServiceTax
                        + prop.KKCServiceTax;

            double ServiceTaxValue = (SeriviceTax + 1) * 100;

            /******************* Modified by Akshaya on 14-APR-2015 end **********/
            int compoundFreq = 2;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            SimpleDateFormat dateformat1 = new SimpleDateFormat("dd-MM-yyyy");

            Date dtBackdate = dateformat1.parse(bkDate);
            String strBackDate = dateFormat.format(dtBackdate);

            Calendar cal = Calendar.getInstance();
            // System.out.println("cal "+cal);
            // System.out.println("cal "+cal.getTime());
            String date = dateFormat.format(cal.getTime());
            // System.out.println("date "+date);

            /******************* Modified by Akshaya on 14-APR-2015 start **********/
            // double netPremWithoutST=Math.round((grossPremium*100)/103.09);
            double netPremWithoutST = Math.round((grossPremium * 100)
                    / ServiceTaxValue);
            /******************* Modified by Akshaya on 14-APR-2015 end **********/
            // System.out.println("netPremWithoutST "+netPremWithoutST);
            int days = cfap.setDate(date, strBackDate);
            // System.out.println("no of days "+days);
            double monthsBetween = cfap.roundDown((double) days / 365 * 12, 0);
            // System.out.println("aaaaaaaaa "+(double)79/365);
            // System.out.println("month "+monthsBetween);

            double interest = getCompoundAmount(monthsBetween,
                    netPremWithoutST, indigoRate, compoundFreq);
            // System.out.println("onterest "+interest);
            return interest;
        } catch (Exception e) {
            return 0;
        }
    }

    private double getCompoundAmount(double monthsBetween,
                                     double netPremWithoutST, double indigoRate, int compoundFreq) {
        double compoundAmount = netPremWithoutST
                * Math.pow((1 + (indigoRate / (100 * compoundFreq))),
                (compoundFreq * (monthsBetween / 12)))
                - netPremWithoutST;
        return compoundAmount;
        // System.out.println("compoundAmount "+compoundAmount);
    } // End

    /*** Added by Priyanka Warekar - 31-08-2018 - start *******/
    public double getServiceTaxMines(double MinesOccuInterest) {
        return Double.parseDouble(cfap.getRoundUp(cfap.getRoundOffLevel2(cfap.getStringWithout_E(MinesOccuInterest * (prop.serviceTax + prop.SBCServiceTax)))));
    }
/*** Added by Priyanka Warekar - 31-08-2018 - end *******/
    /*** modified by Akshaya on 20-MAY-16 start **/

    public double getServiceTax(double premiumWithoutST, boolean JKResident,
                                String type) {
        if (type.equals("basic")) {
            // System.out.println("nnn "+ (premiumWithoutST*prop.serviceTax));
            if (JKResident)
                return Double.parseDouble(cfap.getRoundUp(cfap
                        .getRoundOffLevel2(cfap
                                .getStringWithout_E(premiumWithoutST
                                        * prop.JKServiceTax))));
            else {
                return Double.parseDouble(cfap.getRoundUp(cfap
                        .getRoundOffLevel2(cfap
                                .getStringWithout_E(premiumWithoutST
                                        * prop.serviceTax))));
            }
        } else if (type.equals("SBC")) {
            if (JKResident)
                return 0;
            else {
                return Double.parseDouble(cfap.getRoundUp(cfap.getStringWithout_E(premiumWithoutST * prop.SBCServiceTax)));
            }
        } else if (type.equals("KERALA")) {

            return Double.parseDouble(cfap.getRoundUp(cfap.getStringWithout_E(premiumWithoutST * prop.kerlaServiceTax)));

        } else //KKC
        {
            if (JKResident == true)
                return 0;
            else {
                return Double.parseDouble(cfap.getRoundUp(cfap
                        .getStringWithout_E(premiumWithoutST
                                * prop.KKCServiceTax)));
            }
        }

    }

    public double getServiceTaxSecondYear(double premiumWithoutST,
                                          boolean JKResident, String type) {
        if (type.equals("basic")) {
            if (JKResident)
                return Double.parseDouble(cfap.getRoundUp(cfap
                        .getRoundOffLevel2(cfap
                                .getStringWithout_E(premiumWithoutST
                                        * prop.JKServiceTax))));
            else {
                return Double.parseDouble(cfap.getRoundUp(cfap
                        .getRoundOffLevel2(cfap
                                .getStringWithout_E(premiumWithoutST
                                        * prop.serviceTaxSecondYear))));
            }
        } else if (type.equals("SBC")) {
            if (JKResident)
                return 0;
            else {
                return Double.parseDouble(cfap.getRoundUp(cfap
                        .getStringWithout_E(premiumWithoutST
                                * prop.SBCServiceTaxSecondYear)));
            }
        } else if (type.equals("KERALA")) {
            return Double.parseDouble(cfap.getRoundUp(cfap.getStringWithout_E(premiumWithoutST * prop.kerlaServiceTaxSecondYear)));

        } else //KKC
        {
            if (JKResident == true)
                return 0;
            else {
                return Double.parseDouble(cfap.getRoundUp(cfap
                        .getStringWithout_E(premiumWithoutST
                                * prop.KKCServiceTaxSecondYear)));
            }
        }

    }

    /*** modified by Akshaya on 20-MAY-16 end **/

    private double getBasicPremiumRate() {
        SmartSwadhanPlusDB SSP_DB = new SmartSwadhanPlusDB();
        int arrCount = 0;
        String prStr, pr = null;
        String[] prStrArr;
        double prDouble = 0;
        int PPT = smartSwadhanPlusBean.getPremiumPayingTerm();

        if (smartSwadhanPlusBean.getPlanType().equals("Single")) {
            prStr = SSP_DB.getPremiumRate_Arr_Single();
            prStrArr = cfap.split(prStr, ",");
            //Here min age is 18 & max age is 65/min term is 10 & max term is 30
            for (int i = 18; i < 66; i++) {
                for (int j = 10; j < 31; j++) {
                    if (i == smartSwadhanPlusBean.getAge()
                            && j == smartSwadhanPlusBean.getPolicyTerm()) {
                        pr = prStrArr[arrCount];
                        prDouble = Double.parseDouble(pr);
                        break;
                    }
                    arrCount++;
                }
            }
        } else if (smartSwadhanPlusBean.getPlanType().equals("Regular")) {
            prStr = SSP_DB.getPremiumRate_Arr_Regular();
            prStrArr = cfap.split(prStr, ",");
            //Here min age is 18 & max age is 65/min term is 10 & max term is 30
            for (int i = 18; i < 66; i++) {
                for (int j = 10; j < 31; j++) {
                    if (i == smartSwadhanPlusBean.getAge()
                            && j == smartSwadhanPlusBean.getPolicyTerm()) {
                        pr = prStrArr[arrCount];
                        prDouble = Double.parseDouble(pr);
                        break;
                    }
                    arrCount++;
                }
            }
        } else {

            //Here min age is 18 & max age is 65/min term is 10 & max term is 30
            if (PPT == 5) {
                prStr = SSP_DB.getPremiumRate_Arr_LPPT_5();
                prStrArr = cfap.split(prStr, ",");
                for (int i = 18; i < 66; i++) {
                    for (int j = 10; j < 31; j++) {
                        if (i == smartSwadhanPlusBean.getAge()
                                && j == smartSwadhanPlusBean.getPolicyTerm()) {
                            pr = prStrArr[arrCount];
                            prDouble = Double.parseDouble(pr);
                            break;
                        }
                        arrCount++;
                    }
                }
            } else if (PPT == 10) {
                prStr = SSP_DB.getPremiumRate_Arr_LPPT_10();
                prStrArr = cfap.split(prStr, ",");
                for (int i = 18; i < 66; i++) {
                    for (int j = 15; j < 31; j++) {
                        if (i == smartSwadhanPlusBean.getAge()
                                && j == smartSwadhanPlusBean.getPolicyTerm()) {
                            pr = prStrArr[arrCount];
                            prDouble = Double.parseDouble(pr);
                            break;
                        }
                        arrCount++;
                    }
                }
            } else if (PPT == 15) {
                prStr = SSP_DB.getPremiumRate_Arr_LPPT_15();
                prStrArr = cfap.split(prStr, ",");
                for (int i = 18; i < 66; i++) {
                    for (int j = 20; j < 31; j++) {
                        if (i == smartSwadhanPlusBean.getAge()
                                && j == smartSwadhanPlusBean.getPolicyTerm()) {
                            pr = prStrArr[arrCount];
                            prDouble = Double.parseDouble(pr);
                            break;
                        }
                        arrCount++;
                    }
                }
            }
        }
        // System.out.println("rate "+prDouble);
        return prDouble;

    }

    public double getStaffRebate() {
        if (smartSwadhanPlusBean.getStaffDisc()) {
            if (smartSwadhanPlusBean.getPlanType().equals("Single")) {
                return 0.02;
            } else {
                return 0.05;
            }
        } else
            return 0.0;
    }

    private double getSA_Rebate() {
        double saRebate = 0;
        int sumAssured = smartSwadhanPlusBean.getSumAssured();
        if (smartSwadhanPlusBean.getPlanType().equals("LPPT")) {
            if (smartSwadhanPlusBean.getPremiumPayingTerm() == 5) {
                if (sumAssured >= 750000 && sumAssured <= 999900) {
                    saRebate = 0.45;
                } else if (sumAssured >= 1000000 && sumAssured <= 1499900) {
                    saRebate = 1.1;
                } else if (sumAssured >= 1500000 && sumAssured <= 2499900) {
                    saRebate = 1.75;
                } else if (sumAssured >= 2500000 && sumAssured <= 3499900) {
                    saRebate = 2.2;
                } else if (sumAssured >= 3500000 && sumAssured <= 4999900) {
                    saRebate = 2.4;
                } else if (sumAssured >= 5000000) {
                    saRebate = 2.6;
                }
            } else if (smartSwadhanPlusBean.getPremiumPayingTerm() == 10) {
                if (sumAssured >= 750000 && sumAssured <= 999900) {
                    saRebate = 0.4;
                } else if (sumAssured >= 1000000 && sumAssured <= 1499900) {
                    saRebate = 0.8;
                } else if (sumAssured >= 1500000 && sumAssured <= 2499900) {
                    saRebate = 1.2;
                } else if (sumAssured >= 2500000 && sumAssured <= 3499900) {
                    saRebate = 1.5;
                } else if (sumAssured >= 3500000 && sumAssured <= 4999900) {
                    saRebate = 1.7;
                } else if (sumAssured >= 5000000) {
                    saRebate = 1.85;
                }
            }
            if (smartSwadhanPlusBean.getPremiumPayingTerm() == 15) {
                if (sumAssured >= 750000 && sumAssured <= 999900) {
                    saRebate = 0.3;
                } else if (sumAssured >= 1000000 && sumAssured <= 1499900) {
                    saRebate = 0.6;
                } else if (sumAssured >= 1500000 && sumAssured <= 2499900) {
                    saRebate = 1;
                } else if (sumAssured >= 2500000 && sumAssured <= 3499900) {
                    saRebate = 1.25;
                } else if (sumAssured >= 3500000 && sumAssured <= 4999900) {
                    saRebate = 1.4;
                } else if (sumAssured >= 5000000) {
                    saRebate = 1.5;
                }
            }
        } else if (smartSwadhanPlusBean.getPlanType().equals("Single")) {
            if (sumAssured >= 750000 && sumAssured <= 999900) {
                saRebate = 2;
            } else if (sumAssured >= 1000000 && sumAssured <= 1499900) {
                saRebate = 4;
            } else if (sumAssured >= 1500000 && sumAssured <= 2499900) {
                saRebate = 6;
            } else if (sumAssured >= 2500000 && sumAssured <= 3499900) {
                saRebate = 7.5;
            } else if (sumAssured >= 3500000 && sumAssured <= 4999900) {
                saRebate = 8.25;
            } else if (sumAssured >= 5000000) {
                saRebate = 8.9;
            }
        } else {
            if (sumAssured >= 750000 && sumAssured <= 999900) {
                saRebate = 0.3;
            } else if (sumAssured >= 1000000 && sumAssured <= 1499900) {
                saRebate = 0.6;
            } else if (sumAssured >= 1500000 && sumAssured <= 2499900) {
                saRebate = 0.9;
            } else if (sumAssured >= 2500000 && sumAssured <= 3499900) {
                saRebate = 1.2;
            } else if (sumAssured >= 3500000 && sumAssured <= 4999900) {
                saRebate = 1.3;
            } else if (sumAssured >= 5000000) {
                saRebate = 1.4;
            }
        }
        // System.out.println("sarebate "+saRebate);
        return saRebate;
    }

    private double getLoadingForFrequency() {
        if (smartSwadhanPlusBean.getPlanType().equals("Regular")
                || smartSwadhanPlusBean.getPlanType().equals("LPPT")) {
            if (smartSwadhanPlusBean.getPremiumFreq().equals("Yearly")) {
                return 1;
            } else if (smartSwadhanPlusBean.getPremiumFreq().equals(
                    "Half Yearly")) {
                return 1.04;
            } else if (smartSwadhanPlusBean.getPremiumFreq()
                    .equals("Quarterly")) {
                return 1.06;
            } else {
                return 1.068;
            }
        } else
            return 1;
    }

    public double getPremiumWithoutST() {
        double premiumWithoutST = 0;
        int sumAssured = smartSwadhanPlusBean.getSumAssured();
        int temp = 0;
        if (smartSwadhanPlusBean.getPlanType().equals("Single")
                || smartSwadhanPlusBean.getPremiumFreq().equals("Yearly")) {
            temp = 1;
        } else if (smartSwadhanPlusBean.getPremiumFreq().equals("Half Yearly")) {
            temp = 2;
        } else if (smartSwadhanPlusBean.getPremiumFreq().equals("Quarterly")) {
            temp = 4;
        } else if (smartSwadhanPlusBean.getPremiumFreq().equals("Monthly")) {
            temp = 12;
        }
        // System.out.println("getBasicPremiumRate() "+getBasicPremiumRate());
        // System.out.println("getStaffRebate() "+getStaffRebate());
        // System.out.println("getSA_Rebate() "+getSA_Rebate());
        // System.out.println("sumAssured "+sumAssured);
        // System.out.println("getLoadingForFrequency() "+getLoadingForFrequency());
        // System.out.println("temp "+temp);



        /*-------- Modified by Tushar Kotian on 24/09/2018 ----------*/
//		premiumWithoutST=(getBasicPremiumRate() * (1 - getStaffRebate()) - getSA_Rebate()) * sumAssured/1000 * getLoadingForFrequency()/temp;
        premiumWithoutST = Double.parseDouble(cfap.getRoundOffLevel2(cfap.getStringWithout_E((getBasicPremiumRate() * (1 - getStaffRebate()) - getSA_Rebate()) * sumAssured / 1000 * getLoadingForFrequency() / temp)));
        /*-------- Modified by Tushar Kotian on 24/09/2018 ----------*/
        return premiumWithoutST;
    }

    public double setTotalBasePremiumPaidYearly(double totInstPrem_exclST) {
        return totInstPrem_exclST * getPremiumMultiplicationFactor();
    }

    public double setTotalBasePremiumPaidYearly1(double totInstPrem_exclST) {
        /*return totInstPrem_exclST * getPremiumMultiplicationFactor();*/

        double premiumWithoutST1 = 0;
        int sumAssured = smartSwadhanPlusBean.getSumAssured();
        premiumWithoutST1 = Double.parseDouble(cfap.getRoundOffLevel2(cfap.getStringWithout_E((getBasicPremiumRate() * (1 - getStaffRebate()) - getSA_Rebate()) * sumAssured / 1000)));
        /*-------- Modified by Tushar Kotian on 24/09/2018 ----------*/
        return premiumWithoutST1;

    }

    private double getPremiumMultiplicationFactor() {
        if (smartSwadhanPlusBean.getPremiumFreq().equals("Yearly")
                || smartSwadhanPlusBean.getPremiumFreq().equals("Single")) {
            return 1;
        } else if (smartSwadhanPlusBean.getPremiumFreq().equals("Half Yearly")) {
            return 2;
        } else if (smartSwadhanPlusBean.getPremiumFreq().equals("Quarterly")) {
            return 4;
        } else {
            return 12;
        }
    }

    public double getPremiumWithoutSTWithoutDisc() {
        double premiumWithoutSTWithoutDisc = 0;
        int sumAssured = smartSwadhanPlusBean.getSumAssured();
        int temp = 0;
        if (smartSwadhanPlusBean.getPlanType().equals("Single")
                || smartSwadhanPlusBean.getPremiumFreq().equals("Yearly")) {
            temp = 1;
        } else if (smartSwadhanPlusBean.getPremiumFreq().equals("Half Yearly")) {
            temp = 2;
        } else if (smartSwadhanPlusBean.getPremiumFreq().equals("Quarterly")) {
            temp = 4;
        } else if (smartSwadhanPlusBean.getPremiumFreq().equals("Monthly")) {
            temp = 12;
        }
        // System.out.println("getBasicPremiumRate() " + getBasicPremiumRate());
        // System.out.println("getStaffRebate() " + getStaffRebate());
        // System.out.println("getSA_Rebate() " + getSA_Rebate());
        // System.out.println("sumAssured " + sumAssured);
        // System.out.println("getLoadingForFrequency() "
        // + getLoadingForFrequency());
        // System.out.println("temp " + temp);


        /************** Modified by Tushar Kotian on 24/09/2018 ********************/

//		premiumWithoutSTWithoutDisc = (getBasicPremiumRate() * (1 - 0) - getSA_Rebate())
//				* sumAssured / 1000 * getLoadingForFrequency() / temp;

        premiumWithoutSTWithoutDisc = Double.parseDouble(cfap.getRoundOffLevel2New(cfap.getStringWithout_E((getBasicPremiumRate() * (1 - 0) - getSA_Rebate())
                * sumAssured / 1000 * getLoadingForFrequency() / temp)));

        /************** Modified by Tushar Kotian on 24/09/2018 ********************/

        return premiumWithoutSTWithoutDisc;
    }

    public double getPremiumWithoutSTWithoutDiscWithoutFreqLoading() {
        double premiumWithoutSTWithoutDiscWithoutFreqLoading = 0;
        int sumAssured = smartSwadhanPlusBean.getSumAssured();
        int temp = 0;
        if (smartSwadhanPlusBean.getPlanType().equals("Single")
                || smartSwadhanPlusBean.getPremiumFreq().equals("Yearly")) {
            temp = 1;
        } else if (smartSwadhanPlusBean.getPremiumFreq().equals("Half Yearly")) {
            temp = 2;
        } else if (smartSwadhanPlusBean.getPremiumFreq().equals("Quarterly")) {
            temp = 4;
        } else if (smartSwadhanPlusBean.getPremiumFreq().equals("Monthly")) {
            temp = 12;
        }
        // System.out.println("getBasicPremiumRate() "+getBasicPremiumRate());
        // System.out.println("getStaffRebate() "+getStaffRebate());
        // System.out.println("getSA_Rebate() "+getSA_Rebate());
        // System.out.println("sumAssured "+sumAssured);
        // System.out.println("getLoadingForFrequency() "+getLoadingForFrequency());
        // System.out.println("temp "+temp);


        /************************Modified by Tushar Kotian 24/09/2018 **********************/
//		premiumWithoutSTWithoutDiscWithoutFreqLoading = (getBasicPremiumRate()
//				* (1 - 0) - 0)
//				* sumAssured / 1000;

        premiumWithoutSTWithoutDiscWithoutFreqLoading = Double.parseDouble(cfap.getRoundOffLevel2New(cfap.getStringWithout_E((getBasicPremiumRate()
                * (1 - 0) - 0)
                * sumAssured / 1000)));

        /************************Modified by Tushar Kotian 24/09/2018 **********************/

        return premiumWithoutSTWithoutDiscWithoutFreqLoading;
    }

    public double setAnnualPrem(double premium, int year_F) {
        if (year_F <= smartSwadhanPlusBean.getPremiumPayingTerm()) {
            if (smartSwadhanPlusBean.getPlanType().equals("Single")) {
                if (year_F == 1)
                    return premium;
                else
                    return 0;
            } else
                return premium;
        } else
            return 0;
    }

    public double setCummulativePremiumPaid(double sumcummulativePremiumPaid,
                                            int year_F) {
        if (year_F >= 2 && smartSwadhanPlusBean.getPlanType().equals("Single"))
            return 0;
        if (year_F <= smartSwadhanPlusBean.getPremiumPayingTerm())
            return sumcummulativePremiumPaid;
        else
            return 0;
    }

    public double setGuaranteedMaturityBenefit(
            double sumcummulativePremiumPaid, double totInstPrem_exclST,
            int year_F) {
        if (smartSwadhanPlusBean.getPlanType().equals("Single")) {
            if (year_F == smartSwadhanPlusBean.getPolicyTerm())
                return (totInstPrem_exclST * 1);
            else
                return 0;
        } else if (smartSwadhanPlusBean.getPlanType().equals("Regular")) {
            if (year_F == smartSwadhanPlusBean.getPolicyTerm())
                return ((totInstPrem_exclST * smartSwadhanPlusBean
                        .getPolicyTerm()) * 1);
            else
                return 0;
        } else {
            if (year_F == smartSwadhanPlusBean.getPolicyTerm())
                return (sumcummulativePremiumPaid * 1);
            else
                return 0;
        }

    }

//	Added By Saurabh Jain on 08/11/2019 Start

    public double setNonGuaranteedSurrenderValue(int year_F, double ab) {
        double a = 0, b = 0, c = 0, d = 0;
        SmartSwadhanPlusDB SSP_DB = new SmartSwadhanPlusDB();
        String[] prStrArr;
        double prDouble = 0;
        int arrCount = 0;
        String pr = null;
        if (year_F <= smartSwadhanPlusBean.getPremiumPayingTerm()) {

            a = (Double.valueOf(year_F) / Double.valueOf((smartSwadhanPlusBean.getPremiumPayingTerm())));

            b = ab;

            if (smartSwadhanPlusBean.getPlanType().equals("Single")) {

                prStrArr = cfap.split(SSP_DB.getSSVRate_Single(), ",");

                for (int i = 1; i <= 30; i++) {
                    for (int j = 10; j <= 30; j++) {
                        if (i == year_F && j == smartSwadhanPlusBean.getPolicyTerm()) {
                            pr = prStrArr[arrCount];
                            prDouble = Double.parseDouble(pr);
                            break;
                        }
                        arrCount++;
                    }
                }
            } else if (smartSwadhanPlusBean.getPlanType().equals("Regular")) {

                prStrArr = cfap.split(SSP_DB.getSSVRate_Regular(), ",");

                for (int i = 1; i <= 30; i++) {
                    for (int j = 10; j <= 30; j++) {
                        if (i == year_F && j == smartSwadhanPlusBean.getPolicyTerm()) {
                            pr = prStrArr[arrCount];
                            prDouble = Double.parseDouble(pr);
                            break;
                        }
                        arrCount++;
                    }
                }
            } else {

                if (smartSwadhanPlusBean.getPremiumPayingTerm() == 5) {
					/*prStrArr = cfap.split(SSP_DB.getGSVRate_LPPT_5(), ",");
					// prDouble=Double.parseDouble(prStrArr[year_F-1]);
					prDouble = getPremiumRate(year_F, prStrArr);*/

                    prStrArr = cfap.split(SSP_DB.getSSVRate_LPPT_5(), ",");

                    for (int i = 1; i <= 30; i++) {
                        for (int j = 10; j <= 30; j++) {
                            if (i == year_F && j == smartSwadhanPlusBean.getPolicyTerm()) {
                                pr = prStrArr[arrCount];
                                prDouble = Double.parseDouble(pr);
                                break;
                            }
                            arrCount++;
                        }
                    }

                } else if (smartSwadhanPlusBean.getPremiumPayingTerm() == 10) {
					/*prStrArr = cfap.split(SSP_DB.getGSVRate_LPPT_10(), ",");
					// prDouble=Double.parseDouble(prStrArr[year_F-1]);
					prDouble = getPremiumRate(year_F, prStrArr);*/

                    prStrArr = cfap.split(SSP_DB.getSSVRate_LPPT_10(), ",");

                    for (int i = 1; i <= 30; i++) {
                        for (int j = 15; j <= 30; j++) {
                            if (i == year_F && j == smartSwadhanPlusBean.getPolicyTerm()) {
                                pr = prStrArr[arrCount];
                                prDouble = Double.parseDouble(pr);
                                break;
                            }
                            arrCount++;
                        }
                    }
                } else {
					/*prStrArr = cfap.split(SSP_DB.getGSVRate_LPPT_15(), ",");
					// prDouble=Double.parseDouble(prStrArr[year_F-1]);
					prDouble = getPremiumRate(year_F, prStrArr);*/

                    prStrArr = cfap.split(SSP_DB.getSSVRate_LPPT_15(), ",");

                    for (int i = 1; i <= 30; i++) {
                        for (int j = 20; j <= 30; j++) {
                            if (i == year_F && j == smartSwadhanPlusBean.getPolicyTerm()) {
                                pr = prStrArr[arrCount];
                                prDouble = Double.parseDouble(pr);
                                break;
                            }
                            arrCount++;
                        }
                    }
                }
            }
            d = a * b * prDouble;

        } else {

            b = ab;

            if (smartSwadhanPlusBean.getPlanType().equals("Single")) {

                prStrArr = cfap.split(SSP_DB.getSSVRate_Single(), ",");

                for (int i = 1; i <= 30; i++) {
                    for (int j = 10; j <= 30; j++) {
                        if (i == year_F && j == smartSwadhanPlusBean.getPolicyTerm()) {
                            pr = prStrArr[arrCount];
                            prDouble = Double.parseDouble(pr);
                            break;
                        }
                        arrCount++;
                    }
                }
            } else if (smartSwadhanPlusBean.getPlanType().equals("Regular")) {

                prStrArr = cfap.split(SSP_DB.getSSVRate_Regular(), ",");

                for (int i = 1; i <= 30; i++) {
                    for (int j = 10; j <= 30; j++) {
                        if (i == year_F && j == smartSwadhanPlusBean.getPolicyTerm()) {
                            pr = prStrArr[arrCount];
                            prDouble = Double.parseDouble(pr);
                            break;
                        }
                        arrCount++;
                    }
                }
            } else {

                if (smartSwadhanPlusBean.getPremiumPayingTerm() == 5) {
					/*prStrArr = cfap.split(SSP_DB.getGSVRate_LPPT_5(), ",");
					// prDouble=Double.parseDouble(prStrArr[year_F-1]);
					prDouble = getPremiumRate(year_F, prStrArr);*/

                    prStrArr = cfap.split(SSP_DB.getSSVRate_LPPT_5(), ",");

                    for (int i = 1; i <= 30; i++) {
                        for (int j = 10; j <= 30; j++) {
                            if (i == year_F && j == smartSwadhanPlusBean.getPolicyTerm()) {
                                pr = prStrArr[arrCount];
                                prDouble = Double.parseDouble(pr);
                                break;
                            }
                            arrCount++;
                        }
                    }

                } else if (smartSwadhanPlusBean.getPremiumPayingTerm() == 10) {
					/*prStrArr = cfap.split(SSP_DB.getGSVRate_LPPT_10(), ",");
					// prDouble=Double.parseDouble(prStrArr[year_F-1]);
					prDouble = getPremiumRate(year_F, prStrArr);*/

                    prStrArr = cfap.split(SSP_DB.getSSVRate_LPPT_10(), ",");

                    for (int i = 1; i <= 30; i++) {
                        for (int j = 15; j <= 30; j++) {
                            if (i == year_F && j == smartSwadhanPlusBean.getPolicyTerm()) {
                                pr = prStrArr[arrCount];
                                prDouble = Double.parseDouble(pr);
                                break;
                            }
                            arrCount++;
                        }
                    }
                } else {
					/*prStrArr = cfap.split(SSP_DB.getGSVRate_LPPT_15(), ",");
					// prDouble=Double.parseDouble(prStrArr[year_F-1]);
					prDouble = getPremiumRate(year_F, prStrArr);*/

                    prStrArr = cfap.split(SSP_DB.getSSVRate_LPPT_15(), ",");

                    for (int i = 1; i <= 30; i++) {
                        for (int j = 20; j <= 30; j++) {
                            if (i == year_F && j == smartSwadhanPlusBean.getPolicyTerm()) {
                                pr = prStrArr[arrCount];
                                prDouble = Double.parseDouble(pr);
                                break;
                            }
                            arrCount++;
                        }
                    }
                }
            }
            d = b * prDouble;

        }

        return d;
    }

//	Added By Saurabh Jain on 08/11/2019 End

    public double setGuaranteedSurrenderValue(double sumcummulativePremiumPaid,
                                              int year_F) {
        SmartSwadhanPlusDB SSP_DB = new SmartSwadhanPlusDB();
        String[] prStrArr;
        double prDouble = 0;
        int arrCount = 0;
        String pr = null;

        if (year_F <= smartSwadhanPlusBean.getPolicyTerm()) {

            if (smartSwadhanPlusBean.getPlanType().equals("Single")) {
                prStrArr = cfap.split(SSP_DB.getGSVRate_Single(), ",");

//				Modify By Saurabh Jain on 08/11/2019 Start

                prDouble = Double.parseDouble(prStrArr[year_F - 1]);

//				Modify By Saurabh Jain on 08/11/2019 End


//				Commented By Saurabh Jain on 08/11/2019 Start

//				prDouble = getPremiumRate(year_F, prStrArr);

//				Commented By Saurabh Jain on 08/11/2019 End

//					Commented By Saurabh Jain on 08/11/2019 Start

            } /*else if (smartSwadhanPlusBean.getPlanType().equals("Regular")) {

                prStrArr = cfap.split(SSP_DB.getGSVRate_Regular(), ",");
                // prDouble=Double.parseDouble(prStrArr[year_F-1]);
                prDouble = getPremiumRate(year_F, prStrArr);
            } else {
                if (smartSwadhanPlusBean.getPremiumPayingTerm() == 5) {
                    prStrArr = cfap.split(SSP_DB.getGSVRate_LPPT_5(), ",");
                    // prDouble=Double.parseDouble(prStrArr[year_F-1]);
                    prDouble = getPremiumRate(year_F, prStrArr);
                } else if (smartSwadhanPlusBean.getPremiumPayingTerm() == 10) {
                    prStrArr = cfap.split(SSP_DB.getGSVRate_LPPT_10(), ",");
                    // prDouble=Double.parseDouble(prStrArr[year_F-1]);
                    prDouble = getPremiumRate(year_F, prStrArr);
                } else {
                    prStrArr = cfap.split(SSP_DB.getGSVRate_LPPT_15(), ",");
                    // prDouble=Double.parseDouble(prStrArr[year_F-1]);
                    prDouble = getPremiumRate(year_F, prStrArr);
                }
			}*/

//			Commented By Saurabh Jain on 08/11/2019 End

//			Added By Saurabh Jain on 08/11/2019 Start

            else {

                prStrArr = cfap.split(SSP_DB.getGSVRate_Regular_LPPT(), ",");

                for (int i = 1; i <= 30; i++) {
                    for (int j = 10; j <= 30; j++) {
                        if (i == year_F && j == smartSwadhanPlusBean.getPolicyTerm()) {
                            pr = prStrArr[arrCount];
                            prDouble = Double.parseDouble(pr);
                            break;
                        }
                        arrCount++;
                    }
                }
            }
//			Added By Saurabh Jain on 08/11/2019 End
        }
        // System.out.println(" *** prDouble : " + prDouble);

        return sumcummulativePremiumPaid * prDouble;
    }

//		Commented By Saurabh Jain on 08/11/2019
	/*public double getPremiumRate(int year_F, String[] prStrArr) {
        double prDouble = 0;

        if (year_F <= 8)
            prDouble = Double.parseDouble(prStrArr[year_F - 1]);
        else if (year_F == smartSwadhanPlusBean.getPolicyTerm() - 1
                || year_F == smartSwadhanPlusBean.getPolicyTerm())
            prDouble = Double.parseDouble(prStrArr[8]);
        else if (year_F <= smartSwadhanPlusBean.getPolicyTerm())
            prDouble = Double.parseDouble(prStrArr[7]);
        return prDouble;
	}*/

//	Commented By Saurabh Jain on 08/11/2019

    public double setGuaranteedDeathBenefit(int year_F,
                                            double sumcummulativePremiumPaid, double maxguaranSurrenderValue,
                                            double totInstPrem_exclST, double totalBasePremiumPaidYearly) {
        double number1 = 0, number2 = 0, number3 = 0, number4 = 0;
        if (smartSwadhanPlusBean.getPremiumFreq().equals("Single")) {
            number1 = totalBasePremiumPaidYearly * 1.25;
            number2 = maxguaranSurrenderValue;
            number3 = smartSwadhanPlusBean.getSumAssured();
            return Math.max(number1, Math.max(number2, number3));
        } else {
            number1 = totInstPrem_exclST * 10;
            number2 = maxguaranSurrenderValue;
            number3 = smartSwadhanPlusBean.getSumAssured();
            number4 = sumcummulativePremiumPaid;
            return Math.max(Math.max(number1, number2),
                    Math.max(number3, number4));
        }
    }

}
