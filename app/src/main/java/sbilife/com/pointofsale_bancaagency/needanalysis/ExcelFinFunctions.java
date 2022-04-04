package sbilife.com.pointofsale_bancaagency.needanalysis;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.math.BigDecimal;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


class ExcelFinFunctions {

    public ExcelFinFunctions() {

    }
    //Calculates the payment for a loan based on constant payments and a constant interest rate.
	/*

		PMT(ir,np,pv,fv,type)

		For a more complete description of the arguments in PMT, see the PV function.

		  ir    is the interest rate for the loan.

		   np   is the total number of payments for the loan.

		   Pv     is the present value, or the total amount that a series of future payments is worth now; also known 			as the principal.

		  Fv     is the future value, or a cash balance you want to attain after the last payment is made. If fv is 				omitted, it is assumed to be 0 (zero), that is, the future value of a loan is 0.

		  Type     is the number 0 (zero) or 1 and indicates when payments are due.



	 */


    public double PMT(double ir, double np, double pv, double fv, double type) {
        /*  ir - interest rate per month  np - number of periods (months)  pv - present value  fv - future value (residual 		value)  */
        System.out.println("pow : " + Math.pow((ir + 1), np));
        System.out.println("pow : " + (pv * Math.pow((ir + 1), np) + fv));
        return (ir * (pv * Math.pow((ir + 1), np) + fv)) / ((ir * type + 1) * (Math.pow((ir + 1), np) - 1));

    }

	/*    Returns the future value of an investment based on periodic, constant payments and a constant interest rate.

              calcFV(rate,nper,pmt)



		Rate     is the interest rate per period.

		Nper     is the total number of payment periods in an annuity.

		Pmt     is the payment made each period; it cannot change over the life of the annuity. Typically, pmt 		contains principal and interest but no other fees or taxes. If pmt is omitted, you must include the pv 		argument.




	 */

    public double calcFV(double rate, int nPer, double pps) {
        double sum = pps;
        for (int i = 0; i < nPer; i++) {
            sum += sum * rate;
        }
        return sum;
    }


    double getRoundUp(double number, int num_digits) {
        double retVal;
        BigDecimal bd = new BigDecimal(number);
        bd = bd.setScale(num_digits, BigDecimal.ROUND_UP);
        retVal = bd.doubleValue();

        return retVal;
    }


    // Function parsing Xml string and returning String array
    public String[] parseInputString(String inputStr) throws Exception {

        // String parseStr="";
        String[] parsRes;
        parsRes = new String[48];
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(inputStr));
        Document doc = db.parse(is);
        NodeList nodes = doc.getElementsByTagName("NeedAn");

        Element element = (Element) nodes.item(0);

        NodeList chnodes = element.getChildNodes();

        for (int nodeCount = 0; nodeCount < chnodes.getLength(); nodeCount++) {
            Element celement = (Element) chnodes.item(nodeCount);
            if (nodeCount == 0) {
                parsRes[nodeCount] = celement.getTextContent();
            } else {

                parsRes[nodeCount] = celement.getTextContent();

            }

        }
        // System.out.println(parseStr);
        return parsRes;

    }


    //Function will accept double value. It double value contains 'e' it will return String without 'e'
    public String getStringWithout_E(double doubleVal) {
        String dStr = "" + doubleVal;
        int indexOf_E = dStr.indexOf('E');
        int indexOf_e = dStr.indexOf('e');
        String seperator = null;
        String[] strPartsByE = new String[2];
        String[] strPartsByDecimal = new String[2];
        String rawStrWithoutDecimal = null;
        if ((indexOf_E > 0) || (indexOf_e > 0)) {
            if (indexOf_E > 0) {
                seperator = "E";
            } else if (indexOf_e > 0) {
                seperator = "e";
            }
            strPartsByE = split(dStr, seperator);
            strPartsByDecimal = split(strPartsByE[0], ".");
            rawStrWithoutDecimal = strPartsByDecimal[0] + strPartsByDecimal[1] + "00000000000000000000";
            return (rawStrWithoutDecimal.substring(0, 1 + Integer.parseInt(strPartsByE[1]))) + "." + rawStrWithoutDecimal.substring(1 + Integer.parseInt(strPartsByE[1]), 4 + Integer.parseInt(strPartsByE[1]));
        } else {
            return dStr;
        }
    }

    //Convert & return Product Related Constant Data which is in String form into an array format
    @SuppressWarnings({"rawtypes", "unchecked"})
    public String[] split(String original, String separator) {
        Vector nodes = new Vector();
        int index = original.indexOf(separator);
        while (index >= 0) {
            nodes.addElement(original.substring(0, index));
            original = original.substring(index + separator.length());
            index = original.indexOf(separator);
        }
        nodes.addElement(original);
        String[] result = new String[nodes.size()];
        if (nodes.size() > 0) {
            for (int loop = 0; loop < nodes.size(); loop++)
                result[loop] = (String) nodes.elementAt(loop);
        }
        return result;
    }


    // Added By Mukesh on 3rd April,2015

    //Function for calculating Expected Monthly Expenses at Retirement
    //Function for FV of Available Corpus  (Only pass different value)

    public double getExpMonExpensesAtRetirement(double inflationRate, int noOfYrs, double monthlyExpenses, String lifeStyle) {
        double retVal = 0;
        double temVal = 0;


        temVal = calcFV(inflationRate, noOfYrs, monthlyExpenses);

        //	   System.out.println("FV value" + temVal);
        if (lifeStyle.equals("Basic")) {

            retVal = temVal * 0.7;
        } else if (lifeStyle.equals("Comfortable")) {
            retVal = temVal * 0.85;
        } else {
            retVal = temVal;

        }
        //	   System.out.println("d26" + retVal);

        return retVal;


    }


    //   public ArrayList<String> getChildFuturPlanningArr(double InflationRt,double riskAppetite,double riskAppettiteMon,String PrEduChAge1, String PrEduChBudExpNoYrs1,String PrEduChBudEXp1,String PrEduChCurrEXp1,String PrMaChBudExpNoYrs1,String PrMaChBudEXp1,String PrMaChCurrEXp1)
    //   {
    //	   ArrayList<String> retVal = new ArrayList<String>();
    //	   double fv =0,totCorpReqEdu=0,currSavingsEdu=0,tmpGap1=0,ValPMT=0;
    //	   double totCorpReqMar=0,currSavingsMar=0;
    //
    //
    //
    ////	   String  PrEduChBudExpNoYrs1=inputParam[13];
    ////	   String PrEduChAge1 = inputParam[12];
    ////	   String PrEduChCurrEXp1= inputParam[15];
    ////	   String PrEduChBudEXp1 = inputParam[14];
    ////
    ////	   String PrMaChBudExpNoYrs1 = inputParam[28];
    ////	   String PrMaChBudEXp1 = inputParam[29];
    ////	   String PrMaChCurrEXp1 = inputParam[30];
    //
    //	   /************************************************************* Child 1 start ****************************************/
    //	   /************Education Section *********/
    //
    //	   // Corpus Required for Education
    //	   totCorpReqEdu=calcFV(InflationRt,Integer.parseInt(PrEduChBudExpNoYrs1)- Integer.parseInt(PrEduChAge1),Double.parseDouble(PrEduChCurrEXp1));
    //
    //	   retVal.add(getStringWithout_E(Math.round(totCorpReqEdu))); //0
    //
    ////	   System.out.println("Total Corpus req" + totCorpReqEdu);
    //	   //FV of Current Savings
    //
    //	   currSavingsEdu=calcFV(riskAppetite,Integer.parseInt(PrEduChBudExpNoYrs1)- Integer.parseInt(PrEduChAge1),Double.parseDouble(PrEduChBudEXp1));
    //	   retVal.add(getStringWithout_E(Math.round(currSavingsEdu)));//1
    ////	   System.out.println("Current Sav" + currSavingsEdu);
    //
    //
    //	   //Gap
    //	   tmpGap1 = totCorpReqEdu-currSavingsEdu;
    //
    //	   if (tmpGap1 < 0)
    //	   {
    //		   retVal.add("0"); //2
    //	   }
    //	   else
    //	   {
    //		   retVal.add(getStringWithout_E(Math.round(tmpGap1)));//2
    //	   }
    //
    ////	   System.out.println("Gap " + tmpGap1);
    //	   //Monthly Investment required
    //
    //	   ValPMT= PMT(riskAppettiteMon/12,Integer.parseInt(PrEduChBudExpNoYrs1)*12,0,tmpGap1,0);
    //	   retVal.add(getStringWithout_E(Math.round(ValPMT))); //3
    //
    ////	   System.out.println("Monthly Investment" + ValPMT);
    //
    //	   /*****************End Education Sction*****/ /////////////////////////////
    //
    //	   // Start Marriage Section first Child //
    //	   //Tot Corpus Required for Marriage
    //	   totCorpReqMar=calcFV(InflationRt,Integer.parseInt(PrMaChBudExpNoYrs1)- Integer.parseInt(PrEduChAge1),Double.parseDouble(PrMaChCurrEXp1));
    //	   retVal.add(getStringWithout_E(Math.round(totCorpReqMar))); //4
    //
    ////	   System.out.println("tot corp for Mar" + totCorpReqMar);
    //
    //	   //FV of Current Savings
    //	   currSavingsMar=calcFV(riskAppetite,Integer.parseInt(PrMaChBudExpNoYrs1)- Integer.parseInt(PrEduChAge1),Double.parseDouble(PrMaChBudEXp1));
    //	   retVal.add(getStringWithout_E(Math.round(currSavingsMar))); //5
    //
    ////	   System.out.println("Curr sav marr" + currSavingsMar);
    //	   //Gap
    //	   double tmpGap2=0;
    //	   tmpGap2 = totCorpReqMar-currSavingsMar;
    //
    ////	   System.out.println("Gap in marr" + tmpGap2);
    //
    //	   if (tmpGap1 < 0)
    //		   retVal.add("0"); //6
    //	   else
    //		   retVal.add(getStringWithout_E(Math.round(tmpGap2))); //6
    //
    //
    //	   // Monthly Investment required
    //	   double ValPMT1=0;
    //	   ValPMT1= PMT(riskAppettiteMon/12,Integer.parseInt(PrMaChBudExpNoYrs1)*12,0,tmpGap2,1);
    //	   retVal.add(getStringWithout_E(Math.round(ValPMT1))); //7
    //
    ////	   System.out.println("Monthly inv marr" + ValPMT1);
    //
    //
    //	   /******************************************* First Child end **************************************/
    //
    //
    //
    //	   return retVal;
    //   }
    //


    private double[] getOneChildFuturPlanning(double InflationRt, String[] inputParam, double riskAppetite, double riskAppettiteMon) {

        double totCorpReqEdu = 0, currSavingsEdu = 0, tmpGap1 = 0, ValPMT = 0;
        double totCorpReqMar = 0, currSavingsMar = 0;
        double[] retVal = new double[32];


        for (int i = 0; i < retVal.length; i++) {
            retVal[i] = 0;
        }

        if (Integer.parseInt(inputParam[3]) == 1) {

            String PrEduChBudExpNoYrs1 = inputParam[13];
            String PrEduChAge1 = inputParam[12];
            String PrEduChCurrEXp1 = inputParam[15];
            String PrEduChBudEXp1 = inputParam[14];

            String PrMaChBudExpNoYrs1 = inputParam[28];
            String PrMaChBudEXp1 = inputParam[29];
            String PrMaChCurrEXp1 = inputParam[30];

            /************************************************************* Child 1 start ****************************************/
            /************Education Section *********/

            // Corpus Required for Education
            totCorpReqEdu = calcFV(InflationRt, Integer.parseInt(PrEduChBudExpNoYrs1) - Integer.parseInt(PrEduChAge1), Double.parseDouble(PrEduChCurrEXp1));

            retVal[0] = Math.round(totCorpReqEdu);

            //System.out.println("Total Corpus req" + totCorpReqEdu);
            //FV of Current Savings

            currSavingsEdu = calcFV(riskAppetite, Integer.parseInt(PrEduChBudExpNoYrs1) - Integer.parseInt(PrEduChAge1), Double.parseDouble(PrEduChBudEXp1));
            retVal[1] = Math.round(currSavingsEdu);
            // System.out.println("Current Sav" + currSavingsEdu);


            //Gap
            tmpGap1 = totCorpReqEdu - currSavingsEdu;

            if (tmpGap1 < 0) {
                retVal[2] = 0;
            } else {
                retVal[2] = Math.round(tmpGap1);
            }

            //		   System.out.println("Gap " + tmpGap1);
            //Monthly Investment required

            ValPMT = PMT(riskAppettiteMon / 12, Integer.parseInt(PrEduChBudExpNoYrs1) * 12, 0, tmpGap1, 0);
            retVal[3] = Math.round(ValPMT);

            //		   System.out.println("Monthly Investment" + ValPMT);

            /*****************End Education Sction*****/ /////////////////////////////

            // Start Marriage Section first Child //
            //Tot Corpus Required for Marriage
            totCorpReqMar = calcFV(InflationRt, Integer.parseInt(PrMaChBudExpNoYrs1) - Integer.parseInt(PrEduChAge1), Double.parseDouble(PrMaChCurrEXp1));
            retVal[4] = Math.round(totCorpReqMar);

            //		   System.out.println("tot corp for Mar" + totCorpReqMar);

            //FV of Current Savings
            currSavingsMar = calcFV(riskAppetite, Integer.parseInt(PrMaChBudExpNoYrs1) - Integer.parseInt(PrEduChAge1), Double.parseDouble(PrMaChBudEXp1));
            retVal[5] = Math.round(currSavingsMar);

            //		   System.out.println("Curr sav marr" + currSavingsMar);
            //Gap
            double tmpGap2 = 0;
            tmpGap2 = totCorpReqMar - currSavingsMar;

            //		   System.out.println("Gap in marr" + tmpGap2);

            if (tmpGap1 < 0)
                retVal[6] = 0;
            else
                retVal[6] = Math.round(tmpGap2);


            // Monthly Investment required
            double ValPMT1 = 0;
            ValPMT1 = PMT(riskAppettiteMon / 12, Integer.parseInt(PrMaChBudExpNoYrs1) * 12, 0, tmpGap2, 1);
            retVal[7] = Math.round(ValPMT1);

            //		   System.out.println("Monthly inv marr" + ValPMT1);


            /******************************************* First Child end **************************************/


        }
        return retVal;

    }


    private double[] getTwoChildFuturPlanning(double InflationRt, String[] inputParam, double riskAppetite, double riskAppettiteMon) {


        double totCorpReqEdu = 0, currSavingsEdu = 0, tmpGap1 = 0, ValPMT = 0;
        double totCorpReqMar = 0, currSavingsMar = 0, tmpGap2 = 0, ValPMT1 = 0;
        double[] retVal = new double[32];


        for (int i = 0; i < retVal.length; i++) {
            retVal[i] = 0;
        }

        //Child 1
        String PrEduChAge1 = inputParam[12];
        String PrEduChBudExpNoYrs1 = inputParam[13];
        String PrEduChCurrEXp1 = inputParam[15];
        String PrEduChBudEXp1 = inputParam[14];

        String PrMaChBudExpNoYrs1 = inputParam[28];
        String PrMaChBudEXp1 = inputParam[29];
        String PrMaChCurrEXp1 = inputParam[30];


        //Child 2
        String PrEduChAge2 = inputParam[16];
        String PrEduChBudExpNoYrs2 = inputParam[17];
        String PrEduChCurrEXp2 = inputParam[19];
        String PrEduChBudEXp2 = inputParam[18];

        String PrMaChBudExpNoYrs2 = inputParam[31];
        String PrMaChBudEXp2 = inputParam[32];
        String PrMaChCurrEXp2 = inputParam[33];


        /************************************************************* Child 1 start ****************************************/
        /************Education Section *********/

        // Corpus Required for Education
        totCorpReqEdu = calcFV(InflationRt, Integer.parseInt(PrEduChBudExpNoYrs1) - Integer.parseInt(PrEduChAge1), Double.parseDouble(PrEduChCurrEXp1));

        retVal[0] = Math.round(totCorpReqEdu);

        //System.out.println("Total Corpus req" + totCorpReqEdu);
        //FV of Current Savings

        currSavingsEdu = calcFV(riskAppetite, Integer.parseInt(PrEduChBudExpNoYrs1) - Integer.parseInt(PrEduChAge1), Double.parseDouble(PrEduChBudEXp1));
        retVal[1] = Math.round(currSavingsEdu);
        // System.out.println("Current Sav" + currSavingsEdu);


        //Gap
        tmpGap1 = totCorpReqEdu - currSavingsEdu;

        if (tmpGap1 < 0) {
            retVal[2] = 0;
        } else {
            retVal[2] = Math.round(tmpGap1);
        }

        //  		   System.out.println("Gap " + tmpGap1);
        //Monthly Investment required

        ValPMT = PMT(riskAppettiteMon / 12, Integer.parseInt(PrEduChBudExpNoYrs1) * 12, 0, tmpGap1, 0);
        retVal[3] = Math.round(ValPMT);

        //  		   System.out.println("Monthly Investment" + ValPMT);

        /*****************End Education Sction*****/ /////////////////////////////

        // Start Marriage Section first Child //
        //Tot Corpus Required for Marriage
        totCorpReqMar = calcFV(InflationRt, Integer.parseInt(PrMaChBudExpNoYrs1) - Integer.parseInt(PrEduChAge1), Double.parseDouble(PrMaChCurrEXp1));
        retVal[4] = Math.round(totCorpReqMar);

        //  		   System.out.println("tot corp for Mar" + totCorpReqMar);

        //FV of Current Savings
        currSavingsMar = calcFV(riskAppetite, Integer.parseInt(PrMaChBudExpNoYrs1) - Integer.parseInt(PrEduChAge1), Double.parseDouble(PrMaChBudEXp1));
        retVal[5] = Math.round(currSavingsMar);

        //  		   System.out.println("Curr sav marr" + currSavingsMar);
        //Gap
        tmpGap2 = 0;
        tmpGap2 = totCorpReqMar - currSavingsMar;

        //  		   System.out.println("Gap in marr" + tmpGap2);

        if (tmpGap1 < 0)
            retVal[6] = 0;
        else
            retVal[6] = Math.round(tmpGap2);


        // Monthly Investment required
        ValPMT1 = 0;
        ValPMT1 = PMT(riskAppettiteMon / 12, Integer.parseInt(PrMaChBudExpNoYrs1) * 12, 0, tmpGap2, 1);
        retVal[7] = Math.round(ValPMT1);

        //  		   System.out.println("Monthly inv marr" + ValPMT1);


        /*******************************************  Child  1 end **************************************/


        /************************************************************* Child 2 start ****************************************/
        /************Education Section *********/

        // Corpus Required for Education
        totCorpReqEdu = calcFV(InflationRt, Integer.parseInt(PrEduChBudExpNoYrs2) - Integer.parseInt(PrEduChAge2), Double.parseDouble(PrEduChCurrEXp2));

        retVal[8] = Math.round(totCorpReqEdu);

        //System.out.println("Total Corpus req" + totCorpReqEdu);
        //FV of Current Savings

        currSavingsEdu = calcFV(riskAppetite, Integer.parseInt(PrEduChBudExpNoYrs2) - Integer.parseInt(PrEduChAge2), Double.parseDouble(PrEduChBudEXp2));
        retVal[9] = Math.round(currSavingsEdu);
        // System.out.println("Current Sav" + currSavingsEdu);


        //Gap
        tmpGap1 = totCorpReqEdu - currSavingsEdu;

        if (tmpGap1 < 0) {
            retVal[10] = 0;
        } else {
            retVal[10] = Math.round(tmpGap1);
        }

        //  		   System.out.println("Gap " + tmpGap1);
        //Monthly Investment required

        ValPMT = PMT(riskAppettiteMon / 12, Integer.parseInt(PrEduChBudExpNoYrs2) * 12, 0, tmpGap1, 0);
        retVal[11] = Math.round(ValPMT);

        //  		   System.out.println("Monthly Investment" + ValPMT);

        /*****************End Education Sction*****/ /////////////////////////////

        // Start Marriage Section first Child //
        //Tot Corpus Required for Marriage
        totCorpReqMar = calcFV(InflationRt, Integer.parseInt(PrMaChBudExpNoYrs2) - Integer.parseInt(PrEduChAge2), Double.parseDouble(PrMaChCurrEXp2));
        retVal[12] = Math.round(totCorpReqMar);

        //  		   System.out.println("tot corp for Mar" + totCorpReqMar);

        //FV of Current Savings
        currSavingsMar = calcFV(riskAppetite, Integer.parseInt(PrMaChBudExpNoYrs2) - Integer.parseInt(PrEduChAge2), Double.parseDouble(PrMaChBudEXp2));
        retVal[13] = Math.round(currSavingsMar);

        //  		   System.out.println("Curr sav marr" + currSavingsMar);
        //Gap
        tmpGap2 = 0;
        tmpGap2 = totCorpReqMar - currSavingsMar;

        //  		   System.out.println("Gap in marr" + tmpGap2);

        if (tmpGap1 < 0)
            retVal[14] = 0;
        else
            retVal[14] = Math.round(tmpGap2);


        // Monthly Investment required
        ValPMT1 = 0;
        ValPMT1 = PMT(riskAppettiteMon / 12, Integer.parseInt(PrMaChBudExpNoYrs2) * 12, 0, tmpGap2, 1);
        retVal[15] = Math.round(ValPMT1);

        //  		   System.out.println("Monthly inv marr" + ValPMT1);


        /*******************************************  Child 2 end **************************************/
        return retVal;


    }


    private double[] getThreeChildFuturPlanning(double InflationRt, String[] inputParam, double riskAppetite, double riskAppettiteMon) {

        double totCorpReqEdu = 0, currSavingsEdu = 0, tmpGap1 = 0, ValPMT = 0;
        double totCorpReqMar = 0, currSavingsMar = 0, tmpGap2 = 0, ValPMT1 = 0;
        double[] retVal = new double[32];


        for (int i = 0; i < retVal.length; i++) {
            retVal[i] = 0;
        }

        //Child 1
        String PrEduChAge1 = inputParam[12];
        String PrEduChBudExpNoYrs1 = inputParam[13];
        String PrEduChCurrEXp1 = inputParam[15];
        String PrEduChBudEXp1 = inputParam[14];

        String PrMaChBudExpNoYrs1 = inputParam[28];
        String PrMaChBudEXp1 = inputParam[29];
        String PrMaChCurrEXp1 = inputParam[30];


        //Child 2
        String PrEduChAge2 = inputParam[16];
        String PrEduChBudExpNoYrs2 = inputParam[17];
        String PrEduChCurrEXp2 = inputParam[19];
        String PrEduChBudEXp2 = inputParam[18];

        String PrMaChBudExpNoYrs2 = inputParam[31];
        String PrMaChBudEXp2 = inputParam[32];
        String PrMaChCurrEXp2 = inputParam[33];

        //Child 3
        String PrEduChAge3 = inputParam[20];
        String PrEduChBudExpNoYrs3 = inputParam[21];
        String PrEduChCurrEXp3 = inputParam[23];
        String PrEduChBudEXp3 = inputParam[22];

        String PrMaChBudExpNoYrs3 = inputParam[34];
        String PrMaChBudEXp3 = inputParam[35];
        String PrMaChCurrEXp3 = inputParam[36];


        /************************************************************* Child 1 start ****************************************/
        /************Education Section *********/

        // Corpus Required for Education
        totCorpReqEdu = calcFV(InflationRt, Integer.parseInt(PrEduChBudExpNoYrs1) - Integer.parseInt(PrEduChAge1), Double.parseDouble(PrEduChCurrEXp1));

        retVal[0] = Math.round(totCorpReqEdu);

        //System.out.println("Total Corpus req" + totCorpReqEdu);
        //FV of Current Savings

        currSavingsEdu = calcFV(riskAppetite, Integer.parseInt(PrEduChBudExpNoYrs1) - Integer.parseInt(PrEduChAge1), Double.parseDouble(PrEduChBudEXp1));
        retVal[1] = Math.round(currSavingsEdu);
        // System.out.println("Current Sav" + currSavingsEdu);


        //Gap
        tmpGap1 = totCorpReqEdu - currSavingsEdu;

        if (tmpGap1 < 0) {
            retVal[2] = 0;
        } else {
            retVal[2] = Math.round(tmpGap1);
        }

        //      		   System.out.println("Gap " + tmpGap1);
        //Monthly Investment required

        ValPMT = PMT(riskAppettiteMon / 12, Integer.parseInt(PrEduChBudExpNoYrs1) * 12, 0, tmpGap1, 0);
        retVal[3] = Math.round(ValPMT);

        //      		   System.out.println("Monthly Investment" + ValPMT);

        /*****************End Education Sction*****/ /////////////////////////////

        // Start Marriage Section first Child //
        //Tot Corpus Required for Marriage
        totCorpReqMar = calcFV(InflationRt, Integer.parseInt(PrMaChBudExpNoYrs1) - Integer.parseInt(PrEduChAge1), Double.parseDouble(PrMaChCurrEXp1));
        retVal[4] = Math.round(totCorpReqMar);

        //      		   System.out.println("tot corp for Mar" + totCorpReqMar);

        //FV of Current Savings
        currSavingsMar = calcFV(riskAppetite, Integer.parseInt(PrMaChBudExpNoYrs1) - Integer.parseInt(PrEduChAge1), Double.parseDouble(PrMaChBudEXp1));
        retVal[5] = Math.round(currSavingsMar);

        //      		   System.out.println("Curr sav marr" + currSavingsMar);
        //Gap
        tmpGap2 = 0;
        tmpGap2 = totCorpReqMar - currSavingsMar;

        //      		   System.out.println("Gap in marr" + tmpGap2);

        if (tmpGap1 < 0)
            retVal[6] = 0;
        else
            retVal[6] = Math.round(tmpGap2);


        // Monthly Investment required
        ValPMT1 = 0;
        ValPMT1 = PMT(riskAppettiteMon / 12, Integer.parseInt(PrMaChBudExpNoYrs1) * 12, 0, tmpGap2, 1);
        retVal[7] = Math.round(ValPMT1);

        //      		   System.out.println("Monthly inv marr" + ValPMT1);


        /*******************************************  Child  1 end **************************************/


        /************************************************************* Child 2 start ****************************************/
        /************Education Section *********/

        // Corpus Required for Education
        totCorpReqEdu = calcFV(InflationRt, Integer.parseInt(PrEduChBudExpNoYrs2) - Integer.parseInt(PrEduChAge2), Double.parseDouble(PrEduChCurrEXp2));

        retVal[8] = Math.round(totCorpReqEdu);

        //System.out.println("Total Corpus req" + totCorpReqEdu);
        //FV of Current Savings

        currSavingsEdu = calcFV(riskAppetite, Integer.parseInt(PrEduChBudExpNoYrs2) - Integer.parseInt(PrEduChAge2), Double.parseDouble(PrEduChBudEXp2));
        retVal[9] = Math.round(currSavingsEdu);
        // System.out.println("Current Sav" + currSavingsEdu);


        //Gap
        tmpGap1 = totCorpReqEdu - currSavingsEdu;

        if (tmpGap1 < 0) {
            retVal[10] = 0;
        } else {
            retVal[10] = Math.round(tmpGap1);
        }

        //      		   System.out.println("Gap " + tmpGap1);
        //Monthly Investment required

        ValPMT = PMT(riskAppettiteMon / 12, Integer.parseInt(PrEduChBudExpNoYrs2) * 12, 0, tmpGap1, 0);
        retVal[11] = Math.round(ValPMT);

        //      		   System.out.println("Monthly Investment" + ValPMT);

        /*****************End Education Sction*****/ /////////////////////////////

        // Start Marriage Section first Child //
        //Tot Corpus Required for Marriage
        totCorpReqMar = calcFV(InflationRt, Integer.parseInt(PrMaChBudExpNoYrs2) - Integer.parseInt(PrEduChAge2), Double.parseDouble(PrMaChCurrEXp2));
        retVal[12] = Math.round(totCorpReqMar);

        //      		   System.out.println("tot corp for Mar" + totCorpReqMar);

        //FV of Current Savings
        currSavingsMar = calcFV(riskAppetite, Integer.parseInt(PrMaChBudExpNoYrs2) - Integer.parseInt(PrEduChAge2), Double.parseDouble(PrMaChBudEXp2));
        retVal[13] = Math.round(currSavingsMar);

        //      		   System.out.println("Curr sav marr" + currSavingsMar);
        //Gap
        tmpGap2 = 0;
        tmpGap2 = totCorpReqMar - currSavingsMar;

        //      		   System.out.println("Gap in marr" + tmpGap2);

        if (tmpGap1 < 0)
            retVal[14] = 0;
        else
            retVal[14] = Math.round(tmpGap2);


        // Monthly Investment required
        ValPMT1 = 0;
        ValPMT1 = PMT(riskAppettiteMon / 12, Integer.parseInt(PrMaChBudExpNoYrs2) * 12, 0, tmpGap2, 1);
        retVal[15] = Math.round(ValPMT1);

        //      		   System.out.println("Monthly inv marr" + ValPMT1);


        /*******************************************  Child 2 end **************************************/


        /************************************************************* Child 3 start ****************************************/
        /************Education Section *********/

        // Corpus Required for Education
        totCorpReqEdu = calcFV(InflationRt, Integer.parseInt(PrEduChBudExpNoYrs3) - Integer.parseInt(PrEduChAge3), Double.parseDouble(PrEduChCurrEXp3));

        retVal[16] = Math.round(totCorpReqEdu);

        //System.out.println("Total Corpus req" + totCorpReqEdu);
        //FV of Current Savings

        currSavingsEdu = calcFV(riskAppetite, Integer.parseInt(PrEduChBudExpNoYrs3) - Integer.parseInt(PrEduChAge3), Double.parseDouble(PrEduChBudEXp3));
        retVal[17] = Math.round(currSavingsEdu);
        // System.out.println("Current Sav" + currSavingsEdu);


        //Gap
        tmpGap1 = totCorpReqEdu - currSavingsEdu;

        if (tmpGap1 < 0) {
            retVal[18] = 0;
        } else {
            retVal[18] = Math.round(tmpGap1);
        }

        //      		   System.out.println("Gap " + tmpGap1);
        //Monthly Investment required

        ValPMT = PMT(riskAppettiteMon / 12, Integer.parseInt(PrEduChBudExpNoYrs3) * 12, 0, tmpGap1, 0);
        retVal[19] = Math.round(ValPMT);

        //      		   System.out.println("Monthly Investment" + ValPMT);

        /*****************End Education Sction*****/ /////////////////////////////

        // Start Marriage Section first Child //
        //Tot Corpus Required for Marriage
        totCorpReqMar = calcFV(InflationRt, Integer.parseInt(PrMaChBudExpNoYrs3) - Integer.parseInt(PrEduChAge3), Double.parseDouble(PrMaChCurrEXp3));
        retVal[20] = Math.round(totCorpReqMar);

        //      		   System.out.println("tot corp for Mar" + totCorpReqMar);

        //FV of Current Savings
        currSavingsMar = calcFV(riskAppetite, Integer.parseInt(PrMaChBudExpNoYrs3) - Integer.parseInt(PrEduChAge3), Double.parseDouble(PrMaChBudEXp3));
        retVal[21] = Math.round(currSavingsMar);

        //      		   System.out.println("Curr sav marr" + currSavingsMar);
        //Gap
        tmpGap2 = 0;
        tmpGap2 = totCorpReqMar - currSavingsMar;

        //      		   System.out.println("Gap in marr" + tmpGap2);

        if (tmpGap1 < 0)
            retVal[22] = 0;
        else
            retVal[22] = Math.round(tmpGap2);


        // Monthly Investment required
        ValPMT1 = 0;
        ValPMT1 = PMT(riskAppettiteMon / 12, Integer.parseInt(PrMaChBudExpNoYrs3) * 12, 0, tmpGap2, 1);
        retVal[23] = Math.round(ValPMT1);

        //      		   System.out.println("Monthly inv marr" + ValPMT1);


        /*******************************************  Child 3 end **************************************/


        return retVal;

    }


    private double[] getFourChildFuturPlanning(double InflationRt, String[] inputParam, double riskAppetite, double riskAppettiteMon) {

        double totCorpReqEdu = 0, currSavingsEdu = 0, tmpGap1 = 0, ValPMT = 0;
        double totCorpReqMar = 0, currSavingsMar = 0, tmpGap2 = 0, ValPMT1 = 0;
        double[] retVal = new double[32];


        for (int i = 0; i < retVal.length; i++) {
            retVal[i] = 0;
        }

        //Child 1
        String PrEduChAge1 = inputParam[12];
        String PrEduChBudExpNoYrs1 = inputParam[13];
        String PrEduChCurrEXp1 = inputParam[15];
        String PrEduChBudEXp1 = inputParam[14];

        String PrMaChBudExpNoYrs1 = inputParam[28];
        String PrMaChBudEXp1 = inputParam[29];
        String PrMaChCurrEXp1 = inputParam[30];


        //Child 2
        String PrEduChAge2 = inputParam[16];
        String PrEduChBudExpNoYrs2 = inputParam[17];
        String PrEduChCurrEXp2 = inputParam[19];
        String PrEduChBudEXp2 = inputParam[18];

        String PrMaChBudExpNoYrs2 = inputParam[31];
        String PrMaChBudEXp2 = inputParam[32];
        String PrMaChCurrEXp2 = inputParam[33];

        //Child 3
        String PrEduChAge3 = inputParam[20];
        String PrEduChBudExpNoYrs3 = inputParam[21];
        String PrEduChCurrEXp3 = inputParam[23];
        String PrEduChBudEXp3 = inputParam[22];

        String PrMaChBudExpNoYrs3 = inputParam[34];
        String PrMaChBudEXp3 = inputParam[35];
        String PrMaChCurrEXp3 = inputParam[36];


        //Child 4
        String PrEduChAge4 = inputParam[24];
        String PrEduChBudExpNoYrs4 = inputParam[25];
        String PrEduChCurrEXp4 = inputParam[27];
        String PrEduChBudEXp4 = inputParam[26];

        String PrMaChBudExpNoYrs4 = inputParam[37];
        String PrMaChBudEXp4 = inputParam[38];
        String PrMaChCurrEXp4 = inputParam[39];


        /************************************************************* Child 1 start ****************************************/
        /************Education Section *********/

        // Corpus Required for Education
        totCorpReqEdu = calcFV(InflationRt, Integer.parseInt(PrEduChBudExpNoYrs1) - Integer.parseInt(PrEduChAge1), Double.parseDouble(PrEduChCurrEXp1));

        retVal[0] = Math.round(totCorpReqEdu);

        //System.out.println("Total Corpus req" + totCorpReqEdu);
        //FV of Current Savings

        currSavingsEdu = calcFV(riskAppetite, Integer.parseInt(PrEduChBudExpNoYrs1) - Integer.parseInt(PrEduChAge1), Double.parseDouble(PrEduChBudEXp1));
        retVal[1] = Math.round(currSavingsEdu);
        // System.out.println("Current Sav" + currSavingsEdu);


        //Gap
        tmpGap1 = totCorpReqEdu - currSavingsEdu;

        if (tmpGap1 < 0) {
            retVal[2] = 0;
        } else {
            retVal[2] = Math.round(tmpGap1);
        }

        //  		   System.out.println("Gap " + tmpGap1);
        //Monthly Investment required

        ValPMT = PMT(riskAppettiteMon / 12, Integer.parseInt(PrEduChBudExpNoYrs1) * 12, 0, tmpGap1, 0);
        retVal[3] = Math.round(ValPMT);

        //  		   System.out.println("Monthly Investment" + ValPMT);

        /*****************End Education Sction*****/ /////////////////////////////

        // Start Marriage Section first Child //
        //Tot Corpus Required for Marriage
        totCorpReqMar = calcFV(InflationRt, Integer.parseInt(PrMaChBudExpNoYrs1) - Integer.parseInt(PrEduChAge1), Double.parseDouble(PrMaChCurrEXp1));
        retVal[4] = Math.round(totCorpReqMar);

        //  		   System.out.println("tot corp for Mar" + totCorpReqMar);

        //FV of Current Savings
        currSavingsMar = calcFV(riskAppetite, Integer.parseInt(PrMaChBudExpNoYrs1) - Integer.parseInt(PrEduChAge1), Double.parseDouble(PrMaChBudEXp1));
        retVal[5] = Math.round(currSavingsMar);

        //  		   System.out.println("Curr sav marr" + currSavingsMar);
        //Gap
        tmpGap2 = 0;
        tmpGap2 = totCorpReqMar - currSavingsMar;

        //  		   System.out.println("Gap in marr" + tmpGap2);

        if (tmpGap1 < 0)
            retVal[6] = 0;
        else
            retVal[6] = Math.round(tmpGap2);


        // Monthly Investment required
        ValPMT1 = 0;
        ValPMT1 = PMT(riskAppettiteMon / 12, Integer.parseInt(PrMaChBudExpNoYrs1) * 12, 0, tmpGap2, 1);
        retVal[7] = Math.round(ValPMT1);

        //  		   System.out.println("Monthly inv marr" + ValPMT1);


        /*******************************************  Child  1 end **************************************/


        /************************************************************* Child 2 start ****************************************/
        /************Education Section *********/

        // Corpus Required for Education
        totCorpReqEdu = calcFV(InflationRt, Integer.parseInt(PrEduChBudExpNoYrs2) - Integer.parseInt(PrEduChAge2), Double.parseDouble(PrEduChCurrEXp2));

        retVal[8] = Math.round(totCorpReqEdu);

        //System.out.println("Total Corpus req" + totCorpReqEdu);
        //FV of Current Savings

        currSavingsEdu = calcFV(riskAppetite, Integer.parseInt(PrEduChBudExpNoYrs2) - Integer.parseInt(PrEduChAge2), Double.parseDouble(PrEduChBudEXp2));
        retVal[9] = Math.round(currSavingsEdu);
        // System.out.println("Current Sav" + currSavingsEdu);


        //Gap
        tmpGap1 = totCorpReqEdu - currSavingsEdu;

        if (tmpGap1 < 0) {
            retVal[10] = 0;
        } else {
            retVal[10] = Math.round(tmpGap1);
        }

        //  		   System.out.println("Gap " + tmpGap1);
        //Monthly Investment required

        ValPMT = PMT(riskAppettiteMon / 12, Integer.parseInt(PrEduChBudExpNoYrs2) * 12, 0, tmpGap1, 0);
        retVal[11] = Math.round(ValPMT);

        //  		   System.out.println("Monthly Investment" + ValPMT);

        /*****************End Education Sction*****/ /////////////////////////////

        // Start Marriage Section first Child //
        //Tot Corpus Required for Marriage
        totCorpReqMar = calcFV(InflationRt, Integer.parseInt(PrMaChBudExpNoYrs2) - Integer.parseInt(PrEduChAge2), Double.parseDouble(PrMaChCurrEXp2));
        retVal[12] = Math.round(totCorpReqMar);

        //  		   System.out.println("tot corp for Mar" + totCorpReqMar);

        //FV of Current Savings
        currSavingsMar = calcFV(riskAppetite, Integer.parseInt(PrMaChBudExpNoYrs2) - Integer.parseInt(PrEduChAge2), Double.parseDouble(PrMaChBudEXp2));
        retVal[13] = Math.round(currSavingsMar);

        //  		   System.out.println("Curr sav marr" + currSavingsMar);
        //Gap
        tmpGap2 = 0;
        tmpGap2 = totCorpReqMar - currSavingsMar;

        //  		   System.out.println("Gap in marr" + tmpGap2);

        if (tmpGap1 < 0)
            retVal[14] = 0;
        else
            retVal[14] = Math.round(tmpGap2);


        // Monthly Investment required
        ValPMT1 = 0;
        ValPMT1 = PMT(riskAppettiteMon / 12, Integer.parseInt(PrMaChBudExpNoYrs2) * 12, 0, tmpGap2, 1);
        retVal[15] = Math.round(ValPMT1);

        //  		   System.out.println("Monthly inv marr" + ValPMT1);


        /*******************************************  Child 2 end **************************************/


        /************************************************************* Child 3 start ****************************************/
        /************Education Section *********/

        // Corpus Required for Education
        totCorpReqEdu = calcFV(InflationRt, Integer.parseInt(PrEduChBudExpNoYrs3) - Integer.parseInt(PrEduChAge3), Double.parseDouble(PrEduChCurrEXp3));

        retVal[16] = Math.round(totCorpReqEdu);

        //System.out.println("Total Corpus req" + totCorpReqEdu);
        //FV of Current Savings

        currSavingsEdu = calcFV(riskAppetite, Integer.parseInt(PrEduChBudExpNoYrs3) - Integer.parseInt(PrEduChAge3), Double.parseDouble(PrEduChBudEXp3));
        retVal[17] = Math.round(currSavingsEdu);
        // System.out.println("Current Sav" + currSavingsEdu);


        //Gap
        tmpGap1 = totCorpReqEdu - currSavingsEdu;

        if (tmpGap1 < 0) {
            retVal[18] = 0;
        } else {
            retVal[18] = Math.round(tmpGap1);
        }

        //  		   System.out.println("Gap " + tmpGap1);
        //Monthly Investment required

        ValPMT = PMT(riskAppettiteMon / 12, Integer.parseInt(PrEduChBudExpNoYrs3) * 12, 0, tmpGap1, 0);
        retVal[19] = Math.round(ValPMT);

        //  		   System.out.println("Monthly Investment" + ValPMT);

        /*****************End Education Sction*****/ /////////////////////////////

        // Start Marriage Section first Child //
        //Tot Corpus Required for Marriage
        totCorpReqMar = calcFV(InflationRt, Integer.parseInt(PrMaChBudExpNoYrs3) - Integer.parseInt(PrEduChAge3), Double.parseDouble(PrMaChCurrEXp3));
        retVal[20] = Math.round(totCorpReqMar);

        //  		   System.out.println("tot corp for Mar" + totCorpReqMar);

        //FV of Current Savings
        currSavingsMar = calcFV(riskAppetite, Integer.parseInt(PrMaChBudExpNoYrs3) - Integer.parseInt(PrEduChAge3), Double.parseDouble(PrMaChBudEXp3));
        retVal[21] = Math.round(currSavingsMar);

        //  		   System.out.println("Curr sav marr" + currSavingsMar);
        //Gap
        tmpGap2 = 0;
        tmpGap2 = totCorpReqMar - currSavingsMar;

        //  		   System.out.println("Gap in marr" + tmpGap2);

        if (tmpGap1 < 0)
            retVal[22] = 0;
        else
            retVal[22] = Math.round(tmpGap2);


        // Monthly Investment required
        ValPMT1 = 0;
        ValPMT1 = PMT(riskAppettiteMon / 12, Integer.parseInt(PrMaChBudExpNoYrs3) * 12, 0, tmpGap2, 1);
        retVal[23] = Math.round(ValPMT1);

        //  		   System.out.println("Monthly inv marr" + ValPMT1);


        /*******************************************  Child 3 end **************************************/


        /************************************************************* Child 4 start ****************************************/
        /************Education Section *********/

        // Corpus Required for Education
        totCorpReqEdu = calcFV(InflationRt, Integer.parseInt(PrEduChBudExpNoYrs4) - Integer.parseInt(PrEduChAge4), Double.parseDouble(PrEduChCurrEXp4));

        retVal[24] = Math.round(totCorpReqEdu);

        //System.out.println("Total Corpus req" + totCorpReqEdu);
        //FV of Current Savings

        currSavingsEdu = calcFV(riskAppetite, Integer.parseInt(PrEduChBudExpNoYrs4) - Integer.parseInt(PrEduChAge4), Double.parseDouble(PrEduChBudEXp4));
        retVal[25] = Math.round(currSavingsEdu);
        // System.out.println("Current Sav" + currSavingsEdu);


        //Gap
        tmpGap1 = totCorpReqEdu - currSavingsEdu;

        if (tmpGap1 < 0) {
            retVal[26] = 0;
        } else {
            retVal[26] = Math.round(tmpGap1);
        }

        //  		   System.out.println("Gap " + tmpGap1);
        //Monthly Investment required

        ValPMT = PMT(riskAppettiteMon / 12, Integer.parseInt(PrEduChBudExpNoYrs4) * 12, 0, tmpGap1, 0);
        retVal[27] = Math.round(ValPMT);

        //  		   System.out.println("Monthly Investment" + ValPMT);

        /*****************End Education Sction*****/ /////////////////////////////

        // Start Marriage Section first Child //
        //Tot Corpus Required for Marriage
        totCorpReqMar = calcFV(InflationRt, Integer.parseInt(PrMaChBudExpNoYrs4) - Integer.parseInt(PrEduChAge4), Double.parseDouble(PrMaChCurrEXp4));
        retVal[28] = Math.round(totCorpReqMar);

        //  		   System.out.println("tot corp for Mar" + totCorpReqMar);

        //FV of Current Savings
        currSavingsMar = calcFV(riskAppetite, Integer.parseInt(PrMaChBudExpNoYrs4) - Integer.parseInt(PrEduChAge4), Double.parseDouble(PrMaChBudEXp4));
        retVal[29] = Math.round(currSavingsMar);

        //  		   System.out.println("Curr sav marr" + currSavingsMar);
        //Gap
        tmpGap2 = 0;
        tmpGap2 = totCorpReqMar - currSavingsMar;

        //  		   System.out.println("Gap in marr" + tmpGap2);

        if (tmpGap1 < 0)
            retVal[30] = 0;
        else
            retVal[30] = Math.round(tmpGap2);


        // Monthly Investment required
        ValPMT1 = 0;
        ValPMT1 = PMT(riskAppettiteMon / 12, Integer.parseInt(PrMaChBudExpNoYrs4) * 12, 0, tmpGap2, 1);
        retVal[31] = Math.round(ValPMT1);

        //  		   System.out.println("Monthly inv marr" + ValPMT1);


        /*******************************************  Child 4 end **************************************/


        return retVal;


    }


    public double[] getChildFuturePlanning(double InflationRt, String[] inputParam, double riskAppetite, double riskAppettiteMon) {

        //	   String []retVal = new String[32];
        //       ArrayList<String> futurePlanList = new ArrayList<String>();
        //       ArrayList<String> temp = new ArrayList<String>(Arrays.asList("0","0","0","0","0","0","0","0"));
        //
        //       if (Integer.parseInt(inputParam[3]) ==1)
        //		{
        //    	   futurePlanList.addAll(getChildFuturPlanningArr(InflationRt, riskAppetite, riskAppettiteMon,inputParam[12] , inputParam[13], inputParam[14],  inputParam[15], inputParam[28], inputParam[29], inputParam[30]));
        //    	   futurePlanList.addAll(temp);
        //    	   futurePlanList.addAll(temp);
        //    	   futurePlanList.addAll(temp);
        //
        //		}
        //       else if (Integer.parseInt(inputParam[3]) ==2)
        //       {
        //    	   futurePlanList.addAll(getChildFuturPlanningArr(InflationRt,riskAppetite, riskAppettiteMon,inputParam[12] , inputParam[13], inputParam[14],  inputParam[15], inputParam[28], inputParam[29], inputParam[30]));
        //    	   futurePlanList.addAll(getChildFuturPlanningArr(InflationRt,riskAppetite, riskAppettiteMon,inputParam[16] , inputParam[17], inputParam[18],  inputParam[19], inputParam[31], inputParam[32], inputParam[33]));
        //    	   futurePlanList.addAll(temp);
        //    	   futurePlanList.addAll(temp);
        //       }
        //       else if  (Integer.parseInt(inputParam[3]) ==3)
        //       {
        //    	   futurePlanList.addAll(getChildFuturPlanningArr(InflationRt,riskAppetite, riskAppettiteMon,inputParam[12] , inputParam[13], inputParam[14],  inputParam[15], inputParam[28], inputParam[29], inputParam[30]));
        //    	   futurePlanList.addAll(getChildFuturPlanningArr(InflationRt,riskAppetite, riskAppettiteMon,inputParam[16] , inputParam[17], inputParam[18],  inputParam[19], inputParam[31], inputParam[32], inputParam[33]));
        //    	   futurePlanList.addAll(getChildFuturPlanningArr(InflationRt,riskAppetite, riskAppettiteMon,inputParam[20] , inputParam[21], inputParam[22],  inputParam[23], inputParam[34], inputParam[35], inputParam[36]));
        //    	   futurePlanList.addAll(temp);
        //       }
        //       else if  (Integer.parseInt(inputParam[3]) ==3)
        //       {
        //    	   futurePlanList.addAll(getChildFuturPlanningArr(InflationRt,riskAppetite, riskAppettiteMon,inputParam[12] , inputParam[13], inputParam[14],  inputParam[15], inputParam[28], inputParam[29], inputParam[30]));
        //    	   futurePlanList.addAll(getChildFuturPlanningArr(InflationRt,riskAppetite, riskAppettiteMon,inputParam[16] , inputParam[17], inputParam[18],  inputParam[19], inputParam[31], inputParam[32], inputParam[33]));
        //    	   futurePlanList.addAll(getChildFuturPlanningArr(InflationRt,riskAppetite, riskAppettiteMon,inputParam[20] , inputParam[21], inputParam[22],  inputParam[23], inputParam[34], inputParam[35], inputParam[36]));
        //    	   futurePlanList.addAll(getChildFuturPlanningArr(InflationRt,riskAppetite, riskAppettiteMon,inputParam[24] , inputParam[25], inputParam[26],  inputParam[27], inputParam[37], inputParam[38], inputParam[39]));
        //       }
        //       else
        //       {
        //    	   futurePlanList.addAll(temp);
        //    	   futurePlanList.addAll(temp);
        //    	   futurePlanList.addAll(temp);
        //    	   futurePlanList.addAll(temp);
        //       }
        //	   retVal = futurePlanList.toArray(retVal);

        double[] retVal = new double[32];
        if (Integer.parseInt(inputParam[3]) == 1) {


            retVal = getOneChildFuturPlanning(InflationRt, inputParam, riskAppetite, riskAppettiteMon);
        } else if (Integer.parseInt(inputParam[3]) == 2) {
            retVal = getTwoChildFuturPlanning(InflationRt, inputParam, riskAppetite, riskAppettiteMon);

        } else if (Integer.parseInt(inputParam[3]) == 3) {

            retVal = getThreeChildFuturPlanning(InflationRt, inputParam, riskAppetite, riskAppettiteMon);


        } else if (Integer.parseInt(inputParam[3]) == 4) {
            retVal = getFourChildFuturPlanning(InflationRt, inputParam, riskAppetite, riskAppettiteMon);


        } else {
            for (int i = 0; i < retVal.length; i++) {
                retVal[i] = 0;
            }
        }

        return retVal;


    }


    public double[] getHomeFuturePlanning(double InflationRt, String[] inputParam, double riskAppetite, double riskAppettiteMon) {

        double currSavings = 0, totCorpReqHome = 0, downpay = 0, gap = 0, monthInv = 0;
        double[] retVal = new double[4];


        //Total Corpus Required

        totCorpReqHome = calcFV(InflationRt, Integer.parseInt(inputParam[40]), Integer.parseInt(inputParam[42]));
        retVal[0] = Math.round(totCorpReqHome);
        //	   System.out.println("total corpus" + totCorpReqHome);

/**
 * Modified by Akshaya as D46 value is set to 100%
 */
//		downpay = totCorpReqHome *0.2;
        //                 Down Payment Amount
        downpay = totCorpReqHome * 1;


        //  FV of Current Savings


        currSavings = calcFV(riskAppetite, Integer.parseInt(inputParam[40]), Integer.parseInt(inputParam[41]));
        retVal[1] = Math.round(currSavings);

        //	   System.out.println("curr savings" + currSavings);
        // Gap in Down payment
        gap = downpay - currSavings;

        if (gap < 0) {
            retVal[2] = 0;
        } else {
            retVal[2] = Math.round(gap);
        }

        //	   System.out.println("Gap" + gap);

        //Overall Gap in corpus (hidden field)


        //Monthly investment

        monthInv = PMT(riskAppettiteMon / 12, Integer.parseInt(inputParam[40]) * 12, 0, gap, 1);
        retVal[3] = Math.round(monthInv);

        //	   System.out.println("Month Inv" + monthInv);
        return retVal;

    }


    public double[] getOTHERFuturePlanning(double InflationRt, String[] inputParam, double riskAppetite, double riskAppettiteMon) {
        double currSavings = 0, totCorpReq = 0, downpay = 0, gap = 0, monthInv = 0;
        double[] retVal = new double[4];


        //Total Corpus Required

        totCorpReq = calcFV(InflationRt, Integer.parseInt(inputParam[43]), Integer.parseInt(inputParam[45]));
        retVal[0] = Math.round(totCorpReq);
        //    	   System.out.println("total corpus othr" + totCorpReq);


        //                 Down Payment Amount
        downpay = totCorpReq * 1;


        //  FV of Current Savings


        currSavings = calcFV(riskAppetite, Integer.parseInt(inputParam[43]), Integer.parseInt(inputParam[44]));
        retVal[1] = Math.round(currSavings);

        //    	   System.out.println("curr savings othr" + currSavings);
        // Gap in Down payment
        gap = downpay - currSavings;

        if (gap < 0) {
            retVal[2] = 0;
        } else {
            retVal[2] = Math.round(gap);
        }

        //    	   System.out.println("Gap othr" + gap);


        monthInv = PMT(riskAppettiteMon / 12, Integer.parseInt(inputParam[43]) * 12, 0, gap, 1);
        retVal[3] = Math.round(monthInv);


        //    	   System.out.println("Month Inv othr" + monthInv);
        return retVal;

    }


    public double getRound(double val) {
        return Math.round(val);
    }


    // End 3rd April

}  

