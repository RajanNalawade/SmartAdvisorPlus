package sbilife.com.pointofsale_bancaagency.smartguaranteedsavings;

public class SmartGuaranteedSavingsPlanBean {


    //Bean Variable Declaration
    private int age = 0;
    private String gender = null;
    private double premiumAmount = 0, effectivePremium = 0, sumAssured = 0;
    private int policyTerm = 0, premPayingTerm = 0;
    private String premPayingMode = "", lifeAssuredTitle = null,
            lifeAssuredFirstName = null,
            lifeAssuredMiddleName = null,
            lifeAssuredLastName = null,
            email = null,
            mobile_no = null;
    private boolean staffDisc = false, bancAssuranceDisc = false;


    public String getLifeAssuredTitle() {
        return lifeAssuredTitle;
    }

    public void setLifeAssuredTitle(String lifeAssuredTitle) {
        this.lifeAssuredTitle = lifeAssuredTitle;
    }

    public String getLifeAssuredFirstName() {
        return lifeAssuredFirstName;
    }

    public void setLifeAssuredFirstName(String lifeAssuredFirstName) {
        this.lifeAssuredFirstName = lifeAssuredFirstName;
    }

    public String getLifeAssuredMiddleName() {
        return lifeAssuredMiddleName;
    }

    public void setLifeAssuredMiddleName(String lifeAssuredMiddleName) {
        this.lifeAssuredMiddleName = lifeAssuredMiddleName;
    }

    public String getLifeAssuredLastName() {
        return lifeAssuredLastName;
    }

    public void setLifeAssuredLastName(String lifeAssuredLastName) {
        this.lifeAssuredLastName = lifeAssuredLastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
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
