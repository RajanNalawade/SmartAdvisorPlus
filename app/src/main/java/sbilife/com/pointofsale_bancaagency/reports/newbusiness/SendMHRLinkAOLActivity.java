package sbilife.com.pointofsale_bancaagency.reports.newbusiness;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;

public class SendMHRLinkAOLActivity extends AppCompatActivity implements View.OnClickListener, ServiceHits.DownLoadData {
    private final String METHOD_NAME = "getMHRlink_smrt";
    private ProgressDialog mProgressDialog;
    private ArrayList<ParseXML.SendSMSAlternateProcessValuesModel> globalDataList;
    private Context context;
    private EditText etProposalNumber;
    private String proposalNumber = "";

    private ServiceHits service;

    private CommonMethods commonMethods;
    private String strCIFBDMUserId = "", strCIFBDMEmailId = "", strCIFBDMPassword = "",
            strCIFBDMMObileNo = "";
    private DownloadSendMHRLinkAsyncTask downloadSendMHRLinkAsyncTask;
    private TextView texviewLink;
    private LinearLayout llMHRLink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_send_m_h_r_link_a_o_l);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        context = this;
        commonMethods = new CommonMethods();
        commonMethods.setApplicationToolbarMenu(this, "Send MHR Link (Agent Own Life)");

        etProposalNumber = findViewById(R.id.etProposalNumber);
        Button buttonOk = findViewById(R.id.buttonOk);
        Button buttonOpenLink = findViewById(R.id.buttonOpenLink);
        texviewLink = findViewById(R.id.texviewLink);
        ImageView imageviewLink = findViewById(R.id.imageviewLink);
        llMHRLink = findViewById(R.id.llMHRLink);
        llMHRLink.setVisibility(View.GONE);

        buttonOk.setOnClickListener(this);
        buttonOpenLink.setOnClickListener(this);
        imageviewLink.setOnClickListener(this);
        texviewLink.setOnClickListener(this);


        Intent intent = getIntent();
        String fromHome = intent.getStringExtra("fromHome");

        if (fromHome != null && fromHome.equalsIgnoreCase("Y")) {
            getUserDetails();
        } else {
            strCIFBDMUserId = intent.getStringExtra("strBDMCifCOde");
            strCIFBDMEmailId = intent.getStringExtra("strEmailId");
            strCIFBDMMObileNo = intent.getStringExtra("strMobileNo");
            try {
                strCIFBDMPassword = SimpleCrypto.encrypt("SBIL", "sbil");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        mProgressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
        String Message = "Loading Please wait...";
        mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);

        mProgressDialog.setButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                        if (downloadSendMHRLinkAsyncTask != null) {
                            downloadSendMHRLinkAsyncTask.cancel(true);
                        }

                        if (mProgressDialog != null) {
                            if (mProgressDialog.isShowing()) {
                                mProgressDialog.dismiss();
                            }
                        }
                    }
                });

        mProgressDialog.setMax(100);

    }

    private void getUserDetails() {
        CommonMethods.UserDetailsValuesModel userDetailsValuesModel = commonMethods.setUserDetails(context);
        strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        strCIFBDMPassword = commonMethods.getStrAuth();
        strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.buttonOk:
                commonMethods.hideKeyboard(etProposalNumber, context);
                llMHRLink.setVisibility(View.GONE);
                proposalNumber = etProposalNumber.getText().toString().trim();

                if (commonMethods.isNetworkConnected(context)) {

                    if (TextUtils.isEmpty(proposalNumber)) {
                        commonMethods.showMessageDialog(context, "Please Enter Proposal Number");
                    } else {
                        service_hits(proposalNumber);
                    }
                } else {
                    commonMethods.showMessageDialog(context, commonMethods.NO_INTERNET_MESSAGE);
                }
                break;

            case R.id.imageviewLink:
            case R.id.texviewLink:
            case R.id.buttonOpenLink:
                String link = texviewLink.getText().toString();
                commonMethods.openWebLink(context,link);
                break;
        }
    }

    private void service_hits(String input) {
        service = new ServiceHits(context, METHOD_NAME, input,
                strCIFBDMUserId, strCIFBDMEmailId,
                strCIFBDMMObileNo, strCIFBDMPassword, this);
        service.execute();
    }

    @Override
    public void downLoadData() {
        downloadSendMHRLinkAsyncTask = new DownloadSendMHRLinkAsyncTask();
        downloadSendMHRLinkAsyncTask.execute();
    }

    class DownloadSendMHRLinkAsyncTask extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String result = "";

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
                // getMHRlink_smrt(string strCode, string strProposalNo, string strEmailId, string strMobileNo, string strAuthKey)

                String NAMESPACE = "http://tempuri.org/";
                request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("strCode", strCIFBDMUserId);
                request.addProperty("strProposalNo", proposalNumber);
                commonMethods.appendSecurityParams(context, request, strCIFBDMEmailId, strCIFBDMMObileNo);
                commonMethods.TLSv12Enable();

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
                result = response.toString();

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
            llMHRLink.setVisibility(View.GONE);
            if (running) {
                if (result.equalsIgnoreCase("3")) {
                    commonMethods.showMessageDialog(context, "MHR Link sent successfully");
                } else if (result.equalsIgnoreCase("2")) {
                    commonMethods.showMessageDialog(context, "MHR already submitted");
                } else if (result.equalsIgnoreCase("0")) {
                    commonMethods.showMessageDialog(context, "Invalid Proposal Number");
                } else {
                    llMHRLink.setVisibility(View.VISIBLE);
                    texviewLink.setText(result);
                }
            } else {
                commonMethods.showMessageDialog(context, "No Record found");
            }
        }
    }

}
