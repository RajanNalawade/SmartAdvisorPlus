package sbilife.com.pointofsale_bancaagency.smartshield;

public class SmartShieldBean {
    //Variable Declaration
    private String planName, type, gender, premFreq, LRI, loanAccountNumber, loanFinancialInstitue, loanCategory,
            loanSumAssuredOutstanding, loanBalanceLoanTenure, loanFirstEmiDate, loanLastEmiDate;
    private int age, accCI_SA, basicTerm, accCITerm, adbTerm, atpdbTerm;
    private double adbSA;
    private double atpdbSA;
    private boolean accCIStatus, adbStatus, atpdbStatus, staffDisc, JKResident;
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
    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getLRI() {
        return LRI;
    }

    public void setLRI(String LRI) {
        this.LRI = LRI;
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

    public int getAccCI_SA() {
        return accCI_SA;
    }

    public void setAccCI_SA(int accCI_SA) {
        this.accCI_SA = accCI_SA;
    }

    public boolean getAccCI_Status() {
        return accCIStatus;
    }

    public void setAccCI_Status(boolean accCIStatus) {
        this.accCIStatus = accCIStatus;
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

    public double getADB_SA() {
        return adbSA;
    }

    public void setADB_SA(double adbSA) {
        this.adbSA = adbSA;
    }

    public double getATPDB_SA() {
        return atpdbSA;
    }

    public void setATPDB_SA(double atpdbSA) {
        this.atpdbSA = atpdbSA;
    }

    public int getAccCI_Term() {
        return accCITerm;
    }

    public void setAccCI_Term(int accCITerm) {
        this.accCITerm = accCITerm;
    }


    public String getLoanAccountNumber() {
        return loanAccountNumber;
    }

    public void setLoanAccountNumber(String loanAccountNumber) {
        this.loanAccountNumber = loanAccountNumber;
    }

    public String getLoanFinancialInstitue() {
        return loanFinancialInstitue;
    }

    public void setLoanFinancialInstitue(String loanFinancialInstitue) {
        this.loanFinancialInstitue = loanFinancialInstitue;
    }

    public String getLoanCategory() {
        return loanCategory;
    }

    public void setLoanCategory(String loanCategory) {
        this.loanCategory = loanCategory;
    }

    public String getLoanSumAssuredOutstanding() {
        return loanSumAssuredOutstanding;
    }

    public void setLoanSumAssuredOutstanding(String loanSumAssuredOutstanding) {
        this.loanSumAssuredOutstanding = loanSumAssuredOutstanding;
    }

    public String getLoanBalanceLoanTenure() {
        return loanBalanceLoanTenure;
    }

    public void setLoanBalanceLoanTenure(String loanBalanceLoanTenure) {
        this.loanBalanceLoanTenure = loanBalanceLoanTenure;
    }

    public void setLoanFirstEmiDate(String loanFirstEmiDate) {
        this.loanFirstEmiDate = loanFirstEmiDate;
    }

    public String getLoanFirstEmiDate() {
        return loanFirstEmiDate;
    }

    public void setLoanLastEmiDate(String loanLastEmiDate) {
        this.loanLastEmiDate = loanLastEmiDate;
    }

    public String getLoanLastEmiDate() {
        return loanLastEmiDate;
    }


}
