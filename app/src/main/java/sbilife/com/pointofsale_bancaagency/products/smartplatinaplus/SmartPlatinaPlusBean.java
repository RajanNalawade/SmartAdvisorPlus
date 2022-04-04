package sbilife.com.pointofsale_bancaagency.products.smartplatinaplus;

public class SmartPlatinaPlusBean {
    private int age = 0, guaranteedAge = 0, premPayingTerm = 0, policyTerm = 0, payoutPeriod = 0;
    private String distributionChannel = null, gender = null, guaranteedgender = null, premfreq = null, incomePlan = null, guaranteedpayoutfreq = null;
    private double annualPrem = 0;
    private double sumassured = 0;
    private double guaranteedpayoutamt = 0;
    private boolean isStaff = false, state = false;

    public int getPolicyTerm() {
        return policyTerm;
    }

    public void setPolicyTerm(int policyTerm) {
        this.policyTerm = policyTerm;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getGuaranteedAge() {
        return guaranteedAge;
    }

    public void setGuaranteedAge(int guaranteedAge) {
        this.guaranteedAge = guaranteedAge;
    }

    public int getpayoutPeriod() {
        return payoutPeriod;
    }

    public void setpayoutPeriod(int payoutPeriod) {
        this.payoutPeriod = payoutPeriod;
    }

    public int getPremPayingTerm() {
        return premPayingTerm;
    }

    public void setPremPayingTerm(int premPayingTerm) {
        this.premPayingTerm = premPayingTerm;
    }

    public String getDistributionChannel() {
        return distributionChannel;
    }

    public void setDistributionChannel(String distributionChannel) {
        this.distributionChannel = distributionChannel;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGuaranteedgender() {
        return guaranteedgender;
    }

    public void setGuaranteedgender(String guaranteedgender) {
        this.guaranteedgender = guaranteedgender;
    }

    public String getPremfreq() {
        return premfreq;
    }

    public void setPremfreq(String premfreq) {
        this.premfreq = premfreq;
    }

    public String getincomePlan() {
        return incomePlan;
    }

    public void setincomePlan(String incomePlan) {
        this.incomePlan = incomePlan;
    }

    public String getGuaranteedpayoutfreq() {
        return guaranteedpayoutfreq;
    }

    public void setGuaranteedpayoutfreq(String guaranteedpayoutfreq) {
        this.guaranteedpayoutfreq = guaranteedpayoutfreq;
    }

    public double getAnnualPrem() {
        return annualPrem;
    }

    public void setAnnualPrem(double annualPrem) {
        this.annualPrem = annualPrem;
    }

//	  public double getSumassured()
//	   {return sumassured;}
//	   public void setSumassured(double sumassured)
//	   {this.sumassured=sumassured;}
//
//	  public double getGuaranteedpayoutamt()
//	   {return guaranteedpayoutamt;}
//	   public void setGuaranteedpayoutamt(double guaranteedpayoutamt)
//	   {this.guaranteedpayoutamt=guaranteedpayoutamt;}

    public boolean getIsStaff() {
        return isStaff;
    }

    public void setIsStaff(boolean isStaff) {
        this.isStaff = isStaff;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }


}
