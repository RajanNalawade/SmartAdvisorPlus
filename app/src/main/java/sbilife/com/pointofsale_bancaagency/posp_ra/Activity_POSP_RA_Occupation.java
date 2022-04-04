package sbilife.com.pointofsale_bancaagency.posp_ra;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

public class Activity_POSP_RA_Occupation extends AppCompatActivity implements View.OnClickListener,
        AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener {

    private CommonMethods mCommonMethods;
    private Context mContext;
    private DatabaseHelper db;

    private Button btn_aob_occupation_next, btn_aob_occupation_back;

    private final String DATE_EMPLOYMENT_LAST_DATE = "employment_last_date";

    private int mYear, mMonth, mDay;

    private Date currentDate;
    private boolean is_dashboard = false, is_back_pressed = false, is_bsm_questions = false, is_rejection = false;

    private Spinner spnr_aob_occu_applicable, spnr_aob_occu_self_employed, spnr_aob_occu_area_ops, spnr_aob_occu_annual_income,
            spnr_aob_occu_agency, spnr_aob_occu_evr_surrendered, spnr_aob_occu_other_insurer,
            spnr_aob_occu_r_u_promoter, spnr_aob_occu_company_asso_with_sbil, spnr_aob_occu_releted_sbil_employee,
            spnr_aob_occu_aforeside_relative, spnr_aob_occu_sbi_x_employee;

    private TextView txt_aob_occu_employment_last_date;

    private LinearLayout ll_aob_occu_agency_else, ll_aob_occu_other_insurer_cmmnt, ll_aob_occu_r_u_promoter_yes,
            ll_aob_occu_releted_sbil_employee_yes, ll_aob_occu_sbi_x_employee_cmnt;

    private EditText edt_aob_occu_applicable_commnt, edt_aob_occu_self_employed_cmmnt, edt_aob_occu_company_cmmnt,
            edt_aob_occu_evr_surrendered_cmmnt, edt_aob_occu_other_insurer_comapny, edt_aob_occu_other_insurer_comapny_cmmnt,
            edt_aob_occu_r_u_promoter_comapany, edt_aob_occu_r_u_promoter_comapany_pan, edt_aob_occu_r_u_promoter_comapany_tin,
            edt_aob_occu_company_asso_with_sbil_cmmnt, edt_aob_occu_releted_sbil_employee_name, edt_aob_occu_releted_sbil_employee_design,
            edt_aob_occu_releted_sbil_employee_relation, edt_aob_occu_releted_sbil_employee_bank_add, edt_aob_occu_sbi_x_employee_cmmnt,
            edt_aob_occu_employment_cmmnt, edt_aob_occu_company, edt_aob_occu_releted_sbil_employee_insu_off;

    private StringBuilder str_occupational_info;
    private ParseXML mParseXML;
    private DatePickerDialog datePickerDialog;
    private Calendar mCalender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_posp_ra_occupation);
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
    }

    public void initialisation() {

        mContext = this;
        mCommonMethods = new CommonMethods();
        mCommonMethods.setApplicationToolbarMenu1(this, "POSP-RA");

        db = new DatabaseHelper(mContext);

        mParseXML = new ParseXML();

        spnr_aob_occu_applicable = (Spinner) findViewById(R.id.spnr_aob_occu_applicable);
        ArrayAdapter<String> applicable_adapter = new ArrayAdapter<String>(
                mContext, R.layout.spinner_aob, getResources().getStringArray(R.array.arr_aob_occupation_applicable));
        spnr_aob_occu_applicable.setAdapter(applicable_adapter);
        applicable_adapter.notifyDataSetChanged();
        spnr_aob_occu_applicable.setOnItemSelectedListener(this);

        spnr_aob_occu_self_employed = (Spinner) findViewById(R.id.spnr_aob_occu_self_employed);
        ArrayAdapter<String> self_employed_adapter = new ArrayAdapter<String>(
                mContext, R.layout.spinner_aob, getResources().getStringArray(R.array.arr_aob_ans_yes_no));
        spnr_aob_occu_self_employed.setAdapter(self_employed_adapter);
        self_employed_adapter.notifyDataSetChanged();
        spnr_aob_occu_self_employed.setOnItemSelectedListener(this);

        spnr_aob_occu_area_ops = (Spinner) findViewById(R.id.spnr_aob_occu_area_ops);
        ArrayAdapter<String> area_ops_adapter = new ArrayAdapter<String>(
                mContext, R.layout.spinner_aob, getResources().getStringArray(R.array.arr_aob_occu_area_ops));
        spnr_aob_occu_area_ops.setAdapter(area_ops_adapter);
        area_ops_adapter.notifyDataSetChanged();

        spnr_aob_occu_annual_income = (Spinner) findViewById(R.id.spnr_aob_occu_annual_income);
        ArrayAdapter<String> annual_income_adapter = new ArrayAdapter<String>(
                mContext, R.layout.spinner_aob, getResources().getStringArray(R.array.arr_aob_occu_anual_income));
        spnr_aob_occu_annual_income.setAdapter(annual_income_adapter);
        annual_income_adapter.notifyDataSetChanged();

        spnr_aob_occu_agency = (Spinner) findViewById(R.id.spnr_aob_occu_agency);
        ArrayAdapter<String> agency_adapter = new ArrayAdapter<String>(
                mContext, R.layout.spinner_aob, getResources().getStringArray(R.array.arr_posp_ra_occu_agency));
        spnr_aob_occu_agency.setAdapter(agency_adapter);
        agency_adapter.notifyDataSetChanged();
        spnr_aob_occu_agency.setOnItemSelectedListener(this);

        edt_aob_occu_company = (EditText) findViewById(R.id.edt_aob_occu_company);

        edt_aob_occu_releted_sbil_employee_insu_off = (EditText) findViewById(R.id.edt_aob_occu_releted_sbil_employee_insu_off);

        spnr_aob_occu_evr_surrendered = (Spinner) findViewById(R.id.spnr_aob_occu_evr_surrendered);
        ArrayAdapter<String> evr_surrendered_adapter = new ArrayAdapter<String>(
                mContext, R.layout.spinner_aob, getResources().getStringArray(R.array.arr_aob_ans_yes_no));
        spnr_aob_occu_evr_surrendered.setAdapter(evr_surrendered_adapter);
        evr_surrendered_adapter.notifyDataSetChanged();
        spnr_aob_occu_evr_surrendered.setOnItemSelectedListener(this);

        spnr_aob_occu_other_insurer = (Spinner) findViewById(R.id.spnr_aob_occu_other_insurer);
        ArrayAdapter<String> other_insurer_adapter = new ArrayAdapter<String>(
                mContext, R.layout.spinner_aob, getResources().getStringArray(R.array.arr_aob_ans_yes_no));
        spnr_aob_occu_other_insurer.setAdapter(other_insurer_adapter);
        other_insurer_adapter.notifyDataSetChanged();
        spnr_aob_occu_other_insurer.setOnItemSelectedListener(this);

        spnr_aob_occu_r_u_promoter = (Spinner) findViewById(R.id.spnr_aob_occu_r_u_promoter);
        ArrayAdapter<String> r_u_promoter_adapter = new ArrayAdapter<String>(
                mContext, R.layout.spinner_aob, getResources().getStringArray(R.array.arr_aob_ans_yes_no));
        spnr_aob_occu_r_u_promoter.setAdapter(r_u_promoter_adapter);
        r_u_promoter_adapter.notifyDataSetChanged();
        spnr_aob_occu_r_u_promoter.setOnItemSelectedListener(this);

        spnr_aob_occu_company_asso_with_sbil = (Spinner) findViewById(R.id.spnr_aob_occu_company_asso_with_sbil);
        ArrayAdapter<String> company_asso_with_sbil_adapter = new ArrayAdapter<String>(
                mContext, R.layout.spinner_aob, getResources().getStringArray(R.array.arr_aob_ans_yes_no));
        spnr_aob_occu_company_asso_with_sbil.setAdapter(company_asso_with_sbil_adapter);
        company_asso_with_sbil_adapter.notifyDataSetChanged();
        spnr_aob_occu_company_asso_with_sbil.setOnItemSelectedListener(this);

        spnr_aob_occu_releted_sbil_employee = (Spinner) findViewById(R.id.spnr_aob_occu_releted_sbil_employee);
        ArrayAdapter<String> releted_sbil_employee_adapter = new ArrayAdapter<String>(
                mContext, R.layout.spinner_aob, getResources().getStringArray(R.array.arr_aob_ans_yes_no));
        spnr_aob_occu_releted_sbil_employee.setAdapter(releted_sbil_employee_adapter);
        releted_sbil_employee_adapter.notifyDataSetChanged();
        spnr_aob_occu_releted_sbil_employee.setOnItemSelectedListener(this);

        spnr_aob_occu_aforeside_relative = (Spinner) findViewById(R.id.spnr_aob_occu_aforeside_relative);
        ArrayAdapter<String> aforeside_relative_adapter = new ArrayAdapter<String>(
                mContext, R.layout.spinner_aob, getResources().getStringArray(R.array.arr_aob_ans_yes_no));
        spnr_aob_occu_aforeside_relative.setAdapter(aforeside_relative_adapter);
        aforeside_relative_adapter.notifyDataSetChanged();
        spnr_aob_occu_aforeside_relative.setOnItemSelectedListener(this);

        spnr_aob_occu_sbi_x_employee = (Spinner) findViewById(R.id.spnr_aob_occu_sbi_x_employee);
        ArrayAdapter<String> sbi_x_employee_adapter = new ArrayAdapter<String>(
                mContext, R.layout.spinner_aob, getResources().getStringArray(R.array.arr_aob_ans_yes_no));
        spnr_aob_occu_sbi_x_employee.setAdapter(sbi_x_employee_adapter);
        sbi_x_employee_adapter.notifyDataSetChanged();
        spnr_aob_occu_sbi_x_employee.setOnItemSelectedListener(this);

        ll_aob_occu_agency_else = (LinearLayout) findViewById(R.id.ll_aob_occu_agency_else);
        ll_aob_occu_other_insurer_cmmnt = (LinearLayout) findViewById(R.id.ll_aob_occu_other_insurer_cmmnt);
        ll_aob_occu_r_u_promoter_yes = (LinearLayout) findViewById(R.id.ll_aob_occu_r_u_promoter_yes);
        ll_aob_occu_releted_sbil_employee_yes = (LinearLayout) findViewById(R.id.ll_aob_occu_releted_sbil_employee_yes);
        ll_aob_occu_sbi_x_employee_cmnt = (LinearLayout) findViewById(R.id.ll_aob_occu_sbi_x_employee_cmnt);

        edt_aob_occu_applicable_commnt = (EditText) findViewById(R.id.edt_aob_occu_applicable_commnt);
        edt_aob_occu_self_employed_cmmnt = (EditText) findViewById(R.id.edt_aob_occu_self_employed_cmmnt);
        edt_aob_occu_company_cmmnt = (EditText) findViewById(R.id.edt_aob_occu_company_cmmnt);
        edt_aob_occu_evr_surrendered_cmmnt = (EditText) findViewById(R.id.edt_aob_occu_evr_surrendered_cmmnt);
        edt_aob_occu_other_insurer_comapny = (EditText) findViewById(R.id.edt_aob_occu_other_insurer_comapny);
        edt_aob_occu_other_insurer_comapny_cmmnt = (EditText) findViewById(R.id.edt_aob_occu_other_insurer_comapny_cmmnt);
        edt_aob_occu_r_u_promoter_comapany = (EditText) findViewById(R.id.edt_aob_occu_r_u_promoter_comapany);
        edt_aob_occu_r_u_promoter_comapany_pan = (EditText) findViewById(R.id.edt_aob_occu_r_u_promoter_comapany_pan);
        edt_aob_occu_r_u_promoter_comapany_tin = (EditText) findViewById(R.id.edt_aob_occu_r_u_promoter_comapany_tin);
        edt_aob_occu_company_asso_with_sbil_cmmnt = (EditText) findViewById(R.id.edt_aob_occu_company_asso_with_sbil_cmmnt);
        edt_aob_occu_releted_sbil_employee_name = (EditText) findViewById(R.id.edt_aob_occu_releted_sbil_employee_name);
        edt_aob_occu_releted_sbil_employee_design = (EditText) findViewById(R.id.edt_aob_occu_releted_sbil_employee_design);
        edt_aob_occu_releted_sbil_employee_relation = (EditText) findViewById(R.id.edt_aob_occu_releted_sbil_employee_relation);
        edt_aob_occu_releted_sbil_employee_bank_add = (EditText) findViewById(R.id.edt_aob_occu_releted_sbil_employee_bank_add);
        edt_aob_occu_sbi_x_employee_cmmnt = (EditText) findViewById(R.id.edt_aob_occu_sbi_x_employee_cmmnt);
        edt_aob_occu_employment_cmmnt = (EditText) findViewById(R.id.edt_aob_occu_employment_cmmnt);

        txt_aob_occu_employment_last_date = (TextView) findViewById(R.id.txt_aob_occu_employment_last_date);
        txt_aob_occu_employment_last_date.setOnClickListener(this);

        btn_aob_occupation_next = (Button) findViewById(R.id.btn_aob_occupation_next);
        btn_aob_occupation_next.setOnClickListener(this);
        btn_aob_occupation_back = (Button) findViewById(R.id.btn_aob_occupation_back);
        btn_aob_occupation_back.setOnClickListener(this);

        mCalender = Calendar.getInstance();
        mYear = mCalender.get(Calendar.YEAR);
        mMonth = mCalender.get(Calendar.MONTH);
        mDay = mCalender.get(Calendar.DAY_OF_MONTH);

        currentDate = new Date(mCalender.getTimeInMillis());

        str_occupational_info = new StringBuilder();

        //set Data from DB
        ArrayList<Pojo_POSP_RA> lstRes = db.get_posp_ra_details_by_ID(Activity_POSP_RA_Authentication.row_details);

        if (lstRes.size() > 0) {

            String str_occupational_info = lstRes.get(0).getStr_occupation_info();
            str_occupational_info = str_occupational_info == null ? "" : str_occupational_info;

            if (!str_occupational_info.equals("")) {

                String str_applicable_occupation = mParseXML.parseXmlTag(str_occupational_info, "occupatinal_info_applicable");
                String str_applicable_occupation_cmnt = mParseXML.parseXmlTag(str_occupational_info, "occupatinal_info_applicable_cmnt");

                String str_self_employed = mParseXML.parseXmlTag(str_occupational_info, "occupatinal_info_self_emp");
                String str_self_employed_cmnt = mParseXML.parseXmlTag(str_occupational_info, "occupatinal_info_self_emp_cmnt");

                String str_area_ops = mParseXML.parseXmlTag(str_occupational_info, "occupatinal_info_area_ops");
                String str_annual_income = mParseXML.parseXmlTag(str_occupational_info, "occupatinal_info_annual_income");

                String str_occu_agency = mParseXML.parseXmlTag(str_occupational_info, "occupatinal_info_occu_agency");

                String str_occu_company = mParseXML.parseXmlTag(str_occupational_info, "occupatinal_info_occu_company");
                String str_occu_company_cmnt = mParseXML.parseXmlTag(str_occupational_info, "occupatinal_info_occu_company_cmnt");

                String str_occu_evr_surrendered = mParseXML.parseXmlTag(str_occupational_info, "occupatinal_info_evr_surrendered");
                String str_occu_evr_surrendered_cmnt = mParseXML.parseXmlTag(str_occupational_info, "occupatinal_info_evr_surrendered_cmnt");

                String str_other_insurer = mParseXML.parseXmlTag(str_occupational_info, "occupatinal_info_other_insurer");
                String str_other_insurer_company = mParseXML.parseXmlTag(str_occupational_info, "occupatinal_info_other_insurer_company");
                String str_other_insurer_company_cmnt = mParseXML.parseXmlTag(str_occupational_info, "occupatinal_info_other_insurer_company_cmnt");

                String str_r_u_promoter = mParseXML.parseXmlTag(str_occupational_info, "occupatinal_info_r_u_promoter");
                String str_r_u_promoter_company = mParseXML.parseXmlTag(str_occupational_info, "occupatinal_info_r_u_promoter_company");
                String str_r_u_promoter_company_pan = mParseXML.parseXmlTag(str_occupational_info, "occupatinal_info_r_u_promoter_company_pan");
                String str_r_u_promoter_company_tin = mParseXML.parseXmlTag(str_occupational_info, "occupatinal_info_r_u_promoter_company_tin");

                String str_asso_with_sbil = mParseXML.parseXmlTag(str_occupational_info, "occupatinal_info_asso_with_sbil");
                String str_asso_with_sbil_cmnt = mParseXML.parseXmlTag(str_occupational_info, "occupatinal_info_asso_with_sbil_cmnt");

                String str_related_sbil_emp = mParseXML.parseXmlTag(str_occupational_info, "occupatinal_info_related_sbil_emp");
                String str_related_sbil_emp_name = mParseXML.parseXmlTag(str_occupational_info, "occupatinal_info_related_sbil_emp_name");
                String str_related_sbil_emp_designation = mParseXML.parseXmlTag(str_occupational_info, "occupatinal_info_related_sbil_emp_designation");
                String str_related_sbil_emp_relation = mParseXML.parseXmlTag(str_occupational_info, "occupatinal_info_related_sbil_emp_relation");

                String str_aforeside_relative = mParseXML.parseXmlTag(str_occupational_info, "occupatinal_info_aforeside_relative");
                String str_related_sbil_bank_address = mParseXML.parseXmlTag(str_occupational_info, "occupatinal_info_related_sbil_bank_address");

                String str_sbi_x_emp = mParseXML.parseXmlTag(str_occupational_info, "occupatinal_info_sbi_x_emp");
                String str_sbi_x_emp_cmnt = mParseXML.parseXmlTag(str_occupational_info, "occupatinal_info_sbi_x_emp_cmnt");
                String str_emp_last_date = mParseXML.parseXmlTag(str_occupational_info, "occupatinal_info_emp_last_date");
                String str_emp_cmnt = mParseXML.parseXmlTag(str_occupational_info, "occupatinal_info_emp_cmnt");

                spnr_aob_occu_applicable.setSelection(Arrays.asList(getResources().
                        getStringArray(R.array.arr_aob_occupation_applicable)).indexOf(str_applicable_occupation));
                if (str_applicable_occupation.equals("Others")) {
                    edt_aob_occu_applicable_commnt.setText(str_applicable_occupation_cmnt);
                    edt_aob_occu_applicable_commnt.setVisibility(View.VISIBLE);
                } else
                    edt_aob_occu_applicable_commnt.setVisibility(View.GONE);

                spnr_aob_occu_self_employed.setSelection(Arrays.asList(getResources().
                        getStringArray(R.array.arr_aob_ans_yes_no)).indexOf(str_self_employed));
                if (str_self_employed.equals("Yes")) {
                    edt_aob_occu_self_employed_cmmnt.setVisibility(View.VISIBLE);
                    edt_aob_occu_self_employed_cmmnt.setText(str_self_employed_cmnt);
                } else
                    edt_aob_occu_self_employed_cmmnt.setVisibility(View.GONE);

                spnr_aob_occu_area_ops.setSelection(Arrays.asList(getResources().
                        getStringArray(R.array.arr_aob_occu_area_ops)).indexOf(str_area_ops));

                spnr_aob_occu_annual_income.setSelection(Arrays.asList(getResources().
                        getStringArray(R.array.arr_aob_occu_anual_income)).indexOf(str_annual_income));

                spnr_aob_occu_agency.setSelection(Arrays.asList(getResources().
                        getStringArray(R.array.arr_posp_ra_occu_agency)).indexOf(str_occu_agency));
                if (str_occu_agency.equals("None")) {
                    ll_aob_occu_agency_else.setVisibility(View.GONE);
                } else if (str_occu_agency.equals("Point of Sales Person")) {
                    ll_aob_occu_agency_else.setVisibility(View.VISIBLE);

                    edt_aob_occu_company.setText(str_occu_company);
                    edt_aob_occu_company_cmmnt.setText(str_occu_company_cmnt);
                } else {
                    ll_aob_occu_agency_else.setVisibility(View.GONE);
                }

                spnr_aob_occu_evr_surrendered.setSelection(Arrays.asList(getResources().
                        getStringArray(R.array.arr_aob_ans_yes_no)).indexOf(str_occu_evr_surrendered));
                if (str_occu_evr_surrendered.equals("Yes")) {
                    edt_aob_occu_evr_surrendered_cmmnt.setVisibility(View.VISIBLE);
                    edt_aob_occu_evr_surrendered_cmmnt.setText(str_occu_evr_surrendered_cmnt);
                } else
                    edt_aob_occu_evr_surrendered_cmmnt.setVisibility(View.GONE);


                spnr_aob_occu_other_insurer.setSelection(Arrays.asList(getResources().
                        getStringArray(R.array.arr_aob_ans_yes_no)).indexOf(str_other_insurer));
                if (str_other_insurer.equals("Yes")) {

                    ll_aob_occu_other_insurer_cmmnt.setVisibility(View.VISIBLE);

                    edt_aob_occu_other_insurer_comapny.setText(str_other_insurer_company);
                    edt_aob_occu_other_insurer_comapny_cmmnt.setText(str_other_insurer_company_cmnt);

                } else {
                    ll_aob_occu_other_insurer_cmmnt.setVisibility(View.GONE);
                }

                spnr_aob_occu_r_u_promoter.setSelection(Arrays.asList(getResources().
                        getStringArray(R.array.arr_aob_ans_yes_no)).indexOf(str_r_u_promoter));
                if (str_r_u_promoter.equals("Yes")) {

                    ll_aob_occu_r_u_promoter_yes.setVisibility(View.VISIBLE);

                    edt_aob_occu_r_u_promoter_comapany.setText(str_r_u_promoter_company);
                    edt_aob_occu_r_u_promoter_comapany_pan.setText(str_r_u_promoter_company_pan);
                    edt_aob_occu_r_u_promoter_comapany_tin.setText(str_r_u_promoter_company_tin);

                } else {
                    ll_aob_occu_r_u_promoter_yes.setVisibility(View.GONE);
                }

                spnr_aob_occu_company_asso_with_sbil.setSelection(Arrays.asList(getResources().
                        getStringArray(R.array.arr_aob_ans_yes_no)).indexOf(str_asso_with_sbil));
                if (str_asso_with_sbil.equals("Yes")) {
                    edt_aob_occu_company_asso_with_sbil_cmmnt.setVisibility(View.VISIBLE);
                    edt_aob_occu_company_asso_with_sbil_cmmnt.setText(str_asso_with_sbil_cmnt);
                } else {
                    edt_aob_occu_company_asso_with_sbil_cmmnt.setVisibility(View.GONE);
                }

                spnr_aob_occu_releted_sbil_employee.setSelection(Arrays.asList(getResources().
                        getStringArray(R.array.arr_aob_ans_yes_no)).indexOf(str_related_sbil_emp));
                if (str_related_sbil_emp.equals("Yes")) {

                    ll_aob_occu_releted_sbil_employee_yes.setVisibility(View.VISIBLE);

                    edt_aob_occu_releted_sbil_employee_name.setText(str_related_sbil_emp_name);
                    edt_aob_occu_releted_sbil_employee_design.setText(str_related_sbil_emp_designation);
                    edt_aob_occu_releted_sbil_employee_relation.setText(str_related_sbil_emp_relation);
                } else {
                    ll_aob_occu_releted_sbil_employee_yes.setVisibility(View.GONE);
                }

                spnr_aob_occu_aforeside_relative.setSelection(Arrays.asList(getResources().
                        getStringArray(R.array.arr_aob_ans_yes_no)).indexOf(str_aforeside_relative));
                if (str_aforeside_relative.equals("Yes")) {
                    edt_aob_occu_releted_sbil_employee_bank_add.setVisibility(View.VISIBLE);
                    edt_aob_occu_releted_sbil_employee_bank_add.setText(str_related_sbil_bank_address);
                } else {
                    edt_aob_occu_releted_sbil_employee_bank_add.setVisibility(View.GONE);
                }

                spnr_aob_occu_sbi_x_employee.setSelection(Arrays.asList(getResources().
                        getStringArray(R.array.arr_aob_ans_yes_no)).indexOf(str_sbi_x_emp));
                if (str_sbi_x_emp.equals("Yes")) {
                    ll_aob_occu_sbi_x_employee_cmnt.setVisibility(View.VISIBLE);

                    edt_aob_occu_sbi_x_employee_cmmnt.setText(str_sbi_x_emp_cmnt);

                    if (!str_emp_last_date.equals("")) {
                        String[] arrDate = str_emp_last_date.split("-");

                        txt_aob_occu_employment_last_date.setText(arrDate[1] + "-" + arrDate[0] + "-" + arrDate[2]);
                    } else {
                        txt_aob_occu_employment_last_date.setText("");
                    }

                    edt_aob_occu_employment_cmmnt.setText(str_emp_cmnt);
                } else {
                    ll_aob_occu_sbi_x_employee_cmnt.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {
            case R.id.spnr_aob_occu_applicable:

                spnr_aob_occu_self_employed.setEnabled(false);

                if (position == 1)
                    spnr_aob_occu_self_employed.setSelection(0);
                else
                    spnr_aob_occu_self_employed.setSelection(1);
                break;

            case R.id.spnr_aob_occu_self_employed:
                if (position == 0) {
                    edt_aob_occu_self_employed_cmmnt.setVisibility(View.VISIBLE);
                } else {
                    edt_aob_occu_self_employed_cmmnt.setVisibility(View.GONE);
                }
                break;

            case R.id.spnr_aob_occu_agency:

                if (position == 1 || position == 0) {
                    ll_aob_occu_agency_else.setVisibility(View.GONE);
                    edt_aob_occu_company.setText("");
                    edt_aob_occu_company_cmmnt.setText("");
                } else if (position == 6) {
                    ll_aob_occu_agency_else.setVisibility(View.VISIBLE);
                } else {
                    ll_aob_occu_agency_else.setVisibility(View.GONE);
                    edt_aob_occu_company.setText("");
                    edt_aob_occu_company_cmmnt.setText("");
                    mCommonMethods.showMessageDialog(mContext, "For other agency types please proceed through CMS");
                }
                break;

            case R.id.spnr_aob_occu_evr_surrendered:

                if (position == 0) {
                    edt_aob_occu_evr_surrendered_cmmnt.setVisibility(View.VISIBLE);
                } else {
                    edt_aob_occu_evr_surrendered_cmmnt.setVisibility(View.GONE);
                    edt_aob_occu_evr_surrendered_cmmnt.setText("");
                }
                break;

            case R.id.spnr_aob_occu_other_insurer:

                if (position == 0) {
                    ll_aob_occu_other_insurer_cmmnt.setVisibility(View.VISIBLE);
                } else {
                    ll_aob_occu_other_insurer_cmmnt.setVisibility(View.GONE);
                    edt_aob_occu_other_insurer_comapny.setText("");
                    edt_aob_occu_other_insurer_comapny_cmmnt.setText("");
                }
                break;

            case R.id.spnr_aob_occu_r_u_promoter:

                if (position == 0) {
                    ll_aob_occu_r_u_promoter_yes.setVisibility(View.VISIBLE);
                } else {
                    ll_aob_occu_r_u_promoter_yes.setVisibility(View.GONE);

                    edt_aob_occu_r_u_promoter_comapany.setText("");
                    edt_aob_occu_r_u_promoter_comapany_pan.setText("");
                    edt_aob_occu_r_u_promoter_comapany_tin.setText("");
                }
                break;

            case R.id.spnr_aob_occu_company_asso_with_sbil:

                if (position == 0) {
                    edt_aob_occu_company_asso_with_sbil_cmmnt.setVisibility(View.VISIBLE);
                } else {
                    edt_aob_occu_company_asso_with_sbil_cmmnt.setVisibility(View.GONE);
                    edt_aob_occu_company_asso_with_sbil_cmmnt.setText("");
                }
                break;

            case R.id.spnr_aob_occu_releted_sbil_employee:

                if (position == 0) {
                    ll_aob_occu_releted_sbil_employee_yes.setVisibility(View.VISIBLE);
                } else {
                    ll_aob_occu_releted_sbil_employee_yes.setVisibility(View.GONE);
                    edt_aob_occu_releted_sbil_employee_name.setText("");
                    edt_aob_occu_releted_sbil_employee_design.setText("");
                    edt_aob_occu_releted_sbil_employee_relation.setText("");
                    edt_aob_occu_releted_sbil_employee_insu_off.setText("");
                }
                break;

            case R.id.spnr_aob_occu_aforeside_relative:

                if (position == 0) {
                    edt_aob_occu_releted_sbil_employee_bank_add.setVisibility(View.VISIBLE);
                } else {
                    edt_aob_occu_releted_sbil_employee_bank_add.setText("");
                    edt_aob_occu_releted_sbil_employee_bank_add.setVisibility(View.GONE);
                }

                break;

            case R.id.spnr_aob_occu_sbi_x_employee:

                if (position == 0) {
                    ll_aob_occu_sbi_x_employee_cmnt.setVisibility(View.VISIBLE);
                } else {
                    ll_aob_occu_sbi_x_employee_cmnt.setVisibility(View.GONE);
                    edt_aob_occu_sbi_x_employee_cmmnt.setText("");
                    edt_aob_occu_employment_cmmnt.setText("");
                }
                break;

            default:
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Activity_POSP_RA_Occupation.this, Activity_POSP_RA_PersonalInfo.class);
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

            case R.id.btn_aob_occupation_back:
                is_back_pressed = true;
                onBackPressed();
                break;

            case R.id.btn_aob_occupation_next:

                if (!is_dashboard && !is_bsm_questions) {
                    //1. validate all details
                    String str_error = validate_all_details();
                    if (str_error.equals("")) {

                        //2. create xml string for data saving
                        get_occupational_info_xml();

                        //3. update data against global row id
                        ContentValues cv = new ContentValues();
                        cv.put(db.POSP_RA_OCCUPATION_INFO, str_occupational_info.toString());
                        cv.put(db.POSP_RA_UPDATED_BY, mCommonMethods.GetUserCode(mContext));

                        Calendar c = Calendar.getInstance();
                        //save date in long
                        cv.put(db.POSP_RA_UPDATED_DATE, new Date(c.getTimeInMillis()).getTime() + "");
                        cv.put(db.POSP_RA_IN_APP_STATUS, "3");
                        cv.put(db.POSP_RA_IN_APP_STATUS_REMARK, "Nominee Info Pending");

                        int i = db.update_POSP_RA_details(cv, db.POSP_RA_ID + " =? ",
                                new String[]{Activity_POSP_RA_Authentication.row_details + ""});

                        mCommonMethods.showToast(mContext, "Details saved Successfully : " + i);

                        Intent mIntent = new Intent(Activity_POSP_RA_Occupation.this, Activity_POSP_RA_Nomination.class);
                        if (is_rejection)
                            mIntent.putExtra("is_rejection", is_rejection);

                        startActivity(mIntent);
                    } else {
                        mCommonMethods.showMessageDialog(mContext, str_error);
                    }
                } else if (is_dashboard) {
                    Intent mIntent = new Intent(Activity_POSP_RA_Occupation.this, Activity_POSP_RA_Nomination.class);
                    mIntent.putExtra("is_dashboard", is_dashboard);
                    startActivity(mIntent);
                } else if (is_bsm_questions) {
                    Intent mIntent = new Intent(Activity_POSP_RA_Occupation.this, Activity_POSP_RA_Nomination.class);
                    mIntent.putExtra("is_bsm_questions", is_bsm_questions);
                    startActivity(mIntent);
                }
                break;

            case R.id.txt_aob_occu_employment_last_date:

                datePickerDialog = DatePickerDialog.newInstance(Activity_POSP_RA_Occupation.this, mYear, mMonth, mDay);
                datePickerDialog.setThemeDark(false);
                datePickerDialog.showYearPickerFirst(false);
                //future date not allowed
                datePickerDialog.setMaxDate(mCalender);

                datePickerDialog.dismissOnPause(true); //dismiss dialog when onPause() called?
                datePickerDialog.setAccentColor(getResources().getColor(R.color.Common_blue));
                datePickerDialog.setTitle("Last Employment Date");

                datePickerDialog.show(getFragmentManager(), DATE_EMPLOYMENT_LAST_DATE);
                break;

            default:
                break;

        }
    }

    private void get_occupational_info_xml() {

        String str_applicable_occupation = spnr_aob_occu_applicable.getSelectedItem().toString();
        String str_applicable_occupation_cmnt = edt_aob_occu_applicable_commnt.getText().toString();

        String str_self_employed = spnr_aob_occu_self_employed.getSelectedItem().toString();
        String str_self_employed_cmnt = edt_aob_occu_self_employed_cmmnt.getText().toString();

        String str_area_ops = spnr_aob_occu_area_ops.getSelectedItem().toString();
        String str_annual_income = spnr_aob_occu_annual_income.getSelectedItem().toString();

        if (str_annual_income.equals("<1,00,000")) {
            str_annual_income = "&lt;1,00,000";
        } else if (str_annual_income.equals(">3,00,000")) {
            str_annual_income = "&gt;3,00,000";
        }

        String str_occu_agency = spnr_aob_occu_agency.getSelectedItem().toString();

        String str_occu_company = edt_aob_occu_company.getText().toString();
        String str_occu_company_cmnt = edt_aob_occu_company_cmmnt.getText().toString();

        String str_occu_evr_surrendered = spnr_aob_occu_evr_surrendered.getSelectedItem().toString();
        String str_occu_evr_surrendered_cmnt = edt_aob_occu_evr_surrendered_cmmnt.getText().toString();

        String str_other_insurer = spnr_aob_occu_other_insurer.getSelectedItem().toString();
        String str_other_insurer_company = edt_aob_occu_other_insurer_comapny.getText().toString();
        String str_other_insurer_company_cmnt = edt_aob_occu_other_insurer_comapny_cmmnt.getText().toString();

        String str_r_u_promoter = spnr_aob_occu_r_u_promoter.getSelectedItem().toString();
        String str_r_u_promoter_company = edt_aob_occu_r_u_promoter_comapany.getText().toString();
        String str_r_u_promoter_company_pan = edt_aob_occu_r_u_promoter_comapany_pan.getText().toString();
        String str_r_u_promoter_company_tin = edt_aob_occu_r_u_promoter_comapany_tin.getText().toString();

        String str_asso_with_sbil = spnr_aob_occu_company_asso_with_sbil.getSelectedItem().toString();
        String str_asso_with_sbil_cmnt = edt_aob_occu_company_asso_with_sbil_cmmnt.getText().toString();

        String str_related_sbil_emp = spnr_aob_occu_releted_sbil_employee.getSelectedItem().toString();
        String str_related_sbil_emp_name = edt_aob_occu_releted_sbil_employee_name.getText().toString();
        String str_related_sbil_emp_designation = edt_aob_occu_releted_sbil_employee_design.getText().toString();
        String str_related_sbil_emp_relation = edt_aob_occu_releted_sbil_employee_relation.getText().toString();
        String str_related_sbil_emp_insu_off = edt_aob_occu_releted_sbil_employee_insu_off.getText().toString();

        String str_aforeside_relative = spnr_aob_occu_aforeside_relative.getSelectedItem().toString();
        String str_related_sbil_bank_address = edt_aob_occu_releted_sbil_employee_bank_add.getText().toString();

        String str_sbi_x_emp = spnr_aob_occu_sbi_x_employee.getSelectedItem().toString();
        String str_sbi_x_emp_cmnt = edt_aob_occu_sbi_x_employee_cmmnt.getText().toString();

        String str_emp_last_date = txt_aob_occu_employment_last_date.getText().toString();
        String str_emp_cmnt = edt_aob_occu_employment_cmmnt.getText().toString();

        str_occupational_info = new StringBuilder();

        //str_occupational_info.append("<occupational_info>");
        str_occupational_info.append("<occupatinal_info_applicable>").append(str_applicable_occupation).append("</occupatinal_info_applicable>");
        str_occupational_info.append("<occupatinal_info_applicable_cmnt>").append(str_applicable_occupation_cmnt).append("</occupatinal_info_applicable_cmnt>");
        str_occupational_info.append("<occupatinal_info_self_emp>").append(str_self_employed).append("</occupatinal_info_self_emp>");
        str_occupational_info.append("<occupatinal_info_self_emp_cmnt>").append(str_self_employed_cmnt).append("</occupatinal_info_self_emp_cmnt>");
        str_occupational_info.append("<occupatinal_info_area_ops>").append(str_area_ops).append("</occupatinal_info_area_ops>");
        str_occupational_info.append("<occupatinal_info_annual_income>").append(str_annual_income).append("</occupatinal_info_annual_income>");
        str_occupational_info.append("<occupatinal_info_occu_agency>").append(str_occu_agency).append("</occupatinal_info_occu_agency>");
        str_occupational_info.append("<occupatinal_info_occu_company>").append(str_occu_company).append("</occupatinal_info_occu_company>");
        str_occupational_info.append("<occupatinal_info_occu_company_cmnt>").append(str_occu_company_cmnt).append("</occupatinal_info_occu_company_cmnt>");
        str_occupational_info.append("<occupatinal_info_evr_surrendered>").append(str_occu_evr_surrendered).append("</occupatinal_info_evr_surrendered>");
        str_occupational_info.append("<occupatinal_info_evr_surrendered_cmnt>").append(str_occu_evr_surrendered_cmnt).append("</occupatinal_info_evr_surrendered_cmnt>");
        str_occupational_info.append("<occupatinal_info_other_insurer>").append(str_other_insurer).append("</occupatinal_info_other_insurer>");
        str_occupational_info.append("<occupatinal_info_other_insurer_company>").append(str_other_insurer_company).append("</occupatinal_info_other_insurer_company>");
        str_occupational_info.append("<occupatinal_info_other_insurer_company_cmnt>").append(str_other_insurer_company_cmnt).append("</occupatinal_info_other_insurer_company_cmnt>");
        str_occupational_info.append("<occupatinal_info_r_u_promoter>").append(str_r_u_promoter).append("</occupatinal_info_r_u_promoter>");
        str_occupational_info.append("<occupatinal_info_r_u_promoter_company>").append(str_r_u_promoter_company).append("</occupatinal_info_r_u_promoter_company>");
        str_occupational_info.append("<occupatinal_info_r_u_promoter_company_pan>").append(str_r_u_promoter_company_pan).append("</occupatinal_info_r_u_promoter_company_pan>");
        str_occupational_info.append("<occupatinal_info_r_u_promoter_company_tin>").append(str_r_u_promoter_company_tin).append("</occupatinal_info_r_u_promoter_company_tin>");
        str_occupational_info.append("<occupatinal_info_asso_with_sbil>").append(str_asso_with_sbil).append("</occupatinal_info_asso_with_sbil>");
        str_occupational_info.append("<occupatinal_info_asso_with_sbil_cmnt>").append(str_asso_with_sbil_cmnt).append("</occupatinal_info_asso_with_sbil_cmnt>");
        str_occupational_info.append("<occupatinal_info_related_sbil_emp>").append(str_related_sbil_emp).append("</occupatinal_info_related_sbil_emp>");
        str_occupational_info.append("<occupatinal_info_related_sbil_emp_name>").append(str_related_sbil_emp_name).append("</occupatinal_info_related_sbil_emp_name>");
        str_occupational_info.append("<occupatinal_info_related_sbil_emp_designation>").append(str_related_sbil_emp_designation).append("</occupatinal_info_related_sbil_emp_designation>");
        str_occupational_info.append("<occupatinal_info_related_sbil_emp_relation>").append(str_related_sbil_emp_relation).append("</occupatinal_info_related_sbil_emp_relation>");
        str_occupational_info.append("<occupatinal_info_related_sbil_emp_insu_off>").append(str_related_sbil_emp_insu_off).append("</occupatinal_info_related_sbil_emp_insu_off>");
        str_occupational_info.append("<occupatinal_info_aforeside_relative>").append(str_aforeside_relative).append("</occupatinal_info_aforeside_relative>");
        str_occupational_info.append("<occupatinal_info_related_sbil_bank_address>").append(str_related_sbil_bank_address).append("</occupatinal_info_related_sbil_bank_address>");
        str_occupational_info.append("<occupatinal_info_sbi_x_emp>").append(str_sbi_x_emp).append("</occupatinal_info_sbi_x_emp>");
        str_occupational_info.append("<occupatinal_info_sbi_x_emp_cmnt>").append(str_sbi_x_emp_cmnt).append("</occupatinal_info_sbi_x_emp_cmnt>");

        if (!str_emp_last_date.equals("")) {
            String[] arrDate = str_emp_last_date.split("-");
            str_occupational_info.append("<occupatinal_info_emp_last_date>").append(arrDate[1] + "-" + arrDate[0] + "-" + arrDate[2]).append("</occupatinal_info_emp_last_date>");
        } else {
            str_occupational_info.append("<occupatinal_info_emp_last_date></occupatinal_info_emp_last_date>");
        }
        str_occupational_info.append("<occupatinal_info_emp_cmnt>").append(str_emp_cmnt).append("</occupatinal_info_emp_cmnt>");
        //str_occupational_info.append("</occupational_info>");

    }

    public String validate_all_details() {

        if (spnr_aob_occu_applicable.getSelectedItem().toString().equals("Select Occupation")) {
            spnr_aob_occu_applicable.requestFocus();
            return "Please select applicant occupation";
        }/* else if (spnr_aob_occu_applicable.getSelectedItem().toString().equals("Others") &
                edt_aob_occu_applicable_commnt.getText().toString().equals("")) {
            edt_aob_occu_applicable_commnt.requestFocus();
            return "Please enter comment";
        }*/ else if (spnr_aob_occu_self_employed.getSelectedItem().toString().equals("Yes") &
                edt_aob_occu_self_employed_cmmnt.getText().toString().equals("")) {
            edt_aob_occu_self_employed_cmmnt.requestFocus();
            return "Please enter comment";
        } else if (spnr_aob_occu_area_ops.getSelectedItem().toString().equals("Select Area of Operation")) {
            spnr_aob_occu_area_ops.requestFocus();
            return "Please select area of operation";
        } else if (spnr_aob_occu_agency.getSelectedItem().toString().equals("Select Agency")) {
            spnr_aob_occu_agency.requestFocus();
            return "Please select agency";
        } else if (!spnr_aob_occu_agency.getSelectedItem().toString().equals("None") &
                edt_aob_occu_company.getText().toString().equals("")) {

            if (spnr_aob_occu_agency.getSelectedItemPosition() == 6) {
                edt_aob_occu_company.requestFocus();
                return "Please Enter Name of Insurance Company";
            } else {
                return "For other agency types please proceed through CMS";
            }

        } else if (spnr_aob_occu_evr_surrendered.getSelectedItem().toString().equals("Yes") &
                edt_aob_occu_evr_surrendered_cmmnt.getText().toString().equals("")) {
            edt_aob_occu_evr_surrendered_cmmnt.requestFocus();
            return "Please enter comment";
        } else if (spnr_aob_occu_other_insurer.getSelectedItem().toString().equals("Yes") &
                edt_aob_occu_other_insurer_comapny.getText().toString().equals("")) {
            edt_aob_occu_other_insurer_comapny.requestFocus();
            return "Please enter company name";
        } else if (spnr_aob_occu_other_insurer.getSelectedItem().toString().equals("Yes") &
                edt_aob_occu_other_insurer_comapny_cmmnt.getText().toString().equals("")) {
            edt_aob_occu_other_insurer_comapny_cmmnt.requestFocus();
            return "Please enter comments";
        } else if (spnr_aob_occu_r_u_promoter.getSelectedItem().toString().equals("Yes") &
                edt_aob_occu_r_u_promoter_comapany.getText().toString().equals("")) {
            edt_aob_occu_r_u_promoter_comapany.requestFocus();
            return "Please enter company name";
        } else if (spnr_aob_occu_r_u_promoter.getSelectedItem().toString().equals("Yes") &
                edt_aob_occu_r_u_promoter_comapany_pan.getText().toString().equals("")) {
            edt_aob_occu_r_u_promoter_comapany_pan.requestFocus();
            return "Please enter company pan";
        } else if (spnr_aob_occu_r_u_promoter.getSelectedItem().toString().equals("Yes") &
                edt_aob_occu_r_u_promoter_comapany_tin.getText().toString().equals("")) {
            edt_aob_occu_r_u_promoter_comapany_tin.requestFocus();
            return "Please enter company tin";
        } else if (spnr_aob_occu_company_asso_with_sbil.getSelectedItem().toString().equals("Yes") &
                edt_aob_occu_company_asso_with_sbil_cmmnt.getText().toString().equals("")) {
            edt_aob_occu_company_asso_with_sbil_cmmnt.requestFocus();
            return "Please enter comments";
        } else if (spnr_aob_occu_releted_sbil_employee.getSelectedItem().toString().equals("Yes") &
                edt_aob_occu_releted_sbil_employee_name.getText().toString().equals("")) {
            edt_aob_occu_releted_sbil_employee_name.requestFocus();
            return "Please enter employee name";
        } else if (spnr_aob_occu_releted_sbil_employee.getSelectedItem().toString().equals("Yes") &
                edt_aob_occu_releted_sbil_employee_design.getText().toString().equals("")) {
            edt_aob_occu_releted_sbil_employee_design.requestFocus();
            return "Please enter designation";
        } else if (spnr_aob_occu_releted_sbil_employee.getSelectedItem().toString().equals("Yes") &
                edt_aob_occu_releted_sbil_employee_relation.getText().toString().equals("")) {
            edt_aob_occu_releted_sbil_employee_relation.requestFocus();
            return "Please enter relation with applicant";
        } else if (spnr_aob_occu_releted_sbil_employee.getSelectedItem().toString().equals("Yes") &
                edt_aob_occu_releted_sbil_employee_insu_off.getText().toString().equals("")) {
            edt_aob_occu_releted_sbil_employee_insu_off.requestFocus();
            return "Please enter Details of Bank/SBI Assocaite/SBI LIFE Insurance Office";
        } else if (spnr_aob_occu_aforeside_relative.getSelectedItem().toString().equals("Yes") &
                edt_aob_occu_releted_sbil_employee_bank_add.getText().toString().equals("")) {
            edt_aob_occu_releted_sbil_employee_bank_add.requestFocus();
            return "Please enter bank address";
        } else if (spnr_aob_occu_sbi_x_employee.getSelectedItem().toString().equals("Yes") &
                edt_aob_occu_sbi_x_employee_cmmnt.getText().toString().equals("")) {

            edt_aob_occu_sbi_x_employee_cmmnt.requestFocus();
            return "Please enter comments";
        } else if (spnr_aob_occu_sbi_x_employee.getSelectedItem().toString().equals("Yes") &
                txt_aob_occu_employment_last_date.getText().toString().equals("")) {
            txt_aob_occu_employment_last_date.requestFocus();
            return "Please select last date of employment";
        } else if (spnr_aob_occu_sbi_x_employee.getSelectedItem().toString().equals("Yes") &
                edt_aob_occu_employment_cmmnt.getText().toString().equals("")) {
            edt_aob_occu_employment_cmmnt.requestFocus();
            return "Please enter comments";
        } else
            return "";
    }

    public void enableDisableAllFields(boolean is_enable) {

        spnr_aob_occu_applicable.setEnabled(is_enable);

        spnr_aob_occu_self_employed.setEnabled(is_enable);

        spnr_aob_occu_area_ops.setEnabled(is_enable);

        spnr_aob_occu_annual_income.setEnabled(is_enable);

        spnr_aob_occu_agency.setEnabled(is_enable);

        edt_aob_occu_company.setEnabled(is_enable);

        spnr_aob_occu_evr_surrendered.setEnabled(is_enable);

        spnr_aob_occu_other_insurer.setEnabled(is_enable);

        spnr_aob_occu_r_u_promoter.setEnabled(is_enable);

        spnr_aob_occu_company_asso_with_sbil.setEnabled(is_enable);

        spnr_aob_occu_releted_sbil_employee.setEnabled(is_enable);

        spnr_aob_occu_aforeside_relative.setEnabled(is_enable);

        spnr_aob_occu_sbi_x_employee.setEnabled(is_enable);

        edt_aob_occu_applicable_commnt.setEnabled(is_enable);
        edt_aob_occu_self_employed_cmmnt.setEnabled(is_enable);
        edt_aob_occu_company_cmmnt.setEnabled(is_enable);
        edt_aob_occu_evr_surrendered_cmmnt.setEnabled(is_enable);
        edt_aob_occu_other_insurer_comapny.setEnabled(is_enable);
        edt_aob_occu_other_insurer_comapny_cmmnt.setEnabled(is_enable);
        edt_aob_occu_r_u_promoter_comapany.setEnabled(is_enable);
        edt_aob_occu_r_u_promoter_comapany_pan.setEnabled(is_enable);
        edt_aob_occu_r_u_promoter_comapany_tin.setEnabled(is_enable);
        edt_aob_occu_company_asso_with_sbil_cmmnt.setEnabled(is_enable);
        edt_aob_occu_releted_sbil_employee_name.setEnabled(is_enable);
        edt_aob_occu_releted_sbil_employee_design.setEnabled(is_enable);
        edt_aob_occu_releted_sbil_employee_relation.setEnabled(is_enable);
        edt_aob_occu_releted_sbil_employee_bank_add.setEnabled(is_enable);
        edt_aob_occu_sbi_x_employee_cmmnt.setEnabled(is_enable);
        edt_aob_occu_employment_cmmnt.setEnabled(is_enable);

        txt_aob_occu_employment_last_date.setEnabled(is_enable);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        switch (view.getTag()) {
            case DATE_EMPLOYMENT_LAST_DATE:
                String strSelectedDate = (dayOfMonth < 10 ? "0" : "") + dayOfMonth + "-"
                        + ((monthOfYear + 1) < 10 ? "0" : "") + (monthOfYear + 1) + "-"
                        + year;

                txt_aob_occu_employment_last_date.setText(strSelectedDate);

                break;
        }

    }
}
