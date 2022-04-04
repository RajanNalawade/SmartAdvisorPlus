package sbilife.com.pointofsale_bancaagency.products.smartplatinaassure;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import sbilife.com.pointofsale_bancaagency.common.CommonForAllProd;

public class SmartPlatinaAssureBusinessLogic {

    public CommonForAllProd commonForAllProd = null;
    public SmartPlatinaAssureDB db = null;
    public SmartPlatinaAssureBean smartPlatinaAssureBean = null;


    public CommonForAllProd cfap = null;
    public SmartPlatinaAssureProperties spaProp = null;
    double TabularSAMFRate = 0;
    double GSVFactor = 0, TermToMaturity = 0, ssvFactor = 0;
    double PremiumAmount = 0;

    public SmartPlatinaAssureBusinessLogic(
            SmartPlatinaAssureBean SPAbean) {
        // TODO Auto-generated constructor stub

        cfap = new CommonForAllProd();
        spaProp = new SmartPlatinaAssureProperties();
        db = new SmartPlatinaAssureDB();
    }


    /* Mines logic starts from here */

    /*** modified by Akshaya on 20-MAY-16 end **/
    // Set ST
    public double setServiceTax(double premAmt, double premAmtWithST) {
        return (premAmtWithST - premAmt);
    }

    /********************* Backdate Mines logic start *******************************************************/


    public double getStaffRebate() {
        if (smartPlatinaAssureBean.getIsStaff()) {
            return 0.06;
        } else {
            return 0;
        }
    }


    //##################

    public String getStaffDiscCode() {
        if (smartPlatinaAssureBean.getIsStaff())
            return "SBI";
        else
            return "None";
    }

    public double getPremiumAmount(String premfreq, Double PremAmount) {
        double PremiumAmount = 0;
        if (premfreq.equals("Yearly")) {
            PremiumAmount = PremAmount;
        } else {
            PremiumAmount = Double.parseDouble(cfap.getRoundOffLevel2New(cfap.getStringWithout_E(PremAmount * 0.085)));
        }

        return PremiumAmount;
    }

    public double getServiceTax(double premiumWithoutST, String type) {

        if (type.equals("KERALA")) {
            double a = 0;
            a = Double.parseDouble(cfap.getRoundOffLevel2New(cfap.getStringWithout_E(premiumWithoutST * (spaProp.kerlaServiceTax + 1))));
//			System.out.println(a);

            return Math.round(Double.parseDouble(cfap.getRoundUp(cfap
                    .getStringWithout_E(a - premiumWithoutST))));
        } else if (type.equals("basic")) {
            double a = 0;
            a = Double.parseDouble(cfap.getRoundOffLevel2New(cfap.getStringWithout_E(premiumWithoutST * (spaProp.serviceTax + 1))));
//			System.out.println(a);

            return Math.round(Double.parseDouble(cfap.getRoundUp(cfap
                    .getStringWithout_E(a - premiumWithoutST))));
        } else if (type.equals("SBC")) {
            double a = 0;
            a = Double.parseDouble(cfap.getRoundOffLevel2New(cfap.getStringWithout_E(premiumWithoutST * (spaProp.SBCServiceTax + 1))));

            return Math.round(Double.parseDouble(cfap.getRoundUp(cfap
                    .getStringWithout_E(a - premiumWithoutST))));
        } else // KKC
        {
            double a = 0;
            a = Double.parseDouble(cfap.getRoundOffLevel2New(cfap.getStringWithout_E(premiumWithoutST * (spaProp.KKCServiceTax + 1))));
            return Math.round(Double.parseDouble(cfap.getRoundUp(cfap
                    .getStringWithout_E(a - premiumWithoutST))));
        }

    }



    public double getServiceTax_DefenceOccupation(double premiumWithoutST, String type) {

        if (type.equals("KERALA")) {
            double a = 0;
            a = Double.parseDouble(cfap.getStringWithout_E(premiumWithoutST * (spaProp.kerlaServiceTax + 1)));
//			System.out.println(a);

            return Double.parseDouble(cfap
                    .getStringWithout_E(a - premiumWithoutST));
        } else if (type.equals("basic")) {
            double a = 0;
            a = Double.parseDouble(cfap.getStringWithout_E(premiumWithoutST * (spaProp.serviceTax + 1)));
//			System.out.println(a);

            return Double.parseDouble(cfap
                    .getStringWithout_E(a - premiumWithoutST));
        } else if (type.equals("SBC")) {
            double a = 0;
            a = Double.parseDouble(cfap.getStringWithout_E(premiumWithoutST * (spaProp.SBCServiceTax + 1)));

            return Double.parseDouble(cfap
                    .getStringWithout_E(a - premiumWithoutST));
        } else // KKC
        {
            double a = 0;
            a = Double.parseDouble(cfap.getStringWithout_E(premiumWithoutST * (spaProp.KKCServiceTax + 1)));
            return Double.parseDouble(cfap
                    .getStringWithout_E(a - premiumWithoutST));
        }

    }

    public double getServiceTaxSecondYear(double premiumWithoutST, String type) {
        if (type.equals("KERALA")) {
            double a = 0;
            a = Double.parseDouble(cfap.getRoundOffLevel2New(cfap.getStringWithout_E(premiumWithoutST * (spaProp.kerlaServiceTaxSecondYear + 1))));
            return Math.round(Double.parseDouble(cfap.getRoundUp(cfap
                    .getStringWithout_E(a - premiumWithoutST))));
        } else if (type.equals("basic")) {
            double a = 0;
            a = Double.parseDouble(cfap.getRoundOffLevel2New(cfap.getStringWithout_E(premiumWithoutST * (spaProp.serviceTaxSecondYear + 1))));
            return Math.round(Double.parseDouble(cfap.getRoundUp(cfap
                    .getStringWithout_E(a - premiumWithoutST))));
        } else if (type.equals("SBC")) {
            double a = 0;
            a = Double.parseDouble(cfap.getRoundOffLevel2New(cfap.getStringWithout_E(premiumWithoutST * (spaProp.SBCServiceTaxSecondYear + 1))));
            return Math.round(Double.parseDouble(cfap.getRoundUp(cfap
                    .getStringWithout_E(a - premiumWithoutST))));
        } else // KKC
        {
            double a = 0;
            a = Double.parseDouble(cfap.getRoundOffLevel2New(cfap.getStringWithout_E(premiumWithoutST * (spaProp.KKCServiceTaxSecondYear + 1))));
            return Math.round(Double.parseDouble(cfap.getRoundUp(cfap
                    .getStringWithout_E(a - premiumWithoutST))));
        }

    }

	//added by Roshani Yadav on 15/07/2021//
    public double setSAMFRate(int age, int premPayingTerm) {
        double TabularSAMFRate = 0;
       if(age >=3 && age <=17 && premPayingTerm ==6){

			TabularSAMFRate = 1.10;
		}
        if (age >= 18 && age <= 40 && premPayingTerm == 6) {

            TabularSAMFRate = 1.05;
        } else if (age >= 41 && age <= 50 && premPayingTerm == 6) {
            TabularSAMFRate = 1;
		}
		else if(age >=51 && age <=55 && premPayingTerm ==6){
			TabularSAMFRate = 0.9;
		}
		else if(age >=56 && age <=60 && premPayingTerm ==6){
			TabularSAMFRate = 0.8;
		}

        else if(age >=3 && age <=17 && premPayingTerm ==7){

			TabularSAMFRate = 1.2;
		}
		else if(age >=18 && age <=40 && premPayingTerm ==7){

            TabularSAMFRate = 1.2;
        } else if (age >= 41 && age <= 50 && premPayingTerm == 7) {
            TabularSAMFRate = 1.1;
        }
		else if(age >=51 && age <=55 && premPayingTerm ==7){
			TabularSAMFRate = 1;
		}
		else if(age >=56 && age <=60 && premPayingTerm ==7){
			TabularSAMFRate = 0.9;
		}

        return (TabularSAMFRate * premPayingTerm);
    }

	//end
    public double setSumAssured(double premiumAmt, double SAMFRate) {

        return (premiumAmt * SAMFRate);
    }

    public double setYearlyBasePremiumPaid(int year_F, double premiumAmt, int premPayingTerm) {
        if (year_F > premPayingTerm)
            return 0;
        else
            return premiumAmt;

    }

    public double setGuaranteedAddition(double premiumAmt,
                                        double cummulativePremium) {
        double a = 0;
        if (premiumAmt >= 100000)
            //a = 0.07;
			a = 0.0575;
        else
            //a = 0.065;
			a = 0.0525;
        return (a * cummulativePremium);

    }

    public double setGuaranteedDeathBenefit(double sumAssured,
                                            double premiumAmt, double cummulativeGuaranteedAddition,
                                            double cummulativePremium) {
        double max = Math.max(10 * premiumAmt, sumAssured);
        max = Math.max(max, 1.05 * cummulativePremium);
        return (cummulativeGuaranteedAddition + max);
    }

	public double setGuaranteedMaturityBenefit(double sumAssured,
                                                 double cummulativeGuaranteedAddition, int year_F, int policyTerm, boolean isstaff, double premiumAmt) {
          double a = 0;
          if (isstaff == true) {
              if(policyTerm ==12)
              {a=0.25*premiumAmt;}
              else{a=0.3*premiumAmt;}
              } else {
              a=0;}

		if (year_F == policyTerm){
			return (sumAssured + cummulativeGuaranteedAddition+a);
            }
		else
			return 0;
    }


    public double setGuaranteedSurrenderValue(
            double cummulativeGuaranteedAddition, double cummulativePremium,
            int year_F, int policyTerm) {
        SmartPlatinaAssureDB smartPlatinaAssureDB = new SmartPlatinaAssureDB();

        String[] prStrArr;
        double prDouble = 0;
        int arrCount = 0;
        String pr = null;

        if (policyTerm == 12) {
            prStrArr = cfap.split(smartPlatinaAssureDB.getGSVFactors12(), ",");

            prDouble = Double.parseDouble(prStrArr[year_F - 1]);

            return cummulativePremium * 1 * prDouble;
        } else {


            prStrArr = cfap.split(smartPlatinaAssureDB.getGSVFactors15(), ",");

            prDouble = Double.parseDouble(prStrArr[year_F - 1]);

            return cummulativePremium * 1 * prDouble;
        }


		/*double[] gsvFactor12 = smartPlatinaAssureDB.getGSVFactors12();
        double[] termMaturity = smartPlatinaAssureDB.getTermToMaturity();
        double[] gsvFactor15 = smartPlatinaAssureDB.getGSVFactors15();
        double temp1;
        int position = 1;
        GSVFactor = 0;
        TermToMaturity = 0;
        for (int policyYear = 1; policyYear <= 15; policyYear++) {
            if (policyYear == (policyTerm - year_F + 1)) {
                TermToMaturity = termMaturity[position];
                break;
            }
            position++;
        }

        position = 0;

        if (policyTerm == 12) {
            for (int policyYear = 1; policyYear <= 15; policyYear++) {
                if (policyYear == year_F) {
                    GSVFactor = gsvFactor12[position];
                    break;
                }
                position++;
            }
        } else {
            for (int policyYear = 1; policyYear <= 15; policyYear++) {
                if (policyYear == year_F) {
                    GSVFactor = gsvFactor15[position];
                    break;
                }
                position++;
            }
        }

//		System.out.println("\nGSVFactor"+GSVFactor);
//		System.out.println("TermToMaturity "+TermToMaturity);
//		System.out.println("cummulativePremium "+cummulativePremium);
//		System.out.println("cummulativeGuaranteedAddition "+cummulativeGuaranteedAddition);

//		temp1 = (GSVFactor * cummulativePremium) + (TermToMaturity)
//				* cummulativeGuaranteedAddition;
//
//		return (GSVFactor * cummulativePremium + (TermToMaturity)
//				* cummulativeGuaranteedAddition);

		temp1 = (GSVFactor * cummulativePremium);
//System.out.println("temp1 "+temp1);
		return (GSVFactor * cummulativePremium);*/
    }

    public double getBackDateInterest(double grossPremium, String bkDate) {

        try {

            // double indigoRate = 8.50;
            // double indigoRate = 8.75; /**Changed By Machindra 10/04/2019**/
           // double indigoRate = 6.90;
            double indigoRate = 6.50;
            double ServiceTaxValue = ((spaProp.serviceTax + spaProp.SBCServiceTax + spaProp.KKCServiceTax) + 1) * 100;

            int compoundFreq = 2;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            SimpleDateFormat dateformat1 = new SimpleDateFormat("dd-MM-yyyy");

            Date dtBackdate = dateformat1.parse(bkDate);
            String strBackDate = dateFormat.format(dtBackdate);


            Calendar cal = Calendar.getInstance();
            String date = dateFormat.format(cal.getTime());

            double netPremWithoutST = Math.round((grossPremium * 100) / ServiceTaxValue);
            int days = cfap.setDate(date, strBackDate);
            double monthsBetween = cfap.roundDown((double) days / 365 * 12, 0);

            double interest = getCompoundAmount(monthsBetween, netPremWithoutST, indigoRate, compoundFreq);
            return interest;
        } catch (Exception e) {
            return 0;
        }
    }

    public double getCompoundAmount(double monthsBetween,
                                    double netPremWithoutST, double indigoRate, int compoundFreq) {
        double compoundAmount = netPremWithoutST
                * Math.pow((1 + (indigoRate / (100 * compoundFreq))),
                (compoundFreq * (monthsBetween / 12)))
                - netPremWithoutST;
        return compoundAmount;
        // System.out.println("compoundAmount "+compoundAmount);
    }


    public double getMinesOccuInterest(String premfreq, double SumAssured_Basic) {

        return (SumAssured_Basic / 1000) * 2;


    }

    public double getDefenceOccuInterest(String premfreq, double SumAssured_Basic) {

        if (premfreq.equals("Yearly")) {
            return ((SumAssured_Basic / 1000) * 2);
        } else {
            return ((SumAssured_Basic / 1000) * 2) * 0.085;
    }

    }


    //added by sujata on 28-11-2019
    public double getSpecialSurrender(int Year_F, double premPayingTerm, int policyTerm, double SumAssured_Basic, double sumGuaranteedAddition) {

        double val, val2, a, specialSurrenderVal, ssvFactorVal;
        if (policyTerm == 12) {
            a = 2;
        } else {
            a = 3;
        }

        if (Year_F > premPayingTerm) {
            val = 1;
        } else {
            val = Year_F / premPayingTerm;
        }
        //System.out.println("\nYear_F "+Year_F);
        //System.out.println("premPayingTerm "+premPayingTerm);
        //System.out.println("val "+val);
//			System.out.println("SumAssuredBasic" + SumAssured_Basic);
        val2 = (val * SumAssured_Basic);
//			System.out.println("val2 "+val2);

        if (policyTerm == 12) {
            double[] singleSSV12 = db.getSSVFactor12();
            ssvFactor = singleSSV12[Year_F - 1];
            //	System.out.println("single temp "+ ssvFactor);
            int pos1 = 0;
            for (int i = 1; i <= Year_F; i++) {
                if (i == Year_F) {
                    ssvFactor = singleSSV12[pos1];

                    break;
                }
                pos1++;
            }

        } else {
            double[] singleSSV15 = db.getSSVFactor15();
            ssvFactor = singleSSV15[Year_F - 1];
            //	System.out.println("single temp "+ ssvFactor);
            int pos1 = 0;
            for (int i = 1; i <= Year_F; i++) {
                if (i == Year_F) {
                    ssvFactor = singleSSV15[pos1];
                    break;
                }
                pos1++;
            }


        }

        //System.out.println("");
        //System.out.println("val2 "+val2);
//			System.out.println("ssvFactor "+ssvFactor);
        specialSurrenderVal = (val2 + sumGuaranteedAddition) * ssvFactor;
        //System.out.println("specialSurrenderVal "+specialSurrenderVal);
        return specialSurrenderVal;

    }

    /*** Added by Priyanka Warekar - 31-08-2018 - start *******/
    public double getServiceTaxMines(double MinesOccuInterest) {
        return Double.parseDouble(cfap.getRoundUp(cfap.getRoundOffLevel2(cfap.getStringWithout_E(MinesOccuInterest * (spaProp.serviceTax + spaProp.SBCServiceTax)))));
    }
/*** Added by Priyanka Warekar - 31-08-2018 - end *******/

}
