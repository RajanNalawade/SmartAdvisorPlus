/***************************************************************************************************************
 Author-> Akshaya Mirajkar
 ***************************************************************************************************************/
package sbilife.com.pointofsale_bancaagency.saralshield;

import sbilife.com.pointofsale_bancaagency.common.CommonForAllProd;


class SaralShieldBusinessLogic {
    // Class Variable Declaration
    private SaralShieldBean saralShieldBean = null;
    private CommonForAllProd commonForAllProd = null;
    private SaralShieldProperties prop = null;

    // Store User Entered Data in a Bean Object
    public SaralShieldBusinessLogic(SaralShieldBean saralShieldBean) {
        this.saralShieldBean = saralShieldBean;
        commonForAllProd = new CommonForAllProd();
        prop = new SaralShieldProperties();
    }

    public SaralShieldBean getSaralShieldBean() {
        return saralShieldBean;
    }

    public double getPremWithoutStaffDisc_Basic() {
        if (saralShieldBean.getPremFreq().equals("Half Yearly")) {
            return getPremiumYearly_Basic() * 52 / 100;
        } else if (saralShieldBean.getPremFreq().equals("Quarterly")) {
            return getPremiumYearly_Basic() * 26.5 / 100;
        }
        // For Monthly Premium Frequency Mode
        else if (saralShieldBean.getPremFreq().equals("Monthly")) {
            return getPremiumYearly_Basic() * 8.9 / 100;
        }
        // For Yearly Or Single Mode of Premium
        else {
            return getPremiumYearly_Basic();
        }
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    public double getBasicPremium_Yearly() {
        return getPR_Basic_FromDB() * saralShieldBean.getBasicSA()
                / 1000;
    }

    // public double getDisc_BasicPremium_SelFreq()
    // {
    // if(saralShieldBean.getStaffDisc())
    // {
    // if(!saralShieldBean.getPremFreq().equals("Single"))
    // {
    // if(saralShieldBean.getBasicTerm() >=5 &&
    // saralShieldBean.getBasicTerm() <= 9)
    // {return getBasicPremium_Yearly()*15/100;}
    // else if(saralShieldBean.getBasicTerm() >=10 &&
    // saralShieldBean.getBasicTerm() <= 14)
    // {return getBasicPremium_Yearly()*20/100;}
    // else{return getBasicPremium_Yearly()*25/100;}
    // }else{return getBasicPremium_Yearly()*2/100;}
    // }else{return 0;}
    // }

    public double getDisc_BasicPremium_SelFreq() {
        if (saralShieldBean.getStaffDisc()) {
            if (!saralShieldBean.getPremFreq().equals("Single")) {
                return getBasicPremium_Yearly() * 5 / 100;
            } else {
                return getBasicPremium_Yearly() * 2 / 100;
            }
        } else {
            return 0;
        }
    }

    public double getDiscAfterFreqLoading_Basic_SelFreq() {
        if (saralShieldBean.getPremFreq().equals("Yearly")) {
            return getDisc_BasicPremium_SelFreq();
        }
        if (saralShieldBean.getPremFreq().equals("Half Yearly")) {
            return getDisc_BasicPremium_SelFreq() * 52 / 100;
        } else if (saralShieldBean.getPremFreq().equals("Quarterly")) {
            return getDisc_BasicPremium_SelFreq() * 26.5 / 100;
        }
        // For Monthly Premium Frequency Mode
        else if (saralShieldBean.getPremFreq().equals("Monthly")) {
            return getDisc_BasicPremium_SelFreq() * 8.9 / 100;
        }
        // For Single Mode of Premium
        else {
            return 0;
        }
    }

    public double getADBPremium_Yearly() {
        if (saralShieldBean.getADB_Status()
                && saralShieldBean.getPlanName().equals("Level Term Assurance")) {
            return getPR_Rider_FromDB("ADB")
                    * saralShieldBean.getADB_SA() / 100000;
        } else {
            return 0;
        }
    }

    // public double getDisc_ADBPremium_SelFreq()
    // {
    // if(saralShieldBean.getStaffDisc() &&
    // saralShieldBean.getPlanName().equals("Level Term Assurance"))
    // {
    // if(saralShieldBean.getPremFreq().equals("Yearly") ||
    // saralShieldBean.getPremFreq().equals("Half Yearly") ||
    // saralShieldBean.getPremFreq().equals("Quarterly") ||
    // saralShieldBean.getPremFreq().equals("Monthly"))
    // {
    // if(saralShieldBean.getADB_Term()==5 ||
    // saralShieldBean.getADB_Term()==6 ||
    // saralShieldBean.getADB_Term()==7 ||
    // saralShieldBean.getADB_Term()==8 ||
    // saralShieldBean.getADB_Term()==9)
    // {return getADBPremium_Yearly() *15/100;}
    // else if(saralShieldBean.getADB_Term()==10 ||
    // saralShieldBean.getADB_Term()==11 ||
    // saralShieldBean.getADB_Term()==12 ||
    // saralShieldBean.getADB_Term()==13 ||
    // saralShieldBean.getADB_Term()==14)
    // {return getADBPremium_Yearly() *20/100;}
    // else
    // {return getADBPremium_Yearly() *25/100;}
    // }
    // else{return getADBPremium_Yearly() *2/100;}
    // }else{return 0;}
    // }

    public double getDisc_ADBPremium_SelFreq() {
        if (saralShieldBean.getStaffDisc()
                && saralShieldBean.getPlanName().equals("Level Term Assurance")) {
            if (!saralShieldBean.getPremFreq().equals("Single")) {
                return getADBPremium_Yearly() * 5 / 100;
            } else {
                return getADBPremium_Yearly() * 2 / 100;
            }
        } else {
            return 0;
        }
    }

    public double getATPDBPremium_Yearly() {
        if (saralShieldBean.getATPDB_Status()
                && saralShieldBean.getPlanName().equals("Level Term Assurance")) {
            return getPR_Rider_FromDB("ATPDB")
                    * saralShieldBean.getATPDB_SA() / 100000;
        } else {
            return 0;
        }
    }

    // public double getDisc_ATPDBPremium_SelFreq()
    // {
    // if(saralShieldBean.getStaffDisc() &&
    // saralShieldBean.getPlanName().equals("Level Term Assurance"))
    // {
    // if(saralShieldBean.getPremFreq().equals("Yearly") ||
    // saralShieldBean.getPremFreq().equals("Half Yearly") ||
    // saralShieldBean.getPremFreq().equals("Quarterly") ||
    // saralShieldBean.getPremFreq().equals("Monthly"))
    // {
    // if(saralShieldBean.getATPDB_Term()==5 ||
    // saralShieldBean.getATPDB_Term()==6 ||
    // saralShieldBean.getATPDB_Term()==7 ||
    // saralShieldBean.getATPDB_Term()==8 ||
    // saralShieldBean.getATPDB_Term()==9)
    // {return getATPDBPremium_Yearly() *15/100;}
    // else if(saralShieldBean.getATPDB_Term()==10 ||
    // saralShieldBean.getATPDB_Term()==11 ||
    // saralShieldBean.getATPDB_Term()==12 ||
    // saralShieldBean.getATPDB_Term()==13 ||
    // saralShieldBean.getATPDB_Term()==14)
    // {return getATPDBPremium_Yearly()*20/100;}
    // else
    // {return getATPDBPremium_Yearly() *25/100;}
    // }
    // else
    // {return getATPDBPremium_Yearly()*2/100;}
    // }
    // else
    // {return 0;}
    // }

    public double getDisc_ATPDBPremium_SelFreq() {
        if (saralShieldBean.getStaffDisc()
                && saralShieldBean.getPlanName().equals("Level Term Assurance")) {
            if (!saralShieldBean.getPremFreq().equals("Single")) {
                return getATPDBPremium_Yearly() * 5 / 100;
            } else {
                return getATPDBPremium_Yearly() * 2 / 100;
            }
        } else {
            return 0;
        }
    }

    // ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    public double getPremiumYearly_Basic() {
        if (saralShieldBean.getBasicSA() < 1500000) {
            return getPR_Basic_FromDB() * saralShieldBean.getBasicSA()
                    / 1000;
        } else {
            if (saralShieldBean.getPlanName().equals("Level Term Assurance")) {
                if (saralShieldBean.getPremFreq().equals("Yearly")
                        || saralShieldBean.getPremFreq().equals(
                        "Half Yearly")
                        || saralShieldBean.getPremFreq().equals("Quarterly")
                        || saralShieldBean.getPremFreq().equals("Monthly")) {
                    return getPR_Basic_FromDB()
                            * saralShieldBean.getBasicSA()
                            / 1000
                            - ((saralShieldBean.getBasicSA() / 1000) * 0.3);
                } else {
                    return getPR_Basic_FromDB()
                            * saralShieldBean.getBasicSA()
                            / 1000
                            - ((saralShieldBean.getBasicSA() / 1000) * 0.95);
                }
            } else {
                return getPR_Basic_FromDB()
                        * saralShieldBean.getBasicSA()
                        / 1000
                        - ((saralShieldBean.getBasicSA() / 1000) * 0.95);
            }
        }
    }

    private String getRs_BasicCover(String whichPlan) {
        // For Level Term Assurance
        if (whichPlan.equals("Level Term Assurance")) {
            // For Regular Mode of Premium Frequency
            if (!saralShieldBean.getPremFreq().equals("Single")) {
                // System.out.println("In regular Premium Mode");
                if (saralShieldBean.getGender().equals("Male") || saralShieldBean.getGender().equals("Third Gender")) {
                    return "LTA_M_RP";
                } else {
                    return "LTA_F_RP";
                }
            }
            // For Single Mode of Premium Frequency
            else {
                // System.out.println("In single Premium Mode");
                if (saralShieldBean.getGender().equals("Male") || saralShieldBean.getGender().equals("Third Gender")) {
                    return "LTA_M_SP";
                } else {
                    return "LTA_F_SP";
                }
            }
        }
        // For Decreasing Term Assurance[Loan Protection]
        else if (whichPlan.equals("Decreasing Term Assurance[Loan Protection]")) {
            if (saralShieldBean.getLRI().equals("6%")) {
                return "DTALP_6";
            } else if (saralShieldBean.getLRI().equals("8%")) {
                return "DTALP_8";
            } else if (saralShieldBean.getLRI().equals("10%")) {
                return "DTALP_10";
            } else if (saralShieldBean.getLRI().equals("12%")) {
                return "DTALP_12";
            } else if (saralShieldBean.getLRI().equals("14%")) {
                return "DTALP_14";
            } else if (saralShieldBean.getLRI().equals("16%")) {
                return "DTALP_16";
            } else if (saralShieldBean.getLRI().equals("18%")) {
                return "DTALP_18";
            } else if (saralShieldBean.getLRI().equals("20%")) {
                return "DTALP_20";
            } else {
                return "";
            }
        }
        // For Decreasing Term Assurance[Family Income Protection]
        else {
            return "DTAFIP";
        }
    }

    // This method is used to select Basic PR from the Saral Shield Constant
    // Data
    private double getPR_Basic_FromDB() {
        double prDouble = 0;
        String pr = null, prStr;
        int arrCount = 0;
        String[] prStrArr;
        SaralShieldDB ac = new SaralShieldDB();
        prStr = ac.getBasic_PR_Arr(getRs_BasicCover(saralShieldBean
                .getPlanName()));
        prStrArr = commonForAllProd.split(prStr, ",");
        // Here min age is 18 & max age is 60/min term is 5 & max term is 30
        for (int i = 18; i < 61; i++) {
            for (int j = 5; j < 31; j++) {
                if (i == saralShieldBean.getAge()
                        && j == saralShieldBean.getBasicTerm()) {
                    pr = prStrArr[arrCount];
                    prDouble = Double.parseDouble(pr);
                    break;
                }
                arrCount++;
            }
        }
        return prDouble;
    }

    public double getDiscOnPremiumBasic_Yearly() {
        if (saralShieldBean.getStaffDisc()) {
            if (!saralShieldBean.getPremFreq().equals("Single")) {
                if (saralShieldBean.getBasicTerm() == 5
                        || saralShieldBean.getBasicTerm() == 6
                        || saralShieldBean.getBasicTerm() == 7
                        || saralShieldBean.getBasicTerm() == 8
                        || saralShieldBean.getBasicTerm() == 9) {
                    return (getPR_Basic_FromDB()
                            * saralShieldBean.getBasicSA() / 1000) * 15 / 100;
                } else if (saralShieldBean.getBasicTerm() == 10
                        || saralShieldBean.getBasicTerm() == 11
                        || saralShieldBean.getBasicTerm() == 12
                        || saralShieldBean.getBasicTerm() == 13
                        || saralShieldBean.getBasicTerm() == 14) {
                    return (getPR_Basic_FromDB()
                            * saralShieldBean.getBasicSA() / 1000) * 20 / 100;
                } else {
                    return (getPR_Basic_FromDB()
                            * saralShieldBean.getBasicSA() / 1000) * 25 / 100;
                }
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public double getDiscOnPremiumBasic() {
        if (saralShieldBean.getStaffDisc()) {
            if (!saralShieldBean.getPremFreq().equals("Single")) {
                if (saralShieldBean.getBasicTerm() == 5
                        || saralShieldBean.getBasicTerm() == 6
                        || saralShieldBean.getBasicTerm() == 7
                        || saralShieldBean.getBasicTerm() == 8
                        || saralShieldBean.getBasicTerm() == 9) {
                    return (getPR_Basic_FromDB()
                            * saralShieldBean.getBasicSA() / 1000) * 15 / 100;
                } else if (saralShieldBean.getBasicTerm() == 10
                        || saralShieldBean.getBasicTerm() == 11
                        || saralShieldBean.getBasicTerm() == 12
                        || saralShieldBean.getBasicTerm() == 13
                        || saralShieldBean.getBasicTerm() == 14) {
                    return (getPR_Basic_FromDB()
                            * saralShieldBean.getBasicSA() / 1000) * 20 / 100;
                } else {
                    return (getPR_Basic_FromDB()
                            * saralShieldBean.getBasicSA() / 1000) * 25 / 100;
                }
            } else {
                return (getPR_Basic_FromDB()
                        * saralShieldBean.getBasicSA() / 1000) * 2 / 100;
            }
        } else {
            return 0;
        }
    }

    // ADB Premium Yearly
    private double getPremiumYearly_ADB() {
        if (saralShieldBean.getADB_Status()
                && saralShieldBean.getPlanName().equals("Level Term Assurance")) {
            return getPR_Rider_FromDB("ADB")
                    * saralShieldBean.getADB_SA() / 100000;
        } else {
            return 0;
        }
    }

    // This method is used to select Basic PR for ADB.ATPDB Rider from the Smart
    // Shield Constant Data
    private double getPR_Rider_FromDB(String whichRider) {
        if (!saralShieldBean.getPremFreq().equals("Single")) {
            if (whichRider.equals("ADB")) {
                return 50;
            } else {
                return 40;
            }
        } else {
            int pr = 0;
            SaralShieldDB ac = new SaralShieldDB();
            int[] prArr = null;
            if (whichRider.equals("ADB")) {
                prArr = ac.getADB_PremiumRate_Arr();
                for (int i = 5; i < 31; i++) {
                    if (i == saralShieldBean.getADB_Term()) {
                        pr = prArr[i - 5];
                        break;
                    }
                }
            } else {
                prArr = ac.getATPDB_PremiumRate_Arr();
                for (int i = 5; i < 31; i++) {
                    if (i == saralShieldBean.getATPDB_Term()) {
                        pr = prArr[i - 5];
                        break;
                    }
                }
            }

            return pr;
        }
    }

    public double getDiscOnADB() {
        if (saralShieldBean.getStaffDisc()
                && saralShieldBean.getPlanName().equals("Level Term Assurance")) {
            if (saralShieldBean.getPremFreq().equals("Yearly")
                    || saralShieldBean.getPremFreq().equals("Half Yearly")
                    || saralShieldBean.getPremFreq().equals("Quarterly")
                    || saralShieldBean.getPremFreq().equals("Monthly")) {
                if (saralShieldBean.getADB_Term() == 5
                        || saralShieldBean.getADB_Term() == 6
                        || saralShieldBean.getADB_Term() == 7
                        || saralShieldBean.getADB_Term() == 8
                        || saralShieldBean.getADB_Term() == 9) {
                    return (getPR_Rider_FromDB("ADB")
                            * saralShieldBean.getADB_SA() / 100000) * 15 / 100;
                } else if (saralShieldBean.getADB_Term() == 10
                        || saralShieldBean.getADB_Term() == 11
                        || saralShieldBean.getADB_Term() == 12
                        || saralShieldBean.getADB_Term() == 13
                        || saralShieldBean.getADB_Term() == 14) {
                    return (getPR_Rider_FromDB("ADB")
                            * saralShieldBean.getADB_SA() / 100000) * 20 / 100;
                } else {
                    return (getPR_Rider_FromDB("ADB")
                            * saralShieldBean.getADB_SA() / 100000) * 25 / 100;
                }
            } else {
                return (getPR_Rider_FromDB("ADB")
                        * saralShieldBean.getADB_SA() / 100000) * 2 / 100;
            }
        } else {
            return 0;
        }
    }

    // ATPDB Rider Premium Without Staff Discount
    public double getPremWithoutStaffDisc_ATPDB() {
        if (saralShieldBean.getPremFreq().equals("Half Yearly")) {
            return getPremiumYearly_ATPDB() * 52 / 100;
        } else if (saralShieldBean.getPremFreq().equals("Quarterly")) {
            return getPremiumYearly_ATPDB() * 26.5 / 100;
        }
        // For Monthly Premium Frequency Mode
        else if (saralShieldBean.getPremFreq().equals("Monthly")) {
            return getPremiumYearly_ATPDB() * 8.9 / 100;
        } else {
            return getPremiumYearly_ATPDB();
        }
    }

    // ADB Rider Premium Without Staff Discount
    public double getPremWithoutStaffDisc_ADB() {
        if (saralShieldBean.getPremFreq().equals("Half Yearly")) {
            return getPremiumYearly_ADB() * 52 / 100;
        } else if (saralShieldBean.getPremFreq().equals("Quarterly")) {
            return getPremiumYearly_ADB() * 26.5 / 100;
        }
        // For Monthly Premium Frequency Mode
        else if (saralShieldBean.getPremFreq().equals("Monthly")) {
            return getPremiumYearly_ADB() * 8.9 / 100;
        } else {
            return getPremiumYearly_ADB();
        }
    }

    public double getTotalInstallPremWithDisc(double totalAnnualPremWithDisc) {
        if (saralShieldBean.getPremFreq().equals("Half Yearly")) {
            return totalAnnualPremWithDisc * 52 / 100;
        } else if (saralShieldBean.getPremFreq().equals("Quarterly")) {
            return totalAnnualPremWithDisc * 26.5 / 100;
        }
        // For Monthly Premium Frequency Mode
        else if (saralShieldBean.getPremFreq().equals("Monthly")) {
            return totalAnnualPremWithDisc * 8.9 / 100;
        } else {
            return totalAnnualPremWithDisc;
        }
    }

    // ATPDB Premium Yearly
    private double getPremiumYearly_ATPDB() {
        if (saralShieldBean.getATPDB_Status()
                && saralShieldBean.getPlanName().equals("Level Term Assurance")) {
            // System.out.println("  getPR_Rider_FromDB(ATPDB) "+
            // getPR_Rider_FromDB("ATPDB"));
            return getPR_Rider_FromDB("ATPDB")
                    * saralShieldBean.getATPDB_SA() / 100000;
        } else {
            return 0;
        }
    }

    public double getDiscOnATPDB() {
        if (saralShieldBean.getStaffDisc()
                && saralShieldBean.getPlanName().equals("Level Term Assurance")) {
            if (saralShieldBean.getPremFreq().equals("Yearly")
                    || saralShieldBean.getPremFreq().equals("Half Yearly")
                    || saralShieldBean.getPremFreq().equals("Quarterly")
                    || saralShieldBean.getPremFreq().equals("Monthly")) {
                if (saralShieldBean.getATPDB_Term() == 5
                        || saralShieldBean.getATPDB_Term() == 6
                        || saralShieldBean.getATPDB_Term() == 7
                        || saralShieldBean.getATPDB_Term() == 8
                        || saralShieldBean.getATPDB_Term() == 9) {
                    return (getPR_Rider_FromDB("ATPDB")
                            * saralShieldBean.getATPDB_SA() / 100000) * 15 / 100;
                } else if (saralShieldBean.getATPDB_Term() == 10
                        || saralShieldBean.getATPDB_Term() == 11
                        || saralShieldBean.getATPDB_Term() == 12
                        || saralShieldBean.getATPDB_Term() == 13
                        || saralShieldBean.getATPDB_Term() == 14) {
                    return (getPR_Rider_FromDB("ATPDB")
                            * saralShieldBean.getATPDB_SA() / 100000) * 20 / 100;
                } else {
                    return (getPR_Rider_FromDB("ATPDB")
                            * saralShieldBean.getATPDB_SA() / 100000) * 25 / 100;
                }
            } else {
                return (getPR_Rider_FromDB("ATPDB")
                        * saralShieldBean.getATPDB_SA() / 100000) * 2 / 100;
            }
        } else {
            return 0;
        }
    }

    /*** modified by Akshaya on 20-MAY-16 start **/

    // double getServiceTax()
    // {
    // if(saralShieldBean.isJkResident())
    // return prop.serviceTaxJKResident;
    // else
    // return prop.serviceTax;
    // }
    public double getServiceTax(double premiumWithoutST, boolean JKResident,
                                String type) {
        if (type.equals("basic")) {
            // System.out.println(premiumWithoutST+"       "+(premiumWithoutST*prop.serviceTax));
            if (JKResident)

                return Double.parseDouble(commonForAllProd
                        .getRoundUp(commonForAllProd
                                .getStringWithout_E(premiumWithoutST
                                        * prop.serviceTaxJKResident)));
            else {
                return Double.parseDouble(commonForAllProd
                        .getRoundUp(commonForAllProd
                                .getStringWithout_E(premiumWithoutST
                                        * prop.serviceTax)));
            }
        } else if (type.equals("SBC")) {
            if (JKResident)
                return 0;
            else {
                return Double.parseDouble(commonForAllProd
                        .getRoundUp(commonForAllProd
                                .getStringWithout_E(premiumWithoutST
                                        * prop.SBCServiceTax)));
            }
        } else if (type.equals("KERALA")) {
            return Double.parseDouble(commonForAllProd
                    .getRoundUp(commonForAllProd
                            .getStringWithout_E(premiumWithoutST
                                    * prop.KerlaServiceTax)));
        } else // KKC
        {
            if (JKResident)
                return 0;
            else {
                return Double.parseDouble(commonForAllProd
                        .getRoundUp(commonForAllProd
                                .getStringWithout_E(premiumWithoutST
                                        * prop.KKCServiceTax)));
            }
        }

    }

    /***** added by Akshaya on 04-AUG-15 start */
    public double getDiscPercentage() {
        if (saralShieldBean.getStaffDisc()) {
            if (!saralShieldBean.getPremFreq().equals("Single")) {
                return 0.05;
            } else {
                return 0.02;
            }
        } else {
            return 0;
        }
    }

    /*** modified by Akshaya on 20-MAY-16 end **/

    /*
     * Added By Akshaya on 04/09/2015
     */
    public double getPremWithStaffDisc_Basic(double disc_Basic_SelFreq,
                                             String premiumBasicWithoutDisc_ForSelFreq) {
        double premWithStaffDisc_Basic = 0, disc_selectFreq;
        if (saralShieldBean.getPremFreq().equals("Half Yearly")) {
            disc_selectFreq = disc_Basic_SelFreq * 52 / 100;
        } else if (saralShieldBean.getPremFreq().equals("Quarterly")) {
            disc_selectFreq = disc_Basic_SelFreq * 26.5 / 100;
        }
        // For Monthly Premium Frequency Mode
        else if (saralShieldBean.getPremFreq().equals("Monthly")) {
            disc_selectFreq = disc_Basic_SelFreq * 8.9 / 100;
        }
        // For Yearly Or Single Mode of Premium
        else {
            disc_selectFreq = disc_Basic_SelFreq;
        }

        premWithStaffDisc_Basic = Double
                .parseDouble(premiumBasicWithoutDisc_ForSelFreq)
                - disc_selectFreq;
        return premWithStaffDisc_Basic;
    }

    /***** added by Akshaya on 04-AUG-15 end */

    public double getMinesOccuInterest(double SumAssured_Basic) {
        return (SumAssured_Basic / 1000) * 2;
    }

    /*** Added by Priyanka Warekar - 31-08-2018 - start *******/
    public double getServiceTaxMines(double MinesOccuInterest) {
        return Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getRoundOffLevel2(commonForAllProd.getStringWithout_E(MinesOccuInterest * (prop.serviceTax + prop.SBCServiceTax)))));
    }
/*** Added by Priyanka Warekar - 31-08-2018 - end *******/

}
