package sbilife.com.pointofsale_bancaagency.common;

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

import sbilife.com.pointofsale_bancaagency.ServiceURL;

public class ValidateOTPGeneralAsyncTask extends AsyncTask<String, String, String> {

    int flag = 0;
    private volatile boolean running = true;

    private final ProgressDialog progressDialog;
    private final String mobileNumber;
    private final String OTP;
    private final ValidateOTPGeneralAsyncTask.ValidateOTPAsyncResultInterface listener;
    private final CommonMethods commonMethods;
    public ValidateOTPGeneralAsyncTask(String mobileNumber, String OTP,
                                       ValidateOTPGeneralAsyncTask.ValidateOTPAsyncResultInterface listener,
                                Context context) {
        this.mobileNumber = mobileNumber;
        this.OTP = OTP;
        this.listener = listener;

        progressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
        String Message = "Loading Please wait...";
        progressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setMax(100);
        commonMethods = new CommonMethods();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... aurl) {

        try {

            running = true;
            String NAMESPACE = "http://tempuri.org/";
            final String SOAP_ACTION_VALIDATE_OTP = "http://tempuri.org/validateOTP_easyacess";
            final String METHOD_NAME_VALIDATE_OTP = "validateOTP_easyacess";

            SoapObject request = new SoapObject(NAMESPACE,
                    METHOD_NAME_VALIDATE_OTP);
            request.addProperty("strOTP", OTP);
            request.addProperty("MOBILE_NO", mobileNumber);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            commonMethods.TLSv12Enable();

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String URl = ServiceURL.SERVICE_URL;
            HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
            androidHttpTranport.call(SOAP_ACTION_VALIDATE_OTP, envelope);

            SoapPrimitive sa;

            sa = (SoapPrimitive) envelope.getResponse();
            String inputpolicylist = sa.toString();

            if (inputpolicylist.equalsIgnoreCase("1")) {
                flag = 1;
            } else
                flag = 0;

        } catch (Exception e) {
            e.printStackTrace();
            running = false;

        }
        return null;

    }

    @Override
    protected void onPostExecute(String unused) {

        if (progressDialog.isShowing())
            progressDialog.dismiss();

        if (running) {
            listener.validateOTPAsynResultMethod(flag);
        } else {
            listener.validateOTPAsynResultMethod(-1);
        }
    }

    public interface ValidateOTPAsyncResultInterface {
        void validateOTPAsynResultMethod(int result);
    }
}
