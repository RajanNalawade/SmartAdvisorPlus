package sbilife.com.pointofsale_bancaagency.smartwomenadvantage;

class SmartWomenAdvantageProperties {

    public int minAge = 18, maxAge = 50;

    public double minSumAssured = 200000, maxSumAssured = 1000000,
            terminalBonus = 0.15,
            terminalBonus_8_percent = 0.15,
    // bonusInvestmnt_4_percent=0.02,
    // bonusInvestmnt_8_percent=0.03,
    minYearlyPrem = 15000, minHalfYearlyPrem = 7500,
            minQuarterleyPrem = 4000, minMonthleyPrem = 1500;

    /*****  Change As Per Swachh Bharat Cess new Calculation - Priyanka Warekar - 11-01-2016 - Start**/
    // double FY_serviceTax=0.035;
    // double FY_CessServiceTax=0.0013;
    // double SY_serviceTax=0.0175;
    // double SY_CessServiceTax=0.0007;
    /*****  Change As Per Swachh Bharat Cess new Calculation - Priyanka Warekar - 11-01-2016 - End**/
    /*** modified by Akshaya on 20-MAY-16 start **/

    // double FY_serviceTax=0.035;
    // double FY_CessServiceTax=0.0013;
    // double SY_serviceTax=0.0175;
    // double SY_CessServiceTax=0.0007;

    double serviceTax = 0.0225;
    double SBCServiceTax = 0.0225;
    double KKCServiceTax = 0;

    double serviceTaxSecondYear = 0.01125;
    double SBCServiceTaxSecondYear = 0.01125;
    double KKCServiceTaxSecondYear = 0;

    /*** modified by Akshaya on 20-MAY-16 end **/

    double kerlaServiceTax = 0.025;
    double kerlaServiceTaxSecondYear = 0.0125;
    // double FY_serviceTax=0.0363;
    // double SY_serviceTax=0.0182;
    double serviceTaxJKResident = 0.0126;
}
