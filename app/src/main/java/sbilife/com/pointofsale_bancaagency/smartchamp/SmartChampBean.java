package sbilife.com.pointofsale_bancaagency.smartchamp;

class SmartChampBean {

    // Bean Variable Declaration
    private int ageOfChild = 0, ageOfProposer = 0, PF = 0,
            premiumPayingTerm = 0, policyTerm_Basic = 0;
    private String gender = null, premFreqMode = null, backdate = null;
    private boolean isStaffDisc = false, isJKResident = false, isBackdate = false, isMines = false;
    private double premiumAmount = 0, effectivePremium = 0, sumAssured = 0;

    private boolean isKerlaDisc = false;


    public boolean isKerlaDisc() {
        return isKerlaDisc;
    }

    public void setKerlaDisc(boolean kerlaDisc) {
        isKerlaDisc = kerlaDisc;
    }

    //Bean Getter Setter Methods
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

    public int getPolicyTerm_Basic() {
        return policyTerm_Basic;
    }

    public void setPolicyTerm_Basic(int policyTerm_Basic) {
        this.policyTerm_Basic = policyTerm_Basic;
    }

    public int getPF() {
        return PF;
    }

    public void setPF(int PF) {
        this.PF = PF;
    }

    public double getsumAssured() {
        return sumAssured;
    }

    public void setsumAssured(double sumAssured) {
        this.sumAssured = sumAssured;
    }

    public boolean getIsForStaffOrNot() {
        return isStaffDisc;
    }

    public void setIsForStaffOrNot(boolean isStaffDisc) {
        this.isStaffDisc = isStaffDisc;
    }

    public boolean getIsJKResidentDiscOrNot() {
        return isJKResident;
    }

    public void setIsJKResidentDiscOrNot(boolean isJKResident) {
        this.isJKResident = isJKResident;
    }

    public int getAgeOfChild() {
        return ageOfChild;
    }

    public void setAgeOfChild(int ageOfChild) {
        this.ageOfChild = ageOfChild;
    }

    public int getAgeOfProposer() {
        return ageOfProposer;
    }

    public void setAgeOfProposer(int ageOfProposer) {
        this.ageOfProposer = ageOfProposer;
    }

    public int getPremiumPayingTerm() {
        return premiumPayingTerm;
    }

    public void setPremiumPayingTerm(int premiumPayingTerm) {
        this.premiumPayingTerm = premiumPayingTerm;
    }

    public double getEffectivePremium() {
        return effectivePremium;
    }

    public void setEffectivePremium(double effectivePremium) {
        this.effectivePremium = effectivePremium;
    }

    public double getPremiumAmount() {
        return premiumAmount;
    }

    public void setPremiumAmount(double premiumAmount) {
        this.premiumAmount = premiumAmount;
    }

    /****** Added By Tushar Kotian on 17/4/2017 start *****/

    public boolean getIsBackDate() {
        return isBackdate;
    }

    public void setIsBackDate(boolean isBackdate) {
        this.isBackdate = isBackdate;
    }


    public boolean getIsMines() {
        return isMines;
    }

    public void setIsMines(boolean isMines) {
        this.isMines = isMines;
    }

    public String getBackdate() {
        return backdate;
    }

    public void setBackdate(String backdate) {
        this.backdate = backdate;
    }
    /****** Added By Tushar Kotian on 17/4/2017 end *****/
}
