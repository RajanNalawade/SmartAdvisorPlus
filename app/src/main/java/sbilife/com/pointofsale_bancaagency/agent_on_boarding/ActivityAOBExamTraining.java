package sbilife.com.pointofsale_bancaagency.agent_on_boarding;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

public class ActivityAOBExamTraining extends AppCompatActivity implements View.OnClickListener,
        DatePickerDialog.OnDateSetListener {

    private final String DATE_TRAINING_START_DATE = "TrainingStartDatePicker";
    private final String DATE_TRAINING_END_DATE = "TrainingEndDatePicker";
    private final String DATE_IA_UPGRADE_EXAM_DATE = "IAUpgradeExamDate";
    private final String NAMESPACE = "http://tempuri.org/";
    private final String METHOD_NAME_GET_AOB_EXAM_PLACE = "getAgentExamDetail";
    private CommonMethods mCommonMethods;
    private Context mContext;

    private ProgressDialog mProgressDialog;
    private DatabaseHelper db;

    private AsynchGetAOBExamPlace mAsynchGetAOBExamPlace;
    private Spinner spnr_aob_exam_place, spnr_aob_exam_language, spnr_ia_upgrade_exam_place,
            spnr_ia_upgrade_exam_language, spnr_ia_upgrade_exam_mode, spnr_ia_upgrade_exam_body,
            spnr_ia_upgrade_exam_status, spnr_ia_upgrade_ulip;
    private Button btn_aob_exam_training_next, btn_aob_exam_training_back;
    private TextView txt_aob_training_start_date, txt_aob_training_end_date, txt_ia_upgrade_exam_date;
    private EditText edt_ia_upgrade_roll_no, edt_ia_upgrade_marks, edt_ia_upgrade_urn;
    private ArrayList<String> lstExamPlaces = new ArrayList<>();
    private DatePickerDialog datePickerDialog;
    private Calendar mCalender;
    private int mYear = 0, mMonth = 0, mDay = 0;

    private StringBuilder str_exam_training_details;
    private boolean is_dashboard = false, is_back_pressed = false, is_ia_upgrade = false;
    private ParseXML mParseXML;
    private Date currentDate;

    private LinearLayout ll_exam_details_ia, ll_exam_details_ia_upgrade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_exam_training);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.cifenrollment_pf_activity_title);

        if (getIntent().hasExtra("is_dashboard"))
            is_dashboard = getIntent().getBooleanExtra("is_dashboard", false);

        if (getIntent().hasExtra("is_ia_upgrade"))
            is_ia_upgrade = getIntent().getBooleanExtra("is_ia_upgrade", false);

        mContext = this;
        mCommonMethods = new CommonMethods();

        mCommonMethods.setApplicationToolbarMenu1(this,"Agent on Boarding");

        db = new DatabaseHelper(mContext);

        mParseXML = new ParseXML();

        View view_aob_exam_formIA = findViewById(R.id.view_aob_exam_formIA);
        TextView txt_aob_exam_formIA = findViewById(R.id.txt_aob_exam_formIA);
        if (is_ia_upgrade) {
            view_aob_exam_formIA.setVisibility(View.GONE);
            txt_aob_exam_formIA.setVisibility(View.GONE);
        } else {
            view_aob_exam_formIA.setVisibility(View.VISIBLE);
            txt_aob_exam_formIA.setVisibility(View.VISIBLE);
        }

        ll_exam_details_ia = findViewById(R.id.ll_exam_details_ia);
        ll_exam_details_ia_upgrade = findViewById(R.id.ll_exam_details_ia_upgrade);

        btn_aob_exam_training_next = findViewById(R.id.btn_aob_exam_training_next);
        btn_aob_exam_training_next.setOnClickListener(this);

        btn_aob_exam_training_back = findViewById(R.id.btn_aob_exam_training_back);
        btn_aob_exam_training_back.setOnClickListener(this);

        if (is_ia_upgrade) {
            ll_exam_details_ia.setVisibility(View.GONE);
            ll_exam_details_ia_upgrade.setVisibility(View.VISIBLE);

            initialise_ia_upgrade();

        } else {
            ll_exam_details_ia.setVisibility(View.VISIBLE);
            ll_exam_details_ia_upgrade.setVisibility(View.GONE);
            initialisation_ia();
        }

        if (lstExamPlaces.size() == 0) {
            //get exam places from server
            mAsynchGetAOBExamPlace = new AsynchGetAOBExamPlace();
            mAsynchGetAOBExamPlace.execute();
        }

        mCalender = Calendar.getInstance();
        mYear = mCalender.get(Calendar.YEAR);
        mMonth = mCalender.get(Calendar.MONTH);
        mDay = mCalender.get(Calendar.DAY_OF_MONTH);

        currentDate = new Date(mCalender.getTimeInMillis());

        str_exam_training_details = new StringBuilder();

        //non editable with no saving
        //editable
        enableDisableAllFields(!is_dashboard);
    }

    public void initialise_ia_upgrade() {
        edt_ia_upgrade_roll_no = findViewById(R.id.edt_ia_upgrade_roll_no);
        txt_ia_upgrade_exam_date = findViewById(R.id.txt_ia_upgrade_exam_date);
        txt_ia_upgrade_exam_date.setOnClickListener(this);
        edt_ia_upgrade_marks = findViewById(R.id.edt_ia_upgrade_marks);

        spnr_ia_upgrade_exam_place = findViewById(R.id.spnr_ia_upgrade_exam_place);

        spnr_ia_upgrade_exam_language = findViewById(R.id.spnr_ia_upgrade_exam_language);
        ArrayAdapter<String> exam_language_adapter = new ArrayAdapter<String>(
                mContext, R.layout.spinner_aob, getResources().getStringArray(R.array.arr_aob_exam_language));
        spnr_ia_upgrade_exam_language.setAdapter(exam_language_adapter);
        exam_language_adapter.notifyDataSetChanged();

        spnr_ia_upgrade_exam_mode = findViewById(R.id.spnr_ia_upgrade_exam_mode);
        ArrayAdapter<String> exam_mode_adapter = new ArrayAdapter<String>(
                mContext, R.layout.spinner_aob, new String[]{"Online"});
        spnr_ia_upgrade_exam_mode.setAdapter(exam_mode_adapter);
        exam_mode_adapter.notifyDataSetChanged();

        spnr_ia_upgrade_exam_body = findViewById(R.id.spnr_ia_upgrade_exam_body);
        ArrayAdapter<String> exam_body_adapter = new ArrayAdapter<String>(
                mContext, R.layout.spinner_aob, new String[]{"NSE.IT"});
        spnr_ia_upgrade_exam_body.setAdapter(exam_body_adapter);
        exam_body_adapter.notifyDataSetChanged();

        spnr_ia_upgrade_exam_status = findViewById(R.id.spnr_ia_upgrade_exam_status);
        ArrayAdapter<String> exam_status_adapter = new ArrayAdapter<String>(
                mContext, R.layout.spinner_aob, new String[]{"Pass"});
        spnr_ia_upgrade_exam_status.setAdapter(exam_status_adapter);
        exam_status_adapter.notifyDataSetChanged();

        spnr_ia_upgrade_ulip = findViewById(R.id.spnr_ia_upgrade_ulip);
        ArrayAdapter<String> exam_ulip_adapter = new ArrayAdapter<String>(
                mContext, R.layout.spinner_aob, new String[]{"Yes"});
        spnr_ia_upgrade_ulip.setAdapter(exam_ulip_adapter);
        exam_ulip_adapter.notifyDataSetChanged();

        edt_ia_upgrade_urn = findViewById(R.id.edt_ia_upgrade_urn);

    }

    public void initialisation_ia() {

        spnr_aob_exam_place = findViewById(R.id.spnr_aob_exam_place);

        spnr_aob_exam_language = findViewById(R.id.spnr_aob_exam_language);
        ArrayAdapter<String> exam_language_adapter = new ArrayAdapter<String>(
                mContext, R.layout.spinner_aob, getResources().getStringArray(R.array.arr_aob_exam_language));
        spnr_aob_exam_language.setAdapter(exam_language_adapter);
        exam_language_adapter.notifyDataSetChanged();

        txt_aob_training_start_date = findViewById(R.id.txt_aob_training_start_date);
        txt_aob_training_start_date.setOnClickListener(this);

        txt_aob_training_end_date = findViewById(R.id.txt_aob_training_end_date);
        txt_aob_training_end_date.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {

        if (is_dashboard) {
            Intent intent = new Intent(ActivityAOBExamTraining.this, ActivityAOBForm1A.class);
            intent.putExtra("is_dashboard", is_dashboard);
            startActivity(intent);
        } else if (is_ia_upgrade) {
            Intent intent = new Intent(ActivityAOBExamTraining.this, ActivityAOBBankDetails.class);
            intent.putExtra("is_ia_upgrade", is_ia_upgrade);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_aob_exam_training_back:
                is_back_pressed = true;
                onBackPressed();
                break;

            case R.id.txt_aob_training_start_date:

                datePickerDialog = DatePickerDialog.newInstance(ActivityAOBExamTraining.this, mYear, mMonth, mDay);
                datePickerDialog.setThemeDark(false);
                datePickerDialog.showYearPickerFirst(false);
                datePickerDialog.setMinDate(mCalender);
                datePickerDialog.dismissOnPause(true); //dismiss dialog when onPause() called?
                datePickerDialog.setAccentColor(getResources().getColor(R.color.Common_blue));
                datePickerDialog.setTitle("Training Start Date");

                datePickerDialog.show(getFragmentManager(), DATE_TRAINING_START_DATE);

                break;

            case R.id.txt_aob_training_end_date:

                txt_aob_training_end_date.setText("");

                datePickerDialog = DatePickerDialog.newInstance(ActivityAOBExamTraining.this, mYear, mMonth, mDay);
                datePickerDialog.setThemeDark(false);
                datePickerDialog.showYearPickerFirst(false);
                datePickerDialog.dismissOnPause(true); //dismiss dialog when onPause() called?
                datePickerDialog.setAccentColor(getResources().getColor(R.color.Common_blue));
                datePickerDialog.setMinDate(mCalender);
                datePickerDialog.setTitle("Training End Date");
                datePickerDialog.show(getFragmentManager(), DATE_TRAINING_END_DATE);

                break;

            case R.id.txt_ia_upgrade_exam_date:
                txt_ia_upgrade_exam_date.setText("");
                datePickerDialog = DatePickerDialog.newInstance(ActivityAOBExamTraining.this, mYear, mMonth, mDay);
                datePickerDialog.setThemeDark(false);
                datePickerDialog.showYearPickerFirst(false);
                datePickerDialog.dismissOnPause(true); //dismiss dialog when onPause() called?
                datePickerDialog.setAccentColor(getResources().getColor(R.color.Common_blue));
                //datePickerDialog.setMinDate(mCalender);
                datePickerDialog.setTitle("Exam Date");
                datePickerDialog.show(getFragmentManager(), DATE_IA_UPGRADE_EXAM_DATE);
                break;

            case R.id.btn_aob_exam_training_next:

                onNextClick();

                break;

            default:
                break;
        }
    }

    public void onNextClick() {

        if (!is_dashboard) {
            //1. validate all details
            String str_error = validateDetails();
            if (str_error.equals("")) {

                //2. create xml string for data saving
                get_exam_training_xml();

                //bsm interview questions added here temp. purpose send blank

                StringBuilder str_bsm_questions_details = new StringBuilder();

                //str_bsm_questions_details.append("<bsm_questions_details>");
                str_bsm_questions_details.append("<bsm_questions_q1_yes_no></bsm_questions_q1_yes_no>");
                str_bsm_questions_details.append("<bsm_questions_q1_yes_no_comment></bsm_questions_q1_yes_no_comment>");
                str_bsm_questions_details.append("<bsm_questions_q2_yes_no></bsm_questions_q2_yes_no>");
                str_bsm_questions_details.append("<bsm_questions_q2_yes_no_comment></bsm_questions_q2_yes_no_comment>");
                str_bsm_questions_details.append("<bsm_questions_q3_comment></bsm_questions_q3_comment>");
                str_bsm_questions_details.append("<bsm_questions_q4_comment></bsm_questions_q4_comment>");

                str_bsm_questions_details.append("<bsm_questions_q5_comment></bsm_questions_q5_comment>");
                str_bsm_questions_details.append("<bsm_questions_clarify_check>true</bsm_questions_clarify_check>");

                //3. update data against global row id
                ContentValues cv = new ContentValues();
                cv.put(db.AGENT_ON_BOARDING_EXAM_TRAINING_DETAILS, str_exam_training_details.toString());

                cv.put(db.AGENT_ON_BOARDING_BSM_INTERVIEW_QUE, str_bsm_questions_details.toString());

                if (is_ia_upgrade) {

                    String strFormIA = "";

                    //str_form1a_info.append("<form1a_info>");
                    strFormIA += "<form1a_info_any_insurance></form1a_info_any_insurance>";
                    strFormIA += "<form1a_info_insurance_name></form1a_info_insurance_name>";
                    strFormIA += "<form1a_info_insurance_agency_code></form1a_info_insurance_agency_code>";
                    strFormIA += "<form1a_info_insurance_appointment_date></form1a_info_insurance_appointment_date>";
                    strFormIA += "<form1a_info_insurance_cessation_date></form1a_info_insurance_cessation_date>";
                    strFormIA += "<form1a_info_insurance_reason_for_cess></form1a_info_insurance_reason_for_cess>";
                    //str_occupational_info.append("</form1a_info>");

                    cv.put(db.AGENT_ON_BOARDING_FORM_1_A, strFormIA);
                }

                cv.put(db.AGENT_ON_BOARDING_UPDATED_BY, mCommonMethods.GetUserCode(mContext));

                Calendar c = Calendar.getInstance();
                //save date in long
                cv.put(db.AGENT_ON_BOARDING_UPDATED_DATE, new Date(c.getTimeInMillis()).getTime() + "");
                cv.put(db.AGENT_ON_BOARDING_SYNCH_STATUS, "7");

                int i = db.update_agent_on_boarding_details(cv, db.AGENT_ON_BOARDING_ID + " =? ",
                        new String[]{Activity_AOB_Authentication.row_details + ""});

                mCommonMethods.showToast(mContext, "Details saved Successfully : " + i);

                Intent mIntent = new Intent(ActivityAOBExamTraining.this, ActivityAOBTermsConditionsDeclaration.class);
                if (is_ia_upgrade)
                    mIntent.putExtra("is_ia_upgrade", is_ia_upgrade);
                startActivity(mIntent);

            } else {
                mCommonMethods.showMessageDialog(mContext, str_error);
            }
        } else if (is_dashboard) {
            Intent mIntent = new Intent(ActivityAOBExamTraining.this, ActivityAOBTermsConditionsDeclaration.class);
            mIntent.putExtra("is_dashboard", is_dashboard);
            startActivity(mIntent);
        }
    }

    private void get_exam_training_xml() {

        if (is_ia_upgrade) {

            String str_roll_no = edt_ia_upgrade_roll_no.getText().toString();
            String str_exam_date = txt_ia_upgrade_exam_date.getText().toString();
            String str_obtained_marks = edt_ia_upgrade_marks.getText().toString();
            String str_exam_place = spnr_ia_upgrade_exam_place.getSelectedItem().toString();
            String str_exam_language = spnr_ia_upgrade_exam_language.getSelectedItem().toString();
            String str_exam_mode = spnr_ia_upgrade_exam_mode.getSelectedItem().toString();
            String str_exam_body = spnr_ia_upgrade_exam_body.getSelectedItem().toString();
            String str_exam_status = spnr_ia_upgrade_exam_status.getSelectedItem().toString();
            String str_ulip = spnr_ia_upgrade_ulip.getSelectedItem().toString();
            String str_urn = edt_ia_upgrade_urn.getText().toString();

            //IA data
            str_exam_training_details.append("<exam_details_place></exam_details_place>");
            str_exam_training_details.append("<exam_details_language></exam_details_language>");
            str_exam_training_details.append("<training_details_start_date></training_details_start_date>");
            str_exam_training_details.append("<training_details_end_date></training_details_end_date>");

            //IA upgrade
            str_exam_training_details.append("<ia_upgrade_exam_roll_no>").append(str_roll_no).append("</ia_upgrade_exam_roll_no>");

            if (!str_exam_date.equals("")) {
                String[] strArr = str_exam_date.split("-");
                str_exam_training_details.append("<ia_upgrade_exam_date>").append(strArr[1] + "-" + strArr[0] + "-" + strArr[2]).append("</ia_upgrade_exam_date>");
            } else
                str_exam_training_details.append("<ia_upgrade_exam_date></ia_upgrade_exam_date>");

            str_exam_training_details.append("<ia_upgrade_exam_obtained_marks>")
                    .append(str_obtained_marks).append("</ia_upgrade_exam_obtained_marks>");
            str_exam_training_details.append("<ia_upgrade_exam_place>").append(str_exam_place).append("</ia_upgrade_exam_place>");
            str_exam_training_details.append("<ia_upgrade_exam_language>").append(str_exam_language).append("</ia_upgrade_exam_language>");
            str_exam_training_details.append("<ia_upgrade_exam_mode>").append(str_exam_mode).append("</ia_upgrade_exam_mode>");
            str_exam_training_details.append("<ia_upgrade_exam_body>").append(str_exam_body).append("</ia_upgrade_exam_body>");
            str_exam_training_details.append("<ia_upgrade_exam_status>").append(str_exam_status).append("</ia_upgrade_exam_status>");
            str_exam_training_details.append("<ia_upgrade_ulip>").append(str_ulip).append("</ia_upgrade_ulip>");
            str_exam_training_details.append("<ia_upgrade_urn>").append(str_urn).append("</ia_upgrade_urn>");

        } else {
            String str_exam_place = spnr_aob_exam_place.getSelectedItem().toString();
            String str_exam_language = spnr_aob_exam_language.getSelectedItem().toString();
            String str_training_start_date = txt_aob_training_start_date.getText().toString();
            String str_training_end_date = txt_aob_training_end_date.getText().toString();

            str_exam_training_details = new StringBuilder();

            //str_exam_training_details.append("<exam_training_details>");
            str_exam_training_details.append("<exam_details_place>").append(str_exam_place).append("</exam_details_place>");
            str_exam_training_details.append("<exam_details_language>").append(str_exam_language).append("</exam_details_language>");

            if (!str_training_start_date.equals("")) {
                String[] strArr = str_training_start_date.split("-");
                str_exam_training_details.append("<training_details_start_date>").append(strArr[1] + "-" + strArr[0] + "-" + strArr[2]).append("</training_details_start_date>");
            } else
                str_exam_training_details.append("<training_details_start_date></training_details_start_date>");

            if (!str_training_end_date.equals("")) {
                String[] strArr = str_training_end_date.split("-");
                str_exam_training_details.append("<training_details_end_date>").append(strArr[1] + "-" + strArr[0] + "-" + strArr[2]).append("</training_details_end_date>");
            } else
                str_exam_training_details.append("<training_details_end_date></training_details_end_date>");

            //str_exam_training_details.append("</exam_training_details>");

            //IA upgrade
            str_exam_training_details.append("<ia_upgrade_exam_roll_no></ia_upgrade_exam_roll_no>");
            str_exam_training_details.append("<ia_upgrade_exam_date></ia_upgrade_exam_date>");
            str_exam_training_details.append("<ia_upgrade_exam_obtained_marks></ia_upgrade_exam_obtained_marks>");
            str_exam_training_details.append("<ia_upgrade_exam_place></ia_upgrade_exam_place>");
            str_exam_training_details.append("<ia_upgrade_exam_language></ia_upgrade_exam_language>");
            str_exam_training_details.append("<ia_upgrade_exam_mode></ia_upgrade_exam_mode>");
            str_exam_training_details.append("<ia_upgrade_exam_body></ia_upgrade_exam_body>");
            str_exam_training_details.append("<ia_upgrade_exam_status></ia_upgrade_exam_status>");
            str_exam_training_details.append("<ia_upgrade_ulip></ia_upgrade_ulip>");
            str_exam_training_details.append("<ia_upgrade_urn></ia_upgrade_urn>");
        }

    }

    public String validateDetails() {

        try {
            if (is_ia_upgrade) {
                String str_roll_no = edt_ia_upgrade_roll_no.getText().toString();
                String str_exam_date = txt_ia_upgrade_exam_date.getText().toString();
                String str_obtained_marks = edt_ia_upgrade_marks.getText().toString();
                String str_exam_place = spnr_ia_upgrade_exam_place.getSelectedItem().toString();
                String str_exam_language = spnr_ia_upgrade_exam_language.getSelectedItem().toString();
                String str_urn = edt_ia_upgrade_urn.getText().toString();

                if (str_roll_no.equals("")) {
                    return "Enter Candidate Roll No.";
                } else if (str_exam_date.equals("")) {
                    return "Select Exam Date";
                } else if (str_obtained_marks.equals("")) {
                    return "Enter Obtainde marks";
                } else if (str_exam_place.equals("")) {
                    return "Select Examination Place";
                } else if (str_exam_language.equals("")) {
                    return "Select Examination Language";
                } else if (str_urn.equals("")) {
                    return "Enter URN";
                } else {
                    return "";
                }

            } else {
                if (spnr_aob_exam_place.getSelectedItem().toString().equals("Select Exam Place")) {
                    spnr_aob_exam_place.requestFocus();
                    return "Please Select Exam Place";
                } else if (spnr_aob_exam_language.getSelectedItem().toString().equals("Select Language")) {
                    spnr_aob_exam_language.requestFocus();
                    return "Please Select Exam Language";
                } else if (txt_aob_training_start_date.getText().toString().equals("")) {
                    txt_aob_training_start_date.requestFocus();
                    return "Please Select Training Start Date";
                } else if (txt_aob_training_end_date.getText().toString().equals("")) {
                    txt_aob_training_end_date.requestFocus();
                    return "Please Select Training End Date";
                } else
                    return "";
            }
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public void enableDisableAllFields(boolean is_enable) {

        if (is_ia_upgrade) {
            edt_ia_upgrade_roll_no.setEnabled(is_enable);
            txt_ia_upgrade_exam_date.setEnabled(is_enable);
            edt_ia_upgrade_marks.setEnabled(is_enable);
            spnr_ia_upgrade_exam_place.setEnabled(is_enable);
            spnr_ia_upgrade_exam_language.setEnabled(is_enable);
            spnr_ia_upgrade_exam_mode.setEnabled(is_enable);
            spnr_ia_upgrade_exam_body.setEnabled(is_enable);
            spnr_ia_upgrade_exam_status.setEnabled(is_enable);
            spnr_ia_upgrade_ulip.setEnabled(is_enable);
            edt_ia_upgrade_urn.setEnabled(is_enable);
        } else {
            spnr_aob_exam_place.setEnabled(is_enable);

            spnr_aob_exam_language.setEnabled(is_enable);

            txt_aob_training_start_date.setEnabled(is_enable);

            txt_aob_training_end_date.setEnabled(is_enable);
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        String strSelectedDate = (dayOfMonth < 10 ? "0" : "") + dayOfMonth + "-"
                + ((monthOfYear + 1) < 10 ? "0" : "") + (monthOfYear + 1) + "-"
                + year;

        switch (view.getTag()) {

            case DATE_IA_UPGRADE_EXAM_DATE:
                txt_ia_upgrade_exam_date.setText(strSelectedDate);
                break;

            case DATE_TRAINING_START_DATE:
                txt_aob_training_start_date.setText(strSelectedDate);
                break;

            case DATE_TRAINING_END_DATE:
                try {

                    SimpleDateFormat sdp = new SimpleDateFormat("dd-MM-yyyy");
                    Date selectedDate = sdp.parse(strSelectedDate);

                    String strStart = txt_aob_training_start_date.getText() == null
                            ? "" : txt_aob_training_start_date.getText().toString();

                    if (!strStart.equals("")) {

                        Date startDate = sdp.parse(strStart);

                        long diffDays = (selectedDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24);

                        if (!startDate.after(selectedDate) && !startDate.equals(sdp.format(selectedDate))) {

                            //CMS Changes
                            if ((diffDays >= 2 && diffDays < 7)) {
                                txt_aob_training_end_date.setText(strSelectedDate);
                            } else {
                                mCommonMethods.showMessageDialog(mContext, "Training end date should be of minimum 3 days and maximum 7 days from start date!");
                            }

                        } else {
                            mCommonMethods.showMessageDialog(mContext, "Please select End Date greater than Start Date!");
                        }

                    } else {
                        mCommonMethods.showToast(mContext, "Please enter Start Date first");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public class AsynchGetAOBExamPlace extends AsyncTask<String, String, String> {

        private volatile boolean running = true;

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(mContext, ProgressDialog.THEME_HOLO_LIGHT);
            mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>Please wait...<b></font>"));
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            if (mCommonMethods.isNetworkConnected(mContext)) {

                try {
                    running = true;

                    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_GET_AOB_EXAM_PLACE);
                    mCommonMethods.TLSv12Enable();

                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelope.dotNet = true;

                    envelope.setOutputSoapObject(request);

                    HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL);

                    androidHttpTranport.call(NAMESPACE + METHOD_NAME_GET_AOB_EXAM_PLACE, envelope);
                    //Object response = envelope.getResponse();

                    SoapPrimitive sa = (SoapPrimitive) envelope.getResponse();

                    ParseXML mParse = new ParseXML();


                    List<String> lstParse = mParse.parseParentNode(mParse.parseXmlTag(sa.toString(), "NewDataSet"), "Table");

                    lstExamPlaces = new ArrayList<>();
                    for (String strParse : lstParse) {

                        String strPlace = mParse.parseXmlTag(strParse, "CEC_EXAMCENTER_NAME");

                        lstExamPlaces.add(strPlace);
                    }


                } catch (Exception e) {
                    try {
                        throw (e);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    running = false;
                }

            } else {
                mCommonMethods.showMessageDialog(mContext, mCommonMethods.NO_INTERNET_MESSAGE);
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strResult) {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();

            if (running) {

                if (lstExamPlaces.size() > 0) {

                    Collections.sort(lstExamPlaces);
                    lstExamPlaces.add(0, "Select Exam Place");

                    if (is_ia_upgrade) {
                        ArrayAdapter<String> exam_place_adapter = new ArrayAdapter<String>(mContext, R.layout.spinner_aob, lstExamPlaces);
                        spnr_ia_upgrade_exam_place.setAdapter(exam_place_adapter);
                        exam_place_adapter.notifyDataSetChanged();

                    } else {
                        ArrayAdapter<String> exam_place_adapter = new ArrayAdapter<String>(mContext, R.layout.spinner_aob, lstExamPlaces);
                        spnr_aob_exam_place.setAdapter(exam_place_adapter);
                        exam_place_adapter.notifyDataSetChanged();
                    }

                    //set Data from DB
                    ArrayList<PojoAOB> lstRes = db.get_agent_on_boarding_details_by_ID(Activity_AOB_Authentication.row_details);

                    if (lstRes.size() > 0) {

                        String str_exam_training_info = lstRes.get(0).getStr_exam_training_details();

                        str_exam_training_info = str_exam_training_info == null ? "" : str_exam_training_info;

                        if (!str_exam_training_info.equals("")) {

                            String str_exam_place = mParseXML.parseXmlTag(str_exam_training_info, "exam_details_place");
                            String str_exam_language = mParseXML.parseXmlTag(str_exam_training_info, "exam_details_language");
                            String str_training_start_date = mParseXML.parseXmlTag(str_exam_training_info, "training_details_start_date");
                            String str_training_end_date = mParseXML.parseXmlTag(str_exam_training_info, "training_details_end_date");

                            //IA upgrade
                            String str_ia_upgrade_exam_roll_no = mParseXML.parseXmlTag(str_exam_training_info, "ia_upgrade_exam_roll_no");
                            String str_ia_upgrade_exam_date = mParseXML.parseXmlTag(str_exam_training_info, "ia_upgrade_exam_date");
                            String str_ia_upgrade_exam_obtained_marks = mParseXML.parseXmlTag(str_exam_training_info, "ia_upgrade_exam_obtained_marks");
                            String str_ia_upgrade_exam_place = mParseXML.parseXmlTag(str_exam_training_info, "ia_upgrade_exam_place");
                            String str_ia_upgrade_exam_language = mParseXML.parseXmlTag(str_exam_training_info, "ia_upgrade_exam_language");
                            String str_ia_upgrade_exam_mode = mParseXML.parseXmlTag(str_exam_training_info, "ia_upgrade_exam_mode");
                            String str_ia_upgrade_exam_body = mParseXML.parseXmlTag(str_exam_training_info, "ia_upgrade_exam_body");
                            String str_ia_upgrade_exam_status = mParseXML.parseXmlTag(str_exam_training_info, "ia_upgrade_exam_status");
                            String str_ia_upgrade_ulip = mParseXML.parseXmlTag(str_exam_training_info, "ia_upgrade_ulip");
                            String str_ia_upgrade_urn = mParseXML.parseXmlTag(str_exam_training_info, "ia_upgrade_urn");

                            if (is_ia_upgrade) {
                                edt_ia_upgrade_roll_no.setText(str_ia_upgrade_exam_roll_no);
                                txt_ia_upgrade_exam_date.setText(str_ia_upgrade_exam_date);
                                edt_ia_upgrade_marks.setText(str_ia_upgrade_exam_obtained_marks);

                                spnr_ia_upgrade_exam_place.setSelection(lstExamPlaces.indexOf(str_ia_upgrade_exam_place));
                                spnr_ia_upgrade_exam_language.setSelection(Arrays.asList(getResources().
                                        getStringArray(R.array.arr_aob_exam_language)).indexOf(str_ia_upgrade_exam_language));
                                spnr_ia_upgrade_exam_mode.setSelection(0);
                                spnr_ia_upgrade_exam_body.setSelection(0);
                                spnr_ia_upgrade_exam_status.setSelection(0);
                                spnr_ia_upgrade_ulip.setSelection(0);
                                edt_ia_upgrade_urn.setText(str_ia_upgrade_urn);

                            } else {
                                spnr_aob_exam_place.setSelection(lstExamPlaces.indexOf(str_exam_place));

                                spnr_aob_exam_language.setSelection(Arrays.asList(getResources().
                                        getStringArray(R.array.arr_aob_exam_language)).indexOf(str_exam_language));

                                if (!str_training_start_date.equals("")) {
                                    String[] strArr = str_training_start_date.split("-");
                                    txt_aob_training_start_date.setText(strArr[1] + "-" + strArr[0] + "-" + strArr[2]);
                                } else
                                    txt_aob_training_start_date.setText("");

                                if (!str_training_end_date.equals("")) {
                                    String[] strArr = str_training_end_date.split("-");
                                    txt_aob_training_end_date.setText(strArr[1] + "-" + strArr[0] + "-" + strArr[2]);
                                } else
                                    txt_aob_training_end_date.setText("");
                            }
                        }
                    }

                } else {
                    mCommonMethods.showMessageDialog(mContext, "Sorry Exam Places Not Found.. Please try again");
                }

            } else {
                mCommonMethods.showMessageDialog(mContext, mCommonMethods.WEEK_INTERNET_MESSAGE);
            }
        }
    }
}
