/***************************************************************************************************************
 Author-> Akshaya Mirajkar
 ***************************************************************************************************************/
package sbilife.com.pointofsale_bancaagency.saralmahaanand;

public class SaralMahaAnandProperties {
    public int minAgeLimit = 18, maxAgeLimit = 55, maxAgeAtMaturity = 69,
            minAgeADB = 18, maxAgeADB = 64, minADBterm = 5,
            charge_Inflation = 0, inflation_pa_RP = 0;

    public double minPremAmtYearly = 15000, minPremAmtHalfYearly = 9500,
            minPremAmtQuarterly = 5500,
            minPremAmtMonthly = 2000,
            maxPremAmtYearly = 29000,
            maxPremAmtHalfYearly = 14500,
            maxPremAmtQuarterly = 7200,
            maxPremAmtMonthly = 2400,
            maxSAMF = 20,
            topUpPremiumAmt = 0,
            topUp = 0.02,

    // serviceTax=0.1236,
    /**
     * New ST from 1st June 2016
     */
    //As Per GST
//            serviceTax=0.18,
    //			serviceTax = 0.15,
    charge_SumAssuredBase = 0,
            fixedMonthlyExp_RP = 33.33,
    // mortalityCharges_AsPercentOfofLICtable=1.1,
    mortalityCharges_AsPercentOfofLICtable = 1.2,
    // int1=0.06,
    // int2=0.1,
    int1 = 0.04, int2 = 0.08, charge_Fund = 0, charge_Fund_Ren = 0.01,
            FMC_EquityFund = 0.0135, FMC_BondFund = 0.01,
            FMC_BalancedFund = 0.0125, FMC_IndexFund = 0.0;

    public boolean topUpStatus = true, allocationCharges = true,
            riderCharges = true, mortalityCharges = true,
            mortalityAndRiderCharges = true, administrationCharges = true,
            asPercentOfFirstYrPremium = true, fundManagementCharges = true,
    // surrenderCharges=false,
    surrenderCharges = true, guaranteeCharges = false,
            guaranteeCharges1 = true;

    // Constructor
    public SaralMahaAnandProperties() {
    }

}
