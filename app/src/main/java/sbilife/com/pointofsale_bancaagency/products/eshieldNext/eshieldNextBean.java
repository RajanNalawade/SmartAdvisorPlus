//package com.sbilife.eshieldnext;

package sbilife.com.pointofsale_bancaagency.products.eshieldNext;

public class eshieldNextBean {

    private int age = 0;
    private int BetterHalfBenifitAge = 0;
    private int policyterm = 0, PPT = 0, policyTerm_ADB = 0, policyTerm_ATPDB = 0;
    private double sumassured = 0, sumAssured_ADB = 0, sumAssured_ATPDB, serviceTax = 0;
    private boolean state = false, staff = false;

    private String planoption = "";
    private String premiumPayoption = "";
    private String WholeLifeoption = "";
    private String Channel = "";
    private String smoker = "";
    private String premfreq = "";
    private String gender = "";
    private String Underwriting = "Medical";
    private boolean BetterHalfBenifit;
    private boolean ADBRiderStatus;


    public String getDeathBenefitPaymentOption() {
        return DeathBenefitPaymentOption;
    }

    public void setDeathBenefitPaymentOption(String deathBenefitPaymentOption) {
        DeathBenefitPaymentOption = deathBenefitPaymentOption;
    }

    private String DeathBenefitPaymentOption = "";

    public boolean getADBRiderStatus() {
        return ADBRiderStatus;
    }

    public void setADBRiderStatus(boolean ADBRiderStatus) {
        this.ADBRiderStatus = ADBRiderStatus;
    }

    public boolean getATPDBRiderStatus() {
        return ATPDBRiderStatus;
    }

    public void setATPDBRiderStatus(boolean ATPDBRiderStatus) {
        this.ATPDBRiderStatus = ATPDBRiderStatus;
    }

    private boolean ATPDBRiderStatus;

    public String getGender_spouse() {
        return gender_spouse;
    }

    public void setGender_spouse(String gender_spouse) {
        this.gender_spouse = gender_spouse;
    }

    public String getSmoker_spouse() {
        return smoker_spouse;
    }

    public void setSmoker_spouse(String smoker_spouse) {
        this.smoker_spouse = smoker_spouse;
    }

    private String gender_spouse = "";
    private String smoker_spouse = "";


    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public void setPPT(int PPT) {
        this.PPT = PPT;
    }

    public int getPPT() {
        return PPT;
    }

    public void setBetterHalfBenifitAge(int BetterHalfBenifitAge) {
        this.BetterHalfBenifitAge = BetterHalfBenifitAge;
    }

    public int getBetterHalfBenifitAge() {
        return BetterHalfBenifitAge;
    }

    public void setPolicyterm(int policyterm) {
        this.policyterm = policyterm;
    }

    public int getPolicyterm() {
        return policyterm;
    }

    public void setSumassured(double sumassured) {
        this.sumassured = sumassured;
    }

    public double getSumassured() {
        return sumassured;
    }

    public void setSmoker(String smoker) {
        this.smoker = smoker;
    }

    public String getSmoker() {
        return smoker;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setUnderwriting(String Underwriting) {
        this.Underwriting = Underwriting;
    }

    public String getUnderwriting() {
        return Underwriting;
    }

    public void setPremiumFrequency(String premfreq) {
        this.premfreq = premfreq;
    }

    public String getPremiumFrequency() {
        return premfreq;
    }


    public void setPlanoption(String planoption) {
        this.planoption = planoption;
    }

    public String getPlanoption() {
        return planoption;
    }

    public void setPremiumPayoption(String premiumPayoption) {
        this.premiumPayoption = premiumPayoption;
    }

    public String getPremiumPayoption() {
        return premiumPayoption;
    }

    public void setWholeLifeoption(String WholeLifeoption) {
        this.WholeLifeoption = WholeLifeoption;
    }

    public String getWholeLifeoption() {
        return WholeLifeoption;
    }

    public void setChannel(String Channel) {
        this.Channel = Channel;
    }

    public String getChannel() {
        return Channel;
    }

    public double getSumAssured_ADB() {
        return sumAssured_ADB;
    }

    public void setSumAssured_ADB(int sumAssured_ADB) {
        this.sumAssured_ADB = sumAssured_ADB;
    }

    public int getPolicyTerm_ADB() {
        return policyTerm_ADB;
    }

    public void setPolicyTerm_ADB(int policyTerm_ADB) {
        this.policyTerm_ADB = policyTerm_ADB;
    }

    public int getPolicyTerm_ATPDB() {
        return policyTerm_ATPDB;
    }

    public void setPolicyTerm_ATPDB(int policyTerm_ATPDB) {
        this.policyTerm_ATPDB = policyTerm_ATPDB;
    }


    public double getSumAssured_ATPDB() {
        return sumAssured_ATPDB;
    }

    public void setSumAssured_ATPDB(int sumAssured_ATPDB) {
        this.sumAssured_ATPDB = sumAssured_ATPDB;
    }

    public boolean getBetterHalfBenifit() {
        return BetterHalfBenifit;
    }

    public void setBetterHalfBenifit(boolean BetterHalfBenifit) {
        this.BetterHalfBenifit = BetterHalfBenifit;
    }

    //  Added By Saurabh Jain on 07/06/2019 Start
    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public boolean getStaff() {
        return staff;
    }

    public void setStaff(boolean staff) {
        this.staff = staff;
    }

    public void setServiceTax(boolean isState) {
        if (isState == true) {
            serviceTax = 0.1;
        } else {
            serviceTax = 0.09;
        }
    }

    public double getServiceTax() {
        return this.serviceTax;
    }


}
