package sbilife.com.pointofsale_bancaagency.smartwomenadvantage;

import sbilife.com.pointofsale_bancaagency.common.CommonForAllProd;

class SmartWomenAdvantageBusinessLogic {

    private SmartWomenAdvantageBean smartwomenadvantagebean;
    private CommonForAllProd comm;
    private SmartWomenAdvantageProperties prop;

    private double paidUpValue = 0;
    private double totalBasePremiumPaid = 0;
    private double guaranteedDeathBenefit = 0;
    private double nonGuarateedDeathBenefitAt_4_Percent = 0;
    private double nonGuarateedDeathBenefitAt_8_Percent = 0;
    private double guaranteedSurvivalBenefit = 0;
    private double nonGuarateedSurvivalBenefitAt_4_Percent = 0;
    private double nonGuarateedSurvivalBenefitAt_8_Percent = 0;
    private double guarateedCriticalIllnessBenefitAt_Minor = 0;
    private double guarateedCriticalIllnessBenefitAt_Major = 0;
    private double guarateedCriticalIllnessBenefitAt_Advanced = 0;
    private double GSV_SurrenderValue = 0;
    private double GSV_SurrenderValue_4_Percent = 0;
    private double GSV_SurrenderValue_8_Percent = 0;
    private double SAMFguaranteedDeathBenefit = 0;

    public SmartWomenAdvantageBusinessLogic(
            SmartWomenAdvantageBean smartwomenadvantagebean) {
        // TODO Auto-generated constructor stub

        this.smartwomenadvantagebean = smartwomenadvantagebean;
        comm = new CommonForAllProd();
        prop = new SmartWomenAdvantageProperties();
    }

    public String setFinalPremiumBasicSA() {
        double basicPremiumWTLoadng = setBasicPremiumWTLoading();
        double result = basicPremiumWTLoadng * (1 - getStaffDisc())
                * getModalFactor();
        // System.out.println(" final Basic Prem : "+result);
        return result + "";
    }

    public String setFinalPremiumBasicCritiIllness() {
        double basicPremiumWTLoadng = (smartwomenadvantagebean
                .getEdt_SumAssured() * getTabularPremiumRate_CI()) / 1000;
        double result = basicPremiumWTLoadng * (1 - getStaffDisc())
                * getModalFactor();
        // System.out.println(" final Basic Prem For Criti Illness: "+result);
        return result + "";
    }

    public String setFinalPremiumBasicAPCnCA() {
        if (smartwomenadvantagebean.getIsAPCnCAoption()) {
            double basicPremiumWTLoadng = (smartwomenadvantagebean
                    .getEdt_APCSumAssured() * getTabularPremiumRate_APC()) / 1000;
            double result = basicPremiumWTLoadng * (1 - getStaffDisc())
                    * getModalFactor();
            // System.out.println(" final Basic Prem For APC n CP: "+result);
            return result + "";
        } else
            return 0 + "";
    }

    private double setBasicPremiumWTLoading() {
        double a = 0;
        double tabularPremiumRate = getTabularPremiumRate();

        if (smartwomenadvantagebean.getEdt_SumAssured() >= 200000
                && smartwomenadvantagebean.getEdt_SumAssured() < 500000)
            a = 0;
        else if (smartwomenadvantagebean.getEdt_SumAssured() >= 500000
                && smartwomenadvantagebean.getEdt_SumAssured() < 700000)
            a = 2.5;
        else
            a = 3;
        double temp = ((smartwomenadvantagebean.getEdt_SumAssured() * (tabularPremiumRate - a)) / 1000);
        // System.out.println(" Basic Prem WT Loading : "+temp);

        return temp;
    }

    public double getStaffDisc() {
        if (smartwomenadvantagebean.getIsStaffDisc())
            return 0.06;
        else
            return 0;
    }

    private double getModalFactor() {
        if (smartwomenadvantagebean.getSpnr_PremFreq().equals("Yearly"))
            return 1;
        else if (smartwomenadvantagebean.getSpnr_PremFreq().equals(
                "Half Yearly"))
            return 0.51;
        else if (smartwomenadvantagebean.getSpnr_PremFreq().equals("Quarterly"))
            return 0.26;
        else
            return 0.085;
    }

    private double getTabularPremiumRate() {
        SmartWomenAdvantageDB smartwomenadvantageDB = new SmartWomenAdvantageDB();
        double TabularPremiumRate = 0;
        double[] premiumArr = null;
        if (smartwomenadvantagebean.getSpnr_criticalIllnesOpt().equals("1"))
            premiumArr = smartwomenadvantageDB.getPremiumRates_1_SAMF();
        else if (smartwomenadvantagebean.getSpnr_criticalIllnesOpt()
                .equals("2"))
            premiumArr = smartwomenadvantageDB.getPremiumRates_2_SAMF();
        else
            premiumArr = smartwomenadvantageDB.getPremiumRates_3_SAMF();

        int position = 0, policyCount = 0;
        TabularPremiumRate = 0;

        if (smartwomenadvantagebean.getSpnr_policyterm() == 10)
            policyCount = 0;
        else
            policyCount = 1;
        for (int ageCount = 18; ageCount <= 50; ageCount++) {
            for (int policyterm = 0; policyterm <= 1; policyterm++) {
                if (ageCount == smartwomenadvantagebean.getSpnr_Age()
                        && policyCount == policyterm) {
                    TabularPremiumRate = premiumArr[position];
                    break;
                }
                position++;
            }

        }

        // System.out.println(" TabularPremiumRate : "+TabularPremiumRate);

        return TabularPremiumRate;

    }

    private double getTabularPremiumRate_CI() {
        SmartWomenAdvantageDB smartwomenadvantageDB = new SmartWomenAdvantageDB();
        double TabularPremiumRate = 0;
        double[] premiumArr = null;
        if (smartwomenadvantagebean.getSpnr_criticalIllnesOpt().equals("1")
                && smartwomenadvantagebean.getSpnr_Plan().equals("Gold"))
            premiumArr = smartwomenadvantageDB.getRate_CI_1_Times_Gold();
        else if (smartwomenadvantagebean.getSpnr_criticalIllnesOpt()
                .equals("2")
                && smartwomenadvantagebean.getSpnr_Plan().equals("Gold"))
            premiumArr = smartwomenadvantageDB.getRate_CI_2_Times_Gold();
        else if (smartwomenadvantagebean.getSpnr_criticalIllnesOpt()
                .equals("3")
                && smartwomenadvantagebean.getSpnr_Plan().equals("Gold"))
            premiumArr = smartwomenadvantageDB.getRate_CI_3_Times_Gold();
        else if (smartwomenadvantagebean.getSpnr_criticalIllnesOpt()
                .equals("1")
                && smartwomenadvantagebean.getSpnr_Plan().equals("Platinum"))
            premiumArr = smartwomenadvantageDB.getRate_CI_1_Times_Platinum();
        else if (smartwomenadvantagebean.getSpnr_criticalIllnesOpt()
                .equals("2")
                && smartwomenadvantagebean.getSpnr_Plan().equals("Platinum"))
            premiumArr = smartwomenadvantageDB.getRate_CI_2_Times_Platinum();
        else
            premiumArr = smartwomenadvantageDB.getRate_CI_3_Times_Platinum();
        int position = 0, policyCount = 0;
        TabularPremiumRate = 0;
        if (smartwomenadvantagebean.getSpnr_policyterm() == 10)
            policyCount = 0;
        else
            policyCount = 1;
        for (int ageCount = 18; ageCount <= 50; ageCount++) {
            for (int policyterm = 0; policyterm <= 1; policyterm++) {
                if (ageCount == smartwomenadvantagebean.getSpnr_Age()
                        && policyCount == policyterm) {
                    TabularPremiumRate = premiumArr[position];
                    break;
                }
                position++;
            }

        }

        // System.out.println(" TabularPremiumRate : "+TabularPremiumRate);

        return TabularPremiumRate;

    }

    private double getTabularPremiumRate_APC() {

        SmartWomenAdvantageDB smartwomenadvantageDB = new SmartWomenAdvantageDB();
        double TabularPremiumRate = 0;
        double[] premiumArr = null;
        if (smartwomenadvantagebean.getSpnr_policyterm() == 10)
            premiumArr = smartwomenadvantageDB.getPremiumRatesAPC_10_Term();
        else
            premiumArr = smartwomenadvantageDB.getPremiumRatesAPC_15_Term();

        int position = 0;
        TabularPremiumRate = 0;
        for (int ageCount = 18; ageCount <= 35; ageCount++) {
            if (ageCount == smartwomenadvantagebean.getSpnr_Age()) {
                TabularPremiumRate = premiumArr[position];
                break;
            }
            position++;
        }

        // System.out.println(" TabularPremiumRate : "+TabularPremiumRate);

        return TabularPremiumRate;

    }

    public double getPremiumMultiFactor() {
        double pf = 0;
        if (smartwomenadvantagebean.getSpnr_PremFreq().equalsIgnoreCase(
                "Yearly"))
            pf = 1.0;
        else if (smartwomenadvantagebean.getSpnr_PremFreq().equalsIgnoreCase(
                "Half Yearly"))
            pf = 2.0;
        else if (smartwomenadvantagebean.getSpnr_PremFreq().equalsIgnoreCase(
                "Quarterly"))
            pf = 4.0;
        else
            pf = 12.0;
        return pf;
    }

    public void setTotalBasePremiumPaid(double BasePremiumPaid, int year_F) {
        if (year_F <= smartwomenadvantagebean.getSpnr_policyterm()) {

            this.totalBasePremiumPaid = BasePremiumPaid * year_F;
        } else
            this.totalBasePremiumPaid = 0;
    }

    public double getTotalBasePremiumPaid() {
        return totalBasePremiumPaid;
    }

    public void setSAMFGuaranteedDeathBenefit(int year_F) {
        if (year_F <= smartwomenadvantagebean.getSpnr_policyterm())

            this.SAMFguaranteedDeathBenefit = smartwomenadvantagebean
                    .getEdt_critiSumAssured();
        else
            this.SAMFguaranteedDeathBenefit = 0;
    }

    public double getSAMFGuaranteedDeathBenefit() {
        return SAMFguaranteedDeathBenefit;
    }

    public void setGuaranteedDeathBenefit(int year_F,
                                          String finalPremBasicSAWithRoundup) {
        if (year_F <= smartwomenadvantagebean.getSpnr_policyterm())

            this.guaranteedDeathBenefit = Double.parseDouble(comm.getRound(comm
                    .getStringWithout_E(Math.max(smartwomenadvantagebean
                            .getEdt_critiSumAssured(), getPremiumMultiFactor()
                            * Double.parseDouble(finalPremBasicSAWithRoundup)
                            * year_F * 1.05))));
        else
            this.guaranteedDeathBenefit = 0;
    }

    public double getGuaranteedDeathBenefit() {
        return guaranteedDeathBenefit;
    }

    // Non-Guaranteed Benefit payable on death at 4%
    public void setNonGuarateedDeathBenefitAt_4_Percent(int year_F) {
        double a, b;
        if (year_F <= smartwomenadvantagebean.getSpnr_policyterm())
            a = year_F * smartwomenadvantagebean.getEdt_SumAssured()
                    * getbonusrate4(smartwomenadvantagebean);
        else
            a = 0;
        if (year_F == smartwomenadvantagebean.getSpnr_policyterm())
            b = a * (prop.terminalBonus + 1);
        else
            b = a * 1;

        this.nonGuarateedDeathBenefitAt_4_Percent = Double.parseDouble(comm
                .getRoundUp(comm.getRoundOffLevel2(b + "")));
    }

    public double getNonGuarateedDeathBenefitAt_4_Percent() {
        return nonGuarateedDeathBenefitAt_4_Percent;
    }

    // Non-Guaranteed Benefit payable on death at 4%
    public void setNonGuarateedDeathBenefitAt_8_Percent(int year_F) {
        double a, b;
        if (year_F <= smartwomenadvantagebean.getSpnr_policyterm())
            a = year_F * smartwomenadvantagebean.getEdt_SumAssured()
                    * getbonusrate8(smartwomenadvantagebean);
        else
            a = 0;
        if (year_F == smartwomenadvantagebean.getSpnr_policyterm())
            b = a * (prop.terminalBonus_8_percent + 1);
        else
            b = a * 1;

        this.nonGuarateedDeathBenefitAt_8_Percent = b;
    }

    public double getNonGuarateedDeathBenefitAt_8_Percent() {
        return nonGuarateedDeathBenefitAt_8_Percent;
    }

    // Non-Guaranteed Benefit payable on death at 4%
    public void setGuarateedCriticalIllnessBenefitAt_Minor(int year_F) {
        double a, b;
        if (year_F <= smartwomenadvantagebean.getSpnr_policyterm())
            this.guarateedCriticalIllnessBenefitAt_Minor = smartwomenadvantagebean
                    .getEdt_critiSumAssured() * 0.25;
        else
            this.guarateedCriticalIllnessBenefitAt_Minor = 0;

    }

    public double getGuarateedCriticalIllnessBenefitAt_Minor() {
        return guarateedCriticalIllnessBenefitAt_Minor;
    }

    // Non-Guaranteed Benefit payable on death at 4%
    public void setGuarateedCriticalIllnessBenefitAt_Major(int year_F) {
        double a, b;
        if (year_F <= smartwomenadvantagebean.getSpnr_policyterm())
            this.guarateedCriticalIllnessBenefitAt_Major = smartwomenadvantagebean
                    .getEdt_critiSumAssured() * 1;
        else
            this.guarateedCriticalIllnessBenefitAt_Major = 0;

    }

    public double getGuarateedCriticalIllnessBenefitAt_Major() {
        return guarateedCriticalIllnessBenefitAt_Major;
    }

    // Non-Guaranteed Benefit payable on death at 4%
    public void setGuarateedCriticalIllnessBenefitAt_Advanced(int year_F) {
        double a, b;
        if (year_F <= smartwomenadvantagebean.getSpnr_policyterm())
            this.guarateedCriticalIllnessBenefitAt_Advanced = smartwomenadvantagebean
                    .getEdt_critiSumAssured() * 1.50;
        else
            this.guarateedCriticalIllnessBenefitAt_Advanced = 0;

    }

    public double getGuarateedCriticalIllnessBenefitAt_Advanced() {
        return guarateedCriticalIllnessBenefitAt_Advanced;
    }

    public void setGuaranteedSurvivalBenefit(int year_f) {
        if (year_f == smartwomenadvantagebean.getSpnr_policyterm()) {
            this.guaranteedSurvivalBenefit = smartwomenadvantagebean
                    .getEdt_SumAssured();
        } else
            this.guaranteedSurvivalBenefit = 0;
    }

    public double getGuaranteedSurvivalBenefit() {
        return guaranteedSurvivalBenefit;
    }

    public void setNonGuarateedSurvivalBenefitAt_4_Percent(int year_f) {
        double a, b;
        if (year_f <= smartwomenadvantagebean.getSpnr_policyterm())
            a = ((double) year_f) * getbonusrate4(smartwomenadvantagebean)
                    * this.guaranteedSurvivalBenefit;// (Double.parseDouble(cfap.getRoundUp(cfap.getStringWithout_E(NonGuarateedDeathBenefitAt_4_Percent))));
        else
            a = 0;

        if (year_f == smartwomenadvantagebean.getSpnr_policyterm()) {
            b = prop.terminalBonus_8_percent + 1;// (Double.parseDouble(cfap.getRoundUp(cfap.getStringWithout_E(NonGuarateedDeathBenefitAt_4_Percent))));
        } else
            b = 1;
        this.nonGuarateedSurvivalBenefitAt_4_Percent = a * b;
    }

    public double getNonGuarateedSurvivalBenefitAt_4_Percent() {
        return nonGuarateedSurvivalBenefitAt_4_Percent;
    }

    public void setNonGuarateedSurvivalBenefitAt_8_Percent(int year_f) {

        double a, b;
        if (year_f <= smartwomenadvantagebean.getSpnr_policyterm())
            a = ((double) year_f) * getbonusrate8(smartwomenadvantagebean)
                    * this.guaranteedSurvivalBenefit;// (Double.parseDouble(cfap.getRoundUp(cfap.getStringWithout_E(NonGuarateedDeathBenefitAt_4_Percent))));
        else
            a = 0;

        if (year_f == smartwomenadvantagebean.getSpnr_policyterm()) {
            b = prop.terminalBonus_8_percent + 1;// (Double.parseDouble(cfap.getRoundUp(cfap.getStringWithout_E(NonGuarateedDeathBenefitAt_4_Percent))));
        } else
            b = 1;

        this.nonGuarateedSurvivalBenefitAt_8_Percent = a * b;
    }

    public double getNonGuarateedSurvivalBenefitAt_8_Percent() {
        return nonGuarateedSurvivalBenefitAt_8_Percent;
    }

    public void setPaidUpValue(int year_f) {
        this.paidUpValue = smartwomenadvantagebean.getEdt_SumAssured() * year_f
                / smartwomenadvantagebean.getSpnr_policyterm();
    }

    public double getPaidUpValue() {
        return paidUpValue;
    }

    public void setGSV_SurrenderValue(int year_f, double finalPremBasicSA) {
        SmartWomenAdvantageDB smartwomenadvantageDB = new SmartWomenAdvantageDB();
        double[] premiumArr = smartwomenadvantageDB.getGSV_Rates();
        this.GSV_SurrenderValue = finalPremBasicSA * year_f
                * premiumArr[year_f - 1] * getPremiumMultiFactor();
    }

    public double getGSV_SurrenderValue() {
        return GSV_SurrenderValue;
    }

    public void setNonGSV_surrndr_val_4_Percent(int year_f) {
        SmartWomenAdvantageDB smartwomenadvantageDB = new SmartWomenAdvantageDB();

        double[] premiumArr = null;
        if (smartwomenadvantagebean.getSpnr_policyterm() == 10)
            premiumArr = smartwomenadvantageDB.getSSV_Rates_10();
        else
            premiumArr = smartwomenadvantageDB.getSSV_Rates_15();
        // System.out.println(year_f+"  hihi "+getPaidUpValue()+"      "+getNonGuarateedDeathBenefitAt_4_Percent_O(year_f)+"    "+premiumArr[year_f-1]+"       "+((getPaidUpValue()+getNonGuarateedDeathBenefitAt_4_Percent_O(year_f))*0.6*premiumArr[year_f-1]));
        this.GSV_SurrenderValue_4_Percent = (getPaidUpValue() + getNonGuarateedDeathBenefitAt_4_Percent_O(year_f))
                * 0.6 * premiumArr[year_f - 1];
    }

    public double getNonGSV_surrndr_val_4_Percent() {
        return GSV_SurrenderValue_4_Percent;
    }

    public void setNonGSV_surrndr_val_8_Percent(int year_f) {
        SmartWomenAdvantageDB smartwomenadvantageDB = new SmartWomenAdvantageDB();

        double[] premiumArr = null;
        if (smartwomenadvantagebean.getSpnr_policyterm() == 10)
            premiumArr = smartwomenadvantageDB.getSSV_Rates_10();
        else
            premiumArr = smartwomenadvantageDB.getSSV_Rates_15();
        this.GSV_SurrenderValue_8_Percent = (getPaidUpValue() + getNonGuarateedDeathBenefitAt_8_Percent_P(year_f))
                * premiumArr[year_f - 1];
    }

    public double getNonGSV_surrndr_val_8_Percent() {
        return GSV_SurrenderValue_8_Percent;
    }

    // Non-Guaranteed Benefit payable on death at 4%
    private double getNonGuarateedDeathBenefitAt_4_Percent_O(int year_F) {
        double a, b;
        if (year_F <= smartwomenadvantagebean.getSpnr_policyterm())
            a = year_F * smartwomenadvantagebean.getEdt_SumAssured()
                    * getbonusrate4(smartwomenadvantagebean);
        else
            a = 0;
        if (year_F == smartwomenadvantagebean.getSpnr_policyterm())
            b = a * 1;
        else
            b = a * 1;

        return b;
    }

    // Non-Guaranteed Benefit payable on death at 4%
    private double getNonGuarateedDeathBenefitAt_8_Percent_P(int year_F) {
        double a, b;
        if (year_F <= smartwomenadvantagebean.getSpnr_policyterm())
            a = year_F * smartwomenadvantagebean.getEdt_SumAssured()
                    * getbonusrate8(smartwomenadvantagebean);
        else
            a = 0;
        if (year_F == smartwomenadvantagebean.getSpnr_policyterm())
            b = a * 1;
        else
            b = a * 1;

        return b;
    }

    private double getbonusrate4(SmartWomenAdvantageBean smartWomenAdvantageBean) {
        double bonusrate = 0;

        if (smartWomenAdvantageBean.getSpnr_criticalIllnesOpt().equals("1")) {
            if (smartWomenAdvantageBean.getSpnr_policyterm() == 10) {
                bonusrate = 0.011;
            } else if (smartWomenAdvantageBean.getSpnr_policyterm() == 15) {
                bonusrate = 0.011;
            }
        } else if (smartWomenAdvantageBean.getSpnr_criticalIllnesOpt().equals(
                "2")) {
            if (smartWomenAdvantageBean.getSpnr_policyterm() == 10) {
                bonusrate = 0.01;
            } else if (smartWomenAdvantageBean.getSpnr_policyterm() == 15) {
                bonusrate = 0.01;
            }
        } else if (smartWomenAdvantageBean.getSpnr_criticalIllnesOpt().equals(
                "3")) {
            if (smartWomenAdvantageBean.getSpnr_policyterm() == 10) {
                bonusrate = 0.009;
            } else if (smartWomenAdvantageBean.getSpnr_policyterm() == 15) {
                bonusrate = 0.009;
            }
        }

        return bonusrate;

    }

    private double getbonusrate8(SmartWomenAdvantageBean smartWomenAdvantageBean) {
        double bonusrate8 = 0;

        if (smartWomenAdvantageBean.getSpnr_criticalIllnesOpt().equals("1")) {
            if (smartWomenAdvantageBean.getSpnr_policyterm() == 10) {
                bonusrate8 = 0.029;
            } else if (smartWomenAdvantageBean.getSpnr_policyterm() == 15) {
                bonusrate8 = 0.03;
            }
        } else if (smartWomenAdvantageBean.getSpnr_criticalIllnesOpt().equals(
                "2")) {
            if (smartWomenAdvantageBean.getSpnr_policyterm() == 10) {
                bonusrate8 = 0.026;
            } else if (smartWomenAdvantageBean.getSpnr_policyterm() == 15) {
                bonusrate8 = 0.027;
            }
        } else if (smartWomenAdvantageBean.getSpnr_criticalIllnesOpt().equals(
                "3")) {
            if (smartWomenAdvantageBean.getSpnr_policyterm() == 10) {
                bonusrate8 = 0.024;
            } else if (smartWomenAdvantageBean.getSpnr_policyterm() == 15) {
                bonusrate8 = 0.025;
            }
        }

        return bonusrate8;

    }

    /*** modified by Akshaya on 20-MAY-16 start **/

    public double getServiceTax(double premiumWithoutST, boolean JKResident,
                                String type) {
        if (type.equals("basic")) {
            if (JKResident)
                return Double
                        .parseDouble(comm.getRoundUp(String
                                .valueOf(premiumWithoutST
                                        * prop.serviceTaxJKResident)));
            else {
                return Double.parseDouble(comm.getRoundUp(String
                        .valueOf(premiumWithoutST * prop.serviceTax)));
            }
        } else if (type.equals("SBC")) {
            if (JKResident)
                return 0;
            else {
                return Double.parseDouble(comm.getRoundUp(String
                        .valueOf(premiumWithoutST * prop.SBCServiceTax)));
            }
        } else if (type.equals("KERALA")) {
            return Double.parseDouble(comm.getRoundUp(String.valueOf(premiumWithoutST * prop.kerlaServiceTax)));
        } else // KKC
        {
            if (JKResident == true)
                return 0;
            else {
                return Double.parseDouble(comm.getRoundUp(String
                        .valueOf(premiumWithoutST * prop.KKCServiceTax)));
            }
        }

    }

    public double getServiceTaxSecondYear(double premiumWithoutST,
                                          boolean JKResident, String type) {
        if (type.equals("basic")) {
            if (JKResident)
                return Double
                        .parseDouble(comm.getRoundUp(String
                                .valueOf(premiumWithoutST
                                        * prop.serviceTaxJKResident)));
            else {
                return Double
                        .parseDouble(comm.getRoundUp(String
                                .valueOf(premiumWithoutST
                                        * prop.serviceTaxSecondYear)));
            }
        } else if (type.equals("SBC")) {
            if (JKResident)
                return 0;
            else {
                return Double.parseDouble(comm.getRoundUp(String
                        .valueOf(premiumWithoutST
                                * prop.SBCServiceTaxSecondYear)));
            }
        } else if (type.equals("KERALA")) {
            return Double.parseDouble(comm.getRoundUp(String.valueOf(premiumWithoutST * prop.kerlaServiceTaxSecondYear)));
        } else // KKC
        {
            if (JKResident == true)
                return 0;
            else {
                return Double.parseDouble(comm.getRoundUp(String
                        .valueOf(premiumWithoutST
                                * prop.KKCServiceTaxSecondYear)));
            }
        }

    }

    /*** modified by Akshaya on 20-MAY-16 end **/

    /**************** Modified by Akshaya on 26-Feb-2016 start *************************/

    public double getMinesOccuInterest(double SumAssured_Basic) {
        return (SumAssured_Basic / 1000) * 2;
    }

    public String setFinalPremiumBasicSA_withoutStaff() {
        double basicPremiumWTLoadng = setBasicPremiumWTLoading();
        double result = basicPremiumWTLoadng * (1 - 0) * getModalFactor();
        // System.out.println(" final Basic Prem : " + result);
        return result + "";
    }

    public String setFinalPremiumBasicCritiIllness_withoutStaff() {
        double basicPremiumWTLoadng = (smartwomenadvantagebean
                .getEdt_SumAssured() * getTabularPremiumRate_CI()) / 1000;
        double result = basicPremiumWTLoadng * (1 - 0) * getModalFactor();
        // System.out.println(" final Basic Prem For Criti Illness: " + result);
        return result + "";
    }

    public String setFinalPremiumBasicAPCnCA_withoutStaff() {
        if (smartwomenadvantagebean.getIsAPCnCAoption()) {
            double basicPremiumWTLoadng = (smartwomenadvantagebean
                    .getEdt_APCSumAssured() * getTabularPremiumRate_APC()) / 1000;
            double result = basicPremiumWTLoadng * (1 - 0) * getModalFactor();
            // System.out.println(" final Basic Prem For APC n CP: " + result);
            return result + "";
        } else
            return 0 + "";
    }

    /**************** Modified by Akshaya on 26-Feb-2016 end *************************/

    /**************** Modified by Akshaya on 26-Apr-2016 end *************************/
    // public String setFinalPremiumBasicSA_withoutStaffSA() {
    //
    // double basicPremiumWTLoadng = setBasicPremiumWTLoading();
    // // double tabularPremiumRate=getTabularPremiumRate();
    // // double
    // //
    // result=((smartwomenadvantagebean.getEdt_SumAssured()*(tabularPremiumRate-basicPremiumWTLoadng))/1000);
    // // System.out.println(" final Basic Prem : "+result);
    // // return result+"";
    // return basicPremiumWTLoadng + "";
    // }
    public String setFinalPremiumBasicSA_withoutStaffSA() {

        double tabularPremiumRate = getTabularPremiumRate();

        double temp = ((smartwomenadvantagebean.getEdt_SumAssured() * (tabularPremiumRate)) / 1000);

        return temp + "";

    }

    public String setFinalPremiumBasicCritiIllness_withoutStaffSA() {
        double basicPremiumWTLoadng = (smartwomenadvantagebean
                .getEdt_SumAssured() * getTabularPremiumRate_CI()) / 1000;
        // double result=basicPremiumWTLoadng*(1-0)*getModalFactor();
        // System.out.println(" final Basic Prem For Criti Illness: "+result);
        return basicPremiumWTLoadng + "";
    }

    public String setFinalPremiumBasicAPCnCA_withoutStaffSA() {
        if (smartwomenadvantagebean.getIsAPCnCAoption()) {
            double basicPremiumWTLoadng = (smartwomenadvantagebean
                    .getEdt_APCSumAssured() * getTabularPremiumRate_APC()) / 1000;
            // double result=basicPremiumWTLoadng*(1-0)*getModalFactor();
            // System.out.println(" final Basic Prem For APC n CP: "+result);
            return basicPremiumWTLoadng + "";
        } else
            return 0 + "";
    }

    /*** Added by Priyanka Warekar - 31-08-2018 - start *******/
    public double getServiceTaxMines(double MinesOccuInterest) {
        return Double.parseDouble(comm.getRoundUp(comm.getRoundOffLevel2(comm.getStringWithout_E(MinesOccuInterest * (prop.serviceTax + prop.SBCServiceTax)))));
    }
/*** Added by Priyanka Warekar - 31-08-2018 - end *******/

}
