package sbilife.com.pointofsale_bancaagency.products.smartplatinaplus;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import sbilife.com.pointofsale_bancaagency.common.CommonForAllProd;

public class SmartPlatinaPlusBusinessLogic {
    public CommonForAllProd cfap = null;
    public SmartPlatinaPlusDB db = null;
    public SmartPlatinaPlusProperties spaProp = null;
    public SmartPlatinaPlusBean spaBean = null;
    double SSVFactorRate6Years = 0, SSVFactorRate8Years = 0, SSVFactorRate10Years = 0, GSVFactor = 0, SAMF = 0;

    public SmartPlatinaPlusBusinessLogic(SmartPlatinaPlusBean spaBean) {
        cfap = new CommonForAllProd();
        spaProp = new SmartPlatinaPlusProperties();
        db = new SmartPlatinaPlusDB();
        this.spaBean = spaBean;
    }


    public double getBasicInstallmentPrem() {
        double basic = 0;
        if (spaBean.getPremfreq().equalsIgnoreCase("Monthly")) {
            basic = spaBean.getAnnualPrem() * 0.085;
        } else if (spaBean.getPremfreq().equalsIgnoreCase("Half-Yearly")) {
            basic = spaBean.getAnnualPrem() * 0.51;
        } else
            basic = spaBean.getAnnualPrem();
        return basic;
    }

    //input
    public double getSumAssured() {
        double val = 0;


        val = 11 * spaBean.getAnnualPrem();
//		System.out.println("prArr " +prArr);
//		System.out.println("pr " +pr);
//		System.out.println("val " +val);

        return val;

    }

    // input
    public double getPolicy_Term() {
        int policy_term = 0;
        policy_term = spaBean.getPremPayingTerm() + spaBean.getpayoutPeriod() + 1;
        //System.out.println("policy_term " +policy_term);
        return policy_term;
    }

//	public String getGroupOfAge(int age)
//	{
//		String a;
//		if(age>=7 && age<=40)
//		{
//			a="7-40";
//		}
//		else
//			if(age>=41 && age<=50)
//			{
//				a="41-50";
//			}
//	    else
//				if(age>=51 && age<=55)
//				{
//					a="51-55";
//				}
//	    else
//		     a= "56-60";
//		return a;
//	}

    public double getInstallmentPremFYwithTax() {
        double InstallmentPremFY = 0;

        InstallmentPremFY = getBasicInstallmentPrem() + getBasicInstallmentPrem() * 0.0225 + getBasicInstallmentPrem() * 0.0225;

        //System.out.println("InstallmentPremFY " +InstallmentPremFY);
        return InstallmentPremFY;


    }

    public double getInstallmentPremSYwithTax() {
        double InstallmentPremSY = 0;
        InstallmentPremSY = getBasicInstallmentPrem() + getBasicInstallmentPrem() * 0.01125 + getBasicInstallmentPrem() * 0.01125;

        return InstallmentPremSY;
    }

    //AK5
    public int getAnnulizePremDB() {
        int Annulizeprem = 0;
        if (spaBean.getAnnualPrem() >= 50000 && spaBean.getAnnualPrem() <= 99000) {
            Annulizeprem = 1;
        } else if (spaBean.getAnnualPrem() >= 100000 && spaBean.getAnnualPrem() <= 499000) {
            Annulizeprem = 2;
        } else
            Annulizeprem = 3;

        return Annulizeprem;

    }

    //AK6
    public String getLifeincomeDB() {
        String LifeIncome = null;
        LifeIncome = (getAnnulizePremDB() + "," + spaBean.getPremPayingTerm() +
                "," + spaBean.getpayoutPeriod() + "," + ((int) getPolicy_Term())).trim();
        return LifeIncome;

    }

    //AK7

    public double getGuaranteedIncomeAMTDB(int age) {
        double GuaranteedIncomeDB = 0;
        int pos = 0;
        String LifeIncomeDb = getLifeincomeDB();
        //System.out.println("getLifeincomeDB() " +LifeIncomeDb);
        if (spaBean.getincomePlan().equals("Life Income")) {
            //	System.out.println("spaBean.getincomePlan() " +spaBean.getincomePlan());
            String[] ardb = db.getLifeIncomeAr();
            double[] ardb1 = db.getLifeIncome();
            outerloop:
            for (int i = 0; i <= 60; i++) {
                for (int j = 0; j < ardb.length; j++) {
                    if (age == i && LifeIncomeDb.equals(ardb[j])) {
                        GuaranteedIncomeDB = ardb1[pos];
                        break outerloop;
                    }
                    pos++;
                }

            }
        } else {
            String[] ardb = db.getGuarantedIncomeAr();
            double[] ardb1 = db.getGuarantedIncome();
            outerloop:
            for (int i = 0; i <= 60; i++) {
                for (int j = 0; j < ardb.length; j++) {
                    if (age == i && LifeIncomeDb.equals(ardb[j])) {
                        GuaranteedIncomeDB = ardb1[pos];
                        break outerloop;
                    }
                    pos++;
                }

            }
        }

        return GuaranteedIncomeDB;

    }

    public double getModelFactor() {
        double modelFactor = 0;
        if (spaBean.getGuaranteedpayoutfreq().equalsIgnoreCase("Monthly")) {

            modelFactor = 0.08;
        } else if (spaBean.getGuaranteedpayoutfreq().equalsIgnoreCase("Quarterly")) {

            modelFactor = 0.24;
        } else if (spaBean.getGuaranteedpayoutfreq().equalsIgnoreCase("Half-Yearly")) {

            modelFactor = 0.49;
        } else
            modelFactor = 1;

        return modelFactor;
    }

    public double getGuaranteedIncomeAMT(int age) {
        double Guaranteedamt = 0;

//		System.out.println("getGuaranteedIncomeAMTDB(age) " +getGuaranteedIncomeAMTDB(age));
//		System.out.println("getModelFactor() " +getModelFactor());
//		System.out.println("spaBean.getAnnualPrem()" +spaBean.getAnnualPrem());
        Guaranteedamt = getGuaranteedIncomeAMTDB(age) * getModelFactor() * spaBean.getAnnualPrem();
        return Guaranteedamt;
    }

    public double getAnnualized_premium(int policYr) {
        double Annualizedprem = 0;
        if (policYr > spaBean.getPremPayingTerm()) {
            Annualizedprem = 0;
        } else {
            Annualizedprem = spaBean.getAnnualPrem();
        }

        return Annualizedprem;
    }

    public double getSumAnnualizedPrem() {
        double Sum = 0;
        int ppt = spaBean.getPremPayingTerm();
        for (int i = 1; i <= ppt; i++) {
            Sum += getAnnualized_premium(i);
        }
        //System.out.println("243 " +Sum);
        return Sum;
    }


    public double getTotalPremium(int policYr) {
        double TotalPrem = 0;
        if (policYr > spaBean.getPremPayingTerm()) {
            TotalPrem = 0;
        } else {
            TotalPrem = spaBean.getAnnualPrem() * getPolicyTerm();
        }
        return TotalPrem;
    }

    public double getSurvivalBenefit(int age, int policyYr) {
        double survival = 0;
        double val = (spaBean.getPremPayingTerm() + 2);
        if (policyYr >= val && policyYr <= getPolicy_Term()) {
            if (spaBean.getGuaranteedpayoutfreq().equalsIgnoreCase("Half-Yearly")) {
                survival = 2;
            } else if (spaBean.getGuaranteedpayoutfreq().equalsIgnoreCase("Monthly")) {
                survival = 12;
            } else if (spaBean.getGuaranteedpayoutfreq().equalsIgnoreCase("Quarterly")) {
                survival = 4;
            } else
                survival = 1;

        }
        survival = getGuaranteedIncomeAMT(age) * survival;
        return survival;
    }


//	public double getPPTPremValue(int age)
//	{
//		int pos=0;
//		String Annual,groupofage;
//		double val=0;
//		groupofage= getGroupOfAge(age);
//		Annual = (spaBean.getPremPayingTerm() + " and " +getAnnulizePrem()).trim();
//
//		if(Annual.equals("8 and 1"))
//		  pos=1;
//		else if(Annual.equals("10 and 1"))
//		  pos=2;
//		else if(Annual.equals("6 and 2"))
//		  pos=3;
//		else if(Annual.equals("8 and 2"))
//		  pos=4;
//		else if(Annual.equals("10 and 2"))
//		  pos=5;
//		else if(Annual.equals("6 and 3"))
//		  pos=6;
//		else if(Annual.equals("8 and 3"))
//		  pos=7;
//		else if(Annual.equals("10 and 3"))
//		  pos=8;
//		//System.out.println("pos " +pos);
//		if(groupofage.equals("7-40"))
//		{
//			val=db.getPPTPrem7to40()[pos];
//		}
//
//		else if(groupofage.equals("41-50"))
//		{
//			val=db.getPPTPrem41to50()[pos];
//		}
//		else if(groupofage.equals("51-55"))
//		{
//			val=db.getPPTPrem51to55()[pos];
//		}
//		else
//		{
//			val=db.getPPTPrem56to60()[pos];
//		}
//
//		return val;
//	}


    public double getMaturityBenefit(int age, int policYr) {

        double Maturity = 0;
        if (policYr == getPolicy_Term()) {
            if (spaBean.getIsStaff() == false) {
                Maturity = getSumAnnualizedPrem() * 1.1;
            } else if (spaBean.getPremPayingTerm() == 7) {
                Maturity = getSumAnnualizedPrem() * 1.1 + 0.35 * spaBean.getAnnualPrem();
            } else if (spaBean.getPremPayingTerm() == 8) {
                Maturity = getSumAnnualizedPrem() * 1.1 + 0.4 * spaBean.getAnnualPrem();
            } else
                Maturity = getSumAnnualizedPrem() * 1.1 + 0.5 * spaBean.getAnnualPrem();
        }
        //System.out.println("Maturity " +Maturity);
        return Maturity;
    }


//			{
//				if(spaBean.getPremPayingTerm()==10)
//				{
//					PrvMaturity = getMaturityBenefit(age, policYr-1);
//					for(int i=1;i<=10;i++)
//					{
//						AnnualPremSum += getAnnualized_premium(i);
//					}
//					Maturity = PrvMaturity + AnnualPremSum;
////					System.out.println(" PrvMaturity " +PrvMaturity);
////					System.out.println("AnnualPremSum " +AnnualPremSum);
//
//					if (spaBean.getIsStaff()==true)
//					{
//						//System.out.println("getIsStaff " + spaBean.getIsStaff());
//						if(spaBean.getPremPayingTerm()==6)
//						{
//							Maturity += 0.3*spaBean.getAnnualPrem();
//						}
//						else
//						if(spaBean.getPremPayingTerm()==8)
//						{
//							Maturity += 0.4*spaBean.getAnnualPrem();
//						}
//						else
//						{
//							Maturity += 0.5*spaBean.getAnnualPrem();
//							//System.out.println("0.5*spaBean.getAnnualPrem() " +0.5*spaBean.getAnnualPrem());
//						}
//					}
////					else {
////					if(spaBean.getGuaranteedpayoutfreq().equals("Monthly"))
////					{
////						Maturity +=getGuaranteedIncomeAMT(age) *12;
////					}else if(spaBean.getGuaranteedpayoutfreq().equals("Quarterly"))
////					{
////						Maturity +=getGuaranteedIncomeAMT(age)*4;
////					}else if(spaBean.getGuaranteedpayoutfreq().equals("Half-Yearly"))
////					{
////						Maturity +=getGuaranteedIncomeAMT(age)*2;
////					}else
////					{
////						Maturity +=getGuaranteedIncomeAMT(age);
////						System.out.println("getGuaranteedIncomeAMT(age) " +getGuaranteedIncomeAMT(age));
////					}
////
////				    }
//				}
//			}
//
//			else if(policYr == 30)
//			{
//				if(spaBean.getPremPayingTerm() != 10) {
//				PrvMaturity = getMaturityBenefit(age, policYr-1);
//				for(int i=1;i<=10;i++)
//				{
//					AnnualPremSum += getAnnualized_premium(i);
//				}
//				Maturity = PrvMaturity + AnnualPremSum;
//
//				if (spaBean.getIsStaff()==true)
//				{
//					//System.out.println("getIsStaff " + spaBean.getIsStaff());
//					if(spaBean.getPremPayingTerm()==6)
//					{
//						Maturity += 0.3*spaBean.getAnnualPrem();
//					}
//					else
//					if(spaBean.getPremPayingTerm()==8)
//					{
//						Maturity += 0.4*spaBean.getAnnualPrem();
//					}
//					else
//					{
//						Maturity += 0.5*spaBean.getAnnualPrem();
//					}
//
//			    }
//			 }
//			}
//			else {
//				if(spaBean.getGuaranteedpayoutfreq().equals("Monthly"))
//				{
//					Maturity=getGuaranteedIncomeAMT(age) *12;
//				}else if(spaBean.getGuaranteedpayoutfreq().equals("Quarterly"))
//				{
//					Maturity=getGuaranteedIncomeAMT(age)*4;
//				}else if(spaBean.getGuaranteedpayoutfreq().equals("Half-Yearly"))
//				{
//					Maturity=getGuaranteedIncomeAMT(age)*2;
//				}else
//					Maturity=getGuaranteedIncomeAMT(age);
//			    }
//
//		}


    //		return Maturity;
//	}
    //policy term AH
    public double getPolicyTerm() {
        double PolicyTM = 0;
        if (spaBean.getPremfreq().equalsIgnoreCase("Monthly")) {
            PolicyTM = 12;
        } else if (spaBean.getPremfreq().equalsIgnoreCase("Half-Yearly")) {
            PolicyTM = 6;
        } else if (spaBean.getPremfreq().equalsIgnoreCase("Quarterly")) {
            PolicyTM = 3;
        } else
            PolicyTM = 1;

        return PolicyTM;
    }

    public double getAG(int policyYr) {
        double AG = 0;
        AG = 12 * policyYr;

        return AG;
    }

    public String getDeathBenefitDB() {
        String DeathBenefitDB = null;
        DeathBenefitDB = (spaBean.getPremPayingTerm() +
                "," + spaBean.getpayoutPeriod() + "," + ((int) getPolicy_Term())).trim();
        return DeathBenefitDB;

    }

    public double getTValue(int age, int policyYr) {
        double survival = 0;


        if (spaBean.getGuaranteedpayoutfreq().equalsIgnoreCase("Half-Yearly")) {
            survival = 2;
        } else if (spaBean.getGuaranteedpayoutfreq().equalsIgnoreCase("Monthly")) {
            survival = 12;
        } else if (spaBean.getGuaranteedpayoutfreq().equalsIgnoreCase("Quarterly")) {
            survival = 4;
        } else
            survival = 1;


        survival = getGuaranteedIncomeAMT(age) * survival;
        return survival;
    }

    public double getSumDeathBenefit(int age, int policyYr, double Annualizedprem) {
        double DeathBenefit = 0;
        int pos = 0;
        double val3 = 0;
        //System.out.println("policyYr " +policyYr);
        if (policyYr <= getPolicy_Term()) {

            //System.out.println("518");
            String[] ardb = db.getDeathBenefitAr();
            double[] ardb1 = db.getDeathBenefitGuaranteed();
            outerloop1:
            for (int i = 1; i <= 468; i++) {
                for (int j = 0; j < ardb.length; j++) {

                    if (getAG(policyYr) == i && getDeathBenefitDB().equals(ardb[j])) {
                        val3 = ardb1[pos];
                        break outerloop1;

                    }
                    pos++;
                }

            }
//			System.out.println("getDeathBenefitDB() " +getDeathBenefitDB());
//			System.out.println("getAG(policyYr) " +getAG(policyYr));
//			System.out.println("536 " +val3);
            val3 = val3 * getTValue(age, policyYr);

            //System.out.println("543 " +val3);
            pos = 0;
            double[] ardb2 = db.getDeathBenefitMaturity();

            outerloop2:
            for (int i = 1; i <= 468; i++) {
                for (int j = 0; j < ardb.length; j++) {
                    if (getAG(policyYr) == i && getDeathBenefitDB().equals(ardb[j])) {
                        //System.out.println("549 " +ardb2[pos]);
                        val3 += (ardb2[pos] * (getAnnualized_premium(policyYr) * 1.1));
                        break outerloop2;

                    }
                    pos++;
                }

            }


        }

        //System.out.println("val3 567 " + val3);
        DeathBenefit = Math.max(Math.max(spaBean.getAnnualPrem() * 11, 1.05 * Annualizedprem), val3);
//		System.out.println("spaBean.getAnnualPrem()*11 " +spaBean.getAnnualPrem()*11);
//		System.out.println("1.05*sumAnnualizedprem " +1.05*sumAnnualizedprem);

        if (spaBean.getIsStaff() == true) {
            if (spaBean.getPremPayingTerm() == 7) {
                //System.out.println("0.35*spaBean.getAnnualPrem() " +0.35*spaBean.getAnnualPrem());
                DeathBenefit += 0.35 * spaBean.getAnnualPrem();
            } else if (spaBean.getPremPayingTerm() == 8) {
                //System.out.println("0.4*spaBean.getAnnualPrem() " +0.4*spaBean.getAnnualPrem());

                DeathBenefit += 0.4 * spaBean.getAnnualPrem();
            } else
                //System.out.println("0.5*spaBean.getAnnualPrem() " +0.5*spaBean.getAnnualPrem());

                DeathBenefit += 0.5 * spaBean.getAnnualPrem();

        }
        //System.out.println("DeathBenefit " +DeathBenefit);
        return DeathBenefit;
    }


    public double getDeathBenefit(int age, int policyYr, double sumAnnualizedprem) {
        double DeathBenefit = 0;
        int pos = 0;
        double val3 = 0;
        //System.out.println("policyYr " +policyYr);
        if (policyYr <= getPolicy_Term()) {

            //System.out.println("518");
            String[] ardb = db.getDeathBenefitAr();
            double[] ardb1 = db.getDeathBenefitGuaranteed();
            outerloop1:
            for (int i = 1; i <= 468; i++) {
                for (int j = 0; j < ardb.length; j++) {

                    if (getAG(policyYr) == i && getDeathBenefitDB().equals(ardb[j])) {
                        val3 = ardb1[pos];
                        break outerloop1;

                    }
                    pos++;
                }

            }
//			System.out.println("getDeathBenefitDB() " +getDeathBenefitDB());
//			System.out.println("getAG(policyYr) " +getAG(policyYr));
//			System.out.println("536 " +val3);
            val3 = val3 * getTValue(age, policyYr);

            //System.out.println("543 " +val3);
            pos = 0;
            double[] ardb2 = db.getDeathBenefitMaturity();

            outerloop2:
            for (int i = 1; i <= 468; i++) {
                for (int j = 0; j < ardb.length; j++) {
                    if (getAG(policyYr) == i && getDeathBenefitDB().equals(ardb[j])) {
                        //System.out.println("549 " +ardb2[pos]);
                        val3 += (ardb2[pos] * (getSumAnnualizedPrem() * 1.1));
                        break outerloop2;

                    }
                    pos++;
                }

            }


        }

        //System.out.println("val3 567 " + val3);
        DeathBenefit = Math.max(Math.max(spaBean.getAnnualPrem() * 11, 1.05 * sumAnnualizedprem), val3);
//		System.out.println("spaBean.getAnnualPrem()*11 " +spaBean.getAnnualPrem()*11);
//		System.out.println("1.05*sumAnnualizedprem " +1.05*sumAnnualizedprem);

        if (spaBean.getIsStaff() == true) {
            if (spaBean.getPremPayingTerm() == 7) {
                //System.out.println("0.35*spaBean.getAnnualPrem() " +0.35*spaBean.getAnnualPrem());
                DeathBenefit += 0.35 * spaBean.getAnnualPrem();
            } else if (spaBean.getPremPayingTerm() == 8) {
                //System.out.println("0.4*spaBean.getAnnualPrem() " +0.4*spaBean.getAnnualPrem());

                DeathBenefit += 0.4 * spaBean.getAnnualPrem();
            } else
                //System.out.println("0.5*spaBean.getAnnualPrem() " +0.5*spaBean.getAnnualPrem());

                DeathBenefit += 0.5 * spaBean.getAnnualPrem();

        }
        //System.out.println("DeathBenefit " +DeathBenefit);
        return DeathBenefit;
    }


//		else
//
//
//
//			if (spaBean.getIsStaff()==true)
//			{
//				//System.out.println("getIsStaff " + spaBean.getIsStaff());
//				if(spaBean.getPremPayingTerm()==6)
//				{
//					DeathBenefit += 0.3*spaBean.getAnnualPrem();
//				}
//				else
//				if(spaBean.getPremPayingTerm()==8)
//				{
//					DeathBenefit += 0.4*spaBean.getAnnualPrem();
//				}
//				else
//				{
//					DeathBenefit += (0.5*spaBean.getAnnualPrem());
//				}
//			}
//		}
//
//		return Double.parseDouble(cfap.getRoundUp(cfap.getStringWithout_E(DeathBenefit)));
//
//	}


    public double getMinGuaranteedSurrenderValue(int age, int policyYr, double Sumsurvival) {
        int pos = 0;

        double MinSurVal = 0;
        //System.out.println("policyYr " +policyYr);
        if (policyYr <= getPolicy_Term()) {

            outerloop:
            for (int i = 1; i <= 39; i++) {
                for (int j = 0; j < 11; j++) {
                    if (getPolicy_Term() == db.getGSVFactorPT()[j] && policyYr == i) {

                        MinSurVal = db.getGSVFactor()[pos];
                        break outerloop;

                    }
                    pos++;
                }


            }
            //System.out.println("MinSurVal " +MinSurVal);

            MinSurVal = MinSurVal * getAnnualized_premium(1) * Math.min(policyYr, spaBean.getPremPayingTerm());

            //System.out.println(" getAnnualized_premium(policyYr) " + getAnnualized_premium(policyYr));
            if (policyYr == 1) {
                return Math.max(0, MinSurVal);
            } else {

                return Math.max((MinSurVal - Sumsurvival), 0);
            }
        }
        return 0;

    }


//	public double getSSVFactor(int policy_year)
//	{
//
//		double val = 0;
//		if(spaBean.getPremPayingTerm()==6)
//		{
//
//			int row = 0;
//			for(int i = 11; i<= 60;i++) {
//				for(int j = 1;j <= 7;j++) {
//					if(spaBean.getAge() == i && policy_year == j) {
//						val = db.getSSVFactorRate6Years()[(row * 7) + (j - 1)];
//						break;
//					}
//				}
//				row++;
//			}
//		}
//		else if(spaBean.getPremPayingTerm()==8)
//		{
//
//			int row = 0;
//			for(int i = 9; i<= 60;i++) {
//				for(int j = 1;j <= 9;j++) {
//					if(spaBean.getAge() == i && policy_year == j) {
//						val = db.getSSVFactorRate8Years()[(row * 9) + (j - 1)];
//						break;
//					}
//				}
//				row++;
//			}
//		}
//		else
//		{
//
//			int row = 0;
//			for(int i = 7; i<= 60;i++) {
//				for(int j = 1;j <= 11;j++) {
//					if(spaBean.getAge() == i && policy_year == j) {
//						val = db.getSSVFactorRate10Years()[(row * 11) + (j - 1)];
//						break;
//					}
//				}
//				row++;
//			}
//		}
//
//		return val;
//	}

    public double getTotalReducAmt(int age, int policyYr) {
        double TotalReducAmt = 0;
        double ppt = ((double) policyYr / (double) spaBean.getPremPayingTerm());
        TotalReducAmt = getTValue(age, policyYr) * (Math.min(ppt, 1.0)) *
                spaBean.getpayoutPeriod();

//		System.out.println("getTValue(age, policyYr) " +getTValue(age, policyYr));
//		System.out.println("policyYr**" +policyYr);
//		System.out.println("spaBean.getPremPayingTerm() " +spaBean.getPremPayingTerm());
//		System.out.println("policyYr/spaBean.getPremPayingTerm()****" +ppt);
//		System.out.println("spaBean.getPremPayingTerm() " +spaBean.getPremPayingTerm());
//
//		System.out.println("spaBean.getpayoutPeriod() " +spaBean.getpayoutPeriod());
//        System.out.println("TotalReducAmt " +TotalReducAmt);
        return TotalReducAmt;
    }

    public double getTotalReducGuaranteedAmt(int age, int policyYr, double SurvivalBenefitSum) {
        double TotalReducGuaranteedAmt = 0;
        double ppt = ((double) policyYr / (double) spaBean.getPremPayingTerm());
        TotalReducGuaranteedAmt = SurvivalBenefitSum * (Math.min(ppt, 1.0));
        //System.out.println("TotalReducGuaranteedAmt " +TotalReducGuaranteedAmt);
        return TotalReducGuaranteedAmt;
    }

    public double getTotalOutstandingAMT(int age, int policyYr, double SurvivalBenefitSum) {
        double TotalOutstandingAMT = 0;
        TotalOutstandingAMT = getTotalReducAmt(age, policyYr) - getTotalReducGuaranteedAmt(age, policyYr, SurvivalBenefitSum);
        //		System.out.println("TotalOutstandingAMT** " +TotalOutstandingAMT);
        return TotalOutstandingAMT;
    }


    public double getSpecialSurrenderValue(int age, int policyYr, double AnnualizePremSum, double SurvivalBenefitSum) {

        double SpecialSurValue = 0;
        int pos = 0;
        if (policyYr <= getPolicy_Term()) {
            outerloop:
            for (int i = 1; i <= 39; i++) {
                for (int j = 0; j < 11; j++) {
                    if (getPolicy_Term() == db.getGSVFactorPT()[j] && policyYr == i) {
                        SpecialSurValue = db.getSSVFactor()[pos];
                        break outerloop;

                    }
                    pos++;
                }


            }
//		System.out.println("policyYr " +policyYr);
//		System.out.println("SpecialSurValue801 " +SpecialSurValue);
            SpecialSurValue = SpecialSurValue * (getTotalOutstandingAMT(age, policyYr, SurvivalBenefitSum) + (1.1 * AnnualizePremSum));
//		System.out.println("getTotalOutstandingAMT(age,policyYr) " +getTotalOutstandingAMT(age,policyYr));
//		System.out.println("SpecialSurValue8011 " +SpecialSurValue);
//		System.out.println("(1.1*AnnualizePremSum) " +(1.1*AnnualizePremSum));
        }

        return SpecialSurValue;

    }

    public double getPolicyYr() {
        double PolicyYr = 0;
        if (spaBean.getPremPayingTerm() == 10) {
            PolicyYr = 25;
        } else
            PolicyYr = 30;

        return PolicyYr;

    }

    public double getMinesOccuInterest(double SumAssured) {
        return (SumAssured / 1000) * 2;
    }

    public double getServiceTaxMines(double MinesOccuInterest) {
        double a = 0;
        a = Double.parseDouble(cfap.getRound(cfap.getRoundOffLevel2(cfap.getStringWithout_E(MinesOccuInterest * (spaProp.SBCServiceTax)))));
        System.out.println("a " + a);
        return a;

    }

    public double getBackDateInterest(double grossPremium, String bkDate) {

        try {

            // double indigoRate=7.5;
            /**
             * Modified by Akshaya. Rate Change from 1-APR-2015
             */
            double ST = 0;

            //  ST = 0.0225;

            /******************* Modified by Akshaya on 14-APR-2015 start **********/
//			double indigoRate = 10;

            /******************* Modified by Saurabh Jain on 08-APR-2019 start **********/
            //double indigoRate = 8.75;
            double indigoRate = 6.50;
            /******************* Modified by Saurabh Jain on 08-APR-2019 end **********/

            double ServiceTaxValue = (ST + 1) * 100;

            /******************* Modified by Akshaya on 14-APR-2015 end **********/
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

            /******************* Modified by Akshaya on 14-APR-2015 start **********/
            // double netPremWithoutST=Math.round((grossPremium*100)/103.09);
            double netPremWithoutST = Math.round((grossPremium * 100)
                    / ServiceTaxValue);
            /******************* Modified by Akshaya on 14-APR-2015 end **********/
            // System.out.println("netPremWithoutST "+netPremWithoutST);
            int days = cfap.setDate(date, strBackDate);
            // System.out.println("no of days "+days);
            double monthsBetween = cfap.roundDown(
                    (double) days / 365 * 12, 0);
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


    public double getCompoundAmount(double monthsBetween, double netPremWithoutST, double indigoRate, int compoundFreq) {
        double compoundAmount = netPremWithoutST * Math.pow((1 + (indigoRate / (100 * compoundFreq))), (compoundFreq * (monthsBetween / 12))) - netPremWithoutST;
        return compoundAmount;
//		 ////System.out.println("compoundAmount "+compoundAmount);
    }         //End


}
