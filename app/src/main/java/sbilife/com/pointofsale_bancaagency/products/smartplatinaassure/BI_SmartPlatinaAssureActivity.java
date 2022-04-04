package sbilife.com.pointofsale_bancaagency.products.smartplatinaassure;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
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
import com.itextpdf.text.FontFactory;
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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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

public class BI_SmartPlatinaAssureActivity extends AppCompatActivity implements
        OnEditorActionListener {
    private final int SIGNATURE_ACTIVITY = 1;
    private String proposer_date_of_birth = "";
    private String name_of_proposer = "";
    private String proposer_Is_Same_As_Life_Assured = "Y";
    String output = "";
    String input = "";
    String str_kerla_discount = "No";
    private int DIALOG_ID;
    private Spinner spnr_Age;
    private Button btnSubmit;
    private Button btnback;
    // for BI
    private StringBuilder inputVal;
    private String premPayingTerm = "", premPayingMode = "";
    private String policyTerm = "";
    private String ageAtEntry = "", strProposerAge;
    private String gender = "";
    private String basicCoverSumAssured = "";
    private String basicCoverYearlyPremium = "";
    private String premiumDetailFYBasicPremium = "";
    private String premiumDetailFYServiceTax = "";
    private String premiumDetailFYPremiumWithServiceTax = "";
    private String premiumDetailSYBasicPremium = "";
    private String premiumDetailSYServiceTax = "";
    private String premiumDetailSYPremiumWithServiceTax = "";
    private SmartPlatinaAssureBean smartPlatinaAssureBean;
    private EditText edt_premiumAmt;
    private Spinner spnrPremPayingTerm;
    private Spinner spnrPremPayingMode;
    private Spinner spnrPolicyTerm;
    private Spinner spnr_Gender;
    private RadioButton rb_proposerdetail_personaldetail_backdating_yes;
    private RadioButton rb_proposerdetail_personaldetail_backdating_no;
    private LinearLayout ll_backdating1;
    private Button btn_proposerdetail_personaldetail_backdatingdate;
    private Button btn_MarketingOfficalDate;
    private Button btn_PolicyholderDate;
    private ImageButton Ibtn_signatureofMarketing;
    private ImageButton Ibtn_signatureofPolicyHolders;
    private String latestImage = "";
    // List used for The Policy Detail Depend on The policy Term
    private List<M_BI_Smart_Platina_Assure> list_data;
    // Database Related Variable
    private DatabaseHelper db;
    private Button btn_bi_smart_platina_assure_life_assured_date;
    private String lifeAssured_Title = "";
    private String lifeAssured_First_Name = "";
    private String lifeAssured_Middle_Name = "";
    private String lifeAssured_Last_Name = "";
    private String name_of_life_assured = "";
    private String lifeAssured_date_of_birth = "";
    private String lifeAssuredAge = "";
    private String proposer_Backdating_WishToBackDate_Policy = "";
    private String proposer_Backdating_BackDate = "";
    private String BackDateinterest = "";
    private String BackDateinterestwithGST;
    private String servicetax_MinesOccuInterest;
    private String BackdatingInt;
    // Spinner USed
    private Spinner spnr_bi_smart_platina_assure_life_assured_title;
    // edit Text Used
    private EditText edt_bi_smart_platina_assure_life_assured_first_name;
    private EditText edt_bi_smart_platina_assure_life_assured_middle_name;
    private EditText edt_bi_smart_platina_assure_life_assured_last_name;
    private ParseXML prsObj;
    private String name_of_person = "";
    private String place2 = "";
    private String date1 = "";
    private String date2 = "";
    private String agent_sign = "";
    private String proposer_sign = "";
    private CommonForAllProd commonForAllProd;
    private SmartPlatinaAssureProperties prop;
    // Variable Declaration
    private DecimalFormat currencyFormat;
    private StringBuilder bussIll = null;
    private StringBuilder retVal = null;
    private File mypath;
    private EditText edt_proposerdetail_basicdetail_contact_no;
    private EditText edt_proposerdetail_basicdetail_Email_id;
    private EditText edt_proposerdetail_basicdetail_ConfirmEmail_id;
    private String mobileNo = "";
    private String emailId = "";
    private String ConfirmEmailId = "";
    /* Basic Details */
    private String ProposerEmailId = "";
    private Bitmap photoBitmap;
    private String flg_needAnalyis = "";
    private String basicServiceTax = "";
    private String SBCServiceTax = "";
    private String KKCServiceTax = "";
    private String basicServiceTaxSecondYear = "";
    private String SBCServiceTaxSecondYear = "";
    private String KKCServiceTaxSecondYear = "";
    private String Company_policy_surrender_dec = "";
    private CheckBox selStaffDisc;
    private String staffdiscount = "";
    private String staffStatus = "";
    private int mYear;
    private int mMonth;
    private int mDay;
    private String QuatationNumber = "";
    private String planName = "";
    private boolean validationFla1 = false;
    private Context context;
    private CommonMethods commonMethods;
    private StorageUtils mStorageUtils;
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
    private NeedAnalysisBIService NABIObj;
    private NA_CBI_bean na_cbi_bean;
    private File needAnalysispath, newFile;
    private String agentcode, agentMobile, agentEmail, userType;
    private int needAnalysis_flag = 0;
    private String na_input = null;
    private String na_output = null;
    private String bankUserType = "", transactionMode = "";
    /* parivartan changes */
    private String Check = "";
    private String product_Code, product_UIN, product_cateogory, product_type;
    private ImageButton imageButtonSmartPlatinaProposerPhotograph;
    private CheckBox cb_kerladisc;
    private LinearLayout tr_money_back_gold_proposer_detail2;

    private Spinner spnr_bi_money_back_gold_proposer_title;
    private EditText edt_bi_money_back_gold_proposer_first_name;
    private EditText edt_bi_money_back_gold_proposer_middle_name;
    private EditText edt_bi_money_back_gold_proposer_last_name, edt_bi_smart_wealth_builder_proposer_age;
    private Button btn_bi_money_back_gold_proposer_date;
    private Spinner spinnerProposerGender;
    private String gender_proposer = "";

    private String proposer_Title = "";
    private String proposer_First_Name = "";
    private String proposer_Middle_Name = "";
    private String proposer_Last_Name = "";
    private String MinesOccuInterest = "0", DefOccuInterest = "0";
    double sumAssured;

    double fyServiceTax_Occu = 0;
    double fyPremiumWithST_Occu = 0;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.bi_smart_platina_assure_main);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

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
            planName = getString(R.string.sbi_life_smart_platina_assure);
            product_Code = getString(R.string.sbi_life_smart_platina_assure_code);
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

        prsObj = new ParseXML();


        prop = new SmartPlatinaAssureProperties();

        list_data = new ArrayList<>();

        // Variable Declaration
        currencyFormat = new DecimalFormat("##,##,##,###");


        selStaffDisc = findViewById(R.id.cb_staffdisc);

        rb_proposerdetail_personaldetail_backdating_yes = findViewById(R.id.rb_smart_platina_assure_backdating_yes);
        rb_proposerdetail_personaldetail_backdating_no = findViewById(R.id.rb_smart_platina_assure_backdating_no);

        ll_backdating1 = findViewById(R.id.ll_backdating1);
        btn_proposerdetail_personaldetail_backdatingdate = findViewById(R.id.btn_smart_platina_assure_backdatingdate);
        edt_premiumAmt = findViewById(R.id.et_bi_smart_platina_assure_premium_amt);
        btn_bi_smart_platina_assure_life_assured_date = findViewById(R.id.btn_bi_smart_platina_assure_life_assured_date);
        spnr_bi_smart_platina_assure_life_assured_title = findViewById(R.id.spnr_bi_smart_platina_assure_life_assured_title);
        edt_bi_smart_platina_assure_life_assured_first_name = findViewById(R.id.edt_bi_smart_platina_assure_life_assured_first_name);
        edt_bi_smart_platina_assure_life_assured_middle_name = findViewById(R.id.edt_bi_smart_platina_assure_life_assured_middle_name);
        edt_bi_smart_platina_assure_life_assured_last_name = findViewById(R.id.edt_bi_smart_platina_assure_life_assured_last_name);
        spnrPremPayingTerm = findViewById(R.id.spnr_bi_smart_platina_assure_premium_paying_term);
        spnrPremPayingMode = findViewById(R.id.spnr_bi_smart_platina_assure_premium_paying_mode);
        spnrPolicyTerm = findViewById(R.id.spnr_bi_smart_platina_assure_policyterm);
        spnr_Gender = findViewById(R.id.spnr_bi_smart_platina_assure_selGender);

        //spnr_Gender.setClickable(false);
        //spnr_Gender.setEnabled(false);

        tr_money_back_gold_proposer_detail2 = findViewById(R.id.tr_money_back_gold_proposer_detail2);
        spnr_bi_money_back_gold_proposer_title = findViewById(R.id.spnr_bi_money_back_gold_proposer_title);
        edt_bi_money_back_gold_proposer_first_name = findViewById(R.id.edt_bi_money_back_gold_proposer_first_name);
        edt_bi_money_back_gold_proposer_middle_name = findViewById(R.id.edt_bi_money_back_gold_proposer_middle_name);
        edt_bi_money_back_gold_proposer_last_name = findViewById(R.id.edt_bi_money_back_gold_proposer_last_name);
        btn_bi_money_back_gold_proposer_date = findViewById(R.id.btn_bi_money_back_gold_proposer_date);
        edt_bi_smart_wealth_builder_proposer_age = findViewById(R.id.edt_bi_smart_wealth_builder_proposer_age);
        spinnerProposerGender = findViewById(R.id.spinnerProposerGender);

        spnr_Age = findViewById(R.id.spnr_bi_smart_platina_assure_age);

        edt_proposerdetail_basicdetail_contact_no = findViewById(R.id.edt_smart_platina_assure_contact_no);
        edt_proposerdetail_basicdetail_Email_id = findViewById(R.id.edt_smart_platina_assure_Email_id);
        edt_proposerdetail_basicdetail_ConfirmEmail_id = findViewById(R.id.edt_smart_platina_assure_ConfirmEmail_id);

        btnSubmit = findViewById(R.id.btn_bi_smart_platina_assure_btnSubmit);
        btnback = findViewById(R.id.btn_bi_smart_platina_assure_btnback);


        commonMethods.fillSpinnerValue(context, spnr_bi_smart_platina_assure_life_assured_title,
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

        String[] policyTermArray = {"12", "15"};

        ArrayAdapter<String> policyTermAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, policyTermArray);

        spnrPolicyTerm.setAdapter(policyTermAdapter);

        String[] premPayingTermArray = {"6", "7"};

        ArrayAdapter<String> premPayingTermAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item,
                premPayingTermArray);

        spnrPremPayingTerm.setAdapter(premPayingTermAdapter);
        spnrPremPayingTerm.setEnabled(false);

        String[] premPayingModeArray = {"Yearly", "Monthly"};

        ArrayAdapter<String> premPayingModeAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item,
                premPayingModeArray);

        spnrPremPayingMode.setAdapter(premPayingModeAdapter);

        // Calculate premium

        selStaffDisc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selStaffDisc.isChecked()) {
                    commonMethods.clearFocusable(selStaffDisc);
                    commonMethods.setFocusable(cb_kerladisc);
                    cb_kerladisc.requestFocus();
                }

            }
        });

        cb_kerladisc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_kerladisc.isChecked()) {
                    str_kerla_discount = "Yes";
                    commonMethods.clearFocusable(cb_kerladisc);
                    commonMethods.setFocusable(spnr_bi_smart_platina_assure_life_assured_title);
                    spnr_bi_smart_platina_assure_life_assured_title.requestFocus();
                } else {
                    str_kerla_discount = "No";
                    commonMethods.clearFocusable(cb_kerladisc);
                    commonMethods.setFocusable(spnr_bi_smart_platina_assure_life_assured_title);
                    spnr_bi_smart_platina_assure_life_assured_title.requestFocus();
                }
            }
        });


        edt_bi_smart_platina_assure_life_assured_first_name
                .setOnEditorActionListener(this);
        edt_bi_smart_platina_assure_life_assured_last_name
                .setOnEditorActionListener(this);
        edt_bi_smart_platina_assure_life_assured_middle_name
                .setOnEditorActionListener(this);
        edt_premiumAmt
                .setOnEditorActionListener(this);
        edt_proposerdetail_basicdetail_contact_no
                .setOnEditorActionListener(this);
        edt_proposerdetail_basicdetail_Email_id
                .setOnEditorActionListener(this);
        edt_proposerdetail_basicdetail_ConfirmEmail_id
                .setOnEditorActionListener(this);

        setSpinnerAndOtherListner();

        commonMethods.setFocusable(edt_bi_smart_platina_assure_life_assured_first_name);

        edt_bi_smart_platina_assure_life_assured_first_name
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
                        //email_id_validation(ProposerEmailId);

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
                        //confirming_email_id(proposer_confirm_emailId);

                    }
                });


        //getBasicDetail();

        if (needAnalysis_flag == 1 && !TextUtils.isEmpty(gender)) {
            onClickLADob(btn_bi_smart_platina_assure_life_assured_date);
        }
    }


    private void setSpinnerAndOtherListner() {


        rb_proposerdetail_personaldetail_backdating_yes
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {

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
                    }
                });

        rb_proposerdetail_personaldetail_backdating_no
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {

                        if (isChecked) {

                            proposer_Backdating_WishToBackDate_Policy = "n";
                            proposer_Backdating_BackDate = "";
                            ll_backdating1.setVisibility(View.GONE);

                            spnr_Age.setSelection(
                                    getIndex(spnr_Age, lifeAssuredAge), false);

                            valMaturityAge();
                            rb_proposerdetail_personaldetail_backdating_yes
                                    .setFocusable(false);

                            commonMethods.clearFocusable(rb_proposerdetail_personaldetail_backdating_no);
                            commonMethods.clearFocusable(rb_proposerdetail_personaldetail_backdating_yes);

                        }
                    }
                });

        spnr_bi_smart_platina_assure_life_assured_title
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long id) {

                        if (position > 0) {
                            lifeAssured_Title = spnr_bi_smart_platina_assure_life_assured_title
                                    .getSelectedItem().toString();
                            /*if (lifeAssured_Title.equalsIgnoreCase("Mr.")) {
                                gender = "Male";

                            } else if (lifeAssured_Title
                                    .equalsIgnoreCase("Ms.")) {
                                gender = "Female";
                            } else if (lifeAssured_Title
                                    .equalsIgnoreCase("Mrs.")) {
                                gender = "Female";
                                iv_women.setImageDrawable(getResources().getDrawable(
                                        R.drawable.female_selected));
                                iv_men.setImageDrawable(getResources().getDrawable(
                                        R.drawable.male_nonselected));
                            }*/
//                            setFocusable(edt_bi_smart_platina_assure_life_assured_first_name);

                            edt_bi_smart_platina_assure_life_assured_first_name
                                    .requestFocus();
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });
        spnr_bi_money_back_gold_proposer_title
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long id) {

                        if (position == 0) {
                            proposer_Title = "";
                        } else {
                            proposer_Title = spnr_bi_money_back_gold_proposer_title
                                    .getSelectedItem().toString();

                            if (proposer_Title.equalsIgnoreCase("Mr.")) {
                                // selGender.setSelection(getIndex(selGender,
                                // "Male"));
                            } else if (proposer_Title.equalsIgnoreCase("Ms.")
                                    || proposer_Title.equalsIgnoreCase("Mrs.")) {
                                // selGender.setSelection(getIndex(selGender,
                                // "Female"));
                            }
                            commonMethods.setFocusable(edt_bi_money_back_gold_proposer_first_name);
                            edt_bi_money_back_gold_proposer_first_name
                                    .requestFocus();

                        }

                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });
        spnrPolicyTerm.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spnrPolicyTerm.getSelectedItem().toString().equalsIgnoreCase("12")) {
                    spnrPremPayingTerm.setSelection(0);
                } else {
                    spnrPremPayingTerm.setSelection(1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnback.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                setResult(RESULT_OK);
                finish();
            }
        });
        btnSubmit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {

                inputVal = new StringBuilder();
                retVal = new StringBuilder();
                bussIll = new StringBuilder();

                lifeAssured_First_Name = edt_bi_smart_platina_assure_life_assured_first_name
                        .getText().toString().trim();
                lifeAssured_Middle_Name = edt_bi_smart_platina_assure_life_assured_middle_name
                        .getText().toString().trim();
                lifeAssured_Last_Name = edt_bi_smart_platina_assure_life_assured_last_name
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

                inputVal = new StringBuilder();
                retVal = new StringBuilder();
                bussIll = new StringBuilder();

                proposer_Title = spnr_bi_money_back_gold_proposer_title
                        .getSelectedItem().toString();
                proposer_First_Name = edt_bi_money_back_gold_proposer_first_name
                        .getText().toString().trim();
                proposer_Middle_Name = edt_bi_money_back_gold_proposer_middle_name
                        .getText().toString().trim();
                proposer_Last_Name = edt_bi_money_back_gold_proposer_last_name
                        .getText().toString().trim();

                name_of_proposer = proposer_Title + " " + proposer_First_Name
                        + " " + proposer_Middle_Name + " " + proposer_Last_Name;


                gender_proposer = spinnerProposerGender.getSelectedItem().toString();
                gender = spnr_Gender.getSelectedItem().toString();

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
                if (valLifeAssuredProposerDetail() && valDob() && valProposerDob() && valAge()
                        && valBasicDetail() && valMaturityAge()
                        && valPremiumAmt() && valBackdate()
                        && TrueBackdate()) {

                    if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {
                        proposer_Title = "";
                        proposer_First_Name = "";
                        proposer_Middle_Name = "";
                        proposer_Last_Name = "";
                        name_of_proposer = "";
                        proposer_date_of_birth = "";
                    }

                    addListenerOnSubmit();
                    getInput(smartPlatinaAssureBean);

                    if (needAnalysis_flag == 0) {

                        try {
                            Intent i = new Intent(getApplicationContext(), Success.class);

                            i.putExtra("ProductName",
                                    "Product : SBI Life - " + planName
                                            + " (UIN:" + product_UIN + ")");

                            int premiumDetailFYServiceTax = 0, premiumDetailFYBasicPremium = 0,
                                    premiumDetailSYServiceTax = 0;

                            try {
                                premiumDetailFYServiceTax = ((int) Double.parseDouble(prsObj
                                        .parseXmlTag(retVal.toString(), "FYServTax")));

                                premiumDetailFYBasicPremium = ((int) Double.parseDouble(prsObj
                                        .parseXmlTag(retVal.toString(), "yearlyPrem")));

                                premiumDetailSYServiceTax = ((int) Double.parseDouble(prsObj
                                        .parseXmlTag(retVal.toString(), "SYServTax")));


                            } catch (Exception e) {
                                e.printStackTrace();
                                premiumDetailFYServiceTax = 0;
                                premiumDetailFYBasicPremium = 0;
                                premiumDetailSYServiceTax = 0;
                            }

                            i.putExtra("op", "Yearly Basic Premium is Rs." + currencyFormat.format(Double
                                    .parseDouble(prsObj.parseXmlTag(retVal.toString(), "yearlyPrem"))));
                            i.putExtra("op1", "First Year Applicable Tax is Rs."
                                    + currencyFormat.format(premiumDetailFYServiceTax));


                            int premiumDetailFYPremiumWithServiceTax = premiumDetailFYBasicPremium + premiumDetailFYServiceTax;

                            i.putExtra("op2", "First Year Premium with Applicable Tax is Rs."
                                    + currencyFormat.format(premiumDetailFYPremiumWithServiceTax));

                            int premiumDetailSYPremiumWithServiceTax = premiumDetailFYBasicPremium + premiumDetailSYServiceTax;

                            i.putExtra("op3", "Second Year Onwards Applicable Tax is Rs."
                                    + currencyFormat.format(premiumDetailSYServiceTax));

                            i.putExtra("op4", "Second Year Onwards Premium with Applicable Tax is Rs."
                                    + currencyFormat.format(premiumDetailSYPremiumWithServiceTax));
                            i.putExtra("op5", "Sum Assured is Rs." + currencyFormat.format(Double
                                    .parseDouble(prsObj.parseXmlTag(retVal.toString(), "sumAssured"))));

                            i.putExtra("header", "SBI Life - " + planName);
                            i.putExtra("header1", "(UIN:" + product_UIN + ")");
                            startActivity(i);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Dialog();
                    }

                }
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
                        imageButtonSmartPlatinaProposerPhotograph
                                .setImageBitmap(scaled);
                    } else {
                        photoBitmap = null;
                    }
                }
            }
        }
        /* end */
    }

    private void Dialog() {

        final Dialog d = new Dialog(this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.BLACK));

        d.setContentView(R.layout.layout_smart_platina_assure_bi_grid);

        TextView tv_proposername = d
                .findViewById(R.id.tv_smart_platina_assure_proposername);
        TextView tv_proposal_number = d
                .findViewById(R.id.tv_smart_platina_assure_proposal_number);

        TextView tv_life_assured_name = d
                .findViewById(R.id.tv_bi_smart_platina_assure_life_assured_name);
        TextView tv_life_assured_name2 = d
                .findViewById(R.id.tv_bi_smart_platina_assure_life_assured_name2);

        TextView tv_life_age_at_entry = d
                .findViewById(R.id.tv_bi_smart_platina_assure_life_assured_age);
        TextView tv_life_age_at_entry2 = d
                .findViewById(R.id.tv_bi_smart_platina_assure_life_assured_age2);

        TextView tv_life_assured_gender = d
                .findViewById(R.id.tv_bi_smart_platina_assure_life_assured_gender);
        TextView tv_life_assured_gender2 = d
                .findViewById(R.id.tv_bi_smart_platina_assure_life_assured_gender2);

        TextView tv_premPaymentfrequency = d
                .findViewById(R.id.tv_bi_smart_platina_assure_life_assured_paying_mode);

        TextView tv_bi_is_Staff = d
                .findViewById(R.id.tv_bi_is_Staff);

        TextView tv_sum_assured_at_inception = d.findViewById(R.id.tv_sum_assured_at_inception);

       /* String guaranteed_death_benefit2 = commonForAllProd.getRoundUp(commonForAllProd
                .getStringWithout_E(Double.parseDouble(prsObj
                        .parseXmlTag(output, "guarntdDeathBen" + "1" + ""))))  ;

*/

        TextView tv_basic_cover_term = d
                .findViewById(R.id.tv_bi_smart_platina_assure_term_basic_cover);

        TextView tv_basic_cover_premPayment_term = d
                .findViewById(R.id.tv_bi_smart_platina_assure_premium_paying_term_basic_cover);

        TextView tv_basic_cover_sum_assured = d
                .findViewById(R.id.tv_bi_smart_platina_assure_sum_assured_basic_cover);

        TextView tv_basic_cover_yearly_premium = d
                .findViewById(R.id.tv_bi_smart_platina_assure_yearly_premium_basic_cover);

        TextView tv_basic_cover_annualised_premium = d
                .findViewById(R.id.tv_bi_smart_platina_assure_annualised_premium_basic_cover);

        TableRow tr_proof_adentity = d.findViewById(R.id.tr_proof_adentity);


        TextView tv_premium_detail_basic_premium_first_year = d
                .findViewById(R.id.tv_bi_smart_platina_assure_basic_prem_first_year);
        TextView tv_premium_detail_basic_premium_first_year1 = d
                .findViewById(R.id.tv_bi_smart_platina_assure_basic_prem_first_year1);

        TextView tv_premium_detail_service_tax_first_year = d
                .findViewById(R.id.tv_bi_smart_platina_assure_service_tax_first_year);

        TextView tv_premium_detail_basic_premium_with_tax_first_year = d
                .findViewById(R.id.tv_bi_smart_platina_assure_premium_with_service_tax_first_year);
        TextView tv_premium_detail_basic_premium_with_tax_first_year1 = d
                .findViewById(R.id.tv_bi_smart_platina_assure_premium_with_service_tax_first_year1);

        TextView tv_premium_detail_basic_premium_second_year = d
                .findViewById(R.id.tv_bi_smart_platina_assure_basic_prem_second_year);

        TextView tv_premium_detail_service_tax_second_year = d
                .findViewById(R.id.tv_bi_smart_platina_assure_service_tax_second_year);

        TextView tv_premium_detail_basic_premium_with_tax_second_year = d
                .findViewById(R.id.tv_bi_smart_platina_assure_premium_with_service_tax_second_year);

        GridView gv_userinfo = d
                .findViewById(R.id.gv_smart_platina_assure_userinfo);
        TextView tv_backdatingint = d
                .findViewById(R.id.tv_backdatingint);
        gv_userinfo.setVerticalScrollBarEnabled(true);
        gv_userinfo.setSmoothScrollbarEnabled(true);

        final EditText edt_Policyholderplace = d
                .findViewById(R.id.edt_smart_platina_assure_Policyholderplace);

        final TextView txt_smart_platina_assure_proposer_name = d
                .findViewById(R.id.txt_smart_platina_assure_proposer_name);

        final EditText edt_proposer_name_e_sign = d
                .findViewById(R.id.edt_proposer_name_e_sign);

        final CheckBox cb_statement = d
                .findViewById(R.id.cb_smart_platina_assure_statement);
        cb_statement.setChecked(false);

        TextView tv_bi_smart_platina_assure_basic_service_tax_first_year = d
                .findViewById(R.id.tv_bi_smart_platina_assure_basic_service_tax_first_year);
        TextView tv_bi_smart_platina_assure_swachh_bharat_cess_first_year = d
                .findViewById(R.id.tv_bi_smart_platina_assure_swachh_bharat_cess_first_year);
        TextView tv_bi_smart_platina_assure_krishi_kalyan_cess_first_year = d
                .findViewById(R.id.tv_bi_smart_platina_assure_krishi_kalyan_cess_first_year);

        TextView tv_bi_smart_platina_assure_basic_service_tax_second_year = d
                .findViewById(R.id.tv_bi_smart_platina_assure_basic_service_tax_second_year);
        TextView tv_bi_smart_platina_assure_swachh_bharat_cess_second_year = d
                .findViewById(R.id.tv_bi_smart_platina_assure_swachh_bharat_cess_second_year);
        TextView tv_bi_smart_platina_assure_krishi_kalyan_cess_second_year = d
                .findViewById(R.id.tv_bi_smart_platina_assure_krishi_kalyan_cess_second_year);

        TextView tv_premium_install_rider_type1 = d
                .findViewById(R.id.tv_premium_install_rider_type1);
        TextView tv_mandatory_bi_smart_money_planner_yearly_premium_with_tax1 = d
                .findViewById(R.id.tv_mandatory_bi_smart_money_planner_yearly_premium_with_tax1);
        TextView tv_premium_type = d
                .findViewById(R.id.tv_premium_type);

        TableRow tr_second_year = d
                .findViewById(R.id.tr_second_year);
        TextView tv_smart_platina_assuree_distribution_channel = d
                .findViewById(R.id.tv_smart_platina_assuree_distribution_channel);
        tv_smart_platina_assuree_distribution_channel.setText(userType);
        TextView tv_bi_is_State = d.findViewById(R.id.tv_bi_is_State);

        /* Need Analysis */
        final TextView txt_proposer_name_need_analysis = d
                .findViewById(R.id.txt_proposer_name_need_analysis);

        final CheckBox cb_statement_need_analysis = d
                .findViewById(R.id.cb_statement_need_analysis);
        cb_statement_need_analysis.setChecked(true);
        TableRow tr_need_analysis = d
                .findViewById(R.id.tr_need_analysis);
        final CheckBox checkboxAgentStatement = d.findViewById(R.id.checkboxAgentStatement);
        checkboxAgentStatement.setChecked(false);

        TextView tv_rate_of_applicable_taxes = d.findViewById(R.id.tv_rate_of_applicable_taxes);
       /*  if (cb_kerladisc.isChecked()) {
            tv_bi_is_State.setText("Kerala");
            tv_rate_of_applicable_taxes.setText(" 4.75% in the 1st policy year and 2.375% from 2nd policy year onwards");
        } else {
            tv_bi_is_State.setText("Non Kerala");
            tv_rate_of_applicable_taxes.setText(" 4.5% in the 1st policy year and 2.25% from 2nd policy year onwards");


        }
*/

        if (cb_kerladisc.isChecked()) {
            tv_bi_is_State.setText("");
            tv_rate_of_applicable_taxes.setText(" 4.75% in the 1st policy year and 2.375% from 2nd policy year onwards");
        } else {
            tv_bi_is_State.setText("");
            tv_rate_of_applicable_taxes.setText(" 4.5% in the 1st policy year and 2.25% from 2nd policy year onwards");


        }

        if (NeedAnalysisActivity.str_need_analysis != null) {
            if (NeedAnalysisActivity.str_need_analysis.equals("1")) {


                flg_needAnalyis = "1";
                tr_need_analysis.setVisibility(View.VISIBLE);
            } else {
                cb_statement_need_analysis.setChecked(true);
                tr_need_analysis.setVisibility(View.GONE);
            }

        }

        Button btn_proceed = d
                .findViewById(R.id.btn_smart_platina_assure_proceed);

        btn_MarketingOfficalDate = d
                .findViewById(R.id.btn_smart_platina_assure_MarketingOfficalDate);
        btn_PolicyholderDate = d
                .findViewById(R.id.btn_smart_platina_assure_PolicyholderDate);

        Ibtn_signatureofMarketing = d
                .findViewById(R.id.Ibtn_smart_platina_assure_signatureofMarketing);
        Ibtn_signatureofPolicyHolders = d
                .findViewById(R.id.Ibtn_smart_platina_assure_signatureofPolicyHolders);
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
        if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {
            tv_proposername.setText(name_of_life_assured);
            tv_life_assured_name2.setText(name_of_life_assured);
            txt_smart_platina_assure_proposer_name
                    .setText("I, "
                            + name_of_life_assured
                            + ", having received the information with respect to the above, have understood the above statement before entering into the contract.");
            txt_proposer_name_need_analysis
                    .setText("I, "
                            + name_of_life_assured
                            + ", have undergone the Need Analysis and after having reviewed the SBI Life Product options, I have opted for 'SBI LIFE - " +
                            getString(R.string.sbi_life_smart_platina_assure));
            tv_life_assured_name.setText(name_of_life_assured);

        } else if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("n")) {

            tv_proposername.setText(name_of_proposer);
            tv_life_assured_name2.setText(name_of_life_assured);
            txt_smart_platina_assure_proposer_name
                    .setText("I, "
                            + name_of_proposer
                            + ", having received the information with respect to the above, have understood the above statement before entering into the contract.");
            txt_proposer_name_need_analysis
                    .setText("I, "
                            + name_of_proposer
                            + ", have undergone the Need Analysis and after having reviewed the SBI Life Product options, I have opted for 'SBI LIFE - " + getString(R.string.sbi_life_smart_platina_assure));

            tv_proposername.setText(name_of_proposer);
            tv_life_assured_name.setText(name_of_proposer);
        }

        tv_proposal_number.setText(QuatationNumber);
        edt_Policyholderplace.setText(place2);
        TextView textviewAGentStatement = d.findViewById(R.id.textviewAGentStatement);
        textviewAGentStatement.setText(commonMethods.getAgentDeclaration(context));

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

        imageButtonSmartPlatinaProposerPhotograph = d
                .findViewById(R.id.imageButtonSmartPlatinaProposerPhotograph);
        imageButtonSmartPlatinaProposerPhotograph
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
            imageButtonSmartPlatinaProposerPhotograph
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
                            imageButtonSmartPlatinaProposerPhotograph
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

                name_of_person = txt_smart_platina_assure_proposer_name.getText().toString();
                place2 = edt_Policyholderplace.getText().toString();

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
                    String productCode = getString(R.string.sbi_life_smart_platina_assure_code) + "SPA";

                    na_cbi_bean = new NA_CBI_bean(QuatationNumber, agentcode,
                            "", userType, "", lifeAssured_Title,
                            lifeAssured_First_Name, lifeAssured_Middle_Name,
                            lifeAssured_Last_Name, planName,
                            commonForAllProd.getRound(commonForAllProd.getStringWithout_E(Double
                                    .valueOf(basicCoverSumAssured.equals("") ? "0"
                                            : basicCoverSumAssured))), commonForAllProd
                            .getRound(premiumDetailFYPremiumWithServiceTax), emailId,
                            mobileNo, agentEmail, agentMobile, na_input,
                            na_output, smartPlatinaAssureBean.getPremfreq(),
                            Integer.parseInt(policyTerm), smartPlatinaAssureBean.getPremPayingTerm(), productCode,
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

                    db.AddNeedAnalysisDashboardDetails(new ProductBIBean("",
                            QuatationNumber, planName, getCurrentDate(),
                            mobileNo, getCurrentDate(), db.GetUserCode(),
                            emailId, "", "", agentcode, "", userType, "",
                            lifeAssured_Title, lifeAssured_First_Name,
                            lifeAssured_Middle_Name, lifeAssured_Last_Name,
                            commonForAllProd.getRound(commonForAllProd.getStringWithout_E(Double
                                    .valueOf(basicCoverSumAssured.equals("") ? "0"
                                            : basicCoverSumAssured))), commonForAllProd
                            .getRound(premiumDetailFYPremiumWithServiceTax), agentEmail,
                            agentMobile, na_input, na_output,
                            smartPlatinaAssureBean.getPremfreq(), Integer
                            .parseInt(policyTerm), smartPlatinaAssureBean.getPremPayingTerm(), productCode,
                            commonMethods.getDDMMYYYYDate(lifeAssured_date_of_birth),
                            commonMethods.getDDMMYYYYDate(proposer_date_of_birth), "", transactionMode, inputVal
                            .toString(), retVal.toString().replace(
                            bussIll.toString(), "")));
                    createPdf();
                    NABIObj.serviceHit(context,
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
                        commonMethods.setFocusable(imageButtonSmartPlatinaProposerPhotograph);
                        imageButtonSmartPlatinaProposerPhotograph
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

            }
        });
        input = inputVal.toString();
        output = retVal.toString();

        ageAtEntry = prsObj.parseXmlTag(input, "age");
        strProposerAge = prsObj.parseXmlTag(input, "proposer_age");
        gender = prsObj.parseXmlTag(input, "gender");
        gender_proposer = prsObj.parseXmlTag(input, "proposer_gender");

        if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {
            tv_life_age_at_entry.setText(ageAtEntry + " Years");
            tv_life_age_at_entry2.setText(ageAtEntry + " Years");
            tv_life_assured_gender.setText(gender);
            tv_life_assured_gender2.setText(gender);

        } else if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("n")) {
            tv_life_age_at_entry.setText(strProposerAge + " Years");
            tv_life_age_at_entry2.setText(ageAtEntry + " Years");
            tv_life_assured_gender.setText(gender_proposer);
            tv_life_assured_gender2.setText(gender);
        }


        premPayingMode = prsObj.parseXmlTag(input, "premFreq");
        tv_premPaymentfrequency.setText(premPayingMode);

        staffdiscount = prsObj.parseXmlTag(input, "isStaff");

        if (staffdiscount.equalsIgnoreCase("true")) {
            tv_bi_is_Staff.setText("Yes");
        } else {
            tv_bi_is_Staff.setText("No");
        }

        premPayingTerm = prsObj.parseXmlTag(input, "premPayingTerm");
        tv_basic_cover_premPayment_term.setText(premPayingTerm);

        policyTerm = prsObj.parseXmlTag(input, "policyTerm");
        tv_basic_cover_term.setText(policyTerm);

        if (premPayingMode.equals("Yearly")) {
            tv_premium_type.setText("Yearly Premium (in Rs)");
            //tv_premium_install_rider_type1.setText("Yearly premium (in Rs.)");
            //   tv_mandatory_bi_smart_money_planner_yearly_premium_with_tax1.setText("Yearly Premium, with \n" + "applicable taxes (in Rs.)");
        } else if (premPayingMode.equals("Monthly")) {
            tv_premium_type.setText("Monthly Premium (in Rs)");
            //tv_premium_install_rider_type1.setText("Monthly premium (in Rs.)");
            //  tv_mandatory_bi_smart_money_planner_yearly_premium_with_tax1.setText("Monthly Premium, with \n" + "applicable taxes (in Rs.)");
        }

        basicCoverYearlyPremium = ((int) Double.parseDouble(prsObj.parseXmlTag(input, "premiumAmount"))) + "";
        premiumDetailFYBasicPremium = ((int) Double.parseDouble(prsObj
                .parseXmlTag(output, "yearlyPrem"))) + "";

        //basicCoverYearlyPremium = ((int) Double.parseDouble(prsObj.parseXmlTag(input, "premiumAmount"))) + "";
        tv_basic_cover_annualised_premium.setText("" + commonMethods.getformatedThousandString(Integer.parseInt(premiumDetailFYBasicPremium)));


               /* if(Double.parseDouble(basicCoverYearlyPremium) > 100000)
                {
            tr_proof_adentity.setVisibility(View.VISIBLE);
        } else {
            tr_proof_adentity.setVisibility(View.GONE);
                    }*/


        basicCoverSumAssured = commonForAllProd
                .getRound(commonForAllProd.getStringWithout_E(Double
                        .valueOf(((prsObj.parseXmlTag(output,
                                "sumAssured") == null) || (prsObj
                                .parseXmlTag(output, "sumAssured")
                                .equals(""))) ? "0" : prsObj
                                .parseXmlTag(output, "sumAssured"))))
                + "";

        tv_basic_cover_sum_assured.setText(""
                + commonMethods.getformatedThousandString(Double
                .parseDouble(basicCoverSumAssured)));


        tr_second_year.setVisibility(View.VISIBLE);


        tv_premium_detail_basic_premium_first_year1.setText("" + commonMethods.getformatedThousandString(Integer.parseInt(premiumDetailFYBasicPremium)));

        tv_basic_cover_yearly_premium.setText("" + commonMethods.getformatedThousandString(Integer
                .parseInt(premiumDetailFYBasicPremium)));

        premiumDetailFYServiceTax = ((int) Double.parseDouble(prsObj
                .parseXmlTag(output, "FYServTax"))) + "";
       /* tv_premium_detail_service_tax_first_year.setText(""
                + premiumDetailFYServiceTax);*/

        premiumDetailFYPremiumWithServiceTax = ((int) (Double
                .parseDouble(premiumDetailFYBasicPremium.equals("")
                        || (premiumDetailFYBasicPremium.equals(null)) ? "0"
                        : premiumDetailFYBasicPremium) + Double
                .parseDouble(premiumDetailFYServiceTax.equals("")
                        || (premiumDetailFYServiceTax.equals(null)) ? "0"
                        : premiumDetailFYServiceTax)))
                + "";
        tv_premium_detail_basic_premium_first_year.setText("" + commonMethods.getformatedThousandString(Integer.parseInt(premiumDetailFYPremiumWithServiceTax)));
        tv_premium_detail_basic_premium_with_tax_first_year.setText("" + commonMethods.getformatedThousandString(Integer.parseInt(premiumDetailFYPremiumWithServiceTax)));
        tv_premium_detail_basic_premium_with_tax_first_year1.setText("" + commonMethods.getformatedThousandString(Integer.parseInt(premiumDetailFYBasicPremium)));

        // Amit changes start- 23-5-2016
        // basicServiceTax = prsObj.parseXmlTag(output, "basicServiceTax");
        basicServiceTax = prsObj.parseXmlTag(output, "FYServTax");

        tv_bi_smart_platina_assure_basic_service_tax_first_year
                .setText(""
                        + commonForAllProd.getRound(commonForAllProd.getStringWithout_E(Double
                        .valueOf(basicServiceTax.equals("") ? "0"
                                : basicServiceTax))));

        SBCServiceTax = prsObj.parseXmlTag(output, "SBCServiceTax");

        tv_bi_smart_platina_assure_swachh_bharat_cess_first_year
                .setText(""
                        + commonForAllProd.getRound(commonForAllProd
                        .getStringWithout_E(Double
                                .valueOf(SBCServiceTax.equals("") ? "0"
                                        : SBCServiceTax))));

        KKCServiceTax = prsObj.parseXmlTag(output, "KKCServiceTax");

        tv_bi_smart_platina_assure_krishi_kalyan_cess_first_year
                .setText(""
                        + commonForAllProd.getRound(commonForAllProd
                        .getStringWithout_E(Double
                                .valueOf(KKCServiceTax.equals("") ? "0"
                                        : KKCServiceTax))));
        // Amit changes end- 23-5-2016

        premiumDetailSYBasicPremium = ((int) Double.parseDouble(prsObj
                .parseXmlTag(output, "yearlyPrem"))) + "";


        premiumDetailSYServiceTax = ((int) Double.parseDouble(prsObj
                .parseXmlTag(output, "SYServTax"))) + "";
      /*  tv_premium_detail_service_tax_second_year.setText(""
                + premiumDetailSYServiceTax);*/

        premiumDetailSYPremiumWithServiceTax = ((int) (Double
                .parseDouble(premiumDetailSYBasicPremium.equals("") ? "0"
                        : premiumDetailSYBasicPremium) + Double
                .parseDouble(premiumDetailSYServiceTax.equals("") ? "0"
                        : premiumDetailSYServiceTax)))
                + "";

        tv_premium_detail_basic_premium_second_year.setText("" + commonMethods.getformatedThousandString(Integer.parseInt(premiumDetailSYPremiumWithServiceTax)));
        tv_premium_detail_basic_premium_with_tax_second_year.setText("" + commonMethods.getformatedThousandString(Integer.parseInt(premiumDetailSYPremiumWithServiceTax)));

        // Amit changes start- 23-5-2016
        // basicServiceTaxSecondYear = prsObj.parseXmlTag(output,
        // "basicServiceTaxSecondYear");
        basicServiceTaxSecondYear = prsObj.parseXmlTag(output, "SYServTax");

        tv_bi_smart_platina_assure_basic_service_tax_second_year
                .setText(""
                        + commonForAllProd.getRound(commonForAllProd
                        .getStringWithout_E(Double
                                .valueOf(premiumDetailSYPremiumWithServiceTax
                                        .equals("") ? "0"
                                        : premiumDetailSYPremiumWithServiceTax))));

        SBCServiceTaxSecondYear = prsObj.parseXmlTag(output,
                "SBCServiceTaxSecondYear");

        tv_bi_smart_platina_assure_swachh_bharat_cess_second_year
                .setText(""
                        + commonForAllProd.getRound(commonForAllProd
                        .getStringWithout_E(Double
                                .valueOf(SBCServiceTaxSecondYear
                                        .equals("") ? "0"
                                        : SBCServiceTaxSecondYear))));

        KKCServiceTaxSecondYear = prsObj.parseXmlTag(output,
                "KKCServiceTaxSecondYear");

        tv_bi_smart_platina_assure_krishi_kalyan_cess_second_year
                .setText(""
                        + commonForAllProd.getRound(commonForAllProd
                        .getStringWithout_E(Double
                                .valueOf(KKCServiceTaxSecondYear
                                        .equals("") ? "0"
                                        : KKCServiceTaxSecondYear))));
        // Amit changes end- 23-5-2016

        BackdatingInt = prsObj.parseXmlTag(output, "backdateInt");

        tv_backdatingint.setText(" Rs."
                + commonForAllProd.getRound(commonForAllProd
                .getStringWithout_E(Double.valueOf(BackdatingInt
                        .equals("") ? "0" : BackdatingInt))));

        TextView tv_Company_policy_surrender_dec = d
                .findViewById(R.id.tv_Company_policy_surrender_dec);

        Company_policy_surrender_dec = "Your SBI Life-Smart Platina Assure (UIN - 111N126V04) is a "
                + " Limited "
                + " premium policy, for which your first year "
                + premPayingMode
                + " Premium is Rs "
                + commonMethods.getformatedThousandString(Integer.parseInt(premiumDetailFYPremiumWithServiceTax))
                + " .Your Policy Term is "
                + policyTerm
                + " years"
                + " ,Premium Payment Term is "
                + premPayingTerm
                + " years"
                + " and Basic Sum Assured is Rs. " +

                commonMethods.getformatedThousandString(Double.parseDouble(basicCoverSumAssured));

        tv_Company_policy_surrender_dec.setText(Company_policy_surrender_dec);

        for (int i = 1; i <= Integer.parseInt(policyTerm); i++) {

            String end_of_year = prsObj
                    .parseXmlTag(output, "policyYr" + i + "");
            String yearly_basic_premium = commonForAllProd.getRoundUp(commonForAllProd
                    .getStringWithout_E(Double.parseDouble(prsObj
                            .parseXmlTag(output, "totBasePremPaid" + i + ""))));

            String guaranteed_addition = commonForAllProd.getRoundUp(commonForAllProd
                    .getStringWithout_E(Double.parseDouble(prsObj
                            .parseXmlTag(output, "guarntdAddtn" + i + ""))));
            String guaranteed_death_benefit = commonForAllProd.getRoundUp(commonForAllProd
                    .getStringWithout_E(Double.parseDouble(prsObj
                            .parseXmlTag(output, "guarntdDeathBen" + i + ""))));
            String guaranteed_survival_benefit = commonForAllProd.getRoundUp(commonForAllProd
                    .getStringWithout_E(Double
                            .parseDouble(prsObj.parseXmlTag(output,
                                    "guarntdSurvivalBen" + i + ""))));
            String guaranteed_surrender_value = commonForAllProd.getRoundUp(commonForAllProd
                    .getStringWithout_E(Double
                            .parseDouble(prsObj.parseXmlTag(output, "guarntdSurrndrVal"
                                    + i + ""))));

            String SpecialSurrenderValue = commonForAllProd.getRoundUp(commonForAllProd
                    .getStringWithout_E(Double.parseDouble(prsObj
                            .parseXmlTag(output, "SpecialSurrenderValue" + i + ""))));

            list_data.add(new M_BI_Smart_Platina_Assure(end_of_year,
                    yearly_basic_premium, "0", guaranteed_addition,
                    guaranteed_survival_benefit, guaranteed_death_benefit,
                    guaranteed_surrender_value, SpecialSurrenderValue));

        }

        //   String   guaranteed_death_benefit2 = prsObj.parseXmlTag(output, "guarntdDeathBen" + "1" + "");

        String guaranteed_death_benefit2 = prsObj.parseXmlTag(output, "SumAssuredOnDeath");
        tv_sum_assured_at_inception.setText(""
                + commonMethods.getformatedThousandString(Double
                .parseDouble(guaranteed_death_benefit2)));

        Adapter_BI_SmartPlatinaAssureGridNew adapter = new Adapter_BI_SmartPlatinaAssureGridNew(
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


    // Store user input in Bean object
    private void addListenerOnSubmit() {
        smartPlatinaAssureBean = new SmartPlatinaAssureBean();
        // Insert data entered by user in an object

        smartPlatinaAssureBean.setIsStaff(selStaffDisc.isChecked());

        smartPlatinaAssureBean.setKerlaDisc(cb_kerladisc.isChecked());

        smartPlatinaAssureBean.setAge(Integer.parseInt(spnr_Age.getSelectedItem().toString()));
        smartPlatinaAssureBean.setGender(gender);
        smartPlatinaAssureBean.setPremiumAmt(Double.parseDouble(edt_premiumAmt.getText().toString()));
        smartPlatinaAssureBean.setPremfreq(spnrPremPayingMode.getSelectedItem().toString());
        smartPlatinaAssureBean.setPolicyTerm(Integer.parseInt(spnrPolicyTerm.getSelectedItem().toString()));
        smartPlatinaAssureBean.setPremPayingTerm(Integer.parseInt(spnrPremPayingTerm.getSelectedItem().toString()));


        // Show Smart Platina Assure Output Screen
        showSmartPlatinaAssureOutputPg(smartPlatinaAssureBean);
    }

    private void getInput(SmartPlatinaAssureBean smartplatina_assureBean) {
        inputVal = new StringBuilder();

        boolean staffDisc = smartplatina_assureBean.getIsStaff();
        int age = smartPlatinaAssureBean.getAge();
        String gender = smartplatina_assureBean.getGender();
        int policyTerm = smartplatina_assureBean.getPolicyTerm();
        double premAmount = smartplatina_assureBean.getPremiumAmt();
        String PremPayingMode = smartplatina_assureBean.getPremfreq();
        int premPayingTerm = smartplatina_assureBean.getPremPayingTerm();

        String LifeAssured_title = spnr_bi_smart_platina_assure_life_assured_title
                .getSelectedItem().toString();
        String LifeAssured_firstName = edt_bi_smart_platina_assure_life_assured_first_name
                .getText().toString();
        String LifeAssured_middleName = edt_bi_smart_platina_assure_life_assured_middle_name
                .getText().toString();
        String LifeAssured_lastName = edt_bi_smart_platina_assure_life_assured_last_name
                .getText().toString();
        String LifeAssured_DOB = btn_bi_smart_platina_assure_life_assured_date.getText().toString();

        String wish_to_backdate_flag = "";
        if (rb_proposerdetail_personaldetail_backdating_yes.isChecked()) {
            wish_to_backdate_flag = "y";
        } else {
            wish_to_backdate_flag = "n";
        }
        String backdate = "";
        if (wish_to_backdate_flag.equals("y")) {
            backdate = btn_proposerdetail_personaldetail_backdatingdate.getText().toString();
        } else {
            backdate = "";
        }

        inputVal.append("<?xml version='1.0' encoding='utf-8' ?><smartplatina_assure>");
        inputVal.append("<isStaff>").append(staffDisc).append("</isStaff>");
        inputVal.append("<age>").append(age).append("</age>");
        inputVal.append("<str_kerla_discount>" + str_kerla_discount + "</str_kerla_discount>");
        inputVal.append("<gender>").append(gender).append("</gender>");
        inputVal.append("<str_kerla_discount>" + str_kerla_discount + "</str_kerla_discount>");
        inputVal.append("<LifeAssured_title>").append(LifeAssured_title).append("</LifeAssured_title>");
        inputVal.append("<LifeAssured_firstName>").append(LifeAssured_firstName).append("</LifeAssured_firstName>");
        inputVal.append("<LifeAssured_middleName>").append(LifeAssured_middleName).append("</LifeAssured_middleName>");
        inputVal.append("<LifeAssured_lastName>").append(LifeAssured_lastName).append("</LifeAssured_lastName>");
        inputVal.append("<LifeAssured_DOB>").append(LifeAssured_DOB).append("</LifeAssured_DOB>");
        inputVal.append("<LifeAssured_age>").append(age).append("</LifeAssured_age>");


        inputVal.append("<proposer_title>").append(proposer_Title).append("</proposer_title>");
        inputVal.append("<proposer_firstName>").append(proposer_First_Name).append("</proposer_firstName>");
        inputVal.append("<proposer_middleName>").append(proposer_Middle_Name).append("</proposer_middleName>");
        inputVal.append("<proposer_lastName>").append(proposer_Last_Name).append("</proposer_lastName>");
        inputVal.append("<proposer_DOB>").append(proposer_date_of_birth).append("</proposer_DOB>");
        inputVal.append("<proposer_age>").append(strProposerAge).append("</proposer_age>");
        inputVal.append("<proposer_gender>").append(gender_proposer).append("</proposer_gender>");

        inputVal.append("<policyTerm>").append(policyTerm).append("</policyTerm>");
        inputVal.append("<premFreq>").append(PremPayingMode).append("</premFreq>");
        inputVal.append("<premiumAmount>").append(premAmount).append("</premiumAmount>");
        inputVal.append("<premPayingTerm>").append(premPayingTerm).append("</premPayingTerm>");
        inputVal.append("<Product_Cat>" + product_cateogory + "</Product_Cat>");

        inputVal.append("<product_name>").append(planName).append("</product_name>");
        inputVal.append("<product_Code>").append(product_Code).append("</product_Code>");
        inputVal.append("<product_UIN>").append(product_UIN).append("</product_UIN>");
        inputVal.append("<product_cateogory>").append(product_cateogory).append("</product_cateogory>");
        inputVal.append("<product_type>").append(product_type).append("</product_type>");

        inputVal.append("<proposer_Is_Same_As_Life_Assured>").append(proposer_Is_Same_As_Life_Assured).append("</proposer_Is_Same_As_Life_Assured>");
        inputVal.append("<Wish_to_backdate_policy>").append(wish_to_backdate_flag).append("</Wish_to_backdate_policy>");
        inputVal.append("<backdating_Date>").append(backdate).append("</backdating_Date>");
        inputVal.append("<isJKResident>").append("false").append("</isJKResident>");

        //Added Tushar Kadam Kerla Applicable Tax 7-Jun-2019
        String str_kerla_discount = "N";
        if (cb_kerladisc.isChecked()) {
            str_kerla_discount = "Y";
        }

        inputVal.append("<KFC>" + str_kerla_discount + "</KFC>");
        //End Added Tushar Kadam Kerla Applicable Tax 7-Jun-2019

        inputVal.append("</smartplatina_assure>");

    }

    /********************************** Output starts here **********************************************************/
    // Display Smart Platina Assure Output Screen
    private void showSmartPlatinaAssureOutputPg(SmartPlatinaAssureBean smartPlatinaAssureBean) {
        retVal = new StringBuilder();

        if (selStaffDisc.isChecked()) {

            staffStatus = "sbi";

        } else {
            staffStatus = "none";
        }
        try {
            String[] outputArr = getOutput("BI_of_Smart_Platina_Assure", smartPlatinaAssureBean);


            retVal.append("<?xml version='1.0' encoding='utf-8' ?><SmartPlatinaAssure>");
            retVal.append("<errCode>0</errCode>");
            retVal.append("<staffStatus>").append(staffStatus).append("</staffStatus>");
            retVal.append("<staffRebate></staffRebate>");
            retVal.append("<yearlyPrem>").append(outputArr[0]).append("</yearlyPrem>").
                    append("<FYServTax>").append(outputArr[1]).append("</FYServTax>").
                    append("<FYPrem>").append(outputArr[2]).append("</FYPrem>").
                    append("<SYServTax>").append(outputArr[3]).append("</SYServTax>").
                    append("<SYPrem>").append(outputArr[4]).append("</SYPrem>").
                    append("<sumAssured>").append(outputArr[5]).append("</sumAssured>").
                    append("<GuarntdMatBeneft>").append(outputArr[6]).append("</GuarntdMatBeneft>").
                    append("<SAMF>").append(outputArr[7]).append("</SAMF>").
                    append("<BackDateinterest>").append(outputArr[8]).append("</BackDateinterest>").
                    append("<backdateInt>").append(outputArr[8]).append("</backdateInt>").
                    append("<basicServiceTax>").append(outputArr[9]).append("</basicServiceTax>").
                    append("<SBCServiceTax>").append(outputArr[10]).append("</SBCServiceTax>").
                    append("<KKCServiceTax>").append(outputArr[11]).append("</KKCServiceTax>").
                    append("<basicServiceTaxSecondYear>").append(outputArr[12]).append("</basicServiceTaxSecondYear>").
                    append("<SBCServiceTaxSecondYear>").append(outputArr[13]).append("</SBCServiceTaxSecondYear>").
                    append("<KKCServiceTaxSecondYear>").append(outputArr[14]).append("</KKCServiceTaxSecondYear>").
                    append("<MinesOccuInterest>").append(outputArr[15]).append("</MinesOccuInterest>").
                    append("<OccuInt>").append(outputArr[15]).append("</OccuInt>").
                    append("<OccuIntServiceTax>").append(outputArr[16]).append("</OccuIntServiceTax>").
                    append("<KeralaCessServiceTax>" + outputArr[17] + "</KeralaCessServiceTax>").
                    append("<KeralaCessServiceTaxSecondYear>" + outputArr[18] + "</KeralaCessServiceTaxSecondYear>").append("<SumAssuredOnDeath>" + outputArr[19] + "</SumAssuredOnDeath>");

            int index = smartPlatinaAssureBean.getPolicyTerm();
            String guarntdSurvivalBen = prsObj.parseXmlTag(bussIll.toString(), "guarntdSurvivalBen" + index + "");

            retVal.append("<guarntdSurvivalBen" + index + ">" + guarntdSurvivalBen + "</guarntdSurvivalBen" + index + ">");
            retVal.append(bussIll.toString());
            retVal.append("</SmartPlatinaAssure>");
        } catch (Exception e) {
            // TODO: handle exception

            retVal.append("<?xml version='1.0' encoding='utf-8' ?><SmartPlatinaAssure>" + "<errCode>1</errCode>" + "<errorMessage>").append(e.getMessage()).append("</errorMessage></SmartPlatinaAssure>");
        }

    }


    /********************************** Calculations starts from here **********************************************************/

    private String[] getOutput(String sheetName,
                               SmartPlatinaAssureBean smartPlatinaAssureBean) {

        SmartPlatinaAssureBusinessLogic BIMAST = new SmartPlatinaAssureBusinessLogic(smartPlatinaAssureBean);
        bussIll = new StringBuilder();
        // String MinesOccuInterest = "0";
        int _year_F = 0;

        int year_F = 0;

        double yearlyBasePremiumPaid = 0, GuaranteedDeathBenefit = 0, GuaranteedMaturityBenefit = 0, cummulativePremium = 0,
                GuaranteedAddition = 0, GuaranteedSurrenderValue = 0, SpecialSurrenderValue = 0, cummulativeGuaranteedAddition = 0, sumAssuredOnDeath = 0;

        // From GUI Input

        int age = smartPlatinaAssureBean.getAge();
        int policyTerm = smartPlatinaAssureBean.getPolicyTerm();
        boolean isstaff = smartPlatinaAssureBean.getIsStaff();
        boolean isKerlaDiscount = smartPlatinaAssureBean.isKerlaDisc();
        double premPayingTerm = smartPlatinaAssureBean.getPremPayingTerm();
        double premiumAmt = BIMAST.getPremiumAmount(smartPlatinaAssureBean.getPremfreq(), smartPlatinaAssureBean.getPremiumAmt());

        double SAMFRate = BIMAST.setSAMFRate(age, smartPlatinaAssureBean.getPremPayingTerm());

        sumAssured = Double.valueOf(BIMAST.setSumAssured(
                smartPlatinaAssureBean.getPremiumAmt(), SAMFRate));

       /* String sumAssured = (commonForAllProd.getStringWithout_E((BIMAST.setSumAssured(
                smartPlatinaAssureBean.getPremiumAmt(), SAMFRate))));
        double SumAssured = Double.parseDouble(sumAssured);*/


        DefOccuInterest = "" + BIMAST.getDefenceOccuInterest(smartPlatinaAssureBean.getPremfreq(), sumAssured);


        double basicServiceTax = 0;
        double SBCServiceTax = 0;
        double KKCServiceTax = 0;
        double KerlaServiceTax = 0;
        double KeralaCessServiceTax = 0;

        double basicServiceTax_Occu = 0;
        double SBCServiceTax_Occu = 0;
        double KKCServiceTax_Occu = 0;
        double KerlaServiceTax_Occu = 0;
        double KeralaCessServiceTax_Occu = 0;

//		if(state != null && state.equals("KERALA"))
        if (isKerlaDiscount) {

            KerlaServiceTax = BIMAST.getServiceTax(premiumAmt, "KERALA");
            basicServiceTax = BIMAST.getServiceTax(premiumAmt, "basic");
            KeralaCessServiceTax = KerlaServiceTax - basicServiceTax;


            KerlaServiceTax_Occu = BIMAST.getServiceTax_DefenceOccupation(premiumAmt + Double.parseDouble(DefOccuInterest), "KERALA");
            basicServiceTax_Occu = BIMAST.getServiceTax_DefenceOccupation(premiumAmt + Double.parseDouble(DefOccuInterest), "basic");
            KeralaCessServiceTax_Occu = KerlaServiceTax_Occu - basicServiceTax_Occu;
        } else {
            basicServiceTax = BIMAST.getServiceTax(premiumAmt, "basic");
            SBCServiceTax = BIMAST.getServiceTax(premiumAmt, "SBC");
            KKCServiceTax = BIMAST.getServiceTax(premiumAmt, "KKC");


            basicServiceTax_Occu = BIMAST.getServiceTax_DefenceOccupation(premiumAmt + Double.parseDouble(DefOccuInterest), "basic");
            SBCServiceTax_Occu = BIMAST.getServiceTax_DefenceOccupation(premiumAmt + Double.parseDouble(DefOccuInterest), "SBC");
            KKCServiceTax_Occu = BIMAST.getServiceTax_DefenceOccupation(premiumAmt + Double.parseDouble(DefOccuInterest), "KKC");
        }


        double fyServiceTax = basicServiceTax + SBCServiceTax + KKCServiceTax + KerlaServiceTax;


        fyServiceTax_Occu = basicServiceTax_Occu + SBCServiceTax_Occu + KKCServiceTax_Occu + KerlaServiceTax_Occu;

        double fyPremiumWithST = premiumAmt + fyServiceTax;

        fyPremiumWithST_Occu = premiumAmt + Double.parseDouble(commonForAllProd.getRound(commonForAllProd.getStringWithout_E(fyServiceTax_Occu))) + Double.parseDouble(DefOccuInterest);


        double a = 0, b = 0;
        double basicServiceTaxSecondYear = 0;
        double SBCServiceTaxSecondYear = 0;
        double KKCServiceTaxSecondYear = 0;

        double kerlaServiceTaxSecondYear = 0;
        double KeralaCessServiceTaxSecondYear = 0;

        if (isKerlaDiscount) {
            kerlaServiceTaxSecondYear = BIMAST.getServiceTaxSecondYear(premiumAmt, "KERALA");
            basicServiceTaxSecondYear = BIMAST.getServiceTaxSecondYear(premiumAmt, "basic");
            KeralaCessServiceTaxSecondYear = kerlaServiceTaxSecondYear - basicServiceTaxSecondYear;
        } else {
            basicServiceTaxSecondYear = BIMAST.getServiceTaxSecondYear(premiumAmt, "basic");
            SBCServiceTaxSecondYear = BIMAST.getServiceTaxSecondYear(premiumAmt, "SBC");
            KKCServiceTaxSecondYear = BIMAST.getServiceTaxSecondYear(premiumAmt, "KKC");
        }


        double syServiceTax = basicServiceTaxSecondYear + SBCServiceTaxSecondYear + KKCServiceTaxSecondYear + kerlaServiceTaxSecondYear;

        double syPremiumWithST = premiumAmt + syServiceTax;


        double sumGuaranteedAddition = 0;


        int rowNumber = 0;
        try {
            for (int i = 0; i < policyTerm; i++) {
                rowNumber++;

                year_F = rowNumber;
                _year_F = year_F;
                // System.out.println("1. year_F "+year_F);
                bussIll.append("<policyYr").append(_year_F).append(">").append(_year_F).append("</policyYr").append(_year_F).append(">");

                yearlyBasePremiumPaid = BIMAST
                        .setYearlyBasePremiumPaid(year_F, smartPlatinaAssureBean.getPremiumAmt(), smartPlatinaAssureBean.getPremPayingTerm());
                // System.out.println("2.Total Base Premium Paid "+yearlyBasePremiumPaid);
                bussIll.append("<totBasePremPaid" + _year_F + ">"
                        + Math.round(Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd
                        .getStringWithout_E(yearlyBasePremiumPaid)))) + "</totBasePremPaid" + _year_F
                        + ">");

                cummulativePremium = cummulativePremium + yearlyBasePremiumPaid;
                // System.out.println("3.cummulativePremium "+cummulativePremium);
                bussIll.append("<cummulatvePrem" + _year_F + ">"
                        + Math.round(Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd
                        .getStringWithout_E(cummulativePremium)))) + "</cummulatvePrem" + _year_F
                        + ">");

                GuaranteedAddition = BIMAST.setGuaranteedAddition(
                        smartPlatinaAssureBean.getPremiumAmt(), cummulativePremium);
                cummulativeGuaranteedAddition = cummulativeGuaranteedAddition
                        + GuaranteedAddition;
                // System.out.println("4.Non Guarateed Death Benefit At_8_Percent "+GuaranteedAddition);

                //added by sujata on 28-11-2019
                sumGuaranteedAddition = sumGuaranteedAddition + GuaranteedAddition;
                //System.out.println("sumGuaranteedAddition "+sumGuaranteedAddition);
                //End


                bussIll.append("<guarntdAddtn" + _year_F + ">"
                        + Math.round(GuaranteedAddition) + "</guarntdAddtn" + _year_F + ">");

                GuaranteedDeathBenefit = BIMAST
                        .setGuaranteedDeathBenefit(sumAssured, smartPlatinaAssureBean.getPremiumAmt(),
                                cummulativeGuaranteedAddition,
                                cummulativePremium);
                // System.out.println("5.Guaranteed Death Benefit "+GuaranteedDeathBenefit);
                bussIll.append("<guarntdDeathBen" + _year_F + ">"
                        + Math.round(Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd
                        .getStringWithout_E(GuaranteedDeathBenefit)))) + "</guarntdDeathBen"
                        + _year_F + ">");

                if (year_F == 1) {

                    sumAssuredOnDeath = GuaranteedDeathBenefit - GuaranteedAddition;
                }
                GuaranteedMaturityBenefit = BIMAST
                        .setGuaranteedMaturityBenefit(sumAssured,
                                cummulativeGuaranteedAddition, year_F, policyTerm, isstaff, smartPlatinaAssureBean.getPremiumAmt());
                // System.out.println("6.Guaranteed Survival Benefit "+GuaranteedMaturityBenefit);
              /*  bussIll.append("<guarntdSurvivalBen" + _year_F + ">"
                        + Math.round(Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd
                        .getStringWithout_E(GuaranteedMaturityBenefit)))) + "</guarntdSurvivalBen"
                        + _year_F + ">");*/

                bussIll.append("<guarntdSurvivalBen" + _year_F + ">"
                        + commonForAllProd.getRoundUp(commonForAllProd.getRoundOffLevel2(commonForAllProd
                        .getStringWithout_E(GuaranteedMaturityBenefit))) + "</guarntdSurvivalBen"
                        + _year_F + ">");

                if (_year_F > smartPlatinaAssureBean.getPremPayingTerm()) {

                    a = a;
                } else {
                    if (smartPlatinaAssureBean.getPremfreq().equals("Monthly")) {

                        b = 12;
                        a = a + (premiumAmt * b);
                    } else {

                        b = 1;
                        a = a + (premiumAmt * b);
                    }
                }

                GuaranteedSurrenderValue = BIMAST
                        .setGuaranteedSurrenderValue(
                                cummulativeGuaranteedAddition,
                                a, year_F, policyTerm);
                // System.out.println("7.Guaranteed Surrender Value "+GuaranteedSurrenderValue);
				/*bussIll.append("<guarntdSurrndrVal" + _year_F + ">"
                        + Math.round(Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd
                        .getStringWithout_E(GuaranteedSurrenderValue)))) + "</guarntdSurrndrVal"
						+ _year_F + ">");*/

                bussIll.append("<guarntdSurrndrVal" + _year_F + ">"
                        + commonForAllProd.getRound(commonForAllProd
                        .getStringWithout_E(GuaranteedSurrenderValue)) + "</guarntdSurrndrVal"
                        + _year_F + ">");

                //Added by sujata on 28-11-2019
                SpecialSurrenderValue = Math.round(BIMAST
                        .getSpecialSurrender(
                                year_F, premPayingTerm, policyTerm, sumAssured, sumGuaranteedAddition));

                bussIll.append("<SpecialSurrenderValue" + _year_F + ">"
                        + Math.round(Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd
                        .getStringWithout_E(SpecialSurrenderValue)))) + "</SpecialSurrenderValue"
                        + _year_F + ">");

                //System.out.println("SpecialSurrenderValue "+SpecialSurrenderValue);

            }

            if (rb_proposerdetail_personaldetail_backdating_yes.isChecked()) {
                // "16-jan-2014")));
                BackDateinterest = commonForAllProd.getRoundUp(commonForAllProd
                        .getStringWithout_E(BIMAST.getBackDateInterest(
                                fyPremiumWithST,
                                btn_proposerdetail_personaldetail_backdatingdate
                                        .getText().toString())));

                BackDateinterestwithGST = commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(Double.parseDouble(BackDateinterest) + (Double.parseDouble(BackDateinterest) * prop.GSTforbackdate)));
            } else {
                BackDateinterestwithGST = "0";
            }


            fyPremiumWithST = fyPremiumWithST + Double.parseDouble(BackDateinterestwithGST);


            fyPremiumWithST_Occu = fyPremiumWithST_Occu + Double.parseDouble(BackDateinterestwithGST);

            MinesOccuInterest = "" + BIMAST.getMinesOccuInterest(smartPlatinaAssureBean.getPremfreq(), sumAssured);

            servicetax_MinesOccuInterest = ""
                    + BIMAST
                    .getServiceTaxMines(Double.parseDouble(MinesOccuInterest));

            MinesOccuInterest = "" + (Double.parseDouble(MinesOccuInterest) + Double.parseDouble(servicetax_MinesOccuInterest));


        } catch (Exception e) {
            e.printStackTrace();
        }


        return new String[]{
                (commonForAllProd.getRoundOffLevel2(commonForAllProd.getStringWithout_E(premiumAmt))),
                commonForAllProd.getStringWithout_E(fyServiceTax),
                commonForAllProd.getRoundOffLevel2(commonForAllProd.getStringWithout_E(fyPremiumWithST)),
                commonForAllProd.getStringWithout_E(syServiceTax),
                commonForAllProd.getRoundOffLevel2(commonForAllProd.getStringWithout_E(syPremiumWithST)),
                commonForAllProd.getRoundOffLevel2(commonForAllProd.getStringWithout_E(sumAssured)),
                commonForAllProd.getRoundOffLevel2(commonForAllProd.getStringWithout_E(GuaranteedMaturityBenefit)),
                commonForAllProd.getRoundOffLevel2(commonForAllProd.getStringWithout_E(SAMFRate)), BackDateinterestwithGST,
                commonForAllProd.getStringWithout_E(basicServiceTax),
                commonForAllProd.getStringWithout_E(SBCServiceTax),
                commonForAllProd.getStringWithout_E(KKCServiceTax),
                commonForAllProd.getStringWithout_E(basicServiceTaxSecondYear),
                commonForAllProd.getStringWithout_E(SBCServiceTaxSecondYear),
                commonForAllProd.getStringWithout_E(KKCServiceTaxSecondYear),
                MinesOccuInterest, servicetax_MinesOccuInterest,
                commonForAllProd.getStringWithout_E(KeralaCessServiceTax),
                commonForAllProd.getStringWithout_E(KeralaCessServiceTaxSecondYear), commonForAllProd.getStringWithout_E(sumAssuredOnDeath)
        };
    }

    /********************************** Validations starts here **********************************************************/

    // Maturity Validation
    private boolean valMaturityAge() {
        int Age = Integer.parseInt(spnr_Age.getSelectedItem().toString());
        int PolicyTerm = Integer.parseInt(spnrPolicyTerm.getSelectedItem()
                .toString());
        if ((Age + PolicyTerm) < 18 || (Age + PolicyTerm) > 75) {
            commonMethods.showMessageDialog(context, "Min. Maturity age is 18 years & Max.Maturity age is 75 years");
            commonMethods.setFocusable(btn_bi_smart_platina_assure_life_assured_date);
            btn_bi_smart_platina_assure_life_assured_date
                    .requestFocus();

            return false;

        } else
            return true;

    }

    // Premium Amount Validation
    private boolean valPremiumAmt() {
        String error = "";
        if (edt_premiumAmt.getText().toString().equals("")) {
            error = "Please enter Premium Amount in Rs.";

        } else if (Integer.parseInt(edt_premiumAmt.getText().toString()) < prop.minPremiumAmt) {
            error = "Premium Amount should not be less than Rs."
                    + currencyFormat.format(prop.minPremiumAmt);

        } else {
            if (!(Double.parseDouble(edt_premiumAmt.getText().toString()) % 1000 == 0)) {
                error = "Premium Amount should be multiple of 1000";

            }
        }
        if (!error.equals("")) {
            commonMethods.showMessageDialog(context, error);

            commonMethods.setFocusable(edt_premiumAmt);
            edt_premiumAmt.requestFocus();
            return false;
        } else
            return true;

    }

    // Life Assured Details Validation
    private boolean valLifeAssuredProposerDetail() {
        if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {
            if (lifeAssured_Title.equals("")
                    || lifeAssured_First_Name.equals("")
                    || lifeAssured_Last_Name.equals("")) {

                commonMethods.showMessageDialog(context, "Please Fill Name Detail For LifeAssured");
                if (lifeAssured_Title.equals("")) {
                    // apply focusable method
                    commonMethods.setFocusable(spnr_bi_smart_platina_assure_life_assured_title);
                    spnr_bi_smart_platina_assure_life_assured_title
                            .requestFocus();
                } else if (lifeAssured_First_Name.equals("")) {
                    commonMethods.setFocusable(edt_bi_smart_platina_assure_life_assured_first_name);
                    edt_bi_smart_platina_assure_life_assured_first_name
                            .requestFocus();
                } else {
                    commonMethods.setFocusable(edt_bi_smart_platina_assure_life_assured_last_name);
                    edt_bi_smart_platina_assure_life_assured_last_name
                            .requestFocus();
                }

                return false;
            } else if (lifeAssured_Title.equalsIgnoreCase("Mr.")
                    && gender.equalsIgnoreCase("Female")) {

                commonMethods.showMessageDialog(context, "Life Assured Title and Gender is not Valid");
                // apply focusable method
                commonMethods.setFocusable(spnr_bi_smart_platina_assure_life_assured_title);
                spnr_bi_smart_platina_assure_life_assured_title
                        .requestFocus();

                return false;

            } else if (lifeAssured_Title.equalsIgnoreCase("Ms.")
                    && gender.equalsIgnoreCase("Male")) {

                commonMethods.showMessageDialog(context, "Life Assured Title and Gender is not Valid");
                // apply focusable method
                commonMethods.setFocusable(spnr_bi_smart_platina_assure_life_assured_title);
                spnr_bi_smart_platina_assure_life_assured_title
                        .requestFocus();


                return false;

            } else if (lifeAssured_Title.equalsIgnoreCase("Mrs.")
                    && gender.equalsIgnoreCase("Male")) {
                commonMethods.showMessageDialog(context, "Life Assured Title and Gender is not Valid");
                // apply focusable method
                commonMethods.setFocusable(spnr_bi_smart_platina_assure_life_assured_title);
                spnr_bi_smart_platina_assure_life_assured_title
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
                    commonMethods.setFocusable(spnr_bi_smart_platina_assure_life_assured_title);

                } else if (lifeAssured_First_Name.equals("")) {

                    edt_bi_money_back_gold_proposer_first_name
                            .requestFocus();
                } else {
                    edt_bi_money_back_gold_proposer_last_name
                            .requestFocus();
                }

                return false;
            } else if (lifeAssured_Title.equalsIgnoreCase("Mr.")
                    && gender.equalsIgnoreCase("Female")) {

                commonMethods.showMessageDialog(context, "Life Assured Title and Gender is not Valid");
                commonMethods.setFocusable(spnr_bi_smart_platina_assure_life_assured_title);

                return false;

            } else if (lifeAssured_Title.equalsIgnoreCase("Ms.")
                    && gender.equalsIgnoreCase("Male")) {

                commonMethods.showMessageDialog(context, "Life Assured Title and Gender is not Valid");
                commonMethods.setFocusable(spnr_bi_smart_platina_assure_life_assured_title);

                return false;

            } else if (lifeAssured_Title.equalsIgnoreCase("Mrs.")
                    && gender.equalsIgnoreCase("Male")) {

                commonMethods.showMessageDialog(context, "Life Assured Title and Gender is not Valid");
                commonMethods.setFocusable(spnr_bi_smart_platina_assure_life_assured_title);
                return false;
            } else if (proposer_Title.equals("")
                    || proposer_First_Name.equals("")
                    || proposer_Last_Name.equals("")) {

                commonMethods.showMessageDialog(context, "Please Fill Name Detail For Proposer");
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
                commonMethods.showMessageDialog(context, "Please Select Proposer Gender");
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
            } else if (proposer_Title.equalsIgnoreCase("Mr.")
                    && gender_proposer.equalsIgnoreCase("Female")) {

                commonMethods.showMessageDialog(context, "Proposer Title and Gender is not Valid");
                commonMethods.setFocusable(spnr_bi_money_back_gold_proposer_title);
                spnr_bi_money_back_gold_proposer_title
                        .requestFocus();

                return false;

            } else if (proposer_Title.equalsIgnoreCase("Ms.")
                    && gender_proposer.equalsIgnoreCase("Male")) {

                commonMethods.showMessageDialog(context, "Proposer Title and Gender is not Valid");
                commonMethods.setFocusable(spnr_bi_money_back_gold_proposer_title);
                spnr_bi_money_back_gold_proposer_title
                        .requestFocus();

                return false;

            } else if (proposer_Title.equalsIgnoreCase("Mrs.")
                    && gender_proposer.equalsIgnoreCase("Male")) {

                commonMethods.showMessageDialog(context, "Proposer Title and Gender is not Valid");
                // apply focusable method
                commonMethods.setFocusable(spnr_bi_money_back_gold_proposer_title);
                spnr_bi_money_back_gold_proposer_title
                        .requestFocus();

                return false;

            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    // DOB validation
    private boolean valDob() {

        if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {

            if (lifeAssured_date_of_birth.equals("")
                    || lifeAssured_date_of_birth
                    .equalsIgnoreCase("select Date")) {

                commonMethods.showMessageDialog(context, "Please Select Valid Date Of Birth For LifeAssured");
                // apply focusable method
                commonMethods.setFocusable(btn_bi_smart_platina_assure_life_assured_date);
                btn_bi_smart_platina_assure_life_assured_date.requestFocus();

                return false;
            } else {
                return true;
            }
        } else
            return true;
    }

    private boolean valProposerDob() {

        if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("n")) {

            if (proposer_date_of_birth.equals("")
                    || proposer_date_of_birth.equalsIgnoreCase("Select Date")) {
                commonMethods.showMessageDialog(context,"Please Select Valid Date Of Birth For Proposer");
                commonMethods.setFocusable(btn_bi_money_back_gold_proposer_date);

                return false;
            } else {
                return true;
            }
        } else
            return true;
    }


    //Age Validation
    private boolean valAge() {
        int maxLimit;
        maxLimit = 60;

        if (Integer.parseInt(spnr_Age.getSelectedItem().toString()) > maxLimit) {
            commonMethods.showMessageDialog(context, "Enter age between 3 to " + maxLimit);
            spnr_Age.setSelection(0, false);
            btn_bi_smart_platina_assure_life_assured_date.setText("Select Date");
            btn_bi_smart_platina_assure_life_assured_date.requestFocus();
            lifeAssured_date_of_birth = "";

            return false;
        }
        return true;
    }

    /********************************** Validations ends here **********************************************************/

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
            commonMethods.dialogWarning(context, "Please fill Valid Date", false);
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
                    final_age = Integer.toString(age);
                    if (final_age.contains("-")) {
                        commonMethods.BICommonDialog(context, "Please fill Valid Bith Date");
                    } else {
                        if (3 <= age && age <= 60) {
                            if (Integer.parseInt(final_age) < 18) {
                                //ll_riders.setVisibility(View.GONE);
                                proposer_Is_Same_As_Life_Assured = "N";

                                tr_money_back_gold_proposer_detail2
                                        .setVisibility(View.VISIBLE);

                                btn_bi_smart_platina_assure_life_assured_date
                                        .setText(date);

                                spnr_Age.setSelection(getIndex(spnr_Age, final_age),
                                        false);
                                valMaturityAge();
                                lifeAssured_date_of_birth = commonMethods.getMMDDYYYYDatabaseDate(date + "");
                                if (!proposer_Backdating_BackDate.equals("")) {
                                    if (proposer_Backdating_WishToBackDate_Policy
                                            .equalsIgnoreCase("y")) {
                                        rb_proposerdetail_personaldetail_backdating_no
                                                .setChecked(true);
                                        ll_backdating1.setVisibility(View.GONE);
                                    }
                                    proposer_Backdating_BackDate = "";
                                    btn_proposerdetail_personaldetail_backdatingdate
                                            .setText("Select Date");
                                }

                                commonMethods.clearFocusable(btn_bi_smart_platina_assure_life_assured_date);
                                commonMethods.setFocusable(spnr_bi_money_back_gold_proposer_title);
                                spnr_bi_money_back_gold_proposer_title
                                        .requestFocus();
                            } else {
                                proposer_Is_Same_As_Life_Assured = "Y";
                                tr_money_back_gold_proposer_detail2
                                        .setVisibility(View.GONE);

                                btn_bi_smart_platina_assure_life_assured_date
                                        .setText(date);

                                spnr_Age.setSelection(getIndex(spnr_Age, final_age),
                                        false);
                                valMaturityAge();
                                lifeAssured_date_of_birth = commonMethods.getMMDDYYYYDatabaseDate(date + "");
                                if (!proposer_Backdating_BackDate.equals("")) {
                                    if (proposer_Backdating_WishToBackDate_Policy
                                            .equalsIgnoreCase("y")) {
                                        rb_proposerdetail_personaldetail_backdating_no
                                                .setChecked(true);
                                        ll_backdating1.setVisibility(View.GONE);
                                    }
                                    proposer_Backdating_BackDate = "";
                                    btn_proposerdetail_personaldetail_backdatingdate
                                            .setText("Select Date");
                                }

                                commonMethods.clearFocusable(btn_bi_smart_platina_assure_life_assured_date);

                                commonMethods.setFocusable(edt_proposerdetail_basicdetail_contact_no);
                                edt_proposerdetail_basicdetail_contact_no
                                        .requestFocus();

                            }
                        } else {
                            commonMethods.BICommonDialog(context, "Minimum Age should be 3 yrs and Maximum Age should be 60 yrs For LifeAssured");
                            btn_bi_smart_platina_assure_life_assured_date
                                    .setText("Select Date");
                            lifeAssured_date_of_birth = "";

                            commonMethods.clearFocusable(btn_bi_smart_platina_assure_life_assured_date);
                            commonMethods.setFocusable(btn_bi_smart_platina_assure_life_assured_date);

                            btn_bi_smart_platina_assure_life_assured_date
                                    .requestFocus();
                        }
                    }
                    break;
                case 5:
                    final_age = Integer.toString(age);
                    strProposerAge = final_age;
                    if (final_age.contains("-")) {
                        commonMethods.showMessageDialog(context, "Please fill Valid Bith Date");
                    } else {
                        if (18 <= age && age <= 100) {

                            btn_bi_money_back_gold_proposer_date.setText(date);

                            edt_bi_smart_wealth_builder_proposer_age.setText(strProposerAge);

                            proposer_date_of_birth = commonMethods.getMMDDYYYYDatabaseDate(date + "");

                            commonMethods.clearFocusable(btn_bi_money_back_gold_proposer_date);

                            commonMethods.setFocusable(edt_proposerdetail_basicdetail_contact_no);
                            edt_proposerdetail_basicdetail_contact_no
                                    .requestFocus();

                            // ageInYears
                            // .setSelection(getIndex(ageInYears, final_age));
                            // updatePolicyTermLabel();
                            // updateSAMFlabel();
                        } else {
                            commonMethods.showMessageDialog(context, "Minimum age should be 18 yrs for proposer");
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
                        commonMethods.dialogWarning(context, "Please Select Valid BackDating Date", true);
                        btn_proposerdetail_personaldetail_backdatingdate
                                .setText("Select Date");
                        proposer_Backdating_BackDate = "";
                    }

                    break;

                default:
                    break;
            }
        }

        if (proposer_Backdating_WishToBackDate_Policy.equalsIgnoreCase("y")
                && !proposer_Backdating_BackDate.equals("")) {

            int Proposerage = commonMethods.calculateMyAge(mYear, Integer.parseInt(mont),
                    Integer.parseInt(day), btn_bi_smart_platina_assure_life_assured_date.getText().toString());


            String str_final_age = Integer.toString(Proposerage);
            spnr_Age.setSelection(getIndex(spnr_Age, str_final_age), false);
            valMaturityAge();

        }

    }

    public void onClickLADob(View v) {
        initialiseDateParameter(lifeAssured_date_of_birth, 35);
        DIALOG_ID = 4;
        showDateDialog();
    }

    public void onClickDob(View v) {
        // setDefaultDate();
        DIALOG_ID = 5;
        showDateDialog();
    }

    public void onClickBackDating(View v) {
        String backdate = commonMethods.getMMDDYYYYDatabaseDate(proposer_Backdating_BackDate);
        initialiseDateParameter(backdate, 0);
        DIALOG_ID = 6;
        if (lifeAssured_date_of_birth != null
                && !lifeAssured_date_of_birth.equals("")) {
            showDateDialog();
        } else {
            commonMethods.dialogWarning(context, "Please select a LifeAssured DOB First", true);
        }
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

    private void setDefaultDate(int id) {
        Calendar present_date = Calendar.getInstance();
        present_date.add(Calendar.YEAR, -id);
        mDay = present_date.get(Calendar.DAY_OF_MONTH);
        mMonth = present_date.get(Calendar.MONTH);
        mYear = present_date.get(Calendar.YEAR);
    }

    private String getCurrentDate() {
        Calendar present_date = Calendar.getInstance();
        int mDay = present_date.get(Calendar.DAY_OF_MONTH);
        int mMonth = present_date.get(Calendar.MONTH) + 1;
        int mYear = present_date.get(Calendar.YEAR);

        return mDay + "-" + mMonth + "-" + mYear;

    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (v.getId() == edt_bi_smart_platina_assure_life_assured_first_name
                .getId()) {
            commonMethods.setFocusable(edt_bi_smart_platina_assure_life_assured_middle_name);
            edt_bi_smart_platina_assure_life_assured_middle_name
                    .requestFocus();
        } else if (v.getId() == edt_bi_smart_platina_assure_life_assured_middle_name
                .getId()) {
            commonMethods.setFocusable(edt_bi_smart_platina_assure_life_assured_last_name);
            edt_bi_smart_platina_assure_life_assured_last_name
                    .requestFocus();
        } else if (v.getId() == edt_bi_smart_platina_assure_life_assured_last_name
                .getId()) {
            commonMethods.setFocusable(btn_bi_smart_platina_assure_life_assured_date);
            btn_bi_smart_platina_assure_life_assured_date
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
            commonMethods.hideKeyboard(edt_premiumAmt, context);

        }
        return true;
    }

    private boolean createPdf() {
        try {

            // System.out.println("  "+maturityAge+
            // "  "+annPrem+" "+sumAssured);
            String BASE_FONT_BOLD = "Trebuchet MS B";
            Font titlefontSmall = FontFactory.getFont(BASE_FONT_BOLD, 12,
                    Font.UNDERLINE);

            Font sub_headerBold = new Font(Font.FontFamily.TIMES_ROMAN, 11,
                    Font.BOLD);

            Font headerBold = new Font(Font.FontFamily.TIMES_ROMAN, 8,
                    Font.BOLD);
            Font small_bold = new Font(Font.FontFamily.TIMES_ROMAN, 6,
                    Font.BOLD);
            Font small_normal = new Font(Font.FontFamily.TIMES_ROMAN, 6,
                    Font.NORMAL);
            Font normal_italic = new Font(Font.FontFamily.TIMES_ROMAN, 12,
                    Font.ITALIC);
            Font normal_bolditalic = new Font(Font.FontFamily.TIMES_ROMAN, 12,
                    Font.BOLDITALIC);
            Font normal1_BoldUnderline = new Font(Font.FontFamily.TIMES_ROMAN,
                    6, Font.UNDERLINE | Font.BOLD);
            Font normal_bold = new Font(Font.FontFamily.TIMES_ROMAN, 5,
                    Font.BOLD);
            Font normal = new Font(Font.FontFamily.TIMES_ROMAN, 5, Font.NORMAL);
            Font normal1_bold = new Font(Font.FontFamily.TIMES_ROMAN, 6,
                    Font.BOLD);
            Font normal1 = new Font(Font.FontFamily.TIMES_ROMAN, 6, Font.NORMAL);
            Font small_bold2 = new Font(Font.FontFamily.TIMES_ROMAN, 6,
                    Font.BOLD);
            mypath = mStorageUtils.createFileToAppSpecificDir(context, QuatationNumber + "BI.pdf");
            needAnalysispath = mStorageUtils.createFileToAppSpecificDir(context, "NA.pdf");
            // needAnalysis_Tabular_path = new File(folder, "NA_Tabular.pdf");
            newFile = mStorageUtils.createFileToAppSpecificDir(context, QuatationNumber + "P01.pdf");

            Rectangle rect = new Rectangle(594f, 792f);

            Document document = new Document(rect, 50, 50, 50, 50);
            // Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter pdf_writer = null;
            pdf_writer = PdfWriter.getInstance(document, new FileOutputStream(
                    mypath.getAbsolutePath()));

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Bitmap bitmap = BitmapFactory.decodeResource(getBaseContext()
                    .getResources(), R.drawable.sbi_life_logo);
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
            Image img_sbi_logo = Image.getInstance(stream.toByteArray());
            img_sbi_logo.setAlignment(Image.LEFT);
            img_sbi_logo.getSpacingAfter();
            img_sbi_logo.scaleToFit(80, 50);

            document.open();

            PdfPTable table = new PdfPTable(3);
            table.setWidths(new float[]{2.5f, 8.5f, 2f});
            table.setWidthPercentage(100);
            table.getDefaultCell().setPadding(15);

            PdfPCell cell;

            cell = new PdfPCell(new Phrase("\n", headerBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("SBI Life Insurance Co. Ltd",
                    normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("\n", headerBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setRowspan(9);
            cell.setBorder(Rectangle.NO_BORDER);

            table.addCell(cell);

            cell = new PdfPCell(img_sbi_logo);
            cell.setRowspan(3);
            cell.setBorder(Rectangle.NO_BORDER);

            table.addCell(cell);

            cell = new PdfPCell(
                    new Phrase(
                            "Registered & Corporate Office:, 'Natraj', M.V. Road & Western Express Highway Junction, Andheri (East),",
                            normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);

            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Mumbai - 400 069.",
                    normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);

            table.addCell(cell);

            cell = new PdfPCell(new Phrase("IRDAI Registration No. 111 | Website: www.sbilife.co.in | Email: info@sbilife.co.in | CIN: L99999MH2000PLC129113" + "\n" + "Toll Free: 1800 267 9090 (Between 9.00 am & 9.00 pm)" + "\n",
                    normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);

            table.addCell(cell);

            cell = new PdfPCell(new Phrase("\n", headerBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setRowspan(9);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(
                    new Phrase(
                            "Benefit Illustration (BI) : SBI Life -Smart Platina Assure (UIN :  111N126V04) |" + "\n" + "An Individual, Non-Linked, Non-Participating, Life Endowment Assurance Savings Product ", headerBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);

            table.addCell(cell);

            cell = new PdfPCell(new Phrase("\n", headerBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setRowspan(4);
            cell.setBorder(Rectangle.NO_BORDER);

            table.addCell(cell);

            cell = new PdfPCell(new Phrase("\n", normal));
            cell.setColspan(4);
            cell.setBorder(Rectangle.BOTTOM);
            cell.setBorderWidth(1.2f);
            table.addCell(cell);

            LineSeparator ls = new LineSeparator();
            Paragraph para3 = new Paragraph("Introduction", normal1_bold);
            para3.setAlignment(Element.ALIGN_CENTER);
            Paragraph benefit = new Paragraph(
                    "This benefit illustration is intended to show year-wise premiums payable and benefits under the policy.",
                    normal1);
            benefit.setAlignment(Element.ALIGN_LEFT | Element.ALIGN_JUSTIFIED);

			/*Paragraph para5 = new Paragraph(
					"Currently, the two rates of investment return as specified by IRDAI are 4% and 8% per annum.",
					normal1);
			para5.setAlignment(Element.ALIGN_LEFT | Element.ALIGN_JUSTIFIED);*/
            Paragraph para6 = new Paragraph(
                    "The main objective of the illustration is that the client is able to appreciate the features of the product and the flow of benefits in different circumstances with some level of quantification. For further information on the product and its benefits, please refer to the sales brochure and/or policy document.",
                    normal1);
            para6.setAlignment(Element.ALIGN_LEFT | Element.ALIGN_JUSTIFIED);
            Paragraph new_line = new Paragraph("\n");

            /*Paragraph para7 = new Paragraph("Statutory Warning", small_bold);*/
            para3.setAlignment(Element.ALIGN_LEFT);
			/*Paragraph para8 = new Paragraph(
					"Some benefits are guaranteed and some benefits are variable with returns based on the future performance of your Insurer carrying on life insurance business. If your policy offers guaranteed returns then these will be clearly marked �guaranteed� in the illustration table on this page. If your policy offers variable returns then the illustrations on this page will show two different rates of assumed future investment returns. These assumed rates of return are not guaranteed and they are not the upper or lower limits of what you might get back, as the value of your policy is dependent on a number of factors including future investment performance.�",
					normal1);*/
            benefit.setAlignment(Element.ALIGN_LEFT | Element.ALIGN_JUSTIFIED);

            PdfPTable table_proposer_name = new PdfPTable(4);
            // float[] columnWidths_table_proposer_name = { 2f, 2f, 1f, 2f };
            // table_proposer_name.setWidths(columnWidths_table_proposer_name);
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

            // inputTable here -1

            PdfPTable personalDetail_table = new PdfPTable(2);
            personalDetail_table.setWidths(new float[]{5f, 5f});
            personalDetail_table.setWidthPercentage(100f);
            personalDetail_table.setHorizontalAlignment(Element.ALIGN_LEFT);

            // 1st row
            cell = new PdfPCell(new Phrase(
                    "Proposer and Life Assured Details", normal1_bold));
            cell.setColspan(5);
            cell.setBorder(Rectangle.BOTTOM);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            personalDetail_table.addCell(cell);


            cell = new PdfPCell(new Phrase("Name of the Prospect/Policyholder", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            personalDetail_table.addCell(cell);

            if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {

                cell = new PdfPCell(new Phrase(name_of_life_assured, normal1));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                personalDetail_table.addCell(cell);

            } else if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("n")) {
                cell = new PdfPCell(new Phrase(name_of_proposer, normal1));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                personalDetail_table.addCell(cell);
            }

            // 2 row
            cell = new PdfPCell(new Phrase("Age (Years)", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            personalDetail_table.addCell(cell);


            if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {

                cell = new PdfPCell(new Phrase(ageAtEntry + "", normal1));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                personalDetail_table.addCell(cell);


            } else if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("n")) {
                cell = new PdfPCell(new Phrase(strProposerAge + "", normal1));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                personalDetail_table.addCell(cell);

            }

            cell = new PdfPCell(new Phrase(" Gender", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            personalDetail_table.addCell(cell);


            if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {
                cell = new PdfPCell(new Phrase(gender + "", normal1));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                personalDetail_table.addCell(cell);


            } else if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("n")) {
                cell = new PdfPCell(new Phrase(gender_proposer + "", normal1));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                personalDetail_table.addCell(cell);
            }

            cell = new PdfPCell(new Phrase("Name of the Life Assured", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(name_of_life_assured, normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);

            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Age (Years)", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(ageAtEntry, normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);

            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Gender", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(gender, normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);

            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Staff", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            personalDetail_table.addCell(cell);
            if (staffdiscount.equalsIgnoreCase("true")) {
                cell = new PdfPCell(new Phrase("Yes", normal1));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                personalDetail_table.addCell(cell);
            } else {
                cell = new PdfPCell(new Phrase("No", normal1));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                personalDetail_table.addCell(cell);
            }
            cell = new PdfPCell(new Phrase("State",
                    normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            personalDetail_table.addCell(cell);
           /* if (cb_kerladisc.isChecked()) {
                cell = new PdfPCell(new Phrase("Kerala", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                personalDetail_table.addCell(cell);
            } else {
                cell = new PdfPCell(new Phrase("Non Kerala", normal1));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                personalDetail_table.addCell(cell);

            }*/


            if (cb_kerladisc.isChecked()) {
                cell = new PdfPCell(new Phrase("", normal1));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                personalDetail_table.addCell(cell);
            } else {
                cell = new PdfPCell(new Phrase("", normal1));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                personalDetail_table.addCell(cell);

            }


            cell = new PdfPCell(new Phrase(gender, normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            //personalDetail_table.addCell(cell);
            // 3 row
            cell = new PdfPCell(new Phrase("Premium Payment Frequency ",
                    normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            // personalDetail_table.addCell(cell);


            // 4 row


            // 5 row
            cell = new PdfPCell(
                    new Phrase(smartPlatinaAssureBean.getPremfreq() + "", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            // personalDetail_table.addCell(cell);

            if (selStaffDisc.isChecked()) {
                cell = new PdfPCell(
                        new Phrase("Staff", normal1));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                //   personalDetail_table.addCell(cell);
            } else {
                cell = new PdfPCell(
                        new Phrase("Non Staff", normal1));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                // personalDetail_table.addCell(cell);
            }

            cell = new PdfPCell(
                    new Phrase("NA", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            //personalDetail_table.addCell(cell);

            // Basic Cover

            PdfPTable basicCover_table = new PdfPTable(2);
            basicCover_table.setWidths(new float[]{5f, 5f});
            basicCover_table.setWidthPercentage(100f);
            basicCover_table.setHorizontalAlignment(Element.ALIGN_LEFT);

            // 1st row
            cell = new PdfPCell(new Phrase("Policy Details", normal1_bold));
            cell.setColspan(5);
            cell.setBorder(Rectangle.BOTTOM);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            basicCover_table.addCell(cell);


            cell = new PdfPCell(new Phrase("Policy Option", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            basicCover_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Not Applicable", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            basicCover_table.addCell(cell);
            // 2 row
            cell = new PdfPCell(new Phrase("Policy Term (Years)", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            basicCover_table.addCell(cell);

            cell = new PdfPCell(new Phrase(policyTerm + "", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            basicCover_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Premium Payment Term (years)",
                    normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            basicCover_table.addCell(cell);

            cell = new PdfPCell(
                    new Phrase(premPayingTerm, normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            basicCover_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Mode / Frequency of Premium Payment",
                    normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            basicCover_table.addCell(cell);

            cell = new PdfPCell(
                    new Phrase(premPayingMode, normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            basicCover_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Amount of Installment Premium (Rs.) ",
                    normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            basicCover_table.addCell(cell);

            cell = new PdfPCell(
                    new Phrase((currencyFormat.format(Double
                            .parseDouble(premiumDetailFYBasicPremium))) + "", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            basicCover_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Sum Assured (Rs.)", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            basicCover_table.addCell(cell);

            cell = new PdfPCell(new Phrase((currencyFormat.format(Double
                    .parseDouble(basicCoverSumAssured))) + "", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            basicCover_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Sum Assured on Death (at inception of the policy) (Rs.)",
                    normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            basicCover_table.addCell(cell);

            String guaranteed_death_benefit = currencyFormat.format(Double
                    .parseDouble(commonForAllProd.getRoundUp(commonForAllProd
                            .getStringWithout_E(Double.parseDouble(prsObj
                                    .parseXmlTag(output, "SumAssuredOnDeath"))))));

            cell = new PdfPCell(
                    new Phrase(guaranteed_death_benefit, normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            basicCover_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Rate of Applicable Taxes",
                    normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            basicCover_table.addCell(cell);
            if (cb_kerladisc.isChecked()) {
                cell = new PdfPCell(
                        new Phrase(" 4.75% in the 1st policy year and 2.375% from 2nd policy year onwards", normal1));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                basicCover_table.addCell(cell);
            } else {
                cell = new PdfPCell(
                        new Phrase("4.5% in the 1st policy year and 2.25% from 2nd policy year onwards", normal1));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                basicCover_table.addCell(cell);

            }


            if (smartPlatinaAssureBean.getPremfreq().equalsIgnoreCase("Yearly")) {
                cell = new PdfPCell(new Phrase("Yearly Premium (in Rs)", normal1));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                //basicCover_table.addCell(cell);
            } else {
                cell = new PdfPCell(new Phrase("Monthly Premium (in Rs)", normal1));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                //basicCover_table.addCell(cell);

            }

            cell = new PdfPCell(new Phrase("Annualised Premium (in Rs.)", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            // basicCover_table.addCell(cell);

            // 3 row
           /* cell = new PdfPCell(new Phrase(prop.policyTerm + "", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            basicCover_table.addCell(cell);*/




            /*cell = new PdfPCell(
                    new Phrase(prop.premiumPayingTerm + "", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            basicCover_table.addCell(cell);*/


            cell = new PdfPCell(new Phrase((currencyFormat.format(Double
                    .parseDouble(premiumDetailFYBasicPremium))) + "", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            //basicCover_table.addCell(cell);

            cell = new PdfPCell(new Phrase((currencyFormat.format(Double
                    .parseDouble(basicCoverYearlyPremium))) + "", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            // basicCover_table.addCell(cell);

            // Premium Details

            // PdfPTable premDetail_table = new PdfPTable(7);
            // premDetail_table
            // .setWidths(new float[] { 5f, 5f, 5f, 5f, 5f, 5f, 5f });

            PdfPTable premDetail_table = new PdfPTable(4);
            premDetail_table.setWidths(new float[]{5f, 5f, 5f, 5f});
            premDetail_table.setWidthPercentage(100f);
            premDetail_table.setHorizontalAlignment(Element.ALIGN_LEFT);

            // 1st row
            cell = new PdfPCell(new Phrase("Premium Summary", normal1_bold));
            cell.setColspan(7);
            cell.setBorder(Rectangle.BOTTOM);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            premDetail_table.addCell(cell);

            // 2 row
            cell = new PdfPCell(new Phrase("", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);

            if (smartPlatinaAssureBean.getPremfreq().equalsIgnoreCase("Yearly")) {
                cell = new PdfPCell(new Phrase("Base Plan ", normal1));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                premDetail_table.addCell(cell);
            } else {
                cell = new PdfPCell(new Phrase("Base Plan ", normal1));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                premDetail_table.addCell(cell);
            }

            // cell = new PdfPCell(new Phrase("(a)Basic GST(Rs.)",
            // small_normal));
            // cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            // premDetail_table.addCell(cell);
            //
            // cell = new PdfPCell(new Phrase("(b)Swachh Bharat Cess(Rs.)",
            // small_normal));
            // cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            // premDetail_table.addCell(cell);
            //
            // cell = new PdfPCell(new Phrase("(c)Krishi Kalyan Cess(Rs.)",
            // small_normal));
            // cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            // premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Riders", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);

            if (smartPlatinaAssureBean.getPremfreq().equalsIgnoreCase("Yearly")) {
                cell = new PdfPCell(new Phrase("Total Installment Premium Rs.",
                        normal1));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                premDetail_table.addCell(cell);
            } else {
                cell = new PdfPCell(new Phrase("Total Installment Premium Rs.",
                        normal1));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                premDetail_table.addCell(cell);
            }

            // 3 row
            cell = new PdfPCell(new Phrase("Installment Premium without Applicable Taxes (Rs.)  ", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase((currencyFormat.format(Double
                    .parseDouble(premiumDetailFYBasicPremium))) + "", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);


            cell = new PdfPCell(new Phrase("Not Applicable", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);

            premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase((currencyFormat.format(Double
                    .parseDouble(premiumDetailFYBasicPremium))) + "",
                    normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);


            cell = new PdfPCell(new Phrase("Installment Premium with 1st Year Applicable Taxes (Rs.) ", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase((currencyFormat.format(Double
                    .parseDouble(premiumDetailFYPremiumWithServiceTax))) + "", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);


            cell = new PdfPCell(new Phrase("Not Applicable", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);

            premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase((currencyFormat.format(Double
                    .parseDouble(premiumDetailFYPremiumWithServiceTax))) + "",
                    normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);

            // 4 row

            cell = new PdfPCell(new Phrase("Installment Premium with Applicable Taxes 2nd Year onwards (Rs.) ", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase((currencyFormat.format(Double
                    .parseDouble(premiumDetailSYPremiumWithServiceTax))) + "", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);


            cell = new PdfPCell(new Phrase("Not Applicable", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);

            premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase((currencyFormat.format(Double
                    .parseDouble(premiumDetailSYPremiumWithServiceTax))) + "",
                    normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);

            PdfPTable Table_backdating_premium_with_service_tax = new PdfPTable(
                    2);
            Table_backdating_premium_with_service_tax.setWidthPercentage(100);

            PdfPCell cell_Backdate1 = new PdfPCell(new Paragraph(
                    "Backdating Interest", small_normal));
            cell_Backdate1.setPadding(5);
            PdfPCell cell_Backdate2 = new PdfPCell(new Paragraph("Rs ."
                    + commonForAllProd.getRound(commonForAllProd
                    .getStringWithout_E(Double.valueOf(BackdatingInt
                            .equals("") ? "0" : BackdatingInt))),
                    small_bold));
            cell_Backdate2.setPadding(5);
            cell_Backdate2.setHorizontalAlignment(Element.ALIGN_CENTER);

            // Table_backdating_premium_with_service_tax.addCell(cell_Backdate1);
            //Table_backdating_premium_with_service_tax.addCell(cell_Backdate2);

            Paragraph note = new Paragraph("Please Note: ",
                    normal1_BoldUnderline);
            para3.setAlignment(Element.ALIGN_LEFT);
            Paragraph note_1 = new Paragraph(
                    "1. The premiums can also be paid by giving standing instruction to your bank or you can pay through your credit card.    ",
                    normal1);
            // para4.setAlignment(Element.ALIGN_LEFT | Element.ALIGN_JUSTIFIED);

            Paragraph note_2 = new Paragraph(
                    "2. Applicable Taxes (including surcharge/cess etc), at the rate notified by the Central Government/ State Government / Union Territories of India from time to time and as per the provisions of the prevalent tax laws will be payable on premium as per the product features.",
                    normal1);
            // para4.setAlignment(Element.ALIGN_LEFT | Element.ALIGN_JUSTIFIED);

            Paragraph note_3 = new Paragraph(
                    "2. Applicable Taxes (including surcharge/cess etc), at the rate notified by the Central Government/ State Government / Union Territories of India from time to time and as per the provisions of the prevalent tax laws will be payable on premium/ or any other charges as per the product features.",
                    normal1);
            // para4.setAlignment(Element.ALIGN_LEFT | Element.ALIGN_JUSTIFIED);

            Paragraph termsCondition = new Paragraph(
                    "Other Terms and Conditions", normal1_bold);
            para3.setAlignment(Element.ALIGN_LEFT);
            Paragraph terms_1 = new Paragraph(
                    "1. The benefit calculation is based on the age herein indicated and as applicable for healthy individual.",
                    normal1);
            // para4.setAlignment(Element.ALIGN_LEFT | Element.ALIGN_JUSTIFIED);
            Paragraph terms_2 = new Paragraph(
                    "2. The Maturity/ Death Benefit amount are derived on the assumption that the policies are in-force.",
                    normal1);
            // para4.setAlignment(Element.ALIGN_LEFT | Element.ALIGN_JUSTIFIED);
            Paragraph terms_3 = new Paragraph(
                    "3. Benefits shown below are at the end of the year.", normal1);
            // para4.setAlignment(Element.ALIGN_LEFT | Element.ALIGN_JUSTIFIED);

            // Table here


            Paragraph surrender = new Paragraph("Notes : ", small_bold);
            para3.setAlignment(Element.ALIGN_LEFT);
            Paragraph surrender_value = new Paragraph(
                    "1. Annualized premium shall be the premium amount payable in a year chosen by the policyholder, excluding the taxes,  underwriting extra premiums and loading for modal premiums, if any. Refer sales literature for explanation of terms used in this illustration.",
                    normal1);
            Paragraph notes2 = new Paragraph(
                    "2. All Benefit amount are derived on the assumption that the policies are 'in-force'",
                    normal1);
            Paragraph notes3 = new Paragraph(
                    "3. In addition to Minimum Guaranteed Surrender Value, Surrender value of the Accrued Guaranteed Additions will also be paid.",
                    normal1);
            Paragraph notes4 = new Paragraph(
                    "4. The Guaranteed Surrender Value (GSV) will be equal to GSV factors multiplied by the total premiums paid. Surrender value of guaranteed additions is also added to this GSV.",
                    normal1);
            Paragraph notes5 = new Paragraph(
                    "5. In practice, the company may pay a surrender value which could be higher than the guaranteed surrender value.  The surrender value payable may be reviewed from time to time depending on company's experience of the various factors which impact the surrender values that may be paid. The surrender value would be higher of GSV or SSV.",
                    normal1);
            Paragraph notes6 = new Paragraph(
                    "6. For further information on the product and its benefits, please refer to the sales brochure and/or policy document. Further information will also be available on request.",
                    normal1);
            //  para4.setAlignment(Element.ALIGN_LEFT | Element.ALIGN_JUSTIFIED);

            Paragraph guarnteedSurrender = new Paragraph(
                    "Important :", small_bold);

            para3.setAlignment(Element.ALIGN_LEFT);
            Paragraph guarnteedSurrender_value = new Paragraph(
                    "You may receive a Welcome Call from our representative to confirm your proposal details like Date of Birth, Nominee Name, Address, Email ID, Sum Assured, Premium amount, Premium Payment Term etc.",
                    normal1);
            Paragraph imprtant2 = new Paragraph(
                    "You may have to undergo Medical tests based on our underwriting requirements.",
                    normal1);


            Paragraph specialSurrender = new Paragraph(
                    "Special Surrender Value", small_bold);
            para3.setAlignment(Element.ALIGN_LEFT);
            Paragraph specialSurrender_value = new Paragraph(
                    "The Special Surrender Value (SSV) are non guaranteed and will be equal to the SSV factors multiplied by the Paid-up Value on maturity. The Paid-up Value on maturity is equal to paid-up sum assured on maturity together with accrued guaranteed additions.",
                    normal1);
            //  para4.setAlignment(Element.ALIGN_LEFT | Element.ALIGN_JUSTIFIED);

            Paragraph policySurrender = new Paragraph(
                    "Company's Policy on Surrender", small_bold);
            para3.setAlignment(Element.ALIGN_LEFT);
            Paragraph policySurrender_value = new Paragraph(
                    "In practice, the company may pay a surrender value which could be higher than the guaranteed surrender value.  The surrender value payable may be reviewed from time to time depending on company's experience of the various factors which impact the surrender values that may be paid. The surrender value would be higher of GSV or SSV.",
                    normal1);
            // para4.setAlignment(Element.ALIGN_LEFT | Element.ALIGN_JUSTIFIED);

            Paragraph para9 = new Paragraph(
                    "I,_____________________________________________, having received the information with respect to the above, have understood the above statement before entering into the contract.",
                    normal1_bold);

            PdfPTable table4 = new PdfPTable(10);
            table4.setWidths(new float[]{0.9f, 2.1f, 0.8f, 1.7f, 4.7f, 3f,
                    0.8f, 1.7f, 2.9f, 3.5f});
            table4.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("Place : ", normal1_bold));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table4.addCell(cell);

            cell = new PdfPCell(new Phrase("_______________", normal1));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table4.addCell(cell);
            cell = new PdfPCell(new Phrase("Date : ", normal1_bold));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table4.addCell(cell);
            cell = new PdfPCell(new Phrase("___/___/_____", normal1));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table4.addCell(cell);
            cell = new PdfPCell(
                    new Phrase("IA/CIF/SP/Marketing Official's Signature : ",
                            normal1_bold));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table4.addCell(cell);
            cell = new PdfPCell(new Phrase("_______________________", normal1));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table4.addCell(cell);
            cell = new PdfPCell(new Phrase("Date : ", normal1_bold));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table4.addCell(cell);
            cell = new PdfPCell(new Phrase("___/___/_____", normal1));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table4.addCell(cell);
            cell = new PdfPCell(new Phrase("Policyholder's Signature : ",
                    normal1_bold));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table4.addCell(cell);
            cell = new PdfPCell(new Phrase("__________________________",
                    normal1));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table4.addCell(cell);

            document.add(table);

            // document.add(ls);

            // document.add(para4);

            //document.add(para5);

            //document.add(para7);
            //document.add(para8);

            document.add(new_line);

            document.add(table_proposer_name);
            document.add(new_line);
            document.add(para3);
            document.add(new_line);
            document.add(para6);
            document.add(new_line);
            // document.add(main_table);
            document.add(personalDetail_table);
            document.add(new_line);
            document.add(benefit);
            document.add(new_line);

            document.add(basicCover_table);
            document.add(new_line);
            document.add(premDetail_table);
            document.add(new_line);
            document.add(Table_backdating_premium_with_service_tax);
            document.add(new_line);
            document.add(note);
            document.add(note_1);
            document.add(note_2);
            // document.add(note_3);

            document.add(new_line);


            PdfPTable BI_Pdftable19 = new PdfPTable(1);
            BI_Pdftable19.setWidthPercentage(100);
            PdfPCell BI_Pdftable19_cell1 = new PdfPCell(new Paragraph(
                    "BENEFIT ILLUSTRATION FOR SBI Life - Smart Platina Assure",
                    small_bold));
            BI_Pdftable19_cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_Pdftable19_cell1.setPadding(5);

            PdfPCell amountinrs = new PdfPCell(new Paragraph(
                    "Amounts in Rupees",
                    small_bold));
            amountinrs.setHorizontalAlignment(Element.ALIGN_LEFT);
            amountinrs.setPadding(5);

            BI_Pdftable19.addCell(BI_Pdftable19_cell1);
            BI_Pdftable19.addCell(amountinrs);


            document.add(BI_Pdftable19);

            PdfPTable Table_BI_Header = new PdfPTable(8);
            Table_BI_Header.setWidthPercentage(100);
            PdfPCell cell_EndOfYear = new PdfPCell(new Paragraph("Policy Year",
                    small_bold2));
            cell_EndOfYear.setPadding(5);
            cell_EndOfYear.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell_EndOfYear.setRowspan(3);


            PdfPCell cell_YearlyPremiumPaid = new PdfPCell(new Paragraph(
                    " Annualized premium", small_bold2));
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
                    "Survival Benefits / Loyalty Additions ", small_bold2));
            cell_CummulativePremiumPaid.setPadding(5);
            cell_CummulativePremiumPaid
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            cell_CummulativePremiumPaid.setRowspan(2);
            PdfPCell cell_CummulativePremiumPaid2 = new PdfPCell(new Paragraph(
                    "Guaranteed Additions", small_bold2));
            cell_CummulativePremiumPaid2.setPadding(5);
            cell_CummulativePremiumPaid2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            cell_CummulativePremiumPaid2.setRowspan(2);
            PdfPCell cell_GuarantedDeathBenefit = new PdfPCell(new Paragraph(
                    "Maturity Benefit", small_bold2));

            cell_GuarantedDeathBenefit.setPadding(5);
            cell_GuarantedDeathBenefit
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            cell_GuarantedDeathBenefit.setRowspan(2);

            PdfPCell cell_GuarantedMaturityBenefit = new PdfPCell(
                    new Paragraph("Death benefit",
                            small_bold2));

            cell_GuarantedMaturityBenefit.setPadding(5);
            cell_GuarantedMaturityBenefit
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            cell_GuarantedMaturityBenefit.setRowspan(2);
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
            Table_BI_Header.addCell(cell_GuarantedDeathBenefit);
            Table_BI_Header.addCell(cell_GuarantedMaturityBenefit);
            Table_BI_Header.addCell(cell_GuarantedSurrenderValue);
            Table_BI_Header.addCell(cell_GuarantedSurrenderValue2);
            document.add(Table_BI_Header);

            float[] columnWidthsBI_Header1 = {1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f};
            // for (int i = 0; i < list_data.size(); i++) {
            for (int i = 0; i < Integer.parseInt(policyTerm); i++) {

                PdfPTable Table_BI_Header2 = new PdfPTable(8);

                Table_BI_Header2.setWidthPercentage(100);
                Table_BI_Header2.setWidths(columnWidthsBI_Header1);
                // PdfPCell cell_EndOfYear3 = new PdfPCell(new
                // Paragraph(list_data
                // .get(i).getPolicy_year(), small_bold2));
                PdfPCell cell_EndOfYear3 = new PdfPCell(new Phrase(
                        prsObj.parseXmlTag(output, "policyYr" + (i + 1)),
                        small_bold2));
                cell_EndOfYear3.setPadding(5);
                cell_EndOfYear3.setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell cell_AnnPrem = new PdfPCell(new Phrase(
                        currencyFormat.format(Double.parseDouble(prsObj
                                .parseXmlTag(output, "totBasePremPaid" + (i + 1)))),
                        small_bold2));
                cell_AnnPrem.setPadding(5);
                cell_AnnPrem.setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell cell_cummulativePremiumPaid = new PdfPCell(new Phrase(
                        "0", small_bold2));
                cell_cummulativePremiumPaid.setPadding(5);
                cell_cummulativePremiumPaid
                        .setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell cell_cummulativePremiumPaid2 = new PdfPCell(new Phrase(
                        currencyFormat.format(Double.parseDouble(prsObj
                                .parseXmlTag(output, "guarntdAddtn"
                                        + (i + 1)))), small_bold2));
                cell_cummulativePremiumPaid2.setPadding(5);
                cell_cummulativePremiumPaid2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell cell_guarantedDeathBenefit = new PdfPCell(new Phrase(
                        currencyFormat.format(Double.parseDouble(prsObj
                                .parseXmlTag(output, "guarntdSurvivalBen"
                                        + (i + 1)))), small_bold2));
                cell_guarantedDeathBenefit.setPadding(5);
                cell_guarantedDeathBenefit
                        .setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell cell_BenefitPayableAtMaturity = new PdfPCell(
                        new Phrase(currencyFormat.format(Double
                                .parseDouble(prsObj.parseXmlTag(output,
                                        "guarntdDeathBen" + (i + 1)))),
                                small_bold2));
                cell_BenefitPayableAtMaturity.setPadding(5);
                cell_BenefitPayableAtMaturity
                        .setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell cell_guarantedSurrenderValue = new PdfPCell(
                        new Phrase(currencyFormat.format(Double
                                .parseDouble(prsObj.parseXmlTag(output,
                                        "guarntdSurrndrVal" + (i + 1)))),
                                small_bold2));
                cell_guarantedSurrenderValue.setPadding(5);
                cell_guarantedSurrenderValue
                        .setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell cell_guarantedSurrenderValue2 = new PdfPCell(
                        new Phrase(currencyFormat.format(Double
                                .parseDouble(prsObj.parseXmlTag(output,
                                        "SpecialSurrenderValue" + (i + 1)))),
                                small_bold2));
                cell_guarantedSurrenderValue2.setPadding(5);
                cell_guarantedSurrenderValue2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);

                Table_BI_Header2.addCell(cell_EndOfYear3);
                Table_BI_Header2.addCell(cell_AnnPrem);
                Table_BI_Header2.addCell(cell_cummulativePremiumPaid);
                Table_BI_Header2.addCell(cell_cummulativePremiumPaid2);
                Table_BI_Header2.addCell(cell_guarantedDeathBenefit);
                Table_BI_Header2.addCell(cell_BenefitPayableAtMaturity);
                Table_BI_Header2.addCell(cell_guarantedSurrenderValue);
                Table_BI_Header2.addCell(cell_guarantedSurrenderValue2);

                document.add(Table_BI_Header2);
            }


            document.add(new_line);


            document.add(new_line);
            document.add(surrender);
            document.add(surrender_value);
            document.add(notes2);
            document.add(notes3);
            // document.add(notes4);
            // document.add(notes5);
            // document.add(notes6);

            //document.add(new_line);
            // document.add(guarnteedSurrender);
            //  document.add(guarnteedSurrender_value);
            //document.add(imprtant2);


            //document.add(imprtant3);

            //document.add(new_line);
            //document.add(specialSurrender);
            //document.add(specialSurrender_value);

            //document.add(new_line);
            //document.add(policySurrender);
            //document.add(policySurrender_value);

            //document.add(new_line);

            // document.add(para9);

            //document.add(new_line);
            document.add(new_line);

            PdfPTable BI_Pdftable191 = new PdfPTable(1);
            BI_Pdftable191.setWidthPercentage(100);
            PdfPCell BI_Pdftable191_cell1 = new PdfPCell(new Paragraph(
                    "Important:", small_bold));
            BI_Pdftable191_cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
            BI_Pdftable191_cell1.setPadding(5);

            BI_Pdftable191.addCell(BI_Pdftable191_cell1);
            document.add(BI_Pdftable191);

            PdfPTable BI_Pdftable_CompanysPolicySurrender2 = new PdfPTable(1);
            BI_Pdftable_CompanysPolicySurrender2.setWidthPercentage(100);
            PdfPCell BI_Pdftable_CompanysPolicySurrender2_cell = new PdfPCell(
                    new Paragraph(
                            "You may receive a Welcome Call from our representative to confirm your proposal details like Date of Birth, Nominee Name, Address, Email ID, Sum Assured, Premium amount, Premium Payment Term etc.",
                            small_normal));

            BI_Pdftable_CompanysPolicySurrender2_cell.setPadding(5);

            BI_Pdftable_CompanysPolicySurrender2
                    .addCell(BI_Pdftable_CompanysPolicySurrender2_cell);
            document.add(BI_Pdftable_CompanysPolicySurrender2);

            PdfPTable BI_Pdftable_CompanysPolicySurrender3 = new PdfPTable(1);
            BI_Pdftable_CompanysPolicySurrender3.setWidthPercentage(100);
            PdfPCell BI_Pdftable_CompanysPolicySurrender3_cell = new PdfPCell(
                    new Paragraph(
                            "You may have to undergo Medical tests based on our underwriting requirements.",
                            small_normal));

            BI_Pdftable_CompanysPolicySurrender3_cell.setPadding(5);

            BI_Pdftable_CompanysPolicySurrender3
                    .addCell(BI_Pdftable_CompanysPolicySurrender3_cell);
            document.add(BI_Pdftable_CompanysPolicySurrender3);

            PdfPTable imprtant3 = new PdfPTable(1);
            imprtant3.setWidthPercentage(100);
            PdfPCell imprtant3_cell = new PdfPCell(
                    new Paragraph(
                            "You have to submit Proof of source of Fund.",
                            small_normal));

            imprtant3_cell.setPadding(5);

            BI_Pdftable_CompanysPolicySurrender3
                    .addCell(imprtant3_cell);
            if (Double.parseDouble(basicCoverYearlyPremium) > 100000) {
                document.add(imprtant3);
            }

            // para4.setAlignment(Element.ALIGN_LEFT | Element.ALIGN_JUSTIFIED);


            PdfPTable BI_Pdftable_CompanysPolicySurrender5 = new PdfPTable(1);
            BI_Pdftable_CompanysPolicySurrender5.setWidthPercentage(100);
            PdfPCell BI_Pdftable_CompanysPolicySurrender5_cell = new PdfPCell(
                    new Paragraph(Company_policy_surrender_dec, small_normal));

            BI_Pdftable_CompanysPolicySurrender5_cell.setPadding(5);

            BI_Pdftable_CompanysPolicySurrender5
                    .addCell(BI_Pdftable_CompanysPolicySurrender5_cell);
            //document.add(BI_Pdftable_CompanysPolicySurrender5);

            document.add(new_line);
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
            document.add(new_line);
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
                        new Paragraph("Marketing official's Signature & Company Seal ", small_bold));
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

            return true;
        } catch (Exception e) {
            Log.i(getLocalClassName(), e.toString() + "Error in creating pdf");
            return false;

        }
    }


    /* Basic Details Method */

    private boolean valBasicDetail() {
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
        } /*else if (emailId.equals("")) {
            commonMethods.dialogWarning(context, "Please Fill Email Id", true);
            edt_proposerdetail_basicdetail_Email_id.requestFocus();
            return false;

        } else if (ConfirmEmailId.equals("")) {

            commonMethods.dialogWarning(context, "Please Fill Confirm Email Id", true);
            edt_proposerdetail_basicdetail_ConfirmEmail_id.requestFocus();
            return false;
        } else if (!ConfirmEmailId.equals(emailId)) {
            commonMethods.dialogWarning(context, "Email Id Does Not Match", true);
            return false;
        }*/ else if (!emailId.equals("")) {

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

    private void mobile_validation(String number) {
        if ((number.length() != 10)) {
            edt_proposerdetail_basicdetail_contact_no
                    .setError("Please provide correct 10-digit mobile number");
        } else if ((number.length() == 10)) {
        }
    }

    private void email_id_validation(String email_id) {

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


    private boolean valBackdate() {
        if (proposer_Backdating_WishToBackDate_Policy.equals("y")) {

            if (proposer_Backdating_BackDate.equals("")) {
                commonMethods.showMessageDialog(context, "Please Select Backdate ");
                // apply focusable method
                commonMethods.setFocusable(btn_proposerdetail_personaldetail_backdatingdate);
                btn_proposerdetail_personaldetail_backdatingdate
                        .requestFocus();

                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    private boolean TrueBackdate() {

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

                String back_date_fix = "01-06-2021";

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
                    error = "Please enter backdation date after 01-06-2021";
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


}
