package sbilife.com.pointofsale_bancaagency.shubhnivesh;


public class ShubhNiveshBean {
    //Bean Variable Declaration
    private int age = 0, policyTerm_Basic = 0, policyTerm_PTA = 0, policyTerm_ADB = 0, policyTerm_ATPDB = 0, sumAssured_ADB = 0, sumAssured_ATPDB = 0, sumAssured_PTA = 0;
    private String premiumFreq = null, planName = null, gender = null;
    double sumAssured_Basic = 0, serviceTax = 0;
    private boolean staffDisc = false, bancAssuranceDisc = false, ADB_Status = false, ATPDB_Status = false, PTA_Status = false, jkResident = false;

    //Bean Getter Setter Methods


    /**
     * Applicable Tax for JK Resident.
     * Change as per 1,Jan,2014 by Akshaya Mirajkar.
     */
    private boolean isKerlaDisc = false;

    public void setServiceTax(boolean isState) {
        if (isState == true) {
            serviceTax = 0.19;
        } else {
            serviceTax = 0.18;
        }
    }

    public double getServiceTax() {
        return this.serviceTax;
    }

    public boolean isKerlaDisc() {
        return isKerlaDisc;
    }

    public void setKerlaDisc(boolean kerlaDisc) {
        isKerlaDisc = kerlaDisc;
    }

    public boolean getJKResident() {
        return jkResident;
    }

    public void setJKResident(boolean jKResident) {
        this.jkResident = jKResident;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getPolicyTerm_Basic() {
        return policyTerm_Basic;
    }

    public void setPolicyTerm_Basic(int policyTerm_Basic) {
        this.policyTerm_Basic = policyTerm_Basic;
    }

    public double getSumAssured_Basic() {
        return sumAssured_Basic;
    }

    public void setSumAssured_Basic(double sumAssured_Basic) {
        this.sumAssured_Basic = sumAssured_Basic;
    }

    public int getSumAssured_ADB() {
        return sumAssured_ADB;
    }

    public void setSumAssured_ADB(int sumAssured_ADB) {
        this.sumAssured_ADB = sumAssured_ADB;
    }

    public int getSumAssured_PTA() {
        return sumAssured_PTA;
    }

    public void setSumAssured_PTA(int sumAssured_PTA) {
        this.sumAssured_PTA = sumAssured_PTA;
    }

    public int getSumAssured_ATPDB() {
        return sumAssured_ATPDB;
    }

    public void setSumAssured_ATPDB(int sumAssured_ATPDB) {
        this.sumAssured_ATPDB = sumAssured_ATPDB;
    }

    public boolean getStaffDisc() {
        return staffDisc;
    }

    public void setStaffDisc(boolean staffDisc) {
        this.staffDisc = staffDisc;
    }

    public boolean getBancAssuranceDisc() {
        return bancAssuranceDisc;
    }

    public void setBancAssuranceDisc(boolean bancAssuranceDisc) {
        this.bancAssuranceDisc = bancAssuranceDisc;
    }

    public boolean getADB_Status() {
        return ADB_Status;
    }

    public void setADB_Status(boolean ADB_Status) {
        this.ADB_Status = ADB_Status;
    }

    public boolean getPTA_Status() {
        return PTA_Status;
    }

    public void setPTA_Status(boolean PTA_Status) {
        this.PTA_Status = PTA_Status;
    }

    public boolean getATPDB_Status() {
        return ATPDB_Status;
    }

    public void setATPDB_Status(boolean ATPDB_Status) {
        this.ATPDB_Status = ATPDB_Status;
    }

    public String getPremiumFreq() {
        return premiumFreq;
    }

    public void setPremiumFreq(String premiumFreq) {
        this.premiumFreq = premiumFreq;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getPolicyTerm_ADB() {
        return policyTerm_ADB;
    }

    public void setPolicyTerm_ADB(int policyTerm_ADB) {
        this.policyTerm_ADB = policyTerm_ADB;
    }

    public int getPolicyTerm_PTA() {
        return policyTerm_PTA;
    }

    public void setPolicyTerm_PTA(int policyTerm_PTA) {
        this.policyTerm_PTA = policyTerm_PTA;
    }

    public int getPolicyTerm_ATPDB() {
        return policyTerm_ATPDB;
    }

    public void setPolicyTerm_ATPDB(int policyTerm_ATPDB) {
        this.policyTerm_ATPDB = policyTerm_ATPDB;
    }

}

