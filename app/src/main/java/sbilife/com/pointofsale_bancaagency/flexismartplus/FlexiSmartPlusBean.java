package sbilife.com.pointofsale_bancaagency.flexismartplus;

import android.util.Log;

class FlexiSmartPlusBean {
    //Bean Variable Declaration
    private int PF = 0, ageAtEntry = 0, policyTerm = 0, policyYear = 0, premHolidayTerm = 0;
    private String option = null, gender = null, premFreqMode = null, premiumHolidayStatus = null, topUpStatus = null, inforceStatus = null;
    private boolean isStaffDisc = false, isBancAssuranceDisc = false, JkResident = false;
    private double sumAssured = 0, effectivePremium = 0, premiumAmount = 0, SAMF = 0, topUpPremAmt = 0, effectiveTopUpPrem = 0, serviceTax = 0;

    private boolean isKerlaDisc = false;


    public boolean isKerlaDisc() {
        return isKerlaDisc;
    }

    public void setKerlaDisc(boolean kerlaDisc) {
        isKerlaDisc = kerlaDisc;
    }

    public void setServiceTax(boolean isState) {
        if (isState == true) {
            serviceTax = 0.19;
        } else {
            serviceTax = 0.18;
        }
    }

    public double getServiceTax() {
        return this.serviceTax;
    }


    /**
     * J&K residend is added as per 1st jan 2014,by Vrushali Chaudhari
     *
     * @return
     */
    public void setJkResident(boolean JkResident) {
        this.JkResident = JkResident;
    }

    public boolean getJkResident() {
        return JkResident;
    }

    //Bean Getter Setter Methods
    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTopUpStatus() {
        return topUpStatus;
    }

    public void setTopUpStatus(String topUpStatus) {
        this.topUpStatus = topUpStatus;
    }

    public String getInforceStatus() {
        return inforceStatus;
    }

    public void setInforceStatus(String inforceStatus) {
        this.inforceStatus = inforceStatus;
    }

    public String getPremFreqMode() {
        return premFreqMode;
    }

    public void setPremFreqMode(String premFreqMode) {
        this.premFreqMode = premFreqMode;
    }

    public String getPremiumHolidayStatus() {
        return premiumHolidayStatus;
    }

    public void setPremiumHolidayStatus(String premiumHolidayStatus) {
        this.premiumHolidayStatus = premiumHolidayStatus;
    }

    public int getPolicyTerm() {
        return policyTerm;
    }

    public void setPolicyTerm(int policyTerm) {
        this.policyTerm = policyTerm;
    }

    public int getPremHolidayTerm() {
        return premHolidayTerm;
    }

    public void setPremHolidayTerm(int premHolidayTerm) {
        this.premHolidayTerm = premHolidayTerm;
    }

    public int getPolicyYear() {
        return policyYear;
    }

    public void setPolicyYear(int policyYear) {
        this.policyYear = policyYear;
    }

    public int getPF() {
        return PF;
    }

    public void setPF(int PF) {
        this.PF = PF;
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

    public int getAgeAtEntry() {
        return ageAtEntry;
    }

    public void setAgeAtEntry(int ageAtEntry) {
        this.ageAtEntry = ageAtEntry;
    }

    public double getSAMF() {
        return SAMF;
    }

    public void setSAMF(double SAMF) {
        this.SAMF = SAMF;
    }

    public double getTopUpPremAmt() {
        return topUpPremAmt;
    }

    public void setTopUpPremAmt(double topUpPremAmt) {
        this.topUpPremAmt = topUpPremAmt;
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

    public double getSumAssured() {
        return sumAssured;
    }

    public void setSumAssured() {
        Log.d("check", "effective premium" + getEffectivePremium());
        this.sumAssured = getEffectivePremium() * getSAMF() * getPF();
    }

    public double getEffectiveTopUpPrem() {
        return effectiveTopUpPrem;
    }

    public void setEffectiveTopUpPrem(double topUpPremAmt, double premiumAmount) {
        if ((topUpPremAmt % 100) == 0) {
            this.effectiveTopUpPrem = topUpPremAmt;
        } else {
            this.effectiveTopUpPrem = (topUpPremAmt - (premiumAmount % 100));
        }
    }
}
