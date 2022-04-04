package sbilife.com.pointofsale_bancaagency.smartpower;

import java.text.DecimalFormat;
import java.util.ArrayList;

import sbilife.com.pointofsale_bancaagency.common.CommonForAllProd;

/**
 * @author e21946
 */
class SmartPowerInsuranceBusinessLogic {

    private CommonForAllProd comm = null;
    private SmartPowerInsuranceProperties SPW_prop = null;

    private String month_E = null;
    private String year_F = null;
    private String age_H = null;
    private String premium_I = null, policyInforce_G = "Y";
    private String topUpPremium_J = null;
    private String premiumAllocationCharge_K = null;
    private String topUpCharges_L = null;
    private String ServiceTaxOnAllocation_M = null;
    private String amountAvailableForInvestment_N = null;
    private String amountAvailableForInvestment_N1 = null;
    private String sumAssuredRelatedCharges_O = null;
    private String riderCharges_P = null;
    private String policyAdministrationCharges_Q = null;
    private String mortalityCharges_R = null;
    private String accTPDCharges_S = null;
    private String totalCharges_T = null;
    private String serviceTaxExclOfSTOnAllocAndSurr_U = null;
    private String totalServiceTax_V = null;
    private String additionToFundIfAny_W = null;
    private String fundBeforeFMC_X = null;
    private String fundManagementCharge_Y = null;
    private String serviceTaxOnFMC_Z = null;
    private String fundValueAfterFMCBerforeGA_AA = null;
    private String guaranteedAddition_AB = null;
    private String fundValueAtEnd_AC = null;
    private String surrenderCharges_AD = null;
    private String serviceTaxOnSurrenderCharges_AE = null;
    private String surrenderValue_AF = null;
    private String deathBenefit_AG = null;
    private String mortalityCharges_AH = null;
    private String accTPDCharges_AI = null;
    private String totalCharges_AJ = null;
    private String serviceTaxExclOfSTOnAllocAndSurr_AK = null;
    private String totalServiceTax_AL = null;
    private String additionToFundIfAny_AM = null;
    private String fundBeforeFMC_AN = null;
    private String fundManagementCharge_AO = null;
    private String serviceTaxOnFMC_AP = null;
    private String fundValueAfterFMCBerforeGA_AQ = null;
    private String guaranteedAddition_AR = null;
    private String fundValueAtEnd_AS = null;
    private String surrenderCharges_AT = null;
    private String serviceTaxOnSurrenderCharges_AU = null;
    private String surrenderValue_AV = null;
    private String deathBenefit_AW = null;
    private String oneHundredPercentOfCummulativePremium_AY = null;
    private String surrenderCap_AX = null;
    private String serviceTaxExclOfSTOnAllocAndSurrReductionYield_U = null;
    private String fundBeforeFMCReductionYield_X = null;
    private String serviceTaxOnFMCReductionYield_Z = null;
    private String additionToFundIfAnyReductionYield_W = null;
    private String fundManagementChargeReductionYield_Y = null;
    private String totalServiceTaxReductionYield_V = null;
    private String fundValueAfterFMCBerforeGAReductionYield_AA = null;
    private String fundValueAtEndReductionYield_AC = null;
    private String surrenderChargesReductionYield_AD = null;
    private String serviceTaxOnSurrenderChargesReductionYield_AE = null;
    private String surrenderValueReductionYield_AF = null;
    private String deathBenefitReductionYield_AG = null;
    private String serviceTaxExclOfSTOnAllocAndSurrReductionYield_AK = null;
    private String additionToFundIfAnyReductionYield_AM = null;
    private String fundBeforeFMCReductionYield_AN = null;
    private String fundManagementChargeReductionYield_AO = null;
    private String serviceTaxOnFMCReductionYield_AP = null;
    private String totalServiceTaxReductionYield_AL = null;
    private String fundValueAfterFMCBerforeGAReductionYield_AQ = null;
    private String guaranteedAdditionRedutionYield_AR = null;
    private String fundValueAtEndReductionYield_AS = null;
    private String surrenderChargesReductionYield_AT = null;
    private String serviceTaxOnSurrenderChargesReductionYield_AU = null;
    private String surrenderValueReductionYield_AV = null;
    private String deathBenefitReductionYield_AW = null;
    private String month_BO = null;
    private String reductionYield_BU = null;
    private String reductionYield_BQ = null;
    private String irrAnnual_BQ = null;
    private String irrAnnual_BU = null;
    private String reductionInYieldMaturityAt = null;
    private String reductionInYieldNumberOfYearsElapsedSinceInception = null;
    private String netYieldAt4Percent = null;
    private String netYieldAt8Percent = null;
    private String irrAnnual_BP = null;
    private String reductionYield_BP = null;

    public SmartPowerInsuranceBusinessLogic() {
        comm = new CommonForAllProd();
        SPW_prop = new SmartPowerInsuranceProperties();
    }

    public void setMonth_E(int rowNum) {
        this.month_E = ("" + rowNum);
    }

    public String getMonth_E() {
        return month_E;
    }

    public void setYear_F() {
        this.year_F = comm.getRoundUp("" + (Double.parseDouble(getMonth_E()) / 12));
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

    public void setPremium_I(int premiumPayingTerm, int PF, double effectivePremium) {
        if (getPolicyInForce_G().equals("Y")) {
            if ((Integer.parseInt(getYear_F()) <= premiumPayingTerm) && (((Integer.parseInt(getMonth_E()) - 1) % (12 / PF)) == 0)) {
                premium_I = "" + (effectivePremium / PF);
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

    public void setTopUpPremium_J(boolean topUp, double effectiveTopUpPrem, String addTopUp) {
        if (topUp) {
            if (getMonth_E().equals("1") && addTopUp.equals("Yes")) {
                this.topUpPremium_J = ("" + effectiveTopUpPrem);
            } else {
                this.topUpPremium_J = ("" + 0);
            }
        } else {
            this.topUpPremium_J = ("" + 0);
        }
    }

    public String getTopUpPremium_J() {
        return topUpPremium_J;
    }

    public void setPremiumAllocationCharge_K(boolean staffDisc, int policyTerm) {
        double premAllocationCharge;
        if (staffDisc) {
            premAllocationCharge = Math.max(0, (getAllocationCharges_AZ(policyTerm) - getCommission_BL(policyTerm, staffDisc)));
            //System.out.println("all charge in staff" + premAllocationCharge);
//		this.premiumAllocationCharge_K=comm.getRoundUp(comm.getStringWithout_E(premAllocationCharge));
            this.premiumAllocationCharge_K = comm.getRound(comm.getStringWithout_E(premAllocationCharge));
        } else {
            //System.out.println("** 1 **");
            premAllocationCharge = getAllocationCharges_AZ(policyTerm);
            //System.out.println("all charge in staff else" + premAllocationCharge);
//		this.premiumAllocationCharge_K=comm.getRoundOffLevel2(comm.getStringWithout_E(premAllocationCharge));
//		this.premiumAllocationCharge_K=comm.getRoundUp(comm.getStringWithout_E(premAllocationCharge));
            this.premiumAllocationCharge_K = comm.getRound(comm.getStringWithout_E(premAllocationCharge));
            //                   this.premiumAllocationCharge_K= comm.roundUp_Level2(comm.getStringWithout_E(premAllocationCharge));

        }
    }

    public String getPremiumAllocationCharge_K() {
        return premiumAllocationCharge_K;
    }

    private double getAllocationCharges_AZ(int policyTerm) {
        double allocationCharges;
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            //System.out.println("** 2 **");
            if (Integer.parseInt(getYear_F()) > 10) {
                //System.out.println("** 3 **");
                allocationCharges = (0.015 * Double.parseDouble(getPremium_I()));
                //System.out.println("** 3.1 **" + allocationCharges);
                return allocationCharges;
            } else {
                //System.out.println("**4 **");
                allocationCharges = (getAllocationCharge() * Double.parseDouble(getPremium_I()));
                return allocationCharges;
            }
        } else {
            return allocationCharges = 0;
        }
    }

    private double getAllocationCharge() {
        //System.out.println("** 5 **");
        if (getYear_F().equals("1")) {
            return 0.0575;
        } else if (getYear_F().equals("2")) {
            return 0.04;
        } else if (getYear_F().equals("3")) {
            return 0.04;
        } else if (getYear_F().equals("4")) {
            return 0.04;
        } else if (getYear_F().equals("5")) {
            return 0.04;
        } else if (getYear_F().equals("6")) {
            return 0.035;
        } else if (getYear_F().equals("7")) {
            return 0.035;
        } else if (getYear_F().equals("8")) {
            return 0.025;
        } else if (getYear_F().equals("9")) {
            return 0.02;
        } else if (getYear_F().equals("10")) {
            return 0.015;
        } else {
            //System.out.println("** 6 **");
            return 0.015;
        }
    }

    /*** Priyanka Warekar - 24-08-2015 - start ***/
    public double getCommission(int policyTerm, boolean isStaff) {
        double commission;
        if (Integer.parseInt(getYear_F()) > policyTerm) {
            return 0;
        } else if (Integer.parseInt(getYear_F()) > 10) {
            commission = (getComm(isStaff) * Double.parseDouble(getPremium_I()));
            return commission;
        } else {
            commission = (getComm(isStaff) * Double.parseDouble(getPremium_I()));
            return commission;
        }
    }

    private double getComm(boolean isStaff) {
        if (isStaff)
            return 0;
        else {
            if (getYear_F().equals("1")) {
                return 0.05;
            } else if (getYear_F().equals("2")) {
                return 0.03;
            } else if (getYear_F().equals("3")) {
                return 0.03;
            } else if (getYear_F().equals("4")) {
                return 0.03;
            } else if (getYear_F().equals("5")) {
                return 0.03;
            } else if (getYear_F().equals("6")) {
                return 0.02;
            } else if (getYear_F().equals("7")) {
                return 0.02;
            } else if (getYear_F().equals("8")) {
                return 0.02;
            } else if (getYear_F().equals("9")) {
                return 0.015;
            } else if (getYear_F().equals("10")) {
                return 0.01;
            } else {
                return 0.01;
            }
        }
    }

    private double getCommission_BL(int policyTerm, boolean isStaff) {
        double commission;
        if (Integer.parseInt(getYear_F()) > policyTerm) {
            return 0;
        } else if (Integer.parseInt(getYear_F()) > 10) {
            commission = (getComm() * Double.parseDouble(getPremium_I()));
            return commission;
        } else {
            commission = (getComm() * Double.parseDouble(getPremium_I()));
            return commission;
        }
    }

    private double getComm() {

        if (getYear_F().equals("1")) {
            return 0.05;
        } else if (getYear_F().equals("2")) {
            return 0.03;
        } else if (getYear_F().equals("3")) {
            return 0.03;
        } else if (getYear_F().equals("4")) {
            return 0.03;
        } else if (getYear_F().equals("5")) {
            return 0.03;
        } else if (getYear_F().equals("6")) {
            return 0.02;
        } else if (getYear_F().equals("7")) {
            return 0.02;
        } else if (getYear_F().equals("8")) {
            return 0.02;
        } else if (getYear_F().equals("9")) {
            return 0.015;
        } else if (getYear_F().equals("10")) {
            return 0.01;
        } else {
            return 0.01;
        }

    }

    /*** Priyanka Warekar - 24-08-2015 - end ***/
    public void setTopUpCharges_L(double topUp) {
        this.topUpCharges_L = comm.getRoundUp("" + Double.parseDouble(getTopUpPremium_J()) * topUp);
    }

    public String getTopUpCharges_L() {
        return topUpCharges_L;
    }

    public void setServiceTaxOnAllocation_M(boolean allocationCharges, double serviceTax) {

        if (allocationCharges) {
            this.ServiceTaxOnAllocation_M = comm.getRoundOffLevel2(comm.getStringWithout_E((Double.parseDouble(getPremiumAllocationCharge_K()) + Double.parseDouble(getTopUpCharges_L())) * serviceTax));
            //this.ServiceTaxOnAllocation_M=comm.roundUp_Level2(comm.getStringWithout_E((Double.parseDouble(getPremiumAllocationCharge_K()) + Double.parseDouble(getTopUpCharges_L())) * serviceTax));
        } else {
            this.ServiceTaxOnAllocation_M = "" + 0;
        }
        //        System.out.println("op round " + comm.getRoundOffLevel2((Double.parseDouble(getPremiumAllocationCharge_K()) + Double.parseDouble(getTopUpCharges_L())) * serviceTax));
        //        System.out.println("op round up" + comm.roundUp_Level2(comm.getStringWithout_E((Double.parseDouble(getPremiumAllocationCharge_K()) + Double.parseDouble(getTopUpCharges_L())) * serviceTax)));
        //this.ServiceTaxOnAllocation_M=comm.getRoundOffLevel2((Double.parseDouble(getPremiumAllocationCharge_K()) + Double.parseDouble(getTopUpCharges_L())) * serviceTax);

    }

    public double getTotalFirstYearPremium(double sum_C, double sum_E, double sum_FY_Premium) {
        boolean adb = false;
        String Eligibility_ADB = "Not Eligible";
        double sum = 0;
        if (adb && Eligibility_ADB.equals("Eligible")) {
            sum = sum_C + sum_E + sum_FY_Premium;
        } else {
            sum = sum_E + sum_FY_Premium;
        }
        return sum;
    }

    public String getServiceTaxOnAllocation_M() {
        return ServiceTaxOnAllocation_M;
    }

    public void setAmountAvailableForInvestment_N(int policyTerm) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.amountAvailableForInvestment_N = comm.getRoundOffLevel2(comm.getStringWithout_E(Double.parseDouble(getPremium_I()) + Double.parseDouble(getTopUpPremium_J()) - Double.parseDouble(getPremiumAllocationCharge_K()) - Double.parseDouble(getTopUpCharges_L()) - Double.parseDouble(getServiceTaxOnAllocation_M())));
        } else {
            this.amountAvailableForInvestment_N = "" + 0;
        }
    }

    public String getAmountAvailableForInvestment_N() {
        return amountAvailableForInvestment_N;
    }

    public void setAmountAvailableForInvestment_N1(int policyTerm) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
//		this.amountAvailableForInvestment_N1=comm.getRoundOffLevel2(comm.getStringWithout_E(Double.parseDouble(getPremium_I())+ Double.parseDouble(getTopUpPremium_J()) - Double.parseDouble(getPremiumAllocationCharge_K()) - Double.parseDouble(getTopUpCharges_L())));
            this.amountAvailableForInvestment_N1 = (comm.getStringWithout_E(Double.parseDouble(getPremium_I()) + Double.parseDouble(getTopUpPremium_J()) - Double.parseDouble(getPremiumAllocationCharge_K()) - Double.parseDouble(getTopUpCharges_L())));
        } else {
            this.amountAvailableForInvestment_N1 = "" + 0;
        }
    }

    public String getAmountAvailableForInvestment_N1() {
        return amountAvailableForInvestment_N1;
    }

    public void setSumAssuredRelatedCharges_O(int policyTerm, double sumAssured, double charge_SumAssuredBase) {
        double a, b;
        if (Integer.parseInt(getMonth_E()) == 1) {
            a = charge_SumAssuredBase;
        } else {
            a = 0;
        }
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            b = charge_SumAssuredBase;
        } else {
            b = 0;
        }
        this.sumAssuredRelatedCharges_O = comm.getRoundOffLevel2(comm.getStringWithout_E(sumAssured * (a + b)));
    }

    public String getSumAssuredRelatedCharges_O() {
        return sumAssuredRelatedCharges_O;
    }

    public void setPolicyAdministrationCharge_Q(double _policyAdministrationCharge_Q, int charge_Inflation, double fixedMonthlyExp_RP, int inflation_pa_RP) {
        if (getPolicyInForce_G().equals("Y")) {
            if (((Integer.parseInt(getMonth_E()) - 1) % 12) == 0) {
                this.policyAdministrationCharges_Q = comm.getRoundOffLevel2(comm.getStringWithout_E(getCharge_PP_Ren(fixedMonthlyExp_RP, inflation_pa_RP) / 12 * comm.getPowerOutput((1 + charge_Inflation), Integer.parseInt("" + (Integer.parseInt(getMonth_E()) / 12)))));
            } else {
                this.policyAdministrationCharges_Q = comm.getRoundOffLevel2(comm.getStringWithout_E(_policyAdministrationCharge_Q));
            }
        } else {
            this.policyAdministrationCharges_Q = "" + 0;
        }
    }

    private double getCharge_PP_Ren(double fixedMonthlyExp_RP, int inflation_pa_RP) {
        return fixedMonthlyExp_RP * 12 * comm.pow(1 + inflation_pa_RP, 0);
    }

    public String getPolicyAdministrationCharge_Q() {
        return policyAdministrationCharges_Q;
    }

    public void setOneHundredPercentOfCummulativePremium_AY(double _oneHundredPercentOfCummulativePremium_AY) {
        double a;
        if (getPolicyInForce_G().equals("Y")) {
            a = Double.parseDouble(getPremium_I()) + (_oneHundredPercentOfCummulativePremium_AY / 1.05);
        } else {
            a = 0;
        }
        this.oneHundredPercentOfCummulativePremium_AY = comm.getRoundOffLevel2New(comm.getStringWithout_E(a * 1.05));
    }

    public String getOneHundredPercentOfCummulativePremium_AY() {
        return oneHundredPercentOfCummulativePremium_AY;
    }

    public String[] getForBIArr(double mortalityCharges_AsPercentOfofLICtable) {
        int[] ageArr = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100};
        String[] strArrTReturn = new String[99];
        SmartPowerInsuranceDB smartPowerDB = new SmartPowerInsuranceDB();
        //System.out.println("length "+smartPowerDB.getIAIarray().length);
        for (int i = 0; i < smartPowerDB.getIAIarray().length - 2; i++) {
            //            strArrTReturn[i]=comm.roundUp_Level2(comm.getStringWithout_E(((smartPowerDB.getIAIarray()[i] + smartPowerDB.getIAIarray()[i+1])/2) * mortalityCharges_AsPercentOfofLICtable * 1000));
//		strArrTReturn[i]=comm.getStringWithout_E(((smartPowerDB.getIAIarray()[i] + smartPowerDB.getIAIarray()[i+1])/2) * mortalityCharges_AsPercentOfofLICtable * 1000);
            strArrTReturn[i] = comm.getStringWithout_E(((smartPowerDB.getIAIarray()[i] + smartPowerDB.getIAIarray()[i + 1]) / 2) * mortalityCharges_AsPercentOfofLICtable);
        }
        return strArrTReturn;
    }

    public void setMortalityCharges_R(int ageAtEntry, double sumAssured, String[] forBIArray, int policyTerm, boolean mortalityCharges, double _fundValueAtEnd_AC, String increasingCoverOption) {
        if (!(getPolicyInForce_G().equals("Y")) || Integer.parseInt(getYear_F()) > policyTerm) {
            this.mortalityCharges_R = "" + 0;
        } else {
            double arrOutput = Double.parseDouble(forBIArray[(ageAtEntry + Integer.parseInt(getYear_F())) - 1]);
//		System.out.println("ARRAY OUTPUT "+arrOutput);
            /*double a=1-arrOutput/1000;*/
//		System.out.println("a/1000 "+a);
            /*double b=(double)1/12;*/
//		System.out.println("1/12 "+b);
//		System.out.println("whole value "+(comm.pow(a,b)));
            //double c=(sumAssured * getIncreasingSA(increasingCoverOption)-(Double.parseDouble(getAmountAvailableForInvestment_N())+_fundValueAtEnd_AC));
            double c = sumAssured * getIncreasingSA(increasingCoverOption);
//		System.out.println("c "+c);

            DecimalFormat df = new DecimalFormat("#.#######");

            String roundTo7 = df.format(arrOutput);
            String test = comm.getRoundOffLevel5(roundTo7);

//        System.out.println("arroutput RY "+arrOutput);
//        System.out.println("round to 5 RY"+test);

            double a = Double.parseDouble(test) / 12;
            double max1 = Math.max(c, Double.parseDouble(getOneHundredPercentOfCummulativePremium_AY()));
            double b = (max1 - (Double.parseDouble(getAmountAvailableForInvestment_N()) + _fundValueAtEnd_AC));


            int d = 0;
            if (mortalityCharges) {
                d = 1;
            } else {
                d = 0;
            }

            /*double max1=Math.max(Double.parseDouble(getOneHundredPercentOfCummulativePremium_AY()), c);*/
//		System.out.println("e max "+max1);
            /*double diff= max1 - (Double.parseDouble(getAmountAvailableForInvestment_N())+_fundValueAtEnd_AC);*/

//		System.out.println("diff" +diff);
            //            this.mortalityCharges_R= comm.roundUp_Level2(comm.getStringWithout_E((((1-(comm.pow(a,b)))*Math.max(diff, 0))*d)));
            /*this.mortalityCharges_R= comm.getRoundOffLevel2(comm.getStringWithout_E((((1-(comm.pow(a,b)))*Math.max(diff, 0))*d)));*/
            this.mortalityCharges_R = comm.getRoundOffLevel2New(comm.getStringWithout_E(((a * Math.max(b, 0)) * d)));
        }
    }

    public String getMortalityCharges_R() {
        return mortalityCharges_R;
    }

    private double getIncreasingSA(String increasingCoverOption) {
        double multiplier = 0;
        if (increasingCoverOption.equals("Yes") && Integer.parseInt(getYear_F()) > 5) {

            if (Integer.parseInt(getYear_F()) >= 6 && Integer.parseInt(getYear_F()) < 11)
                multiplier = 1.1;
            else if (Integer.parseInt(getYear_F()) >= 11 && Integer.parseInt(getYear_F()) < 16)
                multiplier = 1.2;
            else if (Integer.parseInt(getYear_F()) >= 16 && Integer.parseInt(getYear_F()) < 21)
                multiplier = 1.3;
            else if (Integer.parseInt(getYear_F()) >= 21 && Integer.parseInt(getYear_F()) < 26)
                multiplier = 1.4;
            else if (Integer.parseInt(getYear_F()) >= 26 && Integer.parseInt(getYear_F()) <= 30)
                multiplier = 1.5;
        } else {
            multiplier = 1;
        }
        return multiplier;
    }

    public void setAccTPDCharge_S(int policyTerm, double accTPDCharge, double sumAssured, boolean mortalityCharges, double _fundValueAtEnd_AC, String increasingCoverOption) {
        if (!(getPolicyInForce_G().equals("Y")) || Integer.parseInt(getYear_F()) > policyTerm) {
            this.accTPDCharges_S = "" + 0;
        } else {
            double accTPD = accTPDCharge / 12000;
            //System.out.println("accTPD "+accTPD);
            double a = sumAssured * getIncreasingSA(increasingCoverOption);
            //System.out.println("a "+a);
            int b = 0;
            if (mortalityCharges) {
                b = 1;
            } else {
                b = 0;
            }
            double max1 = Math.max(Double.parseDouble(getOneHundredPercentOfCummulativePremium_AY()), a);
            //System.out.println("e max "+max1);
            double diff = max1 - (Double.parseDouble(getAmountAvailableForInvestment_N()) + _fundValueAtEnd_AC);
            //System.out.println("diff" +diff);

            this.accTPDCharges_S = comm.getRoundOffLevel2(comm.getStringWithout_E(((accTPD * Math.max(diff, 0)) * b)));
        }
    }

    public String getAccTPDCharge_S() {
        return accTPDCharges_S;
    }

    public void setTotalCharges_T(int policyTerm, double riderCharges_P) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.totalCharges_T = comm.getStringWithout_E(Double.parseDouble(getPolicyAdministrationCharge_Q()) + Double.parseDouble(getMortalityCharges_R()) + Double.parseDouble(getAccTPDCharge_S()) + Double.parseDouble(getSumAssuredRelatedCharges_O()) + riderCharges_P);
        } else {
            this.totalCharges_T = "" + 0;
        }
    }

    public String getTotalCharges_T() {
        return totalCharges_T;
    }

    public void setServiceTaxExclOfSTOnAllocAndSurr_U(boolean mortalityAndRiderCharges, boolean administrationCharges, double serviceTax) {
        double add1, add2;
        if (mortalityAndRiderCharges) {
            add1 = (Double.parseDouble(getMortalityCharges_R()) + Double.parseDouble(getAccTPDCharge_S()));
        } else {
            add1 = 0;
        }
        if (administrationCharges) {
            add2 = Double.parseDouble(getPolicyAdministrationCharge_Q());
        } else {
            add2 = 0;
        }
        this.serviceTaxExclOfSTOnAllocAndSurr_U = comm.getRoundOffLevel2New(comm.getStringWithout_E((add1 + add2) * serviceTax));
    }

    public String getServiceTaxExclOfSTOnAllocAndSurr_U() {
        return serviceTaxExclOfSTOnAllocAndSurr_U;
    }

    public void setTotalServiceTax_V(boolean riderCharges, double riderCharges_P, double serviceTax) {
        double temp;
        if (riderCharges) {
            temp = riderCharges_P;
        } else {
            temp = 0;
        }
        this.totalServiceTax_V = comm.getRoundOffLevel2(comm.getStringWithout_E(Double.parseDouble(getServiceTaxOnAllocation_M()) + Double.parseDouble(getServiceTaxExclOfSTOnAllocAndSurr_U()) + Double.parseDouble(getServiceTaxOnFMC_Z()) + temp * serviceTax));
    }

    public String getTotalServiceTax_V() {
        return totalServiceTax_V;
    }

    public void setAdditionToFundIfAny_W(double _fundValueAtEnd_AC, int policyTerm, double int1, double riderCharges_P) {
	/*if(Integer.parseInt(getYear_F()) <= policyTerm)
	{
            //             System.out.println("addition "+(_fundValueAtEnd_AC+Double.parseDouble(getAmountAvailableForInvestment_N())-Double.parseDouble(getTotalCharges_T())-Double.parseDouble(getServiceTaxExclOfSTOnAllocAndSurr_U())+riderCharges_P));
            //             System.out.println("power "+(comm.pow((1+int1),(double)1/12)-1));
            //             System.out.println("without roundup "+((_fundValueAtEnd_AC+Double.parseDouble(getAmountAvailableForInvestment_N())-Double.parseDouble(getTotalCharges_T())-Double.parseDouble(getServiceTaxExclOfSTOnAllocAndSurr_U())+riderCharges_P)*(comm.pow((1+int1),(double)1/12)-1)));
            this.additionToFundIfAny_W = comm.getRoundOffLevel2(comm.getStringWithout_E((_fundValueAtEnd_AC + Double.parseDouble(getAmountAvailableForInvestment_N()) - Double.parseDouble(getTotalCharges_T()) - Double.parseDouble(getServiceTaxExclOfSTOnAllocAndSurr_U()) + riderCharges_P) * (comm.pow((1 + int1), (double) 1 / 12) - 1)));
	}
	else
	{this.additionToFundIfAny_W=""+0;}*/
	/*System.out.println("_fundValueAtEnd_AC"+_fundValueAtEnd_AC);
	System.out.println("getAmountAvailableForInvestment_N()"+getAmountAvailableForInvestment_N());
	System.out.println("getTotalCharges_T()"+getTotalCharges_T());
	System.out.println("getServiceTaxExclOfSTOnAllocAndSurr_U()"+getServiceTaxExclOfSTOnAllocAndSurr_U());
	System.out.println("riderCharges_P"+riderCharges_P);*/
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            double temp1 = 0, temp2 = 0;
            temp1 = (_fundValueAtEnd_AC + Double.parseDouble(getAmountAvailableForInvestment_N()) - Double.parseDouble(getTotalCharges_T()) - Double.parseDouble(getServiceTaxExclOfSTOnAllocAndSurr_U()) + riderCharges_P);
            double a = (double) 1 / 12;
            temp2 = (comm.pow((1 + int1), a)) - 1;
            this.additionToFundIfAny_W = comm.roundUp_Level2(comm.getStringWithout_E(temp1 * temp2));
        } else {
            this.additionToFundIfAny_W = "" + 0;
        }
    }

    public String getAdditionToFundIfAny_W() {
        return additionToFundIfAny_W;
    }

    public void setFundBeforeFMC_X(double _fundValueAtEnd_AC, int policyTerm, double riderCharges_P) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {

            //this.fundBeforeFMC_X=comm.getRoundUp(_fundValueAtEnd_AC+Double.parseDouble(getAmountAvailableForInvestment_N())-Double.parseDouble(getTotalCharges_T())+Double.parseDouble(getAdditionToFundIfAny_W())-Double.parseDouble(getServiceTaxExclOfSTOnAllocAndSurr_U())+riderCharges_P);
            //System.out.println("sum x " + (_fundValueAtEnd_AC+Double.parseDouble(getAmountAvailableForInvestment_N())-Double.parseDouble(getTotalCharges_T())+Double.parseDouble(getAdditionToFundIfAny_W())-Double.parseDouble(getServiceTaxExclOfSTOnAllocAndSurr_U())+riderCharges_P));
            //this.fundBeforeFMC_X=comm.getRoundUp(comm.getStringWithout_E(_fundValueAtEnd_AC+Double.parseDouble(getAmountAvailableForInvestment_N())-Double.parseDouble(getTotalCharges_T())+Double.parseDouble(getAdditionToFundIfAny_W())-Double.parseDouble(getServiceTaxExclOfSTOnAllocAndSurr_U())+riderCharges_P));
            //System.out.println("sum x " + (_fundValueAtEnd_AC+Double.parseDouble(getAmountAvailableForInvestment_N())-Double.parseDouble(getTotalCharges_T())+Double.parseDouble(getAdditionToFundIfAny_W())-Double.parseDouble(getServiceTaxExclOfSTOnAllocAndSurr_U())+riderCharges_P));
            //this.fundBeforeFMC_X=comm.getRoundUp1(comm.getStringWithout_E(_fundValueAtEnd_AC+Double.parseDouble(getAmountAvailableForInvestment_N())-Double.parseDouble(getTotalCharges_T())+Double.parseDouble(getAdditionToFundIfAny_W())-Double.parseDouble(getServiceTaxExclOfSTOnAllocAndSurr_U())+riderCharges_P));
            //this.fundBeforeFMC_X=comm.getRoundUp(comm.getRoundOffLevel2(_fundValueAtEnd_AC+Double.parseDouble(getAmountAvailableForInvestment_N())-Double.parseDouble(getTotalCharges_T())+Double.parseDouble(getAdditionToFundIfAny_W())-Double.parseDouble(getServiceTaxExclOfSTOnAllocAndSurr_U())+riderCharges_P));
//		this.fundBeforeFMC_X=comm.getRoundUp(comm.getRoundOffLevel2(comm.getStringWithout_E(_fundValueAtEnd_AC+Double.parseDouble(getAmountAvailableForInvestment_N())-Double.parseDouble(getTotalCharges_T())+Double.parseDouble(getAdditionToFundIfAny_W())-Double.parseDouble(getServiceTaxExclOfSTOnAllocAndSurr_U())+riderCharges_P)));
            this.fundBeforeFMC_X = (comm.getRoundOffLevel2(comm.getStringWithout_E(_fundValueAtEnd_AC + Double.parseDouble(getAmountAvailableForInvestment_N()) - Double.parseDouble(getTotalCharges_T()) + Double.parseDouble(getAdditionToFundIfAny_W()) - Double.parseDouble(getServiceTaxExclOfSTOnAllocAndSurr_U()) + riderCharges_P)));
        } else {
            this.fundBeforeFMC_X = "" + 0;
        }
    }

    public String getFundBeforeFMC_X() {
        return fundBeforeFMC_X;
    }

    public void setFundManagementCharge_Y(int policyTerm, String fundOption, double percentToBeInvested_EquityFund, double percentToBeInvested_BondFund, double percentToBeInvested_Top300Fund, double percentToBeInvested_EquityOptFund, double percentToBeInvested_GrowthFund, double percentToBeInvested_BalancedFund, double percentToBeInvested_MoneyMarketFund, double percentToBeInvested_BondOptimiserFund, double percentToBeInvested_PureFund, double percentToBeInvested_CorpBondFund) {
        double temp1 = 0, temp2 = 0;
        if (Integer.parseInt(getMonth_E()) == 1) {
            temp1 = SPW_prop.charge_Fund;
        } else {
            temp1 = 0;
        }
        double a = getCharge_FundRen(fundOption, percentToBeInvested_EquityFund, percentToBeInvested_BondFund, percentToBeInvested_Top300Fund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_MoneyMarketFund, percentToBeInvested_BondOptimiserFund, percentToBeInvested_PureFund, percentToBeInvested_CorpBondFund);
        temp2 = getCharge_FundRen(fundOption, percentToBeInvested_EquityFund, percentToBeInvested_BondFund, percentToBeInvested_Top300Fund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_MoneyMarketFund, percentToBeInvested_BondOptimiserFund, percentToBeInvested_PureFund, percentToBeInvested_CorpBondFund) / 12;
        //        System.out.println("ChargeFundRen "+a);
        //        System.out.println("temp 1 "+temp1);
        //        System.out.println("ChargeFundRen/12 "+temp2);
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.fundManagementCharge_Y = comm.getRoundOffLevel2(comm.getStringWithout_E(Double.parseDouble(getFundBeforeFMC_X()) * (temp1 + temp2)));
        } else {
            this.fundManagementCharge_Y = "" + 0;
        }
    }

    public String getFundManagementCharge_Y() {
        return fundManagementCharge_Y;
    }

    public double getCharge_FundRen(String fundOption, double percentToBeInvested_EquityFund, double percentToBeInvested_BondFund, double percentToBeInvested_Top300Fund, double percentToBeInvested_EquityOptFund, double percentToBeInvested_GrowthFund, double percentToBeInvested_BalancedFund, double percentToBeInvested_MoneyMarketFund, double percentToBeInvested_BondOptimiserFund, double percentToBeInvested_PureFund, double percentToBeInvested_CorpBondFund) {
        if (fundOption.equals("Trigger Fund")) {
            //System.out.println("trigger "+(0.8*SPW_prop.FMC_EquityFund + 0.2*SPW_prop.FMC_BondFund));
            return (0.8 * SPW_prop.FMC_EquityFund + 0.2 * SPW_prop.FMC_BondFund);
        } else {
            return percentToBeInvested_EquityFund * SPW_prop.FMC_EquityFund +
                    percentToBeInvested_BondFund * SPW_prop.FMC_BondFund +
                    percentToBeInvested_Top300Fund * SPW_prop.FMC_Top300Fund +
                    percentToBeInvested_EquityOptFund * SPW_prop.FMC_EquityOptFund +
                    percentToBeInvested_GrowthFund * SPW_prop.FMC_GrowthFund +
                    percentToBeInvested_BalancedFund * SPW_prop.FMC_BalancedFund +
                    percentToBeInvested_MoneyMarketFund * SPW_prop.FMC_MoneyMarketFund +
                    percentToBeInvested_BondOptimiserFund * SPW_prop.FMC_BondOptimiserFund +
                    percentToBeInvested_PureFund * SPW_prop.FMC_PureFund +
                    percentToBeInvested_CorpBondFund * SPW_prop.FMC_CorpBondFund;
        }
    }

    public void setServiceTaxOnFMC_Z(boolean fundManagementCharges, double serviceTax, String fundOption, double percentToBeInvested_EquityFund, double percentToBeInvested_BondFund, double percentToBeInvested_Top300Fund, double percentToBeInvested_EquityOptFund, double percentToBeInvested_GrowthFund, double percentToBeInvested_BalancedFund, double percentToBeInvested_MoneyMarketFund, double percentToBeInvested_BondOptimiserFund, double percentToBeInvested_PureFund, double percentToBeInvested_CorpBondFund) {
        double a = 0;
        if (fundManagementCharges)
//	{a=(Double.parseDouble(getFundManagementCharge_Y()) * (0.0135/getCharge_FundRen( fundOption,percentToBeInvested_EquityFund, percentToBeInvested_BondFund,percentToBeInvested_Top300Fund,percentToBeInvested_EquityOptFund,percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_MoneyMarketFund,percentToBeInvested_BondOptimiserFund,percentToBeInvested_PureFund,percentToBeInvested_CorpBondFund)));}
        {
            a = ((Double.parseDouble(getFundManagementCharge_Y()) * getCharge_FundRen(fundOption, percentToBeInvested_EquityFund, percentToBeInvested_BondFund, percentToBeInvested_Top300Fund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_MoneyMarketFund, percentToBeInvested_BondOptimiserFund, percentToBeInvested_PureFund, percentToBeInvested_CorpBondFund) / getCharge_FundRen(fundOption, percentToBeInvested_EquityFund, percentToBeInvested_BondFund, percentToBeInvested_Top300Fund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_MoneyMarketFund, percentToBeInvested_BondOptimiserFund, percentToBeInvested_PureFund, percentToBeInvested_CorpBondFund)));
        } else {
            a = 0;
        }
        this.serviceTaxOnFMC_Z = comm.getRoundOffLevel2(comm.getStringWithout_E(a * serviceTax));
    }

    public String getServiceTaxOnFMC_Z() {
        return serviceTaxOnFMC_Z;
    }

    public void setFundValueAfterFMCandBeforeGA_AA(int policyTerm) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
		            /*System.out.println( getFundBeforeFMC_X() +" *********** "+ Double.parseDouble(getFundBeforeFMC_X()));
		            System.out.println(getFundManagementCharge_Y() +" ************ "+Double.parseDouble(getFundManagementCharge_Y()));
		            System.out.println(getServiceTaxOnFMC_Z() + " *************** "+ Double.parseDouble(getServiceTaxOnFMC_Z()));
		            System.out.println("AA ouput" + comm.getStringWithout_E(Double.parseDouble(getFundBeforeFMC_X())-Double.parseDouble(getFundManagementCharge_Y())-Double.parseDouble(getServiceTaxOnFMC_Z())));*/

            this.fundValueAfterFMCBerforeGA_AA = comm.getRoundOffLevel2(comm.getStringWithout_E(Double.parseDouble(getFundBeforeFMC_X()) - Double.parseDouble(getFundManagementCharge_Y()) - Double.parseDouble(getServiceTaxOnFMC_Z())));
            //this.fundValueAfterFMCBerforeGA_AA= comm.getRoundOffLevel2(Double.parseDouble(comm.getStringWithout_E(Double.parseDouble(getFundBeforeFMC_X())-Double.parseDouble(getFundManagementCharge_Y())-Double.parseDouble(getServiceTaxOnFMC_Z()))));
        } else {
            this.fundValueAfterFMCBerforeGA_AA = "" + 0;
        }
    }

    public String getFundValueAfterFMCandBeforeGA_AA() {
        return fundValueAfterFMCBerforeGA_AA;
    }

    public void setGuaranteedAddition_AB(double effectivePremium, int PPT, boolean guaranteedAddition) {
        if (getPolicyInForce_G().equals("N")) {
            this.guaranteedAddition_AB = "" + 0;
        } else {
            this.guaranteedAddition_AB = comm.getRoundOffLevel2(comm.getStringWithout_E(effectivePremium * getPercentOfFirstYearPremium(PPT, effectivePremium, guaranteedAddition)));
        }
    }

    public String getGuaranteedAddition_AB() {
        return guaranteedAddition_AB;
    }

    private double getPercentOfFirstYearPremium(int PPT, double effectivePremium, boolean guaranteedAddition) {
        if (guaranteedAddition) {
            if (getMonth_E().equals("60")) {
                if (PPT >= 5) {
                    return 0;
                } else {
                    return 0;
                }
            } else if (getMonth_E().equals("84")) {
                if (PPT >= 7) {
                    return 0;
                } else {
                    return 0;
                }
            } else if (getMonth_E().equals("120")) {
                if (PPT >= 10) {
                    return 0;
                } else {
                    return 0;
                }
            } else if (getMonth_E().equals("180")) {
                if (PPT >= 15) {
                    return 0;
                } else {
                    return 0;
                }
            } else if (getMonth_E().equals("240")) {
                if (PPT >= 20) {
                    return 0;
                } else {
                    return 0;
                }
            } else if (getMonth_E().equals("300")) {
                if (PPT >= 25) {
                    return 0;
                } else {
                    return 0;
                }
            } else if (getMonth_E().equals("360")) {
                if (PPT >= 30) {
                    return 0;
                } else {
                    return 0;
                }
            } else if (getMonth_E().equals("1188")) {
                if (PPT >= 99) {
                    return 0;
                } else {
                    return 0;
                }
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public void setFundValueAtEnd_AC() {
        this.fundValueAtEnd_AC = "" + (Double.parseDouble(getFundValueAfterFMCandBeforeGA_AA()) + Double.parseDouble(getGuaranteedAddition_AB()));
    }

    public String getFundValueAtEnd_AC() {
        return fundValueAtEnd_AC;
    }

    public void setSurrenderCap_AX(double effectivePremium) {
	/*//        System.out.println(" IN AX");
        if (getPolicyInForce_G().equals("Y")) {
            //            System.out.println("Inforce");
            //            System.out.println("Effective premium " + effectivePremium);
            //             System.out.println("index " + Math.min((Integer.parseInt(getYear_F())-1),11));
            //            this.surrenderCap_AX =""+(Double.parseDouble(calCapArr(effectivePremium)[Integer.parseInt(getYear_F())-1]));}
		this.surrenderCap_AX =""+(Double.parseDouble(calCapArr(effectivePremium)[Math.min((Integer.parseInt(getYear_F())-1),11)]));}
	else
	{
		//            System.out.println("not inforse");
		this.surrenderCap_AX =""+0;}*/


        double surrendercap = 0;
        if (getPolicyInForce_G().equals("Y")) {
            /*if(premiumamount<=25000)*/
            if (effectivePremium > 25000) {
                if (effectivePremium > 50000) {

                    if (getYear_F().equals("1")) {
                        surrendercap = 6000;
                    } else if (getYear_F().equals("2")) {
                        surrendercap = 5000;
                    } else if (getYear_F().equals("3")) {
                        surrendercap = 4000;
                    } else if (getYear_F().equals("4")) {
                        surrendercap = 2000;
                    } else {
                        surrendercap = 0;
                    }
                } else {
                    if (getYear_F().equals("1")) {
                        surrendercap = 3000;
                    } else if (getYear_F().equals("2")) {
                        surrendercap = 2000;
                    } else if (getYear_F().equals("3")) {
                        surrendercap = 1500;
                    } else if (getYear_F().equals("4")) {
                        surrendercap = 1000;
                    } else {
                        surrendercap = 0;
                    }
                }
            } else {
         	/*if(getYear_F().equals("1"))
         	{
         		surrendercap = 6000;
         	}
         	else if(getYear_F().equals("2"))
         	{
         		surrendercap = 5000;
         	}
         	else if(getYear_F().equals("3"))
         	{
         		surrendercap = 4000;
         	}
         	else if(getYear_F().equals("4"))
         	{
         		surrendercap = 2000;
         	}
         	else
         	{
         		surrendercap=0;
         	}*/
                if (getYear_F().equals("1")) {
                    surrendercap = 3000;
                } else if (getYear_F().equals("2")) {
                    surrendercap = 2000;
                } else if (getYear_F().equals("3")) {
                    surrendercap = 1500;
                } else if (getYear_F().equals("4")) {
                    surrendercap = 1000;
                } else {
                    surrendercap = 0;
                }
            }

            this.surrenderCap_AX = comm.getStringWithout_E((surrendercap));
        } else {
            this.surrenderCap_AX = "" + 0;
        }
    }

    public String getSurrenderCap_AX() {
        return surrenderCap_AX;
    }

    private String[] calCapArr(double effectivePremium) {
        if (effectivePremium <= 25000) {
            return new String[]{"3000", "2000", "1500", "1000", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"};
        } else {
            return new String[]{"6000", "5000", "4000", "2000", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"};
        }
    }

    public void setSurrenderCharges_AD(double effectivePremium, int PPT) {
	/*double min1=Math.min(Double.parseDouble(getFundValueAtEnd_AC()),effectivePremium);
        double a = getSurrenderCharge(effectivePremium, PPT);
	this.surrenderCharges_AD =comm.getRoundOffLevel2(comm.getStringWithout_E(Math.min((min1*a), Double.parseDouble(getSurrenderCap_AX()))));*/

        double surrenderCharge = 0;

//  if(premiumamount<=25000)
        if (effectivePremium > 25000) {
            if (effectivePremium > 50000) {

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
        } else {
  	/*if(getYear_F().equals("1"))
  	{
  		surrenderCharge = 0.06;
  	}
  	else if(getYear_F().equals("2"))
  	{
  		surrenderCharge = 0.04;
  	}
  	else if(getYear_F().equals("3"))
  	{
  		surrenderCharge = 0.03;
  	}
  	else if(getYear_F().equals("4"))
  	{
  		surrenderCharge = 0.02;
  	}
  	else
  	{
  		surrenderCharge=0;
  	}*/
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


        double val1 = Math.min(Double.parseDouble(getFundValueAtEnd_AC()), effectivePremium);
        double val2 = val1 * surrenderCharge;
        double val3 = Math.min(Double.parseDouble(getSurrenderCap_AX()), val2);

        //return surrenderCharges_AC =cfap.roundUp_Level2( cfap.getStringWithout_E(val3)) ;
        this.surrenderCharges_AD = comm.getRoundOffLevel2New(comm.getStringWithout_E(val3));

    }

    public String getSurrenderCharges_AD() {
        return surrenderCharges_AD;
    }

    private double getSurrenderCharge(double effectivePremium, int PPT) {
        double surrenderCharge = 0;
        if (PPT == 1) {/*For Saral Maha Anand Product PPT will never equal to 1*/} else {
            surrenderCharge = Double.parseDouble(calSurrRateArr(effectivePremium)[Math.min(Integer.parseInt(getYear_F()) - 1, 11)]);
        }
        return surrenderCharge;
    }

    private String[] calSurrRateArr(double effectivePremium) {
        if (effectivePremium <= 25000) {
            return new String[]{"0.20", "0.15", "0.10", "0.05", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"};
        } else {
            return new String[]{"0.06", "0.04", "0.03", "0.02", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"};
        }
    }

    public void setServiceTaxOnSurrenderCharges_AE(boolean surrenderCharges, double serviceTax) {
        if (surrenderCharges) {
            this.serviceTaxOnSurrenderCharges_AE = comm.getRoundOffLevel2(comm.getStringWithout_E(Double.parseDouble(getSurrenderCharges_AD()) * serviceTax));
        } else {
            this.serviceTaxOnSurrenderCharges_AE = "" + 0;
        }
    }

    public String getServiceTaxOnSurrenderCharges_AE() {
        return serviceTaxOnSurrenderCharges_AE;
    }

    public void setSurrenderValue_AF() {
        this.surrenderValue_AF = "" + (Double.parseDouble(getFundValueAtEnd_AC()) - Double.parseDouble(getSurrenderCharges_AD()) - Double.parseDouble(getServiceTaxOnSurrenderCharges_AE()));
    }

    public String getSurrenderValue_AF() {
        return surrenderValue_AF;
    }

    public void setDeathBenefit_AG(int policyTerm, double sumAssured, String increasingCoverOption) {
        if (Integer.parseInt(getYear_F()) > policyTerm) {
            this.deathBenefit_AG = "" + 0;
        } else {
            double a = (sumAssured * getIncreasingSA(increasingCoverOption));
            this.deathBenefit_AG = "" + Math.max(Double.parseDouble(getOneHundredPercentOfCummulativePremium_AY()), Math.max(a, Double.parseDouble(getFundValueAtEnd_AC())));
        }
    }

    public String getDeathBenefit_AG() {
        return deathBenefit_AG;
    }

    public double getDeathBenefit_AB(int policyTerm) {
        if (Integer.parseInt(getYear_F()) > policyTerm) {
            return 0;
        } else {

            double a = Double.parseDouble(getDeathBenefit_AG());
            return Math.max(Double.parseDouble(getFundValueAtEnd_AC()), Math.min(a, 10000000));
        }
    }

    public double getDeathBenefit_AR(int policyTerm) {
        if (Integer.parseInt(getYear_F()) > policyTerm) {
            return 0;
        } else {
            double a = Double.parseDouble(getDeathBenefit_AW());
            return Math.max(Double.parseDouble(getFundValueAtEnd_AS()), Math.min(a, 10000000));
        }
    }

    public void setMortalityCharges_AH(int ageAtEntry, double sumAssured, String[] forBIArray, int policyTerm, boolean mortalityCharges, double _fundValueAtEnd_AS, String increasingCoverOption) {
        if (Integer.parseInt(getYear_F()) > policyTerm) {
            this.mortalityCharges_AH = "" + 0;
        } else {
            double arrOutput = Double.parseDouble(forBIArray[(ageAtEntry + Integer.parseInt(getYear_F())) - 1]);
            //System.out.println("ARRAY OUTPUT "+arrOutput);
		/*double a=1-arrOutput/1000;
            //System.out.println("a/1000 "+a);
            double b = (double) 1 / 12;
            //System.out.println("1/12 "+b);


            double c = sumAssured * getIncreasingSA(increasingCoverOption);
            //System.out.println("c "+c);
		int d=0;
		if(mortalityCharges)
		{d=1;}
		else
		{d=0;}

		double max1=Math.max(Double.parseDouble(getOneHundredPercentOfCummulativePremium_AY()), c);
		//System.out.println("e max "+max1);
		double diff= max1 - (Double.parseDouble(getAmountAvailableForInvestment_N())+_fundValueAtEnd_AS);

		//System.out.println("diff" +diff);
		//            this.mortalityCharges_R= comm.roundUp_Level2(comm.getStringWithout_E((((1-(comm.pow(a,b)))*Math.max(diff, 0))*d)));
		this.mortalityCharges_AH= comm.getRoundOffLevel2(comm.getStringWithout_E((((1-(comm.pow(a,b)))*Math.max(diff, 0))*d)));*/

            DecimalFormat df = new DecimalFormat("#.#######");

            String roundTo7 = df.format(arrOutput);
            String test = comm.getRoundOffLevel5(roundTo7);

//        System.out.println("arroutput RY "+arrOutput);
//        System.out.println("round to 5 RY"+test);
            double c = sumAssured * getIncreasingSA(increasingCoverOption);
            double a = Double.parseDouble(test) / 12;
            double max1 = Math.max(c, Double.parseDouble(getOneHundredPercentOfCummulativePremium_AY()));
            double b = (max1 - (Double.parseDouble(getAmountAvailableForInvestment_N()) + _fundValueAtEnd_AS));


            int d = 0;
            if (mortalityCharges) {
                d = 1;
            } else {
                d = 0;
            }

            /*double max1=Math.max(Double.parseDouble(getOneHundredPercentOfCummulativePremium_AY()), c);*/
            //System.out.println("e max "+max1);
            /*double diff= max1 - (Double.parseDouble(getAmountAvailableForInvestment_N())+_fundValueAtEnd_AC);*/

            //System.out.println("diff" +diff);
            //            this.mortalityCharges_R= comm.roundUp_Level2(comm.getStringWithout_E((((1-(comm.pow(a,b)))*Math.max(diff, 0))*d)));
            /*this.mortalityCharges_R= comm.getRoundOffLevel2(comm.getStringWithout_E((((1-(comm.pow(a,b)))*Math.max(diff, 0))*d)));*/
            this.mortalityCharges_AH = comm.getRoundOffLevel2New(comm.getStringWithout_E(((a * Math.max(b, 0)) * d)));
        }
    }

    public String getMortalityCharges_AH() {
        return mortalityCharges_AH;
    }

    public void setAccTPDCharge_AI(int policyTerm, double accTPDCharge, double sumAssured, boolean mortalityCharges, double _fundValueAtEnd_AS, String increasingCoverOption) {
        if (Integer.parseInt(getYear_F()) > policyTerm) {
            this.accTPDCharges_AI = "" + 0;
        } else {
            double accTPD = accTPDCharge / 12000;
            //System.out.println("accTPD "+accTPD);
            double a = sumAssured * getIncreasingSA(increasingCoverOption);
            //System.out.println("a "+a);
            int b = 0;
            if (mortalityCharges) {
                b = 1;
            } else {
                b = 0;
            }
            double max1 = Math.max(Double.parseDouble(getOneHundredPercentOfCummulativePremium_AY()), a);
            //System.out.println("e max "+max1);
            double diff = max1 - (Double.parseDouble(getAmountAvailableForInvestment_N()) + _fundValueAtEnd_AS);
            //System.out.println("diff" +diff);
            this.accTPDCharges_AI = comm.getRoundOffLevel2(comm.getStringWithout_E(((accTPD * Math.max(diff, 0)) * b)));
        }
    }

    public String getAccTPDCharge_AI() {
        return accTPDCharges_AI;
    }

    public void setTotalCharges_AJ(int policyTerm, double riderCharges_P) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.totalCharges_AJ = comm.getRoundOffLevel2New(comm.getStringWithout_E(Double.parseDouble(getPolicyAdministrationCharge_Q()) + Double.parseDouble(getMortalityCharges_AH()) + Double.parseDouble(getAccTPDCharge_AI()) + Double.parseDouble(getSumAssuredRelatedCharges_O()) + riderCharges_P));
        } else {
            this.totalCharges_AJ = "" + 0;
        }
    }

    public String getTotalCharges_AJ() {
        return totalCharges_AJ;
    }

    public void setServiceTaxExclOfSTOnAllocAndSurr_AK(boolean mortalityAndRiderCharges, boolean administrationCharges, double serviceTax) {
        double add1, add2;
        if (mortalityAndRiderCharges) {

            add1 = (Double.parseDouble(getMortalityCharges_AH()) + Double.parseDouble(getAccTPDCharge_AI()));
        } else {
            add1 = 0;
        }
        if (administrationCharges) {

            add2 = Double.parseDouble(getPolicyAdministrationCharge_Q());
        } else {
            add2 = 0;
        }

        this.serviceTaxExclOfSTOnAllocAndSurr_AK = comm.getRoundOffLevel2New(comm.getStringWithout_E((add1 + add2) * serviceTax));
    }

    public String getServiceTaxExclOfSTOnAllocAndSurr_AK() {
        return serviceTaxExclOfSTOnAllocAndSurr_AK;
    }

    public void setTotalServiceTax_AL(boolean riderCharges, double riderCharges_P, double serviceTax) {
        double temp;
        if (riderCharges) {
            temp = riderCharges_P;
        } else {
            temp = 0;
        }
        this.totalServiceTax_AL = comm.getRoundOffLevel2(comm.getStringWithout_E(Double.parseDouble(getServiceTaxOnAllocation_M()) + Double.parseDouble(getServiceTaxExclOfSTOnAllocAndSurr_AK()) + Double.parseDouble(getServiceTaxOnFMC_AP()) + temp * serviceTax));
    }

    public String getTotalServiceTax_AL() {
        return totalServiceTax_AL;
    }

    public void setAdditionToFundIfAny_AM(double _fundValueAtEnd_AS, int policyTerm, double int2, double riderCharges_P) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            //System.out.println("addition "+(_fundValueAtEnd_AS+Double.parseDouble(getAmountAvailableForInvestment_N())-Double.parseDouble(getTotalCharges_AJ())-Double.parseDouble(getServiceTaxExclOfSTOnAllocAndSurr_AK())+riderCharges_P));
            //System.out.println("power "+(comm.pow((1+int2),(double)1/12)-1));
            //System.out.println("without roundup "+((_fundValueAtEnd_AS+Double.parseDouble(getAmountAvailableForInvestment_N())-Double.parseDouble(getTotalCharges_AJ())-Double.parseDouble(getServiceTaxExclOfSTOnAllocAndSurr_AK())+riderCharges_P)*(comm.pow((1+int2),(double)1/12)-1)));
            this.additionToFundIfAny_AM = comm.getRoundOffLevel2(comm.getStringWithout_E((_fundValueAtEnd_AS + Double.parseDouble(getAmountAvailableForInvestment_N()) - Double.parseDouble(getTotalCharges_AJ()) - Double.parseDouble(getServiceTaxExclOfSTOnAllocAndSurr_AK()) + riderCharges_P) * (comm.pow((1 + int2), (double) 1 / 12) - 1)));
        } else {
            this.additionToFundIfAny_AM = "" + 0;
        }
    }

    public String getAdditionToFundIfAny_AM() {
        return additionToFundIfAny_AM;
    }

    public void setFundBeforeFMC_AN(double _fundValueAtEnd_AS, int policyTerm, double riderCharges_P) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            //this.fundBeforeFMC_X=comm.getRoundUp(_fundValueAtEnd_AC+Double.parseDouble(getAmountAvailableForInvestment_N())-Double.parseDouble(getTotalCharges_T())+Double.parseDouble(getAdditionToFundIfAny_W())-Double.parseDouble(getServiceTaxExclOfSTOnAllocAndSurr_U())+riderCharges_P);
            //System.out.println("AS "+_fundValueAtEnd_AS +"    N "+getAmountAvailableForInvestment_N()+"     AJ "+getTotalCharges_AJ()+"     AM "+getAdditionToFundIfAny_AM()+"     AK "+getServiceTaxExclOfSTOnAllocAndSurr_AK()+ "    p "+riderCharges_P);

            this.fundBeforeFMC_AN = comm.getRoundOffLevel2New(comm.getStringWithout_E(_fundValueAtEnd_AS + Double.parseDouble(getAmountAvailableForInvestment_N()) - Double.parseDouble(getTotalCharges_AJ()) + Double.parseDouble(getAdditionToFundIfAny_AM()) - Double.parseDouble(getServiceTaxExclOfSTOnAllocAndSurr_AK()) + riderCharges_P));
        } else {
            this.fundBeforeFMC_AN = "" + 0;
        }
    }

    public String getFundBeforeFMC_AN() {
        return fundBeforeFMC_AN;
    }

    public void setFundManagementCharge_AO(int policyTerm, String fundOption, double percentToBeInvested_EquityFund, double percentToBeInvested_BondFund, double percentToBeInvested_Top300Fund, double percentToBeInvested_EquityOptFund, double percentToBeInvested_GrowthFund, double percentToBeInvested_BalancedFund, double percentToBeInvested_MoneyMarketFund, double percentToBeInvested_BondOptimiserFund, double percentToBeInvested_PureFund, double percentToBeInvested_CorpBondFund) {
        double temp1 = 0, temp2 = 0;
        if (Integer.parseInt(getMonth_E()) == 1) {
            temp1 = SPW_prop.charge_Fund;
        } else {
            temp1 = 0;
        }
        double a = getCharge_FundRen(fundOption, percentToBeInvested_EquityFund, percentToBeInvested_BondFund, percentToBeInvested_Top300Fund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_MoneyMarketFund, percentToBeInvested_BondOptimiserFund, percentToBeInvested_PureFund, percentToBeInvested_CorpBondFund);
        temp2 = getCharge_FundRen(fundOption, percentToBeInvested_EquityFund, percentToBeInvested_BondFund, percentToBeInvested_Top300Fund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_MoneyMarketFund, percentToBeInvested_BondOptimiserFund, percentToBeInvested_PureFund, percentToBeInvested_CorpBondFund) / 12;
        //System.out.println("ChargeFundRen "+a);
        //System.out.println("ChargeFundRen/12 "+temp2);
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.fundManagementCharge_AO = comm.getRoundOffLevel2(comm.getStringWithout_E(Double.parseDouble(getFundBeforeFMC_AN()) * (temp1 + temp2)));
        } else {
            this.fundManagementCharge_AO = "" + 0;
        }
    }

    public String getFundManagementCharge_AO() {
        return fundManagementCharge_AO;
    }

    public void setServiceTaxOnFMC_AP(boolean fundManagementCharges, double serviceTax, String fundOption, double percentToBeInvested_EquityFund, double percentToBeInvested_BondFund, double percentToBeInvested_Top300Fund, double percentToBeInvested_EquityOptFund, double percentToBeInvested_GrowthFund, double percentToBeInvested_BalancedFund, double percentToBeInvested_MoneyMarketFund, double percentToBeInvested_BondOptimiserFund, double percentToBeInvested_PureFund, double percentToBeInvested_CorpBondFund) {
        double a = 0;
        if (fundManagementCharges)
//	{a=(Double.parseDouble(getFundManagementCharge_AO()) * (0.0135/getCharge_FundRen( fundOption,percentToBeInvested_EquityFund, percentToBeInvested_BondFund,percentToBeInvested_Top300Fund,percentToBeInvested_EquityOptFund,percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_MoneyMarketFund,percentToBeInvested_BondOptimiserFund,percentToBeInvested_PureFund,percentToBeInvested_CorpBondFund)));}
        {
            a = ((Double.parseDouble(getFundManagementCharge_AO()) * getCharge_FundRen(fundOption, percentToBeInvested_EquityFund, percentToBeInvested_BondFund, percentToBeInvested_Top300Fund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_MoneyMarketFund, percentToBeInvested_BondOptimiserFund, percentToBeInvested_PureFund, percentToBeInvested_CorpBondFund) / getCharge_FundRen(fundOption, percentToBeInvested_EquityFund, percentToBeInvested_BondFund, percentToBeInvested_Top300Fund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_MoneyMarketFund, percentToBeInvested_BondOptimiserFund, percentToBeInvested_PureFund, percentToBeInvested_CorpBondFund)));
        } else {
            a = 0;
        }
        this.serviceTaxOnFMC_AP = comm.getRoundOffLevel2(comm.getStringWithout_E(a * serviceTax));
    }

    public String getServiceTaxOnFMC_AP() {
        return serviceTaxOnFMC_AP;
    }

    public void setFundValueAfterFMCandBeforeGA_AQ(int policyTerm) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.fundValueAfterFMCBerforeGA_AQ = comm.getRoundOffLevel2New(comm.getStringWithout_E(Double.parseDouble(getFundBeforeFMC_AN()) - Double.parseDouble(getFundManagementCharge_AO()) - Double.parseDouble(getServiceTaxOnFMC_AP())));
        } else {
            this.fundValueAfterFMCBerforeGA_AQ = "" + 0;
        }
    }

    public String getFundValueAfterFMCandBeforeGA_AQ() {
        return fundValueAfterFMCBerforeGA_AQ;
    }

    public void setGuaranteedAddition_AR(double effectivePremium, int PPT, boolean guaranteedAddition) {
        if (getPolicyInForce_G().equals("N")) {
            this.guaranteedAddition_AR = "" + 0;
        } else {
            this.guaranteedAddition_AR = comm.getRoundOffLevel2(comm.getStringWithout_E(effectivePremium * getPercentOfFirstYearPremium(PPT, effectivePremium, guaranteedAddition)));
        }
    }

    public String getGuaranteedAddition_AR() {
        return guaranteedAddition_AR;
    }

    public void setFundValueAtEnd_AS() {
        this.fundValueAtEnd_AS = comm.getStringWithout_E(Double.parseDouble(getFundValueAfterFMCandBeforeGA_AQ()) + Double.parseDouble(getGuaranteedAddition_AR()));
    }

    public String getFundValueAtEnd_AS() {
        return fundValueAtEnd_AS;
    }

    public void setSurrenderCharges_AT(double effectivePremium, int PPT) {
        double min1 = Math.min(Double.parseDouble(getFundValueAtEnd_AS()), effectivePremium);
        double a = getSurrenderCharge(effectivePremium, PPT);
        this.surrenderCharges_AT = comm.getRoundOffLevel2(comm.getStringWithout_E(Math.min((min1 * a), Double.parseDouble(getSurrenderCap_AX()))));
    }

    public String getSurrenderCharges_AT() {
        return surrenderCharges_AT;
    }

    public void setServiceTaxOnSurrenderCharges_AU(boolean surrenderCharges, double serviceTax) {
        if (surrenderCharges) {
            this.serviceTaxOnSurrenderCharges_AU = comm.getRoundOffLevel2(comm.getStringWithout_E(Double.parseDouble(getSurrenderCharges_AD()) * serviceTax));
        } else {
            this.serviceTaxOnSurrenderCharges_AU = "" + 0;
        }
    }

    public String getServiceTaxOnSurrenderCharges_AU() {
        return serviceTaxOnSurrenderCharges_AU;
    }

    public void setSurrenderValue_AV() {
        this.surrenderValue_AV = "" + (Double.parseDouble(getFundValueAtEnd_AS()) - Double.parseDouble(getSurrenderCharges_AD()) - Double.parseDouble(getServiceTaxOnSurrenderCharges_AU()));
    }

    public String getSurrenderValue_AV() {
        return surrenderValue_AV;
    }

    public void setDeathBenefit_AW(int policyTerm, double sumAssured, String increasingCoverOption) {
        if (Integer.parseInt(getYear_F()) > policyTerm) {
            this.deathBenefit_AW = "" + 0;
        } else {
            double a = (sumAssured * getIncreasingSA(increasingCoverOption));
            this.deathBenefit_AW = "" + Math.max(Double.parseDouble(getOneHundredPercentOfCummulativePremium_AY()), Math.max(a, Double.parseDouble(getFundValueAtEnd_AS())));
        }
    }

    public String getDeathBenefit_AW() {
        return deathBenefit_AW;
    }


    public void setServiceTaxExclOfSTOnAllocAndSurrReductionYield_U(boolean mortalityAndRiderCharges, boolean administrationCharges, double serviceTax) {
        double add1, add2;
        if (mortalityAndRiderCharges) {
            add1 = (Double.parseDouble(getMortalityCharges_R()));
        } else {
            add1 = 0;
        }
        if (administrationCharges) {
            add2 = Double.parseDouble(getPolicyAdministrationCharge_Q());
        } else {
            add2 = 0;
        }
        this.serviceTaxExclOfSTOnAllocAndSurrReductionYield_U = comm.roundUp_Level2(comm.getStringWithout_E((add1 + add2) * serviceTax));
    }

    public String getServiceTaxExclOfSTOnAllocAndSurrReductionYield_U() {
        return serviceTaxExclOfSTOnAllocAndSurrReductionYield_U;
    }

    public void setFundBeforeFMCReductionYield_X(double _fundValueAtEnd_AC, int policyTerm, double riderCharges_P) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {


            //            System.out.println("fund value "+_fundValueAtEnd_AC);
            //            System.out.println("calc "+(_fundValueAtEnd_AC+Double.parseDouble(getAmountAvailableForInvestment_N())-Double.parseDouble(getTotalCharges_T())+Double.parseDouble(getAdditionToFundIfAny_W())-Double.parseDouble(getServiceTaxExclOfSTOnAllocAndSurr_U())+riderCharges_P));
            //            System.out.println("calc "+comm.getStringWithout_E(_fundValueAtEnd_AC+Double.parseDouble(getAmountAvailableForInvestment_N())-Double.parseDouble(getTotalCharges_T())+Double.parseDouble(getAdditionToFundIfAny_W())-Double.parseDouble(getServiceTaxExclOfSTOnAllocAndSurr_U())+riderCharges_P));
            //this.fundBeforeFMC_X=comm.getRoundUp(comm.getRoundOffLevel2(Double.parseDouble(comm.getStringWithout_E(_fundValueAtEnd_AC+Double.parseDouble(getAmountAvailableForInvestment_N())-Double.parseDouble(getTotalCharges_T())+Double.parseDouble(getAdditionToFundIfAny_W())-Double.parseDouble(getServiceTaxExclOfSTOnAllocAndSurr_U())+riderCharges_P))));
            this.fundBeforeFMCReductionYield_X = "" + (_fundValueAtEnd_AC + Double.parseDouble(getAmountAvailableForInvestment_N()) - Double.parseDouble(getTotalCharges_T()) + Double.parseDouble(getAdditionToFundIfAnyReductionYield_W()) - Double.parseDouble(getServiceTaxExclOfSTOnAllocAndSurrReductionYield_U()) + riderCharges_P);
        } else {
            this.fundBeforeFMCReductionYield_X = "" + 0;
        }
    }

    public String getFundBeforeFMCReductionYield_X() {
        return fundBeforeFMCReductionYield_X;
    }

    public void setServiceTaxOnFMCReductionYield_Z(boolean fundManagementCharges, double serviceTax) {
        double a = 0;
        if (fundManagementCharges) {
            a = (Double.parseDouble(getFundManagementChargeReductionYield_Y()));
        } else {
            a = 0;
        }
        this.serviceTaxOnFMCReductionYield_Z = comm.roundUp_Level2(comm.getStringWithout_E(a * serviceTax));
    }

    public String getServiceTaxOnFMCReductionYield_Z() {
        return serviceTaxOnFMCReductionYield_Z;
    }

    public void setAdditionToFundIfAnyReductionYield_W(double _fundValueAtEnd_AC, int policyTerm, double int1, double riderCharges_P) {
        //        System.out.println("inside addition to fund if any reduction yield");
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            //             System.out.println("addition "+(_fundValueAtEnd_AC+Double.parseDouble(getAmountAvailableForInvestment_N())-Double.parseDouble(getTotalCharges_T())-Double.parseDouble(getServiceTaxExclOfSTOnAllocAndSurr_U())+riderCharges_P));
            //             System.out.println("power "+(comm.pow((1+int1),(double)1/12)-1));
            //             System.out.println("without roundup "+((_fundValueAtEnd_AC+Double.parseDouble(getAmountAvailableForInvestment_N())-Double.parseDouble(getTotalCharges_T())-Double.parseDouble(getServiceTaxExclOfSTOnAllocAndSurr_U())+riderCharges_P)*(comm.pow((1+int1),(double)1/12)-1)));


            //            System.out.println("calc "+(_fundValueAtEnd_AC+Double.parseDouble(getAmountAvailableForInvestment_N())-Double.parseDouble(getTotalCharges_T())-Double.parseDouble(getServiceTaxExclOfSTOnAllocAndSurrReductionYield_U())+riderCharges_P)*(comm.pow((1+int1),(double)1/12)-1));
            this.additionToFundIfAnyReductionYield_W = comm.roundUp_Level2(comm.getStringWithout_E((_fundValueAtEnd_AC + Double.parseDouble(getAmountAvailableForInvestment_N()) - Double.parseDouble(getTotalCharges_T()) - Double.parseDouble(getServiceTaxExclOfSTOnAllocAndSurrReductionYield_U()) + riderCharges_P) * (comm.pow((1 + int1), (double) 1 / 12) - 1)));
        } else {
            this.additionToFundIfAnyReductionYield_W = "" + 0;
        }
    }

    public String getAdditionToFundIfAnyReductionYield_W() {
        return additionToFundIfAnyReductionYield_W;
    }

    public void setFundManagementChargeReductionYield_Y(int policyTerm, String fundOption, double percentToBeInvested_EquityFund, double percentToBeInvested_BondFund, double percentToBeInvested_Top300Fund, double percentToBeInvested_EquityOptFund, double percentToBeInvested_GrowthFund, double percentToBeInvested_BalancedFund, double percentToBeInvested_MoneyMarketFund, double percentToBeInvested_BondOptimiserFund, double percentToBeInvested_PureFund, double percentToBeInvested_CorpBondFund) {
        double temp1 = 0, temp2 = 0;
        if (Integer.parseInt(getMonth_E()) == 1) {
            temp1 = SPW_prop.charge_Fund;
        } else {
            temp1 = 0;
        }
        double a = getCharge_FundRen(fundOption, percentToBeInvested_EquityFund, percentToBeInvested_BondFund, percentToBeInvested_Top300Fund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_MoneyMarketFund, percentToBeInvested_BondOptimiserFund, percentToBeInvested_PureFund, percentToBeInvested_CorpBondFund);
        temp2 = getCharge_FundRen(fundOption, percentToBeInvested_EquityFund, percentToBeInvested_BondFund, percentToBeInvested_Top300Fund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_MoneyMarketFund, percentToBeInvested_BondOptimiserFund, percentToBeInvested_PureFund, percentToBeInvested_CorpBondFund) / 12;
        //        System.out.println("ChargeFundRen "+a);
        //        System.out.println("temp 1 "+temp1);
        //        System.out.println("ChargeFundRen/12 "+temp2);
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.fundManagementChargeReductionYield_Y = comm.roundUp_Level2(comm.getStringWithout_E(Double.parseDouble(getFundBeforeFMCReductionYield_X()) * (temp1 + temp2)));
        } else {
            this.fundManagementChargeReductionYield_Y = "" + 0;
        }
    }

    public String getFundManagementChargeReductionYield_Y() {
        return fundManagementChargeReductionYield_Y;
    }

    public void setTotalServiceTaxReductionYield_V(boolean riderChargesYield, double riderCharges_P, double serviceTax) {
        double temp;
        if (riderChargesYield) {
            temp = riderCharges_P;
        } else {
            temp = 0;
        }
        this.totalServiceTaxReductionYield_V = comm.roundUp_Level2(comm.getStringWithout_E(Double.parseDouble(getServiceTaxOnAllocation_M()) + Double.parseDouble(getServiceTaxExclOfSTOnAllocAndSurrReductionYield_U()) + Double.parseDouble(getServiceTaxOnFMCReductionYield_Z()) + temp * serviceTax));
    }

    public String getTotalServiceTaxReductionYield_V() {
        return totalServiceTaxReductionYield_V;
    }

    public void setFundValueAfterFMCandBeforeGAReductionYield_AA(int policyTerm) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.fundValueAfterFMCBerforeGAReductionYield_AA = "" + (Double.parseDouble(getFundBeforeFMCReductionYield_X()) - Double.parseDouble(getFundManagementChargeReductionYield_Y()) - Double.parseDouble(getServiceTaxOnFMCReductionYield_Z()));
            //this.fundValueAfterFMCBerforeGA_AA= comm.getRoundOffLevel2(Double.parseDouble(comm.getStringWithout_E(Double.parseDouble(getFundBeforeFMC_X())-Double.parseDouble(getFundManagementCharge_Y())-Double.parseDouble(getServiceTaxOnFMC_Z()))));
        } else {
            this.fundValueAfterFMCBerforeGAReductionYield_AA = "" + 0;
        }
    }

    public String getFundValueAfterFMCandBeforeGAReductionYield_AA() {
        return fundValueAfterFMCBerforeGAReductionYield_AA;
    }

    public void setFundValueAtEndReductionYield_AC() {
        this.fundValueAtEndReductionYield_AC = "" + (Double.parseDouble(getFundValueAfterFMCandBeforeGAReductionYield_AA()) + Double.parseDouble(getGuaranteedAddition_AB()));
    }

    public String getFundValueAtEndReductionYield_AC() {
        return fundValueAtEndReductionYield_AC;
    }

    public void setSurrenderChargesReductionYield_AD(double effectivePremium, int PPT) {
        double min1 = Math.min(Double.parseDouble(getFundValueAtEndReductionYield_AC()), effectivePremium);
        double a = getSurrenderCharge(effectivePremium, PPT);
        this.surrenderChargesReductionYield_AD = comm.roundUp_Level2(comm.getStringWithout_E(Math.min((min1 * a), Double.parseDouble(getSurrenderCap_AX()))));
    }

    public String getSurrenderChargesReductionYield_AD() {
        return surrenderChargesReductionYield_AD;
    }

    public void setServiceTaxOnSurrenderChargesReductionYield_AE(boolean surrenderCharges, double serviceTax) {
        if (surrenderCharges) {
            this.serviceTaxOnSurrenderChargesReductionYield_AE = comm.getRoundUp_Level2(comm.getStringWithout_E(Double.parseDouble(getSurrenderChargesReductionYield_AD()) * serviceTax));
        } else {
            this.serviceTaxOnSurrenderChargesReductionYield_AE = "" + 0;
        }
    }

    public String getServiceTaxOnSurrenderChargesReductionYield_AE() {
        return serviceTaxOnSurrenderChargesReductionYield_AE;
    }

    public void setSurrenderValueReductionYield_AF() {
        this.surrenderValueReductionYield_AF = "" + (Double.parseDouble(getFundValueAtEndReductionYield_AC()) - Double.parseDouble(getSurrenderChargesReductionYield_AD()) - Double.parseDouble(getServiceTaxOnSurrenderChargesReductionYield_AE()));
    }

    public String getSurrenderValueReductionYield_AF() {
        return surrenderValueReductionYield_AF;
    }

    public void setDeathBenefitReductionYield_AG(int policyTerm, double sumAssured, String increasingCoverOption) {
        if (Integer.parseInt(getYear_F()) > policyTerm) {
            this.deathBenefitReductionYield_AG = "" + 0;
        } else {
            double a = (sumAssured * getIncreasingSA(increasingCoverOption));
            this.deathBenefitReductionYield_AG = "" + Math.max(Double.parseDouble(getOneHundredPercentOfCummulativePremium_AY()), Math.max(a, Double.parseDouble(getFundValueAtEndReductionYield_AC())));
        }
    }

    public String getDeathBenefitReductionYield_AG() {
        return deathBenefitReductionYield_AG;
    }

    public void setServiceTaxExclOfSTOnAllocAndSurrReductionYield_AK(boolean mortalityAndRiderCharges, boolean administrationCharges, double serviceTax) {
        double add1, add2;
        if (mortalityAndRiderCharges) {
            add1 = (Double.parseDouble(getMortalityCharges_AH()));
        } else {
            add1 = 0;
        }
        if (administrationCharges) {
            add2 = Double.parseDouble(getPolicyAdministrationCharge_Q());
        } else {
            add2 = 0;
        }
        this.serviceTaxExclOfSTOnAllocAndSurrReductionYield_AK = comm.roundUp_Level2(comm.getStringWithout_E((add1 + add2) * serviceTax));
    }

    public String getServiceTaxExclOfSTOnAllocAndSurrReductionYield_AK() {
        return serviceTaxExclOfSTOnAllocAndSurrReductionYield_AK;
    }

    public void setAdditionToFundIfAnyReductionYield_AM(double _fundValueAtEnd_AS, int policyTerm, double int2, double riderCharges_P) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            //System.out.println("addition "+(_fundValueAtEnd_AS+Double.parseDouble(getAmountAvailableForInvestment_N())-Double.parseDouble(getTotalCharges_AJ())-Double.parseDouble(getServiceTaxExclOfSTOnAllocAndSurr_AK())+riderCharges_P));
            //System.out.println("power "+(comm.pow((1+int2),(double)1/12)-1));
            //System.out.println("without roundup "+((_fundValueAtEnd_AS+Double.parseDouble(getAmountAvailableForInvestment_N())-Double.parseDouble(getTotalCharges_AJ())-Double.parseDouble(getServiceTaxExclOfSTOnAllocAndSurr_AK())+riderCharges_P)*(comm.pow((1+int2),(double)1/12)-1)));
            this.additionToFundIfAnyReductionYield_AM = comm.roundUp_Level2(comm.getStringWithout_E((_fundValueAtEnd_AS + Double.parseDouble(getAmountAvailableForInvestment_N()) - Double.parseDouble(getTotalCharges_AJ()) - Double.parseDouble(getServiceTaxExclOfSTOnAllocAndSurrReductionYield_AK()) + riderCharges_P) * (comm.pow((1 + int2), (double) 1 / 12) - 1)));
        } else {
            this.additionToFundIfAnyReductionYield_AM = "" + 0;
        }
    }

    public String getAdditionToFundIfAnyReductionYield_AM() {
        return additionToFundIfAnyReductionYield_AM;
    }

    public void setFundBeforeFMCReductionYield_AN(double _fundValueAtEnd_AS, int policyTerm, double riderCharges_P) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            //this.fundBeforeFMC_X=comm.getRoundUp(_fundValueAtEnd_AC+Double.parseDouble(getAmountAvailableForInvestment_N())-Double.parseDouble(getTotalCharges_T())+Double.parseDouble(getAdditionToFundIfAny_W())-Double.parseDouble(getServiceTaxExclOfSTOnAllocAndSurr_U())+riderCharges_P);
            this.fundBeforeFMCReductionYield_AN = "" + (_fundValueAtEnd_AS + Double.parseDouble(getAmountAvailableForInvestment_N()) - Double.parseDouble(getTotalCharges_AJ()) + Double.parseDouble(getAdditionToFundIfAnyReductionYield_AM()) - Double.parseDouble(getServiceTaxExclOfSTOnAllocAndSurrReductionYield_AK()) + riderCharges_P);
        } else {
            this.fundBeforeFMCReductionYield_AN = "" + 0;
        }
    }

    public String getFundBeforeFMCReductionYield_AN() {
        return fundBeforeFMCReductionYield_AN;
    }

    public void setFundManagementChargeReductionYield_AO(int policyTerm, String fundOption, double percentToBeInvested_EquityFund, double percentToBeInvested_BondFund, double percentToBeInvested_Top300Fund, double percentToBeInvested_EquityOptFund, double percentToBeInvested_GrowthFund, double percentToBeInvested_BalancedFund, double percentToBeInvested_MoneyMarketFund, double percentToBeInvested_BondOptimiserFund, double percentToBeInvested_PureFund, double percentToBeInvested_CorpBondFund) {
        double temp1 = 0, temp2 = 0;
        if (Integer.parseInt(getMonth_E()) == 1) {
            temp1 = SPW_prop.charge_Fund;
        } else {
            temp1 = 0;
        }
        double a = getCharge_FundRen(fundOption, percentToBeInvested_EquityFund, percentToBeInvested_BondFund, percentToBeInvested_Top300Fund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_MoneyMarketFund, percentToBeInvested_BondOptimiserFund, percentToBeInvested_PureFund, percentToBeInvested_CorpBondFund);
        temp2 = getCharge_FundRen(fundOption, percentToBeInvested_EquityFund, percentToBeInvested_BondFund, percentToBeInvested_Top300Fund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_MoneyMarketFund, percentToBeInvested_BondOptimiserFund, percentToBeInvested_PureFund, percentToBeInvested_CorpBondFund) / 12;
        //System.out.println("ChargeFundRen "+a);
        //System.out.println("ChargeFundRen/12 "+temp2);
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.fundManagementChargeReductionYield_AO = comm.roundUp_Level2(comm.getStringWithout_E(Double.parseDouble(getFundBeforeFMCReductionYield_AN()) * (temp1 + temp2)));
        } else {
            this.fundManagementChargeReductionYield_AO = "" + 0;
        }
    }

    public String getFundManagementChargeReductionYield_AO() {
        return fundManagementChargeReductionYield_AO;
    }

    public void setServiceTaxOnFMCReductionYield_AP(boolean fundManagementCharges, double serviceTax) {
        double a = 0;
        if (fundManagementCharges) {
            a = (Double.parseDouble(getFundManagementChargeReductionYield_AO()));
        } else {
            a = 0;
        }
        this.serviceTaxOnFMCReductionYield_AP = comm.roundUp_Level2(comm.getStringWithout_E(a * serviceTax));
    }

    public String getServiceTaxOnFMCReductionYield_AP() {
        return serviceTaxOnFMCReductionYield_AP;
    }

    public void setTotalServiceTaxReductionYield_AL(boolean riderCharges, double riderCharges_P, double serviceTax) {
        double temp;
        if (riderCharges) {
            temp = riderCharges_P;
        } else {
            temp = 0;
        }
        this.totalServiceTaxReductionYield_AL = comm.roundUp_Level2(comm.getStringWithout_E(Double.parseDouble(getServiceTaxOnAllocation_M()) + Double.parseDouble(getServiceTaxExclOfSTOnAllocAndSurrReductionYield_AK()) + Double.parseDouble(getServiceTaxOnFMCReductionYield_AP()) + temp * serviceTax));
    }

    public String getTotalServiceTaxReductionYield_AL() {
        return totalServiceTaxReductionYield_AL;
    }

    public void setFundValueAfterFMCandBeforeGAReductionYield_AQ(int policyTerm) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.fundValueAfterFMCBerforeGAReductionYield_AQ = "" + (Double.parseDouble(getFundBeforeFMCReductionYield_AN()) - Double.parseDouble(getFundManagementChargeReductionYield_AO()) - Double.parseDouble(getServiceTaxOnFMCReductionYield_AP()));
        } else {
            this.fundValueAfterFMCBerforeGAReductionYield_AQ = "" + 0;
        }
    }

    public String getFundValueAfterFMCandBeforeGAReductionYield_AQ() {
        return fundValueAfterFMCBerforeGAReductionYield_AQ;
    }

    public void setFundValueAtEndReductionYield_AS() {
        this.fundValueAtEndReductionYield_AS = "" + (Double.parseDouble(getFundValueAfterFMCandBeforeGAReductionYield_AQ()) + Double.parseDouble(getGuaranteedAddition_AR()));
    }

    public String getFundValueAtEndReductionYield_AS() {
        return fundValueAtEndReductionYield_AS;
    }

    public void setSurrenderChargesReductionYield_AT(double effectivePremium, int PPT) {
        double min1 = Math.min(Double.parseDouble(getFundValueAtEndReductionYield_AS()), effectivePremium);
        double a = getSurrenderCharge(effectivePremium, PPT);
        this.surrenderChargesReductionYield_AT = comm.roundUp_Level2(comm.getStringWithout_E(Math.min((min1 * a), Double.parseDouble(getSurrenderCap_AX()))));
    }

    public String getSurrenderChargesReductionYield_AT() {
        return surrenderChargesReductionYield_AT;
    }

    public void setServiceTaxOnSurrenderChargesReductionYield_AU(boolean surrenderCharges, double serviceTax) {
        if (surrenderCharges) {
            this.serviceTaxOnSurrenderChargesReductionYield_AU = comm.getRoundUp_Level2(comm.getStringWithout_E(Double.parseDouble(getSurrenderChargesReductionYield_AT()) * serviceTax));
        } else {
            this.serviceTaxOnSurrenderChargesReductionYield_AU = "" + 0;
        }
    }

    public String getServiceTaxOnSurrenderChargesReductionYield_AU() {
        return serviceTaxOnSurrenderChargesReductionYield_AU;
    }

    public void setSurrenderValueReductionYield_AV() {
        this.surrenderValueReductionYield_AV = "" + (Double.parseDouble(getFundValueAtEndReductionYield_AS()) - Double.parseDouble(getSurrenderChargesReductionYield_AT()) - Double.parseDouble(getServiceTaxOnSurrenderChargesReductionYield_AU()));
    }

    public String getSurrenderValueReductionYield_AV() {
        return surrenderValueReductionYield_AV;
    }

    public void setDeathBenefitReductionYield_AW(int policyTerm, double sumAssured, String increasingCoverOption) {
        if (Integer.parseInt(getYear_F()) > policyTerm) {
            this.deathBenefit_AW = "" + 0;
        } else {
            double a = (sumAssured * getIncreasingSA(increasingCoverOption));
            this.deathBenefitReductionYield_AW = "" + Math.max(Double.parseDouble(getOneHundredPercentOfCummulativePremium_AY()), Math.max(a, Double.parseDouble(getFundValueAtEndReductionYield_AS())));
        }
    }

    public String getDeathBenefitReductionYield_AW() {
        return deathBenefitReductionYield_AW;
    }

    public void setMonth_BO(int monthNumber) {
        this.month_BO = "" + monthNumber;
    }

    public String getMonth_BO() {
        return month_BO;
    }

    public void setReductionYield_BU(int noOfYearsElapsedSinceInception, double _fundValueAtEnd_AS) {
        double a, b;
        //     if((Integer.parseInt(getMonth_E())) <= (noOfYearsElapsedSinceInception*12)+1)
        if ((Integer.parseInt(getMonth_BO())) < (noOfYearsElapsedSinceInception * 12)) {
            a = -(Double.parseDouble(getPremium_I()));
        } else {
            a = 0;
        }

        if ((Integer.parseInt(getMonth_BO())) == (noOfYearsElapsedSinceInception * 12)) {
            b = _fundValueAtEnd_AS;
        } else {
            b = 0;
        }
        // System.out.println("a_BU "+a);
        // System.out.println("b_BU "+b);
        this.reductionYield_BU = "" + (a + b);
    }

    public String getReductionYield_BP() {
        return reductionYield_BP;
    }

    public void setReductionYield_BP(int policyTerm, double _fundValueAtEnd_AS) {
        double a, b;
        //     if((Integer.parseInt(getMonth_E())) <= (noOfYearsElapsedSinceInception*12)+1)
        if ((Integer.parseInt(getMonth_BO())) < (policyTerm * 12)) {
            a = -(Double.parseDouble(getPremium_I()));
        } else {
            a = 0;
        }

        if ((Integer.parseInt(getMonth_BO())) == (policyTerm * 12)) {
            b = _fundValueAtEnd_AS;
        } else {
            b = 0;
        }
        // System.out.println("a_BU "+a);
        // System.out.println("b_BU "+b);
        this.reductionYield_BP = "" + (a + b);
    }

    public String getReductionYield_BU() {
        return reductionYield_BU;
    }

    public void setReductionYield_BQ(int policyTerm, double _fundValueAtEnd_AS) {
        double a, b;
        if ((Integer.parseInt(getMonth_BO())) == (policyTerm * 12)) {
            //            System.out.println("inside if");
            //System.out.println("getFundValueAtEndReductionYield_AS "+getFundValueAtEndReductionYield_AS());
            a = _fundValueAtEnd_AS;
        } else {
            a = 0;
        }

        if ((Integer.parseInt(getMonth_BO())) < (policyTerm * 12)) {
            b = -(Double.parseDouble(getPremium_I()));
        } else {
            b = 0;
        }
        //        System.out.println("a_BQ" +a);
        //this.reductionYield_BQ=""+(Double.parseDouble(getPremium_I()) + a);
        this.reductionYield_BQ = "" + (b + a);
    }

    public String getReductionYield_BQ() {
        return reductionYield_BQ;
    }

    public void setIRRAnnual_BP(double ans) {
        //    	System.out.println("aaaaaaa "+((comm.pow((1+ans),12)-1)*100));
        this.irrAnnual_BP = "" + ((comm.pow((1 + ans), 12) - 1));
    }

    public String getIRRAnnual_BP() {
        return irrAnnual_BP;
    }


    public void setIRRAnnual_BQ(double ans) {
        //    	System.out.println("aaaaaaa "+((comm.pow((1+ans),12)-1)*100));
        this.irrAnnual_BQ = "" + ((comm.pow((1 + ans), 12) - 1));
    }

    public String getIRRAnnual_BQ() {
        return irrAnnual_BQ;
    }


    public void setIRRAnnual_BU(double ans) {
        //System.out.println("aaaaaaa "+((comm.pow((1+ans),12)-1)*100));
        this.irrAnnual_BU = "" + ((comm.pow((1 + ans), 12) - 1));
    }

    public String getIRRAnnual_BU() {
        return irrAnnual_BU;
    }

    public void setReductionInYieldMaturityAt(double int2) {
        this.reductionInYieldMaturityAt = "" + ((int2 - Double.parseDouble(getIRRAnnual_BQ())) * 100);
    }

    public String getReductionInYieldMaturityAt() {
        return reductionInYieldMaturityAt;
    }

    public void setReductionInYieldNumberOfYearsElapsedSinceInception(double int2) {
        this.reductionInYieldNumberOfYearsElapsedSinceInception = "" + ((int2 - Double.parseDouble(getIRRAnnual_BU())) * 100);
    }

    public String getReductionInYieldNumberOfYearsElapsedSinceInception() {
        return reductionInYieldNumberOfYearsElapsedSinceInception;
    }


    public String getnetYieldAt4Percent() {
        return netYieldAt4Percent;
    }

    public void setnetYieldAt4Percent() {
        this.netYieldAt4Percent = "" + Double.parseDouble(getIRRAnnual_BP()) * 100;
    }

    public String getnetYieldAt8Percent() {
        return netYieldAt8Percent;
    }

    public void setnetYieldAt8Percent() {
        this.netYieldAt8Percent = "" + Double.parseDouble(getIRRAnnual_BQ()) * 100;
    }

    public double irr(ArrayList<String> values, double guess) {
        int maxIterationCount = 20;
        double absoluteAccuracy = 1E-7;
        double[] arr = new double[values.size()];
        double x0 = guess;
        double x1;

        int i = 0;
        //       System.out.println("inside irr ");
        //System.out.println("values "+values);
        while (i < maxIterationCount) {

            // the value of the function (NPV) and its derivate can be calculated in the same loop
            double fValue = 0;
            double fDerivative = 0;
            for (int k = 0; k < values.size(); k++) {
                //System.out.println("value "+Double.parseDouble(values.get(k)));
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

}
