package sbilife.com.pointofsale_bancaagency.saralswadhan;

import sbilife.com.pointofsale_bancaagency.common.CommonForAllProd;

class SaralSwadhanPlusBusinessLogic {

    private CommonForAllProd comm = null;
    private SaralSwadhanPlusProperties ssp_prop = null;
    private SaralSwadhanPlusBean saralSwadhanPlusBean = null;
    private SaralSwadhanPlusDB saralSwadhanPlusDB = null;

    private String month_E = null,
            year_F = null,
            age_H = null,
            age_group = null;

    // 9998888909090
    public SaralSwadhanPlusBusinessLogic() {
        comm = new CommonForAllProd();
        ssp_prop = new SaralSwadhanPlusProperties();
        saralSwadhanPlusBean = new SaralSwadhanPlusBean();
        saralSwadhanPlusDB = new SaralSwadhanPlusDB();
    }

    public int setMaturityAge(int policyterm, int age) {
        return (policyterm + age);
    }

    public String setAgeGroup(int age) {
        if (age >= 18 && age <= 30)
            age_group = "18-30";
        else if (age >= 31 && age <= 35)
            age_group = "31-35";
        else if (age >= 36 && age <= 40)
            age_group = "36-40";
        else if (age >= 41 && age <= 45)
            age_group = "41-45";
        else if (age >= 46 && age <= 50)
            age_group = "46-50";
        else
            age_group = "51-55";

        return age_group;
    }

    private String getAgeGroup() {
        return age_group;
    }

    public double setSumAssured(int policyTerm, double premium) {
        String ageGrp = getAgeGroup();
        if (policyTerm == 10) {
            if (ageGrp.equalsIgnoreCase("18-30"))
                return 80 * premium;
            else if (ageGrp.equalsIgnoreCase("31-35"))
                return 65 * premium;
            else if (ageGrp.equalsIgnoreCase("36-40"))
                return 50 * premium;
            else if (ageGrp.equalsIgnoreCase("41-45"))
                return 35 * premium;
            else if (ageGrp.equalsIgnoreCase("46-50"))
                return 25 * premium;
            else
                return 20 * premium;
        } else {
            if (ageGrp.equalsIgnoreCase("18-30"))
                return 95 * premium;
            else if (ageGrp.equalsIgnoreCase("31-35"))
                return 70 * premium;
            else if (ageGrp.equalsIgnoreCase("36-40"))
                return 55 * premium;
            else if (ageGrp.equalsIgnoreCase("41-45"))
                return 40 * premium;
            else if (ageGrp.equalsIgnoreCase("46-50"))
                return 30 * premium;
            else
                return 20 * premium;
        }

    }

    public double setAnnualPrem(double premium, int year_F) {
        if (year_F <= ssp_prop.premiumPayingTerm)
            return premium;
        else
            return 0;
    }

    public double setSumAssured(double sumAssured, int year_F, int policyTerm) {
        if (year_F <= policyTerm)
            return sumAssured;
        else
            return 0;
    }

    public double setBenefitPaybleAtDeath(double sumAssured, int year_F,
                                          int policyTerm) {
        if (year_F <= policyTerm)
            return sumAssured;
        else
            return 0;
    }

    public double setbenefitPaybleAtMaturity(double premium, int year_F,
                                             int policyTerm) {
        if (policyTerm == 10 && year_F == ssp_prop.premiumPayingTerm)
            return ((premium * 10) * 1);
        else if (policyTerm == 15 && year_F == policyTerm)
            return ((premium * 10) * 1.15);
        else
            return 0;
    }

    /******************* Modified by Akshaya on 05-FEB-2015 Start **********/

    public double setSAMF(int policyTerm) {
        String ageGrp = getAgeGroup();
        if (policyTerm == 10) {
            if (ageGrp.equalsIgnoreCase("18-30"))
                return 80;
            else if (ageGrp.equalsIgnoreCase("31-35"))
                return 65;
            else if (ageGrp.equalsIgnoreCase("36-40"))
                return 50;
            else if (ageGrp.equalsIgnoreCase("41-45"))
                return 35;
            else if (ageGrp.equalsIgnoreCase("46-50"))
                return 25;
            else
                return 20;
        } else {
            if (ageGrp.equalsIgnoreCase("18-30"))
                return 95;
            else if (ageGrp.equalsIgnoreCase("31-35"))
                return 70;
            else if (ageGrp.equalsIgnoreCase("36-40"))
                return 55;
            else if (ageGrp.equalsIgnoreCase("41-45"))
                return 40;
            else if (ageGrp.equalsIgnoreCase("46-50"))
                return 30;
            else
                return 20;
        }
    }

    public double setSumAssured(double SAMF, double premium) {

        return SAMF * premium;

        // String ageGrp=getAgeGroup();
        // if(policyTerm==10)
        // {
        // if(ageGrp.equalsIgnoreCase("18-30"))
        // return 80*premium;
        // else if(ageGrp.equalsIgnoreCase("31-35"))
        // return 65*premium;
        // else if(ageGrp.equalsIgnoreCase("36-40"))
        // return 50*premium;
        // else if(ageGrp.equalsIgnoreCase("41-45"))
        // return 35*premium;
        // else if(ageGrp.equalsIgnoreCase("46-50"))
        // return 25*premium;
        // else
        // return 20*premium;
        // }
        // else
        // {
        // if(ageGrp.equalsIgnoreCase("18-30"))
        // return 95*premium;
        // else if(ageGrp.equalsIgnoreCase("31-35"))
        // return 70*premium;
        // else if(ageGrp.equalsIgnoreCase("36-40"))
        // return 55*premium;
        // else if(ageGrp.equalsIgnoreCase("41-45"))
        // return 40*premium;
        // else if(ageGrp.equalsIgnoreCase("46-50"))
        // return 30*premium;
        // else
        // return 20*premium;
        // }

    }

    public double getServiceTax(double premiumWithoutST, String type) {
        if (type.equals("basic")) {
//	 			System.out.println("nnn "+ (premiumWithoutST*prop.serviceTax));
            return Double.parseDouble(comm.getRoundUp(comm.getRoundOffLevel2(comm.getStringWithout_E(premiumWithoutST * ssp_prop.serviceTax))));
        } else if (type.equals("SBC")) {
            return Double.parseDouble(comm.getRoundUp(comm.getStringWithout_E(premiumWithoutST * ssp_prop.SBCServiceTax)));
        }
        //  Added By Saurabh Jain on 15/05/2019 Start
        else if (type.equals("KERALA")) {

            return Double.parseDouble(comm.getRoundUp(comm.getStringWithout_E(premiumWithoutST * ssp_prop.kerlaServiceTax)));

        }
        //  Added By Saurabh Jain on 15/05/2019 End
        else //KKC
        {
            return Double.parseDouble(comm.getRoundUp(comm.getStringWithout_E(premiumWithoutST * ssp_prop.KKCServiceTax)));
        }

    }


    public double getServiceTaxSecondYear(double premiumWithoutST, String type) {
        if (type.equals("basic")) {
            return Double.parseDouble(comm.getRoundUp(comm.getRoundOffLevel2(comm.getStringWithout_E(premiumWithoutST * ssp_prop.serviceTaxSecondYear))));
        } else if (type.equals("SBC")) {
            return Double.parseDouble(comm.getRoundUp(comm.getStringWithout_E(premiumWithoutST * ssp_prop.SBCServiceTaxSecondYear)));
        }
        //  Added By Saurabh Jain on 15/05/2019 Start
        else if (type.equals("KERALA")) {
            return Double.parseDouble(comm.getRoundUp(comm.getStringWithout_E(premiumWithoutST * ssp_prop.kerlaServiceTaxSecondYear)));

        }
        //  Added By Saurabh Jain on

        else //KKC
        {
            return Double.parseDouble(comm.getRoundUp(comm.getStringWithout_E(premiumWithoutST * ssp_prop.KKCServiceTaxSecondYear)));
        }

    }


    /******************* Modified by Akshaya on 05-FEB-2015 End **********/

    public double setGuaranteedSurrenderValue(double sumcummulativePremiumPaid,
                                              int year_F, int policyTerm) {

        String[] prStrArr;
        double prDouble = 0;
        int arrCount = 0;
        String pr = null;

        if (policyTerm == 10) {


            prStrArr = comm.split(saralSwadhanPlusDB.getGSVRate_10(), ",");

        } else {

            prStrArr = comm.split(saralSwadhanPlusDB.getGSVRate_15(), ",");
        }

        prDouble = Double.parseDouble(prStrArr[year_F - 1]);

				/*for (int i=1; i <=30;i++)
		        {
		            for (int j = 10; j <=30; j++)
		            {
		                if(i==year_F && j==saralSwadhanPlusBean.getPolicy_Term())
		                {
		                    pr=prStrArr[arrCount];
		                    prDouble=Double.parseDouble(pr);
		                    break;
		                }
		                arrCount++;
		            }
		        }*/

        return sumcummulativePremiumPaid * prDouble;
    }

    public double setNonGuaranteedSurrenderValue(int year_F, double sumcummulativePremiumPaid, int policyTerm) {

        String[] prStrArr;
        double prDouble = 0;
        int arrCount = 0;
        String pr = null;

        if (policyTerm == 10) {


            prStrArr = comm.split(saralSwadhanPlusDB.getSSVRate_10(), ",");
            prDouble = Double.parseDouble(prStrArr[year_F - 1]);
            return sumcummulativePremiumPaid * 1 * prDouble;

        } else {

            prStrArr = comm.split(saralSwadhanPlusDB.getSSVRate_15(), ",");
            prDouble = Double.parseDouble(prStrArr[year_F - 1]);
            return sumcummulativePremiumPaid * 1.15 * prDouble;
        }


    }

}
