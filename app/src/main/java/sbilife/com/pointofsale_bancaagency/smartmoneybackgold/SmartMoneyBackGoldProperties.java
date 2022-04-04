package sbilife.com.pointofsale_bancaagency.smartmoneybackgold;

class SmartMoneyBackGoldProperties {

    //double serviceTax=0.0309;
    /**
     * New ST from 1st June 2015
     */
//	double serviceTax=0.0363;
    double serviceTaxJkResident = 0.0126;

    /*** modified by Akshaya on 20-MAY-16 start **/

    double serviceTax = 0.0225;
    double SBCServiceTax = 0.0225;
    double KKCServiceTax = 0;


    double serviceTaxSecondYear = 0.01125;
    double SBCServiceTaxSecondYear = 0.01125;
    double KKCServiceTaxSecondYear = 0;
    double kerlaServiceTax = 0.025;
    double kerlaServiceTaxSecondYear = 0.0125;
    /*** modified by Akshaya on 20-MAY-16 end **/

    /**** Added By Priyanka Warekar - 08-08-2016 - Start ****/
    double HalfYearly_Modal_Factor = 0.51,
            Quarterly_Modal_Factor = 0.26,
            Monthly_Modal_Factor = 0.085,
            Yearly_Model_Factor = 1.00;


    /**** Added By Priyanka Warekar - 08-08-2016 - End ****/

    double GSTforbackdate = 0.18; /**Added By Pranprit 07/09/2018**/
}
