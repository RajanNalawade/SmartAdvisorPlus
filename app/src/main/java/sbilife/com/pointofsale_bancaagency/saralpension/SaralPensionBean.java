package sbilife.com.pointofsale_bancaagency.saralpension;

class SaralPensionBean {


    private String type, gender, premFreq, annuityOption = "";
    private int age, basicTerm = 0, premPayingTerm = 0, ptrTerm, ptrSA, PF = 0, vestingAge = 0;
    private boolean staffDisc, JKResident, cb_ptrRider, isBancAssuranceDisc, PTR_Status = false;
    private double basicSA;
    private boolean isKerlaDisc = false;

    public boolean isKerlaDisc() {
        return isKerlaDisc;
    }

    public void setKerlaDisc(boolean kerlaDisc) {
        isKerlaDisc = kerlaDisc;
    }

    public void setBancAssuranceDisc(boolean isBancAssuranceDisc) {
        this.isBancAssuranceDisc = isBancAssuranceDisc;
    }

    public boolean getBancAssuranceDisc() {
        return isBancAssuranceDisc;
    }

    public int getPF() {
        return PF;
    }

    public void setPF(int pF) {
        PF = pF;
    }

    public boolean getJkResident() {
        return JKResident;
    }

    public int getPtrTerm() {
        return ptrTerm;
    }

    public void setPtrTerm(int ptrTerm) {
        this.ptrTerm = ptrTerm;
    }

    public int getPtrSA() {
        return ptrSA;
    }

    public void setPtrSA(int ptrSA) {
        this.ptrSA = ptrSA;
    }

    public boolean getCb_ptrRider() {
        return cb_ptrRider;
    }

    public void setCb_ptrRider(boolean cb_ptrRider) {
        this.cb_ptrRider = cb_ptrRider;
    }

    public void setJKResident(boolean jKResident) {
        JKResident = jKResident;
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

    public void setPTR_Status(boolean PTR_Status) {
        this.PTR_Status = PTR_Status;
    }

    public boolean getPTR_Status() {
        return PTR_Status;
    }

    public String getAnnuityOption() {
        return annuityOption;
    }

    public void setAnnuityOption(String annuityOption) {
        this.annuityOption = annuityOption;
    }

    public void setVestingAge(int vestingAge) {
        this.vestingAge = vestingAge;
    }

    public int getVestingAge() {
        return vestingAge;
    }

}
