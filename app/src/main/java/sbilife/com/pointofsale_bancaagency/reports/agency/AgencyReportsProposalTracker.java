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
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.ArrayList;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.ParseXML.XMLProposerTrackerList;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods.UserDetailsValuesModel;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits.DownLoadData;

public class AgencyReportsProposalTracker extends AppCompatActivity implements
        DownLoadData {

    private Context mContext;
    private CommonMethods commonMethods;

    private final String NAMESPACE = "http://tempuri.org/";
    private final String URl = ServiceURL.SERVICE_URL;

    private final String METHOD_NAME_PROPOSAL_TRACKER = "getProposalStatus";

    private DownloadFileAsyncProposalTracker taskProposalTracker;

    private final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private ProgressDialog mProgressDialog;

    private TextView txterrordescproposaltracker, txtProposalTrackerProposalNo, txtProposalTrackerStatus,
            txtProposalTrackerReason, txtProposalTrackerPolicyHolderName,
            txtProposalTrackerLifeAssuredName, txtProposalMobileNumber;

    private TableLayout tblProposarTrackerStatus;
    private Button btn_proposal_ok;
    private EditText edProposalTrackerNo;

    private String strProposalTrackerErrorCOde = "";
    private String inputpolicylist;
    private String strCIFBDMUserId = "";
    private String strCIFBDMEmailId = "";
    private String strCIFBDMPassword = "";
    private String strCIFBDMMObileNo = "";

    private ArrayList<XMLProposerTrackerList> lstProposerTrackerXml;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.banca_reports_proposal_tracker);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        mProgressDialog = new ProgressDialog(this);
        // initialization
        initialise();
        commonMethods.setApplicationToolbarMenu(this, "Proposal Status Tracker");

        btn_proposal_ok.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                getProposalDetails();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (taskProposalTracker != null) {
                taskProposalTracker.cancel(true);
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

    private void initialise() {

        mContext = this;
        commonMethods = new CommonMethods();
        mProgressDialog = new ProgressDialog(this);
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

        edProposalTrackerNo = findViewById(R.id.edProposalTrackerNo);
        btn_proposal_ok = findViewById(R.id.btn_proposal_ok);
        tblProposarTrackerStatus = findViewById(R.id.tblProposarTrackerStatus);

        txtProposalTrackerProposalNo = findViewById(R.id.txtProposalTrackerProposalNo);
        txtProposalTrackerStatus = findViewById(R.id.txtProposalTrackerStatus);
        txtProposalTrackerReason = findViewById(R.id.txtProposalTrackerReason);

        txtProposalTrackerPolicyHolderName = findViewById(R.id.txtProposalTrackerPolicyHolderName);
        txtProposalTrackerLifeAssuredName = findViewById(R.id.txtProposalTrackerLifeAssuredName);
        txterrordescproposaltracker = findViewById(R.id.txterrordescproposaltracker);

        txtProposalMobileNumber = findViewById(R.id.txtProposalMobileNumber);
    }

    private void getUserDetails() {
        UserDetailsValuesModel userDetailsValuesModel = commonMethods.setUserDetails(mContext);
        strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        strCIFBDMPassword = userDetailsValuesModel.getStrCIFBDMPassword();
        strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();
        commonMethods.printLog("User Details", "strCIFBDMUserId:" + strCIFBDMUserId
                + " strCIFBDMEmailId:" + strCIFBDMEmailId
                + " strCIFBDMPassword:" + strCIFBDMPassword
                + " strCIFBDMMObileNo:" + strCIFBDMMObileNo);
    }

	/*@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		commonMethods.printLog("onClick", "proposal ok");

		switch (v.getId()) {
		case R.id.btn_proposal_ok:
			getProposalDetails();
			break;
		}

	}*/

    private void getProposalDetails() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edProposalTrackerNo.getWindowToken(), 0);

        taskProposalTracker = new DownloadFileAsyncProposalTracker();

        txterrordescproposaltracker.setVisibility(View.GONE);

        // strPolicyDetailcifno =
        // edpolicydetailCifNo.getText().toString();
        // strPolicyDetailpolicyno = edpolicyno.getText().toString();

        if (edProposalTrackerNo.getText().toString().equalsIgnoreCase("")) {
            commonMethods.showMessageDialog(mContext,
                    "Please Enter Proposal No..");
        } else {

            if (commonMethods.isNetworkConnected(mContext)) {
                // startDownloadPolicyDetail();
                // String strUType = GetUserType();
                txtProposalTrackerProposalNo.setText("");
                txtProposalTrackerStatus.setText("");
                txtProposalTrackerLifeAssuredName.setText("");
                txtProposalTrackerPolicyHolderName.setText("");
                txtProposalMobileNumber.setText("");
                tblProposarTrackerStatus.setVisibility(View.GONE);
                service_hits(METHOD_NAME_PROPOSAL_TRACKER);
            } else {
                commonMethods.showMessageDialog(mContext,
                        commonMethods.NO_INTERNET_MESSAGE);
            }
        }
    }

    private void service_hits(String strServiceName) {

        ServiceHits service = new ServiceHits(mContext,
                strServiceName, "", strCIFBDMUserId,
                strCIFBDMEmailId, strCIFBDMMObileNo,
                strCIFBDMPassword, this);
        service.execute();
    }

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
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

                                if (taskProposalTracker != null) {
                                    taskProposalTracker.cancel(true);
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

            default:
                return null;
        }
    }

    private void startDownloadProposalTracker() {
        taskProposalTracker.execute("demo");
    }

    class DownloadFileAsyncProposalTracker extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        /*
         * private String proposalTrackerStatus = "",proposalTrackerReason = "",
         * proposalTrackerNO = "";
         */

        String edProposalTrackerNo_text;

        @Override
        protected void onPreExecute() {
            showProgressDialog();
            edProposalTrackerNo_text = edProposalTrackerNo.getText().toString();
        }

        @Override
        protected String doInBackground(String... aurl) {
            try {
                running = true;
                SoapObject request;

                //String UserType = commonMethods.GetUserType(mContext);

                // string strCode, string strEmailId, string strMobileNo, string
                // strAuthKey
                request = new SoapObject(NAMESPACE, METHOD_NAME_PROPOSAL_TRACKER);
                request.addProperty("strProposalNo", edProposalTrackerNo_text);
                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);
                request.addProperty("strAuthKey", strCIFBDMPassword.trim());
                //request.addProperty("strCode", strCIFBDMUserId);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                // allowAllSSL();
                commonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                try {

                    String SOAP_ACTION_PROPOSAL_TRACKER = "http://tempuri.org/getProposalStatus";
                    androidHttpTranport.call(SOAP_ACTION_PROPOSAL_TRACKER, envelope);
                    Object response = envelope.getResponse();
                    System.out.println("response:" + response.toString());
                    if (!response.toString().contentEquals("anyType{}")) {

                        SoapPrimitive sa;
                        try {
                            sa = (SoapPrimitive) envelope.getResponse();

                            inputpolicylist = sa.toString();

                            if (!sa.toString().equalsIgnoreCase("1")) {
                                ParseXML prsObj = new ParseXML();

                                // <ReqDtls><Table1><Status>Cancelled</Status><ProposalNo>53NA062275</ProposalNo>
                                // <Reason>Sent for
                                // Cancel/Refund</Reason></Table1></ReqDtls>

                                inputpolicylist = prsObj.parseXmlTag(inputpolicylist, "ReqDtls");

                                if (inputpolicylist != null) {

                                    /*
                                     * <ReqDtls> <Table>
                                     * <PL_PROP_NUM>45YA034636</PL_PROP_NUM>
                                     * <PR_FULL_NM>ABDUL ASAD ABDUL SALAM
                                     * SHAIKH</PR_FULL_NM>
                                     * <IR_ROLE>Proposer</IR_ROLE>
                                     * <PropStatus>Cancelled</PropStatus>
                                     * <Reason>Not Taken Up</Reason> </Table>
                                     *
                                     * <Table>
                                     * <PL_PROP_NUM>45YA034636</PL_PROP_NUM>
                                     * <PR_FULL_NM>ABDUL ASAD ABDUL SALAM
                                     * SHAIKH</PR_FULL_NM> <IR_ROLE>
                                     * LifeAssured</IR_ROLE>
                                     * <PropStatus>Cancelled</PropStatus>
                                     * <Reason>Not Taken Up</Reason> </Table>
                                     * </ReqDtls></string>
                                     */
                                    lstProposerTrackerXml = new ArrayList<>();
                                    List<String> Node = prsObj.parseParentNode(inputpolicylist, "Table");

                                    List<XMLProposerTrackerList> nodeData = prsObj.parseNodeElement_Proposertracker(Node);

                                    lstProposerTrackerXml = new ArrayList<>();

                                    /*
                                     * if(node.getREQUIREMENTFLAG().equals(
                                     * "NON-MEDICAL"))
                                     * nonMedicallstPolicyList.add(node);
                                     */
                                    lstProposerTrackerXml.addAll(nodeData);

                                    strProposalTrackerErrorCOde = inputpolicylist;
                                    strProposalTrackerErrorCOde = "success";

                                } else {
                                    strProposalTrackerErrorCOde = "0";
                                }
                            } else {
                                strProposalTrackerErrorCOde = "1";

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
                closeProgressDialog();
            }
            if (running) {

                if (strProposalTrackerErrorCOde != null) {
                    // txterrordescpolicydetail.setText("");

                    if (strProposalTrackerErrorCOde.equalsIgnoreCase("success")) {

						 /*DownloadPropMobileNumberAsyncProposalTracker propMobileNumberAsyncProposalTracker = new DownloadPropMobileNumberAsyncProposalTracker();
						propMobileNumberAsyncProposalTracker.execute();*/

                        txtProposalTrackerProposalNo
                                .setText(lstProposerTrackerXml.get(0)
                                        .getProposerNumber());

                        String reason = lstProposerTrackerXml.get(0).getReason() == null ? ""
                                : lstProposerTrackerXml.get(0).getReason();

                        txtProposalTrackerReason.setText(reason);
                        txtProposalTrackerStatus.setText(lstProposerTrackerXml
                                .get(0).getStatus());
                        txtProposalTrackerLifeAssuredName
                                .setText(lstProposerTrackerXml.get(0)
                                        .getLifeAssuredName());
                        txtProposalTrackerPolicyHolderName
                                .setText(lstProposerTrackerXml.get(0)
                                        .getPolicyHolderName());
                        txtProposalMobileNumber.setText(lstProposerTrackerXml.get(0).getCONTACT_MOBILE());

                        tblProposarTrackerStatus.setVisibility(View.VISIBLE);
                    } else if (strProposalTrackerErrorCOde
                            .equalsIgnoreCase("1")) {
                        tblProposarTrackerStatus.setVisibility(View.GONE);

                        Toast.makeText(getApplicationContext(),
                                "Proposal Number is not valid",
                                Toast.LENGTH_LONG).show();
                    } else {
                        tblProposarTrackerStatus.setVisibility(View.GONE);
                        commonMethods.showMessageDialog(mContext, "Server Not Responding,Try again..");
                    }
                } else {
                    commonMethods.showMessageDialog(mContext, "Server Not Responding,Try again..");
                }
            } else {
                commonMethods.showMessageDialog(mContext, "Server Not Responding,Try again..");
            }
        }


    }

    class DownloadPropMobileNumberAsyncProposalTracker extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        /*
         * private String proposalTrackerStatus = "",proposalTrackerReason = "",
         * proposalTrackerNO = "";
         */
        String edProposalTrackerNo_text, mobile_number = "";

        @Override
        protected void onPreExecute() {
            showProgressDialog();
            edProposalTrackerNo_text = edProposalTrackerNo.getText().toString();
        }

        @Override
        protected String doInBackground(String... aurl) {
            try {
                running = true;
                SoapObject request;

                //String UserType = mCommonMethods.GetUserType(mContext);

                // string strCode, string strEmailId, string strMobileNo, string
                // strAuthKey
                String METHOD_NAME_GET_PROPOSAL_MOBILE_NUMBER = "getPropMobileNo";
                request = new SoapObject(NAMESPACE, METHOD_NAME_GET_PROPOSAL_MOBILE_NUMBER);
                request.addProperty("strProposalNo", edProposalTrackerNo_text);//
                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);
                request.addProperty("strAuthKey", strCIFBDMPassword.trim());
                //request.addProperty("strCode", strCIFBDMUserId);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                // allowAllSSL();
                commonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                try {

                    String SOAP_ACTION_PROPOSAL_MOBILE_NUMBER = "http://tempuri.org/getPropMobileNo";
                    androidHttpTranport.call(SOAP_ACTION_PROPOSAL_MOBILE_NUMBER, envelope);
                    Object response = envelope.getResponse();
                    System.out.println("response:" + response.toString());
                    if (!response.toString().contentEquals("anyType{}")) {

                        SoapPrimitive sa;
                        try {
                            sa = (SoapPrimitive) envelope.getResponse();

                            inputpolicylist = sa.toString();
                            mobile_number = inputpolicylist;
                            System.out.println("inputpolicylist = " + inputpolicylist);

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
                closeProgressDialog();
            }
            if (running) {

                if (strProposalTrackerErrorCOde != null) {
                    // txterrordescpolicydetail.setText("");

                    if (strProposalTrackerErrorCOde.equalsIgnoreCase("success")) {

                        txtProposalTrackerProposalNo
                                .setText(lstProposerTrackerXml.get(0)
                                        .getProposerNumber());
                        txtProposalTrackerReason.setText(lstProposerTrackerXml
                                .get(0).getReason());
                        txtProposalTrackerStatus.setText(lstProposerTrackerXml
                                .get(0).getStatus());
                        txtProposalTrackerLifeAssuredName
                                .setText(lstProposerTrackerXml.get(0)
                                        .getLifeAssuredName());
                        txtProposalTrackerPolicyHolderName
                                .setText(lstProposerTrackerXml.get(0)
                                        .getPolicyHolderName());
                        txtProposalMobileNumber.setText(mobile_number);

                        tblProposarTrackerStatus.getLayoutParams().height = LayoutParams.WRAP_CONTENT;
                        tblProposarTrackerStatus.setVisibility(View.VISIBLE);
                        tblProposarTrackerStatus.requestLayout();
                    } else if (strProposalTrackerErrorCOde
                            .equalsIgnoreCase("1")) {
                        tblProposarTrackerStatus.setVisibility(View.GONE);

                        Toast.makeText(getApplicationContext(),
                                "Proposal Number is not valid",
                                Toast.LENGTH_LONG).show();
                    } else {
                        tblProposarTrackerStatus.setVisibility(View.GONE);
                        commonMethods.showMessageDialog(mContext, "Server Not Responding,Try again..");
                        setProposalTrackerStatusToBlank();
                    }
                } else {
                    commonMethods.showMessageDialog(mContext, "Server Not Responding,Try again..");
                    setProposalTrackerStatusToBlank();
                }
            } else {
                commonMethods.showMessageDialog(mContext, "Server Not Responding,Try again..");
                setProposalTrackerStatusToBlank();
            }
        }

        void setProposalTrackerStatusToBlank() {

            txtProposalTrackerProposalNo.setText("");
            txtProposalTrackerReason.setText("");
            txtProposalTrackerStatus.setText("");
            txtProposalTrackerLifeAssuredName.setText("");
            txtProposalTrackerPolicyHolderName.setText("");

            tblProposarTrackerStatus.setVisibility(View.GONE);
            tblProposarTrackerStatus.requestLayout();

        }
    }

    @SuppressWarnings("deprecation")
    private void showProgressDialog() {
        showDialog(DIALOG_DOWNLOAD_PROGRESS);
    }

    @SuppressWarnings("deprecation")
    private void closeProgressDialog() {
        dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
    }

    @Override
    public void downLoadData() {
        // TODO Auto-generated method stub
        taskProposalTracker = new DownloadFileAsyncProposalTracker();
        startDownloadProposalTracker();
    }

}
