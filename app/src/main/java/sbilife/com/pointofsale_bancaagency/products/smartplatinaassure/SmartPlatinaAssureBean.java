package sbilife.com.pointofsale_bancaagency.products.smartplatinaassure;

public class SmartPlatinaAssureBean {

    private int age = 0, policyTerm = 0, premPayingTerm = 0;
    private String gender = null, premfreq = null;
    private double premiumAmount = 0;
    private boolean isStaff = false;
    private boolean isKerlaDisc = false;


    public boolean isKerlaDisc() {
        return isKerlaDisc;
    }

    public void setKerlaDisc(boolean kerlaDisc) {
        isKerlaDisc = kerlaDisc;
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

    public double getPremiumAmt() {
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

    public boolean getIsStaff() {
        return isStaff;
    }

    public void setIsStaff(boolean isStaff) {
        this.isStaff = isStaff;
    }

}
