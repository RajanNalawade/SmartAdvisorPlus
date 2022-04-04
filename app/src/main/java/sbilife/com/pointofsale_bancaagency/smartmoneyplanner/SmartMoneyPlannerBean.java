package sbilife.com.pointofsale_bancaagency.smartmoneyplanner;

public class SmartMoneyPlannerBean {

    private String type, gender, premFreq;
    private int age, basicTerm, premPayingTerm, plan;
    private boolean staffDisc, JKResident;
    private double basicSA;
    private boolean isKerlaDisc = false;


    public boolean isKerlaDisc() {
        return isKerlaDisc;
    }

    public void setKerlaDisc(boolean kerlaDisc) {
        isKerlaDisc = kerlaDisc;
    }

    public boolean isJKResident() {
        return JKResident;
    }

    public void setJKResident(boolean jKResident) {
        JKResident = jKResident;
    }

    //Getter Setter Methods
    public void setPlan(int plan) {
        this.plan = plan;
    }

    public int getPlan() {
        return plan;
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

    public int getPremPayingTerm() {
        return premPayingTerm;
    }

    public void setPremPayingTerm(int premPayingTerm) {
        this.premPayingTerm = premPayingTerm;
    }

    public int getBasicTerm() {
        return basicTerm;
    }

    public void setBasicTerm(int basicTerm) {
        this.basicTerm = basicTerm;
    }


}
