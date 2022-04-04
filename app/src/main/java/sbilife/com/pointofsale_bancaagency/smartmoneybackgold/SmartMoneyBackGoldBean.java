package sbilife.com.pointofsale_bancaagency.smartmoneybackgold;

class SmartMoneyBackGoldBean {

    //Bean Variable Declaration
    private int age = 0, policyTerm = 0, policyTerm_PT = 0, ppt = 0, policyTerm_ADB = 0, policyTerm_ATPDB = 0, policyTerm_CC13NonLinked = 0, sumAssured_Basic = 0, sumAssured_ADB = 0, sumAssured_ATPDB = 0, sumAssured_PT = 0, sumAssured_CC13NonLinked = 0;
    private String premiumFreq = null, planName = null, gender = null, gender_proposer = null, PremPaymentTerm = null, premFreqOptions = null;
    private boolean staffDisc = false, jkResident = false, ADB_Status = false, ATPDB_Status = false, PT_Status = false, CC13NonLinked_Status = false;
    private boolean isKerlaDisc = false;
    private String premPayOption;
    private String strProposerAge = "";

    public boolean isKerlaDisc() {
        return isKerlaDisc;
    }

    public void setKerlaDisc(boolean kerlaDisc) {
        isKerlaDisc = kerlaDisc;
    }

    //Bean Getter Setter Methods
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getPolicyTerm() {
        return policyTerm;
    }

    public void setPolicyTerm(int policyTerm) {
        this.policyTerm = policyTerm;
    }

    //added by sujata on 04/11/2019 start


    public String getpremPayOption() {
        return premPayOption;
    }

    public void setpremPayOption(String premPayOption) {
        this.premPayOption = premPayOption;
    }

    //end
    public int getSumAssured_Basic() {
        return sumAssured_Basic;
    }

    public void setSumAssured_Basic(int sumAssured_Basic) {
        this.sumAssured_Basic = sumAssured_Basic;
    }

    public int getSumAssured_ADB() {
        return sumAssured_ADB;
    }

    public void setSumAssured_ADB(int sumAssured_ADB) {
        this.sumAssured_ADB = sumAssured_ADB;
    }

    public int getSumAssured_PT() {
        return sumAssured_PT;
    }

    public void setSumAssured_PT(int sumAssured_PT) {
        this.sumAssured_PT = sumAssured_PT;
    }

    public int getSumAssured_CC13NonLinked() {
        return sumAssured_CC13NonLinked;
    }

    public void setSumAssured_CC13NonLinked(int sumAssured_CC13NonLinked) {
        this.sumAssured_CC13NonLinked = sumAssured_CC13NonLinked;
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

    public boolean getJkResident() {
        return jkResident;
    }

    public void setJkResident(boolean jkResident) {
        this.jkResident = jkResident;
    }

    public boolean getADB_Status() {
        return ADB_Status;
    }

    public void setADB_Status(boolean ADB_Status) {
        this.ADB_Status = ADB_Status;
    }

    public boolean getPT_Status() {
        return PT_Status;
    }

    public void setPT_Status(boolean PT_Status) {
        this.PT_Status = PT_Status;
    }

    public boolean getCC13NonLinked_Status() {
        return CC13NonLinked_Status;
    }

    public void setCC13NonLinked_Status(boolean CC13NonLinked_Status) {
        this.CC13NonLinked_Status = CC13NonLinked_Status;
    }

    public boolean getATPDB_Status() {
        return ATPDB_Status;
    }

    public void setATPDB_Status(boolean ATPDB_Status) {
        this.ATPDB_Status = ATPDB_Status;
    }

    public String getPremFreqOptions() {
        return premFreqOptions;
    }

    public void setPremFreqOptions(String premFreqOptions) {
        this.premFreqOptions = premFreqOptions;
    }

    public String getPremiumFreq() {
        return premiumFreq;
    }

    public void setPremiumFreq(String premiumFreq) {
        this.premiumFreq = premiumFreq;
    }

    public String getPremPaymentTerm() {
        return PremPaymentTerm;
    }

    public void setPremPaymentTerm(String premPaymentTerm) {
        PremPaymentTerm = premPaymentTerm;
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

    public String getGender_proposer() {
        return gender_proposer;
    }

    public void setGender_proposer(String gender_proposer) {
        this.gender_proposer = gender_proposer;
    }

    public int getPolicyTerm_ADB() {
        return policyTerm_ADB;
    }

    public void setPolicyTerm_ADB(int policyTerm_ADB) {
        this.policyTerm_ADB = policyTerm_ADB;
    }

    public int getPolicyTerm_PT() {
        return policyTerm_PT;
    }

    public void setPolicyTerm_PT(int policyTerm_PT) {
        this.policyTerm_PT = policyTerm_PT;
    }

    public int getPolicyTerm_ATPDB() {
        return policyTerm_ATPDB;
    }

    public void setPolicyTerm_ATPDB(int policyTerm_ATPDB) {
        this.policyTerm_ATPDB = policyTerm_ATPDB;
    }

    public int getPolicyTerm_CC13NonLinked() {
        return policyTerm_CC13NonLinked;
    }

    public void setPolicyTerm_CC13NonLinked(int policyTerm_CC13NonLinked) {
        this.policyTerm_CC13NonLinked = policyTerm_CC13NonLinked;
    }

    public String getStrProposerAge() {
        return strProposerAge;
    }

    public void setStrProposerAge(String strProposerAge) {
        this.strProposerAge = strProposerAge;
    }
}
