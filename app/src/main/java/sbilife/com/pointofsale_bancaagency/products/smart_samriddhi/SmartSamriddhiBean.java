package sbilife.com.pointofsale_bancaagency.products.smart_samriddhi;

public class SmartSamriddhiBean {


    //Bean Variable Declaration
    private int age = 0;
    private String gender = null, premfreq = null;
    private double premiumAmount = 0, effectivePremium = 0, sumAssured = 0;
    private int policyTerm = 0, premPayingTerm = 0;
    private String premPayingMode = "";
    private boolean staffDisc = false, bancAssuranceDisc = false;
    private boolean isKerlaDisc = false;


    public boolean isKerlaDisc() {
        return isKerlaDisc;
    }

    public void setKerlaDisc(boolean kerlaDisc) {
        isKerlaDisc = kerlaDisc;
    }

    public double getEffectivePremium() {
        return effectivePremium;
    }

    public void setEffectivePremium(double effectivePremium) {
        this.effectivePremium = effectivePremium;
    }

    public double getSumAssured() {
        return sumAssured;
    }

    public void setSumAssured(double sumAssured) {
        this.sumAssured = sumAssured;
    }

    public int getPolicyTerm() {
        return policyTerm;
    }

    public void setPolicyTerm(int policyTerm) {
        this.policyTerm = policyTerm;
    }

    public int getPremPayingTerm() {
        return premPayingTerm;
    }

    public void setPremPayingTerm(int premPayingTerm) {
        this.premPayingTerm = premPayingTerm;
    }

    public String getPremPayingMode() {
        return premPayingMode;
    }

    public void setPremPayingMode(String premPayingMode) {
        this.premPayingMode = premPayingMode;
    }

    public void setPremiumAmount(double premiumAmount) {
        this.premiumAmount = premiumAmount;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPremfreq() {
        return premfreq;
    }

    public void setPremfreq(String premfreq) {
        this.premfreq = premfreq;
    }

    public double getPremiumAmount() {
        return premiumAmount;
    }

    public void setPremiumAmt(double premiumAmount) {
        this.premiumAmount = premiumAmount;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isStaffDisc() {
        return staffDisc;
    }

    public void setStaffDisc(boolean staffDisc) {
        this.staffDisc = staffDisc;
    }

    public boolean isBancAssuranceDisc() {
        return bancAssuranceDisc;
    }

    public void setBancAssuranceDisc(boolean bancAssuranceDisc) {
        this.bancAssuranceDisc = bancAssuranceDisc;
    }


}
