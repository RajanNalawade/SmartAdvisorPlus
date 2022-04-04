package sbilife.com.pointofsale_bancaagency.needanalysis;

class NeedAnalysisOutput {

    public static void main(String[] args) {
        NeedAnalysisBusinessLogic needAnalysisBusinessLogic = new NeedAnalysisBusinessLogic();

        String inputStr = "<?xml version='1.0' encoding='utf-8'?><NeedAn><PrGe>F</PrGe><PrAge>30</PrAge><PrMarrSt>Married</PrMarrSt><PrDependCh>1</PrDependCh><PrAnnIncome>1500000</PrAnnIncome><PrAnnExp>200000</PrAnnExp><PrOutStaHmLoanAmt>100000</PrOutStaHmLoanAmt><PrOutStaOthrLoanAmt>500000</PrOutStaOthrLoanAmt><currLifeInCov>25000000</currLifeInCov><currRetCorpSav>500000</currRetCorpSav><PrRetAge>60</PrRetAge><PrPostRetLife>Luxury</PrPostRetLife>" +
                "<PrEduChAge1>1</PrEduChAge1><PrEduChBudExpNoYrs1>18</PrEduChBudExpNoYrs1><PrEduChBudEXp1>40000</PrEduChBudEXp1><PrEduChCurrEXp1>500000</PrEduChCurrEXp1>" +
                "<PrEduChAge2>1</PrEduChAge2><PrEduChBudExpNoYrs2>18</PrEduChBudExpNoYrs2><PrEduChBudEXp2>40000</PrEduChBudEXp2><PrEduChCurrEXp2>500000</PrEduChCurrEXp2><PrEduChAge3>0</PrEduChAge3><PrEduChBudExpNoYrs3>0</PrEduChBudExpNoYrs3><PrEduChBudEXp3>0</PrEduChBudEXp3><PrEduChCurrEXp3>0</PrEduChCurrEXp3><PrEduChAge4>0</PrEduChAge4><PrEduChBudExpNoYrs4>0</PrEduChBudExpNoYrs4><PrEduChBudEXp4>0</PrEduChBudEXp4><PrEduChCurrEXp4>0</PrEduChCurrEXp4>" +
                "<PrMaChBudExpNoYrs1>25</PrMaChBudExpNoYrs1><PrMaChBudEXp1>200000</PrMaChBudEXp1><PrMaChCurrEXp1>500000</PrMaChCurrEXp1>" +
                "<PrMaChCurrExpNoYrs2>0</PrMaChCurrExpNoYrs2><PrMaChBudEXp2>00</PrMaChBudEXp2><PrMaChCurrEXp2>0</PrMaChCurrEXp2><PrMaChBudExpNoYrs3>0</PrMaChBudExpNoYrs3><PrMaChBudEXp3>0</PrMaChBudEXp3><PrMaChCurrEXp3>0</PrMaChCurrEXp3><PrMaChBudExpNoYrs4>0</PrMaChBudExpNoYrs4><PrMaChBudEXp4>0</PrMaChBudEXp4><PrMaChCurrEXp4>0</PrMaChCurrEXp4>" +
                "<PrProWeCrNoofYr>35</PrProWeCrNoofYr><PrProWeCrBudExp>500000</PrProWeCrBudExp><PrProWeCrCurrExp>5000000</PrProWeCrCurrExp><PrOthFinWeCrNoofYr>4</PrOthFinWeCrNoofYr><PrOthFinWeCrBudExp>300000</PrOthFinWeCrBudExp><PrOthFinWeCrCurrExp>1000000</PrOthFinWeCrCurrExp>" +
                /*** Added by Akshaya on 25-MAY-2015 start********/
                "<Inflation>5</Inflation><RiskApp>Aggressive</RiskApp></NeedAn>";
        /*** Added by Akshaya on 25-MAY-2015 end********/
        // System.out.println("Test:" +inputStr);

        String outPutRes = needAnalysisBusinessLogic.getNewNeedProdRes(inputStr);
        System.out.println(outPutRes);


    }


}
