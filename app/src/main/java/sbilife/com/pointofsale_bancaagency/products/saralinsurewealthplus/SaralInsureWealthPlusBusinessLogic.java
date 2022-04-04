package sbilife.com.pointofsale_bancaagency.products.saralinsurewealthplus;

import java.text.DecimalFormat;
import java.util.ArrayList;

import sbilife.com.pointofsale_bancaagency.common.CommonForAllProd;

public class SaralInsureWealthPlusBusinessLogic {


    public CommonForAllProd cfap = null;
    public SaralInsureWealthPlusProperties prop = null;
    public SaralInsureWealthPlusBean saralInsureWealthPlusBean = null;

    double StaffRebate = 0;
    String month_E = null,
            year_F = null,
            age_H = null,
            policyInforce_G = "Y",
            premium_I = null,
            topUpPremium_J = null,
            premiumAllocationCharge_K = null,
            topUpCharges_L = null,
            serviceTaxOnAllocation_M = null,
            ServiceTaxOnAllocation_RIY = null,
            amountAvailableForInvestment_N = null,
            amountAvailableForInvestment_N1 = null,
            sumAssuredRelatedCharges_O = null,
            riderCharges_P = null,
            policyAdministrationCharge_Q = null,
            mortalityCharges_R = null,
            totalCharges_S = null,
            serviceTaxExclOfSTOnAllocAndSurr_T = null,
            totalServiceTax_U = null,
            additionToFundIfAny_V = null,
            fundBeforeFMC_W = null,
            fundManagementCharge_X = null,
            serviceTaxOnFMC_Y = null,
            fundValueAfterFMCBerforeGA_Z = null,
            guaranteedAddition_AA = null,
            fundValueAtEnd_AB = null,
            surrenderCharges_AC = null,
            serviceTaxOnSurrenderCharges_AD = null,
            surrenderValue_AE = null,
            deathBenefit_AF = null,
            mortalityCharges_AG = null,
            totalCharges_AH = null,
            serviceTaxExclOfSTOnAllocAndSurr_AI = null,
            totalServiceTax_AJ = null,
            additionToFundIfAny_AK = null,
            fundBeforeFMC_AL = null,
            fundManagementCharge_AM = null,
            serviceTaxOnFMC_AN = null,
            fundValueAfterFMCBerforeGA_AO = null,
            guaranteedAddition_AP = null,
            fundValueAtEnd_AQ = null,
            surrenderCharges_AR = null,
            serviceTaxOnSurrenderCharges_AS = null,
            surrenderValue_AT = null,
            deathBenefit_AU = null,
            surrenderCap_AV = null,
            oneHundredPercentOfCummulativePremium_AW = null,
            serviceTaxOnFMCReductionYield_Y = null,
            fundValueAfterFMCBerforeGAReductionYield_Z = null,
            totalServiceTaxReductionYield_U = null,
            fundValueAtEndReductionYield_AB = null,
            surrenderChargesReductionYield_AC = null,
            serviceTaxOnSurrenderChargesReductionYield_AD = null,
            surrenderValueReductionYield_AE = null,
            deathBenefitReductionYield_AF = null,
            serviceTaxOnFMCReductionYield_AN = null,
            fundValueAfterFMCBerforeGAReductionYield_AO = null,
            totalServiceTaxReductionYield_AJ = null,
            fundValueAtEndReductionYield_AQ = null,
            surrenderChargesReductionYield_AR = null,
            serviceTaxOnSurrenderChargesReductionYield_AS = null,
            surrenderValueReductionYield_AT = null,
            deathBenefitReductionYield_AU = null,
            month_BB = null,
            reductionYield_BI = null,
            reductionYield_BD = null,
            irrAnnual_BD = null,
            irrAnnual_BI = null,
            reductionInYieldMaturityAt = null,
            reductionInYieldNumberOfYearsElapsedSinceInception = null,
            TerminalAddition = null,
            AccTPDCharges = null,
            AccTPDCharges_8p = null,
            MortalityCharges_RIY = null, MortalityCharges_RIY8 = null,
            AccTPDCharges_RIY = null, AccTPDCharges_RIY8 = null,
            TotalCharges_RIY = null, TotalCharges_RIY8 = null,
            ServiceTax_exclOfSTonAllocAndSurr_RIY = null, ServiceTax_exclOfSTonAllocAndSurr_RIY8 = null,
            AdditionToFundIfAny_RIY = null, AdditionToFundIfAny_RIY8 = null,
            FundBeforeFMC_RIY = null, FundBeforeFMC_RIY8 = null,
            FundManagementCharge_RIY = null, FundManagementCharge_RIY8 = null,
            ServiceTaxOnFMC_RIY = null, ServiceTaxOnFMC_RIY8 = null,
            TotalServiceTax_RIY = null, TotalServiceTax_RIY8 = null,
            FundValueAfterFMCAndBeforeGA_RIY = null, FundValueAfterFMCAndBeforeGA_RIY8 = null,
            FundValueAtEnd_RIY = null, FundValueAtEnd_RIY8 = null,
            AmountAvailableForInvestment_RIY = null,
            SurrenderCharges_RIY = null, SurrenderCharges_RIY8 = null,
            ServiceTaxOnSurrenderCharges_RIY = null, ServiceTaxOnSurrenderCharges_RIY8 = null,
            SurrenderValue_RIY = null, SurrenderValue_RIY8 = null,
            DeathBenefit_RIY = null, DeathBenefit_RIY8 = null, GuaranteedAddition_RIY8 = null;

    public SaralInsureWealthPlusBusinessLogic() {
        cfap = new CommonForAllProd();
        prop = new SaralInsureWealthPlusProperties();
        saralInsureWealthPlusBean = new SaralInsureWealthPlusBean();
    }

    public void setMonth_E(int rowNumber) {
        this.month_E = ("" + rowNumber);
    }

    public String getMonth_E() {
        return month_E;
    }

    public void setYear_F() {
        this.year_F = cfap.getRoundUp("" + (Double.parseDouble(getMonth_E()) / 12));
    }

    public String getYear_F() {
        return year_F;
    }


    public double getServiceTax() {

        if ((saralInsureWealthPlusBean.isKerlaDisc() == true) && Integer.parseInt(getYear_F()) <= 2) {
            return 0.19;
        } else {
            return 0.18;
        }
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
            if ((Integer.parseInt(getYear_F()) <= PPT) && (((Integer.parseInt(getMonth_E()) - 1) % (12 / PF)) == 0)) {
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

//    public void setTopUpPremium_J(boolean topUp,double effectiveTopUpPrem)
//    {
//        if(getMonth_E().equals("1") && topUp )
//        {this.topUpPremium_J=(""+effectiveTopUpPrem);}
//        else
//        {this.topUpPremium_J=(""+0);}
//    }
//    public String getTopUpPremium_J()
//    {return topUpPremium_J;}

    public void setPremiumAllocationCharge_K(boolean staffDisc, boolean bancAssuranceDisc, String planType, int PPT, double effectivePremium) {


        {
            this.premiumAllocationCharge_K = cfap.getRoundUp(cfap.getRoundOffLevel2New_Saral(cfap.getStringWithout_E(getAllocationCharge(staffDisc, bancAssuranceDisc, planType, PPT, effectivePremium) * Double.parseDouble(getPremium_I()))));
        }
    }

    public String getPremiumAllocationCharge_K() {
        return premiumAllocationCharge_K;
    }

    public double getAllocationCharge(boolean staffDisc, boolean bancAssuranceDisc, String planType, int PPT, double effectivePremium) {
        double temp = 0;
        int yearFactor = 0;
        if (getYear_F().equals("1")) {
            yearFactor = 1;
        } else {
            yearFactor = 0;
        }
        if (planType.equals("Regular")) {
            if (getYear_F().equals("1")) {
                if (staffDisc == true && bancAssuranceDisc == false) {
                    temp = 0.08 - 0.025;
                } else if (staffDisc == false && bancAssuranceDisc == true) {
                    temp = 0.08 - 0.025;
                } else {
                    temp = 0.08;
                }
                return Math.max(temp, 0);
            } else if (getYear_F().equals("2")) {
                if (staffDisc == true && bancAssuranceDisc == false) {
                    temp = 0.055 - 0.025;
                } else if (staffDisc == false && bancAssuranceDisc == true) {
                    temp = 0.055 - 0.025;
                } else {
                    temp = 0.055;
                }
                return Math.max(temp, 0);
            } else if (getYear_F().equals("3")) {
                if (staffDisc == true && bancAssuranceDisc == false) {
                    temp = 0.055 - 0.025;
                } else if (staffDisc == false && bancAssuranceDisc == true) {
                    temp = 0.055 - 0.025;
                } else {
                    temp = 0.055;
                }
                return Math.max(temp, 0);
            } else if (getYear_F().equals("4")) {
                if (staffDisc == true && bancAssuranceDisc == false) {
                    temp = 0.055 - 0.025;
                } else if (staffDisc == false && bancAssuranceDisc == true) {
                    temp = 0.055 - 0.025;
                } else {
                    temp = 0.055;
                }
                return Math.max(temp, 0);
            } else if (getYear_F().equals("5")) {
                if (staffDisc == true && bancAssuranceDisc == false) {
                    temp = 0.055 - 0.025;
                } else if (staffDisc == false && bancAssuranceDisc == true) {
                    temp = 0.055 - 0.025;
                } else {
                    temp = 0.055;
                }
                return Math.max(temp, 0);
            } else if (getYear_F().equals("6")) {
                if (staffDisc == true && bancAssuranceDisc == false) {
                    temp = 0.035 - 0.025;
                } else if (staffDisc == false && bancAssuranceDisc == true) {
                    temp = 0.035 - 0.025;
                } else {
                    temp = 0.035;
                }
                return Math.max(temp, 0);
            } else if (getYear_F().equals("7")) {
                if (staffDisc == true && bancAssuranceDisc == false) {
                    temp = 0.035 - 0.025;
                } else if (staffDisc == false && bancAssuranceDisc == true) {
                    temp = 0.035 - 0.025;
                } else {
                    temp = 0.035;
                }
                return Math.max(temp, 0);
            } else if (getYear_F().equals("8")) {
                if (staffDisc == true && bancAssuranceDisc == false) {
                    temp = 0.035 - 0.025;
                } else if (staffDisc == false && bancAssuranceDisc == true) {
                    temp = 0.035 - 0.025;
                } else {
                    temp = 0.035;
                }
                return Math.max(temp, 0);
            } else if (getYear_F().equals("9")) {
                if (staffDisc == true && bancAssuranceDisc == false) {
                    temp = 0.035 - 0.025;
                } else if (staffDisc == false && bancAssuranceDisc == true) {
                    temp = 0.035 - 0.025;
                } else {
                    temp = 0.035;
                }
                return Math.max(temp, 0);
            } else if (getYear_F().equals("10")) {
                if (staffDisc == true && bancAssuranceDisc == false) {
                    temp = 0.035 - 0.025;
                } else if (staffDisc == false && bancAssuranceDisc == true) {
                    temp = 0.035 - 0.025;
                } else {
                    temp = 0.035;
                }
                return Math.max(temp, 0);
            } else {

                if (staffDisc == true && bancAssuranceDisc == false) {
                    temp = 0.03 - 0.025;
                } else if (staffDisc == false && bancAssuranceDisc == true) {
                    temp = 0.03 - 0.025;
                } else {
                    temp = 0.03;
                }
                return Math.max(temp, 0);

            }


        } else {
            return 0;
        }
        //For Single Mode of Premium Freqency


    }


    public void setServiceTaxOnAllocation_M(boolean allocationCharges, double serviceTax) {
        if (allocationCharges) {
            //this.serviceTaxOnAllocation_M=cfap.roundUp_Level2(cfap.getStringWithout_E(Double.parseDouble(getPremiumAllocationCharge_K())*serviceTax));
            this.serviceTaxOnAllocation_M = cfap.getRoundOffLevel2New_Saral(cfap.getStringWithout_E(Double.parseDouble(getPremiumAllocationCharge_K()) * serviceTax));
        } else {
            this.serviceTaxOnAllocation_M = ("" + 0);
        }
    }

    public String getServiceTaxOnAllocation_M() {
        return serviceTaxOnAllocation_M;
    }


    public void setServiceTaxOnAllocation_RIY(boolean allocationCharges, double serviceTax) {
        if (allocationCharges) {
            //this.serviceTaxOnAllocation_M=cfap.roundUp_Level2(cfap.getStringWithout_E(Double.parseDouble(getPremiumAllocationCharge_K())*serviceTax));
            this.ServiceTaxOnAllocation_RIY = cfap.getRoundOffLevel2New_Saral(cfap.getStringWithout_E(Double.parseDouble(getPremiumAllocationCharge_K()) * serviceTax));
        } else {
            this.ServiceTaxOnAllocation_RIY = ("" + 0);
        }
    }

    public String getServiceTaxOnAllocation_RIY() {
        return ServiceTaxOnAllocation_RIY;
    }


    public void setAmountAvailableForInvestment_N() {
        //this.amountAvailableForInvestment_N=cfap.roundUp_Level2(""+(Double.parseDouble(getPremium_I()) - Double.parseDouble(getPremiumAllocationCharge_K())-Double.parseDouble(getServiceTaxOnAllocation_M())));
        this.amountAvailableForInvestment_N = cfap.getRoundOffLevel2New_Saral(cfap.getStringWithout_E(Double.parseDouble(getPremium_I()) - Double.parseDouble(getPremiumAllocationCharge_K()) - Double.parseDouble(getServiceTaxOnAllocation_M())));
    }

    public String getAmountAvailableForInvestment_N() {
        return amountAvailableForInvestment_N;
    }

    public void setAmountAvailableForInvestment_N1() {
        this.amountAvailableForInvestment_N1 = cfap.roundUp_Level2("" + (Double.parseDouble(getPremium_I()) - Double.parseDouble(getPremiumAllocationCharge_K())));
    }

    public String getAmountAvailableForInvestment_N1() {
        return amountAvailableForInvestment_N1;
    }

    public void setAmountAvailableForInvestment_RIY() {
        //this.amountAvailableForInvestment_N=cfap.roundUp_Level2(""+(Double.parseDouble(getPremium_I()) - Double.parseDouble(getPremiumAllocationCharge_K())-Double.parseDouble(getServiceTaxOnAllocation_M())));
        this.AmountAvailableForInvestment_RIY = cfap.getRoundOffLevel2New_Saral(cfap.getStringWithout_E(Double.parseDouble(getPremium_I()) - Double.parseDouble(getPremiumAllocationCharge_K()) - Double.parseDouble(getServiceTaxOnAllocation_RIY())));
    }

    public String getAmountAvailableForInvestment_RIY() {
        return AmountAvailableForInvestment_RIY;
    }

//    public void setSumAssuredRelatedCharges_O(int policyTerm,double sumAssured,double charge_SumAssuredBase)
//    {
//        double a=0;
//        double b=0;
//        if(getMonth_E().equals("1"))
//        {a=charge_SumAssuredBase;}
//        else
//        {a=0;}
//        if(Integer.parseInt(getYear_F()) <= policyTerm)
//        {b=prop.charge_SA_ren;}
//        else
//        {b=0;}
//        this.sumAssuredRelatedCharges_O = cfap.roundUp_Level2(cfap.getStringWithout_E(sumAssured*(a+b)));
//    }
//    public String getSumAssuredRelatedCharges_O()
//    {return sumAssuredRelatedCharges_O;}

    public void setPolicyAdministrationCharge_Q(double _policyAdministrationCharge_Q, int charge_Inflation, int fixedMonthlyExp_SP, int fixedMonthlyExp_RP, int inflation_pa_SP, int inflation_pa_RP, String planType) {
        if (planType.equals("Regular")) {
            if (getPolicyInForce_G().equals("Y")) {
                if (Integer.parseInt(getYear_F()) <= 5) {
                    policyAdministrationCharge_Q = "" + 0;
                } else {
                    policyAdministrationCharge_Q = cfap.getStringWithout_E(150);

                }
            } else {
                this.policyAdministrationCharge_Q = "" + 0;
            }
        } else {
            this.policyAdministrationCharge_Q = "" + 0;
        }
    }

    public String getPolicyAdministrationCharge_Q() {
        return policyAdministrationCharge_Q;
    }

    public int getCharge_PP_Ren(int fixedMonthlyExp_SP, int fixedMonthlyExp_RP, int inflation_pa_SP, int inflation_pa_RP, String premFreqMode) {
        if (premFreqMode.equals("Single")) {
            return (fixedMonthlyExp_SP * 12 * (1 + inflation_pa_SP) ^ 0);
        }
        //For Regular
        else {
            return (fixedMonthlyExp_RP * 12 * (1 + inflation_pa_RP) ^ 0);
        }
    }

    public void setOneHundredPercentOfCummulativePremium_AW(double sum_I) {
        double a = 0;
        if (getPolicyInForce_G().equals("Y"))
        //{a=Double.parseDouble(getPremium_I())+(_oneHundredPercentOfCummulativePremium_AW/1.05);}
        {
            a = Double.parseDouble(getPremium_I());
        } else {
            a = 0;
        }
        //this.oneHundredPercentOfCummulativePremium_AW=""+(a*1.05);

        this.oneHundredPercentOfCummulativePremium_AW = "" + (sum_I * 1.05);
    }

    public String getOneHundredPercentOfCummulativePremium_AW() {
        return oneHundredPercentOfCummulativePremium_AW;
    }

    public String[] getForBIArr(double mortalityCharges_AsPercentOfofLICtable) {
        int[] ageArr = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100};
        String[] strArrTReturn = new String[99];
        SaralInsureWealthPlusDB saralInvestPlusDB = new SaralInsureWealthPlusDB();
        for (int i = 0; i < saralInvestPlusDB.getIAIarray().length - 2; i++) {
            //System.out.println("saralInvestPlusDB.getIAIarray()[i] "+saralInvestPlusDB.getIAIarray()[i]);
            //System.out.println("saralInvestPlusDB.getIAIarray()[i+1] "+saralInvestPlusDB.getIAIarray()[i+1]);
            strArrTReturn[i] = String.valueOf(((saralInvestPlusDB.getIAIarray()[i] + saralInvestPlusDB.getIAIarray()[i + 1]) / 2) * mortalityCharges_AsPercentOfofLICtable);

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

    public void setMortalityCharges_R(double _fundValueAtEnd_AB, int policyTerm, String[] forBIArray, double sumAssured, boolean mortalityCharges, int ageAtEntry) {
        if ((ageAtEntry < 8) && (Integer.parseInt(getMonth_E()) < 24)) {
            this.mortalityCharges_R = "" + 0;
        } else {
            if (!(getPolicyInForce_G().equals("Y")) || Integer.parseInt(getYear_F()) > policyTerm) {
                this.mortalityCharges_R = "" + 0;
            } else {
                double arrOutput = Double.parseDouble(forBIArray[Integer.parseInt(getAge_H())]);
                DecimalFormat df = new DecimalFormat("#.#######");
//             String aa =cfap.getStringWithout_E(arrOutput);

                String roundTo7 = df.format(arrOutput);
                String test = cfap.getRoundOffLevel5(roundTo7);
                String str1 = cfap.getStringWithout_E(arrOutput);
//            String str2=(test);

//            System.out.println("arroutput RY "+arrOutput);
//            System.out.println("str1 RY"+str1);
//            System.out.println("round to 5 RY"+test);
//            System.out.println("str2 RY"+str2);
//            double div=arrOutput/1000;
//            System.out.println("div "+div);
                double a = Double.parseDouble(test) / 12;
                String str3 = cfap.getStringWithout_E(a);
//            System.out.println("str3 "+str3);
//            System.out.println("a RY"+a);

//            System.out.println("getOneHundredPercentOfCummulativePremium_AW "+Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()));

                double max1 = Math.max(sumAssured, Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()));
//            System.out.println("max1 RY"+max1);
                double b = (max1 - (Double.parseDouble(getAmountAvailableForInvestment_N()) + _fundValueAtEnd_AB));
//            System.out.println("b RY"+b);

                int c = 0;
                if (mortalityCharges) {
                    c = 1;
                } else {
                    c = 0;
                }
                //this.mortalityCharges_R= cfap.roundUp_Level2(cfap.getStringWithout_E(((a*Math.max(b, 0))*c)));
                this.mortalityCharges_R = cfap.getRoundOffLevel2New_Saral(cfap.getStringWithout_E(((a * Math.max(b, 0)) * c)));
            }
        }
    }

    public String getMortalityCharges_R() {
        return mortalityCharges_R;
    }


    public void setMortalityCharges_RIY(double _fundValueAtEnd_AB, int policyTerm, String[] forBIArray, double sumAssured, boolean mortalityCharges) {
        if (!(getPolicyInForce_G().equals("Y")) || Integer.parseInt(getYear_F()) > policyTerm) {
            this.MortalityCharges_RIY = "" + 0;
        } else {

            DecimalFormat df = new DecimalFormat("#.#######");
            //df.setRoundingMode(RoundingMode.CEILING);


            double arrOutput = Double.parseDouble(forBIArray[Integer.parseInt(getAge_H())]);
            String roundTo7 = df.format(arrOutput);

            String test = cfap.getRoundOffLevel5(roundTo7);
            String str1 = cfap.getStringWithout_E(arrOutput);
            //String str2=cfap.getStringWithout_E(test);

//            System.out.println("arroutput RY "+arrOutput);
//            System.out.println("str1 RY"+str1);
//            System.out.println("round to 5 RY"+test);
//            System.out.println("str2 RY"+str2);
            //double div=arrOutput/1000;
            //System.out.println("div "+div);
            double a = Double.parseDouble(test) / 12;
//            System.out.println("a RY"+a);

//            System.out.println("getOneHundredPercentOfCummulativePremium_AW "+Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()));

            double max1 = Math.max(sumAssured, Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()));
//            System.out.println("max1 RY"+max1);
            double b = (max1 - (Double.parseDouble(getAmountAvailableForInvestment_N()) + _fundValueAtEnd_AB));
//            System.out.println("b RY"+b);

            int c = 0;
            if (mortalityCharges) {
                c = 1;
            } else {
                c = 0;
            }
            //this.mortalityCharges_R= cfap.roundUp_Level2(cfap.getStringWithout_E(((a*Math.max(b, 0))*c)));
            this.MortalityCharges_RIY = cfap.getRoundOffLevel2New_Saral(cfap.getStringWithout_E(((a * Math.max(b, 0)) * c)));
        }
    }

    public String getMortalityCharges_RIY() {
        return MortalityCharges_RIY;
    }


    public void setMortalityCharges_RIY8(double _fundValueAtEnd_AB, int policyTerm, String[] forBIArray, double sumAssured, boolean mortalityCharges) {
        if (!(getPolicyInForce_G().equals("Y")) || Integer.parseInt(getYear_F()) > policyTerm) {
            this.MortalityCharges_RIY8 = "" + 0;
        } else {

            DecimalFormat df = new DecimalFormat("#.#######");
            //df.setRoundingMode(RoundingMode.CEILING);


            double arrOutput = Double.parseDouble(forBIArray[Integer.parseInt(getAge_H())]);
            String roundTo7 = df.format(arrOutput);
            String test = cfap.getRoundOffLevel5(roundTo7);
            String str1 = cfap.getStringWithout_E(arrOutput);
            //String str2=cfap.getStringWithout_E(test);

         /*   System.out.println("arroutput RY "+arrOutput);
            System.out.println("str1 RY" + str1);
            System.out.println("round to 5 RY" + test);
            System.out.println("str2 RY"+str2);*/
            //double div=arrOutput/1000;
            //System.out.println("div "+div);
            double a = Double.parseDouble(test) / 12;
//            System.out.println("a RY"+a);

//            System.out.println("getOneHundredPercentOfCummulativePremium_AW "+Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()));

            double max1 = Math.max(sumAssured, Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()));
//            System.out.println("max1 RY"+max1);
            double b = (max1 - (Double.parseDouble(getAmountAvailableForInvestment_N()) + _fundValueAtEnd_AB));
//            System.out.println("b RY"+b);

            int c = 0;
            if (mortalityCharges) {
                c = 1;
            } else {
                c = 0;
            }
            //this.mortalityCharges_R= cfap.roundUp_Level2(cfap.getStringWithout_E(((a*Math.max(b, 0))*c)));
            this.MortalityCharges_RIY8 = cfap.getRoundOffLevel2New_Saral(cfap.getStringWithout_E(((a * Math.max(b, 0)) * c)));
        }
    }

    public String getMortalityCharges_RIY8() {
        return MortalityCharges_RIY8;
    }

    public void setAccTPDCharges(double _fundValueAtEnd_AB, int policyTerm, double acc_tpd_charges, double sumAssured, boolean mortalityCharges) {
        if (!(getPolicyInForce_G().equals("Y")) || Integer.parseInt(getYear_F()) > policyTerm) {
            this.mortalityCharges_R = "" + 0;
        } else {

            DecimalFormat df = new DecimalFormat("#.#####");
            //df.setRoundingMode(RoundingMode.CEILING);
            double a = 0;

            if (Integer.parseInt(getYear_F()) == 0) {
                a = 0;
            } else {
                a = acc_tpd_charges / 12000;
            }

//            System.out.println("a RY"+a);

//            System.out.println("getOneHundredPercentOfCummulativePremium_AW "+Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()));

            double max1 = Math.max(sumAssured, Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()));
//            System.out.println("max1 RY"+max1);
            double b = (max1 - (Double.parseDouble(getAmountAvailableForInvestment_N()) + _fundValueAtEnd_AB));
//            System.out.println("b RY"+b);

            int c = 0;
            if (mortalityCharges) {
                c = 1;
            } else {
                c = 0;
            }
            //this.mortalityCharges_R= cfap.roundUp_Level2(cfap.getStringWithout_E(((a*Math.max(b, 0))*c)));
            this.AccTPDCharges = cfap.getRoundOffLevel2New_Saral(cfap.getStringWithout_E(((a * Math.max(b, 0)) * c)));
        }
    }

    public String getAccTPDCharges() {
        return AccTPDCharges;
    }


    public void setAccTPDCharges_RIY(double _fundValueAtEnd_AB, int policyTerm, double acc_tpd_charges, double sumAssured, boolean mortalityCharges) {
        if (!(getPolicyInForce_G().equals("Y")) || Integer.parseInt(getYear_F()) > policyTerm) {
            this.AccTPDCharges_RIY = "" + 0;
        } else {

            DecimalFormat df = new DecimalFormat("#.#####");
            //df.setRoundingMode(RoundingMode.CEILING);
            double a = 0;

            if (Integer.parseInt(getYear_F()) == 0) {
                a = 0;
            } else {
                a = acc_tpd_charges / 12000;
            }

//            System.out.println("a RY"+a);

//            System.out.println("getOneHundredPercentOfCummulativePremium_AW_RIY "+Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()));

            double max1 = Math.max(sumAssured, Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()));
//            System.out.println("max1 RIY"+max1);
            double b = (max1 - (Double.parseDouble(getAmountAvailableForInvestment_N()) + _fundValueAtEnd_AB));
//            System.out.println("b RIY"+b);

            int c = 0;
            if (mortalityCharges) {
                c = 1;
            } else {
                c = 0;
            }
            //this.mortalityCharges_R= cfap.roundUp_Level2(cfap.getStringWithout_E(((a*Math.max(b, 0))*c)));
            this.AccTPDCharges_RIY = cfap.getRoundOffLevel2New_Saral(cfap.getStringWithout_E(((a * Math.max(b, 0)) * c)));
        }
    }

    public String getAccTPDCharges_RIY() {
        return AccTPDCharges_RIY;
    }


    public void setAccTPDCharges_RIY8(double _fundValueAtEnd_AB, int policyTerm, double acc_tpd_charges, double sumAssured, boolean mortalityCharges) {
        if (!(getPolicyInForce_G().equals("Y")) || Integer.parseInt(getYear_F()) > policyTerm) {
            this.AccTPDCharges_RIY8 = "" + 0;
        } else {

            DecimalFormat df = new DecimalFormat("#.#####");
            //df.setRoundingMode(RoundingMode.CEILING);
            double a = 0;

            if (Integer.parseInt(getYear_F()) == 0) {
                a = 0;
            } else {
                a = acc_tpd_charges / 12000;
            }

//            System.out.println("a RY"+a);

//            System.out.println("getOneHundredPercentOfCummulativePremium_AW_RIY "+Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()));

            double max1 = Math.max(sumAssured, Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()));
//            System.out.println("max1 RIY"+max1);
            double b = (max1 - (Double.parseDouble(getAmountAvailableForInvestment_N()) + _fundValueAtEnd_AB));
//            System.out.println("b RIY"+b);

            int c = 0;
            if (mortalityCharges) {
                c = 1;
            } else {
                c = 0;
            }
            //this.mortalityCharges_R= cfap.roundUp_Level2(cfap.getStringWithout_E(((a*Math.max(b, 0))*c)));
            this.AccTPDCharges_RIY8 = cfap.getRoundOffLevel2New_Saral(cfap.getStringWithout_E(((a * Math.max(b, 0)) * c)));
        }
    }

    public String getAccTPDCharges_RIY8() {
        return AccTPDCharges_RIY8;
    }


    public void setAccTPDCharges_8p(double _fundValueAtEnd_AQ, int policyTerm, double acc_tpd_charges, double sumAssured, boolean mortalityCharges) {
        if (!(getPolicyInForce_G().equals("Y")) || Integer.parseInt(getYear_F()) > policyTerm) {
            this.mortalityCharges_R = "" + 0;
        } else {

            DecimalFormat df = new DecimalFormat("#.#####");
            //df.setRoundingMode(RoundingMode.CEILING);
            double a = 0;

            if (Integer.parseInt(getYear_F()) == 0) {
                a = 0;
            } else {
                a = acc_tpd_charges / 12000;
            }

//            System.out.println("a RY"+a);

//            System.out.println("getOneHundredPercentOfCummulativePremium_AW "+Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()));

            double max1 = Math.max(sumAssured, Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()));
//            System.out.println("max1 RY"+max1);
            double b = (max1 - (Double.parseDouble(getAmountAvailableForInvestment_N()) + _fundValueAtEnd_AQ));
//            System.out.println("b RY"+b);

            int c = 0;
            if (mortalityCharges) {
                c = 1;
            } else {
                c = 0;
            }
            //this.mortalityCharges_R= cfap.roundUp_Level2(cfap.getStringWithout_E(((a*Math.max(b, 0))*c)));
            this.AccTPDCharges_8p = cfap.getRoundOffLevel2New_Saral(cfap.getStringWithout_E(((a * Math.max(b, 0)) * c)));
        }
    }

    public String getAccTPDCharges_8p() {
        return AccTPDCharges_8p;
    }


    public void setTotalCharges_S(int policyTerm, double riderCharges) {


        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.totalCharges_S = "" + (Double.parseDouble(getAccTPDCharges()) + Double.parseDouble(getPolicyAdministrationCharge_Q()) + Double.parseDouble(getMortalityCharges_R()) + riderCharges);
        } else {
            this.totalCharges_S = "" + 0;
        }
    }

    public String getTotalCharges_S() {
        return totalCharges_S;
    }


    public void setTotalCharges_RIY(int policyTerm, double riderCharges) {


        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.TotalCharges_RIY = "" + (Double.parseDouble(getAccTPDCharges_RIY()) + Double.parseDouble(getPolicyAdministrationCharge_Q()) + Double.parseDouble(getMortalityCharges_RIY()) + riderCharges);
        } else {
            this.TotalCharges_RIY = "" + 0;
        }
    }

    public String getTotalCharges_RIY() {
        return TotalCharges_RIY;
    }


    public void setTotalCharges_RIY8(int policyTerm, double riderCharges) {


        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.TotalCharges_RIY8 = "" + (Double.parseDouble(getAccTPDCharges_RIY()) + Double.parseDouble(getPolicyAdministrationCharge_Q()) + Double.parseDouble(getMortalityCharges_RIY8()) + riderCharges);
        } else {
            this.TotalCharges_RIY8 = "" + 0;
        }
    }

    public String getTotalCharges_RIY8() {
        return TotalCharges_RIY8;
    }


    public void setServiceTax_exclOfSTonAllocAndSurr_T(double serviceTax, boolean mortalityAndRiderCharges, boolean administrationCharges) {
        double a = 0, b = 0;
        if (mortalityAndRiderCharges) {
            a = Double.parseDouble(getMortalityCharges_R()) + Double.parseDouble(getAccTPDCharges());
        } else {
            a = 0;
        }
        if (administrationCharges) {
            b = Double.parseDouble(getPolicyAdministrationCharge_Q());
        } else {
            b = 0;
        }
        this.serviceTaxExclOfSTOnAllocAndSurr_T = cfap.getRoundOffLevel2New_Saral(cfap.getStringWithout_E((a + b) * serviceTax));
    }

    public String getServiceTax_exclOfSTonAllocAndSurr_T() {
        return serviceTaxExclOfSTOnAllocAndSurr_T;
    }


    public void setServiceTax_exclOfSTonAllocAndSurr_RIY(double serviceTax, boolean mortalityAndRiderCharges, boolean administrationCharges) {
        double a = 0, b = 0;
        if (mortalityAndRiderCharges) {
            a = Double.parseDouble(getMortalityCharges_RIY()) + Double.parseDouble(getAccTPDCharges_RIY());
        } else {
            a = 0;
        }
        if (administrationCharges) {
            b = Double.parseDouble(getPolicyAdministrationCharge_Q());
        } else {
            b = 0;
        }
        this.ServiceTax_exclOfSTonAllocAndSurr_RIY = cfap.getRoundOffLevel2New_Saral(cfap.getStringWithout_E((a + b) * serviceTax));
    }

    public String getServiceTax_exclOfSTonAllocAndSurr_RIY() {
        return ServiceTax_exclOfSTonAllocAndSurr_RIY;
    }


    public void setServiceTax_exclOfSTonAllocAndSurr_RIY8(double serviceTax, boolean mortalityAndRiderCharges, boolean administrationCharges) {
        double a = 0, b = 0;
        if (mortalityAndRiderCharges) {
            a = Double.parseDouble(getMortalityCharges_RIY8()) + Double.parseDouble(getAccTPDCharges_RIY8());
        } else {
            a = 0;
        }
        if (administrationCharges) {
            b = Double.parseDouble(getPolicyAdministrationCharge_Q());
        } else {
            b = 0;
        }
        this.ServiceTax_exclOfSTonAllocAndSurr_RIY8 = cfap.getRoundOffLevel2New_Saral(cfap.getStringWithout_E((a + b) * serviceTax));
    }

    public String getServiceTax_exclOfSTonAllocAndSurr_RIY8() {
        return ServiceTax_exclOfSTonAllocAndSurr_RIY8;
    }

    public void setAdditionToFundIfAny_V(double _fundValueAtEnd_AB, int policyTerm, double riderCharges) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            double temp1 = 0, temp2 = 0;
            temp1 = (_fundValueAtEnd_AB + Double.parseDouble(getAmountAvailableForInvestment_N()) - Double.parseDouble(getTotalCharges_S()) - Double.parseDouble(getServiceTax_exclOfSTonAllocAndSurr_T()) + riderCharges);
            double a = (double) 1 / 12;
            temp2 = (cfap.pow((1 + prop.int1), a)) - 1;
//            this.additionToFundIfAny_V= cfap.roundUp_Level2( cfap.getStringWithout_E(temp1*temp2));
            this.additionToFundIfAny_V = cfap.getRoundOffLevel2New_Saral(cfap.getStringWithout_E(temp1 * temp2));

//            		System.out.println("additionToFundIfAny_V RIY   "  +additionToFundIfAny_V);
        } else {
            this.additionToFundIfAny_V = "" + 0;
        }
    }

    public String getAdditionToFundIfAny_V() {
        return additionToFundIfAny_V;
    }

    public void setFundBeforeFMC_W(double _fundValueAtEnd_AB, int policyTerm, double riderCharges) {

    	/*System.out.println("_fundValueAtEnd_AB "+_fundValueAtEnd_AB);
    	System.out.println("getAmountAvailableForInvestment_N() "+getAmountAvailableForInvestment_N());
    	System.out.println("getAdditionToFundIfAny_V() "+getAdditionToFundIfAny_V());
    	System.out.println("getTotalCharges_S() "+getTotalCharges_S());
    	System.out.println("getServiceTax_exclOfSTonAllocAndSurr_T() "+getServiceTax_exclOfSTonAllocAndSurr_T());
    	System.out.println("riderCharges "+riderCharges);*/

        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.fundBeforeFMC_W = cfap.getStringWithout_E(_fundValueAtEnd_AB + Double.parseDouble(getAmountAvailableForInvestment_N()) + Double.parseDouble(getAdditionToFundIfAny_V()) - Double.parseDouble(getTotalCharges_S()) - Double.parseDouble(getServiceTax_exclOfSTonAllocAndSurr_T()) + riderCharges);
        } else {
            this.fundBeforeFMC_W = "" + 0;
        }
    }

    public String getFundBeforeFMC_W() {
        return fundBeforeFMC_W;
    }


    public void setFundBeforeFMC_RIY(double _fundValueAtEnd_AB, int policyTerm, double riderCharges) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.FundBeforeFMC_RIY = cfap.getStringWithout_E(_fundValueAtEnd_AB + Double.parseDouble(getAmountAvailableForInvestment_RIY()) + Double.parseDouble(getAdditionToFundIfAny_RIY()) - Double.parseDouble(getTotalCharges_RIY()) - Double.parseDouble(getServiceTax_exclOfSTonAllocAndSurr_RIY()) + riderCharges);
        } else {
            this.FundBeforeFMC_RIY = "" + 0;
        }
    }

    public String getFundBeforeFMC_RIY() {
        return FundBeforeFMC_RIY;
    }


    public void setFundBeforeFMC_RIY8(double _fundValueAtEnd_AB, int policyTerm, double riderCharges) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.FundBeforeFMC_RIY8 = cfap.getStringWithout_E(_fundValueAtEnd_AB + Double.parseDouble(getAmountAvailableForInvestment_RIY()) + Double.parseDouble(getAdditionToFundIfAny_RIY8()) - Double.parseDouble(getTotalCharges_RIY8()) - Double.parseDouble(getServiceTax_exclOfSTonAllocAndSurr_RIY8()) + riderCharges);
        } else {
            this.FundBeforeFMC_RIY8 = "" + 0;
        }
    }

    public String getFundBeforeFMC_RIY8() {
        return FundBeforeFMC_RIY8;
    }

    public void setFundManagementCharge_X(int policyTerm, double percentToBeInvested_EquityFund, double percentToBeInvested_EquityOptFund, double percentToBeInvested_GrowthFund, double percentToBeInvested_BalancedFund, double percentToBeInvested_PureFund, double percentToBeInvested_MidcapFund, double percentToBeInvested_BondOptFund, double percentToBeInvested_CorpFund) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
//            if(Integer.parseInt(getMonth_E())==1)
//            {
//            	//this.fundManagementCharge_X=cfap.roundUp_Level2(cfap.getStringWithout_E(Double.parseDouble(getFundBeforeFMC_W()) * (prop.charge_Fund +(getCharge_fund_ren( percentToBeInvested_EquityFund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_BondFund, percentToBeInvested_MoneyMarketFund, percentToBeInvested_Top300Fund)/12))));
//            	this.fundManagementCharge_X=cfap.getRoundOffLevel2New_Saral(cfap.getStringWithout_E(Double.parseDouble(getFundBeforeFMC_W()) * (prop.charge_Fund +(getCharge_fund_ren( percentToBeInvested_EquityFund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund,percentToBeInvested_PureFund,percentToBeInvested_MidcapFund,percentToBeInvested_BondOptFund,percentToBeInvested_CorpFund)/12))));
//            }
//            else
//            {
            //this.fundManagementCharge_X=cfap.roundUp_Level2(cfap.getStringWithout_E(Double.parseDouble(getFundBeforeFMC_W()) * (getCharge_fund_ren( percentToBeInvested_EquityFund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_BondFund, percentToBeInvested_MoneyMarketFund,  percentToBeInvested_Top300Fund)/12)));
//            	this.fundManagementCharge_X=cfap.getRoundOffLevel2New_Saral(cfap.getStringWithout_E(Double.parseDouble(getFundBeforeFMC_W()) * (getCharge_fund_ren( percentToBeInvested_EquityFund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund,percentToBeInvested_PureFund,percentToBeInvested_MidcapFund,percentToBeInvested_BondOptFund,percentToBeInvested_CorpFund)/12)));
            double a = getCharge_fund_ren(percentToBeInvested_EquityFund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_PureFund, percentToBeInvested_MidcapFund, percentToBeInvested_BondOptFund, percentToBeInvested_CorpFund);
            double b = Double.parseDouble(getFundBeforeFMC_W());

//        	System.out.println("getCharge_fund_ren a "+a);
//        	System.out.println("getFundBeforeFMC_W b "+b);

            this.fundManagementCharge_X = cfap.getRoundOffLevel2New_Saral(cfap.getStringWithout_E(Double.parseDouble(getFundBeforeFMC_W()) * getCharge_fund_ren(percentToBeInvested_EquityFund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_PureFund, percentToBeInvested_MidcapFund, percentToBeInvested_BondOptFund, percentToBeInvested_CorpFund) / 12));

//            }
        } else {
            this.fundManagementCharge_X = "" + 0;
        }
    }

    public String getFundManagementCharge_X() {
        return fundManagementCharge_X;
    }


    public void setFundManagementCharge_RIY(int policyTerm, double percentToBeInvested_EquityFund, double percentToBeInvested_EquityOptFund, double percentToBeInvested_GrowthFund, double percentToBeInvested_BalancedFund, double percentToBeInvested_PureFund, double percentToBeInvested_MidcapFund, double percentToBeInvested_BondOptFund, double percentToBeInvested_CorpFund) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.FundManagementCharge_RIY = cfap.getRoundOffLevel2New_Saral(cfap.getStringWithout_E(Double.parseDouble(getFundBeforeFMC_RIY()) * (getCharge_fund_ren(percentToBeInvested_EquityFund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_PureFund, percentToBeInvested_MidcapFund, percentToBeInvested_BondOptFund, percentToBeInvested_CorpFund) / 12)));

        } else {
            this.FundManagementCharge_RIY = "" + 0;
        }
    }

    public String getFundManagementCharge_RIY() {
        return FundManagementCharge_RIY;
    }


    public void setFundManagementCharge_RIY8(int policyTerm, double percentToBeInvested_EquityFund, double percentToBeInvested_EquityOptFund, double percentToBeInvested_GrowthFund, double percentToBeInvested_BalancedFund, double percentToBeInvested_PureFund, double percentToBeInvested_MidcapFund, double percentToBeInvested_BondOptFund, double percentToBeInvested_CorpFund) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.FundManagementCharge_RIY8 = cfap.getRoundOffLevel2New_Saral(cfap.getStringWithout_E(Double.parseDouble(getFundBeforeFMC_RIY8()) * (getCharge_fund_ren(percentToBeInvested_EquityFund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_PureFund, percentToBeInvested_MidcapFund, percentToBeInvested_BondOptFund, percentToBeInvested_CorpFund) / 12)));

        } else {
            this.FundManagementCharge_RIY8 = "" + 0;
        }
    }

    public String getFundManagementCharge_RIY8() {
        return FundManagementCharge_RIY8;
    }

    public double getCharge_fund_ren(double percentToBeInvested_EquityFund, double percentToBeInvested_EquityOptFund, double percentToBeInvested_GrowthFund, double percentToBeInvested_BalancedFund, double percentToBeInvested_PureFund, double percentToBeInvested_MidcapFund, double percentToBeInvested_BondOptFund, double percentToBeInvested_CorpFund) {
        return percentToBeInvested_EquityFund * prop.FMC_EquityFund +
                percentToBeInvested_EquityOptFund * prop.FMC_EquityOptimiserFund +
                percentToBeInvested_GrowthFund * prop.FMC_GrowthFund +
                percentToBeInvested_BalancedFund * prop.FMC_BalancedFund +
                percentToBeInvested_PureFund * prop.FMC_PureFund +
                percentToBeInvested_MidcapFund * prop.FMC_MidcapFund +
                percentToBeInvested_BondOptFund * prop.FMC_BondOptimizerFund +
                percentToBeInvested_CorpFund * prop.FMC_CorpFund

                ;
    }

    public void setServiceTaxOnFMC_Y(double percentToBeInvested_EquityFund, double percentToBeInvested_EquityOptFund, double percentToBeInvested_GrowthFund, double percentToBeInvested_BalancedFund, double percentToBeInvested_PureFund, double percentToBeInvested_MidcapFund, double percentToBeInvested_BondOptFund, double percentToBeInvested_CorpFund, double serviceTax) {
//        double a=0;
//        if(prop.fundManagementCharges)
//        {a=(Double.parseDouble(getFundManagementCharge_X()) * (0.0135/getCharge_fund_ren(percentToBeInvested_EquityFund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund,percentToBeInvested_PureFund,percentToBeInvested_MidcapFund,percentToBeInvested_BondOptFund,percentToBeInvested_CorpFund)));}
//        else
//        {a=0;}
//        //this.serviceTaxOnFMC_Y = cfap.roundUp_Level2(cfap.getStringWithout_E(a* prop.serviceTax));
//        this.serviceTaxOnFMC_Y = cfap.getRoundOffLevel2New_Saral(cfap.getStringWithout_E(a* prop.serviceTax));


        double a = 0, temp = 0;
        if (prop.fundManagementCharges) {
            a = 1;
        } else {
            a = 0;
        }

        temp = Double.parseDouble(cfap.getRoundOffLevel2New_Saral(cfap.getStringWithout_E(Double.parseDouble(getFundManagementCharge_X()) * getCharge_fund_ren(percentToBeInvested_EquityFund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_PureFund, percentToBeInvested_MidcapFund, percentToBeInvested_BondOptFund, percentToBeInvested_CorpFund) / getCharge_fund_ren(percentToBeInvested_EquityFund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_PureFund, percentToBeInvested_MidcapFund, percentToBeInvested_BondOptFund, percentToBeInvested_CorpFund))));


        //this.serviceTaxOnFMC_Y = cfap.roundUp_Level2(cfap.getStringWithout_E(a* prop.serviceTax));
        this.serviceTaxOnFMC_Y = cfap.getRoundOffLevel2New(cfap.getStringWithout_E(temp * serviceTax * a));

    }

    public String getServiceTaxOnFMC_Y() {
        return serviceTaxOnFMC_Y;
    }


    public void setServiceTaxOnFMC_RIY(double percentToBeInvested_EquityFund, double percentToBeInvested_EquityOptFund, double percentToBeInvested_GrowthFund, double percentToBeInvested_BalancedFund, double percentToBeInvested_PureFund, double percentToBeInvested_MidcapFund, double percentToBeInvested_BondOptFund, double percentToBeInvested_CorpFund, double serviceTax) {
        double a = 0, temp = 0;
        if (prop.fundManagementChargesYield) {
            a = 1;
        } else {
            a = 0;
        }

        temp = Double.parseDouble(cfap.getRoundOffLevel2New_Saral(cfap.getStringWithout_E(Double.parseDouble(getFundManagementCharge_RIY()) * getCharge_fund_ren(percentToBeInvested_EquityFund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_PureFund, percentToBeInvested_MidcapFund, percentToBeInvested_BondOptFund, percentToBeInvested_CorpFund) / getCharge_fund_ren(percentToBeInvested_EquityFund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_PureFund, percentToBeInvested_MidcapFund, percentToBeInvested_BondOptFund, percentToBeInvested_CorpFund))));


        //this.serviceTaxOnFMC_Y = cfap.roundUp_Level2(cfap.getStringWithout_E(a* prop.serviceTax));
        this.ServiceTaxOnFMC_RIY = cfap.getRoundOffLevel2New(cfap.getStringWithout_E(temp * serviceTax * a));

    }

    public String getServiceTaxOnFMC_RIY() {
        return ServiceTaxOnFMC_RIY;
    }


    public void setServiceTaxOnFMC_RIY8(double percentToBeInvested_EquityFund, double percentToBeInvested_EquityOptFund, double percentToBeInvested_GrowthFund, double percentToBeInvested_BalancedFund, double percentToBeInvested_PureFund, double percentToBeInvested_MidcapFund, double percentToBeInvested_BondOptFund, double percentToBeInvested_CorpFund, double serviceTax) {
        double a = 0, temp = 0;
        if (prop.fundManagementChargesYield) {
            a = 1;
        } else {
            a = 0;
        }

        temp = Double.parseDouble(cfap.getRoundOffLevel2New_Saral(cfap.getStringWithout_E(Double.parseDouble(getFundManagementCharge_RIY8()) * getCharge_fund_ren(percentToBeInvested_EquityFund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_PureFund, percentToBeInvested_MidcapFund, percentToBeInvested_BondOptFund, percentToBeInvested_CorpFund) / getCharge_fund_ren(percentToBeInvested_EquityFund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_PureFund, percentToBeInvested_MidcapFund, percentToBeInvested_BondOptFund, percentToBeInvested_CorpFund))));


        //this.serviceTaxOnFMC_Y = cfap.roundUp_Level2(cfap.getStringWithout_E(a* prop.serviceTax));
        this.ServiceTaxOnFMC_RIY8 = cfap.getRoundOffLevel2New(cfap.getStringWithout_E(temp * serviceTax * a));

    }

    public String getServiceTaxOnFMC_RIY8() {
        return ServiceTaxOnFMC_RIY8;
    }


    public void setFundValueAfterFMCAndBeforeGA_Z(int policyTerm) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.fundValueAfterFMCBerforeGA_Z = "" + (Double.parseDouble(getFundBeforeFMC_W()) - Double.parseDouble(getFundManagementCharge_X()) - Double.parseDouble(getServiceTaxOnFMC_Y()));
        } else {
            this.fundValueAfterFMCBerforeGA_Z = "" + 0;
        }
    }

    public String getFundValueAfterFMCAndBeforeGA_Z() {
        return fundValueAfterFMCBerforeGA_Z;
    }


    public void setFundValueAfterFMCAndBeforeGA_RIY(int policyTerm) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.FundValueAfterFMCAndBeforeGA_RIY = "" + (Double.parseDouble(getFundBeforeFMC_RIY()) - Double.parseDouble(getFundManagementCharge_RIY()) - Double.parseDouble(getServiceTaxOnFMC_RIY()));
        } else {
            this.FundValueAfterFMCAndBeforeGA_RIY = "" + 0;
        }
    }

    public String getFundValueAfterFMCAndBeforeGA_RIY() {
        return FundValueAfterFMCAndBeforeGA_RIY;
    }


    public void setFundValueAfterFMCAndBeforeGA_RIY8(int policyTerm) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.FundValueAfterFMCAndBeforeGA_RIY8 = "" + (Double.parseDouble(getFundBeforeFMC_RIY8()) - Double.parseDouble(getFundManagementCharge_RIY8()) - Double.parseDouble(getServiceTaxOnFMC_RIY8()));
        } else {
            this.FundValueAfterFMCAndBeforeGA_RIY8 = "" + 0;
        }
    }

    public String getFundValueAfterFMCAndBeforeGA_RIY8() {
        return FundValueAfterFMCAndBeforeGA_RIY8;
    }


    public void setTotalServiceTax_U(double riderCharges, double serviceTax) {
        double temp1 = 0, temp2 = 0;
        temp1 = Double.parseDouble(getServiceTaxOnAllocation_M()) + Double.parseDouble(getServiceTax_exclOfSTonAllocAndSurr_T()) + Double.parseDouble(getServiceTaxOnFMC_Y());
        if (prop.riderCharges) {
            temp2 = riderCharges;
        } else {
            temp2 = 0;
        }
        this.totalServiceTax_U = cfap.getRoundOffLevel2New(cfap.getStringWithout_E(temp1 + temp2 * serviceTax));
    }

    public String getTotalServiceTax_U() {
        return totalServiceTax_U;
    }


    public void setTotalServiceTax_RIY(double riderCharges, double serviceTax) {
        double temp1 = 0, temp2 = 0;
        temp1 = Double.parseDouble(getServiceTaxOnAllocation_RIY()) + Double.parseDouble(getServiceTax_exclOfSTonAllocAndSurr_RIY()) + Double.parseDouble(getServiceTaxOnFMC_RIY());
        if (prop.riderChargesYield) {
            temp2 = riderCharges;
        } else {
            temp2 = 0;
        }
        this.TotalServiceTax_RIY = cfap.getRoundOffLevel2New(cfap.getStringWithout_E(temp1 + temp2 * serviceTax));
    }

    public String getTotalServiceTax_RIY() {
        return TotalServiceTax_RIY;
    }


    public void setTotalServiceTax_RIY8(double riderCharges, double serviceTax) {
        double temp1 = 0, temp2 = 0;
        temp1 = Double.parseDouble(getServiceTaxOnAllocation_RIY()) + Double.parseDouble(getServiceTax_exclOfSTonAllocAndSurr_RIY8()) + Double.parseDouble(getServiceTaxOnFMC_RIY8());
        if (prop.riderChargesYield) {
            temp2 = riderCharges;
        } else {
            temp2 = 0;
        }
        this.TotalServiceTax_RIY8 = cfap.getRoundOffLevel2New(cfap.getStringWithout_E(temp1 + temp2 * serviceTax));
    }

    public String getTotalServiceTax_RIY8() {
        return TotalServiceTax_RIY8;
    }


    public void setGuaranteedAddition_AA(String planType, double premiumamount, int PPT, ArrayList<Double> lstFundValueAfter_FMC, int month) {
        double a = 0, avg = 0;
        String cal = "";
        if (getPolicyInForce_G().equals("N")) {
            this.guaranteedAddition_AA = "" + 0;
        } else {
            double val1 = 0;
            double val2 = 0;
            if ((Integer.parseInt(getMonth_E()) % 12) == 0) {
                val1 = 1;
            } else {
                val1 = 0;
            }

            if (Integer.parseInt(getYear_F()) <= 5) {
                val2 = 0;
            } else if (Integer.parseInt(getYear_F()) > 5 && Integer.parseInt(getYear_F()) <= 10) {
                val2 = 0.002;
            } else {
                val2 = 0.003;
            }

//        	this.guaranteedAddition_AA =cfap.roundUp_Level2(cfap.getStringWithout_E(1 - 1 * val1 * premiumamount * val2 +1));

            String Val3 = cfap.getRoundOffLevel2New_Saral(cfap.getStringWithout_E(val1 * premiumamount * val2));


            if (month >= 25) {
                for (int i = (month - 1); i >= (month - 12); i--) {
                    a += lstFundValueAfter_FMC.get(i);
//	        		System.out.println("get(i) "+lstFundValueAfter_FMC.get(i));
                }
                avg = a / 12;
//	        	System.out.println("a "+a);
//	        	System.out.println("avg "+avg);
                //cal =cfap.roundUp_Level2(cfap.getStringWithout_E((1 - 1) * (val1 * premiumamount * val2) +(1*0) * (val2*avg)));
//	        	System.out.println("val1 "+val1);
//	        	System.out.println("val2 "+val2);
                cal = cfap.getRoundOffLevel2New_Saral(cfap.getStringWithout_E((1 - 1) * Double.parseDouble(Val3) + val1 * 1 * (val2 * avg)));
            } else {
                //cal =cfap.roundUp_Level2(cfap.getStringWithout_E((1 - 1) * (val1 * premiumamount * val2) +(1*0)));
                cal = cfap.getRoundOffLevel2New_Saral(cfap.getStringWithout_E((1 - 1) * Double.parseDouble(Val3) + (1 * 0)));
            }

            this.guaranteedAddition_AA = cal;
        }
    }

    public String getGuaranteedAddition_AA() {
        return guaranteedAddition_AA;
    }


    public void setTerminalAddition() {
        this.TerminalAddition = "0";
    }

    public String getTerminalAddition() {
        return TerminalAddition;
    }


    public double getPercentOfFirstYrPremium(String planType, int PPT, double effectivePremium) {
        if (prop.asPercentOfFirstYrPremium) {
            if (getMonth_E().equals("60")) {
                if (planType.equals("Limited")) {
                    if (PPT == 5) {
                        return 0;
                    } else if (PPT == 8) {
                        return 0;
                    } else if (PPT == 10) {
                        return 0;
                    } else {
                        return 0;
                    }
                } else {
                    if (PPT == 1) {
                        //Single
                        if (effectivePremium >= 24000 && effectivePremium <= 99900) {
                            return 0;
                        } else if (effectivePremium >= 100000 && effectivePremium <= 499900) {
                            return 0;
                        } else if (effectivePremium >= 500000) {
                            return 0;
                        } else {
                            return 0;
                        }
                    } else {
                        //Regular
                        if (effectivePremium >= 24000 && effectivePremium <= 99900) {
                            return 0;
                        } else if (effectivePremium >= 100000 && effectivePremium <= 499900) {
                            return 0;
                        } else if (effectivePremium >= 500000) {
                            return 0;
                        } else {
                            return 0;
                        }
                    }
                }
            }
            if (getMonth_E().equals("84")) {
                if (planType.equals("Limited")) {
                    if (PPT == 5) {
                        return 0;
                    } else if (PPT == 8) {
                        return 0;
                    } else if (PPT == 10) {
                        return 0;
                    } else {
                        return 0;
                    }
                } else {
                    if (PPT == 1) {
                        //Single
                        if (effectivePremium >= 24000 && effectivePremium <= 99900) {
                            return 0;
                        } else if (effectivePremium >= 100000 && effectivePremium <= 499900) {
                            return 0;
                        } else if (effectivePremium >= 500000) {
                            return 0;
                        } else {
                            return 0;
                        }
                    } else {
                        //Regular
                        if (effectivePremium >= 24000 && effectivePremium <= 99900) {
                            return 0;
                        } else if (effectivePremium >= 100000 && effectivePremium <= 499900) {
                            return 0;
                        } else if (effectivePremium >= 500000) {
                            return 0;
                        } else {
                            return 0;
                        }
                    }
                }
            }
            if (getMonth_E().equals("120")) {
                if (planType.equals("Limited")) {
                    if (PPT == 5) {
                        return 0.05;
                    } else if (PPT == 8) {
                        return 0.05;
                    } else if (PPT == 10) {
                        return 0.05;
                    } else {
                        return 0;
                    }
                } else {
                    if (PPT == 1) {
                        //Single
                        if (effectivePremium >= 24000 && effectivePremium <= 99900) {
                            return 0.05;
                        } else if (effectivePremium >= 100000 && effectivePremium <= 499900) {
                            return 0.05;
                        } else if (effectivePremium >= 500000) {
                            return 0.05;
                        } else {
                            return 0;
                        }
                    } else {
                        //Regular
                        if (effectivePremium >= 24000 && effectivePremium <= 99900) {
                            return 0.05;
                        } else if (effectivePremium >= 100000 && effectivePremium <= 499900) {
                            return 0.05;
                        } else if (effectivePremium >= 500000) {
                            return 0.05;
                        } else {
                            return 0;
                        }
                    }
                }
            }
            if (getMonth_E().equals("180")) {
                if (planType.equals("Limited")) {
                    if (PPT == 5) {
                        return 0.05;
                    } else if (PPT == 8) {
                        return 0.1;
                    } else if (PPT == 10) {
                        return 0.1;
                    } else {
                        return 0;
                    }
                } else {
                    if (PPT == 1) {
                        //Single
                        if (effectivePremium >= 24000 && effectivePremium <= 99900) {
                            return 0.05;
                        } else if (effectivePremium >= 100000 && effectivePremium <= 499900) {
                            return 0.05;
                        } else if (effectivePremium >= 500000) {
                            return 0.05;
                        } else {
                            return 0;
                        }
                    } else {
                        //Regular
                        if (effectivePremium >= 24000 && effectivePremium <= 99900) {
                            return 0.15;
                        } else if (effectivePremium >= 100000 && effectivePremium <= 499900) {
                            return 0.15;
                        } else if (effectivePremium >= 500000) {
                            return 0.15;
                        } else {
                            return 0;
                        }
                    }
                }
            }
            if (getMonth_E().equals("240")) {
                if (planType.equals("Limited")) {
                    if (PPT == 5) {
                        return 0.1;
                    } else if (PPT == 8) {
                        return 0.1;
                    } else if (PPT == 10) {
                        return 0.15;
                    } else {
                        return 0;
                    }
                } else {
                    if (PPT == 1) {
                        //Single
                        if (effectivePremium >= 24000 && effectivePremium <= 99900) {
                            return 0.05;
                        } else if (effectivePremium >= 100000 && effectivePremium <= 499900) {
                            return 0.05;
                        } else if (effectivePremium >= 500000) {
                            return 0.05;
                        } else {
                            return 0;
                        }
                    } else {
                        //Regular
                        if (effectivePremium >= 24000 && effectivePremium <= 99900) {
                            return 0.25;
                        } else if (effectivePremium >= 100000 && effectivePremium <= 499900) {
                            return 0.25;
                        } else if (effectivePremium >= 500000) {
                            return 0.25;
                        } else {
                            return 0;
                        }
                    }
                }
            }
            if (getMonth_E().equals("300")) {
                if (planType.equals("Limited")) {
                    if (PPT == 5) {
                        return 0.1;
                    } else if (PPT == 8) {
                        return 0.1;
                    } else if (PPT == 10) {
                        return 0.2;
                    } else {
                        return 0;
                    }
                } else {
                    if (PPT == 1) {
                        //Single
                        if (effectivePremium >= 24000 && effectivePremium <= 99900) {
                            return 0.07;
                        } else if (effectivePremium >= 100000 && effectivePremium <= 499900) {
                            return 0.07;
                        } else if (effectivePremium >= 500000) {
                            return 0.07;
                        } else {
                            return 0;
                        }
                    } else {
                        //Regular
                        if (effectivePremium >= 24000 && effectivePremium <= 99900) {
                            return 0.35;
                        } else if (effectivePremium >= 100000 && effectivePremium <= 499900) {
                            return 0.35;
                        } else if (effectivePremium >= 500000) {
                            return 0.35;
                        } else {
                            return 0;
                        }
                    }
                }
            }
            if (getMonth_E().equals("360")) {
                if (planType.equals("Limited")) {
                    if (PPT == 5) {
                        return 0.1;
                    } else if (PPT == 8) {
                        return 0.15;
                    } else if (PPT == 10) {
                        return 0.25;
                    } else {
                        return 0;
                    }
                } else {
                    if (PPT == 1) {
                        //Single
                        if (effectivePremium >= 24000 && effectivePremium <= 99900) {
                            return 0.08;
                        } else if (effectivePremium >= 100000 && effectivePremium <= 499900) {
                            return 0.08;
                        } else if (effectivePremium >= 500000) {
                            return 0.08;
                        } else {
                            return 0;
                        }
                    } else {
                        //Regular
                        if (effectivePremium >= 24000 && effectivePremium <= 99900) {
                            return 0.45;
                        } else if (effectivePremium >= 100000 && effectivePremium <= 499900) {
                            return 0.45;
                        } else if (effectivePremium >= 500000) {
                            return 0.45;
                        } else {
                            return 0;
                        }
                    }
                }
            }
            if (getMonth_E().equals("1188")) {
                if (planType.equals("Limited")) {
                    if (PPT == 5) {
                        return 0;
                    } else if (PPT == 8) {
                        return 0;
                    } else if (PPT == 10) {
                        return 0;
                    } else {
                        return 0;
                    }
                } else {
                    if (PPT == 1) {
                        //Single
                        if (effectivePremium >= 24000 && effectivePremium <= 99900) {
                            return 0;
                        } else if (effectivePremium >= 100000 && effectivePremium <= 499900) {
                            return 0;
                        } else if (effectivePremium >= 500000) {
                            return 0;
                        } else {
                            return 0;
                        }
                    } else {
                        //Regular
                        if (effectivePremium >= 24000 && effectivePremium <= 99900) {
                            return 0;
                        } else if (effectivePremium >= 100000 && effectivePremium <= 499900) {
                            return 0;
                        } else if (effectivePremium >= 500000) {
                            return 0;
                        } else {
                            return 0;
                        }
                    }
                }
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public void setFundValueAtEnd_AB() {
        this.fundValueAtEnd_AB = "" + (Double.parseDouble(getGuaranteedAddition_AA()) + Double.parseDouble(getFundValueAfterFMCAndBeforeGA_Z()) + Double.parseDouble(getTerminalAddition()));
    }

    public String getFundValueAtEnd_AB() {
        return fundValueAtEnd_AB;
    }


    public void setFundValueAtEnd_RIY() {
        this.FundValueAtEnd_RIY = "" + (Double.parseDouble(getGuaranteedAddition_AA()) + Double.parseDouble(getFundValueAfterFMCAndBeforeGA_RIY()) + Double.parseDouble(getTerminalAddition()));
    }

    public String getFundValueAtEnd_RIY() {
        return FundValueAtEnd_RIY;
    }


    public void setFundValueAtEnd_RIY8() {
        this.FundValueAtEnd_RIY8 = "" + (Double.parseDouble(getGuaranteedAddition_RIY8()) + Double.parseDouble(getFundValueAfterFMCAndBeforeGA_RIY8()) + Double.parseDouble(getTerminalAddition()));
    }

    public String getFundValueAtEnd_RIY8() {
        return FundValueAtEnd_RIY8;
    }


    public String[] calSurrRateArrRP(double effectivePremium) {
        if (effectivePremium <= 25000) {
            return new String[]{"0.20", "0.15", "0.10", "0.05", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"};
        } else {
            return new String[]{"0.06", "0.04", "0.03", "0.02", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"};
        }
    }

    public String[] calSurrRateArrSP() {
        return new String[]{"0.01", "0.005", "0.003", "0.001", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"};
    }

    public String[] calCapArr(double effectivePremium) {
        if (effectivePremium <= 25000) {
            return new String[]{"3000", "2000", "1500", "1000", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"};
        } else {
            return new String[]{"6000", "5000", "4000", "2000", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"};
        }
    }

    public void setSurrenderCap_AV(double premiumamount) {

        double surrendercap = 0;
        if (getPolicyInForce_G().equals("Y")) {
            if (premiumamount <= 25000) {
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
            }

            this.surrenderCap_AV = cfap.getStringWithout_E((surrendercap));
        } else {
            this.surrenderCap_AV = "" + 0;
        }
    }

    public String getSurrenderCap_AV() {
        return surrenderCap_AV;
    }

    public String setSurrenderCharges_AC(double premiumamount) {
        double surrenderCharge = 0;

        if (premiumamount <= 25000) {
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
        }


        double val1 = Math.min(Double.parseDouble(getFundValueAtEnd_AB()), premiumamount);
        double val2 = val1 * surrenderCharge;
        double val3 = Math.min(Double.parseDouble(getSurrenderCap_AV()), val2);

        //return surrenderCharges_AC =cfap.roundUp_Level2( cfap.getStringWithout_E(val3)) ;
        return surrenderCharges_AC = cfap.getRoundOffLevel2New_Saral(cfap.getStringWithout_E(val3));
    }

    public String getSurrenderCharges_AC() {
        return surrenderCharges_AC;
    }

    public String setSurrenderCharges_RIY(double premiumamount) {
        double surrenderCharge = 0;

        if (premiumamount <= 25000) {
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
        }


        double val1 = Math.min(Double.parseDouble(getFundValueAtEnd_RIY()), premiumamount);
        double val2 = val1 * surrenderCharge;
        double val3 = Math.min(Double.parseDouble(getSurrenderCap_AV()), val2);

        //return surrenderCharges_AC =cfap.roundUp_Level2( cfap.getStringWithout_E(val3)) ;
        return SurrenderCharges_RIY = cfap.getRoundOffLevel2New_Saral(cfap.getStringWithout_E(val3));
    }


    public String getSurrenderCharges_RIY() {
        return SurrenderCharges_RIY;
    }


    public String setSurrenderCharges_RIY8(double premiumamount) {
        double surrenderCharge = 0;

        if (premiumamount <= 25000) {
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
        }


        double val1 = Math.min(Double.parseDouble(getFundValueAtEnd_RIY8()), premiumamount);
        double val2 = val1 * surrenderCharge;
        double val3 = Math.min(Double.parseDouble(getSurrenderCap_AV()), val2);

        //return surrenderCharges_AC =cfap.roundUp_Level2( cfap.getStringWithout_E(val3)) ;
        return SurrenderCharges_RIY8 = cfap.getRoundOffLevel2New_Saral(cfap.getStringWithout_E(val3));
    }


    public String getSurrenderCharges_RIY8() {
        return SurrenderCharges_RIY8;
    }


    public void setServiceTaxOnSurrenderCharges_AD(double serviceTax) {
        if (prop.surrenderCharges) {
            //this.serviceTaxOnSurrenderCharges_AD =cfap.roundUp_Level2(cfap.getStringWithout_E(Double.parseDouble(getSurrenderCharges_AC())*prop.serviceTax));
            this.serviceTaxOnSurrenderCharges_AD = cfap.getRoundOffLevel2New(cfap.getStringWithout_E(Double.parseDouble(getSurrenderCharges_AC()) * serviceTax));
        } else {
            this.serviceTaxOnSurrenderCharges_AD = "" + 0;
        }
    }

    public String getServiceTaxOnSurrenderCharges_AD() {
        return serviceTaxOnSurrenderCharges_AD;
    }


    public void setServiceTaxOnSurrenderCharges_RIY(double serviceTax) {
        if (prop.surrenderChargesYield) {
            //this.serviceTaxOnSurrenderCharges_AD =cfap.roundUp_Level2(cfap.getStringWithout_E(Double.parseDouble(getSurrenderCharges_AC())*prop.serviceTax));
            this.ServiceTaxOnSurrenderCharges_RIY = cfap.getRoundOffLevel2New(cfap.getStringWithout_E(Double.parseDouble(getSurrenderCharges_RIY()) * serviceTax));
        } else {
            this.ServiceTaxOnSurrenderCharges_RIY = "" + 0;
        }
    }

    public String getServiceTaxOnSurrenderCharges_RIY() {
        return ServiceTaxOnSurrenderCharges_RIY;
    }


    public void setServiceTaxOnSurrenderCharges_RIY8(double serviceTax) {
        if (prop.surrenderChargesYield) {
            //this.serviceTaxOnSurrenderCharges_AD =cfap.roundUp_Level2(cfap.getStringWithout_E(Double.parseDouble(getSurrenderCharges_AC())*prop.serviceTax));
            this.ServiceTaxOnSurrenderCharges_RIY8 = cfap.getRoundOffLevel2New(cfap.getStringWithout_E(Double.parseDouble(getSurrenderCharges_RIY8()) * serviceTax));
        } else {
            this.ServiceTaxOnSurrenderCharges_RIY8 = "" + 0;
        }
    }

    public String getServiceTaxOnSurrenderCharges_RIY8() {
        return ServiceTaxOnSurrenderCharges_RIY8;
    }


    public void setSurrenderValue_AE() {
        this.surrenderValue_AE = "" + (Double.parseDouble(getFundValueAtEnd_AB()) - Double.parseDouble(getSurrenderCharges_AC()) - Double.parseDouble(getServiceTaxOnSurrenderCharges_AD()));
    }

    public String getSurrenderValue_AE() {
        return surrenderValue_AE;
    }


    public void setSurrenderValue_RIY() {
        this.SurrenderValue_RIY = "" + (Double.parseDouble(getFundValueAtEnd_RIY()) - Double.parseDouble(getSurrenderCharges_RIY()) - Double.parseDouble(getServiceTaxOnSurrenderCharges_RIY()));
    }

    public String getSurrenderValue_RIY() {
        return SurrenderValue_RIY;
    }


    public void setSurrenderValue_RIY8() {
        this.SurrenderValue_RIY8 = "" + (Double.parseDouble(getFundValueAtEnd_RIY8()) - Double.parseDouble(getSurrenderCharges_RIY8()) - Double.parseDouble(getServiceTaxOnSurrenderCharges_RIY8()));
    }

    public String getSurrenderValue_RIY8() {
        return SurrenderValue_RIY8;
    }


    public void setDeathBenefit_AF(int policyTerm, double sumAssured, double sum_I, double fundValueAtEnd_AB) {
        if ((Integer.parseInt(getAge_H()) < 8) && (Integer.parseInt(getMonth_E()) < 24)) {
            this.deathBenefit_AF = "" + (Double.parseDouble(getFundValueAtEnd_AB()));
        } else {
            if (Integer.parseInt(getYear_F()) > policyTerm) {
                this.deathBenefit_AF = "" + 0;
            } else {
                double val1 = Math.max(fundValueAtEnd_AB, sumAssured);
                //double val2 = Math.max(val1, Double.parseDouble(getPremium_I())*1.05);

                double val2 = Math.max(val1, (sum_I * 1.05));

//        	System.out.println("Val1 :  " +val2);

//        	System.out.println("Val2 :  " +val2);

                this.deathBenefit_AF = cfap.getRoundOffLevel2New_Saral(cfap.getStringWithout_E(val2));
            }
        }

    }

    public String getDeathBenefit_AF() {
        return deathBenefit_AF;
    }


    public void setDeathBenefit_RIY(int policyTerm, double sumAssured, double sum_I) {
        if (Integer.parseInt(getYear_F()) > policyTerm) {
            this.DeathBenefit_RIY = "" + 0;
        } else {
            double val1 = Math.max(Double.parseDouble(getFundValueAtEnd_RIY()), sumAssured);
            //double val2 = Math.max(val1, Double.parseDouble(getPremium_I())*1.05);

            double val2 = Math.max(val1, (sum_I * 1.05));

            this.DeathBenefit_RIY = cfap.getRoundOffLevel2New_Saral(cfap.getStringWithout_E(val2));
        }


    }

    public String getDeathBenefit_RIY() {
        return DeathBenefit_RIY;
    }


    public void setDeathBenefit_RIY8(int policyTerm, double sumAssured, double sum_I) {
        if (Integer.parseInt(getYear_F()) > policyTerm) {
            this.DeathBenefit_RIY8 = "" + 0;
        } else {
            double val1 = Math.max(Double.parseDouble(getFundValueAtEnd_RIY8()), sumAssured);
            //double val2 = Math.max(val1, Double.parseDouble(getPremium_I())*1.05);

            double val2 = Math.max(val1, (sum_I * 1.05));

            this.DeathBenefit_RIY8 = cfap.getRoundOffLevel2New_Saral(cfap.getStringWithout_E(val2));
        }


    }

    public String getDeathBenefit_RIY8() {
        return DeathBenefit_RIY8;
    }


    public void setMortalityCharges_AG(double _fundValueAtEnd_AQ, int policyTerm, String[] forBIArray, double sumAssured, boolean mortalityCharges, int ageAtEntry) {
        if ((ageAtEntry < 8) && (Integer.parseInt(getMonth_E()) < 24)) {
            this.mortalityCharges_AG = "" + 0;
        } else {

            if (!(getPolicyInForce_G().equals("Y")) || Integer.parseInt(getYear_F()) > policyTerm) {
                this.mortalityCharges_AG = "" + 0;
            } else {

                DecimalFormat df = new DecimalFormat("#.#######");
                //df.setRoundingMode(RoundingMode.CEILING);


                double arrOutput = Double.parseDouble(forBIArray[Integer.parseInt(getAge_H())]);
                String roundTo7 = df.format(arrOutput);
                String test = cfap.getRoundOffLevel5(roundTo7);
                //double test = Double.parseDouble(df.format(arrOutput));
                String str1 = cfap.getStringWithout_E(arrOutput);
                //String str2=cfap.getStringWithout_E(test);

//            System.out.println(" mortality8 arroutput "+arrOutput);
//            System.out.println("mortality8 str1 "+str1);
//            System.out.println("mortality8 round to 5 "+test);
                //System.out.println("mortality8 str2 "+str2);
                //double div=arrOutput/1000;
                //System.out.println("div "+div);
                double a = Double.parseDouble(test) / 12;
//            System.out.println("mortality8 a "+a);

                double max1 = Math.max(sumAssured, Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()));
                //System.out.println("max1 "+max1);
                double b = (max1 - (Double.parseDouble(getAmountAvailableForInvestment_N()) + _fundValueAtEnd_AQ));
                //System.out.println("b "+b);

                int c = 0;
                if (mortalityCharges) {
                    c = 1;
                } else {
                    c = 0;
                }
                //this.mortalityCharges_R= cfap.roundUp_Level2(cfap.getStringWithout_E(((a*Math.max(b, 0))*c)));
                this.mortalityCharges_AG = cfap.getRoundOffLevel2New_Saral(cfap.getStringWithout_E(((a * Math.max(b, 0)) * c)));
            }
        }
//    	System.out.println("mortalityAG "+this.mortalityCharges_AG);
    }

    public String getMortalityCharges_AG() {
        return mortalityCharges_AG;
    }

    public void setTotalCharges_AH(int policyTerm, double riderCharges) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
//        	System.out.println(Double.parseDouble(getAccTPDCharges_8p()));
//        	System.out.println(Double.parseDouble(getPolicyAdministrationCharge_Q()));
//        	System.out.println(Double.parseDouble(getMortalityCharges_AG()));

            this.totalCharges_AH = "" + (Double.parseDouble(getAccTPDCharges_8p()) + Double.parseDouble(getPolicyAdministrationCharge_Q()) + Double.parseDouble(getMortalityCharges_AG()) + riderCharges);

        } else {
            this.totalCharges_AH = "" + 0;
        }
    }

    public String getTotalCharges_AH() {
        return totalCharges_AH;
    }

    public void setServiceTax_exclOfSTonAllocAndSurr_AI(double serviceTax, boolean mortalityAndRiderCharges, boolean administrationCharges) {

        double a = 0, b = 0;
        if (mortalityAndRiderCharges) {
            a = Double.parseDouble(getMortalityCharges_AG()) + Double.parseDouble(getAccTPDCharges_8p());
        } else {
            a = 0;
        }
        if (administrationCharges) {
            b = Double.parseDouble(getPolicyAdministrationCharge_Q());
        } else {
            b = 0;
        }
        this.serviceTaxExclOfSTOnAllocAndSurr_AI = cfap.getRoundOffLevel2New_Saral(cfap.getStringWithout_E((a + b) * serviceTax));

    }

    public String getServiceTax_exclOfSTonAllocAndSurr_AI() {
        return serviceTaxExclOfSTOnAllocAndSurr_AI;
    }

    public void setAdditionToFundIfAny_AK(double _fundValueAtEnd_AQ, int policyTerm, double riderCharges) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            double temp1 = 0, temp2 = 0;
            temp1 = (_fundValueAtEnd_AQ + Double.parseDouble(getAmountAvailableForInvestment_N()) - Double.parseDouble(getTotalCharges_AH()) - Double.parseDouble(getServiceTax_exclOfSTonAllocAndSurr_AI()) + riderCharges);
            double a = (double) 1 / 12;
            temp2 = (cfap.pow((1 + prop.int2), a)) - 1;
            this.additionToFundIfAny_AK = cfap.getRoundOffLevel2New_Saral(cfap.getStringWithout_E(temp1 * temp2));
        } else {
            this.additionToFundIfAny_AK = "" + 0;
        }
    }

    public String getAdditionToFundIfAny_AK() {
        return additionToFundIfAny_AK;
    }


    public void setAdditionToFundIfAny_RIY(double _fundValueAtEnd_AB, int policyTerm, double riderCharges) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {


            double temp1 = 0, temp2 = 0;
            temp1 = (_fundValueAtEnd_AB + Double.parseDouble(getAmountAvailableForInvestment_RIY()) - Double.parseDouble(getTotalCharges_RIY()) - Double.parseDouble(getServiceTax_exclOfSTonAllocAndSurr_RIY()) + riderCharges);
            double a = (double) 1 / 12;
            temp2 = (cfap.pow((1 + prop.int1), a)) - 1;
//            this.additionToFundIfAny_V= cfap.roundUp_Level2( cfap.getStringWithout_E(temp1*temp2));
            this.AdditionToFundIfAny_RIY = cfap.getRoundOffLevel2New_Saral(cfap.getStringWithout_E(temp1 * temp2));

//            		System.out.println("additionToFundIfAny_V RIY   "  +AdditionToFundIfAny_RIY);

        } else {
            this.AdditionToFundIfAny_RIY = "" + 0;
        }
    }

    public String getAdditionToFundIfAny_RIY() {
        return AdditionToFundIfAny_RIY;
    }


    public void setAdditionToFundIfAny_RIY8(double _fundValueAtEnd_AB, int policyTerm, double riderCharges) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {


            double temp1 = 0, temp2 = 0;
            temp1 = (_fundValueAtEnd_AB + Double.parseDouble(getAmountAvailableForInvestment_RIY()) - Double.parseDouble(getTotalCharges_RIY8()) - Double.parseDouble(getServiceTax_exclOfSTonAllocAndSurr_RIY8()) + riderCharges);
            double a = (double) 1 / 12;
            temp2 = (cfap.pow((1 + prop.int2), a)) - 1;
//            this.additionToFundIfAny_V= cfap.roundUp_Level2( cfap.getStringWithout_E(temp1*temp2));
            this.AdditionToFundIfAny_RIY8 = cfap.getRoundOffLevel2New_Saral(cfap.getStringWithout_E(temp1 * temp2));


        } else {
            this.AdditionToFundIfAny_RIY8 = "" + 0;
        }
    }

    public String getAdditionToFundIfAny_RIY8() {
        return AdditionToFundIfAny_RIY8;
    }


    public void setFundBeforeFMC_AL(double _fundValueAtEnd_AQ, int policyTerm, double riderCharges) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.fundBeforeFMC_AL = cfap.getStringWithout_E(_fundValueAtEnd_AQ + Double.parseDouble(getAmountAvailableForInvestment_N()) - Double.parseDouble(getTotalCharges_AH()) + Double.parseDouble(getAdditionToFundIfAny_AK()) - Double.parseDouble(getServiceTax_exclOfSTonAllocAndSurr_AI()) + riderCharges);
        } else {
            this.fundBeforeFMC_AL = "" + 0;
        }
    }

    public String getFundBeforeFMC_AL() {
        return fundBeforeFMC_AL;
    }

    public void setFundManagementCharge_AM(int policyTerm, double percentToBeInvested_EquityFund, double percentToBeInvested_EquityOptFund, double percentToBeInvested_GrowthFund, double percentToBeInvested_BalancedFund, double percentToBeInvested_PureFund, double percentToBeInvested_MidcapFund, double percentToBeInvested_BondOptFund, double percentToBeInvested_CorpFund) {


        if (Integer.parseInt(getYear_F()) <= policyTerm) {
//            if(Integer.parseInt(getMonth_E())==1)
//            {this.fundManagementCharge_AM=cfap.getRoundOffLevel2New_Saral(cfap.getStringWithout_E(Double.parseDouble(getFundBeforeFMC_AL()) * (prop.charge_Fund +(getCharge_fund_ren( percentToBeInvested_EquityFund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund,percentToBeInvested_PureFund,percentToBeInvested_MidcapFund,percentToBeInvested_BondOptFund,percentToBeInvested_CorpFund)/12))));}
//            else
            {
                this.fundManagementCharge_AM = cfap.getRoundOffLevel2New_Saral(cfap.getStringWithout_E(Double.parseDouble(getFundBeforeFMC_AL()) * (getCharge_fund_ren(percentToBeInvested_EquityFund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_PureFund, percentToBeInvested_MidcapFund, percentToBeInvested_BondOptFund, percentToBeInvested_CorpFund) / 12)));
            }
        } else {
            this.fundManagementCharge_AM = "" + 0;
        }
    }

    public String getFundManagementCharge_AM() {
        return fundManagementCharge_AM;
    }

    public void setServiceTaxOnFMC_AN(double percentToBeInvested_EquityFund, double percentToBeInvested_EquityOptFund, double percentToBeInvested_GrowthFund, double percentToBeInvested_BalancedFund, double percentToBeInvested_PureFund, double percentToBeInvested_MidcapFund, double percentToBeInvested_BondOptFund, double percentToBeInvested_CorpFund, double serviceTax) {
//        double a=0;
//        if(prop.fundManagementCharges)
//        {a=(Double.parseDouble(getFundManagementCharge_AM()) * (0.0135/getCharge_fund_ren( percentToBeInvested_EquityFund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund,percentToBeInvested_PureFund,percentToBeInvested_MidcapFund,percentToBeInvested_BondOptFund,percentToBeInvested_CorpFund)));}
//        else
//        {a=0;}
//        this.serviceTaxOnFMC_AN = cfap.getRoundOffLevel2New_Saral(cfap.getStringWithout_E(a* prop.serviceTax));
//
        double a = 0, temp = 0;
        if (prop.fundManagementCharges) {
            a = 1;
        } else {
            a = 0;
        }

        temp = Double.parseDouble(cfap.getRoundOffLevel2New_Saral(cfap.getStringWithout_E(Double.parseDouble(getFundManagementCharge_AM()) * getCharge_fund_ren(percentToBeInvested_EquityFund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_PureFund, percentToBeInvested_MidcapFund, percentToBeInvested_BondOptFund, percentToBeInvested_CorpFund) / getCharge_fund_ren(percentToBeInvested_EquityFund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_PureFund, percentToBeInvested_MidcapFund, percentToBeInvested_BondOptFund, percentToBeInvested_CorpFund))));


        //this.serviceTaxOnFMC_Y = cfap.roundUp_Level2(cfap.getStringWithout_E(a* prop.serviceTax));
        this.serviceTaxOnFMC_AN = cfap.getRoundOffLevel2New(cfap.getStringWithout_E(temp * serviceTax * a));


    }

    public String getServiceTaxOnFMC_AN() {
        return serviceTaxOnFMC_AN;
    }

    public void setFundValueAfterFMCAndBeforeGA_AO(int policyTerm) {

        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.fundValueAfterFMCBerforeGA_AO = "" + (Double.parseDouble(getFundBeforeFMC_AL()) - Double.parseDouble(getFundManagementCharge_AM()) - Double.parseDouble(getServiceTaxOnFMC_AN()));
        } else {
            this.fundValueAfterFMCBerforeGA_AO = "" + 0;
        }
    }

    public String getFundValueAfterFMCAndBeforeGA_AO() {
        return fundValueAfterFMCBerforeGA_AO;
    }

    public void setTotalServiceTax_AJ(double riderCharges, double serviceTax) {

        double temp1 = 0, temp2 = 0;
        temp1 = Double.parseDouble(getServiceTaxOnAllocation_M()) + Double.parseDouble(getServiceTax_exclOfSTonAllocAndSurr_AI()) + Double.parseDouble(getServiceTaxOnFMC_AN());
        if (prop.riderCharges) {
            temp2 = riderCharges;
        } else {
            temp2 = 0;
        }
        this.totalServiceTax_AJ = cfap.getRoundOffLevel2New(cfap.getStringWithout_E(temp1 + temp2 * serviceTax));
    }

    public String getTotalServiceTax_AJ() {
        return totalServiceTax_AJ;
    }

    public void setGuaranteedAddition_AP(String planType, double premiumamount, int PPT, ArrayList<Double> lstFundValueAfter_FMC, int month) {


        double a = 0, avg = 0;
        String cal = "";
        if (getPolicyInForce_G().equals("N")) {
            this.guaranteedAddition_AP = "" + 0;
        } else {
            double val1 = 0;
            double val2 = 0;
            if ((Integer.parseInt(getMonth_E()) % 12) == 0) {
                val1 = 1;
            } else {
                val1 = 0;
            }

            if (Integer.parseInt(getYear_F()) <= 5) {
                val2 = 0;
            } else if (Integer.parseInt(getYear_F()) > 5 && Integer.parseInt(getYear_F()) <= 10) {
                val2 = 0.002;
            } else {
                val2 = 0.003;
            }

//        	this.guaranteedAddition_AA =cfap.roundUp_Level2(cfap.getStringWithout_E(1 - 1 * val1 * premiumamount * val2 +1));

            String val3 = cfap.getRoundOffLevel2New_Saral(cfap.getStringWithout_E(val1 * premiumamount * val2));
            if (month >= 25) {
                for (int i = (month - 1); i >= (month - 12); i--) {
                    a += lstFundValueAfter_FMC.get(i);
//	        		System.out.println("get(i) "+lstFundValueAfter_FMC.get(i));
                }
                avg = a / 12;
//	        	System.out.println("a "+a);
//	        	System.out.println("avg "+avg);
                //cal =cfap.roundUp_Level2(cfap.getStringWithout_E((1 - 1) * (val1 * premiumamount * val2) +(1*0) * (val2*avg)));
                cal = cfap.getRoundOffLevel2New_Saral(cfap.getStringWithout_E((1 - 1) * Double.parseDouble(val3) + val1 * 1 * (val2 * avg)));
            } else {
                //cal =cfap.roundUp_Level2(cfap.getStringWithout_E((1 - 1) * (val1 * premiumamount * val2) +(1*0)));
                cal = cfap.getRoundOffLevel2New_Saral(cfap.getStringWithout_E((1 - 1) * Double.parseDouble(val3) + (1 * 0)));
            }

            this.guaranteedAddition_AP = cal;
        }

    }

    public String getGuaranteedAddition_AP() {
        return guaranteedAddition_AP;
    }


    public void setGuaranteedAddition_RIY8(String planType, double premiumamount, int PPT, ArrayList<Double> lstFundValueAfter_RIY8, int month) {


        double a = 0, avg = 0;
        String cal = "";
        if (getPolicyInForce_G().equals("N")) {
            this.GuaranteedAddition_RIY8 = "" + 0;
        } else {
            double val1 = 0;
            double val2 = 0;
            if ((Integer.parseInt(getMonth_E()) % 12) == 0) {
                val1 = 1;
            } else {
                val1 = 0;
            }

            if (Integer.parseInt(getYear_F()) <= 5) {
                val2 = 0;
            } else if (Integer.parseInt(getYear_F()) > 5 && Integer.parseInt(getYear_F()) <= 10) {
                val2 = 0.002;
            } else {
                val2 = 0.003;
            }
//        	System.out.println("val2 RIY8 : "+val2);
//        	this.guaranteedAddition_AA =cfap.roundUp_Level2(cfap.getStringWithout_E(1 - 1 * val1 * premiumamount * val2 +1));

            String val3 = cfap.getRoundOffLevel2New_Saral(cfap.getStringWithout_E(val1 * premiumamount * val2));

//        	System.out.println("val3 RIY8 : "+val3);
//        	System.out.println("Premium Amount RIY8 : "+premiumamount);

            double val4 = 0;

            if (Integer.parseInt(getMonth_E()) % 12 == 0) {

                if (Integer.parseInt(getYear_F()) == 6) {
                    val4 = 0.01;
                } else if (Integer.parseInt(getYear_F()) == 10) {
                    val4 = 0.025;
                } else if (Integer.parseInt(getYear_F()) == 15) {
                    val4 = 0.035;
                } else if (Integer.parseInt(getYear_F()) == 20) {
                    val4 = 0.05;
                } else if (Integer.parseInt(getYear_F()) == 25) {
                    val4 = 0.06;
                } else if (Integer.parseInt(getYear_F()) == 30) {
                    val4 = 0.07;
                }
            } else {
                val4 = 0;
            }


            if (month >= 25) {
                for (int i = (month - 2); i >= (month - 13); i--) {
                    a += lstFundValueAfter_RIY8.get(i);
//	        		System.out.println("get(i) "+lstFundValueAfter_RIY8.get(i));
                }
                avg = a / 12;
//	        	System.out.println("a RIY8"+a);
//	        	System.out.println("avg RIY8"+avg);
                //cal =cfap.roundUp_Level2(cfap.getStringWithout_E((1 - 1) * (val1 * premiumamount * val2) +(1*0) * (val2*avg)));
                cal = cfap.getRoundOffLevel2New_Saral(cfap.getStringWithout_E((1 - 1) * Double.parseDouble(val3) + (val4 * avg)));
//	        	System.out.println("cal RIY8 : "+cal);
            } else {
                //cal =cfap.roundUp_Level2(cfap.getStringWithout_E((1 - 1) * (val1 * premiumamount * val2) +(1*0)));
                cal = cfap.getRoundOffLevel2New_Saral(cfap.getStringWithout_E((1 - 1) * Double.parseDouble(val3) + (1 * 0)));
            }

            this.GuaranteedAddition_RIY8 = cal;
        }

    }

    public String getGuaranteedAddition_RIY8() {
        return GuaranteedAddition_RIY8;
    }


    public void setFundValueAtEnd_AQ() {
        this.fundValueAtEnd_AQ = "" + (Double.parseDouble(getGuaranteedAddition_AP()) + Double.parseDouble(getFundValueAfterFMCAndBeforeGA_AO()));
    }

    public String getFundValueAtEnd_AQ() {
        return fundValueAtEnd_AQ;
    }

    public void setSurrenderCharges_AR(double premiumamount, int PPT) {

        double surrenderCharge = 0;

        if (premiumamount <= 25000) {
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
        }


        double val1 = Math.min(Double.parseDouble(getFundValueAtEnd_AQ()), premiumamount);
        double val2 = val1 * surrenderCharge;
        double val3 = Math.min(Double.parseDouble(getSurrenderCap_AV()), val2);

        this.surrenderCharges_AR = cfap.getRoundOffLevel2New_Saral(cfap.getStringWithout_E(val3));
    }

    public String getSurrenderCharges_AR() {
        return surrenderCharges_AR;
    }

    public void setServiceTaxOnSurrenderCharges_AS(double serviceTax) {
        if (prop.surrenderCharges) {
            this.serviceTaxOnSurrenderCharges_AS = cfap.getRoundOffLevel2New(cfap.getStringWithout_E(Double.parseDouble(getSurrenderCharges_AR()) * serviceTax));
        } else {
            this.serviceTaxOnSurrenderCharges_AS = "" + 0;
        }
    }

    public String getServiceTaxOnSurrenderCharges_AS() {
        return serviceTaxOnSurrenderCharges_AS;
    }

    public void setSurrenderValue_AT() {
        this.surrenderValue_AT = "" + (Double.parseDouble(getFundValueAtEnd_AQ()) - Double.parseDouble(getSurrenderCharges_AR()) - Double.parseDouble(getServiceTaxOnSurrenderCharges_AS()));
    }

    public String getSurrenderValue_AT() {
        return surrenderValue_AT;
    }

    public void setDeathBenefit_AU(int policyTerm, double sumAssured, double sum_I) {
        if ((Integer.parseInt(getAge_H()) < 8) && (Integer.parseInt(getMonth_E()) < 24)) {
            this.deathBenefit_AU = "" + (Double.parseDouble(getFundValueAtEnd_AQ()));
        } else {

            if (Integer.parseInt(getYear_F()) > policyTerm) {
                this.deathBenefit_AU = "" + 0;
            } else {
                double val1 = Math.max(Double.parseDouble(getFundValueAtEnd_AQ()), sumAssured);
                double val2 = Math.max(val1, (sum_I * 1.05));

                this.deathBenefit_AU = cfap.getRoundOffLevel2New_Saral(cfap.getStringWithout_E(val2));
            }

        }


    }

    public String getDeathBenefit_AU() {
        return deathBenefit_AU;
    }

    public void setServiceTaxOnFMCReductionYield_Y() {
        double a = 0;
        if (prop.fundManagementChargesYield) {
            a = (Double.parseDouble(getFundManagementCharge_X()));
        } else {
            a = 0;
        }
        this.serviceTaxOnFMCReductionYield_Y = cfap.roundUp_Level2(cfap.getStringWithout_E(a * getServiceTax()));
    }

    public String getServiceTaxOnFMCReductionYield_Y() {
        return serviceTaxOnFMCReductionYield_Y;
    }

    public void setFundValueAfterFMCAndBeforeGAReductionYield_Z(int policyTerm) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.fundValueAfterFMCBerforeGAReductionYield_Z = "" + (Double.parseDouble(getFundBeforeFMC_W()) - Double.parseDouble(getFundManagementCharge_X()) - Double.parseDouble(getServiceTaxOnFMCReductionYield_Y()));
        } else {
            this.fundValueAfterFMCBerforeGAReductionYield_Z = "" + 0;
        }
    }

    public String getFundValueAfterFMCAndBeforeGAReductionYield_Z() {
        return fundValueAfterFMCBerforeGAReductionYield_Z;
    }

    public void setTotalServiceTaxReductionYield_U(double riderCharges) {
        double temp1 = 0, temp2 = 0;
        temp1 = Double.parseDouble(getServiceTaxOnAllocation_M()) + Double.parseDouble(getServiceTax_exclOfSTonAllocAndSurr_T()) + Double.parseDouble(getServiceTaxOnFMCReductionYield_Y());
        if (prop.riderChargesYield) {
            temp2 = riderCharges;
        } else {
            temp2 = 0;
        }
        this.totalServiceTaxReductionYield_U = cfap.roundUp_Level2(cfap.getStringWithout_E(temp1 + temp2 * getServiceTax()));
    }

    public String getTotalServiceTaxReductionYield_U() {
        return totalServiceTaxReductionYield_U;
    }

    public void setFundValueAtEndReductionYield_AB() {
        fundValueAtEndReductionYield_AB = "" + (Double.parseDouble(getGuaranteedAddition_AA()) + Double.parseDouble(getFundValueAfterFMCAndBeforeGAReductionYield_Z()));

        this.fundValueAtEndReductionYield_AB = "" + (Double.parseDouble(getGuaranteedAddition_AA()) + Double.parseDouble(getFundValueAfterFMCAndBeforeGAReductionYield_Z()));
    }

    public String getFundValueAtEndReductionYield_AB() {
        return fundValueAtEndReductionYield_AB;
    }

    public void setSurrenderChargesReductionYield_AC(double premiumamount) {
        double surrenderCharge = 0;

        if (premiumamount <= 25000) {
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
        }


        double val1 = Math.min(Double.parseDouble(getFundValueAtEndReductionYield_AB()), premiumamount);
        double val2 = val1 * surrenderCharge;
        double val3 = Math.min(Double.parseDouble(getSurrenderCap_AV()), val2);

        //return surrenderCharges_AC =cfap.roundUp_Level2( cfap.getStringWithout_E(val3)) ;
        this.surrenderChargesReductionYield_AC = cfap.getRoundOffLevel2New_Saral(cfap.getStringWithout_E(val3));
    }

    public String getSurrenderChargesReductionYield_AC() {
        return surrenderChargesReductionYield_AC;
    }


    public double getSurrenderCharge(int PPT, double effectivePremium) {
        double surrenderCharge = 0;
        if (PPT == 1) {
            //surrenderCharge=1;
            surrenderCharge = Double.parseDouble(calSurrRateArrSP()[Math.min((Integer.parseInt(getYear_F()) - 1), 11)]);
        } else {
            surrenderCharge = Double.parseDouble(calSurrRateArrRP(effectivePremium)[Math.min((Integer.parseInt(getYear_F()) - 1), 11)]);
        }
        return surrenderCharge;
    }


    public void setServiceTaxOnSurrenderChargesReductionYield_AD() {
        if (prop.surrenderChargesYield) {
            this.serviceTaxOnSurrenderChargesReductionYield_AD = cfap.roundUp_Level2(cfap.getStringWithout_E(Double.parseDouble(getSurrenderChargesReductionYield_AC()) * getServiceTax()));
        } else {
            this.serviceTaxOnSurrenderChargesReductionYield_AD = "" + 0;
        }
    }

    public String getServiceTaxOnSurrenderChargesReductionYield_AD() {
        return serviceTaxOnSurrenderChargesReductionYield_AD;
    }

    public void setSurrenderValueReductionYield_AE() {
        this.surrenderValueReductionYield_AE = "" + (Double.parseDouble(getFundValueAtEndReductionYield_AB()) - Double.parseDouble(getSurrenderChargesReductionYield_AC()) - Double.parseDouble(getServiceTaxOnSurrenderChargesReductionYield_AD()));
    }

    public String getSurrenderValueReductionYield_AE() {
        return surrenderValueReductionYield_AE;
    }

    public void setDeathBenefitReductionYield_AF(int policyTerm, double sumAssured) {
        if (Integer.parseInt(getYear_F()) > policyTerm) {
            this.deathBenefitReductionYield_AF = "" + 0;
        } else {
            this.deathBenefitReductionYield_AF = "" + (Math.max(Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()), Math.max(sumAssured, Double.parseDouble(getFundValueAtEndReductionYield_AB()))));
        }
    }

    public String getDeathBenefitReductionYield_AF() {
        return deathBenefitReductionYield_AF;
    }

    public void setServiceTaxOnFMCReductionYield_AN() {
        double a = 0;
        if (prop.fundManagementChargesYield) {
            a = (Double.parseDouble(getFundManagementCharge_AM()));
        } else {
            a = 0;
        }
        this.serviceTaxOnFMCReductionYield_AN = cfap.roundUp_Level2(cfap.getStringWithout_E(a * getServiceTax()));
    }

    public String getServiceTaxOnFMCReductionYield_AN() {
        return serviceTaxOnFMCReductionYield_AN;
    }

    public void setFundValueAfterFMCAndBeforeGAReductionYield_AO(int policyTerm) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.fundValueAfterFMCBerforeGAReductionYield_AO = "" + (Double.parseDouble(getFundBeforeFMC_AL()) - Double.parseDouble(getFundManagementCharge_AM()) - Double.parseDouble(getServiceTaxOnFMCReductionYield_AN()));
        } else {
            this.fundValueAfterFMCBerforeGAReductionYield_AO = "" + 0;
        }
    }

    public String getFundValueAfterFMCAndBeforeGAReductionYield_AO() {
        return fundValueAfterFMCBerforeGAReductionYield_AO;
    }

    public void setTotalServiceTaxReductionYield_AJ(double riderCharges) {
        double temp1 = 0, temp2 = 0;
        temp1 = Double.parseDouble(getServiceTaxOnAllocation_M()) + Double.parseDouble(getServiceTax_exclOfSTonAllocAndSurr_AI()) + Double.parseDouble(getServiceTaxOnFMCReductionYield_AN());
        if (prop.riderCharges) {
            temp2 = riderCharges;
        } else {
            temp2 = 0;
        }
        this.totalServiceTaxReductionYield_AJ = cfap.roundUp_Level2(cfap.getStringWithout_E(temp1 + temp2 * getServiceTax()));
    }

    public String getTotalServiceTaxReductionYield_AJ() {
        return totalServiceTaxReductionYield_AJ;
    }

    public void setFundValueAtEndReductionYield_AQ() {
        this.fundValueAtEndReductionYield_AQ = "" + (Double.parseDouble(getGuaranteedAddition_AP()) + Double.parseDouble(getFundValueAfterFMCAndBeforeGAReductionYield_AO()));
    }

    public String getFundValueAtEndReductionYield_AQ() {
        return fundValueAtEndReductionYield_AQ;
    }

    public void setSurrenderChargesReductionYield_AR(double effectivePremium, int PPT) {
        double a = Math.min(Double.parseDouble(getFundValueAtEndReductionYield_AQ()), (double) effectivePremium);
        //System.out.println("a "+a);
        double b = getSurrenderCharge(PPT, effectivePremium);
        //System.out.println("b "+b);
        //System.out.println("a*b "+(a*b));
        this.surrenderChargesReductionYield_AR = cfap.roundUp_Level2(cfap.getStringWithout_E(Math.min((a * b), Double.parseDouble(getSurrenderCap_AV()))));
    }

    public String getSurrenderChargesReductionYield_AR() {
        return surrenderChargesReductionYield_AR;
    }

    public void setServiceTaxOnSurrenderChargesReductionYield_AS() {
        if (prop.surrenderChargesYield) {
            this.serviceTaxOnSurrenderChargesReductionYield_AS = cfap.roundUp_Level2(cfap.getStringWithout_E(Double.parseDouble(getSurrenderChargesReductionYield_AR()) * getServiceTax()));
        } else {
            this.serviceTaxOnSurrenderChargesReductionYield_AS = "" + 0;
        }
    }

    public String getServiceTaxOnSurrenderChargesReductionYield_AS() {
        return serviceTaxOnSurrenderChargesReductionYield_AS;
    }

    public void setSurrenderValueReductionYield_AT() {
        this.surrenderValueReductionYield_AT = "" + (Double.parseDouble(getFundValueAtEndReductionYield_AQ()) - Double.parseDouble(getSurrenderChargesReductionYield_AC()) - Double.parseDouble(getServiceTaxOnSurrenderChargesReductionYield_AS()));
    }

    public String getSurrenderValueReductionYield_AT() {
        return surrenderValueReductionYield_AT;
    }

    public void setDeathBenefitReductionYield_AU(int policyTerm, double sumAssured) {
        if (Integer.parseInt(getYear_F()) > policyTerm) {
            this.deathBenefitReductionYield_AU = "" + 0;
        } else {
            this.deathBenefitReductionYield_AU = "" + (Math.max(Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()), Math.max(sumAssured, Double.parseDouble(getFundValueAtEndReductionYield_AQ()))));
        }
    }

    public String getDeathBenefitReductionYield_AU() {
        return deathBenefitReductionYield_AU;
    }

    public void setMonth_BB(int monthNumber) {
        this.month_BB = "" + monthNumber;
    }

    public String getMonth_BB() {
        return month_BB;
    }

    public void setReductionYield_BI(int noOfYearsElapsedSinceInception, double _fundValueAtEnd_AQ) {
        double a, b;
//        if((Integer.parseInt(getMonth_E())) <= (noOfYearsElapsedSinceInception*12)+1)
        if ((Integer.parseInt(getMonth_BB())) < (noOfYearsElapsedSinceInception * 12)) {
            a = -(Double.parseDouble(getPremium_I()));
        } else {
            a = 0;
        }

        if ((Integer.parseInt(getMonth_BB())) == (noOfYearsElapsedSinceInception * 12)) {
            b = _fundValueAtEnd_AQ;
        } else {
            b = 0;
        }
//        System.out.println("a_BU "+a);
//        System.out.println("b_BU "+b);
        this.reductionYield_BI = "" + (a + b);
    }

    public String getReductionYield_BI() {
        return reductionYield_BI;
    }

    public void setReductionYield_BD(int policyTerm, double _FundValueAtEnd_RIY8) {
        double a, b;
        if ((Integer.parseInt(getMonth_BB())) == (policyTerm * 12)) {
//            System.out.println("inside if");
            //System.out.println("getFundValueAtEndReductionYield_AS "+getFundValueAtEndReductionYield_AS());
            a = _FundValueAtEnd_RIY8;
        } else {
            a = 0;
        }

        if ((Integer.parseInt(getMonth_BB())) < (policyTerm * 12)) {
            b = -(Double.parseDouble(getPremium_I()));
        } else {
            b = 0;
        }
//        System.out.println("a_BQ" +a);
        //this.reductionYield_BQ=""+(Double.parseDouble(getPremium_I()) + a);
        this.reductionYield_BD = "" + (b + a);
    }

    public String getReductionYield_BD() {
        return reductionYield_BD;
    }

    public void setIRRAnnual_BD(double ans) {
//    	System.out.println("aaaaaaa "+((cfap.pow((1+ans),12)-1)*100));
        this.irrAnnual_BD = "" + ((cfap.pow((1 + ans), 12) - 1));
    }

    public String getIRRAnnual_BD() {
        return irrAnnual_BD;
    }


    public void setIRRAnnual_BI(double ans) {
        //System.out.println("aaaaaaa "+((comm.pow((1+ans),12)-1)*100));
        this.irrAnnual_BI = "" + ((cfap.pow((1 + ans), 12) - 1));
    }

    public String getIRRAnnual_BI() {
        return irrAnnual_BI;
    }

    public void setReductionInYieldMaturityAt(double int2) {
        this.reductionInYieldMaturityAt = "" + ((int2 - Double.parseDouble(getIRRAnnual_BD())) * 100);
    }

    public String getReductionInYieldMaturityAt() {
        return reductionInYieldMaturityAt;
    }

    public void setReductionInYieldNumberOfYearsElapsedSinceInception(double int2) {
        this.reductionInYieldNumberOfYearsElapsedSinceInception = "" + ((int2 - Double.parseDouble(getIRRAnnual_BI())) * 100);
    }

    public String getReductionInYieldNumberOfYearsElapsedSinceInception() {
        return reductionInYieldNumberOfYearsElapsedSinceInception;
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
        	  /* System.out.println("fValue before"+fValue);
        	   System.out.println("fDerivative before"+fDerivative);*/

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


    public double getCommistionRate(SaralInsureWealthPlusBean saralIinsurewealthPlusBean) {
        int findYear;

        if (Integer.parseInt(year_F) > 10) {
            findYear = 11;
        } else
            findYear = Integer.parseInt(year_F);

        if (saralIinsurewealthPlusBean.getIsForStaffOrNot()) {
            return 0;
        } else if (saralIinsurewealthPlusBean.getPlanType().equals("Limited")) {
            double[] lpptArr = {0.1, 0.02, 0.02, 0.02, 0.02, 0.015, 0.015, 0.01, 0.01, 0.01, 0};
            return lpptArr[findYear - 1];
        } else if (saralIinsurewealthPlusBean.getPlanType().equals("Single")) {
            double[] pptArr = {0.02, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            return pptArr[findYear - 1];
        } else {
//			double []commArr = {0.1,0.02,0.02,0.02,0.02,0.015,0.015,0.01,0.01,0.01,0.01};
            double[] commArr = {0.13, 0.02, 0.02, 0.02, 0.02, 0.02, 0.02, 0.02, 0.02, 0.02, 0.02};

            return commArr[findYear - 1];
        }


    }

    public double getCommission_BL(double annualisesPrem, double topupPrem, SaralInsureWealthPlusBean saralIinsurewealthPlusBean) {
        double topup = 0;
        return getCommistionRate(saralIinsurewealthPlusBean) * annualisesPrem + (topupPrem * topup);


    }

    public double getStaffRebate(boolean IsStaff) {

        if (IsStaff) {
            StaffRebate = 0.06;
        } else {
            StaffRebate = 0.00;
        }
        return StaffRebate;

    }
}
