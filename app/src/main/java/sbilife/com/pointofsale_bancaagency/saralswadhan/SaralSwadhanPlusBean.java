package sbilife.com.pointofsale_bancaagency.saralswadhan;

class SaralSwadhanPlusBean {

    //bean variable declaration
    private int ageAtEntry = 0,
            policy_Term = 0;

    private String gender = null,
            premFreqMode = null;

    private double premiumAmt = 0;
    private boolean state = false;

    public int getAgeAtEntry() {
        return ageAtEntry;
    }

    public void setAgeAtEntry(int ageAtEntry) {
        this.ageAtEntry = ageAtEntry;
    }

    public int getPolicy_Term() {
        return policy_Term;
    }

    public void setPolicy_Term(int policy_Term) {
        this.policy_Term = policy_Term;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPremFreqMode() {
        return premFreqMode;
    }

    public void setPremFreqMode(String premFreqMode) {
        this.premFreqMode = premFreqMode;
    }

    public double getPremiumAmt() {
        return premiumAmt;
    }

    public void setPremiumAmt(double premiumAmt) {
        this.premiumAmt = premiumAmt;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }


}
