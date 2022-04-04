/***************************************************************************************************************
 Author-> Akshaya Mirajkar
 ***************************************************************************************************************/
package sbilife.com.pointofsale_bancaagency.smartscholar;

class SmartScholarProperties {
    public int minAgeLimitForChild = 0, maxAgeLimitForChild = 17,
            minAgeLimitForProposer = 18, maxAgeLimitForProposer = 57, PPT = 8,
            premMult = 100, Gap_Loyalty_Add = 3, firstGAinYr = 8;

    public double effectiveTopUpPrem = 0,
            topUp = 0.0,

    // serviceTax=0.1236,
    /**
     * New ST from 1st June 2016
     */

    // As Per GST
//            serviceTax=0.18,
    // serviceTax=0.15,
    chargeSAren = 0, accidentalBenCharge = 0.5,
            monthlyAdminCharge = 50.0, charge_Fund = 0, minTerm = 0,
            loyalty = 0.01, allocNormalLPPTYr1 = 0.06,
            allocNormalLPPTYr2 = 0.045, allocNormalLPPTYr3 = 0.045,
            allocNormalLPPTYr4 = 0.04, allocNormalLPPTYr5 = 0.04,
            allocNormalLPPTYr6 = 0.01, allocNormalLPPTYr7 = 0.01,
            allocNormalLPPTYr8 = 0.01, allocNormalLPPTYr9 = 0.01,
            allocNormalLPPTYr10 = 0.01, allocCommisionLPPTYr1 = 0.055,
            allocCommisionLPPTYr2 = 0.03, allocCommisionLPPTYr3 = 0.03,
            allocCommisionLPPTYr4 = 0.025, allocCommisionLPPTYr5 = 0.025,
            allocCommisionLPPTYr6 = 0.01, allocCommisionLPPTYr7 = 0.01,
            allocCommisionLPPTYr8 = 0.01, allocCommisionLPPTYr9 = 0.01,
            allocCommisionLPPTYr10 = 0.01, allocNormalSingleYr1 = 0.03,
            allocNormalSingleYr2 = 0, allocNormalSingleYr3 = 0,
            allocNormalSingleYr4 = 0, allocNormalSingleYr5 = 0,
            allocNormalSingleYr6 = 0, allocNormalSingleYr7 = 0,
            allocNormalSingleYr8 = 0, allocNormalSingleYr9 = 0,
            allocNormalSingleYr10 = 0, allocCommisionSingleYr1 = 0.02,
            allocCommisionSingleYr2 = 0, allocCommisionSingleYr3 = 0,
            allocCommisionSingleYr4 = 0, allocCommisionSingleYr5 = 0,
            allocCommisionSingleYr6 = 0, allocCommisionSingleYr7 = 0,
            allocCommisionSingleYr8 = 0, allocCommisionSingleYr9 = 0,
            allocCommisionSingleYr10 = 0, premLimitForSurrCharge = 25000,
            surrPremLessThanOrEqTo25000Yr1 = 3000,
            surrPremLessThanOrEqTo25000Yr2 = 2000,
            surrPremLessThanOrEqTo25000Yr3 = 1500,
            surrPremLessThanOrEqTo25000Yr4 = 1000,
            surrPremLessThanOrEqTo25000Yr5 = 0,
            surrPremLessThanOrEqTo25000Yr6 = 0,
            surrPremLessThanOrEqTo25000Yr7 = 0,
            surrPremLessThanOrEqTo25000Yr8 = 0,
            surrPremLessThanOrEqTo25000Yr9 = 0,
            surrPremLessThanOrEqTo25000Yr10 = 0,
            surrPremGreaterThan25000Yr1 = 6000,
            surrPremGreaterThan25000Yr2 = 5000,
            surrPremGreaterThan25000Yr3 = 4000,
            surrPremGreaterThan25000Yr4 = 2000,
            surrPremGreaterThan25000Yr5 = 0, surrPremGreaterThan25000Yr6 = 0,
            surrPremGreaterThan25000Yr7 = 0, surrPremGreaterThan25000Yr8 = 0,
            surrPremGreaterThan25000Yr9 = 0, surrPremGreaterThan25000Yr10 = 0,
            FMC_EquityFund = 0.0135, FMC_EquityOptimiserFund = 0.0135,
            FMC_GrowthFund = 0.0135, FMC_BalancedFund = 0.0125,
            FMC_BondFund = 0.01, FMC_MoneyMarketFund = 0.0025,
            FMC_BondOptimiserFundII = 0.0135,
            FMC_BondOptimiserFund = 0.0115,
            FMC_Top300Fund = 0.0135,
            FMC_PureFund = 0.0135;

    public boolean allocationCharges = false, optionCharges = false,
            accidentalBenefit = true,
    /*mortalityCharges=false,*/
    mortalityCharges = true,
            PPWBstatus = true,
    // surrenderCharges=false,
    surrenderCharges = true,
            riderCharges = true,
            Loyalty_Add = true;

    public int minPremForYearly_PPTlessThan8 = 50000,
            minPremForHalfYearly_PPTlessThan8 = 25000,
            minPremForQuarterly_PPTlessThan8 = 12500,
            minPremForMonthly_PPTlessThan8 = 4500, minPremForSingle = 75000,
            minPremForYearly_PPTeqOrgreaterThan8 = 24000,
            minPremForHalfYearly_PPTeqOrgreaterThan8 = 16000,
            minPremForQuarterly_PPTeqOrgreaterThan8 = 10000,
            minPremForMonthly_PPTeqOrgreaterThan8 = 4000;

    public double
            // int1=0.06,
            // int2=0.1,
            int1 = 0.04,
            int2 = 0.08, charge_SumAssuredBase = 0,
    // mortalityCharges_AsPercentOfofLICtable=1;
    mortalityCharges_AsPercentOfofLICtable = 1.2;

    public boolean mortalityAndRiderCharges = false,
            administrationCharges = false, fundManagementCharges = false,
            guaranteeCharges = false, topUpStatus = false;

    // Constructor
    public SmartScholarProperties() {
    }

}
