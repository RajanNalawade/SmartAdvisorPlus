package sbilife.com.pointofsale_bancaagency.smartchamp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import sbilife.com.pointofsale_bancaagency.common.CommonForAllProd;

class SmartChampBusinessLogic {


    private CommonForAllProd cfap = null;
    private SmartChampProperties scProp = null;
    SmartChampBean smartchampbean = new SmartChampBean();

    double singleSSVFactor = 0, regularSSVFactor = 0, LimitedGSVFactor = 0, TabularPremiumRate = 0,
            staffRebate = 0, SARebate = 0, NSAP_Rate = 0, EMR = 0,
            LoadngFreqOfPremiums = 0, PF = 0, singleSSVFactor_N = 0,
            regularSSVFactor_N = 0, TotalBasePremiumPaidv = 0, TotalBasePremiumPaidValue = 0, premium_Without_ST_WithoutLoadingFreq = 0, TerminalBonus4per = 0, TerminalBonus8per = 0;
    String b, a, x, y;

    public SmartChampBusinessLogic() {
        cfap = new CommonForAllProd();
        scProp = new SmartChampProperties();

    }

    // Set Premium without ST and roundup
    public double setPremiumWithoutSTwithoutRoundUP(int age, int policyTerm,
                                                    double sumAssured, boolean staffDisc, String premFreqMode) {
        SmartChampDB smartchampDB = new SmartChampDB();
        double SA_Rebate = 0;
        double tabularPremiumRate = 0;

        staffRebate = getStaffRebate(premFreqMode, staffDisc);
        SA_Rebate = getSARebate(premFreqMode, sumAssured);
        LoadngFreqOfPremiums = getLoadingFrqOfPremium(premFreqMode, "Basic");

        tabularPremiumRate = getTabularPremiumRate(age, premFreqMode,
                policyTerm);
        /**** Added By Priyanka Warekar - 08-08-2016 - Start ****/
        // PF = getPF(premFreqMode);
        PF = 1;
        /**** Added By Priyanka Warekar - 08-08-2016 - Start ****/
        return ((tabularPremiumRate * (1.0 - staffRebate) - SARebate + (NSAP_Rate + EMR))
                * sumAssured / 1000.0 * LoadngFreqOfPremiums / PF);
    }

    // Set roundup Premium without ST
    public double setPremiumWithoutSTwithRoundUP(double premium) {
        double riderPremiumWithoutST = 0;
//    	System.out.println("rr "+((Double.parseDouble(cfap.getRoundUp(cfap.getStringWithout_E(premium+riderPremiumWithoutST))))));
        return (Double.parseDouble(cfap.getRoundUp(cfap.getStringWithout_E(premium + riderPremiumWithoutST))));
    }

    public double setServiceTax(double premiumWithST, double premiumwithRoundUP) {
        return (premiumWithST - premiumwithRoundUP);
    }

    //17-12-2019
    public double setTotalPremWithoutFreqRoundUp(double premium_Without_ST_WithoutLoadingFreq) {
        //System.out.println("premium_Without_ST_WithoutLoadingFreq Test "+premium_Without_ST_WithoutLoadingFreq);
        //return (Double.parseDouble(cfap.getRoundUp(cfap.getStringWithout_E(premium_Without_ST_WithoutLoadingFreq))));
        return (Double.parseDouble(cfap.getRoundOffLevel2New(cfap.getStringWithout_E(premium_Without_ST_WithoutLoadingFreq))));
    }

    public double getSARebate(String premFreqMode, double sumAssured) {
        if (premFreqMode.equalsIgnoreCase("Single")) {
            if (sumAssured >= 100000 && sumAssured < 200000)
                SARebate = 0;
            else if (sumAssured >= 200000 && sumAssured < 300000)
                SARebate = 6.5;
            else if (sumAssured >= 300000 && sumAssured < 500000)
                SARebate = 9.5;
            else
                SARebate = 11;
        } else {
            if (sumAssured >= 100000 && sumAssured < 200000)
                SARebate = 0;
            else if (sumAssured >= 200000 && sumAssured < 300000)
                SARebate = 3;
            else if (sumAssured >= 300000 && sumAssured < 500000)
                SARebate = 4.5;
            else
                SARebate = 5.5;
        }

        //System.out.println("SARebate "+SARebate);
        return SARebate;
    }

    public double getStaffRebate(String premFreqMode, boolean staff) {
        double staff_Rebate;
        // staffRebate
        if (staff) {
            if (premFreqMode.equalsIgnoreCase("Single"))
                staff_Rebate = 0.02;
            else
                staff_Rebate = 0.05;
        } else
            staff_Rebate = 0;
        //System.out.println("staff_Rebate "+staff_Rebate);
        return staff_Rebate;

    }

    public double getPF(String premFreqMode) {
        double pf = 0;
        if (premFreqMode.equalsIgnoreCase("Yearly")
                || premFreqMode.equalsIgnoreCase("Single"))
            pf = 1.0;
        else if (premFreqMode.equalsIgnoreCase("Half Yearly"))
            pf = 2.0;
        else if (premFreqMode.equalsIgnoreCase("Quarterly"))
            pf = 4.0;
        else
            pf = 12.0;
        return pf;
    }

    private double getLoadingFrqOfPremium(String premFreqMode, String cover) {

        double loadngFreqOfPremiums = 0;
        // Loading for Frequency Of Premiums
        if (cover.equals("Basic")) {
            if (premFreqMode.equalsIgnoreCase("Yearly")
                    || premFreqMode.equalsIgnoreCase("Single"))
                loadngFreqOfPremiums = 1;
            else if (premFreqMode.equalsIgnoreCase("Half Yearly"))
                loadngFreqOfPremiums = scProp.HalfYearly_Modal_Factor;
            else if (premFreqMode.equalsIgnoreCase("Quarterly"))
                loadngFreqOfPremiums = scProp.Quarterly_Modal_Factor;
            else
                loadngFreqOfPremiums = scProp.Monthly_Modal_Factor;
        } else {
            if (premFreqMode.equalsIgnoreCase("Yearly")
                    || premFreqMode.equalsIgnoreCase("Single"))
                loadngFreqOfPremiums = 1;
            else if (premFreqMode.equalsIgnoreCase("Half Yearly"))
                loadngFreqOfPremiums = 1.04;
            else if (premFreqMode.equalsIgnoreCase("Quarterly"))
                loadngFreqOfPremiums = 1.06;
            else
                loadngFreqOfPremiums = 1.068;
        }

        //System.out.println("loadngFreqOfPremiums "+loadngFreqOfPremiums);
        return loadngFreqOfPremiums;
    }

    //added by sujata
    public double getTabularPremiumRate(int age, String premFreqMode, int policyTerm) {
        SmartChampDB smartchampDB = new SmartChampDB();
        if (age == -1)
            TabularPremiumRate = 0;
        else {
            if (premFreqMode.equalsIgnoreCase("Single")) {
                double[] premiumArr = smartchampDB.getSingleSCRates();
                int position = 0;
                TabularPremiumRate = 0;
                for (int ageCount = 21; ageCount <= 50; ageCount++) {
                    for (int policyTermCount = 8; policyTermCount <= 21; policyTermCount++) {
                        if (ageCount == age && policyTermCount == policyTerm) {
                            TabularPremiumRate = premiumArr[position];
                        }
                        position++;
                    }
                }
            } else {
                double[] premiumArr = smartchampDB.getLimitedSCRates();
                int position = 0;
                TabularPremiumRate = 0;
                for (int ageCount = 21; ageCount <= 50; ageCount++) {
                    for (int policyTermCount = 8; policyTermCount <= 21; policyTermCount++) {
                        if (ageCount == age && policyTermCount == policyTerm) {
                            TabularPremiumRate = premiumArr[position];
                        }
                        position++;
                    }
                }
            }
        }

        //System.out.println("TabularPremiumRate "+TabularPremiumRate);
        return TabularPremiumRate;
    }

    //26-11-2019
    public double setTotalBasePPaid(double BasePremiumPaid, int year_F,
                                    int premiumPayingTerm, String premFreqMode, int age, int policyTerm, String cover, double sumAssured, double staff_Rebate, double TotalBasePremiumPaid) {
        int premmultification = setPremiumMultiplication(premFreqMode);
        if (premFreqMode.equalsIgnoreCase("Single") && year_F >= 2) {
            TotalBasePremiumPaidv = 0;
            //String b;
            return 0;
        } else {
            if (year_F <= premiumPayingTerm) {
                TotalBasePremiumPaidv = (TotalBasePremiumPaid * premmultification);
                //TotalBasePremiumPaid = (BasePremiumPaid * year_F * premmultification);
                //TotalBasePremiumPaid = setTotalBasePremiumPaid(age,policyTerm,premFreqMode,cover ,sumAssured ,staff_Rebate)
                //		 * premmultification;

                //b = cfap.getRoundUp(cfap.getStringWithout_E(TotalBasePremiumPaid));
                //TotalBasePremiumPaidValue = Double.parseDouble(b);
                //TotalBasePremiumPaid = (TotalBasePremiumPaid * premmultification);
                //	System.out.println("BasePremiumPaid "+BasePremiumPaid * premmultification);
                //System.out.println("TotalBasePremiumPaidv "+TotalBasePremiumPaidv);
                return TotalBasePremiumPaidv;
            } else {
                //System.out.println("TotalBasePremiumPaid Factor:  "+TotalBasePremiumPaidv);
                TotalBasePremiumPaidv = 0;
                return TotalBasePremiumPaidv;
            }
        }
    }

    //26-11-2019
    public double setTotalBasePremiumPaid(int age, int policyTerm, String premFreqMode, String cover, double sumAssured, double staff_Rebate) {
//
//    	if(year_f <= premiumPayingTerm)
//    	{
        double pf = 0;
        if (premFreqMode.equalsIgnoreCase("Yearly") || premFreqMode.equalsIgnoreCase("Single"))
            pf = 1.0;
        else if (premFreqMode.equalsIgnoreCase("Half Yearly"))
            pf = 2.0;
        else if (premFreqMode.equalsIgnoreCase("Quarterly"))
            pf = 4.0;
        else
            pf = 12.0;

//    	System.out.println("\nrate " +getTabularPremiumRate(age,premFreqMode,policyTerm));
//    	System.out.println("staff rebate "+ staff_Rebate);
//    	System.out.println("SA Rebate "+getSARebate(premFreqMode,sumAssured));
//    	System.out.println("SumAsured"+ sumAssured );
//    	System.out.println("Loading Frequency "+ getLoadingFrqOfPremium(premFreqMode,cover));
//    	System.out.println("BASE : "+ (( getTabularPremiumRate(age,premFreqMode,policyTerm) *(1- staff_Rebate)- getSARebate(premFreqMode,sumAssured)
//        		+(NSAP_Rate + EMR)) * sumAssured /1000 * getLoadingFrqOfPremium(premFreqMode,cover)/1));
        // 	System.out.println("basicpremium "+(getTabularPremiumRate(age,premFreqMode,policyTerm) *(1- staff_Rebate)- getSARebate(premFreqMode,sumAssured)
        //     		+(NSAP_Rate + EMR)) * sumAssured /1000 * getLoadingFrqOfPremium(premFreqMode,cover)/1);
        return ((getTabularPremiumRate(age, premFreqMode, policyTerm) * (1 - staff_Rebate) - getSARebate(premFreqMode, sumAssured)
                + (NSAP_Rate + EMR)) * sumAssured / 1000 * getLoadingFrqOfPremium(premFreqMode, cover) / 1);
    }
//    	else
//    	{
//    		return 0;
//    	}


    //26-11-2019
    public double set_Premium_Without_ST_WithoutLoadingFreq(int age, int policyTerm, String premFreqMode, String cover, double sumAssured, double staff_Rebate) {
        double pf = 0, premium_Without_ST_WithoutLoadingFreq;
        if (premFreqMode.equalsIgnoreCase("Yearly") || premFreqMode.equalsIgnoreCase("Single"))
            pf = 1.0;
        else if (premFreqMode.equalsIgnoreCase("Half Yearly"))
            pf = 2.0;
        else if (premFreqMode.equalsIgnoreCase("Quarterly"))
            pf = 4.0;
        else
            pf = 12.0;
        premium_Without_ST_WithoutLoadingFreq = ((getTabularPremiumRate(age, premFreqMode, policyTerm) * (1 - staff_Rebate) - getSARebate(premFreqMode, sumAssured)
                + (NSAP_Rate + EMR)) * sumAssured / 1000);

        //System.out.println("premium_Without_ST_WithoutLoadingFreq "+premium_Without_ST_WithoutLoadingFreq);
        return premium_Without_ST_WithoutLoadingFreq;
    }


    public double get_Premium_Without_ST_WithoutLoadingFreq() {
        return premium_Without_ST_WithoutLoadingFreq;
    }






   /* public double  get_Guarateed_add(int year_F,double sumAssured,int policyTerm,double totalBasePremiumPaid
            , double SAMultipleForDeathBenifitToBeConsider, double APWithoutModalLoading) {
        if (year_F <= 1)
            return 0;
        else
            return setGuaranteedDeathBenefit(sumAssured, year_F, policyTerm, totalBasePremiumPaid,
                    SAMultipleForDeathBenifitToBeConsider, APWithoutModalLoading);

    }*/

    //added by sujata on 23-12-2019
    //10-01-2020
    public double setAnnulized_Premium(int year_F, int premiumPayingTerm, double PremiumRoundUpWithoutLoading, String premFreqMode) {
        if (year_F >= 2 && premFreqMode.equalsIgnoreCase("Single")) {
            return 0;
        }
        if (year_F <= premiumPayingTerm) {
            //System.out.println("premiumwithRoundUP "+premiumwithRoundUP,PremiumRoundUpWithoutLoading);
            return PremiumRoundUpWithoutLoading;
        } else {
            return 0;
        }
    }

    public double Maturity_Benefit(double sumAssured, int premiumPayingTerm, int policyTerm, int year_f) {
        if (year_f >= premiumPayingTerm) {
            if (year_f == policyTerm)
                return Math.round(sumAssured * 0.25);
            else
                return 0;
        } else
            return 0;
    }

    public double Reversionary_Bonus4pr(
            String premFreqMode,
            double sumAssured, int year_F, int premiumPayingTerm,
            int policyTerm) {
        return setNonGuarateedDeathBenefitAt_4_Percent(premFreqMode,
                sumAssured, year_F, premiumPayingTerm,
                policyTerm);
        //return smartMoneyBackGoldBean.getSumAssured_Basic()* 0.015 * year_F;

    }

    //added by sujata
    public double Reversionary_Bonus8pr(
            String premFreqMode,
            double sumAssured, int year_F, int premiumPayingTerm,
            int policyTerm) {
        return setNonGuarateedDeathBenefitAt_8_Percent(premFreqMode,
                sumAssured, year_F, premiumPayingTerm,
                policyTerm);
        //return smartMoneyBackGoldBean.getSumAssured_Basic()* 0.015 * year_F;

    }

    //added by sujata on 05-12-2019
    public double setTotalMaturity_Ben4per(double sumAssured, int premiumPayingTerm, int policyTerm, int year_f
            , String premFreqMode, int year_F, double sumsurvivalbenyerr20) {
        if (year_f == policyTerm) {
            //	System.out.println("\nMaturity_Benefit " +Maturity_Benefit(sumAssured, premiumPayingTerm, policyTerm,year_f));
//    		System.out.println("Reversionary_Bonus4pr "+Reversionary_Bonus4pr(
//            		premFreqMode, sumAssured,  year_F, premiumPayingTerm, policyTerm));
//    		System.out.println("sumsurvivalbenyerr20 "+sumsurvivalbenyerr20);
            return Maturity_Benefit(sumAssured, premiumPayingTerm, policyTerm, year_f) + Reversionary_Bonus4pr(
                    premFreqMode, sumAssured, year_F, premiumPayingTerm, policyTerm) + sumsurvivalbenyerr20;

        } else {
            return 0;
        }
    }

    public double setTotalMaturity_Ben8per(double sumAssured, int premiumPayingTerm, int policyTerm, int year_f
            , String premFreqMode, int year_F, double sumsurvivalben8perYear20) {
        if (year_f == policyTerm) {
            return Maturity_Benefit(sumAssured, premiumPayingTerm, policyTerm, year_f) + Reversionary_Bonus8pr(
                    premFreqMode, sumAssured, year_F, premiumPayingTerm, policyTerm) + sumsurvivalben8perYear20;
        } else {
            return 0;
        }
    }

    //added by sujata on 27-11-2019
    //16-12-2019
    public double TotalDeathBenefit_4percent(double sumAssured, int year_F, int policyTerm, int age, double totalBasePremiumPaid,
                                             double SAMultipleForDeathBenifitToBeConsider, double APWithoutModalLoading, String premFreqMode,
                                             int premiumPayingTerm, double PremiumRoundUpWithoutLoading, double FirstYearOfTotBasePrem, double PremiumBasic, double FirstYearOfAnnulizedPrem) {

        return setGuaranteedDeathBenefit(sumAssured, year_F, policyTerm, age, totalBasePremiumPaid,
                SAMultipleForDeathBenifitToBeConsider, APWithoutModalLoading, premiumPayingTerm, premFreqMode, PremiumRoundUpWithoutLoading, FirstYearOfTotBasePrem, PremiumBasic, FirstYearOfAnnulizedPrem);

    }

    //16-12-2019
    public double TotalDeathBenefit_8percent(double sumAssured, int year_F, int policyTerm, int age, double totalBasePremiumPaid,
                                             double SAMultipleForDeathBenifitToBeConsider, double APWithoutModalLoading, String premFreqMode,
                                             int premiumPayingTerm, double PremiumRoundUpWithoutLoading, double FirstYearOfTotBasePrem, double PremiumBasic, double FirstYearOfAnnulizedPrem) {
        return setGuaranteedDeathBenefit(sumAssured, year_F, policyTerm, age, totalBasePremiumPaid,
                SAMultipleForDeathBenifitToBeConsider, APWithoutModalLoading, premiumPayingTerm, premFreqMode, PremiumRoundUpWithoutLoading, FirstYearOfTotBasePrem, PremiumBasic, FirstYearOfAnnulizedPrem);

    }


    ////end


    /**
     * As per the changes from 1st Apr 2015, by Vrushali Chaudhari
     *
     */
//    public double setPremiumWithST(double premiumWithoutST,boolean JKResident)
    // {
    // if(JKResident==true)
    // return(Double.parseDouble(cfap.getRoundUp((cfap.getStringWithout_E(((scProp.serviceTaxJkResident+1)*premiumWithoutST))))));
    // else
    // return(Double.parseDouble(cfap.getRoundUp(cfap.getStringWithout_E(((scProp.serviceTax+1)*premiumWithoutST)))));
    // }

    /*** modified by Akshaya on 20-MAY-16 start **/

    public double getServiceTax(double premiumWithoutST, boolean JKResident,
                                String type) {
        if (type.equals("basic")) {
            // System.out.println();
            if (JKResident)
                return Double.parseDouble(cfap.getRoundUp(cfap
                        .roundUp_Level4(String.valueOf(premiumWithoutST
                                * scProp.serviceTaxJkResident))));
            else {
                return Double.parseDouble(cfap.getRoundUp(cfap
                        .roundUp_Level4(String.valueOf(premiumWithoutST
                                * scProp.serviceTax))));
            }
        } else if (type.equals("SBC")) {
            // System.out.println(String.valueOf(premiumWithoutST*scProp.SBCServiceTax));
            if (JKResident)
                return 0;
            else {
                return Double.parseDouble(cfap.getRoundUp(String
                        .valueOf(premiumWithoutST * scProp.SBCServiceTax)));
            }
        }
        //  Added By Saurabh Jain on 14/05/2019 Start
        else if (type.equals("KERALA")) {

            return Double.parseDouble(cfap.getRoundUp(String.valueOf(premiumWithoutST * scProp.kerlaServiceTax)));
        }
        //  Added By Saurabh Jain on 14/05/2019 End
        else //KKC
        {
            // System.out.println(String.valueOf(premiumWithoutST*scProp.KKCServiceTax));
            if (JKResident)
                return 0;
            else {
                return Double.parseDouble(cfap.getRoundUp(String
                        .valueOf(premiumWithoutST * scProp.KKCServiceTax)));
            }
        }

    }

    public double getServiceTaxSecondYear(double premiumWithoutST,
                                          boolean JKResident, String type) {
        if (type.equals("basic")) {
            if (JKResident)
                return Double
                        .parseDouble(cfap.getRoundUp(String
                                .valueOf(premiumWithoutST
                                        * scProp.serviceTaxJkResident)));
            else {
                return Double
                        .parseDouble(cfap.getRoundUp(String
                                .valueOf(premiumWithoutST
                                        * scProp.serviceTaxSecondYear)));
            }
        } else if (type.equals("SBC")) {
            if (JKResident)
                return 0;
            else {
                return Double.parseDouble(cfap.getRoundUp(String
                        .valueOf(premiumWithoutST
                                * scProp.SBCServiceTaxSecondYear)));
            }
        }
        //  Added By Saurabh Jain on 14/05/2019 Start
        else if (type.equals("KERALA")) {

            return Double.parseDouble(cfap.getRoundUp(String.valueOf(premiumWithoutST * scProp.kerlaServiceTaxSecondYear)));
        }
        //  Added By Saurabh Jain on 14/05/2019 End
        else //KKC
        {
            if (JKResident)
                return 0;
            else {
                return Double.parseDouble(cfap.getRoundUp(String
                        .valueOf(premiumWithoutST
                                * scProp.KKCServiceTaxSecondYear)));
            }
        }

    }

    /*** modified by Akshaya on 20-MAY-16 end **/

    public int setPremiumMultiplication(String premFreqMode) {
        if (premFreqMode.equalsIgnoreCase("Yearly")
                || premFreqMode.equalsIgnoreCase("Single"))
            return 1;
        else if (premFreqMode.equalsIgnoreCase("Half Yearly"))
            return 2;
        else if (premFreqMode.equalsIgnoreCase("Quarterly"))
            return 4;
        else
            return 12;
    }

  /*  public double setGuaranteedDeathBenefit(double sumAssured,int year_F,int policyTerm)
    {
		if (year_F <= policyTerm)
			return sumAssured;
		else
			return 0;
    }*/

    //26-11-2019 by sujata
    //added by sujata on 06-12-2019 extra parameter added as TotalBasePremiumPaidWithoutFreqLoading

    //16-12-2019
    public double setGuaranteedDeathBenefit(double sumAssured, int year_F, int policyTerm, int age, double totalBasePremiumPaid,
                                            double SAMultipleForDeathBenifitToBeConsider, double APWithoutModalLoading, int premiumPayingTerm, String premFreqMode
            , double PremiumRoundUpWithoutLoading, double FirstYearOfTotBasePrem, double PremiumBasic, double FirstYearOfAnnulizedPrem) {
        double a = 0, b = 0, maximumValue1, maximumValue2;
        if (year_F <= policyTerm) {
            if (year_F <= premiumPayingTerm) {
                a = 1.05 * PremiumBasic * year_F;
                //System.out.println("\n year_F "+year_F);
                //System.out.println("a "+a);
//    			System.out.println("PremiumBasic "+PremiumBasic);
            }
            if (year_F > premiumPayingTerm) {
                //a = 1.05*totalBasePremiumPaid*year_F;
                if (premFreqMode.equals("Single")) {
                    premiumPayingTerm = 1;
                }

                a = 1.05 * premiumPayingTerm * FirstYearOfTotBasePrem;
                //System.out.println("premiumPayingTerm "+premiumPayingTerm);

//    			System.out.println("elsevaluea "+a);
//    			System.out.println("FirstYearOfTotBasePrem "+FirstYearOfTotBasePrem);
            }
            //System.out.println("totalBasePremiumPaid "+totalBasePremiumPaid);
            if (premFreqMode.equalsIgnoreCase("Single")) {
                //	b = 1.25 * setAnnulized_Premium(year_F,premiumPayingTerm ,TotalBasePremiumPaidWithoutFreqLoading);
                b = 1.25 * FirstYearOfAnnulizedPrem;
                //	System.out.println("single death "+b);
            } else {
                String x;
                x = (cfap.getRoundUp(cfap.getStringWithout_E((PremiumRoundUpWithoutLoading))));
                b = 10 * (Double.parseDouble(x));
//    			System.out.println("10*paid  "+b);
//    			System.out.println("PremiumRoundUpWithoutLoading Inside "+x);
            }

            maximumValue1 = Math.max(sumAssured, a);
            maximumValue2 = Math.max(maximumValue1, b);
            //	System.out.println("sumAssured "+sumAssured);
            //	System.out.println("value " +a);

            return maximumValue2;
        } else {
            return 0;
        }


    }

    //added by sujata on 27-11-2019
    public double setSAMultipleForDeathBenifitToBeConsider(String premFreqMode, int age, int policyTerm) {
        if (premFreqMode.equalsIgnoreCase("Single")) {
            if (age < 45) {
                return 1.25;
            } else {
                return 1.1;
            }
        } else {
            if (policyTerm < 10) {
                return 5;
            } else {
                if (age < 45) {
                    return 10;
                } else {
                    return 7;
                }
            }
        }
    }

    public double setAPWithoutModalLoading(int age, String premFreqMode, int policyTerm, boolean staff, double sumAssured) {
        double a = Double.parseDouble(cfap.getRoundUp(cfap.getStringWithout_E((getTabularPremiumRate(age, premFreqMode, policyTerm) * (1 - getStaffRebate(premFreqMode, staff)) - getSARebate(premFreqMode, sumAssured)) * sumAssured / 1000)));
        return a;
    }

    /* Mines logic starts from here */
    /****** Added By Tushar Kotian on 17/4/2017 start *****/
    /********************* Backdate Mines logic start *******************************************************/

    public double getMinesOccuInterest(double SumAssured_Basic) {
        return (SumAssured_Basic / 1000) * 2;
    }


    public double getBackDateInterest(double grossPremium, String bkDate) {

        try {

            // double indigoRate=7.5;
            /**
             * Modified by Akshaya. Rate Change from 1-APR-2015
             */
            double ST = 0;
            if (smartchampbean.getIsJKResidentDiscOrNot()) {
                ST = scProp.serviceTaxJkResident;
            } else {
                ST = scProp.serviceTax;
            }
            /******************* Modified by Akshaya on 14-APR-2015 start **********/
//			double indigoRate = 10;

            /******************* Modified by Saurabh Jain on 08-APR-2019 start **********/
            //double indigoRate = 8.75;
            double indigoRate = 6.50;
            /******************* Modified by Saurabh Jain on 08-APR-2019 end **********/

            double ServiceTaxValue = (ST + 1) * 100;

            /******************* Modified by Akshaya on 14-APR-2015 end **********/
            int compoundFreq = 2;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            SimpleDateFormat dateformat1 = new SimpleDateFormat("dd-MM-yyyy");

            Date dtBackdate = dateformat1.parse(bkDate);
            String strBackDate = dateFormat.format(dtBackdate);

            Calendar cal = Calendar.getInstance();
            // System.out.println("cal "+cal);
            // System.out.println("cal "+cal.getTime());
            String date = dateFormat.format(cal.getTime());
            // System.out.println("date "+date);

            /******************* Modified by Akshaya on 14-APR-2015 start **********/
            // double netPremWithoutST=Math.round((grossPremium*100)/103.09);
            double netPremWithoutST = Math.round((grossPremium * 100)
                    / ServiceTaxValue);
            /******************* Modified by Akshaya on 14-APR-2015 end **********/
            // System.out.println("netPremWithoutST "+netPremWithoutST);
            int days = cfap.setDate(date, strBackDate);
            // System.out.println("no of days "+days);
            double monthsBetween = cfap.roundDown(
                    (double) days / 365 * 12, 0);
            // System.out.println("aaaaaaaaa "+(double)79/365);
            // System.out.println("month "+monthsBetween);

            double interest = getCompoundAmount(monthsBetween,
                    netPremWithoutST, indigoRate, compoundFreq);
            // System.out.println("onterest "+interest);
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


    /****** Added By Tushar Kotian on 17/4/2017 end *****/


    public double setYearlyPremiumWithST(double premiumWithST, String premFreqMode) {
        if (premFreqMode.equalsIgnoreCase("Monthly") || premFreqMode.equalsIgnoreCase("Monthly SSS") || premFreqMode.equalsIgnoreCase("Monthly ECS") || premFreqMode.equalsIgnoreCase("Monthly SI/CC"))
            return premiumWithST * 12;
        else if (premFreqMode.equalsIgnoreCase("Quarterly"))
            return premiumWithST * 4;
        else if (premFreqMode.equalsIgnoreCase("Half Yearly"))
            return premiumWithST * 2;
        else if (premFreqMode.equalsIgnoreCase("Yearly"))
            return premiumWithST;
        else
            return premiumWithST;
    }


//    public double  setNonGuarateedDeathBenefitAt_4_Percent(double GuaranteedDeathBenefit,int year_F,int premiumPayingTerm,int policyTerm)
    // { double a,b;
    // if(year_F<=premiumPayingTerm)
    // a= Math.round(year_F * GuaranteedDeathBenefit * (getbonusrate4()));
    // else if(year_F==policyTerm)
//    		a=Math.round(premiumPayingTerm * GuaranteedDeathBenefit * (getbonusrate4()));
    // else
    // a=0;
    // if(year_F==policyTerm)
    // b=scProp.terminalBonus/100;
    // else
    // b=1/100;
    //
    // return (a*b);
    // }

//    public double  setNonGuarateedDeathBenefitAt_8_Percent(double GuaranteedDeathBenefit,int year_F,int premiumPayingTerm,int policyTerm)
    // { double a,b;
    // if(year_F<=premiumPayingTerm)
    // a= Math.round(year_F * GuaranteedDeathBenefit * (getbonusrate8()));
    // else if(year_F==policyTerm)
//		a=Math.round(premiumPayingTerm * GuaranteedDeathBenefit * (getbonusrate8()));
    // else
    // a=0;
    // if(year_F==policyTerm)
    // b=scProp.terminalBonus/100;
    // else
    // b=1/100;
    //
    // return (a*b);
    // }

    //Added by sujata on 10-12-2019
    public double setGuaranteedSurvivalBenefit(double sumAssured,
                                               int premiumPayingTerm, int policyTerm, int year_f) {
        if (year_f >= premiumPayingTerm) {
            if (year_f < policyTerm)
                return Math.round(sumAssured * 0.25);
            else
                return 0;
        } else
            return 0;
    }

    public double setNonGuarateedSurvivalBenefitAt_4_Percent(
            double NonGuarateedDeathBenefitAt_4_Percent, int premiumPayingTerm,
            int policyTerm, int year_f, double sumAssured) {
        double a = 0;
        if (year_f >= premiumPayingTerm) {
            if (year_f < policyTerm)
                a = ((premiumPayingTerm * sumAssured * 0.019) * 0.25);
            else if (year_f == policyTerm)
                a = ((premiumPayingTerm * sumAssured * 0.019) * 0.25)
                        + NonGuarateedDeathBenefitAt_4_Percent;
            else
                a = 0;
        } else
            a = 0;
        return a;
    }

    public double setNonGuarateedSurvivalBenefitAt_8_Percent(
            double NonGuarateedDeathBenefitAt_8_Percent, int premiumPayingTerm,
            int policyTerm, int year_f, double sumAssured) {
        double a = 0;
        if (year_f >= premiumPayingTerm) {
            if (year_f < policyTerm)
                a = ((premiumPayingTerm * sumAssured * 0.03) * 0.25);
            else if (year_f == policyTerm)
                a = ((premiumPayingTerm * sumAssured * 0.03) * 0.25)
                        + NonGuarateedDeathBenefitAt_8_Percent;
            else
                a = 0;
        } else
            a = 0;
        return a;
    }

    public double setTotalSurvivalBenefitAt_4_Percent(
            double GuaranteedSurvivalBenefit,
            double NonGuarateedSurvivalBenefitAt_4_Percent) {
        //System.out.println("GuaranteedSurvivalBenefit "+GuaranteedSurvivalBenefit);
        return (GuaranteedSurvivalBenefit + NonGuarateedSurvivalBenefitAt_4_Percent);
    }

    public double setTotalSurvivalBenefitAt_8_Percent(
            double GuaranteedSurvivalBenefit,
            double NonGuarateedSurvivalBenefitAt_8_Percent) {
        return (GuaranteedSurvivalBenefit + NonGuarateedSurvivalBenefitAt_8_Percent);
    }

    /*************** mofified by Akshaya on 18-Feb-15 start **************/

    public double setPremiumWithoutSTAndDiscwithoutRoundUP(int age,
                                                           int policyTerm, double sumAssured, boolean staffDisc,
                                                           String premFreqMode) {
        SmartChampDB smartchampDB = new SmartChampDB();
        double SA_Rebate = 0;
        double tabularPremiumRate = 0;

        staffRebate = getStaffRebate(premFreqMode, staffDisc);
        SA_Rebate = getSARebate(premFreqMode, sumAssured);
        LoadngFreqOfPremiums = getLoadingFrqOfPremium(premFreqMode, "Basic");

        tabularPremiumRate = getTabularPremiumRate(age, premFreqMode,
                policyTerm);
        /**** Added By Priyanka Warekar - 08-08-2016 - Start ****/
        // PF = getPF(premFreqMode);
        PF = 1;
        /**** Added By Priyanka Warekar - 08-08-2016 - end ****/
        return ((tabularPremiumRate * (1.0 - 0) - SARebate + (NSAP_Rate + EMR))
                * sumAssured / 1000.0 * LoadngFreqOfPremiums / PF);
    }

    /*************** mofified by Akshaya on 18-Feb-15 end **************/

	/*public double setNonGuarateedDeathBenefitAt_4_Percent(String premFreqMode,
			double GuaranteedDeathBenefit, int year_F, int premiumPayingTerm,
			int policyTerm) {
		double a = 0, b=0;

	// if (premFreqMode.equalsIgnoreCase("Single")) {
	// if (year_F <= premiumPayingTerm)
	// a = Math.round(year_F * GuaranteedDeathBenefit
	// * (scProp.singleBonusInvestmnt_4_percent / 100));
	// else if (year_F == policyTerm)
	// a = Math.round(premiumPayingTerm * GuaranteedDeathBenefit
	// * (scProp.singleBonusInvestmnt_4_percent / 100));
	// else
	// a = 0;
	// } else {
	// if (year_F <= premiumPayingTerm)
	// a = Math.round(year_F * GuaranteedDeathBenefit
	// * (getbonusrate4(premFreqMode, policyTerm)));
	// else if (year_F == policyTerm)
	// a = Math.round(premiumPayingTerm * GuaranteedDeathBenefit
	// * (getbonusrate4(premFreqMode, policyTerm)));
	// else
	// a = 0;
	//
	// }

		*//**** Modified by Tushar Kotian on 12/4/2017   start *****//*
			if (year_F <= premiumPayingTerm){
				a = Math.round(year_F * GuaranteedDeathBenefit
						* getbonusrate4(premFreqMode, policyTerm));
			}
			else if (year_F == policyTerm)
			{
				a = Math.round(premiumPayingTerm * GuaranteedDeathBenefit
						* getbonusrate4(premFreqMode, policyTerm));
			}
		*/

    /**** Modified by Tushar Kotian on 12/4/2017   End *****//*
		if (year_F == policyTerm)
			b = scProp.terminalBonus / 100;
		else
			b = 1;

		return (a * b);
	}
	*/
    public double setNonGuarateedDeathBenefitAt_4_Percent(String premFreqMode,
                                                          double sumAssured, int year_F, int premiumPayingTerm,
                                                          int policyTerm) {
        double temp1 = 0;
        double temp2 = 0;
        if (year_F <= premiumPayingTerm) {
            temp1 = Math.round(year_F * sumAssured
                    * getbonusrate4(premFreqMode, policyTerm));
//			System.out.println("temp "+temp1);
        } else if (year_F == policyTerm) {
            temp1 = Math.round(premiumPayingTerm * sumAssured
                    * getbonusrate4(premFreqMode, policyTerm));
        }
        if (year_F == policyTerm)
            temp2 = scProp.terminalBonus / 100;
        else
            temp2 = 1;

//		System.out.println("rev4death "+(temp1 * temp2));
        return (temp1 * temp2);
    }


	/*public double setNonGuarateedDeathBenefitAt_8_Percent(String premFreqMode,
			double GuaranteedDeathBenefit, int year_F, int premiumPayingTerm,
			int policyTerm) {
		double a = 0, b=0;

	// if (premFreqMode.equalsIgnoreCase("Single")) {
	// if (year_F <= premiumPayingTerm)
	// a = Math.round(year_F * GuaranteedDeathBenefit
	// * (scProp.singleBonusInvestmnt_8_percent / 100));
	// else if (year_F == policyTerm)
	// a = Math.round(premiumPayingTerm * GuaranteedDeathBenefit
	// * (scProp.singleBonusInvestmnt_8_percent / 100));
	// else
	// a = 0;
	// } else {
	// if (year_F <= premiumPayingTerm)
	// a = Math.round(year_F * GuaranteedDeathBenefit
	// * (getbonusrate8(premFreqMode, policyTerm)));
	// else if (year_F == policyTerm)
	// a = Math.round(premiumPayingTerm * GuaranteedDeathBenefit
	// * (getbonusrate8(premFreqMode, policyTerm)));
	// else
	// a = 0;
	// }
		*//**** Modified by Tushar Kotian on 12/4/2017   start *****//*
		if (year_F <= premiumPayingTerm){
			a = Math.round(year_F * GuaranteedDeathBenefit
					* getbonusrate8(premFreqMode, policyTerm));
		}
		else if (year_F == policyTerm)
		{
			a = Math.round(premiumPayingTerm * GuaranteedDeathBenefit
					* getbonusrate8(premFreqMode, policyTerm));
		}
	*/

    /**** Modified by Tushar Kotian on 12/4/2017   End *****//*
		if (year_F == policyTerm)
			b = scProp.terminalBonus / 100;
		else
			b = 1;

		return (a * b);
	}*/
    public double setNonGuarateedDeathBenefitAt_8_Percent(String premFreqMode,
                                                          double sumAssured, int year_F, int premiumPayingTerm,
                                                          int policyTerm) {
        double temp1 = 0;
        double temp2 = 0;
        if (year_F <= premiumPayingTerm) {
            temp1 = Math.round(year_F * sumAssured
                    * getbonusrate8(premFreqMode, policyTerm));
        } else if (year_F == policyTerm) {
            temp1 = Math.round(premiumPayingTerm * sumAssured
                    * getbonusrate8(premFreqMode, policyTerm));
        }
        if (year_F == policyTerm)
            temp2 = scProp.terminalBonus / 100;
        else
            temp2 = 1;

        return (temp1 * temp2);
    }


    public double getbonusrate4(String premFreqMode, int policyTerm) {
        double bonusrate4 = 0;

        if (premFreqMode.equals("Single")) {
            if (policyTerm >= 8 && policyTerm <= 11) {
                bonusrate4 = 0.015;
            } else if (policyTerm >= 12 && policyTerm <= 16) {
                bonusrate4 = 0.015;
            } else if (policyTerm >= 17 && policyTerm <= 21) {
                bonusrate4 = 0.013;
            }
        } else {

            if (policyTerm >= 8 && policyTerm <= 11) {
                bonusrate4 = 0.014;
            } else if (policyTerm >= 12 && policyTerm <= 16) {
                bonusrate4 = 0.015;
            } else if (policyTerm >= 17 && policyTerm <= 21) {
                bonusrate4 = 0.013;
            }

        }
        return bonusrate4;

    }

    private double getbonusrate8(String premFreqMode, int policyterm) {
        double bonusrate8 = 0;

        if (premFreqMode.equals("Single")) {
            if (policyterm >= 8 && policyterm <= 11) {
                bonusrate8 = 0.057;
            } else if (policyterm >= 12 && policyterm <= 16) {
                bonusrate8 = 0.058;
            } else if (policyterm >= 17 && policyterm <= 21) {
                bonusrate8 = 0.06;
            }
        } else {

            if (policyterm >= 8 && policyterm <= 11) {
                bonusrate8 = 0.041;
            } else if (policyterm >= 12 && policyterm <= 16) {
                bonusrate8 = 0.04;
            } else if (policyterm >= 17 && policyterm <= 21) {
                bonusrate8 = 0.037;
            }

        }
        return bonusrate8;

    }

    public double setNonGuarateedSurvivalBenefitAt_4_Percent(
            String premFreqMode, double NonGuarateedDeathBenefitAt_4_Percent,
            int premiumPayingTerm, int policyTerm, int year_f, double sumAssured) {
        double a = 0;
        // if (premFreqMode.equalsIgnoreCase("Single")) {
        //
        // if (year_f >= premiumPayingTerm) {
        // if (year_f < policyTerm)
        // a = ((premiumPayingTerm * sumAssured * (0.0125)) * 0.25);
        // else if (year_f == policyTerm)
        // a = ((premiumPayingTerm * sumAssured * (0.0125)) * 0.25)
        // + NonGuarateedDeathBenefitAt_4_Percent;
        // else
        // a = 0;
        // } else
        // a = 0;
        // } else {
        // if (year_f >= premiumPayingTerm) {
        // if (year_f < policyTerm)
        // a = ((premiumPayingTerm * sumAssured * (0.019)) * 0.25);
        // else if (year_f == policyTerm)
        // a = ((premiumPayingTerm * sumAssured * (0.019)) * 0.25)
        // + NonGuarateedDeathBenefitAt_4_Percent;
        // else
        // a = 0;
        // } else
        // a = 0;
        // }

        //added by sujataon 28-11-2019
        /**** Modified By Tushar Kotian 12/04/2017 start ****/
        if (year_f >= premiumPayingTerm) {
            if (year_f < policyTerm) {
                a = ((premiumPayingTerm * sumAssured * getbonusrate4(premFreqMode, policyTerm)) * 0.25);

                //System.out.println("SS "+a);
            }
	/*	else if (year_f == policyTerm)
		{
                a = ((premiumPayingTerm * sumAssured * getbonusrate4(premFreqMode, policyTerm)) * 0.25)
                        + NonGuarateedDeathBenefitAt_4_Percent;
            }
		}*/
            else {
                a = 0;
            }
        } else {
            return 0;
        }
        /**** Modified By Tushar Kotian 12/04/2017 end ****/
        return a;
    }

    public double setNonGuarateedSurvivalBenefitAt_8_Percent(
            String premFreqMode, double NonGuarateedDeathBenefitAt_8_Percent,
            int premiumPayingTerm, int policyTerm, int year_f, double sumAssured) {
        double a = 0;
        // if (premFreqMode.equalsIgnoreCase("Single")) {
        // if (year_f >= premiumPayingTerm) {
        // if (year_f < policyTerm)
        // a = ((premiumPayingTerm * sumAssured * (0.0375)) * 0.25);
        // else if (year_f == policyTerm)
        // a = ((premiumPayingTerm * sumAssured * (0.0375)) * 0.25)
        // + NonGuarateedDeathBenefitAt_8_Percent;
        // else
        // a = 0;
        // } else
        // a = 0;
        // } else {
        // if (year_f >= premiumPayingTerm) {
        // if (year_f < policyTerm)
        // a = ((premiumPayingTerm * sumAssured * (0.03)) * 0.25);
        // else if (year_f == policyTerm)
        // a = ((premiumPayingTerm * sumAssured * (0.03)) * 0.25)
        // + NonGuarateedDeathBenefitAt_8_Percent;
        // else
        // a = 0;
        // } else
        // a = 0;
        // }


        /**** Modified By Tushar Kotian 12/04/2017 start ****/
        if (year_f >= premiumPayingTerm) {
            if (year_f < policyTerm) {
                a = ((premiumPayingTerm * sumAssured * getbonusrate8(premFreqMode, policyTerm)) * 0.25);
            }
		/*else if (year_f == policyTerm)
		{
                a = ((premiumPayingTerm * sumAssured * getbonusrate8(premFreqMode, policyTerm)) * 0.25)
                        + NonGuarateedDeathBenefitAt_8_Percent;
		}*/

            else {
                a = 0;
            }
        } else {
            return 0;
        }
        /**** Modified By Tushar Kotian 12/04/2017 end ****/
        return a;
    }


    ///added by sujata 26-11-2019
    //26-11-2019 sujata
    public double setNonGuarateedSSV_4_Pr(double sumAssured,
                                          int premiumPayingTerm, int policyTerm, int year_F,
                                          String premFreqMode, double NonGuarateedDeathBenefitAt_4_Percent) {
        double a = 0, b = 0, temp = 0, p = 0, gsvFactor = 0, ssvFactor = 0, val = 0, mulofSAandb = 0;
        SmartChampDB smartchampDB = new SmartChampDB();
        if ((premFreqMode.equalsIgnoreCase("Single"))) {
            val = 1;
        } else {
            val = premiumPayingTerm;
        }

        if (year_F > policyTerm) {
            return 0;
        } else {
            if (year_F < val) {
                b = year_F / val;
            } else {
                b = 1;
            }
        }

        mulofSAandb = b * sumAssured;
        //System.out.println("mulofSAandb "+mulofSAandb);

        if (premFreqMode.equalsIgnoreCase("Single")) {
            double[] singleGSV = smartchampDB.getSingleGSVFactor();
            gsvFactor = singleGSV[year_F - 1];
            //System.out.println("single temp "+ gsvFactor);

            double[] singleSSV = smartchampDB.getSingleSSVFactor();
            int pos1 = 0;
            for (int i = 1; i <= year_F; i++) {
                for (int j = 8; j <= 21; j++) {
                    if (i == year_F && j == policyTerm) {
                        ssvFactor = singleSSV[pos1];

                    }
                    pos1++;
                }
            }

        } else {
            //double[] LimitedGSV = smartchampDB.getLimitedGSVFactor();
            double[] regularSSV = smartchampDB.getSSVFactor();
            int pos = 0;
            for (int i = 1; i <= year_F; i++) {
                for (int j = 8; j <= 21; j++) {
                    if (i == year_F && j == policyTerm) {
                        //gsvFactor = LimitedGSV[pos];
                        ssvFactor = regularSSV[pos];

                    }
                    pos++;
                }
            }
        }
        //System.out.println("ssvFactor "+ssvFactor);


        //System.out.println("setTerminalBonus4per " +setTerminalBonus4per(year_F,premiumPayingTerm,policyTerm,premFreqMode,sumAssured));
        double temp1 = (((mulofSAandb + (setTerminalBonus4per(year_F, premiumPayingTerm, policyTerm, premFreqMode, sumAssured)))) * (ssvFactor));
        // double temp2 =
        //System.out.println("temp "+temp1);

        //System.out.println("addition: "+ (mulofSAandb + setTerminalBonus4per(year_F,premiumPayingTerm,policyTerm,premFreqMode,sumAssured)));

        //return (mulofSAandb + setTerminalBonus4per(year_F,premiumPayingTerm,policyTerm,premFreqMode,sumAssured))* (ssvFactor);
        return temp1;
    }


    //26-11-2019

    public double setTerminalBonus4per(int year_F, int premiumPayingTerm, int policyTerm, String premFreqMode, double sumAssured) {
        if (year_F <= premiumPayingTerm) {
            //System.out.println("premiumPayingTerm * getbonusrate4(premFreqMode, policyTerm) * sumAssured) " + premiumPayingTerm * getbonusrate4(premFreqMode, policyTerm) * sumAssured);
            TerminalBonus4per = (year_F * getbonusrate4(premFreqMode, policyTerm) * sumAssured);
            return TerminalBonus4per;
        } else {
            //System.out.println("premiumPayingTerm * getbonusrate4(premFreqMode, policyTerm) * sumAssured) " + premiumPayingTerm * getbonusrate4(premFreqMode, policyTerm) * sumAssured);
            TerminalBonus4per = (premiumPayingTerm * getbonusrate4(premFreqMode, policyTerm) * sumAssured);
            return TerminalBonus4per;
            //System.out.println("");
        }
    }

	/*public double getTerminalBonus4per()
	{
		System.out.println("\nTerminalBonus4per  " + TerminalBonus4per);
		return TerminalBonus4per;
	}*/

    //26-11-2019
    public double setTerminalBonus8per(int year_F, int premiumPayingTerm, int policyTerm, String premFreqMode, double sumAssured) {
        if (year_F <= premiumPayingTerm) {
            //System.out.println("premiumPayingTerm * getbonusrate8(premFreqMode, policyTerm) * sumAssured) " + premiumPayingTerm * getbonusrate8(premFreqMode, policyTerm) * sumAssured);
            TerminalBonus8per = (year_F * getbonusrate8(premFreqMode, policyTerm) * sumAssured);
            return TerminalBonus8per;
        } else {
            //System.out.println("premiumPayingTerm * getbonusrate8(premFreqMode, policyTerm) * sumAssured) " + premiumPayingTerm * getbonusrate8(premFreqMode, policyTerm) * sumAssured);
            TerminalBonus8per = (premiumPayingTerm * getbonusrate8(premFreqMode, policyTerm) * sumAssured);
            return TerminalBonus8per;
            //System.out.println("");
        }
    }


    public double setNonGuarateedSSV_8_Pr(double sumAssured,
                                          int premiumPayingTerm, int policyTerm, int year_F,
                                          String premFreqMode, double NonGuarateedDeathBenefitAt_8_Percent) {
        double a = 0, b = 0, temp = 0, p = 0, gsvFactor = 0, ssvFactor = 0, val = 0, mulofSAandb = 0;
        SmartChampDB smartchampDB = new SmartChampDB();
        if ((premFreqMode.equalsIgnoreCase("Single"))) {
            val = 1;
        } else {
            val = premiumPayingTerm;
        }

        if (year_F > policyTerm) {
            return 0;
        } else {
            if (year_F < val) {
                b = year_F / val;
            } else {
                b = 1;
            }
        }

        mulofSAandb = b * sumAssured;
        //System.out.println("mulofSAandb "+mulofSAandb);

        if (premFreqMode.equalsIgnoreCase("Single")) {
            double[] singleGSV = smartchampDB.getSingleGSVFactor();
            gsvFactor = singleGSV[year_F - 1];
            //System.out.println("single temp "+ gsvFactor);

            double[] singleSSV = smartchampDB.getSingleSSVFactor();
            int pos1 = 0;
            for (int i = 1; i <= year_F; i++) {
                for (int j = 8; j <= 21; j++) {
                    if (i == year_F && j == policyTerm) {
                        ssvFactor = singleSSV[pos1];

                    }
                    pos1++;
                }
            }

        } else {
            //double[] LimitedGSV = smartchampDB.getLimitedGSVFactor();
            double[] regularSSV = smartchampDB.getSSVFactor();
            int pos = 0;
            for (int i = 1; i <= year_F; i++) {
                for (int j = 8; j <= 21; j++) {
                    if (i == year_F && j == policyTerm) {
                        //gsvFactor = LimitedGSV[pos];
                        ssvFactor = regularSSV[pos];

                    }
                    pos++;
                }
            }
        }
        //System.out.println("ssvFactor "+ssvFactor);


        //System.out.println("setTerminalBonus8per " +setTerminalBonus8per(year_F,premiumPayingTerm,policyTerm,premFreqMode,sumAssured));
        double temp2 = (mulofSAandb + setTerminalBonus8per(year_F, premiumPayingTerm, policyTerm, premFreqMode, sumAssured)) * (ssvFactor);

        // System.out.println("\ntemp2 "+temp2);

        return (mulofSAandb + setTerminalBonus8per(year_F, premiumPayingTerm, policyTerm, premFreqMode, sumAssured)) * (ssvFactor);
    }

    //26-11-2019 sujata
    //added by sujata on 06-12-2019 extra para added as TotalBasePremiumPaidWithoutFreqLoading
    public double setGuarateedSSV(double fyTotPremPaid, int premiumPayingTerm,
                                  int policyTerm, int year_F, int age, double BasePremiumPaid,
                                  String premFreqMode, String Cover, double sumAssured
            , double staff_Rebate, double sumSurvivalBenefit, double annualizedPrem, double TotalBasePremiumPaidWithoutFreqLoading, double setTotalBasePPaidFactor) {
        double surrenderValue = 0, a;
        SmartChampDB smartchampDB = new SmartChampDB();
        double[] singleGSV = smartchampDB.getSingleGSVFactor();
        double[] LimitedGSV = smartchampDB.getLimitedGSVFactor();

        try {
            if (year_F <= policyTerm) {
                if (premFreqMode.equalsIgnoreCase("Single")) {
                    double temp = singleGSV[year_F - 1];
//				System.out.println("year_F "+year_F);
//				System.out.println("single temp "+ temp);
//				System.out.println("sumSurvivalBenefit "+sumSurvivalBenefit);
//				System.out.println("base Premium " + totalBasePremiumPaid );
                    surrenderValue = ((temp * annualizedPrem) - sumSurvivalBenefit);

                    //surrenderValue= (temp* setAnnulized_Premium(year_F,premiumPayingTerm,TotalBasePremiumPaidWithoutFreqLoading)-sumSurvivalBenefit;
                    //surrenderValue= temp* premium_Without_ST_WithoutLoadingFreq;
                    //surrenderValue= temp*  setAnnulized_Premium(year_F,premiumPayingTerm,TotalBasePremiumPaidWithoutFreqLoading);

                } else {
                    int pos = 0;
                    double limitedgsv = 0;
                    for (int i = 1; i <= year_F; i++) {
                        for (int j = 8; j <= 21; j++) {
                            if (i == year_F && j == policyTerm) {

                                limitedgsv = LimitedGSV[pos];

                            }
                            pos++;
                        }
                    }

//				System.out.println("\nyear_F "+year_F);
//				System.out.println("premiumPayingTerm "+premiumPayingTerm);


                    if (premiumPayingTerm < year_F) {
                        //System.out.println("Inside if  ");
//				//	a = setTotalBasePPaid(BasePremiumPaid,  year_F,
//							premiumPayingTerm,  premFreqMode , age, policyTerm, "Basic", sumAssured,  staff_Rebate )
//							* premiumPayingTerm;
                        a = annualizedPrem * premiumPayingTerm;
                        //a= setTotalBasePPaidFactor * premiumPayingTerm;
                    } else {
//					a = setTotalBasePPaid(BasePremiumPaid,  year_F,
//							premiumPayingTerm,  premFreqMode , age, policyTerm, "Basic", sumAssured,  staff_Rebate )
//								* year_F;
                        a = annualizedPrem * year_F;
                        //a =setTotalBasePPaidFactor * year_F;
                    }

//				System.out.println("annualizedPrem "+annualizedPrem);
//				System.out.println("limitedgsv "+limitedgsv);
//				System.out.println("a " + a );
//				System.out.println("limitedgsv "+limitedgsv);
//				System.out.println("sumSurvivalBenefit "+sumSurvivalBenefit);
                    surrenderValue = limitedgsv * a - sumSurvivalBenefit;


                }
            } else {
                return 0;
            }

            //System.out.println("\nsurrenderValue "+ surrenderValue);

            if (surrenderValue < 0) {
                return 0;
            }

        } catch (Exception ex) {
            String msg = ex.getMessage();
        }

        return surrenderValue;
    }

    //Added by sujata on 10-12-2019
    public double setTotalSurvivalBenefit4per(double sumAssured, int premiumPayingTerm, int policyTerm, int year_f
            , String premFreqMode, double NonGuarateedDeathBenefitAt_4_Percent, double sumsurvivalbenyerr20) {
        //System.out.println("\nsetGuaranteedSurvivalBenefit "+setGuaranteedSurvivalBenefit(sumAssured,premiumPayingTerm, policyTerm, year_f));
        //System.out.println("setNonGuarateedSurvivalBenefitAt_4_Percent "+setNonGuarateedSurvivalBenefitAt_4_Percent(
//						premFreqMode, NonGuarateedDeathBenefitAt_4_Percent,
//						 premiumPayingTerm,  policyTerm, year_f, sumAssured));
//
        return setGuaranteedSurvivalBenefit(sumAssured, premiumPayingTerm, policyTerm, year_f)
                + setNonGuarateedSurvivalBenefitAt_4_Percent(
                premFreqMode, NonGuarateedDeathBenefitAt_4_Percent,
                premiumPayingTerm, policyTerm, year_f, sumAssured);
    }

    public double setTotalSurvivalBenefit8per(double sumAssured, int premiumPayingTerm, int policyTerm, int year_f
            , String premFreqMode, double NonGuarateedDeathBenefitAt_8_Percent) {
        return setGuaranteedSurvivalBenefit(sumAssured, premiumPayingTerm, policyTerm, year_f)
                + setNonGuarateedSurvivalBenefitAt_8_Percent(
                premFreqMode, NonGuarateedDeathBenefitAt_8_Percent,
                premiumPayingTerm, policyTerm, year_f, sumAssured);
    }


    //////end///////


/*************************** Added by tushar kotian on 3/8/2017 *******************************/

//	}
//
}

