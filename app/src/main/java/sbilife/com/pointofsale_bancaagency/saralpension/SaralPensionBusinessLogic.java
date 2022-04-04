package sbilife.com.pointofsale_bancaagency.saralpension;

import sbilife.com.pointofsale_bancaagency.common.CommonForAllProd;


class SaralPensionBusinessLogic {

    private CommonForAllProd cfap = null;
    private SaralPensionBean saralPensionBean = null;
    private SaralPensionProperties prop = null;
    private SaralPensionDB objDB = null;

    public SaralPensionBusinessLogic(SaralPensionBean saralPensionBean) {
        // TODO Auto-generated constructor stub
        cfap = new CommonForAllProd();
        this.saralPensionBean = saralPensionBean;
        prop = new SaralPensionProperties();
        objDB = new SaralPensionDB();
    }

    public double getBasicPremium() {
        double SA = saralPensionBean.getBasicSA();
        // System.out.println("SA "+SA);
        // System.out.println("rate "+getBasicPremiumRate());
        return (SA * getBasicPremiumRate()) / 1000;
    }

    private double getBasicPremiumRate() {
        SaralPensionDB sp_Db = new SaralPensionDB();
        int arrCount = 0;
        String prStr, pr = null;
        String[] prStrArr;
        double prDouble = 0;
        // if(saralPensionBean.getPremFreq().equals("Single"))
        // {prStr=sp_Db.getPremiumRate_Arr("Single");}
        // else
        // {prStr=sp_Db.getPremiumRate_Arr("Regular");}
        // prStrArr=cfap.split(prStr, ",");

        if (saralPensionBean.getPremFreq().equals("Single")) {
            prStr = sp_Db.getPremiumRate_Arr("Single");
            prStrArr = cfap.split(prStr, ",");
            for (int i = 5; i <= 40; i++) {
                if (i == saralPensionBean.getBasicTerm()) {
                    // System.out.println("i "+i);
                    // System.out.println("term "+saralPensionBean.getBasicTerm());
                    pr = prStrArr[arrCount];
                    prDouble = Double.parseDouble(pr);
                    break;
                }
                arrCount++;
            }
        } else {
            prStr = sp_Db.getPremiumRate_Arr("regular");
            prStrArr = cfap.split(prStr, ",");
            for (int i = 10; i <= 40; i++) {
                if (i == saralPensionBean.getBasicTerm()) {
                    // System.out.println("i "+i);
                    // System.out.println("term "+saralPensionBean.getBasicTerm());
                    pr = prStrArr[arrCount];
                    prDouble = Double.parseDouble(pr);
                    break;
                }
                arrCount++;
            }
        }
        return prDouble;

    }

    public double getDiscount() {
        double disc = 0;
        if (saralPensionBean.getStaffDisc()) {
            if (saralPensionBean.getPremFreq().equals("Single")) {
                disc = getBasicPremium() * 0.02;
            } else {
                disc = getBasicPremium() * 0.0225;
            }
        }
        return disc;
    }

    public double getModalLoading(double annualPremWithDiscount) {
        if (saralPensionBean.getPremFreq().equals("Single")
                || saralPensionBean.getPremFreq().equals("Yearly")) {
            return annualPremWithDiscount;
        } else if (saralPensionBean.getPremFreq().equals("Half Yearly")) {
            return annualPremWithDiscount * 50.2 / 100;
        } else {
            return annualPremWithDiscount * 8.4 / 100;
        }
    }

    private double getPTRPremiumRate() {
        SaralPensionDB sp_Db = new SaralPensionDB();
        int arrCount = 0;
        String prStr, pr = null;
        String[] prStrArr;
        double prDouble = 0;

        if (saralPensionBean.getPremFreq().equals("Single")) {
            prStr = sp_Db.getPTRPremiumRate_Arr("Single");
        } else {
            prStr = sp_Db.getPTRPremiumRate_Arr("Regular");
        }
        prStrArr = cfap.split(prStr, ",");
        // Here min age is 18 & max age is 65/min term is 5 & max term is 30
        for (int i = 18; i < 61; i++) {
            for (int j = 5; j < 31; j++) {

                if (i == saralPensionBean.getAge()
                        && j == saralPensionBean.getPtrTerm()) {
                    pr = prStrArr[arrCount];
                    prDouble = Double.parseDouble(pr);
                    break;
                }
                arrCount++;
            }
        }
        return prDouble;
    }

    public double getPTRPremium() {
        double SA = saralPensionBean.getPtrSA();
        double rate = 0;
        if (saralPensionBean.getPremFreq().equals("Single")) {
            rate = getPTRPremiumRate() * 100;
        } else {
            rate = getPTRPremiumRate();
        }
        return (SA * rate / 100000);
    }

    public double getPTRDiscount() {
        double disc = 0;
        if (saralPensionBean.getStaffDisc()) {
            if (saralPensionBean.getPremFreq().equals("Single")) {
                disc = getPTRPremium() * 0.02;
            } else {
                disc = getPTRPremium() * 0.0225;
            }
        }
        return disc;
    }

    public double getPTRModalLoading(double annualPTRPremWithDiscount) {
        if (saralPensionBean.getPremFreq().equals("Single")
                || saralPensionBean.getPremFreq().equals("Yearly")) {
            return annualPTRPremWithDiscount;
        } else if (saralPensionBean.getPremFreq().equals("Half Yearly")) {
            return annualPTRPremWithDiscount * 52 / 100;
        } else {
            return annualPTRPremWithDiscount * 8.9 / 100;
        }
    }

    /**
     * As per the changes from 1st Apr 2015, by Vrushali Chaudhari
     *
     */

    /*** modified by Akshaya on 20-MAY-16 start **/
    // public double getServiceTax(double totalPremiumWithoutST)
    // {
    // if(saralPensionBean.getJkResident()==true)
    // {return totalPremiumWithoutST * prop.serviceTaxJkResident;}
    // else
    // {return totalPremiumWithoutST * prop.serviceTax;}
    // }
    //Added by sujata on 09-01-2020
    public double getServiceTax(double modalLoading, boolean JKResident, boolean state, String type) {
        double basicST = 0, sbcST = 0, kkcST = 0, basicJkST = 0, keralaST = 0, premST;


        if (type.equals("basic")) {
            if (state == true) {
                basicST = Double.parseDouble(cfap.getRoundUp(cfap.getStringWithout_E(modalLoading * (prop.servtx))));
                //basicST = basicST-modalLoading;
            }
            //				return Double.parseDouble(cfap.getRoundUp(cfap.getStringWithout_E(modalLoading*prop.serviceTaxJkResident)));
            else {
                basicST = Double.parseDouble(cfap.getRoundUp(cfap.getStringWithout_E(modalLoading * (prop.serviceTax))));
                System.out.println("basicST 1 " + (basicST));
                //basicST=(basicST-modalLoading);
            }

            basicST = basicST + (Double.parseDouble(cfap.getRoundUp(cfap.getStringWithout_E(modalLoading * (prop.serviceTax)))));
            System.out.println("basicST " + (basicST));
            return (basicST);
        } else if (type.equals("SBC")) {
            if (JKResident == true)
                return 0;
            else {
                return 0;
                //		return Double.parseDouble(cfap.getRoundUp(cfap.getStringWithout_E(modalLoading*prop.SBCServiceTax)));
            }
        }
        //  Added By Saurabh Jain on 14/05/2019 Start
        else if (type.equals("KERALA")) {
            return 0;
            //		return Double.parseDouble(cfap.getRoundUp(cfap.getStringWithout_E(modalLoading*prop.kerlaServiceTax)));
        }
        //  Added By Saurabh Jain on 14/05/2019 End
        else //KKC
        {
            if (JKResident == true)
                return 0;
            else {
                return 0;
                //				return Double.parseDouble(cfap.getRoundUp(cfap.getStringWithout_E(modalLoading*prop.KKCServiceTax)));
            }
        }

    }


    public double getServiceTaxSecondYear(double modalLoading, boolean JKResident, boolean state, String type) {
        double basicST = 0, sbcST = 0, kkcST = 0, basicJkST = 0, keralaST = 0;

        if (type.equals("basic")) {
            if (state == true) {
                basicST = Double.parseDouble(cfap.getRoundUp(cfap.getStringWithout_E(modalLoading * (prop.kerlaServiceTaxSecondYear))));

            }
            //					return Double.parseDouble(cfap.getRoundUp(cfap.getStringWithout_E(totalPremiumWithoutST*prop.serviceTaxJkResident)));
            else {
                basicST = Double.parseDouble(cfap.getRoundUp(cfap.getStringWithout_E(modalLoading * (prop.serviceTaxSecondYear))));
                //					return Double.parseDouble(cfap.getRoundUp(cfap.getStringWithout_E(totalPremiumWithoutST*prop.serviceTaxSecondYear)));
            }
            basicST = basicST + (Double.parseDouble(cfap.getRoundUp(cfap.getStringWithout_E(modalLoading * (prop.serviceTaxSecondYear)))));

            return basicST;
        } else if (type.equals("SBC")) {
            if (JKResident == true)
                return 0;
            else {
                return 0;
                //return Double.parseDouble(cfap.getRoundUp(cfap.getStringWithout_E(totalPremiumWithoutST*prop.SBCServiceTaxSecondYear)));
            }
        }
        //  Added By Saurabh Jain on 14/05/2019 Start
        else if (type.equals("KERALA")) {
            return 0;
            //return Double.parseDouble(cfap.getRoundUp(cfap.getStringWithout_E(totalPremiumWithoutST*prop.kerlaServiceTaxSecondYear)));

        }
        //  Added By Saurabh Jain on 14/05/2019 End
        else //KKC
        {
            if (JKResident == true)
                return 0;
            else {
                return 0;
                //return Double.parseDouble(cfap.getRoundUp(cfap.getStringWithout_E(totalPremiumWithoutST*prop.KKCServiceTaxSecondYear)));
            }
        }

    }


    /*************Rider service tax*********************/

    public double getServiceTaxRiderBasic(String type, boolean PTR_Status, double PTR_ModalPremiumWithoutDiscount) {
        double basicST = 0;
        if (PTR_Status == true) {
            basicST = Double.parseDouble(cfap.getRoundUp(cfap.getStringWithout_E(PTR_ModalPremiumWithoutDiscount)));
            return basicST;
        } else {
            return 0;
        }
    }

    public double getServiceTaxRider(double premiumSingleInstBasicRider, boolean JKResident, boolean state, String type) {
        double basicST = 0, sbcST = 0, kkcST = 0, basicJkST = 0, keralaST = 0, premST;


        if (type.equals("basic")) {
            if (state == true) {
                basicST = Double.parseDouble(cfap.getRoundUp(cfap.getStringWithout_E(premiumSingleInstBasicRider * (prop.servtx))));
                //basicST = basicST-modalLoading;
            }
            //				return Double.parseDouble(cfap.getRoundUp(cfap.getStringWithout_E(modalLoading*prop.serviceTaxJkResident)));
            else {
                basicST = Double.parseDouble(cfap.getRoundUp(cfap.getStringWithout_E(premiumSingleInstBasicRider * (prop.serviceTax))));
                System.out.println("basicST 1 " + (basicST));
                //basicST=(basicST-modalLoading);
            }

            basicST = basicST + (Double.parseDouble(cfap.getRoundUp(cfap.getStringWithout_E(premiumSingleInstBasicRider * (prop.serviceTax)))));
            System.out.println("basicST " + (basicST));
            return (basicST);
        } else if (type.equals("SBC")) {
            if (JKResident)
                return 0;
            else {
                return 0;
                //		return Double.parseDouble(cfap.getRoundUp(cfap.getStringWithout_E(modalLoading*prop.SBCServiceTax)));
            }
        }
        //  Added By Saurabh Jain on 14/05/2019 Start
        else if (type.equals("KERALA")) {
            return 0;
            //		return Double.parseDouble(cfap.getRoundUp(cfap.getStringWithout_E(modalLoading*prop.kerlaServiceTax)));
        }
        //  Added By Saurabh Jain on 14/05/2019 End
        else //KKC
        {
            if (JKResident == true)
                return 0;
            else {
                return 0;
                //				return Double.parseDouble(cfap.getRoundUp(cfap.getStringWithout_E(modalLoading*prop.KKCServiceTax)));
            }
        }

    }


//for 2nd year


    public double getServiceTaxRiderSecondYear(double premiumSingleInstBasicRider, boolean JKResident, boolean state, String type) {
        double basicST = 0, sbcST = 0, kkcST = 0, basicJkST = 0, keralaST = 0, premST;


        if (type.equals("basic")) {
            if (state == true) {
                basicST = Double.parseDouble(cfap.getRoundUp(cfap.getStringWithout_E(premiumSingleInstBasicRider * (prop.kerlaServiceTaxSecondYear))));
                //basicST = basicST-modalLoading;
            }
            //				return Double.parseDouble(cfap.getRoundUp(cfap.getStringWithout_E(modalLoading*prop.serviceTaxJkResident)));
            else {
                basicST = Double.parseDouble(cfap.getRoundUp(cfap.getStringWithout_E(premiumSingleInstBasicRider * (prop.serviceTaxSecondYear))));
                System.out.println("basicST 1 " + (basicST));
                //basicST=(basicST-modalLoading);
            }

            basicST = basicST + (Double.parseDouble(cfap.getRoundUp(cfap.getStringWithout_E(premiumSingleInstBasicRider * (prop.serviceTaxSecondYear)))));
            System.out.println("basicST " + (basicST));
            return (basicST);
        } else if (type.equals("SBC")) {
            if (JKResident)
                return 0;
            else {
                return 0;
                //		return Double.parseDouble(cfap.getRoundUp(cfap.getStringWithout_E(modalLoading*prop.SBCServiceTax)));
            }
        }
        //  Added By Saurabh Jain on 14/05/2019 Start
        else if (type.equals("KERALA")) {
            return 0;
            //		return Double.parseDouble(cfap.getRoundUp(cfap.getStringWithout_E(modalLoading*prop.kerlaServiceTax)));
        }
        //  Added By Saurabh Jain on 14/05/2019 End
        else //KKC
        {
            if (JKResident == true)
                return 0;
            else {
                return 0;
                //				return Double.parseDouble(cfap.getRoundUp(cfap.getStringWithout_E(modalLoading*prop.KKCServiceTax)));
            }
        }

    }

    /*** modified by Akshaya on 20-MAY-16 end **/

    public double getGuaranteedVestingBenefit(double sumAssured) {
        double val = 0;
        val = sumAssured + sumAssured * (0.025 * 3 + 0.0275 * 2);
        return val;
    }

    public double getVestingBenefit(int j, double guaranteedVestingBenefit) {
        if (j < saralPensionBean.getBasicTerm()) {
            // System.out.println("percent value "+getPercentValue(j));
            guaranteedVestingBenefit = getPercentValue(j)
                    + guaranteedVestingBenefit;
//				System.out.println("guaranteedVestingBenefit in if " +guaranteedVestingBenefit);
        } else if (j == saralPensionBean.getBasicTerm()) {
            // System.out.println("guaranteedVestingBenefit "+guaranteedVestingBenefit);
            guaranteedVestingBenefit = saralPensionBean.getBasicSA()
                    + guaranteedVestingBenefit;
        }
        return guaranteedVestingBenefit;
    }

    public double getVestingBenefit11(int j, double guaranteedVestingBenefit) {
            /*if(j < saralPensionBean.getBasicTerm())
			{
//				System.out.println("percent value "+getPercentValue(j));
				guaranteedVestingBenefit=getPercentValue(j)+guaranteedVestingBenefit;
//				System.out.println("guaranteedVestingBenefit in if " +guaranteedVestingBenefit);
			}
			else*/
        if (j == saralPensionBean.getBasicTerm()) {
//				System.out.println("guaranteedVestingBenefit "+guaranteedVestingBenefit);
            guaranteedVestingBenefit = saralPensionBean.getBasicSA() + (saralPensionBean.getBasicSA() * .025 * 3) + (saralPensionBean.getBasicSA() * .0275 * 2);
        } else {

            guaranteedVestingBenefit = 0;

        }
        return guaranteedVestingBenefit;
    }

    public double getPercentValue(int i) {
        if (i == 1) {
            return 0.025 * saralPensionBean.getBasicSA();
        } else if (i == 2) {
            return 0.025 * saralPensionBean.getBasicSA();
        } else if (i == 3) {
            return 0.025 * saralPensionBean.getBasicSA();
        } else if (i == 4) {
            return 0.0275 * saralPensionBean.getBasicSA();
        } else if (i == 5) {
            return 0.0275 * saralPensionBean.getBasicSA();
        } else {
            return 0;
        }
    }

//	public double getNonGuaranVestingBenefit(double percent,double vestingBenefit,int j,double nonGuaranVestingBenefit)
    // {
    // if(j<=5)
    // {
    // nonGuaranVestingBenefit=vestingBenefit;
    // }
    // else if(j>5 && j< saralPensionBean.getBasicTerm())
    // {
//			nonGuaranVestingBenefit=percent * saralPensionBean.getBasicSA() +nonGuaranVestingBenefit;
    // }
    // else if(j==saralPensionBean.getBasicTerm())
    // {
//			nonGuaranVestingBenefit=percent* saralPensionBean.getBasicSA() +nonGuaranVestingBenefit+saralPensionBean.getBasicSA();
    // }
    // return nonGuaranVestingBenefit;
    // }

    public double getNonGuaranVestingBenefit(String percent, double bonus_rate,
                                             double vestingBenefit, int j, double nonGuaranVestingBenefit) {
        if (j <= 3) {
            if (percent.equals("4%"))
                nonGuaranVestingBenefit = Math.max(0.025, bonus_rate)
                        * saralPensionBean.getBasicSA()
                        + nonGuaranVestingBenefit;
            else
//			nonGuaranVestingBenefit=Math.max(0.025,bonus_rate)*saralPensionBean.getBasicSA()*j+nonGuaranVestingBenefit;
                nonGuaranVestingBenefit = Math.max(0.025, bonus_rate) * saralPensionBean.getBasicSA() * j;
        } else if (j <= 5) {

            if (saralPensionBean.getBasicTerm() == j)
                nonGuaranVestingBenefit = ((Math.max(0.0275, bonus_rate) * saralPensionBean.getBasicSA() + nonGuaranVestingBenefit));
            else if (j == 4) {
                if (percent.equals("4%"))
                    nonGuaranVestingBenefit = Math.max(0.0275, bonus_rate)
                            * saralPensionBean.getBasicSA()
                            + nonGuaranVestingBenefit;
                else
//						nonGuaranVestingBenefit=Math.max(0.0275,bonus_rate)*saralPensionBean.getBasicSA()*j+nonGuaranVestingBenefit;
                    nonGuaranVestingBenefit = Math.max(0.0275, bonus_rate) * saralPensionBean.getBasicSA() * j;

            } else
                nonGuaranVestingBenefit = Math.max(0.0275, bonus_rate)
                        * saralPensionBean.getBasicSA()
                        + nonGuaranVestingBenefit;

        } else if (j > 5 && j < saralPensionBean.getBasicTerm()) {
            nonGuaranVestingBenefit = bonus_rate * saralPensionBean.getBasicSA() + nonGuaranVestingBenefit;
        } else if (j == saralPensionBean.getBasicTerm()) {
            if (percent.equals("4%"))
                //nonGuaranVestingBenefit=((bonus_rate*saralPensionBean.getBasicSA()+nonGuaranVestingBenefit)*1.15) +saralPensionBean.getBasicSA();
                nonGuaranVestingBenefit = ((bonus_rate * saralPensionBean.getBasicSA()) + nonGuaranVestingBenefit);
            else
                nonGuaranVestingBenefit = ((bonus_rate * saralPensionBean.getBasicSA()) + nonGuaranVestingBenefit);
            System.out.println("bonus_rate " + bonus_rate);
            System.out.println("nonGuaranVestingBenefit " + nonGuaranVestingBenefit);
        }
        return nonGuaranVestingBenefit;
    }

    /********* Added by Priyanka Warekar - 14-01-2015 ********/

    /************Added by sujata on 17-01-2020************/
//	public double BaseNewPrem(double sumassured)
//	{
//		if (saralPensionBean.getPremFreq().equals("Yearly") || saralPensionBean.getPremFreq().equals("Half Yearly") || saralPensionBean.getPremFreq().equals("Half Yearly"))
//		{
//
//		}
//	}

    //17-01-2020
    public double getTotalBasePremPaid(/*double totalPremiumWithoutST,*/double totalPremiumWithoutservicetax, int year_F) {
        if (saralPensionBean.getPremFreq().equals("Single")) {
            if (year_F == 1)
                return Double.parseDouble(cfap.getRoundOffLevel2(cfap
                        .getStringWithout_E(totalPremiumWithoutservicetax)));
            else
                return 0;

        } else if (saralPensionBean.getPremFreq().equals("Yearly"))
            return Double.parseDouble(cfap.getRoundOffLevel2(cfap
                    .getStringWithout_E(totalPremiumWithoutservicetax)));
        else if (saralPensionBean.getPremFreq().equals("Half Yearly"))
            return Double.parseDouble(cfap.getRoundOffLevel2(cfap
                    .getStringWithout_E(totalPremiumWithoutservicetax * 2)));
        else
            return Double.parseDouble(cfap.getRoundOffLevel2(cfap
                    .getStringWithout_E(totalPremiumWithoutservicetax * 12)));
    }

    public double getValueOfK(double _totalBasePremiumPaid, double k) {
        return ((_totalBasePremiumPaid + k) * 1.0025);
    }

    public double getGuarnteedDeathBenefit(int year_F,
                                           double cummulativTotalBasePremiumPaid, double guarnVestingBenefit,
                                           double _cal_of_colm_k) {
        if (year_F < saralPensionBean.getBasicTerm())

            return Math.max((_cal_of_colm_k + guarnVestingBenefit),
                    (1.05 * cummulativTotalBasePremiumPaid));
        else if (year_F == saralPensionBean.getBasicTerm())
            return Math.max(
                    (_cal_of_colm_k + guarnVestingBenefit - saralPensionBean
                            .getBasicSA()),
                    (1.05 * cummulativTotalBasePremiumPaid));
        else
            return 0;
    }

    public double getGuarnteedDeathBenefit11(int year_F,
                                             double cummulativTotalBasePremiumPaid, double guarnVestingBenefit,
                                             double _cal_of_colm_k, String _getGuarDeathBenForMat) {

        double a = Double.parseDouble(_getGuarDeathBenForMat);
        if (year_F <= saralPensionBean.getBasicTerm()) {

            return (guarnVestingBenefit + a);

        } else {

            return 0;
        }
    }

    public double getPUV(int year_F, double nonGuaranVestingBenefit) {
        if (year_F < saralPensionBean.getBasicTerm()) {
            if (saralPensionBean.getPremFreq().endsWith("Single"))
                return (saralPensionBean.getBasicSA()
                        + nonGuaranVestingBenefit);
            else
                return ((saralPensionBean.getBasicSA()
                        * year_F / saralPensionBean
                        .getBasicTerm()) + nonGuaranVestingBenefit);

        } else if (year_F == saralPensionBean.getBasicTerm()) {
            if (saralPensionBean.getPremFreq().endsWith("Single"))
                return (saralPensionBean.getBasicSA()
                        + nonGuaranVestingBenefit - saralPensionBean
                        .getBasicSA());
            else
                return ((saralPensionBean.getBasicSA()
                        * year_F / saralPensionBean
                        .getBasicTerm())
                        + nonGuaranVestingBenefit - saralPensionBean
                        .getBasicSA());

        } else
            return 0;
    }

//	public double getNonGuarnteedSurrenderBenefit(int year_F, double _PUV) {
//		int a = 0, arrCount = 0;
//		double ab = 0, b = 0;
//
//		double[] regularGSV = objDB.getDiscountFactor_Arr();
//
//		if (year_F > saralPensionBean.getBasicTerm())
//			return 0;
//		else {
//			if (saralPensionBean.getPremFreq().equalsIgnoreCase("Single")) {
//				if (year_F > 1)
//					a = 1;
//				else
//					a = 0;
//			} else {
//				if (year_F > 2)
//					a = 1;
//				else
//					a = 0;
//			}
//
//			for (int i = 1; i <= 40; i++) {
//				for (int j = 5; j <= 40; j++) {
//					if (i == year_F && saralPensionBean.getBasicTerm() == j) {
////						System.out.println(i + " " + j + "  " + arrCount);
//						b = regularGSV[arrCount];
//						break;
//					}
//					arrCount++;
//					// System.out.println(i +" "+ j +" = "+prStrArr[arrCount]);
//				}
//
//			}
////			System.out.println("GSV : " + b + "   " + a + "   " + _PUV);
//			double val  =  Math.round(_PUV * b * a);
//			return Math.round(_PUV * b * a);
//		}
//	}


    //17-01-2020
    public double getNonGuarnteedSurrenderBenefit(int year_F, double PolicyTerm, double sumassured, double _vestingBenefit_4_pr) {
        int a = 0, arrCount = 0;
        double ab = 0, b = 0;
        double t;

        double[] SSVFactor = objDB.getSSVFactor_Arr(saralPensionBean.getPremFreq());

        if (year_F > saralPensionBean.getBasicTerm())
            return 0;
        else {
            if (saralPensionBean.getPremFreq().equalsIgnoreCase("Single")) {
                t = 1;
            } else {
                t = year_F / PolicyTerm;
//				System.out.println("t "+t);
            }

            double Val = (t * sumassured + _vestingBenefit_4_pr);
            //	System.out.println("_vestingBenefit_4_pr " + _vestingBenefit_4_pr);
            //	System.out.println("Val "+Val);

            if (saralPensionBean.getPremFreq().equalsIgnoreCase("Single")) {
                for (int i = 1; i <= 40; i++) {
                    for (int j = 5; j <= 40; j++) {
                        if (i == year_F && saralPensionBean.getBasicTerm() == j) {
//						System.out.println(i + " " + j + "  " + arrCount);
                            b = SSVFactor[arrCount];
                            break;
                        }
                        arrCount++;
                        // System.out.println(i +" "+ j +" = "+prStrArr[arrCount]);
                    }

                }
            } else
                for (int i = 1; i <= 40; i++) {
                    for (int j = 10; j <= 40; j++) {
                        if (i == year_F && saralPensionBean.getBasicTerm() == j) {
//						System.out.println(i + " " + j + "  " + arrCount);
                            b = SSVFactor[arrCount];
                            System.out.println("b " + b);
                            break;
                        }
                        arrCount++;
//				System.out.println("b "+ b);
                    }

                }
//			System.out.println("GSV : " + b + "   " + a + "   " + _PUV);
            double Surr4per = Val * b;

            //	System.out.println("Surr4per " + Surr4per);
            return Surr4per;
        }
    }


    //17-01-2020
    public double getNonGuarnteedSurrenderBenefit8per(int year_F, double PolicyTerm, double sumassured, double _vestingBenefit_8_pr) {
        int a = 0, arrCount = 0;
        double ab = 0, b = 0;
        double t;

        double[] SSVFactor = objDB.getSSVFactor_Arr(saralPensionBean.getPremFreq());

        if (year_F > saralPensionBean.getBasicTerm())
            return 0;
        else {
            if (saralPensionBean.getPremFreq().equalsIgnoreCase("Single")) {
                t = 1;
            } else {
                t = year_F / PolicyTerm;
//				System.out.println("t "+t);
            }

            double Val = (t * sumassured + _vestingBenefit_8_pr);
            //	System.out.println("_vestingBenefit_4_pr " + _vestingBenefit_4_pr);
            //	System.out.println("Val "+Val);

            if (saralPensionBean.getPremFreq().equalsIgnoreCase("Single")) {
                for (int i = 1; i <= 40; i++) {
                    for (int j = 5; j <= 40; j++) {
                        if (i == year_F && saralPensionBean.getBasicTerm() == j) {
                            // System.out.println(i + " " + j + "  " + arrCount);
                            b = SSVFactor[arrCount];
                            break;
                        }
                        arrCount++;
                        // System.out.println(i +" "+ j +" = "+prStrArr[arrCount]);
                    }

                }
            } else
                for (int i = 1; i <= 40; i++) {
                    for (int j = 10; j <= 40; j++) {
                        if (i == year_F && saralPensionBean.getBasicTerm() == j) {
//						System.out.println(i + " " + j + "  " + arrCount);
                            b = SSVFactor[arrCount];
                            System.out.println("b " + b);
                            break;
                        }
                        arrCount++;
//				System.out.println("b "+ b);
                    }

                }
            // System.out.println("GSV : " + b + "   " + a + "   " + _PUV);
            double Surr4per = Val * b;

            //	System.out.println("Surr4per " + Surr4per);
            return Surr4per;
        }
    }

//	public double getGuarnteedSurrenderBenefit(int year_F,
//			double cummulativTotalBasePremiumPaid, double vestingBenefit) {
//		int a = 0;
//		double bonus = 0, b = 0;
//
//		double[] regularGSV;
//		double[] bonusSurrValueFact;
//
//		if (year_F < saralPensionBean.getBasicTerm()) {
//
//			regularGSV = objDB.getGSVFactor_Arr(saralPensionBean.getPremFreq());
//			b = regularGSV[year_F - 1];
//
//			bonusSurrValueFact = objDB.getBonusSurrenderValueFactor_Arr();
//			bonus = bonusSurrValueFact[saralPensionBean.getBasicTerm() - year_F
//					+ 1 - 1];
//
//			if (saralPensionBean.getPremFreq().equalsIgnoreCase("Single")) {
//				if (year_F > 1)
//					a = 1;
//				else
//					a = 0;
//			} else {
//				if (year_F > 2)
//					a = 1;
//				else
//					a = 0;
//			}
//
//			return Math.round(b * cummulativTotalBasePremiumPaid
//					+ vestingBenefit * bonus * a);
//		} else {
//			if (year_F == saralPensionBean.getBasicTerm()) {
//
//				regularGSV = objDB.getGSVFactor_Arr(saralPensionBean
//						.getPremFreq());
//				b = regularGSV[year_F - 1];
//
//				bonusSurrValueFact = objDB.getBonusSurrenderValueFactor_Arr();
//				bonus = bonusSurrValueFact[saralPensionBean.getBasicTerm()
//						- year_F + 1 - 1];
//
//				if (saralPensionBean.getPremFreq().equalsIgnoreCase("Single")) {
//					if (year_F > 1)
//						a = 1;
//					else
//						a = 0;
//				} else {
//					if (year_F > 2)
//						a = 1;
//					else
//						a = 0;
//				}
//
//				return Math.round(b * cummulativTotalBasePremiumPaid
//						+ (vestingBenefit - saralPensionBean.getBasicSA())
//						* bonus * a);
//			} else
//				return 0;
//		}
//
//	}


    //17-01-2020
    public double getGuarnteedSurrenderBenefit(int year_F,
                                               double cummulativTotalBasePremiumPaid, double vestingBenefit, double sumTotalBasePrem) {
        int a = 0;
        double bonus = 0, b = 0, pr;
        int arrCount = 0;
        String prStr;
        double SurrValue;
        double prDouble = 0;
        double[] regularGSV;
        double[] bonusSurrValueFact;

        if (year_F > saralPensionBean.getBasicTerm()) {
            return 0;
        } else {
            regularGSV = objDB.getGSVFactor_Arr(saralPensionBean.getPremFreq());
            b = regularGSV[year_F - 1];

//			bonusSurrValueFact = objDB.getBonusSurrenderValueFactor_Arr();
//			bonus = bonusSurrValueFact[saralPensionBean.getBasicTerm() - year_F
//					+ 1 - 1];

            if (saralPensionBean.getPremFreq().equalsIgnoreCase("Single")) {
                for (int i = 1; i < 41; i++) {

                    if (i == year_F) {
                        pr = regularGSV[arrCount];
                        prDouble = (pr);
                        break;
                    }
                    arrCount++;
                }

                SurrValue = prDouble * sumTotalBasePrem;
            } else {
                for (int i = 1; i < 41; i++) {
                    for (int j = 10; j < 41; j++) {
                        if (i == year_F && j == saralPensionBean.getBasicTerm()) {
                            pr = regularGSV[arrCount];
                            prDouble = (pr);
                            //System.out.println("prDouble " + prDouble);
                            break;
                        }
                        arrCount++;
                    }

                }
                SurrValue = prDouble * sumTotalBasePrem;
                //		System.out.println("SurrValue " + SurrValue);

            }
        }
        return SurrValue;
    }


    public double getSurrenderBenefit_4Pr(int year_F, double _nonGuaranVestingBenefit_4Percent) {

        double a = 0, b = 0;
        double[] regularGSV;
        double prDouble = 0, pr = 0;
        int arrCount = 0;
        double c = saralPensionBean.getBasicTerm();
        double d = saralPensionBean.getBasicSA();
        if (saralPensionBean.getPremFreq().equals("Single")) {

            a = 1;

        } else {

            a = (year_F / c);
        }

        b = ((a * d) + _nonGuaranVestingBenefit_4Percent);

        regularGSV = objDB.getSSVFactor_Arr(saralPensionBean.getPremFreq());

        if (saralPensionBean.getPremFreq().equalsIgnoreCase("Single")) {
            for (int i = 1; i < 41; i++) {
                for (int j = 5; j < 41; j++) {

                    if (i == year_F && j == saralPensionBean.getBasicTerm()) {
                        pr = regularGSV[arrCount];
                        prDouble = (pr);
                        break;
                    }
                    arrCount++;
                }
            }
        } else {
            for (int i = 1; i < 41; i++) {
                for (int j = 10; j < 41; j++) {
                    if (i == year_F && j == saralPensionBean.getBasicTerm()) {
                        pr = regularGSV[arrCount];
                        prDouble = (pr);
                        //System.out.println("prDouble " + prDouble);
                        break;
                    }
                    arrCount++;
                }

            }
        }
        return (b * prDouble);
    }

    public double getSurrenderBenefit_8Pr(int year_F, double _nonGuaranVestingBenefit_8Percent) {

        double a = 0, b = 0;
        double[] regularGSV;
        double prDouble = 0, pr = 0;
        int arrCount = 0;
        double c = saralPensionBean.getBasicTerm();
        double d = saralPensionBean.getBasicSA();
        if (saralPensionBean.getPremFreq().equals("Single")) {

            a = 1;

        } else {

            a = (year_F / c);
        }

        b = ((a * d) + _nonGuaranVestingBenefit_8Percent);

        regularGSV = objDB.getSSVFactor_Arr(saralPensionBean.getPremFreq());

        if (saralPensionBean.getPremFreq().equalsIgnoreCase("Single")) {
            for (int i = 1; i < 41; i++) {
                for (int j = 5; j < 41; j++) {

                    if (i == year_F && j == saralPensionBean.getBasicTerm()) {
                        pr = regularGSV[arrCount];
                        prDouble = (pr);
                        break;
                    }
                    arrCount++;
                }
            }
        } else {
            for (int i = 1; i < 41; i++) {
                for (int j = 10; j < 41; j++) {
                    if (i == year_F && j == saralPensionBean.getBasicTerm()) {
                        pr = regularGSV[arrCount];
                        prDouble = (pr);
                        //System.out.println("prDouble " + prDouble);
                        break;
                    }
                    arrCount++;
                }

            }
        }
        return (b * prDouble);
    }

    /**
     * Added by Vrushali on 06 Aug 2015 Start
     **/
    public double getStaffRebate() {
        double staff_Rebate;
        // staffRebate
        if (saralPensionBean.getStaffDisc()) {
            if (saralPensionBean.getPremFreq().equals("Single"))
                staff_Rebate = 0.02;
            else
                staff_Rebate = 0.0225;
        } else
            staff_Rebate = 0;
        return staff_Rebate;

    }

    /**
     * Added by Vrushali on 06 Aug 2015 End
     **/

    public double getPremiumRate() {
        double b = 0;
        double[] lifetimeIncome_Arr = objDB.getLifeTimeIncome_Arr();

        b = lifetimeIncome_Arr[saralPensionBean.getBasicTerm()
                + saralPensionBean.getAge() - 40];
        //
        // System.out.println((saralPensionBean.getBasicTerm()
        // + saralPensionBean.getAge() - 1)
        // + "    Premium rAte: " + b);
        return b;
    }

    public double getAnnuityAmount(double maxVestingBenefit) {

        int val1 = saralPensionBean.getAge() + saralPensionBean.getBasicTerm();
        double aa = 0;
        if (saralPensionBean.isKerlaDisc()) {

            aa = 0.019;
        } else {

            aa = 0.018;
        }
        double val2 = Math.round(maxVestingBenefit / (1 + aa));
//		System.out.println(" val2: " +val2);
//        double [] PWBarr=objDB.getAnnuityRatesForLifetimeIncome();

        double pr = getpremiumRate_Monthly();

        double a = 0;
        if (val2 < 1000000) {
            a = 0;
        } else if (val2 >= 1000000 && val2 <= 1499999) {
            a = 0.5;
        } else if (val2 >= 1500000) {
            a = 1;
        }

        double val3 = (((pr * 1.035) + a) / (1 - 0.02));
//        System.out.println(" val3: " +val3);
        return Math.round((val2 * val3) / 1000);

        /**************************Modified by SAURABH JAIN on 07/03/2019*******End*************************/
    }

    public double getbonusrate4(String premFreqMode, int policyTerm) {
        double bonusrate4 = 0;

        if (premFreqMode.equals("Single")) {
            // if(policyTerm>=25 )
            // {
            // bonusrate4 = 0.01 ;
            // }
            // else
            // {
            // bonusrate4 = 0.0 ;
            // }

            if (policyTerm >= 5 && policyTerm <= 9) {
                bonusrate4 = 0.0;
            } else if (policyTerm >= 10 && policyTerm <= 14) {
                bonusrate4 = 0.0;
            } else if (policyTerm >= 15 && policyTerm <= 19) {
                bonusrate4 = 0.0;
            } else if (policyTerm >= 20 && policyTerm <= 24) {
                bonusrate4 = 0.0;
            } else if (policyTerm >= 25) {
                bonusrate4 = 0.01;
            }
        } else {

            if (policyTerm >= 5 && policyTerm <= 9) {
                bonusrate4 = 0.0;
            } else if (policyTerm >= 10 && policyTerm <= 14) {
                bonusrate4 = 0.005;
            } else if (policyTerm >= 15 && policyTerm <= 19) {
                bonusrate4 = 0.008;
            } else if (policyTerm >= 20 && policyTerm <= 24) {
                bonusrate4 = 0.009;
            } else if (policyTerm >= 25) {
                bonusrate4 = 0.01;
            }
        }
        return bonusrate4;

    }

    public double getbonusrate8(String premFreqMode, int policyTerm) {
        double bonusrate8 = 0;

        if (premFreqMode.equals("Single")) {

            if (policyTerm >= 5 && policyTerm <= 9) {
                bonusrate8 = 0.037;
            } else if (policyTerm >= 10 && policyTerm <= 14) {
                bonusrate8 = 0.044;
            } else if (policyTerm >= 15 && policyTerm <= 19) {
                bonusrate8 = 0.049;
            } else if (policyTerm >= 20 && policyTerm <= 24) {
                bonusrate8 = 0.053;
            } else if (policyTerm >= 25) {
                bonusrate8 = 0.056;
            }
        } else {

            if (policyTerm >= 5 && policyTerm <= 9) {
                bonusrate8 = 0.0;
            } else if (policyTerm >= 10 && policyTerm <= 14) {
                bonusrate8 = 0.038;
            } else if (policyTerm >= 15 && policyTerm <= 19) {
                bonusrate8 = 0.04;
            } else if (policyTerm >= 20 && policyTerm <= 24) {
                bonusrate8 = 0.042;
            } else if (policyTerm >= 25) {
                bonusrate8 = 0.044;
            }
        }
        return bonusrate8;

    }

    //added by sujata on 13-01-2020
    //17-01-2020
    public double getannulizedPremFinal(int year_F, int ppt, String premfreq, double annualPremWithDiscount) {
        if (premfreq.equals("Single")) {
            ppt = 1;
            if (year_F <= ppt) {
                //System.out.println("Annulized " + get_Premium_WithoutST_WithoutFreqLoading() );
                return annualPremWithDiscount;
            } else {
                return 0;
            }
        } else {
            if (year_F <= ppt) {
                //	System.out.println("Annulized " + annualPremWithDiscount );
                return annualPremWithDiscount;
            } else {
                return 0;
            }
        }
    }

    public double getSurvivalBenefit(int year_F, double policyterm, double sumassured) {
        if (year_F == policyterm) {
            return sumassured;
        } else {
            return 0;
        }
    }

    public double getNonGuarnteedDeathBenefit4per(int year_F, int policyTerm, double sumassured, String premFreqMode) {
        if (year_F > policyTerm) {
            return 0;
        } else {
            double val = sumassured * getbonusrate4(premFreqMode, policyTerm) * year_F;
            //		System.out.println("val " + val);
            return val;
        }

    }

    public double getNonGuarnteedDeathBenefit8per(int year_F, int policyTerm, double sumassured, String premFreqMode) {
        if (year_F > policyTerm) {
            return 0;
        } else {
            double val = sumassured * getbonusrate8(premFreqMode, policyTerm) * year_F;
            //		System.out.println("val " + val);
            return val;
        }

    }

    public double getGuarDeathBenForMat(int year_F, int policyTerm, double _totalBasePremiumPaid, double getGuarDeathBenForMat) {
        double val;
        if (year_F > policyTerm) {
            return 0;
        } else {
            val = (getGuarDeathBenForMat + _totalBasePremiumPaid) * 1.0025;
        }
        return val;
    }

    public double getTotalMaturityBenefit4per(int year_F, int policyTerm, double sumassured, double _nonGuaranVestingBenefit_4Percent, double _getGuarDeathBenForMat, String premFreqMode) {
        double MaxVal, a;
        if (year_F == policyTerm) {
            MaxVal = Math.max(sumassured, _getGuarDeathBenForMat);
            a = MaxVal + _nonGuaranVestingBenefit_4Percent + 0.15 * _nonGuaranVestingBenefit_4Percent;
//			System.out.println("a " + a);
//			System.out.println("_vestingBenefit_4_pr "+_vestingBenefit_4_pr);
        } else {
            return 0;
        }
        return a;
    }

    //17-01-2020
    public double getTotalMaturityBenefit8per(int year_F, int policyTerm, double sumassured, double _nonGuaranVestingBenefit_8Percent, double _getGuarDeathBenForMat, String premFreqMode) {

        double MaxVal, a;
        if (year_F == policyTerm) {
            MaxVal = Math.max(sumassured, _getGuarDeathBenForMat);
            a = MaxVal + _nonGuaranVestingBenefit_8Percent + 0.15 * _nonGuaranVestingBenefit_8Percent;
//			System.out.println("a " + a);
//			System.out.println("_vestingBenefit_4_pr "+_vestingBenefit_4_pr);
        } else {
            return 0;
        }
        return a;
    }

    public double getTotalDeathBen4per(int year_F, int policyTerm, double _getGuarDeathBenForMat, double _nonGuaranVestingBenefit_4Percent, double sumTotalBasePrem) {
        double totDeath4pr, a, b, MaxVal;
        if (year_F <= policyTerm) {
            a = (_getGuarDeathBenForMat + _nonGuaranVestingBenefit_4Percent + 0.15 * _nonGuaranVestingBenefit_4Percent);
            b = (1.05 * sumTotalBasePrem);
            MaxVal = Math.max(a, b);
        } else {
            return 0;
        }
        return MaxVal;
    }

    public double getTotalDeathBen8per(int year_F, int policyTerm, double _getGuarDeathBenForMat, double _nonGuaranVestingBenefit_8Percent, double sumTotalBasePrem) {

        double totDeath8pr, a, b, MaxVal;
        if (year_F <= policyTerm) {
            a = (_getGuarDeathBenForMat + _nonGuaranVestingBenefit_8Percent + 0.15 * _nonGuaranVestingBenefit_8Percent);
            b = (1.05 * sumTotalBasePrem);
            MaxVal = Math.max(a, b);
        } else {
            return 0;
        }
        return MaxVal;
    }

    public double getGuaranteedAddition(int year_F, double sumassured, double sumGuaranteedAddition, int policyTerm) {
        double val, a;
        if (year_F == 1) {
            val = (sumassured * prop.servtx);
        } else {
            val = sumGuaranteedAddition;

        }
        return val;
    }

    public int getSheetGroup() {
        String annuityOption = saralPensionBean.getAnnuityOption();
        if (annuityOption.contains("Option 1.1") ||
                annuityOption.contains("Option 1.2") ||
                annuityOption.contains("Option 1.3") ||
                annuityOption.contains("Option 1.4") ||
                annuityOption.contains("Option 1.5") ||
                annuityOption.contains("Option 1.6") ||
                annuityOption.contains("Option 1.7") ||
                annuityOption.contains("Option 1.8") ||
                annuityOption.contains("Option 1.9") ||
                annuityOption.contains("Option 1.10")) {
            return 1;
        } else if (annuityOption.contains("Option 2.1")) {
            return 2;
        } else if (annuityOption.contains("Option 2.2")) {
            return 3;
        } else if (annuityOption.contains("Option 2.3")) {
            return 4;
        } else if (annuityOption.contains("Option 2.4")) {
            return 5;
        }
		/*else if(annuityOption.contains("Income for a period with Balance Capital Refund for Period of 15 years"))
		{return 6;}*/

        else {
            return 0;
        }
    }

    public double getpremiumRate_Monthly() {
        int sheetGroup = getSheetGroup();

//		System.out.println("Sheet Group : "+sheetGroup);
        if (sheetGroup == 1) {
            double prDouble = 0;

            String[] prStrArr = cfap.split(objDB.getPRArr_SingleLifeAnnuity(), ",");
            String annuityOption = null;
            int optionRank = 0;
            String pr = null;
            int arrCount = 0;
            annuityOption = saralPensionBean.getAnnuityOption();
            //double prDouble=0;

            if (annuityOption.contains("Option 1.10")) {
                optionRank = 10;
            } else if (annuityOption.contains("Option 1.1")) {
                optionRank = 1;
            } else if (annuityOption.contains("Option 1.2")) {
                optionRank = 2;
            } else if (annuityOption.contains("Option 1.3")) {
                optionRank = 3;
            } else if (annuityOption.contains("Option 1.4")) {
                optionRank = 4;
            } else if (annuityOption.contains("Option 1.5")) {
                optionRank = 5;
            } else if (annuityOption.contains("Option 1.6")) {
                optionRank = 6;
            } else if (annuityOption.contains("Option 1.7")) {
                optionRank = 7;
            } else if (annuityOption.contains("Option 1.8")) {
                optionRank = 8;
            } else if (annuityOption.contains("Option 1.9")) {
                optionRank = 9;
            }

            //for (int i=40; i<=80;i++)
            for (int i = 0; i <= 80; i++) {
                for (int j = 1; j <= 10; j++) {
                    if (i == saralPensionBean.getVestingAge() && j == optionRank) {
                        pr = prStrArr[arrCount];
                        prDouble = Double.parseDouble(pr);
                        break;
                    }
                    arrCount++;
                }
            }
//				                System.out.println("*****************Premium Rate:=> "+prDouble);
            //}
            double a = prDouble;
            return prDouble;
        } else if (sheetGroup == 2) {
            //VLOOKUP(Input!B26,'Joint Life 50%'!B13:AQ54,MATCH(Input!B22,'Joint Life 	50%'!B13:AQ13,0),FALSE)
            String[] prStrArr = cfap.split(objDB.getPRArr_JointLife50Percent(), ",");
            String pr = null;
            int arrCount = 0;
            double prDouble = 0;

            int ageOfSecondAnnuitant = saralPensionBean.getVestingAge();
//			if(ageOfSecondAnnuitant==0)
//			{ageOfSecondAnnuitant=40;}

            //for (int i=40; i<=80;i++)
            for (int i = 40; i <= 80; i++) {
                //for (int j = 40; j <=80; j++)
                for (int j = 40; j <= 80; j++) {
                    if (i == ageOfSecondAnnuitant && j == saralPensionBean.getVestingAge()) {
                        pr = prStrArr[arrCount];
                        prDouble = Double.parseDouble(pr);
                        break;
                    }
                    arrCount++;
                }
            }
//			   System.out.println("Premium Rate:=> "+prDouble);
            return prDouble;
        } else if (sheetGroup == 3) {
            //VLOOKUP(Input!B26,'Joint Life 50%'!B13:AQ54,MATCH(Input!B22,'Joint Life 	50%'!B13:AQ13,0),FALSE)
            String[] prStrArr = cfap.split(objDB.getPRArr_JointLife100Percent(), ",");
            String pr = null;
            int arrCount = 0;
            double prDouble = 0;
            int ageOfSecondAnnuitant = saralPensionBean.getVestingAge();
//			if(ageOfSecondAnnuitant==0)
//			{ageOfSecondAnnuitant=40;}

            //for (int i=40; i<=80;i++)
            for (int i = 40; i <= 80; i++) {
                //for (int j = 40; j <=80; j++)
                for (int j = 40; j <= 80; j++) {
                    if (i == ageOfSecondAnnuitant && j == saralPensionBean.getVestingAge()) {
                        pr = prStrArr[arrCount];
                        prDouble = Double.parseDouble(pr);
                        break;
                    }
                    arrCount++;
                }
            }
//			   System.out.println("Premium Rate:=> "+prDouble);
            return prDouble;
        } else if (sheetGroup == 4) {
            //VLOOKUP(Input!B26,'Joint Life 50%_ROC'!B13:AQ54,MATCH(Input!B22,'Joint Life 	50%_ROC'!B13:AQ13,0),FALSE)
            String[] prStrArr = cfap.split(objDB.getPRArr_JointLife50Percent_ROC(), ",");
            String pr = null;
            int arrCount = 0;
            double prDouble = 0;

            int ageOfSecondAnnuitant = saralPensionBean.getVestingAge();
//			if(ageOfSecondAnnuitant==0)
//			{ageOfSecondAnnuitant=40;}

            //for (int i=40; i<=80;i++)
            for (int i = 40; i <= 80; i++) {
                //for (int j = 40; j <=80; j++)
                for (int j = 40; j <= 80; j++) {
                    //System.out.println("Age of First Annuitant(i) => "+i+"   Age of Second Annuitant(j) => "+j+"   PR=> "+prStrArr[arrCount]);
                    if (i == ageOfSecondAnnuitant && j == saralPensionBean.getVestingAge()) {
                        pr = prStrArr[arrCount];
                        prDouble = Double.parseDouble(pr);
                        //System.out.println("PR Returned=> "+prDouble);
                        break;
                    }
                    arrCount++;
                }
            }
//			   System.out.println("Premium Rate:=> "+prDouble);
            return prDouble;
        } else if (sheetGroup == 5) {
            //VLOOKUP(Input!B26,'Joint_Life_100%_ROC'!B13:AQ54,MATCH(Input!B22,'Joint_Life_100%_RO	C'!B13:AQ13,0),FALSE)
            String[] prStrArr = cfap.split(objDB.getPRArr_JointLife100Percent_ROC(), ",");
            String pr = null;
            int arrCount = 0;
            double prDouble = 0;
            int ageOfSecondAnnuitant = saralPensionBean.getVestingAge();
//			if(ageOfSecondAnnuitant==0)
//			{ageOfSecondAnnuitant=40;}

            //for (int i=40; i<=80;i++)
            for (int i = 40; i <= 80; i++) {
                //for (int j = 40; j <=80; j++)
                for (int j = 40; j <= 80; j++) {
                    if (i == ageOfSecondAnnuitant && j == saralPensionBean.getVestingAge()) {
                        pr = prStrArr[arrCount];
                        prDouble = Double.parseDouble(pr);
                        break;
                    }
                    arrCount++;
                }
            }
//			   System.out.println("Premium Rate:=> "+prDouble);
            return prDouble;
        } else if (sheetGroup == 6) {
            //VLOOKUP(Input!B22,Temporary_Annuity!B13:D77,MATCH(F8,Temporary_Annuity!B14:D14,0),FALSE)
            //VLOOKUP( Age ,Temporary_Annuity!B13:D77,MATCH(F8,Temporary_Annuity!B14:D14,0),FALSE)
            //NOT COMPLETE
            return 0;
        } else {
            return 0;
        }
    }
}
