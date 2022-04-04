package sbilife.com.pointofsale_bancaagency.products.smart_samriddhi;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.view.MotionEvent;
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
import sbilife.com.pointofsale_bancaagency.Element_TextView_BaseAdapter;
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

public class BI_SmartSamriddhiActivity extends AppCompatActivity implements
        OnEditorActionListener {
    private Spinner spnr_Age;
    private Spinner spnr_bi_smart_samriddhi_selGender;
    private Button btnSubmit;
    private Button btnback;


    String basicCoverTerm = "", basicCoverPremiumTerm = "",
            basicCoverSumAssured = "", sumAssuredOnDeath = "", basicCoverYearlyPremium = "",
            premiumDetailFYBasicPremium = "", premiumDetailFYServiceTax = "",
            premiumDetailFYPremiumWithServiceTax = "",
            premiumDetailSYBasicPremium = "", premiumDetailSYServiceTax = "",
            premiumDetailSYPremiumWithServiceTax = "", premiumDetailFYBasicPremium_without_tax = "";


    // for BI
    private StringBuilder inputVal;
    private String policyTerm = "";
    private String ageAtEntry = "";
    private String gender = "";

    String premPayingTerm = "", premPayingMode = "";
    private SmartSamriddhiBean smartSamriddhiBean;

    private EditText edt_premiumAmt;

    private Spinner spnrPremPayingTerm;
    private Spinner spnrPremPayingMode;
    private Spinner spnrPolicyTerm;

    private RadioButton rb_proposerdetail_personaldetail_backdating_yes;
    private RadioButton rb_proposerdetail_personaldetail_backdating_no;

    private LinearLayout ll_backdating1;

    private Button btn_proposerdetail_personaldetail_backdatingdate;
    private Button btn_MarketingOfficalDate;
    private Button btn_PolicyholderDate;

    private ImageButton Ibtn_signatureofMarketing;
    private ImageButton Ibtn_signatureofPolicyHolders;


    private Dialog d;
    private final int SIGNATURE_ACTIVITY = 1;
    private String latestImage = "";

    private List<M_BI_Smart_Samriddhi_Common_Adapter> list_data;

    private final int DATE_DIALOG_ID = 1;
    private int DIALOG_ID;
    private int mYear;
    private int mMonth;
    private int mDay;

    private Button btn_bi_smart_samriddhi_life_assured_date;

    private String QuatationNumber = "";
    private String planName = "";

    private String lifeAssured_Title = "";
    private String lifeAssured_First_Name = "";
    private String lifeAssured_Middle_Name = "";
    private String lifeAssured_Last_Name = "";
    private String name_of_life_assured = "";
    private String lifeAssured_date_of_birth = "";
    private String lifeAssuredAge = "";

    private String proposer_Backdating_WishToBackDate_Policy = "";
    private String proposer_Backdating_BackDate = "";

    private String BackdatingInt;
    private String BackDateinterestwithGST;
    // Spinner USed
    private Spinner spnr_bi_smart_samriddhi_life_assured_title;

    // edit Text Used
    private EditText edt_bi_smart_samriddhi_life_assured_first_name;
    private EditText edt_bi_smart_samriddhi_life_assured_middle_name;
    private EditText edt_bi_smart_samriddhi_life_assured_last_name;

    private String proposer_date_of_birth = "";

    // For Bi Dialog
    private ParseXML prsObj;
    private String name_of_proposer = "";
    private String name_of_person = "";
    private String place2 = "";
    private String date1 = "";
    private String date2 = "";
    private String agent_sign = "";
    private String proposer_sign = "";
    private final String proposer_Is_Same_As_Life_Assured = "Y";

    // Class Declaration
    private CommonForAllProd commonForAllProd;
    private SmartSamriddhiProperties prop;

    // Variable Declaration
    private DecimalFormat currencyFormat;

    private StringBuilder bussIll = null;
    private StringBuilder retVal = null;

    int PF = 0;
    private File mypath;


    /* Basic Details */

    private EditText edt_proposerdetail_basicdetail_contact_no;
    private EditText edt_proposerdetail_basicdetail_Email_id;
    private EditText edt_proposerdetail_basicdetail_ConfirmEmail_id;

    private String mobileNo = "";
    private String emailId = "";
    private String ConfirmEmailId = "";
    private boolean validationFla1 = false;
    private String ProposerEmailId = "";
    private Bitmap photoBitmap;
    private String Company_policy_surrender_dec = "";

    private Context context;
    private CommonMethods commonMethods;
    private StorageUtils mStorageUtils;

    private NeedAnalysisBIService NABIObj;
    private NA_CBI_bean na_cbi_bean;
    private File needAnalysispath, newFile;
    private String agentcode, agentMobile, agentEmail, userType;
    private int needAnalysis_flag = 0;
    private String na_input = null;
    private String na_output = null;
    private DatabaseHelper dbHelper;
    private String bankUserType = "", mode = "";
    /* parivartan changes */
    private String Check = "";
    private String product_Code, product_UIN, product_cateogory, product_type;
    private ImageButton imageButtonSmartSamriddhiProposerPhotograph;
    private String na_dob = "";
    private int flag = 0;

    private CheckBox cb_kerladisc;
    private String output;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.bi_smart_samriddhi_main);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        commonMethods = new CommonMethods();
        mStorageUtils = new StorageUtils();
        commonMethods.setActionbarLayout(this);
        context = this;
        dbHelper = new DatabaseHelper(context);
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
                    dbHelper.GetUserCode());

            agentMobile = SimpleCrypto.decrypt("SBIL",
                    dbHelper.GetMobileNo());
            agentEmail = SimpleCrypto.decrypt("SBIL",
                    dbHelper.GetEmailId());
            userType = SimpleCrypto.decrypt("SBIL",
                    dbHelper.GetUserType());

            /* parivartan changes */
            ProductInfo prodInfoObj = new ProductInfo(context);
            planName = getString(R.string.sbi_life_smart_samriddhi);
            product_Code = getString(R.string.sbi_life_smart_samriddhi_code);
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
                na_dob = intent.getStringExtra("custDOB");
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
        list_data = new ArrayList<>();
        AlertDialog.Builder showAlert = new AlertDialog.Builder(this);

        initialiseDate();


        prsObj = new ParseXML();


        prop = new SmartSamriddhiProperties();

        list_data = new ArrayList<>();

        // Variable Declaration
        currencyFormat = new DecimalFormat("##,##,##,###");

        rb_proposerdetail_personaldetail_backdating_yes = findViewById(R.id.rb_smart_samriddhi_backdating_yes);
        rb_proposerdetail_personaldetail_backdating_no = findViewById(R.id.rb_smart_samriddhi_backdating_no);

        ll_backdating1 = findViewById(R.id.ll_backdating1);
        btn_proposerdetail_personaldetail_backdatingdate = findViewById(R.id.btn_smart_samriddhi_backdatingdate);
        edt_premiumAmt = findViewById(R.id.et_bi_smart_samriddhi_premium_amt);
        btn_bi_smart_samriddhi_life_assured_date = findViewById(R.id.btn_bi_smart_samriddhi_life_assured_date);
        spnr_bi_smart_samriddhi_life_assured_title = findViewById(R.id.spnr_bi_smart_samriddhi_life_assured_title);
        edt_bi_smart_samriddhi_life_assured_first_name = findViewById(R.id.edt_bi_smart_samriddhi_life_assured_first_name);
        edt_bi_smart_samriddhi_life_assured_middle_name = findViewById(R.id.edt_bi_smart_samriddhi_life_assured_middle_name);
        edt_bi_smart_samriddhi_life_assured_last_name = findViewById(R.id.edt_bi_smart_samriddhi_life_assured_last_name);
        spnrPremPayingTerm = findViewById(R.id.spnr_bi_smart_samriddhi_premium_paying_term);
        spnrPremPayingMode = findViewById(R.id.spnr_bi_smart_samriddhi_premium_paying_mode);
        spnrPolicyTerm = findViewById(R.id.spnr_bi_smart_samriddhi_policyterm);
        spnr_bi_smart_samriddhi_selGender = findViewById(R.id.spnr_bi_smart_samriddhi_selGender);

        /*spnr_bi_smart_samriddhi_selGender.setClickable(false);
        spnr_bi_smart_samriddhi_selGender.setEnabled(false);*/


        spnr_Age = findViewById(R.id.spnr_bi_smart_samriddhi_age);

        edt_proposerdetail_basicdetail_contact_no = findViewById(R.id.edt_smart_samriddhi_contact_no);
        edt_proposerdetail_basicdetail_Email_id = findViewById(R.id.edt_smart_samriddhi_Email_id);
        edt_proposerdetail_basicdetail_ConfirmEmail_id = findViewById(R.id.edt_smart_samriddhi_ConfirmEmail_id);

        btnSubmit = findViewById(R.id.btn_bi_smart_samriddhi_btnSubmit);
        btnback = findViewById(R.id.btn_bi_smart_samriddhi_btnback);

        // UI elements
        // inputActivityHeader = (TextView)
        // findViewById(R.id.txt_input_activityheader);
        // Gender


        commonMethods.fillSpinnerValue(context, spnr_bi_smart_samriddhi_life_assured_title,
                Arrays.asList(getResources().getStringArray(R.array.arr_select_title)));

        //Gender
        String[] genderList = {"Male", "Female", "Third Gender"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, genderList);
        genderAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_bi_smart_samriddhi_selGender.setAdapter(genderAdapter);
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

        String[] policyTermArray = {"15"};

        ArrayAdapter<String> policyTermAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, policyTermArray);

        spnrPolicyTerm.setAdapter(policyTermAdapter);
        spnrPolicyTerm.setEnabled(false);

        String[] premPayingTermArray = {"7"};

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

        edt_bi_smart_samriddhi_life_assured_last_name
                .setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        final int DRAWABLE_RIGHT = 2;

                        return false;
                    }
                });

        edt_bi_smart_samriddhi_life_assured_first_name
                .setOnEditorActionListener(this);
        edt_bi_smart_samriddhi_life_assured_last_name
                .setOnEditorActionListener(this);
        edt_bi_smart_samriddhi_life_assured_middle_name
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

        commonMethods.setFocusable(edt_bi_smart_samriddhi_life_assured_first_name);

        edt_bi_smart_samriddhi_life_assured_first_name
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
            onClickLADob(btn_bi_smart_samriddhi_life_assured_date);
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
                            clearFocusable(rb_proposerdetail_personaldetail_backdating_yes);
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
                            // setDefaultDate();
                            ll_backdating1.setVisibility(View.GONE);

                            spnr_Age.setSelection(
                                    getIndex(spnr_Age, lifeAssuredAge), false);

                            valMaturityAge();
                            rb_proposerdetail_personaldetail_backdating_yes
                                    .setFocusable(false);

                            clearFocusable(rb_proposerdetail_personaldetail_backdating_no);
                            clearFocusable(rb_proposerdetail_personaldetail_backdating_yes);

                        }
                    }
                });

        spnr_bi_smart_samriddhi_life_assured_title
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long id) {

                        if (position == 0) {
                            lifeAssured_Title = "";
                        } else {
                            lifeAssured_Title = spnr_bi_smart_samriddhi_life_assured_title
                                    .getSelectedItem().toString();
                            clearFocusable(spnr_bi_smart_samriddhi_life_assured_title);
                            commonMethods.setFocusable(edt_bi_smart_samriddhi_life_assured_first_name);
                            edt_bi_smart_samriddhi_life_assured_first_name
                                    .requestFocus();
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {


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

                lifeAssured_First_Name = edt_bi_smart_samriddhi_life_assured_first_name
                        .getText().toString().trim();
                lifeAssured_Middle_Name = edt_bi_smart_samriddhi_life_assured_middle_name
                        .getText().toString().trim();
                lifeAssured_Last_Name = edt_bi_smart_samriddhi_life_assured_last_name
                        .getText().toString().trim();
                gender = spnr_bi_smart_samriddhi_selGender.getSelectedItem().toString();

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
                        && valBasicDetail() && valMaturityAge()
                        && valPremiumAmt() /*&& valDoYouBackdate()*/ && valBackdate() && TrueBackdate()) {
                    Date();


                    addListenerOnSubmit();
                    getInput(smartSamriddhiBean);

                    if (needAnalysis_flag == 0) {
                        Intent i = new Intent(getApplicationContext(),
                                Success.class);

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
                        d.dismiss();

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
                        imageButtonSmartSamriddhiProposerPhotograph
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
                new ColorDrawable(android.graphics.Color.BLACK));

        d.setContentView(R.layout.layout_smart_samriddhi_bi_grid);

        TextView tv_proposername = d
                .findViewById(R.id.tv_smart_samriddhi_proposername);
        TextView tv_proposal_number = d
                .findViewById(R.id.tv_smart_samriddhi_proposal_number);

        TextView tv_saral_swadhan_plus_distribution_channel = (TextView) d
                .findViewById(R.id.tv_saral_swadhan_plus_distribution_channel);
        tv_saral_swadhan_plus_distribution_channel.setText(userType);

        TextView tv_life_assured_name = (TextView) d
                .findViewById(R.id.tv_bi_smart_samriddhi_life_assured_name);

        TextView tv_life_age_at_entry = d
                .findViewById(R.id.tv_bi_smart_samriddhi_life_assured_age);
        TextView tv_life_age_at_entry2 = (TextView) d
                .findViewById(R.id.tv_bi_smart_samriddhi_life_assured_age2);

        TextView tv_life_assured_gender = d
                .findViewById(R.id.tv_bi_smart_samriddhi_life_assured_gender);
        TextView tv_life_assured_gender2 = (TextView) d
                .findViewById(R.id.tv_bi_smart_samriddhi_life_assured_gender2);

        TextView tv_premPaymentfrequency = d
                .findViewById(R.id.tv_bi_smart_samriddhi_life_assured_paying_mode);

        TextView tv_basic_cover_term = d
                .findViewById(R.id.tv_bi_smart_samriddhi_term_basic_cover);

        TextView tv_basic_cover_premPayment_term = d
                .findViewById(R.id.tv_bi_smart_samriddhi_premium_paying_term_basic_cover);

        TextView tv_basic_cover_sum_assured = d
                .findViewById(R.id.tv_bi_smart_samriddhi_sum_assured_basic_cover);


        TextView tv_bi_smart_samriddhi_sum_assured_on_death = (TextView) d
                .findViewById(R.id.tv_bi_smart_samriddhi_sum_assured_on_death);

        TextView tv_basic_cover_yearly_premium = (TextView) d
                .findViewById(R.id.tv_bi_smart_samriddhi_yearly_premium_basic_cover);

        TextView tv_basic_cover_yearly_premium2 = (TextView) d
                .findViewById(R.id.tv_bi_smart_samriddhi_yearly_premium_basic_cover2);

        TextView tv_basic_cover_yearly_premium3 = (TextView) d
                .findViewById(R.id.tv_bi_smart_samriddhi_yearly_premium_basic_cover3);

		/*TextView tv_basic_cover_annualised_premium = (TextView) d
				.findViewById(R.id.tv_bi_smart_samriddhi_annualised_premium_basic_cover);*/

        TextView tv_premium_detail_basic_premium_first_year = (TextView) d
                .findViewById(R.id.tv_bi_smart_samriddhi_basic_prem_first_year);

        TextView tv_premium_detail_basic_premium_first_year2 = (TextView) d
                .findViewById(R.id.tv_bi_smart_samriddhi_basic_prem_first_year2);

        TextView tv_bi_smart_samriddhi_life_assured_state = (TextView) d
                .findViewById(R.id.tv_bi_smart_samriddhi_life_assured_state);


        TextView tv_bi_smart_samriddhi_rate_applicabletax = (TextView) d
                .findViewById(R.id.tv_bi_smart_samriddhi_rate_applicabletax);



		/*TextView tv_premium_detail_service_tax_first_year = (TextView) d
                .findViewById(R.id.tv_bi_smart_samriddhi_service_tax_first_year);

		TextView tv_premium_detail_basic_premium_with_tax_first_year = (TextView) d
				.findViewById(R.id.tv_bi_smart_samriddhi_premium_with_service_tax_first_year);*/

        TextView tv_premium_detail_basic_premium_second_year = (TextView) d
                .findViewById(R.id.tv_bi_smart_samriddhi_basic_prem_second_year);

        TextView tv_premium_detail_basic_premium_second_year2 = (TextView) d
                .findViewById(R.id.tv_bi_smart_samriddhi_basic_prem_second_year2);

		/*TextView tv_premium_detail_service_tax_second_year = (TextView) d
                .findViewById(R.id.tv_bi_smart_samriddhi_service_tax_second_year);

		TextView tv_premium_detail_basic_premium_with_tax_second_year = (TextView) d
				.findViewById(R.id.tv_bi_smart_samriddhi_premium_with_service_tax_second_year);*/

        GridView gv_userinfo = d
                .findViewById(R.id.gv_smart_samriddhi_userinfo);
        TextView tv_backdatingint = d
                .findViewById(R.id.tv_backdatingint);
        gv_userinfo.setVerticalScrollBarEnabled(true);
        gv_userinfo.setSmoothScrollbarEnabled(true);

        final EditText edt_Policyholderplace = d
                .findViewById(R.id.edt_smart_samriddhi_Policyholderplace);

        final TextView txt_smart_samriddhi_proposer_name = d
                .findViewById(R.id.txt_smart_samriddhi_proposer_name);


        final CheckBox cb_statement = d
                .findViewById(R.id.cb_smart_samriddhi_statement);
        cb_statement.setChecked(false);

		/*TextView tv_bi_smart_samriddhi_basic_service_tax_first_year = (TextView) d
                .findViewById(R.id.tv_bi_smart_samriddhi_basic_service_tax_first_year);
		TextView tv_bi_smart_samriddhi_swachh_bharat_cess_first_year = (TextView) d
                .findViewById(R.id.tv_bi_smart_samriddhi_swachh_bharat_cess_first_year);
		TextView tv_bi_smart_samriddhi_krishi_kalyan_cess_first_year = (TextView) d
                .findViewById(R.id.tv_bi_smart_samriddhi_krishi_kalyan_cess_first_year);

		TextView tv_bi_smart_samriddhi_basic_service_tax_second_year = (TextView) d
                .findViewById(R.id.tv_bi_smart_samriddhi_basic_service_tax_second_year);
		TextView tv_bi_smart_samriddhi_swachh_bharat_cess_second_year = (TextView) d
                .findViewById(R.id.tv_bi_smart_samriddhi_swachh_bharat_cess_second_year);
		TextView tv_bi_smart_samriddhi_krishi_kalyan_cess_second_year = (TextView) d
				.findViewById(R.id.tv_bi_smart_samriddhi_krishi_kalyan_cess_second_year);*/

        TextView tv_premium_install_rider_type1 = (TextView) d
                .findViewById(R.id.tv_premium_install_rider_type1);
		/*TextView tv_mandatory_bi_smart_money_planner_yearly_premium_with_tax1 = (TextView) d
				.findViewById(R.id.tv_mandatory_bi_smart_money_planner_yearly_premium_with_tax1);*/
        TextView tv_premium_type = (TextView) d
                .findViewById(R.id.tv_premium_type);

        TableRow tr_second_year = d
                .findViewById(R.id.tr_second_year);


        TextView tv_ann_prem = (TextView) d
                .findViewById(R.id.tv_ann_prem);

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

        Button btn_proceed = d
                .findViewById(R.id.btn_smart_samriddhi_proceed);

        btn_MarketingOfficalDate = d
                .findViewById(R.id.btn_smart_samriddhi_MarketingOfficalDate);
        btn_PolicyholderDate = d
                .findViewById(R.id.btn_smart_samriddhi_PolicyholderDate);

        Ibtn_signatureofMarketing = d
                .findViewById(R.id.Ibtn_smart_samriddhi_signatureofMarketing);
        Ibtn_signatureofPolicyHolders = d
                .findViewById(R.id.Ibtn_smart_samriddhi_signatureofPolicyHolders);
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
        if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {
            txt_smart_samriddhi_proposer_name
                    .setText("I, "
                            + name_of_life_assured
                            + ", having received the information with respect to the above, have understood the above statement before entering into the contract.");
            txt_proposer_name_need_analysis
                    .setText("I, "
                            + name_of_life_assured
                            + ", have undergone the Need Analysis  after having reviewed the SBI Life Product options, I have opted for SBI LIFE - Smart Samriddhi.");

            tv_life_assured_name.setText(name_of_life_assured);
            tv_proposername.setText(name_of_life_assured);
        } else if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("n")) {
            txt_smart_samriddhi_proposer_name
                    .setText("I, "
                            + name_of_proposer
                            + ", having received the information with respect to the above, have understood the above statement before entering into the contract.");
            txt_proposer_name_need_analysis
                    .setText("I, "
                            + name_of_proposer
                            + ", have undergone the Need Analysis  after having reviewed the SBI Life Product options, I have opted for SBI LIFE - Smart Samriddhi.");

            tv_proposername.setText(name_of_proposer);
        }

        tv_proposal_number.setText(QuatationNumber);

        if (!date2.equals("")) {
            btn_PolicyholderDate.setText(commonMethods.getDDMMYYYYDate(date2));
        } else {
            date2 = commonMethods.getMMDDYYYYDatabaseDate(getCurrentDate());
            btn_PolicyholderDate.setText(getCurrentDate());
        }

        TextView textviewAGentStatement = d.findViewById(R.id.textviewAGentStatement);
        textviewAGentStatement.setText(commonMethods.getAgentDeclaration(context));

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

        imageButtonSmartSamriddhiProposerPhotograph = d
                .findViewById(R.id.imageButtonSmartSamriddhiProposerPhotograph);
        imageButtonSmartSamriddhiProposerPhotograph
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
                                && cb_statement_need_analysis.isChecked() && checkboxAgentStatement.isChecked()) {
                            latestImage = "agent";
                            windowmessagesgin();
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
                                && cb_statement_need_analysis.isChecked() && checkboxAgentStatement.isChecked()) {
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
            imageButtonSmartSamriddhiProposerPhotograph
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
                        // TODO Auto-generated method stub
                        if (isChecked) {
                            linearlayoutTrasactionModeParivartan
                                    .setVisibility(View.VISIBLE);
                        } else {
                            linearlayoutTrasactionModeParivartan
                                    .setVisibility(View.GONE);
                            /* String appointeeSignName = NeedAnalysisActivity.URN_NO
                                    + "_appointee.png";


                            File appointeeSignFile = new File(folder,
                                    appointeeSignName);
                            if (appointeeSignFile.exists()) {
                                appointeeSignFile.delete();
                            }

                           appointeeSign = "";
                            Ibtn_signatureofAppointee.setImageBitmap(null);

                            String thirdyPartySignName = NeedAnalysisActivity.URN_NO
                                    + "_thirdParty.png";

                            File thirdyPartySignFile = new File(folder,
                                    thirdyPartySignName);

                            if (thirdyPartySignFile.exists()) {
                                thirdyPartySignFile.delete();
                            }

                            thirdPartySign = "";
                            Ibtn_signatureofThirdParty.setImageBitmap(null);*/

                            String customerPhotoName = NeedAnalysisActivity.URN_NO
                                    + "_cust1Photo.jpg";
                            File customerPhotoFile = mStorageUtils.createFileToAppSpecificDir(context,
                                    customerPhotoName);
                            if (customerPhotoFile.exists()) {
                                customerPhotoFile.delete();
                            }

                            photoBitmap = null;
                            imageButtonSmartSamriddhiProposerPhotograph
                                    .setImageDrawable(getResources()
                                            .getDrawable(
                                                    R.drawable.focus_imagebutton_photo));


                        }
                    }
                });
        /* end */

        if (mode.equalsIgnoreCase("Manual")) {
            radioButtonTrasactionModeManual.setChecked(true);
        } else if (mode.equalsIgnoreCase("Parivartan")) {
            radioButtonTrasactionModeParivartan.setChecked(true);
        }

        if (!TextUtils.isEmpty(place2)) {
            edt_Policyholderplace.setText(place2);
        }

        btn_proceed.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                name_of_person = txt_smart_samriddhi_proposer_name.getText().toString();
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
                    String productCode = getString(R.string.sbi_life_smart_samriddhi_code);

                    na_cbi_bean = new NA_CBI_bean(QuatationNumber, agentcode,
                            "", userType, "", lifeAssured_Title,
                            lifeAssured_First_Name, lifeAssured_Middle_Name,
                            lifeAssured_Last_Name, planName,
                            commonForAllProd.getRound(commonForAllProd.getStringWithout_E(Double
                                    .valueOf(basicCoverSumAssured.equals("") ? "0"
                                            : basicCoverSumAssured))), commonForAllProd
                            .getRound(premiumDetailFYPremiumWithServiceTax), emailId,
                            mobileNo, agentEmail, agentMobile, na_input,
                            na_output, smartSamriddhiBean.getPremfreq(),
                            Integer.parseInt(policyTerm), smartSamriddhiBean.getPremPayingTerm(), productCode,
                            commonMethods.getDDMMYYYYDate(lifeAssured_date_of_birth),
                            commonMethods.getDDMMYYYYDate(proposer_date_of_birth), inputVal
                            .toString(), retVal.toString().replace(
                            bussIll.toString(), ""));

                    name_of_person = name_of_life_assured;

                    if (radioButtonTrasactionModeParivartan.isChecked()) {
                        mode = "Parivartan";
                    } else if (radioButtonTrasactionModeManual.isChecked()) {
                        mode = "Manual";
                    }

                    dbHelper.AddNeedAnalysisDashboardDetails(new ProductBIBean("",
                            QuatationNumber, planName, getCurrentDate(),
                            mobileNo, getCurrentDate(), dbHelper.GetUserCode(),
                            emailId, "", "", agentcode, "", userType, "",
                            lifeAssured_Title, lifeAssured_First_Name,
                            lifeAssured_Middle_Name, lifeAssured_Last_Name,
                            commonForAllProd.getRound(commonForAllProd.getStringWithout_E(Double
                                    .valueOf(basicCoverSumAssured.equals("") ? "0"
                                            : basicCoverSumAssured))), commonForAllProd
                            .getRound(premiumDetailFYPremiumWithServiceTax), agentEmail,
                            agentMobile, na_input, na_output,
                            smartSamriddhiBean.getPremfreq(), Integer
                            .parseInt(policyTerm), smartSamriddhiBean.getPremPayingTerm(), productCode,
                            commonMethods.getDDMMYYYYDate(lifeAssured_date_of_birth),
                            commonMethods.getDDMMYYYYDate(proposer_date_of_birth), "", mode, inputVal
                            .toString(), retVal.toString().replace(
                            bussIll.toString(), "")));
                    createPdf();
                    NABIObj.serviceHit(BI_SmartSamriddhiActivity.this,
                            na_cbi_bean, newFile, needAnalysispath.getPath(),
                            mypath.getPath(), name_of_person, QuatationNumber, mode);
                    d.dismiss();

                } else {

                    if (proposer_sign.equals("")
                            && !bankUserType.equalsIgnoreCase("Y")) {
                        dialog("Please Make Signature for Proposer ", true);
                        commonMethods.setFocusable(Ibtn_signatureofPolicyHolders);
                        Ibtn_signatureofPolicyHolders.requestFocus();
                    } else if (place2.equals("")) {
                        dialog("Please Fill Place Detail", true);
                        commonMethods.setFocusable(edt_Policyholderplace);
                        edt_Policyholderplace.requestFocus();

                    } else if (agent_sign.equals("")
                            && !bankUserType.equalsIgnoreCase("Y")) {
                        dialog("Please Make Signature for Sales Representative",
                                true);
                        commonMethods.setFocusable(Ibtn_signatureofMarketing);
                        Ibtn_signatureofMarketing.requestFocus();
                    } else if (!cb_statement.isChecked()) {
                        dialog("Please Tick on I Agree Clause ", true);
                        commonMethods.setFocusable(cb_statement);
                        cb_statement.requestFocus();
                    } else if (!checkboxAgentStatement.isChecked()) {
                        commonMethods.dialogWarning(context, "Please Tick on I Agree Clause ", true);
                        commonMethods.setFocusable(checkboxAgentStatement);
                        checkboxAgentStatement.requestFocus();
                    } else if (!radioButtonTrasactionModeParivartan.isChecked()
                            && !radioButtonTrasactionModeManual.isChecked()) {
                        dialog("Please Select Transaction Mode", true);
                        commonMethods.setFocusable(linearlayoutTrasactionModeParivartan);
                        linearlayoutTrasactionModeParivartan.requestFocus();
                    } else if (photoBitmap == null) {
                        dialog("Please Capture the Photo", true);
                        commonMethods.setFocusable(imageButtonSmartSamriddhiProposerPhotograph);
                        imageButtonSmartSamriddhiProposerPhotograph
                                .requestFocus();
                    }
                }

            }
        });

        btn_PolicyholderDate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {


                DIALOG_ID = 2;
                showDialog(DATE_DIALOG_ID);
            }
        });

        btn_MarketingOfficalDate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                DIALOG_ID = 3;
                showDialog(DATE_DIALOG_ID);

            }
        });
        if (cb_kerladisc.isChecked()) {
            tv_bi_smart_samriddhi_life_assured_state.setText("Kerala");
            tv_bi_smart_samriddhi_rate_applicabletax.setText("4.75% in the 1st policy year and 2.375% from 2nd policy year onwards");
        } else {
            tv_bi_smart_samriddhi_life_assured_state.setText("Non Kerala");
            tv_bi_smart_samriddhi_rate_applicabletax.setText("4.5% in the 1st policy year and 2.25% from 2nd policy year onwards");
        }
        String input = inputVal.toString();
        output = retVal.toString();

        ageAtEntry = prsObj.parseXmlTag(input, "age");
        tv_life_age_at_entry.setText(ageAtEntry + " Years");
        tv_life_age_at_entry2.setText(ageAtEntry + " Years");

        tv_life_assured_gender.setText(gender);
        tv_life_assured_gender2.setText(gender);

        premPayingMode = prsObj.parseXmlTag(input, "premFreq");
        tv_premPaymentfrequency.setText(premPayingMode);

        premPayingTerm = prsObj.parseXmlTag(input, "premPayingTerm");
        tv_basic_cover_premPayment_term.setText(premPayingTerm);

        policyTerm = prsObj.parseXmlTag(input, "policyTerm");
        tv_basic_cover_term.setText(policyTerm);

        if (premPayingMode.equals("Yearly")) {
            tv_ann_prem.setText("Annualized premium");
            //  tv_premium_type.setText("Yearly Premium (in Rs)");
            // tv_premium_install_rider_type1.setText("Yearly premium (in Rs.)");
            //tv_mandatory_bi_smart_money_planner_yearly_premium_with_tax1.setText("Yearly Premium, with \n" + "applicable taxes (in Rs.)");
        } else if (premPayingMode.equals("Monthly")) {
            tv_ann_prem.setText("Annualized premium");
            //tv_premium_type.setText("Monthly Premium (in Rs)");
            // tv_premium_install_rider_type1.setText("Monthly premium (in Rs.)");
            //	tv_mandatory_bi_smart_money_planner_yearly_premium_with_tax1.setText("Monthly Premium, with \n" + "applicable taxes (in Rs.)");
        }

        basicCoverYearlyPremium = ((int) Double.parseDouble(prsObj.parseXmlTag(input, "premiumAmount"))) + "";

        //basicCoverYearlyPremium = ((int) Double.parseDouble(prsObj.parseXmlTag(input, "premiumAmount"))) + "";
        // tv_basic_cover_annualised_premium.setText("" + getformatedThousandString(Integer.parseInt(basicCoverYearlyPremium)));

        basicCoverSumAssured = (int) Double.parseDouble(prsObj.parseXmlTag(
                output, "sumAssured")) + "";
        tv_basic_cover_sum_assured.setText(""
                + getformatedThousandString(Integer
                .parseInt(basicCoverSumAssured)));

        sumAssuredOnDeath = ((int) Double.parseDouble(prsObj
                .parseXmlTag(output, "SumAssuredOnDeath")))
                + "";
        tv_bi_smart_samriddhi_sum_assured_on_death.setText(""
                + getformatedThousandString(Integer
                .parseInt(sumAssuredOnDeath)));


        tr_second_year.setVisibility(View.VISIBLE);

        premiumDetailFYBasicPremium = ((int) Double.parseDouble(prsObj
                .parseXmlTag(output, "FYPrem"))) + "";

        premiumDetailFYBasicPremium_without_tax = ((int) Double.parseDouble(prsObj
                .parseXmlTag(output, "yearlyPrem"))) + "";
        tv_premium_detail_basic_premium_first_year.setText(""
                + premiumDetailFYBasicPremium);

        tv_premium_detail_basic_premium_first_year2.setText(""
                + premiumDetailFYBasicPremium);

        tv_basic_cover_yearly_premium.setText("" + getformatedThousandString(Integer
                .parseInt(premiumDetailFYBasicPremium_without_tax)));

        tv_basic_cover_yearly_premium2.setText("" + getformatedThousandString(Integer
                .parseInt(premiumDetailFYBasicPremium_without_tax)));

        tv_basic_cover_yearly_premium3.setText("" + getformatedThousandString(Integer
                .parseInt(premiumDetailFYBasicPremium_without_tax)));

        premiumDetailFYServiceTax = ((int) Double.parseDouble(prsObj
                .parseXmlTag(output, "FYServTax"))) + "";
		/*tv_premium_detail_service_tax_first_year.setText(""
				+ premiumDetailFYServiceTax);*/

        premiumDetailFYPremiumWithServiceTax = ((int) (Double
                .parseDouble(premiumDetailFYBasicPremium.equals("")
                        || (premiumDetailFYBasicPremium.equals(null)) ? "0"
                        : premiumDetailFYBasicPremium) + Double
                .parseDouble(premiumDetailFYServiceTax.equals("")
                        || (premiumDetailFYServiceTax.equals(null)) ? "0"
                        : premiumDetailFYServiceTax)))
                + "";
		/*tv_premium_detail_basic_premium_with_tax_first_year.setText(""
				+ premiumDetailFYPremiumWithServiceTax);*/

        // Amit changes start- 23-5-2016
        // basicServiceTax = prsObj.parseXmlTag(output, "basicServiceTax");
        String basicServiceTax = prsObj.parseXmlTag(output, "FYServTax");

		/*tv_bi_smart_samriddhi_basic_service_tax_first_year
                .setText(""
                        + commonForAllProd.getRound(commonForAllProd.getStringWithout_E(Double
                        .valueOf(basicServiceTax.equals("") ? "0"
										: basicServiceTax))));*/

        String SBCServiceTax = prsObj.parseXmlTag(output, "SBCServiceTax");

		/*tv_bi_smart_samriddhi_swachh_bharat_cess_first_year
                .setText(""
                        + commonForAllProd.getRound(commonForAllProd
                        .getStringWithout_E(Double
                                .valueOf(SBCServiceTax.equals("") ? "0"
												: SBCServiceTax))));*/

        String KKCServiceTax = prsObj.parseXmlTag(output, "KKCServiceTax");

		/*tv_bi_smart_samriddhi_krishi_kalyan_cess_first_year
                .setText(""
                        + commonForAllProd.getRound(commonForAllProd
                        .getStringWithout_E(Double
                                .valueOf(KKCServiceTax.equals("") ? "0"
												: KKCServiceTax))));*/
        // Amit changes end- 23-5-2016

        premiumDetailSYBasicPremium = ((int) Double.parseDouble(prsObj
                .parseXmlTag(output, "SYPrem"))) + "";
        tv_premium_detail_basic_premium_second_year.setText(""
                + premiumDetailSYBasicPremium);

        tv_premium_detail_basic_premium_second_year2.setText(""
                + premiumDetailSYBasicPremium);

        premiumDetailSYServiceTax = ((int) Double.parseDouble(prsObj
                .parseXmlTag(output, "SYServTax"))) + "";
		/*tv_premium_detail_service_tax_second_year.setText(""
				+ premiumDetailSYServiceTax);*/

        premiumDetailSYPremiumWithServiceTax = ((int) (Double
                .parseDouble(premiumDetailSYBasicPremium.equals("") ? "0"
                        : premiumDetailSYBasicPremium) + Double
                .parseDouble(premiumDetailSYServiceTax.equals("") ? "0"
                        : premiumDetailSYServiceTax)))
                + "";
		/*tv_premium_detail_basic_premium_with_tax_second_year.setText(""
				+ premiumDetailSYPremiumWithServiceTax);*/

        // Amit changes start- 23-5-2016
        // basicServiceTaxSecondYear = prsObj.parseXmlTag(output,
        // "basicServiceTaxSecondYear");
        String basicServiceTaxSecondYear = prsObj.parseXmlTag(output, "SYServTax");

		/*tv_bi_smart_samriddhi_basic_service_tax_second_year
                .setText(""
                        + commonForAllProd.getRound(commonForAllProd
                        .getStringWithout_E(Double
                                .valueOf(basicServiceTaxSecondYear
                                        .equals("") ? "0"
												: basicServiceTaxSecondYear))));*/

        String SBCServiceTaxSecondYear = prsObj.parseXmlTag(output,
                "SBCServiceTaxSecondYear");

		/*tv_bi_smart_samriddhi_swachh_bharat_cess_second_year
                .setText(""
                        + commonForAllProd.getRound(commonForAllProd
                        .getStringWithout_E(Double
                                .valueOf(SBCServiceTaxSecondYear
                                        .equals("") ? "0"
												: SBCServiceTaxSecondYear))));*/

        String KKCServiceTaxSecondYear = prsObj.parseXmlTag(output,
                "KKCServiceTaxSecondYear");

		/*tv_bi_smart_samriddhi_krishi_kalyan_cess_second_year
                .setText(""
                        + commonForAllProd.getRound(commonForAllProd
                        .getStringWithout_E(Double
                                .valueOf(KKCServiceTaxSecondYear
                                        .equals("") ? "0"
												: KKCServiceTaxSecondYear))));*/
        // Amit changes end- 23-5-2016

        BackdatingInt = prsObj.parseXmlTag(output, "backdateInt");

        tv_backdatingint.setText(" Rs."
                + commonForAllProd.getRound(commonForAllProd
                .getStringWithout_E(Double.valueOf(BackdatingInt
                        .equals("") ? "0" : BackdatingInt))));

		/*TextView tv_Company_policy_surrender_dec = (TextView) d
				.findViewById(R.id.tv_Company_policy_surrender_dec);*/

        Company_policy_surrender_dec = "Your SBI Life-Smart Samriddhi (UIN - 111N097V03) is a "
                + " Limited "
                + " premium policy, for which your first year"
                + premPayingMode
                + " Premium is Rs "
                + commonMethods.getformatedThousandString(Integer.parseInt(premiumDetailFYPremiumWithServiceTax))
                + " .Your Policy Term is "
                + policyTerm
                + " years"
                + " , Premium Payment Term is "
                + premPayingTerm
                + " years"
                + " and Basic Sum Assured is Rs. " +

                commonMethods.getformatedThousandString(Integer.parseInt(basicCoverSumAssured));

        //tv_Company_policy_surrender_dec.setText(Company_policy_surrender_dec);


        for (int i = 1; i <= Integer.parseInt(policyTerm); i++) {

            String end_of_year = prsObj
                    .parseXmlTag(output, "policyYr" + i + "");
            String yearly_basic_premium = ((int) Double.parseDouble(prsObj
                    .parseXmlTag(output, "totBasePremPaid" + i + ""))) + "";
            String SurvivalBenefits = ((int) Double.parseDouble(prsObj
                    .parseXmlTag(output, "guarntdSurvivalBen" + i + "")))
                    + "";

            String OtherBenefitsifAny = ((int) Double.parseDouble(prsObj
                    .parseXmlTag(output, "guarntdAddtn" + i + "")))
                    + "";
            String guaranteed_maturity_benefit = ((int) Double
                    .parseDouble(prsObj.parseXmlTag(output,
                            "guarntdSurvivalBen" + i + "")))
                    + "";
            //String cumulative_premium = "";
            // String guaranteed_addition = ((int) Double.parseDouble(prsObj
            // .parseXmlTag(output, "guarntdAddtn" + i + ""))) + "";
            String guaranteed_death_benefit = ((int) Double.parseDouble(prsObj
                    .parseXmlTag(output, "guarntdDeathBen" + i + "")))
                    + "";

            String guaranteed_surrender_value = ((int) Double
                    .parseDouble(prsObj.parseXmlTag(output,
                            "guarntdSurrndrVal" + i + "")))
                    + "";
            String nonGuaranSurrenderValue = ((int) Double
                    .parseDouble(prsObj.parseXmlTag(output,
                            "nonGuarntdSurrndrVal" + i + "")))
                    + "";

            list_data.add(new M_BI_Smart_Samriddhi_Common_Adapter(end_of_year,
                    yearly_basic_premium, "0", OtherBenefitsifAny,
                    guaranteed_maturity_benefit, guaranteed_death_benefit,
                    guaranteed_surrender_value, nonGuaranSurrenderValue));
        }

        Adapter_BI_SmartSamriddhiCommonGrid adapter = new Adapter_BI_SmartSamriddhiCommonGrid(
                this, list_data);
        gv_userinfo.setAdapter(adapter);

        GridHeight gh = new GridHeight();
        gh.getheight(gv_userinfo, policyTerm);

		/*for (int i = 1; i <= Integer.parseInt(policyTerm); i++) {

            String end_of_year = prsObj
                    .parseXmlTag(output, "policyYr" + i + "");
            String yearly_basic_premium = ((int) Double.parseDouble(prsObj
                    .parseXmlTag(output, "totBasePremPaid" + i + ""))) + "";
            String cumulative_premium = ((int) Double.parseDouble(prsObj
                    .parseXmlTag(output, "cummulatvePrem" + i + ""))) + "";
            String guaranteed_addition = ((int) Double.parseDouble(prsObj
                    .parseXmlTag(output, "guarntdAddtn" + i + ""))) + "";
            String guaranteed_death_benefit = ((int) Double.parseDouble(prsObj
                    .parseXmlTag(output, "guarntdDeathBen" + i + ""))) + "";
            String guaranteed_maturity_benefit = ((int) Double
                    .parseDouble(prsObj.parseXmlTag(output,
                            "guarntdSurvivalBen" + i + "")))
                    + "";
            String guaranteed_surrender_value = ((int) Double
                    .parseDouble(prsObj.parseXmlTag(output, "guarntdSurrndrVal"
                            + i + "")))
                    + "";

            list_data.add(new M_BI_Smart_Samriddhi_Adapter(
                    end_of_year, yearly_basic_premium, cumulative_premium,
                    guaranteed_addition, guaranteed_death_benefit,
                    guaranteed_maturity_benefit, guaranteed_surrender_value));
        }

        Adapter_BI_SmartSamriddhiGrid adapter = new Adapter_BI_SmartSamriddhiGrid(
                this, list_data);
        gv_userinfo.setAdapter(adapter);

        GridHeight gh = new GridHeight();
		gh.getheight(gv_userinfo,policyTerm);*/

        d.show();

    }

    private void fillSpinnerValue(Spinner spinner, List<String> value_list) {

        Element_TextView_BaseAdapter retd_adapter = new Element_TextView_BaseAdapter(
                BI_SmartSamriddhiActivity.this, value_list);
        spinner.setAdapter(retd_adapter);

    }

    private void initialiseDate() {
        Calendar calender = Calendar.getInstance();
        mYear = calender.get(Calendar.YEAR);
        mMonth = calender.get(Calendar.MONTH);
        mDay = calender.get(Calendar.DAY_OF_MONTH);

    }

    private void Date() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        // formattedDate have current date/time
        // Toast.makeText(this, formattedDate, Toast.LENGTH_SHORT).show();
        c.add(Calendar.DATE, 30);
        String expiredDate = df.format(c.getTime());
    }


    // Store user input in Bean object
    private void addListenerOnSubmit() {
        smartSamriddhiBean = new SmartSamriddhiBean();
        // Insert data entered by user in an object


        if (cb_kerladisc.isChecked()) {
            smartSamriddhiBean.setKerlaDisc(true);
        } else {
            smartSamriddhiBean.setKerlaDisc(false);
        }

        smartSamriddhiBean.setAge(Integer.parseInt(spnr_Age.getSelectedItem()
                .toString()));
        smartSamriddhiBean.setGender(gender);
        smartSamriddhiBean.setPolicyTerm(Integer.parseInt(spnrPolicyTerm
                .getSelectedItem().toString()));
        smartSamriddhiBean.setPremPayingTerm(Integer
                .parseInt(spnrPremPayingTerm.getSelectedItem().toString()));
        smartSamriddhiBean.setPremPayingMode(spnrPremPayingMode
                .getSelectedItem().toString());
        smartSamriddhiBean.setPremiumAmt(Double.parseDouble(edt_premiumAmt
                .getText().toString()));

        // Show Smart Samriddhi Output Screen
        showSmartSamriddhiOutputPg(smartSamriddhiBean);
    }

    private void getInput(SmartSamriddhiBean smartsamriddhiBean) {
        inputVal = new StringBuilder();

        int age = smartSamriddhiBean.getAge();
        int policyTerm = smartsamriddhiBean.getPolicyTerm();
        double premAmount = smartsamriddhiBean.getPremiumAmount();
        String PremPayingMode = smartsamriddhiBean.getPremPayingMode();
        int premPayingTerm = smartsamriddhiBean.getPremPayingTerm();

        String LifeAssured_title = spnr_bi_smart_samriddhi_life_assured_title
                .getSelectedItem().toString();
        String LifeAssured_firstName = edt_bi_smart_samriddhi_life_assured_first_name
                .getText().toString();
        String LifeAssured_middleName = edt_bi_smart_samriddhi_life_assured_middle_name
                .getText().toString();
        String LifeAssured_lastName = edt_bi_smart_samriddhi_life_assured_last_name
                .getText().toString();
        String LifeAssured_DOB = btn_bi_smart_samriddhi_life_assured_date.getText().toString();

        String proposer_title = "";
        String proposer_firstName = "";
        String proposer_middleName = "";
        String proposer_lastName = "";
        String proposer_DOB = "";
        String proposer_age = "";
        String proposer_gender = "";

        inputVal.append("<?xml version='1.0' encoding='utf-8' ?><smartsamriddhi>");
        inputVal.append("<LifeAssured_title>").append(LifeAssured_title).append("</LifeAssured_title>");
        inputVal.append("<LifeAssured_firstName>").append(LifeAssured_firstName).append("</LifeAssured_firstName>");
        inputVal.append("<LifeAssured_middleName>").append(LifeAssured_middleName).append("</LifeAssured_middleName>");
        inputVal.append("<LifeAssured_lastName>").append(LifeAssured_lastName).append("</LifeAssured_lastName>");
        inputVal.append("<LifeAssured_DOB>").append(LifeAssured_DOB).append("</LifeAssured_DOB>");
        inputVal.append("<LifeAssured_age>").append(age).append("</LifeAssured_age>");
        inputVal.append("<gender>").append(gender).append("</gender>");

        inputVal.append("<proposer_title>").append(proposer_title).append("</proposer_title>");
        inputVal.append("<proposer_firstName>").append(proposer_firstName).append("</proposer_firstName>");
        inputVal.append("<proposer_middleName>").append(proposer_middleName).append("</proposer_middleName>");
        inputVal.append("<proposer_lastName>").append(proposer_lastName).append("</proposer_lastName>");
        inputVal.append("<proposer_DOB>").append(proposer_DOB).append("</proposer_DOB>");
        inputVal.append("<proposer_age>").append(proposer_age).append("</proposer_age>");
        inputVal.append("<proposer_gender>").append(proposer_gender).append("</proposer_gender>");

        inputVal.append("<product_name>").append(planName).append("</product_name>");
        inputVal.append("<product_Code>").append(product_Code).append("</product_Code>");
        inputVal.append("<product_UIN>").append(product_UIN).append("</product_UIN>");
        inputVal.append("<product_cateogory>").append(product_cateogory).append("</product_cateogory>");
        inputVal.append("<product_type>").append(product_type).append("</product_type>");

        inputVal.append("<proposer_Is_Same_As_Life_Assured>").append(proposer_Is_Same_As_Life_Assured).append("</proposer_Is_Same_As_Life_Assured>");

        inputVal.append("<isJKResident>").append("false").append("</isJKResident>");
        inputVal.append("<isStaff>").append("false").append("</isStaff>");
        inputVal.append("<age>").append(age).append("</age>");

        inputVal.append("<policyTerm>").append(policyTerm).append("</policyTerm>");

        inputVal.append("<premFreq>").append(PremPayingMode).append("</premFreq>");
        inputVal.append("<premPayingTerm>").append(premPayingTerm).append("</premPayingTerm>");
        String str_productCategory1 = "";
        inputVal.append("<Product_Cat>").append(str_productCategory1).append("</Product_Cat>");

        inputVal.append("<premiumAmount>").append(premAmount).append("</premiumAmount>");

        inputVal.append("<Product_Cat>").append(product_cateogory).append("</Product_Cat>");

        //Added Tushar Kadam Kerla Applicable Tax 7-Jun-2019
        String str_kerla_discount = "N";
        if (cb_kerladisc.isChecked()) {
            str_kerla_discount = "Y";
        }

        inputVal.append("<KFC>" + str_kerla_discount + "</KFC>");
        //End Added Tushar Kadam Kerla Applicable Tax 7-Jun-2019

        inputVal.append("</smartsamriddhi>");

    }

    /********************************** Output starts here **********************************************************/
    // Display Smart Samriddhi Output Screen
    private void showSmartSamriddhiOutputPg(SmartSamriddhiBean SGSPbean) {

        SmartSamriddhiBusinessLogic smartSamriddhiBusinessBean = new SmartSamriddhiBusinessLogic(
                SGSPbean);

        retVal = new StringBuilder();
        String[] outputArr = getOutput(
                SGSPbean);


        double sumAssured = smartSamriddhiBean.getSumAssured();
        Double d = new Double(sumAssured);

        // Intent i = new
        // Intent(getApplicationContext(),com.sbilifeSmartSamriddhidsavingsplan.SmartSamriddhiSuccess.class);

        try {

            retVal.append("<?xml version='1.0' encoding='utf-8' ?><SmartSamriddhi>");
            retVal.append("<errCode>0</errCode>");

            retVal.append("<yearlyPrem>").append(outputArr[0]).append("</yearlyPrem>")
                    .append("<FYServTax>").append(outputArr[1]).append("</FYServTax>")
                    .append("<FYPrem>").append(outputArr[2]).append("</FYPrem>")
                    .append("<SYServTax>").append(outputArr[3]).append("</SYServTax>")
                    .append("<SYPrem>").append(outputArr[4]).append("</SYPrem>")
                    .append("<sumAssured>").append(outputArr[5]).append("</sumAssured>")
                    .append("<GuarntdMatBeneft>").append(outputArr[6]).append("</GuarntdMatBeneft>")
                    .append("<SAMF>").append(outputArr[7]).append("</SAMF>")
                    .append("<BackDateinterest>").append(outputArr[8]).append("</BackDateinterest>")
                    .append("<backdateInt>").append(outputArr[8]).append("</backdateInt>")
                    .append("<basicServiceTax>").append(outputArr[9]).append("</basicServiceTax>")
                    .append("<SBCServiceTax>").append(outputArr[10]).append("</SBCServiceTax>")
                    .append("<KKCServiceTax>").append(outputArr[11]).append("</KKCServiceTax>")
                    .append("<basicServiceTaxSecondYear>").append(outputArr[12]).append("</basicServiceTaxSecondYear>")
                    .append("<SBCServiceTaxSecondYear>").append(outputArr[13]).append("</SBCServiceTaxSecondYear>")
                    .append("<KKCServiceTaxSecondYear>").append(outputArr[14]).append("</KKCServiceTaxSecondYear>")
                    .append("<MinesOccuInterest>").append(outputArr[15]).append("</MinesOccuInterest>")
                    .append("<OccuInt>").append(outputArr[15]).append("</OccuInt>")
                    .append("<OccuIntServiceTax>").append(outputArr[16]).append("</OccuIntServiceTax>").append(
                    "<SumAssuredOnDeath>" + outputArr[19] + "</SumAssuredOnDeath>")
                    //.append("<KeralaCessServiceTax>"  + outputArr[17]  + "</KeralaCessServiceTax>")
                    //.append("<KeralaCessServiceTaxSecondYear>"  + outputArr[18]  + "</KeralaCessServiceTaxSecondYear>")
                    .append(bussIll.toString());
            retVal.append("</SmartSamriddhi>");
        } catch (Exception e) {
            // TODO: handle exception

            retVal.append("<?xml version='1.0' encoding='utf-8' ?><SmartSamriddhi>" + "<errCode>1</errCode>" + "<errorMessage>").append(e.getMessage()).append("</errorMessage></SmartSamriddhi>");
        }

    }


    /********************************** Calculations starts from here **********************************************************/

    private String[] getOutput(SmartSamriddhiBean SGSPbean) {
        SmartSamriddhiBusinessLogic ssBusinessLogic = new SmartSamriddhiBusinessLogic(
                SGSPbean);
        bussIll = new StringBuilder();
        int _year_F = 0;

        int year_F = 0;

        double yearlyBasePremiumPaid = 0, GuaranteedDeathBenefit = 0, yearlyBasePremiumPaid11 = 0, cummulativePremium11 = 0,

                GuaranteedMaturityBenefit = 0, cummulativePremium = 0, GuaranteedAddition = 0, GuaranteedSurrenderValue = 0, cummulativeGuaranteedAddition = 0,
                NonGuaranteedSurrenderValue = 0, sumAssuredOnDeath = 0;

        // From GUI Input

        int age = SGSPbean.getAge();
        double premiumAmt = ssBusinessLogic.getPremiumAmount(SGSPbean.getPremPayingMode(), SGSPbean.getPremiumAmount());

        //


        /*** modified by Akshaya on 20-MAY-16 start **/
		/*double basicServiceTax = ssBusinessLogic.getServiceTax(premiumAmt, "basic");
        double SBCServiceTax = ssBusinessLogic.getServiceTax(premiumAmt, "SBC");
		double KKCServiceTax = ssBusinessLogic.getServiceTax(premiumAmt, "KKC");*/

        double basicServiceTax = 0;
        double SBCServiceTax = 0;
        double KKCServiceTax = 0;
        double KerlaServiceTax = 0;
        double KeralaCessServiceTax = 0;
        boolean isKerlaDiscount = SGSPbean.isKerlaDisc();

        if (isKerlaDiscount) {
            basicServiceTax = ssBusinessLogic.getServiceTax(premiumAmt, "basic");
            KerlaServiceTax = ssBusinessLogic.getServiceTax(premiumAmt, "KERALA");
            KeralaCessServiceTax = KerlaServiceTax - basicServiceTax;
        } else {
            basicServiceTax = ssBusinessLogic.getServiceTax(premiumAmt, "basic");
            SBCServiceTax = ssBusinessLogic.getServiceTax(premiumAmt, "SBC");
            KKCServiceTax = ssBusinessLogic.getServiceTax(premiumAmt, "KKC");
        }


        double fyServiceTax = basicServiceTax + SBCServiceTax + KKCServiceTax + KerlaServiceTax;

        double fyPremiumWithST = premiumAmt + fyServiceTax;

//		double fyPremiumWithST = Double.valueOf(smartSamriddhiBusinessLogic
//				.setPremiumWithST(premiumAmt, prop.fyServiceTax));
//		double fyServiceTax = Double.valueOf(smartSamriddhiBusinessLogic.setServiceTax(
//				premiumAmt, fyPremiumWithST));




		/*double basicServiceTaxSecondYear = ssBusinessLogic.getServiceTaxSecondYear(premiumAmt, "basic");
        double SBCServiceTaxSecondYear = ssBusinessLogic.getServiceTaxSecondYear(premiumAmt, "SBC");
		double KKCServiceTaxSecondYear = ssBusinessLogic.getServiceTaxSecondYear(premiumAmt, "KKC");*/

        double basicServiceTaxSecondYear = 0;
        double SBCServiceTaxSecondYear = 0;
        double KKCServiceTaxSecondYear = 0;
        double kerlaServiceTaxSecondYear = 0;
        double KeralaCessServiceTaxSecondYear = 0;

        if (isKerlaDiscount) {
            basicServiceTaxSecondYear = ssBusinessLogic.getServiceTaxSecondYear(premiumAmt, "basic");
            kerlaServiceTaxSecondYear = ssBusinessLogic.getServiceTaxSecondYear(premiumAmt, "KERALA");
            KeralaCessServiceTaxSecondYear = kerlaServiceTaxSecondYear - basicServiceTaxSecondYear;
        } else {
            basicServiceTaxSecondYear = ssBusinessLogic.getServiceTaxSecondYear(premiumAmt, "basic");
            SBCServiceTaxSecondYear = ssBusinessLogic.getServiceTaxSecondYear(premiumAmt, "SBC");
            KKCServiceTaxSecondYear = ssBusinessLogic.getServiceTaxSecondYear(premiumAmt, "KKC");
        }


        double syServiceTax = basicServiceTaxSecondYear
                + SBCServiceTaxSecondYear + KKCServiceTaxSecondYear + kerlaServiceTaxSecondYear;

        double syPremiumWithST = premiumAmt + syServiceTax;

        // double syPremiumWithST = Double.valueOf(ssBusinessLogic
        // .setPremiumWithST(premiumAmt, prop.syServiceTax));
        // double syServiceTax = Double.valueOf(ssBusinessLogic.setServiceTax(
        // premiumAmt, syPremiumWithST));

        /******************* Modified by Akshaya on 05-FEB-2015 start **********/
        double SAMFRate = ssBusinessLogic.setSAMFRate(age);
        double sumAssured = Double.valueOf(ssBusinessLogic.setSumAssured(SGSPbean.getPremiumAmount(), SAMFRate));

        /******************* Modified by Akshaya on 05-FEB-2015 end **********/

        // System.out.println(fyPremiumWithST+" "+fyServiceTax+"  "+syPremiumWithST+"  "+syServiceTax
        // + " "+sumAssured);
        int rowNumber = 0, j = 0;
        try {
            for (int i = 0; i < prop.policyTerm; i++) {
                rowNumber++;

                year_F = rowNumber;
                _year_F = year_F;
                // System.out.println("1. year_F "+year_F);
                bussIll.append("<policyYr" + _year_F + ">" + _year_F
                        + "</policyYr" + _year_F + ">");

                yearlyBasePremiumPaid = ssBusinessLogic
                        .setYearlyBasePremiumPaid(year_F, SGSPbean.getPremiumAmount());
                // System.out.println("2.Total Base Premium Paid "+yearlyBasePremiumPaid);
                bussIll.append("<totBasePremPaid" + _year_F + ">"
                        + yearlyBasePremiumPaid + "</totBasePremPaid" + _year_F
                        + ">");

                cummulativePremium = cummulativePremium + yearlyBasePremiumPaid;
                // System.out.println("3.cummulativePremium "+cummulativePremium);
                bussIll.append("<cummulatvePrem" + _year_F + ">"
                        + cummulativePremium + "</cummulatvePrem" + _year_F
                        + ">");

                yearlyBasePremiumPaid11 = ssBusinessLogic
                        .setYearlyBasePremiumPaid11(year_F, SGSPbean.getPremiumAmount(), SGSPbean.getPremPayingMode());

                cummulativePremium11 = cummulativePremium11 + yearlyBasePremiumPaid11;

                GuaranteedAddition = ssBusinessLogic.setGuaranteedAddition(
                        SGSPbean.getPremiumAmount(), cummulativePremium);
                cummulativeGuaranteedAddition = cummulativeGuaranteedAddition
                        + GuaranteedAddition;
                // System.out.println("4.Non Guarateed Death Benefit At_8_Percent "+
                // GuaranteedAddition);
                bussIll.append("<guarntdAddtn" + _year_F + ">"
                        + GuaranteedAddition + "</guarntdAddtn" + _year_F + ">");

                GuaranteedDeathBenefit = ssBusinessLogic
                        .setGuaranteedDeathBenefit(sumAssured, SGSPbean.getPremiumAmount(),
                                cummulativeGuaranteedAddition,
                                cummulativePremium);
                // System.out.println("5.Guaranteed Death Benefit "+GuaranteedDeathBenefit);
                bussIll.append("<guarntdDeathBen" + _year_F + ">"
                        + GuaranteedDeathBenefit + "</guarntdDeathBen"
                        + _year_F + ">");


                if (year_F == 1) {

                    sumAssuredOnDeath = GuaranteedDeathBenefit - GuaranteedAddition;
                }

                GuaranteedMaturityBenefit = ssBusinessLogic
                        .setGuaranteedMaturityBenefit(sumAssured,
                                cummulativeGuaranteedAddition, year_F);
                // System.out.println("6.Guaranteed Survival Benefit "+GuaranteedMaturityBenefit);
                bussIll.append("<guarntdSurvivalBen" + _year_F + ">"
                        + GuaranteedMaturityBenefit + "</guarntdSurvivalBen"
                        + _year_F + ">");

                GuaranteedSurrenderValue = Math.round(ssBusinessLogic
                        .setGuaranteedSurrenderValue(
                                cummulativeGuaranteedAddition,
                                cummulativePremium11, year_F));
                // System.out.println("7.Guaranteed Surrender Value "+GuaranteedSurrenderValue);
                bussIll.append("<guarntdSurrndrVal" + _year_F + ">"
                        + GuaranteedSurrenderValue + "</guarntdSurrndrVal"
                        + _year_F + ">");

                NonGuaranteedSurrenderValue = Math.round(ssBusinessLogic
                        .setNonGuaranteedSurrenderValue(
                                sumAssured,
                                cummulativeGuaranteedAddition, year_F));
                // System.out.println("7.Guaranteed Surrender Value "+GuaranteedSurrenderValue);
                bussIll.append("<nonGuarntdSurrndrVal" + _year_F + ">"
                        + NonGuaranteedSurrenderValue + "</nonGuarntdSurrndrVal"
                        + _year_F + ">");

            }

            /* Added by Akshaya on 03-MAR-15 start **********************/

            if (rb_proposerdetail_personaldetail_backdating_yes.isChecked()) {
                // "16-jan-2014")));
                String BackDateinterest = commonForAllProd.getRoundUp(""
                        + (ssBusinessLogic.getBackDateInterest(
                        fyPremiumWithST,
                        btn_proposerdetail_personaldetail_backdatingdate
                                .getText().toString())));

                BackDateinterestwithGST = commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(Double.parseDouble(BackDateinterest) + (Double.parseDouble(BackDateinterest) * prop.GSTforbackdate)));
            } else {
                BackDateinterestwithGST = "0";
            }

            fyPremiumWithST = fyPremiumWithST
                    + Double.parseDouble(BackDateinterestwithGST);
            /****************************** Added by Akshaya on 03-MAR-15 end **********************/

        } catch (Exception e) {
            e.printStackTrace();
        }

        String MinesOccuInterest = "" + ssBusinessLogic.getMinesOccuInterest(smartSamriddhiBean.getSumAssured());

        String servicetax_MinesOccuInterest = ""
                + ssBusinessLogic
                .getServiceTaxMines(Double.parseDouble(MinesOccuInterest));

        MinesOccuInterest = "" + (Double.parseDouble(MinesOccuInterest) + Double.parseDouble(servicetax_MinesOccuInterest));

        /****** modified by Akshaya on 20-MAY-16 start ****/
        return new String[]{
                (commonForAllProd.getStringWithout_E(premiumAmt)),
                commonForAllProd.getStringWithout_E(fyServiceTax),
                commonForAllProd.getStringWithout_E(fyPremiumWithST),
                commonForAllProd.getStringWithout_E(syServiceTax),
                commonForAllProd.getStringWithout_E(syPremiumWithST),
                commonForAllProd.getStringWithout_E(sumAssured),
                commonForAllProd.getStringWithout_E(GuaranteedMaturityBenefit),
                commonForAllProd.getStringWithout_E(SAMFRate),
                BackDateinterestwithGST,
                commonForAllProd.getStringWithout_E(basicServiceTax),
                commonForAllProd.getStringWithout_E(SBCServiceTax),
                commonForAllProd.getStringWithout_E(KKCServiceTax),
                commonForAllProd.getStringWithout_E(basicServiceTaxSecondYear),
                commonForAllProd.getStringWithout_E(SBCServiceTaxSecondYear),
                commonForAllProd.getStringWithout_E(KKCServiceTaxSecondYear),
                MinesOccuInterest, servicetax_MinesOccuInterest,
                commonForAllProd.getStringWithout_E(KeralaCessServiceTax),
                commonForAllProd.getStringWithout_E(KeralaCessServiceTaxSecondYear),
                commonForAllProd.getStringWithout_E(sumAssuredOnDeath)};

    }

    /********************************** Validations starts here **********************************************************/

    // maturity age of policy is 50 years
    private boolean valMaturityAge() {
        int Age = Integer.parseInt(spnr_Age.getSelectedItem().toString());
        int PolicyTerm = Integer.parseInt(spnrPolicyTerm.getSelectedItem()
                .toString());
        if ((Age + PolicyTerm) > 65) {
            /*showAlert.setMessage("Maturity age is 65 years");
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // apply focusable method
                            setFocusable(btn_bi_smart_samriddhi_life_assured_date);
                            btn_bi_smart_samriddhi_life_assured_date
                                    .requestFocus();
                        }
                    });
            showAlert.show();*/

            dialog("Maturity age is 65 years", true);
            commonMethods.setFocusable(btn_bi_smart_samriddhi_life_assured_date);
            btn_bi_smart_samriddhi_life_assured_date
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

        } else if (Integer.parseInt(edt_premiumAmt.getText().toString()) > prop.maxPremiumAmt) {
            error = "Premium Amount should not be greater than Rs."
                    + currencyFormat.format(prop.maxPremiumAmt);

        } else if (Integer.parseInt(edt_premiumAmt.getText().toString()) < prop.minPremiumAmt) {
            error = "Premium Amount should not be less than Rs."
                    + currencyFormat.format(prop.minPremiumAmt);

        } else {
            if (!(Double.parseDouble(edt_premiumAmt.getText().toString()) % 1000 == 0)) {
                error = "Premium Amount should be multiple of 1000";

            }
        }
        if (!error.equals("")) {
            //showAlert(error.toString());
            dialog(error, true);
            commonMethods.setFocusable(edt_premiumAmt);
            edt_premiumAmt.requestFocus();
            return false;
        } else
            return true;

    }

    public boolean valDoYouBackdate() {
        if (!proposer_Backdating_WishToBackDate_Policy.equals("")) {
            return true;
        } else {
            /*showAlert.setMessage("Please Select Do you wish to Backdate ");
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // apply focusable method
                            setFocusable(rb_proposerdetail_personaldetail_backdating_yes);
                            rb_proposerdetail_personaldetail_backdating_yes
                                    .requestFocus();
                        }
                    });
            showAlert.show();*/

            dialog("Please Select Do you wish to Backdate", true);
            commonMethods.setFocusable(rb_proposerdetail_personaldetail_backdating_yes);
            rb_proposerdetail_personaldetail_backdating_yes
                    .requestFocus();
            return false;

        }
    }

    private boolean valBackdate() {
        if (proposer_Backdating_WishToBackDate_Policy.equals("y")) {

            if (proposer_Backdating_BackDate.equals("")) {
                /*showAlert.setMessage("Please Select Backdate ");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // apply focusable method
                                setFocusable(btn_proposerdetail_personaldetail_backdatingdate);
                                btn_proposerdetail_personaldetail_backdatingdate
                                        .requestFocus();

                            }
                        });
                showAlert.show();*/

                dialog("Please Select Backdate", true);
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

                if (currMonth >= 4) {
                    finYerEndDate = dateformat1.parse("1-4-" + currYear);
                } else {
                    finYerEndDate = dateformat1.parse("1-4-" + (currYear - 1));
                }

                if (currentDate.before(dtBackDate)) {
                    error = "Please enter backdation date between "
                            + dateformat1.format(finYerEndDate) + " and "
                            + dateformat1.format(currentDate);
                } else if (dtBackDate.before(finYerEndDate)) {
                    error = "Please enter Backdation date between "
                            + dateformat1.format(finYerEndDate) + " and "
                            + dateformat1.format(currentDate);
                }

                if (!error.equals("")) {
                    //showAlert.setMessage(error);
                    dialog(error, true);
                   /* showAlert.setNeutralButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                }
                            });
                    showAlert.show();*/
                    return false;
                }

            }

        } catch (Exception e) {
            return false;
        }

        return true;

    }

    /********************************** Added by Akshaya on 03-Mar-2015 end ***************************/

    private boolean valLifeAssuredProposerDetail() {
        if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {
            if (lifeAssured_Title.equals("")
                    || lifeAssured_First_Name.equals("")
                    || lifeAssured_Last_Name.equals("")) {

                commonMethods.showMessageDialog(context, "Please Fill Name Detail For LifeAssured");
                if (lifeAssured_Title.equals("")) {
                    // apply focusable method
                    commonMethods.setFocusable(spnr_bi_smart_samriddhi_life_assured_title);
                    spnr_bi_smart_samriddhi_life_assured_title
                            .requestFocus();
                } else if (lifeAssured_First_Name.equals("")) {
                    commonMethods.setFocusable(edt_bi_smart_samriddhi_life_assured_first_name);
                    edt_bi_smart_samriddhi_life_assured_first_name
                            .requestFocus();
                } else {
                    commonMethods.setFocusable(edt_bi_smart_samriddhi_life_assured_last_name);
                    edt_bi_smart_samriddhi_life_assured_last_name
                            .requestFocus();
                }

                return false;
            } else if (gender.equalsIgnoreCase("")) {
                commonMethods.showMessageDialog(context, "Please select Gender");
                commonMethods.setFocusable(spnr_bi_smart_samriddhi_life_assured_title);
                spnr_bi_smart_samriddhi_life_assured_title
                        .requestFocus();

                return false;

            } else if (lifeAssured_Title.equalsIgnoreCase("Mr.")
                    && gender.equalsIgnoreCase("Female")) {

                commonMethods.showMessageDialog(context, "Life Assured Title and Gender is not Valid");
                // apply focusable method
                commonMethods.setFocusable(spnr_bi_smart_samriddhi_life_assured_title);
                spnr_bi_smart_samriddhi_life_assured_title.requestFocus();
                return false;

            } else if (lifeAssured_Title.equalsIgnoreCase("Ms.")
                    && gender.equalsIgnoreCase("Male")) {

                commonMethods.showMessageDialog(context, "Life Assured Title and Gender is not Valid");
                commonMethods.setFocusable(spnr_bi_smart_samriddhi_life_assured_title);
                spnr_bi_smart_samriddhi_life_assured_title
                        .requestFocus();

                return false;

            } else if (lifeAssured_Title.equalsIgnoreCase("Mrs.")
                    && gender.equalsIgnoreCase("Male")) {

                commonMethods.showMessageDialog(context, "Life Assured Title and Gender is not Valid");
                commonMethods.setFocusable(spnr_bi_smart_samriddhi_life_assured_title);
                spnr_bi_smart_samriddhi_life_assured_title
                        .requestFocus();


                return false;
            } else {
                return true;
            }

        } else
            return true;
    }

    private boolean valDob() {

        if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {

            if (lifeAssured_date_of_birth.equals("")
                    || lifeAssured_date_of_birth
                    .equalsIgnoreCase("select Date")) {
               /* showAlert
                        .setMessage("Please Select Valid Date Of Birth For LifeAssured");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // apply focusable method
                                setFocusable(btn_bi_smart_samriddhi_life_assured_date);
                                btn_bi_smart_samriddhi_life_assured_date
                                        .requestFocus();
                            }
                        });
                showAlert.show();*/

                dialog("Please Select Valid Date Of Birth For LifeAssured", true);
                commonMethods.setFocusable(btn_bi_smart_samriddhi_life_assured_date);
                btn_bi_smart_samriddhi_life_assured_date
                        .requestFocus();

                return false;
            } else {
                return true;
            }
        } else
            return true;
    }

    /********************************** Validations ends here **********************************************************/
    // Alert Dialog Box
    public void showAlert(String error) {
        // showAlert.setMessage(error);
        dialog(error, true);
        /*showAlert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        showAlert.show();*/
    }

    // FOr Date Dialog Box
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, R.style.datepickerstyle,
                        mDateSetListener, mDay, mMonth, mYear);
            default:
                break;
        }
        return null;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        switch (id) {
            case DATE_DIALOG_ID:
                ((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
                break;
            default:
                break;

        }
    }

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
            dialog("Please fill Valid Date", false);
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
                        commonMethods.showMessageDialog(context, "Please fill Valid Bith Date");
                    } else {
                        if (18 <= age && age <= 50) {

                            btn_bi_smart_samriddhi_life_assured_date
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

                            clearFocusable(btn_bi_smart_samriddhi_life_assured_date);

                            commonMethods.setFocusable(edt_proposerdetail_basicdetail_contact_no);
                            edt_proposerdetail_basicdetail_contact_no
                                    .requestFocus();

                        } else {
                            commonMethods.showMessageDialog(context, "Minimum Age should be 18 yrs and Maximum Age should be 50 yrs For LifeAssured");
                            btn_bi_smart_samriddhi_life_assured_date
                                    .setText("Select Date");
                            lifeAssured_date_of_birth = "";

                            clearFocusable(btn_bi_smart_samriddhi_life_assured_date);
                            commonMethods.setFocusable(btn_bi_smart_samriddhi_life_assured_date);

                            btn_bi_smart_samriddhi_life_assured_date
                                    .requestFocus();
                        }
                    }
                    break;

                case 6:
                    if (age >= 0) {
                        proposer_Backdating_BackDate = date + "";
                        btn_proposerdetail_personaldetail_backdatingdate
                                .setText(proposer_Backdating_BackDate);
                        clearFocusable(btn_proposerdetail_personaldetail_backdatingdate);

                    } else {
                        dialog("Please Select Valid BackDating Date", true);
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

            int Proposerage = calculateMyAge(mYear, Integer.parseInt(mont),
                    Integer.parseInt(day));

            String str_final_age = Integer.toString(Proposerage);
            spnr_Age.setSelection(getIndex(spnr_Age, str_final_age), false);
            valMaturityAge();

        }

    }

    private int calculateMyAge(int year, int month, int day) {
        Calendar nowCal = new GregorianCalendar(year, month, day);

        String[] ProposerDob = commonMethods.getDDMMYYYYDate(lifeAssured_date_of_birth).split("/");
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

    public void onClickLADob(View v) {
        /*initialiseDateParameter(lifeAssured_date_of_birth, 35);
        DIALOG_ID = 4;
        showDialog(DATE_DIALOG_ID);*/

        if (!na_dob.equals("") && flag == 0) {
            initialiseDateParameter(na_dob, 35);
            DIALOG_ID = 4;
            updateDisplay(DIALOG_ID);
            flag = 1;
        } else {
            initialiseDateParameter(lifeAssured_date_of_birth, 35);
            DIALOG_ID = 4;
            showDialog(DATE_DIALOG_ID);
        }

    }

    public void onClickBackDating(View v) {
        String backdate = commonMethods.getMMDDYYYYDatabaseDate(proposer_Backdating_BackDate);
        initialiseDateParameter(backdate, 0);
        DIALOG_ID = 6;
        if (lifeAssured_date_of_birth != null
                && !lifeAssured_date_of_birth.equals("")) {
            showDialog(DATE_DIALOG_ID);
        } else {
            dialog("Please select a LifeAssured DOB First", true);
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

    private String getformatedThousandString(int number) {
        return NumberFormat.getNumberInstance(Locale.US)
                .format(number);
    }

    private String getCurrentDate() {
        Calendar present_date = Calendar.getInstance();
        int mDay = present_date.get(Calendar.DAY_OF_MONTH);
        int mMonth = present_date.get(Calendar.MONTH) + 1;
        int mYear = present_date.get(Calendar.YEAR);

        return mDay + "-" + mMonth + "-" + mYear;

    }


    // method to set a clearing a element
    private void clearFocusable(View v) {

        v.setFocusable(false);
        v.setFocusableInTouchMode(false);
        // v.clearFocus();
    }


    private void windowmessagesgin() {

        d = new Dialog(BI_SmartSamriddhiActivity.this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.BLACK));

        d.setContentView(R.layout.window_message_signature);
        final Button btn_save = d.findViewById(R.id.save);
        final Button btn_cancel = d.findViewById(R.id.cancel);

        Button btn_takeSign = d.findViewById(R.id.takesignature);

        btn_takeSign.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_save.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(BI_SmartSamriddhiActivity.this,
                        CaptureSignature.class);
                startActivityForResult(intent, SIGNATURE_ACTIVITY);

            }
        });

        btn_cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                d.dismiss();
            }
        });
        d.show();

    }


    private void dialog(String msg, boolean ismandatry) {

        final Dialog d = new Dialog(BI_SmartSamriddhiActivity.this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        d.setContentView(R.layout.window_pop_up_message_with_single_options);
        TextView text_mssg_1 = d.findViewById(R.id.text_mssg_1);
        text_mssg_1.setText(msg);
        TextView text_mssg_2 = d.findViewById(R.id.text_mssg_2);
        TextView text_mssg_3 = d.findViewById(R.id.text_mssg_3);
        if (ismandatry) {
            text_mssg_2.setVisibility(View.GONE);
            text_mssg_3.setVisibility(View.GONE);
        }
        Button ok = d.findViewById(R.id.idbtnagreement);
        ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                d.dismiss();
            }

        });
        d.setCancelable(true);
        d.setCanceledOnTouchOutside(true);
        d.show();

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        if (v.getId() == edt_bi_smart_samriddhi_life_assured_first_name
                .getId()) {
            commonMethods.setFocusable(edt_bi_smart_samriddhi_life_assured_middle_name);
            edt_bi_smart_samriddhi_life_assured_middle_name
                    .requestFocus();
        } else if (v.getId() == edt_bi_smart_samriddhi_life_assured_middle_name
                .getId()) {
            commonMethods.setFocusable(edt_bi_smart_samriddhi_life_assured_last_name);
            edt_bi_smart_samriddhi_life_assured_last_name
                    .requestFocus();
        } else if (v.getId() == edt_bi_smart_samriddhi_life_assured_last_name
                .getId()) {
            commonMethods.setFocusable(btn_bi_smart_samriddhi_life_assured_date);
            btn_bi_smart_samriddhi_life_assured_date
                    .requestFocus();
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
            // clearFocusable(edt_premiumAmt);
            commonMethods.setFocusable(edt_premiumAmt);
            edt_premiumAmt.requestFocus();
        } else if (v.getId() == edt_premiumAmt.getId()) {
            commonMethods.hideKeyboard(edt_premiumAmt, context);

            /*
             * clearFocusable(edt_premiumAmt);
             * commonMethods.setFocusable(rb_proposerdetail_personaldetail_backdating_yes);
             * rb_proposerdetail_personaldetail_backdating_yes.requestFocus();
             */

        }
        return true;
    }

    private boolean createPdf() {
        try {

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

            Font small_bold1 = new Font(Font.FontFamily.TIMES_ROMAN, 8,
                    Font.BOLD);

            Font small_bold2 = new Font(Font.FontFamily.TIMES_ROMAN, 6,
                    Font.BOLD);
            Font small_normal = new Font(Font.FontFamily.TIMES_ROMAN, 8,
                    Font.NORMAL);
            float[] columnWidths2 = {2f, 1f};
            float[] columnWidths4 = {2f, 1f, 2f, 1f};
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
                            "Benefit Illustration for SBI Life - Smart Samriddhi (UIN - 111N097V03)",
                            headerBold));

            PdfPTable headertable = new PdfPTable(1);
            headertable.setWidthPercentage(100);
            PdfPCell c1 = new PdfPCell(new Phrase(Para_Header));
            c1.setBackgroundColor(BaseColor.DARK_GRAY);
            c1.setPadding(5);
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            headertable.addCell(c1);
            headertable.setHorizontalAlignment(Element.ALIGN_CENTER);
            Paragraph para_address = new Paragraph(
                    "SBI Life Insurance Co. Ltd",
                    small_normal);
            para_address.setAlignment(Element.ALIGN_CENTER);
            Paragraph para_address2 = new Paragraph(
                    "Registered & Corporate Office: 'Natraj', M.V.Road and Western Express Highway Junction, Andheri (East), Mumbai - 400069",
                    small_normal);
            para_address2.setAlignment(Element.ALIGN_CENTER);
            Paragraph para_address3 = new Paragraph(
                    "IRDAI Registration No. 111 | Website: www.sbilife.co.in | Email: info@sbilife.co.in | CIN: L99999MH2000PLC129113",
                    small_normal);
            para_address3.setAlignment(Element.ALIGN_CENTER);
            Paragraph para_address4 = new Paragraph(
                    "Toll Free: 1800 267 9090 (Between 9.00 am & 9.00 pm)",
                    small_normal);
            para_address4.setAlignment(Element.ALIGN_CENTER);
            Paragraph para_address1 = new Paragraph("Customised Benefit Illustration (CBI) ", small_bold);
            para_address1.setAlignment(Element.ALIGN_CENTER);
            Paragraph para_address11 = new Paragraph("Benefit Illustration(BI): SBI Life - Smart Samriddhi (UIN - 111N097V03)", small_bold_for_name);
            para_address11.setAlignment(Element.ALIGN_CENTER);
            Paragraph para_address111 = new Paragraph("An Individual, Non-linked, Non-Participating, Life Insurance Savings Product", small_bold_for_name);
            para_address111.setAlignment(Element.ALIGN_CENTER);

            document.add(para_address);
            document.add(para_address2);
            document.add(para_address3);
            document.add(para_address4);
            document.add(para_img_logo_after_space_1);
            //document.add(para_address1);
            document.add(para_address11);
            document.add(para_address111);
            document.add(para_img_logo_after_space_1);
            //document.add(headertable);
            document.add(para_img_logo_after_space_1);

            document.add(para_img_logo_after_space_1);
            if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("Y")) {
                name_of_proposer = name_of_life_assured;
            }

            PdfPTable table_proposer_name = new PdfPTable(2);
            // float[] columnWidths_table_proposer_name = { 2f, 2f, 1f, 2f };
            // table_proposer_name.setWidths(columnWidths_table_proposer_name);
            table_proposer_name.setWidthPercentage(100);

            PdfPCell ProposalNumber_cell_1 = new PdfPCell(new Paragraph(
                    "Proposal No.:", small_normal));
            PdfPCell ProposalNumber_cell_2 = new PdfPCell(new Paragraph(
                    QuatationNumber, small_bold1));
            ProposalNumber_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);


            ProposalNumber_cell_1.setPadding(5);
            ProposalNumber_cell_2.setPadding(5);

            table_proposer_name.addCell(ProposalNumber_cell_1);
            table_proposer_name.addCell(ProposalNumber_cell_2);
            document.add(table_proposer_name);


            PdfPTable table_proposer_name1 = new PdfPTable(2);

            table_proposer_name1.setWidthPercentage(100);

            PdfPCell ProposalNumber_cell_11 = new PdfPCell(new Paragraph(
                    "Channel / Intermediary", small_normal));
            PdfPCell ProposalNumber_cell_21 = new PdfPCell(new Paragraph(
                    userType, small_bold1));
            ProposalNumber_cell_21.setHorizontalAlignment(Element.ALIGN_CENTER);

            ProposalNumber_cell_11.setPadding(5);
            ProposalNumber_cell_21.setPadding(5);


            table_proposer_name1.addCell(ProposalNumber_cell_11);
            table_proposer_name1.addCell(ProposalNumber_cell_21);

            document.add(table_proposer_name1);


            document.add(para_img_logo_after_space_1);

            PdfPTable BI_Pdftable_Introdcution = new PdfPTable(1);
            BI_Pdftable_Introdcution.setWidthPercentage(100);
            PdfPCell BI_Pdftable_Introdcutioncell = new PdfPCell(new Paragraph(
                    "Introduction", small_bold));

            BI_Pdftable_Introdcutioncell
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_Pdftable_Introdcutioncell.setPadding(5);

            BI_Pdftable_Introdcution.addCell(BI_Pdftable_Introdcutioncell);
            document.add(BI_Pdftable_Introdcution);

            PdfPTable BI_Pdftable2 = new PdfPTable(1);
            BI_Pdftable2.setWidthPercentage(100);
            // CR_table6.setWidths(columnWidths_2);
            PdfPCell BI_Pdftable2_cell1 = new PdfPCell(
                    new Paragraph(
                            "Insurance Regulatory & Development Authority of India (IRDAI) requires all life insurance companies operating in India to provide official illustrations to their customers.  The illustrations are based on the investment rates of return set by the Life Insurance Council (constituted under Section 64C(a) of the Insurance Act 1938) and is not intended to reflect the actual investment returns achieved or which may be achieved in future by SBI Life Insurance Company Limited. All life insurance companies use the same rates in their benefit illustrations.",
                            small_normal));

            BI_Pdftable2_cell1.setPadding(5);

            BI_Pdftable2.addCell(BI_Pdftable2_cell1);
            //  document.add(BI_Pdftable2);

            PdfPTable BI_Pdftable3 = new PdfPTable(1);
            BI_Pdftable3.setWidthPercentage(100);
            // CR_table6.setWidths(columnWidths_3);
            PdfPCell BI_Pdftable3_cell1 = new PdfPCell(
                    new Paragraph(
                            "Currently, the two rates of investment return as specified by IRDAI are 4% and 8% per annum.",
                            small_normal));

            BI_Pdftable3_cell1.setPadding(5);

            BI_Pdftable3.addCell(BI_Pdftable3_cell1);
            //document.add(BI_Pdftable3);

            PdfPTable BI_Pdftable5 = new PdfPTable(1);
            BI_Pdftable5.setWidthPercentage(100);
            // CR_table6.setWidths(columnWidths_3);
            PdfPCell BI_Pdftable5_cell1 = new PdfPCell(
                    new Paragraph(
                            "The main objective of the illustration is that the client is able to appreciate the features of the product and the flow of benefits in different circumstances with some level of quantification. For further information on the product and its benefits, please refer to the sales brochure and/or policy document. Further information will also be available on request.",
                            small_normal));

            BI_Pdftable3_cell1.setPadding(5);

            BI_Pdftable5.addCell(BI_Pdftable5_cell1);
            //document.add(BI_Pdftable5);

            PdfPTable BI_Pdftable4 = new PdfPTable(1);
            BI_Pdftable4.setWidthPercentage(100);
            // CR_table6.setWidths(columnWidths_4);
            PdfPCell BI_Pdftable4_cell1 = new PdfPCell(
                    new Paragraph(
                            "The main objective of the illustration is that the client is able to appreciate the features of the product and the flow of benefits in different circumstances with some level of quantification. For further information on the product and its benefits,  please refer to the sales brochure and/or policy document.",
                            small_normal));

            BI_Pdftable4_cell1.setPadding(5);

            BI_Pdftable4.addCell(BI_Pdftable4_cell1);
            document.add(BI_Pdftable4);
            document.add(para_img_logo_after_space_1);

            PdfPTable BI_PdftablePlanDetails = new PdfPTable(1);
            BI_PdftablePlanDetails.setWidthPercentage(100);
            PdfPCell BI_PdftablePlanDetails_cell = new PdfPCell(new Paragraph(
                    "Proposer and Life Assured Details", small_bold));

            BI_PdftablePlanDetails_cell
                    .setBackgroundColor(BaseColor.LIGHT_GRAY);

            BI_PdftablePlanDetails_cell
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftablePlanDetails_cell.setPadding(5);

            BI_PdftablePlanDetails.addCell(BI_PdftablePlanDetails_cell);
            document.add(BI_PdftablePlanDetails);

            PdfPTable table_lifeAssuredName = new PdfPTable(4);
            table_lifeAssuredName.setWidthPercentage(100);

            PdfPCell cell_LifeAssuredName1 = new PdfPCell(new Paragraph(
                    "Name of the Prospect/Policyholder", small_normal));
            cell_LifeAssuredName1.setPadding(5);
            PdfPCell cell_lLifeAssuredName2 = new PdfPCell(new Paragraph(
                    name_of_proposer, small_bold));
            cell_lLifeAssuredName2.setPadding(5);
            cell_lLifeAssuredName2.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_LifeAssuredName12 = new PdfPCell(new Paragraph(
                    "Name of the Life Assured", small_normal));
            cell_LifeAssuredName12.setPadding(5);
            PdfPCell cell_lLifeAssuredName22 = new PdfPCell(new Paragraph(
                    name_of_proposer, small_bold));
            cell_lLifeAssuredName22.setPadding(5);
            cell_lLifeAssuredName22.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_lifeAssuredAge1 = new PdfPCell(new Paragraph(
                    "Age (Years)", small_normal));
            cell_lifeAssuredAge1.setPadding(5);
            PdfPCell cell_lifeAssuredAge2 = new PdfPCell(new Paragraph(
                    ageAtEntry + " Years", small_bold));
            cell_lifeAssuredAge2.setPadding(5);
            cell_lifeAssuredAge2.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_lifeAssuredAge11 = new PdfPCell(new Paragraph(
                    "Age (Years)", small_normal));
            cell_lifeAssuredAge11.setPadding(5);
            PdfPCell cell_lifeAssuredAge21 = new PdfPCell(new Paragraph(
                    ageAtEntry + " Years", small_bold));
            cell_lifeAssuredAge21.setPadding(5);
            cell_lifeAssuredAge21.setHorizontalAlignment(Element.ALIGN_CENTER);

            table_lifeAssuredName.addCell(cell_LifeAssuredName1);
            table_lifeAssuredName.addCell(cell_lLifeAssuredName2);
            table_lifeAssuredName.addCell(cell_LifeAssuredName12);
            table_lifeAssuredName.addCell(cell_lLifeAssuredName22);
            table_lifeAssuredName.addCell(cell_lifeAssuredAge1);
            table_lifeAssuredName.addCell(cell_lifeAssuredAge2);
            table_lifeAssuredName.addCell(cell_lifeAssuredAge11);
            table_lifeAssuredName.addCell(cell_lifeAssuredAge21);
            document.add(table_lifeAssuredName);

            PdfPTable table_lifeAssuredDetails = new PdfPTable(4);
            table_lifeAssuredDetails.setWidthPercentage(100);

            PdfPCell cell_lifeAssuredAmaturityGender1 = new PdfPCell(
                    new Paragraph("Gender", small_normal));
            cell_lifeAssuredAmaturityGender1.setPadding(5);
            PdfPCell cell_lifeAssuredAmaturityGender2 = new PdfPCell(
                    new Paragraph(gender, small_bold));
            cell_lifeAssuredAmaturityGender2.setPadding(5);
            cell_lifeAssuredAmaturityGender2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_lifeAssuredAmaturityGender11 = new PdfPCell(
                    new Paragraph("Gender", small_normal));
            cell_lifeAssuredAmaturityGender11.setPadding(5);
            PdfPCell cell_lifeAssuredAmaturityGender21 = new PdfPCell(
                    new Paragraph(gender, small_bold));
            cell_lifeAssuredAmaturityGender21.setPadding(5);
            cell_lifeAssuredAmaturityGender21
                    .setHorizontalAlignment(Element.ALIGN_CENTER);


            PdfPCell staff = new PdfPCell(
                    new Paragraph("Staff", small_normal));
            staff.setPadding(5);
            PdfPCell staff2;
			/*if (staffdiscount.equalsIgnoreCase("true")) {
				staff2 = new PdfPCell(
						new Paragraph("Yes", small_bold));
				staff2.setPadding(5);
				staff2.setHorizontalAlignment(Element.ALIGN_CENTER);
			} else {*/
            staff2 = new PdfPCell(
                    new Paragraph("No", small_bold));
            staff2.setPadding(5);
            staff2.setHorizontalAlignment(Element.ALIGN_CENTER);
            //	}

            PdfPCell blank = new PdfPCell(
                    new Paragraph("", small_normal));
            blank.setPadding(5);
            PdfPCell blank2 = new PdfPCell(
                    new Paragraph("", small_bold));
            blank2.setPadding(5);
            blank2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell state = new PdfPCell(
                    new Paragraph("State", small_normal));
            state.setPadding(5);
            PdfPCell state2;
            if (cb_kerladisc.isChecked()) {
                state2 = new PdfPCell(
                        new Paragraph("Kerala", small_bold));
                state2.setPadding(5);
                state2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
            } else {
                state2 = new PdfPCell(
                        new Paragraph("Non Kerala", small_bold));
                state2.setPadding(5);
                state2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
            }


            table_lifeAssuredDetails.addCell(cell_lifeAssuredAmaturityGender1);
            table_lifeAssuredDetails.addCell(cell_lifeAssuredAmaturityGender2);
            table_lifeAssuredDetails.addCell(cell_lifeAssuredAmaturityGender11);
            table_lifeAssuredDetails.addCell(cell_lifeAssuredAmaturityGender21);
            // table_lifeAssuredDetails
            //    .addCell(cell_lifeAssured_Premium_frequeny1);
            //  table_lifeAssuredDetails
            //         .addCell(cell_lifeAssured_Premium_frequeny2);
            // table_lifeAssuredDetails
            //         .addCell(staff);
            // table_lifeAssuredDetails
            //           .addCell(staff2);
            /*table_lifeAssuredDetails
                    .addCell(state);
            table_lifeAssuredDetails
                    .addCell(state2);
            table_lifeAssuredDetails
                    .addCell(blank);
            table_lifeAssuredDetails
                    .addCell(blank2);*/

            document.add(table_lifeAssuredDetails);
            document.add(para_img_logo_after_space_1);

            PdfPTable BI_PdftableReadDetails = new PdfPTable(1);
            BI_PdftableReadDetails.setWidthPercentage(100);
            PdfPCell BI_PdftableReadDetails_cell = new PdfPCell(new Paragraph(
                    "How to read and understand this benefit illustration?", small_bold));

            BI_PdftableReadDetails_cell
                    .setBackgroundColor(BaseColor.LIGHT_GRAY);

            BI_PdftableReadDetails_cell
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableReadDetails_cell.setPadding(5);

            BI_PdftableReadDetails.addCell(BI_PdftableReadDetails_cell);
            document.add(BI_PdftableReadDetails);


            PdfPTable BI_Pdftable41 = new PdfPTable(1);
            BI_Pdftable41.setWidthPercentage(100);
            // CR_table6.setWidths(columnWidths_4);
            PdfPCell BI_Pdftable41_cell1 = new PdfPCell(
                    new Paragraph(
                            "This benefit illustration is intended to show year-wise premiums payable and benefits under the policy.",
                            small_normal));

            BI_Pdftable41_cell1.setPadding(5);

            BI_Pdftable41.addCell(BI_Pdftable41_cell1);


            document.add(BI_Pdftable41);
            document.add(para_img_logo_after_space_1);

            // String isStaff = "";
            // if (sspBean.getStaffDisc()) {
            // isStaff = "yes";
            // PdfPTable table_staff_NonStaff = new PdfPTable(2);
            // table_staff_NonStaff.setWidthPercentage(100);
            //
            // PdfPCell cell_staff_NonStaff1 = new PdfPCell(new Paragraph(
            // "Staff Discount", small_normal));
            // cell_staff_NonStaff1.setPadding(5);
            //
            // PdfPCell cell_staff_NonStaff2 = new PdfPCell(new Paragraph(
            // isStaff, small_bold));
            // cell_staff_NonStaff2.setPadding(5);
            // cell_staff_NonStaff2
            // .setHorizontalAlignment(Element.ALIGN_CENTER);
            //
            // table_staff_NonStaff.addCell(cell_staff_NonStaff1);
            // table_staff_NonStaff.addCell(cell_staff_NonStaff2);
            // document.add(table_staff_NonStaff);
            // }

            // String isJk = "";
            // if (cb_bi_smart_income_protect_JKResident.isChecked()) {
            // isJk = "yes";
            // PdfPTable table_is_JK = new PdfPTable(2);
            // table_is_JK.setWidthPercentage(100);
            //
            // PdfPCell cell_is_JK1 = new PdfPCell(new Paragraph("J&K",
            // small_normal));
            // cell_is_JK1.setPadding(5);
            //
            // PdfPCell cell_is_JK2 = new PdfPCell(new Paragraph(isJk,
            // small_bold));
            // cell_is_JK2.setPadding(5);
            // cell_is_JK2.setHorizontalAlignment(Element.ALIGN_CENTER);
            //
            // table_is_JK.addCell(cell_is_JK1);
            // table_is_JK.addCell(cell_is_JK2);
            // document.add(table_is_JK);
            // }


            document.add(para_img_logo_after_space_1);
            PdfPTable BI_PdftablePremiumforBasicCover = new PdfPTable(1);
            BI_PdftablePremiumforBasicCover.setWidthPercentage(100);
            PdfPCell BI_PdftablePremiumforBasicCovercell = new PdfPCell(
                    new Paragraph("Policy Details", small_bold));

            BI_PdftablePremiumforBasicCovercell
                    .setBackgroundColor(BaseColor.LIGHT_GRAY);

            BI_PdftablePremiumforBasicCovercell
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftablePremiumforBasicCovercell.setPadding(5);

            BI_PdftablePremiumforBasicCover
                    .addCell(BI_PdftablePremiumforBasicCovercell);
            document.add(BI_PdftablePremiumforBasicCover);


            PdfPTable Table_policyTerm_annualisedPremium_sumAssured = new PdfPTable(
                    4);
            Table_policyTerm_annualisedPremium_sumAssured
                    .setWidthPercentage(100);

            PdfPCell policy_option1 = new PdfPCell(new Paragraph(
                    "Policy Option", small_normal));
            policy_option1.setPadding(5);
            PdfPCell policy_option2 = new PdfPCell(new Paragraph("Not Applicable",
                    small_bold));

            policy_option2.setPadding(5);
            policy_option2.setHorizontalAlignment(Element.ALIGN_CENTER);


            Table_policyTerm_annualisedPremium_sumAssured.addCell(policy_option1);
            Table_policyTerm_annualisedPremium_sumAssured.addCell(policy_option2);

            PdfPCell cell_premium_paying_term1 = new PdfPCell(new Paragraph(
                    " Amount of Installment Premium (Rs.)", small_normal));
            cell_premium_paying_term1.setPadding(5);
            PdfPCell cell_premium_paying_term2 = new PdfPCell(new Phrase(
                    (currencyFormat.format(Double.parseDouble(premiumDetailFYBasicPremium_without_tax))),
                    small_bold));

            cell_premium_paying_term2.setPadding(5);
            cell_premium_paying_term2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);


            PdfPCell cell_lifeAssured_Premium_frequeny1 = new PdfPCell(
                    new Paragraph("Premium Payment Option", small_normal));
            cell_lifeAssured_Premium_frequeny1.setPadding(5);
            PdfPCell cell_lifeAssured_Premium_frequeny2 = new PdfPCell(
                    new Paragraph("Limited", small_bold));
            cell_lifeAssured_Premium_frequeny2.setPadding(5);
            cell_lifeAssured_Premium_frequeny2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            Table_policyTerm_annualisedPremium_sumAssured.addCell(cell_premium_paying_term1);
            Table_policyTerm_annualisedPremium_sumAssured.addCell(cell_premium_paying_term2);
            Table_policyTerm_annualisedPremium_sumAssured.addCell(cell_lifeAssured_Premium_frequeny1);
            Table_policyTerm_annualisedPremium_sumAssured.addCell(cell_lifeAssured_Premium_frequeny2);


            PdfPCell cell_Term1 = new PdfPCell(new Paragraph("Policy Term (Years)",
                    small_normal));
            PdfPCell cell_Term2 = new PdfPCell(new Paragraph(
                    policyTerm, small_bold));
            cell_Term1.setPadding(5);
            cell_Term2.setPadding(5);
            cell_Term2.setHorizontalAlignment(Element.ALIGN_CENTER);

            String payingTerm = "";
            payingTerm = premPayingTerm;


            PdfPCell cell_plan1 = new PdfPCell(new Paragraph("Sum Assured (Rs.)",
                    small_normal));
            cell_plan1.setPadding(5);
            PdfPCell cell_plan2 = new PdfPCell(new Phrase(
                    (currencyFormat.format(Double.parseDouble(basicCoverSumAssured))),
                    small_bold));
            cell_plan2.setPadding(5);
            cell_plan2.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_PremiumPayingTerm1 = new PdfPCell(new Paragraph(
                    "Premium Payment Term (Years)", small_normal));
            PdfPCell cell_PremiumPayingTerm2 = new PdfPCell(new Paragraph(
                    payingTerm, small_bold));
            cell_PremiumPayingTerm1.setPadding(5);
            cell_PremiumPayingTerm2.setPadding(5);
            cell_PremiumPayingTerm2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_plan11 = new PdfPCell(new Paragraph("Sum Assured on Death (at inception of the policy) (Rs.)",
                    small_normal));
            cell_plan11.setPadding(5);
            PdfPCell cell_plan22 = new PdfPCell(new Phrase(
                    (currencyFormat.format(Double.parseDouble(sumAssuredOnDeath))),
                    small_bold));
            cell_plan22.setPadding(5);
            cell_plan22.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_plan111 = new PdfPCell(new Paragraph("Mode / Frequency of Premium Payment",
                    small_normal));
            cell_plan111.setPadding(5);
            PdfPCell cell_plan222 = new PdfPCell(new Phrase(
                    premPayingMode,
                    small_bold));
            cell_plan222.setPadding(5);
            cell_plan222.setHorizontalAlignment(Element.ALIGN_CENTER);


            Table_policyTerm_annualisedPremium_sumAssured.addCell(cell_Term1);
            Table_policyTerm_annualisedPremium_sumAssured.addCell(cell_Term2);


            Table_policyTerm_annualisedPremium_sumAssured.addCell(cell_plan1);
            Table_policyTerm_annualisedPremium_sumAssured.addCell(cell_plan2);

            Table_policyTerm_annualisedPremium_sumAssured.addCell(cell_PremiumPayingTerm1);
            Table_policyTerm_annualisedPremium_sumAssured.addCell(cell_PremiumPayingTerm2);

            Table_policyTerm_annualisedPremium_sumAssured.addCell(cell_plan11);
            Table_policyTerm_annualisedPremium_sumAssured.addCell(cell_plan22);

            Table_policyTerm_annualisedPremium_sumAssured.addCell(cell_plan111);
            Table_policyTerm_annualisedPremium_sumAssured.addCell(cell_plan222);


            document.add(Table_policyTerm_annualisedPremium_sumAssured);

            PdfPTable table_plan_premium_payingTerm = new PdfPTable(4);
            table_plan_premium_payingTerm.setWidthPercentage(100);


            //table_plan_premium_payingTerm.addCell(cell_premium_paying_term1);
            //table_plan_premium_payingTerm.addCell(cell_premium_paying_term2);
            //document.add(table_plan_premium_payingTerm);


            PdfPTable table_plan_premium_payingTerm2 = new PdfPTable(4);
            table_plan_premium_payingTerm2.setWidthPercentage(100);


            // table_plan_premium_payingTerm2.addCell(cell_premium_paying_term11);
            // table_plan_premium_payingTerm2.addCell(cell_premium_paying_term22);
            //document.add(table_plan_premium_payingTerm2);


            PdfPTable table_plan_premium_payingTerm22 = new PdfPTable(2);
            table_plan_premium_payingTerm22.setWidthPercentage(100);


            PdfPCell cell_premium_paying_term11 = new PdfPCell(new Paragraph(
                    " Rate of Applicable Taxes", small_normal));
            cell_premium_paying_term11.setPadding(5);

            String rate_of_applicable_tax = "";
            /*if (str_kerla_discount.equalsIgnoreCase("Yes")) {
                rate_of_applicable_tax = "4.75% in the 1st policy year and 2.375% from 2nd policy year onwards";
            } else {
                rate_of_applicable_tax = "4.5% in the 1st policy year and 2.25% from 2nd policy year onwards";
            }*/


            // str_kerla_discount = prsObj.parseXmlTag(input, "str_kerla_discount");


            if (cb_kerladisc.isChecked()) {

                rate_of_applicable_tax = " 4.75% in the 1st policy year and 2.375% from 2nd policy year onwards";
            } else {

                rate_of_applicable_tax = "4.5% in the 1st policy year and 2.25% from 2nd policy year onwards";

            }


            PdfPCell cell_premium_paying_term22 = new PdfPCell(new Phrase(
                    rate_of_applicable_tax,
                    small_bold));

            cell_premium_paying_term22.setPadding(5);
            cell_premium_paying_term22
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell blank_1 = new PdfPCell(
                    new Paragraph("", small_normal));
            blank.setPadding(5);
            PdfPCell blank_2 = new PdfPCell(
                    new Paragraph("", small_bold));
            blank2.setPadding(5);
            blank2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);


            table_plan_premium_payingTerm22.addCell(cell_premium_paying_term11);
            table_plan_premium_payingTerm22.addCell(cell_premium_paying_term22);
            //table_plan_premium_payingTerm22.addCell(blank_1);
            //table_plan_premium_payingTerm22.addCell(blank_2);
            document.add(table_plan_premium_payingTerm22);


            PdfPTable table_plan_backdating = new PdfPTable(2);
            table_plan_backdating.setWidthPercentage(100);

            PdfPCell cell_Backdate1 = new PdfPCell(new Paragraph(
                    "Backdating Interest", small_normal));
            cell_Backdate1.setPadding(5);
            PdfPCell cell_Backdate2 = new PdfPCell(new Paragraph("Rs "
                    ,
                    small_bold));

            cell_Backdate2.setPadding(5);
            cell_Backdate2.setHorizontalAlignment(Element.ALIGN_CENTER);

            table_plan_backdating.addCell(cell_Backdate1);
            table_plan_backdating.addCell(cell_Backdate2);
            //document.add(table_plan_backdating);

            PdfPTable Table_Preferred_term_assured_policyTerm_annualisedPremium_sumAssured = new PdfPTable(
                    6);
            Table_Preferred_term_assured_policyTerm_annualisedPremium_sumAssured
                    .setWidthPercentage(100);

            PdfPCell cell_Preferred_term_assured_PlanProposed1 = new PdfPCell(
                    new Paragraph("Premium Paying Term", small_normal));
            cell_Preferred_term_assured_PlanProposed1.setPadding(5);
            PdfPCell cell_Preferred_term_assured_tPlanProposed2 = new PdfPCell(
                    new Paragraph("10" + " Years",
                            small_bold));
            cell_Preferred_term_assured_tPlanProposed2.setPadding(5);
            cell_Preferred_term_assured_tPlanProposed2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_Preferred_term_assured_Term1 = new PdfPCell(
                    new Paragraph("Sum Assured", small_normal));
            PdfPCell cell_Preferred_term_assured_Term2 = new PdfPCell(
                    new Phrase(
                            (currencyFormat.format(Double
                                    .parseDouble(basicCoverSumAssured))),
                            small_bold));
            cell_Preferred_term_assured_Term1.setPadding(5);
            cell_Preferred_term_assured_Term2.setPadding(5);
            cell_Preferred_term_assured_Term2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_Preferred_term_assured_PremiumPayingTerm1 = new PdfPCell(
                    new Paragraph("Annualised Premium",
                            small_normal));
            PdfPCell cell_Preferred_term_assured_PremiumPayingTerm2 = new PdfPCell(
                    new Phrase((currencyFormat.format(Double
                            .parseDouble("10000"))), small_bold));
            cell_Preferred_term_assured_PremiumPayingTerm1.setPadding(5);
            cell_Preferred_term_assured_PremiumPayingTerm2.setPadding(5);
            cell_Preferred_term_assured_PremiumPayingTerm2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            // Table_Preferred_term_assured_policyTerm_annualisedPremium_sumAssured
            // .addCell(cell_Preferred_term_assured_PlanProposed1);
            // Table_Preferred_term_assured_policyTerm_annualisedPremium_sumAssured
            // .addCell(cell_Preferred_term_assured_tPlanProposed2);
            //
            Table_Preferred_term_assured_policyTerm_annualisedPremium_sumAssured
                    .addCell(cell_Preferred_term_assured_Term1);
            Table_Preferred_term_assured_policyTerm_annualisedPremium_sumAssured
                    .addCell(cell_Preferred_term_assured_Term2);
            //
            Table_Preferred_term_assured_policyTerm_annualisedPremium_sumAssured
                    .addCell(cell_Preferred_term_assured_PremiumPayingTerm1);
            Table_Preferred_term_assured_policyTerm_annualisedPremium_sumAssured
                    .addCell(cell_Preferred_term_assured_PremiumPayingTerm2);
            //
            // document.add(Table_Preferred_term_assured_policyTerm_annualisedPremium_sumAssured);
            document.add(para_img_logo_after_space_1);

            // PdfPTable BI_Pdftable_totalPremiumforBaseProduct = new
            // PdfPTable(1);
            // BI_Pdftable_totalPremiumforBaseProduct.setWidthPercentage(100);
            // PdfPCell BI_Pdftable_totalPremiumforBaseProductcell = new
            // PdfPCell(
            // new Paragraph(
            // "Total Premium for Base Product & Rider (if any) (in Rs )",
            // small_bold));
            //
            // BI_Pdftable_totalPremiumforBaseProductcell
            // .setHorizontalAlignment(Element.ALIGN_CENTER);
            // BI_Pdftable_totalPremiumforBaseProductcell.setPadding(5);
            //
            // BI_Pdftable_totalPremiumforBaseProduct
            // .addCell(BI_Pdftable_totalPremiumforBaseProductcell);
            // document.add(BI_Pdftable_totalPremiumforBaseProduct);
            //
            // PdfPTable
            // Table_AccidetnalAndParmantRider_BasicPremium_servicePremium_yearlyPremium
            // = new PdfPTable(
            // 4);
            // Table_AccidetnalAndParmantRider_BasicPremium_servicePremium_yearlyPremium
            // .setWidthPercentage(100);
            //
            // PdfPCell cell_AccidetnalAndParmantRider_BasicPremium1 = new
            // PdfPCell(
            // new Paragraph(premium_paying_frequency
            // + " Installment Base Premium", small_normal));
            // cell_AccidetnalAndParmantRider_BasicPremium1.setPadding(5);
            // PdfPCell cell_AccidetnalAndParmantRider_BasicPremium2 = new
            // PdfPCell(
            // new Paragraph("Rs "
            // + obj.getRound(obj.getStringWithout_E(Double
            // .valueOf(basicprem.equals("") ? "0"
            // : basicprem))), small_bold));
            // cell_AccidetnalAndParmantRider_BasicPremium2.setPadding(5);
            // cell_AccidetnalAndParmantRider_BasicPremium2
            // .setHorizontalAlignment(Element.ALIGN_CENTER);
            //
            // PdfPCell cell_AccidetnalAndParmantRider_ServiceTax1 = new
            // PdfPCell(
            // new Paragraph("GST", small_normal));
            // PdfPCell cell_AccidetnalAndParmantRider_ServiceTax2 = new
            // PdfPCell(
            // new Paragraph("Rs  "
            // + obj.getRound(obj.getStringWithout_E(Double
            // .valueOf(servcTax.equals("") ? "0"
            // : servcTax))), small_bold));
            // cell_AccidetnalAndParmantRider_ServiceTax1.setPadding(5);
            // cell_AccidetnalAndParmantRider_ServiceTax2.setPadding(5);
            // cell_AccidetnalAndParmantRider_ServiceTax2
            // .setHorizontalAlignment(Element.ALIGN_CENTER);
            //
            // Table_AccidetnalAndParmantRider_BasicPremium_servicePremium_yearlyPremium
            // .addCell(cell_AccidetnalAndParmantRider_BasicPremium1);
            // Table_AccidetnalAndParmantRider_BasicPremium_servicePremium_yearlyPremium
            // .addCell(cell_AccidetnalAndParmantRider_BasicPremium2);
            //
            // Table_AccidetnalAndParmantRider_BasicPremium_servicePremium_yearlyPremium
            // .addCell(cell_AccidetnalAndParmantRider_ServiceTax1);
            // Table_AccidetnalAndParmantRider_BasicPremium_servicePremium_yearlyPremium
            // .addCell(cell_AccidetnalAndParmantRider_ServiceTax2);
            //
            // document.add(Table_AccidetnalAndParmantRider_BasicPremium_servicePremium_yearlyPremium);
            //
            // PdfPTable Table_backdating_premium_with_service_tax = new
            // PdfPTable(
            // 2);
            // Table_backdating_premium_with_service_tax.setWidthPercentage(100);
            //
            // PdfPCell cell_Backdate1 = new PdfPCell(new Paragraph(
            // "Backdating Interest", small_normal));
            // cell_Backdate1.setPadding(5);
            //
            // PdfPCell cell_Backdate2 = new PdfPCell(new Paragraph("Rs "
            // + obj.getRound(obj.getStringWithout_E(Double
            // .valueOf(BackdatingInt.equals("") ? "0"
            // : BackdatingInt))), small_bold));
            // cell_Backdate2.setPadding(5);
            // cell_Backdate2.setHorizontalAlignment(Element.ALIGN_CENTER);
            //
            // PdfPCell cell_AccidetnalAndParmantRider_YearlyPremium1 = new
            // PdfPCell(
            // new Paragraph(premium_paying_frequency
            // + "  Installment Base Premium with GST",
            // small_normal));
            // PdfPCell cell_AccidetnalAndParmantRider_YearlyPremium2 = new
            // PdfPCell(
            // new Paragraph("Rs  "
            // + obj.getRound(obj.getStringWithout_E(Double
            // .valueOf(premWthST.equals("") ? "0"
            // : premWthST))), small_bold));
            // cell_AccidetnalAndParmantRider_YearlyPremium1.setPadding(5);
            // cell_AccidetnalAndParmantRider_YearlyPremium2.setPadding(5);
            // cell_AccidetnalAndParmantRider_YearlyPremium2
            // .setHorizontalAlignment(Element.ALIGN_CENTER);
            //
            // Table_backdating_premium_with_service_tax.addCell(cell_Backdate1);
            // Table_backdating_premium_with_service_tax.addCell(cell_Backdate2);
            //
            // Table_backdating_premium_with_service_tax
            // .addCell(cell_AccidetnalAndParmantRider_YearlyPremium1);
            // Table_backdating_premium_with_service_tax
            // .addCell(cell_AccidetnalAndParmantRider_YearlyPremium2);
            // document.add(Table_backdating_premium_with_service_tax);

            document.add(para_img_logo_after_space_1);

            /**** Added By - Priyanka Warekar 26-08-2015 - Start *****/
            PdfPTable FY_SY_premDetail_table = new PdfPTable(4);
            FY_SY_premDetail_table.setWidths(new float[]{5f, 5f, 5f, 5f});
            FY_SY_premDetail_table.setWidthPercentage(100f);
            FY_SY_premDetail_table.setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell cell;

            // 1st row
            cell = new PdfPCell(new Phrase(
                    "Premium Summary ", small_bold));
            cell.setColspan(7);
            cell.setPadding(5);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.BOTTOM | Rectangle.LEFT | Rectangle.RIGHT
                    | Rectangle.TOP);
            FY_SY_premDetail_table.addCell(cell);

            // 2 row
            cell = new PdfPCell(new Phrase("", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(" Base Plan ", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            FY_SY_premDetail_table.addCell(cell);

            // cell = new PdfPCell(new Phrase("(a)Basic GST(Rs.)",
            // small_normal));
            // cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            // FY_SY_premDetail_table.addCell(cell);
            //
            // cell = new PdfPCell(new Phrase("(b)Swachh Bharat Cess(Rs.)",
            // small_normal));
            // cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            // FY_SY_premDetail_table.addCell(cell);
            //
            // cell = new PdfPCell(new Phrase("(c)Krishi Kalyan Cess(Rs.)",
            // small_normal));
            // cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            // FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Riders", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(" Total Installment Premium ", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            FY_SY_premDetail_table.addCell(cell);


            cell = new PdfPCell(new Phrase("Installment Premium without Applicable Taxes (Rs.)  ", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            FY_SY_premDetail_table.addCell(cell);

           /* cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                    .parseDouble(totInstPrem_exclST)), small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            FY_SY_premDetail_table.addCell(cell);*/
            cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                    .parseDouble(premiumDetailFYBasicPremium_without_tax)), small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            FY_SY_premDetail_table.addCell(cell);


            cell = new PdfPCell(new Phrase("Not Applicable", small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                    .parseDouble(premiumDetailFYBasicPremium_without_tax)), small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            FY_SY_premDetail_table.addCell(cell);


            // 3 row
            cell = new PdfPCell(new Phrase("Installment Premium with 1st Year Applicable Taxes (Rs.) ", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            FY_SY_premDetail_table.addCell(cell);

           /* cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                    .parseDouble(totInstPrem_exclST)), small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            FY_SY_premDetail_table.addCell(cell);*/
            cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                    .parseDouble(premiumDetailFYBasicPremium)), small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            FY_SY_premDetail_table.addCell(cell);

            // cell = new PdfPCell(new Phrase(
            // CommonForAllProd.getRound(CommonForAllProd
            // .getStringWithout_E(Double.valueOf(basicServiceTax
            // .equals("") ? "0" : basicServiceTax))),
            // small_normal));
            // cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            // FY_SY_premDetail_table.addCell(cell);
            //
            // cell = new PdfPCell(new Phrase(
            // CommonForAllProd.getRound(CommonForAllProd
            // .getStringWithout_E(Double.valueOf(SBCServiceTax
            // .equals("") ? "0" : SBCServiceTax))),
            // small_normal));
            // cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            // FY_SY_premDetail_table.addCell(cell);
            //
            // cell = new PdfPCell(new Phrase(
            // CommonForAllProd.getRound(CommonForAllProd
            // .getStringWithout_E(Double.valueOf(KKCServiceTax
            // .equals("") ? "0" : KKCServiceTax))),
            // small_normal));
            // cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            // FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Not Applicable", small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                    .parseDouble(premiumDetailFYBasicPremium)), small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            FY_SY_premDetail_table.addCell(cell);

            // 4 row
            cell = new PdfPCell(new Phrase("Installment Premium with Applicable Taxes 2nd Year onwards (Rs.)",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                    .parseDouble(premiumDetailSYBasicPremium)), small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Not Applicable"
                    , small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                    .parseDouble(premiumDetailSYBasicPremium)), small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            FY_SY_premDetail_table.addCell(cell);


            /**** Added By - Priyanka Warekar 26-08-2015 - End *****/

            /**** Added By - Priyanka Warekar - 26-08-2015 - Start ****/
            document.add(FY_SY_premDetail_table);
            /**** Added By - Priyanka Warekar - 26-08-2015 - End ****/

            document.add(para_img_logo_after_space_1);

            PdfPTable BI_Pdftable_note = new PdfPTable(1);
            BI_Pdftable_note.setWidthPercentage(100);
            PdfPCell BI_Pdftable_note_cell1 = new PdfPCell(new Paragraph(
                    "Please Note", small_bold));
            BI_Pdftable_note_cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_Pdftable_note_cell1.setPadding(5);

            BI_Pdftable_note.addCell(BI_Pdftable_note_cell1);
            document.add(BI_Pdftable_note);

            PdfPTable BI_Pdftable6 = new PdfPTable(1);
            BI_Pdftable6.setWidthPercentage(100);
            PdfPCell BI_Pdftable6_cell6 = new PdfPCell(
                    new Paragraph(
                            "1. The premiums can also be paid by giving standing instruction to your bank or you can pay through your credit card.",
                            small_normal));

            BI_Pdftable6_cell6.setPadding(5);

            BI_Pdftable6.addCell(BI_Pdftable6_cell6);
            document.add(BI_Pdftable6);

            PdfPTable BI_Pdftable7 = new PdfPTable(1);
            BI_Pdftable7.setWidthPercentage(100);
            PdfPCell BI_Pdftable7_cell1 = new PdfPCell(
                    new Paragraph(
                            "2. Applicable Taxes (including surcharge/cess etc), at the rate notified by the Central Government/ State Government / Union Territories of India from time to time and as per the provisions of the prevalent tax laws will be payable on premium as per the product features.",
                            small_normal));

            BI_Pdftable7_cell1.setPadding(5);

            BI_Pdftable7.addCell(BI_Pdftable7_cell1);
            document.add(BI_Pdftable7);

           /* PdfPTable taxes_desc = new PdfPTable(1);
            taxes_desc.setWidthPercentage(100);
            PdfPCell taxes_desc_cell = new PdfPCell(
                    new Paragraph(
                            "3. Applicable Taxes (including surcharge/cess etc), at the rate notified by the Central Government/ State Government / Union Territories of India from time to time and as per the provisions of the prevalent tax laws will be payable on premium as per the product features.  ",

                            small_normal));

            taxes_desc_cell.setPadding(5);

            taxes_desc.addCell(taxes_desc_cell);
            document.add(taxes_desc);
*/

            document.add(para_img_logo_after_space_1);

            document.add(para_img_logo_after_space_1);
            PdfPTable BI_Pdftable19 = new PdfPTable(1);
            BI_Pdftable19.setWidthPercentage(100);
            PdfPCell BI_Pdftable19_cell1 = new PdfPCell(new Paragraph(
                    "Benefit Illustration for SBI Life - Smart Samriddhi  (Amount in Rs.)",
                    small_bold));
            BI_Pdftable19_cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_Pdftable19_cell1.setPadding(5);

            BI_Pdftable19.addCell(BI_Pdftable19_cell1);
            document.add(BI_Pdftable19);

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
                    "Survival Benefits / Loyalty Additions", small_bold2));
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


                PdfPCell cell_guaranteed_maturity_benefit = new PdfPCell(new Phrase(
                        currencyFormat.format(Double.parseDouble(prsObj
                                .parseXmlTag(output, "guarntdSurvivalBen"
                                        + (i + 1)))), small_bold2));
                cell_guaranteed_maturity_benefit.setPadding(5);
                cell_guaranteed_maturity_benefit
                        .setHorizontalAlignment(Element.ALIGN_CENTER);


                PdfPCell cell_guarantedDeathBenefit = new PdfPCell(new Phrase(
                        currencyFormat.format(Double.parseDouble(prsObj
                                .parseXmlTag(output, "guarntdDeathBen"
                                        + (i + 1)))), small_bold2));
                cell_guarantedDeathBenefit.setPadding(5);
                cell_guarantedDeathBenefit
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
                                        "nonGuarntdSurrndrVal" + (i + 1)))),
                                small_bold2));
                cell_guarantedSurrenderValue2.setPadding(5);
                cell_guarantedSurrenderValue2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);

                Table_BI_Header2.addCell(cell_EndOfYear3);
                Table_BI_Header2.addCell(cell_AnnPrem);
                Table_BI_Header2.addCell(cell_cummulativePremiumPaid);
                Table_BI_Header2.addCell(cell_cummulativePremiumPaid2);
                Table_BI_Header2.addCell(cell_guaranteed_maturity_benefit);
                Table_BI_Header2.addCell(cell_guarantedDeathBenefit);
                Table_BI_Header2.addCell(cell_guarantedSurrenderValue);
                Table_BI_Header2.addCell(cell_guarantedSurrenderValue2);
                document.add(Table_BI_Header2);
            }

            // Paragraph para_note = new Paragraph(
            // "Benefit Payable to the nominee on death Higher of i) Sum Assured on death# + Vested Simple Reversionary Bonuses + Terminal bonus, if any. OR ii) 105% of all the basic premiums paid.",
            // small_normal);
            // document.add(para_note);
            // document.add(para_img_logo_after_space_1);
            //
            // Paragraph para_EndowmentOption = new Paragraph(
            // "#For details on Sum Assured on death please refer the Sales Brochure",
            // small_normal);
            // document.add(para_EndowmentOption);
            //
            document.add(para_img_logo_after_space_1);


            PdfPTable BI_Pdftable_note2 = new PdfPTable(1);
            BI_Pdftable_note2.setWidthPercentage(100);
            PdfPCell BI_Pdftable_note_cell12 = new PdfPCell(new Paragraph(
                    " Notes", small_bold));
            BI_Pdftable_note_cell12.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_Pdftable_note_cell12.setPadding(5);

            BI_Pdftable_note2.addCell(BI_Pdftable_note_cell12);
            document.add(BI_Pdftable_note2);

            PdfPTable BI_Pdftable62 = new PdfPTable(1);
            BI_Pdftable62.setWidthPercentage(100);
            PdfPCell BI_Pdftable6_cell62 = new PdfPCell(
                    new Paragraph(
                            "1. Annualized Premium  shall  be  the  premium  amount  payable  in  a  year,  chosen  by  the policyholder, excluding applicable taxes and loadings for modal premiums, if any. Refer sales literature for explanation of terms used in this illustration.",
                            small_normal));

            BI_Pdftable6_cell62.setPadding(5);

            BI_Pdftable62.addCell(BI_Pdftable6_cell62);
            document.add(BI_Pdftable62);

            PdfPTable BI_Pdftable624 = new PdfPTable(1);
            BI_Pdftable624.setWidthPercentage(100);
            PdfPCell BI_Pdftable6_cell624 = new PdfPCell(
                    new Paragraph(
                            "2. All Benefit amount are derived on the assumption that the policies are 'in-force'",
                            small_normal));

            BI_Pdftable6_cell624.setPadding(5);

            BI_Pdftable624.addCell(BI_Pdftable6_cell624);
            document.add(BI_Pdftable624);

            PdfPTable BI_Pdftable625 = new PdfPTable(1);
            BI_Pdftable625.setWidthPercentage(100);
            PdfPCell BI_Pdftable6_cell625 = new PdfPCell(
                    new Paragraph(
                            "3. In addition to Minimum Guaranteed Surrender Value, Surrender value of the Accrued Guaranteed Additions will also be paid. For the purpose of guaranteed surrender value (GSV) in this illustration the surrender value of the accrued guaranteed additions are not considered at all.",
                            small_normal));

            BI_Pdftable6_cell625.setPadding(5);

            BI_Pdftable625.addCell(BI_Pdftable6_cell625);
            document.add(BI_Pdftable625);

           /* PdfPTable BI_Pdftable626 = new PdfPTable(1);
            BI_Pdftable626.setWidthPercentage(100);
            PdfPCell BI_Pdftable6_cell626 = new PdfPCell(
                    new Paragraph(
                            "4. The Guaranteed Surrender Value (GSV) will be equal to GSV factors multiplied by the total premiums paid.   ",
                            small_normal));

            BI_Pdftable6_cell626.setPadding(5);

            BI_Pdftable626.addCell(BI_Pdftable6_cell626);
            //document.add(BI_Pdftable626);*/

           /* PdfPTable BI_Pdftable628 = new PdfPTable(1);
            BI_Pdftable628.setWidthPercentage(100);
            PdfPCell BI_Pdftable6_cell628 = new PdfPCell(
                    new Paragraph(
                            "5. In practice, the company may pay a surrender value which could be higher than the guaranteed surrender value.  The surrender value payable may be reviewed from time to time depending on company's experience of the various factors which impact the surrender values that may be paid. The surrender value would be higher of GSV or SSV.",
                            small_normal));

            BI_Pdftable6_cell628.setPadding(5);

            BI_Pdftable628.addCell(BI_Pdftable6_cell628);
            //document.add(BI_Pdftable628);
*/
          /*  PdfPTable BI_Pdftable629 = new PdfPTable(1);
            BI_Pdftable629.setWidthPercentage(100);
            PdfPCell BI_Pdftable6_cell629 = new PdfPCell(
                    new Paragraph(
                            "6. For further information on the product and its benefits, please refer to the sales brochure and/or policy document. Further information will also be available on request.  ",
                            small_normal));

            BI_Pdftable6_cell629.setPadding(5);

            BI_Pdftable629.addCell(BI_Pdftable6_cell629);
            //document.add(BI_Pdftable629);*/

            PdfPTable BI_Pdftable_note23 = new PdfPTable(1);
            BI_Pdftable_note23.setWidthPercentage(100);
            PdfPCell BI_Pdftable_note_cell123 = new PdfPCell(new Paragraph(
                    " Important: ", small_bold));
            BI_Pdftable_note_cell123.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_Pdftable_note_cell123.setPadding(5);

            BI_Pdftable_note23.addCell(BI_Pdftable_note_cell123);
            document.add(BI_Pdftable_note23);

            PdfPTable BI_Pdftable_CompanysPolicySurrender1 = new PdfPTable(1);
            BI_Pdftable_CompanysPolicySurrender1.setWidthPercentage(100);


            // ///
            PdfPCell BI_Pdftable_CompanysPolicySurrender2_cell = new PdfPCell(
                    new Paragraph(
                            "You may receive a welcome call from our representative to confirm your proposal details like Date of Birth,Nominee Name,Address,Email Id,Sum Assured,Premium amount,Premium Payment Term etc.",
                            small_normal));

            BI_Pdftable_CompanysPolicySurrender2_cell.setPadding(5);

            BI_Pdftable_CompanysPolicySurrender1
                    .addCell(BI_Pdftable_CompanysPolicySurrender2_cell);

            PdfPCell BI_Pdftable_CompanysPolicySurrender3_cell = new PdfPCell(
                    new Paragraph(
                            "You may have to undergo Medical tests based on our underwriting requirements.",
                            small_normal));

            BI_Pdftable_CompanysPolicySurrender3_cell.setPadding(5);

           /* BI_Pdftable_CompanysPolicySurrender1
                    .addCell(BI_Pdftable_CompanysPolicySurrender3_cell);*/

            PdfPCell BI_Pdftable_CompanysPolicySurrender1_cell = new PdfPCell(
                    new Paragraph(
                            "You have to submit Proof of source of Fund",
                            small_normal));

            BI_Pdftable_CompanysPolicySurrender1_cell.setPadding(5);

            //  BI_Pdftable_CompanysPolicySurrender1.addCell(BI_Pdftable_CompanysPolicySurrender1_cell);



		/*	PdfPCell BI_Pdftable_CompanysPolicySurrender4_cell = new PdfPCell(
					new Paragraph(
							"Your SBI LIFE - Smart Swadhan Plus(UIN: 111N104V02) is a Regular/Limited premium policy,for which your first year "
									+ premium_paying_frequency
									+ " premium is Rs. "
									+ CommonForAllProd.getRound(CommonForAllProd
											.getStringWithout_E(Double
													.valueOf(premWthST
															.equals("") ? "0"
															: premWthST)))
									+ " Your policy Term is "
									+ policy_term
									+ " years,Premium Payment Term is "
									+ policy_term
									+ " years and Basic Sum Assured is Rs. "
									+ getformatedThousandString(Integer.parseInt(CommonForAllProd.getRound(CommonForAllProd.getStringWithout_E(Double
											.valueOf((sum_assured.equals("") || sum_assured == null) ? "0"
													: sum_assured))))),
							small_normal));

			BI_Pdftable_CompanysPolicySurrender4_cell.setPadding(5);

			BI_Pdftable_CompanysPolicySurrender1
					.addCell(BI_Pdftable_CompanysPolicySurrender4_cell);*/

            // ////

            document.add(BI_Pdftable_CompanysPolicySurrender1);

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
            //  document.add(new_line);
           /* PdfPTable BI_Pdftable_eSign = new PdfPTable(1);
            BI_Pdftable_eSign.setWidthPercentage(100);
            // CR_table6.setWidths(columnWidths_26);
            PdfPCell BI_Pdftable_eSign_cell1 = new PdfPCell(new Paragraph(

                    "This document is eSigned by " + name_of_person, small_bold));

            BI_Pdftable_eSign_cell1.setPadding(5);

            BI_Pdftable_eSign.addCell(BI_Pdftable_eSign_cell1);
			document.add(BI_Pdftable_eSign);
			document.add(para_img_logo_after_space_1);*/


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

                BI_PdftableMarketing
                        .addCell(BI_PdftableMarketing_signature_cell);
                document.add(BI_PdftableMarketing);

                PdfPTable agentDeclarationTable = new PdfPTable(1);
                agentDeclarationTable.setWidthPercentage(100);
                PdfPCell agentDeclaration = new PdfPCell(
                        new Paragraph(commonMethods.getAgentDeclaration(context), small_bold));

                agentDeclaration.setPadding(5);
                agentDeclarationTable.addCell(agentDeclaration);
                document.add(agentDeclarationTable);


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

        if (edt_proposerdetail_basicdetail_contact_no.getText().toString()
                .equals("")) {
            dialog("Please Fill  Mobile No", true);
            edt_proposerdetail_basicdetail_contact_no.requestFocus();
            return false;
        } else if (edt_proposerdetail_basicdetail_contact_no.getText()
                .toString().length() != 10) {
            dialog("Please Fill 10 Digit Mobile No", true);
            edt_proposerdetail_basicdetail_contact_no.requestFocus();
            return false;
        } /*else if (emailId.equals("")) {
            dialog("Please Fill Email Id", true);
            edt_proposerdetail_basicdetail_Email_id.requestFocus();
            return false;

        } else if (ConfirmEmailId.equals("")) {

            dialog("Please Fill Confirm Email Id", true);
            edt_proposerdetail_basicdetail_ConfirmEmail_id.requestFocus();
            return false;
        } else if (!ConfirmEmailId.equals(emailId)) {
            dialog("Email Id Does Not Match", true);
            return false;
        } */ else if (!emailId.equals("")) {

            email_id_validation(emailId);
            if (validationFla1) {
                if (ConfirmEmailId.equals("")) {

                    dialog("Please Fill Confirm Email Id", true);
                    edt_proposerdetail_basicdetail_ConfirmEmail_id.requestFocus();
                    return false;
                } else if (!ConfirmEmailId.equals(emailId)) {
                    dialog("Email Id Does Not Match", true);
                    return false;
                }
                return true;
            } else {
                dialog("Please Fill Valid Email Id", true);
                edt_proposerdetail_basicdetail_Email_id.requestFocus();
                return false;
            }
        } else if (!ConfirmEmailId.equals("")) {

            email_id_validation(ConfirmEmailId);
            if (validationFla1) {

                if (emailId.equals("")) {
                    dialog("Please Fill Email Id", true);
                    edt_proposerdetail_basicdetail_Email_id.requestFocus();
                    return false;
                }

                return true;
            } else {
                dialog("Please Fill Valid Confirm Email Id", true);
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

}

