package sbilife.com.pointofsale_bancaagency.reports.newbusiness;

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
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
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
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods.UserDetailsValuesModel;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits.DownLoadData;

public class BancaReportsPIWCActivity extends AppCompatActivity implements OnClickListener, DownLoadData {

    private final String METHOD_NAME_PIWC_TRACKER = "getPIWCStatus";

    private DownloadFileAsyncPIWCTracker taskPiwcTracker;

    private final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private ProgressDialog mProgressDialog;

    private CommonMethods mCommonMethods;
    private Context context;

    private String strCIFBDMUserId = "", strCIFBDMEmailId = "",
            strCIFBDMPassword = "", strCIFBDMMObileNo = "", edPIWCProposalNo_text = "";

    private EditText edPIWCProposalNo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.banca_reports_piwc);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        context = this;
        mCommonMethods = new CommonMethods();
        mCommonMethods.setApplicationToolbarMenu(this, "PIWC Status");

        taskPiwcTracker = new DownloadFileAsyncPIWCTracker();
        mProgressDialog = new ProgressDialog(context);
        edPIWCProposalNo = findViewById(R.id.edPIWCProposalNo);
        Button btn_PIWC_ok = findViewById(R.id.btn_PIWC_ok);
        LinearLayout lnPIWCProposerTracker = findViewById(R.id.lnPIWCProposerTracker);
        lnPIWCProposerTracker.setVisibility(View.VISIBLE);
        btn_PIWC_ok.setOnClickListener(this);

        Intent intent = getIntent();
        String fromHome = intent.getStringExtra("fromHome");
        if (fromHome != null && fromHome.equalsIgnoreCase("N")) {
            strCIFBDMUserId = intent.getStringExtra("strBDMCifCOde");
            strCIFBDMEmailId = intent.getStringExtra("strEmailId");
            strCIFBDMMObileNo = intent.getStringExtra("strMobileNo");

            try {
                //strCIFBDMPassword = SimpleCrypto.encrypt("SBIL", "sbil");
                strCIFBDMPassword = mCommonMethods.getStrAuth();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (fromHome != null && fromHome.equalsIgnoreCase("PIWC")) {
            lnPIWCProposerTracker.setVisibility(View.GONE);
            edPIWCProposalNo_text = intent.getStringExtra("ProposalNumber");
            getUserDetails();
            if (mCommonMethods.isNetworkConnected(context)) {
                service_hits();
            } else {
                mCommonMethods.showMessageDialog(context, mCommonMethods.NO_INTERNET_MESSAGE);
            }
        } else {
            getUserDetails();
        }
    }

    private void getUserDetails() {
        UserDetailsValuesModel userDetailsValuesModel = mCommonMethods.setUserDetails(context);
        strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        //strCIFBDMPassword = userDetailsValuesModel.getStrCIFBDMPassword();
        strCIFBDMPassword = mCommonMethods.getStrAuth();
        strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();

    }

    private void service_hits() {
        ServiceHits service = new ServiceHits(context,
                METHOD_NAME_PIWC_TRACKER, edPIWCProposalNo_text, strCIFBDMUserId,
                strCIFBDMEmailId, strCIFBDMMObileNo,
                strCIFBDMPassword, this);
        service.execute();

    }


    class DownloadFileAsyncPIWCTracker extends
            AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String proposalTrackerNO = "", paymentAmount = "",
                cashieringDate = "", policyHolderName = "",
                mainReason = "", subReason = "",
                preCallPlDate = "", strPIWCTrackerErrorCOde = "", inputpolicylist = "", mobileNumber = "";
        String PIWCTrackerStatus = "", remarks = "";
        private TextView txtPIWCTrackerProposalNo,
                txtPIWCTrackerPolicyHolderName, txtPIWCTrackerPremiumAmount,
                txtPIWCTrackerCashieredDate, txtPIWCTrackerLatestCallStatus,
                txtPIWCTrackerLatestCallSubStatus,
                txtPIWCTrackerLatestCallDate, txtPIWCTrackerRemarks, textviewMobileNumber;
        private TableLayout tblPIWCProposarTrackerStatus;
        private ImageView imgcontact_cust_r;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
            tblPIWCProposarTrackerStatus = findViewById(R.id.tblPIWCProposarTrackerStatus);
            txtPIWCTrackerProposalNo = findViewById(R.id.txtPIWCTrackerProposalNo);
            txtPIWCTrackerPolicyHolderName = findViewById(R.id.txtPIWCTrackerPolicyHolderName);
            txtPIWCTrackerPremiumAmount = findViewById(R.id.txtPIWCTrackerPremiumAmount);
            txtPIWCTrackerCashieredDate = findViewById(R.id.txtPIWCTrackerCashieredDate);
            txtPIWCTrackerLatestCallStatus = findViewById(R.id.txtPIWCTrackerLatestCallStatus);
            txtPIWCTrackerLatestCallSubStatus = findViewById(R.id.txtPIWCTrackerLatestCallSubStatus);
            txtPIWCTrackerLatestCallDate = findViewById(R.id.txtPIWCTrackerLatestCallDate);
            txtPIWCTrackerRemarks = findViewById(R.id.txtPIWCTrackerRemarks);
            textviewMobileNumber = findViewById(R.id.textviewMobileNumber);
            imgcontact_cust_r = findViewById(R.id.imgcontact_cust_r);

            imgcontact_cust_r.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String mobileNumber = textviewMobileNumber.getText().toString();

                    if (!TextUtils.isEmpty(mobileNumber)) {
                        mCommonMethods.callMobileNumber(mobileNumber, context);
                    }
                }
            });

            textviewMobileNumber.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String mobileNumber = textviewMobileNumber.getText().toString();

                    if (!TextUtils.isEmpty(mobileNumber)) {
                        mCommonMethods.callMobileNumber(mobileNumber, context);
                    }
                }
            });

        }

        @Override
        protected String doInBackground(String... aurl) {
            try {
                System.out.println("doinback piwc");
                running = true;
                SoapObject request;


                String NAMESPACE = "http://tempuri.org/";
                request = new SoapObject(NAMESPACE, METHOD_NAME_PIWC_TRACKER);
                request.addProperty("strProposalNo", edPIWCProposalNo_text);
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
                mCommonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                String URl = ServiceURL.SERVICE_URL;
                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                try {

                    String SOAP_ACTION_PIWC_TRACKER = "http://tempuri.org/getPIWCStatus";
                    androidHttpTranport
                            .call(SOAP_ACTION_PIWC_TRACKER, envelope);
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

                                    inputpolicylist = prsObj.parseXmlTag(
                                            inputpolicylist, "Table");
                                    strPIWCTrackerErrorCOde = inputpolicylist;
                                    System.out
                                            .println("strPIWCTrackerErrorCOde:"
                                                    + strPIWCTrackerErrorCOde);

                                    if (strPIWCTrackerErrorCOde != null) {
                                        // inputpolicylist = sa.toString();
                                        proposalTrackerNO = new ParseXML()
                                                .parseXmlTag(inputpolicylist,
                                                        "PL_PROP_NUM");

                                        policyHolderName = new ParseXML()
                                                .parseXmlTag(inputpolicylist,
                                                        "PR_FULL_NM");

                                        mainReason = new ParseXML()
                                                .parseXmlTag(inputpolicylist,
                                                        "PL_CALL_DESC");

                                        subReason = new ParseXML().parseXmlTag(
                                                inputpolicylist, "CONT_STAT");

                                        cashieringDate = new ParseXML()
                                                .parseXmlTag(inputpolicylist,
                                                        "REC_ADD_DT");

                                        preCallPlDate = new ParseXML()
                                                .parseXmlTag(inputpolicylist,
                                                        "PL_CALL_DT");

                                        paymentAmount = new ParseXML()
                                                .parseXmlTag(inputpolicylist,
                                                        "PA_CHEQUE_AMT");

                                        PIWCTrackerStatus = new ParseXML()
                                                .parseXmlTag(inputpolicylist,
                                                        "STATUS");
                                        remarks = new ParseXML()
                                                .parseXmlTag(inputpolicylist,
                                                        "REMARKS");

                                        mobileNumber = new ParseXML()
                                                .parseXmlTag(inputpolicylist,
                                                        "PROPOSER_MOBILE");
                                        mobileNumber = mobileNumber == null ? "" : mobileNumber;
                                        if (mainReason == null) {
                                            mainReason = "";
                                        }
                                        if (subReason == null) {
                                            subReason = "";
                                        }

                                        if (preCallPlDate == null) {
                                            preCallPlDate = "";
                                        }

                                        strPIWCTrackerErrorCOde = "success";

                                    }

                                } else {
                                    strPIWCTrackerErrorCOde = "0";
                                }
                            } else {
                                strPIWCTrackerErrorCOde = "1";
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

                } catch (IOException e) {
                    try {
                        throw (e);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    mProgressDialog.dismiss();
                    running = false;
                } catch (XmlPullParserException e) {
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
                if (strPIWCTrackerErrorCOde != null) {
                    // txterrordescpolicydetail.setText("");
                    /*
                     * proposalTrackerNO = "",paymentAmount = "", cashieringDate
                     * = "",policyHolderName = "" , PIWCTrackerStatus = "",
                     * mainReason = "",subReason = "",preCallPlDate = "";
                     */
                    if (strPIWCTrackerErrorCOde.equalsIgnoreCase("success")) {
                        txtPIWCTrackerProposalNo.setText(proposalTrackerNO);
                        txtPIWCTrackerPolicyHolderName
                                .setText(policyHolderName);
                        txtPIWCTrackerPremiumAmount.setText(paymentAmount);
                        txtPIWCTrackerCashieredDate.setText(cashieringDate);
                        txtPIWCTrackerLatestCallDate.setText(preCallPlDate);
                        txtPIWCTrackerLatestCallStatus.setText(mainReason);
                        txtPIWCTrackerLatestCallSubStatus.setText(subReason);

                        txtPIWCTrackerRemarks.setText(remarks);
                        textviewMobileNumber.setText(mobileNumber);
                        tblPIWCProposarTrackerStatus.setVisibility(View.VISIBLE);
                    } else if (strPIWCTrackerErrorCOde.equalsIgnoreCase("1")) {
                        Toast.makeText(getApplicationContext(),
                                "You are not authorised user",
                                Toast.LENGTH_LONG).show();
                    } else {
                        //servererror();
                        mCommonMethods.showMessageDialog(context, mCommonMethods.SERVER_ERROR);
                        setPIWCStatusToBlank();
                    }
                } else {
                    //servererror();
                    mCommonMethods.showMessageDialog(context, mCommonMethods.SERVER_ERROR);
                    setPIWCStatusToBlank();
                }
            } else {
                // servererror();
                Toast.makeText(getApplicationContext(),
                        "Proposal Number is not valid", Toast.LENGTH_LONG)
                        .show();
                setPIWCStatusToBlank();
            }
        }

        void setPIWCStatusToBlank() {

            txtPIWCTrackerProposalNo.setText("");
            txtPIWCTrackerPolicyHolderName.setText("");
            txtPIWCTrackerPremiumAmount.setText("");
            txtPIWCTrackerCashieredDate.setText("");
            txtPIWCTrackerLatestCallDate.setText("");
            txtPIWCTrackerLatestCallStatus.setText("");
            txtPIWCTrackerLatestCallSubStatus.setText("");

			/*tblPIWCProposarTrackerStatus.getLayoutParams().height = 0;
			tblPIWCProposarTrackerStatus.requestLayout();*/
            tblPIWCProposarTrackerStatus.setVisibility(View.GONE);

        }
    }

    @SuppressWarnings("deprecation")
    private void dismissProgressDialog() {
        dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
    }

    @SuppressWarnings("deprecation")
    private void showProgressDialog() {
        showDialog(DIALOG_DOWNLOAD_PROGRESS);
    }


    @Override
    public void downLoadData() {
        taskPiwcTracker = new DownloadFileAsyncPIWCTracker();
        taskPiwcTracker.execute("demo");
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_PIWC_ok) {
            getPIWC();
        }

    }

    private void getPIWC() {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edPIWCProposalNo.getWindowToken(), 0);

        taskPiwcTracker = new DownloadFileAsyncPIWCTracker();
        edPIWCProposalNo_text = edPIWCProposalNo.getText().toString();
        if (edPIWCProposalNo_text.equalsIgnoreCase("")) {
            mCommonMethods.showMessageDialog(context, mCommonMethods.PROPOSAL_EMPTY_ALERT);
        } else {

            if (mCommonMethods.isNetworkConnected(context)) {
                service_hits();
            } else {
                mCommonMethods.showMessageDialog(context, mCommonMethods.NO_INTERNET_MESSAGE);
            }
        }
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
                                if (taskPiwcTracker != null) {
                                    taskPiwcTracker.cancel(true);
                                }
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


            if (taskPiwcTracker != null) {
                taskPiwcTracker.cancel(true);
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
}
