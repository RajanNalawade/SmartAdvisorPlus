package sbilife.com.pointofsale_bancaagency.agent_on_boarding;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.common.AsyncUploadFile_Common;
import sbilife.com.pointofsale_bancaagency.common.CommonForAllProd;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

public class ActivityAOBNomination extends AppCompatActivity implements View.OnClickListener,
        AsyncUploadFile_Common.Interface_Upload_File_Common, DatePickerDialog.OnDateSetListener {

    private final String NAMESPACE = "http://tempuri.org/";
    private final String METHOD_NAME_VERIFY_PINCODE = "validatePincode";
    private final String DATE_APPOINTEE_DOB = "AppointeeDOB";
    private final String DATE_NOMINEE_DOB = "NomineeDOB";
    private CommonMethods mCommonMethods;
    private Context mContext;
    private DatabaseHelper db;
    private Button btn_aob_nomination_next, btn_aob_nominee_address_pincode, btn_aob_appointee_address_pincode,
            btn_aob_nomination_back;
    private TextView txt_aob_nominee_dob, txt_aob_appointee_dob;
    private Spinner spnr_aob_nominee_title, spnr_aob_nominee_relation, spnr_aob_nominee_gender, spnr_aob_nom_appointee_title,
            spnr_aob_nom_appointee_relation;
    private EditText edt_aob_nominee_full_name, edt_aob_nominee_percentage, edt_aob_nominee_address, edt_aob_nominee_address2, edt_aob_nominee_address3,
            edt_aob_nominee_address_city, edt_aob_nominee_address_state, edt_aob_nominee_address_pincode, edt_aob_nom_appointee_full_name,
            edt_aob_nom_witness_occupation, edt_aob_nom_witness_full_add, edt_aob_nom_witness_name, edt_aob_appointee_address,
            edt_aob_appointee_address2, edt_aob_appointee_address3,
            edt_aob_appointee_address_city, edt_aob_appointee_address_state, edt_aob_appointee_address_pincode;
    private LinearLayout ll_aob_appointee_details, ll_aob_nominee_address_not_same, ll_aob_appointee_address_not_same, ll_aob_nominee_witness;
    private RadioGroup rg_aob_nominee_same_add_as_applicant, rg_aob_appointee_same_add_as_nominee;
    private RadioButton rb_aob_nominee_same_add_as_yes, rb_aob_nominee_same_add_as_no, rb_aob_appointee_same_add_as_yes,
            rb_aob_appointee_same_add_as_no;
    private CheckBox chkbox_aob_witness_language;
    private DatePickerDialog datePickerDialog;
    private Calendar mCalender;
    private int mDOBDay = 0, mDOBYear = 0, mDOBMonth = 0;
    private boolean is_appointee = false, is_nominee_pin_verify = false, is_appointee_pin_verify = false,
            is_appointee_required = false, is_dashboard = false, is_back_pressed = false, is_ia_upgrade = false;
    private Date currentDate;
    private String str_applicant_address = "", str_applicant_address2 = "", str_applicant_address3 = "",
            str_pincode = "", str_pin_flag = "";

    private StringBuilder str_nominational_info;
    private ParseXML mParseXML;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_aob_nomination);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.cifenrollment_pf_activity_title);

        if (getIntent().hasExtra("is_dashboard"))
            is_dashboard = getIntent().getBooleanExtra("is_dashboard", false);

        if (getIntent().hasExtra("is_ia_upgrade"))
            is_ia_upgrade = getIntent().getBooleanExtra("is_ia_upgrade", false);

        initialisation();

        if (is_dashboard || is_ia_upgrade) {
            //non editable with no saving
            enableDisableAllFields(false);
        } else {
            //editable
            enableDisableAllFields(true);
        }
    }

    public void initialisation() {

        mContext = this;
        mCommonMethods = new CommonMethods();

        mCommonMethods.setApplicationToolbarMenu1(this, "Agent on Boarding");

        db = new DatabaseHelper(mContext);

        mParseXML = new ParseXML();

        View view_aob_nominee_formIA = findViewById(R.id.view_aob_nominee_formIA);
        TextView txt_aob_nominee_formIA = findViewById(R.id.txt_aob_nominee_formIA);
        if (is_ia_upgrade) {
            view_aob_nominee_formIA.setVisibility(View.GONE);
            txt_aob_nominee_formIA.setVisibility(View.GONE);
        } else {
            view_aob_nominee_formIA.setVisibility(View.VISIBLE);
            txt_aob_nominee_formIA.setVisibility(View.VISIBLE);
        }

        spnr_aob_nominee_title = (Spinner) findViewById(R.id.spnr_aob_nominee_title);
        ArrayAdapter<String> nominee_adapter = new ArrayAdapter<String>(
                mContext, R.layout.spinner_aob, getResources().getStringArray(R.array.arr_aob_title));
        spnr_aob_nominee_title.setAdapter(nominee_adapter);
        nominee_adapter.notifyDataSetChanged();

        spnr_aob_nominee_title.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 2 || position == 3) {
                    spnr_aob_nominee_gender.setSelection(1);
                } else
                    spnr_aob_nominee_gender.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        rg_aob_nominee_same_add_as_applicant = (RadioGroup) findViewById(R.id.rg_aob_nominee_same_add_as_applicant);
        rb_aob_nominee_same_add_as_yes = (RadioButton) findViewById(R.id.rb_aob_nominee_same_add_as_yes);
        rb_aob_nominee_same_add_as_no = (RadioButton) findViewById(R.id.rb_aob_nominee_same_add_as_no);

        rg_aob_nominee_same_add_as_applicant.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_aob_nominee_same_add_as_yes:
                        //copy applicant address and save as nominee address
                        ll_aob_nominee_address_not_same.setVisibility(View.GONE);
                        break;

                    case R.id.rb_aob_nominee_same_add_as_no:
                        ll_aob_nominee_address_not_same.setVisibility(View.VISIBLE);
                        break;

                    default:
                        break;
                }
            }
        });

        rg_aob_appointee_same_add_as_nominee = (RadioGroup) findViewById(R.id.rg_aob_appointee_same_add_as_nominee);
        rb_aob_appointee_same_add_as_yes = (RadioButton) findViewById(R.id.rb_aob_appointee_same_add_as_yes);
        rb_aob_appointee_same_add_as_no = (RadioButton) findViewById(R.id.rb_aob_appointee_same_add_as_no);

        rg_aob_appointee_same_add_as_nominee.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_aob_appointee_same_add_as_yes:
                        //copy applicant address and save as nominee address
                        ll_aob_appointee_address_not_same.setVisibility(View.GONE);
                        break;

                    case R.id.rb_aob_appointee_same_add_as_no:
                        ll_aob_appointee_address_not_same.setVisibility(View.VISIBLE);
                        break;

                    default:
                        break;
                }
            }
        });

        spnr_aob_nominee_relation = (Spinner) findViewById(R.id.spnr_aob_nominee_relation);
        ArrayAdapter<String> relation_adapter = new ArrayAdapter<String>(
                mContext, R.layout.spinner_aob, getResources().getStringArray(R.array.arr_aob_nominee_relation));
        spnr_aob_nominee_relation.setAdapter(relation_adapter);
        relation_adapter.notifyDataSetChanged();

        spnr_aob_nominee_gender = (Spinner) findViewById(R.id.spnr_aob_nominee_gender);
        ArrayAdapter<String> nominee_gender_adapter = new ArrayAdapter<String>(
                mContext, R.layout.spinner_aob, getResources().getStringArray(R.array.gender_arrays));
        spnr_aob_nominee_gender.setAdapter(nominee_gender_adapter);
        nominee_gender_adapter.notifyDataSetChanged();

        spnr_aob_nom_appointee_title = (Spinner) findViewById(R.id.spnr_aob_nom_appointee_title);
        ArrayAdapter<String> appointee_title_adapter = new ArrayAdapter<String>(
                mContext, R.layout.spinner_aob, getResources().getStringArray(R.array.arr_aob_title));
        spnr_aob_nom_appointee_title.setAdapter(appointee_title_adapter);
        appointee_title_adapter.notifyDataSetChanged();

        spnr_aob_nom_appointee_relation = (Spinner) findViewById(R.id.spnr_aob_nom_appointee_relation);
        ArrayAdapter<String> appointee_relation_adapter = new ArrayAdapter<String>(
                mContext, R.layout.spinner_aob, getResources().getStringArray(R.array.arr_aob_nominee_relation));
        spnr_aob_nom_appointee_relation.setAdapter(appointee_relation_adapter);
        appointee_relation_adapter.notifyDataSetChanged();

        txt_aob_nominee_dob = (TextView) findViewById(R.id.txt_aob_nominee_dob);
        txt_aob_nominee_dob.setOnClickListener(this);
        txt_aob_appointee_dob = (TextView) findViewById(R.id.txt_aob_appointee_dob);
        txt_aob_appointee_dob.setOnClickListener(this);

        edt_aob_nominee_full_name = (EditText) findViewById(R.id.edt_aob_nominee_full_name);
        edt_aob_nominee_percentage = (EditText) findViewById(R.id.edt_aob_nominee_percentage);
        edt_aob_nominee_address = (EditText) findViewById(R.id.edt_aob_nominee_address);
        edt_aob_nominee_address.setFilters(new InputFilter[]{mCommonMethods.aob_address_filter});

        //added 19-01-2021
        edt_aob_nominee_address2 = (EditText) findViewById(R.id.edt_aob_nominee_address2);
        edt_aob_nominee_address2.setFilters(new InputFilter[]{mCommonMethods.aob_address_filter});

        edt_aob_nominee_address3 = (EditText) findViewById(R.id.edt_aob_nominee_address3);
        edt_aob_nominee_address3.setFilters(new InputFilter[]{mCommonMethods.aob_address_filter});
        //ended 19-01-2021

        edt_aob_nominee_address_city = (EditText) findViewById(R.id.edt_aob_nominee_address_city);
        edt_aob_nominee_address_city.setFilters(new InputFilter[]{mCommonMethods.aob_address_filter});

        edt_aob_nominee_address_state = (EditText) findViewById(R.id.edt_aob_nominee_address_state);
        edt_aob_nominee_address_state.setFilters(new InputFilter[]{mCommonMethods.aob_address_filter});

        edt_aob_nominee_address_pincode = (EditText) findViewById(R.id.edt_aob_nominee_address_pincode);
        edt_aob_nom_appointee_full_name = (EditText) findViewById(R.id.edt_aob_nom_appointee_full_name);
        edt_aob_nom_witness_occupation = (EditText) findViewById(R.id.edt_aob_nom_witness_occupation);

        edt_aob_nom_witness_full_add = (EditText) findViewById(R.id.edt_aob_nom_witness_full_add);
        edt_aob_nom_witness_full_add.setFilters(new InputFilter[]{mCommonMethods.aob_address_filter});

        edt_aob_nom_witness_name = (EditText) findViewById(R.id.edt_aob_nom_witness_name);

        edt_aob_appointee_address = (EditText) findViewById(R.id.edt_aob_appointee_address);
        edt_aob_appointee_address.setFilters(new InputFilter[]{mCommonMethods.aob_address_filter});

        //added 19-01-2021
        edt_aob_appointee_address2 = (EditText) findViewById(R.id.edt_aob_appointee_address2);
        edt_aob_appointee_address2.setFilters(new InputFilter[]{mCommonMethods.aob_address_filter});

        edt_aob_appointee_address3 = (EditText) findViewById(R.id.edt_aob_appointee_address3);
        edt_aob_appointee_address3.setFilters(new InputFilter[]{mCommonMethods.aob_address_filter});
        //ended 19-01-2021

        edt_aob_appointee_address_city = (EditText) findViewById(R.id.edt_aob_appointee_address_city);
        edt_aob_appointee_address_city.setFilters(new InputFilter[]{mCommonMethods.aob_address_filter});

        edt_aob_appointee_address_state = (EditText) findViewById(R.id.edt_aob_appointee_address_state);
        edt_aob_appointee_address_state.setFilters(new InputFilter[]{mCommonMethods.aob_address_filter});

        edt_aob_appointee_address_pincode = (EditText) findViewById(R.id.edt_aob_appointee_address_pincode);

        btn_aob_nomination_next = (Button) findViewById(R.id.btn_aob_nomination_next);
        btn_aob_nomination_next.setOnClickListener(this);

        btn_aob_nomination_back = (Button) findViewById(R.id.btn_aob_nomination_back);
        btn_aob_nomination_back.setOnClickListener(this);

        btn_aob_nominee_address_pincode = (Button) findViewById(R.id.btn_aob_nominee_address_pincode);
        btn_aob_nominee_address_pincode.setOnClickListener(this);

        btn_aob_appointee_address_pincode = (Button) findViewById(R.id.btn_aob_appointee_address_pincode);
        btn_aob_appointee_address_pincode.setOnClickListener(this);

        ll_aob_appointee_details = (LinearLayout) findViewById(R.id.ll_aob_appointee_details);
        ll_aob_nominee_address_not_same = (LinearLayout) findViewById(R.id.ll_aob_nominee_address_not_same);
        ll_aob_appointee_address_not_same = (LinearLayout) findViewById(R.id.ll_aob_appointee_address_not_same);
        ll_aob_nominee_witness = (LinearLayout) findViewById(R.id.ll_aob_nominee_witness);

        chkbox_aob_witness_language = (CheckBox) findViewById(R.id.chkbox_aob_witness_language);

        chkbox_aob_witness_language.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                edt_aob_nom_witness_occupation.setText("");
                edt_aob_nom_witness_full_add.setText("");
                edt_aob_nom_witness_name.setText("");

                if (isChecked) {
                    ll_aob_nominee_witness.setVisibility(View.VISIBLE);
                } else
                    ll_aob_nominee_witness.setVisibility(View.GONE);
            }
        });

        mCalender = Calendar.getInstance();
        mDOBYear = mCalender.get(Calendar.YEAR);
        mDOBMonth = mCalender.get(Calendar.MONTH);
        mDOBDay = mCalender.get(Calendar.DAY_OF_MONTH);

        currentDate = new Date(mCalender.getTimeInMillis());

        str_nominational_info = new StringBuilder();

        edt_aob_nominee_address_pincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                is_nominee_pin_verify = false;

                if (s.toString().length() == 6) {
                    btn_aob_nominee_address_pincode.setEnabled(true);

                    edt_aob_nominee_address_pincode.setError(null);
                } else {
                    btn_aob_nominee_address_pincode.setEnabled(false);

                    edt_aob_nominee_address_pincode.setError("Enter valid pincode");
                }

            }
        });

        edt_aob_appointee_address_pincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                is_appointee_pin_verify = false;

                if (s.toString().length() == 6) {
                    btn_aob_appointee_address_pincode.setEnabled(true);

                    edt_aob_appointee_address_pincode.setError(null);
                } else {
                    btn_aob_appointee_address_pincode.setEnabled(false);

                    edt_aob_appointee_address_pincode.setError("Enter valid pincode");
                }

            }
        });

        //get Data from DB for nominee address if applicant and nominee address is same
        ArrayList<PojoAOB> lstRes = db.get_agent_on_boarding_details_by_ID(Activity_AOB_Authentication.row_details);

        if (lstRes.size() > 0) {
            String str_info = lstRes.get(0).getStr_personal_info();

            ParseXML mXml = new ParseXML();
            str_applicant_address = mXml.parseXmlTag(str_info, "personal_info_permanant_address1");

            //added 19-01-2021
            str_applicant_address2 = mXml.parseXmlTag(str_info, "personal_info_permanant_address2");
            str_applicant_address3 = mXml.parseXmlTag(str_info, "personal_info_permanant_address3");
            //end 19-01-2021

            str_pincode = mXml.parseXmlTag(str_info, "personal_info_permanant_address_pin");

            String str_nominee_info = lstRes.get(0).getStr_nomination_info();
            if (str_nominee_info != null) {

                String str_nominee_title = mParseXML.parseXmlTag(str_nominee_info, "nominee_info_nom_title");
                String str_nominee_full_name = mParseXML.parseXmlTag(str_nominee_info, "nominee_info_nom_full_name");
                String str_nominee_dob = mParseXML.parseXmlTag(str_nominee_info, "nominee_info_nom_dob");
                String str_nominee_relation = mParseXML.parseXmlTag(str_nominee_info, "nominee_info_nom_relation");
                String str_nominee_gender = mParseXML.parseXmlTag(str_nominee_info, "nominee_info_nom_gender");
                String str_nominee_percetage = mParseXML.parseXmlTag(str_nominee_info, "nominee_info_nom_percentage");
                String str_nominee_address = mParseXML.parseXmlTag(str_nominee_info, "nominee_info_nom_address");
                //added 19-01-2021
                String str_nominee_address2 = mParseXML.parseXmlTag(str_nominee_info, "nominee_info_nom_address2");
                String str_nominee_address3 = mParseXML.parseXmlTag(str_nominee_info, "nominee_info_nom_address3");
                //ended 19-01-2021
                String str_nominee_city = mParseXML.parseXmlTag(str_nominee_info, "nominee_info_nom_address_city");
                String str_nominee_state = mParseXML.parseXmlTag(str_nominee_info, "nominee_info_nom_address_state");
                String str_nominee_pincode = mParseXML.parseXmlTag(str_nominee_info, "nominee_info_nom_address_pincode");
                String str_appointee_title = mParseXML.parseXmlTag(str_nominee_info, "nominee_info_appointee_title");

                String str_appointee_full_name = mParseXML.parseXmlTag(str_nominee_info, "nominee_info_appointee_full_name");
                String str_appointee_dob = mParseXML.parseXmlTag(str_nominee_info, "nominee_info_appointee_dob");
                String str_appointee_relation = mParseXML.parseXmlTag(str_nominee_info, "nominee_info_appointee_relation");
                String str_appointee_address = mParseXML.parseXmlTag(str_nominee_info, "nominee_info_appointee_address");

                //added 19-01-2021
                String str_appointee_address2 = mParseXML.parseXmlTag(str_nominee_info, "nominee_info_appointee_address2");
                String str_appointee_address3 = mParseXML.parseXmlTag(str_nominee_info, "nominee_info_appointee_address3");
                //ended 19-01-2021

                String str_appointee_city = mParseXML.parseXmlTag(str_nominee_info, "nominee_info_appointee_address_city");
                String str_appointee_state = mParseXML.parseXmlTag(str_nominee_info, "nominee_info_appointee_address_state");
                String str_appointee_pincode = mParseXML.parseXmlTag(str_nominee_info, "nominee_info_appointee_address_pincode");
                String str_witness_occupation = mParseXML.parseXmlTag(str_nominee_info, "nominee_info_witness_occupation");
                String str_witness_full_adddress = mParseXML.parseXmlTag(str_nominee_info, "nominee_info_witness_full_address");
                String str_witness_name = mParseXML.parseXmlTag(str_nominee_info, "nominee_info_witness_name");

                /*List<String> lstTitle = new ArrayList<>();

                for (String strUppper : Arrays.asList(getResources().getStringArray(R.array.arr_aob_title))){
                    lstTitle.add(strUppper.toUpperCase());
                }*/

                spnr_aob_nominee_title.setSelection(
                        Arrays.asList(getResources().getStringArray(R.array.arr_aob_title)).indexOf(str_nominee_title));

                edt_aob_nominee_full_name.setText(str_nominee_full_name);

                if (!str_nominee_dob.equals("")) {
                    String[] arrDate = str_nominee_dob.split("-");
                    txt_aob_nominee_dob.setText(arrDate[1] + "-" + arrDate[0] + "-" + arrDate[2]);
                } else {
                    txt_aob_nominee_dob.setText("");
                }

                spnr_aob_nominee_relation.setSelection(Arrays.asList(getResources().
                        getStringArray(R.array.arr_aob_nominee_relation)).indexOf(str_nominee_relation));

                spnr_aob_nominee_gender.setSelection(Arrays.asList(getResources().
                        getStringArray(R.array.gender_arrays)).indexOf(str_nominee_gender));

                edt_aob_nominee_percentage.setText(str_nominee_percetage);

                if (str_nominee_city.equals("") &
                        str_nominee_state.equals("") & str_nominee_pincode.equals("")) {

                    ll_aob_nominee_address_not_same.setVisibility(View.GONE);

                    rb_aob_nominee_same_add_as_yes.setChecked(true);
                    rb_aob_nominee_same_add_as_no.setChecked(false);

                } else {
                    ll_aob_nominee_address_not_same.setVisibility(View.VISIBLE);

                    rb_aob_nominee_same_add_as_yes.setChecked(false);
                    rb_aob_nominee_same_add_as_no.setChecked(true);

                    edt_aob_nominee_address.setText(str_nominee_address);
                    edt_aob_nominee_address2.setText(str_nominee_address2);
                    edt_aob_nominee_address3.setText(str_nominee_address3);
                    edt_aob_nominee_address_city.setText(str_nominee_city);
                    edt_aob_nominee_address_state.setText(str_nominee_state);
                    edt_aob_nominee_address_pincode.setText(str_nominee_pincode);
                }

                if (str_appointee_full_name.equals("")) {

                    ll_aob_appointee_details.setVisibility(View.GONE);

                } else {
                    ll_aob_appointee_details.setVisibility(View.VISIBLE);

                    spnr_aob_nom_appointee_title.setSelection(
                            Arrays.asList(getResources().getStringArray(R.array.arr_aob_title)).indexOf(str_appointee_title));

                    edt_aob_nom_appointee_full_name.setText(str_appointee_full_name);

                    if (!str_appointee_dob.equals("")) {
                        String[] arrDate = str_appointee_dob.split("-");
                        txt_aob_appointee_dob.setText(arrDate[1] + "-" + arrDate[0] + "-" + arrDate[2]);
                    } else {
                        txt_aob_appointee_dob.setText("");
                    }

                    spnr_aob_nom_appointee_relation.setSelection(Arrays.asList(getResources().
                            getStringArray(R.array.arr_aob_nominee_relation)).indexOf(str_appointee_relation));

                    if (str_appointee_city.equals("") &
                            str_appointee_state.equals("") &
                            str_appointee_pincode.equals("")) {

                        ll_aob_appointee_address_not_same.setVisibility(View.GONE);

                        rb_aob_appointee_same_add_as_yes.setChecked(true);
                        rb_aob_appointee_same_add_as_no.setChecked(false);

                    } else {
                        ll_aob_appointee_address_not_same.setVisibility(View.VISIBLE);

                        rb_aob_appointee_same_add_as_yes.setChecked(false);
                        rb_aob_appointee_same_add_as_no.setChecked(true);

                        edt_aob_appointee_address.setText(str_appointee_address);

                        //added 19-01-2021
                        edt_aob_appointee_address2.setText(str_appointee_address2);
                        edt_aob_appointee_address3.setText(str_appointee_address3);
                        //ended 19-01-2021

                        edt_aob_appointee_address_city.setText(str_appointee_city);
                        edt_aob_appointee_address_state.setText(str_appointee_state);
                        edt_aob_appointee_address_pincode.setText(str_appointee_pincode);
                    }
                }

                if (str_witness_occupation.equals("")) {

                    chkbox_aob_witness_language.setChecked(false);
                    ll_aob_nominee_witness.setVisibility(View.GONE);

                } else {

                    chkbox_aob_witness_language.setChecked(true);
                    ll_aob_nominee_witness.setVisibility(View.VISIBLE);

                    edt_aob_nom_witness_occupation.setText(str_witness_occupation);
                    edt_aob_nom_witness_full_add.setText(str_witness_full_adddress);
                    edt_aob_nom_witness_name.setText(str_witness_name);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(ActivityAOBNomination.this, ActivityAOBOccupation.class);
        if (is_dashboard) {
            intent.putExtra("is_dashboard", is_dashboard);
        } else if (is_ia_upgrade) {
            intent.putExtra("is_ia_upgrade", is_ia_upgrade);
        }
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_aob_nomination_back:
                is_back_pressed = true;
                onBackPressed();
                break;

            case R.id.btn_aob_nominee_address_pincode:

                is_nominee_pin_verify = false;
                str_pincode = edt_aob_nominee_address_pincode.getText().toString();

                if (!str_pincode.equals("")) {

                    str_pin_flag = "nominee";

                    createSoapRequestToUploadDoc();

                } else {
                    mCommonMethods.showMessageDialog(mContext, "Please enter Pincode");
                }

                break;

            case R.id.btn_aob_appointee_address_pincode:

                is_appointee_pin_verify = false;
                str_pincode = edt_aob_appointee_address_pincode.getText().toString();

                if (!str_pincode.equals("")) {

                    str_pin_flag = "appointee";

                    createSoapRequestToUploadDoc();

                } else {
                    mCommonMethods.showMessageDialog(mContext, "Please enter Pincode");
                }

                break;

            case R.id.btn_aob_nomination_next:

                if (is_dashboard) {
                    Intent mIntent = new Intent(ActivityAOBNomination.this, ActivityAOBBankDetails.class);
                    mIntent.putExtra("is_dashboard", is_dashboard);
                    startActivity(mIntent);
                } else if (is_ia_upgrade) {
                    Intent mIntent = new Intent(ActivityAOBNomination.this, ActivityAOBBankDetails.class);
                    mIntent.putExtra("is_ia_upgrade", is_ia_upgrade);
                    startActivity(mIntent);
                } else {
                    //1. validate all details
                    String str_error = validate_all_details();
                    if (str_error.equals("")) {

                        //2. create xml string for data saving
                        get_nominational_info_xml();

                        //3. update data against global row id
                        ContentValues cv = new ContentValues();
                        cv.put(db.AGENT_ON_BOARDING_NOMINATION_INFO, str_nominational_info.toString());
                        cv.put(db.AGENT_ON_BOARDING_UPDATED_BY, mCommonMethods.GetUserCode(mContext));

                        Calendar c = Calendar.getInstance();
                        //save date in long
                        cv.put(db.AGENT_ON_BOARDING_UPDATED_DATE, new Date(c.getTimeInMillis()).getTime() + "");
                        cv.put(db.AGENT_ON_BOARDING_SYNCH_STATUS, "4");

                        int i = db.update_agent_on_boarding_details(cv, db.AGENT_ON_BOARDING_ID + " =? ",
                                new String[]{Activity_AOB_Authentication.row_details + ""});

                        mCommonMethods.showToast(mContext, "Details saved Successfully : " + i);

                        Intent mIntent = new Intent(ActivityAOBNomination.this, ActivityAOBBankDetails.class);
                        startActivity(mIntent);
                    } else {
                        mCommonMethods.showMessageDialog(mContext, str_error);
                    }
                }
                break;

            case R.id.txt_aob_nominee_dob:
                is_appointee = false;

                datePickerDialog = DatePickerDialog.newInstance(ActivityAOBNomination.this,
                        mDOBYear, mDOBMonth, mDOBDay);
                datePickerDialog.setThemeDark(false);
                datePickerDialog.showYearPickerFirst(false);
                datePickerDialog.setMaxDate(mCalender);
                datePickerDialog.dismissOnPause(true); //dismiss dialog when onPause() called?
                datePickerDialog.setAccentColor(getResources().getColor(R.color.Common_blue));
                datePickerDialog.setTitle("Nominee DOB");

                datePickerDialog.show(getFragmentManager(), DATE_NOMINEE_DOB);

                break;

            case R.id.txt_aob_appointee_dob:
                is_appointee = true;

                datePickerDialog = DatePickerDialog.newInstance(ActivityAOBNomination.this,
                        mDOBYear, mDOBMonth, mDOBDay);
                datePickerDialog.setThemeDark(false);
                datePickerDialog.showYearPickerFirst(false);
                //Applicant should be above 18 years.
                Calendar max_date_c = Calendar.getInstance();
                max_date_c.set(Calendar.YEAR, mDOBYear - 18);
                datePickerDialog.setMaxDate(max_date_c);
                datePickerDialog.dismissOnPause(true); //dismiss dialog when onPause() called?
                datePickerDialog.setAccentColor(getResources().getColor(R.color.Common_blue));
                datePickerDialog.setTitle("Appointee DOB");

                datePickerDialog.show(getFragmentManager(), DATE_APPOINTEE_DOB);
                break;

            default:
                break;

        }
    }

    private void get_nominational_info_xml() {

        String str_nominee_title = spnr_aob_nominee_title.getSelectedItem().toString();
        String str_nominee_full_name = mCommonMethods.removeExtraWhiteSpaces(edt_aob_nominee_full_name);
        String str_nominee_dob = txt_aob_nominee_dob.getText().toString();
        String str_nominee_relation = spnr_aob_nominee_relation.getSelectedItem().toString();
        String str_nominee_gender = spnr_aob_nominee_gender.getSelectedItem().toString();
        String str_nominee_percetage = edt_aob_nominee_percentage.getText().toString();
        String str_appointee_title = spnr_aob_nom_appointee_title.getSelectedItem().toString();
        String str_appointee_full_name = mCommonMethods.removeExtraWhiteSpaces(edt_aob_nom_appointee_full_name);
        String str_appointee_dob = txt_aob_appointee_dob.getText().toString();
        String str_appointee_relation = spnr_aob_nom_appointee_relation.getSelectedItem().toString();
        String str_witness_occupation = edt_aob_nom_witness_occupation.getText().toString();
        String str_witness_full_adddress = mCommonMethods.removeExtraWhiteSpaces(edt_aob_nom_witness_full_add);
        String str_witness_name = mCommonMethods.removeExtraWhiteSpaces(edt_aob_nom_witness_name);

        str_nominational_info = new StringBuilder();

        //str_occupational_info.append("<nominational_info>");

        str_nominational_info.append("<nominee_info_nom_title>").append(str_nominee_title).append("</nominee_info_nom_title>");
        str_nominational_info.append("<nominee_info_nom_full_name>").append(str_nominee_full_name.toUpperCase().trim()).append("</nominee_info_nom_full_name>");

        if (!str_nominee_dob.equals("")) {
            String[] arrDate = str_nominee_dob.split("-");
            str_nominational_info.append("<nominee_info_nom_dob>").append(arrDate[1] + "-" + arrDate[0] + "-" + arrDate[2]).append("</nominee_info_nom_dob>");
        } else {
            str_nominational_info.append("<nominee_info_nom_dob></nominee_info_nom_dob>");
        }
        str_nominational_info.append("<nominee_info_nom_relation>").append(str_nominee_relation).append("</nominee_info_nom_relation>");

        str_nominational_info.append("<nominee_info_nom_gender>").append(str_nominee_gender).append("</nominee_info_nom_gender>");

        str_nominational_info.append("<nominee_info_nom_percentage>").append(str_nominee_percetage).append("</nominee_info_nom_percentage>");

        String str_nominee_address = "", str_nominee_address2 = "", str_nominee_address3 = "",
                str_nominee_state = "", str_nominee_city = "", str_nominee_pincode = "";

        if (rb_aob_nominee_same_add_as_yes.isChecked()) {
            str_nominee_address = str_applicant_address;

            //added 19-01-2021
            str_nominee_address2 = str_applicant_address2;
            str_nominee_address3 = str_applicant_address3;
            str_nominee_pincode = str_pincode;
            //end 19-01-2021

            str_nominational_info.append("<nominee_info_nom_address>").append(str_nominee_address).append("</nominee_info_nom_address>");

            //added 19-01-2021
            str_nominational_info.append("<nominee_info_nom_address2>").append(str_nominee_address2).append("</nominee_info_nom_address2>");
            str_nominational_info.append("<nominee_info_nom_address3>").append(str_nominee_address3).append("</nominee_info_nom_address3>");
            //end 19-01-2021


            str_nominational_info.append("<nominee_info_nom_address_city>").append("").append("</nominee_info_nom_address_city>");
            str_nominational_info.append("<nominee_info_nom_address_state>").append("").append("</nominee_info_nom_address_state>");
            str_nominational_info.append("<nominee_info_nom_address_pincode>").append(str_nominee_pincode).append("</nominee_info_nom_address_pincode>");

        } else {

            str_nominee_address = mCommonMethods.removeExtraWhiteSpaces(edt_aob_nominee_address);

            //added 19-01-2021
            str_nominee_address2 = mCommonMethods.removeExtraWhiteSpaces(edt_aob_nominee_address2);
            str_nominee_address3 = mCommonMethods.removeExtraWhiteSpaces(edt_aob_nominee_address3);
            //ended 19-01-2021

            if (str_nominee_address.length() > 100)
                str_nominee_address = str_nominee_address.substring(0, 99);

            //added 19-01-2021
            if (str_nominee_address2.length() > 100)
                str_nominee_address2 = str_nominee_address2.substring(0, 99);
            if (str_nominee_address3.length() > 100)
                str_nominee_address3 = str_nominee_address3.substring(0, 99);
            //ended 19-01-2021

            str_nominee_city = mCommonMethods.removeExtraWhiteSpaces(edt_aob_nominee_address_city);
            str_nominee_state = mCommonMethods.removeExtraWhiteSpaces(edt_aob_nominee_address_state);
            str_nominee_pincode = edt_aob_nominee_address_pincode.getText().toString();

            str_nominational_info.append("<nominee_info_nom_address>").append(str_nominee_address.toUpperCase().trim()).append("</nominee_info_nom_address>");

            //added 19-01-2021
            str_nominational_info.append("<nominee_info_nom_address2>").append(str_nominee_address2.toUpperCase().trim()).append("</nominee_info_nom_address2>");
            str_nominational_info.append("<nominee_info_nom_address3>").append(str_nominee_address3.toUpperCase().trim()).append("</nominee_info_nom_address3>");
            //ended 19-01-2021

            str_nominational_info.append("<nominee_info_nom_address_city>").append(str_nominee_city).append("</nominee_info_nom_address_city>");
            str_nominational_info.append("<nominee_info_nom_address_state>").append(str_nominee_state).append("</nominee_info_nom_address_state>");
            str_nominational_info.append("<nominee_info_nom_address_pincode>").append(str_nominee_pincode).append("</nominee_info_nom_address_pincode>");
        }

        if (str_appointee_title.equalsIgnoreCase("Select Title"))
            str_nominational_info.append("<nominee_info_appointee_title>").append("").append("</nominee_info_appointee_title>");
        else
            str_nominational_info.append("<nominee_info_appointee_title>").append(str_appointee_title).append("</nominee_info_appointee_title>");

        str_nominational_info.append("<nominee_info_appointee_full_name>").append(str_appointee_full_name.toUpperCase()).append("</nominee_info_appointee_full_name>");

        if (!str_appointee_dob.equals("")) {
            String[] arrDate = str_appointee_dob.split("-");
            str_nominational_info.append("<nominee_info_appointee_dob>").append(arrDate[1] + "-" + arrDate[0] + "-" + arrDate[2]).append("</nominee_info_appointee_dob>");
        } else {
            str_nominational_info.append("<nominee_info_appointee_dob></nominee_info_appointee_dob>");
        }

        if (str_appointee_relation.equalsIgnoreCase("Select Relation"))
            str_nominational_info.append("<nominee_info_appointee_relation>").append("").append("</nominee_info_appointee_relation>");
        else
            str_nominational_info.append("<nominee_info_appointee_relation>").append(str_appointee_relation).append("</nominee_info_appointee_relation>");

        if (is_appointee_required) {
            if (rb_aob_appointee_same_add_as_yes.isChecked()) {
                str_nominational_info.append("<nominee_info_appointee_address>").append(str_nominee_address.toUpperCase().trim()).append("</nominee_info_appointee_address>");

                //added 19-01-2021
                str_nominational_info.append("<nominee_info_appointee_address2>").append(str_nominee_address2.toUpperCase().trim()).append("</nominee_info_appointee_address2>");
                str_nominational_info.append("<nominee_info_appointee_address3>").append(str_nominee_address3.toUpperCase().trim()).append("</nominee_info_appointee_address3>");
                //ended 19-01-2021

                str_nominational_info.append("<nominee_info_appointee_address_city>").append(str_nominee_city).append("</nominee_info_appointee_address_city>");
                str_nominational_info.append("<nominee_info_appointee_address_state>").append(str_nominee_state).append("</nominee_info_appointee_address_state>");
                str_nominational_info.append("<nominee_info_appointee_address_pincode>").append(str_nominee_pincode).append("</nominee_info_appointee_address_pincode>");
            } else {

                String str_appointee_add = mCommonMethods.removeExtraWhiteSpaces(edt_aob_appointee_address);
                if (str_appointee_add.length() > 100)
                    str_appointee_add = str_appointee_add.substring(0, 99);

                //added 19-01-2021
                String str_appointee_add2 = mCommonMethods.removeExtraWhiteSpaces(edt_aob_appointee_address2);
                if (str_appointee_add2.length() > 100)
                    str_appointee_add2 = str_appointee_add2.substring(0, 99);

                String str_appointee_add3 = mCommonMethods.removeExtraWhiteSpaces(edt_aob_appointee_address3);
                if (str_appointee_add3.length() > 100)
                    str_appointee_add3 = str_appointee_add3.substring(0, 99);
                //ended 19-01-2021

                str_nominational_info.append("<nominee_info_appointee_address>").append(str_appointee_add.toUpperCase().trim()).append("</nominee_info_appointee_address>");

                //added 19-01-2021
                str_nominational_info.append("<nominee_info_appointee_address2>").append(str_appointee_add2.toUpperCase().trim()).append("</nominee_info_appointee_address2>");
                str_nominational_info.append("<nominee_info_appointee_address3>").append(str_appointee_add3.toUpperCase().trim()).append("</nominee_info_appointee_address3>");
                //ended 19-01-2021

                str_nominational_info.append("<nominee_info_appointee_address_city>").append(edt_aob_appointee_address_city.getText().toString()).append("</nominee_info_appointee_address_city>");
                str_nominational_info.append("<nominee_info_appointee_address_state>").append(edt_aob_appointee_address_state.getText().toString()).append("</nominee_info_appointee_address_state>");
                str_nominational_info.append("<nominee_info_appointee_address_pincode>").append(edt_aob_appointee_address_pincode.getText().toString()).append("</nominee_info_appointee_address_pincode>");
            }
        } else {
            str_nominational_info.append("<nominee_info_appointee_address>").append("").append("</nominee_info_appointee_address>");

            //added 19-01-2021
            str_nominational_info.append("<nominee_info_appointee_address2>").append("").append("</nominee_info_appointee_address2>");
            str_nominational_info.append("<nominee_info_appointee_address3>").append("").append("</nominee_info_appointee_address3>");
            //ended 19-01-2021

            str_nominational_info.append("<nominee_info_appointee_address_city>").append("").append("</nominee_info_appointee_address_city>");
            str_nominational_info.append("<nominee_info_appointee_address_state>").append("").append("</nominee_info_appointee_address_state>");
            str_nominational_info.append("<nominee_info_appointee_address_pincode>").append("").append("</nominee_info_appointee_address_pincode>");
        }

        if (chkbox_aob_witness_language.isChecked()) {
            str_nominational_info.append("<nominee_info_witness_occupation>").append(str_witness_occupation).append("</nominee_info_witness_occupation>");
            str_nominational_info.append("<nominee_info_witness_full_address>").append(str_witness_full_adddress).append("</nominee_info_witness_full_address>");
            str_nominational_info.append("<nominee_info_witness_name>").append(str_witness_name).append("</nominee_info_witness_name>");
        } else {
            str_nominational_info.append("<nominee_info_witness_occupation>").append("").append("</nominee_info_witness_occupation>");
            str_nominational_info.append("<nominee_info_witness_full_address>").append("").append("</nominee_info_witness_full_address>");
            str_nominational_info.append("<nominee_info_witness_name>").append("").append("</nominee_info_witness_name>");
        }

        //str_occupational_info.append("</nominational_info>");

    }

    public String validate_all_details() {

        if (spnr_aob_nominee_title.getSelectedItem().toString().equals("Select Title")) {
            return "Please select Title";
        } else if (edt_aob_nominee_full_name.getText().toString().equals("")) {
            return "Please enter nominee full name";
        } else if (txt_aob_nominee_dob.getText().toString().equals("")) {
            return "Please select nominee DOB";
        } else if (spnr_aob_nominee_relation.getSelectedItem().toString().equals("Select Relation")) {
            return "Please select relation with nominee";
        } else if (edt_aob_nominee_percentage.getText().toString().equals("")) {
            return "Please enter percentage";
        }/*else if (!edt_aob_nominee_percentage.getText().toString().equals("")){
            if (Integer.parseInt(edt_aob_nominee_percentage.getText().toString()) >= 100){
                return "Percentage sum of all nominee should not be equal to 100%";
            }
        }*/ else if (rb_aob_nominee_same_add_as_no.isChecked() &
                edt_aob_nominee_address.getText().toString().equals("")) {
            edt_aob_nominee_address.requestFocus();
            return "Please enter nominee address";
        } else if (rb_aob_nominee_same_add_as_no.isChecked() &
                edt_aob_nominee_address2.getText().toString().equals("")) {
            edt_aob_nominee_address2.requestFocus();
            return "Please enter nominee address-2";
        } else if (rb_aob_nominee_same_add_as_no.isChecked() &
                edt_aob_nominee_address3.getText().toString().equals("")) {
            edt_aob_nominee_address3.requestFocus();
            return "Please enter nominee address-3";
        } else if (rb_aob_nominee_same_add_as_no.isChecked() &
                edt_aob_nominee_address_city.getText().toString().equals("")) {
            edt_aob_nominee_address_city.requestFocus();
            return "Please enter nominee city";
        } else if (rb_aob_nominee_same_add_as_no.isChecked() &
                edt_aob_nominee_address_state.getText().toString().equals("")) {
            edt_aob_nominee_address_state.requestFocus();
            return "Please enter nominee state";
        } else if (rb_aob_nominee_same_add_as_no.isChecked() &
                edt_aob_nominee_address_pincode.getText().toString().equals("")) {
            edt_aob_nominee_address_pincode.requestFocus();
            return "Please enter nominee address pincode";
        } else if (rb_aob_nominee_same_add_as_no.isChecked() &
                !is_nominee_pin_verify) {
            edt_aob_nominee_address_pincode.requestFocus();
            return "Please verify nominee address pincode";
        } else if (is_appointee_required &
                spnr_aob_nom_appointee_title.getSelectedItem().toString().equals("Select Title")) {

            spnr_aob_nom_appointee_title.requestFocus();
            return "Please select appointee title";
        } else if (is_appointee_required &
                edt_aob_nom_appointee_full_name.getText().toString().equals("")) {
            edt_aob_nom_appointee_full_name.requestFocus();
            return "Please enter appointee full name";
        } else if (is_appointee_required &
                txt_aob_appointee_dob.getText().toString().equals("")) {
            txt_aob_appointee_dob.requestFocus();
            return "Please enter appointee DOB";
        } else if (is_appointee_required &
                spnr_aob_nom_appointee_relation.getSelectedItem().toString().equals("Select Relation")) {
            spnr_aob_nom_appointee_relation.requestFocus();
            return "Please select nominee relation with appointee";
        } else if (is_appointee_required &
                rb_aob_appointee_same_add_as_no.isChecked() &
                edt_aob_appointee_address.getText().toString().equals("")) {
            edt_aob_appointee_address.requestFocus();
            return "Please enter appointee address";
        } else if (is_appointee_required &
                rb_aob_appointee_same_add_as_no.isChecked() &
                edt_aob_appointee_address2.getText().toString().equals("")) {
            edt_aob_appointee_address2.requestFocus();
            return "Please enter appointee - 2 address";
        } else if (is_appointee_required &
                rb_aob_appointee_same_add_as_no.isChecked() &
                edt_aob_appointee_address3.getText().toString().equals("")) {
            edt_aob_appointee_address3.requestFocus();
            return "Please enter appointee - 3 address";
        } else if (is_appointee_required & rb_aob_appointee_same_add_as_no.isChecked() &
                edt_aob_appointee_address_city.getText().toString().equals("")) {
            edt_aob_appointee_address_city.requestFocus();
            return "Please enter appointee address city";
        } else if (is_appointee_required &
                rb_aob_appointee_same_add_as_no.isChecked() &
                edt_aob_appointee_address_state.getText().toString().equals("")) {
            edt_aob_appointee_address_state.requestFocus();
            return "Please enter appointee address state";
        } else if (is_appointee_required &
                rb_aob_appointee_same_add_as_no.isChecked() &
                !is_appointee_pin_verify) {
            edt_aob_appointee_address_pincode.requestFocus();
            return "Please enter appointee address pincode";

        } else if (chkbox_aob_witness_language.isChecked() &
                edt_aob_nom_witness_occupation.getText().toString().equals("")) {

            edt_aob_nom_witness_occupation.requestFocus();
            return "Please enter witness occupation";
        } else if (chkbox_aob_witness_language.isChecked() &
                edt_aob_nom_witness_full_add.getText().toString().equals("")) {
            edt_aob_nom_witness_full_add.requestFocus();
            return "Please select witness full address";
        } else if (chkbox_aob_witness_language.isChecked() &
                edt_aob_nom_witness_name.getText().toString().equals("")) {
            edt_aob_nom_witness_name.requestFocus();
            return "Please enter witness name";

        } else
            return "";
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        String strSelectedDate = (dayOfMonth < 10 ? "0" : "") + dayOfMonth + "-"
                + ((monthOfYear + 1) < 10 ? "0" : "") + (monthOfYear + 1) + "-"
                + year;

        switch (view.getTag()) {

            case DATE_NOMINEE_DOB:
                int age = new CommonForAllProd().getAOBAge(mDOBYear, (mDOBMonth + 1), mDOBDay, strSelectedDate);
                if (age >= 18) {
                    //gone appointee details
                    ll_aob_appointee_details.setVisibility(View.GONE);

                    is_appointee_required = false;

                    spnr_aob_nom_appointee_title.setSelection(0);
                    edt_aob_nom_appointee_full_name.setText("");
                    txt_aob_appointee_dob.setText("");
                    spnr_aob_nom_appointee_relation.setSelection(0);

                    edt_aob_appointee_address.setText("");
                    //added  19-01-2021
                    edt_aob_appointee_address2.setText("");
                    edt_aob_appointee_address3.setText("");
                    //ended  19-01-2021
                    edt_aob_appointee_address_city.setText("");

                    edt_aob_appointee_address_state.setText("");

                    edt_aob_appointee_address_pincode.setText("");


                } else {
                    //visible appointee details
                    ll_aob_appointee_details.setVisibility(View.VISIBLE);

                    is_appointee_required = true;
                }

                txt_aob_nominee_dob.setText(strSelectedDate);
                break;

            case DATE_APPOINTEE_DOB:
                txt_aob_appointee_dob.setText(strSelectedDate);
                break;
        }

    }

    public void enableDisableAllFields(boolean is_enable) {

        spnr_aob_nominee_title.setEnabled(is_enable);

        rg_aob_nominee_same_add_as_applicant.setEnabled(is_enable);
        rb_aob_nominee_same_add_as_yes.setEnabled(is_enable);
        rb_aob_nominee_same_add_as_no.setEnabled(is_enable);

        rg_aob_appointee_same_add_as_nominee.setEnabled(is_enable);
        rb_aob_appointee_same_add_as_yes.setEnabled(is_enable);
        rb_aob_appointee_same_add_as_no.setEnabled(is_enable);

        spnr_aob_nominee_relation.setEnabled(is_enable);

        spnr_aob_nominee_gender.setEnabled(is_enable);

        spnr_aob_nom_appointee_title.setEnabled(is_enable);

        spnr_aob_nom_appointee_relation.setEnabled(is_enable);

        txt_aob_nominee_dob.setEnabled(is_enable);

        txt_aob_appointee_dob.setEnabled(is_enable);

        edt_aob_nominee_full_name.setEnabled(is_enable);
        edt_aob_nominee_percentage.setEnabled(is_enable);
        edt_aob_nominee_address.setEnabled(is_enable);
        //added 19-01-2021
        edt_aob_nominee_address2.setEnabled(is_enable);
        edt_aob_nominee_address3.setEnabled(is_enable);
        //ended 19-01-2021

        edt_aob_nominee_address_city.setEnabled(is_enable);
        edt_aob_nominee_address_state.setEnabled(is_enable);
        edt_aob_nominee_address_pincode.setEnabled(is_enable);
        edt_aob_nom_appointee_full_name.setEnabled(is_enable);
        edt_aob_nom_witness_occupation.setEnabled(is_enable);
        edt_aob_nom_witness_full_add.setEnabled(is_enable);
        edt_aob_nom_witness_name.setEnabled(is_enable);
        edt_aob_appointee_address.setEnabled(is_enable);

        //added 19-01-2021
        edt_aob_appointee_address2.setEnabled(is_enable);
        edt_aob_appointee_address3.setEnabled(is_enable);
        //ended 19-01-2021

        edt_aob_appointee_address_city.setEnabled(is_enable);
        edt_aob_appointee_address_state.setEnabled(is_enable);
        edt_aob_appointee_address_pincode.setEnabled(is_enable);

        chkbox_aob_witness_language.setEnabled(is_enable);
    }

    private void createSoapRequestToUploadDoc() {
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_VERIFY_PINCODE);

        request.addProperty("strPincode", str_pincode);

        new AsyncUploadFile_Common(mContext, this, request, METHOD_NAME_VERIFY_PINCODE).execute();
    }

    @Override
    public void onUploadComplete(Boolean result) {
        if (result) {

            if (str_pin_flag.equals("nominee")) {
                is_nominee_pin_verify = true;
            } else if (str_pin_flag.equals("appointee")) {
                is_appointee_pin_verify = true;
            }
            mCommonMethods.showMessageDialog(mContext, "Pincode is verified");

        } else {

            mCommonMethods.showMessageDialog(mContext, "Pincode is invalid");
        }
    }
}
