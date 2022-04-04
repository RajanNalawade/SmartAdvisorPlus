package sbilife.com.pointofsale_bancaagency.reports.commonreports;

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

import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

public class GetPremiumAmountCommonAsync extends AsyncTask<String, Void, String> {
    private String PremiumAmt;
    private final String policyNumber;
    private final ProgressDialog progressDialog;
    private final GetPremiumAmountInterface listener;

    public GetPremiumAmountCommonAsync(String policyNumber, Context context, GetPremiumAmountInterface listener) {

        this.policyNumber = policyNumber;
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

        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    @Override
    protected String doInBackground(String... param) {

        try {
            //GetPremiumDetailsHotPayment_css(string strPolicyNo, string baCode, string reqCssStr)
            String NAMESPACE = "http://tempuri.org/";
            final String METHOD_NAME_PREMIUM = "GetPremiumDetailsHotPayment_css";
            CommonMethods commonMethods = new CommonMethods();
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_PREMIUM);

            request.addProperty("strPolicyNo", policyNumber);
            request.addProperty("baCode", "0");
            request.addProperty("reqCssStr", "EASYACCESS");

            System.out.println("result " + request.toString());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            //Enable this envelope if service is written in dot net
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE("https://sbilposservices.sbilife.co.in/service.asmx?wsdl");

            commonMethods.TLSv12Enable();

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            final String SOAP_ACTION_PREMIUM = "http://tempuri.org/GetPremiumDetailsHotPayment_css";
            androidHttpTransport.call(SOAP_ACTION_PREMIUM, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

            String result = response.toString();

            System.out.println("result " + result);

            if (result.contains("<ErrCode>0</ErrCode>")) {
                ParseXML prsObj = new ParseXML();
                result = prsObj.parseXmlTag(result, "ScreenData");

                PremiumAmt = prsObj.parseXmlTag(result, "GrossAmt");
                PremiumAmt = PremiumAmt.replaceFirst("^0+(?!$)", "");
                return "Success";

            } else if (result.contains("<ErrCode>1</ErrCode>")) {
                ParseXML prsObj = new ParseXML();
                result = prsObj.parseXmlTag(result, "ErrDesc");
                return result;
            } else {
                return "Try After Some Time...";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "No record found";
        }


    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        if (result.equals("Success")) {
            listener.getPremiumInterfaceMethod(PremiumAmt,"");
        } else {
            listener.getPremiumInterfaceMethod("",result);
        }
    }

    public interface GetPremiumAmountInterface {
        void getPremiumInterfaceMethod(String PremiumAmt,String result);
    }
}