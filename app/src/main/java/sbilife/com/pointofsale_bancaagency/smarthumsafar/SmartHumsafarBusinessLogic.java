package sbilife.com.pointofsale_bancaagency.smarthumsafar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import sbilife.com.pointofsale_bancaagency.common.CommonForAllProd;

class SmartHumsafarBusinessLogic {

    String premiumWithoutStwithotRoundup = null, premiumWithoutStwithotModelLoading = null,
            premiumWithoutStwtRoundup = null, premiumWithoutStFrequencyLoading = null, annulizedpremium = null,
            premiumWithoutStwtRoundupBasicPrem = null, PremiumWITHOUTroundup = null,
            premiumWithoutStwtRound = null, PremiumWithST = null,
            serviceTax = null, premiumAdbRiderLAWithoutRoundup = null,
            premiumAdbRiderLAWithoutRound = null,
            premiumAdbRiderSpouseWithoutRoundup = null,
            premiumAdbRiderSpouseWithoutRound = null,
            basicrider = null, InstbasicriderWITHOUTRoundup = null,
            premiumAdbRiderLAWithRoundup = null,
            premiumAdbRiderSpouseWithRoundup = null,
            basePremiumWithRiderWithRoundUP = null,
            totalBasePremiumPaid = null, GuaranteedDeathBenefit = null,
            nonGuarateedDeathBenefitAt_4_Percent = null,
            nonGuarateedDeathBenefitAt_4_PercentFinal = null,
            nonGuarateedDeathBenefitAt_8_PercentFinal = null,
            nonGuarateedDeathBenefitAt_8_Percent = null,
            guaranteedSurvivalBenefit = null,
            nonGuarateedSurvivalBenefitAt_4_Percent = null,
            nonGuarateedSurvivalBenefitAt_8_Percent = null,
            nonGuaranSurBenAt_4_Illustratn = null,
            nonGuaranSurBenAt_8_Illustratn = null,
            guaranteedDeathBenefitFirstDeath = null,
            guaranteedDeathBenefitSecondDeath = null,
            nonGuarateedDeathBenefitAt_4_illustration = null,
            nonGuarateedDeathBenefitAt_8_illustration = null,
            GSV_surrendr_val = null, NonGSV_surrndr_val_4_Percent = null,
            NonGSV_surrndr_val_8_Percent = null,
            premiumWithoutStWithoutDiscwithotRoundup = null,
            premiumWithoutStWithoutDiscwtRoundup = null,
            premiumAdbRiderLAWithoutDiscWithoutRoundup = null,
            premiumAdbRiderSpouseWithoutDiscWithoutRoundup = null,
            basePremiumWithoutDiscWithRiderWithRoundUP = null,
            basicPremiumWithoutRoundup = null, basicPremiumWithRoundup = null,
            basicPremiumAdbRiderLAWithoutRoundup = null,
            basicPremiumAdbRiderSpouseWithoutRoundup = null;

    private boolean isRiderEligible = false;
    private SmartHumsafarBean smarthumsafarbean;
    private SmartHumsafarDB objSmarthumsafarDB;
    private CommonForAllProd cfap = null;
    private SmartHumsafarProperties prop;

    /*** modified by Akshaya on 20-MAY-16 start **/
    public void setPremiumWithST() {
        if (smarthumsafarbean.getIsJKResidentDiscOrNot())
            PremiumWithST = cfap
                    .getRoundUp((cfap.getStringWithout_E((prop.serviceTaxJKResident + 1)
                            * Double.parseDouble(getBasicPremiumWithRiderWithRoundUP()))));// premium
            // with
            // total
            // basic
            // prem.
        else
            PremiumWithST = cfap
                    .getRoundUp(cfap.getStringWithout_E(((prop.serviceTax + 1) * Double
                            .parseDouble(getBasicPremiumWithRiderWithRoundUP()))));
    }

    public double getLoadingFrqOfPremium(String premFreqMode) {
        double loadngFreqOfPremiums = 0;
        // Loading for Frequency Of Premiums
        if (premFreqMode.equalsIgnoreCase("Yearly"))
            loadngFreqOfPremiums = 1;
        else if (premFreqMode.equalsIgnoreCase("Half Yearly"))
            loadngFreqOfPremiums = prop.HalfYearly_Modal_Factor;
        else if (premFreqMode.equalsIgnoreCase("Quarterly"))
            loadngFreqOfPremiums = prop.Quarterly_Modal_Factor;
        else
            loadngFreqOfPremiums = prop.Monthly_Modal_Factor;
        return loadngFreqOfPremiums;
    }

    // ########################

    public SmartHumsafarBusinessLogic(SmartHumsafarBean smarthumsafarbean) {
        this.smarthumsafarbean = smarthumsafarbean;
        objSmarthumsafarDB = new SmartHumsafarDB();
        cfap = new CommonForAllProd();
        prop = new SmartHumsafarProperties();
    }

    public void setPremiumWithoutSTwithoutRoundup() {
        double staffRebate = getStaffRebate(smarthumsafarbean
                .getIsStaffDiscOrNot());
        double tabularRate = getTabularRatePremium();
        double SARebate = getSARebate(smarthumsafarbean.getsumAssured());
        double NSAPRates = 0;
        double EMR = 0;
        double loadingFreq = getLoadingFrqOfPremium(
                smarthumsafarbean.getPremFreqMode(), "Basic");
        /**** Added By Priyanka Warekar - 08-08-2016 - Start ****/
        // double premFreqMultiFactor =
        // setPremiumMultiplication(smarthumsafarbean
        // .getPremFreqMode());
        double premFreqMultiFactor = 1;
        /**** Added By Priyanka Warekar - 08-08-2016 - Start ****/
        // System.out.println(tabularRate + "  " + staffRebate + "   " +
        // SARebate
        // + "   " + NSAPRates + "  " + EMR + "  " + loadingFreq + "  "
        // + premFreqMultiFactor);
        this.premiumWithoutStwithotRoundup = ""
                + ((tabularRate * (1.0 - staffRebate) - SARebate + (NSAPRates + EMR))
                * smarthumsafarbean.getsumAssured()
                / 1000.0
                * loadingFreq / premFreqMultiFactor);
        // System.out.println(" premiumWithoutStwithotRoundup : "
        // + this.premiumWithoutStwithotRoundup);
    }

    public String getPremiumWithoutSTwithoutRoundup() {
        //System.out.println("premiumWithoutStwithotRoundup12 "+premiumWithoutStwithotRoundup);
        return this.premiumWithoutStwithotRoundup;
    }

    //added by sujata 27-02-2020
    public void setPremiumwithoutModelLoading() {
        double staffRebate = getStaffRebate(smarthumsafarbean
                .getIsStaffDiscOrNot());
        double tabularRate = getTabularRatePremium();
        double SARebate = getSARebate(smarthumsafarbean.getsumAssured());
        double NSAPRates = 0;
        double EMR = 0;
        double loadingFreq = getLoadingFrqOfPremium(
                smarthumsafarbean.getPremFreqMode(), "Basic");

        double premFreqMultiFactor = 1;

        this.premiumWithoutStwithotModelLoading = ""
                + (cfap.getRoundUp(cfap.getStringWithout_E((tabularRate * (1.0 - staffRebate) - SARebate + (NSAPRates + EMR))
                * smarthumsafarbean.getsumAssured()
                / 1000.0)));
//		 System.out.println(" premiumWithoutStwithotRoundup : "
//		 + this.premiumWithoutStwithotRoundup);
    }

    public String getPremiumwithoutModelLoading() {
        //System.out.println("premiumWithoutStwithotRoundup12 "+premiumWithoutStwithotRoundup);
        return this.premiumWithoutStwithotModelLoading;
    }

    //end

    public void setPremiumWithoutSTwithRoundup() {
        this.premiumWithoutStwtRoundup = cfap.roundUp_Level2(cfap
                .getStringWithout_E(Double
                        .parseDouble(getPremiumWithoutSTwithoutRoundup())));
    }

    public String getPremiumWithoutSTwithRoundup() {
        return this.premiumWithoutStwtRoundup;
    }

    //added by sujata on 25-02-2020
    public void setPremiumWithoutSTwithRoundupBasicPrem() {
        this.premiumWithoutStwtRoundupBasicPrem = cfap.getRoundUp(cfap.getRoundOffLevel2(cfap
                .getStringWithout_E(Double
                        .parseDouble(getPremiumWithoutSTwithoutRoundup()))));
    }

    public String getPremiumWithoutSTwithRoundupBasicPrem() {
        //System.out.println("premiumWithoutStwtRoundupBasicPrem "+premiumWithoutStwtRoundupBasicPrem);
        return this.premiumWithoutStwtRoundupBasicPrem;
    }

    //added by sujata on 29-02-2020
    public void setPremiumWithoutSTWITHOUTroundup() {
        this.PremiumWITHOUTroundup = (cfap
                .getStringWithout_E(Double
                        .parseDouble(getPremiumWithoutSTwithoutRoundup())));
    }

    public String getPremiumWithoutSTWITHOUTroundup() {
        //	System.out.println("PremiumWITHOUTroundup "+PremiumWITHOUTroundup);
        return this.PremiumWITHOUTroundup;
    }

    //end
    public void setPremiumRound() {
        this.premiumWithoutStwtRound = cfap.getRound(cfap
                .getStringWithout_E(Double
                        .parseDouble(getPremiumWithoutSTwithoutRoundup())));
    }

    public String getPremiumRound() {
        return this.premiumWithoutStwtRound;
    }


    //end
    //commented by sujata on 25-02-2020
//	public double getServiceTax(double premiumWithoutST, boolean JKResident,
//			String type) {
//		if (type.equals("basic")) {
    // if(JKResident==true)
//				return Double
//						.parseDouble(cfap.getRoundUp(String
//								.valueOf(premiumWithoutST
//										* prop.serviceTaxJKResident)));
//			else {
//				return Double.parseDouble(cfap.getRoundUp(String
//						.valueOf(premiumWithoutST * prop.serviceTax)));
    // }
//		} else if (type.equals("SBC")) {
    // if(JKResident==true)
    // return 0;
//			else {
//				return Double.parseDouble(cfap.getRoundUp(String
//						.valueOf(premiumWithoutST * prop.SBCServiceTax)));
//			}
//		}
//	//  Added By Saurabh Jain on 16/05/2019 Start
//		else if (type.equals("KERALA"))
    // {
//				return Double.parseDouble(cfap.getRoundUp(String
//						.valueOf(premiumWithoutST * prop.kerlaServiceTax)));
    // }
//	//  Added By Saurabh Jain on 16/05/2019 End
    // else //KKC
    // {
    // if(JKResident==true)
    // return 0;
//			else {
//				return Double.parseDouble(cfap.getRoundUp(String
//						.valueOf(premiumWithoutST * prop.KKCServiceTax)));
    // }
    // }
    //
    // }
    //End


    // added by sujata on 25-02-2020
    public double getServiceTax(double premiumWithoutST, boolean JKResident,
                                String type, boolean state) {
        double val1, val2, premST, kkcValue = 0;
        if (type.equals("basic")) {

            if (state == true) {
                premST = Double.parseDouble(cfap.getRoundUp(String.valueOf(premiumWithoutST * prop.kerlaServiceTax)));
                //	System.out.println("premST "+premST);
                //premST=premST+premiumWithoutST;
            } else {
                premST = Double.parseDouble(cfap.getRoundUp(String.valueOf(premiumWithoutST * prop.serviceTax)));
            }
            premST = premST + Double.parseDouble(cfap.getRoundUp(String.valueOf(premiumWithoutST * prop.serviceTax)));
            //premST=premST+premiumWithoutST;
            //	System.out.println("premST 418 "+premST);
//				System.out.println("premiumWithoutST " + premiumWithoutST);
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
                premST = Double.parseDouble(cfap.getRoundUp(String.valueOf(premiumWithoutST * prop.kerlaServiceTaxSecondYear)));
                //premST=premST+premiumWithoutST;
            } else {
                premST = Double.parseDouble(cfap.getRoundUp(String.valueOf(premiumWithoutST * prop.serviceTaxSecondYear)));
            }
            premST = premST + Double.parseDouble(cfap.getRoundUp(String.valueOf(premiumWithoutST * prop.serviceTaxSecondYear)));
            //premST=premST+premiumWithoutST;
            //	System.out.println("premSTsy "+premST);
//					System.out.println("premiumWithoutST " + premiumWithoutST);
            return premST;
        } else {
            premST = 0;
            return premST;
        }
    }

    /*******************start*************************/
    //added by sujata on 25-02-2020 for Rider first and second year
    public double getServiceTaxRider(double InstRider, boolean JKResident,
                                     String type, boolean state) {
        double val1, val2, premST, kkcValue = 0;
        if (type.equals("basic")) {

            if (state == true) {
                premST = Double.parseDouble(cfap.getRoundUp(String.valueOf(InstRider * prop.kerlaServiceTax)));
                //premST=premST+premiumWithoutST;
            } else {
                premST = Double.parseDouble(cfap.getRoundUp(String.valueOf(InstRider * prop.serviceTax)));
            }
            premST = premST + Double.parseDouble(cfap.getRoundUp(String.valueOf(InstRider * prop.serviceTax)));
            //premST=premST+premiumWithoutST;
            //System.out.println("premST InstRider "+premST);
//				System.out.println("premiumWithoutST " + premiumWithoutST);
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
                premST = Double.parseDouble(cfap.getRoundUp(String.valueOf(InstRider * prop.kerlaServiceTaxSecondYear)));
                //premST=premST+premiumWithoutST;
            } else {
                premST = Double.parseDouble(cfap.getRoundUp(String.valueOf(InstRider * prop.serviceTaxSecondYear)));
            }
            premST = premST + Double.parseDouble(cfap.getRoundUp(String.valueOf(InstRider * prop.serviceTaxSecondYear)));
            //premST=premST+premiumWithoutST;
            //	System.out.println("premST InstRider "+premST);
//				System.out.println("premiumWithoutST " + premiumWithoutST);
            return premST;
        } else {
            premST = 0;
            return premST;
        }
    }
    //end

    /*********************end****************************/

    public double setPremiumMultiplication(String premFreqMode) {
        if (premFreqMode.equalsIgnoreCase("Yearly"))
            return 1;
        else if (premFreqMode.equalsIgnoreCase("Half Yearly"))
            return 2;
        else if (premFreqMode.equalsIgnoreCase("Quarterly"))
            return 4;
        else
            return 12;
    }

    private double getLoadingFrqOfPremium(String premFreqMode, String cover) {

        double loadngFreqOfPremiums = 0;
        // Loading for Frequency Of Premiums
        if (cover.equals("Basic")) {
            if (premFreqMode.equalsIgnoreCase("Yearly")
                    || premFreqMode.equalsIgnoreCase("Single"))
                loadngFreqOfPremiums = 1;
            else if (premFreqMode.equalsIgnoreCase("Half Yearly"))
                loadngFreqOfPremiums = prop.HalfYearly_Modal_Factor;
            else if (premFreqMode.equalsIgnoreCase("Quarterly"))
                loadngFreqOfPremiums = prop.Quarterly_Modal_Factor;
            else
                loadngFreqOfPremiums = prop.Monthly_Modal_Factor;
        } else {
            if (premFreqMode.equalsIgnoreCase("Yearly")
                    || premFreqMode.equalsIgnoreCase("Single"))
                loadngFreqOfPremiums = 1;
            else if (premFreqMode.equalsIgnoreCase("Half Yearly"))
                loadngFreqOfPremiums = 1.04;
            else if (premFreqMode.equalsIgnoreCase("Quarterly"))
                loadngFreqOfPremiums = 1.06;
            else
                loadngFreqOfPremiums = 1.068;
        }
        return loadngFreqOfPremiums;
    }

    private double getSARebate(double sumAssured) {
        double SARebate = 0;
        if (sumAssured >= 100000 && sumAssured < 300000)
            SARebate = 0;
        else if (sumAssured >= 300000 && sumAssured < 500000)
            SARebate = 2;
        else
            SARebate = 3;

        return SARebate;
    }

    public double getStaffRebate(boolean staff) {
        double staff_Rebate;
        // staffRebate
        if (staff) {
            staff_Rebate = 0.06;
        } else
            staff_Rebate = 0;
        return staff_Rebate;

    }

    private double getTabularRatePremium() {

        double[] premiumArr = objSmarthumsafarDB.getPremiumRates();
        int position = 0;
        double TabularPremiumRate = 0;
        int equivalentAge = getEquivalentAge(smarthumsafarbean.getAgeLA(),
                smarthumsafarbean.getAgeSpouse());
        for (int ageCount = 18; ageCount <= 46; ageCount++) {
            for (int policyTermCount = 10; policyTermCount <= 30; policyTermCount++) {
                if (ageCount == equivalentAge
                        && policyTermCount == smarthumsafarbean
                        .getPolicyTerm_Basic()) {
                    TabularPremiumRate = premiumArr[position];
                    break;
                }
                position++;
            }
        }
        return TabularPremiumRate;
    }

    public int getEquivalentAge(int ageLA, int ageSpouse) {
        int ageDiff = 0;
        if (ageLA >= ageSpouse)
            ageDiff = ageLA - ageSpouse;
        else
            ageDiff = ageSpouse - ageLA;
        if (ageDiff > 20)
            return 0;
        else {
            int arr[] = {0, 1, 1, 2, 2, 3, 3, 4, 5, 5, 6, 7, 8, 8, 9, 10, 11,
                    11, 12, 13, 14};
            return Math.min(ageLA, ageSpouse) + arr[ageDiff];
        }
    }

    public double getSumAssuredADBRider() {
        if (smarthumsafarbean.getIsApplicableForADBRider()
                && (smarthumsafarbean.getApplicableFor().equalsIgnoreCase(
                "Life to be Assured") || smarthumsafarbean
                .getApplicableFor().equalsIgnoreCase(
                        "Both Lives Assured")))
            return smarthumsafarbean.getADBsumAssuredLA();
        else if (smarthumsafarbean.getIsApplicableForADBRider()
                && (smarthumsafarbean.getApplicableFor().equalsIgnoreCase(
                "Spouse Life to be Assured") || smarthumsafarbean
                .getApplicableFor().equalsIgnoreCase(
                        "Both Lives Assured")))
            return smarthumsafarbean.getADBsumAssuredSpouse();
        else
            return 0;
    }

    public void setisRiderEligible() {
        if (smarthumsafarbean.getIsApplicableForADBRider() == true) {
            if (smarthumsafarbean.getAgeLA() >= 18
                    && smarthumsafarbean.getAgeLA() <= 50)
                this.isRiderEligible = true;
            else
                this.isRiderEligible = false;
        } else
            this.isRiderEligible = false;
    }

    private double getADBRider() {
        setisRiderEligible();
        if (isRiderEligible)
            return 50;
        else
            return 0;
    }

    private double getTabularRatePremiumADBRider() {
        if (smarthumsafarbean.getIsApplicableForADBRider()
                && (smarthumsafarbean.getApplicableFor().equalsIgnoreCase(
                "Life to be Assured") || smarthumsafarbean
                .getApplicableFor().equalsIgnoreCase(
                        "Both Lives Assured")))
            return getADBRider() / 100;
        else if (smarthumsafarbean.getIsApplicableForADBRider()
                && (smarthumsafarbean.getApplicableFor().equalsIgnoreCase(
                "Spouse Life to be Assured") || smarthumsafarbean
                .getApplicableFor().equalsIgnoreCase(
                        "Both Lives Assured")))
            return getADBRider() / 100;
        else
            return 0;
    }

    private double getStaffRebateADBRider(boolean staff) {
        if ((smarthumsafarbean.getIsApplicableForADBRider() && (smarthumsafarbean
                .getApplicableFor().equalsIgnoreCase("Life to be Assured") || smarthumsafarbean
                .getApplicableFor().equalsIgnoreCase("Both Lives Assured")))
                || (smarthumsafarbean.getIsApplicableForADBRider() && (smarthumsafarbean
                .getApplicableFor().equalsIgnoreCase(
                        "Spouse Life to be Assured") || smarthumsafarbean
                .getApplicableFor().equalsIgnoreCase(
                        "Both Lives Assured")))) {
            // staffRebate
            if (staff) {
                return 0.06;
            } else
                return 0;
        } else
            return 0;
    }

    private double getLoadingFreqADBRider() {
        if ((smarthumsafarbean.getIsApplicableForADBRider() && (smarthumsafarbean
                .getApplicableFor().equalsIgnoreCase("Life to be Assured") || smarthumsafarbean
                .getApplicableFor().equalsIgnoreCase("Both Lives Assured")))
                || (smarthumsafarbean.getIsApplicableForADBRider() && (smarthumsafarbean
                .getApplicableFor().equalsIgnoreCase(
                        "Spouse Life to be Assured") || smarthumsafarbean
                .getApplicableFor().equalsIgnoreCase(
                        "Both Lives Assured")))) {
            if (smarthumsafarbean.getPremFreqMode().equalsIgnoreCase("Yearly"))
                return 1;
            else if (smarthumsafarbean.getPremFreqMode().equalsIgnoreCase(
                    "Half Yearly"))
                return 1.04;
            else if (smarthumsafarbean.getPremFreqMode().equalsIgnoreCase(
                    "Quarterly"))
                return 1.06;
            else
                return 1.068;

        } else
            return 0;
    }

    public void setPremiumAdbRiderLAWithoutRoundup() {
        if (smarthumsafarbean.getIsApplicableForADBRider()
                && (smarthumsafarbean.getApplicableFor().equalsIgnoreCase(
                "Life to be Assured") || smarthumsafarbean
                .getApplicableFor().equalsIgnoreCase(
                        "Both Lives Assured"))) {
            double sumAssuredAdbRiderLA = smarthumsafarbean
                    .getADBsumAssuredLA();
            double staffRebate = getStaffRebateADBRider(smarthumsafarbean
                    .getIsStaffDiscOrNot());
            double tabularRate = getTabularRatePremiumADBRider();
            double SARebate = 0;
            double NSAPRates = 0;
            double EMR = 0;
            double loadingFreq = getLoadingFreqADBRider();
            double premFreqMultiFactor = setPremiumMultiplication(smarthumsafarbean
                    .getPremFreqMode());

            this.premiumAdbRiderLAWithoutRoundup = ""
                    + cfap.getRoundOffLevel2(cfap
                    .getStringWithout_E(((tabularRate
                            * (1.0 - staffRebate) - SARebate + (NSAPRates + EMR))
                            * sumAssuredAdbRiderLA
                            / 1000.0
                            * loadingFreq / premFreqMultiFactor)));
            // System.out.println(" premiumAdbRiderLAWithoutRoundup : "
            // + this.premiumAdbRiderLAWithoutRoundup);

        } else
            this.premiumAdbRiderLAWithoutRoundup = 0 + "";
    }

    public String getPremiumAdbRiderLAWithoutRoundup() {
        //System.out.println("4.45 "+premiumAdbRiderLAWithoutRoundup);
        return this.premiumAdbRiderLAWithoutRoundup;
    }

    public String getPremiumAdbRiderLAWithRoundup() {

        premiumAdbRiderLAWithoutRound = cfap.getRound(cfap.getStringWithout_E(Double.parseDouble(premiumAdbRiderLAWithoutRoundup)));
        //	System.out.println("premiumAdbRiderLAWithRoundup "+premiumAdbRiderLAWithRoundup);
        return this.premiumAdbRiderLAWithoutRound;
    }

    public void setPremiumAdbRiderSpouseWithoutRoundup() {
        if (smarthumsafarbean.getIsApplicableForADBRider()
                && (smarthumsafarbean.getApplicableFor().equalsIgnoreCase(
                "Spouse Life to be Assured") || smarthumsafarbean
                .getApplicableFor().equalsIgnoreCase(
                        "Both Lives Assured"))) {
            double sumAssuredAdbRiderSpouse = smarthumsafarbean
                    .getADBsumAssuredSpouse();
            double staffRebate = getStaffRebateADBRider(smarthumsafarbean
                    .getIsStaffDiscOrNot());
            double tabularRate = getTabularRatePremiumADBRider();
            double SARebate = 0;
            double NSAPRates = 0;
            double EMR = 0;
            double loadingFreq = getLoadingFreqADBRider();
            double premFreqMultiFactor = setPremiumMultiplication(smarthumsafarbean
                    .getPremFreqMode());

            this.premiumAdbRiderSpouseWithoutRoundup = ""
                    + cfap.getRoundOffLevel2(cfap
                    .getStringWithout_E(((tabularRate
                            * (1.0 - staffRebate) - SARebate + (NSAPRates + EMR))
                            * sumAssuredAdbRiderSpouse
                            / 1000.0
                            * loadingFreq / premFreqMultiFactor)));
            // System.out.println(" premiumAdbRiderSpouseWithoutRoundup : "
            // + this.premiumAdbRiderSpouseWithoutRoundup);

        } else
            this.premiumAdbRiderSpouseWithoutRoundup = 0 + "";
    }

    public String getPremiumAdbRiderSpouseWithoutRoundup() {
        //	System.out.println("premiumAdbRiderSpouseWithoutRoundup "+premiumAdbRiderSpouseWithoutRoundup);
        return this.premiumAdbRiderSpouseWithoutRoundup;
    }

    //added by sujata on 27-02-2020
    public String getPremiumAdbRiderSpouseWithRound() {
        premiumAdbRiderSpouseWithoutRound = cfap.getRound(cfap.getStringWithout_E(Double.parseDouble(premiumAdbRiderSpouseWithoutRoundup)));
        return this.premiumAdbRiderSpouseWithoutRound;
    }

    //added by sujata
    public void setInstbasicrider() {
        double rider = 0;
        //rider =  (Double.parseDouble(getPremiumAdbRiderSpouseWithRound())+Double.parseDouble(getPremiumAdbRiderLAWithRoundup()));
        basicrider = cfap.getRound(cfap.getStringWithout_E(Double.parseDouble(premiumAdbRiderSpouseWithoutRoundup) + (Double.parseDouble(premiumAdbRiderLAWithoutRoundup))));
    }

    public String getInstbasicrider() {


        return this.basicrider;
    }


    //added by sujata on 29-02-2020
    public void setInstbasicriderWITHOUTRoundup() {
        double rider = 0;
        //rider =  (Double.parseDouble(getPremiumAdbRiderSpouseWithRound())+Double.parseDouble(getPremiumAdbRiderLAWithRoundup()));
        InstbasicriderWITHOUTRoundup = (cfap.getStringWithout_E(Double.parseDouble(premiumAdbRiderSpouseWithoutRoundup) + (Double.parseDouble(premiumAdbRiderLAWithoutRoundup))));
    }

    public String getInstbasicriderWITHOUTRoundup() {


        return this.InstbasicriderWITHOUTRoundup;
    }
    //end
//public String getPremiumAdbRiderLAWithRoundup() {
//
//		premiumAdbRiderLAWithoutRoundup = cfap.getRound(cfap.getStringWithout_E(Double.parseDouble(premiumAdbRiderLAWithoutRoundup)));
//	//	System.out.println("premiumAdbRiderLAWithRoundup "+premiumAdbRiderLAWithRoundup);
//		return this.premiumAdbRiderLAWithoutRoundup;
//	}

    public void setBasicPremiumWithRiderWithRoundUP() {
        this.basePremiumWithRiderWithRoundUP = cfap
                .getRound(cfap.getStringWithout_E(Double
                        .parseDouble(getPremiumWithoutSTwithoutRoundup())
                        + Double.parseDouble(getPremiumAdbRiderLAWithoutRoundup())
                        + Double.parseDouble(getPremiumAdbRiderSpouseWithoutRoundup())));

        System.out.println("basePremiumWithRiderWithRoundUP " + basePremiumWithRiderWithRoundUP);
    }

    //added by sujata on 28-02-2020
    public String getBasicPremiumWithRiderWithRoundUP() {
        return this.basePremiumWithRiderWithRoundUP;
    }

    public void setTotalBasePremiumPaid(int year_F) {
        if (year_F <= smarthumsafarbean.getPolicyTerm_Basic())
            this.totalBasePremiumPaid = String
                    .valueOf((Double//getPremiumWithoutSTwithRoundupBasicPrem
                            //		.parseDouble(getPremiumWithoutSTwithRoundup()) * setPremiumMultiplication(smarthumsafarbean
                            .parseDouble(getPremiumWithoutSTwithRoundupBasicPrem()) * setPremiumMultiplication(smarthumsafarbean
                            .getPremFreqMode())));
        else
            this.totalBasePremiumPaid = String.valueOf(0);
    }

    public String getTotalBasePremiumPaid() {
        //System.out.println("\n totalBasePremiumPaid "+totalBasePremiumPaid);
        return this.totalBasePremiumPaid;
    }

//	public void setGuaranteedBenefitFirstDeath(double sumTotalBasePrem,
//			int year_F) {
//		if (year_F <= smarthumsafarbean.getPolicyTerm_Basic()) {
//			double temp = getEquivalentAge(smarthumsafarbean.getAgeLA(),
//					smarthumsafarbean.getAgeSpouse()) < 45 ? 10 : 7;
//
//			this.guaranteedDeathBenefitFirstDeath = Math
//					.round(Math.max(
//							(Math.max(smarthumsafarbean.getsumAssured(),
//									1.05 * sumTotalBasePrem)),
//							Double.parseDouble(getPremiumWithoutSTwithRoundup())
//									* temp))
//					+ "";
//
//		} else
//			this.guaranteedDeathBenefitFirstDeath = 0 + "";
//	}


    public void setGuaranteedBenefitFirstDeath(double sumTotalBasePrem, double PremiumwithoutModelLoading,
                                               int year_F) {
        if (year_F <= smarthumsafarbean.getPolicyTerm_Basic()) {
//			double temp1,temp2,temp3;
//			temp1=10*freqloading;
//			temp2=

            this.guaranteedDeathBenefitFirstDeath = Math
                    .round(Math.max((Math.max(smarthumsafarbean.getsumAssured(), PremiumwithoutModelLoading * 10)), 1.05 * sumTotalBasePrem))
                    + "";

        } else
            this.guaranteedDeathBenefitFirstDeath = 0 + "";

        //System.out.println("sumTotalBasePrem "+sumTotalBasePrem);
    }

    public String getGuaranteedBenefitFirstDeath() {
        //System.out.println("guaranteedDeathBenefitFirstDeath "+guaranteedDeathBenefitFirstDeath);
        return this.guaranteedDeathBenefitFirstDeath;
    }

    //added by sujata on 25-02-2020
//	public void setGuaranteedBenefitSecondDeath(double sumTotalBasePrem,int year_F) {
//
//		/**************/
//		if (year_F <= smarthumsafarbean.getPolicyTerm_Basic()) {
//			double temp = getEquivalentAge(smarthumsafarbean.getAgeLA(),
//					smarthumsafarbean.getAgeSpouse()) < 45 ? 10 : 7;
//
//			this.guaranteedDeathBenefitSecondDeath = Math
//					.round((Math.max(smarthumsafarbean.getsumAssured(),Double.parseDouble(getPremiumWithoutSTwithRoundup())* temp)))
//					+ "";
//
//		} else
//			this.guaranteedDeathBenefitSecondDeath = 0 + "";
//		/*****************/
//
//		//this.guaranteedDeathBenefitSecondDeath = getGuaranteedBenefitFirstDeath();
//	}

    public void setGuaranteedBenefitSecondDeath(double sumTotalBasePrem, double PremiumwithoutModelLoading, int year_F) {

        /**************/
        if (year_F <= smarthumsafarbean.getPolicyTerm_Basic()) {


            this.guaranteedDeathBenefitSecondDeath = Math
                    .round((Math.max(smarthumsafarbean.getsumAssured(), PremiumwithoutModelLoading * 10)))
                    + "";

        } else
            this.guaranteedDeathBenefitSecondDeath = 0 + "";
        /*****************/

        //this.guaranteedDeathBenefitSecondDeath = getGuaranteedBenefitFirstDeath();
    }

    public String getGuaranteedBenefitSecondDeath() {
        //System.out.println("guaranteedDeathBenefitSecondDeath "+guaranteedDeathBenefitSecondDeath);
        return this.guaranteedDeathBenefitSecondDeath;
    }

    //end//

    public void setNonGuarateedDeathBenefitAt_4_Illustration(int year_F) {
        double a, b;
        if (year_F <= smarthumsafarbean.getPolicyTerm_Basic()) {
            if (year_F <= 3)
                a = year_F * smarthumsafarbean.getsumAssured() * 0.025;
            else
                a = (smarthumsafarbean.getsumAssured() * getbonusrate4(smarthumsafarbean
                        .getPolicyTerm_Basic()))
                        + Double.parseDouble(this.nonGuarateedDeathBenefitAt_4_illustration);

        } else
            a = 0;
        if (year_F == smarthumsafarbean.getPolicyTerm_Basic())
            b = a * (prop.terminalBonus + 1);
        else
            b = a * 1;

        this.nonGuarateedDeathBenefitAt_4_illustration = b + "";
    }

    private String getNonGuarateedDeathBenefitAt_4_Illustration() {
        return nonGuarateedDeathBenefitAt_4_illustration;
    }

    private void setNonGuarateedDeathBenefitAt_8_Illustration(int year_F) {
        double a, b;
        if (year_F <= smarthumsafarbean.getPolicyTerm_Basic())
            a = year_F * smarthumsafarbean.getsumAssured()
                    * getbonusrate8(smarthumsafarbean.getPolicyTerm_Basic());
        else
            a = 0;
        if (year_F == smarthumsafarbean.getPolicyTerm_Basic())
            b = a * (prop.terminalBonus + 1);
        else
            b = a * 1;

        this.nonGuarateedDeathBenefitAt_8_illustration = b + "";
    }

    private String getNonGuarateedDeathBenefitAt_8_Illustration() {
        return nonGuarateedDeathBenefitAt_8_illustration;
    }

    private double getbonusrate4(int policyterm) {
        double bonusrate4 = 0;

        if (policyterm >= 10 && policyterm <= 14) {
            bonusrate4 = 0.016;
        } else if (policyterm >= 15 && policyterm <= 19) {
            bonusrate4 = 0.015;
        } else if (policyterm >= 20 && policyterm <= 24) {
            bonusrate4 = 0.014;
        } else if (policyterm >= 25) {
            bonusrate4 = 0.013;
        }
        return bonusrate4;

    }

    private double getbonusrate8(int policyterm) {
        double bonusrate8 = 0;

        if (policyterm >= 10 && policyterm <= 14) {
            bonusrate8 = 0.035;
        } else if (policyterm >= 15 && policyterm <= 19) {
            bonusrate8 = 0.037;
        } else if (policyterm >= 20 && policyterm <= 24) {
            bonusrate8 = 0.04;
        } else if (policyterm >= 25) {
            bonusrate8 = 0.044;
        }
        return bonusrate8;

    }

    // Non-Guaranteed Benefit payable on death at 4%
    public void setNonGuarateedDeathBenefitAt_4_Percent(int year_F) {
        setNonGuarateedDeathBenefitAt_4_Illustration(year_F);
        this.nonGuarateedDeathBenefitAt_4_Percent = (Double
                .parseDouble(getNonGuarateedDeathBenefitAt_4_Illustration()))
                + "";
    }

    public String getNonGuarateedDeathBenefitAt_4_Percent() {
        return nonGuarateedDeathBenefitAt_4_Percent;
    }

    /******************sujata 25-02-2020***********/
    //25-02-2020
    public void setNonGuarateedDeathBenefitAt_4_PercentFinal(int year_F) {
        double a, b;
        if (year_F <= smarthumsafarbean.getPolicyTerm_Basic()) {
            if (year_F <= 3)
                a = year_F * smarthumsafarbean.getsumAssured() * 0.025;
            else
                a = (smarthumsafarbean.getsumAssured() * getbonusrate4(smarthumsafarbean
                        .getPolicyTerm_Basic()))
                        + Double.parseDouble(this.nonGuarateedDeathBenefitAt_4_PercentFinal);

        } else
            a = 0;


        this.nonGuarateedDeathBenefitAt_4_PercentFinal = a + "";


    }

    //
    public String getNonGuarateedDeathBenefitAt_4_PercentFinal() {
        //	System.out.println("nonGuarateedDeathBenefitAt_4_PercentFinal "+nonGuarateedDeathBenefitAt_4_PercentFinal);
        return nonGuarateedDeathBenefitAt_4_PercentFinal;
    }


    public void setNonGuarateedDeathBenefitAt_8_PercentFinal(int year_F) {
        double a, b;
        if (year_F <= smarthumsafarbean.getPolicyTerm_Basic())
            a = year_F * smarthumsafarbean.getsumAssured()
                    * getbonusrate8(smarthumsafarbean.getPolicyTerm_Basic());
        else
            a = 0;
        if (year_F == smarthumsafarbean.getPolicyTerm_Basic())
            b = a * (prop.terminalBonus + 1);
        else
            b = a * 1;


        this.nonGuarateedDeathBenefitAt_8_PercentFinal = a + "";


    }

    //
    public String getNonGuarateedDeathBenefitAt_8_PercentFinal() {
        //	System.out.println("nonGuarateedDeathBenefitAt_8_PercentFinal "+nonGuarateedDeathBenefitAt_8_PercentFinal);
        return nonGuarateedDeathBenefitAt_8_PercentFinal;
    }


    /*****************end*********************/

    // Non-Guaranteed Benefit payable on death at 8%
    public void setNonGuarateedDeathBenefitAt_8_Percent(int year_F) {
        setNonGuarateedDeathBenefitAt_8_Illustration(year_F);

        if (year_F == 30)
            this.nonGuarateedDeathBenefitAt_8_Percent = (Double
                    .parseDouble(getNonGuarateedDeathBenefitAt_8_Illustration()) + smarthumsafarbean
                    .getsumAssured())
                    + "";
        else
            this.nonGuarateedDeathBenefitAt_8_Percent = (Double
                    .parseDouble(getNonGuarateedDeathBenefitAt_8_Illustration()))
                    + "";

    }

    public String getNonGuarateedDeathBenefitAt_8_Percent() {
        return nonGuarateedDeathBenefitAt_8_Percent;
    }

    public void setGuaranteedSurvivalBenefit(int year_f) {
        if (year_f == smarthumsafarbean.getPolicyTerm_Basic()) {
            this.guaranteedSurvivalBenefit = smarthumsafarbean.getsumAssured()
                    + "";
        } else
            this.guaranteedSurvivalBenefit = 0 + "";
    }

    public String getGuaranteedSurvivalBenefit() {
        return guaranteedSurvivalBenefit;
    }

    private void setNonGuaranSurBenAt_4_Illustratn(int year_f) {

        if (year_f == smarthumsafarbean.getPolicyTerm_Basic()) {
            this.nonGuaranSurBenAt_4_Illustratn = this.nonGuarateedDeathBenefitAt_4_illustration;
        } else

            this.nonGuaranSurBenAt_4_Illustratn = 0 + "";
    }

    private String getNonGuaranSurBenAt_4_Illustratn() {
        return nonGuaranSurBenAt_4_Illustratn;
    }

    public void setNonGuarateedSurvivalBenefitAt_4_Percent(int year_f) {
        this.setNonGuaranSurBenAt_4_Illustratn(year_f);
        if (year_f < smarthumsafarbean.getPolicyTerm_Basic()) {
            this.nonGuarateedSurvivalBenefitAt_4_Percent = this
                    .getNonGuaranSurBenAt_4_Illustratn();
        } else if (year_f == smarthumsafarbean.getPolicyTerm_Basic())

            this.nonGuarateedSurvivalBenefitAt_4_Percent = (Double
                    .parseDouble(getNonGuaranSurBenAt_4_Illustratn())) + "";

        else
            this.nonGuarateedSurvivalBenefitAt_4_Percent = 0 + "";
    }

    public String getNonGuarateedSurvivalBenefitAt_4_Percent() {
        return nonGuarateedSurvivalBenefitAt_4_Percent;
    }

    private void setNonGuaranSurBenAt_8_Illustratn(int year_f) {

        if (year_f == smarthumsafarbean.getPolicyTerm_Basic()) {
            this.nonGuaranSurBenAt_8_Illustratn = this.nonGuarateedDeathBenefitAt_8_illustration;
        } else

            this.nonGuaranSurBenAt_8_Illustratn = 0 + "";

    }

    private String getNonGuaranSurBenAt_8_Illustratn() {
        return nonGuaranSurBenAt_8_Illustratn;
    }

    public void setNonGuarateedSurvivalBenefitAt_8_Percent(int year_f) {
        this.setNonGuaranSurBenAt_8_Illustratn(year_f);
        if (year_f < smarthumsafarbean.getPolicyTerm_Basic()) {
            this.nonGuarateedSurvivalBenefitAt_8_Percent = this
                    .getNonGuaranSurBenAt_8_Illustratn();
        } else if (year_f == smarthumsafarbean.getPolicyTerm_Basic())

            this.nonGuarateedSurvivalBenefitAt_8_Percent = (Double
                    .parseDouble(this.getNonGuaranSurBenAt_8_Illustratn()))
                    + "";

        else
            this.nonGuarateedSurvivalBenefitAt_8_Percent = 0 + "";
    }

    public String getNonGuarateedSurvivalBenefitAt_8_Percent() {
        return nonGuarateedSurvivalBenefitAt_8_Percent;
    }

//	public void setGSV_SurrenderValue(int year_F, double cummulativePremWitOtST) {
//
//		double a = 0, b = 0;
//
////		if(year_F <= smarthumsafarbean.getPolicyTerm_Basic())
////		{
//		double[] regularGSV = objSmarthumsafarDB.getGSV_Rates();
//		int arrCount = 0;
//
//		for (int i = 1; i <= 30; i++)
//		{
//			for (int j = 10; j <= 30; i++)
//			{
//			if (i == year_F && j==smarthumsafarbean.getPolicyTerm_Basic())
//			{
//				// System.out.println(i +" "+ j);
//				a = regularGSV[arrCount];
//				break;
//			}
//			arrCount++;
//			}
//			// System.out.println(i +" "+ j +" = "+prStrArr[arrCount]);
//
//		}
//		System.out.println(" GSV b = " + a);
//
//		if (smarthumsafarbean.getPolicyTerm_Basic() > 9) {
//			if (year_F > 2)
//				b = 1;
//			else
//				b = 0;
//		} else {
//			if (year_F > 1)
//				b = 1;
//			else
//				b = 0;
//		}
//
//		this.GSV_surrendr_val = Math.round(a * b * cummulativePremWitOtST) + "";
////		}
////		else
////		{
////			this.GSV_surrendr_val= "";
////		}
//
////		 System.out.println(year_F + "  a : " + a + " b : " + b + "         "
////		 + this.GSV_surrendr_val);
//	}


    public void setGSV_SurrenderValue(int year_F, double cummulativePremWitOtST) {
        double a = 0, b = 0;

        if (year_F <= smarthumsafarbean.getPolicyTerm_Basic()) {
            double[] regularGSV = objSmarthumsafarDB.getGSV_Rates();
            //	double[] SingleGSV = objSmarthumsafarDB.get
            int arrCount = 0;

            for (int i = 1; i <= 30; i++) {
                for (int j = 10; j <= 30; j++) {
                    if (i == year_F && j == smarthumsafarbean.getPolicyTerm_Basic()) {
                        // System.out.println(i +" "+ j);
                        a = regularGSV[arrCount];
                        break;
                    }
                    arrCount++;
                }
                // System.out.println(i +" "+ j +" = "+prStrArr[arrCount]);

            }
            // System.out.println(" GSV b = " + a);
//		System.out.println("cummulativePremWitOtST "+cummulativePremWitOtST);

            if (smarthumsafarbean.getPolicyTerm_Basic() > 9) {
                if (year_F >= 2)
                    b = 1;
                else
                    b = 0;
            } else {
                if (year_F > 1)
                    b = 1;
                else
                    b = 0;
            }


            this.GSV_surrendr_val = (a * b * cummulativePremWitOtST) + "";
        } else {
            this.GSV_surrendr_val = "";
        }
        //System.out.println("b "+b);

    }


    public String getGSV_SurrenderValue() {
        //System.out.println("\n GSV_surrendr_val "+GSV_surrendr_val);
        return this.GSV_surrendr_val;
    }


    //added by sujata on 26-02-2020

    public void setNonGSV_surrndr_val_4_Percent(int year_F) {
        double a = 0, b = 0, temp = 0;
        if (year_F == smarthumsafarbean.getPolicyTerm_Basic())
            a = prop.terminalBonus + 1;
        else
            a = 1;

        double[] SSV = objSmarthumsafarDB.getSSV_Rates();
        int arrCount = 0;
        for (int i = 1; i <= 30; i++) {
            for (int j = 10; j <= 30; j++) {
                if ((i == year_F && smarthumsafarbean.getPolicyTerm_Basic() == j)) {
                    // System.out.println(i +" "+ j);
                    b = SSV[arrCount];
                    break;
                }
                arrCount++;
                // System.out.println(i +" "+ j +" = "+b);
            }

        }
        temp = (((double) year_F / smarthumsafarbean.getPolicyTerm_Basic())
                * smarthumsafarbean.getsumAssured() + Double.parseDouble(this
                .getNonGuarateedDeathBenefitAt_4_Illustration()) / a);


        this.NonGSV_surrndr_val_4_Percent = (cfap.getRound(cfap
                .getRoundOffLevel2(cfap.getStringWithout_E(temp * b))));

//		System.out.println("temp "+ (cfap.getRound(cfap
//				.getRoundOffLevel2(cfap.getStringWithout_E(temp * b)))));

//		System.out.println("abcd "+ (((double) year_F / smarthumsafarbean.getPolicyTerm_Basic())
//				* smarthumsafarbean.getsumAssured() + Double.parseDouble(this
//				.getNonGuarateedDeathBenefitAt_4_Illustration()) / a));


    }
//end

    public String getNonGSV_surrndr_val_4_Percent() {
        //System.out.println("NonGSV_surrndr_val_4_Percent "+NonGSV_surrndr_val_4_Percent);
        return this.NonGSV_surrndr_val_4_Percent;
    }

    public void setNonGSV_surrndr_val_8_Percent(int year_F) {
        double a = 0, b = 0, temp = 0;
        if (year_F == smarthumsafarbean.getPolicyTerm_Basic())
            a = prop.terminalBonus + 1;
        else
            a = 1;

        double[] SSV = objSmarthumsafarDB.getSSV_Rates();
        int arrCount = 0;
        for (int i = 1; i <= 30; i++) {
            for (int j = 10; j <= 30; j++) {
                if ((i == year_F && smarthumsafarbean.getPolicyTerm_Basic() == j)) {
                    // System.out.println(i +" "+ j);
                    b = SSV[arrCount];
                    break;
                }
                arrCount++;
                // System.out.println(i +" "+ j +" = "+b);
            }

        }

        temp = (((double) year_F / smarthumsafarbean.getPolicyTerm_Basic())
                * smarthumsafarbean.getsumAssured() + Double.parseDouble(this
                .getNonGuarateedDeathBenefitAt_8_Illustration()) / a);
        this.NonGSV_surrndr_val_8_Percent = (cfap.getRound(cfap
                .getRoundOffLevel2(cfap.getStringWithout_E(temp * b))));

    }

    public String getNonGSV_surrndr_val_8_Percent() {
        //System.out.println("NonGSV_surrndr_val_8_Percent "+NonGSV_surrndr_val_8_Percent);
        return this.NonGSV_surrndr_val_8_Percent;
    }

    // added by vrushali on 09 Nov 2015 start
    public void setPremiumAdbRiderLAWithoutDiscWithoutRoundup() {
        if (smarthumsafarbean.getIsApplicableForADBRider() == true
                && (smarthumsafarbean.getApplicableFor().equalsIgnoreCase(
                "Life to be Assured") || smarthumsafarbean
                .getApplicableFor().equalsIgnoreCase(
                        "Both Lives Assured"))) {
            double sumAssuredAdbRiderLA = smarthumsafarbean
                    .getADBsumAssuredLA();
            double staffRebate = getStaffRebateADBRider(smarthumsafarbean
                    .getIsStaffDiscOrNot());
            double tabularRate = getTabularRatePremiumADBRider();
            double SARebate = 0;
            double NSAPRates = 0;
            double EMR = 0;
            double loadingFreq = getLoadingFreqADBRider();
            double premFreqMultiFactor = setPremiumMultiplication(smarthumsafarbean
                    .getPremFreqMode());

            this.premiumAdbRiderLAWithoutDiscWithoutRoundup = ""
                    + cfap.getRoundOffLevel2(cfap
                    .getStringWithout_E(((tabularRate * (1.0 - 0)
                            - SARebate + (NSAPRates + EMR))
                            * sumAssuredAdbRiderLA
                            / 1000.0
                            * loadingFreq / premFreqMultiFactor)));
            // System.out.println(" premiumAdbRiderLAWithoutDiscWithoutRoundup : "
            // + this.premiumAdbRiderLAWithoutDiscWithoutRoundup);

        } else
            this.premiumAdbRiderLAWithoutDiscWithoutRoundup = 0 + "";
    }

    public String getPremiumAdbRiderLAWithoutDiscWithoutRoundup() {
        return this.premiumAdbRiderLAWithoutDiscWithoutRoundup;
    }

    public void setPremiumWithoutSTWithoutDiscwithoutRoundup() {
        // double
        // staffRebate=getStaffRebate(smarthumsafarbean.getIsStaffDiscOrNot());
        double tabularRate = getTabularRatePremium();
        double SARebate = getSARebate(smarthumsafarbean.getsumAssured());
        double NSAPRates = 0;
        double EMR = 0;
        double loadingFreq = getLoadingFrqOfPremium(
                smarthumsafarbean.getPremFreqMode(), "Basic");
        /**** Added By Priyanka Warekar - 08-08-2016 - Start ****/
        // double premFreqMultiFactor =
        // setPremiumMultiplication(smarthumsafarbean
        // .getPremFreqMode());
        double premFreqMultiFactor = 1;
        /**** Added By Priyanka Warekar - 08-08-2016 - Start ****/
        // System.out.println(tabularRate + "  " + SARebate + "   " + NSAPRates
        // + "  " + EMR + "  " + loadingFreq + "  " + premFreqMultiFactor);
        this.premiumWithoutStWithoutDiscwithotRoundup = ""
                + ((tabularRate * (1.0 - 0) - SARebate + (NSAPRates + EMR))
                * smarthumsafarbean.getsumAssured() / 1000.0
                * loadingFreq / premFreqMultiFactor);
        // System.out.println(" premiumWithoutStwithotRoundup : "
        // + this.premiumWithoutStwithotRoundup);
    }

    public String getPremiumWithoutSTWithoutDiscwithoutRoundup() {
        return this.premiumWithoutStWithoutDiscwithotRoundup;
    }

    public void setPremiumWithoutSTWithoutDiscwithRoundup() {
        this.premiumWithoutStWithoutDiscwtRoundup = cfap
                .getRoundUp(cfap.getStringWithout_E(Double
                        .parseDouble(getPremiumWithoutSTWithoutDiscwithoutRoundup())));
    }

    public String getPremiumWithoutSTWithoutDiscwithRoundup() {
        return this.premiumWithoutStWithoutDiscwtRoundup;
    }

    // added by vrushali
    public void setBasicPremiumWithoutRoundup() {
        // double
        // staffRebate=getStaffRebate(smarthumsafarbean.getIsStaffDiscOrNot());
        double tabularRate = getTabularRatePremium();

        this.basicPremiumWithoutRoundup = ""
                + (tabularRate * smarthumsafarbean.getsumAssured() / 1000.0);
        // System.out.println(" basicPremiumWithoutRoundup : "
        // + this.basicPremiumWithoutRoundup);
    }

    public String getBasicPremiumWithoutRoundup() {
        return this.basicPremiumWithoutRoundup;
    }

    public void setBasicPremiumwithRoundup() {
        this.basicPremiumWithRoundup = cfap.getRoundUp(cfap
                .getStringWithout_E(Double
                        .parseDouble(getBasicPremiumWithoutRoundup())));
    }

    public String getBasicPremiumwithRoundup() {
        return this.basicPremiumWithRoundup;
    }

    public void setPremiumAdbRiderSpouseWithoutDiscWithoutRoundup() {
        if (smarthumsafarbean.getIsApplicableForADBRider()
                && (smarthumsafarbean.getApplicableFor().equalsIgnoreCase(
                "Spouse Life to be Assured") || smarthumsafarbean
                .getApplicableFor().equalsIgnoreCase(
                        "Both Lives Assured"))) {
            double sumAssuredAdbRiderSpouse = smarthumsafarbean
                    .getADBsumAssuredSpouse();
            double staffRebate = getStaffRebateADBRider(smarthumsafarbean
                    .getIsStaffDiscOrNot());
            double tabularRate = getTabularRatePremiumADBRider();
            double SARebate = 0;
            double NSAPRates = 0;
            double EMR = 0;
            double loadingFreq = getLoadingFreqADBRider();
            double premFreqMultiFactor = setPremiumMultiplication(smarthumsafarbean
                    .getPremFreqMode());

            this.premiumAdbRiderSpouseWithoutDiscWithoutRoundup = ""
                    + cfap.getRoundOffLevel2(cfap
                    .getStringWithout_E(((tabularRate * (1.0 - 0)
                            - SARebate + (NSAPRates + EMR))
                            * sumAssuredAdbRiderSpouse
                            / 1000.0
                            * loadingFreq / premFreqMultiFactor)));
            // System.out.println(" premiumAdbRiderSpouseWithoutRoundup : "
            // + this.premiumAdbRiderSpouseWithoutRoundup);

        } else
            this.premiumAdbRiderSpouseWithoutDiscWithoutRoundup = 0 + "";
    }

    public String getPremiumAdbRiderSpouseWithoutDiscWithoutRoundup() {
        return this.premiumAdbRiderSpouseWithoutDiscWithoutRoundup;
    }

    public void setBasicPremiumAdbRiderLAWithoutRoundup() {
        if (smarthumsafarbean.getIsApplicableForADBRider()
                && (smarthumsafarbean.getApplicableFor().equalsIgnoreCase(
                "Life to be Assured") || smarthumsafarbean
                .getApplicableFor().equalsIgnoreCase(
                        "Both Lives Assured"))) {
            double sumAssuredAdbRiderLA = smarthumsafarbean
                    .getADBsumAssuredLA();

            double tabularRate = getTabularRatePremiumADBRider();

            this.basicPremiumAdbRiderLAWithoutRoundup = ""
                    + cfap.getRoundOffLevel2(cfap
                    .getStringWithout_E(((tabularRate
                            * sumAssuredAdbRiderLA / 1000.0))));
            // System.out.println(" basicPremiumAdbRiderLAWithoutRoundup : "
            // + this.basicPremiumAdbRiderLAWithoutRoundup);

        } else
            this.basicPremiumAdbRiderLAWithoutRoundup = 0 + "";
    }

    public String getBasicPremiumAdbRiderLAWithoutRoundup() {
        return this.basicPremiumAdbRiderLAWithoutRoundup;
    }

    public void setBasicPremiumAdbRiderSpouseWithoutRoundup() {
        if (smarthumsafarbean.getIsApplicableForADBRider()
                && (smarthumsafarbean.getApplicableFor().equalsIgnoreCase(
                "Life to be Assured") || smarthumsafarbean
                .getApplicableFor().equalsIgnoreCase(
                        "Both Lives Assured"))) {
            double sumAssuredAdbRiderSpouse = smarthumsafarbean
                    .getADBsumAssuredSpouse();

            double tabularRate = getTabularRatePremiumADBRider();

            this.basicPremiumAdbRiderSpouseWithoutRoundup = ""
                    + cfap.getRoundOffLevel2(cfap
                    .getStringWithout_E(((tabularRate
                            * sumAssuredAdbRiderSpouse / 1000.0))));
            // System.out.println(" basicPremiumAdbRiderSpouseWithoutRoundup : "
            // + this.basicPremiumAdbRiderSpouseWithoutRoundup);

        } else
            this.basicPremiumAdbRiderSpouseWithoutRoundup = 0 + "";
    }

    public String getBasicPremiumAdbRiderSpouseWithoutRoundup() {
        return this.basicPremiumAdbRiderSpouseWithoutRoundup;
    }

    public void setBasicPremiumWithoutDiscWithRiderWithRoundUP() {
        this.basePremiumWithoutDiscWithRiderWithRoundUP = cfap
                .getRoundUp(cfap.getStringWithout_E(Double
                        .parseDouble(getPremiumWithoutSTWithoutDiscwithoutRoundup())
                        + Double.parseDouble(getPremiumAdbRiderLAWithoutDiscWithoutRoundup())
                        + Double.parseDouble(getPremiumAdbRiderSpouseWithoutDiscWithoutRoundup())));
    }

    public String getBasicPremiumWithoutDiscWithRiderWithRoundUP() {
        return this.basePremiumWithoutDiscWithRiderWithRoundUP;
    }

    /********** Added by Vrushali on 19-Nov-2015 **************/
    public double getMinesOccuInterest(double SumAssured_Basic) {
        return (SumAssured_Basic / 1000) * 2;
    }

    // public double getBackDateInterest(double grossPremium, String bkDate) {
    //
    // try {
    //
    // // double indigoRate=7.5;
    // /**
    // * Modified by Akshaya. Rate Change from 1-APR-2015
    // */
    //
    // /******************* Modified by Akshaya on 14-APR-2015 start **********/
    // double indigoRate = 10;
    // double SeriviceTax = 0;
    // if (smarthumsafarbean.getIsJKResidentDiscOrNot())
    // SeriviceTax = prop.serviceTaxJKResident;
    // else
    // SeriviceTax = prop.serviceTax;
    //
    // double ServiceTaxValue = (SeriviceTax + 1) * 100;
    //
    // /******************* Modified by Akshaya on 14-APR-2015 end **********/
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
    // /******************* Modified by Akshaya on 14-APR-2015 start **********/
    // // double netPremWithoutST=Math.round((grossPremium*100)/103.09);
    // double netPremWithoutST = Math.round((grossPremium * 100)
    // / ServiceTaxValue);
    // /******************* Modified by Akshaya on 14-APR-2015 end **********/
    // // System.out.println("netPremWithoutST "+netPremWithoutST);
    // int days = cfap.setDate(date, strBackDate);
    // // System.out.println("no of days "+days);
    // double monthsBetween = cfap.roundDown((double) days / 365 * 12, 0);
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

    public double getCompoundAmount(double monthsBetween,
                                    double netPremWithoutST, double indigoRate, int compoundFreq) {
        double compoundAmount = netPremWithoutST
                * Math.pow((1 + (indigoRate / (100 * compoundFreq))),
                (compoundFreq * (monthsBetween / 12)))
                - netPremWithoutST;
        return compoundAmount;
        // System.out.println("compoundAmount "+compoundAmount);
    } // End

    /********** Added by Vrushali on 19-Nov-2015 **************/

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
            if (smarthumsafarbean.getIsJKResidentDiscOrNot())
                SeriviceTax = prop.serviceTaxJKResident;
            else
                SeriviceTax = prop.serviceTax + prop.SBCServiceTax + prop.KKCServiceTax;

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

    public double getServiceTaxMines(double MinesOccuInterest) {
        return Double.parseDouble(cfap.getRoundUp(cfap.getRoundOffLevel2(cfap.getStringWithout_E(MinesOccuInterest * (prop.serviceTax + prop.SBCServiceTax)))));
    }

    public void setPremiumWithoutSTFrequencyLoading() {
        double staffRebate = getStaffRebate(smarthumsafarbean
                .getIsStaffDiscOrNot());
        double tabularRate = getTabularRatePremium();
        double SARebate = getSARebate(smarthumsafarbean.getsumAssured());
        double NSAPRates = 0;
        double EMR = 0;
        double loadingFreq = getLoadingFrqOfPremium(
                smarthumsafarbean.getPremFreqMode(), "Basic");

        double premFreqMultiFactor = 1;

        this.premiumWithoutStFrequencyLoading = ""
                + ((tabularRate * (1.0 - staffRebate) - SARebate + (NSAPRates + EMR))
                * smarthumsafarbean.getsumAssured()
                / 1000.0);

    }

    public String getPremiumWithoutSTFrequencyLoading() {
        //	System.out.println("premiumWithoutStFrequencyLoading "+premiumWithoutStFrequencyLoading);
        return this.premiumWithoutStFrequencyLoading;
    }


    public void setannulizedPremFinal(int year_F, int policyterm, String premfreq) {
        if (year_F <= policyterm) {

            //System.out.println("getPremiumWithoutSTFrequencyLoading "+getPremiumWithoutSTFrequencyLoading());
            annulizedpremium = cfap.getRoundUp(cfap.getStringWithout_E(Double.parseDouble(getPremiumWithoutSTFrequencyLoading())));

        }
//				else
//				{
//
//					annulizedpremium=null;
//					double a=Double.parseDouble(annulizedpremium);
//				}

    }

    public String getannulizedPremFinal() {
        //System.out.println("annulizedpremium "+annulizedpremium);
        return this.annulizedpremium;
    }


    public double getTotalMaturityBenefit4percent(int year, int policyterm, double maturityBen, double nongrntdDeathNenft_4Percent) {
        double totalmat4per;
        if (year == policyterm) {
            totalmat4per = maturityBen + nongrntdDeathNenft_4Percent + 0.15 * nongrntdDeathNenft_4Percent;
        } else {
            totalmat4per = 0;
        }
        return totalmat4per;
    }


    public double getTotalMaturityBenefit8percent(int year, int policyterm, double maturityBen, double nongrntdDeathNenft_8Percent) {
        double totalmat4per;
        if (year == policyterm) {
            totalmat4per = maturityBen + nongrntdDeathNenft_8Percent + 0.15 * nongrntdDeathNenft_8Percent;
        } else {
            totalmat4per = 0;
        }
        return totalmat4per;
    }


    public double getTotalDeathBenefit4perecentFirst(int year, int policyterm, double firstyearofdeath4per) {
        double totalDeath4per;
        if (year <= policyterm) {
            totalDeath4per = Double.parseDouble(getGuaranteedBenefitFirstDeath());
        } else {
            totalDeath4per = 0;
        }
        return totalDeath4per;
    }

    public double getTotalDeathBenefit8perecentSecond(int year, int policyterm, double firstyearofdeath4per) {
        double totalDeath8per;
        if (year <= policyterm) {
            totalDeath8per = Double.parseDouble(getGuaranteedBenefitFirstDeath());
        } else {
            totalDeath8per = 0;
        }
        return totalDeath8per;
    }

    public double getTotalDeathBenefit4percent(int year, int policyterm, double guarntdDeathBenft, double nongrntdDeathNenft_4Percent, double sumtotalBasePremiumPaidforSurr) {
        double totaldeathben, temp1, temp2;
        if (year <= policyterm) {
            temp1 = guarntdDeathBenft + nongrntdDeathNenft_4Percent + nongrntdDeathNenft_4Percent * 0.15;
            temp2 = 1.05 * sumtotalBasePremiumPaidforSurr;
            totaldeathben = Math.max(temp1, temp2);
            //	System.out.println("guarntdDeathBenft "+guarntdDeathBenft);
            //System.out.println("sumtotalBasePremiumPaidforSurr "+sumtotalBasePremiumPaidforSurr);
        } else {
            totaldeathben = 0;
        }
        return totaldeathben;
    }

    public double getTotalDeathBenefit8percent(int year, int policyterm, double guarntdDeathBenft, double nongrntdDeathNenft_8Percent, double sumtotalBasePremiumPaidforSurr) {
        double totaldeathben, temp1, temp2;
        if (year <= policyterm) {
            temp1 = guarntdDeathBenft + nongrntdDeathNenft_8Percent + nongrntdDeathNenft_8Percent * 0.15;
            temp2 = 1.05 * sumtotalBasePremiumPaidforSurr;
            totaldeathben = Math.max(temp1, temp2);
        } else {
            totaldeathben = 0;
        }
        return totaldeathben;
    }
    /*************end******************************/

}
