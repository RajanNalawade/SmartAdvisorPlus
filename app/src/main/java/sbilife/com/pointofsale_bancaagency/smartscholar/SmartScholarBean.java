package sbilife.com.pointofsale_bancaagency.smartscholar;

class SmartScholarBean {
    //Bean Variable Declaration
    private int ageOfChild = 0, ageOfProposer = 0, PF = 0, premiumPayingTerm = 0, policyTerm_Basic = 0, yearsOfDiscontinuance = 0;
    private String gender = null, premFreqMode = null, planType = null;
    private boolean isStaffDisc = false, isBancAssuranceDisc = false;
    private double premiumAmount = 0, effectivePremium = 0, SAMF = 0, effectiveTopUpPrem = 0, percentToBeInvested_EquityFund = 0,
            percentToBeInvested_EquityOptimiserFund = 0, percentToBeInvested_GrowthFund = 0, percentToBeInvested_BalancedFund = 0,
            percentToBeInvested_BondFund = 0, percentToBeInvested_MoneyMarketFund = 1, percentToBeInvested_IndexFund = 0,
            percentToBeInvested_Top300Fund = 0, percentToBeInvested_PEmanagedFund = 0, serviceTax = 0,
            percentToBeInvested_BondOptimiserFund2 = 0, percentToBeInvested_PureFund = 0;

    private boolean isKerlaDisc = false;


    public boolean isKerlaDisc() {
        return isKerlaDisc;
    }

    public void setKerlaDisc(boolean kerlaDisc) {
        isKerlaDisc = kerlaDisc;
    }

    //Bean Getter Setter Methods
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPremFreqMode() {
        return premFreqMode;
    }

    public void setPremFreqMode(String premFreqMode) {
        this.premFreqMode = premFreqMode;
    }

    public int getPolicyTerm_Basic() {
        return policyTerm_Basic;
    }

    public void setPolicyTerm_Basic(int policyTerm_Basic) {
        this.policyTerm_Basic = policyTerm_Basic;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public int getPF() {
        return PF;
    }

    public void setPF(int PF) {
        this.PF = PF;
    }

    public int getYearsOfDiscontinuance() {
        return yearsOfDiscontinuance;
    }

    public void setYearsOfDiscontinuance(int yearsOfDiscontinuance) {
        this.yearsOfDiscontinuance = yearsOfDiscontinuance;
    }

    public boolean getIsForStaffOrNot() {
        return isStaffDisc;
    }

    public void setIsForStaffOrNot(boolean isStaffDisc) {
        this.isStaffDisc = isStaffDisc;
    }

    public boolean getIsBancAssuranceDiscOrNot() {
        return isBancAssuranceDisc;
    }

    public void setIsBancAssuranceDiscOrNot(boolean isBancAssuranceDisc) {
        this.isBancAssuranceDisc = isBancAssuranceDisc;
    }

    public int getAgeOfChild() {
        return ageOfChild;
    }

    public void setAgeOfChild(int ageOfChild) {
        this.ageOfChild = ageOfChild;
    }

    public int getAgeOfProposer() {
        return ageOfProposer;
    }

    public void setAgeOfProposer(int ageOfProposer) {
        this.ageOfProposer = ageOfProposer;
    }

    public int getPremiumPayingTerm() {
        return premiumPayingTerm;
    }

    public void setPremiumPayingTerm(int premiumPayingTerm) {
        this.premiumPayingTerm = premiumPayingTerm;
    }

    public double getSAMF() {
        return SAMF;
    }

    public void setSAMF(double SAMF) {
        this.SAMF = SAMF;
    }

    public double getEffectivePremium() {
        return effectivePremium;
    }

    public void setEffectivePremium(double effectivePremium) {
        this.effectivePremium = effectivePremium;
    }

    public double getPremiumAmount() {
        return premiumAmount;
    }

    public void setPremiumAmount(double premiumAmount) {
        this.premiumAmount = premiumAmount;
    }

    public double getPercentToBeInvested_EquityFund() {
        return percentToBeInvested_EquityFund;
    }

    public void setPercentToBeInvested_EquityFund(double percentToBeInvested_EquityFund) {
        this.percentToBeInvested_EquityFund = percentToBeInvested_EquityFund / 100;
    }

    public double getPercentToBeInvested_EquityOptimiserFund() {
        return percentToBeInvested_EquityOptimiserFund;
    }

    public void setPercentToBeInvested_EquityOptimiserFund(double percentToBeInvested_EquityOptimiserFund) {
        this.percentToBeInvested_EquityOptimiserFund = percentToBeInvested_EquityOptimiserFund / 100;
    }

    public double getPercentToBeInvested_GrowthFund() {
        return percentToBeInvested_GrowthFund;
    }

    public void setPercentToBeInvested_GrowthFund(double percentToBeInvested_GrowthFund) {
        this.percentToBeInvested_GrowthFund = percentToBeInvested_GrowthFund / 100;
    }

    public double getPercentToBeInvested_BalancedFund() {
        return percentToBeInvested_BalancedFund;
    }

    public void setPercentToBeInvested_BalancedFund(double percentToBeInvested_BalancedFund) {
        this.percentToBeInvested_BalancedFund = percentToBeInvested_BalancedFund / 100;
    }

    public double getPercentToBeInvested_BondFund() {
        return percentToBeInvested_BondFund;
    }

    public void setPercentToBeInvested_BondFund(double percentToBeInvested_BondFund) {
        this.percentToBeInvested_BondFund = percentToBeInvested_BondFund / 100;
    }

    public double getPercentToBeInvested_MoneyMarketFund() {
        return percentToBeInvested_MoneyMarketFund;
    }

    public void setPercentToBeInvested_MoneyMarketFund(double percentToBeInvested_MoneyMarketFund) {
        this.percentToBeInvested_MoneyMarketFund = percentToBeInvested_MoneyMarketFund / 100;
    }

    /*public double getPercentToBeInvested_IndexFund()
    {return percentToBeInvested_IndexFund;}
    public void setPercentToBeInvested_IndexFund(double percentToBeInvested_IndexFund)
    {this.percentToBeInvested_IndexFund=percentToBeInvested_IndexFund/100;}*/

    public double getPercentToBeInvested_Top300Fund() {
        return percentToBeInvested_Top300Fund;
    }

    public void setPercentToBeInvested_Top300Fund(double percentToBeInvested_Top300Fund) {
        this.percentToBeInvested_Top300Fund = percentToBeInvested_Top300Fund / 100;
    }

    /* public double getPercentToBeInvested_PEmanagedFund()
     {return percentToBeInvested_PEmanagedFund;}
     public void setPercentToBeInvested_PEmanagedFund(double percentToBeInvested_PEmanagedFund)
     {this.percentToBeInvested_PEmanagedFund=percentToBeInvested_PEmanagedFund/100;}
 */
    public double getEffectiveTopUpPrem() {
        return effectiveTopUpPrem;
    }

    public void setEffectiveTopUpPrem(String addTopUp, String premFreq, double topUpPremiumAmt) {
        if (addTopUp.equals("Yes")) {
            //For Regular Frequency Mode
            if (premFreq.equals("Limited")) {
                if ((topUpPremiumAmt % 100) == 0) {
                    this.effectiveTopUpPrem = topUpPremiumAmt;
                } else {
                    this.effectiveTopUpPrem = topUpPremiumAmt - (topUpPremiumAmt % 100);
                }
            }
            //For Single Frequency Mode
            else {
                if ((topUpPremiumAmt % 100) == 0) {
                    this.effectiveTopUpPrem = topUpPremiumAmt;
                } else {
                    this.effectiveTopUpPrem = topUpPremiumAmt - (topUpPremiumAmt % 100);
                }
            }
        } else {
            this.effectiveTopUpPrem = 0;
        }
    }

   /* public void setServiceTax(boolean isState)
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

    public double getPercentToBeInvested_BondOptimiserFund2() {
        return percentToBeInvested_BondOptimiserFund2;
    }

    public void setPercentToBeInvested_BondOptimiserFund2(double percentToBeInvested_BondOptimiserFund2) {
        this.percentToBeInvested_BondOptimiserFund2 = percentToBeInvested_BondOptimiserFund2 / 100;
    }

    public double getPercentToBeInvested_PureFund() {
        return percentToBeInvested_PureFund;
    }

    public void setPercentToBeInvested_PureFund(double percentToBeInvested_PureFund) {
        this.percentToBeInvested_PureFund = percentToBeInvested_PureFund / 100;
    }
}
