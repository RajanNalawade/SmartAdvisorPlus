package sbilife.com.pointofsale_bancaagency.sampoorncancersuraksha;

import sbilife.com.pointofsale_bancaagency.common.CommonForAllProd;

class SampoornCancerSurakshaBusinesslogic {

    private SampoornCancerSurakshaBean sampoorncancersurakshabean;
    private SampoornCancerSurakshaProperties sampoorncancersurakshaProperties;
    private SampoornCancerSurakshaDB sampoornCancerSurakshaDB;
    private CommonForAllProd cfap;

    public SampoornCancerSurakshaBusinesslogic() {
        sampoorncancersurakshabean = new SampoornCancerSurakshaBean();
        sampoorncancersurakshaProperties = new SampoornCancerSurakshaProperties();
        sampoornCancerSurakshaDB = new SampoornCancerSurakshaDB();
        cfap = new CommonForAllProd();
    }

    public double getBasePremiumRate(String Gender, String PlanType, int age,
                                     int policyterm) {
        double BasePremiumRate = 0;
        if (Gender.equals("Male")) {
            if (PlanType.equals("Standard")) {

                double[] PWBarr = sampoornCancerSurakshaDB
                        .getMale_Rates_Standard();
                int position = 0;
                int premiumPerOneLac = 0;
                for (int i = 6; i <= 65; i++) {
                    for (int j = 5; j <= 30; j++) {
                        if ((age) == i && (policyterm) == j) {
                            // //System.out.println("getValFromPWBtable() -> "+
                            // PWBarr[position]);
                            BasePremiumRate = PWBarr[position];
                        }
                        position++;
                    }
                }

                double a = BasePremiumRate;
                // Log.d(""+a, "tab");

            } else if (PlanType.equals("Classic")) {

                double[] PWBarr = sampoornCancerSurakshaDB
                        .getMale_Rates_Classic();
                int position = 0;
                int premiumPerOneLac = 0;
                for (int i = 6; i <= 65; i++) {
                    for (int j = 5; j <= 30; j++) {
                        if ((age) == i && (policyterm) == j) {
                            // //System.out.println("getValFromPWBtable() -> "+
                            // PWBarr[position]);
                            BasePremiumRate = PWBarr[position];
                        }
                        position++;
                    }
                }

                double a = BasePremiumRate;

            } else {

                double[] PWBarr = sampoornCancerSurakshaDB
                        .getMale_Rates_Enhanced();
                int position = 0;
                int premiumPerOneLac = 0;
                for (int i = 6; i <= 65; i++) {
                    for (int j = 5; j <= 30; j++) {
                        if ((age) == i && (policyterm) == j) {
                            // //System.out.println("getValFromPWBtable() -> "+
                            // PWBarr[position]);
                            BasePremiumRate = PWBarr[position];
                        }
                        position++;
                    }
                }

                double a = BasePremiumRate;

            }
        } else if (Gender.equals("Female")) {
            if (PlanType.equals("Standard")) {

                double[] PWBarr = sampoornCancerSurakshaDB
                        .getFemale_Rates_Standard();
                int position = 0;
                int premiumPerOneLac = 0;
                for (int i = 6; i <= 65; i++) {
                    for (int j = 5; j <= 30; j++) {
                        if ((age) == i && (policyterm) == j) {
                            // //System.out.println("getValFromPWBtable() -> "+
                            // PWBarr[position]);
                            BasePremiumRate = PWBarr[position];
                        }
                        position++;
                    }
                }
                double a = BasePremiumRate;

            } else if (PlanType.equals("Classic")) {

                double[] PWBarr = sampoornCancerSurakshaDB
                        .getFemale_Rates_Classic();
                int position = 0;
                int premiumPerOneLac = 0;
                for (int i = 6; i <= 65; i++) {
                    for (int j = 5; j <= 30; j++) {
                        if ((age) == i && (policyterm) == j) {
                            // //System.out.println("getValFromPWBtable() -> "+
                            // PWBarr[position]);
                            BasePremiumRate = PWBarr[position];
                        }
                        position++;
                    }
                }

                double a = BasePremiumRate;

            } else {

                double[] PWBarr = sampoornCancerSurakshaDB
                        .getFemale_Rates_Enhanced();
                int position = 0;
                int premiumPerOneLac = 0;
                for (int i = 6; i <= 65; i++) {
                    for (int j = 5; j <= 30; j++) {
                        if ((age) == i && (policyterm) == j) {
                            // //System.out.println("getValFromPWBtable() -> "+
                            // PWBarr[position]);
                            BasePremiumRate = PWBarr[position];
                        }
                        position++;
                    }
                }

                double a = BasePremiumRate;

            }

        } else {

            if (PlanType.equals("Standard")) {

                double[] PWBarr = sampoornCancerSurakshaDB
                        .getTransgender_Rates_Standard();
                int position = 0;
                int premiumPerOneLac = 0;
                for (int i = 6; i <= 65; i++) {
                    for (int j = 5; j <= 30; j++) {
                        if ((age) == i && (policyterm) == j) {
                            // //System.out.println("getValFromPWBtable() -> "+
                            // PWBarr[position]);
                            BasePremiumRate = PWBarr[position];
                        }
                        position++;
                    }
                }
                double a = BasePremiumRate;

            } else if (PlanType.equals("Classic")) {

                double[] PWBarr = sampoornCancerSurakshaDB
                        .getTransgender_Rates_Classic();
                int position = 0;
                int premiumPerOneLac = 0;
                for (int i = 6; i <= 65; i++) {
                    for (int j = 5; j <= 30; j++) {
                        if ((age) == i && (policyterm) == j) {
                            // //System.out.println("getValFromPWBtable() -> "+
                            // PWBarr[position]);
                            BasePremiumRate = PWBarr[position];
                        }
                        position++;
                    }
                }

                double a = BasePremiumRate;

            } else {

                double[] PWBarr = sampoornCancerSurakshaDB
                        .getTransgender_Rates_Enhanced();
                int position = 0;
                int premiumPerOneLac = 0;
                for (int i = 6; i <= 65; i++) {
                    for (int j = 5; j <= 30; j++) {
                        if ((age) == i && (policyterm) == j) {
                            // //System.out.println("getValFromPWBtable() -> "+
                            // PWBarr[position]);
                            BasePremiumRate = PWBarr[position];
                        }
                        position++;
                    }
                }

                double a = BasePremiumRate;

            }

        }
        // {
        // double MaleRate=0,FemaleRate = 0 ;
        // if(PlanType.equals("Standard"))
        // {
        //
        // double [] PWBarr=sampoornCancerSurakshaDB.getMale_Rates_Standard();
        // int position=0;
        // int premiumPerOneLac=0;
        // for (int i=6; i<=65; i++)
        // {
        // for (int j=5; j<=30; j++)
        // {
        // if((age)==i && (policyterm)==j)
        // {
        // ////System.out.println("getValFromPWBtable() -> "+ PWBarr[position]);
        // MaleRate= PWBarr[position];
        // }
        // position++;
        // }
        // }
        //
        //
        // double a = BasePremiumRate;
        // // Log.d(""+a, "tab");
        //
        //
        // }
        // else if(PlanType.equals("Classic"))
        // {
        //
        // double [] PWBarr=sampoornCancerSurakshaDB.getMale_Rates_Classic();
        // int position=0;
        // int premiumPerOneLac=0;
        // for (int i=6; i<=65; i++)
        // {
        // for (int j=5; j<=30; j++)
        // {
        // if((age)==i && (policyterm)==j)
        // {
        // ////System.out.println("getValFromPWBtable() -> "+ PWBarr[position]);
        // MaleRate= PWBarr[position];
        // }
        // position++;
        // }
        // }
        // }
        // else
        // {
        //
        // double [] PWBarr=sampoornCancerSurakshaDB.getMale_Rates_Enhanced();
        // int position=0;
        // int premiumPerOneLac=0;
        // for (int i=6; i<=65; i++)
        // {
        // for (int j=5; j<=30; j++)
        // {
        // if((age)==i && (policyterm)==j)
        // {
        // ////System.out.println("getValFromPWBtable() -> "+ PWBarr[position]);
        // MaleRate= PWBarr[position];
        // }
        // position++;
        // }
        // }
        //
        //
        //
        // double a = BasePremiumRate;
        //
        //
        // }
        //
        //
        //
        //
        // if(PlanType.equals("Standard"))
        // {
        //
        // double [] PWBarr=sampoornCancerSurakshaDB.getFemale_Rates_Standard();
        // int position=0;
        // int premiumPerOneLac=0;
        // for (int i=6; i<=65; i++)
        // {
        // for (int j=5; j<=30; j++)
        // {
        // if((age)==i && (policyterm)==j)
        // {
        // ////System.out.println("getValFromPWBtable() -> "+ PWBarr[position]);
        // FemaleRate= PWBarr[position];
        // }
        // position++;
        // }
        // }
        // double a = BasePremiumRate;
        //
        // }
        // else if(PlanType.equals("Classic"))
        // {
        //
        // double [] PWBarr=sampoornCancerSurakshaDB.getFemale_Rates_Classic();
        // int position=0;
        // int premiumPerOneLac=0;
        // for (int i=6; i<=65; i++)
        // {
        // for (int j=5; j<=30; j++)
        // {
        // if((age)==i && (policyterm)==j)
        // {
        // ////System.out.println("getValFromPWBtable() -> "+ PWBarr[position]);
        // FemaleRate= PWBarr[position];
        // }
        // position++;
        // }
        // }
        //
        // double a = BasePremiumRate;
        //
        //
        // }
        // else
        // {
        //
        // double [] PWBarr=sampoornCancerSurakshaDB.getFemale_Rates_Enhanced();
        // int position=0;
        // int premiumPerOneLac=0;
        // for (int i=6; i<=65; i++)
        // {
        // for (int j=5; j<=30; j++)
        // {
        // if((age)==i && (policyterm)==j)
        // {
        // ////System.out.println("getValFromPWBtable() -> "+ PWBarr[position]);
        // FemaleRate= PWBarr[position];
        // }
        // position++;
        // }
        // }
        //
        //
        //
        //
        //
        //
        // }
        //
        //
        // BasePremiumRate = Math.max(MaleRate, FemaleRate);
        //
        //
        // }

        return BasePremiumRate;
    }

    public double getStaffRebate(boolean IsStaff) {
        double StaffRebate = 0;
        if (IsStaff) {
            StaffRebate = 0.05;
        } else {
            StaffRebate = 0.00;
        }
        return StaffRebate;

    }

    public double getSARebate(double sumassured) {
        double SARebate = 0;

        if (sumassured >= 1000000 && sumassured <= 2400000) {
            SARebate = 0;
        } else {
            if (sumassured >= 2500000 && sumassured <= 3900000) {
                SARebate = 0.12;
            } else {
                SARebate = 0.2;
            }
        }

        return SARebate;

    }

    public double getBasePremiumRateWithoutSARebate(String Gender,
                                                    String PlanType, int age, int policyterm, double sumassured) {
        double BasePremiumRateWithoutSARebate = 0;

        BasePremiumRateWithoutSARebate = getBasePremiumRate(Gender, PlanType,
                age, policyterm) - getSARebate(sumassured);
        return BasePremiumRateWithoutSARebate;

    }

    public double getStaffDiscountRate(boolean IsStaff, String Gender,
                                       String PlanType, int age, int policyterm, double sumassured) {
        double StaffDiscountRate = 0;

        if (IsStaff) {
            StaffDiscountRate = getStaffRebate(IsStaff)
                    * getBasePremiumRateWithoutSARebate(Gender, PlanType, age,
                    policyterm, sumassured);
        } else {
            StaffDiscountRate = 0;
        }
        return StaffDiscountRate;

    }

    public double getFinalBasePremiumRate(boolean IsStaff, String Gender,
                                          String PlanType, int age, int policyterm, double sumassured) {
        double FinalBasePremiumRate = 0;
        FinalBasePremiumRate = getBasePremiumRateWithoutSARebate(Gender,
                PlanType, age, policyterm, sumassured)
                - getStaffDiscountRate(IsStaff, Gender, PlanType, age,
                policyterm, sumassured);

        return FinalBasePremiumRate;

    }

    public double getAnnualPremium(boolean IsStaff, String Gender,
                                   String PlanType, int age, int policyterm, double sumassured) {
        double AnnualPremium = 0;
        AnnualPremium = getFinalBasePremiumRate(IsStaff, Gender, PlanType, age,
                policyterm, sumassured) * (sumassured / 1000);

        return AnnualPremium;

    }

    public double getPremium(String premfreq, boolean IsStaff, String Gender,
                             String PlanType, int age, int policyterm, double sumassured) {
        double Premium = 0;
        if (premfreq.equals("Yearly")) {
            Premium = getAnnualPremium(IsStaff, Gender, PlanType, age,
                    policyterm, sumassured) * 1;
        } else if (premfreq.equals("Half-Yearly")) {
            Premium = getAnnualPremium(IsStaff, Gender, PlanType, age,
                    policyterm, sumassured) * 0.51;
        } else if (premfreq.equals("Quarterly")) {
            Premium = getAnnualPremium(IsStaff, Gender, PlanType, age,
                    policyterm, sumassured) * 0.26;
        } else {
            Premium = getAnnualPremium(IsStaff, Gender, PlanType, age,
                    policyterm, sumassured) * 0.085;
        }
        return Premium;

    }

    public double getPremiumOnline(String premfreq, boolean IsStaff,
                                   String Gender, String PlanType, int age, int policyterm,
                                   double sumassured) {
        double Premium = 0;
        if (premfreq.equals("Yearly")) {
            double val = getAnnualPremium(IsStaff, Gender, PlanType, age,
                    policyterm, sumassured) * 1;
            Premium = val - (0.05 * val);
        } else if (premfreq.equals("Half-Yearly")) {
            double val = getAnnualPremium(IsStaff, Gender, PlanType, age,
                    policyterm, sumassured) * 0.51;
            Premium = val - (0.05 * val);
        } else if (premfreq.equals("Quarterly")) {
            double val = getAnnualPremium(IsStaff, Gender, PlanType, age,
                    policyterm, sumassured) * 0.26;
            Premium = val - (0.05 * val);
        } else {
            double val = getAnnualPremium(IsStaff, Gender, PlanType, age,
                    policyterm, sumassured) * 0.085;
            Premium = val - (0.05 * val);
        }
        return Premium;

    }

    // public double getLoadingFrequencyPremium(String premfreq)
    // {
    // double LoadingFrequencyPremium = 0 ;
    //
    //
    // if(premfreq.equals("Yearly") || premfreq.equals("Single"))
    // {
    // LoadingFrequencyPremium =1 ;
    // }
    // else
    // {
    // if (premfreq.equals("Half-Yearly"))
    // {
    // LoadingFrequencyPremium =0.51 ;
    // }
    // else
    // {
    // if (premfreq.equals("Quarterly"))
    // {
    // LoadingFrequencyPremium =0.26 ;
    // }
    // else
    // {
    // LoadingFrequencyPremium =0.085 ;
    // }
    // }
    // }
    //
    // return LoadingFrequencyPremium;
    // }

    // public double getPremiumbeforeST(String Gender,String PlanType,int
    // age,int policyterm,double sumassured,boolean IsStaff,String premfreq)
    // {
    // double PremiumbeforeST = 0 ;
    // // PremiumbeforeST = (((getTabularPremiumRate(Gender, PlanType, age,
    // policyterm) - getSARebate(sumassured)) * sumassured)/1000) * (1-
    // getStaffRebate(IsStaff)) * getLoadingFrequencyPremium(premfreq)/1;
    // PremiumbeforeST = ((getBasePremiumRate(Gender, PlanType, age, policyterm)
    // * (1 - getStaffRebate(IsStaff)) - getSARebate(sumassured) +
    // (sampoorncancersurakshaProperties.EMR+sampoorncancersurakshaProperties.NSAP))
    // * sumassured /1000 *getLoadingFrequencyPremium(premfreq)/1);
    //
    // return PremiumbeforeST ;
    // }

    // public double getPremiumMultiplicationFactor(String premfreq)
    // {
    // double PremiumMultiplicationFactor = 0 ;
    // if(premfreq.equals("Yearly") || premfreq.equals("Single"))
    // {
    // PremiumMultiplicationFactor =1 ;
    // }
    // else
    // {
    // if (premfreq.equals("Half-Yearly"))
    // {
    // PremiumMultiplicationFactor =2 ;
    // }
    // else
    // {
    // if (premfreq.equals("Quarterly"))
    // {
    // PremiumMultiplicationFactor =4 ;
    // }
    // else
    // {
    // PremiumMultiplicationFactor =12 ;
    // }
    // }
    // }
    // return PremiumMultiplicationFactor;
    //
    //
    // }

    // public double getTotalBasePremiumWithoutServtx(String Gender,String
    // PlanType,int age,int policyterm,double sumassured,boolean IsStaff,String
    // premfreq)
    // {
    // double TotalBasePremiumWithoutServtx = 0 ;
    // return TotalBasePremiumWithoutServtx =
    // Double.parseDouble(cfap.getRoundOffLevel2(""+getPremiumbeforeST(Gender,
    // PlanType, age, policyterm, sumassured, IsStaff, premfreq))) *
    // getPremiumMultiplicationFactor(premfreq);
    // }

    // public double getMonthlyPremium_1stYear(String Gender,String PlanType,int
    // age,int policyterm,double sumassured,boolean IsStaff,String premfreq)
    // {
    // double MonthlyPremium_1stYear = 0 ;
    // return MonthlyPremium_1stYear =
    // Double.parseDouble(cfap.getRoundUp(getPremiumbeforeST(Gender, PlanType,
    // age, policyterm, sumassured, IsStaff, premfreq)));
    // }

    // public double getMonthlyPremiumwithservtx_1stYear(boolean jk,String
    // Gender,String PlanType,int age,int policyterm,double sumassured,boolean
    // IsStaff,String premfreq)
    // {
    // double MonthlyPremiumwithservtx_1stYear = 0 ;
    // if(jk)
    // {
    // MonthlyPremiumwithservtx_1stYear = cfap.getRoundUp1((0.0126 + 1) *
    // getMonthlyPremium_1stYear(Gender, PlanType, age, policyterm, sumassured,
    // IsStaff, premfreq)) ;
    // }
    // else
    // {
    // MonthlyPremiumwithservtx_1stYear = cfap.getRoundUp1((0.0363 + 1) *
    // getMonthlyPremium_1stYear(Gender, PlanType, age, policyterm, sumassured,
    // IsStaff, premfreq)) ;
    // }
    //
    //
    // return MonthlyPremiumwithservtx_1stYear;
    //
    // }
    //
    //
    // public double getServiceTax_1stYear(boolean jk,String Gender,String
    // PlanType,int age,int policyterm,double sumassured,boolean IsStaff,String
    // premfreq)
    // {
    // double ServiceTax_1stYear = 0 ;
    // ServiceTax_1stYear = getMonthlyPremiumwithservtx_1stYear(jk, Gender,
    // PlanType, age, policyterm, sumassured, IsStaff, premfreq) -
    // getMonthlyPremium_1stYear(Gender, PlanType, age, policyterm, sumassured,
    // IsStaff, premfreq) ;
    //
    // return ServiceTax_1stYear;
    //
    //
    // }

    private double getFinalBasePremiumRateWithoutDiscount(boolean IsStaff,
                                                          String Gender, String PlanType, int age, int policyterm,
                                                          double sumassured) {
        double FinalBasePremiumRate = 0;
        FinalBasePremiumRate = getBasePremiumRateWithoutSARebate(Gender,
                PlanType, age, policyterm, sumassured) - 0;

        return FinalBasePremiumRate;

    }

    private double getAnnualPremiumWithoutDiscWithoutFreqLoading(
            boolean IsStaff, String Gender, String PlanType, int age,
            int policyterm, double sumassured) {
        double AnnualPremium = 0;
        AnnualPremium = getFinalBasePremiumRateWithoutDiscount(IsStaff, Gender,
                PlanType, age, policyterm, sumassured) * (sumassured / 1000);

        return AnnualPremium;

    }

    public double getPremiumWithoutSTWithoutDisc(String premfreq,
                                                 boolean IsStaff, String Gender, String PlanType, int age,
                                                 int policyterm, double sumassured) {
        double Premium = 0;
        if (premfreq.equals("Yearly")) {
            Premium = getAnnualPremiumWithoutDiscWithoutFreqLoading(IsStaff,
                    Gender, PlanType, age, policyterm, sumassured) * 1;
        } else if (premfreq.equals("Half-Yearly")) {
            Premium = getAnnualPremiumWithoutDiscWithoutFreqLoading(IsStaff,
                    Gender, PlanType, age, policyterm, sumassured) * 0.51;
        } else if (premfreq.equals("Quarterly")) {
            Premium = getAnnualPremiumWithoutDiscWithoutFreqLoading(IsStaff,
                    Gender, PlanType, age, policyterm, sumassured) * 0.26;
        } else {
            Premium = getAnnualPremiumWithoutDiscWithoutFreqLoading(IsStaff,
                    Gender, PlanType, age, policyterm, sumassured) * 0.085;
        }
        return Premium;

    }

    private double getSBC(boolean jk, String Gender, String PlanType, int age,
                          int policyterm, double sumassured, boolean IsStaff, String premfreq) {
        double SBC = 0;
        if (jk) {
            SBC = 0;
        } else {
            SBC = sampoorncancersurakshabean.getServiceTax()
                    * getPremium(premfreq, IsStaff, Gender, PlanType, age,
                    policyterm, sumassured);
        }
        return SBC;
    }

    private double getKKC(boolean jk, String Gender, String PlanType, int age,
                          int policyterm, double sumassured, boolean IsStaff, String premfreq) {
        double KKC = 0;
        // if(jk)
        // {
        // KKC = 0 ;
        // }
        // else
        // {
        // KKC = 0.005 * getPremium(premfreq, IsStaff, Gender, PlanType, age,
        // policyterm, sumassured);
        // }
        return KKC;

    }

    public double getTotalTaxes(boolean jk, String Gender, String PlanType,
                                int age, int policyterm, double sumassured, boolean IsStaff,
                                String premfreq) {
        double TotalTaxes = 0;

        return TotalTaxes = getBasicServiceTax(jk, Gender, PlanType, age,
                policyterm, sumassured, IsStaff, premfreq)
                + getSBC(jk, Gender, PlanType, age, policyterm, sumassured,
                IsStaff, premfreq)
                + getKKC(jk, Gender, PlanType, age, policyterm, sumassured,
                IsStaff, premfreq);

    }

    private double getBasicServiceTax(boolean jk, String Gender,
                                      String PlanType, int age, int policyterm, double sumassured,
                                      boolean IsStaff, String premfreq) {
        double BasicServiceTax = 0;
        if (jk) {
            BasicServiceTax = sampoorncancersurakshaProperties.jkservicetax
                    * getPremium(premfreq, IsStaff, Gender, PlanType, age,
                    policyterm, sumassured);
        } else {
            BasicServiceTax = sampoorncancersurakshaProperties.SGST
                    * getPremium(premfreq, IsStaff, Gender, PlanType, age,
                    policyterm, sumassured);
        }
        return BasicServiceTax;

    }

    // public double getTotalTaxes(boolean jk,String Gender,String PlanType,int
    // age,int policyterm,double sumassured,boolean IsStaff,String premfreq)
    // {
    // double TotalTaxes = 0 ;
    //
    // return TotalTaxes = getBasicServiceTax(jk, Gender, PlanType, age,
    // policyterm, sumassured, IsStaff, premfreq) + getSBC(jk, Gender, PlanType,
    // age, policyterm, sumassured, IsStaff, premfreq) +
    // getKKC(jk, Gender, PlanType, age, policyterm, sumassured, IsStaff,
    // premfreq);
    //
    // }

    // public double getPremiumWithTax(boolean jk,String Gender,String
    // PlanType,int age,int policyterm,double sumassured,boolean IsStaff,String
    // premfreq)
    // {
    // double PremiumWithTax = 0 ;
    // return PremiumWithTax= getMonthlyPremium_1stYear(Gender, PlanType, age,
    // policyterm, sumassured, IsStaff, premfreq) + getTotalTaxes(jk, Gender,
    // PlanType, age, policyterm, sumassured, IsStaff, premfreq);
    // }

    public double getBasicServiceTax(boolean jk, String PremiumbeforeSt) {
        double BasicServiceTax = 0;
        if (jk) {
            BasicServiceTax = sampoorncancersurakshaProperties.jkservicetax
                    * Double.parseDouble(PremiumbeforeSt);
        } else {
            BasicServiceTax = sampoorncancersurakshaProperties.SGST
                    * Double.parseDouble(PremiumbeforeSt);
        }
        return BasicServiceTax;

    }

	/*public double getSBC(boolean jk, String PremiumbeforeSt) {
        double SBC = 0;
        if (jk) {
            SBC = 0;
        } else {
            SBC = sampoorncancersurakshabean.getServiceTax()
                    * Double.parseDouble(PremiumbeforeSt);
        }
        return SBC;
	}*/

    public double getSBC(boolean jk, String PremiumbeforeSt, double CGST) {
        double SBC = 0;
        if (jk) {
            SBC = 0;
        } else {
            SBC = CGST * Double.parseDouble(PremiumbeforeSt);
        }
        return SBC;
    }

    public double getKKC(boolean jk, String PremiumbeforeSt) {
        double KKC = 0;
        // if(jk)
        // {
        // KKC = 0 ;
        // }
        // else
        // {
        // KKC = 0.005 * getPremium(premfreq, IsStaff, Gender, PlanType, age,
        // policyterm, sumassured);
        // }
        return KKC;

    }

    /*Change by vrushali on 01-08-2017*/
    private double getBasePremiumRateWithSARebateZero(String Gender, String PlanType, int age, int policyterm, double sumassured) {
        double BasePremiumRateWithoutSARebate = 0;

        BasePremiumRateWithoutSARebate = getBasePremiumRate(Gender, PlanType, age, policyterm) - 0;
        return BasePremiumRateWithoutSARebate;

    }

    private double getFinalBasePremiumRateWithoutDiscountWithSAZero(boolean IsStaff, String Gender, String PlanType, int age, int policyterm, double sumassured) {
        double FinalBasePremiumRate = 0;
        FinalBasePremiumRate = getBasePremiumRateWithSARebateZero(Gender, PlanType, age, policyterm, sumassured) - 0;

        return FinalBasePremiumRate;


    }

    public double getAnnualPremiumWithoutDiscWithoutFreqLoadingWithSAZero(boolean IsStaff, String Gender, String PlanType, int age, int policyterm, double sumassured) {
        double AnnualPremium = 0;
        AnnualPremium = getFinalBasePremiumRateWithoutDiscountWithSAZero(IsStaff, Gender, PlanType, age, policyterm, sumassured) * (sumassured / 1000);


        return AnnualPremium;

    }
}
