package sbilife.com.pointofsale_bancaagency.smarthumsafar;

class SmartHumsafarBean {

    // Bean Variable Declaration
    private int ageLA = 0, ageSpouse = 0, PF = 0, policyTerm_Basic = 0, ADBpolicyTerm = 0, policyTermRiderSpouse = 0,
            policyTermRiderLifeAssured = 0;
    private String genderLA = null, genderSpouse = null, premFreqMode = null, ApplicableFor = null;
    private boolean isJKResident = false, isApplicableForRider = false, isRiderEligible = false, isStaffDisc = false;
    private double ADBsumAssuredLA = 0, ADBsumAssuredSpouse = 0, sumAssured = 0;
    private String spouse_title = "", spouse_firstName = "", spouse_middleName = "", spouse_lastName = "", spouse_dob = "", spouse_fullName = "";

    private boolean isKerlaDisc = false;


    public boolean isKerlaDisc() {
        return isKerlaDisc;
    }

    public void setKerlaDisc(boolean kerlaDisc) {
        isKerlaDisc = kerlaDisc;
    }

    // Bean Getter Setter Methods
    public String getGenderLA() {
        return genderLA;
    }

    public void setGenderLA(String genderLA) {
        this.genderLA = genderLA;
    }

    public String getApplicableFor() {
        return ApplicableFor;
    }

    public void setApplicableFor(String ApplicableFor) {
        this.ApplicableFor = ApplicableFor;
    }

    public boolean getISRiderEligible() {
        return isRiderEligible;
    }

    public void setISRiderEligible(boolean isRiderEligible) {
        this.isRiderEligible = isRiderEligible;
    }

    public String getGenderSpouse() {
        return genderSpouse;
    }

    public void setGenderSpouse(String genderSpouse) {
        this.genderSpouse = genderSpouse;
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

    public int getADB_PolicyTermLifeAssured() {
        return policyTermRiderLifeAssured;
    }

    public void setADB_PolicyTermLifeAssured(int policyTermRiderLifeAssured) {
        this.policyTermRiderLifeAssured = policyTermRiderLifeAssured;
    }

    public int getADB_PolicyTermSpouse() {
        return policyTermRiderSpouse;
    }

    public void setADB_PolicyTermSpouse(int policyTermRiderSpouse) {
        this.policyTermRiderSpouse = policyTermRiderSpouse;
    }

    public double getsumAssured() {
        return sumAssured;
    }

    public void setsumAssured(double sumAssured) {
        this.sumAssured = sumAssured;
    }

    public double getADBsumAssuredLA() {
        return ADBsumAssuredLA;
    }

    public void setADBsumAssuredLA(double ADBsumAssuredLA) {
        this.ADBsumAssuredLA = ADBsumAssuredLA;
    }

    public double getADBsumAssuredSpouse() {
        return ADBsumAssuredSpouse;
    }

    public void setADBsumAssuredSpouse(double ADBsumAssuredSpouse) {
        this.ADBsumAssuredSpouse = ADBsumAssuredSpouse;
    }

    public boolean getIsJKResidentDiscOrNot() {
        return isJKResident;
    }

    public void setIsJKResidentDiscOrNot(boolean isJKResident) {
        this.isJKResident = isJKResident;
    }

    public boolean getIsStaffDiscOrNot() {
        return isStaffDisc;
    }

    public void setIsStaffDiscOrNot(boolean isStaffDisc) {
        this.isStaffDisc = isStaffDisc;
    }

    public int getAgeLA() {
        return ageLA;
    }

    public void setAgeLA(int ageLA) {
        this.ageLA = ageLA;
    }

    public int getAgeSpouse() {
        return ageSpouse;
    }

    public void setAgeSpouse(int ageSpouse) {
        this.ageSpouse = ageSpouse;
    }

    public boolean getIsApplicableForADBRider() {
        return isApplicableForRider;
    }

    public void setIsApplicableForADBRider(boolean isApplicableForRider) {
        this.isApplicableForRider = isApplicableForRider;
    }

    public void setSpouse_title(String spouse_title) {
        this.spouse_title = spouse_title;
    }

    public String getSpouse_title() {
        return spouse_title;
    }

    public void setSpouse_firstName(String spouse_firstName) {
        this.spouse_firstName = spouse_firstName;
    }

    public String getSpouse_firstName() {
        return spouse_firstName;
    }

    public void setSpouse_middleName(String spouse_middleName) {
        this.spouse_middleName = spouse_middleName;
    }

    public String getSpouse_middleName() {
        return spouse_middleName;
    }

    public void setSpouse_lastName(String spouse_lastName) {
        this.spouse_lastName = spouse_lastName;
    }

    public String getSpouse_lastName() {
        return spouse_lastName;
    }

    public void setSpouse_dob(String spouse_dob) {
        this.spouse_dob = spouse_dob;
    }

    public String getSpouse_dob() {
        return spouse_dob;
    }

    public void setSpouse_fullName(String spouse_fullName) {
        this.spouse_fullName = spouse_fullName;
    }

    public String getSpouse_fullName() {
        return spouse_fullName;
    }

}
