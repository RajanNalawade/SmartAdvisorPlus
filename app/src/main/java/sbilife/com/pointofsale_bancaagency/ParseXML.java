package sbilife.com.pointofsale_bancaagency;

/*
 * whatever xml data come from sever it will parse here
 */

import android.graphics.Color;
import android.text.TextUtils;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import sbilife.com.pointofsale_bancaagency.common.CommonForAllProd;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.home.clsHOLead;



public class ParseXML implements Serializable {

    public static void main(String[] args) {

        ParseXML prsObj = new ParseXML();

        String input = "<cif> <table> <id>1</id> <name>test</name>"

                + "<address>test</address></table><table><id>2</id><name>test2</name>"

                + "<address>test2</address></table><table><id>3</id><name>test3</name><address>test3</address></table>"

                + "</cif>";

        // We Can Ignore The first two Tag since they r Fixed thats why removing
        // that two tag

        input = prsObj.parseXmlTag(input, "cif");

        // Now the remaining list is "<table> <id>1</id> <name>test</name>

        // +
        // "<address>test</address></table><table><id>2</id><name>test2</name>"

        // +
        // "<address>test2</address></table><table><id>3</id><name>test3</name><address>test3</address>""

        List<String> Node = prsObj.parseParentNode(input, "table");

        List<XMLHolder> nodeData = prsObj.parseNodeElement(Node);

        // Now we can easily get the value of each node

        for (XMLHolder node : nodeData) {

            System.out.println(node.getNo());

            System.out.println(node.getFName());

            System.out.println(node.getLName());

            System.out.println(node.getStatus());

            System.out.println(node.getPremiumUp());

        }

    }

    public List<XMLHolder> parseNodeElement(List<String> lsNode) {

        List<XMLHolder> lsData = new ArrayList<XMLHolder>();

        for (String Node : lsNode) {

            String No = parseXmlTag(Node, "POLICYNUMBER");

            String Holderid = parseXmlTag(Node, "HOLDERID");

            String fname = parseXmlTag(Node, "HOLDERPERSONFIRSTNAME");

            String lname = parseXmlTag(Node, "HOLDERPERSONLASTNAME");

            String productname = parseXmlTag(Node, "PRODUCTNAME");

            String policytype = parseXmlTag(Node, "POLICYTYPE");

            String policypaymentterm = parseXmlTag(Node, "POLICYPAYMENTTERM");

            String policysumassured = parseXmlTag(Node, "POLICYSUMASSURED");

            String premfre = parseXmlTag(Node, "PREMIUMPAYMENTFREQUENCY");

            String grossamt = parseXmlTag(Node, "PREMIUMGROSSAMOUNT");

            String status = parseXmlTag(Node, "POLICYCURRENTSTATUS");

            String premiumup = parseXmlTag(Node, "PREMIUMFUP");

            if (premiumup == null) {
                premiumup = "";
            } else {

                premiumup = premiumup.split("T")[0];

                Date dt1 = null;
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat df1 = new SimpleDateFormat("dd-MMMM-yyyy");
                try {
                    dt1 = df.parse(premiumup);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                premiumup = df1.format(dt1);

            }

            XMLHolder nodeVal = new XMLHolder(No, Holderid, fname, lname,
                    productname, policytype, policypaymentterm,
                    policysumassured, premfre, grossamt, status, premiumup);

            lsData.add(nodeVal);

        }

        return lsData;

    }

    public List<XMLDOCREQList> parseNodeElement_DOCREQ(List<String> lsNode) {

        List<XMLDOCREQList> lsData_req = new ArrayList<XMLDOCREQList>();

        for (String Node : lsNode) {

            String desc = parseXmlTag(Node, "DESCRIPTION");

            String reqflag = parseXmlTag(Node, "REQUIREMENTFLAG");
            String propStatus;
            if (parseXmlTag(Node, "PropStatus") == null)
                propStatus = "";
            else
                propStatus = parseXmlTag(Node, "PropStatus");

            XMLDOCREQList nodeVal = new XMLDOCREQList(desc, reqflag, propStatus);

            lsData_req.add(nodeVal);

        }

        return lsData_req;

    }

    public List<XMLHolderMaturity> parseNodeElementMaturity(List<String> lsNode) {

        List<XMLHolderMaturity> lsData = new ArrayList<XMLHolderMaturity>();

        for (String Node : lsNode) {

            /*
             * <Table> <POLICYNUMBER>17000231110</POLICYNUMBER>
             * <POLNUM>17000231110</POLNUM>
             * <Customer_x0020_Code>629233</Customer_x0020_Code>
             * <LIFEASSUREDNAME>Moinul Hoque</LIFEASSUREDNAME>
             * <STATUS>Lapse</STATUS>
             * <Benefit_x0020_Term>12</Benefit_x0020_Term>
             * <POLICYRISKCOMMENCEMENTDATE
             * >2005-02-10T00:00:00-08:00</POLICYRISKCOMMENCEMENTDATE>
             * <MATURITYDATE>2017-02-10T00:00:00-08:00</MATURITYDATE>
             * <PAYMENTAMOUNT>25166</PAYMENTAMOUNT> <First_x0020_Name>Moinul
             * Hoque</First_x0020_Name> <DOB>1965-02-01T00:00:00-08:00</DOB>
             * <CITY>KARIMGANJ</CITY> <ADDRESS>VILL. KALKALIBASTI PO. CHANDKHIRA
             * DIST. KARIMGANJ</ADDRESS>
             * <Postal_x0020_Code>788725</Postal_x0020_Code>
             * <STATE>Assam</STATE>
             * CONTACT_NO_R >03673239260<
             * /CONTACT_NO_R > </Table>
             */

            String policyNumber = parseXmlTag(Node, "POLICYNUMBER");
            String customerCode = parseXmlTag(Node, "Customer_x0020_Code");
            String lifeAssuredName = parseXmlTag(Node, "LIFEASSUREDNAME");
            String status = parseXmlTag(Node, "STATUS");
            String benefitTerm = parseXmlTag(Node, "Benefit_x0020_Term");
            String policyRiskCommencementDate = parseXmlTag(Node,
                    "POLICYRISKCOMMENCEMENTDATE");

            String maturityDate = parseXmlTag(Node, "MATURITYDATE");
            String paymentAmount = parseXmlTag(Node, "PAYMENTAMOUNT");
            String firstName = parseXmlTag(Node, "First_x0020_Name");
            String dOB = parseXmlTag(Node, "DOB");
            String city = parseXmlTag(Node, "CITY");
            String address = parseXmlTag(Node, "ADDRESS");
            String postalCode = parseXmlTag(Node, "Postal_x0020_Code");
            String state = parseXmlTag(Node, "STATE");
            String contactNumber = parseXmlTag(Node,
                    "CONTACT_NO_R");

            /*
             * String No = parseXmlTag(Node, "POLICYNUMBER");
             *
             * String Holderid = parseXmlTag(Node, "HOLDERID");
             *
             * String fname = parseXmlTag(Node, "HOLDERPERSONFIRSTNAME");
             *
             * String lname = parseXmlTag(Node, "HOLDERPERSONLASTNAME");
             *
             * String status = parseXmlTag(Node, "POLICYCURRENTSTATUS");
             *
             * String benefit = parseXmlTag(Node, "POLICYBENEFITTERM");
             *
             * String riskdate = parseXmlTag(Node,
             * "POLICYRISKCOMMENCEMENTDATE");
             */

            // String maturitydate = parseXmlTag(Node, "MaturityDate");

            /*
             * String payamt = parseXmlTag(Node, "PAYMENTAMOUNT"); String
             * paytype = parseXmlTag(Node, "PAYMENTTYPE");
             *
             * if (riskdate == null) { riskdate = ""; } else {
             *
             * riskdate = riskdate.split("T")[0];
             *
             * Date dt1 = null; SimpleDateFormat df = new
             * SimpleDateFormat("yyyy-MM-dd"); SimpleDateFormat df1 = new
             * SimpleDateFormat("dd-MMMM-yyyy"); try { dt1 = (Date)
             * df.parse(riskdate); } catch (ParseException e) { // TODO
             * Auto-generated catch block e.printStackTrace(); } riskdate =
             * df1.format(dt1);
             *
             * }
             */
            SimpleDateFormat dfmaturity = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat df1maturity = new SimpleDateFormat("dd-MMMM-yyyy");
            if (policyRiskCommencementDate == null) {
                policyRiskCommencementDate = "";
            } else {

                policyRiskCommencementDate = policyRiskCommencementDate
                        .split("T")[0];

                Date dt1maturity = null;

                try {
                    dt1maturity = dfmaturity
                            .parse(policyRiskCommencementDate);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                policyRiskCommencementDate = df1maturity.format(dt1maturity);

            }

            if (maturityDate == null) {
                maturityDate = "";
            } else {

                maturityDate = maturityDate.split("T")[0];

                Date dt1maturity = null;

                try {
                    dt1maturity = dfmaturity.parse(maturityDate);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                maturityDate = df1maturity.format(dt1maturity);

            }
            if (dOB == null) {
                dOB = "";
            } else {

                dOB = dOB.split("T")[0];

                Date dt1maturity = null;

                try {
                    dt1maturity = dfmaturity.parse(dOB);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                dOB = df1maturity.format(dt1maturity);
            }
            /*
             * String strPayType = "";
             *
             * if (paytype == null) { strPayType = ""; }
             *
             * if(paytype.contentEquals("Money IN")) { strPayType = "Paid"; }
             * else if(paytype.contentEquals("Money OUT")) { strPayType =
             * "Unpaid"; }
             */

            XMLHolderMaturity nodeVal = new XMLHolderMaturity(policyNumber,
                    customerCode, lifeAssuredName, status, benefitTerm,
                    policyRiskCommencementDate, maturityDate, paymentAmount,
                    firstName, dOB, city, address, postalCode, state,
                    contactNumber);

            lsData.add(nodeVal);

        }

        return lsData;

    }

    public List<XMLHolderSurrender> parseNodeElementSurrender(
            List<String> lsNode) {

        List<XMLHolderSurrender> lsData = new ArrayList<XMLHolderSurrender>();

        for (String Node : lsNode) {

            String No = parseXmlTag(Node, "POLICYNUMBER");

            String Holderid = parseXmlTag(Node, "HOLDERID");

            String fname = parseXmlTag(Node, "HOLDERPERSONFIRSTNAME");

            String lname = parseXmlTag(Node, "HOLDERPERSONLASTNAME");

            String status = parseXmlTag(Node, "STATUSWIDTHDRAWALEFFECTDATE");

            if (status == null) {
                status = "";
            } else {

                status = status.split("T")[0];

                Date dt1 = null;
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat df1 = new SimpleDateFormat("dd-MMMM-yyyy");
                try {
                    dt1 = df.parse(status);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                status = df1.format(dt1);

            }

            XMLHolderSurrender nodeVal = new XMLHolderSurrender(No, Holderid,
                    fname, lname, status);

            lsData.add(nodeVal);

        }

        return lsData;

    }

    public List<XMLHolderRevival> parseNodeElementRevival(List<String> lsNode) {

        List<XMLHolderRevival> lsData = new ArrayList<XMLHolderRevival>();

        for (String Node : lsNode) {

            String No = parseXmlTag(Node, "POLICYNUMBER");

            String Holderid = parseXmlTag(Node, "HOLDERID");

            String fname = parseXmlTag(Node, "HOLDERPERSONFIRSTNAME");

            String lname = parseXmlTag(Node, "HOLDERPERSONLASTNAME");

            // String status = parseXmlTag(Node, "POLICYCURRENTSTATUS");

            // String premiumup = parseXmlTag(Node, "PREMIUMFUP");
            String premiumup = parseXmlTag(Node, "REVIVALDATE");

            if (premiumup == null) {
                premiumup = "";
            } else {

                premiumup = premiumup.split("T")[0];

                Date dt1 = null;
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat df1 = new SimpleDateFormat("dd-MMMM-yyyy");
                try {
                    dt1 = df.parse(premiumup);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                premiumup = df1.format(dt1);

            }

            XMLHolderRevival nodeVal = new XMLHolderRevival(No, Holderid,
                    fname, lname, premiumup);

            lsData.add(nodeVal);

        }

        return lsData;

    }

    public List<XMLHolderRenewal> parseNodeElementRenewal(List<String> lsNode) {

        List<XMLHolderRenewal> lsData = new ArrayList<XMLHolderRenewal>();

        for (String Node : lsNode) {

            String No = parseXmlTag(Node, "POLICYNUMBER");

            String Holderid = parseXmlTag(Node, "HOLDERID");

            String fname = parseXmlTag(Node, "HOLDERPERSONFIRSTNAME");

            String lname = parseXmlTag(Node, "HOLDERPERSONLASTNAME");

            String status = parseXmlTag(Node, "POLICYCURRENTSTATUS");

            String premiumup = parseXmlTag(Node, "PREMIUMFUP");

            String premiumamt = parseXmlTag(Node, "PREMIUMGROSSAMOUNT");

            String paytype = parseXmlTag(Node, "PAYMENTTYPE");

            String CONTACTMOBILE = parseXmlTag(Node,
                    "CONTACTMOBILE");

            String POLICYPAYMENTMECHANISM = parseXmlTag(Node, "POLICYPAYMENTMECHANISM");
            POLICYPAYMENTMECHANISM = POLICYPAYMENTMECHANISM == null ? "" : POLICYPAYMENTMECHANISM;

            String EMAILID = parseXmlTag(Node, "EMAILID");
            EMAILID = EMAILID == null ? "" : EMAILID;

            String POLICYRISKCOMMENCEMENTDATE = parseXmlTag(Node, "POLICYRISKCOMMENCEMENTDATE");
            POLICYRISKCOMMENCEMENTDATE = POLICYRISKCOMMENCEMENTDATE == null ? "" : POLICYRISKCOMMENCEMENTDATE;

            POLICYRISKCOMMENCEMENTDATE = POLICYRISKCOMMENCEMENTDATE.split("T")[0];

            Date dateFinal = null;
            SimpleDateFormat dateExist = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat dateNew = new SimpleDateFormat("dd-MMMM-yyyy");
            try {
                dateFinal = dateExist.parse(POLICYRISKCOMMENCEMENTDATE);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            POLICYRISKCOMMENCEMENTDATE = dateNew.format(dateFinal);

            System.out.println("POLICYRISKCOMMENCEMENTDATE = " + POLICYRISKCOMMENCEMENTDATE);

            String CONTACTRESIDENCE = parseXmlTag(Node, "CONTACTRESIDENCE");
            CONTACTRESIDENCE = CONTACTRESIDENCE == null ? "" : CONTACTRESIDENCE;

            String CONTACTOFFICE = parseXmlTag(Node, "CONTACTOFFICE");
            CONTACTOFFICE = CONTACTOFFICE == null ? "" : CONTACTOFFICE;

            String POLICYTYPE = parseXmlTag(Node, "POLICYTYPE");
            POLICYTYPE = POLICYTYPE == null ? "" : POLICYTYPE;
            if (premiumup == null) {
                premiumup = "";
            } else {

                premiumup = premiumup.split("T")[0];

                Date dt1 = null;
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat df1 = new SimpleDateFormat("dd-MMMM-yyyy");
                try {
                    dt1 = df.parse(premiumup);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                premiumup = df1.format(dt1);

            }

            String strPayType = "";

            if (paytype == null) {
                strPayType = "";
                paytype = "";
            }

            if (paytype.contentEquals("Money IN")) {
                strPayType = "Paid";
            } else if (paytype.contentEquals("Money OUT")) {
                strPayType = "Unpaid";
            }

            if (paytype.contentEquals("Money OUT")) {

                XMLHolderRenewal nodeVal = new XMLHolderRenewal(No, Holderid,
                        fname, lname, status, premiumup, premiumamt, strPayType, CONTACTMOBILE,
                        POLICYPAYMENTMECHANISM, EMAILID, POLICYRISKCOMMENCEMENTDATE, CONTACTRESIDENCE,
                        CONTACTOFFICE, POLICYTYPE);
                lsData.add(nodeVal);

            }

        }

        return lsData;

    }

    public List<XMLHolderBdm> parseNodeElementBdm(List<String> lsNode) {

        List<XMLHolderBdm> lsData = new ArrayList<XMLHolderBdm>();

        for (String Node : lsNode) {

            String banktype = parseXmlTag(Node, "BANKTYPE");

            String branchname = parseXmlTag(Node, "BANKBRANCHNAME");

            String code = parseXmlTag(Node, "CHANNELCODE");

            String sigle = parseXmlTag(Node, "CHANNELSIGLE");

            String fname = parseXmlTag(Node, "CHANNELFIRSTNAME");

            String lname = parseXmlTag(Node, "CHANNELLASTNAME");

            String banktypeome = parseXmlTag(Node, "BANKTYPE1");

            String branchnameone = parseXmlTag(Node, "BANKBRANCHNAME1");

            String MOBILE = parseXmlTag(Node, "MOBILE");

            XMLHolderBdm nodeVal = new XMLHolderBdm(banktype, branchname, code,
                    sigle, fname, lname, banktypeome, branchnameone, MOBILE);

            lsData.add(nodeVal);

        }

        return lsData;

    }

    @SuppressWarnings("unused")
    public List<XMLHolderBDMCIF> parseNodeElementBDMCIF(List<String> lsNode) {

        List<XMLHolderBDMCIF> lsData = new ArrayList<XMLHolderBDMCIF>();
        double dbltot = 0;

        DecimalFormat currencyFormat = new DecimalFormat();
        currencyFormat = new DecimalFormat("##,##,##,###");

        XMLHolderBDMCIF nodeVa = new XMLHolderBDMCIF("Policy Type",
                "Renewal Policy", "New Business Policy",
                "New Business Premium", "Renewal Premium");

        lsData.add(nodeVa);
        String strtotal = "";
        for (String Node : lsNode) {

            String PolicyType = parseXmlTag(Node, "POLICYTYPE");

            String RenPolicy = parseXmlTag(Node, "RENEWEDPOLICY");

            String NewPolicy = parseXmlTag(Node, "NEWPOLICY");

            String NewPremium = parseXmlTag(Node, "NEWBUSINESSPREMIUM");

            String RenPremium = parseXmlTag(Node, "RENEWALPREMIUM");

            XMLHolderBDMCIF nodeVal = new XMLHolderBDMCIF(
                    PolicyType == null ? "" : PolicyType,
                    RenPolicy == null ? "" : RenPolicy, NewPolicy == null ? ""
                    : NewPolicy, NewPremium == null ? "" : NewPremium,
                    RenPremium == null ? "" : RenPremium);

            lsData.add(nodeVal);

            dbltot = dbltot
                    + Double.parseDouble(NewPremium == null ? "0" : NewPremium)
                    + Double.parseDouble(RenPremium == null ? "0" : RenPremium);
            strtotal = String.valueOf(dbltot);
        }

        Calendar cal = Calendar.getInstance();
        int mYear = cal.get(Calendar.YEAR);
        int mMonth = cal.get(Calendar.MONTH);
        // int mDay = cal.get(Calendar.DAY_OF_MONTH);

        String fyear = "";
        String lastd = "";

        // String y = String.valueOf(mYear);
        String m = String.valueOf(mMonth + 1);
        // String d = String.valueOf(mDay);
        // String da = String.valueOf(mDay);
        if (m.contentEquals("1")) {
            m = "January";

            fyear = String.valueOf(mYear - 1);
            lastd = String.valueOf(mYear % 100);
            fyear = fyear + "-" + lastd;

        } else if (m.contentEquals("2")) {
            m = "February";

            fyear = String.valueOf(mYear - 1);
            lastd = String.valueOf(mYear % 100);
            fyear = fyear + "-" + lastd;

        } else if (m.contentEquals("3")) {
            m = "March";

            fyear = String.valueOf(mYear - 1);
            lastd = String.valueOf(mYear % 100);
            fyear = fyear + "-" + lastd;

        } else if (m.contentEquals("4")) {
            m = "April";

            fyear = String.valueOf(mYear);
            lastd = String.valueOf((mYear + 1) % 100);
            fyear = fyear + "-" + lastd;

        } else if (m.contentEquals("5")) {
            m = "May";

            fyear = String.valueOf(mYear);
            lastd = String.valueOf((mYear + 1) % 100);
            fyear = fyear + "-" + lastd;

        } else if (m.contentEquals("6")) {
            m = "June";

            fyear = String.valueOf(mYear);
            lastd = String.valueOf((mYear + 1) % 100);
            fyear = fyear + "-" + lastd;

        } else if (m.contentEquals("7")) {
            m = "July";

            fyear = String.valueOf(mYear);
            lastd = String.valueOf((mYear + 1) % 100);
            fyear = fyear + "-" + lastd;

        } else if (m.contentEquals("8")) {
            m = "August";

            fyear = String.valueOf(mYear);
            lastd = String.valueOf((mYear + 1) % 100);
            fyear = fyear + "-" + lastd;

        } else if (m.contentEquals("9")) {
            m = "September";

            fyear = String.valueOf(mYear);
            lastd = String.valueOf((mYear + 1) % 100);
            fyear = fyear + "-" + lastd;

        } else if (m.contentEquals("10")) {
            m = "October";

            fyear = String.valueOf(mYear);
            lastd = String.valueOf((mYear + 1) % 100);
            fyear = fyear + "-" + lastd;

        } else if (m.contentEquals("11")) {
            m = "November";

            fyear = String.valueOf(mYear);
            lastd = String.valueOf((mYear + 1) % 100);
            fyear = fyear + "-" + lastd;

        } else if (m.contentEquals("12")) {
            m = "December";

            fyear = String.valueOf(mYear);
            lastd = String.valueOf((mYear + 1) % 100);
            fyear = fyear + "-" + lastd;

        }

        // String str =
        // "Total (Renewal + New Business)\n"+"(for financial year 2013-14)";
        String str = "Total (Renewal + New Business)\n"
                + "(for financial year " + fyear + ")";

        XMLHolderBDMCIF nodeV = new XMLHolderBDMCIF("", "", str,
                strtotal, "");

        lsData.add(nodeV);

        return lsData;

    }

    public ArrayList<String> parseXmlTagMultiple(String ParentNode, String tag) {

        ArrayList<String> content = new ArrayList<String>();

        int start_indx = 0;
        int end_indx = 0;

        for (int i = 0; i < ParentNode.length() && start_indx != -1; i = i
                + tag.length()) {
            start_indx = ParentNode.indexOf("<" + tag + ">",
                    end_indx + tag.length());
            end_indx = ParentNode.indexOf("</" + tag + ">",
                    start_indx + tag.length());

            if (start_indx >= 0 && end_indx > 0) {
                content.add(ParentNode.substring(start_indx + tag.length() + 2,
                        end_indx));

            }

        }

        return content;

    }

    public String parseXmlTagNullable(String ParentNode, String tag) {

        try {
            int start_indx = ParentNode.indexOf("<" + tag + ">");

            int end_indx = ParentNode.indexOf("</" + tag + ">",
                    start_indx + tag.length());

            String content = "";

            if (start_indx >= 0 && end_indx > 0) {

                content = ParentNode.substring(start_indx + tag.length() + 2,
                        end_indx);

            }

            return content = content == null ? "" : content;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "";
    }

    public String parseXmlTag(String ParentNode, String tag) {

        try {
            int start_indx = ParentNode.indexOf("<" + tag + ">");

            int end_indx = ParentNode.indexOf("</" + tag + ">",
                    start_indx + tag.length());

            String content = null;

            if (start_indx >= 0 && end_indx > 0) {

                content = ParentNode.substring(start_indx + tag.length() + 2,
                        end_indx);

            }

            return content;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "";
    }

    public List<String> parseParentNode(String XML, String tag) {

        List<String> lsNode = new ArrayList<String>();

        while (XML.length() > 0) {

            int start_indx = XML.indexOf("<" + tag + ">");

            int end_indx = XML.indexOf("</" + tag + ">",
                    start_indx + tag.length());

            String content = null;

            if (start_indx >= 0 && end_indx > 0) {

                content = XML
                        .substring(start_indx + tag.length() + 2, end_indx);

                XML = XML.substring(end_indx + tag.length() + 3);

                lsNode.add(content);

            } else {

                XML = "";

            }

        }

        return lsNode;

    }

    public List<XMLHolderUM> parseNodeElementUM(List<String> lsNode) {

        List<XMLHolderUM> lsData = new ArrayList<XMLHolderUM>();

        for (String Node : lsNode) {

            String code = parseXmlTag(Node, "CHANNELCODE");

            String sigle = parseXmlTag(Node, "CHANNELSIGLE");

            String fname = parseXmlTag(Node, "CHANNELFIRSTNAME");

            String lname = parseXmlTag(Node, "CHANNELLASTNAME");

            // String bankname = parseXmlTag(Node, "UM_BANKNAME");

            String bankname = "";

            XMLHolderUM nodeVal = new XMLHolderUM(bankname, code, sigle, fname,
                    lname);

            lsData.add(nodeVal);

        }

        return lsData;

    }

    public List<XMLHolderCategory> parseNodeElementCategory(List<String> lsNode) {

        List<XMLHolderCategory> lsData = new ArrayList<XMLHolderCategory>();

        for (String Node : lsNode) {

            String id = parseXmlTag(Node, "CATEGORY_MAST_ID");

            String name = parseXmlTag(Node, "CATEGORY_NAME");

            XMLHolderCategory nodeVal = new XMLHolderCategory(id, name);

            lsData.add(nodeVal);

        }

        return lsData;

    }

    public List<XMLHolderSubCategory> parseNodeElementSubCategory(
            List<String> lsNode) {

        List<XMLHolderSubCategory> lsData = new ArrayList<XMLHolderSubCategory>();

        for (String Node : lsNode) {

            String id = parseXmlTag(Node, "SUBCATEGORY_MAST_ID");

            String name = parseXmlTag(Node, "SUBCATEGORY_NAME");

            XMLHolderSubCategory nodeVal = new XMLHolderSubCategory(id, name);

            lsData.add(nodeVal);

        }

        return lsData;

    }

    public List<XMLHolderBankBranch> parseNodeElementBankBranch(
            List<String> lsNode) {

        List<XMLHolderBankBranch> lsData = new ArrayList<XMLHolderBankBranch>();

        for (String Node : lsNode) {

            String id = parseXmlTag(Node, "BANKBRANCHCODE");

            String name = parseXmlTag(Node, "BANKBRANCHNAME");

            XMLHolderBankBranch nodeVal = new XMLHolderBankBranch(id, name);

            lsData.add(nodeVal);

        }

        return lsData;

    }

    public List<XMLHolderParamList> parseNodeElementParamList(
            List<String> lsNode) {

        List<XMLHolderParamList> lsData = new ArrayList<XMLHolderParamList>();

        for (String Node : lsNode) {

            String id = parseXmlTag(Node, "OBJECTIVE_PARAM_MAST_ID");

            String name = parseXmlTag(Node, "PARAM_NAME");

            XMLHolderParamList nodeVal = new XMLHolderParamList(id, name);

            lsData.add(nodeVal);

        }

        return lsData;

    }

    public List<XMLHolderAdvances> parseNodeElementAdvances(List<String> lsNode) {

        List<XMLHolderAdvances> lsData = new ArrayList<XMLHolderAdvances>();

        for (String Node : lsNode) {

            String advances_id = parseXmlTag(Node, "BRANCH_ADVANCES_ID");

            String branchcode = parseXmlTag(Node, "BRANCH_CODE");

            String tot_no_of_acc = parseXmlTag(Node, "TOT_NO_OF_AC");

            String tot_out = parseXmlTag(Node, "TOT_OUTSTANDING");

            String no_of_acc_b1l = parseXmlTag(Node, "NO_OF_AC_B1L");

            String tot_out_b1l = parseXmlTag(Node, "TOT_OUTSTANDING_B1L");

            String no_of_acc_1lto5l = parseXmlTag(Node, "NO_OF_AC_1LTO5L");

            String tot_out_1lto5l = parseXmlTag(Node, "TOT_OUTSTANDING_1LTO5L");

            String no_of_acc_a5l = parseXmlTag(Node, "NO_OF_AC_A5L");

            String tot_out_a5l = parseXmlTag(Node, "TOT_OUTSTANDING_A5L");

            String category = parseXmlTag(Node, "ADVANCES_CATEGORY");

            XMLHolderAdvances nodeVal = new XMLHolderAdvances(advances_id,
                    branchcode, tot_no_of_acc, tot_out, no_of_acc_b1l,
                    tot_out_b1l, no_of_acc_1lto5l, tot_out_1lto5l,
                    no_of_acc_a5l, tot_out_a5l, category);

            lsData.add(nodeVal);

        }

        return lsData;

    }

    public List<XMLHolderDeposit> parseNodeElementDeposit(List<String> lsNode) {

        List<XMLHolderDeposit> lsData = new ArrayList<XMLHolderDeposit>();

        for (String Node : lsNode) {

            String deposit_id = parseXmlTag(Node, "BRANCH_DEPOSITS_ID");

            String branchcode = parseXmlTag(Node, "BRANCH_CODE");

            String tot_acc = parseXmlTag(Node, "TOT_AC");

            String tot_amt = parseXmlTag(Node, "TOTAL_AMOUNT");

            String new_acc_b1k = parseXmlTag(Node, "NEW_AC_B1K");

            String new_bal_b1k = parseXmlTag(Node, "NEW_BALANCE_B1K");

            String new_acc_10kto1l = parseXmlTag(Node, "NEW_AC_10KTO1L");

            String new_bal_10kto1l = parseXmlTag(Node, "NEW_BALANCE_10KTO1L");

            String new_acc_1lto5l = parseXmlTag(Node, "NEW_AC_1LTO5L");

            String new_bal_1lto5l = parseXmlTag(Node, "NEW_BALANCE_1LTO5L");

            String new_acc_5landa = parseXmlTag(Node, "NEW_AC_5LANDA");

            String new_bal_5landa = parseXmlTag(Node, "NEW_BALANCE_5LANDA");

            String category = parseXmlTag(Node, "DEPOSITS_CATEGORY");

            XMLHolderDeposit nodeVal = new XMLHolderDeposit(deposit_id,
                    branchcode, tot_acc, tot_amt, new_acc_b1k, new_bal_b1k,
                    new_acc_10kto1l, new_bal_10kto1l, new_acc_1lto5l,
                    new_bal_1lto5l, new_acc_5landa, new_bal_5landa, category);

            lsData.add(nodeVal);

        }

        return lsData;

    }

    public List<XMLHolderBankList> parseNodeElementBankList(List<String> lsNode) {

        List<XMLHolderBankList> lsData = new ArrayList<XMLHolderBankList>();

        for (String Node : lsNode) {

            String bankName = parseXmlTag(Node, "BA_NAME");

            XMLHolderBankList nodeVal = new XMLHolderBankList(bankName);

            lsData.add(nodeVal);
        }

        return lsData;
    }

    public List<ChildEducationPlannerBean> parseNodeElementChildEducationList(
            List<String> lsNode) {

        List<ChildEducationPlannerBean> lsData = new ArrayList<ChildEducationPlannerBean>();

        for (String Node : lsNode) {

            String prospectName = parseXmlTag(Node, "USER_NAME");
            String prospectEmail = parseXmlTag(Node, "EMAIL_ID");
            String prospectMobile = parseXmlTag(Node, "MOBILE_NUMBER");

            ChildEducationPlannerBean nodeVal = new ChildEducationPlannerBean(
                    prospectName, prospectEmail, prospectMobile, "Child Plan");

            lsData.add(nodeVal);
        }

        return lsData;
    }


    public List<ChildEducationPlannerBean> parseNodeElementRetirementList(
            List<String> lsNode) {


		/*<NewDataSet>
		<Table>
		<PERSON_NAME>Tushar</PERSON_NAME> <PERSON_DOB>21/01/1982</PERSON_DOB>
		<PERSON_AGE>35</PERSON_AGE> </Table> <Table> <PERSON_NAME>Rajan</PERSON_NAME>
		<PERSON_DOB>20/01/1982</PERSON_DOB> <PERSON_AGE>35</PERSON_AGE>
		</Table>
		</NewDataSet> */

        List<ChildEducationPlannerBean> lsData = new ArrayList<ChildEducationPlannerBean>();

        for (String Node : lsNode) {

            String prospectName = parseXmlTag(Node, "PERSON_NAME");
            String prospectEmail = parseXmlTag(Node, "EMAIL_ID");
            String prospectMobile = parseXmlTag(Node, "MOBILE_NO");

            ChildEducationPlannerBean nodeVal = new ChildEducationPlannerBean(
                    prospectName, prospectEmail, prospectMobile,
                    "Retirement Plan");

            lsData.add(nodeVal);
        }

        return lsData;
    }

    public List<XMLHolderBranch_Profile> parseNodeElementBranchProfile(
            List<String> lsNode) {

        List<XMLHolderBranch_Profile> lsData = new ArrayList<XMLHolderBranch_Profile>();

        for (String Node : lsNode) {

            String branchcode = parseXmlTag(Node, "BRANCH_CODE");

            String branchname = parseXmlTag(Node, "BRANCH_NAME");

            String branch_open_date = parseXmlTag(Node, "BRANCH_OPEN_DATE");

            String branch_net_result = parseXmlTag(Node, "BRANCH_NET_RESULT");

            String createddate = parseXmlTag(Node, "CREATED_DT");

            if (branch_open_date == null) {
                branch_open_date = "";
            } else {

                branch_open_date = branch_open_date.split("T")[0];

                Date dt1 = null;
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat df1 = new SimpleDateFormat("dd-MMMM-yyyy");
                try {
                    dt1 = df.parse(branch_open_date);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                branch_open_date = df1.format(dt1);

            }

            if (createddate == null) {
                createddate = "";
            } else {

                createddate = createddate.split("T")[0];

                Date dt1 = null;
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat df1 = new SimpleDateFormat("dd-MMMM-yyyy");
                try {
                    dt1 = df.parse(createddate);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                createddate = df1.format(dt1);

            }

            XMLHolderBranch_Profile nodeVal = new XMLHolderBranch_Profile(
                    branchcode, branchname, branch_open_date,
                    branch_net_result, createddate);

            lsData.add(nodeVal);

        }

        return lsData;

    }

    public List<XMLHolderAGENT> parseNodeElementAGENT(List<String> lsNode) {

        List<XMLHolderAGENT> lsData = new ArrayList<XMLHolderAGENT>();

        /*
         * XMLHolderAGENT nodeVa = new
         * XMLHolderAGENT("Region Name","Channel Code"
         * ,"Surrender Count","Claim Count",
         * "Maturity Count","Lapse Count","TechLapse Count"
         * ,"Claim Amount","Surrender Amount"
         * ,"New Business Count","New Business Amount",
         * "Frequency","Surable Count");
         *
         * lsData.add(nodeVa);
         */

        for (String Node : lsNode) {

            String regionname = parseXmlTag(Node, "SERVICEREGIONNAME");
            String channelcode = parseXmlTag(Node, "CHANNELCODE");
            String surcnt = parseXmlTag(Node, "SURRENDER_COUNT");

            String claimcnt = parseXmlTag(Node, "CLAIM_COUNT");

            String maturitycnt = parseXmlTag(Node, "MATURITY_COUNT");
            String lapsamt = parseXmlTag(Node, "LAPSE_COUNT");
            String techlapse = parseXmlTag(Node, "TECHLAPSE_COUNT");

            String claimamt = parseXmlTag(Node, "CLAIMAMOUNT");

            String suramt = parseXmlTag(Node, "SURRENDERAMOUNT");
            String nbcnt = parseXmlTag(Node, "NB_COUNT");
            String nbamt = parseXmlTag(Node, "NB_AMOUNT");
            String fre = parseXmlTag(Node, "FREQUENCY");
            String surable = parseXmlTag(Node, "SURABLE_COUNT");

            XMLHolderAGENT nodeVal = new XMLHolderAGENT(regionname == null ? ""
                    : regionname, channelcode == null ? "" : channelcode,
                    surcnt == null ? "" : surcnt, claimcnt == null ? ""
                    : claimcnt, maturitycnt == null ? "" : maturitycnt,
                    lapsamt == null ? "" : lapsamt, techlapse == null ? ""
                    : techlapse, claimamt == null ? "" : claimamt,
                    suramt == null ? "" : suramt, nbcnt == null ? "" : nbcnt,
                    nbamt == null ? "" : nbamt, fre == null ? "" : fre,
                    surable == null ? "" : surable);

            lsData.add(nodeVal);

        }
        return lsData;

    }

    public List<XMLHolder_Agent> parseNodeElementAgent(List<String> lsNode) {

        List<XMLHolder_Agent> lsData = new ArrayList<XMLHolder_Agent>();

        for (String Node : lsNode) {

            String ProposalNo = parseXmlTag(Node, "POLICYPROPOSALNUMBER");
            ProposalNo = ProposalNo == null ? "" : ProposalNo;

            String HolderId = parseXmlTag(Node, "HOLDERID");
            HolderId = HolderId == null ? "" : HolderId;

            String No = parseXmlTag(Node, "POLICYNUMBER");
            No = No == null ? "" : No;

            String fname = parseXmlTag(Node, "HOLDERPERSONFIRSTNAME");
            fname = fname == null ? "" : fname;

            String lname = parseXmlTag(Node, "HOLDERPERSONLASTNAME");
            lname = lname == null ? "" : lname;

            String productname = parseXmlTag(Node, "PRODUCTNAME");
            productname = productname == null ? "" : productname;

            String policytype = parseXmlTag(Node, "POLICYTYPE");
            policytype = policytype == null ? "" : policytype;

            String policypaymentterm = parseXmlTag(Node, "POLICYPAYMENTTERM");
            policypaymentterm = policypaymentterm == null ? "" : policypaymentterm;

            String policysumassured = parseXmlTag(Node, "POLICYSUMASSURED");
            policysumassured = policysumassured == null ? "" : policysumassured;

            String premfre = parseXmlTag(Node, "PREMIUMPAYMENTFREQUENCY");
            premfre = premfre == null ? "" : premfre;

            String grossamt = parseXmlTag(Node, "PREMIUMGROSSAMOUNT");
            grossamt = grossamt == null ? "" : grossamt;

            String status = parseXmlTag(Node, "POLICYCURRENTSTATUS");
            status = status == null ? "" : status;

            String premiumup = parseXmlTag(Node, "PREMIUMFUP");


            if (premiumup == null) {
                premiumup = "";
            } else {

                premiumup = premiumup.split("T")[0];

                Date dt1 = null;
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat df1 = new SimpleDateFormat("dd-MMMM-yyyy");
                try {
                    dt1 = df.parse(premiumup);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                premiumup = df1.format(dt1);

            }

            XMLHolder_Agent nodeVal = new XMLHolder_Agent(ProposalNo, HolderId,
                    No, fname, lname, productname, policytype,
                    policypaymentterm, policysumassured, premfre, grossamt,
                    status, premiumup);

            lsData.add(nodeVal);

        }

        return lsData;

    }

    public List<XMLHolderBDM_MAIL_DATA> parseNodeElementBDM_MAIl_DATA(
            List<String> lsNode) {

        List<XMLHolderBDM_MAIL_DATA> lsData = new ArrayList<XMLHolderBDM_MAIL_DATA>();

        int i = 0;

        String nopmtd = "";
        String nbpmtd = "";
        String nbpytd = "";
        String nopytd = "";

        int jh = lsNode.size();

        for (String Node : lsNode) {

            if (jh == 1) {

                nopmtd = parseXmlTag(Node, "MTDNBP");

                if (nopmtd == null) {
                    nbpytd = parseXmlTag(Node, "Amount");
                    nopytd = parseXmlTag(Node, "YTDNBP");

                    XMLHolderBDM_MAIL_DATA nodeVal = new XMLHolderBDM_MAIL_DATA(
                            nbpmtd == null ? "" : nbpmtd, nbpytd == null ? ""
                            : nbpytd, nopmtd == null ? "" : nopmtd,
                            nopytd == null ? "" : nopytd);

                    lsData.add(nodeVal);
                } else {
                    nopmtd = parseXmlTag(Node, "MTDNBP");

                    nbpmtd = parseXmlTag(Node, "Amount");

                    XMLHolderBDM_MAIL_DATA nodeVal = new XMLHolderBDM_MAIL_DATA(
                            nbpmtd == null ? "" : nbpmtd, nbpytd == null ? ""
                            : nbpytd, nopmtd == null ? "" : nopmtd,
                            nopytd == null ? "" : nopytd);

                    lsData.add(nodeVal);
                }

            } else {
                if (i == 0) {
                    nopmtd = parseXmlTag(Node, "MTDNBP");

                    nbpmtd = parseXmlTag(Node, "Amount");

                    i++;
                } else {
                    nbpytd = parseXmlTag(Node, "Amount");

                    nopytd = parseXmlTag(Node, "YTDNBP");

                    XMLHolderBDM_MAIL_DATA nodeVal = new XMLHolderBDM_MAIL_DATA(
                            nbpmtd == null ? "" : nbpmtd, nbpytd == null ? ""
                            : nbpytd, nopmtd == null ? "" : nopmtd,
                            nopytd == null ? "" : nopytd);

                    lsData.add(nodeVal);

                }
            }
        }

        return lsData;

    }

    public List<XMLHolder_Agent_Comm> parseNodeElementAgent_Comm(
            List<String> lsNode) {

        List<XMLHolder_Agent_Comm> lsData = new ArrayList<XMLHolder_Agent_Comm>();

        for (String Node : lsNode) {

            String empname = parseXmlTag(Node, "EMPLOYEENAME");

            String empid = parseXmlTag(Node, "EMPLOYEEID");

            String positiontype = parseXmlTag(Node, "POSITIONTYPE");

            String channeltype = parseXmlTag(Node, "CHANNELTYPE");

            String comm = parseXmlTag(Node, "PAYABLE_GROSS_PRODUCT_COMM");

            String tds = parseXmlTag(Node, "TDS");

            String adj = parseXmlTag(Node, "ADJUSTMENT");

            String st = parseXmlTag(Node, "SERVICE_TAX");

            String finalamt = parseXmlTag(Node, "FINAL_PAYBLE_AMT");

            XMLHolder_Agent_Comm nodeVal = new XMLHolder_Agent_Comm(empname,
                    empid, positiontype, channeltype, comm, tds, adj, st,
                    finalamt);

            lsData.add(nodeVal);

        }

        return lsData;

    }

    public List<XMLHolderDOB> parseNodeElementCust_DOB(List<String> lsNode) {

        List<XMLHolderDOB> lsData = new ArrayList<XMLHolderDOB>();

        for (String Node : lsNode) {

            String dob = parseXmlTag(Node, "HOLDERPERSONDOB");

            String cmob = parseXmlTag(Node, "CONTACTMOBILE");

            String email = parseXmlTag(Node, "EMAILID");

            if (dob == null) {
                dob = "";
            } else {

                dob = dob.split("T")[0];

                Date dt1 = null;
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat df1 = new SimpleDateFormat("dd-MMMM-yyyy");
                try {
                    dt1 = df.parse(dob);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                dob = df1.format(dt1);

            }

            if (cmob == null) {
                cmob = "";
            }
            if (email == null) {
                email = "";
            }

            XMLHolderDOB nodeVal = new XMLHolderDOB(dob, cmob, email);

            lsData.add(nodeVal);

        }

        return lsData;

    }

    public List<XMLHolder_Req> parseNodeElementReq(List<String> lsNode) {

        List<XMLHolder_Req> lsData = new ArrayList<XMLHolder_Req>();

        for (String Node : lsNode) {

            String desc = parseXmlTag(Node, "DESCRIPTION");

            String regflag = parseXmlTag(Node, "REQUIREMENTFLAG");

            String comnts = parseXmlTag(Node, "REQUIREMENTCOMMENT");

            String date = parseXmlTag(Node, "RAISEDATE");

            String payamt = parseXmlTag(Node, "PAYMENTAMOUNT");

            String propno = parseXmlTag(Node, "POLICYPROPOSALNUMBER");

            String payageing = "";

            if (date == null) {
                date = "";
                payageing = "";
            } else {

                date = date.split("T")[0];

                Date dt1 = null;
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat df1 = new SimpleDateFormat("dd-MMMM-yyyy");
                try {
                    dt1 = df.parse(date);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                date = df1.format(dt1);

                // count current date - raised date for paymentageing

                final SimpleDateFormat dateFormat = new SimpleDateFormat(
                        "dd-MM-yyyy");

                Calendar calen = Calendar.getInstance();
                Date cdate = calen.getTime();
                String date1 = dateFormat.format(cdate);
                Date dt12 = null;
                Date dt13 = null;
                try {
                    dt12 = dateFormat.parse(date1);
                } catch (ParseException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

                String datee = dateFormat.format(dt1);
                try {
                    dt13 = dateFormat.parse(datee);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                long diff = dt12.getTime() - dt13.getTime();
                long stry = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                payageing = String.valueOf(stry);

            }

            XMLHolder_Req nodeVal = new XMLHolder_Req(desc, regflag, comnts,
                    date, payamt, propno, payageing);

            lsData.add(nodeVal);

        }

        return lsData;

    }

    public List<XMLHolderPersistency> parseNodeElementPersistency(
            List<String> lsNode, String strMonth) {

        List<XMLHolderPersistency> lsData = new ArrayList<XMLHolderPersistency>();

        if (!strMonth.equalsIgnoreCase("25")) {

            double totalCollectable = 0;
            double totalCollected = 0;
            //  double totalPersistency = 0.0;
            double totalUnpaidCount = 0;

            for (int i = 0; i < lsNode.size(); i++) {

                String Node = lsNode.get(i);

                String collected = parseXmlTag(Node,
                        "COLLECTED");

                String collectable = parseXmlTag(Node,
                        "COLLECTABLE");
                String UNPAID_POLICYCOUNT = parseXmlTag(Node,
                        "UNPAID_POLICYCOUNT");

                String EMPLOYEEID = parseXmlTag(Node,
                        "EMPLOYEEID");
                String EMPLOYEENAME = parseXmlTag(Node,
                        "EMPLOYEENAME");

                String curmonth_ratio = "";
                if (collected.contentEquals("null")
                        || collectable.contentEquals("null")) {
                    curmonth_ratio = "0";
                    //totalCollectable = 0;
                    //totalCollected = 0;
                    //totalUnpaidCount = 0;
                    //totalPersistency = 0;
                } else {
                    double dbl_cltd = Double.parseDouble(collected);
                    double dbl_cltb = Double
                            .parseDouble(collectable);

                    collected = String.format("%.2f", dbl_cltd);
                    collectable = String.format("%.2f", dbl_cltb);
                    double dbl_per = 0;
                    if (dbl_cltb != 0) {
                        dbl_per = (dbl_cltd / dbl_cltb) * 100;
                    }
                    curmonth_ratio = String.format("%.2f", dbl_per);

                    totalCollectable = totalCollectable + dbl_cltb;
                    totalCollected = totalCollected + dbl_cltd;
                    //totalPersistency = totalPersistency + dbl_per;
                    totalUnpaidCount = totalUnpaidCount + Double.parseDouble(UNPAID_POLICYCOUNT);
                }
                XMLHolderPersistency nodeVal = new XMLHolderPersistency(
                        collected == null ? "" : collected,
                        collectable == null ? "" : collectable, "",
                        curmonth_ratio + "%", "", UNPAID_POLICYCOUNT, EMPLOYEEID, EMPLOYEENAME,
                        Color.parseColor("#ffffff"));
                lsData.add(nodeVal);
            }

            // double averagePersistency = totalPersistency / lsNode.size();

            double averagePersistency = 0;
            if (totalCollectable != 0) {
                averagePersistency = (totalCollected / totalCollectable) * 100;
            }

            String collectable = String.format("%.2f", totalCollectable);
            String collected = String.format("%.2f", totalCollected);
            String perAverage = String.format("%.2f", averagePersistency) + "%";
            long unpaidLong = ((long) totalUnpaidCount);
            int colorInt = Color.parseColor("#ADD8E6");
            XMLHolderPersistency nodeVal = new XMLHolderPersistency(collected,
                    collectable, "",
                    perAverage, "", unpaidLong + "", "Total", ""
                    , colorInt);
            lsData.add(nodeVal);
        } else {

            Calendar cal = Calendar.getInstance();
            String monthName = new SimpleDateFormat("MMM").format(cal.getTime());

            String curmonth_collected_pre = monthName.toUpperCase()
                    + "_COLLECTEDPREMIUM";
            String curmonth_collectable_pre = monthName.toUpperCase()
                    + "_COLLECTABLEPREMIUM";
            String curmonth_ratio = "";
            String fy_ratio = "";

            for (String Node : lsNode) {

                String strcurmonth_collected_pre = parseXmlTag(Node,
                        curmonth_collected_pre);

                String strcurmonth_collectable_pre = parseXmlTag(Node,
                        curmonth_collectable_pre);

                String strmarch_collectable_pre = parseXmlTag(Node,
                        "MAR_COLLECTABLEPREMIUM");

                if (strcurmonth_collected_pre.contentEquals("null")
                        || strcurmonth_collectable_pre.contentEquals("null")) {
                    curmonth_ratio = "0";
                } else {
                    double dbl_cltd = Double.parseDouble(strcurmonth_collected_pre);
                    double dbl_cltb = Double
                            .parseDouble(strcurmonth_collectable_pre);
                    double dbl_per = 0;
                    if (dbl_cltb != 0) {
                        dbl_per = (dbl_cltd / dbl_cltb) * 100;
                    }
                    curmonth_ratio = String.valueOf(Math.round(dbl_per));
                }

                if (strmarch_collectable_pre.contentEquals("null")) {
                    fy_ratio = "0";
                } else {
                    double dbl_cltd = Double.parseDouble(strcurmonth_collected_pre);
                    double dbl_cltb = Double.parseDouble(strmarch_collectable_pre);
                    double dbl_per = (dbl_cltd / dbl_cltb) * 100;
                    fy_ratio = String.valueOf(Math.round(dbl_per));
                }

                XMLHolderPersistency nodeVal = new XMLHolderPersistency(
                        strcurmonth_collected_pre == null ? ""
                                : strcurmonth_collected_pre,
                        strcurmonth_collectable_pre == null ? ""
                                : strcurmonth_collectable_pre,
                        strmarch_collectable_pre == null ? ""
                                : strmarch_collectable_pre, curmonth_ratio + "%",
                        fy_ratio + "%", "", "", "", 0);

                lsData.add(nodeVal);

            }
        }

        return lsData;
    }

    public List<XMLHolder_UM_BDM_DAH> parseNodeElement_UM_BDM_DASH(
            List<String> lsNode) {

        List<XMLHolder_UM_BDM_DAH> lsData = new ArrayList<XMLHolder_UM_BDM_DAH>();

        for (String Node : lsNode) {

            String SYSTEM = parseXmlTag(Node, "SYSTEM");

            String strYTD_INITIAL_CASHIERING_AMOUNT = parseXmlTag(Node,
                    "YTD_INITIAL_CASHIERING_AMOUNT");

            String strMTD_INITIAL_CASHIERING_AMOUNT = parseXmlTag(Node,
                    "MTD_INITIAL_CASHIERING_AMOUNT");

            String strYTD_INITIAL_CASHIERING_COUNT = parseXmlTag(Node,
                    "YTD_INITIAL_CASHIERING_COUNT");

            String strMTD_INITIAL_CASHIERING_COUNT = parseXmlTag(Node,
                    "MTD_INITIAL_CASHIERING_COUNT");

            String strYTD_ISSUANCE_COUNT = parseXmlTag(Node,
                    "YTD_ISSUANCE_COUNT");

            String strMTD_ISSUANCE_COUNT = parseXmlTag(Node,
                    "MTD_ISSUANCE_COUNT");

            String strYTD_ISSUANCE_AMOUNT = parseXmlTag(Node,
                    "YTD_ISSUANCE_AMOUNT");

            String strMTD_ISSUANCE_AMOUNT = parseXmlTag(Node,
                    "MTD_ISSUANCE_AMOUNT");

            String strPENDING_ISSUANCE_COUNT = parseXmlTag(Node,
                    "PENDING_ISSUANCE_COUNT");

            String strRENEWAL_COLLECTION_DUE = parseXmlTag(Node,
                    "RENEWAL_COLLECTION_DUE");

            XMLHolder_UM_BDM_DAH nodeVal = new XMLHolder_UM_BDM_DAH(
                    SYSTEM == null ? "" : SYSTEM,
                    strYTD_INITIAL_CASHIERING_AMOUNT == null ? ""
                            : strYTD_INITIAL_CASHIERING_AMOUNT,
                    strMTD_INITIAL_CASHIERING_AMOUNT == null ? ""
                            : strMTD_INITIAL_CASHIERING_AMOUNT,
                    strYTD_INITIAL_CASHIERING_COUNT == null ? ""
                            : strYTD_INITIAL_CASHIERING_COUNT,
                    strMTD_INITIAL_CASHIERING_COUNT == null ? ""
                            : strMTD_INITIAL_CASHIERING_COUNT,
                    strYTD_ISSUANCE_COUNT == null ? "" : strYTD_ISSUANCE_COUNT,
                    strMTD_ISSUANCE_COUNT == null ? "" : strMTD_ISSUANCE_COUNT,
                    strYTD_ISSUANCE_AMOUNT == null ? ""
                            : strYTD_ISSUANCE_AMOUNT,
                    strMTD_ISSUANCE_AMOUNT == null ? ""
                            : strMTD_ISSUANCE_AMOUNT,
                    strPENDING_ISSUANCE_COUNT == null ? ""
                            : strPENDING_ISSUANCE_COUNT,
                    strRENEWAL_COLLECTION_DUE == null ? ""
                            : strRENEWAL_COLLECTION_DUE);

            lsData.add(nodeVal);

        }

        return lsData;

    }

    public List<XMLHolderPolicyDetail> parseNodeElementPolicyDetail(
            List<String> lsNode) {

        List<XMLHolderPolicyDetail> lsData = new ArrayList<XMLHolderPolicyDetail>();

        for (String Node : lsNode) {

            String No = parseXmlTag(Node, "RIDERNAME");

            String fname = parseXmlTag(Node, "SUMINSURED");

            String lname = parseXmlTag(Node, "RISKPREMIUM");

            String status = parseXmlTag(Node, "PREMIUMTERM");

            String benefit = parseXmlTag(Node, "BENEFITERM");

            XMLHolderPolicyDetail nodeVal = new XMLHolderPolicyDetail(No,
                    fname, lname, status, benefit);

            lsData.add(nodeVal);

        }

        return lsData;

    }

    public List<XMLHolderTotalPreUnpaid> parseNodeElementTotalPre(
            List<String> lsNode) {

        List<XMLHolderTotalPreUnpaid> lsData = new ArrayList<XMLHolderTotalPreUnpaid>();

        for (String Node : lsNode) {

            String totalpaid = parseXmlTag(Node, "TOTALPREMIUMPAID");

            String unpaid = parseXmlTag(Node, "LASTTOPUPPAIDDATE");

            if (unpaid == null) {
                unpaid = "";
            } else {

                Date dt1 = null;
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                try {
                    dt1 = df.parse(unpaid);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                unpaid = df.format(dt1);

            }

            XMLHolderTotalPreUnpaid nodeVal = new XMLHolderTotalPreUnpaid(
                    totalpaid, unpaid);

            lsData.add(nodeVal);

        }

        return lsData;

    }

    public List<XMLHolderTotalUnUnpaid> parseNodeElementTotalUnPaid(
            List<String> lsNode) {

        List<XMLHolderTotalUnUnpaid> lsData = new ArrayList<XMLHolderTotalUnUnpaid>();

        for (String Node : lsNode) {

            String totalpaid = parseXmlTag(Node, "TOTALPREMIUMPAID");

            String unpaid = parseXmlTag(Node, "LASTTOPUPPAIDDATE");

            if (unpaid == null) {
                unpaid = "";
            } else {

                Date dt1 = null;
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                try {
                    dt1 = df.parse(unpaid);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                unpaid = df.format(dt1);

            }

            XMLHolderTotalUnUnpaid nodeVal = new XMLHolderTotalUnUnpaid(
                    totalpaid, unpaid);

            lsData.add(nodeVal);

        }

        return lsData;

    }

    public List<XMLHolderTopUp> parseNodeElementTopUp(List<String> lsNode) {

        List<XMLHolderTopUp> lsData = new ArrayList<XMLHolderTopUp>();

        for (String Node : lsNode) {

            String totalpaid = parseXmlTag(Node, "TOTALPREMIUMPAID");

            String unpaid = parseXmlTag(Node, "LASTTOPUPPAIDDATE");

            if (unpaid == null) {
                unpaid = "";
            } else {

                Date dt1 = null;
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                try {
                    dt1 = df.parse(unpaid);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                unpaid = df.format(dt1);

            }

            XMLHolderTopUp nodeVal = new XMLHolderTopUp(totalpaid, unpaid);

            lsData.add(nodeVal);

        }

        return lsData;

    }

    public List<clsHOLead> parseNodeElementLead(List<String> lsNode) {

        List<clsHOLead> lsData = new ArrayList<clsHOLead>();

        for (String Node : lsNode) {

            String strLeadDate = parseXmlTag(Node, "LEAD_DT");
            String strCustId = parseXmlTag(Node, "CIF_CODE");
            String strCustName = parseXmlTag(Node, "CUSTOMER_NAME");
            String strLeadPriority = parseXmlTag(Node, "LEAD_PRIORITY");
            String strLeadStatus = parseXmlTag(Node, "LEAD_STATUS");
            String strLeadSubStatus = parseXmlTag(Node, "LEAD_SUB_STATUS");
            String strProposalNo = parseXmlTag(Node, "PROPOSAL_NO");
            String strFollowupdate = parseXmlTag(Node, "FOLLOWUP_DT");
            String strComments = parseXmlTag(Node, "COMMENTS");
            String strAge = parseXmlTag(Node, "AGE");
            String strTotalAcc = parseXmlTag(Node, "NO_OF_ACCOUNTS");
            String strBalance = parseXmlTag(Node, "BALANCE");
            String strBranchCode = parseXmlTag(Node, "BRANCH_CODE");
            String strBDMCode = parseXmlTag(Node, "BDM_CODE");
            String strSyncStatus = "Open";
            String strLeadID = parseXmlTag(Node, "LEAD_MASTER_ID");
            String strBDMName = parseXmlTag(Node, "BDM_NAME");
            String strSource = parseXmlTag(Node, "SOURCE");

            if (strFollowupdate == null) {
                strFollowupdate = "";
            } else {

                strFollowupdate = strFollowupdate.split("T")[0];

                /*
                 * Date dt1 = null; SimpleDateFormat df = new SimpleDateFormat(
                 * "yyyy-MM-dd"); // SimpleDateFormat df1 = new //
                 * SimpleDateFormat("dd-MMMM-yyyy"); SimpleDateFormat df1 = new
                 * SimpleDateFormat( "d-MMMM-yyyy"); try { dt1 = (Date)
                 * df.parse(strFollowupdate); } catch (ParseException e) { //
                 * TODO Auto-generated catch block e.printStackTrace(); }
                 * strFollowupdate = df1.format(dt1);
                 */

            }

            if (strLeadDate == null) {
                strLeadDate = "";
            } else {

                strLeadDate = strLeadDate.split("T")[0];

                /*
                 * Date dt1 = null; SimpleDateFormat df = new SimpleDateFormat(
                 * "yyyy-MM-dd"); // SimpleDateFormat df1 = new //
                 * SimpleDateFormat("dd-MMMM-yyyy"); SimpleDateFormat df1 = new
                 * SimpleDateFormat( "d-MMMM-yyyy"); try { dt1 = (Date)
                 * df.parse(strLeadDate); } catch (ParseException e) { // TODO
                 * Auto-generated catch block e.printStackTrace(); } strLeadDate
                 * = df1.format(dt1);
                 */

            }

            clsHOLead nodeVal = new clsHOLead(strLeadDate, strCustId,
                    strCustName, strLeadPriority, strLeadStatus == null ? ""
                    : strLeadStatus, strLeadSubStatus == null ? ""
                    : strLeadSubStatus, strProposalNo, strFollowupdate,
                    strComments, strAge, strTotalAcc, strBalance,
                    strBranchCode, strBDMCode, strSyncStatus, strLeadID,
                    strBDMName, strSource);

            lsData.add(nodeVal);

        }

        return lsData;

    }

    public List<XMLProposerTrackerList> parseNodeElement_Proposertracker(
            List<String> lsNode) {

        List<XMLProposerTrackerList> lsData_req = new ArrayList<XMLProposerTrackerList>();

        /*
         * <PL_PROP_NUM>45YA034636</PL_PROP_NUM> <PR_FULL_NM>ABDUL ASAD ABDUL
         * SALAM SHAIKH</PR_FULL_NM> <IR_ROLE>Proposer</IR_ROLE>
         * <PropStatus>Cancelled</PropStatus> <Reason>Not Taken Up</Reason>
         */

		/*<ReqDtls> <Table>
        <PL_PROP_NUM>1KNA380511</PL_PROP_NUM>
        <PR_FULL_NM>DIPITI PRASHANT DESAI</PR_FULL_NM>
        <PR_LIFEASSURED_NM>Dipiti Prashant Desai</PR_LIFEASSURED_NM>
        <PROPSTATUS>Proposition</PROPSTATUS>
        <CONTACT_MOBILE>9923391135</CONTACT_MOBILE> </Table> </ReqDtls>*/

        String proposalNumber = "", status = "", reason = "", iRRole = "";

        String policyHolderName = "";
        String lifeAssuredName = "";
        String CONTACT_MOBILE = "";

        for (String Node : lsNode) {

            proposalNumber = parseXmlTag(Node, "PL_PROP_NUM");
            status = parseXmlTag(Node, "PROPSTATUS");
            reason = parseXmlTag(Node, "Reason");
            // iRRole = parseXmlTag(Node, "IR_ROLE");
            policyHolderName = parseXmlTag(Node, "PR_FULL_NM");
            lifeAssuredName = parseXmlTag(Node, "PR_LIFEASSURED_NM");
            CONTACT_MOBILE = parseXmlTag(Node, "CONTACT_MOBILE");
            /*if (iRRole.equalsIgnoreCase("Proposer")) {
                policyHolderName = parseXmlTag(Node, "PR_FULL_NM");

            } else if (iRRole.equalsIgnoreCase("LifeAssured")) {
                lifeAssuredName = parseXmlTag(Node, "PR_FULL_NM");
            }*/

        }
        XMLProposerTrackerList nodeVal = new XMLProposerTrackerList(
                proposalNumber, policyHolderName, lifeAssuredName, status,
                reason, iRRole, CONTACT_MOBILE);

        lsData_req.add(nodeVal);

        return lsData_req;

    }

    public List<XMLChannelProposerTrackerStatusList> parseNodeElement_ChannelProposertrackerStatusList(
            List<String> lsNode) {

        List<XMLChannelProposerTrackerStatusList> lsData_req = new ArrayList<XMLChannelProposerTrackerStatusList>();

        /*
         * <POLICYPROPOSALNUMBER>53AL912929</POLICYPROPOSALNUMBER>
         * <PAYMENTAMOUNT>200000</PAYMENTAMOUNT>
         * <CASHIERINGDATE>30-09-2016</CASHIERINGDATE> <STATUS>NON
         * MEDICAL</STATUS>
         */
        String CHANNELCODE = "", proposalNumber = "", paymentAmount = "", cashieringDate = "", status = "", pendingStatus = "";

        for (String Node : lsNode) {

            proposalNumber = parseXmlTag(Node, "POLICYPROPOSALNUMBER");
            paymentAmount = parseXmlTag(Node, "PAYMENTAMOUNT");
            cashieringDate = parseXmlTag(Node, "CASHIERINGDATE");
            status = parseXmlTag(Node, "STATUS");
            CHANNELCODE = parseXmlTag(Node, "CHANNELCODE");

            if (status.equalsIgnoreCase("NON MEDICAL")
                    || status.equalsIgnoreCase("MEDICAL")) {
                pendingStatus = parseXmlTag(Node, "PENDINGSTATUS");
            }
            XMLChannelProposerTrackerStatusList nodeVal = new XMLChannelProposerTrackerStatusList(
                    proposalNumber, paymentAmount, cashieringDate, status,
                    pendingStatus, CHANNELCODE);

            lsData_req.add(nodeVal);
        }

        return lsData_req;

    }

    // view policy detail

    public List<XMLHolderSBDueList> parseNodeElementSBDueList(
            List<String> lsNode) {

        List<XMLHolderSBDueList> lsData = new ArrayList<XMLHolderSBDueList>();

        /*
         * <POLICYNUMBER>14001623909</POLICYNUMBER>
         * <Customer_x0020_Code>573491</Customer_x0020_Code>
         * <LIFEASSUREDNAME>Dipankar Roy</LIFEASSUREDNAME>
         * <STATUS>Inforce</STATUS> <Benefit_x0020_Term>15</Benefit_x0020_Term>
         * <POLICYRISKCOMMENCEMENTDATE>2005-01-12T00:00:00-08:00</
         * POLICYRISKCOMMENCEMENTDATE>
         * <MATURITYDATE>2020-01-12T00:00:00-08:00</MATURITYDATE>
         * <PAYMENTAMOUNT>12500</PAYMENTAMOUNT> <First_x0020_Name>Dipankar
         * Roy</First_x0020_Name> <DOB>1969-12-31T00:00:00-08:00</DOB>
         * <CITY>DHARMANAGAR</CITY> <ADDRESS>NETAJI SUBHAS SARANI MADHYA
         * NAYAPARA (N) TRIPURA</ADDRESS>
         * <Postal_x0020_Code>799250</Postal_x0020_Code> <STATE>Tripura</STATE>
         * <CONTACT_NO_R >03822233837</CONTACT_NO_R >
         */
        for (String Node : lsNode) {
            String policyNumber = parseXmlTag(Node, "POLICYNUMBER");
            String customerCode = parseXmlTag(Node, "Customer_x0020_Code");
            String lifeAssuredName = parseXmlTag(Node, "LIFEASSUREDNAME");
            String status = parseXmlTag(Node, "STATUS");
            String benefitTerm = parseXmlTag(Node, "Benefit_x0020_Term");
            String policyRiskCommencementDate = parseXmlTag(Node,
                    "POLICYRISKCOMMENCEMENTDATE");

            String maturityDate = parseXmlTag(Node, "MATURITYDATE");
            String paymentAmount = parseXmlTag(Node, "PAYMENTAMOUNT");
            String firstName = parseXmlTag(Node, "First_x0020_Name");
            String dOB = parseXmlTag(Node, "DOB");
            String city = parseXmlTag(Node, "CITY");
            String address = parseXmlTag(Node, "ADDRESS");
            String postalCode = parseXmlTag(Node, "Postal_x0020_Code");
            String state = parseXmlTag(Node, "STATE");
            String contactNumber = parseXmlTag(Node,
                    "CONTACT_NO_R");

            SimpleDateFormat dfmaturity = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat df1maturity = new SimpleDateFormat("dd-MMMM-yyyy");
            if (policyRiskCommencementDate != null) {

                policyRiskCommencementDate = policyRiskCommencementDate
                        .split("T")[0];

                Date dt1maturity = null;

                try {
                    dt1maturity = dfmaturity
                            .parse(policyRiskCommencementDate);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                policyRiskCommencementDate = df1maturity.format(dt1maturity);

            } else {

                policyRiskCommencementDate = "";

            }

            if (maturityDate != null) {
                maturityDate = maturityDate.split("T")[0];

                Date dt1maturity = null;

                try {
                    dt1maturity = dfmaturity.parse(maturityDate);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                maturityDate = df1maturity.format(dt1maturity);
            } else {
                maturityDate = "";
            }
            if (dOB != null) {
                dOB = dOB.split("T")[0];
                Date dt1maturity = null;
                try {
                    dt1maturity = dfmaturity.parse(dOB);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                dOB = df1maturity.format(dt1maturity);

            } else {
                dOB = "";
            }

            XMLHolderSBDueList nodeVal = new XMLHolderSBDueList(policyNumber,
                    customerCode, lifeAssuredName, status, benefitTerm,
                    policyRiskCommencementDate, maturityDate, paymentAmount,
                    firstName, dOB, city, address, postalCode, state,
                    contactNumber);

            lsData.add(nodeVal);
        }
        return lsData;
    }

    public List<String> parseNodeElementpolicy_issu(List<String> lsNode) {

        List<String> lsData = new ArrayList<String>();

        for (String Node : lsNode) {

            String No = parseXmlTag(Node, "CEC_EXAMCENTER_NAME");

            // XMLHolderIssuPolicy nodeVal = new XMLHolderIssuPolicy(No);

            lsData.add(No);

        }

        return lsData;

    }

    public List<XMLHolderAdvanceQueryResult> parseNodeAdvanceQueryResult(List<String> lsNode) {

        List<XMLHolderAdvanceQueryResult> lsData = new ArrayList<XMLHolderAdvanceQueryResult>();

        String PAY_EX1_91, PAY_EX1_96, HTMLTEXT_280, PAY_EX1_95, PAY_EX1_94,
                ROWNUMBER, Key;
        for (String Node : lsNode) {

            PAY_EX1_91 = parseXmlTag(Node, "PAY_EX1_91");
            PAY_EX1_96 = parseXmlTag(Node, "PAY_EX1_96");
            HTMLTEXT_280 = parseXmlTag(Node, "HTMLTEXT_280");
            PAY_EX1_95 = parseXmlTag(Node, "PAY_EX1_95");
            PAY_EX1_94 = parseXmlTag(Node, "PAY_EX1_94");
            ROWNUMBER = parseXmlTag(Node, "ROWNUMBER");
            Key = parseXmlTag(Node, "Key");


            XMLHolderAdvanceQueryResult nodeVal = new XMLHolderAdvanceQueryResult(PAY_EX1_91,
                    PAY_EX1_96, HTMLTEXT_280, PAY_EX1_95,
                    PAY_EX1_94, ROWNUMBER, Key);
            lsData.add(nodeVal);
        }

        return lsData;
    }

    public ArrayList<String> parseURNNumberBAOnline(List<String> lsNode) {

        ArrayList<String> lsData = new ArrayList<String>();
        lsData.add("Select URN Number");
        for (String Node : lsNode) {

            String URNNumber = parseXmlTag(Node, "URN_NO");
            lsData.add(URNNumber);
        }

        return lsData;

    }

    public List<XMLHolderGroupTermCDStatement> parseNodeGroupTermCDStatement(List<String> lsNode) {

        List<XMLHolderGroupTermCDStatement> lsData = new ArrayList<XMLHolderGroupTermCDStatement>();

        String policy_no = "", policy_holder_name = "", doc = "", ard = "",
                transaction_date = "", transaction_type = "", description = "", transaction_amount = "",
                balance_amount = "", payment_purpose = "", in_out = "";
        for (String Node : lsNode) {

            policy_no = parseXmlTag(Node, "POLICY_NO");
            if (policy_no == null)
                policy_no = "";
            policy_holder_name = parseXmlTag(Node, "POLICY_HOLDER_NAME");
            if (policy_holder_name == null)
                policy_holder_name = "";
            doc = parseXmlTag(Node, "DOC");
            if (doc == null)
                doc = "";
            ard = parseXmlTag(Node, "ARD");
            if (ard == null)
                ard = "";
            transaction_date = parseXmlTag(Node, "TRANSACTION_DATE");
            if (transaction_date == null)
                transaction_date = "";
            transaction_type = parseXmlTag(Node, "TRANSACTION_TYPE");
            if (transaction_type == null)
                transaction_type = "";
            description = parseXmlTag(Node, "DESCRIPTION");
            if (description == null)
                description = "";
            transaction_amount = parseXmlTag(Node, "TRANSACTION_AMOUNT");
            if (transaction_amount == null)
                transaction_amount = "";
            balance_amount = parseXmlTag(Node, "BALANCE_AMOUNT");
            if (balance_amount == null)
                balance_amount = "";
            payment_purpose = parseXmlTag(Node, "PAYMENT_PURPOSE");
            if (payment_purpose == null)
                payment_purpose = "";
            in_out = parseXmlTag(Node, "IN_OUT");
            if (in_out == null)
                in_out = "";

            /*String []policyStartDateArray = POL_START_DATE.split("-");
            String day = policyStartDateArray[0];
            String month = policyStartDateArray[1];
            month = new CommonMethods().getFullMonthName(month);
            String year = policyStartDateArray[2];
            POL_START_DATE = day+"-" + month + "-"+year;

            String []policyRenewalDateArray = RENEWAL_DUE_DATE.split("-");
            day = policyRenewalDateArray[0];
            month = policyRenewalDateArray[1];
            month = new CommonMethods().getFullMonthName(month);
            year = policyRenewalDateArray[2];
            RENEWAL_DUE_DATE = day+"-" + month + "-"+year;*/


            XMLHolderGroupTermCDStatement nodeVal = new XMLHolderGroupTermCDStatement(policy_no, policy_holder_name, doc, ard,
                    transaction_date, transaction_type, description, transaction_amount,
                    balance_amount, payment_purpose, in_out);
            lsData.add(nodeVal);
        }

        return lsData;
    }

    public List<XMLHolderGroupMiniFundStatement> parseNodeGroupMiniFundStatement(List<String> lsNode) {

        List<XMLHolderGroupMiniFundStatement> lsData = new ArrayList<XMLHolderGroupMiniFundStatement>();

        String policy_no = "", billing_date = "", description = "", transaction_amount = "",
                balance = "";
        for (String Node : lsNode) {

            policy_no = parseXmlTag(Node, "POLICY_NO");
            if (policy_no == null)
                policy_no = "";
            billing_date = parseXmlTag(Node, "BILLING_DATE");
            if (billing_date == null)
                billing_date = "";
            description = parseXmlTag(Node, "DESCRIPTION");
            if (description == null)
                description = "";
            transaction_amount = parseXmlTag(Node, "TRANSACTION_AMOUNT");
            if (transaction_amount == null)
                transaction_amount = "";
            balance = parseXmlTag(Node, "BALANCE");
            if (balance == null)
                balance = "";

            XMLHolderGroupMiniFundStatement nodeVal = new XMLHolderGroupMiniFundStatement(policy_no, billing_date, description, transaction_amount,
                    balance);
            lsData.add(nodeVal);
        }

        return lsData;
    }

    public List<XMLHolderGroupMemberView> parseNodeGroupMemberView(List<String> lsNode) {

        List<XMLHolderGroupMemberView> lsData = new ArrayList<XMLHolderGroupMemberView>();

        String policy_no = "", policy_holder = "", str_doc = "", member_emp_id = "", member_name = "",
                member_rcd = "", member_sum_assured = "", rider_ppd = "", rider_AD = "", rider_cl = "", rider_tpd = "",
                rider_sum_assured = "", nominee_name = "", nominee_relationship = "";

        for (String Node : lsNode) {

            policy_no = parseXmlTag(Node, "MASTER_POLICY_NUMBER");
            if (policy_no == null)
                policy_no = "";

            policy_holder = parseXmlTag(Node, "MASTER_POLICY_HOLDER_NAME");
            if (policy_holder == null)
                policy_holder = "";

            str_doc = parseXmlTag(Node, "DOC");
            if (str_doc == null)
                str_doc = "";

            member_emp_id = parseXmlTag(Node, "EMPLOYEE_ID");
            if (member_emp_id == null)
                member_emp_id = "";

            member_name = parseXmlTag(Node, "MEMBER_NAME");
            if (member_name == null)
                member_name = "";

            member_rcd = parseXmlTag(Node, "DOJS");
            if (member_rcd == null)
                member_rcd = "";

            member_sum_assured = parseXmlTag(Node, "BASIC_SUM_ASSURED");
            if (member_sum_assured == null)
                member_sum_assured = "";

            rider_ppd = parseXmlTag(Node, "RIDER_PPD");
            if (rider_ppd == null)
                rider_ppd = "";

            rider_AD = parseXmlTag(Node, "RIDER_AD");
            if (rider_AD == null)
                rider_AD = "";

            rider_cl = parseXmlTag(Node, "RIDER_cI");
            if (rider_cl == null)
                rider_cl = "";

            rider_tpd = parseXmlTag(Node, "RIDER_TPD");
            if (rider_tpd == null)
                rider_tpd = "";

            rider_sum_assured = parseXmlTag(Node, "RIDER_SUM_ASSURED");
            if (rider_sum_assured == null)
                rider_sum_assured = "";

            nominee_name = parseXmlTag(Node, "NOMINEE_NAME");
            if (nominee_name == null)
                nominee_name = "";

            nominee_relationship = parseXmlTag(Node, "NOMINEE_RELATIONSHIP");
            if (nominee_relationship == null)
                nominee_relationship = "";

            XMLHolderGroupMemberView nodeVal = new XMLHolderGroupMemberView(policy_no, policy_holder, str_doc, member_emp_id, member_name,
                    member_rcd, member_sum_assured, rider_ppd, rider_AD, rider_cl, rider_tpd,
                    rider_sum_assured, nominee_name, nominee_relationship);
            lsData.add(nodeVal);
        }

        return lsData;
    }

    public List<XMLHolderGroupSearchClaimView> parseNodeGroupSearchClaimView(List<String> lsNode) {

        List<XMLHolderGroupSearchClaimView> lsData = new ArrayList<XMLHolderGroupSearchClaimView>();

        String claim_id = "", claim_status = "", employee_id = "", member_name = "", saction_date = "",
                claim_amount = "";

        for (String Node : lsNode) {

            claim_id = parseXmlTag(Node, "CLAIM_ID");
            if (claim_id == null)
                claim_id = "";

            claim_status = parseXmlTag(Node, "CLAIM_STATUS");
            if (claim_status == null)
                claim_status = "";

            employee_id = parseXmlTag(Node, "EMPLOYEE_ID");
            if (employee_id == null)
                employee_id = "";

            member_name = parseXmlTag(Node, "MEMBER_NAME");
            if (member_name == null)
                member_name = "";

            saction_date = parseXmlTag(Node, "CLAIM_SANCTION_DATE");
            if (saction_date == null)
                saction_date = "";

            claim_amount = parseXmlTag(Node, "CLAIM_SANCTION_AMOUNT");
            if (claim_amount == null)
                claim_amount = "";

            XMLHolderGroupSearchClaimView nodeVal = new XMLHolderGroupSearchClaimView(claim_id, claim_status, employee_id, member_name, saction_date,
                    claim_amount);
            lsData.add(nodeVal);
        }

        return lsData;
    }

    // for holding agent list which are come from server based on UM login

    public List<XMLHolderGroupFundSearchMemberView> parseNodeGroupFundSearchMemberView(List<String> lsNode) {

        List<XMLHolderGroupFundSearchMemberView> lsData = new ArrayList<XMLHolderGroupFundSearchMemberView>();

        String policy_no = "", policy_holder = "", str_doc = "", employee_id = "", member_name = "",
                balance_fund = "";

        for (String Node : lsNode) {

            policy_no = parseXmlTag(Node, "MASTER_POLICY_NUMBER");
            if (policy_no == null)
                policy_no = "";

            policy_holder = parseXmlTag(Node, "MASTER_POLICY_HOLDER_NAME");
            if (policy_holder == null)
                policy_holder = "";

            str_doc = parseXmlTag(Node, "DOC");
            if (str_doc == null)
                str_doc = "";

            employee_id = parseXmlTag(Node, "EMPLOYEE_ID");
            if (employee_id == null)
                employee_id = "";

            member_name = parseXmlTag(Node, "MEMBER_NAME");
            if (member_name == null)
                member_name = "";

            balance_fund = parseXmlTag(Node, "MEMBER_BALANCE_FUND");
            if (balance_fund == null)
                balance_fund = "";

            XMLHolderGroupFundSearchMemberView nodeVal = new XMLHolderGroupFundSearchMemberView(policy_no, policy_holder,
                    str_doc, employee_id, member_name, balance_fund);

            lsData.add(nodeVal);
        }

        return lsData;
    }

    public List<XMLHolderGroupFundSearchPolicy> parseNodeGroupFundSearchPolicy(List<String> lsNode) {

        List<XMLHolderGroupFundSearchPolicy> lsData = new ArrayList<XMLHolderGroupFundSearchPolicy>();

        String policy_no = "", policy_holder = "", str_doc = "", fund_balance = "", unadjusted_deposite = "",
                policy_status = "";

        for (String Node : lsNode) {

            policy_no = parseXmlTag(Node, "MASTER_POLICY_NUMBER");
            if (policy_no == null)
                policy_no = "";

            policy_holder = parseXmlTag(Node, "MASTER_POLICY_HOLDER_NAME");
            if (policy_holder == null)
                policy_holder = "";

            str_doc = parseXmlTag(Node, "DOC");
            if (str_doc == null)
                str_doc = "";

            fund_balance = parseXmlTag(Node, "FUND_BALANCE");
            if (fund_balance == null)
                fund_balance = "";

            unadjusted_deposite = parseXmlTag(Node, "UNADJUSTED_DEPOSIT");
            if (unadjusted_deposite == null)
                unadjusted_deposite = "";

            policy_status = parseXmlTag(Node, "POLICY_STATUS");
            if (policy_status == null)
                policy_status = "";

            XMLHolderGroupFundSearchPolicy nodeVal = new XMLHolderGroupFundSearchPolicy(policy_no, policy_holder,
                    str_doc, fund_balance, unadjusted_deposite, policy_status);

            lsData.add(nodeVal);
        }

        return lsData;
    }

    public List<XMLHolderGroupPolicySearch> parseNodeGroupPolicySearch(List<String> lsNode) {

        List<XMLHolderGroupPolicySearch> lsData = new ArrayList<XMLHolderGroupPolicySearch>();

        String policy_no = "", policy_holder = "", str_doc = "", strARD = "", total_premium = "",
                lives = "", unadjusted_deposite = "", rideropted = "", str_fcl = "", policy_status = "",
                basic_premium_rate = "", rider_premium_rate = "", total_sum_assured = "", type_scheme = "";

        for (String Node : lsNode) {

            policy_no = parseXmlTag(Node, "MASTER_POLICY_NUMBER");
            if (policy_no == null)
                policy_no = "";

            policy_holder = parseXmlTag(Node, "MASTER_POLICY_HOLDER_NAME");
            if (policy_holder == null)
                policy_holder = "";

            str_doc = parseXmlTag(Node, "DOC");
            if (str_doc == null)
                str_doc = "";

            strARD = parseXmlTag(Node, "ANNUAL_RENEWAL_DATE");
            if (strARD == null)
                strARD = "";

            total_premium = parseXmlTag(Node, "TOTAL_PREMIUM");
            if (total_premium == null)
                total_premium = "";

            lives = parseXmlTag(Node, "LIVES");
            if (lives == null)
                lives = "";

            unadjusted_deposite = parseXmlTag(Node, "UNADJUSTED_DEPOSIT");
            if (unadjusted_deposite == null)
                unadjusted_deposite = "";

            rideropted = parseXmlTag(Node, "RIDEROPTED");
            if (rideropted == null)
                rideropted = "";

            str_fcl = parseXmlTag(Node, "FCL");
            if (str_fcl == null)
                str_fcl = "";

            policy_status = parseXmlTag(Node, "POLICY_STATUS");
            if (policy_status == null)
                policy_status = "";

            basic_premium_rate = parseXmlTag(Node, "BASIC_PREMIUM_RATE");
            if (basic_premium_rate == null)
                basic_premium_rate = "";

            rider_premium_rate = parseXmlTag(Node, "RIDER_PREMIUM_RATE");
            if (rider_premium_rate == null)
                rider_premium_rate = "";

            total_sum_assured = parseXmlTag(Node, "TOTAL_SUM_ASSURED");
            if (total_sum_assured == null)
                total_sum_assured = "";

            type_scheme = parseXmlTag(Node, "TYPE_OF_SCHEME");
            if (type_scheme == null)
                type_scheme = "";

            XMLHolderGroupPolicySearch nodeVal = new XMLHolderGroupPolicySearch(policy_no, policy_holder, str_doc, strARD, total_premium,
                    lives, unadjusted_deposite, rideropted, str_fcl, policy_status,
                    basic_premium_rate, rider_premium_rate, total_sum_assured, type_scheme);
            lsData.add(nodeVal);
        }

        return lsData;
    }

    public List<XMLHolderGroupQuoteIDSearch> parseNodeGroupQuoteIDSearch(List<String> lsNode) {

        List<XMLHolderGroupQuoteIDSearch> lsData = new ArrayList<XMLHolderGroupQuoteIDSearch>();

        String policy_no = "", policy_holder = "", str_doc = "";

        for (String Node : lsNode) {

            policy_no = parseXmlTag(Node, "MASTER_POLICY_NUMBER");
            if (policy_no == null)
                policy_no = "";

            policy_holder = parseXmlTag(Node, "POLICY_HOLDER_NAME");
            if (policy_holder == null)
                policy_holder = "";

            str_doc = parseXmlTag(Node, "DOC");

            if (str_doc == null)
                str_doc = "";

            XMLHolderGroupQuoteIDSearch nodeVal = new XMLHolderGroupQuoteIDSearch(policy_no, policy_holder, str_doc);
            lsData.add(nodeVal);
        }

        return lsData;
    }

    public List<XMLHolderGroupUnderritting> parseNodeGroupUnderwritting(List<String> lsNode) {

        List<XMLHolderGroupUnderritting> lsData = new ArrayList<XMLHolderGroupUnderritting>();

        String member_name = "", member_id = "", requirement_raised = "";


        for (String Node : lsNode) {

            member_name = parseXmlTag(Node, "MEMBER_NAME");
            if (member_name == null)
                member_name = "";

            member_id = parseXmlTag(Node, "EMPLOYEE_ID");
            if (member_id == null)
                member_id = "";

            requirement_raised = parseXmlTag(Node, "REQUIREMENTS_RAISED");

            if (requirement_raised == null)
                requirement_raised = "";

            XMLHolderGroupUnderritting nodeVal = new XMLHolderGroupUnderritting(member_name, member_id, requirement_raised);
            lsData.add(nodeVal);
        }

        return lsData;
    }

    public List<XMLHolderGroupFundContribution> parseNodeGroupFundContribution(List<String> lsNode) {

        List<XMLHolderGroupFundContribution> lsData = new ArrayList<XMLHolderGroupFundContribution>();

        String premium_billed_date = "", amount = "";

        for (String Node : lsNode) {

            premium_billed_date = parseXmlTag(Node, "PREMIUM_BILLED_DATE");
            if (premium_billed_date == null)
                premium_billed_date = "";

            amount = parseXmlTag(Node, "AMOUNT");
            if (amount == null)
                amount = "";

            XMLHolderGroupFundContribution nodeVal = new XMLHolderGroupFundContribution(premium_billed_date, amount);
            lsData.add(nodeVal);
        }

        return lsData;
    }

    public List<XMLHolderGroupFundClaimList> parseNodeGroupFundClaim(List<String> lsNode) {

        List<XMLHolderGroupFundClaimList> lsData = new ArrayList<XMLHolderGroupFundClaimList>();

        String employee_id = "", MEMBER_NAME = "", CLAIM_SANCTION_DATE = "", CLAIM_AMOUNT = "";


        for (String Node : lsNode) {

            employee_id = parseXmlTag(Node, "EMPLOYEE_ID");
            if (employee_id == null)
                employee_id = "";

            MEMBER_NAME = parseXmlTag(Node, "MEMBER_NAME");
            if (MEMBER_NAME == null)
                MEMBER_NAME = "";
            CLAIM_SANCTION_DATE = parseXmlTag(Node, "CLAIM_SANCTION_DATE");

            if (CLAIM_SANCTION_DATE == null)
                CLAIM_SANCTION_DATE = "";


            CLAIM_AMOUNT = parseXmlTag(Node, "CLAIM_SANCTION_AMOUNT");
            if (CLAIM_AMOUNT == null)
                CLAIM_AMOUNT = "";

            XMLHolderGroupFundClaimList nodeVal = new XMLHolderGroupFundClaimList(employee_id, MEMBER_NAME, CLAIM_SANCTION_DATE, CLAIM_AMOUNT);
            lsData.add(nodeVal);
        }

        return lsData;
    }

    public List<XMLHolderGroupNewBusinessList> parseNodeGroupNewBusinessList(List<String> lsNode, String strBusinessType) {

        List<XMLHolderGroupNewBusinessList> lsData = new ArrayList<XMLHolderGroupNewBusinessList>();

        String POLICY_NO = "", POLICY_HOLDER_NAME = "", POL_START_DATE = "";
        for (String Node : lsNode) {

            if (strBusinessType.equalsIgnoreCase("FUND")) {
                POLICY_NO = parseXmlTag(Node, "MASTER_POLICY_NO");
                POLICY_HOLDER_NAME = parseXmlTag(Node, "MASTER_POLICY_HOLDER_NAME");
                POL_START_DATE = parseXmlTag(Node, "PRODUCT_CODE");
            } else {
                POLICY_NO = parseXmlTag(Node, "MASTER_POLICY_NUMBER");
                POLICY_HOLDER_NAME = parseXmlTag(Node, "POLICY_HOLDER_NAME");
                POL_START_DATE = parseXmlTag(Node, "DOC");

                String[] policyStartDateArray = POL_START_DATE.split("-");
                String day = policyStartDateArray[0];
                String month = policyStartDateArray[1];
                month = new CommonMethods().getFullMonthName(month);
                String year = policyStartDateArray[2];
                POL_START_DATE = day + "-" + month + "-" + year;
            }

            XMLHolderGroupNewBusinessList nodeVal = new XMLHolderGroupNewBusinessList(POLICY_NO,
                    POLICY_HOLDER_NAME, POL_START_DATE);
            lsData.add(nodeVal);
        }

        return lsData;
    }

    public List<XMLHolderGroupTermLapseList> parseNodeGroupTermLapseList(List<String> lsNode) {

        List<XMLHolderGroupTermLapseList> lsData = new ArrayList<XMLHolderGroupTermLapseList>();

        String POLICY_NO = "", POLICY_HOLDER_NAME = "", POL_START_DATE = "";
        for (String Node : lsNode) {

            POLICY_NO = parseXmlTag(Node, "MASTER_POLICY_NO");
            POLICY_HOLDER_NAME = parseXmlTag(Node, "MASTER_POLICY_HOLDER_NAME");
            POL_START_DATE = parseXmlTag(Node, "POLICY_EXPIRY_DATE");
            //10-06-2010
            //09-07-2017

            String[] policyStartDateArray = POL_START_DATE.split("-");
            String day = policyStartDateArray[0];
            String month = policyStartDateArray[1];
            month = new CommonMethods().getFullMonthName(month);
            String year = policyStartDateArray[2];
            POL_START_DATE = day + "-" + month + "-" + year;


            XMLHolderGroupTermLapseList nodeVal = new XMLHolderGroupTermLapseList(POLICY_NO, POLICY_HOLDER_NAME, POL_START_DATE);
            lsData.add(nodeVal);
        }

        return lsData;
    }

    public List<XMLHolderGroupTermRenewalList> parseNodeGroupTermRenewalList(List<String> lsNode) {

        List<XMLHolderGroupTermRenewalList> lsData = new ArrayList<XMLHolderGroupTermRenewalList>();

        String POLICY_NO = "", POLICY_HOLDER_NAME = "", POL_START_DATE = "";
        for (String Node : lsNode) {

            POLICY_NO = parseXmlTag(Node, "MASTER_POLICY_NO");
            POLICY_HOLDER_NAME = parseXmlTag(Node, "MASTER_POLICY_HOLDER_NAME");
            POL_START_DATE = parseXmlTag(Node, "ARD");

            String[] policyStartDateArray = POL_START_DATE.split("-");
            String day = policyStartDateArray[0];
            String month = policyStartDateArray[1];
            month = new CommonMethods().getFullMonthName(month);
            String year = policyStartDateArray[2];
            POL_START_DATE = day + "-" + month + "-" + year;

            XMLHolderGroupTermRenewalList nodeVal = new XMLHolderGroupTermRenewalList(POLICY_NO, POLICY_HOLDER_NAME, POL_START_DATE);
            lsData.add(nodeVal);
        }

        return lsData;
    }

    /*<Data> <Table>
        <POLICY_NO>2DQS625395</POLICY_NO>
        <FULLNAME>MADHUKARBORIKAR</FULLNAME>
        <CASHIERINGDATE>13-10-2017</CASHIERINGDATE> </Table>
        <Table>
        <POLICY_NO>1KAP538791</POLICY_NO>
        <FULLNAME>GULABCHAVHAN</FULLNAME>
        <CASHIERINGDATE>13-10-2017</CASHIERINGDATE>
        </Table>
        <Table>
        <POLICY_NO>2DQV544112</POLICY_NO>
        <FULLNAME>MADHUKARBORIKAR</FULLNAME>
        <CASHIERINGDATE>13-10-2017</CASHIERINGDATE>
        </Table> </Data>*/
    public List<EFTPendingForm> parseNodeEFTNode(List<String> lsNode) {

        List<EFTPendingForm> lsData = new ArrayList<EFTPendingForm>();

        String POLICY_NO = "", FULLNAME = "", CASHIERINGDATE = "";
        for (String Node : lsNode) {

            POLICY_NO = parseXmlTag(Node, "POLICY_NO");
            FULLNAME = parseXmlTag(Node, "FULLNAME");
            CASHIERINGDATE = parseXmlTag(Node, "CASHIERINGDATE");

            String[] policyStartDateArray = CASHIERINGDATE.split("-");
            String day = policyStartDateArray[0];
            String month = policyStartDateArray[1];
            month = new CommonMethods().getShortMonthName(month);

            String year = policyStartDateArray[2];
            CASHIERINGDATE = day + "-" + month + "-" + year;

            EFTPendingForm nodeVal = new EFTPendingForm(POLICY_NO, FULLNAME, CASHIERINGDATE);
            lsData.add(nodeVal);

        }

        return lsData;
    }

    public ArrayList<XMLHolderEkycDetails> parseNodeElementEkycDetails(List<String> lsNode, final String str_type) {

        ArrayList<XMLHolderEkycDetails> lsData = new ArrayList<XMLHolderEkycDetails>();

        for (String Node : lsNode) {

            String pol_pro_no = parseXmlTag(Node, "PROPOSALPOLICY");

            String date_time = parseXmlTag(Node, "CREATEDDATE");

            XMLHolderEkycDetails nodeVal = new XMLHolderEkycDetails(pol_pro_no, date_time, str_type);

            lsData.add(nodeVal);
        }
        return lsData;
    }

    /*<PROPOSAL_NO>1RQU130942</PROPOSAL_NO><TAT>0</TAT>
        <BANK_BRANCH_NAME>GODOWN ROAD NIZAMABAD</BANK_BRANCH_NAME>
        <CIF_CODE>990085189</CIF_CODE>
        <CIF_NAME>MURALIMOHANSAMALA</CIF_NAME>
        <CASHIERINGDATE>17-10-2017</CASHIERINGDATE>*/
    public List<EFTFormNotReceived> parseNodeEFTFormNotReceivedNode(List<String> lsNode) {

        List<EFTFormNotReceived> lsData = new ArrayList<EFTFormNotReceived>();

        String PROPOSAL_NO = "", TAT = "", BANK_BRANCH_NAME = "", CIF_CODE = "", CIF_NAME = "", CASHIERINGDATE = "";
        for (String Node : lsNode) {

            PROPOSAL_NO = parseXmlTag(Node, "PROPOSAL_NO");
            TAT = parseXmlTag(Node, "TAT");
            BANK_BRANCH_NAME = parseXmlTag(Node, "BANK_BRANCH_NAME");
            /*CIF_CODE = parseXmlTag(Node, "CIF_CODE");*/
            CIF_CODE = parseXmlTag(Node, "CLIENT_NAME");
            CIF_NAME = parseXmlTag(Node, "CIF_NAME");
            CASHIERINGDATE = parseXmlTag(Node, "CASHIERINGDATE");

            String[] policyStartDateArray = CASHIERINGDATE.split("-");
            String day = policyStartDateArray[0];
            String month = policyStartDateArray[1];
            month = new CommonMethods().getFullMonthName(month);
            String year = policyStartDateArray[2];
            CASHIERINGDATE = day + "-" + month + "-" + year;

            EFTFormNotReceived nodeVal = new EFTFormNotReceived(PROPOSAL_NO, TAT, BANK_BRANCH_NAME, CIF_CODE, CIF_NAME, CASHIERINGDATE);
            lsData.add(nodeVal);
        }

        return lsData;
    }

    public List<ChannelUserReportsValuesModel> parseNodeChannelUserReports(List<String> lsNode) {
        List<ChannelUserReportsValuesModel> lsData = new ArrayList<>();

        String EMPLOYEEID = "", EMPLOYEENAME = "", POSITIONTYPE = "", CONTACTMOBILE = "",
                CONTACTEMAIL = "";
        for (String Node : lsNode) {

            EMPLOYEEID = parseXmlTag(Node, "EMPLOYEEID");
            EMPLOYEENAME = parseXmlTag(Node, "EMPLOYEENAME");
            POSITIONTYPE = parseXmlTag(Node, "POSITIONTYPE");
            CONTACTMOBILE = parseXmlTag(Node, "CONTACTMOBILE");
            CONTACTMOBILE = CONTACTMOBILE == null ? "" : CONTACTMOBILE;
            CONTACTEMAIL = parseXmlTag(Node, "CONTACTEMAIL");
            ChannelUserReportsValuesModel nodeVal = new ChannelUserReportsValuesModel(EMPLOYEEID, EMPLOYEENAME, POSITIONTYPE, CONTACTMOBILE, CONTACTEMAIL);
            lsData.add(nodeVal);
        }
        return lsData;
    }

    public List<AOBPANPendingAgentListValuesModel> parseNodeAOBPANPendingAgentList(List<String> lsNode) {
        List<AOBPANPendingAgentListValuesModel> lsData = new ArrayList<>();

        String UM_CODE = "", PER_FULL_NAME = "", PER_PAN_NO = "", status = "", remarks = "",
                enrollment_type = "";
        for (String Node : lsNode) {

            UM_CODE = parseXmlTag(Node, "UM_CODE");
            PER_FULL_NAME = parseXmlTag(Node, "PER_FULL_NAME");
            PER_PAN_NO = parseXmlTag(Node, "PER_PAN_NO");
            status = parseXmlTag(Node, "STATUS");
            status = status == null ? "" : status;
            remarks = parseXmlTag(Node, "REMARKS");
            remarks = remarks == null ? "" : remarks;
            enrollment_type = parseXmlTag(Node, "sub_type");
            enrollment_type = enrollment_type == null ? "" : enrollment_type;

            AOBPANPendingAgentListValuesModel nodeVal = new AOBPANPendingAgentListValuesModel(UM_CODE,
                    PER_PAN_NO, PER_FULL_NAME, status, remarks, enrollment_type);
            lsData.add(nodeVal);
        }
        return lsData;
    }

    public List<AutoMandateStatusListValuesModel> parseNodeAutoMandateStatusList(List<String> lsNode) {
        List<AutoMandateStatusListValuesModel> lsData = new ArrayList<>();

        String POLICY_NO = "", POL_HOLDER_NAME = "", MANDATE_TYPE = "", MANDATE_STATUS = "",
                STATUS_UPDATE_DATE = "", POLICY_STATUS = "", REJECTION_REASON = "";
        for (String Node : lsNode) {

            POLICY_NO = parseXmlTag(Node, "POLICY_NO");
            POL_HOLDER_NAME = parseXmlTag(Node, "POL_HOLDER_NAME");
            MANDATE_TYPE = parseXmlTag(Node, "MANDATE_TYPE");
            MANDATE_STATUS = parseXmlTag(Node, "MANDATE_STATUS");
            STATUS_UPDATE_DATE = parseXmlTag(Node, "STATUS_UPDATE_DATE");

            if (STATUS_UPDATE_DATE == null) {
                STATUS_UPDATE_DATE = "";
            } else {

                STATUS_UPDATE_DATE = STATUS_UPDATE_DATE.split("T")[0];

                Date dt1 = null;
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat df1 = new SimpleDateFormat("dd-MMMM-yyyy");
                try {
                    dt1 = df.parse(STATUS_UPDATE_DATE);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                STATUS_UPDATE_DATE = df1.format(dt1);

            }

            POLICY_STATUS = parseXmlTag(Node, "POLICY_STATUS");
            REJECTION_REASON = parseXmlTag(Node, "REJECTION_REASON");

            AutoMandateStatusListValuesModel nodeVal = new AutoMandateStatusListValuesModel(POLICY_NO, POL_HOLDER_NAME, MANDATE_TYPE,
                    MANDATE_STATUS, STATUS_UPDATE_DATE, POLICY_STATUS, REJECTION_REASON);
            lsData.add(nodeVal);
        }
        return lsData;
    }


    public List<ProposalListValuesModel> parseNodeProposalList(List<String> lsNode) {
        List<ProposalListValuesModel> lsData = new ArrayList<>();

        String CHANNELCODE = "", PROPOSAL_NO = "", FULLNAME = "", STATUS = "";
        for (String Node : lsNode) {

            PROPOSAL_NO = parseXmlTag(Node, "PROPOSAL_NO");
            FULLNAME = parseXmlTag(Node, "FULLNAME");
            STATUS = parseXmlTag(Node, "STATUS");
            CHANNELCODE = parseXmlTag(Node, "CHANNELCODE");
            ProposalListValuesModel nodeVal = new ProposalListValuesModel(PROPOSAL_NO, FULLNAME, STATUS, CHANNELCODE);
            lsData.add(nodeVal);
        }
        return lsData;
    }

    public List<MedicalPendingRequirementValuesModel> parseNodeMedicalPendingRequirement(List<String> lsNode) {
        List<MedicalPendingRequirementValuesModel> lsData = new ArrayList<>();
            /*<Table>
                <POLICYPROPOSALNUMBER>1KNA365943</POLICYPROPOSALNUMBER>
                <PAYMENTAMOUNT>99900</PAYMENTAMOUNT>
                <CASHIERINGDATE>27-07-2018</CASHIERINGDATE>
                <STATUS>MEDICAL</STATUS>
                <PENDINGSTATUS>2D ECHO DOPPLER</PENDINGSTATUS>
                <CHANNELCODE>990611230</CHANNELCODE>
              </Table>*/


        String POLICYPROPOSALNUMBER, PAYMENTAMOUNT, CASHIERINGDATE, STATUS, CHANNELCODE, PENDINGSTATUS;
        for (String Node : lsNode) {

            POLICYPROPOSALNUMBER = parseXmlTag(Node, "POLICYPROPOSALNUMBER");
            PAYMENTAMOUNT = parseXmlTag(Node, "PAYMENTAMOUNT");
            CASHIERINGDATE = parseXmlTag(Node, "CASHIERINGDATE");
            STATUS = parseXmlTag(Node, "STATUS");
            CHANNELCODE = parseXmlTag(Node, "CHANNELCODE");

            if (STATUS.equalsIgnoreCase("NON MEDICAL")
                    || STATUS.equalsIgnoreCase("MEDICAL")) {
                PENDINGSTATUS = parseXmlTag(Node, "PENDINGSTATUS");
            } else {
                PENDINGSTATUS = "";
            }

            MedicalPendingRequirementValuesModel nodeVal = new MedicalPendingRequirementValuesModel(POLICYPROPOSALNUMBER, PAYMENTAMOUNT, CASHIERINGDATE, STATUS, CHANNELCODE, PENDINGSTATUS);
            lsData.add(nodeVal);
        }
        return lsData;
    }

    public List<PIWCRinnStatusValuesModel> parseNodePIWCRinnStatus(List<String> lsNode) {
        List<PIWCRinnStatusValuesModel> lsData = new ArrayList<>();

        String FORM_NO;
        String NAME;
        String STATUS;
        String PIWC_CALL_DATE;
        String PREM_AMT;
        String REC_ADD_DT;

        for (String Node : lsNode) {

            FORM_NO = parseXmlTag(Node, "FORM_NO");
            NAME = parseXmlTag(Node, "NAME");
            STATUS = parseXmlTag(Node, "STATUS");
            //SUB_STATUS = parseXmlTag(Node, "SUB_STATUS");
            PIWC_CALL_DATE = parseXmlTag(Node, "PIWC_CALL_DATE");
            PREM_AMT = parseXmlTag(Node, "PREM_AMT");
            REC_ADD_DT = parseXmlTag(Node, "REC_ADD_DT");
            PIWCRinnStatusValuesModel nodeVal = new PIWCRinnStatusValuesModel(FORM_NO, NAME, STATUS, "",
                    PIWC_CALL_DATE, PREM_AMT, REC_ADD_DT);
            lsData.add(nodeVal);
        }
        return lsData;
    }

    public List<ProposalRinnStatusValuesModel> parseNodeProposalRinnStatusTracker(List<String> lsNode) {
        List<ProposalRinnStatusValuesModel> lsData = new ArrayList<>();
        String FORM_NO;
        String LAN;
        String STATUS;
        String LOAN_CATEG;
        String REQ_TYPE;
        String NON_MED_DESC;
        String MED_DESC;

        String NON_MED_COMMENTS;
        String BORROWER_TYPE;
        String BANK_NAME;
        String BRANCH_NAME;
        String POLICY_HOLDER_NAME;
        String MED_COMMENTS;
        String PIWC_STATUS;
        String PIWC_SUB_STATUS;
        String EQ_STATUS_NON_MEDICAL;
        String REQ_STATUS_MEDICAL;

        for (String Node : lsNode) {

            FORM_NO = parseXmlTag(Node, "FORM_NO");
            LAN = parseXmlTag(Node, "LAN");
            STATUS = parseXmlTag(Node, "STATUS");

            REQ_TYPE = parseXmlTag(Node, "REQ_TYPE");
            NON_MED_DESC = parseXmlTag(Node, "NON_MED_DESC");
            MED_DESC = parseXmlTag(Node, "MED_DESC");
            NON_MED_COMMENTS = parseXmlTag(Node, "NON_MED_COMMENTS");
            MED_COMMENTS = parseXmlTag(Node, "MED_COMMENTS");

            PIWC_STATUS = parseXmlTag(Node, "PIWC_STATUS");
            PIWC_SUB_STATUS = parseXmlTag(Node, "PIWC_SUB_STATUS");

            EQ_STATUS_NON_MEDICAL = parseXmlTag(Node, "REQ_STATUS_NON_MEDICAL");
            REQ_STATUS_MEDICAL = parseXmlTag(Node, "REQ_STATUS_MEDICAL");

            LOAN_CATEG = parseXmlTag(Node, "LOAN_CATEG");
            BORROWER_TYPE = parseXmlTag(Node, "BORROWER_TYPE");
            BANK_NAME = parseXmlTag(Node, "BANK_NAME");
            BRANCH_NAME = parseXmlTag(Node, "BRANCH_NAME");
            POLICY_HOLDER_NAME = parseXmlTag(Node, "POLICY_HOLDER_NAME");


            //NOn medical Description
            String nonMedicalDescString = NON_MED_COMMENTS == null ? "" : NON_MED_COMMENTS;
            HashSet<String> nonMedicalDescHashSet = new HashSet<String>(Arrays.asList(nonMedicalDescString.split(",")));
            NON_MED_COMMENTS = StringUtils.join(nonMedicalDescHashSet, ',');

            String nonMedicalReqString = NON_MED_DESC == null ? "" : NON_MED_DESC;
            HashSet<String> nonMedicalHashSet = new HashSet<String>(Arrays.asList(nonMedicalReqString.split(",")));
            NON_MED_DESC = StringUtils.join(nonMedicalHashSet, ',');

            //medical Description
            String medicalDescString = MED_COMMENTS == null ? "" : MED_COMMENTS;
            HashSet<String> medicalDescHashSet = new HashSet<String>(Arrays.asList(medicalDescString.split(",")));
            MED_COMMENTS = StringUtils.join(medicalDescHashSet, ',');

            MED_DESC = MED_DESC == null ? "" : MED_DESC;

            String requirementType = REQ_TYPE == null ? "" : REQ_TYPE;
            HashSet<String> hashSet = new HashSet<String>(Arrays.asList(requirementType.split(",")));
            // requirementType = hashSet.toString();
            requirementType = StringUtils.join(hashSet, ',');

            REQ_TYPE = requirementType;

            ProposalRinnStatusValuesModel nodeVal = new ProposalRinnStatusValuesModel(FORM_NO, LAN, STATUS, LOAN_CATEG, REQ_TYPE, NON_MED_DESC, MED_DESC, NON_MED_COMMENTS, BORROWER_TYPE, BANK_NAME, BRANCH_NAME, POLICY_HOLDER_NAME, MED_COMMENTS, PIWC_STATUS, PIWC_SUB_STATUS, EQ_STATUS_NON_MEDICAL, REQ_STATUS_MEDICAL);
            lsData.add(nodeVal);
        }
        return lsData;
    }

    public List<RevivalCampaignValuesModel> parseNodeRevivalCampaign(List<String> lsNode) {
        List<RevivalCampaignValuesModel> lsData = new ArrayList<>();

        String POLICYNUMBER;
        String NAME;
        String MOBILE;
        String EMAIL;

        for (String Node : lsNode) {

            POLICYNUMBER = parseXmlTag(Node, "POLICYNUMBER");
            POLICYNUMBER = POLICYNUMBER == null ? "" : POLICYNUMBER;

            NAME = parseXmlTag(Node, "NAME");
            NAME = NAME == null ? "" : NAME;

            MOBILE = parseXmlTag(Node, "MOBILE");
            MOBILE = MOBILE == null ? "" : MOBILE;

            EMAIL = parseXmlTag(Node, "EMAIL");
            EMAIL = EMAIL == null ? "" : EMAIL;

            RevivalCampaignValuesModel nodeVal = new RevivalCampaignValuesModel(POLICYNUMBER, NAME, MOBILE, EMAIL);
            lsData.add(nodeVal);
        }
        return lsData;
    }

    public List<XMLQuotationSchedule> parseNodeElementQuotationSchedule(List<String> lsNode) {

        List<XMLQuotationSchedule> lsData = new ArrayList<>();

        for (String Node : lsNode) {


            String if_paid_by = parseXmlTag(Node, "paid_by");
            String latest_due = parseXmlTag(Node, "latest_due");
            String no_of_premium = parseXmlTag(Node, "no_premiums");
            String total_amount = parseXmlTag(Node, "total_amount");
            String rate_of_interest = parseXmlTag(Node, "rate_of_interest");
            String late_fee = parseXmlTag(Node, "late_fee");
            String interest_waived = parseXmlTag(Node, "interest_waived");
            String other_dues_pending = parseXmlTag(Node, "other_dues");
            String policy_deposit = parseXmlTag(Node, "policy_deposit");
            String net_revival_amount = parseXmlTag(Node, "net_amount");

            XMLQuotationSchedule nodeVal = new XMLQuotationSchedule(if_paid_by, latest_due, no_of_premium, total_amount, rate_of_interest, late_fee, interest_waived, other_dues_pending, policy_deposit, net_revival_amount);
            lsData.add(nodeVal);

        }

        return lsData;

    }

    public List<HashMap<String, String>> parseNodeElementBreakupDues(List<String> lsNode) {

        List<HashMap<String, String>> lsData = new ArrayList<>();


        for (String Node : lsNode) {

            HashMap<String, String> hmNode = new HashMap<>();
            hmNode.put("bank_charges", parseXmlTag(Node, "bank_charges"));
            hmNode.put("backdated_interest", parseXmlTag(Node, "backdated_interest"));
            hmNode.put("earlier_shortfall", parseXmlTag(Node, "earlier_shortfall"));
            hmNode.put("duplicate_policy", parseXmlTag(Node, "duplicate_policy"));

            lsData.add(hmNode);

        }

        return lsData;

    }

    public List<AutoMandatePenetrationDetailsValuesModel> parseNodeAutoMandatePenetrationDetails(List<String> lsNode) {
        List<AutoMandatePenetrationDetailsValuesModel> lsData = new ArrayList<>();

        String POL_HOLDER_NAME, POLICYCURRENTSTATUS, CONTACT_MOBILE, POLICY_NO, MANDATE_TYPE, MANDATE_STATUS, REJECTION_REASON, MANDATE_RCVD_DATE, SEQ;

        for (String Node : lsNode) {


            POL_HOLDER_NAME = parseXmlTag(Node, "POL_HOLDER_NAME");
            POL_HOLDER_NAME = POL_HOLDER_NAME == null ? "" : POL_HOLDER_NAME;

            POLICYCURRENTSTATUS = parseXmlTag(Node, "POLICYCURRENTSTATUS");
            POLICYCURRENTSTATUS = POLICYCURRENTSTATUS == null ? "" : POLICYCURRENTSTATUS;

            CONTACT_MOBILE = parseXmlTag(Node, "CONTACT_MOBILE");
            CONTACT_MOBILE = CONTACT_MOBILE == null ? "" : CONTACT_MOBILE;

            POLICY_NO = parseXmlTag(Node, "POLICY_NO");
            POLICY_NO = POLICY_NO == null ? "" : POLICY_NO;

            MANDATE_TYPE = parseXmlTag(Node, "MANDATE_TYPE");
            MANDATE_TYPE = MANDATE_TYPE == null ? "" : MANDATE_TYPE;

            MANDATE_STATUS = parseXmlTag(Node, "MANDATE_STATUS");
            MANDATE_STATUS = MANDATE_STATUS == null ? "" : MANDATE_STATUS;

            if (MANDATE_STATUS.toUpperCase().contains("REJECTED")) {
                MANDATE_STATUS = "REJECTED";
            } else if (!TextUtils.isEmpty(MANDATE_STATUS) && !MANDATE_STATUS.toUpperCase().contains("REGISTER")
                    && !MANDATE_STATUS.toUpperCase().contains("DEACTIVATED")) {
                MANDATE_STATUS = "In Process";
            }

            REJECTION_REASON = parseXmlTag(Node, "REJECTION_REASON");
            REJECTION_REASON = REJECTION_REASON == null ? "" : REJECTION_REASON;

            MANDATE_RCVD_DATE = parseXmlTag(Node, "MANDATE_RCVD_DATE");
            MANDATE_RCVD_DATE = MANDATE_RCVD_DATE == null ? "" : MANDATE_RCVD_DATE;

            if (!TextUtils.isEmpty(MANDATE_RCVD_DATE)) {
                MANDATE_RCVD_DATE = MANDATE_RCVD_DATE.split("T")[0];

                Date dt1 = null;
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat df1 = new SimpleDateFormat("dd-MMMM-yyyy");
                try {
                    dt1 = df.parse(MANDATE_RCVD_DATE);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                MANDATE_RCVD_DATE = df1.format(dt1);
            }

            SEQ = parseXmlTag(Node, "SEQ");
            SEQ = SEQ == null ? "" : SEQ;

            AutoMandatePenetrationDetailsValuesModel nodeVal = new AutoMandatePenetrationDetailsValuesModel(POL_HOLDER_NAME, POLICYCURRENTSTATUS, CONTACT_MOBILE, POLICY_NO, MANDATE_TYPE, MANDATE_STATUS, REJECTION_REASON, MANDATE_RCVD_DATE, SEQ);
            lsData.add(nodeVal);
        }
        return lsData;
    }

    public ArrayList<AlternateModeCollectionStatusValuesModel> parseNodeAlternateModeCollectionStatus(List<String> lsNode) {
        ArrayList<AlternateModeCollectionStatusValuesModel> lsData = new ArrayList<>();

        String TDF_POLICY_NUMBER, HOLDER_NAME, TDF_DUE_AMOUNT, TDF_DUE_DATE,
                TDF_DATE_SENT, TDF_STATUS, TDF_FAILURE_REASON, TDF_DEBIT_DATE, PAYMENT_MECHANISM;

        for (String Node : lsNode) {

            TDF_POLICY_NUMBER = parseXmlTag(Node, "TDF_POLICY_NUMBER");
            TDF_POLICY_NUMBER = TDF_POLICY_NUMBER == null ? "" : TDF_POLICY_NUMBER;

            HOLDER_NAME = parseXmlTag(Node, "HOLDER_NAME");
            HOLDER_NAME = HOLDER_NAME == null ? "" : HOLDER_NAME;

            TDF_DUE_AMOUNT = parseXmlTag(Node, "TDF_DUE_AMOUNT");
            TDF_DUE_AMOUNT = TDF_DUE_AMOUNT == null ? "" : TDF_DUE_AMOUNT;

            TDF_DATE_SENT = parseXmlTag(Node, "TDF_DATE_SENT");
            TDF_DATE_SENT = TDF_DATE_SENT == null ? "" : TDF_DATE_SENT;

            TDF_STATUS = parseXmlTag(Node, "TDF_STATUS");
            TDF_STATUS = TDF_STATUS == null ? "" : TDF_STATUS;

            TDF_FAILURE_REASON = parseXmlTag(Node, "TDF_FAILURE_REASON");
            TDF_FAILURE_REASON = TDF_FAILURE_REASON == null ? "" : TDF_FAILURE_REASON;


            TDF_DEBIT_DATE = parseXmlTag(Node, "TDF_DEBIT_DATE");
            TDF_DEBIT_DATE = TDF_DEBIT_DATE == null ? "" : TDF_DEBIT_DATE;

            TDF_DUE_DATE = parseXmlTag(Node, "TDF_DUE_DATE");
            TDF_DUE_DATE = TDF_DUE_DATE == null ? "" : TDF_DUE_DATE;

            PAYMENT_MECHANISM = parseXmlTag(Node, "PAYMENT_MECHANISM");
            PAYMENT_MECHANISM = PAYMENT_MECHANISM == null ? "" : PAYMENT_MECHANISM;


            AlternateModeCollectionStatusValuesModel nodeVal = new AlternateModeCollectionStatusValuesModel(TDF_POLICY_NUMBER, HOLDER_NAME, TDF_DUE_AMOUNT, TDF_DUE_DATE, TDF_DATE_SENT, TDF_STATUS, TDF_FAILURE_REASON, TDF_DEBIT_DATE, PAYMENT_MECHANISM);
            lsData.add(nodeVal);
        }
        return lsData;
    }

    public ArrayList<ViewMedicalStatusvaluesModel> parseNodeViewMedicalStatus(List<String> lsNode) {
        ArrayList<ViewMedicalStatusvaluesModel> lsData = new ArrayList<>();

        String PROPOSAL_NO, CUSTOMER_NAME, TPA_NAME, INTIMATION_DATE,
                MEDICAL_DONE_DATE, TEST_NAME, MAIN_STATUS, SUB_STATUS;

        for (String Node : lsNode) {

            PROPOSAL_NO = parseXmlTag(Node, "PROPOSAL_NO");
            PROPOSAL_NO = PROPOSAL_NO == null ? "" : PROPOSAL_NO;

            CUSTOMER_NAME = parseXmlTag(Node, "CUSTOMER_NAME");
            CUSTOMER_NAME = CUSTOMER_NAME == null ? "" : CUSTOMER_NAME;

            TPA_NAME = parseXmlTag(Node, "TPA_NAME");
            TPA_NAME = TPA_NAME == null ? "" : TPA_NAME;

            INTIMATION_DATE = parseXmlTag(Node, "INTIMATION_DATE");
            INTIMATION_DATE = INTIMATION_DATE == null ? "" : INTIMATION_DATE;

            MEDICAL_DONE_DATE = parseXmlTag(Node, "MEDICAL_DONE_DATE");
            MEDICAL_DONE_DATE = MEDICAL_DONE_DATE == null ? "" : MEDICAL_DONE_DATE;

            TEST_NAME = parseXmlTag(Node, "TEST_NAME");
            TEST_NAME = TEST_NAME == null ? "" : TEST_NAME;


            MAIN_STATUS = parseXmlTag(Node, "MAIN_STATUS");
            MAIN_STATUS = MAIN_STATUS == null ? "" : MAIN_STATUS;

            SUB_STATUS = parseXmlTag(Node, "SUB_STATUS");
            SUB_STATUS = SUB_STATUS == null ? "" : SUB_STATUS;


            ViewMedicalStatusvaluesModel nodeVal = new ViewMedicalStatusvaluesModel(PROPOSAL_NO, CUSTOMER_NAME, TPA_NAME, INTIMATION_DATE, MEDICAL_DONE_DATE, TEST_NAME, MAIN_STATUS, SUB_STATUS);
            lsData.add(nodeVal);
        }
        return lsData;
    }

    public ArrayList<SPListValueModel> parseNodeSPListDeatils(List<String> lsNode) {

        /*<CIFPolicyList> <Table> <CHANNELTECHNICALID>990852220</CHANNELTECHNICALID> <UM_BANKNAME>Stock Holding Corporation Of India Limited</UM_BANKNAME> <BANKNAME>Sriganganagar</BANKNAME> </Table> <Table> </CIFPolicyList>*/

        ArrayList<SPListValueModel> lsData = new ArrayList<>();

        String strChannelID = "", strUMBankName = "", strBankName = "";

        for (String Node : lsNode) {

            strChannelID = parseXmlTag(Node, "CHANNELTECHNICALID");
            strChannelID = strChannelID == null ? "" : strChannelID;

            strUMBankName = parseXmlTag(Node, "UM_BANKNAME");
            strUMBankName = strUMBankName == null ? "" : strUMBankName;

            strBankName = parseXmlTag(Node, "BANKNAME");
            strBankName = strBankName == null ? "" : strBankName;

            SPListValueModel nodeVal = new SPListValueModel(strChannelID, strUMBankName, strBankName);
            lsData.add(nodeVal);
        }
        return lsData;
    }

    public ArrayList<BranchDetailsForBDMValuesModel> parseNodeBranchDetailsForBDM(List<String> lsNode) {
        ArrayList<BranchDetailsForBDMValuesModel> lsData = new ArrayList<>();

        String BANKBRANCHCODE, BANKBRANCHNAME;

        for (String Node : lsNode) {

            BANKBRANCHCODE = parseXmlTag(Node, "BANKBRANCHCODE");
            BANKBRANCHCODE = BANKBRANCHCODE == null ? "" : BANKBRANCHCODE;

            BANKBRANCHNAME = parseXmlTag(Node, "BANKBRANCHNAME");
            BANKBRANCHNAME = BANKBRANCHNAME == null ? "" : BANKBRANCHNAME;


            BranchDetailsForBDMValuesModel nodeVal = new BranchDetailsForBDMValuesModel(BANKBRANCHCODE, BANKBRANCHNAME);
            lsData.add(nodeVal);
        }
        return lsData;
    }

    public ArrayList<RevivalCampaignPoliciesValuesModel> parseNodeRevivalCampaignPoliciesList(List<String> lsNode) {
        ArrayList<RevivalCampaignPoliciesValuesModel> lsData = new ArrayList<>();

        String POLICY_NO, HOLDERNAME, PRODUCTNAME, POLICYSUMASSURED,
                RAG_FLAG, DGH_REQUIREMENT, NET_REVIVAL_START, NET_REVIVAL_LAST;

        for (String Node : lsNode) {

            POLICY_NO = parseXmlTag(Node, "POLICY_NO");
            POLICY_NO = POLICY_NO == null ? "" : POLICY_NO;

            HOLDERNAME = parseXmlTag(Node, "HOLDERNAME");
            HOLDERNAME = HOLDERNAME == null ? "" : HOLDERNAME;

            PRODUCTNAME = parseXmlTag(Node, "PRODUCTNAME");
            PRODUCTNAME = PRODUCTNAME == null ? "" : PRODUCTNAME;

            POLICYSUMASSURED = parseXmlTag(Node, "POLICYSUMASSURED");
            POLICYSUMASSURED = POLICYSUMASSURED == null ? "" : POLICYSUMASSURED;

            RAG_FLAG = parseXmlTag(Node, "RAG_FLAG");
            RAG_FLAG = RAG_FLAG == null ? "" : RAG_FLAG;

            DGH_REQUIREMENT = parseXmlTag(Node, "DGH_REQUIREMENT");
            DGH_REQUIREMENT = DGH_REQUIREMENT == null ? "" : DGH_REQUIREMENT;


            NET_REVIVAL_START = parseXmlTag(Node, "NET_REVIVAL_START");
            NET_REVIVAL_START = NET_REVIVAL_START == null ? "" : NET_REVIVAL_START;

            NET_REVIVAL_LAST = parseXmlTag(Node, "NET_REVIVAL_LAST");
            NET_REVIVAL_LAST = NET_REVIVAL_LAST == null ? "" : NET_REVIVAL_LAST;


            RevivalCampaignPoliciesValuesModel nodeVal = new RevivalCampaignPoliciesValuesModel(POLICY_NO, HOLDERNAME, PRODUCTNAME, POLICYSUMASSURED, RAG_FLAG, DGH_REQUIREMENT, NET_REVIVAL_START, NET_REVIVAL_LAST);
            lsData.add(nodeVal);
        }
        return lsData;
    }

    public List<ClaimRequirementInfoValuesModel> parseNodeClaimRequirementInfo(List<String> lsNode) {

        List<ClaimRequirementInfoValuesModel> lsData = new ArrayList<>();

        String CLAIM_TYPE, AMOUNT, POLICY_NO, LA;
        for (String Node : lsNode) {

            CLAIM_TYPE = parseXmlTag(Node, "CLAIM_TYPE");
            AMOUNT = parseXmlTag(Node, "AMOUNT");
            POLICY_NO = parseXmlTag(Node, "POLICY_NO");
            LA = parseXmlTag(Node, "LA");

            ClaimRequirementInfoValuesModel nodeVal = new ClaimRequirementInfoValuesModel(CLAIM_TYPE, AMOUNT, POLICY_NO, LA);
            lsData.add(nodeVal);

        }

        return lsData;
    }

    public ArrayList<PolicyDispathStatusValuesModel> parseNodePolicyDispatchStatus(List<String> lsNode) {
        ArrayList<PolicyDispathStatusValuesModel> lsData = new ArrayList<>();

        String REPORT_TYPE, DOC_TYPE, PROPOSALNO, POLICYNO, DISPATCH_TO, DISPATCH_THROUGH, AWBNO,
                DESP_DATE, CHEQUENO, CHEQUEDATE, CHEQUEAMOUNT, STATUS;
        Date date = null;

        for (String Node : lsNode) {

            REPORT_TYPE = parseXmlTag(Node, "REPORT_TYPE");
            REPORT_TYPE = REPORT_TYPE == null ? "" : REPORT_TYPE;

            DOC_TYPE = parseXmlTag(Node, "DOC_TYPE");
            DOC_TYPE = DOC_TYPE == null ? "" : DOC_TYPE;

            PROPOSALNO = parseXmlTag(Node, "PROPOSALNO");
            PROPOSALNO = PROPOSALNO == null ? "" : PROPOSALNO;

            POLICYNO = parseXmlTag(Node, "POLICYNO");
            POLICYNO = POLICYNO == null ? "" : POLICYNO;

            DISPATCH_TO = parseXmlTag(Node, "DISPATCH_TO");
            DISPATCH_TO = DISPATCH_TO == null ? "" : DISPATCH_TO;


            DISPATCH_THROUGH = parseXmlTag(Node, "DISPATCH_THROUGH");
            DISPATCH_THROUGH = DISPATCH_THROUGH == null ? "" : DISPATCH_THROUGH;

            AWBNO = parseXmlTag(Node, "AWBNO");
            AWBNO = AWBNO == null ? "" : AWBNO;

            DESP_DATE = parseXmlTag(Node, "DESP_DATE");
            DESP_DATE = DESP_DATE == null ? "" : DESP_DATE;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            try {
                date = dateFormat.parse(DESP_DATE);
            } catch (Exception e) {
                System.out.println(e.getMessage());

            }

            CHEQUENO = parseXmlTag(Node, "CHEQUENO");
            CHEQUENO = CHEQUENO == null ? "" : CHEQUENO;

            CHEQUEDATE = parseXmlTag(Node, "CHEQUEDATE");
            CHEQUEDATE = CHEQUEDATE == null ? "" : CHEQUEDATE;

            CHEQUEAMOUNT = parseXmlTag(Node, "CHEQUEAMOUNT");
            CHEQUEAMOUNT = CHEQUEAMOUNT == null ? "" : CHEQUEAMOUNT;

            STATUS = parseXmlTag(Node, "STATUS");
            STATUS = STATUS == null ? "" : STATUS;


            PolicyDispathStatusValuesModel nodeVal = new PolicyDispathStatusValuesModel(REPORT_TYPE, DOC_TYPE,
                    PROPOSALNO, POLICYNO, DISPATCH_TO, DISPATCH_THROUGH, AWBNO, DESP_DATE, CHEQUENO,
                    CHEQUEDATE, CHEQUEAMOUNT, STATUS, date);
            lsData.add(nodeVal);


        }

        Collections.sort(lsData, new Comparator<PolicyDispathStatusValuesModel>() {
            @Override
            public int compare(PolicyDispathStatusValuesModel o1, PolicyDispathStatusValuesModel o2) {
                if (o1.getDate().after(o2.getDate())) {
                    return -1;
                }
                return 1;
            }
        });
        return lsData;
    }

    public List<Pojo_POSP_RA_Rejection> parseNodeElementPOSP_RA_Rejection(List<String> lsNode, String enrollment_type) {

        List<Pojo_POSP_RA_Rejection> lsData = new ArrayList<>();

        for (String Node : lsNode) {

            String str_enrollment_type = parseXmlTag(Node, "ROLE");
            if (str_enrollment_type.equals("IA")) {
                str_enrollment_type = new CommonMethods().str_ia_upgrade_customer_type;
            }

            if (str_enrollment_type.equals(enrollment_type)) {

                String pan = parseXmlTag(Node, "PAN");
                String ia_code = parseXmlTag(Node, "IACODE");
                String doc_status = parseXmlTag(Node, "DOCUMENT_STATUS");
                String req_status = parseXmlTag(Node, "REQUIREMENT_STATUS");
                String req_raised = parseXmlTag(Node, "REQUIREMENT_RAISED");
                String doc_name = parseXmlTag(Node, "DOCUMENT_NAME");
                String remarks = parseXmlTag(Node, "REMARKS");
                String doc_optional_value = parseXmlTag(Node, "DOCUMENT_OPTION_VALUE");

                Pojo_POSP_RA_Rejection nodeVal = new Pojo_POSP_RA_Rejection(pan, ia_code, doc_status,
                        req_status, req_raised, doc_name, remarks, doc_optional_value, str_enrollment_type);

                lsData.add(nodeVal);
            }
        }

        return lsData;

    }

    public List<PojoPOSPRAStatus> parseNodePOSPRAStatusDetails(List<String> lsNode) {

        List<PojoPOSPRAStatus> lsData = new ArrayList<>();

        for (String Node : lsNode) {

            String pan_no = parseXmlTag(Node, "PAN_NO");

            String ia_code = parseXmlTag(Node, "IACODE");

            String pan_status = parseXmlTag(Node, "STATUS");

            lsData.add(new PojoPOSPRAStatus(pan_no, ia_code, pan_status));
        }
        return lsData;
    }



    /*[<CIFPolicyList>
        <Table>
        <employee_id>SBIL/13274116</employee_id>
        <MEMBER_NAME> ASHOK KUMAR
             SRIVASTAV 2 </MEMBER_NAME>
        <CLAIM_SANCTION_DATE>2012-07-11T00:00:00+05:30</CLAIM_SANCTION_DATE>
             <CLAIM_AMOUNT>250205</CLAIM_AMOUNT>
               </Table>*/

    public List<PojoAOBAgentDetails> parseNodeAOBAgentDetails(List<String> lsNode, String callFrom) {

        List<PojoAOBAgentDetails> lsData = new ArrayList<>();

        for (String Node : lsNode) {

            String ia_code = parseXmlTag(Node, "IACODE");

            String ia_name = parseXmlTag(Node, "IANAME");

            String um_code_code = parseXmlTag(Node, "UMCODE_CODE");

            String um_code = parseXmlTag(Node, "UMCODE");

            String um_name = parseXmlTag(Node, "UMNAME");

            String status = parseXmlTag(Node, "STATUS");

            String strVar1 = "", strVar2 = "", strVar3 = "", strVar4 = "";

            if (callFrom.equals("STATUS_DOC")) {
                strVar1 = parseXmlTag(Node, "DOCUMENT_STATUS") == null ? "" : parseXmlTag(Node, "DOCUMENT_STATUS");
                strVar2 = parseXmlTag(Node, "EFFECTIVE_START_DATE") == null ? "" : parseXmlTag(Node, "EFFECTIVE_START_DATE");
                strVar3 = parseXmlTag(Node, "LAST_UPDATE_DATE") == null ? "" : parseXmlTag(Node, "LAST_UPDATE_DATE");
                strVar4 = parseXmlTag(Node, "USER_NAME") == null ? "" : parseXmlTag(Node, "USER_NAME");
            } else if (callFrom.equals("REQ_DOC")) {
                strVar1 = parseXmlTag(Node, "REMARKS") == null ? "" : parseXmlTag(Node, "REMARKS");
                strVar2 = parseXmlTag(Node, "DOCUMENT_NAME") == null ? "" : parseXmlTag(Node, "DOCUMENT_NAME");
            }


            lsData.add(new PojoAOBAgentDetails(ia_code, ia_name, um_code_code, um_code, um_name, status,
                    strVar1, strVar2, strVar3, strVar4));
        }

        return lsData;

    }

    public ArrayList<PIWCAudioCallingValuesModel> parseNodePIWCAudioCalling(List<String> lsNode) {
        ArrayList<PIWCAudioCallingValuesModel> lsData = new ArrayList<>();

        String PL_PROP_NUM, PR_FULL_NM, PL_CALL_DT, REMARKS, PROPOSER_MOBILE, VD_VERIF, VD_CALL_FLAG;
        for (String Node : lsNode) {

            PL_PROP_NUM = parseXmlTag(Node, "PL_PROP_NUM");
            PL_PROP_NUM = PL_PROP_NUM == null ? "" : PL_PROP_NUM;

            PR_FULL_NM = parseXmlTag(Node, "PR_FULL_NM");
            PR_FULL_NM = PR_FULL_NM == null ? "" : PR_FULL_NM;

            PL_CALL_DT = parseXmlTag(Node, "PL_CALL_DT");
            PL_CALL_DT = PL_CALL_DT == null ? "" : PL_CALL_DT;

            REMARKS = parseXmlTag(Node, "REMARKS");
            REMARKS = REMARKS == null ? "" : REMARKS;

            PROPOSER_MOBILE = parseXmlTag(Node, "PROPOSER_MOBILE");
            PROPOSER_MOBILE = PROPOSER_MOBILE == null ? "" : PROPOSER_MOBILE;

            VD_VERIF = parseXmlTag(Node, "VD_VERIF");
            VD_VERIF = VD_VERIF == null ? "" : VD_VERIF;

            VD_CALL_FLAG = parseXmlTag(Node, "VD_CALL_FLAG");
            VD_CALL_FLAG = VD_CALL_FLAG == null ? "" : VD_CALL_FLAG;
            PIWCAudioCallingValuesModel nodeVal = new PIWCAudioCallingValuesModel(PL_PROP_NUM, PR_FULL_NM, PL_CALL_DT, REMARKS, PROPOSER_MOBILE, VD_VERIF, VD_CALL_FLAG);
            lsData.add(nodeVal);
        }
        return lsData;
    }

    public ArrayList<LMBusinessStatusValuesModel> parseNodeLMBusinessStatus(List<String> lsNode) {
        ArrayList<LMBusinessStatusValuesModel> lsData = new ArrayList<>();

        String IACODE, COT_TOT_STATUS, IANAME, UMCODE, UMNAME,
                BRANCHCODE, BRANCHNAME, DIVISION, AREA, REGION,
                DOA, ZONE, RATED_YTD, NOP, NOP_CONNECT_LIFE,
                RENEWAL_COLLECTED, PERSISTENCY, CLUBSTATUS,
                WEIGHTED_PREMIUM, EXTRACTION_DATE, NBPRANKING;

        CommonForAllProd mCommonForAllProd = new CommonForAllProd();

        for (String Node : lsNode) {

            IACODE = parseXmlTag(Node, "IACODE");
            IACODE = IACODE == null ? "" : IACODE;

            COT_TOT_STATUS = parseXmlTag(Node, "COT_TOT_STATUS");
            COT_TOT_STATUS = COT_TOT_STATUS == null ? "" : COT_TOT_STATUS;

            IANAME = parseXmlTag(Node, "IANAME");
            IANAME = IANAME == null ? "" : IANAME;

            UMCODE = parseXmlTag(Node, "UMCODE");
            UMCODE = UMCODE == null ? "" : UMCODE;

            UMNAME = parseXmlTag(Node, "UMNAME");
            UMNAME = UMNAME == null ? "" : UMNAME;

            BRANCHCODE = parseXmlTag(Node, "BRANCHCODE");
            BRANCHCODE = BRANCHCODE == null ? "" : BRANCHCODE;

            BRANCHNAME = parseXmlTag(Node, "BRANCHNAME");
            BRANCHNAME = BRANCHNAME == null ? "" : BRANCHNAME;

            DIVISION = parseXmlTag(Node, "DIVISION");
            DIVISION = DIVISION == null ? "" : DIVISION;

            AREA = parseXmlTag(Node, "AREA");
            AREA = AREA == null ? "" : AREA;

            REGION = parseXmlTag(Node, "REGION");
            REGION = REGION == null ? "" : REGION;

            DOA = parseXmlTag(Node, "DOA");
            DOA = DOA == null ? "" : DOA;

            ZONE = parseXmlTag(Node, "ZONE");
            ZONE = ZONE == null ? "" : ZONE;

            RATED_YTD = parseXmlTag(Node, "RATED_YTD");
            RATED_YTD = RATED_YTD == null ? "" : RATED_YTD;
            RATED_YTD = mCommonForAllProd.roundUp_Level2(RATED_YTD);

            NOP = parseXmlTag(Node, "NOP");
            NOP = NOP == null ? "" : NOP;

            NOP_CONNECT_LIFE = parseXmlTag(Node, "NOP_CONNECT_LIFE");
            NOP_CONNECT_LIFE = NOP_CONNECT_LIFE == null ? "" : NOP_CONNECT_LIFE;

            RENEWAL_COLLECTED = parseXmlTag(Node, "RENEWAL_COLLECTED");
            RENEWAL_COLLECTED = RENEWAL_COLLECTED == null ? "" : RENEWAL_COLLECTED;
            RENEWAL_COLLECTED = mCommonForAllProd.roundUp_Level2(RENEWAL_COLLECTED);

            PERSISTENCY = parseXmlTag(Node, "PERSISTENCY");
            PERSISTENCY = PERSISTENCY == null ? "" : PERSISTENCY;
            PERSISTENCY = mCommonForAllProd.roundUp_Level2(PERSISTENCY);

            CLUBSTATUS = parseXmlTag(Node, "CLUBSTATUS");
            CLUBSTATUS = CLUBSTATUS == null ? "" : CLUBSTATUS;

            WEIGHTED_PREMIUM = parseXmlTag(Node, "WEIGHTED_PREMIUM");
            WEIGHTED_PREMIUM = WEIGHTED_PREMIUM == null ? "" : WEIGHTED_PREMIUM;
            WEIGHTED_PREMIUM = mCommonForAllProd.roundUp_Level2(WEIGHTED_PREMIUM);

            EXTRACTION_DATE = parseXmlTag(Node, "EXTRACTION_DATE");
            EXTRACTION_DATE = EXTRACTION_DATE == null ? "" : EXTRACTION_DATE;

            NBPRANKING = parseXmlTag(Node, "NBPRANKING");
            NBPRANKING = NBPRANKING == null ? "" : NBPRANKING;

            LMBusinessStatusValuesModel nodeVal = new LMBusinessStatusValuesModel(IACODE, COT_TOT_STATUS, IANAME, UMCODE, UMNAME, BRANCHCODE, BRANCHNAME, DIVISION, AREA, REGION, DOA, ZONE, RATED_YTD, NOP, NOP_CONNECT_LIFE, RENEWAL_COLLECTED, PERSISTENCY, CLUBSTATUS, WEIGHTED_PREMIUM, EXTRACTION_DATE, NBPRANKING);
            lsData.add(nodeVal);
        }
        return lsData;
    }

    public ArrayList<MHRReportValuesModel> parseNodeMHRReport(List<String> lsNode) {
        ArrayList<MHRReportValuesModel> lsData = new ArrayList<>();
        String PROPOSALNUMBER, NAME, REGION, CHANNEL, PCNAME, SUC, SUMASSURED, IANAME, IACODE;
        String ACTIVATION_DATE, FRAUDULENT_FLAG, CLUB_WITHOUT_PERSISTENCE, CLUB_WITH_PERSISTENCE;
        int experience = 0;
        for (String Node : lsNode) {

            PROPOSALNUMBER = parseXmlTag(Node, "PROPOSALNUMBER");
            PROPOSALNUMBER = PROPOSALNUMBER == null ? "" : PROPOSALNUMBER;

            NAME = parseXmlTag(Node, "NAME");
            NAME = NAME == null ? "" : NAME;

            REGION = parseXmlTag(Node, "REGION");
            REGION = REGION == null ? "" : REGION;

            CHANNEL = parseXmlTag(Node, "CHANNEL");
            CHANNEL = CHANNEL == null ? "" : CHANNEL;

            PCNAME = parseXmlTag(Node, "PCNAME");
            PCNAME = PCNAME == null ? "" : PCNAME;

            SUC = parseXmlTag(Node, "SUC");
            SUC = SUC == null ? "" : SUC;

            SUMASSURED = parseXmlTag(Node, "SUMASSURED");
            SUMASSURED = SUMASSURED == null ? "" : SUMASSURED;

            IANAME = parseXmlTag(Node, "IANAME");
            IANAME = IANAME == null ? "" : IANAME;

            IACODE = parseXmlTag(Node, "IACODE");
            IACODE = IACODE == null ? "" : IACODE;

            ACTIVATION_DATE = parseXmlTag(Node, "ACTIVATION_DATE");
            ACTIVATION_DATE = ACTIVATION_DATE == null ? "" : ACTIVATION_DATE;

            if (!TextUtils.isEmpty(ACTIVATION_DATE)) {
                Date date = null;
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
                try {
                    date = sdf.parse(ACTIVATION_DATE);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (date == null) {
                    experience = 0;
                }


                Calendar dob = Calendar.getInstance();
                Calendar today = Calendar.getInstance();

                dob.setTime(date);

                int year = dob.get(Calendar.YEAR);
                int month = dob.get(Calendar.MONTH);
                int day = dob.get(Calendar.DAY_OF_MONTH);

                dob.set(year, month + 1, day);

                experience = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

                if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
                    experience--;
                }
            }

            FRAUDULENT_FLAG = parseXmlTag(Node, "FRAUDULENT_FLAG");
            FRAUDULENT_FLAG = FRAUDULENT_FLAG == null ? "" : FRAUDULENT_FLAG;

            CLUB_WITHOUT_PERSISTENCE = parseXmlTag(Node, "CLUB_WITHOUT_PERSISTENCE");
            CLUB_WITHOUT_PERSISTENCE = CLUB_WITHOUT_PERSISTENCE == null ? "" : CLUB_WITHOUT_PERSISTENCE;

            CLUB_WITH_PERSISTENCE = parseXmlTag(Node, "CLUB_WITH_PERSISTENCE");
            CLUB_WITH_PERSISTENCE = CLUB_WITH_PERSISTENCE == null ? "" : CLUB_WITH_PERSISTENCE;

            MHRReportValuesModel nodeVal = new MHRReportValuesModel(PROPOSALNUMBER, NAME, REGION, CHANNEL, PCNAME, SUC, SUMASSURED, IANAME, IACODE,
                    ACTIVATION_DATE, FRAUDULENT_FLAG, CLUB_WITHOUT_PERSISTENCE, CLUB_WITH_PERSISTENCE, experience);
            lsData.add(nodeVal);
        }
        return lsData;
    }

    public ArrayList<AgentPoliciesRenewalListMonthwiseGio> parseNodeBranchwiseRenewalListGio(List<String> lsNode) {
        ArrayList<AgentPoliciesRenewalListMonthwiseGio> lsData = new ArrayList<>();

        String POLICYNUMBER, HOLDERID, HOLDERPERSONFIRSTNAME, HOLDERPERSONLASTNAME, POLICYCURRENTSTATUS,
                PREMIUMFUP, PREMIUMGROSSAMOUNT, PAYMENTTYPE, CONTACTMOBILE, PERMANENTADDRESS1, PERMANENTADDRESS2,
                PERMANENTADDRESS3, PERMANENTCITY, PERMANENTSTATE, PERMANENTPOSTCODE, RAG_FLAG, SURRENDER_FLAG,
                RESIDUAL_AMOUNT, POLICYRISKCOMMENCEMENTDATE, CONTACTRESIDENCE, CONTACTOFFICE;

        for (String Node : lsNode) {

            POLICYNUMBER = parseXmlTag(Node, "POLICYNUMBER");
            POLICYNUMBER = POLICYNUMBER == null ? "" : POLICYNUMBER;

            HOLDERID = parseXmlTag(Node, "HOLDERID");
            HOLDERID = HOLDERID == null ? "" : HOLDERID;

            HOLDERPERSONFIRSTNAME = parseXmlTag(Node, "HOLDERPERSONFIRSTNAME");
            HOLDERPERSONFIRSTNAME = HOLDERPERSONFIRSTNAME == null ? "" : HOLDERPERSONFIRSTNAME;

            HOLDERPERSONLASTNAME = parseXmlTag(Node, "HOLDERPERSONLASTNAME");
            HOLDERPERSONLASTNAME = HOLDERPERSONLASTNAME == null ? "" : HOLDERPERSONLASTNAME;

            POLICYCURRENTSTATUS = parseXmlTag(Node, "POLICYCURRENTSTATUS");
            POLICYCURRENTSTATUS = POLICYCURRENTSTATUS == null ? "" : POLICYCURRENTSTATUS;

            PREMIUMFUP = parseXmlTag(Node, "PREMIUMFUP");
            if (PREMIUMFUP == null) {
                PREMIUMFUP = "";
            } else {

                PREMIUMFUP = PREMIUMFUP.split("T")[0];

                Date dt1 = null;
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat df1 = new SimpleDateFormat("dd-MMMM-yyyy");
                try {
                    dt1 = df.parse(PREMIUMFUP);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                PREMIUMFUP = df1.format(dt1);

            }


            PREMIUMGROSSAMOUNT = parseXmlTag(Node, "PREMIUMGROSSAMOUNT");
            PREMIUMGROSSAMOUNT = PREMIUMGROSSAMOUNT == null ? "" : PREMIUMGROSSAMOUNT;

            PAYMENTTYPE = parseXmlTag(Node, "PAYMENTTYPE");
            PAYMENTTYPE = PAYMENTTYPE == null ? "" : PAYMENTTYPE;

            CONTACTMOBILE = parseXmlTag(Node, "CONTACTMOBILE");
            CONTACTMOBILE = CONTACTMOBILE == null ? "" : CONTACTMOBILE;

            String strPayType = "";


            if (PAYMENTTYPE.contentEquals("Money IN")) {
                strPayType = "Paid";
            } else if (PAYMENTTYPE.contentEquals("Money OUT")) {
                strPayType = "Unpaid";
            }

            PERMANENTADDRESS1 = parseXmlTag(Node, "PERMANENTADDRESS1");
            PERMANENTADDRESS1 = PERMANENTADDRESS1 == null ? "" : PERMANENTADDRESS1;

            PERMANENTADDRESS2 = parseXmlTag(Node, "PERMANENTADDRESS2");
            PERMANENTADDRESS2 = PERMANENTADDRESS2 == null ? "" : PERMANENTADDRESS2;

            PERMANENTADDRESS3 = parseXmlTag(Node, "PERMANENTADDRESS3");
            PERMANENTADDRESS3 = PERMANENTADDRESS3 == null ? "" : PERMANENTADDRESS3;

            PERMANENTCITY = parseXmlTag(Node, "PERMANENTCITY");
            PERMANENTCITY = PERMANENTCITY == null ? "" : PERMANENTCITY;

            PERMANENTSTATE = parseXmlTag(Node, "PERMANENTSTATE");
            PERMANENTSTATE = PERMANENTSTATE == null ? "" : PERMANENTSTATE;

            PERMANENTPOSTCODE = parseXmlTag(Node, "PERMANENTPOSTCODE");
            PERMANENTPOSTCODE = PERMANENTPOSTCODE == null ? "" : PERMANENTPOSTCODE;

            RAG_FLAG = parseXmlTag(Node, "RAG_FLAG");
            RAG_FLAG = RAG_FLAG == null ? "" : RAG_FLAG;

            SURRENDER_FLAG = parseXmlTag(Node, "SURRENDER_FLAG");
            SURRENDER_FLAG = (SURRENDER_FLAG == null || SURRENDER_FLAG.equalsIgnoreCase("")) ? "NA" : SURRENDER_FLAG;

            RESIDUAL_AMOUNT = parseXmlTag(Node, "RESIDUAL_AMOUNT");
            RESIDUAL_AMOUNT = RESIDUAL_AMOUNT == null ? "" : RESIDUAL_AMOUNT;


            POLICYRISKCOMMENCEMENTDATE = parseXmlTag(Node, "POLICYRISKCOMMENCEMENTDATE");
            POLICYRISKCOMMENCEMENTDATE = POLICYRISKCOMMENCEMENTDATE == null ? "" : POLICYRISKCOMMENCEMENTDATE;
            POLICYRISKCOMMENCEMENTDATE = POLICYRISKCOMMENCEMENTDATE.split("T")[0];

            Date dt1 = null;
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat df1 = new SimpleDateFormat("dd-MMMM-yyyy");
            try {
                dt1 = df.parse(POLICYRISKCOMMENCEMENTDATE);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            POLICYRISKCOMMENCEMENTDATE = df1.format(dt1);


            CONTACTRESIDENCE = parseXmlTag(Node, "CONTACTRESIDENCE");
            CONTACTRESIDENCE = CONTACTRESIDENCE == null ? "" : CONTACTRESIDENCE;

            CONTACTOFFICE = parseXmlTag(Node, "CONTACTOFFICE");
            CONTACTOFFICE = CONTACTOFFICE == null ? "" : CONTACTOFFICE;


            String POLICYTYPE = parseXmlTag(Node, "POLICYTYPE");
            POLICYTYPE = POLICYTYPE == null ? "" : POLICYTYPE;


            String POLICYPAYMENTMECHANISM = parseXmlTag(Node, "POLICYPAYMENTMECHANISM");
            POLICYPAYMENTMECHANISM = POLICYPAYMENTMECHANISM == null ? "" : POLICYPAYMENTMECHANISM;

            if (PAYMENTTYPE.contentEquals("Money OUT")) {
                AgentPoliciesRenewalListMonthwiseGio nodeVal = new AgentPoliciesRenewalListMonthwiseGio(POLICYNUMBER, HOLDERID, HOLDERPERSONFIRSTNAME, HOLDERPERSONLASTNAME,
                        POLICYCURRENTSTATUS, PREMIUMFUP, PREMIUMGROSSAMOUNT, PAYMENTTYPE,
                        CONTACTMOBILE, PERMANENTADDRESS1, PERMANENTADDRESS2, PERMANENTADDRESS3
                        , PERMANENTCITY, PERMANENTSTATE, PERMANENTPOSTCODE, 0.0, 0.0, 0.0,
                        RAG_FLAG, SURRENDER_FLAG, RESIDUAL_AMOUNT, POLICYRISKCOMMENCEMENTDATE, CONTACTRESIDENCE,
                        CONTACTOFFICE, POLICYTYPE, POLICYPAYMENTMECHANISM);
                lsData.add(nodeVal);
            }


        }
        return lsData;
    }

    public ArrayList<BranchAddress> parseNodeBranchList(List<String> lsNode) {
        ArrayList<BranchAddress> lsData = new ArrayList<>();

        String BR_NAME, BR_ADD1, BR_ADD2, BR_ADD3, BR_PIN_CD,
                TEL_NO;

        for (String Node : lsNode) {

            BR_NAME = parseXmlTag(Node, "BR_NAME");
            BR_NAME = BR_NAME == null ? "" : BR_NAME;

            BR_ADD1 = parseXmlTag(Node, "BR_ADD1");
            BR_ADD1 = BR_ADD1 == null ? "" : BR_ADD1;

            BR_ADD2 = parseXmlTag(Node, "BR_ADD2");
            BR_ADD2 = BR_ADD2 == null ? "" : BR_ADD2;

            BR_ADD3 = parseXmlTag(Node, "BR_ADD3");
            BR_ADD3 = BR_ADD3 == null ? "" : BR_ADD3;

            BR_PIN_CD = parseXmlTag(Node, "BR_PIN_CD");
            BR_PIN_CD = BR_PIN_CD == null ? "" : BR_PIN_CD;


            TEL_NO = parseXmlTag(Node, "TEL_NO");
            TEL_NO = TEL_NO == null ? "" : TEL_NO;


            BranchAddress nodeVal = new BranchAddress(BR_NAME, BR_ADD1, BR_ADD2, BR_ADD3,
                    BR_PIN_CD, TEL_NO, 0.0, 0.0, 0.0);
            lsData.add(nodeVal);


        }
        return lsData;
    }

    public ArrayList<RenewalRemark> parseRenewalRemark(List<String> lsNode) {
        ArrayList<RenewalRemark> lsData = new ArrayList<>();

        String POLICYNO, STATUS, SUBSTATUS, REMARKS, CREATED_DATE;

        for (String Node : lsNode) {

            POLICYNO = parseXmlTag(Node, "POLICYNO");
            POLICYNO = POLICYNO == null ? "" : POLICYNO;

            STATUS = parseXmlTag(Node, "STATUS");
            STATUS = STATUS == null ? "" : STATUS;

            SUBSTATUS = parseXmlTag(Node, "SUBSTATUS");
            SUBSTATUS = SUBSTATUS == null ? "" : SUBSTATUS;

            REMARKS = parseXmlTag(Node, "REMARKS");
            REMARKS = REMARKS == null ? "" : REMARKS;

            CREATED_DATE = parseXmlTag(Node, "CREATED_DATE");
            CREATED_DATE = CREATED_DATE == null ? "" : CREATED_DATE;

            RenewalRemark nodeVal = new RenewalRemark(POLICYNO, STATUS, SUBSTATUS, REMARKS, CREATED_DATE);
            lsData.add(nodeVal);

        }
        return lsData;
    }

    public ArrayList<RenewalListGioSummary> parseNodeRenewalListGioSummary(List<String> lsNode) {
        ArrayList<RenewalListGioSummary> lsData = new ArrayList<>();

        String CITY, TOTAL_AMOUNT, TOTAL_COUNT;

        for (String Node : lsNode) {

            CITY = parseXmlTag(Node, "CITY");
            CITY = CITY == null ? "" : CITY;

            TOTAL_AMOUNT = parseXmlTag(Node, "TOTAL_AMOUNT");
            TOTAL_AMOUNT = TOTAL_AMOUNT == null ? "" : TOTAL_AMOUNT;

            TOTAL_COUNT = parseXmlTag(Node, "TOTAL_COUNT");
            TOTAL_COUNT = TOTAL_COUNT == null ? "" : TOTAL_COUNT;

            RenewalListGioSummary nodeVal = new RenewalListGioSummary(CITY, TOTAL_AMOUNT, TOTAL_COUNT);
            lsData.add(nodeVal);


        }
        return lsData;
    }

    public ArrayList<DigitalMHRValuesModel> parseNodeDigitalMHR(List<String> lsNode) {
        ArrayList<DigitalMHRValuesModel> lsData = new ArrayList<>();

        String PROPOSALNO, BITLYLINK;

        for (String Node : lsNode) {

            PROPOSALNO = parseXmlTag(Node, "PROPOSALNO");
            PROPOSALNO = PROPOSALNO == null ? "" : PROPOSALNO;

            BITLYLINK = parseXmlTag(Node, "BITLYLINK");
            BITLYLINK = BITLYLINK == null ? "" : BITLYLINK;
            DigitalMHRValuesModel nodeVal = new DigitalMHRValuesModel(PROPOSALNO, BITLYLINK);
            lsData.add(nodeVal);


        }
        return lsData;
    }


    public ArrayList<SendSMSAlternateProcessValuesModel> parseNodeSendSMSAlternateProcess(List<String> lsNode) {
        ArrayList<SendSMSAlternateProcessValuesModel> lsData = new ArrayList<>();

        String NBM_PROPOSAL_NO, CUST_NAME, CUST_MOBILE_NUMBER, IS_KYC;

        for (String Node : lsNode) {

            NBM_PROPOSAL_NO = parseXmlTag(Node, "NBM_PROPOSAL_NO");
            NBM_PROPOSAL_NO = NBM_PROPOSAL_NO == null ? "" : NBM_PROPOSAL_NO;

            CUST_NAME = parseXmlTag(Node, "CUST_NAME");
            CUST_NAME = CUST_NAME == null ? "" : CUST_NAME;

            CUST_MOBILE_NUMBER = parseXmlTag(Node, "CUST_MOBILE_NUMBER");
            CUST_MOBILE_NUMBER = CUST_MOBILE_NUMBER == null ? "" : CUST_MOBILE_NUMBER;

            IS_KYC = parseXmlTag(Node, "IS_KYC");
            IS_KYC = IS_KYC == null ? "" : IS_KYC;
            SendSMSAlternateProcessValuesModel nodeVal = new SendSMSAlternateProcessValuesModel(NBM_PROPOSAL_NO, CUST_NAME, CUST_MOBILE_NUMBER, IS_KYC);
            lsData.add(nodeVal);

        }
        return lsData;
    }

    public ArrayList<SendSMSLinkValuesModel> parseNodeSendSMSLink(List<String> lsNode) {
        ArrayList<SendSMSLinkValuesModel> lsData = new ArrayList<>();

        String NAME, MOBILE, EMAILID;

        for (String Node : lsNode) {

            NAME = parseXmlTag(Node, "NAME");
            NAME = NAME == null ? "" : NAME;

            MOBILE = parseXmlTag(Node, "MOBILE");
            MOBILE = MOBILE == null ? "" : MOBILE;

            EMAILID = parseXmlTag(Node, "EMAILID");
            EMAILID = EMAILID == null ? "" : EMAILID;

            SendSMSLinkValuesModel nodeVal = new SendSMSLinkValuesModel(NAME, MOBILE, EMAILID);
            lsData.add(nodeVal);
        }
        return lsData;
    }

    public class XMLHolder {

        private String no;
        private String holderid;
        private String fname;
        private String lname;
        private String productname;
        private String policytype;
        private String policypaymentterm;
        private String policysumassured;
        private String premiumfrequency;
        private String grossamt;
        private String status;
        private String premiumup;

        XMLHolder(String no, String holderid, String fname,
                  String lname, String productname, String policytype,
                  String policypaymentterm, String sumassured, String freq,
                  String gamt, String status, String premiumup) {
            super();
            this.no = no;
            this.holderid = holderid;
            this.fname = fname;
            this.lname = lname;
            this.productname = productname;
            this.policytype = policytype;
            this.policypaymentterm = policypaymentterm;
            this.policysumassured = sumassured;
            this.premiumfrequency = freq;
            this.grossamt = gamt;
            this.status = status;
            this.premiumup = premiumup;
        }

        public String getNo() {
            return no;
        }

        public void setNo(String no) {

            this.no = no;

        }

        public String getHolderId() {

            return holderid;

        }

        public void setHolderId(String holderid) {

            this.holderid = holderid;

        }

        public String getFName() {

            return fname;

        }

        public void setFName(String fname) {

            this.fname = fname;

        }

        public String getLName() {

            return lname;

        }

        public void setLName(String lname) {

            this.lname = lname;

        }

        public String getProductName() {

            return productname;

        }

        public void setProductName(String productname) {

            this.productname = productname;

        }

        public String getPolicyType() {

            return policytype;

        }

        public void setPolicyType(String PolicyType) {

            this.policytype = PolicyType;

        }

        public String getPolicyPayTerm() {

            return policypaymentterm;

        }

        public void setPolicyPayTerm(String PolicyPayTerm) {

            this.policypaymentterm = PolicyPayTerm;

        }

        public String getPolicySumAssured() {

            return policysumassured;

        }

        public void setPolicySumAssured(String policysumassured) {

            this.policysumassured = policysumassured;

        }

        public String getPremiumFreq() {

            return premiumfrequency;

        }

        public void setPremiumFreq(String PremiumFreq) {

            this.premiumfrequency = PremiumFreq;

        }

        public String getGrossAmt() {

            return grossamt;

        }

        public void setGrossAmt(String GrossAmt) {

            this.grossamt = GrossAmt;

        }

        public String getStatus() {

            return status;

        }

        public void setStatus(String status) {

            this.status = status;

        }

        public String getPremiumUp() {

            return premiumup;

        }

        public void setPremiumUp(String premiumup) {

            this.premiumup = premiumup;

        }

    }

    public class XMLHolderMaturity {

        private String policyNumber;

        private String customerCode;

        private String lifeAssuredName;

        private String status;

        private String benefitTerm;

        private String policyRiskCommencementDate;

        private String maturityDate;

        private String paymentAmount;

        private String firstName;

        private String DOB;

        private String city;

        private String address;

        private String postalCode;

        private String state;
        private String contactNumber;

        XMLHolderMaturity(String policyNumber, String customerCode,
                          String lifeAssuredName, String status, String benefitTerm,
                          String policyRiskCommencementDate, String maturityDate,
                          String paymentAmount, String firstName, String dOB,
                          String city, String address, String postalCode, String state,
                          String contactNumber) {
            super();
            this.policyNumber = policyNumber;
            this.customerCode = customerCode;
            this.lifeAssuredName = lifeAssuredName;
            this.status = status;
            this.benefitTerm = benefitTerm;
            this.policyRiskCommencementDate = policyRiskCommencementDate;
            this.maturityDate = maturityDate;
            this.paymentAmount = paymentAmount;
            this.firstName = firstName;
            DOB = dOB;
            this.city = city;
            this.address = address;
            this.postalCode = postalCode;
            this.state = state;
            this.contactNumber = contactNumber;
        }

        public String getPolicyNumber() {
            return policyNumber;
        }

        public String getCustomerCode() {
            return customerCode;
        }

        public String getLifeAssuredName() {
            return lifeAssuredName;
        }

        public String getStatus() {
            return status;
        }

        public String getBenefitTerm() {
            return benefitTerm;
        }

        public String getPolicyRiskCommencementDate() {
            return policyRiskCommencementDate;
        }

        public String getMaturityDate() {
            return maturityDate;
        }

        public String getPaymentAmount() {
            return paymentAmount;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getDOB() {
            return DOB;
        }

        public String getCity() {
            return city;
        }

        public String getAddress() {
            return address;
        }

        public String getPostalCode() {
            return postalCode;
        }

        public String getState() {
            return state;
        }

        public String getContactNumber() {
            return contactNumber;
        }

    }

    public class XMLHolderSurrender {

        private String no;

        private String holderid;

        private String fname;

        private String lname;

        private String status;

        XMLHolderSurrender(String no, String holderid, String fname,
                           String lname, String status) {

            super();

            this.no = no;

            this.holderid = holderid;

            this.fname = fname;

            this.lname = lname;

            this.status = status;
        }

        public String getNo() {

            return no;

        }

        public void setNo(String no) {

            this.no = no;

        }

        public String getHolderId() {

            return holderid;

        }

        public void setHolderId(String holderid) {

            this.holderid = holderid;

        }

        public String getFName() {

            return fname;

        }

        public void setFName(String fname) {

            this.fname = fname;

        }

        public String getLName() {

            return lname;

        }

        public void setLName(String lname) {

            this.lname = lname;

        }

        public String getStatus() {

            return status;

        }

        public void setStatus(String status) {

            this.status = status;

        }

    }

    public class XMLHolderRevival {

        private String no;

        private String holderid;

        private String fname;

        private String lname;

        // private String status;

        private String premiumup;

        XMLHolderRevival(String no, String holderid, String fname,
                         String lname, String premiumup) {

            super();

            this.no = no;

            this.holderid = holderid;

            this.fname = fname;

            this.lname = lname;

            // this.status = status;

            this.premiumup = premiumup;
        }

        public String getNo() {

            return no;

        }

        public void setNo(String no) {

            this.no = no;

        }

        public String getHolderId() {

            return holderid;

        }

        public void setHolderId(String holderid) {

            this.holderid = holderid;

        }

        public String getFName() {

            return fname;

        }

        public void setFName(String fname) {

            this.fname = fname;

        }

        public String getLName() {

            return lname;

        }

        public void setLName(String lname) {

            this.lname = lname;

        }

        /*
         * public String getStatus() {
         *
         * return status;
         *
         * }
         *
         * public void setStatus(String status) {
         *
         * this.status = status;
         *
         * }
         */

        public String getPremiumUp() {

            return premiumup;

        }

        public void setPremiumUp(String premiumup) {

            this.premiumup = premiumup;

        }

    }

    public class XMLHolderRenewal {

        private String no;

        private String holderid;

        private String fname;

        private String lname;

        private String status;

        private String premiumup;

        private String premiumamt;

        private String paytype;

        private String CONTACTMOBILE;

        private String POLICYPAYMENTMECHANISM;
        private String EMAILID;
        private String POLICYRISKCOMMENCEMENTDATE, CONTACTRESIDENCE,
                CONTACTOFFICE, POLICYTYPE;

        XMLHolderRenewal(String no, String holderid, String fname,
                         String lname, String status, String premiumup,
                         String premiumamt, String paytype, String CONTACTMOBILE,
                         String POLICYPAYMENTMECHANISM, String EMAILID, String POLICYRISKCOMMENCEMENTDATE,
                         String CONTACTRESIDENCE, String CONTACTOFFICE, String POLICYTYPE) {
            super();

            this.no = no;

            this.holderid = holderid;

            this.fname = fname;

            this.lname = lname;

            this.status = status;

            this.premiumup = premiumup;

            this.premiumamt = premiumamt;

            this.paytype = paytype;

            this.CONTACTMOBILE = CONTACTMOBILE;
            this.POLICYPAYMENTMECHANISM = POLICYPAYMENTMECHANISM;
            this.EMAILID = EMAILID;

            this.POLICYRISKCOMMENCEMENTDATE = POLICYRISKCOMMENCEMENTDATE;
            this.CONTACTRESIDENCE = CONTACTRESIDENCE;
            this.CONTACTOFFICE = CONTACTOFFICE;
            this.POLICYTYPE = POLICYTYPE;
        }

        public String getCONTACTMOBILE() {
            return CONTACTMOBILE;
        }

        public String getNo() {

            return no;

        }

        public void setNo(String no) {

            this.no = no;

        }

        public String getHolderId() {

            return holderid;

        }

        public void setHolderId(String holderid) {

            this.holderid = holderid;

        }

        public String getFName() {

            return fname;

        }

        public void setFName(String fname) {

            this.fname = fname;

        }

        public String getLName() {

            return lname;

        }

        public void setLName(String lname) {

            this.lname = lname;

        }

        public String getStatus() {

            return status;

        }

        public void setStatus(String status) {

            this.status = status;

        }

        public String getPremiumUp() {

            return premiumup;

        }

        public void setPremiumUp(String premiumup) {

            this.premiumup = premiumup;

        }

        public String getPremiumAmt() {

            return premiumamt;

        }

        public void setPremiumAmt(String premiumamt) {

            this.premiumamt = premiumamt;

        }

        public String getPayType() {

            return paytype;

        }

        public void setPayType(String paytype) {

            this.paytype = paytype;
        }

        public String getPOLICYPAYMENTMECHANISM() {
            return POLICYPAYMENTMECHANISM;
        }

        public String getEMAILID() {
            return EMAILID;
        }


        public String getFname() {
            return fname;
        }

        public String getLname() {
            return lname;
        }

        public String getPremiumup() {
            return premiumup;
        }

        public String getPremiumamt() {
            return premiumamt;
        }

        public String getPaytype() {
            return paytype;
        }

        public String getPOLICYRISKCOMMENCEMENTDATE() {
            return POLICYRISKCOMMENCEMENTDATE;
        }

        public String getCONTACTRESIDENCE() {
            return CONTACTRESIDENCE;
        }

        public String getCONTACTOFFICE() {
            return CONTACTOFFICE;
        }

        public String getPOLICYTYPE() {
            return POLICYTYPE;
        }
    }

    public class XMLHolderBdm {

        private String banktype;

        private String branchname;

        private String channelcode;

        private String channelsigle;

        private String fname;

        private String lname;

        private String banktypeone;

        private String branchnameone;
        private String MOBILE;

        XMLHolderBdm(String banktype, String branchname,
                     String channelcode, String channelsigle, String fname,
                     String lname, String banktypeone, String branchnameone, String MOBILE) {

            super();

            this.banktype = banktype;

            this.branchname = branchname;

            this.channelcode = channelcode;

            this.channelsigle = channelsigle;

            this.fname = fname;

            this.lname = lname;

            this.banktypeone = banktypeone;

            this.branchnameone = branchnameone;
            this.MOBILE = MOBILE;
        }

        public String getBankType() {

            return banktype;

        }

        public void setBankType(String btype) {

            this.banktype = btype;

        }

        public String getBranchName() {

            return branchname;

        }

        public void setBranchName(String branchname) {

            this.branchname = branchname;

        }

        public String getChannelCode() {

            return channelcode;

        }

        public void setChannelCode(String code) {

            this.channelcode = code;

        }

        public String getChannelSigle() {

            return channelsigle;

        }

        public void setChannelSigle(String sigle) {

            this.channelsigle = sigle;

        }

        public String getFname() {

            return fname;

        }

        public void setFname(String fname) {

            this.fname = fname;

        }

        public String getLname() {

            return lname;

        }

        public void setLname(String lname) {

            this.lname = lname;

        }

        public String getBankTypeOne() {

            return banktypeone;

        }

        public void setBankTypeOne(String btypeone) {

            this.banktypeone = btypeone;

        }

        public String getBranchNameOne() {

            return branchnameone;

        }

        public void setBranchNameOne(String branchnameone) {

            this.branchnameone = branchnameone;

        }

        public String getMOBILE() {
            return MOBILE;
        }
    }

    class XMLHolderBDMCIF {

        private String policytype;

        private String renewalpolicy;

        private String newpolicy;

        private String newbusinesspremium;

        private String renewalpremium;

        XMLHolderBDMCIF(String policytype, String renewalpolicy,
                        String newpolicy, String newbuspremium, String renewalpremium) {

            super();

            this.policytype = policytype;

            this.renewalpolicy = renewalpolicy;

            this.newpolicy = newpolicy;

            this.newbusinesspremium = newbuspremium;

            this.renewalpremium = renewalpremium;
        }

        public String getPolicyType() {

            return policytype;

        }

        public void setPolicyType(String PolicyType) {

            this.policytype = PolicyType;

        }

        public String getRenewalPolicy() {

            return renewalpolicy;

        }

        public void setRenewalPolicy(String RenewalPolicy) {

            this.renewalpolicy = RenewalPolicy;

        }

        public String getNewBusinessPolicy() {

            return newpolicy;

        }

        public void setNewBusinessPolicy(String NewBusinessPolicy) {

            this.newpolicy = NewBusinessPolicy;

        }

        public String getNewBusinessPremium() {

            return newbusinesspremium;

        }

        public void setNewBusinessPremium(String NewBusinessPremium) {

            this.newbusinesspremium = NewBusinessPremium;

        }

        public String getRenewalPremium() {

            return renewalpremium;

        }

        public void setRenewalPremium(String RenewalPremium) {

            this.renewalpremium = RenewalPremium;

        }

    }

    class XMLHolderPolicyDetail {

        private String ridername;

        private String sumassured;

        private String riskpre;

        private String preterm;

        private String benefitterm;

        XMLHolderPolicyDetail(String ridername, String sumassured,
                              String riskpre, String preterm, String benefitterm) {

            super();

            this.ridername = ridername;

            this.sumassured = sumassured;

            this.riskpre = riskpre;

            this.preterm = preterm;

            this.benefitterm = benefitterm;

        }

        public String getRiderName() {

            return ridername;

        }

        public void setRiderName(String ridername) {

            this.ridername = ridername;

        }

        public String getSumAssured() {

            return sumassured;

        }

        public void setSumAssured(String sumassured) {

            this.sumassured = sumassured;

        }

        public String getRiskPre() {

            return riskpre;

        }

        public void setRiskPre(String riskpre) {

            this.riskpre = riskpre;

        }

        public String getPremiumTerm() {

            return preterm;

        }

        public void setPremiumTerm(String preterm) {

            this.preterm = preterm;

        }

        public String getBenefitTerm() {

            return benefitterm;

        }

        public void setBenefitTerm(String benefitterm) {

            this.benefitterm = benefitterm;

        }

    }

    class XMLHolderTotalPreUnpaid {

        private String totalprepaid;

        private String lasttopunpaid;

        XMLHolderTotalPreUnpaid(String totalprepaid, String lasttopunpaid) {

            super();

            this.totalprepaid = totalprepaid;

            this.lasttopunpaid = lasttopunpaid;
        }

        public String getTotalPaid() {

            return totalprepaid;

        }

        public void setTotalPaid(String totalprepaid) {

            this.totalprepaid = totalprepaid;

        }

        public String getLastUnpaid() {

            return lasttopunpaid;

        }

        public void setLastUnpaid(String lasttopunpaid) {

            this.lasttopunpaid = lasttopunpaid;

        }

    }

    public class XMLHolderYoNoPendingKYC{
        private String strProposalNo = "", str_age_proof = "", str_identity_proof ="", str_code = "";

        public XMLHolderYoNoPendingKYC(String strProposalNo, String str_age_proof, String str_identity_proof,
                                       String str_code) {
            this.strProposalNo = strProposalNo;
            this.str_age_proof = str_age_proof;
            this.str_identity_proof = str_identity_proof;
            this.str_code = str_code;
        }

        public String getStrProposalNo() {
            return strProposalNo;
        }

        public void setStrProposalNo(String strProposalNo) {
            this.strProposalNo = strProposalNo;
        }

        public String getStr_age_proof() {
            return str_age_proof;
        }

        public void setStr_age_proof(String str_age_proof) {
            this.str_age_proof = str_age_proof;
        }

        public String getStr_identity_proof() {
            return str_identity_proof;
        }

        public void setStr_identity_proof(String str_identity_proof) {
            this.str_identity_proof = str_identity_proof;
        }

        public String getStr_code() {
            return str_code;
        }

        public void setStr_code(String str_code) {
            this.str_code = str_code;
        }
    }

    public List<XMLHolderYoNoPendingKYC> parseNodeElementYoNoPendingKYC(
            List<String> lsNode) {

        List<XMLHolderYoNoPendingKYC> lsData = new ArrayList<>();

        for (String Node : lsNode) {

            String strProposalNo = parseXmlTag(Node, "NBM_PROPOSAL_NO");
            strProposalNo = strProposalNo == null ? "" : strProposalNo;

            String strAgeProof = parseXmlTag(Node, "NBD_PROP_AGE_PROOF");
            strAgeProof = strAgeProof == null ? "" : strAgeProof;

            String strIdentity_Proof = parseXmlTag(Node, "NBD_PROP_IDENTITY_PROOF");
            strIdentity_Proof = strIdentity_Proof == null ? "" : strIdentity_Proof;

            String strCode = parseXmlTag(Node, "CD_CODE");
            strCode = strCode == null ? "" : strCode;

            XMLHolderYoNoPendingKYC nodeVal = new XMLHolderYoNoPendingKYC(strProposalNo, strAgeProof,
                    strIdentity_Proof, strCode);

            lsData.add(nodeVal);

        }

        return lsData;

    }

    class XMLHolderTotalUnUnpaid {

        private String totalunprepaid;

        private String lasttopunpaid;

        XMLHolderTotalUnUnpaid(String totalunprepaid,
                               String lasttopunpaid) {

            super();

            this.totalunprepaid = totalunprepaid;

            this.lasttopunpaid = lasttopunpaid;
        }

        public String getTotalPaid() {

            return totalunprepaid;

        }

        public void setTotalPaid(String totalunprepaid) {

            this.totalunprepaid = totalunprepaid;

        }

        public String getLastUnpaid() {

            return lasttopunpaid;

        }

        public void setLastUnpaid(String lasttopunpaid) {

            this.lasttopunpaid = lasttopunpaid;

        }

    }

    class XMLHolderTopUp {

        private String lasttopupamt;

        private String lasttopupdate;

        XMLHolderTopUp(String lasttopupamt, String lasttopupdate) {

            super();

            this.lasttopupamt = lasttopupamt;

            this.lasttopupdate = lasttopupdate;
        }

        public String getLastTopUp() {

            return lasttopupamt;

        }

        public void setLastTopUp(String lasttopupamt) {

            this.lasttopupamt = lasttopupamt;

        }

        public String getLastTopUpDate() {

            return lasttopupdate;

        }

        public void setLastTopUpDate(String lasttopupdate) {

            this.lasttopupdate = lasttopupdate;

        }

    }

    class XMLHolderUM {

        private String bankname;

        private String channelcode;

        private String channelsigle;

        private String fname;

        private String lname;

        XMLHolderUM(String bankname, String channelcode,
                    String channelsigle, String fname, String lname) {

            super();

            this.bankname = bankname;

            this.channelcode = channelcode;

            this.channelsigle = channelsigle;

            this.fname = fname;

            this.lname = lname;
        }

        public String getBankName() {

            return bankname;

        }

        public void setBankName(String bname) {

            this.bankname = bname;

        }

        public String getChannelCode() {

            return channelcode;

        }

        public void setChannelCode(String code) {

            this.channelcode = code;

        }

        public String getChannelSigle() {

            return channelsigle;

        }

        public void setChannelSigle(String sigle) {

            this.channelsigle = sigle;

        }

        public String getFname() {

            return fname;

        }

        public void setFname(String fname) {

            this.fname = fname;

        }

        public String getLname() {

            return lname;

        }

        public void setLname(String lname) {

            this.lname = lname;

        }
    }

    public class XMLHolderCategory {

        private String id;

        private String name;

        XMLHolderCategory(String id, String name) {

            super();

            this.id = id;

            this.name = name;

        }

        public String getId() {

            return id;

        }

        public void setId(String id) {

            this.id = id;

        }

        public String getName() {

            return name;

        }

        public void setName(String name) {

            this.name = name;

        }
    }

    public class XMLHolderSubCategory {

        private String id;

        private String name;

        XMLHolderSubCategory(String id, String name) {

            super();

            this.id = id;

            this.name = name;

        }

        public String getId() {

            return id;

        }

        public void setId(String id) {

            this.id = id;

        }

        public String getName() {

            return name;

        }

        public void setName(String name) {

            this.name = name;

        }
    }

    public class XMLHolderBankBranch {

        private String id;

        private String name;

        XMLHolderBankBranch(String id, String name) {

            super();

            this.id = id;

            this.name = name;

        }

        public String getId() {

            return id;

        }

        public void setId(String id) {

            this.id = id;

        }

        public String getName() {

            return name;

        }

        public void setName(String name) {

            this.name = name;

        }
    }

    public class XMLHolderParamList {

        private String id;

        private String name;

        XMLHolderParamList(String id, String name) {

            super();

            this.id = id;

            this.name = name;

        }

        public String getId() {

            return id;

        }

        public void setId(String id) {

            this.id = id;

        }

        public String getName() {

            return name;

        }

        public void setName(String name) {

            this.name = name;

        }
    }

    public class XMLHolderAdvances {

        private String advancesid;

        private String branchcode;

        private String tot_no_of_acc;

        private String tot_out;

        private String no_of_acc_b1l;

        private String tot_out_b1l;

        private String no_of_acc_1lto5l;

        private String tot_out_1lto5l;

        private String no_of_acc_a5l;

        private String tot_out_a5l;

        private String category;

        XMLHolderAdvances(String advancesid, String branchcode,
                          String tot_no_of_acc, String tot_out, String no_of_acc_b1l,
                          String tot_out_b1l, String no_of_acc_1lto5l,
                          String tot_out_1lto5l, String no_of_acc_a5l,
                          String tot_out_a5l, String category) {

            super();

            this.advancesid = advancesid;

            this.branchcode = branchcode;

            this.tot_no_of_acc = tot_no_of_acc;

            this.tot_out = tot_out;

            this.no_of_acc_b1l = no_of_acc_b1l;

            this.tot_out_b1l = tot_out_b1l;

            this.no_of_acc_1lto5l = no_of_acc_1lto5l;

            this.tot_out_1lto5l = tot_out_1lto5l;

            this.no_of_acc_a5l = no_of_acc_a5l;

            this.tot_out_a5l = tot_out_a5l;

            this.category = category;
        }

        /**
         * @return the advancesid
         */
        public String getAdvancesid() {
            return advancesid;
        }

        /**
         * @param advancesid the advancesid to set
         */
        public void setAdvancesid(String advancesid) {
            this.advancesid = advancesid;
        }

        /**
         * @return the branchcode
         */
        public String getBranchcode() {
            return branchcode;
        }

        /**
         * @param branchcode the branchcode to set
         */
        public void setBranchcode(String branchcode) {
            this.branchcode = branchcode;
        }

        /**
         * @return the tot_no_of_acc
         */
        public String getTot_no_of_acc() {
            return tot_no_of_acc;
        }

        /**
         * @param tot_no_of_acc the tot_no_of_acc to set
         */
        public void setTot_no_of_acc(String tot_no_of_acc) {
            this.tot_no_of_acc = tot_no_of_acc;
        }

        /**
         * @return the tot_out
         */
        public String getTot_out() {
            return tot_out;
        }

        /**
         * @param tot_out the tot_out to set
         */
        public void setTot_out(String tot_out) {
            this.tot_out = tot_out;
        }

        /**
         * @return the no_of_acc_b1l
         */
        public String getNo_of_acc_b1l() {
            return no_of_acc_b1l;
        }

        /**
         * @param no_of_acc_b1l the no_of_acc_b1l to set
         */
        public void setNo_of_acc_b1l(String no_of_acc_b1l) {
            this.no_of_acc_b1l = no_of_acc_b1l;
        }

        /**
         * @return the tot_out_b1l
         */
        public String getTot_out_b1l() {
            return tot_out_b1l;
        }

        /**
         * @param tot_out_b1l the tot_out_b1l to set
         */
        public void setTot_out_b1l(String tot_out_b1l) {
            this.tot_out_b1l = tot_out_b1l;
        }

        /**
         * @return the no_of_acc_1lto5l
         */
        public String getNo_of_acc_1lto5l() {
            return no_of_acc_1lto5l;
        }

        /**
         * @param no_of_acc_1lto5l the no_of_acc_1lto5l to set
         */
        public void setNo_of_acc_1lto5l(String no_of_acc_1lto5l) {
            this.no_of_acc_1lto5l = no_of_acc_1lto5l;
        }

        /**
         * @return the tot_out_1lto5l
         */
        public String getTot_out_1lto5l() {
            return tot_out_1lto5l;
        }

        /**
         * @param tot_out_1lto5l the tot_out_1lto5l to set
         */
        public void setTot_out_1lto5l(String tot_out_1lto5l) {
            this.tot_out_1lto5l = tot_out_1lto5l;
        }

        /**
         * @return the no_of_acc_a5l
         */
        public String getNo_of_acc_a5l() {
            return no_of_acc_a5l;
        }

        /**
         * @param no_of_acc_a5l the no_of_acc_a5l to set
         */
        public void setNo_of_acc_a5l(String no_of_acc_a5l) {
            this.no_of_acc_a5l = no_of_acc_a5l;
        }

        /**
         * @return the tot_out_a5l
         */
        public String getTot_out_a5l() {
            return tot_out_a5l;
        }

        /**
         * @param tot_out_a5l the tot_out_a5l to set
         */
        public void setTot_out_a5l(String tot_out_a5l) {
            this.tot_out_a5l = tot_out_a5l;
        }

        /**
         * @return the category
         */
        public String getCategory() {
            return category;
        }

        /**
         * @param category the category to set
         */
        public void setCategory(String category) {
            this.category = category;
        }

    }

    public class XMLHolderDeposit {

        private String depositid;

        private String branchcode;

        private String tot_acc;

        private String tot_amt;

        private String new_acc_b1k;

        private String new_balance_b1k;

        private String new_acc_10kto1l;

        private String new_bal_10kto1l;

        private String new_acc_1lto5l;

        private String new_bal_1lto5l;

        private String new_acc_5landA;

        private String new_bal_5landA;

        private String category;

        XMLHolderDeposit(String depositid, String branchcode,
                         String tot_acc, String tot_amt, String new_acc_b1k,
                         String new_balance_b1k, String new_acc_10kto1l,
                         String new_bal_10kto1l, String new_acc_1lto5l,
                         String new_bal_1lto5l, String new_acc_5landA,
                         String new_bal_5landA, String category) {
            super();
            this.depositid = depositid;
            this.branchcode = branchcode;
            this.tot_acc = tot_acc;
            this.tot_amt = tot_amt;
            this.new_acc_b1k = new_acc_b1k;
            this.new_balance_b1k = new_balance_b1k;
            this.new_acc_10kto1l = new_acc_10kto1l;
            this.new_bal_10kto1l = new_bal_10kto1l;
            this.new_acc_1lto5l = new_acc_1lto5l;
            this.new_bal_1lto5l = new_bal_1lto5l;
            this.new_acc_5landA = new_acc_5landA;
            this.new_bal_5landA = new_bal_5landA;
            this.category = category;
        }

        /**
         * @return the depositid
         */
        public String getDepositid() {
            return depositid;
        }

        /**
         * @param depositid the depositid to set
         */
        public void setDepositid(String depositid) {
            this.depositid = depositid;
        }

        /**
         * @return the branchcode
         */
        public String getBranchcode() {
            return branchcode;
        }

        /**
         * @param branchcode the branchcode to set
         */
        public void setBranchcode(String branchcode) {
            this.branchcode = branchcode;
        }

        /**
         * @return the tot_acc
         */
        public String getTot_acc() {
            return tot_acc;
        }

        /**
         * @param tot_acc the tot_acc to set
         */
        public void setTot_acc(String tot_acc) {
            this.tot_acc = tot_acc;
        }

        /**
         * @return the tot_amt
         */
        public String getTot_amt() {
            return tot_amt;
        }

        /**
         * @param tot_amt the tot_amt to set
         */
        public void setTot_amt(String tot_amt) {
            this.tot_amt = tot_amt;
        }

        /**
         * @return the new_acc_b1k
         */
        public String getNew_acc_b1k() {
            return new_acc_b1k;
        }

        /**
         * @param new_acc_b1k the new_acc_b1k to set
         */
        public void setNew_acc_b1k(String new_acc_b1k) {
            this.new_acc_b1k = new_acc_b1k;
        }

        /**
         * @return the new_balance_b1k
         */
        public String getNew_balance_b1k() {
            return new_balance_b1k;
        }

        /**
         * @param new_balance_b1k the new_balance_b1k to set
         */
        public void setNew_balance_b1k(String new_balance_b1k) {
            this.new_balance_b1k = new_balance_b1k;
        }

        /**
         * @return the new_acc_10kto1l
         */
        public String getNew_acc_10kto1l() {
            return new_acc_10kto1l;
        }

        /**
         * @param new_acc_10kto1l the new_acc_10kto1l to set
         */
        public void setNew_acc_10kto1l(String new_acc_10kto1l) {
            this.new_acc_10kto1l = new_acc_10kto1l;
        }

        /**
         * @return the new_bal_10kto1l
         */
        public String getNew_bal_10kto1l() {
            return new_bal_10kto1l;
        }

        /**
         * @param new_bal_10kto1l the new_bal_10kto1l to set
         */
        public void setNew_bal_10kto1l(String new_bal_10kto1l) {
            this.new_bal_10kto1l = new_bal_10kto1l;
        }

        /**
         * @return the new_acc_1lto5l
         */
        public String getNew_acc_1lto5l() {
            return new_acc_1lto5l;
        }

        /**
         * @param new_acc_1lto5l the new_acc_1lto5l to set
         */
        public void setNew_acc_1lto5l(String new_acc_1lto5l) {
            this.new_acc_1lto5l = new_acc_1lto5l;
        }

        /**
         * @return the new_bal_1lto5l
         */
        public String getNew_bal_1lto5l() {
            return new_bal_1lto5l;
        }

        /**
         * @param new_bal_1lto5l the new_bal_1lto5l to set
         */
        public void setNew_bal_1lto5l(String new_bal_1lto5l) {
            this.new_bal_1lto5l = new_bal_1lto5l;
        }

        /**
         * @return the new_acc_5landA
         */
        public String getNew_acc_5landA() {
            return new_acc_5landA;
        }

        /**
         * @param new_acc_5landA the new_acc_5landA to set
         */
        public void setNew_acc_5landA(String new_acc_5landA) {
            this.new_acc_5landA = new_acc_5landA;
        }

        /**
         * @return the new_bal_5landA
         */
        public String getNew_bal_5landA() {
            return new_bal_5landA;
        }

        /**
         * @param new_bal_5landA the new_bal_5landA to set
         */
        public void setNew_bal_5landA(String new_bal_5landA) {
            this.new_bal_5landA = new_bal_5landA;
        }

        /**
         * @return the category
         */
        public String getCategory() {
            return category;
        }

        /**
         * @param category the category to set
         */
        public void setCategory(String category) {
            this.category = category;
        }

    }

    public class XMLHolderBranch_Profile {

        private String branchcode;

        private String branch_name;

        private String branch_open_date;

        private String branch_net_result;

        private String branch_created_date;

        XMLHolderBranch_Profile(String branchcode, String branch_name,
                                String branch_open_date, String branch_net_result,
                                String branch_created_date) {
            super();
            this.branchcode = branchcode;
            this.branch_name = branch_name;
            this.branch_open_date = branch_open_date;
            this.branch_net_result = branch_net_result;
            this.branch_created_date = branch_created_date;
        }

        /**
         * @return the branchcode
         */
        public String getBranchcode() {
            return branchcode;
        }

        /**
         * @param branchcode the branchcode to set
         */
        public void setBranchcode(String branchcode) {
            this.branchcode = branchcode;
        }

        /**
         * @return the branch_name
         */
        public String getBranch_name() {
            return branch_name;
        }

        /**
         * @param branch_name the branch_name to set
         */
        public void setBranch_name(String branch_name) {
            this.branch_name = branch_name;
        }

        /**
         * @return the branch_open_date
         */
        public String getBranch_open_date() {
            return branch_open_date;
        }

        /**
         * @param branch_open_date the branch_open_date to set
         */
        public void setBranch_open_date(String branch_open_date) {
            this.branch_open_date = branch_open_date;
        }

        /**
         * @return the branch_net_result
         */
        public String getBranch_net_result() {
            return branch_net_result;
        }

        /**
         * @param branch_net_result the branch_net_result to set
         */
        public void setBranch_net_result(String branch_net_result) {
            this.branch_net_result = branch_net_result;
        }

        /**
         * @return the branch_created_date
         */
        public String getBranch_created_date() {
            return branch_created_date;
        }

        /**
         * @param branch_created_date the branch_created_date to set
         */
        public void setBranch_created_date(String branch_created_date) {
            this.branch_created_date = branch_created_date;
        }

    }

    class XMLHolderAGENT {

        private String regionname;

        private String channelcode;

        private String surrender_count;

        private String claim_count;

        private String maturity_count;

        private String lapse_count;

        private String techlapse_count;

        private String claim_amt;

        private String surrender_amt;

        private String nb_count;

        private String nb_amt;

        // private String updatetime;

        private String frequency;

        private String surable_count;

        XMLHolderAGENT(String regionname, String channelcode,
                       String surrender_count, String claim_count,
                       String maturity_count, String lapse_count,
                       String techlapse_count, String claim_amt, String surrender_amt,
                       String nb_count, String nb_amt, String frequency,
                       String surable_count) {
            super();
            this.regionname = regionname;
            this.channelcode = channelcode;
            this.surrender_count = surrender_count;

            this.claim_count = claim_count;

            this.maturity_count = maturity_count;
            this.lapse_count = lapse_count;
            this.techlapse_count = techlapse_count;

            this.claim_amt = claim_amt;

            this.surrender_amt = surrender_amt;
            this.nb_count = nb_count;
            this.nb_amt = nb_amt;
            // this.updatetime = updatetime;
            this.frequency = frequency;
            this.surable_count = surable_count;
        }

        /**
         * @return the regionname
         */
        public String getRegionname() {
            return regionname;
        }

        /**
         * @param regionname the regionname to set
         */
        public void setRegionname(String regionname) {
            this.regionname = regionname;
        }

        /**
         * @return the channelcode
         */
        public String getChannelcode() {
            return channelcode;
        }

        /**
         * @param channelcode the channelcode to set
         */
        public void setChannelcode(String channelcode) {
            this.channelcode = channelcode;
        }

        /**
         * @return the surrender_count
         */
        public String getSurrender_count() {
            return surrender_count;
        }

        /**
         * @param surrender_count the surrender_count to set
         */
        public void setSurrender_count(String surrender_count) {
            this.surrender_count = surrender_count;
        }

        /**
         * @return the claim_count
         */
        public String getClaim_count() {
            return claim_count;
        }

        /**
         * @param claim_count the claim_count to set
         */
        public void setClaim_count(String claim_count) {
            this.claim_count = claim_count;
        }

        /**
         * @return the maturity_count
         */
        public String getMaturity_count() {
            return maturity_count;
        }

        /**
         * @param maturity_count the maturity_count to set
         */
        public void setMaturity_count(String maturity_count) {
            this.maturity_count = maturity_count;
        }

        /**
         * @return the lapse_count
         */
        public String getLapse_count() {
            return lapse_count;
        }

        /**
         * @param lapse_count the lapse_count to set
         */
        public void setLapse_count(String lapse_count) {
            this.lapse_count = lapse_count;
        }

        /**
         * @return the techlapse_count
         */
        public String getTechlapse_count() {
            return techlapse_count;
        }

        /**
         * @param techlapse_count the techlapse_count to set
         */
        public void setTechlapse_count(String techlapse_count) {
            this.techlapse_count = techlapse_count;
        }

        /**
         * @return the claim_count
         */
        public String getClaim_amt() {
            return claim_amt;
        }


        public void setClaim_amt(String claim_amt) {
            this.claim_amt = claim_amt;
        }

        /**
         * @return the surrender_amt
         */
        public String getSurrender_amt() {
            return surrender_amt;
        }

        /**
         * @param surrender_amt the surrender_amt to set
         */
        public void setSurrender_amt(String surrender_amt) {
            this.surrender_amt = surrender_amt;
        }

        /**
         * @return the nb_count
         */
        public String getNb_count() {
            return nb_count;
        }

        /**
         * @param nb_count the nb_count to set
         */
        public void setNb_count(String nb_count) {
            this.nb_count = nb_count;
        }

        /**
         * @return the nb_amt
         */
        public String getNb_amt() {
            return nb_amt;
        }

        /**
         * @param nb_amt the nb_amt to set
         */
        public void setNb_amt(String nb_amt) {
            this.nb_amt = nb_amt;
        }

        /**
         * @return the updatetime
         */
        /*
         * public String getUpdatetime() { return updatetime; }
         *//**
         * @param updatetime
         *            the updatetime to set
         */
        /*
         * public void setUpdatetime(String updatetime) { this.updatetime =
         * updatetime; }
         */

        /**
         * @return the frequency
         */
        public String getFrequency() {
            return frequency;
        }

        /**
         * @param frequency the frequency to set
         */
        public void setFrequency(String frequency) {
            this.frequency = frequency;
        }

        /**
         * @return the surable_count
         */
        public String getSurable_count() {
            return surable_count;
        }

        /**
         * @param surable_count the surable_count to set
         */
        public void setSurable_count(String surable_count) {
            this.surable_count = surable_count;
        }

    }

	/*<Table> <PER_PAN_NO>AQAPN1927</PER_PAN_NO>
	<PER_FULL_NAME>gcydj</PER_FULL_NAME>
	<UM_CODE>18749</UM_CODE> </Table>*/

    public class XMLHolder_Agent {

        private String proposalno;

        private String holderid;

        private String no;

        private String fname;

        private String lname;

        private String productname;
        private String policytype;
        private String policypaymentterm;
        private String policysumassured;
        private String premiumfrequency;
        private String grossamt;

        private String status;

        private String premiumup;

        XMLHolder_Agent(String propno, String holderid, String no,
                        String fname, String lname, String productname,
                        String policytype, String policypaymentterm, String sumassured,
                        String freq, String gamt, String status, String premiumup) {

            super();

            this.proposalno = propno;

            this.holderid = holderid;

            this.no = no;

            this.fname = fname;

            this.lname = lname;

            this.productname = productname;
            this.policytype = policytype;
            this.policypaymentterm = policypaymentterm;
            this.policysumassured = sumassured;
            this.premiumfrequency = freq;
            this.grossamt = gamt;

            this.status = status;

            this.premiumup = premiumup;

        }

        public String getProposalNo() {

            return proposalno;

        }

        public void setProposalNo(String ProposalNo) {

            this.proposalno = ProposalNo;

        }

        public String getHolderId() {

            return holderid;

        }

        public void setHolderId(String HolderId) {

            this.holderid = HolderId;

        }

        public String getNo() {

            return no;

        }

        public void setNo(String no) {

            this.no = no;

        }

        public String getFName() {

            return fname;

        }

        public void setFName(String fname) {

            this.fname = fname;

        }

        public String getLName() {

            return lname;

        }

        public void setLName(String lname) {

            this.lname = lname;

        }

        public String getProductName() {

            return productname;

        }

        public void setProductName(String productname) {

            this.productname = productname;

        }

        public String getPolicyType() {

            return policytype;

        }

        public void setPolicyType(String PolicyType) {

            this.policytype = PolicyType;

        }

        public String getPolicyPayTerm() {

            return policypaymentterm;

        }

        public void setPolicyPayTerm(String PolicyPayTerm) {

            this.policypaymentterm = PolicyPayTerm;

        }

        public String getPolicySumAssured() {

            return policysumassured;

        }

        public void setPolicySumAssured(String policysumassured) {

            this.policysumassured = policysumassured;

        }

        public String getPremiumFreq() {

            return premiumfrequency;

        }

        public void setPremiumFreq(String PremiumFreq) {

            this.premiumfrequency = PremiumFreq;

        }

        public String getGrossAmt() {

            return grossamt;

        }

        public void setGrossAmt(String GrossAmt) {

            this.grossamt = GrossAmt;

        }

        public String getStatus() {

            return status;

        }

        public void setStatus(String status) {

            this.status = status;

        }

        public String getPremiumUp() {

            return premiumup;

        }

        public void setPremiumUp(String premiumup) {

            this.premiumup = premiumup;

        }

    }

    public class XMLHolderBDM_MAIL_DATA {

        private String nbpmtd;

        private String nbpytd;

        private String nopmtd;

        private String nopytd;

        XMLHolderBDM_MAIL_DATA(String nbpmtd, String nbpytd,
                               String nopmtd, String nopytd) {

            super();

            this.nbpmtd = nbpmtd;

            this.nbpytd = nbpytd;

            this.nopmtd = nopmtd;

            this.nopytd = nopytd;
        }

        public String getNBPMTD() {

            return nbpmtd;

        }

        public void setNBPMTD(String nbpmtd) {

            this.nbpmtd = nbpmtd;

        }

        public String getNBPYTD() {

            return nbpytd;

        }

        public void setNBPYTD(String nbpytd) {

            this.nbpytd = nbpytd;

        }

        public String getNOPMTD() {

            return nopmtd;

        }

        public void setNOPMTD(String nopmtd) {

            this.nopmtd = nopmtd;

        }

        public String getNOPYTD() {

            return nopytd;

        }

        public void setNOPYTD(String nopytd) {

            this.nopytd = nopytd;

        }

    }

    public class XMLHolder_Agent_Comm {

        private String empname;
        private String empid;
        private String positiontype;
        private String channeltype;
        private String comm;
        private String tds;
        private String adjustment;
        private String servicetax;
        private String finalamt;

        XMLHolder_Agent_Comm(String empname, String empid,
                             String positiontype, String channeltype, String comm,
                             String tds, String adjustment, String servicetax,
                             String finalamt) {

            super();

            this.empname = empname;
            this.empid = empid;
            this.positiontype = positiontype;
            this.channeltype = channeltype;
            this.comm = comm;
            this.tds = tds;
            this.adjustment = adjustment;
            this.servicetax = servicetax;
            this.finalamt = finalamt;
        }

        /**
         * @return the empname
         */
        public String getEmpname() {
            return empname;
        }

        /**
         * @param empname the empname to set
         */
        public void setEmpname(String empname) {
            this.empname = empname;
        }

        /**
         * @return the empid
         */
        public String getEmpid() {
            return empid;
        }

        /**
         * @param empid the empid to set
         */
        public void setEmpid(String empid) {
            this.empid = empid;
        }

        /**
         * @return the positiontype
         */
        public String getPositiontype() {
            return positiontype;
        }

        /**
         * @param positiontype the positiontype to set
         */
        public void setPositiontype(String positiontype) {
            this.positiontype = positiontype;
        }

        /**
         * @return the channeltype
         */
        public String getChanneltype() {
            return channeltype;
        }

        /**
         * @param channeltype the channeltype to set
         */
        public void setChanneltype(String channeltype) {
            this.channeltype = channeltype;
        }

        /**
         * @return the comm
         */
        public String getComm() {
            return comm;
        }

        /**
         * @param comm the comm to set
         */
        public void setComm(String comm) {
            this.comm = comm;
        }

        /**
         * @return the tds
         */
        public String getTds() {
            return tds;
        }

        /**
         * @param tds the tds to set
         */
        public void setTds(String tds) {
            this.tds = tds;
        }

        /**
         * @return the adjustment
         */
        public String getAdjustment() {
            return adjustment;
        }

        /**
         * @param adjustment the adjustment to set
         */
        public void setAdjustment(String adjustment) {
            this.adjustment = adjustment;
        }

        /**
         * @return the servicetax
         */
        public String getServicetax() {
            return servicetax;
        }

        /**
         * @param servicetax the servicetax to set
         */
        public void setServicetax(String servicetax) {
            this.servicetax = servicetax;
        }

        /**
         * @return the finalamt
         */
        public String getFinalamt() {
            return finalamt;
        }

        /**
         * @param finalamt the finalamt to set
         */
        public void setFinalamt(String finalamt) {
            this.finalamt = finalamt;
        }

    }

    class XMLHolderDOB {

        private String dob;

        private String mob;

        private String email;

        XMLHolderDOB(String dob, String mob, String email) {

            super();

            this.dob = dob;

            this.mob = mob;

            this.email = email;

        }

        public String getDOB() {

            return dob;

        }

        public void setDOB(String dob) {

            this.dob = dob;

        }

        public String getMobile() {

            return mob;

        }

        public void setMobile(String mob) {

            this.mob = mob;

        }

        public String getEmail() {

            return email;

        }

        public void setEmail(String email) {

            this.email = email;

        }

    }

    public class XMLHolder_Req {

        private String desc;
        private String reqflag;
        private String comments;
        private String raiseddate;
        private String payamt;
        private String propno;
        private String payageing;

        XMLHolder_Req(String desc, String reqflag, String comments,
                      String raiseddate, String payamt, String propno,
                      String payageing) {
            super();
            this.desc = desc;
            this.reqflag = reqflag;
            this.comments = comments;
            this.raiseddate = raiseddate;
            this.payamt = payamt;
            this.propno = propno;
            this.payageing = payageing;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getReqflag() {
            return reqflag;
        }

        public void setReqflag(String reqflag) {
            this.reqflag = reqflag;
        }

        public String getComments() {
            return comments;
        }

        public void setComments(String comments) {
            this.comments = comments;
        }

        public String getRaiseddate() {
            return raiseddate;
        }

        public void setRaiseddate(String raiseddate) {
            this.raiseddate = raiseddate;
        }

        public String getPayment() {
            return payamt;
        }

        public void setPayment(String payamt) {
            this.payamt = payamt;
        }

        public String getProposalNo() {
            return propno;
        }

        public void setProposalNo(String propno) {
            this.propno = propno;
        }

        public String getPayAgeing() {
            return payageing;
        }

        public void setPayAgeing(String payageing) {
            this.payageing = payageing;
        }

    }

    public class XMLHolderPersistency {

        private String curmonth_collected_pre;
        private String curmonth_collectable_pre;
        private String march_collectable_pre;
        private String curmonth_ratio;
        private String fy_ratio;
        private String UNPAID_POLICYCOUNT;
        private String EMPLOYEEID;
        private String EMPLOYEENAME;
        private int colorCode;


        XMLHolderPersistency(String curmonth_collected_pre,
                             String curmonth_collectable_pre, String march_collectable_pre,
                             String curmonth_ratio, String fy_ratio, String UNPAID_POLICYCOUNT,
                             String EMPLOYEEID, String EMPLOYEENAME, int colorCode) {
            super();
            this.curmonth_collected_pre = curmonth_collected_pre;
            this.curmonth_collectable_pre = curmonth_collectable_pre;
            this.march_collectable_pre = march_collectable_pre;
            this.curmonth_ratio = curmonth_ratio;
            this.fy_ratio = fy_ratio;
            this.UNPAID_POLICYCOUNT = UNPAID_POLICYCOUNT;
            this.EMPLOYEEID = EMPLOYEEID;
            this.EMPLOYEENAME = EMPLOYEENAME;
            this.colorCode = colorCode;

        }

        public String getCurmonth_collected_pre() {
            return curmonth_collected_pre;
        }

        public String getUNPAID_POLICYCOUNT() {
            return UNPAID_POLICYCOUNT;
        }


        public String getCurmonth_collectable_pre() {
            return curmonth_collectable_pre;
        }


        public String getMarch_collectable_pre() {
            return march_collectable_pre;
        }


        public String getCurmonth_ratio() {
            return curmonth_ratio;
        }


        public String getFy_ratio() {
            return fy_ratio;
        }


        public String getEMPLOYEEID() {
            return EMPLOYEEID;
        }

        public String getEMPLOYEENAME() {
            return EMPLOYEENAME;
        }

        public int getColorCode() {
            return colorCode;
        }
    }

    public class XMLHolderBankList {

        private String bankName;

        XMLHolderBankList(String bankName) {
            super();
            this.bankName = bankName;
        }

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }
    }

    class XMLHolder_UM_BDM_DAH {

        private String SYSTEM;

        private String YTD_INITIAL_CASHIERING_AMOUNT;

        private String MTD_INITIAL_CASHIERING_AMOUNT;

        private String YTD_INITIAL_CASHIERING_COUNT;

        private String MTD_INITIAL_CASHIERING_COUNT;
        private String YTD_ISSUANCE_COUNT;
        private String MTD_ISSUANCE_COUNT;
        private String YTD_ISSUANCE_AMOUNT;
        private String MTD_ISSUANCE_AMOUNT;
        private String PENDING_ISSUANCE_COUNT;

        private String RENEWAL_COLLECTION_DUE;

        XMLHolder_UM_BDM_DAH(String SYSTEM,
                             String YTD_INITIAL_CASHIERING_AMOUNT,
                             String MTD_INITIAL_CASHIERING_AMOUNT,
                             String YTD_INITIAL_CASHIERING_COUNT,
                             String MTD_INITIAL_CASHIERING_COUNT, String YTD_ISSUANCE_COUNT,
                             String MTD_ISSUANCE_COUNT, String YTD_ISSUANCE_AMOUNT,
                             String MTD_ISSUANCE_AMOUNT, String PENDING_ISSUANCE_COUNT,
                             String RENEWAL_COLLECTION_DUE) {

            super();

            this.SYSTEM = SYSTEM;

            this.YTD_INITIAL_CASHIERING_AMOUNT = YTD_INITIAL_CASHIERING_AMOUNT;

            this.MTD_INITIAL_CASHIERING_AMOUNT = MTD_INITIAL_CASHIERING_AMOUNT;

            this.YTD_INITIAL_CASHIERING_COUNT = YTD_INITIAL_CASHIERING_COUNT;

            this.MTD_INITIAL_CASHIERING_COUNT = MTD_INITIAL_CASHIERING_COUNT;
            this.YTD_ISSUANCE_COUNT = YTD_ISSUANCE_COUNT;
            this.MTD_ISSUANCE_COUNT = MTD_ISSUANCE_COUNT;
            this.YTD_ISSUANCE_AMOUNT = YTD_ISSUANCE_AMOUNT;
            this.MTD_ISSUANCE_AMOUNT = MTD_ISSUANCE_AMOUNT;
            this.PENDING_ISSUANCE_COUNT = PENDING_ISSUANCE_COUNT;

            this.RENEWAL_COLLECTION_DUE = RENEWAL_COLLECTION_DUE;

        }

        public String getSYSTEM() {

            return SYSTEM;

        }

        public void setSYSTEM(String SYSTEM) {

            this.SYSTEM = SYSTEM;

        }

        public String getYTD_INITIAL_CASHIERING_AMOUNT() {

            return YTD_INITIAL_CASHIERING_AMOUNT;

        }

        public void setYTD_INITIAL_CASHIERING_AMOUNT(
                String YTD_INITIAL_CASHIERING_AMOUNT) {

            this.YTD_INITIAL_CASHIERING_AMOUNT = YTD_INITIAL_CASHIERING_AMOUNT;

        }

        public String getMTD_INITIAL_CASHIERING_AMOUNT() {

            return MTD_INITIAL_CASHIERING_AMOUNT;

        }

        public void setMTD_INITIAL_CASHIERING_AMOUNT(
                String MTD_INITIAL_CASHIERING_AMOUNT) {

            this.MTD_INITIAL_CASHIERING_AMOUNT = MTD_INITIAL_CASHIERING_AMOUNT;

        }

        public String getYTD_INITIAL_CASHIERING_COUNT() {

            return YTD_INITIAL_CASHIERING_COUNT;

        }

        public void setYTD_INITIAL_CASHIERING_COUNT(
                String YTD_INITIAL_CASHIERING_COUNT) {

            this.YTD_INITIAL_CASHIERING_COUNT = YTD_INITIAL_CASHIERING_COUNT;

        }

        public String getMTD_INITIAL_CASHIERING_COUNT() {

            return MTD_INITIAL_CASHIERING_COUNT;

        }

        public void setMTD_INITIAL_CASHIERING_COUNT(
                String MTD_INITIAL_CASHIERING_COUNT) {

            this.MTD_INITIAL_CASHIERING_COUNT = MTD_INITIAL_CASHIERING_COUNT;

        }

        public String getYTD_ISSUANCE_COUNT() {

            return YTD_ISSUANCE_COUNT;

        }

        public void setYTD_ISSUANCE_COUNT(String YTD_ISSUANCE_COUNT) {

            this.YTD_ISSUANCE_COUNT = YTD_ISSUANCE_COUNT;

        }

        public String getMTD_ISSUANCE_COUNT() {

            return MTD_ISSUANCE_COUNT;

        }

        public void setMTD_ISSUANCE_COUNT(String MTD_ISSUANCE_COUNT) {

            this.MTD_ISSUANCE_COUNT = MTD_ISSUANCE_COUNT;

        }

        public String getYTD_ISSUANCE_AMOUNT() {

            return YTD_ISSUANCE_AMOUNT;

        }

        public void setYTD_ISSUANCE_AMOUNT(String YTD_ISSUANCE_AMOUNT) {

            this.YTD_ISSUANCE_AMOUNT = YTD_ISSUANCE_AMOUNT;

        }

        public String getMTD_ISSUANCE_AMOUNT() {

            return MTD_ISSUANCE_AMOUNT;

        }

        public void setMTD_ISSUANCE_AMOUNT(String MTD_ISSUANCE_AMOUNT) {

            this.MTD_ISSUANCE_AMOUNT = MTD_ISSUANCE_AMOUNT;

        }

        public String getPENDING_ISSUANCE_COUNT() {

            return PENDING_ISSUANCE_COUNT;

        }

        public void setPENDING_ISSUANCE_COUNT(String PENDING_ISSUANCE_COUNT) {

            this.PENDING_ISSUANCE_COUNT = PENDING_ISSUANCE_COUNT;

        }

        public String getRENEWAL_COLLECTION_DUE() {

            return RENEWAL_COLLECTION_DUE;

        }

        public void setRENEWAL_COLLECTION_DUE(String RENEWAL_COLLECTION_DUE) {

            this.RENEWAL_COLLECTION_DUE = RENEWAL_COLLECTION_DUE;

        }
    }

    public class XMLDOCREQList {

        private String DESCRIPTION;
        private String REQUIREMENTFLAG;
        private String propStatus;

        XMLDOCREQList(String DESCRIPTION, String REQUIREMENTFLAG,
                      String propStatus) {
            super();
            this.DESCRIPTION = DESCRIPTION;
            this.REQUIREMENTFLAG = REQUIREMENTFLAG;
            this.propStatus = propStatus;
        }

        public String getDESCRIPTION() {
            return DESCRIPTION;
        }

        public void setDESCRIPTION(String DESCRIPTION) {
            this.DESCRIPTION = DESCRIPTION;
        }

        public String getREQUIREMENTFLAG() {
            return REQUIREMENTFLAG;
        }

        public void setREQUIREMENTFLAG(String REQUIREMENTFLAG) {
            this.REQUIREMENTFLAG = REQUIREMENTFLAG;
        }

        public String getPropStatus() {
            return propStatus;
        }

        public void setPropStatus(String propStatus) {
            this.propStatus = propStatus;
        }
    }

    public class XMLProposerTrackerList {

        private String proposerNumber;
        private String policyHolderName;
        private String lifeAssuredName;
        private String status;
        private String reason;
        private String IRRole;
        private String CONTACT_MOBILE;

        XMLProposerTrackerList(String proposerNumber,
                               String policyHolderName, String lifeAssuredName, String status,
                               String reason, String iRRole, String CONTACT_MOBILE) {
            super();
            this.proposerNumber = proposerNumber;
            this.policyHolderName = policyHolderName;
            this.lifeAssuredName = lifeAssuredName;
            this.status = status;
            this.reason = reason;
            IRRole = iRRole;
            this.CONTACT_MOBILE = CONTACT_MOBILE;
        }

        public String getProposerNumber() {
            return proposerNumber;
        }

        public String getPolicyHolderName() {
            return policyHolderName;
        }

        public String getLifeAssuredName() {
            return lifeAssuredName;
        }

        public String getStatus() {
            return status;
        }

        public String getReason() {
            return reason;
        }

        public String getIRRole() {
            return IRRole;
        }

        public String getCONTACT_MOBILE() {
            return CONTACT_MOBILE;
        }

    }

    public class XMLChannelProposerTrackerStatusList {

        private String proposalNumber;
        private String paymentAmount;
        private String cashieringDate;
        private String status;
        private String pendingStatus;
        private String CHANNELCODE;

        XMLChannelProposerTrackerStatusList(String proposalNumber,
                                            String paymentAmount, String cashieringDate, String status,
                                            String pendingStatus, String CHANNELCODE) {
            super();
            this.proposalNumber = proposalNumber;
            this.paymentAmount = paymentAmount;
            this.cashieringDate = cashieringDate;
            this.status = status;
            this.pendingStatus = pendingStatus;
            this.CHANNELCODE = CHANNELCODE;
        }

        public String getProposalNumber() {
            return proposalNumber;
        }

        public String getPaymentAmount() {
            return paymentAmount;
        }

        public String getCashieringDate() {
            return cashieringDate;
        }

        public String getStatus() {
            return status;
        }

        public String getPendingStatus() {
            return pendingStatus;
        }

        public String getCHANNELCODE() {
            return CHANNELCODE;
        }
    }

    public class XMLHolderSBDueList {

        private String policyNumber;
        private String customerCode;
        private String lifeAssuredName;
        private String status;
        private String benefitTerm;
        private String policyRiskCommencementDate;
        private String maturityDate;
        private String paymentAmount;
        private String firstName;
        private String DOB;
        private String city;
        private String address;
        private String postalCode;
        private String state;
        private String contactNumber;

        XMLHolderSBDueList(String policyNumber, String customerCode,
                           String lifeAssuredName, String status, String benefitTerm,
                           String policyRiskCommencementDate, String maturityDate,
                           String paymentAmount, String firstName, String dOB,
                           String city, String address, String postalCode, String state,
                           String contactNumber) {
            super();
            this.policyNumber = policyNumber;
            this.customerCode = customerCode;
            this.lifeAssuredName = lifeAssuredName;
            this.status = status;
            this.benefitTerm = benefitTerm;
            this.policyRiskCommencementDate = policyRiskCommencementDate;
            this.maturityDate = maturityDate;
            this.paymentAmount = paymentAmount;
            this.firstName = firstName;
            DOB = dOB;
            this.city = city;
            this.address = address;
            this.postalCode = postalCode;
            this.state = state;
            this.contactNumber = contactNumber;
        }

        public String getPolicyNumber() {
            return policyNumber;
        }

        public String getCustomerCode() {
            return customerCode;
        }

        public String getLifeAssuredName() {
            return lifeAssuredName;
        }

        public String getStatus() {
            return status;
        }

        public String getBenefitTerm() {
            return benefitTerm;
        }

        public String getPolicyRiskCommencementDate() {
            return policyRiskCommencementDate;
        }

        public String getMaturityDate() {
            return maturityDate;
        }

        public String getPaymentAmount() {
            return paymentAmount;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getDOB() {
            return DOB;
        }

        public String getCity() {
            return city;
        }

        public String getAddress() {
            return address;
        }

        public String getPostalCode() {
            return postalCode;
        }

        public String getState() {
            return state;
        }

        public String getContactNumber() {
            return contactNumber;
        }
    }

    public class XMLHolderIssuPolicy {

        private String proposal_no;

        public XMLHolderIssuPolicy(String proposal_no) {

            super();

            this.proposal_no = proposal_no;

        }

        public String getproposal_no() {

            return proposal_no;

        }

        public void setproposal_no(String proposal_no) {

            this.proposal_no = proposal_no;

        }
    }

    public class XMLHolderAdvanceQueryResult {

        // PaymentID,PAY_EX1_91,PAY_EX1_96,HtmlText_280,PAY_EX1_95,PAY_EX1_94
        String PAY_EX1_91;
        String PAY_EX1_96;
        String HTMLTEXT_280;
        String PAY_EX1_95;
        String PAY_EX1_94;
        String ROWNUMBER;
        String Key;

        XMLHolderAdvanceQueryResult(String pAY_EX1_91,
                                    String pAY_EX1_96, String hTMLTEXT_280, String pAY_EX1_95,
                                    String pAY_EX1_94, String rOWNUMBER, String key) {
            super();
            PAY_EX1_91 = pAY_EX1_91;
            PAY_EX1_96 = pAY_EX1_96;
            HTMLTEXT_280 = hTMLTEXT_280;
            PAY_EX1_95 = pAY_EX1_95;
            PAY_EX1_94 = pAY_EX1_94;
            ROWNUMBER = rOWNUMBER;
            Key = key;
        }

        /*
         * <PAY_EX1_91>Interested</PAY_EX1_91> <PAY_EX1_96>Interested to pay
         * online</PAY_EX1_96> <HTMLTEXT_280>LastModified By:LastModified
         * On:04-05-2017 10:47:38 AM Call Centre Remarks</HTMLTEXT_280>
         * <PAY_EX1_95>Amber</PAY_EX1_95> <PAY_EX1_94>8776555432</PAY_EX1_94>
         * <ROWNUMBER>1</ROWNUMBER> <Key>100521</Key>
         */

        public String getPAY_EX1_91() {
            return PAY_EX1_91;
        }

        public String getPAY_EX1_96() {
            return PAY_EX1_96;
        }

        public String getHTMLTEXT_280() {
            return HTMLTEXT_280;
        }

        public String getPAY_EX1_95() {
            return PAY_EX1_95;
        }

        public String getPAY_EX1_94() {
            return PAY_EX1_94;
        }

        public void setPAY_EX1_94(String PAY_EX1_94) {
            this.PAY_EX1_94 = PAY_EX1_94;
        }

        public String getROWNUMBER() {
            return ROWNUMBER;
        }

        public String getKey() {
            return Key;
        }

    }

    public class XMLHolderGroupTermCDStatement {

        private String policy_no = "", policy_holder_name = "", doc = "", ard = "",
                transaction_date = "", transaction_type = "", description = "", transaction_amount = "",
                balance_amount = "", payment_purpose = "", in_out = "";

        XMLHolderGroupTermCDStatement(String policy_no, String policy_holder_name, String doc, String ard, String transaction_date, String transaction_type, String description, String transaction_amount, String balance_amount, String payment_purpose, String in_out) {
            this.policy_no = policy_no;
            this.policy_holder_name = policy_holder_name;
            this.doc = doc;
            this.ard = ard;
            this.transaction_date = transaction_date;
            this.transaction_type = transaction_type;
            this.description = description;
            this.transaction_amount = transaction_amount;
            this.balance_amount = balance_amount;
            this.payment_purpose = payment_purpose;
            this.in_out = in_out;
        }

        public String getPolicy_no() {
            return policy_no;
        }

        public void setPolicy_no(String policy_no) {
            this.policy_no = policy_no;
        }

        public String getPolicy_holder_name() {
            return policy_holder_name;
        }

        public void setPolicy_holder_name(String policy_holder_name) {
            this.policy_holder_name = policy_holder_name;
        }

        public String getDoc() {
            return doc;
        }

        public void setDoc(String doc) {
            this.doc = doc;
        }

        public String getArd() {
            return ard;
        }

        public void setArd(String ard) {
            this.ard = ard;
        }

        public String getTransaction_date() {
            return transaction_date;
        }

        public void setTransaction_date(String transaction_date) {
            this.transaction_date = transaction_date;
        }

        public String getTransaction_type() {
            return transaction_type;
        }

        public void setTransaction_type(String transaction_type) {
            this.transaction_type = transaction_type;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getTransaction_amount() {
            return transaction_amount;
        }

        public void setTransaction_amount(String transaction_amount) {
            this.transaction_amount = transaction_amount;
        }

        public String getBalance_amount() {
            return balance_amount;
        }

        public void setBalance_amount(String balance_amount) {
            this.balance_amount = balance_amount;
        }

        public String getPayment_purpose() {
            return payment_purpose;
        }

        public void setPayment_purpose(String payment_purpose) {
            this.payment_purpose = payment_purpose;
        }

        public String getIn_out() {
            return in_out;
        }

        public void setIn_out(String in_out) {
            this.in_out = in_out;
        }
    }

    public class XMLHolderGroupMiniFundStatement {
        private String policy_no = "", biling_date = "", description = "", transaction_amount = "",
                balance = "";

        XMLHolderGroupMiniFundStatement(String policy_no, String biling_date, String description, String transaction_amount, String balance) {
            this.policy_no = policy_no;
            this.biling_date = biling_date;
            this.description = description;
            this.transaction_amount = transaction_amount;
            this.balance = balance;
        }

        public String getPolicy_no() {
            return policy_no;
        }

        public void setPolicy_no(String policy_no) {
            this.policy_no = policy_no;
        }

        public String getBiling_date() {
            return biling_date;
        }

        public void setBiling_date(String biling_date) {
            this.biling_date = biling_date;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getTransaction_amount() {
            return transaction_amount;
        }

        public void setTransaction_amount(String transaction_amount) {
            this.transaction_amount = transaction_amount;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }
    }

    public class XMLHolderGroupQuoteIDSearch {
        private String policy_no = "", policy_holder = "", str_doc = "";

        XMLHolderGroupQuoteIDSearch(String policy_no, String policy_holder, String str_doc) {
            this.policy_no = policy_no;
            this.policy_holder = policy_holder;
            this.str_doc = str_doc;
        }

        public String getPolicy_no() {
            return policy_no;
        }

        public void setPolicy_no(String policy_no) {
            this.policy_no = policy_no;
        }

        public String getPolicy_holder() {
            return policy_holder;
        }

        public void setPolicy_holder(String policy_holder) {
            this.policy_holder = policy_holder;
        }

        public String getStr_doc() {
            return str_doc;
        }

        public void setStr_doc(String str_doc) {
            this.str_doc = str_doc;
        }
    }

    public class XMLHolderGroupMemberView {
        private String policy_no = "", policy_holder = "", str_doc = "", member_emp_id = "", member_name = "",
                member_rcd = "", member_sum_assured = "", rider_ppd = "", rider_AD = "", rider_cl = "", rider_tpd = "",
                rider_sum_assured = "", nominee_name = "", nominee_relationship = "";

        XMLHolderGroupMemberView(String policy_no, String policy_holder, String str_doc, String member_emp_id, String member_name, String member_rcd, String member_sum_assured, String rider_ppd, String rider_AD, String rider_cl, String rider_tpd,
                                 String rider_sum_assured, String nominee_name, String nominee_relationship) {
            this.policy_no = policy_no;
            this.policy_holder = policy_holder;
            this.str_doc = str_doc;
            this.member_emp_id = member_emp_id;
            this.member_name = member_name;
            this.member_rcd = member_rcd;
            this.member_sum_assured = member_sum_assured;
            this.rider_ppd = rider_ppd;
            this.rider_AD = rider_AD;
            this.rider_cl = rider_cl;
            this.rider_tpd = rider_tpd;
            this.rider_sum_assured = rider_sum_assured;
            this.nominee_name = nominee_name;
            this.nominee_relationship = nominee_relationship;
        }

        public String getRider_ppd() {
            return rider_ppd;
        }

        public void setRider_ppd(String rider_ppd) {
            this.rider_ppd = rider_ppd;
        }

        public String getRider_AD() {
            return rider_AD;
        }

        public void setRider_AD(String rider_AD) {
            this.rider_AD = rider_AD;
        }

        public String getRider_cl() {
            return rider_cl;
        }

        public void setRider_cl(String rider_cl) {
            this.rider_cl = rider_cl;
        }

        public String getRider_tpd() {
            return rider_tpd;
        }

        public void setRider_tpd(String rider_tpd) {
            this.rider_tpd = rider_tpd;
        }

        public String getPolicy_no() {
            return policy_no;
        }

        public void setPolicy_no(String policy_no) {
            this.policy_no = policy_no;
        }

        public String getPolicy_holder() {
            return policy_holder;
        }

        public void setPolicy_holder(String policy_holder) {
            this.policy_holder = policy_holder;
        }

        public String getStr_doc() {
            return str_doc;
        }

        public void setStr_doc(String str_doc) {
            this.str_doc = str_doc;
        }

        public String getMember_emp_id() {
            return member_emp_id;
        }

        public void setMember_emp_id(String member_emp_id) {
            this.member_emp_id = member_emp_id;
        }

        public String getMember_name() {
            return member_name;
        }

        public void setMember_name(String member_name) {
            this.member_name = member_name;
        }

        public String getMember_rcd() {
            return member_rcd;
        }

        public void setMember_rcd(String member_rcd) {
            this.member_rcd = member_rcd;
        }

        public String getMember_sum_assured() {
            return member_sum_assured;
        }

        public void setMember_sum_assured(String member_sum_assured) {
            this.member_sum_assured = member_sum_assured;
        }

        public String getRider_sum_assured() {
            return rider_sum_assured;
        }

        public void setRider_sum_assured(String rider_sum_assured) {
            this.rider_sum_assured = rider_sum_assured;
        }

        public String getNominee_name() {
            return nominee_name;
        }

        public void setNominee_name(String nominee_name) {
            this.nominee_name = nominee_name;
        }

        public String getNominee_relationship() {
            return nominee_relationship;
        }

        public void setNominee_relationship(String nominee_relationship) {
            this.nominee_relationship = nominee_relationship;
        }
    }

    public class XMLHolderGroupSearchClaimView {
        private String claim_id = "", claim_status = "", employee_id = "", member_name = "", saction_date = "",
                claim_amount = "";

        XMLHolderGroupSearchClaimView(String claim_id, String claim_status, String employee_id,
                                      String member_name, String saction_date, String claim_amount) {
            this.claim_id = claim_id;
            this.claim_status = claim_status;
            this.employee_id = employee_id;
            this.member_name = member_name;
            this.saction_date = saction_date;
            this.claim_amount = claim_amount;
        }

        public String getClaim_id() {
            return claim_id;
        }

        public void setClaim_id(String claim_id) {
            this.claim_id = claim_id;
        }

        public String getClaim_status() {
            return claim_status;
        }

        public void setClaim_status(String claim_status) {
            this.claim_status = claim_status;
        }

        public String getEmployee_id() {
            return employee_id;
        }

        public void setEmployee_id(String employee_id) {
            this.employee_id = employee_id;
        }

        public String getMember_name() {
            return member_name;
        }

        public void setMember_name(String member_name) {
            this.member_name = member_name;
        }

        public String getSaction_date() {
            return saction_date;
        }

        public void setSaction_date(String saction_date) {
            this.saction_date = saction_date;
        }

        public String getClaim_amount() {
            return claim_amount;
        }

        public void setClaim_amount(String claim_amount) {
            this.claim_amount = claim_amount;
        }
    }

    public class XMLHolderGroupFundSearchMemberView {
        private String policy_no = "", policy_holder = "", str_doc = "", employee_id = "", member_name = "",
                balance_fund = "";

        XMLHolderGroupFundSearchMemberView(String policy_no, String policy_holder, String str_doc, String employee_id, String member_name, String balance_fund) {
            this.policy_no = policy_no;
            this.policy_holder = policy_holder;
            this.str_doc = str_doc;
            this.employee_id = employee_id;
            this.member_name = member_name;
            this.balance_fund = balance_fund;
        }

        public String getPolicy_no() {
            return policy_no;
        }

        public void setPolicy_no(String policy_no) {
            this.policy_no = policy_no;
        }

        public String getPolicy_holder() {
            return policy_holder;
        }

        public void setPolicy_holder(String policy_holder) {
            this.policy_holder = policy_holder;
        }

        public String getStr_doc() {
            return str_doc;
        }

        public void setStr_doc(String str_doc) {
            this.str_doc = str_doc;
        }

        public String getEmployee_id() {
            return employee_id;
        }

        public void setEmployee_id(String employee_id) {
            this.employee_id = employee_id;
        }

        public String getMember_name() {
            return member_name;
        }

        public void setMember_name(String member_name) {
            this.member_name = member_name;
        }

        public String getBalance_fund() {
            return balance_fund;
        }

        public void setBalance_fund(String balance_fund) {
            this.balance_fund = balance_fund;
        }
    }

    public class XMLHolderGroupFundSearchPolicy {
        private String policy_no = "", policy_holder = "", str_doc = "", fund_balance = "", unadjusted_deposite = "",
                policy_status = "";

        XMLHolderGroupFundSearchPolicy(String policy_no, String policy_holder, String str_doc, String fund_balance, String unadjusted_deposite, String policy_status) {
            this.policy_no = policy_no;
            this.policy_holder = policy_holder;
            this.str_doc = str_doc;
            this.fund_balance = fund_balance;
            this.unadjusted_deposite = unadjusted_deposite;
            this.policy_status = policy_status;
        }

        public String getPolicy_no() {
            return policy_no;
        }

        public void setPolicy_no(String policy_no) {
            this.policy_no = policy_no;
        }

        public String getPolicy_holder() {
            return policy_holder;
        }

        public void setPolicy_holder(String policy_holder) {
            this.policy_holder = policy_holder;
        }

        public String getStr_doc() {
            return str_doc;
        }

        public void setStr_doc(String str_doc) {
            this.str_doc = str_doc;
        }

        public String getFund_balance() {
            return fund_balance;
        }

        public void setFund_balance(String fund_balance) {
            this.fund_balance = fund_balance;
        }

        public String getUnadjusted_deposite() {
            return unadjusted_deposite;
        }

        public void setUnadjusted_deposite(String unadjusted_deposite) {
            this.unadjusted_deposite = unadjusted_deposite;
        }

        public String getPolicy_status() {
            return policy_status;
        }

        public void setPolicy_status(String policy_status) {
            this.policy_status = policy_status;
        }
    }

    public class XMLHolderGroupPolicySearch {
        private String policy_no = "", policy_holder = "", str_doc = "", strARD = "", total_premium = "",
                lives = "", unadjusted_deposite = "", rideropted = "", str_fcl = "", policy_status = "",
                basic_premium_rate = "", rider_premium_rate = "", total_sum_assured = "", type_scheme = "";

        XMLHolderGroupPolicySearch(String policy_no, String policy_holder, String str_doc, String strARD, String total_premium,
                                   String lives, String unadjusted_deposite, String rideropted, String str_fcl, String policy_status,
                                   String basic_premium_rate, String rider_premium_rate, String total_sum_assured, String type_scheme) {
            this.policy_no = policy_no;
            this.policy_holder = policy_holder;
            this.str_doc = str_doc;
            this.strARD = strARD;
            this.total_premium = total_premium;
            this.lives = lives;
            this.unadjusted_deposite = unadjusted_deposite;
            this.rideropted = rideropted;
            this.str_fcl = str_fcl;
            this.policy_status = policy_status;
            this.basic_premium_rate = basic_premium_rate;
            this.rider_premium_rate = rider_premium_rate;
            this.total_sum_assured = total_sum_assured;
            this.type_scheme = type_scheme;
        }


        public String getRider_premium_rate() {
            return rider_premium_rate;
        }

        public void setRider_premium_rate(String rider_premium_rate) {
            this.rider_premium_rate = rider_premium_rate;
        }

        public String getPolicy_no() {
            return policy_no;
        }

        public void setPolicy_no(String policy_no) {
            this.policy_no = policy_no;
        }

        public String getPolicy_holder() {
            return policy_holder;
        }

        public void setPolicy_holder(String policy_holder) {
            this.policy_holder = policy_holder;
        }

        public String getStr_doc() {
            return str_doc;
        }

        public void setStr_doc(String str_doc) {
            this.str_doc = str_doc;
        }

        public String getStrARD() {
            return strARD;
        }

        public void setStrARD(String strARD) {
            this.strARD = strARD;
        }

        public String getTotal_premium() {
            return total_premium;
        }

        public void setTotal_premium(String total_premium) {
            this.total_premium = total_premium;
        }

        public String getLives() {
            return lives;
        }

        public void setLives(String lives) {
            this.lives = lives;
        }

        public String getUnadjusted_deposite() {
            return unadjusted_deposite;
        }

        public void setUnadjusted_deposite(String unadjusted_deposite) {
            this.unadjusted_deposite = unadjusted_deposite;
        }

        public String getRideropted() {
            return rideropted;
        }

        public void setRideropted(String rideropted) {
            this.rideropted = rideropted;
        }

        public String getStr_fcl() {
            return str_fcl;
        }

        public void setStr_fcl(String str_fcl) {
            this.str_fcl = str_fcl;
        }

        public String getPolicy_status() {
            return policy_status;
        }

        public void setPolicy_status(String policy_status) {
            this.policy_status = policy_status;
        }

        public String getBasic_premium_rate() {
            return basic_premium_rate;
        }

        public void setBasic_premium_rate(String basic_premium_rate) {
            this.basic_premium_rate = basic_premium_rate;
        }

        public String getTotal_sum_assured() {
            return total_sum_assured;
        }

        public void setTotal_sum_assured(String total_sum_assured) {
            this.total_sum_assured = total_sum_assured;
        }

        public String getType_scheme() {
            return type_scheme;
        }

        public void setType_scheme(String type_scheme) {
            this.type_scheme = type_scheme;
        }
    }

    public class XMLHolderGroupUnderritting {

        private String member_name = "", member_id = "", requirement_raised = "";

        XMLHolderGroupUnderritting(String member_name, String member_id, String requirement_raised) {
            this.member_name = member_name;
            this.member_id = member_id;
            this.requirement_raised = requirement_raised;
        }

        public String getMember_name() {
            return member_name;
        }

        public void setMember_name(String member_name) {
            this.member_name = member_name;
        }

        public String getMember_id() {
            return member_id;
        }

        public void setMember_id(String member_id) {
            this.member_id = member_id;
        }

        public String getRequirement_raised() {
            return requirement_raised;
        }

        public void setRequirement_raised(String requirement_raised) {
            this.requirement_raised = requirement_raised;
        }
    }

    public class XMLHolderGroupFundContribution {

        private String premium_billed_date = "", amount = "";

        XMLHolderGroupFundContribution(String premium_billed_date, String amount) {
            this.premium_billed_date = premium_billed_date;
            this.amount = amount;
        }

        public String getPremium_billed_date() {
            return premium_billed_date;
        }

        public void setPremium_billed_date(String premium_billed_date) {
            this.premium_billed_date = premium_billed_date;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }
    }

    public class XMLHolderGroupFundClaimList {

        private String employee_id = "";
        private String MEMBER_NAME = "";
        private String CLAIM_SANCTION_DATE = "";
        private String CLAIM_AMOUNT = "";

        XMLHolderGroupFundClaimList(String employee_id, String MEMBER_NAME, String CLAIM_SANCTION_DATE, String CLAIM_AMOUNT) {
            this.employee_id = employee_id;
            this.MEMBER_NAME = MEMBER_NAME;
            this.CLAIM_SANCTION_DATE = CLAIM_SANCTION_DATE;
            this.CLAIM_AMOUNT = CLAIM_AMOUNT;
        }

        public String getEmployee_id() {
            return employee_id;
        }

        public String getMEMBER_NAME() {
            return MEMBER_NAME;
        }

        public String getCLAIM_SANCTION_DATE() {
            return CLAIM_SANCTION_DATE;
        }

        public String getCLAIM_AMOUNT() {
            return CLAIM_AMOUNT;
        }
    }

    public class XMLHolderGroupNewBusinessList {

        // PaymentID,PAY_EX1_91,PAY_EX1_96,HtmlText_280,PAY_EX1_95,PAY_EX1_94
        private String POLICY_NO = "", POLICY_HOLDER_NAME = "", POL_START_DATE = "";

        XMLHolderGroupNewBusinessList(String pOLICY_NO,
                                      String pOLICY_HOLDER_NAME, String pOL_START_DATE) {
            super();
            POLICY_NO = pOLICY_NO;
            POLICY_HOLDER_NAME = pOLICY_HOLDER_NAME;
            POL_START_DATE = pOL_START_DATE;
        }

        public String getPOLICY_NO() {
            return POLICY_NO;
        }

        public String getPOLICY_HOLDER_NAME() {
            return POLICY_HOLDER_NAME;
        }

        public String getPOL_START_DATE() {
            return POL_START_DATE;
        }

        /*
         * <CIFPolicyList> <Table> <POLICY_NO>72100249001</POLICY_NO>
         * <POLICY_HOLDER_NAME>DD 428 THENI DIST EDUCATIONAL &amp; ELECTRICITY
         * DISTRI</POLICY_HOLDER_NAME> <POL_END_DATE>30-05-2018</POL_END_DATE>
         * </Table> <Table> <POLICY_NO>72100250406</POLICY_NO>
         * <POLICY_HOLDER_NAME>NICHI-IN SOFTWARE SOLUTION PRIVATE
         * LIMITED.</POLICY_HOLDER_NAME> <POL_END_DATE>30-05-2018</POL_END_DATE>
         * </Table>  </CIFPolicyList>
         */

    }

    public class XMLHolderGroupTermLapseList {
			/*<Table> <POLICY_NO>72100047707</POLICY_NO>
			<POLICY_HOLDER_NAME>KALINDEE RAIL NIRMAN (ENGINEER) LIMITED</POLICY_HOLDER_NAME>
			<POL_START_DATE>02-07-2014</POL_START_DATE> <RENEWAL_DUE_DATE>01-08-2017</RENEWAL_DUE_DATE>
			<POLICY_STATUS>LAPSED</POLICY_STATUS> </Table> */

        String POLICY_NO = "", POLICY_HOLDER_NAME = "", POL_START_DATE = "";

        XMLHolderGroupTermLapseList(String pOLICY_NO,
                                    String pOLICY_HOLDER_NAME, String pOL_START_DATE) {
            super();
            POLICY_NO = pOLICY_NO;
            POLICY_HOLDER_NAME = pOLICY_HOLDER_NAME;
            POL_START_DATE = pOL_START_DATE;
        }

        public String getPOLICY_NO() {
            return POLICY_NO;
        }

        public void setPOLICY_NO(String pOLICY_NO) {
            POLICY_NO = pOLICY_NO;
        }

        public String getPOLICY_HOLDER_NAME() {
            return POLICY_HOLDER_NAME;
        }

        public void setPOLICY_HOLDER_NAME(String pOLICY_HOLDER_NAME) {
            POLICY_HOLDER_NAME = pOLICY_HOLDER_NAME;
        }

        public String getPOL_START_DATE() {
            return POL_START_DATE;
        }

        public void setPOL_START_DATE(String pOL_START_DATE) {
            POL_START_DATE = pOL_START_DATE;
        }
    }

    public class XMLHolderGroupTermRenewalList {

        private String POLICY_NO = "", POLICY_HOLDER_NAME = "", POL_START_DATE = "";

        XMLHolderGroupTermRenewalList(String pOLICY_NO,
                                      String pOLICY_HOLDER_NAME, String pOL_START_DATE) {
            super();
            POLICY_NO = pOLICY_NO;
            POLICY_HOLDER_NAME = pOLICY_HOLDER_NAME;
            POL_START_DATE = pOL_START_DATE;
        }

        public String getPOLICY_NO() {
            return POLICY_NO;
        }

        public String getPOLICY_HOLDER_NAME() {
            return POLICY_HOLDER_NAME;
        }

        public String getPOL_START_DATE() {
            return POL_START_DATE;
        }
    }

    public class EFTPendingForm {
        private String POLICY_NO;
        private String FULLNAME;
        private String CASHIERINGDATE;

        EFTPendingForm(String POLICY_NO, String FULLNAME, String CASHIERINGDATE) {
            this.POLICY_NO = POLICY_NO;
            this.FULLNAME = FULLNAME;
            this.CASHIERINGDATE = CASHIERINGDATE;
        }

        public String getPOLICY_NO() {
            return POLICY_NO;
        }

        public String getFULLNAME() {
            return FULLNAME;
        }

        public String getCASHIERINGDATE() {
            return CASHIERINGDATE;
        }

    }

    public class EFTFormNotReceived {
        private String PROPOSAL_NO = "";
        private String TAT = "";
        private String BANK_BRANCH_NAME = "";
        private String CIF_CODE = "";
        private String CIF_NAME = "";
        private String CASHIERINGDATE = "";

        EFTFormNotReceived(String PROPOSAL_NO, String TAT, String BANK_BRANCH_NAME, String CIF_CODE, String CIF_NAME, String CASHIERINGDATE) {
            this.PROPOSAL_NO = PROPOSAL_NO;
            this.TAT = TAT;
            this.BANK_BRANCH_NAME = BANK_BRANCH_NAME;
            this.CIF_CODE = CIF_CODE;
            this.CIF_NAME = CIF_NAME;
            this.CASHIERINGDATE = CASHIERINGDATE;
        }

        public String getPROPOSAL_NO() {
            return PROPOSAL_NO;
        }

        public String getTAT() {
            return TAT;
        }

        public String getBANK_BRANCH_NAME() {
            return BANK_BRANCH_NAME;
        }

        public String getCIF_CODE() {
            return CIF_CODE;
        }

        public String getCIF_NAME() {
            return CIF_NAME;
        }

        public String getCASHIERINGDATE() {
            return CASHIERINGDATE;
        }
    }

    public class XMLHolderEkycDetails {
        private String str_policy_no = "", str_link_date = "", str_user_type;

        XMLHolderEkycDetails(String str_policy_no, String str_link_date, String str_user_type) {
            this.str_policy_no = str_policy_no;
            this.str_link_date = str_link_date;
            this.str_user_type = str_user_type;
        }

        public String getStr_user_type() {
            return str_user_type;
        }

        public void setStr_user_type(String str_user_type) {
            this.str_user_type = str_user_type;
        }

        public String getStr_policy_no() {
            return str_policy_no;
        }

        public void setStr_policy_no(String str_policy_no) {
            this.str_policy_no = str_policy_no;
        }

        public String getStr_link_date() {
            return str_link_date;
        }

        public void setStr_link_date(String str_link_date) {
            this.str_link_date = str_link_date;
        }
    }

    /*<Table> <EMPLOYEEID>16736</EMPLOYEEID>
	<EMPLOYEENAME>SHAFIQ UDDIN</EMPLOYEENAME>
	<POSITIONTYPE>BDM</POSITIONTYPE>
	<CONTACTMOBILE>8794720733</CONTACTMOBILE>
	<CONTACTEMAIL>shafiq.uddin@sbi-life.com</CONTACTEMAIL> </Table>*/
    public class ChannelUserReportsValuesModel {

        private String EMPLOYEEID;
        private String EMPLOYEENAME;
        private String POSITIONTYPE;
        private String CONTACTMOBILE;
        private String CONTACTEMAIL;

        ChannelUserReportsValuesModel(String EMPLOYEEID, String EMPLOYEENAME, String POSITIONTYPE, String CONTACTMOBILE, String CONTACTEMAIL) {
            this.EMPLOYEEID = EMPLOYEEID;
            this.EMPLOYEENAME = EMPLOYEENAME;
            this.POSITIONTYPE = POSITIONTYPE;
            this.CONTACTMOBILE = CONTACTMOBILE;
            this.CONTACTEMAIL = CONTACTEMAIL;
        }

        public String getCONTACTMOBILE() {
            return CONTACTMOBILE;
        }

        public String getCONTACTEMAIL() {
            return CONTACTEMAIL;
        }

        public String getEMPLOYEEID() {
            return EMPLOYEEID;
        }

        public String getEMPLOYEENAME() {
            return EMPLOYEENAME;
        }

        public String getPOSITIONTYPE() {
            return POSITIONTYPE;
        }


    }

    public class AOBPANPendingAgentListValuesModel {
        private String UM_CODE;
        private String PER_PAN_NO;
        private String PER_FULL_NAME;
        private String status;
        private String remark;
        private String enrollment_type;

        public AOBPANPendingAgentListValuesModel(String UM_CODE, String PER_PAN_NO, String PER_FULL_NAME,
                                                 String status, String remark, String enrollment_type) {
            this.UM_CODE = UM_CODE;
            this.PER_PAN_NO = PER_PAN_NO;
            this.PER_FULL_NAME = PER_FULL_NAME;
            this.status = status;
            this.remark = remark;
            this.enrollment_type = enrollment_type;
        }

        public String getEnrollment_type() {
            return enrollment_type;
        }

        public void setEnrollment_type(String enrollment_type) {
            this.enrollment_type = enrollment_type;
        }

        public String getPER_PAN_NO() {
            return PER_PAN_NO;
        }

        public String getPER_FULL_NAME() {
            return PER_FULL_NAME;
        }

        public String getUM_CODE() {
            return UM_CODE;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }

    public class AutoMandateStatusListValuesModel {
		/*<POLICY_NO>80006807</POLICY_NO>
		<MANDATE_NO>MA7081193707072017</MANDATE_NO>
		<MANDATE_TYPE>NACH</MANDATE_TYPE>
		<MANDATE_STATUS>READY FOR DISPATCH TO VENDOR</MANDATE_STATUS>*/

		/*<Table>
            <POLICY_NO>16005512109</POLICY_NO>
                <POL_HOLDER_NAME>ANURADHA  PATRA</POL_HOLDER_NAME>
            <MANDATE_TYPE>NACH</MANDATE_TYPE>
                <MANDATE_STATUS>In Process</MANDATE_STATUS>
            <STATUS_UPDATE_DATE>2017-07-25T11:55:20+05:30</STATUS_UPDATE_DATE>
            <POLICY_STATUS>Maturity</POLICY_STATUS>
           </Table>*/

        private String POLICY_NO;
        private String POL_HOLDER_NAME;
        private String MANDATE_TYPE;
        private String MANDATE_STATUS;
        private String STATUS_UPDATE_DATE;
        private String POLICY_STATUS;
        private String REJECTION_REASON;

        AutoMandateStatusListValuesModel(String POLICY_NO, String POL_HOLDER_NAME, String MANDATE_TYPE,
                                         String MANDATE_STATUS, String STATUS_UPDATE_DATE, String POLICY_STATUS,
                                         String REJECTION_REASON) {
            this.POLICY_NO = POLICY_NO;
            this.POL_HOLDER_NAME = POL_HOLDER_NAME;
            this.MANDATE_TYPE = MANDATE_TYPE;
            this.MANDATE_STATUS = MANDATE_STATUS;
            this.STATUS_UPDATE_DATE = STATUS_UPDATE_DATE;
            this.POLICY_STATUS = POLICY_STATUS;
            this.REJECTION_REASON = REJECTION_REASON;
        }

        public String getPOLICY_NO() {
            return POLICY_NO;
        }

        public String getPOL_HOLDER_NAME() {
            return POL_HOLDER_NAME;
        }

        public String getMANDATE_TYPE() {
            return MANDATE_TYPE;
        }

        public String getMANDATE_STATUS() {
            return MANDATE_STATUS;
        }

        public String getSTATUS_UPDATE_DATE() {
            return STATUS_UPDATE_DATE;
        }

        public String getPOLICY_STATUS() {
            return POLICY_STATUS;
        }

        public String getREJECTION_REASON() {
            return REJECTION_REASON;
        }
    }


    public class ProposalListValuesModel {
		/*<NewDataSet> <Table> <PROPOSAL_NO>2FYA006745</PROPOSAL_NO> <FULLNAME>Alia Bhat</FULLNAME>
		<STATUS>Proposition</STATUS> </Table>*/

        private String PROPOSAL_NO = "";
        private String FULLNAME = "";
        private String STATUS = "";
        private String CHANNELCODE = "";

        ProposalListValuesModel(String PROPOSAL_NO, String FULLNAME, String STATUS, String CHANNELCODE) {
            this.PROPOSAL_NO = PROPOSAL_NO;
            this.FULLNAME = FULLNAME;
            this.STATUS = STATUS;
            this.CHANNELCODE = CHANNELCODE;
        }

        public String getPROPOSAL_NO() {
            return PROPOSAL_NO;
        }

        public String getFULLNAME() {
            return FULLNAME;
        }

        public String getSTATUS() {
            return STATUS;
        }

        public String getCHANNELCODE() {
            return CHANNELCODE;
        }
    }

    public class MedicalPendingRequirementValuesModel {
		/*<PolicyDetails> <Table>
		<POLICYPROPOSALNUMBER>1BYA313715</POLICYPROPOSALNUMBER>
		<PAYMENTAMOUNT>55107</PAYMENTAMOUNT>
		<CASHIERINGDATE>06-08-2018</CASHIERINGDATE>
		<STATUS>MEDICAL</STATUS> <CHANNELCODE>990129126</CHANNELCODE> </Table> </PolicyDetails> */

        private String POLICYPROPOSALNUMBER;
        private String PAYMENTAMOUNT;
        private String CASHIERINGDATE;
        private String STATUS;
        private String CHANNELCODE;
        private String PENDINGSTATUS;

        MedicalPendingRequirementValuesModel(String POLICYPROPOSALNUMBER, String PAYMENTAMOUNT,
                                             String CASHIERINGDATE, String STATUS, String CHANNELCODE, String PENDINGSTATUS) {
            this.POLICYPROPOSALNUMBER = POLICYPROPOSALNUMBER;
            this.PAYMENTAMOUNT = PAYMENTAMOUNT;
            this.CASHIERINGDATE = CASHIERINGDATE;
            this.STATUS = STATUS;
            this.CHANNELCODE = CHANNELCODE;
            this.PENDINGSTATUS = PENDINGSTATUS;
        }

        public String getPOLICYPROPOSALNUMBER() {
            return POLICYPROPOSALNUMBER;
        }

        public String getPAYMENTAMOUNT() {
            return PAYMENTAMOUNT;
        }

        public String getCASHIERINGDATE() {
            return CASHIERINGDATE;
        }

        public String getSTATUS() {
            return STATUS;
        }

        public String getCHANNELCODE() {
            return CHANNELCODE;
        }

        public String getPENDINGSTATUS() {
            return PENDINGSTATUS;
        }
    }

    public class PIWCRinnStatusValuesModel {
		/*<ProposalDetail> <Table>
		<FORM_NO>7010458222</FORM_NO>
		<NAME>RADHA JITENDRAKUMAR PATEL</NAME>
		<STATUS>Temporary NC</STATUS>
		<SUB_STATUS>Ringng</SUB_STATUS>
		<PIWC_CALL_DATE>31-10-2017</PIWC_CALL_DATE>
		<PREM_AMT>879</PREM_AMT>
		<REC_ADD_DT>31-10-2017</REC_ADD_DT> </Table> </ProposalDetail>

		 */

        private String FORM_NO;
        private String NAME;
        private String STATUS;
        private String SUB_STATUS;
        private String PIWC_CALL_DATE;
        private String PREM_AMT;
        private String REC_ADD_DT;

        public PIWCRinnStatusValuesModel(String FORM_NO, String NAME, String STATUS, String SUB_STATUS, String PIWC_CALL_DATE, String PREM_AMT, String REC_ADD_DT) {
            this.FORM_NO = FORM_NO;
            this.NAME = NAME;
            this.STATUS = STATUS;
            this.SUB_STATUS = SUB_STATUS;
            this.PIWC_CALL_DATE = PIWC_CALL_DATE;
            this.PREM_AMT = PREM_AMT;
            this.REC_ADD_DT = REC_ADD_DT;
        }


        public String getFORM_NO() {
            return FORM_NO;
        }

        public String getNAME() {
            return NAME;
        }

        public String getSTATUS() {
            return STATUS;
        }

        public String getSUB_STATUS() {
            return SUB_STATUS;
        }

        public String getPIWC_CALL_DATE() {
            return PIWC_CALL_DATE;
        }

        public String getPREM_AMT() {
            return PREM_AMT;
        }

        public String getREC_ADD_DT() {
            return REC_ADD_DT;
        }
    }

    public class ProposalRinnStatusValuesModel {
		/*
         <ProposalDetail> <Table>
         <FORM_NO>7003543402</FORM_NO>
         <LAN>33875878430</LAN>
         <STATUS>Accept</STATUS>
         <LOAN_CATEG>RINN HL</LOAN_CATEG>
         <REQ_TYPE>Non-Medicals</REQ_TYPE>
         <NON_MED_DESC>Base premium Shortage :</NON_MED_DESC>
           <MED_DESC>
         <NON_MED_COMMENTS>123</NON_MED_COMMENTS>
          <BORROWER_TYPE>Individual</BORROWER_TYPE>
          <BANK_NAME>STATE BANK OF INDIA</BANK_NAME>
          <BRANCH_NAME>RASMECC CUM SARC</BRANCH_NAME>
         <POLICY_HOLDER_NAME>MANJUBEN DEVSHIBHAI BANDHIYA</POLICY_HOLDER_NAME>
         <MED_COMMENTS>
         <PIWC_STATUS>
         <PIWC_SUB_STATUS>
         </Table> </ProposalDetail>
		 */

        private String FORM_NO;
        private String LAN;
        private String STATUS;
        private String LOAN_CATEG;
        private String REQ_TYPE;
        private String NON_MED_DESC;
        private String MED_DESC;
        private String NON_MED_COMMENTS;
        private String BORROWER_TYPE;
        private String BANK_NAME;
        private String BRANCH_NAME;
        private String POLICY_HOLDER_NAME;
        private String MED_COMMENTS;
        private String PIWC_STATUS;
        private String PIWC_SUB_STATUS;
        private String EQ_STATUS_NON_MEDICAL;
        private String REQ_STATUS_MEDICAL;

        public ProposalRinnStatusValuesModel(String FORM_NO, String LAN, String STATUS, String LOAN_CATEG, String REQ_TYPE, String NON_MED_DESC, String MED_DESC, String NON_MED_COMMENTS, String BORROWER_TYPE, String BANK_NAME, String BRANCH_NAME, String POLICY_HOLDER_NAME, String MED_COMMENTS, String PIWC_STATUS, String PIWC_SUB_STATUS, String EQ_STATUS_NON_MEDICAL, String REQ_STATUS_MEDICAL) {
            this.FORM_NO = FORM_NO;
            this.LAN = LAN;
            this.STATUS = STATUS;
            this.LOAN_CATEG = LOAN_CATEG;
            this.REQ_TYPE = REQ_TYPE;
            this.NON_MED_DESC = NON_MED_DESC;
            this.MED_DESC = MED_DESC;
            this.NON_MED_COMMENTS = NON_MED_COMMENTS;
            this.BORROWER_TYPE = BORROWER_TYPE;
            this.BANK_NAME = BANK_NAME;
            this.BRANCH_NAME = BRANCH_NAME;
            this.POLICY_HOLDER_NAME = POLICY_HOLDER_NAME;
            this.MED_COMMENTS = MED_COMMENTS;
            this.PIWC_STATUS = PIWC_STATUS;
            this.PIWC_SUB_STATUS = PIWC_SUB_STATUS;
            this.EQ_STATUS_NON_MEDICAL = EQ_STATUS_NON_MEDICAL;
            this.REQ_STATUS_MEDICAL = REQ_STATUS_MEDICAL;
        }

        public String getFORM_NO() {
            return FORM_NO;
        }

        public String getLAN() {
            return LAN;
        }

        public String getSTATUS() {
            return STATUS;
        }

        public String getLOAN_CATEG() {
            return LOAN_CATEG;
        }

        public String getREQ_TYPE() {
            return REQ_TYPE;
        }

        public String getNON_MED_DESC() {
            return NON_MED_DESC;
        }

        public String getMED_DESC() {
            return MED_DESC;
        }

        public String getNON_MED_COMMENTS() {
            return NON_MED_COMMENTS;
        }

        public String getBORROWER_TYPE() {
            return BORROWER_TYPE;
        }

        public String getBANK_NAME() {
            return BANK_NAME;
        }

        public String getBRANCH_NAME() {
            return BRANCH_NAME;
        }

        public String getPOLICY_HOLDER_NAME() {
            return POLICY_HOLDER_NAME;
        }

        public String getMED_COMMENTS() {
            return MED_COMMENTS;
        }

        public String getPIWC_STATUS() {
            return PIWC_STATUS;
        }

        public String getPIWC_SUB_STATUS() {
            return PIWC_SUB_STATUS;
        }

        public String getEQ_STATUS_NON_MEDICAL() {
            return EQ_STATUS_NON_MEDICAL;
        }

        public String getREQ_STATUS_MEDICAL() {
            return REQ_STATUS_MEDICAL;
        }
    }

    public class AutoMandatePenetrationDetailsValuesModel {
        /*<CIFPolicyList> <Table> <POL_HOLDER_NAME>Tapan Roy</POL_HOLDER_NAME>
        <POLICYCURRENTSTATUS>Inforce</POLICYCURRENTSTATUS> <CONTACT_MOBILE>9832047239</CONTACT_MOBILE>
        <SEQ>1</SEQ> </Table> </CIFPolicyList>
        POLICY_NO
                MANDATE_TYPE
        MANDATE_STATUS
                REJECTION_REASON
        MANDATE_RCVD_DATE*/

        private String POL_HOLDER_NAME, POLICYCURRENTSTATUS, CONTACT_MOBILE, POLICY_NO, MANDATE_TYPE, MANDATE_STATUS, REJECTION_REASON, MANDATE_RCVD_DATE, SEQ;

        public AutoMandatePenetrationDetailsValuesModel(String POL_HOLDER_NAME, String POLICYCURRENTSTATUS, String CONTACT_MOBILE, String POLICY_NO, String MANDATE_TYPE, String MANDATE_STATUS, String REJECTION_REASON, String MANDATE_RCVD_DATE, String SEQ) {
            this.POL_HOLDER_NAME = POL_HOLDER_NAME;
            this.POLICYCURRENTSTATUS = POLICYCURRENTSTATUS;
            this.CONTACT_MOBILE = CONTACT_MOBILE;
            this.POLICY_NO = POLICY_NO;
            this.MANDATE_TYPE = MANDATE_TYPE;
            this.MANDATE_STATUS = MANDATE_STATUS;
            this.REJECTION_REASON = REJECTION_REASON;
            this.MANDATE_RCVD_DATE = MANDATE_RCVD_DATE;
            this.SEQ = SEQ;
        }

        public String getPOL_HOLDER_NAME() {
            return POL_HOLDER_NAME;
        }

        public String getPOLICYCURRENTSTATUS() {
            return POLICYCURRENTSTATUS;
        }

        public String getCONTACT_MOBILE() {
            return CONTACT_MOBILE;
        }

        public String getPOLICY_NO() {
            return POLICY_NO;
        }

        public String getMANDATE_TYPE() {
            return MANDATE_TYPE;
        }

        public String getMANDATE_STATUS() {
            return MANDATE_STATUS;
        }

        public String getREJECTION_REASON() {
            return REJECTION_REASON;
        }

        public String getMANDATE_RCVD_DATE() {
            return MANDATE_RCVD_DATE;
        }

        public String getSEQ() {
            return SEQ;
        }
    }

    public class RevivalCampaignValuesModel {
		/*
         <NewDataSet> <Table> <POLICYNUMBER>1K030267204</POLICYNUMBER> <NAME>Srinivas Asampalli</NAME> </Table>
         <Table> <POLICYNUMBER>1K033117007</POLICYNUMBER> <NAME>Bhaskar Kattekola</NAME> </Table>
         <Table> <POLICYNUMBER>1K050080607</POLICYNUMBER> <NAME>Ramesh Reddy Baddam</NAME> </Table>
         <Table> <POLICYNUMBER>27016919806</POLICYNUMBER> <NAME>Macharla Venkata Ramana</NAME> </Table>
          <Table> <POLICYNUMBER>2D001925510</POLICYNUMBER> <NAME>Gurram Sudhakareddy</NAME> </Table>
         <Table> <POLICYNUMBER>35101629202</POLICYNUMBER> <NAME>Nagender Podduturi</NAME> </Table> </NewDataSet>
		 */

        private String POLICYNUMBER;
        private String NAME;
        private String MOBILE;
        private String EMAIL;

        public RevivalCampaignValuesModel(String POLICYNUMBER, String NAME, String MOBILE, String EMAIL) {
            this.POLICYNUMBER = POLICYNUMBER;
            this.NAME = NAME;
            this.MOBILE = MOBILE;
            this.EMAIL = EMAIL;
        }

        public String getPOLICYNUMBER() {
            return POLICYNUMBER;
        }

        public String getNAME() {
            return NAME;
        }

        public String getMOBILE() {
            return MOBILE;
        }

        public String getEMAIL() {
            return EMAIL;
        }
    }

    public class XMLQuotationSchedule {
        private String if_paid_by = "",
                latest_due = "",
                no_of_premium = "",
                total_amount = "",
                rate_of_interest = "",
                late_fee = "",
                interest_waived = "",
                other_dues_pending = "",
                policy_deposit = "",
                net_revival_amount = "";

        public XMLQuotationSchedule(String if_paid_by, String latest_due, String no_of_premium, String total_amount, String rate_of_interest, String late_fee, String interest_waived, String other_dues_pending, String policy_deposit, String net_revival_amount) {
            this.if_paid_by = if_paid_by;
            this.latest_due = latest_due;
            this.no_of_premium = no_of_premium;
            this.total_amount = total_amount;
            this.rate_of_interest = rate_of_interest;
            this.late_fee = late_fee;
            this.interest_waived = interest_waived;
            this.other_dues_pending = other_dues_pending;
            this.policy_deposit = policy_deposit;
            this.net_revival_amount = net_revival_amount;
        }

        public String getIf_paid_by() {
            return if_paid_by;
        }

        public String getLatest_due() {
            return latest_due;
        }

        public String getNo_of_premium() {
            return no_of_premium;
        }

        public String getTotal_amount() {
            return total_amount;
        }

        public String getRate_of_interest() {
            return rate_of_interest;
        }

        public String getLate_fee() {
            return late_fee;
        }

        public String getInterest_waived() {
            return interest_waived;
        }

        public String getOther_dues_pending() {
            return other_dues_pending;
        }

        public String getPolicy_deposit() {
            return policy_deposit;
        }

        public String getNet_revival_amount() {
            return net_revival_amount;
        }
    }

    public class AlternateModeCollectionStatusValuesModel {
      /*  <NewDataSet>
            <Table>
                <TDF_POLICY_NUMBER>18001545008</TDF_POLICY_NUMBER>
                <HOLDER_NAME> Sen Raj B C</HOLDER_NAME>
                <TDF_DUE_AMOUNT>25869</TDF_DUE_AMOUNT>
                <TDF_DUE_DATE>19-SEP-2018</TDF_DUE_DATE>
                <TDF_DATE_SENT>20-SEP-2018</TDF_DATE_SENT>
                <TDF_STATUS>FAILED</TDF_STATUS>
                <TDF_FAILURE_REASON> ACCOUNT CLOSED</TDF_FAILURE_REASON>
            </Table>
        </NewDataSet>*/

        private String TDF_POLICY_NUMBER, HOLDER_NAME, TDF_DUE_AMOUNT, TDF_DUE_DATE,
                TDF_DATE_SENT, TDF_STATUS, TDF_FAILURE_REASON, TDF_DEBIT_DATE, PAYMENT_MECHANISM;

        public AlternateModeCollectionStatusValuesModel(String TDF_POLICY_NUMBER, String HOLDER_NAME, String TDF_DUE_AMOUNT, String TDF_DUE_DATE, String TDF_DATE_SENT, String TDF_STATUS, String TDF_FAILURE_REASON, String TDF_DEBIT_DATE, String PAYMENT_MECHANISM) {
            this.TDF_POLICY_NUMBER = TDF_POLICY_NUMBER;
            this.HOLDER_NAME = HOLDER_NAME;
            this.TDF_DUE_AMOUNT = TDF_DUE_AMOUNT;
            this.TDF_DUE_DATE = TDF_DUE_DATE;
            this.TDF_DATE_SENT = TDF_DATE_SENT;
            this.TDF_STATUS = TDF_STATUS;
            this.TDF_FAILURE_REASON = TDF_FAILURE_REASON;
            this.TDF_DEBIT_DATE = TDF_DEBIT_DATE;
            this.PAYMENT_MECHANISM = PAYMENT_MECHANISM;
        }

        public String getTDF_POLICY_NUMBER() {
            return TDF_POLICY_NUMBER;
        }

        public String getHOLDER_NAME() {
            return HOLDER_NAME;
        }

        public String getTDF_DUE_AMOUNT() {
            return TDF_DUE_AMOUNT;
        }

        public String getTDF_DUE_DATE() {
            return TDF_DUE_DATE;
        }

        public String getTDF_DATE_SENT() {
            return TDF_DATE_SENT;
        }

        public String getTDF_STATUS() {
            return TDF_STATUS;
        }

        public String getTDF_FAILURE_REASON() {
            return TDF_FAILURE_REASON;
        }

        public String getTDF_DEBIT_DATE() {
            return TDF_DEBIT_DATE;
        }

        public String getPAYMENT_MECHANISM() {
            return PAYMENT_MECHANISM;
        }
    }

    public class ViewMedicalStatusvaluesModel {

      /*
               <Table>
            <PROPOSAL_NO>7010504222</PROPOSAL_NO>
            <CUSTOMER_NAME>Fuleshwar?? Singh</CUSTOMER_NAME>
            <TPA_NAME>UHCI</TPA_NAME>
            <INTIMATION_DATE>2-12-0018</INTIMATION_DATE>
            <MEDICAL_DONE_DATE>09-12-0018</MEDICAL_DONE_DATE>
                <TEST_NAME>Category A,Category B,Categoechnician,MER,</TEST_NAME>
            <MAIN_STATUS>Close </MAIN_STATUS>
            <SUB_STATUS>Closed</SUB_STATUS>
        </Table>
        <Table>
            <PROPOSAL_NO>7010504222</PROPOSAL_NO>
            <CUSTOMER_NAME>Fuleshwar?? Singh</CUSTOMER_NAME>
            <TPA_NAME>UHCI</TPA_NAME>
            <INTIMATION_DATE>2-12-0018</INTIMATION_DATE>
            <MEDICAL_DONE_DATE>09-12-0018</MEDICAL_DONE_DATE>
            <TEST_NAME>Category A,Catchnician,MER,</TEST_NAME>
            <MAIN_STATUS>Close </MAIN_STATUS>
            <SUB_STATUS>Closed</SUB_STATUS>
        </Table>
        */

        private String PROPOSAL_NO, CUSTOMER_NAME, TPA_NAME, INTIMATION_DATE,
                MEDICAL_DONE_DATE, TEST_NAME, MAIN_STATUS, SUB_STATUS;

        public ViewMedicalStatusvaluesModel(String PROPOSAL_NO, String CUSTOMER_NAME, String TPA_NAME, String INTIMATION_DATE, String MEDICAL_DONE_DATE, String TEST_NAME, String MAIN_STATUS, String SUB_STATUS) {
            this.PROPOSAL_NO = PROPOSAL_NO;
            this.CUSTOMER_NAME = CUSTOMER_NAME;
            this.TPA_NAME = TPA_NAME;
            this.INTIMATION_DATE = INTIMATION_DATE;
            this.MEDICAL_DONE_DATE = MEDICAL_DONE_DATE;
            this.TEST_NAME = TEST_NAME;
            this.MAIN_STATUS = MAIN_STATUS;
            this.SUB_STATUS = SUB_STATUS;
        }

        public String getPROPOSAL_NO() {
            return PROPOSAL_NO;
        }

        public String getCUSTOMER_NAME() {
            return CUSTOMER_NAME;
        }

        public String getTPA_NAME() {
            return TPA_NAME;
        }

        public String getINTIMATION_DATE() {
            return INTIMATION_DATE;
        }

        public String getMEDICAL_DONE_DATE() {
            return MEDICAL_DONE_DATE;
        }

        public String getTEST_NAME() {
            return TEST_NAME;
        }

        public String getMAIN_STATUS() {
            return MAIN_STATUS;
        }

        public String getSUB_STATUS() {
            return SUB_STATUS;
        }
    }

    public class SPListValueModel {
        /*<CIFPolicyList> <Table> <CHANNELTECHNICALID>990852220</CHANNELTECHNICALID> <UM_BANKNAME>Stock Holding Corporation Of India Limited</UM_BANKNAME> <BANKNAME>Sriganganagar</BANKNAME> </Table> <Table> </CIFPolicyList>*/

        private String strChannelID = "", strUMBankName = "", strBankName = "";

        public SPListValueModel(String strChannelID, String strUMBankName, String strBankName) {
            this.strChannelID = strChannelID;
            this.strUMBankName = strUMBankName;
            this.strBankName = strBankName;
        }

        public String getStrChannelID() {
            return strChannelID;
        }

        public void setStrChannelID(String strChannelID) {
            this.strChannelID = strChannelID;
        }

        public String getStrUMBankName() {
            return strUMBankName;
        }

        public void setStrUMBankName(String strUMBankName) {
            this.strUMBankName = strUMBankName;
        }

        public String getStrBankName() {
            return strBankName;
        }

        public void setStrBankName(String strBankName) {
            this.strBankName = strBankName;
        }
    }

    public class BranchDetailsForBDMValuesModel {
        /*<Table> <BANKBRANCHCODE>489</BANKBRANCHCODE>
        <BANKBRANCHNAME>Thane</BANKBRANCHNAME> </Table>*/
        private String BANKBRANCHCODE, BANKBRANCHNAME;

        public BranchDetailsForBDMValuesModel(String BANKBRANCHCODE, String BANKBRANCHNAME) {
            this.BANKBRANCHCODE = BANKBRANCHCODE;
            this.BANKBRANCHNAME = BANKBRANCHNAME;
        }

        public String getBANKBRANCHCODE() {
            return BANKBRANCHCODE;
        }

        public String getBANKBRANCHNAME() {
            return BANKBRANCHNAME;
        }
    }

    public class RevivalCampaignPoliciesValuesModel {
        /*<NewDataSet> <Table> <POLICY_NO>1K029075003</POLICY_NO>
        <HOLDERNAME>Abdul Sattar Mohammed Shaikh</HOLDERNAME>
        <PRODUCTNAME>Sbi Life Smart Wealth Builder Lp</PRODUCTNAME>
        <POLICYSUMASSURED>990000</POLICYSUMASSURED>
        <RAG_FLAG>Likely</RAG_FLAG>
        <DGH_REQUIREMENT>DGH Required - Medical Waived</DGH_REQUIREMENT>
        <NET_REVIVAL_START>198000</NET_REVIVAL_START>
        <NET_REVIVAL_LAST>198000</NET_REVIVAL_LAST> </Table></NewDataSet>*/

        private String POLICY_NO, HOLDERNAME, PRODUCTNAME, POLICYSUMASSURED, RAG_FLAG, DGH_REQUIREMENT, NET_REVIVAL_START, NET_REVIVAL_LAST;

        public RevivalCampaignPoliciesValuesModel(String POLICY_NO, String HOLDERNAME, String PRODUCTNAME, String POLICYSUMASSURED, String RAG_FLAG, String DGH_REQUIREMENT, String NET_REVIVAL_START, String NET_REVIVAL_LAST) {
            this.POLICY_NO = POLICY_NO;
            this.HOLDERNAME = HOLDERNAME;
            this.PRODUCTNAME = PRODUCTNAME;
            this.POLICYSUMASSURED = POLICYSUMASSURED;
            this.RAG_FLAG = RAG_FLAG;
            this.DGH_REQUIREMENT = DGH_REQUIREMENT;
            this.NET_REVIVAL_START = NET_REVIVAL_START;
            this.NET_REVIVAL_LAST = NET_REVIVAL_LAST;
        }

        public String getPOLICY_NO() {
            return POLICY_NO;
        }

        public String getHOLDERNAME() {
            return HOLDERNAME;
        }

        public String getPRODUCTNAME() {
            return PRODUCTNAME;
        }

        public String getPOLICYSUMASSURED() {
            return POLICYSUMASSURED;
        }

        public String getRAG_FLAG() {
            return RAG_FLAG;
        }

        public String getDGH_REQUIREMENT() {
            return DGH_REQUIREMENT;
        }

        public String getNET_REVIVAL_START() {
            return NET_REVIVAL_START;
        }

        public String getNET_REVIVAL_LAST() {
            return NET_REVIVAL_LAST;
        }
    }

    public class ClaimRequirementInfoValuesModel {
        /*<Table>
        <CLAIM_TYPE>Individual Annuity</CLAIM_TYPE>
                <AMOUNT>8271</AMOUNT>
                <POLICY_NO>22001842006</POLICY_NO>
        <LA>Mr Tukaram Punjaji Lahane</LA> </Table>
                <Table>*/


        private String CLAIM_TYPE, AMOUNT, POLICY_NO, LA;

        public ClaimRequirementInfoValuesModel(String CLAIM_TYPE, String AMOUNT, String POLICY_NO, String LA) {
            this.CLAIM_TYPE = CLAIM_TYPE;
            this.AMOUNT = AMOUNT;
            this.POLICY_NO = POLICY_NO;
            this.LA = LA;
        }

        public String getCLAIM_TYPE() {
            return CLAIM_TYPE;
        }

        public String getAMOUNT() {
            return AMOUNT;
        }

        public String getPOLICY_NO() {
            return POLICY_NO;
        }

        public String getLA() {
            return LA;
        }
    }

    public class PolicyDispathStatusValuesModel {

        /* <AllDispatchValues>
                <REPORT_TYPE>NB</REPORT_TYPE>
                <DOC_TYPE>Policy Doc</DOC_TYPE>
                <PROPOSALNO>2BAM152966</PROPOSALNO>
                <POLICYNO>1P669436206</POLICYNO>
                <DISPATCH_TO>CUSTOMER</DISPATCH_TO>
                <DISPATCH_THROUGH>EMS SPEED POST</DISPATCH_THROUGH>
                <AWBNO>EA028170185IN</AWBNO>
                <DESP_DATE>19/07/2016</DESP_DATE>
                <CHEQUENO>fgdf</CHEQUENO>
                <CHEQUEDATE>13/08/2016</CHEQUEDATE>
                <CHEQUEAMOUNT>343243</CHEQUEAMOUNT>
                <STATUS>y</STATUS>
            </AllDispatchValues>
            <AllDispatchValues>
                <REPORT_TYPE>NB</REPORT_TYPE>
                <DOC_TYPE>Policy Doc</DOC_TYPE>
                <PROPOSALNO>2BAM152966</PROPOSALNO>
                <POLICYNO>1P669436206</POLICYNO>
                <DISPATCH_TO>CUSTOMER</DISPATCH_TO>
                <DISPATCH_THROUGH>EMS SPEED POST</DISPATCH_THROUGH>
                <AWBNO>EA028170185IN</AWBNO>
                <DESP_DATE>19/07/2016</DESP_DATE>
            </AllDispatchValues>*/

        String REPORT_TYPE, DOC_TYPE, PROPOSALNO, POLICYNO, DISPATCH_TO, DISPATCH_THROUGH, AWBNO,
                DESP_DATE, CHEQUENO, CHEQUEDATE, CHEQUEAMOUNT, STATUS;
        Date date;

        public PolicyDispathStatusValuesModel(String REPORT_TYPE, String DOC_TYPE, String PROPOSALNO, String POLICYNO,
                                              String DISPATCH_TO, String DISPATCH_THROUGH, String AWBNO,
                                              String DESP_DATE, String CHEQUENO, String CHEQUEDATE, String CHEQUEAMOUNT,
                                              String STATUS, Date date) {
            this.REPORT_TYPE = REPORT_TYPE;
            this.DOC_TYPE = DOC_TYPE;
            this.PROPOSALNO = PROPOSALNO;
            this.POLICYNO = POLICYNO;
            this.DISPATCH_TO = DISPATCH_TO;
            this.DISPATCH_THROUGH = DISPATCH_THROUGH;
            this.AWBNO = AWBNO;
            this.DESP_DATE = DESP_DATE;
            this.CHEQUENO = CHEQUENO;
            this.CHEQUEDATE = CHEQUEDATE;
            this.CHEQUEAMOUNT = CHEQUEAMOUNT;
            this.STATUS = STATUS;
            this.date = date;
        }

        public String getREPORT_TYPE() {
            return REPORT_TYPE;
        }

        public String getDOC_TYPE() {
            return DOC_TYPE;
        }

        public String getPROPOSALNO() {
            return PROPOSALNO;
        }

        public String getPOLICYNO() {
            return POLICYNO;
        }

        public String getDISPATCH_TO() {
            return DISPATCH_TO;
        }

        public String getDISPATCH_THROUGH() {
            return DISPATCH_THROUGH;
        }

        public String getAWBNO() {
            return AWBNO;
        }

        public String getDESP_DATE() {
            return DESP_DATE;
        }

        public String getCHEQUENO() {
            return CHEQUENO;
        }

        public String getCHEQUEDATE() {
            return CHEQUEDATE;
        }

        public String getCHEQUEAMOUNT() {
            return CHEQUEAMOUNT;
        }

        public String getSTATUS() {
            return STATUS;
        }

        public Date getDate() {
            return date;
        }
    }

    public class Pojo_POSP_RA_Rejection {
        private String str_req_pan = "", //PAN
                str_req_ia_code = "",//IACODE
                str_req_raised = "", //REQUIREMENT_RAISED
                str_req_raised_remark = "", // REMARKS
                str_req_raised_document_status = "",// DOCUMENT_STATUS
                str_req_raised_status = "",//REQUIREMENT_STATUS
                str_req_raised_document_name = "",//DOCUMENT_NAME
                str_req_raised_document_option_value = "",//DOCUMENT_OPTION_VALUE
                str_req_raised_enrollment_type = "";

        public Pojo_POSP_RA_Rejection(String str_req_pan, String str_req_ia_code, String str_req_raised,
                                      String str_req_raised_remark, String str_req_raised_document_status,
                                      String str_req_raised_status, String str_req_raised_document_name,
                                      String str_req_raised_document_option_value,
                                      String str_req_raised_enrollment_type) {
            this.str_req_pan = str_req_pan;
            this.str_req_ia_code = str_req_ia_code;
            this.str_req_raised = str_req_raised;
            this.str_req_raised_remark = str_req_raised_remark;
            this.str_req_raised_document_status = str_req_raised_document_status;
            this.str_req_raised_status = str_req_raised_status;
            this.str_req_raised_document_name = str_req_raised_document_name;
            this.str_req_raised_document_option_value = str_req_raised_document_option_value;
            this.str_req_raised_enrollment_type = str_req_raised_enrollment_type;
        }

        public String getStr_req_raised_enrollment_type() {
            return str_req_raised_enrollment_type;
        }

        public void setStr_req_raised_enrollment_type(String str_req_raised_enrollment_type) {
            this.str_req_raised_enrollment_type = str_req_raised_enrollment_type;
        }

        public String getStr_req_raised_document_option_value() {
            return str_req_raised_document_option_value;
        }

        public void setStr_req_raised_document_option_value(String str_req_raised_document_option_value) {
            this.str_req_raised_document_option_value = str_req_raised_document_option_value;
        }

        public String getStr_req_pan() {
            return str_req_pan;
        }

        public void setStr_req_pan(String str_req_pan) {
            this.str_req_pan = str_req_pan;
        }

        public String getStr_req_ia_code() {
            return str_req_ia_code;
        }

        public void setStr_req_ia_code(String str_req_ia_code) {
            this.str_req_ia_code = str_req_ia_code;
        }

        public String getStr_req_raised() {
            return str_req_raised;
        }

        public void setStr_req_raised(String str_req_raised) {
            this.str_req_raised = str_req_raised;
        }

        public String getStr_req_raised_remark() {
            return str_req_raised_remark;
        }

        public void setStr_req_raised_remark(String str_req_raised_remark) {
            this.str_req_raised_remark = str_req_raised_remark;
        }

        public String getStr_req_raised_document_status() {
            return str_req_raised_document_status;
        }

        public void setStr_req_raised_document_status(String str_req_raised_document_status) {
            this.str_req_raised_document_status = str_req_raised_document_status;
        }

        public String getStr_req_raised_status() {
            return str_req_raised_status;
        }

        public void setStr_req_raised_status(String str_req_raised_status) {
            this.str_req_raised_status = str_req_raised_status;
        }

        public String getStr_req_raised_document_name() {
            return str_req_raised_document_name;
        }

        public void setStr_req_raised_document_name(String str_req_raised_document_name) {
            this.str_req_raised_document_name = str_req_raised_document_name;
        }
    }

    public class PojoPOSPRAStatus {
        private String strPAN = "", strIACode = "", strStatus = "";

        public PojoPOSPRAStatus(String strPAN, String strIACode, String strStatus) {
            this.strPAN = strPAN;
            this.strIACode = strIACode;
            this.strStatus = strStatus;
        }

        public String getStrPAN() {
            return strPAN;
        }

        public void setStrPAN(String strPAN) {
            this.strPAN = strPAN;
        }

        public String getStrIACode() {
            return strIACode;
        }

        public void setStrIACode(String strIACode) {
            this.strIACode = strIACode;
        }

        public String getStrStatus() {
            return strStatus;
        }

        public void setStrStatus(String strStatus) {
            this.strStatus = strStatus;
        }
    }

    public class PojoAOBAgentDetails {
        private String strIACode, strIAName, strUMCode_code, strUmCode, strUMName, strStatus,
                strVarible1, strVarible2, strVarible3, strVarible4;

        public PojoAOBAgentDetails(String strIACode, String strIAName, String strUMCode_code,
                                   String strUmCode, String strUMName, String strStatus, String strVarible1,
                                   String strVarible2, String strVarible3, String strVarible4) {
            this.strIACode = strIACode;
            this.strIAName = strIAName;
            this.strUMCode_code = strUMCode_code;
            this.strUmCode = strUmCode;
            this.strUMName = strUMName;
            this.strStatus = strStatus;

            //common varibles in ActivityAOBAgentDetails.java and ActivityAOBAgentReqDoc.java
            this.strVarible1 = strVarible1;
            this.strVarible2 = strVarible2;
            this.strVarible3 = strVarible3;
            this.strVarible4 = strVarible4;
        }

        public String getStrVarible4() {
            return strVarible4;
        }

        public void setStrVarible4(String strVarible4) {
            this.strVarible4 = strVarible4;
        }

        public String getStrVarible1() {
            return strVarible1;
        }

        public void setStrVarible1(String strVarible1) {
            this.strVarible1 = strVarible1;
        }

        public String getStrVarible2() {
            return strVarible2;
        }

        public void setStrVarible2(String strVarible2) {
            this.strVarible2 = strVarible2;
        }

        public String getStrVarible3() {
            return strVarible3;
        }

        public void setStrVarible3(String strVarible3) {
            this.strVarible3 = strVarible3;
        }

        public String getStrIACode() {
            return strIACode;
        }

        public void setStrIACode(String strIACode) {
            this.strIACode = strIACode;
        }

        public String getStrIAName() {
            return strIAName;
        }

        public void setStrIAName(String strIAName) {
            this.strIAName = strIAName;
        }

        public String getStrUMCode_code() {
            return strUMCode_code;
        }

        public void setStrUMCode_code(String strUMCode_code) {
            this.strUMCode_code = strUMCode_code;
        }

        public String getStrUmCode() {
            return strUmCode;
        }

        public void setStrUmCode(String strUmCode) {
            this.strUmCode = strUmCode;
        }

        public String getStrUMName() {
            return strUMName;
        }

        public void setStrUMName(String strUMName) {
            this.strUMName = strUMName;
        }

        public String getStrStatus() {
            return strStatus;
        }

        public void setStrStatus(String strStatus) {
            this.strStatus = strStatus;
        }
    }

    public class PIWCAudioCallingValuesModel {
        String PL_PROP_NUM, PR_FULL_NM, PL_CALL_DT, REMARKS, PROPOSER_MOBILE, VD_VERIF, VD_CALL_FLAG;

        public PIWCAudioCallingValuesModel(String PL_PROP_NUM, String PR_FULL_NM, String PL_CALL_DT, String REMARKS,
                                           String PROPOSER_MOBILE, String VD_VERIF, String VD_CALL_FLAG) {
            this.PL_PROP_NUM = PL_PROP_NUM;
            this.PR_FULL_NM = PR_FULL_NM;
            this.PL_CALL_DT = PL_CALL_DT;
            this.REMARKS = REMARKS;
            this.PROPOSER_MOBILE = PROPOSER_MOBILE;
            this.VD_VERIF = VD_VERIF;
            this.VD_CALL_FLAG = VD_CALL_FLAG;
        }

        public String getPL_PROP_NUM() {
            return PL_PROP_NUM;
        }

        public String getPR_FULL_NM() {
            return PR_FULL_NM;
        }

        public String getPL_CALL_DT() {
            return PL_CALL_DT;
        }

        public String getREMARKS() {
            return REMARKS;
        }

        public String getPROPOSER_MOBILE() {
            return PROPOSER_MOBILE;
        }

        public String getVD_VERIF() {
            return VD_VERIF;
        }

        public String getVD_CALL_FLAG() {
            return VD_CALL_FLAG;
        }
    }

    public class LMBusinessStatusValuesModel {
        private String IACODE, COT_TOT_STATUS, IANAME, UMCODE, UMNAME,
                BRANCHCODE, BRANCHNAME, DIVISION, AREA, REGION,
                DOA, ZONE, RATED_YTD, NOP, NOP_CONNECT_LIFE,
                RENEWAL_COLLECTED, PERSISTENCY, CLUBSTATUS,
                WEIGHTED_PREMIUM, EXTRACTION_DATE, NBPRANKING;

        public LMBusinessStatusValuesModel(String IACODE, String COT_TOT_STATUS, String IANAME, String UMCODE, String UMNAME, String BRANCHCODE, String BRANCHNAME, String DIVISION, String AREA, String REGION, String DOA, String ZONE, String RATED_YTD, String NOP, String NOP_CONNECT_LIFE, String RENEWAL_COLLECTED, String PERSISTENCY, String CLUBSTATUS, String WEIGHTED_PREMIUM, String EXTRACTION_DATE, String NBPRANKING) {
            this.IACODE = IACODE;
            this.COT_TOT_STATUS = COT_TOT_STATUS;
            this.IANAME = IANAME;
            this.UMCODE = UMCODE;
            this.UMNAME = UMNAME;
            this.BRANCHCODE = BRANCHCODE;
            this.BRANCHNAME = BRANCHNAME;
            this.DIVISION = DIVISION;
            this.AREA = AREA;
            this.REGION = REGION;
            this.DOA = DOA;
            this.ZONE = ZONE;
            this.RATED_YTD = RATED_YTD;
            this.NOP = NOP;
            this.NOP_CONNECT_LIFE = NOP_CONNECT_LIFE;
            this.RENEWAL_COLLECTED = RENEWAL_COLLECTED;
            this.PERSISTENCY = PERSISTENCY;
            this.CLUBSTATUS = CLUBSTATUS;
            this.WEIGHTED_PREMIUM = WEIGHTED_PREMIUM;
            this.EXTRACTION_DATE = EXTRACTION_DATE;
            this.NBPRANKING = NBPRANKING;
        }

        public String getIACODE() {
            return IACODE;
        }

        public String getCOT_TOT_STATUS() {
            return COT_TOT_STATUS;
        }

        public String getIANAME() {
            return IANAME;
        }

        public String getUMCODE() {
            return UMCODE;
        }

        public String getUMNAME() {
            return UMNAME;
        }

        public String getBRANCHCODE() {
            return BRANCHCODE;
        }

        public String getBRANCHNAME() {
            return BRANCHNAME;
        }

        public String getDIVISION() {
            return DIVISION;
        }

        public String getAREA() {
            return AREA;
        }

        public String getREGION() {
            return REGION;
        }

        public String getDOA() {
            return DOA;
        }

        public String getZONE() {
            return ZONE;
        }

        public String getRATED_YTD() {
            return RATED_YTD;
        }

        public String getNOP() {
            return NOP;
        }

        public String getNOP_CONNECT_LIFE() {
            return NOP_CONNECT_LIFE;
        }

        public String getRENEWAL_COLLECTED() {
            return RENEWAL_COLLECTED;
        }

        public String getPERSISTENCY() {
            return PERSISTENCY;
        }

        public String getCLUBSTATUS() {
            return CLUBSTATUS;
        }

        public String getWEIGHTED_PREMIUM() {
            return WEIGHTED_PREMIUM;
        }

        public String getEXTRACTION_DATE() {
            return EXTRACTION_DATE;
        }

        public String getNBPRANKING() {
            return NBPRANKING;
        }
    }

    public class MHRReportValuesModel {
        /*

        <NewDataSet>
    <Table>
        <PROPOSALNUMBER>51NB900231</PROPOSALNUMBER>
        <NAME>PRANEETH KUMAR THOPRAM</NAME>
        <REGION>Telangana</REGION>
        <CHANNEL>Retail Agency</CHANNEL>
        <PCNAME>Hyderabad Pc</PCNAME>
        <SUC>0</SUC>
        <SUMASSURED>540000</SUMASSURED>
        <IANAME>GANGA PRASAD PALLYKONDA</IANAME>
        <IACODE>991165749</IACODE>
        <ACTIVATION_DATE>06-Dec-2019</ACTIVATION_DATE>
        <FRAUDULENT_FLAG>No</FRAUDULENT_FLAG>
        <CLUB_WITHOUT_PERSISTENCE>BRANCH MANAGER CLUB</CLUB_WITHOUT_PERSISTENCE>
        <CLUB_WITH_PERSISTENCE>BRANCH MANAGER CLUB</CLUB_WITH_PERSISTENCE>
    </Table>
</NewDataSet> */
        String PROPOSALNUMBER, NAME, REGION, CHANNEL, PCNAME, SUC, SUMASSURED, IANAME, IACODE, ACTIVATION_DATE, FRAUDULENT_FLAG,
                CLUB_WITHOUT_PERSISTENCE, CLUB_WITH_PERSISTENCE;
        int experience = 0;

        public MHRReportValuesModel(String PROPOSALNUMBER, String NAME, String REGION, String CHANNEL, String PCNAME, String SUC,
                                    String SUMASSURED, String IANAME, String IACODE, String ACTIVATION_DATE, String FRAUDULENT_FLAG,
                                    String CLUB_WITHOUT_PERSISTENCE, String CLUB_WITH_PERSISTENCE, int experience) {
            this.PROPOSALNUMBER = PROPOSALNUMBER;
            this.NAME = NAME;
            this.REGION = REGION;
            this.CHANNEL = CHANNEL;
            this.PCNAME = PCNAME;
            this.SUC = SUC;
            this.SUMASSURED = SUMASSURED;
            this.IANAME = IANAME;
            this.IACODE = IACODE;
            this.ACTIVATION_DATE = ACTIVATION_DATE;
            this.FRAUDULENT_FLAG = FRAUDULENT_FLAG;
            this.CLUB_WITHOUT_PERSISTENCE = CLUB_WITHOUT_PERSISTENCE;
            this.CLUB_WITH_PERSISTENCE = CLUB_WITH_PERSISTENCE;
            this.experience = experience;
        }

        public String getPROPOSALNUMBER() {
            return PROPOSALNUMBER;
        }

        public String getNAME() {
            return NAME;
        }

        public String getREGION() {
            return REGION;
        }

        public String getCHANNEL() {
            return CHANNEL;
        }

        public String getPCNAME() {
            return PCNAME;
        }

        public String getSUC() {
            return SUC;
        }

        public String getSUMASSURED() {
            return SUMASSURED;
        }

        public String getIANAME() {
            return IANAME;
        }

        public String getIACODE() {
            return IACODE;
        }

        public String getACTIVATION_DATE() {
            return ACTIVATION_DATE;
        }

        public String getFRAUDULENT_FLAG() {
            return FRAUDULENT_FLAG;
        }

        public String getCLUB_WITHOUT_PERSISTENCE() {
            return CLUB_WITHOUT_PERSISTENCE;
        }

        public String getCLUB_WITH_PERSISTENCE() {
            return CLUB_WITH_PERSISTENCE;
        }

        public int getExperience() {
            return experience;
        }
    }

    public class AgentPoliciesRenewalListMonthwiseGio implements Serializable {
        public String POLICYNUMBER, HOLDERID, HOLDERPERSONFIRSTNAME, HOLDERPERSONLASTNAME,
                POLICYCURRENTSTATUS, PREMIUMFUP, PREMIUMGROSSAMOUNT, PAYMENTTYPE, CONTACTMOBILE, PERMANENTADDRESS1,
                PERMANENTADDRESS2, PERMANENTADDRESS3, PERMANENTCITY, PERMANENTSTATE, PERMANENTPOSTCODE, RAG_FLAG,
                SURRENDER_FLAG, RESIDUAL_AMOUNT;
        public double POLICYLATITUDE, POLICYLONGITUDE, POLICYDISTANCE;
        public String POLICYRISKCOMMENCEMENTDATE, CONTACTRESIDENCE, CONTACTOFFICE, POLICYTYPE, POLICYPAYMENTMECHANISM;

        public AgentPoliciesRenewalListMonthwiseGio(String POLICYNUMBER, String HOLDERID, String HOLDERPERSONFIRSTNAME, String HOLDERPERSONLASTNAME,
                                                    String POLICYCURRENTSTATUS, String PREMIUMFUP, String PREMIUMGROSSAMOUNT, String PAYMENTTYPE,
                                                    String CONTACTMOBILE, String PERMANENTADDRESS1, String PERMANENTADDRESS2, String PERMANENTADDRESS3,
                                                    String PERMANENTCITY, String PERMANENTSTATE, String PERMANENTPOSTCODE,
                                                    double POLICYLATITUDE, double POLICYLONGITUDE, double POLICYDISTANCE,
                                                    String RAG_FLAG, String SURRENDER_FLAG, String RESIDUAL_AMOUNT,
                                                    String POLICYRISKCOMMENCEMENTDATE, String CONTACTRESIDENCE, String CONTACTOFFICE,
                                                    String POLICYTYPE, String POLICYPAYMENTMECHANISM) {
            this.POLICYNUMBER = POLICYNUMBER;
            this.HOLDERID = HOLDERID;
            this.HOLDERPERSONFIRSTNAME = HOLDERPERSONFIRSTNAME;
            this.HOLDERPERSONLASTNAME = HOLDERPERSONLASTNAME;
            this.POLICYCURRENTSTATUS = POLICYCURRENTSTATUS;
            this.PREMIUMFUP = PREMIUMFUP;
            this.PREMIUMGROSSAMOUNT = PREMIUMGROSSAMOUNT;
            this.PAYMENTTYPE = PAYMENTTYPE;
            this.CONTACTMOBILE = CONTACTMOBILE;
            this.PERMANENTADDRESS1 = PERMANENTADDRESS1;
            this.PERMANENTADDRESS2 = PERMANENTADDRESS2;
            this.PERMANENTADDRESS3 = PERMANENTADDRESS3;
            this.PERMANENTCITY = PERMANENTCITY;
            this.PERMANENTSTATE = PERMANENTSTATE;
            this.PERMANENTPOSTCODE = PERMANENTPOSTCODE;
            this.POLICYLATITUDE = POLICYLATITUDE;
            this.POLICYLONGITUDE = POLICYLONGITUDE;
            this.POLICYDISTANCE = POLICYDISTANCE;
            this.RAG_FLAG = RAG_FLAG;
            this.SURRENDER_FLAG = SURRENDER_FLAG;
            this.RESIDUAL_AMOUNT = RESIDUAL_AMOUNT;

            this.POLICYRISKCOMMENCEMENTDATE = POLICYRISKCOMMENCEMENTDATE;
            this.CONTACTRESIDENCE = CONTACTRESIDENCE;
            this.CONTACTOFFICE = CONTACTOFFICE;
            this.POLICYTYPE = POLICYTYPE;
            this.POLICYPAYMENTMECHANISM = POLICYPAYMENTMECHANISM;

        }

        public String getRAG_FLAG() {
            return RAG_FLAG;
        }


        public String getSURRENDER_FLAG() {
            return SURRENDER_FLAG;
        }


        public String getRESIDUAL_AMOUNT() {
            return RESIDUAL_AMOUNT;
        }


        public double getPOLICYDISTANCE() {
            return POLICYDISTANCE;
        }

        public void setPOLICYDISTANCE(double POLICYDISTANCE) {
            this.POLICYDISTANCE = POLICYDISTANCE;
        }

        public double getPOLICYLATITUDE() {
            return POLICYLATITUDE;
        }

        public void setPOLICYLATITUDE(double POLICYLATITUDE) {
            this.POLICYLATITUDE = POLICYLATITUDE;
        }

        public double getPOLICYLONGITUDE() {
            return POLICYLONGITUDE;
        }

        public void setPOLICYLONGITUDE(double POLICYLONGITUDE) {
            this.POLICYLONGITUDE = POLICYLONGITUDE;
        }

        public String getPOLICYNUMBER() {
            return POLICYNUMBER;
        }

        public void setPOLICYNUMBER(String POLICYNUMBER) {
            this.POLICYNUMBER = POLICYNUMBER;
        }

        public String getHOLDERID() {
            return HOLDERID;
        }

        public void setHOLDERID(String HOLDERID) {
            this.HOLDERID = HOLDERID;
        }

        public String getHOLDERPERSONFIRSTNAME() {
            return HOLDERPERSONFIRSTNAME;
        }

        public void setHOLDERPERSONFIRSTNAME(String HOLDERPERSONFIRSTNAME) {
            this.HOLDERPERSONFIRSTNAME = HOLDERPERSONFIRSTNAME;
        }

        public String getHOLDERPERSONLASTNAME() {
            return HOLDERPERSONLASTNAME;
        }

        public void setHOLDERPERSONLASTNAME(String HOLDERPERSONLASTNAME) {
            this.HOLDERPERSONLASTNAME = HOLDERPERSONLASTNAME;
        }

        public String getPOLICYCURRENTSTATUS() {
            return POLICYCURRENTSTATUS;
        }

        public void setPOLICYCURRENTSTATUS(String POLICYCURRENTSTATUS) {
            this.POLICYCURRENTSTATUS = POLICYCURRENTSTATUS;
        }

        public String getPREMIUMFUP() {
            return PREMIUMFUP;
        }

        public void setPREMIUMFUP(String PREMIUMFUP) {
            this.PREMIUMFUP = PREMIUMFUP;
        }

        public String getPREMIUMGROSSAMOUNT() {
            return PREMIUMGROSSAMOUNT;
        }

        public void setPREMIUMGROSSAMOUNT(String PREMIUMGROSSAMOUNT) {
            this.PREMIUMGROSSAMOUNT = PREMIUMGROSSAMOUNT;
        }

        public String getPAYMENTTYPE() {
            return PAYMENTTYPE;
        }

        public void setPAYMENTTYPE(String PAYMENTTYPE) {
            this.PAYMENTTYPE = PAYMENTTYPE;
        }

        public String getCONTACTMOBILE() {
            return CONTACTMOBILE;
        }

        public void setCONTACTMOBILE(String CONTACTMOBILE) {
            this.CONTACTMOBILE = CONTACTMOBILE;
        }

        public String getPERMANENTADDRESS1() {
            return PERMANENTADDRESS1;
        }

        public void setPERMANENTADDRESS1(String PERMANENTADDRESS1) {
            this.PERMANENTADDRESS1 = PERMANENTADDRESS1;
        }

        public String getPERMANENTADDRESS2() {
            return PERMANENTADDRESS2;
        }

        public void setPERMANENTADDRESS2(String PERMANENTADDRESS2) {
            this.PERMANENTADDRESS2 = PERMANENTADDRESS2;
        }

        public String getPERMANENTADDRESS3() {
            return PERMANENTADDRESS3;
        }

        public void setPERMANENTADDRESS3(String PERMANENTADDRESS3) {
            this.PERMANENTADDRESS3 = PERMANENTADDRESS3;
        }

        public String getPERMANENTCITY() {
            return PERMANENTCITY;
        }

        public void setPERMANENTCITY(String PERMANENTCITY) {
            this.PERMANENTCITY = PERMANENTCITY;
        }

        public String getPERMANENTSTATE() {
            return PERMANENTSTATE;
        }

        public void setPERMANENTSTATE(String PERMANENTSTATE) {
            this.PERMANENTSTATE = PERMANENTSTATE;
        }

        public String getPERMANENTPOSTCODE() {
            return PERMANENTPOSTCODE;
        }

        public void setPERMANENTPOSTCODE(String PERMANENTPOSTCODE) {
            this.PERMANENTPOSTCODE = PERMANENTPOSTCODE;
        }

        public String getPOLICYRISKCOMMENCEMENTDATE() {
            return POLICYRISKCOMMENCEMENTDATE;
        }

        public String getCONTACTRESIDENCE() {
            return CONTACTRESIDENCE;
        }

        public String getCONTACTOFFICE() {
            return CONTACTOFFICE;
        }

        public String getPOLICYTYPE() {
            return POLICYTYPE;
        }

        public String getPOLICYPAYMENTMECHANISM() {
            return POLICYPAYMENTMECHANISM;
        }
    }

    public class BranchAddress implements Serializable {
        public String BR_NAME, BR_ADD1, BR_ADD2, BR_ADD3, BR_PIN_CD,
                TEL_NO;
        public double BRANCHLATITUDE, BRANCHLONGITUDE, BRANCHDISTANCE;

        public BranchAddress(String BR_NAME, String BR_ADD1, String BR_ADD2, String BR_ADD3,
                             String BR_PIN_CD, String TEL_NO, double BRANCHLATITUDE, double BRANCHLONGITUDE, double BRANCHDISTANCE) {
            this.BR_NAME = BR_NAME;
            this.BR_ADD1 = BR_ADD1;
            this.BR_ADD2 = BR_ADD2;
            this.BR_ADD3 = BR_ADD3;
            this.BR_PIN_CD = BR_PIN_CD;
            this.TEL_NO = TEL_NO;
            this.BRANCHLATITUDE = BRANCHLATITUDE;
            this.BRANCHLONGITUDE = BRANCHLONGITUDE;
            this.BRANCHDISTANCE = BRANCHDISTANCE;

        }

        public String getBR_NAME() {
            return BR_NAME;
        }

        public void setBR_NAME(String BR_NAME) {
            this.BR_NAME = BR_NAME;
        }

        public String getBR_ADD1() {
            return BR_ADD1;
        }

        public void setBR_ADD1(String BR_ADD1) {
            this.BR_ADD1 = BR_ADD1;
        }

        public String getBR_ADD2() {
            return BR_ADD2;
        }

        public void setBR_ADD2(String BR_ADD2) {
            this.BR_ADD2 = BR_ADD2;
        }

        public String getBR_ADD3() {
            return BR_ADD3;
        }

        public void setBR_ADD3(String BR_ADD3) {
            this.BR_ADD3 = BR_ADD3;
        }

        public String getBR_PIN_CD() {
            return BR_PIN_CD;
        }

        public void setBR_PIN_CD(String BR_PIN_CD) {
            this.BR_PIN_CD = BR_PIN_CD;
        }

        public String getTEL_NO() {
            return TEL_NO;
        }

        public void setTEL_NO(String TEL_NO) {
            this.TEL_NO = TEL_NO;
        }

        public double getBRANCHLATITUDE() {
            return BRANCHLATITUDE;
        }

        public void setBRANCHLATITUDE(double BRANCHLATITUDE) {
            this.BRANCHLATITUDE = BRANCHLATITUDE;
        }

        public double getBRANCHLONGITUDE() {
            return BRANCHLONGITUDE;
        }

        public void setBRANCHLONGITUDE(double BRANCHLONGITUDE) {
            this.BRANCHLONGITUDE = BRANCHLONGITUDE;
        }

        public double getBRANCHDISTANCE() {
            return BRANCHDISTANCE;
        }

        public void setBRANCHDISTANCE(double BRANCHDISTANCE) {
            this.BRANCHDISTANCE = BRANCHDISTANCE;
        }


    }

    public class RenewalRemark implements Serializable {
        public String POLICYNO, STATUS, SUBSTATUS, REMARKS, CREATED_DATE;

        public RenewalRemark(String POLICYNO, String STATUS, String SUBSTATUS, String REMARKS, String CREATED_DATE) {
            this.POLICYNO = POLICYNO;
            this.STATUS = STATUS;
            this.SUBSTATUS = SUBSTATUS;
            this.REMARKS = REMARKS;
            this.CREATED_DATE = CREATED_DATE;
        }

        public String getPOLICYNO() {
            return POLICYNO;
        }

        public void setPOLICYNO(String POLICYNO) {
            this.POLICYNO = POLICYNO;
        }

        public String getSTATUS() {
            return STATUS;
        }

        public void setSTATUS(String STATUS) {
            this.STATUS = STATUS;
        }

        public String getSUBSTATUS() {
            return SUBSTATUS;
        }

        public void setSUBSTATUS(String SUBSTATUS) {
            this.SUBSTATUS = SUBSTATUS;
        }

        public String getREMARKS() {
            return REMARKS;
        }

        public void setREMARKS(String REMARKS) {
            this.REMARKS = REMARKS;
        }

        public String getCREATED_DATE() {
            return CREATED_DATE;
        }

        public void setCREATED_DATE(String CREATED_DATE) {
            this.CREATED_DATE = CREATED_DATE;
        }
    }

    public class RenewalListGioSummary implements Serializable {
        public String CITY, TOTAL_AMOUNT, TOTAL_COUNT;

        public RenewalListGioSummary(String CITY, String TOTAL_AMOUNT, String TOTAL_COUNT) {

            this.CITY = CITY;
            this.TOTAL_AMOUNT = TOTAL_AMOUNT;
            this.TOTAL_COUNT = TOTAL_COUNT;

        }

        public String getCITY() {
            return CITY;
        }

        public void setCITY(String CITY) {
            this.CITY = CITY;
        }

        public String getTOTAL_AMOUNT() {
            return TOTAL_AMOUNT;
        }

        public void setTOTAL_AMOUNT(String TOTAL_AMOUNT) {
            this.TOTAL_AMOUNT = TOTAL_AMOUNT;
        }

        public String getTOTAL_COUNT() {
            return TOTAL_COUNT;
        }

        public void setTOTAL_COUNT(String TOTAL_COUNT) {
            this.TOTAL_COUNT = TOTAL_COUNT;
        }
    }

    public class DigitalMHRValuesModel {
        public String PROPOSALNO, BITLYLINK;

        public DigitalMHRValuesModel(String PROPOSALNO, String BITLYLINK) {
            this.PROPOSALNO = PROPOSALNO;
            this.BITLYLINK = BITLYLINK;
        }

        public String getPROPOSALNO() {
            return PROPOSALNO;
        }

        public String getBITLYLINK() {
            return BITLYLINK;
        }
    }


    public class SendSMSAlternateProcessValuesModel {
        public String NBM_PROPOSAL_NO, CUST_NAME, CUST_MOBILE_NUMBER, IS_KYC;
        /*<NBM_PROPOSAL_NO>1ZYB245766</NBM_PROPOSAL_NO>
        <CUST_NAME>Ms. trupti gorde</CUST_NAME>
        <CUST_MOBILE_NUMBER>9920181465</CUST_MOBILE_NUMBER>
        <IS_KYC>N</IS_KYC> </Table>*/

        public SendSMSAlternateProcessValuesModel(String NBM_PROPOSAL_NO, String CUST_NAME, String CUST_MOBILE_NUMBER, String IS_KYC) {
            this.NBM_PROPOSAL_NO = NBM_PROPOSAL_NO;
            this.CUST_NAME = CUST_NAME;
            this.CUST_MOBILE_NUMBER = CUST_MOBILE_NUMBER;
            this.IS_KYC = IS_KYC;
        }

        public String getNBM_PROPOSAL_NO() {
            return NBM_PROPOSAL_NO;
        }

        public String getCUST_NAME() {
            return CUST_NAME;
        }

        public String getCUST_MOBILE_NUMBER() {
            return CUST_MOBILE_NUMBER;
        }

        public String getIS_KYC() {
            return IS_KYC;
        }
    }

    public class SendSMSLinkValuesModel {
        public String NAME, MOBILE, EMAILID;
        /*<NewDataSet> <Table>
        <NAME>YODHAN CHOUDHARY</NAME>
        <MOBILE>6205604886</MOBILE>
        <EMAILID>gg@gmail.com</EMAILID>
        </Table> </NewDataSet>*/

        public SendSMSLinkValuesModel(String NAME, String MOBILE, String EMAILID) {
            this.NAME = NAME;
            this.MOBILE = MOBILE;
            this.EMAILID = EMAILID;
        }

        public String getNAME() {
            return NAME;
        }

        public String getMOBILE() {
            return MOBILE;
        }

        public String getEMAILID() {
            return EMAILID;
        }
    }
    public class XMLFundSwitchHolder {

        private String POLICYNO;
        private String PRODUCTNAME;
        private String STATUS;
        private String FUP;
        private String FUNDVALUE;
        private String POLICYTYPE;
        private String MOBILE_NO;

        public XMLFundSwitchHolder(String POLICYNO, String PRODUCTNAME, String STATUS, String FUP, String FUNDVALUE, String POLICYTYPE, String MOBILE_NO) {
            this.POLICYNO = POLICYNO;
            this.PRODUCTNAME = PRODUCTNAME;
            this.STATUS = STATUS;
            this.FUP = FUP;
            this.FUNDVALUE = FUNDVALUE;
            this.POLICYTYPE = POLICYTYPE;
            this.MOBILE_NO = MOBILE_NO;
        }

        public String getMOBILE_NO() {
            return MOBILE_NO;
        }

        public void setMOBILE_NO(String MOBILE_NO) {
            this.MOBILE_NO = MOBILE_NO;
        }

        public String getPOLICYNO() {
            return POLICYNO;
        }

        public void setPOLICYNO(String POLICYNO) {
            this.POLICYNO = POLICYNO;
        }

        public String getPRODUCTNAME() {
            return PRODUCTNAME;
        }

        public void setPRODUCTNAME(String PRODUCTNAME) {
            this.PRODUCTNAME = PRODUCTNAME;
        }

        public String getSTATUS() {
            return STATUS;
        }

        public void setSTATUS(String STATUS) {
            this.STATUS = STATUS;
        }

        public String getFUP() {
            return FUP;
        }

        public void setFUP(String FUP) {
            this.FUP = FUP;
        }

        public String getFUNDVALUE() {
            return FUNDVALUE;
        }

        public void setFUNDVALUE(String FUNDVALUE) {
            this.FUNDVALUE = FUNDVALUE;
        }

        public String getPOLICYTYPE() {
            return POLICYTYPE;
        }

        public void setPOLICYTYPE(String POLICYTYPE) {
            this.POLICYTYPE = POLICYTYPE;
        }
    }

    public List<XMLFundSwitchHolder> parseNodeElementFundSwitch(List<String> lsNode) {

        List<XMLFundSwitchHolder> lsData = new ArrayList<XMLFundSwitchHolder>();
        ParseXML parseXML = new ParseXML();
        for (String Node : lsNode) {

            String POLICYTYPE = parseXML.parseXmlTag(Node, "POLICYTYPE");
            POLICYTYPE = POLICYTYPE == null ? "" : POLICYTYPE;

            if (POLICYTYPE.toUpperCase().contains("ULIP")) {

                String POLICYNO = parseXML.parseXmlTag(Node, "POLICYNO");

                String PRODUCTNAME = parseXML.parseXmlTag(Node, "PRODUCTNAME");
                String STATUS = parseXML.parseXmlTag(Node, "STATUS");
                String FUP = parseXML.parseXmlTag(Node, "FUP");
                String FUNDVALUE = parseXML.parseXmlTag(Node, "FUNDVALUE");
                String MOBILE_NO = parseXML.parseXmlTag(Node, "CONTACTMOBILE");

                XMLFundSwitchHolder nodeVal = new XMLFundSwitchHolder(POLICYNO, PRODUCTNAME, STATUS,
                        FUP, FUNDVALUE, POLICYTYPE, MOBILE_NO);

                lsData.add(nodeVal);
            }


        }

        return lsData;

    }


}




