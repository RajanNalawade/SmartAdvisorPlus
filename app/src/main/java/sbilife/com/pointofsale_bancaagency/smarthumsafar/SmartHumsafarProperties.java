package sbilife.com.pointofsale_bancaagency.smarthumsafar;

class SmartHumsafarProperties {

    public int minAge_LA = 18, maxAge_LA = 46, minAge_spouse = 18,
            maxAge_spouse = 46, minPolicyTerm = 10, maxPolicyTerm = 30,
            minPolicyTermRider = 5, maxPolicyTermRider = 30;

    public double minSumAssured = 100000, maxSumAssured = 50000000,
            minADBT_SA = 25000, maxADBT_SA = 5000000,
            terminalBonus = 0.15,
            minYearlyPrem = 6000, minHalfYearlyPrem = 3000,
            minQuarterleyPrem = 1500, minMonthleyPrem = 500;

    /*** modified by Akshaya on 20-MAY-16 start **/

    double serviceTax = 0.0225;
    double SBCServiceTax = 0.0225;
    double KKCServiceTax = 0;

    //added by sujata on 25-02-2020
    double serviceTaxSecondYear = 0.01125;
    double SBCServiceTaxSecondYear = 0.01125;
    double KKCServiceTaxSecondYear = 0;

    double kerlaServiceTax = 0.025;
    double kerlaServiceTaxSecondYear = 0.0125;

    double serviceTaxJKResident = 0.0126;
    //end

    double HalfYearly_Modal_Factor = 0.51, Quarterly_Modal_Factor = 0.26,
            Monthly_Modal_Factor = 0.085;

    double GSTforbackdate = 0.18; /**Added By Pranprit 07/09/2018**/

}
