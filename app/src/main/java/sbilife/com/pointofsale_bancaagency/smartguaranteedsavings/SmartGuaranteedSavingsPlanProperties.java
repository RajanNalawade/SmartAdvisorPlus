package sbilife.com.pointofsale_bancaagency.smartguaranteedsavings;

public class SmartGuaranteedSavingsPlanProperties {

    public int minAge = 18,
            maxAge = 50,
            policyTerm = 15,
            premiumPayingTerm = 7,
            defaultAge = 12;

    double GSTforbackdate = 0.18;


    public double minPremiumAmt = 15000,
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

    /*** modified by Akshaya on 20-MAY-16 end **/

    public String premiumPayingFreq = "Yearly";


}
