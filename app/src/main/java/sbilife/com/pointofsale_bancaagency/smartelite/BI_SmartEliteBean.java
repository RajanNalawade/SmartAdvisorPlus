package sbilife.com.pointofsale_bancaagency.smartelite;

class BI_SmartEliteBean {
    //Bean Variable Declaration
    private int ageAtEntry = 0, PF = 0, premiumPayingTerm = 0, termADB = 0, policyTerm_Basic = 0, yearsElapsedSinceInception = 0;
    private String gender = null, planOption = null, premFreq = null, premFreqMode = null, investmentStrategy = null;
    private boolean isStaffDisc = false, isBancAssuranceDisc = false, isADBcheked = false;
    private double accidentBenefitSumAssured = 0;
    private double sumAssuredADB = 0;
    private double premiumAmount = 0;
    private double effectivePremium = 0;
    private double SAMF = 0;
    private double percentToBeInvestedForIndexFund = 0;
    private double percentToBeInvestedForDailyProtectFund = 0;
    private double effectiveTopUpPrem = 0;
    private double percentToBeInvested_EquityEliteIIFund = 0;
    private double percentToBeInvested_BalancedFund = 0;
    private double percentToBeInvested_BondFund = 0;
    private double percentToBeInvested_MoneyMarketFund = 0;
    private double percentToBeInvested_IndexFund = 0;
    private double percentToBeInvested_PEmanagedFund = 0;
    private double serviceTax = 0;
    private double percentToBeInvested_BondOptimiserFund = 0;
    private double percentToBeInvested_MidcapFund = 0;
    private double percentToBeInvested_PureFund = 0;
    private double percentToBeInvested_EquityFund = 0;
    private double percent_PureFund = 0;
    private double percent_MidcapFund = 0;
    private double percent_BondOptimiserFund = 0;
    private double percent_EquityFund = 0;
    private double percentToBeInvested_CorpBondFund = 0;


    public double getPercent_PureFund() {
        return percent_PureFund;
    }

    public void setPercent_PureFund(double percent_PureFund) {
        this.percent_PureFund = percent_PureFund / 100;
    }

    public double getPercent_MidcapFund() {
        return percent_MidcapFund;
    }

    public void setPercent_MidcapFund(double percent_MidcapFund) {
        this.percent_MidcapFund = percent_MidcapFund / 100;
    }

    public double getPercent_BondOptimiserFund() {
        return percent_BondOptimiserFund;
    }

    public void setPercent_BondOptimiserFund(double percent_BondOptimiserFund) {
        this.percent_BondOptimiserFund = percent_BondOptimiserFund / 100;
    }

    public double getPercent_EquityFund() {
        return percent_EquityFund;
    }

    public void setPercent_EquityFund(double percent_EquityFund) {
        this.percent_EquityFund = percent_EquityFund / 100;
    }

    public void setPercentToBeInvested_CorpBondFund(double perInvCorpBondFund) {
        this.percentToBeInvested_CorpBondFund = perInvCorpBondFund / 100;
    }

    public double getPercentToBeInvested_CorpBondFund() {
        return percentToBeInvested_CorpBondFund;
    }

    public double getPercent_Discontinuedpolicyfund() {
        return percent_Discontinuedpolicyfund;
    }

    public void setPercent_Discontinuedpolicyfund(double percent_Discontinuedpolicyfund) {
        this.percent_Discontinuedpolicyfund = percent_Discontinuedpolicyfund / 100;
    }

    private double percent_Discontinuedpolicyfund = 0;

    private boolean isKerlaDisc = false;

    /* public void setServiceTax(boolean isState)
     {
        if (isState == true) {
            serviceTax = 0.19;
        } else {
            serviceTax = 0.18;
        }
    }

    public double getServiceTax() {
        return this.serviceTax;
     }*/
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

    public String getPlanOption() {
        return planOption;
    }

    public void setPlanOption(String planOption) {
        this.planOption = planOption;
    }

    public String getPremFreq() {
        return premFreq;
    }

    public void setPremFreq(String premFreq) {
        this.premFreq = premFreq;
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

    public int getTermADB() {
        return termADB;
    }

    public void setTermADB(int termADB) {
        this.termADB = termADB;
    }

    public int getPF() {
        return PF;
    }

    public void setPF(int PF) {
        this.PF = PF;
    }

    public int getYearsElapsedSinceInception() {
        return yearsElapsedSinceInception;
    }

    public void setYearsElapsedSinceInception(int yearsElapsedSinceInception) {
        this.yearsElapsedSinceInception = yearsElapsedSinceInception;
    }

    public boolean getIsForStaffOrNot() {
        return isStaffDisc;
    }

    public void setIsForStaffOrNot(boolean isStaffDisc) {
        this.isStaffDisc = isStaffDisc;
    }

    public boolean getIsBancAssuranceDiscOrNot() {
        return isBancAssuranceDisc;
    }

    public void setIsBancAssuranceDiscOrNot(boolean isBancAssuranceDisc) {
        this.isBancAssuranceDisc = isBancAssuranceDisc;
    }

    public boolean getIsADBchecked() {
        return isADBcheked;
    }

    public void setIsADBchecked(boolean isADBcheked) {
        this.isADBcheked = isADBcheked;
    }

    public int getAgeAtEntry() {
        return ageAtEntry;
    }

    public void setAgeAtEntry(int ageAtEntry) {
        this.ageAtEntry = ageAtEntry;
    }

    public int getPremiumPayingTerm() {
        return premiumPayingTerm;
    }

    public void setPremiumPayingTerm(int premiumPayingTerm) {
        this.premiumPayingTerm = premiumPayingTerm;
    }

    public double getPercentToBeInvestedForIndexFund() {
        return percentToBeInvestedForIndexFund;
    }

    public void setPercentToBeInvestedForIndexFund(double percentToBeInvestedForIndexFund) {
        this.percentToBeInvestedForIndexFund = percentToBeInvestedForIndexFund / 100;
    }

    public double getSAMF() {
        return SAMF;
    }

    public void setSAMF(double SAMF) {
        this.SAMF = SAMF;
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

    public double getSumAssuredADB() {
        return sumAssuredADB;
    }

    public void setSumAssuredADB(double sumAssuredADB) {
        this.sumAssuredADB = sumAssuredADB;
    }

    public double getPercentToBeInvested_EquityEliteIIFund() {
        return percentToBeInvested_EquityEliteIIFund;
    }

    public void setPercentToBeInvested_EquityEliteIIFund(double percentToBeInvested_EquityEliteIIFund) {
        this.percentToBeInvested_EquityEliteIIFund = percentToBeInvested_EquityEliteIIFund / 100;
    }

    public double getPercentToBeInvested_BalancedFund() {
        return percentToBeInvested_BalancedFund;
    }

    public void setPercentToBeInvested_BalancedFund(double percentToBeInvested_BalancedFund) {
        this.percentToBeInvested_BalancedFund = percentToBeInvested_BalancedFund / 100;
    }

    public double getPercentToBeInvested_BondFund() {
        return percentToBeInvested_BondFund;
    }

    public void setPercentToBeInvested_BondFund(double percentToBeInvested_BondFund) {
        this.percentToBeInvested_BondFund = percentToBeInvested_BondFund / 100;
    }

    public double getPercentToBeInvested_MoneyMarketFund() {
        return percentToBeInvested_MoneyMarketFund;
    }

    public void setPercentToBeInvested_MoneyMarketFund(double percentToBeInvested_MoneyMarketFund) {
        this.percentToBeInvested_MoneyMarketFund = percentToBeInvested_MoneyMarketFund / 100;
    }

   /* public double getPercentToBeInvested_IndexFund()
    {return percentToBeInvested_IndexFund;}
    public void setPercentToBeInvested_IndexFund(double percentToBeInvested_IndexFund)
    {this.percentToBeInvested_IndexFund=percentToBeInvested_IndexFund/100;}*/

   /* public double getPercentToBeInvested_PEmanagedFund()
    {return percentToBeInvested_PEmanagedFund;}
    public void setPercentToBeInvested_PEmanagedFund(double percentToBeInvested_PEmanagedFund)
    {this.percentToBeInvested_PEmanagedFund=percentToBeInvested_PEmanagedFund/100;}*/

    public double getPercentToBeInvestedForDailyProtectFund() {
        return percentToBeInvestedForDailyProtectFund;
    }

    public void setPercentToBeInvestedForDailyProtectFund(double percentToBeInvestedForDailyProtectFund) {
        this.percentToBeInvestedForDailyProtectFund = percentToBeInvestedForDailyProtectFund / 100;
    }

    public double getEffectiveTopUpPrem() {
        return effectiveTopUpPrem;
    }

    public void setEffectiveTopUpPrem(String addTopUp, String premFreq, double topUpPremiumAmt) {
        if (addTopUp.equals("Yes")) {
            //For Regular Frequency Mode
            if (premFreq.equals("Limited")) {
                if ((topUpPremiumAmt % 100) == 0) {
                    this.effectiveTopUpPrem = topUpPremiumAmt;
                } else {
                    this.effectiveTopUpPrem = topUpPremiumAmt - (topUpPremiumAmt % 100);
                }
            }
            //For Single Frequency Mode
            else {
                if ((topUpPremiumAmt % 100) == 0) {
                    this.effectiveTopUpPrem = topUpPremiumAmt;
                } else {
                    this.effectiveTopUpPrem = topUpPremiumAmt - (topUpPremiumAmt % 100);
                }
            }
        } else {
            this.effectiveTopUpPrem = 0;
        }
    }

    public double getAccidentBenefitSumAsssured() {
        return accidentBenefitSumAssured;
    }

    public void setAccidentBenefitSumAssured(double sumAssured) {
        this.accidentBenefitSumAssured = Math.min(sumAssured, 5000000);
    }

    public double getPercentToBeInvested_BondOptimiserFund() {
        return percentToBeInvested_BondOptimiserFund;
    }

    public void setPercentToBeInvested_BondOptimiserFund(double percentToBeInvested_BondOptimiserFund) {
        this.percentToBeInvested_BondOptimiserFund = percentToBeInvested_BondOptimiserFund / 100;
    }

    public double getPercentToBeInvested_MidcapFund() {
        return percentToBeInvested_MidcapFund;
    }

    public void setPercentToBeInvested_MidcapFund(double percentToBeInvested_MidcapFund) {
        this.percentToBeInvested_MidcapFund = percentToBeInvested_MidcapFund / 100;
    }

    public double getPercentToBeInvested_PureFund() {
        return percentToBeInvested_PureFund;
    }

    public void setPercentToBeInvested_PureFund(double percentToBeInvested_PureFund) {
        this.percentToBeInvested_PureFund = percentToBeInvested_PureFund / 100;
    }

    public double getPercentToBeInvested_EquityFund() {
        return percentToBeInvested_EquityFund;
    }

    public void setPercentToBeInvested_EquityFund(double percentToBeInvested_EquityFund) {
        this.percentToBeInvested_EquityFund = percentToBeInvested_EquityFund / 100;
    }

    public String getInvestmentStrategy() {
        return investmentStrategy;
    }

    public void setInvestmentStrategy(String investmentStrategy) {
        this.investmentStrategy = investmentStrategy;
    }
}
