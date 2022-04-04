package sbilife.com.pointofsale_bancaagency.smartchamp;

public class SmartChampProperties {


    public int minAgeLimitForChild = 0, maxAgeLimitForChild = 13,
            minAgeLimitForProposer = 21, maxAgeLimitForProposer = 50,
            policyTerm = 21, premPayingTerm = 18;

    public double minSumAssured = 100000, maxSumAssured = 10000000,
            terminalBonus = 15, minYearlyPrem = 6000, minHalfYearlyPrem = 3000,
            minQuarterleyPrem = 1500, minMonthleyPrem = 500,
            minSinglePrem = 66000, serviceTaxJkResident = 0.0126;

    double serviceTax = 0.0225;
    double SBCServiceTax = 0.0225;
    double KKCServiceTax = 0;

    double serviceTaxSecondYear = 0.01125;
    double SBCServiceTaxSecondYear = 0.01125;
    double KKCServiceTaxSecondYear = 0;

    double kerlaServiceTax = 0.025;
    double kerlaServiceTaxSecondYear = 0.0125;
    double singleBonusInvestmnt_4_percent = 1.25,
            singleBonusInvestmnt_8_percent = 3.75;

    double HalfYearly_Modal_Factor = 0.51, Quarterly_Modal_Factor = 0.26,
            Monthly_Modal_Factor = 0.085;

    double GSTforbackdate = 0.18; /**Added By Pranprit 07/09/2018**/

}
