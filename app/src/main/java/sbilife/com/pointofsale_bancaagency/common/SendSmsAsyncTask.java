package sbilife.com.pointofsale_bancaagency.common;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.needanalysis.NeedAnalysisActivity;
import sbilife.com.pointofsale_bancaagency.needanalysis.OthersProductListActivity;
import sbilife.com.pointofsale_bancaagency.new_bussiness.NewBusinessHomeGroupingActivity;

public class SendSmsAsyncTask extends AsyncTask<String, String, String> {

    private volatile boolean running = true;
    private String response = "", strMailID = "";
    private final ProgressDialog progressDialog;

    private final String message;
    private final String mobileNumber;

    private Context context;
    public SendSmsAsyncTask(Context context,String mobileNumber, String message) {
        this.context = context;
        this.mobileNumber = mobileNumber;
        this.message = message;

        progressDialog = new ProgressDialog(context,ProgressDialog.THEME_HOLO_LIGHT);
        String Message = "Loading Please wait...";
        progressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
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

            running = true;
            String NAMESPACE= "http://tempuri.org/";
            String METHOD_NAME_SEND_SMS = "sendSMS_SMRT";

            //email non mandate 02-12-2019 starts
            strMailID = aurl[0];
            //email non mandate 02-12-2019 ends

            //sendSMS_SMRT(string strMessage, string strMobile)

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_SEND_SMS);
            request.addProperty("strMessage", message);
            request.addProperty("strMobile", mobileNumber);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            new CommonMethods().TLSv12Enable();

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            String URl = ServiceURL.SERVICE_URL;
            HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
            String SOAP_ACTION = "http://tempuri.org/" + METHOD_NAME_SEND_SMS;
            androidHttpTranport.call(SOAP_ACTION, envelope);

            SoapPrimitive sa = (SoapPrimitive) envelope.getResponse();
            response = sa.toString();
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

        CommonMethods commonMethods = new CommonMethods();
        if (running) {
            if (response.equalsIgnoreCase("success")) {
                commonMethods.showCentralToast(context, "SMS sent successfully");
            } else {
                commonMethods.showCentralToast(context, "SMS sending failed");
            }
        } else {
            commonMethods.showCentralToast(context, "SMS sending failed");
        }

        //email non mandate 02-12-2019 starts
        if (strMailID.equals("") && !TextUtils.isEmpty(NeedAnalysisActivity.URN_NO)){
            gotoNeedAnalysisHomeDialog("URN  : " + NeedAnalysisActivity.URN_NO + "\n\n Details Sync Succesfully");
        }
        //email non mandate 02-12-2019 ends
    }

    private void gotoNeedAnalysisHomeDialog(String message) {

        try {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_with_ok_button);
            dialog.setCancelable(false);
            TextView text = dialog.findViewById(R.id.tv_title);
            text.setText(message);
            Button dialogButton = dialog.findViewById(R.id.bt_ok);
            dialogButton.setText("Ok");
            dialogButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dialog.dismiss();

                    Intent i = new Intent(context,
                            NewBusinessHomeGroupingActivity.class);
                    context.startActivity(i);
                    NeedAnalysisActivity.URN_NO = "";
                    OthersProductListActivity.URNNumber = "";
                    OthersProductListActivity.groupName = "";
                    ((AppCompatActivity) context).finish();
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
