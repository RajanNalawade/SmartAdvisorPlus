package sbilife.com.pointofsale_bancaagency.needanalysis;

class NeedAnalysisBusinessLogic {
    private ExcelFinFunctions objFin;

    public NeedAnalysisBusinessLogic() {
        // TODO Auto-generated constructor stub

        objFin = new ExcelFinFunctions();
    }

    /******************************* Main Function start ****************************/
    String getNewNeedProdRes(String inputStr) {

        String outputStr = "";
        try {

            String[] inputParam = objFin.parseInputString(inputStr);

            // System.out.println("Length" + inputParam.length);
            if (inputParam.length == 48) {

                Customer customer = new Customer();
                customer.age = Integer.parseInt(inputParam[1]);
                customer.maritalStatus = inputParam[2];// Marital Status
                customer.sex = inputParam[0];// Gender

                customer.dependCh = Integer.parseInt(inputParam[3]);// Dependent
                // Children

                customer.income = Double.parseDouble(inputParam[4]);// Annual
                // income

                customer.totExp = Double.parseDouble(inputParam[5]);// Total
                // expenses
                // Added new fields

                customer.PrOutStaHmLoanAmt = Double.parseDouble(inputParam[6]);
                customer.PrOutStaOthrLoanAmt = Double
                        .parseDouble(inputParam[7]);
                customer.currLifeInCov = Double.parseDouble(inputParam[8]);
                customer.currRetCorpSav = Double.parseDouble(inputParam[9]);
                customer.retiAge = Integer.parseInt(inputParam[10]);// Targeted
                // retirement
                // age
                customer.PrPostRetLife = inputParam[11];

                // Done here
                customer.PrEduCurAge1 = Integer.parseInt(inputParam[12]); // added

                customer.PrEduChBudExpNoYrs1 = Double
                        .parseDouble(inputParam[13]);
                customer.PrEduChBudEXp1 = Double.parseDouble(inputParam[14]);
                customer.PrEduChCurrEXp1 = Double.parseDouble(inputParam[15]);

                customer.PrEduCurAge2 = Integer.parseInt(inputParam[16]); // added

                customer.PrEduChBudExpNoYrs2 = Double
                        .parseDouble(inputParam[17]);
                customer.PrEduChBudEXp2 = Double.parseDouble(inputParam[18]);
                customer.PrEduChCurrEXp2 = Double.parseDouble(inputParam[19]);

                customer.PrEduCurAge3 = Integer.parseInt(inputParam[20]); // added

                customer.PrEduChBudExpNoYrs3 = Double
                        .parseDouble(inputParam[21]);
                customer.PrEduChBudEXp3 = Double.parseDouble(inputParam[22]);
                customer.PrEduChCurrEXp3 = Double.parseDouble(inputParam[23]);

                customer.PrEduCurAge4 = Integer.parseInt(inputParam[24]); // added

                customer.PrEduChBudExpNoYrs4 = Double
                        .parseDouble(inputParam[25]);
                customer.PrEduChBudEXp4 = Double.parseDouble(inputParam[26]);
                customer.PrEduChCurrEXp4 = Double.parseDouble(inputParam[27]);

                customer.PrMaChBudExpNoYrs1 = Double
                        .parseDouble(inputParam[28]);
                customer.PrMaChBudEXp1 = Double.parseDouble(inputParam[29]);
                customer.PrMaChCurrEXp1 = Double.parseDouble(inputParam[30]);
                customer.PrMaChCurrExpNoYrs2 = Double
                        .parseDouble(inputParam[31]);
                customer.PrMaChBudEXp2 = Double.parseDouble(inputParam[32]);
                customer.PrMaChCurrEXp2 = Double.parseDouble(inputParam[33]);
                customer.PrMaChBudExpNoYrs3 = Double
                        .parseDouble(inputParam[34]);
                customer.PrMaChBudEXp3 = Double.parseDouble(inputParam[35]);
                customer.PrMaChCurrEXp3 = Double.parseDouble(inputParam[36]);
                customer.PrMaChBudExpNoYrs4 = Double
                        .parseDouble(inputParam[37]);
                customer.PrMaChBudEXp4 = Double.parseDouble(inputParam[38]);
                customer.PrMaChCurrEXp4 = Double.parseDouble(inputParam[39]);
                customer.PrProWeCrNoofYr = Double.parseDouble(inputParam[40]);
                customer.PrProWeCrBudExp = Double.parseDouble(inputParam[41]);
                customer.PrProWeCrCurrExp = Double.parseDouble(inputParam[42]);
                customer.PrOthFinWeCrNoofYr = Double
                        .parseDouble(inputParam[43]);
                customer.PrOthFinWeCrBudExp = Double
                        .parseDouble(inputParam[44]);
                customer.PrOthFinWeCrCurrExp = Double
                        .parseDouble(inputParam[45]);
                // customer.inflationrate = 0.06;
                customer.inflationrate = Double.parseDouble(inputParam[46]) / 100;
                customer.strRiskAppetite = inputParam[47];

                double riskAppetite = 0.07;
                double riskAppettiteMon = 0.067849745;
                // double retinflationRate =0.0678;

                double cuvGap = 0;
                int tempNoOfYrs = 0;
                double temTotExp = 0;
                double monRetExp = 0;
                double totRetCorpusReq = 0;
                double annuityVal = 0;
                double tempTotCorpus = 0;
                double tempannuityVal = 0;
                double monInvReq = 0;
                double fvOfAvaiCorpus = 0;
                double totGapCorpus = 0;
                double pmtParmam1 = 0;
                double pmtParmam2 = 0;

                if (customer.strRiskAppetite.equals("Conservative")) {
                    riskAppetite = 0.06;
                } else if (customer.strRiskAppetite.equals("Moderate")) {
                    riskAppetite = 0.07;
                } else {
                    riskAppetite = 0.08;
                }

                riskAppettiteMon = getRiskAppetite_Monthly(riskAppetite);
                // End

                // Protection Section

                // Get Factor
                customer.multipFactor = getFactor(customer);
                // Life Cover required
                customer.lifeCovAmt = getLifeCoverage(customer.income,
                        customer.multipFactor, customer.PrOutStaHmLoanAmt,
                        customer.PrOutStaOthrLoanAmt);

                // Gap in protection

                cuvGap = getProtectionGap(customer.lifeCovAmt,
                        customer.currLifeInCov);

                // System.out.println("cuvGap" +
                // objFin.getStringWithout_E(cuvGap));

                // End protection

                // Retirement Planning Start

                // Monthly Expenses at Retirement

                // double inflationRate = customer.inflationrate/100 ; //
                // Moderate
                tempNoOfYrs = customer.retiAge - customer.age;
                temTotExp = customer.totExp / 12;

                monRetExp = objFin.getExpMonExpensesAtRetirement(
                        customer.inflationrate, tempNoOfYrs, temTotExp,
                        customer.PrPostRetLife);// D26=D17
                // System.out.println("monRetExp" + monRetExp);

                // Total corpus Required

                annuityVal = getAnnuityRate(customer);

                tempannuityVal = annuityVal / 1000;

                tempTotCorpus = monRetExp * 12 / tempannuityVal; // D18
                // System.out.println("tot corpus "+tempTotCorpus);
                // Gap in Corpus Required
                // FV of Available Corpus

                fvOfAvaiCorpus = objFin.calcFV(riskAppetite, tempNoOfYrs,
                        customer.currRetCorpSav); // D27

                // System.out.println("riskAppetite" +riskAppetite);
                // System.out.println("tempNoOfYrs" + tempNoOfYrs);
                // System.out.println("customer.currRetCorpSav" +
                // customer.currRetCorpSav);
                // System.out.println("D27= fvOfAvaiCorpus" + fvOfAvaiCorpus);

                totRetCorpusReq = ((monRetExp * 12) / tempannuityVal);

                // totGapCorpus = getTotalGapcorpusReqFReti(monRetExp,
                // fvOfAvaiCorpus, tempannuityVal);
                totGapCorpus = getTotalGapcorpusReqFReti(totRetCorpusReq,
                        fvOfAvaiCorpus);
                // System.out.println("totGapCorpus" +totGapCorpus);
                // Monthly investment required

                pmtParmam1 = riskAppettiteMon / 12;
                pmtParmam2 = tempNoOfYrs * 12;
                monInvReq = objFin.PMT(pmtParmam1, pmtParmam2, 0, totGapCorpus,
                        1);

                // System.out.println("monInvReq" +monInvReq);
                // System.out.println("curr Sav for ret" + fvOfAvaiCorpus);
                // End Retirement Planning

                // Child Future Planning
                double[] childPlan = objFin.getChildFuturePlanning(
                        customer.inflationrate, inputParam, riskAppetite,
                        riskAppettiteMon);

                // End Child Fund Planning

                // Your Aspirations

                double[] homeAsp = new double[4];
                double[] otherAsp = new double[4];

                homeAsp = objFin.getHomeFuturePlanning(customer.inflationrate,
                        inputParam, riskAppetite, riskAppettiteMon);
                otherAsp = objFin.getOTHERFuturePlanning(
                        customer.inflationrate, inputParam, riskAppetite,
                        riskAppettiteMon);

                // End Your Aspirations

                // Protection Plan

                String[] protList = getMyLifeProtectionNeedsProductList(customer);

                customer.lifeProtProdListTrad = protList[0];
                customer.lifeProtProdListUnit = protList[1];

                // System.out.println("Protection" + customer.lifeProtProdList);
                // Protection Plan end

                // Child Plan
                if (Integer.parseInt(inputParam[3]) > 0) {
                    String[] childList = getMyWealthChildNeedsProductList(customer);
                    customer.childProdListTrad = childList[0];
                    customer.childProdListUnit = childList[1];
                } else {
                    customer.childProdListTrad = "";
                    customer.childProdListUnit = "";
                }
                // System.out.println("Child Plan"+ customer.childProdList);
                // Child plan end

                // // Weakth Creation plan
                //
                //				String[] wealthList = getMyWealthSavingNeedsProductList(customer);
                // customer.wealthProdListTrad = wealthList[0];
                // customer.wealthProdListUnit = wealthList[1];
                //
                //				// System.out.println("wealth Plan"+ customer.wealthProdList);
                // // Weakth Creation end
                //
                // // Retirement plan
                //				String[] pensionList = getMyPensionAnnuityNeedsProductList(customer);
                // customer.pesionProdListTrad = pensionList[0];
                // customer.pesionProdListUnit = pensionList[1];

                if (customer.PrProWeCrNoofYr > 0
                        || customer.PrOthFinWeCrNoofYr > 0) {

                    String[] wealthList = getMyWealthSavingNeedsProductList(customer);
                    customer.wealthProdListTrad = wealthList[0];
                    customer.wealthProdListUnit = wealthList[1];
                } else {
                    customer.wealthProdListTrad = "";
                    customer.wealthProdListUnit = "";
                }

                // System.out.println("wealth Plan"+ customer.wealthProdList);
                // Weakth Creation end

                // Retirement plan
                String[] pensionList = getMyPensionAnnuityNeedsProductList(customer);

                if (customer.currRetCorpSav > 0) {
                    customer.pesionProdListTrad = pensionList[0];
                    customer.pesionProdListUnit = pensionList[1];
                } else {
                    customer.pesionProdListTrad = "";
                    customer.pesionProdListUnit = "";
                }
                // System.out.println("Reti Plan"+ customer.pesionProdList);
                // Retirement plan end

                outputStr = "<?xml version='1.0' encoding='utf-8' ?>"
                        + "<NeedAn>" +
                        /*** Added by Akshaya on 26-MAY-2015 start ****/
                        "<LifeProtCorReq>"
                        + objFin.getStringWithout_E(customer.lifeCovAmt)
                        + "</LifeProtCorReq>"
                        +
                        /*** Added by Akshaya on 26-MAY-2015 end ****/
                        "<LifeProtCurrCoverAmt>"
                        + objFin.getStringWithout_E(customer.currLifeInCov)
                        + "</LifeProtCurrCoverAmt>"
                        + "<LifeProtGapAmt>"
                        + objFin.getStringWithout_E(cuvGap)
                        + "</LifeProtGapAmt>"
                        + "<LifeProtTillAge>"
                        + Integer.parseInt(String.valueOf(customer.retiAge))
                        + "</LifeProtTillAge>"
                        +
                        /*** Added by Akshaya on 26-MAY-2015 start ****/
                        "<RetCorReq>"
                        + objFin.getStringWithout_E(Math.round(totRetCorpusReq))
                        + "</RetCorReq>"
                        +
                        /*** Added by Akshaya on 26-MAY-2015 end ****/
                        "<RetCurrSav>"
                        + objFin.getStringWithout_E(objFin
                        .getRound(fvOfAvaiCorpus))
                        + "</RetCurrSav>"
                        + "<RetGap>"
                        + objFin.getStringWithout_E(objFin
                        .getRound(totGapCorpus))
                        + "</RetGap>"
                        + "<RetMonthInv>"
                        + objFin.getStringWithout_E(objFin.getRound(monInvReq))
                        + "</RetMonthInv>"
                        + "<RetYrLeft>"
                        + tempNoOfYrs
                        + "</RetYrLeft>"
                        + "<chEduCorRe1>"
                        + objFin.getStringWithout_E(childPlan[0])
                        + "</chEduCorRe1>"
                        + "<chEdufvCurSaving1>"
                        + objFin.getStringWithout_E(childPlan[1])
                        + "</chEdufvCurSaving1>"
                        + "<chEduGap1>"
                        + objFin.getStringWithout_E(childPlan[2])
                        + "</chEduGap1>"
                        + "<chEduMonInvreq1>"
                        + objFin.getStringWithout_E(childPlan[3])
                        + "</chEduMonInvreq1>"
                        + "<chMarCorRe1>"
                        + objFin.getStringWithout_E(childPlan[4])
                        + "</chMarCorRe1>"
                        + "<chMarfvCurSaving1>"
                        + objFin.getStringWithout_E(childPlan[5])
                        + "</chMarfvCurSaving1>"
                        + "<chMarGap1>"
                        + objFin.getStringWithout_E(childPlan[6])
                        + "</chMarGap1>"
                        + "<chMarMonInvreq1>"
                        + objFin.getStringWithout_E(childPlan[7])
                        + "</chMarMonInvreq1>"
                        + "<chEduCorRe2>"
                        + objFin.getStringWithout_E(childPlan[8])
                        + "</chEduCorRe2>"
                        + "<chEdufvCurSaving2>"
                        + objFin.getStringWithout_E(childPlan[9])
                        + "</chEdufvCurSaving2>"
                        + "<chEduGap2>"
                        + objFin.getStringWithout_E(childPlan[10])
                        + "</chEduGap2>"
                        + "<chEduMonInvreq2>"
                        + objFin.getStringWithout_E(childPlan[11])
                        + "</chEduMonInvreq2>"
                        + "<chMarCorRe2>"
                        + objFin.getStringWithout_E(childPlan[12])
                        + "</chMarCorRe2>"
                        + "<chMarfvCurSaving2>"
                        + objFin.getStringWithout_E(childPlan[13])
                        + "</chMarfvCurSaving2>"
                        + "<chMarGap2>"
                        + objFin.getStringWithout_E(childPlan[14])
                        + "</chMarGap2>"
                        + "<chMarMonInvreq2>"
                        + objFin.getStringWithout_E(childPlan[15])
                        + "</chMarMonInvreq2>"
                        + "<chEduCorRe3>"
                        + objFin.getStringWithout_E(childPlan[16])
                        + "</chEduCorRe3>"
                        + "<chEdufvCurSaving3>"
                        + objFin.getStringWithout_E(childPlan[17])
                        + "</chEdufvCurSaving3>"
                        + "<chEduGap3>"
                        + objFin.getStringWithout_E(childPlan[18])
                        + "</chEduGap3>"
                        + "<chEduMonInvreq3>"
                        + objFin.getStringWithout_E(childPlan[19])
                        + "</chEduMonInvreq3>"
                        + "<chMarCorRe3>"
                        + objFin.getStringWithout_E(childPlan[20])
                        + "</chMarCorRe3>"
                        + "<chMarfvCurSaving3>"
                        + objFin.getStringWithout_E(childPlan[21])
                        + "</chMarfvCurSaving3>"
                        + "<chMarGap3>"
                        + objFin.getStringWithout_E(childPlan[22])
                        + "</chMarGap3>"
                        + "<chMarMonInvreq3>"
                        + objFin.getStringWithout_E(childPlan[23])
                        + "</chMarMonInvreq3>"
                        + "<chEduCorRe4>"
                        + objFin.getStringWithout_E(childPlan[24])
                        + "</chEduCorRe4>"
                        + "<chEdufvCurSaving4>"
                        + objFin.getStringWithout_E(childPlan[25])
                        + "</chEdufvCurSaving4>"
                        + "<chEduGap4>"
                        + objFin.getStringWithout_E(childPlan[26])
                        + "</chEduGap4>"
                        + "<chEduMonInvreq4>"
                        + objFin.getStringWithout_E(childPlan[27])
                        + "</chEduMonInvreq4>"
                        + "<chMarCorRe4>"
                        + objFin.getStringWithout_E(childPlan[28])
                        + "</chMarCorRe4>"
                        + "<chMarfvCurSaving4>"
                        + objFin.getStringWithout_E(childPlan[29])
                        + "</chMarfvCurSaving4>"
                        + "<chMarGap4>"
                        + objFin.getStringWithout_E(childPlan[30])
                        + "</chMarGap4>"
                        + "<chMarMonInvreq4>"
                        + objFin.getStringWithout_E(childPlan[31])
                        + "</chMarMonInvreq4>"
                        + "<homeCorReq>"
                        + objFin.getStringWithout_E(homeAsp[0])
                        + "</homeCorReq>"
                        + "<homeCurSav>"
                        + objFin.getStringWithout_E(homeAsp[1])
                        + "</homeCurSav>"
                        + "<homeGap>"
                        + objFin.getStringWithout_E(homeAsp[2])
                        + "</homeGap>"
                        + "<homeMonInvreq>"
                        + objFin.getStringWithout_E(homeAsp[3])
                        + "</homeMonInvreq>"
                        + "<otheCorReq>"
                        + objFin.getStringWithout_E(otherAsp[0])
                        + "</otheCorReq>"
                        + "<otheCurSav>"
                        + objFin.getStringWithout_E(otherAsp[1])
                        + "</otheCurSav>"
                        + "<otheGap>"
                        + objFin.getStringWithout_E(otherAsp[2])
                        + "</otheGap>"
                        + "<otheMonInvreq>"
                        + objFin.getStringWithout_E(otherAsp[3])
                        + "</otheMonInvreq>"
                        + "<ProdLstLifeProtTrad>"
                        + customer.lifeProtProdListTrad
                        + "</ProdLstLifeProtTrad>"
                        + "<ProdLstLifeProtUnit>"
                        + customer.lifeProtProdListUnit
                        + "</ProdLstLifeProtUnit>"
                        + "<ProdLstChTrad>"
                        + customer.childProdListTrad
                        + "</ProdLstChTrad>"
                        + "<ProdLstChUnit>"
                        + customer.childProdListUnit
                        + "</ProdLstChUnit>"
                        + "<ProdLstWealthCreTrad>"
                        + customer.wealthProdListTrad
                        + "</ProdLstWealthCreTrad>"
                        + "<ProdLstWealthCreUnit>"
                        + customer.wealthProdListUnit
                        + "</ProdLstWealthCreUnit>"
                        + "<ProdLstRetTrad>"
                        + customer.pesionProdListTrad
                        + "</ProdLstRetTrad>"
                        + "<ProdLstRetUnit>"
                        + customer.pesionProdListUnit
                        + "</ProdLstRetUnit>" + "</NeedAn>";
            } else {
                outputStr = "Invalid Input";
            }
            return outputStr;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            outputStr = "Exception " + e.getMessage();

            e.printStackTrace();
        }
        return "";
    }

    /*************************** Main Function end ****************************************/

    private double getFactor(Customer cust) {
        if (cust.age > 17 && cust.age < 31)
            return 15;
        else if (cust.age > 30 && cust.age < 41)
            return 12;
        else if (cust.age > 40 && cust.age < 46)
            return 10;
        else if (cust.age > 45 && cust.age < 51)
            return 8;
        else if (cust.age > 50 && cust.age < 56)
            return 6;
        else if (cust.age > 55 && cust.age < 60)
            return 4;
        else
            // if(cust.age>60 && cust.age<150)
            return 0;

    }

    private double getLifeCoverage(double annualIncome, double afactor,
                                   double outstandingHomeLoan, double outstandingLoan) {
        double lifeCov = 0;
        lifeCov = (annualIncome * afactor) + outstandingHomeLoan
                + outstandingLoan;
        return lifeCov;
    }

    private double getProtectionGap(double reqCovAmt, double curCoverage) {
        double lifeGap = 0;
        double tempVal = 0;
        tempVal = reqCovAmt - curCoverage;
        if (tempVal < 0) {
            lifeGap = 0;
        } else {
            lifeGap = tempVal;
        }
        return lifeGap;
    }

    private double getAnnuityRate(Customer cust) {
        NeedAnalysisDB db = new NeedAnalysisDB();
        String[] prStrArr = objFin.split(db.getAnnuityRates(), ",");

        for (int i = 40; i <= 80; i++) {
            if (cust.retiAge == i) {
                return Double.parseDouble(prStrArr[i - 40]);
            }
        }

        return 0;
    }

    private double getTotalGapcorpusReqFReti(double totRetCorpusReq,
                                             double fvOfAvaiCorpus)
    // double getTotalGapcorpusReqFReti(double expMonIncomeForRetri,double
    // fvOfAvaiCorpus, double annuityVal)
    {
        // double gapVal = 0;
        // double tempVal = 0;
        // tempVal = ((expMonIncomeForRetri * 12) / annuityVal) -
        // fvOfAvaiCorpus;
        // if (tempVal > 0) {
        // gapVal = tempVal;
        // } else {
        // gapVal = 0;
        // }
        // return gapVal;
        double gapVal = 0;
        double tempVal = 0;
        tempVal = totRetCorpusReq - fvOfAvaiCorpus;
        if (tempVal > 0) {
            gapVal = tempVal;
        } else {
            gapVal = 0;
        }
        return gapVal;
    }

    private String[] getMyLifeProtectionNeedsProductList(Customer cust) {

        if (cust.age >= 18 && cust.age <= 25) {
            if (cust.income < 300000) {

                return new String[]{
                        "Sampoorn Cancer Suraksha;2E; UIN: 111N109V03 / Saral Shield ;47;UIN : 111N066V03 / Saral Swadhan+;1J;UIN : 111N092V03 / Poorna Suraksha ;2F; UIN: 111N110V03 ", ""};

            } else if (cust.income >= 300000 && cust.income < 500000) {
                return new String[]{
                        "Sampoorn Cancer Suraksha;2E; UIN: 111N109V03 / Smart Shield ;45;UIN : 111N067V07 / Saral Swadhan+;1J;UIN : 111N092V03 / Poorna Suraksha ;2F; UIN: 111N110V03 ", ""};
            } else if (cust.income >= 500000 && cust.income < 1000000) {
                return new String[]{
                        "Sampoorn Cancer Suraksha;2E; UIN: 111N109V03 / Smart Shield ;45;UIN : 111N067V07 / Smart Swadhan Plus;1Z;UIN : 111N104V02 / Poorna Suraksha ;2F; UIN: 111N110V03 ", ""};
            } else if (cust.income >= 1000000 && cust.income < 2000000) {
                return new String[]{
                        "Sampoorn Cancer Suraksha;2E; UIN: 111N109V03 / Smart Shield ;45;UIN : 111N067V07 / Smart Swadhan Plus;1Z;UIN : 111N104V02 / Poorna Suraksha ;2F; UIN: 111N110V03 ", ""};
            } else if (cust.income >= 2000000) {
                return new String[]{
                        "Sampoorn Cancer Suraksha;2E; UIN: 111N109V03 / Smart Shield ;45;UIN : 111N067V07 / Poorna Suraksha ;2F; UIN: 111N110V03 ", ""};
            }
        } else if (cust.age >= 26 && cust.age <= 35) {
            if (cust.income < 300000) {

                return new String[]{
                        "Sampoorn Cancer Suraksha;2E; UIN: 111N109V03 / Saral Shield ;47;UIN : 111N066V03 / Saral Swadhan+;1J;UIN : 111N092V03 / Poorna Suraksha ;2F; UIN: 111N110V03 ", ""};

            } else if (cust.income >= 300000 && cust.income < 500000) {
                return new String[]{
                        "Sampoorn Cancer Suraksha;2E; UIN: 111N109V03 / Smart Shield ;45;UIN : 111N067V07 / Saral Swadhan+;1J;UIN : 111N092V03 / Poorna Suraksha ;2F; UIN: 111N110V03 ", ""};
            } else if (cust.income >= 500000 && cust.income < 1000000) {
                return new String[]{
                        "Sampoorn Cancer Suraksha;2E; UIN: 111N109V03 / Smart Shield ;45;UIN : 111N067V07 / Smart Swadhan Plus;1Z;UIN : 111N104V02 / Poorna Suraksha ;2F; UIN: 111N110V03 ", ""};
            } else if (cust.income >= 1000000 && cust.income < 2000000) {
                return new String[]{
                        "Sampoorn Cancer Suraksha;2E; UIN: 111N109V03 / Smart Shield ;45;UIN : 111N067V07 / Smart Swadhan Plus;1Z;UIN : 111N104V02 / Poorna Suraksha ;2F; UIN: 111N110V03 ", ""};
            } else if (cust.income >= 2000000) {
                return new String[]{
                        "Sampoorn Cancer Suraksha;2E; UIN: 111N109V03 / Smart Shield ;45;UIN : 111N067V07 / Poorna Suraksha ;2F; UIN: 111N110V03 ", ""};
            }
        } else if (cust.age >= 36 && cust.age <= 45) {
            if (cust.income < 300000) {
                return new String[]{
                        "Sampoorn Cancer Suraksha;2E; UIN: 111N109V03 / Saral Shield ;47;UIN : 111N066V03 / Saral Swadhan+;1J;UIN : 111N092V03 / Poorna Suraksha ;2F; UIN: 111N110V03 ", ""};

            } else if (cust.income >= 300000 && cust.income < 500000) {
                return new String[]{
                        "Sampoorn Cancer Suraksha;2E; UIN: 111N109V03 / Smart Shield ;45;UIN : 111N067V07 / Saral Swadhan+;1J;UIN : 111N092V03 / Poorna Suraksha ;2F; UIN: 111N110V03 ", ""};
            } else if (cust.income >= 500000 && cust.income < 1000000) {
                return new String[]{
                        "Sampoorn Cancer Suraksha;2E; UIN: 111N109V03 / Smart Shield ;45;UIN : 111N067V07 / Smart Swadhan Plus;1Z;UIN : 111N104V02 / Poorna Suraksha ;2F; UIN: 111N110V03 ", ""};
            } else if (cust.income >= 1000000 && cust.income < 2000000) {
                return new String[]{
                        "Sampoorn Cancer Suraksha;2E; UIN: 111N109V03 / Smart Shield ;45;UIN : 111N067V07 / Smart Swadhan Plus;1Z;UIN : 111N104V02 / Poorna Suraksha ;2F; UIN: 111N110V03 ", ""};
            } else if (cust.income >= 2000000) {
                return new String[]{
                        "Sampoorn Cancer Suraksha;2E; UIN: 111N109V03 / Smart Shield ;45;UIN : 111N067V07 / Poorna Suraksha ;2F; UIN: 111N110V03 ", ""};
            }
        } else if (cust.age >= 46 && cust.age <= 55) {
            if (cust.income < 300000) {
                return new String[]{
                        "Sampoorn Cancer Suraksha;2E; UIN: 111N109V03 / Saral Shield ;47;UIN : 111N066V03 / Saral Swadhan+;1J;UIN : 111N092V03 / Poorna Suraksha ;2F; UIN: 111N110V03 ", ""};

            } else if (cust.income >= 300000 && cust.income < 500000) {
                return new String[]{
                        "Sampoorn Cancer Suraksha;2E; UIN: 111N109V03 / Smart Shield ;45;UIN : 111N067V07 / Saral Swadhan+;1J;UIN : 111N092V03 / Poorna Suraksha ;2F; UIN: 111N110V03 ", ""};
            } else if (cust.income >= 500000 && cust.income < 1000000) {
                return new String[]{
                        "Sampoorn Cancer Suraksha;2E; UIN: 111N109V03 / Smart Shield ;45;UIN : 111N067V07 / Smart Swadhan Plus;1Z;UIN : 111N104V02 / Poorna Suraksha ;2F; UIN: 111N110V03 ", ""};
            } else if (cust.income >= 1000000 && cust.income < 2000000) {
                return new String[]{
                        "Sampoorn Cancer Suraksha;2E; UIN: 111N109V03 / Smart Shield ;45;UIN : 111N067V07 / Smart Swadhan Plus;1Z;UIN : 111N104V02 / Poorna Suraksha ;2F; UIN: 111N110V03 ", ""};
            } else if (cust.income >= 2000000) {
                return new String[]{
                        "Sampoorn Cancer Suraksha;2E; UIN: 111N109V03 / Smart Shield ;45;UIN : 111N067V07 / Poorna Suraksha ;2F; UIN: 111N110V03 ", ""};
            }
        } else if (cust.age >= 56) {
            if (cust.income < 300000) {
                return new String[]{
                        "Sampoorn Cancer Suraksha;2E; UIN: 111N109V03 / Saral Shield ;47;UIN : 111N066V03 / Poorna Suraksha ;2F; UIN: 111N110V03 ", ""};

            } else if (cust.income >= 300000 && cust.income < 500000) {
                return new String[]{
                        "Sampoorn Cancer Suraksha;2E; UIN: 111N109V03 / Smart Shield ;45;UIN : 111N067V07 / Poorna Suraksha ;2F; UIN: 111N110V03 ", ""};
            } else if (cust.income >= 500000 && cust.income < 1000000) {
                return new String[]{
                        "Sampoorn Cancer Suraksha;2E; UIN: 111N109V03 / Smart Shield ;45;UIN : 111N067V07 / Smart Swadhan Plus;1Z;UIN : 111N104V02 / Poorna Suraksha ;2F; UIN: 111N110V03 ", ""};
            } else if (cust.income >= 1000000 && cust.income < 2000000) {
                return new String[]{
                        "Sampoorn Cancer Suraksha;2E; UIN: 111N109V03 / Smart Shield ;45;UIN : 111N067V07 / Smart Swadhan Plus;1Z;UIN : 111N104V02 / Poorna Suraksha ;2F; UIN: 111N110V03 ", ""};
            } else if (cust.income >= 2000000) {
                return new String[]{
                        "Sampoorn Cancer Suraksha;2E; UIN: 111N109V03 / Smart Shield ;45;UIN : 111N067V07 / Poorna Suraksha ;2F; UIN: 111N110V03 ", ""};
            }
        }

        return new String[]{"", ""};

    }

    private String[] getMyWealthChildNeedsProductList(Customer cust) {

        if (cust.PrEduCurAge1 <= 13 && cust.PrEduCurAge2 <= 13
                && cust.PrEduCurAge3 <= 13 && cust.PrEduCurAge4 <= 13) {
            if (cust.age >= 18 && cust.age <= 25) {
                if (cust.income < 300000) {
                    return new String[]{
                            "Smart Champ Insurance ;1P;UIN : 111N098V03", ""};
                } else if (cust.income >= 300000 && cust.income < 500000) {
                    if (cust.strRiskAppetite.equals("Conservative")) {
                        return new String[]{
                                "Smart Champ Insurance ;1P;UIN : 111N098V03",
                                ""};
                    } else if (cust.strRiskAppetite.equals("Moderate")) {
                        return new String[]{
                                "Smart Champ Insurance ;1P;UIN : 111N098V03",
                                "Smart Scholar# ;51;UIN : 111L073V03"};
                    } else // Aggressive
                    {
                        return new String[]{"",
                                "Smart Scholar ;51;UIN : 111L073V03"};
                    }
                } else if (cust.income >= 500000 && cust.income < 1000000) {
                    if (cust.strRiskAppetite.equals("Conservative")) {
                        return new String[]{
                                "Smart Champ Insurance ;1P;UIN : 111N098V03",
                                ""};
                    } else if (cust.strRiskAppetite.equals("Moderate")) {
                        return new String[]{
                                "Smart Champ Insurance ;1P;UIN : 111N098V03",
                                "Smart Scholar# ;51;UIN : 111L073V03"};
                    } else // Aggressive
                    {
                        return new String[]{"",
                                "Smart Scholar ;51;UIN : 111L073V03"};
                    }
                } else if (cust.income >= 1000000 && cust.income < 2000000) {
                    if (cust.strRiskAppetite.equals("Conservative")) {
                        return new String[]{
                                "Smart Champ Insurance ;1P;UIN : 111N098V03",
                                ""};
                    } else if (cust.strRiskAppetite.equals("Moderate")) {
                        return new String[]{
                                "Smart Champ Insurance ;1P;UIN : 111N098V03",
                                "Smart Scholar# ;51;UIN : 111L073V03"};
                    } else // Aggressive
                    {
                        return new String[]{"",
                                "Smart Scholar ;51;UIN : 111L073V03"};
                    }
                } else if (cust.income >= 2000000) {
                    if (cust.strRiskAppetite.equals("Conservative")) {
                        return new String[]{
                                "Smart Champ Insurance ;1P;UIN : 111N098V03",
                                ""};
                    } else if (cust.strRiskAppetite.equals("Moderate")) {
                        return new String[]{
                                "Smart Champ Insurance ;1P;UIN : 111N098V03",
                                "Smart Scholar# ;51;UIN : 111L073V03"};
                    } else // Aggressive
                    {
                        return new String[]{"",
                                "Smart Scholar ;51;UIN : 111L073V03"};
                    }
                }
            } else if (cust.age >= 26 && cust.age <= 35) {
                if (cust.income < 300000) {
                    return new String[]{
                            "Smart Champ Insurance ;1P;UIN : 111N098V03", ""};
                } else if (cust.income >= 300000 && cust.income < 500000) {
                    if (cust.strRiskAppetite.equals("Conservative")) {
                        return new String[]{
                                "Smart Champ Insurance ;1P;UIN : 111N098V03",
                                ""};
                    } else if (cust.strRiskAppetite.equals("Moderate")) {
                        return new String[]{
                                "Smart Champ Insurance ;1P;UIN : 111N098V03",
                                "Smart Scholar# ;51;UIN : 111L073V03"};
                    } else // Aggressive
                    {
                        return new String[]{"",
                                "Smart Scholar ;51;UIN : 111L073V03"};
                    }
                } else if (cust.income >= 500000 && cust.income < 1000000) {
                    if (cust.strRiskAppetite.equals("Conservative")) {
                        return new String[]{
                                "Smart Champ Insurance ;1P;UIN : 111N098V03",
                                ""};
                    } else if (cust.strRiskAppetite.equals("Moderate")) {
                        return new String[]{
                                "Smart Champ Insurance ;1P;UIN : 111N098V03",
                                "Smart Scholar# ;51;UIN : 111L073V03"};
                    } else // Aggressive
                    {
                        return new String[]{"",
                                "Smart Scholar ;51;UIN : 111L073V03"};
                    }
                } else if (cust.income >= 1000000 && cust.income < 2000000) {
                    if (cust.strRiskAppetite.equals("Conservative")) {
                        return new String[]{
                                "Smart Champ Insurance ;1P;UIN : 111N098V03",
                                ""};
                    } else if (cust.strRiskAppetite.equals("Moderate")) {
                        return new String[]{
                                "Smart Champ Insurance ;1P;UIN : 111N098V03",
                                "Smart Scholar# ;51;UIN : 111L073V03"};
                    } else // Aggressive
                    {
                        return new String[]{"",
                                "Smart Scholar ;51;UIN : 111L073V03"};
                    }
                } else if (cust.income >= 2000000) {
                    if (cust.strRiskAppetite.equals("Conservative")) {
                        return new String[]{
                                "Smart Champ Insurance ;1P;UIN : 111N098V03",
                                ""};
                    } else if (cust.strRiskAppetite.equals("Moderate")) {
                        return new String[]{
                                "Smart Champ Insurance ;1P;UIN : 111N098V03",
                                "Smart Scholar# ;51;UIN : 111L073V03"};
                    } else // Aggressive
                    {
                        return new String[]{"",
                                "Smart Scholar ;51;UIN : 111L073V03"};
                    }
                }
            } else if (cust.age >= 36 && cust.age <= 45) {
                if (cust.income < 300000) {
                    return new String[]{
                            "Smart Champ Insurance ;1P;UIN : 111N098V03", ""};
                } else if (cust.income >= 300000 && cust.income < 500000) {
                    if (cust.strRiskAppetite.equals("Conservative")) {
                        return new String[]{
                                "Smart Champ Insurance ;1P;UIN : 111N098V03",
                                ""};
                    } else if (cust.strRiskAppetite.equals("Moderate")) {
                        return new String[]{
                                "Smart Champ Insurance ;1P;UIN : 111N098V03",
                                "Smart Scholar# ;51;UIN : 111L073V03"};
                    } else // Aggressive
                    {
                        return new String[]{"",
                                "Smart Scholar ;51;UIN : 111L073V03"};
                    }
                } else if (cust.income >= 500000 && cust.income < 1000000) {
                    if (cust.strRiskAppetite.equals("Conservative")) {
                        return new String[]{
                                "Smart Champ Insurance ;1P;UIN : 111N098V03",
                                ""};
                    } else if (cust.strRiskAppetite.equals("Moderate")) {
                        return new String[]{
                                "Smart Champ Insurance ;1P;UIN : 111N098V03",
                                "Smart Scholar ;51;UIN : 111L073V03"};
                    } else // Aggressive
                    {
                        return new String[]{"",
                                "Smart Scholar ;51;UIN : 111L073V03"};
                    }
                } else if (cust.income >= 1000000 && cust.income < 2000000) {
                    if (cust.strRiskAppetite.equals("Conservative")) {
                        return new String[]{
                                "Smart Champ Insurance ;1P;UIN : 111N098V03",
                                ""};
                    } else if (cust.strRiskAppetite.equals("Moderate")) {
                        return new String[]{
                                "Smart Champ Insurance ;1P;UIN : 111N098V03",
                                "Smart Scholar# ;51;UIN : 111L073V03"};
                    } else // Aggressive
                    {
                        return new String[]{"",
                                "Smart Scholar ;51;UIN : 111L073V03"};
                    }
                } else if (cust.income >= 2000000) {
                    if (cust.strRiskAppetite.equals("Conservative")) {
                        return new String[]{
                                "Smart Champ Insurance ;1P;UIN : 111N098V03",
                                ""};
                    } else if (cust.strRiskAppetite.equals("Moderate")) {
                        return new String[]{
                                "Smart Champ Insurance ;1P;UIN : 111N098V03",
                                "Smart Scholar# ;51;UIN : 111L073V03"};
                    } else // Aggressive
                    {
                        return new String[]{"",
                                "Smart Scholar ;51;UIN : 111L073V03"};
                    }
                }
            } else if (cust.age >= 46 && cust.age <= 55) {
                if (cust.income < 300000) {
                    return new String[]{"", ""};
                } else if (cust.income >= 300000 && cust.income < 500000) {
                    if (cust.strRiskAppetite.equals("Conservative")) {
                        return new String[]{
                                "Smart Champ Insurance ;1P;UIN : 111N098V03",
                                ""};
                    } else if (cust.strRiskAppetite.equals("Moderate")) {
                        return new String[]{
                                "Smart Champ Insurance ;1P;UIN : 111N098V03",
                                "Smart Scholar ;51;UIN : 111L073V03"};
                    } else // Aggressive
                    {
                        return new String[]{"",
                                "Smart Scholar ;51;UIN : 111L073V03"};
                    }
                } else if (cust.income >= 500000 && cust.income < 1000000) {
                    if (cust.strRiskAppetite.equals("Conservative")) {
                        return new String[]{"", ""};
                    } else if (cust.strRiskAppetite.equals("Moderate")) {
                        return new String[]{"", ""};
                    } else // Aggressive
                    {
                        return new String[]{"",
                                "Smart Scholar ;51;UIN : 111L073V03"};
                    }
                } else if (cust.income >= 1000000 && cust.income < 2000000) {
                    if (cust.strRiskAppetite.equals("Conservative")) {
                        return new String[]{"", ""};
                    } else if (cust.strRiskAppetite.equals("Moderate")) {
                        return new String[]{"", ""};
                    } else // Aggressive
                    {
                        return new String[]{"",
                                "Smart Scholar ;51;UIN : 111L073V03"};
                    }
                } else if (cust.income >= 2000000) {
                    if (cust.strRiskAppetite.equals("Conservative")) {
                        return new String[]{"", ""};
                    } else if (cust.strRiskAppetite.equals("Moderate")) {
                        return new String[]{"", ""};
                    } else // Aggressive
                    {
                        return new String[]{"",
                                "Smart Scholar ;51;UIN : 111L073V03"};
                    }
                }
            } else if (cust.age >= 56) {
                if (cust.income < 300000) {
                    return new String[]{"", ""};
                } else if (cust.income >= 300000 && cust.income < 1000000) {
                    if (cust.strRiskAppetite.equals("Conservative")) {
                        return new String[]{"", ""};
                    } else if (cust.strRiskAppetite.equals("Moderate")) {
                        return new String[]{"", ""};
                    } else // Aggressive
                    {
                        return new String[]{"", ""};
                    }
                } else if (cust.income >= 1000000 && cust.income < 2000000) {
                    if (cust.strRiskAppetite.equals("Conservative")) {
                        return new String[]{"", ""};
                    } else if (cust.strRiskAppetite.equals("Moderate")) {
                        return new String[]{"", ""};
                    } else // Aggressive
                    {
                        return new String[]{"", ""};
                    }
                } else if (cust.income >= 2000000) {
                    if (cust.strRiskAppetite.equals("Conservative")) {
                        return new String[]{"", ""};
                    } else if (cust.strRiskAppetite.equals("Moderate")) {
                        return new String[]{"", ""};
                    } else // Aggressive
                    {
                        return new String[]{"", ""};
                    }
                }
            }
        }
        return new String[]{"", ""};

    }

    //Retirement Plans
    private String[] getMyPensionAnnuityNeedsProductList(Customer cust) {

        if (cust.age >= 18 && cust.age <= 25) {
            if (cust.income < 300000) {
                return new String[]{"", ""};
            } else if (cust.income >= 300000 && cust.income < 500000) {
                return new String[]{"", ""};
            } else if (cust.income >= 500000 && cust.income < 1000000) {
                return new String[]{"", ""};
            } else if (cust.income >= 1000000 && cust.income < 2000000) {
                return new String[]{"", ""};
            } else if (cust.income >= 2000000) {
                return new String[]{"", ""};
            }
        } else if (cust.age >= 26 && cust.age <= 35) {

            if (cust.income < 300000) {
                return new String[]{"Saral Retirement Saver ;1E;UIN : 111N088V03", ""};
            } else if (cust.income >= 300000 && cust.income < 500000) {
                if (cust.strRiskAppetite.equals("Conservative")) {
                    return new String[]{"Saral Retirement Saver ;1E;UIN : 111N088V03",
                            "Retire Smart ;1H;UIN : 111L094V02"};
                } else if (cust.strRiskAppetite.equals("Moderate")) {
                    return new String[]{"Saral Retirement Saver ;1E;UIN : 111N088V03",
                            "Retire Smart ;1H;UIN : 111L094V02"};
                } else // Aggressive
                {
                    return new String[]{"",
                            "Retire Smart ;1H;UIN : 111L094V02"};
                }
            } else if (cust.income >= 500000 && cust.income < 1000000) {
                if (cust.strRiskAppetite.equals("Conservative")) {
                    return new String[]{"Saral Retirement Saver ;1E;UIN : 111N088V03",
                            "Retire Smart ;1H;UIN : 111L094V02"};
                } else if (cust.strRiskAppetite.equals("Moderate")) {
                    return new String[]{"Saral Retirement Saver ;1E;UIN : 111N088V03",
                            "Retire Smart ;1H;UIN : 111L094V02"};
                } else // Aggressive
                {
                    return new String[]{"",
                            "Retire Smart ;1H;UIN : 111L094V02"};
                }
            } else if (cust.income >= 1000000 && cust.income < 2000000) {
                if (cust.strRiskAppetite.equals("Conservative")) {
                    return new String[]{"Saral Retirement Saver ;1E;UIN : 111N088V03",
                            "Retire Smart ;1H;UIN : 111L094V02"};
                } else if (cust.strRiskAppetite.equals("Moderate")) {
                    return new String[]{"Saral Retirement Saver ;1E;UIN : 111N088V03",
                            "Retire Smart ;1H;UIN : 111L094V02"};
                } else // Aggressive
                {
                    return new String[]{"",
                            "Retire Smart ;1H;UIN : 111L094V02"};
                }
            } else if (cust.income >= 2000000) {
                if (cust.strRiskAppetite.equals("Conservative")) {
                    return new String[]{"Saral Retirement Saver ;1E;UIN : 111N088V03",
                            "Retire Smart ;1H;UIN : 111L094V02"};
                } else if (cust.strRiskAppetite.equals("Moderate")) {
                    return new String[]{"Saral Retirement Saver ;1E;UIN : 111N088V03",
                            "Retire Smart ;1H;UIN : 111L094V02"};
                } else // Aggressive
                {
                    return new String[]{"",
                            "Retire Smart ;1H;UIN : 111L094V02"};
                }
            }
        } else if (cust.age >= 36 && cust.age <= 45) {

            if (cust.income < 300000) {
                return new String[]{"Saral Retirement Saver ;1E;UIN : 111N088V03", ""};
            } else if (cust.income >= 300000 && cust.income < 500000) {
                if (cust.strRiskAppetite.equals("Conservative")) {
                    return new String[]{"Saral Retirement Saver ;1E;UIN : 111N088V03",
                            "Retire Smart ;1H;UIN : 111L094V02"};
                } else if (cust.strRiskAppetite.equals("Moderate")) {
                    return new String[]{"Saral Retirement Saver ;1E;UIN : 111N088V03",
                            "Retire Smart ;1H;UIN : 111L094V02"};
                } else // Aggressive
                {
                    return new String[]{"",
                            "Retire Smart ;1H;UIN : 111L094V02"};
                }
            } else if (cust.income >= 500000 && cust.income < 1000000) {
                if (cust.strRiskAppetite.equals("Conservative")) {
                    return new String[]{"Saral Retirement Saver ;1E;UIN : 111N088V03",
                            "Retire Smart ;1H;UIN : 111L094V02"};
                } else if (cust.strRiskAppetite.equals("Moderate")) {
                    return new String[]{"Saral Retirement Saver ;1E;UIN : 111N088V03",
                            "Retire Smart ;1H;UIN : 111L094V02"};
                } else // Aggressive
                {
                    return new String[]{"",
                            "Retire Smart ;1H;UIN : 111L094V02"};
                }
            } else if (cust.income >= 1000000 && cust.income < 2000000) {
                if (cust.strRiskAppetite.equals("Conservative")) {
                    return new String[]{"Saral Retirement Saver ;1E;UIN : 111N088V03",
                            "Retire Smart ;1H;UIN : 111L094V02"};
                } else if (cust.strRiskAppetite.equals("Moderate")) {
                    return new String[]{"Saral Retirement Saver ;1E;UIN : 111N088V03",
                            "Retire Smart ;1H;UIN : 111L094V02"};
                } else // Aggressive
                {
                    return new String[]{"",
                            "Retire Smart ;1H;UIN : 111L094V02"};
                }
            } else if (cust.income >= 2000000) {
                if (cust.strRiskAppetite.equals("Conservative")) {
                    return new String[]{"Saral Retirement Saver ;1E;UIN : 111N088V03",
                            "Retire Smart ;1H;UIN : 111L094V02"};
                } else if (cust.strRiskAppetite.equals("Moderate")) {
                    return new String[]{"Saral Retirement Saver ;1E;UIN : 111N088V03",
                            "Retire Smart ;1H;UIN : 111L094V02"};
                } else // Aggressive
                {
                    return new String[]{"",
                            "Retire Smart ;1H;UIN : 111L094V02"};
                }
            }
        } else if (cust.age >= 46 && cust.age <= 55) {

            if (cust.income < 300000) {
                return new String[]{"Saral Retirement Saver ;1E;UIN : 111N088V03/ Annuity Plus ;22;UIN : 111N083V11", ""};
            } else if (cust.income >= 300000 && cust.income < 500000) {
                if (cust.strRiskAppetite.equals("Conservative")) {
                    return new String[]{"Saral Retirement Saver ;1E;UIN : 111N088V03/ Annuity Plus ;22;UIN : 111N083V11", "Retire Smart ;1H;UIN : 111L094V02"};
                } else if (cust.strRiskAppetite.equals("Moderate")) {
                    return new String[]{"Saral Retirement Saver ;1E;UIN : 111N088V03/ Annuity Plus ;22;UIN : 111N083V11", "Retire Smart ;1H;UIN : 111L094V02"};
                } else // Aggressive
                {
                    return new String[]{"Annuity Plus ;22;UIN : 111N083V11", "Retire Smart ;1H;UIN : 111L094V02"};
                }
            } else if (cust.income >= 500000 && cust.income < 1000000) {
                if (cust.strRiskAppetite.equals("Conservative")) {
                    return new String[]{"Saral Retirement Saver ;1E;UIN : 111N088V03/ Annuity Plus ;22;UIN : 111N083V11", "Retire Smart ;1H;UIN : 111L094V02"};
                } else if (cust.strRiskAppetite.equals("Moderate")) {
                    return new String[]{"Saral Retirement Saver ;1E;UIN : 111N088V03/ Annuity Plus ;22;UIN : 111N083V11", "Retire Smart ;1H;UIN : 111L094V02"};
                } else // Aggressive
                {
                    return new String[]{"/ Annuity Plus ;22;UIN : 111N083V11", "Retire Smart ;1H;UIN : 111L094V02"};
                }
            } else if (cust.income >= 1000000 && cust.income < 2000000) {
                if (cust.strRiskAppetite.equals("Conservative")) {
                    return new String[]{"Saral Retirement Saver ;1E;UIN : 111N088V03/ Annuity Plus ;22;UIN : 111N083V11", "Retire Smart ;1H;UIN : 111L094V02"};
                } else if (cust.strRiskAppetite.equals("Moderate")) {
                    return new String[]{"Saral Retirement Saver ;1E;UIN : 111N088V03/ Annuity Plus ;22;UIN : 111N083V11", "Retire Smart ;1H;UIN : 111L094V02"};
                } else // Aggressive
                {
                    return new String[]{"/ Annuity Plus ;22;UIN : 111N083V11", "Retire Smart ;1H;UIN : 111L094V02"};
                }
            } else if (cust.income >= 2000000) {
                if (cust.strRiskAppetite.equals("Conservative")) {
                    return new String[]{"Saral Retirement Saver ;1E;UIN : 111N088V03/ Annuity Plus ;22;UIN : 111N083V11", "Retire Smart ;1H;UIN : 111L094V02"};
                } else if (cust.strRiskAppetite.equals("Moderate")) {
                    return new String[]{"Saral Retirement Saver ;1E;UIN : 111N088V03/ Annuity Plus ;22;UIN : 111N083V11", "Retire Smart ;1H;UIN : 111L094V02"};
                } else // Aggressive
                {
                    return new String[]{"/ Annuity Plus ;22;UIN : 111N083V11", "Retire Smart ;1H;UIN : 111L094V02"};
                }
            }
        } else if (cust.age >= 56) {
            if (cust.income < 300000) {
                return new String[]{
                        "Saral Retirement Saver ;1E;UIN : 111N088V03 / Annuity Plus ;22;UIN : 111N083V11",
                        ""};
            } else if (cust.income >= 300000 && cust.income < 500000) {
                if (cust.strRiskAppetite.equals("Conservative")) {
                    return new String[]{
                            "Saral Retirement Saver ;1E;UIN : 111N088V03 / Annuity Plus ;22;UIN : 111N083V11",
                            "Retire Smart ;1H;UIN : 111L094V02"};
                } else if (cust.strRiskAppetite.equals("Moderate")) {
                    return new String[]{
                            "Saral Retirement Saver ;1E;UIN : 111N088V03 / Annuity Plus ;22;UIN : 111N083V11",
                            "Retire Smart ;1H;UIN : 111L094V02"};
                } else // Aggressive
                {
                    return new String[]{"Annuity Plus ;22;UIN : 111N083V11",
                            "Retire Smart ;1H;UIN : 111L094V02"};
                }
            } else if (cust.income >= 500000 && cust.income < 1000000) {
                if (cust.strRiskAppetite.equals("Conservative")) {
                    return new String[]{
                            "Saral Retirement Saver ;1E;UIN : 111N088V03 / Annuity Plus ;22;UIN : 111N083V11",
                            "Retire Smart ;1H;UIN : 111L094V02"};
                } else if (cust.strRiskAppetite.equals("Moderate")) {
                    return new String[]{
                            "Saral Retirement Saver ;1E;UIN : 111N088V03 / Annuity Plus ;22;UIN : 111N083V11",
                            "Retire Smart ;1H;UIN : 111L094V02"};
                } else // Aggressive
                {
                    return new String[]{"Annuity Plus ;22;UIN : 111N083V11",
                            "Retire Smart ;1H;UIN : 111L094V02"};
                }
            } else if (cust.income >= 1000000 && cust.income < 2000000) {
                if (cust.strRiskAppetite.equals("Conservative")) {
                    return new String[]{
                            "Saral Retirement Saver ;1E;UIN : 111N088V03 / Annuity Plus ;22;UIN : 111N083V11",
                            "Retire Smart ;1H;UIN : 111L094V02"};
                } else if (cust.strRiskAppetite.equals("Moderate")) {
                    return new String[]{
                            "Saral Retirement Saver ;1E;UIN : 111N088V03 / Annuity Plus ;22;UIN : 111N083V11",
                            "Retire Smart ;1H;UIN : 111L094V02"};
                } else // Aggressive
                {
                    return new String[]{"Annuity Plus ;22;UIN : 111N083V11",
                            "Retire Smart ;1H;UIN : 111L094V02"};
                }
            } else if (cust.income >= 2000000) {
                if (cust.strRiskAppetite.equals("Conservative")) {
                    return new String[]{
                            "Saral Retirement Saver ;1E;UIN : 111N088V03 / Annuity Plus ;22;UIN : 111N083V11",
                            "Retire Smart ;1H;UIN : 111L094V02"};
                } else if (cust.strRiskAppetite.equals("Moderate")) {
                    return new String[]{
                            "Saral Retirement Saver ;1E;UIN : 111N088V03 / Annuity Plus ;22;UIN : 111N083V11",
                            "Retire Smart ;1H;UIN : 111L094V02"};
                } else // Aggressive
                {
                    return new String[]{"Annuity Plus ;22;UIN : 111N083V11",
                            "Retire Smart ;1H;UIN : 111L094V02"};
                }
            }
        }

        return new String[]{"", ""};

    }

    //Insurnace with Savings Plan
    private String[] getMyWealthSavingNeedsProductList(Customer cust) {

        if (cust.age >= 18 && cust.age <= 25) { // Done with this condition
            if (cust.income < 300000) {
                if (cust.strRiskAppetite.equals("Aggressive")) {

                    if (cust.maritalStatus.equalsIgnoreCase("Single")) {
                        if (cust.sex.equals("F")) {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Women Advantage;2C;UIN : 111N106V01 / Smart Power Insurance ;1C;UIN : 111L090V02 / Saral Maha Anand# ;50;UIN : 111N097V01 / Smart InsureWealth Plus; 2J;UIN:111L125V02", ""};
                        } else {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Power Insurance ;1C;UIN : 111L090V02 / Saral Maha Anand# ;50;UIN : 111N097V01 / Smart InsureWealth Plus; 2J;UIN:111L125V02", ""};
                        }
                    } else {
                        if (cust.sex.equals("F")) {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Women Advantage;2C;UIN : 111N106V01 / Smart Humsafar; 1W;UIN : 111N103V03 / Smart Power Insurance ;1C;UIN : 111L090V02 / Saral Maha Anand# ;50;UIN : 111N097V01 / Smart InsureWealth Plus; 2J;UIN:111L125V02", ""};
                        } else {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Humsafar; 1W;UIN : 111N103V03 / Smart Power Insurance ;1C;UIN : 111L090V02 / Saral Maha Anand# ;50;UIN : 111N097V01 / Smart InsureWealth Plus; 2J;UIN:111L125V02", ""};
                        }
                    }
                } // Conservative and Moderate
                else {
                    if (cust.maritalStatus.equalsIgnoreCase("Single")) {
                        if (cust.sex.equals("F")) {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Women Advantage;2C;UIN : 111N106V01 / Smart InsureWealth Plus; 2J;UIN:111L125V02", ""};
                        } else {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart InsureWealth Plus; 2J;UIN:111L125V02", ""};
                        }
                    } else {
                        if (cust.sex.equals("F")) {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Women Advantage;2C;UIN : 111N106V01 / Smart Humsafar; 1W;UIN : 111N103V03 / Smart InsureWealth Plus; 2J;UIN:111L125V02", ""};
                        } else {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Humsafar; 1W;UIN : 111N103V03 / Smart InsureWealth Plus; 2J;UIN:111L125V02", ""};
                        }
                    }
                }
            } else if (cust.income >= 300000 && cust.income < 500000) {
                if (cust.strRiskAppetite.equals("Conservative")) {
                    if (cust.maritalStatus.equalsIgnoreCase("Single")) {
                        if (cust.sex.equals("F")) {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Guaranteed Savings Plan;1X;UIN : 111N097V01 /  Smart Income Protect ;1B;UIN : 111N085V04 / Smart Women Advantage;2C;UIN : 111N106V01 / Smart Bachat; 2D;UIN:111N108V03 / Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                        } else {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Guaranteed Savings Plan;1X;UIN : 111N097V01 /  Smart Income Protect ;1B;UIN : 111N085V04 / Smart Bachat; 2D;UIN:111N108V03 / Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                        }
                    } else {
                        if (cust.sex.equals("F")) {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Guaranteed Savings Plan;1X;UIN : 111N097V01 /  Smart Income Protect ;1B;UIN : 111N085V04 / Smart Humsafar; 1W;UIN : 111N103V03 / Smart Women Advantage;2C;UIN : 111N106V01 / Smart Bachat; 2D;UIN:111N108V03 / Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                        } else {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Guaranteed Savings Plan;1X;UIN : 111N097V01 /  Smart Income Protect ;1B;UIN : 111N085V04 / Smart Humsafar; 1W;UIN : 111N103V03 / Smart Bachat; 2D;UIN:111N108V03 / Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                        }
                    }

                } else if (cust.strRiskAppetite.equals("Moderate")) {
                    return new String[]{
                            "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Guaranteed Savings Plan;1X;UIN : 111N097V01 /  Smart Power Insurance# ;1C;UIN : 111L090V02 / Smart Wealth Builder#; 1K;UIN : 111L095V03 / Smart Bachat; 2D;UIN:111N108V03 / Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                } else // Aggressive
                {
                    return new String[]{
                            "Smart Wealth Builder ;1K;UIN : 111L095V03 / Smart Power Insurance ;1C;UIN : 111L090V02 / Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02", ""};
                }
            } else if (cust.income >= 500000 && cust.income < 1000000) {
                if (cust.strRiskAppetite.equals("Conservative")) {
                    if (cust.maritalStatus.equalsIgnoreCase("Single")) {
                        if (cust.sex.equals("F")) {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Guaranteed Savings Plan;1X;UIN : 111N097V01 /  Smart Income Protect ;1B;UIN : 111N085V04 / Smart Women Advantage;2C;UIN : 111N106V01 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Bachat; 2D;UIN:111N108V03 / Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                        } else {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Guaranteed Savings Plan;1X;UIN : 111N097V01 /  Smart Income Protect ;1B;UIN : 111N085V04 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Bachat; 2D;UIN:111N108V03 / Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                        }
                    } else {
                        if (cust.sex.equals("F")) {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Guaranteed Savings Plan;1X;UIN : 111N097V01 /  Smart Humsafar; 1W;UIN : 111N103V03 / Smart Women Advantage;2C;UIN : 111N106V01 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Bachat; 2D;UIN:111N108V03 / Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                        } else {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Guaranteed Savings Plan;1X;UIN : 111N097V01 /  Smart Humsafar; 1W;UIN : 111N103V03 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Bachat; 2D;UIN:111N108V03 / Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                        }
                    }

                } else if (cust.strRiskAppetite.equals("Moderate")) {
                    if (cust.maritalStatus.equalsIgnoreCase("Single")) {
                        if (cust.sex.equals("F")) {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Guaranteed Savings Plan;1X;UIN : 111N097V01 / Smart Income Protect ;1B;UIN : 111N085V04 / Smart Women Advantage;2C;UIN : 111N106V01 / Smart Wealth Builder# ;1K;UIN : 111L095V03 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Power Insurance# ;1C;UIN : 111L090V02 /  Smart Bachat; 2D;UIN:111N108V03 / Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                        } else {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Guaranteed Savings Plan;1X;UIN : 111N097V01 / Smart Income Protect ;1B;UIN : 111N085V04 / Smart Wealth Builder# ;1K;UIN : 111L095V03 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Power Insurance# ;1C;UIN : 111L090V02 /  Smart Bachat; 2D;UIN:111N108V03 / Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                        }
                    } else {
                        if (cust.sex.equals("F")) {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Guaranteed Savings Plan;1X;UIN : 111N097V01 / Smart Humsafar; 1W;UIN : 111N103V03 / Smart Women Advantage;2C;UIN : 111N106V01 / Smart Wealth Builder# ;1K;UIN : 111L095V03 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Power Insurance# ;1C;UIN : 111L090V02 /  Smart Bachat; 2D;UIN:111N108V03 / Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                        } else {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Guaranteed Savings Plan;1X;UIN : 111N097V01 / Smart Humsafar; 1W;UIN : 111N103V03 / Smart Wealth Builder# ;1K;UIN : 111L095V03 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Power Insurance# ;1C;UIN : 111L090V02 /  Smart Bachat; 2D;UIN:111N108V03 / Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                        }
                    }
                } else // Aggressive
                {
                    return new String[]{
                            "Smart Power Insurance ;1C;UIN : 111L090V02 / Smart Wealth Builder ;1K;UIN : 111L095V03 / Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02", ""};
                }
            } else if (cust.income >= 1000000 && cust.income < 2000000) {
                if (cust.strRiskAppetite.equals("Conservative")) {
                    if (cust.maritalStatus.equalsIgnoreCase("Single")) {
                        if (cust.sex.equals("F")) {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Income Protect ;1B;UIN : 111N085V04 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Bachat; 2D;UIN:111N108V03 / Smart Women Advantage;2C;UIN : 111N106V01 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                        } else {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Income Protect ;1B;UIN : 111N085V04 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Bachat; 2D;UIN:111N108V03 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                        }
                    } else {
                        if (cust.sex.equals("F")) {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Income Protect ;1B;UIN : 111N085V04 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Bachat; 2D;UIN:111N108V03 / Smart Humsafar; 1W;UIN : 111N103V03 / Smart Women Advantage;2C;UIN : 111N106V01 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                        } else {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Income Protect ;1B;UIN : 111N085V04 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Bachat; 2D;UIN:111N108V03 / Smart Humsafar; 1W;UIN : 111N103V03 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                        }
                    }
                } else if (cust.strRiskAppetite.equals("Moderate")) {
                    if (cust.maritalStatus.equalsIgnoreCase("Single")) {
                        if (cust.sex.equals("F")) {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Elite# ;53;UIN : 111L072V04 / Smart Income Protect ;1B;UIN : 111N085V04 / Smart Wealth Builder#; 1K;UIN : 111L095V03 / Smart Privilege#;2B;UIN : 111L107V03 / Smart Power Insurance# ;1C;UIN : 111L090V02 / Smart Bachat; 2D;UIN:111N108V03 / Smart Women Advantage;2C;UIN : 111N106V01 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                        } else {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Elite# ;53;UIN : 111L072V04 / Smart Income Protect ;1B;UIN : 111N085V04 / Smart Wealth Builder#; 1K;UIN : 111L095V03 / Smart Privilege#;2B;UIN : 111L107V03 / Smart Power Insurance# ;1C;UIN : 111L090V02 / Smart Bachat; 2D;UIN:111N108V03 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                        }
                    } else {
                        if (cust.sex.equals("F")) {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Elite# ;53;UIN : 111L072V04 / Smart Income Protect ;1B;UIN : 111N085V04 / Smart Wealth Builder#; 1K;UIN : 111L095V03 / Smart Privilege#;2B;UIN : 111L107V03 / Smart Power Insurance# ;1C;UIN : 111L090V02 / Smart Bachat; 2D;UIN:111N108V03 / Smart Women Advantage;2C;UIN : 111N106V01 / Smart Humsafar; 1W;UIN : 111N103V03 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                        } else {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Elite# ;53;UIN : 111L072V04 / Smart Income Protect ;1B;UIN : 111N085V04 / Smart Wealth Builder#; 1K;UIN : 111L095V03 / Smart Privilege#;2B;UIN : 111L107V03 / Smart Power Insurance# ;1C;UIN : 111L090V02 / Smart Bachat; 2D;UIN:111N108V03 / Smart Humsafar; 1W;UIN : 111N103V03 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                        }
                    }
                } else // Aggressive
                {
                    return new String[]{
                            "", "Smart Wealth Builder; 1K;UIN : 111L095V03 / Smart Power Insurance ;1C;UIN : 111L090V02 / Smart Elite ;53;UIN : 111L072V04 / Smart Privilege;2B;UIN : 111L107V03 / Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02"};
                }
            } else if (cust.income >= 2000000) {
                if (cust.strRiskAppetite.equals("Conservative")) {
                    if (cust.maritalStatus.equalsIgnoreCase("Single")) {
                        if (cust.sex.equals("F")) {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Bachat; 2D;UIN:111N108V03 / Smart Women Advantage;2C;UIN : 111N106V01 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                        } else {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Bachat; 2D;UIN:111N108V03 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                        }
                    } else {
                        if (cust.sex.equals("F")) {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Bachat; 2D;UIN:111N108V03 / Smart Women Advantage;2C;UIN : 111N106V01 / Smart Humsafar; 1W;UIN : 111N103V03 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                        } else {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Bachat; 2D;UIN:111N108V03 / Smart Humsafar; 1W;UIN : 111N103V03 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                        }
                    }
                } else if (cust.strRiskAppetite.equals("Moderate")) {
                    return new String[]{
                            "Shubh Nivesh;35;UIN : 111N055V04 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Wealth Builder#; 1K;UIN : 111L095V03 / Smart Elite# ;53;UIN : 111L072V04 / Smart Power Insurance# ;1C;UIN : 111L090V02 / Smart Privilege#;2B;UIN : 111L107V03 / Smart Bachat; 2D;UIN:111N108V03 / Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", "  "};
                } else // Aggressive
                {
                    return new String[]{
                            "", "Smart Power Insurance ;1C;UIN : 111L090V02 / Smart Privilege;2B;UIN : 111L107V03 / Smart Elite ;53;UIN : 111L072V04 / Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02"};
                }
            }

        } else if (cust.age >= 26 && cust.age <= 35) {

            if (cust.income < 300000) {
                if (cust.strRiskAppetite.equals("Conservative")) {
                    if (cust.maritalStatus.equalsIgnoreCase("Single")) {
                        if (cust.sex.equals("F")) {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Women Advantage;2C;UIN : 111N106V01 / Smart InsureWealth Plus; 2J;UIN:111L125V02", ""};
                        } else {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart InsureWealth Plus; 2J;UIN:111L125V02", ""};
                        }
                    } else {
                        if (cust.sex.equals("F")) {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Women Advantage;2C;UIN : 111N106V01 / Smart Humsafar; 1W;UIN : 111N103V03 / Smart InsureWealth Plus; 2J;UIN:111L125V02", ""};
                        } else {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Humsafar; 1W;UIN : 111N103V03 / Smart InsureWealth Plus; 2J;UIN:111L125V02", ""};
                        }
                    }
                } else if (cust.strRiskAppetite.equals("Moderate")) {
                    if (cust.maritalStatus.equalsIgnoreCase("Single")) {
                        if (cust.sex.equals("F")) {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Women Advantage;2C;UIN : 111N106V01 / Smart Power Insurance# ;1C;UIN : 111L090V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02", ""};
                        } else {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Power Insurance# ;1C;UIN : 111L090V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02", ""};
                        }
                    } else {
                        if (cust.sex.equals("F")) {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Women Advantage;2C;UIN : 111N106V01 / Smart Humsafar; 1W;UIN : 111N103V03", " Smart Power Insurance# ;1C;UIN : 111L090V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02"};
                        } else {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Humsafar; 1W;UIN : 111N103V03", " Smart Power Insurance# ;1C;UIN : 111L090V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02"};
                        }
                    }
                } else {
                    if (cust.maritalStatus.equalsIgnoreCase("Single")) {
                        if (cust.sex.equals("F")) {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Women Advantage;2C;UIN : 111N106V01 / Smart Power Insurance ;1C;UIN : 111L090V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02", "  "};
                        } else {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Power Insurance ;1C;UIN : 111L090V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02", "  "};
                        }
                    } else {
                        if (cust.sex.equals("F")) {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Humsafar; 1W;UIN : 111N103V03 / Smart Women Advantage;2C;UIN : 111N106V01", " Smart Power Insurance ;1C;UIN : 111L090V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02"};
                        } else {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Humsafar; 1W;UIN : 111N103V03", " Smart Power Insurance ;1C;UIN : 111L090V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02"};
                        }
                    }
                }
            } else if (cust.income >= 300000 && cust.income < 500000) {
                if (cust.strRiskAppetite.equals("Conservative")) {
                    if (cust.maritalStatus.equalsIgnoreCase("Single")) {
                        if (cust.sex.equals("F")) {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Guaranteed Savings Plan;1X;UIN : 111N097V01 /  Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Income Protect ;1B;UIN : 111N085V04 / Smart Bachat; 2D;UIN:111N108V03 / Smart Women Advantage;2C;UIN : 111N106V01 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                        } else {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Guaranteed Savings Plan;1X;UIN : 111N097V01 /  Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Income Protect ;1B;UIN : 111N085V04 / Smart Bachat; 2D;UIN:111N108V03 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                        }
                    } else {
                        if (cust.sex.equals("F")) {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Guaranteed Savings Plan;1X;UIN : 111N097V01 /  Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Income Protect ;1B;UIN : 111N085V04 / Smart Humsafar; 1W;UIN : 111N103V03 / Smart Women Advantage;2C;UIN : 111N106V01 / Smart Bachat; 2D;UIN:111N108V03 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                        } else {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Guaranteed Savings Plan;1X;UIN : 111N097V01 /  Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Income Protect ;1B;UIN : 111N085V04 / Smart Humsafar; 1W;UIN : 111N103V03 / Smart Bachat; 2D;UIN:111N108V03 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                        }
                    }
                } else if (cust.strRiskAppetite.equals("Moderate")) {
                    if (cust.sex.equals("F"))
                        return new String[]{
                                "Smart Guaranteed Savings Plan;1X;UIN : 111N097V01 / Shubh Nivesh;35;UIN : 111N055V04 / Smart Power Insurance# ;1C;UIN : 111L090V02 / Saral Maha Anand# ;50;UIN : 111N097V01 / Smart Women Advantage;2C;UIN : 111N106V01 / Smart Wealth Builder#; 1K;UIN : 111L095V03 / Smart Bachat; 2D;UIN:111N108V03 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                    else
                        return new String[]{
                                "Smart Guaranteed Savings Plan;1X;UIN : 111N097V01 / Shubh Nivesh;35;UIN : 111N055V04 / Smart Power Insurance# ;1C;UIN : 111L090V02 / Saral Maha Anand# ;50;UIN : 111N097V01 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Wealth Builder#; 1K;UIN : 111L095V03 / Smart Bachat; 2D;UIN:111N108V03 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};

                } else // Aggressive
                {
                    return new String[]{
                            "",
                            "Smart Wealth Builder ;1K;UIN : 111L095V03 / Smart Power Insurance ;1C;UIN : 111L090V02 / Saral Maha Anand ;50;UIN : 111N097V01 / Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02"};
                }
            } else if (cust.income >= 500000 && cust.income < 1000000) {
                if (cust.strRiskAppetite.equals("Conservative")) {
                    if (cust.maritalStatus.equalsIgnoreCase("Single")) {
                        if (cust.sex.equals("F")) {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Guaranteed Savings Plan;1X;UIN : 111N097V01 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Income Protect ;1B;UIN : 111N085V04 / Smart Bachat; 2D;UIN:111N108V03 / Smart Women Advantage;2C;UIN : 111N106V01 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                        } else {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Guaranteed Savings Plan;1X;UIN : 111N097V01 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Income Protect ;1B;UIN : 111N085V04 / Smart Bachat; 2D;UIN:111N108V03 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                        }
                    } else {
                        if (cust.sex.equals("F")) {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Guaranteed Savings Plan;1X;UIN : 111N097V01 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Income Protect ;1B;UIN : 111N085V04 / Smart Humsafar; 1W;UIN : 111N103V03 / Smart Women Advantage;2C;UIN : 111N106V01 / Smart Bachat; 2D;UIN:111N108V03 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                        } else {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Guaranteed Savings Plan;1X;UIN : 111N097V01 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Income Protect ;1B;UIN : 111N085V04 / Smart Humsafar; 1W;UIN : 111N103V03 / Smart Bachat; 2D;UIN:111N108V03 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                        }
                    }
                } else if (cust.strRiskAppetite.equals("Moderate")) {
                    if (cust.sex.equals("F"))
                        return new String[]{
                                "Smart Guaranteed Savings Plan;1X;UIN : 111N097V01 / Smart Income Protect ;1B;UIN : 111N085V04 / Shubh Nivesh;35;UIN : 111N055V04 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Women Advantage;2C;UIN : 111N106V01 / Saral Maha Anand# ;50;UIN : 111N097V01 / Smart Power Insurance# ;1C;UIN : 111L090V02 / Smart Bachat; 2D;UIN:111N108V03 / Smart Wealth Builder#; 1K;UIN : 111L095V03 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                    else
                        return new String[]{
                                "Smart Guaranteed Savings Plan;1X;UIN : 111N097V01 / Smart Income Protect ;1B;UIN : 111N085V04 / Shubh Nivesh;35;UIN : 111N055V04 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Saral Maha Anand# ;50;UIN : 111N097V01 / Smart Power Insurance# ;1C;UIN : 111L090V02 / Smart Bachat; 2D;UIN:111N108V03 / Smart Wealth Builder#; 1K;UIN : 111L095V03 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", "  "};

                } else // Aggressive
                {
                    return new String[]{
                            "",
                            "Smart Wealth Builder ;1K;UIN : 111L095V03 / Smart Power Insurance ;1C;UIN : 111L090V02 / Saral Maha Anand ;50;UIN : 111N097V01 / Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02"};
                }
            } else if (cust.income >= 1000000 && cust.income < 2000000) {
                if (cust.strRiskAppetite.equals("Conservative")) {
                    if (cust.maritalStatus.equalsIgnoreCase("Single")) {
                        if (cust.sex.equals("F")) {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Income Protect ;1B;UIN : 111N085V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Women Advantage;2C;UIN : 111N106V01 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Bachat; 2D;UIN:111N108V03 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                        } else {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Income Protect ;1B;UIN : 111N085V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Bachat; 2D;UIN:111N108V03 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                        }
                    } else {
                        if (cust.sex.equals("F")) {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Income Protect ;1B;UIN : 111N085V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Women Advantage;2C;UIN : 111N106V01 / Smart Humsafar; 1W;UIN : 111N103V03 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Bachat; 2D;UIN:111N108V03 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                        } else {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Income Protect ;1B;UIN : 111N085V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Humsafar; 1W;UIN : 111N103V03 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Bachat; 2D;UIN:111N108V03 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                        }
                    }
                } else if (cust.strRiskAppetite.equals("Moderate")) {
                    if (cust.maritalStatus.equalsIgnoreCase("Single")) {
                        if (cust.sex.equals("F")) {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Income Protect ;1B;UIN : 111N085V04 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Elite# ;53;UIN : 111L072V04 / Smart Power Insurance# ;1C;UIN : 111L090V02 / Smart Privilege#;2B;UIN : 111L107V03 / Smart Women Advantage;2C;UIN : 111N106V01 / Smart Bachat; 2D;UIN:111N108V03 / Smart Wealth Builder#; 1K;UIN : 111L095V03 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                        } else {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Income Protect ;1B;UIN : 111N085V04 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Elite# ;53;UIN : 111L072V04 / Smart Power Insurance# ;1C;UIN : 111L090V02 / Smart Privilege#;2B;UIN : 111L107V03 / Smart Bachat; 2D;UIN:111N108V03 / Smart Wealth Builder#; 1K;UIN : 111L095V03 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                        }
                    } else {
                        if (cust.sex.equals("F")) {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Income Protect ;1B;UIN : 111N085V04 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Elite# ;53;UIN : 111L072V04 / Smart Power Insurance# ;1C;UIN : 111L090V02 / Smart Privilege#;2B;UIN : 111L107V03 / Smart Bachat; 2D;UIN:111N108V03 / Smart Wealth Builder#; 1K;UIN : 111L095V03 / Smart Women Advantage;2C;UIN : 111N106V01 / Smart Humsafar; 1W;UIN : 111N103V03 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                        } else {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Income Protect ;1B;UIN : 111N085V04 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Elite# ;53;UIN : 111L072V04 / Smart Power Insurance# ;1C;UIN : 111L090V02 / Smart Privilege#;2B;UIN : 111L107V03 / Smart Humsafar; 1W;UIN : 111N103V03 / Smart Bachat; 2D;UIN:111N108V03 / Smart Wealth Builder#; 1K;UIN : 111L095V03 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};

                        }
                    }
                } else // Aggressive
                {
                    return new String[]{
                            "",
                            " Smart Wealth Builder ;1K;UIN : 111L095V03 / Smart Power Insurance ;1C;UIN : 111L090V02 / Smart Privilege;2B;UIN : 111L107V03 / Smart Elite ;53;UIN : 111L072V04 / Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02"};
                }
            } else if (cust.income >= 2000000) {
                if (cust.strRiskAppetite.equals("Conservative")) {
                    if (cust.maritalStatus.equalsIgnoreCase("Single")) {
                        if (cust.sex.equals("F")) {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Women Advantage;2C;UIN : 111N106V01 / Smart Bachat; 2D;UIN:111N108V03 / Flexi Smart Plus ;1M;UIN : 111N093V01 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04",
                                    ""};
                        } else {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Bachat; 2D;UIN:111N108V03 / Flexi Smart Plus ;1M;UIN : 111N093V01 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04",
                                    ""};
                        }
                    } else {
                        if (cust.sex.equals("F")) {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Women Advantage;2C;UIN : 111N106V01 / Smart Humsafar; 1W;UIN : 111N103V03 / Smart Bachat; 2D;UIN:111N108V03 / Flexi Smart Plus ;1M;UIN : 111N093V01 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04",
                                    ""};
                        } else {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Humsafar; 1W;UIN : 111N103V03 / Smart Bachat; 2D;UIN:111N108V03 / Flexi Smart Plus ;1M;UIN : 111N093V01 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04",
                                    ""};
                        }
                    }
                } else if (cust.strRiskAppetite.equals("Moderate")) {
                    return new String[]{
                            "Shubh Nivesh;35;UIN : 111N055V04 / Flexi Smart Plus ;1M;UIN : 111N093V01",
                            "Smart Elite# ;53;UIN : 111L072V04 / Smart Power Insurance# ;1C;UIN : 111L090V02 / Smart Bachat; 2D;UIN:111N108V03 / Smart Privilege#;2B;UIN : 111L107V03 / Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04"};
                } else // Aggressive
                {
                    return new String[]{
                            "",
                            "Smart Power Insurance ;1C;UIN : 111L090V02 / Smart Privilege;2B;UIN : 111L107V03 / Smart Elite ;53;UIN : 111L072V04 / Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02"};
                }
            }
        } else if (cust.age >= 36 && cust.age <= 45) {

            if (cust.income < 300000) {
                if (cust.strRiskAppetite.equals("Conservative")) {
                    if (cust.maritalStatus.equalsIgnoreCase("Single")) {
                        if (cust.sex.equals("F")) {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Women Advantage;2C;UIN : 111N106V01 / Smart InsureWealth Plus; 2J;UIN:111L125V02", ""};
                        } else {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart InsureWealth Plus; 2J;UIN:111L125V02", ""};
                        }
                    } else {
                        if (cust.sex.equals("F")) {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Women Advantage;2C;UIN : 111N106V01 / Smart Humsafar; 1W;UIN : 111N103V03 / Smart InsureWealth Plus; 2J;UIN:111L125V02", ""};
                        } else {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Humsafar; 1W;UIN : 111N103V03 / Smart InsureWealth Plus; 2J;UIN:111L125V02", ""};
                        }
                    }
                } else if (cust.strRiskAppetite.equals("Moderate")) {
                    if (cust.maritalStatus.equalsIgnoreCase("Single")) {
                        if (cust.sex.equals("F")) {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Women Advantage;2C;UIN : 111N106V01 / Saral Maha Anand ;50;UIN : 111N097V01 / Smart Power Insurance# ;1C;UIN : 111L090V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02", ""};
                        } else {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Saral Maha Anand ;50;UIN : 111N097V01 / Smart Power Insurance# ;1C;UIN : 111L090V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02", ""};
                        }
                    } else {
                        if (cust.sex.equals("F")) {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Women Advantage;2C;UIN : 111N106V01 / Smart Humsafar; 1W;UIN : 111N103V03", " Saral Maha Anand ;50;UIN : 111N097V01 / Smart Power Insurance# ;1C;UIN : 111L090V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02"};
                        } else {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Humsafar; 1W;UIN : 111N103V03", "Saral Maha Anand ;50;UIN : 111N097V01 / Smart Power Insurance# ;1C;UIN : 111L090V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02"};
                        }
                    }
                } else {
                    if (cust.maritalStatus.equalsIgnoreCase("Single")) {
                        if (cust.sex.equals("F")) {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Women Advantage;2C;UIN : 111N106V01 / Saral Maha Anand ;50;UIN : 111N097V01 / Smart Power Insurance ;1C;UIN : 111L090V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02", ""};
                        } else {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Saral Maha Anand ;50;UIN : 111N097V01 / Smart Power Insurance ;1C;UIN : 111L090V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02", ""};
                        }
                    } else {
                        if (cust.sex.equals("F")) {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Humsafar; 1W;UIN : 111N103V03 / Smart Women Advantage;2C;UIN : 111N106V01", " Saral Maha Anand ;50;UIN : 111N097V01 / Smart Power Insurance ;1C;UIN : 111L090V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02"};
                        } else {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Humsafar; 1W;UIN : 111N103V03", " Saral Maha Anand ;50;UIN : 111N097V01 / Smart Power Insurance ;1C;UIN : 111L090V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02"};
                        }
                    }
                }
            } else if (cust.income >= 300000 && cust.income < 500000) {
                if (cust.strRiskAppetite.equals("Conservative")) {
                    if (cust.maritalStatus.equalsIgnoreCase("Single")) {
                        if (cust.sex.equals("F")) {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Guaranteed Savings Plan;1X;UIN : 111N097V01 /  Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Income Protect ;1B;UIN : 111N085V04 / Smart Bachat; 2D;UIN:111N108V03 / Smart Women Advantage;2C;UIN : 111N106V01 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                        } else {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Guaranteed Savings Plan;1X;UIN : 111N097V01 /  Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Income Protect ;1B;UIN : 111N085V04 / Smart Bachat; 2D;UIN:111N108V03 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                        }
                    } else {
                        if (cust.sex.equals("F")) {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Guaranteed Savings Plan;1X;UIN : 111N097V01 /  Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Income Protect ;1B;UIN : 111N085V04 / Smart Humsafar; 1W;UIN : 111N103V03 / Smart Women Advantage;2C;UIN : 111N106V01 / Smart Bachat; 2D;UIN:111N108V03 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                        } else {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Guaranteed Savings Plan;1X;UIN : 111N097V01 /  Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Income Protect ;1B;UIN : 111N085V04 / Smart Humsafar; 1W;UIN : 111N103V03 / Smart Bachat; 2D;UIN:111N108V03 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                        }
                    }
                } else if (cust.strRiskAppetite.equals("Moderate")) {
                    if (cust.sex.equals("F"))
                        return new String[]{
                                "Smart Guaranteed Savings Plan;1X;UIN : 111N097V01 / Shubh Nivesh;35;UIN : 111N055V04 / Smart Power Insurance# ;1C;UIN : 111L090V02 / Saral Maha Anand# ;50;UIN : 111N097V01 / Smart Women Advantage;2C;UIN : 111N106V01 / Smart Wealth Builder#; 1K;UIN : 111L095V03 / Smart Bachat; 2D;UIN:111N108V03 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                    else
                        return new String[]{
                                "Smart Guaranteed Savings Plan;1X;UIN : 111N097V01 / Shubh Nivesh;35;UIN : 111N055V04 / Smart Power Insurance# ;1C;UIN : 111L090V02 / Saral Maha Anand# ;50;UIN : 111N097V01 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Wealth Builder#; 1K;UIN : 111L095V03 / Smart Bachat; 2D;UIN:111N108V03 / Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};

                } else // Aggressive
                {
                    return new String[]{
                            "",
                            "Smart Wealth Builder ;1K;UIN : 111L095V03 / Smart Power Insurance ;1C;UIN : 111L090V02 / Saral Maha Anand ;50;UIN : 111N097V01 / Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02"};
                }
            } else if (cust.income >= 500000 && cust.income < 1000000) {
                if (cust.strRiskAppetite.equals("Conservative")) {
                    if (cust.maritalStatus.equalsIgnoreCase("Single")) {
                        if (cust.sex.equals("F")) {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Guaranteed Savings Plan;1X;UIN : 111N097V01 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Income Protect ;1B;UIN : 111N085V04 / Smart Bachat; 2D;UIN:111N108V03 / Smart Women Advantage;2C;UIN : 111N106V01 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                        } else {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Guaranteed Savings Plan;1X;UIN : 111N097V01 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Income Protect ;1B;UIN : 111N085V04 / Smart Bachat; 2D;UIN:111N108V03 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                        }
                    } else {
                        if (cust.sex.equals("F")) {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Guaranteed Savings Plan;1X;UIN : 111N097V01 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Income Protect ;1B;UIN : 111N085V04 / Smart Humsafar; 1W;UIN : 111N103V03 / Smart Women Advantage;2C;UIN : 111N106V01 / Smart Bachat; 2D;UIN:111N108V03 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                        } else {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Guaranteed Savings Plan;1X;UIN : 111N097V01 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Income Protect ;1B;UIN : 111N085V04 / Smart Humsafar; 1W;UIN : 111N103V03 / Smart Bachat; 2D;UIN:111N108V03 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                        }
                    }
                } else if (cust.strRiskAppetite.equals("Moderate")) {
                    if (cust.sex.equals("F"))
                        return new String[]{
                                "Smart Guaranteed Savings Plan;1X;UIN : 111N097V01 / Smart Income Protect ;1B;UIN : 111N085V04 / Shubh Nivesh;35;UIN : 111N055V04 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Women Advantage;2C;UIN : 111N106V01 / Saral Maha Anand# ;50;UIN : 111N097V01 / Smart Power Insurance# ;1C;UIN : 111L090V02 / Smart Bachat; 2D;UIN:111N108V03 / Smart Wealth Builder#; 1K;UIN : 111L095V03 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                    else
                        return new String[]{
                                "Smart Guaranteed Savings Plan;1X;UIN : 111N097V01 / Smart Income Protect ;1B;UIN : 111N085V04 / Shubh Nivesh;35;UIN : 111N055V04 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Saral Maha Anand# ;50;UIN : 111N097V01 / Smart Power Insurance# ;1C;UIN : 111L090V02 / Smart Bachat; 2D;UIN:111N108V03 / Smart Wealth Builder#; 1K;UIN : 111L095V03 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", "  "};

                } else // Aggressive
                {
                    return new String[]{
                            "",
                            "Smart Wealth Builder ;1K;UIN : 111L095V03 / Smart Power Insurance ;1C;UIN : 111L090V02 / Saral Maha Anand ;50;UIN : 111N097V01 / Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02"};
                }
            } else if (cust.income >= 1000000 && cust.income < 2000000) {
                if (cust.strRiskAppetite.equals("Conservative")) {
                    if (cust.maritalStatus.equalsIgnoreCase("Single")) {
                        if (cust.sex.equals("F")) {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Income Protect ;1B;UIN : 111N085V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Women Advantage;2C;UIN : 111N106V01 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Bachat; 2D;UIN:111N108V03 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                        } else {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Income Protect ;1B;UIN : 111N085V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Bachat; 2D;UIN:111N108V03 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                        }
                    } else {
                        if (cust.sex.equals("F")) {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Income Protect ;1B;UIN : 111N085V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Women Advantage;2C;UIN : 111N106V01 / Smart Humsafar; 1W;UIN : 111N103V03 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Bachat; 2D;UIN:111N108V03 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                        } else {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Income Protect ;1B;UIN : 111N085V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Humsafar; 1W;UIN : 111N103V03 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Bachat; 2D;UIN:111N108V03 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                        }
                    }
                } else if (cust.strRiskAppetite.equals("Moderate")) {
                    if (cust.maritalStatus.equalsIgnoreCase("Single")) {
                        if (cust.sex.equals("F")) {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Income Protect ;1B;UIN : 111N085V04 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Elite# ;53;UIN : 111L072V04 / Smart Power Insurance# ;1C;UIN : 111L090V02 / Smart Privilege#;2B;UIN : 111L107V03 / Smart Women Advantage;2C;UIN : 111N106V01 / Smart Bachat; 2D;UIN:111N108V03 / Smart Wealth Builder#; 1K;UIN : 111L095V03 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                        } else {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Income Protect ;1B;UIN : 111N085V04 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Elite# ;53;UIN : 111L072V04 / Smart Power Insurance# ;1C;UIN : 111L090V02 / Smart Privilege#;2B;UIN : 111L107V03 / Smart Bachat; 2D;UIN:111N108V03 / Smart Wealth Builder#; 1K;UIN : 111L095V03 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                        }
                    } else {
                        if (cust.sex.equals("F")) {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Income Protect ;1B;UIN : 111N085V04 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Elite# ;53;UIN : 111L072V04 / Smart Power Insurance# ;1C;UIN : 111L090V02 / Smart Privilege#;2B;UIN : 111L107V03 / Smart Bachat; 2D;UIN:111N108V03 / Smart Wealth Builder#; 1K;UIN : 111L095V03 / Smart Women Advantage;2C;UIN : 111N106V01 / Smart Humsafar; 1W;UIN : 111N103V03 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                        } else {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Income Protect ;1B;UIN : 111N085V04 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Elite# ;53;UIN : 111L072V04 / Smart Power Insurance# ;1C;UIN : 111L090V02 / Smart Privilege#;2B;UIN : 111L107V03 / Smart Humsafar; 1W;UIN : 111N103V03 / Smart Bachat; 2D;UIN:111N108V03 / Smart Wealth Builder#; 1K;UIN : 111L095V03 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};

                        }
                    }
                } else // Aggressive
                {
                    return new String[]{
                            "",
                            " Smart Wealth Builder ;1K;UIN : 111L095V03 / Smart Power Insurance ;1C;UIN : 111L090V02 / Smart Privilege;2B;UIN : 111L107V03 / Smart Elite ;53;UIN : 111L072V04 / Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02"};
                }
            } else if (cust.income >= 2000000) {
                if (cust.strRiskAppetite.equals("Conservative")) {
                    if (cust.maritalStatus.equalsIgnoreCase("Single")) {
                        if (cust.sex.equals("F")) {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Women Advantage;2C;UIN : 111N106V01 / Smart Bachat; 2D;UIN:111N108V03 / Flexi Smart Plus ;1M;UIN : 111N093V01 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04",
                                    ""};
                        } else {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Bachat; 2D;UIN:111N108V03 / Flexi Smart Plus ;1M;UIN : 111N093V01 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04",
                                    ""};
                        }
                    } else {
                        if (cust.sex.equals("F")) {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Women Advantage;2C;UIN : 111N106V01 / Smart Humsafar; 1W;UIN : 111N103V03 / Smart Bachat; 2D;UIN:111N108V03 / Flexi Smart Plus ;1M;UIN : 111N093V01 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04",
                                    ""};
                        } else {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Humsafar; 1W;UIN : 111N103V03 / Smart Bachat; 2D;UIN:111N108V03 / Flexi Smart Plus ;1M;UIN : 111N093V01 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04",
                                    ""};
                        }
                    }
                } else if (cust.strRiskAppetite.equals("Moderate")) {
                    return new String[]{
                            "Shubh Nivesh;35;UIN : 111N055V04 / Flexi Smart Plus ;1M;UIN : 111N093V01",
                            "Smart Elite# ;53;UIN : 111L072V04 / Smart Power Insurance# ;1C;UIN : 111L090V02 / Smart Bachat; 2D;UIN:111N108V03 / Smart Privilege#;2B;UIN : 111L107V03 / Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04"};
                } else // Aggressive
                {
                    return new String[]{
                            "",
                            "Smart Power Insurance ;1C;UIN : 111L090V02 / Smart Privilege;2B;UIN : 111L107V03 / Smart Elite ;53;UIN : 111L072V04 / Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02"};
                }
            }
        } else if (cust.age >= 46 && cust.age <= 55) {
            if (cust.income < 300000) {
                if (cust.strRiskAppetite.equals("Conservative")) {
                    return new String[]{
                            "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart InsureWealth Plus; 2J;UIN:111L125V02",
                            ""};
                } else {
                    return new String[]{
                            "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Saral Maha Anand ;50;UIN : 111N097V01 / Smart InsureWealth Plus; 2J;UIN:111L125V02",
                            ""};
                }
            } else if (cust.income >= 300000 && cust.income < 500000) {
                if (cust.strRiskAppetite.equals("Conservative")) {
                    return new String[]{
                            "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Guaranteed Savings Plan;1X;UIN : 111N097V01 / Smart Income Protect ;1B;UIN : 111N085V04 / Smart Bachat; 2D;UIN:111N108V03 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04",
                            ""};

                } else if (cust.strRiskAppetite.equals("Moderate")) {
                    return new String[]{
                            "Shubh Nivesh;35;UIN : 111N055V04 / Smart Guaranteed Savings Plan;1X;UIN : 111N097V01 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Saral Maha Anand# ;50;UIN : 111N097V01 / Smart Wealth Builder#; 1K;UIN : 111L095V03 / Smart Bachat; 2D;UIN:111N108V03 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04",
                            ""};
                } else // Aggressive
                {
                    return new String[]{"", "Smart Wealth Builder ;1K;UIN : 111L095V03 / Saral Maha Anand ;50;UIN : 111N097V01 / Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02"};
                }
            } else if (cust.income >= 500000 && cust.income < 1000000) {
                if (cust.strRiskAppetite.equals("Conservative")) {
                    return new String[]{
                            "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Guaranteed Savings Plan;1X;UIN : 111N097V01 / Smart Income Protect ;1B;UIN : 111N085V04 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Bachat; 2D;UIN:111N108V03 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04",
                            ""};

                } else if (cust.strRiskAppetite.equals("Moderate")) {
                    return new String[]{
                            "Shubh Nivesh;35;UIN : 111N055V04 / Smart Guaranteed Savings Plan;1X;UIN : 111N097V01 / Smart Income Protect ;1B;UIN : 111N085V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Wealth Builder#; 1K;UIN : 111L095V03 / Saral Maha Anand# ;50;UIN : 111N097V01 / Smart Bachat; 2D;UIN:111N108V03 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04",
                            ""};
                } else // Aggressive
                {
                    return new String[]{"",
                            "Smart Wealth Builder ;1K;UIN : 111L095V03 / Saral Maha Anand ;50;UIN : 111N097V01 / Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02"};
                }
            } else if (cust.income >= 1000000 && cust.income < 2000000) {
                if (cust.strRiskAppetite.equals("Conservative")) {
                    if (cust.maritalStatus.equalsIgnoreCase("Single")) {
                        if (cust.sex.equals("F")) {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Income Protect ;1B;UIN : 111N085V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Women Advantage;2C;UIN : 111N106V01 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Bachat; 2D;UIN:111N108V03 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                        } else {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Income Protect ;1B;UIN : 111N085V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Bachat; 2D;UIN:111N108V03 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                        }
                    } else {
                        if (cust.sex.equals("F")) {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Income Protect ;1B;UIN : 111N085V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Women Advantage;2C;UIN : 111N106V01 / Smart Humsafar; 1W;UIN : 111N103V03 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Bachat; 2D;UIN:111N108V03 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04", ""};
                        } else {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Income Protect ;1B;UIN : 111N085V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Smart Humsafar; 1W;UIN : 111N103V03 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Bachat; 2D;UIN:111N108V03 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart Platina Assure; 2K;UIN:111N126V04 ", ""};
                        }
                    }
                } else if (cust.strRiskAppetite.equals("Moderate")) {
                    if (cust.maritalStatus.equalsIgnoreCase("Single")) {
                        if (cust.sex.equals("F")) {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Income Protect ;1B;UIN : 111N085V04 / Smart Elite# ;53;UIN : 111L072V04 / Smart Women Advantage;2C;UIN : 111N106V01 / Smart Wealth Builder#; 1K;UIN : 111L095V03 / Smart Privilege#;2B;UIN : 111L107V03 / Smart Bachat; 2D;UIN:111N108V03 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04",
                                    ""};
                        } else {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Income Protect ;1B;UIN : 111N085V04 / Smart Elite# ;53;UIN : 111L072V04 / Smart Wealth Builder#; 1K;UIN : 111L095V03 / Smart Privilege#;2B;UIN : 111L107V03 / Smart Bachat; 2D;UIN:111N108V03 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04",
                                    ""};
                        }
                    } else {
                        if (cust.sex.equals("F")) {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Income Protect ;1B;UIN : 111N085V04 / Smart Elite# ;53;UIN : 111L072V04 / Smart Women Advantage;2C;UIN : 111N106V01 / Smart Humsafar; 1W;UIN : 111N103V03 / Smart Wealth Builder#; 1K;UIN : 111L095V03 / Smart Privilege#;2B;UIN : 111L107V03 / Smart Bachat; 2D;UIN:111N108V03 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04",
                                    ""};
                        } else {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Income Protect ;1B;UIN : 111N085V04 / Smart Elite# ;53;UIN : 111L072V04 / Smart Humsafar; 1W;UIN : 111N103V03 / Smart Wealth Builder#; 1K;UIN : 111L095V03 / Smart Privilege#;2B;UIN : 111L107V03 / Smart Bachat; 2D;UIN:111N108V03 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04",
                                    ""};
                        }
                    }
                } else {//Aggressive{
                    return new String[]{"",
                            "Smart Wealth Builder ;1K;UIN : 111L095V03 / Smart Elite ;53;UIN : 111L072V04 / Smart Privilege;2B;UIN : 111L107V03 / Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02"};
                }
            } else if (cust.income >= 2000000) {
                if (cust.strRiskAppetite.equals("Conservative")) {
                    if (cust.maritalStatus.equalsIgnoreCase("Single")) {
                        if (cust.sex.equals("F")) {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Women Advantage;2C;UIN : 111N106V01 / Smart Bachat; 2D;UIN:111N108V03 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04",
                                    ""};
                        } else {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Bachat; 2D;UIN:111N108V03 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04",
                                    ""};
                        }
                    } else {
                        if (cust.sex.equals("F")) {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Women Advantage;2C;UIN : 111N106V01 / Smart Humsafar; 1W;UIN : 111N103V03 / Smart Bachat; 2D;UIN:111N108V03 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04",
                                    ""};
                        } else {
                            return new String[]{
                                    "Shubh Nivesh;35;UIN : 111N055V04 / Smart Money Back Gold ;1N;UIN : 111N096V03 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Humsafar; 1W;UIN : 111N103V03 / Smart Bachat; 2D;UIN:111N108V03 /  Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04",
                                    ""};
                        }
                    }
                } else if (cust.strRiskAppetite.equals("Moderate")) {
                    return new String[]{
                            "Shubh Nivesh;35;UIN : 111N055V04 / Flexi Smart Plus ;1M;UIN : 111N093V01 / Smart Privilege#;2B;UIN : 111L107V03 / Smart Elite# ;53;UIN : 111L072V04 / Smart Bachat; 2D;UIN:111N108V03 / Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02 / Smart Platina Assure; 2K;UIN:111N126V04",
                            ""};
                } else // Aggressive
                {
                    return new String[]{"",
                            "Smart Privilege;2B;UIN : 111L107V03 / Smart Elite ;53;UIN : 111L072V04 / Saral InsureWealth Plus; 2H;UIN:111L124V02 / Smart InsureWealth Plus; 2J;UIN:111L125V02"};
                }
            }
        } else if (cust.age >= 56) {
            if (cust.income < 300000) {
                return new String[]{"", ""};
            } else if (cust.income >= 300000 && cust.income < 500000) {
                if (cust.strRiskAppetite.equals("Conservative")) {
                    return new String[]{"", ""};
                } else if (cust.strRiskAppetite.equals("Moderate")) {
                    return new String[]{"",
                            "Smart Wealth Builder*# ;1K;UIN : 111L095V03"};
                } else // Aggressive
                {
                    return new String[]{"",
                            "Smart Wealth Builder* ;1K;UIN : 111L095V03"};
                }
            } else if (cust.income >= 500000 && cust.income < 1000000) {
                if (cust.strRiskAppetite.equals("Conservative")) {
                    return new String[]{
                            "Flexi Smart Plus ;1M;UIN : 111N093V01", ""};
                } else if (cust.strRiskAppetite.equals("Moderate")) {
                    return new String[]{
                            "Flexi Smart Plus ;1M;UIN : 111N093V01",
                            "Smart Wealth Builder*# ;1K;UIN : 111L095V03"};
                } else // Aggressive
                {
                    return new String[]{"",
                            "Smart Wealth Builder* ;1K;UIN : 111L095V03"};
                }
            } else if (cust.income >= 1000000 && cust.income < 2000000) {
                if (cust.strRiskAppetite.equals("Conservative")) {
                    return new String[]{
                            "Flexi Smart Plus ;1M;UIN : 111N093V01", ""};
                } else if (cust.strRiskAppetite.equals("Moderate")) {
                    return new String[]{
                            "Flexi Smart Plus ;1M;UIN : 111N093V01",
                            "Smart Wealth Builder*# ;1K;UIN : 111L095V03 / Smart Elite# ;53;UIN : 111L072V04"};
                } else // Aggressive
                {
                    return new String[]{"",
                            "Smart Wealth Builder* ;1K;UIN : 111L095V03 / Smart Elite ;53;UIN : 111L072V04"};
                }
            } else if (cust.income >= 2000000) {
                if (cust.strRiskAppetite.equals("Conservative")) {
                    return new String[]{
                            "Flexi Smart Plus ;1M;UIN : 111N093V01", ""};
                } else if (cust.strRiskAppetite.equals("Moderate")) {
                    return new String[]{
                            "Flexi Smart Plus ;1M;UIN : 111N093V01",
                            "Smart Elite# ;53;UIN : 111L072V04"};
                } else // Aggressive
                {
                    return new String[]{"",
                            "Smart Elite ;53;UIN : 111L072V04"};
                }
            }
        }

        return new String[]{"", ""};

    }

    private double getRiskAppetite_Monthly(double rate) {
        return (Math.pow(1 + rate, 1.0 / 12.0) - 1) * 12;
    }
}
