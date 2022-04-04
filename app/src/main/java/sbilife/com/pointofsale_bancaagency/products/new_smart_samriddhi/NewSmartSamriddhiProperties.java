package sbilife.com.pointofsale_bancaagency.products.new_smart_samriddhi;

public class NewSmartSamriddhiProperties {

    public int minAge = 18,
            maxAge = 50,
            policyTerm = 15,
            premiumPayingTerm = 7,
            defaultAge = 12;


    public double minPremiumAmt = 12000,
            maxPremiumAmt = 75000,
            maxMaturityAge = 65;
//							fyServiceTax=0.0309,
//							syServiceTax=0.01545;
    /*
     * New ST from 1st June 2015
     */
//							fyServiceTax=0.0363,
//							syServiceTax=0.0182;
    /*** modified by Akshaya on 20-MAY-16 start **/

//									fyServiceTax = 0.0363, syServiceTax = 0.0182;
    double serviceTax = 0.0225;
    double SBCServiceTax = 0.0225;
    double KKCServiceTax = 0;

    double serviceTaxSecondYear = 0.01125;
    double SBCServiceTaxSecondYear = 0.01125;
    double KKCServiceTaxSecondYear = 0;

    double GSTforbackdate = 0.18; /**Added By Pranprit 07/09/2018**/

    /*** modified by Akshaya on 20-MAY-16 end **/

    public String premiumPayingFreq = "Yearly";

    double kerlaServiceTax = 0.025;
    double kerlaServiceTaxSecondYear = 0.0125;

}
