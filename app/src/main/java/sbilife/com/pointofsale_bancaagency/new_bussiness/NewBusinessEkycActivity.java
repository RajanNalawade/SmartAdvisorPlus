package sbilife.com.pointofsale_bancaagency.new_bussiness;

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
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.Calendar;
import java.util.Date;

import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.Verhoeff;
import sbilife.com.pointofsale_bancaagency.ekyc.eKYCActivity;
import sbilife.com.pointofsale_bancaagency.needanalysis.NeedAnalysisActivity;

public class NewBusinessEkycActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;
    private CommonMethods mCommonMethods;
    private DatabaseHelper db;
    private EditText edt_nb_ekyc_proposal_no, edittextConfirmProposal, edt_nb_ekyc_pan_card_no, edt_nb_ekyc_aadhar_no,
            edt_nb_ekyc_name_as_aadhar;

    private String str_proposal_no = "", str_pan_card_no = "", str_aashaar_no = "", str_name_as_on_aadhaar = "", str_ap_flag = "",
            str_from = "", str_urn_no = "", edittextFieldName = "";

    private boolean validate_pan_card, validate_aadhar;

    private final int DIALOG_PROGRESS = 0;
    private ProgressDialog mProgressDialog;
    private AsynchVerifyProposalNo mAsynchVerifyProposalNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_new_business_ekyc);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        initialise();
    }

    private void initialise() {

        mContext = this;
        mCommonMethods = new CommonMethods();

        db = new DatabaseHelper(mContext);

        mCommonMethods.setApplicationToolbarMenu(this, "Ekyc for New Business");

        edt_nb_ekyc_proposal_no = findViewById(R.id.edt_nb_ekyc_proposal_no);
        edittextConfirmProposal = findViewById(R.id.edittextConfirmProposal);
        edittextConfirmProposal.setTransformationMethod(new ChangeTransformationMethod());

        ImageButton ib_nb_ekyc_varify_proposal_no = findViewById(R.id.ib_nb_ekyc_varify_proposal_no);
        ib_nb_ekyc_varify_proposal_no.setOnClickListener(this);

        edt_nb_ekyc_pan_card_no = findViewById(R.id.edt_nb_ekyc_pan_card_no);

        edt_nb_ekyc_aadhar_no = findViewById(R.id.edt_nb_ekyc_aadhar_no);
        edt_nb_ekyc_aadhar_no.setTransformationMethod(new ChangeTransformationMethod());

        edt_nb_ekyc_name_as_aadhar = findViewById(R.id.edt_nb_ekyc_name_as_aadhar);

        Button btn_nb_ekyc_proceed = findViewById(R.id.btn_nb_ekyc_proceed);
        btn_nb_ekyc_proceed.setOnClickListener(this);

        LinearLayout ll_nb_proposal_no = findViewById(R.id.ll_nb_proposal_no);

        str_from = getIntent().getStringExtra("FROM");//Need Analysis(NA) or New Bussiness (NB)

        mAsynchVerifyProposalNo = new AsynchVerifyProposalNo();

        edt_nb_ekyc_pan_card_no.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                str_pan_card_no = edt_nb_ekyc_pan_card_no.getText().toString().trim();
                if (!str_pan_card_no.equals("")) {
                    validate_pan_card = mCommonMethods.valPancard(str_pan_card_no, edt_nb_ekyc_pan_card_no);
                } else {
                    edt_nb_ekyc_pan_card_no.setError(null);
                }
            }
        });

        edt_nb_ekyc_aadhar_no.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                edittextFieldName = "A";
                str_aashaar_no = edt_nb_ekyc_aadhar_no.getText().toString();
                if (str_aashaar_no.length() == 12 || str_aashaar_no.length() == 16) {
                    if (!Verhoeff.validateVerhoeff(str_aashaar_no)) {
                        if (str_aashaar_no.length() == 12)
                            edt_nb_ekyc_aadhar_no.setError("Incorrect Aadhaar Number");
                        else
                            edt_nb_ekyc_aadhar_no.setError("Incorrect Virtual ID");

                        validate_aadhar = false;
                    } else {
                        //edt_ekyc_aadhar_no.setError(null);
                        validate_aadhar = true;
                    }
                } else if (str_aashaar_no.length() > 12) {
                    edt_nb_ekyc_aadhar_no.setError("Incorrect Virtual ID");
                    validate_aadhar = false;
                } else {
                    edt_nb_ekyc_aadhar_no.setError("Incorrect Aadhaar Number");
                    validate_aadhar = false;
                }
            }
        });


        edittextConfirmProposal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                edittextFieldName = "P";
                validateConfirmProposalNumber();
            }
        });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.ib_nb_ekyc_varify_proposal_no:
                try {
                    IntentIntegrator integrator = new IntentIntegrator(NewBusinessEkycActivity.this);
                    integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                    integrator.setPrompt("Scan Code");
                    integrator.setCameraId(0);
                    integrator.setBeepEnabled(true);
                    integrator.setBarcodeImageEnabled(false);
                    integrator.initiateScan();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.btn_nb_ekyc_proceed:
                showEkycContent();
                break;

            default:
                break;

        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_PROGRESS:
                mProgressDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
                String Message = "Loading Please wait...";
                mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Log.d("MainActivity", "Cancelled");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();

            } else {
                Log.d("MainActivity", "Scanned");
                Toast.makeText(this, "Scanned: " + intentResult.getContents(), Toast.LENGTH_LONG).show();
                String capturedBarcodeValue = intentResult.getContents();
                edt_nb_ekyc_proposal_no.setText(capturedBarcodeValue);
            }
        }
    }

    private void showEkycContent() {
        String str_error = validateProposalDetail();
        if (str_error.equals("")) {
            //call service to verify proposal no. has done ekyc or not
            mAsynchVerifyProposalNo = new AsynchVerifyProposalNo();
            mAsynchVerifyProposalNo.execute();

        } else {
            mCommonMethods.showMessageDialog(mContext, str_error);
        }
    }

    private String validateProposalDetail() {

        String str_error = "", confirmProposal = "";
        str_proposal_no = edt_nb_ekyc_proposal_no.getText().toString().trim();
        confirmProposal = edittextConfirmProposal.getText().toString().trim();


        str_name_as_on_aadhaar = edt_nb_ekyc_name_as_aadhar.getText().toString().trim();
        str_name_as_on_aadhaar = edt_nb_ekyc_name_as_aadhar.getText().toString().trim();

        if (str_proposal_no.length() != 10) {
            return str_error = "please enter 10-digit proposal no.";
        } else if (confirmProposal.length() != 10) {
            return str_error = "please confirm proposal no.";
        } else if (!str_proposal_no.equalsIgnoreCase(confirmProposal)) {
            return str_error = "Proposal number does not match";
        } else if (!str_pan_card_no.equals("") && !validate_pan_card) {
            return str_error = "please enter valid pan card no.";
        } else if (!validate_aadhar) {
            return str_error = "please enter valid aadhaar no.";
        } else if (str_name_as_on_aadhaar.equals("")) {
            return str_error = "please enter Name as on Aadhar No.";
        } else {
            return str_error;
        }
    }

    class AsynchVerifyProposalNo extends AsyncTask<String, String, String> {

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

                String METHOD_NAME_EKYC_CHECK_PROPOSAL = "checkProposal_LinkAADHAAR_SMRT";
                String NAMESPACE = "http://tempuri.org/";
                request = new SoapObject(NAMESPACE, METHOD_NAME_EKYC_CHECK_PROPOSAL);
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

                    String SOAP_ACTION_EKYC_CHECK_PROPOSAL = "http://tempuri.org/checkProposal_LinkAADHAAR_SMRT";
                    androidHttpTranport.call(SOAP_ACTION_EKYC_CHECK_PROPOSAL, envelope);
                    //Object response = envelope.getResponse();

                    SoapPrimitive sa = (SoapPrimitive) envelope.getResponse();

                    str_output = sa.toString();

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
                    mCommonMethods.showMessageDialog(mContext, "Proposal Already Linked with Aadhar!!");
                } else if (str_output.equals("0")) {

                    //for AP FLAG
                    if (validate_aadhar && validate_pan_card) {
                        validate_aadhar = true;
                        validate_pan_card = true;
                        str_ap_flag = "AP";
                    } else if (!validate_aadhar && validate_pan_card) {

                        validate_aadhar = false;
                        validate_pan_card = true;
                        str_ap_flag = "P";
                    } else if (validate_aadhar && !validate_pan_card) {

                        validate_aadhar = true;
                        validate_pan_card = false;
                        str_ap_flag = "A";
                    } else if (!validate_aadhar && !validate_pan_card) {

                        validate_aadhar = false;
                        validate_pan_card = false;
                        str_ap_flag = "A";
                    }

                    //after that save data locally and call ekyc activity
                    ContentValues cv = new ContentValues();
                    cv.put(db.EKYC_PS_CLAIMS_USER_ID, mCommonMethods.GetUserID(mContext));
                    cv.put(db.EKYC_PS_CLAIMS_USER_TYPE, mCommonMethods.GetUserType(mContext));
                    cv.put(db.EKYC_PS_CLAIMS_PROPOSAL_NUMBER, str_proposal_no);
                    cv.put(db.EKYC_PS_CLAIMS_CUSTOMER_NAME, str_name_as_on_aadhaar);
                    cv.put(db.EKYC_PS_CLAIMS_CLIENT_ID, "");
                    cv.put(db.EKYC_PS_CLAIMS_PLAN_NAME, "");
                    cv.put(db.EKYC_PS_CLAIMS_POLICY_STATUS, "");
                    cv.put(db.EKYC_PS_CLAIMS_AADHAAR_NUMBER, str_aashaar_no);
                    cv.put(db.EKYC_PS_CLAIMS_PAN, str_pan_card_no);
                    cv.put(db.EKYC_PS_CLAIMS_AP_FLAG, str_ap_flag);
                    cv.put(db.EKYC_PS_CLAIMS_MODE, "Ekyc");
                    cv.put(db.EKYC_PS_CLAIMS_EKYC_MODE, "");
                    cv.put(db.EKYC_PS_CLAIMS_EKYC_RESULT, "");
                    cv.put(db.EKYC_PS_CLAIMS_IS_DELETE, "0");

                    Date currentTime = Calendar.getInstance().getTime();
                    String str_date = String.valueOf(currentTime.getTime());
                    cv.put(db.EKYC_PS_CLAIMS_DATE_TIME, str_date);

                    cv.put(db.EKYC_PS_CLAIMS_EKYC_STATUS, 0);
                    cv.put(db.EKYC_PS_CLAIMS_EKYC_FLOW_TYPE, str_from);
                    cv.put(db.EKYC_PS_CLAIMS_EKYC_NA_URN, NeedAnalysisActivity.URN_NO);

                    db.insert_eKYC_PS_Claims_Details(cv);

                    Intent intent = new Intent(NewBusinessEkycActivity.this, eKYCActivity.class);
                    intent.putExtra("ProposalNumber", str_proposal_no);
                    intent.putExtra("CustomerName", str_name_as_on_aadhaar);
                    intent.putExtra("CustomerAadharNumber", str_aashaar_no);
                    intent.putExtra("CustomerAadharName", str_name_as_on_aadhaar);
                    //intent.putExtra("PlanName", "");
                    intent.putExtra("FROM", str_from);// need analysis(NA) or new bussiness (NB)
                    startActivity(intent);

                    running = false;
                    str_output = "";
                }
            } else {
                mCommonMethods.showMessageDialog(mContext, mCommonMethods.SERVER_ERROR);
            }
        }
    }


    private class ChangeTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new PasswordCharSequence(source);
        }

        private class PasswordCharSequence implements CharSequence {
            private CharSequence mSource;

            public PasswordCharSequence(CharSequence source) {
                mSource = source;
            }

            public char charAt(int index) {
                if (index <= 5 && edittextFieldName.equalsIgnoreCase("P")) {
                    return 'X';
                } else if (index <= 7 && edittextFieldName.equalsIgnoreCase("A")) {
                    return 'X';
                } else
                    return mSource.charAt(index);
            }

            public int length() {
                return mSource.length();
            }

            public CharSequence subSequence(int start, int end) {
                return mSource.subSequence(start, end);
            }
        }
    }


    private void validateConfirmProposalNumber() {
        if (!(edittextConfirmProposal.getText().toString().equals(edt_nb_ekyc_proposal_no.getText().toString()))) {
            edittextConfirmProposal.setError("Proposal Number does not match");
        }

    }
}
