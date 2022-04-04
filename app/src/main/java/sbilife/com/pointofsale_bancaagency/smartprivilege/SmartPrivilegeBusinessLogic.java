package sbilife.com.pointofsale_bancaagency.smartprivilege;

import java.text.DecimalFormat;
import java.util.ArrayList;

import sbilife.com.pointofsale_bancaagency.common.CommonForAllProd;


class SmartPrivilegeBusinessLogic {

    private CommonForAllProd cfap = null;
    private SmartPrivilegeProperties prop = null;
    public double[] arrFundValAfterFMCBeforeGA = new double[500];
    public double[] arrFundValAfterFMCBeforeGA_AR = new double[500];
    private int count = 18;
    private int count_AR = 18;
    public String plan = "Plan-B";

    private String month_E = null;
    private String year_F = null;
    private String age_H = null;
    private String policyInforce_G = "Y";
    private String premium_I = null;
    String topUpPremium_J = null;

    private String premiumAllocationCharge_K = null;
    String topUpCharges_L = null;
    private String serviceTaxOnAllocation_M = null;
    private String amountAvailableForInvestment_N = null;
    private String amountAvailableForInvestment_N1 = null;

    String sumAssuredRelatedCharges_O = null;
    String riderCharges_P = null;
    private String policyAdministrationCharge_N = null;
    private String PPWBCharge_O = null;
    private String riderBenefit_P = null;
    private String addADBnATPDCharge_Q = null;
    private String serviceTaxExclOfSTOnAllocAndSurr_U = null;
    private String totalCharges_T = null;
    private String AccTPDCharges_S = null;
    private String mortalityCharges_R = null;
    String totalCharges_S = null;
    String serviceTaxExclOfSTOnAllocAndSurr_T = null;
    private String totalServiceTax_U = null;
    private String additionToFundIfAny_W = null;
    private String fundBeforeFMC_X = null;
    private String fundManagementCharge_X = null;
    private String serviceTaxOnFMC_Y = null;
    private String guaranteeCharge_Z = null;
    private String terminalAddition_AV = null;
    private String fundValueAfterFMCBerforeGA_Z = null;
    private String guaranteedAddition_AC = null;
    private String terminalAddition_AD = null;
    private String fundValueAtEnd_AE = null;
    private String surrenderCharges_AC = null;
    private String serviceTaxOnSurrenderCharges_AD = null;
    private String surrenderValue_AE = null;
    private String deathBenefit_AF = null;
    private String mortalityCharges_AG = null;
    private String AccTPDCharges_AK = null;
    private String totalCharges_AH = null;
    private String serviceTaxExclOfSTOnAllocAndSurr_AI = null;
    private String totalServiceTax_AJ = null;
    private String additionToFundIfAny_AK = null;
    private String fundBeforeFMC_AL = null;
    private String fundManagementCharge_AM = null;
    private String serviceTaxOnFMC_AN = null;
    private String fundValueAfterFMCBerforeGA_AO = null;
    private String guaranteedAddition_AP = null;
    private String guaranteeCharge_AR = null;
    private String fundValueAtEnd_AQ = null;
    private String surrenderCharges_AR = null;
    private String serviceTaxOnSurrenderCharges_AS = null;
    private String surrenderValue_AT = null;
    private String deathBenefit_AU = null;
    private String surrenderCap_AV = null;
    private String last2YearAvgFundValue_AB = null;
    private String last2YearAvgFundValue_AR = null;
    private String oneHundredPercentOfCummulativePremium_AW = null;

    private String reductionYield_BF = null;
    private String month_BD = null;
    private String irrAnnual_BF = null;

    String[] avgFundValArr_4Per, avgFundValArr_8Per, temp;

    public SmartPrivilegeBusinessLogic() {
        cfap = new CommonForAllProd();
        prop = new SmartPrivilegeProperties();
    }

    public void setMonth_E(int rowNumber) {
        this.month_E = ("" + rowNumber);
    }

    public String getMonth_E() {
        return month_E;
    }

    public void setYear_F() {
        this.year_F = cfap.getRoundUp(""
                + (Double.parseDouble(getMonth_E()) / 12));
    }

    public String getYear_F() {
        return year_F;
    }

    public String getPolicyInForce_G() {
        return policyInforce_G;
    }

    public void setAge_H(int ageAtEntry) {
        this.age_H = "" + (ageAtEntry + Integer.parseInt(getYear_F()) - 1);
    }

    public String getAge_H() {
        return age_H;
    }

    public void setPremium_I(int PPT, int PF, double effectivePrem) {
        if (getPolicyInForce_G().equals("Y")) {
            if ((Integer.parseInt(getYear_F()) <= PPT)
                    && (((Integer.parseInt(getMonth_E()) - 1) % (12 / PF)) == 0)) {
                premium_I = "" + (effectivePrem);
            } else {
                premium_I = ("" + 0);
            }
        } else {
            premium_I = ("" + 0);
        }
    }

    public String getPremium_I() {
        return premium_I;
    }

    public void setPremiumAllocationCharge_J(boolean staffDisc,
                                             boolean bancAssuranceDisc, String planType, int PPT,
                                             double effectivePremium) {
        // System.out.println(" allocation charge : "+getAllocationCharge(staffDisc,bancAssuranceDisc,planType,PPT,effectivePremium));
        if (Integer.parseInt(getYear_F()) > 10) {
            this.premiumAllocationCharge_K = ("" + 0);
        } else {
            this.premiumAllocationCharge_K = cfap.getRoundUp(cfap
                    .getStringWithout_E(getAllocationCharge(staffDisc,
                            bancAssuranceDisc, planType, PPT, effectivePremium)
                            * Double.parseDouble(getPremium_I())), "roundUpII");
        }
    }

    public String getPremiumAllocationCharge_J() {
        return premiumAllocationCharge_K;
    }

    public void setServiceTaxOnAllocation_K(boolean allocationCharges,
                                            double serviceTax) {
        if (allocationCharges) {
            this.serviceTaxOnAllocation_M = cfap.getRoundOffLevel2(cfap
                    .roundUp_Level4(cfap.getStringWithout_E((Double
                            .parseDouble(getPremiumAllocationCharge_J()))
                            * serviceTax)));
        } else {
            this.serviceTaxOnAllocation_M = ("" + 0);
        }
    }

    public String getServiceTaxOnAllocation_K() {
        return serviceTaxOnAllocation_M;
    }

    public void setAmountAvailableForInvestment_L() {
        this.amountAvailableForInvestment_N = cfap.roundUp_Level2("" + (Double.parseDouble(getPremium_I()) - Double.parseDouble(getPremiumAllocationCharge_J()) - Double.parseDouble(getServiceTaxOnAllocation_K())));
    }

    public String getAmountAvailableForInvestment_L() {
        return amountAvailableForInvestment_N;
    }

    public void setAmountAvailableForInvestment_L1() {
        this.amountAvailableForInvestment_N1 = cfap.roundUp_Level2("" + (Double.parseDouble(getPremium_I()) - Double.parseDouble(getPremiumAllocationCharge_J())));
    }

    public String getAmountAvailableForInvestment_L1() {
        return amountAvailableForInvestment_N1;
    }

    public double getCommisionCharge(boolean staffDisc,
                                     boolean bancAssuranceDisc, String planType, int PPT) {
        double[] regularPremium_arr = {0.025, 0.025, 0.025, 0.025, 0.025,
                0.010, 0.010, 0.010, 0.010, 0.010, 0.010, 0.010};
        double[] LPPTPremium_arr = {0.025, 0.025, 0.025, 0.025, 0.025, 0.010,
                0.010, 0.010, 0.010, 0.010, 0.010, 0.010};
        double[] SinglePremium_arr = {0.020, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                0.0, 0.0, 0.0, 0.0, 0.0};

        if (Integer.parseInt(getYear_F()) == 10)
            System.out.println("print");
        if (Integer.parseInt(getYear_F()) > PPT) {
            return 0;
        } else {/*
			if(Integer.parseInt(getYear_F())<12)
			{
				if(planType.equals("Regular"))
					return regularPremium_arr[Integer.parseInt(getYear_F())-1];
				else if(planType.equals("Limited"))
					return LPPTPremium_arr[Integer.parseInt(getYear_F())-1];
				else
					return SinglePremium_arr[Integer.parseInt(getYear_F())-1];
			}
			else
			{
				if(planType.equals("Regular"))
					return regularPremium_arr[11];
				else if(planType.equals("Limited"))
					return LPPTPremium_arr[11];
				else
					return SinglePremium_arr[11];

			}
		*/

            if (Integer.parseInt(getYear_F()) < 12) {
                if (planType.equals("Regular"))
                    return regularPremium_arr[Integer.parseInt(getYear_F()) - 1];
                else if (planType.equals("Limited"))
                    return LPPTPremium_arr[Integer.parseInt(getYear_F()) - 1];
                else
                    return SinglePremium_arr[Integer.parseInt(getYear_F()) - 1];
            } else {
                if (planType.equals("Regular"))
                    return regularPremium_arr[11];
                else if (planType.equals("Limited"))
                    return LPPTPremium_arr[11];
                else
                    return SinglePremium_arr[11];

            }
        }
    }


    public double getAllocationCharge(boolean staffDisc, boolean bancAssuranceDisc, String planType, int PPT, double effectivePremium) {/*
		double regularPremium_arr[] = { 0.025, 0.025, 0.025, 0.025, 0.025,
                0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000};
		double LPPTPremium_arr[] = { 0.025, 0.025, 0.025, 0.025, 0.025, 0.000,
                0.000, 0.000, 0.000, 0.000, 0.000, 0.000};
		double SinglePremium_arr[] = { 0.020, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                0.0, 0.0, 0.0, 0.000, 0.000};

        double temp = 0, ak = 0;

        double commissionCharge = getCommisionCharge(staffDisc,
                bancAssuranceDisc, planType, PPT);
        // System.out.println("commissionCharge "+commissionCharge);
        if (getYear_F().equals("1"))
            ak = 1;
        else
            ak = 0;
        if (staffDisc == true && bancAssuranceDisc == false) {
            if (Integer.parseInt(getYear_F()) < 12) {
                if (planType.equals("Regular"))
                    temp = regularPremium_arr[Integer.parseInt(getYear_F()) - 1]
                            - commissionCharge;
                else if (planType.equals("Limited"))
                    temp = LPPTPremium_arr[Integer.parseInt(getYear_F()) - 1]
                            - commissionCharge;
                else
					temp= SinglePremium_arr[Integer.parseInt(getYear_F())-1]-commissionCharge;
			}
			else
			{
                if (planType.equals("Regular"))
                    temp = regularPremium_arr[11] - commissionCharge;
                else if (planType.equals("Limited"))
                    temp = regularPremium_arr[11] - commissionCharge;
                else
                    temp = regularPremium_arr[11] - commissionCharge;

            }
        } else if (staffDisc == false && bancAssuranceDisc == true) {
            if (Integer.parseInt(getYear_F()) < 12) {
                if (planType.equals("Regular"))
                    temp = regularPremium_arr[Integer.parseInt(getYear_F()) - 1]
                            - commissionCharge * ak;
                else if (planType.equals("Limited"))
                    temp = LPPTPremium_arr[Integer.parseInt(getYear_F()) - 1]
                            - commissionCharge * ak;
                else
					temp= SinglePremium_arr[Integer.parseInt(getYear_F())-1]-commissionCharge*ak;
			}
			else
			{
				if (planType.equals("Regular"))
					temp = regularPremium_arr[11] - commissionCharge * ak;
				else if (planType.equals("Limited"))
					temp = regularPremium_arr[11] - commissionCharge * ak;
				else
					temp = regularPremium_arr[11] - commissionCharge * ak;

			}
		}
		else
		{
			if(Integer.parseInt(getYear_F())<12)
			{
				if(planType.equals("Regular"))
					temp= regularPremium_arr[Integer.parseInt(getYear_F())-1];
				else if(planType.equals("Limited"))
					temp= LPPTPremium_arr[Integer.parseInt(getYear_F())-1];
				else
					temp= SinglePremium_arr[Integer.parseInt(getYear_F())-1];
			}
			else
			{
				if(planType.equals("Regular"))
					temp= regularPremium_arr[11];
				else if(planType.equals("Limited"))
					temp= regularPremium_arr[11];
				else
					temp= regularPremium_arr[11];

			}
		}
//		System.out.println("temp "+temp);

		return Math.max(temp,0);
	*/

        double regularPremium_arr[] = {0.025, 0.025, 0.025, 0.025, 0.025, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000};
        double LPPTPremium_arr[] = {0.025, 0.025, 0.025, 0.025, 0.025, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000};
        double SinglePremium_arr[] = {0.020, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.000, 0.000};

        double temp = 0, ak = 0;

        double commissionCharge = getCommisionCharge(staffDisc, bancAssuranceDisc, planType, PPT);
//		System.out.println("commissionCharge "+commissionCharge);
        if (getYear_F().equals("1"))
            ak = 1;
        else
            ak = 0;
        if (staffDisc == true && bancAssuranceDisc == false) {
            if (Integer.parseInt(getYear_F()) < 12) {
                if (planType.equals("Regular"))
                    temp = regularPremium_arr[Integer.parseInt(getYear_F()) - 1] - commissionCharge;
                else if (planType.equals("Limited"))
                    temp = LPPTPremium_arr[Integer.parseInt(getYear_F()) - 1] - commissionCharge;
                else
                    temp = SinglePremium_arr[Integer.parseInt(getYear_F()) - 1] - commissionCharge;
            } else {
                if (planType.equals("Regular"))
                    temp = regularPremium_arr[11] - commissionCharge;
                else if (planType.equals("Limited"))
                    temp = regularPremium_arr[11] - commissionCharge;
                else
                    temp = regularPremium_arr[11] - commissionCharge;

            }
        } else if (staffDisc == false && bancAssuranceDisc == true) {
            if (Integer.parseInt(getYear_F()) < 12) {
                if (planType.equals("Regular"))
                    temp = regularPremium_arr[Integer.parseInt(getYear_F()) - 1] - commissionCharge * ak;
                else if (planType.equals("Limited"))
                    temp = LPPTPremium_arr[Integer.parseInt(getYear_F()) - 1] - commissionCharge * ak;
                else
                    temp = SinglePremium_arr[Integer.parseInt(getYear_F()) - 1] - commissionCharge * ak;
            } else {
                if (planType.equals("Regular"))
                    temp = regularPremium_arr[11] - commissionCharge * ak;
                else if (planType.equals("Limited"))
                    temp = regularPremium_arr[11] - commissionCharge * ak;
                else
                    temp = regularPremium_arr[11] - commissionCharge * ak;

            }
        } else {
            if (Integer.parseInt(getYear_F()) < 12) {
                if (planType.equals("Regular"))
                    temp = regularPremium_arr[Integer.parseInt(getYear_F()) - 1];
                else if (planType.equals("Limited"))
                    temp = LPPTPremium_arr[Integer.parseInt(getYear_F()) - 1];
                else
                    temp = SinglePremium_arr[Integer.parseInt(getYear_F()) - 1];
            } else {
                if (planType.equals("Regular"))
                    temp = regularPremium_arr[11];
                else if (planType.equals("Limited"))
                    temp = regularPremium_arr[11];
                else
                    temp = regularPremium_arr[11];

            }
        }
        // System.out.println("temp "+temp);

        return Math.max(temp, 0);
    }

    public void setPolicyAdministrationCharge_N() {

        if (getPolicyInForce_G().equals("Y")) {
            if (Integer.parseInt(getYear_F()) == 0) {
                this.policyAdministrationCharge_N = "" + 0;
            } else {
                this.policyAdministrationCharge_N = "" + 0;
            }// Since database values are zero in posi excel.

        } else {
            this.policyAdministrationCharge_N = "" + 0;
        }

    }

    public String getPolicyAdministrationCharge_N() {
        return policyAdministrationCharge_N;
    }

    public void setPPWBCharge_O(boolean mortalityAndRiderCharges, int PPT,
                                int PF, double annualPremium) {
        double ppwbCharge = 0;
        double a = 0;
        if (mortalityAndRiderCharges == true)
            a = 1;
        else
            a = 0;

        double temp = 0;

        if (Integer.parseInt(getYear_F()) != 0
                && Integer.parseInt(getMonth_E()) <= (PPT * 12 - 12 / PF))
            temp = ppwbCharge / 12 * annualPremium / 1000;
        else
            temp = 0;

        this.PPWBCharge_O = (temp * a) + "";
        // System.out.println("PPWBCharge_O + "+PPWBCharge_O);

    }

    public String getPPWBCharge_O() {
        return PPWBCharge_O;
    }

    public void setRiderBenefitCharges_P() {

        if (getPolicyInForce_G().equals("Y")) {
            if (Integer.parseInt(getYear_F()) == 0) {
                this.riderBenefit_P = "" + 0;
            } else {
                this.riderBenefit_P = "" + 0;
            }// Since database values are zero in posi excel.

        } else {
            this.riderBenefit_P = "" + 0;
        }

    }

    public String getRiderBenefitCharges_P() {
        return riderBenefit_P;
    }

    public void setAdd_AdbNAtpdCharges_Q(boolean mortalityCharges,
                                         double sumAssured) {
        double adbNatpdCharge = 0;
        double a = 0;
        if (mortalityCharges == true)
            a = 1;
        else
            a = 0;
        if (getPolicyInForce_G().equals("Y")) {
            if (Integer.parseInt(getYear_F()) == 0) {
                this.addADBnATPDCharge_Q = "" + 0;
            } else {
                this.addADBnATPDCharge_Q = "" + Math.min(sumAssured, 5000000)
                        * adbNatpdCharge * a;
            }// Since database values are zero in posi excel.

        } else {
            this.addADBnATPDCharge_Q = "" + 0;
        }

    }

    public String getAdd_AdbNAtpdCharges_Q() {
        return addADBnATPDCharge_Q;
    }

    public String[] getForBIArr(double mortalityCharges_AsPercentOfofLICtable) {
        int[] ageArr = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100};
        String[] strArrTReturn = new String[99];
        SmartPrivilegeDB dbObj = new SmartPrivilegeDB();

//        System.out.println("length "+smartWealthBuilderDB.getIAIarray().length);
        for (int i = 0; i < dbObj.getIAIarray().length - 2; i++) {
//            strArrTReturn[i]=String.valueOf(((smartWealthBuilderDB.getIAIarray()[i]+smartWealthBuilderDB.getIAIarray()[i+1])/2) * mortalityCharges_AsPercentOfofLICtable);
            strArrTReturn[i] = String.valueOf((dbObj.getIAIarray()[i]) * mortalityCharges_AsPercentOfofLICtable);

//            if(ageArr[i]<7)
//            {strArrTReturn[i]="0";}
//            else
//            {
//                //double a=(((smartWealthBuilderDB.getIAIarray()[i]+smartWealthBuilderDB.getIAIarray()[i+1])/2) * mortalityCharges_AsPercentOfofLICtable);
//                //double b=(double)1/12;
//                //double c=(1-cfap.pow((1-a*mortalityCharges_AsPercentOfofLICtable),b));
//                //strArrTReturn[i]=cfap.roundUp_Level2(cfap.getStringWithout_E(c));
//                strArrTReturn[i]=cfap.roundUp_Level2(cfap.getStringWithout_E(((smartWealthBuilderDB.getIAIarray()[i]+smartWealthBuilderDB.getIAIarray()[i+1])/2) * mortalityCharges_AsPercentOfofLICtable * 1000  ));
//            }

        }
        return strArrTReturn;
    }

	/*public void setMortalityCharges_R(double _fundValueAtEnd_AB,int policyTerm,double sumAssured,boolean mortalityCharges)
	{
		if(!(getPolicyInForce_G().equals("Y"))   ||   Integer.parseInt(getYear_F()) > policyTerm)
		{this.mortalityCharges_R=""+0;}
		else
		{
            SmartPrivilegeDB dbObj = new SmartPrivilegeDB();
            double[] mortalityArr = dbObj.getMortalityChargeArray();
            double arrOutput = mortalityArr[Integer.parseInt(getAge_H())];
            // System.out.println("arroutput "+arrOutput);
            // double div=arrOutput/1000;
            // System.out.println("div "+div);
            double a = arrOutput / 12;
            // System.out.println("a "+a);

//			System.out.println(getAmountAvailableForInvestment_L() + "  "+_fundValueAtEnd_AB);
            double max1 = Math
                    .max(sumAssured,
                            Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()));
            // System.out.println("max1 "+max1);
			double b=(max1-(Double.parseDouble(getAmountAvailableForInvestment_L())+_fundValueAtEnd_AB));
			// System.out.println("b "+b);

			double b=(max1-(Double.parseDouble(getAmountAvailableForInvestment_L())+_fundValueAtEnd_AB)-Double.parseDouble(getServiceTaxOnAllocation_K()));
			int c = 0;
			if(mortalityCharges)
			{c=1;}
			else
			{c=0;}
			this.mortalityCharges_R = cfap.roundUp_Level2(cfap
					.getStringWithout_E(((a * Math.max(b, 0)) * c)));
		}
	}*/

    public void setMortalityCharges_R(double _fundValueAtEnd_AB, int policyTerm, String[] forBIArray, double sumAssured, boolean mortalityCharges) {
        if (!(getPolicyInForce_G().equals("Y")) || Integer.parseInt(getYear_F()) > policyTerm) {
            this.mortalityCharges_R = "" + 0;
        } else {
            double arrOutput = Double.parseDouble(forBIArray[Integer.parseInt(getAge_H())]);


//	            System.out.println("**********arroutput "+arrOutput);
            //double div=arrOutput/1000;
            //System.out.println("div "+div);
//	            double a=arrOutput/12;
            //System.out.println("a "+a);

            DecimalFormat df = new DecimalFormat("#.#######");

            String roundTo7 = df.format(arrOutput);
            String test = cfap.getRoundOffLevel5(roundTo7);

//	            System.out.println("arroutput RY "+arrOutput);
//	            System.out.println("round to 5 RY"+test);

            double a = Double.parseDouble(test) / 12;
            double max1 = Math.max(sumAssured, Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()));

//	            System.out.println("max1 "+max1);

            double b = (max1 - (Double.parseDouble(getAmountAvailableForInvestment_L()) + _fundValueAtEnd_AB));


//	            System.out.println("Minus amount" + (Double.parseDouble(getAmountAvailableForInvestment_N())+_fundValueAtEnd_AB));
//	            System.out.println("N" + getAmountAvailableForInvestment_N());
//	            System.out.println("AB" + _fundValueAtEnd_AB);

            // System.out.println("b "+b);

            int c = 0;
            if (mortalityCharges) {
                c = 1;
            } else {
                c = 0;
            }
//	            this.mortalityCharges_R= cfap.roundUp_Level2(cfap.getStringWithout_E(((a*Math.max(b, 0))*c)));
            this.mortalityCharges_R = cfap.getRoundOffLevel2New(cfap.getStringWithout_E(((a * Math.max(b, 0)) * c)));
        }
    }

    public String getMortalityCharges_R() {
        return mortalityCharges_R;
    }

    public void setOneHundredPercentOfCummulativePremium_AW(
            double _oneHundredPercentOfCummulativePremium_AW) {
        double a = 0;
        if (getPolicyInForce_G().equals("Y")) {
            a = Double.parseDouble(getPremium_I())
                    + (_oneHundredPercentOfCummulativePremium_AW / 1.05);
        } else {
            a = 0;
        }
        this.oneHundredPercentOfCummulativePremium_AW = "" + (a * 1.05);
    }

    public String getOneHundredPercentOfCummulativePremium_AW() {
        return oneHundredPercentOfCummulativePremium_AW;
    }

    public void setAccTPDCharges_S(double _fundValueAtEnd_AB, int policyTerm,
                                   double sumAssured, boolean mortalityCharges) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            double accTPDCharge = 0;
            double a = accTPDCharge / 12000;
            // System.out.println("a "+a);
            double max1 = Math
                    .max(sumAssured,
                            Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()));
            // System.out.println("max1 "+max1);
            double b = (max1 - (Double
                    .parseDouble(getAmountAvailableForInvestment_L()) + _fundValueAtEnd_AB));
            // System.out.println("b "+b);

            int c = 0;
            if (mortalityCharges) {
                c = 1;
            } else {
                c = 0;
            }
            this.AccTPDCharges_S = cfap.roundUp_Level2(cfap
                    .getStringWithout_E(((a * Math.max(b, 0)) * c)));
        } else {
            this.AccTPDCharges_S = "" + 0;
        }
    }

    public String getAccTPDCharges_S() {
        return AccTPDCharges_S;
    }

    public void setTotalCharges_T(int policyTerm, double riderCharges) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.totalCharges_T = ""
                    + (Double.parseDouble(getPolicyAdministrationCharge_N())
                    + Double.parseDouble(getPPWBCharge_O())
                    + Double.parseDouble(getRiderBenefitCharges_P())
                    + Double.parseDouble(getAdd_AdbNAtpdCharges_Q())
                    + Double.parseDouble(getMortalityCharges_R()) + Double
                    .parseDouble(getAccTPDCharges_S()));
        } else {
            this.totalCharges_T = "" + 0;
        }
    }

    public String getTotalCharges_T() {
        return totalCharges_T;
    }

    public void setServiceTax_exclOfSTonAllocAndSurr_U(double serviceTax,
                                                       boolean mortalityAndRiderCharges, boolean administrationCharges) {
        double a = 0, b = 0;
        if (mortalityAndRiderCharges) {
            a = +Double.parseDouble(getPPWBCharge_O())
                    + Double.parseDouble(getRiderBenefitCharges_P())
                    + Double.parseDouble(getAdd_AdbNAtpdCharges_Q())
                    + Double.parseDouble(getMortalityCharges_R())
                    + Double.parseDouble(getAccTPDCharges_S());
        } else {
            a = 0;
        }
        if (administrationCharges) {
            b = Double.parseDouble(getPolicyAdministrationCharge_N());
        } else {
            b = 0;
        }
        this.serviceTaxExclOfSTOnAllocAndSurr_U = cfap.getRoundOffLevel2(cfap
                .roundUp_Level4(cfap.getStringWithout_E((a + b) * serviceTax)));
    }

    public String getServiceTax_exclOfSTonAllocAndSurr_U() {
        return serviceTaxExclOfSTOnAllocAndSurr_U;
    }

    public void setAdditionToFundIfAny_W(double _fundValueAtEnd_AB,
                                         int policyTerm, double riderCharges) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            double temp1 = 0, temp2 = 0;
            temp1 = (_fundValueAtEnd_AB
                    + Double.parseDouble(getAmountAvailableForInvestment_L())
                    - Double.parseDouble(getTotalCharges_T()) - Double
                    .parseDouble(getServiceTax_exclOfSTonAllocAndSurr_U()));
            double a = (double) 1 / 12;
            temp2 = (cfap.pow((1 + prop.int1), a)) - 1;
            this.additionToFundIfAny_W = cfap.roundUp_Level2(cfap
                    .getStringWithout_E(temp1 * temp2));
        } else {
            this.additionToFundIfAny_W = "" + 0;
        }
    }

    public String getAdditionToFundIfAny_W() {
        return additionToFundIfAny_W;
    }

    public void setFundBeforeFMC_X(double _fundValueAtEnd_AB, int policyTerm,
                                   double riderCharges) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.fundBeforeFMC_X = cfap
                    .roundUp_Level2(cfap.getStringWithout_E(_fundValueAtEnd_AB
                            + Double.parseDouble(getAmountAvailableForInvestment_L())
                            + Double.parseDouble(getAdditionToFundIfAny_W())
                            - Double.parseDouble(getTotalCharges_T())
                            - Double.parseDouble(getServiceTax_exclOfSTonAllocAndSurr_U())));
        } else {
            this.fundBeforeFMC_X = "" + 0;
        }
    }

    public String getFundBeforeFMC_X() {
        return fundBeforeFMC_X;
    }

    //added by sujata on 02-01-2020
    public double getCharge_fund_ren(double percentToBeInvested_EquityFund, double percentToBeInvested_EquityOptFund, double percentToBeInvested_GrowthFund, double percentToBeInvested_BalancedFund, double percentToBeInvested_BondFund, double percentToBeInvested_PureFund, double percentToBeInvested_Top300Fund, double percentToBeInvested_MidCapFund, double percentToBeInvested_BondOptimiserFundII, double percentToBeInvested_MoneyMarketFundII, double percentToBeInvested_CorpBondFund) {
		/*if(plan.equalsIgnoreCase("Plan-B"))
		{*/
        return percentToBeInvested_EquityFund * prop.FMC_EquityFund +
                percentToBeInvested_EquityOptFund * prop.FMC_EquityOptimiserFund +
                percentToBeInvested_GrowthFund * prop.FMC_GrowthFund +
                percentToBeInvested_BalancedFund * prop.FMC_BalancedFund +
                percentToBeInvested_BondFund * prop.FMC_BondFund +
                percentToBeInvested_PureFund * prop.FMC_PureFund +
                percentToBeInvested_MidCapFund * prop.FMC_MidcapFund +
                percentToBeInvested_Top300Fund * prop.FMC_Top300Fund +
                percentToBeInvested_BondOptimiserFundII * prop.FMC_BondOptimiserFund +
                percentToBeInvested_MoneyMarketFundII * prop.FMC_MoneyMarketFund +
                percentToBeInvested_CorpBondFund * prop.FMC_CorpBondFund;
        //}
		/*else
		{
		return  percentToBeInvested_EquityFund*prop.FMC_EquityFund+
				percentToBeInvested_EquityOptFund*prop.FMC_EquityOptimiserFund+
				percentToBeInvested_GrowthFund*prop.FMC_GrowthFund+
				percentToBeInvested_BalancedFund*prop.FMC_BalancedFund+
				percentToBeInvested_BondFund*prop.FMC_BondFund+
				percentToBeInvested_PureFund*prop.FMC_PureFund+
				percentToBeInvested_MidCapFund*prop.FMC_MidcapFund+
				percentToBeInvested_Top300Fund*prop.FMC_Top300Fund+
				percentToBeInvested_BondOptimiserFundII*prop.FMC_BondOptimiserFundII+
				percentToBeInvested_MoneyMarketFundII*prop.FMC_MoneyMarketFundII+
				percentToBeInvested_CorpBondFund*prop.FMC_CorpBondFund;
	}
*/

    }

    public void setFundManagementCharge_Y(int policyTerm, double percentToBeInvested_EquityFund, double percentToBeInvested_EquityOptFund, double percentToBeInvested_GrowthFund, double percentToBeInvested_BalancedFund, double percentToBeInvested_BondFund, double percentToBeInvested_PureFund, double percentToBeInvested_Top300Fund, double percentToBeInvested_MidCapFund, double percentToBeInvested_BondOptimiserFundII, double percentToBeInvested_MoneyMarketFundII, double percentToBeInvested_CorpBondFund) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            // if(Integer.parseInt(getMonth_E())==1)
            //	            {this.fundManagementCharge_X=cfap.roundUp_Level2(cfap.getStringWithout_E(Double.parseDouble(getFundBeforeFMC_X()) * (prop.charge_Fund +(getCharge_fund_ren( percentToBeInvested_EquityFund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_BondFund, percentToBeInvested_PureFund, percentToBeInvested_Top300Fund,percentToBeInvested_MidCapFund)/12))));}
            // else
            {
                this.fundManagementCharge_X = cfap.roundUp_Level2(cfap.getStringWithout_E(Double.parseDouble(getFundBeforeFMC_X()) * (getCharge_fund_ren(percentToBeInvested_EquityFund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_BondFund, percentToBeInvested_PureFund, percentToBeInvested_Top300Fund, percentToBeInvested_MidCapFund, percentToBeInvested_BondOptimiserFundII, percentToBeInvested_MoneyMarketFundII, percentToBeInvested_CorpBondFund) / 12)));
            }
        } else {
            this.fundManagementCharge_X = "" + 0;
        }
    }

    public String getFundManagementCharge_Y() {
        return fundManagementCharge_X;
    }

    public void setGuaranteeCharge_Z(int policyTerm) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.guaranteeCharge_Z = cfap.roundUp_Level2(cfap
                    .getStringWithout_E((Double
                            .parseDouble(getFundBeforeFMC_X())
                            * prop.guarantee_charge / 12)));
        } else {
            this.guaranteeCharge_Z = "" + 0;
        }
    }

    public String getGuaranteeCharge_Z() {
        return guaranteeCharge_Z;
    }


    public void setServiceTaxOnFMC_AA(boolean fundManagementCharges, double percentToBeInvested_EquityFund, double percentToBeInvested_EquityOptFund, double percentToBeInvested_GrowthFund, double percentToBeInvested_BalancedFund, double percentToBeInvested_BondFund, double percentToBeInvested_PureFund, double percentToBeInvested_Top300Fund, double percentToBeInvested_MidCapFund, double serviceTax, double percentToBeInvested_BondOptimiserFundII, double percentToBeInvested_MoneyMarketFundII, double percentToBeInvested_CorpBondFund) {
        double a = 0, b = 0, c = 0;
//		b=Double.parseDouble(getGuaranteeCharge_Z())+Double.parseDouble(cfap.getRoundOffLevel2(cfap.getStringWithout_E((Double.parseDouble(getFundManagementCharge_Y()) * (0.0135/getCharge_fund_ren( percentToBeInvested_EquityFund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_BondFund, percentToBeInvested_PureFund, percentToBeInvested_Top300Fund,percentToBeInvested_MidCapFund))))));
        b = Double.parseDouble(getGuaranteeCharge_Z()) + Double.parseDouble(cfap.getRoundOffLevel2(cfap.getStringWithout_E((Double.parseDouble(getFundManagementCharge_Y()) * getCharge_fund_ren(percentToBeInvested_EquityFund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_BondFund, percentToBeInvested_PureFund, percentToBeInvested_Top300Fund, percentToBeInvested_MidCapFund, percentToBeInvested_BondOptimiserFundII, percentToBeInvested_MoneyMarketFundII, percentToBeInvested_CorpBondFund) / getCharge_fund_ren(percentToBeInvested_EquityFund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_BondFund, percentToBeInvested_PureFund, percentToBeInvested_Top300Fund, percentToBeInvested_MidCapFund, percentToBeInvested_BondOptimiserFundII, percentToBeInvested_MoneyMarketFundII, percentToBeInvested_CorpBondFund)))));
        if (fundManagementCharges) {
            a = 1;
        } else {
            a = 0;
        }

        c = a * serviceTax * b;
//		System.out.println(a+"  "+b+"  "+(a*b* prop.serviceTax)+"   "+c+"   "+cfap.getRoundOffLevel2(cfap.roundUp_Level4(cfap.getStringWithout_E(c)))+"  "+(prop.serviceTax*a*b)+"  "+(prop.serviceTax*1.0*b));
        this.serviceTaxOnFMC_Y = cfap.getRoundOffLevel2(cfap
                .roundUp_Level4(cfap.getStringWithout_E(c)));
    }

    public String getServiceTaxOnFMC_AA() {
        return serviceTaxOnFMC_Y;
    }

    public void setFundValueAfterFMCAndBeforeGA_AB(int policyTerm) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.fundValueAfterFMCBerforeGA_Z = ""
                    + cfap.roundUp_Level2(cfap.getStringWithout_E((Double
                    .parseDouble(getFundBeforeFMC_X())
                    - Double.parseDouble(getFundManagementCharge_Y())
                    - Double.parseDouble(getGuaranteeCharge_Z()) - Double
                    .parseDouble(getServiceTaxOnFMC_AA()))));
        } else {
            this.fundValueAfterFMCBerforeGA_Z = "" + 0;
        }
    }

    public String getFundValueAfterFMCAndBeforeGA_AB() {
        return fundValueAfterFMCBerforeGA_Z;
    }

    public void setTotalServiceTax_T(double riderCharges, double serviceTax) {
        double temp1 = 0, temp2 = 0;
        temp1 = Double.parseDouble(getServiceTaxOnAllocation_K())
                + Double.parseDouble(getServiceTax_exclOfSTonAllocAndSurr_U())
                + Double.parseDouble(getServiceTaxOnFMC_AA());
        if (prop.riderCharges) {
            temp2 = 0;
        } else {
            temp2 = 0;
        }
        this.totalServiceTax_U = cfap.getRoundOffLevel2(cfap
                .getStringWithout_E(temp1 + temp2 * serviceTax));
    }

    public String getTotalServiceTax_T() {
        return totalServiceTax_U;
    }

    public void setGuaranteedAddition_AC(String planType, double annualPremium,
                                         int PPT) {

        double[] guaranteeCharge_arr = {0, 0, 0, 0, 0.000, 0.010, 0.000,
                0.000, 0.000, 0.025, 0.000, 0.000, 0.000, 0.000, 0.035, 0.000,
                0.000, 0.000, 0.000, 0.050, 0.000, 0.000, 0.000, 0.000, 0.060,
                0.000, 0.000, 0.000, 0.000, 0.070};
        double guaranteedCharge = 0, a = 0;

        if ((Integer.parseInt(getMonth_E()) % 12) == 0)
            guaranteedCharge = guaranteeCharge_arr[Integer
                    .parseInt(getYear_F()) - 1];
        else
            guaranteedCharge = 0;
        if (Integer.parseInt(getMonth_E()) == 61)
            // System.out.println("guaranteedCharge  : "+guaranteedCharge+
            // "   "+getLast2YearAvgFundValue_AB());

            if ((Integer.parseInt(getMonth_E()) % 12) == 0)
                a = 1;
            else
                a = 0;
        // System.out.println(((1-getLoyaltyCharge())*a*annualPremium*guaranteedCharge));
        // System.out.println((getLoyaltyCharge()*guaranteedCharge*Double.parseDouble(getLast2YearAvgFundValue_AB())));

        if (Integer.parseInt(getMonth_E()) <= 24) {
            this.guaranteedAddition_AC = cfap.roundUp_Level2(cfap
                    .getStringWithout_E((1 - getLoyaltyCharge()) * a
                            * annualPremium * guaranteedCharge));
        } else {
            this.guaranteedAddition_AC = cfap
                    .roundUp_Level2(cfap.getStringWithout_E((1 - getLoyaltyCharge())
                            * a
                            * annualPremium
                            * guaranteedCharge
                            + getLoyaltyCharge()
                            * guaranteedCharge
                            * Double.parseDouble(getLast2YearAvgFundValue_AB())));
        }
    }

    public String getGuaranteedAddition_AC() {
        return guaranteedAddition_AC;
    }

    public void setGuaranteedAdditionYield_AC(double guaranteedAddition) {

        if (guaranteedAddition != 0) {
            this.guaranteedAddition_AC = cfap.roundUp_Level2(cfap
                    .getStringWithout_E(guaranteedAddition));
        } else {
            this.guaranteedAddition_AC = 0 + "";
        }
    }

    public String getGuaranteedAdditionYield_AC() {
        return guaranteedAddition_AC;
    }

    // public void setGuaranteedAdditionYield_AU(double guaranteedAddition)
    // {
    // if(guaranteedAddition!=0)
    // {this.guaranteedAddition_AP
    // =cfap.roundUp_Level2(cfap.getStringWithout_E(guaranteedAddition));}
    // else
    // {this.guaranteedAddition_AP =0+"";}
    // }
    // public String getGuaranteedAdditionYield_AU()
    // {return guaranteedAddition_AP;}

    private double getLoyaltyCharge() {
        // if(getYear_F().equals("1"))
        return 1;
        // else
        // return 0;
    }

    public void setTerminalAddition_AD(String planType, int PPT, int policyTerm) {

        double terminalCharge = 0, a = 0;
        terminalCharge = 0;

        if (Integer.parseInt(getMonth_E()) == (policyTerm * 12))
            a = (Double.parseDouble(getFundValueAfterFMCAndBeforeGA_AB()) + Double
                    .parseDouble(getGuaranteedAddition_AC())) * terminalCharge;
        else
            a = 0;
        if (getPolicyInForce_G().equals("N")) {
            this.terminalAddition_AD = "" + 0;
        } else {
            this.terminalAddition_AD = cfap.roundUp_Level2(cfap
                    .getStringWithout_E(a));
        }
    }

    public String getTerminalAddition_AD() {
        return terminalAddition_AD;
    }

    public void setFundValueAtEnd_AE() {
        this.fundValueAtEnd_AE = ""
                + (Double.parseDouble(getGuaranteedAddition_AC())
                + Double.parseDouble(getTerminalAddition_AD()) + Double
                .parseDouble(getFundValueAfterFMCAndBeforeGA_AB()));
    }

    public String getFundValueAtEnd_AE() {
        return fundValueAtEnd_AE;
    }

    private String[] calSurrRateArrRP(double effectivePremium) {
        if (effectivePremium <= 25000) {
            return new String[]{"0.20", "0.15", "0.10", "0.05", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"};
        } else {
            return new String[]{"0.06", "0.04", "0.03", "0.02", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"};
        }
    }

    private String[] calSurrRateArrSP() {
        return new String[]{"0.01", "0.005", "0.003", "0.001", "0", "0", "0",
                "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                "0", "0", "0", "0", "0", "0", "0"};
    }

    private String[] calCapArr(double effectivePremium) {
        if (effectivePremium <= 25000) {
            return new String[]{"3000", "2000", "1500", "1000", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0"};
        } else {
            return new String[]{"6000", "5000", "4000", "2000", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0", "0", "0", "0"};
        }
    }


    public void setSurrenderCap_AV(double effectivePremium, String planType) {
		/*if(getPolicyInForce_G().equals("Y"))
        // double a=Math.min(Integer.parseInt(getYear_F())-1, 11);
		{this.surrenderCap_AV =""+(Double.parseDouble(calCapArr(annualPremium)[Math.min((Integer.parseInt(getYear_F())-1),11)]));}
		else
		{this.surrenderCap_AV =""+0;}*/

        double a = 0;
        if (planType.equals("Single")) {
            if (effectivePremium > 25000) {
                if (getYear_F().equals("1")) {
                    a = 6000;
                } else if (getYear_F().equals("2")) {
                    a = 5000;
                } else if (getYear_F().equals("3")) {
                    a = 4000;
                } else if (getYear_F().equals("4")) {
                    a = 2000;
                } else {
                    a = 0;
                }
            } else {
                if (getYear_F().equals("1")) {
                    a = 3000;
                } else if (getYear_F().equals("2")) {
                    a = 2000;
                } else if (getYear_F().equals("3")) {
                    a = 1500;
                } else if (getYear_F().equals("4")) {
                    a = 1000;
                } else {
                    a = 0;
                }
            }
        } else {
            if (effectivePremium > 25000) {

                if (getYear_F().equals("1")) {
                    a = 6000;
                } else if (getYear_F().equals("2")) {
                    a = 5000;
                } else if (getYear_F().equals("3")) {
                    a = 4000;
                } else if (getYear_F().equals("4")) {
                    a = 2000;
                } else {
                    a = 0;
                }
            } else {
                if (getYear_F().equals("1")) {
                    a = 3000;
                } else if (getYear_F().equals("2")) {
                    a = 2000;
                } else if (getYear_F().equals("3")) {
                    a = 1500;
                } else if (getYear_F().equals("4")) {
                    a = 1000;
                } else {
                    a = 0;
                }
            }
        }

        this.surrenderCap_AV = "" + a;
    }

    public String getSurrenderCap_AV() {
        return surrenderCap_AV;
    }

    public void setSurrenderCharges_AF(double annualPremium, int PPT, String planType) {
        double a = Math.min(Double.parseDouble(getFundValueAtEnd_AE()), (double) annualPremium);
        // System.out.println("a "+a);
        double b = getSurrenderCharge(PPT, annualPremium, planType);
        // System.out.println("b "+b);
        // System.out.println("a*b "+(a*b));
        this.surrenderCharges_AC = cfap.roundUp_Level2(cfap
                .getStringWithout_E(Math.min((a * b),
                        Double.parseDouble(getSurrenderCap_AV()))));
    }

    public String getSurrenderCharges_AF() {
        return surrenderCharges_AC;
    }

    public double getSurrenderCharge(int PPT, double effectivePremium, String planType) {
        double surrenderCharge = 0;
		/*if(PPT==1)
		{
            // surrenderCharge=1;
			surrenderCharge=Double.parseDouble(calSurrRateArrSP()[Math.min((Integer.parseInt(getYear_F())-1), 11)]);
		}
		else
		{surrenderCharge=Double.parseDouble(calSurrRateArrRP(effectivePremium)[Math.min((Integer.parseInt(getYear_F())-1),11)]);}*/

        if (planType.equals("Single")) {
            if (effectivePremium > 25000) {
                if (getYear_F().equals("1")) {
                    surrenderCharge = 0.01;
                } else if (getYear_F().equals("2")) {
                    surrenderCharge = 0.007;
                } else if (getYear_F().equals("3")) {
                    surrenderCharge = 0.005;
                } else if (getYear_F().equals("4")) {
                    surrenderCharge = 0.003;
                } else {
                    surrenderCharge = 0;
                }
            } else {
                if (getYear_F().equals("1")) {
                    surrenderCharge = 0.02;
                } else if (getYear_F().equals("2")) {
                    surrenderCharge = 0.015;
                } else if (getYear_F().equals("3")) {
                    surrenderCharge = 0.01;
                } else if (getYear_F().equals("4")) {
                    surrenderCharge = 0.005;
                } else {
                    surrenderCharge = 0;
                }
            }
        } else {
            if (effectivePremium > 25000) {

                if (getYear_F().equals("1")) {
                    surrenderCharge = 0.06;
                } else if (getYear_F().equals("2")) {
                    surrenderCharge = 0.04;
                } else if (getYear_F().equals("3")) {
                    surrenderCharge = 0.03;
                } else if (getYear_F().equals("4")) {
                    surrenderCharge = 0.02;
                } else {
                    surrenderCharge = 0;
                }
            } else {
                if (getYear_F().equals("1")) {
                    surrenderCharge = 0.2;
                } else if (getYear_F().equals("2")) {
                    surrenderCharge = 0.15;
                } else if (getYear_F().equals("3")) {
                    surrenderCharge = 0.1;
                } else if (getYear_F().equals("4")) {
                    surrenderCharge = 0.05;
                } else {
                    surrenderCharge = 0;
                }
            }
        }

        return surrenderCharge;
    }

    public void setServiceTaxOnSurrenderCharges_AG(boolean surrenderCharges, double serviceTax) {
        if (surrenderCharges) {
            this.serviceTaxOnSurrenderCharges_AD = cfap.getRoundOffLevel2(cfap
                    .roundUp_Level4(cfap.getStringWithout_E(Double
                            .parseDouble(getSurrenderCharges_AF())
                            * serviceTax)));
        } else {
            this.serviceTaxOnSurrenderCharges_AD = "" + 0;
        }
    }

    public String getServiceTaxOnSurrenderCharges_AG() {
        return serviceTaxOnSurrenderCharges_AD;
    }

    public void setSurrenderValue_AH() {
        this.surrenderValue_AE = ""
                + (Double.parseDouble(getFundValueAtEnd_AE())
                - Double.parseDouble(getSurrenderCharges_AF()) - Double
                .parseDouble(getServiceTaxOnSurrenderCharges_AG()));
    }

    public String getSurrenderValue_AH() {
        return surrenderValue_AE;
    }

    public void setDeathBenefit_AI(int policyTerm, double sumAssured) {
        if (Integer.parseInt(getYear_F()) > policyTerm) {
            this.deathBenefit_AF = "" + 0;
        } else {
            this.deathBenefit_AF = ""
                    + (Math.max(
                    Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()),
                    Math.max(sumAssured,
                            Double.parseDouble(getFundValueAtEnd_AE()))));
        }
    }

    public String getDeathBenefit_AI() {
        return deathBenefit_AF;
    }

	/*public void setMortalityCharges_AJ(double fundValueAtEnd_AQ,int policyTerm,double sumAssured,boolean mortalityCharges)
	{
		if(!(getPolicyInForce_G().equals("Y"))   ||   Integer.parseInt(getYear_F()) > policyTerm)
		{this.mortalityCharges_AG=""+0;}
		else
		{
            SmartPrivilegeDB dbObj = new SmartPrivilegeDB();
            double[] mortalityArr = dbObj.getMortalityChargeArray();
            double arrOutput = mortalityArr[Integer.parseInt(getAge_H())];
            // System.out.println("arroutput "+arrOutput);
            // double div=arrOutput/1000;
            // System.out.println("div "+div);
            double a = arrOutput / 12;
            // System.out.println("a "+a);

//			System.out.println(getAmountAvailableForInvestment_L() + "  "+fundValueAtEnd_AQ);
            double max1 = Math
                    .max(sumAssured,
                            Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()));
            // System.out.println("max1 "+max1);
            double b = (max1 - (Double
                    .parseDouble(getAmountAvailableForInvestment_L()) + fundValueAtEnd_AQ));
            // System.out.println("b "+b);

			int c = 0;
			if(mortalityCharges)
			{c=1;}
			else
			{c=0;}
			this.mortalityCharges_AG= cfap.roundUp_Level2(cfap.getStringWithout_E(((a*Math.max(b, 0))*c)));
		}
	}*/

    public void setMortalityCharges_AJ(double _fundValueAtEnd_AB, int policyTerm, String[] forBIArray, double sumAssured, boolean mortalityCharges) {
        if (!(getPolicyInForce_G().equals("Y")) || Integer.parseInt(getYear_F()) > policyTerm) {
            this.mortalityCharges_AG = "" + 0;
        } else {
            double arrOutput = Double.parseDouble(forBIArray[Integer.parseInt(getAge_H())]);


//            System.out.println("**********arroutput "+arrOutput);
            //double div=arrOutput/1000;
            //System.out.println("div "+div);
//            double a=arrOutput/12;
            //System.out.println("a "+a);

            DecimalFormat df = new DecimalFormat("#.#######");

            String roundTo7 = df.format(arrOutput);
            String test = cfap.getRoundOffLevel5(roundTo7);

//            System.out.println("arroutput RY "+arrOutput);
//            System.out.println("round to 5 RY"+test);

            double a = Double.parseDouble(test) / 12;
            double max1 = Math.max(sumAssured, Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()));

//            System.out.println("max1 "+max1);

            double b = (max1 - (Double.parseDouble(getAmountAvailableForInvestment_L()) + _fundValueAtEnd_AB));


//            System.out.println("Minus amount" + (Double.parseDouble(getAmountAvailableForInvestment_N())+_fundValueAtEnd_AB));
//            System.out.println("N" + getAmountAvailableForInvestment_N());
//            System.out.println("AB" + _fundValueAtEnd_AB);

//            System.out.println("b "+b);

            int c = 0;
            if (mortalityCharges) {
                c = 1;
            } else {
                c = 0;
            }
//            this.mortalityCharges_R= cfap.roundUp_Level2(cfap.getStringWithout_E(((a*Math.max(b, 0))*c)));
            this.mortalityCharges_AG = cfap.getRoundOffLevel2New(cfap.getStringWithout_E(((a * Math.max(b, 0)) * c)));
        }
    }

    public String getMortalityCharges_AJ() {
        return mortalityCharges_AG;
    }

    public void setAccTPDCharges_AK(double fundValueAtEnd_AQ, int policyTerm,
                                    double sumAssured, boolean mortalityCharges) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            double accTPDCharge = 0;
            double a = accTPDCharge / 12000;
            // System.out.println("a "+a);
            double max1 = Math
                    .max(sumAssured,
                            Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()));
            // System.out.println("max1 "+max1);
            double b = (max1 - (Double
                    .parseDouble(getAmountAvailableForInvestment_L()) + fundValueAtEnd_AQ));
            // System.out.println("b "+b);

            int c = 0;
            if (mortalityCharges) {
                c = 1;
            } else {
                c = 0;
            }
            this.AccTPDCharges_AK = cfap.roundUp_Level2(cfap
                    .getStringWithout_E(((a * Math.max(b, 0)) * c)));
        } else {
            this.AccTPDCharges_AK = "" + 0;
        }
    }

    public String getAccTPDCharges_AK() {
        return AccTPDCharges_S;
    }

    public void setTotalCharges_AL(int policyTerm, double riderCharges) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.totalCharges_AH = ""
                    + (Double.parseDouble(getPolicyAdministrationCharge_N())
                    + Double.parseDouble(getPPWBCharge_O())
                    + Double.parseDouble(getRiderBenefitCharges_P())
                    + Double.parseDouble(getAdd_AdbNAtpdCharges_Q())
                    + Double.parseDouble(getMortalityCharges_AJ()) + Double
                    .parseDouble(getAccTPDCharges_AK()));
        } else {
            this.totalCharges_AH = "" + 0;
        }
    }

    public String getTotalCharges_AL() {
        return totalCharges_AH;
    }

    public void setServiceTax_exclOfSTonAllocAndSurr_AM(double serviceTax,
                                                        boolean mortalityAndRiderCharges, boolean administrationCharges) {
        double a = 0, b = 0;
        if (mortalityAndRiderCharges) {
            a = +Double.parseDouble(getPPWBCharge_O())
                    + Double.parseDouble(getRiderBenefitCharges_P())
                    + Double.parseDouble(getAdd_AdbNAtpdCharges_Q())
                    + Double.parseDouble(getMortalityCharges_AJ())
                    + Double.parseDouble(getAccTPDCharges_AK());
        } else {
            a = 0;
        }
        if (administrationCharges) {
            b = Double.parseDouble(getPolicyAdministrationCharge_N());
        } else {
            b = 0;
        }
        this.serviceTaxExclOfSTOnAllocAndSurr_AI = cfap.getRoundOffLevel2(cfap
                .roundUp_Level4(cfap.getStringWithout_E((a + b) * serviceTax)));
    }

    public String getServiceTax_exclOfSTonAllocAndSurr_AM() {
        return serviceTaxExclOfSTOnAllocAndSurr_AI;
    }

    public void setAdditionToFundIfAny_AO(double fundValueAtEnd_AQ,
                                          int policyTerm, double riderCharges) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            double temp1 = 0, temp2 = 0;
            temp1 = (fundValueAtEnd_AQ
                    + Double.parseDouble(getAmountAvailableForInvestment_L())
                    - Double.parseDouble(getTotalCharges_AL()) - Double
                    .parseDouble(getServiceTax_exclOfSTonAllocAndSurr_AM()));
            double a = (double) 1 / 12;
            temp2 = (cfap.pow((1 + prop.int2), a)) - 1;
            this.additionToFundIfAny_AK = cfap.roundUp_Level2(cfap
                    .getStringWithout_E(temp1 * temp2));
        } else {
            this.additionToFundIfAny_AK = "" + 0;
        }
    }

    public String getAdditionToFundIfAny_AO() {
        return additionToFundIfAny_AK;
    }

    public void setFundBeforeFMC_AP(double fundValueAtEnd_AQ, int policyTerm,
                                    double riderCharges) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.fundBeforeFMC_AL = cfap
                    .roundUp_Level2(cfap.getStringWithout_E(fundValueAtEnd_AQ
                            + Double.parseDouble(getAmountAvailableForInvestment_L())
                            + Double.parseDouble(getAdditionToFundIfAny_AO())
                            - Double.parseDouble(getTotalCharges_AL())
                            - Double.parseDouble(getServiceTax_exclOfSTonAllocAndSurr_AM())));
        } else {
            this.fundBeforeFMC_AL = "" + 0;
        }
    }

    public String getFundBeforeFMC_AP() {
        return fundBeforeFMC_AL;
    }


    public void setFundManagementCharge_AQ(int policyTerm, double percentToBeInvested_EquityFund, double percentToBeInvested_EquityOptFund, double percentToBeInvested_GrowthFund, double percentToBeInvested_BalancedFund, double percentToBeInvested_BondFund, double percentToBeInvested_PureFund, double percentToBeInvested_Top300Fund, double percentToBeInvested_MidCapFund, double percentToBeInvested_BondOptimiserFundII, double percentToBeInvested_MoneyMarketFundII, double percentToBeInvested_CorpBondFund) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            // if(Integer.parseInt(getMonth_E())==1)
            //	            {this.fundManagementCharge_X=cfap.roundUp_Level2(cfap.getStringWithout_E(Double.parseDouble(getFundBeforeFMC_X()) * (prop.charge_Fund +(getCharge_fund_ren( percentToBeInvested_EquityFund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_BondFund, percentToBeInvested_PureFund, percentToBeInvested_Top300Fund,percentToBeInvested_MidCapFund)/12))));}
            // else
            {
                this.fundManagementCharge_AM = cfap.roundUp_Level2(cfap.getStringWithout_E(Double.parseDouble(getFundBeforeFMC_AP()) * (getCharge_fund_ren(percentToBeInvested_EquityFund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_BondFund, percentToBeInvested_PureFund, percentToBeInvested_Top300Fund, percentToBeInvested_MidCapFund, percentToBeInvested_BondOptimiserFundII, percentToBeInvested_MoneyMarketFundII, percentToBeInvested_CorpBondFund) / 12)));
            }
        } else {
            this.fundManagementCharge_AM = "" + 0;
        }
    }

    public String getFundManagementCharge_AQ() {
        return fundManagementCharge_AM;
    }

    public void setGuaranteeCharge_AR(int policyTerm) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.guaranteeCharge_AR = ""
                    + (Double.parseDouble(getFundBeforeFMC_AP())
                    * prop.guarantee_charge / 12);
        } else {
            this.guaranteeCharge_AR = "" + 0;
        }
    }

    public String getGuaranteeCharge_AR() {
        return guaranteeCharge_AR;
    }


    public void setServiceTaxOnFMC_AS(boolean fundManagementCharges, double percentToBeInvested_EquityFund, double percentToBeInvested_EquityOptFund, double percentToBeInvested_GrowthFund, double percentToBeInvested_BalancedFund, double percentToBeInvested_BondFund, double percentToBeInvested_PureFund, double percentToBeInvested_Top300Fund, double percentToBeInvested_MidCapFund, double serviceTax, double percentToBeInvested_BondOptimiserFundII, double percentToBeInvested_MoneyMarketFundII, double percentToBeInvested_CorpBondFund) {
        double a = 0, b = 0;
//		b=Double.parseDouble(getGuaranteeCharge_Z())+Double.parseDouble(cfap.getRoundOffLevel2(cfap.getStringWithout_E((Double.parseDouble(getFundManagementCharge_AQ()) * (0.0135/getCharge_fund_ren( percentToBeInvested_EquityFund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_BondFund, percentToBeInvested_PureFund, percentToBeInvested_Top300Fund,percentToBeInvested_MidCapFund))))));
        b = Double.parseDouble(getGuaranteeCharge_Z()) + Double.parseDouble(cfap.getRoundOffLevel2(cfap.getStringWithout_E((Double.parseDouble(getFundManagementCharge_AQ()) * getCharge_fund_ren(percentToBeInvested_EquityFund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_BondFund, percentToBeInvested_PureFund, percentToBeInvested_Top300Fund, percentToBeInvested_MidCapFund, percentToBeInvested_BondOptimiserFundII, percentToBeInvested_MoneyMarketFundII, percentToBeInvested_CorpBondFund) / getCharge_fund_ren(percentToBeInvested_EquityFund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_BondFund, percentToBeInvested_PureFund, percentToBeInvested_Top300Fund, percentToBeInvested_MidCapFund, percentToBeInvested_BondOptimiserFundII, percentToBeInvested_MoneyMarketFundII, percentToBeInvested_CorpBondFund)))));
        if (fundManagementCharges) {
            a = 1.0;
        } else {
            a = 0;
        }
        this.serviceTaxOnFMC_AN = cfap.getRoundOffLevel2(cfap
                .getStringWithout_E(a * b * serviceTax));
    }

    public String getServiceTaxOnFMC_AS() {
        return serviceTaxOnFMC_AN;
    }

    public void setFundValueAfterFMCAndBeforeGA_AT(int policyTerm) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.fundValueAfterFMCBerforeGA_AO = ""
                    + (Double.parseDouble(getFundBeforeFMC_AP())
                    - Double.parseDouble(getFundManagementCharge_AQ())
                    - Double.parseDouble(getGuaranteeCharge_AR()) - Double
                    .parseDouble(getServiceTaxOnFMC_AS()));
        } else {
            this.fundValueAfterFMCBerforeGA_AO = "" + 0;
        }

    }

    public String getFundValueAfterFMCAndBeforeGA_AT() {
        return fundValueAfterFMCBerforeGA_AO;
    }

    public void setTotalServiceTax_AN(double riderCharges, double serviceTax) {
        double temp1 = 0, temp2 = 0;
        temp1 = Double.parseDouble(getServiceTaxOnAllocation_K())
                + Double.parseDouble(getServiceTax_exclOfSTonAllocAndSurr_AM())
                + Double.parseDouble(getServiceTaxOnFMC_AS());
        if (prop.riderCharges) {
            temp2 = 0;
        } else {
            temp2 = 0;
        }
        this.totalServiceTax_AJ = cfap.roundUp_Level2(cfap
                .getStringWithout_E(temp1 + temp2 * serviceTax));
    }

    public String getTotalServiceTax_AN() {
        return totalServiceTax_AJ;
    }

    public void setGuaranteedAddition_AU(String planType, double annualPremium,
                                         int PPT) {

        double guaranteeCharge_arr[] = {0, 0, 0, 0, 0.000, 0.010, 0.000,
                0.000, 0.000, 0.025, 0.000, 0.000, 0.000, 0.000, 0.035, 0.000,
                0.000, 0.000, 0.000, 0.050, 0.000, 0.000, 0.000, 0.000, 0.060,
                0.000, 0.000, 0.000, 0.000, 0.070};
        double guaranteedCharge = 0, a = 0;

        if ((Integer.parseInt(getMonth_E()) % 12) == 0)
            guaranteedCharge = guaranteeCharge_arr[Integer
                    .parseInt(getYear_F()) - 1];
        else
            guaranteedCharge = 0;

        if ((Integer.parseInt(getMonth_E()) % 12) == 0)
            a = 1;
        else
            a = 0;
        if (Integer.parseInt(getMonth_E()) <= 24) {
            this.guaranteedAddition_AP = "" + 0;
        } else {
            this.guaranteedAddition_AP = cfap
                    .roundUp_Level2(cfap.getStringWithout_E((1 - getLoyaltyCharge())
                            * a
                            * annualPremium
                            * guaranteedCharge
                            + getLoyaltyCharge()
                            * guaranteedCharge
                            * Double.parseDouble(getLast2YearAvgFundValue_AR())));
        }
    }

    public String getGuaranteedAddition_AU() {
        return guaranteedAddition_AP;
    }

    public void setTerminalAddition_AV(String planType, int PPT, int policyTerm) {

        double terminalCharge = 0, a = 0;
        terminalCharge = 0;

        if (Integer.parseInt(getMonth_E()) == (policyTerm * 12))
            a = (Double.parseDouble(getFundValueAfterFMCAndBeforeGA_AT()) + Double
                    .parseDouble(getGuaranteedAddition_AU())) * terminalCharge;
        else
            a = 0;
        if (getPolicyInForce_G().equals("N")) {
            this.terminalAddition_AV = "" + 0;
        } else {
            this.terminalAddition_AV = cfap.roundUp_Level2(cfap
                    .getStringWithout_E(a));
        }
    }

    public String getTerminalAddition_AV() {
        return terminalAddition_AV;
    }

    public void setFundValueAtEnd_AW() {
        this.fundValueAtEnd_AQ = ""
                + (Double.parseDouble(getGuaranteedAddition_AU())
                + Double.parseDouble(getTerminalAddition_AV()) + Double
                .parseDouble(getFundValueAfterFMCAndBeforeGA_AT()));
    }

    public String getFundValueAtEnd_AW() {
        return fundValueAtEnd_AQ;
    }


    public void setSurrenderCharges_AX(double annualPremium, int PPT, String planType) {
        double a = Math.min(Double.parseDouble(getFundValueAtEnd_AW()), (double) annualPremium);
        // System.out.println("a "+a);
        double b = getSurrenderCharge(PPT, annualPremium, planType);
        // System.out.println("b "+b);
        // System.out.println("a*b "+(a*b));
        this.surrenderCharges_AR = cfap.roundUp_Level2(cfap
                .getStringWithout_E(Math.min((a * b),
                        Double.parseDouble(getSurrenderCap_AV()))));
    }

    public String getSurrenderCharges_AX() {
        return surrenderCharges_AR;
    }

    public void setServiceTaxOnSurrenderCharges_AY(boolean surrenderCharges, double serviceTax) {
        if (surrenderCharges) {
            this.serviceTaxOnSurrenderCharges_AS = cfap.getRoundOffLevel2(cfap
                    .roundUp_Level4(cfap.getStringWithout_E(Double
                            .parseDouble(getSurrenderCharges_AX())
                            * serviceTax)));
        } else {
            this.serviceTaxOnSurrenderCharges_AS = "" + 0;
        }
    }

    public String getServiceTaxOnSurrenderCharges_AY() {
        return serviceTaxOnSurrenderCharges_AS;
    }

    public void setSurrenderValue_AZ() {
        this.surrenderValue_AT = ""
                + (Double.parseDouble(getFundValueAtEnd_AW())
                - Double.parseDouble(getSurrenderCharges_AX()) - Double
                .parseDouble(getServiceTaxOnSurrenderCharges_AY()));
    }

    public String getSurrenderValue_AZ() {
        return surrenderValue_AT;
    }

    public void setDeathBenefit_BA(int policyTerm, double sumAssured) {
        if (Integer.parseInt(getYear_F()) > policyTerm) {
            this.deathBenefit_AU = "" + 0;
        } else {
            this.deathBenefit_AU = ""
                    + (Math.max(
                    Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()),
                    Math.max(sumAssured,
                            Double.parseDouble(getFundValueAtEnd_AW()))));
        }
    }

    public String getDeathBenefit_BA() {
        return deathBenefit_AU;
    }

    // public void setPrevArrayFundvalAftFMC()
    // {
    // if()
    // }
    //
    //	public String[] setArrayofPrev(String[] source,String[] result,String newVal)
    // {
    // for(int i=0;i<12;i++)
    // {
    // if((i+1)<12)
    // result[i]=source[i+1];
    //
    // }
    // result[11]=newVal;
    // return result;
    // }
    //
    // public double getAverageofArray(String[] source)
    // {
    // double result = 0;
    // for(int i=0;i<12;i++)
    // {
    // result=result+Double.parseDouble(source[i]);
    //
    // }
    //
    // return result;
    // }
    //
    //
    public String getLast2YearAvgFundValue_AB() {
        return last2YearAvgFundValue_AB;
    }

    public void setLast2YearAvgFundValue_AB(
            double[] arrFundValAfterFMCBeforeGA, int count_) {
        if (Integer.parseInt(getMonth_E()) <= 24) {
            this.last2YearAvgFundValue_AB = "" + 0;
        } else {
            int month = Integer.parseInt(getMonth_E());
            double valTobeDivided = 0;
            // if(Integer.parseInt(getMonth_E())==72)
            // {
            for (int i = count_; i < (12 + count_); i++) {    //System.out.println(arrFundValAfterFMCBeforeGA[i]+ "  "+(12+count_)+"   "+i);
                valTobeDivided = valTobeDivided + arrFundValAfterFMCBeforeGA[i];
            }
            count++;
            // }

            ////[Excel is Wrong[Calculation of cell AB25 to AB21 in BI_Incl_Mort & Ser Tax Sheet]- but this doesnt affect the final calculation because LOYALTY ADDITION column for all input scenario gives zero output]
            ////*******************************Wrong Code [To match with Excel] [Start]****************************************************************************************************************************************
            // if(Integer.parseInt(getMonth_E()) == 7)
            // {
            // ////System.out.println("***********");
            // ////System.out.println("IN SEVEN");
            // ////System.out.println("valTobeDivided   ->  "+valTobeDivided);
            // ////System.out.println("Divide By  ->  "+10);
            // ////System.out.println("***********");
            // this.last2YearAvgFundValue_AB=""+(valTobeDivided/10);
            // }
            // else if(Integer.parseInt(getMonth_E()) == 8)
            // {this.last2YearAvgFundValue_AB=""+(valTobeDivided/11);}
            // else if(Integer.parseInt(getMonth_E()) == 9)
            // {this.last2YearAvgFundValue_AB=""+(valTobeDivided/12);}
            // else if(Integer.parseInt(getMonth_E()) == 10)
            // {this.last2YearAvgFundValue_AB=""+(valTobeDivided/13);}
            // else if(Integer.parseInt(getMonth_E()) == 11)
            // {
            // //System.out.println("***********");
            // //System.out.println("IN Eleventh Row");
            // //System.out.println("valTobeDivided   ->  "+valTobeDivided);
            // //System.out.println("Divide By  ->  "+14);
            // //System.out.println("***********");
            // this.last2YearAvgFundValue_AB=""+(valTobeDivided/14);
            // }
            // else if(Integer.parseInt(getMonth_E()) == 12)
            // {this.last2YearAvgFundValue_AB=""+(valTobeDivided/15);}
            // else if(Integer.parseInt(getMonth_E()) == 13)
            // {this.last2YearAvgFundValue_AB=""+(valTobeDivided/16);}
            // else if(Integer.parseInt(getMonth_E()) == 14)
            // {this.last2YearAvgFundValue_AB=""+(valTobeDivided/17);}
            // else if(Integer.parseInt(getMonth_E()) == 15)
            // {this.last2YearAvgFundValue_AB=""+(valTobeDivided/18);}
            // else if(Integer.parseInt(getMonth_E()) == 16)
            // {this.last2YearAvgFundValue_AB=""+(valTobeDivided/19);}
            // else if(Integer.parseInt(getMonth_E()) == 17)
            // {this.last2YearAvgFundValue_AB=""+(valTobeDivided/20);}
            // else if(Integer.parseInt(getMonth_E()) == 18)
            // {this.last2YearAvgFundValue_AB=""+(valTobeDivided/20);}
            // else if(Integer.parseInt(getMonth_E()) == 19)
            // {this.last2YearAvgFundValue_AB=""+(valTobeDivided/21);}
            // else if(Integer.parseInt(getMonth_E()) == 20)
            // {this.last2YearAvgFundValue_AB=""+(valTobeDivided/22);}
            // else if(Integer.parseInt(getMonth_E()) == 21)
            // {this.last2YearAvgFundValue_AB=""+(valTobeDivided/23);}
            // else if(Integer.parseInt(getMonth_E()) == 22)
            // {this.last2YearAvgFundValue_AB=""+(valTobeDivided/23);}
            // else if(Integer.parseInt(getMonth_E()) == 23)
            // {this.last2YearAvgFundValue_AB=""+(valTobeDivided/23);}
            // else
            {
                this.last2YearAvgFundValue_AB = ""
                        + cfap.roundUp_Level2(cfap
                        .getStringWithout_E((valTobeDivided / 12)));
            }
        }
        //*******************************Wrong Code [To match with Excel] [End]**************************************************************
    }

    public String getLast2YearAvgFundValue_AR() {
        return last2YearAvgFundValue_AR;
    }

    public void setLast2YearAvgFundValue_AR(
            double[] arrFundValAfterFMCBeforeGA_AR, int count_) {
        if (Integer.parseInt(getMonth_E()) <= 6) {
            this.last2YearAvgFundValue_AR = "" + 0;
        } else {
            int month = Integer.parseInt(getMonth_E());
            double valTobeDivided = 0;
            // if(Integer.parseInt(getMonth_E())==72)
            // {
            for (int i = count_; i < (12 + count_); i++) {
                //System.out.println(arrFundValAfterFMCBeforeGA_AR[i]+ "  "+(12+count_)+"   "+i);
                valTobeDivided = valTobeDivided
                        + arrFundValAfterFMCBeforeGA_AR[i];
            }
            count_AR++;
            // }
            //*******************************Wrong Code [To match with Excel] [Start]**************************************************************
            // if(Integer.parseInt(getMonth_E()) == 7)
            // {
            // //System.out.println("***********");
            // //System.out.println("IN SEVEN");
            // //System.out.println("valTobeDivided   ->  "+valTobeDivided);
            // //System.out.println("Divide By  ->  "+8);
            // //System.out.println("***********");
            // this.last2YearAvgFundValue_AR=""+(valTobeDivided/8);
            // }
            // else if(Integer.parseInt(getMonth_E()) == 8)
            // {this.last2YearAvgFundValue_AR=""+(valTobeDivided/9);}
            // else if(Integer.parseInt(getMonth_E()) == 9)
            // {this.last2YearAvgFundValue_AR=""+(valTobeDivided/10);}
            // else if(Integer.parseInt(getMonth_E()) == 10)
            // {this.last2YearAvgFundValue_AR=""+(valTobeDivided/11);}
            // else if(Integer.parseInt(getMonth_E()) == 11)
            // {this.last2YearAvgFundValue_AR=""+(valTobeDivided/12);}
            // else if(Integer.parseInt(getMonth_E()) == 12)
            // {this.last2YearAvgFundValue_AR=""+(valTobeDivided/13);}
            // else if(Integer.parseInt(getMonth_E()) == 13)
            // {this.last2YearAvgFundValue_AR=""+(valTobeDivided/14);}
            // else if(Integer.parseInt(getMonth_E()) == 14)
            // {this.last2YearAvgFundValue_AR=""+(valTobeDivided/15);}
            // else if(Integer.parseInt(getMonth_E()) == 15)
            // {this.last2YearAvgFundValue_AR=""+(valTobeDivided/16);}
            // else if(Integer.parseInt(getMonth_E()) == 16)
            // {this.last2YearAvgFundValue_AR=""+(valTobeDivided/17);}
            // else if(Integer.parseInt(getMonth_E()) == 17)
            // {this.last2YearAvgFundValue_AR=""+(valTobeDivided/18);}
            // else if(Integer.parseInt(getMonth_E()) == 18)
            // {this.last2YearAvgFundValue_AR=""+(valTobeDivided/18);}
            // else if(Integer.parseInt(getMonth_E()) == 19)
            // {this.last2YearAvgFundValue_AR=""+(valTobeDivided/19);}
            // else if(Integer.parseInt(getMonth_E()) == 20)
            // {this.last2YearAvgFundValue_AR=""+(valTobeDivided/20);}
            // else if(Integer.parseInt(getMonth_E()) == 21)
            // {this.last2YearAvgFundValue_AR=""+(valTobeDivided/21);}
            // else if(Integer.parseInt(getMonth_E()) == 22)
            // {this.last2YearAvgFundValue_AR=""+(valTobeDivided/22);}
            // else if(Integer.parseInt(getMonth_E()) == 23)
            // {this.last2YearAvgFundValue_AR=""+(valTobeDivided/23);}
            // else
            {
                this.last2YearAvgFundValue_AR = "" + (valTobeDivided / 12);
            }
            // *******************************Wrong Code [To match with Excel]
            // [End]**************************************************************
        }
    }

    public double getCommission_BL(double annualisesPrem, double topupPrem,
                                   SmartPrivilegeBean smartprivilegeBean, int ppt) {
        double topup = 0;
        return getCommistionRate(smartprivilegeBean, ppt) * annualisesPrem
                + (topupPrem * topup);

    }

    private double getCommistionRate(SmartPrivilegeBean smartprivilegeBean,
                                     int ppt) {
        int findYear;

        if (Integer.parseInt(year_F) > 10) {
            findYear = 11;
        } else
            findYear = Integer.parseInt(year_F);

        if (smartprivilegeBean.getIsForStaffOrNot()) {
            return 0;
        } else if (smartprivilegeBean.getPlanType().equals("Limited")) {

            double[] lpptArr = {0.025, 0.025, 0.025, 0.025, 0.025, 0.010,
                    0.010, 0.01, 0.01, 0.01, 0.01};
            if (Integer.parseInt(year_F) > ppt)
                return 0;
            else
                return lpptArr[findYear - 1];
        } else if (smartprivilegeBean.getPlanType().equals("Single")) {
            double[] pptArr = {0.02, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            return pptArr[findYear - 1];
        } else {
            double[] commArr = {0.025, 0.025, 0.025, 0.025, 0.025, 0.010,
                    0.010, 0.01, 0.01, 0.01, 0.01};
            return commArr[findYear - 1];
        }

    }

    public void setReductionYield_BF(int policyTerm, double _fundValueAtEnd_AT) {
        double a, b;
        // if((Integer.parseInt(getMonth_E())) <=
        // (noOfYearsElapsedSinceInception*12)+1)
        if ((Integer.parseInt(getMonth_BD())) < (policyTerm * 12)) {
            a = -(Double.parseDouble(getPremium_I()));
        } else {
            a = 0;
        }

        if ((Integer.parseInt(getMonth_BD())) == (policyTerm * 12)) {
            b = _fundValueAtEnd_AT;
        } else {
            b = 0;
        }
        // System.out.println("a_BC "+a);
        // System.out.println("b_BC "+b);
        this.reductionYield_BF = "" + (a + b);
    }

    public String getReductionYield_BF() {
        return reductionYield_BF;
    }

    public void setMonth_BD(int monthNumber) {
        this.month_BD = "" + monthNumber;
    }

    public String getMonth_BD() {
        return month_BD;
    }

    public void setIRRAnnual_BF(double ans) {
        // System.out.println("aaaaaaa "+((comm.pow((1+ans),12)-1)*100));
        this.irrAnnual_BF = "" + ((cfap.pow((1 + ans), 12) - 1));
    }

    public String getIRRAnnual_BF() {
        return irrAnnual_BF;
    }

    public double irr(ArrayList<String> values, double guess) {
        int maxIterationCount = 20;
        double absoluteAccuracy = 1E-7;
        double[] arr = new double[values.size()];
        double x0 = guess;
        double x1;

        int i = 0;
        // System.out.println("inside irr ");
        // System.out.println("values "+values);
        while (i < maxIterationCount) {

            // the value of the function (NPV) and its derivate can be
            // calculated in the same loop
            double fValue = 0;
            double fDerivative = 0;
            for (int k = 0; k < values.size(); k++) {
                // System.out.println("value "+Double.parseDouble(values.get(k)));
                arr[k] = Double.parseDouble(values.get(k));
                fValue += arr[k] / Math.pow(1.0 + x0, k);
                fDerivative += -k * arr[k] / Math.pow(1.0 + x0, k + 1);
            }

            // the essense of the Newton-Raphson Method
            x1 = x0 - fValue / fDerivative;

            if (Math.abs(x1 - x0) <= absoluteAccuracy) {
                return x1;
            }

            x0 = x1;
            ++i;
        }
        // maximum number of iterations is exceeded
        return 0;
    }

    /* Added by Akshaya on 14-Jun-16 start * */
    public double getMinesOccuInterest(double SumAssured_Basic, double premium) {
        // return (SumAssured_Basic / 1000) * 2;
        double temp = (SumAssured_Basic - premium) / 1000;
        return (temp * 2) / 12;

    }
    /* Added by Akshaya on 14-Jun-16 end * */
}
