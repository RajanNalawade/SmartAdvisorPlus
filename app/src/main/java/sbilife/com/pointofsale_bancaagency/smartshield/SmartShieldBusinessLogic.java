package sbilife.com.pointofsale_bancaagency.smartshield;

import sbilife.com.pointofsale_bancaagency.common.CommonForAllProd;

class SmartShieldBusinessLogic {
    // Declare Variables
    private final SmartShieldBean smartShieldBean;
    private String RecStoreName;
    private int colaccCI = 0;
    private CommonForAllProd commonForAllProd;
    private final SmartShieldProperties prop;

    // Store User Entered data in a Bean Object.Also initialize CommonForAllProd
    // class objects to accces common methods for all products
    public SmartShieldBusinessLogic(SmartShieldBean smartShieldBean) {
        this.smartShieldBean = smartShieldBean;
        commonForAllProd = new CommonForAllProd();
        prop = new SmartShieldProperties();
    }

    /***** added by vrushali start */
    public double getDiscPercentage(String Cover) {
        if (smartShieldBean.getStaffDisc()) {
            if (Cover.equals("CRITI")) {
                if (smartShieldBean.getPremFreq().equals("Single")) {
                    //return 0.05;
                    return 0.015;
                } else {
//					return 0.02;
                    return 0.015;
                }
            } else {
                if (smartShieldBean.getPremFreq().equals("Single")) {
//					return 0.015;
                    return 0.065;
                } else {
//					return 0.015;
                    return 0.05;
                }
            }
        } else {
            return 0;
        }
    }

    public double getMinesOccuInterest(double SumAssured_Basic) {
        return (SumAssured_Basic / 1000) * 2;
    }

    /***** added by vrushali end */

    // Pass Cover Option to this method & get PR for that Cover Option
    public double getPR(String cover) {
        double PR = 0, PR_Yearly = 0;
        switch (cover) {
            case "basic":
                PR_Yearly = getPR_Basic_yearly();

                // System.out.println("Basic PR_Yearly  :  "+ PR_Yearly);

                break;
            case "accCI":
                PR_Yearly = getPR_AccCI_yearly();
                break;
            case "adb":
                PR_Yearly = getPR_ADB_yearly();
                break;
            case "atpdb":
                PR_Yearly = getPR_ATPDB_yearly();
                break;
        }
        // Depending on the premium frequency return PR
        switch (smartShieldBean.getPremFreq()) {
            case "Single":
            case "Yearly":
                PR = PR_Yearly;
                break;
            case "Half Yearly":
                PR = PR_Yearly * (0.52);
                break;
            case "Quarterly":
                PR = PR_Yearly * (26.5 / 100);
                break;
            case "Monthly":
                PR = PR_Yearly * (8.9 / 100);
                break;
        }


        //System.out.println("Basic PR for selected frequency[Not Rounded ]  :  "+ PR);


        return PR;
    }

    // Yearly Premium Calculation for Basic Cover Option
    public double getPR_Basic_yearly() {
        // System.out.println("getprbasicfromdb " +getPR_Basic_FromDB());
        if (smartShieldBean.getBasicSA() < 5000000) {
            return (getPR_Basic_FromDB() * smartShieldBean.getBasicSA() / 1000);
        } else if ((smartShieldBean.getBasicSA() >= 5000000)
                && (smartShieldBean.getBasicSA() < 10000000)) {
            return (getPR_Basic_FromDB() * smartShieldBean.getBasicSA() / 1000) * 0.93;
        } else if (!smartShieldBean.getPremFreq().equals("Single")) {
            return ((getPR_Basic_FromDB() * smartShieldBean.getBasicSA() / 1000) * 0.93)
                    - ((smartShieldBean.getBasicSA() / 1000) * 0.25);
        } else {
            return ((getPR_Basic_FromDB() * smartShieldBean.getBasicSA() / 1000) * 0.93)
                    - ((smartShieldBean.getBasicSA() / 1000) * 1.5);
        }
    }

    // Yearly Premium Calculation for Accelerated Critical Illness Cover Option
    // Option
    private double getPR_AccCI_yearly() {
        if (smartShieldBean.getPlanName().equals("Level Term Assurance")
                || smartShieldBean.getPlanName().equals(
                "Increasing Term Assurance")) {
            return getPR_AccCI_FromDB() * smartShieldBean.getAccCI_SA() / 1000;
        } else {
            return 0;
        }
    }

    // Yearly Premium Calculation for ADB Rider Option
    private double getPR_ADB_yearly() {
        if (smartShieldBean.getPlanName().equals("Level Term Assurance")
                || smartShieldBean.getPlanName().equals(
                "Increasing Term Assurance")) {
            return getPR_ADB_FromDB() * smartShieldBean.getADB_SA() / 100000;
        } else {
            return 0;
        }
    }

    // Yearly Premium Calculation for ATPDB Rider Option
    private double getPR_ATPDB_yearly() {
        if (smartShieldBean.getPlanName().equals("Level Term Assurance")
                || smartShieldBean.getPlanName().equals(
                "Increasing Term Assurance")) {
            return getPR_ATPDB_FromDB() * smartShieldBean.getATPDB_SA()
                    / 100000;
        } else {
            return 0;
        }
    }

    // Decides which RecordStore is to be used
    private String getRecStoreName_Basic() {
        switch (smartShieldBean.getPlanName()) {
            case "Level Term Assurance":
            case "Increasing Term Assurance":
                if (smartShieldBean.getType().equals("Smoker")) {
                    if (!smartShieldBean.getPremFreq().equals("Single")) {
                        if (smartShieldBean.getGender().equals("Male") || smartShieldBean.getGender().equals("Third Gender")) {
                            if (smartShieldBean.getPlanName().equals(
                                    "Level Term Assurance")) {
                                RecStoreName = "SS_LTASRPM";
                            } else {
                                RecStoreName = "SS_ITASRPM";
                            }
                            // Identify column of RecordSet related to accCI
                            colaccCI = 2;
                        } else {
                            if (smartShieldBean.getPlanName().equals(
                                    "Level Term Assurance")) {
                                RecStoreName = "SS_LTASRPF";
                            } else {
                                RecStoreName = "SS_ITASRPF";
                            }
                            // Identify column of RecordSet related to accCI
                            colaccCI = 4;
                        }
                    } else {
                        if (smartShieldBean.getGender().equals("Male") || smartShieldBean.getGender().equals("Third Gender")) {
                            if (smartShieldBean.getPlanName().equals(
                                    "Level Term Assurance")) {
                                RecStoreName = "SS_LTASSPM";
                            } else {
                                RecStoreName = "SS_ITASSPM";
                            }
                            // Identify column of RecordSet related to accCI
                            colaccCI = 6;
                        } else {
                            if (smartShieldBean.getPlanName().equals(
                                    "Level Term Assurance")) {
                                RecStoreName = "SS_LTASSPF";
                            } else {
                                RecStoreName = "SS_ITASSPF";
                            }
                            // Identify column of RecordSet related to accCI
                            colaccCI = 8;
                        }
                    }
                } else {
                    if (!smartShieldBean.getPremFreq().equals("Single")) {
                        if (smartShieldBean.getGender().equals("Male") || smartShieldBean.getGender().equals("Third Gender")) {
                            if (smartShieldBean.getPlanName().equals(
                                    "Level Term Assurance")) {
                                RecStoreName = "SS_LTANSRPM";
                            } else {
                                RecStoreName = "SS_ITANSRPM";
                            }
                            // Identify column of RecordSet related to accCI
                            colaccCI = 3;
                        } else {
                            if (smartShieldBean.getPlanName().equals(
                                    "Level Term Assurance")) {
                                RecStoreName = "SS_LTANSRPF";
                            } else {
                                RecStoreName = "SS_ITANSRPF";
                            }
                            // Identify column of RecordSet related to accCI
                            colaccCI = 5;
                        }
                    } else {
                        if (smartShieldBean.getGender().equals("Male") || smartShieldBean.getGender().equals("Third Gender")) {
                            if (smartShieldBean.getPlanName().equals(
                                    "Level Term Assurance")) {
                                RecStoreName = "SS_LTANSSPM";
                            } else {
                                RecStoreName = "SS_ITANSSPM";
                            }
                            // Identify column of RecordSet related to accCI
                            colaccCI = 7;
                        } else {
                            if (smartShieldBean.getPlanName().equals(
                                    "Level Term Assurance")) {
                                RecStoreName = "SS_LTANSSPF";
                            } else {
                                RecStoreName = "SS_ITANSSPF";
                            }
                            // Identify column of RecordSet related to accCI
                            colaccCI = 9;
                        }
                    }
                }
                break;
            // If plan is-> Decreasing Term Assurance[Loan Protection]
            case "Decreasing Term Assurance[Loan Protection]":
                // If Smoker
                if (smartShieldBean.getType().equals("Smoker")) {
                    switch (smartShieldBean.getLRI()) {
                        case "6%":
                            RecStoreName = "DTALPS6%";
                            break;
                        case "8%":
                            RecStoreName = "DTALPS8%";
                            break;
                        case "10%":
                            RecStoreName = "DTALPS10%";
                            break;
                        case "12%":
                            RecStoreName = "DTALPS12%";
                            break;
                        case "14%":
                            RecStoreName = "DTALPS14%";
                            break;
                        case "16%":
                            RecStoreName = "DTALPS16%";
                            break;
                        case "18%":
                            RecStoreName = "DTALPS18%";
                            break;
                        case "20%":
                            RecStoreName = "DTALPS20%";
                            break;
                    }
                }
                // If Non-Smoker
                else {
                    switch (smartShieldBean.getLRI()) {
                        case "6%":
                            RecStoreName = "DTALPNS6%";
                            break;
                        case "8%":
                            RecStoreName = "DTALPNS8%";
                            break;
                        case "10%":
                            RecStoreName = "DTALPNS10%";
                            break;
                        case "12%":
                            RecStoreName = "DTALPNS12%";
                            break;
                        case "14%":
                            RecStoreName = "DTALPNS14%";
                            break;
                        case "16%":
                            RecStoreName = "DTALPNS16%";
                            break;
                        case "18%":
                            RecStoreName = "DTALPNS18%";
                            break;
                        case "20%":
                            RecStoreName = "DTALPNS20%";
                            break;
                    }
                }
                break;
            // If plan is-> Decreasing Term Assurance[Income Protection]
            case "Decreasing Term Assurance[Family Income Protection]":
                if (smartShieldBean.getType().equals("Smoker")) {
                    RecStoreName = "SS_DTAFIPS";
                } else {
                    RecStoreName = "SS_DTAFIPNS";
                }
                break;
        }
        return RecStoreName;
    }

    // This method is used to select Basic PR from the Smart Shield Constant
    // Data
    private double getPR_Basic_FromDB() {
        getRecStoreName_Basic();
        double prDouble = 0;
        String pr, prStr;
        int arrCount = 0;
        String[] prStrArr;
        SmartShieldDB ac = new SmartShieldDB();
        prStr = ac.getBasicArray(RecStoreName);
        prStrArr = commonForAllProd.split(prStr, ",");
        //Here min age is 18 & max age is 60/min term is 5 & max term is 62
        for (int i = 18; i < 61; i++) {
            for (int j = 5; j < 63; j++) {
                if (i == smartShieldBean.getAge()
                        && j == smartShieldBean.getBasicTerm()) {
                    pr = prStrArr[arrCount];
                    prDouble = Double.parseDouble(pr);
                    break;
                }
                arrCount++;
            }
        }
        // System.out.println("PR ->> "+ prDouble);
        return prDouble;

    }

    // This method is used to select AccCI PR from the Smart Shield Constant
    // Data
    private double getPR_AccCI_FromDB() {
        double prDouble = 0;
        String pr, prStr;
        String[] prStrArr;
        SmartShieldDB ac = new SmartShieldDB();
        prStr = ac.getAccCIArray(colaccCI);
        prStrArr = commonForAllProd.split(prStr, ",");
        for (int i = 18; i < 61; i++) {
            if (smartShieldBean.getAge() == i) {
                pr = prStrArr[i - 18];
                prDouble = Double.parseDouble(pr);
                break;
            }
        }
        return prDouble;
    }

    // This method is used to select ADB Rider PR from the Smart Shield Constant
    // Data
    private double getPR_ADB_FromDB() {
        if (!smartShieldBean.getPremFreq().equals("Single")) {
            return 50;
        } else {
            int pr = 0;
            SmartShieldDB ac = new SmartShieldDB();
            int[] prArr = ac.getADBArr();
//			for (int i = 5; i < 31; i++)
            for (int i = 5; i < 58; i++) {
                if (i == smartShieldBean.getADB_Term()) {
                    pr = prArr[i - 5];
                    break;
                }
            }
            return pr;
        }
    }

    // This method is used to select ATPDB Rider PR from the Smart Shield
    // Constant Data
    private double getPR_ATPDB_FromDB() {
        if (!smartShieldBean.getPremFreq().equals("Single")) {
            return 40;
        } else {
            int pr = 0;
            SmartShieldDB ac = new SmartShieldDB();
            int[] prArr = ac.getATPDBArr();
//			for (int i = 5; i < 31; i++)
            for (int i = 5; i < 58; i++) {
                if (i == smartShieldBean.getATPDB_Term()) {
                    pr = prArr[i - 5];
                    break;
                }
            }
            return pr;
        }
    }

    // This method is used to get Staff Discount for Basic Cover Option
    public double getBasicStaffDisc(double basicPrem) {
        if (smartShieldBean.getStaffDisc()) {
            if (!smartShieldBean.getPremFreq().equals("Single")) {
                if ((smartShieldBean.getBasicTerm()) > 4
                        && (smartShieldBean.getBasicTerm() < 10)) {
                    return basicPrem * 15 / 100;
                } else if ((smartShieldBean.getBasicTerm()) > 9
                        && (smartShieldBean.getBasicTerm() < 15)) {
                    return basicPrem * 20 / 100;
                } else {
                    return basicPrem * 25 / 100;
                }
            } else {
                return basicPrem * 2 / 100;
            }
        } else {
            return 0;
        }
    }

    // This method is used to get Staff Discount for AccCI Cover Option
    public double getAccCIStaffDisc(double accCIPrem) {
        if (smartShieldBean.getStaffDisc()) {
            return accCIPrem * 15 / 100;
        } else {
            return 0;
        }
    }

    // This method is used to get Staff Discount for Rider Option
    public double getRiderDisc(double riderPrem, String whichRider) {
        int riderTerm;
        if (whichRider.equals("ADB")) {
            riderTerm = smartShieldBean.getADB_Term();
        } else {
            riderTerm = smartShieldBean.getATPDB_Term();
        }
        if (smartShieldBean.getStaffDisc()) {
            if (smartShieldBean.getPlanName().equals("Level Term Assurance")
                    || smartShieldBean.getPlanName().equals(
                    "Increasing Term Assurance")) {
                if (!smartShieldBean.getPremFreq().equals("Single")) {
                    if ((riderTerm > 4) && (riderTerm < 10)) {
                        return riderPrem * 15 / 100;
                    } else if ((riderTerm > 9) && (riderTerm < 15)) {
                        return riderPrem * 20 / 100;
                    } else {
                        return riderPrem * 25 / 100;
                    }
                } else {
                    return riderPrem * 2 / 100;
                }
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------

    private double getBasicPremium_Yearly() {
        return getPR_Basic_FromDB() * smartShieldBean.getBasicSA() / 1000;
    }

    public double getDisc_BasicPremium_SelFreq() {
        if (smartShieldBean.getStaffDisc()) {
            // if(!smartShieldBean.getPremFreq().equals("Single"))
            // {
            // if(smartShieldBean.getBasicTerm()>=5 &&
            // smartShieldBean.getBasicTerm() <= 9)
            // {return getBasicPremium_Yearly()*15/100;}
            // else if(smartShieldBean.getBasicTerm()>=10 &&
            // smartShieldBean.getBasicTerm() <= 14)
            // {return getBasicPremium_Yearly()*20/100;}
            // else
            // {return getBasicPremium_Yearly()*25/100;}
            // }
            // else{return getBasicPremium_Yearly()*2/100;}
            // Modified by vrushali
            if (!smartShieldBean.getPremFreq().equals("Single")) {
                return getBasicPremium_Yearly() * 5 / 100;
            } else {
                return getBasicPremium_Yearly() * 2 / 100;
            }

        } else {
            return 0;
        }
    }

    public double getDiscAfterFreqLoading_Basic_SelFreq(double premAfterLSA_disc) {
        if (smartShieldBean.getPremFreq().equals("Yearly")) {
            return premAfterLSA_disc;
        }
        switch (smartShieldBean.getPremFreq()) {
            case "Half Yearly":
                return premAfterLSA_disc * 51 / 100;
            case "Quarterly":
                return premAfterLSA_disc * 26 / 100;

            // For Monthly Premium Frequency Mode
            case "Monthly":
                return premAfterLSA_disc * 8.5 / 100;

            // For Single Mode of Premium
            default:
                return premAfterLSA_disc;
        }
    }

    public double getDiscAfterFreqLoading_Rider_SelFreq(double premAfterLSA_disc) {
        if (smartShieldBean.getPremFreq().equals("Yearly")) {
            return premAfterLSA_disc;
        }
        switch (smartShieldBean.getPremFreq()) {
            case "Half Yearly":
                return premAfterLSA_disc * 52 / 100;
            case "Quarterly":
                return premAfterLSA_disc * 26.5 / 100;

            // For Monthly Premium Frequency Mode
            case "Monthly":
                return premAfterLSA_disc * 8.9 / 100;

            // For Single Mode of Premium
            default:
                return premAfterLSA_disc;
        }
    }

    private double getAccCIPremium_Yearly() {
        if (smartShieldBean.getAccCI_Status() && (smartShieldBean.getPlanName().equals("Level Term Assurance") || smartShieldBean.getPlanName().equals("Increasing Term Assurance"))) {
            return getPR_AccCI_FromDB() * smartShieldBean.getAccCI_SA() / 1000;
        } else {
            return 0;
        }
    }

    public double getDisc_AccCIPremium_SelFreq() {
        if (smartShieldBean.getStaffDisc()) {
            // For Regular Mode of Premium Frequency
            // if(!smartShieldBean.getPremFreq().equals("Single"))
            // {
            // return getAccCIPremium_Yearly() *15/100;
            // }
            // //For Single Mode of Premium Frequency
            // else{return getAccCIPremium_Yearly() *2/100;}
            // Modified by vrushali
            if (!smartShieldBean.getPremFreq().equals("Single")) {
                return getAccCIPremium_Yearly() * 5 / 100;
            }
            // For Single Mode of Premium Frequency
            else {
                return getAccCIPremium_Yearly() * 2 / 100;
            }
        } else {
            return 0;
        }
    }

    private double getADBPremium_Yearly() {
        if (smartShieldBean.getADB_Status() && (smartShieldBean.getPlanName().equals("Level Term Assurance") || smartShieldBean.getPlanName().equals("Increasing Term Assurance"))) {
            return getPR_ADB_FromDB() * smartShieldBean.getADB_SA() / 100000;
        } else {
            return 0;
        }
    }

    public double getDisc_ADBPremium_SelFreq() {
        if (smartShieldBean.getStaffDisc() && (smartShieldBean.getPlanName().equals("Level Term Assurance") || smartShieldBean.getPlanName().equals("Increasing Term Assurance"))) {
            // For Regular Mode of Premium Frequency
            // Modified by vrushali
            if (!smartShieldBean.getPremFreq().equals("Single")) {
                //                if(smartShieldBean.getADB_Term()>=5 && smartShieldBean.getADB_Term()<=9)
                // {return getADBPremium_Yearly() *15/100;}
                //                else if(smartShieldBean.getADB_Term()>=10 && smartShieldBean.getADB_Term()<=14)
                // {return getADBPremium_Yearly() *20/100;}
                // else{return getADBPremium_Yearly() *25/100;}
                return getADBPremium_Yearly() * 5 / 100;
            }
            // For Single Mode of Premium Frequency
            else {
                return getADBPremium_Yearly() * 2 / 100;
            }
        } else {
            return 0;
        }
    }

    private double getATPDBPremium_Yearly() {
        if (smartShieldBean.getATPDB_Status() && (smartShieldBean.getPlanName().equals("Level Term Assurance") || smartShieldBean.getPlanName().equals("Increasing Term Assurance"))) {
            return getPR_ATPDB_FromDB() * smartShieldBean.getATPDB_SA()
                    / 100000;
        } else {
            return 0;
        }
    }

    public double getDisc_ATPDBPremium_SelFreq() {
        if (smartShieldBean.getStaffDisc()
                && (smartShieldBean.getPlanName()
                .equals("Level Term Assurance") || smartShieldBean
                .getPlanName().equals("Increasing Term Assurance"))) {
            // For Regular Mode of Premium Frequency
            // Modified by vrushali
            if (!smartShieldBean.getPremFreq().equals("Single")) {
                //                if(smartShieldBean.getATPDB_Term()>=5 && smartShieldBean.getATPDB_Term()<=9)
                // {return getATPDBPremium_Yearly() *15/100;}
                //                else if(smartShieldBean.getATPDB_Term()>=10 && smartShieldBean.getATPDB_Term()<=14)
                // {return getATPDBPremium_Yearly()*20/100;}
                // else
                // {return getATPDBPremium_Yearly() *25/100;}
                return getATPDBPremium_Yearly() * 5 / 100;
            }
            // For Sinle Mode of Premium Frequency
            else {
                return getATPDBPremium_Yearly() * 2 / 100;
            }
        } else {
            return 0;
        }
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------

    public double getTotalInstallPremWithDisc(double totalAnnualPremWithDisc) {

        System.out
                .println("totalAnnualPremWithDisc " + totalAnnualPremWithDisc);
        switch (smartShieldBean.getPremFreq()) {
            case "Half Yearly":
                return totalAnnualPremWithDisc * 52 / 100;
            case "Quarterly":
                return totalAnnualPremWithDisc * 26.5 / 100;

            // For Monthly Premium Frequency Mode
            case "Monthly":
                return totalAnnualPremWithDisc * 8.9 / 100;
            default:
                return totalAnnualPremWithDisc;
        }
    }

    // double getServiceTax()
    // {
    // if(smartShieldBean.isJKResident())
    // return prop.serviceTaxJKResident;
    // else
    // return prop.serviceTax;
    // }

    /*** modified by Akshaya on 20-MAY-16 start **/
    public double getServiceTax(double premiumWithoutST, boolean JKResident,
                                String type) {
        switch (type) {
            case "basic":
                // System.out.println(commonForAllProd.getStringWithout_E(premiumWithoutST*prop.serviceTax));
                if (JKResident) {

                    double a = Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getRoundOffLevel2(commonForAllProd.getStringWithout_E(premiumWithoutST * prop.serviceTaxJKResident))));
                    return Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getRoundOffLevel2(commonForAllProd.getStringWithout_E(premiumWithoutST * prop.serviceTaxJKResident))));

                } else {
                    return Double.parseDouble(commonForAllProd
                            .getRoundUp(commonForAllProd
                                    .getRoundOffLevel2(commonForAllProd
                                            .getStringWithout_E(premiumWithoutST
                                                    * prop.serviceTax))));
                }
            case "SBC":
                if (JKResident)
                    return 0;
                else {
                    return Double.parseDouble(commonForAllProd
                            .getRoundUp(commonForAllProd
                                    .getStringWithout_E(premiumWithoutST
                                            * prop.SBCServiceTax)));
                }
            case "KERALA":
                return Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumWithoutST * prop.KerlaServiceTax)));
            default:
// KKC

                if (JKResident)
                    return 0;
                else {
                    return Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(premiumWithoutST * prop.KKCServiceTax)));
                }
        }

    }

    /*** modified by Akshaya on 20-MAY-16 end **/

    private double getPR_Criti_FromDB() {
        double prDouble = 0;
        String pr, prStr;
        String[] prStrArr;
        SmartShieldDB ac = new SmartShieldDB();
        if (smartShieldBean.getPremFreq().equals("Single"))
            prStr = ac.getCriti_SingleArr();
        else
            prStr = ac.getCriti_RegularArr();
        prStrArr = commonForAllProd.split(prStr, ",");
        for (int i = 18; i < 61; i++) {
            if (smartShieldBean.getAge() == i) {
                pr = prStrArr[i - 18];
                prDouble = Double.parseDouble(pr);
                break;
            }
        }
        return prDouble;
    }

    public double getBasicPremium_Yearly(String cover) {
        switch (cover) {
            case "basic":
//			double a  = (getPR_Basic_FromDB())*(smartShieldBean.getBasicSA()/1000);

                return (getPR_Basic_FromDB()) * (smartShieldBean.getBasicSA() / 1000);
            case "ADB":

                double a, b, c, d;

//			a= getPR_ADB_FromDB();
//			b=smartShieldBean.getADB_SA();
//			c=100000;
//			d=b/c;
//			double e = (getPR_ADB_FromDB())*(smartShieldBean.getADB_SA()/100000); ;
                return (getPR_ADB_FromDB()) * (smartShieldBean.getADB_SA() / 100000);


            case "ATPD":
                return (getPR_ATPDB_FromDB()) * (smartShieldBean.getATPDB_SA() / 100000);
            default:
                return (getPR_Criti_FromDB()) * (smartShieldBean.getAccCI_SA() / 1000);
        }


    }

    public double getLargeSADiscount(double premium_Basic_WithoutDisc_Yearly) {
        if (smartShieldBean.getBasicSA() < 5000000) {
            return premium_Basic_WithoutDisc_Yearly * 0;
        } else if (smartShieldBean.getBasicSA() >= 5000000
                && smartShieldBean.getBasicSA() < 10000000) {
            return premium_Basic_WithoutDisc_Yearly * 0.10;
        } else if (smartShieldBean.getBasicSA() >= 10000000
                && smartShieldBean.getBasicSA() < 50000000) {
            return premium_Basic_WithoutDisc_Yearly * 0.25;
        } else {
            return premium_Basic_WithoutDisc_Yearly * 0.30;
        }
    }

    /*** Added by Priyanka Warekar - 31-08-2018 - start *******/
    public double getServiceTaxMines(double MinesOccuInterest) {
        return Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getRoundOffLevel2(commonForAllProd.getStringWithout_E(MinesOccuInterest * (prop.serviceTax + prop.SBCServiceTax)))));
    }

}
