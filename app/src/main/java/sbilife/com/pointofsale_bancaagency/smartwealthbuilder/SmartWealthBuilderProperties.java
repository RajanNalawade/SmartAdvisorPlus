/***************************************************************************************************************
 Author-> Vrushali Chaudhari
 Class Description-> Smart Wealth Builder Products Properties are defined here in this class
 ***************************************************************************************************************/

package sbilife.com.pointofsale_bancaagency.smartwealthbuilder;

/**
 * @author e21946
 */
public class SmartWealthBuilderProperties {

    public int minAgeLimit = 7,
            maxAgeLimit = 65,
            proposerAgeMin = 18,
            inflation_pa_RP = 0,
            inflation_pa_SP = 0,
            fixedMonthlyExp_RP = 60,
            fixedMonthlyExp_SP = 50,
            charge_Inflation = 0;

    public double maxPremiumAmtLimit = 250000,
            topUp = 0.02,
    //serviceTax=0.1236,
    /**
     * New ST from 1st June 2016
     */
    //As Per GST
    //serviceTax=0.18,
//                                serviceTax=0.15,
    charge_SA_ren = 0,
            charge_SumAssuredBase = 0,
            mortalityCharges_AsPercentOfofLICtable = 1.2,
            int1 = 0.04,
            int2 = 0.08,
            FMC_EquityFund = 0.0135,
            FMC_EquityOptimiserFund = 0.0135,
            FMC_GrowthFund = 0.0135,
            FMC_BalancedFund = 0.0125,
            FMC_BondFund = 0.01,
            FMC_MoneyMarketFund = 0.0025,
            FMC_Top300Fund = 0.0135,
    //                                FMC_BondOptimiserFund=0.0135,
    FMC_BondOptimiserFund = 0.0115,
            FMC_MidcapFund = 0.0135,
            FMC_PureFund = 0.0135,
            FMC_CorpBondFund = 0.0115,
            charge_Fund = 0;

    public boolean isLT = true,
            isInforce = true,
            topUpStatus = true,
            allocationCharges = true,
            mortalityCharges = true,
            mortalityAndRiderCharges = true,
            administrationCharges = true,
            fundManagementCharges = true,
            riderCharges = true,
            asPercentOfFirstYrPremium = true,
            surrenderCharges = true,
            topUpStatusYield = false,
            allocationChargesYield = false,
            mortalityChargesYield = false,
            mortalityAndRiderChargesYield = false,
            administrationChargesYield = false,
            fundManagementChargesYield = false,
            riderChargesYield = false;

}
