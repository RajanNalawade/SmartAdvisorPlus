/***************************************************************************************************************
 Author-> Vrushali Chaudhari
 Class Description-> Retire Smart Products Properties are defined here in this class
 ***************************************************************************************************************/

package sbilife.com.pointofsale_bancaagency.retiresmart;

public class RetireSmartProperties {

    public boolean topUpStatus = false,
            allocationCharges = true,
            administrationCharge = true,
            mortalityCharges = false,
            mortalityAndRiderCharges = true,
            administrationCharges = true,
            guaranteeCharge = true,
            fundManagementCharges = true,
            surrenderCharges = true,
            riderCharges = false,
            allocationChargesReductionYield = false,
            administrationChargesReductionYield = false,
            mortalityAndRiderChargesReductionYield = false,
            guaranteeChargeReductionYield = false,
            fundManagementChargesReductionYield = false,
            guaranteedAddition = true,
            terminalAddition = true,
            surrenderChargesReductionYield = false;

    public double topUp = 0.02,

    //serviceTax=0.1236,
    /**
     * New ST from 1st June 2016
     */

    //As Per GST
//      					serviceTax=0.18,
    //			            serviceTax=0.15,
    charge_SumAssuredBase = 0,
            fixedMonthlyExp_RP = 45,
            mortalityCharges_AsPercentOfofLICtable = 0,
            accTPD_Charge = 0,
            int1 = 0.04,
            int2 = 0.08,
            FMC_EquityFund = 0.0135,
            FMC_BondFund = 0.01,
            FMC_Top300Fund = 0.0135,
            FMC_EquityOptFund = 0.0135,
            FMC_GrowthFund = 0.0135,
            FMC_BalancedFund = 0.0125,
            FMC_MoneyMarketFund = 0.0025,
            charge_Fund = 0,
            guarantee_chg = 0.0025,
            loyaltyAddition = 0.015,
            minGuarantee = 0.05;

    public int charge_Inflation = 0,
            inflation_pa_RP = 0,
            minAge = 30,
            maxAge = 70;
}
