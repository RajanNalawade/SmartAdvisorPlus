package sbilife.com.pointofsale_bancaagency.agent_on_boarding;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

public class ActivityAOBBasicDetails extends AppCompatActivity implements View.OnClickListener{

    private Context mContext;

    private CommonMethods mCommonMethods;

    private DatabaseHelper db;

    private EditText edt_aob_basic_irn;

    private Spinner spnr_aob_basic_enroll_type;

    private CheckBox chkbox_aob_basic_available_agnt_lst, chkbox_aob_basic_available_agnt_blacklst;

    private Button btn_aob_basic_back, btn_aob_basic_next;

    private ParseXML mParseXML;

    private boolean is_dashboard = false, is_back_pressed = false;

    private StringBuilder str_basic_details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_aob_basic_details);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.cifenrollment_pf_activity_title);

        is_dashboard = getIntent().getBooleanExtra("is_dashboard", false);

        initialisation();

        if (is_dashboard) {
            //non editable with no saving
            enableDisableAllFields(false);
        } else {
            //editable
            enableDisableAllFields(true);
        }
    }

    public void initialisation (){

        mContext = this;
        mCommonMethods = new CommonMethods();

        mCommonMethods.setApplicationToolbarMenu1(this, "Agent on Boarding");

        db = new DatabaseHelper(mContext);

        mParseXML = new ParseXML();

        edt_aob_basic_irn = (EditText) findViewById(R.id.edt_aob_basic_irn);

        spnr_aob_basic_enroll_type = (Spinner) findViewById(R.id.spnr_aob_basic_enroll_type);
        ArrayAdapter<String> enrollment_type_adapter = new ArrayAdapter<String>(
                mContext, R.layout.spinner_aob, getResources().getStringArray(R.array.arr_aob_enrollment_type));
        spnr_aob_basic_enroll_type.setAdapter(enrollment_type_adapter);
        enrollment_type_adapter.notifyDataSetChanged();

        chkbox_aob_basic_available_agnt_lst = (CheckBox) findViewById(R.id.chkbox_aob_basic_available_agnt_lst);
        chkbox_aob_basic_available_agnt_blacklst = (CheckBox) findViewById(R.id.chkbox_aob_basic_available_agnt_blacklst);

        btn_aob_basic_back = (Button) findViewById(R.id.btn_aob_basic_back);
        btn_aob_basic_back.setOnClickListener(this);
        btn_aob_basic_next = (Button) findViewById(R.id.btn_aob_basic_next);
        btn_aob_basic_next.setOnClickListener(this);

        //set Data from DB
        ArrayList<PojoAOB> lstRes = db.get_agent_on_boarding_details_by_ID(Activity_AOB_Authentication.row_details);

        if (lstRes.size() > 0) {

            String str_basic_details = lstRes.get(0).getStr_basic_details();
            str_basic_details = str_basic_details == null ? "" : str_basic_details;

            if (!str_basic_details.equals("")){

                String str_irn = mParseXML.parseXmlTag(str_basic_details, "basic_details_irn");
                str_irn = str_irn == null ? "" : str_irn;

                String str_enrollment_type = mParseXML.parseXmlTag(str_basic_details, "basic_details_enrollment_type");

                String str_is_available_agent_list = mParseXML.parseXmlTag(str_basic_details, "basic_details_available_agent_list");

                String str_is_available_agent_blacklist = mParseXML.parseXmlTag(str_basic_details, "basic_details_available_agent_blacklist");

                edt_aob_basic_irn.setText(str_irn);

                spnr_aob_basic_enroll_type.setSelection(Arrays.asList(getResources().
                        getStringArray(R.array.arr_aob_enrollment_type)).indexOf(str_enrollment_type));

                if (str_is_available_agent_list.equals("true"))
                    chkbox_aob_basic_available_agnt_lst.setChecked(true);

                if (str_is_available_agent_blacklist.equals("true"))
                    chkbox_aob_basic_available_agnt_blacklst.setChecked(true);
            }

        }
    }

    @Override
    public void onBackPressed() {
        if (is_back_pressed)
            super.onBackPressed();
    }

    public String validate_all_details(){

        if (spnr_aob_basic_enroll_type.getSelectedItem().toString().equals("Select Enrollment Type")){
            spnr_aob_basic_enroll_type.requestFocus();
            return "Please Select Enrollment Type";
        }else if (!chkbox_aob_basic_available_agnt_lst.isChecked()){
            chkbox_aob_basic_available_agnt_lst.requestFocus();
            return "Please check PAN on IRDAI portal";
        }else if (!chkbox_aob_basic_available_agnt_blacklst.isChecked()){
            chkbox_aob_basic_available_agnt_blacklst.requestFocus();
            return "Please check if the PAN is black listed on the IRDAI portal";
        }else
            return "";

    }

    private void get_basic_details_xml() {

        String str_irn = edt_aob_basic_irn.getText().toString();
        String str_enrollment_type = spnr_aob_basic_enroll_type.getSelectedItem().toString();

        str_basic_details = new StringBuilder();

        //str_basic_details.append("<basic_details>");
        str_basic_details.append("<basic_details_irn>").append(str_irn).append("</basic_details_irn>");
        str_basic_details.append("<basic_details_enrollment_type>").append(str_enrollment_type).append("</basic_details_enrollment_type>");
        str_basic_details.append("<basic_details_available_agent_list>").append("true").append("</basic_details_available_agent_list>");
        str_basic_details.append("<basic_details_available_agent_blacklist>").append("true").append("</basic_details_available_agent_blacklist>");
        //str_basic_details.append("</basic_details>");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btn_aob_basic_back:
                is_back_pressed = true;
                onBackPressed();
                break;

            case R.id.btn_aob_basic_next:

                if (!is_dashboard) {

                    //1. validate all details
                    String str_error = validate_all_details();
                    if (str_error.equals("")) {

                        //2. create xml string for data saving
                        get_basic_details_xml();

                        //3. update data against global row id
                        ContentValues cv = new ContentValues();
                        cv.put(db.AGENT_ON_BOARDING_BASIC_DETAILS, str_basic_details.toString());
                        cv.put(db.AGENT_ON_BOARDING_UPDATED_BY, mCommonMethods.GetUserCode(mContext));

                        Calendar c = Calendar.getInstance();
                        //save date in long
                        cv.put(db.AGENT_ON_BOARDING_UPDATED_DATE, new Date(c.getTimeInMillis()).getTime() + "");
                        cv.put(db.AGENT_ON_BOARDING_SYNCH_STATUS, "2");

                        int i = db.update_agent_on_boarding_details(cv, db.AGENT_ON_BOARDING_ID + " =? ",
                                new String[]{Activity_AOB_Authentication.row_details + ""});

                        Intent mIntent = new Intent(ActivityAOBBasicDetails.this, ActivityAOBPersonalInfo.class);
                        mIntent.putExtra("is_dashboard", is_dashboard);
                        startActivity(mIntent);

                    } else {
                        mCommonMethods.showMessageDialog(mContext, str_error);
                    }

                }else {
                    Intent mIntent = new Intent(ActivityAOBBasicDetails.this, ActivityAOBPersonalInfo.class);
                    mIntent.putExtra("is_dashboard", is_dashboard);
                    startActivity(mIntent);
                }

                break;

            default:
                break;

        }
    }

    public void enableDisableAllFields(boolean is_enable) {
        edt_aob_basic_irn.setEnabled(is_enable);

        spnr_aob_basic_enroll_type.setEnabled(is_enable);

        chkbox_aob_basic_available_agnt_lst.setEnabled(is_enable);
        chkbox_aob_basic_available_agnt_blacklst.setEnabled(is_enable);
    }
}
