package sbilife.com.pointofsale_bancaagency.smartscholar;

import java.text.DecimalFormat;
import java.util.ArrayList;

import sbilife.com.pointofsale_bancaagency.common.CommonForAllProd;

class SmartScholarBusinessLogic {
    private CommonForAllProd cfap = null;
    private SmartScholarProperties ssProp = null;
    public double[] arrFundValAfterFMCBeforeGA = new double[500];
    public double[] arrFundValAfterFMCBeforeGA_AR = new double[500];
    public int count = 18;
    public int count_AR = 18;
    public static double loyaltyAddition_6Percent = 0;
    public static double loyaltyAddition_10Percent = 0;
    private String fundValueAfterFMCAndBeforeGA_V34 = null;
    public String plan = "Plan-B";

    private String month_E = null,
            year_F = null,
            policyInForce_G = "Y",
            childsAge_H = null,
            proposersAge_I = null,
            premium_J = null,
            topUpPremium_K = null,
            premiumAllocationCharge_L = null,
            topUpCharges_M = null,
            serviceTaxOnAllocation_N = null,
            amountAvailableForInvestment_O = null,
            amountAvailableForInvestment_O1 = null,
            accidentCoverCharges_Q = null,
            transferPercentIfAny_O = null,
            allocatedFundToIndexFund_P = null,
            sumAssuredRelatedCharges_P = null,
            optionCharges_R = null,
            policyAdministrationCharge_R = null,
            mortalityAndPPWBCharges_S = null,
            guaranteeCharge_U = null,
            totalCharges_T = null,
            totalServiceTax_exclOfSTonFMC_U = null,
            last2YearAvgFundValue_AB = null,
            last2YearAvgFundValue_AR = null,
            fundValueAtEnd_AD = null,
            fundValueAtEnd_AT = null,
            loyaltyAddition_AC = null,
            loyaltyAddition_AS = null,
            totalServiceTax_V = null,
            transferOfIndexFundGainToDailyProtectFund_Y = null,
            transferOfCapitalFromIndexToDailyProtectFund_Z = null,
            dailyProtectFundAtStart_AA = null,
            indexFundAtStart_AB = null,
            dailyProtectFundAfterCharges_AC = null,
            indexFundAtStartAfterCharges_AD = null,
            additionToDailyProtectFund_AE = null,
            additionToIndexFund_AF = null,
            FMCearnedOnDailyProtectFund_AG = null,
            FMCearnedOnIndexFund_AH = null,
            additionToFundIfAny_AM = null,
            fundBeforeFMC_X = null,
            fundManagementCharge_Y = null,
            serviceTaxOnFMC_Z = null,
            serviceTaxOnFMC_AP = null,
            fundValueAfterFMCandBeforeGA_AM = null,
            guaranteedAddition_AN = null,
            dailyProtectFundAtEnd_AO = null,
            indexFundAtEnd_AP = null,
            fundValueAtEnd_AQ = null,
            surrenderCharges_AE = null,
            serviceTaxOnSurrenderCharges_AF = null,
            surrenderValue_AG = null,
            deathBenefit_AH = null,
            mortalityAndPPWBCharges_AI = null,
            mortalityAndPPWBChargesReductionYeild_AI = null,
            guaranteeCharge_AW = null,
            totalCharges_AJ = null,
            totalServiceTax_ExclOfSTonAllocAndsurr_AY = null,
            totalServiceTax_AL = null,
            transferOfIndexFundGainToDailyProtectFund_BA = null,
            transferOfCapitalFromIndexToDailyProtectFund_BB = null,
            dailyProtectFundAtStart_BC = null,
            indexFundAtStart_BD = null,
            dailyProtectFundAftercharges_BE = null,
            indexFundAtStartAfterCharges_BF = null,
            additionToDailyProtectFund_BG = null,
            additionToIndexFund_BH = null,
            FMCearnedOnDailyProtectFund_BI = null,
            FMCearnedOnIndexFund_BJ = null,
            additionToFundIfAny_W = null,
            fundBeforeFMC_AN = null,
            fundManagementCharge_AO = null,
            totalServiceTax_exclOfSTonFMC_AK = null,
            fundValueAfterFMCAndBeforeGA_AA = null,
            fundValueAfterFMCAndBeforeGA_AQ = null,
            guaranteedAddition_BP = null,
            dailyProtectFundAtend_BQ = null,
            indexFundAtEnd_BR = null,
            fundValueAtEnd_BS = null,
            surrenderCharges_AU = null,
            serviceTaxOnSurrenderCharges_AV = null,
            surrenderValue_AW = null,
            deathBenefit_AX = null,
            surrenderCap_AY = null,
            oneHundredPercentOfCummulativePremium_AZ = null,
            mortalityAndPPWBChargesReductionYeild_S = null,
            serviceTaxOnFMCReductionYeild_AP = null,
            serviceTaxOnFMCReductionYeild_Z = null,
            surrenderChargesReductionYeild_AU = null,
            surrenderChargesReductionYeild_AE = null,
            reductionYield_BB = null,
            reductionYield_BC = null,
            month_BA = null,
            irrAnnual_BB = null,
            irrAnnual_BC = null,
            fundValueAfterFMCAndBeforeGAReductionYeild_AA = null,
            fundValueAfterFMCAndBeforeGAReductionYeild_AQ = null,
            totalServiceTaxReductionYeild_V = null,
            totalServiceTaxReductionYeild_AL = null,
            surrenderValueReductionYeild_AG = null,
            surrenderValueReductionYeild_AW = null,
            fundValueAtEndReductionYeild_AT = null,
            fundValueAtEndReductionYeild_AD = null,
            reductionInYieldMaturityAt = null,
            reductionInYieldNumberOfYearsElapsedSinceInception = null,
            reductionYield_BG = null,
            irrAnnual_BG = null,
            netYieldAt4Percent = null,
            netYieldAt8Percent = null;

    //Constructor[Initialization of object required to access general calculation methods is done here in constructor]
    public SmartScholarBusinessLogic() {
        cfap = new CommonForAllProd();
        ssProp = new SmartScholarProperties();
    }

    //****************************************************************************************************************************************
    public String getFundValueAfterFMCAndBeforeGA_V34() {
        return fundValueAfterFMCAndBeforeGA_V34;
    }

    public void setFundValueAfterFMCAndBeforeGA_V34() {
        this.fundValueAfterFMCAndBeforeGA_V34 = getFundValueAfterFMCandBeforeGA_AM();
    }
    //****************************************************************************************************************************************

    public String getAmountAvailableForInvestment_O() {
        return amountAvailableForInvestment_O;
    }

    public void setAmountAvailableForInvestment_O() {
        this.amountAvailableForInvestment_O = cfap.getRoundOffLevel2(cfap.getStringWithout_E((Double.parseDouble(getPremium_J()) + Double.parseDouble(getTopUpPremium_K()) - Double.parseDouble(getPremiumAllocationCharge_L()) - Double.parseDouble(getTopUpCharges_M()) - Double.parseDouble(getServiceTaxOnAllocation_N()))));
    }

    public String getAmountAvailableForInvestment_O1() {
        return amountAvailableForInvestment_O1;
    }

    public void setAmountAvailableForInvestment_O1() {
        this.amountAvailableForInvestment_O1 = cfap.getRoundOffLevel2(cfap.getStringWithout_E(Double.parseDouble(getPremium_J()) - Double.parseDouble(getPremiumAllocationCharge_L())));
    }

    public String getLoyaltyAddition_AC() {
        return loyaltyAddition_AC;
    }

    public void setLoyaltyAddition_AC(int policyTerm) {
        double temp1 = 0, temp2 = 0;
        if (getPolicyInForce_G().equals("Y")) {
            if (policyTerm >= ssProp.minTerm) {
                temp1 = getPercentOfFV(policyTerm, Integer.parseInt(getMonth_E()));
            } else {
                temp1 = 0;
            }
        } else {
            temp2 = 0;
        }
        temp2 = Double.parseDouble(getLast2YearAvgFundValue_AB());
//        this.loyaltyAddition_AC=cfap.getRoundOffLevel2(cfap.getStringWithout_E(temp1*temp2));
        this.loyaltyAddition_AC = cfap.getRound(cfap.getStringWithout_E(temp1 * temp2));
        loyaltyAddition_6Percent = loyaltyAddition_6Percent + Double.parseDouble(this.loyaltyAddition_AC);
    }

    public String getFundValueAtEnd_AD() {
        return fundValueAtEnd_AD;
    }

    public void setFundValueAtEnd_AD() {
        this.fundValueAtEnd_AD = "" + (Double.parseDouble(getFundValueAfterFMCAndBeforeGA_AA()) + Double.parseDouble(getLoyaltyAddition_AC()));
    }

    public String getFundValueAtEnd_AT() {
        return fundValueAtEnd_AT;
    }

    public void setFundValueAtEnd_AT() {
        this.fundValueAtEnd_AT = "" + (Double.parseDouble(getFundValueAfterFMCAndBeforeGA_AQ()) + Double.parseDouble(getLoyaltyAddition_AS()));
    }


    public String getFundValueAtEndReductionYeild_AD() {
        return fundValueAtEndReductionYeild_AD;
    }

    public void setFundValueAtEndReductionYeild_AD()
//    {this.fundValueAtEndReductionYeild_AD=""+(Double.parseDouble(getFundValueAfterFMCAndBeforeGAReductionYeild_AA())+Double.parseDouble(getLoyaltyAddition_AC()));}
    {
        this.fundValueAtEndReductionYeild_AD = "" + (Double.parseDouble(getFundValueAfterFMCAndBeforeGAReductionYeild_AA()));
    }

    public String getFundValueAtEndReductionYeild_AT() {
        return fundValueAtEndReductionYeild_AT;
    }

    public void setFundValueAtEndReductionYeild_AT()
//    {this.fundValueAtEndReductionYeild_AT=""+(Double.parseDouble(getFundValueAfterFMCAndBeforeGAReductionYeild_AQ())+Double.parseDouble(getLoyaltyAddition_AS()));}
    {
        this.fundValueAtEndReductionYeild_AT = "" + (Double.parseDouble(getFundValueAfterFMCAndBeforeGAReductionYeild_AQ()));
    }


    public String getLoyaltyAddition_AS() {
        return loyaltyAddition_AS;
    }

    public void setLoyaltyAddition_AS(int policyTerm) {
        double temp1 = 0, temp2 = 0;
        if (getPolicyInForce_G().equals("Y")) {
            if (policyTerm >= ssProp.minTerm) {
                temp1 = getPercentOfFV(policyTerm, Integer.parseInt(getMonth_E()));
            } else {
                temp1 = 0;
            }
        } else {
            temp2 = 0;
        }
        temp2 = Double.parseDouble(getLast2YearAvgFundValue_AR());
        this.loyaltyAddition_AS = cfap.getRoundOffLevel2(cfap.getStringWithout_E(temp1 * temp2));
        loyaltyAddition_10Percent = loyaltyAddition_10Percent + Double.parseDouble(this.loyaltyAddition_AS);
    }

    private double getPercentOfFV(int policyTerm, int month) {
        int[] yrEnd = getYrEndArr(policyTerm);
        double[] PercentOfFVArr = getPercentOfFVArr(policyTerm);
        for (int i = 0; i < yrEnd.length; i++) {
            if ((yrEnd[i] * 12) == month) {
                return PercentOfFVArr[i];
            }
        }
        return 0;
    }

    private double[] getPercentOfFVArr(int policyTerm) {
        double[] percentOfFVArr = new double[15];
        for (int i = 0; i < percentOfFVArr.length; i++) {
            if (i == 0) {
                percentOfFVArr[i] = 0;
            } else if (i == 1) {
                if (ssProp.Loyalty_Add) {
                    if (policyTerm >= (ssProp.firstGAinYr + ssProp.Gap_Loyalty_Add)) {
                        if ((policyTerm - ssProp.firstGAinYr) % ssProp.Gap_Loyalty_Add == 0) {
                            percentOfFVArr[i] = ssProp.loyalty;
                        } else {
                            percentOfFVArr[i] = 0;
                        }
                    } else {
                        percentOfFVArr[i] = 0;
                    }
                } else {
                    percentOfFVArr[i] = 0;
                }
            } else {
                if (ssProp.Loyalty_Add) {
                    percentOfFVArr[i] = ssProp.loyalty;
                } else {
                    percentOfFVArr[i] = 0;
                }
            }
        }
        return percentOfFVArr;
    }

    public double getCommission_AP(double annualisedPrem, double topupPrem, SmartScholarBean smartScholarBean) {
        double topup = 0.01;
//	 		System.out.println("getCommistionRate "+getCommistionRate(smartScholarBean) + " "+annualisedPrem+" "+topupPrem);
        if (smartScholarBean.getIsForStaffOrNot())
            return 0;
        else
            return getCommistionRate(smartScholarBean) * annualisedPrem + topupPrem * topup;
    }

    //	     public double getCommistionRate(SmartScholarBean smartScholarBean)
//	     {
//	 		int findYear;
//
//	 		if(Integer.parseInt(getYear_F()) >10)
//	 		{
//	 			findYear = 1;
//	 		}
//	 		else
//	 			findYear = Integer.parseInt(getYear_F());
//
//	 			double []commArr = {0.055,0.030,0.030,0.025,0.025,0.010,0.010,0.010,0.010,0.010,0.010};
//	 			double temp=commArr[findYear-1];
//	 			return commArr[findYear-1];
//
//	     }
    private double getCommistionRate(SmartScholarBean smartScholarBean) {
        int findYear;

        if (Integer.parseInt(getYear_F()) > 10) {
            findYear = 11;
        } else
            findYear = Integer.parseInt(getYear_F());
        if (!smartScholarBean.getPremFreqMode().equalsIgnoreCase("Single")) {
            double[] commArr = {0.055, 0.030, 0.030, 0.025, 0.025, 0.010, 0.010, 0.010, 0.010, 0.010, 0.010};
            double temp = commArr[findYear - 1];
            return commArr[findYear - 1];
        } else {
            double[] commArr = {0.02, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
            double temp = commArr[findYear - 1];
            return commArr[findYear - 1];
        }
    }

    private int[] getYrEndArr(int policyTerm) {
        int[] yrEndArr = new int[15];
        String[] temp = new String[15];
        for (int i = 0; i < yrEndArr.length; i++) {
            if (i == 0) {
                yrEndArr[i] = 0;
                temp[i] = "0";
            } else if (i == 1) {
                if (policyTerm == ssProp.firstGAinYr) {
                    yrEndArr[i] = ssProp.firstGAinYr - 1;
                } else {
                    yrEndArr[i] = ssProp.firstGAinYr;
                }
                temp[i] = "0";
            } else {
                if (i == 2) {
                    temp[i] = cfap.getRoundUp("" + (((double) policyTerm - (double) yrEndArr[i - 1]) / ssProp.Gap_Loyalty_Add));
                } else {
                    if (temp[i - 1].equals("NA")) {
                        temp[i] = "NA";
                    } else {
                        if ((Integer.parseInt(temp[i - 1]) - 1) <= 0) {
                            temp[i] = "NA";
                        } else {
                            temp[i] = "" + (Integer.parseInt(temp[i - 1]) - 1);
                        }
                    }
                }
                if (temp[i].equals("NA")) {
                    yrEndArr[i] = 99;
                } else {
                    yrEndArr[i] = (policyTerm - Math.max((Integer.parseInt(temp[i]) - 1), 0) * ssProp.Gap_Loyalty_Add);
                }
            }
        }
        return yrEndArr;
    }

    public String getLast2YearAvgFundValue_AB() {
        return last2YearAvgFundValue_AB;
    }

    public void setLast2YearAvgFundValue_AB(double[] arrFundValAfterFMCBeforeGA, int count_) {
        if (Integer.parseInt(getMonth_E()) <= 6) {
            this.last2YearAvgFundValue_AB = "" + 0;
        } else {
            int month = Integer.parseInt(getMonth_E());
            double valTobeDivided = 0;
            for (int i = (1 + count_); i <= (24 + count_); i++) {
                valTobeDivided = valTobeDivided + arrFundValAfterFMCBeforeGA[i];
            }
            count++;

            //[Excel is Wrong[Calculation of cell AB25 to AB21 in BI_Incl_Mort & Ser Tax Sheet]- but this doesnt affect the final calculation because LOYALTY ADDITION column for all input scenario gives zero output]
            //*******************************Wrong Code [To match with Excel] [Start]****************************************************************************************************************************************
            if (Integer.parseInt(getMonth_E()) == 7) {
                ////System.out.println("***********");
                ////System.out.println("IN SEVEN");
                ////System.out.println("valTobeDivided   ->  "+valTobeDivided);
                ////System.out.println("Divide By  ->  "+10);
                ////System.out.println("***********");
                this.last2YearAvgFundValue_AB = "" + (valTobeDivided / 10);
            } else if (Integer.parseInt(getMonth_E()) == 8) {
                this.last2YearAvgFundValue_AB = "" + (valTobeDivided / 11);
            } else if (Integer.parseInt(getMonth_E()) == 9) {
                this.last2YearAvgFundValue_AB = "" + (valTobeDivided / 12);
            } else if (Integer.parseInt(getMonth_E()) == 10) {
                this.last2YearAvgFundValue_AB = "" + (valTobeDivided / 13);
            } else if (Integer.parseInt(getMonth_E()) == 11) {
                //System.out.println("***********");
                //System.out.println("IN Eleventh Row");
                //System.out.println("valTobeDivided   ->  "+valTobeDivided);
                //System.out.println("Divide By  ->  "+14);
                //System.out.println("***********");
                this.last2YearAvgFundValue_AB = "" + (valTobeDivided / 14);
            } else if (Integer.parseInt(getMonth_E()) == 12) {
                this.last2YearAvgFundValue_AB = "" + (valTobeDivided / 15);
            } else if (Integer.parseInt(getMonth_E()) == 13) {
                this.last2YearAvgFundValue_AB = "" + (valTobeDivided / 16);
            } else if (Integer.parseInt(getMonth_E()) == 14) {
                this.last2YearAvgFundValue_AB = "" + (valTobeDivided / 17);
            } else if (Integer.parseInt(getMonth_E()) == 15) {
                this.last2YearAvgFundValue_AB = "" + (valTobeDivided / 18);
            } else if (Integer.parseInt(getMonth_E()) == 16) {
                this.last2YearAvgFundValue_AB = "" + (valTobeDivided / 19);
            } else if (Integer.parseInt(getMonth_E()) == 17) {
                this.last2YearAvgFundValue_AB = "" + (valTobeDivided / 20);
            } else if (Integer.parseInt(getMonth_E()) == 18) {
                this.last2YearAvgFundValue_AB = "" + (valTobeDivided / 20);
            } else if (Integer.parseInt(getMonth_E()) == 19) {
                this.last2YearAvgFundValue_AB = "" + (valTobeDivided / 21);
            } else if (Integer.parseInt(getMonth_E()) == 20) {
                this.last2YearAvgFundValue_AB = "" + (valTobeDivided / 22);
            } else if (Integer.parseInt(getMonth_E()) == 21) {
                this.last2YearAvgFundValue_AB = "" + (valTobeDivided / 23);
            } else if (Integer.parseInt(getMonth_E()) == 22) {
                this.last2YearAvgFundValue_AB = "" + (valTobeDivided / 23);
            } else if (Integer.parseInt(getMonth_E()) == 23) {
                this.last2YearAvgFundValue_AB = "" + (valTobeDivided / 23);
            } else {
                this.last2YearAvgFundValue_AB = "" + (valTobeDivided / 24);
            }
        }
        //*******************************Wrong Code [To match with Excel] [End]**************************************************************
    }

    public String getLast2YearAvgFundValue_AR() {
        return last2YearAvgFundValue_AR;
    }

    public void setLast2YearAvgFundValue_AR(double[] arrFundValAfterFMCBeforeGA_AR, int count_) {
        if (Integer.parseInt(getMonth_E()) <= 6) {
            this.last2YearAvgFundValue_AR = "" + 0;
        } else {
            int month = Integer.parseInt(getMonth_E());
            double valTobeDivided = 0;
            for (int i = (1 + count_); i <= (24 + count_); i++) {
                valTobeDivided = valTobeDivided + arrFundValAfterFMCBeforeGA_AR[i];
            }
            count_AR++;
            //*******************************Wrong Code [To match with Excel] [Start]**************************************************************
            if (Integer.parseInt(getMonth_E()) == 7) {
                //System.out.println("***********");
                //System.out.println("IN SEVEN");
                //System.out.println("valTobeDivided   ->  "+valTobeDivided);
                //System.out.println("Divide By  ->  "+8);
                //System.out.println("***********");
                this.last2YearAvgFundValue_AR = "" + (valTobeDivided / 8);
            } else if (Integer.parseInt(getMonth_E()) == 8) {
                this.last2YearAvgFundValue_AR = "" + (valTobeDivided / 9);
            } else if (Integer.parseInt(getMonth_E()) == 9) {
                this.last2YearAvgFundValue_AR = "" + (valTobeDivided / 10);
            } else if (Integer.parseInt(getMonth_E()) == 10) {
                this.last2YearAvgFundValue_AR = "" + (valTobeDivided / 11);
            } else if (Integer.parseInt(getMonth_E()) == 11) {
                this.last2YearAvgFundValue_AR = "" + (valTobeDivided / 12);
            } else if (Integer.parseInt(getMonth_E()) == 12) {
                this.last2YearAvgFundValue_AR = "" + (valTobeDivided / 13);
            } else if (Integer.parseInt(getMonth_E()) == 13) {
                this.last2YearAvgFundValue_AR = "" + (valTobeDivided / 14);
            } else if (Integer.parseInt(getMonth_E()) == 14) {
                this.last2YearAvgFundValue_AR = "" + (valTobeDivided / 15);
            } else if (Integer.parseInt(getMonth_E()) == 15) {
                this.last2YearAvgFundValue_AR = "" + (valTobeDivided / 16);
            } else if (Integer.parseInt(getMonth_E()) == 16) {
                this.last2YearAvgFundValue_AR = "" + (valTobeDivided / 17);
            } else if (Integer.parseInt(getMonth_E()) == 17) {
                this.last2YearAvgFundValue_AR = "" + (valTobeDivided / 18);
            } else if (Integer.parseInt(getMonth_E()) == 18) {
                this.last2YearAvgFundValue_AR = "" + (valTobeDivided / 18);
            } else if (Integer.parseInt(getMonth_E()) == 19) {
                this.last2YearAvgFundValue_AR = "" + (valTobeDivided / 19);
            } else if (Integer.parseInt(getMonth_E()) == 20) {
                this.last2YearAvgFundValue_AR = "" + (valTobeDivided / 20);
            } else if (Integer.parseInt(getMonth_E()) == 21) {
                this.last2YearAvgFundValue_AR = "" + (valTobeDivided / 21);
            } else if (Integer.parseInt(getMonth_E()) == 22) {
                this.last2YearAvgFundValue_AR = "" + (valTobeDivided / 22);
            } else if (Integer.parseInt(getMonth_E()) == 23) {
                this.last2YearAvgFundValue_AR = "" + (valTobeDivided / 23);
            } else {
                this.last2YearAvgFundValue_AR = "" + (valTobeDivided / 24);
            }
            //*******************************Wrong Code [To match with Excel] [End]**************************************************************
        }
    }

    public String getAccidentCoverCharges_Q() {
        return accidentCoverCharges_Q;
    }

    public void setAccidentCoverCharges_Q(boolean optionCharges, String premFreqMode, double sumAssured) {
        if (optionCharges) {
            this.accidentCoverCharges_Q = cfap.getRoundOffLevel2(cfap.getStringWithout_E(getADBoptionPremiumCharge(premFreqMode, sumAssured)));
        } else {
            this.accidentCoverCharges_Q = "" + 0;
        }
    }

    private String getFMCearnedOnDailyProtectFund_AG() {
        return FMCearnedOnDailyProtectFund_AG;
    }

    public void setFMCearnedOnDailyProtectFund_AG(int policyTerm, double charge_Fund, double charge_Fund_Ren) {
        double a = 0, b = 0;
        if (getMonth_E().equals("1")) {
            b = charge_Fund;
        } else {
            b = 0;
        }
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            a = (Double.parseDouble(getDailyProtectFundAfterCharges_AC()) + Double.parseDouble(getAdditionToDailyProtectFund_AE())) * (b + charge_Fund_Ren / 12);
        } else {
            a = 0;
        }
        this.FMCearnedOnDailyProtectFund_AG = cfap.roundUp_Level2(cfap.getStringWithout_E(a));
    }

    private String getFMCearnedOnDailyProtectFund_BI() {
        return FMCearnedOnDailyProtectFund_BI;
    }

    public void setFMCearnedOnDailyProtectFund_BI(int policyTerm, double charge_Fund, double charge_Fund_Ren) {
        double a = 0;
        if (Double.parseDouble(getAdditionToFundIfAny_AM()) == 1.0) {
            a = charge_Fund;
        } else {
            a = 0;
        }
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.FMCearnedOnDailyProtectFund_BI = cfap.getRoundOffLevel2(cfap.getStringWithout_E(((Double.parseDouble(getDailyProtectFundAftercharges_BE()) + Double.parseDouble(getAdditionToDailyProtectFund_BG())) * (a + charge_Fund_Ren / 12))));
        } else {
            this.FMCearnedOnDailyProtectFund_BI = "" + 0;
        }
    }

    private String getFMCearnedOnIndexFund_AH() {
        return FMCearnedOnIndexFund_AH;
    }

    public void setFMCearnedOnIndexFund_AH(int policyTerm, double charge_Fund, double indexFund) {
        double a = 0;
        if (getMonth_E().equals("1")) {
            a = charge_Fund;
        } else {
            a = 0;
        }
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.FMCearnedOnIndexFund_AH = cfap.roundUp_Level2(cfap.getStringWithout_E(((Double.parseDouble(getIndexFundAtStartAfterCharges_AD()) + Double.parseDouble(getAdditionToIndexFund_AF())) * (a + indexFund / 12))));
        } else {
            this.FMCearnedOnIndexFund_AH = "" + 0;
        }
    }

    private String getFMCearnedOnIndexFund_BJ() {
        return FMCearnedOnIndexFund_BJ;
    }

    public void setFMCearnedOnIndexFund_BJ(double indexFund, int policyTerm, double charge_Fund) {
        double a = 0;
        if (Double.parseDouble(getAdditionToFundIfAny_AM()) == 1) {
            a = charge_Fund;
        } else {
            a = 0;
        }
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.FMCearnedOnIndexFund_BJ = cfap.roundUp_Level2(cfap.getStringWithout_E(((Double.parseDouble(getIndexFundAtStartAfterCharges_BF()) + Double.parseDouble(getAdditionToIndexFund_BH())) * (a + indexFund / 12))));
        } else {
            this.FMCearnedOnIndexFund_BJ = "" + 0;
        }
    }

    public String getTopUpPremium_K() {
        return topUpPremium_K;
    }

    public void setTopUpPremium_K(boolean topUp, double effectiveTopUpPrem) {
        if (getMonth_E().equals("1") && topUp) {
            topUpPremium_K = cfap.getRoundUp("" + effectiveTopUpPrem);
        } else {
            topUpPremium_K = "" + 0;
        }
    }

    private String getAdditionToDailyProtectFund_AE() {
        return additionToDailyProtectFund_AE;
    }

    public void setAdditionToDailyProtectFund_AE(int policyTerm, double int1) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.additionToDailyProtectFund_AE = cfap.roundUp_Level2(cfap.getStringWithout_E((Double.parseDouble(getDailyProtectFundAfterCharges_AC()) * (cfap.pow((1 + int1), (double) 1 / 12) - 1))));
        } else {
            this.additionToDailyProtectFund_AE = "" + 0;
        }
    }

    private String getAdditionToDailyProtectFund_BG() {
        return additionToDailyProtectFund_BG;
    }

    public void setAdditionToDailyProtectFund_BG(int policyTerm, double int2) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.additionToDailyProtectFund_BG = cfap.roundUp_Level2(cfap.getStringWithout_E((Double.parseDouble(getDailyProtectFundAftercharges_BE()) * (cfap.pow((1 + int2), (double) 1 / 12) - 1))));
        } else {
            this.additionToDailyProtectFund_BG = "" + 0;
        }
    }

    public String getAdditionToFundIfAny_AM() {
        return additionToFundIfAny_AM;
    }

    public void setAdditionToFundIfAny_AM(double _fundValueAtEnd_AT, int policyTerm) {
        double temp1 = _fundValueAtEnd_AT + Double.parseDouble(getAmountAvailableForInvestment_O()) - Double.parseDouble(getTotalCharges_AJ()) - Double.parseDouble(getTotalServiceTax_exclOfSTonFMC_AK());
        double temp21 = 1 + ssProp.int2;
        double temp22 = (double) 1 / 12;
        double temp2 = cfap.pow(temp21, temp22) - 1;
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.additionToFundIfAny_AM = cfap.getRoundOffLevel2(cfap.getStringWithout_E(temp1 * temp2));
        } else {
            this.additionToFundIfAny_AM = "" + 0;
        }
    }

    public String getAdditionToFundIfAny_W() {
        return additionToFundIfAny_W;
    }

    public void setAdditionToFundIfAny_W(int policyTerm, double _fundValueAtEnd_AD) {
        double temp1 = _fundValueAtEnd_AD + Double.parseDouble(getAmountAvailableForInvestment_O()) - Double.parseDouble(getTotalCharges_T()) - Double.parseDouble(getTotalServiceTax_exclOfSTonFMC_U());
        double temp21 = 1 + ssProp.int1;
        double temp22 = (double) 1 / 12;
        double temp2 = cfap.pow(temp21, temp22) - 1;
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.additionToFundIfAny_W = cfap.getRoundOffLevel2(cfap.getStringWithout_E(temp1 * temp2));
        } else {
            this.additionToFundIfAny_W = "" + 0;
        }
    }

    private String getAdditionToIndexFund_AF() {
        return additionToIndexFund_AF;
    }

    public void setAdditionToIndexFund_AF(int policyTerm, double int1) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.additionToIndexFund_AF = cfap.roundUp_Level2(cfap.getStringWithout_E((Double.parseDouble(getIndexFundAtStartAfterCharges_AD()) * (cfap.pow(1 + int1, (double) 1 / 12) - 1))));
        } else {
            this.additionToIndexFund_AF = "" + 0;
        }
    }

    private String getAdditionToIndexFund_BH() {
        return additionToIndexFund_BH;
    }

    public void setAdditionToIndexFund_BH(int policyTerm, double int2) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.additionToIndexFund_BH = cfap.roundUp_Level2(cfap.getStringWithout_E((Double.parseDouble(getIndexFundAtStartAfterCharges_BF()) * (cfap.pow((1 + int2), (double) 1 / 12) - 1))));
        } else {
            this.additionToIndexFund_BH = "" + 0;
        }
    }

    public String getChildsAge_H() {
        return childsAge_H;
    }

    public void setChildsAge_H(int childsAge_H) {
        this.childsAge_H = "" + (childsAge_H + Integer.parseInt(getYear_F()) - 1);
    }

    public String getProposersAge_I() {
        return proposersAge_I;
    }

    public void setProposersAge_I(int proposersAge_I) {
        this.proposersAge_I = "" + (proposersAge_I + Integer.parseInt(getYear_F()) - 1);
    }

    private String getAllocatedFundToIndexFund_P() {
        return allocatedFundToIndexFund_P;
    }

    public void setAllocatedFundToIndexFund_P(double p18, double percentToBeInvestedForIndexFund) {
        if (getPolicyInForce_G().equals("Y")) {
            this.allocatedFundToIndexFund_P = cfap.roundUp_Level2(cfap.getStringWithout_E((p18 + Double.parseDouble(getAmountAvailableForInvestment_O()) * percentToBeInvestedForIndexFund * (1 - Double.parseDouble(getTransferPercentIfAny_O())))));
        } else {
            this.allocatedFundToIndexFund_P = "" + 0;
        }
    }

    private String getDailyProtectFundAfterCharges_AC() {
        return dailyProtectFundAfterCharges_AC;
    }

    public void setDailyProtectFundAfterCharges_AC(boolean guaranteeCharges, double serviceTax) {
        double a = 0, b = 0;
        if (guaranteeCharges) {
            a = serviceTax;
        } else {
            a = 0;
        }
        if (getPolicyInForce_G().equals("Y")) {
            this.dailyProtectFundAfterCharges_AC = cfap.roundUp_Level2(cfap.getStringWithout_E(Double.parseDouble(getDailyProtectFundAtStart_AA()) - ((Double.parseDouble(getTotalCharges_T()) + Double.parseDouble(getTotalServiceTax_exclOfSTonFMC_U()) - Double.parseDouble(getGuaranteeCharge_U()) * (1 + a)) * Double.parseDouble(getDailyProtectFundAtStart_AA()) / (Double.parseDouble(getDailyProtectFundAtStart_AA()) + Double.parseDouble(getIndexFundAtStart_AB()))) - Double.parseDouble(getGuaranteeCharge_U()) * (1 + a)));
        } else {
            this.dailyProtectFundAfterCharges_AC = "" + 0;
        }
    }

    private String getDailyProtectFundAftercharges_BE() {
        return dailyProtectFundAftercharges_BE;
    }

    public void setDailyProtectFundAftercharges_BE(boolean guaranteeCharges, double serviceTax) {
        double a = 0;
        if (guaranteeCharges) {
            a = serviceTax;
        } else {
            a = 0;
        }
        if (getPolicyInForce_G().equals("Y")) {
            this.dailyProtectFundAftercharges_BE = cfap.roundUp_Level2(cfap.getStringWithout_E(Double.parseDouble(getDailyProtectFundAtStart_BC()) - ((Double.parseDouble(getTotalCharges_AJ()) + Double.parseDouble(getTotalServiceTax_ExclOfSTonAllocAndsurr_AY()) - Double.parseDouble(getGuaranteeCharge_AW()) * (1 + a)) * Double.parseDouble(getDailyProtectFundAtStart_BC()) / (Double.parseDouble(getDailyProtectFundAtStart_BC()) + Double.parseDouble(getIndexFundAtStart_BD()))) - Double.parseDouble(getGuaranteeCharge_AW()) * (1 + a)));
        } else {
            this.dailyProtectFundAftercharges_BE = "" + 0;
        }
    }

    private String getDailyProtectFundAtEnd_AO() {
        return dailyProtectFundAtEnd_AO;
    }

    public void setDailyProtectFundAtEnd_AO(boolean fundManagementCharges, double charge_Fund_Ren, double serviceTax) {
        double a = 0;
        if (fundManagementCharges) {
            a = Double.parseDouble(getFMCearnedOnDailyProtectFund_AG()) * (0.0135 / charge_Fund_Ren);
        } else {
            a = 0;
        }
        if (getPolicyInForce_G().equals("Y")) {
            this.dailyProtectFundAtEnd_AO = "" + (Double.parseDouble(getDailyProtectFundAfterCharges_AC()) + Double.parseDouble(getAdditionToDailyProtectFund_AE()) - Double.parseDouble(getFMCearnedOnDailyProtectFund_AG()) - a * serviceTax);
        } else {
            this.dailyProtectFundAtEnd_AO = "" + 0;
        }
    }

    private String getDailyProtectFundAtStart_AA() {
        return dailyProtectFundAtStart_AA;
    }

    public void setDailyProtectFundAtStart_AA(double percentToBeInvestedForDailyProtectFund, double _dailyProtectFundAtEnd) {
        if (getPolicyInForce_G().equals("Y")) {
            this.dailyProtectFundAtStart_AA = cfap.roundUp_Level2(cfap.getStringWithout_E(Double.parseDouble(getAmountAvailableForInvestment_O()) * percentToBeInvestedForDailyProtectFund + Double.parseDouble(getTransferOfIndexFundGainToDailyProtectFund_Y()) + Double.parseDouble(getTransferOfCapitalFromIndexToDailyProtectFund_Z()) + _dailyProtectFundAtEnd));
        } else {
            this.dailyProtectFundAtStart_AA = "" + 0;
        }
    }

    private String getDailyProtectFundAtStart_BC() {
        return dailyProtectFundAtStart_BC;
    }

    public void setDailyProtectFundAtStart_BC(double _dailyProtectFundAtEnd_BQ, double percentToBeInvestedForDailyProtectFund) {
        if (getPolicyInForce_G().equals("Y")) {
            this.dailyProtectFundAtStart_BC = cfap.roundUp_Level2(cfap.getStringWithout_E(Double.parseDouble(getAmountAvailableForInvestment_O()) * percentToBeInvestedForDailyProtectFund + Double.parseDouble(getTransferOfIndexFundGainToDailyProtectFund_BA()) + Double.parseDouble(getTransferOfCapitalFromIndexToDailyProtectFund_BB()) + _dailyProtectFundAtEnd_BQ));
        } else {
            this.dailyProtectFundAtStart_BC = "" + 0;
        }
    }

    private String getDailyProtectFundAtEnd_BQ() {
        return dailyProtectFundAtend_BQ;
    }

    public void setDailyProtectFundAtEnd_BQ(double serviceTax, double charge_Fund_Ren, boolean fundManagementCharges) {
        double a = 0;
        if (fundManagementCharges) {
            a = Double.parseDouble(getFMCearnedOnDailyProtectFund_BI()) * (0.0135 / charge_Fund_Ren);
        } else {
            a = 0;
        }
        if (getPolicyInForce_G().equals("Y")) {
            this.dailyProtectFundAtend_BQ = "" + (Double.parseDouble(getDailyProtectFundAftercharges_BE()) + Double.parseDouble(getAdditionToDailyProtectFund_BG()) - Double.parseDouble(getFMCearnedOnDailyProtectFund_BI()) - a * serviceTax);
        } else {
            this.dailyProtectFundAtend_BQ = "" + 0;
        }
    }

    public String getDeathBenefit_AH() {
        return deathBenefit_AH;
    }

    public void setDeathBenefit_AH(double sumAssured, int policyTerm) {
        double max = Math.max(sumAssured, Double.parseDouble(getOneHundredPercentOfCummulativePremium_AZ()));
        if (Integer.parseInt(getYear_F()) > policyTerm) {
            this.deathBenefit_AH = "" + 0;
        } else {
            this.deathBenefit_AH = cfap.getRoundOffLevel2(cfap.getStringWithout_E(max));
        }
    }

    public String getDeathBenefit_AX() {
        return deathBenefit_AX;
    }

    public void setDeathBenefit_AX(double sumAssured, int policyTerm) {
        double max = Math.max(sumAssured, Double.parseDouble(getOneHundredPercentOfCummulativePremium_AZ()));
        if (Integer.parseInt(getYear_F()) > policyTerm) {
            this.deathBenefit_AX = "" + 0;
        } else {
            this.deathBenefit_AX = cfap.getRoundOffLevel2(cfap.getStringWithout_E(max));
        }
    }

    public String getFundBeforeFMC_X() {
        return fundBeforeFMC_X;
    }

    public void setFundBeforeFMC_X(int policyTerm, double _fundValueAtEnd_AD) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            //System.out.println("AD "+cfap.getStringWithout_E(_fundValueAtEnd_AD)+"   O "+getAmountAvailableForInvestment_O()+ "    T "+getTotalCharges_T()+"    W "+getAdditionToFundIfAny_W()+"    U "+getTotalServiceTax_exclOfSTonFMC_U());
            this.fundBeforeFMC_X = cfap.getRoundOffLevel2(cfap.getStringWithout_E(_fundValueAtEnd_AD + Double.parseDouble(getAmountAvailableForInvestment_O()) - Double.parseDouble(getTotalCharges_T()) + Double.parseDouble(getAdditionToFundIfAny_W()) - Double.parseDouble(getTotalServiceTax_exclOfSTonFMC_U())));

        } else {
            this.fundBeforeFMC_X = "" + 0;
        }
    }

    public String getFundBeforeFMC_AN() {
        return fundBeforeFMC_AN;
    }

    public void setFundBeforeFMC_AN(int policyTerm, double _fundValueAtEnd_AT) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.fundBeforeFMC_AN = cfap.getRoundOffLevel2(cfap.getStringWithout_E(_fundValueAtEnd_AT + Double.parseDouble(getAmountAvailableForInvestment_O()) - Double.parseDouble(getTotalCharges_T()) + Double.parseDouble(getAdditionToFundIfAny_AM()) - Double.parseDouble(getTotalServiceTax_exclOfSTonFMC_AK())));
        } else {
            this.fundBeforeFMC_AN = "" + 0;
        }
    }

    public String getFundManagementCharge_Y() {
        return fundManagementCharge_Y;
    }

    public void setFundManagementCharge_Y(int policyTerm, double percentToBeInvested_EquityFund, double percentToBeInvested_EquityOptimiserFund, double percentToBeInvested_GrowthFund, double percentToBeInvested_BalancedFund, double percentToBeInvested_BondFund, double percentToBeInvested_MoneyMarketFund, double percentToBeInvested_IndexFund, double percentToBeInvested_Top300Fund, double percentToBeInvested_PEmanagedFund) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            double temp1 = 0, temp2 = 0, temp21 = 0, temp22;
            temp1 = Double.parseDouble(getFundBeforeFMC_X());
            if (Integer.parseInt(getMonth_E()) == 1) {
                temp21 = ssProp.charge_Fund;
            } else {
                temp21 = 0;
            }
            temp22 = getCharge_fund_ren(percentToBeInvested_EquityFund, percentToBeInvested_EquityOptimiserFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_BondFund, percentToBeInvested_MoneyMarketFund, percentToBeInvested_IndexFund, percentToBeInvested_Top300Fund, percentToBeInvested_PEmanagedFund) * 0.083333333;
//	            System.out.println("temp22"+temp22);
            temp2 = temp21 + temp22;
            this.fundManagementCharge_Y = cfap.getRoundOffLevel2New(cfap.getStringWithout_E(temp1 * temp2));
            //System.out.println("X "+getFundBeforeFMC_X()+ "    temp1 "+cfap.getStringWithout_E(temp1)+"   temp2 "+temp2);

            //this.fundManagementCharge_Y= ""+temp1*temp2;
        } else {
            this.fundManagementCharge_Y = "" + 0;
        }
    }

    public String getFundManagementCharge_AO() {
        return fundManagementCharge_AO;
    }

    public void setFundManagementCharge_AO(int policyTerm, double percentToBeInvested_EquityFund, double percentToBeInvested_EquityOptimiserFund, double percentToBeInvested_GrowthFund, double percentToBeInvested_BalancedFund, double percentToBeInvested_BondFund, double percentToBeInvested_MoneyMarketFund, double percentToBeInvested_IndexFund, double percentToBeInvested_Top300Fund, double percentToBeInvested_PEmanagedFund) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            double temp1 = 0, temp2 = 0, temp21 = 0, temp22;
            temp1 = Double.parseDouble(getFundBeforeFMC_AN());
            if (Integer.parseInt(getMonth_E()) == 1) {
                temp21 = ssProp.charge_Fund;
            } else {
                temp21 = 0;
            }
            temp22 = getCharge_fund_ren(percentToBeInvested_EquityFund, percentToBeInvested_EquityOptimiserFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_BondFund, percentToBeInvested_MoneyMarketFund, percentToBeInvested_IndexFund, percentToBeInvested_Top300Fund, percentToBeInvested_PEmanagedFund) / 12;
            temp2 = temp21 + temp22;
            this.fundManagementCharge_AO = cfap.getRoundOffLevel2(cfap.getStringWithout_E(temp1 * temp2));
        } else {
            this.fundManagementCharge_AO = "" + 0;
        }
    }

    public String getFundValueAfterFMCAndBeforeGAReductionYeild_AA() {
        return fundValueAfterFMCAndBeforeGAReductionYeild_AA;
    }

    public void setFundValueAfterFMCAndBeforeGAReductionYeild_AA(int policyTerm) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.fundValueAfterFMCAndBeforeGAReductionYeild_AA = "" + (Double.parseDouble(getFundBeforeFMC_X()) - Double.parseDouble(getFundManagementCharge_Y()) - Double.parseDouble(getServiceTaxOnFMCReductionYeild_Z()));
        } else {
            this.fundValueAfterFMCAndBeforeGAReductionYeild_AA = "" + 0;
        }
    }

    public String getFundValueAfterFMCAndBeforeGAReductionYeild_AQ() {
        return fundValueAfterFMCAndBeforeGAReductionYeild_AQ;
    }

    public void setFundValueAfterFMCAndBeforeGAReductionYeild_AQ(int policyTerm) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.fundValueAfterFMCAndBeforeGAReductionYeild_AQ = cfap.roundUp_Level2(cfap.getStringWithout_E(Double.parseDouble(getFundBeforeFMC_AN()) - Double.parseDouble(getFundManagementCharge_AO()) - Double.parseDouble(getServiceTaxOnFMCReductionYeild_AP())));
        } else {
            this.fundValueAfterFMCAndBeforeGAReductionYeild_AQ = "" + 0;
        }
    }


    public String getFundValueAfterFMCAndBeforeGA_AA() {
        return fundValueAfterFMCAndBeforeGA_AA;
    }

    public void setFundValueAfterFMCAndBeforeGA_AA(int policyTerm) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            //System.out.println("X "+getFundBeforeFMC_X()+"    Y "+getFundManagementCharge_Y()+"    Z "+getServiceTaxOnFMC_Z());
            this.fundValueAfterFMCAndBeforeGA_AA = cfap.getRoundOffLevel2New(cfap.getStringWithout_E(Double.parseDouble(getFundBeforeFMC_X()) - Double.parseDouble(getFundManagementCharge_Y()) - Double.parseDouble(getServiceTaxOnFMC_Z())));

        } else {
            this.fundValueAfterFMCAndBeforeGA_AA = "" + 0;
        }
    }

    public String getFundValueAfterFMCAndBeforeGA_AQ() {
        return fundValueAfterFMCAndBeforeGA_AQ;
    }

    public void setFundValueAfterFMCAndBeforeGA_AQ(int policyTerm) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.fundValueAfterFMCAndBeforeGA_AQ = cfap.getRoundOffLevel2(cfap.getStringWithout_E(Double.parseDouble(getFundBeforeFMC_AN()) - Double.parseDouble(getFundManagementCharge_AO()) - Double.parseDouble(getServiceTaxOnFMC_AP())));
        } else {
            this.fundValueAfterFMCAndBeforeGA_AQ = "" + 0;
        }
    }


    private String getFundValueAfterFMCandBeforeGA_AM() {
        return fundValueAfterFMCandBeforeGA_AM;
    }

    public void setFundValueAfterFMCandBeforeGA_AM(int policyTerm) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.fundValueAfterFMCandBeforeGA_AM = "" + (Double.parseDouble(getFundBeforeFMC_X()) - Double.parseDouble(getFundManagementCharge_Y()) - Double.parseDouble(getServiceTaxOnFMC_Z()));
        } else {
            this.fundValueAfterFMCandBeforeGA_AM = "" + 0;
        }
    }

    public String getFundValueAtEnd_AQ() {
        return fundValueAtEnd_AQ;
    }

    public void setFundValueAtEnd_AQ() {
        this.fundValueAtEnd_AQ = "" + (Double.parseDouble(getGuaranteedAddition_AN()) + Double.parseDouble(getDailyProtectFundAtEnd_AO()) + Double.parseDouble(getIndexFundAtEnd_AP()));
    }

    public String getFundValueAtEnd_BS() {
        return fundValueAtEnd_BS;
    }

    public void setFundValueAtEnd_BS() {
        this.fundValueAtEnd_BS = "" + (Double.parseDouble(getGuaranteedAddition_BP()) + Double.parseDouble(getDailyProtectFundAtEnd_BQ()) + Double.parseDouble(getIndexFundAtEnd_BR()));
    }

    private String getGuaranteeCharge_AW() {
        return guaranteeCharge_AW;
    }

    public void setGuaranteeCharge_AW(boolean mortalityCharges, double charge_Guarantee) {
        if (getPolicyInForce_G().equals("Y") && mortalityCharges) {
            this.guaranteeCharge_AW = "" + (Double.parseDouble(getDailyProtectFundAtStart_BC()) * charge_Guarantee / 12);
        } else {
            this.guaranteeCharge_AW = "" + 0;
        }
    }

    private String getGuaranteeCharge_U() {
        return guaranteeCharge_U;
    }

    public void setGuaranteeCharge_U(boolean guaranteeCharges, double charge_Guarantee) {
        if (getPolicyInForce_G().equals("Y") && guaranteeCharges) {
            this.guaranteeCharge_U = "" + (Double.parseDouble(getDailyProtectFundAtStart_AA()) * charge_Guarantee / 12);
        } else {
            this.guaranteeCharge_U = "" + 0;
        }
    }

    private String getGuaranteedAddition_AN() {
        return guaranteedAddition_AN;
    }

    public void setGuaranteedAddition_AN() {
        this.guaranteedAddition_AN = "" + 0;
    }

    private String getGuaranteedAddition_BP() {
        return guaranteedAddition_BP;
    }

    public void setGuaranteedAddition_BP() {
        this.guaranteedAddition_BP = "" + 0;
    }

    private String getIndexFundAtEnd_AP() {
        return indexFundAtEnd_AP;
    }

    public void setIndexFundAtEnd_AP(boolean fundManagementCharges, double indexFund, double serviceTax) {
        if (getPolicyInForce_G().equals("Y")) {
            double a = 0;
            if (fundManagementCharges) {
                a = Double.parseDouble(getFMCearnedOnIndexFund_AH()) * (0.0135 / indexFund);
            } else {
                a = 0;
            }
            this.indexFundAtEnd_AP = "" + (Double.parseDouble(getIndexFundAtStartAfterCharges_AD()) + Double.parseDouble(getAdditionToIndexFund_AF()) - Double.parseDouble(getFMCearnedOnIndexFund_AH()) - a * serviceTax);
        } else {
            this.indexFundAtEnd_AP = "" + 0;
        }
    }

    private String getIndexFundAtEnd_BR() {
        return indexFundAtEnd_BR;
    }

    public void setIndexFundAtEnd_BR(boolean fundManagementCharges, double indexFund, double serviceTax) {
        double a = 0;
        if (fundManagementCharges) {
            a = Double.parseDouble(getFMCearnedOnIndexFund_BJ()) * (0.0135 / indexFund);
        } else {
            a = 0;
        }
        if (getPolicyInForce_G().equals("Y")) {
            this.indexFundAtEnd_BR = "" + (Double.parseDouble(getIndexFundAtStartAfterCharges_BF()) + Double.parseDouble(getAdditionToIndexFund_BH()) - Double.parseDouble(getFMCearnedOnIndexFund_BJ()) - a * serviceTax);
        } else {
            this.indexFundAtEnd_BR = "" + 0;
        }
    }

    private String getIndexFundAtStartAfterCharges_AD() {
        return indexFundAtStartAfterCharges_AD;
    }

    public void setIndexFundAtStartAfterCharges_AD(boolean guaranteeCharges, double serviceTax) {
        if (getPolicyInForce_G().equals("Y")) {
            double a = 0;
            if (guaranteeCharges) {
                a = serviceTax;
            } else {
                a = 0;
            }
            this.indexFundAtStartAfterCharges_AD = cfap.roundUp_Level2(cfap.getStringWithout_E(Double.parseDouble(getIndexFundAtStart_AB()) - ((Double.parseDouble(getTotalCharges_T()) + Double.parseDouble(getTotalServiceTax_exclOfSTonFMC_U()) - Double.parseDouble(getGuaranteeCharge_U()) * (1 + a)) * Double.parseDouble(getIndexFundAtStart_AB()) / (Double.parseDouble(getDailyProtectFundAtStart_AA()) + Double.parseDouble(getIndexFundAtStart_AB())))));
        } else {
            this.indexFundAtStartAfterCharges_AD = "" + 0;
        }
    }

    private String getIndexFundAtStartAfterCharges_BF() {
        return indexFundAtStartAfterCharges_BF;
    }

    public void setIndexFundAtStartAfterCharges_BF(boolean guaranteeCharges, double serviceTax) {
        double a = 0;
        if (guaranteeCharges) {
            a = serviceTax;
        } else {
            a = 0;
        }
        if (getPolicyInForce_G().equals("Y")) {
            this.indexFundAtStartAfterCharges_BF = cfap.roundUp_Level2(cfap.getStringWithout_E(Double.parseDouble(getIndexFundAtStart_BD()) - ((Double.parseDouble(getTotalCharges_AJ()) + Double.parseDouble(getTotalServiceTax_ExclOfSTonAllocAndsurr_AY()) - Double.parseDouble(getGuaranteeCharge_AW()) * (1 + a)) * Double.parseDouble(getIndexFundAtStart_BD()) / (Double.parseDouble(getDailyProtectFundAtStart_BC()) + Double.parseDouble(getIndexFundAtStart_BD())))));
        } else {
            this.indexFundAtStartAfterCharges_BF = "" + 0;
        }
    }

    private String getIndexFundAtStart_AB() {
        return indexFundAtStart_AB;
    }

    public void setIndexFundAtStart_AB(double _indexFundAtEnd_AP, double percentToBeInvestedForIndexFund) {
        if (getPolicyInForce_G().equals("Y")) {
            this.indexFundAtStart_AB = cfap.roundUp_Level2(cfap.getStringWithout_E(Double.parseDouble(getAmountAvailableForInvestment_O()) * percentToBeInvestedForIndexFund + _indexFundAtEnd_AP - Double.parseDouble(getTransferOfIndexFundGainToDailyProtectFund_Y()) - Double.parseDouble(getTransferOfCapitalFromIndexToDailyProtectFund_Z())));
        } else {
            this.indexFundAtStart_AB = "" + 0;
        }
    }

    private String getIndexFundAtStart_BD() {
        return indexFundAtStart_BD;
    }

    public void setIndexFundAtStart_BD(double _indexFundAtEnd, double percentToBeInvestedForIndexFund) {
        if (getPolicyInForce_G().equals("Y")) {
            this.indexFundAtStart_BD = cfap.roundUp_Level2(cfap.getStringWithout_E(Double.parseDouble(getAmountAvailableForInvestment_O()) * percentToBeInvestedForIndexFund + _indexFundAtEnd - Double.parseDouble(getTransferOfIndexFundGainToDailyProtectFund_BA()) - Double.parseDouble(getTransferOfCapitalFromIndexToDailyProtectFund_BB())));
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

    public String getMortalityAndPPWBCharges_AI() {
        return mortalityAndPPWBCharges_AI;
    }

/*    public void setMortalityAndPPWBCharges_AI(boolean PPWBstatus,String premFreqMode, double effectivePremium,String[] forBIArray,int ageOfProposer,int premPayingTerm ,int policyTerm, boolean mortalityCharges,double sumAssured,int PF)
     {
         if(Integer.parseInt(getYear_F())>policyTerm)
         {this.mortalityAndPPWBCharges_AI=""+0;}
         else
         {
            double arrOutput = Double.parseDouble(forBIArray[(ageOfProposer + Integer.parseInt(getYear_F())) - 1]);
//	             double a=1-arrOutput/1000;
            double div = arrOutput / 1000;
            double div1 = div / 12;
            double a = 1 - div1;
//	             double b=(double)1/12;
            double c = 0;
             if(Integer.parseInt(getYear_F())>policyTerm)
             {c=0;}
             else
             {c=getPWBcharges(ageOfProposer,effectivePremium,premFreqMode,PPWBstatus,premPayingTerm,PF)+(1-(a))*sumAssured;}
            int d = 0;
             if(mortalityCharges)
             {d=1;}
             else
             {d=0;}
             this.mortalityAndPPWBCharges_AI= cfap.getRoundOffLevel2(cfap.getStringWithout_E((c*d)));
         }
    }*/

    public void setMortalityAndPPWBCharges_AI(double _fundValueAtEnd_AB, int policyTerm, String[] forBIArray, double sumAssured, boolean mortalityCharges) {
        if (!(getPolicyInForce_G().equals("Y")) || Integer.parseInt(getYear_F()) > policyTerm) {
            this.mortalityAndPPWBCharges_AI = "" + 0;
        } else {
            double arrOutput = Double.parseDouble(forBIArray[Integer.parseInt(getProposersAge_I())]);


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
            double max1 = Math.max(sumAssured, Double.parseDouble(getOneHundredPercentOfCummulativePremium_AZ()));

//            System.out.println("max1 "+max1);

//            double b=(max1-(Double.parseDouble(getAmountAvailableForInvestment_O())+_fundValueAtEnd_AB));


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
//            System.out.println("a*Math.max(b, 0))*c"+((a*max1*c)));
            this.mortalityAndPPWBCharges_AI = cfap.getRoundOffLevel2New(cfap.getStringWithout_E(((a * max1 * c))));
        }
    }

    public String getMortalityAndPPWBChargesReductionYeild_AI() {
        return mortalityAndPPWBChargesReductionYeild_AI;
    }
   /* public void setMortalityAndPPWBChargesReductionYeild_AI(boolean PPWBstatus,String premFreqMode, double effectivePremium,String[] forBIArray,int ageOfProposer,int premPayingTerm ,int policyTerm, boolean mortalityCharges,double sumAssured,int PF)
     {
         if(Integer.parseInt(getYear_F())>policyTerm)
         {this.mortalityAndPPWBChargesReductionYeild_AI=""+0;}
         else
         {
            double arrOutput = Double.parseDouble(forBIArray[(ageOfProposer + Integer.parseInt(getYear_F())) - 1]);
//	             double a=1-arrOutput/1000;
            double div = arrOutput / 1000;
            double div1 = Math.pow(div, 1 / 12);
            double a = 1 - div1;
//	             double b=(double)1/12;
            double c = 0;
             if(Integer.parseInt(getYear_F())>policyTerm)
             {c=0;}
             else
            {c=getPWBcharges(ageOfProposer,effectivePremium,premFreqMode,PPWBstatus,premPayingTerm,PF,mortalityCharges)+(1-(a))*sumAssured;}
             int d=0;
             if(mortalityCharges)
             {d=1;}
             else
             {d=0;}
             this.mortalityAndPPWBChargesReductionYeild_AI= cfap.roundUp_Level2(cfap.getStringWithout_E((c*d)));
         }
    }*/

    public void setMortalityAndPPWBChargesReductionYeild_AI(double _fundValueAtEnd_AB, int policyTerm, String[] forBIArray, double sumAssured, boolean mortalityCharges) {
        if (!(getPolicyInForce_G().equals("Y")) || Integer.parseInt(getYear_F()) > policyTerm) {
            this.mortalityAndPPWBChargesReductionYeild_AI = "" + 0;
        } else {
            double arrOutput = Double.parseDouble(forBIArray[Integer.parseInt(getProposersAge_I())]);


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
            double max1 = Math.max(sumAssured, Double.parseDouble(getOneHundredPercentOfCummulativePremium_AZ()));

//            System.out.println("max1 "+max1);

//            double b=(max1-(Double.parseDouble(getAmountAvailableForInvestment_O())+_fundValueAtEnd_AB));


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
//            System.out.println("a*Math.max(b, 0))*c"+((a*max1*c)));
            this.mortalityAndPPWBChargesReductionYeild_AI = cfap.getRoundOffLevel2New(cfap.getStringWithout_E(((a * max1 * c))));
        }
    }

    public String getMortalityAndPPWBCharges_S() {
        return mortalityAndPPWBCharges_S;
    }
/*    public void setMortalityAndPPWBCharges_S(boolean PPWBstatus,String premFreqMode, double effectivePremium,String[] forBIArray,int ageOfProposer,int premPayingTerm ,int policyTerm, boolean mortalityCharges,double sumAssured,int PF)
     {
         if(Integer.parseInt(getYear_F())>policyTerm)
         {this.mortalityAndPPWBCharges_S=""+0;}
         else
         {
            double arrOutput = Double.parseDouble(forBIArray[(ageOfProposer + Integer.parseInt(getYear_F())) - 1]);
//	             double a=1-arrOutput/1000;
            double div = arrOutput / 1000;
            double div1 = div / 12;
            double a = 1 - div1;
//	             double b=(double)1/12;
            double c = 0;
             if(Integer.parseInt(getYear_F())>policyTerm)
             {c=0;}
             else
             {c=getPWBcharges(ageOfProposer,effectivePremium,premFreqMode,PPWBstatus,premPayingTerm,PF)+(1-(a))*sumAssured;}
             int d=0;
             if(mortalityCharges)
             {d=1;}
             else
             {d=0;}
             this.mortalityAndPPWBCharges_S= cfap.getRoundOffLevel2(cfap.getStringWithout_E((c*d)));
         }
    }*/

    public void setMortalityAndPPWBCharges_S(double _fundValueAtEnd_AB, int policyTerm, String[] forBIArray, double sumAssured, boolean mortalityCharges) {
        if (!(getPolicyInForce_G().equals("Y")) || Integer.parseInt(getYear_F()) > policyTerm) {
            this.mortalityAndPPWBCharges_S = "" + 0;
        } else {
            double arrOutput = Double.parseDouble(forBIArray[Integer.parseInt(getProposersAge_I())]);


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
            double max1 = Math.max(sumAssured, Double.parseDouble(getOneHundredPercentOfCummulativePremium_AZ()));

//            System.out.println("max1 "+max1);

//            double b=(max1-(Double.parseDouble(getAmountAvailableForInvestment_O())+_fundValueAtEnd_AB));


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
//            System.out.println("a*Math.max(b, 0))*c"+((a*max1*c)));
            this.mortalityAndPPWBCharges_S = cfap.getRoundOffLevel2New(cfap.getStringWithout_E(((a * max1 * c))));
        }
    }


    public String getMortalityAndPPWBChargesReductionYeild_S() {
        return mortalityAndPPWBChargesReductionYeild_S;
    }

   /* public void setMortalityAndPPWBChargesReductionYeild_S(boolean PPWBstatus,String premFreqMode, double effectivePremium,String[] forBIArray,int ageOfProposer,int premPayingTerm ,int policyTerm, boolean mortalityCharges,double sumAssured,int PF)
     {
         if(Integer.parseInt(getYear_F())>policyTerm)
         {this.mortalityAndPPWBChargesReductionYeild_S=""+0;}
         else
         {
            double arrOutput = Double.parseDouble(forBIArray[(ageOfProposer + Integer.parseInt(getYear_F())) - 1]);
//	             double a=1-arrOutput/1000;
            double div = arrOutput / 1000;
            double div1 = Math.pow(div, 1 / 12);
            double a = 1 - div1;
//	             double b=(double)1/12;
            double c = 0;
             if(Integer.parseInt(getYear_F())>policyTerm)
             {c=0;}
             else
            {c=getPWBcharges(ageOfProposer,effectivePremium,premFreqMode,PPWBstatus,premPayingTerm,PF,mortalityCharges)+(1-(a))*sumAssured;}
             int d=0;
             if(mortalityCharges)
             {d=1;}
             else
             {d=0;}
             this.mortalityAndPPWBChargesReductionYeild_S= cfap.roundUp_Level2(cfap.getStringWithout_E((c*d)));
         }
    }*/

    public void setMortalityAndPPWBChargesReductionYeild_S(double _fundValueAtEnd_AB, int policyTerm, String[] forBIArray, double sumAssured, boolean mortalityCharges) {
        if (!(getPolicyInForce_G().equals("Y")) || Integer.parseInt(getYear_F()) > policyTerm) {
            this.mortalityAndPPWBChargesReductionYeild_S = "" + 0;
        } else {
            double arrOutput = Double.parseDouble(forBIArray[Integer.parseInt(getProposersAge_I())]);


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
            double max1 = Math.max(sumAssured, Double.parseDouble(getOneHundredPercentOfCummulativePremium_AZ()));

//            System.out.println("max1 "+max1);

//            double b=(max1-(Double.parseDouble(getAmountAvailableForInvestment_O())+_fundValueAtEnd_AB));


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
//            System.out.println("a*Math.max(b, 0))*c"+((a*max1*c)));
            this.mortalityAndPPWBChargesReductionYeild_S = cfap.getRoundOffLevel2New(cfap.getStringWithout_E(((a * max1 * c))));
        }
    }

    public String getOneHundredPercentOfCummulativePremium_AZ() {
        return oneHundredPercentOfCummulativePremium_AZ;
    }
   /* public void setOneHundredPercentOfCummulativePremium_AZ(double _prem_J)
     {
         double a=0;
         if(getPolicyInForce_G().equals("Y"))
         {this.oneHundredPercentOfCummulativePremium_AZ=""+(1.05*(_prem_J));}
         else
         {this.oneHundredPercentOfCummulativePremium_AZ=""+0;}
    }*/

    public void setOneHundredPercentOfCummulativePremium_AZ(double _oneHundredPercentOfCummulativePremium_AZ) {
        double a = 0;
        if (getPolicyInForce_G().equals("Y")) {
            a = Double.parseDouble(getPremium_J()) + (_oneHundredPercentOfCummulativePremium_AZ / 1.05);
        } else {
            a = 0;
        }
        this.oneHundredPercentOfCummulativePremium_AZ = "" + (a * 1.05);
    }

    public String getOptionCharges_R() {
        return optionCharges_R;
    }

    public void setOptionCharges_R(boolean riderCharges, boolean eligibilityRider, int policyTermADB, double ADBrate, double SA_ADB) {
        if (riderCharges && eligibilityRider && (Integer.parseInt(getYear_F()) <= policyTermADB)) {
            String dummy = "" + (ADBrate * SA_ADB / 12);
            this.optionCharges_R = dummy;
        } else {
            this.optionCharges_R = "" + 0;
        }
    }

    public String getPolicyAdministrationCharge_R() {
        return policyAdministrationCharge_R;
    }

    public void setPolicyAdministrationCharge_R(double _policyAdministrationCharge_R) {
        if (getPolicyInForce_G().equals("Y")) {
            if (((Integer.parseInt(getMonth_E()) - 1) % 12) == 0)  //(MOD(Month-1,12)=0)
            {
                this.policyAdministrationCharge_R = cfap.getRoundOffLevel2(cfap.getStringWithout_E(ssProp.monthlyAdminCharge));
            } else {
                this.policyAdministrationCharge_R = cfap.getRoundOffLevel2(cfap.getStringWithout_E(_policyAdministrationCharge_R));
            }
        } else {
            this.policyAdministrationCharge_R = "" + 0;
        }
    }

    public String getPolicyInForce_G() {
        return policyInForce_G;
    }

    public String getPremiumAllocationCharge_L() {
        return premiumAllocationCharge_L;
    }

    public void setPremiumAllocationCharge_L(boolean staffDisc, boolean bancAssuranceDisc, String premFreqmode) {
        if (Integer.parseInt(getYear_F()) > 10) {
            this.premiumAllocationCharge_L = ("" + 0);
        } else {
            this.premiumAllocationCharge_L = cfap.getRoundUp(cfap.getStringWithout_E(getAllocationCharges(staffDisc, bancAssuranceDisc, premFreqmode) * Double.parseDouble(getPremium_J())), "roundUpII");
        }
    }

    public String getPremium_J() {
        return premium_J;
    }

    public void setPremium_J(int premiumPayingTerm, int PF, double effectivePrem) {
        if (getPolicyInForce_G().equals("Y")) {
            if ((Integer.parseInt(getYear_F()) <= premiumPayingTerm) && (((Integer.parseInt(getMonth_E()) - 1) % (12 / PF)) == 0)) {
                premium_J = "" + (effectivePrem / PF);
            } else {
                premium_J = ("" + 0);
            }
        } else {
            premium_J = ("" + 0);
        }
    }

    public String getServiceTaxOnAllocation_N() {
        return serviceTaxOnAllocation_N;
    }

    public void setServiceTaxOnAllocation_N(boolean allocationCharges, double serviceTax) {
        if (allocationCharges) {
            this.serviceTaxOnAllocation_N = cfap.getRoundOffLevel2(cfap.getStringWithout_E((Double.parseDouble(getPremiumAllocationCharge_L()) + Double.parseDouble(getTopUpCharges_M())) * serviceTax));
        } else {
            this.serviceTaxOnAllocation_N = ("" + 0);
        }
    }

    public String getServiceTaxOnFMC_Z() {
        return serviceTaxOnFMC_Z;
    }

    public void setServiceTaxOnFMC_Z(boolean fundManagementCharges, double percentToBeInvested_EquityFund, double percentToBeInvested_EquityOptimiserFund, double percentToBeInvested_GrowthFund, double percentToBeInvested_BalancedFund, double percentToBeInvested_BondFund, double percentToBeInvested_MoneyMarketFund, double percentToBeInvested_IndexFund, double percentToBeInvested_Top300Fund, double percentToBeInvested_PEmanagedFund, double serviceTax) {
        double a = 0;
        if (fundManagementCharges) {
            a = Double.parseDouble(getFundManagementCharge_Y()) * (0.0135 / getCharge_fund_ren(percentToBeInvested_EquityFund, percentToBeInvested_EquityOptimiserFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_BondFund, percentToBeInvested_MoneyMarketFund, percentToBeInvested_IndexFund, percentToBeInvested_Top300Fund, percentToBeInvested_PEmanagedFund) / (0.0135 / getCharge_fund_ren(percentToBeInvested_EquityFund, percentToBeInvested_EquityOptimiserFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_BondFund, percentToBeInvested_MoneyMarketFund, percentToBeInvested_IndexFund, percentToBeInvested_Top300Fund, percentToBeInvested_PEmanagedFund)));
        } else {
            a = 0;
        }
        this.serviceTaxOnFMC_Z = cfap.getRoundOffLevel2(cfap.getStringWithout_E(a * serviceTax));
    }

    public String getServiceTaxOnFMCReductionYeild_Z() {
        return serviceTaxOnFMCReductionYeild_Z;
    }

    public void setServiceTaxOnFMCReductionYeild_Z(boolean fundManagementCharges, double percentToBeInvested_EquityFund, double percentToBeInvested_EquityOptimiserFund, double percentToBeInvested_GrowthFund, double percentToBeInvested_BalancedFund, double percentToBeInvested_BondFund, double percentToBeInvested_MoneyMarketFund, double percentToBeInvested_IndexFund, double percentToBeInvested_Top300Fund, double percentToBeInvested_PEmanagedFund, double serviceTax) {
        double a = 0;
        if (fundManagementCharges) {
            a = Double.parseDouble(getFundManagementCharge_Y());
        } else {
            a = 0;
        }
        this.serviceTaxOnFMCReductionYeild_Z = cfap.roundUp_Level2("" + (a * serviceTax));
    }

    public String getServiceTaxOnFMCReductionYeild_AP() {
        return serviceTaxOnFMCReductionYeild_AP;
    }

    public void setServiceTaxOnFMCReductionYeild_AP(boolean fundManagementCharges, double percentToBeInvested_EquityFund, double percentToBeInvested_EquityOptimiserFund, double percentToBeInvested_GrowthFund, double percentToBeInvested_BalancedFund, double percentToBeInvested_BondFund, double percentToBeInvested_MoneyMarketFund, double percentToBeInvested_IndexFund, double percentToBeInvested_Top300Fund, double percentToBeInvested_PEmanagedFund, double serviceTax) {
        double a = 0;
        if (fundManagementCharges) {
            a = Double.parseDouble(getFundManagementCharge_AO());
        } else {
            a = 0;
        }
        this.serviceTaxOnFMCReductionYeild_AP = cfap.roundUp_Level2(cfap.getStringWithout_E(a * serviceTax));
    }

    public String getServiceTaxOnFMC_AP() {
        return serviceTaxOnFMC_AP;
    }

    public void setServiceTaxOnFMC_AP(boolean fundManagementCharges, double percentToBeInvested_EquityFund, double percentToBeInvested_EquityOptimiserFund, double percentToBeInvested_GrowthFund, double percentToBeInvested_BalancedFund, double percentToBeInvested_BondFund, double percentToBeInvested_MoneyMarketFund, double percentToBeInvested_IndexFund, double percentToBeInvested_Top300Fund, double percentToBeInvested_PEmanagedFund, double serviceTax) {
        double a = 0;
        if (fundManagementCharges)
//        {a=Double.parseDouble(getFundManagementCharge_AO()) * (0.0135/getCharge_fund_ren( percentToBeInvested_EquityFund, percentToBeInvested_EquityOptimiserFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_BondFund, percentToBeInvested_MoneyMarketFund, percentToBeInvested_IndexFund, percentToBeInvested_Top300Fund, percentToBeInvested_PEmanagedFund)/(getCharge_fund_ren( percentToBeInvested_EquityFund, percentToBeInvested_EquityOptimiserFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_BondFund, percentToBeInvested_MoneyMarketFund, percentToBeInvested_IndexFund, percentToBeInvested_Top300Fund, percentToBeInvested_PEmanagedFund)));}
        {
            a = Double.parseDouble(getFundManagementCharge_AO()) * (0.0135 / getCharge_fund_ren(percentToBeInvested_EquityFund, percentToBeInvested_EquityOptimiserFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_BondFund, percentToBeInvested_MoneyMarketFund, percentToBeInvested_IndexFund, percentToBeInvested_Top300Fund, percentToBeInvested_PEmanagedFund) / (0.0135 / getCharge_fund_ren(percentToBeInvested_EquityFund, percentToBeInvested_EquityOptimiserFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_BondFund, percentToBeInvested_MoneyMarketFund, percentToBeInvested_IndexFund, percentToBeInvested_Top300Fund, percentToBeInvested_PEmanagedFund)));
        } else {
            a = 0;
        }
        this.serviceTaxOnFMC_AP = cfap.getRoundOffLevel2(cfap.getStringWithout_E(a * serviceTax));
    }

    public String getTotalServiceTax_exclOfSTonFMC_AK() {
        return totalServiceTax_exclOfSTonFMC_AK;
    }

    public void setTotalServiceTax_exclOfSTonFMC_AK(double serviceTax, boolean mortalityAndRiderCharges, boolean administrationCharges, boolean guaranteeCharges, int ageOfProposer, double effectivePremium, String premFreqMode, boolean PPWBstatus, int premPayingTerm, int PF, boolean mortalityCharges) {
        double a = 0, b = 0;
        if (mortalityAndRiderCharges) {
            a = Double.parseDouble(getMortalityAndPPWBCharges_AI()) + Double.parseDouble(getAccidentCoverCharges_Q()) + (getPWBcharges(ageOfProposer, effectivePremium, premFreqMode, PPWBstatus, premPayingTerm, PF, mortalityCharges));
        } else {
            a = 0;
        }
        if (administrationCharges) {
            b = Double.parseDouble(getPolicyAdministrationCharge_R());
        } else {
            b = 0;
        }
        this.totalServiceTax_exclOfSTonFMC_AK = cfap.getRoundOffLevel2(cfap.getStringWithout_E((a + b) * serviceTax));
    }

    public String getServiceTaxOnSurrenderCharges_AF() {
        return serviceTaxOnSurrenderCharges_AF;
    }

    public void setServiceTaxOnSurrenderCharges_AF(boolean surrenderCharges, double serviceTax) {
        if (surrenderCharges) {
            this.serviceTaxOnSurrenderCharges_AF = cfap.getRoundOffLevel2(cfap.getStringWithout_E(Double.parseDouble(getSurrenderCharges_AE()) * serviceTax));
        } else {
            this.serviceTaxOnSurrenderCharges_AF = "" + 0;
        }
    }

    public String getServiceTaxOnSurrenderCharges_AV() {
        return serviceTaxOnSurrenderCharges_AV;
    }

    public void setServiceTaxOnSurrenderCharges_AV(boolean surrenderCharges, double serviceTax) {
        if (surrenderCharges) {
            this.serviceTaxOnSurrenderCharges_AV = cfap.getRoundOffLevel2(cfap.getStringWithout_E(Double.parseDouble(getSurrenderCharges_AU()) * serviceTax));
        } else {
            this.serviceTaxOnSurrenderCharges_AV = "" + 0;
        }
    }

    public String getSumAssuredRelatedCharges_P() {
        return sumAssuredRelatedCharges_P;
    }

    public void setSumAssuredRelatedCharges_P(int policyTerm, double sumAssured, double SAMF, double effectivePremium, double charge_SumAssuredBase, double ChargeSAren) {
        double a = 0;
        double b = 0;
        if (getYear_F().equals("1")) {
            a = charge_SumAssuredBase;
        } else {
            a = 0;
        }
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            b = ChargeSAren;
        } else {
            b = 0;
        }
        this.sumAssuredRelatedCharges_P = cfap.getRoundOffLevel2(cfap.getStringWithout_E(sumAssured * (a + b)));
    }

    public String getSurrenderCap_AY() {
        return surrenderCap_AY;
    }

    public void setSurrenderCap_AY(String premFreqMode, double effectivePremium) {
         /*if(getPolicyInForce_G().equals("Y"))
          {this.surrenderCap_AY =""+ getMaxSurrender(premFreqMode,effectivePremium);}
          else
         {this.surrenderCap_AY =""+0;}*/

        double a = 0;
        if (premFreqMode.equals("Single")) {

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
            if (effectivePremium > 25000) {

                if (effectivePremium > 50000) {

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
        }

        this.surrenderCap_AY = "" + a;
    }

    public String getSurrenderCharges_AE() {
        return surrenderCharges_AE;
    }

    public void setSurrenderCharges_AE(double effectivePremium, String premFreqMode) {
        double a = Math.min(Double.parseDouble(getFundValueAtEnd_AD()), effectivePremium);
        double b = getSurrenderCharge(premFreqMode, effectivePremium);
        this.surrenderCharges_AE = cfap.getRoundOffLevel2(cfap.getStringWithout_E(Math.min((a * b), Double.parseDouble(getSurrenderCap_AY()))));
    }

    public String getSurrenderChargesReductionYeild_AE() {
        return surrenderChargesReductionYeild_AE;
    }

    public void setSurrenderChargesReductionYeild_AE(double effectivePremium, String premFreqMode) {
        double a = Math.min(Double.parseDouble(getFundValueAtEndReductionYeild_AD()), effectivePremium);
        double b = getSurrenderCharge(premFreqMode, effectivePremium);
        if (premFreqMode.equals("Single"))
            this.surrenderChargesReductionYeild_AE = "0";
        else
            this.surrenderChargesReductionYeild_AE = cfap.roundUp_Level2(cfap.getStringWithout_E(Math.min((a * b), Double.parseDouble(getSurrenderCap_AY()))));
    }

    public String getSurrenderChargesReductionYeild_AU() {
        return surrenderChargesReductionYeild_AU;
    }

    public void setSurrenderChargesReductionYeild_AU(double effectivePremium, String premFreqMode) {
        double a = Math.min(Double.parseDouble(getFundValueAtEndReductionYeild_AT()), effectivePremium);
        double b = getSurrenderCharge(premFreqMode, effectivePremium);
        if (premFreqMode.equals("Single"))
            this.surrenderChargesReductionYeild_AU = "0";
        else
            this.surrenderChargesReductionYeild_AU = cfap.roundUp_Level2(cfap.getStringWithout_E(Math.min((a * b), Double.parseDouble(getSurrenderCap_AY()))));
    }

    public String getSurrenderCharges_AU() {
        return surrenderCharges_AU;
    }

    public void setSurrenderCharges_AU(double effectivePremium, String premFreqMode) {
        double a = Math.min(Double.parseDouble(getFundValueAtEnd_AT()), effectivePremium);
        double b = getSurrenderCharge(premFreqMode, effectivePremium);
        this.surrenderCharges_AU = cfap.getRoundOffLevel2(cfap.getStringWithout_E(Math.min((a * b), Double.parseDouble(getSurrenderCap_AY()))));
    }

    public String getSurrenderValueReductionYeild_AG() {
        return surrenderValueReductionYeild_AG;
    }

    public void setSurrenderValueReductionYeild_AG() {
        this.surrenderValueReductionYeild_AG = "" + (Double.parseDouble(getFundValueAtEndReductionYeild_AD()) - Double.parseDouble(getSurrenderChargesReductionYeild_AE()) - Double.parseDouble(getServiceTaxOnSurrenderCharges_AF()));
    }

    public String getSurrenderValueReductionYeild_AW() {
        return surrenderValueReductionYeild_AW;
    }

    public void setSurrenderValueReductionYeild_AW() {
        this.surrenderValueReductionYeild_AW = "" + (Double.parseDouble(getFundValueAtEndReductionYeild_AT()) - Double.parseDouble(getSurrenderChargesReductionYeild_AU()) - Double.parseDouble(getServiceTaxOnSurrenderCharges_AV()));
    }

    public String getSurrenderValue_AG() {
        return surrenderValue_AG;
    }

    public void setSurrenderValue_AG() {
        this.surrenderValue_AG = "" + (Double.parseDouble(getFundValueAtEnd_AD()) - Double.parseDouble(getSurrenderCharges_AE()) - Double.parseDouble(getServiceTaxOnSurrenderCharges_AF()));
    }

    public String getSurrenderValue_AW() {
        return surrenderValue_AW;
    }

    public void setSurrenderValue_AW() {
//	     	System.out.println( getFundValueAtEnd_AT() +"   "+ getSurrenderCharges_AU() +"   "+getServiceTaxOnSurrenderCharges_AV());
        this.surrenderValue_AW = "" + (Double.parseDouble(getFundValueAtEnd_AT()) - Double.parseDouble(getSurrenderCharges_AU()) - Double.parseDouble(getServiceTaxOnSurrenderCharges_AV()));
    }


    public String getTopUpCharges_M() {
        return topUpCharges_M;
    }

    public void setTopUpCharges_M(double topUp) {
        this.topUpCharges_M = cfap.getRoundUp("" + (Double.parseDouble(getTopUpPremium_K()) * topUp));
    }

    public String getTotalChargesReductionYeild_AJ() {
        return totalCharges_AJ;
    }

    public void setTotalChargesReductionYeild_AJ(int ageOfProposer, double effectivePremium, String premFreqMode, boolean PPWBstatus, int premPayingTerm, int PF, int policyTerm, boolean mortalityCharges) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.totalCharges_AJ = "" + (Double.parseDouble(getPolicyAdministrationCharge_R()) + Double.parseDouble(getMortalityAndPPWBChargesReductionYeild_AI()) + Double.parseDouble(getSumAssuredRelatedCharges_P()) + (getPWBcharges(ageOfProposer, effectivePremium, premFreqMode, PPWBstatus, premPayingTerm, PF, mortalityCharges)) + Double.parseDouble(getAccidentCoverCharges_Q()));
        } else {
            this.totalCharges_AJ = "" + 0;
        }
    }

    public String getTotalChargesReductionYeild_T() {
        return totalCharges_T;
    }

    public void setTotalChargesReductionYeild_T(int ageOfProposer, double effectivePremium, String premFreqMode, boolean PPWBstatus, int premPayingTerm, int PF, int policyTerm, boolean mortalityCharges) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.totalCharges_T = "" + (Double.parseDouble(getPolicyAdministrationCharge_R()) + Double.parseDouble(getMortalityAndPPWBChargesReductionYeild_S()) + Double.parseDouble(getSumAssuredRelatedCharges_P()) + (getPWBcharges(ageOfProposer, effectivePremium, premFreqMode, PPWBstatus, premPayingTerm, PF, mortalityCharges)) + Double.parseDouble(getAccidentCoverCharges_Q()));
        } else {
            this.totalCharges_T = "" + 0;
        }
    }

    public String getTotalCharges_AJ() {
        return totalCharges_AJ;
    }

    public void setTotalCharges_AJ(int ageOfProposer, double effectivePremium, String premFreqMode, boolean PPWBstatus, int premPayingTerm, int PF, int policyTerm, boolean mortalityCharges) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.totalCharges_AJ = cfap.getRoundOffLevel2New(cfap.getStringWithout_E(Double.parseDouble(getPolicyAdministrationCharge_R()) + Double.parseDouble(getMortalityAndPPWBCharges_AI()) + Double.parseDouble(getSumAssuredRelatedCharges_P()) + (getPWBcharges(ageOfProposer, effectivePremium, premFreqMode, PPWBstatus, premPayingTerm, PF, mortalityCharges)) + Double.parseDouble(getAccidentCoverCharges_Q())));
        } else {
            this.totalCharges_AJ = "" + 0;
        }
    }

    public String getTotalCharges_T() {
        return totalCharges_T;
    }

    public void setTotalCharges_T(int ageOfProposer, double effectivePremium, String premFreqMode, boolean PPWBstatus, int premPayingTerm, int PF, int policyTerm, boolean mortalityCharges) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.totalCharges_T = cfap.getRoundOffLevel2New(cfap.getStringWithout_E(Double.parseDouble(getPolicyAdministrationCharge_R()) + Double.parseDouble(getMortalityAndPPWBCharges_S()) + Double.parseDouble(getSumAssuredRelatedCharges_P()) + (getPWBcharges(ageOfProposer, effectivePremium, premFreqMode, PPWBstatus, premPayingTerm, PF, mortalityCharges)) + Double.parseDouble(getAccidentCoverCharges_Q())));
        } else {
            this.totalCharges_T = "" + 0;
        }
    }

    public String getTotalServiceTax_AL() {
        return totalServiceTax_AL;
    }

    public void setTotalServiceTax_AL() {
        this.totalServiceTax_AL = (cfap.getStringWithout_E(Double.parseDouble(getServiceTaxOnAllocation_N()) + Double.parseDouble(getTotalServiceTax_exclOfSTonFMC_AK()) + Double.parseDouble(getServiceTaxOnFMC_AP())));
    }

    public String getTotalServiceTaxReductionYeild_AL() {
        return totalServiceTaxReductionYeild_AL;
    }

    public void setTotalServiceTaxReductionYeild_AL() {
        this.totalServiceTaxReductionYeild_AL = cfap.roundUp_Level2(cfap.getStringWithout_E(Double.parseDouble(getServiceTaxOnAllocation_N()) + Double.parseDouble(getTotalServiceTax_exclOfSTonFMC_AK()) + Double.parseDouble(getServiceTaxOnFMCReductionYeild_AP())));
    }


    public String getTotalServiceTax_ExclOfSTonAllocAndsurr_AY() {
        return totalServiceTax_ExclOfSTonAllocAndsurr_AY;
    }

    public void setTotalServiceTax_ExclOfSTonAllocAndsurr_AY(double serviceTax, boolean mortalityAndRiderCharges, boolean administrationAndSArelatedCharges, boolean guaranteeCharges) {
        double a, b, c = 0;
        if (mortalityAndRiderCharges) {
            a = Double.parseDouble(getMortalityAndPPWBCharges_AI()) + Double.parseDouble(getOptionCharges_R());
        } else {
            a = 0;
        }
        if (administrationAndSArelatedCharges) {
            b = Double.parseDouble(getPolicyAdministrationCharge_R()) + Double.parseDouble(getSumAssuredRelatedCharges_P());
        } else {
            b = 0;
        }
        if (guaranteeCharges) {
            c = Double.parseDouble(getGuaranteeCharge_AW());
        } else {
            c = 0;
        }
        this.totalServiceTax_ExclOfSTonAllocAndsurr_AY = cfap.roundUp_Level2(cfap.getStringWithout_E((a + b + c) * serviceTax));
    }

    public String getTotalServiceTax_V() {
        return totalServiceTax_V;
    }

    public void setTotalServiceTax_V(double serviceTax) {

//    	this.totalServiceTax_V =(cfap.getStringWithout_E(Double.parseDouble(getServiceTaxOnAllocation_N())+Double.parseDouble(getTotalServiceTax_exclOfSTonFMC_U())+Double.parseDouble(getServiceTaxOnFMC_Z())));}
    /*System.out.println("serviceTax"+serviceTax);
    System.out.println("getServiceTaxOnAllocation_N"+getServiceTaxOnAllocation_N());
    System.out.println("getTotalServiceTax_exclOfSTonFMC_U"+getTotalServiceTax_exclOfSTonFMC_U());
    System.out.println("getServiceTaxOnFMC_Z"+getServiceTaxOnFMC_Z());*/
        double temp1 = 0, temp2 = 0;
        temp1 = Double.parseDouble(getServiceTaxOnAllocation_N()) + Double.parseDouble(getTotalServiceTax_exclOfSTonFMC_U()) + Double.parseDouble(getServiceTaxOnFMC_Z());
        if (ssProp.riderCharges) {
            temp2 = 0;
        } else {
            temp2 = 0;
        }
        this.totalServiceTax_V = cfap.getRoundOffLevel2(cfap.getStringWithout_E(temp1 + temp2 * serviceTax));

    }


    public String getTotalServiceTaxReductionYeild_V() {
        return totalServiceTaxReductionYeild_V;
    }

    public void setTotalServiceTaxReductionYeild_V() {
        this.totalServiceTaxReductionYeild_V = cfap.roundUp_Level2("" + (Double.parseDouble(getServiceTaxOnAllocation_N()) + Double.parseDouble(getTotalServiceTax_exclOfSTonFMC_U()) + Double.parseDouble(getServiceTaxOnFMCReductionYeild_Z())));
    }


    public String getTotalServiceTax_exclOfSTonFMC_U() {
        return totalServiceTax_exclOfSTonFMC_U;
    }

    public void setTotalServiceTax_exclOfSTonFMC_U(double serviceTax, boolean mortalityAndRiderCharges, boolean administrationCharges, boolean guaranteeCharges, int ageOfProposer, double effectivePremium, String premFreqMode, boolean PPWBstatus, int premPayingTerm, int PF, boolean mortalityCharges) {
        double a = 0, b = 0;
        if (mortalityAndRiderCharges) {
            a = Double.parseDouble(getMortalityAndPPWBCharges_S()) + Double.parseDouble(getAccidentCoverCharges_Q()) + (getPWBcharges(ageOfProposer, effectivePremium, premFreqMode, PPWBstatus, premPayingTerm, PF, mortalityCharges));
        } else {
            a = 0;
        }
        if (administrationCharges) {
            b = Double.parseDouble(getPolicyAdministrationCharge_R());
        } else {
            b = 0;
        }
        this.totalServiceTax_exclOfSTonFMC_U = cfap.getRoundOffLevel2(cfap.getStringWithout_E((a + b) * serviceTax));
    }

    public String getTransferOfCapitalFromIndexToDailyProtectFund_BB() {
        return transferOfCapitalFromIndexToDailyProtectFund_BB;
    }

    public void setTransferOfCapitalFromIndexToDailyProtectFund_BB(double br18) {
        if (getPolicyInForce_G().equals("Y")) {
            this.transferOfCapitalFromIndexToDailyProtectFund_BB = "" + (br18 * Double.parseDouble(getTransferPercentIfAny_O()));
        } else {
            this.transferOfCapitalFromIndexToDailyProtectFund_BB = "" + 0;
        }
    }

    private String getTransferOfCapitalFromIndexToDailyProtectFund_Z() {
        return transferOfCapitalFromIndexToDailyProtectFund_Z;
    }

    public void setTransferOfCapitalFromIndexToDailyProtectFund_Z(double _indexFundAtEnd) {
        if (getPolicyInForce_G().equals("Y")) {
            this.transferOfCapitalFromIndexToDailyProtectFund_Z = "" + (Double.parseDouble(getTransferPercentIfAny_O()) * _indexFundAtEnd);
        }
    }

    private String getTransferOfIndexFundGainToDailyProtectFund_BA() {
        return transferOfIndexFundGainToDailyProtectFund_BA;
    }

    public void setTransferOfIndexFundGainToDailyProtectFund_BA(double _indexFundAtEnd, double thresholdLimitForTransfOfGain, int noOfYrsAllowForTransfOfGain) {
        double a = 0;
        if ((_indexFundAtEnd * (1 - Double.parseDouble(getTransferPercentIfAny_O()))) >= (Double.parseDouble(getAllocatedFundToIndexFund_P()) * (1 + thresholdLimitForTransfOfGain))) {
            a = _indexFundAtEnd - Double.parseDouble(getAllocatedFundToIndexFund_P());
        } else {
            a = 0;
        }
        if (getPolicyInForce_G().equals("Y")) {
            if (Double.parseDouble(getAllocatedFundToIndexFund_P()) > 0 && Integer.parseInt(getMonth_E()) <= noOfYrsAllowForTransfOfGain * 12) {
                this.transferOfIndexFundGainToDailyProtectFund_BA = "" + a;
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

    public void setTransferOfIndexFundGainToDailyProtectFund_Y(double _indexFundAtEnd, int noOfYrsAllowForTransfOfGain, double thresholdLimitForTransfOfGain) {
        if (getPolicyInForce_G().equals("Y")) {
            if (Double.parseDouble(getAllocatedFundToIndexFund_P()) > 0 && Integer.parseInt(getMonth_E()) <= (noOfYrsAllowForTransfOfGain * 12)) {
                if ((_indexFundAtEnd * (1 - Double.parseDouble(getTransferPercentIfAny_O()))) >= (Double.parseDouble(getAllocatedFundToIndexFund_P()) * (1 + thresholdLimitForTransfOfGain))) {
                    this.transferOfIndexFundGainToDailyProtectFund_Y = "" + (_indexFundAtEnd - Double.parseDouble(getAllocatedFundToIndexFund_P()));
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

    public void setTransferPercentIfAny_O(int year_TransferOfCapital_W62, int year_TransferOfCapital_W63, String transferFundStatus) {
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
        } else if (getMonth_E().equals("" + (12 * year_TransferOfCapital_W63 - 6))) {
            if (transferFundStatus.equals("5th Year") || transferFundStatus.equals("No Transfer")) {
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
        this.year_F = cfap.getRoundUp("" + (Double.parseDouble(getMonth_E()) / 12));
    }

    //*******************************************************************************************************

    private double getSurrenderCharge(String premFreqmode, double effectivePremium) {
        double surrenderCharge = 0;
        /*if(!premFreqmode.equals("Single"))
         {surrenderCharge=Double.parseDouble(calSurrRateArr(effectivePremium)[Integer.parseInt(getYear_F())-1]);}
        //For Single Mode of Premium Freqency
        else
        	 Modified by Priyanka Warekar - 20-05-2015
         {surrenderCharge=Double.parseDouble(calSurrRateArrForSingle(effectivePremium)[Integer.parseInt(getYear_F())-1]);
//	         System.out.println(getYear_F()+" surrenderCharge : "+surrenderCharge);
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
            if (effectivePremium > 25000) {

                if (effectivePremium > 50000) {

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

                } else {

                    if (getYear_F().equals("1")) {
                        surrenderCharge = 0.2;
                    } else if (getYear_F().equals("2")) {
                        surrenderCharge = 0.15;
                    } else if (getYear_F().equals("3")) {
                        surrenderCharge = 0.1;
                    } else if (getYear_F().equals("4")) {
                        surrenderCharge = 0.05;
                    } else {
                        surrenderCharge = 0;
                    }
                }

            } else {

                if (getYear_F().equals("1")) {
                    surrenderCharge = 0.2;
                } else if (getYear_F().equals("2")) {
                    surrenderCharge = 0.15;
                } else if (getYear_F().equals("3")) {
                    surrenderCharge = 0.1;
                } else if (getYear_F().equals("4")) {
                    surrenderCharge = 0.05;
                } else {
                    surrenderCharge = 0;
                }
            }
        }

        return surrenderCharge;
    }

    private String[] calSurrRateArrForSingle(double effectivePremium) {
        if (effectivePremium <= 25000) {
            return new String[]{"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"};
        } else {
            return new String[]{"0.01", "0.005", "0.0025", "0.001", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"};
        }
    }

    public int getCharge_PP_Ren(int fixedMonthlyExp_SP, int fixedMonthlyExp_RP, int inflation_pa_SP, int inflation_pa_RP, String premFreqMode) {
        if (premFreqMode.equals("Single")) {
            return (fixedMonthlyExp_SP * 12 * (1 + inflation_pa_SP) ^ 0);
        }
        //For Regular
        else {
            return (fixedMonthlyExp_RP * 12 * (1 + inflation_pa_RP) ^ 0);
        }
    }

    public int getFinYrAfterLaunch(String dateOfInception, String dateOfLaunch) {
        return (getFinYrForCalOfAdminCharges(dateOfInception) - getYrOfDateOfLaunch(dateOfLaunch)) - 1;
    }

    private int getYrOfDateOfLaunch(String dateOfLaunch) {
        String[] dummyArr = cfap.split(dateOfLaunch, "/");
        return Integer.parseInt(dummyArr[2]);
    }

    private int getFinYrForCalOfAdminCharges(String dateOfInception) {
        if (getMonthOfInception(dateOfInception) < 4) {
            return getYearOfInception(dateOfInception);
        } else {
            return getYearOfInception(dateOfInception) + 1;
        }
    }

    private int getMonthOfInception(String dateOfInception) {
        String[] dummyArr = cfap.split(dateOfInception, "/");
        return Integer.parseInt(dummyArr[0]);
    }

    private int getYearOfInception(String dateOfInception) {
        String[] dummyArr = cfap.split(dateOfInception, "/");
        return Integer.parseInt(dummyArr[2]);
    }

    private String[] calSurrRateArr(double effectivePremium) {
        if (effectivePremium <= 25000) {
            return new String[]{"0.20", "0.15", "0.10", "0.05", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"};
        } else {
            return new String[]{"0.06", "0.04", "0.03", "0.02", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"};
        }
    }

    public String[] calCapArr(double effectivePremium) {
        if (effectivePremium <= 25000) {
            return new String[]{"3000", "2000", "1500", "1000", "0", "0", "0", "0", "0", "0", "0"};
        } else {
            return new String[]{"6000", "5000", "4000", "2000", "0", "0", "0", "0", "0", "0", "0"};
        }
    }

    public String[] getForBIArr(double mortalityCharges_AsPercentOfofLICtable) {
        int[] ageArr = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100};
        String[] strArrTReturn = new String[99];
        SmartScholarDB smartScholarDB = new SmartScholarDB();
        for (int i = 0; i < smartScholarDB.getIAIarray().length - 2; i++) {
            strArrTReturn[i] = String.valueOf(((smartScholarDB.getIAIarray()[i] + smartScholarDB.getIAIarray()[i + 1]) / 2) * mortalityCharges_AsPercentOfofLICtable);
//        	strArrTReturn[i]=String.valueOf((smartScholarDB.getIAIarray()[i]) * mortalityCharges_AsPercentOfofLICtable);


        }
        return strArrTReturn;
    }

    private double getAllocationCharges(boolean staffDisc, boolean bancAssuranceDisc, String premFreqmode) {
        double temp1 = 0;
        double temp2 = 0;
        //For Single Frequency Mode
        if (premFreqmode.equals("Single")) {
            if (getYear_F().equals("1")) {
                if (staffDisc || bancAssuranceDisc) {
                    temp2 = ssProp.allocCommisionSingleYr1;
                } else {
                    temp2 = 0;
                }
                temp1 = ssProp.allocNormalSingleYr1;
            } else if (getYear_F().equals("2")) {
                if (staffDisc) {
                    temp2 = ssProp.allocCommisionSingleYr2;
                } else {
                    temp2 = 0;
                }
                temp1 = ssProp.allocNormalSingleYr2;
            } else if (getYear_F().equals("3")) {
                if (staffDisc) {
                    temp2 = ssProp.allocCommisionSingleYr3;
                } else {
                    temp2 = 0;
                }
                temp1 = ssProp.allocNormalSingleYr3;
            } else if (getYear_F().equals("4")) {
                if (staffDisc) {
                    temp2 = ssProp.allocCommisionSingleYr4;
                } else {
                    temp2 = 0;
                }
                temp1 = ssProp.allocNormalSingleYr4;
            } else if (getYear_F().equals("5")) {
                if (staffDisc) {
                    temp2 = ssProp.allocCommisionSingleYr5;
                } else {
                    temp2 = 0;
                }
                temp1 = ssProp.allocNormalSingleYr5;
            } else if (getYear_F().equals("6")) {
                if (staffDisc) {
                    temp2 = ssProp.allocCommisionSingleYr6;
                } else {
                    temp2 = 0;
                }
                temp1 = ssProp.allocNormalSingleYr6;
            } else if (getYear_F().equals("7")) {
                if (staffDisc) {
                    temp2 = ssProp.allocCommisionSingleYr7;
                } else {
                    temp2 = 0;
                }
                temp1 = ssProp.allocNormalSingleYr7;
            } else if (getYear_F().equals("8")) {
                if (staffDisc) {
                    temp2 = ssProp.allocCommisionSingleYr8;
                } else {
                    temp2 = 0;
                }
                temp1 = ssProp.allocNormalSingleYr8;
            } else if (getYear_F().equals("9")) {
                if (staffDisc) {
                    temp2 = ssProp.allocCommisionSingleYr9;
                } else {
                    temp2 = 0;
                }
                temp1 = ssProp.allocNormalSingleYr9;
            } else if (getYear_F().equals("10")) {
                if (staffDisc) {
                    temp2 = ssProp.allocCommisionSingleYr10;
                } else {
                    temp2 = 0;
                }
                temp1 = ssProp.allocNormalSingleYr10;
            }
            return temp1 - temp2;
        }
        //For Limited Premium Frequency
        else {
            if (getYear_F().equals("1")) {
                if (staffDisc || bancAssuranceDisc) {
                    temp2 = ssProp.allocCommisionLPPTYr1;
                } else {
                    temp2 = 0;
                }
                temp1 = ssProp.allocNormalLPPTYr1;
            } else if (getYear_F().equals("2")) {
                if (staffDisc) {
                    temp2 = ssProp.allocCommisionLPPTYr2;
                } else {
                    temp2 = 0;
                }
                temp1 = ssProp.allocNormalLPPTYr2;
            } else if (getYear_F().equals("3")) {
                if (staffDisc) {
                    temp2 = ssProp.allocCommisionLPPTYr3;
                } else {
                    temp2 = 0;
                }
                temp1 = ssProp.allocNormalLPPTYr3;
            } else if (getYear_F().equals("4")) {
                if (staffDisc) {
                    temp2 = ssProp.allocCommisionLPPTYr4;
                } else {
                    temp2 = 0;
                }
                temp1 = ssProp.allocNormalLPPTYr4;
            } else if (getYear_F().equals("5")) {
                if (staffDisc) {
                    temp2 = ssProp.allocCommisionLPPTYr5;
                } else {
                    temp2 = 0;
                }
                temp1 = ssProp.allocNormalLPPTYr5;
            } else if (getYear_F().equals("6")) {
                if (staffDisc) {
                    temp2 = ssProp.allocCommisionLPPTYr6;
                } else {
                    temp2 = 0;
                }
                temp1 = ssProp.allocNormalLPPTYr6;
            } else if (getYear_F().equals("7")) {
                if (staffDisc) {
                    temp2 = ssProp.allocCommisionLPPTYr7;
                } else {
                    temp2 = 0;
                }
                temp1 = ssProp.allocNormalLPPTYr7;
            } else if (getYear_F().equals("8")) {
                if (staffDisc) {
                    temp2 = ssProp.allocCommisionLPPTYr8;
                } else {
                    temp2 = 0;
                }
                temp1 = ssProp.allocNormalLPPTYr8;
            } else if (getYear_F().equals("9")) {
                if (staffDisc) {
                    temp2 = ssProp.allocCommisionLPPTYr9;
                } else {
                    temp2 = 0;
                }
                temp1 = ssProp.allocNormalLPPTYr9;
            } else if (getYear_F().equals("10")) {
                if (staffDisc) {
                    temp2 = ssProp.allocCommisionLPPTYr10;
                } else {
                    temp2 = 0;
                }
                temp1 = ssProp.allocNormalLPPTYr10;
            }


            return temp1 - temp2;
        }
    }


    private double getADBoptionPremiumCharge(String premFreqMode, double sumAssured) {
        double temp = 0;
        if (policyInForce_G.equals("Y")) {
            temp = ssProp.accidentalBenCharge / 12;
        } else {
            temp = 0;
        }
        double temp0 = 0;
        if (ssProp.accidentalBenefit) {
            temp0 = getAccidentBenefitSA(premFreqMode, sumAssured);
        }
        double temp1 = (temp0 / 1000);
        double temp2 = 0;
        if (premFreqMode.equals("Single")) {
            temp2 = 0;
        } else {
            temp2 = temp;
        }
        if (policyInForce_G.equals("Y")) {
            return temp1 * temp2;
        } else {
            return 0;
        }
    }

    public double getAccidentBenefitSA(String premFreqMode, double sumAssured) {
        if (premFreqMode.equals("Single")) {
            return 0;
        } else {
            return Math.min(sumAssured, 5000000.0);
        }
    }

    /******** Added by Akshaya on 19-MAR-15 start*/
    public double getPpwbSA(String premFreqMode, double premiumAmt) {
        if (premFreqMode.equals("Single")) {
            return 0;
        } else if (premFreqMode.equals("Yearly")) {
            return premiumAmt;
        } else if (premFreqMode.equals("Half Yearly")) {
            return premiumAmt * 2;
        } else if (premFreqMode.equals("Quarterly")) {
            return premiumAmt * 4;
        } else if (premFreqMode.equals("Monthly")) {
            return premiumAmt * 12;
        } else {
            return 0;
        }
    }

    /******** Added by Akshaya on 19-MAR-15 end*/
    /*public double getPWBcharges(int ageOfProposer,double effectivePremium,String premFreqMode,boolean PPWBstatus,int premPayingTerm,int PF)
     {
         if(PPWBstatus)
         {
             if(premFreqMode.equals("Single"))
             {return 0;}
             else
             {
                 if(Integer.parseInt(getMonth_E()) > premPayingTerm*12-12/PF)
                 {return 0;}
                 else
                 {
                    double temp1, temp2 = 0;
                    //temp1=INDEX(PWB_Table,Proposer_Age-17,PPT-2)
                    temp1 = getValFromPWBtable(ageOfProposer, premPayingTerm);
                    temp2 = effectivePremium / 1000;
                    return temp1 / 12 * temp2;
                }
            }
        }
         else
         {return 0;}
    }*/
    public double getPWBcharges(int ageOfProposer, double effectivePremium, String premFreqMode, boolean PPWBstatus, int premPayingTerm, int PF, boolean mortalityCharges) {
        double ppwbCharge = 0;
        double a = 0;
        if (mortalityCharges == true)
            a = 1;
        else
            a = 0;

        double temp = 0;

        if (Integer.parseInt(getYear_F()) != 0 && Integer.parseInt(getMonth_E()) <= (premPayingTerm * 12))
            temp = (getValFromPWBtable(ageOfProposer, premPayingTerm)) / 12 * effectivePremium / 1000;
        else
            temp = 0;

        return (temp * a);
//		System.out.println("PPWBCharge_O + "+PPWBCharge_O);

    }

    private double getValFromPWBtable(int ageOfProposer, int premPayingTerm) {
        SmartScholarDB smartScholarDB = new SmartScholarDB();
        double[] PWBarr = smartScholarDB.getPWBtableArray();
        int position = 0;
        int premiumPerOneLac = 0;
        for (int i = 18; i <= 57; i++) {
            for (int j = 3; j <= 25; j++) {
                if ((ageOfProposer) == i && (premPayingTerm) == j) {
                    ////System.out.println("getValFromPWBtable() -> "+ PWBarr[position]);
                    return PWBarr[position];
                }
                position++;
            }
        }
        return 0;
    }

    private double getMaxSurrender(String premFreqMode, double effectivePremium) {
        if (premFreqMode.equals("Single")) {
            if (effectivePremium <= ssProp.premLimitForSurrCharge) {
                return 0;
            } else {
                if (getYear_F().equals("1")) {
                    return ssProp.surrPremGreaterThan25000Yr1;
                } else if (getYear_F().equals("2")) {
                    return ssProp.surrPremGreaterThan25000Yr2;
                } else if (getYear_F().equals("3")) {
                    return ssProp.surrPremGreaterThan25000Yr3;
                } else if (getYear_F().equals("4")) {
                    return ssProp.surrPremGreaterThan25000Yr4;
                } else if (getYear_F().equals("5")) {
                    return ssProp.surrPremGreaterThan25000Yr5;
                } else if (getYear_F().equals("6")) {
                    return ssProp.surrPremGreaterThan25000Yr6;
                } else if (getYear_F().equals("7")) {
                    return ssProp.surrPremGreaterThan25000Yr7;
                } else if (getYear_F().equals("8")) {
                    return ssProp.surrPremGreaterThan25000Yr8;
                } else if (getYear_F().equals("9")) {
                    return ssProp.surrPremGreaterThan25000Yr9;
                } else if (getYear_F().equals("10")) {
                    return ssProp.surrPremGreaterThan25000Yr10;
                } else {
                    return 0;
                }
            }
        } else {
            if (effectivePremium <= ssProp.premLimitForSurrCharge) {
                if (getYear_F().equals("1")) {
                    return ssProp.surrPremLessThanOrEqTo25000Yr1;
                } else if (getYear_F().equals("2")) {
                    return ssProp.surrPremLessThanOrEqTo25000Yr2;
                } else if (getYear_F().equals("3")) {
                    return ssProp.surrPremLessThanOrEqTo25000Yr3;
                } else if (getYear_F().equals("4")) {
                    return ssProp.surrPremLessThanOrEqTo25000Yr4;
                } else if (getYear_F().equals("5")) {
                    return ssProp.surrPremLessThanOrEqTo25000Yr5;
                } else if (getYear_F().equals("6")) {
                    return ssProp.surrPremLessThanOrEqTo25000Yr6;
                } else if (getYear_F().equals("7")) {
                    return ssProp.surrPremLessThanOrEqTo25000Yr7;
                } else if (getYear_F().equals("8")) {
                    return ssProp.surrPremLessThanOrEqTo25000Yr8;
                } else if (getYear_F().equals("9")) {
                    return ssProp.surrPremLessThanOrEqTo25000Yr9;
                } else if (getYear_F().equals("10")) {
                    return ssProp.surrPremLessThanOrEqTo25000Yr10;
                } else {
                    return 0;
                }
            } else {
                if (getYear_F().equals("1")) {
                    return ssProp.surrPremGreaterThan25000Yr1;
                } else if (getYear_F().equals("2")) {
                    return ssProp.surrPremGreaterThan25000Yr2;
                } else if (getYear_F().equals("3")) {
                    return ssProp.surrPremGreaterThan25000Yr3;
                } else if (getYear_F().equals("4")) {
                    return ssProp.surrPremGreaterThan25000Yr4;
                } else if (getYear_F().equals("5")) {
                    return ssProp.surrPremGreaterThan25000Yr5;
                } else if (getYear_F().equals("6")) {
                    return ssProp.surrPremGreaterThan25000Yr6;
                } else if (getYear_F().equals("7")) {
                    return ssProp.surrPremGreaterThan25000Yr7;
                } else if (getYear_F().equals("8")) {
                    return ssProp.surrPremGreaterThan25000Yr8;
                } else if (getYear_F().equals("9")) {
                    return ssProp.surrPremGreaterThan25000Yr9;
                } else if (getYear_F().equals("10")) {
                    return ssProp.surrPremGreaterThan25000Yr10;
                } else {
                    return 0;
                }
            }
        }
    }


    public double getCharge_fund_ren(double percentToBeInvested_EquityFund, double percentToBeInvested_EquityOptimiserFund, double percentToBeInvested_GrowthFund, double percentToBeInvested_BalancedFund, double percentToBeInvested_BondFund, double percentToBeInvested_MoneyMarketFund, double percentToBeInvested_IndexFund, double percentToBeInvested_Top300Fund, double percentToBeInvested_PEmanagedFund) {
        if (plan.equalsIgnoreCase("Plan-B")) {

            return percentToBeInvested_EquityFund * ssProp.FMC_EquityFund +
                    percentToBeInvested_EquityOptimiserFund * ssProp.FMC_EquityOptimiserFund +
                    percentToBeInvested_GrowthFund * ssProp.FMC_GrowthFund +
                    percentToBeInvested_BalancedFund * ssProp.FMC_BalancedFund +
                    percentToBeInvested_BondFund * ssProp.FMC_BondFund +
                    percentToBeInvested_MoneyMarketFund * ssProp.FMC_MoneyMarketFund +
                    percentToBeInvested_IndexFund * ssProp.FMC_BondOptimiserFund +
                    percentToBeInvested_Top300Fund * ssProp.FMC_Top300Fund +
                    percentToBeInvested_PEmanagedFund * ssProp.FMC_PureFund;

        } else {
            return percentToBeInvested_EquityFund * ssProp.FMC_EquityFund +
                    percentToBeInvested_EquityOptimiserFund * ssProp.FMC_EquityOptimiserFund +
                    percentToBeInvested_GrowthFund * ssProp.FMC_GrowthFund +
                    percentToBeInvested_BalancedFund * ssProp.FMC_BalancedFund +
                    percentToBeInvested_BondFund * ssProp.FMC_BondFund +
                    percentToBeInvested_MoneyMarketFund * ssProp.FMC_MoneyMarketFund +
                    percentToBeInvested_IndexFund * ssProp.FMC_BondOptimiserFundII +
                    percentToBeInvested_Top300Fund * ssProp.FMC_Top300Fund +
                    percentToBeInvested_PEmanagedFund * ssProp.FMC_PureFund;
        }
    }


    public void setReductionYield_BB(int policyTerm, double _fundValueAtEnd_AD) {
        double a, b;
        //     if((Integer.parseInt(getMonth_E())) <= (noOfYearsElapsedSinceInception*12)+1)
        if ((Integer.parseInt(getMonth_BA())) < (policyTerm * 12)) {
            a = -(Double.parseDouble(getPremium_J()));
        } else {
            a = 0;
        }

        if ((Integer.parseInt(getMonth_BA())) == (policyTerm * 12)) {
            b = _fundValueAtEnd_AD;
        } else {
            b = 0;
        }
        //   System.out.println("a_BB "+a);
        //   System.out.println("b_BB "+b);
        this.reductionYield_BB = "" + (a + b);
    }

    public String getReductionYield_BB() {
        return reductionYield_BB;
    }


    public void setReductionYield_BC(int policyTerm, double _fundValueAtEnd_AT) {
        double a, b;
        //     if((Integer.parseInt(getMonth_E())) <= (noOfYearsElapsedSinceInception*12)+1)
        if ((Integer.parseInt(getMonth_BA())) < (policyTerm * 12)) {
            a = -(Double.parseDouble(getPremium_J()));
        } else {
            a = 0;
        }

        if ((Integer.parseInt(getMonth_BA())) == (policyTerm * 12)) {
            b = _fundValueAtEnd_AT;
        } else {
            b = 0;
        }
        //   System.out.println("a_BC "+a);
        //   System.out.println("b_BC "+b);
        this.reductionYield_BC = "" + (a + b);
    }

    public String getReductionYield_BC() {
        return reductionYield_BC;
    }


    public void setMonth_BA(int monthNumber) {
        this.month_BA = "" + monthNumber;
    }

    public String getMonth_BA() {
        return month_BA;
    }


    public void setIRRAnnual_BB(double ans) {
//	     	System.out.println("aaaaaaa "+((comm.pow((1+ans),12)-1)*100));
        this.irrAnnual_BB = "" + ((cfap.pow((1 + ans), 12) - 1));
    }

    public String getIRRAnnual_BB() {
        return irrAnnual_BB;
    }


    public void setIRRAnnual_BC(double ans) {
        //System.out.println("aaaaaaa "+((comm.pow((1+ans),12)-1)*100));
        this.irrAnnual_BC = "" + ((cfap.pow((1 + ans), 12) - 1));
    }

    public String getIRRAnnual_BC() {
        return irrAnnual_BC;
    }

    public void setIRRAnnual_BG(double ans) {
        //System.out.println("aaaaaaa "+((comm.pow((1+ans),12)-1)*100));
        this.irrAnnual_BG = "" + ((cfap.pow((1 + ans), 12) - 1));
    }

    public String getIRRAnnual_BG() {
        return irrAnnual_BG;
    }

    public void setReductionInYieldMaturityAt(double int2) {
        this.reductionInYieldMaturityAt = "" + ((int2 - Double.parseDouble(getIRRAnnual_BC())) * 100);
    }

    public String getReductionInYieldMaturityAt() {
        return reductionInYieldMaturityAt;
    }

    public void setReductionInYieldNumberOfYearsElapsedSinceInception(double int2) {
        this.reductionInYieldNumberOfYearsElapsedSinceInception = "" + ((int2 - Double.parseDouble(getIRRAnnual_BG())) * 100);
    }

    public String getReductionInYieldNumberOfYearsElapsedSinceInception() {
        return reductionInYieldNumberOfYearsElapsedSinceInception;
    }

    public String getnetYieldAt4Percent() {
        return netYieldAt4Percent;
    }

    public void setnetYieldAt4Percent() {
        this.netYieldAt4Percent = "" + Double.parseDouble(getIRRAnnual_BB()) * 100;
    }

    public String getnetYieldAt8Percent() {
        return netYieldAt8Percent;
    }

    public void setnetYieldAt8Percent() {
        this.netYieldAt8Percent = "" + Double.parseDouble(getIRRAnnual_BC()) * 100;
    }

    public void setReductionYield_BG(int noOfYearsElapsedSinceInception, double _fundValueAtEnd_AT) {
        double a, b;
        //     if((Integer.parseInt(getMonth_E())) <= (noOfYearsElapsedSinceInception*12)+1)
        if ((Integer.parseInt(getMonth_BA())) < (noOfYearsElapsedSinceInception * 12)) {
            a = -(Double.parseDouble(getPremium_J()));
        } else {
            a = 0;
        }

        if ((Integer.parseInt(getMonth_BA())) == (noOfYearsElapsedSinceInception * 12)) {
            b = _fundValueAtEnd_AT;
        } else {
            b = 0;
        }
        // System.out.println("a_BU "+a);
        // System.out.println("b_BU "+b);
        this.reductionYield_BG = "" + (a + b);
    }

    public String getReductionYield_BG() {
        return reductionYield_BG;
    }


    public double irr(ArrayList<String> values, double guess) {
        int maxIterationCount = 20;
        double absoluteAccuracy = 1E-7;
        double[] arr = new double[values.size()];
        double x0 = guess;
        double x1;

        int i = 0;
//	           System.out.println("inside irr ");
        //System.out.println("values "+values);
        while (i < maxIterationCount) {

            // the value of the function (NPV) and its derivate can be calculated in the same loop
            double fValue = 0;
            double fDerivative = 0;
            for (int k = 0; k < values.size(); k++) {
                //System.out.println("value "+Double.parseDouble(values.get(k)));
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
