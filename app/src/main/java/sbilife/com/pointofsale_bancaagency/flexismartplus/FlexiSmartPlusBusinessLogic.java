package sbilife.com.pointofsale_bancaagency.flexismartplus;

import java.util.ArrayList;

import sbilife.com.pointofsale_bancaagency.common.CommonForAllProd;

class FlexiSmartPlusBusinessLogic {
    private CommonForAllProd cfap = null;
    private double sum = 0;
    private FlexiSmartPlusProperties FS_Prop = null;
    private FlexiSmartPlusBean flexiSmartPlusBean = null;
    private String month_E = null, year_F = null, policyInForce_G = "Y",
            age_H = null, premiumHolidayFlag_I = null, premium_J = null,
            topUpPremium_K = null, commissionPaid_L = null, expense_M = null,
            serviceTax_O = null, riskPremium_N = null,
            totalDeductions_P = null, openingBalance_Q = null,
            guarateedAdditionToPolicyAccount_R = null,
            extraAdditionToPolicyAccount_S = null, closingBalance_T = null,
            closingBalance_AA = null, openingBalance_X = null,
            guarateedAdditionToPolicyAccount_Y = null,
            extraAdditionToPolicyAccount_Z = null, deathBenefit_W = null,
            deathBenefit_AD = null, surrenderCharges_U = null,
            surrenderCharges_AB = null, surrenderValue_V = null,
            surrenderValue_AC = null, premiumAllocationCharge_L = null,
            policyAdministrationCharge_M = null, serviceTax_N = null,
            totalDeductions_O = null, openingBalance_P = null,
            riskPremiumMortality_Q = null, serviceTax_R = null,
            guarateedAdditionToPolicyAccount_S = null,
            extraAdditionToPolicyAccount_T = null, FMC_U = null,
            serviceTaxOnFMC_V = null, closingBalance_W = null,
            surrenderCharges_X = null, surrenderValue_Y = null,
            deathBenefit_Z = null, openingBalance_AA = null,
            riskPremiumMortality_AB = null, serviceTax_AC = null,
            guarateedAdditionToPolicyAccount_AD = null,
            extraAdditionToPolicyAccount_AE = null, FMC_AF = null,
            serviceTaxOnFMC_AG = null, closingBalance_AH = null,
            surrenderCharges_AI = null, surrenderValue_AJ = null,
            deathBenefit_AK = null,
            riskPremiumMortalityReductionYield_Q = null,
            serviceTaxReductionYield_R = null,
            guarateedAdditionToPolicyAccountReductionYield_S = null,
            extraAdditionToPolicyAccountReductionYield_T = null,
            FMCReductionYield_U = null, serviceTaxOnFMCReductionYield_V = null,
            closingBalanceReductionYield_W = null,
            surrenderChargesReductionYield_X = null,
            surrenderValueReductionYield_Y = null,
            deathBenefitReductionYield_Z = null,
            riskPremiumMortalityReductionYield_AB = null,
            serviceTaxReductionYield_AC = null,
            guarateedAdditionToPolicyAccountReductionYield_AD = null,
            extraAdditionToPolicyAccountReductionYield_AE = null,
            FMCReductionYield_AF = null,
            serviceTaxOnFMCReductionYield_AG = null,
            closingBalanceReductionYield_AH = null,
            surrenderChargesReductionYield_AI = null,
            surrenderValueReductionYield_AJ = null,
            deathBenefitReductionYield_AK = null,
            serviceTaxReductionYield_N = null,
            totalDeductionsReductionYield_O = null,
            openingBalanceReductionYield_P = null,
            openingBalanceReductionYield_AA = null, month_BB = null,
            reductionYield_AO = null, irrAnnual_AP = null,
            reductionInYieldMaturityAt = null,
            premiumAllocationChargeReductionYield_L = null;


    //Constructor[Initialization of object required to access general calculation methods is done here in constructor]
    public FlexiSmartPlusBusinessLogic() {
        FS_Prop = new FlexiSmartPlusProperties();
        cfap = new CommonForAllProd();
        flexiSmartPlusBean = new FlexiSmartPlusBean();
    }

    public String getTopUpPremium_K() {
        return topUpPremium_K;
    }

    public void setTopUpPremium_K(double effectiveTopUpPremium, FlexiSmartPlusBean flexiSmartBean) {
        String strToReturn = null;
        if (flexiSmartBean.getTopUpStatus().equals("Yes")) {
            if (Integer.parseInt(getMonth_E()) == 1) {
                strToReturn = "" + effectiveTopUpPremium;
            } else {
                strToReturn = "0";
            }
        } else {
            strToReturn = "0";
        }
        this.topUpPremium_K = strToReturn;
    }

    public String getAge_H() {
        return age_H;
    }

    public void setAge_H(int ageAtEntry) {
        this.age_H = "" + (ageAtEntry + Integer.parseInt(getYear_F()) - 1);
    }

    public String getPremiumHolidayFlag_I() {
        return premiumHolidayFlag_I;
    }

    public void setPremiumHolidayFlag_I(String premHolidayStatus, int policyYear, int premHolidayTerm) {
        String strToReturn = null;
        if (premHolidayStatus.equals("Yes")) {
            if ((Integer.parseInt(getYear_F()) >= policyYear) && (Integer.parseInt(getYear_F()) < (policyYear + premHolidayTerm))) {
                strToReturn = "0";
            } else {
                strToReturn = "1";
            }
        } else {
            strToReturn = "1";
        }
        this.premiumHolidayFlag_I = strToReturn;
    }

    public String getMonth_E() {
        return month_E;
    }

    public void setMonth_E(int rowNum) {
        this.month_E = ("" + rowNum);
    }

    public String getPolicyInForce_G() {
        return policyInForce_G;
    }

    public String getPremium_J() {
        return premium_J;
    }

    public void setPremium_J(int premiumPayingTerm, int PF, double anuualPremium) {
        String strTemp = null;
        if (getPolicyInForce_G().equals("Y")) {
            if ((Integer.parseInt(getYear_F()) <= premiumPayingTerm) && (((Integer.parseInt(getMonth_E()) - 1) % (12 / PF)) == 0)) {
                strTemp = "" + (anuualPremium / PF);
            } else {
                strTemp = ("" + 0);
            }
        } else {
            strTemp = ("" + 0);
        }

        this.premium_J = "" + (Double.parseDouble(strTemp) * Double.parseDouble(getPremiumHolidayFlag_I()));
    }

    public String getYear_F() {
        return year_F;
    }

    public void setYear_F() {
        this.year_F = cfap.getRoundUp("" + (Double.parseDouble(getMonth_E()) / 12));
    }

    public String getCommissionPaid_L() {
        return commissionPaid_L;
    }

    public void setCommissionPaid_L(FlexiSmartPlusBean flexiSmartBean) {
        if (Integer.parseInt(getYear_F()) <= flexiSmartBean.getPolicyTerm()) {
            this.commissionPaid_L = cfap.roundUp_Level2("" + (getCommisionComponent(flexiSmartBean) * Double.parseDouble(getPremium_J()) + Double.parseDouble(getTopUpPremium_K()) * FS_Prop.topUp_commission));
        } else {
            this.commissionPaid_L = "0";
        }
    }

    private double getCommisionComponent(FlexiSmartPlusBean flexiSmartBean) {

        if (Integer.parseInt(getYear_F()) == 1) {
            if (flexiSmartBean.getIsForStaffOrNot() == true && flexiSmartBean.getIsBancAssuranceDiscOrNot() == false) {
                return (getCommissionComponentOfPrem(flexiSmartBean) - getDiscComponentOfPrem(flexiSmartBean));
            } else {
                if (flexiSmartBean.getIsForStaffOrNot() == false && flexiSmartBean.getIsBancAssuranceDiscOrNot() == true) {
                    return (getCommissionComponentOfPrem(flexiSmartBean) - getDiscComponentOfPrem(flexiSmartBean) * 1);
                } else {
                    return getCommissionComponentOfPrem(flexiSmartBean);
                }
            }
        } else {
            if (flexiSmartBean.getIsForStaffOrNot() == true && flexiSmartBean.getIsBancAssuranceDiscOrNot() == false) {
                return (getCommissionComponentOfPrem(flexiSmartBean) - getDiscComponentOfPrem(flexiSmartBean));
            } else {
                if (flexiSmartBean.getIsForStaffOrNot() == false && flexiSmartBean.getIsBancAssuranceDiscOrNot() == true) {
                    return (getCommissionComponentOfPrem(flexiSmartBean) - getDiscComponentOfPrem(flexiSmartBean) * 0);
                } else {
                    return getCommissionComponentOfPrem(flexiSmartBean);
                }
            }
        }
    }

    private double getCommissionComponentOfPrem(FlexiSmartPlusBean flexiSmartBean) {
        if (Integer.parseInt(getYear_F()) == 1) {
            if (flexiSmartBean.getPremiumAmount() < 25000) {
                return 0.15;
            } else {
                return 0.20;
            }
        }
        if (Integer.parseInt(getYear_F()) == 2) {
            if (flexiSmartBean.getPremiumAmount() < 25000) {
                return 0.03;
            } else {
                return 0.03;
            }
        }
        if (Integer.parseInt(getYear_F()) == 3) {
            if (flexiSmartBean.getPremiumAmount() < 25000) {
                return 0.03;
            } else {
                return 0.03;
            }
        }
        if (Integer.parseInt(getYear_F()) == 4) {
            if (flexiSmartBean.getPremiumAmount() < 25000) {
                return 0.01;
            } else {
                return 0.01;
            }
        }
        if (Integer.parseInt(getYear_F()) == 5) {
            if (flexiSmartBean.getPremiumAmount() < 25000) {
                return 0.01;
            } else {
                return 0.01;
            }
        }
        if (Integer.parseInt(getYear_F()) == 6) {
            if (flexiSmartBean.getPremiumAmount() < 25000) {
                return 0.01;
            } else {
                return 0.01;
            }
        }
        if (Integer.parseInt(getYear_F()) == 7) {
            if (flexiSmartBean.getPremiumAmount() < 25000) {
                return 0.01;
            } else {
                return 0.01;
            }
        }
        if (Integer.parseInt(getYear_F()) == 8) {
            if (flexiSmartBean.getPremiumAmount() < 25000) {
                return 0.01;
            } else {
                return 0.01;
            }
        }
        if (Integer.parseInt(getYear_F()) == 9) {
            if (flexiSmartBean.getPremiumAmount() < 25000) {
                return 0.01;
            } else {
                return 0.01;
            }
        }
        if (Integer.parseInt(getYear_F()) == 10) {
            if (flexiSmartBean.getPremiumAmount() < 25000) {
                return 0.01;
            } else {
                return 0.01;
            }
        }
        if (Integer.parseInt(getYear_F()) > 10) {
            if (flexiSmartBean.getPremiumAmount() < 25000) {
                return 0.01;
            } else {
                return 0.01;
            }
        } else {
            return 0;
        }
    }

    private double getDiscComponentOfPrem(FlexiSmartPlusBean flexiSmartBean) {
        if (Integer.parseInt(getYear_F()) == 1) {
            if (flexiSmartBean.getPremiumAmount() < 25000) {
                return 0.15;
            } else {
                return 0.20;
            }
        }
        if (Integer.parseInt(getYear_F()) == 2) {
            if (flexiSmartBean.getPremiumAmount() < 25000) {
                return 0.03;
            } else {
                return 0.03;
            }
        }
        if (Integer.parseInt(getYear_F()) == 3) {
            if (flexiSmartBean.getPremiumAmount() < 25000) {
                return 0.03;
            } else {
                return 0.03;
            }
        }
        if (Integer.parseInt(getYear_F()) == 4) {
            if (flexiSmartBean.getPremiumAmount() < 25000) {
                return 0.01;
            } else {
                return 0.01;
            }
        }
        if (Integer.parseInt(getYear_F()) == 5) {
            if (flexiSmartBean.getPremiumAmount() < 25000) {
                return 0.01;
            } else {
                return 0.01;
            }
        }
        if (Integer.parseInt(getYear_F()) == 6) {
            if (flexiSmartBean.getPremiumAmount() < 25000) {
                return 0.01;
            } else {
                return 0.01;
            }
        }
        if (Integer.parseInt(getYear_F()) == 7) {
            if (flexiSmartBean.getPremiumAmount() < 25000) {
                return 0.01;
            } else {
                return 0.01;
            }
        }
        if (Integer.parseInt(getYear_F()) == 8) {
            if (flexiSmartBean.getPremiumAmount() < 25000) {
                return 0.01;
            } else {
                return 0.01;
            }
        }
        if (Integer.parseInt(getYear_F()) == 9) {
            if (flexiSmartBean.getPremiumAmount() < 25000) {
                return 0.01;
            } else {
                return 0.01;
            }
        }
        if (Integer.parseInt(getYear_F()) == 10) {
            if (flexiSmartBean.getPremiumAmount() < 25000) {
                return 0.01;
            } else {
                return 0.01;
            }
        }
        if (Integer.parseInt(getYear_F()) > 10) {
            if (flexiSmartBean.getPremiumAmount() < 25000) {
                return 0.01;
            } else {
                return 0.01;
            }
        } else {
            return 0;
        }
    }

    private String getExpense_M() {
        return expense_M;
    }

    public void setExpense_M(FlexiSmartPlusBean flexiSmartBean) {
        if (Integer.parseInt(getYear_F()) <= flexiSmartBean.getPolicyTerm()) {
            this.expense_M = cfap.roundUp_Level2("" + (getExpenseComponent(flexiSmartBean) * Double.parseDouble(getPremium_J()) + Double.parseDouble(getTopUpPremium_K()) * FS_Prop.topUp_expense));
        } else {
            this.expense_M = "0";
        }
    }

    private double getExpenseComponent(FlexiSmartPlusBean flexiSmartBean) {
        if (Integer.parseInt(getYear_F()) == 1) {
            if (flexiSmartBean.getPremiumAmount() < 25000) {
                return 0.125;
            } else {
                return 0.075;
            }
        }
        if (Integer.parseInt(getYear_F()) == 2) {
            if (flexiSmartBean.getPremiumAmount() < 25000) {
                return 0.045;
            } else {
                return 0.045;
            }
        }
        if (Integer.parseInt(getYear_F()) == 3) {
            if (flexiSmartBean.getPremiumAmount() < 25000) {
                return 0.045;
            } else {
                return 0.045;
            }
        }
        if (Integer.parseInt(getYear_F()) == 4) {
            if (flexiSmartBean.getPremiumAmount() < 25000) {
                return 0.04;
            } else {
                return 0.04;
            }
        }
        if (Integer.parseInt(getYear_F()) == 5) {
            if (flexiSmartBean.getPremiumAmount() < 25000) {
                return 0.04;
            } else {
                return 0.04;
            }
        }
        if (Integer.parseInt(getYear_F()) == 6) {
            if (flexiSmartBean.getPremiumAmount() < 25000) {
                return 0.04;
            } else {
                return 0.04;
            }
        }
        if (Integer.parseInt(getYear_F()) == 7) {
            if (flexiSmartBean.getPremiumAmount() < 25000) {
                return 0.04;
            } else {
                return 0.04;
            }
        }
        if (Integer.parseInt(getYear_F()) == 8) {
            if (flexiSmartBean.getPremiumAmount() < 25000) {
                return 0.04;
            } else {
                return 0.04;
            }
        }
        if (Integer.parseInt(getYear_F()) == 9) {
            if (flexiSmartBean.getPremiumAmount() < 25000) {
                return 0.04;
            } else {
                return 0.04;
            }
        }
        if (Integer.parseInt(getYear_F()) == 10) {
            if (flexiSmartBean.getPremiumAmount() < 25000) {
                return 0.04;
            } else {
                return 0.04;
            }
        }
        if (Integer.parseInt(getYear_F()) > 10) {
            if (flexiSmartBean.getPremiumAmount() < 25000) {
                return 0.04;
            } else {
                return 0.04;
            }
        } else {
            return 0;
        }
    }

    private String getRiskPremium_N() {
        return riskPremium_N;
    }

    public void setRiskPremium_N(FlexiSmartPlusBean flexiSmartBean, String[] forBIArray) {
        double temp = 0;
        double arrOutput = Double.parseDouble(forBIArray[(flexiSmartBean.getAgeAtEntry() + Integer.parseInt(getYear_F())) - 1]);
        if (!getPolicyInForce_G().equals("Y") || (Integer.parseInt(getYear_F()) > flexiSmartBean.getPolicyTerm())) {
            temp = 0;
        } else {
            if ((Integer.parseInt(getMonth_E()) - 1) % (12 / flexiSmartBean.getPF()) == 0) {
                temp = (arrOutput / (1000 * flexiSmartBean.getPF()) * flexiSmartBean.getSumAssured());
            } else {
                temp = 0;
            }
        }
        this.riskPremium_N = cfap.roundUp_Level2("" + ((temp) * Double.parseDouble(getPremiumHolidayFlag_I())));
    }

    private String getServiceTax_O() {
        return this.serviceTax_O;
    }

    public void setServiceTax_O() {
        this.serviceTax_O = cfap.roundUp_Level2("" + ((Double.parseDouble(getCommissionPaid_L()) + Double.parseDouble(getExpense_M()) + Double.parseDouble(getRiskPremium_N())) * flexiSmartPlusBean.getServiceTax()));
    }

    private String getTotalDeductions_P() {
        return this.totalDeductions_P;
    }

    public void setTotalDeductions_P() {
        this.totalDeductions_P = cfap.roundUp_Level2("" + (Double.parseDouble(getCommissionPaid_L()) + Double.parseDouble(getExpense_M()) + Double.parseDouble(getRiskPremium_N()) + Double.parseDouble(getServiceTax_O())));
    }

    private String getOpeningBalance_Q() {
        return this.openingBalance_Q;
    }

    public void setOpeningBalance_Q(double _closingBalance_T) {
        if (policyInForce_G.equals("Y")) {
            this.openingBalance_Q = cfap.roundUp_Level2("" + (Double.parseDouble(getPremium_J()) + Double.parseDouble(getTopUpPremium_K()) - Double.parseDouble(getTotalDeductions_P()) + _closingBalance_T));
        } else {
            this.openingBalance_Q = "0";
        }
    }

    private String getGuarateedAdditionToPolicyAccount_R() {
        return this.guarateedAdditionToPolicyAccount_R;
    }

    public void setGuarateedAdditionToPolicyAccount_R(FlexiSmartPlusBean flexiSmartBean) {
        if (Integer.parseInt(getYear_F()) <= flexiSmartBean.getPolicyTerm()) {
            this.guarateedAdditionToPolicyAccount_R = cfap.roundUp_Level2("" + (Double.parseDouble(getOpeningBalance_Q()) * (cfap.pow((1 + FS_Prop.guaranteedInterest), (double) 1 / 12) - 1)));
        } else {
            this.guarateedAdditionToPolicyAccount_R = "0";
        }
    }

    private String getExtraAdditionToPolicyAccount_S() {
        return this.extraAdditionToPolicyAccount_S;
    }

    public void setExtraAdditionToPolicyAccount_S(FlexiSmartPlusBean flexiSmartBean) {
        if (Integer.parseInt(getYear_F()) <= flexiSmartBean.getPolicyTerm()) {
            this.extraAdditionToPolicyAccount_S = cfap.roundUp_Level2("" + (Double.parseDouble(getOpeningBalance_Q()) * (cfap.pow((1 + ((FS_Prop.int1 - FS_Prop.fundManagementCharge) - FS_Prop.guaranteedInterest)), (double) 1 / 12) - 1)));
        } else {
            this.extraAdditionToPolicyAccount_S = "0";
        }
    }

    private String getClosingBalance_T() {
        return this.closingBalance_T;
    }

    public void setClosingBalance_T() {
        this.closingBalance_T = cfap.roundUp_Level2("" + (Double.parseDouble(getOpeningBalance_Q()) + Double.parseDouble(getGuarateedAdditionToPolicyAccount_R()) + Double.parseDouble(getExtraAdditionToPolicyAccount_S())));
    }

    public String getDeathBenefit_W() {
        return this.deathBenefit_W;
    }

    public void setDeathBenefit_W(FlexiSmartPlusBean flexiSmartBean) {
        if (Integer.parseInt(getYear_F()) > flexiSmartBean.getPolicyTerm()) {
            this.deathBenefit_W = "0";
        } else {
            this.deathBenefit_W = "" + (flexiSmartBean.getSumAssured() + Double.parseDouble(getClosingBalance_T()));
        }
    }

    public String getDeathBenefit_AD() {
        return this.deathBenefit_AD;
    }

    public void setDeathBenefit_AD(FlexiSmartPlusBean flexiSmartBean) {
        if (Integer.parseInt(getYear_F()) > flexiSmartBean.getPolicyTerm()) {
            this.deathBenefit_AD = "0";
        } else {
            this.deathBenefit_AD = "" + (flexiSmartBean.getSumAssured() + Double.parseDouble(getClosingBalance_AA()));
        }
    }

    private String getOpeningBalance_X() {
        return this.openingBalance_X;
    }

    public void setOpeningBalance_X(double _closingBalance_AA) {
        if (policyInForce_G.equals("Y")) {
            this.openingBalance_X = cfap.roundUp_Level2("" + (Double.parseDouble(getPremium_J()) + Double.parseDouble(getTopUpPremium_K()) - Double.parseDouble(getTotalDeductions_P()) + _closingBalance_AA));
        } else {
            this.openingBalance_X = "0";
        }
    }

    private String getGuarateedAdditionToPolicyAccount_Y() {
        return this.guarateedAdditionToPolicyAccount_Y;
    }

    public void setGuarateedAdditionToPolicyAccount_Y(FlexiSmartPlusBean flexiSmartBean) {
        if (Integer.parseInt(getYear_F()) <= flexiSmartBean.getPolicyTerm()) {
            this.guarateedAdditionToPolicyAccount_Y = cfap.roundUp_Level2("" + (Double.parseDouble(getOpeningBalance_X()) * (cfap.pow((1 + FS_Prop.guaranteedInterest), (double) 1 / 12) - 1)));
        } else {
            this.guarateedAdditionToPolicyAccount_Y = "0";
        }
    }

    private String getExtraAdditionToPolicyAccount_Z() {
        return this.extraAdditionToPolicyAccount_Z;
    }

    public void setExtraAdditionToPolicyAccount_Z(FlexiSmartPlusBean flexiSmartBean) {
        if (Integer.parseInt(getYear_F()) <= flexiSmartBean.getPolicyTerm()) {
            this.extraAdditionToPolicyAccount_Z = cfap.roundUp_Level2("" + (Double.parseDouble(getOpeningBalance_X()) * (cfap.pow((1 + ((FS_Prop.int2 - FS_Prop.fundManagementCharge) - FS_Prop.guaranteedInterest)), (double) 1 / 12) - 1)));
        } else {
            this.extraAdditionToPolicyAccount_Z = "0";
        }
    }

    private String getClosingBalance_AA() {
        return this.closingBalance_AA;
    }

    public void setClosingBalance_AA() {
        this.closingBalance_AA = cfap.roundUp_Level2("" + (Double.parseDouble(getOpeningBalance_X()) + Double.parseDouble(getGuarateedAdditionToPolicyAccount_Y()) + Double.parseDouble(getExtraAdditionToPolicyAccount_Z())));
    }

    private String getSurrenderCharges_U() {
        return this.surrenderCharges_U;
    }

    public void setSurrenderCharges_U() {
        if (Integer.parseInt(getYear_F()) == 1) {
            this.surrenderCharges_U = "0";
        } else {
            this.surrenderCharges_U = cfap.roundUp_Level2("" + (Double.parseDouble(getClosingBalance_T()) * getSurrenderCharges()));
        }
    }

    private String getSurrenderCharges_AB() {
        return this.surrenderCharges_AB;
    }

    public void setSurrenderCharges_AB() {
        if (Integer.parseInt(getYear_F()) == 1) {
            this.surrenderCharges_AB = "0";
        } else {
            this.surrenderCharges_AB = cfap.roundUp_Level2("" + (Double.parseDouble(getClosingBalance_AA()) * getSurrenderCharges()));
        }
    }

    public String getSurrenderValue_V() {
        return this.surrenderValue_V;
    }

    public void setSurrenderValue_V() {
        this.surrenderValue_V = cfap.roundUp_Level2("" + (Double.parseDouble(getClosingBalance_T()) - Double.parseDouble(getSurrenderCharges_U())));
    }

    public String getSurrenderValue_AC() {
        return this.surrenderValue_AC;
    }

    public void setSurrenderValue_AC() {
        this.surrenderValue_AC = cfap.roundUp_Level2("" + (Double.parseDouble(getClosingBalance_AA()) - Double.parseDouble(getSurrenderCharges_AB())));
    }

    private double getSurrenderCharges() {
        double surrenderCharge = 0;
        if (getYear_F().equals("1")) {
            surrenderCharge = 0;
        } else if (getYear_F().equals("2")) {
            surrenderCharge = 0;
        } else if (getYear_F().equals("3")) {
            surrenderCharge = 0;
        } else if (getYear_F().equals("4")) {
            surrenderCharge = 0.02;
        } else if (getYear_F().equals("5")) {
            surrenderCharge = 0.02;
        } else if (getYear_F().equals("6")) {
            surrenderCharge = 0;
        } else if (getYear_F().equals("7")) {
            surrenderCharge = 0;
        } else if (getYear_F().equals("8")) {
            surrenderCharge = 0;
        } else if (getYear_F().equals("9")) {
            surrenderCharge = 0;
        } else if (getYear_F().equals("10")) {
            surrenderCharge = 0;
        } else if (Integer.parseInt(getYear_F()) > 10) {
            surrenderCharge = 0;
        }
        return surrenderCharge;
    }

    //*******************************************************************************************************

    public double getSurrenderCharge(String premFreqmode, double effectivePremium) {
        double surrenderCharge = 0;
        if (premFreqmode.equals("Limited")) {
            surrenderCharge = Double.parseDouble(calSurrRateArr(effectivePremium)[Integer.parseInt(getYear_F()) - 1]);
        }
        //For Single Mode of Premium Freqency
        else {
            surrenderCharge = 0;
        }
        return surrenderCharge;
    }

    //Related to -> T37 / AA37
    public double getAllocationCharge(boolean staffDisc, boolean bancAssuranceDisc, String premFreqmode) {
        double allocationChargeWithoutDisc = 0;
        double discOnallocationCharge = 0;
        if (premFreqmode.equals("Limited")) {
            if (getYear_F().equals("1")) {
                allocationChargeWithoutDisc = 0.085;
                discOnallocationCharge = 0.08;
            } else if (getYear_F().equals("2")) {
                allocationChargeWithoutDisc = 0.06;
                discOnallocationCharge = 0.02;
            } else if (getYear_F().equals("3")) {
                allocationChargeWithoutDisc = 0.06;
                discOnallocationCharge = 0.02;
            } else if (getYear_F().equals("4")) {
                allocationChargeWithoutDisc = 0.06;
                discOnallocationCharge = 0.01;
            } else if (getYear_F().equals("5")) {
                allocationChargeWithoutDisc = 0.06;
                discOnallocationCharge = 0.01;
            } else if (getYear_F().equals("6")) {
                allocationChargeWithoutDisc = 0;
                discOnallocationCharge = 0;
            } else if (getYear_F().equals("7")) {
                allocationChargeWithoutDisc = 0;
                discOnallocationCharge = 0;
            } else if (getYear_F().equals("8")) {
                allocationChargeWithoutDisc = 0;
                discOnallocationCharge = 0;
            } else if (getYear_F().equals("9")) {
                allocationChargeWithoutDisc = 0;
                discOnallocationCharge = 0;
            } else if (getYear_F().equals("10")) {
                allocationChargeWithoutDisc = 0;
                discOnallocationCharge = 0;
            }
        }
        //For Single Mode of Premium Freqency
        else {
            if (getYear_F().equals("1")) {
                allocationChargeWithoutDisc = 0.03;
                discOnallocationCharge = 0.02;
            } else if (getYear_F().equals("2")) {
                allocationChargeWithoutDisc = 0.0;
                discOnallocationCharge = 0.0;
            } else if (getYear_F().equals("3")) {
                allocationChargeWithoutDisc = 0.0;
                discOnallocationCharge = 0.0;
            } else if (getYear_F().equals("4")) {
                allocationChargeWithoutDisc = 0.0;
                discOnallocationCharge = 0.0;
            } else if (getYear_F().equals("5")) {
                allocationChargeWithoutDisc = 0.0;
                discOnallocationCharge = 0.0;
            } else if (getYear_F().equals("6")) {
                allocationChargeWithoutDisc = 0.0;
                discOnallocationCharge = 0.0;
            } else if (getYear_F().equals("7")) {
                allocationChargeWithoutDisc = 0.0;
                discOnallocationCharge = 0.0;
            } else if (getYear_F().equals("8")) {
                allocationChargeWithoutDisc = 0.0;
                discOnallocationCharge = 0.0;
            } else if (getYear_F().equals("9")) {
                allocationChargeWithoutDisc = 0.0;
                discOnallocationCharge = 0.0;
            } else if (getYear_F().equals("10")) {
                allocationChargeWithoutDisc = 0.0;
                discOnallocationCharge = 0.0;
            }
        }
        if (staffDisc == true && bancAssuranceDisc == false) {
            String dummyStr1 = "" + (allocationChargeWithoutDisc - discOnallocationCharge);
            String dummyStr2 = dummyStr1 + "0000000000";
            int decIndex = dummyStr2.indexOf('.');
            String dummyStr3 = null;
            if (dummyStr2.substring(decIndex + 3, decIndex + 7).equals("9999")) {
                dummyStr3 = ("" + (Double.parseDouble(dummyStr2) + 0.00001)).substring(0, decIndex + 5);
            } else {
                dummyStr3 = dummyStr2.substring(0, decIndex + 5);
            }
            return Double.parseDouble(dummyStr3);
        } else if (staffDisc == false && bancAssuranceDisc == true) {
            int mulFactor = 0;
            if (getYear_F().equals("1")) {
                mulFactor = 1;
            } else {
                mulFactor = 0;
            }
            String dummyStr1 = "" + (allocationChargeWithoutDisc - discOnallocationCharge * mulFactor);
            String dummyStr2 = dummyStr1 + "0000000000";
            int decIndex = dummyStr2.indexOf('.');
            String dummyStr3 = null;
            if (dummyStr2.substring(decIndex + 3, decIndex + 7).equals("9999")) {
                dummyStr3 = ("" + (Double.parseDouble(dummyStr2) + 0.00001)).substring(0, decIndex + 5);
            } else {
                dummyStr3 = dummyStr2.substring(0, decIndex + 5);
            }
            return Double.parseDouble(dummyStr3);
        } else {
            return allocationChargeWithoutDisc;
        }
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

    public int getFinYrAfterLaunch(String dateOfInception, String dateOfLaunch) {
        return (getFinYrForCalOfAdminCharges(dateOfInception) - getYrOfDateOfLaunch(dateOfLaunch)) - 1;
    }

    private int getYrOfDateOfLaunch(String dateOfLaunch) {
        String[] dummyArr = cfap.split(dateOfLaunch, "/");
        return Integer.parseInt(dummyArr[2]);
    }

    private int getFinYrForCalOfAdminCharges(String dateOfInception) {
        if (getMonthOfInception(dateOfInception) < 4) {
            return getYearOfInception(dateOfInception);
        } else {
            return getYearOfInception(dateOfInception) + 1;
        }
    }

    private int getMonthOfInception(String dateOfInception) {
        String[] dummyArr = cfap.split(dateOfInception, "/");
        return Integer.parseInt(dummyArr[0]);
    }

    private int getYearOfInception(String dateOfInception) {
        String[] dummyArr = cfap.split(dateOfInception, "/");
        return Integer.parseInt(dummyArr[2]);
    }

    private String[] calSurrRateArr(double effectivePremium) {
        if (effectivePremium <= 25000) {
            return new String[]{"0.20", "0.15", "0.10", "0.05", "0", "0", "0", "0", "0", "0", "0"};
        } else {
            return new String[]{"0.06", "0.04", "0.03", "0.02", "0", "0", "0", "0", "0", "0", "0"};
        }
    }

    private String[] calSurrRateArr() {
        {
            return new String[]{"0.06", "0.04", "0.03", "0.02", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"};
        }
    }

    public String[] calCapArr(double effectivePremium) {
        if (effectivePremium <= 25000) {
            return new String[]{"3000", "2000", "1500", "1000", "0", "0", "0", "0", "0", "0", "0", "0"};
        } else {
            return new String[]{"6000", "5000", "4000", "2000", "0", "0", "0", "0", "0", "0", "0", "0"};
        }
    }

    private String[] calCapArr() {
        {
            return new String[]{"6000", "5000", "4000", "2000", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"};
        }
    }


    public String[] getForBIArr(double riskPremiumRate) {
        String[] strArrTReturn = new String[99];
        FlexiSmartPlusDB flexiSmartDB = new FlexiSmartPlusDB();
        for (int i = 0; i < flexiSmartDB.getIAIarray().length - 2; i++) {
            strArrTReturn[i] = cfap.roundUp_Level2_FS(cfap.getStringWithout_E(((flexiSmartDB.getIAIarray()[i] + flexiSmartDB.getIAIarray()[i + 1]) / 2) * riskPremiumRate * 1000));
        }
        return strArrTReturn;
    }

    private int getModalFactor(FlexiSmartPlusBean flexiSmartBean) {
        int modalFactor = 0;
        if (flexiSmartBean.getPremFreqMode().equals("Quarterly")) {
            modalFactor = 4;
        } else if (flexiSmartBean.getPremFreqMode().equals("Yearly")) {
            modalFactor = 1;
        } else if (flexiSmartBean.getPremFreqMode().equals("Monthly")) {
            modalFactor = 12;
        } else {
            modalFactor = 2;
        }
        return modalFactor;
    }

    private double getApplicableCharges(FlexiSmartPlusBean flexiSmartBean) {
        double applicableCharge = 0, a, additionalAllocation = 0;
        double premium = flexiSmartBean.getPremiumAmount();
        boolean staffDiscount = flexiSmartBean.getIsForStaffOrNot();
        if (Integer.parseInt(getYear_F()) == 1) {
            if (premium * getModalFactor(flexiSmartBean) < 25000) {
                a = 0.09;
            } else {
                a = 0.09;
            }
            if (staffDiscount) {
                additionalAllocation = 0.075;
            } else {
                additionalAllocation = 0;
            }

            applicableCharge = (a - additionalAllocation);
        } else if (Integer.parseInt(getYear_F()) == 2) {
            if (premium * getModalFactor(flexiSmartBean) < 25000) {
                a = 0.065;
            } else {
                a = 0.065;
            }
            if (staffDiscount) {
                additionalAllocation = 0.03;
            } else {
                additionalAllocation = 0;
            }

            applicableCharge = (a - additionalAllocation);
        } else if (Integer.parseInt(getYear_F()) == 3) {
            if (premium * getModalFactor(flexiSmartBean) < 25000) {
                a = 0.065;
            } else {
                a = 0.065;
            }
            if (staffDiscount) {
                additionalAllocation = 0.03;
            } else {
                additionalAllocation = 0;
            }

            applicableCharge = (a - additionalAllocation);
        } else if (Integer.parseInt(getYear_F()) == 4) {
            if (premium * getModalFactor(flexiSmartBean) < 25000) {
                a = 0.065;
            } else {
                a = 0.065;
            }
            if (staffDiscount) {
                additionalAllocation = 0.025;
            } else {
                additionalAllocation = 0;
            }

            applicableCharge = (a - additionalAllocation);
        } else if (Integer.parseInt(getYear_F()) == 5) {
            if (premium * getModalFactor(flexiSmartBean) < 25000) {
                a = 0.06;
            } else {
                a = 0.06;
            }
            if (staffDiscount) {
                additionalAllocation = 0.025;
            } else {
                additionalAllocation = 0;
            }

            applicableCharge = (a - additionalAllocation);
        } else if (Integer.parseInt(getYear_F()) == 6) {
            if (premium * getModalFactor(flexiSmartBean) < 25000) {
                a = 0.05;
            } else {
                a = 0.05;
            }
            if (staffDiscount) {
                additionalAllocation = 0.02;
            } else {
                additionalAllocation = 0;
            }

            applicableCharge = (a - additionalAllocation);
        } else if (Integer.parseInt(getYear_F()) == 7) {
            if (premium * getModalFactor(flexiSmartBean) < 25000) {
                a = 0.05;
            } else {
                a = 0.05;
            }
            if (staffDiscount) {
                additionalAllocation = 0.02;
            } else {
                additionalAllocation = 0;
            }

            applicableCharge = (a - additionalAllocation);
        } else if (Integer.parseInt(getYear_F()) == 8) {
            if (premium * getModalFactor(flexiSmartBean) < 25000) {
                a = 0.05;
            } else {
                a = 0.05;
            }
            if (staffDiscount) {
                additionalAllocation = 0.02;
            } else {
                additionalAllocation = 0;
            }

            applicableCharge = (a - additionalAllocation);
        } else if (Integer.parseInt(getYear_F()) == 9) {
            if (premium * getModalFactor(flexiSmartBean) < 25000) {
                a = 0.05;
            } else {
                a = 0.05;
            }
            if (staffDiscount) {
                additionalAllocation = 0.01;
            } else {
                additionalAllocation = 0;
            }

            applicableCharge = (a - additionalAllocation);
        } else if (Integer.parseInt(getYear_F()) == 10) {
            if (premium * getModalFactor(flexiSmartBean) < 25000) {
                a = 0.05;
            } else {
                a = 0.05;
            }
            if (staffDiscount) {
                additionalAllocation = 0.01;
            } else {
                additionalAllocation = 0;
            }

            applicableCharge = (a - additionalAllocation);
        } else if (Integer.parseInt(getYear_F()) > 10) {
            if (premium * getModalFactor(flexiSmartBean) < 25000) {
                a = 0.05;
            } else {
                a = 0.05;
            }
            if (staffDiscount) {
                additionalAllocation = 0.01;
            } else {
                additionalAllocation = 0;
            }

            applicableCharge = (a - additionalAllocation);
        }
        return applicableCharge;
    }

    public void setPremiumAllocationCharge_L(FlexiSmartPlusBean flexiSmartBean) {
        if (Integer.parseInt(getYear_F()) <= flexiSmartBean.getPolicyTerm()) {
            this.premiumAllocationCharge_L = cfap.roundUp_Level2(cfap.getStringWithout_E(getApplicableCharges(flexiSmartBean) * Double.parseDouble(getPremium_J()) + Double.parseDouble(getTopUpPremium_K()) * FS_Prop.topUp));
        } else {
            this.premiumAllocationCharge_L = "" + 0;
        }
    }

    public String getPremiumAllocationCharge_L() {
        return premiumAllocationCharge_L;
    }

    public void setPolicyAdministartionCharge_M() {
        double a = 0;
        if (Integer.parseInt(getYear_F()) == 1) {
            a = FS_Prop.initial;
        } else {
            a = (FS_Prop.renewal * (Math.pow((1 + FS_Prop.inflation), (Integer.parseInt(getYear_F()) - 2))));
        }
        if (getPolicyInForce_G().equals("Y")) {
            this.policyAdministrationCharge_M = "" + a / 12;
        } else {
            this.policyAdministrationCharge_M = "" + 0;
        }
    }

    public String getPolicyAdministrationCharge_M() {
        return policyAdministrationCharge_M;
    }

    public double getServiceTax(FlexiSmartPlusBean flexiSmartBean, double serviceTax) {
        if (flexiSmartBean.getJkResident() == true) {
            return FS_Prop.serviceTaxJkResident;
        } else {
            return serviceTax;
        }
    }

    public void setServiceTax_N(boolean jkResident, FlexiSmartPlusBean flexiSmartBean, double serviceTax) {
        if (jkResident) {
            this.serviceTax_N = "" + 0;
        } else {
            this.serviceTax_N = cfap.roundUp_Level2(cfap.getStringWithout_E((Double.parseDouble(getPremiumAllocationCharge_L()) + Double.parseDouble(getPolicyAdministrationCharge_M())) * getServiceTax(flexiSmartBean, serviceTax)));
        }

    }

    public String getServiceTax_N() {
        return serviceTax_N;
    }

    public void setTotalDeductions_O() {
        this.totalDeductions_O = "" + (Double.parseDouble(getPremiumAllocationCharge_L()) + Double.parseDouble(getPolicyAdministrationCharge_M()) + Double.parseDouble(getServiceTax_N()));
    }

    public String getTotalDeductions_O() {
        return totalDeductions_O;
    }


    public void setOpeningBalance_P(double _closingBalance_W) {
        if (getPolicyInForce_G().equals("Y")) {
//	        {this.openingBalance_P=cfap.roundUp_Level2(""+(Double.parseDouble(getPremium_J())+Double.parseDouble(getTopUpPremium_K())-Double.parseDouble(getTotalDeductions_O())+_closingBalance_W));
//	        	System.out.println("premium "+Double.parseDouble(getPremium_J()));
//	        	System.out.println("topup "+Double.parseDouble(getTopUpPremium_K()));
//	        	System.out.println("deduction "+Double.parseDouble(getTotalDeductions_O()));
//	        	System.out.println("closing balance "+_closingBalance_W);
//	        	System.out.println("calc  "+(Double.parseDouble(getPremium_J())+Double.parseDouble(getTopUpPremium_K()) - Double.parseDouble(getTotalDeductions_O()) + _closingBalance_W));
//	        	System.out.println("subtarction "+ (0 + 0 - Double.parseDouble(getTotalDeductions_O()) + _closingBalance_W));
            this.openingBalance_P = cfap.roundUp_Level2(cfap.getStringWithout_E(Double.parseDouble(getPremium_J()) + Double.parseDouble(getTopUpPremium_K()) - Double.parseDouble(getTotalDeductions_O()) + _closingBalance_W));
        } else {
            this.openingBalance_P = "0";
        }
    }

    public String getOpeningBalance_P() {
        return this.openingBalance_P;
    }

    public void setRiskPremiumMortality_Q(FlexiSmartPlusBean flexiSmartBean, String[] forBIArray, String option, double sumAssured) {
        double temp = 0, a = 0, max = 0;
        double arrOutput = Double.parseDouble(forBIArray[(flexiSmartBean.getAgeAtEntry() + Integer.parseInt(getYear_F())) - 1]);
        temp = arrOutput / 12;
        if (option.equals("PLATINUM")) {
            a = 0;
        } else {
            a = Double.parseDouble(getOpeningBalance_P());
        }

        max = Math.max(0, (sumAssured - a));
        if (getPolicyInForce_G().equals("Y")) {
            this.riskPremiumMortality_Q = "" + ((temp * max) / 1000);
        } else {
            this.riskPremiumMortality_Q = "" + 0;
        }
    }

    public String getRiskPremiumMortality_Q() {
        return riskPremiumMortality_Q;
    }

    public void setServiceTax_R(FlexiSmartPlusBean flexiSmartBean, double serviceTax) {
        if (getPolicyInForce_G().equals("Y")) {
            this.serviceTax_R = "" + (Double.parseDouble(getRiskPremiumMortality_Q()) * getServiceTax(flexiSmartBean, serviceTax));
        } else {
            this.serviceTax_R = "" + 0;
        }
    }

    public String getServiceTax_R() {
        return serviceTax_R;
    }

    public String getGuarateedAdditionToPolicyAccount_S() {
        return this.guarateedAdditionToPolicyAccount_S;
    }

    public void setGuarateedAdditionToPolicyAccount_S(FlexiSmartPlusBean flexiSmartBean) {
        if (Integer.parseInt(getYear_F()) <= flexiSmartBean.getPolicyTerm()) {
            this.guarateedAdditionToPolicyAccount_S = cfap.roundUp_Level2("" + ((Double.parseDouble(getOpeningBalance_P()) - Double.parseDouble(getRiskPremiumMortality_Q()) - Double.parseDouble(getServiceTax_R())) * (cfap.pow((1 + FS_Prop.guaranteedInterest), (double) 1 / 12) - 1)));
        } else {
            this.guarateedAdditionToPolicyAccount_S = "0";
        }
//	        System.out.println(getYear_F()+"    "+getMonth_E()+" this.guarateedAdditionToPolicyAccount_S   : "+this.guarateedAdditionToPolicyAccount_S);
    }

    public String getExtraAdditionToPolicyAccount_T() {
        return this.extraAdditionToPolicyAccount_T;
    }

    public void setExtraAdditionToPolicyAccount_T(FlexiSmartPlusBean flexiSmartBean) {
        double a;
        if (Integer.parseInt(getYear_F()) <= flexiSmartBean.getPolicyTerm()) {
//	        	this.extraAdditionToPolicyAccount_T=cfap.roundUp_Level2(""+((Double.parseDouble(getOpeningBalance_P())-Double.parseDouble(getRiskPremiumMortality_Q())-Double.parseDouble(getServiceTax_R()))*(cfap.pow((1+(FS_Prop.int1-FS_Prop.guaranteedInterest)),(double)1/12)-1)));
            a = (Double.parseDouble(getOpeningBalance_P()) - Double.parseDouble(getRiskPremiumMortality_Q()) - Double.parseDouble(getServiceTax_R())) * (cfap.pow((1 + (FS_Prop.int1 - FS_Prop.guaranteedInterest)), (double) 1 / 12) - 1);
            this.extraAdditionToPolicyAccount_T = "" + cfap.getRoundOffLevel2New(cfap.getStringWithout_E(a));
//			System.out.println("EXTRA ADDITION "+getMonth_E()+"=" + extraAdditionToPolicyAccount_T);
        } else {
            this.extraAdditionToPolicyAccount_T = "0";
        }
//		        System.out.println(getYear_F()+"    "+getMonth_E()+"   this.extraAdditionToPolicyAccount_T   :  "+this.extraAdditionToPolicyAccount_T);
    }

    public void setFMC_U() {
        if (getPolicyInForce_G().equals("Y")) {
            this.FMC_U = "" + ((Double.parseDouble(getOpeningBalance_P()) - Double.parseDouble(getRiskPremiumMortality_Q()) - Double.parseDouble(getServiceTax_R()) + Double.parseDouble(getGuarateedAdditionToPolicyAccount_S()) + Double.parseDouble(getExtraAdditionToPolicyAccount_T())) * 0.000625);
        } else {
            this.FMC_U = "" + 0;
        }
    }

    public String getFMC_U() {
        return FMC_U;
    }

    public void setServiceTaxOnFMC_V(boolean jkResident, FlexiSmartPlusBean flexiSmartBean, double serviceTax) {
        if (!(jkResident) && getPolicyInForce_G().equals("Y")) {
            this.serviceTaxOnFMC_V = "" + (Double.parseDouble(getFMC_U()) * getServiceTax(flexiSmartBean, serviceTax));
        } else {
            this.serviceTaxOnFMC_V = "" + 0;
        }
    }

    public String getServiceTaxOnFMC_V() {
        return serviceTaxOnFMC_V;
    }

    public void setClosingBalance_W() {
        this.closingBalance_W = cfap.roundUp_Level2(cfap.getStringWithout_E(Double.parseDouble(getOpeningBalance_P()) - Double.parseDouble(getRiskPremiumMortality_Q()) - Double.parseDouble(getServiceTax_R()) + Double.parseDouble(getGuarateedAdditionToPolicyAccount_S()) + Double.parseDouble(getExtraAdditionToPolicyAccount_T()) - Double.parseDouble(getFMC_U()) - Double.parseDouble(getServiceTaxOnFMC_V())));
        this.closingBalance_W = cfap.roundUp_Level2(cfap.getStringWithout_E(Double.parseDouble(getOpeningBalance_P()) - Double.parseDouble(getRiskPremiumMortality_Q()) - Double.parseDouble(getServiceTax_R()) + Double.parseDouble(getGuarateedAdditionToPolicyAccount_S()) + Double.parseDouble(getExtraAdditionToPolicyAccount_T()) - Double.parseDouble(getFMC_U()) - Double.parseDouble(getServiceTaxOnFMC_V())));
    }

    public String getClosingBalance_W() {
        return closingBalance_W;
    }

    public void setSurrenderCharges_X(double premiumAmt) {
        double min = Math.min(Double.parseDouble(getClosingBalance_W()), premiumAmt);
        double surrenderCharge = 0;
        {
            surrenderCharge = Double.parseDouble(calSurrRateArr()[Math.min((Integer.parseInt(getYear_F()) - 1), 11)]);
        }
//	        {surrenderCharge=Double.parseDouble(calSurrRateArr()[(Integer.parseInt(getYear_F())-1)]);}

        double multi = (min * surrenderCharge);
        double a = Double.parseDouble(calCapArr()[Math.min((Integer.parseInt(getYear_F()) - 1), 11)]);
//	        double a=Double.parseDouble(calCapArr()[(Integer.parseInt(getYear_F())-1)]);
        if (getPolicyInForce_G().equals("Y")) {
            this.surrenderCharges_X = cfap.roundUp_Level2(cfap.getStringWithout_E((Math.min((multi), a))));
        } else {
            this.surrenderCharges_X = "" + 0;
        }
    }

    public String getSurrenderCharges_X() {
        return surrenderCharges_X;
    }

    public void setSurrenderValue_Y(FlexiSmartPlusBean flexiSmartBean, double serviceTax) {
        if (getPolicyInForce_G().equals("Y")) {
            this.surrenderValue_Y = cfap.roundUp_Level2(cfap.getStringWithout_E(Double.parseDouble(getClosingBalance_W()) - Double.parseDouble(getSurrenderCharges_X()) * (1 + getServiceTax(flexiSmartBean, serviceTax))));
        } else {
            this.surrenderValue_Y = "" + 0;
        }
    }

    public String getSurrenderValue_Y() {
        return surrenderValue_Y;
    }


    public void setDeathBenefit_Z(String option, double sumAssured, double sumPremium) {
        double a = 0, temp = 0;
        a = 1.05 * sumPremium;
        if (option.equals("GOLD")) {
            temp = Math.max(sumAssured, Double.parseDouble(getClosingBalance_W()));
//	    	System.out.println("temp if " +temp);
        } else {
            temp = sumAssured + Double.parseDouble(getClosingBalance_W());
//	    	System.out.println("temp else " +temp);
        }
        if (getPolicyInForce_G().equals("Y")) {
//	    		System.out.println("final "+Math.max(a, temp));
            this.deathBenefit_Z = "" + Math.max(a, temp);
        } else {
            this.deathBenefit_Z = "" + 0;
        }
    }

    public String getDeathBenefit_Z() {
        return deathBenefit_Z;
    }


    public void setOpeningBalance_AA(double _closingBalance_AH) {
        if (getPolicyInForce_G().equals("Y")) {
//	        	System.out.println("premium "+Double.parseDouble(getPremium_J()));
//	        System.out.println("topup"+Double.parseDouble(getTopUpPremium_K()));
//	        System.out.println("deduction "+Double.parseDouble(getTotalDeductions_O()));
//	        System.out.println("closing "+_closingBalance_AH);
//	        System.out.println("Without round " + (Double.parseDouble(getPremium_J())+Double.parseDouble(getTopUpPremium_K())-Double.parseDouble(getTotalDeductions_O())+_closingBalance_AH));
            this.openingBalance_AA = cfap.roundUp_Level2(cfap.getStringWithout_E(Double.parseDouble(getPremium_J()) + Double.parseDouble(getTopUpPremium_K()) - Double.parseDouble((getTotalDeductions_O())) + _closingBalance_AH));
        }
//	        this.openingBalance_AA=(Double.parseDouble(getPremium_J())+Double.parseDouble(getTopUpPremium_K())-Double.parseDouble(getTotalDeductions_O())+_closingBalance_AH)+"";}
        else {
            this.openingBalance_AA = "0";
        }
    }

    public String getOpeningBalance_AA() {
        return this.openingBalance_AA;
    }

    public void setRiskPremiumMortality_AB(FlexiSmartPlusBean flexiSmartBean, String[] forBIArray, String option, double sumAssured) {
        double temp = 0, a = 0, max = 0;
        double arrOutput = Double.parseDouble(forBIArray[(flexiSmartBean.getAgeAtEntry() + Integer.parseInt(getYear_F())) - 1]);
        temp = arrOutput / 12;
        if (option.equals("PLATINUM")) {
            a = 0;
        } else {
            a = Double.parseDouble(getOpeningBalance_AA());
        }

        max = Math.max(0, (sumAssured - a));
        if (getPolicyInForce_G().equals("Y")) {
            this.riskPremiumMortality_AB = "" + ((temp * max) / 1000);
        } else {
            this.riskPremiumMortality_AB = "" + 0;
        }
    }

    public String getRiskPremiumMortality_AB() {
        return riskPremiumMortality_AB;
    }

    public void setServiceTax_AC(FlexiSmartPlusBean flexiSmartBean, double serviceTax) {
        if (getPolicyInForce_G().equals("Y")) {
            this.serviceTax_AC = "" + (Double.parseDouble(getRiskPremiumMortality_AB()) * getServiceTax(flexiSmartBean, serviceTax));
        } else {
            this.serviceTax_AC = "" + 0;
        }
    }

    public String getServiceTax_AC() {
        return serviceTax_AC;
    }

    public String getGuarateedAdditionToPolicyAccount_AD() {
        return this.guarateedAdditionToPolicyAccount_AD;
    }

    public void setGuarateedAdditionToPolicyAccount_AD(FlexiSmartPlusBean flexiSmartBean) {
        if (Integer.parseInt(getYear_F()) <= flexiSmartBean.getPolicyTerm()) {
            this.guarateedAdditionToPolicyAccount_AD = cfap.roundUp_Level2(cfap.getStringWithout_E((Double.parseDouble(getOpeningBalance_AA()) - Double.parseDouble(getRiskPremiumMortality_AB()) - Double.parseDouble(getServiceTax_AC())) * (cfap.pow((1 + FS_Prop.guaranteedInterest), (double) 1 / 12) - 1)));
        } else {
            this.guarateedAdditionToPolicyAccount_AD = "0";
        }
    }

    public String getExtraAdditionToPolicyAccount_AE() {
        return this.extraAdditionToPolicyAccount_AE;
    }

    public void setExtraAdditionToPolicyAccount_AE(FlexiSmartPlusBean flexiSmartBean) {
        if (Integer.parseInt(getYear_F()) <= flexiSmartBean.getPolicyTerm()) {
            this.extraAdditionToPolicyAccount_AE = cfap.roundUp_Level2(cfap.getStringWithout_E((Double.parseDouble(getOpeningBalance_AA()) - Double.parseDouble(getRiskPremiumMortality_AB()) - Double.parseDouble(getServiceTax_AC())) * (cfap.pow((1 + (FS_Prop.int2 - FS_Prop.guaranteedInterest)), (double) 1 / 12) - 1)));
        } else {
            this.extraAdditionToPolicyAccount_AE = "0";
        }
    }

    public void setFMC_AF() {
        if (getPolicyInForce_G().equals("Y")) {
            this.FMC_AF = "" + ((Double.parseDouble(getOpeningBalance_AA()) - Double.parseDouble(getRiskPremiumMortality_AB()) - Double.parseDouble(getServiceTax_AC()) + Double.parseDouble(getGuarateedAdditionToPolicyAccount_AD()) + Double.parseDouble(getExtraAdditionToPolicyAccount_AE())) * 0.0075 / 12);
        } else {
            this.FMC_AF = "" + 0;
        }
    }

    public String getFMC_AF() {
        return FMC_AF;
    }

    public void setServiceTaxOnFMC_AG(boolean jkResident, FlexiSmartPlusBean flexiSmartBean, double serviceTax) {
        if (!(jkResident) && getPolicyInForce_G().equals("Y")) {
            this.serviceTaxOnFMC_AG = "" + (Double.parseDouble(getFMC_AF()) * getServiceTax(flexiSmartBean, serviceTax));
        } else {
            this.serviceTaxOnFMC_AG = "" + 0;
        }
    }

    public String getServiceTaxOnFMC_AG() {
        return serviceTaxOnFMC_AG;
    }

    public void setClosingBalance_AH() {
        if (getPolicyInForce_G().equals("Y")) {
            this.closingBalance_AH = "" + (Double.parseDouble(getOpeningBalance_AA()) - Double.parseDouble(getRiskPremiumMortality_AB()) - Double.parseDouble(getServiceTax_AC()) + Double.parseDouble(getGuarateedAdditionToPolicyAccount_AD()) + Double.parseDouble(getExtraAdditionToPolicyAccount_AE()) - Double.parseDouble(getFMC_AF()) - Double.parseDouble(getServiceTaxOnFMC_AG()));
        } else {
            this.closingBalance_AH = "" + 0;
        }
    }

    public String getClosingBalance_AH() {
        return closingBalance_AH;
    }

    public void setSurrenderCharges_AI(double premiumAmt) {
        double min = Math.min(Double.parseDouble(getClosingBalance_AH()), premiumAmt);
        double surrenderCharge = 0;
        {
            surrenderCharge = Double.parseDouble(calSurrRateArr()[Math.min((Integer.parseInt(getYear_F()) - 1), 11)]);
        }
//	    	  {surrenderCharge=Double.parseDouble(calSurrRateArr()[(Integer.parseInt(getYear_F())-1)]);}

        double multi = (min * surrenderCharge);
        double a = Double.parseDouble(calCapArr()[Math.min((Integer.parseInt(getYear_F()) - 1), 11)]);
//	        double a=Double.parseDouble(calCapArr()[(Integer.parseInt(getYear_F())-1)]);

        if (getPolicyInForce_G().equals("Y")) {
            this.surrenderCharges_AI = cfap.roundUp_Level2(cfap.getStringWithout_E((Math.min((multi), a))));
        } else {
            this.surrenderCharges_AI = "" + 0;
        }
    }

    public String getSurrenderCharges_AI() {
        return surrenderCharges_AI;
    }

    public void setSurrenderValue_AJ(FlexiSmartPlusBean flexiSmartBean, double serviceTax) {
        if (getPolicyInForce_G().equals("Y")) {
            this.surrenderValue_AJ = cfap.roundUp_Level2(cfap.getStringWithout_E(Double.parseDouble(getClosingBalance_AH()) - Double.parseDouble(getSurrenderCharges_AI()) * (1 + getServiceTax(flexiSmartBean, serviceTax))));
        } else {
            this.surrenderValue_AJ = "" + 0;
        }
    }

    public String getSurrenderValue_AJ() {
        return surrenderValue_AJ;
    }


    public void setDeathBenefit_AK(String option, double sumAssured, double sumPremium) {
        double a = 0, temp = 0;

        a = 1.05 * sumPremium;
        if (option.equals("GOLD")) {
            temp = Math.max(sumAssured, Double.parseDouble(getClosingBalance_AH()));
        } else {
            temp = sumAssured + Double.parseDouble(getClosingBalance_AH());
        }
        if (getPolicyInForce_G().equals("Y")) {
            this.deathBenefit_AK = "" + Math.max(a, temp);
        } else {
            this.deathBenefit_AK = "" + 0;
        }
    }

    public String getDeathBenefit_AK() {
        return deathBenefit_AK;
    }


    private double getApplicableChargesReductionYield(FlexiSmartPlusBean flexiSmartBean) {
        double applicableCharge = 0, a, additionalAllocation = 0, commission = 0;
        double premium = flexiSmartBean.getPremiumAmount();
        boolean staffDiscount = flexiSmartBean.getIsForStaffOrNot();
        if (Integer.parseInt(getYear_F()) == 1) {
            if (premium * getModalFactor(flexiSmartBean) < 25000) {
                a = 0.09;
            } else {
                a = 0.09;
            }
            if (staffDiscount) {
                additionalAllocation = 0.075;
            } else {
                additionalAllocation = 0;
            }

            applicableCharge = (a - additionalAllocation);
        } else if (Integer.parseInt(getYear_F()) == 2) {
            if (premium * getModalFactor(flexiSmartBean) < 25000) {
                a = 0.065;
            } else {
                a = 0.065;
            }
            if (staffDiscount) {
                additionalAllocation = 0.03;
            } else {
                additionalAllocation = 0;
            }

            applicableCharge = (a - additionalAllocation);
        } else if (Integer.parseInt(getYear_F()) == 3) {
            if (premium * getModalFactor(flexiSmartBean) < 25000) {
                a = 0.065;
            } else {
                a = 0.065;
            }
            if (staffDiscount) {
                additionalAllocation = 0.03;
            } else {
                additionalAllocation = 0;
            }

            applicableCharge = (a - additionalAllocation);
        } else if (Integer.parseInt(getYear_F()) == 4) {
            if (premium * getModalFactor(flexiSmartBean) < 25000) {
                a = 0.065;
            } else {
                a = 0.065;
            }
            if (staffDiscount) {
                additionalAllocation = 0.025;
            } else {
                additionalAllocation = 0;
            }

            applicableCharge = (a - additionalAllocation);
        } else if (Integer.parseInt(getYear_F()) == 5) {
            if (premium * getModalFactor(flexiSmartBean) < 25000) {
                a = 0.06;
            } else {
                a = 0.06;
            }
            if (staffDiscount) {
                additionalAllocation = 0.025;
            } else {
                additionalAllocation = 0;
            }

            applicableCharge = (a - additionalAllocation);
        } else if (Integer.parseInt(getYear_F()) == 6) {
            if (premium * getModalFactor(flexiSmartBean) < 25000) {
                a = 0.05;
            } else {
                a = 0.05;
            }
            if (staffDiscount) {
                additionalAllocation = 0.02;
            } else {
                additionalAllocation = 0;
            }

            applicableCharge = (a - additionalAllocation);
        } else if (Integer.parseInt(getYear_F()) == 7) {
            if (premium * getModalFactor(flexiSmartBean) < 25000) {
                a = 0.05;
            } else {
                a = 0.05;
            }
            if (staffDiscount) {
                additionalAllocation = 0.02;
            } else {
                additionalAllocation = 0;
            }

            applicableCharge = (a - additionalAllocation);
        } else if (Integer.parseInt(getYear_F()) == 8) {
            if (premium * getModalFactor(flexiSmartBean) < 25000) {
                a = 0.05;
            } else {
                a = 0.05;
            }
            if (staffDiscount) {
                additionalAllocation = 0.02;
            } else {
                additionalAllocation = 0;
            }

            applicableCharge = (a - additionalAllocation);
        } else if (Integer.parseInt(getYear_F()) == 9) {
            if (premium * getModalFactor(flexiSmartBean) < 25000) {
                a = 0.05;
            } else {
                a = 0.05;
            }
            if (staffDiscount) {
                additionalAllocation = 0.01;
            } else {
                additionalAllocation = 0;
            }

            applicableCharge = (a - additionalAllocation);
        } else if (Integer.parseInt(getYear_F()) == 10) {
            if (premium * getModalFactor(flexiSmartBean) < 25000) {
                a = 0.05;
            } else {
                a = 0.05;
            }
            if (staffDiscount) {
                additionalAllocation = 0.01;
            } else {
                additionalAllocation = 0;
            }

            applicableCharge = (a - additionalAllocation);
        } else if (Integer.parseInt(getYear_F()) > 10) {

            if (staffDiscount) {
                commission = 0;
            } else {
                if (premium * getModalFactor(flexiSmartBean) < 25000) {
                    commission = 0.01;
                } else {
                    commission = 0.01;
                }
            }

            applicableCharge = (commission);
        }
        return applicableCharge;
    }

    public void setPremiumAllocationChargeReductionYield_L(FlexiSmartPlusBean flexiSmartBean) {
        if (Integer.parseInt(getYear_F()) <= flexiSmartBean.getPolicyTerm()) {
            this.premiumAllocationChargeReductionYield_L = cfap.roundUp_Level2(cfap.getStringWithout_E(getApplicableChargesReductionYield(flexiSmartBean) * Double.parseDouble(getPremium_J()) + Double.parseDouble(getTopUpPremium_K()) * FS_Prop.topUp));
        } else {
            this.premiumAllocationChargeReductionYield_L = "" + 0;
        }
    }

    public String getPremiumAllocationChargeReductionYield_L() {
        return premiumAllocationChargeReductionYield_L;
    }


    public void setServiceTaxRedutionYield_N(FlexiSmartPlusBean flexiSmartBean, double serviceTax) {

        {
            this.serviceTaxReductionYield_N = cfap.roundUp_Level2(cfap.getStringWithout_E((Double.parseDouble(getPremiumAllocationChargeReductionYield_L()) + Double.parseDouble(getPolicyAdministrationCharge_M())) * getServiceTax(flexiSmartBean, serviceTax) * 0));
        }

    }

    public String getServiceTaxReductionYield_N() {
        return serviceTaxReductionYield_N;
    }

    public void setTotalDeductionsReductionYield_O() {
        this.totalDeductionsReductionYield_O = "" + (Double.parseDouble(getPremiumAllocationChargeReductionYield_L()) + Double.parseDouble(getPolicyAdministrationCharge_M()) + Double.parseDouble(getServiceTaxReductionYield_N()));
    }

    public String getTotalDeductionsReductionYield_O() {
        return totalDeductionsReductionYield_O;
    }

    public void setOpeningBalanceReductionYield_P(double _closingBalance_W) {
        if (getPolicyInForce_G().equals("Y")) {
            this.openingBalanceReductionYield_P = cfap.roundUp_Level2(cfap.getStringWithout_E(Double.parseDouble(getPremium_J()) + Double.parseDouble(getTopUpPremium_K()) - Double.parseDouble(getTotalDeductionsReductionYield_O()) + _closingBalance_W));
        } else {
            this.openingBalanceReductionYield_P = "0";
        }
    }

    public String getOpeningBalanceReductionYield_P() {
        return this.openingBalanceReductionYield_P;
    }

    public void setRiskPremiumMortalityReductionYield_Q(FlexiSmartPlusBean flexiSmartBean, String[] forBIArray, String option, double sumAssured) {
        double temp = 0, a = 0, max = 0;
        double arrOutput = Double.parseDouble(forBIArray[(flexiSmartBean.getAgeAtEntry() + Integer.parseInt(getYear_F())) - 1]);
        temp = arrOutput / 12;
        if (option.equals("PLATINUM")) {
            a = 0;
        } else {
            a = Double.parseDouble(getOpeningBalanceReductionYield_P());
        }

        max = Math.max(0, (sumAssured - a));
        if (getPolicyInForce_G().equals("Y")) {
            this.riskPremiumMortalityReductionYield_Q = "" + ((temp * max) / 1000 * 0);
        } else {
            this.riskPremiumMortalityReductionYield_Q = "" + 0;
        }
    }

    public String getRiskPremiumMortalityReductionYield_Q() {
        return riskPremiumMortalityReductionYield_Q;
    }

    public void setServiceTaxReductionYield_R(FlexiSmartPlusBean flexiSmartBean, double serviceTax) {
        if (getPolicyInForce_G().equals("Y")) {
            this.serviceTaxReductionYield_R = "" + (Double.parseDouble(getRiskPremiumMortalityReductionYield_Q()) * getServiceTax(flexiSmartBean, serviceTax) * 0);
        } else {
            this.serviceTaxReductionYield_R = "" + 0;
        }
    }

    public String getServiceTaxReductionYield_R() {
        return serviceTaxReductionYield_R;
    }

    public String getGuarateedAdditionToPolicyAccountReductionYield_S() {
        return this.guarateedAdditionToPolicyAccountReductionYield_S;
    }

    public void setGuarateedAdditionToPolicyAccountReductionYield_S(FlexiSmartPlusBean flexiSmartBean) {
        if (Integer.parseInt(getYear_F()) <= flexiSmartBean.getPolicyTerm()) {
            this.guarateedAdditionToPolicyAccountReductionYield_S = cfap.roundUp_Level2(cfap.getStringWithout_E((Double.parseDouble(getOpeningBalanceReductionYield_P()) - Double.parseDouble(getRiskPremiumMortalityReductionYield_Q()) - Double.parseDouble(getServiceTaxReductionYield_R())) * (cfap.pow((1 + FS_Prop.guaranteedInterest), (double) 1 / 12) - 1)));
        } else {
            this.guarateedAdditionToPolicyAccountReductionYield_S = "0";
        }
    }

    public String getExtraAdditionToPolicyAccountRedutionYield_T() {
        return this.extraAdditionToPolicyAccountReductionYield_T;
    }

    public void setExtraAdditionToPolicyAccountReductionYield_T(FlexiSmartPlusBean flexiSmartBean) {
        if (Integer.parseInt(getYear_F()) <= flexiSmartBean.getPolicyTerm()) {
            this.extraAdditionToPolicyAccountReductionYield_T = cfap.roundUp_Level2(cfap.getStringWithout_E((Double.parseDouble(getOpeningBalanceReductionYield_P()) - Double.parseDouble(getRiskPremiumMortalityReductionYield_Q()) - Double.parseDouble(getServiceTaxReductionYield_R())) * (cfap.pow((1 + (FS_Prop.int1 - FS_Prop.guaranteedInterest)), (double) 1 / 12) - 1)));
        } else {
            this.extraAdditionToPolicyAccountReductionYield_T = "0";
        }
    }

    public void setFMCReductionYield_U() {
        if (getPolicyInForce_G().equals("Y")) {
            this.FMCReductionYield_U = "" + ((Double.parseDouble(getOpeningBalanceReductionYield_P()) - Double.parseDouble(getRiskPremiumMortalityReductionYield_Q()) - Double.parseDouble(getServiceTaxReductionYield_R()) + Double.parseDouble(getGuarateedAdditionToPolicyAccountReductionYield_S()) + Double.parseDouble(getExtraAdditionToPolicyAccountRedutionYield_T())) * 0.0075 / 12);
        } else {
            this.FMCReductionYield_U = "" + 0;
        }
    }

    public String getFMCReductionYield_U() {
        return FMCReductionYield_U;
    }

    public void setServiceTaxOnFMCReductionYield_V(FlexiSmartPlusBean flexiSmartBean, double serviceTax) {
        if (getPolicyInForce_G().equals("Y")) {
            this.serviceTaxOnFMCReductionYield_V = "" + (Double.parseDouble(getFMCReductionYield_U()) * getServiceTax(flexiSmartBean, serviceTax) * 0);
        } else {
            this.serviceTaxOnFMCReductionYield_V = "" + 0;
        }
    }

    public String getServiceTaxOnFMCReductionYield_V() {
        return serviceTaxOnFMCReductionYield_V;
    }

    public void setClosingBalanceReductionYield_W() {
        this.closingBalanceReductionYield_W = cfap.roundUp_Level2(cfap.getStringWithout_E(Double.parseDouble(getOpeningBalanceReductionYield_P()) - Double.parseDouble(getRiskPremiumMortalityReductionYield_Q()) - Double.parseDouble(getServiceTaxReductionYield_R()) + Double.parseDouble(getGuarateedAdditionToPolicyAccountReductionYield_S()) + Double.parseDouble(getExtraAdditionToPolicyAccountRedutionYield_T()) - Double.parseDouble(getFMCReductionYield_U()) - Double.parseDouble(getServiceTaxOnFMCReductionYield_V())));
    }

    public String getClosingBalanceReductionYield_W() {
        return closingBalanceReductionYield_W;
    }

    public void setSurrenderChargesReductionYield_X(double premiumAmt) {
        double min = Math.min(Double.parseDouble(getClosingBalanceReductionYield_W()), premiumAmt);
        double surrenderCharge = 0;
        {
            surrenderCharge = Double.parseDouble(calSurrRateArr()[Math.min((Integer.parseInt(getYear_F()) - 1), 11)]);
        }
//	        {surrenderCharge=Double.parseDouble(calSurrRateArr()[(Integer.parseInt(getYear_F())-1)]);}

        double multi = (min * surrenderCharge);
        double a = Double.parseDouble(calCapArr()[Math.min((Integer.parseInt(getYear_F()) - 1), 11)]);
//	        double a=Double.parseDouble(calCapArr()[(Integer.parseInt(getYear_F())-1)]);
        if (getPolicyInForce_G().equals("Y")) {
            this.surrenderChargesReductionYield_X = cfap.roundUp_Level2(cfap.getStringWithout_E(Math.min((multi), a)));
        } else {
            this.surrenderChargesReductionYield_X = "" + 0;
        }
    }

    public String getSurrenderChargesReductionYield_X() {
        return surrenderChargesReductionYield_X;
    }

    public void setSurrenderValueReductionYield_Y(FlexiSmartPlusBean flexiSmartBean) {
        if (getPolicyInForce_G().equals("Y")) {
            this.surrenderValueReductionYield_Y = cfap.roundUp_Level2(cfap.getStringWithout_E(Double.parseDouble(getClosingBalanceReductionYield_W()) - Double.parseDouble(getSurrenderChargesReductionYield_X())));
        } else {
            this.surrenderValueReductionYield_Y = "" + 0;
        }
    }

    public String getSurrenderValueReductionYield_Y() {
        return surrenderValueReductionYield_Y;
    }

    public void setDeathBenefitReductionYield_Z(String option, double sumAssured) {
        double a = 0, temp = 0;
        sum += Double.parseDouble(getPremium_J());
        a = 1.05 * sum;
        if (option.equals("GOLD")) {
            temp = Math.max(sumAssured, Double.parseDouble(getClosingBalanceReductionYield_W()));
        } else {
            temp = sumAssured + Double.parseDouble(getClosingBalanceReductionYield_W());
        }
        if (getPolicyInForce_G().equals("Y")) {
            this.deathBenefitReductionYield_Z = "" + Math.max(a, temp);
        } else {
            this.deathBenefitReductionYield_Z = "" + 0;
        }
    }

    public String getDeathBenefitReductionYield_Z() {
        return deathBenefitReductionYield_Z;
    }

    public void setOpeningBalanceReductionYield_AA(double _closingBalance_AH) {
        if (getPolicyInForce_G().equals("Y")) {
            this.openingBalanceReductionYield_AA = cfap.roundUp_Level2(cfap.getStringWithout_E(Double.parseDouble(getPremium_J()) + Double.parseDouble(getTopUpPremium_K()) - Double.parseDouble(getTotalDeductionsReductionYield_O()) + _closingBalance_AH));
        } else {
            this.openingBalanceReductionYield_AA = "0";
        }
    }

    public String getOpeningBalanceReductionYield_AA() {
        return this.openingBalanceReductionYield_AA;
    }


    public void setRiskPremiumMortalityReductionYield_AB(FlexiSmartPlusBean flexiSmartBean, String[] forBIArray, String option, double sumAssured) {
        double temp = 0, a = 0, max = 0;
        double arrOutput = Double.parseDouble(forBIArray[(flexiSmartBean.getAgeAtEntry() + Integer.parseInt(getYear_F())) - 1]);
        temp = arrOutput / 12;
        if (option.equals("PLATINUM")) {
            a = 0;
        } else {
            a = Double.parseDouble(getOpeningBalanceReductionYield_AA());
        }

        max = Math.max(0, (sumAssured - a));
        if (getPolicyInForce_G().equals("Y")) {
            this.riskPremiumMortalityReductionYield_AB = "" + ((temp * max) / 1000 * 0);
        } else {
            this.riskPremiumMortalityReductionYield_AB = "" + 0;
        }
    }

    public String getRiskPremiumMortalityReductionYield_AB() {
        return riskPremiumMortalityReductionYield_AB;
    }

    public void setServiceTaxReductionYield_AC(FlexiSmartPlusBean flexiSmartBean, double serviceTax) {
        if (getPolicyInForce_G().equals("Y")) {
            this.serviceTaxReductionYield_AC = "" + (Double.parseDouble(getRiskPremiumMortalityReductionYield_AB()) * getServiceTax(flexiSmartBean, serviceTax) * 0);
        } else {
            this.serviceTaxReductionYield_AC = "" + 0;
        }
    }

    public String getServiceTaxReductionYield_AC() {
        return serviceTaxReductionYield_AC;
    }

    public String getGuarateedAdditionToPolicyAccountReductionYield_AD() {
        return this.guarateedAdditionToPolicyAccountReductionYield_AD;
    }

    public void setGuarateedAdditionToPolicyAccountReductionYield_AD(FlexiSmartPlusBean flexiSmartBean) {
        if (Integer.parseInt(getYear_F()) <= flexiSmartBean.getPolicyTerm()) {
            this.guarateedAdditionToPolicyAccountReductionYield_AD = cfap.roundUp_Level2(cfap.getStringWithout_E((Double.parseDouble(getOpeningBalanceReductionYield_AA()) - Double.parseDouble(getRiskPremiumMortalityReductionYield_AB()) - Double.parseDouble(getServiceTaxReductionYield_AC())) * (cfap.pow((1 + FS_Prop.guaranteedInterest), (double) 1 / 12) - 1)));
        } else {
            this.guarateedAdditionToPolicyAccountReductionYield_AD = "0";
        }
    }

    public String getExtraAdditionToPolicyAccountReductionYield_AE() {
        return this.extraAdditionToPolicyAccountReductionYield_AE;
    }

    public void setExtraAdditionToPolicyAccountReductionYield_AE(FlexiSmartPlusBean flexiSmartBean) {
        if (Integer.parseInt(getYear_F()) <= flexiSmartBean.getPolicyTerm()) {
            this.extraAdditionToPolicyAccountReductionYield_AE = cfap.roundUp_Level2(cfap.getStringWithout_E((Double.parseDouble(getOpeningBalanceReductionYield_AA()) - Double.parseDouble(getRiskPremiumMortalityReductionYield_AB()) - Double.parseDouble(getServiceTaxReductionYield_AC())) * (cfap.pow((1 + (FS_Prop.int2 - FS_Prop.guaranteedInterest)), (double) 1 / 12) - 1)));
        } else {
            this.extraAdditionToPolicyAccountReductionYield_AE = "0";
        }
    }

    public void setFMCReductionYield_AF() {
        if (getPolicyInForce_G().equals("Y")) {
            this.FMCReductionYield_AF = "" + ((Double.parseDouble(getOpeningBalanceReductionYield_AA()) - Double.parseDouble(getRiskPremiumMortalityReductionYield_AB()) - Double.parseDouble(getServiceTaxReductionYield_AC()) + Double.parseDouble(getGuarateedAdditionToPolicyAccountReductionYield_AD()) + Double.parseDouble(getExtraAdditionToPolicyAccountReductionYield_AE())) * 0.0075 / 12);
        } else {
            this.FMCReductionYield_AF = "" + 0;
        }
    }

    public String getFMCReductionYield_AF() {
        return FMCReductionYield_AF;
    }

    public void setServiceTaxOnFMCReductionYield_AG(FlexiSmartPlusBean flexiSmartBean, double serviceTax) {

        {
            this.serviceTaxOnFMCReductionYield_AG = "" + (Double.parseDouble(getFMCReductionYield_AF()) * getServiceTax(flexiSmartBean, serviceTax) * 0);
        }

    }

    public String getServiceTaxOnFMCReductionYield_AG() {
        return serviceTaxOnFMCReductionYield_AG;
    }

    public void setReductionYield_AO(int policyTerm, double _closingBalance_AH) {
        double a, b;
        if ((Integer.parseInt(getMonth_BB())) == (policyTerm * 12)) {

            a = _closingBalance_AH;
        } else {
            a = 0;
        }

        if ((Integer.parseInt(getMonth_BB())) < (policyTerm * 12)) {
            b = -(Double.parseDouble(getPremium_J()));
        } else {
            b = 0;
        }
//	      System.out.println("a_BQ" +a);
        //this.reductionYield_BQ=""+(Double.parseDouble(getPremium_I()) + a);
        this.reductionYield_AO = "" + (b + a);
    }

    public String getReductionYield_AO() {
        return reductionYield_AO;
    }


    public void setClosingBalanceReductionYield_AH() {
        if (getPolicyInForce_G().equals("Y")) {
            this.closingBalanceReductionYield_AH = "" + (Double.parseDouble(getOpeningBalanceReductionYield_AA()) - Double.parseDouble(getRiskPremiumMortalityReductionYield_AB()) - Double.parseDouble(getServiceTaxReductionYield_AC()) + Double.parseDouble(getGuarateedAdditionToPolicyAccountReductionYield_AD()) + Double.parseDouble(getExtraAdditionToPolicyAccountReductionYield_AE()) - Double.parseDouble(getFMCReductionYield_AF()) - Double.parseDouble(getServiceTaxOnFMCReductionYield_AG()));
        } else {
            this.closingBalanceReductionYield_AH = "" + 0;
        }
    }

    public String getClosingBalanceReductionYield_AH() {
        return closingBalanceReductionYield_AH;
    }

    public void setSurrenderChargesReductionYield_AI(double premiumAmt) {
        double min = Math.min(Double.parseDouble(getClosingBalanceReductionYield_AH()), premiumAmt);
        double surrenderCharge = 0;
        {
            surrenderCharge = Double.parseDouble(calSurrRateArr()[Math.min((Integer.parseInt(getYear_F()) - 1), 11)]);
        }
//	    	  {surrenderCharge=Double.parseDouble(calSurrRateArr()[(Integer.parseInt(getYear_F())-1)]);}

        double multi = (min * surrenderCharge);
        double a = Double.parseDouble(calCapArr()[Math.min((Integer.parseInt(getYear_F()) - 1), 11)]);
//	        double a=Double.parseDouble(calCapArr()[(Integer.parseInt(getYear_F())-1)]);

        if (getPolicyInForce_G().equals("Y")) {
            this.surrenderChargesReductionYield_AI = cfap.roundUp_Level2(cfap.getStringWithout_E(Math.min((multi), a)));
        } else {
            this.surrenderChargesReductionYield_AI = "" + 0;
        }
    }

    public String getSurrenderChargesReductionYield_AI() {
        return surrenderChargesReductionYield_AI;
    }

    public void setSurrenderValueReductionYield_AJ(FlexiSmartPlusBean flexiSmartBean) {
        if (getPolicyInForce_G().equals("Y")) {
            this.surrenderValueReductionYield_AJ = cfap.roundUp_Level2(cfap.getStringWithout_E(Double.parseDouble(getClosingBalanceReductionYield_AH()) - Double.parseDouble(getSurrenderChargesReductionYield_AI())));
        } else {
            this.surrenderValueReductionYield_AJ = "" + 0;
        }
    }

    public String getSurrenderValueReductionYield_AJ() {
        return surrenderValueReductionYield_AJ;
    }


    public void setDeathBenefitReductionYield_AK(String option, double sumAssured) {
        double a = 0, temp = 0;
        sum += Double.parseDouble(getPremium_J());
        a = 1.05 * sum;
        if (option.equals("GOLD")) {
            temp = Math.max(sumAssured, Double.parseDouble(getClosingBalanceReductionYield_AH()));
        } else {
            temp = sumAssured + Double.parseDouble(getClosingBalanceReductionYield_AH());
        }
        if (getPolicyInForce_G().equals("Y")) {
            this.deathBenefitReductionYield_AK = "" + Math.max(a, temp);
        } else {
            this.deathBenefitReductionYield_AK = "" + 0;
        }
    }

    public String getDeathBenefitReductionYield_AK() {
        return deathBenefitReductionYield_AK;
    }

    public void setMonth_BB(int monthNumber) {
        this.month_BB = "" + monthNumber;
    }

    public String getMonth_BB() {
        return month_BB;
    }

//	    public void setReductionYield_AO(int policyTerm,double _closingBalance_AH)
//	    {
//	        double a,b;
//	        if((Integer.parseInt(getMonth_BB())) == (policyTerm*12))
//	        {
////	            System.out.println("inside if");
//	            //System.out.println("getFundValueAtEndReductionYield_AS "+getFundValueAtEndReductionYield_AS());
//	            System.out.println("if equals");
//	        	a=Double.parseDouble(getClosingBalanceReductionYield_AH());
//	        }
//	        else
//	        {a=0;}
//
//	        if((Integer.parseInt(getMonth_BB())) < (policyTerm*12))
//	        {System.out.println("if not equals");
//	        	b=-(Double.parseDouble(getPremium_J())); }
//	        else
//	        {b=0;}
////	        System.out.println("a_BQ" +a);
//	        //this.reductionYield_BQ=""+(Double.parseDouble(getPremium_I()) + a);
//	        this.reductionYield_AO=""+(b + a);
//	    }
//	    public String getReductionYield_AO()
//	    {return reductionYield_AO;}

    public void setIRRAnnual_AP(double monthlyIRR) {
//	    	System.out.println("aaaaaaa "+((cfap.pow((1+ans),12)-1)*100));
        this.irrAnnual_AP = "" + ((cfap.pow((1 + monthlyIRR), 12) - 1));
    }

    public String getIRRAnnual_AP() {
        return irrAnnual_AP;
    }

    public void setReductionInYieldMaturityAt(double int2) {
        this.reductionInYieldMaturityAt = "" + ((int2 - Double.parseDouble(getIRRAnnual_AP())) * 100);
    }

    public String getReductionInYieldMaturityAt() {
        return reductionInYieldMaturityAt;
    }

    public double irr(ArrayList<String> values, double guess) {
        int maxIterationCount = 20;
        double absoluteAccuracy = 1E-7;
        double[] arr = new double[values.size()];
        double x0 = guess;
        double x1;

        int i = 0;
//	          System.out.println("inside irr ");
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


    public double getCommission_AF(double annualisedPrem, FlexiSmartPlusBean flexiSmartBean) {

//			System.out.println("getCommistionRate "+getCommistionRate(smartEliteBean));
        return getCommistionRate(flexiSmartBean) * annualisedPrem;


    }

    private double getCommistionRate(FlexiSmartPlusBean flexiSmartBean) {
        int findYear;
        double premium = flexiSmartBean.getPremiumAmount();

        if (Integer.parseInt(getYear_F()) > 10) {
            findYear = 11;
        } else
            findYear = Integer.parseInt(getYear_F());

        if (flexiSmartBean.getIsForStaffOrNot()) {
            return 0;
        } else if (premium * getModalFactor(flexiSmartBean) < 25000) {
            double[] commArr = {0.075, 0.03, 0.03, 0.025, 0.025, 0.02, 0.02, 0.02, 0.01, 0.01, 0.01};
            return commArr[findYear - 1];
        } else {
            double[] commArr1 = {0.075, 0.03, 0.03, 0.025, 0.025, 0.02, 0.02, 0.02, 0.01, 0.01, 0.01};
            return commArr1[findYear - 1];
        }

    }

}
