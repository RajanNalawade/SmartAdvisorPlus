package sbilife.com.pointofsale_bancaagency.products.smartinsurewealthplus;

public class SmartInsureWealthPlusBean {

    //Bean Variable Declaration
    private int ageAtEntry = 0,
            ageOfProposer = 0,
            PF = 0,
            premiumPayingTerm = 0,
            policyTerm_Basic = 0,
            noOfYearsearsElapsedSinceInception = 0;

    private String gender = null,
            planType = null,
            premFreq = null,
            proposer_gender = "";

    private boolean isStaffDisc = false,
            triggerStrategy = true,
            autoAssetAllocationStrategy = false;

    private double sumassured = 0,
            premiumAmount = 0,
            effectivePremium = 0,
            SAMF = 0,
            effectiveTopUpPrem = 0,
            percentToBeInvested_EquityFund = 0,
            percentToBeInvested_EquityOptFund = 0,
            percentToBeInvested_GrowthFund = 0,
            percentToBeInvested_BalancedFund = 0,
            PercentToBeInvested_PureFund = 0,
            PercentToBeInvested_MidCapFund = 0,
            PercentToBeInvested_BondOptimiserFund = 0,
            CorpbondFund = 0,
            moneyMarketFund = 0, serviceTax = 0;

    private boolean isKerlaDisc = false;


    public boolean isKerlaDisc() {
        return isKerlaDisc;
    }

    public void setKerlaDisc(boolean kerlaDisc) {
        isKerlaDisc = kerlaDisc;
    }

    public void setAgeAtEntry(int ageAtEntry) {
        this.ageAtEntry = ageAtEntry;
    }

    public int getAgeAtEntry() {
        return ageAtEntry;
    }

    public void setAgeOfProposer(int ageOfProposer) {
        this.ageOfProposer = ageOfProposer;
    }

    public int getAgeOfProposer() {
        return ageOfProposer;
    }

    public void setPF(int PF) {
        this.PF = PF;
    }

    public int getPF() {
        return PF;
    }

    public void setPremiumPayingTerm(int premiumPayingTerm) {
        this.premiumPayingTerm = premiumPayingTerm;
    }

    public int getPremiumPayingTerm() {
        return premiumPayingTerm;
    }

    public void setPolicyTerm_Basic(int policyTerm_Basic) {
        this.policyTerm_Basic = policyTerm_Basic;
    }

    public int getPolicyTerm_Basic() {
        return policyTerm_Basic;
    }

    public void setNoOfYearsElapsedSinceInception(int noOfYearsearsElapsedSinceInception) {
        this.noOfYearsearsElapsedSinceInception = noOfYearsearsElapsedSinceInception;
    }

    public int getNoOfYearsElapsedSinceInception() {
        return noOfYearsearsElapsedSinceInception;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public String getProposer_gender() {
        return proposer_gender;
    }

    public void setProposer_gender(String proposer_gender) {
        this.proposer_gender = proposer_gender;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPremFreq(String premFreq) {
        this.premFreq = premFreq;
    }

    public String getPremFreq() {
        return premFreq;
    }

    public void setIsForStaffOrNot(boolean isStaffDisc) {
        this.isStaffDisc = isStaffDisc;
    }

    public boolean getIsForStaffOrNot() {
        return isStaffDisc;
    }

    public void setPremiumAmount(double premiumAmount) {
        this.premiumAmount = premiumAmount;
    }

    public double getPremiumAmount() {
        return premiumAmount;
    }

    public void setSumAssured(double sumassured) {
        this.sumassured = sumassured;
    }

    public double getSumAssured() {
        return sumassured;
    }

    public void setEffectivePremium(double effectivePremium) {
        this.effectivePremium = effectivePremium;
    }

    public double getEffectivePremium() {
        return effectivePremium;
    }

    public void setSAMF(double SAMF) {
        this.SAMF = SAMF;
    }

    public double getSAMF() {
        return SAMF;
    }

    public void setTriggerStrategy(boolean triggerStrategy) {
        this.triggerStrategy = triggerStrategy;
    }

    public boolean getTriggerStrategy() {
        return triggerStrategy;
    }

    public void setAutoAssetAllocationStrategy(boolean autoAssetAllocationStrategy) {
        this.autoAssetAllocationStrategy = autoAssetAllocationStrategy;
    }

    public boolean getAutoAssetAllocationStrategy() {
        return autoAssetAllocationStrategy;
    }

    public void setPercentToBeInvested_EquityFund(double percentToBeInvested_EquityFund) {
        this.percentToBeInvested_EquityFund = percentToBeInvested_EquityFund / 100;
    }

    public double getPercentToBeInvested_EquityFund() {
        return percentToBeInvested_EquityFund;
    }

    public void setPercentToBeInvested_EquityOptFund(double percentToBeInvested_EquityOptFund) {
        this.percentToBeInvested_EquityOptFund = percentToBeInvested_EquityOptFund / 100;
    }

    public double getPercentToBeInvested_EquityOptFund() {
        return percentToBeInvested_EquityOptFund;
    }

    public void setPercentToBeInvested_GrowthFund(double percentToBeInvested_GrowthFund) {
        this.percentToBeInvested_GrowthFund = percentToBeInvested_GrowthFund / 100;
    }

    public double getPercentToBeInvested_GrowthFund() {
        return percentToBeInvested_GrowthFund;
    }

    public void setPercentToBeInvested_BalancedFund(double percentToBeInvested_BalancedFund) {
        this.percentToBeInvested_BalancedFund = percentToBeInvested_BalancedFund / 100;
    }

    public double getPercentToBeInvested_BalancedFund() {
        return percentToBeInvested_BalancedFund;
    }

    public void setPercentToBeInvested_PureFund(double PercentToBeInvested_PureFund) {
        this.PercentToBeInvested_PureFund = PercentToBeInvested_PureFund / 100;
    }

    public double getPercentToBeInvested_PureFund() {
        return PercentToBeInvested_PureFund;
    }

    public void setPercentToBeInvested_MidCapFund(double PercentToBeInvested_MidCapFund) {
        this.PercentToBeInvested_MidCapFund = PercentToBeInvested_MidCapFund / 100;
    }

    public double getPercentToBeInvested_MidCapFund() {
        return PercentToBeInvested_MidCapFund;
    }

    public void setPercentToBeInvested_BondOptimiserFund(double PercentToBeInvested_BondOptimiserFund) {
        this.PercentToBeInvested_BondOptimiserFund = PercentToBeInvested_BondOptimiserFund / 100;
    }

    public double getPercentToBeInvested_BondOptimiserFund() {
        return PercentToBeInvested_BondOptimiserFund;
    }

    public void setCorpbondFund(double CorpbondFund) {
        this.CorpbondFund = CorpbondFund / 100;
    }

    public double getCorpbondFund() {
        return CorpbondFund;
    }

    public void setMoneyMarketFund(double moneyMarketFund) {
        this.moneyMarketFund = moneyMarketFund / 100;
    }

    public double getMoneyMarketFund() {
        return moneyMarketFund;
    }


    public double getEffectiveTopUpPrem() {
        return effectiveTopUpPrem;
    }

    public void setEffectiveTopUpPrem(String addTopUp, String premFreq, double topUpPremiumAmt) {
        if (addTopUp.equals("Yes")) {
            if (premFreq.equals("Regular")) {
                if ((topUpPremiumAmt % 100) == 0) {
                    this.effectiveTopUpPrem = topUpPremiumAmt;
                } else {
                    this.effectiveTopUpPrem = topUpPremiumAmt - (topUpPremiumAmt % 100);
                }
            } else {
                if ((topUpPremiumAmt % 100) == 0) {
                    this.effectiveTopUpPrem = topUpPremiumAmt;
                }
                {
                    this.effectiveTopUpPrem = topUpPremiumAmt - (topUpPremiumAmt % 100);
                }
            }
        } else {
            this.effectiveTopUpPrem = 0;
        }
    }

    /*public void setServiceTax(boolean isState)
    {
        if(isState==true)
        {
            serviceTax=0.19;
        }
        else
        {
            serviceTax=0.18;
        }
    }

    public double getServiceTax()
    {
        return this.serviceTax;
    }*/


}
