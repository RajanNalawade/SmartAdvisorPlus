package sbilife.com.pointofsale_bancaagency.agent_on_boarding;


import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

public class ActivityAOBForm1A extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private final String DATE_APPOINTEE_DATE = "AppointeeDate";
    private final String DATE_CESSATION_DATE = "CessationDate";

    private CommonMethods mCommonMethods;
    private Context mContext;
    private DatabaseHelper db;
    private Button btn_aob_form1a_submit, btn_aob_form1a_back;
    private Spinner spnr_aob_form1a_any_insurance;
    private TextView txt_aob_form1a_doa, txt_aob_form1a_doc;
    private LinearLayout ll_aob_form1a_any_insurance_yes, ll_aob_form1a_insurance_reason_for_cess;
    private EditText edt_aob_form1a_insurance_name, edt_aob_form1a_insurance_agency_code, edt_aob_form1a_insurance_reason_for_cess;
    private int mDOBDay = 0, mDOBYear = 0, mDOBMonth = 0;
    private ArrayList<PojoAOB> lstRes = new ArrayList<>();

    private Calendar mCalender;
    private DatePickerDialog datePickerDialog;
    private Date currentDate;

    private StringBuilder str_form1a_info;

    private ParseXML mParseXML;
    private boolean is_dashboard = false, is_back_pressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_aob_form_1a);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.cifenrollment_pf_activity_title);

        if (getIntent().hasExtra("is_dashboard"))
            is_dashboard = getIntent().getBooleanExtra("is_dashboard", false);

        initialisation();

        if (is_dashboard){
            //non editable with no saving
            enableDisableAllFields(false);
        }else {
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

        spnr_aob_form1a_any_insurance = (Spinner) findViewById(R.id.spnr_aob_form1a_any_insurance);
        ArrayAdapter<String> any_insurance_adapter = new ArrayAdapter<String>(
                mContext, R.layout.spinner_aob, getResources().getStringArray(R.array.arr_aob_ans_yes_no));
        spnr_aob_form1a_any_insurance.setAdapter(any_insurance_adapter);
        any_insurance_adapter.notifyDataSetChanged();

        txt_aob_form1a_doa = (TextView) findViewById(R.id.txt_aob_form1a_doa);
        txt_aob_form1a_doa.setOnClickListener(this);
        txt_aob_form1a_doc = (TextView) findViewById(R.id.txt_aob_form1a_doc);
        txt_aob_form1a_doc.setOnClickListener(this);

        ll_aob_form1a_any_insurance_yes = (LinearLayout) findViewById(R.id.ll_aob_form1a_any_insurance_yes);

        ll_aob_form1a_insurance_reason_for_cess = (LinearLayout) findViewById(R.id.ll_aob_form1a_insurance_reason_for_cess);

        edt_aob_form1a_insurance_name = (EditText) findViewById(R.id.edt_aob_form1a_insurance_name);
        edt_aob_form1a_insurance_agency_code = (EditText) findViewById(R.id.edt_aob_form1a_insurance_agency_code);
        edt_aob_form1a_insurance_reason_for_cess = (EditText) findViewById(R.id.edt_aob_form1a_insurance_reason_for_cess);

        btn_aob_form1a_submit = (Button) findViewById(R.id.btn_aob_form1a_submit);
        btn_aob_form1a_submit.setOnClickListener(this);

        btn_aob_form1a_back = (Button) findViewById(R.id.btn_aob_form1a_back);
        btn_aob_form1a_back.setOnClickListener(this);

        mCalender = Calendar.getInstance();
        mDOBYear = mCalender.get(Calendar.YEAR);
        mDOBMonth = mCalender.get(Calendar.MONTH);
        mDOBDay = mCalender.get(Calendar.DAY_OF_MONTH);

        currentDate = new Date(mCalender.getTimeInMillis());

        str_form1a_info = new StringBuilder();

        //set Data from DB
        lstRes = db.get_agent_on_boarding_details_by_ID(Activity_AOB_Authentication.row_details);

        if (lstRes.size() > 0) {

            String str_form_1a = lstRes.get(0).getStr_form_1_a();
            str_form_1a = str_form_1a == null ? "" : str_form_1a;

            if (!str_form_1a.equals("")){

                String str_any_insurance = mParseXML.parseXmlTag(str_form_1a, "form1a_info_any_insurance");
                String str_insurance_name = mParseXML.parseXmlTag(str_form_1a, "form1a_info_insurance_name");
                String str_insurance_agency_code = mParseXML.parseXmlTag(str_form_1a, "form1a_info_insurance_agency_code");
                String str_insurance_appointment_date = mParseXML.parseXmlTag(str_form_1a, "form1a_info_insurance_appointment_date");
                String str_insurance_cessation_date = mParseXML.parseXmlTag(str_form_1a, "form1a_info_insurance_cessation_date");
                String str_insurance_reason_for_cess = mParseXML.parseXmlTag(str_form_1a, "form1a_info_insurance_reason_for_cess");

                spnr_aob_form1a_any_insurance.setSelection(Arrays.asList(getResources().
                        getStringArray(R.array.arr_aob_ans_yes_no)).indexOf(str_any_insurance));

                if (str_any_insurance.equals("Yes")){
                    ll_aob_form1a_any_insurance_yes.setVisibility(View.VISIBLE);

                    edt_aob_form1a_insurance_name.setText(str_insurance_name);

                    edt_aob_form1a_insurance_agency_code.setText(str_insurance_agency_code);

                    if (!str_insurance_appointment_date.equals("")) {
                        String[] arrDate = str_insurance_appointment_date.split("-");
                        txt_aob_form1a_doa.setText(arrDate[1] + "-" + arrDate[0] + "-" + arrDate[2]);
                    } else {
                        txt_aob_form1a_doa.setText("");
                    }

                    if (str_insurance_cessation_date.equals("")){

                        ll_aob_form1a_insurance_reason_for_cess.setVisibility(View.GONE);

                        edt_aob_form1a_insurance_reason_for_cess.setText("");

                    }else {
                        ll_aob_form1a_insurance_reason_for_cess.setVisibility(View.VISIBLE);

                        if (!str_insurance_cessation_date.equals("")) {
                            String[] arrDate = str_insurance_cessation_date.split("-");
                            txt_aob_form1a_doc.setText(arrDate[1] + "-" + arrDate[0] + "-" + arrDate[2]);
                        } else {
                            txt_aob_form1a_doc.setText("");
                        }

                        edt_aob_form1a_insurance_reason_for_cess.setText(str_insurance_reason_for_cess);
                    }

                }else {
                    ll_aob_form1a_any_insurance_yes.setVisibility(View.GONE);
                }
            }
        }

        spnr_aob_form1a_any_insurance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (lstRes.size() > 0){
                    String str_res = lstRes.get(0).getStr_form_1_a() == null ? "" : lstRes.get(0).getStr_form_1_a();
                    if (str_res.equals("")){
                        edt_aob_form1a_insurance_name.setText("");
                        edt_aob_form1a_insurance_agency_code.setText("");

                        txt_aob_form1a_doa.setText("");

                        txt_aob_form1a_doc.setText("");

                        edt_aob_form1a_insurance_reason_for_cess.setText("");

                        if (position == 0) {
                            ll_aob_form1a_any_insurance_yes.setVisibility(View.VISIBLE);
                        } else {
                            ll_aob_form1a_any_insurance_yes.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void get_form1a_xml() {

        String str_any_insurance = spnr_aob_form1a_any_insurance.getSelectedItem().toString();
        String str_insurance_name = edt_aob_form1a_insurance_name.getText().toString();
        String str_insurance_agency_code = edt_aob_form1a_insurance_agency_code.getText().toString();
        String str_insurance_appointment_date = txt_aob_form1a_doa.getText().toString();
        String str_insurance_cessation_date = txt_aob_form1a_doc.getText().toString();
        String str_insurance_reason_for_cess = edt_aob_form1a_insurance_reason_for_cess.getText().toString();

        str_form1a_info = new StringBuilder();

        //str_form1a_info.append("<form1a_info>");
        str_form1a_info.append("<form1a_info_any_insurance>").append(str_any_insurance).append("</form1a_info_any_insurance>");
        str_form1a_info.append("<form1a_info_insurance_name>").append(str_insurance_name).append("</form1a_info_insurance_name>");
        str_form1a_info.append("<form1a_info_insurance_agency_code>").append(str_insurance_agency_code).append("</form1a_info_insurance_agency_code>");

        if (!str_insurance_appointment_date.equals("")) {
            String[] arrDate = str_insurance_appointment_date.split("-");
            str_form1a_info.append("<form1a_info_insurance_appointment_date>").append(arrDate[1] + "-" + arrDate[0] + "-" + arrDate[2]).append("</form1a_info_insurance_appointment_date>");
        } else {
            str_form1a_info.append("<form1a_info_insurance_appointment_date></form1a_info_insurance_appointment_date>");
        }

        if (!str_insurance_cessation_date.equals("")) {
            String[] arrDate = str_insurance_cessation_date.split("-");
            str_form1a_info.append("<form1a_info_insurance_cessation_date>").append(arrDate[1] + "-" + arrDate[0] + "-" + arrDate[2]).append("</form1a_info_insurance_cessation_date>");
        } else {
            str_form1a_info.append("<form1a_info_insurance_cessation_date></form1a_info_insurance_cessation_date>");
        }

        str_form1a_info.append("<form1a_info_insurance_reason_for_cess>").append(str_insurance_reason_for_cess).append("</form1a_info_insurance_reason_for_cess>");
        //str_occupational_info.append("</form1a_info>");

    }

    public String validate_all_details() {

        if (spnr_aob_form1a_any_insurance.getSelectedItem().equals("Yes") &
                edt_aob_form1a_insurance_name.getText().toString().equals("")) {
            edt_aob_form1a_insurance_name.requestFocus();
            return "Please enter name of insurer";
        } else if (spnr_aob_form1a_any_insurance.getSelectedItem().equals("Yes") &
                edt_aob_form1a_insurance_agency_code.getText().toString().equals("")) {
            edt_aob_form1a_insurance_agency_code.requestFocus();
            return "Please enter agency code";
        } else if (spnr_aob_form1a_any_insurance.getSelectedItem().equals("Yes") &
                txt_aob_form1a_doa.getText().toString().equals("")) {
            txt_aob_form1a_doa.requestFocus();
            return "Please enter date of appointment";
        } else if (spnr_aob_form1a_any_insurance.getSelectedItem().equals("Yes") &
                !txt_aob_form1a_doc.getText().toString().equals("") &
                edt_aob_form1a_insurance_reason_for_cess.getText().toString().equals("")) {
            edt_aob_form1a_insurance_reason_for_cess.requestFocus();
            return "Please enter reason for cessation of agency";
        } else
            return "";
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(ActivityAOBForm1A.this, ActivityAOBBankDetails.class);
        if (is_dashboard){
        intent.putExtra("is_dashboard", is_dashboard);
        }
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_aob_form1a_back:
                is_back_pressed = true;
                onBackPressed();
                break;

            case R.id.btn_aob_form1a_submit:

                if (!is_dashboard){

                    //1. validate all details
                    String str_error = validate_all_details();
                    if (str_error.equals("")) {

                        //2. create xml string for data saving
                        get_form1a_xml();

                        //3. update data against global row id
                        ContentValues cv = new ContentValues();
                        cv.put(db.AGENT_ON_BOARDING_FORM_1_A, str_form1a_info.toString());
                        cv.put(db.AGENT_ON_BOARDING_UPDATED_BY, mCommonMethods.GetUserCode(mContext));

                        Calendar c = Calendar.getInstance();
                        //save date in long
                        cv.put(db.AGENT_ON_BOARDING_UPDATED_DATE, new Date(c.getTimeInMillis()).getTime() + "");
                        cv.put(db.AGENT_ON_BOARDING_SYNCH_STATUS, "6");

                        int i = db.update_agent_on_boarding_details(cv, db.AGENT_ON_BOARDING_ID + " =? ",
                                new String[]{Activity_AOB_Authentication.row_details + ""});

                        mCommonMethods.showToast(mContext, "Details saved Successfully : " + i);

                        Intent mIntent = new Intent(ActivityAOBForm1A.this, ActivityAOBExamTraining.class);
                        mIntent.putExtra("is_dashboard", is_dashboard);
                        startActivity(mIntent);

                    /*//synch all details to server
                    ArrayList<PojoAOB> lstRes = db.get_agent_on_boarding_details_by_ID(Activity_AOB_Authentication.row_details);
                    if(lstRes.size() >0){

                        //create final Agent on boarding string to synch
                        str_final_aob_info.append("<?xml version='1.0' encoding='utf-8' ?><agent_on_boarding>");
                        str_final_aob_info.append(lstRes.get(0).getStr_personal_info().toString());//personal info
                        str_final_aob_info.append(lstRes.get(0).getStr_occupation_info().toString());//occupational info
                        str_final_aob_info.append(lstRes.get(0).getStr_nomination_info().toString());//nominational info
                        str_final_aob_info.append(lstRes.get(0).getStr_bank_details().toString());//bank details info
                        str_final_aob_info.append(lstRes.get(0).getStr_form_1_a().toString());//form 1-a info

                        SimpleDateFormat sdp = new SimpleDateFormat("MM-dd-yyyy");
                        String str_created_date = sdp.format(new Date(c.getTimeInMillis()));
                        str_final_aob_info.append("<created_date>" + str_created_date + "</created_date>");//created_date

                        str_final_aob_info.append("</agent_on_boarding>");

                        mAsyncAllAOB = new AsyncAllAOB();
                        mAsyncAllAOB.execute();

                    } else {
                        mCommonMethods.showMessageDialog(mContext, "Data Synch Failed");
                    }*/
                    } else {
                        mCommonMethods.showMessageDialog(mContext, str_error);
                    }

                }else if (is_dashboard){
                    Intent mIntent = new Intent(ActivityAOBForm1A.this, ActivityAOBExamTraining.class);
                    mIntent.putExtra("is_dashboard", is_dashboard);
                    startActivity(mIntent);
                }

                break;

            case R.id.txt_aob_form1a_doa:

                datePickerDialog = DatePickerDialog.newInstance(ActivityAOBForm1A.this,
                        mDOBYear, mDOBMonth, mDOBDay);
                datePickerDialog.setThemeDark(false);
                datePickerDialog.showYearPickerFirst(false);
                datePickerDialog.dismissOnPause(true); //dismiss dialog when onPause() called?
                datePickerDialog.setAccentColor(getResources().getColor(R.color.Common_blue));
                datePickerDialog.setMaxDate(mCalender);
                datePickerDialog.setTitle("Appointee Date");
                datePickerDialog.show(getFragmentManager(), DATE_APPOINTEE_DATE);

                break;

            case R.id.txt_aob_form1a_doc:

                datePickerDialog = DatePickerDialog.newInstance(ActivityAOBForm1A.this,
                        mDOBYear, mDOBMonth, mDOBDay);
                datePickerDialog.setThemeDark(false);
                datePickerDialog.showYearPickerFirst(false);
                datePickerDialog.dismissOnPause(true); //dismiss dialog when onPause() called?
                datePickerDialog.setAccentColor(getResources().getColor(R.color.Common_blue));
                //datePickerDialog.setMinDate(mCalender);
                datePickerDialog.setTitle("Cessation Date");
                datePickerDialog.show(getFragmentManager(), DATE_CESSATION_DATE);
                break;

            default:
                break;

        }
    }

    public void enableDisableAllFields(boolean is_enable){

        spnr_aob_form1a_any_insurance.setEnabled(is_enable);

        edt_aob_form1a_insurance_name.setEnabled(is_enable);

        edt_aob_form1a_insurance_agency_code.setEnabled(is_enable);
        txt_aob_form1a_doa.setEnabled(is_enable);

        txt_aob_form1a_doc.setEnabled(is_enable);
        edt_aob_form1a_insurance_reason_for_cess.setEnabled(is_enable);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        String strSelectedDate = (dayOfMonth < 10 ? "0" : "") + dayOfMonth + "-"
                + ((monthOfYear + 1) < 10 ? "0" : "") + (monthOfYear + 1) + "-"
                + year;

        switch (view.getTag()){
            case DATE_APPOINTEE_DATE:
                /*int age = new CommonForAllProd().calculateMyAge(mDOBYear, mDOBMonth, mDOBDay,strSelectedDate);
                    if (age >18){
                        txt_aob_personal_dob.setText(strSelectedDate);
                    }else{
                        mCommonMethods.showMessageDialog(mContext, "should be above 18 years.");
                    }*/

                txt_aob_form1a_doa.setText(strSelectedDate);
                break;

            case DATE_CESSATION_DATE:

                ll_aob_form1a_insurance_reason_for_cess.setVisibility(View.VISIBLE);

                try{

                    SimpleDateFormat sdp = new SimpleDateFormat("dd-MM-yyyy");
                    Date selectedDate = sdp.parse(strSelectedDate);

                    long diff = selectedDate.getTime() - currentDate.getTime();

                    int dayCount = (int) (diff / (1000 * 60 * 60 * 24));

                    // Date should be less than 90 days from system date
                    if (dayCount > 90){
                        txt_aob_form1a_doc.setText(strSelectedDate);
                    }else{
                        mCommonMethods.showMessageDialog(mContext, "Date should be greater than 90 days from date of application");
                    }

                }catch (ParseException e){
                    e.printStackTrace();
                    mCommonMethods.showToast(mContext, "parse error \n"+e.getMessage());
                }

                break;
        }
    }
}
