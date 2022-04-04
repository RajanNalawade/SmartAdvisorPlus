package sbilife.com.pointofsale_bancaagency.smartincomeprotect;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import sbilife.com.pointofsale_bancaagency.common.CommonForAllProd;

class SmartIncomeProtectBusinessLogic {
    private SmartIncomeProtectBean smartIncomeProtectBean;
    private CommonForAllProd commonForAllProd;
    private SmartIncomeProtectDB ac;
    private SmartIncomeProtectProperties prop;

    // rider ADB , ATPDB and CC13Nonlinked DB


    // returns ATPDB premium


    // returns cc13Nonlinked rider premium

    public double getBonusRate(String returnOnInvestment) {
        if (returnOnInvestment.equals("4%")) {
            if (smartIncomeProtectBean.getPremFreq().equals("Single")) {
                return 0.0;
            } else {
                return 0.0175;
            }
        } else {
            return 0.030;
        }
    }

    //	public double getPremiumInstWithSTFirstYear(double premiumInst) {
//		double temp = 0;
//		if (smartIncomeProtectBean.getJKResident()) {
//			temp = ((prop.JKserviceTax + 1) * premiumInst);
//		} else {
//			temp = ((prop.FY_serviceTax + 1) * premiumInst);
//		}
//		return temp;
//	}
//
//	public double getPremiumInstWithSTSecondYear(double premiumInst) {
//		double temp = 0;
//		if (smartIncomeProtectBean.getJKResident()) {
//			temp = ((prop.JKserviceTax + 1) * premiumInst);
//		} else {
//			temp = ((prop.SY_serviceTax + 1) * premiumInst);
//		}
//		return temp;
//	}

    /* BI made by Priyanka Warekar - 2-1-2015 */


    // Returns basic premium not rounded and without Applicable Tax

    // basic premium DB


    // returns PT premium


    // returns ATPDB premium


    // returns cc13Nonlinked rider premium


    // Returns basic premium not rounded and without Applicable Tax


    // returns ADB premium


    /********************************* Added by Akshaya on 04-AUG-2015 end *****************/

    public double getMinesOccuInterest(double SumAssured_Basic) {
        return (SumAssured_Basic / 1000) * 2;
    }

    public double getBackDateInterest(double grossPremium, String bkDate) {

        try {

            // double indigoRate=7.5;
            double ST;
            if (smartIncomeProtectBean.getJKResident()) {
                ST = prop.serviceTaxJkResident;
            } else {
                ST = prop.serviceTax;
            }
            //			double indigoRate = 10;

            double indigoRate = 6.50;

            double ServiceTaxValue = (ST + 1) * 100;

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

            // double netPremWithoutST=Math.round((grossPremium*100)/103.09);
            double netPremWithoutST = Math.round((grossPremium * 100)
                    / ServiceTaxValue);
            // System.out.println("netPremWithoutST "+netPremWithoutST);
            int days = commonForAllProd.setDate(date, strBackDate);
            // System.out.println("no of days "+days);
            double monthsBetween = commonForAllProd.roundDown(
                    (double) days / 365 * 12, 0);
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
    }

    // returns ATPDB premium


    // returns cc13Nonlinked rider premium


    // returns PT premium


    // returns ADB premium

    /********************************* Added by Akshaya on 31-AUG-2015 end *****************/

    // Store user data in bean
    public SmartIncomeProtectBusinessLogic(SmartIncomeProtectBean smartIncomeProtectBean) {
        this.smartIncomeProtectBean = smartIncomeProtectBean;
        commonForAllProd = new CommonForAllProd();
        prop = new SmartIncomeProtectProperties();
        ac = new SmartIncomeProtectDB();
    }

    public SmartIncomeProtectBean getSmartIncomeProtectBean() {
        return smartIncomeProtectBean;
    }

    // Returns basic premium not rounded and without Applicable Tax
    public double get_Premium_Basic_WithoutST_NotRounded() {
        int divFact = 0;
        if (smartIncomeProtectBean.getPremFreq().equals("Yearly")) {
            divFact = 1;
        } else if (smartIncomeProtectBean.getPremFreq()
                .equals("Half Yearly")) {
            divFact = 2;
        } else if (smartIncomeProtectBean.getPremFreq().equals("Quarterly")) {
            divFact = 4;
        } else {
            divFact = 12;
        }

        // System.out.println("DB" + getPR_Basic_FromDB());
        // System.out.println("staff rebate" + getStaffRebate("Basic"));
        // System.out.println("SA rebate" + getSA_Rebate_Basic());
        // System.out.println("loading "+ getLoadingForFreqOfPremiums("Basic"));
        // System.out.println("Frequency**" +
        // smartIncomeProtectBean.getPremFreq()+"**" + " Age : "+
        // smartIncomeProtectBean.getAge()+" "+smartIncomeProtectBean.getBasicTerm());
        // return
        // (getPR_Basic_FromDB()*(1-getStaffRebate("Basic"))-getSA_Rebate_Basic()+(getNSAP()
        // +
        // getEMR()))*smartIncomeProtectBean.getBasicSA()/1000*getLoadingForFreqOfPremiums("Basic")/divFact;
        return (getPR_Basic_FromDB() * (1 - getStaffRebate("Basic"))
                - getSA_Rebate_Basic() + (getNSAP() + getEMR()))
                * smartIncomeProtectBean.getBasicSA()
                / 1000
                * getLoadingForFreqOfPremiums("Basic") / 1;
    }

    //added by sujata on 19-02-2020 for frequency loading
    public double get_premium_withoutFreqLoading() {
        int divFact = 0;
        if (smartIncomeProtectBean.getPremFreq().equals("Yearly")) {
            divFact = 1;
        } else if (smartIncomeProtectBean.getPremFreq()
                .equals("Half Yearly")) {
            divFact = 2;
        } else if (smartIncomeProtectBean.getPremFreq().equals("Quarterly")) {
            divFact = 4;
        } else {
            divFact = 12;
        }

        return (getPR_Basic_FromDB() * (1 - getStaffRebate("Basic"))
                - getSA_Rebate_Basic() + (getNSAP() + getEMR()))
                * smartIncomeProtectBean.getBasicSA()
                / 1000;

        //return (premiumBasic_Rounded * divFact);

    }
    //end


    // basic premium DB
    private double getPR_Basic_FromDB() {
        double prDouble = 0;
        String pr, prStr;
        int arrCount = 0;
        String[] prStrArr;

        prStr = ac.getBasic_PR_Arr();
        prStrArr = commonForAllProd.split(prStr, ",");
        int ptArr[] = {7, 12, 15};

        for (int i = 8; i <= 58; i++) {
            for (int j = 0; j <= 2; j++) {
                if ((i == smartIncomeProtectBean.getAge() && smartIncomeProtectBean
                        .getBasicTerm() == ptArr[j])) {
                    // //System.out.println(i +" "+ j);
                    pr = prStrArr[arrCount];

                    prDouble = Double.parseDouble(pr);
                    break;
                }
                arrCount++;
                // //System.out.println(i +" "+ j +" = "+prStrArr[arrCount]);
            }

        }
        return prDouble;

    }

    public double getStaffRebate(String rider) {
        if (rider.equals("CCNonLinked")) {
            if (smartIncomeProtectBean.getStaffDisc()) {
                return 0.015;
            } else {
                return 0.0;
            }
        } else {
            if (smartIncomeProtectBean.getStaffDisc()) {
                return 0.05;
            } else {
                return 0.0;
            }
        }

    }

    private double getSA_Rebate_Basic() {
        if ((smartIncomeProtectBean.getBasicSA() >= 100000)
                && (smartIncomeProtectBean.getBasicSA() < 200000)) {
            return 0.0;
        } else if ((smartIncomeProtectBean.getBasicSA() >= 200000)
                && (smartIncomeProtectBean.getBasicSA() < 500000)) {
            return 2;
        } else if (smartIncomeProtectBean.getBasicSA() >= 500000) {
            return 3;
        }

        return 0.0;
    }

    private double getLoadingForFreqOfPremiums(String cover) {
        if (cover.equals("Basic")) {
            // //System.out.println("Inside basic");
            if (smartIncomeProtectBean.getPremFreq().equals("Yearly")) {
                return 1;
            }
            if (smartIncomeProtectBean.getPremFreq().equals("Half Yearly")) {
                return prop.HalfYearly_Modal_Factor;
            } else if (smartIncomeProtectBean.getPremFreq().equals(
                    "Quarterly")) {
                return prop.Quarterly_Modal_Factor;
            } else if (smartIncomeProtectBean.getPremFreq()
                    .equals("Monthly")) {
                return prop.Monthly_Modal_Factor;
            }

        } else if (cover.equals("Rider")) {
            // //System.out.println("Inside rider");
            if (smartIncomeProtectBean.getPremFreq().equals("Yearly")) {
                return 1;
            } else if (smartIncomeProtectBean.getPremFreq().equals(
                    "Half Yearly")) {
                return 1.04;
            } else if (smartIncomeProtectBean.getPremFreq().equals(
                    "Quarterly")) {
                return 1.06;
            } else if (smartIncomeProtectBean.getPremFreq()
                    .equals("Monthly")) {
                return 1.068;
            }

        }

        return 0.0;
    }

    private int getNSAP() {
        return 0;
    }

    private int getEMR() {
        return 0;
    }

    // returns PT premium
    public double get_Premium_PT_WithoutST_NotRounded() {

        int divFactor;
        if (smartIncomeProtectBean.getPremFreq().equals("Yearly")) {
            divFactor = 1;
        } else if (smartIncomeProtectBean.getPremFreq()
                .equals("Half Yearly")) {
            divFactor = 2;
        } else if (smartIncomeProtectBean.getPremFreq().equals("Quarterly")) {
            divFactor = 4;
        } else {
            divFactor = 12;
        }
        double pr_rate = Double.parseDouble("" + getPR_PTA_FromDB());
        // System.out.println("pr_rate : "+pr_rate);
        // System.out.println("getStaffRebate "+getStaffRebate("Basic"));
        // System.out.println("getSA_Rebate_Rider() "+getSA_Rebate_Rider());
        // System.out.println("getNSAP() "+getNSAP());
        // System.out.println("getEMR() "+getEMR());
        // System.out.println("smartIncomeProtectBean.getSumAssured_PT() "+smartIncomeProtectBean.getSumAssured_PT());
        // System.out.println("getLoadingForFreqOfPremiums "+getLoadingForFreqOfPremiums("Rider"));
        // System.out.println("PT rate" + getPR_PTA_FromDB() +
        // " SA :  "+getStaffRebate("Basic")
        // +" sum Assured PT : "+smartIncomeProtectBean.getSumAssured_PT()+" Loadng Frq:  "+getLoadingForFreqOfPremiums("Rider")
        // +" SA Rebate: "+getSA_Rebate_Rider()+"sum : "+(getNSAP() +
        // getEMR())+" "+smartIncomeProtectBean.getSumAssured_PT()/1000+" "+(getLoadingForFreqOfPremiums("Rider")/divFactor)+"  "+(smartIncomeProtectBean.getSumAssured_PT()/1000));
        // System.out.println(getPR_PTA_FromDB()*(1-getStaffRebate("Basic")));
        // System.out.println(getPR_PTA_FromDB()*(1-getStaffRebate("Basic"))-getSA_Rebate_Rider());
        // String temp=""+(2.28*100);
        // double
        // temp4=(((pr_rate*(1-getStaffRebate("Basic"))-getSA_Rebate_Rider()+(getNSAP()
        // +
        // getEMR()))*smartIncomeProtectBean.getSumAssured_PT()/1000*getLoadingForFreqOfPremiums("Rider")/divFactor)/100);
        // System.out.println();
        //
        // double temp3=(((2.28*100)*5000000.0)/100);
        // double temp2=Double.parseDouble(""+2.28*5000000);
        // String
        // temp1=""+((getPR_PTA_FromDB()*(1-getStaffRebate("Basic"))-getSA_Rebate_Rider()+(getNSAP()
        // + getEMR()))*smartIncomeProtectBean.getSumAssured_PT());
        // System.out.println(" temp : "+temp+" temp1 : "+temp1 + "   "+temp2 +
        // "  "+temp3+" "+"  "+temp4);
        // System.out.println((getPR_PTA_FromDB()*(1-getStaffRebate("Basic"))-getSA_Rebate_Rider()+(getNSAP()
        // + getEMR())));
        // System.out.println(""+(getPR_PTA_FromDB()*(1-getStaffRebate("Basic"))-getSA_Rebate_Rider()+(getNSAP()
        // + getEMR()))*smartIncomeProtectBean.getSumAssured_PT());
        // System.out.println((getPR_PTA_FromDB()*(1-getStaffRebate("Basic"))-getSA_Rebate_Rider())*(smartIncomeProtectBean.getSumAssured_PT()/1000));
        // System.out.println((getPR_PTA_FromDB()*(1-getStaffRebate("Basic"))-getSA_Rebate_Rider()+(getNSAP()
        // +
        // getEMR()))*smartIncomeProtectBean.getSumAssured_PT()/1000*getLoadingForFreqOfPremiums("Rider"));
        // System.out.println(getPR_PTA_FromDB()*(1-getStaffRebate("Basic")));
        // System.out.println(getPR_PTA_FromDB()*(1-getStaffRebate("Basic")));
        return ((getPR_PTA_FromDB() * (1 - getStaffRebate("Basic"))
                - getSA_Rebate_Rider() + (getNSAP() + getEMR()))
                * smartIncomeProtectBean.getPreferredSA()
                / 1000
                * getLoadingForFreqOfPremiums("Rider") / divFactor);

    }

    // PT rider DB
    private double getPR_PTA_FromDB() {

        double prDouble;
        String pr = null, prStr;
        int arrCount = 0;
        String[] prStrArr;
        SmartIncomeProtectDB ac = new SmartIncomeProtectDB();
        prStr = ac.getPTA_PremiumRate_Arr();
        prStrArr = commonForAllProd.split(prStr, ",");

        for (
                int i = 18;
                i < 61; i++) {
            for (int j = 5; j < 31; j++) {
                if (i == smartIncomeProtectBean.getAge()
                        && j == smartIncomeProtectBean.getPreferredTerm()) {
                    pr = prStrArr[arrCount];
                    prDouble = Double.parseDouble(pr);
                    // System.out.println(" prDouble : "+ prDouble);
                    break;
                }
                arrCount++;
            }
        }

        prDouble = Double.parseDouble(pr) / 100;
        return prDouble;
    }

    private int getSA_Rebate_Rider() {
        return 0;
    }

    // returns ADB premium
    public double get_Premium_ADB_WithoutST_NotRounded() {
        int divFactor;
        if (smartIncomeProtectBean.getPremFreq().equals("Yearly")) {
            divFactor = 1;
        } else if (smartIncomeProtectBean.getPremFreq()
                .equals("Half Yearly")) {
            divFactor = 2;
        } else if (smartIncomeProtectBean.getPremFreq().equals("Quarterly")) {
            divFactor = 4;
        } else {
            divFactor = 12;
        }
        // System.out.println("ADB rate" + getPR_Rider_FromDB("ADB") +
        // " SA :  "+getStaffRebate("Basic") +
        // " Loadng Frq:  "+getLoadingForFreqOfPremiums("Rider"));

        return (getPR_Rider_FromDB("ADB") * (1 - getStaffRebate("Basic"))
                - getSA_Rebate_Rider() + (getNSAP() + getEMR()))
                * smartIncomeProtectBean.getADB_SA()
                / 1000
                * getLoadingForFreqOfPremiums("Rider") / divFactor;
    }

    // rider ADB , ATPDB and CC13Nonlinked DB
    private double getPR_Rider_FromDB(String whichRider) {
        if (whichRider.equals("ADB")) {
            if (smartIncomeProtectBean.getPremFreq().equals("Yearly")
                    || smartIncomeProtectBean.getPremFreq().equals(
                    "Half Yearly")
                    || smartIncomeProtectBean.getPremFreq().equals(
                    "Quarterly")
                    || smartIncomeProtectBean.getPremFreq()
                    .equals("Monthly")) {
                return 0.5;
            } else {
                String pr = null, prStr;
                String[] prStrArr;

                SmartIncomeProtectDB ac = new SmartIncomeProtectDB();
                prStr = ac.getADB_PremiumRate_Arr();
                prStrArr = commonForAllProd.split(prStr, ",");
                for (int i = 5; i < 31; i++) {
                    if (i == smartIncomeProtectBean.getADB_Term()) {
                        pr = prStrArr[i - 5];
                        break;
                    }
                }

                double rate = Double.parseDouble(pr) / 100;
                // //System.out.println("ADB rate" + rate);

                return rate;

            }
        } else if (whichRider.equals("ATPDB")) {
            if (smartIncomeProtectBean.getPremFreq().equals("Yearly")
                    || smartIncomeProtectBean.getPremFreq().equals(
                    "Half Yearly")
                    || smartIncomeProtectBean.getPremFreq().equals(
                    "Quarterly")
                    || smartIncomeProtectBean.getPremFreq()
                    .equals("Monthly")) {
                return 0.4;
            } else {
                String pr = null, prStr;
                String[] prStrArr;

                SmartIncomeProtectDB ac = new SmartIncomeProtectDB();
                prStr = ac.getATPDB_PremiumRate_Arr();
                prStrArr = commonForAllProd.split(prStr, ",");
                for (int i = 5; i < 31; i++) {
                    if (i == smartIncomeProtectBean.getATPDB_Term()) {
                        pr = prStrArr[i - 5];
                        break;
                    }
                }

                double rate = Double.parseDouble(pr) / 100;
                // //System.out.println("ADB rate" + rate);

                return rate;

            }
        } else if (whichRider.equals("CC13NonLinked")) {
            String pr = null, prStr;
            String[] prStrArr;

            SmartIncomeProtectDB ac = new SmartIncomeProtectDB();
            prStr = ac.getCC13NonLinked_PremiumRate_Arr();
            prStrArr = commonForAllProd.split(prStr, ",");
            for (int i = 18; i < 65; i++) {
                if (i == smartIncomeProtectBean.getAge()) {
                    pr = prStrArr[i - 18];
                    break;
                }
            }

            // //System.out.println("cc rate" + pr);

            return Double.parseDouble(pr);
        }

        return 0.0;
    }

    // returns ATPDB premium
    public double get_Premium_ATPDB_WithoutST_NotRounded() {
        int divFactor;
        if (smartIncomeProtectBean.getPremFreq().equals("Yearly")) {
            divFactor = 1;
        } else if (smartIncomeProtectBean.getPremFreq()
                .equals("Half Yearly")) {
            divFactor = 2;
        } else if (smartIncomeProtectBean.getPremFreq().equals("Quarterly")) {
            divFactor = 4;
        } else {
            divFactor = 12;
        }
        // System.out.println("ATPD rate" + getPR_Rider_FromDB("ATPDB") +
        // " SA :  "+getStaffRebate("Basic") +
        // " Loadng Frq:  "+getLoadingForFreqOfPremiums("Rider"));

        return (getPR_Rider_FromDB("ATPDB") * (1 - getStaffRebate("Basic"))
                - getSA_Rebate_Rider() + (getNSAP() + getEMR()))
                * smartIncomeProtectBean.getATPDB_SA()
                / 1000
                * getLoadingForFreqOfPremiums("Rider") / divFactor;
    }

    // returns cc13Nonlinked rider premium
    public double get_Premium_CC13NonLinked_WithoutST_NotRounded() {
        int divFactor;
        if (smartIncomeProtectBean.getPremFreq().equals("Yearly")) {
            divFactor = 1;
        } else if (smartIncomeProtectBean.getPremFreq()
                .equals("Half Yearly")) {
            divFactor = 2;
        } else if (smartIncomeProtectBean.getPremFreq().equals("Quarterly")) {
            divFactor = 4;
        } else {
            divFactor = 12;
        }
        // System.out.println("CC13NonLinked rate"
        // +getPR_Rider_FromDB("CC13NonLinked") +
        // " SA :  "+getStaffRebate("CCNonLinked") +
        // " Loadng Frq:  "+getLoadingForFreqOfPremiums("Rider"));

        // //System.out.println("cc staff rebate " +
        // getStaffRebate("CCNonLinked"));

        return (getPR_Rider_FromDB("CC13NonLinked")
                * (1 - getStaffRebate("CCNonLinked")) - getSA_Rebate_Rider() + (getNSAP() + getEMR()))
                * smartIncomeProtectBean.getCc13nlSA()
                / 1000
                * getLoadingForFreqOfPremiums("Rider") / divFactor;
    }

    private double getBonusRate(String returnOnInvestment, int policyterm,
                                int year_F) {

        double bonusrate;
        if (policyterm == 7 && year_F <= policyterm) {
            if (returnOnInvestment.equals("4%")) {

                bonusrate = 0.005;
            } else {
                bonusrate = 0.05;
            }

        } else if (policyterm == 12 && year_F <= policyterm) {
            if (returnOnInvestment.equals("4%")) {

                bonusrate = 0.009;
            } else {
                bonusrate = 0.045;
            }

        } else if (policyterm == 15 && year_F <= policyterm) {
            if (returnOnInvestment.equals("4%")) {

                bonusrate = 0.01;
            } else {
                bonusrate = 0.044;
            }

        } else {
            bonusrate = 0;
        }
//		System.out.println(policyterm+" "+bonusrate+"  "+returnOnInvestment);
        return bonusrate;
    }

    public double getTerminalBonus() {
        return (1 + 0.25);
    }

    /**
     * As per the changes from 1st Apr 2015, by Vrushali Chaudhari
     */
    public double getPremiumInstWithST(double premiumInst) {
        double temp;
        if (smartIncomeProtectBean.getJKResident()) {
            temp = ((prop.serviceTaxJkResident + 1) * premiumInst);
        } else {
            temp = ((prop.serviceTax + 1) * premiumInst);
        }
        return temp;
    }

    /* BI made by Priyanka Warekar - 2-1-2015 */

    public double getYearlyTotBasePremPaid(double premiumBasic_NotRounded) {
        int divFact;
        if (smartIncomeProtectBean.getPremFreq().equals("Yearly")) {
            divFact = 1;
        } else if (smartIncomeProtectBean.getPremFreq()
                .equals("Half Yearly")) {
            divFact = 2;
        } else if (smartIncomeProtectBean.getPremFreq().equals("Quarterly")) {
            divFact = 4;
        } else {
            divFact = 12;
        }
        return premiumBasic_NotRounded * divFact;
    }

    public double get_Total_Base_Premium_Paid(int year_F, double premBasic) {
        if (year_F != 1
                && smartIncomeProtectBean.getPremFreq().equalsIgnoreCase(
                "Single")) {
            return 0;
        } else {
            if (year_F <= smartIncomeProtectBean.getBasicTerm()) {
                // System.out.println(""+premBasic*year_F);
                return (premBasic * year_F);
            } else
                return 0;

        }
    }

    public double getGuarntedDeathBenefit(int year_F, int policyTerm, int age, double sumAssured, double totaBasePrem) {

        if (year_F <= policyTerm) {
            double temp;
            double temp1;
            double temp2;

            temp1 = 10 * get_premium_withoutFreqLoading();
            temp2 = 1.1 * sumAssured;
            temp = Math.max(temp1, temp2);
            //System.out.println("temp "+temp);
            return Math.round(temp);
        } else {
            return 0;
        }
    }

    public double getAP_without_Modal_Loading() {
        int divFact = 0;
        if (smartIncomeProtectBean.getPremFreq().equals("Yearly")) {
            divFact = 1;
        } else if (smartIncomeProtectBean.getPremFreq()
                .equals("Half Yearly")) {
            divFact = 2;
        } else if (smartIncomeProtectBean.getPremFreq().equals("Quarterly")) {
            divFact = 4;
        } else {
            divFact = 12;
        }

        // System.out.println("DB" + getPR_Basic_FromDB());
        // System.out.println("staff rebate" + getStaffRebate("Basic"));
        // System.out.println("SA rebate" + getSA_Rebate_Basic());
        // System.out.println("loading "+ getLoadingForFreqOfPremiums("Basic"));
        // System.out.println("Frequency**" +
        // smartIncomeProtectBean.getPremiumFreq()+"**" + " Age : "+
        // smartIncomeProtectBean.getAge()+" "+smartIncomeProtectBean.getBasicTerm());
        return (getPR_Basic_FromDB() * (1 - 0) - getSA_Rebate_Basic() + (getNSAP() + getEMR()))
                * smartIncomeProtectBean.getBasicSA()
                / 1000;
    }

    public double getNonGuarntedDeathBenefit(int year_F,
                                             String returnOnInvestment, int policyTerm, int age, double sumAssured, double totaBasePrem) {

        double a, b;
        if (year_F <= policyTerm) {
            a = year_F * sumAssured * getBonusRate(returnOnInvestment, smartIncomeProtectBean.getBasicTerm(), year_F);
            //System.out.println("a "+a);
        } else {
            a = 0;
        }
//		if(year_F==policyTerm)
//			b=a*(prop.terminalBonus+1);
//		else
//			b=a*1;

        return a;


    }

//	public double getGuaranMatBen(int year_F) {
//		if (year_F > smartIncomeProtectBean.getBasicTerm()
//				&& (smartIncomeProtectBean.getBasicTerm() + 16) > year_F) {
//			return (double) 0.11 * smartIncomeProtectBean.getSumAssured_Basic();
//		} else {
//			return 0;
//		}
//	}

    public double getGuaranMatBen(int year_F) {
        double matben = 0;
        if (smartIncomeProtectBean.getMaturityOption().equals("Y")) {
            if (year_F == smartIncomeProtectBean.getBasicTerm()) {
                matben = 1.1 * smartIncomeProtectBean.getBasicSA();
            }
        } else {
            if (year_F > smartIncomeProtectBean.getBasicTerm()
                    && (smartIncomeProtectBean.getBasicTerm() + 16) > year_F) {
                matben = 0.11 * smartIncomeProtectBean.getBasicSA();

            } else {
                matben = 0;
            }

        }
        return matben;
    }

    public double getNonGuaranMatBen(int year_F, double nonGuarantedDeathBenefit) {
        if (year_F == smartIncomeProtectBean.getBasicTerm())
            return nonGuarantedDeathBenefit;
        else
            return 0;
    }

//	public double getGSV_SurrenderValue(int year_F, double totaBasePrem) {
//		int a = 0, arrCount = 0;
//		double ab = 0, b = 0;
//
//		double[] regularGSV = ac.getGSV_Factor_Arr();
//
//		if (year_F > smartIncomeProtectBean.getBasicTerm())
//			return 0;
//		else {
//			if (smartIncomeProtectBean.getBasicTerm() > 9) {
//				if (year_F > 2)
//					a = 1;
//				else
//					a = 0;
//			} else {
//				if (year_F > 1)
//					a = 1;
//				else
//					a = 0;
//			}
//
//			int ptArr[] = { 7, 12, 15 };
//
//			for (int i = 1; i <= 15; i++) {
//				for (int j = 0; j <= 2; j++) {
//					if ((i == year_F && smartIncomeProtectBean.getBasicTerm() == ptArr[j])) {
//						// //System.out.println(i +" "+ j);
//						b = regularGSV[arrCount];
//						break;
//					}
//					arrCount++;
//					// //System.out.println(i +" "+ j
//					// +" = "+prStrArr[arrCount]);
//				}
//
//			}
//			// System.out.println("GSV : "+b +"   "+a+"   "+totaBasePrem);
//			return Math.round(totaBasePrem * b * a);
//		}
//	}


    public double getGSV_SurrenderValue(int year_F, double sumGuaranteedSurvivalBen) {
        //get_Total_Base_Premium_Paid
        int a = 0, arrCount = 0;
        double ab = 0, b = 0, SurrenderValue;

        double[] regularGSV = ac.getGSV_Factor_Arr();

        if (year_F > smartIncomeProtectBean.getBasicTerm())
            return 0;
        else {
            int ptArr[] = {7, 12, 15};

            for (int i = 1; i <= 15; i++) {
                for (int j = 0; j <= 2; j++) {
                    if ((i == year_F && smartIncomeProtectBean.getBasicTerm() == ptArr[j])) {
                        // //System.out.println(i +" "+ j);
                        b = regularGSV[arrCount];
                        break;
                    }
                    arrCount++;
                    // //System.out.println(i +" "+ j
                    // +" = "+prStrArr[arrCount]);
                }

            }

            SurrenderValue = sumGuaranteedSurvivalBen * b;
//			System.out.println("sumGuaranteedSurvivalBen "+sumGuaranteedSurvivalBen);
//			System.out.println("SurrenderValue "+SurrenderValue);
            return SurrenderValue;
        }
    }

//	public double getNonGSV_surrndr_val(int year_F,int policyterm,double sumassured, double nongrntdDeathNenft,double sumrevesionaryBonus4per) {
//		double a = 0, b = 0, c = 0,p=0,temp = 0;
//		int ptArr[] = { 7, 12, 15 };
//		if (year_F > smartIncomeProtectBean.getBasicTerm())
//			return 0;
//		else {
//			if (smartIncomeProtectBean.getPremiumFreq().equalsIgnoreCase(
//					"Single"))
//				return 0;
//			else {
//
//				double[] regularSSV = ac.getSSV_RP_Arr();
//				int arrCount = 0;
//				double regularSSVFactor = 0;
//
//				for (int i = 1; i <= 15; i++) {
//					for (int j = 0; j <= 2; j++) {
//						if ((i == year_F && smartIncomeProtectBean
//								.getBasicTerm() == ptArr[j])) {
//							// //System.out.println(i +" "+ j);
//							b = regularSSV[arrCount];
//							break;
//						}
//						arrCount++;
//						// //System.out.println(i +" "+ j
//						// +" = "+prStrArr[arrCount]);
//					}
//
//				}
//				// System.out.println("SSV : "+b);
//				double temp2 = Double.parseDouble(String
//						.valueOf(smartIncomeProtectBean.getBasicTerm()));
//				double temp1 = Double.parseDouble(String
//						.valueOf(smartIncomeProtectBean.getSumAssured_Basic()));
//				//a = year_F / temp2 * temp1;
//				a= year_F/policyterm;
//				c= sumassured + sumrevesionaryBonus4per;
//
//				p = a*c;
//
////				return (Math.round(Double.parseDouble(commonForAllProd
////						.getRoundOffLevel2(commonForAllProd
////								.getStringWithout_E((a + nongrntdDeathNenft)
////										* b)))));
//				//System.out.println("surr4per "+ (p*b));
//				return p * b;
//
//			}
//		}
//	}

    //added by sujata on 19-02-2020
    public double getNonGSV_surrndr_val4percent(int year_F, double policyterm, double sumassured, double nongrntdDeathNenft, double nongrntdDeathNenft_4Percent) {
        double a, b = 0, c = 0, p = 0, temp = 0;
        int ptArr[] = {7, 12, 15};
        if (year_F > smartIncomeProtectBean.getBasicTerm())
            return 0;
        else {

            double[] regularSSV = ac.getSSV_RP_Arr();
            int arrCount = 0;
            double regularSSVFactor = 0;

            a = (year_F / policyterm * sumassured + nongrntdDeathNenft_4Percent);


            for (int i = 1; i <= 15; i++) {
                for (int j = 0; j <= 2; j++) {
                    if ((i == year_F && smartIncomeProtectBean.getBasicTerm() == ptArr[j])) {
                        // //System.out.println(i +" "+ j);
                        b = regularSSV[arrCount];
                        //System.out.println("b "+b);
                        break;
                    }
                    arrCount++;

                }

            }
            //	System.out.println("asdf "+ (a*b));
        }
        return a * b;
    }


    public double getNonGSV_surrndr_val8percent(int year_F, double policyterm, double sumassured, double nongrntdDeathNenft, double nongrntdDeathNenft_8Percent) {
        double a, b = 0, c = 0, p = 0, temp = 0;
        int ptArr[] = {7, 12, 15};
        if (year_F > smartIncomeProtectBean.getBasicTerm())
            return 0;
        else {

            double[] regularSSV = ac.getSSV_RP_Arr();
            int arrCount = 0;
            double regularSSVFactor = 0;

            a = (year_F / policyterm * sumassured + nongrntdDeathNenft_8Percent);

            for (int i = 1; i <= 15; i++) {
                for (int j = 0; j <= 2; j++) {
                    if ((i == year_F && smartIncomeProtectBean
                            .getBasicTerm() == ptArr[j])) {
                        // //System.out.println(i +" "+ j);
                        b = regularSSV[arrCount];
                        //	System.out.println("b "+b);
                        break;
                    }
                    arrCount++;
                }

            }
            //System.out.println("asdf "+ (a*b));
        }
        return a * b;
    }

    /********************************* Added by Akshaya on 04-AUG-2015 start *****************/

    // Returns basic premium not rounded and without service tax
    public double get_Premium_Basic_WithoutStaffDisc_NotRounded() {
        int divFact = 0;
        if (smartIncomeProtectBean.getPremFreq().equals("Yearly")) {
            divFact = 1;
        } else if (smartIncomeProtectBean.getPremFreq()
                .equals("Half Yearly")) {
            divFact = 2;
        } else if (smartIncomeProtectBean.getPremFreq().equals("Quarterly")) {
            divFact = 4;
        } else {
            divFact = 12;
        }

        // System.out.println("DB" + getPR_Basic_FromDB());
        // System.out.println("staff rebate" + getStaffRebate("Basic"));
        // System.out.println("SA rebate" + getSA_Rebate_Basic());
        // System.out.println("loading "+ getLoadingForFreqOfPremiums("Basic"));
        // System.out.println("Frequency**" +
        // smartIncomeProtectBean.getPremFreq()+"**" + " Age : "+
        // smartIncomeProtectBean.getAge()+" "+smartIncomeProtectBean.getBasicTerm());
        return (getPR_Basic_FromDB() * (1 - 0) - getSA_Rebate_Basic() + (getNSAP() + getEMR()))
                * smartIncomeProtectBean.getBasicSA()
                / 1000
                * getLoadingForFreqOfPremiums("Basic") / 1;
    }

    /********************************* Added by Akshaya on 04-AUG-2015 start *****************/

    // Returns basic premium not rounded and without service tax
    public double get_Premium_Basic_WithoutStaffDiscSA_NotRounded() {
        int divFact = 0;
        if (smartIncomeProtectBean.getPremFreq().equals("Yearly")) {
            divFact = 1;
        } else if (smartIncomeProtectBean.getPremFreq()
                .equals("Half Yearly")) {
            divFact = 2;
        } else if (smartIncomeProtectBean.getPremFreq().equals("Quarterly")) {
            divFact = 4;
        } else {
            divFact = 12;
        }

        // System.out.println("DB" + getPR_Basic_FromDB());
        // System.out.println("staff rebate" + getStaffRebate("Basic"));
        // System.out.println("SA rebate" + getSA_Rebate_Basic());
        // System.out.println("loading "+ getLoadingForFreqOfPremiums("Basic"));
        // System.out.println("Frequency**" +
        // smartIncomeProtectBean.getPremFreq()+"**" + " Age : "+
        // smartIncomeProtectBean.getAge()+" "+smartIncomeProtectBean.getBasicTerm());

        // return (getPR_Basic_FromDB() * (1 - 0) - 0 + (getNSAP() + getEMR()))
        // * smartIncomeProtectBean.getBasicSA() / 1000
        // * getLoadingForFreqOfPremiums("Basic") / divFact;

        return (getPR_Basic_FromDB() * (1 - 0) - 0 + (getNSAP() + getEMR()))
                * smartIncomeProtectBean.getBasicSA() / 1000;

    }

    /********************************* Added by Akshaya on 04-AUG-2015 start *****************/
    // returns PT premium
    public double get_Premium_PT_WithoutStaffDisc_NotRounded() {
        int divFactor;
        if (smartIncomeProtectBean.getPremFreq().equals("Yearly")) {
            divFactor = 1;
        } else if (smartIncomeProtectBean.getPremFreq()
                .equals("Half Yearly")) {
            divFactor = 2;
        } else if (smartIncomeProtectBean.getPremFreq().equals("Quarterly")) {
            divFactor = 4;
        } else {
            divFactor = 12;
        }
        // System.out.println("PT rate" + getPR_PTA_FromDB() +
        // " SA :  "+getStaffRebate("Basic") +
        // " Loadng Frq:  "+getLoadingForFreqOfPremiums("Rider"));
        return (getPR_PTA_FromDB() * (1 - 0) - getSA_Rebate_Rider() + (getNSAP() + getEMR()))
                * smartIncomeProtectBean.getPreferredSA()
                / 1000
                * getLoadingForFreqOfPremiums("Rider") / divFactor;
    }

    /*** Modified by Akshaya on 31-AUG-15 Start ***/
    // returns PT premium
    public double get_Premium_PT_WithoutStaffDiscSA_NotRounded() {
        int divFactor = 0;
        if (smartIncomeProtectBean.getPremFreq().equals("Yearly")) {
            divFactor = 1;
        } else if (smartIncomeProtectBean.getPremFreq()
                .equals("Half Yearly")) {
            divFactor = 2;
        } else if (smartIncomeProtectBean.getPremFreq().equals("Quarterly")) {
            divFactor = 4;
        } else {
            divFactor = 12;
        }
        // System.out.println("PT rate" + getPR_PTA_FromDB() +
        // " SA :  "+getStaffRebate("Basic") +
        // " Loadng Frq:  "+getLoadingForFreqOfPremiums("Rider"));

        return (getPR_PTA_FromDB() * (1 - 0) - 0 + (getNSAP() + getEMR()))
                * smartIncomeProtectBean.getPreferredSA() / 1000;

    }

    /********************************* Added by Akshaya on 04-AUG-2015 start *****************/
    // returns ADB premium
    public double get_Premium_ADB_WithoutStaffDisc_NotRounded() {
        int divFactor;
        if (smartIncomeProtectBean.getPremFreq().equals("Yearly")) {
            divFactor = 1;
        } else if (smartIncomeProtectBean.getPremFreq()
                .equals("Half Yearly")) {
            divFactor = 2;
        } else if (smartIncomeProtectBean.getPremFreq().equals("Quarterly")) {
            divFactor = 4;
        } else {
            divFactor = 12;
        }
        // System.out.println("ADB rate" + getPR_Rider_FromDB("ADB") + " SA :  "
        // + getStaffRebate("Basic") + " Loadng Frq:  "
        // + getLoadingForFreqOfPremiums("Rider"));

        return (getPR_Rider_FromDB("ADB") * (1 - 0) - getSA_Rebate_Rider() + (getNSAP() + getEMR()))
                * smartIncomeProtectBean.getADB_SA()
                / 1000
                * getLoadingForFreqOfPremiums("Rider") / divFactor;
    }

    /********************************* Added by Akshaya on 31-AUG-2015 start *****************/
    // returns ADB premium
    public double get_Premium_ADB_WithoutStaffDiscSA_NotRounded() {
        int divFactor = 0;
        if (smartIncomeProtectBean.getPremFreq().equals("Yearly")) {
            divFactor = 1;
        } else if (smartIncomeProtectBean.getPremFreq()
                .equals("Half Yearly")) {
            divFactor = 2;
        } else if (smartIncomeProtectBean.getPremFreq().equals("Quarterly")) {
            divFactor = 4;
        } else {
            divFactor = 12;
        }
        // System.out.println("ADB rate" + getPR_Rider_FromDB("ADB") + " SA :  "
        // + getStaffRebate("Basic") + " Loadng Frq:  "
        // + getLoadingForFreqOfPremiums("Rider"));

        // return (
        // getPR_Rider_FromDB("ADB")*(1-0)-getSA_Rebate_Rider()+(getNSAP() +
        // getEMR()))*smartIncomeProtectBean.getADB_SA()/1000*getLoadingForFreqOfPremiums("Rider")/divFactor;

        return (getPR_Rider_FromDB("ADB") * (1 - 0) - 0 + (getNSAP() + getEMR()))
                * smartIncomeProtectBean.getADB_SA() / 1000;
    }

    /********************************* Added by Akshaya on 04-AUG-2015 start *****************/
    // returns ATPDB premium
    public double get_Premium_ATPDB_WithoutStaffDisc_NotRounded() {
        int divFactor;
        if (smartIncomeProtectBean.getPremFreq().equals("Yearly")) {
            divFactor = 1;
        } else if (smartIncomeProtectBean.getPremFreq()
                .equals("Half Yearly")) {
            divFactor = 2;
        } else if (smartIncomeProtectBean.getPremFreq().equals("Quarterly")) {
            divFactor = 4;
        } else {
            divFactor = 12;
        }
        // System.out.println("ATPD rate" + getPR_Rider_FromDB("ATPDB")
        // + " SA :  " + getStaffRebate("Basic") + " Loadng Frq:  "
        // + getLoadingForFreqOfPremiums("Rider"));

        return (getPR_Rider_FromDB("ATPDB") * (1 - 0) - getSA_Rebate_Rider() + (getNSAP() + getEMR()))
                * smartIncomeProtectBean.getATPDB_SA()
                / 1000
                * getLoadingForFreqOfPremiums("Rider") / divFactor;
    }

    /********************************* Added by Akshaya on 31-AUG-2015 start *****************/
    // returns ATPDB premium
    public double get_Premium_ATPDB_WithoutStaffDiscSA_NotRounded() {
        int divFactor = 0;
        if (smartIncomeProtectBean.getPremFreq().equals("Yearly")) {
            divFactor = 1;
        } else if (smartIncomeProtectBean.getPremFreq()
                .equals("Half Yearly")) {
            divFactor = 2;
        } else if (smartIncomeProtectBean.getPremFreq().equals("Quarterly")) {
            divFactor = 4;
        } else {
            divFactor = 12;
        }
        // System.out.println("ATPD rate" + getPR_Rider_FromDB("ATPDB")
        // + " SA :  " + getStaffRebate("Basic") + " Loadng Frq:  "
        // + getLoadingForFreqOfPremiums("Rider"));

        // return (
        // getPR_Rider_FromDB("ATPDB")*(1-0)-getSA_Rebate_Rider()+(getNSAP() +
        // getEMR()))*smartIncomeProtectBean.getATPDB_SA()/1000*getLoadingForFreqOfPremiums("Rider")/divFactor;

        return (getPR_Rider_FromDB("ATPDB") * (1 - 0) - 0 + (getNSAP() + getEMR()))
                * smartIncomeProtectBean.getATPDB_SA() / 1000;
    }

    /********************************* Added by Akshaya on 04-AUG-2015 start *****************/
    // returns cc13Nonlinked rider premium
    public double get_Premium_CC13NonLinked_WithoutStaffDisc_NotRounded() {
        int divFactor;
        if (smartIncomeProtectBean.getPremFreq().equals("Yearly")) {
            divFactor = 1;
        } else if (smartIncomeProtectBean.getPremFreq()
                .equals("Half Yearly")) {
            divFactor = 2;
        } else if (smartIncomeProtectBean.getPremFreq().equals("Quarterly")) {
            divFactor = 4;
        } else {
            divFactor = 12;
        }
        // System.out.println("CC13NonLinked rate"
        // +getPR_Rider_FromDB("CC13NonLinked") +
        // " SA :  "+getStaffRebate("CCNonLinked") +
        // " Loadng Frq:  "+getLoadingForFreqOfPremiums("Rider"));

        // System.out.println("cc staff rebate " +
        // getStaffRebate("CCNonLinked"));

        return (getPR_Rider_FromDB("CC13NonLinked") * (1 - 0)
                - getSA_Rebate_Rider() + (getNSAP() + getEMR()))
                * smartIncomeProtectBean.getCc13nlSA()
                / 1000
                * getLoadingForFreqOfPremiums("Rider") / divFactor;
    }

    /********************************* Added by Akshaya on 31-AUG-2015 start *****************/
    // returns cc13Nonlinked rider premium
    public double get_Premium_CC13NonLinked_WithoutStaffDiscSA_NotRounded() {
        int divFactor = 0;
        if (smartIncomeProtectBean.getPremFreq().equals("Yearly")) {
            divFactor = 1;
        } else if (smartIncomeProtectBean.getPremFreq()
                .equals("Half Yearly")) {
            divFactor = 2;
        } else if (smartIncomeProtectBean.getPremFreq().equals("Quarterly")) {
            divFactor = 4;
        } else {
            divFactor = 12;
        }
        // System.out.println("CC13NonLinked rate"
        // +getPR_Rider_FromDB("CC13NonLinked") +
        // " SA :  "+getStaffRebate("CCNonLinked") +
        // " Loadng Frq:  "+getLoadingForFreqOfPremiums("Rider"));

        // System.out.println("cc staff rebate " +
        // getStaffRebate("CCNonLinked"));

        // return (
        // getPR_Rider_FromDB("CC13NonLinked")*(1-0)-getSA_Rebate_Rider()+(getNSAP()
        // +
        // getEMR()))*smartIncomeProtectBean.getCc13nlSA()/1000*getLoadingForFreqOfPremiums("Basic")/divFactor;
        return (getPR_Rider_FromDB("CC13NonLinked") * (1 - 0) - 0 + (getNSAP() + getEMR()))
                * smartIncomeProtectBean.getCc13nlSA() / 1000;

    }

    /********************************* Added by Akshaya on 31-AUG-2015 end *****************/

//	public double getServiceTax(double premiumWithoutST, boolean JKResident,
//			String type,boolean state) {
//		if (type.equals("basic")) {
//			if (JKResident == true)
//				return Double
//						.parseDouble(commonForAllProd.getRoundUp(String
//								.valueOf(premiumWithoutST
//										* prop.serviceTaxJkResident)));
//			else {
//				// System.out.println("" + premiumWithoutST*prop.serviceTax);
//				// System.out.println("" +
//				// commonForAllProd.roundUp_Level2(String.valueOf(premiumWithoutST*prop.serviceTax))
//				// );
//				return Double.parseDouble(commonForAllProd
//						.getRoundUp(commonForAllProd.roundUp_Level2(String
//								.valueOf(premiumWithoutST * prop.serviceTax))));
//			}
//		} else if (type.equals("SBC")) {
//			if (JKResident == true)
//				return 0;
//			else {
//				return Double.parseDouble(commonForAllProd.getRoundUp(String
//						.valueOf(premiumWithoutST * prop.SBCServiceTax)));
//			}
//		}
//	//  Added By Saurabh Jain on 14/05/2019 Start
//		else if (type.equals("KERALA")) {
//				return Double.parseDouble(commonForAllProd.getRoundUp(String
//						.valueOf(premiumWithoutST * prop.kerlaServiceTax)));
//		}
//	//  Added By Saurabh Jain on 14/05/2019 End
//		else // KKC
//		{
//			if (JKResident == true)
//				return 0;
//			else {
//				return Double.parseDouble(commonForAllProd.getRoundUp(String
//						.valueOf(premiumWithoutST * prop.KKCServiceTax)));
//			}
//		}
//
//	}


    //added by sujata on 28-02-2020
    public double getServiceTaxBI(double premiumWithoutST, boolean JKResident,
                                  String type, boolean state) {

        double temp1, temp2;
        if (type.equals("basic")) {
            if (JKResident == true)
                return Double
                        .parseDouble(commonForAllProd.getRoundUp(String
                                .valueOf(premiumWithoutST
                                        * prop.serviceTaxJkResident)));
            else {

                temp1 = Double.parseDouble(commonForAllProd
                        .getRoundUp(commonForAllProd.roundUp_Level2(String
                                .valueOf(premiumWithoutST * (prop.basetax + 1)))));

                temp2 = temp1 - premiumWithoutST;
                System.out.println("temp1 " + temp1);
                System.out.println("temp2 " + temp2);

                return temp2;
            }
        } else if (type.equals("SBC")) {
            if (JKResident == true)
                return 0;
            else {

                return 0;
            }
        } else if (type.equals("KERALA")) {
            return Double.parseDouble(commonForAllProd.getRoundUp(String
                    .valueOf(premiumWithoutST * prop.kerlaServiceTax)));
        } else // KKC
        {
            if (JKResident == true)
                return 0;
            else {

                return 0;
            }
        }
    }


    public double getServiceTaxBISecondYear(double premiumWithoutST, boolean JKResident,
                                            String type, boolean state) {
        if (type.equals("basic")) {
            if (JKResident == true)
                return Double
                        .parseDouble(commonForAllProd.getRoundUp(String
                                .valueOf(premiumWithoutST
                                        * prop.serviceTaxJkResident)));
            else {


                System.out.println("tax2 " + (Double.parseDouble(commonForAllProd
                        .getRoundUp(commonForAllProd.roundUp_Level2(String
                                .valueOf(premiumWithoutST * (prop.serviceTax)))))));

                return Double.parseDouble(commonForAllProd
                        .getRoundUp(commonForAllProd.roundUp_Level2(String
                                .valueOf(premiumWithoutST * (prop.serviceTax)))));
            }
        } else if (type.equals("SBC")) {
            if (JKResident == true)
                return 0;
            else {

                return 0;
            }
        } else if (type.equals("KERALA")) {
            return Double.parseDouble(commonForAllProd.getRoundUp(String
                    .valueOf(premiumWithoutST * prop.kerlaServiceTax)));
        } else // KKC
        {
            if (JKResident == true)
                return 0;
            else {

                return 0;
            }
        }
    }
    //end

    // added by sujata on 19-02-2020
    public double getServiceTax(double premiumWithoutST, boolean JKResident,
                                String type, boolean state) {
        double val1, val2, premST, kkcValue = 0;
        if (type.equals("basic")) {

            if (state == true) {
                premST = Double.parseDouble(commonForAllProd.getRoundUp(String.valueOf(premiumWithoutST * prop.kerlaServiceTax)));
                //premST=premST+premiumWithoutST;
            } else {
                premST = Double.parseDouble(commonForAllProd.getRoundUp(String.valueOf(premiumWithoutST * prop.serviceTax)));
            }
            premST = premST + Double.parseDouble(commonForAllProd.getRoundUp(String.valueOf(premiumWithoutST * prop.serviceTax)));
            //premST=premST+premiumWithoutST;
            //	System.out.println("premST 418 "+premST);
//			System.out.println("premiumWithoutST " + premiumWithoutST);
            return premST;
        } else {
            premST = 0;
            return premST;
        }
    }


    public double getServiceTaxSecondYear(double premiumWithoutST,
                                          boolean JKResident, String type, boolean state) {

        double val1, val2, premST, kkcValue = 0;
        if (type.equals("basic")) {

            if (state == true) {
                premST = Double.parseDouble(commonForAllProd.getRoundUp(String.valueOf(premiumWithoutST * prop.kerlaServiceTaxSecondYear)));
                //premST=premST+premiumWithoutST;
            } else {
                premST = Double.parseDouble(commonForAllProd.getRoundUp(String.valueOf(premiumWithoutST * prop.serviceTaxSecondYear)));
            }
            premST = premST + Double.parseDouble(commonForAllProd.getRoundUp(String.valueOf(premiumWithoutST * prop.serviceTaxSecondYear)));
            //premST=premST+premiumWithoutST;
            //	System.out.println("premSTsy "+premST);
//				System.out.println("premiumWithoutST " + premiumWithoutST);
            return premST;
        } else {
            premST = 0;
            return premST;
        }
    }

    /*** Added by Priyanka Warekar - 31-08-2018 - start *******/
    public double getServiceTaxMines(double MinesOccuInterest) {
        return Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getRoundOffLevel2(commonForAllProd.getStringWithout_E(MinesOccuInterest * (prop.serviceTax + prop.SBCServiceTax)))));
    }

    /*** Added by Priyanka Warekar - 31-08-2018 - end *******/

    public double getServiceTaxRider(double InstRider, boolean JKResident,
                                     String type, boolean state) {
        double val1, val2, premST, kkcValue = 0;
        if (type.equals("basic")) {

            if (state == true) {
                premST = Double.parseDouble(commonForAllProd.getRoundUp(String.valueOf(InstRider * prop.kerlaServiceTax)));
                //premST=premST+premiumWithoutST;
            } else {
                premST = Double.parseDouble(commonForAllProd.getRoundUp(String.valueOf(InstRider * prop.serviceTax)));
            }
            premST = premST + Double.parseDouble(commonForAllProd.getRoundUp(String.valueOf(InstRider * prop.serviceTax)));
            //premST=premST+premiumWithoutST;
            System.out.println("premST InstRider " + premST);
//			System.out.println("premiumWithoutST " + premiumWithoutST);
            return premST;
        } else {
            premST = 0;
            return premST;
        }
    }


    public double getServiceTaxRiderSY(double InstRider, boolean JKResident,
                                       String type, boolean state) {
        double val1, val2, premST, kkcValue = 0;
        if (type.equals("basic")) {

            if (state == true) {
                premST = Double.parseDouble(commonForAllProd.getRoundUp(String.valueOf(InstRider * prop.kerlaServiceTaxSecondYear)));
                //premST=premST+premiumWithoutST;
            } else {
                premST = Double.parseDouble(commonForAllProd.getRoundUp(String.valueOf(InstRider * prop.serviceTaxSecondYear)));
            }
            premST = premST + Double.parseDouble(commonForAllProd.getRoundUp(String.valueOf(InstRider * prop.serviceTaxSecondYear)));
            //premST=premST+premiumWithoutST;
            System.out.println("premST InstRider " + premST);
//			System.out.println("premiumWithoutST " + premiumWithoutST);
            return premST;
        } else {
            premST = 0;
            return premST;
        }
    }
//	public double getServiceTaxSecondYear(double premiumWithoutST,
//			boolean JKResident, String type) {
//		if (type.equals("basic")) {
//			if (JKResident == true)
//				return Double
//						.parseDouble(commonForAllProd.getRoundUp(String
//								.valueOf(premiumWithoutST
//										* prop.serviceTaxJkResident)));
//			else {
//				return Double
//						.parseDouble(commonForAllProd.getRoundUp(String
//								.valueOf(premiumWithoutST
//										* prop.serviceTaxSecondYear)));
//			}
//		} else if (type.equals("SBC")) {
//			if (JKResident == true)
//				return 0;
//			else {
//				return Double.parseDouble(commonForAllProd.getRoundUp(String
//						.valueOf(premiumWithoutST
//								* prop.SBCServiceTaxSecondYear)));
//			}
//		}
//	//  Added By Saurabh Jain on 14/05/2019 Start
//		else if (type.equals("KERALA")) {
//				return Double.parseDouble(commonForAllProd.getRoundUp(String
//						.valueOf(premiumWithoutST
//								* prop.kerlaServiceTaxSecondYear)));
//		}
//	//  Added By Saurabh Jain on 14/05/2019 End
//		else // KKC
//		{
//			if (JKResident == true)
//				return 0;
//			else {
//				return Double.parseDouble(commonForAllProd.getRoundUp(String
//						.valueOf(premiumWithoutST
//								* prop.KKCServiceTaxSecondYear)));
//			}
//		}
//
//	}


    /* Added By tushar Kotian On 2/8/2017  */

   /* public double getBackDateInterest(double grossPremium, String bkDate) {

        try {

            // double indigoRate=7.5;
            *//*
            double ST = 0;
            if (smartIncomeProtectBean.getJKResident()) {
                ST = prop.serviceTaxJkResident;
            } else {
                ST = prop.serviceTax+prop.SBCServiceTax+prop.KKCServiceTax;
            }
            *//*
//			double indigoRate = 10;

            *//*
            double indigoRate = 8.75;
            *//*

            double ServiceTaxValue = (ST + 1) * 100;

            *//*
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

            *//*
            // double netPremWithoutST=Math.round((grossPremium*100)/103.09);
            double netPremWithoutST = Math.round((grossPremium * 100)
                    / ServiceTaxValue);
            */

    /******************* Modified by Akshaya on 14-APR-2015 end **********//*
            // System.out.println("netPremWithoutST "+netPremWithoutST);
            int days = commonForAllProd.setDate(date, strBackDate);
            // System.out.println("no of days "+days);
            double monthsBetween = commonForAllProd.roundDown(
                    (double) days / 365 * 12, 0);
            // System.out.println("aaaaaaaaa "+(double)79/365);
            // System.out.println("month "+monthsBetween);

            double interest = getCompoundAmount(monthsBetween,
                    netPremWithoutST, indigoRate, compoundFreq);
            // System.out.println("onterest "+interest);
            return interest;
        }

        catch (Exception e) {
            return 0;
        }
    }*/

   /* public double getCompoundAmount(double monthsBetween,
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
    }*/

    //added by sujata on 19-02-2020
    public double getannulizedPremFinal(int year_F, int policyterm, String premfreq) {
        if (year_F <= policyterm) {

            return get_premium_withoutFreqLoading();
        } else {
            return 0;
        }

    }

    public double getSurvivalBenefit(int year, int policyterm, double SumAssured_Basic) {
        double survivalben;
        if ((year > policyterm) && (policyterm + 16 > year)) {
            //survivalben = SumAssured_Basic * prop.survivalbenper;
            survivalben = 0;
        } else {
            survivalben = 0;
        }
        return survivalben;
    }

    public double gettotalMaturityBenfit(int year, int policyterm, double nongrntdDeathNenft_4Percent) {
        double totalmat4per;
        if (year == policyterm) {
            totalmat4per = nongrntdDeathNenft_4Percent + 0.15 * nongrntdDeathNenft_4Percent;
        } else {
            totalmat4per = 0;
        }

        totalmat4per = totalmat4per + getGuaranMatBen(year);

        return totalmat4per;
    }

    public double gettotalMaturityBenfit8percent(int year, int policyterm, double nongrntdDeathNenft_8Percent) {
        double totalmat8per;
        if (year == policyterm) {
            totalmat8per = nongrntdDeathNenft_8Percent + 0.15 * nongrntdDeathNenft_8Percent;
        } else {
            totalmat8per = 0;
        }

        totalmat8per = totalmat8per + getGuaranMatBen(year);

        return totalmat8per;
    }

    public double getTotalDeathBenefit4percent(int year, int policyterm, double guarntdDeathBenft, double nongrntdDeathNenft_4Percent, double prempaid) {
        double totaldeathben, temp1, temp2;
        if (year <= policyterm) {
            temp1 = guarntdDeathBenft + nongrntdDeathNenft_4Percent + nongrntdDeathNenft_4Percent * 0.15;
            temp2 = 1.05 * prempaid;
            totaldeathben = Math.max(temp1, temp2);
            //System.out.println("prempaid "+prempaid);
            //	System.out.println("temp2 "+temp2);
        } else {
            totaldeathben = 0;
        }
        return totaldeathben;
    }

    public double getTotalDeathBenefit8percent(int year, int policyterm, double guarntdDeathBenft, double nongrntdDeathNenft_8Percent, double sumGuaranteedSurvivalBen) {
        double totaldeathben, temp1, temp2;
        if (year <= policyterm) {
            temp1 = guarntdDeathBenft + nongrntdDeathNenft_8Percent + nongrntdDeathNenft_8Percent * 0.15;
            temp2 = 1.05 * sumGuaranteedSurvivalBen;
            totaldeathben = Math.max(temp1, temp2);
        } else {
            totaldeathben = 0;
        }
        return totaldeathben;
    }
    //End

}
