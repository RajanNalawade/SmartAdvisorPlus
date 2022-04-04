package sbilife.com.pointofsale_bancaagency.smartelite;

class BI_SmartEliteProperties {
    // 1.Date of Launch Settings
    int monthOfLaunch = 12, dayOfLaunch = 15, yearOfLaunch = 2009;
    // 2.Date of Inception Settings
    int monthOfInception = 2, dayOfInception = 9, yearOfInception = 2012;

    public int minAgeLimit = 18, maxAgeLimit = 60, minPolicyTerm = 5,
            charge_Inflation = 0, fixedMonthlyExp_RP = 60,
            fixedMonthlyExp_SP = 50, inflation_pa_RP = 0, inflation_pa_SP = 0;

    public double topUpPremiumAmt = 25000,
            topUp = 0.02,
    // serviceTax=0.1236,
    /**
     * New ST from 1st june 2016
     */
    //As Per GST
//					 Commented By Saurabh Jain and added in Bean class serviceTax=0.18 on 13/05/2019 Start
    //serviceTax=0.18,
//            		 Commented By Saurabh Jain and added in Bean class serviceTax=0.18 on 13/05/2019 End
//			serviceTax = 0.15,
    charge_SumAssuredBase = 0,
            ADBandATPDCharge = 0.0005,
    // mortalityCharges_AsPercentOfofLICtable=1,
//			mortalityCharges_AsPercentOfofLICtable = 1.2,

    //			Added By Saurabh Jain on 17/12/2019
//					mortalityCharges_AsPercentOfofLICtable = 1.1,
    mortalityCharges_AsPercentOfofLICtable = 1.2,
    // int1=0.06,
    // int2=0.1,
    int1 = 0.04, int2 = 0.08,
            charge_Fund = 0,
    // charge_Fund_Ren=0.0135,
    charge_Fund_Ren = 0.0125,

    //			FMC_EquityEliteIIFund = 0.0135,
    FMC_EquityEliteIIFund = 0.0125,
    //					FMC_BalancedFund = 0.0135,
    FMC_BalancedFund = 0.0125,
    //			FMC_BondFund = 0.0135,
    FMC_BondFund = 0.01,
    //							FMC_MoneyMarketFund = 0.0075,
    FMC_MoneyMarketFund = 0.0025,
    //			FMC_BondOptimiserFund =0.0135,
    FMC_BondOptimiserFund = 0.0115,
            FMC_EquityFund = 0.0115, /* This fund used as a corp bond fund */
            FMC_MidcapFund = 0.0135, FMC_PureFund = 0.0135;
//			FMC_IndexFund = 0.0, FMC_PEmanagedFund = 0.0;

    public boolean topUpStatus = true,
            allocationCharges = true,
            mortalityCharges = true,
            mortalityAndRiderCharges = true,
            administrationAndSArelatedCharges = true,
            fundManagementCharges = true,
            riderCharges = true,
    // surrenderCharges=false;
    surrenderCharges = true, allocationChargesReductionYield = false,
            mortalityChargesReductionYield = false,
            mortalityAndRiderChargesReductionYield = false,
            administrationAndSArelatedChargesReductionYield = false,
            fundManagementChargesReductionYield = false,
            riderChargesReductionYield = false,
            surrenderChargesReductionYield = false;

    public int noOfYearsForSArelatedCharges = 3;

    public double indexFund = 0.0125;

    // *********************************************************************************************************
    // Constructor
    public BI_SmartEliteProperties() {
    }

}
