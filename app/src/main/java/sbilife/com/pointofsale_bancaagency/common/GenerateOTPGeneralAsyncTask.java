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

public class GenerateOTPGeneralAsyncTask extends AsyncTask<String, String, String> {
    int flag = 0;
    private volatile boolean running = true;
    private final ProgressDialog progressDialog;
    private String mobileNumber;
    private final GenerateOTPAsyncResultInterface listener;
    private final CommonMethods commonMethods;

    public GenerateOTPGeneralAsyncTask(String mobileNumber, GenerateOTPAsyncResultInterface listener, Context mContext) {
        this.mobileNumber = mobileNumber;
        this.listener = listener;
        progressDialog = new ProgressDialog(mContext, ProgressDialog.THEME_HOLO_LIGHT);
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
        if (progressDialog != null) {
            progressDialog.show();
        }
    }

    @Override
    protected String doInBackground(String... aurl) {

        try {
            final String SOAP_ACTION_GENERATE_PASSCODE = "http://tempuri.org/GenerateOTP_EasyAccess";
            final String METHOD_NAME_GENERATE_PASSCODE = "GenerateOTP_EasyAccess";
            String NAMESPACE = "http://tempuri.org/";
            running = true;
            SoapObject request = new SoapObject(NAMESPACE,
                    METHOD_NAME_GENERATE_PASSCODE);
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

            androidHttpTranport.call(SOAP_ACTION_GENERATE_PASSCODE, envelope);
            SoapPrimitive sa;

            sa = (SoapPrimitive) envelope.getResponse();
            String inputpolicylist = sa.toString();

            if (inputpolicylist.equalsIgnoreCase("0") || inputpolicylist.equalsIgnoreCase("2")) {
                flag = 0;
            } else {
                flag = 1;
            }

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
            listener.generateOTPAsynResultMethod(flag);
        } else {
            listener.generateOTPAsynResultMethod(-1);
        }
    }

    public interface GenerateOTPAsyncResultInterface {
        void generateOTPAsynResultMethod(int result);
    }
}
