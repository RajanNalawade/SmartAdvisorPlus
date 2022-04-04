package sbilife.com.pointofsale_bancaagency.smartwomenadvantage;

class SmartWomenAdvantageBean {

    private boolean isStaffDisc;
    private boolean isjkResident;
    private boolean isAPCnCAoption;
    private String spnr_Plan;
    private String spnr_Gender;
    private String spnr_PremFreq;
    private String spnr_criticalIllnesOpt;
    private int spnr_Age;
    private int spnr_policyterm;
    private double edt_SumAssured;
    private double edt_critiSumAssured;
    private double edt_APCSumAssured;
    private boolean isKerlaDisc = false;


    public boolean isKerlaDisc() {
        return isKerlaDisc;
    }

    public void setKerlaDisc(boolean kerlaDisc) {
        isKerlaDisc = kerlaDisc;
    }

    public boolean getIsStaffDisc() {
        return isStaffDisc;
    }

    public void setIsStaffDisc(boolean isStaffDisc) {
        this.isStaffDisc = isStaffDisc;
    }

    public boolean getIsjkResident() {
        return isjkResident;
    }

    public void setIsjkResident(boolean isjkResident) {
        this.isjkResident = isjkResident;
    }

    public boolean getIsAPCnCAoption() {
        return isAPCnCAoption;
    }

    public void setIsAPCnCAoption(boolean isAPCnCAoption) {
        this.isAPCnCAoption = isAPCnCAoption;
    }

    public String getSpnr_Plan() {
        return spnr_Plan;
    }

    public void setSpnr_Plan(String spnr_Plan) {
        this.spnr_Plan = spnr_Plan;
    }

    public int getSpnr_Age() {
        return spnr_Age;
    }

    public void setSpnr_Age(int spnr_Age) {
        this.spnr_Age = spnr_Age;
    }

    public String getSpnr_Gender() {
        return spnr_Gender;
    }

    public void setSpnr_Gender(String spnr_Gender) {
        this.spnr_Gender = spnr_Gender;
    }

    public String getSpnr_PremFreq() {
        return spnr_PremFreq;
    }

    public void setSpnr_PremFreq(String spnr_PremFreq) {
        this.spnr_PremFreq = spnr_PremFreq;
    }

    public String getSpnr_criticalIllnesOpt() {
        return spnr_criticalIllnesOpt;
    }

    public void setSpnr_criticalIllnesOpt(String spnr_criticalIllnesOpt) {
        this.spnr_criticalIllnesOpt = spnr_criticalIllnesOpt;
    }

    public int getSpnr_policyterm() {
        return spnr_policyterm;
    }

    public void setSpnr_policyterm(int spnr_policyterm) {
        this.spnr_policyterm = spnr_policyterm;
    }

    public double getEdt_SumAssured() {
        return edt_SumAssured;
    }

    public void setEdt_SumAssured(double edt_SumAssured) {
        this.edt_SumAssured = edt_SumAssured;
    }

    public double getEdt_critiSumAssured() {
        return edt_critiSumAssured;
    }

    public void setEdt_critiSumAssured(double edt_critiSumAssured) {
        this.edt_critiSumAssured = edt_critiSumAssured;
    }

    public double getEdt_APCSumAssured() {
        return edt_APCSumAssured;
    }

    public void setEdt_APCSumAssured(double edt_APCSumAssured) {
        this.edt_APCSumAssured = edt_APCSumAssured;
    }

}
