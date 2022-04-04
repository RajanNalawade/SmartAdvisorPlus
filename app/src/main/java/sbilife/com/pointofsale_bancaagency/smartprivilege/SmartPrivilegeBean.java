package sbilife.com.pointofsale_bancaagency.smartprivilege;

class SmartPrivilegeBean {
    // Bean Variable Declaration
    private int ageAtEntry = 0, ageOfProposer = 0, PF = 0,
            premiumPayingTerm = 0, policyTerm_Basic = 0,
            noOfYearsearsElapsedSinceInception = 0;

    private String gender = null, planType = null, premFreq = null;

    private boolean isStaffDisc = false;

    private double premiumAmount = 0, annualPremium = 0, SAMF = 0,
            effectiveTopUpPrem = 0, percentToBeInvested_EquityFund = 0,
            percentToBeInvested_EquityOptFund = 0,
            percentToBeInvested_GrowthFund = 0,
            percentToBeInvested_BalancedFund = 0,
            percentToBeInvested_BondFund = 0,
            percentToBeInvested_MoneyMarketFund = 0,
            percentToBeInvested_Top300Fund = 0,
            percentToBeInvested_PureFund = 0, percentToBeInvested_MidcapFund = 0,
            percentToBeInvested_bondOptimiserFund2 = 0, percentToBeInvested_moneyMarketFund2 = 0, percentToBeInvested_CorpBondFund = 0, serviceTax = 0;


    private boolean isKerlaDisc = false;


    public boolean isKerlaDisc() {
        return isKerlaDisc;
    }

    public void setKerlaDisc(boolean kerlaDisc) {
        isKerlaDisc = kerlaDisc;
    }

    /*public void setServiceTax(boolean isState)
    {
        if (isState == true) {
            serviceTax = 0.1;
        } else {
            serviceTax = 0.09;
        }
    }

    public double getServiceTax() {
        return this.serviceTax;
    }*/
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

    public void setNoOfYearsElapsedSinceInception(
            int noOfYearsearsElapsedSinceInception) {
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

    public void setAnnualPremium(double annualPremium) {
        this.annualPremium = annualPremium;
    }

    public double getAnnualPremium() {
        return annualPremium;
    }

    public void setSAMF(double SAMF) {
        this.SAMF = SAMF;
    }

    public double getSAMF() {
        return SAMF;
    }

    public void setPercentToBeInvested_EquityFund(
            double percentToBeInvested_EquityFund) {
        this.percentToBeInvested_EquityFund = percentToBeInvested_EquityFund / 100;
    }

    public double getPercentToBeInvested_EquityFund() {
        return percentToBeInvested_EquityFund;
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

    public double getPercentToBeInvested_BalancedFund() {
        return percentToBeInvested_BalancedFund;
    }

    public void setPercentToBeInvested_BondFund(
            double percentToBeInvested_BondFund) {
        this.percentToBeInvested_BondFund = percentToBeInvested_BondFund / 100;
    }

    public double getPercentToBeInvested_BondFund() {
        return percentToBeInvested_BondFund;
    }

    public void setPercentToBeInvested_MidcapFund(
            double percentToBeInvested_MidcapFund) {
        this.percentToBeInvested_MidcapFund = percentToBeInvested_MidcapFund / 100;
    }

    public double getPercentToBeInvested_MidcapFund() {
        return percentToBeInvested_MidcapFund;
    }

    public void setPercentToBeInvested_CorpBondFund(double perInvCorpBondFund) {
        this.percentToBeInvested_CorpBondFund = perInvCorpBondFund / 100;
    }

    public double getPercentToBeInvested_CorpBondFund() {
        return percentToBeInvested_CorpBondFund;
    }

    public void setPercentToBeInvested_PureFund(double percentToBeInvested_PureFund) {
        this.percentToBeInvested_PureFund = percentToBeInvested_PureFund / 100;
    }

    public double getPercentToBeInvested_PureFund() {
        return percentToBeInvested_PureFund;
    }

    public void setPercentToBeInvested_Top300Fund(
            double percentToBeInvested_Top300Fund) {
        this.percentToBeInvested_Top300Fund = percentToBeInvested_Top300Fund / 100;
    }

    public double getPercentToBeInvested_Top300Fund() {
        return percentToBeInvested_Top300Fund;
    }

    public double getPercentToBeInvested_bondOptimiserFund2() {
        return percentToBeInvested_bondOptimiserFund2;
    }

    public void setPercentToBeInvested_bondOptimiserFund2(double percentToBeInvested_bondOptimiserFund2) {
        this.percentToBeInvested_bondOptimiserFund2 = percentToBeInvested_bondOptimiserFund2 / 100;
    }

    public double getPercentToBeInvested_moneyMarketFund2() {
        return percentToBeInvested_moneyMarketFund2;
    }

    public void setPercentToBeInvested_moneyMarketFund2(double percentToBeInvested_moneyMarketFund2) {
        this.percentToBeInvested_moneyMarketFund2 = percentToBeInvested_moneyMarketFund2 / 100;
    }

    public double getEffectiveTopUpPrem() {
        return effectiveTopUpPrem;
    }

    public void setEffectiveTopUpPrem(String addTopUp, String premFreq,
                                      double topUpPremiumAmt) {
        if (addTopUp.equals("Yes")) {
            if (premFreq.equals("Regular")) {
                if ((topUpPremiumAmt % 100) == 0) {
                    this.effectiveTopUpPrem = topUpPremiumAmt;
                } else {
                    this.effectiveTopUpPrem = topUpPremiumAmt
                            - (topUpPremiumAmt % 100);
                }
            } else {
                if ((topUpPremiumAmt % 100) == 0) {
                    this.effectiveTopUpPrem = topUpPremiumAmt;
                }
                {
                    this.effectiveTopUpPrem = topUpPremiumAmt
                            - (topUpPremiumAmt % 100);
                }
            }
        } else {
            this.effectiveTopUpPrem = 0;
        }
    }
}
