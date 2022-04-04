package sbilife.com.pointofsale_bancaagency.smartpower;

class SmartPowerInsuranceBean {

    // bean variable declaration
    private int ageAtEntry = 0, policy_Term = 0,
            noOfYearsElapsedSinceInception = 0, PF = 0;

    private String gender = null, fundOption = null, premFreqMode = null,
            increasingCoverOption = null;

    private double premiumAmt = 0, SAMF = 0, effectivePremium = 0,
            percentToBeInvested_EquityFund = 0,
            percentToBeInvested_BondFund = 0,
            percentToBeInvested_Top300Fund = 0,
            percentToBeInvested_EquityOptFund = 0,
            percentToBeInvested_GrowthFund = 0,
            percentToBeInvested_BalancedFund = 0,
            percentToBeInvested_MoneyMarketFund = 0,
            effectiveTopUpPrem = 0, serviceTax = 0,
            percentToBeInvested_BondOptimiserFund = 0,
            percentToBeInvested_PureFund = 0,
            percentToBeInvested_CorpBondFund = 0;

    private boolean isStaffDisc = false, isJKResident = false;

    private boolean isKerlaDisc = false;

    /*public void setServiceTax(boolean isState)
    {
        if(isState==true)
        {
            serviceTax=0.1;
        }
        else
        {
            serviceTax=0.09;
        }
    }

    public double getServiceTax()
    {
        return this.serviceTax;
    }*/


    public boolean isKerlaDisc() {
        return isKerlaDisc;
    }

    public void setKerlaDisc(boolean kerlaDisc) {
        isKerlaDisc = kerlaDisc;
    }


    public boolean isJKResident() {
        return isJKResident;
    }

    public void setJKResident(boolean isJKResident) {
        this.isJKResident = isJKResident;
    }

    // bean getter setter method
    public void setAge(int ageAtEntry) {
        this.ageAtEntry = ageAtEntry;
    }

    public int getAge() {
        return ageAtEntry;
    }

    public void setPolicy_Term(int policy_Term) {
        this.policy_Term = policy_Term;
    }

    public int getPolicy_Term() {
        return policy_Term;
    }

    public void setNoOfYearsElapsedSinceInception(
            int noOfYearsElapsedSinceInception) {
        this.noOfYearsElapsedSinceInception = noOfYearsElapsedSinceInception;
    }

    public int getNoOfYearsElapsedSinceInception() {
        return noOfYearsElapsedSinceInception;
    }

    public void setPF(int PF) {
        this.PF = PF;
    }

    public int getPF() {
        return PF;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setFundOption(String fundOption) {
        this.fundOption = fundOption;
    }

    public String getFundOption() {
        return fundOption;
    }

    public void setPremFreqMode(String premFreqMode) {
        this.premFreqMode = premFreqMode;
    }

    public String getPremFreqMode() {
        return premFreqMode;
    }

    public void setIncreasingCoverOption(String increasingCoverOption) {
        this.increasingCoverOption = increasingCoverOption;
    }

    public String getIncreasingCoverOption() {
        return increasingCoverOption;
    }

    public void setPremiumAmt(double premiumAmt) {
        this.premiumAmt = premiumAmt;
    }

    public double getPremiumAmt() {
        return premiumAmt;
    }

    public void setSAMF(double SAMF) {
        this.SAMF = SAMF;
    }

    public double getSAMF() {
        return SAMF;
    }

    public double getEffectivePremium() {
        return effectivePremium;
    }

    public void setEffectivePremium(double effectivePremium) {
        this.effectivePremium = effectivePremium;
    }

    public void setPercentToBeInvested_EquityFund(
            double percentToBeInvested_EquityFund) {
        this.percentToBeInvested_EquityFund = percentToBeInvested_EquityFund / 100;
    }

    public double getPercentToBeInvested_EquityFund() {
        return percentToBeInvested_EquityFund;
    }

    public void setPercentToBeInvested_BondFund(
            double percentToBeInvested_BondFund) {
        this.percentToBeInvested_BondFund = percentToBeInvested_BondFund / 100;
    }

    public double getPercentToBeInvested_BondFund() {
        return percentToBeInvested_BondFund;
    }

    public void setPercentToBeInvested_Top300Fund(
            double percentToBeInvested_Top300Fund) {
        this.percentToBeInvested_Top300Fund = percentToBeInvested_Top300Fund / 100;
    }

    public double getPercentToBeInvested_Top300Fund() {
        return percentToBeInvested_Top300Fund;
    }

    public void setPercentToBeInvested_EquityOptFund(
            double percentToBeInvested_EquityOptFund) {
        this.percentToBeInvested_EquityOptFund = percentToBeInvested_EquityOptFund / 100;
    }

    public double getPercentToBeInvested_EquityOptFund() {
        return percentToBeInvested_EquityOptFund;
    }

    public void setPercentToBeInvested_GrowthFund(
            double percentToBeInvested_GrowthFund) {
        this.percentToBeInvested_GrowthFund = percentToBeInvested_GrowthFund / 100;
    }

    public double getPercentToBeInvested_GrowthFund() {
        return percentToBeInvested_GrowthFund;
    }

    public void setPercentToBeInvested_BalancedFund(
            double percentToBeInvested_BalancedFund) {
        this.percentToBeInvested_BalancedFund = percentToBeInvested_BalancedFund / 100;
    }

    public double getPercentToBeInvested_Balanced() {
        return percentToBeInvested_BalancedFund;
    }

    public void setPercentToBeInvested_MoneyMarketFund(
            double percentToBeInvested_MoneyMarketFund) {
        this.percentToBeInvested_MoneyMarketFund = percentToBeInvested_MoneyMarketFund / 100;
    }

    public double getPercentToBeInvested_MoneyMarketFund() {
        return percentToBeInvested_MoneyMarketFund;
    }

    public void setIsStaffDiscOrNot(boolean isStaffDisc) {
        this.isStaffDisc = isStaffDisc;
    }

    public boolean getIsStaffDiscOrNot() {
        return isStaffDisc;
    }

    public void setPercentToBeInvested_BondOptimiserFund(double percentToBeInvested_BondOptimiserFund) {
        this.percentToBeInvested_BondOptimiserFund = percentToBeInvested_BondOptimiserFund / 100;
    }

    public double getPercentToBeInvested_BondOptimiserFund() {
        return percentToBeInvested_BondOptimiserFund;
    }

    public void setPercentToBeInvested_PureFund(double percentToBeInvested_PureFund) {
        this.percentToBeInvested_PureFund = percentToBeInvested_PureFund / 100;
    }

    public double getPercentToBeInvested_PureFund() {
        return percentToBeInvested_PureFund;
    }

    public void setPercentToBeInvested_CorpBondFund(double perInvCorpBondFund) {
        this.percentToBeInvested_CorpBondFund = perInvCorpBondFund / 100;
    }

    public double getPercentToBeInvested_CorpBondFund() {
        return percentToBeInvested_CorpBondFund;
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

}
