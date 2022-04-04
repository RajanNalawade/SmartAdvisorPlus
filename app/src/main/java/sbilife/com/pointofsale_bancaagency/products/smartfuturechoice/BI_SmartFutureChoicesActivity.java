package sbilife.com.pointofsale_bancaagency.products.smartfuturechoice;

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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
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

public class BI_SmartFutureChoicesActivity extends AppCompatActivity implements
        OnEditorActionListener {
    private int MobileLenth = 10;
    private String ExpiredDate;
    private String formattedDate;

    private String QuatationNumber;
    private String planName = "";

    private Spinner spinner_select_plan, spinner_select_bonus, spinner_age,
            spinner_policyterm, spinner_premium_payment_term,
            spinner_premiumfreq;
    private EditText et_sumassured, et_adtpd_benifit;
    private Button btnSubmit;
    private CheckBox cb_staffdisc;
    private final SmartFutureChoicesBean smartbachatbean = new SmartFutureChoicesBean();
    private final CommonForAllProd commonforall = new CommonForAllProd();

    private RadioButton rb_smart_bachat_backdating_yes, rb_smart_bachat_backdating_no;
    private LinearLayout ll_backdating1;
    private TextView btn_smart_bachat_backdatingdate;

    private double PremiumBeforeST;

    /* Basic Details */
    private String mobileNo = "";
    private String emailId = "";
    private String ConfirmEmailId = "";
    private String EMAIL_PATTERN;
    private Pattern pattern;
    private Matcher matcher;
    private boolean validationFla1 = false;
    private EditText edt_proposerdetail_basicdetail_contact_no,
            edt_proposerdetail_basicdetail_Email_id,
            edt_proposerdetail_basicdetail_ConfirmEmail_id;
    private TextView btn_bi_smart_future_choices_life_assured_date;
    private Spinner spnr_bi_smart_bachat_life_assured_title, spnr_bi_smart_future_choices_selGender, spinnerProposerGender;
    private EditText edt_bi_smart_bachat_life_assured_first_name,
            edt_bi_smart_bachat_life_assured_middle_name,
            edt_bi_smart_bachat_life_assured_last_name;

    private int DIALOG_ID;
    private int mYear;
    private int mMonth;
    private int mDay;

    private DatabaseHelper db;
    private CommonForAllProd obj;
    private ParseXML prsObj;
    private String ProposerEmailId = "";
    private Bitmap photoBitmap;
    private String flg_needAnalyis = "";

    private String output = "";

    private String age_entry = "";
    private String gender = "", gender_proposer = "";

    private String premium_paying_frequency = "";
    private String plan = "";
    private String bonusOption = "";
    private String policy_term = "";
    private String sum_assured = "";
    private String lifeAssured_Title = "", lifeAssured_First_Name = "",
            lifeAssured_Middle_Name = "", lifeAssured_Last_Name = "",
            name_of_life_assured = "";
    private String lifeAssured_date_of_birth = "";
    private String proposer_Title = "", proposer_First_Name = "",
            proposer_Middle_Name = "", proposer_Last_Name = "",
            name_of_proposer = "",
            proposer_date_of_birth = "",
            proposer_Is_Same_As_Life_Assured = "Y", lifeAssuredAge = "";

    private String proposer_Backdating_WishToBackDate_Policy = "";
    private String proposer_Backdating_BackDate = "";
    private String name_of_person = "";
    private String place2 = "";
    private String date1 = "";
    private String date2 = "";
    private String agent_sign = "";
    private String proposer_sign = "";
    private String staffdiscount = "";
    private String isJkResident = "";
    private StringBuilder inputVal, retVal, bussIll;
    private String premPayTerm = "";

    private ImageButton Ibtn_signatureofMarketing;
    private ImageButton Ibtn_signatureofPolicyHolders;

    private Button btn_MarketingOfficalDate;
    private Button btn_PolicyholderDate;
    private List<M_BI_SmartFutureChoices_Adapter> list_data;


    private String basicServiceTax = "";
    private String servicetax = "";
    private final String servcTaxSecondYear = "";
    private String premWthSTSecondYear = "";
    private DecimalFormat currencyFormat, decimalcurrencyFormat;
    private String BasicInstallmentPremSecondYear = "", BasicInstallmentPremFirstYear = "", SumAssuredBasic = "", installmntPrem = "";

    private String BackDateinterest = "", BackDateinterestwithGST = "";
    private final String MinesOccuInterest = "";
    private String staffRebate = "";
    private String staffStatus = "";

    private TableRow tr_smart_bachat_proposer_detail1,
            tr_smart_bachat_proposer_detail12, tr_smart_bachat_proposer_detail13, tr_smart_bachat_proposer_gender_detail3, tr_smart_bachat_gender_stmt_proposer_detail31,
            tr_smart_bachat_proposer_detail2, tr_smart_bachat_proposer_detail22;
    private Spinner spnr_bi_smart_future_choices_proposer_title;
    private EditText edt_bi_smart_future_choices_proposer_last_name,
            edt_bi_smart_future_choices_proposer_middle_name,
            edt_bi_smart_future_choices_proposer_first_name;
    private TextView btn_bi_smart_future_choices_proposer_date;
    private String ProposerAge = "";

    private final String is_Bahrain = "";


    private CheckBox cb_kerladisc;
    private String str_kerla_discount = "No";
    private String STR_MAT_VALUE_PROD_CHANGE = "";
    private String STR_MAT_VALUE_PROD_QUT_NO = "";

    private String Smart_bachat_sum_assured_on_death = "";

    private double premBasicRoundUp = 0;
    private final double premBasicRoundedOff2 = 0;
    private double Sumassured_Basic = 0;
    private double rateofprem = 0;
    private String premiumSingleInstBasic, AnnualizedPrem, SurvivalBenefit, SurrebderBenefit, GuaranteedBenefit, GuaranteedMaturityBen, CashBonus4percent, CashBonus8percent, Defferedcash4per, Defferedcash8per, SurrenderBen4Per, SurrenderBen8Per, totalmaturity4per, totalmaturity8per, totalDeathBen4per, totalDeathBen8per, premInstBasicFirstYear, premInstBasicSecondYear;

    private RadioButton rb_cash_bonus, rb_deferred_cash_bonus;
    private String str_bonus_type = "";

    private final String proposer_isSmoker = "";
    private String isSmoker = "";
    private Context context;
    private CommonMethods commonMethods;
    private StorageUtils mStorageUtils;
    private String agentcode, agentMobile, agentEmail, userType;
    private String product_Code, product_UIN, product_cateogory, product_type;
    private int needAnalysis_flag = 0;
    private String na_input = null;
    private String na_output = null;
    private String bankUserType = "", transactionMode = "";
    private NeedAnalysisBIService NABIObj;
    private NA_CBI_bean na_cbi_bean;
    private File needAnalysispath, newFile, mypath;
    private ImageButton imageButtonProposerPhotograph;
    private String Check = "";
    private String latestImage = "";
    private String serviceTaxSecondYear;
    private String premiumSingleInstBasicWithST, premiumSingleInstBasicWithSTSecondYear;
    private String serviceTax;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.bi_smartfuturechoices);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        commonMethods = new CommonMethods();
        mStorageUtils = new StorageUtils();
        commonMethods.setApplicationToolbarMenu(this, getString(R.string.app_name));
        context = this;
        NABIObj = new NeedAnalysisBIService(this);
        list_data = new ArrayList<M_BI_SmartFutureChoices_Adapter>();

        // list_data = new ArrayList<M_BI_SmartMoneyBackGold_Adapter>();
        initialiseDate();
        db = new DatabaseHelper(this);

        currencyFormat = new DecimalFormat("##,##,##,###");
        Intent intent = getIntent();
        String na_flag = intent.getStringExtra("NAFlag");

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
            planName = getString(R.string.sbi_life_smart_future_choices);
            product_Code = getString(R.string.sbi_life_smart_future_choices_code);
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



        STR_MAT_VALUE_PROD_CHANGE = intent.getStringExtra("MAT_VALUE_PROD_CHANGE");
        STR_MAT_VALUE_PROD_QUT_NO = intent.getStringExtra("MAT_VALUE_PROD_QUT_NO");

        if (STR_MAT_VALUE_PROD_CHANGE == null) {
            STR_MAT_VALUE_PROD_CHANGE = "";
        }
        if (STR_MAT_VALUE_PROD_QUT_NO == null) {
            STR_MAT_VALUE_PROD_QUT_NO = "";
        }
        obj = new CommonForAllProd();


        prsObj = new ParseXML();
        retVal = new StringBuilder();

        rb_cash_bonus = (RadioButton) findViewById(R.id.rb_cash_bonus);
        rb_deferred_cash_bonus = (RadioButton) findViewById(R.id.rb_deferred_cash_bonus);


        spinner_select_plan = (Spinner) findViewById(R.id.spinner_select_plan);
        spinner_select_bonus = (Spinner) findViewById(R.id.spinner_select_bonus);
        spinner_age = (Spinner) findViewById(R.id.spinner_age);

        spinner_age.setClickable(false);
        spinner_age.setEnabled(false);

        spinner_policyterm = (Spinner) findViewById(R.id.spinner_policyterm);
        spinner_premium_payment_term = (Spinner) findViewById(R.id.spinner_premium_payment_term);
        spinner_premiumfreq = (Spinner) findViewById(R.id.spinner_premiumfreq);

        et_sumassured = (EditText) findViewById(R.id.et_sumassured);
        et_adtpd_benifit = (EditText) findViewById(R.id.et_adtpd_benifit);
        et_adtpd_benifit.setClickable(false);
        et_adtpd_benifit.setEnabled(false);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        cb_staffdisc = (CheckBox) findViewById(R.id.cb_staffdisc);

        spnr_bi_smart_bachat_life_assured_title = (Spinner) findViewById(R.id.spnr_bi_smart_bachat_life_assured_title);
        edt_bi_smart_bachat_life_assured_first_name = (EditText) findViewById(R.id.edt_bi_smart_bachat_life_assured_first_name);
        edt_bi_smart_bachat_life_assured_middle_name = (EditText) findViewById(R.id.edt_bi_smart_bachat_life_assured_middle_name);
        edt_bi_smart_bachat_life_assured_last_name = (EditText) findViewById(R.id.edt_bi_smart_bachat_life_assured_last_name);

        spnr_bi_smart_future_choices_selGender = findViewById(R.id.spnr_bi_smart_future_choices_selGender);
        spinnerProposerGender = findViewById(R.id.spinnerProposerGender);
        btn_bi_smart_future_choices_life_assured_date = findViewById(R.id.btn_bi_smart_future_choices_life_assured_date);

        edt_proposerdetail_basicdetail_contact_no = (EditText) findViewById(R.id.edt_smart_bachat_contact_no);
        edt_proposerdetail_basicdetail_Email_id = (EditText) findViewById(R.id.edt_smart_bachat_Email_id);
        edt_proposerdetail_basicdetail_ConfirmEmail_id = (EditText) findViewById(R.id.edt_smart_bachat_ConfirmEmail_id);

        rb_smart_bachat_backdating_yes = (RadioButton) findViewById(R.id.rb_smart_bachat_backdating_yes);
        rb_smart_bachat_backdating_no = (RadioButton) findViewById(R.id.rb_smart_bachat_backdating_no);
        ll_backdating1 = (LinearLayout) findViewById(R.id.ll_backdating1);
        btn_smart_bachat_backdatingdate = findViewById(R.id.btn_smart_bachat_backdatingdate);

        // proposer
        edt_bi_smart_future_choices_proposer_last_name = (EditText) findViewById(R.id.edt_bi_smart_future_choices_proposer_last_name);
        edt_bi_smart_future_choices_proposer_middle_name = (EditText) findViewById(R.id.edt_bi_smart_future_choices_proposer_middle_name);
        edt_bi_smart_future_choices_proposer_first_name = (EditText) findViewById(R.id.edt_bi_smart_future_choices_proposer_first_name);
        spnr_bi_smart_future_choices_proposer_title = (Spinner) findViewById(R.id.spnr_bi_smart_future_choices_proposer_title);
        tr_smart_bachat_proposer_detail1 = (TableRow) findViewById(R.id.tr_smart_bachat_proposer_detail1);
        tr_smart_bachat_proposer_detail12 = (TableRow) findViewById(R.id.tr_smart_bachat_proposer_detail12);
        tr_smart_bachat_proposer_gender_detail3 = (TableRow) findViewById(R.id.tr_smart_bachat_proposer_gender_detail3);
        tr_smart_bachat_gender_stmt_proposer_detail31 = (TableRow) findViewById(R.id.tr_smart_bachat_gender_stmt_proposer_detail31);

        tr_smart_bachat_proposer_detail13 = (TableRow) findViewById(R.id.tr_smart_bachat_proposer_detail13);
        tr_smart_bachat_proposer_detail2 = (TableRow) findViewById(R.id.tr_smart_bachat_proposer_detail2);
        tr_smart_bachat_proposer_detail22 = (TableRow) findViewById(R.id.tr_smart_bachat_proposer_detail22);
        btn_bi_smart_future_choices_proposer_date = findViewById(R.id.btn_bi_smart_future_choices_proposer_date);

        commonMethods.fillSpinnerValue(context, spnr_bi_smart_bachat_life_assured_title,
                Arrays.asList(getResources().getStringArray(R.array.arr_select_title)));
        commonMethods.fillSpinnerValue(context, spnr_bi_smart_future_choices_proposer_title,
                Arrays.asList(getResources().getStringArray(R.array.arr_select_title)));

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, getResources().getStringArray(R.array.gender_all_arrays));
        genderAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_bi_smart_future_choices_selGender.setAdapter(genderAdapter);
        genderAdapter.notifyDataSetChanged();

        ArrayAdapter<String> genderAdapter_prop = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, getResources().getStringArray(R.array.gender_all_arrays));
        genderAdapter_prop.setDropDownViewResource(R.layout.spinner_item1);
        spinnerProposerGender.setAdapter(genderAdapter);
        genderAdapter_prop.notifyDataSetChanged();

        String[] ageList = new String[33];
        for (int i = 18; i <= 50; i++) {
            ageList[i - 18] = i + "";
        }
        ArrayAdapter<String> ageAdapter = new ArrayAdapter<String>(
                getApplicationContext(), R.layout.spinner_item, ageList);
        ageAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spinner_age.setAdapter(ageAdapter);
        ageAdapter.notifyDataSetChanged();

        String[] planList = {
                "Classic Choice",
                "Flexi Choice"};
        ArrayAdapter<String> planAdapter = new ArrayAdapter<String>(
                getApplicationContext(), R.layout.spinner_item, planList);
        planAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spinner_select_plan.setAdapter(planAdapter);
        planAdapter.notifyDataSetChanged();

        String[] bonusList = {
                "Cash Bonus",
                "Deferred Cash Bonus"};
        ArrayAdapter<String> bonusAdapter = new ArrayAdapter<String>(
                getApplicationContext(), R.layout.spinner_item, bonusList);
        planAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spinner_select_bonus.setAdapter(bonusAdapter);
        bonusAdapter.notifyDataSetChanged();


        String[] policyterm = {"12", "15", "20", "25", "30"};

        ArrayAdapter<String> policytermadapter = new ArrayAdapter<String>(
                getApplicationContext(), R.layout.spinner_item, policyterm);
        policytermadapter.setDropDownViewResource(R.layout.spinner_item1);
        spinner_policyterm.setAdapter(policytermadapter);
        policytermadapter.notifyDataSetChanged();

        String[] premFreqList = {"Yearly", "Half-Yearly", "Monthly"};
        ArrayAdapter<String> premFreqAdapter = new ArrayAdapter<String>(
                getApplicationContext(), R.layout.spinner_item, premFreqList);
        premFreqAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spinner_premiumfreq.setAdapter(premFreqAdapter);
        premFreqAdapter.notifyDataSetChanged();

        //String[] premiumpayingterm = {"6", "7", "10", "15"};
        String[] premiumpayingterm = {"7", "10", "12", "15"};

        ArrayAdapter<String> premiumpayingtermadapter = new ArrayAdapter<String>(
                getApplicationContext(), R.layout.spinner_item,
                premiumpayingterm);
        premiumpayingtermadapter
                .setDropDownViewResource(R.layout.spinner_item1);
        spinner_premium_payment_term.setAdapter(premiumpayingtermadapter);
        premiumpayingtermadapter.notifyDataSetChanged();


        cb_kerladisc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_kerladisc.isChecked()) {
                    str_kerla_discount = "Yes";
                    clearFocusable(cb_kerladisc);
                    commonMethods.setFocusable(spnr_bi_smart_bachat_life_assured_title);
                    spnr_bi_smart_bachat_life_assured_title.requestFocus();
                } else {
                    str_kerla_discount = "No";
                    clearFocusable(cb_kerladisc);
                    commonMethods.setFocusable(spnr_bi_smart_bachat_life_assured_title);
                    spnr_bi_smart_bachat_life_assured_title.requestFocus();
                }
            }
        });

        spinner_policyterm
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int pos, long arg3) {


                        if (pos == 0) {
                            String[] premiumpayingterm = {"7"};

                            ArrayAdapter<String> premiumpayingtermadapter = new ArrayAdapter<String>(
                                    getApplicationContext(),
                                    R.layout.spinner_item, premiumpayingterm);
                            premiumpayingtermadapter
                                    .setDropDownViewResource(R.layout.spinner_item1);
                            spinner_premium_payment_term
                                    .setAdapter(premiumpayingtermadapter);
                            premiumpayingtermadapter.notifyDataSetChanged();
                        } else if (pos == 1) {

                            String[] premiumpayingterm = {"7", "10"};

                            ArrayAdapter<String> premiumpayingtermadapter = new ArrayAdapter<String>(
                                    getApplicationContext(),
                                    R.layout.spinner_item, premiumpayingterm);
                            premiumpayingtermadapter
                                    .setDropDownViewResource(R.layout.spinner_item1);
                            spinner_premium_payment_term
                                    .setAdapter(premiumpayingtermadapter);
                            premiumpayingtermadapter.notifyDataSetChanged();

                        } else if (pos == 2 || pos == 3) {

                            String[] premiumpayingterm = {"7", "10", "12", "15"};

                            ArrayAdapter<String> premiumpayingtermadapter = new ArrayAdapter<String>(
                                    getApplicationContext(),
                                    R.layout.spinner_item, premiumpayingterm);
                            premiumpayingtermadapter
                                    .setDropDownViewResource(R.layout.spinner_item1);
                            spinner_premium_payment_term
                                    .setAdapter(premiumpayingtermadapter);
                            premiumpayingtermadapter.notifyDataSetChanged();

                        } else if (pos == 4) {

                            String[] premiumpayingterm = {"10", "12", "15"};

                            ArrayAdapter<String> premiumpayingtermadapter = new ArrayAdapter<String>(
                                    getApplicationContext(),
                                    R.layout.spinner_item, premiumpayingterm);
                            premiumpayingtermadapter
                                    .setDropDownViewResource(R.layout.spinner_item1);
                            spinner_premium_payment_term
                                    .setAdapter(premiumpayingtermadapter);
                            premiumpayingtermadapter.notifyDataSetChanged();

                        }

                        spinner_policyterm.setFocusable(false);
                        clearFocusable(spinner_policyterm);
                        commonMethods.setFocusable(spinner_premium_payment_term);
                        spinner_premium_payment_term.requestFocus();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        spinner_select_plan
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int pos, long arg3) {
                        // TODO Auto-generated method stub

                        spinner_select_plan.setFocusable(false);
                        clearFocusable(spinner_select_plan);
                        commonMethods.setFocusable(spinner_policyterm);
                        spinner_policyterm.requestFocus();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        spinner_select_bonus.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int pos, long arg3) {
                // TODO Auto-generated method stub
                bonusOption = spinner_select_bonus.getSelectedItem().toString();
                spinner_select_bonus.setFocusable(false);
                clearFocusable(spinner_select_bonus);

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        spinner_premium_payment_term
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int pos, long arg3) {

                        spinner_premium_payment_term.setFocusable(false);
                        clearFocusable(spinner_premium_payment_term);
                        commonMethods.setFocusable(spinner_premiumfreq);
                        spinner_premiumfreq.requestFocus();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        spinner_premiumfreq
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int pos, long arg3) {

                        spinner_premiumfreq.setFocusable(false);
                        clearFocusable(spinner_premiumfreq);

                        if (spnr_bi_smart_bachat_life_assured_title
                                .getSelectedItem().toString()
                                .equalsIgnoreCase("Select Title")) {
                            clearFocusable(spinner_premiumfreq);
                            commonMethods.setFocusable(spnr_bi_smart_bachat_life_assured_title);
                            spnr_bi_smart_bachat_life_assured_title
                                    .requestFocus();
                        } else {
                            clearFocusable(spinner_premiumfreq);
                            commonMethods.setFocusable(et_sumassured);
                            et_sumassured.requestFocus();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });
        spnr_bi_smart_bachat_life_assured_title
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long id) {
                        // TODO Auto-generated method stub
                        if (position > 0) {
                            lifeAssured_Title = spnr_bi_smart_bachat_life_assured_title
                                    .getSelectedItem().toString();
                            clearFocusable(spnr_bi_smart_bachat_life_assured_title);

                            commonMethods.setFocusable(edt_bi_smart_bachat_life_assured_first_name);

                            edt_bi_smart_bachat_life_assured_first_name
                                    .requestFocus();

                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        spnr_bi_smart_future_choices_proposer_title
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long id) {
                        // TODO Auto-generated method stub
                        if (position > 0) {
                            proposer_Title = spnr_bi_smart_future_choices_proposer_title
                                    .getSelectedItem().toString();

                            clearFocusable(spnr_bi_smart_future_choices_proposer_title);

                            commonMethods.setFocusable(edt_bi_smart_future_choices_proposer_first_name);

                            edt_bi_smart_future_choices_proposer_first_name
                                    .requestFocus();

                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        rb_smart_bachat_backdating_yes
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        // TODO Auto-generated method stub
                        if (isChecked) {
                            proposer_Backdating_WishToBackDate_Policy = "y";
                            ll_backdating1.setVisibility(View.VISIBLE);
                            btn_smart_bachat_backdatingdate
                                    .setText("Select Date");
                            proposer_Backdating_BackDate = "";

                            rb_smart_bachat_backdating_yes.setFocusable(false);
                            clearFocusable(rb_smart_bachat_backdating_yes);
                            commonMethods.setFocusable(btn_smart_bachat_backdatingdate);
                            btn_smart_bachat_backdatingdate.requestFocus();

                        }
                    }
                });

        rb_smart_bachat_backdating_no
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        if (isChecked) {
                            proposer_Backdating_WishToBackDate_Policy = "n";
                            if (proposer_Backdating_WishToBackDate_Policy
                                    .equalsIgnoreCase("Y")) {

                                commonMethods.showMessageDialog(context, "Please Select Life Assure Dob First");
                                btn_bi_smart_future_choices_life_assured_date
                                        .setText("Select Date");
                                lifeAssured_date_of_birth = "";
                                ll_backdating1
                                        .setVisibility(View.GONE);

                                proposer_Backdating_WishToBackDate_Policy = "N";
                                ll_backdating1.setVisibility(View.GONE);
                            } else {
                                proposer_Backdating_WishToBackDate_Policy = "n";
                                proposer_Backdating_BackDate = "";
                                // setDefaultDate();
                                ll_backdating1.setVisibility(View.GONE);

                                String lifeAssuredAge1 = lifeAssuredAge;

                                spinner_age.setSelection(
                                        getIndex(spinner_age, lifeAssuredAge),
                                        false);
                                // btn_smart_bachat_backdatingdate.setText("Select Date");
                                rb_smart_bachat_backdating_yes
                                        .setFocusable(false);

                            }

                        }
                    }
                });

        btnSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                inputVal = new StringBuilder();
                retVal = new StringBuilder();
                bussIll = new StringBuilder();

                proposer_First_Name = edt_bi_smart_future_choices_proposer_first_name
                        .getText().toString().trim();
                proposer_Middle_Name = edt_bi_smart_future_choices_proposer_middle_name
                        .getText().toString().trim();
                proposer_Last_Name = edt_bi_smart_future_choices_proposer_last_name
                        .getText().toString().trim();

                gender = spnr_bi_smart_future_choices_selGender.getSelectedItem().toString();
                gender_proposer = spinnerProposerGender.getSelectedItem().toString();
                name_of_proposer = proposer_Title + " " + proposer_First_Name
                        + " " + proposer_Middle_Name + " " + proposer_Last_Name;

                lifeAssured_First_Name = edt_bi_smart_bachat_life_assured_first_name
                        .getText().toString().trim();
                lifeAssured_Middle_Name = edt_bi_smart_bachat_life_assured_middle_name
                        .getText().toString().trim();
                lifeAssured_Last_Name = edt_bi_smart_bachat_life_assured_last_name
                        .getText().toString().trim();

                name_of_life_assured = lifeAssured_Title + " "
                        + lifeAssured_First_Name + " "
                        + lifeAssured_Middle_Name + " " + lifeAssured_Last_Name;

                mobileNo = edt_proposerdetail_basicdetail_contact_no.getText()
                        .toString();
                emailId = edt_proposerdetail_basicdetail_Email_id.getText()
                        .toString();
                ConfirmEmailId = edt_proposerdetail_basicdetail_ConfirmEmail_id
                        .getText().toString();
                Date();


                if (valLifeAssuredProposerDetail() && valDob() && valCashBonus()
                        && valProposerDob() && sumassuredval() && valPremAmount(Double.parseDouble(et_sumassured.getText().toString()))
                        && valBasicDetail() && matval() && valDoYouBackdate()
                        && valBackdate() && TrueBackdate()) {
                    addListenerOnSubmit();
                    getInput(smartbachatbean);

                    if (needAnalysis_flag == 0) {
                        Intent i = new Intent(BI_SmartFutureChoicesActivity.this,
                                Success.class);
                        /*i.putExtra("op", "Life Cover Basic Premium is Rs. " + InstallmentPremLifeCover_ExclSt);
                        i.putExtra("op1", "Life Cover Applicable Tax is Rs. " + ApplicableTaxesLifeCover);
                        i.putExtra("op2", "Life Cover Premium with Applicable Tax is Rs. " + InstallmentPremLifeCover_InclSt);

                        i.putExtra("op3", "Critical Illness Cover Basic Premium is Rs. " + TotalPremiumWithoutST_CI);
                        i.putExtra("op4", "Critical Illness Cover Basic Applicable Tax is Rs. " + ServiceTax_CI);
                        i.putExtra("op5", "Critical Illness Cover Basic Premium with Applicable Tax is Rs. " + TotalPremiumWithST_CI);

                        //          i.putExtra("op6","Total Basic Premium is Rs."+ installmntPremWithoutSerTx);
                        //          i.putExtra("op7","Total Applicable Tax is Rs."+ servTax);
                        i.putExtra("op6", "Total Premium with Applicable Tax is Rs. " + TotalFinalPremium_IncST);*/


                        i.putExtra("op", "Premium Before Applicable Taxes : Rs. " + currencyFormat.format(premBasicRoundUp));
                        i.putExtra("op1", "Applicable Taxes 1st Year : Rs. " + currencyFormat.format(Double.parseDouble(serviceTax)));
                        i.putExtra("op2", "Applicable Taxes 2nd Year Onwards : Rs. " + currencyFormat.format(Double.parseDouble(serviceTaxSecondYear)));
                        i.putExtra("op3", "Premium with Applicable Taxes 1st year : Rs. " + currencyFormat.format(Double.parseDouble(premiumSingleInstBasicWithST)));
                        i.putExtra("op4", "Premium with Applicable Taxes 2nd year onwards : Rs. " + currencyFormat.format(Double.parseDouble(premiumSingleInstBasicWithSTSecondYear)));
                        i.putExtra("op5", "Matuarity Benifit Guaranteed : Rs. " + currencyFormat.format(Double.parseDouble(GuaranteedMaturityBen)));
                        i.putExtra("op6", "Total Matuarity Benifit (Guaranteed+Non-Guaranteed) 4% : Rs. " + currencyFormat.format(Double.parseDouble(totalmaturity4per)));
                        i.putExtra("op7", "Total Matuarity Benifit (Guaranteed+Non-Guaranteed) 8% : Rs. " + currencyFormat.format(Double.parseDouble(totalmaturity8per)));
//
                        i.putExtra("header", getString(R.string.sbi_life_smart_future_choices));
                        i.putExtra("header1", "(" + getString(R.string.sbi_life_smart_future_choices_uin) + ")");
                        startActivity(i);

                    } else
                        Dialog();

                   /* if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {
                        proposer_Title = "";
                        proposer_First_Name = "";
                        proposer_Middle_Name = "";
                        proposer_Last_Name = "";
                        name_of_proposer = "";
                        proposer_date_of_birth = "";
                    }


                    isSync = true;
                    isFlag1 = true;
                    isFlag2 = true;
                    isFlag3 = true;
                    isFlag4 = true;
                    if (ValNAPremium()) {
                        long count = insertDataIntoDatabase();

                        if (count > 0) {
                            Dialog();
                        }
                    }*/
                }

            }

        });


        edt_bi_smart_bachat_life_assured_first_name
                .setOnEditorActionListener(this);
        edt_bi_smart_bachat_life_assured_middle_name
                .setOnEditorActionListener(this);
        edt_bi_smart_bachat_life_assured_last_name
                .setOnEditorActionListener(this);

        edt_bi_smart_future_choices_proposer_first_name.setOnEditorActionListener(this);
        edt_bi_smart_future_choices_proposer_middle_name
                .setOnEditorActionListener(this);
        edt_bi_smart_future_choices_proposer_last_name.setOnEditorActionListener(this);

        edt_proposerdetail_basicdetail_contact_no
                .setOnEditorActionListener(this);
        edt_proposerdetail_basicdetail_Email_id.setOnEditorActionListener(this);
        edt_proposerdetail_basicdetail_ConfirmEmail_id
                .setOnEditorActionListener(this);
        et_sumassured.setOnEditorActionListener(this);
        et_adtpd_benifit.setOnEditorActionListener(this);

        edt_proposerdetail_basicdetail_contact_no
                .addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence arg0, int arg1,
                                              int arg2, int arg3) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void beforeTextChanged(CharSequence arg0, int arg1,
                                                  int arg2, int arg3) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void afterTextChanged(Editable arg0) {
                        // TODO Auto-generated method stub
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
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void beforeTextChanged(CharSequence arg0, int arg1,
                                                  int arg2, int arg3) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void afterTextChanged(Editable arg0) {
                        // TODO Auto-generated method stub
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
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void beforeTextChanged(CharSequence arg0, int arg1,
                                                  int arg2, int arg3) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void afterTextChanged(Editable arg0) {
                        // TODO Auto-generated method stub
                        String proposer_confirm_emailId = edt_proposerdetail_basicdetail_ConfirmEmail_id
                                .getText().toString();
                        confirming_email_id(proposer_confirm_emailId);

                    }
                });
        et_sumassured.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {

                try {
                    if (!et_sumassured.getText().toString().equals("")) {
                        int sumAssured = Integer.parseInt(et_sumassured
                                .getText().toString());
                        if (sumAssured < 5000000)
                            et_adtpd_benifit.setText(sumAssured + "");
                        else {
                            et_adtpd_benifit.setText(5000000 + "");
                        }
                    } else
                        et_adtpd_benifit.setText(0 + "");
                } catch (Exception ignored) {
                }

            }

        });

        rb_cash_bonus.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    str_bonus_type = "Cash Bonus";
                }
            }
        });

        rb_deferred_cash_bonus.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    str_bonus_type = "Deferred Cash Bonus";
                }
            }
        });

        TableRow tr_staff_disc;


        tr_staff_disc = (TableRow) findViewById(R.id.tr_smart_bachat_staff_disc);

        // if (userType.equalsIgnoreCase("BAP")
        // || userType.equalsIgnoreCase("CAG")) {
        // tr_staff_disc.setVisibility(View.GONE);
        // }
        if (is_Bahrain.equalsIgnoreCase("TRUE")) {
            BahrainMethod();
        }


    }

    public void addListenerOnSubmit() {

        if (cb_staffdisc.isChecked()) {
            smartbachatbean.setIsForStaffOrNot(true);
        } else {
            smartbachatbean.setIsForStaffOrNot(false);
        }
        if (cb_kerladisc.isChecked()) {
            smartbachatbean.setKerlaDisc(true);
        } else {
            smartbachatbean.setKerlaDisc(false);
        }
        smartbachatbean.setIsJammuResident(false);


        if (proposer_isSmoker.equalsIgnoreCase("Smoker")) {
            smartbachatbean.setSmoker("Smoker");
        } else if (proposer_isSmoker.equalsIgnoreCase("Non-Smoker")) {
            smartbachatbean.setSmoker("Non-Smoker");
        }
        smartbachatbean.setPlanType(spinner_select_plan.getSelectedItem()
                .toString());
        smartbachatbean.setBonusOption(spinner_select_bonus.getSelectedItem()
                .toString());
        smartbachatbean.setAge(Integer.parseInt(spinner_age.getSelectedItem()
                .toString()));
        smartbachatbean.setAge(Integer.parseInt(spinner_age.getSelectedItem().toString()));
        smartbachatbean.setGender(gender);
        smartbachatbean.setPolicyterm(Integer.parseInt(spinner_policyterm
                .getSelectedItem().toString()));
        smartbachatbean.setPremiumpayingterm(Integer
                .parseInt(spinner_premium_payment_term.getSelectedItem()
                        .toString()));
        smartbachatbean.setPremiumFrequency(spinner_premiumfreq
                .getSelectedItem().toString());
        smartbachatbean.setSumAssured(Integer.parseInt(et_sumassured.getText()
                .toString()));
        smartbachatbean.setBancAssuranceDisc(false);
        showSmartBachatOutputPg(smartbachatbean);
    }

    /************************** Output STarts Here ****************/
    // Display Smart Scholar Output Screen
    private void showSmartBachatOutputPg(SmartFutureChoicesBean smartfuturechoicesBean) {
        //Variable declaration
        //StringBuilder retVal =  new StringBuilder();
        String BI = "";
        int year_F = 0;

        if (cb_staffdisc.isChecked()) {

            staffStatus = "sbi";

        } else {
            staffStatus = "none";
        }
        boolean state = smartfuturechoicesBean.isKerlaDisc();


        //Class Declaration
        SmartFutureChoicesProperties prop = new SmartFutureChoicesProperties();
        SmartFutureChoicesBusinessLogic smartfuturechoicesBusinessLogic = new SmartFutureChoicesBusinessLogic(smartfuturechoicesBean);
       /*// String PremiumBasic1 = commonforall.getRound(commonforall.getStringWithout_E(smartfuturechoicesBean.getSumAssured()));
        String PremiumBasic = commonforall.getRound(commonforall.getStringWithout_E(smartfuturechoicesBean.getSumAssured()));
        String rates = (commonforall.getStringWithout_E(smartfuturechoicesBusinessLogic.getPR_Basic_FromDB(smartfuturechoicesBean.getAge(), smartfuturechoicesBean.getPremiumpayingterm(), smartfuturechoicesBean.getPolicyterm())));
        rateofprem = Double.parseDouble((rates));*/

        staffRebate = smartfuturechoicesBusinessLogic
                .getStaffRebate(cb_staffdisc.isChecked()) + "";

        String PremiumBasic1 = commonforall.getRound(commonforall.getStringWithout_E(smartfuturechoicesBean.getSumAssured()));
        String PremiumBasic = commonforall.getRound(commonforall.getStringWithout_E(smartfuturechoicesBusinessLogic.getInstallmentPremium(smartfuturechoicesBean.getPremiumFrequency(), smartfuturechoicesBean.getSumAssured())));
        String rates = (commonforall.getStringWithout_E(smartfuturechoicesBusinessLogic.getPR_Basic_FromDB(smartfuturechoicesBean.getAge(), smartfuturechoicesBean.getPremiumpayingterm(), smartfuturechoicesBean.getPolicyterm())));
        rateofprem = Double.parseDouble((rates));

        String getSumAssured = (commonforall.getStringWithout_E(smartfuturechoicesBusinessLogic.getSumassured(smartfuturechoicesBean.getSumAssured(), rateofprem)));
        premBasicRoundUp = Double.parseDouble(commonforall.getRound(PremiumBasic));
        Sumassured_Basic = Double.parseDouble((getSumAssured));

        //	System.out.println("abc "+Sumassured_Basic);
//		premBasic = Double.parseDouble(commonforall.getRound(PremiumBasic));	
//		premBasicRoundedOff2=Double.parseDouble(commonforall.getRoundOffLevel2(PremiumBasic));

        //Fair
//		nonGuaranMatBen_4Percent=commonforall.getStringWithout_E(Math.round(smartfuturechoicesBusinessLogic.getNonGuaranteedMatBenefit("4%")));
//		nonGuaranMatBen_8Percent= commonforall.getStringWithout_E(Math.round(smartfuturechoicesBusinessLogic.getNonGuaranteedMatBenefit("8%")));

        premiumSingleInstBasic = commonforall.getRoundUp(commonforall.getStringWithout_E((Double.parseDouble(PremiumBasic))));

        double basicServiceTax = 0;
        double SBCServiceTax = 0;
        double KKCServiceTax = 0;
        double kerlaServiceTax = 0;


        if (state) {
            basicServiceTax = smartfuturechoicesBusinessLogic.getServiceTax((premBasicRoundUp), smartfuturechoicesBean.isKerlaDisc(), "basic");
        } else {
            basicServiceTax = smartfuturechoicesBusinessLogic.getServiceTax(premBasicRoundUp, smartfuturechoicesBean.isKerlaDisc(), "basic");
        }

        serviceTax = commonforall.getStringWithout_E(basicServiceTax + SBCServiceTax + KKCServiceTax + kerlaServiceTax);

        premInstBasicFirstYear = commonforall.getRoundUp(commonforall.getStringWithout_E(Double.parseDouble(PremiumBasic) + Double.parseDouble(serviceTax)));
        //	System.out.println("serviceTax "+serviceTax);
        //	System.out.println("premInstBasicFirstYear "+premInstBasicFirstYear);

        //	premInstBasicSecondYear
        double basicServiceTaxSecondYear = 0;
        double SBCServiceTaxSecondYear = 0;
        double KKCServiceTaxSecondYear = 0;
        double kerlaServiceTaxSecondYear = 0;

        if (state) {
            basicServiceTaxSecondYear = smartfuturechoicesBusinessLogic.getServiceTaxSecondYear(premBasicRoundUp, smartfuturechoicesBean.isKerlaDisc(), "basic");
//			 kerlaServiceTaxSecondYear = smartfuturechoicesBusinessLogic.getServiceTaxSecondYear(premBasicRoundUp,shubhNiveshBean.isJkResident(),shubhNiveshBean.isKerlaDisc(),"KERALA");
//			 KeralaCessServiceTaxSecondYear =smartfuturechoicesBusinessLogic.getServiceTaxSecondYear(premBasicRoundUp,shubhNiveshBean.isJkResident(),shubhNiveshBean.isKerlaDisc(),"KKC");
        } else {
            basicServiceTaxSecondYear = smartfuturechoicesBusinessLogic.getServiceTaxSecondYear(premBasicRoundUp, smartfuturechoicesBean.isKerlaDisc(), "basic");
//				 SBCServiceTaxSecondYear = smartfuturechoicesBusinessLogic.getServiceTaxSecondYear(premBasicRoundUp,shubhNiveshBean.isJkResident(),shubhNiveshBean.isKerlaDisc(),"SBC");
//				 KKCServiceTaxSecondYear = smartfuturechoicesBusinessLogic.getServiceTaxSecondYear(premBasicRoundUp,shubhNiveshBean.isJkResident(),shubhNiveshBean.isKerlaDisc(),"KKC");
        }
        serviceTaxSecondYear = commonforall.getStringWithout_E(basicServiceTaxSecondYear + SBCServiceTaxSecondYear + KKCServiceTaxSecondYear + kerlaServiceTaxSecondYear);
        //	System.out.println("serviceTaxSecondYear "+serviceTaxSecondYear);

        premInstBasicSecondYear = commonforall.getRoundUp(commonforall.getStringWithout_E(Double.parseDouble(PremiumBasic) + Double.parseDouble(serviceTaxSecondYear)));

        //	System.out.println("premInstBasicSecondYear "+premInstBasicSecondYear);

        premiumSingleInstBasicWithST = commonforall.getStringWithout_E(Double.parseDouble(premInstBasicFirstYear));
        //	System.out.println("premiumSingleInstBasicWithST " + premiumSingleInstBasicWithST);
        premiumSingleInstBasicWithSTSecondYear = commonforall.getStringWithout_E(Double.parseDouble(premInstBasicSecondYear));

        String valPremiumError = valInstPremium(commonforall.getRoundUp(PremiumBasic), smartfuturechoicesBean.getPremiumFrequency());

        if (proposer_isSmoker.equalsIgnoreCase("Smoker")) {
            isSmoker = "Smoker";
        } else if (proposer_isSmoker.equalsIgnoreCase("Non-Smoker")) {
            isSmoker = "Non-Smoker";
        }
        //String valRiderPremiumError = valRiderPremium(premBasic);

    /*    MinesOccuInterest = ""
                + smartfuturechoicesBusinessLogic.getMinesOccuInterest(smartfuturechoicesBean
                .getSumAssured_Basic());*/

        if (rb_smart_bachat_backdating_yes.isChecked()) {
            // "16-jan-2014")));
            BackDateinterest = commonforall
                    .getRoundUp(commonforall.getStringWithout_E(smartfuturechoicesBusinessLogic.getBackDateInterest(
                            Double.parseDouble(premiumSingleInstBasicWithST),
                            btn_smart_bachat_backdatingdate.getText()
                                    .toString())));

            BackDateinterestwithGST = commonforall.getRoundUp(commonforall.getStringWithout_E(Double.parseDouble(BackDateinterest) + (Double.parseDouble(BackDateinterest) * prop.GSTforbackdate)));
        } else {
            BackDateinterestwithGST = "0";
            BackDateinterest = "0";
        }

        premiumSingleInstBasicWithST = commonforall
                .getStringWithout_E(Double
                        .parseDouble(premiumSingleInstBasicWithST)
                        + Double.parseDouble(BackDateinterestwithGST));

        BI = getOutput("Illustration", smartfuturechoicesBean);

        retVal.append("<?xml version='1.0' encoding='utf-8' ?><smartFutureChoices>");
        if (valPremiumError.equals("")) {
            retVal.append("<errCode>0</errCode>");

            retVal.append("<staffDiscCode>").append(smartfuturechoicesBusinessLogic.getStaffDiscCode()).append("</staffDiscCode>");
            retVal.append("<smokerOrNot>").append(isSmoker).append("</smokerOrNot>");

            retVal.append("<SumAssuredBasic>").append(commonforall.getRound(commonforall.getStringWithout_E(Sumassured_Basic))).append("</SumAssuredBasic>");

            retVal.append("<basicPrem>").append(commonforall.getRound(commonforall.getStringWithout_E(premBasicRoundUp))).append("</basicPrem>");

            retVal.append("<basicPremNotRounded>").append(commonforall.getRound(commonforall.getStringWithout_E(premBasicRoundedOff2))).append("</basicPremNotRounded>");

            /*
             * Installment Premium without Service Tax is Rs.
             */
            retVal.append("<installmntPrem>").append(premiumSingleInstBasic).append("</installmntPrem>");

            /*
             * Service Tax is Rs.
             */

            retVal.append("<servTax>").append(serviceTax).append("</servTax>");
            retVal.append("<servcTax>").append(serviceTax).append("</servcTax>");//Parivartan

            /*
             * Installment Premium with Service Tax is Rs.
             */

            retVal.append("<installmntPremWithSerTx>").append(premiumSingleInstBasicWithST).append("</installmntPremWithSerTx>");

            /*
             * Guaranteed Maturity Benefit is Rs.
             */

            retVal.append("<servTaxSeondYear>").append(commonforall.getStringWithout_E(Double.parseDouble(serviceTaxSecondYear))).append("</servTaxSeondYear>");
            retVal.append("<installmntPremWithSerTxSecondYear>").append(premiumSingleInstBasicWithSTSecondYear).append("</installmntPremWithSerTxSecondYear>");

            retVal.append("<MinesOccuInterest>" + MinesOccuInterest + "</MinesOccuInterest>");

            retVal.append("<backdateInt>").append(BackDateinterestwithGST).append("</backdateInt>");
            retVal.append("<staffStatus>").append(staffStatus).append("</staffStatus>");
            retVal.append("<staffRebate>").append(staffRebate).append("</staffRebate>");
            retVal.append(BI);

        }
        //Min premium or min rider error
        else {
            if (!valPremiumError.equals("")) {
                retVal.append("<errCode>1</errCode>");
                retVal.append("<minPremError>").append(valPremiumError).append("</minPremError>");
            }

        }
        retVal.append("</smartFutureChoices>");

    }

    private void Alert(String msg) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                BI_SmartFutureChoicesActivity.this);
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    /********************** Calculation Starts Here ***********************************************/

    public String getOutput(String string, SmartFutureChoicesBean smartfuturechoicesBean) {
        bussIll = new StringBuilder();
        SmartFutureChoicesBusinessLogic smartfuturechoicesBusinessLogic = new SmartFutureChoicesBusinessLogic(smartfuturechoicesBean);
        double basePremWithTx = 0, basePremWithTx_1 = 0, prem = 0;


        double sumannualizedPrem = 0, firstyearofannulized = 0, CashBonus4percentfy = 0, CashBonus8percentfy = 0, sumsurvivalBenefit = 0, sumofdefferedcash = 0, sumofdefferedcash8 = 0, sumcashbonus4fordf = 0, sumcashbonus4 = 0, sumcashbonus8 = 0;
        try {

            for (int i = 1; i <= smartfuturechoicesBean.getPolicyterm(); i++) {


                bussIll.append("<policyYr").append(i).append(">").append(i).append("</policyYr").append(i).append(">");

                basePremWithTx_1 = (basePremWithTx);

                if (i == 1) {
                    prem = (basePremWithTx_1);
                }

                String AnnulizedPrem = commonforall.getRound(commonforall.getStringWithout_E(smartfuturechoicesBusinessLogic.getAnnulizedPremium(i, smartfuturechoicesBean.getPremiumpayingterm(), smartfuturechoicesBean.getSumAssured())));
                //String AnnulizedPrem=commonforall.getRound(commonforall.getStringWithout_E(smartfuturechoicesBusinessLogic.getAnnulizedPremium(i,smartfuturechoicesBean.getPremiumpayingterm(),Double.parseDouble(PremiumBasic1));
                sumannualizedPrem = sumannualizedPrem + Double.parseDouble(AnnulizedPrem);
                if (i == 1) {
                    firstyearofannulized = Double.parseDouble(AnnulizedPrem);
                }
                AnnualizedPrem = commonforall.getRoundUp(commonforall.getStringWithout_E((Double.parseDouble(AnnulizedPrem))));

                //String Flexichoiceyear=commonforall.getRound(commonforall.getStringWithout_E(smartfuturechoicesBusinessLogic.getFlexichoiceyear(smartfuturechoicesBean.getPolicyterm(),smartfuturechoicesBean.getPremiumpayingterm(),"10%")));
                //System.out.println("Flexichoiceyear"+Flexichoiceyear);

                String SurvivalBen = commonforall.getRound(commonforall.getStringWithout_E(smartfuturechoicesBusinessLogic.getSurvivalBenefit(i, smartfuturechoicesBean.getPolicyterm(), smartfuturechoicesBean.getPremiumpayingterm(), Sumassured_Basic, "10%")));
                SurvivalBenefit = commonforall.getRoundUp(commonforall.getStringWithout_E((Double.parseDouble(SurvivalBen))));

                //if(i==i)
                //sumsurvivalBenefit=sumsurvivalBenefit-i;
                //System.out.println("sumsurvivalBenefit "+sumsurvivalBenefit);


                String SurrenderBen = commonforall.getRound(commonforall.getStringWithout_E(smartfuturechoicesBusinessLogic.getSurrenderBenefit(i, smartfuturechoicesBean.getPolicyterm(), sumannualizedPrem, sumsurvivalBenefit)));
                SurrebderBenefit = commonforall.getRoundUp(commonforall.getStringWithout_E((Double.parseDouble(SurrenderBen))));

                {
                    sumsurvivalBenefit = sumsurvivalBenefit + Double.parseDouble(SurvivalBenefit);
                }

                String GuaranteedBen = commonforall.getRound(commonforall.getStringWithout_E(smartfuturechoicesBusinessLogic.getGauranteedDeathBenefit(i, smartfuturechoicesBean.getPolicyterm(), firstyearofannulized, sumannualizedPrem)));
                GuaranteedBenefit = commonforall.getRound(commonforall.getStringWithout_E((Double.parseDouble(GuaranteedBen))));

                String MaturityBen = commonforall.getRound(commonforall.getStringWithout_E(smartfuturechoicesBusinessLogic.getMaturityBenefit(i, smartfuturechoicesBean.getAge(), smartfuturechoicesBean.getPolicyterm(), smartfuturechoicesBean.getPremiumpayingterm(), Sumassured_Basic)));
                GuaranteedMaturityBen = commonforall.getRound(commonforall.getStringWithout_E((Double.parseDouble(MaturityBen))));

                String cashbonus4 = (commonforall.getStringWithout_E(smartfuturechoicesBusinessLogic.getCashBonus4per(i, Sumassured_Basic, "4%")));
                sumcashbonus4 = (sumcashbonus4 + Double.parseDouble(cashbonus4));

                //System.out.println("cashbonus4"+cashbonus4);

                //System.out.println("ssssssssumcashbonus4   "+sumcashbonus4);
                if (i >= 3) {
                    sumcashbonus4fordf = sumcashbonus4;
                }

                CashBonus4percent = commonforall.getRound(commonforall.getStringWithout_E((Double.parseDouble(cashbonus4))));
                if (i == 1) {
                    CashBonus4percentfy = Double.parseDouble(CashBonus4percent);
                }

                double dfcashfordf = 0;

                String defferedcash4per = (commonforall.getStringWithout_E(smartfuturechoicesBusinessLogic.getAccumulatedDefferedcash4per(i, sumcashbonus4, CashBonus4percentfy, sumofdefferedcash, dfcashfordf)));

                //	String defferedcash4per=(commonforall.getStringWithout_E(smartfuturechoicesBusinessLogic.getAccumulatedDefferedcash(i, Double.parseDouble(CashBonus4percent), CashBonus4percentfy,sumofdefferedcash,dfcashfordf)));
                sumofdefferedcash = (sumofdefferedcash + Double.parseDouble(defferedcash4per));
                Defferedcash4per = commonforall.getRound(commonforall.getStringWithout_E((Double.parseDouble(defferedcash4per))));

                if (i >= 2) {
                    dfcashfordf = Double.parseDouble(defferedcash4per);
                }

                String cashbonus8 = (commonforall.getStringWithout_E(smartfuturechoicesBusinessLogic.getCashBonus8per(i, Sumassured_Basic, "8%")));
                sumcashbonus8 = sumcashbonus8 + Double.parseDouble(cashbonus8);
                CashBonus8percent = commonforall.getRound(commonforall.getStringWithout_E((Double.parseDouble(cashbonus8))));

                if (i == 1) {
                    CashBonus8percentfy = Double.parseDouble(CashBonus8percent);
                }
                double dfcashfordf8 = 0;

                String defferedcash8per = commonforall.getStringWithout_E(smartfuturechoicesBusinessLogic.getAccumulatedDefferedcash8per(i, sumcashbonus8, CashBonus8percentfy, sumofdefferedcash8, dfcashfordf8));

                //	String defferedcash4per=(commonforall.getStringWithout_E(smartfuturechoicesBusinessLogic.getAccumulatedDefferedcash(i, Double.parseDouble(CashBonus4percent), CashBonus4percentfy,sumofdefferedcash,dfcashfordf)));
                sumofdefferedcash8 = (sumofdefferedcash8 + Double.parseDouble(defferedcash8per));
                Defferedcash8per = commonforall.getRound(commonforall.getStringWithout_E((Double.parseDouble(defferedcash8per))));

                if (i >= 2) {
                    dfcashfordf8 = Double.parseDouble(defferedcash8per);
                }

                String getSurrenderBen4Per = commonforall.getRound(commonforall.getStringWithout_E(smartfuturechoicesBusinessLogic.getSurrenderBen4Per(i, smartfuturechoicesBean.getAge(), Double.parseDouble(cashbonus4), sumannualizedPrem, sumsurvivalBenefit, Sumassured_Basic)));
                SurrenderBen4Per = commonforall.getRound(commonforall.getStringWithout_E((Double.parseDouble(getSurrenderBen4Per))));

                String getSurrenderBen8Per = commonforall.getRound(commonforall.getStringWithout_E(smartfuturechoicesBusinessLogic.getSurrenderBen8Per(i, smartfuturechoicesBean.getAge(), Double.parseDouble(cashbonus8), sumannualizedPrem, sumsurvivalBenefit, Sumassured_Basic)));
                SurrenderBen8Per = commonforall.getRound(commonforall.getStringWithout_E((Double.parseDouble(getSurrenderBen8Per))));

                String totalmaturity4 = commonforall.getRound(commonforall.getStringWithout_E(smartfuturechoicesBusinessLogic.getTotalMaturity4per(i, smartfuturechoicesBean.getAge(), Sumassured_Basic, sumcashbonus4, Double.parseDouble(cashbonus4), Double.parseDouble(GuaranteedMaturityBen), Double.parseDouble(Defferedcash4per))));
                totalmaturity4per = commonforall.getRound(commonforall.getStringWithout_E((Double.parseDouble(totalmaturity4))));

                String totalmaturity8 = commonforall.getRound(commonforall.getStringWithout_E(smartfuturechoicesBusinessLogic.getTotalMaturity8per(i, smartfuturechoicesBean.getAge(), Sumassured_Basic, sumcashbonus8, Double.parseDouble(cashbonus8), Double.parseDouble(GuaranteedMaturityBen), Double.parseDouble(Defferedcash8per))));
                totalmaturity8per = commonforall.getRound(commonforall.getStringWithout_E((Double.parseDouble(totalmaturity8))));

                totalDeathBen4per = commonforall.getRound(commonforall.getStringWithout_E(smartfuturechoicesBusinessLogic.getTotalDeathBenefit4per(i, sumcashbonus4, Double.parseDouble(cashbonus4), Double.parseDouble(GuaranteedBenefit), Double.parseDouble(Defferedcash4per), sumannualizedPrem)));

                totalDeathBen8per = commonforall.getRound(commonforall.getStringWithout_E(smartfuturechoicesBusinessLogic.getTotalDeathBenefit8per(i, sumcashbonus8, Double.parseDouble(cashbonus8), Double.parseDouble(GuaranteedBenefit), Double.parseDouble(Defferedcash8per), sumannualizedPrem)));


                //bussIll.append("<TotBasePremWithTx"+ i +">" + commonforall.getStringWithout_E(basePremWithTx_1) + "</TotBasePremWithTx"+ i +">");
                bussIll.append("<AnnualizedPremium").append(i).append(">").append(AnnualizedPrem).append("</AnnualizedPremium").append(i).append(">");
                bussIll.append("<GuaranteedAdd").append(i).append(">").append(0).append("</GuaranteedAdd").append(i).append(">");
                bussIll.append("<GuranteedSurrenderBenefit").append(i).append(">").append(SurrebderBenefit).append("</GuranteedSurrenderBenefit").append(i).append(">");
                bussIll.append("<GuranteedSurvivalBenefit").append(i).append(">").append(SurvivalBenefit).append("</GuranteedSurvivalBenefit").append(i).append(">");
                bussIll.append("<GuaranteedDeathBenefit").append(i).append(">").append(GuaranteedBenefit).append("</GuaranteedDeathBenefit").append(i).append(">");
                bussIll.append("<GuaranteedMaturityBen").append(i).append(">").append(GuaranteedMaturityBen).append("</GuaranteedMaturityBen").append(i).append(">");
                bussIll.append("<CashBonus4percent").append(i).append(">").append(CashBonus4percent).append("</CashBonus4percent").append(i).append(">");
                bussIll.append("<CashBonus8percent").append(i).append(">").append(CashBonus8percent).append("</CashBonus8percent").append(i).append(">");
                bussIll.append("<Defferedcash4per").append(i).append(">").append(Defferedcash4per).append("</Defferedcash4per").append(i).append(">");
                bussIll.append("<Defferedcash8per").append(i).append(">").append(Defferedcash8per).append("</Defferedcash8per").append(i).append(">");

                bussIll.append("<SurrenderBen4Per").append(i).append(">").append(SurrenderBen4Per).append("</SurrenderBen4Per").append(i).append(">");
                bussIll.append("<SurrenderBen8Per").append(i).append(">").append(SurrenderBen8Per).append("</SurrenderBen8Per").append(i).append(">");

                bussIll.append("<Revisionary4per").append(i).append(">").append(0).append("</Revisionary4per").append(i).append(">");
                bussIll.append("<Revisionary8per").append(i).append(">").append(0).append("</Revisionary8per").append(i).append(">");

                bussIll.append("<totalmaturity4per").append(i).append(">").append(totalmaturity4per).append("</totalmaturity4per").append(i).append(">");
                bussIll.append("<totalmaturity8per").append(i).append(">").append(totalmaturity8per).append("</totalmaturity8per").append(i).append(">");
                bussIll.append("<totalDeathBen4per").append(i).append(">").append(totalDeathBen4per).append("</totalDeathBen4per").append(i).append(">");
                bussIll.append("<totalDeathBen8per").append(i).append(">").append(totalDeathBen8per).append("</totalDeathBen8per").append(i).append(">");


            }

        } catch (Exception e) {
            e.printStackTrace();

        }

        return bussIll.toString();
    }

    public void getInput(SmartFutureChoicesBean smartbachatbean) {

        inputVal = new StringBuilder();
        // From GUI Input
        boolean staffDisc = smartbachatbean.getIsForStaffOrNot();
        boolean isJKResident = smartbachatbean.getIsJammuResident();
        int policyTerm = smartbachatbean.getPolicyterm();
        String premFreq = smartbachatbean.getPremiumFrequency();
        double sumAssured = smartbachatbean.getSumAssured();
        String plantype = smartbachatbean.getPlantype();
        int premPayingterm = smartbachatbean.getPremiumpayingterm();
        inputVal.append("<?xml version='1.0' encoding='utf-8' ?><smartFutureChoices>");
        inputVal.append("<LifeAssured_title>").append(lifeAssured_Title).append("</LifeAssured_title>");
        inputVal.append("<LifeAssured_firstName>").append(lifeAssured_First_Name).append("</LifeAssured_firstName>");
        inputVal.append("<LifeAssured_middleName>").append(lifeAssured_Middle_Name).append("</LifeAssured_middleName>");
        inputVal.append("<LifeAssured_lastName>").append(lifeAssured_Last_Name).append("</LifeAssured_lastName>");
        inputVal.append("<LifeAssured_DOB>").append(lifeAssured_date_of_birth).append("</LifeAssured_DOB>");
        inputVal.append("<LifeAssured_age>").append(spinner_age.getSelectedItem().toString()).append("</LifeAssured_age>");

        inputVal.append("<gender>").append(gender).append("</gender>");
        inputVal.append("<isStaff>").append(staffDisc).append("</isStaff>");
        inputVal.append("<str_kerla_discount>").append(str_kerla_discount).append("</str_kerla_discount>");
        inputVal.append("<isJKResident>").append(isJKResident).append("</isJKResident>");
        inputVal.append("<age>").append(spinner_age.getSelectedItem().toString()).append("</age>");

        inputVal.append("<ProposerAge>").append(ProposerAge).append("</ProposerAge>");

        inputVal.append("<proposer_title>").append(proposer_Title).append("</proposer_title>");
        inputVal.append("<proposer_firstName>").append(proposer_First_Name).append("</proposer_firstName>");
        inputVal.append("<proposer_middleName>").append(proposer_Middle_Name).append("</proposer_middleName>");
        inputVal.append("<proposer_lastName>").append(proposer_Last_Name).append("</proposer_lastName>");
        inputVal.append("<proposer_DOB>").append(proposer_date_of_birth).append("</proposer_DOB>");
        inputVal.append("<proposer_age>").append("").append("</proposer_age>");
        inputVal.append("<proposer_gender>").append("").append("</proposer_gender>");

        inputVal.append("<product_name>").append(planName).append("</product_name>");
        /* parivartan changes */
        inputVal.append("<product_Code>").append(product_Code).append("</product_Code>");
        inputVal.append("<product_UIN>").append(product_UIN).append("</product_UIN>");
        inputVal.append("<product_cateogory>").append(product_cateogory).append("</product_cateogory>");
        inputVal.append("<product_type>").append(product_type).append("</product_type>");
        inputVal.append("<gender_proposer>").append(gender_proposer).append("</gender_proposer>");

        inputVal.append("<policyTerm>").append(policyTerm).append("</policyTerm>");
        inputVal.append("<premPayTerm>").append(premPayingterm).append("</premPayTerm>");
        inputVal.append("<premFreq>").append(premFreq).append("</premFreq>");
        inputVal.append("<sumAssured>").append(sumAssured).append("</sumAssured>");
        inputVal.append("<plan>").append(plantype).append("</plan>");
        inputVal.append("<bonusOption>").append(smartbachatbean.getBonusOption()).append("</bonusOption>");
        inputVal.append("<proposer_Is_Same_As_Life_Assured>").append(proposer_Is_Same_As_Life_Assured).append("</proposer_Is_Same_As_Life_Assured>");


        inputVal.append("<bonus_type>").append(str_bonus_type).append("</bonus_type>");

        inputVal.append("<Product_Cat>").append(product_cateogory).append("</Product_Cat>");
        inputVal.append("<MAT_VALUE_PROD_CHANGE>").append(STR_MAT_VALUE_PROD_CHANGE).append("</MAT_VALUE_PROD_CHANGE>");

        inputVal.append("<MAT_VALUE_PROD_QUT_NO>").append(STR_MAT_VALUE_PROD_QUT_NO).append("</MAT_VALUE_PROD_QUT_NO>");
        inputVal.append("<BIVERSION>" + "1" + "</BIVERSION>");
//Added Tushar Kadam Kerla Applicable Tax 7-Jun-2019
        String str_kerla_discount = "N";
        if (cb_kerladisc.isChecked()) {
            str_kerla_discount = "Y";
        }

        inputVal.append("<KFC>").append(str_kerla_discount).append("</KFC>");
        inputVal.append("</smartFutureChoices>");
    }

    public boolean valLifeAssuredProposerDetail() {
        if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {
            if (lifeAssured_Title.equals("")
                    || lifeAssured_First_Name.equals("")
                    || lifeAssured_Last_Name.equals("")) {

                commonMethods.showMessageDialog(context, "Please Fill Name Detail For LifeAssured");

                if (lifeAssured_Title.equals("")) {
                    // apply focusable method
                    commonMethods.setFocusable(spnr_bi_smart_bachat_life_assured_title);
                    spnr_bi_smart_bachat_life_assured_title
                            .requestFocus();
                } else if (lifeAssured_First_Name.equals("")) {
                    commonMethods.setFocusable(edt_bi_smart_bachat_life_assured_first_name);
                    edt_bi_smart_bachat_life_assured_first_name
                            .requestFocus();
                } else {
                    commonMethods.setFocusable(edt_bi_smart_bachat_life_assured_last_name);
                    edt_bi_smart_bachat_life_assured_last_name
                            .requestFocus();
                }

                return false;
            } else if (gender.equalsIgnoreCase("")) {

                commonMethods.showMessageDialog(context, "Please select Gender");
                return false;

            } else if (lifeAssured_Title.equalsIgnoreCase("Mr.")
                    && gender.equalsIgnoreCase("Female")) {

                commonMethods.showMessageDialog(context, "Life Assured Title and Gender is not Valid");
                commonMethods.setFocusable(spnr_bi_smart_bachat_life_assured_title);
                spnr_bi_smart_bachat_life_assured_title
                        .requestFocus();

                return false;

            } else if (lifeAssured_Title.equalsIgnoreCase("Ms.")
                    && gender.equalsIgnoreCase("Male")) {

                commonMethods.showMessageDialog(context, "Life Assured Title and Gender is not Valid");
                commonMethods.setFocusable(spnr_bi_smart_bachat_life_assured_title);
                spnr_bi_smart_bachat_life_assured_title
                        .requestFocus();

                return false;

            } else if (lifeAssured_Title.equalsIgnoreCase("Mrs.")
                    && gender.equalsIgnoreCase("Male")) {

                commonMethods.showMessageDialog(context, "Life Assured Title and Gender is not Valid");
                commonMethods.setFocusable(spnr_bi_smart_bachat_life_assured_title);
                spnr_bi_smart_bachat_life_assured_title
                        .requestFocus();

                return false;
            } else {
                return true;
            }

        } else if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("n")) {

            if (lifeAssured_Title.equals("")
                    || lifeAssured_First_Name.equals("")
                    || lifeAssured_Last_Name.equals("")) {

                commonMethods.showMessageDialog(context, "Please Fill Name Detail For LifeAssured");
                if (lifeAssured_Title.equals("")) {
                    // apply focusable method
                    commonMethods.setFocusable(spnr_bi_smart_bachat_life_assured_title);
                    spnr_bi_smart_bachat_life_assured_title
                            .requestFocus();
                } else if (lifeAssured_First_Name.equals("")) {

                    edt_bi_smart_bachat_life_assured_first_name
                            .requestFocus();
                } else {
                    edt_bi_smart_bachat_life_assured_last_name
                            .requestFocus();
                }

                return false;
            } else if (lifeAssured_Title.equalsIgnoreCase("Mr.")
                    && gender.equalsIgnoreCase("Female")) {

                commonMethods.showMessageDialog(context, "Life Assured Title and Gender is not Valid");
                commonMethods.setFocusable(spnr_bi_smart_bachat_life_assured_title);
                spnr_bi_smart_bachat_life_assured_title
                        .requestFocus();

                return false;

            } else if (lifeAssured_Title.equalsIgnoreCase("Ms.")
                    && gender.equalsIgnoreCase("Male")) {

                commonMethods.showMessageDialog(context, "Life Assured Title and Gender is not Valid");
                commonMethods.setFocusable(spnr_bi_smart_bachat_life_assured_title);
                spnr_bi_smart_bachat_life_assured_title
                        .requestFocus();

                return false;

            } else if (lifeAssured_Title.equalsIgnoreCase("Mrs.")
                    && gender.equalsIgnoreCase("Male")) {

                commonMethods.showMessageDialog(context, "Life Assured Title and Gender is not Valid");
                commonMethods.setFocusable(spnr_bi_smart_bachat_life_assured_title);
                spnr_bi_smart_bachat_life_assured_title
                        .requestFocus();

                return false;
            } else if (proposer_Title.equals("")
                    || proposer_First_Name.equals("")
                    || proposer_Last_Name.equals("")) {

                commonMethods.showMessageDialog(context, "Please Fill Name Detail For Proposer");
                if (proposer_Title.equals("")) {
                    // apply focusable method
                    commonMethods.setFocusable(spnr_bi_smart_future_choices_proposer_title);
                    spnr_bi_smart_future_choices_proposer_title
                            .requestFocus();
                } else if (proposer_First_Name.equals("")) {
                    edt_bi_smart_future_choices_proposer_first_name
                            .requestFocus();
                } else {
                    edt_bi_smart_future_choices_proposer_last_name
                            .requestFocus();
                }

                return false;

            } else if (gender_proposer.equalsIgnoreCase("")) {

                commonMethods.showMessageDialog(context, "Please select Proposer Gender");

                return false;

            } else if (proposer_Title.equalsIgnoreCase("Mr.")
                    && gender_proposer.equalsIgnoreCase("Female")) {

                commonMethods.showMessageDialog(context, "Proposar Title and Gender is not Valid");
                commonMethods.setFocusable(spnr_bi_smart_future_choices_proposer_title);
                spnr_bi_smart_future_choices_proposer_title
                        .requestFocus();

                return false;

            } else if (proposer_Title.equalsIgnoreCase("Ms.")
                    && gender_proposer.equalsIgnoreCase("Male")) {

                commonMethods.showMessageDialog(context, "Proposar Title and Gender is not Valid");
                commonMethods.setFocusable(spnr_bi_smart_future_choices_proposer_title);
                spnr_bi_smart_future_choices_proposer_title
                        .requestFocus();

                return false;

            } else if (proposer_Title.equalsIgnoreCase("Mrs.")
                    && gender_proposer.equalsIgnoreCase("Male")) {

                commonMethods.showMessageDialog(context, "Proposar Title and Gender is not Valid");
                commonMethods.setFocusable(spnr_bi_smart_future_choices_proposer_title);
                spnr_bi_smart_future_choices_proposer_title
                        .requestFocus();

                return false;

            } else {
                return true;
            }
        } else {
            return true;
        }

    }

    public boolean valDob() {

        if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {

            if (lifeAssured_date_of_birth.equals("")
                    || lifeAssured_date_of_birth
                    .equalsIgnoreCase("select Date")) {
                commonMethods.showMessageDialog(context, "Please Select Valid Date Of Birth For LifeAssured");
                commonMethods.setFocusable(btn_bi_smart_future_choices_life_assured_date);
                btn_bi_smart_future_choices_life_assured_date
                        .requestFocus();

                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public boolean sumassuredval() {
        StringBuilder error = new StringBuilder();
        if (et_sumassured.getText().toString().equals("")) {
            error.append("Please enter Annulised Premium in Rs ");
        } else if (Double.parseDouble(et_sumassured.getText().toString()) % 1000 != 0) {
            error.append("Premium should be multiple of 1,000");
        } else if (Integer.parseInt(et_sumassured.getText().toString()) < 100000) {
            error.append("Minimum Premium for Yearly Frequency mode is 100000");
        }


        if (!error.toString().equals("")) {

            commonMethods.showMessageDialog(context, error.toString());
            return false;
        } else
            return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


        int SIGNATURE_ACTIVITY = 1;
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
                        // ProductHomePageActivity.customer_Signature =
                        // CaptureSignature.scaled;
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

    /* Basic Details Method */

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
                .toString().length() != MobileLenth) {
            commonMethods.dialogWarning(context, "Please Fill " + MobileLenth + " Digit Mobile No", true);
            edt_proposerdetail_basicdetail_contact_no.requestFocus();
            return false;
        } /*else if (emailId.equals("")) {
            commonMethods.dialogWarning(context,"Please Fill Email Id", true);
            edt_proposerdetail_basicdetail_Email_id.requestFocus();
            return false;

        } else if (ConfirmEmailId.equals("")) {

            commonMethods.dialogWarning(context,"Please Fill Confirm Email Id", true);
            edt_proposerdetail_basicdetail_ConfirmEmail_id.requestFocus();
            return false;
        } else if (!ConfirmEmailId.equals(emailId)) {
            commonMethods.dialogWarning(context,"Email Id Does Not Match", true);
            return false;
        } */ else if (!emailId.equals("")) {

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
        } else {
            return true;
        }

    }

    public void mobile_validation(String number) {
        if ((number.length() != MobileLenth)) {
            edt_proposerdetail_basicdetail_contact_no
                    .setError("Please provide correct " + MobileLenth + "-digit mobile number");
        } else if ((number.length() == MobileLenth)) {
        }
    }

    public void email_id_validation(String email_id) {

        EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email_id);
        if (!(matcher.matches())) {
            edt_proposerdetail_basicdetail_Email_id
                    .setError("Please provide the correct email address");
            validationFla1 = false;
        } else if (!cb_staffdisc.isChecked()
                && (email_id.contains("@sbi.co.in")
                || email_id.contains("@sbm.co.in")
                || email_id.contains("@sbbj.co.in")
                || email_id.contains("@sbhyd.co.in")
                || email_id.contains("@sbp.co.in")
                || email_id.contains("@sbt.co.in")
                || email_id.contains("@sbi-life.com") || email_id
                .contains("@sbilife.co.in"))) {
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

    private void initialiseDate() {
        Calendar calender = Calendar.getInstance();
        mYear = calender.get(Calendar.YEAR);
        mMonth = calender.get(Calendar.MONTH);
        mDay = calender.get(Calendar.DAY_OF_MONTH);

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

    public void Date() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formattedDate = df.format(c.getTime());
        // formattedDate have current date/time
        // Toast.makeText(this, formattedDate, Toast.LENGTH_SHORT).show();
        c.add(Calendar.DATE, 30);
        ExpiredDate = df.format(c.getTime());
    }

    /**
     * Used To Change date From dd-mm-yyyy to mm-dd-yyyy
     */
    public String getDate1(String OldDate) {
        String NewDate = "";
        try {
            DateFormat userDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            DateFormat dateFormatNeeded = new SimpleDateFormat("MM-dd-yyyy");
            Date date = userDateFormat.parse(OldDate);

            NewDate = dateFormatNeeded.format(date);

        } catch (Exception e) {
            // TODO: handle exception
            Log.e("Integrated Service", "Error in Getting Date");
        }

        return NewDate;
    }

    // FOr Date Dialog Box


    private void showDateDialog() {
        DatePickerDialog dialog = new DatePickerDialog(
                context,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener,
                mYear, mMonth, mDay);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private final DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // if (view.isShown()) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            updateDisplay(DIALOG_ID);
            // }
        }
    };

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
        } else {
            age = tYear - mYear - 1;
        }

        String final_age = age + " yrs";

        if (final_age.contains("-")) {
            commonMethods.dialogWarning(context, "Please fill Valid Date", true);
        } else {
            switch (id) {

                case 2:
                    btn_PolicyholderDate.setText(date);
                    break;
                case 3:
                    btn_MarketingOfficalDate.setText(date);
                    break;

                case 4:
                    ProposerAge = final_age;
                    final_age = Integer.toString(age);
                    if (final_age.contains("-")) {
                        commonMethods.showMessageDialog(context, "Please fill Valid Birth Date");
                    } else {
                        if (18 <= age && age <= 50) {

                            btn_bi_smart_future_choices_proposer_date.setText(date);
                            // edt_bi_smart_bachat_life_assured_age
                            // .setText(final_age);
                            proposer_date_of_birth = getDate1(date + "");

                            clearFocusable(btn_bi_smart_future_choices_proposer_date);
                            commonMethods.setFocusable(edt_proposerdetail_basicdetail_contact_no);
                            edt_proposerdetail_basicdetail_contact_no
                                    .requestFocus();

                        } else {
                            commonMethods.showMessageDialog(context, "Minimum age should be 18 years and Maximum age should be 50 years");
                            btn_bi_smart_future_choices_proposer_date
                                    .setText("Select Date");
                            // edt_bi_smart_bachat_life_assured_age
                            // .setText("");
                            proposer_date_of_birth = "";
                            clearFocusable(btn_bi_smart_future_choices_proposer_date);
                            commonMethods.setFocusable(btn_bi_smart_future_choices_proposer_date);
                            btn_bi_smart_future_choices_proposer_date.requestFocus();
                        }
                    }
                    break;

                case 5:

                    final_age = Integer.toString(age);
                    if (final_age.contains("-")) {
                        commonMethods.showMessageDialog(context, "Please fill Valid Birth Date");
                    } else {
                        if (18 <= age && age <= 50) {
                            lifeAssuredAge = final_age;
                            btn_bi_smart_future_choices_life_assured_date.setText(date);

                            String[] ageList = new String[33];
                            for (int i = 18; i <= 50; i++) {
                                ageList[i - 18] = i + "";
                            }
                            ArrayAdapter<String> ageAdapter = new ArrayAdapter<String>(
                                    getApplicationContext(), R.layout.spinner_item,
                                    ageList);
                            ageAdapter
                                    .setDropDownViewResource(R.layout.spinner_item1);
                            spinner_age.setAdapter(ageAdapter);
                            ageAdapter.notifyDataSetChanged();

                            spinner_age.setSelection(
                                    getIndex(spinner_age, final_age), false);

                            lifeAssured_date_of_birth = getDate1(date + "");

                            tr_smart_bachat_proposer_detail1
                                    .setVisibility(View.GONE);
                            tr_smart_bachat_proposer_detail12
                                    .setVisibility(View.GONE);
                            tr_smart_bachat_proposer_gender_detail3.setVisibility(View.GONE);
                            tr_smart_bachat_gender_stmt_proposer_detail31.setVisibility(View.GONE);
                            tr_smart_bachat_proposer_detail13
                                    .setVisibility(View.GONE);
                            tr_smart_bachat_proposer_detail2
                                    .setVisibility(View.GONE);
                            tr_smart_bachat_proposer_detail22
                                    .setVisibility(View.GONE);
                            proposer_Is_Same_As_Life_Assured = "y";

                            clearFocusable(btn_bi_smart_future_choices_life_assured_date);
                            commonMethods.setFocusable(edt_proposerdetail_basicdetail_contact_no);
                            edt_proposerdetail_basicdetail_contact_no
                                    .requestFocus();

                            btn_smart_bachat_backdatingdate.setText("Select Date");
                            proposer_Backdating_BackDate = "";

                        } else {
                            commonMethods.showMessageDialog(context, "Minimum Age should be "
                                    + 18
                                    + " yrs and Maximum Age should be  50 yrs");
                            btn_bi_smart_future_choices_life_assured_date
                                    .setText("Select Date");
                            lifeAssured_date_of_birth = "";

                            String[] ageList = new String[33];
                            for (int i = 18; i <= 50; i++) {
                                ageList[i - 18] = i + "";
                            }
                            ArrayAdapter<String> ageAdapter = new ArrayAdapter<String>(
                                    getApplicationContext(), R.layout.spinner_item,
                                    ageList);
                            ageAdapter
                                    .setDropDownViewResource(R.layout.spinner_item1);
                            spinner_age.setAdapter(ageAdapter);
                            ageAdapter.notifyDataSetChanged();

                            btn_smart_bachat_backdatingdate.setText("Select Date");
                            proposer_Backdating_BackDate = "";

                            clearFocusable(btn_bi_smart_future_choices_life_assured_date);
                            commonMethods.setFocusable(btn_bi_smart_future_choices_life_assured_date);
                            btn_bi_smart_future_choices_life_assured_date.requestFocus();
                        }

                    }
                    break;

                case 6:
                    if (age >= 0) {
                        proposer_Backdating_BackDate = date + "";

                       /* if(TrueBackdateLaunch())
                        {*/
                        btn_smart_bachat_backdatingdate
                                .setText(proposer_Backdating_BackDate);
                        clearFocusable(btn_smart_bachat_backdatingdate);

                        commonMethods.setFocusable(spinner_age);
                        spinner_age.requestFocus();
                        // }
                    } else {
                        commonMethods.dialogWarning(context, "Please Select Valid BackDating Date", true);
                        btn_smart_bachat_backdatingdate.setText("Select Date");
                        proposer_Backdating_BackDate = "";
                    }

                    break;

                default:
                    break;

            }

        }

        if (proposer_Backdating_WishToBackDate_Policy.equalsIgnoreCase("y")
                && !proposer_Backdating_BackDate.equals("")) {

            int Proposerage = calculateMyAge(mYear, Integer.parseInt(mont),
                    Integer.parseInt(day));
            String str_final_Age = Integer.toString(Proposerage);
            spinner_age.setSelection(getIndex(spinner_age, str_final_Age),
                    false);
            // valAge();

        }

    }

    private int calculateMyAge(int year, int month, int day) {
        Calendar nowCal = new GregorianCalendar(year, month, day);

        String[] ProposerDob = getDate(lifeAssured_date_of_birth).split("-");
        // int age = Integer.parseInt(ProposerDob[3]) -
        // birthCal.get(Calendar.YEAR);

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

    public void onClickProposerDob(View v) {
        initialiseDateParameter(proposer_date_of_birth, 35);
        DIALOG_ID = 4;
        showDateDialog();
    }

    public void onClickLADob(View v) {
        initialiseDateParameter(lifeAssured_date_of_birth, 35);
        DIALOG_ID = 5;
        showDateDialog();
    }

    public void onClickBackDating(View v) {
        String backdate = getDate1(proposer_Backdating_BackDate);
        initialiseDateParameter(backdate, 0);
        DIALOG_ID = 6;
        if (lifeAssured_date_of_birth != null
                && !lifeAssured_date_of_birth.equals("")) {
            showDateDialog();
        } else {
            commonMethods.dialogWarning(context, "Please select a LifeAssured DOB First", true);
        }
    }

    public void Dialog() {

        final Dialog d = new Dialog(this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.BLACK));

        d.setContentView(R.layout.layout_smart_future_choices_bi_grid);


        TextView tv_channel_intermediary = (TextView) d
                .findViewById(R.id.tv_channel_intermediary);
        tv_channel_intermediary.setText(userType);


        TextView tv_proposername = (TextView) d
                .findViewById(R.id.tv_proposername);
        TextView tv_proposal_number = (TextView) d
                .findViewById(R.id.tv_proposal_number);

        TextView tv_bi_smart_bachat_life_assured_name = (TextView) d
                .findViewById(R.id.tv_bi_smart_bachat_life_assured_name);
        TextView tv_bi_smart_bachat_life_assured_age = (TextView) d
                .findViewById(R.id.tv_bi_smart_bachat_life_assured_age);
        TextView tv_bi_smart_bachat_life_assured_gender = (TextView) d
                .findViewById(R.id.tv_bi_smart_bachat_life_assured_gender);
        TextView tv_bi_smart_bachat_life_assured_premium_frequency = (TextView) d
                .findViewById(R.id.tv_bi_smart_bachat_life_assured_premium_frequency);

        TextView tv_bi_smart_bachat_plan_proposed = (TextView) d
                .findViewById(R.id.tv_bi_smart_bachat_plan_proposed);

        TextView tv_bi_smart_bachat_bonus_option = (TextView) d
                .findViewById(R.id.tv_bi_smart_bachat_bonus_option);
        TextView tv_bi_smart_bachat_term = (TextView) d
                .findViewById(R.id.tv_bi_smart_bachat_term);
        TextView tv_bi_smart_bachat_premium_paying_term = (TextView) d
                .findViewById(R.id.tv_bi_smart_bachat_premium_paying_term);
        TextView tv_bi_smart_bachat_sum_assured = (TextView) d
                .findViewById(R.id.tv_bi_smart_bachat_sum_assured);
        TextView tv_premium_text_for_option_heading = (TextView) d
                .findViewById(R.id.tv_premium_text_for_option_heading);

        TextView tv_bi_smart_bachat_rate_of_applicable_taxes = (TextView) d.
                findViewById(R.id.tv_bi_smart_bachat_rate_of_applicable_taxes);


        TextView tv_bi_smart_bachat_yearly_premium_with_tax = (TextView) d
                .findViewById(R.id.tv_bi_smart_bachat_yearly_premium_with_tax);

        // Second year tables

        final TextView tv_premium_install_rider_type1 = (TextView) d
                .findViewById(R.id.tv_premium_install_rider_type1);

        final TextView tv_mandatory_bi_smart_bachat_yearly_premium_with_tax1 = (TextView) d
                .findViewById(R.id.tv_mandatory_bi_smart_bachat_yearly_premium_with_tax1);

        // First year policy
        TextView tv_bi_smart_bachat_basic_premium_first_year = (TextView) d
                .findViewById(R.id.tv_bi_smart_bachat_basic_premium_first_year);
        TextView tv_bi_smart_bachat_service_tax_first_year = (TextView) d
                .findViewById(R.id.tv_bi_smart_bachat_service_tax_first_year);
        TextView tv_bi_smart_bachat_yearly_premium_with_tax_first_year = (TextView) d
                .findViewById(R.id.tv_bi_smart_bachat_yearly_premium_with_tax_first_year);

        // Seconf year policy onwards
        TextView tv_bi_smart_bachat_basic_premium_second_year = (TextView) d
                .findViewById(R.id.tv_bi_smart_bachat_basic_premium_second_year);
        TextView tv_bi_smart_bachat_service_tax_second_year = (TextView) d
                .findViewById(R.id.tv_bi_smart_bachat_service_tax_second_year);
        TextView tv_bi_smart_bachat_yearly_premium_with_tax_second_year = (TextView) d
                .findViewById(R.id.tv_bi_smart_bachat_yearly_premium_with_tax_second_year);

        TableRow tr_second_year = (TableRow) d
                .findViewById(R.id.tr_second_year);

        final TextView tvCbStatementFirst = d
                .findViewById(R.id.tvCbStatementFirst);

        GridView gv_userinfo = (GridView) d.findViewById(R.id.gv_userinfo);

        final EditText edt_Policyholderplace = (EditText) d
                .findViewById(R.id.edt_Policyholderplace);

        TextView tv_bi_is_Staff = (TextView) d
                .findViewById(R.id.tv_bi_is_Staff);

        TextView tv_bi_is_jk = (TextView) d.findViewById(R.id.tv_bi_is_jk);

        TextView tv_bi_smart_bachat_basic_service_tax_first_year = (TextView) d
                .findViewById(R.id.tv_bi_smart_bachat_basic_service_tax_first_year);
        TextView tv_bi_smart_bachat_swachh_bharat_cess_first_year = (TextView) d
                .findViewById(R.id.tv_bi_smart_bachat_swachh_bharat_cess_first_year);
        TextView tv_bi_smart_bachat_krishi_kalyan_cess_first_year = (TextView) d
                .findViewById(R.id.tv_bi_smart_bachat_krishi_kalyan_cess_first_year);

        TextView tv_bi_smart_bachat_basic_service_tax_second_year = (TextView) d
                .findViewById(R.id.tv_bi_smart_bachat_basic_service_tax_second_year);
        TextView tv_bi_smart_bachat_swachh_bharat_cess_second_year = (TextView) d
                .findViewById(R.id.tv_bi_smart_bachat_swachh_bharat_cess_second_year);
        TextView tv_bi_smart_bachat_krishi_kalyan_cess_second_year = (TextView) d
                .findViewById(R.id.tv_bi_smart_bachat_krishi_kalyan_cess_second_year);

        TextView tv_uin_smart_bachat = (TextView) d
                .findViewById(R.id.tv_uin_smart_bachat);


        TextView tv_bi_smart_bachat_premium_accidental_death_disability_name = (TextView) d
                .findViewById(R.id.tv_bi_smart_bachat_premium_accidental_death_disability_name);

        TextView tv_bi_smart_bachat_premium_accidental_death_disability_value = (TextView) d
                .findViewById(R.id.tv_bi_smart_bachat_premium_accidental_death_disability_value);

        TextView tv_bi_smart_bachat_premium_excluding_service_tax_name = (TextView) d
                .findViewById(R.id.tv_bi_smart_bachat_premium_excluding_service_tax_name);

        TextView tv_bi_smart_bachat_premium_excluding_service_tax_value = (TextView) d
                .findViewById(R.id.tv_bi_smart_bachat_premium_excluding_service_tax_value);


        TextView tv_bi_smart_bachat_premium_excluding_service_tax_value_2 = (TextView) d
                .findViewById(R.id.tv_bi_smart_bachat_premium_excluding_service_tax_value_2);
        TextView tv_bi_smart_bachat_premium_accidental_death_disability_value_2 = (TextView) d
                .findViewById(R.id.tv_bi_smart_bachat_premium_accidental_death_disability_value_2);


        TextView tv_bi_smart_bachat_backdating_interest = (TextView) d
                .findViewById(R.id.tv_bi_smart_bachat_backdating_interest);

        TableRow tr_bi_smart_bachat_backdating = (TableRow) d
                .findViewById(R.id.tr_bi_smart_bachat_backdating);

        View view_backdating = (View) d.findViewById(R.id.view_backdating);


        final CheckBox cb_statement = (CheckBox) d
                .findViewById(R.id.cb_statement);
        cb_statement.setChecked(false);

        /* Need Analysis */
        final TextView txt_proposer_name_need_analysis = d
                .findViewById(R.id.txt_proposer_name_need_analysis);

        final CheckBox cb_statement_need_analysis = (CheckBox) d
                .findViewById(R.id.cb_statement_need_analysis);
        cb_statement_need_analysis.setChecked(true);
        TableRow tr_need_analysis = (TableRow) d
                .findViewById(R.id.tr_need_analysis);
        final CheckBox checkboxAgentStatement = d.findViewById(R.id.checkboxAgentStatement);
        checkboxAgentStatement.setChecked(false);

        /* Agent Consent */


        TextView tv_proposerage = (TextView) d
                .findViewById(R.id.tv_proposerage);
        TextView tv_proposergender = (TextView) d.findViewById(R.id.tv_proposergender);


        TextView tv_bi_smart_bachat_installment_premium = (TextView) d
                .findViewById(R.id.tv_bi_smart_bachat_installment_premium);
        TextView tv_bi_smart_bachat_sum_assured_on_death = (TextView) d
                .findViewById(R.id.tv_bi_smart_bachat_sum_assured_on_death);
        TextView tv_bi_smart_bachat_AD_TPD_sum_assured = (TextView) d
                .findViewById(R.id.tv_bi_smart_bachat_AD_TPD_sum_assured);
        TextView tv_bi_smart_bachat_base_premium_without_tax = (TextView) d
                .findViewById(R.id.tv_bi_smart_bachat_base_premium_without_tax);
        TextView tv_bi_smart_bachat_AD_TPD_premium_without_tax = (TextView) d
                .findViewById(R.id.tv_bi_smart_bachat_AD_TPD_premium_without_tax);

        TextView tv_bi_smart_bachat_base_premium_without_tax2 = (TextView) d
                .findViewById(R.id.tv_bi_smart_bachat_base_premium_without_tax2);
        TextView tv_bi_smart_bachat_base_premium_with_tax_first_year2 = (TextView) d
                .findViewById(R.id.tv_bi_smart_bachat_base_premium_with_tax_first_year2);
        TextView tv_bi_smart_bachat_base_premium_with_tax_second_year2 = (TextView) d
                .findViewById(R.id.tv_bi_smart_bachat_base_premium_with_tax_second_year2);

        TextView tv_bi_smart_bachat_base_premium_with_tax_first_year = (TextView) d
                .findViewById(R.id.tv_bi_smart_bachat_base_premium_with_tax_first_year);

        TextView tv_bi_smart_bachat_base_premium_with_tax_second_year = (TextView) d
                .findViewById(R.id.tv_bi_smart_bachat_base_premium_with_tax_second_year);

        if (NeedAnalysisActivity.str_need_analysis != null) {
            if (NeedAnalysisActivity.str_need_analysis.equals("1")) {

                File mypath_old = mStorageUtils.createFileToAppSpecificDir(context, "NA" + ".pdf");
                File mypath_new = mStorageUtils.createFileToAppSpecificDir(context,
                        QuatationNumber + "_NA" + ".pdf");
                if (mypath_old.exists()) {
                    mypath_old.renameTo(mypath_new);
                }

                File mypath_old_Annexure1 = mStorageUtils.createFileToAppSpecificDir(context,
                        "NA_Annexure1.pdf");
                File mypath_old_Annexure2 = mStorageUtils.createFileToAppSpecificDir(context,
                        "NA_Annexure2.pdf");

                File mypath_new_Annexure1 = mStorageUtils.createFileToAppSpecificDir(context,
                        QuatationNumber + "_NA1" + ".pdf");
                File mypath_new_Annexure2 = mStorageUtils.createFileToAppSpecificDir(context,
                        QuatationNumber + "_NA2" + ".pdf");

                if (mypath_old_Annexure1.exists()) {
                    mypath_old_Annexure1.renameTo(mypath_new_Annexure1);
                }
                if (mypath_old_Annexure2.exists()) {
                    mypath_old_Annexure2.renameTo(mypath_new_Annexure2);
                }
                flg_needAnalyis = "1";
                tr_need_analysis.setVisibility(View.VISIBLE);
            } else {
                cb_statement_need_analysis.setChecked(true);
                tr_need_analysis.setVisibility(View.GONE);
            }

        }
        TextView tv_state = (TextView) d.findViewById(R.id.tv_state);
        if (cb_kerladisc.isChecked()) {
            tv_state.setText("Kerala");
            tv_bi_smart_bachat_rate_of_applicable_taxes.setText("4.75% in the 1st policy year and 2.375% from 2nd policy year onwards");
        } else {
            tv_state.setText("Non Kerala");
            tv_bi_smart_bachat_rate_of_applicable_taxes.setText("4.5% in the 1st policy year and 2.25% from 2nd policy year onwards");
        }

        Button btn_proceed = (Button) d.findViewById(R.id.btn_proceed);

        Ibtn_signatureofMarketing = (ImageButton) d
                .findViewById(R.id.Ibtn_signatureofMarketing);
        Ibtn_signatureofPolicyHolders = (ImageButton) d
                .findViewById(R.id.Ibtn_signatureofPolicyHolders);

        btn_MarketingOfficalDate = (Button) d
                .findViewById(R.id.btn_MarketingOfficalDate);
        btn_PolicyholderDate = (Button) d
                .findViewById(R.id.btn_PolicyholderDate);
        Calendar calender = Calendar.getInstance();
        int Year = calender.get(Calendar.YEAR);
        int Month = calender.get(Calendar.MONTH);
        int Day = calender.get(Calendar.DAY_OF_MONTH);
        list_data.clear();

        if (!proposer_sign.equals("")) {
            if (flg_needAnalyis.equals("1")) {
                tr_need_analysis.setVisibility(View.VISIBLE);
            } else {
                cb_statement_need_analysis.setChecked(true);
                tr_need_analysis.setVisibility(View.GONE);
            }
        }
        String input = inputVal.toString();
        output = retVal.toString();

        if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {

            tvCbStatementFirst
                    .setText("I, "
                            + name_of_life_assured
                            + ",having received the information with respect to the above, have understood the above statement before entering into a contract.");

            txt_proposer_name_need_analysis
                    .setText("I, "
                            + name_of_life_assured
                            + ", have undergone the Need Analysis and after having reviewed the SBI Life Product options, I have opted for 'SBI LIFE - " +
                            getString(R.string.sbi_life_smart_future_choices));

            tv_proposername.setText(name_of_life_assured);

            age_entry = prsObj.parseXmlTag(input, "age");

            tv_proposerage.setText(age_entry);
            tv_proposergender.setText(gender);

            ProposerAge = lifeAssuredAge;
            tv_bi_smart_bachat_life_assured_name.setText(name_of_life_assured);
        } else if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("n")) {
            tv_proposername.setText(name_of_proposer);
            tv_bi_smart_bachat_life_assured_name.setText(name_of_proposer);
            ProposerAge = prsObj.parseXmlTag(input, "ProposerAge");
            tv_proposergender.setText(gender_proposer);

            tvCbStatementFirst
                    .setText("I, "
                            + name_of_proposer
                            + ",having received the information with respect to the above, have understood the above statement before entering into a contract.");

            txt_proposer_name_need_analysis
                    .setText("I, "
                            + name_of_proposer
                            + ", have undergone the Need Analysis and after having reviewed the SBI Life Product options, I have opted for 'SBI LIFE - " +
                            getString(R.string.sbi_life_smart_future_choices));

            tv_proposerage.setText(ProposerAge + " Years");
            //}
        }
        tv_proposal_number.setText(QuatationNumber);

        TextView textviewAGentStatement = d.findViewById(R.id.textviewAGentStatement);
        textviewAGentStatement.setText("I, " + commonMethods.getUserName(context)
                + " have explained the premiums and benefits under the policy fully to the prospect/policyholder.");
        if (place2.equals("")) {
            edt_Policyholderplace.setText(place2);

        } else {
            edt_Policyholderplace.setText(place2);
        }

        if (!date2.equals("")) {
            btn_PolicyholderDate.setText(commonMethods.getDDMMYYYYDate(date2));
        } else {
            date2 = commonMethods.getMMDDYYYYDatabaseDate(getCurrentDate());
            btn_PolicyholderDate.setText(getCurrentDate());
        }

        if (!date1.equals("")) {
            btn_MarketingOfficalDate.setText(commonMethods.getDDMMYYYYDate(date1));
        } else {
            date1 = commonMethods.getMMDDYYYYDatabaseDate(getCurrentDate());
            btn_MarketingOfficalDate.setText(getCurrentDate());
        }
        if (agent_sign != null && !agent_sign.equals("")) {
            cb_statement.setChecked(true);
            checkboxAgentStatement.setChecked(true);
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
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Check = "Photo";
                        commonMethods.windowmessage(context, "_cust1Photo.jpg");
                    }
                });

        Ibtn_signatureofMarketing
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
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

                    }
                });

        Ibtn_signatureofPolicyHolders
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
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
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
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
                    }
                });

        radioButtonTrasactionModeManual
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        if (isChecked) {
                            transactionMode = "Manual";
                        } else {

                        }
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

        btn_proceed.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                final String isActive = "0";
                // if (frmProductHome.equals("FALSE")) {

                //name_of_person = edt_proposer_name.getText().toString();
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
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf((sum_assured.equals("") || sum_assured == null) ? "0"
                                            : sum_assured))),
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(premBasicRoundUp))),
                            emailId, mobileNo, agentEmail, agentMobile,
                            na_input, na_output, premium_paying_frequency, Integer
                            .parseInt(policy_term), smartbachatbean.getPremiumpayingterm(), productCode,
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
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf((sum_assured.equals("") || sum_assured == null) ? "0"
                                            : sum_assured))),
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(premBasicRoundUp))),
                            agentEmail, agentMobile, na_input, na_output,
                            premium_paying_frequency, Integer.parseInt(policy_term), smartbachatbean.getPremiumpayingterm(),
                            productCode, commonMethods.getDDMMYYYYDate(lifeAssured_date_of_birth),
                            commonMethods.getDDMMYYYYDate(proposer_date_of_birth), "", transactionMode, inputVal
                            .toString(), retVal.toString().replace(
                            bussIll.toString(), "")));
                    /* end */

                    createSmartFutureChoicesPdf();

                    NABIObj.serviceHit(BI_SmartFutureChoicesActivity.this,
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

            }

        });

        btn_PolicyholderDate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                DIALOG_ID = 2;
                showDateDialog();
            }
        });

        btn_MarketingOfficalDate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                DIALOG_ID = 3;
                showDateDialog();

            }
        });

        sum_assured = prsObj.parseXmlTag(input, "sumAssured");
        tv_uin_smart_bachat
                .setText("Benefit Illustration(BI): SBI Life - Smart Future Choices (UIN No -"
                        + "111N127V01" + ")" + "\n" + "An Individual, Non-Linked, Participating, Life Insurance Savings Product");
        tv_bi_smart_bachat_life_assured_name.setText(name_of_life_assured);
        age_entry = prsObj.parseXmlTag(input, "age");
        tv_bi_smart_bachat_life_assured_age.setText(age_entry + " Years");

        gender = prsObj.parseXmlTag(input, "gender");
        tv_bi_smart_bachat_life_assured_gender.setText(gender);

        premium_paying_frequency = prsObj.parseXmlTag(input, "premFreq");
        tv_bi_smart_bachat_life_assured_premium_frequency
                .setText(premium_paying_frequency);

        staffdiscount = prsObj.parseXmlTag(input, "isStaff");

        if (staffdiscount.equalsIgnoreCase("true")) {
            tv_bi_is_Staff.setText("Yes");
        } else {
            tv_bi_is_Staff.setText("No");
        }

        isJkResident = prsObj.parseXmlTag(input, "isJKResident");

        if (isJkResident.equalsIgnoreCase("true")) {
            tv_bi_is_jk.setText("Yes");
        } else {
            tv_bi_is_jk.setText("No");
        }

        plan = prsObj.parseXmlTag(input, "plan");
        tv_bi_smart_bachat_plan_proposed.setText(plan);

        bonusOption = prsObj.parseXmlTag(input, "bonusOption");
        tv_bi_smart_bachat_bonus_option.setText(bonusOption);

        policy_term = prsObj.parseXmlTag(input, "policyTerm");
        tv_bi_smart_bachat_term.setText(policy_term + "");

        premPayTerm = prsObj.parseXmlTag(input, "premPayTerm");
        String payingTerm = "";

        payingTerm = premPayTerm + "";

        tv_bi_smart_bachat_premium_paying_term.setText(payingTerm);

        if (plan.equalsIgnoreCase("Classic Choice")) {
            tv_premium_text_for_option_heading.setText("Classic Choice");

        } else {
            tv_premium_text_for_option_heading.setText("Flexi Choice");

        }

        SumAssuredBasic = prsObj.parseXmlTag(output, "SumAssuredBasic");
        tv_bi_smart_bachat_sum_assured.setText("" + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                .valueOf(SumAssuredBasic.equals("") ? "0"
                        : SumAssuredBasic))))));

        installmntPrem = prsObj.parseXmlTag(output, "installmntPrem");
        tv_bi_smart_bachat_installment_premium.setText("" + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                .valueOf(installmntPrem.equals("") ? "0"
                        : installmntPrem))))));

        BasicInstallmentPremFirstYear = prsObj.parseXmlTag(output, "installmntPremWithSerTx");

        BasicInstallmentPremSecondYear = prsObj.parseXmlTag(output, "installmntPremWithSerTxSecondYear");

        if (premium_paying_frequency.equals("Yearly")) {

            tv_mandatory_bi_smart_bachat_yearly_premium_with_tax1
                    .setText("Yearly Premium with Applicable taxes(Rs.)");

            tv_bi_smart_bachat_premium_accidental_death_disability_name
                    .setText("Yearly premium for Accidental Death and Disability Benefit(in Rs.)");

            tv_bi_smart_bachat_premium_excluding_service_tax_name
                    .setText("Yearly Premium for base cover(Rs.)");

            tv_premium_install_rider_type1
                    .setText("Yearly Premium excluding Applicable taxes(Rs.)");

        } else if (premium_paying_frequency.equals("Half-Yearly")) {

            tv_mandatory_bi_smart_bachat_yearly_premium_with_tax1
                    .setText("Half Yearly Premium with Applicable taxes(Rs.)");

            tv_premium_install_rider_type1
                    .setText("Half Yearly Premium excluding Applicable taxes(Rs.)");

            tv_bi_smart_bachat_premium_accidental_death_disability_name
                    .setText("Half Yearly premium for Accidental Death and Disability Benefit(in Rs.)");

            tv_bi_smart_bachat_premium_excluding_service_tax_name
                    .setText("Half Yearly Premium for base cover(Rs.)");

        } else if (premium_paying_frequency.equals("Monthly")) {

            tv_mandatory_bi_smart_bachat_yearly_premium_with_tax1
                    .setText("Monthly Premium with Applicable taxes(Rs.)");

            tv_premium_install_rider_type1
                    .setText("Monthly Premium excluding Applicable taxes(Rs.)");

            tv_bi_smart_bachat_premium_accidental_death_disability_name
                    .setText("Monthly premium for Accidental Death and Disability Benefit(in Rs.)");

            tv_bi_smart_bachat_premium_excluding_service_tax_name
                    .setText("Monthly Premium for base cover(Rs.)");
        }

        servicetax = prsObj.parseXmlTag(output, "servTax");

        premWthSTSecondYear = prsObj.parseXmlTag(output,
                "installmntPremWithSerTxSecondYear");

        basicServiceTax = prsObj.parseXmlTag(output, "servTax");

        tv_bi_smart_bachat_basic_service_tax_first_year.setText(""
                + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                .valueOf(basicServiceTax.equals("") ? "0"
                        : basicServiceTax))))));


        tv_bi_smart_bachat_service_tax_second_year.setText(""
                + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                .valueOf(servcTaxSecondYear.equals("") ? "0"
                        : servcTaxSecondYear))))));

        tv_bi_smart_bachat_yearly_premium_with_tax_second_year.setText(""
                + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                .valueOf(premWthSTSecondYear.equals("") ? "0"
                        : premWthSTSecondYear))))));

        String death_gurantee_sumAssured = prsObj.parseXmlTag(output, "GuaranteedDeathBenefit1");

        Smart_bachat_sum_assured_on_death = getformatedThousandString(Integer.parseInt(obj.getRound(obj
                .getStringWithout_E(Double.valueOf((death_gurantee_sumAssured
                        .equals("") || death_gurantee_sumAssured == null) ? "0"
                        : death_gurantee_sumAssured)))));

        tv_bi_smart_bachat_sum_assured_on_death.setText("Rs."
                + Smart_bachat_sum_assured_on_death);

        tv_bi_smart_bachat_base_premium_without_tax.setText(
                getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf(installmntPrem
                        .equals("") ? "0" : installmntPrem))))));

        tv_bi_smart_bachat_base_premium_without_tax2.setText(
                getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf(installmntPrem
                        .equals("") ? "0" : installmntPrem))))));
        tv_bi_smart_bachat_base_premium_with_tax_first_year2.setText(getformatedThousandString(Integer.parseInt(obj.getRound(obj.
                getStringWithout_E(Double.valueOf(BasicInstallmentPremFirstYear
                        .equals("") ? "0" : BasicInstallmentPremFirstYear))))));
        tv_bi_smart_bachat_base_premium_with_tax_second_year2.setText(getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf(
                BasicInstallmentPremSecondYear.equals("") ? "0" : BasicInstallmentPremSecondYear))))));


        tv_bi_smart_bachat_base_premium_with_tax_first_year.setText(getformatedThousandString(Integer.parseInt(obj.getRound(obj.
                getStringWithout_E(Double.valueOf(BasicInstallmentPremFirstYear
                        .equals("") ? "0" : BasicInstallmentPremFirstYear))))));


        tv_bi_smart_bachat_base_premium_with_tax_second_year.setText(getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf(
                BasicInstallmentPremSecondYear.equals("") ? "0" : BasicInstallmentPremSecondYear))))));


        for (int i = 1; i <= Integer.parseInt(policy_term); i++) {

            String end_of_year = prsObj
                    .parseXmlTag(output, "policyYr" + i + "");
            String total_base_premium = ((int) Double.parseDouble(prsObj
                    .parseXmlTag(output, "AnnualizedPremium" + i + ""))) + "";
            String guaranteed_surrender_value = ((int) Double
                    .parseDouble(prsObj.parseXmlTag(output, "GuranteedSurrenderBenefit"
                            + i + "")))
                    + "";
            String GuranteedSurvivalBenefit = ((int) Double
                    .parseDouble(prsObj.parseXmlTag(output, "GuranteedSurvivalBenefit"
                            + i + "")))
                    + "";


            String guarntdDeathBenft = ((int) Double.parseDouble(prsObj
                    .parseXmlTag(output, "GuaranteedDeathBenefit" + i + ""))) + "";
            String MaturityBenefit = ((int) Double
                    .parseDouble(prsObj.parseXmlTag(output, "GuaranteedMaturityBen" + i
                            + "")))
                    + "";

            String not_guaranteed_surrender_value_4per = ((int) Double
                    .parseDouble(prsObj.parseXmlTag(output,
                            "SurrenderBen4Per" + i + "")))
                    + "";

            String not_guaranteed_surrender_value_8per = ((int) Double
                    .parseDouble(prsObj.parseXmlTag(output,
                            "SurrenderBen8Per" + i + ""))) + "";

            String CashBonus4percent = ((int) Double
                    .parseDouble(prsObj.parseXmlTag(output,
                            "CashBonus4percent" + i + ""))) + "";

            String bonusOption4per = ((int) Double
                    .parseDouble(prsObj.parseXmlTag(output,
                            "Defferedcash4per" + i + ""))) + "";


            String CashBonus8percent = ((int) Double
                    .parseDouble(prsObj.parseXmlTag(output,
                            "CashBonus8percent" + i + ""))) + "";

            String bonusOption8per = ((int) Double
                    .parseDouble(prsObj.parseXmlTag(output,
                            "Defferedcash8per" + i + ""))) + "";

            String TotalMaturityBenefit4per = ((int) Double
                    .parseDouble(prsObj.parseXmlTag(output,
                            "totalmaturity4per" + i + "")))
                    + "";
            String TotalMaturityBenefit8per = ((int) Double
                    .parseDouble(prsObj.parseXmlTag(output,
                            "totalmaturity8per" + i + "")))
                    + "";
            String TotalDeathBenefit4per = ((int) Double
                    .parseDouble(prsObj.parseXmlTag(output,
                            "totalDeathBen4per" + i + "")))
                    + "";
            String TotalDeathBenefit8per = ((int) Double
                    .parseDouble(prsObj.parseXmlTag(output,
                            "totalDeathBen8per" + i + "")))
                    + "";

            bussIll.append("<GuaranteedMaturityBen").append(i).append(">").append(GuaranteedMaturityBen).append("</GuaranteedMaturityBen").append(i).append(">");

            list_data.add(new M_BI_SmartFutureChoices_Adapter(end_of_year,
                    total_base_premium,
                    "0",
                    GuranteedSurvivalBenefit,
                    guaranteed_surrender_value,
                    guarntdDeathBenft,
                    MaturityBenefit,
                    "0",
                    CashBonus4percent, bonusOption4per,
                    not_guaranteed_surrender_value_4per,
                    "0",
                    CashBonus8percent, bonusOption8per,
                    not_guaranteed_surrender_value_8per,
                    TotalMaturityBenefit4per,
                    TotalMaturityBenefit8per,
                    TotalDeathBenefit4per,
                    TotalDeathBenefit8per));
        }

        Adapter_BI_SmartFutureChoicesGridNew adapter = new Adapter_BI_SmartFutureChoicesGridNew(this, list_data);
        gv_userinfo.setAdapter(adapter);

        GridHeight gh = new GridHeight();
        gh.getheight(gv_userinfo, policy_term);
        d.show();

    }



    public String getformatedThousandString(int number) {
        return NumberFormat.getNumberInstance(Locale.US)
                .format(number);
    }

    public String getCurrentDate() {
        Calendar present_date = Calendar.getInstance();
        int mDay = present_date.get(Calendar.DAY_OF_MONTH);
        int mMonth = present_date.get(Calendar.MONTH) + 1;
        int mYear = present_date.get(Calendar.YEAR);

        return mDay + "-" + mMonth + "-" + mYear;

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


    // method to set a clearing a element
    private void clearFocusable(View v) {
        // TODO Auto-generated method stub
        v.setFocusable(false);
        v.setFocusableInTouchMode(false);
        // v.clearFocus();
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

    public void setDefaultDate(int id) {
        Calendar present_date = Calendar.getInstance();
        present_date.add(Calendar.YEAR, -id);
        mDay = present_date.get(Calendar.DAY_OF_MONTH);
        mMonth = present_date.get(Calendar.MONTH);
        mYear = present_date.get(Calendar.YEAR);
    }

    private boolean matval() {

        int age = Integer.parseInt(spinner_age.getSelectedItem().toString());
        int policyterm = Integer.parseInt(spinner_policyterm.getSelectedItem()
                .toString());

        int val = age + policyterm;

        if (val > 70) {
            Alert("Maximum maturity age is 70 Years.");

            return false;
        }

        return true;
    }

    public boolean valDoYouBackdate() {
        if (!proposer_Backdating_WishToBackDate_Policy.equals("")) {
            return true;
        } else {
            commonMethods.showMessageDialog(context, "Please Select Do you wish to Backdate ");
            commonMethods.setFocusable(rb_smart_bachat_backdating_yes);
            rb_smart_bachat_backdating_yes.requestFocus();

            return false;

        }
    }

    public boolean valProposerDob() {

        if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("N")) {

            if (proposer_date_of_birth.equals("")
                    || proposer_date_of_birth.equalsIgnoreCase("Select Date")) {
                commonMethods.showMessageDialog(context, "Please Select Valid Date Of Birth For Proposer");
                commonMethods.setFocusable(btn_bi_smart_future_choices_proposer_date);
                btn_bi_smart_future_choices_proposer_date
                        .requestFocus();

                return false;
            } else {
                return true;
            }
        } else
            return true;
    }

    public boolean valBackdate() {
        if (proposer_Backdating_WishToBackDate_Policy.equals("y")) {

            if (proposer_Backdating_BackDate.equals("")) {
                commonMethods.showMessageDialog(context, "Please Select Backdate ");
                commonMethods.setFocusable(btn_smart_bachat_backdatingdate);
                btn_smart_bachat_backdatingdate.requestFocus();
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    boolean TrueBackdate() {

        try {

            String error = "", error1 = "";

            if (rb_smart_bachat_backdating_yes.isChecked()) {

                final Calendar c = Calendar.getInstance();
                final int currYear = c.get(Calendar.YEAR);
                final int currMonth = c.get(Calendar.MONTH) + 1;

                SimpleDateFormat dateformat1 = new SimpleDateFormat(
                        "dd-MM-yyyy");
                Date dtBackDate = dateformat1
                        .parse(btn_smart_bachat_backdatingdate.getText()
                                .toString());
                Date currentDate = c.getTime();

                Date finYerEndDate = null;

                String back_date_fix = "29-07-2020";

                Date date1 = dateformat1.parse(back_date_fix);
                // if(date1.compareTo(dtBackDate) >= 0){
                // error1 = "Please enter backdation date after 9-1-2017";
                // }

                if (currMonth >= 4) {
                    finYerEndDate = dateformat1.parse("1-4-" + currYear);
                } else {
                    finYerEndDate = dateformat1.parse("1-4-" + (currYear - 1));
                }
                if (date1.compareTo(dtBackDate) > 0) {
                    error = "Please enter backdation date after 29-07-2020";
                } else if (currentDate.before(dtBackDate)) {
                    error = "Please enter backdation date between "
                            + dateformat1.format(finYerEndDate) + " and "
                            + dateformat1.format(currentDate);
                } else if (dtBackDate.before(finYerEndDate)) {
                    error = "Please enter Backdation date between "
                            + dateformat1.format(finYerEndDate) + " and "
                            + dateformat1.format(currentDate);
                }

                if (!error.equals("")) {
                    commonMethods.showMessageDialog(context, error);
                    btn_smart_bachat_backdatingdate
                            .setText("Select Date");
                    proposer_Backdating_BackDate = "";
                    commonMethods.setFocusable(btn_smart_bachat_backdatingdate);
                    btn_smart_bachat_backdatingdate
                            .requestFocus();

                    return false;
                }

            }

        } catch (Exception e) {
            return false;
        }

        return true;

    }

    private String valInstPremium(String premiumSingleInstBasic, String premFreq) {
        String error = "";

        if (premFreq.equals("Yearly") && (Integer.parseInt(premiumSingleInstBasic) < 100000)) {
            error = "Minimum premium for Yearly Mode under this product is Rs.100000";

        } else if (premFreq.equals("Half Yearly") && (Integer.parseInt(premiumSingleInstBasic) < 51000)) {
            error = "Minimum premium for Half Yearly Mode under this product is Rs.51000";
        } else if (premFreq.contains("Monthly") && (Integer.parseInt(premiumSingleInstBasic) < 8500)) {
            error = "Minimum premium for Monthly Mode under this product is Rs.8500";
        } else if (Sumassured_Basic <= 655000) {
            error = "Minimum sumassured under this product is Rs.655000";
        } else {
            return error;
        }
        return error;
    }

   /* private String valInstPremium(String premiumSingleInstBasic, String premFreq) {
        String error = "";

        if (premFreq.equals("Yearly") && (Integer.parseInt(premiumSingleInstBasic) < 100000)) {
            error = "Minimum premium for Yearly Mode under this product is Rs.100000";

        } else if (premFreq.equals("Half Yearly") && (Integer.parseInt(premiumSingleInstBasic) < 51000)) {
            error = "Minimum premium for Half Yearly Mode under this product is Rs.51000";
        } else if (premFreq.contains("Monthly") && (Integer.parseInt(premiumSingleInstBasic) < 8500)) {
            error = "Minimum premium for Monthly Mode under this product is Rs.8500";
        } else {
            return error;
        }
        return error;
    }*/

    public boolean valPremAmount(double premiumwithRoundUP) {
        String error = "";

        if ((premiumwithRoundUP < 100000)
                && spinner_premiumfreq.getSelectedItem().toString()
                .equals("Yearly")) {
            error = "Minimum Premium for Yearly Frequency mode is 100000";
        } else if ((premiumwithRoundUP < 51000)
                && spinner_premiumfreq.getSelectedItem().toString()
                .equals("Half-Yearly")) {
            error = "Minimum Premium for Half - Yearly Frequency mode is 51000";
        } else if ((premiumwithRoundUP < 8500)
                && spinner_premiumfreq.getSelectedItem().toString()
                .equals("Monthly")) {
            error = "Minimum Premium for Monthly Frequency mode must is 8500";
        }

        if (!error.equals("")) {
            Alert(error);
            return false;
        } else
            return true;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        // TODO Auto-generated method stub
        if (v.getId() == edt_bi_smart_bachat_life_assured_first_name.getId()) {
            // clearFocusable(edt_bi_smart_bachat_life_assured_first_name);
            commonMethods.setFocusable(edt_bi_smart_bachat_life_assured_middle_name);
            edt_bi_smart_bachat_life_assured_middle_name.requestFocus();
        } else if (v.getId() == edt_bi_smart_bachat_life_assured_middle_name
                .getId()) {
            // clearFocusable(edt_bi_smart_bachat_life_assured_middle_name);
            commonMethods.setFocusable(edt_bi_smart_bachat_life_assured_last_name);
            edt_bi_smart_bachat_life_assured_last_name.requestFocus();
        } else if (v.getId() == edt_bi_smart_bachat_life_assured_last_name
                .getId()) {

            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(
                    edt_bi_smart_bachat_life_assured_last_name.getWindowToken(),
                    0);
            // clearFocusable(edt_bi_smart_bachat_life_assured_last_name);
            commonMethods.setFocusable(btn_bi_smart_future_choices_life_assured_date);
            btn_bi_smart_future_choices_life_assured_date.requestFocus();
        } else if (v.getId() == edt_bi_smart_future_choices_proposer_first_name.getId()) {
            // clearFocusable(edt_bi_smart_future_choices_proposer_first_name);
            commonMethods.setFocusable(edt_bi_smart_future_choices_proposer_middle_name);
            edt_bi_smart_future_choices_proposer_middle_name.requestFocus();
        } else if (v.getId() == edt_bi_smart_future_choices_proposer_middle_name
                .getId()) {
            // clearFocusable(edt_bi_smart_future_choices_proposer_middle_name);
            commonMethods.setFocusable(edt_bi_smart_future_choices_proposer_last_name);
            edt_bi_smart_future_choices_proposer_last_name.requestFocus();
        } else if (v.getId() == edt_bi_smart_future_choices_proposer_last_name.getId()) {

            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(
                    edt_bi_smart_future_choices_proposer_last_name.getWindowToken(), 0);
            // clearFocusable(edt_bi_smart_future_choices_proposer_last_name);
            commonMethods.setFocusable(btn_bi_smart_future_choices_proposer_date);
            btn_bi_smart_future_choices_proposer_date.requestFocus();
        } else if (v.getId() == edt_proposerdetail_basicdetail_contact_no.getId()) {
            // clearFocusable(premiumAmt);
            commonMethods.setFocusable(edt_proposerdetail_basicdetail_Email_id);
            edt_proposerdetail_basicdetail_Email_id.requestFocus();
        } else if (v.getId() == edt_proposerdetail_basicdetail_Email_id.getId()) {
            // clearFocusable(premiumAmt);
            commonMethods.setFocusable(edt_proposerdetail_basicdetail_ConfirmEmail_id);
            edt_proposerdetail_basicdetail_ConfirmEmail_id.requestFocus();
        } else if (v.getId() == edt_proposerdetail_basicdetail_ConfirmEmail_id
                .getId()) {
            // clearFocusable(premiumAmt);
            clearFocusable(spinner_select_plan);
            commonMethods.setFocusable(spinner_select_plan);
            spinner_select_plan.requestFocus();
        } else if (v.getId() == spinner_select_plan.getId()) {
            // clearFocusable(premiumAmt);
            clearFocusable(spinner_policyterm);
            commonMethods.setFocusable(spinner_policyterm);
            spinner_policyterm.requestFocus();
        } else if (v.getId() == spinner_policyterm.getId()) {
            // clearFocusable(premiumAmt);
            clearFocusable(spinner_premium_payment_term);
            commonMethods.setFocusable(spinner_premium_payment_term);
            spinner_premium_payment_term.requestFocus();
        } else if (v.getId() == spinner_premium_payment_term.getId()) {
            // clearFocusable(premiumAmt);
            clearFocusable(spinner_premiumfreq);
            commonMethods.setFocusable(spinner_premiumfreq);
            spinner_premiumfreq.requestFocus();
        } else if (v.getId() == spinner_premiumfreq.getId()) {
            // clearFocusable(premiumAmt);
            clearFocusable(et_sumassured);
            commonMethods.setFocusable(et_sumassured);
            et_sumassured.requestFocus();
        } else if (v.getId() == et_sumassured.getId()) {
            // clearFocusable(premiumAmt);
            commonMethods.setFocusable(et_adtpd_benifit);
            et_adtpd_benifit.requestFocus();
        } else if (v.getId() == et_adtpd_benifit.getId()) {
            // clearFocusable(premiumAmt);
            commonMethods.setFocusable(rb_smart_bachat_backdating_no);
            rb_smart_bachat_backdating_no.requestFocus();
        } else if (v.getId() == rb_smart_bachat_backdating_no.getId()) {

            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(
                    rb_smart_bachat_backdating_no.getWindowToken(), 0);
        }
        return true;

    }


    public void BahrainMethod() {
        MobileLenth = 11;
        edt_proposerdetail_basicdetail_contact_no.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
    }


    boolean TrueBackdateLaunch() {
        try {
            String error = "";

            if (rb_smart_bachat_backdating_yes.isChecked()) {

                final Calendar c = Calendar.getInstance();
                final int currYear = c.get(Calendar.YEAR);
                final int currMonth = c.get(Calendar.MONTH) + 1;

                SimpleDateFormat dateformat1 = new SimpleDateFormat(
                        "dd-MM-yyyy");
                Date dtBackDate = dateformat1.parse(proposer_Backdating_BackDate);
                Date currentDate = c.getTime();

                Date finYerEndDate = null;

                Date launchDate = dateformat1.parse("03-06-2019");

                if (currMonth >= 4) {
                    finYerEndDate = dateformat1.parse("1-4-" + currYear);
                } else {
                    finYerEndDate = dateformat1.parse("1-4-" + (currYear - 1));
                }

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
                    commonMethods.showMessageDialog(context, error);
                    return false;
                }

            }

        } catch (Exception e) {
            return false;
        }

        return true;

    }



    /*public boolean ValNAPremium() {


        String Annupremium = obj.getRoundUp(obj
                .getStringWithout_E(Double.parseDouble(prsObj
                        .parseXmlTag(new String(inputVal), "sumAssured" + ""))));
        if (Annupremium != null && userType.equalsIgnoreCase("CIF")) {
            Double double_Annupremium = Double.parseDouble(Annupremium);
            Double double_suggested_annual_income = 0.0;
            String str_suggested_annual_income = "";
            if (NeedAnalysisActivity.str_suggested_annual_income != null && !NeedAnalysisActivity.str_suggested_annual_income.equals("")) {
                //  double_suggested_annual_income = Double.parseDouble(NeedAnalysisActivity.str_suggested_annual_income);
                String str_plan = "";
                if (spinner_premiumfreq.getSelectedItem().toString().equalsIgnoreCase("Single")) {
                    str_plan = "Single";

                } else if (spinner_policyterm.getSelectedItem().toString().equalsIgnoreCase(spinner_premium_payment_term.getSelectedItem().toString())) {
                    str_plan = "Regular";

                } else {
                    str_plan = "Limited";

                }

                str_suggested_annual_income = UtilityMain.CalculateSuggestedPremium(str_plan, spinner_policyterm.getSelectedItem().toString(), spinner_premium_payment_term.getSelectedItem().toString());


                double_suggested_annual_income = Double.parseDouble(str_suggested_annual_income);


                if ((double_Annupremium > double_suggested_annual_income) && userType.equalsIgnoreCase("CIF")) {
                    commonMethods.showMessageDialog(context,"Annualized premium can not be more than the suggested annual premium during Suitability Analysis");
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        } else {
            return true;
        }

    }
    public String CalculateSuggestedPremium(String Plan, String Term,String PPT) {

        double surplus_income = NeedAnalysisActivity.surplus_income;
        String strSavings = NeedAnalysisActivity.strSavings;
        double suggested_annual_income = 0;
        String str_suggested_annual_income = "";
        double annual_income_savings = 0;
        CommonForAllProd obj = new CommonForAllProd();
        if (Plan.equalsIgnoreCase("Single")) {
            annual_income_savings = (Double.parseDouble(strSavings) * 0.7);
        } else if (Plan.equalsIgnoreCase("Regular")) {
            annual_income_savings = ((Double.parseDouble(strSavings) * 0.7)) / Integer.parseInt(Term);
        }
        else if (Plan.equalsIgnoreCase("Limited") || Plan.equalsIgnoreCase("LPPT")) {
            annual_income_savings = ((Double.parseDouble(strSavings) * 0.7)) / Integer.parseInt(PPT);
        }

        else {
            annual_income_savings = (Double.parseDouble(strSavings) * 0.7);
        }
        suggested_annual_income = surplus_income + annual_income_savings;
        str_suggested_annual_income = obj.getRound(obj.getStringWithout_E(suggested_annual_income));
        return str_suggested_annual_income;
    }*/

    /* Basic Details Method */
    public boolean valCashBonus() {
        if (bonusOption.equalsIgnoreCase("")) {
            commonMethods.showMessageDialog(context, "Please select Cash Bonus Type");
            return false;
        } else {
            return true;
        }

    }

    public void createSmartFutureChoicesPdf() {

        currencyFormat = new DecimalFormat("##,##,##,###");
        decimalcurrencyFormat = new DecimalFormat("##,##,##,###.##");

        try {
            ParseXML prsObj = new ParseXML();
            System.out.println("Final output in BIPDF" + output);
            String basicPrem = prsObj.parseXmlTag(output, "basicPrem");
            String basicServiceTax = prsObj.parseXmlTag(output,
                    "basicServiceTax");
            String basicServiceTaxSecondYear = prsObj.parseXmlTag(output,
                    "basicServiceTaxSecondYear");
            String SBCServiceTax = prsObj.parseXmlTag(output, "SBCServiceTax");
            String SBCServiceTaxSecondYear = prsObj.parseXmlTag(output,
                    "SBCServiceTaxSecondYear");
            String KKCServiceTax = prsObj.parseXmlTag(output, "KKCServiceTax");
            String KKCServiceTaxSecondYear = prsObj.parseXmlTag(output,
                    "KKCServiceTaxSecondYear");
            String servTax = prsObj.parseXmlTag(output, "servTax");
            String servTaxSeondYear = prsObj.parseXmlTag(output,
                    "servTaxSecondYear");
            String installmntPremWithSerTx = prsObj.parseXmlTag(output,
                    "installmntPremWithSerTx");
            String installmntPremWithSerTxSecondYear = prsObj.parseXmlTag(
                    output, "installmntPremWithSerTxSecondYear");

            String installmntPrem = prsObj
                    .parseXmlTag(output, "installmntPrem");
            String modalprem = prsObj.parseXmlTag(output, "modalprem");
            String adtpdRiderprem = prsObj
                    .parseXmlTag(output, "adtpdRiderprem");

            String BASE_FONT_BOLD = "Trebuchet MS B";

            Font small_bold3 = new Font(Font.FontFamily.TIMES_ROMAN, 5,
                    Font.BOLD);
            Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
                    Font.NORMAL, BaseColor.RED);
            Font sub_headerBold = new Font(Font.FontFamily.TIMES_ROMAN, 11,
                    Font.BOLD);
            Font headerBold = new Font(Font.FontFamily.TIMES_ROMAN, 11,
                    Font.BOLD, BaseColor.WHITE);
            Font small_bold = new Font(Font.FontFamily.TIMES_ROMAN, 8,
                    Font.BOLD);
            Font small_bold_for_name = new Font(Font.FontFamily.TIMES_ROMAN,
                    10, Font.BOLD);
            Font small_bold1 = new Font(Font.FontFamily.TIMES_ROMAN, 6,
                    Font.BOLD);
            Font small_normal1 = new Font(Font.FontFamily.TIMES_ROMAN, 6,
                    Font.NORMAL);
            Font small_normal = new Font(Font.FontFamily.TIMES_ROMAN, 8,
                    Font.NORMAL);
            Font normal = new Font(Font.FontFamily.TIMES_ROMAN, 5, Font.NORMAL);
            Font normal_bold = new Font(Font.FontFamily.TIMES_ROMAN, 5,
                    Font.BOLD);
            Font normal1_bold = new Font(Font.FontFamily.TIMES_ROMAN, 6,
                    Font.BOLD);
            mypath = mStorageUtils.createFileToAppSpecificDir(context, QuatationNumber + "BI.pdf");
            needAnalysispath = mStorageUtils.createFileToAppSpecificDir(context, "NA.pdf");
            // needAnalysis_Tabular_path = new File(folder, "NA_Tabular.pdf");
            newFile = mStorageUtils.createFileToAppSpecificDir(context, QuatationNumber + "P01.pdf");

            PdfPCell cell;
            Rectangle rect = new Rectangle(594f, 792f);

            Document document = new Document(rect, 50, 50, 50, 50);
            // Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter pdf_writer = null;
            pdf_writer = PdfWriter.getInstance(document, new FileOutputStream(
                    mypath.getAbsolutePath()));

            document.open();

            // For SBI- Life Logo starts
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Bitmap bitmap = BitmapFactory.decodeResource(getBaseContext()
                    .getResources(), R.drawable.sbi_life_logo);
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
            Image img_sbi_logo = Image.getInstance(stream.toByteArray());
            img_sbi_logo.setAlignment(Image.LEFT);
            img_sbi_logo.getSpacingAfter();
            img_sbi_logo.scaleToFit(80, 50);

            Paragraph para_img_logo = new Paragraph("");
            para_img_logo.add(img_sbi_logo);

            Paragraph para_img_logo_after_space_1 = new Paragraph(" ");

            document.add(para_img_logo);
            // For SBI- Life Logo ends

            // To draw line after the sbi logo image
            document.add(new LineSeparator());
            document.add(para_img_logo_after_space_1);

            // For the BI Smart Elite Table Header(Grey One)
            Paragraph Para_Header = new Paragraph();
            Para_Header
                    .add(new Paragraph(
                            "Benefit Illustration(BI): SBI Life - Smart Future Choices (UIN: 111N127V01)" + "\n" + "An Individual, Non-Linked, Participating, Life Insurance Savings Product",
                            headerBold));

            PdfPTable headertable = new PdfPTable(1);
            headertable.setWidthPercentage(100);
            PdfPCell c1 = new PdfPCell(new Phrase(Para_Header));
            c1.setPadding(5);
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            headertable.addCell(c1);
            headertable.setHorizontalAlignment(Element.ALIGN_CENTER);
            Paragraph para_address = new Paragraph(
                    "SBI Life Insurance Co. Ltd" + "\n" + "Registered & Corporate Office: 'Natraj', M.V.Road and Western Express Highway Junction, Andheri (East), Mumbai - 400069",
                    small_bold_for_name);
            para_address.setAlignment(Element.ALIGN_CENTER);
            Paragraph para_address1 = new Paragraph(
                    "IRDAI Registration No. 111 | Website: www.sbilife.co.in | Email: info@sbilife.co.in | CIN: L99999MH2000PLC129113" + "\n" + "Toll Free: 1800 267 9090 (Between 9.00 am & 9.00 pm)" + "\n" + "Benefit Illustration(BI): SBI Life - Smart Future Choices (UIN: 111N127V01)" + "\n" + "An Individual, Non-Linked, Participating, Life Insurance Savings Product",
                    small_bold);
            para_address1.setAlignment(Element.ALIGN_CENTER);
            document.add(para_address);
            document.add(para_address1);
            //document.add(Para_Header);
            document.add(para_img_logo_after_space_1);

            document.add(para_img_logo_after_space_1);

            document.add(para_img_logo_after_space_1);
            if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("Y")) {
                name_of_proposer = name_of_life_assured;
            }

            PdfPTable table_proposer_name = new PdfPTable(4);
            // float[] columnWidths_table_proposer_name = { 2f, 2f, 1f, 2f };
            // table_proposer_name.setWidths(columnWidths_table_proposer_name);
            table_proposer_name.setWidthPercentage(100);

            PdfPCell ProposalNumber_cell_1 = new PdfPCell(new Paragraph(
                    "Quotation Number", small_normal));
            PdfPCell ProposalNumber_cell_2 = new PdfPCell(new Paragraph(""
                    + QuatationNumber, small_bold1));

            ProposalNumber_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell NameofProposal_cell_3 = new PdfPCell(new Paragraph(
                    "Channel / Intermediary ", small_normal));
            PdfPCell NameofProposal_cell_4 = new PdfPCell(new Paragraph(""
                    + userType, small_bold1));
            NameofProposal_cell_4.setHorizontalAlignment(Element.ALIGN_CENTER);

            NameofProposal_cell_4.setVerticalAlignment(Element.ALIGN_CENTER);

            ProposalNumber_cell_1.setPadding(5);
            ProposalNumber_cell_2.setPadding(5);
            NameofProposal_cell_3.setPadding(5);
            NameofProposal_cell_4.setPadding(5);

            table_proposer_name.addCell(ProposalNumber_cell_1);
            table_proposer_name.addCell(ProposalNumber_cell_2);
            table_proposer_name.addCell(NameofProposal_cell_3);
            table_proposer_name.addCell(NameofProposal_cell_4);
            document.add(table_proposer_name);
            document.add(para_img_logo_after_space_1);

            PdfPTable BI_Pdftable_Introdcution = new PdfPTable(1);
            BI_Pdftable_Introdcution.setWidthPercentage(100);
            PdfPCell BI_Pdftable_Introdcutioncell = new PdfPCell(new Paragraph(
                    "Introduction", small_bold));

            BI_Pdftable_Introdcutioncell
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_Pdftable_Introdcutioncell.setPadding(5);
            BI_Pdftable_Introdcutioncell
                    .setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_Pdftable_Introdcution.addCell(BI_Pdftable_Introdcutioncell);
            document.add(BI_Pdftable_Introdcution);

            PdfPTable BI_Pdftable2 = new PdfPTable(1);
            BI_Pdftable2.setWidthPercentage(100);
            // CR_table6.setWidths(columnWidths_2);
            PdfPCell BI_Pdftable2_cell1 = new PdfPCell(
                    new Paragraph(
                            "Insurance Regulatory & Development Authority of India (IRDAI) requires all life insurance companies operating in India to provide official illustrations to their customers. The illustrations are based on the investment rates of return set by the Life Insurance Council (constituted under Section 64C(a) of the Insurance Act 1938) and is not intended to reflect the actual investment returns achieved or which may be achieved in future by SBI life Insurance Company Limited.  All life insurance companies use the same rates in their benefit illustrations."
                            ,
                            small_normal));
            BI_Pdftable2_cell1.setPadding(5);
            BI_Pdftable2.addCell(BI_Pdftable2_cell1);
            //document.add(BI_Pdftable2);

            PdfPTable BI_Pdftable3 = new PdfPTable(1);
            BI_Pdftable3.setWidthPercentage(100);
            // CR_table6.setWidths(columnWidths_3);
            PdfPCell BI_Pdftable3_cell1 = new PdfPCell(
                    new Paragraph(
                            "The main objective of the illustration is that the client is able to appreciate the features of the product and the flow of benefits in different circumstances with some level of quantification. For further information on the product, its benefits and applicable charges please refer to the sales brochure and/or policy document.",
                            small_normal));
            BI_Pdftable3_cell1.setPadding(5);

            BI_Pdftable3.addCell(BI_Pdftable3_cell1);
            document.add(BI_Pdftable3);


            document.add(para_img_logo_after_space_1);

            // inputTable here -1

            PdfPTable personalDetail_table = new PdfPTable(2);
            personalDetail_table.setWidths(new float[]{5f, 5f,});
            personalDetail_table.setWidthPercentage(100f);

            personalDetail_table.setHorizontalAlignment(Element.ALIGN_LEFT);


            // 1st row
            cell = new PdfPCell(new Phrase(
                    "Proposer & Life Assured ", small_bold));
            cell.setColspan(2);
            cell.setPadding(5);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell.setBorder(Rectangle.BOTTOM | Rectangle.LEFT | Rectangle.RIGHT
                    | Rectangle.TOP);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            personalDetail_table.addCell(cell);


            cell = new PdfPCell(new Phrase("Name of the Prospect/Policyholder/Life Assured", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);

            if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {
                cell = new PdfPCell(new Phrase(
                        name_of_life_assured, small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPadding(5);
                personalDetail_table.addCell(cell);
            } else if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("n")) {
                cell = new PdfPCell(new Phrase(
                        name_of_proposer, small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPadding(5);
                personalDetail_table.addCell(cell);
            }

            cell = new PdfPCell(new Phrase("Age(Years)", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);
            if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {

                cell = new PdfPCell(new Phrase(age_entry + "" + "",
                        small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPadding(5);
                personalDetail_table.addCell(cell);
            } else if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("n")) {
                cell = new PdfPCell(new Phrase(ProposerAge + "" + "",
                        small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPadding(5);
                personalDetail_table.addCell(cell);
            }
            cell = new PdfPCell(new Phrase("Gender", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);
            if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {

                cell = new PdfPCell(new Phrase(gender + "" + "",
                        small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPadding(5);
                personalDetail_table.addCell(cell);
            } else if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("n")) {
                cell = new PdfPCell(new Phrase(gender_proposer + "" + "",
                        small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPadding(5);
                personalDetail_table.addCell(cell);
            }
            cell = new PdfPCell(new Phrase("State", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            //personalDetail_table.addCell(cell);

            if (cb_kerladisc.isChecked()) {
                cell = new PdfPCell(new Phrase("Kerala" + "" + "",
                        small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPadding(5);
                //personalDetail_table.addCell(cell);
            } else {
                cell = new PdfPCell(new Phrase("Non Kerala" + "" + "",
                        small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPadding(5);
                //personalDetail_table.addCell(cell);
            }

           /* cell = new PdfPCell(new Phrase("Name of the Life Assured", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    name_of_life_assured, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);


            cell = new PdfPCell(new Phrase("Age(Years)", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(smartbachatbean.getAge() + "" + " Years",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Gender", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(gender + "" + "",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);*/

            cell = new PdfPCell(new Phrase("Staff Discount", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);


            if (staffdiscount.equalsIgnoreCase("true")) {
                cell = new PdfPCell(new Phrase("Yes", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPadding(5);
                personalDetail_table.addCell(cell);
            } else {
                cell = new PdfPCell(new Phrase("No", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPadding(5);
                personalDetail_table.addCell(cell);
            }





           /* cell = new PdfPCell(new Phrase("Gender", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(smartbachatbean.getGender() + "",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);*/





         /*   cell = new PdfPCell(new Phrase("Plan", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(smartbachatbean.getPlantype() + "",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);
*/
           /* cell = new PdfPCell(new Phrase("Base Sum Assured (in Rs.)",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(
                    new Phrase(currencyFormat.format(smartbachatbean
                            .getSumAssured()) + "", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);*/


          /*  String adtpdbenfits_str = "";

            if (plan.equalsIgnoreCase("Option A (Endowment Option)")) {
                adtpdbenfits_str = "0";
            } else {
                adtpdbenfits_str = et_adtpd_benifit.getText().toString();
            }

            cell = new PdfPCell(new Phrase("Accidental Death and Disability Benefit  Sum Assured (in Rs.)",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(
                    new Phrase(currencyFormat.format(Double.parseDouble(adtpdbenfits_str)) + "", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);*/


         /*   cell = new PdfPCell(new Phrase("Staff/Non-Staff ", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);

            if (smartbachatbean.getIsForStaffOrNot())
                cell = new PdfPCell(new Phrase("Staff", small_normal));
            else
                cell = new PdfPCell(new Phrase("Non-Staff", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);
*/

            cell = new PdfPCell(new Phrase("", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);

            document.add(personalDetail_table);
            document.add(para_img_logo_after_space_1);

            PdfPTable BI_Pdftable_BI_details = new PdfPTable(1);
            BI_Pdftable_BI_details.setWidthPercentage(100);
            PdfPCell BI_Pdftable_BI_details_cell1 = new PdfPCell(new Paragraph(
                    "How to read and understand this benefit illustration?", small_bold));
            BI_Pdftable_BI_details_cell1
                    .setHorizontalAlignment(Element.ALIGN_LEFT);
            BI_Pdftable_BI_details_cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_Pdftable_BI_details_cell1.setPadding(5);

            BI_Pdftable_BI_details.addCell(BI_Pdftable_BI_details_cell1);
            document.add(BI_Pdftable_BI_details);

            PdfPTable BI_Pdftable_BI_details1 = new PdfPTable(1);
            BI_Pdftable_BI_details1.setWidthPercentage(100);
            PdfPCell BI_Pdftable_BI_details_cell2 = new PdfPCell(
                    new Paragraph(
                            "This benefit illustration is intended to show year-wise premiums payable and benefits under the policy, at two assumed rates of interest i.e, 8% p.a and 4% p.a.",
                            small_normal));
            BI_Pdftable_BI_details_cell2.setPadding(5);
            BI_Pdftable_BI_details1.addCell(BI_Pdftable_BI_details_cell2);
            document.add(BI_Pdftable_BI_details1);


            PdfPTable BI_Pdftable_BI_details2 = new PdfPTable(1);
            BI_Pdftable_BI_details2.setWidthPercentage(100);
            PdfPCell BI_Pdftable_BI_details2_cell2 = new PdfPCell(
                    new Paragraph(
                            "Some benefits are guaranteed and some benefits are variable with returns based on the future performance of your insurer carrying on life insurance business. If your policy offers guaranteed benefits then these will be clearly marked “guaranteed” in the illustration table on this page. If your policy offers variable benefits then the illustrations on this page will show two different rates of assumed future investment returns, of 8% p.a. and 4% p.a. These assumed rates of return are not guaranteed and they are not the upper or lower limits of what you might get back, as the value of your policy is dependent on a number of factors including future investment performance.",
                            small_normal));
            BI_Pdftable_BI_details2_cell2.setPadding(5);
            BI_Pdftable_BI_details2.addCell(BI_Pdftable_BI_details2_cell2);
            document.add(BI_Pdftable_BI_details2);


            document.add(para_img_logo_after_space_1);

            PdfPTable BI_Pdftable_Policy_details = new PdfPTable(1);
            BI_Pdftable_Policy_details.setWidthPercentage(100);
            PdfPCell BI_Pdftable_Policy_details_cell1 = new PdfPCell(new Paragraph(
                    "Policy Details", small_bold));
            BI_Pdftable_Policy_details_cell1
                    .setHorizontalAlignment(Element.ALIGN_LEFT);
            BI_Pdftable_Policy_details_cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_Pdftable_Policy_details_cell1.setPadding(5);

            BI_Pdftable_Policy_details.addCell(BI_Pdftable_Policy_details_cell1);
            document.add(BI_Pdftable_Policy_details);

            PdfPTable table_Policy_options = new PdfPTable(2);
            // float[] columnWidths_table_Policy_options = { 2f, 2f, 1f, 2f };
            // table_Policy_options.setWidths(columnWidths_table_Policy_options);
            table_Policy_options.setWidthPercentage(100);

            PdfPCell Policy_options_cell_1 = new PdfPCell(new Paragraph(
                    "Policy Option", small_normal));
            PdfPCell Policy_options_cell_2 = new PdfPCell(new Paragraph(
                    smartbachatbean.getPlantype(), small_bold1));
            Policy_options_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);

            Policy_options_cell_1.setPadding(5);
            Policy_options_cell_2.setPadding(5);

            table_Policy_options.addCell(Policy_options_cell_1);
            table_Policy_options.addCell(Policy_options_cell_2);
            document.add(table_Policy_options);


            PdfPTable personalDetail_tablesecond = new PdfPTable(2);
            personalDetail_tablesecond.setWidths(new float[]{5f, 5f,});
            personalDetail_tablesecond.setWidthPercentage(100f);

            personalDetail_tablesecond.setHorizontalAlignment(Element.ALIGN_LEFT);

          /*  cell = new PdfPCell(new Phrase("Premium Payment Option", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_tablesecond.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    "Limited"+ "", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_tablesecond.addCell(cell);*/

            cell = new PdfPCell(new Phrase("Policy Term (Years)", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_tablesecond.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    smartbachatbean.getPolicyterm() + "", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_tablesecond.addCell(cell);

            cell = new PdfPCell(new Phrase("Premium Payment Term (years)",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_tablesecond.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    smartbachatbean.getPremiumpayingterm() + "", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_tablesecond.addCell(cell);

            cell = new PdfPCell(new Phrase("Mode / Frequency of Premium Payment",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_tablesecond.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    smartbachatbean.getPremiumFrequency() + "", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_tablesecond.addCell(cell);


            cell = new PdfPCell(new Phrase("Amount of Installment Premium(Rs.)",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_tablesecond.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    " Rs."
                            + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf(installmntPrem
                            .equals("") ? "0" : installmntPrem))))), small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_tablesecond.addCell(cell);

            document.add(personalDetail_tablesecond);

            PdfPTable table_bonus_type = new PdfPTable(2);
            // float[] columnWidths_table_bonus_type = { 2f, 2f, 1f, 2f };
            // table_bonus_type.setWidths(columnWidths_table_bonus_type);
            table_bonus_type.setWidthPercentage(100);

            PdfPCell bonus_type_cell_1 = new PdfPCell(new Paragraph(
                    "Bonus Type", small_normal));
            PdfPCell bonus_type_cell_2 = new PdfPCell(new Paragraph(
                    bonusOption, small_bold1));
            bonus_type_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);

            bonus_type_cell_1.setPadding(5);
            bonus_type_cell_2.setPadding(5);

            table_bonus_type.addCell(bonus_type_cell_1);
            table_bonus_type.addCell(bonus_type_cell_2);
            document.add(table_bonus_type);

            PdfPTable table_Sum_assured = new PdfPTable(2);
            // float[] columnWidths_table_Sum_assured = { 2f, 2f, 1f, 2f };
            // table_Sum_assured.setWidths(columnWidths_table_Sum_assured);
            table_Sum_assured.setWidthPercentage(100);

            PdfPCell Sum_assured_cell_1 = new PdfPCell(new Paragraph(
                    "Sum Assured (Rs.)", small_normal));
            PdfPCell Sum_assured_cell_2 = new PdfPCell(new Paragraph(
                    "" + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                            .valueOf(SumAssuredBasic.equals("") ? "0"
                                    : SumAssuredBasic))))) + "", small_bold1));
            Sum_assured_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);

            Sum_assured_cell_1.setPadding(5);
            Sum_assured_cell_2.setPadding(5);

            table_Sum_assured.addCell(Sum_assured_cell_1);
            table_Sum_assured.addCell(Sum_assured_cell_2);
            document.add(table_Sum_assured);


            PdfPTable table_Sum_assured_death = new PdfPTable(2);
            // float[] columnWidths_table_Sum_assured_death = { 2f, 2f, 1f, 2f };
            // table_Sum_assured_death.setWidths(columnWidths_table_Sum_assured_death);
            table_Sum_assured_death.setWidthPercentage(100);

            PdfPCell Sum_assured_death_cell_1 = new PdfPCell(new Paragraph(
                    "Sum Assured on Death (at inception of the policy)(Rs.)", small_normal));
            PdfPCell Sum_assured_death_cell_2 = new PdfPCell(new Paragraph(
                    Smart_bachat_sum_assured_on_death + "", small_bold1));
            Sum_assured_death_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);

            Sum_assured_death_cell_1.setPadding(5);
            Sum_assured_death_cell_2.setPadding(5);

            table_Sum_assured_death.addCell(Sum_assured_death_cell_1);
            table_Sum_assured_death.addCell(Sum_assured_death_cell_2);
            document.add(table_Sum_assured_death);


            PdfPTable table_Sum_assured_ad_tpd = new PdfPTable(2);
            // float[] columnWidths_table_Sum_assured_ad_tpd = { 2f, 2f, 1f, 2f };
            // table_Sum_assured_ad_tpd.setWidths(columnWidths_table_Sum_assured_ad_tpd);
            table_Sum_assured_ad_tpd.setWidthPercentage(100);

            PdfPCell Sum_assured_ad_tpd_cell_1 = new PdfPCell(new Paragraph(
                    "", small_normal));
            PdfPCell Sum_assured_ad_tpd_cell_2 = new PdfPCell(new Paragraph(
                    "", small_bold1));
            Sum_assured_ad_tpd_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell Sum_assured_ad_tpd_cell_11 = new PdfPCell(new Paragraph(
                    "Rate of Applicable Taxes (GST)", small_normal));
            PdfPCell Sum_assured_ad_tpd_cell_22;
            if (cb_kerladisc.isChecked()) {

                Sum_assured_ad_tpd_cell_22 = new PdfPCell(new Paragraph(
                        "4.75% in the 1st policy year and 2.375% from 2nd policy year onwards", small_bold1));
                Sum_assured_ad_tpd_cell_22.setHorizontalAlignment(Element.ALIGN_CENTER);


            } else {
                Sum_assured_ad_tpd_cell_22 = new PdfPCell(new Paragraph(
                        "4.5% in the 1st policy year and 2.25% from 2nd policy year onwards", small_bold1));
                Sum_assured_ad_tpd_cell_22.setHorizontalAlignment(Element.ALIGN_CENTER);
            }


            Sum_assured_ad_tpd_cell_1.setPadding(5);
            Sum_assured_ad_tpd_cell_2.setPadding(5);

            table_Sum_assured_ad_tpd.addCell(Sum_assured_ad_tpd_cell_1);
            table_Sum_assured_ad_tpd.addCell(Sum_assured_ad_tpd_cell_2);
            table_Sum_assured_ad_tpd.addCell(Sum_assured_ad_tpd_cell_11);
            table_Sum_assured_ad_tpd.addCell(Sum_assured_ad_tpd_cell_22);
            document.add(table_Sum_assured_ad_tpd);

            document.add(para_img_logo_after_space_1);

            PdfPTable BI_Pdftable_Premium_summary = new PdfPTable(1);
            BI_Pdftable_Premium_summary.setWidthPercentage(100);
            PdfPCell BI_Pdftable_Premium_summary_cell1 = new PdfPCell(new Paragraph(
                    "Premium Summary", small_bold));
            BI_Pdftable_Premium_summary_cell1
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_Pdftable_Premium_summary_cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_Pdftable_Premium_summary_cell1.setPadding(5);

            BI_Pdftable_Premium_summary.addCell(BI_Pdftable_Premium_summary_cell1);
            document.add(BI_Pdftable_Premium_summary);


            PdfPTable BI_Pdftable_Premium_summary_table = new PdfPTable(4);
            BI_Pdftable_Premium_summary_table.setWidthPercentage(100);
            PdfPCell BI_Pdftable_Premium_summary_table_cell1 = new PdfPCell(new Paragraph(
                    "", small_bold));

            PdfPCell BI_Pdftable_Premium_summary_table_cell2 = new PdfPCell(new Paragraph(
                    "Base Plan (Rs.)", small_bold));

            PdfPCell BI_Pdftable_Premium_summary_table_cell4 = new PdfPCell(new Paragraph(
                    "Riders (Rs.)", small_bold));
            PdfPCell BI_Pdftable_Premium_summary_table_cell5 = new PdfPCell(new Paragraph(
                    "Total Installment Premium (Rs.)", small_bold));
            BI_Pdftable_Premium_summary_table_cell1
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_Pdftable_Premium_summary_table_cell2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            BI_Pdftable_Premium_summary_table_cell4
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_Pdftable_Premium_summary_table_cell5
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_Pdftable_Premium_summary_table_cell1.setPadding(5);
            BI_Pdftable_Premium_summary_table_cell2.setPadding(5);
            BI_Pdftable_Premium_summary_table_cell4.setPadding(5);
            BI_Pdftable_Premium_summary_table_cell5.setPadding(5);

            BI_Pdftable_Premium_summary_table.addCell(BI_Pdftable_Premium_summary_table_cell1);
            BI_Pdftable_Premium_summary_table.addCell(BI_Pdftable_Premium_summary_table_cell2);
            BI_Pdftable_Premium_summary_table.addCell(BI_Pdftable_Premium_summary_table_cell4);
            BI_Pdftable_Premium_summary_table.addCell(BI_Pdftable_Premium_summary_table_cell5);
            document.add(BI_Pdftable_Premium_summary_table);


            PdfPTable BI_Pdftable_Premium_summary_table1 = new PdfPTable(4);
            BI_Pdftable_Premium_summary_table1.setWidthPercentage(100);
            PdfPCell BI_Pdftable_Premium_summary_table1_cell1 = new PdfPCell(new Paragraph(
                    "Installment Premium without Applicable Taxes (Rs.)", small_bold));

            PdfPCell BI_Pdftable_Premium_summary_table1_cell2 = new PdfPCell(new Paragraph(currencyFormat.format(Double.parseDouble(obj.getRound(obj.getStringWithout_E(Double.valueOf(installmntPrem
                    .equals("") ? "0" : installmntPrem))))), small_normal));
            PdfPCell BI_Pdftable_Premium_summary_table1_cell3;


            PdfPCell BI_Pdftable_Premium_summary_table1_cell4 = new PdfPCell(new Paragraph(
                    "Not Applicable", small_normal));


            PdfPCell BI_Pdftable_Premium_summary_table1_cell5 = new PdfPCell(new Paragraph(
                    currencyFormat.format(Double.parseDouble(obj.getRound(obj.getStringWithout_E(Double.valueOf(installmntPrem
                            .equals("") ? "0" : installmntPrem))))), small_normal));
            BI_Pdftable_Premium_summary_table1_cell1
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_Pdftable_Premium_summary_table1_cell2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            BI_Pdftable_Premium_summary_table1_cell4
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_Pdftable_Premium_summary_table1_cell5
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_Pdftable_Premium_summary_table1_cell1.setPadding(5);
            BI_Pdftable_Premium_summary_table1_cell2.setPadding(5);
            BI_Pdftable_Premium_summary_table1_cell4.setPadding(5);
            BI_Pdftable_Premium_summary_table1_cell5.setPadding(5);

            BI_Pdftable_Premium_summary_table1.addCell(BI_Pdftable_Premium_summary_table1_cell1);
            BI_Pdftable_Premium_summary_table1.addCell(BI_Pdftable_Premium_summary_table1_cell2);
            BI_Pdftable_Premium_summary_table1.addCell(BI_Pdftable_Premium_summary_table1_cell4);
            BI_Pdftable_Premium_summary_table1.addCell(BI_Pdftable_Premium_summary_table1_cell5);
            document.add(BI_Pdftable_Premium_summary_table1);


            PdfPTable BI_Pdftable_Premium_summary_table2 = new PdfPTable(4);
            BI_Pdftable_Premium_summary_table2.setWidthPercentage(100);
            PdfPCell BI_Pdftable_Premium_summary_table2_cell1 = new PdfPCell(new Paragraph(
                    "Installment Premium with Applicable Taxes: for 1st Year (Rs.)", small_bold));

            PdfPCell BI_Pdftable_Premium_summary_table2_cell2 = new PdfPCell(new Paragraph(
                    currencyFormat.format(Double
                            .parseDouble(obj.getRound(obj.getStringWithout_E(Double.valueOf(BasicInstallmentPremFirstYear
                                    .equals("") ? "0" : BasicInstallmentPremFirstYear))))), small_normal));


            PdfPCell BI_Pdftable_Premium_summary_table2_cell4 = new PdfPCell(new Paragraph(
                    "Not Applicable", small_normal));


            PdfPCell BI_Pdftable_Premium_summary_table2_cell5 = new PdfPCell(new Paragraph(
                    currencyFormat.format(Double
                            .parseDouble(obj.getRound(obj.getStringWithout_E(Double.valueOf(BasicInstallmentPremFirstYear
                                    .equals("") ? "0" : BasicInstallmentPremFirstYear))))), small_normal));
            BI_Pdftable_Premium_summary_table2_cell1
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_Pdftable_Premium_summary_table2_cell2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            BI_Pdftable_Premium_summary_table2_cell4
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_Pdftable_Premium_summary_table2_cell5
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_Pdftable_Premium_summary_table2_cell1.setPadding(5);
            BI_Pdftable_Premium_summary_table2_cell2.setPadding(5);
            BI_Pdftable_Premium_summary_table2_cell4.setPadding(5);
            BI_Pdftable_Premium_summary_table2_cell5.setPadding(5);

            BI_Pdftable_Premium_summary_table2.addCell(BI_Pdftable_Premium_summary_table2_cell1);
            BI_Pdftable_Premium_summary_table2.addCell(BI_Pdftable_Premium_summary_table2_cell2);
            BI_Pdftable_Premium_summary_table2.addCell(BI_Pdftable_Premium_summary_table2_cell4);
            BI_Pdftable_Premium_summary_table2.addCell(BI_Pdftable_Premium_summary_table2_cell5);
            document.add(BI_Pdftable_Premium_summary_table2);


            PdfPTable BI_Pdftable_Premium_summary_table3 = new PdfPTable(4);
            BI_Pdftable_Premium_summary_table3.setWidthPercentage(100);
            PdfPCell BI_Pdftable_Premium_summary_table3_cell1 = new PdfPCell(new Paragraph(
                    "Installment Premium with Applicable Taxes: for 2nd Year Onwards (Rs.)", small_bold));

            PdfPCell BI_Pdftable_Premium_summary_table3_cell2 = new PdfPCell(new Paragraph(
                    currencyFormat.format(Double
                            .parseDouble(obj.getRound(obj.getStringWithout_E(Double.valueOf(BasicInstallmentPremSecondYear
                                    .equals("") ? "0" : BasicInstallmentPremSecondYear))))), small_normal));


            PdfPCell BI_Pdftable_Premium_summary_table3_cell4 = new PdfPCell(new Paragraph(
                    "Not Applicable", small_normal));


            PdfPCell BI_Pdftable_Premium_summary_table3_cell5 = new PdfPCell(new Paragraph(
                    currencyFormat.format(Double.parseDouble(obj.getRound(obj.getStringWithout_E(Double.valueOf(BasicInstallmentPremSecondYear
                            .equals("") ? "0" : BasicInstallmentPremSecondYear))))), small_normal));
            BI_Pdftable_Premium_summary_table3_cell1
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_Pdftable_Premium_summary_table3_cell2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            BI_Pdftable_Premium_summary_table3_cell4
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_Pdftable_Premium_summary_table3_cell5
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_Pdftable_Premium_summary_table3_cell1.setPadding(5);
            BI_Pdftable_Premium_summary_table3_cell2.setPadding(5);
            BI_Pdftable_Premium_summary_table3_cell4.setPadding(5);
            BI_Pdftable_Premium_summary_table3_cell5.setPadding(5);

            BI_Pdftable_Premium_summary_table3.addCell(BI_Pdftable_Premium_summary_table3_cell1);
            BI_Pdftable_Premium_summary_table3.addCell(BI_Pdftable_Premium_summary_table3_cell2);
            BI_Pdftable_Premium_summary_table3.addCell(BI_Pdftable_Premium_summary_table3_cell4);
            BI_Pdftable_Premium_summary_table3.addCell(BI_Pdftable_Premium_summary_table3_cell5);
            document.add(BI_Pdftable_Premium_summary_table3);


            document.add(para_img_logo_after_space_1);
            PdfPTable BI_Pdftable_Notes = new PdfPTable(1);
            BI_Pdftable_Notes.setWidthPercentage(100);
            PdfPCell BI_Pdftable_Notes_cell1 = new PdfPCell(new Paragraph(
                    "Please Note:", small_bold));
            BI_Pdftable_Notes_cell1
                    .setHorizontalAlignment(Element.ALIGN_LEFT);
            BI_Pdftable_Notes_cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_Pdftable_Notes_cell1.setPadding(5);

            BI_Pdftable_Notes.addCell(BI_Pdftable_Notes_cell1);
            document.add(BI_Pdftable_Notes);

            PdfPTable BI_Pdftable_Notes1 = new PdfPTable(1);
            BI_Pdftable_Notes1.setWidthPercentage(100);
            PdfPCell BI_Pdftable_Notes_cell2 = new PdfPCell(
                    new Paragraph(
                            "1. The premiums can be paid by giving standing instruction to your bank or you can pay through your credit card.",
                            small_normal));
            BI_Pdftable_Notes_cell2.setPadding(5);
            BI_Pdftable_Notes1.addCell(BI_Pdftable_Notes_cell2);
            document.add(BI_Pdftable_Notes1);

            PdfPTable BI_Pdftable_Notes2 = new PdfPTable(1);
            BI_Pdftable_Notes2.setWidthPercentage(100);
            PdfPCell BI_Pdftable_Notes2_cell2 = new PdfPCell(
                    new Paragraph(
                            "2. Applicable Taxes (including surcharge/cess etc), at the rate notified by the Central Government/ State Government / Union Territories of India from time to time and as per the provisions of the prevalent tax laws will be payable on premium as per the product features.",
                            small_normal));
            BI_Pdftable_Notes2_cell2.setPadding(5);
            BI_Pdftable_Notes2.addCell(BI_Pdftable_Notes2_cell2);
            document.add(BI_Pdftable_Notes2);

            PdfPTable BI_Pdftable_Notes3 = new PdfPTable(1);
            BI_Pdftable_Notes3.setWidthPercentage(100);
            PdfPCell BI_Pdftable_Notes3_cell2 = new PdfPCell(
                    new Paragraph(
                            "3. Tax deduction under Section 80 C is available. However in case the premium paid during the financial year, exceeds 10% of the sum assured, the benefit will be limited up to 10% of the sum assured. Tax exemption under Section 10(10D) is available at the time of maturity/surrender, subject to the premium not exceeding 10% of the sum assured in any of the years during the term of the policy. However, death proceeds are completely exempt."
                            ,
                            small_normal));
            BI_Pdftable_Notes3_cell2.setPadding(5);
            BI_Pdftable_Notes3.addCell(BI_Pdftable_Notes3_cell2);
            // document.add(BI_Pdftable_Notes3);

            PdfPTable BI_Pdftable_Notes4 = new PdfPTable(1);
            BI_Pdftable_Notes4.setWidthPercentage(100);
            PdfPCell BI_Pdftable_Notes4_cell2 = new PdfPCell(
                    new Paragraph(
                            "3. In case of Option B - If accidental total and Permanent Disablity claim is paid, then future premiums would be waived off for the rest of premium payment term."
                            ,
                            small_normal));
            BI_Pdftable_Notes4_cell2.setPadding(5);
            BI_Pdftable_Notes4.addCell(BI_Pdftable_Notes4_cell2);
            //document.add(BI_Pdftable_Notes4);
            document.add(para_img_logo_after_space_1);


//            if (smartbachatbean.getPlantype().equals(
//                    "Option A (Endowment Option)")) {
                /*PdfPTable Table_life_assured_name = new PdfPTable(2);
                Table_life_assured_name.setWidthPercentage(100);

                PdfPCell cell_life_assured1 = new PdfPCell(new Paragraph(
                        "Name", small_normal));
                cell_life_assured1.setPadding(5);
                // cell_life_assured1.setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell cell_life_assured2 = new PdfPCell(new Paragraph(
                        name_of_life_assured, small_bold));
                cell_life_assured2.setPadding(5);
                // cell_life_assured2.setHorizontalAlignment(Element.ALIGN_CENTER);
                Table_life_assured_name.addCell(cell_life_assured1);
                Table_life_assured_name.addCell(cell_life_assured2);
                document.add(Table_life_assured_name);
                document.add(para_img_logo_after_space_1);*/
            //}

            // Basic Cover


           /* if (smartbachatbean.getIsJammuResident() == true) {
                PdfPTable Table_jk = new PdfPTable(2);
                Table_jk.setWidthPercentage(100);

                PdfPCell cell_jk1 = new PdfPCell(new Paragraph(
                        "IsJ&K Resident", small_normal));
                cell_jk1.setPadding(5);
                // cell_jk1.setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell cell_jk2 = new PdfPCell(new Paragraph("Yes",
                        small_normal));
                cell_jk2.setPadding(5);
                // cell_jk2.setHorizontalAlignment(Element.ALIGN_CENTER);
                Table_jk.addCell(cell_jk1);
                Table_jk.addCell(cell_jk2);
                document.add(Table_jk);

            }
*/
       /*     if (proposer_Backdating_WishToBackDate_Policy.equalsIgnoreCase("y")) {
                PdfPTable Table_backdating_premium_with_service_tax = new PdfPTable(
                        2);
                Table_backdating_premium_with_service_tax
                        .setWidthPercentage(100);

                PdfPCell cell_Backdate1 = new PdfPCell(new Paragraph(
                        "Backdating Interest", small_normal));
                cell_Backdate1.setPadding(5);
                // cell_Backdate1.setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell cell_Backdate2 = new PdfPCell(new Paragraph("Rs. "
                        + obj.getRound(obj.getStringWithout_E(Double
                        .valueOf(BackdatingInt.equals("") ? "0"
                                : BackdatingInt))), small_normal));

                cell_Backdate2.setPadding(5);
                // cell_Backdate2.setHorizontalAlignment(Element.ALIGN_CENTER);
                Table_backdating_premium_with_service_tax
                        .addCell(cell_Backdate1);
                Table_backdating_premium_with_service_tax
                        .addCell(cell_Backdate2);
                document.add(Table_backdating_premium_with_service_tax);

            }*/

          /*  document.add(para_img_logo_after_space_1);
            PdfPTable premiumDetail_table = new PdfPTable(4);
            premiumDetail_table.setWidths(new float[]{5f, 4f, 5f, 4f});
            premiumDetail_table.setWidthPercentage(100f);
            premiumDetail_table.setHorizontalAlignment(Element.ALIGN_LEFT);

            document.add(premiumDetail_table);*/

        /*    document.add(para_img_logo_after_space_1);

            if (smartbachatbean.getPlantype().equals(
                    "Option A (Endowment Option)")) {

                PdfPTable basicCover_table = new PdfPTable(3);
                basicCover_table.setWidths(new float[]{10f, 6f, 6f});
                basicCover_table.setWidthPercentage(100f);
                basicCover_table.setHorizontalAlignment(Element.ALIGN_LEFT);

                // 1st row
                cell = new PdfPCell(new Phrase("Premium for "
                        + smartbachatbean.getPlantype(), small_bold));
                cell.setColspan(3);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell.setBorder(Rectangle.BOTTOM | Rectangle.TOP
                        | Rectangle.LEFT | Rectangle.RIGHT);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(5);
                basicCover_table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "Policy Years", small_bold));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(5);
                basicCover_table.addCell(cell);

                cell = new PdfPCell(new Phrase("First Year", small_bold));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(5);
                basicCover_table.addCell(cell);

                cell = new PdfPCell(new Phrase("Second Year Onwards",
                        small_bold));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(5);
                basicCover_table.addCell(cell);

                cell = new PdfPCell(new Phrase(""
                        + smartbachatbean.getPremiumFrequency() + " Premium (Rs.)",
                        small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPadding(5);
                basicCover_table.addCell(cell);

                // 2 row
                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(modalprem)) + "", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPadding(5);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                basicCover_table.addCell(cell);

                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(modalprem)) + "", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPadding(5);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                basicCover_table.addCell(cell);

                // cell = new PdfPCell(new Phrase("(a) Basic GST",
                // small_normal));
                // cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                // cell.setPadding(5);
                // basicCover_table.addCell(cell);
                //
                // // 2 row
                // cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                // .parseDouble(basicServiceTax)) + "", small_normal));
                // cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                // cell.setPadding(5);
                // cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                // basicCover_table.addCell(cell);
                //
                // cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                // .parseDouble(basicServiceTaxSecondYear)) + "",
                // small_normal));
                // cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                // cell.setPadding(5);
                // cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                // basicCover_table.addCell(cell);

                // cell = new PdfPCell(new Phrase(
                // "(b) Swachh Bharat Cess (SBC) (Rs.)", small_normal));
                // cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                // cell.setPadding(5);
                // basicCover_table.addCell(cell);
                //
                // // 2 row
                // cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                // .parseDouble(SBCServiceTax)) + "", small_normal));
                // cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                // cell.setPadding(5);
                // cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                // basicCover_table.addCell(cell);
                //
                // cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                // .parseDouble(SBCServiceTaxSecondYear)) + "",
                // small_normal));
                // cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                // cell.setPadding(5);
                // cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                // basicCover_table.addCell(cell);
                //
                // cell = new PdfPCell(new Phrase(
                // "(c) Krishi Kalyan Cess (KKC) (Rs.)", small_normal));
                // cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                // cell.setPadding(5);
                // basicCover_table.addCell(cell);
                //
                // // 2 row
                //
                // cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                // .parseDouble(KKCServiceTax)) + "", small_normal));
                //
                // // else
                // // cell = new PdfPCell(new Phrase(0+"",small_normal));
                // cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                // cell.setPadding(5);
                // cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                // basicCover_table.addCell(cell);
                //
                // // if(cessFirstYear!=null)
                // cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                // .parseDouble(KKCServiceTaxSecondYear)) + "",
                // small_normal));
                // // else
                // // cell = new PdfPCell(new Phrase(0+"",small_normal));
                // cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                // cell.setPadding(5);
                // cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                // basicCover_table.addCell(cell);

                cell = new PdfPCell(new Phrase("Applicable taxes (Rs.)", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPadding(5);
                basicCover_table.addCell(cell);

                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(servTax)) + "", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPadding(5);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                basicCover_table.addCell(cell);

                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(servTaxSeondYear)) + "", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPadding(5);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                basicCover_table.addCell(cell);

                cell = new PdfPCell(new Phrase(""
                        + smartbachatbean.getPremiumFrequency()
                        + " premium with Applicable taxes (Rs.)", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPadding(5);
                basicCover_table.addCell(cell);

                // 2 row
                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(installmntPremWithSerTx)) + "",
                        small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPadding(5);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                basicCover_table.addCell(cell);

                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(installmntPremWithSerTxSecondYear)) + "",
                        small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPadding(5);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                basicCover_table.addCell(cell);

                document.add(basicCover_table);

                document.add(para_img_logo_after_space_1);

            } else {

                PdfPTable basicCover_table = new PdfPTable(3);
                basicCover_table.setWidths(new float[]{10f, 6f, 6f});
                basicCover_table.setWidthPercentage(100f);
                basicCover_table.setHorizontalAlignment(Element.ALIGN_LEFT);

                // 1st row
                cell = new PdfPCell(new Phrase("Premium for "
                        + smartbachatbean.getPlantype(), small_bold));
                cell.setColspan(3);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell.setBorder(Rectangle.BOTTOM | Rectangle.TOP
                        | Rectangle.LEFT | Rectangle.RIGHT);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(5);
                basicCover_table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "Policy Years", small_bold));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(5);
                basicCover_table.addCell(cell);

                cell = new PdfPCell(new Phrase("First Year", small_bold));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(5);
                basicCover_table.addCell(cell);

                cell = new PdfPCell(new Phrase("Second Year Onwards",
                        small_bold));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPadding(5);
                basicCover_table.addCell(cell);

                cell = new PdfPCell(new Phrase(""
                        + smartbachatbean.getPremiumFrequency()
                        + " premium for base cover(in Rs.)", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPadding(5);
                basicCover_table.addCell(cell);

                // 2 row
                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(basicPrem)) + "", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPadding(5);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                basicCover_table.addCell(cell);

                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(basicPrem)) + "", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPadding(5);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                basicCover_table.addCell(cell);

                cell = new PdfPCell(
                        new Phrase(
                                ""
                                        + smartbachatbean.getPremiumFrequency()
                                        + " premium for Accidental Death and Disability benifit(in Rs.)",
                                small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPadding(5);
                basicCover_table.addCell(cell);

                // 2 row
                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(adtpdRiderprem)) + "", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPadding(5);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                basicCover_table.addCell(cell);

                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(adtpdRiderprem)) + "", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPadding(5);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                basicCover_table.addCell(cell);

                cell = new PdfPCell(new Phrase("Total "
                        + smartbachatbean.getPremiumFrequency()
                        + " premium excluding Applicable taxes (in Rs.)", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPadding(5);
                basicCover_table.addCell(cell);

                // 2 row
                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(modalprem)) + "", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPadding(5);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                basicCover_table.addCell(cell);

                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(modalprem)) + "", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPadding(5);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                basicCover_table.addCell(cell);

                // cell = new PdfPCell(new Phrase("(a) Basic GST",
                // small_normal));
                // cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                // cell.setPadding(5);
                // basicCover_table.addCell(cell);
                //
                // // 2 row
                // cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                // .parseDouble(basicServiceTax)) + "", small_normal));
                // cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                // cell.setPadding(5);
                // cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                // basicCover_table.addCell(cell);
                //
                // cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                // .parseDouble(basicServiceTaxSecondYear)) + "",
                // small_normal));
                // cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                // cell.setPadding(5);
                // cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                // basicCover_table.addCell(cell);
                //
                // cell = new PdfPCell(new Phrase(
                // "(b) Swachh Bharat Cess (SBC) (Rs.)", small_normal));
                // cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                // cell.setPadding(5);
                // basicCover_table.addCell(cell);
                //
                // // 2 row
                // cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                // .parseDouble(SBCServiceTax)) + "", small_normal));
                // cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                // cell.setPadding(5);
                // cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                // basicCover_table.addCell(cell);
                //
                // cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                // .parseDouble(SBCServiceTaxSecondYear)) + "",
                // small_normal));
                // cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                // cell.setPadding(5);
                // cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                // basicCover_table.addCell(cell);
                //
                // cell = new PdfPCell(new Phrase(
                // "(c) Krishi Kalyan Cess (KKC) (Rs.)", small_normal));
                // cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                // cell.setPadding(5);
                // basicCover_table.addCell(cell);
                //
                // // 2 row
                //
                // cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                // .parseDouble(KKCServiceTax)) + "", small_normal));
                //
                // // else
                // // cell = new PdfPCell(new Phrase(0+"",small_normal));
                // cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                // cell.setPadding(5);
                // cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                // basicCover_table.addCell(cell);
                //
                // // if(cessFirstYear!=null)
                // cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                // .parseDouble(KKCServiceTaxSecondYear)) + "",
                // small_normal));
                // // else
                // // cell = new PdfPCell(new Phrase(0+"",small_normal));
                // cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                // cell.setPadding(5);
                // cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                // basicCover_table.addCell(cell);

                cell = new PdfPCell(new Phrase("Applicable taxes (Rs.)", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPadding(5);
                basicCover_table.addCell(cell);

                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(servTax)) + "", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPadding(5);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                basicCover_table.addCell(cell);

                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(servTaxSeondYear)) + "", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPadding(5);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                basicCover_table.addCell(cell);

                cell = new PdfPCell(new Phrase(""
                        + smartbachatbean.getPremiumFrequency()
                        + " premium with Applicable taxes (in Rs.)", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPadding(5);
                basicCover_table.addCell(cell);

                // 2 row
                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(installmntPremWithSerTx)) + "",
                        small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPadding(5);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                basicCover_table.addCell(cell);

                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(installmntPremWithSerTxSecondYear)) + "",
                        small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPadding(5);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                basicCover_table.addCell(cell);

                document.add(basicCover_table);

                document.add(para_img_logo_after_space_1);

            }*/


            document.add(para_img_logo_after_space_1);


            PdfPTable table1 = new PdfPTable(19);
            table1.setWidths(new float[]{2f, 5f, 5f, 5f, 5f, 5f, 5f, 5f, 5f,
                    5f, 5f, 5f, 5f, 5f, 5f, 5f, 5f, 5f, 5f});
            table1.setWidthPercentage(100);

            // 1st row
            cell = new PdfPCell(
                    new Phrase(
                            "BENEFIT ILLUSTRATION FOR SBI LIFE - Smart Future Choices (Amount in Rs.)",
                            normal1_bold));
            cell.setColspan(19);
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(cell);

            // 2nd row
            cell = new PdfPCell(new Phrase("Policy Year", normal_bold));
            cell.setRowspan(3);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    "Annualized premium", normal_bold));
            cell.setRowspan(3);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("Guaranteed Benefits",
                    normal_bold));
            cell.setColspan(5);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(
                    new Phrase("Non-Guaranteed Benefits @ 4% p.a.", normal_bold));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(
                    new Phrase("Non-Guaranteed Benefits @ 8% p.a.", normal_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(4);
            table1.addCell(cell);

            // 3rd
            cell = new PdfPCell(new Phrase("Total benefits (Including Guaranteed and Non-Guaranteed benefits)", normal_bold));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    "Guaranteed additions",
                    normal_bold));
            cell.setRowspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("Survival benefit", normal_bold));
            cell.setRowspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    "Surrender Benefit",
                    normal_bold));
            cell.setRowspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("Death Benefit", normal_bold));
            cell.setRowspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("Maturity Benefit", normal_bold));
            cell.setRowspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            // 4 row
            cell = new PdfPCell(new Phrase("Reversionary bonus", normal_bold));
            cell.setRowspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("Cash bonus", normal_bold));
            cell.setRowspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("Accumulated Deferred Cash Bonus", normal_bold));
            cell.setRowspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);


            cell = new PdfPCell(new Phrase("Surrender benefit", normal_bold));
            cell.setRowspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("Reversionary bonus", normal_bold));
            cell.setRowspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("Cash bonus", normal_bold));
            cell.setRowspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("Accumulated Deferred Cash Bonus", normal_bold));
            cell.setRowspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("Surrender benefit", normal_bold));
            cell.setRowspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("Maturity Benefit", normal_bold));
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("Death Benefit", normal_bold));
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("Total Maturity benefit, incl. Terminal bonus, if any, @ 4% (7+9/10)", normal_bold));
            cell.setRowspan(1);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("Total Maturity benefit, incl. Terminal bonus, if any, @ 8% (7+13/14)", normal_bold));
            cell.setRowspan(1);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("Total Death benefit, incl. Terminal bonus, if any, @ 4% (6+9/10)", normal_bold));
            cell.setRowspan(1);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("Total Death benefit, incl. Terminal bonus, if any, @ 8% (6+13/14)", normal_bold));
            cell.setRowspan(1);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("1", normal_bold));
            cell.setRowspan(1);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("2", normal_bold));
            cell.setRowspan(1);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("3", normal_bold));
            cell.setRowspan(1);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("4", normal_bold));
            cell.setRowspan(1);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("5", normal_bold));
            cell.setRowspan(1);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("6", normal_bold));
            cell.setRowspan(1);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("7", normal_bold));
            cell.setRowspan(1);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("8", normal_bold));
            cell.setRowspan(1);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("9", normal_bold));
            cell.setRowspan(1);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("10", normal_bold));
            cell.setRowspan(1);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("11", normal_bold));
            cell.setRowspan(1);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("12", normal_bold));
            cell.setRowspan(1);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("13", normal_bold));
            cell.setRowspan(1);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("14", normal_bold));
            cell.setRowspan(1);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("15", normal_bold));
            cell.setRowspan(1);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("16", normal_bold));
            cell.setRowspan(1);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("17", normal_bold));
            cell.setRowspan(1);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("18", normal_bold));
            cell.setRowspan(1);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("19", normal_bold));
            cell.setRowspan(1);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            for (int i = 0; i < list_data.size(); i++) {

                cell = new PdfPCell(new Phrase(list_data.get(i)
                        .getEnd_of_year(), normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        currencyFormat.format(Double.parseDouble(list_data.get(
                                i).getTotal_base_premium())), normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);


                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data.get(i)
                                .getGuaranteed_additions())), normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);


                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data.get(i)
                                .getGuaranteed_survival_benefit())), normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);


                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data.get(i)
                                .getGuaranteed_surrender_value())), normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);


                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data.get(i)
                                .getGuaranteed_death_benefit())), normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);


                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data.get(i)
                                .getGuaranteed_maturity_benefit())), normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);


                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data.get(i)
                                .getNot_guaranteed_maturity_benefit_4per())),
                        normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);


                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data.get(i)
                                .getCash_bonus_4per())),
                        normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);

                /*cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell = new PdfPCell(new Phrase("0", normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);*/


                cell = new PdfPCell(new Phrase(currencyFormat.format(Double.parseDouble(list_data.get(i).getBonusOption4per())), normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase(currencyFormat.format(Double.parseDouble(list_data.get(i).getNot_guaranteed_surrender_value_4per())), normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);


                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data.get(i)
                                .getNot_guaranteed_maturity_benefit_8per())),
                        normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);


                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data.get(i)
                                .getCash_bonus_8per())),
                        normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);

              /*  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell = new PdfPCell(new Phrase("0", normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);*/

                cell = new PdfPCell(new Phrase(currencyFormat.format(Double.parseDouble(list_data.get(i).getBonusOption8per())), normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);


                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data.get(i)
                                .getNot_guaranteed_surrender_value_8per())),
                        normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);


                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data.get(i)
                                .getNot_guaranteed_survival_benefit_4per())),
                        normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);


                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data.get(i)
                                .getNot_guaranteed_survival_benefit_8per())),
                        normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);


                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data.get(i)
                                .getNot_guaranteed_death_benefit_4per())),
                        normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);
                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data.get(i)
                                .getNot_guaranteed_death_benefit_8per())),
                        normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);


            }

            document.add(table1);

            // Table here
           /* if (smartbachatbean
                    .getPlantype()
                    .equals("Option B (Endowment Option with in-built Accidental Death and Total Permanent Disability (AD&TPD) Benefit)")) {
                PdfPTable table1 = new PdfPTable(17);
                table1.setWidths(new float[]{5f, 5f, 5f, 5f, 5f, 5f, 5f, 5f,
                        5f, 5f, 5f,5f,5f,5f,5f,5f,5f});
                table1.setWidthPercentage(100);

                // 1st row
                cell = new PdfPCell(new Phrase(
                        "BENEFIT ILLUSTRATION FOR SBI LIFE  Smart Bachat",
                        small_bold));
                cell.setColspan(19);
                cell.setPadding(5);
                cell.setBorder(Rectangle.BOTTOM | Rectangle.LEFT
                        | Rectangle.RIGHT | Rectangle.TOP);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);

                // 2nd row
                cell = new PdfPCell(new Phrase("Policy Year", small_bold1));
                cell.setRowspan(3);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase("Annualized premium",
                        small_bold1));
                cell.setRowspan(3);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "Guaranteed Benefits", small_bold1));
                cell.setColspan(5);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "Non-Guaranteed Benefits @ 4%p.a." , small_bold1));
                cell.setColspan(3);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "Non-Guaranteed Benefits @ 8%p.a." , small_bold1));
                cell.setColspan(3);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);

                // 3rd
                cell = new PdfPCell(new Phrase("Total benefits (including Guaranteed and Non-Guaranteed benefits)", small_bold1));
                cell.setColspan(4);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase("Guaranteed additions", small_bold1));
                cell.setRowspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase("Survival benefit" , small_bold1));
                cell.setRowspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase("Surrender Benefit", small_bold1));
                cell.setRowspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);


                cell = new PdfPCell(new Phrase("Death Benefit", small_bold1));
                cell.setRowspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase("Maturity Benefit", small_bold1));
                cell.setRowspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);


                cell = new PdfPCell(new Phrase("Revisionary bonus",
                        small_bold1));
                cell.setRowspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);

                // 4 row
                cell = new PdfPCell(new Phrase("Cash bonus", small_bold1));
                cell.setRowspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase("Surrender benefit", small_bold1));
                cell.setRowspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase("Revisionary bonus", small_bold1));
                cell.setRowspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase("Cash bonus", small_bold1));
                cell.setRowspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase("Surrender benefit", small_bold1));
                cell.setRowspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase("Maturity Benefit", small_bold1));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase("Death Benefit", small_bold1));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase("Total Maturity benefit, incl.Terminal bonus, if any, @ 4% (7+8+9)", small_bold1));
                //cell.setRowspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);
                cell = new PdfPCell(new Phrase("Total Maturity benefit, incl.Terminal bonus, if any, @ 8% (7+11+12)", small_bold1));
                //cell.setRowspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);
                cell = new PdfPCell(new Phrase("Total Death benefit, incl.Terminal bonus, if any, @ 4% (6+8+9)", small_bold1));
                //cell.setRowspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);
                cell = new PdfPCell(new Phrase("Total Death benefit, incl.Terminal bonus, if any, @ 8% (6+11+12)", small_bold1));
                //cell.setRowspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);

                for (int i = 0; i < smartbachatbean.getPolicyterm(); i++) {
                    cell = new PdfPCell(new Phrase(prsObj.parseXmlTag(output,
                            "policyYr" + (i + 1)), small_normal1));
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                            | Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(cell);

                   *//* cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                            .parseDouble(prsObj.parseXmlTag(output,
                                    "TotBasePremWithTx" + (i + 1)))),
                            small_normal1));*//*
                    cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                            .parseDouble(prsObj.parseXmlTag(output,
                                    "Annualized_Premium" + (i + 1)))),
                            small_normal1));

                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                            | Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(cell);

                    cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                            .parseDouble(prsObj.parseXmlTag(output, "deathGuar"
                                    + (i + 1)))), small_normal1));
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                            | Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(cell);

                    cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                            .parseDouble(prsObj.parseXmlTag(output,
                                    "deathNonGuar4pa" + (i + 1)))),
                            small_normal1));
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                            | Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(cell);

                    cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                            .parseDouble(prsObj.parseXmlTag(output,
                                    "deathNonGuar8pa" + (i + 1)))),
                            small_normal1));
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                            | Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(cell);

                    cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                            .parseDouble(prsObj.parseXmlTag(output,
                                    "Accidentaldeathbenifit" + (i + 1)))),
                            small_normal1));
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                            | Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(cell);

                    cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                            .parseDouble(prsObj.parseXmlTag(output, "matGuar"
                                    + (i + 1)))), small_normal1));
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                            | Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(cell);

                    cell = new PdfPCell(
                            new Phrase(currencyFormat.format(Double
                                    .parseDouble(prsObj.parseXmlTag(output,
                                            "matNonGuar4pa" + (i + 1)))),
                                    small_normal1));
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                            | Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(cell);

                    cell = new PdfPCell(
                            new Phrase(currencyFormat.format(Double
                                    .parseDouble(prsObj.parseXmlTag(output,
                                            "matNonGuar8pa" + (i + 1)))),
                                    small_normal1));
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                            | Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(cell);

                    cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                            .parseDouble(prsObj.parseXmlTag(output, "surGuar"
                                    + (i + 1)))), small_normal1));
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                            | Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(cell);

                    cell = new PdfPCell(
                            new Phrase(currencyFormat.format(Double
                                    .parseDouble(prsObj.parseXmlTag(output,
                                            "surNonGuar4pa" + (i + 1)))),
                                    small_normal1));
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                            | Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(cell);

                    cell = new PdfPCell(
                            new Phrase(currencyFormat.format(Double
                                    .parseDouble(prsObj.parseXmlTag(output,
                                            "surNonGuar8pa" + (i + 1)))),
                                    small_normal1));
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                            | Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(cell);
                }

                document.add(table1);
                document.add(para_img_logo_after_space_1);
            } else {
                PdfPTable table1 = new PdfPTable(17);
                table1.setWidths(new float[]{5f, 5f, 5f, 5f, 5f, 5f, 5f, 5f,
                        5f, 5f, 5f,5f,5f,5f,5f,5f,5f});
                table1.setWidthPercentage(100);

                // 1st row
                cell = new PdfPCell(new Phrase(
                        "BENEFIT ILLUSTRATION FOR SBI LIFE  Smart Bachat",
                        small_bold));
                cell.setColspan(17);
                cell.setPadding(5);
                cell.setBorder(Rectangle.BOTTOM | Rectangle.LEFT
                        | Rectangle.RIGHT | Rectangle.TOP);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);

                // 2nd row
                cell = new PdfPCell(new Phrase("Policy Year", small_bold1));
                cell.setRowspan(3);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase("Annualized premium",
                        small_bold1));
                cell.setRowspan(3);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "Guaranteed Benefits", small_bold1));
                cell.setColspan(5);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "Non-Guaranteed Benefits @ 4%p.a." , small_bold1));
                cell.setColspan(3);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "Non-Guaranteed Benefits @ 8%p.a." , small_bold1));
                cell.setColspan(3);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);

                // 3rd
                cell = new PdfPCell(new Phrase("Total benefits (including Guaranteed and Non-Guaranteed benefits)", small_bold1));
                cell.setColspan(4);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase("Guaranteed additions", small_bold1));
                cell.setRowspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase("Survival benefit" , small_bold1));
                cell.setRowspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase("Surrender Benefit", small_bold1));
                cell.setRowspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);


                cell = new PdfPCell(new Phrase("Death Benefit", small_bold1));
                cell.setRowspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase("Maturity Benefit", small_bold1));
                cell.setRowspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);


                cell = new PdfPCell(new Phrase("Revisionary bonus",
                        small_bold1));
                cell.setRowspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);

                // 4 row
                cell = new PdfPCell(new Phrase("Cash bonus", small_bold1));
                cell.setRowspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase("Surrender benefit", small_bold1));
                cell.setRowspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase("Revisionary bonus", small_bold1));
                cell.setRowspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase("Cash bonus", small_bold1));
                cell.setRowspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase("Surrender benefit", small_bold1));
                cell.setRowspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase("Maturity Benefit", small_bold1));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase("Death Benefit", small_bold1));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase("Total Maturity benefit, incl.Terminal bonus, if any, @ 4% (7+8+9)", small_bold1));
                //cell.setRowspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);
                cell = new PdfPCell(new Phrase("Total Maturity benefit, incl.Terminal bonus, if any, @ 8% (7+11+12)", small_bold1));
                //cell.setRowspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);
                cell = new PdfPCell(new Phrase("Total Death benefit, incl.Terminal bonus, if any, @ 4% (6+8+9)", small_bold1));
                //cell.setRowspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);
                cell = new PdfPCell(new Phrase("Total Death benefit, incl.Terminal bonus, if any, @ 8% (6+11+12)", small_bold1));
                //cell.setRowspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);

                for (int i = 0; i < smartbachatbean.getPolicyterm(); i++) {
                    cell = new PdfPCell(new Phrase(prsObj.parseXmlTag(output,
                            "policyYr" + (i + 1)), small_normal1));
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                            | Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(cell);

                  *//*  cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                            .parseDouble(prsObj.parseXmlTag(output,
                                    "TotBasePremWithTx" + (i + 1)))),
                            small_normal1));*//*
                    cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                            .parseDouble(prsObj.parseXmlTag(output,
                                    "Annualized_Premium" + (i + 1)))),
                            small_normal1));

                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                            | Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(cell);

                    cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                            .parseDouble(prsObj.parseXmlTag(output, "deathGuar"
                                    + (i + 1)))), small_normal1));
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                            | Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(cell);

                    cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                            .parseDouble(prsObj.parseXmlTag(output,
                                    "deathNonGuar4pa" + (i + 1)))),
                            small_normal1));
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                            | Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(cell);

                    cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                            .parseDouble(prsObj.parseXmlTag(output,
                                    "deathNonGuar8pa" + (i + 1)))),
                            small_normal1));
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                            | Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(cell);

                    cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                            .parseDouble(prsObj.parseXmlTag(output, "matGuar"
                                    + (i + 1)))), small_normal1));
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                            | Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(cell);

                    cell = new PdfPCell(
                            new Phrase(currencyFormat.format(Double
                                    .parseDouble(prsObj.parseXmlTag(output,
                                            "matNonGuar4pa" + (i + 1)))),
                                    small_normal1));
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                            | Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(cell);

                    cell = new PdfPCell(
                            new Phrase(currencyFormat.format(Double
                                    .parseDouble(prsObj.parseXmlTag(output,
                                            "matNonGuar8pa" + (i + 1)))),
                                    small_normal1));
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                            | Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(cell);

                    cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                            .parseDouble(prsObj.parseXmlTag(output, "surGuar"
                                    + (i + 1)))), small_normal1));
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                            | Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(cell);

                    cell = new PdfPCell(
                            new Phrase(currencyFormat.format(Double
                                    .parseDouble(prsObj.parseXmlTag(output,
                                            "surNonGuar4pa" + (i + 1)))),
                                    small_normal1));
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                            | Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(cell);

                    cell = new PdfPCell(
                            new Phrase(currencyFormat.format(Double
                                    .parseDouble(prsObj.parseXmlTag(output,
                                            "surNonGuar8pa" + (i + 1)))),
                                    small_normal1));
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                            | Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(cell);
                }

                document.add(table1);
                document.add(para_img_logo_after_space_1);
            }*/

            PdfPTable BI_Pdftable_benefit_payable = new PdfPTable(1);
            BI_Pdftable_benefit_payable.setWidthPercentage(100);
            PdfPCell BI_Pdftable_benefit_payable_cell1 = new PdfPCell(
                    new Paragraph(
                            "Notes:",
                            small_bold));
            BI_Pdftable_benefit_payable_cell1.setPadding(5);
            BI_Pdftable_benefit_payable
                    .addCell(BI_Pdftable_benefit_payable_cell1);

            PdfPCell BI_Pdftable_benefit_payable_cell12 = new PdfPCell(
                    new Paragraph(
                            "1. Annualized premium shall be the premium amount payable in a year chosen by the policyholder, excluding the applicable taxes,  underwriting extra premiums and loading for modal premiums, if any. Refer sales literature for explanation of terms used in this illustration.",
                            small_normal));
            BI_Pdftable_benefit_payable_cell12.setPadding(5);
            BI_Pdftable_benefit_payable
                    .addCell(BI_Pdftable_benefit_payable_cell12);

            PdfPCell BI_Pdftable_benefit_payable_cell13 = new PdfPCell(
                    new Paragraph(
                            "2. All Benefit amount are derived on the assumption that the policies are ”in-force”",
                            small_normal));
            BI_Pdftable_benefit_payable_cell13.setPadding(5);
            BI_Pdftable_benefit_payable
                    .addCell(BI_Pdftable_benefit_payable_cell13);

            PdfPCell BI_Pdftable_benefit_payable_cell14 = new PdfPCell(
                    new Paragraph(
                            "3. The benefit calculation is based on the age herein indicated and as applicable for a healthy individual.",
                            small_normal));
            BI_Pdftable_benefit_payable_cell14.setPadding(5);
            BI_Pdftable_benefit_payable
                    .addCell(BI_Pdftable_benefit_payable_cell14);

            PdfPCell BI_Pdftable_benefit_payable_cell15 = new PdfPCell(
                    new Paragraph(
                            "4. Surrender value payable on surrender is higher of the Guaranteed Surrender Value (GSV) and Special Surrender Value (SSV). In addition to the higher of GSV (column 5) and SSV (column 12 and 14 ) Accumulated Survival Benefit, if any and Accumulated Deferred Cash Bonus, if any will also be paid.",
                            small_normal));
            BI_Pdftable_benefit_payable_cell15.setPadding(5);
            BI_Pdftable_benefit_payable
                    .addCell(BI_Pdftable_benefit_payable_cell15);


            PdfPCell BI_Pdftable_benefit_payable_cell151 = new PdfPCell(
                    new Paragraph(
                            "5. The interest rate used in the above calculation for  accumulating the cash bonus is 3% p.a. However, the actual applicable interest rate for accumulation of deferred Cash Bonus shall be the RBI Reverse Repo rate less 100 basis points as on 1st April of the Financial Year in which the accumulated amount is payable.",
                            small_normal));
            BI_Pdftable_benefit_payable_cell151.setPadding(5);
            BI_Pdftable_benefit_payable
                    .addCell(BI_Pdftable_benefit_payable_cell151);

            document.add(BI_Pdftable_benefit_payable);


            document.add(para_img_logo_after_space_1);


            PdfPTable BI_Pdftable_GuaranteedSurrenderValue = new PdfPTable(1);
            BI_Pdftable_GuaranteedSurrenderValue.setWidthPercentage(100);
            PdfPCell BI_Pdftable_GuaranteedSurrenderValue_cell1 = new PdfPCell(
                    new Paragraph("Bonus Rates", small_bold));
            BI_Pdftable_GuaranteedSurrenderValue_cell1
                    .setHorizontalAlignment(Element.ALIGN_LEFT);
            BI_Pdftable_GuaranteedSurrenderValue_cell1.setPadding(5);

            BI_Pdftable_GuaranteedSurrenderValue
                    .addCell(BI_Pdftable_GuaranteedSurrenderValue_cell1);
            document.add(BI_Pdftable_GuaranteedSurrenderValue);

            PdfPTable BI_Pdftable_RegularPremiumPolicies = new PdfPTable(1);
            BI_Pdftable_RegularPremiumPolicies.setWidthPercentage(100);
            PdfPCell BI_Pdftable_RegularPremiumPolicies_cell = new PdfPCell(
                    new Paragraph(
                            "This is a with profit plan and participates in the profits of the company’s life insurance business. Cash Bonus and Terminal bonus will be declared based on Statutory Valuation carried out at the end of every financial year. Cash Bonus, if declared, would be expressed as a percentage of Basic Sum Assured.",
                            small_normal));

            BI_Pdftable_RegularPremiumPolicies_cell.setPadding(5);

            BI_Pdftable_RegularPremiumPolicies
                    .addCell(BI_Pdftable_RegularPremiumPolicies_cell);
            document.add(BI_Pdftable_RegularPremiumPolicies);

            document.add(para_img_logo_after_space_1);

            PdfPTable BI_Pdftable_CompanysPolicySurrender = new PdfPTable(1);
            BI_Pdftable_CompanysPolicySurrender.setWidthPercentage(100);
            PdfPCell BI_Pdftable_CompanysPolicySurrender_cell1 = new PdfPCell(
                    new Paragraph("Important : ", small_bold));
            BI_Pdftable_CompanysPolicySurrender_cell1
                    .setHorizontalAlignment(Element.ALIGN_LEFT);
            BI_Pdftable_CompanysPolicySurrender_cell1.setPadding(5);

           /* BI_Pdftable_CompanysPolicySurrender
                    .addCell(BI_Pdftable_CompanysPolicySurrender_cell1);
            document.add(BI_Pdftable_CompanysPolicySurrender);

            PdfPTable BI_Pdftable_CompanysPolicySurrender1 = new PdfPTable(1);
            BI_Pdftable_CompanysPolicySurrender1.setWidthPercentage(100);
            PdfPCell BI_Pdftable_CompanysPolicySurrender1_cell = new PdfPCell(
                    new Paragraph(
                            "In practice, the company may pay a surrender value, Special Surrender Value (SSV) which could be higher than the guaranteed surrender value. The Special Surrender Value will be non-guaranteed  which is assessed based on the past financial/demographic experience of the company with regard to your policy/group of similar policies, as well as the likely future experience. The surrender value payable may be reviewed from time to time depending on company's experience of the various factors which impact the surrender values that may be paid. The surrender value payable would be higher of GSV or SSV.",
                            small_normal));

            BI_Pdftable_CompanysPolicySurrender1_cell.setPadding(5);

            BI_Pdftable_CompanysPolicySurrender1
                    .addCell(BI_Pdftable_CompanysPolicySurrender1_cell);
            document.add(BI_Pdftable_CompanysPolicySurrender1);*/
            document.add(para_img_logo_after_space_1);


            PdfPTable BI_Pdftable_CompanysPolicySurrender2 = new PdfPTable(1);
            BI_Pdftable_CompanysPolicySurrender2.setWidthPercentage(100);
            PdfPCell BI_Pdftable_CompanysPolicySurrender2_cell = new PdfPCell(
                    new Paragraph(
                            "You may receive a Welcome Call from our representative to confirm your proposal details like Date of Birth, Nominee Name, Address, Email ID, Sum Assured, Premium amount, Premium Payment Term etc.",
                            small_normal));
            PdfPCell you_may = new PdfPCell(
                    new Paragraph(
                            "You may have to undergo Medical Test based on our Underwriting Requirements.",
                            small_normal));
            PdfPCell you_may2 = new PdfPCell(
                    new Paragraph(
                            "You have to submit Proof of source of Fund",
                            small_normal));

            BI_Pdftable_CompanysPolicySurrender2_cell.setPadding(5);

            BI_Pdftable_CompanysPolicySurrender2.addCell(BI_Pdftable_CompanysPolicySurrender_cell1);
            BI_Pdftable_CompanysPolicySurrender2
                    .addCell(BI_Pdftable_CompanysPolicySurrender2_cell);
            BI_Pdftable_CompanysPolicySurrender2
                    .addCell(you_may);
            BI_Pdftable_CompanysPolicySurrender2
                    .addCell(you_may2);
            document.add(BI_Pdftable_CompanysPolicySurrender2);


            PdfPTable BI_Pdftable_CompanysPolicySurrender3 = new PdfPTable(1);
            BI_Pdftable_CompanysPolicySurrender3.setWidthPercentage(100);
            PdfPCell BI_Pdftable_CompanysPolicySurrender3_cell = new PdfPCell(
                    new Paragraph(
                            "Your SBI Life Smart Future Choices (UIN: 111N127V01) is a Limited premium policy, for which your first year "
                                    + smartbachatbean.getPremiumFrequency()
                                    + " premium is Rs. "
                                    + currencyFormat.format(Double
                                    .parseDouble(installmntPremWithSerTx))
                                    + ". Your Policy Term is "
                                    + smartbachatbean.getPolicyterm()
                                    + " years, Premium Payment Term is "
                                    + smartbachatbean.getPremiumpayingterm()
                                    + " years and Basic Sum Assured is Rs. "
                                    + currencyFormat.format(smartbachatbean
                                    .getSumAssured()), small_normal));

            BI_Pdftable_CompanysPolicySurrender3_cell.setPadding(5);

            BI_Pdftable_CompanysPolicySurrender3
                    .addCell(BI_Pdftable_CompanysPolicySurrender3_cell);
            //  document.add(BI_Pdftable_CompanysPolicySurrender3);

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
                                    + " having received the information with respect to the above, have understood the above statement before entering into the contract.",
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
            document.add(para_img_logo_after_space_1);
           /* PdfPTable BI_Pdftable_eSign = new PdfPTable(1);
            BI_Pdftable_eSign.setWidthPercentage(100);
            // CR_table6.setWidths(columnWidths_26);
            PdfPCell BI_Pdftable_eSign_cell1 = new PdfPCell(new Paragraph(

                    "This document is eSigned by " + name_of_person, small_bold));

            BI_Pdftable_eSign_cell1.setPadding(5);

            BI_Pdftable_eSign.addCell(BI_Pdftable_eSign_cell1);
            document.add(BI_Pdftable_eSign);
			document.add(para_img_logo_after_space_1);*/

            PdfPTable agentDeclarationTable = new PdfPTable(1);
            agentDeclarationTable.setWidthPercentage(100);
            PdfPCell agentDeclaration = new PdfPCell(
                    new Paragraph("I, " + commonMethods.getUserName(context)
                            + " have explained the premiums and benefits under the policy fully to the prospect/policyholder.",
                            small_bold));

            agentDeclaration.setPadding(5);
            agentDeclarationTable.addCell(agentDeclaration);
            document.add(agentDeclarationTable);
            document.add(para_img_logo_after_space_1);

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

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }


}

