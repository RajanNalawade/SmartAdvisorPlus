package sbilife.com.pointofsale_bancaagency.agent_on_boarding;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.common.AsyncUploadFile_Common;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

public class ActivityAOBBankDetails extends AppCompatActivity implements View.OnClickListener,
        AsyncUploadFile_Common.Interface_Upload_File_Common {

    private final String NAMESPACE = "http://tempuri.org/";
    private final String METHOD_NAME_VERIFY_BANK_DETAILS = "validate_ifsc_brcode_agentonboard";
    InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; ++i) {
                if (!Pattern.compile("[ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890]*").matcher(String.valueOf(source.charAt(i))).matches()) {
                    return "";
                }
            }

            return null;
        }
    };
    private CommonMethods mCommonMethods;
    private Context mContext;
    private DatabaseHelper db;
    private Button btn_aob_bank_next, btn_aob_bank_back;
    private LinearLayout ll_aob_bank_account_brach_code, ll_aob_bank_account_brach_ifsc;
    private Spinner spnr_aob_bank_account_type;
    private EditText edt_aob_bank_account_num, edt_aob_bank_account_brach_code, edt_aob_bank_account_brach_ifsc;
    private boolean validate_acc_no = false, is_dashboard = false, is_back_pressed = false, is_ia_upgrade = false;
    private StringBuilder str_bank_info = null;
    private ParseXML mParseXML;
    private ArrayList<PojoAOB> lstRes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_aob_bank_details);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.cifenrollment_pf_activity_title);

        if (getIntent().hasExtra("is_dashboard"))
            is_dashboard = getIntent().getBooleanExtra("is_dashboard", false);

        if (getIntent().hasExtra("is_ia_upgrade"))
            is_ia_upgrade = getIntent().getBooleanExtra("is_ia_upgrade", false);

        initialisation();

        //non editable with no saving
        if (is_dashboard || is_ia_upgrade) {
            //non editable with no saving
            enableDisableAllFields(false);
        } else {
            enableDisableAllFields(true);
        }
    }

    public void initialisation() {

        mContext = this;
        mCommonMethods = new CommonMethods();
        mCommonMethods.setApplicationToolbarMenu1(this,"Agent on Boarding");

        db = new DatabaseHelper(mContext);

        mParseXML = new ParseXML();

        View view_aob_bank_formIA = findViewById(R.id.view_aob_bank_formIA);
        TextView txt_aob_bank_formIA = findViewById(R.id.txt_aob_bank_formIA);
        if (is_ia_upgrade) {
            view_aob_bank_formIA.setVisibility(View.GONE);
            txt_aob_bank_formIA.setVisibility(View.GONE);
        } else {
            view_aob_bank_formIA.setVisibility(View.VISIBLE);
            txt_aob_bank_formIA.setVisibility(View.VISIBLE);
        }

        spnr_aob_bank_account_type = (Spinner) findViewById(R.id.spnr_aob_bank_account_type);
        ArrayAdapter<String> acc_type_adapter = new ArrayAdapter<String>(
                mContext, R.layout.spinner_aob, getResources().getStringArray(R.array.arr_aob_bank_account));
        spnr_aob_bank_account_type.setAdapter(acc_type_adapter);
        acc_type_adapter.notifyDataSetChanged();

        spnr_aob_bank_account_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                /*String str_res = lstRes.get(0).getStr_bank_details() == null ? "" : lstRes.get(0).getStr_bank_details();
                if (str_res.equals("")) {
                    edt_aob_bank_account_num.setText("");

                    edt_aob_bank_account_brach_ifsc.setText("");

                    edt_aob_bank_account_brach_code.setText("");*/

                //NEFT
                if (position == 0) {

                    edt_aob_bank_account_num.setText("");

                    edt_aob_bank_account_brach_ifsc.setText("");

                    edt_aob_bank_account_brach_code.setText("");


                    ll_aob_bank_account_brach_ifsc.setVisibility(View.GONE);

                    ll_aob_bank_account_brach_code.setVisibility(View.GONE);


                } else if (position == 2) {

                    //edt_aob_bank_account_num.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                    //set 20 chara account no
                    edt_aob_bank_account_num.setFilters(new InputFilter[]{filter, new InputFilter.LengthFilter(20)});

                    //edt_aob_bank_account_num.setText("");

                    ll_aob_bank_account_brach_ifsc.setVisibility(View.VISIBLE);

                    ll_aob_bank_account_brach_code.setVisibility(View.GONE);

                } else {
                    //edt_aob_bank_account_num.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    //set 11 chara account no
                    edt_aob_bank_account_num.setFilters(new InputFilter[]{filter, new InputFilter.LengthFilter(11)});

                    //edt_aob_bank_account_num.setText("");

                    ll_aob_bank_account_brach_ifsc.setVisibility(View.GONE);

                    ll_aob_bank_account_brach_code.setVisibility(View.VISIBLE);
                }
                //}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        edt_aob_bank_account_num = (EditText) findViewById(R.id.edt_aob_bank_account_num);

        edt_aob_bank_account_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (spnr_aob_bank_account_type.getSelectedItem().toString().equals("EFT")) {

                    if (s.length() != 11) {
                        validate_acc_no = false;
                        edt_aob_bank_account_num.setError("Account should be 11 digit");
                    } else {
                        validate_acc_no = true;
                        edt_aob_bank_account_num.setError(null);
                    }

                } else if (spnr_aob_bank_account_type.getSelectedItem().toString().equals("NEFT")) {
                    if (s.length() == 0) {
                        validate_acc_no = false;
                        edt_aob_bank_account_num.setError("Account should be less than 20 digit");
                    } else {
                        validate_acc_no = true;
                        edt_aob_bank_account_num.setError(null);
                    }
                }

            }
        });

        edt_aob_bank_account_brach_code = (EditText) findViewById(R.id.edt_aob_bank_account_brach_code);
        edt_aob_bank_account_brach_ifsc = (EditText) findViewById(R.id.edt_aob_bank_account_brach_ifsc);

        btn_aob_bank_next = (Button) findViewById(R.id.btn_aob_bank_next);
        btn_aob_bank_next.setOnClickListener(this);

        btn_aob_bank_back = (Button) findViewById(R.id.btn_aob_bank_back);
        btn_aob_bank_back.setOnClickListener(this);

        ll_aob_bank_account_brach_code = (LinearLayout) findViewById(R.id.ll_aob_bank_account_brach_code);
        ll_aob_bank_account_brach_ifsc = (LinearLayout) findViewById(R.id.ll_aob_bank_account_brach_ifsc);

        str_bank_info = new StringBuilder();

        //set Data from DB
        lstRes = db.get_agent_on_boarding_details_by_ID(Activity_AOB_Authentication.row_details);

        if (lstRes.size() > 0) {

            String str_bank_info = lstRes.get(0).getStr_bank_details();
            str_bank_info = str_bank_info == null ? "" : str_bank_info;

            if (!str_bank_info.equals("")) {

                String str_bank_acc_type = mParseXML.parseXmlTag(str_bank_info, "bank_info_acc_type");
                String str_bank_acc_number = mParseXML.parseXmlTag(str_bank_info, "bank_info_acc_number");
                String str_bank_branch_code = mParseXML.parseXmlTag(str_bank_info, "bank_info_branch_code");
                String str_bank_ifsc_code = mParseXML.parseXmlTag(str_bank_info, "bank_info_ifsc_code");

                spnr_aob_bank_account_type.setSelection(Arrays.asList(getResources().
                        getStringArray(R.array.arr_aob_bank_account)).indexOf(str_bank_acc_type));

                edt_aob_bank_account_num.setText(str_bank_acc_number);

                edt_aob_bank_account_brach_code.setText(str_bank_branch_code);
                edt_aob_bank_account_brach_ifsc.setText(str_bank_ifsc_code);

                if (str_bank_acc_type.equals("EFT")) {
                    ll_aob_bank_account_brach_code.setVisibility(View.VISIBLE);
                    ll_aob_bank_account_brach_ifsc.setVisibility(View.GONE);
                } else {
                    ll_aob_bank_account_brach_code.setVisibility(View.GONE);
                    ll_aob_bank_account_brach_ifsc.setVisibility(View.VISIBLE);
                }
            }
        }

    }

    private void get_bank_info_xml() {

        String str_bank_acc_type = spnr_aob_bank_account_type.getSelectedItem().toString();
        String str_bank_acc_number = edt_aob_bank_account_num.getText().toString();
        String str_bank_branch_code = edt_aob_bank_account_brach_code.getText().toString();
        String str_bank_ifsc_code = edt_aob_bank_account_brach_ifsc.getText().toString();

        str_bank_info = new StringBuilder();

        //str_occupational_info.append("<bank_info>");
        str_bank_info.append("<bank_info_acc_type>").append(str_bank_acc_type).append("</bank_info_acc_type>");
        str_bank_info.append("<bank_info_acc_number>").append(str_bank_acc_number).append("</bank_info_acc_number>");
        str_bank_info.append("<bank_info_branch_code>").append(str_bank_branch_code).append("</bank_info_branch_code>");
        str_bank_info.append("<bank_info_ifsc_code>").append(str_bank_ifsc_code).append("</bank_info_ifsc_code>");

        //str_occupational_info.append("</bank_info>");
    }

    public String validate_all_details() {

        if (spnr_aob_bank_account_type.getSelectedItem().toString().equals("Select Account Type")) {
            spnr_aob_bank_account_type.requestFocus();
            return "Please select account type";
        } else if (!validate_acc_no) {
            edt_aob_bank_account_num.requestFocus();
            return "Please enter valid account number";
        } else if (spnr_aob_bank_account_type.getSelectedItem().toString().equals("EFT") &
                edt_aob_bank_account_brach_code.getText().toString().equals("")) {
            edt_aob_bank_account_brach_code.requestFocus();
            return "Please enter branch code";
        } else if (spnr_aob_bank_account_type.getSelectedItem().toString().equals("NEFT") &
                edt_aob_bank_account_brach_ifsc.getText().toString().equals("")) {

            edt_aob_bank_account_brach_ifsc.requestFocus();
            return "Please enter branch ifsc code";
        } else if (spnr_aob_bank_account_type.getSelectedItem().toString().equals("NEFT") &
                edt_aob_bank_account_brach_ifsc.getText().toString().contains("SBIN")) {

            edt_aob_bank_account_brach_ifsc.requestFocus();
            return "Please enter details under EFT";
        } else if (spnr_aob_bank_account_type.getSelectedItem().toString().equals("NEFT") &
                edt_aob_bank_account_brach_ifsc.getText().toString().length() != 11) {
            edt_aob_bank_account_brach_ifsc.requestFocus();
            return "please enter proper ifsc code no.";
        } else
            return "";
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(ActivityAOBBankDetails.this, ActivityAOBNomination.class);
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

            case R.id.btn_aob_bank_back:
                is_back_pressed = true;
                onBackPressed();
                break;

            case R.id.btn_aob_bank_next:

                if (is_dashboard) {
                    Intent mIntent = new Intent(ActivityAOBBankDetails.this, ActivityAOBForm1A.class);
                    mIntent.putExtra("is_dashboard", is_dashboard);
                    startActivity(mIntent);
                } else if (is_ia_upgrade) {
                    Intent mIntent = new Intent(ActivityAOBBankDetails.this, ActivityAOBExamTraining.class);
                    mIntent.putExtra("is_ia_upgrade", is_ia_upgrade);
                    startActivity(mIntent);
                } else {
                    //1. validate all details
                    String str_error = validate_all_details();
                    if (str_error.equals("")) {

                        //verify branch code and ifsc code
                        if (spnr_aob_bank_account_type.getSelectedItem().toString().equals("EFT")) {

                            createSoapRequestToUploadDoc(spnr_aob_bank_account_type.getSelectedItem().toString(),
                                    edt_aob_bank_account_brach_code.getText().toString());

                        } else if (spnr_aob_bank_account_type.getSelectedItem().toString().equals("NEFT")) {

                            createSoapRequestToUploadDoc(spnr_aob_bank_account_type.getSelectedItem().toString(),
                                    edt_aob_bank_account_brach_ifsc.getText().toString());
                        }
                    } else {
                        mCommonMethods.showMessageDialog(mContext, str_error);
                    }
                }
                break;

            default:
                break;

        }
    }

    public void enableDisableAllFields(boolean is_enable) {

        spnr_aob_bank_account_type.setEnabled(is_enable);

        edt_aob_bank_account_num.setEnabled(is_enable);

        edt_aob_bank_account_brach_code.setEnabled(is_enable);
        edt_aob_bank_account_brach_ifsc.setEnabled(is_enable);
    }

    private void createSoapRequestToUploadDoc(final String strType, final String strCode) {
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_VERIFY_BANK_DETAILS);
        request.addProperty("srCode", strCode);
        request.addProperty("srType", strType);

        new AsyncUploadFile_Common(mContext, this, request,
                METHOD_NAME_VERIFY_BANK_DETAILS).execute();
    }

    @Override
    public void onUploadComplete(Boolean result) {
        if (result) {

            //2. create xml string for data saving
            get_bank_info_xml();

            //3. update data against global row id
            ContentValues cv = new ContentValues();
            cv.put(db.AGENT_ON_BOARDING_BANK_DETAILS, str_bank_info.toString());
            /*if (is_ia_upgrade) {

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
            }*/

            cv.put(db.AGENT_ON_BOARDING_UPDATED_BY, mCommonMethods.GetUserCode(mContext));

            Calendar c = Calendar.getInstance();
            //save date in long
            cv.put(db.AGENT_ON_BOARDING_UPDATED_DATE, new Date(c.getTimeInMillis()).getTime() + "");
            cv.put(db.AGENT_ON_BOARDING_SYNCH_STATUS, "5");

            int i = db.update_agent_on_boarding_details(cv, db.AGENT_ON_BOARDING_ID + " =? ",
                    new String[]{Activity_AOB_Authentication.row_details + ""});

            mCommonMethods.showToast(mContext, "Details saved Successfully : " + i);

            Intent mIntent = new Intent(ActivityAOBBankDetails.this, ActivityAOBForm1A.class);
            startActivity(mIntent);

        } else {

            if (spnr_aob_bank_account_type.getSelectedItem().toString().equals("EFT")) {
                mCommonMethods.showMessageDialog(mContext, "Wrong branch code");
            } else if (spnr_aob_bank_account_type.getSelectedItem().toString().equals("NEFT")) {
                mCommonMethods.showMessageDialog(mContext, "Wrong IFSC code");
            }
        }
    }
}
