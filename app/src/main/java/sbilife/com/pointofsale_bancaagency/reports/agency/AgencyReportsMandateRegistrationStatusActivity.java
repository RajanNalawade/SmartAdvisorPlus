package sbilife.com.pointofsale_bancaagency.reports.agency;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods.UserDetailsValuesModel;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits.DownLoadData;

public class AgencyReportsMandateRegistrationStatusActivity extends AppCompatActivity implements OnClickListener, DownLoadData {

    private final String METHOD_NAME_MANDATE_REGISTRATION_STATUS = "getMandateStatus";

    private DownloadFileAsyncMandateRegistrationStatus taskMandateRegistrationStatus;

    private final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private ProgressDialog mProgressDialog;

    private CommonMethods commonMethods;
    private Context context;

    private String strCIFBDMUserId = "", strCIFBDMEmailId = "",
            strCIFBDMPassword = "", strCIFBDMMObileNo = "";
    private TextView textmandateRegistrationStatusOutput;
    private EditText edmandateRegistrationStatusPolicyNo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.banca_reports_mandate_registration_status);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        context = this;
        commonMethods = new CommonMethods();
        commonMethods.setApplicationToolbarMenu(this, "Mandate Registration Status");

        taskMandateRegistrationStatus = new DownloadFileAsyncMandateRegistrationStatus();
        mProgressDialog = new ProgressDialog(context);

        Button btn_mandateRegistrationStatus_ok = findViewById(R.id.btn_mandateRegistrationStatus_ok);
        textmandateRegistrationStatusOutput = findViewById(R.id.textmandateRegistrationStatusOutput);
        edmandateRegistrationStatusPolicyNo = findViewById(R.id.edmandateRegistrationStatusPolicyNo);
        btn_mandateRegistrationStatus_ok.setOnClickListener(this);
        Intent intent = getIntent();
        String fromHome = intent.getStringExtra("fromHome");
        if (fromHome != null && fromHome.equalsIgnoreCase("N")) {
            strCIFBDMUserId = intent.getStringExtra("strBDMCifCOde");
            strCIFBDMEmailId = intent.getStringExtra("strEmailId");
            strCIFBDMMObileNo = intent.getStringExtra("strMobileNo");

            try {
                strCIFBDMPassword = SimpleCrypto.encrypt("SBIL", "sbil");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            getUserDetails();
        }
    }

    private void getUserDetails() {
        UserDetailsValuesModel userDetailsValuesModel = commonMethods.setUserDetails(context);
        strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        strCIFBDMPassword = userDetailsValuesModel.getStrCIFBDMPassword();
        strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();
    }

    private void service_hits() {


        ServiceHits service = new ServiceHits(context,
                METHOD_NAME_MANDATE_REGISTRATION_STATUS, edmandateRegistrationStatusPolicyNo.getText().toString(), strCIFBDMUserId,
                strCIFBDMEmailId, strCIFBDMMObileNo,
                strCIFBDMPassword, this);
        service.execute();

    }


    private void startDownloadMandateRegistrationStatus() {
        taskMandateRegistrationStatus.execute("");
    }

    @Override
    public void downLoadData() {
        startDownloadMandateRegistrationStatus();

    }

    class DownloadFileAsyncMandateRegistrationStatus extends
            AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String mandateRegistrationStatusOutput = "",
                strMandateRegistrationStatusErrorCOde = "", inputpolicylist = "", edmandateRegistrationStatusPolicyNo_text;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
            edmandateRegistrationStatusPolicyNo_text = edmandateRegistrationStatusPolicyNo.getText().toString();
        }

        @Override
        protected String doInBackground(String... aurl) {
            try {
                System.out.println("doinback piwc");
                running = true;
                SoapObject request;

                // string strPolicyNo, string strEmailId, string strMobileNo,
                // string strAuthKey
                String NAMESPACE = "http://tempuri.org/";
                request = new SoapObject(NAMESPACE,
                        METHOD_NAME_MANDATE_REGISTRATION_STATUS);
                request.addProperty("strPolicyNo",
                        edmandateRegistrationStatusPolicyNo_text);
                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);
                request.addProperty("strAuthKey", strCIFBDMPassword.trim());
                request.addProperty("strCode", strCIFBDMUserId);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);
                System.out.println("request piwc:" + request.toString());
                // allowAllSSL();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                String URl = ServiceURL.SERVICE_URL;
                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                try {

                    String SOAP_ACTION_MANDATE_REGISTRATION_STATUS = "http://tempuri.org/getMandateStatus";
                    androidHttpTranport.call(
                            SOAP_ACTION_MANDATE_REGISTRATION_STATUS, envelope);
                    Object response = envelope.getResponse();

                    if (!response.toString().contentEquals("<PolicyDetails />")) {

                        SoapPrimitive sa;
                        try {
                            sa = (SoapPrimitive) envelope.getResponse();

                            inputpolicylist = sa.toString();

                            if (!sa.toString().equalsIgnoreCase("1")) {
                                ParseXML prsObj = new ParseXML();

                                // <ReqDtls><Table1><Status>Cancelled</Status><ProposalNo>53NA062275</ProposalNo>
                                // <Reason>Sent for
                                // Cancel/Refund</Reason></Table1></ReqDtls>

                                inputpolicylist = prsObj.parseXmlTag(
                                        inputpolicylist, "PolicyDetails");
                                System.out.println("inputpolicylist:"
                                        + inputpolicylist);

                                if (inputpolicylist != null) {
                                    // strProposalTrackerErrorCOde =
                                    // inputpolicylist;

                                    inputpolicylist = prsObj.parseXmlTag(
                                            inputpolicylist, "Table");
                                    strMandateRegistrationStatusErrorCOde = inputpolicylist;
                                    System.out
                                            .println("strMandateRegistrationStatusErrorCOde:"
                                                    + strMandateRegistrationStatusErrorCOde);

                                    if (strMandateRegistrationStatusErrorCOde != null) {
                                        // inputpolicylist = sa.toString();
                                        mandateRegistrationStatusOutput = new ParseXML()
                                                .parseXmlTag(inputpolicylist,
                                                        "RESULT");
                                        strMandateRegistrationStatusErrorCOde = "success";

                                    }

                                } else {
                                    strMandateRegistrationStatusErrorCOde = "0";
                                }
                            } else {
                                strMandateRegistrationStatusErrorCOde = "1";
                            }
                        } catch (Exception e) {
                            try {
                                throw (e);
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                            mProgressDialog.dismiss();
                            running = false;
                        }
                    } else {
                        mProgressDialog.dismiss();
                        running = false;
                    }

                } catch (IOException | XmlPullParserException e) {
                    try {
                        throw (e);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    mProgressDialog.dismiss();
                    running = false;
                }
            } catch (Exception e) {
                try {
                    throw (e);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                mProgressDialog.dismiss();
                running = false;
            }
            return null;

        }

        @Override
        protected void onProgressUpdate(String... progress) {
            Log.d("ANDRO_ASYNC", progress[0]);
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing()) {
                dismissProgressDialog();
            }
            if (running) {
                if (strMandateRegistrationStatusErrorCOde != null) {

                    if (strMandateRegistrationStatusErrorCOde
                            .equalsIgnoreCase("success")) {
                        textmandateRegistrationStatusOutput.setText(Html
                                .fromHtml("<b>Status : </b> "
                                        + mandateRegistrationStatusOutput));
                        textmandateRegistrationStatusOutput.getParent()
                                .requestChildFocus(
                                        textmandateRegistrationStatusOutput,
                                        textmandateRegistrationStatusOutput);

                    } else if (strMandateRegistrationStatusErrorCOde
                            .equalsIgnoreCase("1")) {
                        Toast.makeText(getApplicationContext(),
                                "You are not authorised user",
                                Toast.LENGTH_LONG).show();
                    } else {
                        // servererror();
                        Toast.makeText(getApplicationContext(),
                                "Policy Number is not valid", Toast.LENGTH_LONG)
                                .show();
                        setmandateRegistrationStatusToBlank();
                    }
                } else {
                    // servererror();
                    Toast.makeText(getApplicationContext(),
                            "Policy Number is not valid", Toast.LENGTH_LONG)
                            .show();
                    setmandateRegistrationStatusToBlank();
                }
            } else {
                // servererror();
                Toast.makeText(getApplicationContext(),
                        "Policy Number is not valid", Toast.LENGTH_LONG).show();
                setmandateRegistrationStatusToBlank();
            }
        }

        void setmandateRegistrationStatusToBlank() {
            textmandateRegistrationStatusOutput.setText("");
        }
    }

    @SuppressWarnings("deprecation")
    private void dismissProgressDialog() {
        if (mProgressDialog.isShowing()) {
            dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
        }
    }

    @SuppressWarnings("deprecation")
    private void showProgressDialog() {
        showDialog(DIALOG_DOWNLOAD_PROGRESS);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_DOWNLOAD_PROGRESS:
                mProgressDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
                String Message = "Loading Please wait...";
                mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgressDialog.setCancelable(true);

                mProgressDialog.setButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (taskMandateRegistrationStatus != null)
                                    taskMandateRegistrationStatus.cancel(true);
                                if (mProgressDialog != null) {
                                    if (mProgressDialog.isShowing()) {
                                        mProgressDialog.dismiss();
                                    }
                                }
                            }
                        });

                mProgressDialog.setMax(100);
                mProgressDialog.show();
                return mProgressDialog;

		/*case DATE_DIALOG_ID:
			return new DatePickerDialog(this, R.style.datepickerstyle,
					mDateSetListener, mYear, mMonth, mDay);*/

            default:
                return null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (taskMandateRegistrationStatus != null) {
                taskMandateRegistrationStatus.cancel(true);
            }
            if (mProgressDialog != null) {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
            }

            finish();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.btn_mandateRegistrationStatus_ok) {
            getMandateStatus();
        }
    }

    private void getMandateStatus() {
        commonMethods.hideKeyboard(edmandateRegistrationStatusPolicyNo, context);
        taskMandateRegistrationStatus = new DownloadFileAsyncMandateRegistrationStatus();

        if (commonMethods.isNetworkConnected(context)) {

            if (edmandateRegistrationStatusPolicyNo.getText().toString().equalsIgnoreCase("")) {
                //ProposalvalidationAlert("Please Enter Policy Number..");
                commonMethods.showMessageDialog(context, commonMethods.PROPOSAL_EMPTY_ALERT);
            } else {
                service_hits();
            }
        } else {
            //intereneterror();
            commonMethods.showMessageDialog(context, commonMethods.NO_INTERNET_MESSAGE);
        }
    }
}
