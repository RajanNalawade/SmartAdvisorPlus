package sbilife.com.pointofsale_bancaagency.flexismartplus;

class FlexiSmartPlusProperties {
    public int minAgeLimit = 18, maxAgeLimit = 60, minPolicyTermLimit = 5,
            maxPolicyTermLimit = 30, minPremHolidayTerm = 1,
            maxPremHolidayTerm = 3, minPolicyYear = 6;

    public double minSAMF = 10,
            maxSAMF = 20,
            topUp = 0.0,
            topUp_commission = 0.02,
            riskPremiumRate = 1.2,

    /**
     * As per the changes from 1st Apr 2015, by Vrushali Chaudhari
     *
     */
    // serviceTaxJkResident=0.105
    // serviceTax=0.1236,
    /**
     * New ST from 1st june 2016
     */
    //serviceTax = 0.18,
    serviceTaxJkResident = 0.126,
            serviceTaxReductionYield = 0.145, guaranteedInterest = 0.01,
            int1 = 0.04, int2 = 0.08, fundManagementCharge = 0.01,
            topUp_expense = 0.01, inflation = 0.03, initial = 840,
            renewal = 600;

    /*********************************************************************************************************/

    // Fixed Input
    public int year_TransferOfCapital_W62 = 5;
    public int year_TransferOfCapital_W63 = 6;
    public int noOfYrsAllowForTransfOfGain = 6; // *Fixed Input,*Sheet Name ->
    // Input,*Cell -> X57
    public double thresholdLimitForTransfOfGain = 0.15; // *Fixed Input,*Sheet
    // Name -> Input,*Cell
    // -> X56
    public double charge_Guarantee = 0.005; // *Fixed Input,*Sheet Name ->
    // Input,*Cell -> AB21
    public double charge_SumAssuredBase = 0; // *Fixed Input,*Sheet Name ->
    // Input,*Cell -> AO24 -> AB22
    public double charge_Fund = 0; // *Fixed Input,*Sheet Name -> Input,*Cell ->
    // AO17
    public int charge_Inflation = 0; // *Fixed Input,*Sheet Name -> Input,*Cell
    // -> AT5
    public double charge_Fund_Ren = 0.01; // *Fixed Input,*Sheet Name ->
    // Input,*Cell -> AB20 -> AE59
    public double indexFund = 0.0125; // *Fixed Input,*Sheet Name -> Input,*Cell
    // -> AE60
    public int noOfYearsForSArelatedCharges = 3; // *Fixed Input,*Sheet Name ->
    // Input,*Cell -> AO25 ->
    // AB23
    public double ADBrate = 0.0005; // *Fixed Input,*Sheet Name -> Input,*Cell
    // -> AE33
    public int inflation_pa_RP = 0; // *Fixed Input,*Sheet Name -> Input,*Cell
    // -> AF13
    public int inflation_pa_SP = 0; // *Fixed Input,*Sheet Name -> Input,*Cell
    // -> AG13 === AF13
    public int fixedMonthlyExp_RP = 60; // *Fixed Input,*Sheet Name ->
    // Input,*Cell -> AF12
    public int fixedMonthlyExp_SP = 50; // *Fixed Input,*Sheet Name ->
    // Input,*Cell -> AG12
    public double topUpPremiumAmt = 5000; // *Fixed Input,*Sheet Name ->
    // Input,*Cell -> D25

    // Input Drop Downs from "BI_Incl_Mort & Ser Tax" sheet
    public boolean allocationCharges = false; // *Sheet Name -> BI_Incl_Mort &
    // Ser Tax,*Cell ->B20
    public boolean mortalityAndRiderCharges = false; // *Sheet Name ->
    // BI_Incl_Mort & Ser
    // Tax,*Cell ->B21
    public boolean administrationAndSArelatedCharges = false; // *Sheet Name ->
    // BI_Incl_Mort
    // & Ser
    // Tax,*Cell
    // ->B22
    public boolean fundManagementCharges = false; // *Sheet Name -> BI_Incl_Mort
    // & Ser Tax,*Cell ->B23
    public boolean surrenderCharges = false; // *Sheet Name -> BI_Incl_Mort &
    // Ser Tax,*Cell ->B24
    public boolean guaranteeCharges = false; // *Sheet Name -> BI_Incl_Mort &
    // Ser Tax,*Cell ->B25
    public boolean mortalityCharges = false; // *Sheet Name -> BI_Incl_Mort &
    // Ser Tax,*Cell ->B27
    public boolean riderCharges = false; // *Sheet Name -> BI_Incl_Mort & Ser
    // Tax,*Cell ->B29

    // Constructor
    public FlexiSmartPlusProperties() {
    }

}
