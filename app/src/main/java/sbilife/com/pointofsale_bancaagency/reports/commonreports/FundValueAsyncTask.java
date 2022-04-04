package sbilife.com.pointofsale_bancaagency.reports.commonreports;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.text.Html;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.List;

import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

public class FundValueAsyncTask extends AsyncTask<String, String, String> {
    private volatile boolean running = true;
    private String status = "";
    private final ProgressDialog progressDialog;
    private String policyNumber, customerId;
    private GetFundValueInterface listener;
    private String inputpolicylist;
    private String strPolicyListErrorCOde;

    private List<String> Node;

    public FundValueAsyncTask(Context context, String customerId, String policyNumber,
                              GetFundValueInterface listener) {
        this.policyNumber = policyNumber;
        this.customerId = customerId;
        this.listener = listener;
        progressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
        String Message = "Loading Please wait...";
        progressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(true);
        progressDialog.setMax(100);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.show();
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    protected String doInBackground(String... aurl) {
        try {
            running = true;
            String NAMESPACE = "http://tempuri.org/";
            CommonMethods mCommonMethods = new CommonMethods();

            final String METHOD_NAME_FUND_VALUE_DETAIL = "getFundValueDetail";
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_FUND_VALUE_DETAIL);
            //request.addProperty("strCustId", "36103313");
            request.addProperty("strCustID", customerId);
            request.addProperty("strAuthKey", mCommonMethods.getEncryptedAuthKey());
//                request.addProperty("strCustId", "36103313");
            //request.addProperty("strCustId", "47163191");
//				request.addProperty("strCustID",customerId);
//				request.addProperty("strCustId","10082889");
            System.out.println("sa.toString() = " + request.toString());
            SoapSerializationEnvelope envelope =
                    new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);
            final String SOAP_ACTION_FUND_VALUE_DETAIL = "http://tempuri.org/getFundValueDetail";
            mCommonMethods.TLSv12Enable();
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            HttpTransportSE androidHttpTranport = new HttpTransportSE("https://sbilposservices.sbilife.co.in/service.asmx?wsdl");

            androidHttpTranport.call(SOAP_ACTION_FUND_VALUE_DETAIL, envelope);
            Object response = envelope.getResponse();

            SoapPrimitive sa = null;

            sa = (SoapPrimitive) envelope.getResponse();

            inputpolicylist = sa.toString();
            System.out.println("sa.toString() = " + sa.toString());


            ParseXML prsObj = new ParseXML();

            inputpolicylist = prsObj.parseXmlTag(inputpolicylist, "CustDls");
            inputpolicylist = new ParseXML().parseXmlTag(inputpolicylist, "ScreenData");
            strPolicyListErrorCOde = inputpolicylist;

            if (strPolicyListErrorCOde == null) {
//							inputpolicylist = "<CustDls> <Table> <POLICYNO>18001065009</POLICYNO> <PRODUCTNAME>HORIZON</PRODUCTNAME> <DATEOFCOMMENCEMENT>15-FEB-2005</DATEOFCOMMENCEMENT> <FREQUENCY>Quarterly</FREQUENCY> <PREMIUMAMOUNT>3000</PREMIUMAMOUNT> <STATUS>In Force</STATUS> <PREMIUMDUEDATE>01-OCT-2012</PREMIUMDUEDATE> <UNPAIDPREMIUM>15-AUG-2012</UNPAIDPREMIUM> <MATURITYDATE> </MATURITYDATE> <MATURITYFLAG>false</MATURITYFLAG> </Table> </CustDls> ";
//                    inputpolicylist = "<CustDls> <Table> <POLICYNO>51451067504</POLICYNO> <PRODUCTNAME>Sbi Life Smart Wealth Builder</PRODUCTNAME> <STATUS>Lapse</STATUS> <FUP>26-AUG-2016</FUP> <FUNDVALUE>30021.10260062</FUNDVALUE> <POLICYTYPE>ULIP</POLICYTYPE> </Table> <Table> <POLICYNO>1K024365706</POLICYNO> <PRODUCTNAME>Sbi Life Smart Wealth Builder</PRODUCTNAME> <STATUS>Lapse</STATUS> <FUP>26-AUG-2016</FUP> <FUNDVALUE>30021.10260062</FUNDVALUE> <POLICYTYPE>ULIP</POLICYTYPE> </Table> </CustDls>";
//                    inputpolicylist = "<CustDls>  <Table> <POLICYNO>51451067504</POLICYNO>  <PRODUCTNAME>SHIELD Plan A -  Level Cover</PRODUCTNAME>    <DATEOFCOMMENCEMENT>05-NOV-2008</DATEOFCOMMENCEMENT>    <FREQUENCY>Yearly</FREQUENCY>    <PREMIUMAMOUNT>13214</PREMIUMAMOUNT>    <STATUS>In Force</STATUS>    <PREMIUMDUEDATE>05-NOV-2011</PREMIUMDUEDATE>    <UNPAIDPREMIUM>05-NOV-2012</UNPAIDPREMIUM>    <MATURITYDATE xml:space='preserve'> </MATURITYDATE>    <MATURITYFLAG>false</MATURITYFLAG>  </Table>  <Table>    <POLICYNO>14001705310</POLICYNO>    <PRODUCTNAME>MoneyBack Option 1</PRODUCTNAME>    <DATEOFCOMMENCEMENT>10-FEB-2005</DATEOFCOMMENCEMENT>   <FREQUENCY>Yearly</FREQUENCY>    <PREMIUMAMOUNT>5978</PREMIUMAMOUNT>    <STATUS>Lapse Paid Up</STATUS>    <PREMIUMDUEDATE>10-FEB-2011</PREMIUMDUEDATE>    <UNPAIDPREMIUM>10-FEB-2012</UNPAIDPREMIUM>    <MATURITYDATE xml:space='preserve'> </MATURITYDATE>    <MATURITYFLAG>false</MATURITYFLAG>  </Table>  <Table>    <POLICYNO>18003030006</POLICYNO>    <PRODUCTNAME>HORIZON</PRODUCTNAME>    <DATEOFCOMMENCEMENT>10--2006</DATEOFCOMMENCEMENT>    <FREQUENCY>Yearly</FREQUENCY>    <PREMIUMAMOUNT>12000</PREMIUMAMOUNT>    <STATUS>In Force</STATUS>    <PREMIUMDUEDATE>10-JAN-2012</PREMIUMDUEDATE>    <UNPAIDPREMIUM>10-JAN-2013</UNPAIDPREMIUM>    <MATURITYDATE xml:space='preserve'> </MATURITYDATE>    <MATURITYFLAG>false</MATURITYFLAG>  </Table></CustDls>";

                inputpolicylist = sa.toString();
//                    inputpolicylist = prsObj.parseXmlTag(inputpolicylist, "CustDls");

                Node = prsObj.parseParentNode(inputpolicylist, "Table");
                status = "Success";
            }

        } catch (Exception e) {
            progressDialog.dismiss();
            running = false;
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPostExecute(String unused) {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        if (running) {
            if (status.equalsIgnoreCase("Success")) {
                listener.getFundValueInterfaceMethod(Node, policyNumber);
            } else {
                listener.getFundValueInterfaceMethod(null, "");
            }
        } else {
            listener.getFundValueInterfaceMethod(null, "");
        }
    }

    public interface GetFundValueInterface {
        void getFundValueInterfaceMethod(List<String> Node, String policyNumber);
    }
}

