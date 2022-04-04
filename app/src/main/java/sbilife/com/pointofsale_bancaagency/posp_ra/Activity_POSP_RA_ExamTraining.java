package sbilife.com.pointofsale_bancaagency.posp_ra;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

public class Activity_POSP_RA_ExamTraining extends AppCompatActivity implements View.OnClickListener,
        DatePickerDialog.OnDateSetListener {

    private final String DATE_EXAM_DATE = "ExamDatePicker";
    private final String DATE_TRAINING_START_DATE = "TrainingStartDatePicker";
    private final String DATE_TRAINING_END_DATE = "TrainingEndDatePicker";
    private final String NAMESPACE = "http://tempuri.org/";
    private final String METHOD_NAME_GET_POSP_RA_EXAM_PLACE = "getAgentExamDetail_other";
    private CommonMethods mCommonMethods;
    private Context mContext;

    //private AsynchGetAOBExamPlace mAsynchGetAOBExamPlace;
    private ProgressDialog mProgressDialog;
    private DatabaseHelper db;
    private Spinner spnr_pos_training_lang, spnr_aob_exam_language, spnr_posp_training_mode, spnr_posp_required_hrs,
            spnr_posp_institute_name, spnr_posp_exam_mode, spnr_posp_exam_body, spnr_posp_exam_status;
    private Button btn_aob_exam_training_next, btn_aob_exam_training_back;

    //private ArrayList<String> lstExamPlaces = new ArrayList<>();
    private TextView txt_aob_training_start_date, txt_aob_training_end_date, txt_posp_exam_date;
    private EditText edt_posp_mark_obtained;
    private int mYear = 0, mMonth = 0, mDay = 0;

    private StringBuilder str_exam_training_details;
    private boolean is_dashboard = false, is_back_pressed = false, /*is_requirement_raised = false,*/
            is_bsm_questions = false, is_rejection = false;
    private ParseXML mParseXML;
    private Date currentDate;

    private DatePickerDialog datePickerDialog;
    private Calendar mCalender;
    private LinearLayout ll_pos_exam_details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_posp_ra_exam_training);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.cifenrollment_pf_activity_title);

        if (getIntent().hasExtra("is_dashboard"))
            is_dashboard = getIntent().getBooleanExtra("is_dashboard", false);

        if (getIntent().hasExtra("is_bsm_questions"))
            is_bsm_questions = getIntent().getBooleanExtra("is_bsm_questions", false);

        if (getIntent().hasExtra("is_rejection"))
            is_rejection = getIntent().getBooleanExtra("is_rejection", false);

        initialisation();

        if (is_dashboard || is_bsm_questions) {
            //non editable with no saving
            enableDisableAllFields(false);
        } else if (is_rejection) {
            //editable
            enableDisableAllFields(true);
        } else {
            //editable
            enableDisableAllFields(true);
        }

        spnr_pos_training_lang.setEnabled(false);
        spnr_aob_exam_language.setEnabled(false);
    }

    public void initialisation() {

        mContext = this;
        mCommonMethods = new CommonMethods();

        mCommonMethods.setApplicationToolbarMenu1(this, "POSP-RA");

        db = new DatabaseHelper(mContext);

        mParseXML = new ParseXML();

        spnr_pos_training_lang = (Spinner) findViewById(R.id.spnr_pos_training_lang);
        ArrayAdapter<String> training_language_adapter = new ArrayAdapter<String>(
                mContext, R.layout.spinner_aob, getResources().getStringArray(R.array.arr_aob_exam_language));
        spnr_pos_training_lang.setAdapter(training_language_adapter);
        training_language_adapter.notifyDataSetChanged();
        spnr_pos_training_lang.setSelection(1);


        spnr_aob_exam_language = (Spinner) findViewById(R.id.spnr_aob_exam_language);
        ArrayAdapter<String> exam_language_adapter = new ArrayAdapter<String>(
                mContext, R.layout.spinner_aob, getResources().getStringArray(R.array.arr_aob_exam_language));
        spnr_aob_exam_language.setAdapter(exam_language_adapter);
        exam_language_adapter.notifyDataSetChanged();
        spnr_aob_exam_language.setSelection(1);

        spnr_posp_training_mode = (Spinner) findViewById(R.id.spnr_posp_training_mode);
        ArrayAdapter<String> training_mode_adapter = new ArrayAdapter<String>(
                mContext, R.layout.spinner_aob, new String[]{"Online"});
        spnr_posp_training_mode.setAdapter(training_mode_adapter);
        training_mode_adapter.notifyDataSetChanged();

        spnr_posp_required_hrs = (Spinner) findViewById(R.id.spnr_posp_required_hrs);
        ArrayAdapter<String> required_hrs_adapter = new ArrayAdapter<String>(
                mContext, R.layout.spinner_aob, new String[]{"15"});
        spnr_posp_required_hrs.setAdapter(required_hrs_adapter);
        required_hrs_adapter.notifyDataSetChanged();

        spnr_posp_institute_name = (Spinner) findViewById(R.id.spnr_posp_institute_name);
        ArrayAdapter<String> institute_name_adapter = new ArrayAdapter<String>(
                mContext, R.layout.spinner_aob, new String[]{"III"});
        spnr_posp_institute_name.setAdapter(institute_name_adapter);
        institute_name_adapter.notifyDataSetChanged();

        spnr_posp_exam_mode = (Spinner) findViewById(R.id.spnr_posp_exam_mode);
        ArrayAdapter<String> exam_mode_adapter = new ArrayAdapter<String>(
                mContext, R.layout.spinner_aob, new String[]{"Online"});
        spnr_posp_exam_mode.setAdapter(exam_mode_adapter);
        exam_mode_adapter.notifyDataSetChanged();

        spnr_posp_exam_body = (Spinner) findViewById(R.id.spnr_posp_exam_body);
        ArrayAdapter<String> exam_body_adapter = new ArrayAdapter<String>(
                mContext, R.layout.spinner_aob, new String[]{"III"});
        spnr_posp_exam_body.setAdapter(exam_body_adapter);
        exam_body_adapter.notifyDataSetChanged();

        spnr_posp_exam_status = (Spinner) findViewById(R.id.spnr_posp_exam_status);
        ArrayAdapter<String> exam_status_adapter = new ArrayAdapter<String>(
                mContext, R.layout.spinner_aob, new String[]{"Pass"});
        spnr_posp_exam_status.setAdapter(exam_status_adapter);
        exam_status_adapter.notifyDataSetChanged();

        btn_aob_exam_training_next = (Button) findViewById(R.id.btn_aob_exam_training_next);
        btn_aob_exam_training_next.setOnClickListener(this);

        btn_aob_exam_training_back = (Button) findViewById(R.id.btn_aob_exam_training_back);
        btn_aob_exam_training_back.setOnClickListener(this);

        txt_aob_training_start_date = (TextView) findViewById(R.id.txt_aob_training_start_date);
        txt_aob_training_start_date.setOnClickListener(this);

        txt_aob_training_end_date = (TextView) findViewById(R.id.txt_aob_training_end_date);
        txt_aob_training_end_date.setOnClickListener(this);

        txt_posp_exam_date = findViewById(R.id.txt_posp_exam_date);
        txt_posp_exam_date.setOnClickListener(this);

        edt_posp_mark_obtained = findViewById(R.id.edt_posp_mark_obtained);

        ll_pos_exam_details = findViewById(R.id.ll_pos_exam_details);

        if (is_rejection) {
            ll_pos_exam_details.setVisibility(View.VISIBLE);
        } else {
            ll_pos_exam_details.setVisibility(View.GONE);
        }

        /*if (lstExamPlaces.size() == 0){
            //get exam places from server
            mAsynchGetAOBExamPlace = new AsynchGetAOBExamPlace();
            mAsynchGetAOBExamPlace.execute();
        }*/

        mCalender = Calendar.getInstance();
        mYear = mCalender.get(Calendar.YEAR);
        mMonth = mCalender.get(Calendar.MONTH);
        mDay = mCalender.get(Calendar.DAY_OF_MONTH);

        currentDate = new Date(mCalender.getTimeInMillis());

        //set Data from DB
        ArrayList<Pojo_POSP_RA> lstRes = db.get_posp_ra_details_by_ID(Activity_POSP_RA_Authentication.row_details);

        if (lstRes.size() > 0) {

            String str_exam_training_info = lstRes.get(0).getStr_exam_training_details();

            str_exam_training_info = str_exam_training_info == null ? "" : str_exam_training_info;

            if (!str_exam_training_info.equals("")) {

                //String str_exam_place = mParseXML.parseXmlTag(str_exam_training_info, "exam_details_place");
                //String str_exam_language = mParseXML.parseXmlTag(str_exam_training_info, "exam_details_language");
                String str_training_start_date = mParseXML.parseXmlTag(str_exam_training_info, "training_details_start_date");
                String str_training_end_date = mParseXML.parseXmlTag(str_exam_training_info, "training_details_end_date");

                String str_training_language = mParseXML.parseXmlTag(str_exam_training_info, "training_details_language");
                String str_exam_date = mParseXML.parseXmlTag(str_exam_training_info, "exam_details_exam_date");

                String str_training_mode = mParseXML.parseXmlTag(str_exam_training_info, "training_details_training_mode");
                String str_training_required_hrs = mParseXML.parseXmlTag(str_exam_training_info, "training_details_required_hrs");
                String str_institute_name = mParseXML.parseXmlTag(str_exam_training_info, "training_details_institute_name");
                String str_exam_mode = mParseXML.parseXmlTag(str_exam_training_info, "exam_details_exam_mode");
                String str_exam_body = mParseXML.parseXmlTag(str_exam_training_info, "exam_details_exam_body");
                String str_obtained_marks = mParseXML.parseXmlTag(str_exam_training_info, "exam_details_marks_obtained");
                String str_exam_status = mParseXML.parseXmlTag(str_exam_training_info, "exam_details_exam_status");

                /*spnr_pos_training_lang.setSelection(Arrays.asList(getResources().
                        getStringArray(R.array.arr_aob_exam_language)).indexOf(str_training_language));

                spnr_aob_exam_language.setSelection(Arrays.asList(getResources().
                        getStringArray(R.array.arr_aob_exam_language)).indexOf(str_exam_language));*/

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

                if (!str_exam_date.equals("")) {
                    String[] strArr = str_exam_date.split("-");
                    txt_posp_exam_date.setText(strArr[1] + "-" + strArr[0] + "-" + strArr[2]);
                } else
                    txt_posp_exam_date.setText("");

                spnr_posp_training_mode.setSelection(0);
                spnr_posp_required_hrs.setSelection(0);
                spnr_posp_institute_name.setSelection(0);
                spnr_posp_exam_mode.setSelection(0);
                spnr_posp_exam_body.setSelection(0);
                edt_posp_mark_obtained.setText(str_obtained_marks);
                spnr_posp_exam_status.setSelection(0);
            }
        }
        str_exam_training_details = new StringBuilder();
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Activity_POSP_RA_ExamTraining.this, Activity_POSP_RA_BankDetails.class);
        if (is_bsm_questions)
            intent.putExtra("is_bsm_questions", is_bsm_questions);
        else if (is_dashboard)
            intent.putExtra("is_dashboard", is_dashboard);
        else if (is_rejection)
            intent.putExtra("is_rejection", is_rejection);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_aob_exam_training_back:
                is_back_pressed = true;
                onBackPressed();
                break;

            case R.id.txt_aob_training_start_date:

                datePickerDialog = DatePickerDialog.newInstance(Activity_POSP_RA_ExamTraining.this, mYear, mMonth, mDay);
                datePickerDialog.setThemeDark(false);
                datePickerDialog.showYearPickerFirst(false);
                //datePickerDialog.setMinDate(mCalender);
                datePickerDialog.dismissOnPause(true); //dismiss dialog when onPause() called?
                datePickerDialog.setAccentColor(getResources().getColor(R.color.Common_blue));
                datePickerDialog.setTitle("Training Start Date");

                datePickerDialog.show(getFragmentManager(), DATE_TRAINING_START_DATE);

                break;

            case R.id.txt_aob_training_end_date:

                txt_aob_training_end_date.setText("");

                datePickerDialog = DatePickerDialog.newInstance(Activity_POSP_RA_ExamTraining.this, mYear, mMonth, mDay);
                datePickerDialog.setThemeDark(false);
                datePickerDialog.showYearPickerFirst(false);
                datePickerDialog.dismissOnPause(true); //dismiss dialog when onPause() called?
                datePickerDialog.setAccentColor(getResources().getColor(R.color.Common_blue));
                //datePickerDialog.setMinDate(mCalender);
                datePickerDialog.setTitle("Training End Date");
                datePickerDialog.show(getFragmentManager(), DATE_TRAINING_END_DATE);

                break;

            case R.id.txt_posp_exam_date:
                datePickerDialog = DatePickerDialog.newInstance(Activity_POSP_RA_ExamTraining.this, mYear, mMonth, mDay);
                datePickerDialog.setThemeDark(false);
                datePickerDialog.showYearPickerFirst(false);
                datePickerDialog.dismissOnPause(true); //dismiss dialog when onPause() called?
                datePickerDialog.setAccentColor(getResources().getColor(R.color.Common_blue));
                datePickerDialog.setTitle("Exam Date");

                datePickerDialog.show(getFragmentManager(), DATE_EXAM_DATE);
                break;

            case R.id.btn_aob_exam_training_next:

                onNextClick();

                break;

            default:
                break;
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int mYear, int mMonth, int mDay) {

        String strSelectedDate = (mDay < 10 ? "0" : "") + mDay + "-"
                + ((mMonth + 1) < 10 ? "0" : "") + (mMonth + 1) + "-"
                + mYear;

        switch (view.getTag()) {
            case DATE_EXAM_DATE:
                txt_posp_exam_date.setText(strSelectedDate);
                break;

            case DATE_TRAINING_START_DATE:
                txt_aob_training_start_date.setText(strSelectedDate);
                break;

            case DATE_TRAINING_END_DATE:
                try {

                    /*SimpleDateFormat sdp = new SimpleDateFormat("dd-MM-yyyy");
                    Date selectedDate = sdp.parse(strSelectedDate);*/

                    String strStart = txt_aob_training_start_date.getText() == null
                            ? "" : txt_aob_training_start_date.getText().toString();

                    if (!strStart.equals("")) {

                        /*Date startDate = sdp.parse(strStart);

                        long diffDays = (selectedDate.getTime() - startDate.getTime())/(1000*60*60*24);

                        if (!startDate.after(selectedDate) && !startDate.equals(sdp.format(selectedDate))) {

                            if ((*//*diffDays >= 1 &&*//* diffDays < 3)){*/
                        txt_aob_training_end_date.setText(strSelectedDate);
                            /*}else {
                                mCommonMethods.showMessageDialog(mContext, "Minimum training days should be 2 days.");
                            }

                        }else {
                            mCommonMethods.showMessageDialog(mContext, "Please select End Date greater than Start Date!");
                        }*/

                    } else {
                        mCommonMethods.showToast(mContext, "Please enter Start Date first");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    public void onNextClick() {

        if (is_dashboard) {
            Intent mIntent = new Intent(Activity_POSP_RA_ExamTraining.this, Activity_POSP_RA_TermsConditionsDeclaration.class);
            mIntent.putExtra("is_dashboard", is_dashboard);
            startActivity(mIntent);
        } else if (is_bsm_questions) {
            Intent mIntent = new Intent(Activity_POSP_RA_ExamTraining.this, Activity_POSP_RA_TermsConditionsDeclaration.class);
            mIntent.putExtra("is_bsm_questions", is_bsm_questions);
            startActivity(mIntent);
        } else if (!is_dashboard && !is_bsm_questions) {
            //1. validate all details
            String str_error = validateDetails();
            if (str_error.equals("")) {

                //2. create xml string for data saving
                get_exam_training_xml();

                //3. update data against global row id
                ContentValues cv = new ContentValues();
                cv.put(db.POSP_RA_EXAM_TRAINING_DETAILS, str_exam_training_details.toString());

                cv.put(db.POSP_RA_UPDATED_BY, mCommonMethods.GetUserCode(mContext));

                Calendar c = Calendar.getInstance();
                //save date in long
                cv.put(db.POSP_RA_UPDATED_DATE, new Date(c.getTimeInMillis()).getTime() + "");
                cv.put(db.POSP_RA_IN_APP_STATUS, "6");
                cv.put(db.POSP_RA_IN_APP_STATUS_REMARK, "Terms and Condition Pending");

                int i = db.update_POSP_RA_details(cv, db.POSP_RA_ID + " =? ",
                        new String[]{Activity_POSP_RA_Authentication.row_details + ""});

                mCommonMethods.showToast(mContext, "Details saved Successfully : " + i);

                Intent mIntent = new Intent(Activity_POSP_RA_ExamTraining.this, Activity_POSP_RA_TermsConditionsDeclaration.class);
                if (is_rejection)
                    mIntent.putExtra("is_rejection", is_rejection);

                startActivity(mIntent);

            } else {
                mCommonMethods.showMessageDialog(mContext, str_error);
            }
        }
    }

    private void get_exam_training_xml() {

        //String str_exam_place = spnr_aob_exam_place.getSelectedItem().toString();
        String str_training_language = spnr_pos_training_lang.getSelectedItem().toString();
        String str_exam_language = spnr_aob_exam_language.getSelectedItem().toString();

        String str_training_start_date = "", str_training_end_date = "", str_exam_date = "", str_training_mode = "",
                str_training_required_hrs = "", str_institute_name = "", str_exam_mode = "", str_exam_body = "",
                str_obtained_marks = "", str_exam_status = "";
        String[] strArr;
        if (is_rejection) {

            str_training_start_date = txt_aob_training_start_date.getText().toString();
            if (!str_training_start_date.equals("")) {
                strArr = str_training_start_date.split("-");
                str_training_start_date = strArr[1] + "-" + strArr[0] + "-" + strArr[2];
            }

            str_training_end_date = txt_aob_training_end_date.getText().toString();
            if (!str_training_end_date.equals("")) {
                strArr = str_training_end_date.split("-");
                str_training_end_date = strArr[1] + "-" + strArr[0] + "-" + strArr[2];
            }

            str_exam_date = txt_posp_exam_date.getText().toString();
            if (!str_exam_date.equals("")) {
                strArr = str_exam_date.split("-");
                str_exam_date = strArr[1] + "-" + strArr[0] + "-" + strArr[2];
            }

            str_training_mode = spnr_posp_training_mode.getSelectedItem().toString();
            str_training_required_hrs = spnr_posp_required_hrs.getSelectedItem().toString();
            str_institute_name = spnr_posp_institute_name.getSelectedItem().toString();
            str_exam_mode = spnr_posp_exam_mode.getSelectedItem().toString();
            str_exam_body = spnr_posp_exam_body.getSelectedItem().toString();
            str_obtained_marks = edt_posp_mark_obtained.getText().toString();
            str_exam_status = spnr_posp_exam_status.getSelectedItem().toString();

        }

        //str_exam_training_details.append("<exam_training_details>");
        str_exam_training_details.append("<exam_details_place></exam_details_place>");
        str_exam_training_details.append("<exam_details_language>").append(str_exam_language).append("</exam_details_language>");
        str_exam_training_details.append("<training_details_start_date>").append(str_training_start_date).append("</training_details_start_date>");
        str_exam_training_details.append("<training_details_end_date>").append(str_training_end_date).append("</training_details_end_date>");

        str_exam_training_details.append("<training_details_language>").append(str_training_language).append("</training_details_language>");
        str_exam_training_details.append("<exam_details_exam_date>").append(str_exam_date).append("</exam_details_exam_date>");

        str_exam_training_details.append("<training_details_training_mode>").append(str_training_mode).append("</training_details_training_mode>");
        str_exam_training_details.append("<training_details_required_hrs>").append(str_training_required_hrs).append("</training_details_required_hrs>");
        str_exam_training_details.append("<training_details_institute_name>").append(str_institute_name).append("</training_details_institute_name>");
        str_exam_training_details.append("<exam_details_exam_mode>").append(str_exam_mode).append("</exam_details_exam_mode>");
        str_exam_training_details.append("<exam_details_exam_body>").append(str_exam_body).append("</exam_details_exam_body>");
        str_exam_training_details.append("<exam_details_marks_obtained>").append(str_obtained_marks).append("</exam_details_marks_obtained>");
        str_exam_training_details.append("<exam_details_exam_status>").append(str_exam_status).append("</exam_details_exam_status>");

        //str_exam_training_details.append("</exam_training_details>");

    }

    public String validateDetails() {

            /*if (spnr_aob_exam_place.getSelectedItem().toString().equals("Select Exam Place")){
                spnr_aob_exam_place.requestFocus();
                return "Please Select Exam Place";
            }*/

        String strError = "";

        if (spnr_pos_training_lang.getSelectedItem().toString().equals("Select Language")) {
            spnr_pos_training_lang.requestFocus();
            strError += "Please Select Training Language";
        }

        if (spnr_aob_exam_language.getSelectedItem().toString().equals("Select Language")) {
            spnr_aob_exam_language.requestFocus();
            strError += "Please Select Exam Language";
        }

        if (is_rejection) {
            if (txt_aob_training_start_date.getText().toString().equals("")) {
                txt_aob_training_start_date.requestFocus();
                strError += "Please Select Training Start Date";
            }

            if (txt_aob_training_end_date.getText().toString().equals("")) {
                txt_aob_training_end_date.requestFocus();
                strError += "Please Select Training End Date";
            }

            if (txt_posp_exam_date.getText().toString().equals("")) {
                txt_posp_exam_date.requestFocus();
                strError += "Please Select Exam Date";
            }

            String strMarks = edt_posp_mark_obtained.getText().toString();
            strMarks = strMarks == null ? "" : strMarks;
            if (strMarks.equals("")) {
                edt_posp_mark_obtained.requestFocus();
                strError += "Please Enter Obtained Marks in the Exam";
            } else if (Integer.parseInt(strMarks) < 9) {
                strError += "Minimum Marks obtained should be 9.";
            }

        }

        return strError;
    }

    /*public class AsynchGetAOBExamPlace extends AsyncTask<String, String, String> {

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

            if (mCommonMethods.isNetworkConnected(mContext)){

                try {
                    running = true;

                    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_GET_POSP_RA_EXAM_PLACE);
                    request.addProperty("Type", mCommonMethods.str_posp_ra_customer_type);
                    mCommonMethods.TLSv12Enable();

                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelope.dotNet = true;

                    envelope.setOutputSoapObject(request);

                    HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL);
                    try {
                        androidHttpTranport.call(NAMESPACE + METHOD_NAME_GET_POSP_RA_EXAM_PLACE, envelope);
                        //Object response = envelope.getResponse();

                        SoapPrimitive sa = null;
                        try {
                            sa = (SoapPrimitive) envelope.getResponse();

                            ParseXML mParse = new ParseXML();


                            List<String> lstParse = mParse.parseParentNode(mParse.parseXmlTag(sa.toString(), "NewDataSet"), "Table");

                            lstExamPlaces = new ArrayList<>();
                            for (String strParse: lstParse){

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

                    } catch (IOException e) {
                        try {
                            throw (e);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        running = false;
                    } catch (XmlPullParserException e) {
                        try {
                            throw (e);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        running = false;
                    }
                } catch (Exception e) {
                    try {
                        throw (e);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    running = false;
                }

            }else {
                mCommonMethods.showMessageDialog(mContext, mCommonMethods.NO_INTERNET_MESSAGE);
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strResult) {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();

            if (running){

                if (lstExamPlaces.size() > 0){

                    Collections.sort(lstExamPlaces);
                    lstExamPlaces.add(0, "Select Exam Place");

                    ArrayAdapter<String> exam_place_adapter = new ArrayAdapter<String>(mContext, R.layout.spinner_aob, lstExamPlaces);
                    spnr_aob_exam_place.setAdapter(exam_place_adapter);
                    exam_place_adapter.notifyDataSetChanged();

                    //set Data from DB
                    ArrayList<Pojo_POSP_RA> lstRes = db.get_posp_ra_details_by_ID(Activity_POSP_RA_Authentication.row_details);

                    if (lstRes.size() > 0) {

                        String str_exam_training_info = lstRes.get(0).getStr_exam_training_details();

                        str_exam_training_info = str_exam_training_info == null ? "" : str_exam_training_info;

                        if (!str_exam_training_info.equals("")){

                            String str_exam_place = mParseXML.parseXmlTag(str_exam_training_info, "exam_details_place");
                            String str_exam_language = mParseXML.parseXmlTag(str_exam_training_info, "exam_details_language");
                            String str_training_start_date = mParseXML.parseXmlTag(str_exam_training_info, "training_details_start_date");
                            String str_training_end_date = mParseXML.parseXmlTag(str_exam_training_info, "training_details_end_date");

                            spnr_aob_exam_place.setSelection(lstExamPlaces.indexOf(str_exam_place));

                            spnr_aob_exam_language.setSelection(Arrays.asList(getResources().
                                    getStringArray(R.array.arr_aob_exam_language)).indexOf(str_exam_language));

                            if (!str_training_start_date.equals("")){
                                String[] strArr = str_training_start_date.split("-");
                                txt_aob_training_start_date.setText(strArr[1]+"-"+strArr[0]+"-"+strArr[2]);
                            }else
                                txt_aob_training_start_date.setText("");

                            if (!str_training_end_date.equals("")){
                                String[] strArr = str_training_end_date.split("-");
                                txt_aob_training_end_date.setText(strArr[1]+"-"+strArr[0]+"-"+strArr[2]);
                            }else
                                txt_aob_training_end_date.setText("");
                        }
                    }

                }else {
                    mCommonMethods.showMessageDialog(mContext, "Sorry Exam Places Not Found.. Please try again");
                }

            }else {
                mCommonMethods.showMessageDialog(mContext, mCommonMethods.WEEK_INTERNET_MESSAGE);
            }
        }
    }*/

    public void enableDisableAllFields(boolean is_enable) {

        spnr_pos_training_lang.setEnabled(is_enable);

        spnr_aob_exam_language.setEnabled(is_enable);

        txt_aob_training_start_date.setEnabled(is_enable);

        txt_aob_training_end_date.setEnabled(is_enable);
    }
}
