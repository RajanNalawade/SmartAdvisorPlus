package sbilife.com.pointofsale_bancaagency.smartmoneyplanner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import sbilife.com.pointofsale_bancaagency.common.CommonForAllProd;
import sbilife.com.pointofsale_bancaagency.smartchamp.SmartChampDB;

class SmartMoneyPlannerBusinessLogic {

    private double TabularPremiumRate = 0;
    private double staffRebate = 0;
    private double SARebate = 0;
    private final double NSAP_Rate = 0;
    private final double EMR = 0;
    private double LoadngFreqOfPremiums = 0;
    private double PF = 0;

    private double premiumWithoutSTwithoutRoundUP = 0;
    private double yearlyPremiumWithoutST = 0;
    private double premiumWithoutSTwithRoundUP = 0;
    double premiumWithST = 0;
    double serviceTax = 0;
    private double BasePremiumWithoutSTwithRoundUP = 0;
    private double basePremiumWithST = 0;
    private double baseServiceTax = 0;
    private double yearlyPremiumWithST = 0;
    private double totalBasePremiumPaid = 0;
    private double guaranteedDeathBenefit = 0;
    private double nonGuarateedDeathBenefitAt_4_Percent = 0;
    private double nonGuarateedDeathBenefitAt_8_Percent = 0;
    private double guaranteedSurvivalBenefit = 0;
    private double GuaranteedMaturityBenefit = 0;
    private double nonGuarateedSurvivalBenefitAt_4_Percent = 0;
    private double nonGuarateedSurvivalBenefitAt_8_Percent = 0;
    private double GSV_surrendr_val = 0;
    private double NonGSV_surrndr_val_4Percent = 0;
    private double NonGSV_surrndr_val_8Percent = 0;
    private double singleTotalBasePremPaid = 0;
    private double AP_without_Modal_Loading = 0;
    private double totalBasePremiumPaidPPT = 0;

    /**
     * Added by Akshaya on 04-AUG-15 Start
     ********/
    private String premiumWithoutSTwithoutStaffwithoutRoundUP;
    private String premiumWithoutSTwithoutStaffwithRoundUP;
    /**
     * Added by Akshaya on 04-AUG-15 End
     ********/

    private int growthPeriod = 0;
    private CommonForAllProd cfap = null;
    private SmartMoneyPlannerProperties smpProp = null;
    // SmartMoneyPlannerProperties prop = null;
    private SmartMoneyPlannerDB dbObj = null;
    private SmartMoneyPlannerBean smartmoneyplannerbean = null;
    private SmartMoneyPlannerProperties prop = null;

    public double getMinesOccuInterest(double SumAssured_Basic) {
        return (SumAssured_Basic / 1000) * 2;
    }

    public double getBackDateInterest(double grossPremium, String bkDate) {

        try {

            // double indigoRate=7.5;
            double ST = 0;
            if (smartmoneyplannerbean.isJKResident()) {
                ST = smpProp.serviceTaxJKResident;
            } else {
                ST = smpProp.serviceTax + smpProp.SBCServiceTax
                        + smpProp.KKCServiceTax;
            }
            double indigoRate = 6.50;
            double ServiceTaxValue = (ST + 1) * 100;

            int compoundFreq = 2;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            SimpleDateFormat dateformat1 = new SimpleDateFormat("dd-MM-yyyy");

            Date dtBackdate = dateformat1.parse(bkDate);
            String strBackDate = dateFormat.format(dtBackdate);

            Calendar cal = Calendar.getInstance();
            // System.out.println("cal "+cal);
            // System.out.println("cal "+cal.getTime());
            String date = dateFormat.format(cal.getTime());
            // System.out.println("date "+date);

            // double netPremWithoutST=Math.round((grossPremium*100)/103.09);
            double netPremWithoutST = Math.round((grossPremium * 100)
                    / ServiceTaxValue);
            // System.out.println("netPremWithoutST "+netPremWithoutST);
            int days = cfap.setDate(date, strBackDate);
            // System.out.println("no of days "+days);
            double monthsBetween = cfap.roundDown((double) days / 365 * 12, 0);
            // System.out.println("aaaaaaaaa "+(double)79/365);
            // System.out.println("month "+monthsBetween);

            double interest = getCompoundAmount(monthsBetween,
                    netPremWithoutST, indigoRate, compoundFreq);
            // System.out.println("onterest "+interest);
            return interest;
        } catch (Exception e) {
            return 0;
        }
    }

    private double getCompoundAmount(double monthsBetween,
                                     double netPremWithoutST, double indigoRate, int compoundFreq) {
        double compoundAmount = netPremWithoutST
                * Math.pow((1 + (indigoRate / (100 * compoundFreq))),
                (compoundFreq * (monthsBetween / 12)))
                - netPremWithoutST;
        return compoundAmount;
        // System.out.println("compoundAmount "+compoundAmount);
    }

    public SmartMoneyPlannerBusinessLogic(
            SmartMoneyPlannerBean smartMoneyPlannerBean) {
        // TODO Auto-generated constructor stub
        this.smartmoneyplannerbean = smartMoneyPlannerBean;
        dbObj = new SmartMoneyPlannerDB();
        prop = new SmartMoneyPlannerProperties();
        cfap = new CommonForAllProd();
        smpProp = new SmartMoneyPlannerProperties();
    }

    // Set Premium without ST and roundup
    public void setPremiumWithoutSTwithoutRoundUP(int age, int plan,
                                                  double sumAssured, boolean staffDisc, String premFreqMode) {
        SmartChampDB smartchampDB = new SmartChampDB();
        double SA_Rebate = 0;
        double tabularPremiumRate = 0;

        staffRebate = getStaffRebate(premFreqMode, staffDisc);
        SA_Rebate = getSARebate(premFreqMode, sumAssured);
        LoadngFreqOfPremiums = getLoadingFrqOfPremium(premFreqMode, "Basic");

        tabularPremiumRate = getTabularPremiumRate(age, premFreqMode, plan);

//				System.out.println("staffRebate "+staffRebate);
//				System.out.println("SA_Rebate "+SA_Rebate);
//				System.out.println("LoadngFreqOfPremiums "+LoadngFreqOfPremiums);
//				System.out.println("tabularPremiumRate "+tabularPremiumRate);

        // PF = getPF(premFreqMode);
        PF = 1;
        double temp = ((tabularPremiumRate * (1.0 - staffRebate) - SARebate + (NSAP_Rate + EMR))
                * sumAssured / 1000.0 * LoadngFreqOfPremiums / PF);
        double ans = Double
                .valueOf(cfap.getRoundOffLevel2(String.valueOf(temp)));

        this.premiumWithoutSTwithoutRoundUP = ((tabularPremiumRate
                * (1.0 - staffRebate) - SARebate + (NSAP_Rate + EMR))
                * sumAssured / 1000.0 * LoadngFreqOfPremiums / PF);

        // return ans;
    }

    public double getPremiumWithoutSTwithoutRoundUP() {
        return premiumWithoutSTwithoutRoundUP;
    }

    private double getSARebate(String premFreqMode, double sumAssured) {
        if (premFreqMode.equalsIgnoreCase("Single")) {
            if (sumAssured >= 100000 && sumAssured < 200000)
                SARebate = 0;
            else if (sumAssured >= 200000 && sumAssured < 300000)
                SARebate = 20;
            else if (sumAssured >= 300000 && sumAssured < 500000)
                SARebate = 25;
            else
                SARebate = 30;
        } else {
            if (sumAssured >= 100000 && sumAssured < 200000)
                SARebate = 0;
            else if (sumAssured >= 200000 && sumAssured < 300000)
                SARebate = 2;
            else if (sumAssured >= 300000 && sumAssured < 500000)
                SARebate = 3;
            else
                SARebate = 5;
        }
        return SARebate;
    }

    public double getStaffRebate(String premFreqMode, boolean staff) {
        double staff_Rebate;
        // staffRebate
        if (staff) {
            if (premFreqMode.equalsIgnoreCase("Single"))
                staff_Rebate = 0.02;
            else
                staff_Rebate = 0.07;
        } else
            staff_Rebate = 0;
        return staff_Rebate;

    }

    public double getPF(String premFreqMode) {
        double pf = 0;
        if (premFreqMode.equalsIgnoreCase("Yearly")
                || premFreqMode.equalsIgnoreCase("Single"))
            pf = 1.0;
        else if (premFreqMode.equalsIgnoreCase("Half Yearly"))
            pf = 2.0;
        else if (premFreqMode.equalsIgnoreCase("Quarterly"))
            pf = 4.0;
        else
            pf = 12.0;
        return pf;
    }

    private double getLoadingFrqOfPremium(String premFreqMode, String cover) {

        double loadngFreqOfPremiums = 0;
        // Loading for Frequency Of Premiums
        if (cover.equals("Basic")) {
            if (premFreqMode.equalsIgnoreCase("Yearly")
                    || premFreqMode.equalsIgnoreCase("Single"))
                loadngFreqOfPremiums = 1;
            else if (premFreqMode.equalsIgnoreCase("Half Yearly"))
                loadngFreqOfPremiums = prop.HalfYearly_Modal_Factor;
            else if (premFreqMode.equalsIgnoreCase("Quarterly"))
                loadngFreqOfPremiums = prop.Quarterly_Modal_Factor;
            else
                loadngFreqOfPremiums = prop.Monthly_Modal_Factor;
        } else {
            if (premFreqMode.equalsIgnoreCase("Yearly")
                    || premFreqMode.equalsIgnoreCase("Single"))
                loadngFreqOfPremiums = 1;
            else if (premFreqMode.equalsIgnoreCase("Half Yearly"))
                loadngFreqOfPremiums = 1.04;
            else if (premFreqMode.equalsIgnoreCase("Quarterly"))
                loadngFreqOfPremiums = 1.06;
            else
                loadngFreqOfPremiums = 1.068;
        }
        return loadngFreqOfPremiums;
    }


    private double getTabularPremiumRate(int age, String premFreqMode, int plan) {
        SmartMoneyPlannerDB smartmoneyplanneDB = new SmartMoneyPlannerDB();
        if (age == -1)
            TabularPremiumRate = 0;
        else {
            if (premFreqMode.equalsIgnoreCase("Single")) {
                double[] premiumArr = smartmoneyplanneDB
                        .getSinglePremiumRates();
                int position = 0;
                TabularPremiumRate = 0;
                for (int ageCount = 18; ageCount <= 60; ageCount++) {
                    for (int plancount = 1; plancount <= 4; plancount++) {
                        if (ageCount == age && plancount == plan) {
                            TabularPremiumRate = premiumArr[position];
                            break;
                        }
                        position++;
                    }
                }
            } else {
                double[] premiumArr = smartmoneyplanneDB
                        .getRegularPremiumRates();
                int position = 0;
                TabularPremiumRate = 0;
                for (int ageCount = 18; ageCount <= 60; ageCount++) {
                    for (int plancount = 1; plancount <= 4; plancount++) {
                        if (ageCount == age && plancount == plan) {
                            TabularPremiumRate = premiumArr[position];
                            break;
                        }
                        position++;
                    }
                }
            }
        }

        return TabularPremiumRate;
    }

    public int setBenefitPayingTerm(int plan) {
        if (plan == 1)
            return 5;
        else if (plan == 2)
            return 10;
        else if (plan == 3)
            return 5;
        else
            return 10;
    }

    public int getGrowthPeriod(int age, String premFreqMode, int plan) {
        SmartMoneyPlannerDB smartmoneyplanneDB = new SmartMoneyPlannerDB();
        if (age == -1)
            growthPeriod = 0;
        else {
            if (premFreqMode.equalsIgnoreCase("Single")) {
                int[] growthArr = smartmoneyplanneDB.getGrowthPeriod();
                int position = 0;
                growthPeriod = 0;

                for (int plancount = 1; plancount <= 4; plancount++) {
                    if (plancount == plan) {
                        growthPeriod = growthArr[position + 4];
                    }
                    position++;
                }
            } else {
                int[] growthArr = smartmoneyplanneDB.getGrowthPeriod();
                int position = 0;
                growthPeriod = 0;
                for (int plancount = 1; plancount <= 4; plancount++) {
                    if (plancount == plan) {
                        growthPeriod = growthArr[position];
                    }
                    position++;
                }

            }
        }
        if (premFreqMode.equalsIgnoreCase("Single"))
            return growthPeriod;
        else
            return growthPeriod + smartmoneyplannerbean.getPremPayingTerm();
    }

    public void setPremiumWithoutSTwithRoundUP(double premium,
                                               double PTARiderPremium, double ADBRiderPremium,
                                               double ATPDRiderPremium, double criticarePremium) {
        double riderPremiumWithoutST = 0;
        this.premiumWithoutSTwithRoundUP = (Double.parseDouble(cfap
                .getRoundUp(cfap.getRoundOffLevel2(cfap
                        .getStringWithout_E(premium + PTARiderPremium
                                + ADBRiderPremium + ATPDRiderPremium
                                + criticarePremium)))));
    }

    public double getPremiumWithoutSTwithRoundUP() {
        return premiumWithoutSTwithRoundUP;
    }

    // public void setPremiumWithST(double premiumWithoutST,boolean JKResident)
    // {
    // if(JKResident==true)
//			//		    		return(Double.parseDouble(cfap.getRoundUp((cfap.getStringWithout_E(((0.0105+1)*premiumWithoutST))))));
    // this.premiumWithST=(Double.parseDouble(cfap.getRoundUp((cfap.getStringWithout_E(((prop.serviceTaxJKResident+1)*premiumWithoutST))))));
    // else
    // this.premiumWithST=(Double.parseDouble(cfap.getRoundUp(cfap.getStringWithout_E(((prop.serviceTax+1)*premiumWithoutST)))));
    // }
    // public double getPremiumWithST()
    // {
    // return premiumWithST;
    // }
    //
    // public void setServiceTax(double premiumWithST,double premiumwithRoundUP)
    // {
    // this.serviceTax= (premiumWithST-premiumwithRoundUP);
    // }
    // public double getServiceTax()
    // {
    // return serviceTax;
    // }

    /*** modified by Akshaya on 20-MAY-16 start **/

    public double getServiceTax(double premiumWithoutST, boolean JKResident,
                                String type) {
        if (type.equals("basic")) {
            if (JKResident)
                return Double.parseDouble(cfap.getRoundUp(String.valueOf(premiumWithoutST * smpProp.serviceTaxJKResident)));
            else {
                return Double.parseDouble(cfap.getRoundUp(String.valueOf(premiumWithoutST * smpProp.serviceTax)));
            }
        } else if (type.equals("SBC")) {
            if (JKResident)
                return 0;
            else {
                return Double.parseDouble(cfap.getRoundUp(String.valueOf(premiumWithoutST * smpProp.SBCServiceTax)));
            }
        } else if (type.equals("KERALA")) {

            return Double.parseDouble(cfap.getRoundUp(String.valueOf(premiumWithoutST * smpProp.kerlaServiceTax)));
        } else //KKC
        {
            if (JKResident)
                return 0;
            else {
                return Double.parseDouble(cfap.getRoundUp(String.valueOf(premiumWithoutST * smpProp.KKCServiceTax)));
            }
        }

    }

    public double getServiceTaxSecondYear(double premiumWithoutST,
                                          boolean JKResident, String type) {
        if (type.equals("basic")) {
            if (JKResident)
                return Double.parseDouble(cfap.getRoundUp(String.valueOf(premiumWithoutST * smpProp.serviceTaxJKResident)));
            else {
                return Double.parseDouble(cfap.getRoundUp(String.valueOf(premiumWithoutST * smpProp.serviceTaxSecondYear)));
            }
        } else if (type.equals("SBC")) {
            if (JKResident)
                return 0;
            else {
                return Double.parseDouble(cfap.getRoundUp(String.valueOf(premiumWithoutST * smpProp.SBCServiceTaxSecondYear)));
            }
        } else if (type.equals("KERALA")) {
            return Double.parseDouble(cfap.getRoundUp(String.valueOf(premiumWithoutST * smpProp.kerlaServiceTaxSecondYear)));

        } else // KKC
        {
            if (JKResident)
                return 0;
            else {
                return Double.parseDouble(cfap.getRoundUp(String.valueOf(premiumWithoutST * smpProp.KKCServiceTaxSecondYear)));
            }
        }

    }

    /*** modified by Akshaya on 20-MAY-16 end **/

    /* For the Base Premium */
    public void setBasePremiumWithoutSTwithRoundUP(double premium) {
        double riderPremiumWithoutST = 0;
        this.BasePremiumWithoutSTwithRoundUP = (Double.parseDouble(cfap
                .getRoundUp(cfap.getRoundOffLevel2(cfap
                        .getStringWithout_E(premium)))));
    }

    public double getBasePremiumWithoutSTwithRoundUP() {
        return BasePremiumWithoutSTwithRoundUP;
    }

    public void setBasePremiumWithST(double basePremiumWithoutST,
                                     boolean JKResident) {
        if (JKResident)
            // return(Double.parseDouble(cfap.getRoundUp((cfap.getStringWithout_E(((0.0105+1)*basePremiumWithoutST))))));
            this.basePremiumWithST = (Double
                    .parseDouble(cfap.getRoundUp((cfap
                            .getStringWithout_E(((prop.serviceTaxJKResident + 1) * basePremiumWithoutST))))));
        else
            this.basePremiumWithST = (Double
                    .parseDouble(cfap.getRoundUp(cfap
                            .getStringWithout_E(((prop.serviceTax + 1) * basePremiumWithoutST)))));
    }

    public double getBasePremiumWithST() {
        return basePremiumWithST;
    }

    public void setBaseServiceTax(double basePremiumWithST,
                                  double premiumwithRoundUP) {
        this.baseServiceTax = (basePremiumWithST - premiumwithRoundUP);
    }

    public double getBaseServiceTax() {
        return baseServiceTax;
    }

    /* For the Base Premium */

    public void setYearlyPremiumWithST(double premiumWithST, String premFreqMode) {
        if (premFreqMode.equalsIgnoreCase("Monthly"))
            this.yearlyPremiumWithST = premiumWithST * 12;
        else if (premFreqMode.equalsIgnoreCase("Quarterly"))
            this.yearlyPremiumWithST = premiumWithST * 4;
        else if (premFreqMode.equalsIgnoreCase("Half Yearly"))
            this.yearlyPremiumWithST = premiumWithST * 2;
        else if (premFreqMode.equalsIgnoreCase("Yearly"))
            this.yearlyPremiumWithST = premiumWithST;
        else
            this.yearlyPremiumWithST = premiumWithST;
    }

    public double getYearlyPremiumWithST() {
        return yearlyPremiumWithST;
    }

    public void setYearlyPremiumWithoutST(double premiumWithoutST,
                                          String premFreqMode) {
        this.yearlyPremiumWithoutST = Double
                .parseDouble(cfap.getRoundUp(cfap.getRoundOffLevel2New(cfap
                        .getStringWithout_E(premiumWithoutST))))
                * setPremiumMultiplication(premFreqMode);
    }

    public double getYearlyPremiumWithoutST() {
        return yearlyPremiumWithoutST;
    }

    private int setPremiumMultiplication(String premFreqMode) {
        if (premFreqMode.equalsIgnoreCase("Yearly")
                || premFreqMode.equalsIgnoreCase("Single"))
            return 1;
        else if (premFreqMode.equalsIgnoreCase("Half Yearly"))
            return 2;
        else if (premFreqMode.equalsIgnoreCase("Quarterly"))
            return 4;
        else
            return 12;
    }

    // 2. Total Base Premium Paid

    //added by sujata on 20-02-2020
    public void setTotalBasePremiumPaid(double BasePremiumPaid, int year_F, int premiumPayingTerm, String premFreqMode, int policyTerm) {
        if (year_F <= policyTerm) {
            if (year_F <= premiumPayingTerm)
                this.totalBasePremiumPaid = Math.round(BasePremiumPaid);
            else
                this.totalBasePremiumPaid = 0;
        } else
            this.totalBasePremiumPaid = 0;

        //System.out.println("totalBasePremiumPaid "+totalBasePremiumPaid);
        setSingleTotalBasePremiumPaid(year_F, totalBasePremiumPaid);
    }

    public void settotalbaseofppt(int year_F, int ppt, double BasePremiumPaid) {
        if (year_F <= ppt) {
            //System.out.println("BasePremiumPaid "+BasePremiumPaid);
            this.totalBasePremiumPaidPPT = Math.round(BasePremiumPaid);
        } else {
            this.totalBasePremiumPaidPPT = 0;
        }
        setSingleTotalBasePremiumPaid(year_F, totalBasePremiumPaid);

    }

    public double gettotalbaseofppt() {
        return this.totalBasePremiumPaidPPT;
    }


//			public void setTotalBasePremiumPaid(double BasePremiumPaid,int year_F,int premiumPayingTerm,String premFreqMode,int policyTerm )
//			{
//				if(year_F<=policyTerm)
//				{
//					if(year_F<=premiumPayingTerm)
//						this.totalBasePremiumPaid= Math.round(BasePremiumPaid);
//					else
//						this.totalBasePremiumPaid= Math.round(BasePremiumPaid*premiumPayingTerm);
//				}
//				else
//					this.totalBasePremiumPaid= 0;
//
//				//System.out.println("totalBasePremiumPaid "+totalBasePremiumPaid);
//			setSingleTotalBasePremiumPaid(year_F, totalBasePremiumPaid);


    //				if(year_F<=policyTerm)
//				{
//					if(year_F<=premiumPayingTerm)
//						this.totalBasePremiumPaid= Math.round(BasePremiumPaid*year_F);
//					else
//						this.totalBasePremiumPaid= Math.round(BasePremiumPaid*premiumPayingTerm);
//				}
//				else
//					this.totalBasePremiumPaid= 0;
//			setSingleTotalBasePremiumPaid(year_F, totalBasePremiumPaid);

    /********************** Changed By Saurabh Jain  12/03/2019 END******************/


    public double getTotalBasePremiumPaid() {
        return totalBasePremiumPaid;
    }

    // Guaranteed Benefit payable on death

//			public void setGuaranteedDeathBenefit(double sumAssured,int year_F,int policyTerm,int age,int plan,boolean staffDisc,String premFreqMode)
//			{
//				double a=0;
//				if(smartMoneyPlannerBean.getPremFreqMode().equals("Single")){
//					if(age <45){
//						a=1.25;
//					}
//					else{a=1.1;}
//				}
//				else if(age <45){
//					a=10;
//				}
//				else{a=7;}
//
//				if(year_F<=policyTerm){
//					double temp=0;
//					double temp1 =0;
//					temp =Math.max(sumAssured,(a*getAP_without_Modal_Loading(age,plan,sumAssured,staffDisc,premFreqMode)));
//					temp1 =Math.max(temp,(1.05*getTotalBasePremiumPaid()));
//					this.guaranteedDeathBenefit= temp1;
//				}
//				else{
//					this.guaranteedDeathBenefit= 0;}
//
//
//			}

    public void setGuaranteedDeathBenefit(double sumAssured, double annualprem, int year_F, int policyTerm) {

        if (year_F <= policyTerm) {
            if (smartmoneyplannerbean.getPremFreq().equals("Single")) {
                guaranteedDeathBenefit = Math.max(sumAssured, annualprem * 1.25);
            } else {
                guaranteedDeathBenefit = Math.max(sumAssured, annualprem * 10);
            }
        }
    }

    public double getGuaranteedDeathBenefit() {
        return guaranteedDeathBenefit;
    }


    public double getAP_without_Modal_Loading(int age, int plan, double sumAssured, boolean staffDisc, String premFreqMode) {
        SmartChampDB smartchampDB = new SmartChampDB();
        double SA_Rebate = 0;
        double tabularPremiumRate = 0;

        staffRebate = getStaffRebate(premFreqMode, staffDisc);
        SA_Rebate = getSARebate(premFreqMode, sumAssured);
        LoadngFreqOfPremiums = getLoadingFrqOfPremium(premFreqMode, "Basic");

        tabularPremiumRate = getTabularPremiumRate(age, premFreqMode, plan);
//				PF = getPF(premFreqMode);
//				PF=1;
//				double temp=((tabularPremiumRate*(1.0-staffRebate)-SARebate+(NSAP_Rate+EMR))*sumAssured/1000.0*LoadngFreqOfPremiums/PF);
//				double ans=Double.valueOf(cfap.getRoundOffLevel2(String.valueOf(temp)));

//				this.premiumWithoutSTwithoutRoundUP= ((tabularPremiumRate*(1.0-staffRebate)-SARebate+(NSAP_Rate+EMR))*sumAssured/1000.0*LoadngFreqOfPremiums/PF);
        AP_without_Modal_Loading = (Double.parseDouble(cfap.getRoundUp(cfap.getRoundOffLevel2(cfap.getStringWithout_E((tabularPremiumRate * (1.0 - staffRebate) - SARebate + (NSAP_Rate + EMR)) * sumAssured / 1000)))));
//				AP_without_Modal_Loading=Math.round((tabularPremiumRate*(1.0-staffRebate)-SARebate+(NSAP_Rate+EMR))*sumAssured/1000);
//				System.out.println("AP_without_Modal_Loading"+AP_without_Modal_Loading);
        return AP_without_Modal_Loading;

    }

    /********************** Changed By Saurabh Jain  19/03/2019 END******************/


    // Non-Guaranteed Benefit payable on death at 4%
    public void setNonGuarateedDeathBenefitAt_4_Percent(double sumAssured, int year_F, int policyTerm) {
        double a, b;
        if (year_F <= policyTerm)
            a = year_F * sumAssured * getbonusrate4(smartmoneyplannerbean);
        else
            a = 0;
//			if(year_F==policyTerm)
//				b=a*(smpProp.terminalBonus+100)/100;
//			else
//				b=a*1;

        this.nonGuarateedDeathBenefitAt_4_Percent = a;
    }

    public double getNonGuarateedDeathBenefitAt_4_Percent() {
        return nonGuarateedDeathBenefitAt_4_Percent;
    }

    public void setNonGuarateedDeathBenefitAt_4_PercentSur(double sumAssured, int year_F, int policyTerm) {
        double a, b;
        if (year_F <= policyTerm)
            a = year_F * sumAssured * getbonusrate4(smartmoneyplannerbean);
        else
            a = 0;
        if (year_F == policyTerm)
            //b=a*(smpProp.terminalBonus+100)/100;
            b = a * (smpProp.terminalBonus + 1);
        else
            b = a * 1;

        //System.out.println("nonGuarateedDeathBenefitAt_4_Percent "+nonGuarateedDeathBenefitAt_4_Percent);
        this.nonGuarateedDeathBenefitAt_4_Percent = b;
    }

    public double getNonGuarateedDeathBenefitAt_4_PercentSur() {
        //System.out.println("nonGuarateedDeathBenefitAt_4_Percent "+nonGuarateedDeathBenefitAt_4_Percent);

        return nonGuarateedDeathBenefitAt_4_Percent;
    }

    // Non-Guaranteed Benefit payable on death at 8%
    public void setNonGuarateedDeathBenefitAt_8_Percent(double sumAssured, int year_F, int policyTerm) {
        double a, b;
        if (year_F <= policyTerm)
            a = year_F * sumAssured * getbonusrate8(smartmoneyplannerbean);
        else
            a = 0;
//			if(year_F==policyTerm)
//				b=a*(smpProp.terminalBonus+100)/100;
//			else
//				b=a*1;
        this.nonGuarateedDeathBenefitAt_8_Percent = a;
    }

    public double getNonGuarateedDeathBenefitAt_8_Percent() {
        return nonGuarateedDeathBenefitAt_8_Percent;
    }


    //added by sujata on 24-02-2020
    public void setNonGuarateedDeathBenefitAt_8_PercentSur(double sumAssured, int year_F, int policyTerm) {
        double a, b;
        if (year_F <= policyTerm)
            a = year_F * sumAssured * getbonusrate8(smartmoneyplannerbean);
        else
            a = 0;
        if (year_F == policyTerm)
            b = a * (smpProp.terminalBonus + 1);
        else
            b = a * 1;
        this.nonGuarateedDeathBenefitAt_8_Percent = b;
    }

    public double getNonGuarateedDeathBenefitAt_8_PercentSur() {
        return nonGuarateedDeathBenefitAt_8_Percent;
    }


    private double getbonusrate4(SmartMoneyPlannerBean smartMoneyPlannerBean) {
        double bonusrate4 = 0;

        if (smartMoneyPlannerBean.getPremFreq().equals("Single")) {
            if (smartMoneyPlannerBean.getPlan() == 1) {
                bonusrate4 = 0.008;
            } else if (smartMoneyPlannerBean.getPlan() == 2) {
                bonusrate4 = 0.008;
            } else if (smartMoneyPlannerBean.getPlan() == 3) {
                bonusrate4 = 0.007;
            } else if (smartMoneyPlannerBean.getPlan() == 4) {
                bonusrate4 = 0.007;
            }

        } else {

            if (smartMoneyPlannerBean.getPlan() == 1) {
                bonusrate4 = 0.015;
            } else if (smartMoneyPlannerBean.getPlan() == 2) {
                bonusrate4 = 0.014;
            } else if (smartMoneyPlannerBean.getPlan() == 3) {
                bonusrate4 = 0.014;
            } else if (smartMoneyPlannerBean.getPlan() == 4) {
                bonusrate4 = 0.013;
            }

        }
        return bonusrate4;

    }


    private double getbonusrate8(SmartMoneyPlannerBean smartMoneyPlannerBean) {
        double bonusrate8 = 0;

        if (smartMoneyPlannerBean.getPremFreq().equals("Single")) {
            if (smartMoneyPlannerBean.getPlan() == 1) {
                bonusrate8 = 0.048;
            } else if (smartMoneyPlannerBean.getPlan() == 2) {
                bonusrate8 = 0.053;
            } else if (smartMoneyPlannerBean.getPlan() == 3) {
                bonusrate8 = 0.052;
            } else if (smartMoneyPlannerBean.getPlan() == 4) {
                bonusrate8 = 0.058;
            }

        } else {

            if (smartMoneyPlannerBean.getPlan() == 1) {
                bonusrate8 = 0.049;
            } else if (smartMoneyPlannerBean.getPlan() == 2) {
                bonusrate8 = 0.056;
            } else if (smartMoneyPlannerBean.getPlan() == 3) {
                bonusrate8 = 0.049;
            } else if (smartMoneyPlannerBean.getPlan() == 4) {
                bonusrate8 = 0.055;
            }

        }
        return bonusrate8;

    }


    //added by sujata on 22-02-2020

    public void setGuaranteedSurvivalBenefit(double sumAssured,
                                             int growthPeriod, int policyTerm, int year_f, int BPP) {
        if (year_f > growthPeriod && year_f <= policyTerm) {
            this.guaranteedSurvivalBenefit = sumAssured / BPP;
        } else
            this.guaranteedSurvivalBenefit = 0;


//				else
//				{
//					this.guaranteedSurvivalBenefit= 0;
//				}
    }

    public double getGuaranteedSurvivalBenefit() {
        //System.out.println("guaranteedSurvivalBenefit "+guaranteedSurvivalBenefit);
        return guaranteedSurvivalBenefit;
    }


    public void setGuaranteedMaturityBenefit(int year_f, int policyTerm, double sumAssured, double BPP) {
        if (year_f == policyTerm) {
            if (year_f > growthPeriod && year_f <= policyTerm) {
                this.GuaranteedMaturityBenefit = sumAssured / BPP;
            } else
                this.GuaranteedMaturityBenefit = 0;
        } else {
            this.GuaranteedMaturityBenefit = 0;
        }
    }

    public double getGuaranteedMaturityBenefit() {
        return GuaranteedMaturityBenefit;
    }

    //added by sujata on 22-02-2020
    public double getGuaranteedSurvivalBenefitFinal(int policyTerm, int year_f) {
        if (year_f > policyTerm) {
            return 0;
        } else {
            if (year_f == policyTerm) {
                return 0;
            } else {
                //System.out.println("getGuaranteedSurvivalBenefit "+getGuaranteedSurvivalBenefit());
                return getGuaranteedSurvivalBenefit();
            }
        }
    }


    public void setNonGuarateedSurvivalBenefitAt_4_Percent(double NonGuarateedDeathBenefitAt_4_Percent, int policyTerm, int year_f) {

        if (year_f == policyTerm) {
            this.nonGuarateedSurvivalBenefitAt_4_Percent = (Double
                    .parseDouble(cfap.getRoundUp(cfap
                            .getStringWithout_E(NonGuarateedDeathBenefitAt_4_Percent))));
        } else

            this.nonGuarateedSurvivalBenefitAt_4_Percent = 0;
    }

    public double getNonGuarateedSurvivalBenefitAt_4_Percent() {
        return nonGuarateedSurvivalBenefitAt_4_Percent;
    }

    public void setNonGuarateedSurvivalBenefitAt_8_Percent(
            double nonGuarateedDeathBenefitAt_8_Percent, int policyTerm,
            int year_f) {

        if (year_f == policyTerm) {
            this.nonGuarateedSurvivalBenefitAt_8_Percent = (Double
                    .parseDouble(cfap.getRoundUp(cfap
                            .getStringWithout_E(nonGuarateedDeathBenefitAt_8_Percent))));
        } else

            this.nonGuarateedSurvivalBenefitAt_8_Percent = 0;
    }

    public double getNonGuarateedSurvivalBenefitAt_8_Percent() {
        return nonGuarateedSurvivalBenefitAt_8_Percent;
    }

    /* Rider function */
    public double preferredTARiderPremium() {
        return 0;
    }

    public double ADBRiderPremium() {
        return 0;
    }

    public double ATPDRiderPremium() {
        return 0;
    }

    public double critiCareRiderPremium() {
        return 0;
    }

    /* Rider function */

    /**
     * Added by Akshaya on 04-AUG-15 Start
     ********/
    // Set Premium without ST and roundup
    public void setPremiumWithoutSTwithoutStaffwithoutRoundUP(int age,
                                                              int plan, double sumAssured, boolean staffDisc, String premFreqMode) {
        SmartChampDB smartchampDB = new SmartChampDB();
        double SA_Rebate = 0;
        double tabularPremiumRate = 0;
        double temp1 = 0;

        staffRebate = getStaffRebate(premFreqMode, staffDisc);
        SA_Rebate = getSARebate(premFreqMode, sumAssured);
        LoadngFreqOfPremiums = getLoadingFrqOfPremium(premFreqMode, "Basic");

        tabularPremiumRate = getTabularPremiumRate(age, premFreqMode, plan);
        // PF = getPF(premFreqMode);
        PF = 1;
        double temp = ((tabularPremiumRate * (1.0 - staffRebate) - SARebate + (NSAP_Rate + EMR))
                * sumAssured / 1000.0 * LoadngFreqOfPremiums / PF);
        double ans = Double
                .valueOf(cfap.getRoundOffLevel2(String.valueOf(temp)));

        temp = ((tabularPremiumRate * (1.0 - 0) - SARebate + (NSAP_Rate + EMR))
                * sumAssured / 1000.0 * LoadngFreqOfPremiums / PF);

        this.premiumWithoutSTwithoutStaffwithoutRoundUP = cfap
                .getRoundOffLevel2New(cfap.getStringWithout_E(temp));

    }

    public String getPremiumWithoutSTwithoutStaffwithoutRoundUP() {
        return premiumWithoutSTwithoutStaffwithoutRoundUP;
    }

    public void setPremiumWithoutSTwithoutStaffwithRoundUP() {
        this.premiumWithoutSTwithoutStaffwithRoundUP = cfap
                .getRoundUp(premiumWithoutSTwithoutStaffwithoutRoundUP);
    }

    public String getPremiumWithoutSTwithoutStaffwithRoundUP() {
        return premiumWithoutSTwithoutStaffwithRoundUP;
    }

    private void setSingleTotalBasePremiumPaid(int year_f,
                                               double totalBasePremiumPaid) {
        if (smartmoneyplannerbean.getPremFreq().equals("Single") && year_f == 1)
            this.singleTotalBasePremPaid = totalBasePremiumPaid;
        else if (smartmoneyplannerbean.getPremFreq().equals("Single"))
            this.singleTotalBasePremPaid = singleTotalBasePremPaid;
        else {
            if ((smartmoneyplannerbean.getPlan() == 1 || smartmoneyplannerbean
                    .getPlan() == 2) && year_f == 6)
                this.singleTotalBasePremPaid = totalBasePremiumPaid;
            else if ((smartmoneyplannerbean.getPlan() == 3 || smartmoneyplannerbean
                    .getPlan() == 4) && year_f == 10)
                this.singleTotalBasePremPaid = totalBasePremiumPaid;
        }
    }

    public double getSingleTotalBasePremiumPaid() {
        return this.singleTotalBasePremPaid;
    }


//			public void setGSV_SurrenderValue(int year_F, double totaBasePrem,
//					double sumGuaranteedSurvBen) {
//
//				double a = 0, b = 0, c = 0, d = 0;
//
    // a=totaBasePrem;
//
//				if (smartMoneyPlannerBean.getPremFreqMode().equals("Single"))
//					a = getSingleTotalBasePremiumPaid();
//				else {
//					// if(year_F>6 & year_F<10)
//					// a=totaBasePrem;
//					if ((smartMoneyPlannerBean.getPlan() == 3 || smartMoneyPlannerBean
//							.getPlan() == 4) && year_F >= 10)
//						a = getSingleTotalBasePremiumPaid();
//					else if ((smartMoneyPlannerBean.getPlan() == 3 || smartMoneyPlannerBean
//							.getPlan() == 4) && (year_F > 6 && year_F < 10))
    // a=totaBasePrem;
//					else if ((smartMoneyPlannerBean.getPlan() == 1 || smartMoneyPlannerBean
//							.getPlan() == 2) && year_F >= 6)
//						a = getSingleTotalBasePremiumPaid();
//				}
//
////				System.out.println(getSingleTotalBasePremiumPaid() + "  "
////						+ totaBasePrem);
//				if (smartMoneyPlannerBean.getPremFreqMode().equalsIgnoreCase("Single")) {
//					if (year_F <= smartMoneyPlannerBean.getPolicyTerm_Basic() && year_F <= 3)
//						b = 0.70;
//					else if (year_F <= smartMoneyPlannerBean.getPolicyTerm_Basic()
//							&& year_F > 3)
//						b = 0.90;
//					else
//						b = 0;
//				} else {
//					double[] regularGSV = dbObj.getGSV_Rates();
//					int arrCount = 0;
//					for (int i = 1; i <= 25; i++) {
//						for (int j = 1; j <= 4; j++) {
//							if ((i == year_F && smartMoneyPlannerBean.getPlan() == j)) {
//								// System.out.println(i +" "+ j);
//								c = regularGSV[arrCount];
//								break;
//							}
//							arrCount++;
//							// System.out.println(i +" "+ j +" = "+prStrArr[arrCount]);
//						}
//
//					}
////					System.out.println(" GSV b = " + c);
//
//					if (smartMoneyPlannerBean.getPolicyTerm_Basic() > 9) {
//						if (year_F >= 2)
//							d = 1;
//						else
//							d = 0;
//					} else {
//						if (year_F > 1)
//							d = 1;
//						else
//							d = 0;
//					}
//					b = c * d;
//				}
//				if (year_F > smartMoneyPlannerBean.getPolicyTerm_Basic())
//					c = 0;
//				else if (year_F > 1)
//					c = sumGuaranteedSurvBen;
//
//				this.GSV_surrendr_val = Math.max(0, ((a * b) - c));
////				System.out.println(year_F + "  a : " + a + " b : " + b + " c: " + c
////						+ "         " + this.GSV_surrendr_val);
//			}


    public void setGSV_SurrenderValue(int year_F, double sumSurvivalbenefit, double firstYearoftotalbasePrem, double sumtotalBasePremiumPaidforSurr) {

        double a = 0, c = 0, d = 0, p = 0, surrenderValue = 0, GSVval;

        SmartMoneyPlannerDB db = new SmartMoneyPlannerDB();

        double[] singleGSV = db.getSingleGSV_Factor();
        double[] RegularGSV = db.getRegularGSV_Factor(smartmoneyplannerbean.getPlan());


        if (year_F <= smartmoneyplannerbean.getBasicTerm()) {
//						a= sumtotalBasePremiumPaidforSurr;
//						System.out.println("a "+a);

            if (smartmoneyplannerbean.getPremFreq().equals("Single")) {
                double temp = singleGSV[year_F - 1];
                surrenderValue = temp;

            } else {
                int pos = 0;
                //double regularGSV=0;
                for (int i = 1; i <= year_F; i++) {
                    if (i == year_F) {
                        surrenderValue = RegularGSV[pos];


                    }
                    pos++;
                }

            }

            if (smartmoneyplannerbean.getBasicTerm() > 9) {
                if (year_F >= 2)
                    d = 1;
                else
                    d = 0;
            } else {
                if (year_F > 1)
                    d = 1;
                else
                    d = 0;
            }

            if (year_F > smartmoneyplannerbean.getBasicTerm())
                c = 0;
            else if (year_F > 1)
                c = sumSurvivalbenefit;


            if (year_F == 1) {

                GSVval = (firstYearoftotalbasePrem * surrenderValue);
                //System.out.println("firstYearoftotalbasePrem " +firstYearoftotalbasePrem);

            } else {
                GSVval = (sumtotalBasePremiumPaidforSurr * surrenderValue * d - sumSurvivalbenefit);
            }
            System.out.println("regularGSV " + surrenderValue);
            //	GSVval= (sumtotalBasePremiumPaidforSurr * surrenderValue );
//				System.out.println("\n year_F "+year_F);
//				System.out.println("sumtotalBasePremiumPaidforSurr "+sumtotalBasePremiumPaidforSurr);

            System.out.println("GSVval " + GSVval);

            //System.out.println("sumSurvivalbenefit "+sumSurvivalbenefit);

            //	GSVval=(sumtotalBasePremiumPaidforSurr * surrenderValue * p);
            //System.out.println("GSVval "+GSVval);

            //	this.GSV_surrendr_val = Math.max(0, ((a * b) - c));
            // System.out.println(year_F + "  a : " + a + " b : " + b + " c: " + c
            // + "         " + this.GSV_surrendr_val);
        } else {
            GSVval = 0;
        }

        this.GSV_surrendr_val = Math.max(0, GSVval);
    }


    public double getGSV_SurrenderValue() {
        return this.GSV_surrendr_val;
    }

    public void setNonGSV_surrndr_val_4_Percent(int year_F,
                                                double nongrntdDeathNenft) {
        double a = 0, b = 0, temp = 0;
        int[] ptArr = {7, 12, 15};
        if (year_F <= smartmoneyplannerbean.getPremPayingTerm())
            a = ((double) year_F / smartmoneyplannerbean.getPremPayingTerm())
                    * smartmoneyplannerbean.getBasicSA() + nongrntdDeathNenft;
        else
            a = 1 * smartmoneyplannerbean.getBasicSA() + nongrntdDeathNenft;

        if (year_F > smartmoneyplannerbean.getBasicTerm())
            this.NonGSV_surrndr_val_4Percent = 0;
        else {
            if (smartmoneyplannerbean.getPremFreq().equalsIgnoreCase("Single")) {

                double[] singleSSV = dbObj.getSingle_SSV_Rates();
                int arrCount = 0;
                for (int i = 1; i <= 25; i++) {
                    for (int j = 1; j <= 4; j++) {
                        if ((i == year_F && smartmoneyplannerbean.getPlan() == j)) {
                            // System.out.println(i +" "+ j);
                            b = singleSSV[arrCount];
                            break;
                        }
                        arrCount++;
                        // System.out.println(i +" "+ j +" = "+b);
                    }

                }
                // System.out.println(" NON GSV b = " + b);
            } else {
                double[] regularSSV = dbObj.getRegular_SSV_Rates();
                int arrCount = 0;

                for (int i = 1; i <= 25; i++) {
                    for (int j = 1; j <= 4; j++) {
                        if ((i == year_F && smartmoneyplannerbean.getPlan() == j)) {
                            // System.out.println(i +" "+ j);
                            b = regularSSV[arrCount];
                            break;
                        }
                        arrCount++;
                        // System.out.println(i +" "+ j +" = "+b);
                    }
                }
                // System.out.println(" NON GSV b = " + b);
            }


            this.NonGSV_surrndr_val_4Percent = (Math.round(Double
                    .parseDouble(cfap.getRound(cfap
                            .getStringWithout_E(a * b)))));

            //System.out.println("NonGSV_surrndr_val_4Percent "+NonGSV_surrndr_val_4Percent);

        }
    }

    public double getNonGSV_surrndr_val_4_Percent() {
        return this.NonGSV_surrndr_val_4Percent;
    }

    public void setNonGSV_surrndr_val_8_Percent(int year_F,
                                                double nongrntdDeathNenft) {
        double a = 0, b = 0, temp = 0;
        int[] ptArr = {7, 12, 15};
        if (year_F <= smartmoneyplannerbean.getPremPayingTerm())
            a = ((double) year_F / smartmoneyplannerbean.getPremPayingTerm())
                    * smartmoneyplannerbean.getBasicSA() + nongrntdDeathNenft;
        else
            a = 1 * smartmoneyplannerbean.getBasicSA() + nongrntdDeathNenft;

        if (year_F > smartmoneyplannerbean.getBasicTerm())
            this.NonGSV_surrndr_val_8Percent = 0;
        else {
            if (smartmoneyplannerbean.getPremFreq().equalsIgnoreCase("Single")) {

                double[] singleSSV = dbObj.getSingle_SSV_Rates();
                int arrCount = 0;
                for (int i = 1; i <= 25; i++) {
                    for (int j = 1; j <= 4; j++) {
                        if ((i == year_F && smartmoneyplannerbean.getPlan() == j)) {
                            // System.out.println(i +" "+ j);
                            b = singleSSV[arrCount];
                            break;
                        }
                        arrCount++;
                        // System.out.println(i +" "+ j +" = "+b);
                    }

                }
                // System.out.println(" NON GSV b = " + b);
            } else {

                double[] regularSSV = dbObj.getRegular_SSV_Rates();
                int arrCount = 0;

                for (int i = 1; i <= 25; i++) {
                    for (int j = 1; j <= 4; j++) {
                        if ((i == year_F && smartmoneyplannerbean.getPlan() == j)) {
                            // System.out.println(i +" "+ j);
                            b = regularSSV[arrCount];
                            break;
                        }
                        arrCount++;
                        // System.out.println(i +" "+ j +" = "+b);
                    }

                }

            }


            this.NonGSV_surrndr_val_8Percent = (Math.round(Double
                    .parseDouble(cfap.getRoundOffLevel2(cfap
                            .getStringWithout_E(a * b)))));

            //System.out.println("NonGSV_surrndr_val_8Percent "+NonGSV_surrndr_val_8Percent);

        }
    }

    public double getNonGSV_surrndr_val_8_Percent() {
        return this.NonGSV_surrndr_val_8Percent;
    }

    public double getServiceTaxMines(double MinesOccuInterest) {
        return Double.parseDouble(cfap.getRoundUp(cfap.getRoundOffLevel2(cfap.getStringWithout_E(MinesOccuInterest * (prop.serviceTax + prop.SBCServiceTax)))));
    }

    //added by sujata on 22-02-2020

    public double getAnnulizedPremium(int year, int policyTerm) {
        if (year <= policyTerm) {
            return getTotalBasePremiumPaid();
        } else {
            return 0;
        }
    }


    public double getTotalMaturityBenefit4percent(int year, int policyterm, double maturityBen, double nongrntdDeathNenft_4Percent) {
        double totalmat4per;
        if (year == policyterm) {
            totalmat4per = maturityBen + nongrntdDeathNenft_4Percent + 0.15 * nongrntdDeathNenft_4Percent;
        } else {
            totalmat4per = 0;
        }
        return totalmat4per;
    }

    public double getTotalMaturityBenefit8percent(int year, int policyterm, double maturityBen, double nongrntdDeathNenft_8Percent) {
        double totalmat4per;
        if (year == policyterm) {
            totalmat4per = maturityBen + nongrntdDeathNenft_8Percent + 0.15 * nongrntdDeathNenft_8Percent;
        } else {
            totalmat4per = 0;
        }
        return totalmat4per;
    }

    public double getTotalDeathBenefit4percent(int year, int policyterm, double guarntdDeathBenft, double nongrntdDeathNenft_4Percent, double sumtotalBasePremiumPaidforSurr) {
        double totaldeathben, temp1, temp2;
        if (year <= policyterm) {
            temp1 = guarntdDeathBenft + nongrntdDeathNenft_4Percent + nongrntdDeathNenft_4Percent * 0.15;
            temp2 = 1.05 * sumtotalBasePremiumPaidforSurr;
            totaldeathben = Math.max(temp1, temp2);
            //System.out.println("prempaid "+prempaid);
            //	System.out.println("temp2 "+temp2);
        } else {
            totaldeathben = 0;
        }
        return totaldeathben;
    }

    public double getTotalDeathBenefit8percent(int year, int policyterm, double guarntdDeathBenft, double nongrntdDeathNenft_8Percent, double sumtotalBasePremiumPaidforSurr) {
        double totaldeathben, temp1, temp2;
        if (year <= policyterm) {
            temp1 = guarntdDeathBenft + nongrntdDeathNenft_8Percent + nongrntdDeathNenft_8Percent * 0.15;
            temp2 = 1.05 * sumtotalBasePremiumPaidforSurr;
            totaldeathben = Math.max(temp1, temp2);
        } else {
            totaldeathben = 0;
        }
        return totaldeathben;
    }
}
