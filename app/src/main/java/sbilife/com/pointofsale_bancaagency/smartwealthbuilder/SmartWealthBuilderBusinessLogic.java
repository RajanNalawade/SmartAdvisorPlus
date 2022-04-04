/************************************************************************************************************************************
 Author-> Vrushali Chaudhari
 Class Description-> Business logic of ULIP-Smart Wealth Builder Calculator is implemented here in this class
 *************************************************************************************************************************************/

package sbilife.com.pointofsale_bancaagency.smartwealthbuilder;

import java.text.DecimalFormat;
import java.util.ArrayList;

import sbilife.com.pointofsale_bancaagency.common.CommonForAllProd;

/**
 * @author e21946
 */
class SmartWealthBuilderBusinessLogic {

    private CommonForAllProd cfap = null;
    private SmartWealthBuilderProperties prop = null;

    private String month_E = null;
    private String year_F = null;
    private String age_H = null;
    private String policyInforce_G = "Y";
    private String premium_I = null;
    private String topUpPremium_J = null;
    private String premiumAllocationCharge_K = null;
    private String topUpCharges_L = null;
    private String serviceTaxOnAllocation_M = null;
    private String amountAvailableForInvestment_N = null,
            amountAvailableForInvestment_N1 = null;
    private String sumAssuredRelatedCharges_O = null;
    String riderCharges_P = null;
    private String policyAdministrationCharge_Q = null;
    private String mortalityCharges_R = null;
    private String totalCharges_S = null;
    private String serviceTaxExclOfSTOnAllocAndSurr_T = null;
    private String totalServiceTax_U = null;
    private String additionToFundIfAny_V = null;
    private String fundBeforeFMC_W = null;
    private String fundManagementCharge_X = null;
    private String serviceTaxOnFMC_Y = null;
    private String fundValueAfterFMCBerforeGA_Z = null;
    private String guaranteedAddition_AA = null;
    private String fundValueAtEnd_AB = null;
    private String surrenderCharges_AC = null;
    private String serviceTaxOnSurrenderCharges_AD = null;
    private String surrenderValue_AE = null;
    private String deathBenefit_AF = null;
    private String mortalityCharges_AG = null;
    private String totalCharges_AH = null;
    private String serviceTaxExclOfSTOnAllocAndSurr_AI = null;
    private String totalServiceTax_AJ = null;
    private String additionToFundIfAny_AK = null;
    private String fundBeforeFMC_AL = null;
    private String fundManagementCharge_AM = null;
    private String serviceTaxOnFMC_AN = null;
    private String fundValueAfterFMCBerforeGA_AO = null;
    private String guaranteedAddition_AP = null;
    private String fundValueAtEnd_AQ = null;
    private String surrenderCharges_AR = null;
    private String serviceTaxOnSurrenderCharges_AS = null;
    private String surrenderValue_AT = null;
    private String deathBenefit_AU = null;
    private String surrenderCap_AV = null;
    private String oneHundredPercentOfCummulativePremium_AW = null;
    private String serviceTaxOnFMCReductionYield_Y = null;
    private String fundValueAfterFMCBerforeGAReductionYield_Z = null;
    private String totalServiceTaxReductionYield_U = null;
    private String fundValueAtEndReductionYield_AB = null;
    private String surrenderChargesReductionYield_AC = null;
    private String serviceTaxOnSurrenderChargesReductionYield_AD = null;
    private String surrenderValueReductionYield_AE = null;
    private String deathBenefitReductionYield_AF = null;
    private String serviceTaxOnFMCReductionYield_AN = null;
    private String fundValueAfterFMCBerforeGAReductionYield_AO = null;
    private String totalServiceTaxReductionYield_AJ = null;
    private String fundValueAtEndReductionYield_AQ = null;
    private String surrenderChargesReductionYield_AR = null;
    private String serviceTaxOnSurrenderChargesReductionYield_AS = null;
    private String surrenderValueReductionYield_AT = null;
    private String deathBenefitReductionYield_AU = null;
    private String month_BB = null;
    private String reductionYield_BI = null;
    private String reductionYield_BD = null;
    private String irrAnnual_BD = null;
    private String irrAnnual_BI = null;
    private String reductionInYieldMaturityAt = null;
    private String reductionInYieldNumberOfYearsElapsedSinceInception = null;
    private String netYieldAt8Percent = null;// Added by Akshaya on 18-Feb-2014

    /************ Added by Akshaya start ****************************/

    public double getStaffPercentage(boolean isStaff, String planType, int PPT) {
        double staffPercentage = 0;
        if (isStaff) {
            if (planType.equals("Single")) {
                staffPercentage = 0.02;
            } else if (planType.equals("Regular")) {
                staffPercentage = 0.09;
            } else {
                if (PPT == 5) {
                    staffPercentage = 0.09;
                } else if (PPT == 8) {
                    staffPercentage = 0.09;
                } else if (PPT == 10) {
                    staffPercentage = 0.09;
                }
            }
        } else {
            staffPercentage = 0.0;
        }
        return staffPercentage;
    }

    public String getStaffDiscCode(boolean isStaff) {
        if (isStaff)
            return "SBI";
        else
            return "None";
    }

    /************ Added by Akshaya end ****************************/

    public SmartWealthBuilderBusinessLogic() {
        cfap = new CommonForAllProd();
        prop = new SmartWealthBuilderProperties();
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
                premium_I = "" + (effectivePrem / PF);
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

    public void setTopUpPremium_J(boolean topUp, double effectiveTopUpPrem) {
        if (getMonth_E().equals("1") && topUp) {
            this.topUpPremium_J = ("" + effectiveTopUpPrem);
        } else {
            this.topUpPremium_J = ("" + 0);
        }
    }

    public String getTopUpPremium_J() {
        return topUpPremium_J;
    }

    public void setPremiumAllocationCharge_K(boolean staffDisc,
                                             boolean bancAssuranceDisc, String planType, int PPT,
                                             double effectivePremium) {
        if (Integer.parseInt(getYear_F()) > 10) {
            this.premiumAllocationCharge_K = ("" + 0);
        } else
//        {this.premiumAllocationCharge_K= cfap.getRoundUp(cfap.getStringWithout_E(getAllocationCharge(staffDisc,bancAssuranceDisc,planType,PPT,effectivePremium)* Double.parseDouble(getPremium_I())),"roundUpII");}
            /*        {this.premiumAllocationCharge_K= cfap.getRoundUp(cfap.getStringWithout_E(getAllocationCharge(staffDisc,bancAssuranceDisc,planType,PPT,effectivePremium)* Double.parseDouble(getPremium_I())));}*/ {
            this.premiumAllocationCharge_K = cfap.getRound(cfap.getStringWithout_E(getAllocationCharge(staffDisc, bancAssuranceDisc, planType, PPT, effectivePremium) * Double.parseDouble(getPremium_I())));
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
        if (planType.equals("Limited")) {
            if (getYear_F().equals("1")) {
                if (staffDisc == true && bancAssuranceDisc == false) {
                    temp = 0.09 - 0.10;
                } else if (staffDisc == false && bancAssuranceDisc == true) {
                    temp = (0.09 - 0.10 * 1);
                } else {
                    temp = 0.09;
                }
                return Math.max(temp, 0);
            } else if (getYear_F().equals("2")) {
                if (staffDisc == true && bancAssuranceDisc == false) {
                    temp = 0.065 - 0.02;
                } else if (staffDisc == false && bancAssuranceDisc == true) {
                    temp = (0.065 - 0.02 * 0);
                } else {
                    temp = 0.065;
                }
                return Math.max(temp, 0);
            } else if (getYear_F().equals("3")) {
                if (staffDisc == true && bancAssuranceDisc == false) {
                    temp = 0.065 - 0.02;
                } else if (staffDisc == false && bancAssuranceDisc == true) {
                    temp = (0.065 - 0.02 * 0);
                } else {
                    temp = 0.065;
                }
                return Math.max(temp, 0);
            } else if (getYear_F().equals("4")) {
                if (staffDisc == true && bancAssuranceDisc == false) {
                    temp = 0.06 - 0.02;
                } else if (staffDisc == false && bancAssuranceDisc == true) {
                    temp = (0.06 - 0.02 * 0);
                } else {
                    temp = 0.06;
                }
                return Math.max(temp, 0);
            } else if (getYear_F().equals("5")) {
                if (staffDisc == true && bancAssuranceDisc == false) {
                    temp = 0.06 - 0.02;
                } else if (staffDisc == false && bancAssuranceDisc == true) {
                    temp = (0.06 - 0.02 * 0);
                } else {
                    temp = 0.06;
                }
                return Math.max(temp, 0);
            } else if (getYear_F().equals("6")) {
                if (staffDisc == true && bancAssuranceDisc == false) {
                    temp = 0.035 - 0.015;
                } else if (staffDisc == false && bancAssuranceDisc == true) {
                    temp = (0.035 - 0.015 * 0);
                } else {
                    temp = 0.035;
                }
                return Math.max(temp, 0);
            } else if (getYear_F().equals("7")) {
                if (staffDisc == true && bancAssuranceDisc == false) {
                    temp = 0.035 - 0.015;
                } else if (staffDisc == false && bancAssuranceDisc == true) {
                    temp = (0.035 - 0.015 * 0);
                } else {
                    temp = 0.035;
                }
                return Math.max(temp, 0);
            } else if (getYear_F().equals("8")) {
                if (staffDisc == true && bancAssuranceDisc == false) {
                    temp = 0.03 - 0.01;
                } else if (staffDisc == false && bancAssuranceDisc == true) {
                    temp = (0.03 - 0.01 * 0);
                } else {
                    temp = 0.03;
                }
                return Math.max(temp, 0);
            } else if (getYear_F().equals("9")) {
                if (staffDisc == true && bancAssuranceDisc == false) {
                    temp = 0.03 - 0.01;
                } else if (staffDisc == false && bancAssuranceDisc == true) {
                    temp = (0.03 - 0.01 * 0);
                } else {
                    temp = 0.03;
                }
                return Math.max(temp, 0);
            } else if (getYear_F().equals("10")) {
                if (staffDisc == true && bancAssuranceDisc == false) {
                    temp = 0.03 - 0.01;
                } else if (staffDisc == false && bancAssuranceDisc == true) {
                    temp = (0.03 - 0.01 * 0);
                } else {
                    temp = 0.03;
                }
                return Math.max(temp, 0);
            } else {
                return 0;
            }
        }
        // For Single Mode of Premium Freqency
        else {
            // For Single Frequency Mode
            if (PPT == 1) {
                if (getYear_F().equals("1")) {
                    if (effectivePremium >= 24000 && effectivePremium <= 100000) {
                        if (staffDisc == true && bancAssuranceDisc == false) {
                            return Math.max(0, (0.03 - 0.02));
                        } else {
                            if (staffDisc == false && bancAssuranceDisc == true) {
                                return Math
                                        .max(0, ((0.03 - 0.02) * yearFactor));
                            } else {
                                return Math.max(0, 0.03);
                            }
                        }
                    } else if (effectivePremium >= 100001
                            && effectivePremium <= 500000) {
                        if (staffDisc == true && bancAssuranceDisc == false) {
                            return Math.max(0, (0.03 - 0.02));
                        } else {
                            if (staffDisc == false && bancAssuranceDisc == true) {
                                return Math
                                        .max(0, ((0.03 - 0.02) * yearFactor));
                            } else {
                                return Math.max(0, 0.03);
                            }
                        }
                    } else if (effectivePremium > 500000) {
                        if (staffDisc == true && bancAssuranceDisc == false) {
                            return Math.max(0, (0.03 - 0.02));
                        } else {
                            if (staffDisc == false && bancAssuranceDisc == true) {
                                return Math
                                        .max(0, ((0.03 - 0.02) * yearFactor));
                            } else {
                                return Math.max(0, 0.03);
                            }
                        }
                    } else {
                        return 0;
                    }
                } else {
                    return 0;
                }
            }
            // For Regular Frequency Mode
            else {
                if (getYear_F().equals("1")) {
                    if (effectivePremium >= 24000 && effectivePremium <= 100000) {
                        if (staffDisc == true && bancAssuranceDisc == false) {
                            return Math.max(0, (0.09 - 0.1));
                        } else {
                            if (staffDisc == false && bancAssuranceDisc == true) {
                                return Math.max(0, (0.09 - 0.1 * yearFactor));
                            } else {
                                return Math.max(0, 0.09);
                            }
                        }
                    } else if (effectivePremium >= 100001
                            && effectivePremium <= 500000) {
                        if (staffDisc == true && bancAssuranceDisc == false) {
                            return Math.max(0, (0.09 - 0.1));
                        } else {
                            if (staffDisc == false && bancAssuranceDisc == true) {
                                return Math.max(0, (0.09 - 0.1 * yearFactor));
                            } else {
                                return Math.max(0, 0.09);
                            }
                        }
                    } else if (effectivePremium > 500000) {
                        if (staffDisc == true && bancAssuranceDisc == false) {
                            return Math.max(0, (0.09 - 0.1));
                        } else {
                            if (staffDisc == false && bancAssuranceDisc == true) {
                                return Math.max(0, (0.09 - 0.1 * yearFactor));
                            } else {
                                return Math.max(0, 0.09);
                            }
                        }
                    } else {
                        return 0;
                    }
                } else if (getYear_F().equals("2")) {
                    if (effectivePremium >= 24000 && effectivePremium <= 100000) {
                        if (staffDisc == true && bancAssuranceDisc == false) {
                            return Math.max(0, (0.065 - 0.02));
                        } else {
                            if (staffDisc == false && bancAssuranceDisc == true) {
                                return Math.max(0, (0.065 - 0.02 * yearFactor));
                            } else {
                                return Math.max(0, (0.065));
                            }
                        }
                    } else if (effectivePremium >= 100001
                            && effectivePremium <= 500000) {
                        if (staffDisc == true && bancAssuranceDisc == false) {
                            return Math.max(0, (0.065 - 0.02));
                        } else {
                            if (staffDisc == false && bancAssuranceDisc == true) {
                                return Math.max(0, (0.065 - 0.02 * yearFactor));
                            } else {
                                return Math.max(0, (0.065));
                            }
                        }
                    } else if (effectivePremium > 500000) {
                        if (staffDisc == true && bancAssuranceDisc == false) {
                            return Math.max(0, (0.065 - 0.02));
                        } else {
                            if (staffDisc == false && bancAssuranceDisc == true) {
                                return Math.max(0, (0.065 - 0.02 * yearFactor));
                            } else {
                                return Math.max(0, (0.065));
                            }
                        }
                    } else {
                        return 0;
                    }
                } else if (getYear_F().equals("3")) {
                    if (effectivePremium >= 24000 && effectivePremium <= 100000) {
                        if (staffDisc == true && bancAssuranceDisc == false) {
                            return Math.max(0, (0.065 - 0.02));
                        } else {
                            if (staffDisc == false && bancAssuranceDisc == true) {
                                return Math.max(0, (0.065 - 0.02 * yearFactor));
                            } else {
                                return Math.max(0, (0.065));
                            }
                        }
                    } else if (effectivePremium >= 100001
                            && effectivePremium <= 500000) {
                        if (staffDisc == true && bancAssuranceDisc == false) {
                            return Math.max(0, (0.065 - 0.02));
                        } else {
                            if (staffDisc == false && bancAssuranceDisc == true) {
                                return Math.max(0, (0.065 - 0.02 * yearFactor));
                            } else {
                                return Math.max(0, (0.065));
                            }
                        }
                    } else if (effectivePremium > 500000) {
                        if (staffDisc == true && bancAssuranceDisc == false) {
                            return Math.max(0, (0.065 - 0.02));
                        } else {
                            if (staffDisc == false && bancAssuranceDisc == true) {
                                return Math.max(0, (0.065 - 0.02 * yearFactor));
                            } else {
                                return Math.max(0, (0.065));
                            }
                        }
                    } else {
                        return 0;
                    }
                } else if (getYear_F().equals("4")) {
                    if (effectivePremium >= 24000 && effectivePremium <= 100000) {
                        if (staffDisc == true && bancAssuranceDisc == false) {
                            return Math.max(0, (0.06 - 0.02));
                        } else {
                            if (staffDisc == false && bancAssuranceDisc == true) {
                                return Math.max(0, (0.06 - 0.02 * yearFactor));
                            } else {
                                return Math.max(0, (0.06));
                            }
                        }
                    } else if (effectivePremium >= 100001
                            && effectivePremium <= 500000) {
                        if (staffDisc == true && bancAssuranceDisc == false) {
                            return Math.max(0, (0.06 - 0.02));
                        } else {
                            if (staffDisc == false && bancAssuranceDisc == true) {
                                return Math.max(0, (0.06 - 0.02 * yearFactor));
                            } else {
                                return Math.max(0, (0.06));
                            }
                        }
                    } else if (effectivePremium > 500000) {
                        if (staffDisc == true && bancAssuranceDisc == false) {
                            return Math.max(0, (0.06 - 0.02));
                        } else {
                            if (staffDisc == false && bancAssuranceDisc == true) {
                                return Math.max(0, (0.06 - 0.02 * yearFactor));
                            } else {
                                return Math.max(0, (0.06));
                            }
                        }
                    } else {
                        return 0;
                    }
                } else if (getYear_F().equals("5")) {
                    if (effectivePremium >= 24000 && effectivePremium <= 100000) {
                        if (staffDisc == true && bancAssuranceDisc == false) {
                            return Math.max(0, (0.06 - 0.02));
                        } else {
                            if (staffDisc == false && bancAssuranceDisc == true) {
                                return Math.max(0, (0.06 - 0.02 * yearFactor));
                            } else {
                                return Math.max(0, (0.06));
                            }
                        }
                    } else if (effectivePremium >= 100001
                            && effectivePremium <= 500000) {
                        if (staffDisc == true && bancAssuranceDisc == false) {
                            return Math.max(0, (0.06 - 0.02));
                        } else {
                            if (staffDisc == false && bancAssuranceDisc == true) {
                                return Math.max(0, (0.06 - 0.02 * yearFactor));
                            } else {
                                return Math.max(0, (0.06));
                            }
                        }
                    } else if (effectivePremium > 500000) {
                        if (staffDisc == true && bancAssuranceDisc == false) {
                            return Math.max(0, (0.06 - 0.02));
                        } else {
                            if (staffDisc == false && bancAssuranceDisc == true) {
                                return Math.max(0, (0.06 - 0.02 * yearFactor));
                            } else {
                                return Math.max(0, (0.06));
                            }
                        }
                    } else {
                        return 0;
                    }
                } else if (getYear_F().equals("6")) {
                    if (effectivePremium >= 24000 && effectivePremium <= 100000) {
                        if (staffDisc == true && bancAssuranceDisc == false) {
                            return Math.max(0, (0.035 - 0.015));
                        } else {
                            if (staffDisc == false && bancAssuranceDisc == true) {
                                return Math
                                        .max(0, (0.035 - 0.015 * yearFactor));
                            } else {
                                return Math.max(0, (0.035));
                            }
                        }
                    } else if (effectivePremium >= 100001
                            && effectivePremium <= 500000) {
                        if (staffDisc == true && bancAssuranceDisc == false) {
                            return Math.max(0, (0.035 - 0.015));
                        } else {
                            if (staffDisc == false && bancAssuranceDisc == true) {
                                return Math
                                        .max(0, (0.035 - 0.015 * yearFactor));
                            } else {
                                return Math.max(0, (0.035));
                            }
                        }
                    } else if (effectivePremium > 500000) {
                        if (staffDisc == true && bancAssuranceDisc == false) {
                            return Math.max(0, (0.035 - 0.015));
                        } else {
                            if (staffDisc == false && bancAssuranceDisc == true) {
                                return Math
                                        .max(0, (0.035 - 0.015 * yearFactor));
                            } else {
                                return Math.max(0, (0.035));
                            }
                        }
                    } else {
                        return 0;
                    }
                } else if (getYear_F().equals("7")) {
                    if (effectivePremium >= 24000 && effectivePremium <= 100000) {
                        if (staffDisc == true && bancAssuranceDisc == false) {
                            return Math.max(0, (0.035 - 0.015));
                        } else {
                            if (staffDisc == false && bancAssuranceDisc == true) {
                                return Math
                                        .max(0, (0.035 - 0.015 * yearFactor));
                            } else {
                                return Math.max(0, (0.035));
                            }
                        }
                    } else if (effectivePremium >= 100001
                            && effectivePremium <= 500000) {
                        if (staffDisc == true && bancAssuranceDisc == false) {
                            return Math.max(0, (0.035 - 0.015));
                        } else {
                            if (staffDisc == false && bancAssuranceDisc == true) {
                                return Math
                                        .max(0, (0.035 - 0.015 * yearFactor));
                            } else {
                                return Math.max(0, (0.035));
                            }
                        }
                    } else if (effectivePremium > 500000) {
                        if (staffDisc == true && bancAssuranceDisc == false) {
                            return Math.max(0, (0.035 - 0.015));
                        } else {
                            if (staffDisc == false && bancAssuranceDisc == true) {
                                return Math
                                        .max(0, (0.035 - 0.015 * yearFactor));
                            } else {
                                return Math.max(0, (0.035));
                            }
                        }
                    } else {
                        return 0;
                    }
                } else if (getYear_F().equals("8")) {
                    if (effectivePremium >= 24000 && effectivePremium <= 100000) {
                        if (staffDisc == true && bancAssuranceDisc == false) {
                            return Math.max(0, (0.03 - 0.01));
                        } else {
                            if (staffDisc == false && bancAssuranceDisc == true) {
                                return Math.max(0, (0.03 - 0.01 * yearFactor));
                            } else {
                                return Math.max(0, (0.03));
                            }
                        }
                    } else if (effectivePremium >= 100001
                            && effectivePremium <= 500000) {
                        if (staffDisc == true && bancAssuranceDisc == false) {
                            return Math.max(0, (0.03 - 0.01));
                        } else {
                            if (staffDisc == false && bancAssuranceDisc == true) {
                                return Math.max(0, (0.03 - 0.01 * yearFactor));
                            } else {
                                return Math.max(0, (0.03));
                            }
                        }
                    } else if (effectivePremium > 500000) {
                        if (staffDisc == true && bancAssuranceDisc == false) {
                            return Math.max(0, (0.03 - 0.01));
                        } else {
                            if (staffDisc == false && bancAssuranceDisc == true) {
                                return Math.max(0, (0.03 - 0.01 * yearFactor));
                            } else {
                                return Math.max(0, (0.03));
                            }
                        }
                    } else {
                        return 0;
                    }
                } else if (getYear_F().equals("9")) {
                    if (effectivePremium >= 24000 && effectivePremium <= 100000) {
                        if (staffDisc == true && bancAssuranceDisc == false) {
                            return Math.max(0, (0.03 - 0.01));
                        } else {
                            if (staffDisc == false && bancAssuranceDisc == true) {
                                return Math.max(0, (0.03 - 0.01 * yearFactor));
                            } else {
                                return Math.max(0, (0.03));
                            }
                        }
                    } else if (effectivePremium >= 100001
                            && effectivePremium <= 500000) {
                        if (staffDisc == true && bancAssuranceDisc == false) {
                            return Math.max(0, (0.03 - 0.01));
                        } else {
                            if (staffDisc == false && bancAssuranceDisc == true) {
                                return Math.max(0, (0.03 - 0.01 * yearFactor));
                            } else {
                                return Math.max(0, (0.03));
                            }
                        }
                    } else if (effectivePremium > 500000) {
                        if (staffDisc == true && bancAssuranceDisc == false) {
                            return Math.max(0, (0.03 - 0.01));
                        } else {
                            if (staffDisc == false && bancAssuranceDisc == true) {
                                return Math.max(0, (0.03 - 0.01 * yearFactor));
                            } else {
                                return Math.max(0, (0.03));
                            }
                        }
                    } else {
                        return 0;
                    }
                } else if (getYear_F().equals("10")) {
                    if (effectivePremium >= 24000 && effectivePremium <= 100000) {
                        if (staffDisc == true && bancAssuranceDisc == false) {
                            return Math.max(0, (0.03 - 0.01));
                        } else {
                            if (staffDisc == false && bancAssuranceDisc == true) {
                                return Math.max(0, (0.03 - 0.01 * yearFactor));
                            } else {
                                return Math.max(0, (0.03));
                            }
                        }
                    } else if (effectivePremium >= 100001
                            && effectivePremium <= 500000) {
                        if (staffDisc == true && bancAssuranceDisc == false) {
                            return Math.max(0, (0.03 - 0.01));
                        } else {
                            if (staffDisc == false && bancAssuranceDisc == true) {
                                return Math.max(0, (0.03 - 0.01 * yearFactor));
                            } else {
                                return Math.max(0, (0.03));
                            }
                        }
                    } else if (effectivePremium > 500000) {
                        if (staffDisc == true && bancAssuranceDisc == false) {
                            return Math.max(0, (0.03 - 0.01));
                        } else {
                            if (staffDisc == false && bancAssuranceDisc == true) {
                                return Math.max(0, (0.03 - 0.01 * yearFactor));
                            } else {
                                return Math.max(0, (0.03));
                            }
                        }
                    } else {
                        return 0;
                    }
                } else {
                    return 0;
                }
            }
        }
    }

    public void setTopUpCharges_L(double topUp) {
        this.topUpCharges_L = cfap.getRoundUp(""
                + (Double.parseDouble(getTopUpPremium_J()) * topUp));
    }

    public String getTopUpCharges_L() {
        return topUpCharges_L;
    }

    public void setServiceTaxOnAllocation_M(boolean allocationCharges,
                                            double serviceTax) {
        if (allocationCharges) {
            this.serviceTaxOnAllocation_M = cfap
                    .roundUp_Level2(cfap.getStringWithout_E((Double
                            .parseDouble(getPremiumAllocationCharge_K()) + Double
                            .parseDouble(getTopUpCharges_L()))
                            * serviceTax));
        } else {
            this.serviceTaxOnAllocation_M = ("" + 0);
        }
    }

    public String getServiceTaxOnAllocation_M() {
        return serviceTaxOnAllocation_M;
    }

    public void setAmountAvailableForInvestment_N() {
        this.amountAvailableForInvestment_N = cfap.roundUp_Level2(""
                + (Double.parseDouble(getPremium_I())
                + Double.parseDouble(getTopUpPremium_J())
                - Double.parseDouble(getPremiumAllocationCharge_K())
                - Double.parseDouble(getTopUpCharges_L()) - Double
                .parseDouble(getServiceTaxOnAllocation_M())));
    }

    public String getAmountAvailableForInvestment_N() {
        return amountAvailableForInvestment_N;
    }

    //	Added By Saurabh Jain on 14/10/2019 Start
    public void setAmountAvailableForInvestment_N1() {
        this.amountAvailableForInvestment_N1 = cfap.roundUp_Level2("" + (Double.parseDouble(getPremium_I()) - Double.parseDouble(getPremiumAllocationCharge_K())));
    }

    public String getAmountAvailableForInvestment_N1() {
        return amountAvailableForInvestment_N1;
    }

//	Added By Saurabh Jain on 14/10/2019 Start

    public void setSumAssuredRelatedCharges_O(int policyTerm,
                                              double sumAssured, double charge_SumAssuredBase) {
        double a = 0;
        double b = 0;
        if (getMonth_E().equals("1")) {
            a = charge_SumAssuredBase;
        } else {
            a = 0;
        }
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            b = prop.charge_SA_ren;
        } else {
            b = 0;
        }
        this.sumAssuredRelatedCharges_O = cfap.roundUp_Level2(cfap
                .getStringWithout_E(sumAssured * (a + b)));
    }

    public String getSumAssuredRelatedCharges_O() {
        return sumAssuredRelatedCharges_O;
    }

    public void setPolicyAdministrationCharge_Q(
            double _policyAdministrationCharge_Q, int charge_Inflation,
            int fixedMonthlyExp_SP, int fixedMonthlyExp_RP,
            int inflation_pa_SP, int inflation_pa_RP, String planType) {
        if (Integer.parseInt(getMonth_E()) <= 60 && !planType.equals("Single")) {
            this.policyAdministrationCharge_Q = "" + 0;
        } else {
            if (getPolicyInForce_G().equals("Y")) {
                if (((Integer.parseInt(getMonth_E()) - 1) % 12) == 0) {
                    this.policyAdministrationCharge_Q = cfap
                            .roundUp_Level2(cfap.getStringWithout_E(getCharge_PP_Ren(
                                    fixedMonthlyExp_SP, fixedMonthlyExp_RP,
                                    inflation_pa_SP, inflation_pa_RP, planType)
                                    / 12
                                    * cfap.getPowerOutput(
                                    (1 + charge_Inflation),
                                    Integer.parseInt(""
                                            + (Integer
                                            .parseInt(getMonth_E()) / 12)))));
                } else {
                    this.policyAdministrationCharge_Q = cfap
                            .roundUp_Level2(cfap
                                    .getStringWithout_E(_policyAdministrationCharge_Q));
                }
            } else {
                this.policyAdministrationCharge_Q = "" + 0;
            }
        }
    }

    public String getPolicyAdministrationCharge_Q() {
        return policyAdministrationCharge_Q;
    }

    public int getCharge_PP_Ren(int fixedMonthlyExp_SP, int fixedMonthlyExp_RP, int inflation_pa_SP, int inflation_pa_RP, String premFreqMode) {
        if (premFreqMode.equals("Single")) {
            return (fixedMonthlyExp_SP * 12 * (1 + inflation_pa_SP) ^ 0);
        }
        // For Regular
        else {
            return (fixedMonthlyExp_RP * 12 * (1 + inflation_pa_RP) ^ 0);
        }
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

    public String[] getForBIArr(double mortalityCharges_AsPercentOfofLICtable) {
        int[] ageArr = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
                13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28,
                29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44,
                45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60,
                61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76,
                77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92,
                93, 94, 95, 96, 97, 98, 99, 100};
        String[] strArrTReturn = new String[99];
        SmartWealthBuilderDB smartWealthBuilderDB = new SmartWealthBuilderDB();

//        System.out.println("length "+smartWealthBuilderDB.getIAIarray().length);
        for (int i = 0; i < smartWealthBuilderDB.getIAIarray().length - 2; i++) {
//            strArrTReturn[i]=String.valueOf(((smartWealthBuilderDB.getIAIarray()[i]+smartWealthBuilderDB.getIAIarray()[i+1])/2) * mortalityCharges_AsPercentOfofLICtable);
//        	strArrTReturn[i]=String.valueOf((smartWealthBuilderDB.getIAIarray()[i]) * mortalityCharges_AsPercentOfofLICtable);

            strArrTReturn[i] = String.valueOf(((smartWealthBuilderDB.getIAIarray()[i] + smartWealthBuilderDB.getIAIarray()[i + 1]) / 2) * mortalityCharges_AsPercentOfofLICtable);

            // if(ageArr[i]<7)
            // {strArrTReturn[i]="0";}
            // else
            // {
//                //double a=(((smartWealthBuilderDB.getIAIarray()[i]+smartWealthBuilderDB.getIAIarray()[i+1])/2) * mortalityCharges_AsPercentOfofLICtable);
            // //double b=(double)1/12;
//                //double c=(1-cfap.pow((1-a*mortalityCharges_AsPercentOfofLICtable),b));
            // //strArrTReturn[i]=cfap.roundUp_Level2(cfap.getStringWithout_E(c));
//                strArrTReturn[i]=cfap.roundUp_Level2(cfap.getStringWithout_E(((smartWealthBuilderDB.getIAIarray()[i]+smartWealthBuilderDB.getIAIarray()[i+1])/2) * mortalityCharges_AsPercentOfofLICtable * 1000  ));
            // }

        }
        return strArrTReturn;
    }

    public void setMortalityCharges_R(double _fundValueAtEnd_AB,
                                      int policyTerm, String[] forBIArray, double sumAssured,
                                      boolean mortalityCharges) {
        if (!(getPolicyInForce_G().equals("Y"))
                || Integer.parseInt(getYear_F()) > policyTerm) {
            this.mortalityCharges_R = "" + 0;
        } else {
            double arrOutput = Double.parseDouble(forBIArray[Integer
                    .parseInt(getAge_H())]);

            // System.out.println("**********arroutput "+arrOutput);
            // double div=arrOutput/1000;
            // System.out.println("div "+div);
//            double a=arrOutput/12;
            // System.out.println("a "+a);

            DecimalFormat df = new DecimalFormat("#.#######");

            String roundTo7 = df.format(arrOutput);
            String test = cfap.getRoundOffLevel5(roundTo7);

//            System.out.println("arroutput RY "+arrOutput);
//            System.out.println("round to 5 RY"+test);

            double a = Double.parseDouble(test) / 12;
            double max1 = Math
                    .max(sumAssured,
                            Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()));

            // System.out.println("max1 "+max1);

            double b = (max1 - (Double
                    .parseDouble(getAmountAvailableForInvestment_N()) + _fundValueAtEnd_AB));


//            System.out.println("Minus amount" + (Double.parseDouble(getAmountAvailableForInvestment_N())+_fundValueAtEnd_AB));
            // System.out.println("N" + getAmountAvailableForInvestment_N());
            // System.out.println("AB" + _fundValueAtEnd_AB);

            // System.out.println("b "+b);

            int c = 0;
            if (mortalityCharges) {
                c = 1;
            } else {
                c = 0;
            }
//            this.mortalityCharges_R= cfap.roundUp_Level2(cfap.getStringWithout_E(((a*Math.max(b, 0))*c)));
            this.mortalityCharges_R = cfap.getRoundOffLevel2New(cfap.getStringWithout_E(((a * Math.max(b, 0)) * c)));
        }
    }

    public String getMortalityCharges_R() {
        return mortalityCharges_R;
    }

    public void setTotalCharges_S(int policyTerm, double riderCharges) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.totalCharges_S = ""
                    + (Double.parseDouble(getPolicyAdministrationCharge_Q())
                    + Double.parseDouble(getMortalityCharges_R())
                    + Double.parseDouble(getSumAssuredRelatedCharges_O()) + riderCharges);
        } else {
            this.totalCharges_S = "" + 0;
        }
    }

    public String getTotalCharges_S() {
        return totalCharges_S;
    }

    public void setServiceTax_exclOfSTonAllocAndSurr_T(double serviceTax,
                                                       boolean mortalityAndRiderCharges, boolean administrationCharges) {
        double a = 0, b = 0;
        if (mortalityAndRiderCharges) {
            a = Double.parseDouble(getMortalityCharges_R());
        } else {
            a = 0;
        }
        if (administrationCharges) {
            b = Double.parseDouble(getPolicyAdministrationCharge_Q());
        } else {
            b = 0;
        }
        this.serviceTaxExclOfSTOnAllocAndSurr_T = cfap.roundUp_Level2(cfap
                .getStringWithout_E((a + b) * serviceTax));
    }

    public String getServiceTax_exclOfSTonAllocAndSurr_T() {
        return serviceTaxExclOfSTOnAllocAndSurr_T;
    }

    public void setAdditionToFundIfAny_V(double _fundValueAtEnd_AB,
                                         int policyTerm, double riderCharges) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            double temp1 = 0, temp2 = 0;
            temp1 = (_fundValueAtEnd_AB
                    + Double.parseDouble(getAmountAvailableForInvestment_N())
                    - Double.parseDouble(getTotalCharges_S())
                    - Double.parseDouble(getServiceTax_exclOfSTonAllocAndSurr_T()) + riderCharges);
            double a = (double) 1 / 12;
            temp2 = (cfap.pow((1 + prop.int1), a)) - 1;
            this.additionToFundIfAny_V = cfap.roundUp_Level2(cfap
                    .getStringWithout_E(temp1 * temp2));
        } else {
            this.additionToFundIfAny_V = "" + 0;
        }
    }

    public String getAdditionToFundIfAny_V() {
        return additionToFundIfAny_V;
    }

    public void setFundBeforeFMC_W(double _fundValueAtEnd_AB, int policyTerm,
                                   double riderCharges) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.fundBeforeFMC_W = cfap
                    .getStringWithout_E(_fundValueAtEnd_AB
                            + Double.parseDouble(getAmountAvailableForInvestment_N())
                            - Double.parseDouble(getTotalCharges_S())
                            + Double.parseDouble(getAdditionToFundIfAny_V())
                            - Double.parseDouble(getServiceTax_exclOfSTonAllocAndSurr_T())
                            + riderCharges);
        } else {
            this.fundBeforeFMC_W = "" + 0;
        }
    }

    public String getFundBeforeFMC_W() {
        return fundBeforeFMC_W;
    }

    public void setFundManagementCharge_X(int policyTerm, double percentToBeInvested_EquityFund, double percentToBeInvested_EquityOptFund, double percentToBeInvested_GrowthFund, double percentToBeInvested_BalancedFund, double percentToBeInvested_BondFund, double percentToBeInvested_MoneyMarketFund, double percentToBeInvested_Top300Fund, double percentToBeInvested_BondOptimiserFund, double percentToBeInvested_MidcapFund, double percentToBeInvested_PureFund, double percentToBeInvested_CorpBondFund) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            if (Integer.parseInt(getMonth_E()) == 1) {
                this.fundManagementCharge_X = cfap.roundUp_Level2(cfap.getStringWithout_E(Double.parseDouble(getFundBeforeFMC_W()) * (prop.charge_Fund + (getCharge_fund_ren(percentToBeInvested_EquityFund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_BondFund, percentToBeInvested_MoneyMarketFund, percentToBeInvested_Top300Fund, percentToBeInvested_BondOptimiserFund, percentToBeInvested_MidcapFund, percentToBeInvested_PureFund, percentToBeInvested_CorpBondFund) / 12))));
            } else {
                this.fundManagementCharge_X = cfap.roundUp_Level2(cfap.getStringWithout_E(Double.parseDouble(getFundBeforeFMC_W()) * (getCharge_fund_ren(percentToBeInvested_EquityFund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_BondFund, percentToBeInvested_MoneyMarketFund, percentToBeInvested_Top300Fund, percentToBeInvested_BondOptimiserFund, percentToBeInvested_MidcapFund, percentToBeInvested_PureFund, percentToBeInvested_CorpBondFund) / 12)));
            }
        } else {
            this.fundManagementCharge_X = "" + 0;
        }
    }

    public String getFundManagementCharge_X() {
        return fundManagementCharge_X;
    }

    public double getCharge_fund_ren(double percentToBeInvested_EquityFund, double percentToBeInvested_EquityOptFund, double percentToBeInvested_GrowthFund, double percentToBeInvested_BalancedFund, double percentToBeInvested_BondFund, double percentToBeInvested_MoneyMarketFund, double percentToBeInvested_Top300Fund, double percentToBeInvested_BondOptimiserFund, double percentToBeInvested_MidcapFund, double percentToBeInvested_PureFund, double percentToBeInvested_CorpBondFund) {
        return percentToBeInvested_EquityFund * prop.FMC_EquityFund +
                percentToBeInvested_EquityOptFund * prop.FMC_EquityOptimiserFund +
                percentToBeInvested_GrowthFund * prop.FMC_GrowthFund +
                percentToBeInvested_BalancedFund * prop.FMC_BalancedFund +
                percentToBeInvested_BondFund * prop.FMC_BondFund +
                percentToBeInvested_MoneyMarketFund * prop.FMC_MoneyMarketFund +
                percentToBeInvested_Top300Fund * prop.FMC_Top300Fund +
                percentToBeInvested_BondOptimiserFund * prop.FMC_BondOptimiserFund +
                percentToBeInvested_MidcapFund * prop.FMC_MidcapFund +
                percentToBeInvested_PureFund * prop.FMC_PureFund +
                percentToBeInvested_CorpBondFund * prop.FMC_CorpBondFund;
    }

    public void setServiceTaxOnFMC_Y(double percentToBeInvested_EquityFund, double percentToBeInvested_EquityOptFund, double percentToBeInvested_GrowthFund, double percentToBeInvested_BalancedFund, double percentToBeInvested_BondFund, double percentToBeInvested_MoneyMarketFund, double percentToBeInvested_Top300Fund, double serviceTax, double percentToBeInvested_BondOptimiserFund, double percentToBeInvested_MidcapFund, double percentToBeInvested_PureFund, double percentToBeInvested_CorpBondFund) {
        double a = 0;
        if (prop.fundManagementCharges)
//        {a=(Double.parseDouble(getFundManagementCharge_X()) * (0.0135/getCharge_fund_ren( percentToBeInvested_EquityFund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_BondFund, percentToBeInvested_MoneyMarketFund, percentToBeInvested_Top300Fund,percentToBeInvested_BondOptimiserFund,percentToBeInvested_MidcapFund,percentToBeInvested_PureFund)));}
        {
            a = ((Double.parseDouble(getFundManagementCharge_X()) * getCharge_fund_ren(percentToBeInvested_EquityFund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_BondFund, percentToBeInvested_MoneyMarketFund, percentToBeInvested_Top300Fund, percentToBeInvested_BondOptimiserFund, percentToBeInvested_MidcapFund, percentToBeInvested_PureFund, percentToBeInvested_CorpBondFund) / getCharge_fund_ren(percentToBeInvested_EquityFund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_BondFund, percentToBeInvested_MoneyMarketFund, percentToBeInvested_Top300Fund, percentToBeInvested_BondOptimiserFund, percentToBeInvested_MidcapFund, percentToBeInvested_PureFund, percentToBeInvested_CorpBondFund)));
        } else {
            a = 0;
        }
        this.serviceTaxOnFMC_Y = cfap.roundUp_Level2(cfap.getStringWithout_E(a * serviceTax));
    }

    public String getServiceTaxOnFMC_Y() {
        return serviceTaxOnFMC_Y;
    }

    public void setFundValueAfterFMCAndBeforeGA_Z(int policyTerm) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.fundValueAfterFMCBerforeGA_Z = ""
                    + (Double.parseDouble(getFundBeforeFMC_W())
                    - Double.parseDouble(getFundManagementCharge_X()) - Double
                    .parseDouble(getServiceTaxOnFMC_Y()));
        } else {
            this.fundValueAfterFMCBerforeGA_Z = "" + 0;
        }
    }

    public String getFundValueAfterFMCAndBeforeGA_Z() {
        return fundValueAfterFMCBerforeGA_Z;
    }

    public void setTotalServiceTax_U(double riderCharges, double serviceTax) {

    	/*System.out.println("getServiceTaxOnAllocation_M()"+getServiceTaxOnAllocation_M());
    	System.out.println("getServiceTax_exclOfSTonAllocAndSurr_T()"+getServiceTax_exclOfSTonAllocAndSurr_T());
    	System.out.println("getServiceTaxOnFMC_Y()"+getServiceTaxOnFMC_Y());
    	System.out.println("riderCharges"+riderCharges);
    	System.out.println("serviceTax"+serviceTax);*/
        double temp1 = 0, temp2 = 0;
        temp1 = Double.parseDouble(getServiceTaxOnAllocation_M())
                + Double.parseDouble(getServiceTax_exclOfSTonAllocAndSurr_T())
                + Double.parseDouble(getServiceTaxOnFMC_Y());
        if (prop.riderCharges) {
            temp2 = riderCharges;
        } else {
            temp2 = 0;
        }
        this.totalServiceTax_U = cfap.roundUp_Level2(cfap.getStringWithout_E(temp1 + temp2 * serviceTax));
    }

    public String getTotalServiceTax_U() {
        return totalServiceTax_U;
    }

    public void setGuaranteedAddition_AA(String planType,
                                         double effectivePremium, int PPT) {
        if (getPolicyInForce_G().equals("N")) {
            this.guaranteedAddition_AA = "" + 0;
        } else {
            this.guaranteedAddition_AA = cfap.roundUp_Level2(cfap
                    .getStringWithout_E(effectivePremium
                            * getPercentOfFirstYrPremium(planType, PPT,
                            effectivePremium)));
        }
    }

    public String getGuaranteedAddition_AA() {
        return guaranteedAddition_AA;
    }


    public double getPercentOfFirstYrPremium(String planType, int PPT, double effectivePremium) {
        if (prop.asPercentOfFirstYrPremium) {

//        	Modified by Saurabh Jain on 06/11/2019
            if (getMonth_E().equals("60")) {
                if (planType.equals("Limited")) {
                    if (PPT == 7) {
                        return 0;
                    } else if (PPT == 10) {
                        return 0;
                    } else if (PPT == 12) {
                        return 0;
                    } else if (PPT == 15) {
                        return 0;
                    } else {
                        return 0;
                    }
                } else if (planType.equals("Regular")) {
                    return 0;
                } else {
                    //Single
                    return 0;
                }
//            	Modified by Saurabh Jain on 06/11/2019
                /*{
                    if(PPT==1)
                    {
                        //Single
                        if(effectivePremium >= 24000 && effectivePremium<=99900)
                        {return 0;}
                        else if(effectivePremium >= 100000 && effectivePremium<=499900)
                        {return 0;}
                        else if(effectivePremium >= 500000)
                        {return 0;}
                        else
                        {return 0;}
                    }
                    else
                    {
                        //Regular
                        if(effectivePremium >= 24000 && effectivePremium<=99900)
                        {return 0;}
                        else if(effectivePremium >= 100000 && effectivePremium<=499900)
                        {return 0;}
                        else if(effectivePremium >= 500000)
                        {return 0;}
                        else
                        {return 0;}
                    }
                }*/
            }
//        	Modified by Saurabh Jain on 06/11/2019
            if (getMonth_E().equals("84")) {/*
                if(planType.equals("Limited"))
                {
                    if(PPT==5)
                    {return 0;}
                    else if(PPT==8)
                    {return 0;}
                    else if(PPT==10)
                    {return 0;}
                    else{return 0;}
                    }
                else
                {
                    if(PPT==1)
                    {
                        //Single
                        if(effectivePremium >= 24000 && effectivePremium<=99900)
                        {return 0;}
                        else if(effectivePremium >= 100000 && effectivePremium<=499900)
                        {return 0;}
                        else if(effectivePremium >= 500000)
                        {return 0;}
                        else
                        {return 0;}
                    }
                    else
                    {
                        //Regular
                        if(effectivePremium >= 24000 && effectivePremium<=99900)
                        {return 0;}
                        else if(effectivePremium >= 100000 && effectivePremium<=499900)
                        {return 0;}
                        else if(effectivePremium >= 500000)
                        {return 0;}
                        else
                        {return 0;}
                }
            }
            */
                if (planType.equals("Limited")) {
                    if (PPT == 7) {
                        return 0;
                    } else if (PPT == 10) {
                        return 0;
                    } else if (PPT == 12) {
                        return 0;
                    } else if (PPT == 15) {
                        return 0;
                    } else {
                        return 0;
                    }
                } else if (planType.equals("Regular")) {
                    return 0;
                } else {
                    //Single
                    return 0;
                }
            }
            if (getMonth_E().equals("120")) {/*
                if(planType.equals("Limited"))
                {
                    if(PPT==5)
                    {return 0.05;}
                    else if(PPT==8)
                    {return 0.05;}
                    else if(PPT==10)
                    {return 0.05;}
                    else{return 0;}
                }
                else
                {
                    if(PPT==1)
                    {
                        //Single
                        if(effectivePremium >= 24000 && effectivePremium<=99900)
                        {return 0.05;}
                        else if(effectivePremium >= 100000 && effectivePremium<=499900)
                        {return 0.05;}
                        else if(effectivePremium >= 500000)
                        {return 0.05;}
                        else
                        {return 0;}
                    }
                    else
                    {
                        // Regular
                        if(effectivePremium >= 24000 && effectivePremium<=99900)
                        {return 0.05;}
                        else if(effectivePremium >= 100000 && effectivePremium<=499900)
                        {return 0.05;}
                        else if(effectivePremium >= 500000)
                        {return 0.05;}
                        else
                        {return 0;}
                    }
                }
            */
                if (planType.equals("Limited")) {
                    if (PPT == 7) {
                        return 0.05;
                    } else if (PPT == 10) {
                        return 0.05;
                    } else if (PPT == 12) {
                        return 0.05;
                    } else if (PPT == 15) {
                        return 0.05;
                    } else {
                        return 0;
                    }
                } else if (planType.equals("Regular")) {
                    return 0.05;
                } else {
                    //Single
                    return 0.05;
                }
            }
//        	Modified by Saurabh Jain on 06/11/2019
            if (getMonth_E().equals("180")) {/*
                if(planType.equals("Limited"))
                {
                    if(PPT==5)
                    {return 0.05;}
                    else if(PPT==8)
                    {return 0.1;}
                    else if(PPT==10)
                    {return 0.1;}
                    else{return 0;}
                    }
                else
                {
                    if(PPT==1)
                    {
                        //Single
                        if(effectivePremium >= 24000 && effectivePremium<=99900)
                        {return 0.05;}
                        else if(effectivePremium >= 100000 && effectivePremium<=499900)
                        {return 0.05;}
                        else if(effectivePremium >= 500000)
                        {return 0.05;}
                        else
                        {return 0;}
                }
                    else
                    {
                        //Regular
                        if(effectivePremium >= 24000 && effectivePremium<=99900)
                        {return 0.15;}
                        else if(effectivePremium >= 100000 && effectivePremium<=499900)
                        {return 0.15;}
                        else if(effectivePremium >= 500000)
                        {return 0.15;}
                        else
                        {return 0;}
            }
                }
            */
                if (planType.equals("Limited")) {
                    if (PPT == 7) {
                        return 0.1;
                    } else if (PPT == 10) {
                        return 0.1;
                    } else if (PPT == 12) {
                        return 0.1;
                    } else if (PPT == 15) {
                        return 0.1;
                    } else {
                        return 0;
                    }
                } else if (planType.equals("Regular")) {
                    return 0.15;
                } else {
                    // Single
                    return 0.05;
                }
            }
//        	Modified by Saurabh Jain on 06/11/2019
            if (getMonth_E().equals("240")) {/*
                if(planType.equals("Limited"))
                {
                    if(PPT==5)
                    {return 0.1;}
                    else if(PPT==8)
                    {return 0.1;}
                    else if(PPT==10)
                    {return 0.15;}
                    else{return 0;}
                }
                else
                {
                    if(PPT==1)
                    {
                        //Single
                        if(effectivePremium >= 24000 && effectivePremium<=99900)
                        {return 0.05;}
                        else if(effectivePremium >= 100000 && effectivePremium<=499900)
                        {return 0.05;}
                        else if(effectivePremium >= 500000)
                        {return 0.05;}
                        else
                        {return 0;}
                    }
                    else
                    {
                        // Regular
                        if(effectivePremium >= 24000 && effectivePremium<=99900)
                        {return 0.25;}
                        else if(effectivePremium >= 100000 && effectivePremium<=499900)
                        {return 0.25;}
                        else if(effectivePremium >= 500000)
                        {return 0.25;}
                        else
                        {return 0;}
                    }
                }
            */

                if (planType.equals("Limited")) {
                    if (PPT == 7) {
                        return 0.1;
                    } else if (PPT == 10) {
                        return 0.15;
                    } else if (PPT == 12) {
                        return 0.15;
                    } else if (PPT == 15) {
                        return 0.2;
                    } else {
                        return 0;
                    }
                } else if (planType.equals("Regular")) {
                    return 0.25;
                } else {
                    // Single
                    return 0.05;
                }
            }
            if (getMonth_E().equals("300")) {/*
                if(planType.equals("Limited"))
                {
                    if(PPT==5)
                    {return 0.1;}
                    else if(PPT==8)
                    {return 0.1;}
                    else if(PPT==10)
                    {return 0.2;}
                    else{return 0;}
                }
                else
                {
                    if(PPT==1)
                    {
                        //Single
                        if(effectivePremium >= 24000 && effectivePremium<=99900)
                        {return 0.07;}
                        else if(effectivePremium >= 100000 && effectivePremium<=499900)
                        {return 0.07;}
                        else if(effectivePremium >= 500000)
                        {return 0.07;}
                        else
                        {return 0;}
                    }
                    else
                    {
                        // Regular
                        if(effectivePremium >= 24000 && effectivePremium<=99900)
                        {return 0.35;}
                        else if(effectivePremium >= 100000 && effectivePremium<=499900)
                        {return 0.35;}
                        else if(effectivePremium >= 500000)
                        {return 0.35;}
                        else
                        {return 0;}
                    }
                }
            */
                if (planType.equals("Limited")) {
                    if (PPT == 7) {
                        return 0.1;
                    } else if (PPT == 10) {
                        return 0.2;
                    } else if (PPT == 12) {
                        return 0.25;
                    } else if (PPT == 15) {
                        return 0.3;
                    } else {
                        return 0;
                    }
                } else if (planType.equals("Regular")) {
                    return 0.35;
                } else {
                    // Single
                    return 0.07;
                }
            }
            if (getMonth_E().equals("360")) {/*
                if(planType.equals("Limited"))
                {
                    if(PPT==5)
                    {return 0.1;}
                    else if(PPT==8)
                    {return 0.15;}
                    else if(PPT==10)
                    {return 0.25;}
                    else{return 0;}
                }
                else
                {
                    if(PPT==1)
                    {
                        //Single
                        if(effectivePremium >= 24000 && effectivePremium<=99900)
                        {return 0.08;}
                        else if(effectivePremium >= 100000 && effectivePremium<=499900)
                        {return 0.08;}
                        else if(effectivePremium >= 500000)
                        {return 0.08;}
                        else
                        {return 0;}
                    }
                    else
                    {
                        // Regular
                        if(effectivePremium >= 24000 && effectivePremium<=99900)
                        {return 0.45;}
                        else if(effectivePremium >= 100000 && effectivePremium<=499900)
                        {return 0.45;}
                        else if(effectivePremium >= 500000)
                        {return 0.45;}
                        else
                        {return 0;}
                    }
                }
            */

                if (planType.equals("Limited")) {
                    if (PPT == 7) {
                        return 0.15;
                    } else if (PPT == 10) {
                        return 0.25;
                    } else if (PPT == 12) {
                        return 0.30;
                    } else if (PPT == 15) {
                        return 0.35;
                    } else {
                        return 0;
                    }
                } else if (planType.equals("Regular")) {
                    return 0.45;
                } else {
                    // Single
                    return 0.08;
                }
            }
//        	Modified by Saurabh Jain on 06/11/2019
            if (getMonth_E().equals("1188")) {/*
                if(planType.equals("Limited"))
                {
                    if(PPT==5)
                    {return 0;}
                    else if(PPT==8)
                    {return 0;}
                    else if(PPT==10)
                    {return 0;}
                    else{return 0;}
                }
                else
                {
                    if(PPT==1)
                    {
                        //Single
                        if(effectivePremium >= 24000 && effectivePremium<=99900)
                        {return 0;}
                        else if(effectivePremium >= 100000 && effectivePremium<=499900)
                        {return 0;}
                        else if(effectivePremium >= 500000)
                        {return 0;}
                        else
                        {return 0;}
                    }
                    else
                    {
                        // Regular
                        if(effectivePremium >= 24000 && effectivePremium<=99900)
                        {return 0;}
                        else if(effectivePremium >= 100000 && effectivePremium<=499900)
                        {return 0;}
                        else if(effectivePremium >= 500000)
                        {return 0;}
                        else
                        {return 0;}
                    }
                }
            */
                if (planType.equals("Limited")) {
                    if (PPT == 7) {
                        return 0;
                    } else if (PPT == 10) {
                        return 0;
                    } else if (PPT == 12) {
                        return 0;
                    } else if (PPT == 15) {
                        return 0;
                    } else {
                        return 0;
                    }
                } else if (planType.equals("Regular")) {
                    return 0;
                } else {
                    //Single
                    return 0;
                }
            } else {
                return 0;
            }
        }
//    	Modified by Saurabh Jain on 06/11/2019
        else {
            return 0;
        }
    }

    public void setFundValueAtEnd_AB() {
        this.fundValueAtEnd_AB = ""
                + (Double.parseDouble(getGuaranteedAddition_AA()) + Double
                .parseDouble(getFundValueAfterFMCAndBeforeGA_Z()));
    }

    public String getFundValueAtEnd_AB() {
        return fundValueAtEnd_AB;
    }

    public String[] calSurrRateArrRP(double effectivePremium) {
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

    public String[] calSurrRateArrSP() {
        return new String[]{"0.01", "0.005", "0.003", "0.001", "0", "0", "0",
                "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                "0", "0", "0", "0", "0", "0", "0"};
    }

    public String[] calCapArr(double effectivePremium) {
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
        {this.surrenderCap_AV =""+(Double.parseDouble(calCapArr(effectivePremium)[Math.min((Integer.parseInt(getYear_F())-1),11)]));}
        else
        {this.surrenderCap_AV =""+0;}*/

//    	Added by Saurabh Jain on 18/10/2019
        double a = 0;
        if (planType.equals("Single")) {
            if (effectivePremium > 25000) {
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
            } else {
                a = 0;
            }
        } else {
            if (effectivePremium > 25000) {

                if (effectivePremium > 50000) {

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
                a = 0;
            }
        }
        this.surrenderCap_AV = "" + a;
//    	Added by Saurabh Jain on 18/10/2019

    	/*if(getPolicyInForce_G().equals("Y"))
            //double a=Math.min(Integer.parseInt(getYear_F())-1, 11);
            {this.surrenderCap_AV =""+(Double.parseDouble(calCapArr(effectivePremium)[Math.min((Integer.parseInt(getYear_F())-1),11)]));}
            else
            {this.surrenderCap_AV =""+0;}*/

    }

    public String getSurrenderCap_AV() {
        return surrenderCap_AV;
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
            } else {
                surrenderCharge = 0;
            }
        } else {
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
                surrenderCharge = 0;
            }
        }

    	 /*double surrenderCharge=0;
         if(PPT==1)
         {
             //surrenderCharge=1;
             surrenderCharge=Double.parseDouble(calSurrRateArrSP()[Math.min((Integer.parseInt(getYear_F())-1), 11)]);
        }
         else
         {surrenderCharge=Double.parseDouble(calSurrRateArrRP(effectivePremium)[Math.min((Integer.parseInt(getYear_F())-1),11)]);}*/

        return surrenderCharge;
    }

    public void setSurrenderCharges_AC(double effectivePremium, int PPT, String planType) {
        double a = Math.min(Double.parseDouble(getFundValueAtEnd_AB()), (double) effectivePremium);
        // System.out.println("a "+a);
        double b = getSurrenderCharge(PPT, effectivePremium, planType);
      /*System.out.println("b "+b);
      System.out.println("a*b "+(a*b));
      System.out.println("getSurrenderCap_AV()"+getSurrenderCap_AV());*/
        this.surrenderCharges_AC = cfap.roundUp_Level2(cfap
                .getStringWithout_E(Math.min((a * b),
                        Double.parseDouble(getSurrenderCap_AV()))));
    }

    public String getSurrenderCharges_AC() {
        return surrenderCharges_AC;
    }


    public void setServiceTaxOnSurrenderCharges_AD(double serviceTax) {
        if (prop.surrenderCharges) {
            this.serviceTaxOnSurrenderCharges_AD = cfap.roundUp_Level2(cfap.getStringWithout_E(Double.parseDouble(getSurrenderCharges_AC()) * serviceTax));
        } else {
            this.serviceTaxOnSurrenderCharges_AD = "" + 0;
        }
    }

    public String getServiceTaxOnSurrenderCharges_AD() {
        return serviceTaxOnSurrenderCharges_AD;
    }

    public void setSurrenderValue_AE() {
    	/*System.out.println("getFundValueAtEnd_AB()"+getFundValueAtEnd_AB());
    	System.out.println("getSurrenderCharges_AC()"+getSurrenderCharges_AC());
    	System.out.println("getServiceTaxOnSurrenderCharges_AD()"+getServiceTaxOnSurrenderCharges_AD());*/
        this.surrenderValue_AE = ""
                + (Double.parseDouble(getFundValueAtEnd_AB())
                - Double.parseDouble(getSurrenderCharges_AC()) - Double
                .parseDouble(getServiceTaxOnSurrenderCharges_AD()));
    }

    public String getSurrenderValue_AE() {
        return surrenderValue_AE;
    }

    public void setDeathBenefit_AF(int policyTerm, double sumAssured) {
        if (Integer.parseInt(getYear_F()) > policyTerm) {
            this.deathBenefit_AF = "" + 0;
        } else {
            this.deathBenefit_AF = ""
                    + (Math.max(
                    Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()),
                    Math.max(sumAssured,
                            Double.parseDouble(getFundValueAtEnd_AB()))));
        }
    }

    public String getDeathBenefit_AF() {
        return deathBenefit_AF;
    }

    public void setMortalityCharges_AG(double _fundValueAtEnd_AQ,
                                       int policyTerm, String[] forBIArray, double sumAssured,
                                       boolean mortalityCharges) {
        if (!(getPolicyInForce_G().equals("Y"))
                || Integer.parseInt(getYear_F()) > policyTerm) {
            this.mortalityCharges_AG = "" + 0;
        } else {
            double arrOutput = Double.parseDouble(forBIArray[Integer
                    .parseInt(getAge_H())]);
            // System.out.println("arroutput "+arrOutput);
            // double div=arrOutput/1000;
//            double a=arrOutput/12;
            // System.out.println("a "+a);


            DecimalFormat df = new DecimalFormat("#.#######");

            String roundTo7 = df.format(arrOutput);
            String test = cfap.getRoundOffLevel5(roundTo7);

//            System.out.println("arroutput RY "+arrOutput);
//            System.out.println("round to 5 RY"+test);

            double a = Double.parseDouble(test) / 12;

            double max1 = Math
                    .max(sumAssured,
                            Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()));
            // System.out.println("max1 "+max1);
            double b = (max1 - (Double
                    .parseDouble(getAmountAvailableForInvestment_N()) + _fundValueAtEnd_AQ));
            // System.out.println("b "+b);

            int c = 0;
            if (mortalityCharges) {
                c = 1;
            } else {
                c = 0;
            }
//            this.mortalityCharges_AG= cfap.roundUp_Level2(cfap.getStringWithout_E(((a*Math.max(b, 0))*c)));
            this.mortalityCharges_AG = cfap.getRoundOffLevel2New(cfap.getStringWithout_E(((a * Math.max(b, 0)) * c)));
        }
    }

    public String getMortalityCharges_AG() {
        return mortalityCharges_AG;
    }

    public void setTotalCharges_AH(int policyTerm, double riderCharges) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.totalCharges_AH = ""
                    + (Double.parseDouble(getPolicyAdministrationCharge_Q())
                    + Double.parseDouble(getMortalityCharges_AG())
                    + Double.parseDouble(getSumAssuredRelatedCharges_O()) + riderCharges);
        } else {
            this.totalCharges_AH = "" + 0;
        }
    }

    public String getTotalCharges_AH() {
        return totalCharges_AH;
    }

    public void setServiceTax_exclOfSTonAllocAndSurr_AI(double serviceTax,
                                                        boolean mortalityAndRiderCharges, boolean administrationCharges) {
        double a = 0, b = 0;
        if (mortalityAndRiderCharges) {
            a = Double.parseDouble(getMortalityCharges_AG());
        } else {
            a = 0;
        }
        if (administrationCharges) {
            b = Double.parseDouble(getPolicyAdministrationCharge_Q());
        } else {
            b = 0;
        }
        this.serviceTaxExclOfSTOnAllocAndSurr_AI = cfap.roundUp_Level2(cfap
                .getStringWithout_E((a + b) * serviceTax));
    }

    public String getServiceTax_exclOfSTonAllocAndSurr_AI() {
        return serviceTaxExclOfSTOnAllocAndSurr_AI;
    }

    public void setAdditionToFundIfAny_AK(double _fundValueAtEnd_AQ,
                                          int policyTerm, double riderCharges) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            double temp1 = 0, temp2 = 0;
            temp1 = (_fundValueAtEnd_AQ
                    + Double.parseDouble(getAmountAvailableForInvestment_N())
                    - Double.parseDouble(getTotalCharges_AH())
                    - Double.parseDouble(getServiceTax_exclOfSTonAllocAndSurr_AI()) + riderCharges);
            double a = (double) 1 / 12;
            temp2 = (cfap.pow((1 + prop.int2), a)) - 1;
            this.additionToFundIfAny_AK = cfap.roundUp_Level2(cfap
                    .getStringWithout_E(temp1 * temp2));
        } else {
            this.additionToFundIfAny_AK = "" + 0;
        }
    }

    public String getAdditionToFundIfAny_AK() {
        return additionToFundIfAny_AK;
    }

    public void setFundBeforeFMC_AL(double _fundValueAtEnd_AQ, int policyTerm,
                                    double riderCharges) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.fundBeforeFMC_AL = cfap
                    .getStringWithout_E(_fundValueAtEnd_AQ
                            + Double.parseDouble(getAmountAvailableForInvestment_N())
                            - Double.parseDouble(getTotalCharges_AH())
                            + Double.parseDouble(getAdditionToFundIfAny_AK())
                            - Double.parseDouble(getServiceTax_exclOfSTonAllocAndSurr_AI())
                            + riderCharges);
        } else {
            this.fundBeforeFMC_AL = "" + 0;
        }
    }

    public String getFundBeforeFMC_AL() {
        return fundBeforeFMC_AL;
    }

    public void setFundManagementCharge_AM(int policyTerm, double percentToBeInvested_EquityFund, double percentToBeInvested_EquityOptFund, double percentToBeInvested_GrowthFund, double percentToBeInvested_BalancedFund, double percentToBeInvested_BondFund, double percentToBeInvested_MoneyMarketFund, double percentToBeInvested_Top300Fund, double percentToBeInvested_BondOptimiserFund, double percentToBeInvested_MidcapFund, double percentToBeInvested_PureFund, double percentToBeInvested_CorpBondFund) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            if (Integer.parseInt(getMonth_E()) == 1) {
                this.fundManagementCharge_AM = cfap.roundUp_Level2(cfap.getStringWithout_E(Double.parseDouble(getFundBeforeFMC_AL()) * (prop.charge_Fund + (getCharge_fund_ren(percentToBeInvested_EquityFund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_BondFund, percentToBeInvested_MoneyMarketFund, percentToBeInvested_Top300Fund, percentToBeInvested_BondOptimiserFund, percentToBeInvested_MidcapFund, percentToBeInvested_PureFund, percentToBeInvested_CorpBondFund) / 12))));
            } else {
                this.fundManagementCharge_AM = cfap.roundUp_Level2(cfap.getStringWithout_E(Double.parseDouble(getFundBeforeFMC_AL()) * (getCharge_fund_ren(percentToBeInvested_EquityFund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_BondFund, percentToBeInvested_MoneyMarketFund, percentToBeInvested_Top300Fund, percentToBeInvested_BondOptimiserFund, percentToBeInvested_MidcapFund, percentToBeInvested_PureFund, percentToBeInvested_CorpBondFund) / 12)));
            }
        } else {
            this.fundManagementCharge_AM = "" + 0;
        }
    }

    public String getFundManagementCharge_AM() {
        return fundManagementCharge_AM;
    }

    public void setServiceTaxOnFMC_AN(double percentToBeInvested_EquityFund, double percentToBeInvested_EquityOptFund, double percentToBeInvested_GrowthFund, double percentToBeInvested_BalancedFund, double percentToBeInvested_BondFund, double percentToBeInvested_MoneyMarketFund, double percentToBeInvested_Top300Fund, double serviceTax, double percentToBeInvested_BondOptimiserFund, double percentToBeInvested_MidcapFund, double percentToBeInvested_PureFund, double percentToBeInvested_CorpBondFund) {
        double a = 0;
        if (prop.fundManagementCharges)
//        {a=(Double.parseDouble(getFundManagementCharge_AM()) * (0.0135/getCharge_fund_ren( percentToBeInvested_EquityFund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_BondFund, percentToBeInvested_MoneyMarketFund, percentToBeInvested_Top300Fund,percentToBeInvested_BondOptimiserFund,percentToBeInvested_MidcapFund,percentToBeInvested_PureFund)));}
        {
            a = ((Double.parseDouble(getFundManagementCharge_AM()) * getCharge_fund_ren(percentToBeInvested_EquityFund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_BondFund, percentToBeInvested_MoneyMarketFund, percentToBeInvested_Top300Fund, percentToBeInvested_BondOptimiserFund, percentToBeInvested_MidcapFund, percentToBeInvested_PureFund, percentToBeInvested_CorpBondFund) / getCharge_fund_ren(percentToBeInvested_EquityFund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_BondFund, percentToBeInvested_MoneyMarketFund, percentToBeInvested_Top300Fund, percentToBeInvested_BondOptimiserFund, percentToBeInvested_MidcapFund, percentToBeInvested_PureFund, percentToBeInvested_CorpBondFund)));
        } else {
            a = 0;
        }
        this.serviceTaxOnFMC_AN = cfap.roundUp_Level2(cfap.getStringWithout_E(a * serviceTax));
    }

    public String getServiceTaxOnFMC_AN() {
        return serviceTaxOnFMC_AN;
    }

    public void setFundValueAfterFMCAndBeforeGA_AO(int policyTerm) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.fundValueAfterFMCBerforeGA_AO = ""
                    + (Double.parseDouble(getFundBeforeFMC_AL())
                    - Double.parseDouble(getFundManagementCharge_AM()) - Double
                    .parseDouble(getServiceTaxOnFMC_AN()));
        } else {
            this.fundValueAfterFMCBerforeGA_AO = "" + 0;
        }
    }

    public String getFundValueAfterFMCAndBeforeGA_AO() {
        return fundValueAfterFMCBerforeGA_AO;
    }

    public void setTotalServiceTax_AJ(double riderCharges, double serviceTax) {
        double temp1 = 0, temp2 = 0;
        temp1 = Double.parseDouble(getServiceTaxOnAllocation_M())
                + Double.parseDouble(getServiceTax_exclOfSTonAllocAndSurr_AI())
                + Double.parseDouble(getServiceTaxOnFMC_AN());
        if (prop.riderCharges) {
            temp2 = riderCharges;
        } else {
            temp2 = 0;
        }
        this.totalServiceTax_AJ = cfap.roundUp_Level2(cfap.getStringWithout_E(temp1 + temp2 * serviceTax));
    }

    public String getTotalServiceTax_AJ() {
        return totalServiceTax_AJ;
    }

    public void setGuaranteedAddition_AP(String planType,
                                         double effectivePremium, int PPT) {
        if (getPolicyInForce_G().equals("N")) {
            this.guaranteedAddition_AP = "" + 0;
        } else {
            this.guaranteedAddition_AP = cfap.roundUp_Level2(cfap
                    .getStringWithout_E(effectivePremium
                            * getPercentOfFirstYrPremium(planType, PPT,
                            effectivePremium)));
        }
    }

    public String getGuaranteedAddition_AP() {
        return guaranteedAddition_AP;
    }

    public void setFundValueAtEnd_AQ() {
        this.fundValueAtEnd_AQ = ""
                + (Double.parseDouble(getGuaranteedAddition_AP()) + Double
                .parseDouble(getFundValueAfterFMCAndBeforeGA_AO()));
    }

    public String getFundValueAtEnd_AQ() {
        return fundValueAtEnd_AQ;
    }

    public void setSurrenderCharges_AR(double effectivePremium, int PPT, String planType) {
        double a = Math.min(Double.parseDouble(getFundValueAtEnd_AQ()), (double) effectivePremium);
        // System.out.println("a "+a);
        double b = getSurrenderCharge(PPT, effectivePremium, planType);
        // System.out.println("b "+b);
        // System.out.println("a*b "+(a*b));
        this.surrenderCharges_AR = cfap.roundUp_Level2(cfap
                .getStringWithout_E(Math.min((a * b),
                        Double.parseDouble(getSurrenderCap_AV()))));
    }

    public String getSurrenderCharges_AR() {
        return surrenderCharges_AR;
    }

    public void setServiceTaxOnSurrenderCharges_AS(double serviceTax) {
        if (prop.surrenderCharges) {
            this.serviceTaxOnSurrenderCharges_AS = cfap.roundUp_Level2(cfap.getStringWithout_E(Double.parseDouble(getSurrenderCharges_AR()) * serviceTax));
        } else {
            this.serviceTaxOnSurrenderCharges_AS = "" + 0;
        }
    }

    public String getServiceTaxOnSurrenderCharges_AS() {
        return serviceTaxOnSurrenderCharges_AS;
    }

    public void setSurrenderValue_AT() {
        this.surrenderValue_AT = ""
                + (Double.parseDouble(getFundValueAtEnd_AQ())
                - Double.parseDouble(getSurrenderCharges_AR()) - Double
                .parseDouble(getServiceTaxOnSurrenderCharges_AS()));
    }

    public String getSurrenderValue_AT() {
        return surrenderValue_AT;
    }

    public void setDeathBenefit_AU(int policyTerm, double sumAssured) {
        if (Integer.parseInt(getYear_F()) > policyTerm) {
            this.deathBenefit_AU = "" + 0;
        } else {
            this.deathBenefit_AU = ""
                    + (Math.max(
                    Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()),
                    Math.max(sumAssured,
                            Double.parseDouble(getFundValueAtEnd_AQ()))));
        }
    }

    public String getDeathBenefit_AU() {
        return deathBenefit_AU;
    }

    public void setServiceTaxOnFMCReductionYield_Y(double serviceTax) {
        double a = 0;
        if (prop.fundManagementChargesYield) {
            a = (Double.parseDouble(getFundManagementCharge_X()));
        } else {
            a = 0;
        }
        this.serviceTaxOnFMCReductionYield_Y = cfap.roundUp_Level2(cfap.getStringWithout_E(a * serviceTax));
    }

    public String getServiceTaxOnFMCReductionYield_Y() {
        return serviceTaxOnFMCReductionYield_Y;
    }

    public void setFundValueAfterFMCAndBeforeGAReductionYield_Z(int policyTerm) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.fundValueAfterFMCBerforeGAReductionYield_Z = ""
                    + (Double.parseDouble(getFundBeforeFMC_W())
                    - Double.parseDouble(getFundManagementCharge_X()) - Double
                    .parseDouble(getServiceTaxOnFMCReductionYield_Y()));
        } else {
            this.fundValueAfterFMCBerforeGAReductionYield_Z = "" + 0;
        }
    }

    public String getFundValueAfterFMCAndBeforeGAReductionYield_Z() {
        return fundValueAfterFMCBerforeGAReductionYield_Z;
    }

    public void setTotalServiceTaxReductionYield_U(double riderCharges, double serviceTax) {
        double temp1 = 0, temp2 = 0;
        temp1 = Double.parseDouble(getServiceTaxOnAllocation_M())
                + Double.parseDouble(getServiceTax_exclOfSTonAllocAndSurr_T())
                + Double.parseDouble(getServiceTaxOnFMCReductionYield_Y());
        if (prop.riderChargesYield) {
            temp2 = riderCharges;
        } else {
            temp2 = 0;
        }
        this.totalServiceTaxReductionYield_U = cfap.roundUp_Level2(cfap.getStringWithout_E(temp1 + temp2 * serviceTax));
    }

    public String getTotalServiceTaxReductionYield_U() {
        return totalServiceTaxReductionYield_U;
    }

    public void setFundValueAtEndReductionYield_AB() {
        this.fundValueAtEndReductionYield_AB = ""
                + (Double.parseDouble(getGuaranteedAddition_AA()) + Double
                .parseDouble(getFundValueAfterFMCAndBeforeGAReductionYield_Z()));
    }

    public String getFundValueAtEndReductionYield_AB() {
        return fundValueAtEndReductionYield_AB;
    }

    public void setSurrenderChargesReductionYield_AC(double effectivePremium, int PPT, String planType) {
        double a = Math.min(Double.parseDouble(getFundValueAtEndReductionYield_AB()), (double) effectivePremium);
        // System.out.println("a "+a);
        double b = getSurrenderCharge(PPT, effectivePremium, planType);
        // System.out.println("b "+b);
        // System.out.println("a*b "+(a*b));
        this.surrenderChargesReductionYield_AC = cfap.roundUp_Level2(cfap
                .getStringWithout_E(Math.min((a * b),
                        Double.parseDouble(getSurrenderCap_AV()))));
    }

    public String getSurrenderChargesReductionYield_AC() {
        return surrenderChargesReductionYield_AC;
    }

    public void setServiceTaxOnSurrenderChargesReductionYield_AD(double serviceTax) {
        if (prop.surrenderCharges) {
            this.serviceTaxOnSurrenderChargesReductionYield_AD = cfap.roundUp_Level2(cfap.getStringWithout_E(Double.parseDouble(getSurrenderChargesReductionYield_AC()) * serviceTax));
        } else {
            this.serviceTaxOnSurrenderChargesReductionYield_AD = "" + 0;
        }
    }

    public String getServiceTaxOnSurrenderChargesReductionYield_AD() {
        return serviceTaxOnSurrenderChargesReductionYield_AD;
    }

    public void setSurrenderValueReductionYield_AE() {
        this.surrenderValueReductionYield_AE = ""
                + (Double.parseDouble(getFundValueAtEndReductionYield_AB())
                - Double.parseDouble(getSurrenderChargesReductionYield_AC()) - Double
                .parseDouble(getServiceTaxOnSurrenderChargesReductionYield_AD()));
    }

    public String getSurrenderValueReductionYield_AE() {
        return surrenderValueReductionYield_AE;
    }

    public void setDeathBenefitReductionYield_AF(int policyTerm,
                                                 double sumAssured) {
        if (Integer.parseInt(getYear_F()) > policyTerm) {
            this.deathBenefitReductionYield_AF = "" + 0;
        } else {
            this.deathBenefitReductionYield_AF = ""
                    + (Math.max(
                    Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()),
                    Math.max(
                            sumAssured,
                            Double.parseDouble(getFundValueAtEndReductionYield_AB()))));
        }
    }

    public String getDeathBenefitReductionYield_AF() {
        return deathBenefitReductionYield_AF;
    }

    public void setServiceTaxOnFMCReductionYield_AN(double serviceTax) {
        double a = 0;
        if (prop.fundManagementChargesYield) {
            a = (Double.parseDouble(getFundManagementCharge_AM()));
        } else {
            a = 0;
        }
        this.serviceTaxOnFMCReductionYield_AN = cfap.roundUp_Level2(cfap.getStringWithout_E(a * serviceTax));
    }

    public String getServiceTaxOnFMCReductionYield_AN() {
        return serviceTaxOnFMCReductionYield_AN;
    }

    public void setFundValueAfterFMCAndBeforeGAReductionYield_AO(int policyTerm) {
        if (Integer.parseInt(getYear_F()) <= policyTerm) {
            this.fundValueAfterFMCBerforeGAReductionYield_AO = ""
                    + (Double.parseDouble(getFundBeforeFMC_AL())
                    - Double.parseDouble(getFundManagementCharge_AM()) - Double
                    .parseDouble(getServiceTaxOnFMCReductionYield_AN()));
        } else {
            this.fundValueAfterFMCBerforeGAReductionYield_AO = "" + 0;
        }
    }

    public String getFundValueAfterFMCAndBeforeGAReductionYield_AO() {
        return fundValueAfterFMCBerforeGAReductionYield_AO;
    }

    public void setTotalServiceTaxReductionYield_AJ(double riderCharges, double serviceTax) {
        double temp1 = 0, temp2 = 0;
        temp1 = Double.parseDouble(getServiceTaxOnAllocation_M())
                + Double.parseDouble(getServiceTax_exclOfSTonAllocAndSurr_AI())
                + Double.parseDouble(getServiceTaxOnFMCReductionYield_AN());
        if (prop.riderCharges) {
            temp2 = riderCharges;
        } else {
            temp2 = 0;
        }
        this.totalServiceTaxReductionYield_AJ = cfap.roundUp_Level2(cfap.getStringWithout_E(temp1 + temp2 * serviceTax));
    }

    public String getTotalServiceTaxReductionYield_AJ() {
        return totalServiceTaxReductionYield_AJ;
    }

    public void setFundValueAtEndReductionYield_AQ() {
        this.fundValueAtEndReductionYield_AQ = ""
                + (Double.parseDouble(getGuaranteedAddition_AP()) + Double
                .parseDouble(getFundValueAfterFMCAndBeforeGAReductionYield_AO()));
    }

    public String getFundValueAtEndReductionYield_AQ() {
        return fundValueAtEndReductionYield_AQ;
    }

    public void setSurrenderChargesReductionYield_AR(double effectivePremium, int PPT, String planType) {
        double a = Math.min(Double.parseDouble(getFundValueAtEndReductionYield_AQ()), (double) effectivePremium);
        // System.out.println("a "+a);
        double b = getSurrenderCharge(PPT, effectivePremium, planType);
        // System.out.println("b "+b);
        // System.out.println("a*b "+(a*b));
        this.surrenderChargesReductionYield_AR = cfap.roundUp_Level2(cfap
                .getStringWithout_E(Math.min((a * b),
                        Double.parseDouble(getSurrenderCap_AV()))));
    }

    public String getSurrenderChargesReductionYield_AR() {
        return surrenderChargesReductionYield_AR;
    }

    public void setServiceTaxOnSurrenderChargesReductionYield_AS(double serviceTax) {
        if (prop.surrenderCharges) {
            this.serviceTaxOnSurrenderChargesReductionYield_AS = cfap.roundUp_Level2(cfap.getStringWithout_E(Double.parseDouble(getSurrenderChargesReductionYield_AR()) * serviceTax));
        } else {
            this.serviceTaxOnSurrenderChargesReductionYield_AS = "" + 0;
        }
    }

    public String getServiceTaxOnSurrenderChargesReductionYield_AS() {
        return serviceTaxOnSurrenderChargesReductionYield_AS;
    }

    public void setSurrenderValueReductionYield_AT() {
        this.surrenderValueReductionYield_AT = ""
                + (Double.parseDouble(getFundValueAtEndReductionYield_AQ())
                - Double.parseDouble(getSurrenderChargesReductionYield_AR()) - Double
                .parseDouble(getServiceTaxOnSurrenderChargesReductionYield_AS()));
    }

    public String getSurrenderValueReductionYield_AT() {
        return surrenderValueReductionYield_AT;
    }

    public void setDeathBenefitReductionYield_AU(int policyTerm,
                                                 double sumAssured) {
        if (Integer.parseInt(getYear_F()) > policyTerm) {
            this.deathBenefitReductionYield_AU = "" + 0;
        } else {
            this.deathBenefitReductionYield_AU = ""
                    + (Math.max(
                    Double.parseDouble(getOneHundredPercentOfCummulativePremium_AW()),
                    Math.max(
                            sumAssured,
                            Double.parseDouble(getFundValueAtEndReductionYield_AQ()))));
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

    public void setReductionYield_BI(int noOfYearsElapsedSinceInception,
                                     double _fundValueAtEnd_AQ) {
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
        // System.out.println("a_BU "+a);
        // System.out.println("b_BU "+b);
        this.reductionYield_BI = "" + (a + b);
    }

    public String getReductionYield_BI() {
        return reductionYield_BI;
    }

    public void setReductionYield_BD(int policyTerm, double _fundValueAtEnd_AQ) {
        double a, b;
        if ((Integer.parseInt(getMonth_BB())) == (policyTerm * 12)) {
            // System.out.println("inside if");
            // System.out.println("getFundValueAtEndReductionYield_AS "+getFundValueAtEndReductionYield_AS());
            a = _fundValueAtEnd_AQ;
        } else {
            a = 0;
        }

        if ((Integer.parseInt(getMonth_BB())) < (policyTerm * 12)) {
            b = -(Double.parseDouble(getPremium_I()));
        } else {
            b = 0;
        }
        // System.out.println("a_BQ" +a);
        // this.reductionYield_BQ=""+(Double.parseDouble(getPremium_I()) + a);
        this.reductionYield_BD = "" + (b + a);
    }

    public String getReductionYield_BD() {
        return reductionYield_BD;
    }

    public void setIRRAnnual_BD(double ans) {
        // System.out.println("aaaaaaa "+((cfap.pow((1+ans),12)-1)*100));
        this.irrAnnual_BD = "" + ((cfap.pow((1 + ans), 12) - 1));
    }

    public String getIRRAnnual_BD() {
        return irrAnnual_BD;
    }

    public void setIRRAnnual_BI(double ans) {
        // System.out.println("aaaaaaa "+((comm.pow((1+ans),12)-1)*100));
        this.irrAnnual_BI = "" + ((cfap.pow((1 + ans), 12) - 1));
    }

    public String getIRRAnnual_BI() {
        return irrAnnual_BI;
    }

    public void setReductionInYieldMaturityAt(double int2) {
        this.reductionInYieldMaturityAt = ""
                + ((int2 - Double.parseDouble(getIRRAnnual_BD())) * 100);
    }

    public String getReductionInYieldMaturityAt() {
        return reductionInYieldMaturityAt;
    }

    /*** Added by Akshaya on 18-Feb-2014 start ***/
    public String getnetYieldAt8Percent() {
        return netYieldAt8Percent;
    }

    public void setnetYieldAt8Percent() {
        this.netYieldAt8Percent = "" + Double.parseDouble(getIRRAnnual_BD())
                * 100;
    }

    /*** Added by Akshaya on 18-Feb-2014 end ***/

    public void setReductionInYieldNumberOfYearsElapsedSinceInception(
            double int2) {
        this.reductionInYieldNumberOfYearsElapsedSinceInception = ""
                + ((int2 - Double.parseDouble(getIRRAnnual_BI())) * 100);
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
        // System.out.println("inside irr ");
        // System.out.println("values "+values);
        while (i < maxIterationCount) {

            // the value of the function (NPV) and its derivate can be calculated in the same loop
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

    /************ Added by Akshaya start ****************************/

    public double getCommission_BL(double annualisesPrem, double topupPrem,
                                   SmartWealthBuilderBean smartWealthBuilderBean) {
        double topup = 0;
        return getCommistionRate(smartWealthBuilderBean) * annualisesPrem
                + (topupPrem * topup);

    }


    public double getCommistionRate(SmartWealthBuilderBean smartWealthBuilderBean) {
        int findYear;

        if (Integer.parseInt(year_F) > 10) {
            findYear = 11;
        } else
            findYear = Integer.parseInt(year_F);

        if (smartWealthBuilderBean.getIsForStaffOrNot()) {
            return 0;
        } else if (smartWealthBuilderBean.getPlanType().equals("Limited")) {
            double[] lpptArr = {0.1, 0.02, 0.02, 0.02, 0.02, 0.015, 0.015,
                    0.01, 0.01, 0.01, 0};
            return lpptArr[findYear - 1];
        } else if (smartWealthBuilderBean.getPlanType().equals("Single")) {
            double[] pptArr = {0.02, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            return pptArr[findYear - 1];
        } else {
            double[] commArr = {0.1, 0.02, 0.02, 0.02, 0.02, 0.015, 0.015,
                    0.01, 0.01, 0.01, 0.01};
            return commArr[findYear - 1];
        }

    }

}
