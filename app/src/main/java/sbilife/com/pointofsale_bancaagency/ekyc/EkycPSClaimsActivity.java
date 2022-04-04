package sbilife.com.pointofsale_bancaagency.ekyc;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.Calendar;
import java.util.Date;

import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.Verhoeff;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

public class EkycPSClaimsActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;
    private CommonMethods mCommonMethods;
    private DatabaseHelper db;

    private LinearLayout ll_proposal_details;
    private EditText edt_ekyc_proposal_no, edt_ekyc_customer_name, edt_ekyc_client_id, edt_ekyc_plan_name,
            edt_ekyc_policy_status, edt_ekyc_aadhar_no, edt_ekyc_pan_card_no, edt_ekyc_name_as_aadhar;
    private Button btn_ekyc_varify_proposal_no, btn_ekyc_proceed;

    private String str_proposal_no = "", str_cust_name = "", str_client_id = "", str_plan_name = "",
            str_policy_status = "", str_aashaar_no = "", str_pan_card_no = "", str_mode = "Ekyc",
            str_ap_flag = "", str_name_as_aadhar = "";
    private  final String NAMESPACE = "http://tempuri.org/";

    private  final String SOAP_ACTION_EKYC_GET_DETAILS = "http://tempuri.org/getCustDetail_SMRT";
    private  final String METHOD_NAME_EKYC_GET_DETAILS = "getCustDetail_SMRT";

    private  final String SOAP_ACTION_SAVE_EKYC_DETAILS = "http://tempuri.org/saveeKYCDetail_SMRT";
    private  final String METHOD_NAME_SAVE_EKYC_DETAILS = "saveeKYCDetail_SMRT";

    public  final int DIALOG_PROGRESS = 0;
    private ProgressDialog mProgressDialog;
    private AsynchVerifyProposalNo mAsynchVerifyProposalNo;
    private AsynchSave_eKYC_Details mAsynchSave_eKYC_details;

    private boolean validate_aadhar = false, validate_pan_card = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_ekyc_psclaims);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        initialise();
    }

   /* @Override
    public void onBackPressed() {
        Intent i = new Intent(mContext,CarouselHomeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }*/

    public void initialise() {

        mContext = this;
        mCommonMethods = new CommonMethods();

        db = new DatabaseHelper(mContext);

        mCommonMethods.setApplicationToolbarMenu(this, "Ekyc for PS and Claims");

        str_proposal_no = getIntent().getStringExtra("POLICY_NUM");

        ll_proposal_details = findViewById(R.id.ll_proposal_details);
        edt_ekyc_proposal_no = findViewById(R.id.edt_ekyc_proposal_no);
        edt_ekyc_customer_name = findViewById(R.id.edt_ekyc_customer_name);
        edt_ekyc_client_id = findViewById(R.id.edt_ekyc_client_id);
        edt_ekyc_plan_name = findViewById(R.id.edt_ekyc_plan_name);
        edt_ekyc_policy_status = findViewById(R.id.edt_ekyc_policy_status);
        edt_ekyc_aadhar_no = findViewById(R.id.edt_ekyc_aadhar_no);
        edt_ekyc_pan_card_no = findViewById(R.id.edt_ekyc_pan_card_no);
        edt_ekyc_name_as_aadhar = findViewById(R.id.edt_ekyc_name_as_aadhar);

        btn_ekyc_varify_proposal_no = findViewById(R.id.btn_ekyc_varify_proposal_no);
        btn_ekyc_varify_proposal_no.setOnClickListener(this);

        btn_ekyc_proceed = findViewById(R.id.btn_ekyc_proceed);
        btn_ekyc_proceed.setOnClickListener(this);

        if (!str_proposal_no.equals("")){
            edt_ekyc_proposal_no.setText(str_proposal_no);

            btn_ekyc_varify_proposal_no.setVisibility(View.GONE);

            mAsynchVerifyProposalNo = new AsynchVerifyProposalNo();
            mAsynchVerifyProposalNo.execute();
        }else
            btn_ekyc_varify_proposal_no.setVisibility(View.VISIBLE);

        edt_ekyc_aadhar_no.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                str_aashaar_no = edt_ekyc_aadhar_no.getText().toString();
                if (str_aashaar_no.length() == 12 || str_aashaar_no.length() == 16){
                    if (!Verhoeff.validateVerhoeff(str_aashaar_no)) {
                        if (str_aashaar_no.length() == 12)
                        edt_ekyc_aadhar_no.setError("Incorrect Aadhaar Number");
                        else
                            edt_ekyc_aadhar_no.setError("Incorrect Virtual ID");

                        validate_aadhar = false;
                    } else {
                        //edt_ekyc_aadhar_no.setError(null);
                        validate_aadhar = true;
                    }
                }else if (str_aashaar_no.length() > 12){
                    edt_ekyc_aadhar_no.setError("Incorrect Virtual ID");
                    validate_aadhar = false;
                }else{
                    edt_ekyc_aadhar_no.setError("Incorrect Aadhaar Number");
                    validate_aadhar = false;
                }
            }
        });

        edt_ekyc_pan_card_no.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                str_pan_card_no = edt_ekyc_pan_card_no.getText().toString().trim();
                validate_pan_card = mCommonMethods.valPancard(str_pan_card_no, edt_ekyc_pan_card_no);
            }
        });
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_ekyc_varify_proposal_no:
                verifyProposalNum();
                break;

            case R.id.btn_ekyc_proceed:
                onProceedClick();
                break;

            default:
                break;
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {

        switch (id) {
            case DIALOG_PROGRESS:
                mProgressDialog = new ProgressDialog(this,
                        ProgressDialog.THEME_HOLO_LIGHT);
                String Message = "Loading Please wait...";
                mProgressDialog.setMessage(Html
                        .fromHtml("<font color='#00a1e3'><b>" + Message
                                + "<b></font>"));
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                return mProgressDialog;

            default:
                return null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mAsynchVerifyProposalNo != null)
            mAsynchVerifyProposalNo.cancel(true);

        if (mAsynchSave_eKYC_details != null)
            mAsynchSave_eKYC_details.cancel(true);

    }

    private void verifyProposalNum() {

        str_proposal_no = edt_ekyc_proposal_no.getText().toString().trim();

        if (str_proposal_no != null && !str_proposal_no.equals("")) {
            mAsynchVerifyProposalNo = new AsynchVerifyProposalNo();
            mAsynchVerifyProposalNo.execute();
        } else {
            mCommonMethods.showToast(mContext, "Please Enter Proposal Number");
        }
    }

    public void onProceedClick() {

        str_proposal_no = edt_ekyc_proposal_no.getText().toString().trim();
        str_client_id = edt_ekyc_client_id.getText().toString().trim();
        str_plan_name = edt_ekyc_plan_name.getText().toString().trim();
        str_policy_status = edt_ekyc_policy_status.getText().toString().trim();
        str_aashaar_no = edt_ekyc_aadhar_no.getText().toString().trim();
        str_pan_card_no = edt_ekyc_pan_card_no.getText().toString().trim();
        str_name_as_aadhar = edt_ekyc_name_as_aadhar.getText().toString();

        String str_val = valid_deatails();
        if (str_val.equals("")) {

            //save details
            ContentValues cv = new ContentValues();
            cv.put(db.EKYC_PS_CLAIMS_USER_ID, mCommonMethods.GetUserID(mContext));
            cv.put(db.EKYC_PS_CLAIMS_USER_TYPE, mCommonMethods.GetUserType(mContext));
            cv.put(db.EKYC_PS_CLAIMS_PROPOSAL_NUMBER, str_proposal_no);
            cv.put(db.EKYC_PS_CLAIMS_CUSTOMER_NAME, str_cust_name);
            cv.put(db.EKYC_PS_CLAIMS_CLIENT_ID, str_client_id);
            cv.put(db.EKYC_PS_CLAIMS_PLAN_NAME, str_plan_name);
            cv.put(db.EKYC_PS_CLAIMS_POLICY_STATUS, str_policy_status);
            cv.put(db.EKYC_PS_CLAIMS_AADHAAR_NUMBER, str_aashaar_no);
            cv.put(db.EKYC_PS_CLAIMS_PAN, str_pan_card_no);
            cv.put(db.EKYC_PS_CLAIMS_AP_FLAG, str_ap_flag);
            cv.put(db.EKYC_PS_CLAIMS_MODE, str_mode);
            cv.put(db.EKYC_PS_CLAIMS_EKYC_MODE, "");
            cv.put(db.EKYC_PS_CLAIMS_EKYC_RESULT, "");
            cv.put(db.EKYC_PS_CLAIMS_IS_DELETE, "0");

            Date currentTime = Calendar.getInstance().getTime();
            String str_date = String.valueOf(currentTime.getTime());
            cv.put(db.EKYC_PS_CLAIMS_DATE_TIME, str_date);

            cv.put(db.EKYC_PS_CLAIMS_EKYC_STATUS, 0);

            db.insert_eKYC_PS_Claims_Details(cv);

            //call service
            /*mAsynchSave_eKYC_details = new AsynchSave_eKYC_Details();
            mAsynchSave_eKYC_details.execute();*/

            Intent intent = new Intent(EkycPSClaimsActivity.this, eKYCActivity.class);
            intent.putExtra("ProposalNumber", str_proposal_no);
            intent.putExtra("CustomerName", str_cust_name);
            intent.putExtra("CustomerAadharNumber", str_aashaar_no);
            intent.putExtra("CustomerAadharName", str_name_as_aadhar);
            intent.putExtra("PlanName", str_plan_name);
            intent.putExtra("FROM", "PS_CLAIM");
            startActivity(intent);

        } else {
            mCommonMethods.showMessageDialog(mContext, str_val);
        }
    }

    private String valid_deatails() {

        String str_result = "";

        if (str_client_id.equals("")) {
            str_result = "Invalid Proposal Number";
        }else{
            if (str_proposal_no.equals("")) {
                str_result = "Please Enter Proposal Number\n";
            }
            if (!validate_aadhar) {
                str_result = "Please Enter Aadhaar Number\n";
            }
            if (!validate_pan_card) {
                str_result = "Please Enter Pan Card Number\n";
            }
            if (str_name_as_aadhar.equals("")){
                str_result = "Please Enter Name As Aadhar\n";
            }
        }

        return str_result;
    }

    public class AsynchVerifyProposalNo extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String str_output = "";

        @Override
        protected void onPreExecute() {
            showDialog(DIALOG_PROGRESS);
        }

        @Override
        protected String doInBackground(String... params) {

            try {

                running = true;
                SoapObject request = null;

                request = new SoapObject(NAMESPACE, METHOD_NAME_EKYC_GET_DETAILS);
                request.addProperty("strProposalNo", str_proposal_no);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                mCommonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL);
                try {

                    androidHttpTranport.call(SOAP_ACTION_EKYC_GET_DETAILS, envelope);
                    //Object response = envelope.getResponse();

                    SoapPrimitive sa = (SoapPrimitive) envelope.getResponse();

                    str_output = sa.toString();

                    if (str_output.equals("1") || str_output.equals("2")) {
                        return str_output;
                    }else {
                        ParseXML prsObj = new ParseXML();
                        str_output = prsObj.parseXmlTag(
                                str_output, "ReqDtls");
                        str_output = prsObj.parseXmlTag(
                                str_output, "Table");

                        str_cust_name = prsObj.parseXmlTag(
                                str_output, "FULLNAME");

                        str_client_id = prsObj.parseXmlTag(
                                str_output, "HOLDERID");

                        str_plan_name = prsObj.parseXmlTag(
                                str_output, "PRODUCTPLAN");

                        str_policy_status = prsObj.parseXmlTag(
                                str_output, "POLICYCURRENTSTATUS");

                        /*str_aashaar_no = prsObj.parseXmlTag(
                                str_output, "AADHARNO");*/
                        str_aashaar_no = "";

                        str_pan_card_no = prsObj.parseXmlTag(
                                str_output, "PANNUM");

                        if (str_pan_card_no == null)
                            str_pan_card_no = "";
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
        protected void onPostExecute(String s) {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();

            if (running) {

                if (str_output.equals("1")) {
                    ll_proposal_details.setVisibility(View.GONE);

                    mCommonMethods.showMessageDialog(mContext, "Invalid Proposal Number!!");
                } else if (str_output.equals("2")){
                    ll_proposal_details.setVisibility(View.GONE);
                    btn_ekyc_varify_proposal_no.setVisibility(View.VISIBLE);

                    mCommonMethods.showMessageDialog(mContext, "Proposal/Policy Already Linked with Aadhar!!");
                }else {

                    ll_proposal_details.setVisibility(View.VISIBLE);
                    //set data to respective fields
                    edt_ekyc_customer_name.setText(str_cust_name);
                    edt_ekyc_client_id.setText(str_client_id);
                    edt_ekyc_plan_name.setText(str_plan_name);
                    edt_ekyc_policy_status.setText(str_policy_status);

                    validate_pan_card = mCommonMethods.valPancard(str_pan_card_no, edt_ekyc_pan_card_no);

                    //for AP FLAG
                    if (!str_aashaar_no.equals("") && validate_pan_card){
                        edt_ekyc_aadhar_no.setText(str_aashaar_no);
                        validate_aadhar = true;

                        edt_ekyc_pan_card_no.setText(str_pan_card_no);
                        edt_ekyc_pan_card_no.setEnabled(false);
                        validate_pan_card = true;

                        str_ap_flag = "AP";
                    }else if (str_aashaar_no.equals("") && validate_pan_card){
                        edt_ekyc_aadhar_no.setText("");
                        edt_ekyc_aadhar_no.requestFocus();
                        validate_aadhar = false;

                        edt_ekyc_pan_card_no.setText(str_pan_card_no);
                        edt_ekyc_pan_card_no.setEnabled(false);
                        validate_pan_card = true;

                        str_ap_flag = "P";
                    }else if (!str_aashaar_no.equals("") && !validate_pan_card){

                        edt_ekyc_aadhar_no.setText(str_aashaar_no);
                        validate_aadhar = true;

                        edt_ekyc_pan_card_no.setText(str_pan_card_no);
                        edt_ekyc_pan_card_no.setEnabled(true);
                        edt_ekyc_pan_card_no.requestFocus();
                        validate_pan_card = false;

                        str_ap_flag = "A";
                    }else if (str_aashaar_no.equals("") && !validate_pan_card){

                        edt_ekyc_aadhar_no.setText("");
                        edt_ekyc_aadhar_no.requestFocus();
                        validate_aadhar = false;

                        edt_ekyc_pan_card_no.setText(str_pan_card_no);
                        edt_ekyc_pan_card_no.setEnabled(true);
                        validate_pan_card = false;

                        str_ap_flag = "A";
                    }

                    running = false;
                    str_output = "";
                }
            } else {
                ll_proposal_details.setVisibility(View.GONE);

                mCommonMethods.showMessageDialog(mContext, mCommonMethods.SERVER_ERROR);
            }
        }
    }

    public class AsynchSave_eKYC_Details extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String str_output = "";

        @Override
        protected void onPreExecute() {
            showDialog(DIALOG_PROGRESS);
        }

        @Override
        protected String doInBackground(String... params) {

            try {

                running = true;
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_SAVE_EKYC_DETAILS);
                request.addProperty("strPoicyNo", str_proposal_no);
                request.addProperty("strCustName", str_cust_name);
                request.addProperty("strClientID", str_client_id);
                request.addProperty("strPlanName", str_plan_name);
                request.addProperty("strPolicyStatus", str_policy_status);
                request.addProperty("strAadharNo", str_aashaar_no);
                request.addProperty("strType", str_mode);
                request.addProperty("strMode", "");
                request.addProperty("strCode", mCommonMethods.GetUserID(mContext));

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                mCommonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL);
                try {

                    androidHttpTranport.call(SOAP_ACTION_SAVE_EKYC_DETAILS, envelope);
                    //Object response = envelope.getResponse();

                    SoapPrimitive sa = (SoapPrimitive) envelope.getResponse();

                    str_output = sa.toString();

                    return str_output;

                } catch (Exception e) {
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
        protected void onPostExecute(String s) {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();

            if (running) {

                if (str_output.equals("1")) {
                    mCommonMethods.showToast(mContext, "Data Saved Successfully!!");

                    //navigate to ekyc

                    Intent intent = new Intent(EkycPSClaimsActivity.this, eKYCActivity.class);
                    intent.putExtra("ProposalNumber", str_proposal_no);
                    intent.putExtra("CustomerName", str_cust_name);
                    intent.putExtra("CustomerAadharNumber", str_aashaar_no);
                    intent.putExtra("CustomerAadharName", str_name_as_aadhar);
                    intent.putExtra("PlanName", str_plan_name);
                    intent.putExtra("FROM", "PS_CLAIM");
                    startActivity(intent);

                    //clear_all();

                } else {
                    mCommonMethods.showToast(mContext, "Data Not Synched!!");
                }
                running = false;
                str_output = "";

            } else {

                mCommonMethods.showMessageDialog(mContext, mCommonMethods.SERVER_ERROR);
            }
        }
    }

    private void clear_all() {

        edt_ekyc_proposal_no.setText("");
        ll_proposal_details.setVisibility(View.GONE);
        edt_ekyc_customer_name.setText("");
        edt_ekyc_client_id.setText("");
        edt_ekyc_plan_name.setText("");
        edt_ekyc_policy_status.setText("");
        edt_ekyc_aadhar_no.setText("");
        edt_ekyc_aadhar_no.setEnabled(true);

        str_mode = "";
    }

}
