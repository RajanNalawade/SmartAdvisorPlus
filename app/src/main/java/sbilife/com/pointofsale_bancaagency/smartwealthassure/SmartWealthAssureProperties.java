/***************************************************************************************************************
 Author-> Akshaya Mirajkar
 ***************************************************************************************************************/
package sbilife.com.pointofsale_bancaagency.smartwealthassure;

class SmartWealthAssureProperties {
    public int minAgeLimit = 8, maxAgeLimit = 65, minPolicyTerm = 10,
            minPremAmt = 50000, d25 = 5000, year_TransferOfCapital_W62 = 5,
            year_TransferOfCapital_W63 = 6, charge_Inflation = 0,
            noOfYearsForSArelatedCharges = 3,
            noOfYrsAfterWhichTransferTakePlace = 10;

    public double topUpPremiumAmt = 5000,
            topUp = 0.0,

    // serviceTax=0.1236,
    /**
     * New ST from 1st June 2016
     */

    // As Per GST
//             serviceTax=0.18,
    // serviceTax=0.15,
    charge_SumAssuredBase = 0, ADBrate = 0.0005, charge_Fund_Ren = 0.0,
            charge_PP_Ren = 540, charge_Fund = 0, charge_Guarantee = 0.0035,
    // change by vrushali
    returnGuaranteeFund = 0.0, bondFund = 0.01, equityFund = 0.0135,
            PEmanagedFund = 0.0,
            FMC_BalancedFund = 0.0125,
            FMC_MoneyMarketFund = 0.0025,
            FMC_BondOptimiserFund = 0.0115,
            FMC_PureFund = 0.0135,
            FMC_CorpBondFund = 0.0115;

    public boolean topUpStatus = true,
            allocationCharges = true,
            riderCharges = true,
            mortalityCharges = true,
    // guaranteeCharges=true,
    guaranteeCharges = false,
            mortalityAndRiderCharges = true,
            administrationAndSArelatedCharges = true,
            fundManagementCharges = true,
    // surrenderCharges=false;
    surrenderCharges = true, guaranteeCharges1 = true,
            allocationChargesReductionYield = false,
            riderChargesReductionYield = false,
            mortalityChargesReductionYield = false,
            guaranteeChargesReductionYield = false,
            guaranteeChargesReductionYield1 = false,
            mortalityAndRiderChargesReductionYield = false,
            administrationAndSArelatedChargesReductionYield = false,
            fundManagementChargesReductionYield = false,
            surrenderChargesReductionYield = false;

    // modified by vrushali chaudhari as per the requirement in version 2
    public double mortalityCharges_AsPercentOfofLICtable = 1.0,
    // int1=0.06,
    // int2=0.1;
    int1 = 0.04, int2 = 0.08;

    // Constructor
    public SmartWealthAssureProperties() {
    }

}
