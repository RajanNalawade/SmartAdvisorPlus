package sbilife.com.pointofsale_bancaagency.smartprivilege;

public class SmartPrivilegeProperties {

    //	double  serviceTax=0.18,
    double int1 = 0.04,
            int2 = 0.08,
            FMC_EquityFund = 0.0135, FMC_EquityOptimiserFund = 0.0135,
            FMC_GrowthFund = 0.0135, FMC_BalancedFund = 0.0125,
            FMC_BondFund = 0.01, FMC_PureFund = 0.0135,
            FMC_MidcapFund = 0.0135,
            FMC_Top300Fund = 0.0135,
            FMC_BondOptimiserFundII = 0.0135,
            FMC_MoneyMarketFundII = 0.0075,
    //					FMC_BondOptimiserFund =0.0115,
    FMC_BondOptimiserFund = 0.0115,
            FMC_CorpBondFund = 0.0115,
            FMC_MoneyMarketFund = 0.0025,
            charge_Fund = 0,
            guarantee_charge = 0,
            mortalityCharges_AsPercentOfofLICtable = 1,
            maxPremiumAmtLimit = 10000000;

    public int minAgeLimit = 8, maxAgeLimit = 55, proposerAgeMin = 18;

    public boolean allocationCharges = true, mortalityCharges = true,
            mortalityAndRiderCharges = true, administrationCharges = true,
            fundManagementCharges = true, riderCharges = true,
            asPercentOfFirstYrPremium = true, surrenderCharges = true,

    topUpStatusYield = false, allocationChargesYield = false,
            mortalityChargesYield = false,
            mortalityAndRiderChargesYield = false,
            administrationChargesYield = false,
            fundManagementChargesYield = false, riderChargesYield = false,
            surrenderChargesYield = false;
}
