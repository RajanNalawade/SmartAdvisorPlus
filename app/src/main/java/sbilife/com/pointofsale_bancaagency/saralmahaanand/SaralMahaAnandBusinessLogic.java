package sbilife.com.pointofsale_bancaagency.saralmahaanand;

import java.util.ArrayList;

import sbilife.com.pointofsale_bancaagency.common.CommonForAllProd;

class SaralMahaAnandBusinessLogic {
    private CommonForAllProd cfap = null;
    private SaralMahaAnandProperties SMA_Prop = null;
    private SaralMahaAnandDB saralDB = null;
    private String month_E = null, policyInForce_G = "Y";
    private String year_F = null;
    private String age_H = null;
    private String premium_I = null;
    private String TopUpPremium_J = null;
    private String premiumAllocationCharge_K = null;
    private String topUpCharges_L = null;
    private String serviceTaxOnAllocation_M = null;
    private String AmountAvailableForInvestment_N = null;
    private String transferPercentIfAny_O = null;
    private String allocatedFundToIndexFund_P = null;
    private String sumAssuredRelatedCharges_O = null;
    private String optionCharges_R = null;
    private String riderCharges_P = null;
    private String policyAdministrationCharge_Q = null;
    private String mortalityCharges_R = null;
    private String guaranteeCharge_U = null;
    private String totalCharges_S = null;
    private String totalServiceTax_ExclOfSTonAllocAndSurr_T = null;
    private String totalServiceTax_U = null;
    private String transferOfIndexFundGainToDailyProtectFund_Y = null;
    private String transferOfCapitalFromIndexToDailyProtectFund_Z = null;
    private String dailyProtectFundAtStart_AA = null;
    private String indexFundAtStart_AB = null;
    private String dailyProtectFundAfterCharges_AC = null;
    private String indexFundAtStartAfterCharges_AD = null;
    private String additionToDailyProtectFund_AE = null;
    private String additionToIndexFund_AF = null;
    private String FMCearnedOnDailyProtectFund_AG = null;
    private String FMCearnedOnIndexFund_AH = null;
    private String additionToFundIfAny_V = null;
    private String fundBeforeFMC_W = null;
    private String fundManagementCharge_X = null;
    private String serviceTaxOnFMC_Y = null;
    private String fundValueAfterFMCandBeforeGA_Z = null;
    private String guaranteedAddition_AA = null;
    private String dailyProtectFundAtEnd_AO = null;
    private String indexFundAtEnd_AP = null;
    private String fundValueAtEnd_AB = null;
    private String fundValueAtEnd_AQ = null;
    private String surrenderCharges_AC = null;
    private String serviceTaxOnSurrenderCharges_AD = null;
    private String surrenderValue_AE = null;
    private String deathBenefit_AF = null;
    private String mortalityCharges_AG = null;
    private String guaranteeCharge_AW = null;
    private String totalCharges_AH = null;
    private String totalServiceTax_ExclOfSTonAllocAndsurr_AI = null;
    private String totalServiceTax_AJ = null;
    private String transferOfIndexFundGainToDailyProtectFund_BA = null;
    private String transferOfCapitalFromIndexToDailyProtectFund_BB = null;
    private String dailyProtectFundAtStart_BC = null;
    private String indexFundAtStart_BD = null;
    private String dailyProtectFundAftercharges_BE = null;
    private String indexFundAtStartAfterCharges_BF = null;
    private String additionToDailyProtectFund_BG = null;
    private String additionToIndexFund_BH = null;
    private String FMCearnedOnDailyProtectFund_BI = null;
    private String FMCearnedOnIndexFund_BJ = null;
    private String additionToFundIfAny_AK = null;
    private String fundBeforeFMC_AL = null;
    private String fundManagementCharge_AM = null;
    private String serviceTaxOnFMC_AN = null;
    private String fundValueAfterFMCandBeforeGA_AO = null;
    private String guaranteedAddition_AP = null;
    private String dailyProtectFundAtend_BQ = null;
    private String indexFundAtEnd_BR = null;
    private String fundValueAtEnd_BS = null;
    private String surrenderCharges_AR = null;
    private String serviceTaxOnSurrenderCharges_AS = null;
    private String surrenderValue_AT = null;
    private String deathBenefit_AU = null;
    private String surrenderCap_AV = null;
    private String oneHundredPercentOfCummulativePremium_AW = null;
    private String fundValueAfterFMCAndBeforeGA_V34 = null;
    private String riderChargesReductionYield_P = null;
    private String mortalityChargesReductionYield_AG = null;
    private String mortalityChargesReductionYield_R = null;
    private String totalServiceTax_ExclOfSTonAllocAndsurrReductionYield_AI = null;
    private String totalServiceTax_ExclOfSTonAllocAndSurrReductionYield_T = null;
    private String totalServiceTaxReductionYield_AJ = null;
    private String totalServiceTaxReductionYield_U = null;
    private String fundBeforeFMCReductionYield_AL = null;
    private String fundBeforeFMCReductionYield_W = null;
    private String serviceTaxOnFMCReductionYield_AN = null;
    private String serviceTaxOnFMCReductionYield_Y = null;
    private String fundValueAfterFMCandBeforeGAReductionYield_Z = null;
    private String fundValueAfterFMCandBeforeGAReductionYield_AO = null;
    private String fundManagementChargeReductionYield_AM = null;
    private String fundManagementChargeReductionYield_X = null;
    private String additionToFundIfAnyReductionYield_AK = null;
    private String additionToFundIfAnyReductionYield_V = null;
    private String totalChargesReductionYield_S = null;
    private String totalChargesReductionYield_AH = null;
    private String surrenderValueReductionYield_AT = null;
    private String surrenderValueReductionYield_AE = null;
    private String deathBenefitReductionYield_AU = null;
    private String deathBenefitReductionYield_AF = null;
    private String fundValueAtEndReductionYield_AB = null;
    private String fundValueAtEndReductionYield_AQ = null;
    private String month_BM = null;
    private String netYieldAt8Percent = null;
    private String netYieldAt4Percent = null;
    private String reductionInYieldNumberOfYearsElapsedSinceInception = null;
    private String reductionInYieldMaturityAt = null;
    private String irrAnnual_BS = null;
    private String irrAnnual_BN = null;
    private String irrAnnual_BO = null;
    private String reductionYield_BS = null;
    private String reductionYield_BN = null;
    private String reductionYield_BO = null;
    private String surrenderChargesReductionYield_AR = null;
    private String surrenderChargesReductionYield_AC = null;
    private String serviceTaxOnSurrenderChargesReductionYield_AD = null;
    private String serviceTaxOnSurrenderChargesReductionYield_AS = null;
    private String SurrenderCharges_Y = null;
    private String SurrenderCharges_AO = null;

    /************************************************** BL for Reduction yield ends here *******************************************************/

    // Constructor[Initialization of object required to access general
    // calculation methods is done here in constructor]
    public SaralMahaAnandBusinessLogic() {
        SMA_Prop = new SaralMahaAnandProperties();
        cfap = new CommonForAllProd();
        saralDB = new SaralMahaAnandDB();
    }

    // *********************************************************************
    public String getFundValueAfterFMCAndBeforeGA_V34() {
        return fundValueAfterFMCAndBeforeGA_V34;
    }

    public void setFundValueAfterFMCAndBeforeGA_V34() {
        this.fundValueAfterFMCAndBeforeGA_V34 = getFundValueAfterFMCandBeforeGA_Z();
    }

    // *********************************************************************

    public String getFundValueAtEnd_AB() {
        return fundValueAtEnd_AB;
    }

    public void setFundValueAtEnd_AB() {
        this.fundValueAtEnd_AB = cfap.getRoundOffLevel2(cfap
                .getStringWithout_E((Double
                        .parseDouble(getGuaranteedAddition_AA()) + Double
                        .parseDouble(getFundValueAfterFMCandBeforeGA_Z()))));
    }

    public String getAmountAvailableForInvestment_N() {
        return AmountAvailableForInvestment_N;
    }

    public void setAmountAvailableForInvestment_N() {
        this.AmountAvailableForInvestment_N = cfap.getRoundOffLevel2(cfap
                .getStringWithout_E((Double.parseDouble(getPremium_I())
                        + Double.parseDouble(getTopUpPremium_J())
                        - Double.parseDouble(getPremiumAllocationCharge_K())
                        - Double.parseDouble(getTopUpCharges_L()) - Double
                        .parseDouble(getServiceTaxOnAllocation_M()))));
    }

    // public String getRiderCharges_P()
    // {return riderCharges_P;}
    // public void setRiderCharges_P(boolean riderCharges,boolean
    // isADBChecked,int ageAtEntry,boolean staffDisc,boolean
    // bancAssuranceDisc,int PPT,int PF,int termADB,double SA_ADB)
    // {
    // if(riderCharges)
    // {riderCharges_P=cfap.roundUp_Level2(cfap.getStringWithout_E(
    // getADBcharge(isADBChecked,ageAtEntry,staffDisc,bancAssuranceDisc,PPT,PF,termADB,SA_ADB)));}
    // else
    // {riderCharges_P=""+0;}
    // }

    public double getADBcharge(boolean isADBChecked, int ageAtEntry,
                               boolean staffDisc, boolean bancAssuranceDisc, int PPT, int PF,
                               int termADB, double SA_ADB) {
        double temp1 = 0, temp2 = 0, temp3 = 0;
        if ((getPolicyInForce_G().equals("Y"))
                && ((ageAtEntry + Integer.parseInt(getYear_F()) - 1) < SMA_Prop.maxAgeAtMaturity)
                && (ageAtEntry > (SMA_Prop.minAgeADB - 1)) && (isADBChecked)
                && (Integer.parseInt(getYear_F()) <= termADB)) {
            if (PPT == 1) {
                if (Integer.parseInt(getMonth_E()) == 1) {
                    temp1 = getADBrateSingle(termADB) / 100;
                } else {
                    temp1 = 0;
                }
            } else {
                if (Integer.parseInt(getYear_F()) <= PPT
                        && ((Integer.parseInt(getMonth_E()) - 1) % (12 / PF)) == 0) {
                    temp1 = (getADBrateRegular(termADB) / 100) / PF;
                } else {
                    temp1 = 0;
                }
            }
        } else {
            temp1 = 0;
        }
        temp2 = (SA_ADB / 1000);
        temp3 = (1 - getDiscountRate(staffDisc, bancAssuranceDisc));
        return temp1 * temp2 * temp3;
    }

    private double getDiscountRate(boolean staffDisc, boolean bancAssuranceDisc) {
        if (getYear_F().equals("1")) {
            if (staffDisc) {
                return 0.05;
            } else {
                return 0;
            }
        } else {
            if (staffDisc) {
                return 0.05;
            } else {
                return 0;
            }
        }
    }

    private int getADBrateSingle(int termADB) {
        SaralMahaAnandDB db = new SaralMahaAnandDB();
        int[] ADBsingleArr = db.getADBsinglePremiumArr();
        for (int i = 5; i <= 51; i++) {
            if (i == termADB) {
                return ADBsingleArr[i];
            }
        }
        return 0;
    }

    private double getADBrateRegular(int termADB) {
        return 50;
    }

    private String getFMCearnedOnDailyProtectFund_AG() {
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

    private String getFMCearnedOnDailyProtectFund_BI() {
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

    private String getFMCearnedOnIndexFund_AH() {
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

    private String getFMCearnedOnIndexFund_BJ() {
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

    public String getTopUpPremium_J() {
        return TopUpPremium_J;
    }

    public void setTopUpPremium_J(boolean topUp, double effectiveTopUpPrem,
                                  String addTopUp) {
        if (topUp) {
            if (getMonth_E().equals("1") && addTopUp.equals("Yes")) {
                this.TopUpPremium_J = ("" + effectiveTopUpPrem);
            } else {
                this.TopUpPremium_J = ("" + 0);
            }
        } else {
            this.TopUpPremium_J = ("" + 0);
        }
    }

    private String getAdditionToDailyProtectFund_AE() {
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

    private String getAdditionToDailyProtectFund_BG() {
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
                                         int policyTerm, double int1) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.additionToFundIfAny_V = cfap
                    .getRoundOffLevel2((cfap.getStringWithout_E((_fundValueAtEnd_AB
                            + Double.parseDouble(getAmountAvailableForInvestment_N())
                            - Double.parseDouble(getTotalCharges_S())
                            - Double.parseDouble(getTotalServiceTax_ExclOfSTonAllocAndSurr_T()) + Double
                            .parseDouble(getRiderCharges_P()))
                            * (cfap.pow((1 + int1), (double) 1 / 12) - 1))));
            // System.out.println(getMonth_E()+
            // "       "+additionToFundIfAny_V+"        "+fundValueAtEnd_AB+"      "+Double.parseDouble(getAmountAvailableForInvestment_N())+"      "+Double.parseDouble(getTotalCharges_S())+"           "+Double.parseDouble(getTotalServiceTax_ExclOfSTonAllocAndSurr_T())+"         "+Double.parseDouble(getRiderCharges_P())+"     ");

        } else {
            this.additionToFundIfAny_V = "" + 0;
        }
    }

    public String getAdditionToFundIfAny_AK() {
        return additionToFundIfAny_AK;
    }

    public void setAdditionToFundIfAny_AK(double _fundValueAtEnd_AQ,
                                          int policyTerm, double int2) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.additionToFundIfAny_AK = cfap
                    .getRoundOffLevel2(cfap.getStringWithout_E((_fundValueAtEnd_AQ
                            + Double.parseDouble(getAmountAvailableForInvestment_N())
                            - Double.parseDouble(getTotalCharges_AH())
                            - Double.parseDouble(getTotalServiceTax_ExclOfSTonAllocAndSurr_AI()) + Double
                            .parseDouble(getRiderCharges_P()))
                            * (cfap.pow((1 + int2), (double) 1 / 12) - 1)));
        } else {
            this.additionToFundIfAny_AK = "" + 0;
        }
    }

    private String getAdditionToIndexFund_AF() {
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

    private String getAdditionToIndexFund_BH() {
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

    private String getAllocatedFundToIndexFund_P() {
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

    private String getDailyProtectFundAfterCharges_AC() {
        return dailyProtectFundAfterCharges_AC;
    }

    public void setDailyProtectFundAfterCharges_AC(boolean guaranteeCharges,
                                                   double serviceTax) {
        double a = 0;
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

    private String getDailyProtectFundAftercharges_BE() {
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
            this.dailyProtectFundAtEnd_AO = cfap
                    .getRoundOffLevel2(cfap.getStringWithout_E(Double
                            .parseDouble(getDailyProtectFundAfterCharges_AC())
                            + Double.parseDouble(getAdditionToDailyProtectFund_AE())
                            - Double.parseDouble(getFMCearnedOnDailyProtectFund_AG())
                            - a * serviceTax));
        } else {
            this.dailyProtectFundAtEnd_AO = "" + 0;
        }
    }

    private String getDailyProtectFundAtStart_AA() {
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

    private String getDailyProtectFundAtStart_BC() {
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

    private String getDailyProtectFundAtEnd_BQ() {
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

    public void setDeathBenefit_AF(int policyTerm, double sumAssured) {
        if (Integer.parseInt(getYear_F()) > policyTerm) {
            this.deathBenefit_AF = "" + 0;
        } else {
            this.deathBenefit_AF = cfap
                    .getRoundOffLevel2(cfap.getStringWithout_E(Math.max(
                            Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()),
                            Math.max(sumAssured,
                                    Double.parseDouble(getFundValueAtEnd_AB())))));
        }
    }

    public String getDeathBenefit_AU() {
        return deathBenefit_AU;
    }

    public void setDeathBenefit_AU(int policyTerm, double sumAssured) {
        if (Integer.parseInt(getYear_F()) > policyTerm) {
            this.deathBenefit_AU = "" + 0;
        } else {
            this.deathBenefit_AU = cfap
                    .getRoundOffLevel2(cfap.getStringWithout_E(Math.max(
                            Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()),
                            Math.max(sumAssured,
                                    Double.parseDouble(getFundValueAtEnd_AQ())))));
        }
    }

    private String getFundBeforeFMC_W() {
        return fundBeforeFMC_W;
    }

    public void setFundBeforeFMC_W(double _fundValueAtEnd_AB, int policyTerm) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            // System.out.println(getYear_F()+"      "+getMonth_E()+"        "+_fundValueAtEnd_AB+"               "+Double.parseDouble(getAmountAvailableForInvestment_N())+"           "+Double.parseDouble(getTotalCharges_S())+"                "+Double.parseDouble(getAdditionToFundIfAny_V())+"          "+Double.parseDouble(getTotalServiceTax_ExclOfSTonAllocAndSurr_T())+"      "+Double.parseDouble(getServiceTaxOnAllocation_M()));
            // modified by vrushali chaudhari on 22nd jan 14.
            // this.fundBeforeFMC_W=
            // ""+(_fundValueAtEnd_AB+Double.parseDouble(getAmountAvailableForInvestment_N())-Double.parseDouble(getTotalCharges_S())+Double.parseDouble(getAdditionToFundIfAny_V())-Double.parseDouble(getTotalServiceTax_ExclOfSTonAllocAndSurr_T())+Double.parseDouble(getRiderCharges_P()));
            this.fundBeforeFMC_W = cfap
                    .getRoundOffLevel2New(cfap.getStringWithout_E(_fundValueAtEnd_AB
                            + Double.parseDouble(getAmountAvailableForInvestment_N())
                            - Double.parseDouble(getTotalCharges_S())
                            + Double.parseDouble(getAdditionToFundIfAny_V())
                            - Double.parseDouble(getTotalServiceTax_ExclOfSTonAllocAndSurr_T())
                            - Double.parseDouble(getServiceTaxOnAllocation_M())));
        } else {
            this.fundBeforeFMC_W = "" + 0;
        }
    }

    public String getFundBeforeFMC_AL() {
        return fundBeforeFMC_AL;
    }

    public void setFundBeforeFMC_AL(double _fundValueAtEnd_AQ, int policyTerm) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            // modified by vrushali chaudhari on 22nd jan 14.
            // this.fundBeforeFMC_AL=
            // ""+(_fundValueAtEnd_AQ+Double.parseDouble(getAmountAvailableForInvestment_N())-Double.parseDouble(getTotalCharges_AH())+Double.parseDouble(getAdditionToFundIfAny_AK())-Double.parseDouble(getTotalServiceTax_ExclOfSTonAllocAndSurr_AI())+Double.parseDouble(getRiderCharges_P()));
            this.fundBeforeFMC_AL = cfap
                    .getRoundOffLevel2(cfap.getStringWithout_E(_fundValueAtEnd_AQ
                            + Double.parseDouble(getAmountAvailableForInvestment_N())
                            - Double.parseDouble(getTotalCharges_AH())
                            + Double.parseDouble(getAdditionToFundIfAny_AK())
                            - Double.parseDouble(getTotalServiceTax_ExclOfSTonAllocAndSurr_AI())
                            - Double.parseDouble(getServiceTaxOnAllocation_M())));

        } else {
            this.fundBeforeFMC_AL = "" + 0;
        }
    }

    public String getFundManagementCharge_X() {
        return fundManagementCharge_X;
    }

    public void setFundManagementCharge_X(int policyTerm,
                                          double percentToBeInvested_EquityFund,
                                          double percentToBeInvested_BondFund,
                                          double percentToBeInvested_BalancedFund,
                                          double percentToBeInvested_IndexFund) {
        double temp1 = 0, temp2 = 0;
        if (Integer.parseInt(getMonth_E()) == 1) {
            temp1 = SMA_Prop.charge_Fund;
        } else {
            temp1 = 0;
        }
        temp2 = getCharge_fund_ren(percentToBeInvested_EquityFund,
                percentToBeInvested_BondFund, percentToBeInvested_BalancedFund,
                percentToBeInvested_IndexFund) / 12;

        // System.out.println("getFundBeforeFMC_W "+getFundBeforeFMC_W());
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.fundManagementCharge_X = cfap.getRoundOffLevel2New(cfap
                    .getStringWithout_E(Double
                            .parseDouble(getFundBeforeFMC_W())
                            * (temp1 + temp2)));
        } else {
            this.fundManagementCharge_X = "" + 0;
        }
    }

    public String getFundManagementCharge_AM() {
        return fundManagementCharge_AM;
    }

    public void setFundManagementCharge_AM(int policyTerm,
                                           double percentToBeInvested_EquityFund,
                                           double percentToBeInvested_BondFund,
                                           double percentToBeInvested_BalancedFund,
                                           double percentToBeInvested_IndexFund) {
        double temp1 = 0, temp2 = 0;
        if (Integer.parseInt(getMonth_E()) == 1) {
            temp1 = SMA_Prop.charge_Fund;
        } else {
            temp1 = 0;
        }
        temp2 = getCharge_fund_ren(percentToBeInvested_EquityFund,
                percentToBeInvested_BondFund, percentToBeInvested_BalancedFund,
                percentToBeInvested_IndexFund) / 12;
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.fundManagementCharge_AM = cfap.getRoundOffLevel2(cfap
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
            double val = (Double.parseDouble(getFundBeforeFMC_AL())
                    - Double.parseDouble(getFundManagementCharge_AM()) - Double
                    .parseDouble(getServiceTaxOnFMC_AN()));
            this.fundValueAfterFMCandBeforeGA_AO = cfap.getRoundOffLevel2(cfap
                    .getStringWithout_E(val));
        } else {
            this.fundValueAfterFMCandBeforeGA_AO = "" + 0;
        }
    }

    public String getFundValueAfterFMCandBeforeGA_Z() {
        return fundValueAfterFMCandBeforeGA_Z;
    }

    public void setFundValueAfterFMCandBeforeGA_Z(int policyTerm) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            // System.out.println(Double.parseDouble(getFundBeforeFMC_W())+"             "+Double.parseDouble(getFundManagementCharge_X())+"            "+Double.parseDouble(getServiceTaxOnFMC_Y()));
            this.fundValueAfterFMCandBeforeGA_Z = cfap.getRoundOffLevel2(cfap
                    .getStringWithout_E(Double
                            .parseDouble(getFundBeforeFMC_W())
                            - Double.parseDouble(getFundManagementCharge_X())
                            - Double.parseDouble(getServiceTaxOnFMC_Y())));
        } else {
            this.fundValueAfterFMCandBeforeGA_Z = "" + 0;
        }
    }

    public String getFundValueAtEnd_AQ() {
        return fundValueAtEnd_AQ;
    }

    public void setFundValueAtEnd_AQ() {
        double val = (Double.parseDouble(getGuaranteedAddition_AP()) + Double
                .parseDouble(getFundValueAfterFMCandBeforeGA_AO()));
        this.fundValueAtEnd_AQ = cfap.getRoundOffLevel2(cfap
                .getStringWithout_E(val));
    }

    public String getFundValueAtEnd_BS() {
        return fundValueAtEnd_BS;
    }

    public void setFundValueAtEnd_BS() {
        this.fundValueAtEnd_BS = cfap.getRoundOffLevel2(cfap
                .getStringWithout_E(Double
                        .parseDouble(getGuaranteedAddition_AP())
                        + Double.parseDouble(getDailyProtectFundAtEnd_BQ())
                        + Double.parseDouble(getIndexFundAtEnd_BR())));
    }

    private String getGuaranteeCharge_AW() {
        return guaranteeCharge_AW;
    }

    public void setGuaranteeCharge_AW(boolean mortalityCharges,
                                      double charge_Guarantee) {
        if (getPolicyInForce_G().equals("Y") && mortalityCharges) {
            this.guaranteeCharge_AW = cfap.getRoundOffLevel2(cfap
                    .getStringWithout_E(Double
                            .parseDouble(getDailyProtectFundAtStart_BC())
                            * charge_Guarantee / 12));
        } else {
            this.guaranteeCharge_AW = "" + 0;
        }
    }

    private String getGuaranteeCharge_U() {
        return guaranteeCharge_U;
    }

    public void setGuaranteeCharge_U(boolean guaranteeCharges,
                                     double charge_Guarantee) {
        if (getPolicyInForce_G().equals("Y") && guaranteeCharges) {
            this.guaranteeCharge_U = cfap.getRoundOffLevel2(cfap
                    .getStringWithout_E(Double
                            .parseDouble(getDailyProtectFundAtStart_AA())
                            * charge_Guarantee / 12));
        } else {
            this.guaranteeCharge_U = "" + 0;
        }
    }

    public String getGuaranteedAddition_AA() {
        return guaranteedAddition_AA;
    }

    public void setGuaranteedAddition_AA(double effectivePremium, int PPT,
                                         boolean asPercentOfFirstYrPremium) {
        if (getPolicyInForce_G().equals("N")) {
            this.guaranteedAddition_AA = "" + 0;
        } else {
            this.guaranteedAddition_AA = cfap.getRoundOffLevel2(cfap
                    .getStringWithout_E(effectivePremium
                            * getPercentOfFirstYrPremium(PPT, effectivePremium,
                            asPercentOfFirstYrPremium)));
        }
    }

    public String getGuaranteedAddition_AP() {
        return guaranteedAddition_AP;
    }

    public void setGuaranteedAddition_AP(double effectivePremium, int PPT,
                                         boolean asPercentOfFirstYrPremium) {
        if (getPolicyInForce_G().equals("N")) {
            this.guaranteedAddition_AP = "" + 0;
        } else {
            this.guaranteedAddition_AP = cfap.getRoundOffLevel2(cfap
                    .getStringWithout_E(effectivePremium
                            * getPercentOfFirstYrPremium(PPT, effectivePremium,
                            asPercentOfFirstYrPremium)));
        }
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
            this.indexFundAtEnd_AP = cfap.getRoundOffLevel2(cfap
                    .getStringWithout_E(Double
                            .parseDouble(getIndexFundAtStartAfterCharges_AD())
                            + Double.parseDouble(getAdditionToIndexFund_AF())
                            - Double.parseDouble(getFMCearnedOnIndexFund_AH())
                            - a * serviceTax));
        } else {
            this.indexFundAtEnd_AP = "" + 0;
        }
    }

    private String getIndexFundAtEnd_BR() {
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
            this.indexFundAtEnd_BR = cfap.getRoundOffLevel2(cfap
                    .getStringWithout_E(Double
                            .parseDouble(getIndexFundAtStartAfterCharges_BF())
                            + Double.parseDouble(getAdditionToIndexFund_BH())
                            - Double.parseDouble(getFMCearnedOnIndexFund_BJ())
                            - a * serviceTax));
        } else {
            this.indexFundAtEnd_BR = "" + 0;
        }
    }

    private String getIndexFundAtStartAfterCharges_AD() {
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

    private String getIndexFundAtStartAfterCharges_BF() {
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

    private String getIndexFundAtStart_AB() {
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

    private String getIndexFundAtStart_BD() {
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

    public String getMortalityCharges_AG() {
        return mortalityCharges_AG;
    }

    public void setMortalityCharges_AG(double _fundValueAtEnd_AQ,
                                       boolean mortalityCharges, double sumAssured, int policyTerm,
                                       String[] forBIArray, int ageAtEntry) {
        if (!(getPolicyInForce_G().equals("Y"))
                || Integer.parseInt(getYear_F()) > policyTerm) {
            this.mortalityCharges_AG = "" + 0;
        } else {
            // modified by Vrushali Chaudhari on 22nd jan 14.
            double arrOutput = Double
                    .parseDouble(forBIArray[(ageAtEntry + Integer
                            .parseInt(getYear_F())) - 1]);
            // double a=1-arrOutput/1000;
            double div = arrOutput / 1000;
            double div1 = div / 12;
            double a = 1 - div1;
            // double b=(double)1/12;
            // Commented by Akshaya Mirajkar on 23,Apr,2013.
            // double c=Math.max(sumAssured,
            // Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()))-(Double.parseDouble(getAmountAvailableForInvestment_N())+_fundValueAtEnd_AB);
            double c = sumAssured
                    - (Double.parseDouble(getAmountAvailableForInvestment_N()) + _fundValueAtEnd_AQ);
            int d = 0;
            if (mortalityCharges) {
                d = 1;
            } else {
                d = 0;
            }

            this.mortalityCharges_AG = cfap.getRoundOffLevel2((cfap
                    .getStringWithout_E((((1 - (a)) * Math.max(c, 0)) * d))));
        }
    }

    public String getMortalityCharges_R() {
        return mortalityCharges_R;
    }

    public void setMortalityCharges_R(double _fundValueAtEnd_AB,
                                      int policyTerm, String[] forBIArray, int ageAtEntry,
                                      double sumAssured, boolean mortalityCharges) {
        if (!(getPolicyInForce_G().equals("Y"))
                || Integer.parseInt(getYear_F()) > policyTerm) {
            this.mortalityCharges_R = "" + 0;
        } else {
            // modified by Vrushali Chaudhari on 22nd jan 14.
            double arrOutput = Double
                    .parseDouble(forBIArray[(ageAtEntry + Integer
                            .parseInt(getYear_F())) - 1]);
            // double a=1-arrOutput/1000;
            double div = arrOutput / 1000;
            double div1 = div / 12;
            double a = 1 - div1;
            // double b=(double)1/12;
            // Commented by Akshaya Mirajkar on 23,Apr,2013.
            // double c=Math.max(sumAssured,
            // Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()))-(Double.parseDouble(getAmountAvailableForInvestment_N())+_fundValueAtEnd_AB);
            double c = sumAssured
                    - (Double.parseDouble(getAmountAvailableForInvestment_N()) + _fundValueAtEnd_AB);

            int d = 0;
            if (mortalityCharges) {
                d = 1;
            } else {
                d = 0;
            }

            this.mortalityCharges_R = cfap.getRoundOffLevel2(cfap
                    .getStringWithout_E((((1 - (a)) * Math.max(c, 0)) * d)));
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
            double fixedMonthlyExp_RP, int inflation_pa_RP) {
        if (getPolicyInForce_G().equals("Y")) {
            if (((Integer.parseInt(getMonth_E()) - 1) % 12) == 0) {
                this.policyAdministrationCharge_Q = cfap
                        .getRoundOffLevel2(cfap.getStringWithout_E(getCharge_PP_Ren(
                                fixedMonthlyExp_RP, inflation_pa_RP)
                                / 12
                                * cfap.getPowerOutput(
                                (1 + charge_Inflation),
                                Integer.parseInt(""
                                        + (Integer
                                        .parseInt(getMonth_E()) / 12)))));
            } else {
                this.policyAdministrationCharge_Q = cfap.getRoundOffLevel2(cfap
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
                                             boolean bancAssuranceDisc) {
        if (Integer.parseInt(getYear_F()) > 10) {
            this.premiumAllocationCharge_K = ("" + 0);
        } else {
            this.premiumAllocationCharge_K = cfap.getRoundOffLevel2(cfap
                    .getStringWithout_E(getAllocationCharge(staffDisc,
                            bancAssuranceDisc)
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
                premium_I = cfap.getRoundOffLevel2(cfap
                        .getStringWithout_E(effectivePrem / PF));
            } else {
                premium_I = ("" + 0);
            }
        } else {
            premium_I = ("" + 0);
        }
    }

    private String getServiceTaxOnAllocation_M() {
        return serviceTaxOnAllocation_M;
    }

    public void setServiceTaxOnAllocation_M(boolean allocationCharges,
                                            double serviceTax) {
        if (allocationCharges) {
            this.serviceTaxOnAllocation_M = cfap
                    .getRoundOffLevel2(cfap.getStringWithout_E((Double
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
                                     double serviceTax, double percentToBeInvested_EquityFund,
                                     double percentToBeInvested_BondFund,
                                     double percentToBeInvested_BalancedFund,
                                     double percentToBeInvested_IndexFund) {
        // System.out.println("X" + getFundManagementCharge_X() + "charge Fund "
        // + getCharge_fund_ren( percentToBeInvested_EquityFund,
        // percentToBeInvested_BondFund, percentToBeInvested_BalancedFund,
        // percentToBeInvested_IndexFund));

        double a = 0;
        if (fundManagementCharges) {
            a = (Double.parseDouble(getFundManagementCharge_X()) * (0.0135 / getCharge_fund_ren(
                    percentToBeInvested_EquityFund,
                    percentToBeInvested_BondFund,
                    percentToBeInvested_BalancedFund,
                    percentToBeInvested_IndexFund)));
        } else {
            a = 0;
        }
        this.serviceTaxOnFMC_Y = cfap.getRoundOffLevel2New(cfap
                .getStringWithout_E(a * serviceTax));

        // System.out.println("a* serviceTax" + (a* serviceTax) + "a" + a);
    }

    public String getServiceTaxOnFMC_AN() {
        return serviceTaxOnFMC_AN;
    }

    public void setServiceTaxOnFMC_AN(boolean fundManagementCharges,
                                      double serviceTax, double percentToBeInvested_EquityFund,
                                      double percentToBeInvested_BondFund,
                                      double percentToBeInvested_BalancedFund,
                                      double percentToBeInvested_IndexFund) {
        double a = 0;
        if (fundManagementCharges) {
            a = (Double.parseDouble(getFundManagementCharge_AM()) * (0.0135 / getCharge_fund_ren(
                    percentToBeInvested_EquityFund,
                    percentToBeInvested_BondFund,
                    percentToBeInvested_BalancedFund,
                    percentToBeInvested_IndexFund)));
        } else {
            a = 0;
        }
        this.serviceTaxOnFMC_AN = cfap.getRoundOffLevel2(cfap
                .getStringWithout_E(a * serviceTax));
    }

    private String getServiceTaxOnSurrenderCharges_AD() {
        return serviceTaxOnSurrenderCharges_AD;
    }

    public void setServiceTaxOnSurrenderCharges_AD(boolean surrenderCharges,
                                                   double serviceTax) {
        if (surrenderCharges) {
            this.serviceTaxOnSurrenderCharges_AD = cfap
                    .getRoundOffLevel2(cfap.getStringWithout_E(Double
                            .parseDouble(getSurrenderCharges_AC()) * serviceTax));
        } else {
            this.serviceTaxOnSurrenderCharges_AD = "" + 0;
        }
    }

    public String getServiceTaxOnSurrenderCharges_AS() {
        return serviceTaxOnSurrenderCharges_AS;
    }

    public void setServiceTaxOnSurrenderCharges_AS(double serviceTax,
                                                   boolean surrenderCharges) {
        if (surrenderCharges) {
            this.serviceTaxOnSurrenderCharges_AS = cfap
                    .getRoundOffLevel2(cfap.getStringWithout_E(Double
                            .parseDouble(getSurrenderCharges_AR()) * serviceTax));
        } else {
            this.serviceTaxOnSurrenderCharges_AS = "" + 0;
        }
    }

    private String getSumAssuredRelatedCharges_O() {
        return sumAssuredRelatedCharges_O;
    }

    public void setSumAssuredRelatedCharges_O(int policyTerm,
                                              double sumAssured, double SAMF, double effectivePremium,
                                              double charge_SumAssuredBase) {
        double a = 0;
        double b = 0;
        if (getMonth_E().equals("1")) {
            a = charge_SumAssuredBase;
        } else {
            a = 0;
        }
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            b = charge_SumAssuredBase;
        } else {
            b = 0;
        }
        this.sumAssuredRelatedCharges_O = cfap.getRoundOffLevel2(cfap
                .getStringWithout_E(sumAssured * (a + b)));
    }

    public String getSurrenderCap_AV() {
        return surrenderCap_AV;
    }

    public void setSurrenderCap_AV(double effectivePremium) {
        if (getPolicyInForce_G().equals("Y")) {
            this.surrenderCap_AV = cfap.getRoundOffLevel2(cfap
                    .getStringWithout_E(Double
                            .parseDouble(calCapArr(effectivePremium)[Integer
                                    .parseInt(getYear_F()) - 1])));
        } else {
            this.surrenderCap_AV = "" + 0;
        }
    }

    public String getSurrenderCharges_AC() {
        return surrenderCharges_AC;
    }

    public void setSurrenderCharges_AC(double effectivePremium, int PPT) {
        double a = Math.min(Double.parseDouble(getFundValueAtEnd_AB()),
                effectivePremium);
        double b = getSurrenderCharge(effectivePremium, PPT);
        this.surrenderCharges_AC = cfap.getRoundOffLevel2(cfap
                .getStringWithout_E(Math.min((a * b),
                        Double.parseDouble(getSurrenderCap_AV()))));
    }

    public String getSurrenderCharges_AR() {
        return surrenderCharges_AR;
    }

    public void setSurrenderCharges_AR(double effectivePremium, int PPT) {
        double a = Math.min(Double.parseDouble(getFundValueAtEnd_AQ()),
                effectivePremium);
        double b = getSurrenderCharge(effectivePremium, PPT);
        this.surrenderCharges_AR = cfap.getRoundOffLevel2(cfap
                .getStringWithout_E(Math.min((a * b),
                        Double.parseDouble(getSurrenderCap_AV()))));
    }

    public String getSurrenderValue_AE() {
        return surrenderValue_AE;
    }

    public void setSurrenderValue_AE() {
        this.surrenderValue_AE = cfap
                .getRoundOffLevel2(cfap.getStringWithout_E(Double
                        .parseDouble(getFundValueAtEnd_AB())
                        - Double.parseDouble(getSurrenderCharges_AC())
                        - Double.parseDouble(getServiceTaxOnSurrenderCharges_AD())));
    }

    public String getSurrenderValue_AT() {
        return surrenderValue_AT;
    }

    public void setSurrenderValue_AT() {
        this.surrenderValue_AT = cfap
                .getRoundOffLevel2(cfap.getStringWithout_E(Double
                        .parseDouble(getFundValueAtEnd_AQ())
                        - Double.parseDouble(getSurrenderCharges_AR())
                        - Double.parseDouble(getServiceTaxOnSurrenderCharges_AS())));
    }

    private String getTopUpCharges_L() {
        return topUpCharges_L;
    }

    public void setTopUpCharges_L(double topUp) {
        this.topUpCharges_L = cfap.getRoundOffLevel2(cfap
                .getStringWithout_E(Double.parseDouble(getTopUpPremium_J())
                        * topUp));
    }

    public String getTotalCharges_AH() {
        return totalCharges_AH;
    }

    public void setTotalCharges_AH(int policyTerm) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.totalCharges_AH = cfap
                    .getRoundOffLevel2(cfap.getStringWithout_E(Double
                            .parseDouble(getPolicyAdministrationCharge_Q())
                            + Double.parseDouble(getMortalityCharges_AG())
                            + Double.parseDouble(getSumAssuredRelatedCharges_O())
                            + Double.parseDouble(getRiderCharges_P())));
        } else {
            this.totalCharges_AH = "" + 0;
        }
    }

    public String getTotalCharges_S() {
        return totalCharges_S;
    }

    public void setTotalCharges_S(int policyTerm) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.totalCharges_S = cfap
                    .getRoundOffLevel2(cfap.getStringWithout_E(Double
                            .parseDouble(getPolicyAdministrationCharge_Q())
                            + Double.parseDouble(getMortalityCharges_R())
                            + Double.parseDouble(getSumAssuredRelatedCharges_O())
                            + Double.parseDouble(getRiderCharges_P())));
        } else {
            this.totalCharges_S = "" + 0;
        }
    }

    public String getTotalServiceTax_AJ() {
        return totalServiceTax_AJ;
    }

    public void setTotalServiceTax_AJ(boolean riderCharges) {
        //double temp = 0;
        if (riderCharges) {
            //temp = Double.parseDouble(getRiderCharges_P());
        } else {
            //temp = 0;
        }
        // modified by vrushali chaudhari on 22nd jan 14.
        // this.totalServiceTax_AJ
        // =cfap.roundUp_Level2(cfap.getStringWithout_E(Double.parseDouble(getServiceTaxOnAllocation_M())+Double.parseDouble(getTotalServiceTax_ExclOfSTonAllocAndSurr_AI())+Double.parseDouble(getServiceTaxOnFMC_AN())+(temp*SMA_Prop.serviceTax)));
        this.totalServiceTax_AJ = cfap
                .getRoundOffLevel2(cfap.getStringWithout_E(Double
                        .parseDouble(getServiceTaxOnAllocation_M())
                        + Double.parseDouble(getTotalServiceTax_ExclOfSTonAllocAndSurr_AI())
                        + Double.parseDouble(getServiceTaxOnFMC_AN())));

    }

    // modified by vrushali chaudhari on 22nd jan 14
    public String getTotalServiceTax_ExclOfSTonAllocAndSurr_AI() {
        return totalServiceTax_ExclOfSTonAllocAndsurr_AI;
    }

    public void setTotalServiceTax_ExclOfSTonAllocAndSurr_AI(double serviceTax,
                                                             boolean mortalityAndRiderCharges, boolean administrationCharges,
                                                             boolean riderCharges) {
        double a = 0, b = 0, c = 0, d = 0, e = 0;
        if (mortalityAndRiderCharges) {
            a = Double.parseDouble(getMortalityCharges_AG());
        } else {
            a = 0;
        }
        if (administrationCharges) {
            b = Double.parseDouble(getPolicyAdministrationCharge_Q());
        } else {
            b = 0;
        }
        if (riderCharges) {
            c = Double.parseDouble(getRiderCharges_P());
        } else {
            c = 0;
        }
        d = ((a + b) * serviceTax);
        e = (c * serviceTax);
        this.totalServiceTax_ExclOfSTonAllocAndsurr_AI = cfap
                .roundUp_Level2(cfap.getStringWithout_E((d + e)));
    }

    public String getTotalServiceTax_U() {
        return totalServiceTax_U;
    }

    public void setTotalServiceTax_U(boolean riderCharges) {
		/*double temp = 0;
		if (riderCharges) {
			temp = Double.parseDouble(getRiderCharges_P());
		} else {
			temp = 0;
		}*/
        // modified by vrushali chaudhari on 22nd jan 14.
        // this.totalServiceTax_U
        // =cfap.roundUp_Level2(cfap.getStringWithout_E(Double.parseDouble(getServiceTaxOnAllocation_M())+Double.parseDouble(getTotalServiceTax_ExclOfSTonAllocAndSurr_T())+Double.parseDouble(getServiceTaxOnFMC_Y())
        // + (temp*SMA_Prop.serviceTax)));
        this.totalServiceTax_U = cfap
                .getRoundOffLevel2(cfap.getStringWithout_E(Double
                        .parseDouble(getServiceTaxOnAllocation_M())
                        + Double.parseDouble(getTotalServiceTax_ExclOfSTonAllocAndSurr_T())
                        + Double.parseDouble(getServiceTaxOnFMC_Y())));
    }

    // modified by vrushali chaudhari on 22nd jan 14.
    private String getTotalServiceTax_ExclOfSTonAllocAndSurr_T() {
        return totalServiceTax_ExclOfSTonAllocAndSurr_T;
    }

    public void setTotalServiceTax_ExclOfSTonAllocAndSurr_T(double serviceTax,
                                                            boolean mortalityAndRiderCharges, boolean administrationCharges,
                                                            boolean riderCharges) {
        double a = 0, b = 0, c = 0, d = 0, e = 0;
        if (mortalityAndRiderCharges) {
            a = Double.parseDouble(getMortalityCharges_R());
        } else {
            a = 0;
        }
        if (administrationCharges) {
            b = Double.parseDouble(getPolicyAdministrationCharge_Q());
        } else {
            b = 0;
        }
        if (riderCharges) {
            c = Double.parseDouble(getRiderCharges_P());
        } else {
            c = 0;
        }
        String test = Double.toString(serviceTax);
        // System.out.println(test);
        double temp = (a + b);
        d = (temp * Double.parseDouble(test));
        e = (c * serviceTax);
        // if(getMonth_E().equals("51"))
        // System.out.println("     " + (d+e));
        this.totalServiceTax_ExclOfSTonAllocAndSurr_T = cfap
                .getRoundOffLevel2(cfap.getStringWithout_E(d + e));
    }

    private String getTransferOfCapitalFromIndexToDailyProtectFund_BB() {
        return transferOfCapitalFromIndexToDailyProtectFund_BB;
    }

    public void setTransferOfCapitalFromIndexToDailyProtectFund_BB(double br18) {
        if (getPolicyInForce_G().equals("Y")) {
            this.transferOfCapitalFromIndexToDailyProtectFund_BB = cfap
                    .getRoundOffLevel2(cfap.getStringWithout_E(br18
                            * Double.parseDouble(getTransferPercentIfAny_O())));
        } else {
            this.transferOfCapitalFromIndexToDailyProtectFund_BB = "" + 0;
        }
    }

    private String getTransferOfCapitalFromIndexToDailyProtectFund_Z() {
        return transferOfCapitalFromIndexToDailyProtectFund_Z;
    }

    public void setTransferOfCapitalFromIndexToDailyProtectFund_Z(
            double _indexFundAtEnd) {
        if (getPolicyInForce_G().equals("Y")) {
            this.transferOfCapitalFromIndexToDailyProtectFund_Z = cfap
                    .getRoundOffLevel2(cfap.getStringWithout_E(Double
                            .parseDouble(getTransferPercentIfAny_O())
                            * _indexFundAtEnd));
        }
    }

    private String getTransferOfIndexFundGainToDailyProtectFund_BA() {
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
                this.transferOfIndexFundGainToDailyProtectFund_BA = cfap
                        .getRoundOffLevel2(cfap.getStringWithout_E(a));
            } else {
                this.transferOfIndexFundGainToDailyProtectFund_BA = "" + 0;
            }
        } else {
            this.transferOfIndexFundGainToDailyProtectFund_BA = "" + 0;
        }
    }

    private String getTransferOfIndexFundGainToDailyProtectFund_Y() {
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
                    this.transferOfIndexFundGainToDailyProtectFund_Y = cfap
                            .getRoundOffLevel2(cfap.getStringWithout_E(_indexFundAtEnd
                                    - Double.parseDouble(getAllocatedFundToIndexFund_P())));
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

    private String getTransferPercentIfAny_O() {
        return transferPercentIfAny_O;
    }

    public void setTransferPercentIfAny_O(int year_TransferOfCapital_W62,
                                          int year_TransferOfCapital_W63, String transferFundStatus) {
        double x62 = 0;
        if (getMonth_E().equals("" + (12 * year_TransferOfCapital_W62 - 6))) {
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

    private double getSurrenderCharge(double effectivePremium, int PPT) {
        double surrenderCharge = 0;
        if (PPT == 1) {/*
         * For Saral Maha Anand Product PPT will never equal to
         * 1
         */
        } else {
            surrenderCharge = Double
                    .parseDouble(calSurrRateArr(effectivePremium)[Integer
                            .parseInt(getYear_F()) - 1]);
        }
        return surrenderCharge;
    }

    private double getAllocationCharge(boolean staffDisc,
                                       boolean bancAssuranceDisc) {
        int yearFactor = 0;
        if (getYear_F().equals("1")) {
            yearFactor = 1;
        } else {
            yearFactor = 0;
        }
        if (getYear_F().equals("1")) {
            if (staffDisc == true && bancAssuranceDisc == false) {
                return Math.max(0, (0.0625 - 0.06));
            } else {
                if (staffDisc == false && bancAssuranceDisc == true) {
                    return Math.max(0, (0.0625 - 0.06 * yearFactor));
                } else {
                    return Math.max(0, 0.0625);
                }
            }
        } else if (getYear_F().equals("2")) {
            if (staffDisc == true && bancAssuranceDisc == false) {
                return Math.max(0, (0.0375 - 0.0275));
            } else {
                if (staffDisc == false && bancAssuranceDisc == true) {
                    return Math.max(0, (0.0375 - 0.0275 * yearFactor));
                } else {
                    return Math.max(0, 0.0375);
                }
            }
        } else if (getYear_F().equals("3")) {
            if (staffDisc == true && bancAssuranceDisc == false) {
                return Math.max(0, (0.0375 - 0.0275));
            } else {
                if (staffDisc == false && bancAssuranceDisc == true) {
                    return Math.max(0, (0.0375 - 0.0275 * yearFactor));
                } else {
                    return Math.max(0, 0.0375);
                }
            }
        } else if (getYear_F().equals("4")) {
            if (staffDisc == true && bancAssuranceDisc == false) {
                return Math.max(0, (0.0375 - 0.0275));
            } else {
                if (staffDisc == false && bancAssuranceDisc == true) {
                    return Math.max(0, (0.0375 - 0.0275 * yearFactor));
                } else {
                    return Math.max(0, 0.0375);
                }
            }
        } else if (getYear_F().equals("5")) {
            if (staffDisc == true && bancAssuranceDisc == false) {
                return Math.max(0, (0.0375 - 0.0275));
            } else {
                if (staffDisc == false && bancAssuranceDisc == true) {
                    return Math.max(0, (0.0375 - 0.0275 * yearFactor));
                } else {
                    return Math.max(0, 0.0375);
                }
            }
        } else if (getYear_F().equals("6")) {
            if (staffDisc == true && bancAssuranceDisc == false) {
                return Math.max(0, (0.03 - 0.02));
            } else {
                if (staffDisc == false && bancAssuranceDisc == true) {
                    return Math.max(0, (0.03 - 0.02 * yearFactor));
                } else {
                    return Math.max(0, 0.03);
                }
            }
        } else if (getYear_F().equals("7")) {
            if (staffDisc == true && bancAssuranceDisc == false) {
                return Math.max(0, (0.03 - 0.02));
            } else {
                if (staffDisc == false && bancAssuranceDisc == true) {
                    return Math.max(0, (0.03 - 0.02 * yearFactor));
                } else {
                    return Math.max(0, 0.03);
                }
            }
        } else if (getYear_F().equals("8")) {
            if (staffDisc == true && bancAssuranceDisc == false) {
                return Math.max(0, (0.03 - 0.02));
            } else {
                if (staffDisc == false && bancAssuranceDisc == true) {
                    return Math.max(0, (0.03 - 0.02 * yearFactor));
                } else {
                    return Math.max(0, 0.03);
                }
            }
        } else if (getYear_F().equals("9")) {
            if (staffDisc == true && bancAssuranceDisc == false) {
                return Math.max(0, (0.03 - 0.02));
            } else {
                if (staffDisc == false && bancAssuranceDisc == true) {
                    return Math.max(0, (0.03 - 0.02 * yearFactor));
                } else {
                    return Math.max(0, 0.03);
                }
            }
        } else if (getYear_F().equals("10")) {
            if (staffDisc == true && bancAssuranceDisc == false) {
                return Math.max(0, (0.03 - 0.02));
            } else {
                if (staffDisc == false && bancAssuranceDisc == true) {
                    return Math.max(0, (0.03 - 0.02 * yearFactor));
                } else {
                    return Math.max(0, 0.03);
                }
            }
        }
        // For Policuy Term greater than 11
        else {
            if (staffDisc == true && bancAssuranceDisc == false) {
                return Math.max(0, (0 - 0.01));
            } else {
                if (staffDisc == false && bancAssuranceDisc == true) {
                    return Math.max(0, (0 - 0.01 * yearFactor));
                } else {
                    return Math.max(0, 0);
                }
            }
        }
    }

    public double getCharge_PP_Ren(double fixedMonthlyExp_RP,
                                   int inflation_pa_RP) {
        return fixedMonthlyExp_RP * 12 * cfap.pow(1 + inflation_pa_RP, 0);
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

    public String[] calSurrRateArr(double effectivePremium) {
        if (effectivePremium <= 25000) {
            return new String[]{"0.20", "0.15", "0.10", "0.05", "0", "0",
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
                    "0", "0", "0", "0"};
        }
    }

    public String[] calCapArr(double effectivePremium) {
        if (effectivePremium <= 25000) {
            return new String[]{"3000", "2000", "1500", "1000", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0"};
        } else {
            return new String[]{"6000", "5000", "4000", "2000", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0"};
        }
    }

    public String[] getForBIArr(double mortalityCharges_AsPercentOfofLICtable) {
		/*int[] ageArr = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
				13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28,
				29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44,
				45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60,
				61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76,
				77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92,
				93, 94, 95, 96, 97, 98, 99, 100 };*/
        String[] strArrTReturn = new String[99];
        SaralMahaAnandDB saralMahaAnandDB = new SaralMahaAnandDB();
        for (int i = 0; i < saralMahaAnandDB.getIAIarray().length - 2; i++) {
            strArrTReturn[i] = cfap
                    .roundUp_Level2(cfap.getStringWithout_E(((saralMahaAnandDB
                            .getIAIarray()[i] + saralMahaAnandDB.getIAIarray()[i + 1]) / 2)
                            * mortalityCharges_AsPercentOfofLICtable * 1000));
        }
        return strArrTReturn;
    }

    public double getPercentOfFirstYrPremium(int PPT, double effectivePremium,
                                             boolean asPercentOfFirstYrPremium) {
        if (asPercentOfFirstYrPremium) {
            if (getMonth_E().equals("60")) {
                if (PPT >= 5) {
                    return 0;
                } else {
                    return 0;
                }
            } else if (getMonth_E().equals("84")) {
                if (PPT >= 7) {
                    return 0;
                } else {
                    return 0;
                }
            } else if (getMonth_E().equals("120")) {
                if (PPT >= 10) {
                    return 0.05;
                } else {
                    return 0;
                }
            } else if (getMonth_E().equals("180")) {
                if (PPT >= 15) {
                    return 0.1;
                } else {
                    return 0;
                }
            } else if (getMonth_E().equals("240")) {
                if (PPT >= 20) {
                    return 0.15;
                } else {
                    return 0;
                }
            } else if (getMonth_E().equals("300")) {
                if (PPT >= 25) {
                    return 0;
                } else {
                    return 0;
                }
            } else if (getMonth_E().equals("360")) {
                if (PPT >= 30) {
                    return 0;
                } else {
                    return 0;
                }
            } else if (getMonth_E().equals("1188")) {
                if (PPT >= 99) {
                    return 0;
                } else {
                    return 0;
                }
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public double getCharge_fund_ren(double percentToBeInvested_EquityFund,
                                     double percentToBeInvested_BondFund,
                                     double percentToBeInvested_BalancedFund,
                                     double percentToBeInvested_IndexFund) {
        return percentToBeInvested_EquityFund * SMA_Prop.FMC_EquityFund
                + percentToBeInvested_BondFund * SMA_Prop.FMC_BondFund
                + percentToBeInvested_BalancedFund * SMA_Prop.FMC_BalancedFund
                + percentToBeInvested_IndexFund * SMA_Prop.FMC_IndexFund;
    }

    public String getRiderCharges_P() {
        return riderCharges_P;
    }

    public void setRiderCharges_P(boolean riderCharges, boolean isADBChecked,
                                  int ageAtEntry, boolean staffDisc, boolean bancAssuranceDisc,
                                  int PPT, int PF, int termADB, double SA_ADB) {
        double riderChargePerAnnum;
        if (Integer.parseInt(getMonth_E()) <= (termADB * 12)) {
            riderChargePerAnnum = ((50 * SA_ADB) / 100000);
            this.riderCharges_P = cfap.roundUp_Level2(cfap
                    .getStringWithout_E(riderChargePerAnnum / 12));
        } else {
            riderCharges_P = "" + 0;
        }
    }

    public String getSurrenderCharges_Y() {
        return SurrenderCharges_Y;
    }

    public void setSurrenderCharges_Y(double fundValueAtEnd_X, int policyTerm,
                                      double AnnPrem, double premium) {
        //double riderChargePerAnnum;
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            double a = 0;
            double number1 = 0, number2 = 0, number3 = 0;
            if (AnnPrem > 25000) {
                double[] surrendercharge = saralDB
                        .getSurrenderChargeArr_grter_25K();

                if (Integer.parseInt(getYear_F()) > surrendercharge.length)
                    a = surrendercharge[surrendercharge.length - 1] / 100;
                else
                    a = surrendercharge[Integer.parseInt(getYear_F()) - 1] / 100;
                number1 = fundValueAtEnd_X * a;
            } else {
                double[] surrendercharge = saralDB
                        .getSurrenderChargeArr_less_25K();

                if (Integer.parseInt(getYear_F()) > surrendercharge.length)
                    a = surrendercharge[surrendercharge.length - 1] / 100;
                else
                    a = surrendercharge[Integer.parseInt(getYear_F()) - 1] / 100;
                number1 = fundValueAtEnd_X * a;
            }
            number2 = AnnPrem * a;
            if (AnnPrem > 25000) {
                double[] surrendercap = saralDB.getPremium_grter_25K();
                if (Integer.parseInt(getYear_F()) > surrendercap.length)
                    number3 = surrendercap[surrendercap.length - 1];
                else
                    number3 = surrendercap[Integer.parseInt(getYear_F()) - 1];
            } else {
                double[] surrendercap = saralDB.getPremium_less_25K();
                if (Integer.parseInt(getYear_F()) > surrendercap.length)
                    number3 = surrendercap[surrendercap.length - 1];
                else
                    number3 = surrendercap[Integer.parseInt(getYear_F()) - 1];
            }

            // System.out.println(" number1 : "+number1 +"   "+number2+
            // "   "+number3);
            this.SurrenderCharges_Y = cfap.getRoundUp(""
                    + Math.min(Math.min(number1, number2), number3));
            // System.out.println(year_F+"  SurrenderCharges_Y : "+this.SurrenderCharges_Y);
        } else {
            SurrenderCharges_Y = "" + 0;
        }
    }

    public String getSurrenderCharges_AO() {
        return SurrenderCharges_AO;
    }

    public void setSurrenderCharges_AO(double fundValueAtEnd_AQ,
                                       int policyTerm, double AnnPrem, double premium) {
        //double riderChargePerAnnum;
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            double a = 0;
            double number1 = 0, number2 = 0, number3 = 0;
            if (AnnPrem > 25000) {
                double[] surrendercharge = saralDB
                        .getSurrenderChargeArr_grter_25K();

                if (Integer.parseInt(getYear_F()) > surrendercharge.length)
                    a = surrendercharge[surrendercharge.length - 1] / 100;
                else
                    a = surrendercharge[Integer.parseInt(getYear_F()) - 1] / 100;
                number1 = fundValueAtEnd_AQ * a;
            } else {
                double[] surrendercharge = saralDB
                        .getSurrenderChargeArr_less_25K();

                if (Integer.parseInt(getYear_F()) > surrendercharge.length)
                    a = surrendercharge[surrendercharge.length - 1] / 100;
                else
                    a = surrendercharge[Integer.parseInt(getYear_F()) - 1] / 100;
                number1 = fundValueAtEnd_AQ * a;
            }
            number2 = AnnPrem * a;
            // System.out.println(" AnnPrem : "+AnnPrem+"  "+fundValueAtEnd_AQ+"  "+a);
            if (AnnPrem > 25000) {
                double[] surrendercap = saralDB.getPremium_grter_25K();
                if (Integer.parseInt(getYear_F()) > surrendercap.length)
                    number3 = surrendercap[surrendercap.length - 1];
                else
                    number3 = surrendercap[Integer.parseInt(getYear_F()) - 1];
            } else {
                double[] surrendercap = saralDB.getPremium_less_25K();
                if (Integer.parseInt(getYear_F()) > surrendercap.length)
                    number3 = surrendercap[surrendercap.length - 1];
                else
                    number3 = surrendercap[Integer.parseInt(getYear_F()) - 1];
            }

            // System.out.println(" number1 : "+number1 +"   "+number2+
            // "   "+number3);
            this.SurrenderCharges_AO = cfap.getRoundUp(""
                    + Math.min(Math.min(number1, number2), number3));
            // System.out.println(year_F+"  SurrenderCharges_AO : "+this.SurrenderCharges_AO);

            // this.SurrenderCharges_AO=cfap.getRoundUp(""+(fundValueAtEnd_AQ*a));
        } else {
            SurrenderCharges_AO = "" + 0;
        }
    }

    // Added by Priyanka Warekar - 19-12-2014

    public double getCommission_BL(int policyTerm, double annPrem,
                                   double topUpPrem, boolean isStaff) {
        double commission;
        if (isStaff == true) {
            return 0;
        } else {
            if (Integer.parseInt(getYear_F()) > policyTerm) {
                return 0;
            } else if (Integer.parseInt(getYear_F()) > 10) {
                commission = (0.01 * Double.parseDouble(getPremium_I()) + topUpPrem * 0.01);
                return commission;
            } else {
                commission = (getComm() * Double.parseDouble(getPremium_I()) + topUpPrem * 0.01);
                return commission;
            }
        }
    }

    public double getComm() {
        if (getYear_F().equals("1")) {
            return 0.06;
        } else if (getYear_F().equals("2")) {
            return 0.0275;
        } else if (getYear_F().equals("3")) {
            return 0.0275;
        } else if (getYear_F().equals("4")) {
            return 0.0275;
        } else if (getYear_F().equals("5")) {
            return 0.0275;
        } else if (getYear_F().equals("6")) {
            return 0.02;
        } else if (getYear_F().equals("7")) {
            return 0.02;
        } else if (getYear_F().equals("8")) {
            return 0.02;
        } else if (getYear_F().equals("9")) {
            return 0.02;
        } else if (getYear_F().equals("10")) {
            return 0.02;
        } else {
            return 0.01;
        }
    }

    /************************************************** BL for Reduction yield starts here *******************************************************/

    public String getRiderChargesReductionYield_P() {
        return riderChargesReductionYield_P;
    }

    public void setRiderChargesReductionYield_P(boolean riderCharges,
                                                boolean isADBChecked, int ageAtEntry, boolean staffDisc,
                                                boolean bancAssuranceDisc, int PPT, int PF, int termADB,
                                                double SA_ADB) {
        double riderChargePerAnnum;
        if (riderCharges) {
            riderChargePerAnnum = ((50 * SA_ADB) / 100000);
            this.riderChargesReductionYield_P = cfap.roundUp_Level2(cfap
                    .getStringWithout_E(riderChargePerAnnum / 12));
        } else {
            this.riderChargesReductionYield_P = "" + 0;
        }
    }

    private String getMortalityChargesReductionYield_AG() {
        return mortalityChargesReductionYield_AG;
    }

    public void setMortalityChargesReductionYield_AG(double _fundValueAtEnd_AQ,
                                                     boolean mortalityCharges, double sumAssured, int policyTerm,
                                                     String[] forBIArray, int ageAtEntry) {
        if (!(getPolicyInForce_G().equals("Y"))
                || Integer.parseInt(getYear_F()) > policyTerm) {
            this.mortalityChargesReductionYield_AG = "" + 0;
        } else {
            double arrOutput = Double
                    .parseDouble(forBIArray[(ageAtEntry + Integer
                            .parseInt(getYear_F())) - 1]);
            // System.out.println("arrOutput "+arrOutput);
            double a = 1 - arrOutput / 1000;

            double b = (double) 1 / 12;
            double c = Math
                    .max(0,
                            (sumAssured - (Double
                                    .parseDouble(getAmountAvailableForInvestment_N()) + _fundValueAtEnd_AQ)));
            int d = 0;
            if (mortalityCharges) {
                d = 1;
            } else {
                d = 0;
            }
            this.mortalityChargesReductionYield_AG = cfap.roundUp_Level2(cfap
                    .getStringWithout_E((((1 - (cfap.pow(a, b))) * Math.max(c,
                            0)) * d)));

        }
    }

    private String getMortalityChargesReductionYield_R() {
        return mortalityChargesReductionYield_R;
    }

    public void setMortalityChargesReductionYield_R(double _fundValueAtEnd_AB,
                                                    int policyTerm, String[] forBIArray, int ageAtEntry,
                                                    double sumAssured, boolean mortalityCharges) {
        if (!(getPolicyInForce_G().equals("Y"))
                || Integer.parseInt(getYear_F()) > policyTerm) {
            this.mortalityChargesReductionYield_R = "" + 0;
        } else {
            double arrOutput = Double
                    .parseDouble(forBIArray[(ageAtEntry + Integer
                            .parseInt(getYear_F())) - 1]);
            // System.out.println("arrOutput "+arrOutput);
            double a = 1 - arrOutput / 1000;

            double b = (double) 1 / 12;
            double temp = (sumAssured - (Double
                    .parseDouble(getAmountAvailableForInvestment_N()) + _fundValueAtEnd_AB));

            double c = Math.max(temp, 0);
            int d = 0;
            if (mortalityCharges) {
                d = 1;
            } else {
                d = 0;
            }
            this.mortalityChargesReductionYield_R = cfap.roundUp_Level2(cfap
                    .getStringWithout_E((((1 - (cfap.pow(a, b))) * Math.max(c,
                            0)) * d)));
        }

    }

    public String getTotalServiceTaxReductionYield_U() {
        return totalServiceTaxReductionYield_U;
    }

    public void setTotalServiceTaxReductionYield_U(boolean riderCharges, double serviceTax) {
        double temp = 0;
        if (riderCharges) {
            temp = Double.parseDouble(getRiderChargesReductionYield_P());
        } else {
            temp = 0;
        }
        // modified by vrushali chaudhari on 22nd jan 14.
        // this.totalServiceTax_U
        // =cfap.roundUp_Level2(cfap.getStringWithout_E(Double.parseDouble(getServiceTaxOnAllocation_M())+Double.parseDouble(getTotalServiceTax_ExclOfSTonAllocAndSurr_T())+Double.parseDouble(getServiceTaxOnFMC_Y())
        // + (temp*SMA_Prop.serviceTax)));
        this.totalServiceTaxReductionYield_U = cfap
                .roundUp_Level2(cfap.getStringWithout_E(Double
                        .parseDouble(getServiceTaxOnAllocation_M())
                        + Double.parseDouble(getTotalServiceTax_ExclOfSTonAllocAndSurrReductionYield_T())
                        + Double.parseDouble(getServiceTaxOnFMCReductionYield_Y())
                        + temp * serviceTax));
    }

    public String getTotalServiceTaxReductionYield_AJ() {
        return totalServiceTaxReductionYield_AJ;
    }

    public void setTotalServiceTaxReductionYield_AJ(boolean riderCharges, double serviceTax) {
        double temp = 0;
        if (riderCharges) {
            temp = Double.parseDouble(getRiderChargesReductionYield_P());
        } else {
            temp = 0;
        }
        // modified by vrushali chaudhari on 22nd jan 14.
        // this.totalServiceTax_AJ
        // =cfap.roundUp_Level2(cfap.getStringWithout_E(Double.parseDouble(getServiceTaxOnAllocation_M())+Double.parseDouble(getTotalServiceTax_ExclOfSTonAllocAndSurr_AI())+Double.parseDouble(getServiceTaxOnFMC_AN())+(temp*SMA_Prop.serviceTax)));
        this.totalServiceTaxReductionYield_AJ = cfap
                .roundUp_Level2(cfap.getStringWithout_E(Double
                        .parseDouble(getServiceTaxOnAllocation_M())
                        + Double.parseDouble(getTotalServiceTax_ExclOfSTonAllocAndSurrReductionYield_AI())
                        + Double.parseDouble(getServiceTaxOnFMCReductionYield_AN())
                        + temp * serviceTax));

    }

    private String getTotalServiceTax_ExclOfSTonAllocAndSurrReductionYield_T() {
        return totalServiceTax_ExclOfSTonAllocAndSurrReductionYield_T;
    }

    public void setTotalServiceTax_ExclOfSTonAllocAndSurrReductionYield_T(
            double serviceTax, boolean mortalityAndRiderCharges,
            boolean administrationCharges, boolean riderCharges) {
        double a = 0, b = 0, d = 0;//, e = 0;c = 0,
        if (mortalityAndRiderCharges) {
            a = Double.parseDouble(getMortalityChargesReductionYield_R());
        } else {
            a = 0;
        }
        if (administrationCharges) {
            b = Double.parseDouble(getPolicyAdministrationCharge_Q());
        } else {
            b = 0;
        }
        d = ((a + b) * serviceTax);

        this.totalServiceTax_ExclOfSTonAllocAndSurrReductionYield_T = cfap
                .roundUp_Level2(cfap.getStringWithout_E(d));
    }

    private String getTotalServiceTax_ExclOfSTonAllocAndSurrReductionYield_AI() {
        return totalServiceTax_ExclOfSTonAllocAndsurrReductionYield_AI;
    }

    public void setTotalServiceTax_ExclOfSTonAllocAndSurrReductionYield_AI(
            double serviceTax, boolean mortalityAndRiderCharges,
            boolean administrationCharges, boolean riderCharges) {
        double a = 0, b = 0, d = 0;// c = 0, e = 0;
        if (mortalityAndRiderCharges) {
            a = Double.parseDouble(getMortalityChargesReductionYield_R());
        } else {
            a = 0;
        }
        if (administrationCharges) {
            b = Double.parseDouble(getPolicyAdministrationCharge_Q());
        } else {
            b = 0;
        }
        d = ((a + b) * serviceTax);

        this.totalServiceTax_ExclOfSTonAllocAndsurrReductionYield_AI = cfap
                .roundUp_Level2(cfap.getStringWithout_E((d)));
    }

    private String getServiceTaxOnFMCReductionYield_Y() {
        return serviceTaxOnFMCReductionYield_Y;
    }

    public void setServiceTaxOnFMCReductionYield_Y(
            boolean fundManagementCharges, double serviceTax,
            double percentToBeInvested_EquityFund,
            double percentToBeInvested_BondFund,
            double percentToBeInvested_BalancedFund,
            double percentToBeInvested_IndexFund) {
        double a = 0;
        if (fundManagementCharges) {
            a = Double.parseDouble(getFundManagementChargeReductionYield_X());
        } else {
            a = 0;
        }
        this.serviceTaxOnFMCReductionYield_Y = cfap.roundUp_Level2(cfap
                .getStringWithout_E(a * serviceTax));
    }

    private String getServiceTaxOnFMCReductionYield_AN() {
        return serviceTaxOnFMCReductionYield_AN;
    }

    public void setServiceTaxOnFMCReductionYield_AN(
            boolean fundManagementCharges, double serviceTax,
            double percentToBeInvested_EquityFund,
            double percentToBeInvested_BondFund,
            double percentToBeInvested_BalancedFund,
            double percentToBeInvested_IndexFund) {
        double a = 0;
        if (fundManagementCharges) {
            a = (Double.parseDouble(getFundManagementChargeReductionYield_AM()));
        } else {
            a = 0;
        }
        this.serviceTaxOnFMCReductionYield_AN = cfap.roundUp_Level2(cfap
                .getStringWithout_E(a * serviceTax));
    }

    private String getFundBeforeFMCReductionYield_W() {
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
                    .parseDouble(getRiderChargesReductionYield_P()));
            // this.fundBeforeFMCReductionYield_W=
            // ""+(_fundValueAtEnd_AB+Double.parseDouble(getAmountAvailableForInvestment_N())-Double.parseDouble(getTotalCharges_S())+Double.parseDouble(getAdditionToFundIfAny_V())-Double.parseDouble(getTotalServiceTax_ExclOfSTonAllocAndSurr_T())-Double.parseDouble(getServiceTaxOnAllocation_M()));
        } else {
            this.fundBeforeFMCReductionYield_W = "" + 0;
        }
    }

    private String getFundBeforeFMCReductionYield_AL() {
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
                    .parseDouble(getRiderChargesReductionYield_P()));
            // this.fundBeforeFMCReductionYield_AL=
            // ""+(_fundValueAtEnd_AQ+Double.parseDouble(getAmountAvailableForInvestment_N())-Double.parseDouble(getTotalCharges_AH())+Double.parseDouble(getAdditionToFundIfAny_AK())-Double.parseDouble(getTotalServiceTax_ExclOfSTonAllocAndSurr_AI())-Double.parseDouble(getServiceTaxOnAllocation_M()));

        } else {
            this.fundBeforeFMCReductionYield_AL = "" + 0;
        }
    }

    private String getTotalChargesReductionYield_AH() {
        return totalChargesReductionYield_AH;
    }

    public void setTotalChargesReductionYield_AH(int policyTerm) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.totalChargesReductionYield_AH = ""
                    + (Double.parseDouble(getPolicyAdministrationCharge_Q())
                    + Double.parseDouble(getMortalityChargesReductionYield_AG())
                    + Double.parseDouble(getSumAssuredRelatedCharges_O()) + Double
                    .parseDouble(getRiderChargesReductionYield_P()));
        } else {
            this.totalChargesReductionYield_AH = "" + 0;
        }
    }

    private String getTotalChargesReductionYield_S() {
        return totalChargesReductionYield_S;
    }

    public void setTotalChargesReductionYield_S(int policyTerm) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.totalChargesReductionYield_S = ""
                    + (Double.parseDouble(getPolicyAdministrationCharge_Q())
                    + Double.parseDouble(getMortalityChargesReductionYield_R())
                    + Double.parseDouble(getSumAssuredRelatedCharges_O()) + Double
                    .parseDouble(getRiderChargesReductionYield_P()));
        } else {
            this.totalChargesReductionYield_S = "" + 0;
        }
    }

    private String getAdditionToFundIfAnyReductionYield_V() {
        return additionToFundIfAnyReductionYield_V;
    }

    public void setAdditionToFundIfAnyReductionYield_V(
            double _fundValueAtEnd_AB, int policyTerm, double int1) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.additionToFundIfAnyReductionYield_V = cfap
                    .roundUp_Level2(cfap.getStringWithout_E((_fundValueAtEnd_AB
                            + Double.parseDouble(getAmountAvailableForInvestment_N())
                            - Double.parseDouble(getTotalChargesReductionYield_S())
                            - Double.parseDouble(getTotalServiceTax_ExclOfSTonAllocAndSurrReductionYield_T()) + Double
                            .parseDouble(getRiderChargesReductionYield_P()))
                            * (cfap.pow((1 + int1), (double) 1 / 12) - 1)));
        } else {
            this.additionToFundIfAnyReductionYield_V = "" + 0;
        }
    }

    private String getAdditionToFundIfAnyReductionYield_AK() {
        return additionToFundIfAnyReductionYield_AK;
    }

    public void setAdditionToFundIfAnyReductionYield_AK(
            double _fundValueAtEnd_AQ, int policyTerm, double int2) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.additionToFundIfAnyReductionYield_AK = cfap
                    .roundUp_Level2(cfap.getStringWithout_E((_fundValueAtEnd_AQ
                            + Double.parseDouble(getAmountAvailableForInvestment_N())
                            - Double.parseDouble(getTotalChargesReductionYield_AH())
                            - Double.parseDouble(getTotalServiceTax_ExclOfSTonAllocAndSurrReductionYield_AI()) + Double
                            .parseDouble(getRiderChargesReductionYield_P()))
                            * (cfap.pow((1 + int2), (double) 1 / 12) - 1)));
        } else {
            this.additionToFundIfAnyReductionYield_AK = "" + 0;
        }
    }

    private String getFundManagementChargeReductionYield_X() {
        return fundManagementChargeReductionYield_X;
    }

    public void setFundManagementChargeReductionYield_X(int policyTerm,
                                                        double percentToBeInvested_EquityFund,
                                                        double percentToBeInvested_BondFund,
                                                        double percentToBeInvested_BalancedFund,
                                                        double percentToBeInvested_IndexFund) {
        double temp1 = 0, temp2 = 0;
        if (Integer.parseInt(getMonth_E()) == 1) {
            temp1 = SMA_Prop.charge_Fund;
        } else {
            temp1 = 0;
        }
        temp2 = getCharge_fund_ren(percentToBeInvested_EquityFund,
                percentToBeInvested_BondFund, percentToBeInvested_BalancedFund,
                percentToBeInvested_IndexFund) / 12;
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.fundManagementChargeReductionYield_X = cfap
                    .roundUp_Level2(cfap.getStringWithout_E(Double
                            .parseDouble(getFundBeforeFMCReductionYield_W())
                            * (temp1 + temp2)));
        } else {
            this.fundManagementChargeReductionYield_X = "" + 0;
        }
    }

    private String getFundManagementChargeReductionYield_AM() {
        return fundManagementChargeReductionYield_AM;
    }

    public void setFundManagementChargeReductionYield_AM(int policyTerm,
                                                         double percentToBeInvested_EquityFund,
                                                         double percentToBeInvested_BondFund,
                                                         double percentToBeInvested_BalancedFund,
                                                         double percentToBeInvested_IndexFund) {
        double temp1 = 0, temp2 = 0;
        if (Integer.parseInt(getMonth_E()) == 1) {
            temp1 = SMA_Prop.charge_Fund;
        } else {
            temp1 = 0;
        }
        temp2 = getCharge_fund_ren(percentToBeInvested_EquityFund,
                percentToBeInvested_BondFund, percentToBeInvested_BalancedFund,
                percentToBeInvested_IndexFund) / 12;
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.fundManagementChargeReductionYield_AM = cfap
                    .roundUp_Level2(cfap.getStringWithout_E(Double
                            .parseDouble(getFundBeforeFMCReductionYield_AL())
                            * (temp1 + temp2)));
        } else {
            this.fundManagementChargeReductionYield_AM = "" + 0;
        }
    }

    private String getFundValueAfterFMCandBeforeGAReductionYield_AO() {
        return fundValueAfterFMCandBeforeGAReductionYield_AO;
    }

    public void setFundValueAfterFMCandBeforeGAReductionYield_AO(int policyTerm) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            double val = (Double
                    .parseDouble(getFundBeforeFMCReductionYield_AL())
                    - Double.parseDouble(getFundManagementChargeReductionYield_AM()) - Double
                    .parseDouble(getServiceTaxOnFMCReductionYield_AN()));
            this.fundValueAfterFMCandBeforeGAReductionYield_AO = "" + val;
        } else {
            this.fundValueAfterFMCandBeforeGAReductionYield_AO = "" + 0;
        }
    }

    private String getFundValueAfterFMCandBeforeGAReductionYield_Z() {
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

    public String getFundValueAtEndReductionYield_AQ() {
        return fundValueAtEndReductionYield_AQ;
    }

    public void setFundValueAtEndReductionYield_AQ() {
        double val = (Double.parseDouble(getGuaranteedAddition_AP()) + Double
                .parseDouble(getFundValueAfterFMCandBeforeGAReductionYield_AO()));
        this.fundValueAtEndReductionYield_AQ = "" + val;
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
        } else {
            this.deathBenefitReductionYield_AF = ""
                    + (Math.max(
                    Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()),
                    Math.max(
                            sumAssured,
                            Double.parseDouble(getFundValueAtEndReductionYield_AB()))));
        }
    }

    public String getDeathBenefitReductionYield_AU() {
        return deathBenefitReductionYield_AU;
    }

    public void setDeathBenefitReductionYield_AU(int policyTerm,
                                                 double sumAssured) {
        if (Integer.parseInt(getYear_F()) > policyTerm) {
            this.deathBenefitReductionYield_AU = "" + 0;
        } else {
            this.deathBenefitReductionYield_AU = ""
                    + (Math.max(
                    Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()),
                    Math.max(
                            sumAssured,
                            Double.parseDouble(getFundValueAtEndReductionYield_AQ()))));
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

    public String getSurrenderValueReductionYield_AT() {
        return surrenderValueReductionYield_AT;
    }

    public void setSurrenderValueReductionYield_AT() {
        this.surrenderValueReductionYield_AT = ""
                + (Double.parseDouble(getFundValueAtEndReductionYield_AQ())
                - Double.parseDouble(getSurrenderChargesReductionYield_AR()) - Double
                .parseDouble(getServiceTaxOnSurrenderChargesReductionYield_AS()));
    }

    private String getSurrenderChargesReductionYield_AC() {
        return surrenderChargesReductionYield_AC;
    }

    public void setSurrenderChargesReductionYield_AC(double effectivePremium,
                                                     int PPT) {
        double a = Math.min(
                Double.parseDouble(getFundValueAtEndReductionYield_AB()),
                effectivePremium);
        double b = getSurrenderCharge(effectivePremium, PPT);
        this.surrenderChargesReductionYield_AC = cfap.roundUp_Level2(cfap
                .getStringWithout_E(Math.min((a * b),
                        Double.parseDouble(getSurrenderCap_AV()))));
    }

    private String getSurrenderChargesReductionYield_AR() {
        return surrenderChargesReductionYield_AR;
    }

    public void setSurrenderChargesReductionYield_AR(double effectivePremium,
                                                     int PPT) {
        double a = Math.min(
                Double.parseDouble(getFundValueAtEndReductionYield_AQ()),
                effectivePremium);
        double b = getSurrenderCharge(effectivePremium, PPT);
        this.surrenderChargesReductionYield_AR = cfap.roundUp_Level2(cfap
                .getStringWithout_E(Math.min((a * b),
                        Double.parseDouble(getSurrenderCap_AV()))));
    }

    private String getServiceTaxOnSurrenderChargesReductionYield_AD() {
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

    private String getServiceTaxOnSurrenderChargesReductionYield_AS() {
        return serviceTaxOnSurrenderChargesReductionYield_AS;
    }

    public void setServiceTaxOnSurrenderChargesReductionYield_AS(
            double serviceTax, boolean surrenderCharges) {
        if (surrenderCharges) {
            this.serviceTaxOnSurrenderChargesReductionYield_AS = cfap
                    .roundUp_Level2(cfap.getStringWithout_E(Double
                            .parseDouble(getSurrenderChargesReductionYield_AR())
                            * serviceTax));
        } else {
            this.serviceTaxOnSurrenderChargesReductionYield_AS = "" + 0;
        }
    }

    public void setMonth_BM(int monthNumber) {
        this.month_BM = "" + monthNumber;
    }

    private String getMonth_BM() {
        return month_BM;
    }

    public void setReductionYield_BN(int policyTerm, double _fundValueAtEnd_AD) {
        double a, b;
        // if((Integer.parseInt(getMonth_E())) <=
        // (noOfYearsElapsedSinceInception*12)+1)
        if ((Integer.parseInt(getMonth_BM())) < (policyTerm * 12)) {
            a = -(Double.parseDouble(getPremium_I()));
        } else {
            a = 0;
        }

        if ((Integer.parseInt(getMonth_BM())) == (policyTerm * 12)) {
            b = _fundValueAtEnd_AD;
        } else {
            b = 0;
        }
        // System.out.println("a_BB "+a);
        // System.out.println("b_BB "+b);
        this.reductionYield_BN = "" + (a + b);
    }

    public String getReductionYield_BN() {
        return reductionYield_BN;
    }

    public void setReductionYield_BO(int policyTerm, double _fundValueAtEnd_AD) {
        double a, b;
        // if((Integer.parseInt(getMonth_E())) <=
        // (noOfYearsElapsedSinceInception*12)+1)
        if ((Integer.parseInt(getMonth_BM())) < (policyTerm * 12)) {
            a = -(Double.parseDouble(getPremium_I()));
        } else {
            a = 0;
        }

        if ((Integer.parseInt(getMonth_BM())) == (policyTerm * 12)) {
            b = _fundValueAtEnd_AD;
        } else {
            b = 0;
        }
        // System.out.println("a_BB "+a);
        // System.out.println("b_BB "+b);
        this.reductionYield_BO = "" + (a + b);
    }

    public String getReductionYield_BO() {
        return reductionYield_BO;
    }

    public void setReductionYield_BS(int policyTerm, double _fundValueAtEnd_AD) {
        double a, b;
        // if((Integer.parseInt(getMonth_E())) <=
        // (noOfYearsElapsedSinceInception*12)+1)
        if ((Integer.parseInt(getMonth_BM())) < (policyTerm * 12)) {
            a = -(Double.parseDouble(getPremium_I()));
        } else {
            a = 0;
        }

        if ((Integer.parseInt(getMonth_BM())) == (policyTerm * 12)) {
            b = _fundValueAtEnd_AD;
        } else {
            b = 0;
        }
        // System.out.println("a_BB "+a);
        // System.out.println("b_BB "+b);
        this.reductionYield_BS = "" + (a + b);
    }

    public String getReductionYield_BS() {
        return reductionYield_BS;
    }

    public void setIRRAnnual_BN(double ans) {
        // System.out.println("aaaaaaa "+((comm.pow((1+ans),12)-1)*100));
        this.irrAnnual_BN = "" + ((cfap.pow((1 + ans), 12) - 1));
    }

    public String getIRRAnnual_BN() {
        return irrAnnual_BN;
    }

    public void setIRRAnnual_BO(double ans) {
        // System.out.println("aaaaaaa "+((comm.pow((1+ans),12)-1)*100));
        this.irrAnnual_BO = "" + ((cfap.pow((1 + ans), 12) - 1));
    }

    public String getIRRAnnual_BO() {
        return irrAnnual_BO;
    }

    public void setIRRAnnual_BS(double ans) {
        // System.out.println("aaaaaaa "+((comm.pow((1+ans),12)-1)*100));
        this.irrAnnual_BS = "" + ((cfap.pow((1 + ans), 12) - 1));
    }

    public String getIRRAnnual_BS() {
        return irrAnnual_BS;
    }

    public void setReductionInYieldMaturityAt(double int2) {
        this.reductionInYieldMaturityAt = ""
                + ((int2 - Double.parseDouble(getIRRAnnual_BO())) * 100);
    }

    public String getReductionInYieldMaturityAt() {
        return reductionInYieldMaturityAt;
    }

    public void setReductionInYieldNumberOfYearsElapsedSinceInception(
            double int2) {
        this.reductionInYieldNumberOfYearsElapsedSinceInception = ""
                + ((int2 - Double.parseDouble(getIRRAnnual_BS())) * 100);
    }

    public String getReductionInYieldNumberOfYearsElapsedSinceInception() {
        return reductionInYieldNumberOfYearsElapsedSinceInception;
    }

    public String getnetYieldAt4Percent() {
        return netYieldAt4Percent;
    }

    public void setnetYieldAt4Percent() {
        this.netYieldAt4Percent = "" + Double.parseDouble(getIRRAnnual_BN())
                * 100;
    }

    public String getnetYieldAt8Percent() {
        return netYieldAt8Percent;
    }

    public void setnetYieldAt8Percent() {
        this.netYieldAt8Percent = "" + Double.parseDouble(getIRRAnnual_BO())
                * 100;
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

}
