package sbilife.com.pointofsale_bancaagency.smartelite;

import java.text.DecimalFormat;
import java.util.ArrayList;

import sbilife.com.pointofsale_bancaagency.common.CommonForAllProd;

class BI_SmartEliteBusinessLogic {
    private CommonForAllProd cfap = null;
    private String fundValueAfterFMCandBeforeGA_V34 = null;
    private BI_SmartEliteProperties SE_Prop = null;
    private String month_E = null, year_F = null, riderCharges_P = null,
            policyInForce_G = "Y", age_H = null, premium_I = null,
            TopUpPremium_J = null, premiumAllocationCharge_K = null,
            topUpCharges_L = null, serviceTaxOnAllocation_M = null,
            AmountAvailableForInvestment_N = null, AmountAvailableForInvestment_N1 = null,
            transferPercentIfAny_O = null, allocatedFundToIndexFund_P = null,
            sumAssuredRelatedCharges_O = null, optionCharges_R = null,
            policyAdministrationCharge_Q = null, mortalityCharges_R = null,
            guaranteeCharge_U = null, totalCharges_S = null,
            totalServiceTax_ExclOfSTonAllocAndSurr_T = null,
            totalServiceTax_U = null,
            transferOfIndexFundGainToDailyProtectFund_Y = null,
            transferOfCapitalFromIndexToDailyProtectFund_Z = null,
            dailyProtectFundAtStart_AA = null, indexFundAtStart_AB = null,
            dailyProtectFundAfterCharges_AC = null,
            indexFundAtStartAfterCharges_AD = null,
            additionToDailyProtectFund_AE = null,
            additionToIndexFund_AF = null,
            FMCearnedOnDailyProtectFund_AG = null,
            FMCearnedOnIndexFund_AH = null, additionToFundIfAny_V = null,
            fundBeforeFMC_W = null, fundManagementCharge_X = null,
            serviceTaxOnFMC_Y = null, fundValueAfterFMCandBeforeGA_Z = null,
            guaranteedAddition_AA = null, dailyProtectFundAtEnd_AO = null,
            indexFundAtEnd_AP = null, fundValueAtEnd_AQ = null,
            surrenderCharges_AC = null, serviceTaxOnSurrenderCharges_AD = null,
            surrenderValue_AE = null, deathBenefit_AF = null,
            mortalityCharges_AG = null, guaranteeCharge_AW = null,
            totalCharges_AH = null,
            totalServiceTax_ExclOfSTonAllocAndsurr_AI = null,
            totalServiceTax_AJ = null,
            transferOfIndexFundGainToDailyProtectFund_BA = null,
            transferOfCapitalFromIndexToDailyProtectFund_BB = null,
            dailyProtectFundAtStart_BC = null, indexFundAtStart_BD = null,
            dailyProtectFundAftercharges_BE = null,
            indexFundAtStartAfterCharges_BF = null,
            additionToDailyProtectFund_BG = null,
            additionToIndexFund_BH = null,
            FMCearnedOnDailyProtectFund_BI = null,
            FMCearnedOnIndexFund_BJ = null, additionToFundIfAny_AK = null,
            fundBeforeFMC_AL = null, fundManagementCharge_AM = null,
            serviceTaxOnFMC_AN = null, fundValueAfterFMCandBeforeGA_AO = null,
            guaranteedAddition_AP = null, dailyProtectFundAtend_BQ = null,
            indexFundAtEnd_BR = null, fundValueAtEnd_AB = null,
            surrenderCharges_AR = null, serviceTaxOnSurrenderCharges_AS = null,
            surrenderValue_AT = null, deathBenefit_AU = null,
            surrenderCap_AV = null,
            oneHundredPercentOfCummulativePremium_AW = null,
            mortalityChargesReductionYield_AG = null,
            totalChargesReductionYield_AH = null,
            totalServiceTax_ExclOfSTonAllocAndsurrReductionYield_AI = null,
            additionToFundIfAnyReductionYield_AK = null,
            fundBeforeFMCReductionYield_AL = null,
            fundManagementChargeReductionYield_AM = null,
            serviceTaxOnFMCReductionYield_AN = null,
            fundValueAfterFMCandBeforeGAReductionYield_AO = null,
            fundValueAtEndReductionYield_AQ = null,
            deathBenefitReductionYield_AU = null,
            totalServiceTaxReductionYield_AJ = null,
            surrenderChargesReductionYield_AR = null,
            serviceTaxOnSurrenderChargesReductionYield_AS = null,
            surrenderValueReductionYield_AT = null, month_BG = null,
            reductionYield_BI = null, reductionYield_BD = null,
            irrAnnual_BD = null, irrAnnual_BI = null,
            reductionInYieldMaturityAt = null,
            reductionInYieldNumberOfYearsElapsedSinceInception = null,
            mortalityChargesReductionYield_R = null,
            totalChargesReductionYield_S = null,
            totalServiceTax_ExclOfSTonAllocAndsurrReductionYield_T = null,
            additionToFundIfAnyReductionYield_V = null,
            fundBeforeFMCReductionYield_W = null,
            fundManagementChargeReductionYield_X = null,
            serviceTaxOnFMCReductionYield_Y = null,
            fundValueAfterFMCandBeforeGAReductionYield_Z = null,
            reductionYield_BC = null, fundValueAtEndReductionYield_AB = null,
            deathBenefitReductionYield_AF = null,
            totalServiceTaxReductionYield_U = null,
            surrenderChargesReductionYield_AC = null,
            serviceTaxOnSurrenderChargesReductionYield_AD = null,
            surrenderValueReductionYield_AE = null, irrAnnual_BC = null;


    //##########################
    public BI_SmartEliteBusinessLogic() {
        cfap = new CommonForAllProd();
        SE_Prop = new BI_SmartEliteProperties();
    }

    // **********************************************************************************************************************************
    public String getFundValueAfterFMCAndBeforeGA_V34() {
        return fundValueAfterFMCandBeforeGA_V34;
    }

    public void setFundValueAfterFMCAndBeforeGA_V34() {
        this.fundValueAfterFMCandBeforeGA_V34 = getFundValueAfterFMCandBeforeGA_Z();
    }

    // **********************************************************************************************************************************

    public String getAmountAvailableForInvestment_N() {
        return AmountAvailableForInvestment_N;
    }

    public void setAmountAvailableForInvestment_N() {/*
		this.AmountAvailableForInvestment_N = ""
				+ (Double.parseDouble(getPremium_I())
						+ Double.parseDouble(getTopUpPremium_J())
						- Double.parseDouble(getPremiumAllocationCharge_K())
						- Double.parseDouble(getTopUpCharges_L()) - Double
							.parseDouble(getServiceTaxOnAllocation_M()));
	*/
        {
            this.AmountAvailableForInvestment_N = cfap.roundUp_Level2("" + (Double.parseDouble(getPremium_I()) - Double.parseDouble(getPremiumAllocationCharge_K()) - Double.parseDouble(getServiceTaxOnAllocation_M())));
        }
    }

    public String getAmountAvailableForInvestment_N1() {
        return AmountAvailableForInvestment_N1;
    }

    public void setAmountAvailableForInvestment_N1() {/*
		this.AmountAvailableForInvestment_N = ""
				+ (Double.parseDouble(getPremium_I())
						+ Double.parseDouble(getTopUpPremium_J())
						- Double.parseDouble(getPremiumAllocationCharge_K())
						- Double.parseDouble(getTopUpCharges_L()) - Double
							.parseDouble(getServiceTaxOnAllocation_M()));
	*/
        {
            this.AmountAvailableForInvestment_N1 = cfap.roundUp_Level2("" + (Double.parseDouble(getPremium_I()) - Double.parseDouble(getPremiumAllocationCharge_K())));
        }
    }

    public String getFMCearnedOnDailyProtectFund_AG() {
        return FMCearnedOnDailyProtectFund_AG;
    }

    public void setFMCearnedOnDailyProtectFund_AG(int policyTerm,
                                                  double charge_Fund, double charge_Fund_Ren) {
        double a = 0, b = 0;
        if (getMonth_E().equals("1")) {
            b = charge_Fund;
        } else {
            b = 0;
        }
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            a = (Double.parseDouble(getDailyProtectFundAfterCharges_AC()) + Double
                    .parseDouble(getAdditionToDailyProtectFund_AE()))
                    * (b + charge_Fund_Ren / 12);
        } else {
            a = 0;
        }
        this.FMCearnedOnDailyProtectFund_AG = cfap.roundUp_Level2(cfap
                .getStringWithout_E(a));
    }

    public String getFMCearnedOnDailyProtectFund_BI() {
        return FMCearnedOnDailyProtectFund_BI;
    }

    public void setFMCearnedOnDailyProtectFund_BI(int policyTerm,
                                                  double charge_Fund, double charge_Fund_Ren) {
        double a = 0;
        if (Double.parseDouble(getAdditionToFundIfAny_V()) == 1.0) {
            a = charge_Fund;
        } else {
            a = 0;
        }
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.FMCearnedOnDailyProtectFund_BI = cfap
                    .roundUp_Level2(cfap.getStringWithout_E(((Double
                            .parseDouble(getDailyProtectFundAftercharges_BE()) + Double
                            .parseDouble(getAdditionToDailyProtectFund_BG())) * (a + charge_Fund_Ren / 12))));
        } else {
            this.FMCearnedOnDailyProtectFund_BI = "" + 0;
        }
    }

    public String getFMCearnedOnIndexFund_AH() {
        return FMCearnedOnIndexFund_AH;
    }

    public void setFMCearnedOnIndexFund_AH(int policyTerm, double charge_Fund,
                                           double indexFund) {
        double a = 0;
        if (getMonth_E().equals("1")) {
            a = charge_Fund;
        } else {
            a = 0;
        }
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.FMCearnedOnIndexFund_AH = cfap
                    .roundUp_Level2(cfap.getStringWithout_E(((Double
                            .parseDouble(getIndexFundAtStartAfterCharges_AD()) + Double
                            .parseDouble(getAdditionToIndexFund_AF())) * (a + indexFund / 12))));
        } else {
            this.FMCearnedOnIndexFund_AH = "" + 0;
        }
    }

    public String getFMCearnedOnIndexFund_BJ() {
        return FMCearnedOnIndexFund_BJ;
    }

    public void setFMCearnedOnIndexFund_BJ(double indexFund, int policyTerm,
                                           double charge_Fund) {
        double a = 0;
        if (Double.parseDouble(getAdditionToFundIfAny_V()) == 1) {
            a = charge_Fund;
        } else {
            a = 0;
        }
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.FMCearnedOnIndexFund_BJ = cfap
                    .roundUp_Level2(cfap.getStringWithout_E(((Double
                            .parseDouble(getIndexFundAtStartAfterCharges_BF()) + Double
                            .parseDouble(getAdditionToIndexFund_BH())) * (a + indexFund / 12))));
        } else {
            this.FMCearnedOnIndexFund_BJ = "" + 0;
        }
    }

    public String getRiderCharges_P() {
        return riderCharges_P;
    }

    public void setRiderCharges_P(double sumAssured) {

        double a = 0, b = 0;
        a = Math.min(sumAssured, 5000000);
        b = ((a * 0.5) / 12000);
        this.riderCharges_P = cfap
                .roundUp_Level2(cfap.getStringWithout_E(b));
    }

    public String getTopUpPremium_J() {
        return TopUpPremium_J;
    }

    public void setTopUpPremium_J(boolean topUp, double effectiveTopUpPrem,
                                  String addTopUp) {
        if (getMonth_E().equals("1") && topUp && addTopUp.equals("Yes")) {
            this.TopUpPremium_J = ("" + effectiveTopUpPrem);
        } else {
            this.TopUpPremium_J = ("" + 0);
        }
    }

    public String getAdditionToDailyProtectFund_AE() {
        return additionToDailyProtectFund_AE;
    }

    public void setAdditionToDailyProtectFund_AE(int policyTerm, double int1) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.additionToDailyProtectFund_AE = cfap
                    .roundUp_Level2(cfap.getStringWithout_E((Double
                            .parseDouble(getDailyProtectFundAfterCharges_AC()) * (cfap
                            .pow((1 + int1), (double) 1 / 12) - 1))));
        } else {
            this.additionToDailyProtectFund_AE = "" + 0;
        }
    }

    public String getAdditionToDailyProtectFund_BG() {
        return additionToDailyProtectFund_BG;
    }

    public void setAdditionToDailyProtectFund_BG(int policyTerm, double int2) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.additionToDailyProtectFund_BG = cfap
                    .roundUp_Level2(cfap.getStringWithout_E((Double
                            .parseDouble(getDailyProtectFundAftercharges_BE()) * (cfap
                            .pow((1 + int2), (double) 1 / 12) - 1))));
        } else {
            this.additionToDailyProtectFund_BG = "" + 0;
        }
    }

    public String getAdditionToFundIfAny_V() {
        return additionToFundIfAny_V;
    }

    public void setAdditionToFundIfAny_V(double _fundValueAtEnd_AB,
                                         int policyTerm) {
		/*System.out.println("_fundValueAtEnd_AB"+_fundValueAtEnd_AB);
		System.out.println("getAmountAvailableForInvestment_N()"+getAmountAvailableForInvestment_N());
		System.out.println("getTotalCharges_S()"+getTotalCharges_S());
		System.out.println("getTotalServiceTax_ExclOfSTonAllocAndSurr_T()"+getTotalServiceTax_ExclOfSTonAllocAndSurr_T());
		System.out.println("getRiderCharges_P()"+getRiderCharges_P());*/
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
			/*this.additionToFundIfAny_V = cfap
					.roundUp_Level2(cfap.getStringWithout_E((_fundValueAtEnd_AB
							+ Double.parseDouble(getAmountAvailableForInvestment_N())
							- Double.parseDouble(getTotalCharges_S())
							- Double.parseDouble(getTotalServiceTax_ExclOfSTonAllocAndSurr_T()) + Double
								.parseDouble(getRiderCharges_P()))
							* (cfap.pow(1 + SE_Prop.int1, (double) 1 / 12) - 1)));*/
			/*this.additionToFundIfAny_V = cfap
					.roundUp_Level2(cfap.getStringWithout_E((_fundValueAtEnd_AB
							+ Double.parseDouble(getAmountAvailableForInvestment_N())
							- Double.parseDouble(getTotalCharges_S())
							- Double.parseDouble(getTotalServiceTax_ExclOfSTonAllocAndSurr_T()))
							* (cfap.pow(1 + SE_Prop.int1, (double) 1 / 12) - 1)));*/
            double temp1 = 0, temp2 = 0;
            temp1 = (_fundValueAtEnd_AB + Double.parseDouble(getAmountAvailableForInvestment_N()) - Double.parseDouble(getTotalCharges_S()) - Double.parseDouble(getTotalServiceTax_ExclOfSTonAllocAndSurr_T()));
            double a = (double) 1 / 12;
            temp2 = (cfap.pow((1 + SE_Prop.int1), a)) - 1;
            this.additionToFundIfAny_V = cfap.roundUp_Level2(cfap.getStringWithout_E(temp1 * temp2));

        } else {
            this.additionToFundIfAny_V = "" + 0;
        }
    }

    public String getAdditionToFundIfAny_AK() {
        return additionToFundIfAny_AK;
    }

    public void setAdditionToFundIfAny_AK(double _fundValueAtEnd_AQ,
                                          int policyTerm) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.additionToFundIfAny_AK = cfap
                    .roundUp_Level2(cfap.getStringWithout_E((_fundValueAtEnd_AQ
                            + Double.parseDouble(getAmountAvailableForInvestment_N())
                            - Double.parseDouble(getTotalCharges_AH())
                            - Double.parseDouble(getTotalServiceTax_ExclOfSTonAllocAndSurr_AI()))
                            * (cfap.pow(1 + SE_Prop.int2, (double) 1 / 12) - 1)));
        } else {
            this.additionToFundIfAny_AK = "" + 0;
        }
    }

    public String getAdditionToIndexFund_AF() {
        return additionToIndexFund_AF;
    }

    public void setAdditionToIndexFund_AF(int policyTerm, double int1) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.additionToIndexFund_AF = cfap
                    .roundUp_Level2(cfap.getStringWithout_E((Double
                            .parseDouble(getIndexFundAtStartAfterCharges_AD()) * (cfap
                            .pow(1 + int1, (double) 1 / 12) - 1))));
        } else {
            this.additionToIndexFund_AF = "" + 0;
        }
    }

    public String getAdditionToIndexFund_BH() {
        return additionToIndexFund_BH;
    }

    public void setAdditionToIndexFund_BH(int policyTerm, double int2) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.additionToIndexFund_BH = cfap
                    .roundUp_Level2(cfap.getStringWithout_E((Double
                            .parseDouble(getIndexFundAtStartAfterCharges_BF()) * (cfap
                            .pow((1 + int2), (double) 1 / 12) - 1))));
        } else {
            this.additionToIndexFund_BH = "" + 0;
        }
    }

    public String getAge_H() {
        return age_H;
    }

    public void setAge_H(int ageAtEntry) {
        this.age_H = "" + (ageAtEntry + Integer.parseInt(getYear_F()) - 1);
    }

    public String getAllocatedFundToIndexFund_P() {
        return allocatedFundToIndexFund_P;
    }

    public void setAllocatedFundToIndexFund_P(double p18,
                                              double percentToBeInvestedForIndexFund) {
        if (getPolicyInForce_G().equals("Y")) {
            this.allocatedFundToIndexFund_P = cfap
                    .roundUp_Level2(cfap.getStringWithout_E((p18 + Double
                            .parseDouble(getAmountAvailableForInvestment_N())
                            * percentToBeInvestedForIndexFund
                            * (1 - Double
                            .parseDouble(getTransferPercentIfAny_O())))));
        } else {
            this.allocatedFundToIndexFund_P = "" + 0;
        }
    }

    public String getDailyProtectFundAfterCharges_AC() {
        return dailyProtectFundAfterCharges_AC;
    }

    public void setDailyProtectFundAfterCharges_AC(boolean guaranteeCharges,
                                                   double serviceTax) {
        double a = 0, b = 0;
        if (guaranteeCharges) {
            a = serviceTax;
        } else {
            a = 0;
        }
        if (getPolicyInForce_G().equals("Y")) {
            this.dailyProtectFundAfterCharges_AC = cfap
                    .roundUp_Level2(cfap.getStringWithout_E(Double
                            .parseDouble(getDailyProtectFundAtStart_AA())
                            - ((Double.parseDouble(getTotalCharges_S())
                            + Double.parseDouble(getTotalServiceTax_ExclOfSTonAllocAndSurr_T()) - Double
                            .parseDouble(getGuaranteeCharge_U())
                            * (1 + a))
                            * Double.parseDouble(getDailyProtectFundAtStart_AA()) / (Double
                            .parseDouble(getDailyProtectFundAtStart_AA()) + Double
                            .parseDouble(getIndexFundAtStart_AB())))
                            - Double.parseDouble(getGuaranteeCharge_U())
                            * (1 + a)));
        } else {
            this.dailyProtectFundAfterCharges_AC = "" + 0;
        }
    }

    public String getDailyProtectFundAftercharges_BE() {
        return dailyProtectFundAftercharges_BE;
    }

    public void setDailyProtectFundAftercharges_BE(boolean guaranteeCharges,
                                                   double serviceTax) {
        double a = 0;
        if (guaranteeCharges) {
            a = serviceTax;
        } else {
            a = 0;
        }
        if (getPolicyInForce_G().equals("Y")) {
            this.dailyProtectFundAftercharges_BE = cfap
                    .roundUp_Level2(cfap.getStringWithout_E(Double
                            .parseDouble(getDailyProtectFundAtStart_BC())
                            - ((Double.parseDouble(getTotalCharges_AH())
                            + Double.parseDouble(getTotalServiceTax_ExclOfSTonAllocAndSurr_AI()) - Double
                            .parseDouble(getGuaranteeCharge_AW())
                            * (1 + a))
                            * Double.parseDouble(getDailyProtectFundAtStart_BC()) / (Double
                            .parseDouble(getDailyProtectFundAtStart_BC()) + Double
                            .parseDouble(getIndexFundAtStart_BD())))
                            - Double.parseDouble(getGuaranteeCharge_AW())
                            * (1 + a)));
        } else {
            this.dailyProtectFundAftercharges_BE = "" + 0;
        }
    }

    public String getDailyProtectFundAtEnd_AO() {
        return dailyProtectFundAtEnd_AO;
    }

    public void setDailyProtectFundAtEnd_AO(boolean fundManagementCharges,
                                            double charge_Fund_Ren, double serviceTax) {
        double a = 0;
        if (fundManagementCharges) {
            a = Double.parseDouble(getFMCearnedOnDailyProtectFund_AG())
                    * (0.0135 / charge_Fund_Ren);
        } else {
            a = 0;
        }
        if (getPolicyInForce_G().equals("Y")) {
            this.dailyProtectFundAtEnd_AO = ""
                    + (Double.parseDouble(getDailyProtectFundAfterCharges_AC())
                    + Double.parseDouble(getAdditionToDailyProtectFund_AE())
                    - Double.parseDouble(getFMCearnedOnDailyProtectFund_AG()) - a
                    * serviceTax);
        } else {
            this.dailyProtectFundAtEnd_AO = "" + 0;
        }
    }

    public String getDailyProtectFundAtStart_AA() {
        return dailyProtectFundAtStart_AA;
    }

    public void setDailyProtectFundAtStart_AA(
            double percentToBeInvestedForDailyProtectFund,
            double _dailyProtectFundAtEnd) {
        if (getPolicyInForce_G().equals("Y")) {
            this.dailyProtectFundAtStart_AA = cfap
                    .roundUp_Level2(cfap.getStringWithout_E(Double
                            .parseDouble(getAmountAvailableForInvestment_N())
                            * percentToBeInvestedForDailyProtectFund
                            + Double.parseDouble(getTransferOfIndexFundGainToDailyProtectFund_Y())
                            + Double.parseDouble(getTransferOfCapitalFromIndexToDailyProtectFund_Z())
                            + _dailyProtectFundAtEnd));
        } else {
            this.dailyProtectFundAtStart_AA = "" + 0;
        }
    }

    public String getDailyProtectFundAtStart_BC() {
        return dailyProtectFundAtStart_BC;
    }

    public void setDailyProtectFundAtStart_BC(double _dailyProtectFundAtEnd_BQ,
                                              double percentToBeInvestedForDailyProtectFund) {
        if (getPolicyInForce_G().equals("Y")) {
            this.dailyProtectFundAtStart_BC = cfap
                    .roundUp_Level2(cfap.getStringWithout_E(Double
                            .parseDouble(getAmountAvailableForInvestment_N())
                            * percentToBeInvestedForDailyProtectFund
                            + Double.parseDouble(getTransferOfIndexFundGainToDailyProtectFund_BA())
                            + Double.parseDouble(getTransferOfCapitalFromIndexToDailyProtectFund_BB())
                            + _dailyProtectFundAtEnd_BQ));
        } else {
            this.dailyProtectFundAtStart_BC = "" + 0;
        }
    }

    public String getDailyProtectFundAtEnd_BQ() {
        return dailyProtectFundAtend_BQ;
    }

    public void setDailyProtectFundAtEnd_BQ(double serviceTax,
                                            double charge_Fund_Ren, boolean fundManagementCharges) {
        double a = 0;
        if (fundManagementCharges) {
            a = Double.parseDouble(getFMCearnedOnDailyProtectFund_BI())
                    * (0.0135 / charge_Fund_Ren);
        } else {
            a = 0;
        }
        if (getPolicyInForce_G().equals("Y")) {
            this.dailyProtectFundAtend_BQ = ""
                    + (Double.parseDouble(getDailyProtectFundAftercharges_BE())
                    + Double.parseDouble(getAdditionToDailyProtectFund_BG())
                    - Double.parseDouble(getFMCearnedOnDailyProtectFund_BI()) - a
                    * serviceTax);
        } else {
            this.dailyProtectFundAtend_BQ = "" + 0;
        }
    }

    public String getDeathBenefit_AF() {
        return deathBenefit_AF;
    }

    public void setDeathBenefit_AF(int policyTerm, double sumAssured,
                                   String planOption) {
        if (Integer.parseInt(getYear_F()) > policyTerm) {
            this.deathBenefit_AF = "" + 0;
        } else {
            if (planOption.equals("Gold")) {
                this.deathBenefit_AF = ""
                        + (Math.max(
                        Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()),
                        Math.max(sumAssured, Double
                                .parseDouble(getFundValueAtEnd_AB()))));
            } else {
                this.deathBenefit_AF = ""
                        + (Math.max(
                        Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()),
                        (sumAssured + Double
                                .parseDouble(getFundValueAtEnd_AB()))));
            }
        }

		/*else
        {this.deathBenefit_AF=""+(Math.max(Double.parseDouble( getOneHundredPercentOfCummulativePremium_AW()), Math.max(sumAssured, Double.parseDouble(getFundValueAtEnd_AB()))));}*/
    }

    public String getDeathBenefit_AU() {
        return deathBenefit_AU;
    }

    public void setDeathBenefit_AU(int policyTerm, double sumAssured,
                                   String planOption) {
        if (Integer.parseInt(getYear_F()) > policyTerm) {
            this.deathBenefit_AU = "" + 0;
        } else {
            if (planOption.equals("Gold")) {
                this.deathBenefit_AU = ""
                        + (Math.max(
                        Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()),
                        Math.max(sumAssured, Double
                                .parseDouble(getFundValueAtEnd_AQ()))));
            } else {
                this.deathBenefit_AU = ""
                        + (Math.max(
                        Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()),
                        (sumAssured + Double
                                .parseDouble(getFundValueAtEnd_AQ()))));
            }
        }
		/*else
		{this.deathBenefit_AU=""+(Math.max(Double.parseDouble( getOneHundredPercentOfCummulativePremium_AW()), Math.max(sumAssured, Double.parseDouble(getFundValueAtEnd_AQ()))));}*/
    }

    public String getFundBeforeFMC_W() {
        return fundBeforeFMC_W;
    }

    public void setFundBeforeFMC_W(double _fundValueAtEnd_AB, int policyTerm) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
			/*this.fundBeforeFMC_W = ""
					+ (_fundValueAtEnd_AB
							+ Double.parseDouble(getAmountAvailableForInvestment_N())
							- Double.parseDouble(getTotalCharges_S())
							+ Double.parseDouble(getAdditionToFundIfAny_V())
							- Double.parseDouble(getTotalServiceTax_ExclOfSTonAllocAndSurr_T()) + Double
								.parseDouble(getRiderCharges_P()));*/
            this.fundBeforeFMC_W = ""
                    + (_fundValueAtEnd_AB
                    + Double.parseDouble(getAmountAvailableForInvestment_N())
                    - Double.parseDouble(getTotalCharges_S())
                    + Double.parseDouble(getAdditionToFundIfAny_V())
                    - Double.parseDouble(getTotalServiceTax_ExclOfSTonAllocAndSurr_T()));
        } else {
            this.fundBeforeFMC_W = "" + 0;
        }
    }

    public String getFundBeforeFMC_AL() {
        return fundBeforeFMC_AL;
    }

    public void setFundBeforeFMC_AL(double _fundValueAtEnd_AQ, int policyTerm) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.fundBeforeFMC_AL = ""
                    + (_fundValueAtEnd_AQ
                    + Double.parseDouble(getAmountAvailableForInvestment_N())
                    - Double.parseDouble(getTotalCharges_AH())
                    + Double.parseDouble(getAdditionToFundIfAny_AK())
                    - Double.parseDouble(getTotalServiceTax_ExclOfSTonAllocAndSurr_AI()));
        } else {
            this.fundBeforeFMC_AL = "" + 0;
        }
    }

    public String getFundManagementCharge_X() {
        return fundManagementCharge_X;
    }

    public void setFundManagementCharge_X(int policyTerm,
                                          double percentToBeInvested_EquityEliteIIFund,
                                          double percentToBeInvested_BalancedFund,
                                          double percentToBeInvested_BondFund,
                                          double percentToBeInvested_MoneyMarketFund,
                                          double percentToBeInvested_BondOptimiserFund,
                                          double percentToBeInvested_EquityFund,
                                          double percentToBeInvested_MidcapFund,
                                          double percentToBeInvested_PureFund) {
        double temp1 = 0, temp2 = 0;
        if (Integer.parseInt(getMonth_E()) == 1) {
            temp1 = SE_Prop.charge_Fund;
        } else {
            temp1 = 0;
        }
        temp2 = getCharge_fund_ren(percentToBeInvested_EquityEliteIIFund,
                percentToBeInvested_BalancedFund, percentToBeInvested_BondFund,
                percentToBeInvested_MoneyMarketFund,
                percentToBeInvested_BondOptimiserFund,
                percentToBeInvested_EquityFund,
                percentToBeInvested_MidcapFund,
                percentToBeInvested_PureFund) / 12;
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.fundManagementCharge_X = cfap.roundUp_Level2(cfap
                    .getStringWithout_E(Double
                            .parseDouble(getFundBeforeFMC_W())
                            * (temp1 + temp2)));
        } else {
            this.fundManagementCharge_X = "" + 0;
        }
    }

    public double getCharge_fund_ren(
            double percentToBeInvested_EquityEliteIIFund,
            double percentToBeInvested_BalancedFund,
            double percentToBeInvested_BondFund,
            double percentToBeInvested_MoneyMarketFund,
            double percentToBeInvested_BondOptimiserFund,
            double percentToBeInvested_EquityFund,
            double percentToBeInvested_MidcapFund,
            double percentToBeInvested_PureFund) {
        return percentToBeInvested_EquityEliteIIFund
                * SE_Prop.FMC_EquityEliteIIFund
                + percentToBeInvested_BalancedFund * SE_Prop.FMC_BalancedFund
                + percentToBeInvested_BondFund * SE_Prop.FMC_BondFund
                + percentToBeInvested_MoneyMarketFund
                * SE_Prop.FMC_MoneyMarketFund + percentToBeInvested_BondOptimiserFund
                * SE_Prop.FMC_BondOptimiserFund + percentToBeInvested_EquityFund
                * SE_Prop.FMC_EquityFund + percentToBeInvested_MidcapFund
                * SE_Prop.FMC_MidcapFund + percentToBeInvested_PureFund
                * SE_Prop.FMC_PureFund;
    }

    public String getFundManagementCharge_AM() {
        return fundManagementCharge_AM;
    }

    public void setFundManagementCharge_AM(int policyTerm,
                                           double percentToBeInvested_EquityEliteIIFund,
                                           double percentToBeInvested_BalancedFund,
                                           double percentToBeInvested_BondFund,
                                           double percentToBeInvested_MoneyMarketFund,
                                           double percentToBeInvested_BondOptimiserFund,
                                           double percentToBeInvested_EquityFund,
                                           double percentToBeInvested_MidcapFund,
                                           double percentToBeInvested_PureFund) {
        double temp1 = 0, temp2 = 0;
        if (Integer.parseInt(getMonth_E()) == 1) {
            temp1 = SE_Prop.charge_Fund;
        } else {
            temp1 = 0;
        }
        temp2 = getCharge_fund_ren(percentToBeInvested_EquityEliteIIFund,
                percentToBeInvested_BalancedFund, percentToBeInvested_BondFund,
                percentToBeInvested_MoneyMarketFund,
                percentToBeInvested_BondOptimiserFund,
                percentToBeInvested_EquityFund,
                percentToBeInvested_MidcapFund,
                percentToBeInvested_PureFund) / 12;
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.fundManagementCharge_AM = cfap.roundUp_Level2(cfap
                    .getStringWithout_E(Double
                            .parseDouble(getFundBeforeFMC_AL())
                            * (temp1 + temp2)));
        } else {
            this.fundManagementCharge_AM = "" + 0;
        }
    }

    public String getFundValueAfterFMCandBeforeGA_AO() {
        return fundValueAfterFMCandBeforeGA_AO;
    }

    public void setFundValueAfterFMCandBeforeGA_AO(int policyTerm) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.fundValueAfterFMCandBeforeGA_AO = ""
                    + (Double.parseDouble(getFundBeforeFMC_AL())
                    - Double.parseDouble(getFundManagementCharge_AM()) - Double
                    .parseDouble(getServiceTaxOnFMC_AN()));
        } else {
            this.fundValueAfterFMCandBeforeGA_AO = "" + 0;
        }
    }

    public String getFundValueAfterFMCandBeforeGA_Z() {
        return fundValueAfterFMCandBeforeGA_Z;
    }

    public void setFundValueAfterFMCandBeforeGA_Z(int policyTerm) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.fundValueAfterFMCandBeforeGA_Z = ""
                    + cfap
                    .roundUp_Level2(cfap.getStringWithout_E(Double.parseDouble(getFundBeforeFMC_W())
                            - Double.parseDouble(getFundManagementCharge_X()) - Double
                            .parseDouble(getServiceTaxOnFMC_Y())));
        } else {
            this.fundValueAfterFMCandBeforeGA_Z = "" + 0;
        }
    }

    public String getFundValueAtEnd_AQ() {
        return fundValueAtEnd_AQ;
    }

    public void setFundValueAtEnd_AQ() {
        this.fundValueAtEnd_AQ = ""
                + (Double.parseDouble(getGuaranteedAddition_AP()) + Double
                .parseDouble(getFundValueAfterFMCandBeforeGA_AO()));
    }

    public String getFundValueAtEnd_AB() {
        return fundValueAtEnd_AB;
    }

    public void setFundValueAtEnd_AB() {
        this.fundValueAtEnd_AB = ""
                + (Double.parseDouble(getGuaranteedAddition_AA()) + Double
                .parseDouble(getFundValueAfterFMCandBeforeGA_Z()));
    }

    public String getGuaranteeCharge_AW() {
        return guaranteeCharge_AW;
    }

    public void setGuaranteeCharge_AW(boolean mortalityCharges,
                                      double charge_Guarantee) {
        if (getPolicyInForce_G().equals("Y") && mortalityCharges) {
            this.guaranteeCharge_AW = ""
                    + (Double.parseDouble(getDailyProtectFundAtStart_BC())
                    * charge_Guarantee / 12);
        } else {
            this.guaranteeCharge_AW = "" + 0;
        }
    }

    public String getGuaranteeCharge_U() {
        return guaranteeCharge_U;
    }

    public void setGuaranteeCharge_U(boolean guaranteeCharges,
                                     double charge_Guarantee) {
        if (getPolicyInForce_G().equals("Y") && guaranteeCharges) {
            this.guaranteeCharge_U = ""
                    + (Double.parseDouble(getDailyProtectFundAtStart_AA())
                    * charge_Guarantee / 12);
        } else {
            this.guaranteeCharge_U = "" + 0;
        }
    }

    public String getGuaranteedAddition_AA() {
        return guaranteedAddition_AA;
    }

    public void setGuaranteedAddition_AA() {
        this.guaranteedAddition_AA = "" + 0;
    }

    public String getGuaranteedAddition_AP() {
        return guaranteedAddition_AP;
    }

    public void setGuaranteedAddition_AP() {
        this.guaranteedAddition_AP = "" + 0;
    }

    public String getIndexFundAtEnd_AP() {
        return indexFundAtEnd_AP;
    }

    public void setIndexFundAtEnd_AP(boolean fundManagementCharges,
                                     double indexFund, double serviceTax) {
        if (getPolicyInForce_G().equals("Y")) {
            double a = 0;
            if (fundManagementCharges) {
                a = Double.parseDouble(getFMCearnedOnIndexFund_AH())
                        * (0.0135 / indexFund);
            } else {
                a = 0;
            }
            this.indexFundAtEnd_AP = ""
                    + (Double.parseDouble(getIndexFundAtStartAfterCharges_AD())
                    + Double.parseDouble(getAdditionToIndexFund_AF())
                    - Double.parseDouble(getFMCearnedOnIndexFund_AH()) - a
                    * serviceTax);
        } else {
            this.indexFundAtEnd_AP = "" + 0;
        }
    }

    public String getIndexFundAtEnd_BR() {
        return indexFundAtEnd_BR;
    }

    public void setIndexFundAtEnd_BR(boolean fundManagementCharges,
                                     double indexFund, double serviceTax) {
        double a = 0;
        if (fundManagementCharges) {
            a = Double.parseDouble(getFMCearnedOnIndexFund_BJ())
                    * (0.0135 / indexFund);
        } else {
            a = 0;
        }
        if (getPolicyInForce_G().equals("Y")) {
            this.indexFundAtEnd_BR = ""
                    + (Double.parseDouble(getIndexFundAtStartAfterCharges_BF())
                    + Double.parseDouble(getAdditionToIndexFund_BH())
                    - Double.parseDouble(getFMCearnedOnIndexFund_BJ()) - a
                    * serviceTax);
        } else {
            this.indexFundAtEnd_BR = "" + 0;
        }
    }

    public String getIndexFundAtStartAfterCharges_AD() {
        return indexFundAtStartAfterCharges_AD;
    }

    public void setIndexFundAtStartAfterCharges_AD(boolean guaranteeCharges,
                                                   double serviceTax) {
        if (getPolicyInForce_G().equals("Y")) {
            double a = 0;
            if (guaranteeCharges) {
                a = serviceTax;
            } else {
                a = 0;
            }
            this.indexFundAtStartAfterCharges_AD = cfap
                    .roundUp_Level2(cfap.getStringWithout_E(Double
                            .parseDouble(getIndexFundAtStart_AB())
                            - ((Double.parseDouble(getTotalCharges_S())
                            + Double.parseDouble(getTotalServiceTax_ExclOfSTonAllocAndSurr_T()) - Double
                            .parseDouble(getGuaranteeCharge_U())
                            * (1 + a))
                            * Double.parseDouble(getIndexFundAtStart_AB()) / (Double
                            .parseDouble(getDailyProtectFundAtStart_AA()) + Double
                            .parseDouble(getIndexFundAtStart_AB())))));
        } else {
            this.indexFundAtStartAfterCharges_AD = "" + 0;
        }
    }

    public String getIndexFundAtStartAfterCharges_BF() {
        return indexFundAtStartAfterCharges_BF;
    }

    public void setIndexFundAtStartAfterCharges_BF(boolean guaranteeCharges,
                                                   double serviceTax) {
        double a = 0;
        if (guaranteeCharges) {
            a = serviceTax;
        } else {
            a = 0;
        }
        if (getPolicyInForce_G().equals("Y")) {
            this.indexFundAtStartAfterCharges_BF = cfap
                    .roundUp_Level2(cfap.getStringWithout_E(Double
                            .parseDouble(getIndexFundAtStart_BD())
                            - ((Double.parseDouble(getTotalCharges_AH())
                            + Double.parseDouble(getTotalServiceTax_ExclOfSTonAllocAndSurr_AI()) - Double
                            .parseDouble(getGuaranteeCharge_AW())
                            * (1 + a))
                            * Double.parseDouble(getIndexFundAtStart_BD()) / (Double
                            .parseDouble(getDailyProtectFundAtStart_BC()) + Double
                            .parseDouble(getIndexFundAtStart_BD())))));
        } else {
            this.indexFundAtStartAfterCharges_BF = "" + 0;
        }
    }

    public String getIndexFundAtStart_AB() {
        return indexFundAtStart_AB;
    }

    public void setIndexFundAtStart_AB(double _indexFundAtEnd_AP,
                                       double percentToBeInvestedForIndexFund) {
        if (getPolicyInForce_G().equals("Y")) {
            this.indexFundAtStart_AB = cfap
                    .roundUp_Level2(cfap.getStringWithout_E(Double
                            .parseDouble(getAmountAvailableForInvestment_N())
                            * percentToBeInvestedForIndexFund
                            + _indexFundAtEnd_AP
                            - Double.parseDouble(getTransferOfIndexFundGainToDailyProtectFund_Y())
                            - Double.parseDouble(getTransferOfCapitalFromIndexToDailyProtectFund_Z())));
        } else {
            this.indexFundAtStart_AB = "" + 0;
        }
    }

    public String getIndexFundAtStart_BD() {
        return indexFundAtStart_BD;
    }

    public void setIndexFundAtStart_BD(double _indexFundAtEnd,
                                       double percentToBeInvestedForIndexFund) {
        if (getPolicyInForce_G().equals("Y")) {
            this.indexFundAtStart_BD = cfap
                    .roundUp_Level2(cfap.getStringWithout_E(Double
                            .parseDouble(getAmountAvailableForInvestment_N())
                            * percentToBeInvestedForIndexFund
                            + _indexFundAtEnd
                            - Double.parseDouble(getTransferOfIndexFundGainToDailyProtectFund_BA())
                            - Double.parseDouble(getTransferOfCapitalFromIndexToDailyProtectFund_BB())));
        } else {
            this.indexFundAtStart_BD = "" + 0;
        }
    }

    public String getMonth_E() {
        return month_E;
    }

    public void setMonth_E(int rowNum) {
        this.month_E = ("" + rowNum);
    }

    public String getMortalityCharges_R() {
        return mortalityCharges_R;
    }

    public void setMortalityCharges_R(double _fundValueAtEnd_AB,
                                      int policyTerm, String[] forBIArray, int ageAtEntry,
                                      double sumAssured, boolean mortalityCharges, String planOption) {/*
		if (!(getPolicyInForce_G().equals("Y"))
				|| Integer.parseInt(getYear_F()) > policyTerm) {
			this.mortalityCharges_R = "" + 0;
		} else {
			double arrOutput = Double
					.parseDouble(forBIArray[(ageAtEntry + Integer
							.parseInt(getYear_F())) - 1]);
			// double a=1-arrOutput/1000;
			double div = arrOutput / 1000;
			double div1 = div / 12;
			double a = 1 - div1;
			// double b=(double)1/12;
			double c = 0;
			if (planOption.equals("Gold")) {
				c = Math.max(
						sumAssured,
						Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()))
						- (Double
								.parseDouble(getAmountAvailableForInvestment_N()) + _fundValueAtEnd_AB);
			} else {
				c = Math.max(
						sumAssured,
						(Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()) - _fundValueAtEnd_AB));
			}
			int d = 0;
			if (mortalityCharges) {
				d = 1;
			} else {
				d = 0;
			}
			double e = Math.min(sumAssured, 5000000);
			double f = SE_Prop.ADBandATPDCharge / 12;
			this.mortalityCharges_R = cfap
					.roundUp_Level2(cfap.getStringWithout_E((1 - a)
							* Math.max(c, 0) + (e * f) * d));
		}
	*/

        if (!(getPolicyInForce_G().equals("Y")) || Integer.parseInt(getYear_F()) > policyTerm) {
            this.mortalityCharges_R = "" + 0;
        } else {
            double arrOutput = Double.parseDouble(forBIArray[Integer.parseInt(getAge_H())]);


//            System.out.println("**********arroutput "+arrOutput);
            //double div=arrOutput/1000;
            //System.out.println("div "+div);
//            double a=arrOutput/12;
            //System.out.println("a "+a);

            DecimalFormat df = new DecimalFormat("#.#######");

            String roundTo7 = df.format(arrOutput);
            String test = cfap.getRoundOffLevel5(roundTo7);

//            System.out.println("arroutput RY "+arrOutput);
//            System.out.println("round to 5 RY"+test);

            double a = Double.parseDouble(test) / 12;
            /*double max1=Math.max(sumAssured, Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()));

//            System.out.println("max1 "+max1);

            double b=(max1-(Double.parseDouble(getAmountAvailableForInvestment_N())+_fundValueAtEnd_AB));*/

            double b = 0;

            if (planOption.equals("Gold")) {
                b = Math.max(
                        sumAssured,
                        Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()))
                        - (Double
                        .parseDouble(getAmountAvailableForInvestment_N()) + _fundValueAtEnd_AB);
            } else {
                b = Math.max(
                        sumAssured,
                        (Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()) - _fundValueAtEnd_AB));
            }
//            System.out.println("Minus amount" + (Double.parseDouble(getAmountAvailableForInvestment_N())+_fundValueAtEnd_AB));
//            System.out.println("N" + getAmountAvailableForInvestment_N());
//            System.out.println("AB" + _fundValueAtEnd_AB);

//            System.out.println("b "+b);

            int c = 0;
            if (mortalityCharges) {
                c = 1;
            } else {
                c = 0;
            }
//            this.mortalityCharges_R= cfap.roundUp_Level2(cfap.getStringWithout_E(((a*Math.max(b, 0))*c)));
            this.mortalityCharges_R = cfap.getRoundOffLevel2New(cfap.getStringWithout_E(((a * Math.max(b, 0)) * c)));
        }

    }

    public String getMortalityCharges_AG() {
        return mortalityCharges_AG;
    }

    public void setMortalityCharges_AG(double _fundValueAtEnd_AQ,
                                       int policyTerm, String[] forBIArray, int ageAtEntry,
                                       double sumAssured, boolean mortalityCharges, String planOption) {/*
		if (!(getPolicyInForce_G().equals("Y"))
				|| Integer.parseInt(getYear_F()) > policyTerm) {
			this.mortalityCharges_AG = "" + 0;
		} else {
			double arrOutput = Double
					.parseDouble(forBIArray[(ageAtEntry + Integer
							.parseInt(getYear_F())) - 1]);
			// double a=1-arrOutput/1000;
			double div = arrOutput / 1000;
			double div1 = div / 12;
			double a = 1 - div1;
			// double b=(double)1/12;
			double c = 0;
			if (planOption.equals("Gold")) {
				c = Math.max(
						sumAssured,
						Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()))
						- (Double
								.parseDouble(getAmountAvailableForInvestment_N()) + _fundValueAtEnd_AQ);
			} else {
				c = Math.max(
						sumAssured,
						(Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()) - _fundValueAtEnd_AQ));
			}
			int d = 0;
			if (mortalityCharges) {
				d = 1;
			} else {
				d = 0;
			}
			double e = Math.min(sumAssured, 5000000);
			double f = SE_Prop.ADBandATPDCharge / 12;
			this.mortalityCharges_AG = cfap.roundUp_Level2(cfap
					.getStringWithout_E((1 - (a)) * Math.max(c, 0) + (e * f)
							* d));
		}
	*/

        if (!(getPolicyInForce_G().equals("Y")) || Integer.parseInt(getYear_F()) > policyTerm) {
            this.mortalityCharges_AG = "" + 0;
        } else {
            double arrOutput = Double.parseDouble(forBIArray[Integer.parseInt(getAge_H())]);
            //System.out.println("arroutput "+arrOutput);
            //double div=arrOutput/1000;
//            double a=arrOutput/12;
            //System.out.println("a "+a);


            DecimalFormat df = new DecimalFormat("#.#######");

            String roundTo7 = df.format(arrOutput);
            String test = cfap.getRoundOffLevel5(roundTo7);

//            System.out.println("arroutput RY "+arrOutput);
//            System.out.println("round to 5 RY"+test);

            double a = Double.parseDouble(test) / 12;

            /*double max1=Math.max(sumAssured, Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()));
            //System.out.println("max1 "+max1);
            double b=(max1-(Double.parseDouble(getAmountAvailableForInvestment_N())+_fundValueAtEnd_AQ));*/
            //System.out.println("b "+b);

            double b = 0;
            if (planOption.equals("Gold")) {

                b = Math.max(
                        sumAssured,
                        Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()))
                        - (Double
                        .parseDouble(getAmountAvailableForInvestment_N()) + _fundValueAtEnd_AQ);
            } else {
                b = Math.max(
                        sumAssured,
                        (Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()) - _fundValueAtEnd_AQ));
            }

            int c = 0;
            if (mortalityCharges) {
                c = 1;
            } else {
                c = 0;
            }
//            this.mortalityCharges_AG= cfap.roundUp_Level2(cfap.getStringWithout_E(((a*Math.max(b, 0))*c)));
            this.mortalityCharges_AG = cfap.getRoundOffLevel2New(cfap.getStringWithout_E(((a * Math.max(b, 0)) * c)));
        }

    }

    public String getOneHundredPercentOfCummulativePremium_AW() {
        return oneHundredPercentOfCummulativePremium_AW;
    }

    public void setOneHundredPercentOfCummulativePremium_AW(
            double _oneHundredPercentOfCummulativePremium_AW) {
        double a = 0;
        if (getPolicyInForce_G().equals("Y")) {
            a = Double.parseDouble(getPremium_I())
                    + (_oneHundredPercentOfCummulativePremium_AW / 1.05);
        } else {
            a = 0;
        }
        this.oneHundredPercentOfCummulativePremium_AW = "" + (a * 1.05);
    }

    public String getOptionCharges_R() {
        return optionCharges_R;
    }

    public void setOptionCharges_R(boolean riderCharges,
                                   boolean eligibilityRider, int policyTermADB, double ADBrate,
                                   double SA_ADB) {
        if (riderCharges && eligibilityRider
                && (Integer.parseInt(getYear_F()) <= policyTermADB)) {
            String dummy = "" + (ADBrate * SA_ADB / 12);
            this.optionCharges_R = dummy;
        } else {
            this.optionCharges_R = "" + 0;
        }
    }

    public String getPolicyAdministrationCharge_Q() {
        return policyAdministrationCharge_Q;
    }

    public void setPolicyAdministrationCharge_Q(
            double _policyAdministrationCharge_Q, int charge_Inflation,
            int fixedMonthlyExp_SP, int fixedMonthlyExp_RP,
            int inflation_pa_SP, int inflation_pa_RP, String premFreqMode) {
        if (getPolicyInForce_G().equals("Y")) {
            if (((Integer.parseInt(getMonth_E()) - 1) % 12) == 0) {
                this.policyAdministrationCharge_Q = cfap
                        .roundUp_Level2(cfap.getStringWithout_E(getCharge_PP_Ren(
                                fixedMonthlyExp_SP, fixedMonthlyExp_RP,
                                inflation_pa_SP, inflation_pa_RP, premFreqMode)
                                / 12
                                * cfap.getPowerOutput(
                                (1 + charge_Inflation),
                                Integer.parseInt(""
                                        + (Integer
                                        .parseInt(getMonth_E()) / 12)))));
            } else {
                this.policyAdministrationCharge_Q = cfap.roundUp_Level2(cfap
                        .getStringWithout_E(_policyAdministrationCharge_Q));
            }
        } else {
            this.policyAdministrationCharge_Q = "" + 0;
        }
    }

    public String getPolicyInForce_G() {
        return policyInForce_G;
    }

    public String getPremiumAllocationCharge_K() {
        return premiumAllocationCharge_K;
    }

    public void setPremiumAllocationCharge_K(boolean staffDisc,
                                             boolean bancAssuranceDisc, String premFreqmode) {
        if (Integer.parseInt(getYear_F()) > 10) {
            this.premiumAllocationCharge_K = ("" + 0);
        } else {
            this.premiumAllocationCharge_K = cfap.getRoundUp(cfap
                    .getStringWithout_E(getAllocationCharge(staffDisc,
                            bancAssuranceDisc, premFreqmode)
                            * Double.parseDouble(getPremium_I())));
        }
    }

    public String getPremium_I() {
        return premium_I;
    }

    public void setPremium_I(int premiumPayingTerm, int PF, double effectivePrem) {
        if (getPolicyInForce_G().equals("Y")) {
            if ((Integer.parseInt(getYear_F()) <= premiumPayingTerm)
                    && (((Integer.parseInt(getMonth_E()) - 1) % (12 / PF)) == 0)) {
                premium_I = "" + (effectivePrem / PF);
//				premium_I = "" + (effectivePrem);
            } else {
                premium_I = ("" + 0);
            }
        } else {
            premium_I = ("" + 0);
        }
    }

    public String getServiceTaxOnAllocation_M() {
        return serviceTaxOnAllocation_M;
    }

    public void setServiceTaxOnAllocation_M(boolean allocationCharges,
                                            double serviceTax) {
        if (allocationCharges) {
            this.serviceTaxOnAllocation_M = cfap
                    .roundUp_Level2(cfap.getStringWithout_E((Double
                            .parseDouble(getPremiumAllocationCharge_K()) + Double
                            .parseDouble(getTopUpCharges_L()))
                            * serviceTax));
        } else {
            this.serviceTaxOnAllocation_M = ("" + 0);
        }
    }

    public String getServiceTaxOnFMC_Y() {
        return serviceTaxOnFMC_Y;
    }

    public void setServiceTaxOnFMC_Y(boolean fundManagementCharges,
                                     double serviceTax, double indexFund,
                                     double percentToBeInvested_EquityEliteIIFund,
                                     double percentToBeInvested_BalancedFund,
                                     double percentToBeInvested_BondFund,
                                     double percentToBeInvested_MoneyMarketFund,
                                     double percentToBeInvested_BondOptimiserFund,
                                     double percentToBeInvested_EquityFund,
                                     double percentToBeInvested_MidcapFund,
                                     double percentToBeInvested_PureFund) {
        double a = 0;
        if (fundManagementCharges) {
            a = (Double.parseDouble(getFundManagementCharge_X()) * getCharge_fund_ren(
                    percentToBeInvested_EquityEliteIIFund,
                    percentToBeInvested_BalancedFund,
                    percentToBeInvested_BondFund,
                    percentToBeInvested_MoneyMarketFund,
                    percentToBeInvested_BondOptimiserFund,
                    percentToBeInvested_EquityFund,
                    percentToBeInvested_MidcapFund,
                    percentToBeInvested_PureFund) / getCharge_fund_ren(percentToBeInvested_EquityEliteIIFund,
                    percentToBeInvested_BalancedFund,
                    percentToBeInvested_BondFund,
                    percentToBeInvested_MoneyMarketFund,
                    percentToBeInvested_BondOptimiserFund,
                    percentToBeInvested_EquityFund,
                    percentToBeInvested_MidcapFund,
                    percentToBeInvested_PureFund));
        } else {
            a = 0;
        }
        this.serviceTaxOnFMC_Y = cfap.roundUp_Level2(cfap.getStringWithout_E(a
                * serviceTax));
    }

    public String getServiceTaxOnFMC_AN() {
        return serviceTaxOnFMC_AN;
    }

    public void setServiceTaxOnFMC_AN(boolean fundManagementCharges,
                                      double serviceTax, double indexFund,
                                      double percentToBeInvested_EquityEliteIIFund,
                                      double percentToBeInvested_BalancedFund,
                                      double percentToBeInvested_BondFund,
                                      double percentToBeInvested_MoneyMarketFund,
                                      double percentToBeInvested_BondOptimiserFund,
                                      double percentToBeInvested_EquityFund,
                                      double percentToBeInvested_MidcapFund,
                                      double percentToBeInvested_PureFund) {
        double a = 0;
        if (fundManagementCharges) {
            a = (Double.parseDouble(getFundManagementCharge_AM()) * getCharge_fund_ren(
                    percentToBeInvested_EquityEliteIIFund,
                    percentToBeInvested_BalancedFund,
                    percentToBeInvested_BondFund,
                    percentToBeInvested_MoneyMarketFund,
                    percentToBeInvested_BondOptimiserFund,
                    percentToBeInvested_EquityFund,
                    percentToBeInvested_MidcapFund,
                    percentToBeInvested_PureFund) / getCharge_fund_ren(
                    percentToBeInvested_EquityEliteIIFund,
                    percentToBeInvested_BalancedFund,
                    percentToBeInvested_BondFund,
                    percentToBeInvested_MoneyMarketFund,
                    percentToBeInvested_BondOptimiserFund,
                    percentToBeInvested_EquityFund,
                    percentToBeInvested_MidcapFund,
                    percentToBeInvested_PureFund));
        } else {
            a = 0;
        }
        this.serviceTaxOnFMC_AN = cfap.roundUp_Level2(cfap.getStringWithout_E(a
                * serviceTax));
    }

    public String getServiceTaxOnSurrenderCharges_AD() {
        return serviceTaxOnSurrenderCharges_AD;
    }

    public void setServiceTaxOnSurrenderCharges_AD(boolean surrenderCharges,
                                                   double serviceTax) {
        if (surrenderCharges) {
            this.serviceTaxOnSurrenderCharges_AD = cfap
                    .roundUp_Level2(cfap.getStringWithout_E(Double
                            .parseDouble(getSurrenderCharges_AC()) * serviceTax));
        } else {
            this.serviceTaxOnSurrenderCharges_AD = "" + 0;
        }
    }

    public String getServiceTaxOnSurrenderCharges_AS() {
        return serviceTaxOnSurrenderCharges_AS;
    }

    public void setServiceTaxOnSurrenderCharges_AS(boolean surrenderCharges,
                                                   double serviceTax) {
        if (surrenderCharges) {
            this.serviceTaxOnSurrenderCharges_AS = cfap
                    .roundUp_Level2(cfap.getStringWithout_E(Double
                            .parseDouble(getSurrenderCharges_AR()) * serviceTax));
        } else {
            this.serviceTaxOnSurrenderCharges_AS = "" + 0;
        }
    }

    public String getSumAssuredRelatedCharges_O() {
        return sumAssuredRelatedCharges_O;
    }

    public void setSumAssuredRelatedCharges_O(int policyTerm,
                                              double sumAssured, double SAMF, double effectivePremium,
                                              double charge_SumAssuredBase) {
        double a = 0;
        if (getMonth_E().equals("1")) {
            a = charge_SumAssuredBase;
        } else {
            a = 0;
        }
        double b = 0;
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            b = charge_SumAssuredBase / 12;
        } else {
            b = 0;
        }
        this.sumAssuredRelatedCharges_O = cfap.roundUp_Level2(cfap
                .getStringWithout_E(sumAssured * (a + b)));
    }

    public String getSurrenderCap_AV() {
        return surrenderCap_AV;
    }

    public void setSurrenderCap_AV(double effectivePremium, String premFreq) {
		/*if (getPolicyInForce_G().equals("Y")) {
			this.surrenderCap_AV = ""
					+ (Double.parseDouble(calCapArr(effectivePremium)[Integer
							.parseInt(getYear_F()) - 1]));
		} else {
			this.surrenderCap_AV = "" + 0;
		}*/

        double a = 0;
        if (premFreq.equals("Single")) {

            if (effectivePremium > 25000) {

                if (effectivePremium > 300000) {

                    if (getYear_F().equals("1")) {
                        a = 6000;
                    } else if (getYear_F().equals("2")) {
                        a = 5000;
                    } else if (getYear_F().equals("3")) {
                        a = 4000;
                    } else if (getYear_F().equals("4")) {
                        a = 2000;
                    } else {
                        a = 0;
                    }
                } else {

                    if (getYear_F().equals("1")) {
                        a = 3000;
                    } else if (getYear_F().equals("2")) {
                        a = 2000;
                    } else if (getYear_F().equals("3")) {
                        a = 1500;
                    } else if (getYear_F().equals("4")) {
                        a = 1000;
                    } else {
                        a = 0;
                    }
                }
            } else {
        		/*if(getYear_F().equals("1"))
             	{
             		a = 3000;
             	}
             	else if(getYear_F().equals("2"))
             	{
             		a = 2000;
             	}
             	else if(getYear_F().equals("3"))
             	{
             		a = 1500;
             	}
             	else if(getYear_F().equals("4"))
             	{
             		a = 1000;
		}
             	else
             	{
             		a=0;
             	}*/
                a = 0;
            }
        } else {
            if (effectivePremium > 25000) {

                if (getYear_F().equals("1")) {
                    a = 6000;
                } else if (getYear_F().equals("2")) {
                    a = 5000;
                } else if (getYear_F().equals("3")) {
                    a = 4000;
                } else if (getYear_F().equals("4")) {
                    a = 2000;
                } else {
                    a = 0;
                }
            } else {/*
        		if(getYear_F().equals("1"))
                 	{
                 		a = 3000;
                 	}
                 	else if(getYear_F().equals("2"))
                 	{
                 		a = 2000;
                 	}
                 	else if(getYear_F().equals("3"))
                 	{
                 		a = 1500;
                 	}
                 	else if(getYear_F().equals("4"))
                 	{
                 		a = 1000;
                 	}
                 	else
                 	{
                 		a=0;
                 	}
        	*/
                a = 0;
            }
        }

        this.surrenderCap_AV = "" + a;
    }

    public String getSurrenderCharges_AC() {
        return surrenderCharges_AC;
    }

    public void setSurrenderCharges_AC(double effectivePremium,
                                       String premFreq) {
        double a = Math.min(Double.parseDouble(getFundValueAtEnd_AB()),
                effectivePremium);
        double b = getSurrenderCharge(premFreq, effectivePremium);
        this.surrenderCharges_AC = cfap.roundUp_Level2(cfap
                .getStringWithout_E(Math.min((a * b),
                        Double.parseDouble(getSurrenderCap_AV()))));
    }

    public String getSurrenderCharges_AR() {
        return surrenderCharges_AR;
    }

    public void setSurrenderCharges_AR(double effectivePremium,
                                       String premFreq) {
        double a = Math.min(Double.parseDouble(getFundValueAtEnd_AQ()),
                effectivePremium);
        double b = getSurrenderCharge(premFreq, effectivePremium);
        this.surrenderCharges_AR = cfap.roundUp_Level2(cfap
                .getStringWithout_E(Math.min((a * b),
                        Double.parseDouble(getSurrenderCap_AV()))));
    }

    public String getSurrenderValue_AE() {
        return surrenderValue_AE;
    }

    public void setSurrenderValue_AE() {
        this.surrenderValue_AE = ""
                + (Double.parseDouble(getFundValueAtEnd_AB())
                - Double.parseDouble(getSurrenderCharges_AC()) - Double
                .parseDouble(getServiceTaxOnSurrenderCharges_AD()));
    }

    public String getSurrenderValue_AT() {
        return surrenderValue_AT;
    }

    public void setSurrenderValue_AT() {
        this.surrenderValue_AT = ""
                + cfap
                .roundUp_Level2(cfap.getStringWithout_E((Double.parseDouble(getFundValueAtEnd_AQ())
                        - Double.parseDouble(getSurrenderCharges_AR()) - Double
                        .parseDouble(getServiceTaxOnSurrenderCharges_AS()))));
    }

    public String getTopUpCharges_L() {
        return topUpCharges_L;
    }

    public void setTopUpCharges_L(double topUp) {
        this.topUpCharges_L = cfap.getRoundUp(""
                + (Double.parseDouble(getTopUpPremium_J()) * topUp));
    }

    public String getTotalCharges_AH() {
        return totalCharges_AH;
    }

    public void setTotalCharges_AH(int policyTerm) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.totalCharges_AH = ""
                    + (Double.parseDouble(getPolicyAdministrationCharge_Q())
                    + Double.parseDouble(getMortalityCharges_AG())
                    + Double.parseDouble(getSumAssuredRelatedCharges_O()) + Double
                    .parseDouble(getRiderCharges_P()));
        } else {
            this.totalCharges_AH = "" + 0;
        }
    }

    public String getTotalCharges_S() {
        return totalCharges_S;
    }

    public void setTotalCharges_S(int policyTerm) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
			/*System.out.println("getPolicyAdministrationCharge_Q()"+getPolicyAdministrationCharge_Q());
			System.out.println("getMortalityCharges_R()"+getMortalityCharges_R());
			System.out.println("getSumAssuredRelatedCharges_O()"+getSumAssuredRelatedCharges_O());
			System.out.println("getRiderCharges_P()"+getRiderCharges_P());*/
            this.totalCharges_S = ""
                    + (Double.parseDouble(getPolicyAdministrationCharge_Q())
                    + Double.parseDouble(getMortalityCharges_R())
                    + Double.parseDouble(getSumAssuredRelatedCharges_O()) + Double
                    .parseDouble(getRiderCharges_P()));
        } else {
            this.totalCharges_S = "" + 0;
        }
    }

    public String getTotalServiceTax_AJ() {
        return totalServiceTax_AJ;
    }

    public void setTotalServiceTax_AJ(double serviceTax) {
        double temp = 0;
        if (SE_Prop.riderCharges) {
            temp = 0;
        } else {
            temp = 0;
        }
        this.totalServiceTax_AJ = cfap
                .roundUp_Level2(cfap.getStringWithout_E(Double
                        .parseDouble(getServiceTaxOnAllocation_M())
                        + Double.parseDouble(getTotalServiceTax_ExclOfSTonAllocAndSurr_AI())
                        + Double.parseDouble(getServiceTaxOnFMC_AN())
                        + temp
                        * serviceTax));
    }

    public String getTotalServiceTax_ExclOfSTonAllocAndSurr_AI() {
        return totalServiceTax_ExclOfSTonAllocAndsurr_AI;
    }

    public void setTotalServiceTax_ExclOfSTonAllocAndSurr_AI(double serviceTax,
                                                             boolean mortalityAndRiderCharges,
                                                             boolean administrationAndSArelatedCharges) {
        double a = 0, b = 0, c = 0;
        if (mortalityAndRiderCharges) {
            a = Double.parseDouble(getMortalityCharges_AG());
        } else {
            a = 0;
        }
        if (administrationAndSArelatedCharges) {
            b = Double.parseDouble(getPolicyAdministrationCharge_Q());
        } else {
            b = 0;
        }
        this.totalServiceTax_ExclOfSTonAllocAndsurr_AI = cfap
                .roundUp_Level2(cfap.getStringWithout_E((a + b + Double.parseDouble(getRiderCharges_P())) * serviceTax));
    }

    public String getTotalServiceTax_U() {
        return totalServiceTax_U;
    }

    public void setTotalServiceTax_U(double serviceTax) {
        double temp = 0;
        if (SE_Prop.riderCharges) {
            temp = 0;
        } else {
            temp = 0;
        }
        this.totalServiceTax_U = cfap
                .roundUp_Level2(cfap.getStringWithout_E(Double
                        .parseDouble(getServiceTaxOnAllocation_M())
                        + Double.parseDouble(getTotalServiceTax_ExclOfSTonAllocAndSurr_T())
                        + Double.parseDouble(getServiceTaxOnFMC_Y())
                        + temp
                        * serviceTax));
    }

    public String getTotalServiceTax_ExclOfSTonAllocAndSurr_T() {
        return totalServiceTax_ExclOfSTonAllocAndSurr_T;
    }

    public void setTotalServiceTax_exclOfSTonAllocAndSurr_T(double serviceTax,
                                                            boolean mortalityAndRiderCharges,
                                                            boolean administrationAndSArelatedCharges) {
        double a = 0, b = 0, c = 0;
        if (mortalityAndRiderCharges) {
            a = Double.parseDouble(getMortalityCharges_R());
        } else {
            a = 0;
        }
        if (administrationAndSArelatedCharges) {
            b = Double.parseDouble(getPolicyAdministrationCharge_Q());
        } else {
            b = 0;
        }
        this.totalServiceTax_ExclOfSTonAllocAndSurr_T = cfap
                .roundUp_Level2(cfap.getStringWithout_E((a + b + Double.parseDouble(getRiderCharges_P())) * serviceTax));
    }

    public String getTransferOfCapitalFromIndexToDailyProtectFund_BB() {
        return transferOfCapitalFromIndexToDailyProtectFund_BB;
    }

    public void setTransferOfCapitalFromIndexToDailyProtectFund_BB(double br18) {
        if (getPolicyInForce_G().equals("Y")) {
            this.transferOfCapitalFromIndexToDailyProtectFund_BB = ""
                    + (br18 * Double.parseDouble(getTransferPercentIfAny_O()));
        } else {
            this.transferOfCapitalFromIndexToDailyProtectFund_BB = "" + 0;
        }
    }

    public String getTransferOfCapitalFromIndexToDailyProtectFund_Z() {
        return transferOfCapitalFromIndexToDailyProtectFund_Z;
    }

    public void setTransferOfCapitalFromIndexToDailyProtectFund_Z(
            double _indexFundAtEnd) {
        if (getPolicyInForce_G().equals("Y")) {
            // System.out.println("$ Double.parseDouble(getTransferPercentIfAny_O()) : "+Double.parseDouble(getTransferPercentIfAny_O())
            // );
            // System.out.println("$ _indexFundAtEnd : "+_indexFundAtEnd );
            this.transferOfCapitalFromIndexToDailyProtectFund_Z = ""
                    + (Double.parseDouble(getTransferPercentIfAny_O()) * _indexFundAtEnd);
        }
    }

    public String getTransferOfIndexFundGainToDailyProtectFund_BA() {
        return transferOfIndexFundGainToDailyProtectFund_BA;
    }

    public void setTransferOfIndexFundGainToDailyProtectFund_BA(
            double _indexFundAtEnd, double thresholdLimitForTransfOfGain,
            int noOfYrsAllowForTransfOfGain) {
        double a = 0;
        if ((_indexFundAtEnd * (1 - Double
                .parseDouble(getTransferPercentIfAny_O()))) >= (Double
                .parseDouble(getAllocatedFundToIndexFund_P()) * (1 + thresholdLimitForTransfOfGain))) {
            a = _indexFundAtEnd
                    - Double.parseDouble(getAllocatedFundToIndexFund_P());
        } else {
            a = 0;
        }
        if (getPolicyInForce_G().equals("Y")) {
            if (Double.parseDouble(getAllocatedFundToIndexFund_P()) > 0
                    && Integer.parseInt(getMonth_E()) <= noOfYrsAllowForTransfOfGain * 12) {
                this.transferOfIndexFundGainToDailyProtectFund_BA = "" + a;
            } else {
                this.transferOfIndexFundGainToDailyProtectFund_BA = "" + 0;
            }
        } else {
            this.transferOfIndexFundGainToDailyProtectFund_BA = "" + 0;
        }
    }

    public String getTransferOfIndexFundGainToDailyProtectFund_Y() {
        return transferOfIndexFundGainToDailyProtectFund_Y;
    }

    public void setTransferOfIndexFundGainToDailyProtectFund_Y(
            double _indexFundAtEnd, int noOfYrsAllowForTransfOfGain,
            double thresholdLimitForTransfOfGain) {
        if (getPolicyInForce_G().equals("Y")) {
            if (Double.parseDouble(getAllocatedFundToIndexFund_P()) > 0
                    && Integer.parseInt(getMonth_E()) <= (noOfYrsAllowForTransfOfGain * 12)) {
                if ((_indexFundAtEnd * (1 - Double
                        .parseDouble(getTransferPercentIfAny_O()))) >= (Double
                        .parseDouble(getAllocatedFundToIndexFund_P()) * (1 + thresholdLimitForTransfOfGain))) {
                    this.transferOfIndexFundGainToDailyProtectFund_Y = ""
                            + (_indexFundAtEnd - Double
                            .parseDouble(getAllocatedFundToIndexFund_P()));
                } else {
                    this.transferOfIndexFundGainToDailyProtectFund_Y = "" + 0;
                }
            } else {
                this.transferOfIndexFundGainToDailyProtectFund_Y = "" + 0;
            }
        } else {
            this.transferOfIndexFundGainToDailyProtectFund_Y = "" + 0;
        }
    }

    public String getTransferPercentIfAny_O() {
        return transferPercentIfAny_O;
    }

    public void setTransferPercentIfAny_O(int year_TransferOfCapital_W62,
                                          int year_TransferOfCapital_W63, String transferFundStatus) {
        double x62 = 0;
        if (getMonth_E().equals("" + (12 * year_TransferOfCapital_W62 - 6))) // (Month=(12*Input!$W$62-6))
        {
            if (transferFundStatus.equals("No Transfer")) {
                this.transferPercentIfAny_O = "" + 0;
            } else if (transferFundStatus.equals("6th Year")) {
                this.transferPercentIfAny_O = "" + 0;
            } else {
                x62 = 0.5;
                this.transferPercentIfAny_O = "" + 0.5;
            }
        } else if (getMonth_E().equals(
                "" + (12 * year_TransferOfCapital_W63 - 6))) {
            if (transferFundStatus.equals("5th Year")
                    || transferFundStatus.equals("No Transfer")) {
                this.transferPercentIfAny_O = "" + 0;
            } else if (x62 > 0) {
                this.transferPercentIfAny_O = "" + 1;
            } else {
                this.transferPercentIfAny_O = "" + 0.5;
            }
        } else {
            this.transferPercentIfAny_O = "" + 0;
        }
    }

    public String getYear_F() {
        return year_F;
    }

    public void setYear_F() {
        this.year_F = cfap.getRoundUp(""
                + (Double.parseDouble(getMonth_E()) / 12));
    }

    // *******************************************************************************************************

    public double getSurrenderCharge(String premFreqmode,
                                     double effectivePremium) {
        double surrenderCharge = 0;

		/*if (premFreqmode.equals("Limited")) {
			surrenderCharge = Double
					.parseDouble(calSurrRateArrLimited(effectivePremium)[Math
							.min((Integer.parseInt(getYear_F()) - 1), 11)]);
		}
		// For Single Mode of Premium Freqency
		else {
			surrenderCharge = Double.parseDouble(calSurrRateArrSingle()[Math
					.min((Integer.parseInt(getYear_F()) - 1), 11)]);
		}*/
        if (premFreqmode.equals("Single")) {

            if (effectivePremium > 25000) {

                if (effectivePremium > 300000) {

                    if (getYear_F().equals("1")) {
                        surrenderCharge = 0.01;
                    } else if (getYear_F().equals("2")) {
                        surrenderCharge = 0.007;
                    } else if (getYear_F().equals("3")) {
                        surrenderCharge = 0.005;
                    } else if (getYear_F().equals("4")) {
                        surrenderCharge = 0.0035;
                    } else {
                        surrenderCharge = 0;
                    }
                } else {
                    if (getYear_F().equals("1")) {
                        surrenderCharge = 0.02;
                    } else if (getYear_F().equals("2")) {
                        surrenderCharge = 0.015;
                    } else if (getYear_F().equals("3")) {
                        surrenderCharge = 0.01;
                    } else if (getYear_F().equals("4")) {
                        surrenderCharge = 0.005;
                    } else {
                        surrenderCharge = 0;
                    }
                }
            } else {
        		/*if(getYear_F().equals("1"))
             	{
             		surrenderCharge = 0.0;
             	}
             	else if(getYear_F().equals("2"))
             	{
             		surrenderCharge = 0.0;
             	}
             	else if(getYear_F().equals("3"))
             	{
             		surrenderCharge = 0.0;
             	}
             	else if(getYear_F().equals("4"))
             	{
             		surrenderCharge = 0.0;
             	}
             	else
             	{
             		surrenderCharge=0;
             	}*/
                surrenderCharge = 0;
            }
        } else {
            if (effectivePremium > 25000) {

                if (getYear_F().equals("1")) {
                    surrenderCharge = 0.06;
                } else if (getYear_F().equals("2")) {
                    surrenderCharge = 0.04;
                } else if (getYear_F().equals("3")) {
                    surrenderCharge = 0.03;
                } else if (getYear_F().equals("4")) {
                    surrenderCharge = 0.02;
                } else {
                    surrenderCharge = 0;
                }
            } else {/*
        		if(getYear_F().equals("1"))
                 	{
                 		surrenderCharge = 0.2;
                 	}
                 	else if(getYear_F().equals("2"))
                 	{
                 		surrenderCharge = 0.15;
                 	}
                 	else if(getYear_F().equals("3"))
                 	{
                 		surrenderCharge = 0.1;
                 	}
                 	else if(getYear_F().equals("4"))
                 	{
                 		surrenderCharge = 0.05;
                 	}
                 	else
                 	{
                 		surrenderCharge=0;
                 	}
        	*/
                surrenderCharge = 0;
            }
        }
        return surrenderCharge;
    }

    public double getAllocationCharge(boolean staffDisc,
                                      boolean bancAssuranceDisc, String premFreqmode) {
        // For Single Mode
        if (premFreqmode.equals("Single")) {
            if (Integer.parseInt(getYear_F()) == 1) {
                if (staffDisc == true && bancAssuranceDisc == false) {
                    return 0;
                } else if (staffDisc == false && bancAssuranceDisc == true) {
                    return 0;
                } else {
                    return 0.02;
                }
            } else {
                return 0;
            }
        }
        // For LPPT Mode
        else {
            if (Integer.parseInt(getYear_F()) == 1) {
                if (staffDisc == true && bancAssuranceDisc == false) {
                    return 0.005;
                } else if (staffDisc == false && bancAssuranceDisc == true) {
                    return 0.005;
                } else {
                    return 0.03;
                }
            } else if (Integer.parseInt(getYear_F()) == 2) {
                if (staffDisc == true && bancAssuranceDisc == false) {
                    return 0.005;
                } else if (staffDisc == false && bancAssuranceDisc == true) {
                    return 0.03;
                } else {
                    return 0.03;
                }
            } else if (Integer.parseInt(getYear_F()) == 3) {
                if (staffDisc == true && bancAssuranceDisc == false) {
                    return 0.005;
                } else if (staffDisc == false && bancAssuranceDisc == true) {
                    return 0.03;
                } else {
                    return 0.03;
                }
            } else if (Integer.parseInt(getYear_F()) == 4) {
                if (staffDisc == true && bancAssuranceDisc == false) {
                    return 0.005;
                } else if (staffDisc == false && bancAssuranceDisc == true) {
                    return 0.03;
                } else {
                    return 0.03;
                }
            } else if (Integer.parseInt(getYear_F()) == 5) {
                if (staffDisc == true && bancAssuranceDisc == false) {
                    return 0.005;
                } else if (staffDisc == false && bancAssuranceDisc == true) {
                    return 0.03;
                } else {
                    return 0.03;
                }
            } else if (Integer.parseInt(getYear_F()) == 6) {
                if (staffDisc == true && bancAssuranceDisc == false) {
                    return 0;
                } else if (staffDisc == false && bancAssuranceDisc == true) {
                    return 0;
                } else {
                    return 0;
                }
            } else if (Integer.parseInt(getYear_F()) == 7) {
                if (staffDisc == true && bancAssuranceDisc == false) {
                    return 0;
                } else if (staffDisc == false && bancAssuranceDisc == true) {
                    return 0;
                } else {
                    return 0;
                }
            } else if (Integer.parseInt(getYear_F()) == 8) {
                if (staffDisc == true && bancAssuranceDisc == false) {
                    return 0;
                } else if (staffDisc == false && bancAssuranceDisc == true) {
                    return 0;
                } else {
                    return 0;
                }
            } else if (Integer.parseInt(getYear_F()) == 9) {
                if (staffDisc == true && bancAssuranceDisc == false) {
                    return 0;
                } else if (staffDisc == false && bancAssuranceDisc == true) {
                    return 0;
                } else {
                    return 0;
                }
            } else if (Integer.parseInt(getYear_F()) == 10) {
                if (staffDisc == true && bancAssuranceDisc == false) {
                    return 0;
                } else if (staffDisc == false && bancAssuranceDisc == true) {
                    return 0;
                } else {
                    return 0;
                }
            } else {
                return 0;
            }
        }
    }

    public int getCharge_PP_Ren(int fixedMonthlyExp_SP, int fixedMonthlyExp_RP,
                                int inflation_pa_SP, int inflation_pa_RP, String premFreqMode) {
        if (premFreqMode.equals("Single")) {
            return (fixedMonthlyExp_SP * 12 * (1 + inflation_pa_SP) ^ getFinYearsAfterLaunch());
        }
        // For Regular
        else {
            return (fixedMonthlyExp_RP * 12 * (1 + inflation_pa_RP) ^ getFinYearsAfterLaunch());
        }
    }

    public int getFinYearsAfterLaunch() {
        int finYrForCalOfAdminCharges = 0;
        if (SE_Prop.monthOfInception < 4) {
            finYrForCalOfAdminCharges = SE_Prop.yearOfInception;
        } else {
            finYrForCalOfAdminCharges = SE_Prop.yearOfInception + 1;
        }
        return (finYrForCalOfAdminCharges - SE_Prop.yearOfLaunch) - 1;
    }

    public int getFinYrAfterLaunch(String dateOfInception, String dateOfLaunch) {
        return (getFinYrForCalOfAdminCharges(dateOfInception) - getYrOfDateOfLaunch(dateOfLaunch)) - 1;
    }

    public int getYrOfDateOfLaunch(String dateOfLaunch) {
        String[] dummyArr = cfap.split(dateOfLaunch, "/");
        return Integer.parseInt(dummyArr[2]);
    }

    public int getFinYrForCalOfAdminCharges(String dateOfInception) {
        if (getMonthOfInception(dateOfInception) < 4) {
            return getYearOfInception(dateOfInception);
        } else {
            return getYearOfInception(dateOfInception) + 1;
        }
    }

    public int getMonthOfInception(String dateOfInception) {
        String[] dummyArr = cfap.split(dateOfInception, "/");
        return Integer.parseInt(dummyArr[0]);
    }

    public int getYearOfInception(String dateOfInception) {
        String[] dummyArr = cfap.split(dateOfInception, "/");
        return Integer.parseInt(dummyArr[2]);
    }

    public String[] calSurrRateArrLimited(double effectivePremium) {
        if (effectivePremium <= 25000) {
            return new String[]{"0.20", "0.15", "0.10", "0.05", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0"};
        } else {
            return new String[]{"0.06", "0.04", "0.03", "0.02", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0"};
        }
    }

    public String[] calSurrRateArrSingle() {
        return new String[]{"0.01", "0.005", "0.0025", "0.001", "0", "0",
                "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                "0", "0", "0", "0"};
    }

    public String[] calCapArr(double effectivePremium) {
        if (effectivePremium <= 25000) {
            return new String[]{"3000", "2000", "1500", "1000", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0"};
        } else {
            return new String[]{"6000", "5000", "4000", "2000", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0"};
        }
    }

    public String[] getForBIArr(double mortalityCharges_AsPercentOfofLICtable) {/*
		int[] ageArr = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
				13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28,
				29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44,
				45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60,
				61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76,
				77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92,
				93, 94, 95, 96, 97, 98, 99, 100 };
		String[] strArrTReturn = new String[99];
		SmartEliteDB smartEliteDB = new SmartEliteDB();
		for (int i = 0; i < smartEliteDB.getIAIarray().length - 2; i++) {
			if (ageArr[i] < 7) {
				strArrTReturn[i] = "0";
			} else {
				strArrTReturn[i] = cfap
						.roundUp_Level2(cfap.getStringWithout_E(((smartEliteDB
								.getIAIarray()[i] + smartEliteDB.getIAIarray()[i + 1]) / 2)
								* mortalityCharges_AsPercentOfofLICtable * 1000));
			}
		}
		return strArrTReturn;
	*/

        int[] ageArr = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100};
        String[] strArrTReturn = new String[99];
        BI_SmartEliteDB smartEliteDB = new BI_SmartEliteDB();

//        System.out.println("length "+smartWealthBuilderDB.getIAIarray().length);
        for (int i = 0; i < smartEliteDB.getIAIarray().length - 2; i++) {
            strArrTReturn[i] = String.valueOf(((smartEliteDB.getIAIarray()[i] + smartEliteDB.getIAIarray()[i + 1]) / 2) * mortalityCharges_AsPercentOfofLICtable);
//        	strArrTReturn[i]=String.valueOf((smartEliteDB.getIAIarray()[i]) * mortalityCharges_AsPercentOfofLICtable);

//            if(ageArr[i]<7)
//            {strArrTReturn[i]="0";}
//            else
//            {
//                //double a=(((smartWealthBuilderDB.getIAIarray()[i]+smartWealthBuilderDB.getIAIarray()[i+1])/2) * mortalityCharges_AsPercentOfofLICtable);
//                //double b=(double)1/12;
//                //double c=(1-cfap.pow((1-a*mortalityCharges_AsPercentOfofLICtable),b));
//                //strArrTReturn[i]=cfap.roundUp_Level2(cfap.getStringWithout_E(c));
//                strArrTReturn[i]=cfap.roundUp_Level2(cfap.getStringWithout_E(((smartWealthBuilderDB.getIAIarray()[i]+smartWealthBuilderDB.getIAIarray()[i+1])/2) * mortalityCharges_AsPercentOfofLICtable * 1000  ));
//            }

        }
        return strArrTReturn;

    }

    public String getMortalityChargesReductionYield_AG() {
        return mortalityChargesReductionYield_AG;
    }

    public void setMortalityChargesReductionYield_AG(double _fundValueAtEnd_AQ,
                                                     int policyTerm, String[] forBIArray, int ageAtEntry,
                                                     double sumAssured, boolean mortalityCharges) {/*
		if (!(getPolicyInForce_G().equals("Y"))
				|| Integer.parseInt(getYear_F()) > policyTerm) {
			this.mortalityChargesReductionYield_AG = "" + 0;
		} else {
			double arrOutput = Double
					.parseDouble(forBIArray[(ageAtEntry + Integer
							.parseInt(getYear_F())) - 1]);
			double a = 1 - arrOutput / 1000;
			double b = (double) 1 / 12;
			double c = 0;
			if (planOption.equals("Gold")) {
				c = Math.max(
						sumAssured,
						Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()))
						- (Double
								.parseDouble(getAmountAvailableForInvestment_N()) + _fundValueAtEnd_AQ);
			} else {
				c = Math.max(
						sumAssured,
						(Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()) - _fundValueAtEnd_AQ));
			}
			int d = 0;
			// System.out.println("mortalityCharges "+mortalityCharges);
			if (mortalityCharges) {
				d = 1;
			} else {
				d = 0;
			}
			double e = Math.min(sumAssured, 5000000);
			double f = SE_Prop.ADBandATPDCharge / 12;
			// System.out.println("d "+d);
			this.mortalityChargesReductionYield_AG = cfap.roundUp_Level2(cfap
					.getStringWithout_E(((1 - (Math.pow(a, b)))
							* Math.max(c, 0) + (e * f))
							* (d)));
		}
	*/
        if (!(getPolicyInForce_G().equals("Y")) || Integer.parseInt(getYear_F()) > policyTerm) {
            this.mortalityChargesReductionYield_AG = "" + 0;
        } else {
            double arrOutput = Double.parseDouble(forBIArray[Integer.parseInt(getAge_H())]);
            //System.out.println("arroutput "+arrOutput);
            //double div=arrOutput/1000;
//            double a=arrOutput/12;
            //System.out.println("a "+a);


            DecimalFormat df = new DecimalFormat("#.#######");

            String roundTo7 = df.format(arrOutput);
            String test = cfap.getRoundOffLevel5(roundTo7);

//            System.out.println("arroutput RY "+arrOutput);
//            System.out.println("round to 5 RY"+test);

            double a = Double.parseDouble(test) / 12;

            double max1 = Math.max(sumAssured, Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()));
            //System.out.println("max1 "+max1);
            double b = (max1 - (Double.parseDouble(getAmountAvailableForInvestment_N()) + _fundValueAtEnd_AQ));
            //System.out.println("b "+b);

            int c = 0;
            if (mortalityCharges) {
                c = 1;
            } else {
                c = 0;
            }
//            this.mortalityCharges_AG= cfap.roundUp_Level2(cfap.getStringWithout_E(((a*Math.max(b, 0))*c)));
            this.mortalityChargesReductionYield_AG = cfap.getRoundOffLevel2New(cfap.getStringWithout_E(((a * Math.max(b, 0)) * c)));
        }
    }

    public String getTotalChargesReductionYield_AH() {
        return totalChargesReductionYield_AH;
    }

    public void setTotalChargesReductionYield_AH(int policyTerm) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.totalChargesReductionYield_AH = ""
                    + (Double.parseDouble(getPolicyAdministrationCharge_Q())
                    + Double.parseDouble(getMortalityChargesReductionYield_AG())
                    + Double.parseDouble(getSumAssuredRelatedCharges_O()) + Double
                    .parseDouble(getRiderCharges_P()));
        } else {
            this.totalChargesReductionYield_AH = "" + 0;
        }
    }

    public String getTotalServiceTax_ExclOfSTonAllocAndSurrReductionYield_AI() {
        return totalServiceTax_ExclOfSTonAllocAndsurrReductionYield_AI;
    }

    public void setTotalServiceTax_ExclOfSTonAllocAndSurrReductionYield_AI(
            double serviceTax, boolean mortalityAndRiderCharges,
            boolean administrationAndSArelatedCharges) {
        double a = 0, b = 0, c = 0;
        if (mortalityAndRiderCharges) {
            a = Double.parseDouble(getMortalityChargesReductionYield_AG());
        } else {
            a = 0;
        }
        if (administrationAndSArelatedCharges) {
            b = Double.parseDouble(getPolicyAdministrationCharge_Q());
        } else {
            b = 0;
        }
        this.totalServiceTax_ExclOfSTonAllocAndsurrReductionYield_AI = cfap
                .roundUp_Level2(cfap.getStringWithout_E((a + b) * serviceTax));
    }

    public String getAdditionToFundIfAnyReductionYield_AK() {
        return additionToFundIfAnyReductionYield_AK;
    }

    public void setAdditionToFundIfAnyReductionYield_AK(
            double _fundValueAtEnd_AQ, int policyTerm) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.additionToFundIfAnyReductionYield_AK = cfap
                    .roundUp_Level2(cfap.getStringWithout_E((_fundValueAtEnd_AQ
                            + Double.parseDouble(getAmountAvailableForInvestment_N())
                            - Double.parseDouble(getTotalChargesReductionYield_AH())
                            - Double.parseDouble(getTotalServiceTax_ExclOfSTonAllocAndSurrReductionYield_AI()) + Double
                            .parseDouble(getRiderCharges_P()))
                            * (cfap.pow(1 + SE_Prop.int2, (double) 1 / 12) - 1)));
        } else {
            this.additionToFundIfAnyReductionYield_AK = "" + 0;
        }
    }

    public String getFundBeforeFMCReductionYield_AL() {
        return fundBeforeFMCReductionYield_AL;
    }

    public void setFundBeforeFMCReductionYield_AL(double _fundValueAtEnd_AQ,
                                                  int policyTerm) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.fundBeforeFMCReductionYield_AL = ""
                    + (_fundValueAtEnd_AQ
                    + Double.parseDouble(getAmountAvailableForInvestment_N())
                    - Double.parseDouble(getTotalChargesReductionYield_AH())
                    + Double.parseDouble(getAdditionToFundIfAnyReductionYield_AK())
                    - Double.parseDouble(getTotalServiceTax_ExclOfSTonAllocAndSurrReductionYield_AI()) + Double
                    .parseDouble(getRiderCharges_P()));
        } else {
            this.fundBeforeFMCReductionYield_AL = "" + 0;
        }
    }

    public String getFundManagementChargeReductionYield_AM() {
        return fundManagementChargeReductionYield_AM;
    }

    public void setFundManagementChargeReductionYield_AM(int policyTerm,
                                                         double percentToBeInvested_EquityEliteIIFund,
                                                         double percentToBeInvested_BalancedFund,
                                                         double percentToBeInvested_BondFund,
                                                         double percentToBeInvested_MoneyMarketFund,
                                                         double percentToBeInvested_BondOptimiserFund,
                                                         double percentToBeInvested_EquityFund,
                                                         double percentToBeInvested_MidcapFund,
                                                         double percentToBeInvested_PureFund) {
        double temp1 = 0, temp2 = 0;
        if (Integer.parseInt(getMonth_E()) == 1) {
            temp1 = SE_Prop.charge_Fund;
        } else {
            temp1 = 0;
        }
        temp2 = getCharge_fund_ren(percentToBeInvested_EquityEliteIIFund,
                percentToBeInvested_BalancedFund, percentToBeInvested_BondFund,
                percentToBeInvested_MoneyMarketFund,
                percentToBeInvested_BondOptimiserFund,
                percentToBeInvested_EquityFund,
                percentToBeInvested_MidcapFund,
                percentToBeInvested_PureFund) / 12;
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.fundManagementChargeReductionYield_AM = cfap
                    .roundUp_Level2(cfap.getStringWithout_E(Double
                            .parseDouble(getFundBeforeFMCReductionYield_AL())
                            * (temp1 + temp2)));
        } else {
            this.fundManagementChargeReductionYield_AM = "" + 0;
        }
    }

    public String getServiceTaxOnFMCReductionYield_AN() {
        return serviceTaxOnFMCReductionYield_AN;
    }

    public void setServiceTaxOnFMCReductionYield_AN(
            boolean fundManagementCharges, double serviceTax, double indexFund,
            double percentToBeInvested_EquityEliteIIFund,
            double percentToBeInvested_BalancedFund,
            double percentToBeInvested_BondFund,
            double percentToBeInvested_MoneyMarketFund,
            double percentToBeInvested_BondOptimiserFund,
            double percentToBeInvested_EquityFund,
            double percentToBeInvested_MidcapFund,
            double percentToBeInvested_PureFund) {
        double a = 0;
        if (fundManagementCharges) {
            a = (Double.parseDouble(getFundManagementChargeReductionYield_AM()));
        } else {
            a = 0;
        }
        this.serviceTaxOnFMCReductionYield_AN = cfap.roundUp_Level2(cfap
                .getStringWithout_E(a * serviceTax));
    }

    public String getFundValueAfterFMCandBeforeGAReductionYield_AO() {
        return fundValueAfterFMCandBeforeGAReductionYield_AO;
    }

    public void setFundValueAfterFMCandBeforeGAReductionYield_AO(int policyTerm) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.fundValueAfterFMCandBeforeGAReductionYield_AO = ""
                    + (Double.parseDouble(getFundBeforeFMCReductionYield_AL())
                    - Double.parseDouble(getFundManagementChargeReductionYield_AM()) - Double
                    .parseDouble(getServiceTaxOnFMCReductionYield_AN()));
        } else {
            this.fundValueAfterFMCandBeforeGAReductionYield_AO = "" + 0;
        }
    }

    public String getFundValueAtEndReductionYield_AQ() {
        return fundValueAtEndReductionYield_AQ;
    }

    public void setFundValueAtEndReductionYield_AQ() {
        this.fundValueAtEndReductionYield_AQ = ""
                + (Double.parseDouble(getGuaranteedAddition_AP()) + Double
                .parseDouble(getFundValueAfterFMCandBeforeGAReductionYield_AO()));
    }

    public String getDeathBenefitReductionYield_AU() {
        return deathBenefitReductionYield_AU;
    }

    public void setDeathBenefitReductionYield_AU(int policyTerm,
                                                 double sumAssured) {
        if (Integer.parseInt(getYear_F()) > policyTerm) {
            this.deathBenefitReductionYield_AU = "" + 0;
        } /*else {
			if (planOption.equals("Gold")) {
				this.deathBenefitReductionYield_AU = ""
						+ (Math.max(
								Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()),
								Math.max(
										sumAssured,
										Double.parseDouble(getFundValueAtEndReductionYield_AQ()))));
			} else {
				this.deathBenefitReductionYield_AU = ""
						+ (Math.max(
								Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()),
								(sumAssured + Double
										.parseDouble(getFundValueAtEndReductionYield_AQ()))));
			}
		}*/ else {
            this.deathBenefitReductionYield_AU = "" + (Math.max(Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()), Math.max(sumAssured, Double.parseDouble(getFundValueAtEndReductionYield_AQ()))));
        }
    }

    public String getTotalServiceTaxReductionYield_AJ() {
        return totalServiceTaxReductionYield_AJ;
    }

    public void setTotalServiceTaxReductionYield_AJ(double serviceTax) {
        double temp = 0;
        if (SE_Prop.riderChargesReductionYield) {
            temp = Double.parseDouble(getRiderCharges_P());
        } else {
            temp = 0;
        }
        this.totalServiceTaxReductionYield_AJ = cfap
                .roundUp_Level2(cfap.getStringWithout_E(Double
                        .parseDouble(getServiceTaxOnAllocation_M())
                        + Double.parseDouble(getTotalServiceTax_ExclOfSTonAllocAndSurrReductionYield_AI())
                        + Double.parseDouble(getServiceTaxOnFMCReductionYield_AN())
                        + temp * serviceTax));
    }

    public String getSurrenderChargesReductionYield_AR() {
        return surrenderChargesReductionYield_AR;
    }

    public void setSurrenderChargesReductionYield_AR(double effectivePremium,
                                                     String premFreq) {
        double a = Math.min(
                Double.parseDouble(getFundValueAtEndReductionYield_AQ()),
                effectivePremium);
        double b = getSurrenderCharge(premFreq, effectivePremium);
        this.surrenderChargesReductionYield_AR = cfap.roundUp_Level2(cfap
                .getStringWithout_E(Math.min((a * b),
                        Double.parseDouble(getSurrenderCap_AV()))));
    }

    public String getServiceTaxOnSurrenderChargesReductionYield_AS() {
        return serviceTaxOnSurrenderChargesReductionYield_AS;
    }

    public void setServiceTaxOnSurrenderChargesReductionYield_AS(
            boolean surrenderCharges, double serviceTax) {
        if (surrenderCharges) {
            this.serviceTaxOnSurrenderChargesReductionYield_AS = cfap
                    .roundUp_Level2(cfap.getStringWithout_E(Double
                            .parseDouble(getSurrenderChargesReductionYield_AR())
                            * serviceTax));
        } else {
            this.serviceTaxOnSurrenderChargesReductionYield_AS = "" + 0;
        }
    }

    public String getSurrenderValueReductionYield_AT() {
        return surrenderValueReductionYield_AT;
    }

    public void setSurrenderValueReductionYield_AT() {
        this.surrenderValueReductionYield_AT = ""
                + (Double.parseDouble(getFundValueAtEndReductionYield_AQ())
                - Double.parseDouble(getSurrenderChargesReductionYield_AR()) - Double
                .parseDouble(getServiceTaxOnSurrenderChargesReductionYield_AS()));
    }

    public void setMonth_BG(int monthNumber) {
        this.month_BG = "" + monthNumber;
    }

    public String getMonth_BG() {
        return month_BG;
    }

    public void setReductionYield_BI(int noOfYearsElapsedSinceInception,
                                     double _fundValueAtEnd_AQ) {
        double a, b;
        // if((Integer.parseInt(getMonth_E())) <=
        // (noOfYearsElapsedSinceInception*12)+1)
        if ((Integer.parseInt(getMonth_BG())) < (noOfYearsElapsedSinceInception * 12)) {
            a = -(Double.parseDouble(getPremium_I()));
        } else {
            a = 0;
        }

        if ((Integer.parseInt(getMonth_BG())) == (noOfYearsElapsedSinceInception * 12)) {
            b = _fundValueAtEnd_AQ;
        } else {
            b = 0;
        }
        // System.out.println("a_BU "+a);
        // System.out.println("b_BU "+b);
        this.reductionYield_BI = "" + (a + b);
    }

    public String getReductionYield_BI() {
        return reductionYield_BI;
    }

    public void setReductionYield_BD(int policyTerm, double _fundValueAtEnd_AQ) {
        double a, b;
        if ((Integer.parseInt(getMonth_BG())) == (policyTerm * 12)) {
            // System.out.println("inside if");
            // System.out.println("getFundValueAtEndReductionYield_AS "+getFundValueAtEndReductionYield_AS());
            a = _fundValueAtEnd_AQ;
        } else {
            a = 0;
        }

        if ((Integer.parseInt(getMonth_BG())) < (policyTerm * 12)) {
            b = -(Double.parseDouble(getPremium_I()));
        } else {
            b = 0;
        }
        // System.out.println("a_BQ" +a);
        // this.reductionYield_BQ=""+(Double.parseDouble(getPremium_I()) + a);
        this.reductionYield_BD = "" + (b + a);
    }

    public String getReductionYield_BD() {
        return reductionYield_BD;
    }

    public void setIRRAnnual_BD(double ans) {
        // System.out.println("aaaaaaa "+((cfap.pow((1+ans),12)-1)*100));
        this.irrAnnual_BD = "" + ((cfap.pow((1 + ans), 12) - 1));
    }

    public String getIRRAnnual_BD() {
        return irrAnnual_BD;
    }

    public void setIRRAnnual_BI(double ans) {
        // System.out.println("aaaaaaa "+((comm.pow((1+ans),12)-1)*100));
        this.irrAnnual_BI = "" + ((cfap.pow((1 + ans), 12) - 1));
    }

    public String getIRRAnnual_BI() {
        return irrAnnual_BI;
    }

    public void setReductionInYieldMaturityAt(double int2) {
        this.reductionInYieldMaturityAt = ""
                + ((int2 - Double.parseDouble(getIRRAnnual_BD())) * 100);
    }

    public String getReductionInYieldMaturityAt() {
        return reductionInYieldMaturityAt;
    }

    public void setReductionInYieldNumberOfYearsElapsedSinceInception(
            double int2) {
        this.reductionInYieldNumberOfYearsElapsedSinceInception = ""
                + ((int2 - Double.parseDouble(getIRRAnnual_BI())) * 100);
    }

    public String getReductionInYieldNumberOfYearsElapsedSinceInception() {
        return reductionInYieldNumberOfYearsElapsedSinceInception;
    }

    public double irr(ArrayList<String> values, double guess) {
        int maxIterationCount = 20;
        double absoluteAccuracy = 1E-7;
        double[] arr = new double[values.size()];
        double x0 = guess;
        double x1;

        int i = 0;
        // System.out.println("inside irr ");
        // System.out.println("values "+values);
        while (i < maxIterationCount) {

            // the value of the function (NPV) and its derivate can be
            // calculated in the same loop
            double fValue = 0;
            double fDerivative = 0;
            for (int k = 0; k < values.size(); k++) {
                // System.out.println("value "+Double.parseDouble(values.get(k)));
                arr[k] = Double.parseDouble(values.get(k));
                fValue += arr[k] / Math.pow(1.0 + x0, k);
                fDerivative += -k * arr[k] / Math.pow(1.0 + x0, k + 1);
            }

            // the essense of the Newton-Raphson Method
            x1 = x0 - fValue / fDerivative;

            if (Math.abs(x1 - x0) <= absoluteAccuracy) {
                return x1;
            }

            x0 = x1;
            ++i;
        }
        // maximum number of iterations is exceeded
        return 0;
    }

    public double getCommission_AQ(double annualisedPrem, double topupPrem,
                                   BI_SmartEliteBean smartEliteBean) {
        double topup = 0.01;
//		System.out.println("getCommistionRate "
//				+ getCommistionRate(smartEliteBean));
        return getCommistionRate(smartEliteBean) * annualisedPrem + topupPrem
                * topup;

    }

    public double getCommistionRate(BI_SmartEliteBean smartEliteBean) {
        int findYear;

        if (Integer.parseInt(getYear_F()) > 10) {
            findYear = 11;
        } else
            findYear = Integer.parseInt(getYear_F());

        if (smartEliteBean.getIsForStaffOrNot()) {
            return 0;
        } else if (smartEliteBean.getPremFreq().equals("Limited")) {
            double[] lpptArr = {0.025, 0.025, 0.025, 0.025, 0.025, 0.01, 0.01,
                    0.01, 0.01, 0.01, 0};
            return lpptArr[findYear - 1];
        } else if (smartEliteBean.getPremFreq().equals("Single")) {
            double[] pptArr = {0.02, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            return pptArr[findYear - 1];
        } else {
            double[] commArr = {0.1, 0.02, 0.02, 0.02, 0.02, 0.015, 0.015,
                    0.01, 0.01, 0.01, 0.01};
            return commArr[findYear - 1];
        }
    }

    public String getMortalityChargesReductionYield_R() {
        return mortalityChargesReductionYield_R;
    }

    public void setMortalityChargesReductionYield_R(double _fundValueAtEnd_AB,
                                                    int policyTerm, String[] forBIArray, int ageAtEntry,
                                                    double sumAssured, boolean mortalityCharges) {/*
		if (!(getPolicyInForce_G().equals("Y"))
				|| Integer.parseInt(getYear_F()) > policyTerm) {
			this.mortalityChargesReductionYield_R = "" + 0;
		} else {
			double arrOutput = Double
					.parseDouble(forBIArray[(ageAtEntry + Integer
							.parseInt(getYear_F())) - 1]);
			double a = 1 - arrOutput / 1000;
			double b = (double) 1 / 12;
			double c = 0;
			if (planOption.equals("Gold")) {
				c = Math.max(
						sumAssured,
						Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()))
						- (Double
								.parseDouble(getAmountAvailableForInvestment_N()) + _fundValueAtEnd_AB);
			} else {
				c = Math.max(
						sumAssured,
						(Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()) - _fundValueAtEnd_AB));
			}
			int d = 0;
			// System.out.println("mortalityCharges "+mortalityCharges);
			if (mortalityCharges) {
				d = 1;
			} else {
				d = 0;
			}
			double e = Math.min(sumAssured, 5000000);
			double f = SE_Prop.ADBandATPDCharge / 12;
			// System.out.println("d "+d);
			this.mortalityChargesReductionYield_R = cfap.roundUp_Level2(cfap
					.getStringWithout_E(((1 - (Math.pow(a, b)))
							* Math.max(c, 0) + (e * f))
							* (d)));
		}
	*/
        if (!(getPolicyInForce_G().equals("Y")) || Integer.parseInt(getYear_F()) > policyTerm) {
            this.mortalityChargesReductionYield_R = "" + 0;
        } else {
            double arrOutput = Double.parseDouble(forBIArray[Integer.parseInt(getAge_H())]);


//            System.out.println("**********arroutput "+arrOutput);
            //double div=arrOutput/1000;
            //System.out.println("div "+div);
//            double a=arrOutput/12;
            //System.out.println("a "+a);

            DecimalFormat df = new DecimalFormat("#.#######");

            String roundTo7 = df.format(arrOutput);
            String test = cfap.getRoundOffLevel5(roundTo7);

//            System.out.println("arroutput RY "+arrOutput);
//            System.out.println("round to 5 RY"+test);

            double a = Double.parseDouble(test) / 12;
            double max1 = Math.max(sumAssured, Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()));

//            System.out.println("max1 "+max1);

            double b = (max1 - (Double.parseDouble(getAmountAvailableForInvestment_N()) + _fundValueAtEnd_AB));


//            System.out.println("Minus amount" + (Double.parseDouble(getAmountAvailableForInvestment_N())+_fundValueAtEnd_AB));
//            System.out.println("N" + getAmountAvailableForInvestment_N());
//            System.out.println("AB" + _fundValueAtEnd_AB);

//            System.out.println("b "+b);

            int c = 0;
            if (mortalityCharges) {
                c = 1;
            } else {
                c = 0;
            }
//            this.mortalityCharges_R= cfap.roundUp_Level2(cfap.getStringWithout_E(((a*Math.max(b, 0))*c)));
            this.mortalityChargesReductionYield_R = cfap.getRoundOffLevel2New(cfap.getStringWithout_E(((a * Math.max(b, 0)) * c)));
        }
    }

    public String getTotalChargesReductionYield_S() {
        return totalChargesReductionYield_S;
    }

    public void setTotalChargesReductionYield_S(int policyTerm) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.totalChargesReductionYield_S = ""
                    + (Double.parseDouble(getPolicyAdministrationCharge_Q())
                    + Double.parseDouble(getMortalityChargesReductionYield_R())
                    + Double.parseDouble(getSumAssuredRelatedCharges_O()) + Double
                    .parseDouble(getRiderCharges_P()));
        } else {
            this.totalChargesReductionYield_S = "" + 0;
        }
    }

    public String getTotalServiceTax_ExclOfSTonAllocAndSurrReductionYield_T() {
        return totalServiceTax_ExclOfSTonAllocAndsurrReductionYield_T;
    }

    public void setTotalServiceTax_ExclOfSTonAllocAndSurrReductionYield_T(
            double serviceTax, boolean mortalityAndRiderCharges,
            boolean administrationAndSArelatedCharges) {
        double a = 0, b = 0, c = 0;
        if (mortalityAndRiderCharges) {
            a = Double.parseDouble(getMortalityChargesReductionYield_R());
        } else {
            a = 0;
        }
        if (administrationAndSArelatedCharges) {
            b = Double.parseDouble(getPolicyAdministrationCharge_Q());
        } else {
            b = 0;
        }
        this.totalServiceTax_ExclOfSTonAllocAndsurrReductionYield_T = cfap
                .roundUp_Level2(cfap.getStringWithout_E((a + b) * serviceTax));
    }

    public String getAdditionToFundIfAnyReductionYield_V() {
        return additionToFundIfAnyReductionYield_V;
    }

    public void setAdditionToFundIfAnyReductionYield_V(
            double _fundValueAtEnd_AB, int policyTerm) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.additionToFundIfAnyReductionYield_V = cfap
                    .roundUp_Level2(cfap.getStringWithout_E((_fundValueAtEnd_AB
                            + Double.parseDouble(getAmountAvailableForInvestment_N())
                            - Double.parseDouble(getTotalChargesReductionYield_S())
                            - Double.parseDouble(getTotalServiceTax_ExclOfSTonAllocAndSurrReductionYield_T()) + Double
                            .parseDouble(getRiderCharges_P()))
                            * (cfap.pow(1 + SE_Prop.int1, (double) 1 / 12) - 1)));
        } else {
            this.additionToFundIfAnyReductionYield_V = "" + 0;
        }
    }

    public String getFundBeforeFMCReductionYield_W() {
        return fundBeforeFMCReductionYield_W;
    }

    public void setFundBeforeFMCReductionYield_W(double _fundValueAtEnd_AB,
                                                 int policyTerm) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.fundBeforeFMCReductionYield_W = ""
                    + (_fundValueAtEnd_AB
                    + Double.parseDouble(getAmountAvailableForInvestment_N())
                    - Double.parseDouble(getTotalChargesReductionYield_S())
                    + Double.parseDouble(getAdditionToFundIfAnyReductionYield_V())
                    - Double.parseDouble(getTotalServiceTax_ExclOfSTonAllocAndSurrReductionYield_T()) + Double
                    .parseDouble(getRiderCharges_P()));
        } else {
            this.fundBeforeFMCReductionYield_W = "" + 0;
        }
    }

    public String getFundManagementChargeReductionYield_X() {
        return fundManagementChargeReductionYield_X;
    }

    public void setFundManagementChargeReductionYield_X(int policyTerm,
                                                        double percentToBeInvested_EquityEliteIIFund,
                                                        double percentToBeInvested_BalancedFund,
                                                        double percentToBeInvested_BondFund,
                                                        double percentToBeInvested_MoneyMarketFund,
                                                        double percentToBeInvested_BondOptimiserFund,
                                                        double percentToBeInvested_EquityFund,
                                                        double percentToBeInvested_MidcapFund,
                                                        double percentToBeInvested_PureFund) {
        double temp1 = 0, temp2 = 0;
        if (Integer.parseInt(getMonth_E()) == 1) {
            temp1 = SE_Prop.charge_Fund;
        } else {
            temp1 = 0;
        }
        temp2 = getCharge_fund_ren(percentToBeInvested_EquityEliteIIFund,
                percentToBeInvested_BalancedFund, percentToBeInvested_BondFund,
                percentToBeInvested_MoneyMarketFund,
                percentToBeInvested_BondOptimiserFund,
                percentToBeInvested_EquityFund,
                percentToBeInvested_MidcapFund,
                percentToBeInvested_PureFund) / 12;
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.fundManagementChargeReductionYield_X = cfap
                    .roundUp_Level2(cfap.getStringWithout_E(Double
                            .parseDouble(getFundBeforeFMCReductionYield_W())
                            * (temp1 + temp2)));
        } else {
            this.fundManagementChargeReductionYield_X = "" + 0;
        }
    }

    public String getServiceTaxOnFMCReductionYield_Y() {
        return serviceTaxOnFMCReductionYield_Y;
    }

    public void setServiceTaxOnFMCReductionYield_Y(
            boolean fundManagementCharges, double serviceTax, double indexFund,
            double percentToBeInvested_EquityEliteIIFund,
            double percentToBeInvested_BalancedFund,
            double percentToBeInvested_BondFund,
            double percentToBeInvested_MoneyMarketFund,
            double percentToBeInvested_BondOptimiserFund,
            double percentToBeInvested_EquityFund,
            double percentToBeInvested_MidcapFund,
            double percentToBeInvested_PureFund) {
        double a = 0;
        if (fundManagementCharges) {
            a = (Double.parseDouble(getFundManagementChargeReductionYield_X()));
        } else {
            a = 0;
        }
        this.serviceTaxOnFMCReductionYield_Y = cfap.roundUp_Level2(cfap
                .getStringWithout_E(a * serviceTax));
    }

    public String getFundValueAfterFMCandBeforeGAReductionYield_Z() {
        return fundValueAfterFMCandBeforeGAReductionYield_Z;
    }

    public void setFundValueAfterFMCandBeforeGAReductionYield_Z(int policyTerm) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.fundValueAfterFMCandBeforeGAReductionYield_Z = ""
                    + (Double.parseDouble(getFundBeforeFMCReductionYield_W())
                    - Double.parseDouble(getFundManagementChargeReductionYield_X()) - Double
                    .parseDouble(getServiceTaxOnFMCReductionYield_Y()));
        } else {
            this.fundValueAfterFMCandBeforeGAReductionYield_Z = "" + 0;
        }
    }

    public void setReductionYield_BC(int policyTerm, double _fundValueAtEnd_AB) {
        double a, b;
        if ((Integer.parseInt(getMonth_BG())) == (policyTerm * 12)) {
            // System.out.println("inside if");
            // System.out.println("getFundValueAtEndReductionYield_AS "+getFundValueAtEndReductionYield_AS());
            a = _fundValueAtEnd_AB;
        } else {
            a = 0;
        }

        if ((Integer.parseInt(getMonth_BG())) < (policyTerm * 12)) {
            b = -(Double.parseDouble(getPremium_I()));
        } else {
            b = 0;
        }
        // System.out.println("a_BQ" +a);
        // this.reductionYield_BQ=""+(Double.parseDouble(getPremium_I()) + a);
        this.reductionYield_BC = "" + (b + a);
    }

    public String getReductionYield_BC() {
        return reductionYield_BC;
    }

    public String getFundValueAtEndReductionYield_AB() {
        return fundValueAtEndReductionYield_AB;
    }

    public void setFundValueAtEndReductionYield_AB() {
        this.fundValueAtEndReductionYield_AB = ""
                + (Double.parseDouble(getGuaranteedAddition_AA()) + Double
                .parseDouble(getFundValueAfterFMCandBeforeGAReductionYield_Z()));
    }

    public String getDeathBenefitReductionYield_AF() {
        return deathBenefitReductionYield_AF;
    }

    public void setDeathBenefitReductionYield_AF(int policyTerm,
                                                 double sumAssured) {
        if (Integer.parseInt(getYear_F()) > policyTerm) {
            this.deathBenefitReductionYield_AF = "" + 0;
        } /*else {
			if (planOption.equals("Gold")) {
				this.deathBenefitReductionYield_AF = ""
						+ (Math.max(
								Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()),
								Math.max(
										sumAssured,
										Double.parseDouble(getFundValueAtEndReductionYield_AB()))));
			} else {
				this.deathBenefitReductionYield_AF = ""
						+ (Math.max(
								Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()),
								(sumAssured + Double
										.parseDouble(getFundValueAtEndReductionYield_AB()))));
			}
		}*/ else {
            this.deathBenefitReductionYield_AF = "" + (Math.max(Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()), Math.max(sumAssured, Double.parseDouble(getFundValueAtEndReductionYield_AB()))));
        }
    }

    public String getTotalServiceTaxReductionYield_U() {
        return totalServiceTaxReductionYield_U;
    }

    public void setTotalServiceTaxReductionYield_U(double serviceTax) {
        double temp = 0;
        if (SE_Prop.riderChargesReductionYield) {
            temp = Double.parseDouble(getRiderCharges_P());
        } else {
            temp = 0;
        }
        this.totalServiceTaxReductionYield_U = cfap
                .roundUp_Level2(cfap.getStringWithout_E(Double
                        .parseDouble(getServiceTaxOnAllocation_M())
                        + Double.parseDouble(getTotalServiceTax_ExclOfSTonAllocAndSurrReductionYield_T())
                        + Double.parseDouble(getServiceTaxOnFMCReductionYield_Y())
                        + temp * serviceTax));
    }

    public String getSurrenderChargesReductionYield_AC() {
        return surrenderChargesReductionYield_AC;
    }

    public void setSurrenderChargesReductionYield_AC(double effectivePremium,
                                                     String premFreq) {
        double a = Math.min(
                Double.parseDouble(getFundValueAtEndReductionYield_AB()),
                effectivePremium);
        double b = getSurrenderCharge(premFreq, effectivePremium);
        this.surrenderChargesReductionYield_AC = cfap.roundUp_Level2(cfap
                .getStringWithout_E(Math.min((a * b),
                        Double.parseDouble(getSurrenderCap_AV()))));
    }

    public String getServiceTaxOnSurrenderChargesReductionYield_AD() {
        return serviceTaxOnSurrenderChargesReductionYield_AD;
    }

    public void setServiceTaxOnSurrenderChargesReductionYield_AD(
            boolean surrenderCharges, double serviceTax) {
        if (surrenderCharges) {
            this.serviceTaxOnSurrenderChargesReductionYield_AD = cfap
                    .roundUp_Level2(cfap.getStringWithout_E(Double
                            .parseDouble(getSurrenderChargesReductionYield_AC())
                            * serviceTax));
        } else {
            this.serviceTaxOnSurrenderChargesReductionYield_AD = "" + 0;
        }
    }

    public String getSurrenderValueReductionYield_AE() {
        return surrenderValueReductionYield_AE;
    }

    public void setSurrenderValueReductionYield_AE() {
        this.surrenderValueReductionYield_AE = ""
                + (Double.parseDouble(getFundValueAtEndReductionYield_AB())
                - Double.parseDouble(getSurrenderChargesReductionYield_AC()) - Double
                .parseDouble(getServiceTaxOnSurrenderChargesReductionYield_AD()));
    }

    public void setIRRAnnual_BC(double ans) {
        // System.out.println("aaaaaaa "+((cfap.pow((1+ans),12)-1)*100));
        this.irrAnnual_BC = "" + ((cfap.pow((1 + ans), 12) - 1));
    }

    public String getIRRAnnual_BC() {
        return irrAnnual_BC;
    }

    // Changes to be made in connect life on 30/12/2014
    public double getStaffDiscPercentage(boolean isStaffDisc, int PPT,
                                         String premFreq) {
        // System.out.println("inside staff");
        //
        // System.out.println("boolean "+isStaffDisc);
        double discPercentage = 0;
        if (isStaffDisc) {
            // System.out.println("inside 1st if");
            if (premFreq.equals("Single")) {
                discPercentage = 0.02;
            } else {
                if (PPT == 5)
                    discPercentage = 0.025;
                else if (PPT == 8)
                    discPercentage = 0.025;
                else if (PPT == 10)
                    discPercentage = 0.025;
            }
        } else {
            discPercentage = 0.0;
        }
        // System.out.println("-------------- " +discPercentage);
        return discPercentage;
    }

    public String getStaffDiscCode(boolean isStaff) {
        if (isStaff)
            return "SBI";
        else
            return "None";
    }
}
