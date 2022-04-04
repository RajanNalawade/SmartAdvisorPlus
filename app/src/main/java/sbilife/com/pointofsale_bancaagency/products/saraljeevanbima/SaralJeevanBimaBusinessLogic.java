package sbilife.com.pointofsale_bancaagency.products.saraljeevanbima;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import sbilife.com.pointofsale_bancaagency.common.CommonForAllProd;

public class SaralJeevanBimaBusinessLogic {
    SaralJeevanBimaProperties obj;
    private SaralJeevanBimaBean saralJeevanBimaBean = null;
    private CommonForAllProd commonForAllProd = null;
    SaralJeevanBimaDB ac;

    double premiumRate = 0, singlePremium = 0, BasicServiceTax = 0, SBC = 0, ServiceTax = 0, PremiumwithST = 0, staffDisc = 0;

    public SaralJeevanBimaBusinessLogic(SaralJeevanBimaBean saralJeevanBimaBean) {
        this.saralJeevanBimaBean = saralJeevanBimaBean;
        obj = new SaralJeevanBimaProperties();
        commonForAllProd = new CommonForAllProd();
        ac = new SaralJeevanBimaDB();
    }


    public double getPr_Basic_FromDB() {

        double prDouble = 0;
        String pr = null, prStr;
        int arrCount = 0;
        String[] prStrArr;
        double[] prArr = null;

        prStr = ac.getBasic_PR_Arr(saralJeevanBimaBean.getPremFreqOptions(), Double.parseDouble(saralJeevanBimaBean.getPremPaymentTerm()));
        prStrArr = commonForAllProd.split(prStr, ",");
        int ppt = 0;

        if (saralJeevanBimaBean.getPremFreqOptions().equalsIgnoreCase("Regular")) {

            for (int ageCount = 18; ageCount <= 65; ageCount++) {
                for (int policyTermCount = 5; policyTermCount <= 40; policyTermCount++) {
                    if (ageCount == saralJeevanBimaBean.getAge() && policyTermCount == saralJeevanBimaBean.getPolicyTerm()) {
                        pr = prStrArr[arrCount];
                        prDouble = Double.parseDouble(pr);
                        System.out.println("prDouble " + prDouble);
                        break;

                    }
                    arrCount++;
                }
            }


        } else if (saralJeevanBimaBean.getPremFreqOptions().equals("LPPT") && saralJeevanBimaBean.getPremPaymentTerm().equalsIgnoreCase("10")) {

            for (int ageCount = 18; ageCount <= 65; ageCount++) {
                for (int policyTermCount = 15; policyTermCount <= 40; policyTermCount++) {
                    if (ageCount == saralJeevanBimaBean.getAge() && policyTermCount == saralJeevanBimaBean.getPolicyTerm()) {
                        pr = prStrArr[arrCount];
                        prDouble = Double.parseDouble(pr);
                        // System.out.println("prDouble "+prDouble);
                        break;

                    }
                    arrCount++;
                }

            }
        } else if (saralJeevanBimaBean.getPremFreqOptions().equals("LPPT") && saralJeevanBimaBean.getPremPaymentTerm().equalsIgnoreCase("5")) {

            for (int ageCount = 18; ageCount <= 65; ageCount++) {
                for (int policyTermCount = 10; policyTermCount <= 40; policyTermCount++) {
                    if (ageCount == saralJeevanBimaBean.getAge() && policyTermCount == saralJeevanBimaBean.getPolicyTerm()) {
                        pr = prStrArr[arrCount];
                        prDouble = Double.parseDouble(pr);
                        //System.out.println("prDouble "+prDouble);
                        break;

                    }
                    arrCount++;
                }

            }
        } else {
            for (int ageCount = 18; ageCount <= 65; ageCount++) {
                for (int policyTermCount = 5; policyTermCount <= 40; policyTermCount++) {

                    if (ageCount == saralJeevanBimaBean.getAge() && policyTermCount == saralJeevanBimaBean.getPolicyTerm()) {
                        pr = prStrArr[arrCount];
                        prDouble = Double.parseDouble(pr);
                        // System.out.println("prDouble "+prDouble);
                        break;

                    }
                    arrCount++;

                }
            }
        }


        return prDouble;


//		double [] premiumArr=jeevandb.getBasic_PR_Arr(saralJeevanBimaBean.getPremFreqOptions());
//        int position=0;
//		double TabularPremiumRate=0;
//        for (int ageCount=18; ageCount<=65; ageCount++)
//        {
//            for (int policyTermCount=5; policyTermCount<=40; policyTermCount++) 
//            {
//                if(ageCount==saralJeevanBimaBean.getAge() && policyTermCount==saralJeevanBimaBean.getPolicyTerm())
//                {TabularPremiumRate=premiumArr[position];}
//                position++;
//            }
//        }
//        return TabularPremiumRate;
    }


    public double getStaffRebate() {
        double staff_Rebate;

        if (saralJeevanBimaBean.getStaffDisc() == true) {
            if (saralJeevanBimaBean.getPremiumFreq().equalsIgnoreCase("Single"))
                staff_Rebate = 0.02;
            else
                staff_Rebate = 0.05;
        } else
            staff_Rebate = 0;

        return staff_Rebate;

    }

    public double getLoadingFrqOfPremium() {

        double loadngFreqOfPremiums = 0;

        if (saralJeevanBimaBean.getPremiumFreq().equalsIgnoreCase("Yearly") || saralJeevanBimaBean.getPremiumFreq().equalsIgnoreCase("Single"))
            loadngFreqOfPremiums = 1;
        else if (saralJeevanBimaBean.getPremiumFreq().equalsIgnoreCase("Half Yearly"))
            loadngFreqOfPremiums = obj.HalfYearly_Modal_Factor;
        else if (saralJeevanBimaBean.getPremiumFreq().equalsIgnoreCase("Quarterly"))
            loadngFreqOfPremiums = obj.Quarterly_Modal_Factor;
        else
            loadngFreqOfPremiums = obj.Monthly_Modal_Factor;

        return loadngFreqOfPremiums;
    }

    public double getlargeSA(double SumAssured) {
        double LargeSa;
        if (SumAssured >= 1500000) {
            if (saralJeevanBimaBean.getPremFreqOptions().equals("LPPT")) {
                LargeSa = (0.3 * SumAssured / 1000);
            } else if (saralJeevanBimaBean.getPremFreqOptions().equals("Regular")) {
                LargeSa = (0.2 * SumAssured / 1000);
            } else {
                LargeSa = (0.8 * SumAssured / 1000);
            }
        } else {
            LargeSa = 0;
        }
        //System.out.println("LargeSa "+LargeSa);
        return LargeSa;
    }

    public double get_Premium_without_tax() {

        double prem = 0;
//		 System.out.println("\n getPr_Basic_FromDB() "+getPr_Basic_FromDB());
//		 System.out.println("saraljeevanbimabean.getsumassured() "+saraljeevanbimabean.getsumassured());
//		 System.out.println("getLoadingFrqOfPremium() "+getLoadingFrqOfPremium());
//		 System.out.println("1- getStaffRebate() "+(1- getStaffRebate()));
//		 System.out.println("getlargeSA(saraljeevanbimabean.getsumassured()  "+(getlargeSA(saraljeevanbimabean.getsumassured())));
//
//
//		 System.out.println("a "+(getPr_Basic_FromDB() * saraljeevanbimabean.getsumassured()/1000));
//		 System.out.println("(1-getStaffRebate()) "+(1-getStaffRebate()));
//		 System.out.println("");

        prem = (((getPr_Basic_FromDB() * saralJeevanBimaBean.getSumAssured_Basic() / 1000) - getlargeSA(saralJeevanBimaBean.getSumAssured_Basic())) * (1 - getStaffRebate()) * (getLoadingFrqOfPremium()));

        return prem;
        //return ( getPr_Basic_FromDB() *(1- getStaffRebate())* (saraljeevanbimabean.getsumassured() /1000 )* getLoadingFrqOfPremium());
    }

    public double get_premium_withtotalServiceTax(double premiumBasic) {
        double basicST = 0;
        if (saralJeevanBimaBean.isKerlaDisc() == true) {
            // System.out.println("premiumBasic1 "+premiumBasic);
            basicST = Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumBasic * (obj.servtx))));
            System.out.println("basicST1 " + basicST);

        } else {
            basicST = Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumBasic * (obj.serviceTax))));
        }

        basicST = basicST + (Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumBasic * (obj.serviceTax)))));
        System.out.println("basicST " + basicST);
        return (basicST);
    }

    public double getbasicServicetax(double premiumBasic) {
        double Servicetax;

        Servicetax = (get_premium_withtotalServiceTax(premiumBasic) - get_Premium_without_tax());
        return Servicetax;
    }

    /**************** Modified by Akshaya on 12-Feb-2015 end *************************/


    public double getDefOccuInterest(double SumAssured_Basic) {

        String[] prStrArr;
        String SMF = "1";


        if (saralJeevanBimaBean.getPremiumFreq().equals("Single")) {
            // prStrArr = commonForAllProd.split(ac.getExtraPremiumRate_Single(), ",");

            if (saralJeevanBimaBean.getPolicyTerm() > 30) {
                SMF = "17.29";
            } else {
                //  SMF = prStrArr[saralJeevanBimaBean.getPolicyTerm() - 1];
            }
        }
        return ((SumAssured_Basic / 1000) * 2) * (getLoadingForFreqOfPremiums("Basic") * Double.parseDouble(SMF)) / 1;
    }

    public double getMinesOccuInterest(double SumAssured_Basic) {
        return (SumAssured_Basic / 1000) * 2;
    }

    public double getLoadingForFreqOfPremiums(String cover) {
        if (cover.equals("Basic")) {
            if (saralJeevanBimaBean.getPremiumFreq().equals("Yearly") || saralJeevanBimaBean.getPremiumFreq().equals("Single")) {
                return obj.Yearly_Model_Factor;
            } else if (saralJeevanBimaBean.getPremiumFreq().equals("Half Yearly")) {
                return obj.HalfYearly_Modal_Factor;
            } else if (saralJeevanBimaBean.getPremiumFreq().equals("Quarterly")) {
                return obj.Quarterly_Modal_Factor;
            } else {
                return obj.Monthly_Modal_Factor;
            }
        } else {
            if (saralJeevanBimaBean.getPremiumFreq().equals("Yearly")) {
                return 1;
            } else if (saralJeevanBimaBean.getPremiumFreq().equals("Half Yearly")) {
                return 1.04;
            } else if (saralJeevanBimaBean.getPremiumFreq().equals("Quarterly")) {
                return 1.06;
            } else {
                return 1.068;
            }
        }

    }


    /*************************** Added By  Tushar Kotian on 3//8/2017****************/

    public double getBackDateInterest(double grossPremium, String bkDate) {

        try {

            // double indigoRate=7.5;
            /**
             * Modified by Akshaya. Rate Change from 1-APR-2015
             */
            double ST = 0;
            if (saralJeevanBimaBean.getJkResident()) {
                ST = obj.serviceTaxJkResident;
            } else {
                ST = obj.serviceTax;
            }
            /******************* Modified by Akshaya on 14-APR-2015 start **********/
//			double indigoRate = 10;

            /******************* Modified by Saurabh Jain on 08-APR-2019 start **********/
            // double indigoRate = 8.75;
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

    public double getCompoundAmount(double monthsBetween,
                                    double netPremWithoutST, double indigoRate, int compoundFreq) {
        double compoundAmount = netPremWithoutST
                * Math.pow((1 + (indigoRate / (100 * compoundFreq))),
                (compoundFreq * (monthsBetween / 12)))
                - netPremWithoutST;
        System.out.println("compoundAmount " + compoundAmount);
        return compoundAmount;
        //System.out.println("compoundAmount "+compoundAmount);
    }
    /*public double getNonGSV_surrndr_val_SSV_8(String PlanName,int PolicyTerm,int policyyear,int sumassured,String nongrntdDeathNenft_8Percent)
	{
		double NonGSV_surrndr_val_SSV_8 = 0 ;
		double [] SSV_factor_array  = ac.getSSV(PolicyTerm) ;
		double [] PUV_factor_array = ac.getPUV(PlanName) ;

		double SSV_Factor = SSV_factor_array[policyyear-1];
		double PUV_Factor = PUV_factor_array[policyyear-1];

		NonGSV_surrndr_val_SSV_8 = ((sumassured*(PUV_Factor/1000))+Double.parseDouble(nongrntdDeathNenft_8Percent))*SSV_Factor ;

		return NonGSV_surrndr_val_SSV_8;

	}*/


	/*public double getNonGSV_surrndr_val_SSV_4(String PlanName,int PolicyTerm,int policyyear,int sumassured,String nongrntdDeathNenft_4Percent)
	{
		double NonGSV_surrndr_val_SSV_4 = 0 ;
		double [] SSV_factor_array  = ac.getSSV(PolicyTerm) ;
		double [] PUV_factor_array = ac.getPUV(PlanName) ;

		double SSV_Factor = SSV_factor_array[policyyear-1];
		double PUV_Factor = PUV_factor_array[policyyear-1];

		NonGSV_surrndr_val_SSV_4 = ((sumassured*(PUV_Factor/1000))+Double.parseDouble(nongrntdDeathNenft_4Percent))*SSV_Factor ;

		return NonGSV_surrndr_val_SSV_4;

	}*/

	/*public double get_Premium_Without_ST_WithoutLoadingFreq()
	{
		int divFactor=0;
		if(smartMoneyBackGoldBean.getPremiumFreq().equals("Yearly") ||  smartMoneyBackGoldBean.getPremiumFreq().equals("Single"))
		{divFactor=1;}
		else if (smartMoneyBackGoldBean.getPremiumFreq().equals("Half Yearly")){divFactor=2;}
		else if(smartMoneyBackGoldBean.getPremiumFreq().equals("Quarterly")){divFactor=4;}
		//For Monthly Premium Frequency Mode
		else{divFactor=12;}
		return ( getPR_Basic_FromDB() *(1- getStaffRebate("basic"))- getSA_Rebate_Basic() +( getNSAP() + getEMR()))* smartMoneyBackGoldBean.getSumAssured_Basic() /1000;
//        return ( getPR_Basic_FromDB() *(1- getStaffRebate("basic"))- getSA_Rebate_Basic() +( getNSAP() + getEMR()))* smartMoneyBackGoldBean.getSumAssured_Basic() /1000;

	}*/

    /*** Added by Priyanka Warekar - 31-08-2018 - start *******/
    public double getServiceTaxMines(double MinesOccuInterest) {
        return Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getRoundOffLevel2(commonForAllProd.getStringWithout_E(MinesOccuInterest * (obj.serviceTax + obj.servtx)))));
    }

    /*** Added by Priyanka Warekar - 31-08-2018 - end *******/
    public double getStaffRebate(boolean IsStaff) {
        double StaffRebate = 0;
        if (IsStaff) {
            StaffRebate = 0.06;
        } else {
            StaffRebate = 0.00;
        }
        return StaffRebate;

    }

}
