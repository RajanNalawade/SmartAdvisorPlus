/***************************************************************************************************************
 Author-> Vrushali Chaudhari
 Class Description-> Smart Power Products Properties are defined here in this class
 ***************************************************************************************************************/

package sbilife.com.pointofsale_bancaagency.smartpower;

/**
 * @author e21946
 */
public class SmartPowerInsuranceProperties {

    public int minAgeLimit = 18,
            maxAgeLimit = 45,
            maxAgeAtMaturity = 65,
            charge_Inflation = 0,
            inflation_pa_RP = 0;

    public double minPremAmtMonthly = 2000,
            maxPremAmtMonthly = 83332.25,
            minPremAmtQuarterly = 5500,
            maxPremAmtQuarterly = 249998.8,
            minPremAmtHalfYearly = 9500,
            maxPremAmtHalfYearly = 499998.5,
            minPremAmtYearly = 15000,
            maxPremAmtYearly = 999999,

    //serviceTax=0.1236,
    /**
     * New ST from 1st June 2016
     */
    //As Per GST
//                    		serviceTax=0.18,
//                    serviceTax=0.15,
    topUpPremiumAmt = 0,
            topUp = 0.02,
            charge_SumAssuredBase = 0,
            fixedMonthlyExp_RP = 33.33,
            mortalityCharges_AsPercentOfofLICtable = 1.2,
            accTPD_Charge = 0.4,
            int1 = 0.04,
            int2 = 0.08,
            charge_Fund = 0,
            FMC_EquityFund = 0.0135,
            FMC_BondFund = 0.01,
            FMC_Top300Fund = 0.0135,
            FMC_EquityOptFund = 0.0135,
            FMC_GrowthFund = 0.0135,
            FMC_BalancedFund = 0.0125,
            FMC_MoneyMarketFund = 0.0025,
            FMC_BondOptimiserFund = 0.0115,
            FMC_PureFund = 0.0135,
            FMC_CorpBondFund = 0.0115,
            minSAMF = 10,
            maxSAMF = 10;
    ;

//    public boolean  topUpStatus=false,
//                    allocationCharges=false,
//                    riderCharges=false,
//                    mortalityCharges=false,
//                    mortalityAndRiderCharges=false,
//                    administrationCharges=false,
//                    fundManagementCharges=false,
//                    guaranteedAddition=false,
//                    surrenderCharges=false;

    public boolean topUpStatus = true,
            allocationCharges = true,
            riderCharges = true,
            mortalityCharges = true,
            mortalityAndRiderCharges = true,
            administrationCharges = true,
            fundManagementCharges = true,
            guaranteedAddition = true,
    /*surrenderCharges=false,*/
    surrenderCharges = true,
            allocationChargesYield = false,
            mortalityAndRiderChargesYield = false,
            administrationChargesYield = false,
            fundManagementChargesYield = false,
            mortalityChargesYield = false,
            riderChargesYield = false, topUpStatusYield = false;

    public SmartPowerInsuranceProperties() {
    }
}
