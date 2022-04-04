package sbilife.com.pointofsale_bancaagency.saralmahaanand;

public class SaralMahaAnandBean {
    //Bean Variable Declaration
    private int ageAtEntry = 0, PF = 0, termADB = 0, policyTerm_Basic = 0, yearsElapsedSinceInception = 0;
    private String gender = null, premFreqMode = null;
    private boolean isStaffDisc = false, isBancAssuranceDisc = false, isADBcheked = false, JKResident = false;
    private double sumAssuredADB = 0, premiumAmount = 0, effectivePremium = 0, SAMF = 0, effectiveTopUpPrem = 0, percentToBeInvested_EquityFund = 0,
            percentToBeInvested_BondFund = 0, percentToBeInvested_BalancedFund = 0, percentToBeInvested_IndexFund = 0, serviceTax = 0;

    private boolean isKerlaDisc = false;

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

    public double getPercentToBeInvested_IndexFund() {
        return percentToBeInvested_IndexFund;
    }

    public void setPercentToBeInvested_IndexFund(
            double percentToBeInvested_IndexFund) {
        this.percentToBeInvested_IndexFund = percentToBeInvested_IndexFund / 100;
    }

    public double getPercentToBeInvested_EquityFund() {
        return percentToBeInvested_EquityFund;
    }

    public void setPercentToBeInvested_EquityFund(double percentToBeInvested_EquityFund) {
        this.percentToBeInvested_EquityFund = percentToBeInvested_EquityFund / 100;
    }

    public double getPercentToBeInvested_BondFund() {
        return percentToBeInvested_BondFund;
    }

    public void setPercentToBeInvested_BondFund(double percentToBeInvested_BondFund) {
        this.percentToBeInvested_BondFund = percentToBeInvested_BondFund / 100;
    }

    public double getPercentToBeInvested_BalancedFund() {
        return percentToBeInvested_BalancedFund;
    }

    public void setPercentToBeInvested_BalancedFund(double percentToBeInvested_BalancedFund) {
        this.percentToBeInvested_BalancedFund = percentToBeInvested_BalancedFund / 100;
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

    public void setEffectiveTopUpPrem(String addTopUp, double topUpPremiumAmt) {
        if (addTopUp.equals("Yes")) {
            if ((topUpPremiumAmt % 500) == 0) {
                this.effectiveTopUpPrem = topUpPremiumAmt;
            } else {
                this.effectiveTopUpPrem = topUpPremiumAmt - (topUpPremiumAmt % 500);
            }
        } else {
            this.effectiveTopUpPrem = 0;
        }
    }

    public boolean isJKResident() {
        return JKResident;
    }

    public void setJKResident(boolean jKResident) {
        JKResident = jKResident;
    }


}

