package sbilife.com.pointofsale_bancaagency.smartmoneyplanner;

public class SmartMoneyPlannerProperties {

    public int minAge = 18,
            maxAge = 60;


    public double minSumAssured = 100000,
            maxSumAssured = 50000000,
            terminalBonus = 0.15,
            bonusInvestmnt_4_percent = 1.30,
            bonusInvestmnt_8_percent = 3;

//    double serviceTax=0.0363;
    /*** modified by Akshaya on 20-MAY-16 start **/

    double serviceTax = 0.0225;
    double SBCServiceTax = 0.0225;
    double KKCServiceTax = 0;

    double serviceTaxSecondYear = 0.01125;
    double SBCServiceTaxSecondYear = 0.01125;
    double KKCServiceTaxSecondYear = 0;

//  Added By Saurabh Jain on 15/05/2019 Start

    double kerlaServiceTax = 0.025;
    double kerlaServiceTaxSecondYear = 0.0125;

//  Added By Saurabh Jain on 15/05/2019 End

    /*** modified by Akshaya on 20-MAY-16 end **/

    double serviceTaxJKResident = 0.0126;

    /**** Added By Priyanka Warekar - 08-08-2016 - Start ****/
    double HalfYearly_Modal_Factor = 0.51,
            Quarterly_Modal_Factor = 0.26,
            Monthly_Modal_Factor = 0.085;

    /**** Added By Priyanka Warekar - 08-08-2016 - End ****/

    double GSTforbackdate = 0.18; /**Added By Pranprit 07/09/2018**/
}
