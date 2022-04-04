package sbilife.com.pointofsale_bancaagency.smartbachat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import sbilife.com.pointofsale_bancaagency.common.CommonForAllProd;


public class SmartBachatBusinessLogic {
    SmartBachatDB smartbachatdb = new SmartBachatDB();
    SmartBachatProperties smartbachatproperties = new SmartBachatProperties();
    int year, StaffRebate_ADTPD = 0;
    double baserate, staffRebate, SARebate, LoadingFrequencyPremium, PremiumBeforeST, premium_Without_ST_WithoutLoadingFreq, TotalPremiumPaid_withoutST, PremiumMultiplicationFactor,
            TotalBasePremium, BenifitPayableOnDeath_Gauranteed, TotalPremiumpaid_fordeathBenifit, BenifitPayableOnDeath_NonGauranteed_4, TotalPremiumpaid_fordeathBenifit6,
            TotalPremiumpaid_fordeathBenifit8, BenifitPayableOnDeath_NonGauranteed_8, SurvivalBenifit_Guaranteed, SurvivalBenifit_Guaranteed8, SurrenederValue_Guaranteed,
            SurvivalBenifit_Guaranteed4, SurrenederValue_Guaranteed_4, SurrenederValue_Guaranteed_8, TerminalBonus_4, TerminalBonus_8,
            Premium_with_ST, Premium_with_ServiceTax, Premium_with_ServiceTax_FirstYear, ST_firstYear, Premium_with_ST_secondyear, ST_SecondYear, BasicServiceTax_firstYear, SwachBharat_1styear, KeralaServiceTax_1styear,
            KrishiKalyan_1stYear, TotalTaxes1st_year, BasicServiceTax_secondYear, SwachBharat_2ndyear, KrishiKalyan_2ndYear, TotalTaxes2nd_year, KeralaServiceTax_2ndyear,
            FinalPremium_WithTaxes_1stYear, FinalPremium_WithTaxes_2ndYear, baserateforadtpd, StaffRebatefor_ADTPD, LoadingFrequencyPremiumfor_ADTPD,
            PremiumBeforeST_ADTPD, TotalPremium, ModalPremium, BenifitPayableOn_AccidentalDeath, premiumBeforeSTWithoutDisc_ADTPD, premiumBeforeSTWithoutDiscWithoutLoading_ADTPD,
            yearlyPremiumBeforeModalLoading, yearlyPremiumBeforeModalLoading_ADTPD, AnnualPremiumWithoutModal, baserateatpdbMinusbaserate, SurrenederValue_GuaranteedValue,
            ModalPremium1, ModalPremium2, adtpd, atpdb1, atpdb2;
    CommonForAllProd commonforall = new CommonForAllProd();
    SmartBachatBean smartbachatBean = new SmartBachatBean();


    public void setYear(int rowNumber) {
        this.year = rowNumber;

        ////System.out.print("year"+year);
    }

    public int getYear() {
        return year;
    }


    public void setBaseRate(String plantype, int policyterm, int ppt, int age) {
        if (plantype.equals("Option A (Endowment Option)")) {

            if ((policyterm >= 10 && policyterm < 15 && ppt != 6 && ppt != 7) || (policyterm >= 15 && policyterm < 20 && ppt == 15)) {

                baserate = 0;
            } else {
                //added by sujata on 21-11-2019///
                if (ppt == 6) {

                    double[] PWBarr = smartbachatdb.getBaseRates_6years();
                    int position = 0;
                    int premiumPerOneLac = 0;
                    for (int i = 6; i <= 50; i++) {
                        for (int j = 12; j <= 25; j++) {
                            if ((age) == i && (policyterm) == j) {
                                ////////System.out.println("getValFromPWBtable() -> "+ PWBarr[position]);
                                baserate = PWBarr[position];
                                // System.out.println("baserate1 "+ baserate);
                            }
                            position++;
                        }
                    }

                } else if (ppt == 7) {


                    double[] PWBarr = smartbachatdb.getBaseRates_7years();
                    int position = 0;
                    int premiumPerOneLac = 0;
                    for (int i = 6; i <= 50; i++) {
                        for (int j = 12; j <= 25; j++) {
                            if ((age) == i && (policyterm) == j) {
                                ////////System.out.println("getValFromPWBtable() -> "+ PWBarr[position]);
                                baserate = PWBarr[position];
                                // System.out.println("baserate "+baserate);
                            }
                            position++;
                        }
                    }


                } else if (ppt == 10) {


                    double[] PWBarr = smartbachatdb.getBaseRates_10years();
                    int position = 0;
                    int premiumPerOneLac = 0;
                    for (int i = 6; i <= 50; i++) {
                        for (int j = 15; j <= 25; j++) {
                            if ((age) == i && (policyterm) == j) {
                                ////////System.out.println("getValFromPWBtable() -> "+ PWBarr[position]);
                                baserate = PWBarr[position];
                            }
                            position++;
                        }
                    }


                } else {


                    double[] PWBarr = smartbachatdb.getBaseRates_15years();
                    int position = 0;
                    int premiumPerOneLac = 0;
                    for (int i = 6; i <= 50; i++) {
                        for (int j = 20; j <= 25; j++) {
                            if ((age) == i && (policyterm) == j) {
                                ////////System.out.println("getValFromPWBtable() -> "+ PWBarr[position]);
                                baserate = PWBarr[position];
                            }
                            position++;
                        }
                    }


                }
            }

        } else {


            if ((policyterm >= 10 && policyterm < 15 && ppt != 6 && ppt != 7) || (policyterm >= 15 && policyterm < 20 && ppt == 15)) {

                baserate = 0;
            }

            ///end//////


            ////added by sujata on 21-11-2019///
            else {
                if (ppt == 6) {

                    double[] PWBarr = smartbachatdb.getBaseRates_6years();

                    //Changed By Manish
//					double [] PWBarr=smartbachatdb.getBaseRates_6years_OptionB();
                    int position = 0;
                    int premiumPerOneLac = 0;
                    for (int i = 6; i <= 50; i++) {
                        for (int j = 12; j <= 25; j++) {
                            if ((age) == i && (policyterm) == j) {
                                ////////System.out.println("getValFromPWBtable() -> "+ PWBarr[position]);
                                baserate = PWBarr[position];
                            }
                            position++;
                        }
                    }

                } else if (ppt == 7) {


                    double[] PWBarr = smartbachatdb.getBaseRates_7years();
                    //Changed By Manish
//					double [] PWBarr=smartbachatdb.getBaseRates_7years_OptionB();
                    int position = 0;
                    int premiumPerOneLac = 0;
                    for (int i = 6; i <= 50; i++) {
                        for (int j = 12; j <= 25; j++) {
                            if ((age) == i && (policyterm) == j) {
                                ////////System.out.println("getValFromPWBtable() -> "+ PWBarr[position]);
                                baserate = PWBarr[position];
                            }
                            position++;
                        }
                    }


                } else if (ppt == 10) {


                    double[] PWBarr = smartbachatdb.getBaseRates_10years();
                    //Changed By Manish
//					double [] PWBarr=smartbachatdb.getBaseRates_10years_OptionB();
                    int position = 0;
                    int premiumPerOneLac = 0;
                    for (int i = 6; i <= 50; i++) {
                        for (int j = 15; j <= 25; j++) {
                            if ((age) == i && (policyterm) == j) {
                                ////////System.out.println("getValFromPWBtable() -> "+ PWBarr[position]);
                                baserate = PWBarr[position];
                            }
                            position++;
                        }
                    }


                } else {


                    double[] PWBarr = smartbachatdb.getBaseRates_15years();
                    //Changed By Manish
//					double [] PWBarr=smartbachatdb.getBaseRates_10years_OptionB();
                    int position = 0;
                    int premiumPerOneLac = 0;
                    for (int i = 6; i <= 50; i++) {
                        for (int j = 20; j <= 25; j++) {
                            if ((age) == i && (policyterm) == j) {
                                ////////System.out.println("getValFromPWBtable() -> "+ PWBarr[position]);
                                baserate = PWBarr[position];
                            }
                            position++;
                        }
                    }


                }
            }
        }
    }

    public double getBaseRate() {
        return baserate;
    }


    public void setStaffRebate(boolean isstaff) {
        if (isstaff) {
            staffRebate = 0.06;
        } else {
            staffRebate = 0.00;
        }
    }

    public double getStaffRebate() {
        return staffRebate;
    }

    ////added by sujata on 21-11-2019///
    public void setSARebate(double sumassured) {
        if (sumassured >= 100000 && sumassured < 299000) {
            SARebate = 0;
        } else {
            if (sumassured >= 300000 && sumassured < 499000) {
                SARebate = 3;
            } else {
                //SARebate= 4.5 ;
                SARebate = 4;
            }
        }
//		if(sumassured>=500000)
//		{
//			SARebate = 5.5;
//		}
    }


    //end//
    public double getSARebate() {
        return SARebate;
    }


    public void setLoadingFrequencyPremium(String premfreq) {
        if (premfreq.equals("Yearly") || premfreq.equals("Single")) {
            LoadingFrequencyPremium = 1;
        } else {
            if (premfreq.equals("Half-Yearly")) {
                LoadingFrequencyPremium = 0.51;
            } else {
                if (premfreq.equals("Quarterly")) {
                    LoadingFrequencyPremium = 0.26;
                } else {
                    LoadingFrequencyPremium = 0.085;
                }
            }
        }
    }

    public double getLoadingFrequencyPremium() {
        return LoadingFrequencyPremium;
    }


    public void setPremiumBeforeST(double sumassured) {

        int divFactor = 0;
		  /* System.out.println("rate " + getBaseRate() );
	       System.out.println("staff rebate " + getStaffRebate());
       System.out.println("SA rebate " + getSARebate());

	       System.out.println("SA Basic " +  sumassured);
	       System.out.println("loading " + getLoadingFrequencyPremium());
       System.out.println("divfact " + divFactor);*/


        double a = Double.parseDouble(commonforall.getRoundOffLevel2New(commonforall.getStringWithout_E((((getBaseRate() - getSARebate()) * sumassured) / 1000) * (1 - getStaffRebate()) * getLoadingFrequencyPremium() / 1)));


        //System.out.println(" a : "+a);

        PremiumBeforeST = a;
    }

    public double getPremiumBeforeST() {
        return PremiumBeforeST;
    }


    //11-12-2019 added by sujata

    public double get_Premium_Without_ST_WithoutLoadingFreq(double sumassured) {
        //System.out.println("\npremium_Without_ST_WithoutLoadingFreq "+premium_Without_ST_WithoutLoadingFreq);
        int divFactor = 0;
//		 	System.out.println("\nrate "+getBaseRate());
//		 	System.out.println("getSARebate() "+getSARebate());
//		 	System.out.println("sumassured "+sumassured);
//		 	System.out.println("getStaffRebate "+getStaffRebate());
//		 	System.out.println("\n");
        premium_Without_ST_WithoutLoadingFreq = Double.parseDouble(commonforall.getRoundOffLevel2New(commonforall.getStringWithout_E((((getBaseRate() - getSARebate()) * sumassured) / 1000) * (1 - getStaffRebate()))));
//
//	    	System.out.println("premium_Without_ST_WithoutLoadingFreq "+premium_Without_ST_WithoutLoadingFreq);
        return premium_Without_ST_WithoutLoadingFreq;
    }

    //added by sujata on 21-11-2019
    public void setBaseRateFor_ADTPD(String Plantype, int policyterm, int ppt, int age) {
        if (Plantype.equals("Option B (Endowment Option with in-built Accidental Death and Total Permanent Disability (AD&TPD) Benefit)")) {

            if (ppt == 6) {

                double[] PWBarr = smartbachatdb.getADTPD_PPT_6();
                int position = 0;
                int premiumPerOneLac = 0;
                for (int i = 18; i <= 50; i++) {
                    for (int j = 12; j <= 25; j++) {
                        if ((age) == i && (policyterm) == j) {
                            ////////System.out.println("getValFromPWBtable() -> "+ PWBarr[position]);
                            baserateforadtpd = PWBarr[position];
                        }
                        position++;
                    }
                }

            } else if (ppt == 7) {


                double[] PWBarr = smartbachatdb.getADTPD_PPT_7();
                int position = 0;
                int premiumPerOneLac = 0;
                for (int i = 18; i <= 50; i++) {
                    for (int j = 12; j <= 25; j++) {
                        if ((age) == i && (policyterm) == j) {
                            ////////System.out.println("getValFromPWBtable() -> "+ PWBarr[position]);
                            baserateforadtpd = PWBarr[position];
                        }
                        position++;
                    }
                }


            } else if (ppt == 10) {


                double[] PWBarr = smartbachatdb.getADTPD_PPT_10();
                int position = 0;
                int premiumPerOneLac = 0;
                for (int i = 18; i <= 50; i++) {
                    for (int j = 15; j <= 25; j++) {
                        if ((age) == i && (policyterm) == j) {
                            ////////System.out.println("getValFromPWBtable() -> "+ PWBarr[position]);
                            baserateforadtpd = PWBarr[position];
                        }
                        position++;
                    }
                }


            } else {


                double[] PWBarr = smartbachatdb.getADTPD_PPT_15();
                int position = 0;
                int premiumPerOneLac = 0;
                for (int i = 18; i <= 50; i++) {
                    for (int j = 20; j <= 25; j++) {
                        if ((age) == i && (policyterm) == j) {
                            ////////System.out.println("getValFromPWBtable() -> "+ PWBarr[position]);
                            baserateforadtpd = PWBarr[position];
                        }
                        position++;
                    }
                }


            }

            //if(smartbachatBean.getPlantype().equals("Option B (Endowment Option with in-built Accidental Death and Total Permanent Disability (AD&TPD) Benefit)"))
        }
        if (Plantype.equals("Option B (Endowment Option with in-built Accidental Death and Total Permanent Disability (AD&TPD) Benefit)")) {
//				System.out.println("baserate "+baserate);
//				System.out.println("baserateforadtpd "+baserateforadtpd);
            baserateatpdbMinusbaserate = (baserateforadtpd - baserate);
//			baserateatpdbMinusbaserate = baserateforadtpd;

            //return baserateatpdbMinusbaserate;
        } else {
            baserateatpdbMinusbaserate = 0;
            //return baserateatpdbMinusbaserate;
        }

    }


    //added by sujata on 21-11-2019
    public double getBaseRateFor_ADTPD() {
        return baserateatpdbMinusbaserate;
        //}
    }
    //public double


    //end///


    public void setStaffRebatefor_ADTPD(boolean isstaff) {
        if (isstaff) {
            StaffRebatefor_ADTPD = 0.06;
        } else {
            StaffRebatefor_ADTPD = 0.00;
        }
    }

    public double getStaffRebatefor_ADTPD() {
        ;

        //System.out.println("StaffRebatefor_ADTPD "+ StaffRebatefor_ADTPD);
        return StaffRebatefor_ADTPD;
    }


    public void setLoadingFrequencyPremiumfor_ADTPD(String premfreq) {
        if (premfreq.equals("Yearly") || premfreq.equals("Single")) {
            LoadingFrequencyPremiumfor_ADTPD = 1;
        } else {
            if (premfreq.equals("Half-Yearly")) {
                LoadingFrequencyPremiumfor_ADTPD = 0.51;
            } else {
                if (premfreq.equals("Quarterly")) {
                    LoadingFrequencyPremiumfor_ADTPD = 0.26;
                } else {
                    LoadingFrequencyPremiumfor_ADTPD = 0.085;
                }
            }
        }
    }

    public double getLoadingFrequencyPremiumfor_ADTPD() {
        //System.out.println("LoadingFrequencyPremiumfor_ADTPD "+ LoadingFrequencyPremiumfor_ADTPD);
        return LoadingFrequencyPremiumfor_ADTPD;
    }


    public void setPremiumBeforeST_ADTPD(double sumassured) {
		/*System.out.println("BaseRate "+getBaseRateFor_ADTPD());
		System.out.println("\ngetStaffRebatefor_ADTPD() "+getStaffRebatefor_ADTPD());
		System.out.println("\n StaffRebatefor_ADTPD "+ getBaseRateFor_ADTPD());
		System.out.println("\n sumassured "+sumassured);
		System.out.println("\n getLoadingFrequencyPremiumfor_ADTPD() "+getLoadingFrequencyPremiumfor_ADTPD());*/

        double a = Double.parseDouble(commonforall.getRoundOffLevel2(commonforall.getStringWithout_E(((getBaseRateFor_ADTPD() * (1 - getStaffRebatefor_ADTPD())) * sumassured / 1000 * getLoadingFrequencyPremiumfor_ADTPD() / 1))));
        //double a = ((getBaseRateFor_ADTPD() - StaffRebate_ADTPD) * (1 - getStaffRebatefor_ADTPD()) + (smartbachatproperties.NSAP + smartbachatproperties.EMR)) * (sumassured/1000) * (getLoadingFrequencyPremiumfor_ADTPD()/1);
        //	double a =(getBaseRate()* (1-getStaffRebate()) - getSARebate() +(smartbachatproperties.NSAP + smartbachatproperties.EMR))* (sumassured/1000) *(getLoadingFrequencyPremium()/1) ;
        //String b = commonforall.getRoundUp(a);
//		System.out.println("ATPDB "+a);

        PremiumBeforeST_ADTPD = a;
    }

    public double getPremiumBeforeST_ADTPD() {
        //System.out.println("\n PremiumBeforeST_ADTPD "+PremiumBeforeST_ADTPD);
        return PremiumBeforeST_ADTPD;
    }


//	//added by sujata on 10-12-2019
//	public void setPremium_Firstyear_ST_ADTPD(double sumassured)
//	{
//
//	}
//

    public void setTotalPremium(String premfreq) {
        if (premfreq.equals("Yearly")) {
            TotalPremium = getModalPremium();
        } else if (premfreq.equals("Half-Yearly")) {
            TotalPremium = getModalPremium() * 2;
        } else if (premfreq.equals("Monthly")) {
            TotalPremium = getModalPremium() * 12;
        } else {
            TotalPremium = getModalPremium() * 4;
        }


    }

    public double getTotalPremium() {
        return TotalPremium;
    }


    public void setPremiumMultiplicationFactor(String premfreq) {

        if (premfreq.equals("Yearly") || premfreq.equals("Single")) {
            PremiumMultiplicationFactor = 1;
        } else {
            if (premfreq.equals("Half-Yearly")) {
                PremiumMultiplicationFactor = 2;
            } else {
                if (premfreq.equals("Quarterly")) {
                    PremiumMultiplicationFactor = 4;
                } else {
                    PremiumMultiplicationFactor = 12;
                }
            }
        }


    }

    public double getPremiumMultiplicationFactor() {
        return PremiumMultiplicationFactor;
    }


    public void setModalPremium(String plantype) {
        double a;
        if (plantype.equals("Option A (Endowment Option)")) {
            a = getPremiumBeforeST();
        } else {
            a = getPremiumBeforeST() + getPremiumBeforeST_ADTPD();
        }


        String b = commonforall.getRoundUp(commonforall.getStringWithout_E(a));
        ModalPremium = Double.parseDouble(b);
    }

    public double getModalPremium() {
        //System.out.println("ModalPremium "+ModalPremium);
        return ModalPremium;
    }

	/*public void setModalPremium1(String plantype)
	{	double a;
		if(plantype.equals("Option A (Endowment Option)"))
		{
			a = getPremium_with_ST() ;
		}
		else
		{
			a = getPremium_with_ST() + getST_firstYear();
		}

		System.out.println("getPremium_with_ST()"+getPremium_with_ST());
		System.out.println("getST_firstYear()"+getST_firstYear());
		String b = commonforall.getRoundUp(commonforall.getStringWithout_E(a));
		ModalPremium1 = Double.parseDouble(b);
	}

	public double getModalPremium1()
	{
		//System.out.println("ModalPremium "+ModalPremium);
		return ModalPremium1 ;
	}

	public void setModalPremium2(String plantype)
	{	double a;
		if(plantype.equals("Option A (Endowment Option)"))
		{
			a = getPremium_with_ST_secondyear() ;
		}
		else
		{
			a = getPremium_with_ST_secondyear() + getST_SecondYear();
		}


		String b = commonforall.getRoundUp(commonforall.getStringWithout_E(a));
		ModalPremium2 = Double.parseDouble(b);
	}

	public double getModalPremium2()
	{
		//System.out.println("ModalPremium "+ModalPremium);
		return ModalPremium2 ;
	}*/

    //added by sujata on 11-12-2019
    public void setTotalBasePremium(int year_F, int ppt) {
        TotalBasePremium = getModalPremium() * getPremiumMultiplicationFactor();
    }

    public double getTotalBasePremium() {

//		System.out.println("getModalPremium "+getModalPremium());
//		System.out.println("getPremiumMultiplicationFactor() "+getPremiumMultiplicationFactor());
        //	System.out.println("TotalBasePremium "+TotalBasePremium);
        return TotalBasePremium;
    }

    //11-12-2019 added by sujata
    public double getBasePremiumuptoPpt(double premBasic,
                                        int PolicyTerm, String PremiumFreq, int ppt) {

        if (getYear() <= ppt) {
            //System.out.println("year "+getYear());
            return getTotalBasePremium();
            //return get_Premium_Without_ST_WithoutLoadingFreq();
        } else
            return 0;
    }


    public void setTotalPremiumPaid_withoutST(int ppt, int policyterm) {

        double temp = 0;
        if (getYear() <= ppt) {
            temp = getTotalBasePremium() * getYear();
        } else {
            if (getYear() <= policyterm) {
                temp = getTotalBasePremium() * ppt;
            } else {
                temp = 0.00;
            }
        }

        String temp2 = commonforall.getRoundUp(commonforall.getStringWithout_E(temp));
        TotalPremiumPaid_withoutST = Double.parseDouble(temp2);


    }

    public double getTotalPremiumPaid_withoutST() {
        //System.out.println("TotalPremiumPaid_withoutST: "+TotalPremiumPaid_withoutST);
        return TotalPremiumPaid_withoutST;
    }


    public double getTotalPremiumpaid_fordeathBenifit() {
        if (getYear() == 1) {
            TotalPremiumpaid_fordeathBenifit = getTotalPremiumPaid_withoutST();
        }


        return TotalPremiumpaid_fordeathBenifit;
    }


    /**
     * Modified by vrushali on 27 Dec 2016
     *
     * @param sumassured
     */

	/*public void setBenifitPayableOnDeath_Gauranteed(double sumassured)
	{
		double a = getAnnualPremiumWithoutModal() *10 ;
		double b = getTotalPremiumPaid_withoutST() * 1.05;
		double c =Math.max(a, sumassured);


		String temp2 = commonforall.getRound(commonforall.getStringWithout_E(Math.max(b, c)));
		BenifitPayableOnDeath_Gauranteed =Double.parseDouble(temp2) ;


	}*/

    //11-12-2019 added by sujata
    public void setBenifitPayableOnDeath_Gauranteed(double sumassured, double FirstYearOfAnnulized, int policyterm, int year) {
        if (year <= policyterm) {
            //double a = getAnnualPremiumWithoutModal() *11;
            //System.out.println("getAnnualPremiumWithoutModal() "+getAnnualPremiumWithoutModal());
            //double b = getTotalPremiumPaid_withoutST() * 1.05;
            double a = FirstYearOfAnnulized * 10;
            double c = Math.max(a, sumassured);

            //String temp2 = commonforall.getRound(commonforall.getStringWithout_E(Math.max(b, c)));
            String temp2 = commonforall.getRound(commonforall.getStringWithout_E(c));
            BenifitPayableOnDeath_Gauranteed = Double.parseDouble(temp2);
            //System.out.println("BenifitPayableOnDeath_Gauranteed "+ BenifitPayableOnDeath_Gauranteed);

        } else {
            BenifitPayableOnDeath_Gauranteed = 0;
        }
    }

    public double getBenifitPayableOnDeath_Gauranteed() {
        return BenifitPayableOnDeath_Gauranteed;
    }


    //11-12-2019 added by sujata
    public void setBenifitPayableOnDeath_NonGauranteed_4(int ppt, int policyterm, double sumassured) {
        double temp1;
        String temp2;
        double a = 0;
        double b = 0;

        if (getYear() <= policyterm) {
            a = getYear() * sumassured * getBonusRate_4(ppt, policyterm);
        } else {
            a = 0;
        }
		/*if(getYear()==policyterm)
		{
			b = 1.15 ;
		}
		else
		{
			b = 1 ;
		}
		*/
        temp1 = a;
        temp2 = commonforall.getRound(commonforall.getRoundOffLevel2New(commonforall.getStringWithout_E(temp1)));


        BenifitPayableOnDeath_NonGauranteed_4 = Double.parseDouble(temp2);

    }

    public double getBenifitPayableOnDeath_NonGauranteed_4() {
        return BenifitPayableOnDeath_NonGauranteed_4;
    }

    //11-12-2019 added by sujata
    public void setBenifitPayableOnDeath_NonGauranteed_8(int ppt, int policyterm, double sumassured) {
        double temp1;
        String temp2;

        double a = 0;
        double b = 0;

        if (getYear() <= policyterm) {
            a = getYear() * sumassured * getBonusRate_8(policyterm, ppt);
        } else {
            a = 0;
        }
		/*if(getYear()==policyterm)
		{
			b = 1.15 ;
		}
		else
		{
			b = 1 ;
		}
		*/

        temp1 = a;
        temp2 = commonforall.getRound(commonforall.getRoundOffLevel2New(commonforall.getStringWithout_E(temp1)));
        BenifitPayableOnDeath_NonGauranteed_8 = Double.parseDouble(temp2);


    }

    public double getBenifitPayableOnDeath_NonGauranteed_8() {
        return BenifitPayableOnDeath_NonGauranteed_8;
    }


    public void setBenifitPayableOn_AccidentalDeath(double adtpdamt, int policyterm) {
        if (getYear() <= policyterm) {

            String temp2 = commonforall.getRound(commonforall.getStringWithout_E(adtpdamt));
            BenifitPayableOn_AccidentalDeath = Double.parseDouble(temp2);
        } else {
            BenifitPayableOn_AccidentalDeath = 0;
        }
    }


    public double getBenifitPayableOn_AccidentalDeath() {
        return BenifitPayableOn_AccidentalDeath;
    }

    public void setSurvivalBenifit_Guaranteed(int policyterm, double sumassured) {
        if (getYear() == policyterm) {
            String temp2 = commonforall.getRound(commonforall.getStringWithout_E(sumassured));
            SurvivalBenifit_Guaranteed = Double.parseDouble(temp2);
        } else {
            SurvivalBenifit_Guaranteed = 0;
        }

    }

    public double getSurvivalBenifit_Guaranteed() {
        return SurvivalBenifit_Guaranteed;
    }


    public void setSurvivalBenifit_Guaranteed4(int policyterm, double sumassured) {
        if (getYear() == policyterm) {
            String temp2 = commonforall.getRound(commonforall.getStringWithout_E(getBenifitPayableOnDeath_NonGauranteed_4()));
            SurvivalBenifit_Guaranteed4 = Double.parseDouble(temp2);
        } else {
            SurvivalBenifit_Guaranteed4 = 0;
        }


    }

    public double getSurvivalBenifit_Guaranteed4() {
        return SurvivalBenifit_Guaranteed4;
    }


    public void setSurvivalBenifit_Guaranteed8(int policyterm, double sumassured) {
        if (getYear() == policyterm) {
            String temp2 = commonforall.getRound(commonforall.getStringWithout_E(getBenifitPayableOnDeath_NonGauranteed_8()));
            SurvivalBenifit_Guaranteed8 = Double.parseDouble(temp2);
        } else {
            SurvivalBenifit_Guaranteed8 = 0;
        }

    }

    public double getSurvivalBenifit_Guaranteed8() {
        return SurvivalBenifit_Guaranteed8;
    }


    public void setSurrenederValue_Guaranteed(int policyterm, int ppt, int age, int Year_F) {
        double gsvfactor = 0, value2 = 0;
        if (getYear() > policyterm) {
            SurrenederValue_Guaranteed = 0;
        } else {
            // double [] GSV_Value=smartbachatdb.getGSVFactor_5_7years();
            double[] GSV_Value = smartbachatdb.getGSVFactor_GSVSur();
            gsvfactor = GSV_Value[getYear() - 1];

            int position = 0;
            //int premiumPerOneLac=0;
            for (int i = 1; i <= 25; i++) {
                for (int j = 12; j <= 25; j++) {
                    if ((Year_F) == i && (policyterm) == j) {
                        //System.out.println("getValFromPWBtable() -> "+ PWBarr[position]);
                        SurrenederValue_Guaranteed = GSV_Value[position];

                        //System.out.println("GSV val "+SurrenederValue_Guaranteed);
                    }
                    position++;
                }
            }

//			        System.out.println("\n year "+getYear());
//			        System.out.println("base "+getTotalPremiumPaid_withoutST());
            SurrenederValue_GuaranteedValue = (SurrenederValue_Guaranteed * getTotalPremiumPaid_withoutST());
            // System.out.println("SurrenederValue_GuaranteedValue  "+SurrenederValue_GuaranteedValue);
        }
    }


    public double getSurrenederValue_Guaranteed() {
        return SurrenederValue_GuaranteedValue;
    }


    public void setTerminalBonus_4(int ppt, int policyterm, double sumassured) {
        TerminalBonus_4 = policyterm * sumassured * getBonusRate_4(ppt, policyterm) * smartbachatproperties.terminalbonus;

    }

    public double getTerminalBonus_4() {
        return TerminalBonus_4;
    }


    public void setTerminalBonus_8(int ppt, int policyterm, double sumassured) {
        TerminalBonus_8 = policyterm * sumassured * getBonusRate_8(policyterm, ppt) * smartbachatproperties.terminalbonus;

    }

    public double getTerminalBonus_8() {
        return TerminalBonus_8;
    }

    //added by sujta on 22-11-2019
    public double getBonusRate_4(int ppt, int policyterm) {
        double BonusRate_4 = 0;
        if (ppt == 6) {
//			if(policyterm>=12 && policyterm<=15)
            if (policyterm >= 12 && policyterm <= 14) {
//				BonusRate_4 = 0.008 ;
                BonusRate_4 = 0.01;
            }
//			else if (policyterm>=16 && policyterm<=20)
            else if (policyterm >= 15 && policyterm <= 19) {
//				BonusRate_4 = 0.0065 ;
                BonusRate_4 = 0.008;
            }
//			else if(policyterm>=20 )
            else if (policyterm >= 20) {
//				BonusRate_4 = 0.007 ;
                BonusRate_4 = 0.007;
            }

        } else if (ppt == 7) {
//			if(policyterm>=12 && policyterm<=15)
            if (policyterm >= 12 && policyterm <= 14) {
//				BonusRate_4 = 0.0085 ;
                BonusRate_4 = 0.012;
            }
//			else if (policyterm>=16 && policyterm<=20)
            else if (policyterm >= 15 && policyterm <= 19) {
//				BonusRate_4 = 0.0065 ;
                BonusRate_4 = 0.01;
            }
//			else if(policyterm>=20 )
            else if (policyterm >= 20 && policyterm <= 25) {
//				BonusRate_4 = 0.005 ;
                BonusRate_4 = 0.008;
            }

        } else if (ppt == 10) {
			/*if (policyterm>=10 && policyterm<=15)
			{
				BonusRate_4 = 0.009 ;
			}*/
//			else if(policyterm>=16 && policyterm<=20)
            if (policyterm >= 15 && policyterm <= 19) {
//				BonusRate_4 = 0.0075 ;
                BonusRate_4 = 0.012;
            }
//			else if(policyterm>=20)
            else if (policyterm >= 20 && policyterm <= 25) {
//				BonusRate_4 =0.006;
                BonusRate_4 = 0.01;
            }

        } else {
			/*if (policyterm>=10 && policyterm<=15)
			{
				BonusRate_4 = 0;
			}
			else if(policyterm>=16 && policyterm<=20)
			{
				BonusRate_4 = 0.009 ;
			}*/
//			else if(policyterm>=20)
            if (policyterm >= 20 && policyterm <= 25) {
//				BonusRate_4 = 0.0075;
                BonusRate_4 = 0.012;
            }
        }
        return BonusRate_4;

    }


    public double getBonusRate_8(int policyterm, int ppt) {
        double BonusRate_8 = 0;
        if (ppt == 6) {
//			if(policyterm>=12 && policyterm<=15)
            if (policyterm >= 12 && policyterm <= 14) {
//				BonusRate_8 = 0.036 ;
                BonusRate_8 = 0.034;
            }
//			else if (policyterm>=16 && policyterm<=20)
            else if (policyterm >= 15 && policyterm <= 19) {
//				BonusRate_8 = 0.039 ;
                BonusRate_8 = 0.038;
            }
//			else if(policyterm>=20 )
            else if (policyterm >= 20) {
//				BonusRate_8 = 0.043  ;
                BonusRate_8 = 0.041;
            }

        } else if (ppt == 7) {
//			if(policyterm>=12 && policyterm<=15)
            if (policyterm >= 12 && policyterm <= 14) {
                BonusRate_8 = 0.035;
            }
//			else if (policyterm>=16 && policyterm<=20)
            else if (policyterm >= 15 && policyterm <= 19) {
//				BonusRate_8 = 0.038 ;
                BonusRate_8 = 0.039;
            }
//			else if(policyterm>=20 )
            else if (policyterm >= 20 && policyterm <= 25) {
                BonusRate_8 = 0.042;
            }

        } else if (ppt == 10) {
			/*if(policyterm>=12 && policyterm<=15)
			{
				BonusRate_8 = 0.035 ;
			}*/
//			else if (policyterm>=16 && policyterm<=20)
            if (policyterm >= 15 && policyterm <= 19) {
//				BonusRate_8 = 0.036 ;
                BonusRate_8 = 0.038;
            }
//			else if(policyterm>=20 )
            else if (policyterm >= 20 && policyterm <= 25) {
//				BonusRate_8 = 0.04 ;
                BonusRate_8 = 0.042;
            }

        } else {
			/*if(policyterm>=12 && policyterm<=15)
			{
				BonusRate_8 = 0;
			}
			else if (policyterm>=16 && policyterm<=20)
			{
				BonusRate_8 = 0.036 ;
			}
			else */
            if (policyterm >= 20 && policyterm <= 25) {
//				BonusRate_8 = 0.0375 ;
                BonusRate_8 = 0.04;
            }
        }
        return BonusRate_8;

    }


    //added by sujata on 22-11-2019
    public void setSurrenederValue_Guaranteed_4(int policyterm, int ppt, double sumassured) {
        if (getYear() > policyterm) {
            SurrenederValue_Guaranteed_4 = 0;
        } else {
            double a = Math.min(getYear(), ppt);

            double val1 = a / ppt;
//			System.out.println("Year "+getYear());
//			System.out.println("\nval1 "+val1);
            double val2 = sumassured + getBenifitPayableOnDeath_NonGauranteed_4();
//			System.out.println("val2 "+val2);
            double val3 = 0;
            double val4 = 0;
            if (getYear() == policyterm) {
                val3 = getTerminalBonus_4() * sumassured * policyterm * 0.15;
            } else {
                val3 = 0;
            }

            //System.out.println("val3 "+val3);
            if (ppt == 6) {
                double[] PWBarr = smartbachatdb.getSSVFactor_6years();
                int position = 0;
                int premiumPerOneLac = 0;
                for (int i = 1; i <= 25; i++) {
                    for (int j = 12; j <= 25; j++) {
                        if ((getYear()) == i && (policyterm) == j) {
                            ////////System.out.println("getValFromPWBtable() -> "+ PWBarr[position]);
                            val4 = PWBarr[position];
                        }
                        position++;
                    }
                }


            } else if (ppt == 7) {


                double[] PWBarr = smartbachatdb.getSSVFactor_7years();
                int position = 0;
                int premiumPerOneLac = 0;
                for (int i = 1; i <= 25; i++) {
                    for (int j = 12; j <= 25; j++) {
                        if ((getYear()) == i && (policyterm) == j) {
                            ////////System.out.println("getValFromPWBtable() -> "+ PWBarr[position]);
                            val4 = PWBarr[position];
                        }
                        position++;
                    }
                }


            } else if (ppt == 10) {


                double[] PWBarr = smartbachatdb.getSSVFactor_10years();
                int position = 0;
                int premiumPerOneLac = 0;
                for (int i = 1; i <= 25; i++) {
                    for (int j = 15; j <= 25; j++) {
                        if ((getYear()) == i && (policyterm) == j) {
                            ////////System.out.println("getValFromPWBtable() -> "+ PWBarr[position]);
                            val4 = PWBarr[position];
                        }
                        position++;
                    }
                }


            } else if (ppt == 15) {


                double[] PWBarr = smartbachatdb.getSSVFactor_15years();
                int position = 0;
                int premiumPerOneLac = 0;
                for (int i = 1; i <= 25; i++) {
                    for (int j = 20; j <= 25; j++) {
                        if ((getYear()) == i && (policyterm) == j) {
                            ////////System.out.println("getValFromPWBtable() -> "+ PWBarr[position]);
                            val4 = PWBarr[position];
                        }
                        position++;
                    }
                }


            }

            //11-12-2019 added by sujata
			/*System.out.println("a"+a);
			System.out.println("ppt"+ppt);
			System.out.println("sumassured"+sumassured);
			System.out.println("getBenifitPayableOnDeath_NonGauranteed_4()"+getBenifitPayableOnDeath_NonGauranteed_4());
			System.out.println("val4"+val4);*/
            SurrenederValue_Guaranteed_4 = Double.parseDouble(commonforall.getRound(commonforall.getRoundOffLevel2New(commonforall.getStringWithout_E(((a / ppt * sumassured + getBenifitPayableOnDeath_NonGauranteed_4()) * val4)))));

        }
    }

    public double getSurrenederValue_Guaranteed_4() {
        return SurrenederValue_Guaranteed_4;
    }


    public void setSurrenederValue_Guaranteed_8(int policyterm, int ppt, double sumassured) {
        if (getYear() > policyterm) {
            SurrenederValue_Guaranteed_8 = 0;
        } else {
            double a = Math.min(getYear(), ppt);

            double val1 = a / ppt;
            //System.out.println("Year "+getYear());
            //System.out.println("\nval1 "+val1);
            double val2 = sumassured + getBenifitPayableOnDeath_NonGauranteed_8();
            //System.out.println("val2 "+val2);
            double val3 = 0;
            double val4 = 0;
            if (getYear() == policyterm) {
                val3 = getTerminalBonus_8();
            } else {
                val3 = 0;
            }


            if (ppt == 6) {


                double[] PWBarr = smartbachatdb.getSSVFactor_6years();
                int position = 0;
                int premiumPerOneLac = 0;
                for (int i = 1; i <= 25; i++) {
                    for (int j = 12; j <= 25; j++) {
                        if ((getYear()) == i && (policyterm) == j) {
                            ////////System.out.println("getValFromPWBtable() -> "+ PWBarr[position]);
                            val4 = PWBarr[position];
                        }
                        position++;
                    }
                }


            } else if (ppt == 7) {
                double[] PWBarr = smartbachatdb.getSSVFactor_7years();
                int position = 0;
                int premiumPerOneLac = 0;
                for (int i = 1; i <= 25; i++) {
                    for (int j = 12; j <= 25; j++) {
                        if ((getYear()) == i && (policyterm) == j) {
                            // System.out.println("getValFromPWBtable() -> "+ PWBarr[position]);
                            val4 = PWBarr[position];
                        }
                        position++;
                    }
                }


            } else if (ppt == 10) {
                double[] PWBarr = smartbachatdb.getSSVFactor_10years();
                int position = 0;
                int premiumPerOneLac = 0;
                for (int i = 1; i <= 25; i++) {
                    for (int j = 15; j <= 25; j++) {
                        if ((getYear()) == i && (policyterm) == j) {
                            ////////System.out.println("getValFromPWBtable() -> "+ PWBarr[position]);
                            val4 = PWBarr[position];
                        }
                        position++;
                    }
                }


            } else if (ppt == 15) {
                double[] PWBarr = smartbachatdb.getSSVFactor_15years();
                int position = 0;
                int premiumPerOneLac = 0;
                for (int i = 1; i <= 25; i++) {
                    for (int j = 20; j <= 25; j++) {
                        if ((getYear()) == i && (policyterm) == j) {
                            ////////System.out.println("getValFromPWBtable() -> "+ PWBarr[position]);
                            val4 = PWBarr[position];
                        }
                        position++;
                    }
                }


            }
            //11-12-2019 added by sujata
            //System.out.println(" surr "+commonforall.getStringWithout_E(((a /ppt*sumassured + getBenifitPayableOnDeath_NonGauranteed_8()-val3)*val4)));
            SurrenederValue_Guaranteed_8 = Double.parseDouble(commonforall.getRound(commonforall.getRoundOffLevel2New(commonforall.getStringWithout_E(((a / ppt * sumassured + getBenifitPayableOnDeath_NonGauranteed_8()) * val4)))));

        }


    }

    public double getSurrenederValue_Guaranteed_8() {
        return SurrenederValue_Guaranteed_8;
    }


    //added by sujata on 11-12-2019///
    //31-12-2019
    public void setPremium_with_ST(boolean jk, boolean state) {
        String a;
        String c;
        double b, p;
        double premVal;
        c = commonforall.getRoundUp(commonforall.getStringWithout_E(getPremiumBeforeST()));
        p = Double.parseDouble(c);

        if (jk) {
            Premium_with_ST = (1.0126) * getPremiumBeforeST();
        } else {
            //added by sujata on 21-11-2019///
            //if(smartbachatBean.getState())
            if (state == true) {
//				Premium_with_ST = (1.025) * getPremiumBeforeST();
                premVal = (1.025) * getPremiumBeforeST();
            } else {
                //Premium_with_ST = (1.0225) * getPremiumBeforeST();
                premVal = (1.0225) * p;
                a = commonforall.getRoundUp(commonforall.getStringWithout_E(premVal));
                premVal = Double.parseDouble(a);
            }

            Premium_with_ST = (1.0225) * p;
            a = commonforall.getRoundUp(commonforall.getStringWithout_E(Premium_with_ST));
            Premium_with_ST = Double.parseDouble(a);


//			System.out.println("smartbachatBean.getState() " + smartbachatBean.getState());

            //Premium_with_ST =Premium_with_ST +Premium_with_ST;
            Premium_with_ST = premVal + Premium_with_ST;

            a = commonforall.getRoundUp(commonforall.getStringWithout_E(Premium_with_ST));
            Premium_with_ServiceTax = Double.parseDouble(a);
            //System.out.println("premservicetax "+Premium_with_ServiceTax);

        }


//		System.out.print("Premium_with_ServiceTax "+Premium_with_ServiceTax);

    }

    public double getPremium_with_ST() {
        return Premium_with_ServiceTax;

    }

    //11-12-2019 added by sujata
    //26-12-2019
    public void setPremium_with_ATPDB_FirstYear(String plantype, boolean jk, boolean state) {
        String a, b, c, x, y, z;
        double t, premAtpdb;

        if (plantype.equals("Option A (Endowment Option)")) {
            Premium_with_ServiceTax_FirstYear = 0;

        } else {
            if (jk) {
                Premium_with_ServiceTax_FirstYear = ((1.0126) * getPremiumBeforeST_ADTPD()) - getPremiumBeforeST_ADTPD();
            } else {
                //added by sujata on 21-11-2019///
                if (state == true) {

//				Premium_with_ST = ((1.025) * getPremiumBeforeST_ADTPD())-getPremiumBeforeST_ADTPD();
//				a = commonforall.getRoundUp(commonforall.getStringWithout_E(Premium_with_ST));
//				Premium_with_ST = Double.parseDouble(a);

                    premAtpdb = ((1.025) * getPremiumBeforeST_ADTPD()) - getPremiumBeforeST_ADTPD();
                    a = commonforall.getRoundUp(commonforall.getStringWithout_E(premAtpdb));
                    premAtpdb = Double.parseDouble(a);

                } else {
                    premAtpdb = ((1.0225) * getPremiumBeforeST_ADTPD());
                    a = commonforall.getRoundUp(commonforall.getStringWithout_E(premAtpdb));
                    premAtpdb = Double.parseDouble(a);
                    premAtpdb = premAtpdb - getPremiumBeforeST_ADTPD();
                    c = commonforall.getRound(commonforall.getStringWithout_E(premAtpdb));
                    premAtpdb = Double.parseDouble(c);


                }
                //Premium_with_ST = (1.0375) * getPremiumBeforeST();
                //(((1.0225) * getPremiumBeforeST_ADTPD())-getPremiumBeforeST_ADTPD())
                Premium_with_ST = ((1.0225) * getPremiumBeforeST_ADTPD());
                a = commonforall.getRoundUp(commonforall.getStringWithout_E(Premium_with_ST));
                Premium_with_ST = Double.parseDouble(a);
                Premium_with_ST = Premium_with_ST - getPremiumBeforeST_ADTPD();
                c = commonforall.getRound(commonforall.getStringWithout_E(Premium_with_ST));
                Premium_with_ST = Double.parseDouble(c);


                Premium_with_ST = premAtpdb + Premium_with_ST;
                //Premium_with_ST = Premium_with_ST + (((1.0225) * getPremiumBeforeST_ADTPD())-getPremiumBeforeST_ADTPD());

                a = commonforall.getRoundUp(commonforall.getStringWithout_E(Premium_with_ST));
                Premium_with_ServiceTax = Double.parseDouble(a);

            }

            Premium_with_ServiceTax_FirstYear = (Premium_with_ServiceTax + getPremiumBeforeST_ADTPD());
            a = commonforall.getRound(commonforall.getStringWithout_E(Premium_with_ServiceTax_FirstYear));
            Premium_with_ServiceTax_FirstYear = Double.parseDouble(a);


        }

//		System.out.print("\n Premium_with_ServiceTax_FirstYear "+Premium_with_ServiceTax_FirstYear);

    }
    //11-12-2019 added by sujata

    public double getPremium_with_ATPDB_FirstYear() {
        return Premium_with_ServiceTax_FirstYear;

    }

    public void setAdtpd(String plantype, boolean jk, boolean state, double sumassured) {
        double temp = 0;
        System.out.println("getBaseRateFor_ADTPD()" + getBaseRateFor_ADTPD());
        System.out.println("getBaseRate()" + getBaseRate());
//		temp =getBaseRateFor_ADTPD() -getBaseRate() ;
        temp = getBaseRateFor_ADTPD();

        if (plantype.equals("Option A (Endowment Option)")) {
            adtpd = 0;

        } else {
//		double a =Double.parseDouble(commonforall.getRoundOffLevel2New(commonforall.getStringWithout_E((((getBaseRate() - getSARebate()) * sumassured)/1000) * (1- getStaffRebate()) * getLoadingFrequencyPremium()/1 )));
            adtpd = Double.parseDouble(commonforall.getRoundUp(commonforall.getStringWithout_E((((temp * (1 - getStaffRebate()) * sumassured) / 1000) * getLoadingFrequencyPremium() / 1))));
        }
    }

    public double getAdtpd() {

        return adtpd;
    }

    public void setAdtpd1(String plantype, boolean jk, boolean state) {

        double premAtpdb = 0, premAtpdb1 = 0;
        String a, b;
        if (plantype.equals("Option A (Endowment Option)")) {
            Premium_with_ServiceTax_FirstYear = 0;

        } else {
            if (jk) {
                Premium_with_ServiceTax_FirstYear = ((1.0126) * getPremiumBeforeST_ADTPD()) - getPremiumBeforeST_ADTPD();
            } else {
                //added by sujata on 21-11-2019///
                if (state == true) {

//				Premium_with_ST = ((1.025) * getPremiumBeforeST_ADTPD())-getPremiumBeforeST_ADTPD();
//				a = commonforall.getRoundUp(commonforall.getStringWithout_E(Premium_with_ST));
//				Premium_with_ST = Double.parseDouble(a);

                    premAtpdb = ((1.025) * getAdtpd()) - getAdtpd();
                    a = commonforall.getRoundUp(commonforall.getStringWithout_E(premAtpdb));
                    premAtpdb = Double.parseDouble(a);

                } else {
                    premAtpdb = ((1.0225 * getAdtpd()) - getAdtpd());
                    a = commonforall.getRoundUp(commonforall.getStringWithout_E(premAtpdb));
                    premAtpdb = Double.parseDouble(a);


                }
            }
            premAtpdb1 = ((1.0225 * getAdtpd()) - getAdtpd());
            b = commonforall.getRoundUp(commonforall.getStringWithout_E(premAtpdb1));
            premAtpdb1 = Double.parseDouble(b);

            atpdb1 = (premAtpdb + premAtpdb1) + getAdtpd();
        }
    }

    public double getAdtpd1() {

        return atpdb1;
    }


    public void setAdtpd2(String plantype, boolean jk, boolean state) {

        double premAtpdb = 0, premAtpdb1 = 0;
        String a, b;
        if (plantype.equals("Option A (Endowment Option)")) {
            Premium_with_ServiceTax_FirstYear = 0;

        } else {
            if (jk) {
                Premium_with_ServiceTax_FirstYear = ((1.0126) * getPremiumBeforeST_ADTPD()) - getPremiumBeforeST_ADTPD();
            } else {
                //added by sujata on 21-11-2019///
                if (state == true) {

//				Premium_with_ST = ((1.025) * getPremiumBeforeST_ADTPD())-getPremiumBeforeST_ADTPD();
//				a = commonforall.getRoundUp(commonforall.getStringWithout_E(Premium_with_ST));
//				Premium_with_ST = Double.parseDouble(a);

                    premAtpdb = ((1.0125) * getAdtpd()) - getAdtpd();
                    a = commonforall.getRoundUp(commonforall.getStringWithout_E(premAtpdb));
                    premAtpdb = Double.parseDouble(a);

                } else {
                    premAtpdb = ((1.01125 * getAdtpd()) - getAdtpd());
                    a = commonforall.getRoundUp(commonforall.getStringWithout_E(premAtpdb));
                    premAtpdb = Double.parseDouble(a);


                }
            }
			/*premAtpdb1 = ((1.01125 * getAdtpd()) -getAdtpd());
			b = commonforall.getRoundUp(commonforall.getStringWithout_E(premAtpdb1));
			premAtpdb1 = Double.parseDouble(b);*/
            atpdb2 = ((premAtpdb * 2) + getAdtpd());
        }
    }

    public double getAdtpd2() {

        return atpdb2;
    }

    //11-12-2019 added by sujata
    //26-12-2019
    public void setPremium_with_ATPDB_SecondYear(String plantype, boolean jk, boolean state) {
        String a, c, r;

        double b, t, premAtpdb2;
        r = commonforall.getRound(commonforall.getStringWithout_E(getPremiumBeforeST_ADTPD()));
        t = Double.parseDouble(r);

        if (plantype.equals("Option A (Endowment Option)")) {
            Premium_with_ServiceTax = 0;

        } else {
            if (jk) {
                Premium_with_ST = ((1.0126) * getPremiumBeforeST_ADTPD()) - getPremiumBeforeST_ADTPD();
            } else {
                //added by sujata on 21-11-2019///
                if (state == true) {
                    premAtpdb2 = ((1.0125) * getPremiumBeforeST_ADTPD()) - getPremiumBeforeST_ADTPD();
                    a = commonforall.getRoundUp(commonforall.getStringWithout_E(premAtpdb2));
                    premAtpdb2 = Double.parseDouble(a);
                    //System.out.println("\n Premium_with_ServiceTax "+Premium_with_ServiceTax);
                } else {
                    //	System.out.println("t "+t);
                    premAtpdb2 = ((1.01125) * t);
                    a = commonforall.getRoundUp(commonforall.getStringWithout_E(premAtpdb2));
                    premAtpdb2 = Double.parseDouble(a);
                    premAtpdb2 = premAtpdb2 - getPremiumBeforeST_ADTPD();
                    c = commonforall.getRound(commonforall.getStringWithout_E(premAtpdb2));
                    premAtpdb2 = Double.parseDouble(c);
                }

                Premium_with_ST = ((1.01125) * t);
                a = commonforall.getRoundUp(commonforall.getStringWithout_E(Premium_with_ST));
                Premium_with_ST = Double.parseDouble(a);
                Premium_with_ST = Premium_with_ST - getPremiumBeforeST_ADTPD();
                c = commonforall.getRound(commonforall.getStringWithout_E(Premium_with_ST));
                Premium_with_ST = Double.parseDouble(c);


                //	Premium_with_ST = Premium_with_ST + (((1.01125) * getPremiumBeforeST_ADTPD())-getPremiumBeforeST_ADTPD());
                Premium_with_ST = premAtpdb2 + Premium_with_ST;
                a = commonforall.getRoundUp(commonforall.getStringWithout_E(Premium_with_ST));
                Premium_with_ServiceTax = Double.parseDouble(a);
                //System.out.println("\n Premium_with_ServiceTax "+Premium_with_ServiceTax);
            }

            Premium_with_ServiceTax = (Premium_with_ServiceTax + getPremiumBeforeST_ADTPD());
            a = commonforall.getRound(commonforall.getStringWithout_E(Premium_with_ServiceTax));
            Premium_with_ServiceTax = Double.parseDouble(a);

        }
		/*Premium_with_ServiceTax_FirstYear = (Premium_with_ServiceTax + getPremiumBeforeST_ADTPD());
		a =commonforall.getRoundUp(commonforall.getStringWithout_E(Premium_with_ServiceTax_FirstYear));
		Premium_with_ServiceTax_FirstYear = Double.parseDouble(a);
		*/


//		System.out.print("\n Premium_with_ServiceTaxSecond "+Premium_with_ServiceTax);

    }

    //11-12-2019 added by sujata
    public double getPremium_with_ATPDB_SecondYear() {
        return Premium_with_ServiceTax;

    }

    //added by sujata on 09-12-2019
    //31-12-2019
    public void setST_firstYear() {
        String a, c;
        double b, p;
        c = commonforall.getRoundUp(commonforall.getStringWithout_E(getPremiumBeforeST()));
        p = Double.parseDouble(c);
        //b = (getPremium_with_ST() - getPremiumBeforeST()) ;
        b = (getPremium_with_ST() - p);
        //System.out.println("p "+p);
        //System.out.println("getPremium_with_ST() "+getPremium_with_ST());
        a = commonforall.getRoundUp(commonforall.getStringWithout_E(b));
        ST_firstYear = Double.parseDouble(a);
//		System.out.print("\nST_firstYear "+ST_firstYear);
    }

    //31-12-2019
    public double getST_firstYear() {
        return ST_firstYear;
    }


    //11-12-2019 added by sujata
    public void setPremium_with_ST_secondyear(boolean jk, boolean state) {
        String a;
        String c;
        double b, p;
        double premValue;
        c = commonforall.getRoundUp(commonforall.getStringWithout_E(getPremiumBeforeST()));
        p = Double.parseDouble(c);
        if (jk) {
            Premium_with_ST_secondyear = (1.0126) * getPremiumBeforeST();
        } else {
            //Premium_with_ST_secondyear = (1.01876) * getPremiumBeforeST();
            if (state == true) {
                //Premium_with_ST_secondyear = (1.0125) * getPremiumBeforeST();
                premValue = (1.0125) * p;
            } else {
                //Premium_with_ST_secondyear = (1.01125) * getPremiumBeforeST();
//				Premium_with_ST_secondyear = (1.01125) * p;
//				a = commonforall.getRoundUp(commonforall.getStringWithout_E(Premium_with_ST_secondyear));
//				Premium_with_ST_secondyear = Double.parseDouble(a);

                premValue = (1.01125) * p;
                a = commonforall.getRoundUp(commonforall.getStringWithout_E(premValue));
                premValue = Double.parseDouble(a);


            }

            Premium_with_ST_secondyear = (1.01125) * p;
            a = commonforall.getRoundUp(commonforall.getStringWithout_E(Premium_with_ST_secondyear));
            Premium_with_ST_secondyear = Double.parseDouble(a);

            //Premium_with_ST_secondyear = Premium_with_ST_secondyear + (1.01125) * getPremiumBeforeST();
            Premium_with_ST_secondyear = premValue + Premium_with_ST_secondyear;
            a = commonforall.getRoundUp(commonforall.getStringWithout_E(Premium_with_ST_secondyear));
            Premium_with_ST_secondyear = Double.parseDouble(a);
        }

//		System.out.print("\nPremium_with_ST_secondyear123456789 "+(Premium_with_ST_secondyear));
    }

    public double getPremium_with_ST_secondyear() {
        return Premium_with_ST_secondyear;
    }

    //added by sujata on 09-12-2019
    //31-12-2019
    public void setST_SecondYear() {
        String a, c;
        double b, p;
        c = commonforall.getRoundUp(commonforall.getStringWithout_E(getPremiumBeforeST()));
        p = Double.parseDouble(c);
        b = (getPremium_with_ST_secondyear() - p);
        a = commonforall.getRoundUp(commonforall.getStringWithout_E(b));
        ST_SecondYear = Double.parseDouble(a);
//		System.out.print("\n ST_SecondYear "+ST_SecondYear);
    }

    public double getST_SecondYear() {
        return ST_SecondYear;
    }


    //31-12-2019
    public void setBasicServiceTax_firstYear(boolean jk) {
        double a;
        String b;
        if (jk) {
            a = ((1 + smartbachatproperties.Basic_service_tax_FY_JK) * getModalPremium()) - getModalPremium();
            b = commonforall.getRoundUp(commonforall.getStringWithout_E(a));
            BasicServiceTax_firstYear = Double.parseDouble(b);
        } else {
            a = ((1 + smartbachatproperties.Basic_service_tax_FY) * getModalPremium()) - getModalPremium();
            b = commonforall.getRoundUp(commonforall.getStringWithout_E(a));
            BasicServiceTax_firstYear = Double.parseDouble(b);
        }

        //System.out.print("\n BasicServiceTax_firstYear "+BasicServiceTax_firstYear);
    }

    public double getBasicServiceTax_firstYear() {
        return BasicServiceTax_firstYear;
    }

    /*********Added by sujata on 22-11-2019****************/
    //11-12-2019 added by sujata
    public double get_Annualized_Premium(int year_F, int ppt, double adtpdSumAssured, double sumassured) {
        if (year_F <= ppt) {
            //  System.out.println("\nannu1 "+get_Premium_Without_ST_WithoutLoadingFreq(sumassured));
            //return getTotalPremiumPaid_withoutST();
            // System.out.println("Annulizesd "+ (get_Premium_Without_ST_WithoutLoadingFreq(sumassured) + getPremiumBeforeSTWithoutDiscWithoutLoading_ADTPD(adtpdSumAssured)));
            return (get_Premium_Without_ST_WithoutLoadingFreq(sumassured) + getPremiumBeforeSTWithoutDiscWithoutLoading_ADTPD(adtpdSumAssured));
        } else
            return 0;
    }


    //11-12-2019 added by sujata
    public double getMaturity_Benefit4(int year_F, int PolicyTerm) {
        if (year_F == PolicyTerm) {

            return (getSurvivalBenifit_Guaranteed() + getBenifitPayableOnDeath_NonGauranteed_4() + 0.15 * getBenifitPayableOnDeath_NonGauranteed_4());
        } else {
            return 0;
        }

    }

    //11-12-2019 added by sujata
    public double getMaturity_Benefit8(int year_F, int PolicyTerm) {
        if (year_F == PolicyTerm)
            return (getSurvivalBenifit_Guaranteed() + (getBenifitPayableOnDeath_NonGauranteed_8()) + 0.15 * getBenifitPayableOnDeath_NonGauranteed_8());
        else
            return 0;

    }

    //11-12-2019 added by sujata
    public double get_Total_Base_Premium_Paid(int year_F, double premBasic,
                                              int PolicyTerm, String PremiumFreq) {
        if (year_F != 1 && PremiumFreq.equalsIgnoreCase("Single")) {
            return 0;

        } else {
            if (year_F <= PolicyTerm)
                return premBasic;
            else
                return 0;
        }
    }

    //11-12-2019 added by sujata
    public double getTotalDeathBenefit4per(int year_F, int PolicyTerm, double SumAssured_Basic, double sumAnnualizedPrem) {

        //return getBenifitPayableOnDeath_Gauranteed()+getBenifitPayableOnDeath_NonGauranteed_4();
        double max1, max2, totalDeathBenefit4;
        if (year_F <= PolicyTerm) {

            max1 = (getBenifitPayableOnDeath_Gauranteed() + getBenifitPayableOnDeath_NonGauranteed_4() + getBenifitPayableOnDeath_NonGauranteed_4() * 0.15);

            //System.out.println("max1 "+max1);
            max2 = (1.05 * sumAnnualizedPrem);
            //System.out.println("\naddi "+(getBenifitPayableOnDeath_Gauranteed()+getBenifitPayableOnDeath_NonGauranteed_4()+getBenifitPayableOnDeath_NonGauranteed_4()));
            totalDeathBenefit4 = Math.max(max1, max2);
            //System.out.println("\nsumAnnualizedPrem "+sumAnnualizedPrem);
            //System.out.println("max2 "+max2);
            //System.out.println("totalDeathBenefit4 "+totalDeathBenefit4);
        } else {
            totalDeathBenefit4 = 0;
        }
        //System.out.println("max1 "+max1);

        return totalDeathBenefit4;
    }

    //11-12-2019 added by sujata
    public double getTotalDeathBenefit8per(int year_F, int PolicyTerm, double SumAssured_Basic, double sumAnnualizedPrem) {

        //	return getBenifitPayableOnDeath_Gauranteed()+getBenifitPayableOnDeath_NonGauranteed_8();
        double max1, max2, totalDeathBenefit4;
        if (year_F <= PolicyTerm) {
            max1 = (getBenifitPayableOnDeath_Gauranteed() + getBenifitPayableOnDeath_NonGauranteed_8() + getBenifitPayableOnDeath_NonGauranteed_8() * 0.15);

            max2 = (1.05 * sumAnnualizedPrem);
            //System.out.println("\naddi "+(getBenifitPayableOnDeath_Gauranteed()+getBenifitPayableOnDeath_NonGauranteed_4()+getBenifitPayableOnDeath_NonGauranteed_4()));
            totalDeathBenefit4 = Math.max(max1, max2);
            //System.out.println("max8percent: "+max2);

        } else {
            totalDeathBenefit4 = 0;
        }
        //System.out.println("max1 "+max1);
        return totalDeathBenefit4;
    }


    /***********End****************/


    public void setSwachBharat_1styear(boolean jk) {
        double a;
        String b;
        if (jk) {
            a = ((1 + smartbachatproperties.SBC_FY_JK) * getModalPremium()) - getModalPremium();
            b = commonforall.getRoundUp(commonforall.getStringWithout_E(a));
            SwachBharat_1styear = Double.parseDouble(b);
        } else {
            a = ((1 + smartbachatproperties.SBC_FY) * getModalPremium()) - getModalPremium();
            b = commonforall.getRoundUp(commonforall.getStringWithout_E(a));
            SwachBharat_1styear = Double.parseDouble(b);
        }

        //System.out.print("\n SwachBharat_1styear "+SwachBharat_1styear);
    }

    public double getSwachBharat_1styear() {
        return SwachBharat_1styear;
    }

//  Added By Saurabh Jain on 16/05/2019 Start

    public void setKeralaServiceTax_1styear(boolean state) {
        double a;
        String b;
        if (state) {
            a = ((1 + smartbachatproperties.kerlaServiceTax) * getModalPremium()) - getModalPremium();
            b = commonforall.getRoundUp(commonforall.getStringWithout_E(a));
            KeralaServiceTax_1styear = Double.parseDouble(b);
        } else {
            KeralaServiceTax_1styear = 0;
        }
    }

    public double getKeralaServiceTax_1styear() {
        return KeralaServiceTax_1styear;
    }

//  Added By Saurabh Jain on 16/05/2019 End

    public void setKrishiKalyan_1stYear(boolean jk) {
        double a;
        String b;
        if (jk) {
            a = ((1 + smartbachatproperties.KKC_FY_JK) * getModalPremium()) - getModalPremium();
            b = commonforall.getRoundUp(commonforall.getRoundOffLevel2New(commonforall.getStringWithout_E(a)));
            KrishiKalyan_1stYear = Double.parseDouble(b);
        } else {
            a = ((1 + smartbachatproperties.KKC_FY) * getModalPremium()) - getModalPremium();
            b = commonforall.getRoundUp(commonforall.getRoundOffLevel2New(commonforall.getStringWithout_E(a)));
            KrishiKalyan_1stYear = Double.parseDouble(b);
        }

        ////System.out.print("KrishiKalyan_1stYear"+KrishiKalyan_1stYear);
    }

    public double getKrishiKalyan_1stYear() {
        return KrishiKalyan_1stYear;
    }

    public void setTotalTaxes1st_year(boolean state) {
        double a;
        String b;
        if (state) {
            a = getBasicServiceTax_firstYear() + getKeralaServiceTax_1styear();
            b = commonforall.getStringWithout_E(a);
            TotalTaxes1st_year = Double.parseDouble(b);
        } else {
            a = getBasicServiceTax_firstYear() + getSwachBharat_1styear() + getKrishiKalyan_1stYear();
            b = commonforall.getStringWithout_E(a);
            TotalTaxes1st_year = Double.parseDouble(b);
        }

        //	System.out.print("TotalTaxes1st_year"+TotalTaxes1st_year);
    }

    public double getTotalTaxes1st_year() {
        return TotalTaxes1st_year;
    }


    public void setBasicServiceTax_secondYear(boolean jk) {
        double a;
        String b;
        if (jk) {
            a = ((1 + smartbachatproperties.Basic_service_tax_SY_JK) * getModalPremium()) - getModalPremium();
//			b = commonforall.getRoundUp(commonforall.getRoundOffLevel2New(commonforall.getStringWithout_E(a)));
            b = commonforall.getRoundUp(commonforall.getStringWithout_E(a));
            BasicServiceTax_secondYear = Double.parseDouble(b);
        } else {
            a = ((1 + smartbachatproperties.Basic_service_tax_SY) * getModalPremium()) - getModalPremium();
//			b = commonforall.getRoundUp(commonforall.getRoundOffLevel2New(commonforall.getStringWithout_E(a)));
            b = commonforall.getRoundUp(commonforall.getStringWithout_E(a));
            BasicServiceTax_secondYear = Double.parseDouble(b);
        }

        ////System.out.print("BasicServiceTax_secondYear"+BasicServiceTax_secondYear);
    }

    public double getBasicServiceTax_secondYear() {
        return BasicServiceTax_secondYear;
    }


    public void setSwachBharat_2ndyear(boolean jk) {
        double a;
        String b;
        if (jk) {
            a = ((1 + smartbachatproperties.SBC_SY_JK) * getModalPremium()) - getModalPremium();
//			b = commonforall.getRoundUp(commonforall.getRoundOffLevel2New(commonforall.getStringWithout_E(a)));
            b = commonforall.getRoundUp(commonforall.getStringWithout_E(a));
            SwachBharat_2ndyear = Double.parseDouble(b);
        } else {
            a = ((1 + smartbachatproperties.SBC_SY) * getModalPremium()) - getModalPremium();


//			b = commonforall.getRoundUp(commonforall.getRoundOffLevel2New(commonforall.getStringWithout_E(a)));
            b = commonforall.getRoundUp(commonforall.getStringWithout_E(a));
            SwachBharat_2ndyear = Double.parseDouble(b);
        }

        ////System.out.print("SwachBharat_2ndyear"+SwachBharat_2ndyear);
    }

    public double getSwachBharat_2ndyear() {
        return SwachBharat_2ndyear;
    }

    //  Added By Saurabh Jain on 16/05/2019 Start
    public void setKeralaServiceTax_2ndyear(boolean state) {
        double a;
        String b;
        if (state) {
            a = ((1 + smartbachatproperties.kerlaServiceTaxSecondYear) * getModalPremium()) - getModalPremium();
//			b = commonforall.getRoundUp(commonforall.getRoundOffLevel2New(commonforall.getStringWithout_E(a)));
            b = commonforall.getRoundUp(commonforall.getStringWithout_E(a));
            KeralaServiceTax_2ndyear = Double.parseDouble(b);
        } else {
            KeralaServiceTax_2ndyear = 0;
        }
    }

//  Added By Saurabh Jain on 16/05/2019 Start

    public double getKeralaServiceTax_2ndyear() {
        return KeralaServiceTax_2ndyear;
    }


    public void setKrishiKalyan_2ndYear(boolean jk) {
        double a;
        String b;
        if (jk) {
            a = ((smartbachatproperties.KKC_SY_JK) * getModalPremium()) - getModalPremium();
            b = commonforall.getRoundUp(commonforall.getRoundOffLevel2New(commonforall.getStringWithout_E(a)));
//			KrishiKalyan_2ndYear = Double.parseDouble(b);
            KrishiKalyan_2ndYear = 0;
        } else {
            a = ((smartbachatproperties.KKC_SY) * getModalPremium()) - getModalPremium();
            b = commonforall.getRoundUp(commonforall.getRoundOffLevel2New(commonforall.getStringWithout_E(a)));
//			KrishiKalyan_2ndYear = Double.parseDouble(b);
            KrishiKalyan_2ndYear = 0;
        }

        ////System.out.print("KrishiKalyan_2ndYear"+KrishiKalyan_2ndYear);
    }

    public double getKrishiKalyan_2ndYear() {
        return KrishiKalyan_2ndYear;
    }

    public void setTotalTaxes2nd_year(boolean state) {
        double a;
        String b;

        if (state) {
            a = getBasicServiceTax_secondYear() + getKeralaServiceTax_2ndyear();
            b = commonforall.getStringWithout_E(a);
            TotalTaxes2nd_year = Double.parseDouble(b);
        } else {
            a = getBasicServiceTax_secondYear() + getSwachBharat_2ndyear() + getKrishiKalyan_2ndYear();
            b = commonforall.getStringWithout_E(a);
            TotalTaxes2nd_year = Double.parseDouble(b);
        }
        ////System.out.print("KrishiKalyan_2ndYear"+KrishiKalyan_2ndYear);
    }

    public double getTotalTaxes2nd_year() {
        return TotalTaxes2nd_year;
    }

    public void setFinalPremium_WithTaxes_1stYear() {
        double a;
        String b;
        a = getST_firstYear() + getPremium_with_ATPDB_FirstYear();
//		a = getModalPremium()  + getTotalTaxes1st_year();
        b = commonforall.getRound(commonforall.getStringWithout_E(a));
//		b = commonforall.getRoundOffLevel2New(commonforall.getStringWithout_E(a));
        FinalPremium_WithTaxes_1stYear = Double.parseDouble(b);

//		System.out.println("\ngetST_firstYear()(): "+getST_firstYear());
//		System.out.println("\ngetPremium_with_ATPDB_FirstYear(): "+getPremium_with_ATPDB_FirstYear());
//		System.out.print("\nFinalPremium_WithTaxes_1stYear "+FinalPremium_WithTaxes_1stYear);
    }

    public double getFinalPremium_WithTaxes_1stYear() {
        return FinalPremium_WithTaxes_1stYear;
    }


    public void setFinalPremium_WithTaxes_2ndYear() {
        double a;
        String b;
//		a = getST_SecondYear() + getPremium_with_ATPDB_SecondYear();
        a = getModalPremium() + getTotalTaxes2nd_year();
        b = commonforall.getRound(commonforall.getStringWithout_E(a));
//		b = commonforall.getRoundOffLevel2New(commonforall.getStringWithout_E(a));
        FinalPremium_WithTaxes_2ndYear = Double.parseDouble(b);
//		System.out.println("getST_SecondYear() "+getST_SecondYear());
//		System.out.println(" getPremium_with_ATPDB_SecondYear()"+ getPremium_with_ATPDB_SecondYear());
        //	System.out.print("FinalPremium_WithTaxes_2ndYear"+FinalPremium_WithTaxes_2ndYear);
    }

    public double getFinalPremium_WithTaxes_2ndYear() {
        return FinalPremium_WithTaxes_2ndYear;
    }

    /**
     * added by vrushali on  10-Dec-2015
     * 1. PL_premium and PC_premium
     *
     * @return
     */
    public double getPremiumWithoutSTWithoutDisc(double sumassured) {
        double premiumWithoutSTWithoutDisc = 0;


        premiumWithoutSTWithoutDisc = ((((getBaseRate() - getSARebate()) * sumassured) / 1000) * (1 - 0) * getLoadingFrequencyPremium() / 1);
//		System.out.println("getBaseRate() "+ getBaseRate() );
//		System.out.println("sumassured "+sumassured);
//		System.out.println("getLoadingFrequencyPremium() "+getLoadingFrequencyPremium());
//		System.out.println("premiumWithoutSTWithoutDisc "+premiumWithoutSTWithoutDisc);

        return premiumWithoutSTWithoutDisc;
    }

    public double getPremiumWithoutSTWithoutDiscWithoutFreqLoading(double sumassured) {
        double premiumWithoutSTWithoutDiscWithoutFreqLoading = 0;


        premiumWithoutSTWithoutDiscWithoutFreqLoading = ((((getBaseRate() - 0) * sumassured) / 1000) * (1 - 0));
//		System.out.println("getBaseRate() "+ getBaseRate() );
//		System.out.println("sumassured "+sumassured);
//		System.out.println("premiumWithoutSTWithoutDiscWithoutFreqLoading "+premiumWithoutSTWithoutDiscWithoutFreqLoading);
        return premiumWithoutSTWithoutDiscWithoutFreqLoading;


    }

    public double getPremiumBeforeSTWithoutDisc_ADTPD(double adtpdSumAssured) {
        double a = (getBaseRateFor_ADTPD() * (1 - 0)) * adtpdSumAssured / 1000 * getLoadingFrequencyPremiumfor_ADTPD() / 1;
        //double a = ((getBaseRateFor_ADTPD() - StaffRebate_ADTPD) * (1 - getStaffRebatefor_ADTPD()) + (smartbachatproperties.NSAP + smartbachatproperties.EMR)) * (sumassured/1000) * (getLoadingFrequencyPremiumfor_ADTPD()/1);
        //	double a =(getBaseRate()* (1-getStaffRebate()) - getSARebate() +(smartbachatproperties.NSAP + smartbachatproperties.EMR))* (sumassured/1000) *(getLoadingFrequencyPremium()/1) ;
        //String b = commonforall.getRoundUp(a);


        premiumBeforeSTWithoutDisc_ADTPD = a;
        return premiumBeforeSTWithoutDisc_ADTPD;
    }

//	public double getPremiumBeforeSTWithoutDisc_ADTPD()
//	{
//		return premiumBeforeSTWithoutDisc_ADTPD ;
//	}

    //13-12-2019
    public double getPremiumBeforeSTWithoutDiscWithoutLoading_ADTPD(double adtpdSumAssured) {
        //premiumBeforeSTWithoutDiscWithoutLoading_ADTPD = Double.parseDouble(commonforall.getRoundOffLevel2New(commonforall.getStringWithout_E((((getBaseRate() - getSARebate()) * sumassured)/1000)
        premiumBeforeSTWithoutDiscWithoutLoading_ADTPD = Double.parseDouble(commonforall.getRound(commonforall.getStringWithout_E(getBaseRateFor_ADTPD() * (1 - getStaffRebatefor_ADTPD()) * adtpdSumAssured / 1000)));
        //double a = ((getBaseRateFor_ADTPD() - StaffRebate_ADTPD) * (1 - getStaffRebatefor_ADTPD()) + (smartbachatproperties.NSAP + smartbachatproperties.EMR)) * (sumassured/1000) * (getLoadingFrequencyPremiumfor_ADTPD()/1);

//		System.out.println("\ngetBaseRateFor_ADTPD() "+getBaseRateFor_ADTPD());
//		System.out.println("adtpdSumAssured "+adtpdSumAssured);
        // = a;
//		System.out.println("premiumBeforeSTWithoutDiscWithoutLoading_ADTPD "+premiumBeforeSTWithoutDiscWithoutLoading_ADTPD);
        return premiumBeforeSTWithoutDiscWithoutLoading_ADTPD;
    }

//	public double getPremiumBeforeSTWithoutDiscWithoutLoading_ADTPD()
//	{
//		return premiumBeforeSTWithoutDiscWithoutLoading_ADTPD ;
//	}

    /********** Added by Vrushali on 23-Dec-2016 **************/

    public double getMinesOccuInterest(double SumAssured_Basic) {
        return (SumAssured_Basic / 1000) * 2;
    }

    public double getBackDateInterest(double grossPremium, String bkDate) {

        try {

            SmartBachatBean smartBachatBean = new SmartBachatBean();
//		double indigoRate=7.5;
            /**
             * Modified by Akshaya. Rate Change from 1-APR-2015
             */

            /******************* Modified by Akshaya on 14-APR-2015 start**********/
//		double indigoRate=10;

            /******************* Modified by Saurabh Jain on 08-APR-2019 start **********/
            double indigoRate = 6.50;
            /******************* Modified by Saurabh Jain on 08-APR-2019 end **********/

            double SeriviceTax = 0;
            if (smartBachatBean.getIsJammuResident())
                SeriviceTax = smartbachatproperties.Basic_service_tax_FY_JK;
            else
                SeriviceTax = smartbachatproperties.Basic_service_tax_FY;

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
            int days = commonforall.setDate(date, strBackDate);
//       ////System.out.println("no of days "+days);
            double monthsBetween = commonforall.roundDown((double) days / 365 * 12, 0);
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
    }         //End
    /********** Added by Vrushali on 23-Dec-2016 **************/


    /**
     * Added by vrushali on 27 Dec 2016
     *
     * @param sumassured
     */

    public void setYearlyPremiumBeforeModalLoading(double sumassured) {
        yearlyPremiumBeforeModalLoading = (((getBaseRate() - getSARebate()) * sumassured) / 1000) * (1 - getStaffRebate());
    }

    public double getYearlyPremiumBeforeModalLoading() {
        //System.out.println("sdc "+ yearlyPremiumBeforeModalLoading);
        return yearlyPremiumBeforeModalLoading;

    }

    public void setYearlyPremiumBeforeModalLoading_ADTPD(double adtpdSumAssured) {
        //System.out.println("ac24 "+ ( getBaseRateFor_ADTPD() * (1-getStaffRebatefor_ADTPD())) * adtpdSumAssured/1000 );
        yearlyPremiumBeforeModalLoading_ADTPD = (getBaseRateFor_ADTPD() * (1 - getStaffRebatefor_ADTPD())) * adtpdSumAssured / 1000;
    }

    public double getYearlyPremiumBeforeModalLoading_ADTPD() {
        return yearlyPremiumBeforeModalLoading_ADTPD;

    }

    public void setAnnualPremiumWithoutModal() {
        AnnualPremiumWithoutModal = Double.parseDouble(commonforall.getRoundUp(commonforall.getStringWithout_E(getYearlyPremiumBeforeModalLoading() + getYearlyPremiumBeforeModalLoading_ADTPD())));
    }

    public double getAnnualPremiumWithoutModal() {
        System.out.println("AnnualPremiumWithoutModal " + AnnualPremiumWithoutModal);
        return AnnualPremiumWithoutModal;
    }


    public double getServiceTaxMines(double MinesOccuInterest) {
        return Double.parseDouble(commonforall.getRoundUp(commonforall.getRoundOffLevel2(commonforall.getStringWithout_E(MinesOccuInterest * (smartbachatproperties.Basic_service_tax_FY + smartbachatproperties.SBC_FY)))));
    }


/**
 * End
 */


}
