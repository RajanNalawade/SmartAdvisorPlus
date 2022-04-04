package sbilife.com.pointofsale_bancaagency.products.eshieldNext;


import sbilife.com.pointofsale_bancaagency.common.CommonForAllProd;

public class eshieldNextBusinessLogic {
    private eshieldNextBean eshieldNextBean = null;
    sbilife.com.pointofsale_bancaagency.products.eshieldNext.eshieldNextDB eshieldNextDB;
    eshieldNextDB_Rates eshieldNextDBRates;
    eshieldNextDB_ICRates eshieldNextDBICRates;
    sbilife.com.pointofsale_bancaagency.products.eshieldNext.eshieldNextDB_WLRates eshieldNextDB_WLRates;
    sbilife.com.pointofsale_bancaagency.products.eshieldNext.eshieldNextProperties eshieldNextProperties;
    CommonForAllProd cfap;
    double basicpremiumrate = 0, basicBetterhalfpremiumrate = 0, basicpremium = 0, basicBetterhalfpremium = 0, adbpremium = 0, atpdbpremium = 0, totalpremiumwithoutST = 0, ServiceTax = 0, PremiumwithST = 0, increasebasicpremium = 0, levelbasicpremium = 0, basicChannelDis = 0, ADBChannelDis, ATPDBChannelDis = 0, BetterHalfChannelDis = 0, staffDisc = 0, freqLoading = 0, ADBfreqLoading = 0, ATPDBfreqLoading = 0, BetterhalffreqLoading = 0, freqLoadingStaff = 0, ATPDBfreqLoadingStaff = 0, BHfreqLoadingStaff = 0, ADBfreqLoadingStaff = 0, BetterHalfstaffDisc = 0, ADBstaffDisc = 0, ATPDBstaffDisc = 0;
    int sumassuredslab = 0;
    int policyTermMax = 0;
    double BasicServiceTax = 0;
    double SBC = 0, KKC = 0;
    String ageGroup;

    public eshieldNextBusinessLogic(eshieldNextBean eshieldNextBean) {
        this.eshieldNextBean = eshieldNextBean;
        eshieldNextDB = new eshieldNextDB();
        eshieldNextDBRates = new eshieldNextDB_Rates();
        eshieldNextDBICRates = new eshieldNextDB_ICRates();
        eshieldNextDB_WLRates = new eshieldNextDB_WLRates();
        eshieldNextProperties = new eshieldNextProperties();
        cfap = new CommonForAllProd();
    }

    public int getsumassuredslab(double sumassured, String ppopt) {
        if (sumassured >= 5000000 && sumassured <= 9900000) {
            sumassuredslab = 0;
        }
        if (sumassured >= 10000000) {
            sumassuredslab = 1;
        }

        if (ppopt.equalsIgnoreCase("Regular") || ppopt.equalsIgnoreCase("Single")) {
            if (sumassured >= 10000000 && sumassured <= 49900000) {
                sumassuredslab = 2;
            }
            if (sumassured >= 50000000) {
                sumassuredslab = 3;
            }
        }
//		 else
//		 {
//			 sumassuredslab=0;
//		 }
        return sumassuredslab;

    }

    public double getbasicpremiumrate(String gender, String planoption, String premiumPayoption, String Smokertype, double sumassured, int age, int policyterm, int PPT, String WholelifePopt) {
        if (planoption.equalsIgnoreCase("Level Cover") || planoption.equalsIgnoreCase("Level Cover with Future Proofing Benefits") || planoption.equalsIgnoreCase("Level Cover with Future Proofing Benefits")) {
            if (premiumPayoption.equalsIgnoreCase("Regular") && (gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Third Gender") || gender.equalsIgnoreCase("Third Gender")) && Smokertype.equalsIgnoreCase("Non Smoker") && getsumassuredslab(sumassured, premiumPayoption) == 0 && WholelifePopt.equalsIgnoreCase("Other than Whole life")) {
                double[] PWBarr = eshieldNextDB.getLC_Male_Nonsmoker_slab2();
                int position = 0;
                for (int i = 18; i <= 60; i++) {
//            for (int j=5; j<=30; j++)
                    for (int j = 5; j <= 67; j++) {
                        if ((age) == i && (policyterm) == j) {
                            basicpremiumrate = PWBarr[position];
                            break;
                        }
                        position++;
                    }
                }
            } else if (premiumPayoption.equalsIgnoreCase("Regular") && (gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Third Gender") || gender.equalsIgnoreCase("Third Gender")) && Smokertype.equalsIgnoreCase("Smoker") && getsumassuredslab(sumassured, premiumPayoption) == 0 && WholelifePopt.equalsIgnoreCase("Other than Whole life")) {
                double[] PWBarr = eshieldNextDB.getLC_Male_smoker_slab2();
                int position = 0;
                for (int i = 18; i <= 60; i++) {
//            for (int j=5; j<=30; j++)
                    for (int j = 5; j <= 67; j++) {
                        if ((age) == i && (policyterm) == j) {
                            basicpremiumrate = PWBarr[position];
                            break;
                        }
                        position++;
                    }
                }
            } else if (premiumPayoption.equalsIgnoreCase("Regular") && gender.equalsIgnoreCase("Female") && Smokertype.equalsIgnoreCase("Smoker") && getsumassuredslab(sumassured, premiumPayoption) == 0 && WholelifePopt.equalsIgnoreCase("Other than Whole life")) {
                double[] PWBarr = eshieldNextDB.getLC_Female_smoker_slab2();
                int position = 0;
                for (int i = 18; i <= 60; i++) {
//            for (int j=5; j<=30; j++)
                    for (int j = 5; j <= 67; j++) {
                        if ((age) == i && (policyterm) == j) {
                            basicpremiumrate = PWBarr[position];
                            break;
                        }
                        position++;
                    }
                }
                System.out.println("basicpremiumrate " + basicpremiumrate);

            } else if (premiumPayoption.equalsIgnoreCase("Regular") && gender.equalsIgnoreCase("Female") && Smokertype.equalsIgnoreCase("Non Smoker") && getsumassuredslab(sumassured, premiumPayoption) == 0 && WholelifePopt.equalsIgnoreCase("Other than Whole life")) {
                double[] PWBarr = eshieldNextDB.getLC_Female_nonsmoker_slab2();
                if ((age) == 18 || (age) == 19 || (age) == 20 || (age) == 21) {
                    age = 18;
                } else {
                    age = (age - 3);
                }
                int position = 0;
                for (int i = 18; i <= 60; i++) {
//            for (int j=5; j<=30; j++)
                    for (int j = 5; j <= 67; j++) {
                        if ((age) == i && (policyterm) == j) {
                            basicpremiumrate = PWBarr[position];
                            break;
                        }
                        position++;
                    }
                }


            } else if (premiumPayoption.equalsIgnoreCase("Single") && (gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Third Gender") || gender.equalsIgnoreCase("Third Gender")) && Smokertype.equalsIgnoreCase("Non Smoker") && getsumassuredslab(sumassured, premiumPayoption) == 0 && WholelifePopt.equalsIgnoreCase("Other than Whole life")) {
                double[] PWBarr = eshieldNextDB.getLC_Male_Nonsmoker_slab0();
                int position = 0;
                for (int i = 18; i <= 65; i++) {
//            for (int j=5; j<=30; j++)
                    for (int j = 5; j <= 67; j++) {
                        if ((age) == i && (policyterm) == j) {
                            basicpremiumrate = PWBarr[position];
                            break;
                        }
                        position++;
                    }
                }


            } else if (premiumPayoption.equalsIgnoreCase("Single") && (gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Third Gender") || gender.equalsIgnoreCase("Third Gender")) && Smokertype.equalsIgnoreCase("Smoker") && getsumassuredslab(sumassured, premiumPayoption) == 0 && WholelifePopt.equalsIgnoreCase("Other than Whole life")) {
                double[] PWBarr = eshieldNextDB.getLC_Male_smoker_slab0();
                int position = 0;
                for (int i = 18; i <= 65; i++) {
//            for (int j=5; j<=30; j++)
                    for (int j = 5; j <= 67; j++) {
                        if ((age) == i && (policyterm) == j) {
                            basicpremiumrate = PWBarr[position];
                            break;
                        }
                        position++;
                    }
                }


            } else if (premiumPayoption.equalsIgnoreCase("Single") && (gender.equalsIgnoreCase("Female")) && Smokertype.equalsIgnoreCase("Smoker") && getsumassuredslab(sumassured, premiumPayoption) == 0 && WholelifePopt.equalsIgnoreCase("Other than Whole life")) {
                double[] PWBarr = eshieldNextDB.getLC_Female_smoker_slab0();
                int position = 0;
                for (int i = 18; i <= 65; i++) {
//            for (int j=5; j<=30; j++)
                    for (int j = 5; j <= 67; j++) {
                        if ((age) == i && (policyterm) == j) {
                            basicpremiumrate = PWBarr[position];
                            break;
                        }
                        position++;
                    }
                }


            } else if (premiumPayoption.equalsIgnoreCase("Single") && (gender.equalsIgnoreCase("Female")) && Smokertype.equalsIgnoreCase("Non Smoker") && getsumassuredslab(sumassured, premiumPayoption) == 0 && WholelifePopt.equalsIgnoreCase("Other than Whole life")) {
                double[] PWBarr = eshieldNextDB.getLC_Female_nonsmoker_slab0();
                int position = 0;
                if ((age) == 18 || (age) == 19 || (age) == 20 || (age) == 21) {
                    age = 18;
                } else {
                    age = (age - 3);
                }
                for (int i = 18; i <= 65; i++) {
                    for (int j = 5; j <= 67; j++)
//        		for (int j=5; j<=30; j++)
                    {
                        if ((age) == i && (policyterm) == j) {
                            basicpremiumrate = PWBarr[position];
                            break;
                        }
                        position++;
                    }
                }


            } else if (premiumPayoption.equalsIgnoreCase("Single") && (gender.equalsIgnoreCase("Female")) && Smokertype.equalsIgnoreCase("Non Smoker") && getsumassuredslab(sumassured, premiumPayoption) == 0 && WholelifePopt.equalsIgnoreCase("Other than Whole life")) {
                double[] PWBarr = eshieldNextDB.getLC_Male_Nonsmoker_slab0();
                int position = 0;
                for (int i = 18; i <= 65; i++) {
                    for (int j = 5; j <= 67; j++)
//        		for (int j=5; j<=30; j++)
                    {
                        if ((age) == i && (policyterm) == j) {
                            basicpremiumrate = PWBarr[position];
                            break;
                        }
                        position++;
                    }
                }


            } else if (premiumPayoption.equalsIgnoreCase("LPPT") && ((gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Third Gender") || gender.equalsIgnoreCase("Third Gender"))) && Smokertype.equalsIgnoreCase("Non Smoker") && getsumassuredslab(sumassured, premiumPayoption) == 0 && WholelifePopt.equalsIgnoreCase("Other than Whole life")) {
                double[] PWBarr;
                int position = 0;
                if (PPT == 5) {
                    PWBarr = eshieldNextDBRates.getLC_Male_Nonsmoker_slab1_LPPT5();
                    for (int i = 18; i <= 65; i++) {
                        for (int j = 10; j <= 67; j++)
//             		for (int j=5; j<=35; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else if (PPT == 7) {
                    PWBarr = eshieldNextDBRates.getLC_Male_Nonsmoker_slab1_LPPT7();

                    for (int i = 18; i <= 65; i++) {
                        for (int j = 10; j <= 67; j++)
//        		for (int j=5; j<=35; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }


                } else if (PPT == 10) {
                    PWBarr = eshieldNextDBRates.getLC_Male_Nonsmoker_slab1_LPPT10();
                    for (int i = 18; i <= 65; i++) {
                        for (int j = 15; j <= 67; j++)
//            		for (int j=5; j<=35; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else if (PPT == 15) {
                    PWBarr = eshieldNextDBRates.getLC_Male_Nonsmoker_slab1_LPPT15();
                    for (int i = 18; i <= 65; i++) {
                        for (int j = 20; j <= 67; j++)
//            		for (int j=5; j<=35; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else if (PPT == 20) {
                    PWBarr = eshieldNextDBRates.getLC_Male_Nonsmoker_slab1_LPPT20();
                    for (int i = 18; i <= 60; i++) {
                        for (int j = 25; j <= 67; j++)
//             		for (int j=5; j<=35; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else if (PPT == 25) {
                    PWBarr = eshieldNextDBRates.getLC_Male_Nonsmoker_slab1_LPPT25(); //this is for 25
                    for (int i = 18; i <= 55; i++) {
                        for (int j = 30; j <= 67; j++)
//             		for (int j=5; j<=35; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                }
                //added for pplicyterm  less 5
                else {
                    PWBarr = eshieldNextDB.getLC_Male_Nonsmoker_slab1_PT();

                    for (int i = 18; i <= 65; i++) {
                        for (int j = 10; j <= 67; j++)
                        //        		for (int j=5; j<=30; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                }

                //       abcd
//        for (int i=18; i<=65; i++)
//        {
//        	for (int j=10; j<=67; j++)
////        		for (int j=5; j<=35; j++)
//            {
//                if((age)==i && (policyterm)==j)
//                {
//                	basicpremiumrate= PWBarr[position];
//                	break;
//                }
//                position++;
//            }
//        }


            } else if (premiumPayoption.equalsIgnoreCase("LPPT") && (gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Third Gender") || gender.equalsIgnoreCase("Third Gender")) && Smokertype.equalsIgnoreCase("Smoker") && getsumassuredslab(sumassured, premiumPayoption) == 0 && WholelifePopt.equalsIgnoreCase("Other than Whole life")) {
                double[] PWBarr = null;
                int position = 0;
                if (PPT == 5) {
                    PWBarr = eshieldNextDBRates.getLC_Male_smoker_slab1_LPPT5();
                    for (int i = 18; i <= 65; i++) {
                        for (int j = 10; j <= 67; j++)
//            		for (int j=5; j<=35; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else if (PPT == 7) {
                    PWBarr = eshieldNextDBRates.getLC_Male_smoker_slab1_LPPT7();
                    for (int i = 18; i <= 65; i++) {
                        for (int j = 10; j <= 67; j++)
//            		for (int j=5; j<=35; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else if (PPT == 10) {
                    PWBarr = eshieldNextDBRates.getLC_Male_smoker_slab1_LPPT10();
                    for (int i = 18; i <= 65; i++) {
                        for (int j = 15; j <= 67; j++)
//            		for (int j=5; j<=35; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else if (PPT == 15) {
                    PWBarr = eshieldNextDBRates.getLC_Male_smoker_slab1_LPPT15();
                    for (int i = 18; i <= 65; i++) {
                        for (int j = 20; j <= 67; j++)
//            		for (int j=5; j<=35; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else if (PPT == 20) {
                    PWBarr = eshieldNextDBRates.getLC_Male_smoker_slab1_LPPT20();
                    for (int i = 18; i <= 60; i++) {
                        for (int j = 25; j <= 67; j++)
//            		for (int j=5; j<=35; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else if (PPT == 25) {
                    PWBarr = eshieldNextDBRates.getLC_Male_smoker_slab1_LPPT25();
                    for (int i = 18; i <= 55; i++) {
                        for (int j = 30; j <= 67; j++)
//            		for (int j=5; j<=35; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else {
                    PWBarr = eshieldNextDB.getLC_Male_smoker_slab1_PT();
                    //   int position=0;
                    for (int i = 18; i <= 65; i++) {
                        for (int j = 10; j <= 67; j++)
//            		for (int j=5; j<=30; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                }

//        for (int i=18; i<=65; i++)
//        {
//        	for (int j=15; j<=67; j++)
////        		for (int j=5; j<=35; j++)
//            {
//                if((age)==i && (policyterm)==j)
//                {
//                	basicpremiumrate= PWBarr[position];
//                	break;
//                }
//                position++;
//            }
//        }
            } else if (premiumPayoption.equalsIgnoreCase("LPPT") && (gender.equalsIgnoreCase("Female")) && Smokertype.equalsIgnoreCase("Smoker") && getsumassuredslab(sumassured, premiumPayoption) == 0 && WholelifePopt.equalsIgnoreCase("Other than Whole life")) {
                double[] PWBarr = null;
                int position = 0;
                if (PPT == 5) {
                    PWBarr = eshieldNextDBRates.getLC_Female_smoker_slab1_LPPT5();
                    for (int i = 18; i <= 65; i++) {
                        for (int j = 10; j <= 67; j++)
//            		for (int j=5; j<=35; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else if (PPT == 7) {
                    PWBarr = eshieldNextDBRates.getLC_Female_smoker_slab1_LPPT7();
                    for (int i = 18; i <= 65; i++) {
                        for (int j = 10; j <= 67; j++)
//            		for (int j=5; j<=35; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else if (PPT == 10) {
                    PWBarr = eshieldNextDBRates.getLC_Female_smoker_slab1_LPPT10();
                    for (int i = 18; i <= 65; i++) {
                        for (int j = 15; j <= 67; j++)
//            		for (int j=5; j<=35; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else if (PPT == 15) {
                    PWBarr = eshieldNextDBRates.getLC_Female_smoker_slab1_LPPT15();
                    for (int i = 18; i <= 65; i++) {
                        for (int j = 20; j <= 67; j++)
//            		for (int j=5; j<=35; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else if (PPT == 20) {
                    PWBarr = eshieldNextDBRates.getLC_Female_smoker_slab1_LPPT20();
                    for (int i = 18; i <= 60; i++) {
                        for (int j = 25; j <= 67; j++)
//            		for (int j=5; j<=35; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else if (PPT == 25) {
                    PWBarr = eshieldNextDBRates.getLC_Female_smoker_slab1_LPPT25();
                    for (int i = 18; i <= 55; i++) {
                        for (int j = 30; j <= 67; j++)
//            		for (int j=5; j<=35; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else {//getLC_Male_smoker_slab1_PT()//getLC_Female_Nonsmoker_slab1_PT();
                    PWBarr = eshieldNextDB.getLC_Female_smoker_slab1_PT1();
                    //int position=0;
                    for (int i = 18; i <= 65; i++) {
                        for (int j = 10; j <= 67; j++)
//            		for (int j=5; j<=30; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                }

//        for (int i=18; i<=65; i++)
//        {
//        	for (int j=5; j<=67; j++)
////        		for (int j=5; j<=35; j++)
//            {
//                if((age)==i && (policyterm)==j)
//                {
//                	basicpremiumrate= PWBarr[position];
//                	break;
//                }
//                position++;
//            }
//        }


            } else if (premiumPayoption.equalsIgnoreCase("LPPT") && (gender.equalsIgnoreCase("Female")) && Smokertype.equalsIgnoreCase("Non Smoker") && getsumassuredslab(sumassured, premiumPayoption) == 0 && WholelifePopt.equalsIgnoreCase("Other than Whole life")) {
                if ((age) == 18 || (age) == 19 || (age) == 20 || (age) == 21) {
                    age = 18;
                } else {
                    age = (age - 3);
                }
                double[] PWBarr = null;
                int position = 0;
                if (PPT == 5) {
                    PWBarr = eshieldNextDBRates.getLC_Female_Nonsmoker_slab1_LPPT5();
                    for (int i = 18; i <= 65; i++) {
                        for (int j = 10; j <= 67; j++)
//            		for (int j=5; j<=35; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else if (PPT == 7) {
                    PWBarr = eshieldNextDBRates.getLC_Female_Nonsmoker_slab1_LPPT7();
                    for (int i = 18; i <= 65; i++) {
                        for (int j = 10; j <= 67; j++)
//            		for (int j=5; j<=35; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else if (PPT == 10) {
                    PWBarr = eshieldNextDBRates.getLC_Female_Nonsmoker_slab1_LPPT10();
                    for (int i = 18; i <= 65; i++) {
                        for (int j = 15; j <= 67; j++)
//            		for (int j=5; j<=35; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else if (PPT == 15) {
                    PWBarr = eshieldNextDBRates.getLC_Female_Nonsmoker_slab1_LPPT15();
                    for (int i = 18; i <= 65; i++) {
                        for (int j = 20; j <= 67; j++)
//            		for (int j=5; j<=35; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else if (PPT == 20) {
                    PWBarr = eshieldNextDBRates.getLC_Female_Nonsmoker_slab1_LPPT20();
                    for (int i = 18; i <= 60; i++) {
                        for (int j = 25; j <= 67; j++)
//            		for (int j=5; j<=35; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else if (PPT == 25) {
                    PWBarr = eshieldNextDBRates.getLC_Female_Nonsmoker_slab1_LPPT25();
                    for (int i = 18; i <= 55; i++) {
                        for (int j = 30; j <= 67; j++)
//            		for (int j=5; j<=35; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else {//getLC_Male_Nonsmoker_slab1_PT()//getLC_Female_Nonsmoker_slab1_PT()
                    PWBarr = eshieldNextDB.getLC_Male_Nonsmoker_slab1_PT();
                    //int position=0;
                    for (int i = 18; i <= 65; i++) {
                        for (int j = 10; j <= 67; j++)
//            		for (int j=5; j<=30; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                }

//        for (int i=18; i<=65; i++)
//        {
//        	for (int j=10; j<=67; j++)
////        		for (int j=5; j<=35; j++)
//            {
//                if((age)==i && (policyterm)==j)
//                {
//                	basicpremiumrate= PWBarr[position];
//                	break;
//                }
//                position++;
//            }
//        }


            }
            //policy Term less 5 Level Cover
            else if (premiumPayoption.equalsIgnoreCase("LPPT") && (gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Third Gender") || gender.equalsIgnoreCase("Third Gender")) && Smokertype.equalsIgnoreCase("Non Smoker") && getsumassuredslab(sumassured, premiumPayoption) == 0 && WholelifePopt.equalsIgnoreCase("Other than Whole life")) {
                double val = 0;

                if (PPT == 5)
                    val = 0;
                else if (PPT == 7)
                    val = 1;
                else if (PPT == 10)
                    val = 2;
                else if (PPT == 15)
                    val = 3;
                else if (PPT == 20)
                    val = 4;
                else if (PPT == 25)
                    val = 5;
                else
                    val = 6;
                if (val == 6) {
                    double[] PWBarr = eshieldNextDB.getLC_Male_Nonsmoker_slab1_PT();
                    int position = 0;
                    for (int i = 18; i <= 65; i++) {
                        for (int j = 10; j <= 67; j++)
                        //        		for (int j=5; j<=30; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else {
                    basicpremiumrate = 0;
                }
            } else if (premiumPayoption.equalsIgnoreCase("LPPT") && (gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Third Gender") || gender.equalsIgnoreCase("Third Gender")) && Smokertype.equalsIgnoreCase("Smoker") && getsumassuredslab(sumassured, premiumPayoption) == 0 && WholelifePopt.equalsIgnoreCase("Other than Whole life")) {
                double val = 0;

                if (PPT == 5)
                    val = 0;
                else if (PPT == 7)
                    val = 1;
                else if (PPT == 10)
                    val = 2;
                else if (PPT == 15)
                    val = 3;
                else if (PPT == 20)
                    val = 4;
                else if (PPT == 25)
                    val = 5;
                else
                    val = 6;

                if (val == 6) {
                    double[] PWBarr = eshieldNextDB.getLC_Male_smoker_slab1_PT();
                    int position = 0;
                    for (int i = 18; i <= 65; i++) {
                        for (int j = 10; j <= 67; j++)
//        		for (int j=5; j<=30; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else {
                    basicpremiumrate = 0;
                }

            } else if (premiumPayoption.equalsIgnoreCase("LPPT") && PPT > 25 && (gender.equalsIgnoreCase("Female")) && Smokertype.equalsIgnoreCase("Non Smoker") && getsumassuredslab(sumassured, premiumPayoption) == 1 && WholelifePopt.equalsIgnoreCase("Other than Whole life")) {
                double val = 0;

                if ((age) == 18 || (age) == 19 || (age) == 20 || (age) == 21) {
                    age = 18;
                } else {
                    age = (age - 3);
                }


                if (PPT == 5)
                    val = 0;
                else if (PPT == 7)
                    val = 1;
                else if (PPT == 10)
                    val = 2;
                else if (PPT == 15)
                    val = 3;
                else if (PPT == 20)
                    val = 4;
                else if (PPT == 25)
                    val = 5;
                else
                    val = 6;

                if (val == 6) {//getLC_Male_Nonsmoker_slab3_PT1crore()//getLC_Female_Nonsmoker_slab1_PT()
                    double[] PWBarr = eshieldNextDB.getLC_Male_Nonsmoker_slab3_PT1crore();
                    int position = 0;
                    for (int i = 18; i <= 65; i++) {
                        for (int j = 10; j <= 67; j++)
//        		for (int j=5; j<=30; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else {
                    basicpremiumrate = 0;
                }
            } else if (premiumPayoption.equalsIgnoreCase("LPPT") && (gender.equalsIgnoreCase("Female")) && Smokertype.equalsIgnoreCase("Smoker") && getsumassuredslab(sumassured, premiumPayoption) == 0 && WholelifePopt.equalsIgnoreCase("Other than Whole life")) {
                double val = 0;

                if (PPT == 5)
                    val = 0;
                else if (PPT == 7)
                    val = 1;
                else if (PPT == 10)
                    val = 2;
                else if (PPT == 15)
                    val = 3;
                else if (PPT == 20)
                    val = 4;
                else if (PPT == 25)
                    val = 5;
                else
                    val = 6;

                if (val == 6) {
                    double[] PWBarr = eshieldNextDB.getLC_Female_smoker_slab1_PT();
                    int position = 0;
                    for (int i = 18; i <= 65; i++) {
                        for (int j = 10; j <= 67; j++)
//        		for (int j=5; j<=30; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else {
                    basicpremiumrate = 0;
                }
            }


            //Whole Life Option
            else if (planoption.equalsIgnoreCase("Level Cover") && premiumPayoption.equalsIgnoreCase("LPPT") && (gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Third Gender") || gender.equalsIgnoreCase("Third Gender")) && Smokertype.equalsIgnoreCase("Non Smoker") && getsumassuredslab(sumassured, premiumPayoption) == 0 && WholelifePopt.equalsIgnoreCase("Whole life")) {
                double[] PWBarr = eshieldNextDB_WLRates.getLC_Male_Nonsmoker_slab1_LPPT1(PPT);
                int position = 0;
                for (int i = 42; i <= 65; i++) {

                    if ((age) == i) {
                        basicpremiumrate = PWBarr[position];
                        break;
                    }
                    position++;

                }
            } else if (planoption.equalsIgnoreCase("Level Cover") && premiumPayoption.equalsIgnoreCase("LPPT") && Smokertype.equalsIgnoreCase("Non Smoker") && getsumassuredslab(sumassured, premiumPayoption) == 1 && WholelifePopt.equalsIgnoreCase("Whole life")) {
                if (gender.equalsIgnoreCase("Female")) {
                    if ((age) == 18 || (age) == 19 || (age) == 20 || (age) == 21) {
                        age = 18;
                    } else {
                        age = (age - 3);
                    }
                }
                double[] PWBarr = eshieldNextDB_WLRates.getLC_Male_Nonsmoker_slab1_LPPT_WL1Crore(PPT);
                int position = 0;
                for (int i = 42; i <= 65; i++) {

                    if ((age) == i) {
                        basicpremiumrate = PWBarr[position];
                        break;
                    }
                    position++;

                }
            } else if (planoption.equalsIgnoreCase("Level Cover") && premiumPayoption.equalsIgnoreCase("LPPT") && (gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Third Gender") || gender.equalsIgnoreCase("Third Gender")) && Smokertype.equalsIgnoreCase("Smoker") && getsumassuredslab(sumassured, premiumPayoption) == 0 && WholelifePopt.equalsIgnoreCase("Whole life")) {
                double[] PWBarr = eshieldNextDB_WLRates.getLC_Male_smoker_slab1_LPPT1(PPT);
                int position = 0;
                for (int i = 42; i <= 65; i++) {

                    if ((age) == i) {
                        basicpremiumrate = PWBarr[position];
                        break;
                    }
                    position++;

                }


            } else if (planoption.equalsIgnoreCase("Level Cover") && premiumPayoption.equalsIgnoreCase("LPPT") && (gender.equalsIgnoreCase("Female")) && Smokertype.equalsIgnoreCase("Non Smoker") && getsumassuredslab(sumassured, premiumPayoption) == 0 && WholelifePopt.equalsIgnoreCase("Whole life")) {
                double[] PWBarr = eshieldNextDB_WLRates.getLC_Male_Nonsmoker_slab1_LPPT1(PPT);
                int position = 0;
                for (int i = 45; i <= 65; i++) {

                    if ((age) == i) {
                        basicpremiumrate = PWBarr[position];
                        break;
                    }
                    position++;

                }
            } else if (planoption.equalsIgnoreCase("Level Cover") && premiumPayoption.equalsIgnoreCase("LPPT") && (gender.equalsIgnoreCase("Female")) && Smokertype.equalsIgnoreCase("Smoker") && getsumassuredslab(sumassured, premiumPayoption) == 0 && WholelifePopt.equalsIgnoreCase("Whole life")) {
                if ((age) == 18 || (age) == 19 || (age) == 20 || (age) == 21) {
                    age = 18;
                } else {
                    age = (age - 3);
                }
                double[] PWBarr = eshieldNextDB_WLRates.getLC_Female_Nonsmoker_slab1_LPPT1(PPT);
                int position = 0;
                for (int i = 42; i <= 65; i++) {

                    if ((age) == i) {
                        basicpremiumrate = PWBarr[position];
                        break;
                    }
                    position++;

                }
            }


            //medical add //


//		if(planoption.equalsIgnoreCase("Medical"))
//		{
            else if (premiumPayoption.equalsIgnoreCase("Regular") && Smokertype.equalsIgnoreCase("Non Smoker") && getsumassuredslab(sumassured, premiumPayoption) == 2 && WholelifePopt.equalsIgnoreCase("Other than Whole life")) {
                if (gender.equalsIgnoreCase("Female")) {
                    if ((age) == 18 || (age) == 19 || (age) == 20 || (age) == 21) {
                        age = 18;
                    } else {
                        age = (age - 3);
                    }
                }
                double[] PWBarr = eshieldNextDB.getLC_Male_Nonsmoker_slab2_Regulare1crore();
                int position = 0;
                for (int i = 18; i <= 65; i++) {
                    for (int j = 5; j <= 67; j++)
//        		for (int j=5; j<=30; j++)
                    {
                        if ((age) == i && (policyterm) == j) {
                            basicpremiumrate = PWBarr[position];
                            break;
                        }
                        position++;
                    }
                }
            } else if (premiumPayoption.equalsIgnoreCase("Regular") && Smokertype.equalsIgnoreCase("Non Smoker") && getsumassuredslab(sumassured, premiumPayoption) == 3 && WholelifePopt.equalsIgnoreCase("Other than Whole life")) {
                if (gender.equalsIgnoreCase("Female")) {
                    if ((age) == 18 || (age) == 19 || (age) == 20 || (age) == 21) {
                        age = 18;
                    } else {
                        age = (age - 3);
                    }
                }
                double[] PWBarr = eshieldNextDB.getLC_Male_Nonsmoker_slab2_Regulare5crore();
                int position = 0;
                for (int i = 18; i <= 60; i++) {
                    for (int j = 5; j <= 67; j++)
//        		for (int j=5; j<=30; j++)
                    {
                        if ((age) == i && (policyterm) == j) {
                            basicpremiumrate = PWBarr[position];
                            //System.out.println("basicpremiumrate "+basicpremiumrate);
                            break;
                        }
                        position++;
                    }
                }
            } else if (premiumPayoption.equalsIgnoreCase("Single") && Smokertype.equalsIgnoreCase("Non Smoker") && getsumassuredslab(sumassured, premiumPayoption) == 2 && WholelifePopt.equalsIgnoreCase("Other than Whole life")) {
                if (gender.equalsIgnoreCase("Female")) {
                    if ((age) == 18 || (age) == 19 || (age) == 20 || (age) == 21) {
                        age = 18;
                    } else {
                        age = (age - 3);
                    }
                }
                double[] PWBarr = eshieldNextDB.getLC_Male_Nonsmoker_slab2_Single1crore();
                int position = 0;
                for (int i = 18; i <= 65; i++) {
                    for (int j = 5; j <= 67; j++)
//        		for (int j=5; j<=30; j++)
                    {
                        if ((age) == i && (policyterm) == j) {
                            basicpremiumrate = PWBarr[position];
                            break;
                        }
                        position++;
                    }
                }
            } else if (premiumPayoption.equalsIgnoreCase("Single") && getsumassuredslab(sumassured, premiumPayoption) == 3 && WholelifePopt.equalsIgnoreCase("Other than Whole life")) {
                if (gender.equalsIgnoreCase("Female")) {
                    if ((age) == 18 || (age) == 19 || (age) == 20 || (age) == 21) {
                        age = 18;
                    } else {
                        age = (age - 3);
                    }
                }
                double[] PWBarr = eshieldNextDB.getLC_Male_Nonsmoker_slab2_Single5crore();
                int position = 0;
                for (int i = 18; i <= 65; i++) {
                    for (int j = 5; j <= 67; j++)
//        		for (int j=5; j<=30; j++)
                    {
                        if ((age) == i && (policyterm) == j) {
                            basicpremiumrate = PWBarr[position];
                            break;
                        }
                        position++;
                    }
                }
            } else if (premiumPayoption.equalsIgnoreCase("LPPT") && Smokertype.equalsIgnoreCase("Non Smoker")
                    && getsumassuredslab(sumassured, premiumPayoption) == 1 && WholelifePopt.equalsIgnoreCase("Other than Whole life")) {
                if (gender.equalsIgnoreCase("Female")) {
                    if ((age) == 18 || (age) == 19 || (age) == 20 || (age) == 21) {
                        age = 18;
                    } else {
                        age = (age - 3);
                    }
                }
                double[] PWBarr = null;
                int position = 0;

                if (PPT == 5) {
                    PWBarr = eshieldNextDBRates.getLC_Male_Nonsmoker_slab1_LPPT5_1crore();
                    for (int i = 18; i <= 65; i++) {
                        for (int j = 10; j <= 67; j++)
//            		for (int j=5; j<=30; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else if (PPT == 7) {
                    PWBarr = eshieldNextDBRates.getLC_Male_Nonsmoker_slab1_LPPT7_1crore();
                    for (int i = 18; i <= 65; i++) {
                        for (int j = 10; j <= 67; j++)
//            		for (int j=5; j<=30; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else if (PPT == 10) {
                    PWBarr = eshieldNextDBRates.getLC_Male_Nonsmoker_slab1_LPPT10_1crore();
                    for (int i = 18; i <= 65; i++) {
                        for (int j = 15; j <= 67; j++)
//            		for (int j=5; j<=30; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else if (PPT == 15) {
                    PWBarr = eshieldNextDBRates.getLC_Male_Nonsmoker_slab1_LPPT15_1crore();
                    for (int i = 18; i <= 65; i++) {
                        for (int j = 20; j <= 67; j++)
//            		for (int j=5; j<=30; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else if (PPT == 20) {
                    PWBarr = eshieldNextDBRates.getLC_Male_Nonsmoker_slab1_LPPT20_1crore();
                    for (int i = 18; i <= 65; i++) {
                        for (int j = 25; j <= 67; j++)
//            		for (int j=5; j<=30; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else if (PPT == 25) {
                    PWBarr = eshieldNextDBRates.getLC_Male_Nonsmoker_slab1_LPPT25_1crore();
                    for (int i = 18; i <= 65; i++) {
                        for (int j = 30; j <= 67; j++)
//            		for (int j=5; j<=30; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else {
                    PWBarr = eshieldNextDB.getLC_Male_Nonsmoker_slab3_PT1crore();
                    for (int i = 18; i <= 65; i++) {
                        for (int j = 10; j <= 67; j++)
//            		for (int j=5; j<=30; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                }
//        for (int i=18; i<=65; i++)
//        {
//        	for (int j=5; j<=67; j++)
////        		for (int j=5; j<=30; j++)
//            {
//                if((age)==i && (policyterm)==j)
//                {
//                	basicpremiumrate= PWBarr[position];
//                	break;
//                }
//                position++;
//            }
//        }
            }


            //policy trem less 5 1croe above
            else if (premiumPayoption.equalsIgnoreCase("LPPT") && Smokertype.equalsIgnoreCase("Non Smoker") && getsumassuredslab(sumassured, premiumPayoption) == 3 && WholelifePopt.equalsIgnoreCase("Other than Whole life")) {
                if (gender.equalsIgnoreCase("Female")) {
                    if ((age) == 18 || (age) == 19 || (age) == 20 || (age) == 21) {
                        age = 18;
                    } else {
                        age = (age - 3);
                    }
                }
                double val = 0;

                if (PPT == 5)
                    val = 0;
                else if (PPT == 7)
                    val = 1;
                else if (PPT == 10)
                    val = 2;
                else if (PPT == 15)
                    val = 3;
                else if (PPT == 20)
                    val = 4;
                else if (PPT == 25)
                    val = 5;
                else
                    val = 6;

                if (val == 6) {
                    double[] PWBarr = eshieldNextDB.getLC_Male_Nonsmoker_slab3_PT1crore();
                    int position = 0;
                    for (int i = 18; i <= 65; i++) {
                        for (int j = 10; j <= 67; j++)
//	        		for (int j=5; j<=30; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else {
                    basicpremiumrate = 0;
                }
            } else if (premiumPayoption.equalsIgnoreCase("LPPT") && (gender.equalsIgnoreCase("Female")) && Smokertype.equalsIgnoreCase("Non Smoker") && getsumassuredslab(sumassured, premiumPayoption) == 1 && WholelifePopt.equalsIgnoreCase("Whole life")) {
                if ((age) == 18 || (age) == 19 || (age) == 20 || (age) == 21) {
                    age = 18;
                } else {
                    age = (age - 3);
                }

                double[] PWBarr = eshieldNextDBRates.getLC_Male_Nonsmoker_slab3_LPPT1_1crore();
                int position = 0;
                for (int i = 42; i <= 65; i++) {

                    if ((age) == i) {
                        basicpremiumrate = PWBarr[position];
                        break;
                    }
                    position++;


                }


            }
            //}

//		else if(planoption.equalsIgnoreCase("Level Cover") && gender.equalsIgnoreCase("Female") && Smokertype.equalsIgnoreCase("Non Smoker") && getsumassuredslab(sumassured)==1)
//		{
//		double [] PWBarr=eshieldNextDB.getLC_Female_nonsmoker_slab1();
//        int position=0;
//        for (int i=18; i<=60; i++)
//        {
//        	for (int j=5; j<=67; j++)
////        		for (int j=5; j<=35; j++)
//            {
//                if((age)==i && (policyterm)==j)
//                {
//                	basicpremiumrate= PWBarr[position];
//                	break;
//                }
//                position++;
//            }
//        }
//
//
//		}
        } else
        //if(planoption.equalsIgnoreCase("Increasing Cover"))
        {
            if (planoption.equalsIgnoreCase("Increasing Cover") && premiumPayoption.equalsIgnoreCase("Regular") && (gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Third Gender") || gender.equalsIgnoreCase("Third Gender")) && Smokertype.equalsIgnoreCase("Non Smoker") && getsumassuredslab(sumassured, premiumPayoption) == 0 && WholelifePopt.equalsIgnoreCase("Other than Whole life")) {
                double[] PWBarr = eshieldNextDB.getIC_Male_Nonsmoker_slab0();
                int position = 0;
                for (int i = 18; i <= 60; i++) {
                    for (int j = 10; j <= 67; j++)
//        		for (int j=10; j<=30; j++)
                    {
                        if ((age) == i && (policyterm) == j) {
                            basicpremiumrate = PWBarr[position];
                            break;
                        }
                        position++;
                    }
                }


            } else if (planoption.equalsIgnoreCase("Increasing Cover") && premiumPayoption.equalsIgnoreCase("Regular") && (gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Third Gender") || gender.equalsIgnoreCase("Third Gender")) && Smokertype.equalsIgnoreCase("Smoker") && getsumassuredslab(sumassured, premiumPayoption) == 0 && WholelifePopt.equalsIgnoreCase("Other than Whole life")) {
                double[] PWBarr = eshieldNextDB.getIC_Male_smoker_slab0();
                int position = 0;
                for (int i = 18; i <= 60; i++) {
                    for (int j = 10; j <= 67; j++)
//        		for (int j=10; j<=30; j++)
                    {
                        if ((age) == i && (policyterm) == j) {
                            basicpremiumrate = PWBarr[position];
                            break;
                        }
                        position++;
                    }
                }
            } else if (planoption.equalsIgnoreCase("Increasing Cover") && premiumPayoption.equalsIgnoreCase("Regular") && (gender.equalsIgnoreCase("Female")) && Smokertype.equalsIgnoreCase("Smoker") && getsumassuredslab(sumassured, premiumPayoption) == 0 && WholelifePopt.equalsIgnoreCase("Other than Whole life")) {
                double[] PWBarr = eshieldNextDB.getIC_Female_smoker_slab0();


                int position = 0;
                for (int i = 18; i <= 60; i++) {
                    for (int j = 10; j <= 67; j++)
//        		for (int j=10; j<=30; j++)
                    {
                        if ((age) == i && (policyterm) == j) {
                            basicpremiumrate = PWBarr[position];
                            break;
                        }
                        position++;
                    }
                }


            }

            if (planoption.equalsIgnoreCase("Increasing Cover") && premiumPayoption.equalsIgnoreCase("Regular") && (gender.equalsIgnoreCase("Female")) && Smokertype.equalsIgnoreCase("Non Smoker") && getsumassuredslab(sumassured, premiumPayoption) == 0 && WholelifePopt.equalsIgnoreCase("Other than Whole life")) {

                if ((age) == 18 || (age) == 19 || (age) == 20 || (age) == 21) {
                    age = 18;
                } else {
                    age = (age - 3);
                }
                double[] PWBarr = eshieldNextDB.getIC_Female_Nonsmoker_Singleslab011();

                int position = 0;
                for (int i = 18; i <= 60; i++) {
                    for (int j = 10; j <= 67; j++)
//        		for (int j=10; j<=30; j++)
                    {
                        if ((age) == i && (policyterm) == j) {
                            basicpremiumrate = PWBarr[position];
                            break;
                        }
                        position++;
                    }
                }
                System.out.println("basicpremiumrate " + basicpremiumrate);

            } else if (planoption.equalsIgnoreCase("Increasing Cover") && premiumPayoption.equalsIgnoreCase("Single") && (gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Third Gender") || gender.equalsIgnoreCase("Third Gender")) && Smokertype.equalsIgnoreCase("Non Smoker") && getsumassuredslab(sumassured, premiumPayoption) == 0 && WholelifePopt.equalsIgnoreCase("Other than Whole life")) {
                double[] PWBarr = eshieldNextDB.getIC_Male_Nonsmoker_slab01();


                int position = 0;
                for (int i = 18; i <= 65; i++) {
                    for (int j = 10; j <= 67; j++)
//        		for (int j=10; j<=30; j++)
                    {
                        if ((age) == i && (policyterm) == j) {
                            basicpremiumrate = PWBarr[position];
                            break;
                        }
                        position++;
                    }
                }


            } else if (planoption.equalsIgnoreCase("Increasing Cover") && premiumPayoption.equalsIgnoreCase("Single") && (gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Third Gender") || gender.equalsIgnoreCase("Third Gender")) && Smokertype.equalsIgnoreCase("Smoker") && getsumassuredslab(sumassured, premiumPayoption) == 0 && WholelifePopt.equalsIgnoreCase("Other than Whole life")) {
                double[] PWBarr = eshieldNextDB.getIC_Male_smokerSingle_slab0();
                int position = 0;
                for (int i = 18; i <= 65; i++) {
                    for (int j = 10; j <= 67; j++)
//        		for (int j=10; j<=30; j++)
                    {
                        if ((age) == i && (policyterm) == j) {
                            basicpremiumrate = PWBarr[position];
                            break;
                        }
                        position++;
                    }
                }


            } else if (planoption.equalsIgnoreCase("Increasing Cover") && premiumPayoption.equalsIgnoreCase("Single") && (gender.equalsIgnoreCase("Female")) && Smokertype.equalsIgnoreCase("Smoker") && getsumassuredslab(sumassured, premiumPayoption) == 0 && WholelifePopt.equalsIgnoreCase("Other than Whole life")) {
                double[] PWBarr = eshieldNextDB.getIC_Female_smokerSingle_slab0();
                int position = 0;
                for (int i = 18; i <= 65; i++) {
                    for (int j = 10; j <= 67; j++)
//        		for (int j=10; j<=30; j++)
                    {
                        if ((age) == i && (policyterm) == j) {
                            basicpremiumrate = PWBarr[position];
                            break;
                        }
                        position++;
                    }
                }


            } else if (premiumPayoption.equalsIgnoreCase("Single") && (gender.equalsIgnoreCase("Female")) && Smokertype.equalsIgnoreCase("Non Smoker") && getsumassuredslab(sumassured, premiumPayoption) == 0 && WholelifePopt.equalsIgnoreCase("Other than Whole life")) {
                double[] PWBarr = eshieldNextDBRates.getIC_Female_NonsmokerSingle_slab0();
                if ((age) == 18 || (age) == 19 || (age) == 20 || (age) == 21) {
                    age = 18;
                } else {
                    age = (age - 3);
                }
                int position = 0;
                for (int i = 18; i <= 65; i++) {
                    for (int j = 10; j <= 67; j++)
//        		for (int j=10; j<=30; j++)
                    {
                        if ((age) == i && (policyterm) == j) {
                            basicpremiumrate = PWBarr[position];
                            break;
                        }
                        position++;
                    }
                }


            } else if (planoption.equalsIgnoreCase("Increasing Cover") && premiumPayoption.equalsIgnoreCase("LPPT") && (gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Third Gender") || gender.equalsIgnoreCase("Third Gender")) && Smokertype.equalsIgnoreCase("Non Smoker") && getsumassuredslab(sumassured, premiumPayoption) == 0 && WholelifePopt.equalsIgnoreCase("Other than Whole life")) {
                double[] PWBarr = null;
                int position = 0;
                if (PPT == 5) {
                    PWBarr = eshieldNextDBICRates.getIC_Male_Nonsmoker_slab1_LPPT5();
                    for (int i = 18; i <= 65; i++) {
                        for (int j = 10; j <= 67; j++)
//            		for (int j=10; j<=30; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else if (PPT == 7) {
                    PWBarr = eshieldNextDBICRates.getIC_Male_Nonsmoker_slab1_LPPT7();
                    for (int i = 18; i <= 65; i++) {
                        for (int j = 10; j <= 67; j++)
//             		for (int j=10; j<=30; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else if (PPT == 10) {
                    PWBarr = eshieldNextDBICRates.getIC_Male_Nonsmoker_slab1_LPPT10();
                    for (int i = 18; i <= 65; i++) {
                        for (int j = 15; j <= 67; j++)
//             		for (int j=10; j<=30; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else if (PPT == 15) {
                    PWBarr = eshieldNextDBICRates.getIC_Male_Nonsmoker_slab1_LPPT15();
                    for (int i = 18; i <= 60; i++) {
                        for (int j = 20; j <= 67; j++)
//             		for (int j=10; j<=30; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else if (PPT == 20) {
                    PWBarr = eshieldNextDBICRates.getIC_Male_Nonsmoker_slab1_LPPT20();
                    for (int i = 18; i <= 60; i++) {
                        for (int j = 25; j <= 67; j++)
//            		for (int j=10; j<=30; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else if (PPT == 25) {
                    PWBarr = eshieldNextDBICRates.getIC_Male_Nonsmoker_slab1_LPPT25();
                    for (int i = 18; i <= 60; i++) {
                        for (int j = 30; j <= 67; j++)
//            		for (int j=10; j<=30; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else {
                    PWBarr = eshieldNextDBICRates.getIC_Male_Nonsmoker_slab1_LPPT30();
                    for (int i = 18; i <= 65; i++) {
                        for (int j = 10; j <= 67; j++)
//            		for (int j=10; j<=30; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                }
//        for (int i=18; i<=65; i++)
//        {
//        	for (int j=10; j<=67; j++)
////        		for (int j=10; j<=30; j++)
//            {
//                if((age)==i && (policyterm)==j)
//                {
//                	basicpremiumrate= PWBarrL[position];
//                	break;
//                }
//                position++;
//            }
//        }
//

            } else if (planoption.equalsIgnoreCase("Increasing Cover") && premiumPayoption.equalsIgnoreCase("LPPT") && (gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Third Gender") || gender.equalsIgnoreCase("Third Gender")) && Smokertype.equalsIgnoreCase("Smoker") && getsumassuredslab(sumassured, premiumPayoption) == 0 && WholelifePopt.equalsIgnoreCase("Other than Whole life")) {
                double[] PWBarr = null;
                int position = 0;
                if (PPT == 5) {
                    PWBarr = eshieldNextDBICRates.getIC_Male_Smoker_slab1_LPPT5();
                    for (int i = 18; i <= 60; i++) {
                        for (int j = 10; j <= 67; j++)
//            		for (int j=10; j<=30; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else if (PPT == 7) {
                    PWBarr = eshieldNextDBICRates.getIC_Male_Smoker_slab1_LPPT7();
                    for (int i = 18; i <= 60; i++) {
                        for (int j = 10; j <= 67; j++)
//            		for (int j=10; j<=30; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else if (PPT == 10) {
                    PWBarr = eshieldNextDBICRates.getIC_Male_Smoker_slab1_LPPT10();
                    for (int i = 18; i <= 60; i++) {
                        for (int j = 15; j <= 67; j++)
//             		for (int j=10; j<=30; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else if (PPT == 15) {
                    PWBarr = eshieldNextDBICRates.getIC_Male_Smoker_slab1_LPPT15();
                    for (int i = 18; i <= 60; i++) {
                        for (int j = 20; j <= 67; j++)
//             		for (int j=10; j<=30; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else if (PPT == 20) {
                    PWBarr = eshieldNextDBICRates.getIC_Male_Smoker_slab1_LPPT20();
                    for (int i = 18; i <= 60; i++) {
                        for (int j = 25; j <= 67; j++)
//             		for (int j=10; j<=30; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else if (PPT == 25) {
                    PWBarr = eshieldNextDBICRates.getIC_Male_Smoker_slab1_LPPT25();
                    for (int i = 18; i <= 60; i++) {
                        for (int j = 30; j <= 67; j++)
//            		for (int j=10; j<=30; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else {
                    PWBarr = eshieldNextDBICRates.getIC_Male_Smoker_slab1_LPPT30();
                    for (int i = 18; i <= 65; i++) {
                        for (int j = 10; j <= 67; j++)
//             		for (int j=10; j<=30; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                }
//        for (int i=18; i<=60; i++)
//        {
//        	for (int j=5; j<=67; j++)
////        		for (int j=10; j<=30; j++)
//            {
//                if((age)==i && (policyterm)==j)
//                {
//                	basicpremiumrate= PWBarr[position];
//                	break;
//                }
//                position++;
//            }
//        }


            } else if (planoption.equalsIgnoreCase("Increasing Cover") && premiumPayoption.equalsIgnoreCase("LPPT") && gender.equalsIgnoreCase("Female") && Smokertype.equalsIgnoreCase("Smoker") && getsumassuredslab(sumassured, premiumPayoption) == 0 && WholelifePopt.equalsIgnoreCase("Other than Whole life")) {
                double[] PWBarr = null;
                int position = 0;
                if (PPT == 5) {
                    PWBarr = eshieldNextDBICRates.getIC_Female_Smoker_slab1_LPPT5();
                    for (int i = 18; i <= 60; i++) {
                        for (int j = 10; j <= 67; j++)
//            		for (int j=10; j<=30; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else if (PPT == 7) {
                    PWBarr = eshieldNextDBICRates.getIC_Female_Smokerr_slab1_LPPT7();
                    for (int i = 18; i <= 65; i++) {
                        for (int j = 10; j <= 67; j++)
//             		for (int j=10; j<=30; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else if (PPT == 10) {
                    PWBarr = eshieldNextDBICRates.getIC_Female_Smoker_slab1_LPPT10();
                    for (int i = 18; i <= 65; i++) {
                        for (int j = 15; j <= 67; j++)
//              		for (int j=10; j<=30; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else if (PPT == 15) {
                    PWBarr = eshieldNextDBICRates.getIC_Female_Smoker_slab1_LPPT15();
                    for (int i = 18; i <= 65; i++) {
                        for (int j = 20; j <= 67; j++)
//             		for (int j=10; j<=30; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else if (PPT == 20) {
                    PWBarr = eshieldNextDBICRates.getIC_Female_Smoker_slab1_LPPT20();
                    for (int i = 18; i <= 60; i++) {
                        for (int j = 25; j <= 67; j++)
//            		for (int j=10; j<=30; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else if (PPT == 25) {
                    PWBarr = eshieldNextDBICRates.getIC_Female_Smoker_slab1_LPPT25();
                    for (int i = 18; i <= 60; i++) {
                        for (int j = 30; j <= 67; j++)
//            		for (int j=10; j<=30; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else {
                    PWBarr = eshieldNextDBICRates.getIC_Female_Smoker_slab1_LPPT30();
                    for (int i = 18; i <= 65; i++) {
                        for (int j = 10; j <= 67; j++)
//             		for (int j=10; j<=30; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                }
//        for (int i=18; i<=60; i++)
//        {
//        	for (int j=5; j<=67; j++)
////        		for (int j=10; j<=30; j++)
//            {
//                if((age)==i && (policyterm)==j)
//                {
//                	basicpremiumrate= PWBarr[position];
//                	break;
//                }
//                position++;
//            }
//        }


            } else if (planoption.equalsIgnoreCase("Increasing Cover") && premiumPayoption.equalsIgnoreCase("LPPT") && gender.equalsIgnoreCase("Female") && Smokertype.equalsIgnoreCase("Non Smoker") && getsumassuredslab(sumassured, premiumPayoption) == 0 && WholelifePopt.equalsIgnoreCase("Other than Whole life")) {
                if ((age) == 18 || (age) == 19 || (age) == 20 || (age) == 21) {
                    age = 18;
                } else {
                    age = (age - 3);
                }

                double[] PWBarr = null;
                int position = 0;
                if (PPT == 5) {
                    PWBarr = eshieldNextDBICRates.getIC_Female_Nonsmoker_slab1_LPPT5();
                    for (int i = 18; i <= 60; i++) {
                        for (int j = 10; j <= 67; j++)
//            		for (int j=10; j<=30; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else if (PPT == 7) {
                    PWBarr = eshieldNextDBICRates.getIC_Female_Smoker_slab1_LPPT7();
                    for (int i = 18; i <= 60; i++) {
                        for (int j = 10; j <= 67; j++)
//             		for (int j=10; j<=30; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else if (PPT == 10) {
                    PWBarr = eshieldNextDBICRates.getIC_Female_Nonsmoker_slab1_LPPT10();
                    for (int i = 18; i <= 60; i++) {
                        for (int j = 15; j <= 67; j++)
//              		for (int j=10; j<=30; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else if (PPT == 15) {
                    PWBarr = eshieldNextDBICRates.getIC_Female_Nonsmoker_slab1_LPPT15();
                    for (int i = 18; i <= 60; i++) {
                        for (int j = 20; j <= 67; j++)
//             		for (int j=10; j<=30; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else if (PPT == 20) {
                    PWBarr = eshieldNextDBICRates.getIC_Female_Nonsmoker_slab1_LPPT20();
                    for (int i = 18; i <= 60; i++) {
                        for (int j = 25; j <= 67; j++)
//               		for (int j=10; j<=30; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else if (PPT == 25) {
                    PWBarr = eshieldNextDBICRates.getIC_Female_Nonsmoker_slab1_LPPT25();
                    for (int i = 18; i <= 60; i++) {
                        for (int j = 30; j <= 67; j++)
//            		for (int j=10; j<=30; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else {
                    PWBarr = eshieldNextDBICRates.getIC_Female_Nonsmoker_slab1_LPPT30();
                    for (int i = 18; i <= 65; i++) {
                        for (int j = 10; j <= 67; j++)
//             		for (int j=10; j<=30; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                }
//            for (int i=18; i<=60; i++)
//        {
//        	for (int j=5; j<=67; j++)
////        		for (int j=10; j<=30; j++)
//            {
//                if((age)==i && (policyterm)==j)
//                {
//                	basicpremiumrate= PWBarr[position];
//                	break;
//                }
//                position++;
//            }
//        }


            } else if (planoption.equalsIgnoreCase("Increasing Cover") && premiumPayoption.equalsIgnoreCase("LPPT") && (gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Third Gender") || gender.equalsIgnoreCase("Third Gender")) && Smokertype.equalsIgnoreCase("Non Smoker") && getsumassuredslab(sumassured, premiumPayoption) == 0 && WholelifePopt.equalsIgnoreCase("Other than Whole life")) {
                double val = 0;

                if (PPT == 5)
                    val = 0;
                else if (PPT == 7)
                    val = 1;
                else if (PPT == 10)
                    val = 2;
                else if (PPT == 15)
                    val = 3;
                else if (PPT == 20)
                    val = 4;
                else if (PPT == 25)
                    val = 5;
                else
                    val = 6;

                double[] PWBarr = eshieldNextDBICRates.getIC_Male_Nonsmoker_slab1_PL();
                int position = 0;


                if (val == 6) {
                    for (int i = 18; i <= 65; i++) {
//        	for (int j=10; j<=getPolicyTermMax(planoption); j++)
                        for (int j = 10; j <= 67; j++)
//        		for (int j=10; j<=30; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else {
                    basicpremiumrate = 0;
                }

            } else if (planoption.equalsIgnoreCase("Increasing Cover") && premiumPayoption.equalsIgnoreCase("LPPT") && (gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Third Gender") || gender.equalsIgnoreCase("Third Gender")) && Smokertype.equalsIgnoreCase("Smoker") && getsumassuredslab(sumassured, premiumPayoption) == 0 && WholelifePopt.equalsIgnoreCase("Other than Whole life")) {
                double val = 0;

                if (PPT == 5)
                    val = 0;
                else if (PPT == 7)
                    val = 1;
                else if (PPT == 10)
                    val = 2;
                else if (PPT == 15)
                    val = 3;
                else if (PPT == 20)
                    val = 4;
                else if (PPT == 25)
                    val = 5;
                else
                    val = 6;

                if (val == 6) {
                    double[] PWBarr = eshieldNextDBICRates.getIC_Male_Smoker_slab1_PL();
                    int position = 0;
                    for (int i = 18; i <= 65; i++) {
                        for (int j = 10; j <= 67; j++)
//        		for (int j=10; j<=30; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else {
                    basicpremiumrate = 0;
                }

            } else if (planoption.equalsIgnoreCase("Increasing Cover") && premiumPayoption.equalsIgnoreCase("LPPT") && gender.equalsIgnoreCase("Female") && Smokertype.equalsIgnoreCase("Non Smoker") && getsumassuredslab(sumassured, premiumPayoption) == 0 && WholelifePopt.equalsIgnoreCase("Other than Whole life")) {
                double val = 0;
                if ((age) == 18 || (age) == 19 || (age) == 20 || (age) == 21) {
                    age = 18;
                } else {
                    age = (age - 3);
                }

                if (PPT == 5)
                    val = 0;
                else if (PPT == 7)
                    val = 1;
                else if (PPT == 10)
                    val = 2;
                else if (PPT == 15)
                    val = 3;
                else if (PPT == 20)
                    val = 4;
                else if (PPT == 25)
                    val = 5;
                else
                    val = 6;

                double[] PWBarr = eshieldNextDBICRates.getIC_Female_Nonsmoker_slab1_PL();
                int position = 0;
                if (val == 6) {
                    for (int i = 18; i <= 65; i++) {
//        	for (int j=10; j<=getPolicyTermMax(planoption); j++)
                        for (int j = 10; j <= 67; j++)
//        		for (int j=10; j<=30; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else {
                    basicpremiumrate = 0;
                }

            } else if (planoption.equalsIgnoreCase("Increasing Cover") && premiumPayoption.equalsIgnoreCase("LPPT") && gender.equalsIgnoreCase("Female") && Smokertype.equalsIgnoreCase("Smoker") && getsumassuredslab(sumassured, premiumPayoption) == 0 && WholelifePopt.equalsIgnoreCase("Other than Whole life")) {
                double val = 0;

                if (PPT == 5)
                    val = 0;
                else if (PPT == 7)
                    val = 1;
                else if (PPT == 10)
                    val = 2;
                else if (PPT == 15)
                    val = 3;
                else if (PPT == 20)
                    val = 4;
                else if (PPT == 25)
                    val = 5;
                else
                    val = 6;

                double[] PWBarr = eshieldNextDBICRates.getIC_Female_Smoker_slab1_PL();
                int position = 0;
                if (val == 6) {
                    for (int i = 18; i <= 65; i++) {
                        for (int j = 10; j <= 67; j++)
//        		for (int j=10; j<=30; j++)
                        {
                            if ((age) == i && (policyterm) == j) {
                                basicpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else {
                    basicpremiumrate = 0;
                }

            }

            //Whole Life
            else if (planoption.equalsIgnoreCase("Increasing Cover") && premiumPayoption.equalsIgnoreCase("LPPT") && (gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Third Gender") || gender.equalsIgnoreCase("Third Gender")) && Smokertype.equalsIgnoreCase("Non Smoker") && getsumassuredslab(sumassured, premiumPayoption) == 0 && WholelifePopt.equalsIgnoreCase("Whole life")) {
                double col = 0;

                double[] PWBarr = eshieldNextDB_WLRates.getIC_Male_Nonsmoker_slab1_WL(PPT);
                int position = 0;
                for (int i = 42; i <= 65; i++) {

                    if ((age) == i) {
                        basicpremiumrate = PWBarr[position];
                        break;
                    }
                    position++;
                }
            } else if (planoption.equalsIgnoreCase("Increasing Cover") && premiumPayoption.equalsIgnoreCase("LPPT") && Smokertype.equalsIgnoreCase("Non Smoker") && getsumassuredslab(sumassured, premiumPayoption) == 1 && WholelifePopt.equalsIgnoreCase("Whole life")) {
                if (gender.equalsIgnoreCase("Female")) {
                    if ((age) == 18 || (age) == 19 || (age) == 20 || (age) == 21) {
                        age = 18;
                    } else {
                        age = (age - 3);
                    }
                }

                double[] PWBarr = eshieldNextDB_WLRates.getIC_Male_Nonsmoker_slab1_WL1Crore(PPT);
                int position = 0;
                for (int i = 42; i <= 65; i++) {

                    if ((age) == i) {
                        basicpremiumrate = PWBarr[position];
                        break;
                    }
                    position++;
                }
            } else if (planoption.equalsIgnoreCase("Increasing Cover") && premiumPayoption.equalsIgnoreCase("LPPT") && (gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Third Gender") || gender.equalsIgnoreCase("Third Gender")) && Smokertype.equalsIgnoreCase("Smoker") && getsumassuredslab(sumassured, premiumPayoption) == 0 && WholelifePopt.equalsIgnoreCase("Whole life")) {
                double[] PWBarr = eshieldNextDB_WLRates.getIC_Male_Smoker_slab1_WL(PPT);
                int position = 0;
                for (int i = 42; i <= 65; i++) {

                    if ((age) == i) {
                        basicpremiumrate = PWBarr[position];
                        break;
                    }
                    position++;

                }


            } else if (planoption.equalsIgnoreCase("Increasing Cover") && premiumPayoption.equalsIgnoreCase("LPPT") && gender.equalsIgnoreCase("Female") && Smokertype.equalsIgnoreCase("Smoker") && getsumassuredslab(sumassured, premiumPayoption) == 0 && WholelifePopt.equalsIgnoreCase("Whole life")) {
                double[] PWBarr = eshieldNextDB_WLRates.getIC_Female_smoker_slab1_WL(PPT);
                int position = 0;

                for (int i = 45; i <= 65; i++) {

                    if ((age) == i) {
                        basicpremiumrate = PWBarr[position];
                        break;
                    }
                    position++;

                }


            } else if (planoption.equalsIgnoreCase("Increasing Cover") && premiumPayoption.equalsIgnoreCase("LPPT") && gender.equalsIgnoreCase("Female") && Smokertype.equalsIgnoreCase("Non Smoker") && getsumassuredslab(sumassured, premiumPayoption) == 0 && WholelifePopt.equalsIgnoreCase("Whole life")) {
                if ((age) == 18 || (age) == 19 || (age) == 20 || (age) == 21) {
                    age = 18;
                } else {
                    age = (age - 3);
                }
                double[] PWBarr = eshieldNextDB_WLRates.getIC_Male_Nonsmoker_slab1_WL(PPT);
                int position = 0;

                for (int i = 42; i <= 65; i++) {

                    if ((age) == i) {
                        basicpremiumrate = PWBarr[position];
                        break;
                    }
                    position++;

                }
            }

            //ADD Underwriting here for medical

//		if(planoption.equalsIgnoreCase("Medical"))
//		{
            else if (planoption.equalsIgnoreCase("Increasing Cover") && premiumPayoption.equalsIgnoreCase("Regular") && Smokertype.equalsIgnoreCase("Non Smoker") &&
                    (getsumassuredslab(sumassured, premiumPayoption) == 1
                            || getsumassuredslab(sumassured, premiumPayoption) == 2
                            || getsumassuredslab(sumassured, premiumPayoption) == 3)
                    && WholelifePopt.equalsIgnoreCase("Other than Whole life")) {
                if (gender.equalsIgnoreCase("Female")) {
                    if ((age) == 18 || (age) == 19 || (age) == 20 || (age) == 21) {
                        age = 18;
                    } else {
                        age = (age - 3);
                    }
                }
                double[] PWBarr = eshieldNextDBICRates.getIC_Male_Nonsmoker_slab3Regular_1Crore();
                int position = 0;
                for (int i = 18; i <= 65; i++) {
                    for (int j = 10; j <= 67; j++)
//        		for (int j=10; j<=30; j++)
                    {
                        if ((age) == i && (policyterm) == j) {
                            basicpremiumrate = PWBarr[position];
                            break;
                        }
                        position++;
                    }
                }


            } else if (planoption.equalsIgnoreCase("Increasing Cover") && premiumPayoption.equalsIgnoreCase("Single") && Smokertype.equalsIgnoreCase("Non Smoker") &&
                    (getsumassuredslab(sumassured, premiumPayoption) == 1 ||
                            getsumassuredslab(sumassured, premiumPayoption) == 2 ||
                            getsumassuredslab(sumassured, premiumPayoption) == 3) && WholelifePopt.equalsIgnoreCase("Other than Whole life")) {
                if (gender.equalsIgnoreCase("Female")) {
                    if ((age) == 18 || (age) == 19 || (age) == 20 || (age) == 21) {
                        age = 18;
                    } else {
                        age = (age - 3);
                    }
                }
                double[] PWBarr = eshieldNextDBICRates.getIC_Male_Nonsmoker_slab3Single_1Crore();
                int position = 0;
                for (int i = 18; i <= 65; i++) {
                    for (int j = 10; j <= 67; j++)
//	        		for (int j=10; j<=30; j++)
                    {
                        if ((age) == i && (policyterm) == j) {
                            basicpremiumrate = PWBarr[position];
                            break;
                        }
                        position++;
                    }
                }
            } else if (planoption.equalsIgnoreCase("Increasing Cover") && PPT == 5 && premiumPayoption.equalsIgnoreCase("LPPT") &&
                    Smokertype.equalsIgnoreCase("Non Smoker") && getsumassuredslab(sumassured, premiumPayoption) == 1 &&
                    WholelifePopt.equalsIgnoreCase("Other than Whole life")) {
                if (gender.equalsIgnoreCase("Female")) {
                    if ((age) == 18 || (age) == 19 || (age) == 20 || (age) == 21) {
                        age = 18;
                    } else {
                        age = (age - 3);
                    }
                }
                //System.out.println("planoption "+planoption);
                double[] PWBarr = null;
                int position = 0;

                //	System.out.println("planoption "+planoption);

                PWBarr = eshieldNextDBICRates.getIC_Male_Nonsmoker_slab2_LPPT5();
//        if(eshieldNextBean.getPremiumPayoption().equalsIgnoreCase("7"))
//        	PWBarr=eshieldNextDBICRates.getIC_Male_Nonsmoker_slab2_LPPT7();
//        if(eshieldNextBean.getPremiumPayoption().equalsIgnoreCase("10"))
//        	PWBarr=eshieldNextDBICRates.getIC_Male_Nonsmoker_slab2_LPPT10();
//
//
                for (int i = 18; i <= 65; i++) {
                    for (int j = 10; j <= 67; j++)
//        		for (int j=10; j<=30; j++)
                    {
                        if ((age) == i && (policyterm) == j) {
                            basicpremiumrate = PWBarr[position];
                            break;
                        }
                        position++;
                    }
                }


            } else if (planoption.equalsIgnoreCase("Increasing Cover") && PPT == 7 && premiumPayoption.equalsIgnoreCase("LPPT") && Smokertype.equalsIgnoreCase("Non Smoker") && getsumassuredslab(sumassured, premiumPayoption) == 1 && WholelifePopt.equalsIgnoreCase("Other than Whole life")) {
                if (gender.equalsIgnoreCase("Female")) {
                    if ((age) == 18 || (age) == 19 || (age) == 20 || (age) == 21) {
                        age = 18;
                    } else {
                        age = (age - 3);
                    }
                }
                double[] PWBarr = null;
                //=eshieldNextDB.getIC_Female_nonsmoker_slab3();
                int position = 0;

                PWBarr = eshieldNextDBICRates.getIC_Male_Nonsmoker_slab2_LPPT7();
                for (int i = 18; i <= 65; i++) {
                    for (int j = 10; j <= 67; j++)
//        		for (int j=10; j<=30; j++)
                    {
                        if ((age) == i && (policyterm) == j) {
                            basicpremiumrate = PWBarr[position];
                            break;
                        }
                        position++;
                    }
                }


            } else if (planoption.equalsIgnoreCase("Increasing Cover") && PPT == 10 && premiumPayoption.equalsIgnoreCase("LPPT") && Smokertype.equalsIgnoreCase("Non Smoker") && getsumassuredslab(sumassured, premiumPayoption) == 1 && WholelifePopt.equalsIgnoreCase("Other than Whole life")) {
                if (gender.equalsIgnoreCase("Female")) {
                    if ((age) == 18 || (age) == 19 || (age) == 20 || (age) == 21) {
                        age = 18;
                    } else {
                        age = (age - 3);
                    }
                }
                double[] PWBarr = null;
                //=eshieldNextDB.getIC_Female_nonsmoker_slab3();
                int position = 0;
//        if(PPT==10)
                PWBarr = eshieldNextDBICRates.getIC_Male_Nonsmoker_slab2_LPPT10();
                for (int i = 18; i <= 65; i++) {
                    for (int j = 15; j <= 67; j++)
//        		for (int j=10; j<=30; j++)
                    {
                        if ((age) == i && (policyterm) == j) {
                            basicpremiumrate = PWBarr[position];
                            break;
                        }
                        position++;
                    }
                }


            } else if (planoption.equalsIgnoreCase("Increasing Cover") && PPT == 15 && premiumPayoption.equalsIgnoreCase("LPPT") && Smokertype.equalsIgnoreCase("Non Smoker") && getsumassuredslab(sumassured, premiumPayoption) == 1 && WholelifePopt.equalsIgnoreCase("Other than Whole life")) {
                if (gender.equalsIgnoreCase("Female")) {
                    if ((age) == 18 || (age) == 19 || (age) == 20 || (age) == 21) {
                        age = 18;
                    } else {
                        age = (age - 3);
                    }
                }
                double[] PWBarr = null;
                //=eshieldNextDB.getIC_Female_nonsmoker_slab3();
                int position = 0;
//        if(PPT==15)
                PWBarr = eshieldNextDBICRates.getIC_Male_Nonsmoker_slab2_LPPT15();
                for (int i = 18; i <= 65; i++) {
                    for (int j = 20; j <= 67; j++)
//        		for (int j=10; j<=30; j++)
                    {
                        if ((age) == i && (policyterm) == j) {
                            basicpremiumrate = PWBarr[position];
                            break;
                        }
                        position++;
                    }
                }


            } else if (planoption.equalsIgnoreCase("Increasing Cover") && PPT == 20 && premiumPayoption.equalsIgnoreCase("LPPT") && Smokertype.equalsIgnoreCase("Non Smoker") && getsumassuredslab(sumassured, premiumPayoption) == 1 && WholelifePopt.equalsIgnoreCase("Other than Whole life")) {
                if (gender.equalsIgnoreCase("Female")) {
                    if ((age) == 18 || (age) == 19 || (age) == 20 || (age) == 21) {
                        age = 18;
                    } else {
                        age = (age - 3);
                    }
                }
                double[] PWBarr = null;
                //=eshieldNextDB.getIC_Female_nonsmoker_slab3();
                int position = 0;
//        if(PPT==20)
                PWBarr = eshieldNextDBICRates.getIC_Male_Nonsmoker_slab2_LPPT20();

                for (int i = 18; i <= 60; i++) {
                    for (int j = 25; j <= 67; j++)
//        		for (int j=10; j<=30; j++)
                    {
                        if ((age) == i && (policyterm) == j) {
                            basicpremiumrate = PWBarr[position];
                            break;
                        }
                        position++;
                    }
                }


            } else if (planoption.equalsIgnoreCase("Increasing Cover") && PPT == 25 && premiumPayoption.equalsIgnoreCase("LPPT") && Smokertype.equalsIgnoreCase("Non Smoker") && getsumassuredslab(sumassured, premiumPayoption) == 1 && WholelifePopt.equalsIgnoreCase("Other than Whole life")) {
                if (gender.equalsIgnoreCase("Female")) {
                    if ((age) == 18 || (age) == 19 || (age) == 20 || (age) == 21) {
                        age = 18;
                    } else {
                        age = (age - 3);
                    }
                }
                double[] PWBarr = null;
                //=eshieldNextDB.getIC_Female_nonsmoker_slab3();
                int position = 0;
//      if(PPT==25)
                PWBarr = eshieldNextDBICRates.getIC_Male_Nonsmoker_slab2_LPPT25();
                for (int i = 18; i <= 55; i++) {
                    for (int j = 30; j <= 67; j++)
//        		for (int j=10; j<=30; j++)
                    {
                        if ((age) == i && (policyterm) == j) {
                            basicpremiumrate = PWBarr[position];
                            break;
                        }
                        position++;
                    }
                }


            } else if (planoption.equalsIgnoreCase("Increasing Cover") && premiumPayoption.equalsIgnoreCase("LPPT") && Smokertype.equalsIgnoreCase("Non Smoker") && getsumassuredslab(sumassured, premiumPayoption) == 1 && WholelifePopt.equalsIgnoreCase("Other than Whole life")) {
                if (gender.equalsIgnoreCase("Female")) {
                    if ((age) == 18 || (age) == 19 || (age) == 20 || (age) == 21) {
                        age = 18;
                    } else {
                        age = (age - 3);
                    }
                }
                double[] PWBarr = eshieldNextDBICRates.getIC_Male_Nonsmoker_slab2PT_1crore();
                int position = 0;
                for (int i = 18; i <= 65; i++) {
                    for (int j = 10; j <= 67; j++)
//        		for (int j=10; j<=30; j++)
                    {
                        if ((age) == i && (policyterm) == j) {
                            basicpremiumrate = PWBarr[position];
                            break;
                        }
                        position++;
                    }
                }
            } else if (planoption.equalsIgnoreCase("Increasing Cover") && premiumPayoption.equalsIgnoreCase("LPPT") && gender.equalsIgnoreCase("Female") && Smokertype.equalsIgnoreCase("Non Smoker") && getsumassuredslab(sumassured, premiumPayoption) == 1 && WholelifePopt.equalsIgnoreCase("Whole life")) {
                if ((age) == 18 || (age) == 19 || (age) == 20 || (age) == 21) {
                    age = 18;
                } else {
                    age = (age - 3);
                }

                double[] PWBarr = eshieldNextDBICRates.getIC_Male_Nonsmoker_slab2PT_WL1crore();
                int position = 0;
                for (int i = 42; i <= 65; i++) {

                    if ((age) == i) {
                        basicpremiumrate = PWBarr[position];
                        break;
                    }
                    position++;

                }
            }

        }


        //	return basicpremiumrate;

        return basicpremiumrate;


    }


    public double getbasicpremium(String premfreq, double sumassured, String basicpremiumrate) {
        double val = 0;

        if (premfreq.equalsIgnoreCase("Half-Yearly")) {
            val = 0.51;
        } else if (premfreq.equalsIgnoreCase("Quarterly")) {
            val = 0.26;
        } else if (premfreq.equalsIgnoreCase("Monthly")) {
            val = 0.085;
        } else {
            val = 1;
        }
//		System.out.println("basicpremiumrate "+basicpremiumrate);
//		System.out.println("sumassured "+sumassured);
//		System.out.println("val "+val);
        basicpremium = Double.parseDouble(basicpremiumrate) * (sumassured / 1000) * val;
//		basicpremium = Double.parseDouble(basicpremiumrate) * val;
        return basicpremium;

    }


    public double getBetterHalfBenefitPtemiumRate(String gender, String planoption, String premiumPayoption, String Smokertype, double sumassured, int age, int policyterm, int PPT, String WholelifePopt) {
        //Medical && Better Half Benefit
        double polyterm = 0;
        polyterm = Math.min((60 - age), policyterm);

        if (premiumPayoption.equalsIgnoreCase("Regular") && gender.equalsIgnoreCase("Female") && Smokertype.equalsIgnoreCase("Non Smoker")) {
            double[] PWBarr = eshieldNextDBICRates.getBHB_Female_Nonsmoker_slab0();
            int position = 0;
//	        if((age)==18 || (age)==19 ||(age)==20 || (age)==21 )
//			{
//			age =18;
//			}
//			else
//			{
//			age	=(age-3);
//			}
            for (int i = 18; i <= 55; i++) {
                for (int j = 5; j <= 42; j++)
//	        		for (int j=10; j<=30; j++)
                {
                    if ((age) == i && (polyterm) == j) {
                        basicBetterhalfpremiumrate = PWBarr[position];
                        break;
                    }
                    position++;
                }
            }
        } else if (premiumPayoption.equalsIgnoreCase("Regular") && gender.equalsIgnoreCase("Female") && Smokertype.equalsIgnoreCase("Smoker")) {

            double[] PWBarr = eshieldNextDBICRates.getBHB_Female_Smoker_slab0();
            int position = 0;
            for (int i = 18; i <= 55; i++) {
                for (int j = 5; j <= 42; j++)
                //       		for (int j=10; j<=30; j++)
                {
                    if ((age) == i && (polyterm) == j) {
                        basicBetterhalfpremiumrate = PWBarr[position];
                        break;
                    }
                    position++;
                }
            }
        } else if (premiumPayoption.equalsIgnoreCase("Regular") && (gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Third Gender") || gender.equalsIgnoreCase("Third Gender")) && Smokertype.equalsIgnoreCase("Non Smoker")) {
            double[] PWBarr = eshieldNextDBICRates.getBHB_Male_Nonsmoker_slab0();
            int position = 0;
            for (int i = 18; i <= 55; i++) {
                for (int j = 5; j <= 42; j++)
                //       		for (int j=10; j<=30; j++)
                {
                    if ((age) == i && (polyterm) == j) {
                        basicBetterhalfpremiumrate = PWBarr[position];
                        //  System.out.println("basicBetterhalfpremiumrate " +basicBetterhalfpremiumrate);
                        break;
                    }
                    position++;
                }

            }

        } else if (premiumPayoption.equalsIgnoreCase("Regular") && (gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Third Gender") || gender.equalsIgnoreCase("Third Gender")) && Smokertype.equalsIgnoreCase("Smoker")) {
            double[] PWBarr = eshieldNextDBICRates.getBHB_Male_Smoker_slab0();
            int position = 0;
            for (int i = 18; i <= 55; i++) {
                for (int j = 5; j <= 42; j++)
//       		for (int j=10; j<=30; j++)
                {
                    if ((age) == i && (polyterm) == j) {
                        basicBetterhalfpremiumrate = PWBarr[position];
                        break;
                    }
                    position++;
                }
            }
        } else if (premiumPayoption.equalsIgnoreCase("Single") && gender.equalsIgnoreCase("Female") && Smokertype.equalsIgnoreCase("Non Smoker")) {
            double[] PWBarr = eshieldNextDBICRates.getBHB_Female_NonsmokerSingle_slab0();
            int position = 0;
//       if((age)==18 || (age)==19 ||(age)==20 || (age)==21 )
//		{
//		age =18;
//		}
//		else
//		{
//		age	=(age-3);
//		}
            for (int i = 18; i <= 55; i++) {
                for (int j = 5; j <= 42; j++)
//       		for (int j=10; j<=30; j++)
                {
                    if ((age) == i && (polyterm) == j) {
                        basicBetterhalfpremiumrate = PWBarr[position];
                        break;
                    }
                    position++;
                }
            }
        } else if (premiumPayoption.equalsIgnoreCase("Single") && gender.equalsIgnoreCase("Female") && Smokertype.equalsIgnoreCase("Smoker")) {
            double[] PWBarr = eshieldNextDBICRates.getBHB_Female_SmokerSingle_slab0();
            int position = 0;
            for (int i = 18; i <= 55; i++) {
                for (int j = 5; j <= 42; j++)
//       		for (int j=10; j<=30; j++)
                {
                    if ((age) == i && (polyterm) == j) {
                        basicBetterhalfpremiumrate = PWBarr[position];
                        break;
                    }
                    position++;
                }
            }
        } else if (premiumPayoption.equalsIgnoreCase("Single") && (gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Third Gender") || gender.equalsIgnoreCase("Third Gender")) && Smokertype.equalsIgnoreCase("Non Smoker")) {
            double[] PWBarr = eshieldNextDBICRates.getBHB_Male_NonsmokerSingle_slab0();
            int position = 0;
            for (int i = 18; i <= 55; i++) {
                for (int j = 5; j <= 42; j++)
//       		for (int j=10; j<=30; j++)
                {
                    if ((age) == i && (polyterm) == j) {
                        basicBetterhalfpremiumrate = PWBarr[position];
                        break;
                    }
                    position++;
                }
            }
        } else if (premiumPayoption.equalsIgnoreCase("Single") && (gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Third Gender") || gender.equalsIgnoreCase("Third Gender")) && Smokertype.equalsIgnoreCase("Smoker")) {
            double[] PWBarr = eshieldNextDBICRates.getBHB_Male_SmokerSingle_slab0();
            int position = 0;
            for (int i = 18; i <= 55; i++) {
                for (int j = 5; j <= 42; j++)
//       		for (int j=10; j<=30; j++)
                {
                    if ((age) == i && (polyterm) == j) {
                        basicBetterhalfpremiumrate = PWBarr[position];
                        break;
                    }
                    position++;
                }
            }
        } else if (premiumPayoption.equalsIgnoreCase("LPPT") && gender.equalsIgnoreCase("Female") && Smokertype.equalsIgnoreCase("Non Smoker")) {
            double[] PWBarr = null;
            int position = 0;
//       if((age)==18 || (age)==19 ||(age)==20 || (age)==21 )
//		{
//		age =18;
//		}
//		else
//		{
//		age	=(age-3);
//		}

            if (PPT == 5)
                PWBarr = eshieldNextDBICRates.getBHB_Female_Nonsmoker_slab1_LPPT5();
            else if (PPT == 7)
                PWBarr = eshieldNextDBICRates.getBHB_Female_Nonsmoker_slab1_LPPT7();
            else if (PPT == 10)
                PWBarr = eshieldNextDBICRates.getBHB_Female_Nonsmoker_slab1_LPPT10();
            else if (PPT == 15)
                PWBarr = eshieldNextDBICRates.getBHB_Female_Nonsmoker_slab1_LPPT15();
            else if (PPT == 20)
                PWBarr = eshieldNextDBICRates.getBHB_Female_Nonsmoker_slab1_LPPT20();
            else if (PPT == 25)
                PWBarr = eshieldNextDBICRates.getBHB_Female_Nonsmoker_slab1_LPPT25();
            else
                PWBarr = eshieldNextDBICRates.getBHB_Female_NonsmokerPT_slab0LPPT();
            for (int i = 18; i <= 55; i++) {
                for (int j = 5; j <= 42; j++)
//       		for (int j=10; j<=30; j++)
                {
                    if ((age) == i && (polyterm) == j) {
                        basicBetterhalfpremiumrate = PWBarr[position];
                        break;
                    }
                    position++;
                }
            }
        } else if (premiumPayoption.equalsIgnoreCase("LPPT") && gender.equalsIgnoreCase("Female") && Smokertype.equalsIgnoreCase("Smoker")) {
            double[] PWBarr = null;
            int position = 0;

            if (PPT == 5)
                PWBarr = eshieldNextDBICRates.getBHB_Female_Smoker_slab1_LPPT5();
            else if (PPT == 7)
                PWBarr = eshieldNextDBICRates.getBHB_Female_Smoker_slab1_LPPT7();
            else if (PPT == 10)
                PWBarr = eshieldNextDBICRates.getBHB_Female_Smoker_slab1_LPPT10();
            else if (PPT == 15)
                PWBarr = eshieldNextDBICRates.getBHB_Female_Smoker_slab1_LPPT15();
            else if (PPT == 20)
                PWBarr = eshieldNextDBICRates.getBHB_Female_Smoker_slab1_LPPT20();
            else if (PPT == 25)
                PWBarr = eshieldNextDBICRates.getBHB_Female_Smoker_slab1_LPPT25();
            else
                PWBarr = eshieldNextDBICRates.getBHB_Female_SmokerPT_slab0();
            for (int i = 18; i <= 55; i++) {
                for (int j = 5; j <= 42; j++)
//       		for (int j=10; j<=30; j++)
                {
                    if ((age) == i && (polyterm) == j) {
                        basicBetterhalfpremiumrate = PWBarr[position];
                        break;
                    }
                    position++;
                }
            }
        } else if (premiumPayoption.equalsIgnoreCase("LPPT") && (gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Third Gender") || gender.equalsIgnoreCase("Third Gender")) && Smokertype.equalsIgnoreCase("Non Smoker")) {
            double[] PWBarr = null;
            int position = 0;

            if (PPT == 5)
                PWBarr = eshieldNextDBICRates.getBHB_Male_Nonsmoker_slab1_LPPT5();
            else if (PPT == 7)
                PWBarr = eshieldNextDBICRates.getBHB_Male_Nonsmoker_slab1_LPPT7();
            else if (PPT == 10)
                PWBarr = eshieldNextDBICRates.getBHB_Male_Nonsmoker_slab1_LPPT10();
            else if (PPT == 15)
                PWBarr = eshieldNextDBICRates.getBHB_Male_Nonsmoker_slab1_LPPT15();
            else if (PPT == 20)
                PWBarr = eshieldNextDBICRates.getBHB_Male_Nonsmoker_slab1_LPPT20();
            else if (PPT == 25)
                PWBarr = eshieldNextDBICRates.getBHB_Male_Nonsmoker_slab1_LPPT25();
            else
                PWBarr = eshieldNextDBICRates.getBHB_Male_Nonsmoker_slab1_LPPT30();

            for (int i = 18; i <= 55; i++) {
                for (int j = 5; j <= 42; j++)
//       		for (int j=10; j<=30; j++)
                {
                    if ((age) == i && (polyterm) == j) {
                        basicBetterhalfpremiumrate = PWBarr[position];
                        break;
                    }
                    position++;
                }
            }
        } else if (premiumPayoption.equalsIgnoreCase("LPPT") && (gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Third Gender") || gender.equalsIgnoreCase("Third Gender")) && Smokertype.equalsIgnoreCase("Smoker")) {
            double[] PWBarr = null;
            int position = 0;


            if (PPT == 5)
                PWBarr = eshieldNextDBICRates.getBHB_Male_Smoker_slab1_LPPT5();
            else if (PPT == 7)
                PWBarr = eshieldNextDBICRates.getBHB_Male_Smoker_slab1_LPPT7();
            else if (PPT == 10)
                PWBarr = eshieldNextDBICRates.getBHB_Male_Smoker_slab1_LPPT10();
            else if (PPT == 15)
                PWBarr = eshieldNextDBICRates.getBHB_Male_Smoker_slab1_LPPT15();
            else if (PPT == 20)
                PWBarr = eshieldNextDBICRates.getBHB_Male_Smoker_slab1_LPPT20();
            else if (PPT == 25)
                PWBarr = eshieldNextDBICRates.getBHB_Male_Smoker_slab1_LPPT25();
            else
                PWBarr = eshieldNextDBICRates.getBHB_Male_Smoker_slab1_LPPT30();

            for (int i = 18; i <= 55; i++) {
                for (int j = 5; j <= 42; j++)
//       		for (int j=10; j<=30; j++)
                {
                    if ((age) == i && (polyterm) == j) {
                        basicBetterhalfpremiumrate = PWBarr[position];
                        break;
                    }
                    position++;
                }
            }
        } else
            //policy term less 5 better half benefit
            if (premiumPayoption.equalsIgnoreCase("LPPT") && gender.equalsIgnoreCase("Female") && Smokertype.equalsIgnoreCase("Non Smoker")) {
                double val = 0;
                if ((age) == 18 || (age) == 19 || (age) == 20 || (age) == 21) {
                    age = 18;
                } else {
                    age = (age - 3);
                }
                if (PPT == 5)
                    val = 0;
                else if (PPT == 7)
                    val = 1;
                else if (PPT == 10)
                    val = 2;
                else if (PPT == 15)
                    val = 3;
                else if (PPT == 20)
                    val = 4;
                else if (PPT == 25)
                    val = 5;
                else
                    val = 6;
                if (val == 6) {
                    double[] PWBarr = eshieldNextDBICRates.getBHB_Female_NonsmokerPT_slab0LPPT();
                    int position = 0;
                    for (int i = 18; i <= 55; i++) {
                        for (int j = 5; j <= 42; j++)
                        //       		for (int j=10; j<=30; j++)
                        {
                            if ((age) == i && (polyterm) == j) {
                                basicBetterhalfpremiumrate = PWBarr[position];
                                break;
                            }
                            position++;
                        }
                    }
                } else {
                    basicBetterhalfpremiumrate = 0;
                }
            } else if (premiumPayoption.equalsIgnoreCase("Policy Term less 5") && gender.equalsIgnoreCase("Female") && Smokertype.equalsIgnoreCase("Smoker")) {
                double[] PWBarr = eshieldNextDBICRates.getBHB_Female_SmokerPT_slab0();
                int position = 0;
                for (int i = 18; i <= 55; i++) {
                    for (int j = 5; j <= 42; j++)
//       		for (int j=10; j<=30; j++)
                    {
                        if ((age) == i && (polyterm) == j) {
                            basicBetterhalfpremiumrate = PWBarr[position];
                            break;
                        }
                        position++;
                    }
                }
            } else if (premiumPayoption.equalsIgnoreCase("Policy Term less 5") && (gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Third Gender") || gender.equalsIgnoreCase("Third Gender")) && Smokertype.equalsIgnoreCase("Non Smoker")) {
                double[] PWBarr = eshieldNextDBICRates.getBHB_Male_SmokerPT_slab0();
                int position = 0;
                for (int i = 18; i <= 55; i++) {
                    for (int j = 5; j <= 42; j++)
//       		for (int j=10; j<=30; j++)
                    {
                        if ((age) == i && (polyterm) == j) {
                            basicBetterhalfpremiumrate = PWBarr[position];
                            break;
                        }
                        position++;
                    }
                }
            } else if (premiumPayoption.equalsIgnoreCase("Policy Term less 5") && (gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Third Gender") || gender.equalsIgnoreCase("Third Gender")) && Smokertype.equalsIgnoreCase("Smoker")) {
                double[] PWBarr = eshieldNextDBICRates.getBHB_Male_SmokerPT_slab0();
                int position = 0;
                for (int i = 18; i <= 55; i++) {
                    for (int j = 5; j <= 42; j++)
//       		for (int j=10; j<=30; j++)
                    {
                        if ((age) == i && (polyterm) == j) {
                            basicBetterhalfpremiumrate = PWBarr[position];
                            break;
                        }
                        position++;
                    }
                }
            }

        //System.out.println("basicBetterhalfpremiumrate "+basicBetterhalfpremiumrate);
        return basicBetterhalfpremiumrate;
    }

    public double getBetterHalfBenefitPtemium(String premfreq, double sumassured, String basicBetterhalfpremiumrate) {
        double val = 0;

//		if(eshieldNextBean.getBetterHalfBenifit()==true)
//		{
        if (premfreq.equalsIgnoreCase("Half-Yearly")) {
            val = 0.51;
        } else if (premfreq.equalsIgnoreCase("Monthly")) {
            val = 0.085;
        } else {
            val = 1;
        }
//		System.out.println("basicBetterhalfpremiumrate "+basicBetterhalfpremiumrate);
//		System.out.println("sumassured "+sumassured);
//		System.out.println("val "+val);
        basicBetterhalfpremium = Double.parseDouble(basicBetterhalfpremiumrate) * val;
//		basicBetterhalfpremiumrate = Double.parseDouble(basicpremiumrate) * val;
//		}
//		else
//		{
//			basicBetterhalfpremium=0;
//		}
        //System.out.println("basicBetterhalfpremium "+basicBetterhalfpremium);
        return basicBetterhalfpremium;
    }


    public double getLevelbasicpremium(String premfreq, double sumassured, String basicpremiumrateLevel) {
        double val = 0;

        if (premfreq.equalsIgnoreCase("Half-Yearly")) {
            val = 0.51;
        } else if (premfreq.equalsIgnoreCase("Quarterly")) {
            val = 0.26;
        } else if (premfreq.equalsIgnoreCase("Monthly")) {
            val = 0.085;
        } else {
            val = 1;
        }

        //levelbasicpremium =Double.parseDouble(basicpremiumrateLevel) * (sumassured/1000) * val;
        levelbasicpremium = Double.parseDouble(basicpremiumrateLevel) * (sumassured / 1000) * val;
        return levelbasicpremium;

    }

    public double getIncreasebasicpremium(String premfreq, double sumassured, String basicpremiumrateIncrease) {
        double val = 0;

        if (premfreq.equalsIgnoreCase("Half-Yearly")) {
            val = 0.51;
        } else if (premfreq.equalsIgnoreCase("Quarterly")) {
            val = 0.26;
        } else if (premfreq.equalsIgnoreCase("Monthly")) {
            val = 0.085;
        } else {
            val = 1;
        }

        increasebasicpremium = Double.parseDouble(basicpremiumrateIncrease) * (sumassured / 1000) * val;
        increasebasicpremium = Double.parseDouble(basicpremiumrateIncrease) * (sumassured / 1000) * val;
        return increasebasicpremium;

    }

    //Added by Roshani Yadav //
    public String getAdbMaturity() {
        if (eshieldNextBean.getAge() + eshieldNextBean.getPolicyTerm_ADB() <= 75)
            return "Eligible";
        else
            return "Not Eligible";
    }

    public String getAtpdbMaturity() {
        if (eshieldNextBean.getAge() + eshieldNextBean.getPolicyTerm_ATPDB() <= 75)
            return "Eligible";
        else
            return "Not Eligible";
    }

    public double getADBFinalPremRate(String premPayOption, double policyTerm, double accidentDeathBen, int PPT) {

        if (getAdbMaturity() == "Eligible") {

            if (premPayOption.equalsIgnoreCase("Single")) {
                double[] PWBarr = eshieldNextDBICRates.getRPSP_ADB();
                int position = 0;
                for (int i = 5; i <= 57; i++) {

                    if ((policyTerm) == i) {
                        accidentDeathBen = PWBarr[position];
                        break;
                    }
                    position++;

                }
            } else if (premPayOption.equalsIgnoreCase("LPPT")) {
                int ppt = PPT == 6 ? (eshieldNextBean.getPolicyterm() - 5) : PPT;
                double[] PWBarr = eshieldNextDBICRates.getLP_ADB();
                int position = 0;
                outerloop:
                for (int i = 5; i <= 56; i++) {
                    for (int j = 6; j <= 57; j++) {
                        if ((policyTerm) == j && ppt == i) {
                            accidentDeathBen = PWBarr[position];
                            break outerloop;
                        }
                        position++;
                    }
                }
                System.out.println("accidentDeathBenADB " + accidentDeathBen);
            } else
                accidentDeathBen = 50;
        } else {
            accidentDeathBen = 0;
        }
        return accidentDeathBen;
    }

    public double getATDBFinalPremRate(String premPayOption, double policyTerm, double accidentDeathBen, int PPT) {
        if (getAtpdbMaturity() == "Eligible") {
            if (premPayOption.equalsIgnoreCase("Single")) {
                double[] PWBarr = eshieldNextDBICRates.getRPSP_ATDB();
                int position = 0;
                for (int i = 5; i <= 57; i++) {

                    if ((policyTerm) == i) {
                        accidentDeathBen = PWBarr[position];
                        break;
                    }
                    position++;

                }
            } else if (premPayOption.equalsIgnoreCase("LPPT")) {
                int ppt = PPT == 6 ? (eshieldNextBean.getPolicyterm() - 5) : PPT;
                double[] PWBarr = eshieldNextDBICRates.getLP_ATDB();
                int position = 0;
                outerloop:
                for (int i = 5; i <= 56; i++) {
                    for (int j = 6; j <= 57; j++) {
                        if ((policyTerm) == j && ppt == i) {
                            accidentDeathBen = PWBarr[position];
                            break outerloop;
                        }
                        position++;
                    }
                }
                System.out.println("accidentDeathBenATDB " + accidentDeathBen);
            } else
                accidentDeathBen = 40;
        } else {
            accidentDeathBen = 0;
        }
        return accidentDeathBen;
    }


    //end


    public double getadbpremium(String premfreq, double adbsumassured

            , int age, int policyTerm, String PremiumPayOptn, int PPT) {

        double val = 0;
        double accidentDeathBen = 0;

        if (premfreq.equalsIgnoreCase("Half-Yearly")) {
            val = 0.52;
        } else if (premfreq.equalsIgnoreCase("Monthly")) {
            val = 0.089;
        } else {
            val = 1;
        }
        //System.out.println("getAdbMaturity " +getAdbMaturity());
//		 if(getAdbMaturity()== "Eligible")
//		 {
//
//			 if(PremiumPayOptn.equalsIgnoreCase("Single"))
//				{
//					double [] PWBarr=eshieldNextDBICRates.getRPSP_ADB();
//				       int position=0;
//				       for (int i=5; i<=57; i++)
//				       {
//
//				               if((policyTerm)==i)
//				               {
//				            	   accidentDeathBen= PWBarr[position];
//				               	break;
//				               }
//				               position++;
//
//				       }
//				}
//				else if(PremiumPayOptn.equalsIgnoreCase("LPPT"))
//				{
//					double [] PWBarr=eshieldNextDBICRates.getLP_ADB();
//				       int position=0;
//				       outerloop:
//				       for (int i=5; i<=56; i++)
//				       {
//				    	   for(int j=6; j<=57; j++)
//				    	   {
//				               if((policyTerm)==j && PPT==i)
//				               {
//				            	   accidentDeathBen= PWBarr[position];
//				               	break outerloop;
//				               }
//				               position++;
//				    	   }
//				       }
//				      //System.out.println("accidentDeathBen " +accidentDeathBen);
//				}else
//					accidentDeathBen=50;
//		 }
//		 else {
//			 accidentDeathBen=0;
//		 }

//		else
//		{
//			adbpremium=50;
//		}
        adbpremium = ((adbsumassured * getADBFinalPremRate(PremiumPayOptn, policyTerm, accidentDeathBen, PPT)) / 100000) * val;
//		System.out.println("adbpremium " + adbpremium);
//		System.out.println("val " +val);
//		System.out.println("accidentDeathBen " +accidentDeathBen);


        return adbpremium;

    }


    public double getatpdbpremium(String premfreq, double atpdbumassured
            , int age, int policyTerm, String PremiumPayOptn, int PPT) {
        double val = 0;
        double accidentDeathBen = 0;


        //if(isatpdb.equalsIgnoreCase("False"))
        //{
        //    return "-";
        //}
        //else
        //{accidentDeathBen=0;}


        if (premfreq.equalsIgnoreCase("Half-Yearly")) {
            val = 0.52;
        } else if (premfreq.equalsIgnoreCase("Monthly")) {
            val = 0.089;
        } else {
            val = 1;
        }
//		if(getAtpdbMaturity()== "Eligible")
//		{
//			 if(PremiumPayOptn.equalsIgnoreCase("Single"))
//				{
//					double [] PWBarr=eshieldNextDBICRates.getRPSP_ATDB();
//				       int position=0;
//				       for (int i=5; i<=57; i++)
//				       {
//
//				               if((policyTerm)==i)
//				               {
//				            	   accidentDeathBen= PWBarr[position];
//				               	break;
//				               }
//				               position++;
//
//				       }
//				}
//				else if(PremiumPayOptn.equalsIgnoreCase("LPPT"))
//				{
//					double [] PWBarr=eshieldNextDBICRates.getLP_ATDB();
//				       int position=0;
//				       outerloop:
//				       for (int i=5; i<=56; i++)
//				       {
//				    	   for(int j=6; j<=57; j++)
//				    	   {
//				               if((policyTerm)==j && PPT==i)
//				               {
//				            	   accidentDeathBen= PWBarr[position];
//				               	break outerloop;
//				               }
//				               position++;
//				    	   }
//				       }
//				       //System.out.println("accidentDeathBen " +accidentDeathBen);
//				}
//				else
//					accidentDeathBen=40;
//		}
//
//		else {
//			 accidentDeathBen=0;
//		 }
        {
            atpdbpremium = ((atpdbumassured * getATDBFinalPremRate(PremiumPayOptn, policyTerm, accidentDeathBen, PPT)) / 100000) * val;
        }
        //	System.out.println("atpdbpremium " +atpdbpremium);
        return atpdbpremium;

    }


    public double gettotalpremiumwithoutST(String basicpremium, String adbpremium, String atpdbpremium, String basicBetterhalfpremium) {
//		totalpremiumwithoutST = basicpremium + adbpremium + atpdbpremium;
        totalpremiumwithoutST = Double.parseDouble(cfap.getRoundOffLevel2New(cfap.getStringWithout_E(Double.parseDouble(basicpremium) + Double.parseDouble(adbpremium) + Double.parseDouble(atpdbpremium) + Double.parseDouble(basicBetterhalfpremium))));
//		System.out.println("totalpremiumwithoutST "+totalpremiumwithoutST);
//		System.out.println("adbpremium "+adbpremium);
//		System.out.println("atpdbpremium "+atpdbpremium);
//		System.out.println("basicBetterhalfpremium "+basicBetterhalfpremium);
        return totalpremiumwithoutST;

    }

    public String getageGroup(double PolicyTerm) {

        if (PolicyTerm >= 5 && PolicyTerm <= 12) {
            ageGroup = "5-12";
        } else {
            ageGroup = "13 and above";
        }
        return ageGroup;
    }

    public double getBasicChannelDiscount(boolean staff, String Channel, double basicprem, String premPayOption, double sumassured, double rate) {
        double channel;
        double val = 0;
//		System.out.println("Channel " +Channel);
        val = (sumassured * rate / 1000);
        System.out.println("valbasic " + val);
        System.out.println("rateChannel " + rate);
        if ((Channel.endsWith("Online"))) {
            if (premPayOption.equalsIgnoreCase("Single")) {
                if (getageGroup(eshieldNextBean.getPolicyterm()).equalsIgnoreCase("5-12")) {
                    channel = 0.02;
                } else {
                    channel = 0.03;
                }
            } else if (premPayOption.equalsIgnoreCase("Regular")) {
                channel = 0.015;
            } else {
                channel = 0.04;
            }
        } else {
            channel = 0;
        }
        basicChannelDis = val * channel;

        System.out.println("channel " + channel);
        System.out.println("basicChannelDis " + basicChannelDis);
        return basicChannelDis;
    }


    public double getbasicCover(double sumassured, double basicpremiumrate) {
        double BasicCover = 0;
        BasicCover = Double.parseDouble(cfap.getRoundOffLevel2(cfap.getStringWithout_E(sumassured * basicpremiumrate / 1000)));
        System.out.println("sumassured " + sumassured);
        System.out.println("basicpremiumrate " + basicpremiumrate);
        //System.out.println("BasicCover "+BasicCover);
        return BasicCover;
    }

    public double getStaffDisc(String premPayOption, boolean staff, double basicprem, double policyterm, double sumassured, double rate) {
        double staffval = 0;
        if (staff) {
            if (premPayOption.equalsIgnoreCase("Single")) {
                if (getageGroup(policyterm).equalsIgnoreCase("5-12")) {
                    staffval = 0.05;
                } else {
                    staffval = 0.075;
                }
            } else if (premPayOption.equalsIgnoreCase("Regular")) {
                staffval = 0.025;
            } else {
                staffval = 0.05;
            }
        } else {

            staffval = 0;
        }

        staffDisc = Double.parseDouble(cfap.getRoundOffLevel2(cfap.getStringWithout_E(getbasicCover(sumassured, rate) * staffval)));
        //	System.out.println("staffDisc " +staffDisc);
        return staffDisc;
    }


    public double getADBChannelDiscount(boolean staff, String Channel, double adbpremium, String premPayOption, double sumassured, double rate, int policyTerm, int PPT) {
        double channel, val = 0, val2 = 0, accidentDeathBen = 0;

//		if(getAdbMaturity()== "Eligible")
//		 {
//
//			 if(premPayOption.equalsIgnoreCase("Single"))
//				{
//					double [] PWBarr=eshieldNextDBICRates.getRPSP_ADB();
//				       int position=0;
//				       for (int i=5; i<=57; i++)
//				       {
//
//				               if((policyTerm)==i)
//				               {
//				            	   accidentDeathBen= PWBarr[position];
//				               	break;
//				               }
//				               position++;
//
//				       }
//				}
//				else if(premPayOption.equalsIgnoreCase("LPPT"))
//				{
//					double [] PWBarr=eshieldNextDBICRates.getLP_ADB();
//				       int position=0;
//				       outerloop:
//				       for (int i=5; i<=56; i++)
//				       {
//				    	   for(int j=6; j<=57; j++)
//				    	   {
//				               if((policyTerm)==j && PPT==i)
//				               {
//				            	   accidentDeathBen= PWBarr[position];
//				               	break outerloop;
//				               }
//				               position++;
//				    	   }
//				       }
//				      // System.out.println("accidentDeathBen " +accidentDeathBen);
//				}
//				else
//					accidentDeathBen=50;
//		 }
//		 else {
//			 accidentDeathBen=0;
//		 }

        val = (sumassured * getADBFinalPremRate(premPayOption, policyTerm, accidentDeathBen, PPT) / 100000);
        //System.out.println("val ***" +val);
        if ((Channel.endsWith("Online"))) {
            if (premPayOption.equalsIgnoreCase("Single")) {
                if (ageGroup.equalsIgnoreCase("5-12")) {
                    channel = 0.02;
                } else {
                    channel = 0.03;
                }
            } else if (premPayOption.equalsIgnoreCase("Regular")) {
                channel = 0.015;
            } else {
                channel = 0.04;
            }
        } else {
            channel = 0;
        }
        ADBChannelDis = val * channel;
        //System.out.println("ADBChannelDis*** "+ADBChannelDis);
        return ADBChannelDis;
    }

    public double getpremAfterChannelDis(String premPayOption, int policyTerm, int PPT, double sumassured) {
        double channel, val = 0, val1 = 0, accidentDeathBen = 0;

        if (getAdbMaturity() == "Eligible") {

            if (premPayOption.equalsIgnoreCase("Single")) {
                double[] PWBarr = eshieldNextDBICRates.getRPSP_ADB();
                int position = 0;
                for (int i = 5; i <= 57; i++) {

                    if ((policyTerm) == i) {
                        accidentDeathBen = PWBarr[position];
                        break;
                    }
                    position++;

                }
            } else if (premPayOption.equalsIgnoreCase("LPPT")) {
                double[] PWBarr = eshieldNextDBICRates.getLP_ADB();
                int position = 0;
                outerloop:
                for (int i = 5; i <= 56; i++) {
                    for (int j = 6; j <= 57; j++) {
                        if ((policyTerm) == j && PPT == i) {
                            accidentDeathBen = PWBarr[position];
                            break outerloop;
                        }
                        position++;
                    }
                }
                // System.out.println("accidentDeathBen " +accidentDeathBen);
            } else
                accidentDeathBen = 50;
        } else {
            accidentDeathBen = 0;
        }

        val = (sumassured * accidentDeathBen / 100000);
        //System.out.println("val ***" +val);

        val1 = val - ADBChannelDis;

        // System.out.println("val1*** " +val1);
        return val1;

    }

    public double getATPDBChannelDiscount(boolean staff, String Channel, double atpdbpremium, String premPayOption, double sumassured, double rate, int policyTerm, int PPT) {
        double channel, val = 0, accidentDeathBen = 0;
//		if(getAtpdbMaturity()== "Eligible")
//		{
//			 if(premPayOption.equalsIgnoreCase("Single"))
//				{
//					double [] PWBarr=eshieldNextDBICRates.getRPSP_ATDB();
//				       int position=0;
//				       for (int i=5; i<=57; i++)
//				       {
//
//				               if((policyTerm)==i)
//				               {
//				            	   accidentDeathBen= PWBarr[position];
//				               	break;
//				               }
//				               position++;
//
//				       }
//				}
//				else if(premPayOption.equalsIgnoreCase("LPPT"))
//				{
//					double [] PWBarr=eshieldNextDBICRates.getLP_ATDB();
//				       int position=0;
//				       outerloop:
//				       for (int i=5; i<=56; i++)
//				       {
//				    	   for(int j=6; j<=57; j++)
//				    	   {
//				               if((policyTerm)==j && PPT==i)
//				               {
//				            	   accidentDeathBen= PWBarr[position];
//				               	break outerloop;
//				               }
//				               position++;
//				    	   }
//				       }
//				       //System.out.println("accidentDeathBen " +accidentDeathBen);
//				}
//				else
//					accidentDeathBen=40;
//		}
//
//		else {
//			 accidentDeathBen=0;
//		 }
        val = (sumassured * getATDBFinalPremRate(premPayOption, policyTerm, accidentDeathBen, PPT) / 100000);

        if ((Channel.endsWith("Online"))) {
            if (premPayOption.equalsIgnoreCase("Single")) {
                if (ageGroup.equalsIgnoreCase("5-12")) {
                    channel = 0.02;
                } else {
                    channel = 0.03;
                }
            } else if (premPayOption.equalsIgnoreCase("Regular")) {
                channel = 0.015;
            } else {
                channel = 0.04;
            }
        } else {
            channel = 0;
        }
        ATPDBChannelDis = val * channel;
        //System.out.println("ATPDBChannelDis &&& " +ATPDBChannelDis);
        return ATPDBChannelDis;
    }

    public double getAtdbafetrChannelDis(String premPayOption, int policyTerm, int PPT, double adbsumAssured) {
        double channel, val = 0, val2 = 0, accidentDeathBen = 0;
//		if(getAtpdbMaturity()== "Eligible")
//		{
//			 if(premPayOption.equalsIgnoreCase("Single"))
//				{
//					double [] PWBarr=eshieldNextDBICRates.getRPSP_ATDB();
//				       int position=0;
//				       for (int i=5; i<=57; i++)
//				       {
//
//				               if((policyTerm)==i)
//				               {
//				            	   accidentDeathBen= PWBarr[position];
//				               	break;
//				               }
//				               position++;
//
//				       }
//				}
//				else if(premPayOption.equalsIgnoreCase("LPPT"))
//				{
//					double [] PWBarr=eshieldNextDBICRates.getLP_ATDB();
//				       int position=0;
//				       outerloop:
//				       for (int i=5; i<=56; i++)
//				       {
//				    	   for(int j=6; j<=57; j++)
//				    	   {
//				               if((policyTerm)==j && PPT==i)
//				               {
//				            	   accidentDeathBen= PWBarr[position];
//				               	break outerloop;
//				               }
//				               position++;
//				    	   }
//				       }
//				       //System.out.println("accidentDeathBen " +accidentDeathBen);
//				}
//				else
//					accidentDeathBen=40;
//		}

//		else {
//			 accidentDeathBen=0;
//		 }
        val = (adbsumAssured * getATDBFinalPremRate(premPayOption, policyTerm, accidentDeathBen, PPT) / 100000);
        //System.out.println("val && " +val);

        val2 = val - ATPDBChannelDis;

        //System.out.println("val2 && " +val2);
        return val2;
    }


    public double getBetterHalfChannelDiscount(boolean staff, String Channel, double BetterHalfBenefitPrem, String premPayOption, double BHrate) {
        double channel;

        if ((Channel.endsWith("Online"))) {
            if (premPayOption.equalsIgnoreCase("Single")) {
                if (ageGroup.equalsIgnoreCase("5-12")) {
                    channel = 0.02;
                } else {
                    channel = 0.03;
                }
            } else if (premPayOption.equalsIgnoreCase("Regular")) {
                channel = 0.015;
            } else {
                channel = 0.04;
            }
        } else {
            channel = 0;
        }
        BetterHalfChannelDis = BHrate * channel;
        //System.out.println("BetterHalfChannelDis "+BetterHalfChannelDis);
        return BetterHalfChannelDis;
    }

    public double getbasicFrequencyLoading(String premFreq, double basicPrem, double channeldis, double sumassured, double rate) {
        double PremafterChannel;
        double val = 0;
        val = (sumassured * rate / 1000);
        PremafterChannel = (val - channeldis);
        //PremafterChannel = (val-channeldis);
        System.out.println("sumassured&&&& " + sumassured);
        System.out.println("channeldis&&&& " + channeldis);
        System.out.println("val&&&& " + val);
        System.out.println("PremafterChannel&&&& " + PremafterChannel);
        System.out.println("rate " + rate);
        if (premFreq.equalsIgnoreCase("Half-Yearly")) {
            freqLoading = PremafterChannel * 0.51;
        } else if (premFreq.equalsIgnoreCase("Monthly")) {
            freqLoading = PremafterChannel * 0.085;
        } else {
            freqLoading = PremafterChannel;
        }

//		System.out.println("\nfreqLoading1&&&&& "+freqLoading);

        return freqLoading;
    }


    public double getADBFrequencyLoading(String premFreq, double adbpremium, double channeldis, double sumassured, String premPayOption, int policyTerm, int PPT) {
        double PremafterChannel;
        PremafterChannel = getpremAfterChannelDis(premPayOption, policyTerm, PPT, sumassured);

//		val=(sumassured * 50/100000);
//		PremafterChannel = (val-channeldis);
        if (premFreq.equalsIgnoreCase("Half-Yearly")) {
            ADBfreqLoading = PremafterChannel * 0.52;
        } else if (premFreq.equalsIgnoreCase("Monthly")) {
            ADBfreqLoading = PremafterChannel * 0.089;
        } else {
            ADBfreqLoading = PremafterChannel;
        }
        //System.out.println("\nADBfreqLoading  "+ADBfreqLoading);
        return ADBfreqLoading;
    }

    public double getATPDBFrequencyLoading(String premFreq, double atpdbpremium, double channeldis, double sumassured, String premPayOption, int policyTerm, int PPT) {
        double PremafterChannel;


        PremafterChannel = getAtdbafetrChannelDis(premPayOption, policyTerm, PPT, sumassured);
        if (premFreq.equalsIgnoreCase("Half-Yearly")) {
            ATPDBfreqLoading = PremafterChannel * 0.52;
        } else if (premFreq.equalsIgnoreCase("Monthly")) {
            ATPDBfreqLoading = PremafterChannel * 0.089;
        } else {
            ATPDBfreqLoading = PremafterChannel;
        }
        //System.out.println("ATPDBfreqLoading &&& "+ATPDBfreqLoading);

        return ATPDBfreqLoading;
    }

    public double getBetterHalfFrequencyLoading(String premFreq, double betterhalfprem, double channeldis, double basicPremrate) {
        double PremafterChannel;
//		if(eshieldNextBean.getBetterHalfBenifit()==true)
//		{

        PremafterChannel = (basicPremrate - channeldis);
        System.out.println("PremafterChannelBHF " + PremafterChannel);
        if (premFreq.equalsIgnoreCase("Half-Yearly")) {
            BetterhalffreqLoading = PremafterChannel * 0.51;
        } else if (premFreq.equalsIgnoreCase("Monthly")) {
            BetterhalffreqLoading = PremafterChannel * 0.085;
        } else {
            BetterhalffreqLoading = PremafterChannel;
        }
//		}
//		else
//		{
//			BetterhalffreqLoading=0;
//		}
        //System.out.println("BetterhalffreqLoading "+BetterhalffreqLoading);

        return BetterhalffreqLoading;
    }


    public double getinstallmentPremWithChannelDis(boolean staff, String Channel) {
        double InstallchannelDis = 0;
        if (staff == true && Channel.equalsIgnoreCase("Online")) {
            InstallchannelDis = 0;
        } else if (Channel.equalsIgnoreCase("Online")) {
            InstallchannelDis = (ADBfreqLoading + ATPDBfreqLoading + freqLoading + BetterhalffreqLoading);
            //InstallchannelDis=(adbpremium+atpdbpremium+freqLoading+BetterhalffreqLoading);
            System.out.println("ADBfreqLoading ** " + ADBfreqLoading);
            System.out.println("ATPDBfreqLoading ** " + ATPDBfreqLoading);
            System.out.println("freqLoading ** " + freqLoading);
            System.out.println("BetterhalffreqLoading ** " + BetterhalffreqLoading);

        }

        return InstallchannelDis;
    }


    public double getADBStaffDisc(String premPayOption, boolean staff, double adbprem, double PolicyTerm, double adbsumassured, int PPT) {
        double staffval = 0, val = 0, accidentDeathBen = 0;
        if (staff) {


            if (premPayOption.equalsIgnoreCase("Single")) {
                if (getageGroup(PolicyTerm).equalsIgnoreCase("5-12")) {
                    staffval = 0.05;
                } else {
                    staffval = 0.075;
                }
            } else if (premPayOption.equalsIgnoreCase("Regular")) {
                staffval = 0.025;
            } else {
                staffval = 0.05;
            }
        } else {

            staffval = 0;
        }

        val = (adbsumassured * getADBFinalPremRate(premPayOption, PolicyTerm, accidentDeathBen, PPT)) / 100000;

        ADBstaffDisc = val * staffval;
        //		System.out.println("staffval  " +staffval);
        return ADBstaffDisc;
    }

    public double getATPDBStaffDisc(String premPayOption, boolean staff, double atpdbprem, double PolicyTerm, double adbsumassured, int PPT) {
        double staffval = 0, val = 0, accidentDeathBen = 0;
        if (staff) {
            if (premPayOption.equalsIgnoreCase("Single")) {
                if (getageGroup(PolicyTerm).equalsIgnoreCase("5-12")) {
                    staffval = 0.05;
                } else {
                    staffval = 0.075;
                }
            } else if (premPayOption.equalsIgnoreCase("Regular")) {
                staffval = 0.025;
            } else {
                staffval = 0.05;
            }
        } else {

            staffval = 0;
        }
        val = (adbsumassured * getATDBFinalPremRate(premPayOption, PolicyTerm, accidentDeathBen, PPT)) / 100000;
        ATPDBstaffDisc = val * staffval;
//			System.out.println("staffval** " +staffval);
//			System.out.println("val** " +val);
//			System.out.println("ATPDBstaffDisc** " +ATPDBstaffDisc);
        return ATPDBstaffDisc;
    }

    public double getbetterhalfStaffDisc(String premPayOption, boolean staff, double betterhalfprem, double PolicyTerm) {
        double staffval = 0;
        if (staff) {
            if (premPayOption.equalsIgnoreCase("Single")) {
                if (getageGroup(PolicyTerm).equalsIgnoreCase("5-12")) {
                    staffval = 0.05;
                } else {
                    staffval = 0.075;
                }
            } else if (premPayOption.equalsIgnoreCase("Regular")) {
                staffval = 0.025;
            } else {
                staffval = 0.05;
            }
        } else {

            staffval = 0;
        }

        BetterHalfstaffDisc = basicBetterhalfpremiumrate * staffval;
        return BetterHalfstaffDisc;
    }

    public double getbasicFrequencyLoadingStaffDiscount(String premFreq, double basicCover, double staffdis) {
        double PremafterChannel = 0;

        PremafterChannel = Double.parseDouble(cfap.getRoundOffLevel2(cfap.getStringWithout_E(basicCover - staffdis)));
        System.out.println("PremafterChannel% " + PremafterChannel);
        System.out.println("basicCover " + basicCover);
        System.out.println("staffdis " + staffdis);
        if (premFreq.equalsIgnoreCase("Half-Yearly")) {
            freqLoadingStaff = PremafterChannel * 0.51;
        } else if (premFreq.equalsIgnoreCase("Monthly")) {
            freqLoadingStaff = PremafterChannel * 0.085;
        } else {
            freqLoadingStaff = PremafterChannel;
        }
        System.out.println("freqLoadingStaff_1 " + freqLoadingStaff);
        return freqLoadingStaff;
    }

    public double getADBFrequencyLoadingStaffDiscount(String premFreq, double basicPrem, double staffdis, double adbsumassured, String premPayOption, int policyTerm, int PPT) {
        double PremafterChannel = 0, accidentDeathBen = 0;


        double val = 0;
        val = adbsumassured * getADBFinalPremRate(premPayOption, policyTerm, accidentDeathBen, PPT) / 100000;
        System.out.println("adbsumassured " + adbsumassured);
        System.out.println("val** " + val);
        System.out.println("ADBstaffDisc " + ADBstaffDisc);
        PremafterChannel = (val - ADBstaffDisc);
        System.out.println("PremafterChannel** " + PremafterChannel);
        if (premFreq.equalsIgnoreCase("Half-Yearly")) {
            ADBfreqLoadingStaff = PremafterChannel * 0.52;
        } else if (premFreq.equalsIgnoreCase("Monthly")) {
            ADBfreqLoadingStaff = PremafterChannel * 0.089;
        } else {
            ADBfreqLoadingStaff = PremafterChannel;
        }
        //		System.out.println("ADBfreqLoadingStaff_2 " +ADBfreqLoadingStaff);
        return ADBfreqLoadingStaff;
    }

    public double getATPDBFrequencyLoadingStaffDiscount(String premFreq, double basicPrem, double staffdis, double atpdbsumassured, String premPayOption, int policyTerm, int PPT) {
        double PremafterChannel = 0, accidentDeathBen = 0;
        double val = 0;
        val = atpdbsumassured * getATDBFinalPremRate(premPayOption, policyTerm, accidentDeathBen, PPT) / 100000;
        //		System.out.println("val  " + val);
        PremafterChannel = (val - ATPDBstaffDisc);
//			System.out.println("ATPDBstaffDisc " +ATPDBstaffDisc);
//			System.out.println("PremafterChannel " +PremafterChannel);
        if (premFreq.equalsIgnoreCase("Half-Yearly")) {
            ATPDBfreqLoadingStaff = PremafterChannel * 0.52;
        } else if (premFreq.equalsIgnoreCase("Monthly")) {
            ATPDBfreqLoadingStaff = PremafterChannel * 0.089;
        } else {
            ATPDBfreqLoadingStaff = PremafterChannel;
        }
        //		System.out.println("ATPDBfreqLoadingStaff_3 " +ATPDBfreqLoadingStaff);
        return ATPDBfreqLoadingStaff;
    }

    public double getBetterhalfFrequencyLoadingStaffDiscount(String premFreq, double basicPrem, double staffdis) {
        double PremafterChannel = 0;

        PremafterChannel = (basicBetterhalfpremiumrate - staffdis);
        if (premFreq.equalsIgnoreCase("Half-Yearly")) {
            BHfreqLoadingStaff = PremafterChannel * 0.51;
        } else if (premFreq.equalsIgnoreCase("Monthly")) {
            BHfreqLoadingStaff = PremafterChannel * 0.085;
        } else {
            BHfreqLoadingStaff = PremafterChannel;
        }
        //	System.out.println("BHfreqLoadingStaff_4 " +BHfreqLoadingStaff);
        return BHfreqLoadingStaff;
    }

    public double getinstallmentPremWithStaffDis(boolean staff) {
        double InstallstaffDis = 0;

        if (staff == true) {
            System.out.println("ADBfreqLoadingStaff " + ADBfreqLoadingStaff);
            System.out.println("ATPDBfreqLoadingStaff " + ATPDBfreqLoadingStaff);
            System.out.println("freqLoadingStaff " + freqLoadingStaff);
            System.out.println("BHfreqLoadingStaff " + BHfreqLoadingStaff);
            InstallstaffDis = (ADBfreqLoadingStaff + ATPDBfreqLoadingStaff + freqLoadingStaff + BHfreqLoadingStaff);
        } else {
            InstallstaffDis = 0;
        }

        return InstallstaffDis;
    }


    public double getBasicServiceTax(boolean staff, boolean state, String channel, double instchannelDis, double instSatffDis) {
        System.out.println("instchannelDis " + instchannelDis);
        System.out.println("instSatffDis" + instSatffDis);
        // System.out.println("eshieldNextProperties.CGST"+eshieldNextProperties.CGST);
        //System.out.println("totalpremiumwithoutST*eshieldNextProperties.CGST"+totalpremiumwithoutST*eshieldNextProperties.CGST);*/
        if (staff == false && channel.equalsIgnoreCase("Online")) {
            if (state == true) {
                BasicServiceTax = Double.parseDouble(cfap.getRoundUp(cfap.getStringWithout_E(instchannelDis * eshieldNextProperties.SGST)));
            } else {

                BasicServiceTax = Double.parseDouble(cfap.getRoundUp(cfap.getStringWithout_E(instchannelDis * eshieldNextProperties.CGST)));
                // BasicServiceTax= BasicServiceTax+BasicServiceTax;
            }
            BasicServiceTax = BasicServiceTax + Double.parseDouble(cfap.getRoundUp(cfap.getStringWithout_E(instchannelDis * eshieldNextProperties.CGST)));

        } else {
            if (state == true) {
                BasicServiceTax = Double.parseDouble(cfap.getRoundUp(cfap.getStringWithout_E(instSatffDis * eshieldNextProperties.SGST)));
            } else {
                BasicServiceTax = Double.parseDouble(cfap.getRoundUp(cfap.getStringWithout_E(instSatffDis * eshieldNextProperties.CGST)));
                // BasicServiceTax= BasicServiceTax+BasicServiceTax;
            }

            BasicServiceTax = BasicServiceTax + Double.parseDouble(cfap.getRoundUp(cfap.getStringWithout_E(instSatffDis * eshieldNextProperties.CGST)));

        }
        return BasicServiceTax;

    }


    public double getSBC(double SGST) {

        SBC = Double.parseDouble(cfap.getRoundUp(cfap.getStringWithout_E(totalpremiumwithoutST * SGST)));

        return SBC;
    }

    public double getKKC() {
        return KKC;

    }

    public double getServiceTax(boolean staff, String Channel) {
        if ((staff == false) && (Channel.endsWith("Online"))) {
            ServiceTax = BasicServiceTax;
        } else {
            ServiceTax = BasicServiceTax;
        }
        return ServiceTax;

    }

    public double getYearlyinstWithTaxes(boolean staff, String Channel, double InstallmentPremiumChannelDiscount, double InstallmentpremiumwithStaffDiscount) {
        double YearlyinstWithTaxes = 0;
        if ((staff == false) && (Channel.endsWith("Online"))) {
            YearlyinstWithTaxes = BasicServiceTax + InstallmentPremiumChannelDiscount;
        } else {
            YearlyinstWithTaxes = BasicServiceTax + InstallmentpremiumwithStaffDiscount;
        }
        return YearlyinstWithTaxes;
    }


    public double getPremiumwithST(String totalpremium) {
        PremiumwithST = Double.parseDouble(totalpremium) + ServiceTax;

        return PremiumwithST;

    }


    public double getStaffRebate(String Freq, boolean flag) {
        double staff_Rebate;

        if (flag == true) {
            if (Freq.equalsIgnoreCase("Single"))
                staff_Rebate = 0.02;
            else
                staff_Rebate = 0.05;
        } else
            staff_Rebate = 0;

        return staff_Rebate;

    }


}
