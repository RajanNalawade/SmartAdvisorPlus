package sbilife.com.pointofsale_bancaagency.poornsuraksha;


import sbilife.com.pointofsale_bancaagency.common.CommonForAllProd;

class PoornSurakshaBusinessLogic {

    private PoornSurakshaBean poornasurakshabean;
    private PoornSurakshaDB poornasurakshaDB;
    private PoornSurakshaProperties poornasurakshaproperties;
    private CommonForAllProd cfap;
    private double PremiumRate = 0;
    private double PremiumRate_CI = 0;
    private double StaffRebate = 0;
    private double SARebate = 0;
    private double LoadingFrequencyPremium = 0;
    private double PremiumBeforeST = 0;
    private double PremiumBeforeST_CI = 0;
    private String TotalPremiumWithST;
    private String TotalPremiumWithST_CI;
    private String TotalPremiumWithoutST;
    private String TotalPremiumWithoutST_CI;
    private double ServiceTax = 0;
    private double ServiceTax_CI = 0;
    private double TotalBasePremiumPaidWithoutTaxes = 0;
    private double GuaranteedDeathBenifit = 0;
    private double Percentage = 0;
    private double GuaranteedCriticalillnessBenifit = 0;
    private double GuaranteedDeathBenifit1 = 0;
    private double SumAssured_CI = 0;
    private double totInstPrem_exclST_exclDisc = 0;
    private double totalInstPrem_exclST_exclDisc = 0;
    private double totInstPrem_exclST_exclDisc_exclFreqLoading = 0;
    private double rider_Prem_exclST_exclDisc = 0;
    private double rider_Prem_exclST_exclDisc_exclFreqLoading = 0;
    private double SumAssured_basic_80 = 0;

    public PoornSurakshaBusinessLogic(PoornSurakshaBean poornSurakshaBean) {
        this.poornasurakshabean = poornSurakshaBean;
        poornasurakshaDB = new PoornSurakshaDB();
        poornasurakshaproperties = new PoornSurakshaProperties();
        cfap = new CommonForAllProd();
    }

    public double getStaffRebate(boolean IsStaff) {

        if (IsStaff) {
            StaffRebate = 0.06;
        } else {
            StaffRebate = 0.00;
        }
        return StaffRebate;

    }

    public double getSARebate(double sumassured) {

        if (sumassured >= 2000000 && sumassured < 5000000) {
            SARebate = 0;
        } else {
            if (sumassured >= 5000000 && sumassured < 10000000) {
                SARebate = 0.1;
            } else {
                SARebate = 0.15;
            }
        }

        return SARebate;

    }

    public double getLoadingFrequencyPremium(String premfreq) {

        if (premfreq.equals("Yearly")) {
            LoadingFrequencyPremium = 1;
        } else {
            if (premfreq.equals("Half-Yearly")) {
                LoadingFrequencyPremium = 0.51;
            } else {
                LoadingFrequencyPremium = 0.085;
            }

        }

        return LoadingFrequencyPremium;
    }

    public double getPremiumRate(String gender, String smoker, int age,
                                 int policyterm) {

        if (gender.equals("Male") && smoker.equals("Non-Smoker")
                || gender.equals("Third Gender") && smoker.equals("Non-Smoker")) {

            double[] PWBarr = poornasurakshaDB.getMale_NonSmoker_Rate();
            int position = 0;
            int premiumPerOneLac = 0;
            for (int i = 18; i <= 65; i++) {
                for (int j = 10; j <= 30; j = j + 5) {
                    if ((age) == i && (policyterm) == j) {
                        PremiumRate = PWBarr[position];
                        break;
                    }
                    position++;
                }
            }

            PremiumRate = PremiumRate;
            // Log.d(""+a, "tab");

        } else if (gender.equals("Male") && smoker.equals("Smoker")
                || gender.equals("Third Gender") && smoker.equals("Smoker")) {

            double[] PWBarr = poornasurakshaDB.getMale_Smoker_Rate();
            int position = 0;
            int premiumPerOneLac = 0;
            for (int i = 18; i <= 65; i++) {
                for (int j = 10; j <= 30; j = j + 5) {
                    if ((age) == i && (policyterm) == j) {
                        PremiumRate = PWBarr[position];
                        break;
                    }
                    position++;
                }
            }

            PremiumRate = PremiumRate;
            // Log.d(""+a, "tab");

        } else if (gender.equals("Female") && smoker.equals("Non-Smoker")) {

            double[] PWBarr = poornasurakshaDB.getFemale_NonSmoker_Rate();
            int position = 0;
            int premiumPerOneLac = 0;
            for (int i = 18; i <= 65; i++) {
                for (int j = 10; j <= 30; j = j + 5) {
                    if ((age) == i && (policyterm) == j) {
                        PremiumRate = PWBarr[position];
                        break;
                    }
                    position++;
                }
            }

            PremiumRate = PremiumRate;
            // Log.d(""+a, "tab");

        } else {

            double[] PWBarr = poornasurakshaDB.getFemale_Smoker_Rate();
            int position = 0;
            int premiumPerOneLac = 0;
            for (int i = 18; i <= 65; i++) {
                for (int j = 10; j <= 30; j = j + 5) {
                    if ((age) == i && (policyterm) == j) {
                        PremiumRate = PWBarr[position];
                        break;
                    }
                    position++;
                }
            }

            PremiumRate = PremiumRate;
            // Log.d(""+a, "tab");

        }

        PremiumRate = PremiumRate;
        PremiumRate = PremiumRate;
        return PremiumRate;

    }

    public double getPremiumBeforeST(double sumassured) {

        PremiumBeforeST = ((PremiumRate * (1 - SARebate)) * (1 - StaffRebate))
                * (sumassured / 1000) * LoadingFrequencyPremium;
        PremiumBeforeST = ((PremiumRate * (1 - SARebate)) * (1 - StaffRebate))
                * (sumassured / 1000) * LoadingFrequencyPremium;
        return PremiumBeforeST;

    }

    public String getTotalPremiumWithoutST() {
        TotalPremiumWithoutST = cfap.getRoundUp("" + PremiumBeforeST);

        return TotalPremiumWithoutST;
    }

	/*public String getTotalPremiumWithST() {
		TotalPremiumWithST = (Double.parseDouble(cfap.getRoundOffLevel2New(cfap
				.getStringWithout_E(Double.parseDouble(TotalPremiumWithoutST)
						* poornasurakshaproperties.CGST)))
				+ Double.parseDouble(cfap.getRoundOffLevel2New(cfap
						.getStringWithout_E(Double
								.parseDouble(TotalPremiumWithoutST)
								* poornasurakshaproperties.SGST))) + Double
					.parseDouble(TotalPremiumWithoutST))
				+ "";
		// System.out.println("  "+cfap.getRound(cfap.getStringWithout_E(Double.parseDouble(TotalPremiumWithoutST)
		// * poornasurakshaproperties.CGST)));
		return TotalPremiumWithST;
	}*/

    public String getTotalPremiumWithST() {
        TotalPremiumWithST = (Double.parseDouble(cfap.getRoundOffLevel2New(cfap.getStringWithout_E(Double.parseDouble(TotalPremiumWithoutST) * poornasurakshabean.getServiceTax()))) + Double.parseDouble(cfap.getRoundOffLevel2New(cfap.getStringWithout_E(Double.parseDouble(TotalPremiumWithoutST) * poornasurakshaproperties.SGST))) + Double.parseDouble(TotalPremiumWithoutST)) + "";
//				 System.out.println("  "+cfap.getRound(cfap.getStringWithout_E(Double.parseDouble(TotalPremiumWithoutST) * poornasurakshaproperties.CGST)));
        return TotalPremiumWithST;
    }

    //	public double getServiceTax(double InstallmentPremLifeCover_ExclSt) {
//		ServiceTax = Double.parseDouble(cfap.getRound(cfap
//				.getStringWithout_E(InstallmentPremLifeCover_ExclSt
//						* poornasurakshaproperties.CGST)))
//				+ Double.parseDouble(cfap.getRound(cfap
//						.getStringWithout_E(InstallmentPremLifeCover_ExclSt
//								* poornasurakshaproperties.SGST)));
//		// ServiceTax = Double.parseDouble(TotalPremiumWithST) -
//		// Double.parseDouble(TotalPremiumWithoutST) ;
//		return ServiceTax;
//
//	}
    public double getServiceTax(double InstallmentPremLifeCover_ExclSt, double CGST) {
        ServiceTax = Double.parseDouble(cfap.getRoundUp(cfap.getStringWithout_E(InstallmentPremLifeCover_ExclSt * CGST))) + Double.parseDouble(cfap.getRoundUp(cfap.getStringWithout_E(InstallmentPremLifeCover_ExclSt * poornasurakshaproperties.SGST)));
//                         ServiceTax = Double.parseDouble(TotalPremiumWithST) - Double.parseDouble(TotalPremiumWithoutST) ;
        return ServiceTax;


    }

    public double getTotalBasePremiumPaidWithoutTaxes(int policyyear,
                                                      String premfreq, double TotalFinalPremium_ExclST) {
        if (premfreq.equals("Yearly")) {
            TotalBasePremiumPaidWithoutTaxes = TotalFinalPremium_ExclST * 1
                    * policyyear;

        } else if (premfreq.equals("Half-Yearly")) {
            TotalBasePremiumPaidWithoutTaxes = TotalFinalPremium_ExclST * 2
                    * policyyear;

        } else {
            TotalBasePremiumPaidWithoutTaxes = TotalFinalPremium_ExclST * 12
                    * policyyear;
        }

        return TotalBasePremiumPaidWithoutTaxes;

    }

    private double getPercentage(int policyterm, int policyear) {
        if (policyear == 2) {
            if (policyterm == 10) {
                Percentage = GuaranteedCriticalillnessBenifit * 0.15;
            } else if (policyterm == 15) {
                Percentage = GuaranteedCriticalillnessBenifit * 0.1;
            } else if (policyterm == 20) {
                Percentage = GuaranteedCriticalillnessBenifit * 0.075;
            } else if (policyterm == 25) {
                Percentage = GuaranteedCriticalillnessBenifit * 0.06;
            } else if (policyterm == 30) {
                Percentage = GuaranteedCriticalillnessBenifit * 0.05;
            }

        }

        Percentage = Percentage;
        return Percentage;

    }

    public double getGuaranteedDeathBenifit(int policyyear, double sumassured,
                                            int policyterm) {
        if (policyyear == 1) {
            GuaranteedDeathBenifit = sumassured * 0.8;
        } else {
            GuaranteedDeathBenifit1 = GuaranteedDeathBenifit;
            GuaranteedDeathBenifit = GuaranteedDeathBenifit1
                    - getPercentage(policyterm, policyyear);
        }
        return GuaranteedDeathBenifit;

    }

    public double getGuaranteedCriticalillnessBenifit(int policyyear,
                                                      double sumassured, int policyterm) {

        if (policyyear == 1) {
            GuaranteedCriticalillnessBenifit = sumassured * 0.2;
        } else {
            GuaranteedCriticalillnessBenifit = GuaranteedCriticalillnessBenifit
                    + getPercentage(policyterm, policyyear);
        }

        return GuaranteedCriticalillnessBenifit;

    }

    public double getSumAssured_CI(double sumassured) {
        SumAssured_CI = sumassured * 0.2;
        return SumAssured_CI;

    }

    public double getCriticalIllnessRate(String gender, String smoker, int age,
                                         int policyterm) {

        if (gender.equals("Male") && smoker.equals("Non-Smoker")
                || gender.equals("Third Gender") && smoker.equals("Non-Smoker")) {

            double[] PWBarr = poornasurakshaDB.getMale_NonSmoker_Rate_CI();
            int position = 0;
            int premiumPerOneLac = 0;
            for (int i = 18; i <= 65; i++) {
                for (int j = 10; j <= 30; j = j + 5) {
                    if ((age) == i && (policyterm) == j) {
                        PremiumRate_CI = PWBarr[position];
                        break;
                    }
                    position++;
                }
            }

            PremiumRate_CI = PremiumRate_CI;
            // Log.d(""+a, "tab");

        } else if (gender.equals("Male") && smoker.equals("Smoker")
                || gender.equals("Third Gender") && smoker.equals("Smoker")) {

            double[] PWBarr = poornasurakshaDB.getMale_Smoker_Rate_CI();
            int position = 0;
            int premiumPerOneLac = 0;
            for (int i = 18; i <= 65; i++) {
                for (int j = 10; j <= 30; j = j + 5) {
                    if ((age) == i && (policyterm) == j) {
                        PremiumRate_CI = PWBarr[position];
                        break;
                    }
                    position++;
                }
            }

            PremiumRate_CI = PremiumRate_CI;
            // Log.d(""+a, "tab");

        } else if (gender.equals("Female") && smoker.equals("Non-Smoker")) {

            double[] PWBarr = poornasurakshaDB.getFemale_NonSmoker_Rate_CI();
            int position = 0;
            int premiumPerOneLac = 0;
            for (int i = 18; i <= 65; i++) {
                for (int j = 10; j <= 30; j = j + 5) {
                    if ((age) == i && (policyterm) == j) {
                        PremiumRate_CI = PWBarr[position];
                        break;
                    }
                    position++;
                }
            }

            PremiumRate_CI = PremiumRate_CI;
            // Log.d(""+a, "tab");

        } else {

            double[] PWBarr = poornasurakshaDB.getFemale_Smoker_Rate_CI();
            int position = 0;
            int premiumPerOneLac = 0;
            for (int i = 18; i <= 65; i++) {
                for (int j = 10; j <= 30; j = j + 5) {
                    if ((age) == i && (policyterm) == j) {
                        PremiumRate_CI = PWBarr[position];
                        break;
                    }
                    position++;
                }
            }

            PremiumRate_CI = PremiumRate_CI;
            // Log.d(""+a, "tab");

        }

        PremiumRate_CI = PremiumRate_CI;
        PremiumRate_CI = PremiumRate_CI;
        return PremiumRate_CI;

    }

    public double getPremiumBeforeST_CI(double sumassured) {

        PremiumBeforeST_CI = ((PremiumRate_CI * (1 - SARebate)) * (1 - StaffRebate))
                * (SumAssured_CI / 1000) * LoadingFrequencyPremium;
        return PremiumBeforeST_CI;

    }

    public String getTotalPremiumWithoutST_CI() {
        TotalPremiumWithoutST_CI = cfap.getRoundOffLevel2New(""
                + PremiumBeforeST_CI);

        return TotalPremiumWithoutST_CI;
    }

	/*public String getTotalPremiumWithST_CI() {
		TotalPremiumWithST_CI = (Double.parseDouble(cfap
				.getRoundOffLevel2New(cfap.getStringWithout_E(Double
						.parseDouble(TotalPremiumWithoutST_CI)
						* poornasurakshaproperties.CGST)))
				+ Double.parseDouble(cfap.getRoundOffLevel2New(cfap
						.getStringWithout_E(Double
								.parseDouble(TotalPremiumWithoutST_CI)
								* poornasurakshaproperties.SGST))) + Double
					.parseDouble(TotalPremiumWithoutST_CI))
				+ "";

		return TotalPremiumWithST_CI;

	}*/

    public String getTotalPremiumWithST_CI() {
        TotalPremiumWithST_CI = (Double.parseDouble(cfap.getRoundOffLevel2New(cfap.getStringWithout_E(Double.parseDouble(TotalPremiumWithoutST_CI) * poornasurakshabean.getServiceTax()))) + Double.parseDouble(cfap.getRoundOffLevel2New(cfap.getStringWithout_E(Double.parseDouble(TotalPremiumWithoutST_CI) * poornasurakshaproperties.SGST))) + Double.parseDouble(TotalPremiumWithoutST_CI)) + "";

        return TotalPremiumWithST_CI;
    }


    public double getServiceTax_CI(double CGST) {
        String TotalPremiumWithoutST_CI_roundUp = cfap.getRoundUp(TotalPremiumWithoutST_CI);
        ServiceTax_CI = Double.parseDouble(cfap.getRoundUp(cfap.getStringWithout_E(Double.parseDouble(TotalPremiumWithoutST_CI_roundUp) * CGST))) + Double.parseDouble(cfap.getRoundUp(cfap.getStringWithout_E(Double.parseDouble(TotalPremiumWithoutST_CI_roundUp) * poornasurakshaproperties.SGST)));
//			 ServiceTax_CI = Double.parseDouble(TotalPremiumWithST_CI) - Double.parseDouble(TotalPremiumWithoutST_CI) ;
        return ServiceTax_CI;

    }

    // public double getServiceTax_CI()
    // {
    // ServiceTax_CI =
    // Double.parseDouble(cfap.getRound(cfap.getStringWithout_E(Double.parseDouble(TotalPremiumWithoutST_CI)
    // * poornasurakshaproperties.CGST))) +
    // Double.parseDouble(cfap.getRound(cfap.getStringWithout_E(Double.parseDouble(TotalPremiumWithoutST_CI)
    // * poornasurakshaproperties.SGST)));
    // // ServiceTax_CI = Double.parseDouble(TotalPremiumWithST_CI) -
    // Double.parseDouble(TotalPremiumWithoutST_CI) ;
    // return ServiceTax_CI;
    //
    // }

//	public double getServiceTax_CI() {
//		String TotalPremiumWithoutST_CI_roundUp = cfap
//				.getRoundUp(TotalPremiumWithoutST_CI);
//		ServiceTax_CI = Double.parseDouble(cfap.getRound(cfap
//				.getStringWithout_E(Double
//						.parseDouble(TotalPremiumWithoutST_CI_roundUp)
//						* poornasurakshaproperties.CGST)))
//				+ Double.parseDouble(cfap.getRound(cfap
//						.getStringWithout_E(Double
//								.parseDouble(TotalPremiumWithoutST_CI_roundUp)
//								* poornasurakshaproperties.SGST)));
//		// ServiceTax_CI = Double.parseDouble(TotalPremiumWithST_CI) -
//		// Double.parseDouble(TotalPremiumWithoutST_CI) ;
//		return ServiceTax_CI;
//
//	}
	 /*public double getServiceTax_CI()
     {
		String TotalPremiumWithoutST_CI_roundUp=cfap.getRoundUp(TotalPremiumWithoutST_CI);
		ServiceTax_CI = Double.parseDouble(cfap.getRoundUp(cfap.getStringWithout_E(Double.parseDouble(TotalPremiumWithoutST_CI_roundUp) * poornasurakshaproperties.CGST))) + Double.parseDouble(cfap.getRoundUp(cfap.getStringWithout_E(Double.parseDouble(TotalPremiumWithoutST_CI_roundUp) * poornasurakshaproperties.SGST)));
		//ServiceTax_CI = Double.parseDouble(TotalPremiumWithST_CI) - Double.parseDouble(TotalPremiumWithoutST_CI) ;
		return ServiceTax_CI;

     } */

    /**** Added By Priyanka Warekar - 7-12-2017 - end *****/

    // // Changes Added by Pranprit on 21/12/2017
    // public double getPremiumWithoutSTWithoutDisc(double sumassured) {
    // totInstPrem_exclST_exclDisc = ((PremiumRate * (1 - SARebate)))
    // * (sumassured / 1000) * LoadingFrequencyPremium;
    // return totInstPrem_exclST_exclDisc;
    // }
    //
    // public double getAnnualPremiumWithoutDiscWithoutFreqLoadingWithoutSAZero(
    // double sumassured) {
    // totInstPrem_exclST_exclDisc_exclFreqLoading = ((PremiumRate * (1 - 0)))
    // * (sumassured / 1000);
    // return totInstPrem_exclST_exclDisc_exclFreqLoading;
    //
    // }
    //
    public double getTotalPremiumWithoutSTWithoutDisc() {

        totalInstPrem_exclST_exclDisc = totInstPrem_exclST_exclDisc + rider_Prem_exclST_exclDisc;
        return totalInstPrem_exclST_exclDisc;
    }


    public double getPremiumWithoutSTWithoutDisc(double sumassured) {
        // double riderPrem=200.0/1000.0*(PremiumRate_CI);
        // double basePrem=PremiumRate-riderPrem;
        totInstPrem_exclST_exclDisc = (((PremiumRate - (200.0 / 1000.0 * (PremiumRate_CI)))
                * (1 - SARebate) * (1 - 0)))
                * (sumassured / 1000) * LoadingFrequencyPremium;
        return totInstPrem_exclST_exclDisc;
    }

    public double getAnnualPremiumWithoutDiscWithoutFreqLoadingWithoutSAZero(
            double sumassured) {
        totInstPrem_exclST_exclDisc_exclFreqLoading = (((PremiumRate - (200.0 / 1000.0 * (PremiumRate_CI)))
                * (1 - 0) * (1 - 0)))
                * (sumassured / 1000);
        return totInstPrem_exclST_exclDisc_exclFreqLoading;

    }

    public double getPremiumWithoutSTWithoutDisc_CritiIllness(double sumassured) {
        rider_Prem_exclST_exclDisc = ((PremiumRate_CI * (1 - SARebate)))
                * (SumAssured_CI / 1000) * LoadingFrequencyPremium;

        return rider_Prem_exclST_exclDisc;
    }

    public double getAnnualPremiumWithoutDiscWithoutFreqLoadingWithoutSAZero_CritiIllness(
            double sumassured) {
        rider_Prem_exclST_exclDisc_exclFreqLoading = ((PremiumRate_CI * (1 - 0)))
                * (SumAssured_CI / 1000);

        return rider_Prem_exclST_exclDisc_exclFreqLoading;

    }

    public double getCirtiSumAssured(double sumassured) {
        return sumassured * 0.2;
    }

    public double getSumAssured_basic_80(double sumassured) {
        SumAssured_basic_80 = sumassured * 0.8;
        return SumAssured_basic_80;
    }

    public double getMinesOccuInterest(double SumAssured_Basic) {
        return (SumAssured_Basic / 1000) * 2;
    }

    /*** Added by Priyanka Warekar - 31-08-2018 - start *******/
    public double getServiceTaxMines(double MinesOccuInterest) {
        return Double.parseDouble(cfap.getRoundUp(cfap.getRoundOffLevel2(cfap.getStringWithout_E(MinesOccuInterest * (poornasurakshabean.getServiceTax() + poornasurakshaproperties.SGST)))));
    }
/*** Added by Priyanka Warekar - 31-08-2018 - end *******/

}
