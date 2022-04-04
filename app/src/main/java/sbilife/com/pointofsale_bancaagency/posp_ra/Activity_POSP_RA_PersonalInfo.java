package sbilife.com.pointofsale_bancaagency.posp_ra;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.AsyncUploadFile_Common;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.ekyc.offline_ekyc.OfflinePaperlessKycResponse;
import sbilife.com.pointofsale_bancaagency.ekyc.offline_ekyc.UidData;
import sbilife.com.pointofsale_bancaagency.ekyc.utilites.XMLUtilities;

public class Activity_POSP_RA_PersonalInfo extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener,
        DatePickerDialog.OnDateSetListener, AsyncUploadFile_Common.Interface_Upload_File_Common {

    private final String DATE_APPLICANT_DOB = "ApplicantDOB",
            DATE_APPLICANT_PASS_YEAR_MONTH = "ApplicantPassYear";

    private final String NAMESPACE = "http://tempuri.org/";
    private final String METHOD_NAME_VERIFY_PINCODE = "validatePincode";
    private final String METHOD_NAME_CHECK_EMAIL_MOBILE = "checkMobileEmail_other";
    private final String METHOD_NAME_GENERATE_OTP = "GenerateOTP_EasyAccess";
    private final String METHOD_NAME_VALIDATE_OTP = "validateOTP_easyacess";

    private CommonMethods mCommonMethods;
    private Context mContext;
    private DatabaseHelper db;
    private int mDOBDay = 0, mDOBYear = 0, mDOBMonth = 0;
    private Date currentDate;
    private Button btn_aob_personal_info_next, btn_aob_personal_permanent_pin, btn_aob_personal_comm_pin,
            btn_aob_personal_info_back, btn_posp_ra_verify_otp;
    private Spinner spnr_aob_personal_title, spnr_aob_personal_gender, spnr_posp_ra_nationality, spnr_aob_personal_relation_applicant,
            spnr_aob_personal_marital_status, spnr_aob_personal_caste_category, spnr_aob_personal_bas_qualification,
            spnr_aob_personal_pro_qualification;

    private EditText edt_aob_personal_full_name, edt_aob_personal_aadhaar_no, edt_aob_personal_pan_no,
            edt_aob_personal_communication_address1, edt_aob_personal_communication_address2, edt_aob_personal_communication_address3,
            edt_aob_personal_communication_pin, edt_aob_personal_permnt_address1, edt_aob_personal_permnt_address2,
            edt_aob_personal_permnt_address3, edt_aob_personal_permnt_pin, edt_aob_personal_father_husbund_name,
            edt_aob_personal_maiden_name, edt_aob_personal_mobile, edt_aob_personal_residence_no, edt_aob_personal_email_id,
            edt_aob_personal_pass_roll_no, edt_aob_personal_pass_university, edt_aob_personal_pro_qualification_cmnt,
            edt_posp_personal_nationality, edt_posp_ra_otp;

    private TextView txt_aob_personal_dob, txt_aob_personal_pass_year_month;

    private String str_aadhaar_no = "", str_pan_no = "", str_pin_flag = "", str_pincode = "", strUM = "",
            str_applicant_stored_email_id = "", str_applicant_stored_mobile_no = "";
    private StringBuilder str_personal_info;
    private boolean /*validate_pan_card = false, validate_aadhar = false,*/ validate_email = false, is_per_pin_valid = false,
            is_comm_pin_valid = false, is_dashboard = false, is_back_pressed = false, is_bsm_questions = false,
            is_otp_generated = false, is_rejection = false;

    private LinearLayout ll_aob_personal_maiden_name, ll_personal_info_communication_address,
            ll_aob_personal_pass_university, ll_posp_ra_verify_otp;

    private RadioGroup rg_aob_personal_info_same_permanent;
    private RadioButton rb_aob_personal_info_same_add_as_yes, rb_aob_personal_info_same_add_as_no;

    private ProgressDialog mProgressDialog;

    private DatePickerDialog datePickerDialog;
    private Calendar mCalender;

    private ParseXML mParseXML;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onResume();
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_posp_ra_personal_info);
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

            btn_aob_personal_info_next.setText("Next");
        } else if (is_rejection) {
            //editable
            enableDisableAllFields(true);
            btn_aob_personal_info_next.setText("Send OTP");
        } else {
            //editable
            enableDisableAllFields(true);
            btn_aob_personal_info_next.setText("Send OTP");
        }


        if (edt_aob_personal_communication_address1.getText().length() != 0)
            edt_aob_personal_communication_address1.setEnabled(false);

        if (edt_aob_personal_communication_address2.getText().length() != 0)
            edt_aob_personal_communication_address2.setEnabled(false);

        if (edt_aob_personal_communication_address3.getText().length() != 0)
            edt_aob_personal_communication_address3.setEnabled(false);

        if (edt_aob_personal_communication_pin.getText().length() != 0)
            edt_aob_personal_communication_pin.setEnabled(false);

    }

    public void initialisation() {

        mContext = this;
        mCommonMethods = new CommonMethods();

        mCommonMethods.setApplicationToolbarMenu1(this, "POSP-RA");

        db = new DatabaseHelper(mContext);

        mParseXML = new ParseXML();

        spnr_aob_personal_title = (Spinner) findViewById(R.id.spnr_aob_personal_title);
        ArrayAdapter<String> title_adapter = new ArrayAdapter<String>(
                mContext, R.layout.spinner_aob, getResources().getStringArray(R.array.arr_aob_title));
        spnr_aob_personal_title.setAdapter(title_adapter);
        title_adapter.notifyDataSetChanged();
        spnr_aob_personal_title.setOnItemSelectedListener(this);

        spnr_aob_personal_gender = (Spinner) findViewById(R.id.spnr_aob_personal_gender);
        ArrayAdapter<String> gender_adapter = new ArrayAdapter<String>(
                mContext, R.layout.spinner_aob, getResources().getStringArray(R.array.gender_arrays));
        spnr_aob_personal_gender.setAdapter(gender_adapter);
        gender_adapter.notifyDataSetChanged();

        spnr_posp_ra_nationality = (Spinner) findViewById(R.id.spnr_posp_ra_nationality);
        ArrayAdapter<String> nationality_adapter = new ArrayAdapter<String>(
                mContext, R.layout.spinner_aob, getResources().getStringArray(R.array.arr_posp_ra_nationality));
        spnr_posp_ra_nationality.setAdapter(nationality_adapter);
        nationality_adapter.notifyDataSetChanged();
        spnr_posp_ra_nationality.setOnItemSelectedListener(this);

        edt_posp_personal_nationality = findViewById(R.id.edt_posp_personal_nationality);
        edt_posp_ra_otp = findViewById(R.id.edt_posp_ra_otp);

        spnr_aob_personal_relation_applicant = (Spinner) findViewById(R.id.spnr_aob_personal_relation_applicant);
        ArrayAdapter<String> relation_applicant_adapter = new ArrayAdapter<String>(
                mContext, R.layout.spinner_aob, getResources().getStringArray(R.array.arr_aob_relation_with_applicant));
        spnr_aob_personal_relation_applicant.setAdapter(relation_applicant_adapter);
        relation_applicant_adapter.notifyDataSetChanged();

        spnr_aob_personal_marital_status = (Spinner) findViewById(R.id.spnr_aob_personal_marital_status);
        ArrayAdapter<String> marital_status_adapter = new ArrayAdapter<String>(
                mContext, R.layout.spinner_aob, getResources().getStringArray(R.array.arr_aob_marital_status));
        spnr_aob_personal_marital_status.setAdapter(marital_status_adapter);
        marital_status_adapter.notifyDataSetChanged();

        spnr_aob_personal_caste_category = (Spinner) findViewById(R.id.spnr_aob_personal_caste_category);
        ArrayAdapter<String> caste_category_adapter = new ArrayAdapter<String>(
                mContext, R.layout.spinner_aob, getResources().getStringArray(R.array.arr_aob_caste_category));
        spnr_aob_personal_caste_category.setAdapter(caste_category_adapter);
        caste_category_adapter.notifyDataSetChanged();

        spnr_aob_personal_bas_qualification = (Spinner) findViewById(R.id.spnr_aob_personal_bas_qualification);
        ArrayAdapter<String> bas_qualification_adapter = new ArrayAdapter<String>(
                mContext, R.layout.spinner_aob, getResources().getStringArray(R.array.arr_aob_basic_qualification));
        spnr_aob_personal_bas_qualification.setAdapter(bas_qualification_adapter);
        bas_qualification_adapter.notifyDataSetChanged();

        spnr_aob_personal_pro_qualification = (Spinner) findViewById(R.id.spnr_aob_personal_pro_qualification);
        ArrayAdapter<String> pro_qualification_adapter = new ArrayAdapter<String>(
                mContext, R.layout.spinner_aob, getResources().getStringArray(R.array.arr_aob_pro_qualification));
        spnr_aob_personal_pro_qualification.setAdapter(pro_qualification_adapter);
        spnr_aob_personal_pro_qualification.setOnItemSelectedListener(this);
        pro_qualification_adapter.notifyDataSetChanged();

        txt_aob_personal_dob = (TextView) findViewById(R.id.txt_aob_personal_dob);
        txt_aob_personal_dob.setOnClickListener(this);
        txt_aob_personal_pass_year_month = (TextView) findViewById(R.id.txt_aob_personal_pass_year_month);
        txt_aob_personal_pass_year_month.setOnClickListener(this);

        btn_aob_personal_info_next = (Button) findViewById(R.id.btn_aob_personal_info_next);
        btn_aob_personal_info_next.setOnClickListener(this);

        btn_aob_personal_info_back = (Button) findViewById(R.id.btn_aob_personal_info_back);
        btn_aob_personal_info_back.setOnClickListener(this);

        btn_aob_personal_permanent_pin = (Button) findViewById(R.id.btn_aob_personal_permanent_pin);
        btn_aob_personal_permanent_pin.setOnClickListener(this);

        btn_aob_personal_comm_pin = (Button) findViewById(R.id.btn_aob_personal_comm_pin);
        btn_aob_personal_comm_pin.setOnClickListener(this);

        btn_posp_ra_verify_otp = findViewById(R.id.btn_posp_ra_verify_otp);
        btn_posp_ra_verify_otp.setOnClickListener(this);

        edt_aob_personal_full_name = (EditText) findViewById(R.id.edt_aob_personal_full_name);
        //edt_aob_personal_full_name.setFilters(new InputFilter[]{mCommonMethods.aob_address_filter});

        edt_aob_personal_aadhaar_no = (EditText) findViewById(R.id.edt_aob_personal_aadhaar_no);
        edt_aob_personal_pan_no = (EditText) findViewById(R.id.edt_aob_personal_pan_no);

        edt_aob_personal_communication_address1 = (EditText) findViewById(R.id.edt_aob_personal_communication_address1);
        //edt_aob_personal_communication_address1.setFilters(new InputFilter[]{mCommonMethods.aob_address_filter});

        edt_aob_personal_communication_address2 = (EditText) findViewById(R.id.edt_aob_personal_communication_address2);
        //edt_aob_personal_communication_address2.setFilters(new InputFilter[]{mCommonMethods.aob_address_filter});

        edt_aob_personal_communication_address3 = (EditText) findViewById(R.id.edt_aob_personal_communication_address3);
        //edt_aob_personal_communication_address3.setFilters(new InputFilter[]{mCommonMethods.aob_address_filter});

        edt_aob_personal_communication_pin = (EditText) findViewById(R.id.edt_aob_personal_communication_pin);

        edt_aob_personal_permnt_address1 = (EditText) findViewById(R.id.edt_aob_personal_permnt_address1);
        //edt_aob_personal_permnt_address1.setFilters(new InputFilter[]{mCommonMethods.aob_address_filter});

        edt_aob_personal_permnt_address2 = (EditText) findViewById(R.id.edt_aob_personal_permnt_address2);
        //edt_aob_personal_permnt_address2.setFilters(new InputFilter[]{mCommonMethods.aob_address_filter});

        edt_aob_personal_permnt_address3 = (EditText) findViewById(R.id.edt_aob_personal_permnt_address3);
        //edt_aob_personal_permnt_address3.setFilters(new InputFilter[]{mCommonMethods.aob_address_filter});

        edt_aob_personal_permnt_pin = (EditText) findViewById(R.id.edt_aob_personal_permnt_pin);

        edt_aob_personal_father_husbund_name = (EditText) findViewById(R.id.edt_aob_personal_father_husbund_name);
        //edt_aob_personal_father_husbund_name.setFilters(new InputFilter[]{mCommonMethods.aob_address_filter});

        edt_aob_personal_maiden_name = (EditText) findViewById(R.id.edt_aob_personal_maiden_name);
        edt_aob_personal_mobile = (EditText) findViewById(R.id.edt_aob_personal_mobile);
        edt_aob_personal_mobile.setFilters(new InputFilter[]{mCommonMethods.aob_mobile_filter});

        edt_aob_personal_residence_no = (EditText) findViewById(R.id.edt_aob_personal_residence_no);
        edt_aob_personal_residence_no.setFilters(new InputFilter[]{mCommonMethods.aob_mobile_filter});

        edt_aob_personal_email_id = (EditText) findViewById(R.id.edt_aob_personal_email_id);
        edt_aob_personal_pass_roll_no = (EditText) findViewById(R.id.edt_aob_personal_pass_roll_no);
        edt_aob_personal_pass_university = (EditText) findViewById(R.id.edt_aob_personal_pass_university);
        edt_aob_personal_pass_university.setFilters(new InputFilter[]{mCommonMethods.aob_mobile_filter});

        edt_aob_personal_pro_qualification_cmnt = (EditText) findViewById(R.id.edt_aob_personal_pro_qualification_cmnt);

        ll_aob_personal_maiden_name = (LinearLayout) findViewById(R.id.ll_aob_personal_maiden_name);
        ll_personal_info_communication_address = (LinearLayout) findViewById(R.id.ll_personal_info_communication_address);
        ll_aob_personal_pass_university = (LinearLayout) findViewById(R.id.ll_aob_personal_pass_university);
        ll_posp_ra_verify_otp = findViewById(R.id.ll_posp_ra_verify_otp);

        rg_aob_personal_info_same_permanent = (RadioGroup) findViewById(R.id.rg_aob_personal_info_same_permanent);
        rb_aob_personal_info_same_add_as_yes = (RadioButton) findViewById(R.id.rb_aob_personal_info_same_add_as_yes);
        rb_aob_personal_info_same_add_as_no = (RadioButton) findViewById(R.id.rb_aob_personal_info_same_add_as_no);

        rg_aob_personal_info_same_permanent.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_aob_personal_info_same_add_as_yes:
                        //copy applicant address and save as nominee address
                        ll_personal_info_communication_address.setVisibility(View.GONE);
                        break;

                    case R.id.rb_aob_personal_info_same_add_as_no:
                        ll_personal_info_communication_address.setVisibility(View.VISIBLE);
                        break;

                    default:
                        break;
                }
            }
        });


        edt_aob_personal_communication_pin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                is_per_pin_valid = false;

                if (s.toString().length() == 6) {
                    btn_aob_personal_permanent_pin.setEnabled(true);

                    edt_aob_personal_communication_pin.setError(null);
                } else {
                    btn_aob_personal_permanent_pin.setEnabled(false);

                    edt_aob_personal_communication_pin.setError("Enter valid pincode");
                }

            }
        });

        edt_aob_personal_permnt_pin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                is_comm_pin_valid = false;

                if (s.toString().length() == 6) {
                    btn_aob_personal_comm_pin.setEnabled(true);

                    edt_aob_personal_permnt_pin.setError(null);
                } else {
                    btn_aob_personal_comm_pin.setEnabled(false);

                    edt_aob_personal_permnt_pin.setError("Enter valid pincode");
                }

            }
        });

        //ekyc removed
        /*edt_aob_personal_aadhaar_no.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                str_aadhaar_no = edt_aob_personal_aadhaar_no.getText().toString().replaceAll("\\s+", "").trim();

                if (str_aadhaar_no.length() == 12) {
                    if (!Verhoeff.validateVerhoeff(str_aadhaar_no)) {
                        edt_aob_personal_aadhaar_no.setError("Incorrect Aadhaar Number");
                        validate_aadhar = false;
                    } else {
                        //edt_aob_auth_aadhaar.setError(null);
                        validate_aadhar = true;
                    }
                } else {
                    edt_aob_personal_aadhaar_no.setError("Incorrect Aadhaar Number");
                    validate_aadhar = false;
                }
            }
        });*/

        /*edt_aob_personal_pan_no.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                str_pan_no = edt_aob_personal_pan_no.getText().toString().replaceAll("\\s+", "").trim();
                validate_pan_card = mCommonMethods.valPancard(str_pan_no, edt_aob_personal_pan_no);
            }
        });*/

        edt_aob_personal_email_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validate_email = mCommonMethods.emailPatternValidation(edt_aob_personal_email_id, mContext);
            }
        });

        str_personal_info = new StringBuilder();

        mCalender = Calendar.getInstance();
        mDOBYear = mCalender.get(Calendar.YEAR);
        mDOBMonth = mCalender.get(Calendar.MONTH);
        mDOBDay = mCalender.get(Calendar.DAY_OF_MONTH);

        currentDate = new Date(mCalender.getTimeInMillis());

        //set Data from DB
        ArrayList<Pojo_POSP_RA> lstRes = db.get_posp_ra_details_by_ID(Activity_POSP_RA_Authentication.row_details);

        if (lstRes.size() > 0) {

            edt_aob_personal_aadhaar_no.setText(lstRes.get(0).getStr_aadhaar_no());
            //str_pan_no = lstRes.get(0).getStr_pan_no() == null ? "" : lstRes.get(0).getStr_pan_no();
            edt_aob_personal_pan_no.setText(lstRes.get(0).getStr_pan_no());
            strUM = lstRes.get(0).getStr_created_by();

            String str_personal_info = lstRes.get(0).getStr_personal_info();
            str_personal_info = str_personal_info == null ? "" : str_personal_info;

            if (!str_personal_info.equals("")) {

                //get available data
                String str_applicant_title = mParseXML.parseXmlTag(str_personal_info, "personal_info_title");
                String str_applicant_full_name = mParseXML.parseXmlTag(str_personal_info, "personal_info_full_name");
                String str_applicant_dob = mParseXML.parseXmlTag(str_personal_info, "personal_info_dob");
                String str_applicant_gender = mParseXML.parseXmlTag(str_personal_info, "personal_info_gender");

                String str_applicant_aadhaar = mParseXML.parseXmlTag(str_personal_info, "personal_info_aadhaar_no");
                String str_applicant_pan = mParseXML.parseXmlTag(str_personal_info, "personal_info_pan_no");

                String str_applicant_nationality = mParseXML.parseXmlTag(str_personal_info, "personal_info_nationality");

                String str_applicant_permanent_add1 = mParseXML.parseXmlTag(str_personal_info, "personal_info_permanant_address1");
                String str_applicant_permanent_add2 = mParseXML.parseXmlTag(str_personal_info, "personal_info_permanant_address2");
                String str_applicant_permanent_add3 = mParseXML.parseXmlTag(str_personal_info, "personal_info_permanant_address3");
                String str_applicant_permanent_add_pin = mParseXML.parseXmlTag(str_personal_info, "personal_info_permanant_address_pin");

                String str_applicant_permanent_add_same = mParseXML.parseXmlTag(str_personal_info, "personal_info_communication_add_same");

                String str_applicant_communication_add1 = mParseXML.parseXmlTag(str_personal_info, "personal_info_communication_address1");
                String str_applicant_communication_add2 = mParseXML.parseXmlTag(str_personal_info, "personal_info_communication_address2");
                String str_applicant_communication_add3 = mParseXML.parseXmlTag(str_personal_info, "personal_info_communication_address3");

                String str_applicant_communication_add_pin = mParseXML.parseXmlTag(str_personal_info, "personal_info_communication_address_pin");

                String str_applicant_father_husband_name = mParseXML.parseXmlTag(str_personal_info, "personal_info_father_husband_name");
                String str_applicant_relation = mParseXML.parseXmlTag(str_personal_info, "personal_info_relation_with_applicant");
                String str_applicant_maiden_name = mParseXML.parseXmlTag(str_personal_info, "personal_info_maiden_name");
                String str_applicant_marital_status = mParseXML.parseXmlTag(str_personal_info, "personal_info_marital_status");
                String str_applicant_caste_category = mParseXML.parseXmlTag(str_personal_info, "personal_info_caste_category");
                str_applicant_stored_mobile_no = mParseXML.parseXmlTag(str_personal_info, "personal_info_mobile_no");
                String str_applicant_residence_no = mParseXML.parseXmlTag(str_personal_info, "personal_info_residence_no");
                str_applicant_stored_email_id = mParseXML.parseXmlTag(str_personal_info, "personal_info_email_id");
                String str_applicant_basic_qualification = mParseXML.parseXmlTag(str_personal_info, "personal_info_educational_details_basic_qualification");
                String str_applicant_passing_roll_no = mParseXML.parseXmlTag(str_personal_info, "personal_info_educational_details_passing_roll_no");
                String str_applicant_passing_university = mParseXML.parseXmlTag(str_personal_info, "personal_info_educational_details_passing_university");
                String str_applicant_passing_year_month = mParseXML.parseXmlTag(str_personal_info, "personal_info_educational_details_passing_month_year");
                String str_applicant_pro_qualification = mParseXML.parseXmlTag(str_personal_info, "personal_info_educational_details_professional_qualification");
                String str_applicant_pro_qualification_others = mParseXML.parseXmlTag(str_personal_info, "personal_info_educational_details_professional_qualification_others");

                spnr_aob_personal_title.setSelection(Arrays.asList(getResources().getStringArray(R.array.arr_aob_title)).indexOf(str_applicant_title));

                edt_aob_personal_full_name.setText(str_applicant_full_name);

                if (!str_applicant_dob.equals("")) {
                    String[] arrDate = str_applicant_dob.split("-");
                    txt_aob_personal_dob.setText(arrDate[1] + "-" + arrDate[0] + "-" + arrDate[2]);
                } else {
                    txt_aob_personal_dob.setText("");
                }

                if (str_applicant_nationality.equals("Indian")) {
                    spnr_posp_ra_nationality.setSelection(0);
                } else {
                    spnr_posp_ra_nationality.setSelection(1);
                    edt_posp_personal_nationality.setText(str_applicant_nationality);
                }

                edt_aob_personal_aadhaar_no.setText(str_applicant_aadhaar);

                spnr_aob_personal_gender.setSelection(Arrays.asList(getResources().
                        getStringArray(R.array.gender_arrays)).indexOf(str_applicant_gender));

                edt_aob_personal_communication_address1.setText(str_applicant_permanent_add1);
                edt_aob_personal_communication_address1.setEnabled(false);
                edt_aob_personal_communication_address2.setText(str_applicant_permanent_add2);
                edt_aob_personal_communication_address3.setText(str_applicant_permanent_add3);
                edt_aob_personal_communication_pin.setText(str_applicant_permanent_add_pin);
                edt_aob_personal_communication_pin.setEnabled(false);

                if (str_applicant_permanent_add_same.equals("true")) {
                    ll_personal_info_communication_address.setVisibility(View.GONE);
                    rb_aob_personal_info_same_add_as_yes.setChecked(true);
                    rb_aob_personal_info_same_add_as_no.setChecked(false);

                } else {
                    ll_personal_info_communication_address.setVisibility(View.VISIBLE);
                    rb_aob_personal_info_same_add_as_yes.setChecked(false);
                    rb_aob_personal_info_same_add_as_no.setChecked(true);

                    edt_aob_personal_permnt_address1.setText(str_applicant_communication_add1);
                    edt_aob_personal_permnt_address2.setText(str_applicant_communication_add2);
                    edt_aob_personal_permnt_address3.setText(str_applicant_communication_add3);
                    edt_aob_personal_permnt_pin.setText(str_applicant_communication_add_pin);
                }

                edt_aob_personal_father_husbund_name.setText(str_applicant_father_husband_name);

                spnr_aob_personal_relation_applicant.setSelection(Arrays.asList(getResources().
                        getStringArray(R.array.arr_aob_relation_with_applicant)).indexOf(str_applicant_relation));

                spnr_aob_personal_relation_applicant.setSelection(Arrays.asList(getResources().
                        getStringArray(R.array.arr_aob_relation_with_applicant)).indexOf(str_applicant_relation));

                if (str_applicant_maiden_name.equals("")) {
                    ll_aob_personal_maiden_name.setVisibility(View.GONE);
                } else {
                    ll_aob_personal_maiden_name.setVisibility(View.VISIBLE);

                    edt_aob_personal_maiden_name.setText(str_applicant_maiden_name);
                }

                spnr_aob_personal_marital_status.setSelection(Arrays.asList(getResources().
                        getStringArray(R.array.arr_aob_marital_status)).indexOf(str_applicant_marital_status));

                spnr_aob_personal_caste_category.setSelection(Arrays.asList(getResources().
                        getStringArray(R.array.arr_aob_caste_category)).indexOf(str_applicant_caste_category));

                edt_aob_personal_mobile.setText(str_applicant_stored_mobile_no);
                edt_aob_personal_residence_no.setText(str_applicant_residence_no);
                edt_aob_personal_email_id.setText(str_applicant_stored_email_id);

                spnr_aob_personal_bas_qualification.setSelection(Arrays.asList(getResources().
                        getStringArray(R.array.arr_aob_basic_qualification)).indexOf(str_applicant_basic_qualification));

                edt_aob_personal_pass_roll_no.setText(str_applicant_passing_roll_no);
                edt_aob_personal_pass_university.setText(str_applicant_passing_university);
                txt_aob_personal_pass_year_month.setText(str_applicant_passing_year_month);

                spnr_aob_personal_pro_qualification.setSelection(Arrays.asList(getResources().
                        getStringArray(R.array.arr_aob_pro_qualification)).indexOf(str_applicant_pro_qualification));

                edt_aob_personal_pro_qualification_cmnt.setText(str_applicant_pro_qualification_others);
                /*if (str_applicant_pro_qualification.equalsIgnoreCase("Others")) {
                    ll_aob_personal_pass_university.setVisibility(View.VISIBLE);

                } else {
                    ll_aob_personal_pass_university.setVisibility(View.GONE);
                }*/
            } else {
                try {

                    //ekyc removed
                    /*String str_aadhaar_details = SimpleCrypto.decrypt("SBIL", lstRes.get(0).getStr_aadhaar_details().toString());

                    str_aadhaar_details = str_aadhaar_details == null ? "" : str_aadhaar_details;

                    if (!str_aadhaar_details.equals("")) {
                        final ECSKycResponse res = (ECSKycResponse) XMLUtilities.parseXML(ECSKycResponse.class, str_aadhaar_details);

                        String str_aob_aadhar_dob = res.getPoi().getDob() == null ? ""
                                : res.getPoi().getDob();
                        txt_aob_personal_dob.setText(str_aob_aadhar_dob);

                        String str_aob_aadhar_name = res.getPoi().getDob() == null ? "" : res.getPoi().getName();
                        edt_aob_personal_full_name.setText(str_aob_aadhar_name);

                        String str_Address_title = res.getPoa().getCo() == null ? ""
                                : res.getPoa().getCo();
                        String str_aad_Address1 = (res.getPoa().getLoc() == null ? ""
                                : res.getPoa().getLoc() + " ")
                                + (res.getPoa().getHouse() == null ? "" : res
                                .getPoa().getHouse() + " ")
                                + (res.getPoa().getStreet() == null ? "" : res
                                .getPoa().getStreet() + " ")
                                + (res.getPoa().getLm() == null ? "" : res.getPoa()
                                .getLm());
                        String str_aad_Address2 = (res.getPoa().getSubdist() == null ? ""
                                : res.getPoa().getSubdist() + " ")
                                + (res.getPoa().getPo() == null ? "" : res.getPoa()
                                .getPo() + " ");
                        String str_aad_Address3 = (res.getPoa().getVtc() == null ? ""
                                : res.getPoa().getVtc());
                        String str_PinCode = (res.getPoa().getPc() == null ? ""
                                : res.getPoa().getPc());
                        String str_City = (res.getPoa().getDist() == null ? ""
                                : res.getPoa().getDist());
                        String str_State = (res.getPoa().getState() == null ? ""
                                : res.getPoa().getState());

                                String str_aob_aadhaar_address = str_Address_title + "\n"
                                        + str_aad_Address1 + "\n" + str_aad_Address2
                                        + str_aad_Address3 + "\n" + str_PinCode + "\n"
                                        + str_City + "\n" + str_State;

                        edt_aob_personal_permanent_address1.setText(str_aad_Address1);
                        edt_aob_personal_permanent_address2.setText(str_aad_Address2);
                        edt_aob_personal_permanent_address3.setText(str_aad_Address3);
                        edt_aob_personal_permanent_pin.setText(str_PinCode);

                        edt_aob_personal_comm_address1.setText(str_aad_Address1);
                        edt_aob_personal_comm_address2.setText(str_aad_Address2);
                        edt_aob_personal_comm_address3.setText(str_aad_Address3);
                        edt_aob_personal_comm_pin.setText(str_PinCode);

                        String str_aob_aadhar_email_id = res.getPoi().getEmail() == null ? "" : res.getPoi().getEmail();
                        edt_aob_personal_email_id.setText(str_aob_aadhar_email_id);

                        String str_aob_aadhar_mobile_no = res.getPoi().getPhone() == null ? "" : res.getPoi().getPhone();
                        edt_aob_personal_mobile.setText(str_aob_aadhar_mobile_no);
                    }*/

                    //get PAN details
                    String str_pan_details = lstRes.get(0).getStr_pan_details().toString();

                    str_pan_details = str_pan_details == null ? "" : str_pan_details;

                    if (!str_pan_details.equals("")) {

                        edt_aob_personal_pan_no.setText(lstRes.get(0).getStr_pan_no());

                        str_pan_details = mParseXML.parseXmlTag(str_pan_details,
                                "PANDETAILS");

                        String str_ReturnCode = mParseXML.parseXmlTag(str_pan_details, "RETURNCODE");
                        if (str_ReturnCode.equals("1")) {

                            /*String str_Pan_no = mParseXML.parseXmlTag(str_pan_details, "PANNO");
                            edt_aob_personal_pan_no.setText(str_Pan_no);*/

                            edt_aob_personal_pan_no.setText(lstRes.get(0).getStr_pan_no());

                                    /*String str_Pan_Status = mParseXML.parseXmlTag(
                                            str_pan_details, "PANSTATUS");*/

                            String strPANTitle = mParseXML.parseXmlTag(str_pan_details, "TITLE");
                            if (strPANTitle == null || strPANTitle.equalsIgnoreCase("NA")) {
                                strPANTitle = "";
                            }

                            String strPANFirstName = mParseXML.parseXmlTag(str_pan_details, "FIRSTNAME");
                            if (strPANFirstName == null || strPANFirstName.equalsIgnoreCase("NA")) {
                                strPANFirstName = "";
                            }

                            String strPANMiddleName = mParseXML.parseXmlTag(str_pan_details, "MIDDLENAME");
                            if (strPANMiddleName == null || strPANMiddleName.equalsIgnoreCase("NA")) {
                                strPANMiddleName = "";
                            }

                            String strPANLastName = mParseXML.parseXmlTag(str_pan_details, "LASTNAME");
                            if (strPANLastName == null || strPANLastName.equalsIgnoreCase("NA")) {
                                strPANLastName = "";
                            }

                            if (!strPANTitle.equals("")) {
                                spnr_aob_personal_title.setSelection(Arrays.asList(getResources().getStringArray(R.array.arr_aob_title)).indexOf(strPANTitle));
                            }

                            edt_aob_personal_full_name.setText(strPANFirstName + " " + strPANMiddleName + " " + strPANLastName);

                        } else
                            edt_aob_personal_full_name.setText("");
                    }

                    //get offline ekyc details
                    String str_aadhaar_details = lstRes.get(0).getStr_aadhaar_details().toString();

                    str_aadhaar_details = str_aadhaar_details == null ? "" : str_aadhaar_details;

                    if (!str_aadhaar_details.equals("")) {
                        final OfflinePaperlessKycResponse res = (OfflinePaperlessKycResponse) XMLUtilities
                                .parseXML(OfflinePaperlessKycResponse.class, str_aadhaar_details);

                        final UidData res_UidData = res.getUidData();

                        String str_Address_title = res_UidData.getPoa().getCo() == null ? ""
                                : res_UidData.getPoa().getCo();

                        String str_aad_Address1 = (res_UidData.getPoa().getLoc() == null ? ""
                                : res_UidData.getPoa().getLoc() + " ")
                                + (res_UidData.getPoa().getHouse() == null ? "" : res_UidData
                                .getPoa().getHouse() + " ")
                                + (res_UidData.getPoa().getStreet() == null ? "" : res_UidData
                                .getPoa().getStreet() + " ")
                                + (res_UidData.getPoa().getLm() == null ? "" : res_UidData.getPoa()
                                .getLm());
                        String str_aad_Address2 = (res_UidData.getPoa().getSubdist() == null ? ""
                                : res_UidData.getPoa().getSubdist() + " ")
                                + (res_UidData.getPoa().getPo() == null ? "" : res_UidData.getPoa()
                                .getPo() + " ");

                        String str_aad_Address3 = (res_UidData.getPoa().getVtc() == null ? ""
                                : res_UidData.getPoa().getVtc());
                        String str_PinCode = (res_UidData.getPoa().getPc() == null ? ""
                                : res_UidData.getPoa().getPc());

                        if (!str_PinCode.equals("")) {
                            edt_aob_personal_communication_pin.setText(str_PinCode);
                            edt_aob_personal_communication_pin.setEnabled(false);
                            is_comm_pin_valid = true;
                        } else {
                            edt_aob_personal_communication_pin.setEnabled(true);
                            is_comm_pin_valid = false;
                        }

                        String str_City = (res_UidData.getPoa().getDist() == null ? ""
                                : res_UidData.getPoa().getDist());
                        String str_State = (res_UidData.getPoa().getState() == null ? ""
                                : res_UidData.getPoa().getState());

                    /*str_QR_code_mailID = res_UidData.getPoi().getEmail() == null ? ""
                            : res_UidData.getPoi().getEmail();
                    str_QR_code_mobile = res_UidData.getPoi().getPhone() == null ? ""
                            : res_UidData.getPoi().getPhone();*/

                        String str_ekyc_code_DOB = res_UidData.getPoi().getDob() == null ? ""
                                : res_UidData.getPoi().getDob();
                        if (!mCommonMethods.isThisDateValid(str_ekyc_code_DOB)) {
                            str_ekyc_code_DOB = "01-01-1990";
                        }

                        txt_aob_personal_dob.setText(str_ekyc_code_DOB);

                        String str_ekyc_code_Name = res_UidData.getPoi().getName() == null ? ""
                                : res_UidData.getPoi().getName();

                        String str_ekyc_code_Gender = res_UidData.getPoi().getGender() == null ? ""
                                : res_UidData.getPoi().getGender();
                        if (!str_ekyc_code_Gender.equals("")) {
                            if (str_ekyc_code_Gender.equals("M")) {
                                spnr_aob_personal_gender.setSelection(0);
                            } else if (str_ekyc_code_Gender.equals("F")) {
                                spnr_aob_personal_gender.setSelection(1);
                            } else {
                                spnr_aob_personal_gender.setSelection(2);
                            }

                            spnr_aob_personal_gender.setEnabled(false);
                        }


                        /*str_QR_code_Photo = res.getUidData().getPht() == null ? ""
                                : res.getUidData().getPht();*/

                        if (!(str_Address_title.equals(""))) {
                            str_Address_title = str_Address_title.substring(0, 3);
                        } else {
                            str_Address_title = "C/O";
                        }

                        String str_Address1 = "", str_Address2 = "", str_Address3 = "";

                        String[] lines = mCommonMethods.Split_EKYC((str_aad_Address1 + " "
                                        + str_aad_Address2 + " " + str_aad_Address3), 45,
                                135);
                        for (int i = 0; i < lines.length; i++) {

                            if (i == 0) {
                                str_Address1 = lines[i];
                            } else if (i == 1) {
                                str_Address2 = lines[i];

                            } else if (i == 2) {
                                str_Address3 = lines[i];
                            }

                        }

                        if (!str_Address_title.equalsIgnoreCase("")) {
                            str_Address_title = str_Address_title + ", ";
                        }

                        if (!str_Address1.equalsIgnoreCase("")) {
                            str_Address1 = str_Address1 + ", ";
                        }
                        if (!str_Address2.equalsIgnoreCase("")) {
                            str_Address2 = str_Address2 + ", ";
                        }
                        if (!str_Address3.equalsIgnoreCase("")) {
                            str_Address3 = str_Address3 + "-";
                        }
                        if (!str_PinCode.equalsIgnoreCase("")) {
                            str_PinCode = str_PinCode + ", ";
                        }
                        if (!str_City.equalsIgnoreCase("")) {
                            str_City = str_City + ".";
                        }

                        //str_Address1 = str_Address_title + str_Address1;

                        String str_ekyc_code_address = str_Address_title + str_Address1
                                + str_Address2 + str_Address3 + str_City
                                + str_State;

                        if (str_ekyc_code_address.length() > 100)
                            str_ekyc_code_address = str_ekyc_code_address.substring(0, 99);

                        /*if (str_Address2.length() > 100){
                            str_Address2 = str_Address2.substring(0, 99);
                        }

                        if (str_Address3.length() > 100){
                            str_Address3 = str_Address3.substring(0, 99);
                        }*/

                        edt_aob_personal_communication_address1.setText(str_ekyc_code_address);
                        edt_aob_personal_communication_address2.setText(str_Address2);
                        edt_aob_personal_communication_address3.setText(str_Address3);
                        edt_aob_personal_communication_pin.setText(str_PinCode);

                        edt_aob_personal_permnt_address1.setText(str_aad_Address1);
                        edt_aob_personal_permnt_address2.setText(str_aad_Address2);
                        edt_aob_personal_permnt_address3.setText(str_aad_Address3);
                        edt_aob_personal_permnt_pin.setText(str_PinCode);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {

            case R.id.spnr_aob_personal_title:

                if (position == 1) {
                    spnr_aob_personal_gender.setSelection(0);

                    ll_aob_personal_maiden_name.setVisibility(View.GONE);

                } else if (position == 2) {
                    spnr_aob_personal_gender.setSelection(1);

                    ll_aob_personal_maiden_name.setVisibility(View.VISIBLE);
                } else if (position == 3) {
                    //set gender
                    spnr_aob_personal_gender.setSelection(1);

                    //maiden name gone
                    ll_aob_personal_maiden_name.setVisibility(View.GONE);
                } else if (position == 4) {
                    //set gender
                    spnr_aob_personal_gender.setSelection(2);

                    //maiden name gone
                    ll_aob_personal_maiden_name.setVisibility(View.GONE);
                } else {

                    //maiden name visible
                    ll_aob_personal_maiden_name.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.spnr_posp_ra_nationality:
                if (position == 0) {
                    edt_posp_personal_nationality.setVisibility(View.GONE);
                } else {
                    edt_posp_personal_nationality.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.spnr_aob_personal_pro_qualification:

                if (position == 8) {
                    edt_aob_personal_pro_qualification_cmnt.setVisibility(View.VISIBLE);
                } else {
                    edt_aob_personal_pro_qualification_cmnt.setText("");
                    edt_aob_personal_pro_qualification_cmnt.setVisibility(View.GONE);
                }

                break;

            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {

        if (is_dashboard) {
            startActivity(new Intent(Activity_POSP_RA_PersonalInfo.this, Activity_POSP_RA_Dashboard.class));
        } else if (is_bsm_questions) {
            Intent mIntent = new Intent(Activity_POSP_RA_PersonalInfo.this, Activity_POSP_RA_PANPendingAgentList.class);
            mIntent.putExtra("UMCode", strUM);
            startActivity(mIntent);
        } else if (is_rejection) {
            //redirect to dashboard page
            Intent i = new Intent(Activity_POSP_RA_PersonalInfo.this, Activity_POSP_RA_Rejection_Remarks.class);
            i.putExtra("enrollment_type", mCommonMethods.str_posp_ra_customer_type);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        } else {
            if (is_back_pressed) {
                startActivity(new Intent(Activity_POSP_RA_PersonalInfo.this, Activity_POSP_RA_Authentication.class));
            }
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_aob_personal_info_back:
                is_back_pressed = true;
                onBackPressed();
                break;

            case R.id.txt_aob_personal_dob:

                datePickerDialog = DatePickerDialog.newInstance(Activity_POSP_RA_PersonalInfo.this, mDOBYear, mDOBMonth, mDOBDay);
                datePickerDialog.setThemeDark(false);
                datePickerDialog.showYearPickerFirst(false);
                /*//future date not allowed
                datePickerDialog.setMinDate(mCalender);*/

                //Applicant should be above 18 years.
                Calendar max_date_c = Calendar.getInstance();
                max_date_c.set(Calendar.YEAR, mDOBYear - 18);
                datePickerDialog.setMaxDate(max_date_c);

                datePickerDialog.dismissOnPause(true); //dismiss dialog when onPause() called?
                datePickerDialog.setAccentColor(getResources().getColor(R.color.Common_blue));
                datePickerDialog.setTitle("Applicant DOB");

                datePickerDialog.show(getFragmentManager(), DATE_APPLICANT_DOB);
                break;

            case R.id.txt_aob_personal_pass_year_month:
                datePickerDialog = DatePickerDialog.newInstance(Activity_POSP_RA_PersonalInfo.this, mDOBYear, mDOBMonth, mDOBDay);
                datePickerDialog.setThemeDark(false);
                datePickerDialog.showYearPickerFirst(false);
                //future date not allowed
                datePickerDialog.setMaxDate(mCalender);

                datePickerDialog.dismissOnPause(true); //dismiss dialog when onPause() called?
                datePickerDialog.setAccentColor(getResources().getColor(R.color.Common_blue));
                datePickerDialog.setTitle("Applicant Passing Year");

                datePickerDialog.show(getFragmentManager(), DATE_APPLICANT_PASS_YEAR_MONTH);
                break;

            case R.id.btn_aob_personal_permanent_pin:

                is_per_pin_valid = false;
                str_pincode = edt_aob_personal_communication_pin.getText().toString();

                if (!str_pincode.equals("")) {

                    str_pin_flag = "permanent";

                    createSoapRequestToUploadDoc(METHOD_NAME_VERIFY_PINCODE);

                } else {
                    mCommonMethods.showMessageDialog(mContext, "Please enter Pincode");
                }

                break;

            case R.id.btn_aob_personal_comm_pin:

                is_comm_pin_valid = false;
                str_pincode = edt_aob_personal_permnt_pin.getText().toString();

                if (!str_pincode.equals("")) {

                    str_pin_flag = "communication";

                    createSoapRequestToUploadDoc(METHOD_NAME_VERIFY_PINCODE);

                } else {
                    mCommonMethods.showMessageDialog(mContext, "Please enter Pincode");
                }
                break;

            case R.id.btn_aob_personal_info_next:

                if (is_dashboard) {
                    Intent mIntent = new Intent(Activity_POSP_RA_PersonalInfo.this, Activity_POSP_RA_Occupation.class);
                    mIntent.putExtra("is_dashboard", is_dashboard);
                    startActivity(mIntent);
                } else if (is_bsm_questions) {
                    Intent mIntent = new Intent(Activity_POSP_RA_PersonalInfo.this, Activity_POSP_RA_Occupation.class);
                    mIntent.putExtra("is_bsm_questions", is_bsm_questions);
                    startActivity(mIntent);
                } else if (!is_dashboard && !is_bsm_questions) {

                    String str_error = validate_all_details();
                    if (str_error.equals("")) {

                        if (edt_aob_personal_mobile.getText().toString().equals(str_applicant_stored_mobile_no)
                                && edt_aob_personal_email_id.getText().toString().equals(str_applicant_stored_email_id)) {
                            //save data and move to occupation details
                            savePOSP_RA_PersonalInfo();
                        } else {
                            //check mobile and email
                            new AsyncCheckMobileEmail_Other().execute(
                                    edt_aob_personal_mobile.getText().toString(),
                                    edt_aob_personal_email_id.getText().toString());
                        }

                    } else {
                        mCommonMethods.showMessageDialog(mContext, str_error);
                    }
                }

                break;

            case R.id.btn_posp_ra_verify_otp:

                if (is_otp_generated) {
                    String strOTP = edt_posp_ra_otp.getText().toString();
                    strOTP = strOTP == null ? "" : strOTP;
                    if (strOTP.equals("")) {
                        mCommonMethods.showToast(mContext, "Please enter OTP");
                    } else {
                        createSoapRequestToUploadDoc(METHOD_NAME_VALIDATE_OTP);
                    }
                } else {
                    mCommonMethods.showMessageDialog(mContext, "Please generate otp first");
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        switch (view.getTag()) {
            case DATE_APPLICANT_DOB:
                String strSelectedDate = (dayOfMonth < 10 ? "0" : "") + dayOfMonth + "-"
                        + ((monthOfYear + 1) < 10 ? "0" : "") + (monthOfYear + 1) + "-"
                        + year;
                txt_aob_personal_dob.setText(strSelectedDate);
                break;

            case DATE_APPLICANT_PASS_YEAR_MONTH:
                String strSelectedPassDate = ((monthOfYear + 1) < 10 ? "0" : "") + (monthOfYear + 1) + "-" + year;
                txt_aob_personal_pass_year_month.setText(strSelectedPassDate);
                break;
        }
    }

    //2. create xml string for data saving
    private void get_personal_info_xml() {

        str_pan_no = edt_aob_personal_pan_no.getText().toString();

        String str_applicant_title = spnr_aob_personal_title.getSelectedItem().toString();
        String str_applicant_full_name = mCommonMethods.removeExtraWhiteSpaces(edt_aob_personal_full_name);

        String str_applicant_dob = txt_aob_personal_dob.getText().toString();
        String str_applicant_gender = spnr_aob_personal_gender.getSelectedItem().toString();

        String str_applicant_permanent_add1 = mCommonMethods.removeExtraWhiteSpaces(edt_aob_personal_communication_address1);
        String str_applicant_permanent_add2 = mCommonMethods.removeExtraWhiteSpaces(edt_aob_personal_communication_address2);
        String str_applicant_permanent_add3 = mCommonMethods.removeExtraWhiteSpaces(edt_aob_personal_communication_address3);
        String str_applicant_permanent_add_pin = edt_aob_personal_communication_pin.getText().toString();

        String str_applicant_communication_add1 = mCommonMethods.removeExtraWhiteSpaces(edt_aob_personal_permnt_address1);
        String str_applicant_communication_add2 = mCommonMethods.removeExtraWhiteSpaces(edt_aob_personal_permnt_address2);
        String str_applicant_communication_add3 = mCommonMethods.removeExtraWhiteSpaces(edt_aob_personal_permnt_address3);
        String str_applicant_communication_add_pin = edt_aob_personal_permnt_pin.getText().toString();

        String str_applicant_father_husband_name = mCommonMethods.removeExtraWhiteSpaces(edt_aob_personal_father_husbund_name);
        String str_applicant_relation = spnr_aob_personal_relation_applicant.getSelectedItem().toString();
        String str_applicant_maiden_name = mCommonMethods.removeExtraWhiteSpaces(edt_aob_personal_maiden_name);
        String str_applicant_marital_status = spnr_aob_personal_marital_status.getSelectedItem().toString();
        String str_applicant_caste_category = spnr_aob_personal_caste_category.getSelectedItem().toString();
        String str_applicant_mobile_no = edt_aob_personal_mobile.getText().toString();
        String str_applicant_residence_no = edt_aob_personal_residence_no.getText().toString();
        String str_applicant_email_id = edt_aob_personal_email_id.getText().toString();
        String str_applicant_basic_qualification = spnr_aob_personal_bas_qualification.getSelectedItem().toString();
        String str_applicant_passing_roll_no = edt_aob_personal_pass_roll_no.getText().toString();
        String str_applicant_passing_university = mCommonMethods.removeExtraWhiteSpaces(edt_aob_personal_pass_university);
        String str_applicant_passing_year_month = txt_aob_personal_pass_year_month.getText().toString();
        String str_applicant_pro_qualification = spnr_aob_personal_pro_qualification.getSelectedItem().toString();
        String str_applicant_pro_qualification_others = mCommonMethods.removeExtraWhiteSpaces(edt_aob_personal_pro_qualification_cmnt);

        str_personal_info = new StringBuilder();

        /*str_personal_info.append("<personal_info>");*/
        str_personal_info.append("<personal_info_title>").append(str_applicant_title).append("</personal_info_title>");
        str_personal_info.append("<personal_info_full_name>").append(str_applicant_full_name.toUpperCase().trim()).append("</personal_info_full_name>");

        if (!str_applicant_dob.equals("")) {
            String[] arrDate = str_applicant_dob.split("-");
            str_personal_info.append("<personal_info_dob>").append(arrDate[1] + "-" + arrDate[0] + "-" + arrDate[2]).append("</personal_info_dob>");
        } else {
            str_personal_info.append("<personal_info_dob></personal_info_dob>");
        }

        str_personal_info.append("<personal_info_gender>").append(str_applicant_gender).append("</personal_info_gender>");

        String strNationality = "";
        if (spnr_posp_ra_nationality.getSelectedItemPosition() == 0)
            strNationality = "Indian";
        else {
            strNationality = edt_posp_personal_nationality.getText().toString();
        }

        str_personal_info.append("<personal_info_nationality>" + strNationality + "</personal_info_nationality>");

        str_personal_info.append("<personal_info_aadhaar_no>").append(str_aadhaar_no).append("</personal_info_aadhaar_no>");
        str_personal_info.append("<personal_info_pan_no>").append(str_pan_no).append("</personal_info_pan_no>");

        /*str_personal_info.append("<personal_info_permanant_address1>").append(str_applicant_permanent_add1).append("</personal_info_permanant_address1>");
         *//*str_personal_info.append("<personal_info_permanant_address2>").append(str_applicant_permanent_add2).append("</personal_info_permanant_address2>");
        str_personal_info.append("<personal_info_permanant_address3>").append(str_applicant_permanent_add3).append("</personal_info_permanant_address3>");*//*
        str_personal_info.append("<personal_info_permanant_address2>").append("").append("</personal_info_permanant_address2>");
        str_personal_info.append("<personal_info_permanant_address3>").append("").append("</personal_info_permanant_address3>");
        str_personal_info.append("<personal_info_permanant_address_pin>").append(str_applicant_permanent_add_pin).append("</personal_info_permanant_address_pin>");

        str_personal_info.append("<personal_info_communication_add_same>").append(rb_aob_personal_info_same_add_as_yes.isChecked()).append("</personal_info_communication_add_same>");
        if (rb_aob_personal_info_same_add_as_yes.isChecked()){
            str_personal_info.append("<personal_info_communication_address1>").append(str_applicant_permanent_add1).append("</personal_info_communication_address1>");
            *//*str_personal_info.append("<personal_info_communication_address2>").append(str_applicant_permanent_add2).append("</personal_info_communication_address2>");
            str_personal_info.append("<personal_info_communication_address3>").append(str_applicant_permanent_add3).append("</personal_info_communication_address3>");*//*
            str_personal_info.append("<personal_info_communication_address2>").append("").append("</personal_info_communication_address2>");
            str_personal_info.append("<personal_info_communication_address3>").append("").append("</personal_info_communication_address3>");
            str_personal_info.append("<personal_info_communication_address_pin>").append(str_applicant_permanent_add_pin).append("</personal_info_communication_address_pin>");
        }else {
            str_personal_info.append("<personal_info_communication_address1>").append(str_applicant_communication_add1).append("</personal_info_communication_address1>");
            *//*str_personal_info.append("<personal_info_communication_address2>").append(str_applicant_communication_add2).append("</personal_info_communication_address2>");
            str_personal_info.append("<personal_info_communication_address3>").append(str_applicant_communication_add3).append("</personal_info_communication_address3>");*//*
            str_personal_info.append("<personal_info_communication_address2>").append("").append("</personal_info_communication_address2>");
            str_personal_info.append("<personal_info_communication_address3>").append("").append("</personal_info_communication_address3>");
            str_personal_info.append("<personal_info_communication_address_pin>").append(str_applicant_communication_add_pin).append("</personal_info_communication_address_pin>");
        }*/

        //as per new offline ekyc requirement Permanent Address field in the personal tab needs to be renamed as communication address
        if (str_applicant_permanent_add1.length() > 100)
            str_applicant_permanent_add1 = str_applicant_permanent_add1.substring(0, 99);

        if (str_applicant_permanent_add2.length() > 100)
            str_applicant_permanent_add2 = str_applicant_permanent_add2.substring(0, 99);

        if (str_applicant_permanent_add3.length() > 100)
            str_applicant_permanent_add3 = str_applicant_permanent_add3.substring(0, 99);


        str_personal_info.append("<personal_info_communication_address1>").append(str_applicant_permanent_add1.toUpperCase().trim()).append("</personal_info_communication_address1>");
        str_personal_info.append("<personal_info_communication_address2>").append(str_applicant_permanent_add2.toUpperCase().trim()).append("</personal_info_communication_address2>");
        str_personal_info.append("<personal_info_communication_address3>").append(str_applicant_permanent_add3.toUpperCase().trim()).append("</personal_info_communication_address3>");
        str_personal_info.append("<personal_info_communication_address_pin>").append(str_applicant_permanent_add_pin).append("</personal_info_communication_address_pin>");

        str_personal_info.append("<personal_info_communication_add_same>").append(rb_aob_personal_info_same_add_as_yes.isChecked()).append("</personal_info_communication_add_same>");
        if (rb_aob_personal_info_same_add_as_yes.isChecked()) {
            str_personal_info.append("<personal_info_permanant_address1>").append(str_applicant_permanent_add1.toUpperCase().trim()).append("</personal_info_permanant_address1>");
            str_personal_info.append("<personal_info_permanant_address2>").append(str_applicant_permanent_add2.toUpperCase().trim()).append("</personal_info_permanant_address2>");
            str_personal_info.append("<personal_info_permanant_address3>").append(str_applicant_permanent_add3.toUpperCase().trim()).append("</personal_info_permanant_address3>");
            str_personal_info.append("<personal_info_permanant_address_pin>").append(str_applicant_permanent_add_pin).append("</personal_info_permanant_address_pin>");
        } else {

            if (str_applicant_communication_add1.length() > 100)
                str_applicant_communication_add1 = str_applicant_communication_add1.substring(0, 99);

            if (str_applicant_communication_add2.length() > 100)
                str_applicant_communication_add2 = str_applicant_communication_add2.substring(0, 99);

            if (str_applicant_communication_add3.length() > 100)
                str_applicant_communication_add3 = str_applicant_communication_add3.substring(0, 99);

            str_personal_info.append("<personal_info_permanant_address1>").append(str_applicant_communication_add1.toUpperCase().trim()).append("</personal_info_permanant_address1>");
            str_personal_info.append("<personal_info_permanant_address2>").append(str_applicant_communication_add2.toUpperCase().trim()).append("</personal_info_permanant_address2>");
            str_personal_info.append("<personal_info_permanant_address3>").append(str_applicant_communication_add3.toUpperCase().trim()).append("</personal_info_permanant_address3>");
            str_personal_info.append("<personal_info_permanant_address_pin>").append(str_applicant_communication_add_pin).append("</personal_info_permanant_address_pin>");
        }

        str_personal_info.append("<personal_info_father_husband_name>").append(str_applicant_father_husband_name.toUpperCase().trim()).append("</personal_info_father_husband_name>");
        str_personal_info.append("<personal_info_relation_with_applicant>").append(str_applicant_relation).append("</personal_info_relation_with_applicant>");
        str_personal_info.append("<personal_info_maiden_name>").append(str_applicant_maiden_name).append("</personal_info_maiden_name>");
        str_personal_info.append("<personal_info_marital_status>").append(str_applicant_marital_status).append("</personal_info_marital_status>");
        str_personal_info.append("<personal_info_caste_category>").append(str_applicant_caste_category).append("</personal_info_caste_category>");
        str_personal_info.append("<personal_info_mobile_no>").append(str_applicant_mobile_no).append("</personal_info_mobile_no>");
        str_personal_info.append("<personal_info_residence_no>").append(str_applicant_residence_no).append("</personal_info_residence_no>");
        str_personal_info.append("<personal_info_email_id>").append(str_applicant_email_id).append("</personal_info_email_id>");
        str_personal_info.append("<personal_info_educational_details_basic_qualification>").append(str_applicant_basic_qualification).append("</personal_info_educational_details_basic_qualification>");
        str_personal_info.append("<personal_info_educational_details_passing_roll_no>").append(str_applicant_passing_roll_no).append("</personal_info_educational_details_passing_roll_no>");
        str_personal_info.append("<personal_info_educational_details_passing_university>").append(str_applicant_passing_university.toUpperCase().trim()).append("</personal_info_educational_details_passing_university>");
        str_personal_info.append("<personal_info_educational_details_passing_month_year>").append(str_applicant_passing_year_month).append("</personal_info_educational_details_passing_month_year>");
        str_personal_info.append("<personal_info_educational_details_professional_qualification>").append(str_applicant_pro_qualification).append("</personal_info_educational_details_professional_qualification>");
        str_personal_info.append("<personal_info_educational_details_professional_qualification_others>").append(str_applicant_pro_qualification_others).append("</personal_info_educational_details_professional_qualification_others>");
        /*str_personal_info.append("</personal_info>");*/
    }

    public String validate_all_details() {

        if (spnr_aob_personal_title.getSelectedItem().toString().equals("Select Title")) {
            spnr_aob_personal_title.requestFocus();
            return "Please Select Title";
        } else if (edt_aob_personal_full_name.getText().toString().equals("")) {
            edt_aob_personal_full_name.requestFocus();
            return "Please Enter Full Name";
        } else if (txt_aob_personal_dob.getText().toString().equals("")) {
            txt_aob_personal_dob.requestFocus();
            return "Please Select Date of Birth";
        } else if (spnr_posp_ra_nationality.getSelectedItemPosition() == 1
                && edt_posp_personal_nationality.getText().toString().equals("")) {
            edt_posp_personal_nationality.requestFocus();
            return "Please Enter Nationality";
        }
        /*else if (!validate_aadhar){
            edt_aob_personal_aadhaar_no.requestFocus();
            return "Please enter aadhaar details";
        }*/ /*else if (!validate_pan_card) {
            edt_aob_personal_pan_no.requestFocus();
            return "Please enter PAN details";
        }*/
        else if (edt_aob_personal_communication_address1.getText().toString().equals("")) {
            edt_aob_personal_communication_address1.requestFocus();
            return "Please enter Communication Address-1";
        } else if (edt_aob_personal_communication_address2.getText().toString().equals("")) {
            edt_aob_personal_communication_address2.requestFocus();
            return "Please enter Communication Address-2";
        } else if (edt_aob_personal_communication_address3.getText().toString().equals("")) {
            edt_aob_personal_communication_address3.requestFocus();
            return "Please enter Communication Address-3";
        } else if (edt_aob_personal_communication_pin.getText().toString().equals("")) {
            edt_aob_personal_communication_pin.requestFocus();
            return "Please enter Communication Pincode";
        } else if (!is_per_pin_valid) {
            edt_aob_personal_communication_pin.requestFocus();
            return "Please verify Communication Address Pincode";
        } /*else if (rb_aob_personal_info_same_add_as_no.isChecked() &
                edt_aob_personal_comm_address1.getText().toString().equals("")) {
            edt_aob_personal_comm_address1.requestFocus();
            return "Please enter Permanent Address";
        } else if (rb_aob_personal_info_same_add_as_no.isChecked() & !is_comm_pin_valid) {
            edt_aob_personal_comm_pin.requestFocus();
            return "Please verify Permanent Pincode";
        } */ else if (edt_aob_personal_father_husbund_name.getText().toString().equals("")) {
            edt_aob_personal_father_husbund_name.requestFocus();
            return "Please enter Father's / Husband's Name";
        } else if (spnr_aob_personal_relation_applicant.getSelectedItem().equals("Select Relation")) {
            spnr_aob_personal_relation_applicant.requestFocus();
            return "Please Select Relation with Applicant";
        } else if (spnr_aob_personal_title.getSelectedItem().toString().equals("Mrs") &
                edt_aob_personal_maiden_name.getText().toString().equals("")) {
            edt_aob_personal_maiden_name.requestFocus();
            return "Please enter Maiden Name";
        } else if (spnr_aob_personal_marital_status.getSelectedItem().toString().equals("Select Marital Status")) {
            spnr_aob_personal_marital_status.requestFocus();
            return "Please Select Marital Status";
        } else if (spnr_aob_personal_caste_category.getSelectedItem().equals("Select Caste Category")) {
            spnr_aob_personal_caste_category.requestFocus();
            return "Please Select Caste Category";
        } else if (edt_aob_personal_mobile.getText().toString().length() != 10) {
            edt_aob_personal_mobile.requestFocus();
            return "Please Enter 10 digit Mobile Number";
        } else if (edt_aob_personal_residence_no.getText().toString().length() != 10) {
            edt_aob_personal_residence_no.requestFocus();
            return "Please Enter 10 digit Residence Number";
        } else if (!validate_email) {
            edt_aob_personal_email_id.requestFocus();
            return "Please Enter email ID";
        } else if (spnr_aob_personal_bas_qualification.getSelectedItem().toString().equals("Select Basic Qualification")) {
            spnr_aob_personal_bas_qualification.requestFocus();
            return "Please Select Basic Qualification";
        } else if (edt_aob_personal_pass_roll_no.getText().toString().equals("")) {
            edt_aob_personal_pass_roll_no.requestFocus();
            return "Please Enter Roll Number";
        } else if (edt_aob_personal_pass_university.getText().toString().equals("")) {
            edt_aob_personal_pass_university.requestFocus();
            return "Please Enter Passing Board/University";
        } else if (txt_aob_personal_pass_year_month.getText().toString().equals("")) {
            txt_aob_personal_pass_year_month.requestFocus();
            return "Please Select Passing Year";
        } else if (spnr_aob_personal_pro_qualification.getSelectedItem().toString().equals("Select Professional Qualification")) {
            spnr_aob_personal_pro_qualification.requestFocus();
            return "Please Select Professional Qualification";
        } else if (spnr_aob_personal_pro_qualification.getSelectedItem().toString().equals("Others") &
                edt_aob_personal_pro_qualification_cmnt.getText().toString().equals("")) {
            edt_aob_personal_pro_qualification_cmnt.requestFocus();
            return "Please Enter Other Professional Qualification";
        } else
            return "";
    }

    public void enableDisableAllFields(boolean is_enable) {
        spnr_aob_personal_title.setEnabled(is_enable);

        spnr_aob_personal_gender.setEnabled(is_enable);

        spnr_aob_personal_relation_applicant.setEnabled(is_enable);

        spnr_aob_personal_marital_status.setEnabled(is_enable);

        spnr_aob_personal_caste_category.setEnabled(is_enable);

        spnr_aob_personal_bas_qualification.setEnabled(is_enable);

        spnr_aob_personal_pro_qualification.setEnabled(is_enable);

        txt_aob_personal_dob.setEnabled(is_enable);
        txt_aob_personal_pass_year_month.setEnabled(is_enable);

        btn_aob_personal_permanent_pin.setEnabled(is_enable);

        btn_aob_personal_comm_pin.setEnabled(is_enable);

        edt_aob_personal_full_name.setEnabled(is_enable);
        edt_aob_personal_aadhaar_no.setEnabled(is_enable);
        //edt_aob_personal_pan_no.setEnabled(is_enable);
        edt_aob_personal_communication_address1.setEnabled(is_enable);
        edt_aob_personal_communication_address2.setEnabled(is_enable);
        edt_aob_personal_communication_address3.setEnabled(is_enable);
        edt_aob_personal_communication_pin.setEnabled(is_enable);
        edt_aob_personal_permnt_address1.setEnabled(is_enable);
        edt_aob_personal_permnt_address2.setEnabled(is_enable);
        edt_aob_personal_permnt_address3.setEnabled(is_enable);
        edt_aob_personal_permnt_pin.setEnabled(is_enable);
        edt_aob_personal_father_husbund_name.setEnabled(is_enable);
        edt_aob_personal_maiden_name.setEnabled(is_enable);
        edt_aob_personal_mobile.setEnabled(is_enable);
        edt_aob_personal_residence_no.setEnabled(is_enable);
        edt_aob_personal_email_id.setEnabled(is_enable);
        edt_aob_personal_pass_roll_no.setEnabled(is_enable);
        edt_aob_personal_pass_university.setEnabled(is_enable);
        edt_aob_personal_pro_qualification_cmnt.setEnabled(is_enable);

        rg_aob_personal_info_same_permanent.setEnabled(is_enable);
        rb_aob_personal_info_same_add_as_yes.setEnabled(is_enable);
        rb_aob_personal_info_same_add_as_no.setEnabled(is_enable);
    }

    private void createSoapRequestToUploadDoc(String strMethod) {

        SoapObject request = new SoapObject(NAMESPACE, strMethod);
        if (strMethod.equals(METHOD_NAME_VERIFY_PINCODE)) {
            request.addProperty("strPincode", str_pincode);
            new AsyncUploadFile_Common(mContext, this, request, strMethod).execute();
        } else if (strMethod.equals(METHOD_NAME_GENERATE_OTP)) {


            is_otp_generated = false;
            str_pin_flag = METHOD_NAME_GENERATE_OTP;
            request.addProperty("MOBILE_NO", edt_aob_personal_mobile.getText().toString()
                    + "|" + edt_aob_personal_email_id.getText().toString());
            new AsyncUploadFile_Common(mContext, this, request, strMethod).execute();
        } else if (strMethod.equals(METHOD_NAME_VALIDATE_OTP)) {
            str_pin_flag = METHOD_NAME_VALIDATE_OTP;
            request.addProperty("strOTP", edt_posp_ra_otp.getText().toString());
            request.addProperty("MOBILE_NO", edt_aob_personal_mobile.getText().toString());
            new AsyncUploadFile_Common(mContext, this, request, strMethod).execute();
        }
    }

    @Override
    public void onUploadComplete(Boolean result) {

        if (result) {

            if (str_pin_flag.equals(METHOD_NAME_GENERATE_OTP)) {
                is_otp_generated = true;
                ll_posp_ra_verify_otp.setVisibility(View.VISIBLE);
            } else if (str_pin_flag.equals(METHOD_NAME_VALIDATE_OTP)) {
                savePOSP_RA_PersonalInfo();
            } else {
                if (str_pin_flag.equals("permanent")) {
                    is_per_pin_valid = true;
                } else if (str_pin_flag.equals("communication")) {
                    is_comm_pin_valid = true;
                }
                mCommonMethods.showMessageDialog(mContext, "Pincode is verified");
            }
        } else {
            if (str_pin_flag.equals(METHOD_NAME_GENERATE_OTP)) {
                ll_posp_ra_verify_otp.setVisibility(View.GONE);
            } else if (str_pin_flag.equals(METHOD_NAME_VALIDATE_OTP)) {
                is_otp_generated = false;
            } else {
                mCommonMethods.showMessageDialog(mContext, "Pincode is invalid");
            }
        }

    }

    private void savePOSP_RA_PersonalInfo() {
        //1. validate all details
        String str_error = validate_all_details();
        if (str_error.equals("")) {

            //2. create xml string for data saving
            get_personal_info_xml();

            //3. update data against global row id
            ContentValues cv = new ContentValues();
            cv.clear();
            cv.put(db.POSP_RA_PERSONAL_INFO, str_personal_info.toString());
            cv.put(db.POSP_RA_UPDATED_BY, mCommonMethods.GetUserCode(mContext));

            //save date in long
            cv.put(db.POSP_RA_UPDATED_DATE, new Date(mCalender.getTimeInMillis()).getTime() + "");
            cv.put(db.POSP_RA_IN_APP_STATUS, "2");
            cv.put(db.POSP_RA_IN_APP_STATUS_REMARK, "Occupational Info Pending");

            int i = db.update_POSP_RA_details(cv, db.POSP_RA_ID + " =? ",
                    new String[]{Activity_POSP_RA_Authentication.row_details + ""});

            Intent mIntent = new Intent(Activity_POSP_RA_PersonalInfo.this, Activity_POSP_RA_Occupation.class);
            if (is_rejection)
                mIntent.putExtra("is_rejection", true);

            startActivity(mIntent);

            mCommonMethods.showToast(mContext, "Details saved Successfully : " + i);

        } else {
            mCommonMethods.showMessageDialog(mContext, str_error);
        }
    }

    class AsyncCheckMobileEmail_Other extends AsyncTask<String, String, String> {

        private volatile boolean running = false;

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(Activity_POSP_RA_PersonalInfo.this, ProgressDialog.THEME_HOLO_LIGHT);
            mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>Loading Please wait...<b></font>"));
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            if (mCommonMethods.isNetworkConnected(mContext)) {
                try {
                    running = true;

                    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_CHECK_EMAIL_MOBILE);

                    request.addProperty("strMobile", strings[0]);
                    request.addProperty("strEmail", strings[1]);

                    mCommonMethods.TLSv12Enable();

                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                            SoapEnvelope.VER11);
                    envelope.dotNet = true;

                    new MarshalBase64().register(envelope); // serialization

                    envelope.setOutputSoapObject(request);

                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL, 50000);

                    androidHttpTranport.call(NAMESPACE + METHOD_NAME_CHECK_EMAIL_MOBILE, envelope);
                    Object response = envelope.getResponse();
                    return response.toString();

                } catch (Exception e) {
                    e.printStackTrace();
                    running = false;
                    return mCommonMethods.WEEK_INTERNET_MESSAGE;
                }
            } else {
                return mCommonMethods.NO_INTERNET_MESSAGE;
            }
        }

        @Override
        protected void onPostExecute(String mResult) {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();

            if (running) {

                if (mResult.equals("1")) {
                    //generate otp
                    createSoapRequestToUploadDoc(METHOD_NAME_GENERATE_OTP);
                } else {
                    mCommonMethods.showMessageDialog(mContext, mResult);
                }
            } else {
                mCommonMethods.showMessageDialog(mContext, mResult);
            }
        }
    }

}
