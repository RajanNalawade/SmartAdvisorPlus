package sbilife.com.pointofsale_bancaagency.home.lmcorner;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;

public class IAClubDetailActivity extends AppCompatActivity implements ServiceHits.DownLoadData {

    private final String METHOD_NAME = "getIAClubDetail_smrt";
    private ProgressDialog mProgressDialog;
    private Context context;
    private CommonMethods commonMethods;
    private ServiceHits service;
    private String strCIFBDMUserId = "";
    private String strCIFBDMEmailId = "";
    private String strCIFBDMMObileNo = "", strCIFBDMPassword = "";
    private IAClubDetailAsyncTask iaClubDetailAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_i_a_club_detail);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        context = this;
        commonMethods = new CommonMethods();
        commonMethods.setApplicationToolbarMenu(this, "LM Survey");

        Intent intent = getIntent();
        String fromHome = intent.getStringExtra("fromHome");
        if (fromHome != null && fromHome.equalsIgnoreCase("N")) {
            strCIFBDMUserId = intent.getStringExtra("strBDMCifCOde");
            strCIFBDMEmailId = intent.getStringExtra("strEmailId");
            strCIFBDMMObileNo = intent.getStringExtra("strMobileNo");
        } else {
            CommonMethods.UserDetailsValuesModel userDetailsValuesModel = commonMethods.setUserDetails(context);
            strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
            strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
            strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();
        }
        try {
            strCIFBDMPassword = commonMethods.getStrAuth();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mProgressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
        String Message = "Loading Please wait...";
        mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);

        mProgressDialog.setButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        killTasks();
                    }
                });

        mProgressDialog.setMax(100);
        service_hits();
    }

    private void service_hits() {
        CommonMethods.UserDetailsValuesModel userDetailsValuesModel = commonMethods.setUserDetails(context);
        String input = strCIFBDMUserId;
        service = new ServiceHits(context, METHOD_NAME, input,
                strCIFBDMUserId, strCIFBDMEmailId,
                strCIFBDMMObileNo, strCIFBDMPassword, this);
        service.execute();
    }

    @Override
    public void downLoadData() {
        iaClubDetailAsyncTask = new IAClubDetailAsyncTask();
        iaClubDetailAsyncTask.execute();
    }

    @Override
    protected void onDestroy() {
        killTasks();
        super.onDestroy();
    }

    private void killTasks() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        if (iaClubDetailAsyncTask != null) {
            iaClubDetailAsyncTask.cancel(true);
        }

        if (service != null) {
            service.cancel(true);
        }
    }

    private void clearList() {

    }

    class IAClubDetailAsyncTask extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String error = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();

            if (mProgressDialog != null && !mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
        }

        @SuppressLint("SimpleDateFormat")
        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;
                SoapObject request;
                //    getIAClubDetail_smrt(string strCode, string strEmailId, string strMobileNo, string strAuthKey)
                String NAMESPACE = "http://tempuri.org/";
                request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("strCode", strCIFBDMUserId);
                commonMethods.appendSecurityParams(context, request, strCIFBDMEmailId, strCIFBDMMObileNo);
                System.out.println("request:" + request.toString());

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                commonMethods.TLSv12Enable();

                String URl = ServiceURL.SERVICE_URL;
                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                String SOAP_ACTION = "http://tempuri.org/" + METHOD_NAME;
                androidHttpTranport.call(SOAP_ACTION, envelope);
                Object response = envelope.getResponse();

                if (!response.toString().contentEquals("<CIFPolicyList />")) {

                    String inputpolicylist = response.toString();
                    System.out.println("inputpolicylist = " + inputpolicylist);
                    error = inputpolicylist;

                } else {
                    error = "1";
                }

            } catch (Exception e) {
                mProgressDialog.dismiss();
                running = false;
            }
            return null;

        }


        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            if (running) {
                if (error.equalsIgnoreCase("1")) {
                    String appMsgStatus = "Links\n" +
                            "\n" +
                            "\" Life Mitra - We are listening\"   https://bit.ly/3gHJpQA\n\n" +
                            " \"लाइफ मित्र - हम सुन रहे हैं....\" https://bit.ly/3eGGWmG";
                    appMsgStatus = appMsgStatus.replace("\\n\\n", "\n\n");
                    Log.d("msg = ", appMsgStatus);
                    final SpannableString spannableString = new SpannableString(appMsgStatus); // msg should have url to enable clicking
                    Linkify.addLinks(spannableString, Linkify.WEB_URLS);


                    final AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                    LayoutInflater inflater = ((AppCompatActivity) context).getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.dialog_with_ok_button, null);
                    builder1.setView(dialogView);
                    TextView text = dialogView.findViewById(R.id.tv_title);
                    text.setText(spannableString);
                    text.setMovementMethod(LinkMovementMethod.getInstance());
                    Button dialogButton = dialogView.findViewById(R.id.bt_ok);
                    dialogButton.setText("Ok");
                    //((TextView) dialogView.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
                    final AlertDialog dialog = builder1.create();
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    //dialog.setContentView(R.layout.dialog_with_ok_button);
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                } else {
                    commonMethods.showMessageDialog(context, "You are not eligible.");
                    clearList();
                }
            } else {
                commonMethods.showMessageDialog(context, "No Record found");
                clearList();
            }
        }
    }
}