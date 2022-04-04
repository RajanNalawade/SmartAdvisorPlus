/**************************************************************
 * Author-> Akshaya Mirajkar
 *************************************************************/

package sbilife.com.pointofsale_bancaagency.shubhnivesh;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import sbilife.com.pointofsale_bancaagency.common.CommonForAllProd;


class ShubhNiveshBusinessLogic {
    //Class Variable Declaration
    private ShubhNiveshBean shubhNiveshBean = null;
    private CommonForAllProd commonForAllProd = null;
    private ShubhNiveshProperties prop = null;


    //Store User Entered Data in a Bean Object
    public ShubhNiveshBusinessLogic(ShubhNiveshBean shubhNiveshBean) {
        this.shubhNiveshBean = shubhNiveshBean;
        commonForAllProd = new CommonForAllProd();
        prop = new ShubhNiveshProperties();
    }

    public ShubhNiveshBean getSaralShieldBean() {
        return shubhNiveshBean;
    }

    //    public String get_Premium_Basic_WithoutST_Rounded()
    //    {return commonForAllProd.getRoundUp(""+get_Premium_Basic_WithoutST_NotRounded());}


    /*** modified by Akshaya on 20-MAY-16 start **/
//	public double getServiceTax()
//	{
//		if(shubhNiveshBean.isJkResident())
//			return prop.serviceTaxJKResident;
//		else
//			return 	prop.serviceTax;	
//	}

//	 public double getServiceTax(double premiumWithoutST,boolean JKResident,String type)
//		{
//			if(type.equals("basic"))
//			{
//				if(JKResident==true)
//					return Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumWithoutST*prop.serviceTaxJKResident)));
//				else
//				{
//					
//					System.out.println("premiumWithoutST "+commonForAllProd.getStringWithout_E(premiumWithoutST));
//					System.out.println("ST "+(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumWithoutST*(prop.serviceTax+1)))));
//					return Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumWithoutST*(prop.serviceTax+1))));
//				}
//			}
//			else if (type.equals("SBC"))
//			{
//				if(JKResident==true)
//					return 0;
//				else
//				{
//					return Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumWithoutST*prop.SBCServiceTax)));
//				}
//			}
//			else //KKC
//			{
//				if(JKResident==true)
//					return 0;
//				else
//				{
//					return Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumWithoutST*prop.KKCServiceTax)));
//				}
//			}
//				
//		}


//Added by sujata on 09-01-2020
    public double getServiceTax(double premiumBasic, boolean JKResident, boolean state, String type) {
        double basicST = 0, sbcST = 0, kkcST = 0, basicJkST = 0, keralaST = 0, premST;

        if (type.equals("basic")) {
            if (JKResident) {
//				basicJkST=Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumWithoutST*(prop.serviceTaxJKResident+1))));
                basicJkST = Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumBasic * (prop.serviceTaxJKResident + 1))));
//				return basicJkST-premiumWithoutST;
                return Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(basicJkST - premiumBasic)));
            } else {
                if (state == true) {
                    basicST = Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumBasic * (prop.servtx))));
//				basicST = basicST-premiumBasic-1;
//				System.out.println("basicSTKerala "+basicST);
                } else {
                    basicST = Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumBasic * (prop.serviceTax))));
//				System.out.println("basicST Before " + basicST);
//				basicST=(basicST-premiumBasic-1);
//				System.out.println("basicSTminus "+basicST);
//				System.out.println("premiumBasic "+premiumBasic);
                }
//				basicST= Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumWithoutST*(prop.serviceTax+1))));
//				basicST= Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumWithoutST*(prop.serviceTax+1))));
//				return basicST-premiumWithoutST;
            }

            basicST = basicST + (Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumBasic * (prop.serviceTax)))));
//			System.out.println("basicST "+ (basicST-premiumBasic));
//			return (basicST-premiumBasic);
//			System.out.println("(basicST+premiumBasic) " + (basicST+premiumBasic));
            return (basicST);
            //return Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(basicST-premiumWithoutST)));
        } else if (type.equals("SBC")) {
            if (JKResident)
                return 0;
            else {
//				sbcST= Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumWithoutST*(prop.SBCServiceTax+1))));
                sbcST = Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumBasic * 1)));
//				return sbcST-premiumWithoutST;
                return Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(sbcST - premiumBasic)));
            }
        } else if (type.equals("KERALA")) {
            keralaST = Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumBasic * 1)));
//				return sbcST-premiumWithoutST;
            return Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(keralaST - premiumBasic)));
        } else //KKC
        {
            if (JKResident)
                return 0;
            else {
//				kkcST= Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumWithoutST*(prop.KKCServiceTax+1))));
                kkcST = Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumBasic * 1)));
//				return kkcST-premiumWithoutST;
                return Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(kkcST - premiumBasic)));
            }
        }

    }


    public double getServiceTaxSecondYear(double premiumBasic, boolean JKResident, boolean state, String type) {

        double basicST = 0, sbcST = 0, kkcST = 0, basicJkST = 0, keralaST = 0;

        if (type.equals("basic")) {
            if (JKResident) {
//					basicJkST=Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumWithoutST*(prop.serviceTaxJKResident+1))));
                basicJkST = Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumBasic * (prop.serviceTaxJKResident))));
//					return basicJkST-premiumWithoutST;
                return Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(basicJkST - premiumBasic)));
            } else {
                if (state == true) {
                    basicST = Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumBasic * (prop.kerlaServiceTaxSecondYear))));
                    //			basicST = basicST-premiumBasic-1;
                } else {
                    basicST = Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumBasic * (prop.serviceTaxSecondYear))));
//					System.out.println("basicST Before " + basicST);
//					basicST=(basicST-premiumBasic-1);
					/*System.out.println("basicSTminus "+basicST);
					System.out.println("premiumBasic "+premiumBasic);*/
                }
//					basicST= Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumWithoutST*(prop.serviceTax+1))));
//					basicST= Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumWithoutST*(prop.serviceTax+1))));
//					return basicST - premiumWithoutST;
            }

            basicST = basicST + (Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumBasic * (prop.serviceTaxSecondYear)))));
//				System.out.println("basicST 2 year "+ (basicST+premiumBasic));
//				return (basicST+premiumBasic);
            return basicST;
            //return Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(basicST-premiumWithoutST)));
        } else if (type.equals("SBC")) {
            if (JKResident)
                return 0;
            else {
//					sbcST=Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumWithoutST*(prop.SBCServiceTaxSecondYear+1))));
                sbcST = Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumBasic * 1)));
                return Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(sbcST - premiumBasic)));
//					return sbcST- premiumWithoutST;
            }
        } else if (type.equals("KERALA")) {
            keralaST = Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumBasic * 1)));
            return Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(keralaST - premiumBasic)));
//					return sbcST- premiumWithoutST;
        } else //KKC
        {
            if (JKResident)
                return 0;
            else {
//					kkcST=Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumWithoutST*(prop.KKCServiceTaxSecondYear+1))));
                kkcST = Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumBasic * 1)));
                return Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(kkcST - premiumBasic)));
//					return kkcST- premiumWithoutST;
            }
        }

    }


    public double getServiceTaxRider(double premiumBasicRider, boolean JKResident, boolean state, String type) {
        double basicSTRider = 0, sbcST = 0, kkcST = 0, basicJkST = 0, keralaST = 0, premST;

        if (type.equals("basic")) {
            if (JKResident == true) {
//					basicJkST=Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumWithoutST*(prop.serviceTaxJKResident+1))));
                basicJkST = Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumBasicRider * (prop.serviceTaxJKResident))));
//					return basicJkST-premiumWithoutST;
                return Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(basicJkST - premiumBasicRider)));
            } else {
                if (state == true) {
                    basicSTRider = Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumBasicRider * (prop.servtx))));
                    //				basicSTRider=basicSTRider-premiumBasicRider-1;
                } else {
                    basicSTRider = Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumBasicRider * (prop.serviceTax))));
//					System.out.println("basicST Before " + basicSTRider);
//					basicSTRider=(basicSTRider-premiumBasicRider-1);
//					System.out.println("basicSTminus "+basicSTRider);
//					System.out.println("premiumBasicRider "+premiumBasicRider);
                }
//					basicST= Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumWithoutST*(prop.serviceTax+1))));
//					basicST= Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumWithoutST*(prop.serviceTax+1))));
//					return basicST-premiumWithoutST;
            }

            basicSTRider = basicSTRider + (Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumBasicRider * (prop.serviceTax)))));
//				System.out.println("basicSTRider "+ (basicSTRider));
            return (basicSTRider);
            //return Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(basicST-premiumWithoutST)));
        } else if (type.equals("SBC")) {
            if (JKResident == true)
                return 0;
            else {
//					sbcST= Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumWithoutST*(prop.SBCServiceTax+1))));
                sbcST = Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumBasicRider * 1)));
//					return sbcST-premiumWithoutST;
                return Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(sbcST - premiumBasicRider)));
            }
        } else if (type.equals("KERALA")) {
            keralaST = Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumBasicRider * 1)));
//					return sbcST-premiumWithoutST;
            return Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(keralaST - premiumBasicRider)));
        } else //KKC
        {
            if (JKResident == true)
                return 0;
            else {
//					kkcST= Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumWithoutST*(prop.KKCServiceTax+1))));
                kkcST = Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumBasicRider * 1)));
//					return kkcST- premiumWithoutST;
                return Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(kkcST - premiumBasicRider)));
            }
        }

    }


    public double getServiceTaxRiderSecondYear(double premiumBasicRider, boolean JKResident, boolean state, String type) {
        double basicSTRider2 = 0, sbcST = 0, kkcST = 0, basicJkST = 0, keralaST = 0, premST;

        if (type.equals("basic")) {
            if (JKResident == true) {
//					basicJkST=Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumWithoutST*(prop.serviceTaxJKResident+1))));
                basicJkST = Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumBasicRider * (prop.serviceTaxJKResident))));
//					return basicJkST-premiumWithoutST;
                return Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(basicJkST - premiumBasicRider)));
            } else {
                if (state == true) {
                    basicSTRider2 = Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumBasicRider * (prop.kerlaServiceTaxSecondYear))));
//						basicSTRider2=basicSTRider2-premiumBasicRider-1;
                } else {
                    basicSTRider2 = Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumBasicRider * (prop.serviceTaxSecondYear))));
//					System.out.println("basicST Before " + basicSTRider2);
//					basicSTRider2=(basicSTRider2-premiumBasicRider-1);
//					System.out.println("basicSTminus "+basicSTRider2);
//					System.out.println("premiumBasicRider "+premiumBasicRider);
                }
//					basicST= Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumWithoutST*(prop.serviceTax+1))));
//					basicST= Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumWithoutST*(prop.serviceTax+1))));
//					return basicST-premiumWithoutST;
            }

            basicSTRider2 = basicSTRider2 + (Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumBasicRider * (prop.serviceTaxSecondYear)))));
            //System.out.println("basicSTRiderSY "+ (basicSTRider2-premiumBasicRider));
            return (basicSTRider2);
            //return Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(basicST-premiumWithoutST)));
        } else if (type.equals("SBC")) {
            if (JKResident == true)
                return 0;
            else {
//					sbcST= Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumWithoutST*(prop.SBCServiceTax+1))));
                sbcST = Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumBasicRider * 1)));
//					return sbcST-premiumWithoutST;
                return Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(sbcST - premiumBasicRider)));
            }
        } else if (type.equals("KERALA")) {
            keralaST = Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumBasicRider * 1)));
//					return sbcST-premiumWithoutST;
            return Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(keralaST - premiumBasicRider)));
        } else //KKC
        {
            if (JKResident == true)
                return 0;
            else {
//					kkcST= Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumWithoutST*(prop.KKCServiceTax+1))));
                kkcST = Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumBasicRider * 1)));
//					return kkcST-premiumWithoutST;
                return Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(kkcST - premiumBasicRider)));
            }
        }

    }


//		 public double getServiceTaxSecondYear(double premiumWithoutST,boolean JKResident,String type)
//			{
//				if(type.equals("basic"))
//				{
//					if(JKResident==true)
//						return Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumWithoutST*prop.serviceTaxJKResident)));
//					else
//					{
//						return Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumWithoutST*prop.serviceTaxSecondYear)));
//					}
//				}
//				else if (type.equals("SBC"))
//				{
//					if(JKResident==true)
//						return 0;
//					else
//					{
//						return Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumWithoutST*prop.SBCServiceTaxSecondYear)));
//					}
//				}
//				else //KKC
//				{
//					if(JKResident==true)
//						return 0;
//					else
//					{
//						return Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumWithoutST*prop.KKCServiceTaxSecondYear)));
//					}
//				}
//
//			}

    /*** modified by Akshaya on 20-MAY-16 end **/


    public double get_Premium_Basic_WithoutST_NotRounded() {
        int divFactor = 0;
        if (shubhNiveshBean.getPremiumFreq().equals("Yearly") || shubhNiveshBean.getPremiumFreq().equals("Single")) {
            divFactor = 1;
        } else if (shubhNiveshBean.getPremiumFreq().equals("Half Yearly")) {
            divFactor = 2;
        } else if (shubhNiveshBean.getPremiumFreq().equals("Quarterly")) {
            divFactor = 4;
        } else {
            divFactor = 12;
        }

//		        System.out.println("rate " + getPR_Basic_FromDB());
//		        System.out.println("staff rebate " + getStaffRebate());
//		        System.out.println("SA rebate " + getSA_Rebate_Basic());
//		        System.out.println("NSAP " + getNSAP());
//		        System.out.println("EMR " + getEMR());
//		        System.out.println("SA Basic " + shubhNiveshBean.getSumAssured_Basic());
//		        System.out.println("loading " + getLoadingForFreqOfPremiums("Basic"));
//		        System.out.println("divfact " + divFactor);
        /**** Added By Priyanka Warekar - 08-08-2016 - Start ****/

//		        System.out.println("final "+( getPR_Basic_FromDB() *(1- getStaffRebate())- getSA_Rebate_Basic() +( getNSAP() + getEMR()))* shubhNiveshBean.getSumAssured_Basic() /1000* getLoadingForFreqOfPremiums("Basic")/1);

        return Double.parseDouble(commonForAllProd.getRoundOffLevel2(commonForAllProd.getStringWithout_E((getPR_Basic_FromDB() * (1 - getStaffRebate()) - getSA_Rebate_Basic() + (getNSAP() + getEMR())) * shubhNiveshBean.getSumAssured_Basic() / 1000 * getLoadingForFreqOfPremiums("Basic") / 1)));
        //Modified by tushar kotian on 9/8/2017//
        //return (( getPR_Basic_FromDB() *(1- getStaffRebate())- getSA_Rebate_Basic() +( getNSAP() + getEMR()))* shubhNiveshBean.getSumAssured_Basic() /1000* getLoadingForFreqOfPremiums("Basic")/1);
        /**** Added By Priyanka Warekar - 08-08-2016 - End ****/
    }


    /********************Added by Tushar Kotian on 19/9/2017 **********************/
    public double get_Premium_WithoutST_WithoutFreqLoading() {
        int divFactor = 0;
        if (shubhNiveshBean.getPremiumFreq().equals("Yearly") || shubhNiveshBean.getPremiumFreq().equals("Single")) {
            divFactor = 1;
        } else if (shubhNiveshBean.getPremiumFreq().equals("Half Yearly")) {
            divFactor = 2;
        } else if (shubhNiveshBean.getPremiumFreq().equals("Quarterly")) {
            divFactor = 4;
        } else {
            divFactor = 12;
        }


        return Double.parseDouble(commonForAllProd.getRoundOffLevel2(commonForAllProd.getStringWithout_E((getPR_Basic_FromDB() * (1 - getStaffRebate()) - getSA_Rebate_Basic() + (getNSAP() + getEMR())) * shubhNiveshBean.getSumAssured_Basic() / 1000)));
    }

    /********************Added by Tushar Kotian on 19/9/2017 **********************/


    public double get_Premium_Basic_WithoutAnyDisc_NotRounded() {
        int divFactor = 0;
        if (shubhNiveshBean.getPremiumFreq().equals("Yearly") || shubhNiveshBean.getPremiumFreq().equals("Single")) {
            divFactor = 1;
        } else if (shubhNiveshBean.getPremiumFreq().equals("Half Yearly")) {
            divFactor = 2;
        } else if (shubhNiveshBean.getPremiumFreq().equals("Quarterly")) {
            divFactor = 4;
        } else {
            divFactor = 12;
        }

        //        System.out.println("rate" + getPR_Basic_FromDB());
        //        System.out.println("staff rebate " + getStaffRebate());
        //        System.out.println("SA rebate" + getSA_Rebate_Basic());
        //        System.out.println("NSAP" + getNSAP());
        //        System.out.println("EMR" + getEMR());
        //        System.out.println("SA Basic" + shubhNiveshBean.getSumAssured_Basic());
        //        System.out.println("loading " + getLoadingForFreqOfPremiums());
        //        System.out.println("divfact " + divFactor);
        /**** Added By Priyanka Warekar - 08-08-2016 - Start ****/
        return (getPR_Basic_FromDB() + (getNSAP() + getEMR())) * shubhNiveshBean.getSumAssured_Basic() / 1000 * getLoadingForFreqOfPremiums("Basic") / 1;
        /**** Added By Priyanka Warekar - 08-08-2016 - end ****/
    }


    //This method is used to select Basic PR from the Shubh Nivesh Constant Data
    private double getPR_Basic_FromDB() {
        double prDouble = 0;
        String pr = null, prStr;
        int arrCount = 0;
        String[] prStrArr;
        ShubhNiveshDB ac = new ShubhNiveshDB();
        prStr = ac.getBasic_PR_Arr(getRs_Basic());
        prStrArr = commonForAllProd.split(prStr, ",");
        //Here min age is 18 & max age is 65/min term is 5 & max term is 30
        for (int i = 18; i < 61; i++) {
            for (int j = 5; j < 31; j++) {
                if (i == shubhNiveshBean.getAge() && j == shubhNiveshBean.getPolicyTerm_Basic()) {
                    pr = prStrArr[arrCount];
                    prDouble = Double.parseDouble(pr);
                    break;
                }
                arrCount++;
            }
        }
        return prDouble;
    }

    private String getRs_Basic() {
        //For  Plan- Endowment Option Plan
        if (shubhNiveshBean.getPlanName().equals("Endowment Option")) {
            //For -> Endowment Option Plan >> Single Premium
            if (shubhNiveshBean.getPremiumFreq().equals("Single")) {
                return "EA_SP";
            }
            //For -> Endowment Option Plan >> Regular Premium
            else {
                return "EA_RP";
            }
        }
        //For  plan- Endowment With Whole Life Plan
        else {
            //For -> Endowment with Whole Life Plan >> Single Premium
            if (shubhNiveshBean.getPremiumFreq().equals("Single")) {
                return "EAWL_SP";
            }
            //For -> Endowment with Whole Life Plan >> Regular Premium
            else {
                return "EAWL_RP";
            }
        }
    }

    public double getStaffRebate() {
        if (shubhNiveshBean.getStaffDisc() || shubhNiveshBean.getBancAssuranceDisc()) {
            if (shubhNiveshBean.getPremiumFreq().equals("Single")) {
                return 0.02;
            } else {
                return 0.06;
            }
        } else {
            return 0;
        }
    }

    private double getSA_Rebate_Basic() {
        //For Regular Premium Mode
        if (!shubhNiveshBean.getPremiumFreq().equals("Single")) {
            if (shubhNiveshBean.getSumAssured_Basic() >= 75000 && shubhNiveshBean.getSumAssured_Basic() < 150000) {
                return 0;
            } else if (shubhNiveshBean.getSumAssured_Basic() >= 150000 && shubhNiveshBean.getSumAssured_Basic() < 300000) {
                return 2.25;
            } else if (shubhNiveshBean.getSumAssured_Basic() >= 300000 && shubhNiveshBean.getSumAssured_Basic() < 600000) {
                return 4.5;
            }
            //If(shubhNiveshBean.getSumAssured_Basic() >=600000)
            else {
                return 6;
            }
        }
        //For Single Premium Mode
        else {
            if (shubhNiveshBean.getSumAssured_Basic() >= 75000 && shubhNiveshBean.getSumAssured_Basic() < 150000) {
                return 0;
            } else if (shubhNiveshBean.getSumAssured_Basic() >= 150000 && shubhNiveshBean.getSumAssured_Basic() < 300000) {
                return 4.5;
            } else if (shubhNiveshBean.getSumAssured_Basic() >= 300000 && shubhNiveshBean.getSumAssured_Basic() < 600000) {
                return 9;
            }
            //if(shubhNiveshBean.getSumAssured_Basic() >=600000)
            else {
                return 12;
            }
        }
    }

    private double getLoadingForFreqOfPremiums(String cover) {
        if (cover.equals("Basic")) {
            if (shubhNiveshBean.getPremiumFreq().equals("Yearly") || shubhNiveshBean.getPremiumFreq().equals("Single")) {
                return 1;
            } else if (shubhNiveshBean.getPremiumFreq().equals("Half Yearly")) {
                return prop.HalfYearly_Modal_Factor;
            } else if (shubhNiveshBean.getPremiumFreq().equals("Quarterly")) {
                return prop.Quarterly_Modal_Factor;
            } else {
                return prop.Monthly_Modal_Factor;
            }
        } else {

            if (shubhNiveshBean.getPremiumFreq().equals("Yearly") || shubhNiveshBean.getPremiumFreq().equals("Single")) {
                return 1;
            } else if (shubhNiveshBean.getPremiumFreq().equals("Half Yearly")) {
                return 1.04;
            } else if (shubhNiveshBean.getPremiumFreq().equals("Quarterly")) {
                return 1.06;
            } else {
                return 1.068;
            }

        }
    }

    private int getNSAP() {
        return 0;
    }

    private int getEMR() {
        return 0;
    }

    public double get_Premium_PTA_WithoutST_NotRounded() {
        int divFactor = 0;
        if (shubhNiveshBean.getPremiumFreq().equals("Yearly") || shubhNiveshBean.getPremiumFreq().equals("Single")) {
            divFactor = 1;
        } else if (shubhNiveshBean.getPremiumFreq().equals("Half Yearly")) {
            divFactor = 2;
        } else if (shubhNiveshBean.getPremiumFreq().equals("Quarterly")) {
            divFactor = 4;
        } else {
            divFactor = 12;
        }
        return (getPR_PTA_FromDB() * (1 - getStaffRebate()) - getSA_Rebate_Rider() + (getNSAP() + getEMR())) * shubhNiveshBean.getSumAssured_PTA() / 1000 * getLoadingForFreqOfPremiums("Rider") / divFactor;
    }


    public double get_Premium_PTA_WithoutAnyDisc_NotRounded() {
        int divFactor = 0;
        if (shubhNiveshBean.getPremiumFreq().equals("Yearly") || shubhNiveshBean.getPremiumFreq().equals("Single")) {
            divFactor = 1;
        } else if (shubhNiveshBean.getPremiumFreq().equals("Half Yearly")) {
            divFactor = 2;
        } else if (shubhNiveshBean.getPremiumFreq().equals("Quarterly")) {
            divFactor = 4;
        } else {
            divFactor = 12;
        }
        return (getPR_PTA_FromDB() + (getNSAP() + getEMR())) * shubhNiveshBean.getSumAssured_PTA() / 1000 * getLoadingForFreqOfPremiums("Rider") / divFactor;
    }

    private double getPR_PTA_FromDB() {
        double prDouble = 0;
        String pr = null, prStr;
        int arrCount = 0;
        String[] prStrArr;
        ShubhNiveshDB ac = new ShubhNiveshDB();
        if (shubhNiveshBean.getPremiumFreq().equals("Single")) {
            prStr = ac.getPTA_PremiumRate_Arr("Single");
        } else {
            prStr = ac.getPTA_PremiumRate_Arr("Regular");
        }
        prStrArr = commonForAllProd.split(prStr, ",");
        //Here min age is 18 & max age is 65/min term is 5 & max term is 30
        for (int i = 18; i < 61; i++) {
            for (int j = 5; j < 31; j++) {
                if (i == shubhNiveshBean.getAge() && j == shubhNiveshBean.getPolicyTerm_PTA()) {
                    pr = prStrArr[arrCount];
                    prDouble = Double.parseDouble(pr);
                    break;
                }
                arrCount++;
            }
        }
        return prDouble;
    }

    public double get_Premium_ADB_WithoutST_NotRounded() {
        int divFactor = 0;
        if (shubhNiveshBean.getPremiumFreq().equals("Yearly") || shubhNiveshBean.getPremiumFreq().equals("Single")) {
            divFactor = 1;
        } else if (shubhNiveshBean.getPremiumFreq().equals("Half Yearly")) {
            divFactor = 2;
        } else if (shubhNiveshBean.getPremiumFreq().equals("Quarterly")) {
            divFactor = 4;
        } else {
            divFactor = 12;
        }
        return (getPR_Rider_FromDB("ADB") * (1 - getStaffRebate()) - getSA_Rebate_Rider() + (getNSAP() + getEMR())) * shubhNiveshBean.getSumAssured_ADB() / 1000 * getLoadingForFreqOfPremiums("Rider") / divFactor;
    }


    public double get_Premium_ADB_WithoutAnyDisc_NotRounded() {
        int divFactor = 0;
        if (shubhNiveshBean.getPremiumFreq().equals("Yearly") || shubhNiveshBean.getPremiumFreq().equals("Single")) {
            divFactor = 1;
        } else if (shubhNiveshBean.getPremiumFreq().equals("Half Yearly")) {
            divFactor = 2;
        } else if (shubhNiveshBean.getPremiumFreq().equals("Quarterly")) {
            divFactor = 4;
        } else {
            divFactor = 12;
        }
        return (getPR_Rider_FromDB("ADB") + (getNSAP() + getEMR())) * shubhNiveshBean.getSumAssured_ADB() / 1000 * getLoadingForFreqOfPremiums("Rider") / divFactor;
    }


    public double get_Premium_ATPDB_WithoutST_NotRounded() {
        int divFactor = 0;
        if (shubhNiveshBean.getPremiumFreq().equals("Yearly") || shubhNiveshBean.getPremiumFreq().equals("Single")) {
            divFactor = 1;
        } else if (shubhNiveshBean.getPremiumFreq().equals("Half Yearly")) {
            divFactor = 2;
        } else if (shubhNiveshBean.getPremiumFreq().equals("Quarterly")) {
            divFactor = 4;
        } else {
            divFactor = 12;
        }


        //        System.out.println("***********************************************************************");
        //        System.out.println("getPR_Rider_FromDB('ATPDB') -> "+getPR_Rider_FromDB("ATPDB"));
        //        System.out.println("getStaffRebate() -> "+getStaffRebate());
        //        System.out.println("getSA_Rebate_Rider() -> "+getSA_Rebate_Rider());
        //        System.out.println("getNSAP() -> "+getNSAP());
        //        System.out.println("getEMR() -> "+getEMR());
        //        System.out.println("shubhNiveshBean.getSumAssured_ATPDB() -> "+shubhNiveshBean.getSumAssured_ATPDB());
        //        System.out.println("getLoadingForFreqOfPremiums() -> "+getLoadingForFreqOfPremiums());
        //        System.out.println("divFactor -> "+divFactor);
        //        System.out.println("***********************************************************************");


        return (getPR_Rider_FromDB("ATPDB") * (1 - getStaffRebate()) - getSA_Rebate_Rider() + (getNSAP() + getEMR())) * shubhNiveshBean.getSumAssured_ATPDB() / 1000 * getLoadingForFreqOfPremiums("Rider") / divFactor;
    }


    public double get_Premium_ATPDB_WithoutAnyDisc_NotRounded() {
        int divFactor = 0;
        if (shubhNiveshBean.getPremiumFreq().equals("Yearly") || shubhNiveshBean.getPremiumFreq().equals("Single")) {
            divFactor = 1;
        } else if (shubhNiveshBean.getPremiumFreq().equals("Half Yearly")) {
            divFactor = 2;
        } else if (shubhNiveshBean.getPremiumFreq().equals("Quarterly")) {
            divFactor = 4;
        } else {
            divFactor = 12;
        }

        return (getPR_Rider_FromDB("ATPDB") + (getNSAP() + getEMR())) * shubhNiveshBean.getSumAssured_ATPDB() / 1000 * getLoadingForFreqOfPremiums("Rider") / divFactor;
    }


    private int getSA_Rebate_Rider() {
        return 0;
    }

    //This method is used to select Basic PR for ADB.ATPDB Rider from the Shubh Nivesh Constant Data
    private double getPR_Rider_FromDB(String whichRider) {
        //        System.out.println("whichRider -> "+whichRider);
        if (!shubhNiveshBean.getPremiumFreq().equals("Single")) {
            if (whichRider.equals("ADB")) {
                //                System.out.println("..................getPR_Rider_FromDB()    :   "+ 0.5);
                return 0.5;
            } else {
                //                System.out.println("..................getPR_Rider_FromDB()    :   "+ 0.4);
                return 0.4;
            }
        } else {
            double pr = 0;
            ShubhNiveshDB ac = new ShubhNiveshDB();
            int[] prArr = null;
            if (whichRider.equals("ADB")) {
                prArr = ac.getADB_PremiumRate_Arr();
                for (int i = 5; i < 31; i++) {
                    if (i == shubhNiveshBean.getPolicyTerm_ADB()) {
                        pr = prArr[i - 5];
                        break;
                    }
                }
            } else {
                prArr = ac.getATPDB_PremiumRate_Arr();
                for (int i = 5; i < 31; i++) {
                    if (i == shubhNiveshBean.getPolicyTerm_ATPDB()) {
                        pr = prArr[i - 5];
                        break;
                    }
                }
            }
            return pr / 100;
        }
    }


    //added by sujata on 08-01-2020

    public double getannulizedPremFinal(int year_F, int ppt, String premfreq) {
        if (premfreq.equals("Single")) {
            ppt = 1;
            if (year_F <= ppt) {
                //System.out.println("Annulized " + get_Premium_WithoutST_WithoutFreqLoading() );
                return get_Premium_WithoutST_WithoutFreqLoading();
            } else {
                return 0;
            }
        } else {
            if (year_F <= ppt) {
                //System.out.println("Annulized " + get_Premium_WithoutST_WithoutFreqLoading() );
                return get_Premium_WithoutST_WithoutFreqLoading();
            } else {
                return 0;
            }
        }
    }


    public double getBonusRate(String returnOnInvestment, String plantype,
                               int policyterm, String premfreq) {
        double bonusrate = 0;

        if (plantype.equals("Endowment Option")) {
            if (premfreq.equals("Single")) {
                if (policyterm >= 5 && policyterm <= 9) {
                    if (returnOnInvestment.equals("4%")) {
                        bonusrate = 0.005;
                    } else {
                        bonusrate = 0.035;
                    }
                } else if (policyterm >= 10 && policyterm <= 14) {
                    if (returnOnInvestment.equals("4%")) {
                        bonusrate = 0.006;
                    } else {
                        bonusrate = 0.04;
                    }
                } else if (policyterm >= 15 && policyterm <= 19) {
                    if (returnOnInvestment.equals("4%")) {
                        bonusrate = 0.006;
                    } else {
                        bonusrate = 0.045;
                    }
                } else if (policyterm >= 20 && policyterm <= 24) {
                    if (returnOnInvestment.equals("4%")) {
                        bonusrate = 0.006;
                    } else {
                        bonusrate = 0.051;
                    }
                } else if (policyterm >= 25) {
                    if (returnOnInvestment.equals("4%")) {
                        bonusrate = 0.006;
                    } else {
                        bonusrate = 0.058;
                    }
                }

            } else {

                if (policyterm >= 7 && policyterm <= 9) {
                    if (returnOnInvestment.equals("4%")) {
                        bonusrate = 0.016;
                    } else {
                        bonusrate = 0.034;
                    }
                } else if (policyterm >= 10 && policyterm <= 14) {
                    if (returnOnInvestment.equals("4%")) {
                        bonusrate = 0.016;
                    } else {
                        bonusrate = 0.036;
                    }
                } else if (policyterm >= 15 && policyterm <= 19) {
                    if (returnOnInvestment.equals("4%")) {
                        bonusrate = 0.015;
                    } else {
                        bonusrate = 0.036;
                    }
                } else if (policyterm >= 20 && policyterm <= 24) {
                    if (returnOnInvestment.equals("4%")) {
                        bonusrate = 0.015;
                    } else {
                        bonusrate = 0.038;
                    }
                } else if (policyterm >= 25) {
                    if (returnOnInvestment.equals("4%")) {
                        bonusrate = 0.015;
                    } else {
                        bonusrate = 0.04;
                    }
                }


            }

        } else {

            if (premfreq.equals("Single")) {
                if (policyterm >= 5 && policyterm <= 9) {
                    if (returnOnInvestment.equals("4%")) {
                        bonusrate = 0;
                    } else {
                        bonusrate = 0;
                    }
                } else if (policyterm >= 10 && policyterm <= 14) {
                    if (returnOnInvestment.equals("4%")) {
                        bonusrate = 0;
                    } else {
                        bonusrate = 0;
                    }
                } else if (policyterm >= 15 && policyterm <= 19) {
                    if (returnOnInvestment.equals("4%")) {
                        bonusrate = 0.004;
                    } else {
                        bonusrate = 0.051;
                    }
                } else if (policyterm >= 20 && policyterm <= 24) {
                    if (returnOnInvestment.equals("4%")) {
                        bonusrate = 0.003;
                    } else {
                        bonusrate = 0.057;
                    }
                } else if (policyterm >= 25) {
                    if (returnOnInvestment.equals("4%")) {
                        bonusrate = 0.003;
                    } else {
                        bonusrate = 0.065;
                    }
                }

            } else {

                if (policyterm >= 7 && policyterm <= 9) {
                    if (returnOnInvestment.equals("4%")) {
                        bonusrate = 0;
                    } else {
                        bonusrate = 0;
                    }
                } else if (policyterm >= 10 && policyterm <= 14) {
                    if (returnOnInvestment.equals("4%")) {
                        bonusrate = 0;
                    } else {
                        bonusrate = 0;
                    }
                } else if (policyterm >= 15 && policyterm <= 19) {
                    if (returnOnInvestment.equals("4%")) {
                        bonusrate = 0.014;
                    } else {
                        bonusrate = 0.04;
                    }
                } else if (policyterm >= 20 && policyterm <= 24) {
                    if (returnOnInvestment.equals("4%")) {
                        bonusrate = 0.013;
                    } else {
                        bonusrate = 0.042;
                    }
                } else if (policyterm >= 25) {
                    if (returnOnInvestment.equals("4%")) {
                        bonusrate = 0.013;
                    } else {
                        bonusrate = 0.044;
                    }
                }


            }


        }

        return bonusrate;

    }
//	{
//		//        //For Single Premium
//		//        if(shubhNiveshBean.getPremiumFreq().equals("Single"))
//		//        {
//		//            if(returnOnInvestment.equals("6%"))
//		//            {return 0.0225;}
//		//            //For 10%
//		//            else
//		//            {
//		//                if(shubhNiveshBean.getPolicyTerm_Basic()>=5  &&   shubhNiveshBean.getPolicyTerm_Basic()<=14)
//		//                {return 0.0425;}
//		//                if(shubhNiveshBean.getPolicyTerm_Basic()>=15 && shubhNiveshBean.getPolicyTerm_Basic()<=19)
//		//                {return 0.045;}
//		//                if(shubhNiveshBean.getPolicyTerm_Basic()>=20)
//		//                {return 0.0475;}
//		//                else{return 0;}
//		//            }
//		//        }
//		//        //For Regular Premium
//		//        else
//		//        {
//		//            if(returnOnInvestment.equals("6%"))
//		//            {return 0.0225;}
//		//            //For 10%
//		//            else
//		//            {
//		//                if(shubhNiveshBean.getPolicyTerm_Basic()>=5  &&   shubhNiveshBean.getPolicyTerm_Basic()<=14)
//		//                {return 0.04;}
//		//                if(shubhNiveshBean.getPolicyTerm_Basic()>=15 && shubhNiveshBean.getPolicyTerm_Basic()<=19)
//		//                {return 0.0425;}
//		//                if(shubhNiveshBean.getPolicyTerm_Basic()>=20)
//		//                {return 0.045;}
//		//                else{return 0;}
//		//            }
//		//        }
//
//
////		if(returnOnInvestment.equals("4%"))
////			return 0.019;
////		else
////			return 0.03;
//
//		double bonusrate = 0 ;
//
//		if(plantype.equals(""))
//
//
//		return bonusrate;
//
//
//
//	}


    public double getNonGuaranteedMatBenefit(String returnOnInvestment) {
        return shubhNiveshBean.getPolicyTerm_Basic() * shubhNiveshBean.getSumAssured_Basic() * getBonusRate(returnOnInvestment, shubhNiveshBean.getPlanName(), shubhNiveshBean.getPolicyTerm_Basic(), shubhNiveshBean.getPremiumFreq()) * (0.15 + 1);
    }


    /************************************* Added by Akshaya Start **********************************/


    public double getTotPremPaidWtServTx_B(double premBasic, int polYear) {
        int mulFact = 1;

        if (shubhNiveshBean.getPremiumFreq().equals("Yearly") || shubhNiveshBean.getPremiumFreq().equals("Single"))
            mulFact = 1;
        else if (shubhNiveshBean.getPremiumFreq().equals("Half Yearly"))
            mulFact = 2;
        else if (shubhNiveshBean.getPremiumFreq().equals("Quarterly"))
            mulFact = 4;
        else
            mulFact = 12;


        if (shubhNiveshBean.getPremiumFreq().equals("Single") && polYear != 1)
            return 0;
        else
//			return Math.round(premBasic*mulFact*polYear);
            return (premBasic * mulFact * polYear);

    }

    //Added by sujata on 08-01-2020
    public double getDeathBenefitNonGuaranteed4per(int polYear, int PolicyTerm) {
        double temp1, temp2;
        if (polYear > PolicyTerm) {
            return 0;
        } else {
            temp1 = polYear * (shubhNiveshBean.getSumAssured_Basic() * getBonusRate("4%", shubhNiveshBean.getPlanName(), shubhNiveshBean.getPolicyTerm_Basic(), shubhNiveshBean.getPremiumFreq()));

//		if(polYear==shubhNiveshBean.getPolicyTerm_Basic())
//			temp2 = 1.15;
//		else
//			temp2 = 1;

//		System.out.println("death "+(Math.round(temp1)));
            return Math.round(temp1);
        }
    }

    public double getDeathBenefitNonGuaranteed4perILL(int polYear, int PolicyTerm) {
        double temp1, temp2;
        if (polYear > PolicyTerm) {
            return 0;
        } else {
            temp1 = polYear * (shubhNiveshBean.getSumAssured_Basic() * getBonusRate("4%", shubhNiveshBean.getPlanName(), shubhNiveshBean.getPolicyTerm_Basic(), shubhNiveshBean.getPremiumFreq()));

            if (polYear == shubhNiveshBean.getPolicyTerm_Basic())
                temp2 = 1.15;
            else
                temp2 = 1;


            return Math.round(temp1 * temp2);
        }
    }

    public double getDeathBenefitNonGuaranteed8per(int polYear, int PolicyTerm) {
        double temp1, temp2;
        if (polYear > PolicyTerm) {
            return 0;
        } else
            temp1 = polYear * shubhNiveshBean.getSumAssured_Basic() * getBonusRate("8%", shubhNiveshBean.getPlanName(), shubhNiveshBean.getPolicyTerm_Basic(), shubhNiveshBean.getPremiumFreq());

		/*if(polYear==shubhNiveshBean.getPolicyTerm_Basic())
			temp2 = 1.15;
		else
			temp2 = 1;*/

        //System.out.println("death8 "+(Math.round(temp1)));
        return Math.round(temp1);
    }

    public double getDeathBenefitNonGuaranteed8perILL(int polYear, int PolicyTerm) {
        double temp1, temp2;
        if (polYear > PolicyTerm) {
            return 0;
        } else
            temp1 = polYear * shubhNiveshBean.getSumAssured_Basic() * getBonusRate("8%", shubhNiveshBean.getPlanName(), shubhNiveshBean.getPolicyTerm_Basic(), shubhNiveshBean.getPremiumFreq());

        if (polYear == shubhNiveshBean.getPolicyTerm_Basic())
            temp2 = 1.15;
        else
            temp2 = 1;

        //System.out.println("death8 "+(Math.round(temp1*temp2)));
        return Math.round(temp1 * temp2);
    }

//	public String getSurrenderGuaranteed(double premBasic, int polYear, int policyTerm,
//			double sumtotalPrem) {
//		double temp1 = 0, temp2 = 0;
//
//		//if (polYear <= polYear) {
//		if (polYear <= policyTerm) {
//			if (shubhNiveshBean.getPremiumFreq().equals("Single")) {
//				temp1 = sumtotalPrem * getGVSFactor(polYear);
//
////				if (shubhNiveshBean.getPolicyTerm_Basic() > 9 && polYear > 1) {
////					temp2 = 1;
////				} else
////					temp2 = 0;
//
//				return String.valueOf(Math.round(temp1));
//
//			} else // Single else
//			{
//				temp1 = sumtotalPrem * getGVSFactor(polYear);
//
//				/*if (shubhNiveshBean.getPolicyTerm_Basic() > 9) {
//					if (polYear > 2)
//						temp2 = 1;
//					else
//						temp2 = 0;
//				} else {
//					if (polYear > 1)
//						temp2 = 1;
//					else
//						temp2 = 0;
//				}*/
//
//			}
//
//		} else {
//			/*if (shubhNiveshBean.getPremiumFreq().equals("Single"))
//				temp1 = premBasic * getGVSFactor(polYear);
//			else
//				temp1 = basePremWithTx * getGVSFactor(polYear);
//
//			if (shubhNiveshBean.getPolicyTerm_Basic() > 9) {
//				if (polYear > 2)
//					temp2 = 1;
//				else
//					temp2 = 0;
//			} else {
//				if (polYear > 1)
//					temp2 = 1;
//				else
//					temp2 = 0;
//			}*/
//			temp1 =0;
//		}
//		 System.out.println("GVS Factor" + getGVSFactor(polYear));
//		// System.out.println("Temp1" + temp1);
//		// System.out.println("Temp2" + temp2);
//		// System.out.println("op" + Math.round(temp1*temp2));
//
//		return String.valueOf(Math.round(temp1));
//	}


    public String getSurrenderGuaranteed(double premBasic, int polYear, int policyTerm,
                                         double sumtotalPrem) {
        double temp1 = 0, temp2 = 0;

        if (shubhNiveshBean.getPlanName().equals("Endowment with Whole Life Option")) {
            if (polYear <= policyTerm) {
                if (shubhNiveshBean.getPremiumFreq().equals("Single")) {
                    temp1 = sumtotalPrem * getGVSFactor(polYear);
                    //		System.out.println("temp1 "+temp1);
                    return String.valueOf(Math.round(temp1));
                } else {
                    temp1 = sumtotalPrem * getGVSFactor(polYear);
                    //	System.out.println("temp1 "+temp1);
                }

            } else {

                temp1 = 0;
            }
        } else {
            if (polYear > policyTerm) {
                temp1 = 0;
            } else {
                if (shubhNiveshBean.getPremiumFreq().equals("Single")) {
                    temp1 = sumtotalPrem * getGVSFactor(polYear);
                    return String.valueOf(Math.round(temp1));
                } else {
                    temp1 = sumtotalPrem * getGVSFactor(polYear);
                    System.out.println("temp1 " + temp1);
                    System.out.println("getGVSFactor(polYear) " + getGVSFactor(polYear));
                    System.out.println("sumtotalPrem " + sumtotalPrem);
                }


            }
//		return String.valueOf(Math.round(temp1));
//		System.out.println("String.valueOf(temp1) "+String.valueOf(temp1));

        }
        return String.valueOf(temp1);
    }

    public double getGVSFactor(int polYear) {

        ShubhNiveshDB ac = new ShubhNiveshDB();
        double[] PWBarr = ac.getGVSFactors_SP();
        double[] PWBarr1 = ac.getGVSFactors_RP();
        double arr = 0;
        int position = 0;

        if (shubhNiveshBean.getPremiumFreq().equals("Single")) {

            for (int i = 1; i <= 30; i++) {

                if (polYear == i) {

                    arr = PWBarr[position];
                }
                position++;
            }

        } else {
            for (int i = 1; i <= 30; i++) {
                for (int j = 10; j <= 30; j++) {
                    if (i == polYear && j == shubhNiveshBean.getPolicyTerm_Basic()) {
                        arr = PWBarr1[position];
                    }
                    position++;
                }
            }
        }
        return arr;
    }


    public double getSSVFactor(int polYear) {
        ShubhNiveshDB ac = new ShubhNiveshDB();
        double[] PWBarrSSV = ac.getSSV_SP();
        double[] PWBarrSSV1 = ac.getSSV_RP();
        double arr = 0;
        int position = 0;
        if (shubhNiveshBean.getPremiumFreq().equals("Single")) {

            for (int i = 1; i <= 30; i++) {
                for (int j = 5; j <= 30; j++) {
                    if (polYear == i && j == shubhNiveshBean.getPolicyTerm_Basic()) {
                        arr = PWBarrSSV[position];
                    }
                    position++;
                }

            }

        } else {
            for (int i = 1; i <= 30; i++) {
                for (int j = 5; j <= 30; j++) {
                    if (i == polYear && j == shubhNiveshBean.getPolicyTerm_Basic()) {
                        arr = PWBarrSSV1[position];
                        //System.out.println("arr " + arr);
                    }
                    position++;
                }
            }
        }
        return arr;
    }

//	public double getSurrenderNonGuaranteed4per(int polYear,double deathNonGuar4pa)
//	{
//		double factor,FinalTemp = 0,temp1,temp2;
//		double sur4pa;
//
//			if(polYear==1 && shubhNiveshBean.getPremiumFreq().equals("Single"))
//				temp1=0;
//			else
//			{
//
//				temp1 = Double.parseDouble(String.valueOf(polYear))/Double.parseDouble(String.valueOf(shubhNiveshBean.getPolicyTerm_Basic()))*Double.parseDouble(String.valueOf(shubhNiveshBean.getSumAssured_Basic()));
//				temp2 = deathNonGuar4pa/((polYear==shubhNiveshBean.getPolicyTerm_Basic())?1.15:1);
//				FinalTemp = (temp1+temp2)*getSSVRate(polYear);
//
//
////				temp1 = (((polYear/shubhNiveshBean.getPolicyTerm_Basic())*shubhNiveshBean.getSumAssured_Basic())+(deathNonGuar4pa/((polYear==shubhNiveshBean.getPolicyTerm_Basic())?1.15:1)))*getSSVRate(polYear);
//			}
//
////			System.out.println("pol Year"+polYear);
////		    System.out.println("Basic term" + shubhNiveshBean.getPolicyTerm_Basic());
////			System.out.println("sa" +shubhNiveshBean.getSumAssured_Basic());
////			System.out.println("Multiplier" + ((polYear==shubhNiveshBean.getPolicyTerm_Basic())?1.15:1));
////		    System.out.println("SSV Rate" + getSSVRate(polYear));
////			System.out.println("deathNonGuar4pa" + deathNonGuar4pa);
//
//
//
//
//
//		if(shubhNiveshBean.getPlanName().equals("Endowment with Whole Life Option"))
//		{
//			if(shubhNiveshBean.getAge() >= 18 && shubhNiveshBean.getAge() <=30)
//				factor=1.1;
//			else if(shubhNiveshBean.getAge() >= 31 && shubhNiveshBean.getAge() <=40)
//				factor=1.15;
//			else
//				factor=1.2;
//
//			sur4pa = Math.round(FinalTemp*factor);
//		}
//		else
//		{
//			sur4pa = Math.round(FinalTemp);
//		}
//
//
////		System.out.println("** " +  Math.round(sur4pa));
//
//		return Math.round(sur4pa);
//	}
//
//
//	public double getSurrenderNonGuaranteed8per(int polYear,double deathNonGuar8pa)
//	{
//		double factor,FinalTemp = 0,temp1,temp2;
//		double sur8pa;
//
//			if(polYear==1 && shubhNiveshBean.getPremiumFreq().equals("Single"))
//				temp1=0;
//			else
//			{
//
//				temp1 = Double.parseDouble(String.valueOf(polYear))/Double.parseDouble(String.valueOf(shubhNiveshBean.getPolicyTerm_Basic()))*Double.parseDouble(String.valueOf(shubhNiveshBean.getSumAssured_Basic()));
//				temp2 = deathNonGuar8pa/((polYear==shubhNiveshBean.getPolicyTerm_Basic())?1.15:1);
//				FinalTemp = (temp1+temp2)*getSSVRate(polYear);
//
//
////				temp1 = (((polYear/shubhNiveshBean.getPolicyTerm_Basic())*shubhNiveshBean.getSumAssured_Basic())+(deathNonGuar4pa/((polYear==shubhNiveshBean.getPolicyTerm_Basic())?1.15:1)))*getSSVRate(polYear);
//			}
//
////			System.out.println("pol Year"+polYear);
////		    System.out.println("Basic term" + shubhNiveshBean.getPolicyTerm_Basic());
////			System.out.println("sa" +shubhNiveshBean.getSumAssured_Basic());
////			System.out.println("Multiplier" + ((polYear==shubhNiveshBean.getPolicyTerm_Basic())?1.15:1));
////		    System.out.println("SSV Rate" + getSSVRate(polYear));
////			System.out.println("deathNonGuar4pa" + deathNonGuar4pa);
//
//
//
//
//
//		if(shubhNiveshBean.getPlanName().equals("Endowment with Whole Life Option"))
//		{
//			if(shubhNiveshBean.getAge() >= 18 && shubhNiveshBean.getAge() <=30)
//				factor=1.1;
//			else if(shubhNiveshBean.getAge() >= 31 && shubhNiveshBean.getAge() <=40)
//				factor=1.15;
//			else
//				factor=1.2;
//
//			sur8pa = Math.round(FinalTemp*factor);
//		}
//		else
//		{
//			sur8pa = Math.round(FinalTemp);
//		}
//
//
////		System.out.println("** " +  Math.round(sur8pa));
//
//		return Math.round(sur8pa);
//	}
//


    public double getSurrenderNonGuaranteed4per(int polYear, double deathNonGuar4pa, double sumassured, int age, String plantype, int PolicyYear) {
        double temp1, temp2, tb_4 = 0, a, b, c;
        double SurrenderNonGuaranteed4per = 0;

        if (shubhNiveshBean.getPlanName().equals("Endowment with Whole Life Option")) {
            if (polYear <= PolicyYear) {
                if (shubhNiveshBean.getPremiumFreq().equals("Single")) {
                    temp1 = getSSVFactor(polYear) * (sumassured + deathNonGuar4pa);
                } else {
                    a = (sumassured * polYear / PolicyYear + deathNonGuar4pa);
//					System.out.println("sumassured "+sumassured);
//					System.out.println("a" + a);
//					System.out.println("polYear " + polYear);
//					b = (PolicyYear + deathNonGuar4pa);
                    temp1 = getSSVFactor(polYear) * a;
//					System.out.println("temp1 "+temp1);
                }
                SurrenderNonGuaranteed4per = temp1 * getWholeLiferateupfactor(age, plantype);
            }
        } else if (polYear <= PolicyYear) {
            if (shubhNiveshBean.getPremiumFreq().equals("Single")) {
                temp1 = getSSVFactor(polYear) * (sumassured + deathNonGuar4pa);
            } else {
                a = (sumassured * polYear / PolicyYear + deathNonGuar4pa);
//				System.out.println("sumassured "+sumassured);
//				System.out.println("a" + a);
//				System.out.println("polYear " + polYear);
//				b = (PolicyYear + deathNonGuar4pa);
                temp1 = getSSVFactor(polYear) * a;
//				System.out.println("temp1 "+temp1);
            }
            SurrenderNonGuaranteed4per = temp1 * 1/*getWholeLiferateupfactor(age, plantype)*/;

        }
//		System.out.println("SurrenderNonGuaranteed4per "+SurrenderNonGuaranteed4per);
        return SurrenderNonGuaranteed4per;

//	return SurrenderNonGuaranteed4per ;
    }


    public double getSurrenderNonGuaranteed8per(int polYear, double deathNonGuar8pa, double sumassured, int age, String plantype, int PolicyYear) {
        double temp1, temp2, tb_4 = 0, a, b, c;
        double SurrenderNonGuaranteed8per = 0;
        if (shubhNiveshBean.getPlanName().equals("Endowment with Whole Life Option")) {
            if (polYear <= PolicyYear) {
                if (shubhNiveshBean.getPremiumFreq().equals("Single")) {
                    temp1 = getSSVFactor(polYear) * (sumassured + deathNonGuar8pa);
                    System.out.println("SurrenderNonGuaranteed8per" + SurrenderNonGuaranteed8per);
                } else {
                    a = (sumassured * polYear / PolicyYear + deathNonGuar8pa);
//					System.out.println("sumassured "+sumassured);
                    System.out.println("a" + a);
//					System.out.println("polYear " + polYear);
//					b = (PolicyYear + deathNonGuar4pa);
                    temp1 = getSSVFactor(polYear) * a;
                    //			System.out.println("temp1 "+temp1);
                }
                SurrenderNonGuaranteed8per = temp1 * getWholeLiferateupfactor(age, plantype);
//				System.out.println("SurrenderNonGuaranteed8per" + SurrenderNonGuaranteed8per);

            }
            System.out.println("SurrenderNonGuaranteed8per" + SurrenderNonGuaranteed8per);

        } else if (polYear <= PolicyYear) {
            if (shubhNiveshBean.getPremiumFreq().equals("Single")) {
                temp1 = getSSVFactor(polYear) * (sumassured + deathNonGuar8pa);
//				System.out.println("\n getSSVFactor(polYear)........ "+getSSVFactor(polYear));
//				System.out.println("deathNonGuar8pa "+deathNonGuar8pa);
//				System.out.println("sumassured " + sumassured);
//				System.out.println("temp1 "+temp1);
            } else {
                a = (sumassured * polYear / PolicyYear + deathNonGuar8pa);
//				System.out.println("sumassured "+sumassured);
//				System.out.println("a" + a);
//				System.out.println("polYear " + polYear);
//				b = (PolicyYear + deathNonGuar4pa);
                temp1 = getSSVFactor(polYear) * a;
                //			System.out.println("temp1 "+temp1);
            }
            SurrenderNonGuaranteed8per = temp1 * 1/*getWholeLiferateupfactor(age, plantype)*/;

        }
//		System.out.println("SurrenderNonGuaranteed8per "+SurrenderNonGuaranteed8per);
        return SurrenderNonGuaranteed8per;

//	return SurrenderNonGuaranteed4per ;

    }


    /****** Created by tushar kotian on 24/10/2017 start ***/
    private double getterminalbonus4(int polYear) {
        double terminalbonus4 = 0;

        if (polYear == shubhNiveshBean.getPolicyTerm_Basic()) {
            terminalbonus4 = shubhNiveshBean.getPolicyTerm_Basic() * shubhNiveshBean.getSumAssured_Basic() * getBonusRate("4%", shubhNiveshBean.getPlanName(), shubhNiveshBean.getPolicyTerm_Basic(), shubhNiveshBean.getPremiumFreq()) * 0.15;
        } else {
            terminalbonus4 = 0;
        }


        return terminalbonus4;

    }


    private double getterminalbonus8(int polYear) {
        double terminalbonus8 = 0;

        if (polYear == shubhNiveshBean.getPolicyTerm_Basic()) {
            terminalbonus8 = shubhNiveshBean.getPolicyTerm_Basic() * shubhNiveshBean.getSumAssured_Basic() * getBonusRate("8%", shubhNiveshBean.getPlanName(), shubhNiveshBean.getPolicyTerm_Basic(), shubhNiveshBean.getPremiumFreq()) * 0.15;
        } else {
            terminalbonus8 = 0;
        }


        return terminalbonus8;

    }

    /****** Created by tushar kotian on 24/10/2017 end ***/

//	public double getSurrenderNonGuaranteed8per(int polYear,double deathNonGuar8pa,int age,String plantype,int PolicyTerm)
//	{
//		double temp1,temp2,tb_8=0;
//
//		if(shubhNiveshBean.getPremiumFreq().equals("Single"))
//		{
//			if(polYear==shubhNiveshBean.getPolicyTerm_Basic())
////				tb_8=shubhNiveshBean.getPolicyTerm_Basic()*shubhNiveshBean.getSumAssured_Basic()*prop.bonusRate_8_percent*prop.terminalBonus;
//
//				/****** Created by tushar kotian on 24/10/2017  Start***/
//				tb_8=getterminalbonus8(polYear);
//			/****** Created by tushar kotian on 24/10/2017  End***/
//
//				else
//				tb_8=0;
//				temp1=shubhNiveshBean.getSumAssured_Basic()+(getDeathBenefitNonGuaranteed8perILL(polYear,PolicyTerm))-tb_8;
//			//	temp1=shubhNiveshBean.getSumAssured_Basic()+deathNonGuar8pa-tb_8;
//
////			System.out.println("8 % *** SSV Rate" + getSSVRate(polYear) + "    "+tb_8+"   "+temp1+"    "+(temp1*getSSVRate(polYear)*getWholeLiferateupfactor(age,plantype)));
////			double temp=temp1*getSSVRate(polYear);
////			return  Math.round(temp1*getSSVRate(polYear));
//
//				/*************************** Modified by Saurabh Jain on 02/03/2019 START **********************/
//
//				return  Math.round(temp1*getSSVRate(polYear)*getWholeLiferateupfactor(age,plantype));
//
//				/*************************** Modified by Saurabh Jain on 02/03/2019 END **********************/
//		}
//		else
//		{
//			if(polYear==shubhNiveshBean.getPolicyTerm_Basic())
////				tb_8=shubhNiveshBean.getPolicyTerm_Basic()*shubhNiveshBean.getSumAssured_Basic()*prop.bonusRate_8_percent*prop.terminalBonus;
//
//				/****** Created by tushar kotian on 24/10/2017  Start***/
//				tb_8=getterminalbonus8(polYear);
//			/****** Created by tushar kotian on 24/10/2017  End***/
//			else
//				tb_8=0;
//
//			temp2 = (double)Math.min(polYear, shubhNiveshBean.getPolicyTerm_Basic())/shubhNiveshBean.getPolicyTerm_Basic()*shubhNiveshBean.getSumAssured_Basic()+(getDeathBenefitNonGuaranteed8perILL(polYear,PolicyTerm))-tb_8;
////			temp2 = (double)Math.min(polYear, shubhNiveshBean.getPolicyTerm_Basic())/shubhNiveshBean.getPolicyTerm_Basic()*shubhNiveshBean.getSumAssured_Basic()+deathNonGuar8pa-tb_8;
////			System.out.println("8 % *** SSV Rate" + getSSVRate(polYear) + "    "+tb_8+"   "+temp2+"    "+(temp2*getSSVRate(polYear)));
////			 double temp=temp2*getSSVRate(polYear);
//			/*************************** Modified by Tushar Kotian on 18/09/2017 START **********************/
//			return Math.round(temp2*getSSVRate(polYear)*getWholeLiferateupfactor(age,plantype));
//
//
//			/*************************** Modified by Tushar Kotian on 18/09/2017 END **********************/
//		}
//
//}
    public double getSSVRate(int polYear) {
        double[] prStrArr;
        double pr = 0;
        ShubhNiveshDB ac = new ShubhNiveshDB();
        int arrCount = 0;
        if (shubhNiveshBean.getPremiumFreq().equals("Single"))
            prStrArr = ac.getSSV_SP();
        else
            prStrArr = ac.getSSV_RP();

        // Here min age is 18 & max age is 65/min term is 5 & max term is 30
        for (int i = 1; i <= 30; i++) {
            for (int j = 5; j <= 30; j++) {
                if (i == polYear && j == shubhNiveshBean.getPolicyTerm_Basic()) {
                    pr = prStrArr[arrCount];
                    break;
                }
                arrCount++;
            }
        }

        return pr;
    }

    public double getMinesOccuInterest(double SumAssured_Basic) {
        return (SumAssured_Basic / 1000) * 2;
    }

    public String getStaffDiscCode() {
        if (shubhNiveshBean.getStaffDisc())
            return "SBI";
        else
            return "None";
    }

    /********************** Added by Akshaya End ****************************************************/


    /********************* Added by Tushar Kotian on 3/8/2017 *****************************/

    public double getBackDateInterest(double grossPremium, String bkDate) {

        try {

            // double indigoRate=7.5;
            /**
             * Modified by Akshaya. Rate Change from 1-APR-2015
             */
            double ST = 0;
            if (shubhNiveshBean.getJKResident()) {
                ST = prop.serviceTaxJKResident;
            } else {
                ST = prop.serviceTax;
            }
            /******************* Modified by Akshaya on 14-APR-2015 start **********/
//			double indigoRate = 10;

            /******************* Modified by Saurabh Jain on 08-APR-2019 start **********/
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
            int days = commonForAllProd.setDate(date, strBackDate);
            // System.out.println("no of days "+days);
            double monthsBetween = commonForAllProd.roundDown(
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

    private double getCompoundAmount(double monthsBetween,
                                     double netPremWithoutST, double indigoRate, int compoundFreq) {
        double compoundAmount = netPremWithoutST
                * Math.pow((1 + (indigoRate / (100 * compoundFreq))),
                (compoundFreq * (monthsBetween / 12)))
                - netPremWithoutST;
        return compoundAmount;
        // System.out.println("compoundAmount "+compoundAmount);
    }

    /********************** Added by Akshaya End ****************************************************/

    /********************* Added by Tushar Kotian on 9/8/2017 *****************************/

    //18-01-2020
    public double getDeathBenifitGuaranteed(String premfreq, int policyterm, int age, int polYear, double sumassured, double premBasic, double annualizedPrem) {
        double DeathBenifitGuaranteed = 0;
        double val = 0;
        if (polYear <= policyterm) {
            if (premfreq.equals("Single")) {
                val = premBasic * 1.25;
                val = Math.max(val, sumassured);
            } else {
                val = Math.max(sumassured, 10 * annualizedPrem);
                //System.out.println("annualizedPrem "+annualizedPrem);
                //System.out.println("val " + val);
            }

        } else {

            return 0;
        }
//		if(polYear<=policyterm)
//		{
//			double a=0,b=0;
//			/********************Modified by Tushar Kotian on 19/9/2017 **********************/
//			a = val * get_Premium_WithoutST_WithoutFreqLoading();
//			/********************Modified by Tushar Kotian on 19/9/2017 **********************/
//			b = 1.05 * getTotPremPaidWtServTx_B(premBasic, polYear);
//			double max1=Math.max(sumassured, a);
//			double max=Math.max(max1, b);
//			DeathBenifitGuaranteed = Math.round(max);
//
//		}
//		else
//		{
//			DeathBenifitGuaranteed=0;
//		}

        return val;

    }

    /********************* Modified by Tushar Kotian on 18/09/2017 *****************************/


    public double getWholeLiferateupfactor(int age, String plantype) {


        double WholeLiferateupfactor = 0;

        //if(plantype.equals("Endowment with Whole Life Option"))
        //	{

        if (age >= 18 && age <= 30) {
            WholeLiferateupfactor = 1.1;
        } else {
            if (age >= 31 && age <= 40) {
                WholeLiferateupfactor = 1.15;
            } else {
                WholeLiferateupfactor = 1.2;
            }

        }
	/*	}
		}
		else
		{
            WholeLiferateupfactor = 1;
		}*/
        return WholeLiferateupfactor;


    }


//Added By sujata ON 08-02-2020

    public double TotalMaturityBenefit4per(int year_F, int PolicyTerm, double deathNonGuar4pa) {
        double mat;
        if (year_F == shubhNiveshBean.getPolicyTerm_Basic()) {
            mat = (shubhNiveshBean.getSumAssured_Basic() + deathNonGuar4pa + (.15 * deathNonGuar4pa));
        } else {
            mat = 0;
        }

//		double mat4per =( mat + deathNonGuar4pa);
//		System.out.println("mat4per "+mat4per);
        return mat;
    }

    public double TotalMaturityBenefit8per(int year_F, int PolicyTerm, double deathNonGuar8pa) {
        double mat;
        if (year_F == shubhNiveshBean.getPolicyTerm_Basic()) {
            mat = (shubhNiveshBean.getSumAssured_Basic() + deathNonGuar8pa + (.15 * deathNonGuar8pa));
        } else {
            mat = 0;
        }

//		double mat8per =( mat + deathNonGuar8pa);
//		System.out.println("mat8per "+mat8per);
        return mat;
    }

    //18-01-2020
    public double getTotalPremiumforDeath(int year_F, int PolicyTerm, double prem) {
        if (shubhNiveshBean.getPremiumFreq().equals("Single")) {
            PolicyTerm = 1;
        }

        if (year_F <= PolicyTerm) {
            //System.out.println("premBasic " + premBasic);
            return prem;
        } else {
            return 0;
        }


    }

    //18-01-2020
    public double TotalDeathBenefit4per(int year, int PolicyTerm, double DeathGuar, double deathNonGuar4pa, double prem) {
        double a = 0, b = 0;
        double totaldeath4per;
        if (year <= PolicyTerm) {
            a = (DeathGuar + deathNonGuar4pa + (.15 * deathNonGuar4pa));
            b = 1.05 * prem;
            totaldeath4per = Math.max(a, b);
            System.out.println("prem " + prem);
            System.out.println("b " + b);
//		System.out.println("deathNonGuar4pa " + deathNonGuar4pa);
            System.out.println("totaldeath4per " + totaldeath4per);
        } else {
            return 0;
        }
//		System.out.println("totaldeath4per "+totaldeath4per);
//		System.out.println("getTotalPremiumforDeath_b "+getTotalPremiumforDeath_b);

        return totaldeath4per;

    }

    public double TotalDeathBenefit8per(int year, int PolicyTerm, double deathGuar, double deathNonGuar8pa, double prem) {
        double totaldeath8per;
        double a = 0, b = 0;
        if (year <= PolicyTerm) {
            a = (deathGuar + deathNonGuar8pa + (.15 * deathNonGuar8pa));
            b = 1.05 * prem;
            totaldeath8per = Math.max(a, b);
        } else {
            return 0;
        }
//		System.out.println("totaldeath8per "+totaldeath8per);
        return totaldeath8per;
    }

}
/********************* Modified by Tushar Kotian on 18/09/2017 *****************************/

/********************* 9/8/2017 by Tushar Kotian on 18/09/2017 *****************************/


