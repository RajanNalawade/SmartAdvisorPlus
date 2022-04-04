package sbilife.com.pointofsale_bancaagency.rinnraksha;

class RinnRakshaProperties {
    int minAge_HomeLoan = 18;
    int minAge_PersonalLoan = 18;
    int minAge_VehicleLoan = 18;
    int minAge_EducationLoan = 16;
    int minAge_PersonalLoan_HomeLoanEquity = 18;
    int maxAge_HomeLoan = 67;
    int maxAge_PersonalLoan = 65;
    int maxAge_VehicleLoan = 65;
    int maxAge_EducationLoan = 65;
    int maxAge_PersonalLoan_HomeLoanEquity = 67;

    /**
     * Change as per 28,May,2015 by Priyanka Warekar
     * JkResident Service tax
     */

//    double serviceTax=12.36/100;
    /**
     * Modified by Priyanka Warekar - 15,Nov,2015
     * Swach bharat Cess
     */
//	 serviceTax=0.14;


    /**
     * Modified by Tushar Kotian on 6/4/2017 ---- > Start
     */
    double serviceTaxJkResident = 0.126;
    double SBCServiceTaxJkResident = 0.00;
    double KKCServiceTaxJkResident = 0.00;

    double serviceTax = 0.14;
    double SBCServiceTax = 0.005;
    double KKCServiceTax = 0.005;
    /**
     * Modified by Tushar Kotian on 6/4/2017 ---- > End
     */

//    double serviceTax=0.15;
//	   double serviceTaxJKResident=0.126;

    String[] Staff_HomeLoan = {"Between 6.00% to 8.00%", "Between 8.50% to 11.50%"};
    String[] NonStaff_HomeLoan_old = {"Between 08.00% to 08.49%", "Between 8.50% to 11.50%", "Between 11.50% to 12.75%"};
    String[] NonStaff_HomeLoan = {"Between 06.00% to 08.49%", "Between 8.50% to 11.50%"};
    //    String[] Staff_LoanSubCategory_PersonalLoan={"Personal Loan","Mortgage Loan"};
    String[] Staff_LoanSubCategory_PersonalLoan = {"Personal Loan"};
    String[] NonStaff_LoanSubCategory_PersonalLoan = {"Personal Loan", "Mortgage Loan", "Home Loan Equity"};
    String[] Staff_PersonalLoan = {"Between 7.00% to 10.00%", "Between 14.00% to 16.99%", "Between 17.00% to 20.00%"};
    /**** Modified By - Priyanka Warekar - 4-sept-2015 --- Start ***/
//    String[] NonStaff_PersonalLoan={"Between 11.00% to 13.75%","Between 14.00% to 16.99%","Between 17.00% to 20.00%"};
    /***** Modified by - Priyanka Warekar - 01-04-2017- start *****/
//    String[] NonStaff_PersonalLoan={"Between 11.00% to 13.75%","Between 13.76% to 16.99%","Between 17.00% to 20.00%"};
    String[] NonStaff_PersonalLoan = {"Between 7.75% to 10.75%", "Between 10.76% to 13.75%", "Between 13.76% to 16.99%", "Between 17.00% to 20.00%"};
    /***** Modified by - Priyanka Warekar - 01-04-2017- end *****/
    /**** Modified By - Priyanka Warekar - 4-sept-2015 --- End ***/
    String[] NonStaff_PersonalLoan_HomeLoanEquity = {"Between 10.00% to 12.99%", "Between 13.00% to 16.00%"};
    /**** Modified By - Priyanka Warekar - 4-sept-2015 --- Start ***/
//    String[] NonStaff_PersonalLoan_MortgageLoan={"Between 14.00% to 16.99%","Between 13.00% to 16.00%"};
    /***** Modified by - Priyanka Warekar - 01-04-2017- start *****/
//    String[] NonStaff_PersonalLoan_MortgageLoan={"Between 13.76% to 16.99%","Between 17.00% to 20.00%"};
    String[] NonStaff_PersonalLoan_MortgageLoan = {"Between 14.00% to 16.99%", "Between 17.00% to 20.00%"};
    /***** Modified by - Priyanka Warekar - 01-04-2017- end *****/
    /**** Modified By - Priyanka Warekar - 4-sept-2015 --- End ***/
    String[] VechicleBorrowers = {"Only Primary Borrower", "2 Co-Borrowers"};
    String[] HoamLoanBorrowers = {"Only Primary Borrower", "2 Co-Borrowers", "3 Co-Borrowers"};


    /**** Modified By - Priyanka Warekar - 10-dec-2015 --- Start ***/
//    String[] Staff_VechicleLoan={"Between 7% to 10%(New Auto Loan)","Between 7% to 10%(Used Auto Loan)","Between 7% to 10%(Two Wheelers Loan)","Between 10.25% to 14%(New Auto Loan)","Between 15% to 18%(Used Auto Loan)","Between 15% to 18%(Two Wheelers Loan)"};
//    String[] NonStaff_VechicleLoan={"Between 10.25% to 14%(New Auto Loan)","Between 15% to 18%(Used Auto Loan)","Between 15% to 18%(Two Wheelers Loan)","Between 10.25% to 14%(Combo Vehicle Loan)"};
    String[] Staff_VechicleLoan = {"Between 7% to 10%(New Auto Loan)", "Between 7% to 10%(Used Auto Loan)", "Between 7% to 10%(Two Wheelers Loan)", "Between 10.25% to 14%(New Auto Loan)", "Between 15% to 18%(Used Auto Loan)", "Between 15% to 18%(Two Wheelers Loan)"};
    String[] NonStaff_VechicleLoan = {"Between 10.25% to 14%(New Auto Loan)", "Between 15% to 18%(Used Auto Loan)", "Between 15% to 18%(Two Wheelers Loan)", "Between 10.25% to 14%(Combo Vehicle Loan)", "Between 8.50% to 10.24%(New Auto Loan)"};
    /**** Modified By - Priyanka Warekar - 10-dec-2015 --- End ***/

    String[] Staff_EducationLoan = {"Between 7.00% to 10.00%", "Between 12.00% to 15.00%"};
    String[] NonStaff_EducationLoan = {"Between 09.00% to 11.50%", "Between 11.51% to 15.00%"};
    /**** Modified By - Priyanka Warekar - 4-sept-2015 --- Start ***/
//    String[] Moratorium_EducationLoan={"3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", 
//    		"22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", 
//    		"42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60"};
    String[] Moratorium_EducationLoan = {"3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21",
            "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41",
            "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60",
            "61", "62", "63", "64", "65", "66", "67", "68", "69", "70", "71", "72"};


    /**** Modified By - Priyanka Warekar - 4-sept-2015 --- End ***/

    String[] Moratorium_HoamLoan_Staff = {"3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21",
            "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36"};

    //********************added by vrushali chaudhari********************
    String[] Moratorium_HoamLoan_NonStaff = {"3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21",
            "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48"};
    /***************************************************************************************************************/

    String[] NonStaff_HomeLoan_RRB = {"Select Interest Rate Range", "Between 8.50% to 11.50%", "Between 11.51% to 14.50%"};
    String[] Staff_HomeLoan_RRB = {"Select Interest Rate Range"};
    String[] Staff_LoanSubCategory_PersonalLoan_RRB = {"Personal Loan"};
    String[] NonStaff_LoanSubCategory_PersonalLoan_RRB = {"Personal Loan"};
    String[] Staff_PersonalLoan_RRB = {"Select Interest Rate Range"};
    String[] NonStaff_PersonalLoan_RRB = {"Select Interest Rate Range", "Between 11% to 14.25%", "Between 14.50% to 17.50%"};
    String[] VechicleBorrowers_RRB = {"Only Primary Borrower", "2 Co-Borrowers"};
    String[] HoamLoanBorrowers_RRB = {"Only Primary Borrower", "2 Co-Borrowers", "3 Co-Borrowers"};
    String[] Staff_VechicleLoan_RRB = {"Select Interest Rate Range"};
    String[] NonStaff_VechicleLoan_RRB = {"Select Interest Rate Range", "Between 10% to 13%(Car Loan)", "Between 13.01% to 16%(Car Loan)", "Between 11.50% to 14.50%(Two Wheelers Loan)", "Between 14.51% to 17.50%(Two Wheelers Loan)", "Between 17.51% to 19.00%(Two Wheelers Loan)", "Between 9.50% to 12.50%(Tractor Loan)", "Between 12.51% to 15.50%(Tractor Loan)"};

    String[] Moratorium_HoamLoan_Staff_RRB = {"3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21",
            "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36"};

    String[] Staff_EducationLoan_RRB = {"Select Interest Rate Range"};
    String[] NonStaff_EducationLoan_RRB = {"Select Interest Rate Range", "Between 10% to 13%", "Between 13.01% to 15%"};

    String[] Moratorium_HoamLoan_RRB = {"3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18"};

    String[] Moratorium_EducationLoan_RRB = {"3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21",
            "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41",
            "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60"};

    //Constructor
    public RinnRakshaProperties() {
    }
}
