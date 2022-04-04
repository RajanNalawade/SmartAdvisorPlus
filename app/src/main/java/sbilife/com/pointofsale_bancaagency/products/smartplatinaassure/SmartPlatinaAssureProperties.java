package sbilife.com.pointofsale_bancaagency.products.smartplatinaassure;

public class SmartPlatinaAssureProperties {

    public int minAge = 3,
            maxAge = 60,
            policyTerm = 15,
            premiumPayingTerm = 7,
            defaultAge = 12;


    public double minPremiumAmt = 50000,
            maxMaturityAge = 65;

    double serviceTax = 0.0225;
    double SBCServiceTax = 0.0225;
    double KKCServiceTax = 0;

    double serviceTaxSecondYear = 0.01125;
    double SBCServiceTaxSecondYear = 0.01125;
    double KKCServiceTaxSecondYear = 0;

    double GSTforbackdate = 0.18; /**Added By Pranprit 07/09/2018**/

    double kerlaServiceTax = 0.025;  //added by sourabh soni for kerla state 26/04/2019
    double kerlaServiceTaxSecondYear = 0.0125;


}
