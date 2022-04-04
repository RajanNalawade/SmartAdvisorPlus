/***************************************************************************************************************
 Author-> Sujata Khandekar
 *************************************************************/

package sbilife.com.pointofsale_bancaagency.products.smartfuturechoice;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import sbilife.com.pointofsale_bancaagency.common.CommonForAllProd;


public class SmartFutureChoicesBusinessLogic {
    //Class Variable Declaration
    private SmartFutureChoicesBean smartfutureChoiceBean = null;
    private CommonForAllProd commonForAllProd = null;
    private SmartFutureChoicesProperties prop = null;
    private SmartFutureChoicesBusinessLogic bl = null;

    double Rate = 0, SurrenderValue = 0, maturityBenefit = 0;


    //Store User Entered Data in a Bean Object
    public SmartFutureChoicesBusinessLogic(SmartFutureChoicesBean smartfutureChoiceBean) {
        this.smartfutureChoiceBean = smartfutureChoiceBean;
        commonForAllProd = new CommonForAllProd();
        prop = new SmartFutureChoicesProperties();
    }

    public SmartFutureChoicesBean getSaralShieldBean() {
        return smartfutureChoiceBean;
    }

    public double getInstallmentPremium(String premFreq, double annualPremium) {
        double installmentPremium = 0;
        if (premFreq.equals("Monthly")) {
            installmentPremium = annualPremium * 0.085;
        } else if (premFreq.equals("Half-Yearly")) {
            installmentPremium = annualPremium * 0.51;
        } else
            installmentPremium = annualPremium;
        //System.out.println("installmentPremium"+installmentPremium);
        return installmentPremium;
    }

    //Fair
    public double getServiceTax(double premiumBasic, boolean state, String type) {
        double basicST = 0, sbcST = 0, kkcST = 0, basicJkST = 0, keralaST = 0, premST;

        if (type.equals("basic")) {
            if (state == true) {
                basicST = Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumBasic * (prop.servtx))));

            } else {
                basicST = Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumBasic * (prop.serviceTax))));

            }

            basicST = basicST + (Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumBasic * (prop.serviceTax)))));

            return (basicST);
        } else {
            return basicST = 0;
        }


    }


    public double getServiceTaxSecondYear(double premiumBasic, boolean state, String type) {

        double basicST = 0, sbcST = 0, kkcST = 0, basicJkST = 0, keralaST = 0;


        if (type.equals("basic")) {
            if (state == true) {
                basicST = Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumBasic * (prop.kerlaServiceTaxSecondYear))));
            } else {
                basicST = Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumBasic * (prop.serviceTaxSecondYear))));

            }

            basicST = basicST + (Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumBasic * (prop.serviceTaxSecondYear)))));

            return basicST;
        } else {
            return basicST = 0;
        }
    }


    //This method is used to select Basic PR from the smartFuture constant Data
    public double getPR_Basic_FromDB(int age, double ppt, double Policyterm) {
        SmartFutureChoicesDB ac = new SmartFutureChoicesDB();

        double[] premiumArr = ac.getBasic_PR_Arr(ppt, Policyterm);
        int position = 0;


        for (int ageCount = 18; ageCount <= 50; ageCount++) {

            if (ageCount == age) {
                Rate = premiumArr[position];
            }
            position++;
        }

        //System.out.println("rate "+Rate);
        return Rate;
    }

    public double getSumassured(double AnnualPrem, double rateofprem) {
        double a, b, getsum = 0;

        if (smartfutureChoiceBean.getIsForStaffOrNot()) {
            a = ((AnnualPrem) * 1000);
            b = (a / rateofprem) * 1.06;
            //	b=(a/rateofprem)+0.0106;
//			System.out.println("into "+a);
//			System.out.println("divide "+b);
//			System.out.println("rateofprem"+rateofprem);
            //System.out.println("b"+b);
            getsum = b;
        } else {
            //System.out.println("AnnualPrem"+AnnualPrem);
            a = ((AnnualPrem) * 1000);
            b = (a / rateofprem);


            getsum = b;
        }

        if (getsum % 1000 == 0) {
            return getsum;
        } else {
            int division = (int) ((getsum / 1000) + 1);
            //int division=(int)((getsum/10)+1);
            getsum = division * 1000;
        }

        //System.out.println("sumassuredmathround "+ getsum);
        return (getsum);
    }


    public double getMinesOccuInterest(double SumAssured_Basic) {
        return (SumAssured_Basic / 1000) * 2;
    }

    public String getStaffDiscCode() {
        if (smartfutureChoiceBean.getIsForStaffOrNot())
            return "SBI";
        else
            return "None";
    }

    public double getAnnulizedPremium(int year_F, double premiumPayingTerm, double AnnualPremium) {
        if (year_F <= premiumPayingTerm) {
            //System.out.println("premiumwithRoundUP "+premiumwithRoundUP,PremiumRoundUpWithoutLoading);
            return AnnualPremium;
        } else {
            return 0;
        }
    }


    public double[] getFlexichoiceyear(double PolicyTerm, double ppt, String ChoicePercent) {
        double Flexichoiceyear = 0, Flexichoiceyear2 = 0;
        if (smartfutureChoiceBean.getPlantype().equals("Flexi Choice")) {
            if (ppt == 7) {
                if (PolicyTerm == 12)
                    return new double[]{Flexichoiceyear = 8, Flexichoiceyear2 = 10};
                else if (PolicyTerm == 15)
                    return new double[]{Flexichoiceyear = 8, Flexichoiceyear2 = 12};
                else if (PolicyTerm == 20)
                    return new double[]{Flexichoiceyear = 8, Flexichoiceyear2 = 14};
                else if (PolicyTerm == 25)
                    return new double[]{Flexichoiceyear = 8, Flexichoiceyear2 = 17};

            } else if (ppt == 10) {
                if (PolicyTerm == 15)
                    return new double[]{Flexichoiceyear = 11, Flexichoiceyear2 = 13};
                else if (PolicyTerm == 20)
                    return new double[]{Flexichoiceyear = 11, Flexichoiceyear2 = 16};
                else if (PolicyTerm == 25)
                    return new double[]{Flexichoiceyear = 11, Flexichoiceyear2 = 18};
                else if (PolicyTerm == 30)
                    return new double[]{Flexichoiceyear = 11, Flexichoiceyear2 = 21};
            } else if (ppt == 12) {

                if (PolicyTerm == 20)
                    return new double[]{Flexichoiceyear = 13, Flexichoiceyear2 = 17};
                else if (PolicyTerm == 25)
                    return new double[]{Flexichoiceyear = 13, Flexichoiceyear2 = 19};
                else if (PolicyTerm == 30)
                    return new double[]{Flexichoiceyear = 13, Flexichoiceyear2 = 22};
            } else {
                if (PolicyTerm == 20)
                    return new double[]{Flexichoiceyear = 16, Flexichoiceyear2 = 18};
                else if (PolicyTerm == 25)
                    return new double[]{Flexichoiceyear = 16, Flexichoiceyear2 = 21};
                else if (PolicyTerm == 30)
                    return new double[]{Flexichoiceyear = 16, Flexichoiceyear2 = 23};
            }
        }

        return new double[]{Flexichoiceyear, Flexichoiceyear2};
    }

    public double getSurvivalBenefit(int year_F, double PolicyTerm, double ppt, double Sumassured, String ChoicePercent) {
        double Survivalbenefit = 0;
        double arr = 0;
        //	SmartFutureChoicesDB ac=new SmartFutureChoicesDB();

        double[] flexiArr = getFlexichoiceyear(PolicyTerm, ppt, ChoicePercent);
        int position = 0;

        try {
            if (smartfutureChoiceBean.getPlantype().equals("Flexi Choice")) {

                for (int _year_F = 1; _year_F <= 30; _year_F++) {
                    {
                        arr = flexiArr[position];
                        if (arr == year_F) {
                            Survivalbenefit = 0.1 * Sumassured;
                        }
                    }
                    position++;
                }


            } else {
                Survivalbenefit = 0;
            }
        } catch (Exception ex) {
            // TODO: handle exception
            String msg = ex.getMessage();
        }
        //System.out.println("Survivalbenefit "+Survivalbenefit);
        return Survivalbenefit;
    }

    public double getSurrenderBenefit(int year_F, double PolicyTerm, double sumannualizedPrem, double sumsurvivalBenefit) {
        double SurrenderValue = 0;
        SmartFutureChoicesDB db = new SmartFutureChoicesDB();
        double[] GSV = db.getGVSFactors(PolicyTerm);
        try {
            if (year_F <= PolicyTerm) {
                double arr = 0;
                int position = 0;
                if (smartfutureChoiceBean.getPlantype().equals("Classic Choice")) {
                    for (int i = 1; i <= 30; i++) {

                        if (year_F == i) {
                            arr = GSV[position];
                            // System.out.println("arr " + arr);
                        }
                        position++;


                    }
                    //System.out.println("sumannualizedPrem"+sumannualizedPrem);
                    SurrenderValue = arr * sumannualizedPrem;
                } else {
                    int i;
                    for (i = 1; i <= 30; i++) {

                        if (year_F == i) {
                            arr = GSV[position];
                            // System.out.println("arr " + arr);
                        }
                        position++;

//	        			 if(year_F==i)
//	        			 {
//	        				 year_F=i-1;
//	        				 sumsurvivalBenefit=sumsurvivalBenefit;
//	        			 }


                    }

                    System.out.println("\n Year " + year_F);
                    System.out.println("arr " + arr);
                    System.out.println("sumannualizedPrem " + sumannualizedPrem);
                    System.out.println("sumsurvivalBenefit " + sumsurvivalBenefit);

                    SurrenderValue = arr * sumannualizedPrem - sumsurvivalBenefit;
                    //System.out.println("\nSurrenderValue "+SurrenderValue);

                }
            } else {
                return SurrenderValue = 0;
            }
        } catch (Exception ex) {
            // TODO: handle exception
            String msg = ex.getMessage();
        }
        return SurrenderValue;
    }

    public double getGauranteedDeathBenefit(int Year, double PolicyTerm, double firstyearofannulized, double sumofAnnulized) {
        double a, b, DeathBen;
        if (Year <= PolicyTerm) {
            DeathBen = firstyearofannulized * 11;
//			b=1.05 * sumofAnnulized;
//			DeathBen=Math.max(a, b);
        } else {
            return DeathBen = 0;
        }
//		System.out.println("a"+a);
//		System.out.println("b"+b);
//		System.out.println("DeathBen "+DeathBen);
        return DeathBen;
    }

    public double getMaturityBenefit(int Year, int age, double PolicyTerm, double ppt, double Sumassured) {
        double maturityBen = 0, val1;
        double arr = 0;
        int position = 0;
        SmartFutureChoicesDB db = new SmartFutureChoicesDB();
        double[] LumsumFact = db.getLumsumFactor(ppt, PolicyTerm);

        try {
            if (Year == PolicyTerm) {
                if (smartfutureChoiceBean.getPlantype().equals("Flexi Choice")) {
                    //	System.out.println("Sumassured"+Sumassured);
                    maturityBenefit = 0.8 * Sumassured;
                } else {
                    for (int ageCount = 18; ageCount <= 50; ageCount++) {

                        if (ageCount == age) {
                            arr = LumsumFact[position];
                            // System.out.println("arr " + arr);
                        }
                        position++;


                    }
                    maturityBenefit = arr * Sumassured;
                    //	System.out.println("maturityBenefit"+maturityBenefit);
                    //SurrenderValue=arr*sumannualizedPrem-sumsurvivalBenefit;

                }
            } else {
                return maturityBenefit = 0;
            }
        } catch (Exception e) {
            // TODO: handle exception
            String msg = e.getMessage();
        }
        return maturityBenefit;
    }


    public double getBonusRate(int Year, String returnOnInvestment) {
        double bonusrate = 0;
        String Planname = smartfutureChoiceBean.getPlantype();
        int Policyterm = smartfutureChoiceBean.getPolicyterm();
        int Ppt = smartfutureChoiceBean.getPremiumpayingterm();
        if (Planname.equals("Classic Choice")) {
            if (returnOnInvestment.equals("4%") && Year <= Ppt) {
                if (Policyterm == 12 && Ppt == 7)
                    bonusrate = 0.011;
                else if (Policyterm == 12 && Ppt == 10)
                    bonusrate = 0;
                else if (Policyterm == 12 && Ppt == 12)
                    bonusrate = 0;
                else if (Policyterm == 12 && Ppt == 15)
                    bonusrate = 0;

                else if (Policyterm == 15 && Ppt == 7)
                    bonusrate = 0.0105;
                else if (Policyterm == 15 && Ppt == 10)
                    bonusrate = 0.0115;
                else if (Policyterm == 15 && Ppt == 12)
                    bonusrate = 0;
                else if (Policyterm == 15 && Ppt == 15)
                    bonusrate = 0;

                else if (Policyterm == 20 && Ppt == 7)
                    bonusrate = 0.0105;
                else if (Policyterm == 20 && Ppt == 10)
                    bonusrate = 00.011;
                else if (Policyterm == 20 && Ppt == 12)
                    bonusrate = 0.0115;
                else if (Policyterm == 20 && Ppt == 15)
                    bonusrate = 0.012;

                else if (Policyterm == 25 && Ppt == 7)
                    bonusrate = 0.0105;
                else if (Policyterm == 25 && Ppt == 10)
                    bonusrate = 0.011;
                else if (Policyterm == 25 && Ppt == 12)
                    bonusrate = 0.0115;
                else if (Policyterm == 25 && Ppt == 15)
                    bonusrate = 0.012;

                else if (Policyterm == 30 && Ppt == 7)
                    bonusrate = 0;
                else if (Policyterm == 30 && Ppt == 10)
                    bonusrate = 0.011;
                else if (Policyterm == 30 && Ppt == 12)
                    bonusrate = 0.0115;
                else if (Policyterm == 30 && Ppt == 15)
                    bonusrate = 0.012;
            } else if (returnOnInvestment.equals("4%") && Year > Ppt) {
                if (Policyterm == 12 && Ppt == 7)
                    bonusrate = 0.009;
                else if (Policyterm == 12 && Ppt == 10)
                    bonusrate = 0;
                else if (Policyterm == 12 && Ppt == 12)
                    bonusrate = 0;
                else if (Policyterm == 12 && Ppt == 15)
                    bonusrate = 0;

                else if (Policyterm == 15 && Ppt == 7)
                    bonusrate = 0.009;
                else if (Policyterm == 15 && Ppt == 10)
                    bonusrate = 0.0095;
                else if (Policyterm == 15 && Ppt == 12)
                    bonusrate = 0;
                else if (Policyterm == 15 && Ppt == 15)
                    bonusrate = 0;

                else if (Policyterm == 20 && Ppt == 7)
                    bonusrate = 0.0085;
                else if (Policyterm == 20 && Ppt == 10)
                    bonusrate = 0.0095;
                else if (Policyterm == 20 && Ppt == 12)
                    bonusrate = 0.01;
                else if (Policyterm == 20 && Ppt == 15)
                    bonusrate = 0.0115;

                else if (Policyterm == 25 && Ppt == 7)
                    bonusrate = 0.008;
                else if (Policyterm == 25 && Ppt == 10)
                    bonusrate = 0.009;
                else if (Policyterm == 25 && Ppt == 12)
                    bonusrate = 0.0095;
                else if (Policyterm == 25 && Ppt == 15)
                    bonusrate = 0.01;

                else if (Policyterm == 30 && Ppt == 7)
                    bonusrate = 0;
                else if (Policyterm == 30 && Ppt == 10)
                    bonusrate = 0.0085;
                else if (Policyterm == 30 && Ppt == 12)
                    bonusrate = 0.009;
                else if (Policyterm == 30 && Ppt == 15)
                    bonusrate = 0.0095;
            } else if (returnOnInvestment.equals("8%") && Year <= Ppt) {
                if (Policyterm == 12 && Ppt == 7)
                    bonusrate = 0.0275;
                else if (Policyterm == 12 && Ppt == 10)
                    bonusrate = 0;
                else if (Policyterm == 12 && Ppt == 12)
                    bonusrate = 0;
                else if (Policyterm == 12 && Ppt == 15)
                    bonusrate = 0;

                else if (Policyterm == 15 && Ppt == 7)
                    bonusrate = 0.028;
                else if (Policyterm == 15 && Ppt == 10)
                    bonusrate = 0.026;
                else if (Policyterm == 15 && Ppt == 12)
                    bonusrate = 0;
                else if (Policyterm == 15 && Ppt == 15)
                    bonusrate = 0;

                else if (Policyterm == 20 && Ppt == 7)
                    bonusrate = 0.028;
                else if (Policyterm == 20 && Ppt == 10)
                    bonusrate = 0.0265;
                else if (Policyterm == 20 && Ppt == 12)
                    bonusrate = 0.0255;
                else if (Policyterm == 20 && Ppt == 15)
                    bonusrate = 0.0245;

                else if (Policyterm == 25 && Ppt == 7)
                    bonusrate = 0.028;
                else if (Policyterm == 25 && Ppt == 10)
                    bonusrate = 0.0265;
                else if (Policyterm == 25 && Ppt == 12)
                    bonusrate = 0.0255;
                else if (Policyterm == 25 && Ppt == 15)
                    bonusrate = 0.025;

                else if (Policyterm == 30 && Ppt == 7)
                    bonusrate = 0;
                else if (Policyterm == 30 && Ppt == 10)
                    bonusrate = 0.0265;
                else if (Policyterm == 30 && Ppt == 12)
                    bonusrate = 0.0255;
                else if (Policyterm == 30 && Ppt == 15)
                    bonusrate = 0.025;
            } else {
                if (Policyterm == 12 && Ppt == 7)
                    bonusrate = 0.029;
                else if (Policyterm == 12 && Ppt == 10)
                    bonusrate = 0;
                else if (Policyterm == 12 && Ppt == 12)
                    bonusrate = 0;
                else if (Policyterm == 12 && Ppt == 15)
                    bonusrate = 0;

                else if (Policyterm == 15 && Ppt == 7)
                    bonusrate = 0.0295;
                else if (Policyterm == 15 && Ppt == 10)
                    bonusrate = 0.028;
                else if (Policyterm == 15 && Ppt == 12)
                    bonusrate = 0;
                else if (Policyterm == 15 && Ppt == 15)
                    bonusrate = 0;

                else if (Policyterm == 20 && Ppt == 7)
                    bonusrate = 0.0305;
                else if (Policyterm == 20 && Ppt == 10)
                    bonusrate = 0.0285;
                else if (Policyterm == 20 && Ppt == 12)
                    bonusrate = 0.028;
                else if (Policyterm == 20 && Ppt == 15)
                    bonusrate = 0.0275;

                else if (Policyterm == 25 && Ppt == 7)
                    bonusrate = 0.0305;
                else if (Policyterm == 25 && Ppt == 10)
                    bonusrate = 0.0285;
                else if (Policyterm == 25 && Ppt == 12)
                    bonusrate = 0.029;
                else if (Policyterm == 25 && Ppt == 15)
                    bonusrate = 0.027;

                else if (Policyterm == 30 && Ppt == 7)
                    bonusrate = 0;
                else if (Policyterm == 30 && Ppt == 10)
                    bonusrate = 0.0285;
                else if (Policyterm == 30 && Ppt == 12)
                    bonusrate = 0.029;
                else if (Policyterm == 30 && Ppt == 15)
                    bonusrate = 0.027;
            }
        } else {
            if (returnOnInvestment.equals("4%")) {
                if (Policyterm == 12 && Ppt == 7)
                    bonusrate = 0.011;
                else if (Policyterm == 12 && Ppt == 10)
                    bonusrate = 0;
                else if (Policyterm == 12 && Ppt == 12)
                    bonusrate = 0;
                else if (Policyterm == 12 && Ppt == 15)
                    bonusrate = 0;

                else if (Policyterm == 15 && Ppt == 7)
                    bonusrate = 0.0105;
                else if (Policyterm == 15 && Ppt == 10)
                    bonusrate = 0.0115;
                else if (Policyterm == 15 && Ppt == 12)
                    bonusrate = 0;
                else if (Policyterm == 15 && Ppt == 15)
                    bonusrate = 0;

                else if (Policyterm == 20 && Ppt == 7)
                    bonusrate = 0.0105;
                else if (Policyterm == 20 && Ppt == 10)
                    bonusrate = 0.011;
                else if (Policyterm == 20 && Ppt == 12)
                    bonusrate = 0.0115;
                else if (Policyterm == 20 && Ppt == 15)
                    bonusrate = 0.012;

                else if (Policyterm == 25 && Ppt == 7)
                    bonusrate = 0.0105;
                else if (Policyterm == 25 && Ppt == 10)
                    bonusrate = 0.011;
                else if (Policyterm == 25 && Ppt == 12)
                    bonusrate = 0.0115;
                else if (Policyterm == 25 && Ppt == 15)
                    bonusrate = 0.012;

                else if (Policyterm == 30 && Ppt == 7)
                    bonusrate = 0;
                else if (Policyterm == 30 && Ppt == 10)
                    bonusrate = 0.011;
                else if (Policyterm == 30 && Ppt == 12)
                    bonusrate = 0.0115;
                else if (Policyterm == 30 && Ppt == 15)
                    bonusrate = 0.012;
            } else {
                if (Policyterm == 12 && Ppt == 7)
                    bonusrate = 0.0275;
                else if (Policyterm == 12 && Ppt == 10)
                    bonusrate = 0;
                else if (Policyterm == 12 && Ppt == 12)
                    bonusrate = 0;
                else if (Policyterm == 12 && Ppt == 15)
                    bonusrate = 0;

                else if (Policyterm == 15 && Ppt == 7)
                    bonusrate = 0.028;
                else if (Policyterm == 15 && Ppt == 10)
                    bonusrate = 0.026;
                else if (Policyterm == 15 && Ppt == 12)
                    bonusrate = 0;
                else if (Policyterm == 15 && Ppt == 15)
                    bonusrate = 0;

                else if (Policyterm == 20 && Ppt == 7)
                    bonusrate = 0.028;
                else if (Policyterm == 20 && Ppt == 10)
                    bonusrate = 0.0265;
                else if (Policyterm == 20 && Ppt == 12)
                    bonusrate = 0.0255;
                else if (Policyterm == 20 && Ppt == 15)
                    bonusrate = 0.0245;

                else if (Policyterm == 25 && Ppt == 7)
                    bonusrate = 0.028;
                else if (Policyterm == 25 && Ppt == 10)
                    bonusrate = 0.0265;
                else if (Policyterm == 25 && Ppt == 12)
                    bonusrate = 0.0255;
                else if (Policyterm == 25 && Ppt == 15)
                    bonusrate = 0.025;

                else if (Policyterm == 30 && Ppt == 7)
                    bonusrate = 0;
                else if (Policyterm == 30 && Ppt == 10)
                    bonusrate = 0.0265;
                else if (Policyterm == 30 && Ppt == 12)
                    bonusrate = 0.0255;
                else if (Policyterm == 30 && Ppt == 15)
                    bonusrate = 0.025;
            }
        }
        return bonusrate;

    }

    public double getCashBonus4per(int Year, double sumassured, String returnOnInvestment) {
        double cashbonus = 0;
        if (smartfutureChoiceBean.getPlantype().equals("Classic Choice")) {
            if (Year <= smartfutureChoiceBean.getPremiumpayingterm()) {
                cashbonus = sumassured * getBonusRate(Year, returnOnInvestment);
            } else {
                cashbonus = sumassured * getBonusRate(Year, returnOnInvestment);
            }
        } else {
            if (Year <= smartfutureChoiceBean.getPolicyterm()) {
                cashbonus = sumassured * getBonusRate(Year, returnOnInvestment);
            }
        }

//		System.out.println("sumassured f"+sumassured);
//		System.out.println("getBonusRate(returnOnInvestment)" +getBonusRate(Year,returnOnInvestment));
        return cashbonus;
    }

    public double getCashBonus8per(int Year, double sumassured, String returnOnInvestment) {
        double cashbonus = 0;
        if (smartfutureChoiceBean.getPlantype().equals("Classic Choice")) {
            if (Year <= smartfutureChoiceBean.getPolicyterm()) {
                cashbonus = sumassured * getBonusRate(Year, returnOnInvestment);
            } else {
                cashbonus = sumassured * getBonusRate(Year, returnOnInvestment);
            }
        } else {
            if (Year <= smartfutureChoiceBean.getPolicyterm()) {
                cashbonus = sumassured * getBonusRate(Year, returnOnInvestment);
            }
        }
        //System.out.println("cashbonus"+cashbonus);
        return cashbonus;
    }

    public double getSurrenderBen4Per(double Year, int age, double CashBonus4percent, double sumannulize, double sumsurvival, double Sumassured_Basic) {
        double Surrender = 0;
        double arr1 = 0, arr2 = 0, lumsum = 0, a, b, c;
        ;
        int position = 0;
        int pos = 0;
        //int age=smartfutureChoiceBean.getAge();
        double ppt = smartfutureChoiceBean.getPremiumpayingterm();
        int Policyterm = smartfutureChoiceBean.getPolicyterm();
        SmartFutureChoicesDB db = new SmartFutureChoicesDB();
        double[] SSVclassicdb = db.getSSVClassic_Choice(age, ppt, Policyterm);
        double[] SSVFlexidb = db.getSSVFlexi_Choice(age, ppt, Policyterm);
        double[] lumsumFactdb = db.getLumsumFactor(ppt, Policyterm);
        try {
            if (Year <= Policyterm) {
                if (smartfutureChoiceBean.getPlantype().equals("Classic Choice")) {
                    for (int ageCount = 18; ageCount <= 50; ageCount++) {

                        if (ageCount == age) {
                            lumsum = lumsumFactdb[pos];
                            // System.out.println("arr " + arr);
                        }
                        pos++;

                    }
                    for (int i = 1; i <= 30; i++) {
                        if (Year == i) {
                            arr1 = SSVclassicdb[position];
                            //System.out.println("arr1 " + arr1);
                        }
                        position++;
                    }


                    //a=Sumassured_Basic*lumsum;
                    a = Sumassured_Basic;
                    b = Math.min(1, Year / ppt);
                    Surrender = arr1 * (a * b);
                    //	Surrender=(arr1*(sumannulize+CashBonus4percent));
                    //System.out.println("(sumannulize+CashBonus4percent)" + (sumannulize+CashBonus4percent));


                    //	System.out.println("");

                } else {
                    for (int i = 1; i <= 30; i++) {
                        if (Year == i) {
                            arr2 = SSVFlexidb[position];
                            //	System.out.println("arr2 " + arr2);
                        }
                        position++;
                    }
                    //	Surrender=(arr2*(sumannulize+CashBonus4percent)-sumsurvival);


                    //a=(0.8*Sumassured_Basic);
                    a = (Sumassured_Basic);
                    b = Math.min(1, Year / ppt);
                    Surrender = arr2 * a * b;


                }
            } else {
                return Surrender = 0;
            }
            //System.out.println("Surrender" +Surrender);
        } catch (Exception e) {
            // TODO: handle exception
            String msg = e.getMessage();
        }
        return Surrender;
    }


    public double getSurrenderBen8Per(double Year, int age, double CashBonus8percent, double sumannulize, double sumsurvival, double Sumassured_Basic) {
        double Surrender = 0;
        double arr1 = 0, arr2 = 0, lumsum = 0, a, b, c;
        ;
        int position = 0;
        int pos = 0;
        //int age=smartfutureChoiceBean.getAge();
        double ppt = smartfutureChoiceBean.getPremiumpayingterm();
        int Policyterm = smartfutureChoiceBean.getPolicyterm();
        SmartFutureChoicesDB db = new SmartFutureChoicesDB();
        double[] SSVclassicdb = db.getSSVClassic_Choice(age, ppt, Policyterm);
        double[] SSVFlexidb = db.getSSVFlexi_Choice(age, ppt, Policyterm);
        double[] lumsumFactdb = db.getLumsumFactor(ppt, Policyterm);

        try {
            if (Year <= Policyterm) {
                if (smartfutureChoiceBean.getPlantype().equals("Classic Choice")) {
                    for (int ageCount = 18; ageCount <= 50; ageCount++) {

                        if (ageCount == age) {
                            lumsum = lumsumFactdb[pos];
                            // System.out.println("arr " + arr);
                        }
                        pos++;

                    }
                    for (int i = 1; i <= 30; i++) {
                        if (Year == i) {
                            arr1 = SSVclassicdb[position];
                            //System.out.println("arr1 " + arr1);
                        }
                        position++;
                    }


                    a = Sumassured_Basic;
                    b = Math.min(1, Year / ppt);
                    Surrender = arr1 * (a * b);
                    //	Surrender=(arr1*(sumannulize+CashBonus4percent));
                    //	System.out.println("(sumannulize+CashBonus4percent)" + (sumannulize+CashBonus4percent));


                    //	System.out.println("");

                } else {
                    for (int i = 1; i <= 30; i++) {
                        if (Year == i) {
                            arr2 = SSVFlexidb[position];
                            //	System.out.println("arr2 " + arr2);
                        }
                        position++;
                    }
                    //	Surrender=(arr2*(sumannulize+CashBonus4percent)-sumsurvival);


                    a = (Sumassured_Basic);
                    b = Math.min(1, Year / ppt);
                    Surrender = arr2 * a * b;


                }
            } else {
                return Surrender = 0;
            }
            //	System.out.println("Surrender" +Surrender);
        } catch (Exception e) {
            // TODO: handle exception
            String msg = e.getMessage();
        }

        return Surrender;
    }

    public double getTotalMaturity4per(int Year, int age, double sumassured, double sumcashbonus4, double cashbonus4, double GuaranteedMaturityBen, double Defferedcash4per) {
        double totalmaturity4 = 0;
        double arr = 0;
        int position = 0;
        int Policyterm = smartfutureChoiceBean.getPolicyterm();
        int ppt = smartfutureChoiceBean.getPremiumpayingterm();
        SmartFutureChoicesDB db = new SmartFutureChoicesDB();
        double[] LumsumFact = db.getLumsumFactor(ppt, Policyterm);
        try {
            if (Year == Policyterm) {
                if (smartfutureChoiceBean.getBonusOption().equals("Deferred Cash Bonus")) {
                    totalmaturity4 = GuaranteedMaturityBen + Defferedcash4per + (0.15 * sumcashbonus4);
                } else {
                    totalmaturity4 = GuaranteedMaturityBen + cashbonus4 + (0.15 * sumcashbonus4);
                }

//			System.out.println("GuaranteedMaturityBen"+GuaranteedMaturityBen);
//			System.out.println("Defferedcash4per"+Defferedcash4per);
//			System.out.println("sumcashbonus4"+sumcashbonus4);


            } else {
                return 0;
            }
        } catch (Exception e) {
            // TODO: handle exception
            String msg = e.getMessage();
        }
        //System.out.println("totalmaturity4 "+totalmaturity4);
        return totalmaturity4;
    }


    public double getTotalMaturity8per(int Year, int age, double sumassured, double sumcashbonus8, double cashbonus8, double GuaranteedMaturityBen, double Defferedcash8per) {
        double totalmaturity8 = 0;
        double arr = 0;
        int position = 0;
        int Policyterm = smartfutureChoiceBean.getPolicyterm();
        int ppt = smartfutureChoiceBean.getPremiumpayingterm();
        SmartFutureChoicesDB db = new SmartFutureChoicesDB();
        double[] LumsumFact = db.getLumsumFactor(ppt, Policyterm);
        try {
            if (Year == Policyterm) {
                if (smartfutureChoiceBean.getBonusOption().equals("Deferred Cash Bonus")) {
                    totalmaturity8 = GuaranteedMaturityBen + Defferedcash8per + (0.15 * sumcashbonus8);
                } else {
                    totalmaturity8 = GuaranteedMaturityBen + cashbonus8 + (0.15 * sumcashbonus8);
                }


            } else {
                return 0;
            }
        } catch (Exception e) {
            // TODO: handle exception
            String msg = e.getMessage();
        }
        //	System.out.println("totalmaturity8 "+totalmaturity8);
        return totalmaturity8;
    }

    public double getTotalDeathBenefit4per(int Year, double sumcashbonus, double cashbonus, double deathBenefit, double Defferedcash4per, double sumannualizedPrem) {
        double totaldeathBenefit = 0, a, b, c;

        int Policyterm = smartfutureChoiceBean.getPolicyterm();

        try {
            if (Year <= Policyterm) {
                if (smartfutureChoiceBean.getBonusOption().equals("Deferred Cash Bonus")) {
                    a = deathBenefit + Defferedcash4per + (0.15 * sumcashbonus);
                    b = 1.05 * sumannualizedPrem;
                    totaldeathBenefit = Math.max(a, b);
                } else {
                    a = deathBenefit + cashbonus + (0.15 * sumcashbonus);
                    b = 1.05 * sumannualizedPrem;
                    totaldeathBenefit = Math.max(a, b);
                }
            } else {
                return 0;
            }
        } catch (Exception e) {
            // TODO: handle exception
            String msg = e.getMessage();
        }


        return totaldeathBenefit;
    }

    public double getTotalDeathBenefit8per(int Year, double sumcashbonus8, double cashbonus8, double deathBenefit, double Defferedcash8per, double sumannualizedPrem) {
        double totaldeathBenefit = 0, a, b;

        int Policyterm = smartfutureChoiceBean.getPolicyterm();

        try {
            if (Year <= Policyterm)
                if (smartfutureChoiceBean.getBonusOption().equals("Deferred Cash Bonus")) {
                    a = deathBenefit + Defferedcash8per + (0.15 * sumcashbonus8);
                    b = 1.05 * sumannualizedPrem;
                    totaldeathBenefit = Math.max(a, b);
//				System.out.println("\na "+a);
//				System.out.println("b "+b);
                } else {
                    a = deathBenefit + cashbonus8 + (0.15 * sumcashbonus8);
                    b = 1.05 * sumannualizedPrem;
                    totaldeathBenefit = Math.max(a, b);
                }

        } catch (Exception e) {
            // TODO: handle exception
            String msg = e.getMessage();
        }
        //System.out.println("totalmaturity4 "+totalmaturity4);
        return totaldeathBenefit;
    }

    //added by sujata on 30/09/2020
//		public double getAccumulatedDefferedcash(int Year,double sumcashbonus4, double CashBonus4percentfy,double sumofdefferedcash,double dfcashfordf)
//		{
//			 int Policyterm=smartfutureChoiceBean.getPolicyTerm_Basic();
//			 double a,b,Defferedcash=0;
//			 try{
//			if(Year<=Policyterm)
//			{
//				if(smartfutureChoiceBean.getBonusOption().equals("Deferred Cash Bonus"))
//				{
//					if(Year==1)
//					{
//					//	System.out.println("CashBonus4percentfy"+CashBonus4percentfy);
//						Defferedcash=CashBonus4percentfy;
//					}
//
//					else if(Year==2)
//					{
//						Defferedcash=0.03*CashBonus4percentfy+(sumcashbonus4*2);
//					//	System.out.println("\n CashBonus4percentfy"+CashBonus4percentfy);
//					//	System.out.println("sumcashbonus4 "+sumcashbonus4);
//
//					//	System.out.println("Defferedcash"+Defferedcash);
//					}
//
//
//					else if(Year>=3)
//					{
//
//					//	a=0.03*defferedcash4per+CashBonus4percent;
//						//Defferedcash=(0.03*Defferedcash+sumcashbonus4)+(0.03*sumofdefferedcash);
//						a=(0.03*dfcashfordf+sumcashbonus4);
//						b=(0.03*sumofdefferedcash);
//						Defferedcash=a+b;
//
//						System.out.println("\n a "+a);
//						System.out.println("\n b "+b);
//						System.out.println("\n sumcashbonus4 "+sumcashbonus4);
//
//					//	b=0.03*sumofdefferedcash;
//					//	Defferedcash=a+b;
//					}
//					else
//					{
//						Defferedcash=0;
//					}
//				}
//			}
//			else
//			{
//				return 0;
//			}
//
//			 }catch(Exception e)
//
//			 {String msg=e.getMessage();}
//			System.out.println("Defferedcash"+Defferedcash);
//			 return Defferedcash;
//		}

    public double getAccumulatedDefferedcash4per(int Year, double sumcashbonus4, double CashBonus4percentfy, double sumofdefferedcash, double dfcashfordf) {
        int Policyterm = smartfutureChoiceBean.getPolicyterm();
        double a, b, Defferedcash = 0;
        try {
            if (Year <= Policyterm) {
                if (smartfutureChoiceBean.getBonusOption().equals("Deferred Cash Bonus")) {
                    a = (0.03 * dfcashfordf + sumcashbonus4);
                    b = (0.03 * sumofdefferedcash);
                    Defferedcash = a + b;
//					System.out.println("\n year "+Year);
//					System.out.println("\n a "+a);
//					System.out.println("\n b "+b);
//					System.out.println("\n sumcashbonus444444 "+sumcashbonus4);
                }
            } else {
                return 0;
            }

        } catch (Exception e) {
            String msg = e.getMessage();
        }
        //System.out.println("Defferedcash"+Defferedcash);
        return Defferedcash;
    }

    public double getAccumulatedDefferedcash8per(int Year, double sumcashbonus8, double CashBonus8percentfy, double sumofdefferedcash8, double dfcashfordf8) {
        int Policyterm = smartfutureChoiceBean.getPolicyterm();
        double a, b, Defferedcash = 0;
        try {
            if (Year <= Policyterm) {
                if (smartfutureChoiceBean.getBonusOption().equals("Deferred Cash Bonus")) {
                    a = (0.03 * dfcashfordf8) + sumcashbonus8;
                    b = (0.03 * sumofdefferedcash8);
                    Defferedcash = a + b;


//					System.out.println("\n year "+Year);
//					System.out.println("a "+a);
//					System.out.println(" b "+b);
//					System.out.println("sumcashbonus8 "+sumcashbonus8);
//					System.out.println("sumofdefferedcash8 "+sumofdefferedcash8);
//					System.out.println("dfcashfordf8 "+dfcashfordf8);
//					System.out.println(" sumcashbonus444444 "+sumcashbonus8);

                }
            } else {
                return 0;
            }

        } catch (Exception e) {
            String msg = e.getMessage();
        }
        //	System.out.println("Defferedcash"+Defferedcash);
        return Defferedcash;
    }

    public double getBackDateInterest(double grossPremium, String bkDate) {

        try {

//		double indigoRate=7.5;
            /**
             * Modified by Akshaya. Rate Change from 1-APR-2015
             */

            /******************* Modified by Akshaya on 14-APR-2015 start**********/
//		double indigoRate=10;

            /******************* Modified by Saurabh Jain on 08-APR-2019 start **********/
            //double indigoRate = 8.75;
            double indigoRate = 6.50;
            /******************* Modified by Saurabh Jain on 08-APR-2019 end **********/

            double SeriviceTax = 0;
            if (smartfutureChoiceBean.getIsJammuResident())
                SeriviceTax = prop.serviceTaxJKResident;
            else
                SeriviceTax = prop.serviceTax;

            double ServiceTaxValue = (SeriviceTax + 1) * 100;

            /******************* Modified by Akshaya on 14-APR-2015 end**********/
            int compoundFreq = 2;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            SimpleDateFormat dateformat1 = new SimpleDateFormat("dd-MM-yyyy");

            Date dtBackdate = dateformat1.parse(bkDate);
            String strBackDate = dateFormat.format(dtBackdate);


            Calendar cal = Calendar.getInstance();
//	    ////System.out.println("cal "+cal);
//	    ////System.out.println("cal "+cal.getTime());
            String date = dateFormat.format(cal.getTime());
//	    ////System.out.println("date "+date);

            /******************* Modified by Akshaya on 14-APR-2015 start**********/
//      double netPremWithoutST=Math.round((grossPremium*100)/103.09);
            double netPremWithoutST = Math.round((grossPremium * 100) / ServiceTaxValue);
            /******************* Modified by Akshaya on 14-APR-2015 end**********/
//       ////System.out.println("netPremWithoutST "+netPremWithoutST);
            int days = commonForAllProd.setDate(date, strBackDate);
//       ////System.out.println("no of days "+days);
            double monthsBetween = commonForAllProd.roundDown((double) days / 365 * 12, 0);
//       ////System.out.println("aaaaaaaaa "+(double)79/365);
//       ////System.out.println("month "+monthsBetween);

            double interest = getCompoundAmount(monthsBetween, netPremWithoutST, indigoRate, compoundFreq);
//       ////System.out.println("onterest "+interest);
            return interest;
        } catch (Exception e) {
            return 0;
        }
    }

    public double getCompoundAmount(double monthsBetween, double netPremWithoutST, double indigoRate, int compoundFreq) {
        double compoundAmount = netPremWithoutST * Math.pow((1 + (indigoRate / (100 * compoundFreq))), (compoundFreq * (monthsBetween / 12))) - netPremWithoutST;
        return compoundAmount;
//		 ////System.out.println("compoundAmount "+compoundAmount);
    }

    public double getStaffRebate(boolean IsStaff) {
        double StaffRebate = 0;
        if (IsStaff) {
            StaffRebate = 0.06;
        } else {
            StaffRebate = 0.00;
        }
        return StaffRebate;

    }
}




	

