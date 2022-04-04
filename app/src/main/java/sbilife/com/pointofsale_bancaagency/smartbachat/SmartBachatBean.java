package sbilife.com.pointofsale_bancaagency.smartbachat;

public class SmartBachatBean {


    private int age = 0;
    private int laage = 0;
    private int policyterm = 0;
    private int premiumpayingterm = 0;
    private double sumassured = 0;
    private double adtpdbenifit = 0;


    private String plantype = null;
    private String premfreq = null;
    private String gender = null;

    private boolean isStaffDisc = false;
    private boolean isjammuresident = false;

    private boolean isKerlaDisc = false;


    public boolean isKerlaDisc() {
        return isKerlaDisc;
    }

    public void setKerlaDisc(boolean kerlaDisc) {
        isKerlaDisc = kerlaDisc;
    }


    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    //added by sujata on 21-11-2019///
    public void setLaAge(int laage) {
        this.laage = laage;
    }

    public int getLaAge() {
        return laage;
    }
    ///End////

    public void setPolicyterm(int policyterm) {
        this.policyterm = policyterm;
    }

    public int getPolicyterm() {
        return policyterm;
    }


    public void setPremiumpayingterm(int premiumpayingterm) {
        this.premiumpayingterm = premiumpayingterm;
    }

    public int getPremiumpayingterm() {
        return premiumpayingterm;
    }


    public void setSumAssured(double sumassured) {
        this.sumassured = sumassured;
    }

    public double getSumAssured() {
        return sumassured;
    }


    public void setAdtpdbenifit(int adtpdbenifit) {
        this.adtpdbenifit = adtpdbenifit;
    }

    public double getAdtpdbenifit() {
        return adtpdbenifit;
    }


    public void setPlanType(String plantype) {
        this.plantype = plantype;
    }

    public String getPlantype() {
        return plantype;
    }


    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }


    public void setPremiumFrequency(String premfreq) {
        this.premfreq = premfreq;
    }

    public String getPremiumFrequency() {
        return premfreq;
    }


    public void setIsForStaffOrNot(boolean isStaffDisc) {
        this.isStaffDisc = isStaffDisc;
    }

    public boolean getIsForStaffOrNot() {
        return isStaffDisc;
    }


    public void setIsJammuResident(boolean jammu) {
        this.isjammuresident = jammu;
    }

    public boolean getIsJammuResident() {
        return isjammuresident;
    }

}
