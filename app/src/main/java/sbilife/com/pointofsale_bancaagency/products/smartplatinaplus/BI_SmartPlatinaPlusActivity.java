package sbilife.com.pointofsale_bancaagency.products.smartplatinaplus;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sbilife.com.pointofsale_bancaagency.CaptureSignature;
import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.NA_CBI_bean;
import sbilife.com.pointofsale_bancaagency.NeedAnalysisBIService;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.ProposerCaptureSignature;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.common.CommonForAllProd;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.ProductInfo;
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.common.StorageUtils;
import sbilife.com.pointofsale_bancaagency.common.Success;
import sbilife.com.pointofsale_bancaagency.needanalysis.NeedAnalysisActivity;
import sbilife.com.pointofsale_bancaagency.new_bussiness.ProductBIBean;
import sbilife.com.pointofsale_bancaagency.utility.GridHeight;

public class BI_SmartPlatinaPlusActivity extends AppCompatActivity implements
        OnEditorActionListener {
    private Spinner spnr_Age;
    private Button btnSubmit, btnback;
    // for BI
    private StringBuilder inputVal;
    private String premPayingTerm = "", premPayingMode = "", policyTerm = "", payoutPeriod = "",
            ageAtEntry = "", gender = "";
    private String basicCoverSumAssured = "";
    private SmartPlatinaPlusBean smartPlatinaPlusBean;
    private EditText edt_premiumAmt;
    private Spinner spnrPremPayingTerm, spnrPremPayingMode, spnrPolicyTerm;
    private RadioButton rb_proposerdetail_personaldetail_backdating_yes,
            rb_proposerdetail_personaldetail_backdating_no;
    private TextView btn_proposerdetail_personaldetail_backdatingdate,
            btn_MarketingOfficalDate;
    private Button btn_PolicyholderDate;
    private ImageButton Ibtn_signatureofMarketing;
    private ImageButton Ibtn_signatureofPolicyHolders;
    private Dialog d;
    private final int SIGNATURE_ACTIVITY = 1;
    private String latestImage = "";
    private List<M_BI_SmartPlatinaPlus_Adapter> list_data;
    private DatabaseHelper db;
    private static int DIALOG_ID;
    private int mYear;
    private int mMonth;
    private int mDay;
    private TextView btn_bi_smart_guaranteed_saving_plan_life_assured_date;
    private String QuatationNumber = "";
    private String planName = "";
    private String proposer_Title = "", proposer_First_Name = "",
            proposer_Middle_Name = "", proposer_Last_Name = "";
    private String lifeAssured_Title = "", lifeAssured_First_Name = "",
            lifeAssured_Middle_Name = "", lifeAssured_Last_Name = "",
            name_of_life_assured = "", lifeAssured_date_of_birth = "",
            lifeAssuredAge = "";
    private String proposer_Backdating_WishToBackDate_Policy = "";
    private Spinner spnr_bi_smart_guaranteed_saving_plan_life_assured_title;
    private EditText edt_bi_smart_guaranteed_saving_plan_life_assured_first_name;
    private EditText edt_bi_smart_guaranteed_saving_plan_life_assured_middle_name;
    private EditText edt_bi_smart_guaranteed_saving_plan_life_assured_last_name;
    private String proposer_date_of_birth = "";
    private String output = "";
    private String input = "";
    private ParseXML prsObj;
    private String name_of_proposer = "";
    private String name_of_person = "";
    private String place1 = "";
    private String place2 = "";
    private String date1 = "";
    private String date2 = "";
    private String agent_sign = "";
    private String proposer_sign = "", proposer_Is_Same_As_Life_Assured = "Y";
    private CommonForAllProd commonForAllProd;
    private SmartPlatinaPlusProperties prop;
    private DecimalFormat currencyFormat;
    private StringBuilder bussIll = null;
    private StringBuilder retVal = null;
    private File mypath;
    private EditText edt_proposerdetail_basicdetail_contact_no,
            edt_proposerdetail_basicdetail_Email_id,
            edt_proposerdetail_basicdetail_ConfirmEmail_id;
    private String mobileNo = "";
    private String emailId = "";
    private String ConfirmEmailId = "";
    private boolean validationFla1 = false;
    private String ProposerEmailId = "";
    private Bitmap photoBitmap;
    private CheckBox cb_staffdisc;
    private String isStaff = "", proposer_Backdating_BackDate = "", strGuaranteedPayoutFrequency = "", str_income_plan;
    private Spinner spnr_guaranteed_payout_frequency, spnr_income_plan;
    private LinearLayout linearlayoutProposerDetails;
    private Spinner spnr_bi_money_back_gold_proposer_title;
    private EditText edt_bi_money_back_gold_proposer_first_name,
            edt_bi_money_back_gold_proposer_middle_name,
            edt_bi_money_back_gold_proposer_last_name;
    private TextView btn_bi_money_back_gold_proposer_date;
    private String strProposerAge = "";
    private TextView edt_bi_money_back_gold_proposer_age;
    private String gender_proposer = "";
    private String BackDateinterest = "";
    private String BackDateinterestwithGST = "";
    private LinearLayout ll_backdating1;
    private final String str_user_POS_User = "";
    private EditText edt_bi_smart_guaranteed_saving_plan_Actual_policyterm;
    private String basicprem = "", basicpremfirstyr = "", basicpremsecondyr = "";
    private Context context;
    private CommonMethods commonMethods;
    private StorageUtils mStorageUtils;
    private NeedAnalysisBIService NABIObj;
    private CheckBox cb_kerladisc;
    private String agentcode, agentMobile, agentEmail, userType;
    private int needAnalysis_flag = 0;
    private String product_Code, product_UIN, product_cateogory, product_type;
    private String na_input = null;
    private String na_output = null;
    private String bankUserType = "", transactionMode = "";
    private Spinner spnr_Gender, spinnerProposerGender;
    private String Check = "";
    private ImageButton imageButtonProposerPhotograph;
    private NA_CBI_bean na_cbi_bean;
    private File needAnalysispath, newFile;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bi_smartplatinaplus);

        commonMethods = new CommonMethods();
        mStorageUtils = new StorageUtils();
        commonMethods.setApplicationToolbarMenu(this, getString(R.string.app_name));
        context = this;
        db = new DatabaseHelper(this);
        NABIObj = new NeedAnalysisBIService(this);
        prsObj = new ParseXML();


        currencyFormat = new DecimalFormat("##,##,##,###");
        Intent intent = getIntent();
        String na_flag = intent.getStringExtra("NAFlag");
        commonForAllProd = new CommonForAllProd();

        //Added Tushar Kadam Kerla Applicable Tax 7-Jun-2019
        TableRow tablerowKerlaDiscount = findViewById(R.id.tablerowKerlaDiscount);
        cb_kerladisc = findViewById(R.id.cb_kerladisc);
        commonMethods.setKerlaDiscount(context, tablerowKerlaDiscount, cb_kerladisc);
        //End Added Tushar Kadam Kerla Applicable Tax 7-Jun-2019
        try {
            agentcode = SimpleCrypto.decrypt("SBIL",
                    db.GetUserCode());

            agentMobile = SimpleCrypto.decrypt("SBIL",
                    db.GetMobileNo());
            agentEmail = SimpleCrypto.decrypt("SBIL",
                    db.GetEmailId());
            userType = SimpleCrypto.decrypt("SBIL",
                    db.GetUserType());

            /* parivartan changes */
            ProductInfo prodInfoObj = new ProductInfo(context);
            planName = getString(R.string.sbi_life_smart_platina_plus);
            product_Code = getString(R.string.sbi_life_smart_platina_plus_code);
            product_UIN = prodInfoObj.getProductUIN(planName);
            product_cateogory = prodInfoObj
                    .getProductCategory(planName);
            product_type = prodInfoObj.getProductType(planName);
            /* end */
        } catch (Exception e) {

            e.printStackTrace();
        }
        if (na_flag != null) {
            if (na_flag.equalsIgnoreCase("1")) {
                needAnalysis_flag = 1;
                na_input = intent.getStringExtra("NaInput");
                na_output = intent.getStringExtra("NaOutput");
                String na_dob = intent.getStringExtra("custDOB");
                // gender=intent.getStringExtra("custGender");

                bankUserType = intent.getStringExtra("Other");

                if (bankUserType != null) {

                } else {
                    bankUserType = "";
                }

                int k = 12 - (agentcode).length();
                StringBuilder zero = new StringBuilder();
                for (int i = 0; i < k; i++) {
                    zero = zero.append("0");
                }
                QuatationNumber = CommonForAllProd.getquotationNumber30(
                        product_Code, agentcode, zero + "");

            }
        } else
            needAnalysis_flag = 0;

        initialiseDate();


        // Class Declaration

        prsObj = new ParseXML();

        commonForAllProd = new CommonForAllProd();
        prop = new SmartPlatinaPlusProperties();

        list_data = new ArrayList<>();

        // Variable Declaration
        currencyFormat = new DecimalFormat("##,##,##,###");
        rb_proposerdetail_personaldetail_backdating_yes = findViewById(R.id.rb_smart_guaranteed_saving_backdating_yes);
        rb_proposerdetail_personaldetail_backdating_no = findViewById(R.id.rb_smart_guaranteed_saving_backdating_no);

        btn_proposerdetail_personaldetail_backdatingdate = findViewById(R.id.btn_shubh_nivesh_backdatingdate);
        edt_premiumAmt = findViewById(R.id.et_bi_smart_guaranteed_saving_plan_premium_amt);
        btn_bi_smart_guaranteed_saving_plan_life_assured_date = findViewById(R.id.btn_bi_smart_guaranteed_saving_plan_life_assured_date);
        spnr_bi_smart_guaranteed_saving_plan_life_assured_title = findViewById(R.id.spnr_bi_smart_guaranteed_saving_plan_life_assured_title);
        edt_bi_smart_guaranteed_saving_plan_life_assured_first_name = findViewById(R.id.edt_bi_smart_guaranteed_saving_plan_life_assured_first_name);
        edt_bi_smart_guaranteed_saving_plan_life_assured_middle_name = findViewById(R.id.edt_bi_smart_guaranteed_saving_plan_life_assured_middle_name);
        edt_bi_smart_guaranteed_saving_plan_life_assured_last_name = findViewById(R.id.edt_bi_smart_guaranteed_saving_plan_life_assured_last_name);
        spnrPremPayingTerm = findViewById(R.id.spnr_bi_smart_guaranteed_saving_plan_premium_paying_term);
        spnrPremPayingMode = findViewById(R.id.spnr_bi_smart_guaranteed_saving_plan_premium_paying_mode);
        spnrPolicyTerm = findViewById(R.id.spnr_bi_smart_guaranteed_saving_plan_policyterm);
        spnr_Gender = findViewById(R.id.spnr_Gender);
        spinnerProposerGender = findViewById(R.id.spinnerProposerGender);

        spnr_Age = findViewById(R.id.spnr_bi_smart_guaranteed_saving_plan_age);
        spnr_Age.setVisibility(View.INVISIBLE);
        edt_proposerdetail_basicdetail_contact_no = findViewById(R.id.edt_smart_guaranteed_saving_contact_no);
        edt_proposerdetail_basicdetail_Email_id = findViewById(R.id.edt_guaranteed_saving_Email_id);
        edt_proposerdetail_basicdetail_ConfirmEmail_id = findViewById(R.id.edt_guaranteed_saving_ConfirmEmail_id);

        btnSubmit = findViewById(R.id.btn_bi_smart_guaranteed_saving_plan_btnSubmit);
        btnback = findViewById(R.id.btn_bi_smart_guaranteed_saving_plan_btnback);
        cb_staffdisc = findViewById(R.id.cb_staffdisc);
        spnr_guaranteed_payout_frequency = findViewById(R.id.spnr_guaranteed_payout_frequency);
        spnr_income_plan = findViewById(R.id.spnr_income_plan);


        linearlayoutProposerDetails = findViewById(R.id.linearlayoutProposerDetails);
        spnr_bi_money_back_gold_proposer_title = findViewById(R.id.spnr_bi_money_back_gold_proposer_title);
        edt_bi_money_back_gold_proposer_first_name = findViewById(R.id.edt_bi_money_back_gold_proposer_first_name);
        edt_bi_money_back_gold_proposer_middle_name = findViewById(R.id.edt_bi_money_back_gold_proposer_middle_name);
        edt_bi_money_back_gold_proposer_last_name = findViewById(R.id.edt_bi_money_back_gold_proposer_last_name);
        btn_bi_money_back_gold_proposer_date = findViewById(R.id.btn_bi_money_back_gold_proposer_date);


        edt_bi_money_back_gold_proposer_age = findViewById(R.id.edt_bi_money_back_gold_proposer_age);

        rb_proposerdetail_personaldetail_backdating_yes = findViewById(R.id.rb_shubh_nivesh_backdating_yes);
        rb_proposerdetail_personaldetail_backdating_no = findViewById(R.id.rb_shubh_nivesh_backdating_no);

        ll_backdating1 = findViewById(R.id.ll_backdating1);


        edt_bi_smart_guaranteed_saving_plan_Actual_policyterm = findViewById(R.id.edt_bi_smart_guaranteed_saving_plan_Actual_policyterm);

        edt_bi_smart_guaranteed_saving_plan_Actual_policyterm.setClickable(false);
        edt_bi_smart_guaranteed_saving_plan_Actual_policyterm.setEnabled(false);


        String str_agent_bank_name = "";
        if (str_agent_bank_name != null && str_agent_bank_name.equalsIgnoreCase("YESBANK")) {
            cb_staffdisc.setText("SBI Staff Discount");
        } else {
            edt_proposerdetail_basicdetail_contact_no.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        }

        commonMethods.fillSpinnerValue(context, spnr_bi_smart_guaranteed_saving_plan_life_assured_title,
                Arrays.asList(getResources().getStringArray(R.array.arr_select_title)));

        commonMethods.fillSpinnerValue(context, spnr_bi_money_back_gold_proposer_title,
                Arrays.asList(getResources().getStringArray(R.array.arr_select_title)));

        //Gender
        String[] genderList = {"Male", "Female", "Third Gender"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, genderList);
        genderAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_Gender.setAdapter(genderAdapter);
        spinnerProposerGender.setAdapter(genderAdapter);
        genderAdapter.notifyDataSetChanged();

        // Age
        String[] AgeList = new String[prop.maxAge - prop.minAge + 1];
        for (int i = prop.minAge; i <= prop.maxAge; i++) {
            AgeList[i - prop.minAge] = i + "";
        }
        ArrayAdapter<String> AgeAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, AgeList);
        AgeAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr_Age.setAdapter(AgeAdapter);
        AgeAdapter.notifyDataSetChanged();
        spnr_Age.setSelection(prop.defaultAge, false);


        String[] premPayingTermArray = {"Select", "7", "8", "10"};

        ArrayAdapter<String> premPayingTermAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item,
                premPayingTermArray);

        spnrPremPayingTerm.setAdapter(premPayingTermAdapter);


        String[] premPayingModeArray = {"Yearly", "Half-Yearly", "Monthly"};

        ArrayAdapter<String> premPayingModeAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item,
                premPayingModeArray);

        spnrPremPayingMode.setAdapter(premPayingModeAdapter);
        //spnrPremPayingMode.setEnabled(false);

        String[] guaranteedPayoutFrequencyArray = {"Yearly", "Half-Yearly", "Quarterly", "Monthly"};
        ArrayAdapter<String> guaranteedPayoutFrequencyAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, guaranteedPayoutFrequencyArray);
        spnr_guaranteed_payout_frequency.setAdapter(guaranteedPayoutFrequencyAdapter);
        //spnr_guaranteed_payout_frequency.setEnabled(false);


        String[] spnr_income_planArray = {"Life Income", "Guaranteed Income"};
        ArrayAdapter<String> spnr_income_planAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, spnr_income_planArray);
        spnr_income_plan.setAdapter(spnr_income_planAdapter);
        //spnr_income_plan.setEnabled(false);

        // Calculate premium

        cb_staffdisc.setOnClickListener(v -> {
            if (cb_staffdisc.isChecked()) {
                cb_staffdisc.setChecked(true);
            }
        });

        edt_bi_smart_guaranteed_saving_plan_life_assured_first_name
                .setOnEditorActionListener(this);
        edt_bi_smart_guaranteed_saving_plan_life_assured_last_name
                .setOnEditorActionListener(this);
        edt_bi_smart_guaranteed_saving_plan_life_assured_middle_name
                .setOnEditorActionListener(this);
        edt_premiumAmt.setOnEditorActionListener(this);

        setSpinnerAndOtherListner();

        commonMethods.setFocusable(edt_bi_smart_guaranteed_saving_plan_life_assured_first_name);

        edt_bi_smart_guaranteed_saving_plan_life_assured_first_name
                .requestFocus();

        edt_proposerdetail_basicdetail_contact_no
                .addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence arg0, int arg1,
                                              int arg2, int arg3) {


                    }

                    @Override
                    public void beforeTextChanged(CharSequence arg0, int arg1,
                                                  int arg2, int arg3) {


                    }

                    @Override
                    public void afterTextChanged(Editable arg0) {

                        String abc = edt_proposerdetail_basicdetail_contact_no
                                .getText().toString();
                        mobile_validation(abc);

                    }
                });

        edt_proposerdetail_basicdetail_Email_id
                .addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence arg0, int arg1,
                                              int arg2, int arg3) {


                    }

                    @Override
                    public void beforeTextChanged(CharSequence arg0, int arg1,
                                                  int arg2, int arg3) {


                    }

                    @Override
                    public void afterTextChanged(Editable arg0) {

                        ProposerEmailId = edt_proposerdetail_basicdetail_Email_id
                                .getText().toString();
                        email_id_validation(ProposerEmailId);

                    }
                });

        edt_proposerdetail_basicdetail_ConfirmEmail_id
                .addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence arg0, int arg1,
                                              int arg2, int arg3) {


                    }

                    @Override
                    public void beforeTextChanged(CharSequence arg0, int arg1,
                                                  int arg2, int arg3) {


                    }

                    @Override
                    public void afterTextChanged(Editable arg0) {

                        String proposer_confirm_emailId = edt_proposerdetail_basicdetail_ConfirmEmail_id
                                .getText().toString();
                        confirming_email_id(proposer_confirm_emailId);

                    }
                });

        edt_proposerdetail_basicdetail_contact_no
                .setOnEditorActionListener(this);
        edt_proposerdetail_basicdetail_Email_id.setOnEditorActionListener(this);
        edt_proposerdetail_basicdetail_ConfirmEmail_id
                .setOnEditorActionListener(this);
    }


    private void setSpinnerAndOtherListner() {

        spnrPremPayingTerm.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                premPayingTerm = spnrPremPayingTerm.getSelectedItem().toString();
                if (premPayingTerm.equalsIgnoreCase("10")) {
                    String[] policyTermArray = {"15", "20", "25"};
                    ArrayAdapter<String> policyTermAdapter = new ArrayAdapter<>(
                            getApplicationContext(), R.layout.spinner_item, policyTermArray);
                    spnrPolicyTerm.setAdapter(policyTermAdapter);
                    //spnrPolicyTerm.setEnabled(false);
                } else if (premPayingTerm.equalsIgnoreCase("7") || premPayingTerm.equalsIgnoreCase("8")) {
                    String[] policyTermArray = {"15", "20", "25", "30"};
                    ArrayAdapter<String> policyTermAdapter = new ArrayAdapter<>(
                            getApplicationContext(), R.layout.spinner_item, policyTermArray);
                    spnrPolicyTerm.setAdapter(policyTermAdapter);
                    //spnrPolicyTerm.setEnabled(false);
                }
                if (!premPayingTerm.equals("") && !payoutPeriod.equals("") && !premPayingTerm.equalsIgnoreCase("Select")) {
                    edt_bi_smart_guaranteed_saving_plan_Actual_policyterm.setText(String.valueOf(Integer.parseInt(premPayingTerm) + Integer.parseInt(payoutPeriod) + 1));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnrPolicyTerm.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                premPayingTerm = spnrPremPayingTerm.getSelectedItem().toString();
                payoutPeriod = spnrPolicyTerm.getSelectedItem().toString();
                if (!premPayingTerm.equals("") && !payoutPeriod.equals("") && !premPayingTerm.equalsIgnoreCase("Select")) {
                    edt_bi_smart_guaranteed_saving_plan_Actual_policyterm.setText(String.valueOf(Integer.parseInt(premPayingTerm) + Integer.parseInt(payoutPeriod) + 1));
                }
                if (premPayingTerm.equalsIgnoreCase("Select")) {
                    commonMethods.showMessageDialog(context,"Please select Premium Paying Term ");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnr_income_plan.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                str_income_plan = spnr_income_plan.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spnr_bi_smart_guaranteed_saving_plan_life_assured_title
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long id) {

                        if (position > 0) {
                            lifeAssured_Title = spnr_bi_smart_guaranteed_saving_plan_life_assured_title
                                    .getSelectedItem().toString();
                            commonMethods.setFocusable(edt_bi_smart_guaranteed_saving_plan_life_assured_first_name);

                            edt_bi_smart_guaranteed_saving_plan_life_assured_first_name
                                    .requestFocus();
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {


                    }
                });


        spnr_bi_money_back_gold_proposer_title
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long id) {

                        if (position > 0) {
                            proposer_Title = spnr_bi_money_back_gold_proposer_title
                                    .getSelectedItem().toString();


                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {


                    }
                });
        btnback.setOnClickListener(arg0 -> {
            setResult(RESULT_OK);
            finish();
        });
        btnSubmit.setOnClickListener(arg0 -> {
            inputVal = new StringBuilder();
            retVal = new StringBuilder();
            bussIll = new StringBuilder();


            proposer_First_Name = edt_bi_money_back_gold_proposer_first_name
                    .getText().toString().trim();
            proposer_Middle_Name = edt_bi_money_back_gold_proposer_middle_name
                    .getText().toString().trim();
            proposer_Last_Name = edt_bi_money_back_gold_proposer_last_name
                    .getText().toString().trim();

            gender_proposer = spinnerProposerGender.getSelectedItem().toString();
            gender = spnr_Gender.getSelectedItem().toString();

            name_of_proposer = proposer_Title + " " + proposer_First_Name
                    + " " + proposer_Middle_Name + " " + proposer_Last_Name;


            lifeAssured_First_Name = edt_bi_smart_guaranteed_saving_plan_life_assured_first_name
                    .getText().toString();
            lifeAssured_Middle_Name = edt_bi_smart_guaranteed_saving_plan_life_assured_middle_name
                    .getText().toString();
            lifeAssured_Last_Name = edt_bi_smart_guaranteed_saving_plan_life_assured_last_name
                    .getText().toString();

            name_of_life_assured = lifeAssured_Title + " "
                    + lifeAssured_First_Name + " "
                    + lifeAssured_Middle_Name + " " + lifeAssured_Last_Name;

            mobileNo = edt_proposerdetail_basicdetail_contact_no.getText()
                    .toString();
            emailId = edt_proposerdetail_basicdetail_Email_id.getText()
                    .toString();
            ConfirmEmailId = edt_proposerdetail_basicdetail_ConfirmEmail_id
                    .getText().toString();

            // calculate effective premium
            if (valLifeAssuredProposerDetail() && valDob()
                    && valBasicDetail() && valPremiumAmt() && valDoYouBackdate() && valBackdate()
                    && TrueBackdate() && valMaturityAge()) {
                Date();

                if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {
                    proposer_Title = "";
                    proposer_First_Name = "";
                    proposer_Middle_Name = "";
                    proposer_Last_Name = "";
                    name_of_proposer = "";
                    proposer_date_of_birth = "";
                }
                boolean flag_summasurred = addListenerOnSubmit();
                if (flag_summasurred) {
                    getInput(smartPlatinaPlusBean);

                    if (needAnalysis_flag == 0) {
                        Intent i = new Intent(BI_SmartPlatinaPlusActivity.this,
                                Success.class);
                        output = retVal.toString();
                        basicprem = prsObj.parseXmlTag(output, "InstallmentPremWithoutTax");
                        basicprem = commonForAllProd.getRoundUp(commonForAllProd
                                .getStringWithout_E(Double.valueOf(basicprem
                                        .equals("") ? "0" : basicprem)));

                        basicpremfirstyr = prsObj.parseXmlTag(output, "FyInstallmentPremWithTax");
                        basicpremfirstyr = commonForAllProd.getRoundUp(commonForAllProd
                                .getStringWithout_E(Double.valueOf(basicpremfirstyr
                                        .equals("") ? "0" : basicpremfirstyr)));

                        i.putExtra("op", "Premium Before Applicable Taxes : Rs. " + currencyFormat.format(Double.parseDouble(basicprem)));
                        i.putExtra("op3", "Premium with Applicable Taxes 1st year : Rs. " + currencyFormat.format(Double.parseDouble(basicpremfirstyr)));

                        basicpremsecondyr = prsObj.parseXmlTag(output, "SyInstallmentPremWithTax");
                        basicpremsecondyr = commonForAllProd.getRoundUp(commonForAllProd
                                .getStringWithout_E(Double.valueOf(basicpremsecondyr
                                        .equals("") ? "0" : basicpremsecondyr)));
                        basicCoverSumAssured = (int) Double.parseDouble(prsObj.parseXmlTag(output, "SumAssured")) + "";

                        System.out.println("basicpremsecondyr = " + basicpremsecondyr);
                        System.out.println("basicCoverSumAssured = " + basicCoverSumAssured);
                        i.putExtra("op4", "Second Year Onwards Premium with Applicable Tax is Rs."
                                + basicpremsecondyr);
                        i.putExtra("op5", "Sum Assured is Rs." + currencyFormat.format(Double
                                .parseDouble(basicCoverSumAssured)));

                        i.putExtra("header", getString(R.string.sbi_life_smart_platina_plus));
                        i.putExtra("header1", "(UIN:" + getString(R.string.sbi_life_smart_platina_plus_uin) + ")");
                        startActivity(i);

                    } else
                        Dialog();
                }
            }
        });
        rb_proposerdetail_personaldetail_backdating_yes
                .setOnCheckedChangeListener((buttonView, isChecked) -> {

                    if (isChecked) {
                        proposer_Backdating_WishToBackDate_Policy = "y";
                        ll_backdating1.setVisibility(View.VISIBLE);
                        btn_proposerdetail_personaldetail_backdatingdate
                                .setText("Select Date");
                        proposer_Backdating_BackDate = "";

                        rb_proposerdetail_personaldetail_backdating_yes
                                .setFocusable(false);
                        commonMethods.clearFocusable(rb_proposerdetail_personaldetail_backdating_yes);
                        commonMethods.setFocusable(btn_proposerdetail_personaldetail_backdatingdate);
                        btn_proposerdetail_personaldetail_backdatingdate
                                .requestFocus();

                    }
                });

        rb_proposerdetail_personaldetail_backdating_no
                .setOnCheckedChangeListener((buttonView, isChecked) -> {

                    if (isChecked) {

                        if (!proposer_Backdating_BackDate.equalsIgnoreCase("")) {
                            btn_bi_smart_guaranteed_saving_plan_life_assured_date
                                    .setText("Select Date");
                            lifeAssured_date_of_birth = "";
                        }

                        proposer_Backdating_WishToBackDate_Policy = "n";
                        proposer_Backdating_BackDate = "";
                        // setDefaultDate();
                        ll_backdating1.setVisibility(View.GONE);

                        spnr_Age
                                .setSelection(
                                        getIndex(spnr_Age, lifeAssuredAge),
                                        false);
                        valAge();
                        rb_proposerdetail_personaldetail_backdating_yes
                                .setFocusable(false);

                        commonMethods.clearFocusable(rb_proposerdetail_personaldetail_backdating_no);
                        commonMethods.clearFocusable(rb_proposerdetail_personaldetail_backdating_yes);


                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGNATURE_ACTIVITY) {
            if (resultCode == RESULT_OK) {

                Bundle bundle = data.getExtras();
                String status = bundle.getString("status");
                if (status != null && status.equalsIgnoreCase("done")) {
                    Toast toast = Toast
                            .makeText(this, "Signature capture successful!",
                                    Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 105, 50);
                    toast.show();
                    if (latestImage.equalsIgnoreCase("proposer")) {
                        Ibtn_signatureofPolicyHolders
                                .setImageBitmap(ProposerCaptureSignature.scaled);
                        Bitmap signature = ProposerCaptureSignature.scaled;
                        if (signature != null) {
                            ByteArrayOutputStream out = new ByteArrayOutputStream();
                            signature.compress(Bitmap.CompressFormat.PNG, 100,
                                    out);
                            byte[] signByteArray = out.toByteArray();
                            proposer_sign = Base64.encodeToString(
                                    signByteArray, Base64.DEFAULT);
                        }
                    } else if (latestImage.equalsIgnoreCase("agent")) {
                        Ibtn_signatureofMarketing
                                .setImageBitmap(CaptureSignature.scaled);
                        Bitmap signature = CaptureSignature.scaled;
                        if (signature != null) {
                            ByteArrayOutputStream out = new ByteArrayOutputStream();
                            signature.compress(Bitmap.CompressFormat.PNG, 100,
                                    out);
                            byte[] signByteArray = out.toByteArray();
                            agent_sign = Base64.encodeToString(signByteArray,
                                    Base64.DEFAULT);
                        }

                    }

                }
            }
        } else if (requestCode == 3) {
            if (resultCode == RESULT_OK) {
                if (Check.equals("Photo")) {

                    File Photo = commonMethods.galleryAddPic(context);
                    Bitmap bmp = BitmapFactory.decodeFile(Photo
                            .getAbsolutePath());

                    Bitmap b = null;
                    Uri imageUri = Uri.fromFile(new File(Photo.toString()));
                    try {
                        b = MediaStore.Images.Media.getBitmap(

                                getContentResolver(), imageUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Bitmap mFaceBitmap = b != null ? b.copy(Bitmap.Config.RGB_565, true) : null;
                    assert b != null;
                    b.recycle();
                    if (mFaceBitmap != null) {
                        Bitmap scaled = Bitmap.createScaledBitmap(bmp, 230,
                                200, true);
                        photoBitmap = scaled;
                        imageButtonProposerPhotograph
                                .setImageBitmap(scaled);
                    } else {
                        photoBitmap = null;
                    }
                }
            }
        }
        /* end */
    }

    @SuppressLint("SetTextI18n")
    private void Dialog() {

        final Dialog d = new Dialog(this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.BLACK));

        d.setContentView(R.layout.layout_smartplatinaplus_bi_grid);

        TextView tv_proposername =  d
                .findViewById(R.id.tv_smart_guaranteed_saving_plan_proposername);


        TextView tv_smart_platina_plus_distribution_channel =  d
                .findViewById(R.id.tv_smart_platina_plus_distribution_channel);

        TextView tv_bi_smart_guaranteed_saving_plan_life_assured_is_staff_or_not =  d
                .findViewById(R.id.tv_bi_smart_guaranteed_saving_plan_life_assured_is_staff_or_not);
        if (cb_staffdisc.isChecked()) {
            tv_bi_smart_guaranteed_saving_plan_life_assured_is_staff_or_not.setText("Yes");
        } else {
            tv_bi_smart_guaranteed_saving_plan_life_assured_is_staff_or_not.setText("No");
        }


        tv_smart_platina_plus_distribution_channel.setText(userType);


        TextView tv_proposal_number =  d
                .findViewById(R.id.tv_smart_guaranteed_saving_plan_proposal_number);


        TextView tv_bi_smart_guaranteed_saving_plan_proposer_name =  d
                .findViewById(R.id.tv_bi_smart_guaranteed_saving_plan_proposer_name);
        TextView tv_bi_smart_guaranteed_saving_plan_proposer_age =  d
                .findViewById(R.id.tv_bi_smart_guaranteed_saving_plan_proposer_age);
        TextView tv_bi_smart_guaranteed_saving_plan_proposer_gender =  d
                .findViewById(R.id.tv_bi_smart_guaranteed_saving_plan_proposer_gender);


        TextView tv_life_assured_name =  d
                .findViewById(R.id.tv_bi_smart_guaranteed_saving_plan_life_assured_name);

        TextView tv_life_age_at_entry =  d
                .findViewById(R.id.tv_bi_smart_guaranteed_saving_plan_life_assured_age);

        TextView tv_life_assured_gender =  d
                .findViewById(R.id.tv_bi_smart_guaranteed_saving_plan_life_assured_gender);

        TextView tv_premPaymentfrequency =  d
                .findViewById(R.id.tv_bi_smart_guaranteed_saving_plan_life_assured_paying_mode);

        TextView tv_GuaranteedPayoutFrequency =  d
                .findViewById(R.id.tv_GuaranteedPayoutFrequency);

        TextView tv_basic_cover_term =  d
                .findViewById(R.id.tv_bi_smart_guaranteed_saving_plan_term_basic_cover);


        TextView tv_income_plan =  d
                .findViewById(R.id.tv_income_plan);

        TextView tv_basic_cover_premPayment_term =  d
                .findViewById(R.id.tv_bi_smart_guaranteed_saving_plan_premium_paying_term_basic_cover);


        TextView tv_bi_smart_guaranteed_saving_plan_amount_of_installment_premium =  d
                .findViewById(R.id.tv_bi_smart_guaranteed_saving_plan_amount_of_installment_premium);


        TextView tv_basic_cover_sum_assured =  d
                .findViewById(R.id.tv_bi_smart_guaranteed_saving_plan_sum_assured_basic_cover);
        TextView tv_sum_assured_onDeath =  d
                .findViewById(R.id.tv_sum_assured_onDeath);

        TextView tv_basic_cover_yearly_premium =  d
                .findViewById(R.id.tv_bi_smart_guaranteed_saving_plan_yearly_premium_basic_cover);

        TextView tv_basic_prem_without_st =  d
                .findViewById(R.id.tv_basic_prem_without_st);
        TextView tv_basic_prem_with_st =  d
                .findViewById(R.id.tv_basic_prem_with_st);

        TextView tv_premium_detail_basic_premium_first_year =  d
                .findViewById(R.id.tv_bi_smart_guaranteed_saving_plan_basic_prem_first_year);
        TextView tv_bi_smart_guaranteed_saving_plan_premium_with_service_tax_first_year =  d
                .findViewById(R.id.tv_bi_smart_guaranteed_saving_plan_premium_with_service_tax_first_year);

        TextView tv_bi_smart_guaranteed_saving_plan_basic_prem_second_year =  d
                .findViewById(R.id.tv_bi_smart_guaranteed_saving_plan_basic_prem_second_year);
        TextView tv_bi_smart_guaranteed_saving_plan_premium_with_service_tax_second_year =  d
                .findViewById(R.id.tv_bi_smart_guaranteed_saving_plan_premium_with_service_tax_second_year);

        TextView tv_rate_of_applicable_taxes =  d
                .findViewById(R.id.tv_rate_of_applicable_taxes);

        TextView tv_guaranteedIncome =  d
                .findViewById(R.id.tv_guaranteedIncome);

        TextView tv_payoutPeriod =  d
                .findViewById(R.id.tv_payoutPeriod);


        GridView gv_userinfo = (GridView) d
                .findViewById(R.id.gv_smart_guaranteed_saving_plan_userinfo);

        gv_userinfo.setVerticalScrollBarEnabled(true);
        gv_userinfo.setSmoothScrollbarEnabled(true);

        final EditText edt_Policyholderplace = d
                .findViewById(R.id.edt_Policyholderplace);


        final TextView tvCbStatementFirst = d
                .findViewById(R.id.tvCbStatementFirst);

        final TextView txt_proposer_name_need_analysis = d
                .findViewById(R.id.txt_proposer_name_need_analysis);

        final CheckBox cb_statement = d
                .findViewById(R.id.cb_statement);
        cb_statement.setChecked(false);

        final CheckBox cb_statement_need_analysis = d
                .findViewById(R.id.cb_statement_need_analysis);
        cb_statement_need_analysis.setChecked(true);


        TableRow tr_second_year = d
                .findViewById(R.id.tr_second_year);

        /* Need Analysis */

        final CheckBox checkboxAgentStatement = d
                .findViewById(R.id.checkboxAgentStatement);
        checkboxAgentStatement.setChecked(false);


        TableRow tr_need_analysis = d
                .findViewById(R.id.tr_need_analysis);

        input = inputVal.toString();
        output = retVal.toString();
        
        Button btn_proceed = d
                .findViewById(R.id.btn_smart_guaranteed_saving_plan_proceed);

        btn_MarketingOfficalDate = d
                .findViewById(R.id.btn_MarketingOfficalDate);
        btn_PolicyholderDate = d
                .findViewById(R.id.btn_PolicyholderDate);

        Ibtn_signatureofMarketing = (ImageButton) d
                .findViewById(R.id.Ibtn_signatureofMarketing);
        Ibtn_signatureofPolicyHolders = (ImageButton) d
                .findViewById(R.id.Ibtn_signatureofPolicyHolders);
        Calendar calender = Calendar.getInstance();
        int Year = calender.get(Calendar.YEAR);
        int Month = calender.get(Calendar.MONTH);
        int Day = calender.get(Calendar.DAY_OF_MONTH);

        list_data.clear();
        if (!proposer_sign.equals("")) {
            String flg_needAnalyis = "";
            if (flg_needAnalyis.equals("1")) {
                tr_need_analysis.setVisibility(View.VISIBLE);
            } else {
                cb_statement_need_analysis.setChecked(true);
                tr_need_analysis.setVisibility(View.GONE);
            }
        }

        ageAtEntry = prsObj.parseXmlTag(input, "age");
        gender = prsObj.parseXmlTag(input, "gender");
        strProposerAge = prsObj.parseXmlTag(input, "strProposerAge");
        lifeAssuredAge = prsObj.parseXmlTag(input, "lifeAssuredAge");
        gender_proposer = prsObj.parseXmlTag(input, "gender_proposer");


        if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {

            tvCbStatementFirst
                    .setText("I, "
                            + name_of_life_assured
                            + ",having received the information with respect to the above, have understood the above statement before entering into a contract.");

            txt_proposer_name_need_analysis
                    .setText("I, "
                            + name_of_life_assured
                            + ", have undergone the Need Analysis and after having reviewed the SBI Life Product options, I have opted for 'SBI LIFE - " +
                            getString(R.string.sbi_life_smart_platina_plus));


            tv_proposername.setText(name_of_life_assured);
            tv_life_assured_name.setText(name_of_life_assured);
            tv_life_age_at_entry.setText(ageAtEntry + " Years");
            tv_life_assured_gender.setText(gender);
            tv_bi_smart_guaranteed_saving_plan_proposer_name.setText(name_of_life_assured);
            tv_bi_smart_guaranteed_saving_plan_proposer_age.setText(ageAtEntry + " Years");
            tv_bi_smart_guaranteed_saving_plan_proposer_gender.setText(gender);

        } else if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("n")) {

            tvCbStatementFirst
                    .setText("I, "
                            + name_of_proposer
                            + ",having received the information with respect to the above, have understood the above statement before entering into a contract.");

            txt_proposer_name_need_analysis
                    .setText("I, "
                            + name_of_proposer
                            + ", have undergone the Need Analysis and after having reviewed the SBI Life Product options, I have opted for 'SBI LIFE - " +
                            getString(R.string.sbi_life_smart_platina_plus));

            tv_proposername.setText(name_of_proposer);
            tv_life_assured_name.setText(name_of_life_assured);
            tv_life_age_at_entry.setText(ageAtEntry + " Years");
            tv_life_assured_gender.setText(gender);
            tv_bi_smart_guaranteed_saving_plan_proposer_name.setText(name_of_proposer);
            tv_bi_smart_guaranteed_saving_plan_proposer_age.setText(strProposerAge + " Years");
            tv_bi_smart_guaranteed_saving_plan_proposer_gender.setText(gender_proposer);

        }
        tv_proposal_number.setText(QuatationNumber);
        TextView textviewAGentStatement = d.findViewById(R.id.textviewAGentStatement);
        textviewAGentStatement.setText("I, " + commonMethods.getUserName(context) + " have explained the premiums and benefits under the product fully to the prospect/policyholder.");

        edt_Policyholderplace.setText(place2);
        if (!date2.equals("")) {
            btn_PolicyholderDate.setText(getDate(date2));
        } else {
            date2 = commonMethods.getMMDDYYYYDatabaseDate(getCurrentDate());
            btn_PolicyholderDate.setText(getCurrentDate());
        }

        if (!date1.equals("")) {
            btn_MarketingOfficalDate.setText(getDate(date1));
        } else {
            date1 = commonMethods.getMMDDYYYYDatabaseDate(getCurrentDate());
            btn_MarketingOfficalDate.setText(getCurrentDate());
        }

        if (agent_sign != null && !agent_sign.equals("")) {
            cb_statement.setChecked(true);
            byte[] signByteArray = Base64.decode(agent_sign, 0);
            Bitmap bitmap = BitmapFactory.decodeByteArray(signByteArray, 0,
                    signByteArray.length);
            Ibtn_signatureofMarketing.setImageBitmap(bitmap);
        }

        if (proposer_sign != null && !proposer_sign.equals("")) {
            byte[] signByteArray = Base64.decode(proposer_sign, 0);
            Bitmap bitmap = BitmapFactory.decodeByteArray(signByteArray, 0,
                    signByteArray.length);
            Ibtn_signatureofPolicyHolders.setImageBitmap(bitmap);
        }

        imageButtonProposerPhotograph = d
                .findViewById(R.id.imageButtonProposerPhotograph);
        imageButtonProposerPhotograph
                .setOnClickListener(view -> {
                    Check = "Photo";
                    commonMethods.windowmessage(context, "_cust1Photo.jpg");
                });

        Ibtn_signatureofMarketing
                .setOnClickListener(view -> {

                    if (cb_statement.isChecked()
                            && cb_statement_need_analysis.isChecked()
                            && checkboxAgentStatement.isChecked()) {
                        latestImage = "agent";
                        commonMethods.windowMessageSign(context, latestImage);
                    } else {
                        commonMethods.dialogWarning(context, "Please Tick on I Agree Clause ", true);
                        commonMethods.setFocusable(cb_statement);
                        cb_statement.requestFocus();
                    }

                });

        Ibtn_signatureofPolicyHolders
                .setOnClickListener(view -> {
                    if (cb_statement.isChecked()
                            && cb_statement_need_analysis.isChecked()
                            && checkboxAgentStatement.isChecked()) {
                        latestImage = "proposer";
                        commonMethods.windowmessageProposersgin(context,
                                NeedAnalysisActivity.URN_NO + "_cust1sign");
                    } else {
                        commonMethods.dialogWarning(context, "Please Tick on I Agree Clause ", true);
                        commonMethods.setFocusable(cb_statement);
                        cb_statement.requestFocus();
                    }
                });

        LinearLayout linearLayoutProposerSignature = d
                .findViewById(R.id.linearLayoutProposerSignature);
        LinearLayout linearLayoutmarketingOfficerSignature = d
                .findViewById(R.id.linearLayoutmarketingOfficerSignature);

        if (bankUserType.equalsIgnoreCase("Y")) {
            linearLayoutProposerSignature.setVisibility(View.GONE);
            linearLayoutmarketingOfficerSignature.setVisibility(View.GONE);
        } else {
            linearLayoutProposerSignature.setVisibility(View.VISIBLE);
            linearLayoutmarketingOfficerSignature.setVisibility(View.VISIBLE);
        }

        if (photoBitmap != null) {
            imageButtonProposerPhotograph
                    .setImageBitmap(photoBitmap);
        }

        final RadioButton radioButtonTrasactionModeManual = d
                .findViewById(R.id.radioButtonTrasactionModeManual);
        final RadioButton radioButtonTrasactionModeParivartan = d
                .findViewById(R.id.radioButtonTrasactionModeParivartan);
        final LinearLayout linearlayoutTrasactionModeParivartan = d
                .findViewById(R.id.linearlayoutTrasactionModeParivartan);

        radioButtonTrasactionModeParivartan
                .setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        transactionMode = "Parivartan";
                        linearlayoutTrasactionModeParivartan
                                .setVisibility(View.VISIBLE);
                    } else {
                        linearlayoutTrasactionModeParivartan
                                .setVisibility(View.GONE);

                        String customerPhotoName = NeedAnalysisActivity.URN_NO
                                + "_cust1Photo.jpg";
                        File customerPhotoFile = mStorageUtils.createFileToAppSpecificDir(context,
                                customerPhotoName);
                        if (customerPhotoFile.exists()) {
                            customerPhotoFile.delete();
                        }

                        photoBitmap = null;
                        imageButtonProposerPhotograph
                                .setImageDrawable(getResources()
                                        .getDrawable(
                                                R.drawable.focus_imagebutton_photo));
                    }
                });

        radioButtonTrasactionModeManual
                .setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        transactionMode = "Manual";
                    } else {

                    }
                });
        /* end */

        if (transactionMode.equalsIgnoreCase("Manual")) {
            radioButtonTrasactionModeManual.setChecked(true);
        } else if (transactionMode.equalsIgnoreCase("Parivartan")) {
            radioButtonTrasactionModeParivartan.setChecked(true);
        }

        if (!TextUtils.isEmpty(place2)) {
            edt_Policyholderplace.setText(place2);
        }

        btn_proceed.setOnClickListener(v -> {
            place1 = "";
            place2 = edt_Policyholderplace.getText().toString();
            date1 = btn_MarketingOfficalDate.getText().toString();
            date2 = btn_PolicyholderDate.getText().toString();

            if ((!name_of_proposer.equals("") || !name_of_life_assured
                    .equals(""))
                    && !place2.equals("")
                    && !date1.equals("Select Date")
                    && (!date2.equals("Select Date") || bankUserType
                    .equalsIgnoreCase("Y"))
                    && (!agent_sign.equals("") || bankUserType
                    .equalsIgnoreCase("Y"))
                    && (!proposer_sign.equals("") || bankUserType
                    .equalsIgnoreCase("Y"))
                    && (cb_statement.isChecked())
                    && checkboxAgentStatement.isChecked()
                    && (((photoBitmap != null) && radioButtonTrasactionModeParivartan
                    .isChecked()) || radioButtonTrasactionModeManual
                    .isChecked())) {
                NeedAnalysisActivity.str_need_analysis = "";
                String productCode = getString(R.string.sbi_life_smart_future_choices_code);


                na_cbi_bean = new NA_CBI_bean(
                        QuatationNumber,
                        agentcode,
                        "",
                        userType,
                        "",
                        lifeAssured_Title,
                        lifeAssured_First_Name,
                        lifeAssured_Middle_Name,
                        lifeAssured_Last_Name,
                        planName,
                        commonForAllProd.getRound(commonForAllProd.getStringWithout_E(Double
                                .parseDouble((basicCoverSumAssured.equals("") || basicCoverSumAssured == null) ? "0"
                                        : basicCoverSumAssured))),
                        commonForAllProd.getRound(commonForAllProd.getStringWithout_E(Double
                                .valueOf(basicprem))),
                        emailId, mobileNo, agentEmail, agentMobile,
                        na_input, na_output, smartPlatinaPlusBean.getPremfreq(), Integer
                        .parseInt(policyTerm), smartPlatinaPlusBean.getPremPayingTerm(), productCode,
                        commonMethods.getDDMMYYYYDate(lifeAssured_date_of_birth),
                        commonMethods.getDDMMYYYYDate(proposer_date_of_birth), inputVal
                        .toString(), retVal.toString().replace(
                        bussIll.toString(), ""));

                name_of_person = name_of_life_assured;
                if (radioButtonTrasactionModeParivartan.isChecked()) {
                    transactionMode = "Parivartan";
                } else if (radioButtonTrasactionModeManual.isChecked()) {
                    transactionMode = "Manual";
                }
                db.AddNeedAnalysisDashboardDetails(new ProductBIBean(
                        "",
                        QuatationNumber,
                        planName,
                        getCurrentDate(),
                        mobileNo,
                        getCurrentDate(),
                        db.GetUserCode(),
                        emailId,
                        "",
                        "",
                        agentcode,
                        "",
                        userType,
                        "",
                        lifeAssured_Title,
                        lifeAssured_First_Name,
                        lifeAssured_Middle_Name,
                        lifeAssured_Last_Name,
                        commonForAllProd.getRound(commonForAllProd.getStringWithout_E(Double
                                .valueOf((basicCoverSumAssured.equals("") || basicCoverSumAssured == null) ? "0"
                                        : basicCoverSumAssured))),
                        commonForAllProd.getRound(commonForAllProd.getStringWithout_E(Double
                                .valueOf(basicprem))),
                        agentEmail, agentMobile, na_input, na_output,
                        smartPlatinaPlusBean.getPremfreq(), Integer.parseInt(policyTerm),
                        smartPlatinaPlusBean.getPremPayingTerm(),
                        productCode, commonMethods.getDDMMYYYYDate(lifeAssured_date_of_birth),
                        commonMethods.getDDMMYYYYDate(proposer_date_of_birth), "", transactionMode, inputVal
                        .toString(), retVal.toString().replace(
                        bussIll.toString(), "")));
                /* end */

                createPdfNew();

                NABIObj.serviceHit(BI_SmartPlatinaPlusActivity.this,
                        na_cbi_bean, newFile, needAnalysispath.getPath(),
                        mypath.getPath(), name_of_person, QuatationNumber, transactionMode);
                d.dismiss();

            } else {

                if (proposer_sign.equals("")
                        && !bankUserType.equalsIgnoreCase("Y")) {
                    commonMethods.dialogWarning(context, "Please Make Signature for Proposer ", true);
                    commonMethods.setFocusable(Ibtn_signatureofPolicyHolders);
                    Ibtn_signatureofPolicyHolders.requestFocus();
                } else if (place2.equals("")) {
                    commonMethods.dialogWarning(context, "Please Fill Place Detail", true);
                    commonMethods.setFocusable(edt_Policyholderplace);
                    edt_Policyholderplace.requestFocus();

                } else if (agent_sign.equals("")
                        && !bankUserType.equalsIgnoreCase("Y")) {
                    commonMethods.dialogWarning(context, "Please Make Signature for Sales Representative",
                            true);
                    commonMethods.setFocusable(Ibtn_signatureofMarketing);
                    Ibtn_signatureofMarketing.requestFocus();
                } else if (!cb_statement.isChecked()) {
                    commonMethods.dialogWarning(context, "Please Tick on I Agree Clause ", true);
                    commonMethods.setFocusable(cb_statement);
                    cb_statement.requestFocus();
                } else if (!checkboxAgentStatement.isChecked()) {
                    commonMethods.dialogWarning(context, "Please Tick on I Agree Clause ", true);
                    commonMethods.setFocusable(checkboxAgentStatement);
                    checkboxAgentStatement.requestFocus();
                } else if (!radioButtonTrasactionModeParivartan.isChecked()
                        && !radioButtonTrasactionModeManual.isChecked()) {
                    commonMethods.dialogWarning(context, "Please Select Transaction Mode", true);
                    commonMethods.setFocusable(linearlayoutTrasactionModeParivartan);
                    linearlayoutTrasactionModeParivartan.requestFocus();
                } else if (photoBitmap == null) {
                    commonMethods.dialogWarning(context, "Please Capture the Photo", true);
                    commonMethods.setFocusable(imageButtonProposerPhotograph);
                    imageButtonProposerPhotograph
                            .requestFocus();
                }
            }


        });

        btn_PolicyholderDate.setOnClickListener(v -> {
            DIALOG_ID = 2;
            showDateDialog();
        });

        btn_MarketingOfficalDate.setOnClickListener(v -> {
            DIALOG_ID = 3;
            showDateDialog();
        });
        

        isStaff = prsObj.parseXmlTag(input, "isStaff");

        premPayingMode = prsObj.parseXmlTag(input, "premFreq");
        tv_premPaymentfrequency.setText(premPayingMode);

        strGuaranteedPayoutFrequency = prsObj.parseXmlTag(input, "strGuaranteedPayoutFrequency");
        tv_GuaranteedPayoutFrequency.setText(strGuaranteedPayoutFrequency);

        premPayingTerm = prsObj.parseXmlTag(input, "premPayingTerm");
        tv_basic_cover_premPayment_term.setText(premPayingTerm + " Years");

        tv_basic_cover_term.setText(prsObj.parseXmlTag(input, "payoutPeriod") + " Years");

        str_income_plan = prsObj.parseXmlTag(input, "strIncomePlan");
        tv_income_plan.setText(str_income_plan);

        String basicCoverYearlyPremium = ((int) Double.parseDouble(prsObj.parseXmlTag(input, "premiumAmount"))) + "";
        tv_basic_cover_yearly_premium.setText("" + basicCoverYearlyPremium);

        basicCoverSumAssured = (int) Double.parseDouble(prsObj.parseXmlTag(output, "SumAssured")) + "";
        tv_basic_cover_sum_assured.setText("" + basicCoverSumAssured);


        tv_sum_assured_onDeath.setText("" + prsObj.parseXmlTag(output, "SumAssuredOnDeath"));

        tv_rate_of_applicable_taxes.setText("4.5% in the 1st policy year and 2.25% from 2nd policy year onwards");
        tv_payoutPeriod.setText(prsObj.parseXmlTag(input, "policyTerm") + " Years");

        tv_guaranteedIncome.setText(prsObj.parseXmlTag(output, "GuaranteedIncome") + "");

        tr_second_year.setVisibility(View.VISIBLE);

        tv_basic_prem_without_st.setText(prsObj.parseXmlTag(output, "InstallmentPremWithoutTax"));

        basicprem = prsObj.parseXmlTag(output, "InstallmentPremWithoutTax");
        basicprem = commonForAllProd.getRoundUp(commonForAllProd
                .getStringWithout_E(Double.valueOf(basicprem
                        .equals("") ? "0" : basicprem)));

        tv_basic_prem_with_st.setText(basicprem);
        tv_bi_smart_guaranteed_saving_plan_amount_of_installment_premium.setText(prsObj.parseXmlTag(output, "InstallmentPremWithoutTax"));


        String premiumDetailFYServiceTax = prsObj.parseXmlTag(output, "FyInstallmentPremWithTax");
        tv_premium_detail_basic_premium_first_year.setText("" + premiumDetailFYServiceTax);

        basicpremfirstyr = prsObj.parseXmlTag(output, "FyInstallmentPremWithTax");
        basicpremfirstyr = commonForAllProd.getRoundUp(commonForAllProd
                .getStringWithout_E(Double.valueOf(basicpremfirstyr
                        .equals("") ? "0" : basicpremfirstyr)));


        tv_bi_smart_guaranteed_saving_plan_premium_with_service_tax_first_year.setText("" + basicpremfirstyr);
        String premiumDetailSYServiceTax = prsObj.parseXmlTag(output, "SyInstallmentPremWithTax");
        tv_bi_smart_guaranteed_saving_plan_basic_prem_second_year.setText("" + premiumDetailSYServiceTax);


        basicpremsecondyr = prsObj.parseXmlTag(output, "SyInstallmentPremWithTax");
        basicpremsecondyr = commonForAllProd.getRoundUp(commonForAllProd
                .getStringWithout_E(Double.valueOf(basicpremsecondyr
                        .equals("") ? "0" : basicpremsecondyr)));

        tv_bi_smart_guaranteed_saving_plan_premium_with_service_tax_second_year.setText(basicpremsecondyr);

        TextView tv_Company_policy_surrender_dec =  d
                .findViewById(R.id.tv_Company_policy_surrender_dec);

        String company_policy_surrender_dec = "Your SBI Life-Smart Platina Plus (UIN - 111N133V01) is a "
                + " premium policy, for which your first year yearly Premium is Rs "
                + basicpremfirstyr
                + " .Your Policy Term is "
                + policyTerm
                + " years"
                + " .Your Premium Paying Term is "
                + premPayingTerm
                + " years"
                + " and Basic Sum Assured is Rs. " +

                basicCoverSumAssured;

        tv_Company_policy_surrender_dec.setText(company_policy_surrender_dec);


        for (int i = 1; i <= Integer.parseInt(policyTerm); i++) {
            String end_of_year = prsObj.parseXmlTag(output, "PolicyYr" + i + "");
            String yearly_basic_premium = prsObj.parseXmlTag(output, "AnnualizePrem" + i + "");
            if (!yearly_basic_premium.equalsIgnoreCase("-")) {

                yearly_basic_premium = getformatedThousandString(Integer.parseInt(commonForAllProd.getRound(commonForAllProd.getStringWithout_E(Double
                        .valueOf(((prsObj.parseXmlTag(output,
                                "AnnualizePrem" + i + "") == null) || (prsObj
                                .parseXmlTag(output,
                                        "AnnualizePrem" + i + "")
                                .equals(""))) ? "0" : prsObj
                                .parseXmlTag(output,
                                        "AnnualizePrem" + i + "")))
                        + "")));

            }

            String SurvivalBenefit = getformatedThousandString(Integer.parseInt(commonForAllProd.getRound(commonForAllProd.getStringWithout_E(Double
                    .valueOf(((prsObj.parseXmlTag(output,
                            "SurvivalBenefit" + i + "") == null) || (prsObj
                            .parseXmlTag(output,
                                    "SurvivalBenefit" + i + "")
                            .equals(""))) ? "0" : prsObj
                            .parseXmlTag(output,
                                    "SurvivalBenefit" + i + "")))
                    + "")));


            String guaranteed_maturity_benefit = getformatedThousandString(Integer.parseInt(commonForAllProd.getRound(commonForAllProd.getStringWithout_E(Double
                    .valueOf(((prsObj.parseXmlTag(output,
                            "MaturityBenefit" + i + "") == null) || (prsObj
                            .parseXmlTag(output,
                                    "MaturityBenefit" + i + "")
                            .equals(""))) ? "0" : prsObj
                            .parseXmlTag(output,
                                    "MaturityBenefit" + i + "")))
                    + "")));

            String guaranteed_death_benefit = getformatedThousandString(Integer.parseInt(commonForAllProd.getRound(commonForAllProd.getStringWithout_E(Double
                    .valueOf(((prsObj.parseXmlTag(output,
                            "DeathBenefit" + i + "") == null) || (prsObj
                            .parseXmlTag(output,
                                    "DeathBenefit" + i + "")
                            .equals(""))) ? "0" : prsObj
                            .parseXmlTag(output,
                                    "DeathBenefit" + i + "")))
                    + "")));


            String guaranteed_surrender_value = getformatedThousandString(Integer.parseInt(commonForAllProd.getRound(commonForAllProd.getStringWithout_E(Double
                    .valueOf(((prsObj.parseXmlTag(output,
                            "MinGuaranteedSurrenderValue" + i + "") == null) || (prsObj
                            .parseXmlTag(output,
                                    "MinGuaranteedSurrenderValue" + i + "")
                            .equals(""))) ? "0" : prsObj
                            .parseXmlTag(output,
                                    "MinGuaranteedSurrenderValue" + i + "")))
                    + "")));


            String SpecialSurrenderValue = getformatedThousandString(Integer.parseInt(commonForAllProd.getRound(commonForAllProd.getStringWithout_E(Double
                    .valueOf(((prsObj.parseXmlTag(output,
                            "SpecialSurrenderValue" + i + "") == null) || (prsObj
                            .parseXmlTag(output,
                                    "SpecialSurrenderValue" + i + "")
                            .equals(""))) ? "0" : prsObj
                            .parseXmlTag(output,
                                    "SpecialSurrenderValue" + i + "")))
                    + "")));


            list_data.add(new M_BI_SmartPlatinaPlus_Adapter(end_of_year, yearly_basic_premium, SurvivalBenefit,
                    "0", guaranteed_maturity_benefit,
                    guaranteed_death_benefit, guaranteed_surrender_value, SpecialSurrenderValue));
        }

        Adapter_BI_SmartPlatinaPlusGrid adapter = new Adapter_BI_SmartPlatinaPlusGrid(
                this, list_data);
        gv_userinfo.setAdapter(adapter);
        GridHeight gh = new GridHeight();
        gh.getheight(gv_userinfo, policyTerm);

        d.show();

    }

    private void initialiseDate() {
        Calendar calender = Calendar.getInstance();
        mYear = calender.get(Calendar.YEAR);
        mMonth = calender.get(Calendar.MONTH);
        mDay = calender.get(Calendar.DAY_OF_MONTH);

    }

    public void Date() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        /* For DashBoard */
        String formattedDate = df.format(c.getTime());
        c.add(Calendar.DATE, 30);
    }


    // Store user input in Bean object
    protected boolean addListenerOnSubmit() {
        smartPlatinaPlusBean = new SmartPlatinaPlusBean();
        // Insert data entered by user in an object
        smartPlatinaPlusBean.setIsStaff(cb_staffdisc.isChecked() == true);
        smartPlatinaPlusBean.setAge(Integer.parseInt(spnr_Age.getSelectedItem().toString()));
        smartPlatinaPlusBean.setGender(gender);
        smartPlatinaPlusBean.setPremPayingTerm(Integer.parseInt(premPayingTerm));
        smartPlatinaPlusBean.setpayoutPeriod(Integer.parseInt(payoutPeriod));
        smartPlatinaPlusBean.setPolicyTerm(Integer.parseInt(premPayingTerm) + Integer.parseInt(payoutPeriod) + 1);
        smartPlatinaPlusBean.setAnnualPrem(Double.parseDouble(edt_premiumAmt.getText().toString()));
//			smartPlatinaPlusBean.setSumassured(Double.parseDouble(SumAssured));
        smartPlatinaPlusBean.setPremfreq(spnrPremPayingMode.getSelectedItem().toString());

        smartPlatinaPlusBean.setGuaranteedpayoutfreq(spnr_guaranteed_payout_frequency.getSelectedItem().toString());
        smartPlatinaPlusBean.setincomePlan(str_income_plan);


        return showSmartPlatinaPlusOutputPg(smartPlatinaPlusBean);
    }

    private void getInput(SmartPlatinaPlusBean smartPlatinaPlusBean) {
        inputVal = new StringBuilder();
        policyTerm = edt_bi_smart_guaranteed_saving_plan_Actual_policyterm.getText().toString();
        int age = smartPlatinaPlusBean.getAge();
        String gender = smartPlatinaPlusBean.getGender();
        double premAmount = smartPlatinaPlusBean.getAnnualPrem();
        String PremPayingMode = smartPlatinaPlusBean.getPremfreq();
        String strGuaranteedPayoutFrequency = smartPlatinaPlusBean.getGuaranteedpayoutfreq();
        int premPayingTerm = smartPlatinaPlusBean.getPremPayingTerm();
        // int policyTerm = smartPlatinaPlusBean.getPolicyTerm();
        int payoutPeriod = smartPlatinaPlusBean.getpayoutPeriod();
        String str_income_plan = smartPlatinaPlusBean.getincomePlan();
        boolean isStaff = smartPlatinaPlusBean.getIsStaff();

        inputVal.append("<?xml version='1.0' encoding='utf-8' ?><SmartPlatinaPlus>");
        inputVal.append("<LifeAssured_title>").append(lifeAssured_Title).append("</LifeAssured_title>");
        inputVal.append("<LifeAssured_firstName>").append(lifeAssured_First_Name).append("</LifeAssured_firstName>");
        inputVal.append("<LifeAssured_middleName>").append(lifeAssured_Middle_Name).append("</LifeAssured_middleName>");
        inputVal.append("<LifeAssured_lastName>").append(lifeAssured_Last_Name).append("</LifeAssured_lastName>");
        inputVal.append("<LifeAssured_DOB>").append(lifeAssured_date_of_birth).append("</LifeAssured_DOB>");
        inputVal.append("<LifeAssured_age>").append(age).append("</LifeAssured_age>");

        inputVal.append("<age>").append(age).append("</age>");
        inputVal.append("<gender>").append(gender).append("</gender>");
        inputVal.append("<gender_proposer>").append(gender_proposer).append("</gender_proposer>");
        inputVal.append("<isStaff>").append(isStaff).append("</isStaff>");
        inputVal.append("<premFreq>").append(PremPayingMode).append("</premFreq>");
        inputVal.append("<strGuaranteedPayoutFrequency>").append(strGuaranteedPayoutFrequency).append("</strGuaranteedPayoutFrequency>");
        inputVal.append("<premiumAmount>").append(premAmount).append("</premiumAmount>");
        inputVal.append("<premPayingTerm>").append(premPayingTerm).append("</premPayingTerm>");
        inputVal.append("<payoutPeriod>").append(payoutPeriod).append("</payoutPeriod>");
        inputVal.append("<policyTerm>").append(policyTerm).append("</policyTerm>");
        inputVal.append("<strIncomePlan>").append(str_income_plan).append("</strIncomePlan>");
        inputVal.append("<Product_Cat>").append(product_cateogory).append("</Product_Cat>");
        inputVal.append("<lifeAssuredAge>").append(lifeAssuredAge).append("</lifeAssuredAge>");
        inputVal.append("<ageAtEntry>").append(ageAtEntry).append("</ageAtEntry>");

        inputVal.append("<strProposerAge>").append(strProposerAge).append("</strProposerAge>");
        inputVal.append("<ProposerAge>").append(strProposerAge).append("</ProposerAge>");

        inputVal.append("<proposer_title>").append(proposer_Title).append("</proposer_title>");
        inputVal.append("<proposer_firstName>").append(proposer_First_Name).append("</proposer_firstName>");
        inputVal.append("<proposer_middleName>").append(proposer_Middle_Name).append("</proposer_middleName>");
        inputVal.append("<proposer_lastName>").append(proposer_Last_Name).append("</proposer_lastName>");
        inputVal.append("<proposer_DOB>").append(proposer_date_of_birth).append("</proposer_DOB>");
        inputVal.append("<proposer_age>").append(strProposerAge).append("</proposer_age>");
        inputVal.append("<proposer_gender>").append(gender_proposer).append("</proposer_gender>");

        inputVal.append("<product_name>").append(planName).append("</product_name>");
        /* parivartan changes */
        inputVal.append("<product_Code>").append(product_Code).append("</product_Code>");
        inputVal.append("<product_UIN>").append(product_UIN).append("</product_UIN>");
        inputVal.append("<product_cateogory>").append(product_cateogory).append("</product_cateogory>");
        inputVal.append("<product_type>").append(product_type).append("</product_type>");

        inputVal.append("<plan>").append(planName).append("</plan>");
        inputVal.append("<proposer_Is_Same_As_Life_Assured>").append(proposer_Is_Same_As_Life_Assured).append("</proposer_Is_Same_As_Life_Assured>");
        inputVal.append("<Product_Cat>").append(product_cateogory).append("</Product_Cat>");

        inputVal.append("<BIVERSION>" + "1" + "</BIVERSION>");
        //Added Tushar Kadam Kerla Applicable Tax 7-Jun-2019
        String str_kerla_discount = "N";
        if (cb_kerladisc.isChecked()) {
            str_kerla_discount = "Y";
        }
        inputVal.append("<KFC>").append(str_kerla_discount).append("</KFC>");

        inputVal.append("</SmartPlatinaPlus>");
        System.out.println("inputVal = " + inputVal.toString());

    }

    /********************************** Output starts here **********************************************************/
    public boolean showSmartPlatinaPlusOutputPg(SmartPlatinaPlusBean smartPlatinaPlusBean) {

        boolean flag_sumassured = true;
        retVal = new StringBuilder();
        SmartPlatinaPlusBusinessLogic smartPlatinaBl = new SmartPlatinaPlusBusinessLogic(smartPlatinaPlusBean);

        boolean isstaff = smartPlatinaPlusBean.getIsStaff();
        //System.out.println("isstaff " + isstaff );
        int age = smartPlatinaPlusBean.getAge();
        // 	int policyTerm = smartPlatinaPlusBean.getPolicyTerm();
        int premPayingTerm = smartPlatinaPlusBean.getPremPayingTerm();
        double annualPrem = smartPlatinaPlusBean.getAnnualPrem();
        //	double sumassured = smartPlatinaPlusBean.getSumassured();
        double InstallmentPremWithoutTax = 0, FyInstallmentPremWithTax = 0, fySeviceTax = 0, SyInstallmentPremWithTax = 0, sySeviceTax = 0,
                GuaranteedIncome = 0, AnnualizePrem = 0, SurvivalBenefit = 0, MaturityBenefit = 0, DeathBenefit = 0, TotalPrem = 0,
                MinGuaranteedSurrenderValue = 0, SpecialSurrenderValue = 0, AnnualizeSumPrem = 0, SurvivalBenefitSum = 0, BackDateinterestwithGST1 = 0, SumassuredonDeath = 0;
        //			System.out.println("FyInstallmentPremWithTax " +FyInstallmentPremWithTax);



        /*if(Ismines.equals("true"))
				{*/


        String minesOccuInterest = commonForAllProd
                .getStringWithout_E((smartPlatinaBl.getMinesOccuInterest(smartPlatinaBl.getSumAssured()) + smartPlatinaBl.getServiceTaxMines(smartPlatinaBl.getMinesOccuInterest(smartPlatinaBl.getSumAssured()))));


        //}
				/*else
				{
					MinesOccuInterest="0";
            }*/


        System.out.println("Backdate: " + BackDateinterestwithGST);
        try {

            retVal.append("<?xml version='1.0' encoding='utf-8' ?><SmartPlatinaPlus>");
            retVal.append("<errCode>0</errCode>");
            InstallmentPremWithoutTax = smartPlatinaBl.getBasicInstallmentPrem();
            FyInstallmentPremWithTax = smartPlatinaBl.getInstallmentPremFYwithTax();
            fySeviceTax = FyInstallmentPremWithTax - InstallmentPremWithoutTax;
            SyInstallmentPremWithTax = smartPlatinaBl.getInstallmentPremSYwithTax();
            sySeviceTax = SyInstallmentPremWithTax - InstallmentPremWithoutTax;
            GuaranteedIncome = smartPlatinaBl.getGuaranteedIncomeAMT(age);


            if (rb_proposerdetail_personaldetail_backdating_yes.isChecked()) {
                BackDateinterest = commonForAllProd
                        .getRoundUp(commonForAllProd.getStringWithout_E
                                (smartPlatinaBl.getBackDateInterest((InstallmentPremWithoutTax),
                                        proposer_Backdating_BackDate)));

                BackDateinterestwithGST = commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(Double.parseDouble(BackDateinterest) + (Double.parseDouble(BackDateinterest) * prop.GSTforbackdate)));
            } else {
                BackDateinterestwithGST = "0";
            }


            FyInstallmentPremWithTax = FyInstallmentPremWithTax
                    + Double.parseDouble(BackDateinterestwithGST);

            SumassuredonDeath = (smartPlatinaBl.getSumDeathBenefit(age, premPayingTerm, AnnualizePrem));
            retVal.append("<Isstaff>" + isstaff + "</Isstaff>");
            retVal.append("<InstallmentPremWithoutTax>" + commonForAllProd.getRoundOffLevel2
                    (commonForAllProd.getStringWithout_E(InstallmentPremWithoutTax)) +
                    "</InstallmentPremWithoutTax>");
            retVal.append("<FyInstallmentPremWithTax>" + commonForAllProd.getRoundOffLevel2
                    (commonForAllProd.getStringWithout_E(FyInstallmentPremWithTax)) +
                    "</FyInstallmentPremWithTax>");
            retVal.append("<firtYrServTax>" + commonForAllProd.getRoundOffLevel2
                    (commonForAllProd.getStringWithout_E(fySeviceTax)) +
                    "</firtYrServTax>");
            retVal.append("<SyInstallmentPremWithTax>" + commonForAllProd.getRoundOffLevel2
                    (commonForAllProd.getStringWithout_E(SyInstallmentPremWithTax)) +
                    "</SyInstallmentPremWithTax>");
            retVal.append("<secYrServTax>" + commonForAllProd.getRoundOffLevel2
                    (commonForAllProd.getStringWithout_E(sySeviceTax)) +
                    "</secYrServTax>");
            retVal.append("<GuaranteedIncome>" + commonForAllProd.getRoundOffLevel2
                    (commonForAllProd.getStringWithout_E(GuaranteedIncome)) +
                    "</GuaranteedIncome>");
            retVal.append("<SumAssured>" + commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E
                    (smartPlatinaBl.getSumAssured())) +
                    "</SumAssured>");
            retVal.append("<SAMF>" + "0" + "</SAMF>");
            retVal.append("<SumAssuredOnDeath>" + commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E
                    (SumassuredonDeath)) +
                    "</SumAssuredOnDeath>");
            retVal.append("<BackDateinterest>" + BackDateinterest +
                    "</BackDateinterest>");
            retVal.append("<backdateInt>" + BackDateinterestwithGST +
                    "</backdateInt>");
            retVal.append("<OccuInt>" + minesOccuInterest + "</OccuInt>");

            String servicetax_MinesOccuInterest = "";
            retVal.append("<OccuIntServiceTax>" + servicetax_MinesOccuInterest + "</OccuIntServiceTax>");


            for (int policy_yr = 1; policy_yr <= smartPlatinaBl.getPolicy_Term(); policy_yr++) {
                TotalPrem += smartPlatinaBl.getTotalPremium(policy_yr);
                retVal.append("<PolicyYr" + policy_yr + ">" + policy_yr + "</PolicyYr" + policy_yr + ">");
                AnnualizePrem = (smartPlatinaBl.getAnnualized_premium(policy_yr));
                retVal.append("<AnnualizePrem" + policy_yr + ">" + (AnnualizePrem > 0 ? AnnualizePrem : "-") + "</AnnualizePrem" + policy_yr + ">");
                AnnualizeSumPrem += AnnualizePrem;
                retVal.append("<OtherBenefit" + policy_yr + ">" + "-" + "</OtherBenefit" + policy_yr + ">");
                SurvivalBenefit = (smartPlatinaBl.getSurvivalBenefit(age, policy_yr));
                retVal.append("<SurvivalBenefit" + policy_yr + ">" + commonForAllProd.getRound(commonForAllProd.getStringWithout_E(SurvivalBenefit)) + "</SurvivalBenefit" + policy_yr + ">");
                MaturityBenefit = (smartPlatinaBl.getMaturityBenefit(age, policy_yr));
                retVal.append("<MaturityBenefit" + policy_yr + ">" + MaturityBenefit + "</MaturityBenefit" + policy_yr + ">");
                DeathBenefit = (smartPlatinaBl.getDeathBenefit(age, policy_yr, AnnualizeSumPrem));
                //double DeathBenefit1 =Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(DeathBenefit)));
                retVal.append("<DeathBenefit" + policy_yr + ">" + commonForAllProd.getRound(commonForAllProd.getStringWithout_E(DeathBenefit)) + "</DeathBenefit" + policy_yr + ">");
                SurvivalBenefitSum += SurvivalBenefit;
                MinGuaranteedSurrenderValue = smartPlatinaBl.getMinGuaranteedSurrenderValue(age, policy_yr, SurvivalBenefitSum - SurvivalBenefit);
                retVal.append("<MinGuaranteedSurrenderValue" + policy_yr + ">" + (commonForAllProd.getRound(commonForAllProd.getStringWithout_E(MinGuaranteedSurrenderValue))) + "</MinGuaranteedSurrenderValue" + policy_yr + ">");
                SpecialSurrenderValue = (smartPlatinaBl.getSpecialSurrenderValue(age, policy_yr, AnnualizeSumPrem, SurvivalBenefitSum - SurvivalBenefit));
                retVal.append("<SpecialSurrenderValue" + policy_yr + ">" + commonForAllProd.getRound(commonForAllProd.getStringWithout_E(SpecialSurrenderValue)) + "</SpecialSurrenderValue" + policy_yr + ">");


            }

            retVal.append("</SmartPlatinaPlus>");

            flag_sumassured = valSA(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E
                    (smartPlatinaBl.getSumAssured())));

            System.out.println("retVal.toString() = " + retVal.toString());
        } catch (Exception e) {
            // TODO: handle exception

            retVal.append("<?xml version='1.0' encoding='utf-8' ?><SmartPlatinaPlus>"
                    + "<errCode>1</errCode>" +
                    "<errorMessage>" + e.getMessage() + "</errorMessage></SmartPlatinaPlus>");
        }
        return flag_sumassured;
    }

    /********************************** Validations starts here **********************************************************/

    // maturity age of policy is 50 years
    private boolean valMaturityAge() {
        policyTerm = edt_bi_smart_guaranteed_saving_plan_Actual_policyterm.getText().toString();
        int Age = Integer.parseInt(spnr_Age.getSelectedItem().toString());
        int premPayingTerm = Integer.parseInt(spnrPremPayingTerm.getSelectedItem().toString());

        if ((Integer.parseInt(policyTerm) + Age) > 99 && !str_user_POS_User.equalsIgnoreCase("POSP") && !str_user_POS_User.equalsIgnoreCase("POSPRA")) {
            commonMethods.showMessageDialog(context,"Max. maturity age is 99 years");
            commonMethods.setFocusable(btn_bi_smart_guaranteed_saving_plan_life_assured_date);
            btn_bi_smart_guaranteed_saving_plan_life_assured_date
                    .requestFocus();

            return false;

        } else if ((str_user_POS_User.equalsIgnoreCase("POSP") || str_user_POS_User.equalsIgnoreCase("POSPRA")) && (Integer.parseInt(policyTerm) + Age) > 65) {
            commonMethods.showMessageDialog(context,"Max. maturity age is 65 years");
                        commonMethods.setFocusable(btn_bi_smart_guaranteed_saving_plan_life_assured_date);
                        btn_bi_smart_guaranteed_saving_plan_life_assured_date
                                .requestFocus();
            return false;
        } else
            return true;

    }

    // Premium Amount Validation
    public boolean valPremiumAmt() {
        String error = "";
        if (str_income_plan.equals("")) {
            error = "Please select Income Plan";
        }
        if (premPayingTerm.equals("10") && payoutPeriod.equalsIgnoreCase("30")) {
            error = "For premium paying term 10 ,payout period 30 is not allowed";

        }
        if (edt_premiumAmt.getText().toString().equals("")) {
            error = "Please enter Premium Amount in Rs.";

        }

		/*else if (Integer.parseInt(edt_premiumAmt.getText().toString()) > prop.maxPremiumAmt) {
			error = "Premium Amount should not be greater than Rs."
					+ currencyFormat.format(prop.maxPremiumAmt);}
*/
        else if (Integer.parseInt(edt_premiumAmt.getText().toString()) < prop.minPremiumAmt) {
            error = "Premium Amount should not be less than Rs."
                    + currencyFormat.format(prop.minPremiumAmt);

        } else {
            if (!(Double.parseDouble(edt_premiumAmt.getText().toString()) % 1000 == 0)) {
                error = "Premium Amount should be multiple of 1000";

            }
        }
        if (!error.toString().equals("")) {
            commonMethods.showMessageDialog(context,error.toString());
            commonMethods.setFocusable(edt_premiumAmt);
            edt_premiumAmt.requestFocus();
            return false;
        } else
            return true;

    }

    // Life Assured Details Validation
    public boolean valLifeAssuredProposerDetail() {
        if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {
            if (lifeAssured_Title.equals("")
                    || lifeAssured_First_Name.equals("")
                    || lifeAssured_Last_Name.equals("")) {

                commonMethods.showMessageDialog(context,"Please Fill Name Detail For LifeAssured");
                            if (lifeAssured_Title.equals("")) {
                                // apply focusable method
                                commonMethods.setFocusable(spnr_bi_smart_guaranteed_saving_plan_life_assured_title);
                                spnr_bi_smart_guaranteed_saving_plan_life_assured_title
                                        .requestFocus();
                            } else if (lifeAssured_First_Name.equals("")) {
                                commonMethods.setFocusable(edt_bi_smart_guaranteed_saving_plan_life_assured_first_name);
                                edt_bi_smart_guaranteed_saving_plan_life_assured_first_name
                                        .requestFocus();
                            } else {
                                commonMethods.setFocusable(edt_bi_smart_guaranteed_saving_plan_life_assured_last_name);
                                edt_bi_smart_guaranteed_saving_plan_life_assured_last_name
                                        .requestFocus();
                            }

                return false;
            } else if (gender.equalsIgnoreCase("")) {

                commonMethods.showMessageDialog(context,"Please select Gender");
                            // apply focusable method
                            commonMethods.setFocusable(spnr_bi_smart_guaranteed_saving_plan_life_assured_title);
                            spnr_bi_smart_guaranteed_saving_plan_life_assured_title
                                    .requestFocus();

                return false;

            } else if (lifeAssured_Title.equalsIgnoreCase("Mr.")
                    && gender.equalsIgnoreCase("Female")) {

                commonMethods.showMessageDialog(context,"Life Assured Title and Gender is not Valid");
                            // apply focusable method
                            commonMethods.setFocusable(spnr_bi_smart_guaranteed_saving_plan_life_assured_title);
                            spnr_bi_smart_guaranteed_saving_plan_life_assured_title
                                    .requestFocus();

                return false;

            } else if (lifeAssured_Title.equalsIgnoreCase("Ms.")
                    && gender.equalsIgnoreCase("Male")) {

                commonMethods.showMessageDialog(context,"Life Assured Title and Gender is not Valid");
                            commonMethods.setFocusable(spnr_bi_smart_guaranteed_saving_plan_life_assured_title);
                            spnr_bi_smart_guaranteed_saving_plan_life_assured_title
                                    .requestFocus();
                return false;

            } else if (lifeAssured_Title.equalsIgnoreCase("Mrs.")
                    && gender.equalsIgnoreCase("Male")) {

                commonMethods.showMessageDialog(context,"Life Assured Title and Gender is not Valid");
                            // apply focusable method
                            commonMethods.setFocusable(spnr_bi_smart_guaranteed_saving_plan_life_assured_title);
                            spnr_bi_smart_guaranteed_saving_plan_life_assured_title
                                    .requestFocus();

                return false;
            } else {
                return true;
            }

        } else if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("n")) {

            if (lifeAssured_Title.equals("")
                    || lifeAssured_First_Name.equals("")
                    || lifeAssured_Last_Name.equals("")) {

                commonMethods.showMessageDialog(context,"Please Fill Name Detail For LifeAssured");
                            if (lifeAssured_Title.equals("")) {
                                // apply focusable method
                                commonMethods.setFocusable(spnr_bi_smart_guaranteed_saving_plan_life_assured_title);
                                spnr_bi_smart_guaranteed_saving_plan_life_assured_title
                                        .requestFocus();
                            } else if (lifeAssured_First_Name.equals("")) {
                                commonMethods.setFocusable(edt_bi_smart_guaranteed_saving_plan_life_assured_first_name);
                                edt_bi_smart_guaranteed_saving_plan_life_assured_first_name
                                        .requestFocus();
                            } else {
                                commonMethods.setFocusable(edt_bi_smart_guaranteed_saving_plan_life_assured_first_name);
                                edt_bi_smart_guaranteed_saving_plan_life_assured_first_name
                                        .requestFocus();
                            }

                return false;
            } else if (gender.equalsIgnoreCase("")) {

                commonMethods.showMessageDialog(context,"Please select Gender");
                            // apply focusable method
                            commonMethods.setFocusable(spnr_bi_smart_guaranteed_saving_plan_life_assured_title);
                            spnr_bi_smart_guaranteed_saving_plan_life_assured_title
                                    .requestFocus();

                return false;

            } else if (lifeAssured_Title.equalsIgnoreCase("Mr.")
                    && gender.equalsIgnoreCase("Female")) {

                commonMethods.showMessageDialog(context,"Life Assured Title and Gender is not Valid");
                            // apply focusable method
                            commonMethods.setFocusable(spnr_bi_smart_guaranteed_saving_plan_life_assured_title);
                            spnr_bi_smart_guaranteed_saving_plan_life_assured_title
                                    .requestFocus();

                return false;

            } else if (lifeAssured_Title.equalsIgnoreCase("Ms.")
                    && gender.equalsIgnoreCase("Male")) {

                commonMethods.showMessageDialog(context,"Life Assured Title and Gender is not Valid");
                            // apply focusable method
                            commonMethods.setFocusable(spnr_bi_smart_guaranteed_saving_plan_life_assured_title);
                            spnr_bi_smart_guaranteed_saving_plan_life_assured_title
                                    .requestFocus();

                return false;

            } else if (lifeAssured_Title.equalsIgnoreCase("Mrs.")
                    && gender.equalsIgnoreCase("Male")) {

                commonMethods.showMessageDialog(context,"Life Assured Title and Gender is not Valid");
                            // apply focusable method
                            commonMethods.setFocusable(spnr_bi_smart_guaranteed_saving_plan_life_assured_title);
                            spnr_bi_smart_guaranteed_saving_plan_life_assured_title
                                    .requestFocus();

                return false;
            } else if (proposer_Title.equals("")
                    || proposer_First_Name.equals("")
                    || proposer_Last_Name.equals("")) {

                commonMethods.showMessageDialog(context,"Please Fill Name Detail For Proposer");
                            if (proposer_Title.equals("")) {
                                // apply focusable method
                                commonMethods.setFocusable(spnr_bi_money_back_gold_proposer_title);
                                spnr_bi_money_back_gold_proposer_title
                                        .requestFocus();
                            } else if (proposer_First_Name.equals("")) {
                                edt_bi_money_back_gold_proposer_first_name
                                        .requestFocus();
                            } else {
                                edt_bi_money_back_gold_proposer_last_name
                                        .requestFocus();
                            }

                return false;

            } else if (gender_proposer.equalsIgnoreCase("")) {
                commonMethods.dialogWarning(context,"Please Select Proposer Gender", true);

                return false;
            } else if (proposer_Title.equalsIgnoreCase("Mr.")
                    && gender_proposer.equalsIgnoreCase("Female")) {

                commonMethods.showMessageDialog(context,"Proposer Title and Gender is not Valid");
                            // apply focusable method
                            commonMethods.setFocusable(spnr_bi_money_back_gold_proposer_title);
                            spnr_bi_money_back_gold_proposer_title
                                    .requestFocus();

                return false;

            } else if (proposer_Title.equalsIgnoreCase("Ms.")
                    && gender_proposer.equalsIgnoreCase("Male")) {

                commonMethods.showMessageDialog(context,"Proposer Title and Gender is not Valid");
                            // apply focusable method
                            commonMethods.setFocusable(spnr_bi_money_back_gold_proposer_title);
                            spnr_bi_money_back_gold_proposer_title
                                    .requestFocus();

                return false;

            } else if (proposer_Title.equalsIgnoreCase("Mrs.")
                    && gender_proposer.equalsIgnoreCase("Male")) {

                commonMethods.showMessageDialog(context,"Proposer Title and Gender is not Valid");
                            // apply focusable method
                            commonMethods.setFocusable(spnr_bi_money_back_gold_proposer_title);
                            spnr_bi_money_back_gold_proposer_title
                                    .requestFocus();

                return false;
            } else {
                return true;
            }
        } else
            return true;
    }

    public boolean valDob() {

        if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {

            if (lifeAssured_date_of_birth.equals("")
                    || lifeAssured_date_of_birth
                    .equalsIgnoreCase("select Date")) {
                commonMethods.showMessageDialog(context,"Please Select Valid Date Of Birth For LifeAssured");
                            // apply focusable method
                            commonMethods.setFocusable(btn_bi_smart_guaranteed_saving_plan_life_assured_date);
                            btn_bi_smart_guaranteed_saving_plan_life_assured_date
                                    .requestFocus();

                return false;
            } else {
                return true;
            }
        }


        if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("n")) {
            if (lifeAssured_date_of_birth.equals("")
                    || lifeAssured_date_of_birth
                    .equalsIgnoreCase("select Date")) {
                commonMethods.showMessageDialog(context,"Please Select Valid Date Of Birth For LifeAssured");
                            // apply focusable method
                            commonMethods.setFocusable(btn_bi_smart_guaranteed_saving_plan_life_assured_date);
                            btn_bi_smart_guaranteed_saving_plan_life_assured_date
                                    .requestFocus();

                return false;
            } else if (proposer_date_of_birth.equals("")
                    || proposer_date_of_birth
                    .equalsIgnoreCase("select Date")) {
                commonMethods.showMessageDialog(context,"Please select valid date of birth For Proposer");
                            // apply focusable method
                            commonMethods.setFocusable(btn_bi_money_back_gold_proposer_date);
                            btn_bi_money_back_gold_proposer_date
                                    .requestFocus();

                return false;
            } else {
                return true;
            }
        } else
            return true;
    }

    /********************************** Validations ends here **********************************************************/


    private final DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            updateDisplay(DIALOG_ID);
        }
    };

    private void showDateDialog() {
        DatePickerDialog dialog = new DatePickerDialog(
                context,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener,
                mYear, mMonth, mDay);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    // For Displaying StartDate and EndDate after its selection
    private void updateDisplay(int id) {

        String mont = (mMonth + 1 < 10 ? "0" : "") + (mMonth + 1);
        String day = (mDay < 10 ? "0" : "") + mDay;

        StringBuilder date = new StringBuilder().append(day).append("-")
                .append(mont).append("-").append(mYear);

        Calendar present_date = Calendar.getInstance();
        int tDay = present_date.get(Calendar.DAY_OF_MONTH);
        int tMonth = present_date.get(Calendar.MONTH);
        int tYear = present_date.get(Calendar.YEAR);

        int age;

        if (tMonth == mMonth) {
            if (tDay >= mDay) {
                age = tYear - mYear;
            } else
                age = tYear - mYear - 1;
        } else if (tMonth > mMonth) {
            age = tYear - mYear;
        } else
            age = tYear - mYear - 1;

        String final_age = Integer.toString(age);

        if (final_age.contains("-")) {
            commonMethods.dialogWarning(context,"Please fill Valid Date", false);
        } else {
            switch (id) {

                case 2:
                    btn_PolicyholderDate.setText(date);
                    break;
                case 3:
                    btn_MarketingOfficalDate.setText(date);
                    break;

                case 4:
                    lifeAssuredAge = final_age;
                    if (str_user_POS_User.equalsIgnoreCase("POSP") || str_user_POS_User.equalsIgnoreCase("POSPRA")) {
                        prop.maxAge = 42;
                    }
                    final_age = Integer.toString(age);
                    if (final_age.contains("-")) {
                        commonMethods.showMessageDialog(context,"Please fill Valid Bith Date");
                    } else {
                        if (prop.minAge <= age && age <= prop.maxAge) {

                            btn_bi_smart_guaranteed_saving_plan_life_assured_date
                                    .setText(date);
                            spnr_Age.setSelection(getIndex(spnr_Age, final_age),
                                    false);

                            if (Integer.parseInt(final_age) < 18) {
                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                                Date datenew = null;
                                try {
                                    datenew = sdf.parse(date.toString());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                Calendar cal1 = Calendar.getInstance();
                                cal1.setTime(datenew);

                                long msDiff = present_date.getTimeInMillis() - cal1.getTimeInMillis();
                                long daysDiff = TimeUnit.MILLISECONDS.toDays(msDiff);

                                if (daysDiff < 30) {
                                    commonMethods.showMessageDialog(context,"Minimum Age should be 0 (30 days)");
                                    btn_bi_smart_guaranteed_saving_plan_life_assured_date
                                            .setText("Select Date");
                                    lifeAssured_date_of_birth = "";
                                    commonMethods.clearFocusable(btn_bi_smart_guaranteed_saving_plan_life_assured_date);
                                    commonMethods.setFocusable(btn_bi_smart_guaranteed_saving_plan_life_assured_date);

                                    btn_bi_smart_guaranteed_saving_plan_life_assured_date
                                            .requestFocus();
                                } else {
                                    proposer_Is_Same_As_Life_Assured = "N";
                                    linearlayoutProposerDetails
                                            .setVisibility(View.VISIBLE);


                                    btn_bi_smart_guaranteed_saving_plan_life_assured_date
                                            .setText(date);

                                    spnr_Age.setSelection(getIndex(spnr_Age, final_age),
                                            false);
                                    //valMaturityAge();
                                    lifeAssured_date_of_birth = commonMethods.getMMDDYYYYDatabaseDate(date + "");
                                    if (!proposer_Backdating_BackDate.equals("")) {
                                        if (proposer_Backdating_WishToBackDate_Policy
                                                .equalsIgnoreCase("y")) {
                                            rb_proposerdetail_personaldetail_backdating_no
                                                    .setChecked(true);
                                        }
                                        proposer_Backdating_BackDate = "";
                                        btn_proposerdetail_personaldetail_backdatingdate
                                                .setText("Select Date");

                                        commonMethods.clearFocusable(btn_bi_smart_guaranteed_saving_plan_life_assured_date);
                                        commonMethods.setFocusable(btn_bi_smart_guaranteed_saving_plan_life_assured_date);

                                        btn_bi_smart_guaranteed_saving_plan_life_assured_date
                                                .requestFocus();
                                    }
                                }
                            } else {
                                proposer_Is_Same_As_Life_Assured = "Y";
                                linearlayoutProposerDetails
                                        .setVisibility(View.GONE);

                                btn_bi_smart_guaranteed_saving_plan_life_assured_date
                                        .setText(date);

                                spnr_Age.setSelection(getIndex(spnr_Age, final_age),
                                        false);
                                //valMaturityAge();
                                lifeAssured_date_of_birth = commonMethods.getMMDDYYYYDatabaseDate(date + "");

                                commonMethods.clearFocusable(btn_bi_smart_guaranteed_saving_plan_life_assured_date);
                                commonMethods.setFocusable(btn_bi_smart_guaranteed_saving_plan_life_assured_date);

                                btn_bi_smart_guaranteed_saving_plan_life_assured_date
                                        .requestFocus();
                            }
                            spnr_Age.setVisibility(View.VISIBLE);
                        } else {
                            commonMethods.showMessageDialog(context,"Minimum Age should be " + prop.minAge
                                    + " yrs and Maximum Age should be " + prop.maxAge
                                    + " yrs For LifeAssured");
                            btn_bi_smart_guaranteed_saving_plan_life_assured_date
                                    .setText("Select Date");
                            lifeAssured_date_of_birth = "";

                            commonMethods.clearFocusable(btn_bi_smart_guaranteed_saving_plan_life_assured_date);
                            commonMethods.setFocusable(btn_bi_smart_guaranteed_saving_plan_life_assured_date);

                            btn_bi_smart_guaranteed_saving_plan_life_assured_date
                                    .requestFocus();
                        }
                    }
                    break;

                case 5:
                    strProposerAge = final_age;
                    final_age = Integer.toString(age);
                    if (final_age.contains("-")) {
                        commonMethods.showMessageDialog(context,"Please fill Valid Bith Date");
                    } else {
                        if (18 <= age && age <= 60) {

                            btn_bi_money_back_gold_proposer_date.setText(date);

                            edt_bi_money_back_gold_proposer_age.setText(strProposerAge);

                            proposer_date_of_birth = commonMethods.getMMDDYYYYDatabaseDate(date + "");

                            commonMethods.clearFocusable(btn_bi_money_back_gold_proposer_date);

                            commonMethods.setFocusable(edt_proposerdetail_basicdetail_contact_no);
                            edt_proposerdetail_basicdetail_contact_no
                                    .requestFocus();

                        } else {
                            commonMethods.showMessageDialog(context,"Minimum age should be 18 yrs for proposer");
                            btn_bi_money_back_gold_proposer_date
                                    .setText("Select Date");
                            proposer_date_of_birth = "";
                            commonMethods.clearFocusable(btn_bi_money_back_gold_proposer_date);
                            commonMethods.setFocusable(btn_bi_money_back_gold_proposer_date);
                            btn_bi_money_back_gold_proposer_date.requestFocus();

                        }
                    }
                    break;
                case 6:
                    if (age >= 0) {
                        proposer_Backdating_BackDate = date + "";
                        btn_proposerdetail_personaldetail_backdatingdate
                                .setText(proposer_Backdating_BackDate);
                        commonMethods.clearFocusable(btn_proposerdetail_personaldetail_backdatingdate);


                    } else {
                        commonMethods.dialogWarning(context,"Please Select Valid BackDating Date", true);
                        btn_proposerdetail_personaldetail_backdatingdate
                                .setText("Select Date");
                        proposer_Backdating_BackDate = "";
                    }

                    break;

                default:
                    break;
            }
        }
    }


    public void onClickLADob(View v) {
        initialiseDateParameter(lifeAssured_date_of_birth, 35);
        DIALOG_ID = 4;
        showDateDialog();
    }

    public void onClickDob(View v) {
        initialiseDateParameter(proposer_date_of_birth, 35);
        // setDefaultDate();
        DIALOG_ID = 5;
        showDateDialog();
    }

    private int getIndex(Spinner s1, String value) {

        int index = 0;

        for (int i = 0; i < s1.getCount(); i++) {
            if (s1.getItemAtPosition(i).equals(value)) {
                index = i;
            }
        }
        return index;
    }

    public void setDefaultDate(int id) {
        Calendar present_date = Calendar.getInstance();
        present_date.add(Calendar.YEAR, -id);
        mDay = present_date.get(Calendar.DAY_OF_MONTH);
        mMonth = present_date.get(Calendar.MONTH);
        mYear = present_date.get(Calendar.YEAR);
    }

    public String getformatedThousandString(int number) {
        String formatedstring = NumberFormat.getNumberInstance(Locale.US)
                .format(number);
        return formatedstring;
    }

    public String getCurrentDate() {
        Calendar present_date = Calendar.getInstance();
        int mDay = present_date.get(Calendar.DAY_OF_MONTH);
        int mMonth = present_date.get(Calendar.MONTH) + 1;
        int mYear = present_date.get(Calendar.YEAR);

        return mDay + "-" + mMonth + "-" + mYear;

    }

    /**
     * Used To Change date From mm-dd-yyyy to dd-mm-yyyy
     */
    public String getDate(String OldDate) {
        String NewDate = "";
        try {
            DateFormat userDateFormat = new SimpleDateFormat("MM-dd-yyyy");
            DateFormat dateFormatNeeded = new SimpleDateFormat("dd-MM-yyyy");
            Date date = userDateFormat.parse(OldDate);

            NewDate = dateFormatNeeded.format(date);

        } catch (Exception e) {
            // TODO: handle exception
            Log.e("Integrated Service", "Error in Getting Date");
        }

        return NewDate;
    }



    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        if (v.getId() == edt_bi_smart_guaranteed_saving_plan_life_assured_first_name
                .getId()) {
            commonMethods.setFocusable(edt_bi_smart_guaranteed_saving_plan_life_assured_middle_name);
            edt_bi_smart_guaranteed_saving_plan_life_assured_middle_name
                    .requestFocus();
        } else if (v.getId() == edt_bi_smart_guaranteed_saving_plan_life_assured_middle_name
                .getId()) {
            commonMethods.setFocusable(edt_bi_smart_guaranteed_saving_plan_life_assured_last_name);
            edt_bi_smart_guaranteed_saving_plan_life_assured_last_name
                    .requestFocus();
        } else if (v.getId() == edt_bi_smart_guaranteed_saving_plan_life_assured_last_name
                .getId()) {
            commonMethods.setFocusable(btn_bi_smart_guaranteed_saving_plan_life_assured_date);
            btn_bi_smart_guaranteed_saving_plan_life_assured_date
                    .requestFocus();
        } else if (v.getId() == edt_proposerdetail_basicdetail_contact_no.getId()) {
            commonMethods.setFocusable(edt_proposerdetail_basicdetail_Email_id);
            edt_proposerdetail_basicdetail_Email_id.requestFocus();
        } else if (v.getId() == edt_proposerdetail_basicdetail_Email_id.getId()) {
            commonMethods.setFocusable(edt_proposerdetail_basicdetail_ConfirmEmail_id);
            edt_proposerdetail_basicdetail_ConfirmEmail_id.requestFocus();
        } else if (v.getId() == edt_proposerdetail_basicdetail_ConfirmEmail_id
                .getId()) {
            commonMethods.setFocusable(edt_premiumAmt);
            edt_premiumAmt.requestFocus();
        } else if (v.getId() == edt_premiumAmt.getId()) {

        }
        return true;
    }

    public boolean valBasicDetail() {
        if (gender.equalsIgnoreCase("")) {
            commonMethods.dialogWarning(context, "Please Select Gender", true);
            return false;
        } else if (edt_proposerdetail_basicdetail_contact_no.getText().toString()
                .equals("")) {
            commonMethods.dialogWarning(context, "Please Fill  Mobile No", true);
            edt_proposerdetail_basicdetail_contact_no.requestFocus();
            return false;
        } else if (edt_proposerdetail_basicdetail_contact_no.getText()
                .toString().length() != 10) {
            commonMethods.dialogWarning(context, "Please Fill 10 Digit Mobile No", true);
            edt_proposerdetail_basicdetail_contact_no.requestFocus();
            return false;
        } else if (!emailId.equals("")) {

            email_id_validation(emailId);
            if (validationFla1) {

                if (ConfirmEmailId.equals("")) {

                    commonMethods.dialogWarning(context, "Please Fill Confirm Email Id", true);
                    edt_proposerdetail_basicdetail_ConfirmEmail_id.requestFocus();
                    return false;
                } else if (!ConfirmEmailId.equals(emailId)) {
                    commonMethods.dialogWarning(context, "Email Id Does Not Match", true);
                    return false;
                }

                return true;
            } else {
                commonMethods.dialogWarning(context, "Please Fill Valid Email Id", true);
                edt_proposerdetail_basicdetail_Email_id.requestFocus();
                return false;
            }
        } else if (!ConfirmEmailId.equals("")) {

            email_id_validation(ConfirmEmailId);
            if (validationFla1) {
                if (emailId.equals("")) {
                    commonMethods.dialogWarning(context, "Please Fill Email Id", true);
                    edt_proposerdetail_basicdetail_Email_id.requestFocus();
                    return false;

                }
                return true;
            } else {
                commonMethods.dialogWarning(context, "Please Fill Valid Confirm Email Id", true);
                edt_proposerdetail_basicdetail_ConfirmEmail_id.requestFocus();
                return false;
            }
        } else if (premPayingTerm.equalsIgnoreCase("Select")) {
            commonMethods.showMessageDialog(context,"Please select Premium Paying Term");
            return false;
        } else {
            return true;
        }

    }

    public void mobile_validation(String number) {
        if ((number.length() != 10)) {
            edt_proposerdetail_basicdetail_contact_no
                    .setError("Please provide correct 10-digit mobile number");
        } else if ((number.length() == 10)) {
        }
    }

    public void email_id_validation(String email_id) {

        String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email_id);
        if (!(matcher.matches())) {
            edt_proposerdetail_basicdetail_Email_id
                    .setError("Please provide the correct email address");
            validationFla1 = false;
        } else if (email_id.contains("@sbi.co.in")
                || email_id.contains("@sbm.co.in")
                || email_id.contains("@sbbj.co.in")
                || email_id.contains("@sbhyd.co.in")
                || email_id.contains("@sbp.co.in")
                || email_id.contains("@sbt.co.in")
                || email_id.contains("@sbi-life.com")
                || email_id.contains("@sbilife.co.in")) {
            edt_proposerdetail_basicdetail_Email_id
                    .setError("Please provide the Personal email address");
            validationFla1 = false;
        } else if ((matcher.matches())) {
            validationFla1 = true;
        }
    }

    public void confirming_email_id(String email_id) {

        if (!(email_id.equals(ProposerEmailId))) {
            edt_proposerdetail_basicdetail_ConfirmEmail_id
                    .setError("Email id does not match");
        } else if ((email_id.equals(ProposerEmailId))) {
        }

    }


    private void initialiseDateParameter(String date_value, int id) {
        if (date_value.equals("")) {
            setDefaultDate(id);
        } else {
            String[] array = date_value.split("-");
            mMonth = Integer.parseInt(array[0]) - 1;
            mDay = Integer.parseInt(array[1]);
            mYear = Integer.parseInt(array[2]);
        }
    }


    public void createPdfNew() {
        Paragraph new_line = new Paragraph("\n");
        try {
            Font small_bold2 = new Font(Font.FontFamily.TIMES_ROMAN, 6,
                    Font.BOLD);
            Font bold = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);

            Font headerBold = new Font(Font.FontFamily.TIMES_ROMAN, 11,
                    Font.BOLD, BaseColor.WHITE);
            Font small_bold = new Font(Font.FontFamily.TIMES_ROMAN, 8,
                    Font.BOLD);
            Font normal = new Font(Font.FontFamily.TIMES_ROMAN, 5, Font.NORMAL);
            Font small_bold_for_name = new Font(Font.FontFamily.TIMES_ROMAN,
                    10, Font.BOLD);
            Font small_normal = new Font(Font.FontFamily.TIMES_ROMAN, 8,
                    Font.NORMAL);
            Font sub_headerBold = new Font(Font.FontFamily.TIMES_ROMAN, 7,
                    Font.BOLD);
            Font small_bold1 = new Font(Font.FontFamily.TIMES_ROMAN, 6,
                    Font.BOLD);
            Font white_headerBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
                    Font.BOLD, BaseColor.WHITE);
            Font small_normal_biFont = new Font(Font.FontFamily.TIMES_ROMAN, 8,
                    Font.NORMAL);
            Font big_normal_biFont = new Font(Font.FontFamily.TIMES_ROMAN, 11,
                    Font.BOLD);
            Font medium_normal_underline_biFont = new Font(
                    Font.FontFamily.TIMES_ROMAN, 10, Font.UNDERLINE);
            Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD,
                    BaseColor.RED);
            Font small_normal1 = new Font(Font.FontFamily.TIMES_ROMAN, 8,
                    Font.NORMAL);
            Font normal1_bold = new Font(Font.FontFamily.TIMES_ROMAN, 6,
                    Font.BOLD);

            mypath = mStorageUtils.createFileToAppSpecificDir(context, QuatationNumber + "BI.pdf");
            needAnalysispath = mStorageUtils.createFileToAppSpecificDir(context, "NA.pdf");
            newFile = mStorageUtils.createFileToAppSpecificDir(context, QuatationNumber + "P01.pdf");
            Rectangle rect = new Rectangle(594f, 792f);
            Document document = new Document(rect, 50, 50, 50, 50);
            // Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter pdf_writer = null;
            pdf_writer = PdfWriter.getInstance(document, new FileOutputStream(
                    mypath.getAbsolutePath()));

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                    R.drawable.sbi_life_logo);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bitMapData = stream.toByteArray();
            Image image = Image.getInstance(bitMapData);
            image.scalePercent(50f);
            image.setAlignment(Element.ALIGN_LEFT);
            document.open();
            //Header

            Paragraph para_img_logo_after_space_1 = new Paragraph(" ");

            Paragraph para_address = new Paragraph("SBI Life Insurance Co. Ltd																		\r\n"
                    + "Registered & Corporate Office: 'Natraj', M.V.Road and Western Express Highway Junction, Andheri (East), Mumbai - 400069 																		\r\n"
                    + "IRDAI Registration No. 111 | Website: www.sbilife.co.in | Email: info@sbilife.co.in | CIN: L99999MH2000PLC129113																		\r\n"
                    + "Toll Free: 1800 267 9090 (Between 9.00 am & 9.00 pm)																		\r\n"
                    + "", small_bold);
            para_address.setAlignment(Element.ALIGN_CENTER);
            document.add(para_address);
            document.add(para_img_logo_after_space_1);

            //BI Header

            Paragraph BI_header = new Paragraph(" Benefit Illustration (BI) : SBI Life -Smart Platina Plus (UIN :  111N133V01)| An Individual, Non-Linked, Non-Participating, Life Insurance Savings Product \r\n"
                    + "\r\n"
                    + "", small_bold);
            BI_header.setAlignment(Element.ALIGN_CENTER);
            document.add(BI_header);
            document.add(para_img_logo_after_space_1);

            PdfPTable table_proposer_name = new PdfPTable(4);
            table_proposer_name.setWidthPercentage(100);

            PdfPCell ProposalNumber_cell_1 = new PdfPCell(new Paragraph(
                    "Proposal Number", small_normal));
            PdfPCell ProposalNumber_cell_2 = new PdfPCell(new Paragraph(
                    QuatationNumber, normal1_bold));
            PdfPCell ProposalNumber_cell_11 = new PdfPCell(new Paragraph(
                    "Channel / Intermediary :", small_normal));
            PdfPCell ProposalNumber_cell_21 = new PdfPCell(new Paragraph(
                    userType, normal1_bold));
            ProposalNumber_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);
            ProposalNumber_cell_21.setHorizontalAlignment(Element.ALIGN_CENTER);


            ProposalNumber_cell_1.setPadding(5);
            ProposalNumber_cell_2.setPadding(5);
            ProposalNumber_cell_11.setPadding(5);
            ProposalNumber_cell_21.setPadding(5);

            table_proposer_name.addCell(ProposalNumber_cell_1);
            table_proposer_name.addCell(ProposalNumber_cell_2);
            table_proposer_name.addCell(ProposalNumber_cell_11);
            table_proposer_name.addCell(ProposalNumber_cell_21);
            document.add(table_proposer_name);
            document.add(para_img_logo_after_space_1);


            Paragraph para;

            para = new Paragraph("Introduction", small_bold);

            document.add(para);

            para = new Paragraph("The main objective of the illustration is that the client is able to appreciate the features of the product and the flow of benefits in different circumstances with some level of quantification. For further information on the product and its benefits, please refer to the sales brochure and/or policy document.", small_normal);

            document.add(para);

            document.add(para_img_logo_after_space_1);


            PdfPTable table;
            PdfPCell cell;

            table = new PdfPTable(1);
            table.setWidthPercentage(100);


            cell = new PdfPCell(new Paragraph("Proposer and Life Assured Details", small_bold));

            table.addCell(cell);

            document.add(para_img_logo_after_space_1);

            document.add(table);

            document.add(para_img_logo_after_space_1);
            if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("n")) {
                table = new PdfPTable(2);
                table.setWidthPercentage(100);
//	         table.setHorizontalAlignment(0);

                cell = new PdfPCell(new Paragraph("Name of the Prospect/Policyholder", small_bold));
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph(name_of_proposer, small_bold));
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("Age (Years)", small_bold));
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph(strProposerAge, small_bold));
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("Gender", small_bold));
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph(gender_proposer, small_bold));
                table.addCell(cell);

            } else {
                table = new PdfPTable(2);
                table.setWidthPercentage(100);
//	         table.setHorizontalAlignment(0);

                cell = new PdfPCell(new Paragraph("Name of the Prospect/Policyholder", small_bold));
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph(name_of_life_assured, small_bold));
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("Age (Years)", small_bold));
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph(lifeAssuredAge, small_bold));
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("Gender", small_bold));
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph(gender, small_bold));
                table.addCell(cell);
            }
            cell = new PdfPCell(new Paragraph("State", small_bold));
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("", small_bold));
            table.addCell(cell);

            document.add(table);
            document.add(para_img_logo_after_space_1);


            table = new PdfPTable(2);
            table.setWidthPercentage(100);
//	         table.setHorizontalAlignment(2);

            cell = new PdfPCell(new Paragraph("Name of the Life Assured", small_bold));
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(name_of_life_assured, small_bold));
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Age (Years)", small_bold));
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(lifeAssuredAge, small_bold));
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Gender", small_bold));
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(gender, small_bold));
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Staff", small_bold));
            table.addCell(cell);
            if (isStaff.equalsIgnoreCase("true")) {
                cell = new PdfPCell(new Paragraph("Yes", small_bold));
            } else {
                cell = new PdfPCell(new Paragraph("No", small_bold));
            }
            table.addCell(cell);

            document.add(table);

            document.add(para_img_logo_after_space_1);

            para = new Paragraph("This benefit illustration is intended to show year-wise premiums payable and benefits under the policy. 																		\r\n"
                    + "", small_normal);

            document.add(para);

            document.add(para_img_logo_after_space_1);

            table = new PdfPTable(1);
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Paragraph("Policy Details", small_bold));

            table.addCell(cell);

            document.add(table);
            document.add(para_img_logo_after_space_1);

            table = new PdfPTable(2);
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Paragraph("Policy Option (Income Option)", small_bold));
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(str_income_plan, small_bold));
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Policy Term (Years)", small_bold));
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(policyTerm, small_bold));
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Premium Payment Term (Years)", small_bold));
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(premPayingTerm, small_bold));
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Mode / Frequency of Premium Payment", small_bold));
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(premPayingMode, small_bold));
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Guaranteed Income Payout Frequency", small_bold));
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(strGuaranteedPayoutFrequency, small_bold));
            table.addCell(cell);

            document.add(table);
            document.add(para_img_logo_after_space_1);

            table = new PdfPTable(2);
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Paragraph("Amount of Installment Premium (Rs.)", small_bold));
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(prsObj.parseXmlTag(output, "InstallmentPremWithoutTax"), small_bold));
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Sum Assured (Rs.)", small_bold));
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(prsObj.parseXmlTag(output, "SumAssured"), small_bold));
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Sum Assured on Death (at inception of the policy) (Rs.)", small_bold));
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(prsObj.parseXmlTag(output, "SumAssuredOnDeath"), small_bold));
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Rate of Applicable Taxes", small_bold));
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("4.5% in the 1st policy year and 2.25% from 2nd policy year onwards", small_bold));
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Guaranteed Income (Rs.)", small_bold));
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(prsObj.parseXmlTag(output, "GuaranteedIncome"), small_bold));
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Payout Period", small_bold));
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(payoutPeriod + " years", small_bold));
            table.addCell(cell);

            document.add(table);
            document.add(para_img_logo_after_space_1);


            para = new Paragraph("Premium Summary", small_normal);

            document.add(para);

            document.add(para_img_logo_after_space_1);


            table = new PdfPTable(8);
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Paragraph("", small_bold));
            cell.setColspan(4);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Base Plan", small_bold));
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Riders", small_bold));
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Total Installment Premium", small_bold));
            cell.setColspan(2);
            table.addCell(cell);


            cell = new PdfPCell(new Paragraph("Installment Premium without Applicable Taxes (Rs.)", small_bold));
            cell.setColspan(4);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(prsObj.parseXmlTag(output, "InstallmentPremWithoutTax"), small_bold));
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Not Applicable", small_bold));
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(basicprem, small_bold));
            cell.setColspan(2);
            table.addCell(cell);


            cell = new PdfPCell(new Paragraph("Installment Premium with 1st Year Applicable Taxes (Rs.)", small_bold));
            cell.setColspan(4);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(prsObj.parseXmlTag(output, "FyInstallmentPremWithTax"), small_bold));
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Not Applicable", small_bold));
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(basicpremfirstyr, small_bold));
            cell.setColspan(2);
            table.addCell(cell);


            cell = new PdfPCell(new Paragraph("Installment Premium with Applicable Taxes 2nd Year onwards (Rs.)", small_bold));
            cell.setColspan(4);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(prsObj.parseXmlTag(output, "SyInstallmentPremWithTax"), small_bold));
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Not Applicable", small_bold));
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(basicpremsecondyr, small_bold));
            cell.setColspan(2);
            table.addCell(cell);


            document.add(table);


            document.add(para_img_logo_after_space_1);

            para = new Paragraph("Please Note:", small_bold);

            document.add(para);

            para = new Paragraph("1. The premiums can also be paid by giving standing instruction to your bank or you can pay through your credit card.    \r\n"
                    + "2. Applicable Taxes (including surcharge/cess etc), at the rate notified by the Central Government/ State Government / Union Territories of India from time to time and as per the provisions of the prevalent tax laws will be payable on premium as per the product features.", small_normal);

            document.add(para);


            //document.newPage();


            document.add(para_img_logo_after_space_1);
            para = new Paragraph("Benefit Illustration for SBI Life - Smart Platina Plus", small_bold);

            document.add(para);

            para = new Paragraph("Amount in Rupees", small_bold);

            document.add(para);

            table.addCell(cell);

            document.add(para_img_logo_after_space_1);

            //Adding rows
            PdfPTable Table_BI_Header = new PdfPTable(8);
            Table_BI_Header.setWidthPercentage(100);
            PdfPCell cell_EndOfYear = new PdfPCell(new Paragraph("Policy Year",
                    small_bold2));
            cell_EndOfYear.setPadding(5);
            cell_EndOfYear.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell_EndOfYear.setRowspan(3);

            String str_ann_prem = "";

            str_ann_prem = " Annualized premium";

            PdfPCell cell_YearlyPremiumPaid = new PdfPCell(new Paragraph(
                    str_ann_prem, small_bold2));
            cell_YearlyPremiumPaid.setPadding(5);
            cell_YearlyPremiumPaid.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell_YearlyPremiumPaid.setRowspan(3);

            PdfPCell cell_YearlyPremiumPaid2 = new PdfPCell(new Paragraph(
                    "Guaranteed", small_bold2));
            cell_YearlyPremiumPaid2.setPadding(5);
            cell_YearlyPremiumPaid2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell_YearlyPremiumPaid2.setColspan(5);
            PdfPCell cell_YearlyPremiumPaid3 = new PdfPCell(new Paragraph(
                    "Non- Guaranteed", small_bold2));
            cell_YearlyPremiumPaid3.setPadding(5);
            cell_YearlyPremiumPaid3.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell_YearlyPremiumPaid3.setColspan(1);


            PdfPCell cell_CummulativePremiumPaid = new PdfPCell(new Paragraph(
                    "Guaranteed Income", small_bold2));
            cell_CummulativePremiumPaid.setPadding(5);
            cell_CummulativePremiumPaid
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            cell_CummulativePremiumPaid.setRowspan(2);
            PdfPCell cell_CummulativePremiumPaid2 = new PdfPCell(new Paragraph(
                    "Other Benefits", small_bold2));
            cell_CummulativePremiumPaid2.setPadding(5);
            cell_CummulativePremiumPaid2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            cell_CummulativePremiumPaid2.setRowspan(2);


            PdfPCell cell_GuarantedMaturityBenefit = new PdfPCell(
                    new Paragraph("Maturity Benefit",
                            small_bold2));

            cell_GuarantedMaturityBenefit.setPadding(5);
            cell_GuarantedMaturityBenefit
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            cell_GuarantedMaturityBenefit.setRowspan(2);


            PdfPCell cell_GuarantedDeathBenefit = new PdfPCell(new Paragraph(
                    "Death benefit", small_bold2));

            cell_GuarantedDeathBenefit.setPadding(5);
            cell_GuarantedDeathBenefit
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            cell_GuarantedDeathBenefit.setRowspan(2);

            PdfPCell cell_GuarantedSurrenderValue = new PdfPCell(new Paragraph(
                    "Minimum Guaranteed Surrender Value", small_bold2));

            cell_GuarantedSurrenderValue.setPadding(5);
            cell_GuarantedSurrenderValue
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            cell_GuarantedSurrenderValue.setRowspan(2);
            PdfPCell cell_GuarantedSurrenderValue2 = new PdfPCell(new Paragraph(
                    "Special Surrender Value", small_bold2));

            cell_GuarantedSurrenderValue2.setPadding(5);
            cell_GuarantedSurrenderValue2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            cell_GuarantedSurrenderValue2.setRowspan(2);
            Table_BI_Header.addCell(cell_EndOfYear);
            Table_BI_Header.addCell(cell_YearlyPremiumPaid);
            Table_BI_Header.addCell(cell_YearlyPremiumPaid2);
            Table_BI_Header.addCell(cell_YearlyPremiumPaid3);
            Table_BI_Header.addCell(cell_CummulativePremiumPaid);
            Table_BI_Header.addCell(cell_CummulativePremiumPaid2);
            Table_BI_Header.addCell(cell_GuarantedMaturityBenefit);
            Table_BI_Header.addCell(cell_GuarantedDeathBenefit);
            Table_BI_Header.addCell(cell_GuarantedSurrenderValue);
            Table_BI_Header.addCell(cell_GuarantedSurrenderValue2);
            document.add(Table_BI_Header);

            float[] columnWidthsBI_Header1 = {1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f};

            PdfPTable table1 = new PdfPTable(8);
            table1.setWidthPercentage(100);
            table1.setWidths(columnWidthsBI_Header1);

            //Row 1
            for (int i = 0; i < Integer.parseInt(policyTerm); i++) {
                String Yearly_basic_premium = "";
                if (!list_data.get(
                        i).getYearly_basic_premium().equalsIgnoreCase("-")) {
                    Yearly_basic_premium = list_data.get(
                            i).getYearly_basic_premium();
                }


                PdfPCell cell1 = new PdfPCell(new Phrase(list_data.get(i)
                        .getEnd_of_year(), normal));
                cell1.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell1);

                PdfPCell cell2 = new PdfPCell(new Phrase(
                        Yearly_basic_premium, normal));
                cell2.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell2);
                PdfPCell cell3 = new PdfPCell(new Phrase(
                        list_data.get(
                                i).getSurvivalBenefit(), normal));
                cell3.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell3);
                PdfPCell cell4 = new PdfPCell(new Phrase(
                        list_data.get(
                                i).getOtherBenefit(), normal));
                cell4.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell4.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell4);
                PdfPCell cell5 = new PdfPCell(new Phrase(list_data.get(i)
                        .getGuaranteed_maturity_benefit(), normal));
                cell5.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell5.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell5);

                PdfPCell cell6 = new PdfPCell(new Phrase(list_data.get(i)
                        .getGuaranteed_death_benefit(), normal));
                cell6.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell6.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell6);
                PdfPCell cell7 = new PdfPCell(new Phrase(list_data.get(i)
                        .getGuaranteed_surrender_value(), normal));
                cell7.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell7.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell7);

                PdfPCell cell8 = new PdfPCell(new Phrase(list_data.get(i)
                        .getSpecialSurrenderValue(), normal));
                cell8.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell8.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell8);


            }

            document.add(table1);
            //document.add(table);

            document.add(para_img_logo_after_space_1);


            para = new Paragraph("Notes : ", small_bold);

            document.add(para);

            para = new Paragraph("1. Annualized premium shall be the premium amount payable in a year chosen by the policyholder, excluding the taxes,  underwriting extra premiums and loading for modal premiums, if any. Refer sales literature for explanation of terms used in this illustration.																		\r\n"
                    + "2. All Benefit amount are derived on the assumption that the policies are 'in-force'																		\r\n"
                    + "3. Death Benefit shown above is at the end of the policy year.																		\r\n"
                    + "4. In life Income option, Guaranteed income would stop on death of the life assured. In Guaranteed Income Option, Guaranteed income would continue during the payout period even after the death of the life assured.", small_normal);

            document.add(para);


            document.add(para_img_logo_after_space_1);

            para = new Paragraph("Important : ", small_bold);

            document.add(para);
            document.add(para_img_logo_after_space_1);

            para = new Paragraph("You may receive a Welcome Call from our representative to confirm your proposal details like Date of Birth, Nominee Name, Address, Email ID, Sum Assured, Premium amount, Premium Payment Term etc.																		\r\n"
                    + "", small_normal);

            document.add(para);
            document.add(para_img_logo_after_space_1);

            para = new Paragraph("You may have to undergo Medical tests based on our underwriting requirements.																		\r\n"
                    + "", small_normal);

            document.add(para);
            document.add(para_img_logo_after_space_1);


            Calendar present_date = Calendar.getInstance();
            int mDay = present_date.get(Calendar.DAY_OF_MONTH);
            int mMonth = present_date.get(Calendar.MONTH);
            int mYear = present_date.get(Calendar.YEAR);

            String CurrentDate = mDay + "-" + (mMonth + 1) + "-" + mYear;

            PdfPTable BI_Pdftable26 = new PdfPTable(1);
            BI_Pdftable26.setWidthPercentage(100);
            // CR_table6.setWidths(columnWidths_26);
            PdfPCell BI_Pdftable26_cell1 = new PdfPCell(
                    new Paragraph(
                            "I, "
                                    + name_of_person
                                    + "     having received the information with respect to the above, have understood the above statement before entering into the contract.",
                            small_bold));

            BI_Pdftable26_cell1.setPadding(5);

            BI_Pdftable26.addCell(BI_Pdftable26_cell1);
            document.add(BI_Pdftable26);
            document.add(BI_Pdftable26_cell1);

            if (!bankUserType.equalsIgnoreCase("Y")) {
                PdfPTable BI_PdftablePolicyHolder = new PdfPTable(1);
                BI_PdftablePolicyHolder.setWidthPercentage(100);
                PdfPCell BI_PdftablePolicyHolder_signature_cell = new PdfPCell(
                        new Paragraph("PolicyHolder's Signature", small_bold));
                BI_PdftablePolicyHolder_signature_cell
                        .setBackgroundColor(BaseColor.LIGHT_GRAY);
                BI_PdftablePolicyHolder_signature_cell
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftablePolicyHolder_signature_cell.setPadding(5);
                BI_PdftablePolicyHolder
                        .addCell(BI_PdftablePolicyHolder_signature_cell);
                document.add(BI_PdftablePolicyHolder);
            }
            PdfPTable BI_PdftablePolicyHolder_signature;

            if (!bankUserType.equalsIgnoreCase("Y")) {
                BI_PdftablePolicyHolder_signature = new PdfPTable(3);
            } else {
                BI_PdftablePolicyHolder_signature = new PdfPTable(2);
            }

            BI_PdftablePolicyHolder_signature.setWidthPercentage(100);
            PdfPCell BI_PdftablePolicyHolder_signature_1 = new PdfPCell(
                    new Paragraph("Place :" + place2, small_normal));
            PdfPCell BI_PdftablePolicyHolder_signature_2 = new PdfPCell(
                    new Paragraph("Date  :" + CurrentDate, small_normal));

            PdfPCell BI_PdftablePolicyHolder_signature_3 = new PdfPCell();
            if (!bankUserType.equalsIgnoreCase("Y")) {
                byte[] fbyt_Proposer = Base64.decode(proposer_sign, 0);
                Bitmap Proposerbitmap = BitmapFactory.decodeByteArray(
                        fbyt_Proposer, 0, fbyt_Proposer.length);

                BI_PdftablePolicyHolder_signature_3.setFixedHeight(60f);
                ByteArrayOutputStream PolicyHolder_signature_stream = new ByteArrayOutputStream();
                (Proposerbitmap).compress(Bitmap.CompressFormat.PNG, 50,
                        PolicyHolder_signature_stream);
                Image PolicyHolder_signature = Image
                        .getInstance(PolicyHolder_signature_stream
                                .toByteArray());
                BI_PdftablePolicyHolder_signature_3
                        .setImage(PolicyHolder_signature);
            }
            BI_PdftablePolicyHolder_signature_1.setPadding(5);
            BI_PdftablePolicyHolder_signature_2.setPadding(5);
            BI_PdftablePolicyHolder_signature
                    .addCell(BI_PdftablePolicyHolder_signature_1);
            BI_PdftablePolicyHolder_signature
                    .addCell(BI_PdftablePolicyHolder_signature_2);

            if (!bankUserType.equalsIgnoreCase("Y")) {
                BI_PdftablePolicyHolder_signature
                        .addCell(BI_PdftablePolicyHolder_signature_3);
            }
            document.add(BI_PdftablePolicyHolder_signature);
            document.add(new_line);

            PdfPTable agentDeclarationTable = new PdfPTable(1);
            agentDeclarationTable.setWidthPercentage(100);
            PdfPCell agentDeclaration = new PdfPCell(
                    new Paragraph("I, " + commonMethods.getUserName(context)
                            + " have explained the premiums and benefits under the product fully to the prospect/policyholder.",
                            small_bold));

            agentDeclaration.setPadding(5);
            agentDeclarationTable.addCell(agentDeclaration);
            document.add(agentDeclarationTable);

            document.add(new_line);
            if (!bankUserType.equalsIgnoreCase("Y")) {
                PdfPTable BI_PdftableMarketing = new PdfPTable(1);
                BI_PdftableMarketing.setWidthPercentage(100);
                PdfPCell BI_PdftableMarketing_signature_cell = new PdfPCell(
                        new Paragraph("Marketing official's Signature & Company Seal", small_bold));
                BI_PdftableMarketing_signature_cell
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableMarketing_signature_cell
                        .setBackgroundColor(BaseColor.LIGHT_GRAY);
                BI_PdftableMarketing_signature_cell.setPadding(5);

                BI_PdftableMarketing.addCell(BI_PdftableMarketing_signature_cell);
                document.add(BI_PdftableMarketing);

                PdfPTable BI_PdftableMarketing_signature = new PdfPTable(3);

                BI_PdftableMarketing_signature.setWidthPercentage(100);

                PdfPCell BI_PdftableMarketing_signature_1 = new PdfPCell(
                        new Paragraph("Place :" + place2, small_normal));
                PdfPCell BI_PdftableMarketing_signature_2 = new PdfPCell(
                        new Paragraph("Date  :" + CurrentDate, small_normal));

                byte[] fbyt_agent = Base64.decode(agent_sign, 0);
                Bitmap Agentbitmap = BitmapFactory.decodeByteArray(fbyt_agent,
                        0, fbyt_agent.length);

                /*
                 * PdfPCell BI_PdftableMarketing_signature_3 = new PdfPCell( new
                 * Paragraph(str_sign_header, small_normal));
                 */

                PdfPCell BI_PdftableMarketing_signature_3 = new PdfPCell();
                BI_PdftableMarketing_signature_3.setFixedHeight(60f);
                ByteArrayOutputStream Marketing_officials_signature_stream = new ByteArrayOutputStream();

                (Agentbitmap).compress(Bitmap.CompressFormat.PNG, 50,
                        Marketing_officials_signature_stream);

                Image Marketing_officials_signature = Image
                        .getInstance(Marketing_officials_signature_stream
                                .toByteArray());

                BI_PdftableMarketing_signature_3
                        .setImage(Marketing_officials_signature);

                BI_PdftableMarketing_signature_1.setPadding(5);
                BI_PdftableMarketing_signature_2.setPadding(5);

                BI_PdftableMarketing_signature
                        .addCell(BI_PdftableMarketing_signature_1);
                BI_PdftableMarketing_signature
                        .addCell(BI_PdftableMarketing_signature_2);
                BI_PdftableMarketing_signature
                        .addCell(BI_PdftableMarketing_signature_3);
                document.add(BI_PdftableMarketing_signature);
            }
            document.close();
            pdf_writer.close();

        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Message" + e.getMessage());
            // TODO: handle exception
        }

    }


    public boolean valBackdate() {
        if (proposer_Backdating_WishToBackDate_Policy.equals("y")) {

            if (proposer_Backdating_BackDate.equals("")) {
                commonMethods.showMessageDialog(context,"Please Select Backdate ");
                            commonMethods.setFocusable(btn_proposerdetail_personaldetail_backdatingdate);
                            btn_proposerdetail_personaldetail_backdatingdate
                                    .requestFocus();

                return false;
            } else if (Integer.parseInt(spnr_Age.getSelectedItem().toString()) < 18) {
                commonMethods.showMessageDialog(context,"Backdate is not allowed for minor Life Assured");
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    boolean TrueBackdate() {

        try {

            String error = "";

            if (rb_proposerdetail_personaldetail_backdating_yes.isChecked()) {

                final Calendar c = Calendar.getInstance();
                final int currYear = c.get(Calendar.YEAR);
                final int currMonth = c.get(Calendar.MONTH) + 1;

                SimpleDateFormat dateformat1 = new SimpleDateFormat(
                        "dd-MM-yyyy");
                Date dtBackDate = dateformat1
                        .parse(btn_proposerdetail_personaldetail_backdatingdate
                                .getText().toString());
                Date currentDate = c.getTime();

                Date finYerEndDate = null;

                if (currMonth >= 4) {
                    finYerEndDate = dateformat1.parse("1-4-" + currYear);
                } else {
                    finYerEndDate = dateformat1.parse("1-4-" + (currYear - 1));
                }
                Date launchDate = dateformat1.parse("01-12-2021");

                if (currentDate.before(dtBackDate)) {
                    error = "Please enter backdation date between "
                            + dateformat1.format(finYerEndDate) + " and "
                            + dateformat1.format(currentDate);
                } else if (dtBackDate.before(launchDate)
                        && finYerEndDate.before(launchDate)) {
                    error = "Please enter Backdation date between "
                            + dateformat1.format(launchDate) + " and "
                            + dateformat1.format(currentDate);
                } else if (dtBackDate.before(finYerEndDate)) {
                    error = "Please enter Backdation date between "
                            + dateformat1.format(finYerEndDate) + " and "
                            + dateformat1.format(currentDate);
                }

                if (!error.equals("")) {
                    commonMethods.showMessageDialog(context,error);
                                // apply focusable method
                                commonMethods.setFocusable(btn_proposerdetail_personaldetail_backdatingdate);
                                btn_proposerdetail_personaldetail_backdatingdate
                                        .requestFocus();
                    return false;
                }

            }

        } catch (Exception e) {
            return false;
        }

        return true;

    }

    public boolean valDoYouBackdate() {
        if (!proposer_Backdating_WishToBackDate_Policy.equals("")) {
            return true;
        } else {
            commonMethods.showMessageDialog(context,"Please Select Do you wish to Backdate ");
                        commonMethods.setFocusable(rb_proposerdetail_personaldetail_backdating_yes);
                        rb_proposerdetail_personaldetail_backdating_yes
                                .requestFocus();
            return false;

        }
    }

    private int calculateMyAge(int year, int month, int day) {
        Calendar nowCal = new GregorianCalendar(year, month, day);

        String[] ProposerDob = getDate(lifeAssured_date_of_birth).split("-");

        int age = nowCal.get(Calendar.YEAR) - Integer.parseInt(ProposerDob[2]);

        boolean isMonthGreater = Integer.parseInt(ProposerDob[1]) > nowCal
                .get(Calendar.MONTH);

        boolean isMonthSameButDayGreater = Integer.parseInt(ProposerDob[1]) == nowCal
                .get(Calendar.MONTH)
                && Integer.parseInt(ProposerDob[0]) > nowCal
                .get(Calendar.DAY_OF_MONTH);

        if (isMonthGreater || isMonthSameButDayGreater) {
            age = age - 1;
        }
        return age;
    }

    public void onClickBackDating(View v) {
        String backdate = commonMethods.getMMDDYYYYDatabaseDate(proposer_Backdating_BackDate);
        initialiseDateParameter(backdate, 0);
        DIALOG_ID = 6;
        if (lifeAssured_date_of_birth != null
                && !lifeAssured_date_of_birth.equals("")) {
            showDateDialog();
        } else {
            commonMethods.dialogWarning(context,"Please select a LifeAssured DOB First", true);
        }
    }

    void valAge() {
        int minLimit = 0;
        int maxLimit = 60;
        if (str_user_POS_User.equalsIgnoreCase("POSP") || str_user_POS_User.equalsIgnoreCase("POSPRA")) {
            maxLimit = 42;
        }
        if (Integer.parseInt(spnr_Age.getSelectedItem().toString()) < minLimit || Integer.parseInt(spnr_Age.getSelectedItem().toString()) > maxLimit) {
            commonMethods.showMessageDialog(context,"Enter age between " + minLimit + " to " + maxLimit);
            btn_bi_smart_guaranteed_saving_plan_life_assured_date.setText("Select Date");
            btn_bi_smart_guaranteed_saving_plan_life_assured_date.requestFocus();
            lifeAssured_date_of_birth = "";

        }
    }

    Boolean valSA(String SumAssured) {
        StringBuilder error = new StringBuilder();

        if ((Double.parseDouble(SumAssured) > 2500000) && (str_user_POS_User.equalsIgnoreCase("POSP") || str_user_POS_User.equalsIgnoreCase("POSPRA"))) {
            error.append("Max. Sum assured is 25,00,000");

        }
        if (!error.toString().equals("")) {
            commonMethods.showMessageDialog(context,error.toString());
            return false;
        } else
            return true;
    }

}
