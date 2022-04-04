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

import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

public class SendRenewalSMSAsynTask extends AsyncTask<String, String, String> {

    private volatile boolean running = true;
    private String response = "";

    private final String policyNumber;
    private final String mobileNumber;
    private final String dueDate;
    private final String status;
    private final String amount;
    private final String language;
    private final ProgressDialog progressDialog;
    private final SendRenewalSMSInterface listener;

    public SendRenewalSMSAsynTask(String policyNumber, String mobileNumber, String dueDate, String status,
                                  String amount, String language, Context context, SendRenewalSMSInterface listener) {
        this.policyNumber = policyNumber;
        this.mobileNumber = mobileNumber;
        this.dueDate = dueDate;
        this.status = status;
        this.amount = amount;
        this.language = language;
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

    @SuppressLint("SimpleDateFormat")
    @Override
    protected String doInBackground(String... aurl) {
        try {
            CommonMethods commonMethods = new CommonMethods();
            String NAMESPACE = "http://tempuri.org/";
            running = true;
            String newDueDate = "";
            try {
                String[] separated = dueDate.split("-");
                String day = separated[0]; // this will contain "Fruit"
                String monthNumber = commonMethods.getMonthNumber(separated[1]);
                String month = commonMethods.getFullMonthName(monthNumber);
                String year = separated[2];

                newDueDate = day + "-" + month + "-" + year;
            } catch (Exception e) {
                e.printStackTrace();
            }

            String METHOD_NAME_SEND_SMS = "SendRenewalDueSMS_SMRT";
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_SEND_SMS);
            request.addProperty("strPolicyNo", policyNumber);
            request.addProperty("strMobileNo", mobileNumber);
            //request.addProperty("strDueDate", dueDate);
            request.addProperty("strDueDate", newDueDate);
            request.addProperty("strStatus", status);
            request.addProperty("strAmt", amount);
            request.addProperty("strLanguage", language);
            System.out.println("request.toString() = " + request.toString());
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
            String SOAP_ACTION = "http://tempuri.org/" + METHOD_NAME_SEND_SMS;
            androidHttpTranport.call(SOAP_ACTION, envelope);

            SoapPrimitive sa = (SoapPrimitive) envelope.getResponse();
            response = sa.toString();
            System.out.println("request.toString() = " + response.toString());
            if (response.equalsIgnoreCase("1")) {
                response = "success";
            } else {
                response = "";
            }
        } catch (Exception e) {
            progressDialog.dismiss();
            running = false;
        }
        return null;

    }


    @Override
    protected void onPostExecute(String unused) {

        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        if (running) {
            if (response.equalsIgnoreCase("success")) {
                listener.getSMSDetailsInterfaceMethod("1");
                //commonMethods.showMessageDialog(context, "Message sent successfully");
            } else {
                listener.getSMSDetailsInterfaceMethod("0");
                //commonMethods.showMessageDialog(context, "Message sending failed");
            }
        } else {
            listener.getSMSDetailsInterfaceMethod("0");
            //commonMethods.showMessageDialog(context, "Message sending failed");
        }
    }

    public interface SendRenewalSMSInterface {
        void getSMSDetailsInterfaceMethod(String result);
    }
}