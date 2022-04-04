package sbilife.com.pointofsale_bancaagency.smartswadhanplus;

class SmartSwadhanPlusBean {

    // Bean Variable Declaration
    private int age = 0, policyTerm = 0, premiumPayingTerm = 0, sumAssured = 0;
    private String premiumFreq = null, planType = null, gender = null;
    private boolean staffDisc = false, jkResident = false,
            jkResidentDisc = false;
    private boolean isKerlaDisc = false;
    String distribution_channel = "";

    public String getDistribution_channel() {
        return distribution_channel;
    }

    public void setDistribution_channel(String distribution_channel) {
        this.distribution_channel = distribution_channel;
    }

    public boolean isKerlaDisc() {
        return isKerlaDisc;
    }

    public void setKerlaDisc(boolean kerlaDisc) {
        isKerlaDisc = kerlaDisc;
    }

    // Bean Getter Setter Methods
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

    public int getPremiumPayingTerm() {
        return premiumPayingTerm;
    }

    public void setPremiumPayingTerm(int premiumPayingTerm) {
        this.premiumPayingTerm = premiumPayingTerm;
    }

    public int getSumAssured() {
        return sumAssured;
    }

    public void setSumAssured(int sumAssured) {
        this.sumAssured = sumAssured;
    }

    public String getPremiumFreq() {
        return premiumFreq;
    }

    public void setPremiumFreq(String premiumFreq) {
        this.premiumFreq = premiumFreq;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean getStaffDisc() {
        return staffDisc;
    }

    public void setStaffDisc(boolean staffDisc) {
        this.staffDisc = staffDisc;
    }

    public void setJkResident(boolean jkResident) {
        this.jkResident = jkResident;
    }

    public boolean getJkResident() {
        return jkResident;
    }

    public boolean getJKResidentDisc() {
        return jkResidentDisc;
    }

    public void setJKResidentDisc(boolean jkResidentDisc) {
        this.jkResidentDisc = jkResidentDisc;
    }
}
