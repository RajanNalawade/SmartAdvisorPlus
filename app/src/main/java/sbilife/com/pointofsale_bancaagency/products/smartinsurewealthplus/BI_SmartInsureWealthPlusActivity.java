package sbilife.com.pointofsale_bancaagency.products.smartinsurewealthplus;

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
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
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
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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

public class BI_SmartInsureWealthPlusActivity extends AppCompatActivity implements
        TextView.OnEditorActionListener {
    private final int SIGNATURE_ACTIVITY = 1;
    private SmartInsureWealthPlusBean smartinsurewealthplusBean;
    private Spinner spnr_bi_smart_insure_wealth_plus_proposer_title;
    private EditText edt_bi_smart_insure_wealth_plus_proposer_first_name;
    private EditText edt_bi_smart_insure_wealth_plus_proposer_middle_name;
    private EditText edt_bi_smart_insure_wealth_plus_proposer_last_name;
    private Button btn_bi_smart_insure_wealth_plus_proposer_date;
    private TextView textviewProposerAge;
    private EditText edt_bi_smart_insure_wealth_plus_life_assured_first_name,
            edt_bi_smart_insure_wealth_plus_life_assured_middle_name,
            edt_bi_smart_insure_wealth_plus_life_assured_last_name;
    private Spinner spnr_bi_smart_insure_wealth_plus_life_assured_title;
    // variable declaration
    private Button btn_bi_smart_insure_wealth_plus_life_assured_date;
    private String lifeAssured_Title = "", lifeAssured_First_Name = "",
            lifeAssured_Middle_Name = "", lifeAssured_Last_Name = "",
            name_of_life_assured = "", lifeAssured_date_of_birth = "",
            lifeAssuredAge = "";
    private RadioButton rb_smart_insure_wealth_plus_trigger_fund_yes,
            rb_smart_insure_wealth_plus_trigger_fund_no,
            rb_smart_insure_wealth_plus_auto_allocation_yes,
            rb_smart_insure_wealth_plus_auto_allocation_no;
    // Retrieving value from database and storing
    private String age_entry = "", propserAge = "";
    private String maturityAge = "";
    private String gender = "";
    private String planType = "";
    private String annPrem = "";
    private String netYield8pa = "";
    private String sumAssured = "";
    private String mode_of_policy = "";
    private String plan = "";
    private String premPayTerm = "";
    private String perInvEquityFund = "";
    private String perInvEquityOptimiserFund = "";
    private String perInvgrowthFund = "";
    private String perInvBalancedFund = "";
    private String perInvPureFund = "";
    private String perInvMidCapFund = "";
    private String perInvBondOptimiser = "";
    private String perInvCorpBondFund = "";
    private String perInvMoneyMktFund = "";
    private String premFreqMode = "";
    private String redInYieldNoYr = "";
    private String policyTerm = "";
    // For Bi Dialog
    private String name_of_proposer = "";
    private String strProposerAge = "";
    private String name_of_person = "";
    private String place2 = "";
    private String date1 = "";
    private String date2 = "";
    private String agent_sign = "";
    private String proposer_sign = "";
    private File mypath;
    private String proposer_Title = "", proposer_First_Name = "",
            proposer_Middle_Name = "", proposer_Last_Name = "",
            proposer_Is_Same_As_Life_Assured = "",
            is_trigger_fund = "",
            auto_allocation = "";
    private Button btn_MarketingOfficalDate;
    private Button btn_PolicyholderDate;
    private ImageButton Ibtn_signatureofMarketing;
    private ImageButton Ibtn_signatureofPolicyHolders;
    private String latestImage = "";
    // for BI
    private StringBuilder inputVal;
    private String proposer_date_of_birth = "";
    private String staffdiscount = "";
    private EditText edt_proposerdetail_basicdetail_contact_no,
            edt_proposerdetail_basicdetail_Email_id,
            edt_proposerdetail_basicdetail_ConfirmEmail_id;
    private String mobileNo = "";
    private String emailId = "";
    private String ConfirmEmailId = "";
    /* Basic Details */
    private String ProposerEmailId = "";
    private Bitmap photoBitmap;
    private String flg_needAnalyis = "";
    private String Company_policy_surrender_dec = "";
    private String gender_proposer = "";
    private String staffStatus = "";
    private String discountPercentage = "", effectivePremium;
    private Spinner ageInYears, selPlan, selPremiumFrequencyMode,
            selPolicyTerm;
    private TextView txtPremiumFreqMode;
    private LinearLayout tbSFINEquityFund, tbSFINPureFund, tbSFINBondOptimiser, tbSFINCorpBondFund, tableRow6,
    /*tableRow7,*/ tbSFINEquityOptFund, tbSFINGrowthFund,
            tbSFINBalancedFund, tbSFINMidCapFund, tbSFINMoneyMktFund, linearlayoutProposerDetails;
    private StringBuilder retVal;
    private StringBuilder bussIll = null;
    // For BI
    private TextView help_policyTerm, help_premiumAmt;
    private EditText premiumAmt,
            percent_EquityFund, percent_EquityOptFund, percent_GrowthFund,
            percent_BalancedFund, percent_PureFund, percent_MidCapFund,
            percent_BondOptimiser, percent_CorpBondFund, percent_MoneyMktFund;
    private int DIALOG_ID = 0;
    private CommonMethods commonMethods;
    private StorageUtils mStorageUtils;
    private Context context;
    private NeedAnalysisBIService NABIObj;
    private DatabaseHelper dbHelper;
    private ParseXML prsObj;
    private double SAMF = 0;
    private DecimalFormat currencyFormat;
    private CommonForAllProd commonForAllProd;
    private int needAnalysis_flag = 0;
    private String na_input, na_output, na_dob = "", bankUserType = "",
            mode = "", transactionMode = "", Check = "", product_Code,
            product_UIN, product_cateogory, product_type, planName = "",
            agentcode, agentMobile, agentEmail, userType, QuatationNumber;
    private LinearLayout tl_fund;
    private String sr_Code = "";
    private int mYear;
    private int mMonth;
    private int mDay;
    private final DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {

            mYear = year;
            mMonth = month;
            mDay = day;
            updateDisplay(DIALOG_ID);
        }
    };
    private boolean validationFla1 = false;
    private boolean validationFlag2 = false;
    private boolean validationFlag3 = false;
    private Button btnSubmit, back;
    private List<M_BI_SmartInsureWealthPlusAdapterCommon> list_data;
    // Context con;
    private List<M_BI_SmartInsureWealthPlusAdapterCommon2> list_data1, list_data2;
    private SmartInsureWealthPlusProperties prop;
    private CheckBox isStaffDisc, selViewSFIN;
    private ImageButton imageButtonSmartInsureWealthPlusProposerPhotograph;
    private NA_CBI_bean na_cbi_bean;
    private File newFile;
    private Spinner spnr_bi_selGender, spinnerProposerGender;

    private CheckBox cb_kerladisc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.bi_smartinsurewealthplusmain);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        commonMethods = new CommonMethods();
        mStorageUtils = new StorageUtils();
        commonMethods.setApplicationToolbarMenu(this, getString(R.string.app_name));
        context = this;


        dbHelper = new DatabaseHelper(this);
        NABIObj = new NeedAnalysisBIService(this);
        prsObj = new ParseXML();
        initialiseDate();
        smartinsurewealthplusBean = new SmartInsureWealthPlusBean();

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
            planName = getString(R.string.sbi_life_smart_insure_wealth_plus);
            product_Code = getString(R.string.sbi_life_smart_insure_wealth_plus_code);
            product_UIN = prodInfoObj.getProductUIN(planName);
            product_cateogory = prodInfoObj.getProductCategory(planName);
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
        } else {
            needAnalysis_flag = 0;
        }

        tl_fund = findViewById(R.id.tl_fund);
        spnr_bi_smart_insure_wealth_plus_proposer_title = findViewById(R.id.spnr_bi_smart_insure_wealth_plus_proposer_title);
        edt_bi_smart_insure_wealth_plus_proposer_first_name = findViewById(R.id.edt_bi_smart_insure_wealth_plus_proposer_first_name);
        edt_bi_smart_insure_wealth_plus_proposer_middle_name = findViewById(R.id.edt_bi_smart_insure_wealth_plus_proposer_middle_name);
        edt_bi_smart_insure_wealth_plus_proposer_last_name = findViewById(R.id.edt_bi_smart_insure_wealth_plus_proposer_last_name);
        spnr_bi_selGender = findViewById(R.id.spnr_bi_selGender);
        spinnerProposerGender = findViewById(R.id.spinnerProposerGender);

        btn_bi_smart_insure_wealth_plus_proposer_date = findViewById(R.id.btn_bi_smart_insure_wealth_plus_proposer_date);
        btn_bi_smart_insure_wealth_plus_life_assured_date = findViewById(R.id.btn_bi_smart_insure_wealth_plus_life_assured_date);
        rb_smart_insure_wealth_plus_trigger_fund_yes = findViewById(R.id.rb_smart_insure_wealth_plus_trigger_fund_yes);
        rb_smart_insure_wealth_plus_trigger_fund_no = findViewById(R.id.rb_smart_insure_wealth_plus_trigger_fund_no);
        rb_smart_insure_wealth_plus_auto_allocation_yes = findViewById(R.id.rb_smart_insure_wealth_plus_auto_allocation_yes);
        rb_smart_insure_wealth_plus_auto_allocation_no = findViewById(R.id.rb_smart_insure_wealth_plus_auto_allocation_no);
        textviewProposerAge = findViewById(R.id.textviewProposerAge);

        spnr_bi_smart_insure_wealth_plus_life_assured_title = findViewById(R.id.spnr_bi_smart_insure_wealth_plus_life_assured_title);
        edt_bi_smart_insure_wealth_plus_life_assured_first_name = findViewById(R.id.edt_bi_smart_insure_wealth_plus_life_assured_first_name);
        edt_bi_smart_insure_wealth_plus_life_assured_middle_name = findViewById(R.id.edt_bi_smart_insure_wealth_plus_life_assured_middle_name);
        edt_bi_smart_insure_wealth_plus_life_assured_last_name = findViewById(R.id.edt_bi_smart_insure_wealth_plus_life_assured_last_name);


        btnSubmit = findViewById(R.id.btnSubmit);
        back = findViewById(R.id.back);

        edt_proposerdetail_basicdetail_contact_no = findViewById(R.id.edt_smart_insure_wealth_plus_contact_no);
        edt_proposerdetail_basicdetail_Email_id = findViewById(R.id.edt_smart_insure_wealth_plus_Email_id);
        edt_proposerdetail_basicdetail_ConfirmEmail_id = findViewById(R.id.edt_smart_insure_wealth_plus_ConfirmEmail_id);


        /*List<String> title_list = new ArrayList<String>();


        title_list.add("Select Title");
        title_list.add("Mr.");
        title_list.add("Ms.");
        title_list.add("Mrs.");*/
        list_data = new ArrayList<M_BI_SmartInsureWealthPlusAdapterCommon>();
        list_data1 = new ArrayList<M_BI_SmartInsureWealthPlusAdapterCommon2>();
        list_data2 = new ArrayList<M_BI_SmartInsureWealthPlusAdapterCommon2>();

        commonMethods.fillSpinnerValue(context, spnr_bi_smart_insure_wealth_plus_proposer_title,
                Arrays.asList(getResources().getStringArray(R.array.arr_select_title)));

        commonMethods.fillSpinnerValue(context, spnr_bi_smart_insure_wealth_plus_life_assured_title,
                Arrays.asList(getResources().getStringArray(R.array.arr_select_title)));

        //Gender
        String[] genderList = {"Male", "Female", "Third Gender"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, genderList);
        genderAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_bi_selGender.setAdapter(genderAdapter);
        spinnerProposerGender.setAdapter(genderAdapter);
        genderAdapter.notifyDataSetChanged();

        prop = new SmartInsureWealthPlusProperties();

        currencyFormat = new DecimalFormat("##,##,##,###");

        isStaffDisc = findViewById(R.id.cb_staffdisc);

        // Age
        ageInYears = findViewById(R.id.age);
        ageInYears.setClickable(false);
        ageInYears.setEnabled(false);

        String[] ageList = new String[56];
        for (int i = 0; i <= 55; i++) {
            ageList[i] = i + "";
        }
        ArrayAdapter<String> ageAdapter = new ArrayAdapter<String>(
                getApplicationContext(), R.layout.spinner_item, ageList);
        ageAdapter.setDropDownViewResource(R.layout.spinner_item1);
        ageInYears.setAdapter(ageAdapter);
        ageAdapter.notifyDataSetChanged();

        // plan type
        selPlan = findViewById(R.id.planType);
        String[] planList = new String[]{"Regular"};
        ArrayAdapter<String> planAdapter = new ArrayAdapter<String>(
                getApplicationContext(), R.layout.spinner_item, planList);
        planAdapter.setDropDownViewResource(R.layout.spinner_item1);
        selPlan.setAdapter(planAdapter);
        selPlan.setSelection(0);
        selPlan.setEnabled(false);
        planAdapter.notifyDataSetChanged();


        // premium frequency mode
        txtPremiumFreqMode = findViewById(R.id.txt_premium_frequency_mode);
        selPremiumFrequencyMode = findViewById(R.id.premium_frequency_mode);
        String[] premFreq = new String[]{"Monthly"};
        ArrayAdapter<String> premFreqAdapter = new ArrayAdapter<String>(
                getApplicationContext(), R.layout.spinner_item, premFreq);
        premFreqAdapter.setDropDownViewResource(R.layout.spinner_item1);
        selPremiumFrequencyMode.setAdapter(premFreqAdapter);
        /*selPremiumFrequencyMode.setSelection(0);*/
        selPremiumFrequencyMode.setEnabled(false);
        premFreqAdapter.notifyDataSetChanged();

        // policy Term
        selPolicyTerm = findViewById(R.id.policyTerm);
        String[] policyTermList = new String[]{"10", "15", "20", "25"};
        ArrayAdapter<String> policyTermAdapter = new ArrayAdapter<String>(
                getApplicationContext(), R.layout.spinner_item, policyTermList);
        policyTermAdapter.setDropDownViewResource(R.layout.spinner_item1);
        selPolicyTerm.setAdapter(policyTermAdapter);
        policyTermAdapter.notifyDataSetChanged();

        // Premium Amount
        premiumAmt = findViewById(R.id.premium_Amt);

        // Funds
        percent_EquityFund = findViewById(R.id.equityFund);
        percent_EquityOptFund = findViewById(R.id.equityOptFund);
        percent_GrowthFund = findViewById(R.id.growthFund);
        percent_BalancedFund = findViewById(R.id.balancedFund);
        percent_PureFund = findViewById(R.id.PureFund);
        percent_MidCapFund = findViewById(R.id.MidCapFund);
        percent_BondOptimiser = findViewById(R.id.BondOptimiser);
        percent_CorpBondFund = findViewById(R.id.percent_CorpBondFund);
        percent_MoneyMktFund = findViewById(R.id.moneyMktFund);

        /*help_SAMF = (TextView) findViewById(R.id.help_samf);*/
        help_premiumAmt = findViewById(R.id.help_premAmt);
        help_policyTerm = findViewById(R.id.help_policyterm);
        /*help_noOfYearsElapsedSinceInception = (TextView) findViewById(R.id.help_years_elapsed_since_inception);*/

        // SFIN of Funds
        selViewSFIN = findViewById(R.id.selViewSFIN);
        linearlayoutProposerDetails = findViewById(R.id.linearlayoutProposerDetails);
        tableRow6 = findViewById(R.id.tableRow6);
        tbSFINEquityFund = findViewById(R.id.tbSFINEquityFund);
        tbSFINEquityOptFund = findViewById(R.id.tbSFINEquityOptFund);
        tbSFINGrowthFund = findViewById(R.id.tbSFINGrowthFund);
        tbSFINBalancedFund = findViewById(R.id.tbSFINBalancedFund);
        tbSFINPureFund = findViewById(R.id.tbSFINPureFund);
        tbSFINMidCapFund = findViewById(R.id.tbSFINMidCapFund);
        tbSFINBondOptimiser = findViewById(R.id.tbSFINBondOptFund);
        tbSFINCorpBondFund = findViewById(R.id.tbSFINCorpBondFund);
        tbSFINMoneyMktFund = findViewById(R.id.tbSFINMoneyMktFund);

        edt_bi_smart_insure_wealth_plus_life_assured_first_name
                .setOnEditorActionListener(this);
        edt_bi_smart_insure_wealth_plus_life_assured_middle_name
                .setOnEditorActionListener(this);
        edt_bi_smart_insure_wealth_plus_life_assured_last_name
                .setOnEditorActionListener(this);
        edt_bi_smart_insure_wealth_plus_proposer_first_name
                .setOnEditorActionListener(this);
        edt_bi_smart_insure_wealth_plus_proposer_middle_name
                .setOnEditorActionListener(this);
        edt_bi_smart_insure_wealth_plus_proposer_last_name
                .setOnEditorActionListener(this);
        rb_smart_insure_wealth_plus_trigger_fund_yes
                .setOnEditorActionListener(this);
        rb_smart_insure_wealth_plus_trigger_fund_no
                .setOnEditorActionListener(this);
        rb_smart_insure_wealth_plus_auto_allocation_yes
                .setOnEditorActionListener(this);
        rb_smart_insure_wealth_plus_auto_allocation_no
                .setOnEditorActionListener(this);


        premiumAmt.setOnEditorActionListener(this);
        /*SAMF.setOnEditorActionListener(this);*/
        /*noOfYearsElapsedSinceInception.setOnEditorActionListener(this);*/

        percent_EquityFund.setOnEditorActionListener(this);
        percent_EquityOptFund.setOnEditorActionListener(this);
        percent_GrowthFund.setOnEditorActionListener(this);
        percent_BalancedFund.setOnEditorActionListener(this);
        percent_PureFund.setOnEditorActionListener(this);
        percent_MidCapFund.setOnEditorActionListener(this);
        percent_BondOptimiser.setOnEditorActionListener(this);
        percent_CorpBondFund.setOnEditorActionListener(this);
        percent_MoneyMktFund.setOnEditorActionListener(this);

        commonMethods.setFocusable(spnr_bi_smart_insure_wealth_plus_life_assured_title);

        spnr_bi_smart_insure_wealth_plus_life_assured_title.requestFocus();

        setSpinnerAndOtherListner();

        commonMethods.setFocusable(edt_bi_smart_insure_wealth_plus_life_assured_first_name);
        edt_bi_smart_insure_wealth_plus_life_assured_first_name.requestFocus();

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
                        commonMethods.mobileNumberPatternValidation(edt_proposerdetail_basicdetail_contact_no, context);
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
                        //commonMethods.confirming_email_id(edt_proposerdetail_basicdetail_Email_id, edt_proposerdetail_basicdetail_ConfirmEmail_id);

                    }
                });

        edt_proposerdetail_basicdetail_contact_no
                .setOnEditorActionListener(this);
        edt_proposerdetail_basicdetail_Email_id.setOnEditorActionListener(this);
        edt_proposerdetail_basicdetail_ConfirmEmail_id
                .setOnEditorActionListener(this);


        /*LinearLayout tr_staff_disc = findViewById(R.id.tr_smart_insure_wealth_plus_staff_disc);

        if (userType.equalsIgnoreCase("BAP")
                || userType.equalsIgnoreCase("CAG")) {
            tr_staff_disc.setVisibility(View.GONE);
        }*/
    }

    public void email_id_validation(String email_id) {
        String EMAIL_PATTERN;
        Pattern pattern;
        Matcher matcher;

        EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email_id);
        if (!(matcher.matches())) {
            edt_proposerdetail_basicdetail_Email_id
                    .setError("Please provide the correct email address");
            validationFla1 = false;
        } else if (!isStaffDisc.isChecked()
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

    private void setSpinnerAndOtherListner() {

        rb_smart_insure_wealth_plus_trigger_fund_yes
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            is_trigger_fund = "true";
                            rb_smart_insure_wealth_plus_auto_allocation_no.setChecked(true);
                            rb_smart_insure_wealth_plus_auto_allocation_yes.setEnabled(false);
                            rb_smart_insure_wealth_plus_auto_allocation_no.setEnabled(false);
                            rb_smart_insure_wealth_plus_auto_allocation_yes.setClickable(false);
                            rb_smart_insure_wealth_plus_auto_allocation_no.setClickable(false);
                            tl_fund.setVisibility(View.GONE);
                            percent_EquityFund.setText("");
                            percent_BondOptimiser.setText("");
                            percent_BalancedFund.setText("");
                            percent_CorpBondFund.setText("");
                            percent_EquityOptFund.setText("");
                            percent_GrowthFund.setText("");
                            percent_MidCapFund.setText("");
                            percent_MoneyMktFund.setText("");
                            percent_PureFund.setText("");
                            /*setFocusable(rb_smart_insure_wealth_plus_auto_allocation_yes);
                            rb_smart_insure_wealth_plus_auto_allocation_yes.requestFocus();*/
                        }
                    }
                });

        rb_smart_insure_wealth_plus_trigger_fund_no
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            is_trigger_fund = "false";
                            rb_smart_insure_wealth_plus_auto_allocation_yes.setEnabled(true);
                            rb_smart_insure_wealth_plus_auto_allocation_no.setEnabled(true);
                            rb_smart_insure_wealth_plus_auto_allocation_yes.setClickable(true);
                            rb_smart_insure_wealth_plus_auto_allocation_no.setClickable(true);
                            if (is_trigger_fund.equalsIgnoreCase("false") && auto_allocation.equalsIgnoreCase("false")) {
                                tl_fund.setVisibility(View.VISIBLE);
                                commonMethods.setFocusable(percent_EquityFund);
                                percent_EquityFund.requestFocus();
                            } else {
                                tl_fund.setVisibility(View.GONE);
                                /*setFocusable(rb_smart_insure_wealth_plus_auto_allocation_no);
                                rb_smart_insure_wealth_plus_auto_allocation_no.requestFocus();*/
                            }
                        }
                    }
                });

        rb_smart_insure_wealth_plus_auto_allocation_yes
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            auto_allocation = "true";
                            rb_smart_insure_wealth_plus_trigger_fund_no.setChecked(true);
                            rb_smart_insure_wealth_plus_trigger_fund_yes.setEnabled(false);
                            rb_smart_insure_wealth_plus_trigger_fund_no.setEnabled(false);
                            rb_smart_insure_wealth_plus_trigger_fund_yes.setClickable(false);
                            rb_smart_insure_wealth_plus_trigger_fund_no.setClickable(false);
                            tl_fund.setVisibility(View.GONE);
                            commonMethods.setFocusable(selViewSFIN);
                            selViewSFIN.requestFocus();
                            percent_EquityFund.setText("");
                            percent_BondOptimiser.setText("");
                            percent_BalancedFund.setText("");
                            percent_CorpBondFund.setText("");
                            percent_EquityOptFund.setText("");
                            percent_GrowthFund.setText("");
                            percent_MidCapFund.setText("");
                            percent_MoneyMktFund.setText("");
                            percent_PureFund.setText("");
                        }
                    }
                });

        rb_smart_insure_wealth_plus_auto_allocation_no
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            auto_allocation = "false";
                            rb_smart_insure_wealth_plus_trigger_fund_yes.setEnabled(true);
                            rb_smart_insure_wealth_plus_trigger_fund_no.setEnabled(true);
                            rb_smart_insure_wealth_plus_trigger_fund_yes.setClickable(true);
                            rb_smart_insure_wealth_plus_trigger_fund_no.setClickable(true);
                            if (is_trigger_fund.equalsIgnoreCase("false") && auto_allocation.equalsIgnoreCase("false")) {
                                tl_fund.setVisibility(View.VISIBLE);
                                commonMethods.setFocusable(percent_EquityFund);
                                percent_EquityFund.requestFocus();
                            } else {
                                tl_fund.setVisibility(View.GONE);
                                commonMethods.setFocusable(selViewSFIN);
                                selViewSFIN.requestFocus();
                            }
                        }
                    }
                });


        selPremiumFrequencyMode
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        // TODO Auto-generated method stub
                        if (selPlan.getSelectedItem().toString()
                                .equalsIgnoreCase("Regular")) {
                            /*setFocusable(selPolicyTerm);
                            selPolicyTerm.requestFocus();*/
                        } else {
                            commonMethods.setFocusable(selPolicyTerm);
                            selPolicyTerm.requestFocus();
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });

        // plan type
        selPlan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
                                       long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        // age in years
        ageInYears.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
                                       long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        // policy term
        selPolicyTerm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
                                       long id) {
                if (selPlan.getSelectedItem().toString().equals("Regular")) {
                    if (Integer.parseInt(ageInYears.getSelectedItem()
                            .toString()) > 55) {

                        if (Integer
                                .parseInt(!textviewProposerAge.getText().toString().equals("") ? textviewProposerAge
                                        .getText().toString() : "0") < 18) {
                            commonMethods.showMessageDialog(context, "Life Assured Date Age Must Be Less Than 55");


                            btn_bi_smart_insure_wealth_plus_proposer_date
                                    .setText("Select Date");
                        } else {
                            commonMethods.showMessageDialog(context, "Proposer Date of Birth Must Be Less Than 55");

                            btn_bi_smart_insure_wealth_plus_life_assured_date
                                    .setText("Select Date");
                        }
                    } else {
                        commonMethods.clearFocusable(selPlan);
                        /*setFocusable(premiumAmt);
                         premiumAmt.requestFocus();*/

                    }
                    commonMethods.clearFocusable(selPlan);
                } else {
                    commonMethods.clearFocusable(selPlan);
                    commonMethods.setFocusable(premiumAmt);
                    premiumAmt.requestFocus();
                }

                /*updatePremiumAmtLabel();*/
                effectivePremium = getEffectivePremium(smartinsurewealthplusBean);

                if (pos == 0) {
                    txtPremiumFreqMode.setVisibility(View.VISIBLE);
                    selPremiumFrequencyMode.setVisibility(View.VISIBLE);
                } else {
                    commonMethods.clearFocusable(selPolicyTerm);
                    commonMethods.setFocusable(premiumAmt);
                    premiumAmt.requestFocus();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }

        });

        // premium amount
        premiumAmt.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                if (!premiumAmt.getText().equals("")) {
                    effectivePremium = getEffectivePremium(smartinsurewealthplusBean);
                }
            }
        });

        selViewSFIN
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        // TODO Auto-generated method stub

                        if (isChecked) {
                            tbSFINEquityFund.setVisibility(View.VISIBLE);
                            tbSFINEquityOptFund.setVisibility(View.VISIBLE);
                            tbSFINGrowthFund.setVisibility(View.VISIBLE);
                            tbSFINBalancedFund.setVisibility(View.VISIBLE);
                            tbSFINPureFund.setVisibility(View.VISIBLE);
                            tbSFINMidCapFund.setVisibility(View.VISIBLE);
                            tbSFINBondOptimiser.setVisibility(View.VISIBLE);
                            tbSFINCorpBondFund.setVisibility(View.VISIBLE);
                            tbSFINMoneyMktFund.setVisibility(View.VISIBLE);
                        } else {
                            tbSFINEquityFund.setVisibility(View.GONE);
                            tbSFINEquityOptFund.setVisibility(View.GONE);
                            tbSFINGrowthFund.setVisibility(View.GONE);
                            tbSFINBalancedFund.setVisibility(View.GONE);
                            tbSFINPureFund.setVisibility(View.GONE);
                            tbSFINMidCapFund.setVisibility(View.GONE);
                            tbSFINBondOptimiser.setVisibility(View.GONE);
                            tbSFINCorpBondFund.setVisibility(View.GONE);
                            tbSFINMoneyMktFund.setVisibility(View.GONE);
                        }

                    }
                });

        spnr_bi_smart_insure_wealth_plus_proposer_title
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long id) {
                        if (position > 0) {
                            proposer_Title = spnr_bi_smart_insure_wealth_plus_proposer_title
                                    .getSelectedItem().toString();

                            if (proposer_Title.equalsIgnoreCase("Mr.")) {
                                gender_proposer = "Male";

                            } else if (proposer_Title.equalsIgnoreCase("Ms.")
                                    || proposer_Title.equalsIgnoreCase("Mrs.")) {
                                gender_proposer = "Female";
                            }
                            spinnerProposerGender.setSelection(commonMethods.getIndex(spinnerProposerGender, gender_proposer));
                            commonMethods.setFocusable(edt_bi_smart_insure_wealth_plus_proposer_first_name);

                            edt_bi_smart_insure_wealth_plus_proposer_first_name
                                    .requestFocus();
                        } else {
                            proposer_Title = "";
                            gender_proposer = "";
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });

        spnr_bi_smart_insure_wealth_plus_life_assured_title
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long id) {
                        if (position > 0) {
                            lifeAssured_Title = spnr_bi_smart_insure_wealth_plus_life_assured_title
                                    .getSelectedItem().toString();
                            if (lifeAssured_Title.equalsIgnoreCase("Mr.")) {
                                gender = "Male";
                            } else if (lifeAssured_Title
                                    .equalsIgnoreCase("Ms.")) {
                                gender = "Female";
                            } else if (lifeAssured_Title
                                    .equalsIgnoreCase("Mrs.")) {
                                gender = "Female";
                            }

                            spnr_bi_selGender.setSelection(commonMethods.getIndex(spnr_bi_selGender, gender));

                            commonMethods.clearFocusable(spnr_bi_smart_insure_wealth_plus_life_assured_title);
                            edt_bi_smart_insure_wealth_plus_life_assured_first_name
                                    .requestFocus();
                        } else {
                            lifeAssured_Title = "";
                            gender = "";
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });

        // staff disc
        isStaffDisc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                if (isStaffDisc.isChecked()) {
                    isStaffDisc.setChecked(true);
                    commonMethods.clearFocusable(spnr_bi_smart_insure_wealth_plus_life_assured_title);
                    commonMethods.setFocusable(spnr_bi_smart_insure_wealth_plus_life_assured_title);
                    spnr_bi_smart_insure_wealth_plus_life_assured_title
                            .requestFocus();
                }

            }
        });

        // go button

        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                setResult(RESULT_OK);
                finish();

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                inputVal = new StringBuilder();
                retVal = new StringBuilder();
                bussIll = new StringBuilder();

                proposer_First_Name = edt_bi_smart_insure_wealth_plus_proposer_first_name
                        .getText().toString();
                proposer_Middle_Name = edt_bi_smart_insure_wealth_plus_proposer_middle_name
                        .getText().toString();
                proposer_Last_Name = edt_bi_smart_insure_wealth_plus_proposer_last_name
                        .getText().toString();

                name_of_proposer = proposer_Title + " "
                        + proposer_First_Name + " "
                        + proposer_Middle_Name + " " + proposer_Last_Name;

                gender_proposer = spinnerProposerGender.getSelectedItem().toString();

                lifeAssured_First_Name = edt_bi_smart_insure_wealth_plus_life_assured_first_name
                        .getText().toString();
                lifeAssured_Middle_Name = edt_bi_smart_insure_wealth_plus_life_assured_middle_name
                        .getText().toString();
                lifeAssured_Last_Name = edt_bi_smart_insure_wealth_plus_life_assured_last_name
                        .getText().toString();
                gender = spnr_bi_selGender.getSelectedItem().toString();

                name_of_life_assured = lifeAssured_Title + " "
                        + lifeAssured_First_Name + " "
                        + lifeAssured_Middle_Name + " " + lifeAssured_Last_Name;

                mobileNo = edt_proposerdetail_basicdetail_contact_no.getText()
                        .toString();
                emailId = edt_proposerdetail_basicdetail_Email_id.getText()
                        .toString();
                ConfirmEmailId = edt_proposerdetail_basicdetail_ConfirmEmail_id
                        .getText().toString();

                if (valProposerSameAsLifeAssured()
                        && valLifeAssuredProposerDetail() && valDob()
                        && valLifeAssuredMinorDetail() && valBasicDetail()
                        && val_proposer_gender() && valPremiumAmt() && valMaturityAge()
                        && valTotalAllocation()) {
                    if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {
                        proposer_Title = "";
                        proposer_First_Name = "";
                        proposer_Middle_Name = "";
                        proposer_Last_Name = "";
                        name_of_proposer = "";
                        proposer_date_of_birth = "";
                        gender_proposer = "";
                    }

                    addListenerOnSubmit();
                    getInput(smartinsurewealthplusBean);

                    if (needAnalysis_flag == 0) {

                        Intent i = new Intent(getApplicationContext(),
                                Success.class);

                        i.putExtra("ProductName",
                                "Product : SBI Life - " + planName
                                        + " (UIN:" + product_UIN + ")");

                        i.putExtra(
                                "op",
                                "Sum Assured is Rs. "
                                        + currencyFormat.format(Double
                                        .parseDouble(prsObj
                                                .parseXmlTag(retVal.toString(),
                                                        "sumAssured"))));
                        i.putExtra(
                                "op1",
                                "Fund Value @ 4% is Rs. "
                                        + currencyFormat.format(Double
                                        .parseDouble(prsObj
                                                .parseXmlTag(retVal.toString(),
                                                        "fundVal4"))));
                        i.putExtra(
                                "op2",
                                "Fund Value @ 8% is Rs. "
                                        + currencyFormat.format(Double
                                        .parseDouble(prsObj
                                                .parseXmlTag(retVal.toString(),
                                                        "fundVal8"))));
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

    private void initialiseDate() {
        Calendar calender = Calendar.getInstance();
        mYear = calender.get(Calendar.YEAR);
        mMonth = calender.get(Calendar.MONTH);
        mDay = calender.get(Calendar.DAY_OF_MONTH);

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
                        imageButtonSmartInsureWealthPlusProposerPhotograph
                                .setImageBitmap(scaled);
                    } else {
                        photoBitmap = null;
                    }
                }
            }
        }
    }

    public void Dialog() {
        final Dialog d = new Dialog(this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.BLACK));

        d.setContentView(R.layout.layout_smart_insure_wealth_plus_bi_grid);

        final CommonForAllProd obj = new CommonForAllProd();

        LinearLayout ll_auto_asset_allocation_strategy = d.findViewById(R.id.ll_auto_asset_allocation_strategy);
        LinearLayout ll_trigger_strategy = d.findViewById(R.id.ll_trigger_strategy);
        LinearLayout ll_smart_choice_strategy = d.findViewById(R.id.ll_smart_choice_strategy);

        TextView tv_proposername = d
                .findViewById(R.id.tv_proposername);
        TextView tv_proposal_number = d
                .findViewById(R.id.tv_proposal_number);
        TextView tv_bi_smart_insure_wealth_plus_age_entry = d
                .findViewById(R.id.tv_bi_smart_insure_wealth_plus_age_entry);
        TextView tv_bi_smart_insure_wealth_plus_maturity_age = d
                .findViewById(R.id.tv_bi_smart_insure_wealth_plus_maturity_age);
        TextView tv_bi_smart_insure_wealth_plus_life_assured_gender = d
                .findViewById(R.id.tv_bi_smart_insure_wealth_plus_life_assured_gender);
        TextView tv_bi_smart_insure_wealth_plus_policy_term = d
                .findViewById(R.id.tv_bi_smart_insure_wealth_plus_policy_term);
        TextView tv_bi_smart_insure_wealth_plus_perm_paying_term = d
                .findViewById(R.id.tv_bi_smart_insure_wealth_plus_perm_paying_term);
        TextView tv_bi_smart_insure_wealth_plus_smaf = d
                .findViewById(R.id.tv_bi_smart_insure_wealth_plus_smaf);
        TextView tv_bi_smart_insure_wealth_plus_annualised_premium = d
                .findViewById(R.id.tv_bi_smart_insure_wealth_plus_annualised_premium);
        TextView tv_bi_smart_insure_wealth_plus_monthly_premium = d
                .findViewById(R.id.tv_bi_smart_insure_wealth_plus_monthly_premium);
        TextView tv_bi_smart_insure_wealth_plus_sum_assured_main = d
                .findViewById(R.id.tv_bi_smart_insure_wealth_plus_sum_assured_main);
        TextView tv_bi_smart_insure_wealth_plus_mode = d
                .findViewById(R.id.tv_bi_smart_insure_wealth_plus_mode);

        TextView tv_smart_insure_wealth_plus_equity_fund_allocation = d
                .findViewById(R.id.tv_smart_insure_wealth_plus_equity_fund_allocation);
        TextView tv_smart_insure_wealth_plus_equity_optimiser_fund_allocation = d
                .findViewById(R.id.tv_smart_insure_wealth_plus_equity_optimiser_fund_allocation);
        TextView tv_smart_insure_wealth_plus_growth_fund_allocation = d
                .findViewById(R.id.tv_smart_insure_wealth_plus_growth_fund_allocation);
        TextView tv_smart_insure_wealth_plus_balanced_fund_allocation = d
                .findViewById(R.id.tv_smart_insure_wealth_plus_balanced_fund_allocation);
        TextView tv_smart_insure_wealth_plus_pure_fund_allocation = d
                .findViewById(R.id.tv_smart_insure_wealth_plus_pure_fund_allocation);
        TextView tv_smart_insure_wealth_plus_midcap_fund_allocation = d
                .findViewById(R.id.tv_smart_insure_wealth_plus_midcap_fund_allocation);
        TextView tv_smart_insure_wealth_plus_bond_optimiser_fund_allocation = d
                .findViewById(R.id.tv_smart_insure_wealth_plus_bond_optimiser_fund_allocation);
        TextView tv_smart_insure_wealth_plus_corp_bond_fund_allocation = d
                .findViewById(R.id.tv_smart_insure_wealth_plus_corp_bond_fund_allocation);
        TextView tv_smart_insure_wealth_plus_money_mkt_fund_allocation = d
                .findViewById(R.id.tv_smart_insure_wealth_plus_money_mkt_fund_allocation);

        TextView tv_smart_insure_wealth_plus_equity_fund_fmc = d
                .findViewById(R.id.tv_smart_insure_wealth_plus_equity_fund_fmc);
        TextView tv_smart_insure_wealth_plus_equity_optimiser_fund_fmc = d
                .findViewById(R.id.tv_smart_insure_wealth_plus_equity_optimiser_fund_fmc);
        TextView tv_smart_insure_wealth_plus_growth_fund_fmc = d
                .findViewById(R.id.tv_smart_insure_wealth_plus_growth_fund_fmc);
        TextView tv_smart_insure_wealth_plus_balanced_fund_fmc = d
                .findViewById(R.id.tv_smart_insure_wealth_plus_balanced_fund_fmc);
        TextView tv_smart_insure_wealth_plus_pure_fund_fmc = d
                .findViewById(R.id.tv_smart_insure_wealth_plus_pure_fund_fmc);
        TextView tv_smart_insure_wealth_plus_midcap_fund_fmc = d
                .findViewById(R.id.tv_smart_insure_wealth_plus_midcap_fund_fmc);
        TextView tv_smart_insure_wealth_plus_bond_optimiser_fund_fmc = d
                .findViewById(R.id.tv_smart_insure_wealth_plus_bond_optimiser_fund_fmc);
        TextView tv_smart_insure_wealth_plus_corp_bond_fund_fmc = d
                .findViewById(R.id.tv_smart_insure_wealth_plus_corp_bond_fund_fmc);
        TextView tv_smart_insure_wealth_plus_money_mkt_fund_fmc = d
                .findViewById(R.id.tv_smart_insure_wealth_plus_money_mkt_fund_fmc);

        /*TextView tv_smart_insure_wealth_plus_no_of_years_elapsed = (TextView) d
                .findViewById(R.id.tv_smart_insure_wealth_plus_no_of_years_elapsed);*/
        TextView tv_smart_insure_wealth_plus_reduction_yield = d
                .findViewById(R.id.tv_smart_insure_wealth_plus_reduction_yield);
        /*TextView tv_smart_insure_wealth_plus_maturity_at = (TextView) d
                .findViewById(R.id.tv_smart_insure_wealth_plus_maturity_at);
        TextView tv_smart_insure_wealth_plus_reduction_yeild2 = (TextView) d
                .findViewById(R.id.tv_smart_insure_wealth_plus_reduction_yeild2);*/
        TextView tv_bi_smart_insure_wealth_plus_name_of_proposer = (TextView) d
                .findViewById(R.id.tv_bi_smart_insure_wealth_plus_name_of_proposer);

        TextView tv_bi_smart_insure_wealth_plus_age_name_of_life_assured = (TextView) d
                .findViewById(R.id.tv_bi_smart_insure_wealth_plus_age_name_of_life_assured);
        tv_bi_smart_insure_wealth_plus_age_name_of_life_assured.setText(name_of_life_assured);

        if (edt_bi_smart_insure_wealth_plus_proposer_first_name.getText().toString().equalsIgnoreCase("")) {
            tv_bi_smart_insure_wealth_plus_name_of_proposer.setText(name_of_life_assured);
        } else {
            tv_bi_smart_insure_wealth_plus_name_of_proposer.setText(name_of_proposer);

        }
        TextView tv_bi_smart_insure_wealth_plus_age_of_proposer = (TextView) d
                .findViewById(R.id.tv_bi_smart_insure_wealth_plus_age_of_proposer);


        final EditText edt_Policyholderplace = d
                .findViewById(R.id.edt_Policyholderplace);
        TextView tv_channel_intermediary = (TextView) d
                .findViewById(R.id.tv_channel_intermediary);
        tv_channel_intermediary.setText(userType);
        //
        // edt_Policyholderplace.setText(list_channel_detail.getBranchName());

        TextView tv_smart_insure_wealth_plus_death_benefit = d
                .findViewById(R.id.tv_smart_insure_wealth_plus_death_benefit);

        TextView tv_smart_insure_wealth_plus_net_yield = d
                .findViewById(R.id.tv_smart_insure_wealth_plus_net_yield);

        TextView tv_smart_insure_wealth_plus_net_yield2 = d
                .findViewById(R.id.tv_smart_insure_wealth_plus_net_yield2);

        TextView tv_bi_is_Staff = d
                .findViewById(R.id.tv_bi_is_Staff);

        TextView tv_bi_strategy = d.findViewById(R.id.tv_bi_strategy);


        GridView gv_userinfo = d.findViewById(R.id.gv_userinfo);

        GridView gv_userinfo2 = d.findViewById(R.id.gv_userinfo2);

        GridView gv_userinfo3 = d.findViewById(R.id.gv_userinfo3);

        final CheckBox cb_statement = d
                .findViewById(R.id.cb_statement);
        cb_statement.setChecked(false);

        /* Need Analysis */
        final TextView txt_proposer_name_need_analysis = d
                .findViewById(R.id.txt_proposer_name_need_analysis);

        final TextView txt_smart_insure_wealth_plus_proposer_name = d
                .findViewById(R.id.txt_smart_insure_wealth_plus_proposer_name);

        final CheckBox cb_statement_need_analysis = d
                .findViewById(R.id.cb_statement_need_analysis);
        cb_statement_need_analysis.setChecked(true);
        TableRow tr_need_analysis = d
                .findViewById(R.id.tr_need_analysis);

        if (NeedAnalysisActivity.str_need_analysis != null) {
            if (NeedAnalysisActivity.str_need_analysis.equals("1")) {


                flg_needAnalyis = "1";
                tr_need_analysis.setVisibility(View.VISIBLE);
            } else {
                cb_statement_need_analysis.setChecked(true);
                tr_need_analysis.setVisibility(View.GONE);
            }

        }

        Button btn_proceed = d.findViewById(R.id.btn_proceed);
        btn_MarketingOfficalDate = d
                .findViewById(R.id.btn_MarketingOfficalDate);
        btn_PolicyholderDate = d
                .findViewById(R.id.btn_PolicyholderDate);

        Ibtn_signatureofMarketing = d
                .findViewById(R.id.Ibtn_signatureofMarketing);
        Ibtn_signatureofPolicyHolders = d
                .findViewById(R.id.Ibtn_signatureofPolicyHolders);
        Calendar calender = Calendar.getInstance();
        int Year = calender.get(Calendar.YEAR);
        int Month = calender.get(Calendar.MONTH);
        int Day = calender.get(Calendar.DAY_OF_MONTH);
        list_data.clear();
        list_data1.clear();
        list_data2.clear();

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
            txt_smart_insure_wealth_plus_proposer_name
                    .setText("I, "
                            + name_of_life_assured
                            + ", having received the information with respect to the above, have understood the above statement before entering into the contract.");
            txt_proposer_name_need_analysis
                    .setText("I, "
                            + name_of_life_assured
                            + ", have undergone the Need Analysis and after having reviewed the SBI Life Product options, I have opted for 'SBI LIFE - " +
                            getString(R.string.sbi_life_smart_insure_wealth_plus) + "'.");

            tv_proposername.setText(name_of_life_assured);
        } else if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("n")) {

            tv_proposername.setText(name_of_proposer);

            txt_smart_insure_wealth_plus_proposer_name
                    .setText("I, "
                            + name_of_proposer
                            + ", having received the information with respect to the above, have understood the above statement before entering into the contract.");
            txt_proposer_name_need_analysis
                    .setText("I, "
                            + name_of_proposer
                            + ", have undergone the Need Analysis and after having reviewed the SBI Life Product options, I have opted for 'SBI LIFE - " + getString(R.string.sbi_life_smart_insure_wealth_plus) + "'.");

            tv_proposername.setText(name_of_proposer);
        }
        tv_proposal_number.setText(QuatationNumber);

        if (!date2.equals("")) {
            btn_PolicyholderDate.setText(commonMethods.getDDMMYYYYDate(date2));
        } else {
            date2 = commonMethods.getMMDDYYYYDatabaseDate(getCurrentDate());
            btn_PolicyholderDate.setText(getCurrentDate());
        }

        if (!date1.equals("")) {
            btn_MarketingOfficalDate.setText(commonMethods.getDDMMYYYYDate(date1));
        } else {
            date1 = commonMethods.getDDMMYYYYDate(getCurrentDate());
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
        /* Changes Done By Machindra */
       /* String str_sign_of_proposer = "";

        if (str_sign_of_proposer != null && !str_sign_of_proposer.equals("")) {
            byte[] signByteArray = Base64.decode(str_sign_of_proposer, 0);
            Bitmap bitmap = BitmapFactory.decodeByteArray(signByteArray, 0,
                    signByteArray.length);
            Ibtn_signatureofPolicyHolders.setImageBitmap(bitmap);
        }*/

        imageButtonSmartInsureWealthPlusProposerPhotograph = d
                .findViewById(R.id.imageButtonSmartInsureWealthPlusProposerPhotograph);
        imageButtonSmartInsureWealthPlusProposerPhotograph
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Check = "Photo";
                        commonMethods.windowmessage(context, "_cust1Photo.jpg");
                    }
                });

        Ibtn_signatureofMarketing
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (cb_statement.isChecked()
                                && cb_statement_need_analysis.isChecked()) {
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
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (cb_statement.isChecked()
                                && cb_statement_need_analysis.isChecked()) {
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
            imageButtonSmartInsureWealthPlusProposerPhotograph
                    .setImageBitmap(photoBitmap);
        }

        final RadioButton radioButtonTrasactionModeManual = d
                .findViewById(R.id.radioButtonTrasactionModeManual);
        final RadioButton radioButtonTrasactionModeParivartan = d
                .findViewById(R.id.radioButtonTrasactionModeParivartan);
        final LinearLayout linearlayoutTrasactionModeParivartan = d
                .findViewById(R.id.linearlayoutTrasactionModeParivartan);

        radioButtonTrasactionModeParivartan
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

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
                            imageButtonSmartInsureWealthPlusProposerPhotograph
                                    .setImageDrawable(getResources()
                                            .getDrawable(
                                                    R.drawable.focus_imagebutton_photo));


                        }
                    }
                });

        radioButtonTrasactionModeManual
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

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

        btn_proceed.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                name_of_person = txt_smart_insure_wealth_plus_proposer_name.getText().toString();
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
                        && (((photoBitmap != null) && radioButtonTrasactionModeParivartan
                        .isChecked()) || radioButtonTrasactionModeManual
                        .isChecked())) {
                    NeedAnalysisActivity.str_need_analysis = "";
                    String productCode = getString(R.string.sbi_life_smart_insure_wealth_plus_code);

                    na_cbi_bean = new NA_CBI_bean(QuatationNumber, agentcode,
                            "", userType, "", lifeAssured_Title,
                            lifeAssured_First_Name, lifeAssured_Middle_Name,
                            lifeAssured_Last_Name, planName,
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(sumAssured.equals("") ? "0"
                                            : sumAssured))), premiumAmt.getText().toString(), emailId,
                            mobileNo, agentEmail, agentMobile, na_input,
                            na_output, smartinsurewealthplusBean.getPremFreq(),
                            Integer.parseInt(policyTerm), 0, productCode,
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

                    dbHelper.AddNeedAnalysisDashboardDetails(new ProductBIBean("",
                            QuatationNumber, planName, getCurrentDate(),
                            mobileNo, getCurrentDate(), dbHelper.GetUserCode(),
                            emailId, "", "", agentcode, "", userType, "",
                            lifeAssured_Title, lifeAssured_First_Name,
                            lifeAssured_Middle_Name, lifeAssured_Last_Name,
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(sumAssured.equals("") ? "0"
                                            : sumAssured))), premiumAmt.getText().toString(), agentEmail,
                            agentMobile, na_input, na_output,
                            smartinsurewealthplusBean.getPremFreq(), Integer
                            .parseInt(policyTerm), 0, productCode,
                            commonMethods.getDDMMYYYYDate(lifeAssured_date_of_birth),
                            commonMethods.getDDMMYYYYDate(proposer_date_of_birth), "", transactionMode, inputVal
                            .toString(), retVal.toString().replace(
                            bussIll.toString(), "")));
                    CreateSmartInsureWealthPlusBIPdf();
                    NABIObj.serviceHit(context,
                            na_cbi_bean, newFile, "",
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
                    } else if (!radioButtonTrasactionModeParivartan.isChecked()
                            && !radioButtonTrasactionModeManual.isChecked()) {
                        commonMethods.dialogWarning(context, "Please Select Transaction Mode", true);
                        commonMethods.setFocusable(linearlayoutTrasactionModeParivartan);
                        linearlayoutTrasactionModeParivartan.requestFocus();
                    } else if (photoBitmap == null) {
                        commonMethods.dialogWarning(context, "Please Capture the Photo", true);
                        commonMethods.setFocusable(imageButtonSmartInsureWealthPlusProposerPhotograph);
                        imageButtonSmartInsureWealthPlusProposerPhotograph
                                .requestFocus();
                    }
                }
            }
        });

        btn_PolicyholderDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                DIALOG_ID = 2;
                showDateDialog();
            }
        });

        btn_MarketingOfficalDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DIALOG_ID = 3;
                showDateDialog();

            }
        });

        String input = inputVal.toString();
        String output = retVal.toString();
        System.out.println("output = " + output);
        System.out.println("input = " + input);
        staffdiscount = prsObj.parseXmlTag(input, "isStaff");

        if (staffdiscount.equalsIgnoreCase("true")) {
            tv_bi_is_Staff.setText("Yes");
        } else {

            tv_bi_is_Staff.setText("No");

        }
        sumAssured = prsObj.parseXmlTag(output, "sumAssured");
        age_entry = prsObj.parseXmlTag(input, "age");
        tv_bi_smart_insure_wealth_plus_age_entry.setText(age_entry + " Years");

        if (textviewProposerAge.getText().toString().equalsIgnoreCase("")) {
            tv_bi_smart_insure_wealth_plus_age_of_proposer.setText(age_entry);
        } else {
            propserAge = prsObj.parseXmlTag(input, "proposerAge");
            tv_bi_smart_insure_wealth_plus_age_of_proposer.setText(propserAge);
        }

        maturityAge = prsObj.parseXmlTag(output, "maturityAge");
        tv_bi_smart_insure_wealth_plus_maturity_age.setText(maturityAge + " Years");

        gender = prsObj.parseXmlTag(input, "gender");
        tv_bi_smart_insure_wealth_plus_life_assured_gender.setText(gender);

        mode_of_policy = prsObj.parseXmlTag(output, "mode");

        premFreqMode = prsObj.parseXmlTag(input, "premFreqMode");
        tv_bi_smart_insure_wealth_plus_mode.setText(premFreqMode);

        policyTerm = prsObj.parseXmlTag(input, "policyTerm");
        tv_bi_smart_insure_wealth_plus_policy_term.setText(policyTerm + " Years");

        premPayTerm = prsObj.parseXmlTag(input, "policyTerm");
        tv_bi_smart_insure_wealth_plus_perm_paying_term
                .setText(premPayTerm == null ? "" : premPayTerm + " Years");

        tv_bi_smart_insure_wealth_plus_smaf.setText(String.valueOf(SAMF));

        annPrem = prsObj.parseXmlTag(output, "annPrem");
        tv_bi_smart_insure_wealth_plus_annualised_premium.setText("Rs." + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf(sumAssured.equals("") ? "0" : sumAssured))))));

        String premiumAmount = prsObj.parseXmlTag(input, "premiumAmount");
        tv_bi_smart_insure_wealth_plus_monthly_premium.setText("Rs. " + premiumAmount);

        String isTriggerFund = prsObj.parseXmlTag(input, "isTriggerFund");

        String AutoAllocation = prsObj.parseXmlTag(input, "AutoAllocation");

        if (isTriggerFund.equalsIgnoreCase("false") && AutoAllocation.equalsIgnoreCase("false")) {

            tv_bi_strategy.setText("Smart Choice Strategy");
            ll_smart_choice_strategy.setVisibility(View.VISIBLE);
            ll_trigger_strategy.setVisibility(View.GONE);
            ll_auto_asset_allocation_strategy.setVisibility(View.GONE);
        } else if (isTriggerFund.equalsIgnoreCase("false") && AutoAllocation.equalsIgnoreCase("true")) {
            tv_bi_strategy.setText("Auto Asset Allocation Strategy");
            ll_auto_asset_allocation_strategy.setVisibility(View.VISIBLE);
            ll_trigger_strategy.setVisibility(View.GONE);
            ll_smart_choice_strategy.setVisibility(View.GONE);
        } else {
            tv_bi_strategy.setText("Trigger Strategy");
            ll_auto_asset_allocation_strategy.setVisibility(View.GONE);
            ll_trigger_strategy.setVisibility(View.VISIBLE);
            ll_smart_choice_strategy.setVisibility(View.GONE);
        }

        sumAssured = prsObj.parseXmlTag(output, "sumAssured");
        tv_smart_insure_wealth_plus_death_benefit.setText("" + obj.getRound(obj.getStringWithout_E(Double.valueOf(sumAssured.equals("") ? "0" : sumAssured))));

        netYield8pa = prsObj.parseXmlTag(output, "netYield8pa");
        tv_smart_insure_wealth_plus_net_yield.setText(netYield8pa + " %");
        tv_smart_insure_wealth_plus_net_yield2.setText(netYield8pa + " %");

        /*TextView tv_bi_smart_insure_wealth_plus_premium_type = (TextView) d.findViewById(R.id.tv_bi_smart_insure_wealth_plus_premium_type);*/

        planType = prsObj.parseXmlTag(input, "plan");

        tv_bi_smart_insure_wealth_plus_sum_assured_main.setText("Rs." + commonMethods.getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf(sumAssured.equals("") ? "0" : sumAssured))))));

        perInvEquityFund = (prsObj.parseXmlTag(input, "perInvEquityFund"));
        tv_smart_insure_wealth_plus_equity_fund_allocation.setText((perInvEquityFund.equals("") ? "0" : perInvEquityFund) + " %");

        perInvEquityOptimiserFund = (prsObj.parseXmlTag(input, "perInvEquityOptimiserFund"));
        tv_smart_insure_wealth_plus_equity_optimiser_fund_allocation.setText((perInvEquityOptimiserFund.equals("") ? "0" : perInvEquityOptimiserFund) + " %");

        perInvgrowthFund = (prsObj.parseXmlTag(input, "perInvgrowthFund"));
        tv_smart_insure_wealth_plus_growth_fund_allocation.setText((perInvgrowthFund.equals("") ? "0" : perInvgrowthFund) + " %");

        perInvBalancedFund = (prsObj.parseXmlTag(input, "perInvBalancedFund"));
        tv_smart_insure_wealth_plus_balanced_fund_allocation.setText((perInvBalancedFund.equals("") ? "0" : perInvBalancedFund) + " %");

        perInvPureFund = (prsObj.parseXmlTag(input, "perInvPureFund"));
        tv_smart_insure_wealth_plus_pure_fund_allocation.setText((perInvPureFund.equals("") ? "0" : perInvPureFund) + " %");

        perInvMidCapFund = (prsObj.parseXmlTag(input, "perInvMidCapFund"));
        tv_smart_insure_wealth_plus_midcap_fund_allocation.setText((perInvMidCapFund.equals("") ? "0" : perInvMidCapFund) + " %");

        perInvBondOptimiser = (prsObj.parseXmlTag(input, "perInvBondOptimiser"));
        tv_smart_insure_wealth_plus_bond_optimiser_fund_allocation.setText((perInvBondOptimiser.equals("") ? "0" : perInvBondOptimiser) + " %");

        perInvCorpBondFund = (prsObj.parseXmlTag(input, "perInvCorpBondFund"));
        tv_smart_insure_wealth_plus_corp_bond_fund_allocation.setText((perInvCorpBondFund.equals("") ? "0" : perInvCorpBondFund) + " %");

        perInvMoneyMktFund = (prsObj.parseXmlTag(input, "perInvMoneyMktFund"));
        tv_smart_insure_wealth_plus_money_mkt_fund_allocation.setText((perInvMoneyMktFund.equals("") ? "0" : perInvMoneyMktFund) + " %");


        tv_smart_insure_wealth_plus_equity_fund_fmc.setText("1.35 %");
        tv_smart_insure_wealth_plus_equity_optimiser_fund_fmc.setText("1.35 %");
        tv_smart_insure_wealth_plus_growth_fund_fmc.setText("1.35 %");
        tv_smart_insure_wealth_plus_balanced_fund_fmc.setText("1.25 %");
        tv_smart_insure_wealth_plus_pure_fund_fmc.setText("1.35 %");
        tv_smart_insure_wealth_plus_midcap_fund_fmc.setText("1.35 %");
        tv_smart_insure_wealth_plus_bond_optimiser_fund_fmc.setText("1.15 %");
        tv_smart_insure_wealth_plus_corp_bond_fund_fmc.setText("1.15 %");
        tv_smart_insure_wealth_plus_money_mkt_fund_fmc.setText("0.25 %");

        /*noOfYrElapsed = prsObj.parseXmlTag(input, "noOfYrElapsed");
        tv_smart_insure_wealth_plus_no_of_years_elapsed.setText(noOfYrElapsed + " Years");*/

        redInYieldNoYr = prsObj.parseXmlTag(output, "redInYieldNoYr");
        tv_smart_insure_wealth_plus_reduction_yield.setText(redInYieldNoYr + " %");

        /*redInYieldMat = prsObj.parseXmlTag(output, "redInYieldMat");
        tv_smart_insure_wealth_plus_reduction_yeild2.setText(redInYieldMat + " %");*/

        TextView tv_Company_policy_surrender_dec = d.findViewById(R.id.tv_Company_policy_surrender_dec);

        if (planType.equalsIgnoreCase("Regular")) {
            Company_policy_surrender_dec = "Your SBI LIFE - " + getString(R.string.sbi_life_smart_insure_wealth_plus) + " (UIN No - "
                    + getString(R.string.sbi_life_smart_insure_wealth_plus_uin) + ") is a "
                    + planType
                    + " premium policy, for which your first year Premium is Rs "
                    + obj.getRound(obj.getStringWithout_E(Double.valueOf(annPrem)))
                    + " .Your Policy Term is "
                    + policyTerm
                    + " years"
                    + " , Premium Paying Term is "
                    + premPayTerm
                    + " years"
                    + " Premium Payment Term is same as policy term and "
                    + " Sum Assured is Rs. "
                    + obj.getRound(obj.getStringWithout_E(Double.valueOf(sumAssured
                    .equals("") ? "0" : sumAssured)));
        }

        tv_Company_policy_surrender_dec.setText(Company_policy_surrender_dec);


        for (int i = 1; i <= Integer.parseInt(policyTerm); i++) {
            String policy_year = prsObj
                    .parseXmlTag(output, "policyYr" + i + "");
            String premium = prsObj.parseXmlTag(output, "AnnPrem" + i + "");
            premium = premium == null ? "" : premium;

            String premium_allocation_charge = prsObj.parseXmlTag(output,
                    "PremAllCharge" + i + "");
            premium_allocation_charge = premium_allocation_charge == null ? "" : premium_allocation_charge;

            String annulize_premium_allocation_charge = prsObj.parseXmlTag(output,
                    "AmtAviFrInv" + i + "");
            annulize_premium_allocation_charge = annulize_premium_allocation_charge == null ? "" : annulize_premium_allocation_charge;

            String AdditionsToTheFund4Pr = prsObj.parseXmlTag(output,
                    "AdditionsToTheFund4Pr" + i + "");
            AdditionsToTheFund4Pr = AdditionsToTheFund4Pr == null ? "" : AdditionsToTheFund4Pr;

            String AdditionsToTheFund8Pr = prsObj.parseXmlTag(output,
                    "AdditionsToTheFund8Pr" + i + "");
            AdditionsToTheFund8Pr = AdditionsToTheFund8Pr == null ? "" : AdditionsToTheFund8Pr;

            String policy_administration_charge = prsObj.parseXmlTag(output,
                    "PolAdminChrg" + i + "");
            policy_administration_charge = policy_administration_charge == null ? "" : policy_administration_charge;

            String mortality_charge1 = prsObj.parseXmlTag(output, "MortChrg4Pr"
                    + i + "");
            mortality_charge1 = mortality_charge1 == null ? "" : mortality_charge1;

            String total_charge1A = prsObj.parseXmlTag(output, "OtherCharges4Pr_PartA" + i
                    + "");
            total_charge1A = total_charge1A == null ? "" : total_charge1A;

            String total_charge2A = prsObj.parseXmlTag(output, "OtherCharges8Pr_PartA" + i
                    + "");
            total_charge2A = total_charge2A == null ? "" : total_charge2A;

            String service_tax_on_mortality_charge1 = prsObj.parseXmlTag(
                    output, "TotServTax4Pr" + i + "");
            service_tax_on_mortality_charge1 = service_tax_on_mortality_charge1 == null ? "" : service_tax_on_mortality_charge1;

            String fund_before_fmc1 = prsObj.parseXmlTag(output,
                    "FundBefFMC4Pr" + i + "");
            fund_before_fmc1 = fund_before_fmc1 == null ? "" : fund_before_fmc1;

            String fund_management_charge1 = prsObj.parseXmlTag(output,
                    "FundMgmtCharg4Pr" + i + "");
            fund_management_charge1 = fund_management_charge1 == null ? "" : fund_management_charge1;

            String guranteed_addition1 = prsObj.parseXmlTag(output,
                    "GuarntAdd4Pr" + i + "");
            guranteed_addition1 = guranteed_addition1 == null ? "" : guranteed_addition1;

            String fund_value_at_end1 = prsObj.parseXmlTag(output,
                    "FundValAtEnd4Pr" + i + "");
            fund_value_at_end1 = fund_value_at_end1 == null ? "" : fund_value_at_end1;

            String return_of_moratlity_charge1 = prsObj.parseXmlTag(output,
                    "ReturnMortalityChrg4Pr" + i + "");
            return_of_moratlity_charge1 = return_of_moratlity_charge1 == null ? "" : return_of_moratlity_charge1;

            String surrender_value1 = prsObj.parseXmlTag(output, "SurrVal4Pr"
                    + i + "");
            surrender_value1 = surrender_value1 == null ? "" : surrender_value1;

            String death_benefit1 = prsObj.parseXmlTag(output, "DeathBen4Pr"
                    + i + "");
            death_benefit1 = death_benefit1 == null ? "" : death_benefit1;

            String mortality_charge2 = prsObj.parseXmlTag(output, "MortChrg8Pr"
                    + i + "");
            mortality_charge2 = mortality_charge2 == null ? "" : mortality_charge2;

            String total_charge1B = prsObj.parseXmlTag(output, "OtherCharges4Pr_PartB" + i
                    + "");
            total_charge1B = total_charge1B == null ? "" : total_charge1B;

            String total_charge2B = prsObj.parseXmlTag(output, "OtherCharges8Pr_PartB" + i
                    + "");
            total_charge2B = total_charge2B == null ? "" : total_charge2B;

            String service_tax_on_mortality_charge2 = prsObj.parseXmlTag(
                    output, "TotServTxOnCharg8Pr" + i + "");
            service_tax_on_mortality_charge2 = service_tax_on_mortality_charge2 == null ? "" : service_tax_on_mortality_charge2;

            String fund_before_fmc2 = prsObj.parseXmlTag(output,
                    "FundBefFMC8Pr" + i + "");
            fund_before_fmc2 = fund_before_fmc2 == null ? "" : fund_before_fmc2;

            String fund_management_charge2 = prsObj.parseXmlTag(output,
                    "FundMgmtCharg8Pr" + i + "");
            fund_management_charge2 = fund_management_charge2 == null ? "" : fund_management_charge2;

            String guranteed_addition2 = prsObj.parseXmlTag(output,
                    "GuarntAdd8Pr" + i + "");
            guranteed_addition2 = guranteed_addition2 == null ? "" : guranteed_addition2;

            String fund_value_at_end2 = prsObj.parseXmlTag(output,
                    "FundValAtEnd8Pr" + i + "");
            fund_value_at_end2 = fund_value_at_end2 == null ? "" : fund_value_at_end2;

            String return_of_moratlity_charge2 = prsObj.parseXmlTag(output,
                    "ReturnMortalityChrg8Pr" + i + "");
            return_of_moratlity_charge2 = return_of_moratlity_charge2 == null ? "" : return_of_moratlity_charge2;

            String surrender_value2 = prsObj.parseXmlTag(output, "SurrVal8Pr"
                    + i + "");
            surrender_value2 = surrender_value2 == null ? "" : surrender_value2;

            String death_benefit2 = prsObj.parseXmlTag(output, "DeathBen8Pr"
                    + i + "");
            death_benefit2 = death_benefit2 == null ? "" : death_benefit2;

            String commission = prsObj.parseXmlTag(output, "CommIfPay8Pr" + i
                    + "");
            commission = commission == null ? "" : commission;

         /*   list_data.add(new M_BI_SaralInsureWealthPlusAdapterCommon("","","","","","","","","","","","","","",""));
            list_data1.add(new M_BI_SaralInsureWealthPlusAdapterCommon2("","","","","","","","","","","","","","",""));
            list_data2.add(new M_BI_SaralInsureWealthPlusAdapterCommon2("","","","","","","","","","","","","","",""));
          */
            list_data.add(new M_BI_SmartInsureWealthPlusAdapterCommon(
                    policy_year,
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((premium.equals("")) ? "0" : premium))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf(mortality_charge1.equals("") ? "0" : mortality_charge1))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf(total_charge1A.equals("") ? "0" : total_charge1A))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((service_tax_on_mortality_charge1.equals("") || service_tax_on_mortality_charge1 == null) ? "0" : service_tax_on_mortality_charge1))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((fund_value_at_end1.equals("") || fund_value_at_end1 == null) ? "0" : fund_value_at_end1))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((return_of_moratlity_charge1.equals("") || return_of_moratlity_charge1 == null) ? "0" : return_of_moratlity_charge1))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((surrender_value1.equals("") || surrender_value1 == null) ? "0" : surrender_value1))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((death_benefit1.equals("") || death_benefit1 == null) ? "0" : death_benefit1))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((mortality_charge2.equals("") || mortality_charge2 == null) ? "0" : mortality_charge2))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((total_charge2A.equals("") || total_charge2A == null) ? "0" : total_charge2A))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((service_tax_on_mortality_charge2.equals("") || service_tax_on_mortality_charge2 == null) ? "0" : service_tax_on_mortality_charge2))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((fund_value_at_end2.equals("") || fund_value_at_end2 == null) ? "0" : fund_value_at_end2))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((return_of_moratlity_charge2.equals("") || return_of_moratlity_charge2 == null) ? "0" : return_of_moratlity_charge2))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((surrender_value2.equals("") || surrender_value2 == null) ? "0" : surrender_value2))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((death_benefit2.equals("") || death_benefit2 == null) ? "0" : death_benefit2))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((commission.equals("") || commission == null) ? "0" : commission))) + ""));

            list_data1.add(new M_BI_SmartInsureWealthPlusAdapterCommon2(
                    policy_year,
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((premium.equals("") || premium == null) ? "0" : premium))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((premium_allocation_charge.equals("") || premium_allocation_charge == null) ? "0" : premium_allocation_charge))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((annulize_premium_allocation_charge.equals("") || annulize_premium_allocation_charge == null) ? "0" : annulize_premium_allocation_charge))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((mortality_charge2.equals("") || mortality_charge2 == null) ? "0" : mortality_charge2))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((service_tax_on_mortality_charge2.equals("") || service_tax_on_mortality_charge2 == null) ? "0" : service_tax_on_mortality_charge2))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((policy_administration_charge.equals("") || policy_administration_charge == null) ? "0" : policy_administration_charge))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((total_charge2B.equals("") || total_charge2B == null) ? "0" : total_charge2B))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((AdditionsToTheFund8Pr.equals("") || AdditionsToTheFund8Pr == null) ? "0" : AdditionsToTheFund8Pr))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((guranteed_addition2.equals("") || guranteed_addition2 == null) ? "0" : guranteed_addition2))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((fund_before_fmc2.equals("") || fund_before_fmc2 == null) ? "0" : fund_before_fmc2))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((fund_management_charge2.equals("") || fund_management_charge2 == null) ? "0" : fund_management_charge2))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((fund_value_at_end2.equals("") || fund_value_at_end2 == null) ? "0" : fund_value_at_end2))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((return_of_moratlity_charge2.equals("") || return_of_moratlity_charge2 == null) ? "0" : return_of_moratlity_charge2))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((surrender_value2.equals("") || surrender_value2 == null) ? "0" : surrender_value2))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((death_benefit2.equals("") || death_benefit2 == null) ? "0" : death_benefit2))) + ""));

            list_data2.add(new M_BI_SmartInsureWealthPlusAdapterCommon2(
                    policy_year,
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((premium.equals("") || premium == null) ? "0" : premium))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((premium_allocation_charge.equals("") || premium_allocation_charge == null) ? "0" : premium_allocation_charge))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((annulize_premium_allocation_charge.equals("") || annulize_premium_allocation_charge == null) ? "0" : annulize_premium_allocation_charge))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((mortality_charge1.equals("") || mortality_charge1 == null) ? "0" : mortality_charge1))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((service_tax_on_mortality_charge1.equals("") || service_tax_on_mortality_charge1 == null) ? "0" : service_tax_on_mortality_charge1))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((policy_administration_charge.equals("") || policy_administration_charge == null) ? "0" : policy_administration_charge))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((total_charge1B.equals("") || total_charge1B == null) ? "0" : total_charge1B))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((AdditionsToTheFund4Pr.equals("") || AdditionsToTheFund4Pr == null) ? "0" : AdditionsToTheFund4Pr))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((guranteed_addition1.equals("") || guranteed_addition1 == null) ? "0" : guranteed_addition1))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((fund_before_fmc1.equals("") || fund_before_fmc1 == null) ? "0" : fund_before_fmc1))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((fund_management_charge1.equals("") || fund_management_charge1 == null) ? "0" : fund_management_charge1))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((fund_value_at_end1.equals("") || fund_value_at_end1 == null) ? "0" : fund_value_at_end1))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((return_of_moratlity_charge1.equals("") || return_of_moratlity_charge1 == null) ? "0" : return_of_moratlity_charge1))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((surrender_value1.equals("") || surrender_value1 == null) ? "0" : surrender_value1))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((death_benefit1.equals("") || death_benefit1 == null) ? "0" : death_benefit1))) + ""));
        }


        Adapter_BI_SmartInsureWealthPlusGridCommon adapter = new Adapter_BI_SmartInsureWealthPlusGridCommon(
                BI_SmartInsureWealthPlusActivity.this, list_data);
        gv_userinfo.setAdapter(adapter);

        Adapter_BI_SmartInsureWealthPlusGridCommon2 adapter1 = new Adapter_BI_SmartInsureWealthPlusGridCommon2(
                BI_SmartInsureWealthPlusActivity.this, list_data1);
        gv_userinfo2.setAdapter(adapter1);

        Adapter_BI_SmartInsureWealthPlusGridCommon2 adapter2 = new Adapter_BI_SmartInsureWealthPlusGridCommon2(
                BI_SmartInsureWealthPlusActivity.this, list_data2);
        gv_userinfo3.setAdapter(adapter2);


        GridHeight gh = new GridHeight();
        gh.getheight(gv_userinfo, policyTerm);
        gh.getheight(gv_userinfo2, policyTerm);
        gh.getheight(gv_userinfo3, policyTerm);

        d.show();

    }

    public void onClickDob(View v) {
        initialiseDateParameter(proposer_date_of_birth, 35);
        DIALOG_ID = 4;
        showDateDialog();
    }

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

        String final_age = age + " yrs";

        if (final_age.contains("-")) {
            commonMethods.dialogWarning(context, "Please fill Valid Birth Date", false);
        } else {
            switch (id) {

                case 2:
                    btn_PolicyholderDate.setText(date);
                    break;
                case 3:
                    btn_MarketingOfficalDate.setText(date);
                    break;

                case 4:
                    strProposerAge = final_age;
                    final_age = Integer.toString(age);
                    if (final_age.contains("-")) {
                        commonMethods.showMessageDialog(context, "Please fill Valid Birth Date");
                    } else {
                        if (18 <= age && age <= 100) {

                            btn_bi_smart_insure_wealth_plus_proposer_date.setText(date);
                            textviewProposerAge
                                    .setText(final_age);
                            proposer_date_of_birth = commonMethods.getMMDDYYYYDatabaseDate(date + "");

                            commonMethods.clearFocusable(btn_bi_smart_insure_wealth_plus_proposer_date);

                            /*
                             * setFocusable(selPlan); selPlan.requestFocus();
                             */

                            commonMethods.setFocusable(edt_proposerdetail_basicdetail_contact_no);
                            edt_proposerdetail_basicdetail_contact_no
                                    .requestFocus();

                            // ageInYears
                            // .setSelection(getIndex(ageInYears, final_age));
                            // updatePolicyTermLabel();
                            // updateSAMFlabel();
                        } else {
                            commonMethods.showMessageDialog(context, "Minimum Age should be 18 yrs For Proposer");
                            btn_bi_smart_insure_wealth_plus_proposer_date
                                    .setText("Select Date");
                            textviewProposerAge.setText("");
                            proposer_date_of_birth = "";
                            commonMethods.clearFocusable(btn_bi_smart_insure_wealth_plus_proposer_date);
                            commonMethods.setFocusable(btn_bi_smart_insure_wealth_plus_proposer_date);
                            btn_bi_smart_insure_wealth_plus_proposer_date
                                    .requestFocus();

                        }
                    }
                    break;

                case 5:
                    lifeAssuredAge = final_age;
                    final_age = Integer.toString(age);
                    if (final_age.contains("-")) {
                        commonMethods.showMessageDialog(context, "Please fill Valid Birth Date");
                    } else {

                        if (selPlan.getSelectedItem().toString().equals("Regular")) {
                            if (0 <= age && age <= 55) {

                                btn_bi_smart_insure_wealth_plus_life_assured_date
                                        .setText(date);

                                ageInYears.setSelection(
                                        commonMethods.getIndex(ageInYears, final_age), false);
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
                                        commonMethods.showMessageDialog(context, "Minimum Age should be 0 (30 days)");
                                        btn_bi_smart_insure_wealth_plus_life_assured_date
                                                .setText("Select Date");

                                        lifeAssured_date_of_birth = "";
                                        commonMethods.clearFocusable(btn_bi_smart_insure_wealth_plus_life_assured_date);
                                        commonMethods.setFocusable(btn_bi_smart_insure_wealth_plus_life_assured_date);
                                        btn_bi_smart_insure_wealth_plus_life_assured_date
                                                .requestFocus();
                                    } else {

                                        linearlayoutProposerDetails.setVisibility(View.VISIBLE);
                                        proposer_Is_Same_As_Life_Assured = "n";

                                        commonMethods.clearFocusable(btn_bi_smart_insure_wealth_plus_life_assured_date);
                                        commonMethods.setFocusable(edt_bi_smart_insure_wealth_plus_proposer_first_name);
                                        edt_bi_smart_insure_wealth_plus_proposer_first_name
                                                .requestFocus();
                                    }
                                } else {
                                    linearlayoutProposerDetails.setVisibility(View.GONE);
                                    proposer_Is_Same_As_Life_Assured = "y";

                                    commonMethods.clearFocusable(btn_bi_smart_insure_wealth_plus_life_assured_date);
                                    commonMethods.setFocusable(edt_proposerdetail_basicdetail_contact_no);
                                    edt_proposerdetail_basicdetail_contact_no
                                            .requestFocus();
                                }

                                ageInYears.setSelection(
                                        commonMethods.getIndex(ageInYears, final_age), false);
                                lifeAssured_date_of_birth = commonMethods.getMMDDYYYYDatabaseDate(date + "");

                            } else {
                                commonMethods.showMessageDialog(context, "Minimum Age should be 0 (30 days) and Maximum Age should be 55 yrs For LifeAssured Plan Type Regular");
                                btn_bi_smart_insure_wealth_plus_life_assured_date
                                        .setText("Select Date");
                                lifeAssured_date_of_birth = "";
                                commonMethods.clearFocusable(btn_bi_smart_insure_wealth_plus_life_assured_date);
                                commonMethods.setFocusable(btn_bi_smart_insure_wealth_plus_life_assured_date);
                                btn_bi_smart_insure_wealth_plus_life_assured_date
                                        .requestFocus();
                            }
                        }
                    }

                    break;

                default:
                    break;
            }
        }
    }

    public void onClickLADob(View v) {
        initialiseDateParameter(lifeAssured_date_of_birth, 35);
        DIALOG_ID = 5;
        showDateDialog();
    }

    public void onClickProposerDob(View v) {
        initialiseDateParameter(proposer_date_of_birth, 35);
        DIALOG_ID = 4;
        showDateDialog();
    }

    public void CreateSmartInsureWealthPlusBIPdf() {

        // String QuatationNumber = ProductHomePageActivity.quotation_Number;
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

            Font small_bold1 = new Font(Font.FontFamily.TIMES_ROMAN, 8,
                    Font.BOLD);

            Font small_bold2 = new Font(Font.FontFamily.TIMES_ROMAN, 4,
                    Font.BOLD);
            Font small_normal = new Font(Font.FontFamily.TIMES_ROMAN, 8,
                    Font.NORMAL);

            // File mypath = new File(folder, PropserNumber +
            // "Proposalno_p02.pdf");

            mypath = mStorageUtils.createFileToAppSpecificDir(context, QuatationNumber + "BI.pdf");
            //needAnalysispath = new File(folder, "NA.pdf");
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

            // For the BI Smart Power Insurance Table Header(Grey One)
            Paragraph Para_Header = new Paragraph();
            Para_Header
                    .add(new Paragraph(
                            "IN THIS POLICY, THE INVESTMENT RISK IN INVESTMENT PORTFOLIO IS BORNE BY THE POLICYHOLDER.",
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
                    small_bold);
            para_address.setAlignment(Element.ALIGN_CENTER);
            Paragraph para_address1 = new Paragraph(
                    "Corporate Office: 'Natraj', M.V.Road and Western Express Highway Junction, Andheri (East), Mumbai - 400069",
                    small_bold);
            para_address1.setAlignment(Element.ALIGN_CENTER);
            Paragraph para_address2 = new Paragraph(
                    "IRDAI Registration No. 111 | Website: www.sbilife.co.in | Email: info@sbilife.co.in | CIN: L99999MH2000PLC129113",
                    small_bold);
            para_address2.setAlignment(Element.ALIGN_CENTER);
            Paragraph para_address3 = new Paragraph(
                    "Toll Free: 1800 267 9090 (Between 9.00 am & 9.00 pm)",
                    small_bold);
            para_address3.setAlignment(Element.ALIGN_CENTER);
            Paragraph para_address4 = new Paragraph(
                    "Customised Benefit Illustration (CBI)",
                    small_bold);
            para_address4.setAlignment(Element.ALIGN_CENTER);
            Paragraph para_address5 = new Paragraph(
                    "SBI Life - Smart InsureWealth Plus (111L125V02)",
                    small_bold);
            para_address5.setAlignment(Element.ALIGN_CENTER);
            Paragraph para_address6 = new Paragraph(
                    "An Individual, Unit-linked, Non-Participating, Life Insurance Product",
                    small_bold);
            para_address6.setAlignment(Element.ALIGN_CENTER);

            document.add(para_address);
            document.add(para_address1);
            document.add(para_address2);
            document.add(para_address3);
            document.add(para_address4);
            document.add(para_address5);
            document.add(para_address6);
            document.add(para_img_logo_after_space_1);
            document.add(headertable);
            document.add(para_img_logo_after_space_1);
            // document.add(para_img_logo_after_space_1);
            //
            // Paragraph para_channalDetails = new Paragraph("Channel Details",
            // small_bold);
            // document.add(para_channalDetails);
            // document.add(para_img_logo_after_space_1);
            // // Get Channel Detail
            // M_ChannelDetails channelDetail = db
            // .getChannelDetail(LoginActivity.confirmemailid);
            //
            // String code = channelDetail.getCode();
            // code = code.substring(2, code.length()).trim();
            // String name = channelDetail.getName();
            // PdfPTable table_code_name = new PdfPTable(4);
            // table_code_name.setWidthPercentage(100);
            //
            // PdfPCell table_code_name_cell_1 = new PdfPCell(new Paragraph(
            // "IA Code", small_normal));
            // PdfPCell table_code_name_cell_2 = new PdfPCell(new
            // Paragraph(code,
            // small_bold));
            // table_code_name_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);
            // PdfPCell table_code_name_cell_3 = new PdfPCell(new Paragraph(
            // "IA Name", small_normal));
            // PdfPCell table_code_name_cell_4 = new PdfPCell(new
            // Paragraph(name,
            // small_bold));
            //
            // table_code_name_cell_4.setVerticalAlignment(Element.ALIGN_CENTER);
            //
            // table_code_name_cell_1.setPadding(5);
            // table_code_name_cell_2.setPadding(5);
            // table_code_name_cell_3.setPadding(5);
            // table_code_name_cell_4.setPadding(5);
            //
            // table_code_name.addCell(table_code_name_cell_1);
            // table_code_name.addCell(table_code_name_cell_2);
            // table_code_name.addCell(table_code_name_cell_3);
            // table_code_name.addCell(table_code_name_cell_4);
            //
            // document.add(table_code_name);

            document.add(para_img_logo_after_space_1);
            // Proposer Name
            if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {
                name_of_proposer = name_of_life_assured;
            }
            PdfPTable table_proposer_name = new PdfPTable(4);
            // float[] columnWidths_table_proposer_name = { 2f, 2f, 1f, 2f };
            // table_proposer_name.setWidths(columnWidths_table_proposer_name);
            table_proposer_name.setWidthPercentage(100);

            PdfPCell ProposalNumber_cell_1 = new PdfPCell(new Paragraph(
                    "Quotation Number", small_normal));
            PdfPCell ProposalNumber_cell_2 = new PdfPCell(new Paragraph(
                    QuatationNumber, small_bold1));
            ProposalNumber_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell NameofProposal_cell_3 = new PdfPCell(new Paragraph(
                    "Proposer Name ", small_normal));
            PdfPCell NameofProposal_cell_4 = new PdfPCell(new Paragraph(
                    name_of_proposer, small_bold1));
            NameofProposal_cell_4.setHorizontalAlignment(Element.ALIGN_CENTER);

            NameofProposal_cell_4.setVerticalAlignment(Element.ALIGN_CENTER);

            PdfPCell ProposalNumber_cell_5 = new PdfPCell(new Paragraph(
                    "Channel / Intermediary :", small_normal));
            PdfPCell ProposalNumber_cell_6 = new PdfPCell(new Paragraph(
                    userType, small_bold1));
            ProposalNumber_cell_6.setHorizontalAlignment(Element.ALIGN_CENTER);

            ProposalNumber_cell_1.setPadding(5);
            ProposalNumber_cell_2.setPadding(5);
            NameofProposal_cell_3.setPadding(5);
            NameofProposal_cell_4.setPadding(5);
            ProposalNumber_cell_5.setPadding(5);
            ProposalNumber_cell_6.setPadding(5);

            // table_proposer_name.addCell(ProposalNumber_cell_1);
            // table_proposer_name.addCell(ProposalNumber_cell_2);
            table_proposer_name.addCell(NameofProposal_cell_3);
            table_proposer_name.addCell(NameofProposal_cell_4);
            table_proposer_name.addCell(ProposalNumber_cell_5);
            table_proposer_name.addCell(ProposalNumber_cell_6);
            document.add(table_proposer_name);
            document.add(para_img_logo_after_space_1);

            PdfPTable BI_Pdftable1 = new PdfPTable(1);
            BI_Pdftable1.setWidthPercentage(100);
            // CR_table6.setWidths(columnWidths_2);
            PdfPCell BI_Pdftable1_cell1 = new PdfPCell(
                    new Paragraph(
                            "Insurance Regulatory and Development authority of India (IRDAI) requires all life insurance companies operating in India to provide official illustrations to their customers. The illustrations are based on the investment rates of return set by the Insurance Regulatory and Development Authority of India (Unit Linked Insurance Products) Regulations, 2019 and is not intended to reflect the actual investment returns achieved or which may be achieved in future by SBI Life Insurance Company Limited.",
                            small_normal));

            BI_Pdftable1_cell1.setPadding(5);

            BI_Pdftable1.addCell(BI_Pdftable1_cell1);
            document.add(BI_Pdftable1);

            PdfPTable BI_Pdftable2 = new PdfPTable(1);
            BI_Pdftable2.setWidthPercentage(100);
            // CR_table6.setWidths(columnWidths_2);
            PdfPCell BI_Pdftable2_cell1 = new PdfPCell(
                    new Paragraph(
                            "The main objective of the illustration is that the client is able to appreciate the features of the product and the flow of benefits in different circumstances with some level of quantification. For further information on the product, its benefits and applicable charges please refer to the sales brochure and/or policy document. Further information will also be available on request.",
                            small_normal));

            BI_Pdftable2_cell1.setPadding(5);

            BI_Pdftable2.addCell(BI_Pdftable2_cell1);
            document.add(BI_Pdftable2);

            PdfPTable BI_Pdftable3 = new PdfPTable(1);
            BI_Pdftable3.setWidthPercentage(100);
            PdfPCell BI_Pdftable3_cell1 = new PdfPCell(
                    new Paragraph(
                            "Some benefits are guaranteed and some benefits are variable with returns based on the future fund performance of SBI Life Insurance Company Limited. If your policy offers guaranteed returns then the same will be clearly marked as “guaranteed” in the illustration table. If your policy offers variable returns then the illustration will show two different rates of assumed future investment returns. These assumed rates of return are not guaranteed and they are not the upper or lower limits of what you might get back, as the value of your policy is dependent on a number of factors including future fund investment performance.",
                            small_normal));

            BI_Pdftable3_cell1.setPadding(5);

            BI_Pdftable3.addCell(BI_Pdftable3_cell1);
            document.add(BI_Pdftable3);

            PdfPTable BI_Pdftable4 = new PdfPTable(1);
            BI_Pdftable4.setWidthPercentage(100);
            PdfPCell BI_Pdftable4_cell1 = new PdfPCell(
                    new Paragraph(
                            "Some benefits are guaranteed and some benefits are variable with returns based on the future fund performance of SBI Life Insurance Company Limited. If your policy offers guaranteed returns then the same will be clearly marked as “guaranteed” in the illustration. If your policy offers variable returns then the illustration will be based on two different rates of assumed future investment returns. These assumed rates of return are not guaranteed and they are not the upper or lower limits of what you might get back, as the value of your policy is dependent on a number of factors including future fund investment performance.",
                            small_normal));

            BI_Pdftable4_cell1.setPadding(5);

            BI_Pdftable4.addCell(BI_Pdftable4_cell1);
            //document.add(BI_Pdftable4);

            PdfPTable BI_Pdftable6 = new PdfPTable(1);
            BI_Pdftable6.setWidthPercentage(100);
            PdfPCell BI_Pdftable6_cell6 = new PdfPCell(
                    new Paragraph(
                            "1. Kindly note that this is an illustration only and does not in any way create any rights and/or obligations. The actual experience on the contract may be different from what is illustrated. The non-guaranteed low and high rate mentioned  relate to assumed investment returns at different rates and may vary depending upon market conditions. For more details on risk factors, terms and conditions please read sales brochure carefully before concluding purchase.",

                            small_normal));

            BI_Pdftable6_cell6.setPadding(5);

            BI_Pdftable6.addCell(BI_Pdftable6_cell6);
            //document.add(BI_Pdftable6);

            PdfPTable BI_Pdftable7 = new PdfPTable(1);
            BI_Pdftable7.setWidthPercentage(100);
            PdfPCell BI_Pdftable7_cell1 = new PdfPCell(
                    new Paragraph(
                            "2. The unit values may go up as well as down and past performance is no indication of future performance on the part of SBI Life Insurance Co. Ltd. We would request you to appreciate the associated risk under this plan vis-à-vis the likely future returns before taking your investment decision.",

                            small_normal));

            BI_Pdftable7_cell1.setPadding(5);

            BI_Pdftable7.addCell(BI_Pdftable7_cell1);
            //document.add(BI_Pdftable7);

            PdfPTable BI_Pdftable9 = new PdfPTable(1);
            BI_Pdftable9.setWidthPercentage(100);
            PdfPCell BI_Pdftable9_cell1 = new PdfPCell(
                    new Paragraph(
                            "3. Please read this benefit illustration in conjunction with Sales Brochure and the Policy Document to understand all Terms, Conditions &amp; Exclusions carefully.",

                            small_normal));

            BI_Pdftable9_cell1.setPadding(5);

            BI_Pdftable9.addCell(BI_Pdftable9_cell1);
            // document.add(BI_Pdftable9);

            PdfPTable BI_Pdftable15 = new PdfPTable(1);
            BI_Pdftable15.setWidthPercentage(100);
            PdfPCell BI_Pdftable15_cell1 = new PdfPCell(
                    new Paragraph(
                            "4. This illustration has been prepared in compliance with the IRDA (Linked Products) Regulations, 2013.",
                            small_normal));

            BI_Pdftable15_cell1.setPadding(5);

            BI_Pdftable15.addCell(BI_Pdftable15_cell1);
            // document.add(BI_Pdftable15);

            document.add(para_img_logo_after_space_1);

            String isStaff = "";
            if (isStaffDisc.isChecked()) {
                isStaff = "yes";
            } else {

                isStaff = "No";
            }
            PdfPTable table_staff_disccount = new PdfPTable(2);
            table_staff_disccount.setWidthPercentage(100);

            PdfPCell cell_staff_disccount1 = new PdfPCell(new Paragraph(
                    "Staff Discount", small_normal));
            cell_staff_disccount1.setPadding(5);
            PdfPCell cell_staff_disccount2 = new PdfPCell(new Paragraph(
                    isStaff, small_bold));
            cell_staff_disccount2.setPadding(5);
            cell_staff_disccount2.setHorizontalAlignment(Element.ALIGN_CENTER);

            table_staff_disccount.addCell(cell_staff_disccount1);
            table_staff_disccount.addCell(cell_staff_disccount2);

            // document.add(table_staff_disccount);

            // document.add(para_img_logo_after_space_1);

            PdfPTable BI_PdftablePlanDetails = new PdfPTable(1);
            BI_PdftablePlanDetails.setWidthPercentage(100);
            PdfPCell BI_PdftablePlanDetails_cell = new PdfPCell(new Paragraph(
                    "Proposer, Life Assured and Plan Details", small_bold));

            BI_PdftablePlanDetails_cell
                    .setBackgroundColor(BaseColor.LIGHT_GRAY);

            BI_PdftablePlanDetails_cell
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftablePlanDetails_cell.setPadding(5);

            BI_PdftablePlanDetails.addCell(BI_PdftablePlanDetails_cell);
            document.add(BI_PdftablePlanDetails);

            PdfPTable table_lifeAssuredDetails = new PdfPTable(4);
            table_lifeAssuredDetails.setWidthPercentage(100);

            PdfPCell name_of_proposer1 = new PdfPCell(new Paragraph(
                    "Name of Proposer", small_normal));
            name_of_proposer1.setPadding(5);

            PdfPCell name_of_proposer2;
            if (edt_bi_smart_insure_wealth_plus_proposer_first_name.getText().toString().equalsIgnoreCase("")) {
                name_of_proposer2 = new PdfPCell(new Paragraph(name_of_life_assured, small_bold));
                name_of_proposer2.setPadding(5);
                name_of_proposer2.setHorizontalAlignment(Element.ALIGN_CENTER);

            } else {
                name_of_proposer2 = new PdfPCell(new Paragraph(name_of_proposer, small_bold));
                name_of_proposer2.setPadding(5);
                name_of_proposer2.setHorizontalAlignment(Element.ALIGN_CENTER);
            }
            PdfPCell Age_of_prop1 = new PdfPCell(new Paragraph(
                    "Age of Proposer", small_normal));
            Age_of_prop1.setPadding(5);
            //  age_entry = prsObj.parseXmlTag(input, "age");

            //propserAge = prsObj.parseXmlTag(input, "proposerAge");

            PdfPCell Age_of_prop2;
            if (textviewProposerAge.getText().toString().equalsIgnoreCase("")) {
                Age_of_prop2 = new PdfPCell(new Paragraph(age_entry, small_bold));
                Age_of_prop2.setPadding(5);
                Age_of_prop2.setHorizontalAlignment(Element.ALIGN_CENTER);

            } else {
                Age_of_prop2 = new PdfPCell(new Paragraph(propserAge, small_bold));
                Age_of_prop2.setPadding(5);
                Age_of_prop2.setHorizontalAlignment(Element.ALIGN_CENTER);
            }

            PdfPCell name_of_life_assured1 = new PdfPCell(new Paragraph(
                    "Name of the Life Assured", small_normal));
            name_of_life_assured1.setPadding(5);

            PdfPCell name_of_life_assured2 = new PdfPCell(new Paragraph(name_of_life_assured, small_bold));
            name_of_life_assured2.setPadding(5);
            name_of_life_assured2.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_lifeAssuredAge1 = new PdfPCell(new Paragraph(
                    "Age of the Life Assured", small_normal));
            cell_lifeAssuredAge1.setPadding(5);
            PdfPCell cell_lifeAssuredAge2 = new PdfPCell(new Paragraph("  "
                    + age_entry + " Years", small_bold));
            cell_lifeAssuredAge2.setPadding(5);
            cell_lifeAssuredAge2.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_lifeAssuredAmaturityAge1 = new PdfPCell(
                    new Paragraph("Maturity Age" + "  ", small_normal));
            cell_lifeAssuredAmaturityAge1.setPadding(5);
            PdfPCell cell_lifeAssuredAmaturityAge2 = new PdfPCell(
                    new Paragraph(maturityAge + " Years", small_bold));
            cell_lifeAssuredAmaturityAge2.setPadding(5);
            cell_lifeAssuredAmaturityAge2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            table_lifeAssuredDetails.addCell(name_of_proposer1);
            table_lifeAssuredDetails.addCell(name_of_proposer2);
            table_lifeAssuredDetails.addCell(Age_of_prop1);
            table_lifeAssuredDetails.addCell(Age_of_prop2);
            table_lifeAssuredDetails.addCell(name_of_life_assured1);
            table_lifeAssuredDetails.addCell(name_of_life_assured2);
            table_lifeAssuredDetails.addCell(cell_lifeAssuredAge1);
            table_lifeAssuredDetails.addCell(cell_lifeAssuredAge2);

            //table_lifeAssuredDetails.addCell(cell_lifeAssuredAmaturityAge1);
            // table_lifeAssuredDetails.addCell(cell_lifeAssuredAmaturityAge2);

            document.add(table_lifeAssuredDetails);

            PdfPTable table_lifeAssuredDetails_gender_policy_term = new PdfPTable(
                    4);
            table_lifeAssuredDetails_gender_policy_term.setWidthPercentage(100);

            PdfPCell cell_lifeAssured_gender1 = new PdfPCell(new Paragraph(
                    "Life Assured Gender", small_normal));
            cell_lifeAssured_gender1.setPadding(5);
            PdfPCell cell_lifeAssured_gender2 = new PdfPCell(new Paragraph("  "
                    + gender, small_bold));
            cell_lifeAssured_gender2.setPadding(5);
            cell_lifeAssured_gender2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_termsofPolicy1 = new PdfPCell(new Paragraph(
                    "Policy Term " + "  ", small_normal));
            cell_termsofPolicy1.setPadding(5);
            PdfPCell cell_termsofPolicy2 = new PdfPCell(new Paragraph("  "
                    + policyTerm + " Years", small_bold));
            cell_termsofPolicy2.setPadding(5);
            cell_termsofPolicy2.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell premium_payment_term1 = new PdfPCell(new Paragraph(
                    "Premium Payment Term ", small_normal));
            premium_payment_term1.setPadding(5);
            PdfPCell premium_payment_term2 = new PdfPCell(new Paragraph(" Same as Policy Term ", small_bold));
            premium_payment_term2.setPadding(5);
            premium_payment_term2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_monthly_prem1 = new PdfPCell(new Paragraph("Amount of Installment Premium ", small_normal));
            cell_monthly_prem1.setPadding(5);
            PdfPCell cell_monthly_prem2 = new PdfPCell(new Paragraph("" + premiumAmt.getText().toString(), small_bold));
            cell_monthly_prem2.setPadding(5);
            cell_monthly_prem2.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell mode1 = new PdfPCell(new Paragraph("Mode / Frequency of Premium Payment ", small_normal));
            mode1.setPadding(5);
            PdfPCell mode2 = new PdfPCell(new Paragraph("Monthly", small_bold));
            mode2.setPadding(5);
            mode2.setHorizontalAlignment(Element.ALIGN_CENTER);
            //  table_lifeAssuredDetails_gender_policy_term
            //         .addCell(cell_lifeAssured_gender1);
            // table_lifeAssuredDetails_gender_policy_term
            //        .addCell(cell_lifeAssured_gender2);

            table_lifeAssuredDetails_gender_policy_term
                    .addCell(cell_termsofPolicy1);
            table_lifeAssuredDetails_gender_policy_term
                    .addCell(cell_termsofPolicy2);

            table_lifeAssuredDetails_gender_policy_term
                    .addCell(premium_payment_term1);
            table_lifeAssuredDetails_gender_policy_term
                    .addCell(premium_payment_term2);

            table_lifeAssuredDetails_gender_policy_term
                    .addCell(cell_monthly_prem1);
            table_lifeAssuredDetails_gender_policy_term
                    .addCell(cell_monthly_prem2);


            table_lifeAssuredDetails_gender_policy_term
                    .addCell(mode1);
            table_lifeAssuredDetails_gender_policy_term
                    .addCell(mode2);

            document.add(table_lifeAssuredDetails_gender_policy_term);

            String PlanType = "";
            if (plan.equalsIgnoreCase("Regular")
                    || plan.equalsIgnoreCase("Limited")) {

                PlanType = "Annualised Premium";

            } else if (plan.equalsIgnoreCase("Single")) {
                PlanType = "Single Premium";
            }

            CommonForAllProd obj = new CommonForAllProd();

            PdfPTable table_lifeAssuredDetails_premium_sumAssured = new PdfPTable(2);
            table_lifeAssuredDetails_premium_sumAssured.setWidthPercentage(100);

            PdfPCell cell_regularPremium1 = new PdfPCell(new Paragraph("Sum Assured", small_normal));
            cell_regularPremium1.setPadding(5);
            PdfPCell cell_regularPremium2 = new PdfPCell(new Paragraph(
                    "Rs. "
                            + obj.getRound(obj.getStringWithout_E(Double
                            .valueOf((sumAssured.equals("") || sumAssured == null) ? "0"
                                    : sumAssured))), small_bold));

            cell_regularPremium2.setPadding(5);
            cell_regularPremium2.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_sumAssured1 = new PdfPCell(new Paragraph("Sum assured", small_normal));
            cell_sumAssured1.setPadding(5);
            PdfPCell cell_sumAssured2 = new PdfPCell(new Paragraph(
                    "Rs. "
                            + obj.getRound(obj.getStringWithout_E(Double
                            .valueOf(sumAssured.equals("") ? "0"
                                    : sumAssured))), small_bold));
            cell_sumAssured2.setPadding(5);
            cell_sumAssured2.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell rate = new PdfPCell(new Paragraph(
                    "Rate of Applicable Taxes", small_bold));

            PdfPCell rate2 = new PdfPCell(new Paragraph(
                    "18%", small_bold));


            table_lifeAssuredDetails_premium_sumAssured
                    .addCell(cell_regularPremium1);
            table_lifeAssuredDetails_premium_sumAssured
                    .addCell(cell_regularPremium2);

            table_lifeAssuredDetails_premium_sumAssured
                    .addCell(rate);
            table_lifeAssuredDetails_premium_sumAssured
                    .addCell(rate2);

            //table_lifeAssuredDetails_premium_sumAssured
            //         .addCell(cell_sumAssured1);
            // table_lifeAssuredDetails_premium_sumAssured
            //        .addCell(cell_sumAssured2);

            document.add(table_lifeAssuredDetails_premium_sumAssured);

            PdfPTable tablePolicyDetails2 = new PdfPTable(4);
            tablePolicyDetails2.setWidthPercentage(100);

            PdfPCell cell_mode1 = new PdfPCell(new Paragraph("Mode",
                    small_normal));
            cell_mode1.setPadding(5);
            PdfPCell cell_mode2 = new PdfPCell(new Paragraph("  "
                    + mode_of_policy, small_bold));
            cell_mode2.setPadding(5);
            cell_mode2.setHorizontalAlignment(Element.ALIGN_CENTER);

            String payingTerm = "";
            if (planType.equalsIgnoreCase("single")) {
                payingTerm = "1 Year";
            } else if (planType.equalsIgnoreCase("Regular")) {
                payingTerm = policyTerm + " Years";
            } else {
                payingTerm = premPayTerm + " Years";
            }

            PdfPCell cell_firstYearPremium1 = new PdfPCell(new Paragraph(
                    "Premium Payment Term", small_normal));
            cell_firstYearPremium1.setPadding(5);
            PdfPCell cell_firstYearPremium2 = new PdfPCell(new Paragraph(
                    payingTerm, small_bold));
            cell_firstYearPremium2.setPadding(5);
            cell_firstYearPremium2.setHorizontalAlignment(Element.ALIGN_CENTER);

            // tablePolicyDetails2.addCell(cell_mode1);
            // tablePolicyDetails2.addCell(cell_mode2);

            // tablePolicyDetails2.addCell(cell_firstYearPremium1);
            //tablePolicyDetails2.addCell(cell_firstYearPremium2);

            document.add(tablePolicyDetails2);

            PdfPTable tablePolicyDetails3 = new PdfPTable(4);
            tablePolicyDetails3.setWidthPercentage(100);

           /* PdfPCell cell_monthly_prem1 = new PdfPCell(new Paragraph("Monthly Premium", small_normal));
            cell_monthly_prem1.setPadding(5);
            PdfPCell cell_monthly_prem2 = new PdfPCell(new Paragraph("Rs. " + premiumAmt.getText().toString(), small_bold));
            cell_monthly_prem2.setPadding(5);
            cell_monthly_prem2.setHorizontalAlignment(Element.ALIGN_CENTER);*/

            PdfPCell cell_monthly_prem3 = new PdfPCell(new Paragraph("", small_normal));
            // cell_monthly_prem1.setPadding(5);
            PdfPCell cell_monthly_prem4 = new PdfPCell(new Paragraph("", small_bold));
            //cell_monthly_prem2.setPadding(5);
            // cell_monthly_prem2.setHorizontalAlignment(Element.ALIGN_CENTER);

            // tablePolicyDetails3.addCell(cell_monthly_prem1);
            //  tablePolicyDetails3.addCell(cell_monthly_prem2);
            tablePolicyDetails3.addCell(cell_monthly_prem3);
            tablePolicyDetails3.addCell(cell_monthly_prem4);

            document.add(tablePolicyDetails3);

            document.add(para_img_logo_after_space_1);
            PdfPTable BI_PdftableFundDetails = new PdfPTable(1);
            BI_PdftableFundDetails.setWidthPercentage(100);

            if (is_trigger_fund.equalsIgnoreCase("false") && auto_allocation.equalsIgnoreCase("false")) {
                PdfPTable table_strategy = new PdfPTable(2);
                table_strategy.setWidthPercentage(100);

                PdfPCell cell_strategy1 = new PdfPCell(new Paragraph(
                        "Investment Strategy Opted For", small_normal));
                cell_strategy1.setPadding(5);
                PdfPCell cell_strategy2 = new PdfPCell(new Paragraph(
                        "Smart Choice Strategy", small_bold));
                cell_strategy2.setPadding(5);
                cell_strategy2.setHorizontalAlignment(Element.ALIGN_CENTER);

                table_strategy.addCell(cell_strategy1);
                table_strategy.addCell(cell_strategy2);

                document.add(table_strategy);
            } else if (is_trigger_fund.equalsIgnoreCase("false") && auto_allocation.equalsIgnoreCase("true")) {
                PdfPTable table_strategy = new PdfPTable(2);
                table_strategy.setWidthPercentage(100);

                PdfPCell cell_strategy1 = new PdfPCell(new Paragraph(
                        "Investment Strategy Opted For", small_normal));
                cell_strategy1.setPadding(5);
                PdfPCell cell_strategy2 = new PdfPCell(new Paragraph(
                        "Auto Asset Allocation Strategy", small_bold));
                cell_strategy2.setPadding(5);
                cell_strategy2.setHorizontalAlignment(Element.ALIGN_CENTER);

                table_strategy.addCell(cell_strategy1);
                table_strategy.addCell(cell_strategy2);

                document.add(table_strategy);
            } else {
                PdfPTable table_strategy = new PdfPTable(2);
                table_strategy.setWidthPercentage(100);

                PdfPCell cell_strategy1 = new PdfPCell(new Paragraph(
                        "Investment Strategy Opted For", small_normal));
                cell_strategy1.setPadding(5);
                PdfPCell cell_strategy2 = new PdfPCell(new Paragraph(
                        "Trigger Fund Strategy", small_bold));
                cell_strategy2.setPadding(5);
                cell_strategy2.setHorizontalAlignment(Element.ALIGN_CENTER);

                table_strategy.addCell(cell_strategy1);
                table_strategy.addCell(cell_strategy2);

                document.add(table_strategy);
            }

            // document.add(para_img_logo_after_space_1);

            PdfPCell BI_PdftableFundDetails_cell = new PdfPCell(new Paragraph(
                    "Allocation and FMC to the Funds chosen", small_bold));

            BI_PdftableFundDetails_cell
                    .setBackgroundColor(BaseColor.LIGHT_GRAY);

            BI_PdftableFundDetails_cell
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableFundDetails_cell.setPadding(5);

            BI_PdftableFundDetails.addCell(BI_PdftableFundDetails_cell);
            //document.add(BI_PdftableFundDetails);

            PdfPTable BI_PdftableFundTypes = new PdfPTable(4);
            BI_PdftableFundTypes.setWidthPercentage(100);

            PdfPCell BI_PdftableFundTypes_cell1 = new PdfPCell(new Paragraph(
                    "Fund Name (SFIN Name)", small_bold1));
            PdfPCell BI_PdftableFundTypes_cell2 = new PdfPCell(new Paragraph(
                    "% Allocation", small_bold1));

            PdfPCell BI_PdftableFundTypes_cell3 = new PdfPCell(new Paragraph(
                    "FMC", small_bold1));
            PdfPCell BI_PdftableFundTypes_cell4 = new PdfPCell(new Paragraph(
                    "Risk Level", small_bold1));

            BI_PdftableFundTypes_cell1
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableFundTypes_cell1.setPadding(5);

            BI_PdftableFundTypes_cell2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableFundTypes_cell2.setPadding(5);

            BI_PdftableFundTypes_cell3
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableFundTypes_cell3.setPadding(5);

            BI_PdftableFundTypes.addCell(BI_PdftableFundTypes_cell1);
            BI_PdftableFundTypes.addCell(BI_PdftableFundTypes_cell2);
            BI_PdftableFundTypes.addCell(BI_PdftableFundTypes_cell3);
            BI_PdftableFundTypes.addCell(BI_PdftableFundTypes_cell4);

            document.add(BI_PdftableFundTypes);


            if (is_trigger_fund.equalsIgnoreCase("false") && auto_allocation.equalsIgnoreCase("false")) {
                String EquityFundFMC = "1.35%";
                if (perInvEquityFund.equalsIgnoreCase("")) {

                    perInvEquityFund = "0";
                }

                PdfPTable BI_PdftableEquityFund = new PdfPTable(4);
                BI_PdftableEquityFund.setWidthPercentage(100);

                PdfPCell BI_PdftableEquityFund_cell1 = new PdfPCell(new Paragraph(
                        "Equity Fund (SFIN: ULIF001100105EQUITY-FND111)", small_normal));
                PdfPCell BI_PdftableEquityFund_cell2 = new PdfPCell(new Paragraph(
                        perInvEquityFund + " %", small_normal));

                PdfPCell BI_PdftableEquityFund_cell3 = new PdfPCell(new Paragraph(
                        EquityFundFMC, small_normal));
                PdfPCell BI_PdftableEquityFund_cell4 = new PdfPCell(new Paragraph(
                        "High", small_normal));

                BI_PdftableEquityFund_cell1
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableEquityFund_cell1.setPadding(5);

                BI_PdftableEquityFund_cell2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableEquityFund_cell2.setPadding(5);

                BI_PdftableEquityFund_cell3
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableEquityFund_cell3.setPadding(5);

                BI_PdftableEquityFund.addCell(BI_PdftableEquityFund_cell1);
                BI_PdftableEquityFund.addCell(BI_PdftableEquityFund_cell2);
                BI_PdftableEquityFund.addCell(BI_PdftableEquityFund_cell3);
                BI_PdftableEquityFund.addCell(BI_PdftableEquityFund_cell4);

                document.add(BI_PdftableEquityFund);

                if (perInvEquityOptimiserFund.equalsIgnoreCase("")) {

                    perInvEquityOptimiserFund = "0";
                }

                String EquityOptimiserFund = "0.0";
                String EquityOptimiserFundFMC = "1.35%";

                PdfPTable BI_PdftableEquityOptimiserFund = new PdfPTable(4);
                BI_PdftableEquityOptimiserFund.setWidthPercentage(100);

                PdfPCell BI_PdftableTopEquityOptimiserFund_cell1 = new PdfPCell(
                        new Paragraph("Equity Optimiser Fund (SFIN: ULIF010210108EQTYOPTFND111)", small_normal));
                PdfPCell BI_PdftableEquityOptimiserFund_cell2 = new PdfPCell(
                        new Paragraph(perInvEquityOptimiserFund + " %",
                                small_normal));

                PdfPCell BI_PdftableTopEquityOptimiserFund_cell3 = new PdfPCell(
                        new Paragraph(EquityOptimiserFundFMC, small_normal));
                PdfPCell BI_PdftableTopEquityOptimiserFund_cell4 = new PdfPCell(
                        new Paragraph("High", small_normal));

                BI_PdftableTopEquityOptimiserFund_cell1
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableTopEquityOptimiserFund_cell1.setPadding(5);

                BI_PdftableEquityOptimiserFund_cell2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableEquityOptimiserFund_cell2.setPadding(5);

                BI_PdftableTopEquityOptimiserFund_cell3
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableTopEquityOptimiserFund_cell3.setPadding(5);

                BI_PdftableEquityOptimiserFund
                        .addCell(BI_PdftableTopEquityOptimiserFund_cell1);
                BI_PdftableEquityOptimiserFund
                        .addCell(BI_PdftableEquityOptimiserFund_cell2);
                BI_PdftableEquityOptimiserFund
                        .addCell(BI_PdftableTopEquityOptimiserFund_cell3);
                BI_PdftableEquityOptimiserFund
                        .addCell(BI_PdftableTopEquityOptimiserFund_cell4);
                document.add(BI_PdftableEquityOptimiserFund);

                if (perInvgrowthFund.equalsIgnoreCase("")) {

                    perInvgrowthFund = "0";
                }

                String GrowthFund = "0.0";
                String GrowthFundFMC = "1.35%";

                PdfPTable BI_PdftableGrowthFund = new PdfPTable(4);
                BI_PdftableGrowthFund.setWidthPercentage(100);

                PdfPCell BI_PdftableTopGrowthFund_cell1 = new PdfPCell(
                        new Paragraph("Growth Fund (SFIN: ULIF003241105GROWTH-FND111)", small_normal));
                PdfPCell BI_PdftableGrowthFund_cell2 = new PdfPCell(new Paragraph(
                        perInvgrowthFund + " %", small_normal));

                PdfPCell BI_PdftableTopGrowthFund_cell3 = new PdfPCell(
                        new Paragraph(GrowthFundFMC, small_normal));
                PdfPCell BI_PdftableTopGrowthFund_cell4 = new PdfPCell(
                        new Paragraph("Medium to High", small_normal));

                BI_PdftableTopGrowthFund_cell1
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableTopGrowthFund_cell1.setPadding(5);

                BI_PdftableGrowthFund_cell2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableGrowthFund_cell2.setPadding(5);

                BI_PdftableTopGrowthFund_cell3
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableTopGrowthFund_cell3.setPadding(5);

                BI_PdftableGrowthFund.addCell(BI_PdftableTopGrowthFund_cell1);
                BI_PdftableGrowthFund.addCell(BI_PdftableGrowthFund_cell2);
                BI_PdftableGrowthFund.addCell(BI_PdftableTopGrowthFund_cell3);
                BI_PdftableGrowthFund.addCell(BI_PdftableTopGrowthFund_cell4);

                document.add(BI_PdftableGrowthFund);

                if (perInvBalancedFund.equalsIgnoreCase("")) {

                    perInvBalancedFund = "0";
                }

                String BalancedFund = "0.0";
                String BalancedFundFMC = "1.25%";

                PdfPTable BI_PdftableBalancedFund = new PdfPTable(4);
                BI_PdftableBalancedFund.setWidthPercentage(100);

                PdfPCell BI_PdftableBalancedFund_cell1 = new PdfPCell(
                        new Paragraph("Balanced Fund (SFIN: ULIF004051205BALANCDFND111)", small_normal));
                PdfPCell BI_PdftableBalancedFund_cell2 = new PdfPCell(
                        new Paragraph(perInvBalancedFund + " %", small_normal));

                PdfPCell BI_PdftableBalancedFund_cell3 = new PdfPCell(
                        new Paragraph(BalancedFundFMC, small_normal));
                PdfPCell BI_PdftableBalancedFund_cell4 = new PdfPCell(
                        new Paragraph("Medium", small_normal));

                BI_PdftableBalancedFund_cell1
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableBalancedFund_cell1.setPadding(5);

                BI_PdftableBalancedFund_cell2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableBalancedFund_cell2.setPadding(5);

                BI_PdftableBalancedFund_cell3
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableBalancedFund_cell3.setPadding(5);

                BI_PdftableBalancedFund.addCell(BI_PdftableBalancedFund_cell1);
                BI_PdftableBalancedFund.addCell(BI_PdftableBalancedFund_cell2);
                BI_PdftableBalancedFund.addCell(BI_PdftableBalancedFund_cell3);
                BI_PdftableBalancedFund.addCell(BI_PdftableBalancedFund_cell4);

                document.add(BI_PdftableBalancedFund);

                if (perInvCorpBondFund.equalsIgnoreCase("")) {

                    perInvCorpBondFund = "0";
                }

                String CorpBondFund = "0.0";
                String CorpBondFundFMC = "1.15%";

                PdfPTable BI_PdftableCorpBondFund = new PdfPTable(4);
                BI_PdftableCorpBondFund.setWidthPercentage(100);

                PdfPCell BI_PdftableCorpBondFund_cell1 = new PdfPCell(new Paragraph(
                        "Corporate Bond Fund (SFIN: ULIF033290618CORBONDFND111)", small_normal));
                PdfPCell BI_PdftableCorpBondFund_cell2 = new PdfPCell(new Paragraph(
                        perInvCorpBondFund + " %", small_normal));

                PdfPCell BI_PdftableCorpBondFund_cell3 = new PdfPCell(new Paragraph(
                        CorpBondFundFMC, small_normal));
                PdfPCell BI_PdftableCorpBondFund_cell4 = new PdfPCell(new Paragraph(
                        "Low to Medium", small_normal));

                BI_PdftableCorpBondFund_cell1
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableCorpBondFund_cell1.setPadding(5);

                BI_PdftableCorpBondFund_cell2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableCorpBondFund_cell2.setPadding(5);

                BI_PdftableCorpBondFund_cell3
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableCorpBondFund_cell3.setPadding(5);

                BI_PdftableCorpBondFund.addCell(BI_PdftableCorpBondFund_cell1);
                BI_PdftableCorpBondFund.addCell(BI_PdftableCorpBondFund_cell2);
                BI_PdftableCorpBondFund.addCell(BI_PdftableCorpBondFund_cell3);
                BI_PdftableCorpBondFund.addCell(BI_PdftableCorpBondFund_cell4);

                document.add(BI_PdftableCorpBondFund);

                if (perInvMidCapFund.equalsIgnoreCase("")) {

                    perInvMidCapFund = "0";
                }
                String MidCapFund = "0.0";
                String MidCapFundFMC = "1.35%";

                PdfPTable BI_PdftableMidCapFund = new PdfPTable(4);
                BI_PdftableMidCapFund.setWidthPercentage(100);

                PdfPCell BI_PdftableMidCapFund_cell1 = new PdfPCell(
                        new Paragraph("Midcap Fund (SFIN: ULIF031290915MIDCAPFUND111)", small_normal));
                PdfPCell BI_PdftableMidCapFund_cell2 = new PdfPCell(
                        new Paragraph(perInvMidCapFund + " %", small_normal));

                PdfPCell BI_PdftableMidCapFund_cell3 = new PdfPCell(
                        new Paragraph(MidCapFundFMC, small_normal));
                PdfPCell BI_PdftableMidCapFund_cell4 = new PdfPCell(
                        new Paragraph("High", small_normal));


                BI_PdftableMidCapFund_cell1
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableMidCapFund_cell1.setPadding(5);

                BI_PdftableMidCapFund_cell2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableMidCapFund_cell2.setPadding(5);

                BI_PdftableMidCapFund_cell3
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableMidCapFund_cell3.setPadding(5);

                BI_PdftableMidCapFund
                        .addCell(BI_PdftableMidCapFund_cell1);
                BI_PdftableMidCapFund
                        .addCell(BI_PdftableMidCapFund_cell2);
                BI_PdftableMidCapFund
                        .addCell(BI_PdftableMidCapFund_cell3);
                BI_PdftableMidCapFund
                        .addCell(BI_PdftableMidCapFund_cell4);
                document.add(BI_PdftableMidCapFund);

                if (perInvBondOptimiser.equalsIgnoreCase("")) {

                    perInvBondOptimiser = "0";
                }

                String BondOptimiser = "0.0";
                String BondOptimiserFMC = "1.15%";

                PdfPTable BI_PdftableBondOptimiser = new PdfPTable(4);
                BI_PdftableBondOptimiser.setWidthPercentage(100);

                PdfPCell BI_PdftableBondOptimiser_cell1 = new PdfPCell(new Paragraph(
                        "Bond Optimiser Fund (SFIN: ULIF032290618BONDOPTFND111)", small_normal));
                PdfPCell BI_PdftableBondOptimiser_cell2 = new PdfPCell(new Paragraph(
                        perInvBondOptimiser + " %", small_normal));

                PdfPCell BI_PdftableBondOptimiser_cell3 = new PdfPCell(new Paragraph(
                        BondOptimiserFMC, small_normal));
                PdfPCell BI_PdftableBondOptimiser_cell4 = new PdfPCell(new Paragraph(
                        "Low to Medium", small_normal));

                BI_PdftableBondOptimiser_cell1
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableBondOptimiser_cell1.setPadding(5);

                BI_PdftableBondOptimiser_cell2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableBondOptimiser_cell2.setPadding(5);

                BI_PdftableBondOptimiser_cell3
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableBondOptimiser_cell3.setPadding(5);

                BI_PdftableBondOptimiser.addCell(BI_PdftableBondOptimiser_cell1);
                BI_PdftableBondOptimiser.addCell(BI_PdftableBondOptimiser_cell2);
                BI_PdftableBondOptimiser.addCell(BI_PdftableBondOptimiser_cell3);
                BI_PdftableBondOptimiser.addCell(BI_PdftableBondOptimiser_cell4);

                document.add(BI_PdftableBondOptimiser);

                if (perInvPureFund.equalsIgnoreCase("")) {

                    perInvPureFund = "0";
                }

                String PureFund = "0.2";
                String PureFundFMC = "1.35%";

                PdfPTable BI_PdftablePureFund = new PdfPTable(4);
                BI_PdftablePureFund.setWidthPercentage(100);

                PdfPCell BI_PdftablePureFund_cell1 = new PdfPCell(new Paragraph(
                        "Pure Fund (SFIN: ULIF030290915PUREULPFND111)", small_normal));
                PdfPCell BI_PdftablePureFund_cell2 = new PdfPCell(new Paragraph(
                        perInvPureFund + " %", small_normal));

                PdfPCell BI_PdftablePureFund_cell3 = new PdfPCell(new Paragraph(
                        PureFundFMC, small_normal));
                PdfPCell BI_PdftablePureFund_cell4 = new PdfPCell(new Paragraph(
                        "High", small_normal));


                BI_PdftablePureFund_cell1
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftablePureFund_cell1.setPadding(5);

                BI_PdftablePureFund_cell2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftablePureFund_cell2.setPadding(5);

                BI_PdftablePureFund_cell3
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftablePureFund_cell3.setPadding(5);

                BI_PdftablePureFund.addCell(BI_PdftablePureFund_cell1);
                BI_PdftablePureFund.addCell(BI_PdftablePureFund_cell2);
                BI_PdftablePureFund.addCell(BI_PdftablePureFund_cell3);
                BI_PdftablePureFund.addCell(BI_PdftablePureFund_cell4);

                document.add(BI_PdftablePureFund);

                if (perInvMoneyMktFund.equalsIgnoreCase("")) {

                    perInvMoneyMktFund = "0";
                }

                String MoneyMktFund = "0.2";
                String MoneyMktFundFMC = "0.25%";

                PdfPTable BI_PdftableMoneyMktFund = new PdfPTable(4);
                BI_PdftableMoneyMktFund.setWidthPercentage(100);

                PdfPCell BI_PdftableMoneyMktFund_cell1 = new PdfPCell(new Paragraph(
                        "Money Market Fund (SFIN: ULIF005010206MONYMKTFND111)", small_normal));
                PdfPCell BI_PdftableMoneyMktFund_cell2 = new PdfPCell(new Paragraph(
                        perInvMoneyMktFund + " %", small_normal));

                PdfPCell BI_PdftableMoneyMktFund_cell3 = new PdfPCell(new Paragraph(
                        MoneyMktFundFMC, small_normal));
                PdfPCell BI_PdftableMoneyMktFund_cell4 = new PdfPCell(new Paragraph(
                        "Low", small_normal));


                BI_PdftableMoneyMktFund_cell1
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableMoneyMktFund_cell1.setPadding(5);

                BI_PdftableMoneyMktFund_cell2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableMoneyMktFund_cell2.setPadding(5);

                BI_PdftableMoneyMktFund_cell3
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableMoneyMktFund_cell3.setPadding(5);

                BI_PdftableMoneyMktFund.addCell(BI_PdftableMoneyMktFund_cell1);
                BI_PdftableMoneyMktFund.addCell(BI_PdftableMoneyMktFund_cell2);
                BI_PdftableMoneyMktFund.addCell(BI_PdftableMoneyMktFund_cell3);
                BI_PdftableMoneyMktFund.addCell(BI_PdftableMoneyMktFund_cell4);

                document.add(BI_PdftableMoneyMktFund);
            } else if (is_trigger_fund.equalsIgnoreCase("false") && auto_allocation.equalsIgnoreCase("true")) {
                {
                    String EquityFundFMC = "1.35%";
                    if (perInvEquityFund.equalsIgnoreCase("")) {

                        perInvEquityFund = "0";
                    }

                    PdfPTable BI_PdftableEquityFund = new PdfPTable(4);
                    BI_PdftableEquityFund.setWidthPercentage(100);

                    PdfPCell BI_PdftableEquityFund_cell1 = new PdfPCell(new Paragraph(
                            "Equity Fund (SFIN: ULIF001100105EQUITY-FND111)", small_normal));
                    PdfPCell BI_PdftableEquityFund_cell2 = new PdfPCell(new Paragraph("100 % of", small_normal));

                    PdfPCell BI_PdftableEquityFund_cell3 = new PdfPCell(new Paragraph(
                            EquityFundFMC, small_normal));
                    PdfPCell BI_PdftableEquityFund_cell4 = new PdfPCell(new Paragraph(
                            "High", small_normal));

                    BI_PdftableEquityFund_cell1
                            .setHorizontalAlignment(Element.ALIGN_CENTER);
                    BI_PdftableEquityFund_cell1.setPadding(5);

                    BI_PdftableEquityFund_cell2
                            .setHorizontalAlignment(Element.ALIGN_CENTER);
                    BI_PdftableEquityFund_cell2.setPadding(5);

                    BI_PdftableEquityFund_cell3
                            .setHorizontalAlignment(Element.ALIGN_CENTER);
                    BI_PdftableEquityFund_cell3.setPadding(5);

                    BI_PdftableEquityFund.addCell(BI_PdftableEquityFund_cell1);
                    BI_PdftableEquityFund.addCell(BI_PdftableEquityFund_cell2);
                    BI_PdftableEquityFund.addCell(BI_PdftableEquityFund_cell3);
                    BI_PdftableEquityFund.addCell(BI_PdftableEquityFund_cell4);

                    document.add(BI_PdftableEquityFund);

                    if (perInvCorpBondFund.equalsIgnoreCase("")) {

                        perInvCorpBondFund = "0";
                    }

                    String CorpBondFund = "0.0";
                    String CorpBondFundFMC = "1.15%";

                    PdfPTable BI_PdftableCorpBondFund = new PdfPTable(4);
                    BI_PdftableCorpBondFund.setWidthPercentage(100);

                    PdfPCell BI_PdftableCorpBondFund_cell1 = new PdfPCell(new Paragraph(
                            "Corporate Bond Fund (SFIN: ULIF033290618CORBONDFND111)", small_normal));
                    PdfPCell BI_PdftableCorpBondFund_cell2 = new PdfPCell(new Paragraph("Annualized Premium", small_normal));

                    PdfPCell BI_PdftableCorpBondFund_cell3 = new PdfPCell(new Paragraph(
                            CorpBondFundFMC, small_normal));
                    PdfPCell BI_PdftableCorpBondFund_cell4 = new PdfPCell(new Paragraph(
                            "Low to Medium", small_normal));

                    BI_PdftableCorpBondFund_cell1
                            .setHorizontalAlignment(Element.ALIGN_CENTER);
                    BI_PdftableCorpBondFund_cell1.setPadding(5);

                    BI_PdftableCorpBondFund_cell2
                            .setHorizontalAlignment(Element.ALIGN_CENTER);
                    BI_PdftableCorpBondFund_cell2.setPadding(5);

                    BI_PdftableCorpBondFund_cell3
                            .setHorizontalAlignment(Element.ALIGN_CENTER);
                    BI_PdftableCorpBondFund_cell3.setPadding(5);

                    BI_PdftableCorpBondFund.addCell(BI_PdftableCorpBondFund_cell1);
                    BI_PdftableCorpBondFund.addCell(BI_PdftableCorpBondFund_cell2);
                    BI_PdftableCorpBondFund.addCell(BI_PdftableCorpBondFund_cell3);
                    BI_PdftableCorpBondFund.addCell(BI_PdftableCorpBondFund_cell4);

                    document.add(BI_PdftableCorpBondFund);

                    if (perInvMoneyMktFund.equalsIgnoreCase("")) {

                        perInvMoneyMktFund = "0";
                    }

                    String MoneyMktFund = "0.2";
                    String MoneyMktFundFMC = "0.25%";

                    PdfPTable BI_PdftableMoneyMktFund = new PdfPTable(4);
                    BI_PdftableMoneyMktFund.setWidthPercentage(100);

                    PdfPCell BI_PdftableMoneyMktFund_cell1 = new PdfPCell(new Paragraph(
                            "Money Market Fund (SFIN: ULIF005010206MONYMKTFND111)", small_normal));
                    PdfPCell BI_PdftableMoneyMktFund_cell2 = new PdfPCell(new Paragraph(" among the three funds", small_normal));

                    PdfPCell BI_PdftableMoneyMktFund_cell3 = new PdfPCell(new Paragraph(
                            MoneyMktFundFMC, small_normal));
                    PdfPCell BI_PdftableMoneyMktFund_cell4 = new PdfPCell(new Paragraph(
                            "Low", small_normal));

                    BI_PdftableMoneyMktFund_cell1
                            .setHorizontalAlignment(Element.ALIGN_CENTER);
                    BI_PdftableMoneyMktFund_cell1.setPadding(5);

                    BI_PdftableMoneyMktFund_cell2
                            .setHorizontalAlignment(Element.ALIGN_CENTER);
                    BI_PdftableMoneyMktFund_cell2.setPadding(5);

                    BI_PdftableMoneyMktFund_cell3
                            .setHorizontalAlignment(Element.ALIGN_CENTER);
                    BI_PdftableMoneyMktFund_cell3.setPadding(5);

                    BI_PdftableMoneyMktFund.addCell(BI_PdftableMoneyMktFund_cell1);
                    BI_PdftableMoneyMktFund.addCell(BI_PdftableMoneyMktFund_cell2);
                    BI_PdftableMoneyMktFund.addCell(BI_PdftableMoneyMktFund_cell3);
                    BI_PdftableMoneyMktFund.addCell(BI_PdftableMoneyMktFund_cell4);

                    document.add(BI_PdftableMoneyMktFund);

                    if (perInvEquityOptimiserFund.equalsIgnoreCase("")) {

                        perInvEquityOptimiserFund = "0";
                    }

                    String EquityOptimiserFund = "0.0";
                    String EquityOptimiserFundFMC = "1.35%";

                    PdfPTable BI_PdftableEquityOptimiserFund = new PdfPTable(4);
                    BI_PdftableEquityOptimiserFund.setWidthPercentage(100);

                    PdfPCell BI_PdftableTopEquityOptimiserFund_cell1 = new PdfPCell(
                            new Paragraph("", small_normal));
                    PdfPCell BI_PdftableEquityOptimiserFund_cell2 = new PdfPCell(
                            new Paragraph("as term to maturity",
                                    small_normal));

                    PdfPCell BI_PdftableTopEquityOptimiserFund_cell3 = new PdfPCell(
                            new Paragraph("", small_normal));
                    PdfPCell BI_PdftableTopEquityOptimiserFund_cell4 = new PdfPCell(
                            new Paragraph("", small_normal));

                    BI_PdftableTopEquityOptimiserFund_cell1
                            .setHorizontalAlignment(Element.ALIGN_CENTER);
                    BI_PdftableTopEquityOptimiserFund_cell1.setPadding(5);

                    BI_PdftableEquityOptimiserFund_cell2
                            .setHorizontalAlignment(Element.ALIGN_CENTER);
                    BI_PdftableEquityOptimiserFund_cell2.setPadding(5);

                    BI_PdftableTopEquityOptimiserFund_cell3
                            .setHorizontalAlignment(Element.ALIGN_CENTER);
                    BI_PdftableTopEquityOptimiserFund_cell3.setPadding(5);

                    BI_PdftableEquityOptimiserFund
                            .addCell(BI_PdftableTopEquityOptimiserFund_cell1);
                    BI_PdftableEquityOptimiserFund
                            .addCell(BI_PdftableEquityOptimiserFund_cell2);
                    BI_PdftableEquityOptimiserFund
                            .addCell(BI_PdftableTopEquityOptimiserFund_cell3);
                    BI_PdftableEquityOptimiserFund
                            .addCell(BI_PdftableTopEquityOptimiserFund_cell4);
                    document.add(BI_PdftableEquityOptimiserFund);

                    if (perInvgrowthFund.equalsIgnoreCase("")) {

                        perInvgrowthFund = "0";
                    }

                    String GrowthFund = "0.0";
                    String GrowthFundFMC = "1.35%";

                    PdfPTable BI_PdftableGrowthFund = new PdfPTable(3);
                    BI_PdftableGrowthFund.setWidthPercentage(100);

                    PdfPCell BI_PdftableTopGrowthFund_cell1 = new PdfPCell(
                            new Paragraph("", small_normal));
                    PdfPCell BI_PdftableGrowthFund_cell2 = new PdfPCell(new Paragraph(
                            "Three Funds as per", small_normal));

                    PdfPCell BI_PdftableTopGrowthFund_cell3 = new PdfPCell(
                            new Paragraph("", small_normal));

                    BI_PdftableTopGrowthFund_cell1
                            .setHorizontalAlignment(Element.ALIGN_CENTER);
                    BI_PdftableTopGrowthFund_cell1.setPadding(5);

                    BI_PdftableGrowthFund_cell2
                            .setHorizontalAlignment(Element.ALIGN_CENTER);
                    BI_PdftableGrowthFund_cell2.setPadding(5);

                    BI_PdftableTopGrowthFund_cell3
                            .setHorizontalAlignment(Element.ALIGN_CENTER);
                    BI_PdftableTopGrowthFund_cell3.setPadding(5);

                    BI_PdftableGrowthFund.addCell(BI_PdftableTopGrowthFund_cell1);
                    BI_PdftableGrowthFund.addCell(BI_PdftableGrowthFund_cell2);
                    BI_PdftableGrowthFund.addCell(BI_PdftableTopGrowthFund_cell3);
                    //document.add(BI_PdftableGrowthFund);

                    if (perInvBalancedFund.equalsIgnoreCase("")) {

                        perInvBalancedFund = "0";
                    }

                    String BalancedFund = "0.0";
                    String BalancedFundFMC = "1.25%";

                    PdfPTable BI_PdftableBalancedFund = new PdfPTable(3);
                    BI_PdftableBalancedFund.setWidthPercentage(100);

                    PdfPCell BI_PdftableBalancedFund_cell1 = new PdfPCell(
                            new Paragraph("", small_normal));
                    PdfPCell BI_PdftableBalancedFund_cell2 = new PdfPCell(
                            new Paragraph(" Term to", small_normal));

                    PdfPCell BI_PdftableBalancedFund_cell3 = new PdfPCell(
                            new Paragraph("", small_normal));

                    BI_PdftableBalancedFund_cell1
                            .setHorizontalAlignment(Element.ALIGN_CENTER);
                    BI_PdftableBalancedFund_cell1.setPadding(5);

                    BI_PdftableBalancedFund_cell2
                            .setHorizontalAlignment(Element.ALIGN_CENTER);
                    BI_PdftableBalancedFund_cell2.setPadding(5);

                    BI_PdftableBalancedFund_cell3
                            .setHorizontalAlignment(Element.ALIGN_CENTER);
                    BI_PdftableBalancedFund_cell3.setPadding(5);

                    BI_PdftableBalancedFund.addCell(BI_PdftableBalancedFund_cell1);
                    BI_PdftableBalancedFund.addCell(BI_PdftableBalancedFund_cell2);
                    BI_PdftableBalancedFund.addCell(BI_PdftableBalancedFund_cell3);
                    // document.add(BI_PdftableBalancedFund);

                    if (perInvMidCapFund.equalsIgnoreCase("")) {

                        perInvMidCapFund = "0";
                    }
                    String MidCapFund = "0.0";
                    String MidCapFundFMC = "1.35%";

                    PdfPTable BI_PdftableMidCapFund = new PdfPTable(3);
                    BI_PdftableMidCapFund.setWidthPercentage(100);

                    PdfPCell BI_PdftableMidCapFund_cell1 = new PdfPCell(
                            new Paragraph("", small_normal));
                    PdfPCell BI_PdftableMidCapFund_cell2 = new PdfPCell(
                            new Paragraph("Maturity", small_normal));

                    PdfPCell BI_PdftableMidCapFund_cell3 = new PdfPCell(
                            new Paragraph("", small_normal));

                    BI_PdftableMidCapFund_cell1
                            .setHorizontalAlignment(Element.ALIGN_CENTER);
                    BI_PdftableMidCapFund_cell1.setPadding(5);

                    BI_PdftableMidCapFund_cell2
                            .setHorizontalAlignment(Element.ALIGN_CENTER);
                    BI_PdftableMidCapFund_cell2.setPadding(5);

                    BI_PdftableMidCapFund_cell3
                            .setHorizontalAlignment(Element.ALIGN_CENTER);
                    BI_PdftableMidCapFund_cell3.setPadding(5);

                    BI_PdftableMidCapFund
                            .addCell(BI_PdftableMidCapFund_cell1);
                    BI_PdftableMidCapFund
                            .addCell(BI_PdftableMidCapFund_cell2);
                    BI_PdftableMidCapFund
                            .addCell(BI_PdftableMidCapFund_cell3);
                    //document.add(BI_PdftableMidCapFund);

                }

            } else {
                String EquityFundFMC = "1.35%";
                if (perInvEquityFund.equalsIgnoreCase("")) {

                    perInvEquityFund = "0";
                }

                PdfPTable BI_PdftableEquityFund = new PdfPTable(4);
                BI_PdftableEquityFund.setWidthPercentage(100);

                PdfPCell BI_PdftableEquityFund_cell1 = new PdfPCell(new Paragraph(
                        "Equity Fund (SFIN: ULIF001100105EQUITY-FND111)", small_normal));
                PdfPCell BI_PdftableEquityFund_cell2 = new PdfPCell(new Paragraph("80%", small_normal));

                PdfPCell BI_PdftableEquityFund_cell3 = new PdfPCell(new Paragraph(
                        EquityFundFMC, small_normal));
                PdfPCell BI_PdftableEquityFund_cell4 = new PdfPCell(new Paragraph(
                        "High", small_normal));

                BI_PdftableEquityFund_cell1
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableEquityFund_cell1.setPadding(5);

                BI_PdftableEquityFund_cell2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableEquityFund_cell2.setPadding(5);

                BI_PdftableEquityFund_cell3
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableEquityFund_cell3.setPadding(5);

                BI_PdftableEquityFund.addCell(BI_PdftableEquityFund_cell1);
                BI_PdftableEquityFund.addCell(BI_PdftableEquityFund_cell2);
                BI_PdftableEquityFund.addCell(BI_PdftableEquityFund_cell3);
                BI_PdftableEquityFund.addCell(BI_PdftableEquityFund_cell4);

                document.add(BI_PdftableEquityFund);

                if (perInvEquityOptimiserFund.equalsIgnoreCase("")) {

                    perInvEquityOptimiserFund = "0";
                }

                String EquityOptimiserFund = "0.0";
                String EquityOptimiserFundFMC = "1.15%";

                PdfPTable BI_PdftableEquityOptimiserFund = new PdfPTable(4);
                BI_PdftableEquityOptimiserFund.setWidthPercentage(100);

                PdfPCell BI_PdftableTopEquityOptimiserFund_cell1 = new PdfPCell(
                        new Paragraph("Corporate Bond Fund (SFIN : ULIF033290618CORBONDFND111)", small_normal));
                PdfPCell BI_PdftableEquityOptimiserFund_cell2 = new PdfPCell(
                        new Paragraph("20%", small_normal));

                PdfPCell BI_PdftableTopEquityOptimiserFund_cell3 = new PdfPCell(
                        new Paragraph(EquityOptimiserFundFMC, small_normal));
                PdfPCell BI_PdftableTopEquityOptimiserFund_cell4 = new PdfPCell(
                        new Paragraph("Low to Medium", small_normal));

                BI_PdftableTopEquityOptimiserFund_cell1
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableTopEquityOptimiserFund_cell1.setPadding(5);

                BI_PdftableEquityOptimiserFund_cell2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableEquityOptimiserFund_cell2.setPadding(5);

                BI_PdftableTopEquityOptimiserFund_cell3
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableTopEquityOptimiserFund_cell3.setPadding(5);

                BI_PdftableEquityOptimiserFund
                        .addCell(BI_PdftableTopEquityOptimiserFund_cell1);
                BI_PdftableEquityOptimiserFund
                        .addCell(BI_PdftableEquityOptimiserFund_cell2);
                BI_PdftableEquityOptimiserFund
                        .addCell(BI_PdftableTopEquityOptimiserFund_cell3);
                BI_PdftableEquityOptimiserFund
                        .addCell(BI_PdftableTopEquityOptimiserFund_cell4);
                document.add(BI_PdftableEquityOptimiserFund);

                if (perInvgrowthFund.equalsIgnoreCase("")) {

                    perInvgrowthFund = "0";
                }

                String GrowthFund = "0.0";
                String GrowthFundFMC = "1.35%";

                PdfPTable BI_PdftableGrowthFund = new PdfPTable(3);
                BI_PdftableGrowthFund.setWidthPercentage(100);

                PdfPCell BI_PdftableTopGrowthFund_cell1 = new PdfPCell(
                        new Paragraph("Growth Fund (SFIN: ULIF003241105GROWTH-FND111)", small_normal));
                PdfPCell BI_PdftableGrowthFund_cell2 = new PdfPCell(new Paragraph(
                        " 0 %", small_normal));

                PdfPCell BI_PdftableTopGrowthFund_cell3 = new PdfPCell(
                        new Paragraph(GrowthFundFMC, small_normal));

                BI_PdftableTopGrowthFund_cell1
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableTopGrowthFund_cell1.setPadding(5);

                BI_PdftableGrowthFund_cell2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableGrowthFund_cell2.setPadding(5);

                BI_PdftableTopGrowthFund_cell3
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableTopGrowthFund_cell3.setPadding(5);

                BI_PdftableGrowthFund.addCell(BI_PdftableTopGrowthFund_cell1);
                BI_PdftableGrowthFund.addCell(BI_PdftableGrowthFund_cell2);
                BI_PdftableGrowthFund.addCell(BI_PdftableTopGrowthFund_cell3);
                //document.add(BI_PdftableGrowthFund);

                if (perInvBalancedFund.equalsIgnoreCase("")) {

                    perInvBalancedFund = "0";
                }

                String BalancedFund = "0.0";
                String BalancedFundFMC = "1.25%";

                PdfPTable BI_PdftableBalancedFund = new PdfPTable(3);
                BI_PdftableBalancedFund.setWidthPercentage(100);

                PdfPCell BI_PdftableBalancedFund_cell1 = new PdfPCell(
                        new Paragraph("Balanced Fund (SFIN: ULIF004051205BALANCDFND111)", small_normal));
                PdfPCell BI_PdftableBalancedFund_cell2 = new PdfPCell(
                        new Paragraph("0 %", small_normal));

                PdfPCell BI_PdftableBalancedFund_cell3 = new PdfPCell(
                        new Paragraph(BalancedFundFMC, small_normal));

                BI_PdftableBalancedFund_cell1
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableBalancedFund_cell1.setPadding(5);

                BI_PdftableBalancedFund_cell2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableBalancedFund_cell2.setPadding(5);

                BI_PdftableBalancedFund_cell3
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableBalancedFund_cell3.setPadding(5);

                BI_PdftableBalancedFund.addCell(BI_PdftableBalancedFund_cell1);
                BI_PdftableBalancedFund.addCell(BI_PdftableBalancedFund_cell2);
                BI_PdftableBalancedFund.addCell(BI_PdftableBalancedFund_cell3);
                //document.add(BI_PdftableBalancedFund);

                if (perInvCorpBondFund.equalsIgnoreCase("")) {

                    perInvCorpBondFund = "0";
                }

                String CorpBondFund = "0.0";
                String CorpBondFundFMC = "1.15%";

                PdfPTable BI_PdftableCorpBondFund = new PdfPTable(3);
                BI_PdftableCorpBondFund.setWidthPercentage(100);

                PdfPCell BI_PdftableCorpBondFund_cell1 = new PdfPCell(new Paragraph(
                        "Corporate Bond Fund (SFIN: ULIF033290618CORBONDFND111)", small_normal));
                PdfPCell BI_PdftableCorpBondFund_cell2 = new PdfPCell(new Paragraph("20 %", small_normal));

                PdfPCell BI_PdftableCorpBondFund_cell3 = new PdfPCell(new Paragraph(
                        CorpBondFundFMC, small_normal));

                BI_PdftableCorpBondFund_cell1
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableCorpBondFund_cell1.setPadding(5);

                BI_PdftableCorpBondFund_cell2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableCorpBondFund_cell2.setPadding(5);

                BI_PdftableCorpBondFund_cell3
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableCorpBondFund_cell3.setPadding(5);

                BI_PdftableCorpBondFund.addCell(BI_PdftableCorpBondFund_cell1);
                BI_PdftableCorpBondFund.addCell(BI_PdftableCorpBondFund_cell2);
                BI_PdftableCorpBondFund.addCell(BI_PdftableCorpBondFund_cell3);
                // document.add(BI_PdftableCorpBondFund);

                if (perInvMidCapFund.equalsIgnoreCase("")) {

                    perInvMidCapFund = "0";
                }
                String MidCapFund = "0.0";
                String MidCapFundFMC = "1.35%";

                PdfPTable BI_PdftableMidCapFund = new PdfPTable(3);
                BI_PdftableMidCapFund.setWidthPercentage(100);

                PdfPCell BI_PdftableMidCapFund_cell1 = new PdfPCell(
                        new Paragraph("Midcap Fund (SFIN: ULIF031290915MIDCAPFUND111)", small_normal));
                PdfPCell BI_PdftableMidCapFund_cell2 = new PdfPCell(
                        new Paragraph("0 %", small_normal));

                PdfPCell BI_PdftableMidCapFund_cell3 = new PdfPCell(
                        new Paragraph(MidCapFundFMC, small_normal));

                BI_PdftableMidCapFund_cell1
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableMidCapFund_cell1.setPadding(5);

                BI_PdftableMidCapFund_cell2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableMidCapFund_cell2.setPadding(5);

                BI_PdftableMidCapFund_cell3
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableMidCapFund_cell3.setPadding(5);

                BI_PdftableMidCapFund
                        .addCell(BI_PdftableMidCapFund_cell1);
                BI_PdftableMidCapFund
                        .addCell(BI_PdftableMidCapFund_cell2);
                BI_PdftableMidCapFund
                        .addCell(BI_PdftableMidCapFund_cell3);
                //document.add(BI_PdftableMidCapFund);

                if (perInvBondOptimiser.equalsIgnoreCase("")) {

                    perInvBondOptimiser = "0";
                }

                String BondOptimiser = "0.0";
                String BondOptimiserFMC = "1.15%";

                PdfPTable BI_PdftableBondOptimiser = new PdfPTable(3);
                BI_PdftableBondOptimiser.setWidthPercentage(100);

                PdfPCell BI_PdftableBondOptimiser_cell1 = new PdfPCell(new Paragraph(
                        "Bond Optimiser Fund (SFIN: ULIF032290618BONDOPTFND111)", small_normal));
                PdfPCell BI_PdftableBondOptimiser_cell2 = new PdfPCell(new Paragraph("0 %", small_normal));

                PdfPCell BI_PdftableBondOptimiser_cell3 = new PdfPCell(new Paragraph(
                        BondOptimiserFMC, small_normal));

                BI_PdftableBondOptimiser_cell1
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableBondOptimiser_cell1.setPadding(5);

                BI_PdftableBondOptimiser_cell2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableBondOptimiser_cell2.setPadding(5);

                BI_PdftableBondOptimiser_cell3
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableBondOptimiser_cell3.setPadding(5);

                BI_PdftableBondOptimiser.addCell(BI_PdftableBondOptimiser_cell1);
                BI_PdftableBondOptimiser.addCell(BI_PdftableBondOptimiser_cell2);
                BI_PdftableBondOptimiser.addCell(BI_PdftableBondOptimiser_cell3);
                //document.add(BI_PdftableBondOptimiser);

                if (perInvPureFund.equalsIgnoreCase("")) {

                    perInvPureFund = "0";
                }

                String PureFund = "0.2";
                String PureFundFMC = "1.35%";

                PdfPTable BI_PdftablePureFund = new PdfPTable(3);
                BI_PdftablePureFund.setWidthPercentage(100);

                PdfPCell BI_PdftablePureFund_cell1 = new PdfPCell(new Paragraph(
                        "Pure Fund (SFIN: ULIF030290915PUREULPFND111)", small_normal));
                PdfPCell BI_PdftablePureFund_cell2 = new PdfPCell(new Paragraph("0 %", small_normal));

                PdfPCell BI_PdftablePureFund_cell3 = new PdfPCell(new Paragraph(
                        PureFundFMC, small_normal));

                BI_PdftablePureFund_cell1
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftablePureFund_cell1.setPadding(5);

                BI_PdftablePureFund_cell2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftablePureFund_cell2.setPadding(5);

                BI_PdftablePureFund_cell3
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftablePureFund_cell3.setPadding(5);

                BI_PdftablePureFund.addCell(BI_PdftablePureFund_cell1);
                BI_PdftablePureFund.addCell(BI_PdftablePureFund_cell2);
                BI_PdftablePureFund.addCell(BI_PdftablePureFund_cell3);
                //document.add(BI_PdftablePureFund);

                if (perInvMoneyMktFund.equalsIgnoreCase("")) {

                    perInvMoneyMktFund = "0";
                }

                String MoneyMktFund = "0.2";
                String MoneyMktFundFMC = "0.25%";

                PdfPTable BI_PdftableMoneyMktFund = new PdfPTable(3);
                BI_PdftableMoneyMktFund.setWidthPercentage(100);

                PdfPCell BI_PdftableMoneyMktFund_cell1 = new PdfPCell(new Paragraph(
                        "Money Market Fund (SFIN: ULIF005010206MONYMKTFND111)", small_normal));
                PdfPCell BI_PdftableMoneyMktFund_cell2 = new PdfPCell(new Paragraph(" 0 %", small_normal));

                PdfPCell BI_PdftableMoneyMktFund_cell3 = new PdfPCell(new Paragraph(
                        MoneyMktFundFMC, small_normal));

                BI_PdftableMoneyMktFund_cell1
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableMoneyMktFund_cell1.setPadding(5);

                BI_PdftableMoneyMktFund_cell2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableMoneyMktFund_cell2.setPadding(5);

                BI_PdftableMoneyMktFund_cell3
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableMoneyMktFund_cell3.setPadding(5);

                BI_PdftableMoneyMktFund.addCell(BI_PdftableMoneyMktFund_cell1);
                BI_PdftableMoneyMktFund.addCell(BI_PdftableMoneyMktFund_cell2);
                BI_PdftableMoneyMktFund.addCell(BI_PdftableMoneyMktFund_cell3);
                // document.add(BI_PdftableMoneyMktFund);
            }

            document.add(para_img_logo_after_space_1);
            PdfPTable BI_Pdftablereadunderstand = new PdfPTable(1);
            BI_Pdftablereadunderstand.setWidthPercentage(100);
            PdfPCell BI_Pdftablereadunderstand_cell = new PdfPCell(new Paragraph(
                    "How to read and understand this benefit illustration?", small_bold));

            BI_Pdftablereadunderstand_cell
                    .setBackgroundColor(BaseColor.LIGHT_GRAY);

            BI_Pdftablereadunderstand_cell
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_Pdftablereadunderstand_cell.setPadding(5);

            BI_Pdftablereadunderstand.addCell(BI_Pdftablereadunderstand_cell);

            PdfPTable BI_Pdftable101 = new PdfPTable(1);
            BI_Pdftable101.setWidthPercentage(100);
            PdfPCell BI_Pdftable101_cell1 = new PdfPCell(
                    new Paragraph(
                            "This benefit illustration is intended to show what charges are deducted from your premiums and how the unit fund, net of charges and taxes, may grow over the years of the policy term if the fund earns a gross return of 8% p.a. or 4% p.a. These rates, i.e. 8% p.a. and 4% p.a. are assumed only for the purpose of illustrating the flow of benefits if the returns are at this level. It should not be interpreted that the returns under the plan are going to be  either 8% p.a. or 4% p.a.",

                            small_normal));

            BI_Pdftable101_cell1.setPadding(5);

            BI_Pdftable101.addCell(BI_Pdftable101_cell1);

            PdfPTable BI_Pdftable102 = new PdfPTable(1);
            BI_Pdftable102.setWidthPercentage(100);
            PdfPCell BI_Pdftable102_cell1 = new PdfPCell(
                    new Paragraph(
                            "Net yield mentioned corresponds to the gross investment return of 8% p.a., net of all charges but does not consider mortality, underwriting extra, if any, if deducted by cancellation of units. It demonstrates the impact of charges exclusive of taxes on the net yield. Please note that the mortality charges per thousand sum assured in general, increases with age.",

                            small_normal));

            BI_Pdftable102_cell1.setPadding(5);

            BI_Pdftable102.addCell(BI_Pdftable102_cell1);

            PdfPTable BI_Pdftable103 = new PdfPTable(1);
            BI_Pdftable103.setWidthPercentage(100);
            PdfPCell BI_Pdftable103_cell1 = new PdfPCell(
                    new Paragraph(
                            "The actual returns can vary depending on the performance of the chosen fund, charges towards mortality, underwriting extra etc. The investment risk in this policy is borne by the policyholder, hence, for more details on terms and conditions please read the sales literature carefully.",

                            small_normal));

            BI_Pdftable103_cell1.setPadding(5);

            BI_Pdftable103.addCell(BI_Pdftable103_cell1);

            PdfPTable BI_Pdftable104 = new PdfPTable(1);
            BI_Pdftable104.setWidthPercentage(100);
            PdfPCell BI_Pdftable104_cell1 = new PdfPCell(
                    new Paragraph(
                            "Part A of this statement presents a summary view of year- by- year charges deducted under the policy, fund value, surrender value and the death benefit, at two assumed rates of return. Part B of this statement presents a detailed break-up of the charges, and other values.",

                            small_normal));

            BI_Pdftable104_cell1.setPadding(5);

            BI_Pdftable104.addCell(BI_Pdftable104_cell1);
            document.add(BI_Pdftablereadunderstand);
            document.add(BI_Pdftable101);
            document.add(BI_Pdftable102);
            document.add(BI_Pdftable103);
            document.add(BI_Pdftable104);

            //Grid Start

            PdfPTable t1 = new PdfPTable(1);
            t1.setWidthPercentage(100);
            PdfPCell cell1 = new PdfPCell(new Paragraph("PART A", small_bold));
            t1.addCell(cell1);
            document.add(t1);


            PdfPTable BI_PdftableOutputHeader = new PdfPTable(4);

            BI_PdftableOutputHeader.setWidthPercentage(100);
            float[] columnWidths = {2f, 6f, 6f, 1f};
            BI_PdftableOutputHeader.setWidths(columnWidths);
            PdfPCell BI_PdftableBI_PdftableOutputHeader_cell1 = new PdfPCell(
                    new Paragraph("Amount in Rupees", small_bold2));
            PdfPCell BI_PdftableBI_PdftableOutputHeader_cell2 = new PdfPCell(
                    new Paragraph("At 4% p.a. Gross Investment return", small_bold2));
            PdfPCell BI_PdftableBI_PdftableOutputHeader_cell4 = new PdfPCell(
                    new Paragraph("At 8% p.a. Gross Investment return", small_bold2));
            PdfPCell BI_PdftableBI_PdftableOutputHeader_cell41 = new PdfPCell(
                    new Paragraph("", small_bold2));

            BI_PdftableBI_PdftableOutputHeader_cell1
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableBI_PdftableOutputHeader_cell1.setPadding(5);

            BI_PdftableBI_PdftableOutputHeader_cell2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableBI_PdftableOutputHeader_cell2.setPadding(5);

            /*BI_PdftableBI_PdftableOutputHeader_cell3
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableBI_PdftableOutputHeader_cell3.setPadding(5);*/

            BI_PdftableBI_PdftableOutputHeader_cell4
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableBI_PdftableOutputHeader_cell4.setPadding(5);
            BI_PdftableBI_PdftableOutputHeader_cell41
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableBI_PdftableOutputHeader_cell41.setPadding(5);

            BI_PdftableOutputHeader
                    .addCell(BI_PdftableBI_PdftableOutputHeader_cell1);
            BI_PdftableOutputHeader
                    .addCell(BI_PdftableBI_PdftableOutputHeader_cell2);
            /*BI_PdftableOutputHeader
                    .addCell(BI_PdftableBI_PdftableOutputHeader_cell3);*/
            BI_PdftableOutputHeader
                    .addCell(BI_PdftableBI_PdftableOutputHeader_cell4);
            BI_PdftableOutputHeader
                    .addCell(BI_PdftableBI_PdftableOutputHeader_cell41);

            document.add(BI_PdftableOutputHeader);

            /*PdfPTable BI_PdftablecolumnsNo = new PdfPTable(16);
            BI_PdftablecolumnsNo.setWidthPercentage(100);

            PdfPCell BI_Pdftable_columnsNo_cell1 = new PdfPCell(new Paragraph(
                    "1", small_bold2));
            PdfPCell BI_Pdftable_columnsNo_cell2 = new PdfPCell(new Paragraph(
                    "2", small_bold2));

            PdfPCell BI_Pdftable_columnsNo_cell3 = new PdfPCell(new Paragraph(
                    "3", small_bold2));

            PdfPCell BI_Pdftable_columnsNo_cell4 = new PdfPCell(new Paragraph(
                    "4", small_bold2));
            PdfPCell BI_Pdftable_columnsNo_cell5 = new PdfPCell(new Paragraph(
                    "5", small_bold2));

            PdfPCell BI_Pdftable_columnsNo_cell6 = new PdfPCell(new Paragraph(
                    "6", small_bold2));

            PdfPCell BI_Pdftable_columnsNo_cell7 = new PdfPCell(new Paragraph(
                    "7", small_bold2));
            PdfPCell BI_Pdftable_columnsNo_cell8 = new PdfPCell(new Paragraph(
                    "8", small_bold2));

            PdfPCell BI_Pdftable_columnsNo_cell9 = new PdfPCell(new Paragraph(
                    "9", small_bold2));

            PdfPCell BI_Pdftable_columnsNo_cell10 = new PdfPCell(new Paragraph(
                    "10", small_bold2));
            PdfPCell BI_Pdftable_columnsNo_cell11 = new PdfPCell(new Paragraph(
                    "11", small_bold2));
            PdfPCell BI_Pdftable_columnsNo_cell12 = new PdfPCell(new Paragraph(
                    "12", small_bold2));

            PdfPCell BI_Pdftable_columnsNo_cell13 = new PdfPCell(new Paragraph(
                    "13", small_bold2));
            PdfPCell BI_Pdftable_columnsNo_cell14 = new PdfPCell(new Paragraph(
                    "14", small_bold2));

            PdfPCell BI_Pdftable_columnsNo_cell15 = new PdfPCell(new Paragraph(
                    "15", small_bold2));
            PdfPCell BI_Pdftable_columnsNo_cell16 = new PdfPCell(new Paragraph(
                    "16", small_bold2));


            BI_PdftablecolumnsNo.addCell(BI_Pdftable_columnsNo_cell1);
            BI_PdftablecolumnsNo.addCell(BI_Pdftable_columnsNo_cell2);
            BI_PdftablecolumnsNo.addCell(BI_Pdftable_columnsNo_cell3);

            BI_PdftablecolumnsNo.addCell(BI_Pdftable_columnsNo_cell4);
            BI_PdftablecolumnsNo.addCell(BI_Pdftable_columnsNo_cell5);
            BI_PdftablecolumnsNo.addCell(BI_Pdftable_columnsNo_cell6);

            BI_PdftablecolumnsNo.addCell(BI_Pdftable_columnsNo_cell7);
            BI_PdftablecolumnsNo.addCell(BI_Pdftable_columnsNo_cell8);
            BI_PdftablecolumnsNo.addCell(BI_Pdftable_columnsNo_cell9);

            BI_PdftablecolumnsNo.addCell(BI_Pdftable_columnsNo_cell10);
            BI_PdftablecolumnsNo.addCell(BI_Pdftable_columnsNo_cell11);
            BI_PdftablecolumnsNo.addCell(BI_Pdftable_columnsNo_cell12);

            BI_PdftablecolumnsNo.addCell(BI_Pdftable_columnsNo_cell13);
            BI_PdftablecolumnsNo.addCell(BI_Pdftable_columnsNo_cell14);
            BI_PdftablecolumnsNo.addCell(BI_Pdftable_columnsNo_cell15);
            BI_PdftablecolumnsNo.addCell(BI_Pdftable_columnsNo_cell16);

            document.add(BI_PdftablecolumnsNo);*/

            PdfPTable BI_Pdftableoutput = new PdfPTable(17);
            BI_Pdftableoutput.setWidthPercentage(100);

            PdfPCell BI_Pdftable_output_cell1 = new PdfPCell(new Paragraph(
                    "Policy Year", small_bold2));
            PdfPCell BI_Pdftable_output_cell2 = new PdfPCell(new Paragraph(
                    "Annualized Premium", small_bold2));

            PdfPCell BI_Pdftable_output_cell3 = new PdfPCell(new Paragraph(
                    "Mortality  Charges", small_bold2));

            PdfPCell BI_Pdftable_output_cell4 = new PdfPCell(new Paragraph(
                    "Other Charges*",
                    small_bold2));
            PdfPCell BI_Pdftable_output_cell5 = new PdfPCell(new Paragraph(
                    "Applicable Taxes", small_bold2));

            PdfPCell BI_Pdftable_output_cell6 = new PdfPCell(new Paragraph(
                    "Fund at end of the year", small_bold2));

            PdfPCell BI_Pdftable_output_cell655 = new PdfPCell(new Paragraph(
                    "Return of Moratlity Charge", small_bold2));

            PdfPCell BI_Pdftable_output_cell7 = new PdfPCell(new Paragraph(
                    "Surrender Value", small_bold2));
            PdfPCell BI_Pdftable_output_cell8 = new PdfPCell(new Paragraph(
                    "Death Benefit", small_bold2));
            PdfPCell BI_Pdftable_output_cell81 = new PdfPCell(new Paragraph(
                    "Mortality  Charges", small_bold2));

            PdfPCell BI_Pdftable_output_cell9 = new PdfPCell(new Paragraph(
                    "Other Charges*", small_bold2));

            PdfPCell BI_Pdftable_output_cell10 = new PdfPCell(new Paragraph(
                    "Applicable Taxes", small_bold2));
            PdfPCell BI_Pdftable_output_cell11 = new PdfPCell(new Paragraph(
                    "Fund at end of the year", small_bold2));

            PdfPCell BI_Pdftable_output_cell1122 = new PdfPCell(new Paragraph(
                    "Return of Moratlity Charge", small_bold2));


            PdfPCell BI_Pdftable_output_cell13 = new PdfPCell(new Paragraph(
                    "Surrender Value", small_bold2));
            PdfPCell BI_Pdftable_output_cell14 = new PdfPCell(new Paragraph(
                    "Death Benefit", small_bold2));

            PdfPCell BI_Pdftable_output_cell24 = new PdfPCell(new Paragraph(
                    "Commission payable to Intermediary(Rs)", small_bold2));

            BI_Pdftableoutput.addCell(BI_Pdftable_output_cell1);
            BI_Pdftableoutput.addCell(BI_Pdftable_output_cell2);
            BI_Pdftableoutput.addCell(BI_Pdftable_output_cell3);

            BI_Pdftableoutput.addCell(BI_Pdftable_output_cell4);
            BI_Pdftableoutput.addCell(BI_Pdftable_output_cell5);
            BI_Pdftableoutput.addCell(BI_Pdftable_output_cell6);
            BI_Pdftableoutput.addCell(BI_Pdftable_output_cell655);

            BI_Pdftableoutput.addCell(BI_Pdftable_output_cell7);
            BI_Pdftableoutput.addCell(BI_Pdftable_output_cell8);
            BI_Pdftableoutput.addCell(BI_Pdftable_output_cell81);
            BI_Pdftableoutput.addCell(BI_Pdftable_output_cell9);

            BI_Pdftableoutput.addCell(BI_Pdftable_output_cell10);
            BI_Pdftableoutput.addCell(BI_Pdftable_output_cell11);
            BI_Pdftableoutput.addCell(BI_Pdftable_output_cell1122);

            BI_Pdftableoutput.addCell(BI_Pdftable_output_cell13);
            BI_Pdftableoutput.addCell(BI_Pdftable_output_cell14);

            BI_Pdftableoutput.addCell(BI_Pdftable_output_cell24);
            document.add(BI_Pdftableoutput);

            PdfPTable BI_Pdftableoutput_no = new PdfPTable(17);
            BI_Pdftableoutput_no.setWidthPercentage(100);

            PdfPCell BI_Pdftable_output_no_cell1 = new PdfPCell(new Paragraph(
                    "1", small_bold2));
            PdfPCell BI_Pdftable_output_no_cell2 = new PdfPCell(new Paragraph(
                    "2", small_bold2));

            PdfPCell BI_Pdftable_output_no_cell3 = new PdfPCell(new Paragraph(
                    "3", small_bold2));

            PdfPCell BI_Pdftable_output_no_cell4 = new PdfPCell(new Paragraph(
                    "4", small_bold2));
            PdfPCell BI_Pdftable_output_no_cell5 = new PdfPCell(new Paragraph(
                    "5", small_bold2));

            PdfPCell BI_Pdftable_output_no_cell6 = new PdfPCell(new Paragraph(
                    "6", small_bold2));

            PdfPCell BI_Pdftable_output_no_cell7 = new PdfPCell(new Paragraph(
                    "7", small_bold2));
            PdfPCell BI_Pdftable_output_no_cell8 = new PdfPCell(new Paragraph(
                    "8", small_bold2));

            PdfPCell BI_Pdftable_output_no_cell9 = new PdfPCell(new Paragraph(
                    "9", small_bold2));

            PdfPCell BI_Pdftable_output_no_cell10 = new PdfPCell(new Paragraph(
                    "10", small_bold2));
            PdfPCell BI_Pdftable_output_no_cell11 = new PdfPCell(new Paragraph(
                    "11", small_bold2));

            PdfPCell BI_Pdftable_output_no_cell12 = new PdfPCell(new Paragraph(
                    "12", small_bold2));

            PdfPCell BI_Pdftable_output_no_cell13 = new PdfPCell(new Paragraph(
                    "13", small_bold2));
            PdfPCell BI_Pdftable_output_no_cell14 = new PdfPCell(new Paragraph(
                    "14", small_bold2));


            PdfPCell BI_Pdftable_output_no_cell24 = new PdfPCell(new Paragraph(
                    "15", small_bold2));

            PdfPCell BI_Pdftable_output_no_cell249 = new PdfPCell(new Paragraph(
                    "16", small_bold2));

            PdfPCell BI_Pdftable_output_no_cell2489 = new PdfPCell(new Paragraph(
                    "17", small_bold2));


            BI_Pdftableoutput_no.addCell(BI_Pdftable_output_no_cell1);
            BI_Pdftableoutput_no.addCell(BI_Pdftable_output_no_cell2);
            BI_Pdftableoutput_no.addCell(BI_Pdftable_output_no_cell3);

            BI_Pdftableoutput_no.addCell(BI_Pdftable_output_no_cell4);
            BI_Pdftableoutput_no.addCell(BI_Pdftable_output_no_cell5);
            BI_Pdftableoutput_no.addCell(BI_Pdftable_output_no_cell6);

            BI_Pdftableoutput_no.addCell(BI_Pdftable_output_no_cell7);
            BI_Pdftableoutput_no.addCell(BI_Pdftable_output_no_cell8);
            BI_Pdftableoutput_no.addCell(BI_Pdftable_output_no_cell9);

            BI_Pdftableoutput_no.addCell(BI_Pdftable_output_no_cell10);
            BI_Pdftableoutput_no.addCell(BI_Pdftable_output_no_cell11);
            BI_Pdftableoutput_no.addCell(BI_Pdftable_output_no_cell12);

            BI_Pdftableoutput_no.addCell(BI_Pdftable_output_no_cell13);
            BI_Pdftableoutput_no.addCell(BI_Pdftable_output_no_cell14);

            BI_Pdftableoutput_no.addCell(BI_Pdftable_output_no_cell24);
            BI_Pdftableoutput_no.addCell(BI_Pdftable_output_no_cell249);
            BI_Pdftableoutput_no.addCell(BI_Pdftable_output_no_cell2489);
            document.add(BI_Pdftableoutput_no);

            for (int i = 0; i < Integer.parseInt(policyTerm); i++) {

                PdfPTable BI_Pdftableoutput_row1 = new PdfPTable(17);
                BI_Pdftableoutput_row1.setWidthPercentage(100);

                PdfPCell BI_Pdftable_output_row1_cell1 = new PdfPCell(
                        new Paragraph(list_data.get(i).getPolicy_year(),
                                small_bold2));
                PdfPCell BI_Pdftable_output_row1_cell2 = new PdfPCell(
                        new Paragraph(list_data.get(i).getPremium(),
                                small_bold2));

                PdfPCell BI_Pdftable_output_row1_cell3 = new PdfPCell(
                        new Paragraph(list_data.get(i)
                                .getMortality_charge1(), small_bold2));

                /*PdfPCell BI_Pdftable_output_row1_cell4 = new PdfPCell(
                        new Paragraph(list_data.get(i)
                                .getAmount_for_investment(), small_bold2));*/
                PdfPCell BI_Pdftable_output_row1_cell5 = new PdfPCell(
                        new Paragraph(list_data.get(i)
                                .getTotal_charge1(), small_bold2));

                PdfPCell BI_Pdftable_output_row1_cell6 = new PdfPCell(
                        new Paragraph(list_data.get(i).getService_tax_on_mortality_charge1(),
                                small_bold2));

                PdfPCell BI_Pdftable_output_row1_cell7 = new PdfPCell(
                        new Paragraph(list_data.get(i).getFund_value_at_end1(),
                                small_bold2));
                PdfPCell BI_Pdftable_output_row1_cell722 = new PdfPCell(
                        new Paragraph(list_data.get(i).getTv_return_of_moratlity_charge1(),
                                small_bold2));
                PdfPCell BI_Pdftable_output_row1_cell8 = new PdfPCell(
                        new Paragraph(list_data.get(i)
                                .getSurrender_value1(),
                                small_bold2));
                PdfPCell BI_Pdftable_output_row1_cell81 = new PdfPCell(
                        new Paragraph(list_data.get(i).getDeath_benefit1(), small_bold2));


                PdfPCell BI_Pdftable_output_row1_cell31 = new PdfPCell(
                        new Paragraph(list_data.get(i)
                                .getMortality_charge2(), small_bold2));

                PdfPCell BI_Pdftable_output_row1_cell51 = new PdfPCell(
                        new Paragraph(list_data.get(i)
                                .getTotal_charge2(), small_bold2));

                PdfPCell BI_Pdftable_output_row1_cell61 = new PdfPCell(
                        new Paragraph(list_data.get(i).getService_tax_on_mortality_charge2(),
                                small_bold2));

                PdfPCell BI_Pdftable_output_row1_cell12 = new PdfPCell(
                        new Paragraph(list_data.get(i).getFund_value_at_end2(),
                                small_bold2));
                PdfPCell BI_Pdftable_output_row1_cell12334 = new PdfPCell(
                        new Paragraph(list_data.get(i).getTv_return_of_moratlity_charge2(),
                                small_bold2));

                PdfPCell BI_Pdftable_output_row1_cell13 = new PdfPCell(
                        new Paragraph(list_data.get(i).getSurrender_value2(),
                                small_bold2));

                PdfPCell BI_Pdftable_output_row1_cell14 = new PdfPCell(
                        new Paragraph(list_data.get(i).getDeath_benefit2(),
                                small_bold2));


                PdfPCell BI_Pdftable_output_row1_cell24 = new PdfPCell(
                        new Paragraph(list_data.get(i).getCommission(),
                                small_bold2));

                /*PdfPCell BI_Pdftable_output_row1_cell16 = new PdfPCell(
                        new Paragraph(list_data.get(i).getReturnmortality(),
                                small_bold2));*/

                BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell1);
                BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell2);
                BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell3);

                //BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell4);
                BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell5);
                BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell6);

                BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell7);

                BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell722);
                BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell8);
                //BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell9);

                BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell81);
                BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell31);

                BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell51);
                BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell61);

                BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell12);
                BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell12334);

                BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell13);
                BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell14);

                BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell24);
                document.add(BI_Pdftableoutput_row1);

            }


            Calendar present_date1 = Calendar.getInstance();
            int mDay1 = present_date1.get(Calendar.DAY_OF_MONTH);
            int mMonth1 = present_date1.get(Calendar.MONTH);
            int mYear1 = present_date1.get(Calendar.YEAR);

            String CurrentDate1 = mDay1 + "-" + (mMonth1 + 1) + "-" + mYear;
            PdfPTable BI_Pdftable2611 = new PdfPTable(1);

            BI_Pdftable2611.setWidthPercentage(100);
            // CR_table6.setWidths(columnWidths_26);
            PdfPCell BI_Pdftable26_cell14 = new PdfPCell(
                    new Paragraph(
                            "I, "
                                    + name_of_person
                                    + "    have received the information with respect to the above and have understood the above statement before entering into a contract.",
                            small_bold));

            BI_Pdftable26_cell14.setPadding(5);


            BI_Pdftable2611.addCell(BI_Pdftable26_cell14);

            PdfPTable BI_Pdftable261 = new PdfPTable(1);
            BI_Pdftable261.setWidthPercentage(100);
            // CR_table6.setWidths(columnWidths_26);

            PdfPCell place22 = new PdfPCell(
                    new Paragraph("Place :" + place2, small_normal));
            PdfPCell date22 = new PdfPCell(
                    new Paragraph("Date  :" + CurrentDate1, small_normal));

            PdfPTable BI_Pdftable_eSign1 = new PdfPTable(1);
            BI_Pdftable_eSign1.setWidthPercentage(100);
            // CR_table6.setWidths(columnWidths_26);
            PdfPCell BI_Pdftable_eSign_cell13 = new PdfPCell(new Paragraph(

                    "This document is eSigned by " + name_of_person, small_bold));

            BI_Pdftable_eSign_cell13.setPadding(5);

            BI_Pdftable_eSign1.addCell(BI_Pdftable_eSign_cell13);
            BI_Pdftable_eSign1.addCell(place22);
            BI_Pdftable_eSign1.addCell(date22);

            PdfPTable BI_PdftableMarketing1 = new PdfPTable(1);
            BI_PdftableMarketing1.setWidthPercentage(100);
            PdfPCell BI_PdftableMarketing_signature_cell5 = new PdfPCell(
                    new Paragraph("Marketing official's Signature & Company Seal", small_bold));
            BI_PdftableMarketing_signature_cell5
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableMarketing_signature_cell5
                    .setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableMarketing_signature_cell5.setPadding(5);

            BI_PdftableMarketing1.addCell(BI_PdftableMarketing_signature_cell5);

            PdfPCell BI_Pdftable26_cell113 = new PdfPCell(
                    new Paragraph(
                            "I, "
                                    + name_of_proposer
                                    + "     , have explained the premiums, charges and benefits under the policy fully to the prospect/policyholder.",
                            small_bold));

            BI_Pdftable26_cell113.setPadding(5);

            BI_PdftableMarketing1.addCell(BI_Pdftable26_cell113);

            /*M_ChannelDetails list_channel_detail5 = db.getChannelDetail(sr_Code);
            String code5 = list_channel_detail5.getCode();
            String name5 = list_channel_detail5.getName();
            String str_sign_header1 = "";
            if (userType.equals("AGENT")) {
                str_sign_header1 = "(IA code- " + code5 + ")" + "\n"
                        + "Name of IA- " + name5 + "\n"
                        + "Authenticated by Id & Password";
            } else if (str_usertype.equals("CIF")) {
                str_sign_header1 = "(CIF code- " + code5 + ")" + "\n"
                        + "Name of CIF- " + name5 + "\n"
                        + "Authenticated by Id & Password";

            } else if (str_usertype.equals("BAP")) {
                str_sign_header1 = "(BAP code- " + code5 + ")" + "\n"
                        + "Name of Broker- " + name5 + "\n"
                        + "Authenticated by Id & Password";

            } else if (str_usertype.equals("CAG")) {
                str_sign_header1 = "(CAG code- " + code5 + ")" + "\n"
                        + "Name of Corporate Agent- " + name5 + "\n"
                        + "Authenticated by Id & Password";

            }*/

            PdfPTable BI_PdftableMarketing_signature1 = new PdfPTable(3);

            BI_PdftableMarketing_signature1.setWidthPercentage(100);

            PdfPCell BI_PdftableMarketing_signature_115 = new PdfPCell(
                    new Paragraph("Place :" + place2, small_normal));
            PdfPCell BI_PdftableMarketing_signature_215 = new PdfPCell(
                    new Paragraph("Date  :" + CurrentDate1, small_normal));

            /*PdfPCell BI_PdftableMarketing_signature_315 = new PdfPCell(
                    new Paragraph(str_sign_header1, small_normal));*/

            BI_PdftableMarketing_signature_115.setPadding(5);
            BI_PdftableMarketing_signature_215.setPadding(5);

            BI_PdftableMarketing_signature1
                    .addCell(BI_PdftableMarketing_signature_115);
            BI_PdftableMarketing_signature1
                    .addCell(BI_PdftableMarketing_signature_215);
            /*BI_PdftableMarketing_signature1
                    .addCell(BI_PdftableMarketing_signature_315);*/

            PdfPTable BI_PdftableOutputHeader833 = new PdfPTable(3);
            BI_PdftableOutputHeader833.setWidthPercentage(100);
            float[] columnWidths833 = {2f, 6f, 7f};
            BI_PdftableOutputHeader833.setWidths(columnWidths833);
            PdfPCell see_partB = new PdfPCell(
                    new Paragraph("* See Part B for details", small_bold2));
            see_partB.setColspan(3);
            PdfPCell decl = new PdfPCell(
                    new Paragraph("IN THIS POLICY, THE INVESTMENT RISK IS BORNE BY THE POLICYHOLDER AND THE ABOVE INTEREST RATES ARE ONLY FOR ILLUSTRATIVE PURPOSE", small_bold2));
            decl.setColspan(3);

            BI_PdftableOutputHeader833
                    .addCell(see_partB);
            BI_PdftableOutputHeader833
                    .addCell(decl);


            document.add(BI_PdftableOutputHeader833);

            document.add(BI_Pdftable2611);
            document.add(BI_Pdftable_eSign1);
            document.add(BI_PdftableMarketing1);
            document.add(BI_PdftableMarketing_signature1);

            PdfPTable t22 = new PdfPTable(1);
            t22.setWidthPercentage(100);
            PdfPCell cell22 = new PdfPCell(new Paragraph("* See Part B for details", small_bold2));
            t22.addCell(cell22);
            //document.add(t22);

            PdfPTable t2 = new PdfPTable(1);
            t2.setWidthPercentage(100);
            PdfPCell cell2 = new PdfPCell(new Paragraph("PART B", small_bold));
            t2.addCell(cell2);
            document.add(t2);


            PdfPTable BI_PdftableOutputHeader8 = new PdfPTable(3);
            BI_PdftableOutputHeader8.setWidthPercentage(100);
            float[] columnWidths8 = {2f, 6f, 7f};
            BI_PdftableOutputHeader8.setWidths(columnWidths8);
            PdfPCell BI_PdftableBI_PdftableOutputHeader_cell18 = new PdfPCell(
                    new Paragraph("Amount in  Rs.", small_bold2));
            PdfPCell BI_PdftableBI_PdftableOutputHeader_cell28 = new PdfPCell(
                    new Paragraph("Gross Yield 8% pa",
                            small_bold2));


            PdfPCell BI_PdftableBI_PdftableOutputHeader_cell48 = new PdfPCell(
                    new Paragraph("Net Yield " + netYield8pa + "%", small_bold2));

            BI_PdftableBI_PdftableOutputHeader_cell18
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableBI_PdftableOutputHeader_cell18.setPadding(5);

            BI_PdftableBI_PdftableOutputHeader_cell28
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableBI_PdftableOutputHeader_cell28.setPadding(5);


            BI_PdftableBI_PdftableOutputHeader_cell48
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableBI_PdftableOutputHeader_cell48.setPadding(5);

            BI_PdftableOutputHeader8
                    .addCell(BI_PdftableBI_PdftableOutputHeader_cell18);
            BI_PdftableOutputHeader8
                    .addCell(BI_PdftableBI_PdftableOutputHeader_cell28);

            BI_PdftableOutputHeader8
                    .addCell(BI_PdftableBI_PdftableOutputHeader_cell48);

            document.add(BI_PdftableOutputHeader8);

            PdfPTable BI_Pdftableoutput8 = new PdfPTable(16);
            BI_Pdftableoutput8.setWidthPercentage(100);

            PdfPCell BI_Pdftable_output_cell18 = new PdfPCell(new Paragraph(
                    "Policy Year", small_bold2));
            PdfPCell BI_Pdftable_output_cell28 = new PdfPCell(new Paragraph(
                    "Annualized Premium  (AP) ", small_bold2));

            PdfPCell BI_Pdftable_output_cell38 = new PdfPCell(new Paragraph(
                    "Premium Allocation Charge (PAC)", small_bold2));

            PdfPCell BI_Pdftable_output_cell48 = new PdfPCell(new Paragraph(
                    "Annualized Premium - Premium Allocation Charge",
                    small_bold2));
            PdfPCell BI_Pdftable_output_cell58 = new PdfPCell(new Paragraph(
                    "Mortality charge", small_bold2));

            PdfPCell BI_Pdftable_output_cell68 = new PdfPCell(new Paragraph(
                    "Applicable Taxes", small_bold2));

            PdfPCell BI_Pdftable_output_cell78 = new PdfPCell(new Paragraph(
                    "Policy Admin charge", small_bold2));

            PdfPCell BI_Pdftable_output_cell781 = new PdfPCell(new Paragraph(
                    "Other charges*", small_bold2));
            PdfPCell BI_Pdftable_output_cell88 = new PdfPCell(new Paragraph(
                    "Additions to the fund*", small_bold2));
            PdfPCell BI_Pdftable_output_cell881 = new PdfPCell(new Paragraph(
                    "Guaranteed Addition", small_bold2));

            PdfPCell BI_Pdftable_output_cell98 = new PdfPCell(new Paragraph(
                    "Fund before FMC", small_bold2));

            PdfPCell BI_Pdftable_output_cell108 = new PdfPCell(new Paragraph(
                    "FMC", small_bold2));
            PdfPCell BI_Pdftable_output_cell118 = new PdfPCell(new Paragraph(
                    "Fund at End of year", small_bold2));

            PdfPCell BI_Pdftable_output_cell11833 = new PdfPCell(new Paragraph(
                    "Return of Moratlity Charge", small_bold2));
            PdfPCell BI_Pdftable_output_cell128 = new PdfPCell(new Paragraph(
                    "Surrender Value", small_bold2));

            PdfPCell BI_Pdftable_output_cell138 = new PdfPCell(new Paragraph(
                    "Death benefit", small_bold2));


            BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell18);
            BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell28);
            BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell38);

            BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell48);
            BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell58);
            BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell68);

            BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell78);
            BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell781);
            BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell88);
            BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell881);
            BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell98);

            BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell108);
            BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell118);
            BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell11833);
            BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell128);

            BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell138);

            document.add(BI_Pdftableoutput8);

            PdfPTable BI_Pdftableoutput_no8 = new PdfPTable(15);
            BI_Pdftableoutput_no8.setWidthPercentage(100);

            PdfPCell BI_Pdftable_output_no_cell18 = new PdfPCell(new Paragraph(
                    "1", small_bold2));
            PdfPCell BI_Pdftable_output_no_cell28 = new PdfPCell(new Paragraph(
                    "2", small_bold2));

            PdfPCell BI_Pdftable_output_no_cell38 = new PdfPCell(new Paragraph(
                    "3", small_bold2));

            PdfPCell BI_Pdftable_output_no_cell48 = new PdfPCell(new Paragraph(
                    "4", small_bold2));
            PdfPCell BI_Pdftable_output_no_cell58 = new PdfPCell(new Paragraph(
                    "5", small_bold2));

            PdfPCell BI_Pdftable_output_no_cell68 = new PdfPCell(new Paragraph(
                    "6", small_bold2));

            PdfPCell BI_Pdftable_output_no_cell78 = new PdfPCell(new Paragraph(
                    "7", small_bold2));
            PdfPCell BI_Pdftable_output_no_cell88 = new PdfPCell(new Paragraph(
                    "8", small_bold2));

            PdfPCell BI_Pdftable_output_no_cell98 = new PdfPCell(new Paragraph(
                    "9", small_bold2));

            PdfPCell BI_Pdftable_output_no_cell108 = new PdfPCell(new Paragraph(
                    "10", small_bold2));
            PdfPCell BI_Pdftable_output_no_cell118 = new PdfPCell(new Paragraph(
                    "11", small_bold2));

            PdfPCell BI_Pdftable_output_no_cell128 = new PdfPCell(new Paragraph(
                    "12", small_bold2));

            PdfPCell BI_Pdftable_output_no_cell138 = new PdfPCell(new Paragraph(
                    "13", small_bold2));
            PdfPCell BI_Pdftable_output_no_cell148 = new PdfPCell(new Paragraph(
                    "14", small_bold2));


            PdfPCell BI_Pdftable_output_no_cell248 = new PdfPCell(new Paragraph(
                    "15", small_bold2));

            BI_Pdftableoutput_no8.addCell(BI_Pdftable_output_no_cell18);
            BI_Pdftableoutput_no8.addCell(BI_Pdftable_output_no_cell28);
            BI_Pdftableoutput_no8.addCell(BI_Pdftable_output_no_cell38);

            BI_Pdftableoutput_no8.addCell(BI_Pdftable_output_no_cell48);
            BI_Pdftableoutput_no8.addCell(BI_Pdftable_output_no_cell58);
            BI_Pdftableoutput_no8.addCell(BI_Pdftable_output_no_cell68);

            BI_Pdftableoutput_no8.addCell(BI_Pdftable_output_no_cell78);
            BI_Pdftableoutput_no8.addCell(BI_Pdftable_output_no_cell88);
            BI_Pdftableoutput_no8.addCell(BI_Pdftable_output_no_cell98);

            BI_Pdftableoutput_no8.addCell(BI_Pdftable_output_no_cell108);
            BI_Pdftableoutput_no8.addCell(BI_Pdftable_output_no_cell118);
            BI_Pdftableoutput_no8.addCell(BI_Pdftable_output_no_cell128);

            BI_Pdftableoutput_no8.addCell(BI_Pdftable_output_no_cell138);
            BI_Pdftableoutput_no8.addCell(BI_Pdftable_output_no_cell148);

            BI_Pdftableoutput_no8.addCell(BI_Pdftable_output_no_cell248);
            // document.add(BI_Pdftableoutput_no8);

            for (int i = 0; i < Integer.parseInt(policyTerm); i++) {

                PdfPTable BI_Pdftableoutput_row18 = new PdfPTable(16);
                BI_Pdftableoutput_row18.setWidthPercentage(100);

                PdfPCell BI_Pdftable_output_row1_cell18 = new PdfPCell(
                        new Paragraph(list_data1.get(i).getPolicy_year(),
                                small_bold2));
                PdfPCell BI_Pdftable_output_row1_cell28 = new PdfPCell(
                        new Paragraph(list_data1.get(i).getPremium(),
                                small_bold2));

                PdfPCell BI_Pdftable_output_row1_cell38 = new PdfPCell(
                        new Paragraph(list_data1.get(i)
                                .getPremium_allocation_charge(), small_bold2));

                PdfPCell BI_Pdftable_output_row1_cell48 = new PdfPCell(
                        new Paragraph(list_data1.get(i)
                                .getAnnulized_premium_allocation_charge(), small_bold2));


                PdfPCell BI_Pdftable_output_row1_cell68 = new PdfPCell(
                        new Paragraph(list_data1.get(i).getMortality_charge1(),
                                small_bold2));

                PdfPCell BI_Pdftable_output_row1_cell88 = new PdfPCell(
                        new Paragraph(list_data1.get(i)
                                .getService_tax_on_mortality_charge1(),
                                small_bold2));

                PdfPCell BI_Pdftable_output_row1_cell58 = new PdfPCell(
                        new Paragraph(list_data1.get(i)
                                .getPolicy_administration_charge(), small_bold2));

                PdfPCell BI_Pdftable_output_row1_cell118 = new PdfPCell(
                        new Paragraph(
                                list_data1.get(i).getGuranteed_addition1(),
                                small_bold2));

                PdfPCell BI_Pdftable_output_row1_cell78 = new PdfPCell(
                        new Paragraph(list_data1.get(i).getTotal_charge1(),
                                small_bold2));

                PdfPCell BI_Pdftable_output_row1_cell881 = new PdfPCell(
                        new Paragraph(list_data1.get(i)
                                .getStr_AddToTheFund4Pr(),
                                small_bold2));

                PdfPCell BI_Pdftable_output_row1_cell98 = new PdfPCell(
                        new Paragraph(list_data1.get(i).getFund_before_fmc1(),
                                small_bold2));

                PdfPCell BI_Pdftable_output_row1_cell108 = new PdfPCell(
                        new Paragraph(list_data1.get(i)
                                .getFund_management_charge1(), small_bold2));


                PdfPCell BI_Pdftable_output_row1_cell128 = new PdfPCell(
                        new Paragraph(list_data1.get(i).getFund_value_at_end1(),
                                small_bold2));


                PdfPCell BI_Pdftable_output_row1_cell12833 = new PdfPCell(
                        new Paragraph(list_data1.get(i).getTv_return_of_moratlity_charge1(),
                                small_bold2));
                PdfPCell BI_Pdftable_output_row1_cell138 = new PdfPCell(
                        new Paragraph(list_data1.get(i).getSurrender_value1(),
                                small_bold2));

                PdfPCell BI_Pdftable_output_row1_cell148 = new PdfPCell(
                        new Paragraph(list_data1.get(i).getDeath_benefit1(),
                                small_bold2));


                BI_Pdftableoutput_row18.addCell(BI_Pdftable_output_row1_cell18);
                BI_Pdftableoutput_row18.addCell(BI_Pdftable_output_row1_cell28);
                BI_Pdftableoutput_row18.addCell(BI_Pdftable_output_row1_cell38);
                BI_Pdftableoutput_row18.addCell(BI_Pdftable_output_row1_cell48);
                BI_Pdftableoutput_row18.addCell(BI_Pdftable_output_row1_cell68);
                BI_Pdftableoutput_row18.addCell(BI_Pdftable_output_row1_cell88);
                BI_Pdftableoutput_row18.addCell(BI_Pdftable_output_row1_cell58);
                BI_Pdftableoutput_row18.addCell(BI_Pdftable_output_row1_cell118);
                BI_Pdftableoutput_row18.addCell(BI_Pdftable_output_row1_cell78);
                BI_Pdftableoutput_row18.addCell(BI_Pdftable_output_row1_cell881);

                BI_Pdftableoutput_row18.addCell(BI_Pdftable_output_row1_cell98);

                BI_Pdftableoutput_row18.addCell(BI_Pdftable_output_row1_cell108);
                BI_Pdftableoutput_row18.addCell(BI_Pdftable_output_row1_cell128);
                BI_Pdftableoutput_row18.addCell(BI_Pdftable_output_row1_cell12833);

                BI_Pdftableoutput_row18.addCell(BI_Pdftable_output_row1_cell138);
                BI_Pdftableoutput_row18.addCell(BI_Pdftable_output_row1_cell148);

                document.add(BI_Pdftableoutput_row18);

            }

            PdfPTable BI_PdftableOutputHeader4 = new PdfPTable(3);
            BI_PdftableOutputHeader4.setWidthPercentage(100);
            float[] columnWidths84 = {2f, 6f, 7f};
            BI_PdftableOutputHeader4.setWidths(columnWidths84);
            PdfPCell BI_PdftableBI_PdftableOutputHeader_cell14 = new PdfPCell(
                    new Paragraph("Amount in  Rs.", small_bold2));
            PdfPCell BI_PdftableBI_PdftableOutputHeader_cell24 = new PdfPCell(
                    new Paragraph("Gross Yield 4% pa",
                            small_bold2));


            PdfPCell BI_PdftableBI_PdftableOutputHeader_cell44 = new PdfPCell(
                    new Paragraph("", small_bold2));

            BI_PdftableBI_PdftableOutputHeader_cell14
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableBI_PdftableOutputHeader_cell14.setPadding(5);

            BI_PdftableBI_PdftableOutputHeader_cell24
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableBI_PdftableOutputHeader_cell24.setPadding(5);


            BI_PdftableBI_PdftableOutputHeader_cell44
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableBI_PdftableOutputHeader_cell44.setPadding(5);

            BI_PdftableOutputHeader4
                    .addCell(BI_PdftableBI_PdftableOutputHeader_cell14);
            BI_PdftableOutputHeader4
                    .addCell(BI_PdftableBI_PdftableOutputHeader_cell24);

            BI_PdftableOutputHeader4
                    .addCell(BI_PdftableBI_PdftableOutputHeader_cell44);

            document.add(BI_PdftableOutputHeader4);

            PdfPTable BI_Pdftableoutput4 = new PdfPTable(16);
            BI_Pdftableoutput4.setWidthPercentage(100);

            PdfPCell BI_Pdftable_output_cell181 = new PdfPCell(new Paragraph(
                    "Policy Year", small_bold2));
            PdfPCell BI_Pdftable_output_cell281 = new PdfPCell(new Paragraph(
                    "Annualized Premium  (AP) ", small_bold2));

            PdfPCell BI_Pdftable_output_cell381 = new PdfPCell(new Paragraph(
                    "Premium Allocation Charge (PAC)", small_bold2));

            PdfPCell BI_Pdftable_output_cell481 = new PdfPCell(new Paragraph(
                    "Annualized Premium - Premium Allocation Charge",
                    small_bold2));
            PdfPCell BI_Pdftable_output_cell581 = new PdfPCell(new Paragraph(
                    "Mortality charge", small_bold2));

            PdfPCell BI_Pdftable_output_cell681 = new PdfPCell(new Paragraph(
                    "Applicable Taxes", small_bold2));

            PdfPCell BI_Pdftable_output_cell7814 = new PdfPCell(new Paragraph(
                    "Policy Admin charge", small_bold2));

            PdfPCell BI_Pdftable_output_cell7811 = new PdfPCell(new Paragraph(
                    "Other charges*", small_bold2));
            PdfPCell BI_Pdftable_output_cell8814 = new PdfPCell(new Paragraph(
                    "Additions to the fund*", small_bold2));
            PdfPCell BI_Pdftable_output_cell8811 = new PdfPCell(new Paragraph(
                    "Guaranteed Addition", small_bold2));

            PdfPCell BI_Pdftable_output_cell981 = new PdfPCell(new Paragraph(
                    "Fund before FMC", small_bold2));

            PdfPCell BI_Pdftable_output_cell1081 = new PdfPCell(new Paragraph(
                    "FMC", small_bold2));
            PdfPCell BI_Pdftable_output_cell1181 = new PdfPCell(new Paragraph(
                    "Fund at End of year", small_bold2));

            PdfPCell BI_Pdftable_output_cell118144 = new PdfPCell(new Paragraph(
                    "Return of Moratlity Charge", small_bold2));

            PdfPCell BI_Pdftable_output_cell1281 = new PdfPCell(new Paragraph(
                    "Surrender Value", small_bold2));

            PdfPCell BI_Pdftable_output_cell1381 = new PdfPCell(new Paragraph(
                    "Death benefit", small_bold2));


            BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell181);
            BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell281);
            BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell381);

            BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell481);
            BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell581);
            BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell681);

            BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell7814);
            BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell7811);
            BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell8814);
            BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell8811);
            BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell981);

            BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell1081);
            BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell1181);
            BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell118144);

            BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell1281);

            BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell1381);

            document.add(BI_Pdftableoutput4);

            PdfPTable BI_Pdftableoutput_no4 = new PdfPTable(15);
            BI_Pdftableoutput_no4.setWidthPercentage(100);

            PdfPCell BI_Pdftable_output_no_cell181 = new PdfPCell(new Paragraph(
                    "1", small_bold2));
            PdfPCell BI_Pdftable_output_no_cell281 = new PdfPCell(new Paragraph(
                    "2", small_bold2));

            PdfPCell BI_Pdftable_output_no_cell381 = new PdfPCell(new Paragraph(
                    "3", small_bold2));

            PdfPCell BI_Pdftable_output_no_cell481 = new PdfPCell(new Paragraph(
                    "4", small_bold2));
            PdfPCell BI_Pdftable_output_no_cell581 = new PdfPCell(new Paragraph(
                    "5", small_bold2));

            PdfPCell BI_Pdftable_output_no_cell681 = new PdfPCell(new Paragraph(
                    "6", small_bold2));

            PdfPCell BI_Pdftable_output_no_cell781 = new PdfPCell(new Paragraph(
                    "7", small_bold2));
            PdfPCell BI_Pdftable_output_no_cell881 = new PdfPCell(new Paragraph(
                    "8", small_bold2));

            PdfPCell BI_Pdftable_output_no_cell981 = new PdfPCell(new Paragraph(
                    "9", small_bold2));

            PdfPCell BI_Pdftable_output_no_cell1081 = new PdfPCell(new Paragraph(
                    "10", small_bold2));
            PdfPCell BI_Pdftable_output_no_cell1181 = new PdfPCell(new Paragraph(
                    "11", small_bold2));

            PdfPCell BI_Pdftable_output_no_cell1281 = new PdfPCell(new Paragraph(
                    "12", small_bold2));

            PdfPCell BI_Pdftable_output_no_cell1381 = new PdfPCell(new Paragraph(
                    "13", small_bold2));
            PdfPCell BI_Pdftable_output_no_cell1481 = new PdfPCell(new Paragraph(
                    "14", small_bold2));


            PdfPCell BI_Pdftable_output_no_cell2481 = new PdfPCell(new Paragraph(
                    "15", small_bold2));

            BI_Pdftableoutput_no4.addCell(BI_Pdftable_output_no_cell181);
            BI_Pdftableoutput_no4.addCell(BI_Pdftable_output_no_cell281);
            BI_Pdftableoutput_no4.addCell(BI_Pdftable_output_no_cell381);

            BI_Pdftableoutput_no4.addCell(BI_Pdftable_output_no_cell481);
            BI_Pdftableoutput_no4.addCell(BI_Pdftable_output_no_cell581);
            BI_Pdftableoutput_no4.addCell(BI_Pdftable_output_no_cell681);

            BI_Pdftableoutput_no4.addCell(BI_Pdftable_output_no_cell781);
            BI_Pdftableoutput_no4.addCell(BI_Pdftable_output_no_cell881);
            BI_Pdftableoutput_no4.addCell(BI_Pdftable_output_no_cell981);

            BI_Pdftableoutput_no4.addCell(BI_Pdftable_output_no_cell1081);
            BI_Pdftableoutput_no4.addCell(BI_Pdftable_output_no_cell1181);
            BI_Pdftableoutput_no4.addCell(BI_Pdftable_output_no_cell1281);

            BI_Pdftableoutput_no4.addCell(BI_Pdftable_output_no_cell1381);
            BI_Pdftableoutput_no4.addCell(BI_Pdftable_output_no_cell1481);

            BI_Pdftableoutput_no4.addCell(BI_Pdftable_output_no_cell2481);
            // document.add(BI_Pdftableoutput_no4);

            for (int i = 0; i < Integer.parseInt(policyTerm); i++) {

                PdfPTable BI_Pdftableoutput_row184 = new PdfPTable(16);
                BI_Pdftableoutput_row184.setWidthPercentage(100);

                PdfPCell BI_Pdftable_output_row1_cell181 = new PdfPCell(
                        new Paragraph(list_data2.get(i).getPolicy_year(),
                                small_bold2));
                PdfPCell BI_Pdftable_output_row1_cell281 = new PdfPCell(
                        new Paragraph(list_data2.get(i).getPremium(),
                                small_bold2));

                PdfPCell BI_Pdftable_output_row1_cell381 = new PdfPCell(
                        new Paragraph(list_data2.get(i)
                                .getPremium_allocation_charge(), small_bold2));

                PdfPCell BI_Pdftable_output_row1_cell481 = new PdfPCell(
                        new Paragraph(list_data2.get(i)
                                .getAnnulized_premium_allocation_charge(), small_bold2));


                PdfPCell BI_Pdftable_output_row1_cell681 = new PdfPCell(
                        new Paragraph(list_data2.get(i).getMortality_charge1(),
                                small_bold2));

                PdfPCell BI_Pdftable_output_row1_cell881 = new PdfPCell(
                        new Paragraph(list_data2.get(i)
                                .getService_tax_on_mortality_charge1(),
                                small_bold2));

                PdfPCell BI_Pdftable_output_row1_cell581 = new PdfPCell(
                        new Paragraph(list_data2.get(i)
                                .getPolicy_administration_charge(), small_bold2));

                PdfPCell BI_Pdftable_output_row1_cell1181 = new PdfPCell(
                        new Paragraph(
                                list_data2.get(i).getGuranteed_addition1(),
                                small_bold2));

                PdfPCell BI_Pdftable_output_row1_cell781 = new PdfPCell(
                        new Paragraph(list_data2.get(i).getTotal_charge1(),
                                small_bold2));

                PdfPCell BI_Pdftable_output_row1_cell8811 = new PdfPCell(
                        new Paragraph(list_data2.get(i)
                                .getStr_AddToTheFund4Pr(),
                                small_bold2));

                PdfPCell BI_Pdftable_output_row1_cell981 = new PdfPCell(
                        new Paragraph(list_data2.get(i).getFund_before_fmc1(),
                                small_bold2));

                PdfPCell BI_Pdftable_output_row1_cell1081 = new PdfPCell(
                        new Paragraph(list_data2.get(i)
                                .getFund_management_charge1(), small_bold2));


                PdfPCell BI_Pdftable_output_row1_cell1281 = new PdfPCell(
                        new Paragraph(list_data2.get(i).getFund_value_at_end1(),
                                small_bold2));

                PdfPCell BI_Pdftable_output_row1_cell128144 = new PdfPCell(
                        new Paragraph(list_data2.get(i).getTv_return_of_moratlity_charge1(),
                                small_bold2));

                PdfPCell BI_Pdftable_output_row1_cell1381 = new PdfPCell(
                        new Paragraph(list_data2.get(i).getSurrender_value1(),
                                small_bold2));

                PdfPCell BI_Pdftable_output_row1_cell1481 = new PdfPCell(
                        new Paragraph(list_data2.get(i).getDeath_benefit1(),
                                small_bold2));


                BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell181);
                BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell281);
                BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell381);
                BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell481);
                BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell681);
                BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell881);
                BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell581);
                BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell1181);
                BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell781);
                BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell8811);

                BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell981);

                BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell1081);
                BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell1281);
                BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell128144);


                BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell1381);
                BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell1481);

                document.add(BI_Pdftableoutput_row184);

            }

            PdfPTable BI_PdftableNetYield1 = new PdfPTable(4);

            BI_PdftableNetYield1.setWidthPercentage(100);
            float[] columnWidthsNetYield1 = {5f, 9f, 9f, 1f};
            BI_PdftableNetYield1.setWidths(columnWidthsNetYield1);
            PdfPCell BI_PdftableBI_PdftableNetYield_cell11 = new PdfPCell(
                    new Paragraph("", small_normal));
            PdfPCell BI_PdftableBI_PdftableNetYield_cell21 = new PdfPCell(
                    new Paragraph("Net Yield -  " + netYield8pa + " %", small_bold2));

            PdfPCell BI_PdftableBI_PdftableNetYield_cell31 = new PdfPCell(
                    new Paragraph("Reduction in Yield -  " + redInYieldNoYr + " %",
                            small_bold2));

            PdfPCell BI_PdftableBI_PdftableNetYield_cell41 = new PdfPCell(
                    new Paragraph("", small_bold2));

            BI_PdftableBI_PdftableNetYield_cell11
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableBI_PdftableNetYield_cell11.setPadding(5);

            BI_PdftableBI_PdftableNetYield_cell21
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableBI_PdftableNetYield_cell21.setPadding(5);

            BI_PdftableBI_PdftableNetYield_cell31
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableBI_PdftableNetYield_cell31.setPadding(5);

            BI_PdftableBI_PdftableNetYield_cell41
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableBI_PdftableNetYield_cell41.setPadding(5);

            BI_PdftableNetYield1.addCell(BI_PdftableBI_PdftableNetYield_cell11);
            BI_PdftableNetYield1.addCell(BI_PdftableBI_PdftableNetYield_cell21);
            BI_PdftableNetYield1.addCell(BI_PdftableBI_PdftableNetYield_cell31);
            BI_PdftableNetYield1.addCell(BI_PdftableBI_PdftableNetYield_cell41);

            //document.add(BI_PdftableNetYield1);


            //2nd Grid End

            document.add(para_img_logo_after_space_1);
            PdfPTable BI_Pdftable_note = new PdfPTable(1);
            BI_Pdftable_note.setWidthPercentage(100);
            PdfPCell BI_Pdftable_note_cell1 = new PdfPCell(new Paragraph(
                    "Notes", small_bold));
            BI_Pdftable_note_cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_Pdftable_note_cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
            BI_Pdftable_note_cell1.setPadding(5);

            BI_Pdftable_note.addCell(BI_Pdftable_note_cell1);
            document.add(BI_Pdftable_note);

            PdfPTable BI_Pdftable8 = new PdfPTable(1);
            BI_Pdftable8.setWidthPercentage(100);
            PdfPCell BI_Pdftable8_cell1 = new PdfPCell(
                    new Paragraph(
                            "1). Refer the sales literature for explanation of terms used in this illustration",

                            small_normal));

            BI_Pdftable8_cell1.setPadding(5);

            BI_Pdftable8.addCell(BI_Pdftable8_cell1);
            document.add(BI_Pdftable8);


            PdfPTable BI_Pdftable10 = new PdfPTable(1);
            BI_Pdftable10.setWidthPercentage(100);
            PdfPCell BI_Pdftable10_cell1 = new PdfPCell(
                    new Paragraph(
                            "2) Please read this benefit illustration in conjunction with Sales Brochure and the Policy Document to understand all Terms, Conditions & Exclusions carefully.",
                            small_normal));

            BI_Pdftable10_cell1.setPadding(5);

            BI_Pdftable10.addCell(BI_Pdftable10_cell1);
            document.add(BI_Pdftable10);

            PdfPTable BI_Pdftable11 = new PdfPTable(1);
            BI_Pdftable11.setWidthPercentage(100);
            PdfPCell BI_Pdftable11_cell1 = new PdfPCell(
                    new Paragraph(
                            "3) Kindly note that above is only an illustration and does not in any way create any rights and/or obligations. The actual experience on the contract may be different from what is illustrated. The non-guaranteed low and high rate mentioned above relate to assumed investment returns at different rates and may vary depending upon market conditions. For more details on risk factors, terms and conditions please read sales brochure carefully.",
                            small_normal));

            BI_Pdftable11_cell1.setPadding(5);

            BI_Pdftable11.addCell(BI_Pdftable11_cell1);
            document.add(BI_Pdftable11);


            PdfPTable BI_Pdftable13 = new PdfPTable(1);
            BI_Pdftable13.setWidthPercentage(100);
            PdfPCell BI_Pdftable13_cell1 = new PdfPCell(
                    new Paragraph(
                            "4) The unit values may go up as well as down and past performance is no indication of future performance on the part of SBI Life Insurance Co. Ltd. We would request you to appreciate the associated risk under this plan vis-à-vis the likely future returns before taking your investment decision.",
                            small_normal));

            BI_Pdftable13_cell1.setPadding(5);

            BI_Pdftable13.addCell(BI_Pdftable13_cell1);
            document.add(BI_Pdftable13);


            PdfPTable BI_Pdftable14 = new PdfPTable(1);
            BI_Pdftable14.setWidthPercentage(100);
            PdfPCell BI_Pdftable14_cell1 = new PdfPCell(
                    new Paragraph(
                            "5) It is assumed that the policy is in force throughout the term.",

                            small_normal));

            BI_Pdftable14_cell1.setPadding(5);

            BI_Pdftable14.addCell(BI_Pdftable14_cell1);
            document.add(BI_Pdftable14);

            PdfPTable point6 = new PdfPTable(1);
            point6.setWidthPercentage(100);
            PdfPCell point6_1 = new PdfPCell(
                    new Paragraph(
                            "6) Fund management charge is based on the specific  investment strategy / fund option(s) chosen.",

                            small_normal));

            point6_1.setPadding(5);

            point6.addCell(point6_1);
            document.add(point6);

            PdfPTable point7 = new PdfPTable(1);
            point7.setWidthPercentage(100);
            PdfPCell point7_1 = new PdfPCell(
                    new Paragraph(
                            "7) Surrender Value equals the Fund Value at the end of the year minus Discontinuance Charges. Surrender value is available on or after 5th policy anniversary.",

                            small_normal));

            point7_1.setPadding(5);

            point7.addCell(point7_1);
            document.add(point7);

            PdfPTable point8 = new PdfPTable(1);
            point8.setWidthPercentage(100);
            PdfPCell point8_1 = new PdfPCell(
                    new Paragraph(
                            "8) Acceptance of proposal is subject to Underwriting decision. Mortality charges are for a healthy person.",

                            small_normal));

            point8_1.setPadding(5);

            point8.addCell(point8_1);
            document.add(point8);

            PdfPTable point9 = new PdfPTable(1);
            point9.setWidthPercentage(100);
            PdfPCell point9_1 = new PdfPCell(
                    new Paragraph(
                            "9) Applicable Taxes (including surcharge/cess etc), at the rate notified by the Central Government/ State Government / Union Territories of India from time to time and as per the provisions of the prevalent tax laws will be payable on premium/ or any other charges as per the product features.",

                            small_normal));

            point9_1.setPadding(5);

            point9.addCell(point9_1);
            document.add(point9);
            PdfPTable BI_Pdftable_TAX_FMC = new PdfPTable(1);
            BI_Pdftable_TAX_FMC.setWidthPercentage(100);
            PdfPCell BI_Pdftable_TAX_FMC_cell1 = new PdfPCell(new Paragraph(
                    "10) This policy provides guaranteed death benefit of Rs."
                            + obj.getRound(obj.getStringWithout_E(Double
                            .valueOf(sumAssured.equals("") ? "0"
                                    : sumAssured))),

                    small_normal));

            BI_Pdftable_TAX_FMC_cell1.setPadding(5);

            BI_Pdftable_TAX_FMC.addCell(BI_Pdftable_TAX_FMC_cell1);
            document.add(BI_Pdftable_TAX_FMC);

            PdfPTable point11 = new PdfPTable(1);
            point11.setWidthPercentage(100);
            PdfPCell point11_1 = new PdfPCell(
                    new Paragraph(
                            "11) Net Yield have been calculated after applying all the charges (except GST, mortality charges).",

                            small_normal));

            point11_1.setPadding(5);

            point11.addCell(point11_1);
            document.add(point11);

            document.add(para_img_logo_after_space_1);
            PdfPTable BI_Pdftable19 = new PdfPTable(1);
            BI_Pdftable19.setWidthPercentage(100);
            PdfPCell BI_Pdftable19_cell1 = new PdfPCell(new Paragraph(
                    "Definition of Various Charges", small_bold));
            BI_Pdftable19_cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);

            BI_Pdftable19_cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_Pdftable19_cell1.setPadding(5);

            BI_Pdftable19.addCell(BI_Pdftable19_cell1);
            document.add(BI_Pdftable19);

            PdfPTable BI_Pdftable20 = new PdfPTable(1);
            BI_Pdftable20.setWidthPercentage(100);
            PdfPCell BI_Pdftable20_cell1 = new PdfPCell(
                    new Paragraph(
                            "1)Policy Administration Charges : a charge of a fixed sum which is applied at the beginning of each policy month by cancelling units for equivalent amount, deducted for maintaining the policy.",
                            small_normal));

            BI_Pdftable20_cell1.setPadding(5);

            BI_Pdftable20.addCell(BI_Pdftable20_cell1);
            document.add(BI_Pdftable20);

            PdfPTable BI_Pdftable21 = new PdfPTable(1);
            BI_Pdftable21.setWidthPercentage(100);
            PdfPCell BI_Pdftable21_cell1 = new PdfPCell(
                    new Paragraph(
                            "2)Premium Allocation Charge : is the percentage of premium that would not be utilised to purchase units.",
                            small_normal));

            BI_Pdftable21_cell1.setPadding(5);

            BI_Pdftable21.addCell(BI_Pdftable21_cell1);
            document.add(BI_Pdftable21);

            PdfPTable BI_Pdftable22 = new PdfPTable(1);
            BI_Pdftable22.setWidthPercentage(100);
            PdfPCell BI_Pdftable22_cell1 = new PdfPCell(
                    new Paragraph(
                            "3)Mortality Charges : are the charges recovered for providing life insurance cover, deducted at the beginning of each policy month by cancelling units for equivalent amount.",
                            small_normal));

            BI_Pdftable22_cell1.setPadding(5);

            BI_Pdftable22.addCell(BI_Pdftable22_cell1);
            document.add(BI_Pdftable22);

            PdfPTable BI_Pdftable23 = new PdfPTable(1);
            BI_Pdftable23.setWidthPercentage(100);
            PdfPCell BI_Pdftable23_cell1 = new PdfPCell(
                    new Paragraph(
                            "4)Fund Management Charge (FMC) : is the deduction made from the fund at a stated percentage before the computation of the NAV of the fund.",
                            small_normal));

            BI_Pdftable23_cell1.setPadding(5);

            BI_Pdftable23.addCell(BI_Pdftable23_cell1);
            document.add(BI_Pdftable23);
            document.add(para_img_logo_after_space_1);

            PdfPTable BI_Pdftable_CompanysPolicySurrender2 = new PdfPTable(1);
            BI_Pdftable_CompanysPolicySurrender2.setWidthPercentage(100);

            PdfPCell imp = new PdfPCell(
                    new Paragraph(
                            "Important : ",
                            small_normal));
            imp.setBackgroundColor(BaseColor.LIGHT_GRAY);

            PdfPCell BI_Pdftable_CompanysPolicySurrender2_cell = new PdfPCell(
                    new Paragraph(
                            "You may receive a Welcome Call from our representative to confirm your proposal details like Date of Birth, Nominee Name, Address, Email ID, Sum Assured, Premium amount, Premium Payment Term etc.",
                            small_normal));

            BI_Pdftable_CompanysPolicySurrender2_cell.setPadding(5);
            BI_Pdftable_CompanysPolicySurrender2
                    .addCell(imp);
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

            PdfPTable BI_Pdftable_CompanysPolicySurrender4 = new PdfPTable(1);
            BI_Pdftable_CompanysPolicySurrender4.setWidthPercentage(100);
            PdfPCell BI_Pdftable_CompanysPolicySurrender4_cell = new PdfPCell(
                    new Paragraph(
                            "If premium is greater than Rs 1Lakh. You have to submit Proof of source of Fund.",
                            small_normal));

            BI_Pdftable_CompanysPolicySurrender4_cell.setPadding(5);

            BI_Pdftable_CompanysPolicySurrender4
                    .addCell(BI_Pdftable_CompanysPolicySurrender4_cell);
            if ((Double.parseDouble(premiumAmt.getText().toString()) * 12) > 100000) {
                document.add(BI_Pdftable_CompanysPolicySurrender4);
            }

            PdfPTable BI_Pdftable_CompanysPolicySurrender5 = new PdfPTable(1);
            BI_Pdftable_CompanysPolicySurrender5.setWidthPercentage(100);
            PdfPCell BI_Pdftable_CompanysPolicySurrender5_cell = new PdfPCell(
                    new Paragraph(Company_policy_surrender_dec, small_normal));

            BI_Pdftable_CompanysPolicySurrender5_cell.setPadding(5);

            BI_Pdftable_CompanysPolicySurrender5
                    .addCell(BI_Pdftable_CompanysPolicySurrender5_cell);
            document.add(BI_Pdftable_CompanysPolicySurrender5);

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
                                    + "   , have received the information with respect to the above and have understood the above statement before entering into a contract.",
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
            Log.i(getLocalClassName(), e.toString() + "Error in creating pdf");
        }
    }

    // Store user input in Bean object
    protected void addListenerOnSubmit() {
        smartinsurewealthplusBean = new SmartInsureWealthPlusBean();

        if (isStaffDisc.isChecked()) {
            smartinsurewealthplusBean.setIsForStaffOrNot(true);
        } else {
            smartinsurewealthplusBean.setIsForStaffOrNot(false);
        }

        if (cb_kerladisc.isChecked()) {
            smartinsurewealthplusBean.setKerlaDisc(true);
        } else {
            smartinsurewealthplusBean.setKerlaDisc(false);
        }

        if (is_trigger_fund.equalsIgnoreCase("true")) {
            smartinsurewealthplusBean.setTriggerStrategy(true);
        } else {
            smartinsurewealthplusBean.setTriggerStrategy(false);
        }

        if (auto_allocation.equalsIgnoreCase("true")) {
            smartinsurewealthplusBean.setAutoAssetAllocationStrategy(true);
        } else {
            smartinsurewealthplusBean.setAutoAssetAllocationStrategy(false);
        }

        smartinsurewealthplusBean.setAgeAtEntry(Integer.parseInt(ageInYears.getSelectedItem().toString()));
        smartinsurewealthplusBean.setGender(gender);
        smartinsurewealthplusBean.setProposer_gender(gender_proposer);
        smartinsurewealthplusBean.setPlanType(selPlan.getSelectedItem().toString());
        smartinsurewealthplusBean.setPremFreq(selPremiumFrequencyMode.getSelectedItem().toString());

        if (Integer.parseInt(ageInYears.getSelectedItem().toString()) < 18) {
            smartinsurewealthplusBean.setAgeOfProposer(Integer
                    .parseInt(textviewProposerAge.getText().toString()));
        } else {
            smartinsurewealthplusBean.setAgeOfProposer(0);
        }

        smartinsurewealthplusBean.setPolicyTerm_Basic(Integer.parseInt(selPolicyTerm.getSelectedItem().toString()));

        smartinsurewealthplusBean.setPremiumPayingTerm(Integer.parseInt(selPolicyTerm.getSelectedItem().toString()));

        smartinsurewealthplusBean.setPremiumAmount(Double.parseDouble(premiumAmt.getText().toString()));

        /*if (Integer.parseInt(selPolicyTerm.getSelectedItem().toString()) <= 20) {
            SAMF = 10;
        } else {
            SAMF = Integer.parseInt(selPolicyTerm.getSelectedItem().toString()) * 0.5;
        }*/

        SAMF = 10;
        smartinsurewealthplusBean.setSAMF(SAMF);

        smartinsurewealthplusBean.setSumAssured(Integer
                .parseInt(premiumAmt.getText().toString()) * 12 * SAMF);

        // System.out.println("********* 9 ***********");

        smartinsurewealthplusBean.setEffectivePremium(Double.parseDouble(getEffectivePremium(smartinsurewealthplusBean)));

        smartinsurewealthplusBean.setPF(12);

        if (is_trigger_fund.equalsIgnoreCase("true")) {
            smartinsurewealthplusBean.setPercentToBeInvested_EquityFund(80);

            smartinsurewealthplusBean.setCorpbondFund(20);
        } else if (is_trigger_fund.equalsIgnoreCase("false") && auto_allocation.equalsIgnoreCase("false")) {

            if (!percent_EquityFund.getText().toString().equals(""))
                smartinsurewealthplusBean.setPercentToBeInvested_EquityFund(Double.parseDouble(percent_EquityFund.getText().toString()));
            else
                smartinsurewealthplusBean.setPercentToBeInvested_EquityFund(0);

            if (!percent_EquityOptFund.getText().toString().equals(""))
                smartinsurewealthplusBean.setPercentToBeInvested_EquityOptFund(Double.parseDouble(percent_EquityOptFund.getText().toString()));
            else
                smartinsurewealthplusBean.setPercentToBeInvested_EquityOptFund(0);

            if (!percent_GrowthFund.getText().toString().equals(""))
                smartinsurewealthplusBean.setPercentToBeInvested_GrowthFund(Double.parseDouble(percent_GrowthFund.getText().toString()));
            else
                smartinsurewealthplusBean.setPercentToBeInvested_GrowthFund(0);

            if (!percent_BalancedFund.getText().toString().equals(""))
                smartinsurewealthplusBean.setPercentToBeInvested_BalancedFund(Double.parseDouble(percent_BalancedFund.getText().toString()));
            else
                smartinsurewealthplusBean.setPercentToBeInvested_BalancedFund(0);

            if (!percent_PureFund.getText().toString().equals(""))
                smartinsurewealthplusBean.setPercentToBeInvested_PureFund(Double.parseDouble(percent_PureFund.getText().toString()));
            else
                smartinsurewealthplusBean.setPercentToBeInvested_PureFund(0);

            if (!percent_MidCapFund.getText().toString().equals(""))
                smartinsurewealthplusBean.setPercentToBeInvested_MidCapFund(Double.parseDouble(percent_MidCapFund.getText().toString()));
            else
                smartinsurewealthplusBean.setPercentToBeInvested_MidCapFund(0);

            if (!percent_BondOptimiser.getText().toString().equals(""))
                smartinsurewealthplusBean.setPercentToBeInvested_BondOptimiserFund(Double.parseDouble(percent_BondOptimiser.getText().toString()));
            else
                smartinsurewealthplusBean.setPercentToBeInvested_BondOptimiserFund(0);

            if (!percent_CorpBondFund.getText().toString().equals(""))
                smartinsurewealthplusBean.setCorpbondFund(Double.parseDouble(percent_CorpBondFund.getText().toString()));
            else
                smartinsurewealthplusBean.setCorpbondFund(0);

            if (!percent_MoneyMktFund.getText().toString().equals(""))
                smartinsurewealthplusBean.setMoneyMarketFund(Double.parseDouble(percent_MoneyMktFund.getText().toString()));
            else
                smartinsurewealthplusBean.setMoneyMarketFund(0);
        }

        // Show Smart InsureWealth Plus Output Screen
        showSmartInsureWealthPlusOutputPg(smartinsurewealthplusBean);
    }

    /******************************************* calculation part starts here ******************************************/

    public void showSmartInsureWealthPlusOutputPg(
            SmartInsureWealthPlusBean smartinsurewealthplusBean) {
        try {
            retVal = new StringBuilder();


            if (isStaffDisc.isChecked())
                staffStatus = "sbi";
            else
                staffStatus = "none";


            if (smartinsurewealthplusBean.getPlanType().equals("Regular")) {
                mode = "Regular";
            }


            String[] outputArr = getOutput("BI_Incl_Mort & Ser Tax",
                    smartinsurewealthplusBean);
            /*String[] redinYieldArr = getOutputReductionInYield(
                    "Benefit Illustrator_CAP", smartinsurewealthplusBean);*/
            /** <netYield8pa> added by Akshaya on 18-Feb-2014 **/

            retVal.append("<?xml version='1.0' encoding='utf-8' ?><SmartInsureWealthPlus>");
            retVal.append("<staffStatus>" + staffStatus + "</staffStatus>");
            retVal.append("<staffRebate>" + discountPercentage + "</staffRebate>");
            retVal.append("<errCode>0</errCode>"
                    + "<maturityAge>" + (smartinsurewealthplusBean.getAgeAtEntry() + smartinsurewealthplusBean.getPolicyTerm_Basic()) + "</maturityAge>"
                    + "<annPrem>" + smartinsurewealthplusBean.getEffectivePremium() + "</annPrem>"
                    + "<premPayTerm>" + smartinsurewealthplusBean.getPremiumPayingTerm() + "</premPayTerm>"
                    + "<mode>" + mode + "</mode>"
                    + "<redInYieldMat>" + outputArr[0] + "</redInYieldMat>"
                    + "<redInYieldNoYr>" + outputArr[0] + "</redInYieldNoYr>"
                    + "<netYield8pa>" + outputArr[1] + "</netYield8pa>"
                    + "<sumAssured>" + outputArr[5] + "</sumAssured>"
                    + "<fundVal4>" + outputArr[3] + "</fundVal4>"
                    + "<fundVal8>" + outputArr[4] + "</fundVal8>"
                    + bussIll.toString());

            retVal.append("</SmartInsureWealthPlus>");

        } catch (Exception e) {
            retVal.append("<?xml version='1.0' encoding='utf-8' ?><SmartInsureWealthPlus>"
                    + "<errCode>1</errCode>"
                    + "<errorMessage>"
                    + e.getMessage() + "</errorMessage></SmartInsureWealthPlus>");
        }

        System.out.println("Final output in xml" + retVal.toString());

    }


    public void getInput(SmartInsureWealthPlusBean smartinsurewealthplusBean) {

        inputVal = new StringBuilder();

        String LifeAssured_title = spnr_bi_smart_insure_wealth_plus_life_assured_title
                .getSelectedItem().toString();
        String LifeAssured_firstName = edt_bi_smart_insure_wealth_plus_life_assured_first_name
                .getText().toString();

        String LifeAssured_middleName = edt_bi_smart_insure_wealth_plus_life_assured_middle_name
                .getText().toString();

        String LifeAssured_lastName = edt_bi_smart_insure_wealth_plus_life_assured_last_name
                .getText().toString();

        String LifeAssured_DOB = btn_bi_smart_insure_wealth_plus_life_assured_date
                .getText().toString();
        String LifeAssured_age = ageInYears.getSelectedItem().toString();

        String proposer_DOB = "";
        String proposer_age = "";
        // String proposer_gender = "";

        // From GUI Input
        boolean staffDisc = smartinsurewealthplusBean.getIsForStaffOrNot();
        int policyTerm = smartinsurewealthplusBean.getPolicyTerm_Basic();
        int ageAtEntry = smartinsurewealthplusBean.getAgeAtEntry();
        plan = smartinsurewealthplusBean.getPlanType();
        int proposerAge = smartinsurewealthplusBean.getAgeOfProposer();
        double premiumAmount = smartinsurewealthplusBean.getPremiumAmount();
        boolean isTriggerFund = smartinsurewealthplusBean.getTriggerStrategy();
        boolean isAutoAllocation = smartinsurewealthplusBean.getAutoAssetAllocationStrategy();

        if (!btn_bi_smart_insure_wealth_plus_proposer_date.getText().toString()
                .equals("Select Date")) {
            Calendar present_date = Calendar.getInstance();
            int tDay = present_date.get(Calendar.DAY_OF_MONTH);
            int tMonth = present_date.get(Calendar.MONTH);
            int tYear = present_date.get(Calendar.YEAR);
            proposer_DOB = btn_bi_smart_insure_wealth_plus_proposer_date.getText()
                    .toString();
            proposer_age = commonForAllProd.calculateMyAge(tYear, tMonth + 1,
                    tDay, commonMethods.getMMDDYYYYDatabaseDate(proposer_DOB)) + "";
        }

        inputVal.append("<?xml version='1.0' encoding='utf-8' ?><SmartInsureWealthPlus>");
        inputVal.append("<LifeAssured_title>").append(LifeAssured_title).append("</LifeAssured_title>");
        inputVal.append("<LifeAssured_firstName>").append(LifeAssured_firstName).append("</LifeAssured_firstName>");
        inputVal.append("<LifeAssured_middleName>").append(LifeAssured_middleName).append("</LifeAssured_middleName>");
        inputVal.append("<LifeAssured_lastName>").append(LifeAssured_lastName).append("</LifeAssured_lastName>");
        inputVal.append("<LifeAssured_DOB>").append(LifeAssured_DOB).append("</LifeAssured_DOB>");
        inputVal.append("<LifeAssured_age>").append(LifeAssured_age).append("</LifeAssured_age>");

        inputVal.append("<proposer_title>").append(proposer_Title).append("</proposer_title>");
        inputVal.append("<proposer_firstName>").append(proposer_First_Name).append("</proposer_firstName>");
        inputVal.append("<proposer_middleName>").append(proposer_Middle_Name).append("</proposer_middleName>");
        inputVal.append("<proposer_lastName>").append(proposer_Last_Name).append("</proposer_lastName>");
        inputVal.append("<proposer_DOB>").append(proposer_DOB).append("</proposer_DOB>");
        inputVal.append("<proposer_age>").append(proposer_age).append("</proposer_age>");
        inputVal.append("<proposer_gender>").append(gender_proposer).append("</proposer_gender>");

        inputVal.append("<isStaff>" + staffDisc + "</isStaff>");
        inputVal.append("<age>" + ageInYears.getSelectedItem().toString() + "</age>");
        inputVal.append("<gender>" + gender + "</gender>");
        inputVal.append("<gender_proposer>" + gender_proposer + "</gender_proposer>");
        inputVal.append("<plan>" + plan + "</plan>");
        inputVal.append("<premFreqMode>" + selPremiumFrequencyMode.getSelectedItem().toString() + "</premFreqMode>");
        inputVal.append("<proposerAge>" + proposerAge + "</proposerAge>");
        inputVal.append("<policyTerm>" + policyTerm + "</policyTerm>");
        inputVal.append("<premiumAmount>" + (int) premiumAmount + "</premiumAmount>");
        inputVal.append("<isTriggerFund>" + isTriggerFund + "</isTriggerFund>");
        inputVal.append("<AutoAllocation>" + isAutoAllocation + "</AutoAllocation>");
        if (isAutoAllocation) {
            if (policyTerm >= 18) {
                inputVal.append("<perInvEquityFund>" + "70" + "</perInvEquityFund>");
                inputVal.append("<perInvEquityOptimiserFund>" + percent_EquityOptFund.getText().toString() + "</perInvEquityOptimiserFund>");
                inputVal.append("<perInvgrowthFund>" + percent_GrowthFund.getText().toString() + "</perInvgrowthFund>");
                inputVal.append("<perInvBalancedFund>" + percent_BalancedFund.getText().toString() + "</perInvBalancedFund>");
                inputVal.append("<perInvPureFund>" + percent_PureFund.getText().toString() + "</perInvPureFund>");
                inputVal.append("<perInvMidCapFund>" + percent_MidCapFund.getText().toString() + "</perInvMidCapFund>");
                inputVal.append("<perInvBondOptimiser>" + percent_BondOptimiser.getText().toString() + "</perInvBondOptimiser>");
                inputVal.append("<perInvCorpBondFund>" + "30" + "</perInvCorpBondFund>");
                inputVal.append("<perInvMoneyMktFund>" + percent_MoneyMktFund.getText().toString() + "</perInvMoneyMktFund>");
            } else if (policyTerm == 15) {
                inputVal.append("<perInvEquityFund>" + "70" + "</perInvEquityFund>");
                inputVal.append("<perInvEquityOptimiserFund>" + percent_EquityOptFund.getText().toString() + "</perInvEquityOptimiserFund>");
                inputVal.append("<perInvgrowthFund>" + percent_GrowthFund.getText().toString() + "</perInvgrowthFund>");
                inputVal.append("<perInvBalancedFund>" + percent_BalancedFund.getText().toString() + "</perInvBalancedFund>");
                inputVal.append("<perInvPureFund>" + percent_PureFund.getText().toString() + "</perInvPureFund>");
                inputVal.append("<perInvMidCapFund>" + percent_MidCapFund.getText().toString() + "</perInvMidCapFund>");
                inputVal.append("<perInvBondOptimiser>" + percent_BondOptimiser.getText().toString() + "</perInvBondOptimiser>");
                inputVal.append("<perInvCorpBondFund>" + "30" + "</perInvCorpBondFund>");
                inputVal.append("<perInvMoneyMktFund>" + percent_MoneyMktFund.getText().toString() + "</perInvMoneyMktFund>");
            } else if (policyTerm == 10) {
                inputVal.append("<perInvEquityFund>" + "50" + "</perInvEquityFund>");
                inputVal.append("<perInvEquityOptimiserFund>" + percent_EquityOptFund.getText().toString() + "</perInvEquityOptimiserFund>");
                inputVal.append("<perInvgrowthFund>" + percent_GrowthFund.getText().toString() + "</perInvgrowthFund>");
                inputVal.append("<perInvBalancedFund>" + percent_BalancedFund.getText().toString() + "</perInvBalancedFund>");
                inputVal.append("<perInvPureFund>" + percent_PureFund.getText().toString() + "</perInvPureFund>");
                inputVal.append("<perInvMidCapFund>" + percent_MidCapFund.getText().toString() + "</perInvMidCapFund>");
                inputVal.append("<perInvBondOptimiser>" + percent_BondOptimiser.getText().toString() + "</perInvBondOptimiser>");
                inputVal.append("<perInvCorpBondFund>" + "50" + "</perInvCorpBondFund>");
                inputVal.append("<perInvMoneyMktFund>" + percent_MoneyMktFund.getText().toString() + "</perInvMoneyMktFund>");
            }
        } else {
            inputVal.append("<perInvEquityFund>" + percent_EquityFund.getText().toString() + "</perInvEquityFund>");
            inputVal.append("<perInvEquityOptimiserFund>" + percent_EquityOptFund.getText().toString() + "</perInvEquityOptimiserFund>");
            inputVal.append("<perInvgrowthFund>" + percent_GrowthFund.getText().toString() + "</perInvgrowthFund>");
            inputVal.append("<perInvBalancedFund>" + percent_BalancedFund.getText().toString() + "</perInvBalancedFund>");
            inputVal.append("<perInvPureFund>" + percent_PureFund.getText().toString() + "</perInvPureFund>");
            inputVal.append("<perInvMidCapFund>" + percent_MidCapFund.getText().toString() + "</perInvMidCapFund>");
            inputVal.append("<perInvBondOptimiser>" + percent_BondOptimiser.getText().toString() + "</perInvBondOptimiser>");
            inputVal.append("<perInvCorpBondFund>" + percent_CorpBondFund.getText().toString() + "</perInvCorpBondFund>");
            inputVal.append("<perInvMoneyMktFund>" + percent_MoneyMktFund.getText().toString() + "</perInvMoneyMktFund>");
        }
        inputVal.append("<Product_Cat>" + product_cateogory + "</Product_Cat>");

        inputVal.append("<product_name>").append(planName).append("</product_name>");
        inputVal.append("<product_Code>").append(product_Code).append("</product_Code>");
        inputVal.append("<product_UIN>").append(product_UIN).append("</product_UIN>");
        inputVal.append("<product_cateogory>").append(product_cateogory).append("</product_cateogory>");
        inputVal.append("<product_type>").append(product_type).append("</product_type>");

        inputVal.append("<proposer_Is_Same_As_Life_Assured>").append(proposer_Is_Same_As_Life_Assured).append("</proposer_Is_Same_As_Life_Assured>");

        //Added Tushar Kadam Kerla Applicable Tax 7-Jun-2019
        String str_kerla_discount = "N";
        if (cb_kerladisc.isChecked()) {
            str_kerla_discount = "Y";
        }

        inputVal.append("<KFC>" + str_kerla_discount + "</KFC>");
        //End Added Tushar Kadam Kerla Applicable Tax 7-Jun-2019

        inputVal.append("</SmartInsureWealthPlus>");

    }

    private String[] getOutput(String string,
                               SmartInsureWealthPlusBean smartInsureWealthPlusBean) {


        SmartInsureWealthPlusProperties smartinsurewealthplurprop = new SmartInsureWealthPlusProperties();

        int _month_E = 0, _year_F = 0, _age_H = 0;

        String _policyInforce_G = "Y";

        double _premium_I = 0, _topUpPremium_J = 0, _premiumAllocationCharge_K = 0, _topUpCharges_L = 0, _ServiceTaxOnAllocation_M = 0, _amountAvailableForInvestment_N = 0, _sumAssuredRelatedCharges_O = 0, _riderCharges_P = 0, _policyAdministrationCharges_Q = 0, _mortalityCharges_R = 0, _totalCharges_S = 0, _serviceTaxExclOfSTOnAllocAndSurr_T = 0, _totalServiceTax_U = 0, _additionToFundIfAny_V = 0, _fundBeforeFMC_W = 0, _fundManagementCharge_X = 0, _serviceTaxOnFMC_Y = 0, _fundValueAfterFMCBerforeGA_Z = 0, _guaranteedAddition_AA = 0, _TerminalAddition = 0, _fundValueAtEnd_AB = 0, _surrenderCharges_AC = 0, _serviceTaxOnSurrenderCharges_AD = 0, _surrenderValue_AE = 0, _deathBenefit_AF = 0,
                _mortalityCharges_AG = 0, _totalCharges_AH = 0, _serviceTaxExclOfSTOnAllocAndSurr_AI = 0, _totalServiceTax_AJ = 0, _additionToFundIfAny_AK = 0, _fundBeforeFMC_AL = 0, _fundManagementCharge_AM = 0, _serviceTaxOnFMC_AN = 0, _fundValueAfterFMCBerforeGA_AO = 0, _guaranteedAddition_AP = 0, _fundValueAtEnd_AQ = 0, _surrenderCharges_AR = 0, _serviceTaxOnSurrenderCharges_AS = 0, _surrenderValue_AT = 0, _deathBenefit_AU = 0, _surrenderCap_AV = 0, _oneHundredPercentOfCummulativePremium_AW = 0, _AccTPDCharges = 0, _AccTPDCharges_8p = 0;

        double sum_AZ = 0, sum_AV = 0;

        // temp variable declaration.
        int month_E = 0, year_F = 0, age_H = 0;

        String policyInforce_G = "Y", returnOfMortalityChargeFinal = "0";

        double premium_I = 0, topUpPremium_J = 0, premiumAllocationCharge_K = 0, topUpCharges_L = 0, ServiceTaxOnAllocation_M = 0, amountAvailableForInvestment_N = 0, sumAssuredRelatedCharges_O = 0, riderCharges_P = 0, policyAdministrationCharges_Q = 0, mortalityCharges_R = 0, totalCharges_S = 0, serviceTaxExclOfSTOnAllocAndSurr_T = 0, totalServiceTax_U = 0, additionToFundIfAny_V = 0, fundBeforeFMC_W = 0, fundManagementCharge_X = 0, serviceTaxOnFMC_Y = 0, fundValueAfterFMCBerforeGA_Z = 0, guaranteedAddition_AA = 0, TerminalAddition = 0, fundValueAtEnd_AB = 0, surrenderCharges_AC = 0,
                serviceTaxOnSurrenderCharges_AD = 0, surrenderValue_AE = 0, deathBenefit_AF = 0, mortalityCharges_AG = 0, totalCharges_AH = 0, serviceTaxExclOfSTOnAllocAndSurr_AI = 0, totalServiceTax_AJ = 0, additionToFundIfAny_AK = 0, fundBeforeFMC_AL = 0, fundManagementCharge_AM = 0, serviceTaxOnFMC_AN = 0, fundValueAfterFMCBerforeGA_AO = 0, guaranteedAddition_AP = 0, fundValueAtEnd_AQ = 0, surrenderCharges_AR = 0, serviceTaxOnSurrenderCharges_AS = 0, surrenderValue_AT = 0, deathBenefit_AU = 0, surrenderCap_AV = 0, oneHundredPercentOfCummulativePremium_AW = 0, AccTPDCharges = 0, AccTPDCharges_8p = 0;

        double ServiceTaxOnAllocation_RIY = 0, _ServiceTaxOnAllocation_RIY = 0, MortalityCharges_RIY = 0, _MortalityCharges_RIY = 0, AccTPDCharges_RIY = 0, _AccTPDCharges_RIY = 0, TotalCharges_RIY = 0, _TotalCharges_RIY = 0, ServiceTax_exclOfSTonAllocAndSurr_RIY = 0, _ServiceTax_exclOfSTonAllocAndSurr_RIY = 0, AdditionToFundIfAny_RIY = 0, _AdditionToFundIfAny_RIY = 0, FundBeforeFMC_RIY = 0, _FundBeforeFMC_RIY = 0, FundManagementCharge_RIY = 0, _FundManagementCharge_RIY = 0, ServiceTaxOnFMC_RIY = 0, _ServiceTaxOnFMC_RIY = 0, TotalServiceTax_RIY = 0, _TotalServiceTax_RIY = 0, FundValueAfterFMCAndBeforeGA_RIY = 0, _FundValueAfterFMCAndBeforeGA_RIY = 0, GuaranteedAddition_RIY = 0, TerminalAddition_RIY = 0, FundValueAtEnd_RIY = 0, _FundValueAtEnd_RIY = 0, AmountAvailableForInvestment_RIY = 0, _AmountAvailableForInvestment_RIY = 0, SurrenderCharges_RIY = 0, _SurrenderCharges_RIY = 0, ServiceTaxOnSurrenderCharges_RIY = 0, _ServiceTaxOnSurrenderCharges_RIY = 0, SurrenderValue_RIY = 0, _SurrenderValue_RIY = 0, DeathBenefit_RIY = 0, _DeathBenefit_RIY = 0;

        double MortalityCharges_RIY8 = 0, _MortalityCharges_RIY8 = 0, AccTPDCharges_RIY8 = 0, _AccTPDCharges_RIY8 = 0, TotalCharges_RIY8 = 0, _TotalCharges_RIY8 = 0, ServiceTax_exclOfSTonAllocAndSurr_RIY8 = 0, _ServiceTax_exclOfSTonAllocAndSurr_RIY8 = 0, AdditionToFundIfAny_RIY8 = 0, _AdditionToFundIfAny_RIY8 = 0, FundBeforeFMC_RIY8 = 0, _FundBeforeFMC_RIY8 = 0, FundManagementCharge_RIY8 = 0, _FundManagementCharge_RIY8 = 0, ServiceTaxOnFMC_RIY8 = 0, _ServiceTaxOnFMC_RIY8 = 0, TotalServiceTax_RIY8 = 0, _TotalServiceTax_RIY8 = 0, FundValueAfterFMCAndBeforeGA_RIY8 = 0, _FundValueAfterFMCAndBeforeGA_RIY8 = 0, GuaranteedAddition_RIY8 = 0, TerminalAddition_RIY8 = 0, FundValueAtEnd_RIY8 = 0, _FundValueAtEnd_RIY8 = 0, AmountAvailableForInvestment_RIY8 = 0, _AmountAvailableForInvestment_RIY8 = 0, SurrenderCharges_RIY8 = 0, _SurrenderCharges_RIY8 = 0, ServiceTaxOnSurrenderCharges_RIY8 = 0, _ServiceTaxOnSurrenderCharges_RIY8 = 0, SurrenderValue_RIY8 = 0, _SurrenderValue_RIY8 = 0, DeathBenefit_RIY8 = 0, _DeathBenefit_RIY8 = 0;

        // For BI
        double sum_I = 0, sum_K = 0, sum_N = 0, sum_Q = 0, sum_R = 0, sum_S = 0, sum_U = 0, sum_X = 0, sum_AA = 0, sum_AG = 0, sum_AH = 0, sum_AJ = 0, sum_AM = 0, sum_AP = 0, Commission_BL = 0, sum_J = 0, cumm_Sum_I = 0;

        double _sum_X = 0, _sum_AM = 0, _sum_J = 0, _sum_I = 0, reductionYield_BD = 0, _reductionYield_BD = 0, irrAnnual_BD = 0, _irrAnnual_BD = 0, reductionInYieldMaturityAt = 0, _reductionInYieldMaturityAt = 0, _GuaranteedAddition_RIY8 = 0;

        double otherCharges_PartB = 0, otherCharges1_PartB = 0, otherCharges_PartA = 0, otherCharges1_PartA = 0, sum_01 = 0, sum_02 = 0, sum_03 = 0, sum1_03, amountAvailableForInvestment_N1 = 0, _amountAvailableForInvestment_N1 = 0, sum_N1 = 0;

        double returnMortality_4pr = 0, returnMortality_8pr = 0;
        int returnMortality = 0;
        ArrayList<String> List_BD = new ArrayList<String>();
        ArrayList<String> List_BI = new ArrayList<String>();
        // from GUI inputs
        boolean StaffDisc = smartInsureWealthPlusBean.getIsForStaffOrNot();
        boolean autoAssetAllocationStrategy = smartInsureWealthPlusBean.getAutoAssetAllocationStrategy();
        boolean triggerStrategy = smartInsureWealthPlusBean.getTriggerStrategy();
        boolean bancAssuranceDisc = false;
        int ageAtEntry = smartInsureWealthPlusBean.getAgeAtEntry();
        int policyTerm = smartInsureWealthPlusBean.getPolicyTerm_Basic();
        String planType = smartInsureWealthPlusBean.getPlanType();
        String premFreq = smartInsureWealthPlusBean.getPremFreq();

        int premiumPayingTerm = smartInsureWealthPlusBean.getPremiumPayingTerm();
        double SAMF = smartInsureWealthPlusBean.getSAMF();

        double percentToBeInvested_EquityFund = smartInsureWealthPlusBean
                .getPercentToBeInvested_EquityFund();
        double percentToBeInvested_EquityOptFund = smartInsureWealthPlusBean
                .getPercentToBeInvested_EquityOptFund();
        double percentToBeInvested_GrowthFund = smartInsureWealthPlusBean
                .getPercentToBeInvested_GrowthFund();
        double percentToBeInvested_BalancedFund = smartInsureWealthPlusBean
                .getPercentToBeInvested_BalancedFund();
        double percentToBeInvested_PureFund = smartInsureWealthPlusBean
                .getPercentToBeInvested_PureFund();
        double percentToBeInvested_MidcapFund = smartInsureWealthPlusBean
                .getPercentToBeInvested_MidCapFund();
        double percentToBeInvested_BondOptFund = smartInsureWealthPlusBean
                .getPercentToBeInvested_BondOptimiserFund();
        double percentToBeInvested_CorpFund = smartInsureWealthPlusBean
                .getCorpbondFund();
        double percentToBeInvested_MoneyMarketFund = smartInsureWealthPlusBean
                .getMoneyMarketFund();


//		double serviceTax =smartInsureWealthPlusBean.getServiceTax();
        double serviceTax = 0;
        boolean isKerlaDisc = smartInsureWealthPlusBean.isKerlaDisc();


        String transferFundStatus = "No Transfer";
        String addTopUp = "No";

        double effectivePremium = smartInsureWealthPlusBean
                .getEffectivePremium();
        int extraPremium = 0;
        int PF = smartInsureWealthPlusBean.getPF();
        /**
         * Modified by Akshaya
         */
        int PPT = smartInsureWealthPlusBean.getPolicyTerm_Basic();
        double effectiveTopUpPrem = smartInsureWealthPlusBean
                .getEffectiveTopUpPrem(); // *Sheet Name -> Input,*Cell -> Y70
        double sumAssured = (effectivePremium * SAMF);

        SmartInsureWealthPlusBusinessLogic BIMAST = new SmartInsureWealthPlusBusinessLogic();
        String[] forBIArray = BIMAST
                .getForBIArr(prop.mortalityCharges_AsPercentOfofLICtable);
        int rowNumber = 0;


        int monthNumber = 0;
        int month_BB = 0, _month_BB = 0;

        ArrayList<Double> lstFundValueAfter_FMC = new ArrayList<Double>();
        ArrayList<Double> lstFundValueAfter_FMC8 = new ArrayList<Double>();

        ArrayList<Double> lstFundValueAfter_FMC_RIY = new ArrayList<Double>();
        ArrayList<Double> lstFundValueAfter_RIY8 = new ArrayList<Double>();

        String Fund4 = null, Fund8 = null;

        try {
            for (int i = 0; i <= (policyTerm * 12); i++) {
                rowNumber++;

                BIMAST.setMonth_E(rowNumber);
                month_E = Integer.parseInt(BIMAST.getMonth_E());
                _month_E = month_E;
                // ////System.out.println("1. month_E "+BIMAST.getMonth_E());
                ////System.out.println("1. month_E " + month_E);

                BIMAST.setYear_F();
                year_F = Integer.parseInt(BIMAST.getYear_F());
                _year_F = year_F;
                // //System.out.println("2. year_F "+BIMAST.getYear_F());
                //System.out.println("2. year_F " + year_F);

                if ((_month_E % 12) == 0) {
                    bussIll.append("<policyYr" + _year_F + ">" + _year_F
                            + "</policyYr" + _year_F + ">");
                }

                if (isKerlaDisc == true && _year_F <= 2) {
                    serviceTax = 0.19;
                } else {
                    serviceTax = 0.18;
                }

                policyInforce_G = BIMAST.getPolicyInForce_G();
                _policyInforce_G = policyInforce_G;
                // //System.out.println("3. policyInforce_G "+BIMAST.getPolicyInForce_G());
                //System.out.println("3. policyInforce_G " + policyInforce_G);

                BIMAST.setAge_H(ageAtEntry);
                age_H = Integer.parseInt(BIMAST.getAge_H());
                _age_H = age_H;
//				 //System.out.println("4. age_H "+BIMAST.getAge_H());
                //System.out.println("4. age_H " + age_H);

                BIMAST.setPremium_I(PPT, PF,
                        smartInsureWealthPlusBean.getPremiumAmount());
                premium_I = Double.parseDouble(BIMAST.getPremium_I());
                _premium_I = premium_I;
                //System.out.println("_premium_I" + _premium_I);

                sum_I += _premium_I;
                cumm_Sum_I += _premium_I;
                if ((_month_E % 12) == 0) {

                    bussIll.append("<AnnPrem" + _year_F + ">" + commonForAllProd.getStringWithout_E(sum_I)
                            + "</AnnPrem" + _year_F + ">");
                    _sum_I = sum_I;
                    sum_I = 0;
                }

                BIMAST.setPremiumAllocationCharge_K(StaffDisc,
                        bancAssuranceDisc, planType, PPT, effectivePremium);
                premiumAllocationCharge_K = Double.parseDouble(BIMAST
                        .getPremiumAllocationCharge_K());
                _premiumAllocationCharge_K = premiumAllocationCharge_K;
                // //System.out.println("7. premiumAllocationCharge_K "+BIMAST.getPremiumAllocationCharge_K());
                //System.out.println("7. premiumAllocationCharge_K "+ premiumAllocationCharge_K);

                sum_K += _premiumAllocationCharge_K;
                sum_01 = sum_K;

                if ((_month_E % 12) == 0) {

                    bussIll.append("<PremAllCharge" + _year_F + ">"
                            + Math.round(sum_K) + "</PremAllCharge" + _year_F
                            + ">");
                    sum_K = 0;
                }

                BIMAST.setServiceTaxOnAllocation_M(prop.allocationCharges,
                        serviceTax);
                ServiceTaxOnAllocation_M = Double.parseDouble(BIMAST
                        .getServiceTaxOnAllocation_M());
                _ServiceTaxOnAllocation_M = ServiceTaxOnAllocation_M;
                // //System.out.println("9. ServiceTaxOnAllocation_M "+BIMAST.getServiceTaxOnAllocation_M());
                //System.out.println("9. ServiceTaxOnAllocation_M "						+ ServiceTaxOnAllocation_M);

                BIMAST.setAmountAvailableForInvestment_N();
                amountAvailableForInvestment_N = Double.parseDouble(BIMAST
                        .getAmountAvailableForInvestment_N());
                _amountAvailableForInvestment_N = amountAvailableForInvestment_N;
                // //System.out.println("10. amountAvailableForInvestment_N "+BIMAST.getAmountAvailableForInvestment_N());
                //System.out.println("10. amountAvailableForInvestment_N "						+ amountAvailableForInvestment_N);

                sum_N += _amountAvailableForInvestment_N;
                BIMAST.setAmountAvailableForInvestment_N1();
                amountAvailableForInvestment_N1 = Double.parseDouble(BIMAST.getAmountAvailableForInvestment_N1());
                _amountAvailableForInvestment_N1 = amountAvailableForInvestment_N1;
                sum_N1 += _amountAvailableForInvestment_N1;


				/*if ((_month_E % 12) == 0) {

                    bussIll.append("<AmtAviFrInv" + _year_F + ">"
                            + Math.round(sum_N) + "</AmtAviFrInv" + _year_F
                            + ">");
                    sum_N = 0;
				}*/

                if ((_month_E % 12) == 0) {

                    bussIll.append("<AmtAviFrInv" + _year_F + ">"
                            + Math.round(sum_N1) + "</AmtAviFrInv" + _year_F
                            + ">");
                    sum_N1 = 0;
                }

                BIMAST.setPolicyAdministrationCharge_Q(
                        _policyAdministrationCharges_Q, prop.charge_Inflation,
                        prop.fixedMonthlyExp_SP, prop.fixedMonthlyExp_RP,
                        prop.inflation_pa_SP, prop.inflation_pa_RP, planType);
                policyAdministrationCharges_Q = Double.parseDouble(BIMAST
                        .getPolicyAdministrationCharge_Q());
                _policyAdministrationCharges_Q = policyAdministrationCharges_Q;
                // //System.out.println("13. policyAdministrationCharges_Q "+BIMAST.getPolicyAdministrationCharge_Q());
                //System.out.println("13. policyAdministrationCharges_Q "						+ policyAdministrationCharges_Q);

                sum_Q += _policyAdministrationCharges_Q;
                sum_02 = sum_Q;

                if ((_month_E % 12) == 0) {

                    bussIll.append("<PolAdminChrg" + _year_F + ">"
                            + Math.round(sum_Q) + "</PolAdminChrg" + _year_F
                            + ">");
                    sum_Q = 0;
                }


                BIMAST.setOneHundredPercentOfCummulativePremium_AW(cumm_Sum_I);
                oneHundredPercentOfCummulativePremium_AW = Double
                        .parseDouble(BIMAST
                                .getOneHundredPercentOfCummulativePremium_AW());
                _oneHundredPercentOfCummulativePremium_AW = oneHundredPercentOfCummulativePremium_AW;
                // //System.out.println("45. oneHundredPercentOfCummulativePremium_AW "+BIMAST.getOneHundredPercentOfCummulativePremium_AW());
//				System.out
//						.println("45. oneHundredPercentOfCummulativePremium_AW "
//								+ oneHundredPercentOfCummulativePremium_AW);

                BIMAST.setMortalityCharges_R(_fundValueAtEnd_AB, policyTerm,
                        forBIArray, smartInsureWealthPlusBean.getSumAssured(),
                        prop.mortalityCharges, smartInsureWealthPlusBean.getAgeAtEntry());
                mortalityCharges_R = Double.parseDouble(BIMAST
                        .getMortalityCharges_R());
                _mortalityCharges_R = mortalityCharges_R;
                // //System.out.println("14. mortalityCharges_R "+BIMAST.getMortalityCharges_R());
                //System.out.println("14. mortalityCharges_R "						+ mortalityCharges_R);

                sum_R += _mortalityCharges_R;
                returnMortality_4pr += _mortalityCharges_R;
                if ((_month_E % 12) == 0) {

                    // //System.out.println("SUM="+sum_R);

                    bussIll.append("<MortChrg4Pr" + _year_F + ">"
                            + Math.round(sum_R) + "</MortChrg4Pr" + _year_F
                            + ">");
                    sum_R = 0;
                }

                if ((_month_E % 12) == 0) {

                    // //System.out.println("SUM="+sum_R);

                    bussIll.append("<ExtraPremium4Pr" + _year_F + ">"
                            + extraPremium + "</ExtraPremium4Pr" + _year_F
                            + ">");
                }

                BIMAST.setAccTPDCharges(_fundValueAtEnd_AB, policyTerm,
                        prop.acc_tpd_charges,
                        smartInsureWealthPlusBean.getSumAssured(),
                        prop.mortalityCharges);
                AccTPDCharges = Double.parseDouble(BIMAST.getAccTPDCharges());
                _AccTPDCharges = AccTPDCharges;
                // //System.out.println("14. mortalityCharges_R "+BIMAST.getMortalityCharges_R());
//				//System.out.println("14. AccTPDCharges " + AccTPDCharges);

                BIMAST.setTotalCharges_S(policyTerm, riderCharges_P);
                totalCharges_S = Double.parseDouble(BIMAST.getTotalCharges_S());
                _totalCharges_S = totalCharges_S;
                // //System.out.println("15. totalCharges_S "+BIMAST.getTotalCharges_S());
                //System.out.println("15. totalCharges_S " + totalCharges_S);

                sum_S += _totalCharges_S;
                if ((_month_E % 12) == 0) {

                    bussIll.append("<TotCharg4Pr" + _year_F + ">"
                            + Math.round(sum_S) + "</TotCharg4Pr" + _year_F
                            + ">");
                    sum_S = 0;
                }

                BIMAST.setServiceTax_exclOfSTonAllocAndSurr_T(serviceTax,
                        prop.mortalityAndRiderCharges,
                        prop.administrationCharges);
                serviceTaxExclOfSTOnAllocAndSurr_T = Double.parseDouble(BIMAST
                        .getServiceTax_exclOfSTonAllocAndSurr_T());
                _serviceTaxExclOfSTOnAllocAndSurr_T = serviceTaxExclOfSTOnAllocAndSurr_T;
                // //System.out.println("16. serviceTaxExclOfSTOnAllocAndSurr_T "+BIMAST.getServiceTax_exclOfSTonAllocAndSurr_T());
                //System.out.println("16. serviceTaxExclOfSTOnAllocAndSurr_T "						+ serviceTaxExclOfSTOnAllocAndSurr_T);

                BIMAST.setAdditionToFundIfAny_V(_fundValueAtEnd_AB, policyTerm,
                        riderCharges_P);
                additionToFundIfAny_V = Double.parseDouble(BIMAST
                        .getAdditionToFundIfAny_V());
                _additionToFundIfAny_V = additionToFundIfAny_V;

                sum_AV += additionToFundIfAny_V;

                if ((_month_E % 12) == 0) {

                    bussIll.append("<AdditionsToTheFund4Pr" + _year_F + ">"
                            + commonForAllProd.getRound(commonForAllProd.getStringWithout_E(sum_AV)) + "</AdditionsToTheFund4Pr" + _year_F
                            + ">");
                    sum_AV = 0;
                }

                // System.out.println("18. additionToFundIfAny_V "+BIMAST.getAdditionToFundIfAny_V());
//				System.out.println("18. additionToFundIfAny_V "
//						+ additionToFundIfAny_V);

                BIMAST.setFundBeforeFMC_W(_fundValueAtEnd_AB, policyTerm,
                        riderCharges_P);
                fundBeforeFMC_W = Double.parseDouble(BIMAST
                        .getFundBeforeFMC_W());
                _fundBeforeFMC_W = fundBeforeFMC_W;
                // //System.out.println("19. fundBeforeFMC_W "+BIMAST.getFundBeforeFMC_W());
                //System.out.println("19. fundBeforeFMC_W " + fundBeforeFMC_W);

                BIMAST.setFundManagementCharge_X(policyTerm,
                        percentToBeInvested_EquityFund,
                        percentToBeInvested_EquityOptFund,
                        percentToBeInvested_GrowthFund,
                        percentToBeInvested_BalancedFund,
                        percentToBeInvested_PureFund,
                        percentToBeInvested_MidcapFund,
                        percentToBeInvested_BondOptFund,
                        percentToBeInvested_CorpFund,
                        percentToBeInvested_MoneyMarketFund,
                        autoAssetAllocationStrategy);
                fundManagementCharge_X = Double.parseDouble(BIMAST
                        .getFundManagementCharge_X());
                _fundManagementCharge_X = fundManagementCharge_X;
                // //System.out.println("20. fundManagementCharge_X "+BIMAST.getFundManagementCharge_X());
                //System.out.println("20. fundManagementCharge_X "						+ fundManagementCharge_X);

                sum_X += _fundManagementCharge_X;

                sum_03 = sum_X;

                if ((_month_E % 12) == 0) {
                    _sum_X = Math.round(sum_X);
                    bussIll.append("<FundMgmtCharg4Pr" + _year_F + ">" + commonForAllProd.getStringWithout_E(_sum_X)
                            + "</FundMgmtCharg4Pr" + _year_F + ">");
                    sum_X = 0;
                }

                otherCharges_PartA = sum_01 + sum_02 + sum_03;

                if ((_month_E % 12) == 0) {

                    bussIll.append("<OtherCharges4Pr_PartA" + _year_F + ">"
                            + commonForAllProd.getRound(String
                            .valueOf(otherCharges_PartA)) + "</OtherCharges4Pr_PartA" + _year_F
                            + ">");
                    otherCharges_PartA = 0;
                }

                if ((_month_E % 12) == 0) {

                    bussIll.append("<OtherCharges4Pr_PartB" + _year_F + ">"
                            + commonForAllProd.getRound(String
                            .valueOf(otherCharges_PartB)) + "</OtherCharges4Pr_PartB" + _year_F
                            + ">");
                    otherCharges_PartB = 0;
                }



				/*if ((_month_E % 12) == 0) {
                    bussIll.append("<LoyaltyAdd4Pr" + _year_F + ">" + 0 +
                            "</LoyaltyAdd4Pr" + _year_F + ">");
				}*/

                BIMAST.setServiceTaxOnFMC_Y(percentToBeInvested_EquityFund,
                        percentToBeInvested_EquityOptFund,
                        percentToBeInvested_GrowthFund,
                        percentToBeInvested_BalancedFund,
                        percentToBeInvested_PureFund,
                        percentToBeInvested_MidcapFund,
                        percentToBeInvested_BondOptFund,
                        percentToBeInvested_CorpFund,
                        percentToBeInvested_MoneyMarketFund,
                        autoAssetAllocationStrategy,
                        policyTerm, serviceTax);
                serviceTaxOnFMC_Y = Double.parseDouble(BIMAST
                        .getServiceTaxOnFMC_Y());
                _serviceTaxOnFMC_Y = serviceTaxOnFMC_Y;
                // //System.out.println("21. serviceTaxOnFMC_Y "+BIMAST.getServiceTaxOnFMC_Y());
                //System.out.println("21. serviceTaxOnFMC_Y " + serviceTaxOnFMC_Y);

                BIMAST.setTotalServiceTax_U(riderCharges_P, serviceTax);
                totalServiceTax_U = Double.parseDouble(BIMAST
                        .getTotalServiceTax_U());
                _totalServiceTax_U = totalServiceTax_U;
                // //System.out.println("17. totalServiceTax_U "+BIMAST.getTotalServiceTax_U());
                //System.out.println("17. totalServiceTax_U " + totalServiceTax_U);

                sum_U += _totalServiceTax_U;
                if ((_month_E % 12) == 0) {
                    bussIll.append("<TotServTax4Pr" + _year_F + ">"
                            + Math.round(sum_U) + "</TotServTax4Pr" + _year_F
                            + ">");
                    sum_U = 0;
                }

                BIMAST.setFundValueAfterFMCAndBeforeGA_Z(policyTerm);
                fundValueAfterFMCBerforeGA_Z = Double.parseDouble(BIMAST
                        .getFundValueAfterFMCAndBeforeGA_Z());
                _fundValueAfterFMCBerforeGA_Z = fundValueAfterFMCBerforeGA_Z;
                // //System.out.println("22. fundValueAfterFMCBerforeGA_Z "+BIMAST.getFundValueAfterFMCAndBeforeGA_Z());
                //System.out.println("22. fundValueAfterFMCBerforeGA_Z "						+ fundValueAfterFMCBerforeGA_Z);

                lstFundValueAfter_FMC.add(_fundValueAfterFMCBerforeGA_Z);

                BIMAST.setGuaranteedAddition_AA(planType,
                        smartInsureWealthPlusBean.getEffectivePremium(), PPT,
                        lstFundValueAfter_FMC, _month_E);
                guaranteedAddition_AA = Double.parseDouble(BIMAST
                        .getGuaranteedAddition_AA());
                _guaranteedAddition_AA = guaranteedAddition_AA;
                // //System.out.println("23. guaranteedAddition_AA "+BIMAST.getGuaranteedAddition_AA());
                //System.out.println("23. guaranteedAddition_AA "						+ guaranteedAddition_AA);

                sum_AA += _guaranteedAddition_AA;
                if ((_month_E % 12) == 0) {
                    bussIll.append("<GuarntAdd4Pr" + _year_F + ">"
                            + Math.round(sum_AA) + "</GuarntAdd4Pr" + _year_F
                            + ">");
                    sum_AA = 0;
                }

                BIMAST.setTerminalAddition();
                TerminalAddition = Double.parseDouble(BIMAST
                        .getTerminalAddition());
                _TerminalAddition = TerminalAddition;

                BIMAST.setFundValueAtEnd_AB();
                fundValueAtEnd_AB = Double.parseDouble(BIMAST
                        .getFundValueAtEnd_AB());
                _fundValueAtEnd_AB = fundValueAtEnd_AB;
                // //System.out.println("24. fundValueAtEnd_AB "+BIMAST.getFundValueAtEnd_AB());
                //System.out.println(month_E + " fundValueAtEnd_AB "						+ fundValueAtEnd_AB);

                if ((_month_E % 12) == 0) {
                    bussIll.append("<FundValAtEnd4Pr"
                            + _year_F
                            + ">"
                            + commonForAllProd.getRoundUp(commonForAllProd
                            .getStringWithout_E(_fundValueAtEnd_AB))
                            + "</FundValAtEnd4Pr" + _year_F + ">");

                    Fund4 = commonForAllProd.getRoundUp(commonForAllProd
                            .getStringWithout_E(_fundValueAtEnd_AB));

                }

                if ((_month_E % 12) == 0) {
                    // double temp =
                    // Math.round(Double.parseDouble(commonForAllProd.getRoundUp(String.valueOf(_fundValueAtEnd_AC)))
                    // + _sum_Y);
                    // //System.out.println(commonForAllProd.getRoundUp(String.valueOf(_fundValueAtEnd_AC))
                    // + " + " + _sum_Y + " = " + temp);
                    bussIll.append("<FundBefFMC4Pr"
                            + _year_F
                            + ">"
                            + Math.round(Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd
                            .getStringWithout_E(_fundValueAtEnd_AB)))
                            + _sum_X) + "</FundBefFMC4Pr" + _year_F
                            + ">");
                }

                BIMAST.setSurrenderCap_AV(effectivePremium);
                surrenderCap_AV = Double.parseDouble(BIMAST
                        .getSurrenderCap_AV());
                _surrenderCap_AV = surrenderCap_AV;
                // //System.out.println("44. surrenderCap_AV "+BIMAST.getSurrenderCap_AV());
                //System.out.println("44. surrenderCap_AV " + surrenderCap_AV);

                BIMAST.setSurrenderCharges_AC(smartInsureWealthPlusBean
                        .getEffectivePremium());
                surrenderCharges_AC = Double.parseDouble(BIMAST
                        .getSurrenderCharges_AC());
                _surrenderCharges_AC = surrenderCharges_AC;
                // //System.out.println("25. surrenderCharges_AC "+BIMAST.getSurrenderCharges_AC());
                //System.out.println("25. surrenderCharges_AC "						+ surrenderCharges_AC);

                BIMAST.setServiceTaxOnSurrenderCharges_AD(serviceTax);
                serviceTaxOnSurrenderCharges_AD = Double.parseDouble(BIMAST
                        .getServiceTaxOnSurrenderCharges_AD());
                _serviceTaxOnSurrenderCharges_AD = serviceTaxOnSurrenderCharges_AD;
                // //System.out.println("26. serviceTaxOnSurrenderCharges_AD "+BIMAST.getServiceTaxOnSurrenderCharges_AD());
                //System.out.println("26. serviceTaxOnSurrenderCharges_AD "						+ serviceTaxOnSurrenderCharges_AD);

                BIMAST.setSurrenderValue_AE();
                surrenderValue_AE = Double.parseDouble(BIMAST
                        .getSurrenderValue_AE());
                _surrenderValue_AE = surrenderValue_AE;
                // //System.out.println("27. surrenderValue_AE "+BIMAST.getSurrenderValue_AE());
                System.out
                        .println("27. surrenderValue_AE " + surrenderValue_AE);

                if ((_month_E % 12) == 0) {
                    bussIll.append("<SurrVal4Pr"
                            + _year_F
                            + ">"
                            + commonForAllProd.getRoundUp(commonForAllProd
                            .getStringWithout_E(_surrenderValue_AE))
                            + "</SurrVal4Pr" + _year_F + ">");

                }

                BIMAST.setDeathBenefit_AF(policyTerm, sumAssured, cumm_Sum_I, fundValueAtEnd_AB);
                deathBenefit_AF = Double.parseDouble(BIMAST
                        .getDeathBenefit_AF());
                _deathBenefit_AF = deathBenefit_AF;
                // //System.out.println("28. deathBenefit_AF "+BIMAST.getDeathBenefit_AF());
                //System.out.println("DDDDDDDDDDDDDDeathBenefit_AF " + deathBenefit_AF);

                if ((_month_E % 12) == 0) {

                    bussIll.append("<DeathBen4Pr"
                            + _year_F
                            + ">"
                            + commonForAllProd.getRoundUp(commonForAllProd
                            .getStringWithout_E(_deathBenefit_AF))
                            + "</DeathBen4Pr" + _year_F + ">");

                }

                double _month_L = _month_E;
                if ((_month_L / 12) == policyTerm) {

                    // //System.out.println("SUM="+sum_R);

                    bussIll.append("<ReturnMortalityChrg4Pr" + _year_F + ">"
                            + Math.round(returnMortality_4pr) + "</ReturnMortalityChrg4Pr" + _year_F
                            + ">");
                } else if ((_month_E % 12) == 0) {
                    bussIll.append("<ReturnMortalityChrg4Pr" + _year_F + ">"
                            + returnMortality + "</ReturnMortalityChrg4Pr" + _year_F
                            + ">");
                }

                BIMAST.setMortalityCharges_AG(_fundValueAtEnd_AQ, policyTerm,
                        forBIArray, sumAssured, prop.mortalityCharges, smartInsureWealthPlusBean.getAgeAtEntry());
                mortalityCharges_AG = Double.parseDouble(BIMAST
                        .getMortalityCharges_AG());
                _mortalityCharges_AG = mortalityCharges_AG;
                //System.out.println("29. mortalityCharges_AG "+ BIMAST.getMortalityCharges_AG());
                // //System.out.println("29. mortalityCharges_AG "+mortalityCharges_AG);

                sum_AG += _mortalityCharges_AG;
                returnMortality_8pr += _mortalityCharges_AG;
                if ((_month_E % 12) == 0) {

                    bussIll.append("<MortChrg8Pr" + _year_F + ">"
                            + Math.round(sum_AG) + "</MortChrg8Pr" + _year_F
                            + ">");
                    sum_AG = 0;
                }

                if ((_month_E % 12) == 0) {

                    // //System.out.println("SUM="+sum_R);

                    bussIll.append("<ExtraPremium8Pr" + _year_F + ">"
                            + extraPremium + "</ExtraPremium8Pr" + _year_F
                            + ">");
                }

                BIMAST.setAccTPDCharges_8p(_fundValueAtEnd_AQ, policyTerm,
                        prop.acc_tpd_charges,
                        smartInsureWealthPlusBean.getSumAssured(),
                        prop.mortalityCharges);
                AccTPDCharges_8p = Double.parseDouble(BIMAST
                        .getAccTPDCharges_8p());
                _AccTPDCharges_8p = AccTPDCharges_8p;
                // //System.out.println("14. mortalityCharges_R "+BIMAST.getMortalityCharges_R());
                //System.out.println("14. AccTPDCharges_8p " + AccTPDCharges_8p);

                BIMAST.setTotalCharges_AH(policyTerm, riderCharges_P);
                totalCharges_AH = Double.parseDouble(BIMAST
                        .getTotalCharges_AH());
                _totalCharges_AH = totalCharges_AH;
                //System.out.println("30. totalCharges_AH "+ BIMAST.getTotalCharges_AH());
                // //System.out.println("30. totalCharges_AH "+totalCharges_AH);

                sum_AH += _totalCharges_AH;
                if ((_month_E % 12) == 0) {
                    bussIll.append("<TotCharg8Pr" + _year_F + ">"
                            + Math.round(sum_AH) + "</TotCharg8Pr" + _year_F
                            + ">");
                    sum_AH = 0;
                }

                BIMAST.setServiceTax_exclOfSTonAllocAndSurr_AI(serviceTax,
                        prop.mortalityAndRiderCharges,
                        prop.administrationCharges);
                serviceTaxExclOfSTOnAllocAndSurr_AI = Double.parseDouble(BIMAST
                        .getServiceTax_exclOfSTonAllocAndSurr_AI());
                _serviceTaxExclOfSTOnAllocAndSurr_AI = serviceTaxExclOfSTOnAllocAndSurr_AI;
                //System.out.println("31. serviceTaxExclOfSTOnAllocAndSurr_AI "+ BIMAST.getServiceTax_exclOfSTonAllocAndSurr_AI());
                // //System.out.println("31. serviceTaxExclOfSTOnAllocAndSurr_AI "+serviceTaxExclOfSTOnAllocAndSurr_AI);

                BIMAST.setAdditionToFundIfAny_AK(_fundValueAtEnd_AQ,
                        policyTerm, riderCharges_P);
                additionToFundIfAny_AK = Double.parseDouble(BIMAST
                        .getAdditionToFundIfAny_AK());
                _additionToFundIfAny_AK = additionToFundIfAny_AK;


                sum_AZ += additionToFundIfAny_AK;

                if ((_month_E % 12) == 0) {

                    bussIll.append("<AdditionsToTheFund8Pr" + _year_F + ">"
                            + commonForAllProd.getRound(commonForAllProd.getStringWithout_E(sum_AZ)) + "</AdditionsToTheFund8Pr" + _year_F
                            + ">");
                    sum_AZ = 0;
                }


//				System.out.println("33. additionToFundIfAny_AK "
//						+ BIMAST.getAdditionToFundIfAny_AK());
                // System.out.println("33. additionToFundIfAny_AK "+additionToFundIfAny_AK);

                BIMAST.setFundBeforeFMC_AL(_fundValueAtEnd_AQ, policyTerm,
                        riderCharges_P);
                fundBeforeFMC_AL = Double.parseDouble(BIMAST
                        .getFundBeforeFMC_AL());
                _fundBeforeFMC_AL = fundBeforeFMC_AL;
                //System.out.println("34. fundBeforeFMC_AL "+ BIMAST.getFundBeforeFMC_AL());
                // //System.out.println("34. fundBeforeFMC_AL "+fundBeforeFMC_AL);

                BIMAST.setFundManagementCharge_AM(policyTerm,
                        percentToBeInvested_EquityFund,
                        percentToBeInvested_EquityOptFund,
                        percentToBeInvested_GrowthFund,
                        percentToBeInvested_BalancedFund,
                        percentToBeInvested_PureFund,
                        percentToBeInvested_MidcapFund,
                        percentToBeInvested_BondOptFund,
                        percentToBeInvested_CorpFund,
                        percentToBeInvested_MoneyMarketFund,
                        autoAssetAllocationStrategy);
                fundManagementCharge_AM = Double.parseDouble(BIMAST
                        .getFundManagementCharge_AM());
                _fundManagementCharge_AM = fundManagementCharge_AM;
                //System.out.println("35. fundManagementCharge_AM "+ BIMAST.getFundManagementCharge_AM());
                // //System.out.println("35. fundManagementCharge_AM "+fundManagementCharge_AM);

                sum_AM += _fundManagementCharge_AM;

                sum1_03 = sum_AM;

                if ((_month_E % 12) == 0) {
                    _sum_AM = Math.round(sum_AM);
                    bussIll.append("<FundMgmtCharg8Pr" + _year_F + ">"
                            + commonForAllProd.getStringWithout_E(_sum_AM) + "</FundMgmtCharg8Pr" + _year_F + ">");
                    sum_AM = 0;

                }

                otherCharges1_PartA = sum_01 + sum_02 + sum1_03;

                if ((_month_E % 12) == 0) {

                    bussIll.append("<OtherCharges8Pr_PartA" + _year_F + ">"
                            + commonForAllProd.getRound(String
                            .valueOf(otherCharges1_PartA)) + "</OtherCharges8Pr_PartA" + _year_F
                            + ">");
                    otherCharges1_PartA = 0;
                }

                if ((_month_E % 12) == 0) {

                    bussIll.append("<OtherCharges8Pr_PartB" + _year_F + ">"
                            + commonForAllProd.getRound(String
                            .valueOf(otherCharges1_PartB)) + "</OtherCharges8Pr_PartB" + _year_F
                            + ">");
                    otherCharges1_PartB = 0;
                }


				/*if ((_month_E % 12) == 0) {
                    bussIll.append("<LoyaltyAdd8Pr" + _year_F + ">" + 0 +
                            "</LoyaltyAdd8Pr" + _year_F + ">");
				}*/

                BIMAST.setServiceTaxOnFMC_AN(percentToBeInvested_EquityFund,
                        percentToBeInvested_EquityOptFund,
                        percentToBeInvested_GrowthFund,
                        percentToBeInvested_BalancedFund,
                        percentToBeInvested_PureFund,
                        percentToBeInvested_MidcapFund,
                        percentToBeInvested_BondOptFund,
                        percentToBeInvested_CorpFund,
                        percentToBeInvested_MoneyMarketFund,
                        autoAssetAllocationStrategy,
                        policyTerm, serviceTax);
                serviceTaxOnFMC_AN = Double.parseDouble(BIMAST
                        .getServiceTaxOnFMC_AN());
                _serviceTaxOnFMC_AN = serviceTaxOnFMC_AN;
                //System.out.println("36. serviceTaxOnFMC_AN "+ BIMAST.getServiceTaxOnFMC_AN());
                // //System.out.println("36. serviceTaxOnFMC_AN "+serviceTaxOnFMC_AN);

                BIMAST.setFundValueAfterFMCAndBeforeGA_AO(policyTerm);
                fundValueAfterFMCBerforeGA_AO = Double.parseDouble(BIMAST
                        .getFundValueAfterFMCAndBeforeGA_AO());
                _fundValueAfterFMCBerforeGA_AO = fundValueAfterFMCBerforeGA_AO;
                //System.out.println("37. fundValueAfterFMCBerforeGA_AO "+ BIMAST.getFundValueAfterFMCAndBeforeGA_AO());
                // //System.out.println("37. fundValueAfterFMCBerforeGA_AO "+fundValueAfterFMCBerforeGA_AO);

                BIMAST.setTotalServiceTax_AJ(riderCharges_P, serviceTax);
                totalServiceTax_AJ = Double.parseDouble(BIMAST
                        .getTotalServiceTax_AJ());
                _totalServiceTax_AJ = totalServiceTax_AJ;
                //System.out.println("32. totalServiceTax_AJ "+ BIMAST.getTotalServiceTax_AJ());
                // //System.out.println("32. totalServiceTax_AJ "+totalServiceTax_AJ);

                sum_AJ += _totalServiceTax_AJ;
                if ((_month_E % 12) == 0) {

                    bussIll.append("<TotServTxOnCharg8Pr" + _year_F + ">"
                            + Math.round(sum_AJ) + "</TotServTxOnCharg8Pr"
                            + _year_F + ">");
                    sum_AJ = 0;
                }

                lstFundValueAfter_FMC8.add(_fundValueAfterFMCBerforeGA_AO);

                BIMAST.setGuaranteedAddition_AP(planType,
                        smartInsureWealthPlusBean.getEffectivePremium(), PPT,
                        lstFundValueAfter_FMC8, _month_E);
                guaranteedAddition_AP = Double.parseDouble(BIMAST
                        .getGuaranteedAddition_AP());
                _guaranteedAddition_AP = guaranteedAddition_AP;
                //System.out.println("38. guaranteedAddition_AP "+ BIMAST.getGuaranteedAddition_AP());
                // //System.out.println("38. guaranteedAddition_AP "+guaranteedAddition_AP);

                sum_AP += _guaranteedAddition_AP;
                if ((_month_E % 12) == 0) {
                    bussIll.append("<GuarntAdd8Pr" + _year_F + ">"
                            + Math.round(sum_AP) + "</GuarntAdd8Pr" + _year_F
                            + ">");
                    sum_AP = 0;
                }

                BIMAST.setFundValueAtEnd_AQ();
                fundValueAtEnd_AQ = Double.parseDouble(BIMAST
                        .getFundValueAtEnd_AQ());
                _fundValueAtEnd_AQ = fundValueAtEnd_AQ;
                //System.out.println("39. fundValueAtEnd_AQ "+ BIMAST.getFundValueAtEnd_AQ());
                // //System.out.println("39. fundValueAtEnd_AQ "+fundValueAtEnd_AQ);

                if ((_month_E % 12) == 0) {
                    bussIll.append("<FundValAtEnd8Pr"
                            + _year_F
                            + ">"
                            + commonForAllProd.getRoundUp(commonForAllProd
                            .getStringWithout_E(_fundValueAtEnd_AQ))
                            + "</FundValAtEnd8Pr" + _year_F + ">");

                    Fund8 = commonForAllProd.getRoundUp(commonForAllProd
                            .getStringWithout_E(_fundValueAtEnd_AQ));
                }

                if ((_month_E % 12) == 0) {
                    // double temp =
                    // Math.round(Double.parseDouble(commonForAllProd.getRoundUp(String.valueOf(_fundValueAtEnd_AC)))
                    // + _sum_Y);
                    // //System.out.println(commonForAllProd.getRoundUp(String.valueOf(_fundValueAtEnd_AC))
                    // + " + " + _sum_Y + " = " + temp);
                    bussIll.append("<FundBefFMC8Pr"
                            + _year_F
                            + ">"
                            + Math.round(Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd
                            .getStringWithout_E(_fundValueAtEnd_AQ)))
                            + _sum_AM) + "</FundBefFMC8Pr" + _year_F
                            + ">");
                }

                BIMAST.setSurrenderCharges_AR(effectivePremium, PPT);
                surrenderCharges_AR = Double.parseDouble(BIMAST
                        .getSurrenderCharges_AR());
                _surrenderCharges_AR = surrenderCharges_AR;
                //System.out.println("40. surrenderCharges_AR "+ BIMAST.getSurrenderCharges_AR());
                // //System.out.println("40. surrenderCharges_AR "+surrenderCharges_AR);

                BIMAST.setServiceTaxOnSurrenderCharges_AS(serviceTax);
                serviceTaxOnSurrenderCharges_AS = Double.parseDouble(BIMAST
                        .getServiceTaxOnSurrenderCharges_AS());
                _serviceTaxOnSurrenderCharges_AS = serviceTaxOnSurrenderCharges_AS;
                //System.out.println("41. serviceTaxOnSurrenderCharges_AS "+ BIMAST.getServiceTaxOnSurrenderCharges_AS());
                // //System.out.println("41. serviceTaxOnSurrenderCharges_AS "+serviceTaxOnSurrenderCharges_AS);

                BIMAST.setSurrenderValue_AT();
                surrenderValue_AT = Double.parseDouble(BIMAST
                        .getSurrenderValue_AT());
                _surrenderValue_AT = surrenderValue_AT;
                //System.out.println("42. surrenderValue_AT "+ BIMAST.getSurrenderValue_AT());
                // //System.out.println("42. surrenderValue_AT "+surrenderValue_AT);

                if ((_month_E % 12) == 0) {
                    bussIll.append("<SurrVal8Pr"
                            + _year_F
                            + ">"
                            + commonForAllProd.getRoundUp(commonForAllProd
                            .getStringWithout_E(_surrenderValue_AT))
                            + "</SurrVal8Pr" + _year_F + ">");
                }

                BIMAST.setDeathBenefit_AU(policyTerm, sumAssured, cumm_Sum_I);
                deathBenefit_AU = Double.parseDouble(BIMAST
                        .getDeathBenefit_AU());
                _deathBenefit_AU = deathBenefit_AU;
                //System.out.println("43. deathBenefit_AU "+ BIMAST.getDeathBenefit_AU());
                // //System.out.println("43. deathBenefit_AU "+deathBenefit_AU);

                if ((_month_E % 12) == 0) {

                    bussIll.append("<DeathBen8Pr"
                            + _year_F
                            + ">"
                            + commonForAllProd.getRoundUp(commonForAllProd
                            .getStringWithout_E(_deathBenefit_AU))
                            + "</DeathBen8Pr" + _year_F + ">");

                }
                if ((_month_E % 12) == 0) {
                    Commission_BL = BIMAST.getCommission_BL(_sum_I, _sum_J,
                            smartInsureWealthPlusBean);
                    bussIll.append("<CommIfPay8Pr" + _year_F + ">"
                            + Math.round(Commission_BL) + "</CommIfPay8Pr"
                            + _year_F + ">");

                }

                double _month_K = _month_E;
                if ((_month_K / 12) == policyTerm) {

                    // //System.out.println("SUM="+sum_R);
                    returnOfMortalityChargeFinal = commonForAllProd.getRoundOffLevel2(commonForAllProd.getStringWithout_E(returnMortality_8pr));
                    bussIll.append("<ReturnMortalityChrg8Pr" + _year_F + ">"
                            + Math.round(returnMortality_8pr) + "</ReturnMortalityChrg8Pr" + _year_F
                            + ">");
                } else if ((_month_E % 12) == 0) {
                    bussIll.append("<ReturnMortalityChrg8Pr" + _year_F + ">"
                            + returnMortality + "</ReturnMortalityChrg8Pr" + _year_F
                            + ">");
                }

                /*
                 * @@@@@@@@@@@@@@@@@@@@@@@@@ Reduction Yield Starts from here
                 * @@@@@@@@@@@@@@@@@@@@@@@@@
                 */

                /* @@@@@@ Till Premium allocation charge it is the same @@@@@ */

                BIMAST.setServiceTaxOnAllocation_RIY(
                        prop.allocationChargesYield, serviceTax);
                ServiceTaxOnAllocation_RIY = Double.parseDouble(BIMAST
                        .getServiceTaxOnAllocation_RIY());
                _ServiceTaxOnAllocation_RIY = ServiceTaxOnAllocation_RIY;

                BIMAST.setAmountAvailableForInvestment_RIY();
                AmountAvailableForInvestment_RIY = Double.parseDouble(BIMAST
                        .getAmountAvailableForInvestment_RIY());
                _AmountAvailableForInvestment_RIY = AmountAvailableForInvestment_RIY;
                // //System.out.println("10. amountAvailableForInvestment_N "+BIMAST.getAmountAvailableForInvestment_N());
                //System.out.println("44. AmountAvailableForInvestment_RIY "						+ AmountAvailableForInvestment_RIY);

                BIMAST.setMortalityCharges_RIY(_FundValueAtEnd_RIY, policyTerm,
                        forBIArray, smartInsureWealthPlusBean.getSumAssured(),
                        prop.mortalityChargesYield);
                MortalityCharges_RIY = Double.parseDouble(BIMAST
                        .getMortalityCharges_RIY());
                _MortalityCharges_RIY = MortalityCharges_RIY;
                // //System.out.println("14. mortalityCharges_R "+BIMAST.getMortalityCharges_R());
                //System.out.println("44. mortalityCharges_RIY "						+ MortalityCharges_RIY);

                BIMAST.setAccTPDCharges_RIY(_FundValueAtEnd_RIY, policyTerm,
                        prop.acc_tpd_charges,
                        smartInsureWealthPlusBean.getSumAssured(),
                        prop.mortalityChargesYield);
                AccTPDCharges_RIY = Double.parseDouble(BIMAST
                        .getAccTPDCharges_RIY());
                _AccTPDCharges_RIY = AccTPDCharges_RIY;
                // //System.out.println("14. mortalityCharges_R "+BIMAST.getMortalityCharges_R());
                System.out
                        .println("45. AccTPDCharges_RIY " + AccTPDCharges_RIY);

                BIMAST.setTotalCharges_RIY(policyTerm, riderCharges_P);
                TotalCharges_RIY = Double.parseDouble(BIMAST
                        .getTotalCharges_RIY());
                _TotalCharges_RIY = TotalCharges_RIY;
                // //System.out.println("15. totalCharges_S "+BIMAST.getTotalCharges_S());
                //System.out.println("46. totalCharges_RIY " + TotalCharges_RIY);

                BIMAST.setServiceTax_exclOfSTonAllocAndSurr_RIY(
                        serviceTax, prop.mortalityAndRiderChargesYield,
                        prop.administrationChargesYield);
                ServiceTax_exclOfSTonAllocAndSurr_RIY = Double
                        .parseDouble(BIMAST
                                .getServiceTax_exclOfSTonAllocAndSurr_RIY());
                _ServiceTax_exclOfSTonAllocAndSurr_RIY = ServiceTax_exclOfSTonAllocAndSurr_RIY;
                // //System.out.println("16. serviceTaxExclOfSTOnAllocAndSurr_T "+BIMAST.getServiceTax_exclOfSTonAllocAndSurr_T());
                //System.out.println("47. ServiceTax_exclOfSTonAllocAndSurr_RIY "						+ ServiceTax_exclOfSTonAllocAndSurr_RIY);

                BIMAST.setAdditionToFundIfAny_RIY(_FundValueAtEnd_RIY,
                        policyTerm, riderCharges_P);
                AdditionToFundIfAny_RIY = Double.parseDouble(BIMAST
                        .getAdditionToFundIfAny_RIY());
                _AdditionToFundIfAny_RIY = AdditionToFundIfAny_RIY;
                //System.out.println("48. AdditionToFundIfAny_RIY "+ BIMAST.getAdditionToFundIfAny_RIY());

                BIMAST.setFundBeforeFMC_RIY(_FundValueAtEnd_RIY, policyTerm,
                        riderCharges_P);
                FundBeforeFMC_RIY = Double.parseDouble(BIMAST
                        .getFundBeforeFMC_RIY());
                _FundBeforeFMC_RIY = FundBeforeFMC_RIY;
                // System.out.println("19. fundBeforeFMC_W "+BIMAST.getFundBeforeFMC_W());
//				System.out
//						.println("49. FundBeforeFMC_RIY " + FundBeforeFMC_RIY);

                BIMAST.setFundManagementCharge_RIY(policyTerm,
                        percentToBeInvested_EquityFund,
                        percentToBeInvested_EquityOptFund,
                        percentToBeInvested_GrowthFund,
                        percentToBeInvested_BalancedFund,
                        percentToBeInvested_PureFund,
                        percentToBeInvested_MidcapFund,
                        percentToBeInvested_BondOptFund,
                        percentToBeInvested_CorpFund,
                        percentToBeInvested_MoneyMarketFund,
                        autoAssetAllocationStrategy);
                FundManagementCharge_RIY = Double.parseDouble(BIMAST
                        .getFundManagementCharge_RIY());
                _FundManagementCharge_RIY = FundManagementCharge_RIY;
                //System.out.println("50. FundManagementCharge_RIY "						+ FundManagementCharge_RIY);

                BIMAST.setServiceTaxOnFMC_RIY(percentToBeInvested_EquityFund,
                        percentToBeInvested_EquityOptFund,
                        percentToBeInvested_GrowthFund,
                        percentToBeInvested_BalancedFund,
                        percentToBeInvested_PureFund,
                        percentToBeInvested_MidcapFund,
                        percentToBeInvested_BondOptFund,
                        percentToBeInvested_CorpFund,
                        percentToBeInvested_MoneyMarketFund,
                        autoAssetAllocationStrategy,
                        policyTerm, serviceTax);
                ServiceTaxOnFMC_RIY = Double.parseDouble(BIMAST
                        .getServiceTaxOnFMC_RIY());
                _ServiceTaxOnFMC_RIY = ServiceTaxOnFMC_RIY;
                //System.out.println("51. ServiceTaxOnFMC_RIY "						+ ServiceTaxOnFMC_RIY);

                BIMAST.setTotalServiceTax_RIY(riderCharges_P, serviceTax);
                TotalServiceTax_RIY = Double.parseDouble(BIMAST
                        .getTotalServiceTax_RIY());
                _TotalServiceTax_RIY = TotalServiceTax_RIY;
                //System.out.println("52. TotalServiceTax_RIY "						+ TotalServiceTax_RIY);

                BIMAST.setFundValueAfterFMCAndBeforeGA_RIY(policyTerm);
                FundValueAfterFMCAndBeforeGA_RIY = Double.parseDouble(BIMAST
                        .getFundValueAfterFMCAndBeforeGA_RIY());
                _FundValueAfterFMCAndBeforeGA_RIY = FundValueAfterFMCAndBeforeGA_RIY;
                //System.out.println("53. FundValueAfterFMCAndBeforeGA_RIY "						+ FundValueAfterFMCAndBeforeGA_RIY);

                //lstFundValueAfter_FMC.add(FundValueAfterFMCAndBeforeGA_RIY);

                GuaranteedAddition_RIY = _guaranteedAddition_AA;

                TerminalAddition_RIY = TerminalAddition;

                BIMAST.setFundValueAtEnd_RIY();
                FundValueAtEnd_RIY = Double.parseDouble(BIMAST
                        .getFundValueAtEnd_RIY());
                _FundValueAtEnd_RIY = FundValueAtEnd_RIY;
                //System.out.println("55. FundValueAtEnd_RIY "						+ FundValueAtEnd_RIY);

                BIMAST.setSurrenderCharges_RIY(smartInsureWealthPlusBean
                        .getEffectivePremium());
                SurrenderCharges_RIY = Double.parseDouble(BIMAST
                        .getSurrenderCharges_RIY());
                _SurrenderCharges_RIY = SurrenderCharges_RIY;
                // //System.out.println("25. surrenderCharges_AC "+BIMAST.getSurrenderCharges_AC());
                //System.out.println("56. SurrenderCharges_RIY "						+ SurrenderCharges_RIY);

                BIMAST.setServiceTaxOnSurrenderCharges_RIY(serviceTax);
                ServiceTaxOnSurrenderCharges_RIY = Double.parseDouble(BIMAST
                        .getServiceTaxOnSurrenderCharges_RIY());
                _ServiceTaxOnSurrenderCharges_RIY = ServiceTaxOnSurrenderCharges_RIY;
                // //System.out.println("26. serviceTaxOnSurrenderCharges_AD "+BIMAST.getServiceTaxOnSurrenderCharges_AD());
                //System.out.println("57. ServiceTaxOnSurrenderCharges_RIY "						+ ServiceTaxOnSurrenderCharges_RIY);

                BIMAST.setSurrenderValue_RIY();
                SurrenderValue_RIY = Double.parseDouble(BIMAST
                        .getSurrenderValue_RIY());
                _SurrenderValue_RIY = SurrenderValue_RIY;
                // //System.out.println("27. surrenderValue_AE "+BIMAST.getSurrenderValue_AE());
                //System.out.println("58. SurrenderValue_RIY "						+ SurrenderValue_RIY);

                BIMAST.setDeathBenefit_RIY(policyTerm, sumAssured, cumm_Sum_I);
                DeathBenefit_RIY = Double.parseDouble(BIMAST
                        .getDeathBenefit_RIY());
                _DeathBenefit_RIY = DeathBenefit_RIY;
                //System.out.println("51. DeathBenefit_RIY " + DeathBenefit_RIY);

                /* @@@@@@@@@@@@@@@@@@@@ RIY 8% - START @@@@@@@@@@@@@@@@@@@@@@@@ */

                BIMAST.setMortalityCharges_RIY8(_FundValueAtEnd_RIY8,
                        policyTerm, forBIArray,
                        smartInsureWealthPlusBean.getSumAssured(),
                        prop.mortalityChargesYield);
                MortalityCharges_RIY8 = Double.parseDouble(BIMAST
                        .getMortalityCharges_RIY8());
                _MortalityCharges_RIY8 = MortalityCharges_RIY8;
                // //System.out.println("14. mortalityCharges_R "+BIMAST.getMortalityCharges_R());
                //System.out.println("52. mortalityCharges_RIY8 "						+ MortalityCharges_RIY8);

                BIMAST.setAccTPDCharges_RIY8(_FundValueAtEnd_RIY8, policyTerm,
                        prop.acc_tpd_charges,
                        smartInsureWealthPlusBean.getSumAssured(),
                        prop.mortalityChargesYield);
                AccTPDCharges_RIY8 = Double.parseDouble(BIMAST
                        .getAccTPDCharges_RIY8());
                _AccTPDCharges_RIY8 = AccTPDCharges_RIY8;
                // //System.out.println("14. mortalityCharges_R "+BIMAST.getMortalityCharges_R());
                //System.out.println("53. AccTPDCharges_RIY8 "						+ AccTPDCharges_RIY8);

                BIMAST.setTotalCharges_RIY8(policyTerm, riderCharges_P);
                TotalCharges_RIY8 = Double.parseDouble(BIMAST
                        .getTotalCharges_RIY8());
                _TotalCharges_RIY8 = TotalCharges_RIY;
                // //System.out.println("15. totalCharges_S "+BIMAST.getTotalCharges_S());
                System.out
                        .println("54. totalCharges_RIY8 " + TotalCharges_RIY8);

                BIMAST.setServiceTax_exclOfSTonAllocAndSurr_RIY8(
                        serviceTax, prop.mortalityAndRiderChargesYield,
                        prop.administrationChargesYield);
                ServiceTax_exclOfSTonAllocAndSurr_RIY8 = Double
                        .parseDouble(BIMAST
                                .getServiceTax_exclOfSTonAllocAndSurr_RIY8());
                _ServiceTax_exclOfSTonAllocAndSurr_RIY8 = ServiceTax_exclOfSTonAllocAndSurr_RIY8;
                // //System.out.println("16. serviceTaxExclOfSTOnAllocAndSurr_T "+BIMAST.getServiceTax_exclOfSTonAllocAndSurr_T());
                System.out
                        .println("55. ServiceTax_exclOfSTonAllocAndSurr_RIY8 "
                                + ServiceTax_exclOfSTonAllocAndSurr_RIY8);

                BIMAST.setAdditionToFundIfAny_RIY8(_FundValueAtEnd_RIY8,
                        policyTerm, riderCharges_P);
                AdditionToFundIfAny_RIY8 = Double.parseDouble(BIMAST
                        .getAdditionToFundIfAny_RIY8());
                _AdditionToFundIfAny_RIY8 = AdditionToFundIfAny_RIY8;
                //System.out.println("56. AdditionToFundIfAny_RIY8 "+ BIMAST.getAdditionToFundIfAny_RIY8());

                BIMAST.setFundBeforeFMC_RIY8(_FundValueAtEnd_RIY8, policyTerm,
                        riderCharges_P);
                FundBeforeFMC_RIY8 = Double.parseDouble(BIMAST
                        .getFundBeforeFMC_RIY8());
                _FundBeforeFMC_RIY8 = FundBeforeFMC_RIY8;
                //System.out.println("57. FundBeforeFMC_RIY8 "+ FundBeforeFMC_RIY8);

                BIMAST.setFundManagementCharge_RIY8(policyTerm,
                        percentToBeInvested_EquityFund,
                        percentToBeInvested_EquityOptFund,
                        percentToBeInvested_GrowthFund,
                        percentToBeInvested_BalancedFund,
                        percentToBeInvested_PureFund,
                        percentToBeInvested_MidcapFund,
                        percentToBeInvested_BondOptFund,
                        percentToBeInvested_CorpFund,
                        percentToBeInvested_MoneyMarketFund,
                        autoAssetAllocationStrategy);
                FundManagementCharge_RIY8 = Double.parseDouble(BIMAST
                        .getFundManagementCharge_RIY8());
                _FundManagementCharge_RIY8 = FundManagementCharge_RIY8;
                //System.out.println("58. FundManagementCharge_RIY8 "						+ FundManagementCharge_RIY8);

                BIMAST.setServiceTaxOnFMC_RIY8(percentToBeInvested_EquityFund,
                        percentToBeInvested_EquityOptFund,
                        percentToBeInvested_GrowthFund,
                        percentToBeInvested_BalancedFund,
                        percentToBeInvested_PureFund,
                        percentToBeInvested_MidcapFund,
                        percentToBeInvested_BondOptFund,
                        percentToBeInvested_CorpFund,
                        percentToBeInvested_MoneyMarketFund,
                        autoAssetAllocationStrategy,
                        policyTerm, serviceTax);
                ServiceTaxOnFMC_RIY8 = Double.parseDouble(BIMAST
                        .getServiceTaxOnFMC_RIY8());
                _ServiceTaxOnFMC_RIY8 = ServiceTaxOnFMC_RIY8;
                //System.out.println("51. ServiceTaxOnFMC_RIY8 "						+ ServiceTaxOnFMC_RIY8);

                BIMAST.setTotalServiceTax_RIY8(riderCharges_P, serviceTax);
                TotalServiceTax_RIY8 = Double.parseDouble(BIMAST
                        .getTotalServiceTax_RIY8());
                _TotalServiceTax_RIY8 = TotalServiceTax_RIY8;
                //System.out.println("52. TotalServiceTax_RIY8 "						+ TotalServiceTax_RIY8);

                BIMAST.setFundValueAfterFMCAndBeforeGA_RIY8(policyTerm);
                FundValueAfterFMCAndBeforeGA_RIY8 = Double.parseDouble(BIMAST
                        .getFundValueAfterFMCAndBeforeGA_RIY8());
                _FundValueAfterFMCAndBeforeGA_RIY8 = FundValueAfterFMCAndBeforeGA_RIY8;
                //System.out.println("53. FundValueAfterFMCAndBeforeGA_RIY8 "						+ FundValueAfterFMCAndBeforeGA_RIY8);

                lstFundValueAfter_RIY8.add(FundValueAfterFMCAndBeforeGA_RIY8);

//				GuaranteedAddition_RIY8 = _guaranteedAddition_AP;

                BIMAST.setGuaranteedAddition_RIY8(planType,
                        smartInsureWealthPlusBean.getEffectivePremium(), PPT,
                        lstFundValueAfter_RIY8, _month_E);
                GuaranteedAddition_RIY8 = Double.parseDouble(BIMAST
                        .getGuaranteedAddition_RIY8());
                _GuaranteedAddition_RIY8 = guaranteedAddition_AP;
                //System.out.println("60. GuaranteedAddition_RIY8 "+ BIMAST.getGuaranteedAddition_RIY8());

                //System.out.println("53. GuaranteedAddition_RIY8 "						+ GuaranteedAddition_RIY8);
                TerminalAddition_RIY8 = TerminalAddition;

                BIMAST.setMonth_BB(monthNumber);
                month_BB = Integer.parseInt(BIMAST.getMonth_BB());
                _month_BB = month_BB;
                //System.out.println("month_bb " + month_BB);


                BIMAST.setReductionYield_BD(policyTerm, _FundValueAtEnd_RIY8, returnOfMortalityChargeFinal);
                reductionYield_BD = Double.parseDouble(BIMAST
                        .getReductionYield_BD());
                _reductionYield_BD = reductionYield_BD;
                //System.out.println("reductionYield_BD " + reductionYield_BD);

                BIMAST.setFundValueAtEnd_RIY8();
                FundValueAtEnd_RIY8 = Double.parseDouble(BIMAST
                        .getFundValueAtEnd_RIY8());
                _FundValueAtEnd_RIY8 = FundValueAtEnd_RIY8;
                //System.out.println("55. FundValueAtEnd_RIY8 "						+ FundValueAtEnd_RIY8);

                BIMAST.setSurrenderCharges_RIY8(smartInsureWealthPlusBean
                        .getEffectivePremium());
                SurrenderCharges_RIY8 = Double.parseDouble(BIMAST
                        .getSurrenderCharges_RIY8());
                _SurrenderCharges_RIY8 = SurrenderCharges_RIY8;
                // //System.out.println("25. surrenderCharges_AC "+BIMAST.getSurrenderCharges_AC());
                //System.out.println("56. SurrenderCharges_RIY8 "						+ SurrenderCharges_RIY8);

                BIMAST.setServiceTaxOnSurrenderCharges_RIY8(serviceTax);
                ServiceTaxOnSurrenderCharges_RIY8 = Double.parseDouble(BIMAST
                        .getServiceTaxOnSurrenderCharges_RIY8());
                _ServiceTaxOnSurrenderCharges_RIY8 = ServiceTaxOnSurrenderCharges_RIY8;
                // //System.out.println("26. serviceTaxOnSurrenderCharges_AD "+BIMAST.getServiceTaxOnSurrenderCharges_AD());
                //System.out.println("57. ServiceTaxOnSurrenderCharges_RIY8 "						+ ServiceTaxOnSurrenderCharges_RIY8);

                BIMAST.setSurrenderValue_RIY8();
                SurrenderValue_RIY8 = Double.parseDouble(BIMAST
                        .getSurrenderValue_RIY8());
                _SurrenderValue_RIY8 = SurrenderValue_RIY8;
                // //System.out.println("27. surrenderValue_AE "+BIMAST.getSurrenderValue_AE());
                //System.out.println("58. SurrenderValue_RIY8 "						+ SurrenderValue_RIY8);

                BIMAST.setDeathBenefit_RIY8(policyTerm, sumAssured, cumm_Sum_I);
                DeathBenefit_RIY8 = Double.parseDouble(BIMAST
                        .getDeathBenefit_RIY8());
                _DeathBenefit_RIY8 = DeathBenefit_RIY;
                System.out
                        .println("59. DeathBenefit_RIY8 " + DeathBenefit_RIY8);


                monthNumber++;


//				if(month_E != policyTerm*12)
//				{
                List_BD.add(commonForAllProd.roundUp_Level2(commonForAllProd
                        .getStringWithout_E(reductionYield_BD)));
//				}
//				else
//				{
//					List_BD.add(commonForAllProd.roundUp_Level2(commonForAllProd
//							.getStringWithout_E(FundValueAtEnd_RIY8)));
//				}
                // List_BI.add(commonForAllProd.roundUp_Level2(commonForAllProd.getStringWithout_E(reductionYield_BI)));





                /* @@@@@@@@@@@@@@@@@@@@ RIY 8% - END @@@@@@@@@@@@@@@@@@@@@@@@ */

            }

            double ans = BIMAST.irr(List_BD, 0.001);
            // double ans1=BIMAST.irr(List_BI, 0.01);
            //System.out.println("ans_BD "+ans);
            // //System.out.println("ans1_BI "+ans1);

            BIMAST.setIRRAnnual_BD(ans);
            irrAnnual_BD = Double.parseDouble(BIMAST.getIRRAnnual_BD());
            _irrAnnual_BD = irrAnnual_BD;
            //System.out.println("irrAnnual_BD " + irrAnnual_BD);

            /*
             * BIMAST.setIRRAnnual_BI(ans1);
             * irrAnnual_BI=Double.parseDouble(BIMAST.getIRRAnnual_BI());
             * _irrAnnual_BI=irrAnnual_BI;
             */
            // //System.out.println("irrAnnual_BU "+irrAnnual_BI);

            BIMAST.setReductionInYieldMaturityAt(prop.int2);
            reductionInYieldMaturityAt = Double.parseDouble(BIMAST
                    .getReductionInYieldMaturityAt());
            _reductionInYieldMaturityAt = reductionInYieldMaturityAt;
            //System.out.println("reductionInYieldMaturityAt "						+ reductionInYieldMaturityAt);

        } catch (Exception e) {
            // TODO: handle exception

            String str = e.getMessage();
        }

        return new String[]{
                commonForAllProd.getRoundOffLevel2(commonForAllProd.getStringWithout_E(reductionInYieldMaturityAt)),
                commonForAllProd.getRoundOffLevel2(commonForAllProd.getStringWithout_E(irrAnnual_BD * 100)),
                commonForAllProd.getRoundOffLevel2(commonForAllProd.getStringWithout_E(reductionInYieldMaturityAt)),
                Fund4,
                Fund8,
                commonForAllProd.getStringWithout_E(smartInsureWealthPlusBean.getSumAssured())
        }
                ;
    }

    public void setDefaultDate(int id) {
        Calendar present_date = Calendar.getInstance();
        present_date.add(Calendar.YEAR, -id);
        mDay = present_date.get(Calendar.DAY_OF_MONTH);
        mMonth = present_date.get(Calendar.MONTH);
        mYear = present_date.get(Calendar.YEAR);
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

    private String getEffectivePremium(
            SmartInsureWealthPlusBean smartInsureWealthPlusBean) {
        double effectivepremium = 0;

        effectivepremium = smartInsureWealthPlusBean.getPremiumAmount() * 12;

        return "" + effectivepremium;
    }

    public boolean valPremiumAmt() {
        double minPremiumAmt = 0; /*maxPremiumAmt = prop.maxPremiumAmtLimit;*/
        double premium = 0;
        String error = "";
        if (selPlan.getSelectedItem().toString().equals("Regular")) {
            minPremiumAmt = 4000;
        }
        if (premiumAmt.getText().toString().equals("")) {
            error = "Please enter Premium Amount in Rs.";
        } else if (!(Double.parseDouble(premiumAmt.getText().toString()) % 100 == 0)) {
            error = "Premium Amount should be multiple of 100";
        } else {
            if (Double.parseDouble(premiumAmt.getText().toString()) < minPremiumAmt) {
                error = "Enter Minimum Premium Amount of Rs."
                        + currencyFormat.format(minPremiumAmt);
            }

        }

        if (!error.equals("")) {
            commonMethods.showMessageDialog(context, error);


            return false;
        } else
            return true;
    }

    public boolean valMaturityAge() {
        int Age = Integer.parseInt(ageInYears.getSelectedItem().toString());
        int PolicyTerm = Integer.parseInt(selPolicyTerm.getSelectedItem()
                .toString());

        // if (selPlan.getSelectedItem().toString().equals("Single")) {
        if ((Age + PolicyTerm) > 65) {
            commonMethods.showMessageDialog(context, "Max. Maturity age allowed is 65 years");
            selPolicyTerm.setSelection(0, false);
            // apply for focusable
            commonMethods.setFocusable(btn_bi_smart_insure_wealth_plus_life_assured_date);
            btn_bi_smart_insure_wealth_plus_life_assured_date
                    .requestFocus();

            return false;

        } else if ((Age + PolicyTerm) < 18) {
            commonMethods.showMessageDialog(context, "Min. Maturity age allowed is 18 years");
            selPolicyTerm.setSelection(0, false);
            // apply for focusable
            commonMethods.setFocusable(selPolicyTerm);
            selPolicyTerm.requestFocus();

            return false;

        } else {
            return true;
        }
    }

    // Addition of invested fund mub be 100%
    public boolean valTotalAllocation() {
        String error;

        if (is_trigger_fund.equalsIgnoreCase("") && auto_allocation.equalsIgnoreCase("")) {
            commonMethods.showMessageDialog(context, "Please select Strategy");
            percent_EquityFund.requestFocus();

            return false;
        } else if (is_trigger_fund.equalsIgnoreCase("false") && auto_allocation.equalsIgnoreCase("false")) {
            double equityFund, equityOptFund, growthFund, balancedFund, pureFund, midcapFund, BondOptimiser, corpBond, MoneyMkt;

            if (!percent_EquityFund.getText().toString().equals(""))
                equityFund = Double.parseDouble(percent_EquityFund.getText().toString());
            else
                equityFund = 0;

            if (!percent_EquityOptFund.getText().toString().equals(""))
                equityOptFund = Double.parseDouble(percent_EquityOptFund.getText().toString());
            else
                equityOptFund = 0;

            if (!percent_GrowthFund.getText().toString().equals(""))
                growthFund = Double.parseDouble(percent_GrowthFund.getText().toString());
            else
                growthFund = 0;

            if (!percent_BalancedFund.getText().toString().equals(""))
                balancedFund = Double.parseDouble(percent_BalancedFund.getText().toString());
            else
                balancedFund = 0;

            if (!percent_PureFund.getText().toString().equals(""))
                pureFund = Double.parseDouble(percent_PureFund.getText().toString());
            else
                pureFund = 0;

            if (!percent_MidCapFund.getText().toString().equals(""))
                midcapFund = Double.parseDouble(percent_MidCapFund.getText().toString());
            else
                midcapFund = 0;

            if (!percent_BondOptimiser.getText().toString().equals(""))
                BondOptimiser = Double.parseDouble(percent_BondOptimiser.getText().toString());
            else
                BondOptimiser = 0;

            if (!percent_CorpBondFund.getText().toString().equals(""))
                corpBond = Double.parseDouble(percent_CorpBondFund.getText().toString());
            else
                corpBond = 0;

            if (!percent_MoneyMktFund.getText().toString().equals(""))
                MoneyMkt = Double.parseDouble(percent_MoneyMktFund.getText().toString());
            else
                MoneyMkt = 0;

            if ((equityFund + equityOptFund + growthFund + balancedFund + pureFund
                    + midcapFund + BondOptimiser + corpBond + MoneyMkt) != 100) {
                commonMethods.showMessageDialog(context, "Total sum of % to be invested for all fund should be equal to 100%");
                percent_EquityFund.requestFocus();


                return false;
            } else
                return true;
        } else {
            return true;
        }

    }

    public boolean valLifeAssuredDetail() {

        if (btn_bi_smart_insure_wealth_plus_proposer_date.getText().toString()
                .equals("")) {
            commonMethods.dialogWarning(context, "Please Select Proposer DOB First", true);
            return false;
        } else if ((Integer
                .parseInt(!textviewProposerAge.getText()
                        .toString().equals("") ? textviewProposerAge
                        .getText().toString() : "0")) < 18) {
            if (ageInYears.getSelectedItem()
                    .toString().equals("")) {
                commonMethods.dialogWarning(context, "Please Select LifeAssured DOB First", true);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public boolean valName() {
        if (!proposer_Title.equals("") && !proposer_First_Name.equals("")
                && !proposer_Last_Name.equals("")) {
            return true;
        } else {
            commonMethods.showMessageDialog(context, "Please Fill The Detail For Name");
            return false;
        }
    }

    public boolean valProposerSameAsLifeAssured() {
        if (!proposer_Is_Same_As_Life_Assured.equals("")) {
            return true;
        } else {
            commonMethods.showMessageDialog(context, "Please Select Whether Proposer Is Same As Life Assured");
            return false;

        }
    }

    public boolean valLifeAssuredProposerDetail() {
        if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {
            if (lifeAssured_Title.equals("")
                    || lifeAssured_First_Name.equals("")
                    || lifeAssured_Last_Name.equals("")) {

                commonMethods.showMessageDialog(context, "Please Fill Name Detail For LifeAssured");
                if (lifeAssured_Title.equals("")) {
                    // apply focusable method
                    commonMethods.setFocusable(spnr_bi_smart_insure_wealth_plus_life_assured_title);
                    spnr_bi_smart_insure_wealth_plus_life_assured_title
                            .requestFocus();
                } else if (lifeAssured_First_Name.equals("")) {

                    edt_bi_smart_insure_wealth_plus_life_assured_first_name
                            .requestFocus();
                } else {
                    edt_bi_smart_insure_wealth_plus_life_assured_last_name
                            .requestFocus();
                }
                return false;
            } else if (lifeAssured_Title.equalsIgnoreCase("Mr.")
                    && gender.equalsIgnoreCase("Female")) {

                commonMethods.showMessageDialog(context, "Life Assured Title and Gender is not Valid");
                // apply focusable method
                commonMethods.setFocusable(spnr_bi_smart_insure_wealth_plus_life_assured_title);
                spnr_bi_smart_insure_wealth_plus_life_assured_title
                        .requestFocus();

                return false;

            } else if (lifeAssured_Title.equalsIgnoreCase("Ms.")
                    && gender.equalsIgnoreCase("Male")) {

                commonMethods.showMessageDialog(context, "Life Assured Title and Gender is not Valid");
                commonMethods.setFocusable(spnr_bi_smart_insure_wealth_plus_life_assured_title);
                spnr_bi_smart_insure_wealth_plus_life_assured_title
                        .requestFocus();

                return false;

            } else if (lifeAssured_Title.equalsIgnoreCase("Mrs.")
                    && gender.equalsIgnoreCase("Male")) {

                commonMethods.showMessageDialog(context, "Life Assured Title and Gender is not Valid");
                // apply focusable method
                commonMethods.setFocusable(spnr_bi_smart_insure_wealth_plus_life_assured_title);
                spnr_bi_smart_insure_wealth_plus_life_assured_title
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
                    commonMethods.setFocusable(spnr_bi_smart_insure_wealth_plus_life_assured_title);
                    spnr_bi_smart_insure_wealth_plus_life_assured_title
                            .requestFocus();
                } else if (lifeAssured_First_Name.equals("")) {

                    edt_bi_smart_insure_wealth_plus_life_assured_first_name
                            .requestFocus();
                } else {
                    edt_bi_smart_insure_wealth_plus_life_assured_last_name
                            .requestFocus();
                }

                return false;
            } else if (lifeAssured_Title.equalsIgnoreCase("Mr.")
                    && gender.equalsIgnoreCase("Female")) {

                commonMethods.showMessageDialog(context, "Life Assured Title and Gender is not Valid");
                commonMethods.setFocusable(spnr_bi_smart_insure_wealth_plus_life_assured_title);
                spnr_bi_smart_insure_wealth_plus_life_assured_title
                        .requestFocus();

                return false;

            } else if (lifeAssured_Title.equalsIgnoreCase("Ms.")
                    && gender.equalsIgnoreCase("Male")) {

                commonMethods.showMessageDialog(context, "Life Assured Title and Gender is not Valid");
                commonMethods.setFocusable(spnr_bi_smart_insure_wealth_plus_life_assured_title);
                spnr_bi_smart_insure_wealth_plus_life_assured_title
                        .requestFocus();

                return false;

            } else if (lifeAssured_Title.equalsIgnoreCase("Mrs.")
                    && gender.equalsIgnoreCase("Male")) {

                commonMethods.showMessageDialog(context, "Life Assured Title and Gender is not Valid");
                commonMethods.setFocusable(spnr_bi_smart_insure_wealth_plus_life_assured_title);
                spnr_bi_smart_insure_wealth_plus_life_assured_title
                        .requestFocus();

                return false;
            } else if (proposer_Title.equals("")
                    || proposer_First_Name.equals("")
                    || proposer_Last_Name.equals("")) {

                commonMethods.showMessageDialog(context, "Please Fill Name Detail For Proposer");
                if (proposer_Title.equals("")) {
                    // apply focusable method
                    commonMethods.setFocusable(spnr_bi_smart_insure_wealth_plus_proposer_title);
                    spnr_bi_smart_insure_wealth_plus_proposer_title
                            .requestFocus();
                } else if (proposer_First_Name.equals("")) {
                    edt_bi_smart_insure_wealth_plus_proposer_first_name
                            .requestFocus();
                } else {
                    edt_bi_smart_insure_wealth_plus_proposer_last_name
                            .requestFocus();
                }

                return false;

            } else if (proposer_Title.equalsIgnoreCase("Mr.")
                    && gender_proposer.equalsIgnoreCase("Female")) {

                commonMethods.showMessageDialog(context, "Proposar Title and Gender is not Valid");
                commonMethods.setFocusable(spnr_bi_smart_insure_wealth_plus_proposer_title);
                spnr_bi_smart_insure_wealth_plus_proposer_title
                        .requestFocus();

                return false;

            } else if (proposer_Title.equalsIgnoreCase("Ms.")
                    && gender_proposer.equalsIgnoreCase("Male")) {

                commonMethods.showMessageDialog(context, "Proposar Title and Gender is not Valid");
                commonMethods.setFocusable(spnr_bi_smart_insure_wealth_plus_proposer_title);
                spnr_bi_smart_insure_wealth_plus_proposer_title
                        .requestFocus();

                return false;

            } else if (proposer_Title.equalsIgnoreCase("Mrs.")
                    && gender_proposer.equalsIgnoreCase("Male")) {

                commonMethods.showMessageDialog(context, "Proposar Title and Gender is not Valid");
                commonMethods.setFocusable(spnr_bi_smart_insure_wealth_plus_proposer_title);
                spnr_bi_smart_insure_wealth_plus_proposer_title
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
                commonMethods.setFocusable(btn_bi_smart_insure_wealth_plus_life_assured_date);
                btn_bi_smart_insure_wealth_plus_life_assured_date
                        .requestFocus();

                return false;
            } else {
                return true;
            }
        } else if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("n")) {

            if (lifeAssured_date_of_birth.equals("")
                    || lifeAssured_date_of_birth
                    .equalsIgnoreCase("select Date")) {
                commonMethods.showMessageDialog(context, "Please Select Valid Date Of Birth For LifeAssured");
                commonMethods.setFocusable(btn_bi_smart_insure_wealth_plus_life_assured_date);
                btn_bi_smart_insure_wealth_plus_life_assured_date
                        .requestFocus();

                return false;
            } else if (proposer_date_of_birth.equals("")
                    || proposer_date_of_birth.equalsIgnoreCase("select Date")) {
                commonMethods.showMessageDialog(context, "Please Select Valid Date Of Birth For Proposer");
                commonMethods.setFocusable(btn_bi_smart_insure_wealth_plus_proposer_date);
                btn_bi_smart_insure_wealth_plus_proposer_date
                        .requestFocus();

                return false;
            } else {
                return true;
            }

        } else {
            return false;
        }
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
        } */ else if (!emailId.equals("")) {

            email_id_validation(emailId);
            if (validationFla1 == true) {

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
            if (validationFla1 == true) {

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

    public boolean valLifeAssuredMinorDetail() {
        if (linearlayoutProposerDetails.getVisibility() == View.VISIBLE) {
            if (proposer_Title.equals("") || proposer_First_Name.equals("")
                    || proposer_Last_Name.equals("")) {

                commonMethods.showMessageDialog(context, "Please Fill Name Detail For Proposer");
                if (proposer_Title.equals("")) {
                    spnr_bi_smart_insure_wealth_plus_proposer_title
                            .requestFocus();
                } else if (proposer_First_Name.equals("")) {
                    edt_bi_smart_insure_wealth_plus_proposer_first_name
                            .requestFocus();
                } else {
                    edt_bi_smart_insure_wealth_plus_proposer_last_name
                            .requestFocus();
                }
                return false;
            } else if (proposer_date_of_birth.equals("")
                    || proposer_date_of_birth.equals("Select Date")) {
                commonMethods.showMessageDialog(context, "Please Select Valid DOB For Proposer");
                commonMethods.setFocusable(btn_bi_smart_insure_wealth_plus_proposer_date);
                btn_bi_smart_insure_wealth_plus_proposer_date
                        .requestFocus();
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    public boolean val_proposer_gender() {

        if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("n")) {
            if (gender_proposer.equals("")) {
                commonMethods.dialogWarning(context, "Please Select Proposer Gender", true);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public String getformatedThousandString(int number) {
        String formatedstring = NumberFormat.getNumberInstance(Locale.US)
                .format(number);
        return formatedstring;
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

        // TODO Auto-generated method stub
        if (v.getId() == edt_bi_smart_insure_wealth_plus_life_assured_first_name
                .getId()) {
            // clearFocusable(edt_bi_smart_insure_wealth_plus_life_assured_first_name);
            commonMethods.setFocusable(edt_bi_smart_insure_wealth_plus_life_assured_middle_name);
            edt_bi_smart_insure_wealth_plus_life_assured_middle_name.requestFocus();
        } else if (v.getId() == edt_bi_smart_insure_wealth_plus_life_assured_middle_name
                .getId()) {
            // clearFocusable(edt_bi_smart_insure_wealth_plus_life_assured_middle_name);
            commonMethods.setFocusable(edt_bi_smart_insure_wealth_plus_life_assured_last_name);
            edt_bi_smart_insure_wealth_plus_life_assured_last_name.requestFocus();
        } else if (v.getId() == edt_bi_smart_insure_wealth_plus_life_assured_last_name
                .getId()) {

            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(
                    edt_bi_smart_insure_wealth_plus_life_assured_last_name
                            .getWindowToken(), 0);
            // clearFocusable(edt_bi_smart_insure_wealth_plus_life_assured_last_name);
            commonMethods.setFocusable(btn_bi_smart_insure_wealth_plus_life_assured_date);
            btn_bi_smart_insure_wealth_plus_life_assured_date.requestFocus();
        } else if (v.getId() == edt_bi_smart_insure_wealth_plus_proposer_first_name
                .getId()) {
            // clearFocusable(edt_bi_smart_insure_wealth_plus_proposer_first_name);
            commonMethods.setFocusable(edt_bi_smart_insure_wealth_plus_proposer_middle_name);
            edt_bi_smart_insure_wealth_plus_proposer_middle_name.requestFocus();
        } else if (v.getId() == edt_bi_smart_insure_wealth_plus_proposer_middle_name
                .getId()) {
            // clearFocusable(edt_bi_smart_insure_wealth_plus_proposer_middle_name);
            commonMethods.setFocusable(edt_bi_smart_insure_wealth_plus_proposer_last_name);
            edt_bi_smart_insure_wealth_plus_proposer_last_name.requestFocus();
        } else if (v.getId() == edt_bi_smart_insure_wealth_plus_proposer_last_name
                .getId()) {

            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(
                    edt_bi_smart_insure_wealth_plus_proposer_last_name
                            .getWindowToken(), 0);
            // clearFocusable(edt_bi_smart_insure_wealth_plus_proposer_last_name);
            commonMethods.setFocusable(btn_bi_smart_insure_wealth_plus_proposer_date);
            btn_bi_smart_insure_wealth_plus_proposer_date.requestFocus();
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
            commonMethods.clearFocusable(selPlan);
            commonMethods.setFocusable(selPlan);
            selPlan.requestFocus();
        } else if (v.getId() == premiumAmt.getId()) {
            commonMethods.setFocusable(percent_EquityFund);
            percent_EquityFund.requestFocus();
        } else if (v.getId() == percent_EquityFund.getId()) {
            commonMethods.setFocusable(percent_EquityOptFund);
            percent_EquityOptFund.requestFocus();
        } else if (v.getId() == percent_EquityOptFund.getId()) {
            commonMethods.setFocusable(percent_GrowthFund);
            percent_GrowthFund.requestFocus();
        } else if (v.getId() == percent_GrowthFund.getId()) {
            commonMethods.setFocusable(percent_BalancedFund);
            percent_BalancedFund.requestFocus();
        } else if (v.getId() == percent_BalancedFund.getId()) {
            commonMethods.setFocusable(percent_CorpBondFund);
            percent_CorpBondFund.requestFocus();
        } else if (v.getId() == percent_CorpBondFund.getId()) {
            commonMethods.setFocusable(percent_MidCapFund);
            percent_MidCapFund.requestFocus();
        } else if (v.getId() == percent_MidCapFund.getId()) {
            commonMethods.setFocusable(percent_BondOptimiser);
            percent_BondOptimiser.requestFocus();
        } else if (v.getId() == percent_BondOptimiser.getId()) {
            commonMethods.setFocusable(percent_PureFund);
            percent_PureFund.requestFocus();
        } else if (v.getId() == percent_PureFund.getId()) {
            commonMethods.setFocusable(percent_MoneyMktFund);
            percent_MoneyMktFund.requestFocus();
        } else if (v.getId() == percent_MoneyMktFund.getId()) {
            commonMethods.setFocusable(percent_MoneyMktFund);
            percent_MoneyMktFund.requestFocus();
        }
        return true;

    }


}


