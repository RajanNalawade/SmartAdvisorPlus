package sbilife.com.pointofsale_bancaagency.posp_ra;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.home.CarouselHomeActivity;

public class Activity_POSP_RA_InterviewBSM extends AppCompatActivity implements View.OnClickListener {
    private final String NAMESPACE = "http://tempuri.org/";

    private final String METHOD_NAME_ACCEPT_BY_BSM = "saveBSMQues_Agentonboard";
    private final String METHOD_NAME_REJECT_BY_BSM = "saveBSMQues_Agentonboard_other";
    private CommonMethods mCommonMethods;
    private Context mContext;
    private DatabaseHelper db;

    private Spinner spnr_aob_bsm_interview_q1, spnr_aob_bsm_interview_q2;
    private EditText edt_aob_bsm_interview_q1, edt_aob_bsm_interview_q2, edt_aob_bsm_interview_q3, edt_aob_bsm_interview_q4,
            edt_aob_bsm_interview_q5, edt_aob_bsm_rejection_remark;
    private CheckBox chkbox_aob_bsm_interview_clarify, chkbox_aob_bsm_interview_clarify2;

    private Button btn_aob_bsm_interview_next, btn_aob_bsm_interview_reject;

    private StringBuilder str_bsm_questions_details;
    private String PANNumber = "", UMCode = "";

    private ProgressDialog mProgressDialog;
    private Context context;
    private AsyncAcceptRejectBYBSM mAsyncAcceptBYBSM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_posp_ra_interview_bsm);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.cifenrollment_pf_activity_title);

        context = this;
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
        String Message = "Loading Please wait...";
        mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);
        PANNumber = getIntent().getStringExtra("PANNumber");
        UMCode = getIntent().getStringExtra("UMCode");
        initialisation();
    }

    public void initialisation() {

        mContext = this;
        mCommonMethods = new CommonMethods();

        mCommonMethods.setApplicationToolbarMenu1(this, "BSM Questions");

        db = new DatabaseHelper(mContext);

        spnr_aob_bsm_interview_q1 = findViewById(R.id.spnr_aob_bsm_interview_q1);
        ArrayAdapter<String> bsm_interview_q1_adapter = new ArrayAdapter<String>(
                mContext, R.layout.spinner_aob, getResources().getStringArray(R.array.arr_aob_ans_yes_no));
        spnr_aob_bsm_interview_q1.setAdapter(bsm_interview_q1_adapter);
        bsm_interview_q1_adapter.notifyDataSetChanged();

        edt_aob_bsm_interview_q1 = findViewById(R.id.edt_aob_bsm_interview_q1);

        spnr_aob_bsm_interview_q1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    edt_aob_bsm_interview_q1.setVisibility(View.VISIBLE);
                } else {
                    edt_aob_bsm_interview_q1.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnr_aob_bsm_interview_q2 = findViewById(R.id.spnr_aob_bsm_interview_q2);
        ArrayAdapter<String> bsm_interview_q2_adapter = new ArrayAdapter<String>(
                mContext, R.layout.spinner_aob, getResources().getStringArray(R.array.arr_aob_ans_yes_no));
        spnr_aob_bsm_interview_q2.setAdapter(bsm_interview_q2_adapter);
        bsm_interview_q2_adapter.notifyDataSetChanged();

        edt_aob_bsm_interview_q2 = findViewById(R.id.edt_aob_bsm_interview_q2);

        spnr_aob_bsm_interview_q2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    edt_aob_bsm_interview_q2.setVisibility(View.VISIBLE);
                } else {
                    edt_aob_bsm_interview_q2.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        edt_aob_bsm_interview_q3 = findViewById(R.id.edt_aob_bsm_interview_q3);
        edt_aob_bsm_interview_q4 = findViewById(R.id.edt_aob_bsm_interview_q4);
        edt_aob_bsm_interview_q5 = findViewById(R.id.edt_aob_bsm_interview_q5);

        btn_aob_bsm_interview_next = findViewById(R.id.btn_aob_bsm_interview_next);
        btn_aob_bsm_interview_next.setOnClickListener(this);

        btn_aob_bsm_interview_reject = findViewById(R.id.btn_aob_bsm_interview_reject);
        btn_aob_bsm_interview_reject.setOnClickListener(this);

        chkbox_aob_bsm_interview_clarify = findViewById(R.id.chkbox_aob_bsm_interview_clarify);
        chkbox_aob_bsm_interview_clarify2 = findViewById(R.id.chkbox_aob_bsm_interview_clarify2);

        edt_aob_bsm_rejection_remark = findViewById(R.id.edt_aob_bsm_rejection_remark);

        str_bsm_questions_details = new StringBuilder();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_aob_bsm_interview_next:
                edt_aob_bsm_rejection_remark.setVisibility(View.GONE);
                onNextClick();
                break;

            case R.id.btn_aob_bsm_interview_reject:
                edt_aob_bsm_rejection_remark.setVisibility(View.VISIBLE);
                edt_aob_bsm_rejection_remark.requestFocus();
                String strRemark = edt_aob_bsm_rejection_remark.getText().toString();
                strRemark = strRemark == null ? "" : strRemark;
                if (strRemark.equals("")) {
                    mCommonMethods.showMessageDialog(mContext, "Please enter rejection remark first then press reject button");
                } else {
                    //save data
                    onRejectClick();
                }
                break;

            default:
                break;
        }
    }

    private void onRejectClick() {

        str_bsm_questions_details = new StringBuilder();

        str_bsm_questions_details.append("<?xml version='1.0' encoding='utf-8' ?>");
        str_bsm_questions_details.append("<t_bsm>");
        str_bsm_questions_details.append("<PER_PAN_NO>").append(PANNumber).append("</PER_PAN_NO>");
        str_bsm_questions_details.append("<UM_CODE>").append(UMCode).append("</UM_CODE>");
        str_bsm_questions_details.append("<STATUS>Rejected</STATUS>");
        str_bsm_questions_details.append("<REMARKS>" + edt_aob_bsm_rejection_remark.getText().toString() + "</REMARKS>");

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        String formattedDate = df.format(c);

        str_bsm_questions_details.append("<CREATED_DATE>").append(formattedDate).append("</CREATED_DATE>");

        str_bsm_questions_details.append("<TYPE>").append(mCommonMethods.str_posp_ra_customer_type).append("</TYPE>");
        str_bsm_questions_details.append("<SUB_TYPE>New</SUB_TYPE>");

        str_bsm_questions_details.append("</t_bsm>");

        mAsyncAcceptBYBSM = new AsyncAcceptRejectBYBSM();
        mAsyncAcceptBYBSM.execute(METHOD_NAME_REJECT_BY_BSM);
    }

    private void onNextClick() {

        //1. validate all details
        String str_error = validateDetails();
        if (str_error.equals("")) {

            //2. create xml string for data saving
            get_bsm_questions_xml();

            //3. update data against global row id
           /* ContentValues cv = new ContentValues();
            cv.put(DatabaseHelper.POSP_RA_BSM_INTERVIEW_QUE, str_bsm_questions_details.toString());
            cv.put(DatabaseHelper.POSP_RA_UPDATED_BY, mCommonMethods.GetUserCode(mContext));

            Calendar c = Calendar.getInstance();
            //save date in long
            cv.put(DatabaseHelper.POSP_RA_UPDATED_DATE, new Date(c.getTimeInMillis()).getTime() +"");
            cv.put(DatabaseHelper.POSP_RA_SYNCH_STATUS, "8");

            int i = db.update_POSP_RA_details(cv, DatabaseHelper.POSP_RA_ID + " =? ",
                    new String[]{Activity_AOB_Authentication.row_details+""});

            mCommonMethods.showToast(mContext, "Details saved Successfully : " + i);*/
            mAsyncAcceptBYBSM = new AsyncAcceptRejectBYBSM();
            mAsyncAcceptBYBSM.execute(METHOD_NAME_ACCEPT_BY_BSM);

        } else {
            mCommonMethods.showMessageDialog(mContext, str_error);
        }

    }

    private void get_bsm_questions_xml() {

        String bsm_interview_q1_yes_no = spnr_aob_bsm_interview_q1.getSelectedItem().toString();
        String bsm_interview_q2_yes_no = spnr_aob_bsm_interview_q2.getSelectedItem().toString();
        String bsm_interview_q3 = edt_aob_bsm_interview_q3.getText().toString();
        String bsm_interview_q4 = edt_aob_bsm_interview_q4.getText().toString();
        String bsm_interview_q5 = edt_aob_bsm_interview_q5.getText().toString();

        str_bsm_questions_details = new StringBuilder();

        //str_bsm_questions_details.append("<bsm_questions_details>");
        str_bsm_questions_details.append("<?xml version='1.0' encoding='utf-8' ?>");
        str_bsm_questions_details.append("<t_bsm>");
        str_bsm_questions_details.append("<bsm_questions_q1_yes_no>").append(bsm_interview_q1_yes_no).append("</bsm_questions_q1_yes_no>");

        if (bsm_interview_q1_yes_no.equals("Yes")) {
            str_bsm_questions_details.append("<bsm_questions_q1_yes_no_comment>").append(edt_aob_bsm_interview_q1.getText().toString()).append("</bsm_questions_q1_yes_no_comment>");
        } else
            str_bsm_questions_details.append("<bsm_questions_q1_yes_no_comment>").append("").append("</bsm_questions_q1_yes_no_comment>");

        str_bsm_questions_details.append("<bsm_questions_q2_yes_no>").append(bsm_interview_q2_yes_no).append("</bsm_questions_q2_yes_no>");

        if (bsm_interview_q2_yes_no.equals("Yes")) {
            str_bsm_questions_details.append("<bsm_questions_q2_yes_no_comment>").append(edt_aob_bsm_interview_q2.getText().toString()).append("</bsm_questions_q2_yes_no_comment>");
        } else
            str_bsm_questions_details.append("<bsm_questions_q2_yes_no_comment>").append("").append("</bsm_questions_q2_yes_no_comment>");

        str_bsm_questions_details.append("<bsm_questions_q3_comment>").append(bsm_interview_q3).append("</bsm_questions_q3_comment>");

        str_bsm_questions_details.append("<bsm_questions_q4_comment>").append(bsm_interview_q4).append("</bsm_questions_q4_comment>");

        str_bsm_questions_details.append("<bsm_questions_q5_comment>").append(bsm_interview_q5).append("</bsm_questions_q5_comment>");

        if (chkbox_aob_bsm_interview_clarify.isChecked())
            str_bsm_questions_details.append("<bsm_questions_clarify_check>true</bsm_questions_clarify_check>");
        else
            str_bsm_questions_details.append("<bsm_questions_clarify_check>false</bsm_questions_clarify_check>");

        str_bsm_questions_details.append("<PER_PAN_NO>").append(PANNumber).append("</PER_PAN_NO>");
        str_bsm_questions_details.append("<UM_CODE>").append(UMCode).append("</UM_CODE>");

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        String formattedDate = df.format(c);

        str_bsm_questions_details.append("<CREATED_DATE>").append(formattedDate).append("</CREATED_DATE>");

        if (chkbox_aob_bsm_interview_clarify2.isChecked())
            str_bsm_questions_details.append("<bsm_questions_clarify_check2>true</bsm_questions_clarify_check2>");
        else
            str_bsm_questions_details.append("<bsm_questions_clarify_check2>false</bsm_questions_clarify_check2>");

        str_bsm_questions_details.append("<TYPE>").append(mCommonMethods.str_posp_ra_customer_type).append("</TYPE>");
        str_bsm_questions_details.append("<SUB_TYPE>New</SUB_TYPE>");

        str_bsm_questions_details.append("</t_bsm>");
    }

    public String validateDetails() {

        if (spnr_aob_bsm_interview_q1.getSelectedItem().toString().equals("Yes") &
                edt_aob_bsm_interview_q1.getText().toString().equals("")) {
            edt_aob_bsm_interview_q1.requestFocus();
            return "Please Enter Comments";
        } else if (spnr_aob_bsm_interview_q2.getSelectedItem().toString().equals("Yes") &
                edt_aob_bsm_interview_q2.getText().toString().equals("")) {
            edt_aob_bsm_interview_q2.requestFocus();
            return "Please Enter Comments";
        } else if (edt_aob_bsm_interview_q3.getText().toString().equals("")) {
            edt_aob_bsm_interview_q3.requestFocus();
            return "Please Enter details";
        } else if (edt_aob_bsm_interview_q4.getText().toString().equals("")) {
            edt_aob_bsm_interview_q4.requestFocus();
            return "Please Enter details";
        } else if (edt_aob_bsm_interview_q5.getText().toString().equals("")) {
            edt_aob_bsm_interview_q5.requestFocus();
            return "Please Enter details";
        } else if (!chkbox_aob_bsm_interview_clarify.isChecked()) {
            chkbox_aob_bsm_interview_clarify.requestFocus();
            return "Please check clarify details";
        } else if (!chkbox_aob_bsm_interview_clarify2.isChecked()) {
            chkbox_aob_bsm_interview_clarify2.requestFocus();
            return "Please check clarify details";
        } else
            return "";

    }

    public void showMessageDialog(String message) {
        try {
            final Dialog dialog = new Dialog(mContext);
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

                    //redirect to home page
                    startActivity(new Intent(context, CarouselHomeActivity.class));
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        if (mAsyncAcceptBYBSM != null) {
            mAsyncAcceptBYBSM.cancel(true);
        }
    }

    class AsyncAcceptRejectBYBSM extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String result = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... strTypes) {
            try {

                running = true;
                SoapObject request = null;

                if (strTypes[0].equals(METHOD_NAME_ACCEPT_BY_BSM)) {
                    request = new SoapObject(NAMESPACE, METHOD_NAME_ACCEPT_BY_BSM);
                    request.addProperty("xmlStr", str_bsm_questions_details.toString());
                } else if (strTypes[0].equals(METHOD_NAME_REJECT_BY_BSM)) {
                    request = new SoapObject(NAMESPACE, METHOD_NAME_REJECT_BY_BSM);
                    request.addProperty("xmlStr", str_bsm_questions_details.toString());
                }
                request.addProperty("Type", mCommonMethods.str_posp_ra_customer_type);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                // 	allowAllSSL();
                mCommonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL);

                if (strTypes[0].equals(METHOD_NAME_ACCEPT_BY_BSM)) {
                    androidHttpTranport.call(NAMESPACE + METHOD_NAME_ACCEPT_BY_BSM, envelope);
                } else if (strTypes[0].equals(METHOD_NAME_REJECT_BY_BSM)) {
                    androidHttpTranport.call(NAMESPACE + METHOD_NAME_REJECT_BY_BSM, envelope);
                }

                SoapPrimitive sa = (SoapPrimitive) envelope.getResponse();
                result = sa.toString();

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
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            if (running) {

                String strMsg = "";

                if (result.equalsIgnoreCase("1")) {
                    strMsg = "Data Uploaded Successfully";
                } else if (result.equalsIgnoreCase("2")) {
                    strMsg = "Pan Already Exist";
                } else if (result.equalsIgnoreCase("0")) {
                    strMsg = "Something Went Wrong";
                }
                showMessageDialog(strMsg);
            } else {
                mCommonMethods.showMessageDialog(context, mCommonMethods.SERVER_ERROR);
            }
        }
    }
}
