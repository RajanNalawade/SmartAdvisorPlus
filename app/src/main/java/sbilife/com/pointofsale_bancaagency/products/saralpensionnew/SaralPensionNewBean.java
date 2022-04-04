package sbilife.com.pointofsale_bancaagency.products.saralpensionnew;

import java.util.Date;

public class SaralPensionNewBean {

    //****************************
    private boolean isStaff = false, isJKresident = false, isADB = false, isAdvanceAnnuityPayout = false;
    private String applicableFor = "", sourceOfBusiness = "", channelDetails = "", modeOfAnnuityPayout = "", annuityOption = "",
            genderOfFirstAnnuitant = "", genderOfSecondAnnuitant = "", optFor = "", firstNameOfSecondAnnuitant = "", middleNameOfSecondAnnuitant = "",
            lastNameOfSecondAnnuitant = "", dobOfSecondAnnuitant = "";
    private Date proposalDate = null, annuityPayoutDate = null;
    private int ageOfFirstAnnuitant = 0, ageOfSecondAnnuitant = 0;
    private double additionalAmountIfAny = 0, vestingAmount = 0, annuityAmount = 0, serviceTax = 0;
    private String str_annuity_plus_purchase_annuity_for = "", str_annuity_plus_immediate_annuity_plan_for = "";


    //Bean Getter Setter Methods
    private boolean isKerlaDisc = false;


    public boolean isKerlaDisc() {
        return isKerlaDisc;
    }

    public void setKerlaDisc(boolean kerlaDisc) {
        isKerlaDisc = kerlaDisc;
    }

    public String getStr_annuity_plus_purchase_annuity_for() {
        return str_annuity_plus_purchase_annuity_for;
    }

    public void setStr_annuity_plus_purchase_annuity_for(String str_annuity_plus_purchase_annuity_for) {
        this.str_annuity_plus_purchase_annuity_for = str_annuity_plus_purchase_annuity_for;
    }

    public String getStr_annuity_plus_immediate_annuity_plan_for() {
        return str_annuity_plus_immediate_annuity_plan_for;
    }

    public void setStr_annuity_plus_immediate_annuity_plan_for(String str_annuity_plus_immediate_annuity_plan_for) {
        this.str_annuity_plus_immediate_annuity_plan_for = str_annuity_plus_immediate_annuity_plan_for;
    }

    public String getSourceOfBusiness() {
        return sourceOfBusiness;
    }

    public String getFirstNameOfSecondAnnuitant() {
        return firstNameOfSecondAnnuitant;
    }

    public void setFirstNameOfSecondAnnuitant(String firstNameOfSecondAnnuitant) {
        this.firstNameOfSecondAnnuitant = firstNameOfSecondAnnuitant;
    }

    public String getMiddleNameOfSecondAnnuitant() {
        return middleNameOfSecondAnnuitant;
    }

    public void setMiddleNameOfSecondAnnuitant(String middleNameOfSecondAnnuitant) {
        this.middleNameOfSecondAnnuitant = middleNameOfSecondAnnuitant;
    }

    public String getLastNameOfSecondAnnuitant() {
        return lastNameOfSecondAnnuitant;
    }

    public void setLastNameOfSecondAnnuitant(String lastNameOfSecondAnnuitant) {
        this.lastNameOfSecondAnnuitant = lastNameOfSecondAnnuitant;
    }

    public String getDobOfSecondAnnuitant() {
        return dobOfSecondAnnuitant;
    }

    public void setDobOfSecondAnnuitant(String dobOfSecondAnnuitant) {
        this.dobOfSecondAnnuitant = dobOfSecondAnnuitant;
    }

    public void setSourceOfBusiness(String sourceOfBusiness) {
        this.sourceOfBusiness = sourceOfBusiness;
    }

    public String getApplicableFor() {
        return applicableFor;
    }

    public void setApplicableFor(String applicableFor) {
        this.applicableFor = applicableFor;
    }

    public double getAdditionalAmountIfAny() {
        return additionalAmountIfAny;
    }

    public void setAdditionalAmountIfAny(double additionalAmountIfAny) {
        this.additionalAmountIfAny = additionalAmountIfAny;
    }

    public int getAgeOfFirstAnnuitant() {
        return ageOfFirstAnnuitant;
    }

    public void setAgeOfFirstAnnuitant(int ageOfFirstAnnuitant) {
        this.ageOfFirstAnnuitant = ageOfFirstAnnuitant;
    }

    public int getAgeOfSecondAnnuitant() {
        return ageOfSecondAnnuitant;
    }

    public void setAgeOfSecondAnnuitant(int ageOfSecondAnnuitant) {
        this.ageOfSecondAnnuitant = ageOfSecondAnnuitant;
    }

    public double getAnnuityAmount() {
        return annuityAmount;
    }

    public void setAnnuityAmount(double annuityAmount) {
        this.annuityAmount = annuityAmount;
    }

    public String getAnnuityOption() {
        return annuityOption;
    }

    public void setAnnuityOption(String annuityOption) {
        this.annuityOption = annuityOption;
    }

    public Date getAnnuityPayoutDate() {
        return annuityPayoutDate;
    }

    public void setAnnuityPayoutDate(Date annuityPayoutDate) {
        this.annuityPayoutDate = annuityPayoutDate;
    }

    public String getChannelDetails() {
        return channelDetails;
    }

    public void setChannelDetails(String channelDetails) {
        this.channelDetails = channelDetails;
    }

    public String getGenderOfFirstAnnuitant() {
        return genderOfFirstAnnuitant;
    }

    public void setGenderOfFirstAnnuitant(String genderOfFirstAnnuitant) {
        this.genderOfFirstAnnuitant = genderOfFirstAnnuitant;
    }

    public String getGenderOfSecondAnnuitant() {
        return genderOfSecondAnnuitant;
    }

    public void setGenderOfSecondAnnuitant(String genderOfSecondAnnuitant) {
        this.genderOfSecondAnnuitant = genderOfSecondAnnuitant;
    }

    public boolean getIsADB() {
        return isADB;
    }

    public void setIsADB(boolean isADB) {
        this.isADB = isADB;
    }

    public boolean getIsAdvanceAnnuityPayout() {
        return isAdvanceAnnuityPayout;
    }

    public void setIsAdvanceAnnuityPayout(boolean isAdvanceAnnuityPayout) {
        this.isAdvanceAnnuityPayout = isAdvanceAnnuityPayout;
    }

    public boolean getIsJKresident() {
        return isJKresident;
    }

    public void setIsJKresident(boolean isJKresident) {
        this.isJKresident = isJKresident;
    }

    public boolean getIsStaff() {
        return isStaff;
    }

    public void setIsStaff(boolean isStaff) {
        this.isStaff = isStaff;
    }

    public String getModeOfAnnuityPayout() {
        return modeOfAnnuityPayout;
    }

    public void setModeOfAnnuityPayout(String modeOfAnnuityPayout) {
        this.modeOfAnnuityPayout = modeOfAnnuityPayout;
    }

    public String getOptFor() {
        return optFor;
    }

    public void setOptFor(String optFor) {
        this.optFor = optFor;
    }

    public Date getProposalDate() {
        return proposalDate;
    }

    public void setProposalDate(Date proposalDate) {
        this.proposalDate = proposalDate;
    }

    public double getVestingAmount() {
        return vestingAmount;
    }

    public void setVestingAmount(double vestingAmount) {
        this.vestingAmount = vestingAmount;
    }

    public void setServiceTax(boolean isState) {
        if (isState == true) {
            serviceTax = 0.01;
        } else {
            serviceTax = 0.009;
        }
    }

    public double getServiceTax() {
        return this.serviceTax;
    }

}
