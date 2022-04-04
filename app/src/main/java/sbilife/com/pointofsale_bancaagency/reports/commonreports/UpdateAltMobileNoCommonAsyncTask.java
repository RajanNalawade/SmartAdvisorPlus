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
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

public class UpdateAltMobileNoCommonAsyncTask extends
        AsyncTask<String, String, String> {

    private volatile boolean running = true;
    private final String mobileNumber;
    private final String paymentKey;
    private final ProgressDialog progressDialog;
    private final UpdateAltMobNoInterface listener;

    public UpdateAltMobileNoCommonAsyncTask(String paymentKey, String mobileNumber,
                                            Context context, UpdateAltMobNoInterface listener) {
        this.paymentKey = paymentKey;
        this.mobileNumber = mobileNumber;
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
        String message = "";
        try {

            running = true;
            SoapObject request;
            CommonMethods commonMethods = new CommonMethods();
            String NAMESPACE = "http://tempuri.org/";

            String METHOD_NAME_UPDATE_MOBILE_ADVANCE_QUERY = "updateCRMMobile";
            request = new SoapObject(NAMESPACE,
                    METHOD_NAME_UPDATE_MOBILE_ADVANCE_QUERY);

            request.addProperty("strMobileNo", mobileNumber);
            request.addProperty("strPaymentKey", paymentKey);
            System.out.println("request:" + request.toString());

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            // allowAllSSL();
            commonMethods.TLSv12Enable();
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String URl = ServiceURL.SERVICE_URL;
            HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
            try {
                String SOAP_ACTION_UPDATE_MOBILE_ADVANCE_QUERY = "http://tempuri.org/updateCRMMobile";
                androidHttpTranport.call(SOAP_ACTION_UPDATE_MOBILE_ADVANCE_QUERY,envelope);

                Object response = envelope.getResponse();
                if (!response.toString().contentEquals("anyType{}")) {
                    SoapPrimitive sa;
                    try {
                        sa = (SoapPrimitive) envelope.getResponse();
                        String inputpolicylist = sa.toString();
                        System.out.println("request:" + inputpolicylist);
                        if (inputpolicylist.equalsIgnoreCase("Success")) {
                            message = inputpolicylist;
                        }

                    } catch (Exception e) {
                        progressDialog.dismiss();
                        running = false;
                    }
                }

            } catch (IOException e) {
                progressDialog.dismiss();
                running = false;
            } catch (XmlPullParserException e) {
                progressDialog.dismiss();
                running = false;
            }
        } catch (Exception e) {
            progressDialog.dismiss();
            running = false;
        }
        return message;

    }

    @Override
    protected void onPostExecute(String message) {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        listener.getUpdateAltMobResultMethod(message);
    }

    public interface UpdateAltMobNoInterface {
        void getUpdateAltMobResultMethod(String result);
    }
}