package sbilife.com.pointofsale_bancaagency.smartwealthassure;

class SmartWealthAssureBean {
    //Bean Variable Declaration
    private int ageAtEntry = 0,
            PF = 1,
            premiumPayingTerm = 0,
            termADB = 0,
            policyTerm_Basic = 0,
            yearsElapsedSinceInception = 0;

    private String gender = null,
            premFreqMode = null;

    private boolean isStaffDisc = false,
            isJkResident = false,
            isBancAssuranceDisc = false,
            isADBcheked = false;

    private double sumAssuredADB = 0,
            premiumAmount = 0,
            effectivePremium = 0,
            effectiveTopUpPrem = 0,
            SAMF = 0,
            percentToBeInvested_GuaranteeFund = 0,
            percentToBeInvested_BondFund = 0,
            percentToBeInvested_EquityFund = 0,
            percentToBeInvested_PEmanagedFund = 0, serviceTax = 0, percentToBeInvested_BalancedFund = 0,
            percentToBeInvested_MoneyMarketFund = 0,
            percentToBeInvested_BondOptimiserFund = 0,
            percentToBeInvested_PureFund = 0,
            percentToBeInvested_CorpBondFund = 0;

    private boolean isKerlaDisc = false;
   /* public void setServiceTax(boolean isState)
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

    public int getTermADB() {
        return termADB;
    }

    public void setTermADB(int termADB) {
        this.termADB = termADB;
    }

    public int getPF() {
        return PF;
    }

    public void setPF(int PF) {
        this.PF = PF;
    }

    public int getYearsElapsedSinceInception() {
        return yearsElapsedSinceInception;
    }

    public void setYearsElapsedSinceInception(int yearsElapsedSinceInception) {
        this.yearsElapsedSinceInception = yearsElapsedSinceInception;
    }

    public boolean getIsForStaffOrNot() {
        return isStaffDisc;
    }

    public void setIsForStaffOrNot(boolean isStaffDisc) {
        this.isStaffDisc = isStaffDisc;
    }

    public boolean isJkResident() {
        return isJkResident;
    }

    public void setJkResident(boolean isJkResident) {
        this.isJkResident = isJkResident;
    }

    public boolean getIsBancAssuranceDiscOrNot() {
        return isBancAssuranceDisc;
    }

    public void setIsBancAssuranceDiscOrNot(boolean isBancAssuranceDisc) {
        this.isBancAssuranceDisc = isBancAssuranceDisc;
    }

    public boolean getIsADBchecked() {
        return isADBcheked;
    }

    public void setIsADBchecked(boolean isADBcheked) {
        this.isADBcheked = isADBcheked;
    }

    public int getAgeAtEntry() {
        return ageAtEntry;
    }

    public void setAgeAtEntry(int ageAtEntry) {
        this.ageAtEntry = ageAtEntry;
    }

    public int getPremiumPayingTerm() {
        return premiumPayingTerm;
    }

    public void setPremiumPayingTerm(int premiumPayingTerm) {
        this.premiumPayingTerm = premiumPayingTerm;
    }

    public double getPercentToBeInvested_GuaranteeFund() {
        return percentToBeInvested_GuaranteeFund;
    }

    public void setPercentToBeInvested_GuaranteeFund(double percentToBeInvested_GuaranteeFund) {
        this.percentToBeInvested_GuaranteeFund = percentToBeInvested_GuaranteeFund / 100;
    }

    public double getPercentToBeInvested_BondFund() {
        return percentToBeInvested_BondFund;
    }

    public void setPercentToBeInvested_BondFund(double percentToBeInvested_BondFund) {
        this.percentToBeInvested_BondFund = percentToBeInvested_BondFund / 100;
    }

    public void setPercentToBeInvested_BalancedFund(double percentToBeInvested_BalancedFund) {
        this.percentToBeInvested_BalancedFund = percentToBeInvested_BalancedFund / 100;
    }

    public double getPercentToBeInvested_BalancedFund() {
        return percentToBeInvested_BalancedFund;
    }

    public double getPercentToBeInvested_EquityFund() {
        return percentToBeInvested_EquityFund;
    }

    public void setPercentToBeInvested_EquityFund(double percentToBeInvested_EquityFund) {
        this.percentToBeInvested_EquityFund = percentToBeInvested_EquityFund / 100;
    }

    public void setPercentToBeInvested_MoneyMarketFund(double percentToBeInvested_MoneyMarketFund) {
        this.percentToBeInvested_MoneyMarketFund = percentToBeInvested_MoneyMarketFund / 100;
    }

    public double getPercentToBeInvested_MoneyMarketFund() {
        return percentToBeInvested_MoneyMarketFund;
    }

    public double getPercentToBeInvested_PEmanagedFund() {
        return percentToBeInvested_PEmanagedFund;
    }

    public void setPercentToBeInvested_PEmanagedFund(double percentToBeInvested_PEmanagedFund) {
        this.percentToBeInvested_PEmanagedFund = percentToBeInvested_PEmanagedFund / 100;
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

    public double getSumAssuredADB() {
        return sumAssuredADB;
    }

    public void setSumAssuredADB(double sumAssuredADB) {
        this.sumAssuredADB = sumAssuredADB;
    }

    public double getEffectiveTopUpPrem() {
        return effectiveTopUpPrem;
    }

    public void setEffectiveTopUpPrem(String addTopUp, String premFreqMode, double topUpPremiumAmt) {
        if (addTopUp.equals("Yes")) {
            //For Regular Frequency Mode
            if (premFreqMode.equals("Limited")) {
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
}
