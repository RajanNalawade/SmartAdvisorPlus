package sbilife.com.pointofsale_bancaagency.poornsuraksha;

class PoornSurakshaBean {

    private int age = 0;
    private int policyterm = 0;
    private double sumassured = 0, serviceTax = 0;


    private String smoker;
    private String premfreq = null;
    private String gender = null;

    private boolean isStaffDisc = false;

    private boolean isKerlaDisc = false;


    public boolean isKerlaDisc() {
        return isKerlaDisc;
    }

    public void setKerlaDisc(boolean kerlaDisc) {
        isKerlaDisc = kerlaDisc;
    }


    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }


    public void setPolicyterm(int policyterm) {
        this.policyterm = policyterm;
    }

    public int getPolicyterm() {
        return policyterm;
    }


    public void setSumAssured(double sumassured) {
        this.sumassured = sumassured;
    }

    public double getSumAssured() {
        return sumassured;
    }


    public void setSmoker(String smoker) {
        this.smoker = smoker;
    }

    public String getSmoker() {
        return smoker;
    }


    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }


    public void setPremiumFrequency(String premfreq) {
        this.premfreq = premfreq;
    }

    public String getPremiumFrequency() {
        return premfreq;
    }


    public void setIsForStaffOrNot(boolean isStaffDisc) {
        this.isStaffDisc = isStaffDisc;
    }

    public boolean getIsForStaffOrNot() {
        return isStaffDisc;
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





