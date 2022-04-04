package sbilife.com.pointofsale_bancaagency.sampoorncancersuraksha;

class SampoornCancerSurakshaBean {

    // Variable Declaration
    private String planName, type, gender, premFreq, proposer_gender;
    private int age, basicTerm;
    private boolean staffDisc, JKResident;
    private double basicSA, serviceTax = 0;

    private boolean isKerlaDisc = false;


    public boolean isKerlaDisc() {
        return isKerlaDisc;
    }

    public void setKerlaDisc(boolean kerlaDisc) {
        isKerlaDisc = kerlaDisc;
    }

    /**
     * Applicable Taxes for JK Resident Change as per 1,Jan,2014 by Akshaya Mirajkar.
     */

    public boolean isJKResident() {
        return JKResident;
    }

    public String getProposer_gender() {
        return proposer_gender;
    }

    public void setProposer_gender(String proposer_gender) {
        this.proposer_gender = proposer_gender;
    }

    public void setJKResident(boolean jKResident) {
        JKResident = jKResident;
    }

    // Getter Setter Methods
    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public boolean getStaffDisc() {
        return staffDisc;
    }

    public void setStaffDisc(boolean staffDisc) {
        this.staffDisc = staffDisc;
    }

    public double getBasicSA() {
        return basicSA;
    }

    public void setBasicSA(double basicSA) {
        this.basicSA = basicSA;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPremFreq() {
        return premFreq;
    }

    public void setPremFreq(String premFreq) {
        this.premFreq = premFreq;
    }

    public int getBasicTerm() {
        return basicTerm;
    }

    public void setBasicTerm(int basicTerm) {
        this.basicTerm = basicTerm;
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
