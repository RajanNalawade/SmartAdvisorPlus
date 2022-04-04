package sbilife.com.pointofsale_bancaagency.smartwealthassure;

import java.util.ArrayList;

import sbilife.com.pointofsale_bancaagency.common.CommonForAllProd;

class SmartWealthAssureBusinessLogic {

    private CommonForAllProd cfap = null;
    private SmartWealthAssureProperties swap = null;
    private String month_E = null;
    private String year_F = null;
    private String policyInForce_G = "Y";
    private String age_H = null;
    private String premium_I = null;
    private String TopUpPremium_J = null;
    private String premiumAllocationCharge_K = null;
    private String topUpCharges_L = null;
    private String serviceTaxOnAllocation_M = null;
    private String AmountAvailableForInvestment_N = null, AmountAvailableForInvestment_N1 = null;
    private String transferPercentIfAny_O = null;
    private String allocatedFundToFundsUnderMarketLinkedReturn_P = null;
    private String sumAssuredRelatedCharges_Q = null;
    private String optionCharges_R = null;
    private String policyAdministrationCharge_S = null;
    private String mortalityCharges_T = null;
    private String guaranteeCharge_U = null;
    private String totalCharges_V = null;
    private String totalServiceTax_exclOfSTonAllocAndSurr_W = null;
    private String totalServiceTax_X = null;
    private String transferOfFundFromRGFtoMarketLinkedFunds_Y = null;
    private String transferOfFundFromRGFtoMarketLinkedFunds_AZ = null;
    private String guaranteedAddition_AM = null;
    private String guaranteedAddition_BN = null;
    private String fundUnderRGFatEnd_AN = null;
    private String fundsUnderMarketLinkedReturnAtEnd_AO = null;
    private String fundsUnderMarketLinkedReturnAtEnd_BP = null;
    private String fundUnderRGFatEnd_BO = null;
    private String fundUnderRGFatStart_Z = null;
    private String fundUnderRGFatStart_BA = null;
    private String fundsUnderMarketLinkedReturnAtStart_AA = null;
    private String fundsUnderMarketLinkedReturnAtStart_BB = null;
    private String fundUnderRGFafterCharges_AB = null;
    private String fundsUnderMarketLinkedReturnAtStartAfterCharges_AC = null;
    private String fundsUnderMarketLinkedReturnAtStartAfterCharges_BD = null;
    private String fundUnderRGFafterCharges_BC = null;
    private String fundValueAtEnd_AP = null;
    private String fundValueAtEnd_BQ = null;
    private String transferOfCapitalFromIndexToDailyProtectFund_Z = null;
    private String dailyProtectFundAtStart_AA = null;
    private String indexFundAtStart_AB = null;
    private String dailyProtectFundAfterCharges_AC = null;
    private String indexFundAtStartAfterCharges_AD = null;
    private String additionToDailyProtectFund_AE = null;
    private String additionToIndexFund_AF = null;
    private String fMCearnedOnFundUnderRGF_AF = null;
    private String fMCearnedOnFundUnderRGF_BG = null;
    private String fMCearnedOnFundsUnderMarketLinkedReturn_AG = null;
    private String fMCearnedOnFundsUnderMarketLinkedReturn_BH = null;
    private String additionToFundUnderRGF_AD = null;
    private String additionToFundsUnderMarketLinkedReturn_AE = null;
    private String additionToFundsUnderMarketLinkedReturn_BF = null;
    private String additionToFundUnderRGF_BE = null;
    private String fundBeforeFMC_AI = null;
    private String fundManagementCharge_AJ = null;
    private String serviceTaxOnFMC_AK = null;
    private String fundValueAfterFMCandBeforeGA_AL = null;
    private String guaranteedAddition_AN = null;
    private String dailyProtectFundAtEnd_AO = null;
    private String indexFundAtEnd_AP = null;
    private String fundValueAtEnd_AQ = null;
    private String surrenderCharges_AQ = null;
    private String surrenderCharges_BR = null;
    private String serviceTaxOnSurrenderCharges_AR = null;
    private String serviceTaxOnSurrenderCharges_BS = null;
    private String surrenderValue_AS = null;
    private String surrenderValue_BT = null;
    private String deathBenefit_AT = null;
    private String mortalityCharges_AU = null;
    private String guaranteeCharge_AV = null;
    private String totalCharges_AW = null;
    private String totalServiceTax_ExclOfSTonAllocAndsurr_AX = null;
    private String totalServiceTax_AY = null;
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
    private String additionToFundIfAny_AH = null;
    private String additionToFundIfAny_BI = null;
    private String fundBeforeFMC_BJ = null;
    private String fundManagementCharge_BK = null;
    private String serviceTaxOnFMC_BL = null;
    private String fundValueAfterFMCandBeforeGA_BM = null;
    private String guaranteedAddition_BP = null;
    private String dailyProtectFundAtend_BQ = null;
    private String indexFundAtEnd_BR = null;
    private String fundValueAtEnd_BS = null;
    private String surrenderCharges_BT = null;
    private String serviceTaxOnSurrenderCharges_BU = null;
    private String surrenderValue_BV = null;
    private String deathBenefit_BU = null;
    private String surrenderCap_BV = null;
    private String oneHundredPercentOfCummulativePremium_BW = null;
    private String mortalityChargesReductionYield_T = null;
    private String totalChargesReductionYield_V = null;
    private String fundUnderRGFafterChargesReductionYield_AB = null;
    private String totalServiceTax_exclOfSTonAllocAndSurrRedutionYield_W = null;
    private String fundsUnderMarketLinkedReturnAtStartAfterChargesReductionYield_AC = null;
    private String additionToFundUnderRGFReductionYield_AD = null;
    private String additionToFundsUnderMarketLinkedReturnReductionYield_AE = null;
    private String additionToFundIfAnyReductionYield_AH = null;
    private String fMCearnedOnFundUnderRGFReductionYield_AF = null;
    private String fundUnderRGFatEndReductionYield_AN = null;
    private String fMCearnedOnFundsUnderMarketLinkedReturnReductionYield_AG = null;
    private String fundsUnderMarketLinkedReturnAtEndReductionYield_AO = null;
    private String fundValueAtEndReductionYield_AP = null;
    private String fundBeforeFMCReductionYield_AI = null;
    private String fundManagementChargeReductionYield_AJ = null;
    private String serviceTaxOnFMCReductionYield_AK = null;
    private String totalServiceTaxReductionYield_X = null;
    private String fundValueAfterFMCandBeforeGAReductionYield_AL = null;
    private String surrenderChargesReductionYield_AQ = null;
    private String serviceTaxOnSurrenderChargesReductionYield_AR = null;
    private String reductionYield_CI = null;
    private String reductionYield_CC = null;
    private String reductionYield_CD = null;
    private String month_CB = null;
    private String irrAnnual_CC = null;
    private String irrAnnual_CD = null;
    private String irrAnnual_CI = null;
    private String reductionInYieldMaturityAt = null;
    private String reductionInYieldNumberOfYearsElapsedSinceInception = null;
    private String netYieldAt4Percent = null;
    private String netYieldAt8Percent = null;
    private String fundValueAtEndReductionYield_BQ = null;
    private String serviceTaxOnSurrenderChargesReductionYield_BS = null;
    private String surrenderChargesReductionYield_BR = null;
    private String fundValueAfterFMCandBeforeGAReductionYield_BM = null;
    private String totalServiceTaxReductionYield_AY = null;
    private String serviceTaxOnFMCReductionYield_BL = null;
    private String fundManagementChargeReductionYield_BK = null;
    private String fundBeforeFMCReductionYield_BJ = null;
    private String fundsUnderMarketLinkedReturnAtEndReductionYield_BP = null;
    private String fMCearnedOnFundsUnderMarketLinkedReturnReductionYield_BH = null;
    private String fundUnderRGFatEndReductionYield_BO = null;
    private String fMCearnedOnFundUnderRGFReductionYield_BG = null;
    private String additionToFundIfAnyReductionYield_BI = null;
    private String additionToFundsUnderMarketLinkedReturnReductionYield_BF = null;
    private String additionToFundUnderRGFReductionYield_BE = null;
    private String fundsUnderMarketLinkedReturnAtStartAfterChargesReductionYield_BD = null;
    private String fundUnderRGFafterChargesReductionYield_BC = null;
    private String totalServiceTax_ExclOfSTonAllocAndsurrReductionYield_AX = null;
    private String totalChargesReductionYield_AW = null;
    private String mortalityChargesReductionYield_AU = null;
    // Variable related to Format Sheet
    private String fundValueAfterFMCAndBeforeGA_V34 = null;

    /************************************************* BL for Reduction Yield Ends here *********************************************************/


    //Constructor[Initialization of object required to access general calculation methods is done here in constructor]
    public SmartWealthAssureBusinessLogic() {
        cfap = new CommonForAllProd();
        swap = new SmartWealthAssureProperties();
    }

    // *********************************************************************
    public String getFundValueAfterFMCAndBeforeGA_V34() {
        return fundValueAfterFMCAndBeforeGA_V34;
    }

    public void setFundValueAfterFMCAndBeforeGA_V34() {
        this.fundValueAfterFMCAndBeforeGA_V34 = getFundValueAfterFMCandBeforeGA_AL();
    }

    // *********************************************************************

    public String getAmountAvailableForInvestment_N() {
        return AmountAvailableForInvestment_N;
    }

    public void setAmountAvailableForInvestment_N() {
        this.AmountAvailableForInvestment_N = cfap.roundUp_Level2(cfap.getStringWithout_E(Double.parseDouble("" + (Double.parseDouble(getPremium_I()) + Double.parseDouble(getTopUpPremium_J()) - Double.parseDouble(getPremiumAllocationCharge_K()) - Double.parseDouble(getTopUpCharges_L()) - Double.parseDouble(getServiceTaxOnAllocation_M())))));
    }

    public String getAmountAvailableForInvestment_N1() {
        return AmountAvailableForInvestment_N1;
    }

    public void setAmountAvailableForInvestment_N1() {
        this.AmountAvailableForInvestment_N1 = cfap.roundUp_Level2(cfap.getStringWithout_E(Double.parseDouble("" + (Double.parseDouble(getPremium_I()) + Double.parseDouble(getTopUpPremium_J()) - Double.parseDouble(getPremiumAllocationCharge_K()) - Double.parseDouble(getTopUpCharges_L())))));
    }

    public String getFMCearnedOnFundUnderRGF_AF() {
        return fMCearnedOnFundUnderRGF_AF;
    }

    public void setFMCearnedOnFundUnderRGF_AF(int policyTerm,
                                              double charge_Fund, double charge_Fund_Ren) {
        // Round Level 2
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            double temp1 = 0;
            if (Integer.parseInt(getMonth_E()) == 1) {
                temp1 = charge_Fund;
            } else {
                temp1 = 0;
            }
            this.fMCearnedOnFundUnderRGF_AF = cfap
                    .roundUp_Level2(cfap.getStringWithout_E((Double
                            .parseDouble(getFundUnderRGFafterCharges_AB()) + Double
                            .parseDouble(getAdditionToFundUnderRGF_AD()))
                            * (temp1 + charge_Fund_Ren / 12)));
        } else {
            this.fMCearnedOnFundUnderRGF_AF = "" + 0;
        }
    }

    public String getFMCearnedOnFundUnderRGF_BG() {
        return fMCearnedOnFundUnderRGF_BG;
    }

    public void setFMCearnedOnFundUnderRGF_BG(int policyTerm,
                                              double charge_Fund, double charge_Fund_Ren) {
        // Round Level 2
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            double temp1 = 0;
            if (Integer.parseInt(getMonth_E()) == 1) {
                temp1 = charge_Fund;
            } else {
                temp1 = 0;
            }

            this.fMCearnedOnFundUnderRGF_BG = cfap
                    .roundUp_Level2(cfap.getStringWithout_E((Double
                            .parseDouble(getFundUnderRGFafterCharges_BC()) + Double
                            .parseDouble(getAdditionToFundUnderRGF_BE()))
                            * (temp1 + charge_Fund_Ren / 12)));
        } else {
            this.fMCearnedOnFundUnderRGF_BG = "" + 0;
        }
    }

    public String getFMCearnedOnDailyProtectFund_BI() {
        return FMCearnedOnDailyProtectFund_BI;
    }

    public void setFMCearnedOnDailyProtectFund_BI(int policyTerm,
                                                  double charge_Fund, double charge_Fund_Ren) {
        double a = 0;
        if (Double.parseDouble(getAdditionToFundUnderRGF_AD()) == 1.0) {
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

    public String getFMCearnedOnFundsUnderMarketLinkedReturn_AG() {
        return fMCearnedOnFundsUnderMarketLinkedReturn_AG;
    }

    public void setFMCearnedOnFundsUnderMarketLinkedReturn_AG(int policyTerm, double charge_Fund, double returnGuaranteeFund, double bondFund, double equityFund, double PEmanagedFund, double percentToBeInvestedForBondFund, double percentToBeInvestedForEquityFund,
                                                              double percentToBeInvested_BalancedFund,
                                                              double percentToBeInvested_BondOptimiserFund,
                                                              double percentToBeInvested_MoneyMarketFund,
                                                              double percentToBeInvested_CorpBondFund,
                                                              double percentToBeInvested_PureFund,
                                                              double FMC_BalancedFund, double FMC_BondOptimiserFund, double FMC_MoneyMarketFund, double FMC_CorpBondFund, double FMC_PureFund) {
        double temp1 = 0, temp2 = 0;
        if (Integer.parseInt(getMonth_E()) == 1) {
            temp1 = charge_Fund;
        } else {
            temp1 = 0;
        }
        if (Integer.parseInt(getYear_F()) <= 10) {
//		        	temp2=getSumProduct1(bondFund,equityFund,PEmanagedFund,percentToBeInvestedForBondFund, percentToBeInvestedForEquityFund,percentToBeInvestedForPEmanagedFund);
            temp2 = getSumProduct1(bondFund, equityFund, percentToBeInvestedForBondFund, percentToBeInvestedForEquityFund, percentToBeInvested_BalancedFund,
                    percentToBeInvested_BondOptimiserFund,
                    percentToBeInvested_MoneyMarketFund,
                    percentToBeInvested_CorpBondFund,
                    percentToBeInvested_PureFund,
                    FMC_BalancedFund, FMC_BondOptimiserFund, FMC_MoneyMarketFund, FMC_CorpBondFund, FMC_PureFund);


        } else {
//		        	temp2=getSumProduct2(returnGuaranteeFund,bondFund,equityFund,PEmanagedFund,percentToBeInvestedForReturnGuaranteedFund,percentToBeInvestedForBondFund, percentToBeInvestedForEquityFund,percentToBeInvestedForPEmanagedFund);
            temp2 = getSumProduct2(bondFund, equityFund, percentToBeInvestedForBondFund, percentToBeInvestedForEquityFund, percentToBeInvested_BalancedFund,
                    percentToBeInvested_BondOptimiserFund,
                    percentToBeInvested_MoneyMarketFund,
                    percentToBeInvested_CorpBondFund,
                    percentToBeInvested_PureFund,
                    FMC_BalancedFund, FMC_BondOptimiserFund, FMC_MoneyMarketFund, FMC_CorpBondFund, FMC_PureFund);

        }

        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.fMCearnedOnFundsUnderMarketLinkedReturn_AG = cfap
                    .roundUp_Level2(cfap.getStringWithout_E((Double
                            .parseDouble(getFundsUnderMarketLinkedReturnAtStartAfterCharges_AC()) + Double
                            .parseDouble(getAdditionToFundsUnderMarketLinkedReturn_AE()))
                            * (temp1 + temp2 / 12)));
        } else {
            this.fMCearnedOnFundsUnderMarketLinkedReturn_AG = "" + 0;
        }
    }

    public String getFMCearnedOnFundsUnderMarketLinkedReturn_BH() {
        return fMCearnedOnFundsUnderMarketLinkedReturn_BH;
    }

    public void setFMCearnedOnFundsUnderMarketLinkedReturn_BH(int policyTerm, double charge_Fund, double returnGuaranteeFund, double bondFund, double equityFund, double PEmanagedFund, double percentToBeInvestedForReturnGuaranteedFund, double percentToBeInvestedForBondFund, double percentToBeInvestedForEquityFund, double percentToBeInvestedForPEmanagedFund,
                                                              double percentToBeInvested_BalancedFund,
                                                              double percentToBeInvested_BondOptimiserFund,
                                                              double percentToBeInvested_MoneyMarketFund,
                                                              double percentToBeInvested_CorpBondFund,
                                                              double percentToBeInvested_PureFund,
                                                              double FMC_BalancedFund, double FMC_BondOptimiserFund, double FMC_MoneyMarketFund, double FMC_CorpBondFund, double FMC_PureFund) {
        double temp1 = 0, temp2 = 0;
        if (Integer.parseInt(getMonth_E()) == 1) {
            temp1 = charge_Fund;
        } else {
            temp1 = 0;
        }
        if (Integer.parseInt(getYear_F()) <= 10) {
            temp2 = getSumProduct1(bondFund, equityFund, percentToBeInvestedForBondFund, percentToBeInvestedForEquityFund,
                    percentToBeInvested_BalancedFund,
                    percentToBeInvested_BondOptimiserFund,
                    percentToBeInvested_MoneyMarketFund,
                    percentToBeInvested_CorpBondFund,
                    percentToBeInvested_PureFund,
                    FMC_BalancedFund, FMC_BondOptimiserFund, FMC_MoneyMarketFund, FMC_CorpBondFund, FMC_PureFund);
        } else {
            temp2 = getSumProduct2(bondFund, equityFund, percentToBeInvestedForBondFund, percentToBeInvestedForEquityFund,
                    percentToBeInvested_BalancedFund,
                    percentToBeInvested_BondOptimiserFund,
                    percentToBeInvested_MoneyMarketFund,
                    percentToBeInvested_CorpBondFund,
                    percentToBeInvested_PureFund,
                    FMC_BalancedFund, FMC_BondOptimiserFund, FMC_MoneyMarketFund, FMC_CorpBondFund, FMC_PureFund);
        }

        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.fMCearnedOnFundsUnderMarketLinkedReturn_BH = cfap
                    .roundUp_Level2(cfap.getStringWithout_E((Double
                            .parseDouble(getFundsUnderMarketLinkedReturnAtStartAfterCharges_BD()) + Double
                            .parseDouble(getAdditionToFundsUnderMarketLinkedReturn_BF()))
                            * (temp1 + temp2 / 12)));
        } else {
            this.fMCearnedOnFundsUnderMarketLinkedReturn_BH = "" + 0;
        }
    }

    //		    public double getSumProduct1(double bondFund, double equityFund, double PEmanagedFund,double percentToBeInvestedForBondFund,double percentToBeInvestedForEquityFund,double percentToBeInvestedForPEmanagedFund)
    // {
    // double _bondFund=0,_equityFund=0,_PEmanagedFund=0;
    // if(percentToBeInvestedForBondFund > 0 )
    // {_bondFund=percentToBeInvestedForBondFund/(percentToBeInvestedForBondFund+percentToBeInvestedForEquityFund+percentToBeInvestedForPEmanagedFund);}
    // else
    // {_bondFund=0;}
    // if(percentToBeInvestedForEquityFund > 0 )
    // {_equityFund=percentToBeInvestedForEquityFund/(percentToBeInvestedForBondFund+percentToBeInvestedForEquityFund+percentToBeInvestedForPEmanagedFund);}
    // else
    // {_equityFund=0;}
    // if(percentToBeInvestedForPEmanagedFund > 0 )
    // {_PEmanagedFund=percentToBeInvestedForPEmanagedFund/(percentToBeInvestedForBondFund+percentToBeInvestedForEquityFund+percentToBeInvestedForPEmanagedFund);}
    // else
    // {_PEmanagedFund=0;}
//		        return (bondFund*_bondFund+equityFund*_equityFund+PEmanagedFund*_PEmanagedFund);
    // }
    public double getSumProduct1(double bondFund, double equityFund, double percentToBeInvestedForBondFund, double percentToBeInvestedForEquityFund,
                                 double percentToBeInvested_BalancedFund,
                                 double percentToBeInvested_BondOptimiserFund,
                                 double percentToBeInvested_MoneyMarketFund,
                                 double percentToBeInvested_CorpBondFund,
                                 double percentToBeInvested_PureFund,
                                 double FMC_BalancedFund, double FMC_BondOptimiserFund, double FMC_MoneyMarketFund, double FMC_CorpBondFund, double FMC_PureFund) {
        double _bondFund = 0, _equityFund = 0, _PEmanagedFund = 0;
	       /*if(percentToBeInvestedForBondFund > 0 )
		       {_bondFund=percentToBeInvestedForBondFund/(percentToBeInvestedForBondFund+percentToBeInvestedForEquityFund);}
		       else
		       {_bondFund=0;}
		       if(percentToBeInvestedForEquityFund > 0 )
		       {_equityFund=percentToBeInvestedForEquityFund/(percentToBeInvestedForBondFund+percentToBeInvestedForEquityFund);}
		       else
		       {_equityFund=0;}
        // if(percentToBeInvestedForPEmanagedFund > 0 )
        // {_PEmanagedFund=percentToBeInvestedForPEmanagedFund/(percentToBeInvestedForBondFund+percentToBeInvestedForEquityFund+percentToBeInvestedForPEmanagedFund);}
        // else
        // {_PEmanagedFund=0;}
	        return (bondFund*_bondFund+equityFund*_equityFund);*/
        return percentToBeInvestedForBondFund * bondFund +
                percentToBeInvestedForEquityFund * equityFund +
                percentToBeInvested_BalancedFund * FMC_BalancedFund +
                percentToBeInvested_BondOptimiserFund * FMC_BondOptimiserFund +
                percentToBeInvested_MoneyMarketFund * FMC_MoneyMarketFund +
                percentToBeInvested_CorpBondFund * FMC_CorpBondFund +
                percentToBeInvested_PureFund * FMC_PureFund;
    }


//		    public double getSumProduct2(double returnGuaranteeFund,double bondFund, double equityFund, double PEmanagedFund,double percentToBeInvestedForReturnGuaranteedFund, double percentToBeInvestedForBondFund, double percentToBeInvestedForEquityFund, double percentToBeInvestedForPEmanagedFund)
    // {
    // double _returnGuaranteeFund=0,_bondFund=0,_equityFund=0,_PEmanagedFund=0;
    // if(percentToBeInvestedForReturnGuaranteedFund > 0 )
    // {_returnGuaranteeFund=percentToBeInvestedForReturnGuaranteedFund/(percentToBeInvestedForReturnGuaranteedFund+percentToBeInvestedForBondFund+percentToBeInvestedForEquityFund+percentToBeInvestedForPEmanagedFund);}
    // else
    // {_returnGuaranteeFund=0;}
    //
    // if(percentToBeInvestedForBondFund > 0 )
    // {_bondFund=percentToBeInvestedForBondFund/(percentToBeInvestedForReturnGuaranteedFund+percentToBeInvestedForBondFund+percentToBeInvestedForEquityFund+percentToBeInvestedForPEmanagedFund);}
    // else
    // {_bondFund=0;}
    // if(percentToBeInvestedForEquityFund > 0 )
    // {_equityFund=percentToBeInvestedForEquityFund/(percentToBeInvestedForReturnGuaranteedFund+percentToBeInvestedForBondFund+percentToBeInvestedForEquityFund+percentToBeInvestedForPEmanagedFund);}
    // else
    // {_equityFund=0;}
    // if(percentToBeInvestedForPEmanagedFund > 0 )
    // {_PEmanagedFund=percentToBeInvestedForPEmanagedFund/(percentToBeInvestedForReturnGuaranteedFund+percentToBeInvestedForBondFund+percentToBeInvestedForEquityFund+percentToBeInvestedForPEmanagedFund);}
    // else
    // {_PEmanagedFund=0;}
//		        return (returnGuaranteeFund*_returnGuaranteeFund+bondFund*_bondFund+equityFund*_equityFund+PEmanagedFund*_PEmanagedFund);
    // }

    public double getSumProduct2(double bondFund, double equityFund, double percentToBeInvestedForBondFund, double percentToBeInvestedForEquityFund,
                                 double percentToBeInvested_BalancedFund,
                                 double percentToBeInvested_BondOptimiserFund,
                                 double percentToBeInvested_MoneyMarketFund,
                                 double percentToBeInvested_CorpBondFund,
                                 double percentToBeInvested_PureFund,
                                 double FMC_BalancedFund, double FMC_BondOptimiserFund, double FMC_MoneyMarketFund, double FMC_CorpBondFund, double FMC_PureFund) {
        double _returnGuaranteeFund = 0, _bondFund = 0, _equityFund = 0, _PEmanagedFund = 0;
        // if(percentToBeInvestedForReturnGuaranteedFund > 0 )
        // {_returnGuaranteeFund=percentToBeInvestedForReturnGuaranteedFund/(percentToBeInvestedForReturnGuaranteedFund+percentToBeInvestedForBondFund+percentToBeInvestedForEquityFund+percentToBeInvestedForPEmanagedFund);}
        // else
        // {_returnGuaranteeFund=0;}

	       /*if(percentToBeInvestedForBondFund > 0 )
		       {_bondFund=percentToBeInvestedForBondFund/(percentToBeInvestedForBondFund+percentToBeInvestedForEquityFund);}
		       else
		       {_bondFund=0;}
		       if(percentToBeInvestedForEquityFund > 0 )
		       {_equityFund=percentToBeInvestedForEquityFund/(percentToBeInvestedForBondFund+percentToBeInvestedForEquityFund);}
		       else
		       {_equityFund=0;}
        // if(percentToBeInvestedForPEmanagedFund > 0 )
        // {_PEmanagedFund=percentToBeInvestedForPEmanagedFund/(percentToBeInvestedForReturnGuaranteedFund+percentToBeInvestedForBondFund+percentToBeInvestedForEquityFund+percentToBeInvestedForPEmanagedFund);}
        // else
        // {_PEmanagedFund=0;}
	        return (bondFund*_bondFund+equityFund*_equityFund);*/

        return percentToBeInvestedForBondFund * bondFund +
                percentToBeInvestedForEquityFund * equityFund +
                percentToBeInvested_BalancedFund * FMC_BalancedFund +
                percentToBeInvested_BondOptimiserFund * FMC_BondOptimiserFund +
                percentToBeInvested_MoneyMarketFund * FMC_MoneyMarketFund +
                percentToBeInvested_CorpBondFund * FMC_CorpBondFund +
                percentToBeInvested_PureFund * FMC_PureFund;
    }

    public String getFMCearnedOnIndexFund_BJ() {
        return FMCearnedOnIndexFund_BJ;
    }

    public void setFMCearnedOnIndexFund_BJ(double indexFund, int policyTerm,
                                           double charge_Fund) {
        double a = 0;
        if (Double.parseDouble(getAdditionToFundUnderRGF_AD()) == 1) {
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

    public void setTopUpPremium_J(boolean topUp, double effectiveTopUpPrem) {
        if (getMonth_E().equals("1") && topUp) {
            this.TopUpPremium_J = ("" + effectiveTopUpPrem);
        } else {
            this.TopUpPremium_J = ("" + 0);
        }
    }

    public String getFundUnderRGFatEnd_AN() {
        return fundUnderRGFatEnd_AN;
    }

    public void setFundUnderRGFatEnd_AN(boolean fundManagementCharges,
                                        double serviceTax) {
        if (getPolicyInForce_G().equals("Y")) {
            double temp1 = 0, temp2 = 0, temp3 = 0;

            temp1 = (Double.parseDouble(getFundUnderRGFafterCharges_AB()) + Double
                    .parseDouble(getAdditionToFundUnderRGF_AD())) * 0.0135 / 12;
            if (fundManagementCharges) {
                temp2 = Double.parseDouble(getFMCearnedOnFundUnderRGF_AF());
            } else {
                temp2 = 0;
            }
            temp3 = Math.max(temp1, temp2);

            this.fundUnderRGFatEnd_AN = cfap
                    .roundUp_Level2(cfap.getStringWithout_E((Double
                            .parseDouble(getFundUnderRGFafterCharges_AB())
                            + Double.parseDouble(getAdditionToFundUnderRGF_AD())
                            - Double.parseDouble(getFMCearnedOnFundUnderRGF_AF()) - temp3
                            * serviceTax)));
        } else {
            this.fundUnderRGFatEnd_AN = ("" + 0);
        }
    }

    public String getFundsUnderMarketLinkedReturnAtEnd_AO() {
        return fundsUnderMarketLinkedReturnAtEnd_AO;
    }

    public void setfundsUnderMarketLinkedReturnAtEnd_AO(boolean fundManagementCharges, double serviceTax, int policyTerm) {
	        /*if(getPolicyInForce_G().equals("Y"))
		        {
            double temp1 = 0, temp2 = 0, temp3 = 0;

	            System.out.println("getFundsUnderMarketLinkedReturnAtStartAfterCharges_AC()"+getFundsUnderMarketLinkedReturnAtStartAfterCharges_AC());
	            System.out.println("getAdditionToFundsUnderMarketLinkedReturn_AE()"+getAdditionToFundsUnderMarketLinkedReturn_AE());
		            temp1=(Double.parseDouble(getFundsUnderMarketLinkedReturnAtStartAfterCharges_AC())+Double.parseDouble(getAdditionToFundsUnderMarketLinkedReturn_AE()))*0.0135/12;
		            if(fundManagementCharges)
		            {temp2=Double.parseDouble(getFMCearnedOnFundsUnderMarketLinkedReturn_AG());}
		            else
		            {temp2=0;}
            temp3 = Math.max(temp1, temp2);
		            this.fundsUnderMarketLinkedReturnAtEnd_AO= ""+(Double.parseDouble(getFundsUnderMarketLinkedReturnAtStartAfterCharges_AC())+Double.parseDouble(getAdditionToFundsUnderMarketLinkedReturn_AE()) -Double.parseDouble(getFMCearnedOnFundsUnderMarketLinkedReturn_AG())-temp3 * serviceTax);
		        }
		        else
	        {this.fundsUnderMarketLinkedReturnAtEnd_AO=(""+0);}*/
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.fundsUnderMarketLinkedReturnAtEnd_AO = "" + (Double.parseDouble(getFundBeforeFMC_AI()) - Double.parseDouble(getFundManagementCharge_AJ()) - Double.parseDouble(getServiceTaxOnFMC_AK()));
        } else {
            this.fundsUnderMarketLinkedReturnAtEnd_AO = "" + 0;
        }
    }

    public String getFundsUnderMarketLinkedReturnAtEnd_BP() {
        return fundsUnderMarketLinkedReturnAtEnd_BP;
    }

    public void setfundsUnderMarketLinkedReturnAtEnd_BP(boolean fundManagementCharges, double serviceTax, int policyTerm) {
	        /*if(getPolicyInForce_G().equals("Y"))
		        {
            double temp1 = 0, temp2 = 0, temp3 = 0;

            temp1 = (Double
                    .parseDouble(getFundsUnderMarketLinkedReturnAtStartAfterCharges_BD()) + Double
                    .parseDouble(getAdditionToFundsUnderMarketLinkedReturn_BF())) * 0.0135 / 12;
		            if(fundManagementCharges)
		            {temp2=Double.parseDouble(getFMCearnedOnFundsUnderMarketLinkedReturn_BH());}
		            else
		            {temp2=0;}
            temp3 = Math.max(temp1, temp2);
		            this.fundsUnderMarketLinkedReturnAtEnd_BP= ""+(Double.parseDouble(getFundsUnderMarketLinkedReturnAtStartAfterCharges_BD())+Double.parseDouble(getAdditionToFundsUnderMarketLinkedReturn_BF()) -Double.parseDouble(getFMCearnedOnFundsUnderMarketLinkedReturn_BH())-temp3 * serviceTax);
		        }
		        else
	        {this.fundsUnderMarketLinkedReturnAtEnd_BP=(""+0);}*/
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.fundsUnderMarketLinkedReturnAtEnd_BP = "" + (Double.parseDouble(getFundBeforeFMC_BJ()) - Double.parseDouble(getFundManagementCharge_BK()) - Double.parseDouble(getServiceTaxOnFMC_BL()));
        } else {
            this.fundsUnderMarketLinkedReturnAtEnd_BP = "" + 0;
        }
    }

    public String getFundUnderRGFatEnd_BO() {
        return fundUnderRGFatEnd_BO;
    }

    public void setFundUnderRGFatEnd_BO(boolean fundManagementCharges,
                                        double serviceTax) {
        if (getPolicyInForce_G().equals("Y")) {
            double temp1 = 0, temp2 = 0, temp3 = 0;

            temp1 = (Double.parseDouble(getFundUnderRGFafterCharges_BC()) + Double
                    .parseDouble(getAdditionToFundUnderRGF_BE())) * 0.0135 / 12;
            if (fundManagementCharges) {
                temp2 = Double.parseDouble(getFMCearnedOnFundUnderRGF_BG());
            } else {
                temp2 = 0;
            }
            temp3 = Math.max(temp1, temp2);
            this.fundUnderRGFatEnd_BO = ""
                    + (Double.parseDouble(getFundUnderRGFafterCharges_BC())
                    + Double.parseDouble(getAdditionToFundUnderRGF_BE())
                    - Double.parseDouble(getFMCearnedOnFundUnderRGF_BG()) - temp3
                    * serviceTax);
        } else {
            this.fundUnderRGFatEnd_BO = ("" + 0);
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

    public String getGuaranteedAddition_AM() {
        return guaranteedAddition_AM;
    }

    public void setGuaranteedAddition_AM() {
        this.guaranteedAddition_AM = "" + 0;
    }

    public String getGuaranteedAddition_BN() {
        return guaranteedAddition_BN;
    }

    public void setGuaranteedAddition_BN() {
        this.guaranteedAddition_BN = "" + 0;
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

    public String getAdditionToFundUnderRGF_AD() {
        return additionToFundUnderRGF_AD;
    }

    public void setAdditionToFundUnderRGF_AD(int policyTerm, double int1) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.additionToFundUnderRGF_AD = cfap
                    .roundUp_Level2(cfap.getStringWithout_E((Double
                            .parseDouble(getFundUnderRGFafterCharges_AB()) * (cfap
                            .pow((1 + int1), (double) 1 / 12) - 1))));
        } else {
            this.additionToFundUnderRGF_AD = "" + 0;
        }
    }

    public String getAdditionToFundsUnderMarketLinkedReturn_AE() {
        return additionToFundsUnderMarketLinkedReturn_AE;
    }

    public void setAdditionToFundsUnderMarketLinkedReturn_AE(int policyTerm,
                                                             double int1) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.additionToFundsUnderMarketLinkedReturn_AE = cfap
                    .roundUp_Level2(cfap.getStringWithout_E((Double
                            .parseDouble(getFundsUnderMarketLinkedReturnAtStartAfterCharges_AC()) * (cfap
                            .pow((1 + int1), (double) 1 / 12) - 1))));
        } else {
            this.additionToFundsUnderMarketLinkedReturn_AE = "" + 0;
        }
    }

    public String getAdditionToFundsUnderMarketLinkedReturn_BF() {
        return additionToFundsUnderMarketLinkedReturn_BF;
    }

    public void setAdditionToFundsUnderMarketLinkedReturn_BF(int policyTerm,
                                                             double int2) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.additionToFundsUnderMarketLinkedReturn_BF = cfap
                    .roundUp_Level2(cfap.getStringWithout_E((Double
                            .parseDouble(getFundsUnderMarketLinkedReturnAtStartAfterCharges_BD()) * (cfap
                            .pow((1 + int2), (double) 1 / 12) - 1))));
        } else {
            this.additionToFundsUnderMarketLinkedReturn_BF = "" + 0;
        }
    }

    public String getAdditionToFundUnderRGF_BE() {
        return additionToFundUnderRGF_BE;
    }

    public void setAdditionToFundUnderRGF_BE(int policyTerm, double int2) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.additionToFundUnderRGF_BE = cfap
                    .roundUp_Level2(cfap.getStringWithout_E((Double
                            .parseDouble(getFundUnderRGFafterCharges_BC()) * (cfap
                            .pow((1 + int2), (double) 1 / 12) - 1))));
        } else {
            this.additionToFundUnderRGF_BE = "" + 0;
        }
    }

    public String getFundsUnderMarketLinkedReturnAtStart_AA() {
        return fundsUnderMarketLinkedReturnAtStart_AA;
    }

    public void setFundsUnderMarketLinkedReturnAtStart_AA(
            double _fundsUnderMarketLinkedReturnAtEnd_AO) {

        if (getPolicyInForce_G().equals("Y")) {
            this.fundsUnderMarketLinkedReturnAtStart_AA = cfap
                    .roundUp_Level2(cfap.getStringWithout_E(Double
                            .parseDouble(getAmountAvailableForInvestment_N())
                            + _fundsUnderMarketLinkedReturnAtEnd_AO
                            + Double.parseDouble(getTransferOfFundFromRGFtoMarketLinkedFunds_Y())));
        } else {
            this.fundsUnderMarketLinkedReturnAtStart_AA = "" + 0;
        }
    }

    public String getFundsUnderMarketLinkedReturnAtStartAfterCharges_AC() {
        return fundsUnderMarketLinkedReturnAtStartAfterCharges_AC;
    }

    public void setFundsUnderMarketLinkedReturnAtStartAfterCharges_AC(
            boolean guaranteeCharges, double serviceTax) {
        double temp1 = 0;
        if (guaranteeCharges) {
            temp1 = serviceTax;
        } else {
            temp1 = 0;
        }

        if (getPolicyInForce_G().equals("Y")) {
            this.fundsUnderMarketLinkedReturnAtStartAfterCharges_AC = cfap
                    .roundUp_Level2(cfap.getStringWithout_E(Double
                            .parseDouble(getFundsUnderMarketLinkedReturnAtStart_AA())
                            - ((Double.parseDouble(getTotalCharges_V())
                            + Double.parseDouble(getTotalServiceTax_exclOfSTonAllocAndSurr_W()) - Double
                            .parseDouble(getGuaranteeCharge_U())
                            * (1 + temp1))
                            * Double.parseDouble(getFundsUnderMarketLinkedReturnAtStart_AA()) / (Double
                            .parseDouble(getFundUnderRGFatStart_Z()) + Double
                            .parseDouble(getFundsUnderMarketLinkedReturnAtStart_AA())))));
        } else {
            this.fundsUnderMarketLinkedReturnAtStartAfterCharges_AC = "" + 0;
        }
    }

    public String getFundsUnderMarketLinkedReturnAtStartAfterCharges_BD() {
        return fundsUnderMarketLinkedReturnAtStartAfterCharges_BD;
    }

    public void setFundsUnderMarketLinkedReturnAtStartAfterCharges_BD(
            boolean guaranteeCharges, double serviceTax) {
        double temp1 = 0;
        if (guaranteeCharges) {
            temp1 = serviceTax;
        } else {
            temp1 = 0;
        }

        if (getPolicyInForce_G().equals("Y")) {
            this.fundsUnderMarketLinkedReturnAtStartAfterCharges_BD = cfap
                    .roundUp_Level2(cfap.getStringWithout_E(Double
                            .parseDouble(getFundsUnderMarketLinkedReturnAtStart_BB())
                            - ((Double.parseDouble(getTotalCharges_AW())
                            + Double.parseDouble(getTotalServiceTax_ExclOfSTonAllocAndSurr_AX()) - Double
                            .parseDouble(getGuaranteeCharge_AV())
                            * (1 + temp1))
                            * Double.parseDouble(getFundsUnderMarketLinkedReturnAtStart_BB()) / (Double
                            .parseDouble(getFundUnderRGFatStart_BA()) + Double
                            .parseDouble(getFundsUnderMarketLinkedReturnAtStart_BB())))));
        } else {
            this.fundsUnderMarketLinkedReturnAtStartAfterCharges_BD = "" + 0;
        }
    }

    public String getFundsUnderMarketLinkedReturnAtStart_BB() {
        return fundsUnderMarketLinkedReturnAtStart_BB;
    }

    public void setFundsUnderMarketLinkedReturnAtStart_BB(
            double _fundsUnderMarketLinkedReturnAtEnd_BP,
            double percentToBeInvestedForReturnGuaranteedFund) {
        if (getPolicyInForce_G().equals("Y")) {
            this.fundsUnderMarketLinkedReturnAtStart_BB = cfap
                    .roundUp_Level2(cfap.getStringWithout_E(Double
                            .parseDouble(getAmountAvailableForInvestment_N())
                            * (1 - percentToBeInvestedForReturnGuaranteedFund)
                            + _fundsUnderMarketLinkedReturnAtEnd_BP
                            + Double.parseDouble(getTransferOfFundFromRGFtoMarketLinkedFunds_AZ())));
        } else {
            this.fundsUnderMarketLinkedReturnAtStart_BB = "" + 0;
        }
    }

    public String getFundUnderRGFafterCharges_AB() {
        return fundUnderRGFafterCharges_AB;
    }

    public void setFundUnderRGFafterCharges_AB(boolean guaranteeCharges,
                                               double serviceTax) {
        double temp1 = 0;
        if (guaranteeCharges) {
            temp1 = serviceTax;
        } else {
            temp1 = 0;
        }

        if (getPolicyInForce_G().equals("Y")) {
            this.fundUnderRGFafterCharges_AB = cfap
                    .roundUp_Level2(cfap.getStringWithout_E(Double
                            .parseDouble(getFundUnderRGFatStart_Z())
                            - ((Double.parseDouble(getTotalCharges_V())
                            + Double.parseDouble(getTotalServiceTax_exclOfSTonAllocAndSurr_W()) - Double
                            .parseDouble(getGuaranteeCharge_U())
                            * (1 + temp1))
                            * Double.parseDouble(getFundUnderRGFatStart_Z()) / (Double
                            .parseDouble(getFundUnderRGFatStart_Z()) + Double
                            .parseDouble(getFundsUnderMarketLinkedReturnAtStart_AA())))
                            - Double.parseDouble(getGuaranteeCharge_U())
                            * (1 + temp1)));
        } else {
            this.fundUnderRGFafterCharges_AB = "" + 0;
        }
    }

    public String getFundUnderRGFafterCharges_BC() {
        return fundUnderRGFafterCharges_BC;
    }

    public void setFundUnderRGFafterCharges_BC(boolean guaranteeCharges,
                                               double serviceTax) {
        double temp1 = 0;
        if (guaranteeCharges) {
            temp1 = serviceTax;
        } else {
            temp1 = 0;
        }

        if (getPolicyInForce_G().equals("Y")) {
            this.fundUnderRGFafterCharges_BC = cfap
                    .roundUp_Level2(cfap.getStringWithout_E(Double
                            .parseDouble(getFundUnderRGFatStart_BA())
                            - ((Double.parseDouble(getTotalCharges_AW())
                            + Double.parseDouble(getTotalServiceTax_ExclOfSTonAllocAndSurr_AX()) - Double
                            .parseDouble(getGuaranteeCharge_AV())
                            * (1 + temp1))
                            * Double.parseDouble(getFundUnderRGFatStart_BA()) / (Double
                            .parseDouble(getFundUnderRGFatStart_BA()) + Double
                            .parseDouble(getFundsUnderMarketLinkedReturnAtStart_BB())))
                            - Double.parseDouble(getGuaranteeCharge_AV())
                            * (1 + temp1)));
        } else {
            this.fundUnderRGFafterCharges_BC = "" + 0;
        }
    }

    public String getFundUnderRGFatStart_Z() {
        return fundUnderRGFatStart_Z;
    }

    public void setFundUnderRGFatStart_Z(double _fundUnderRGFatEnd_AN,
                                         double percentToBeInvestedForReturnGuaranteedFund) {
        if (getPolicyInForce_G().equals("Y")) {
            this.fundUnderRGFatStart_Z = cfap
                    .roundUp_Level2(cfap.getStringWithout_E((Double
                            .parseDouble(getAmountAvailableForInvestment_N())
                            * percentToBeInvestedForReturnGuaranteedFund
                            - Double.parseDouble(getTransferOfFundFromRGFtoMarketLinkedFunds_Y()) + _fundUnderRGFatEnd_AN)));
        } else {
            this.fundUnderRGFatStart_Z = "" + 0;
        }
    }

    public String getFundUnderRGFatStart_BA() {
        return fundUnderRGFatStart_BA;
    }

    public void setFundUnderRGFatStart_BA(double _fundUnderRGFatEnd_BO,
                                          double percentToBeInvestedForReturnGuaranteedFund) {
        if (getPolicyInForce_G().equals("Y")) {
            this.fundUnderRGFatStart_BA = cfap
                    .roundUp_Level2(cfap.getStringWithout_E(Double
                            .parseDouble(getAmountAvailableForInvestment_N())
                            * percentToBeInvestedForReturnGuaranteedFund
                            - Double.parseDouble(getTransferOfFundFromRGFtoMarketLinkedFunds_AZ())
                            + _fundUnderRGFatEnd_BO));
        } else {
            this.fundUnderRGFatStart_BA = "" + 0;
        }
    }

    public String getAdditionToFundIfAny_AH() {
        return additionToFundIfAny_AH;
    }

    public void setAdditionToFundIfAny_AH(int policyTerm, double _fundValueAtEnd_AB, double int1) {
	    	/*System.out.println("getAdditionToFundUnderRGF_AD()"+getAdditionToFundUnderRGF_AD());
	    	System.out.println("getAdditionToFundsUnderMarketLinkedReturn_AE()"+getAdditionToFundsUnderMarketLinkedReturn_AE());*/
	        /*if(Integer.parseInt(getYear_F()) <= policyTerm)
		        {this.additionToFundIfAny_AH =cfap.roundUp_Level2( cfap.getStringWithout_E((Double.parseDouble(getAdditionToFundUnderRGF_AD())+Double.parseDouble(getAdditionToFundsUnderMarketLinkedReturn_AE()))));}
	        else
	        {this.additionToFundIfAny_AH =""+0;}*/
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            double temp1 = 0, temp2 = 0;
            temp1 = (_fundValueAtEnd_AB + Double.parseDouble(getAmountAvailableForInvestment_N()) - Double.parseDouble(getTotalCharges_V())) - Double.parseDouble(getTotalServiceTax_exclOfSTonAllocAndSurr_W());
            double a = (double) 1 / 12;
            temp2 = (cfap.pow((1 + int1), a)) - 1;
            this.additionToFundIfAny_AH = cfap.roundUp_Level2(cfap.getStringWithout_E(temp1 * temp2));
        } else {
            this.additionToFundIfAny_AH = "" + 0;
        }
    }

    public String getAdditionToFundIfAny_BI() {
        return additionToFundIfAny_BI;
    }

    public void setAdditionToFundIfAny_BI(int policyTerm, double _fundValueAtEnd_BQ, double int2) {
	        /*if(Integer.parseInt(getYear_F()) <= policyTerm)
		        {this.additionToFundIfAny_BI =cfap.roundUp_Level2( cfap.getStringWithout_E((Double.parseDouble(getAdditionToFundUnderRGF_BE())+Double.parseDouble(getAdditionToFundsUnderMarketLinkedReturn_BF()))));}
		        else
	        {this.additionToFundIfAny_BI =""+0;}*/

        System.out.println("_fundValueAtEnd_BQ" + _fundValueAtEnd_BQ);

        System.out.println("getAmountAvailableForInvestment_N()" + getAmountAvailableForInvestment_N());
        System.out.println("getTotalCharges_AW()" + getTotalCharges_AW());
        System.out.println("getTotalServiceTax_ExclOfSTonAllocAndSurr_AX()" + getTotalServiceTax_ExclOfSTonAllocAndSurr_AX());

        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            double temp1 = 0, temp2 = 0;
            temp1 = (_fundValueAtEnd_BQ + Double.parseDouble(getAmountAvailableForInvestment_N()) - Double.parseDouble(getTotalCharges_AW())) - Double.parseDouble(getTotalServiceTax_ExclOfSTonAllocAndSurr_AX());
            double a = (double) 1 / 12;
            temp2 = (cfap.pow((1 + int2), a)) - 1;
            this.additionToFundIfAny_BI = cfap.roundUp_Level2(cfap.getStringWithout_E(temp1 * temp2));
        } else {
            this.additionToFundIfAny_AH = "" + 0;
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

    public String getAllocatedFundToFundsUnderMarketLinkedReturn_P() {
        return allocatedFundToFundsUnderMarketLinkedReturn_P;
    }

    public void setAllocatedFundToFundsUnderMarketLinkedReturn_P(
            double _allocatedFundToFundsUnderMarketLinkedReturn_P,
            double percentToBeInvestedForBondFund) {
        if (getPolicyInForce_G().equals("Y")) {
            this.allocatedFundToFundsUnderMarketLinkedReturn_P = cfap
                    .roundUp_Level2(cfap
                            .getStringWithout_E((_allocatedFundToFundsUnderMarketLinkedReturn_P + Double
                                    .parseDouble(getAmountAvailableForInvestment_N())
                                    * percentToBeInvestedForBondFund
                                    * (1 - Double
                                    .parseDouble(getTransferPercentIfAny_O())))));
        } else {
            this.allocatedFundToFundsUnderMarketLinkedReturn_P = "" + 0;
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
                            - ((Double.parseDouble(getTotalCharges_V())
                            + Double.parseDouble(getTotalServiceTax_exclOfSTonAllocAndSurr_W()) - Double
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
                            - ((Double.parseDouble(getTotalCharges_AW())
                            + Double.parseDouble(getTotalServiceTax_ExclOfSTonAllocAndSurr_AX()) - Double
                            .parseDouble(getGuaranteeCharge_AV())
                            * (1 + a))
                            * Double.parseDouble(getDailyProtectFundAtStart_BC()) / (Double
                            .parseDouble(getDailyProtectFundAtStart_BC()) + Double
                            .parseDouble(getIndexFundAtStart_BD())))
                            - Double.parseDouble(getGuaranteeCharge_AV())
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
            a = Double.parseDouble(getFMCearnedOnFundUnderRGF_AF())
                    * (0.0135 / charge_Fund_Ren);
        } else {
            a = 0;
        }
        if (getPolicyInForce_G().equals("Y")) {
            this.dailyProtectFundAtEnd_AO = ""
                    + (Double.parseDouble(getDailyProtectFundAfterCharges_AC())
                    + Double.parseDouble(getAdditionToDailyProtectFund_AE())
                    - Double.parseDouble(getFMCearnedOnFundUnderRGF_AF()) - a
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
                            + Double.parseDouble(getTransferOfFundFromRGFtoMarketLinkedFunds_Y())
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

    public String getDeathBenefit_AT() {
        return deathBenefit_AT;
    }

    public void setDeathBenefit_AT(int policyTerm, double sumAssured) {
        if (Integer.parseInt(getYear_F()) > policyTerm) {
            this.deathBenefit_AT = "" + 0;
        } else {
            this.deathBenefit_AT = ""
                    + (Math.max(
                    Double.parseDouble(getOneHundredPercentOfCummulativePremium_BW()),
                    Math.max(sumAssured,
                            Double.parseDouble(getFundValueAtEnd_AP()))));
        }
    }

    public String getDeathBenefit_BU() {
        return deathBenefit_BU;
    }

    public void setDeathBenefit_BU(int policyTerm, double sumAssured) {
        if (Integer.parseInt(getYear_F()) > policyTerm) {
            this.deathBenefit_BU = "" + 0;
        } else {
            this.deathBenefit_BU = ""
                    + (Math.max(
                    Double.parseDouble(getOneHundredPercentOfCummulativePremium_BW()),
                    Math.max(sumAssured,
                            Double.parseDouble(getFundValueAtEnd_BQ()))));
        }
    }

    public String getFundBeforeFMC_AI() {
        return fundBeforeFMC_AI;
    }

    public void setFundBeforeFMC_AI(int policyTerm, double _fundValueAtEnd_AP) {
	        /*if(Integer.parseInt(getYear_F())<=policyTerm)
		        {this.fundBeforeFMC_AI= ""+(Double.parseDouble(getFundUnderRGFafterCharges_AB())+Double.parseDouble(getFundsUnderMarketLinkedReturnAtStartAfterCharges_AC())+Double.parseDouble(getAdditionToFundIfAny_AH()));}
		        else
	        {this.fundBeforeFMC_AI=""+0;}*/

        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.fundBeforeFMC_AI = cfap.getStringWithout_E(_fundValueAtEnd_AP + Double.parseDouble(getAmountAvailableForInvestment_N()) - Double.parseDouble(getTotalCharges_V()) + Double.parseDouble(getAdditionToFundIfAny_AH()) - Double.parseDouble(getTotalServiceTax_exclOfSTonAllocAndSurr_W()));
        } else {
            this.fundBeforeFMC_AI = "" + 0;
        }
    }

    public String getFundBeforeFMC_BJ() {
        return fundBeforeFMC_AI;
    }

    public void setFundBeforeFMC_BJ(int policyTerm, double _fundValueAtEnd_BQ) {
	        /*if(Integer.parseInt(getYear_F())<=policyTerm)
	        {this.fundBeforeFMC_AI= ""+(Double.parseDouble(getFundUnderRGFafterCharges_BC())+Double.parseDouble(getFundsUnderMarketLinkedReturnAtStartAfterCharges_BD())+Double.parseDouble(getAdditionToFundIfAny_BI()));}
		        else
	        {this.fundBeforeFMC_AI=""+0;}*/

        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.fundBeforeFMC_AI = cfap.getStringWithout_E(_fundValueAtEnd_BQ + Double.parseDouble(getAmountAvailableForInvestment_N()) - Double.parseDouble(getTotalCharges_AW()) + Double.parseDouble(getAdditionToFundIfAny_BI()) - Double.parseDouble(getTotalServiceTax_ExclOfSTonAllocAndSurr_AX()));
        } else {
            this.fundBeforeFMC_AI = "" + 0;
        }
    }

    public String getFundManagementCharge_AJ() {
        return fundManagementCharge_AJ;
    }

    public void setFundManagementCharge_AJ() {
        this.fundManagementCharge_AJ = cfap
                .roundUp_Level2(cfap.getStringWithout_E(Double
                        .parseDouble(getFMCearnedOnFundUnderRGF_AF())
                        + Double.parseDouble(getFMCearnedOnFundsUnderMarketLinkedReturn_AG())));
    }

    public String getFundManagementCharge_BK() {
        return fundManagementCharge_BK;
    }

    public void setFundManagementCharge_BK() {
        this.fundManagementCharge_BK = cfap
                .roundUp_Level2(cfap.getStringWithout_E(Double
                        .parseDouble(getFMCearnedOnFundUnderRGF_BG())
                        + Double.parseDouble(getFMCearnedOnFundsUnderMarketLinkedReturn_BH())));
    }

    public String getFundValueAfterFMCandBeforeGA_BM() {
        return fundValueAfterFMCandBeforeGA_BM;
    }

    public void setFundValueAfterFMCandBeforeGA_BM(int policyTerm) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.fundValueAfterFMCandBeforeGA_BM = ""
                    + (Double.parseDouble(getFundBeforeFMC_BJ())
                    - Double.parseDouble(getFundManagementCharge_BK()) - Double
                    .parseDouble(getServiceTaxOnFMC_BL()));
        } else {
            this.fundValueAfterFMCandBeforeGA_BM = "" + 0;
        }
    }

    public String getFundValueAfterFMCandBeforeGA_AL() {
        return fundValueAfterFMCandBeforeGA_AL;
    }

    public void setFundValueAfterFMCandBeforeGA_AL(int policyTerm) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.fundValueAfterFMCandBeforeGA_AL = ""
                    + (Double.parseDouble(getFundBeforeFMC_AI())
                    - Double.parseDouble(getFundManagementCharge_AJ()) - Double
                    .parseDouble(getServiceTaxOnFMC_AK()));
        } else {
            this.fundValueAfterFMCandBeforeGA_AL = "" + 0;
        }
    }

    public String getFundValueAtEnd_AP() {
        return fundValueAtEnd_AP;
    }

    public void setFundValueAtEnd_AP() {
        this.fundValueAtEnd_AP = ""
                + (Double.parseDouble(getGuaranteedAddition_AM())
                + Double.parseDouble(getFundUnderRGFatEnd_AN()) + Double
                .parseDouble(getFundsUnderMarketLinkedReturnAtEnd_AO()));
    }

    public String getFundValueAtEnd_BQ() {
        return fundValueAtEnd_BQ;
    }

    public void setFundValueAtEnd_BQ() {
        this.fundValueAtEnd_BQ = ""
                + (Double.parseDouble(getGuaranteedAddition_BN())
                + Double.parseDouble(getFundUnderRGFatEnd_BO()) + Double
                .parseDouble(getFundsUnderMarketLinkedReturnAtEnd_BP()));
    }

    public String getFundValueAtEnd_AQ() {
        return fundValueAtEnd_AQ;
    }

    public void setFundValueAtEnd_AQ() {
        this.fundValueAtEnd_AQ = ""
                + (Double.parseDouble(getGuaranteedAddition_AN())
                + Double.parseDouble(getDailyProtectFundAtEnd_AO()) + Double
                .parseDouble(getIndexFundAtEnd_AP()));
    }

    public String getFundValueAtEnd_BS() {
        return fundValueAtEnd_BS;
    }

    public void setFundValueAtEnd_BS() {
        this.fundValueAtEnd_BS = ""
                + (Double.parseDouble(getGuaranteedAddition_BP())
                + Double.parseDouble(getDailyProtectFundAtEnd_BQ()) + Double
                .parseDouble(getIndexFundAtEnd_BR()));
    }


    public String getGuaranteeCharge_AV() {
        return guaranteeCharge_AV;
    }

    public void setGuaranteeCharge_AV(boolean guaranteeCharges,
                                      double charge_Guarantee) {
        if (getPolicyInForce_G().equals("Y") && guaranteeCharges) {
            this.guaranteeCharge_AV = ""
                    + (Double.parseDouble(getFundUnderRGFatStart_BA())
                    * charge_Guarantee / 12);
        } else {
            this.guaranteeCharge_AV = "" + 0;
        }
    }

    public String getGuaranteeCharge_U() {
        return guaranteeCharge_U;
    }

    public void setGuaranteeCharge_U(boolean guaranteeCharges,
                                     double charge_Guarantee) {
        if (getPolicyInForce_G().equals("Y") && guaranteeCharges) {
            this.guaranteeCharge_U = ""
                    + (Double.parseDouble(getFundUnderRGFatStart_Z())
                    * charge_Guarantee / 12);
        } else {
            this.guaranteeCharge_U = "" + 0;
        }
    }

    public String getGuaranteedAddition_AN() {
        return guaranteedAddition_AN;
    }

    public void setGuaranteedAddition_AN() {
        this.guaranteedAddition_AN = "" + 0;
    }

    public String getGuaranteedAddition_BP() {
        return guaranteedAddition_BP;
    }

    public void setGuaranteedAddition_BP() {
        this.guaranteedAddition_BP = "" + 0;
    }

    public String getIndexFundAtEnd_AP() {
        return indexFundAtEnd_AP;
    }

    public void setIndexFundAtEnd_AP(boolean fundManagementCharges,
                                     double indexFund, double serviceTax) {
        if (getPolicyInForce_G().equals("Y")) {
            double a = 0;
            if (fundManagementCharges) {
                a = Double
                        .parseDouble(getFMCearnedOnFundsUnderMarketLinkedReturn_AG())
                        * (0.0135 / indexFund);
            } else {
                a = 0;
            }
            this.indexFundAtEnd_AP = ""
                    + (Double.parseDouble(getIndexFundAtStartAfterCharges_AD())
                    + Double.parseDouble(getAdditionToIndexFund_AF())
                    - Double.parseDouble(getFMCearnedOnFundsUnderMarketLinkedReturn_AG()) - a
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
                            - ((Double.parseDouble(getTotalCharges_V())
                            + Double.parseDouble(getTotalServiceTax_exclOfSTonAllocAndSurr_W()) - Double
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
                            - ((Double.parseDouble(getTotalCharges_AW())
                            + Double.parseDouble(getTotalServiceTax_ExclOfSTonAllocAndSurr_AX()) - Double
                            .parseDouble(getGuaranteeCharge_AV())
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
                            - Double.parseDouble(getTransferOfFundFromRGFtoMarketLinkedFunds_Y())
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

    public String getMortalityCharges_AU() {
        return mortalityCharges_AU;
    }

    public void setMortalityCharges_AU(double _fundValueAtEnd_BQ,
                                       int policyTerm, String[] forBIArray, int ageAtEntry,
                                       double sumAssured, boolean mortalityCharges) {
        if (!(getPolicyInForce_G().equals("Y"))
                || Integer.parseInt(getYear_F()) > policyTerm) {
            this.mortalityCharges_AU = "" + 0;
        } else {
            double arrOutput = Double
                    .parseDouble(forBIArray[(ageAtEntry + Integer
                            .parseInt(getYear_F())) - 1]);
            // double a=1-arrOutput/1000;
            double div = arrOutput / 1000;
            // System.out.println("div "+div);
            double div1 = div / 12;
            // System.out.println("div1 "+div1);
            double a = 1 - div1;
            // System.out.println("a "+a);
            // double b=(double)1/12;
            double c = Math
                    .max(sumAssured,
                            Double.parseDouble(getOneHundredPercentOfCummulativePremium_BW()))
                    - (Double.parseDouble(getAmountAvailableForInvestment_N()) + _fundValueAtEnd_BQ);
            int d = 0;
            if (mortalityCharges) {
                d = 1;
            } else {
                d = 0;
            }
            this.mortalityCharges_AU = cfap.roundUp_Level2(cfap
                    .getStringWithout_E(((1 - (a)) * Math.max(c, 0)) * d));
        }
    }

    public String getMortalityCharges_T() {
        return mortalityCharges_T;
    }

    public void setMortalityCharges_T(double _fundValueAtEnd_AP,
                                      int policyTerm, String[] forBIArray, int ageAtEntry,
                                      double sumAssured, boolean mortalityCharges) {
        if (!(getPolicyInForce_G().equals("Y"))
                || Integer.parseInt(getYear_F()) > policyTerm) {
            this.mortalityCharges_T = "" + 0;
        } else {
            double arrOutput = Double
                    .parseDouble(forBIArray[(ageAtEntry + Integer
                            .parseInt(getYear_F())) - 1]);
            // System.out.println("arrOutput "+arrOutput);
            // double a=1-arrOutput/1000;
            double div = arrOutput / 1000;
            double div1 = div / 12;
            double a = 1 - div1;
            // double b=(double)1/12;
            double c = Math
                    .max(sumAssured,
                            Double.parseDouble(getOneHundredPercentOfCummulativePremium_BW()))
                    - (Double.parseDouble(getAmountAvailableForInvestment_N()) + _fundValueAtEnd_AP);
            int d = 0;
            if (mortalityCharges) {
                d = 1;
            } else {
                d = 0;
            }

            this.mortalityCharges_T = cfap.roundUp_Level2(cfap
                    .getStringWithout_E((((1 - (a)) * Math.max(c, 0)) * d)));
        }
    }

    public String getOneHundredPercentOfCummulativePremium_BW() {
        return oneHundredPercentOfCummulativePremium_BW;
    }

    public void setOneHundredPercentOfCummulativePremium_BW(
            double _oneHundredPercentOfCummulativePremium_BW) {
        double a = 0;
        if (getPolicyInForce_G().equals("Y")) {
            a = Double.parseDouble(getPremium_I())
                    + (_oneHundredPercentOfCummulativePremium_BW / 1.05);
        } else {
            a = 0;
        }
        this.oneHundredPercentOfCummulativePremium_BW = "" + (a * 1.05);
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
            this.optionCharges_R = cfap.getRoundOffLevel2(cfap
                    .getStringWithout_E(Double.parseDouble(dummy)));
        } else {
            this.optionCharges_R = "" + 0;
        }
    }

    public String getPolicyAdministrationCharge_S() {
        return policyAdministrationCharge_S;
    }

    public void setPolicyAdministrationCharge_S(
            double _policyAdministrationCharge_S, int charge_Inflation,
            String premFreqMode, double charge_PP_Ren) {
        if (getPolicyInForce_G().equals("Y")
                && (Integer.parseInt(getMonth_E()) < 61)) {
            if (((Integer.parseInt(getMonth_E()) - 1) % 12) == 0) {
                this.policyAdministrationCharge_S = cfap
                        .roundUp_Level2(cfap.getStringWithout_E(charge_PP_Ren
                                / 12
                                * cfap.getPowerOutput(
                                (1 + charge_Inflation),
                                Integer.parseInt(""
                                        + (Integer
                                        .parseInt(getMonth_E()) / 12)))));
            } else {
                this.policyAdministrationCharge_S = cfap.roundUp_Level2(cfap
                        .getStringWithout_E(_policyAdministrationCharge_S));
            }
        } else {
            this.policyAdministrationCharge_S = "" + 0;
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
        } else
            /*{this.premiumAllocationCharge_K= cfap.getRoundUp(cfap.getStringWithout_E(getAllocationCharge(staffDisc, bancAssuranceDisc, premFreqmode)* Double.parseDouble(getPremium_I())),"roundUpII");}*/ {
            this.premiumAllocationCharge_K = cfap.getRoundUp(cfap.getStringWithout_E(getAllocationCharge(staffDisc, bancAssuranceDisc, premFreqmode) * Double.parseDouble(getPremium_I())));
        }
    }

    public String getPremium_I() {
        return premium_I;
    }

    public void setPremium_I(int premiumPayingTerm, int PF, double effectivePrem) {
        if (getPolicyInForce_G().equals("Y")) {
            if ((Integer.parseInt(getYear_F()) <= premiumPayingTerm)
                    && (((Integer.parseInt(getMonth_E()) - 1) % (12 / PF)) == 0)) {
                premium_I = cfap.roundUp_Level2(cfap.getStringWithout_E(Double
                        .parseDouble("" + (effectivePrem / PF))));
                // System.out.println("Premium I "+ premium_I);
//		            System.out.println("premium I without e"+ cfap.roundUp_Level2(cfap.getStringWithout_E(Double.parseDouble(premium_I))));

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

    public String getServiceTaxOnFMC_AK() {
        return serviceTaxOnFMC_AK;
    }

    public void setServiceTaxOnFMC_AK(boolean fundManagementCharges, double serviceTax,
                                      double bondFund, double equityFund, double PEmanagedFund, double percentToBeInvestedForBondFund, double percentToBeInvestedForEquityFund,
                                      double percentToBeInvested_BalancedFund,
                                      double percentToBeInvested_BondOptimiserFund,
                                      double percentToBeInvested_MoneyMarketFund,
                                      double percentToBeInvested_CorpBondFund,
                                      double percentToBeInvested_PureFund,
                                      double FMC_BalancedFund, double FMC_BondOptimiserFund, double FMC_MoneyMarketFund, double FMC_CorpBondFund, double FMC_PureFund) {
	        /*double temp1=0,temp2=0,temp3=0;
        temp1 = (Double.parseDouble(getFundBeforeFMC_AI()) * 0.0135) / 12;
		        if(fundManagementCharges)
		        {temp2=Double.parseDouble(getFundManagementCharge_AJ());}
		        else
		        {temp2=0;}
        temp3 = Math.max(temp1, temp2);
	        this.serviceTaxOnFMC_AK = cfap.roundUp_Level2(cfap.getStringWithout_E(temp3* serviceTax));*/

        double a = 0;
        if (fundManagementCharges)
//	        {a=(Double.parseDouble(getFundManagementCharge_X()) * (0.0135/getCharge_fund_ren( percentToBeInvested_EquityFund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_BondFund, percentToBeInvested_MoneyMarketFund, percentToBeInvested_Top300Fund,percentToBeInvested_BondOptimiserFund,percentToBeInvested_MidcapFund,percentToBeInvested_PureFund)));}
        {
            a = ((Double.parseDouble(getFundManagementCharge_AJ()) * getSumProduct1(bondFund, equityFund, percentToBeInvestedForBondFund, percentToBeInvestedForEquityFund, percentToBeInvested_BalancedFund, percentToBeInvested_BondOptimiserFund, percentToBeInvested_MoneyMarketFund, percentToBeInvested_CorpBondFund, percentToBeInvested_PureFund, FMC_BalancedFund, FMC_BondOptimiserFund, FMC_MoneyMarketFund, FMC_CorpBondFund, FMC_PureFund) / getSumProduct1(bondFund, equityFund, percentToBeInvestedForBondFund, percentToBeInvestedForEquityFund, percentToBeInvested_BalancedFund,
                    percentToBeInvested_BondOptimiserFund,
                    percentToBeInvested_MoneyMarketFund,
                    percentToBeInvested_CorpBondFund,
                    percentToBeInvested_PureFund,
                    FMC_BalancedFund, FMC_BondOptimiserFund, FMC_MoneyMarketFund, FMC_CorpBondFund, FMC_PureFund)));
        } else {
            a = 0;
        }
        this.serviceTaxOnFMC_AK = cfap.roundUp_Level2(cfap.getStringWithout_E(a * serviceTax));
    }

    public String getServiceTaxOnFMC_BL() {
        return serviceTaxOnFMC_BL;
    }

    public void setServiceTaxOnFMC_BL(boolean fundManagementCharges, double serviceTax,
                                      double bondFund, double equityFund, double PEmanagedFund, double percentToBeInvestedForBondFund, double percentToBeInvestedForEquityFund,
                                      double percentToBeInvested_BalancedFund,
                                      double percentToBeInvested_BondOptimiserFund,
                                      double percentToBeInvested_MoneyMarketFund,
                                      double percentToBeInvested_CorpBondFund,
                                      double percentToBeInvested_PureFund,
                                      double FMC_BalancedFund, double FMC_BondOptimiserFund, double FMC_MoneyMarketFund, double FMC_CorpBondFund, double FMC_PureFund) {
	        /*double temp1=0,temp2=0,temp3=0;
        temp1 = (Double.parseDouble(getFundBeforeFMC_BJ()) * 0.0135) / 12;
		        if(fundManagementCharges)
		        {temp2=Double.parseDouble(getFundManagementCharge_BK());}
		        else
		        {temp2=0;}
		        temp3=Math.max(temp1, temp2);
	        this.serviceTaxOnFMC_BL = cfap.roundUp_Level2(cfap.getStringWithout_E(temp3* serviceTax));*/

        double a = 0;
        if (fundManagementCharges)
//	        {a=(Double.parseDouble(getFundManagementCharge_X()) * (0.0135/getCharge_fund_ren( percentToBeInvested_EquityFund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_BondFund, percentToBeInvested_MoneyMarketFund, percentToBeInvested_Top300Fund,percentToBeInvested_BondOptimiserFund,percentToBeInvested_MidcapFund,percentToBeInvested_PureFund)));}
        {
            a = ((Double.parseDouble(getFundManagementCharge_BK()) * getSumProduct1(bondFund, equityFund, percentToBeInvestedForBondFund, percentToBeInvestedForEquityFund, percentToBeInvested_BalancedFund, percentToBeInvested_BondOptimiserFund, percentToBeInvested_MoneyMarketFund, percentToBeInvested_CorpBondFund, percentToBeInvested_PureFund, FMC_BalancedFund, FMC_BondOptimiserFund, FMC_MoneyMarketFund, FMC_CorpBondFund, FMC_PureFund) / getSumProduct1(bondFund, equityFund, percentToBeInvestedForBondFund, percentToBeInvestedForEquityFund, percentToBeInvested_BalancedFund,
                    percentToBeInvested_BondOptimiserFund,
                    percentToBeInvested_MoneyMarketFund,
                    percentToBeInvested_CorpBondFund,
                    percentToBeInvested_PureFund,
                    FMC_BalancedFund, FMC_BondOptimiserFund, FMC_MoneyMarketFund, FMC_CorpBondFund, FMC_PureFund)));
        } else {
            a = 0;
        }
        this.serviceTaxOnFMC_BL = cfap.roundUp_Level2(cfap.getStringWithout_E(a * serviceTax));
    }

    public String getServiceTaxOnSurrenderCharges_AR() {
        return serviceTaxOnSurrenderCharges_AR;
    }

    public void setServiceTaxOnSurrenderCharges_AR(boolean surrenderCharges,
                                                   double serviceTax) {
        if (surrenderCharges) {
            this.serviceTaxOnSurrenderCharges_AR = cfap
                    .roundUp_Level2(cfap.getStringWithout_E(Double
                            .parseDouble(getSurrenderCharges_AQ()) * serviceTax));
        } else {
            this.serviceTaxOnSurrenderCharges_AR = "" + 0;
        }
    }

    public String getServiceTaxOnSurrenderCharges_BS() {
        return serviceTaxOnSurrenderCharges_BS;
    }

    public void setServiceTaxOnSurrenderCharges_BS(boolean surrenderCharges,
                                                   double serviceTax) {
        if (surrenderCharges) {
            this.serviceTaxOnSurrenderCharges_BS = cfap
                    .roundUp_Level2(cfap.getStringWithout_E(Double
                            .parseDouble(getSurrenderCharges_BR()) * serviceTax));
        } else {
            this.serviceTaxOnSurrenderCharges_BS = "" + 0;
        }
    }

    public String getServiceTaxOnSurrenderCharges_BU() {
        return serviceTaxOnSurrenderCharges_BU;
    }

    public void setServiceTaxOnSurrenderCharges_BU(double serviceTax,
                                                   boolean surrenderCharges) {
        if (surrenderCharges) {
            this.serviceTaxOnSurrenderCharges_BU = cfap
                    .roundUp_Level2(cfap.getStringWithout_E(Double
                            .parseDouble(getSurrenderCharges_BT()) * serviceTax));
        } else {
            this.serviceTaxOnSurrenderCharges_BU = "" + 0;
        }
    }

    public String getSumAssuredRelatedCharges_Q() {
        return sumAssuredRelatedCharges_Q;
    }

    public void setSumAssuredRelatedCharges_Q(int noOfYearsForSArelatedCharges,
                                              double sumAssured, double SAMF, double effectivePremium,
                                              double charge_SumAssuredBase) {
        double a = 0;
        double b = 0;
        if (getMonth_E().equals("1")) {
            a = charge_SumAssuredBase;
        } else {
            a = 0;
        }
        if (Integer.parseInt(getYear_F()) <= noOfYearsForSArelatedCharges) {
            b = charge_SumAssuredBase / 12;
        } else {
            b = 0;
        }
        this.sumAssuredRelatedCharges_Q = cfap.roundUp_Level2(cfap
                .getStringWithout_E(sumAssured * (a + b)));
    }

    public String getSurrenderCap_BV() {
        return surrenderCap_BV;
    }

    public void setSurrenderCap_BV(double effectivePremium) {
        // //System.out.println("Received  effectivePremium =  "+effectivePremium);
	        /*if(getPolicyInForce_G().equals("Y"))
		        {this.surrenderCap_BV =""+(Double.parseDouble(calCapArr(effectivePremium)[Integer.parseInt(getYear_F())-1]));}
		        else
	        {this.surrenderCap_BV =""+0;}*/

        double a = 0;
//	    	if(planType.equals("Single")){
	        	/*if(effectivePremium >25000){
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
	        	} else {
	        		a=0;
	        	}
	        } else{*/
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
        this.surrenderCap_BV = "" + a;
    }

    public String getSurrenderCharges_AQ() {
        return surrenderCharges_AQ;
    }

    public void setSurrenderCharges_AQ(double effectivePremium,
                                       String premFreqMode) {
        double a = Math.min(Double.parseDouble(getFundValueAtEnd_AP()), (double) effectivePremium);
        double b = getSurrenderCharge(premFreqMode, effectivePremium);
        this.surrenderCharges_AQ = cfap.roundUp_Level2(cfap
                .getStringWithout_E(Math.min((a * b),
                        Double.parseDouble(getSurrenderCap_BV()))));
    }

    public String getSurrenderCharges_BR() {
        return surrenderCharges_BR;
    }

    public void setSurrenderCharges_BR(double effectivePremium,
                                       String premFreqMode) {
        double a = Math.min(Double.parseDouble(getFundValueAtEnd_BQ()), (double) effectivePremium);
        double b = getSurrenderCharge(premFreqMode, effectivePremium);
        this.surrenderCharges_BR = cfap.roundUp_Level2(cfap
                .getStringWithout_E(Math.min((a * b),
                        Double.parseDouble(getSurrenderCap_BV()))));
    }

    public String getSurrenderCharges_BT() {
        return surrenderCharges_BT;
    }

    public void setSurrenderCharges_BT(double effectivePremium,
                                       String premFreqMode) {
        double c = Math.min(Double.parseDouble(getFundValueAtEnd_BS()),
                effectivePremium);
        double d = getSurrenderCharge(premFreqMode, effectivePremium);
        this.surrenderCharges_BT = cfap.roundUp_Level2(cfap
                .getStringWithout_E(Math.min((c * d),
                        Double.parseDouble(getSurrenderCap_BV()))));
    }

    public String getSurrenderValue_AS() {
        return surrenderValue_AS;
    }

    public void setSurrenderValue_AS() {
        this.surrenderValue_AS = ""
                + (Double.parseDouble(getFundValueAtEnd_AP())
                - Double.parseDouble(getSurrenderCharges_AQ()) - Double
                .parseDouble(getServiceTaxOnSurrenderCharges_AR()));
    }

    public String getSurrenderValue_BT() {
        return surrenderValue_BT;
    }

    public void setSurrenderValue_BT() {
        this.surrenderValue_BT = ""
                + (Double.parseDouble(getFundValueAtEnd_BQ())
                - Double.parseDouble(getSurrenderCharges_BR()) - Double
                .parseDouble(getServiceTaxOnSurrenderCharges_BS()));
    }

    public String getSurrenderValue_BV() {
        return surrenderValue_BV;
    }

    public void setSurrenderValue_BV() {
        this.surrenderValue_BV = ""
                + (Double.parseDouble(getFundValueAtEnd_BS())
                + Double.parseDouble(getSurrenderCharges_BT()) + Double
                .parseDouble(getServiceTaxOnSurrenderCharges_BU()));
    }

    public String getTopUpCharges_L() {
        return topUpCharges_L;
    }

    public void setTopUpCharges_L(double topUp) {
        this.topUpCharges_L = cfap.getRoundUp(""
                + (Double.parseDouble(getTopUpPremium_J()) * topUp));
    }

    public String getTotalCharges_AW() {
        return totalCharges_AW;
    }

    public void setTotalCharges_AW(int policyTerm) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.totalCharges_AW = "" + (Double.parseDouble(getPolicyAdministrationCharge_S()) + Double.parseDouble(getMortalityCharges_AU()) + Double.parseDouble(getSumAssuredRelatedCharges_Q()) + Double.parseDouble(getGuaranteeCharge_AV()) + Double.parseDouble(getOptionCharges_R()));
        } else {
            this.totalCharges_AW = "" + 0;
        }
    }

    public String getTotalCharges_V() {
        return totalCharges_V;
    }

    public void setTotalCharges_V(int policyTerm) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.totalCharges_V = ""
                    + (Double.parseDouble(getPolicyAdministrationCharge_S())
                    + Double.parseDouble(getMortalityCharges_T())
                    + Double.parseDouble(getSumAssuredRelatedCharges_Q())
                    + Double.parseDouble(getOptionCharges_R()) + Double
                    .parseDouble(getGuaranteeCharge_U()));
        } else {
            this.totalCharges_V = "" + 0;
        }
    }

    public String getTotalServiceTax_AY() {
        return totalServiceTax_AY;
    }

    public void setTotalServiceTax_AY() {
        this.totalServiceTax_AY = cfap
                .roundUp_Level2(cfap.getStringWithout_E(Double
                        .parseDouble(getServiceTaxOnAllocation_M())
                        + Double.parseDouble(getTotalServiceTax_ExclOfSTonAllocAndSurr_AX())
                        + Double.parseDouble(getServiceTaxOnFMC_BL())));
    }

    public String getTotalServiceTax_X() {
        return totalServiceTax_X;
    }

    public void setTotalServiceTax_X() {
        this.totalServiceTax_X = cfap
                .roundUp_Level2(cfap.getStringWithout_E(Double
                        .parseDouble(getServiceTaxOnAllocation_M())
                        + Double.parseDouble(getTotalServiceTax_exclOfSTonAllocAndSurr_W())
                        + Double.parseDouble(getServiceTaxOnFMC_AK())));
    }

    public String getTotalServiceTax_exclOfSTonAllocAndSurr_W() {
        return totalServiceTax_exclOfSTonAllocAndSurr_W;
    }

    public void setTotalServiceTax_exclOfSTonAllocAndSurr_W(double serviceTax,
                                                            boolean mortalityAndRiderCharges,
                                                            boolean administrationAndSArelatedCharges, boolean guaranteeCharges) {
        double a = 0, b = 0, c = 0;
        if (mortalityAndRiderCharges) {
            a = Double.parseDouble(getMortalityCharges_T())
                    + Double.parseDouble(getOptionCharges_R());
        } else {
            a = 0;
        }
        if (administrationAndSArelatedCharges) {
            b = Double.parseDouble(getPolicyAdministrationCharge_S())
                    + Double.parseDouble(getSumAssuredRelatedCharges_Q());
        } else {
            b = 0;
        }
        if (guaranteeCharges) {
            c = Double.parseDouble(getGuaranteeCharge_U());
        } else {
            c = 0;
        }
        this.totalServiceTax_exclOfSTonAllocAndSurr_W = cfap
                .roundUp_Level2(cfap.getStringWithout_E((a + b + c)
                        * serviceTax));
    }

    public String getTotalServiceTax_ExclOfSTonAllocAndSurr_AX() {
        return totalServiceTax_ExclOfSTonAllocAndsurr_AX;
    }

    public void setTotalServiceTax_ExclOfSTonAllocAndSurr_AX(double serviceTax,
                                                             boolean mortalityAndRiderCharges,
                                                             boolean administrationAndSArelatedCharges, boolean guaranteeCharges) {
        double a = 0, b = 0, c = 0;
        if (mortalityAndRiderCharges) {
            a = Double.parseDouble(getMortalityCharges_AU())
                    + Double.parseDouble(getOptionCharges_R());
        } else {
            a = 0;
        }
        if (administrationAndSArelatedCharges) {
            b = Double.parseDouble(getPolicyAdministrationCharge_S())
                    + Double.parseDouble(getSumAssuredRelatedCharges_Q());
        } else {
            b = 0;
        }
        if (guaranteeCharges) {
            c = Double.parseDouble(getGuaranteeCharge_AV());
        } else {
            c = 0;
        }
        this.totalServiceTax_ExclOfSTonAllocAndsurr_AX = cfap
                .roundUp_Level2(cfap.getStringWithout_E((a + b + c)
                        * serviceTax));
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
                .parseDouble(getAllocatedFundToFundsUnderMarketLinkedReturn_P()) * (1 + thresholdLimitForTransfOfGain))) {
            a = _indexFundAtEnd
                    - Double.parseDouble(getAllocatedFundToFundsUnderMarketLinkedReturn_P());
        } else {
            a = 0;
        }
        if (getPolicyInForce_G().equals("Y")) {
            if (Double
                    .parseDouble(getAllocatedFundToFundsUnderMarketLinkedReturn_P()) > 0
                    && Integer.parseInt(getMonth_E()) <= noOfYrsAllowForTransfOfGain * 12) {
                this.transferOfIndexFundGainToDailyProtectFund_BA = "" + a;
            } else {
                this.transferOfIndexFundGainToDailyProtectFund_BA = "" + 0;
            }
        } else {
            this.transferOfIndexFundGainToDailyProtectFund_BA = "" + 0;
        }
    }

    public String getTransferOfFundFromRGFtoMarketLinkedFunds_Y() {
        return transferOfFundFromRGFtoMarketLinkedFunds_Y;
    }

    public void setTransferOfFundFromRGFtoMarketLinkedFunds_Y(
            double _fundUnderRGFatEnd_AN,
            int noOfYrsAfterWhichTransferTakePlace, double charge_SumAssuredBase) {
        if (getPolicyInForce_G().equals("Y")) {
            if (Integer.parseInt(getMonth_E()) == (noOfYrsAfterWhichTransferTakePlace * 12 + 1)) {
                this.transferOfFundFromRGFtoMarketLinkedFunds_Y = ""
                        + _fundUnderRGFatEnd_AN;
            } else {
                this.transferOfFundFromRGFtoMarketLinkedFunds_Y = "" + 0;
            }
        } else {
            this.transferOfFundFromRGFtoMarketLinkedFunds_Y = "" + 0;
        }
    }

    public String getTransferOfFundFromRGFtoMarketLinkedFunds_AZ() {
        return transferOfFundFromRGFtoMarketLinkedFunds_AZ;
    }

    public void setTransferOfFundFromRGFtoMarketLinkedFunds_AZ(double _fundUnderRGFatEnd_BO, int noOfYrsAfterWhichTransferTakePlace, double charge_SumAssuredBase)//,double _fundUnderRGFatEND,int noOfYrsAllowForTransfOfGain,double thresholdLimitForTransfOfGain)
    {
        if (getPolicyInForce_G().equals("Y")) {
            if (Integer.parseInt(getMonth_E()) == (noOfYrsAfterWhichTransferTakePlace * 12 + 1)) {
                this.transferOfFundFromRGFtoMarketLinkedFunds_AZ = ""
                        + _fundUnderRGFatEnd_BO;
            } else {
                this.transferOfFundFromRGFtoMarketLinkedFunds_AZ = "" + 0;
            }
        } else {
            this.transferOfFundFromRGFtoMarketLinkedFunds_AZ = "" + 0;
        }
    }

    public String getTransferPercentIfAny_O() {
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

    public double getSurrenderCharge(String premFreqmode, double effectivePremium) {
        double surrenderCharge = 0;
	       /* if(premFreqmode.equals("Limited"))
		        {surrenderCharge=Double.parseDouble(calSurrRateArrRP(effectivePremium)[Math.min((Integer.parseInt(getYear_F())-1),11)]);}
        // For Single Mode of Premium Freqency
		        else
		        {surrenderCharge=Double.parseDouble(calSurrRateArrSP()[Math.min((Integer.parseInt(getYear_F())-1),11)]);;}
	        return surrenderCharge;*/

//	        if(planType.equals("Single")){
	        	/*if(effectivePremium >25000){
	        		if(getYear_F().equals("1"))
	                 	{
	                 		surrenderCharge = 0.02;
	                 	}
	                 	else if(getYear_F().equals("2"))
	                 	{
	                 		surrenderCharge = 0.015;
	                 	}
	                 	else if(getYear_F().equals("3"))
	                 	{
	                 		surrenderCharge = 0.01;
	                 	}
	                 	else if(getYear_F().equals("4"))
	                 	{
	                 		surrenderCharge = 0.005;
	                 	}
	                 	else
	                 	{
	                 		surrenderCharge=0;
	                 	}
	        	} else {
	        		surrenderCharge=0;
	        	}
	        else{*/
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

	    	 /*double surrenderCharge=0;
	         if(PPT==1)
	         {
	             //surrenderCharge=1;
            surrenderCharge = Double.parseDouble(calSurrRateArrSP()[Math.min((Integer.parseInt(getYear_F()) - 1), 11)]);
        }
	         else
	         {surrenderCharge=Double.parseDouble(calSurrRateArrRP(effectivePremium)[Math.min((Integer.parseInt(getYear_F())-1),11)]);}*/

        return surrenderCharge;
    }

    // Related to -> T37 / AA37
    public double getAllocationCharge(boolean staffDisc, boolean bancAssuranceDisc, String premFreqmode) {
        double allocationChargeWithoutDisc = 0;
        double discOnallocationCharge = 0;
        if (premFreqmode.equals("Limited")) {
            if (getYear_F().equals("1")) {
                allocationChargeWithoutDisc = 0.085;
                discOnallocationCharge = 0.08;
            } else if (getYear_F().equals("2")) {
                allocationChargeWithoutDisc = 0.06;
                discOnallocationCharge = 0.02;
            } else if (getYear_F().equals("3")) {
                allocationChargeWithoutDisc = 0.06;
                discOnallocationCharge = 0.02;
            } else if (getYear_F().equals("4")) {
                allocationChargeWithoutDisc = 0.06;
                discOnallocationCharge = 0.01;
            } else if (getYear_F().equals("5")) {
                allocationChargeWithoutDisc = 0.06;
                discOnallocationCharge = 0.01;
            } else if (getYear_F().equals("6")) {
                allocationChargeWithoutDisc = 0;
                discOnallocationCharge = 0;
            } else if (getYear_F().equals("7")) {
                allocationChargeWithoutDisc = 0;
                discOnallocationCharge = 0;
            } else if (getYear_F().equals("8")) {
                allocationChargeWithoutDisc = 0;
                discOnallocationCharge = 0;
            } else if (getYear_F().equals("9")) {
                allocationChargeWithoutDisc = 0;
                discOnallocationCharge = 0;
            } else if (getYear_F().equals("10")) {
                allocationChargeWithoutDisc = 0;
                discOnallocationCharge = 0;
            }
        }
        // For Single Mode of Premium Freqency
        else {
            if (getYear_F().equals("1")) {
                allocationChargeWithoutDisc = 0.03;
                discOnallocationCharge = 0.02;
            } else if (getYear_F().equals("2")) {
                allocationChargeWithoutDisc = 0.0;
                discOnallocationCharge = 0.0;
            } else if (getYear_F().equals("3")) {
                allocationChargeWithoutDisc = 0.0;
                discOnallocationCharge = 0.0;
            } else if (getYear_F().equals("4")) {
                allocationChargeWithoutDisc = 0.0;
                discOnallocationCharge = 0.0;
            } else if (getYear_F().equals("5")) {
                allocationChargeWithoutDisc = 0.0;
                discOnallocationCharge = 0.0;
            } else if (getYear_F().equals("6")) {
                allocationChargeWithoutDisc = 0.0;
                discOnallocationCharge = 0.0;
            } else if (getYear_F().equals("7")) {
                allocationChargeWithoutDisc = 0.0;
                discOnallocationCharge = 0.0;
            } else if (getYear_F().equals("8")) {
                allocationChargeWithoutDisc = 0.0;
                discOnallocationCharge = 0.0;
            } else if (getYear_F().equals("9")) {
                allocationChargeWithoutDisc = 0.0;
                discOnallocationCharge = 0.0;
            } else if (getYear_F().equals("10")) {
                allocationChargeWithoutDisc = 0.0;
                discOnallocationCharge = 0.0;
            }
        }
        if (staffDisc == true && bancAssuranceDisc == false) {
            String dummyStr1 = ""
                    + (allocationChargeWithoutDisc - discOnallocationCharge);
            String dummyStr2 = dummyStr1 + "0000000000";
            int decIndex = dummyStr2.indexOf('.');
            String dummyStr3 = null;
            if (dummyStr2.substring(decIndex + 3, decIndex + 7).equals("9999")) {
                dummyStr3 = ("" + (Double.parseDouble(dummyStr2) + 0.00001))
                        .substring(0, decIndex + 5);
            } else {
                dummyStr3 = dummyStr2.substring(0, decIndex + 5);
            }
            return Double.parseDouble(dummyStr3);
        } else if (staffDisc == false && bancAssuranceDisc == true) {
            int mulFactor = 0;
            if (getYear_F().equals("1")) {
                mulFactor = 1;
            } else {
                mulFactor = 0;
            }
            String dummyStr1 = ""
                    + (allocationChargeWithoutDisc - discOnallocationCharge
                    * mulFactor);
            String dummyStr2 = dummyStr1 + "0000000000";
            int decIndex = dummyStr2.indexOf('.');
            String dummyStr3 = null;
            if (dummyStr2.substring(decIndex + 3, decIndex + 7).equals("9999")) {
                dummyStr3 = ("" + (Double.parseDouble(dummyStr2) + 0.00001))
                        .substring(0, decIndex + 5);
            } else {
                dummyStr3 = dummyStr2.substring(0, decIndex + 5);
            }
            return Double.parseDouble(dummyStr3);
        } else {
            return allocationChargeWithoutDisc;
        }
    }

    public int getCharge_PP_Ren(int fixedMonthlyExp_SP, int fixedMonthlyExp_RP,
                                int inflation_pa_SP, int inflation_pa_RP, String premFreqMode) {
        if (premFreqMode.equals("Single")) {
            return (fixedMonthlyExp_SP * 12 * (1 + inflation_pa_SP) ^ 0);
        }
        // For Regular
        else {
            return (fixedMonthlyExp_RP * 12 * (1 + inflation_pa_RP) ^ 0);
        }
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

    public String[] calSurrRateArrRP(double effectivePremium) {
        if (effectivePremium <= 25000) {
            return new String[]{"0.20", "0.15", "0.10", "0.05", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"};
        } else {
            return new String[]{"0.06", "0.04", "0.03", "0.02", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"};
        }
    }

    public String[] calSurrRateArrSP() {
        return new String[]{"0.01", "0.005", "0.0025", "0.001", "0", "0",
                "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                "0", "0", "0", "0", "0", "0", "0", "0"};
    }


    public String[] calCapArr(double effectivePremium) {
        if (effectivePremium <= 25000) {
            return new String[]{"3000", "2000", "1500", "1000", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0"};
        } else {
            return new String[]{"6000", "5000", "4000", "2000", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0"};
        }
    }

    public String[] getForBIArr(double mortalityCharges_AsPercentOfofLICtable) {
        int[] ageArr = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
                13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28,
                29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44,
                45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60,
                61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76,
                77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92,
                93, 94, 95, 96, 97, 98, 99, 100};
        String[] strArrTReturn = new String[99];
        SmartWealthAssureDB smartPerformerDB = new SmartWealthAssureDB();
        for (int i = 0; i < smartPerformerDB.getIAIarray().length - 2; i++) {
            if (ageArr[i] < 7) {
                strArrTReturn[i] = "0";
            } else {
                // System.out.println("   "+smartPerformerDB.getIAIarray()[i]);
//		            	System.out.println(((smartPerformerDB.getIAIarray()[i]) * mortalityCharges_AsPercentOfofLICtable * 1000  ));
                strArrTReturn[i] = cfap
                        .getRoundOffLevel2(cfap.getStringWithout_E((smartPerformerDB
                                .getIAIarray()[i])
                                * mortalityCharges_AsPercentOfofLICtable * 1000));
            }
        }
        return strArrTReturn;
    }

    // Added by Priyanka Warekar - 17-12-2014

    /************************************************** BL for Reduction yield starts here *******************************************************/

    public String getMortalityChargesReductionYield_T() {
        return mortalityChargesReductionYield_T;
    }

    public void setMortalityChargesReductionYield_T(double _fundValueAtEnd_AP,
                                                    int policyTerm, String[] forBIArray, int ageAtEntry,
                                                    double sumAssured, boolean mortalityCharges) {
        if (!(getPolicyInForce_G().equals("Y"))
                || Integer.parseInt(getYear_F()) > policyTerm) {
            this.mortalityChargesReductionYield_T = "" + 0;
        } else {
            double arrOutput = Double
                    .parseDouble(forBIArray[(ageAtEntry + Integer
                            .parseInt(getYear_F())) - 1]);
            // System.out.println("arrOutput "+arrOutput);
            double a = 1 - arrOutput / 1000;
            // double div=arrOutput/1000;
            // double div1=div/12;
            // double a=1-div1;
            double b = (double) 1 / 12;
            double c = Math
                    .max(sumAssured,
                            Double.parseDouble(getOneHundredPercentOfCummulativePremium_BW()))
                    - (Double.parseDouble(getAmountAvailableForInvestment_N()) + _fundValueAtEnd_AP);
            int d = 0;
            if (mortalityCharges) {
                d = 1;
            } else {
                d = 0;
            }
            this.mortalityChargesReductionYield_T = cfap.roundUp_Level2(cfap
                    .getStringWithout_E((((1 - (cfap.pow(a, b))) * Math.max(c,
                            0)) * d)));
        }
    }

    public String getMortalityChargesReductionYield_AU() {
        return mortalityChargesReductionYield_AU;
    }

    public void setMortalityChargesReductionYield_AU(double _fundValueAtEnd_BQ,
                                                     int policyTerm, String[] forBIArray, int ageAtEntry,
                                                     double sumAssured, boolean mortalityCharges) {
        if (!(getPolicyInForce_G().equals("Y"))
                || Integer.parseInt(getYear_F()) > policyTerm) {
            this.mortalityChargesReductionYield_AU = "" + 0;
        } else {
            double arrOutput = Double
                    .parseDouble(forBIArray[(ageAtEntry + Integer
                            .parseInt(getYear_F())) - 1]);
            // System.out.println("arrOutput "+arrOutput);
            double a = 1 - arrOutput / 1000;
            // double div=arrOutput/1000;
            // double div1=div/12;
            // double a=1-div1;
            double b = (double) 1 / 12;
            double c = Math
                    .max(sumAssured,
                            Double.parseDouble(getOneHundredPercentOfCummulativePremium_BW()))
                    - (Double.parseDouble(getAmountAvailableForInvestment_N()) + _fundValueAtEnd_BQ);
            int d = 0;
            if (mortalityCharges) {
                d = 1;
            } else {
                d = 0;
            }
            this.mortalityChargesReductionYield_AU = cfap.roundUp_Level2(cfap
                    .getStringWithout_E((((1 - (cfap.pow(a, b))) * Math.max(c,
                            0)) * d)));
        }
    }

    public String getTotalChargesReductionYield_V() {
        return totalChargesReductionYield_V;
    }

    public void setTotalChargesReductionYield_V(int policyTerm) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            // System.out.println("inside if");
            // System.out.println("S "+Double.parseDouble(getPolicyAdministrationCharge_S()));
            // System.out.println("T "+Double.parseDouble(getMortalityChargesReductionYield_T()));
            // System.out.println("Q "+Double.parseDouble(getSumAssuredRelatedCharges_Q()));
            // System.out.println("R "+Double.parseDouble(getOptionCharges_R()));
            // System.out.println("U "+Double.parseDouble(getGuaranteeCharge_U()));
            this.totalChargesReductionYield_V = ""
                    + (Double.parseDouble(getPolicyAdministrationCharge_S())
                    + Double.parseDouble(getMortalityChargesReductionYield_T())
                    + Double.parseDouble(getSumAssuredRelatedCharges_Q())
                    + Double.parseDouble(getOptionCharges_R()) * 0 + Double
                    .parseDouble(getGuaranteeCharge_U()));
            // System.out.println("after if");
        } else {
            this.totalChargesReductionYield_V = "" + 0;
        }
    }


    public String getTotalChargesReductionYield_AW() {
        return totalChargesReductionYield_AW;
    }

    public void setTotalChargesReductionYield_AW(int policyTerm) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.totalChargesReductionYield_AW = ""
                    + (Double.parseDouble(getPolicyAdministrationCharge_S())
                    + Double.parseDouble(getMortalityChargesReductionYield_AU())
                    + Double.parseDouble(getSumAssuredRelatedCharges_Q())
                    + Double.parseDouble(getOptionCharges_R()) * 0 + Double
                    .parseDouble(getGuaranteeCharge_AV()));
        } else {
            this.totalChargesReductionYield_AW = "" + 0;
        }
    }

    public String getTotalServiceTax_exclOfSTonAllocAndSurrReductionYield_W() {
        return totalServiceTax_exclOfSTonAllocAndSurrRedutionYield_W;
    }

    public void setTotalServiceTax_exclOfSTonAllocAndSurrReductionYield_W(
            double serviceTax, boolean mortalityAndRiderCharges,
            boolean administrationAndSArelatedCharges, boolean guaranteeCharges) {
        double a = 0, b = 0, c = 0;
        if (mortalityAndRiderCharges) {
            a = Double.parseDouble(getMortalityChargesReductionYield_T())
                    + Double.parseDouble(getOptionCharges_R());
        } else {
            a = 0;
        }
        if (administrationAndSArelatedCharges) {
            b = Double.parseDouble(getPolicyAdministrationCharge_S())
                    + Double.parseDouble(getSumAssuredRelatedCharges_Q());
        } else {
            b = 0;
        }
        if (guaranteeCharges) {
            c = Double.parseDouble(getGuaranteeCharge_U());
        } else {
            c = 0;
        }
        this.totalServiceTax_exclOfSTonAllocAndSurrRedutionYield_W = cfap
                .roundUp_Level2(cfap.getStringWithout_E((a + b + c)
                        * serviceTax));
    }

    public String getTotalServiceTax_ExclOfSTonAllocAndSurrReductionYield_AX() {
        return totalServiceTax_ExclOfSTonAllocAndsurrReductionYield_AX;
    }

    public void setTotalServiceTax_ExclOfSTonAllocAndSurrReductionYield_AX(
            double serviceTax, boolean mortalityAndRiderCharges,
            boolean administrationAndSArelatedCharges, boolean guaranteeCharges) {
        double a = 0, b = 0, c = 0;
        if (mortalityAndRiderCharges) {
            a = Double.parseDouble(getMortalityChargesReductionYield_AU())
                    + Double.parseDouble(getOptionCharges_R());
        } else {
            a = 0;
        }
        if (administrationAndSArelatedCharges) {
            b = Double.parseDouble(getPolicyAdministrationCharge_S())
                    + Double.parseDouble(getSumAssuredRelatedCharges_Q());
        } else {
            b = 0;
        }
        if (guaranteeCharges) {
            c = Double.parseDouble(getGuaranteeCharge_AV());
        } else {
            c = 0;
        }
        this.totalServiceTax_ExclOfSTonAllocAndsurrReductionYield_AX = cfap
                .roundUp_Level2(cfap.getStringWithout_E((a + b + c)
                        * serviceTax));
    }

    public String getFundUnderRGFafterChargesReductionYield_AB() {
        return fundUnderRGFafterChargesReductionYield_AB;
    }

    public void setFundUnderRGFafterChargesReductionYield_AB(
            boolean guaranteeCharges, double serviceTax) {
        double temp1 = 0;
        if (guaranteeCharges) {
            temp1 = serviceTax;
        } else {
            temp1 = 0;
        }

        if (getPolicyInForce_G().equals("Y")) {
            this.fundUnderRGFafterChargesReductionYield_AB = cfap
                    .roundUp_Level2(cfap.getStringWithout_E(Double
                            .parseDouble(getFundUnderRGFatStart_Z())
                            - ((Double
                            .parseDouble(getTotalChargesReductionYield_V())
                            + Double.parseDouble(getTotalServiceTax_exclOfSTonAllocAndSurrReductionYield_W()) - Double
                            .parseDouble(getGuaranteeCharge_U())
                            * (1 + temp1))
                            * Double.parseDouble(getFundUnderRGFatStart_Z()) / (Double
                            .parseDouble(getFundUnderRGFatStart_Z()) + Double
                            .parseDouble(getFundsUnderMarketLinkedReturnAtStart_AA())))
                            - Double.parseDouble(getGuaranteeCharge_U())
                            * (1 + temp1)));
        } else {
            this.fundUnderRGFafterChargesReductionYield_AB = "" + 0;

        }
    }

    public String getFundUnderRGFafterChargesReductionYield_BC() {
        return fundUnderRGFafterChargesReductionYield_BC;
    }

    public void setFundUnderRGFafterChargesReductionYield_BC(
            boolean guaranteeCharges, double serviceTax) {
        double temp1 = 0;
        if (guaranteeCharges) {
            temp1 = serviceTax;
        } else {
            temp1 = 0;
        }

        if (getPolicyInForce_G().equals("Y")) {
            this.fundUnderRGFafterChargesReductionYield_BC = cfap
                    .roundUp_Level2(cfap.getStringWithout_E(Double
                            .parseDouble(getFundUnderRGFatStart_BA())
                            - ((Double
                            .parseDouble(getTotalChargesReductionYield_AW())
                            + Double.parseDouble(getTotalServiceTax_ExclOfSTonAllocAndSurrReductionYield_AX()) - Double
                            .parseDouble(getGuaranteeCharge_AV())
                            * (1 + temp1))
                            * Double.parseDouble(getFundUnderRGFatStart_BA()) / (Double
                            .parseDouble(getFundUnderRGFatStart_BA()) + Double
                            .parseDouble(getFundsUnderMarketLinkedReturnAtStart_BB())))
                            - Double.parseDouble(getGuaranteeCharge_AV())
                            * (1 + temp1)));
        } else {
            this.fundUnderRGFafterChargesReductionYield_BC = "" + 0;
            ;
        }
    }


    public String getFundsUnderMarketLinkedReturnAtStartAfterChargesReductionYield_AC() {
        return fundsUnderMarketLinkedReturnAtStartAfterChargesReductionYield_AC;
    }

    public void setFundsUnderMarketLinkedReturnAtStartAfterChargesReductionYield_AC(
            boolean guaranteeCharges, double serviceTax) {
        double temp1 = 0;
        if (guaranteeCharges) {
            temp1 = serviceTax;
        } else {
            temp1 = 0;
        }

        if (getPolicyInForce_G().equals("Y")) {
            this.fundsUnderMarketLinkedReturnAtStartAfterChargesReductionYield_AC = cfap
                    .roundUp_Level2(cfap.getStringWithout_E(Double
                            .parseDouble(getFundsUnderMarketLinkedReturnAtStart_AA())
                            - ((Double
                            .parseDouble(getTotalChargesReductionYield_V())
                            + Double.parseDouble(getTotalServiceTax_exclOfSTonAllocAndSurrReductionYield_W()) - Double
                            .parseDouble(getGuaranteeCharge_U())
                            * (1 + temp1))
                            * Double.parseDouble(getFundsUnderMarketLinkedReturnAtStart_AA()) / (Double
                            .parseDouble(getFundUnderRGFatStart_Z()) + Double
                            .parseDouble(getFundsUnderMarketLinkedReturnAtStart_AA())))));
        } else {
            this.fundsUnderMarketLinkedReturnAtStartAfterChargesReductionYield_AC = "" + 0;
        }
    }

    public String getFundsUnderMarketLinkedReturnAtStartAfterChargesReductionYield_BD() {
        return fundsUnderMarketLinkedReturnAtStartAfterChargesReductionYield_BD;
    }

    public void setFundsUnderMarketLinkedReturnAtStartAfterChargesReductionYield_BD(
            boolean guaranteeCharges, double serviceTax) {
        double temp1 = 0;
        if (guaranteeCharges) {
            temp1 = serviceTax;
        } else {
            temp1 = 0;
        }

        if (getPolicyInForce_G().equals("Y")) {
            this.fundsUnderMarketLinkedReturnAtStartAfterChargesReductionYield_BD = cfap
                    .roundUp_Level2(cfap.getStringWithout_E(Double
                            .parseDouble(getFundsUnderMarketLinkedReturnAtStart_BB())
                            - ((Double
                            .parseDouble(getTotalChargesReductionYield_AW())
                            + Double.parseDouble(getTotalServiceTax_ExclOfSTonAllocAndSurrReductionYield_AX()) - Double
                            .parseDouble(getGuaranteeCharge_AV())
                            * (1 + temp1))
                            * Double.parseDouble(getFundsUnderMarketLinkedReturnAtStart_BB()) / (Double
                            .parseDouble(getFundUnderRGFatStart_BA()) + Double
                            .parseDouble(getFundsUnderMarketLinkedReturnAtStart_BB())))));
        } else {
            this.fundsUnderMarketLinkedReturnAtStartAfterChargesReductionYield_BD = "" + 0;
        }
    }

    public String getAdditionToFundUnderRGFReductionYield_AD() {
        return additionToFundUnderRGFReductionYield_AD;
    }

    public void setAdditionToFundUnderRGFReductionYield_AD(int policyTerm,
                                                           double int1) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.additionToFundUnderRGFReductionYield_AD = cfap
                    .roundUp_Level2(cfap.getStringWithout_E((Double
                            .parseDouble(getFundUnderRGFafterChargesReductionYield_AB()) * (cfap
                            .pow((1 + int1), (double) 1 / 12) - 1))));
        } else {
            this.additionToFundUnderRGFReductionYield_AD = "" + 0;
        }
    }

    public String getAdditionToFundUnderRGFReductionYield_BE() {
        return additionToFundUnderRGFReductionYield_BE;
    }

    public void setAdditionToFundUnderRGFReductionYield_BE(int policyTerm,
                                                           double int2) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.additionToFundUnderRGFReductionYield_BE = cfap
                    .roundUp_Level2(cfap.getStringWithout_E((Double
                            .parseDouble(getFundUnderRGFafterChargesReductionYield_BC()) * (cfap
                            .pow((1 + int2), (double) 1 / 12) - 1))));
        } else {
            this.additionToFundUnderRGFReductionYield_BE = "" + 0;
        }
    }


    public String getAdditionToFundsUnderMarketLinkedReturnReductionYield_AE() {
        return additionToFundsUnderMarketLinkedReturnReductionYield_AE;
    }

    public void setAdditionToFundsUnderMarketLinkedReturnReductionYield_AE(
            int policyTerm, double int1) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.additionToFundsUnderMarketLinkedReturnReductionYield_AE = cfap
                    .roundUp_Level2(cfap.getStringWithout_E((Double
                            .parseDouble(getFundsUnderMarketLinkedReturnAtStartAfterChargesReductionYield_AC()) * (cfap
                            .pow((1 + int1), (double) 1 / 12) - 1))));
        } else {
            this.additionToFundsUnderMarketLinkedReturnReductionYield_AE = "" + 0;
        }
    }

    public String getAdditionToFundsUnderMarketLinkedReturnReductionYield_BF() {
        return additionToFundsUnderMarketLinkedReturnReductionYield_BF;
    }

    public void setAdditionToFundsUnderMarketLinkedReturnReductionYield_BF(
            int policyTerm, double int2) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.additionToFundsUnderMarketLinkedReturnReductionYield_BF = cfap
                    .roundUp_Level2(cfap.getStringWithout_E((Double
                            .parseDouble(getFundsUnderMarketLinkedReturnAtStartAfterChargesReductionYield_BD()) * (cfap
                            .pow((1 + int2), (double) 1 / 12) - 1))));
        } else {
            this.additionToFundsUnderMarketLinkedReturnReductionYield_BF = "" + 0;
        }
    }

    public String getAdditionToFundIfAnyReductionYield_AH() {
        return additionToFundIfAnyReductionYield_AH;
    }

    public void setAdditionToFundIfAnyReductionYield_AH(int policyTerm) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.additionToFundIfAnyReductionYield_AH = cfap
                    .roundUp_Level2(cfap.getStringWithout_E((Double
                            .parseDouble(getAdditionToFundUnderRGFReductionYield_AD()) + Double
                            .parseDouble(getAdditionToFundsUnderMarketLinkedReturnReductionYield_AE()))));
        } else {
            this.additionToFundIfAnyReductionYield_AH = "" + 0;
        }
    }


    public String getAdditionToFundIfAnyReductionYield_BI() {
        return additionToFundIfAnyReductionYield_BI;
    }

    public void setAdditionToFundIfAnyReductionYield_BI(int policyTerm) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.additionToFundIfAnyReductionYield_BI = cfap
                    .roundUp_Level2(cfap.getStringWithout_E((Double
                            .parseDouble(getAdditionToFundUnderRGFReductionYield_BE()) + Double
                            .parseDouble(getAdditionToFundsUnderMarketLinkedReturnReductionYield_BF()))));
        } else {
            this.additionToFundIfAnyReductionYield_BI = "" + 0;
        }
    }

    public String getFMCearnedOnFundUnderRGFReductionYield_AF() {
        return fMCearnedOnFundUnderRGFReductionYield_AF;
    }

    public void setFMCearnedOnFundUnderRGFReductionYield_AF(int policyTerm,
                                                            double charge_Fund, double charge_Fund_Ren) {
        // Round Level 2
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            double temp1 = 0;
            if (Integer.parseInt(getMonth_E()) == 1) {
                temp1 = charge_Fund;
            } else {
                temp1 = 0;
            }
            this.fMCearnedOnFundUnderRGFReductionYield_AF = cfap
                    .roundUp_Level2(cfap.getStringWithout_E((Double
                            .parseDouble(getFundUnderRGFafterChargesReductionYield_AB()) + Double
                            .parseDouble(getAdditionToFundUnderRGFReductionYield_AD()))
                            * (temp1 + charge_Fund_Ren / 12)));
        } else {
            this.fMCearnedOnFundUnderRGFReductionYield_AF = "" + 0;
        }
    }

    public String getFMCearnedOnFundUnderRGFReductionYield_BG() {
        return fMCearnedOnFundUnderRGFReductionYield_BG;
    }

    public void setFMCearnedOnFundUnderRGFReductionYield_BG(int policyTerm,
                                                            double charge_Fund, double charge_Fund_Ren) {
        // Round Level 2
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            double temp1 = 0;
            if (Integer.parseInt(getMonth_E()) == 1) {
                temp1 = charge_Fund;
            } else {
                temp1 = 0;
            }
            this.fMCearnedOnFundUnderRGFReductionYield_BG = cfap
                    .roundUp_Level2(cfap.getStringWithout_E((Double
                            .parseDouble(getFundUnderRGFafterChargesReductionYield_BC()) + Double
                            .parseDouble(getAdditionToFundUnderRGFReductionYield_BE()))
                            * (temp1 + charge_Fund_Ren / 12)));
        } else {
            this.fMCearnedOnFundUnderRGFReductionYield_BG = "" + 0;
        }
    }

    public String getFundUnderRGFatEndReductionYield_AN() {
        return fundUnderRGFatEndReductionYield_AN;
    }

    public void setFundUnderRGFatEndReductionYield_AN(
            boolean fundManagementCharges, double serviceTax) {
        if (getPolicyInForce_G().equals("Y")) {
            double temp1 = 0, temp2 = 0, temp3 = 0;

            // temp1=(Double.parseDouble(getFundUnderRGFafterChargesReductionYield_AB())+Double.parseDouble(getAdditionToFundUnderRGF_AD()))*0.0135/12;
            if (fundManagementCharges) {
                temp2 = Double
                        .parseDouble(getFMCearnedOnFundUnderRGFReductionYield_AF());
            } else {
                temp2 = 0;
            }
            // temp3=Math.max(temp1, temp2);

            this.fundUnderRGFatEndReductionYield_AN = cfap
                    .roundUp_Level2(cfap.getStringWithout_E((Double
                            .parseDouble(getFundUnderRGFafterChargesReductionYield_AB())
                            + Double.parseDouble(getAdditionToFundUnderRGFReductionYield_AD())
                            - Double.parseDouble(getFMCearnedOnFundUnderRGFReductionYield_AF()) - temp2
                            * serviceTax)));
        } else {
            this.fundUnderRGFatEndReductionYield_AN = ("" + 0);
        }
    }

    public String getFundUnderRGFatEndReductionYield_BO() {
        return fundUnderRGFatEndReductionYield_BO;
    }

    public void setFundUnderRGFatEndReductionYield_BO(
            boolean fundManagementCharges, double serviceTax) {
        if (getPolicyInForce_G().equals("Y")) {
            double temp1 = 0, temp2 = 0, temp3 = 0;

            // temp1=(Double.parseDouble(getFundUnderRGFafterChargesReductionYield_AB())+Double.parseDouble(getAdditionToFundUnderRGF_AD()))*0.0135/12;
            if (fundManagementCharges) {
                temp2 = Double
                        .parseDouble(getFMCearnedOnFundUnderRGFReductionYield_AF());
            } else {
                temp2 = 0;
            }
            // temp3=Math.max(temp1, temp2);

            this.fundUnderRGFatEndReductionYield_BO = cfap
                    .roundUp_Level2(cfap.getStringWithout_E((Double
                            .parseDouble(getFundUnderRGFafterChargesReductionYield_BC())
                            + Double.parseDouble(getAdditionToFundUnderRGFReductionYield_BE())
                            - Double.parseDouble(getFMCearnedOnFundUnderRGFReductionYield_BG()) - temp2
                            * serviceTax)));
        } else {
            this.fundUnderRGFatEndReductionYield_BO = ("" + 0);
        }
    }

    public String getFMCearnedOnFundsUnderMarketLinkedReturnReductionYield_AG() {
        return fMCearnedOnFundsUnderMarketLinkedReturnReductionYield_AG;
    }

    public void setFMCearnedOnFundsUnderMarketLinkedReturnReductionYield_AG(int policyTerm, double charge_Fund, double returnGuaranteeFund, double bondFund, double equityFund, double PEmanagedFund, double percentToBeInvestedForBondFund, double percentToBeInvestedForEquityFund,
                                                                            double percentToBeInvested_BalancedFund,
                                                                            double percentToBeInvested_BondOptimiserFund,
                                                                            double percentToBeInvested_MoneyMarketFund,
                                                                            double percentToBeInvested_CorpBondFund,
                                                                            double percentToBeInvested_PureFund,
                                                                            double FMC_BalancedFund, double FMC_BondOptimiserFund, double FMC_MoneyMarketFund, double FMC_CorpBondFund, double FMC_PureFund) {
        double temp1 = 0, temp2 = 0;
        if (Integer.parseInt(getMonth_E()) == 1) {
            temp1 = charge_Fund;
        } else {
            temp1 = 0;
        }
        if (Integer.parseInt(getYear_F()) <= 10) {
//		        	temp2=getSumProduct1(bondFund,equityFund,PEmanagedFund,percentToBeInvestedForBondFund, percentToBeInvestedForEquityFund,percentToBeInvestedForPEmanagedFund);
            temp2 = getSumProduct1(bondFund, equityFund, percentToBeInvestedForBondFund, percentToBeInvestedForEquityFund,
                    percentToBeInvested_BalancedFund,
                    percentToBeInvested_BondOptimiserFund,
                    percentToBeInvested_MoneyMarketFund,
                    percentToBeInvested_CorpBondFund,
                    percentToBeInvested_PureFund,
                    FMC_BalancedFund, FMC_BondOptimiserFund, FMC_MoneyMarketFund, FMC_CorpBondFund, FMC_PureFund);
        } else {
//		        	temp2=getSumProduct2(returnGuaranteeFund,bondFund,equityFund,PEmanagedFund,percentToBeInvestedForReturnGuaranteedFund,percentToBeInvestedForBondFund, percentToBeInvestedForEquityFund,percentToBeInvestedForPEmanagedFund);
            temp2 = getSumProduct2(bondFund, equityFund, percentToBeInvestedForBondFund, percentToBeInvestedForEquityFund,
                    percentToBeInvested_BalancedFund,
                    percentToBeInvested_BondOptimiserFund,
                    percentToBeInvested_MoneyMarketFund,
                    percentToBeInvested_CorpBondFund,
                    percentToBeInvested_PureFund,
                    FMC_BalancedFund, FMC_BondOptimiserFund, FMC_MoneyMarketFund, FMC_CorpBondFund, FMC_PureFund);
        }

        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.fMCearnedOnFundsUnderMarketLinkedReturnReductionYield_AG = cfap
                    .roundUp_Level2(cfap.getStringWithout_E((Double
                            .parseDouble(getFundsUnderMarketLinkedReturnAtStartAfterChargesReductionYield_AC()) + Double
                            .parseDouble(getAdditionToFundsUnderMarketLinkedReturnReductionYield_AE()))
                            * (temp1 + temp2 / 12)));
        } else {
            this.fMCearnedOnFundsUnderMarketLinkedReturnReductionYield_AG = "" + 0;
        }
    }

    public String getFMCearnedOnFundsUnderMarketLinkedReturnReductionYield_BH() {
        return fMCearnedOnFundsUnderMarketLinkedReturnReductionYield_BH;
    }

    public void setFMCearnedOnFundsUnderMarketLinkedReturnReductionYield_BH(int policyTerm, double charge_Fund, double returnGuaranteeFund, double bondFund, double equityFund, double PEmanagedFund, double percentToBeInvestedForReturnGuaranteedFund, double percentToBeInvestedForBondFund, double percentToBeInvestedForEquityFund, double percentToBeInvestedForPEmanagedFund,
                                                                            double percentToBeInvested_BalancedFund,
                                                                            double percentToBeInvested_BondOptimiserFund,
                                                                            double percentToBeInvested_MoneyMarketFund,
                                                                            double percentToBeInvested_CorpBondFund,
                                                                            double percentToBeInvested_PureFund,
                                                                            double FMC_BalancedFund, double FMC_BondOptimiserFund, double FMC_MoneyMarketFund, double FMC_CorpBondFund, double FMC_PureFund) {
        double temp1 = 0, temp2 = 0;
        if (Integer.parseInt(getMonth_E()) == 1) {
            temp1 = charge_Fund;
        } else {
            temp1 = 0;
        }
        if (Integer.parseInt(getYear_F()) <= 10) {
            temp2 = getSumProduct1(bondFund, equityFund, percentToBeInvestedForBondFund, percentToBeInvestedForEquityFund,
                    percentToBeInvested_BalancedFund,
                    percentToBeInvested_BondOptimiserFund,
                    percentToBeInvested_MoneyMarketFund,
                    percentToBeInvested_CorpBondFund,
                    percentToBeInvested_PureFund,
                    FMC_BalancedFund, FMC_BondOptimiserFund, FMC_MoneyMarketFund, FMC_CorpBondFund, FMC_PureFund);
        } else {
            temp2 = getSumProduct2(bondFund, equityFund, percentToBeInvestedForBondFund, percentToBeInvestedForEquityFund,
                    percentToBeInvested_BalancedFund,
                    percentToBeInvested_BondOptimiserFund,
                    percentToBeInvested_MoneyMarketFund,
                    percentToBeInvested_CorpBondFund,
                    percentToBeInvested_PureFund,
                    FMC_BalancedFund, FMC_BondOptimiserFund, FMC_MoneyMarketFund, FMC_CorpBondFund, FMC_PureFund);
        }

        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.fMCearnedOnFundsUnderMarketLinkedReturnReductionYield_BH = cfap
                    .roundUp_Level2(cfap.getStringWithout_E((Double
                            .parseDouble(getFundsUnderMarketLinkedReturnAtStartAfterChargesReductionYield_BD()) + Double
                            .parseDouble(getAdditionToFundsUnderMarketLinkedReturnReductionYield_BF()))
                            * (temp1 + temp2 / 12)));
        } else {
            this.fMCearnedOnFundsUnderMarketLinkedReturnReductionYield_BH = "" + 0;
        }
    }

    public String getFundsUnderMarketLinkedReturnAtEndReductionYield_AO() {
        return fundsUnderMarketLinkedReturnAtEndReductionYield_AO;
    }

    public void setfundsUnderMarketLinkedReturnAtEndReductionYield_AO(
            boolean fundManagementCharges, double serviceTax) {
        if (getPolicyInForce_G().equals("Y")) {
            double temp1 = 0, temp2 = 0, temp3 = 0;

            // temp1=(Double.parseDouble(getFundsUnderMarketLinkedReturnAtStartAfterCharges_AC())+Double.parseDouble(getAdditionToFundsUnderMarketLinkedReturn_AE()))*0.0135/12;
            if (fundManagementCharges) {
                temp2 = Double
                        .parseDouble(getFMCearnedOnFundsUnderMarketLinkedReturnReductionYield_AG());
            } else {
                temp2 = 0;
            }
            // temp3=Math.max(temp1, temp2);
            this.fundsUnderMarketLinkedReturnAtEndReductionYield_AO = ""
                    + (Double
                    .parseDouble(getFundsUnderMarketLinkedReturnAtStartAfterChargesReductionYield_AC())
                    + Double.parseDouble(getAdditionToFundsUnderMarketLinkedReturnReductionYield_AE())
                    - Double.parseDouble(getFMCearnedOnFundsUnderMarketLinkedReturnReductionYield_AG()) - temp2
                    * serviceTax);
        } else {
            this.fundsUnderMarketLinkedReturnAtEndReductionYield_AO = ("" + 0);
        }
    }

    public String getFundsUnderMarketLinkedReturnAtEndReductionYield_BP() {
        return fundsUnderMarketLinkedReturnAtEndReductionYield_BP;
    }

    public void setfundsUnderMarketLinkedReturnAtEndReductionYield_BP(
            boolean fundManagementCharges, double serviceTax) {
        if (getPolicyInForce_G().equals("Y")) {
            double temp1 = 0, temp2 = 0, temp3 = 0;

            // temp1=(Double.parseDouble(getFundsUnderMarketLinkedReturnAtStartAfterChargesReductionYield_BD())+Double.parseDouble(getAdditionToFundsUnderMarketLinkedReturn_BF()))*0.0135/12;
            if (fundManagementCharges) {
                temp2 = Double
                        .parseDouble(getFMCearnedOnFundsUnderMarketLinkedReturn_BH());
            } else {
                temp2 = 0;
            }
            // temp3=Math.max(temp1, temp2);
            this.fundsUnderMarketLinkedReturnAtEndReductionYield_BP = ""
                    + (Double
                    .parseDouble(getFundsUnderMarketLinkedReturnAtStartAfterChargesReductionYield_BD())
                    + Double.parseDouble(getAdditionToFundsUnderMarketLinkedReturnReductionYield_BF())
                    - Double.parseDouble(getFMCearnedOnFundsUnderMarketLinkedReturnReductionYield_BH()) - temp2
                    * serviceTax);
        } else {
            this.fundsUnderMarketLinkedReturnAtEndReductionYield_BP = ("" + 0);
        }
    }

    public String getFundValueAtEndReductionYield_AP() {
        return fundValueAtEndReductionYield_AP;
    }

    public void setFundValueAtEndReductionYield_AP() {
        this.fundValueAtEndReductionYield_AP = ""
                + (Double.parseDouble(getGuaranteedAddition_AM())
                + Double.parseDouble(getFundUnderRGFatEndReductionYield_AN()) + Double
                .parseDouble(getFundsUnderMarketLinkedReturnAtEndReductionYield_AO()));
    }

    public String getFundValueAtEndReductionYield_BQ() {
        return fundValueAtEndReductionYield_BQ;
    }

    public void setFundValueAtEndReductionYield_BQ() {
        this.fundValueAtEndReductionYield_BQ = ""
                + (Double.parseDouble(getGuaranteedAddition_BN())
                + Double.parseDouble(getFundUnderRGFatEndReductionYield_BO()) + Double
                .parseDouble(getFundsUnderMarketLinkedReturnAtEndReductionYield_BP()));
    }


    public String getFundBeforeFMCReductionYield_AI() {
        return fundBeforeFMCReductionYield_AI;
    }

    public void setFundBeforeFMCReductionYield_AI(int policyTerm) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.fundBeforeFMCReductionYield_AI = ""
                    + (Double
                    .parseDouble(getFundUnderRGFafterChargesReductionYield_AB())
                    + Double.parseDouble(getFundsUnderMarketLinkedReturnAtStartAfterChargesReductionYield_AC()) + Double
                    .parseDouble(getAdditionToFundIfAnyReductionYield_AH()));
        } else {
            this.fundBeforeFMCReductionYield_AI = "" + 0;
        }
    }

    public String getFundBeforeFMCReductionYield_BJ() {
        return fundBeforeFMCReductionYield_BJ;
    }

    public void setFundBeforeFMCReductionYield_BJ(int policyTerm) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.fundBeforeFMCReductionYield_BJ = ""
                    + (Double
                    .parseDouble(getFundUnderRGFafterChargesReductionYield_BC())
                    + Double.parseDouble(getFundsUnderMarketLinkedReturnAtStartAfterChargesReductionYield_BD()) + Double
                    .parseDouble(getAdditionToFundIfAnyReductionYield_BI()));
        } else {
            this.fundBeforeFMCReductionYield_BJ = "" + 0;
        }
    }

    public String getFundManagementChargeReductionYield_AJ() {
        return fundManagementChargeReductionYield_AJ;
    }

    public void setFundManagementChargeReductionYield_AJ() {
        this.fundManagementChargeReductionYield_AJ = cfap
                .roundUp_Level2(cfap.getStringWithout_E(Double
                        .parseDouble(getFMCearnedOnFundUnderRGFReductionYield_AF())
                        + Double.parseDouble(getFMCearnedOnFundsUnderMarketLinkedReturnReductionYield_AG())));
    }


    public String getFundManagementChargeReductionYield_BK() {
        return fundManagementChargeReductionYield_BK;
    }

    public void setFundManagementChargeReductionYield_BK() {
        this.fundManagementChargeReductionYield_BK = cfap
                .roundUp_Level2(cfap.getStringWithout_E(Double
                        .parseDouble(getFMCearnedOnFundUnderRGFReductionYield_BG())
                        + Double.parseDouble(getFMCearnedOnFundsUnderMarketLinkedReturnReductionYield_BH())));
    }


    public String getServiceTaxOnFMCReductionYield_AK() {
        return serviceTaxOnFMCReductionYield_AK;
    }

    public void setServiceTaxOnFMCReductionYield_AK(
            boolean fundManagementCharges, double serviceTax) {
        double temp1 = 0, temp2 = 0, temp3 = 0;
        // temp1=(Double.parseDouble(getFundBeforeFMC_AI()) * 0.0135)/12;
        if (fundManagementCharges) {
            temp2 = Double
                    .parseDouble(getFundManagementChargeReductionYield_AJ());
        } else {
            temp2 = 0;
        }
        // temp3=Math.max(temp1, temp2);
        this.serviceTaxOnFMCReductionYield_AK = cfap.roundUp_Level2(cfap
                .getStringWithout_E(temp2 * serviceTax));
    }

    public String getServiceTaxOnFMCReductionYield_BL() {
        return serviceTaxOnFMCReductionYield_BL;
    }

    public void setServiceTaxOnFMCReductionYield_BL(
            boolean fundManagementCharges, double serviceTax) {
        double temp1 = 0, temp2 = 0, temp3 = 0;
        //    temp1=(Double.parseDouble(getFundBeforeFMCReductionYield_BJ()) * 0.0135)/12;
        if (fundManagementCharges) {
            temp2 = Double
                    .parseDouble(getFundManagementChargeReductionYield_BK());
        } else {
            temp2 = 0;
        }
        // temp3=Math.max(temp1, temp2);
        this.serviceTaxOnFMCReductionYield_BL = cfap.roundUp_Level2(cfap
                .getStringWithout_E(temp2 * serviceTax));
    }

    public String getTotalServiceTaxReductionYield_X() {
        return totalServiceTaxReductionYield_X;
    }

    public void setTotalServiceTaxReductionYield_X() {
        this.totalServiceTaxReductionYield_X = cfap
                .roundUp_Level2(cfap.getStringWithout_E(Double
                        .parseDouble(getServiceTaxOnAllocation_M())
                        + Double.parseDouble(getTotalServiceTax_exclOfSTonAllocAndSurrReductionYield_W())
                        + Double.parseDouble(getServiceTaxOnFMCReductionYield_AK())));
    }

    public String getTotalServiceTaxReductionYield_AY() {
        return totalServiceTaxReductionYield_AY;
    }

    public void setTotalServiceTaxReductionYield_AY() {
        this.totalServiceTaxReductionYield_AY = cfap
                .roundUp_Level2(cfap.getStringWithout_E(Double
                        .parseDouble(getServiceTaxOnAllocation_M())
                        + Double.parseDouble(getTotalServiceTax_ExclOfSTonAllocAndSurrReductionYield_AX())
                        + Double.parseDouble(getServiceTaxOnFMCReductionYield_BL())));
    }

    public String getFundValueAfterFMCandBeforeGAReductionYield_AL() {
        return fundValueAfterFMCandBeforeGAReductionYield_AL;
    }

    public void setFundValueAfterFMCandBeforeGAReductionYield_AL(int policyTerm) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.fundValueAfterFMCandBeforeGAReductionYield_AL = ""
                    + (Double.parseDouble(getFundBeforeFMCReductionYield_AI())
                    - Double.parseDouble(getFundManagementChargeReductionYield_AJ()) - Double
                    .parseDouble(getServiceTaxOnFMCReductionYield_AK()));
        } else {
            this.fundValueAfterFMCandBeforeGAReductionYield_AL = "" + 0;
        }
    }

    public String getFundValueAfterFMCandBeforeGAReductionYield_BM() {
        return fundValueAfterFMCandBeforeGAReductionYield_BM;
    }

    public void setFundValueAfterFMCandBeforeGAReductionYield_BM(int policyTerm) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.fundValueAfterFMCandBeforeGAReductionYield_BM = ""
                    + (Double.parseDouble(getFundBeforeFMCReductionYield_BJ())
                    - Double.parseDouble(getFundManagementChargeReductionYield_BK()) - Double
                    .parseDouble(getServiceTaxOnFMCReductionYield_BL()));
        } else {
            this.fundValueAfterFMCandBeforeGAReductionYield_BM = "" + 0;
        }
    }

    public String getSurrenderChargesReductionYield_AQ() {
        return surrenderChargesReductionYield_AQ;
    }

    public void setSurrenderChargesReductionYield_AQ(double effectivePremium,
                                                     String premFreqMode) {
        double a = Math.min(
                Double.parseDouble(getFundValueAtEndReductionYield_AP()),
                effectivePremium);
        double b = getSurrenderCharge(premFreqMode, effectivePremium);
        this.surrenderChargesReductionYield_AQ = cfap.roundUp_Level2(cfap
                .getStringWithout_E(Math.min((a * b),
                        Double.parseDouble(getSurrenderCap_BV()))));
    }


    public String getSurrenderChargesReductionYield_BR() {
        return surrenderChargesReductionYield_BR;
    }

    public void setSurrenderChargesReductionYield_BR(double effectivePremium,
                                                     String premFreqMode) {
        double a = Math.min(
                Double.parseDouble(getFundValueAtEndReductionYield_BQ()),
                effectivePremium);
        double b = getSurrenderCharge(premFreqMode, effectivePremium);
        this.surrenderChargesReductionYield_BR = cfap.roundUp_Level2(cfap
                .getStringWithout_E(Math.min((a * b),
                        Double.parseDouble(getSurrenderCap_BV()))));
    }


    public String getServiceTaxOnSurrenderChargesReductionYield_AR() {
        return serviceTaxOnSurrenderChargesReductionYield_AR;
    }

    public void setServiceTaxOnSurrenderChargesReductionYield_AR(
            boolean surrenderCharges, double serviceTax) {
        if (surrenderCharges) {
            this.serviceTaxOnSurrenderChargesReductionYield_AR = cfap
                    .roundUp_Level2(cfap.getStringWithout_E(Double
                            .parseDouble(getSurrenderChargesReductionYield_AQ())
                            * serviceTax));
        } else {
            this.serviceTaxOnSurrenderChargesReductionYield_AR = "" + 0;
        }
    }

    public String getServiceTaxOnSurrenderChargesReductionYield_BS() {
        return serviceTaxOnSurrenderChargesReductionYield_BS;
    }

    public void setServiceTaxOnSurrenderChargesReductionYield_BS(
            boolean surrenderCharges, double serviceTax) {
        if (surrenderCharges) {
            this.serviceTaxOnSurrenderChargesReductionYield_BS = cfap
                    .roundUp_Level2(cfap.getStringWithout_E(Double
                            .parseDouble(getSurrenderChargesReductionYield_BR())
                            * serviceTax));
        } else {
            this.serviceTaxOnSurrenderChargesReductionYield_BS = "" + 0;
        }
    }

    public String getSurrenderValueReductionYield_AS() {
        return surrenderValue_AS;
    }

    public void setSurrenderValueReductionYield_AS() {
        this.surrenderValue_AS = ""
                + (Double.parseDouble(getFundValueAtEndReductionYield_AP())
                - Double.parseDouble(getSurrenderChargesReductionYield_AQ()) - Double
                .parseDouble(getServiceTaxOnSurrenderChargesReductionYield_AR()));
    }

    public String getSurrenderValueReductionYield_BT() {
        return surrenderValue_BT;
    }

    public void setSurrenderValueReductionYield_BT() {
        this.surrenderValue_BT = ""
                + (Double.parseDouble(getFundValueAtEndReductionYield_BQ())
                - Double.parseDouble(getSurrenderChargesReductionYield_BR()) - Double
                .parseDouble(getServiceTaxOnSurrenderChargesReductionYield_BS()));
    }

    public String getDeathBenefitReductionYield_AT() {
        return deathBenefit_AT;
    }

    public void setDeathBenefitReductionYield_AT(int policyTerm,
                                                 double sumAssured) {
        if (Integer.parseInt(getYear_F()) > policyTerm) {
            this.deathBenefit_AT = "" + 0;
        } else {
            this.deathBenefit_AT = ""
                    + (Math.max(
                    Double.parseDouble(getOneHundredPercentOfCummulativePremium_BW()),
                    Math.max(
                            sumAssured,
                            Double.parseDouble(getFundValueAtEndReductionYield_AP()))));
        }
    }

    public String getDeathBenefitReductionYield_BU() {
        return deathBenefit_BU;
    }

    public void setDeathBenefitReductionYield_BU(int policyTerm,
                                                 double sumAssured) {
        if (Integer.parseInt(getYear_F()) > policyTerm) {
            this.deathBenefit_BU = "" + 0;
        } else {
            this.deathBenefit_BU = ""
                    + (Math.max(
                    Double.parseDouble(getOneHundredPercentOfCummulativePremium_BW()),
                    Math.max(
                            sumAssured,
                            Double.parseDouble(getFundValueAtEndReductionYield_BQ()))));
        }
    }

    public double getCommission_AQ(double annualisedPrem, double topupPrem,
                                   SmartWealthAssureBean smartWealthAssureBean) {
        double topup = 0.01;
//				System.out.println("getCommistionRate "+getCommistionRate(smartWealthAssureBean) + " "+annualisedPrem+" "+topupPrem);
        if (smartWealthAssureBean.getIsForStaffOrNot())
            return 0;
        else
            return getCommistionRate(smartWealthAssureBean) * annualisedPrem
                    + topupPrem * topup;

    }

    public double getCommistionRate(SmartWealthAssureBean smartWealthAssureBean) {
        int findYear;

        if (Integer.parseInt(getYear_F()) > 10) {
            findYear = 11;
        } else
            findYear = Integer.parseInt(getYear_F());

        if (smartWealthAssureBean.getIsForStaffOrNot()) {
            return 0;
        } else {
            double[] commArr = {0.02, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                    0.0, 0.0};
            double temp = commArr[findYear - 1];
            // System.out.println("temp : "+temp);

            return commArr[findYear - 1];
        }
    }

    public void setMonth_CB(int monthNumber) {
        this.month_CB = "" + monthNumber;
    }

    public String getMonth_CB() {
        return month_CB;
    }

    public void setReductionYield_CC(int policyTerm, double _fundValueAtEnd_AD) {
        double a, b;
        //     if((Integer.parseInt(getMonth_E())) <= (noOfYearsElapsedSinceInception*12)+1)
        if ((Integer.parseInt(getMonth_CB())) < (policyTerm * 12)) {
            a = -(Double.parseDouble(getPremium_I()));
        } else {
            a = 0;
        }

        if ((Integer.parseInt(getMonth_CB())) == (policyTerm * 12)) {
            b = _fundValueAtEnd_AD;
        } else {
            b = 0;
        }
        // System.out.println("a_BB "+a);
        // System.out.println("b_BB "+b);
        this.reductionYield_CC = "" + (a + b);
    }

    public String getReductionYield_CC() {
        return reductionYield_CC;
    }

    public void setReductionYield_CD(int policyTerm, double _fundValueAtEnd_AD) {
        double a, b;
        //     if((Integer.parseInt(getMonth_E())) <= (noOfYearsElapsedSinceInception*12)+1)
        if ((Integer.parseInt(getMonth_CB())) < (policyTerm * 12)) {
            a = -(Double.parseDouble(getPremium_I()));
        } else {
            a = 0;
        }

        if ((Integer.parseInt(getMonth_CB())) == (policyTerm * 12)) {
            b = _fundValueAtEnd_AD;
        } else {
            b = 0;
        }
        // System.out.println("a_BB "+a);
        // System.out.println("b_BB "+b);
        this.reductionYield_CD = "" + (a + b);
    }

    public String getReductionYield_CD() {
        return reductionYield_CD;
    }

    public void setReductionYield_CI(int policyTerm, double _fundValueAtEnd_AD) {
        double a, b;
        //     if((Integer.parseInt(getMonth_E())) <= (noOfYearsElapsedSinceInception*12)+1)
        if ((Integer.parseInt(getMonth_CB())) < (policyTerm * 12)) {
            a = -(Double.parseDouble(getPremium_I()));
        } else {
            a = 0;
        }

        if ((Integer.parseInt(getMonth_CB())) == (policyTerm * 12)) {
            b = _fundValueAtEnd_AD;
        } else {
            b = 0;
        }
        // System.out.println("a_BB "+a);
        // System.out.println("b_BB "+b);
        this.reductionYield_CI = "" + (a + b);
    }

    public String getReductionYield_CI() {
        return reductionYield_CI;
    }

    public void setIRRAnnual_CC(double ans) {
        // System.out.println("aaaaaaa "+((comm.pow((1+ans),12)-1)*100));
        this.irrAnnual_CC = "" + ((cfap.pow((1 + ans), 12) - 1));
    }

    public String getIRRAnnual_CC() {
        return irrAnnual_CC;
    }

    public void setIRRAnnual_CD(double ans) {
        // System.out.println("aaaaaaa "+((comm.pow((1+ans),12)-1)*100));
        this.irrAnnual_CD = "" + ((cfap.pow((1 + ans), 12) - 1));
    }

    public String getIRRAnnual_CD() {
        return irrAnnual_CD;
    }

    public void setIRRAnnual_CI(double ans) {
        // System.out.println("aaaaaaa "+((comm.pow((1+ans),12)-1)*100));
        this.irrAnnual_CI = "" + ((cfap.pow((1 + ans), 12) - 1));
    }

    public String getIRRAnnual_CI() {
        return irrAnnual_CI;
    }

    public void setReductionInYieldMaturityAt(double int2) {
        this.reductionInYieldMaturityAt = ""
                + ((int2 - Double.parseDouble(getIRRAnnual_CD())) * 100);
    }

    public String getReductionInYieldMaturityAt() {
        return reductionInYieldMaturityAt;
    }

    public void setReductionInYieldNumberOfYearsElapsedSinceInception(
            double int2) {
        this.reductionInYieldNumberOfYearsElapsedSinceInception = ""
                + ((int2 - Double.parseDouble(getIRRAnnual_CI())) * 100);
    }

    public String getReductionInYieldNumberOfYearsElapsedSinceInception() {
        return reductionInYieldNumberOfYearsElapsedSinceInception;
    }

    public String getnetYieldAt4Percent() {
        return netYieldAt4Percent;
    }

    public void setnetYieldAt4Percent() {
        this.netYieldAt4Percent = "" + Double.parseDouble(getIRRAnnual_CC())
                * 100;
    }

    public String getnetYieldAt8Percent() {
        return netYieldAt8Percent;
    }

    public void setnetYieldAt8Percent() {
        this.netYieldAt8Percent = "" + Double.parseDouble(getIRRAnnual_CD())
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

            // the value of the function (NPV) and its derivate can be calculated in the same loop
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
