/***************************************************************************************************************
 Author-> Vrushali Chaudhari
 Class Description-> Setter getter methods for Retire Smart products are defined here
 ***************************************************************************************************************/

package sbilife.com.pointofsale_bancaagency.retiresmart;

class RetireSmartBean {

    private boolean isStaffDisc = false;
    private int ageAtEntry = 0, policyTerm = 0, premiumPayingTerm = 0, noOfYearsElapsedSinceInception = 0, PF = 0, vestingAge = 0;
    private String premFrequencyMode = null, premPayingOption = null, gender = null, planOption = null, annuityOption = null;
    private double premiumAmount = 0, effectiveTopUpPrem = 0, serviceTax = 0;

    private boolean isKerlaDisc = false;
    private double
            percentToBeInvested_EquityPensionFund = 0,
            percentToBeInvested_EquityOptPensionFund = 0,
            percentToBeInvested_GrowthPensionFund = 0,
            percentToBeInvested_BondPensionFund = 0,
            percentToBeInvested_MoneyMarketPensionFund = 0;
    String bi_retire_smart_plan_option = "";


    public boolean isKerlaDisc() {
        return isKerlaDisc;
    }

    public void setKerlaDisc(boolean kerlaDisc) {
        isKerlaDisc = kerlaDisc;
    }

    public void setStaffDisc(boolean isStaffDisc) {
        this.isStaffDisc = isStaffDisc;
    }

    public boolean getStaffDisc() {
        return isStaffDisc;
    }

    public void setAge(int ageAtEntry) {
        this.ageAtEntry = ageAtEntry;
    }

    public int getAge() {
        return ageAtEntry;
    }

    public void setPolicyTerm(int policyTerm) {
        this.policyTerm = policyTerm;
    }

    public int getPolicyTerm() {
        return policyTerm;
    }

    public void setPremiumPayingTerm(int premiumPayingTerm) {
        this.premiumPayingTerm = premiumPayingTerm;
    }

    public int getPremiumPayingTerm() {
        return premiumPayingTerm;
    }

    public void setNoOfYearsElapsedSinceInception(
            int noOfYearsElapsedSinceInception) {
        this.noOfYearsElapsedSinceInception = noOfYearsElapsedSinceInception;
    }

    public int getNoOfYearsElapsedSinceInception() {
        return noOfYearsElapsedSinceInception;
    }

    public void setPremFrequencyMode(String premFrequencyMode) {
        this.premFrequencyMode = premFrequencyMode;
    }

    public String getPremFrequencyMode() {
        return premFrequencyMode;
    }

    public void setPremPayingOption(String premPayingOption) {
        this.premPayingOption = premPayingOption;
    }

    public String getPremPayingOption() {
        return premPayingOption;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setPremiumAmount(double premiumAmount) {
        this.premiumAmount = premiumAmount;
    }

    public double getPremiumAmount() {
        return premiumAmount;
    }

    public void setPF(int PF) {
        this.PF = PF;
    }

    public int getPF() {
        return PF;
    }

    public String getAnnuityOption() {
        return annuityOption;
    }

    public void setAnnuityOption(String annuityOption) {
        this.annuityOption = annuityOption;
    }

    public void setEffectiveTopUpPrem(String addTopUp, double topUpPremiumAmt) {
        if (addTopUp.equals("Yes")) {
            if ((topUpPremiumAmt % 500) == 0) {
                this.effectiveTopUpPrem = topUpPremiumAmt;
            } else {
                this.effectiveTopUpPrem = topUpPremiumAmt
                        - (topUpPremiumAmt % 500);
            }
        } else {
            this.effectiveTopUpPrem = 0;
        }
    }

    public double getEffectiveTopUpPrem() {
        return effectiveTopUpPrem;
    }
	/*public void setServiceTax(boolean isState)
	{
        if (isState == true) {
            serviceTax = 0.19;
        } else {
            serviceTax = 0.18;
        }
    }

    public double getServiceTax() {
        return this.serviceTax;
	}*/

//  Added By Saurabh Jain on 10/06/2019 End

    public void setPlanOption(String planOption) {
        this.planOption = planOption;
    }

    public String getPlanOption() {
        return planOption;
    }

    public void setPercentToBeInvested_EquityPensionFund(double percentToBeInvested_EquityPensionFund) {
        this.percentToBeInvested_EquityPensionFund = percentToBeInvested_EquityPensionFund / 100;
    }

    public double getPercentToBeInvested_EquityPensionFund() {
        return percentToBeInvested_EquityPensionFund;
    }

    public void setPercentToBeInvested_EquityOptPensionFund(double percentToBeInvested_EquityOptPensionFund) {
        this.percentToBeInvested_EquityOptPensionFund = percentToBeInvested_EquityOptPensionFund / 100;
    }

    public double getPercentToBeInvested_EquityOptPensionFund() {
        return percentToBeInvested_EquityOptPensionFund;
    }

    public void setPercentToBeInvested_GrowthPensionFund(double percentToBeInvested_GrowthPensionFund) {
        this.percentToBeInvested_GrowthPensionFund = percentToBeInvested_GrowthPensionFund / 100;
    }

    public double getPercentToBeInvested_GrowthPensionFund() {
        return percentToBeInvested_GrowthPensionFund;
    }

    public void setPercentToBeInvested_BondPensionFund(double percentToBeInvested_BondPensionFund) {
        this.percentToBeInvested_BondPensionFund = percentToBeInvested_BondPensionFund / 100;
    }

    public double getPercentToBeInvested_BondPensionFund() {
        return percentToBeInvested_BondPensionFund;
    }

    public void setPercentToBeInvested_MoneyMarketPensionFund(double percentToBeInvested_MoneyMarketPensionFund) {
        this.percentToBeInvested_MoneyMarketPensionFund = percentToBeInvested_MoneyMarketPensionFund / 100;
    }

    public double getPercentToBeInvested_MoneyMarketPensionFund() {
        return percentToBeInvested_MoneyMarketPensionFund;
    }

    public String getBi_retire_smart_plan_option() {
        return bi_retire_smart_plan_option;
    }

    public void setBi_retire_smart_plan_option(String bi_retire_smart_plan_option) {
        this.bi_retire_smart_plan_option = bi_retire_smart_plan_option;
    }

    public void setVestingAge(int vestingAge) {
        this.vestingAge = vestingAge;
    }

    public int getVestingAge() {
        return vestingAge;
    }
}
