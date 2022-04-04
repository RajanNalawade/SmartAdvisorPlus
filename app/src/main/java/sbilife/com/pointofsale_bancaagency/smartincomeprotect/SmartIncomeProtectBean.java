package sbilife.com.pointofsale_bancaagency.smartincomeprotect;

public class SmartIncomeProtectBean {

    private String planName, type, gender, premFreq, maturityBenOpt;
    private int age, accCI_SA, adbSA, atpdbSA, basicTerm, accCITerm, adbTerm, atpdbTerm, preferredSA, cc13nlSA, preferredTerm, cc13nlTerm;
    private Boolean preferredStatus, adbStatus, atpdbStatus, cc13nlStatus, staffDisc, JKResident;
    private double basicSA;

    private boolean isKerlaDisc = false;


    public boolean isKerlaDisc() {
        return isKerlaDisc;
    }

    public void setKerlaDisc(boolean kerlaDisc) {
        isKerlaDisc = kerlaDisc;
    }

    public int getPreferredTerm() {
        return preferredTerm;
    }

    public void setPreferredTerm(int preferredTerm) {
        this.preferredTerm = preferredTerm;
    }

    public int getCc13nlTerm() {
        return cc13nlTerm;
    }

    public boolean getJKResident() {
        return JKResident;
    }

    public void setJKResident(boolean jKResident) {
        this.JKResident = jKResident;
    }

    public void setCc13nlTerm(int cc13nlTerm) {
        this.cc13nlTerm = cc13nlTerm;
    }

    //Getter Setter Methods
    public String getPlanName() {
        return planName;
    }

    public int getPreferredSA() {
        return preferredSA;
    }

    public void setPreferredSA(int preferredSA) {
        this.preferredSA = preferredSA;
    }

    public int getCc13nlSA() {
        return cc13nlSA;
    }

    public void setCc13nlSA(int cc13nlSA) {
        this.cc13nlSA = cc13nlSA;
    }

    public boolean getStaffDisc() {
        return staffDisc;
    }

    public void setStaffDisc(boolean staffDisc) {
        this.staffDisc = staffDisc;
    }

    public Boolean getPreferredStatus() {
        return preferredStatus;
    }

    public void setPreferredStatus(Boolean preferredStatus) {
        this.preferredStatus = preferredStatus;
    }

    public Boolean getCc13nlStatus() {
        return cc13nlStatus;
    }

    public void setCc13nlStatus(Boolean cc13nlStatus) {
        this.cc13nlStatus = cc13nlStatus;
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

    //added by sujata 20-02-2020
    public String getMaturityOption() {
        return maturityBenOpt;
    }

    public void setMaturityOption(String maturityBenOpt) {
        this.maturityBenOpt = maturityBenOpt;
    }

    //end
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

    public int getAccCI_SA() {
        return accCI_SA;
    }

    public void setAccCI_SA(int accCI_SA) {
        this.accCI_SA = accCI_SA;
    }

    public boolean getADB_Status() {
        return adbStatus;
    }

    public void setADB_Status(boolean adbStatus) {
        this.adbStatus = adbStatus;
    }

    public boolean getATPDB_Status() {
        return atpdbStatus;
    }

    public void setATPDB_Status(boolean atpdbStatus) {
        this.atpdbStatus = atpdbStatus;
    }

    public int getADB_Term() {
        return adbTerm;
    }

    public void setADB_Term(int adbTerm) {
        this.adbTerm = adbTerm;
    }

    public int getATPDB_Term() {
        return atpdbTerm;
    }

    public void setATPDB_Term(int atpdbTerm) {
        this.atpdbTerm = atpdbTerm;
    }

    public int getADB_SA() {
        return adbSA;
    }

    public void setADB_SA(int adbSA) {
        this.adbSA = adbSA;
    }

    public int getATPDB_SA() {
        return atpdbSA;
    }

    public void setATPDB_SA(int atpdbSA) {
        this.atpdbSA = atpdbSA;
    }

    public int getAccCI_Term() {
        return accCITerm;
    }

    public void setAccCI_Term(int accCITerm) {
        this.accCITerm = accCITerm;
    }

}