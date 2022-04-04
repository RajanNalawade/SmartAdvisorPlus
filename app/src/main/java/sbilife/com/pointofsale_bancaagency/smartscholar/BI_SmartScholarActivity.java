package sbilife.com.pointofsale_bancaagency.smartscholar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
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
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
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
import sbilife.com.pointofsale_bancaagency.common.DesimalDigitsInputFilter;
import sbilife.com.pointofsale_bancaagency.common.ProductInfo;
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.common.StorageUtils;
import sbilife.com.pointofsale_bancaagency.common.Success;
import sbilife.com.pointofsale_bancaagency.needanalysis.NeedAnalysisActivity;
import sbilife.com.pointofsale_bancaagency.new_bussiness.ProductBIBean;
import sbilife.com.pointofsale_bancaagency.utility.GridHeight;

@SuppressWarnings("deprecation")
public class BI_SmartScholarActivity extends AppCompatActivity implements
        OnEditorActionListener {

    private NeedAnalysisBIService NABIObj;
    private NA_CBI_bean na_cbi_bean;
    private File needAnalysispath;
    private File newFile;
    private String agentcode;
    private String agentMobile;
    private String agentEmail;
    private String userType;
    private int needAnalysis_flag = 0;
    private String na_input = null;
    private String na_output = null;
    private DatabaseHelper dbHelper;

    // UI Elements
    private Spinner spnr_Gender;
    private Spinner spnr_Age;
    private Spinner spnr_bi_smart_scholar_proposer_age;
    private EditText edt_premiumAmount;
    private EditText edt_SAMF;
    private EditText edt_NoOfYearsElapsed;
    private EditText edt_percent_EquityFund;
    private EditText edt_percent_EquityOptimiserFund;
    private EditText edt_percent_GrowthFund;
    private EditText edt_percent_BalancedFund;
    private EditText edt_percent_BondFund;
    private EditText edt_percent_MoneyMarketFund;
    private EditText edt_percent_Top300Fund;

    private TextView tvHelpSAMF;
    private TextView tvHelpPremAmount;
    private TextView tvHelpPremiumTerm;
    private TextView tvHelpPolicyTerm;
    private TextView tvHelpNoOfYearsElapsed;

    // UI Elements
    private CheckBox isStaffDisc;
    private Button btnSubmit;
    private Button btnback;
    // for BI
    private StringBuilder inputVal;
    private String premPayingMode = "";
    private String policyTermStr = "";
    private String proposerageAtEntry = "";

    private SmartScholarBean smartScholarBean;

    // EditText edt_edt_premiumAmount;

    private Spinner spnrPremPayingTerm;
    private Spinner spnrPremPayingMode;
    private Spinner spnrPolicyTerm;

    private RadioButton rb_proposerdetail_personaldetail_backdating_yes;


    private Button btn_proposerdetail_personaldetail_backdatingdate;
    private Button btn_MarketingOfficalDate;
    private Button btn_PolicyholderDate;

    private ImageButton Ibtn_signatureofMarketing;
    private ImageButton Ibtn_signatureofPolicyHolders;


    private Dialog d;
    private final int SIGNATURE_ACTIVITY = 1;
    private String latestImage = "";

    // List used for The Policy Detail Depend on The policy Term
    private List<M_BI_SmartScholarGrid_AdapterCommon> list_data;
    List<M_BI_SmartScholarGrid_AdapterCommon2> list_data1;
    List<M_BI_SmartScholarGrid_AdapterCommon2> list_data2;

    private final int DATE_DIALOG_ID = 1;
    private int DIALOG_ID;
    private int mYear;
    private int mMonth;
    private int mDay;

    private Button btn_bi_smart_scholar_life_assured_date;
    private Button btn_bi_smart_scholar_proposer_date;

    private String QuatationNumber = "";
    // private String currentRecordId = "";
    private String planName = "";
    // private String sr_Code = "";

    private String proposer_Title = "";
    private String proposer_First_Name = "";
    private String proposer_Middle_Name = "";
    private String proposer_Last_Name = "";
    private String product_name = "";

    private String lifeAssured_Title = "";
    private String lifeAssured_First_Name = "";
    private String lifeAssured_Middle_Name = "";
    private String lifeAssured_Last_Name = "";
    private String name_of_life_assured = "";
    private String lifeAssured_date_of_birth = "";
    private String lifeAssuredAge = "";

    private final String proposer_Backdating_WishToBackDate_Policy = "";
    private String proposer_Backdating_BackDate = "";

    // Spinner USed
    private Spinner spnr_bi_smart_scholar_life_assured_title;

    // edit Text Used
    private EditText edt_bi_smart_scholar_life_assured_first_name;
    private EditText edt_bi_smart_scholar_life_assured_middle_name;
    private EditText edt_bi_smart_scholar_life_assured_last_name;

    // Spinner USed
    private Spinner spnr_bi_smart_scholar_proposer_title;

    // edit Text Used
    private EditText edt_bi_smart_scholar_proposer_first_name;
    private EditText edt_bi_smart_scholar_proposer_middle_name;
    private EditText edt_bi_smart_scholar_proposer_last_name;

    private String proposerAge = "";
    private String LifeAssuredAge = "";
    private String proposer_date_of_birth = "";
    private String life_assured_date_of_birth = "";

    // Retrieving value from database and storing
    private String output = "";
    private String input = "";

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
    private SmartScholarProperties prop;

    // Variable Declaration
    private DecimalFormat currencyFormat;
    private AlertDialog.Builder showAlert;

    private String effectivePremium = "0";
    private String PremFreqMode = "";

    private StringBuilder bussIll = null;
    private StringBuilder retVal = null;

    private File mypath;

    String netYield4 = "";
    String netYield8 = "";


    /* String For BI Grid */
    String gender = "";
    private String age_entry = "";
    private String policy_term = "";
    private String sum_assured = "";
    String plan = "";
    private String premium_paying_term = "";
    private String percentage_equity_fund = "";
    private String percentage_equity_optimiser_fund = "";
    private String percentage_growth_fund = "";

    private String percentage_bond_fund = "";
    private String percentage_balanced_fund = "";
    private String percentage_money_market_fund = "";
    private String percentage_top300_fund = "";
    private String no_of_year_elapsed;

    private ScrollView svSmartScholarMain;

    String netYield4Pr = "";
    String netYield8Pr = "";
    String premFreq = "";

    /* Basic Details */

    private EditText edt_proposerdetail_basicdetail_contact_no;
    private EditText edt_proposerdetail_basicdetail_Email_id;
    private EditText edt_proposerdetail_basicdetail_ConfirmEmail_id;
    private EditText edt_bondOptimiserFund2;
    private EditText edt_pureFund;

    private String mobileNo = "";
    private String emailId = "";
    private String ConfirmEmailId = "";
    private boolean validationFla1 = false;
    private String ProposerEmailId = "";
    private Bitmap photoBitmap;

    private String proposer_gender = "";

    private CommonForAllProd obj;
    private String premiumAmount = "";

    private Spinner spnr_bi_smart_scholar_proposer_Gender;
    private TableRow tr_premiumpayingterm;

    private String product_Code, product_UIN, product_cateogory, product_type;
    private String bankUserType = "", mode = "";

    private Context context;

    String flg_needAnalyis = "";

    TableRow tr_smart_platina_assure_kerla_disc;

    private String Check = "";
    private CommonMethods commonMethods;
    private StorageUtils mStorageUtils;
    private ImageButton imageButtonSmartScholarProposerPhotograph;

    private LinearLayout linearlayoutThirdpartySignature,
            linearlayoutAppointeeSignature;
    private ImageButton Ibtn_signatureofThirdParty, Ibtn_signatureofAppointee;
    private String thirdPartySign = "", appointeeSign = "", Company_policy_surrender_dec = "";
    private String percentage_bondOptimiserFund2 = "", percentage_pureFund = "";

    private CheckBox cb_kerladisc;
    private String str_kerla_discount = "No";

    private String str_rate_of_applicable_tax = "";
    private TextView tv_bond_optimiser_fund2;
    private String STR_PLAN_AB = "B";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.bi_smartscholarmain);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        dbHelper = new DatabaseHelper(getApplicationContext());
        context = this;
        commonMethods = new CommonMethods();
        mStorageUtils = new StorageUtils();
        commonMethods.setActionbarLayout(this);

        NABIObj = new NeedAnalysisBIService(this);
        prsObj = new ParseXML();
        Intent intent = getIntent();

        //Added Tushar Kadam Kerla Applicable Tax 7-Jun-2019
        TableRow tablerowKerlaDiscount = findViewById(R.id.tablerowKerlaDiscount);
        cb_kerladisc = findViewById(R.id.cb_kerladisc);
        commonMethods.setKerlaDiscount(context, tablerowKerlaDiscount, cb_kerladisc);
        //End Added Tushar Kadam Kerla Applicable Tax 7-Jun-2019

        String na_flag = intent.getStringExtra("NAFlag");

        if (na_flag != null) {
            if (na_flag.equalsIgnoreCase("1")) {
                needAnalysis_flag = 1;
                na_input = intent.getStringExtra("NaInput");
                na_output = intent.getStringExtra("NaOutput");
                bankUserType = intent.getStringExtra("Other");

                if (bankUserType != null) {

                } else {
                    bankUserType = "";
                }

                try {
                    agentcode = SimpleCrypto.decrypt("SBIL",
                            dbHelper.GetUserCode());

                    agentMobile = SimpleCrypto.decrypt("SBIL",
                            dbHelper.GetMobileNo());
                    agentEmail = SimpleCrypto.decrypt("SBIL",
                            dbHelper.GetEmailId());
                    userType = SimpleCrypto.decrypt("SBIL",
                            dbHelper.GetUserType());

                    ProductInfo prodInfoObj = new ProductInfo();
                    planName = "Smart Scholar";
                    product_Code = prodInfoObj.getProductCode(planName);
                    product_UIN = prodInfoObj.getProductUIN(planName);
                    product_cateogory = prodInfoObj
                            .getProductCategory(planName);
                    product_type = prodInfoObj.getProductType(planName);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
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
        // ProductHomePageActivity.path.setText("Benefit Illustrator");

        initialiseDate();
        // ProductHomePageActivity.path.setText("Benefit Illustrator");
        obj = new CommonForAllProd();

        prsObj = new ParseXML();

        commonForAllProd = new CommonForAllProd();
        prop = new SmartScholarProperties();

        list_data = new ArrayList<M_BI_SmartScholarGrid_AdapterCommon>();
        list_data1 = new ArrayList<M_BI_SmartScholarGrid_AdapterCommon2>();
        list_data2 = new ArrayList<M_BI_SmartScholarGrid_AdapterCommon2>();

        svSmartScholarMain = findViewById(R.id.sv_bi_smart_scholar_main);

        btn_bi_smart_scholar_life_assured_date = findViewById(R.id.btn_bi_smart_scholar_life_assured_date);
        btn_bi_smart_scholar_proposer_date = findViewById(R.id.btn_bi_smart_scholar_proposer_date);

        spnr_bi_smart_scholar_life_assured_title = findViewById(R.id.spnr_bi_smart_scholar_life_assured_title);
        spnr_bi_smart_scholar_proposer_title = findViewById(R.id.spnr_bi_smart_scholar_proposer_title);

        edt_bi_smart_scholar_life_assured_first_name = findViewById(R.id.edt_bi_smart_scholar_life_assured_first_name);
        edt_bi_smart_scholar_life_assured_middle_name = findViewById(R.id.edt_bi_smart_scholar_life_assured_middle_name);
        edt_bi_smart_scholar_life_assured_last_name = findViewById(R.id.edt_bi_smart_scholar_life_assured_last_name);

        edt_bi_smart_scholar_proposer_first_name = findViewById(R.id.edt_bi_smart_scholar_proposer_first_name);
        edt_bi_smart_scholar_proposer_middle_name = findViewById(R.id.edt_bi_smart_scholar_proposer_middle_name);
        edt_bi_smart_scholar_proposer_last_name = findViewById(R.id.edt_bi_smart_scholar_proposer_last_name);

        spnrPremPayingTerm = findViewById(R.id.spnr_bi_smart_scholar_paying_term_option);
        spnrPremPayingMode = findViewById(R.id.spnr_bi_smart_scholar_premium_paying_mode);
        spnrPolicyTerm = findViewById(R.id.spnr_bi_smart_scholar_policyterm);
        spnr_Gender = findViewById(R.id.spnr_bi_smart_scholar_selGender);
//        spnr_Gender.setClickable(false);
//        spnr_Gender.setEnabled(false);

        spnr_bi_smart_scholar_proposer_Gender = findViewById(R.id.spnr_bi_smart_scholar_proposer_Gender);
//        spnr_bi_smart_scholar_proposer_Gender.setClickable(false);
//        spnr_bi_smart_scholar_proposer_Gender.setEnabled(false);

        spnr_Age = findViewById(R.id.spnr_bi_smart_scholar_life_assured_age);
        spnr_Age.setEnabled(false);

        spnr_bi_smart_scholar_proposer_age = findViewById(R.id.spnr_bi_smart_scholar_proposer_age);
        spnr_bi_smart_scholar_proposer_age.setEnabled(false);

        edt_proposerdetail_basicdetail_contact_no = findViewById(R.id.edt_smart_scholar_contact_no);
        edt_proposerdetail_basicdetail_Email_id = findViewById(R.id.edt_smart_scholar_Email_id);
        edt_proposerdetail_basicdetail_ConfirmEmail_id = findViewById(R.id.edt_smart_scholar_ConfirmEmail_id);

        // Calculate premium
        btnSubmit = findViewById(R.id.btn_bi_smart_scholar_btnSubmit);
        btnback = findViewById(R.id.btn_bi_smart_scholar_btnback);

        // Sum Assured
        edt_premiumAmount = findViewById(R.id.edt_bi_smart_scholar_premium_amt);
        edt_percent_EquityFund = findViewById(R.id.edt_bi_smart_scholar_equity_fund);
        edt_percent_EquityOptimiserFund = findViewById(R.id.edt_bi_smart_scholar_equity_optimiser_fund);
        edt_percent_GrowthFund = findViewById(R.id.edt_bi_smart_scholar_growth_fund);
        edt_percent_BalancedFund = findViewById(R.id.edt_bi_smart_scholar_balancedfund);
        edt_percent_BondFund = findViewById(R.id.edt_bi_smart_scholar_bondfund);
        edt_percent_MoneyMarketFund = findViewById(R.id.edt_bi_smart_scholar_moneymarketfund);
        edt_percent_Top300Fund = findViewById(R.id.edt_bi_smart_scholar_top300_fund);
        edt_bondOptimiserFund2 = (EditText) findViewById(R.id.edt_bondOptimiserFund2);
        edt_pureFund = (EditText) findViewById(R.id.edt_pureFund);
        edt_SAMF = findViewById(R.id.edt_bi_smart_scholar_samf);
        edt_NoOfYearsElapsed = findViewById(R.id.edt_bi_smart_scholar_years_elapsed_since_inception);
        isStaffDisc = findViewById(R.id.cb_staffdisc);
        tvHelpSAMF = findViewById(R.id.help_samf);
        tvHelpPremAmount = findViewById(R.id.help_prem_amt);
        tvHelpNoOfYearsElapsed = findViewById(R.id.help_years_elapsed_since_inception);
        tvHelpPremiumTerm = findViewById(R.id.help_premium_term);
        tvHelpPolicyTerm = findViewById(R.id.help_policy_term);
        tr_premiumpayingterm = findViewById(R.id.tr_premiumpayingterm);


        tr_smart_platina_assure_kerla_disc = (TableRow) findViewById(R.id.tablerowKerlaDiscount);
        cb_kerladisc = (CheckBox) findViewById(R.id.cb_kerladisc);
        tv_bond_optimiser_fund2 = (TextView) findViewById(R.id.tv_bond_optimiser_fund2);

        if (STR_PLAN_AB.equals("A")) {
            tv_bond_optimiser_fund2.setText("% to be invested for Bond Optimiser Fund II");
        } else if (STR_PLAN_AB.equals("B")) {
            tv_bond_optimiser_fund2.setText("% to be invested for Bond Optimiser Fund");
        }

        // Variable Declaration
        currencyFormat = new DecimalFormat("##,##,##,###");
        showAlert = new AlertDialog.Builder(this);


        // Age
        String[] childAgeList = new String[18];
        for (int i = 0; i <= 17; i++) {
            childAgeList[i] = i + "";
        }
        ArrayAdapter<String> childAgeAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, childAgeList);
        childAgeAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_Age.setAdapter(childAgeAdapter);
        childAgeAdapter.notifyDataSetChanged();

        // Gender
        String[] genderList = {"Male", "Female", "Third Gender"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(getApplicationContext(),
                R.layout.spinner_item, genderList);
        genderAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_Gender.setAdapter(genderAdapter);
        spnr_bi_smart_scholar_proposer_Gender.setAdapter(genderAdapter);
        genderAdapter.notifyDataSetChanged();

        // Propose Age
        String[] proposerAgeList = new String[40];
        for (int i = 18; i <= 57; i++) {
            proposerAgeList[i - 18] = i + "";
        }
        ArrayAdapter<String> proposerAgeAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, proposerAgeList);
        proposerAgeAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_bi_smart_scholar_proposer_age.setAdapter(proposerAgeAdapter);
        proposerAgeAdapter.notifyDataSetChanged();

        // Policy Term
        String[] policyTermList = new String[18];
        for (int i = 8; i <= 25; i++) {
            policyTermList[i - 8] = i + "";
        }
        ArrayAdapter<String> policyTermAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, policyTermList);
        policyTermAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnrPolicyTerm.setAdapter(policyTermAdapter);
        policyTermAdapter.notifyDataSetChanged();

        String[] premiumTermList = new String[21];
        for (int i = 5; i <= 25; i++) {
            premiumTermList[i - 5] = i + "";
        }
        ArrayAdapter<String> premiumTermAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, premiumTermList);
        premiumTermAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnrPremPayingTerm.setAdapter(premiumTermAdapter);
        premiumTermAdapter.notifyDataSetChanged();

        // premium Frequency Mode
        String[] premiumFrequencyModeList = {"Single", "Yearly",
                "Half Yearly", "Quarterly", "Monthly"};
        ArrayAdapter<String> premiumFrequencyModeAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item,
                premiumFrequencyModeList);
        premiumFrequencyModeAdapter
                .setDropDownViewResource(R.layout.spinner_item1);
        spnrPremPayingMode.setAdapter(premiumFrequencyModeAdapter);
        premiumFrequencyModeAdapter.notifyDataSetChanged();

        // Premium Paying Term
        // txt_premPayingTerm =
        // (TextView)findViewById(R.id.txt_premiumpayingterm);

        commonMethods.fillSpinnerValue(context, spnr_bi_smart_scholar_life_assured_title,
                Arrays.asList(getResources().getStringArray(R.array.arr_select_title)));

        commonMethods.fillSpinnerValue(context, spnr_bi_smart_scholar_proposer_title,
                Arrays.asList(getResources().getStringArray(R.array.arr_select_title)));

        // SAMF
        edt_SAMF.setFilters(new InputFilter[]{new DesimalDigitsInputFilter(2)});

        // Funds

        /*
         * //help declaration help_SAMF =
         * (TextView)findViewById(R.id.help_samf); tvHelpPremAmount =
         * (TextView)findViewById(R.id.help_prem_amt); tvHelpNoOfYearsElapsed =
         * (TextView) findViewById(R.id.help_years_elapsed_since_inception);
         * help_policyTerm = (TextView)findViewById(R.id.help_policyterm);
         * tvHelpPremiumTerm = (TextView)findViewById(R.id.tvHelpPremiumTerm);
         */


        /********************* Item Listener starts from here ******************************************/

        // modified by vrushali chaudhari

        // setBIInputGui();

        edt_premiumAmount.setOnEditorActionListener(this);
        edt_percent_EquityFund.setOnEditorActionListener(this);
        edt_percent_EquityOptimiserFund.setOnEditorActionListener(this);
        edt_percent_GrowthFund.setOnEditorActionListener(this);
        edt_percent_BalancedFund.setOnEditorActionListener(this);
        edt_percent_BondFund.setOnEditorActionListener(this);
        edt_percent_MoneyMarketFund.setOnEditorActionListener(this);
        edt_percent_Top300Fund.setOnEditorActionListener(this);

        edt_bondOptimiserFund2.setOnEditorActionListener(this);
        edt_pureFund.setOnEditorActionListener(this);

        edt_SAMF.setOnEditorActionListener(this);
        edt_NoOfYearsElapsed.setOnEditorActionListener(this);
        edt_bi_smart_scholar_life_assured_first_name
                .setOnEditorActionListener(this);
        edt_bi_smart_scholar_life_assured_middle_name
                .setOnEditorActionListener(this);
        edt_bi_smart_scholar_life_assured_last_name
                .setOnEditorActionListener(this);

        edt_bi_smart_scholar_proposer_first_name
                .setOnEditorActionListener(this);
        edt_bi_smart_scholar_proposer_middle_name
                .setOnEditorActionListener(this);
        edt_bi_smart_scholar_proposer_last_name.setOnEditorActionListener(this);
        setFocusable(spnr_bi_smart_scholar_life_assured_title);

        spnr_bi_smart_scholar_life_assured_title.requestFocus();

        setSpinnerAndOterListner();

        edt_proposerdetail_basicdetail_contact_no
                .addTextChangedListener(new TextWatcher() {

                    public void onTextChanged(CharSequence arg0, int arg1,
                                              int arg2, int arg3) {
                        // TODO Auto-generated method stub

                    }

                    public void beforeTextChanged(CharSequence arg0, int arg1,
                                                  int arg2, int arg3) {
                        // TODO Auto-generated method stub

                    }

                    public void afterTextChanged(Editable arg0) {
                        // TODO Auto-generated method stub
                        String abc = edt_proposerdetail_basicdetail_contact_no
                                .getText().toString();
                        mobile_validation(abc);

                    }
                });

        edt_proposerdetail_basicdetail_Email_id
                .addTextChangedListener(new TextWatcher() {

                    public void onTextChanged(CharSequence arg0, int arg1,
                                              int arg2, int arg3) {
                        // TODO Auto-generated method stub

                    }

                    public void beforeTextChanged(CharSequence arg0, int arg1,
                                                  int arg2, int arg3) {
                        // TODO Auto-generated method stub

                    }

                    public void afterTextChanged(Editable arg0) {
                        // TODO Auto-generated method stub
                        ProposerEmailId = edt_proposerdetail_basicdetail_Email_id
                                .getText().toString();
                        //email_id_validation(ProposerEmailId);

                    }
                });

        edt_proposerdetail_basicdetail_ConfirmEmail_id
                .addTextChangedListener(new TextWatcher() {

                    public void onTextChanged(CharSequence arg0, int arg1,
                                              int arg2, int arg3) {
                        // TODO Auto-generated method stub

                    }

                    public void beforeTextChanged(CharSequence arg0, int arg1,
                                                  int arg2, int arg3) {
                        // TODO Auto-generated method stub

                    }

                    public void afterTextChanged(Editable arg0) {
                        // TODO Auto-generated method stub
                        String proposer_confirm_emailId = edt_proposerdetail_basicdetail_ConfirmEmail_id
                                .getText().toString();
                        //confirming_email_id(proposer_confirm_emailId);

                    }
                });

        edt_proposerdetail_basicdetail_contact_no
                .setOnEditorActionListener(this);
        edt_proposerdetail_basicdetail_Email_id.setOnEditorActionListener(this);
        edt_proposerdetail_basicdetail_ConfirmEmail_id
                .setOnEditorActionListener(this);
        // getBasicDetail();
        edt_NoOfYearsElapsed.setText("8");

        TableRow tr_staff_disc = findViewById(R.id.tr_smart_scholar_staff_disc);

        String str_usertype = "";
        try {
            str_usertype = SimpleCrypto.decrypt("SBIL",
                    dbHelper.GetUserType());

        } catch (Exception e) {
            e.printStackTrace();
        }
		/*if (str_usertype.equalsIgnoreCase("BAP")
				|| str_usertype.equalsIgnoreCase("CAG")) {
			tr_staff_disc.setVisibility(View.GONE);
		}*/
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        boolean flagFocus = true;
        if (!flagFocus) {
            svSmartScholarMain.requestFocus();
        }

    }

    private void setSpinnerAndOterListner() {
        // TODO Auto-generated method stub
        // For Staff Discount
        isStaffDisc.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // if((isStaffDisc.isChecked()) &&
                if ((isStaffDisc.isChecked())) {
                    isStaffDisc.setChecked(true);

                }
                clearFocusable(isStaffDisc);
                clearFocusable(spnr_bi_smart_scholar_life_assured_title);
                setFocusable(spnr_bi_smart_scholar_life_assured_title);
                spnr_bi_smart_scholar_life_assured_title.requestFocus();
            }
        });


        cb_kerladisc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_kerladisc.isChecked()) {
                    str_kerla_discount = "Yes";
                    clearFocusable(cb_kerladisc);
                    setFocusable(spnr_bi_smart_scholar_life_assured_title);
                    spnr_bi_smart_scholar_life_assured_title.requestFocus();
                } else {
                    str_kerla_discount = "No";
                    clearFocusable(cb_kerladisc);
                    setFocusable(spnr_bi_smart_scholar_life_assured_title);
                    spnr_bi_smart_scholar_life_assured_title.requestFocus();
                }
            }
        });

        // Policy Term
        spnrPolicyTerm.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
                                       long id) {
                updatePremPayingTermLabel();
                updateSAMFlabel();
                updatenoOfYearsElapsedSinceInception();

                clearFocusable(spnrPolicyTerm);
                setFocusable(spnrPremPayingMode);
                spnrPremPayingMode.requestFocus();
                edt_NoOfYearsElapsed.setText(spnrPolicyTerm.getSelectedItem()
                        .toString());
                // removed by vrushali chaudhari
                // noOfYearsElapsedSinceInception.setText(spnrPolicyTerm.getSelectedItem().toString());
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        // Premium frequency mode
        spnrPremPayingMode
                .setOnItemSelectedListener(new OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int pos, long id) {

                        updatePremPayingTermLabel();
                        updateedt_premiumAmountLabel();
                        updateSAMFlabel();

                        if (pos == 0) {
                            spnrPremPayingTerm.setEnabled(false);
                            clearFocusable(spnrPremPayingMode);
                            setFocusable(edt_bi_smart_scholar_life_assured_first_name);
                            edt_bi_smart_scholar_life_assured_first_name
                                    .requestFocus();
                            tr_premiumpayingterm.setVisibility(View.GONE);
                        } else {
                            tr_premiumpayingterm.setVisibility(View.VISIBLE);
                            spnrPremPayingTerm.setEnabled(true);
                            clearFocusable(spnrPremPayingMode);
                            clearFocusable(spnrPremPayingTerm);
                            setFocusable(spnrPremPayingTerm);
                            spnrPremPayingTerm.requestFocus();
                        }

                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });

        spnrPremPayingTerm
                .setOnItemSelectedListener(new OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int pos, long id) {

                        updateedt_premiumAmountLabel();
                        updateSAMFlabel();

                        clearFocusable(spnrPremPayingTerm);

                        // changes done by amit - 25-3-2015
                        setFocusable(edt_bi_smart_scholar_life_assured_first_name);
                        edt_bi_smart_scholar_life_assured_first_name
                                .requestFocus();

                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });

        spnr_bi_smart_scholar_life_assured_title
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long id) {
                        // TODO Auto-generated method stub
                        if (position == 0) {
                            lifeAssured_Title = "";
                        } else {
                            lifeAssured_Title = spnr_bi_smart_scholar_life_assured_title
                                    .getSelectedItem().toString();
                          /*  if (lifeAssured_Title.equalsIgnoreCase("Mr.")) {
								spnr_Gender
										.setSelection(
												getIndex(
														spnr_bi_smart_scholar_life_assured_title,
														"Male"), false);
							} else if (lifeAssured_Title
									.equalsIgnoreCase("Ms.")) {
								spnr_Gender.setSelection(
										getIndex(spnr_Gender, "Female"), false);
							} else if (lifeAssured_Title
									.equalsIgnoreCase("Mrs.")) {
								spnr_Gender.setSelection(
										getIndex(spnr_Gender, "Female"), false);
                            }*/
                            clearFocusable(spnr_bi_smart_scholar_life_assured_title);
                            // changes done by amit - 25-3-2015
                            setFocusable(edt_bi_smart_scholar_life_assured_first_name);

                            edt_bi_smart_scholar_life_assured_first_name
                                    .requestFocus();
                        }

                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        spnr_bi_smart_scholar_proposer_title
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long id) {
                        // TODO Auto-generated method stub
                        if (position == 0) {
                            proposer_Title = "";
                        } else {
                            proposer_Title = spnr_bi_smart_scholar_proposer_title
                                    .getSelectedItem().toString();

                           /* if (proposer_Title.equalsIgnoreCase("Mr.")) {
								spnr_bi_smart_scholar_proposer_Gender
										.setSelection(
												getIndex(
														spnr_bi_smart_scholar_proposer_Gender,
														"Male"), false);
							} else if (proposer_Title.equalsIgnoreCase("Ms.")) {
								spnr_bi_smart_scholar_proposer_Gender
										.setSelection(
												getIndex(
														spnr_bi_smart_scholar_proposer_Gender,
														"Female"), false);
							} else if (proposer_Title.equalsIgnoreCase("Mrs.")) {
								spnr_bi_smart_scholar_proposer_Gender
										.setSelection(
												getIndex(
														spnr_bi_smart_scholar_proposer_Gender,
														"Female"), false);
                            }*/

                            clearFocusable(spnr_bi_smart_scholar_proposer_title);

                            setFocusable(edt_bi_smart_scholar_proposer_first_name);

                            edt_bi_smart_scholar_proposer_first_name
                                    .requestFocus();
                        }

                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        // Premium Amount
        edt_premiumAmount
                .setOnFocusChangeListener(new View.OnFocusChangeListener() {

                    public void onFocusChange(View arg0, boolean arg1) {
                        // TODO Auto-generated method stub

                        if (!edt_premiumAmount.getText().toString().equals("")) {
                            effectivePremium = getEffectivePremium();
                            updateSAMFlabel();
                        }
                    }
                });


        // Go Home Button
        btnback.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                setResult(RESULT_OK);
                finish();
            }
        });

        // Calculate premium
        btnSubmit.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {


                gender = spnr_Gender.getSelectedItem().toString();
                proposer_gender = spnr_bi_smart_scholar_proposer_Gender.getSelectedItem().toString();

                inputVal = new StringBuilder();
                retVal = new StringBuilder();
                bussIll = new StringBuilder();

                proposer_First_Name = edt_bi_smart_scholar_proposer_first_name
                        .getText().toString();
                proposer_Middle_Name = edt_bi_smart_scholar_proposer_middle_name
                        .getText().toString();
                proposer_Last_Name = edt_bi_smart_scholar_proposer_last_name
                        .getText().toString();

                name_of_proposer = proposer_Title + " " + proposer_First_Name
                        + " " + proposer_Middle_Name + " " + proposer_Last_Name;

                lifeAssured_First_Name = edt_bi_smart_scholar_life_assured_first_name
                        .getText().toString();
                lifeAssured_Middle_Name = edt_bi_smart_scholar_life_assured_middle_name
                        .getText().toString();
                lifeAssured_Last_Name = edt_bi_smart_scholar_life_assured_last_name
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
                        && valBasicDetail() && validateMaturityAge()
                        && valPolicyTerm() && valPremiumPayingTerm()
                        && valedt_premiumAmount() && valSAMF()
                        && valYearsElapsedSinceInception()
                        && valTotalAllocation()) {
                    Date();

                    addListenerOnSubmit();
                    getInput(smartScholarBean);
                    // insertDataIntoDatabase();
                    if (needAnalysis_flag == 0) {
                        Intent i = new Intent(getApplicationContext(),
                                Success.class);

                        i.putExtra("ProductName",
                                "Product : SBI Life - Smart Scholar (UIN:111L073V03)");

                        i.putExtra(
                                "op",
                                "Sum Assured is Rs. "
                                        + currencyFormat.format(Double
                                        .parseDouble(prsObj
                                                .parseXmlTag(retVal
                                                                .toString(),
                                                        "sumAssured"))));
                        i.putExtra(
                                "op1",
                                "Fund Value @ 4% is Rs. "
                                        + currencyFormat.format(Double
                                        .parseDouble(prsObj
                                                .parseXmlTag(retVal
                                                                .toString(),
                                                        "fundVal4"))));
                        i.putExtra(
                                "op2",
                                "Fund Value @ 8% is Rs. "
                                        + currencyFormat.format(Double
                                        .parseDouble(prsObj
                                                .parseXmlTag(retVal
                                                                .toString(),
                                                        "fundVal8"))));
                        if (spnrPremPayingMode.getSelectedItem().toString()
                                .equals("Single"))
                            i.putExtra(
                                    "op3",
                                    "Single Premium (SP) is Rs. "
                                            + currencyFormat.format(Double.parseDouble(prsObj
                                            .parseXmlTag(
                                                    retVal.toString(),
                                                    "annPrem"))));
                        else
                            i.putExtra(
                                    "op3",
                                    "Annualised Premium (LPPT) is Rs. "
                                            + currencyFormat.format(Double.parseDouble(prsObj
                                            .parseXmlTag(
                                                    retVal.toString(),
                                                    "annPrem"))));

                        i.putExtra("header", "SBI Life - Smart Scholar");
                        i.putExtra("header1", "(UIN:111L073V03)");
                        startActivity(i);
                    } else
                        Dialog();

                }
            }

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

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

                    } else if (latestImage.equalsIgnoreCase("thirdParty")) {
                        Ibtn_signatureofThirdParty
                                .setImageBitmap(ProposerCaptureSignature.scaled);

                        Bitmap signature = ProposerCaptureSignature.scaled;
                        if (signature != null) {
                            ByteArrayOutputStream out = new ByteArrayOutputStream();
                            signature.compress(Bitmap.CompressFormat.PNG, 100,
                                    out);
                            byte[] signByteArray = out.toByteArray();
                            thirdPartySign = Base64.encodeToString(
                                    signByteArray, Base64.DEFAULT);
                        }
                    } else if (latestImage.equalsIgnoreCase("Appointee")) {
                        Ibtn_signatureofAppointee
                                .setImageBitmap(ProposerCaptureSignature.scaled);

                        Bitmap signature = ProposerCaptureSignature.scaled;
                        if (signature != null) {
                            ByteArrayOutputStream out = new ByteArrayOutputStream();
                            signature.compress(Bitmap.CompressFormat.PNG, 100,
                                    out);
                            byte[] signByteArray = out.toByteArray();
                            appointeeSign = Base64.encodeToString(
                                    signByteArray, Base64.DEFAULT);
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
                    Uri imageUri;
                    try {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            imageUri = commonMethods.getContentUri(context, new File(Photo.toString()));
                        } else {
                            imageUri = Uri.fromFile(new File(Photo.toString()));
                        }

                        b = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
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
                        imageButtonSmartScholarProposerPhotograph
                                .setImageBitmap(scaled);
                    } else {
                        photoBitmap = null;
                    }
                }
            }
        }
    }

    private void Dialog() {

        final Dialog d = new Dialog(this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.BLACK));

        d.setContentView(R.layout.layout_smart_scholar_bi_grid_common);

        TextView tv_guaranted_death = d
                .findViewById(R.id.tv_guaranted_death);

        TextView rate_of_applicable_taxes = (TextView) d
                .findViewById(R.id.rate_of_applicable_taxes);
        if (cb_kerladisc.isChecked()) {
            str_rate_of_applicable_tax = "19 %";
            rate_of_applicable_taxes.setText(str_rate_of_applicable_tax);
        } else {
            str_rate_of_applicable_tax = "18 %";
            rate_of_applicable_taxes.setText(str_rate_of_applicable_tax);
        }

        final TextView tv_proposername = (TextView) d
                .findViewById(R.id.tv_proposername);

        TextView tv_bi_smart_scholar_porposer_name = (TextView) d
                .findViewById(R.id.tv_bi_smart_scholar_porposer_name);
        TextView tv_bi_smart_scholar_life_assured_name = (TextView) d
                .findViewById(R.id.tv_bi_smart_scholar_life_assured_name);

        final TextView tv_proposal_number = d
                .findViewById(R.id.tv_proposal_number);
        tv_proposal_number.setText(QuatationNumber);

        TextView tv_channel_intermediary = (TextView) d
                .findViewById(R.id.tv_channel_intermediary);

        tv_channel_intermediary.setText(userType);

        TextView tv_bi_smart_scholar_proposer_age_entry = d
                .findViewById(R.id.tv_bi_smart_scholar_porposer_age_entry);
        TextView tv_bi_smart_scholar_proposer_maturity_age = d
                .findViewById(R.id.tv_bi_smart_scholar_proposer_maturity_age);

        TextView tv_bi_smart_scholar_age_entry = d
                .findViewById(R.id.tv_bi_smart_scholar_age_entry);
        TextView tv_bi_smart_scholar_maturity_age = d
                .findViewById(R.id.tv_bi_smart_scholar_maturity_age);
        TextView tv_bi_smart_scholar_life_assured_gender = d
                .findViewById(R.id.tv_bi_smart_scholar_life_assured_gender);
        TextView tv_bi_smart_scholar_policy_term = d
                .findViewById(R.id.tv_bi_smart_scholar_policy_term);
        TextView tv_bi_smart_scholar_annualised_premium = d
                .findViewById(R.id.tv_bi_smart_scholar_annualised_premium);
        TextView tv_bi_smart_scholar_sum_assured = d
                .findViewById(R.id.tv_bi_smart_scholar_sum_assured);
        TextView tv_bi_smart_scholar_premium_paying_term = d
                .findViewById(R.id.tv_bi_smart_scholar_premium_paying_term);
        TextView tv_bi_smart_scholar_accidental_benefit_sum_assured = d
                .findViewById(R.id.tv_bi_smart_scholar_accidental_benefit_sum_assured);
        TextView tv_bi_smart_scholar_yearly_premium = d
                .findViewById(R.id.tv_bi_smart_scholar_yearly_premium);

        TextView tv_smart_scholar_equity_fund_allocation = d
                .findViewById(R.id.tv_smart_scholar_equity_fund_allocation);
        TextView tv_smart_scholar_equity_fund_fmc = d
                .findViewById(R.id.tv_smart_scholar_equity_fund_fmc);

        TextView tv_smart_scholar_equity_optimiser_fund_allocation = d
                .findViewById(R.id.tv_smart_scholar_equity_optimiser_fund_allocation);
        TextView tv_smart_scholar_equity_optimiser_fund_fmc = d
                .findViewById(R.id.tv_smart_scholar_equity_optimiser_fund_fmc);

        TextView tv_smart_scholar_growth_fund_allocation = d
                .findViewById(R.id.tv_smart_scholar_growth_fund_allocation);
        TextView tv_smart_scholar_growth_fund_fmc = d
                .findViewById(R.id.tv_smart_scholar_growth_fund_fmc);

        TextView tv_smart_scholar_balanced_fund_allocation = d
                .findViewById(R.id.tv_smart_scholar_balanced_fund_allocation);
        TextView tv_smart_scholar_balanced_fund_fmc = d
                .findViewById(R.id.tv_smart_scholar_balanced_fund_fmc);
        TextView tv_smart_scholar_bond_fund_allocation = d
                .findViewById(R.id.tv_smart_scholar_bond_fund_allocation);
        TextView tv_smart_scholar_bond_plan_fmc = d
                .findViewById(R.id.tv_smart_scholar_bond_plan_fmc);
        TextView tv_smart_scholar_market_fund_allocation = d
                .findViewById(R.id.tv_smart_scholar_market_fund_allocation);
        TextView tv_smart_scholar_market_fund_fmc = d
                .findViewById(R.id.tv_smart_scholar_market_fund_fmc);

        TextView tv_smart_scholar_top300_fund_allocation = (TextView) d
                .findViewById(R.id.tv_smart_scholar_top300_fund_allocation);


        TextView tv_smart_scholer_optimiser_fund2_allocation = (TextView) d
                .findViewById(R.id.tv_smart_scholer_optimiser_fund2_allocation);

        TextView tv_smart_scholer_optimiser_fund2_fmc = (TextView) d
                .findViewById(R.id.tv_smart_scholer_optimiser_fund2_fmc);
        TextView tv_smart_scholer_pure_fund_allocation = (TextView) d
                .findViewById(R.id.tv_smart_scholer_pure_fund_allocation);

        TextView tv_smart_scholer_pure_fund_fmc = (TextView) d
                .findViewById(R.id.tv_smart_scholer_pure_fund_fmc);


        TextView tv_smart_scholar_top300_fund_fmc = (TextView) d
                .findViewById(R.id.tv_smart_scholar_top300_fund_fmc);

        TextView tv_smart_scholar_no_of_years_elapsed = d
                .findViewById(R.id.tv_smart_scholar_no_of_years_elapsed);
        TextView tv_smart_scholar_reduction_yield = d
                .findViewById(R.id.tv_smart_scholar_reduction_yield);
        TextView tv_smart_scholar_maturity_age2 = d
                .findViewById(R.id.tv_smart_scholar_maturity_age2);
        TextView tv_smart_scholar_reduction_yield2 = d
                .findViewById(R.id.tv_smart_scholar_reduction_yeild2);

        TextView tv_bi_is_Staff = d
                .findViewById(R.id.tv_bi_is_Staff);
        TableRow txt_submit_proof = (TableRow) d
                .findViewById(R.id.txt_submit_proof);


        TextView tv_smart_scholar_sbi_life_details = d
                .findViewById(R.id.tv_smart_scholar_sbi_life_details);

        final EditText edt_Policyholderplace = d
                .findViewById(R.id.edt_Policyholderplace);

        final TextView edt_proposer_name = d
                .findViewById(R.id.edt_ProposalName);

        final CheckBox cb_statement = d
                .findViewById(R.id.cb_statement);
        cb_statement.setChecked(false);

        /* Need Analysis */
        final TextView edt_proposer_name_need_analysis = d
                .findViewById(R.id.edt_proposer_name_need_analysis);

        final CheckBox cb_statement_need_analysis = d
                .findViewById(R.id.cb_statement_need_analysis);
        cb_statement_need_analysis.setChecked(true);
        TableRow tr_need_analysis = d
                .findViewById(R.id.tr_need_analysis);
        final CheckBox checkboxAgentStatement = d.findViewById(R.id.checkboxAgentStatement);
        checkboxAgentStatement.setChecked(false);

        TableRow tr_submit_id_proof = (TableRow) d
                .findViewById(R.id.txt_submit_proof);

        TextView tv_bond_optimiser_fund2_dialog = (TextView) d.findViewById(R.id.tv_bond_optimiser_fund2_dialog);

        if (STR_PLAN_AB.equals("A")) {
            tv_bond_optimiser_fund2_dialog.setText("Bond Optimiser Fund II (SFIN : ULIF037160919BONDOPFND2111)");
        } else if (STR_PLAN_AB.equals("B")) {
            tv_bond_optimiser_fund2_dialog.setText("Bond Optimiser Fund (SFIN : ULIF032290618BONDOPTFND111)");
        }


        if (NeedAnalysisActivity.str_need_analysis != null) {
            if (NeedAnalysisActivity.str_need_analysis.equals("1")) {

                File mypath_old = mStorageUtils.createFileToAppSpecificDir(context, "NA" + ".pdf");
                File mypath_new = mStorageUtils.createFileToAppSpecificDir(context,
                        QuatationNumber + "_NA" + ".pdf");
                if (mypath_old.exists()) {
                    mypath_old.renameTo(mypath_new);
                }
                flg_needAnalyis = "1";
                tr_need_analysis.setVisibility(View.VISIBLE);
            } else {
                cb_statement_need_analysis.setChecked(true);
                tr_need_analysis.setVisibility(View.GONE);
            }

        }

        TextView tv_accidental_death_benefit = d
                .findViewById(R.id.tv_accidental_death_benefit);
        // TextView tv_accidental_total_and_permanent_death_benefit = (TextView)
        // d
        // .findViewById(R.id.tv_accidental_total_and_permanent_death_benefit);

        TextView tv_smart_scholar_premium_tag = (TextView) d
                .findViewById(R.id.tv_smart_scholar_premium_tag);

        TextView tv_bi_smart_scholar_plan_detail_premium_tag = d
                .findViewById(R.id.tv_bi_smart_scholar_plan_detail_premium_tag);

        TextView tv_smart_scholar_net_yield_8 = d
                .findViewById(R.id.tv_smart_scholar_net_yield_8);

        TextView tv_smart_scholar_net_yield_4 = d
                .findViewById(R.id.tv_smart_scholar_net_yield_4);

        Button btn_proceed = d.findViewById(R.id.btn_proceed);

        btn_MarketingOfficalDate = d
                .findViewById(R.id.btn_MarketingOfficalDate);
        btn_PolicyholderDate = d
                .findViewById(R.id.btn_PolicyholderDate);

        Ibtn_signatureofMarketing = d
                .findViewById(R.id.Ibtn_signatureofMarketing);
        Ibtn_signatureofPolicyHolders = d
                .findViewById(R.id.Ibtn_signatureofPolicyHolders);

        list_data.clear();
        list_data1.clear();
        list_data2.clear();

        // getValueFromDatabase();

//        premiumAmount = ((int) Double.parseDouble(prsObj.parseXmlTag(input, "premiumAmount"))) + "";
//
//        if (Double.parseDouble(premiumAmount) > 100000) {
//            tr_submit_id_proof.setVisibility(View.VISIBLE);
//        } else {
//            tr_submit_id_proof.setVisibility(View.GONE);
//        }

        if (!proposer_sign.equals("")) {
            /***** Added by Akshaya on 19-MAR-15 end */String flg_needAnalyis = "";
            if (flg_needAnalyis.equals("1")) {
                tr_need_analysis.setVisibility(View.VISIBLE);
            } else {
                cb_statement_need_analysis.setChecked(true);
                tr_need_analysis.setVisibility(View.GONE);
            }
        }
        if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {
            edt_proposer_name
                    .setText("I, "
                            + name_of_life_assured
                            + ", have received the information with respect to the above and have understood the above statement before entering into a contract.");
            edt_proposer_name_need_analysis
                    .setText("I, "
                            + name_of_life_assured
                            + ", have undergone the Need Analysis  after having reviewed the SBI Life Product options, I have opted for SBI LIFE - Smart Scholar.\n\n"
                            + "I, " + commonMethods.getUserName(context)
                            + " have explained the premiums, charges and benefits under the policy fully to the prospect/policyholder.");

            // tv_life_assured_name.setText(name_of_life_assured);
            tv_proposername.setText(name_of_life_assured);
            tv_bi_smart_scholar_porposer_name.setText(name_of_proposer);
            tv_bi_smart_scholar_life_assured_name.setText(name_of_life_assured);
        } else if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("n")) {
            edt_proposer_name
                    .setText("I, "
                            + name_of_proposer
                            + ", have received the information with respect to the above and have understood the above statement before entering into a contract.");
            edt_proposer_name_need_analysis
                    .setText("I, "
                            + name_of_proposer
                            + ", have undergone the Need Analysis  after having reviewed the SBI Life Product options, I have opted for SBI LIFE - Smart Scholar.\n\n"
                            + "I, " + commonMethods.getUserName(context)
                            + " have explained the premiums, charges and benefits under the policy fully to the prospect/policyholder.");

            tv_proposername.setText(name_of_proposer);
            tv_bi_smart_scholar_life_assured_name.setText(name_of_life_assured);
        }

        TextView textviewAGentStatement = d.findViewById(R.id.textviewAGentStatement);
        textviewAGentStatement.setText(commonMethods.getAgentDeclaration(context));
        if (!date2.equals("")) {
            btn_PolicyholderDate.setText(getDate(date2));
        } else {
            date2 = getDate1(getCurrentDate());
            btn_PolicyholderDate.setText(getCurrentDate());
        }

        if (!date1.equals("")) {
            btn_MarketingOfficalDate.setText(getDate(date1));
        } else {
            date1 = getDate1(getCurrentDate());
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
        GridView gv_userinfo = d.findViewById(R.id.gv_userinfo);
        GridView gv_userinfo2 = (GridView) d.findViewById(R.id.gv_userinfo2);
        GridView gv_userinfo3 = (GridView) d.findViewById(R.id.gv_userinfo3);

        imageButtonSmartScholarProposerPhotograph = d
                .findViewById(R.id.imageButtonSmartScholarProposerPhotograph);
        imageButtonSmartScholarProposerPhotograph
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Check = "Photo";
                        commonMethods.windowmessage(context, "_cust1Photo.jpg");
                    }
                });

        Ibtn_signatureofMarketing
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View view) {
                        if (cb_statement.isChecked()
                                && cb_statement_need_analysis.isChecked()
                                && checkboxAgentStatement.isChecked()) {
                            latestImage = "agent";
                            windowmessagesgin();
                        } else {
                            commonMethods.dialogWarning(context, "Please Tick on I Agree Clause ", true);
                            setFocusable(cb_statement);
                            cb_statement.requestFocus();
                        }

                    }
                });

        Ibtn_signatureofPolicyHolders
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View view) {
                        if (cb_statement.isChecked()
                                && cb_statement_need_analysis.isChecked()
                                && checkboxAgentStatement.isChecked()) {
                            latestImage = "proposer";
                            commonMethods.windowmessageProposersgin(context,
                                    NeedAnalysisActivity.URN_NO + "_cust1sign");
                        } else {
                            commonMethods.dialogWarning(context, "Please Tick on I Agree Clause ", true);
                            setFocusable(cb_statement);
                            cb_statement.requestFocus();
                        }

                    }
                });

        linearlayoutThirdpartySignature = d
                .findViewById(R.id.linearlayoutThirdpartySignature);
        Ibtn_signatureofThirdParty = d
                .findViewById(R.id.Ibtn_signatureofThirdParty);

        if (thirdPartySign != null && !thirdPartySign.equals("")) {
            byte[] signByteArray = Base64.decode(thirdPartySign, 0);
            Bitmap bitmap = BitmapFactory.decodeByteArray(signByteArray, 0,
                    signByteArray.length);
            Ibtn_signatureofThirdParty.setImageBitmap(bitmap);
        }

        final RadioButton radioButtonDepositPaymentYes = d
                .findViewById(R.id.radioButtonDepositPaymentYes);
        final RadioButton radioButtonDepositPaymentNo = d
                .findViewById(R.id.radioButtonDepositPaymentNo);

        radioButtonDepositPaymentNo
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        // TODO Auto-generated method stub
                        if (isChecked) {
                            linearlayoutThirdpartySignature
                                    .setVisibility(View.VISIBLE);
                        } else {
                            linearlayoutThirdpartySignature
                                    .setVisibility(View.GONE);

                            String thirdyPartySignName = NeedAnalysisActivity.URN_NO
                                    + "_thirdParty.png";
                            File thirdyPartySignFile = mStorageUtils.createFileToAppSpecificDir(context,
                                    thirdyPartySignName);

                            if (thirdyPartySignFile.exists()) {
                                thirdyPartySignFile.delete();
                            }

                            thirdPartySign = "";
                            Ibtn_signatureofThirdParty.setImageBitmap(null);

                        }
                    }
                });

        Ibtn_signatureofThirdParty
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View view) {

                        if (cb_statement.isChecked()
                                && cb_statement_need_analysis.isChecked()) {
                            latestImage = "thirdParty";
                            commonMethods
                                    .windowmessageProposersgin(context,
                                            NeedAnalysisActivity.URN_NO
                                                    + "_thirdParty");
                        } else {
                            commonMethods.dialogWarning(context, "Please Tick on I Agree Clause ", true);
                            setFocusable(cb_statement);
                            cb_statement.requestFocus();
                        }

                    }
                });

        // ////

        linearlayoutAppointeeSignature = d
                .findViewById(R.id.linearlayoutAppointeeSignature);
        Ibtn_signatureofAppointee = d
                .findViewById(R.id.Ibtn_signatureofAppointee);

        if (appointeeSign != null && !appointeeSign.equals("")) {
            byte[] signByteArray = Base64.decode(appointeeSign, 0);
            Bitmap bitmap = BitmapFactory.decodeByteArray(signByteArray, 0,
                    signByteArray.length);
            Ibtn_signatureofAppointee.setImageBitmap(bitmap);
        }

        final RadioButton radioButtonAppointeeYes = d
                .findViewById(R.id.radioButtonAppointeeYes);
        final RadioButton radioButtonAppointeeNo = d
                .findViewById(R.id.radioButtonAppointeeNo);

        radioButtonAppointeeYes
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        // TODO Auto-generated method stub
                        if (isChecked) {
                            linearlayoutAppointeeSignature
                                    .setVisibility(View.VISIBLE);
                        } else {
                            linearlayoutAppointeeSignature
                                    .setVisibility(View.GONE);
                            String appointeeSignName = NeedAnalysisActivity.URN_NO
                                    + "_appointee.png";
                            File appointeeSignFile = mStorageUtils.createFileToAppSpecificDir(context,
                                    appointeeSignName);
                            if (appointeeSignFile.exists()) {
                                appointeeSignFile.delete();
                            }

                            appointeeSign = "";
                            Ibtn_signatureofAppointee.setImageBitmap(null);
                        }
                    }
                });

        if (photoBitmap != null) {
            imageButtonSmartScholarProposerPhotograph
                    .setImageBitmap(photoBitmap);
        }

        Ibtn_signatureofAppointee
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View view) {

                        if (cb_statement.isChecked()
                                && cb_statement_need_analysis.isChecked()) {
                            latestImage = "Appointee";
                            commonMethods.windowmessageProposersgin(context,
                                    NeedAnalysisActivity.URN_NO + "_appointee");
                        } else {
                            commonMethods.dialogWarning(context, "Please Tick on I Agree Clause ", true);
                            setFocusable(cb_statement);
                            cb_statement.requestFocus();
                        }

                    }
                });

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

                            String appointeeSignName = NeedAnalysisActivity.URN_NO
                                    + "_appointee.png";
                            File appointeeSignFile = mStorageUtils.createFileToAppSpecificDir(context,
                                    appointeeSignName);
                            if (appointeeSignFile.exists()) {
                                appointeeSignFile.delete();
                            }

                            appointeeSign = "";
                            Ibtn_signatureofAppointee.setImageBitmap(null);

                            String thirdyPartySignName = NeedAnalysisActivity.URN_NO
                                    + "_thirdParty.png";
                            File thirdyPartySignFile = mStorageUtils.createFileToAppSpecificDir(context,
                                    thirdyPartySignName);

                            if (thirdyPartySignFile.exists()) {
                                thirdyPartySignFile.delete();
                            }

                            thirdPartySign = "";
                            Ibtn_signatureofThirdParty.setImageBitmap(null);

                            String customerPhotoName = NeedAnalysisActivity.URN_NO
                                    + "_cust1Photo.jpg";
                            File customerPhotoFile = mStorageUtils.createFileToAppSpecificDir(context,
                                    customerPhotoName);
                            if (customerPhotoFile.exists()) {
                                customerPhotoFile.delete();
                            }

                            photoBitmap = null;
                            imageButtonSmartScholarProposerPhotograph
                                    .setImageDrawable(getResources()
                                            .getDrawable(
                                                    R.drawable.focus_imagebutton_photo));

                            RadioGroup radioGroupDepositPayment = d
                                    .findViewById(R.id.radioGroupDepositPayment);
                            radioGroupDepositPayment.clearCheck();

                            RadioGroup radioGroupAppointee = d
                                    .findViewById(R.id.radioGroupAppointee);
                            radioGroupAppointee.clearCheck();
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
        if (mode.equalsIgnoreCase("Manual")) {
            radioButtonTrasactionModeManual.setChecked(true);
        } else if (mode.equalsIgnoreCase("Parivartan")) {
            radioButtonTrasactionModeParivartan.setChecked(true);
        }

        if (!TextUtils.isEmpty(place2)) {
            edt_Policyholderplace.setText(place2);
        }


        btn_proceed.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // if (frmProductHome.equals("FALSE")) {
                name_of_person = edt_proposer_name.getText().toString();
                place2 = edt_Policyholderplace.getText().toString();

                if (!place2.equals("")
                        && !date1.equals("Select Date")
                        && (!date2.equals("Select Date") || bankUserType
                        .equalsIgnoreCase("Y"))
                        && (!agent_sign.equals("") || bankUserType
                        .equalsIgnoreCase("Y"))
                        && (!proposer_sign.equals("") || bankUserType
                        .equalsIgnoreCase("Y"))
                        && (cb_statement.isChecked())
                        && checkboxAgentStatement.isChecked()
                        && (((photoBitmap != null
                        //remove parivartan validation
								/*&& ((radioButtonDepositPaymentNo.isChecked() == true && !thirdPartySign
										.equals("")) || radioButtonDepositPaymentYes
										.isChecked() == true) && ((radioButtonAppointeeYes
								.isChecked() == true && !appointeeSign
								.equals("")) || radioButtonAppointeeNo
								.isChecked() == true)*/) && radioButtonTrasactionModeParivartan
                        .isChecked()) || radioButtonTrasactionModeManual
                        .isChecked())) {
                    NeedAnalysisActivity.str_need_analysis = "";

                    // String isActive = "0";
                    String productCode = "UPCP30";

                    na_cbi_bean = new NA_CBI_bean(
                            QuatationNumber,
                            agentcode,
                            "",
                            userType,
                            "",
                            proposer_Title,
                            proposer_First_Name,
                            proposer_Last_Name,
                            proposer_Last_Name,
                            planName,
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf((sum_assured.equals("") || sum_assured == null) ? "0"
                                            : sum_assured))), obj
                            .getRound(premiumAmount), emailId,
                            mobileNo, agentEmail, agentMobile, na_input,
                            na_output, premPayingMode, Integer
                            .parseInt(policyTermStr), 0, productCode,
                            getDate(lifeAssured_date_of_birth), "", inputVal
                            .toString(), retVal.toString().replace(
                            bussIll.toString(), ""));

                    name_of_person = name_of_life_assured;

                    if (radioButtonTrasactionModeParivartan.isChecked()) {
                        mode = "Parivartan";
                    } else if (radioButtonTrasactionModeManual.isChecked()) {
                        mode = "Manual";
                    }

                    dbHelper.AddNeedAnalysisDashboardDetails(new ProductBIBean(
                            "",
                            QuatationNumber,
                            planName,
                            getCurrentDate(),
                            mobileNo,
                            getCurrentDate(),
                            dbHelper.GetUserCode(),
                            emailId,
                            "",
                            "",
                            agentcode,
                            "",
                            userType,
                            "",
                            proposer_Title,
                            proposer_First_Name,
                            proposer_Last_Name,
                            proposer_Last_Name,
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf((sum_assured.equals("") || sum_assured == null) ? "0"
                                            : sum_assured))), obj
                            .getRound(premiumAmount), agentEmail,
                            agentMobile, na_input, na_output, premPayingMode,
                            Integer.parseInt(policyTermStr), 0, productCode,
                            getDate(lifeAssured_date_of_birth), "", "", mode, inputVal
                            .toString(), retVal.toString().replace(
                            bussIll.toString(), "")));

                    createPdf();


                    NABIObj.serviceHit(BI_SmartScholarActivity.this,
                            na_cbi_bean, newFile, needAnalysispath.getPath(),
                            mypath.getPath(), name_of_person, QuatationNumber, mode);
                    d.dismiss();
                } else {

                    if (proposer_sign.equals("")
                            && !bankUserType.equalsIgnoreCase("Y")) {
                        commonMethods.dialogWarning(context, "Please Make Signature for Proposer ", true);
                        setFocusable(Ibtn_signatureofPolicyHolders);
                        Ibtn_signatureofPolicyHolders.requestFocus();
                    } else if (place2.equals("")) {
                        commonMethods.dialogWarning(context, "Please Fill Place Detail", true);
                        setFocusable(edt_Policyholderplace);
                        edt_Policyholderplace.requestFocus();

                    } else if (agent_sign.equals("")
                            && !bankUserType.equalsIgnoreCase("Y")) {
                        commonMethods.dialogWarning(context, "Please Make Signature for Sales Representative",
                                true);
                        setFocusable(Ibtn_signatureofMarketing);
                        Ibtn_signatureofMarketing.requestFocus();
                    } else if (!cb_statement.isChecked()) {
                        commonMethods.dialogWarning(context, "Please Tick on I Agree Clause ", true);
                        setFocusable(cb_statement);
                        cb_statement.requestFocus();
                    } else if (!checkboxAgentStatement.isChecked()) {
                        commonMethods.dialogWarning(context, "Please Tick on I Agree Clause ", true);
                        commonMethods.setFocusable(checkboxAgentStatement);
                        checkboxAgentStatement.requestFocus();
                    } else if (!radioButtonTrasactionModeParivartan.isChecked()
                            && !radioButtonTrasactionModeManual.isChecked()) {
                        commonMethods.dialogWarning(context, "Please Select Transaction Mode", true);
                        setFocusable(linearlayoutTrasactionModeParivartan);
                        linearlayoutTrasactionModeParivartan.requestFocus();
                    } else if (photoBitmap == null) {
                        commonMethods.dialogWarning(context, "Please Capture the Photo", true);
                        setFocusable(imageButtonSmartScholarProposerPhotograph);
                        imageButtonSmartScholarProposerPhotograph
                                .requestFocus();
                    } else if (!radioButtonDepositPaymentYes.isChecked()
                            && !radioButtonDepositPaymentNo.isChecked()) {
                        commonMethods.dialogWarning(context, "Please Select Third Party Payment", true);
                        setFocusable(linearlayoutThirdpartySignature);
                        linearlayoutThirdpartySignature.requestFocus();
                    } else if (radioButtonDepositPaymentNo.isChecked()
                            && thirdPartySign.equals("")) {
                        commonMethods.dialogWarning(context, "Please Make Signature for Third party ", true);
                        setFocusable(Ibtn_signatureofThirdParty);
                        Ibtn_signatureofThirdParty.requestFocus();
                    } else if (!radioButtonAppointeeYes.isChecked()
                            && !radioButtonAppointeeNo.isChecked()) {
                        commonMethods.dialogWarning(context, "Please Select Appointee Payment", true);
                        setFocusable(linearlayoutAppointeeSignature);
                        linearlayoutAppointeeSignature.requestFocus();
                    } else if (radioButtonAppointeeYes.isChecked()
                            && appointeeSign.equals("")) {
                        commonMethods.dialogWarning(context, "Please Make Signature for Appointee ", true);
                        setFocusable(Ibtn_signatureofAppointee);
                        Ibtn_signatureofAppointee.requestFocus();
                    }
                }
            }
        });

        btn_PolicyholderDate.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub

                DIALOG_ID = 2;
                showDialog(DATE_DIALOG_ID);
            }
        });

        btn_MarketingOfficalDate.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                DIALOG_ID = 3;
                showDialog(DATE_DIALOG_ID);

            }
        });

        input = inputVal.toString();
        output = retVal.toString();

        policyTermStr = prsObj.parseXmlTag(input, "policyTerm");
        premPayingMode = prsObj.parseXmlTag(input, "premFreq");

        if (proposer_Title.equalsIgnoreCase("Mr.")) {
            tv_bi_smart_scholar_life_assured_gender.setText("Male");
        } else if (proposer_Title.equalsIgnoreCase("Ms.")) {
            tv_bi_smart_scholar_life_assured_gender.setText("Female");
        } else if (proposer_Title.equalsIgnoreCase("Mrs.")) {
            tv_bi_smart_scholar_life_assured_gender.setText("Female");
        }
        // gender = prsObj.parseXmlTag(input, "gender");
        // tv_bi_smart_scholar_life_assured_gender.setText(gender);

        String staffdiscount = prsObj.parseXmlTag(input, "isStaff");

        if (staffdiscount.equalsIgnoreCase("true")) {
            // tr_staff_per.setVisibility(View.VISIBLE);
            tv_bi_is_Staff.setText("Yes");
            // tv_bi_flexi_smart_plus_staff_per.setText(staffdiscount_per);
        } else {

            // .setVisibility(View.GONE);
            tv_bi_is_Staff.setText("No");

        }
        TextView tv_premPaymentfrequency = d
                .findViewById(R.id.tv_bi_smart_scholar_premium_paying_frequency);

        String premFreq = prsObj.parseXmlTag(input, "premFreq");
        premPayingMode = premFreq;

        if (premPayingMode.equalsIgnoreCase("Single")) {
            tv_premPaymentfrequency.setText("One time at the inception of the policy");

        } else {
            tv_premPaymentfrequency.setText(premFreq);
        }

        premiumAmount = ((int) Double.parseDouble(prsObj.parseXmlTag(input, "premiumAmount"))) + "";

        if (Double.parseDouble(premiumAmount) > 100000) {
            tr_submit_id_proof.setVisibility(View.VISIBLE);
        } else {
            tr_submit_id_proof.setVisibility(View.GONE);
        }
        /* if (premFreq.equalsIgnoreCase("Yearly")) {
			tv_bi_smart_scholar_plan_detail_premium_tag
					.setText("Yearly Premium");
		} else if (premFreq.equalsIgnoreCase("Half Yearly")) {
			tv_bi_smart_scholar_plan_detail_premium_tag
					.setText("Half Yearly Premium");
		} else if (premFreq.equalsIgnoreCase("Quarterly")) {
			tv_bi_smart_scholar_plan_detail_premium_tag
					.setText("Quarterly Premium");
		} else if (premFreq.equalsIgnoreCase("Monthly")) {
			tv_bi_smart_scholar_plan_detail_premium_tag
					.setText("Monthly Premium");
		} else if (premFreq.equalsIgnoreCase("Single")) {
			tv_bi_smart_scholar_plan_detail_premium_tag
					.setText("Single Premium");
        }*/
        if (premFreq.equalsIgnoreCase("Yearly")) {
            tv_bi_smart_scholar_plan_detail_premium_tag
                    .setText("Amount of Installment Premium");
        } else if (premFreq.equalsIgnoreCase("Half Yearly")) {
            tv_bi_smart_scholar_plan_detail_premium_tag
                    .setText("Amount of Installment Premium");
        } else if (premFreq.equalsIgnoreCase("Quarterly")) {
            tv_bi_smart_scholar_plan_detail_premium_tag
                    .setText("Amount of Installment Premium");
        } else if (premFreq.equalsIgnoreCase("Monthly")) {
            tv_bi_smart_scholar_plan_detail_premium_tag
                    .setText("Amount of Installment Premium");
        } else if (premFreq.equalsIgnoreCase("Single")) {
            tv_bi_smart_scholar_plan_detail_premium_tag
                    .setText("Amount of Installment Premium");
        }
//        premiumAmount = prsObj.parseXmlTag(input, "premiumAmount");
        netYield4 = prsObj.parseXmlTag(output, "netYield4Pa");
        netYield8 = prsObj.parseXmlTag(output, "netYield8Pa");

        // tv_smart_scholar_net_yield_4.setText(netYield4 + "%");
        tv_smart_scholar_net_yield_8.setText(netYield8 + "%");

        age_entry = prsObj.parseXmlTag(input, "age");
        proposerageAtEntry = prsObj.parseXmlTag(input, "ageProposer");
        tv_bi_smart_scholar_age_entry.setText(age_entry + " Years");
        tv_bi_smart_scholar_proposer_age_entry.setText(proposerageAtEntry
                + " Years");

        String maturity_age = prsObj.parseXmlTag(output, "maturityAgeofChild");
        String proposerMaturity_age = prsObj.parseXmlTag(output,
                "maturityAgeofProposer");
        tv_bi_smart_scholar_maturity_age.setText(maturity_age + " Years");
        tv_bi_smart_scholar_proposer_maturity_age.setText(proposerMaturity_age
                + " Years");

        String annualised_premium = prsObj.parseXmlTag(output, "annPrem");
        tv_bi_smart_scholar_annualised_premium
                .setText("Rs. "
                        + obj.getRound(obj.getStringWithout_E(Double
                        .valueOf((annualised_premium.equals("") || annualised_premium == null) ? "0"
                                : annualised_premium))));

        sum_assured = prsObj.parseXmlTag(output, "sumAssured");
        String annualInstallment = currencyFormat.format(Math.min(
                Double.parseDouble(sum_assured), 5000000) * 0.1);

        // AnnualInstallment = prsObj.parseXmlTag(output, "AnnualInstallment");
        // tv_accidental_sum_assured_installment.setText("Rs."
        // + ((AnnualInstallment.equals("")) ? "0" : AnnualInstallment));

        policy_term = prsObj.parseXmlTag(input, "policyTerm");
        tv_bi_smart_scholar_policy_term.setText(policy_term + " Years ");
        tv_smart_scholar_maturity_age2.setText(policy_term + " Years ");

        tv_bi_smart_scholar_sum_assured.setText("Rs. "
                + getformatedThousandString(Double.valueOf(sum_assured)
                .intValue()));
        tv_guaranted_death.setText("9) This policy provides guaranteed death benefit of Rs. " + sum_assured);

        premium_paying_term = prsObj.parseXmlTag(input, "premPayingTerm");

        if (premFreq.equalsIgnoreCase("Single")) {

            tv_bi_smart_scholar_premium_paying_term.setText("One time at the inception of the policy");
        } else {
            tv_bi_smart_scholar_premium_paying_term.setText(premium_paying_term);
        }

        premiumAmount = prsObj.parseXmlTag(input, "premiumAmount");

        if (Double.parseDouble(premiumAmount) > 100000) {
            txt_submit_proof.setVisibility(View.VISIBLE);
        } else {
            txt_submit_proof.setVisibility(View.GONE);
        }

        premiumAmount = obj.getRound(obj.getStringWithout_E(Double
                .valueOf((premiumAmount))));
        tv_bi_smart_scholar_yearly_premium.setText("Rs. " + premiumAmount);

        String accidental_benefit_sumassured = prsObj.parseXmlTag(output,
                "accBenSumAssured");
        tv_bi_smart_scholar_accidental_benefit_sum_assured.setText("Rs. "
                + obj.getRound(obj.getStringWithout_E(Double
                .valueOf(accidental_benefit_sumassured.equals("") ? "0"
                        : accidental_benefit_sumassured))));

        // tv_accidental_death_benefit
        // .setText("Rs."
        // +
        // obj.getRound(obj.getStringWithout_E(Double.valueOf((accidental_benefit_sumassured
        // .equals("") || accidental_benefit_sumassured == null) ? "0"
        // : accidental_benefit_sumassured))) + ".  ");
        // tv_accidental_death_benefit1
        // .setText("Rs."
        // +
        // obj.getRound(obj.getStringWithout_E(Double.valueOf((accidental_benefit_sumassured
        // .equals("") || accidental_benefit_sumassured == null) ? "0"
        // : accidental_benefit_sumassured))));
        // tv_accidental_total_and_permanent_death_benefit
        // .setText("Rs."
        // +
        // obj.getRound(obj.getStringWithout_E(Double.valueOf((accidental_benefit_sumassured
        // .equals("") || accidental_benefit_sumassured == null) ? "0"
        // : accidental_benefit_sumassured))));

        tv_accidental_death_benefit
                .setText("12)In case of accidental death or accidental total  and permanent disability, whichever  is earlier,  this policy provides additional guaranteed benefit of Rs. "
                        + currencyFormat.format(Math.min(Double
                        .parseDouble(prsObj.parseXmlTag(output,
                                "sumAssured")), 5000000))
                        + ". In case of accidental death Rs. "
                        + currencyFormat.format(Math.min(Double
                        .parseDouble(prsObj.parseXmlTag(output,
                                "sumAssured")), 5000000))
                        + " will be paid as lump sum where as in case of accidental total and permenant disability Rs. "
                        + currencyFormat.format(Math.min(Double
                        .parseDouble(prsObj.parseXmlTag(output,
                                "sumAssured")), 5000000))
                        + " would be paid in 10  equal annual installments  of Rs. "
                        + currencyFormat.format(Math.min(Double
                        .parseDouble(prsObj.parseXmlTag(output,
                                "sumAssured")), 5000000) * 0.1)
                        + ". \n The benefit under Accident Benefit is subject to a Cap of Rs. 50 Lacs under all policies with SBI Life  for Accidental Death and Accidental TPD benefit for a single life.");

        percentage_equity_fund = (prsObj.parseXmlTag(input, "perInvEquityFund"));
        percentage_equity_optimiser_fund = (prsObj.parseXmlTag(input,
                "perInvEquityOptimiserFund"));
        percentage_growth_fund = (prsObj.parseXmlTag(input, "perInvgrowthFund"));

        percentage_bond_fund = (prsObj.parseXmlTag(input, "perInvBondFund"));

        tv_smart_scholar_equity_fund_allocation.setText((percentage_equity_fund
                .equals("") ? "0" : percentage_equity_fund) + " % ");
        tv_smart_scholar_equity_fund_fmc.setText("1.35 % ");
        tv_smart_scholar_equity_optimiser_fund_allocation
                .setText((percentage_equity_optimiser_fund.equals("") ? "0"
                        : percentage_equity_optimiser_fund) + " % ");
        tv_smart_scholar_equity_optimiser_fund_fmc.setText("1.35 % ");
        tv_smart_scholar_growth_fund_allocation.setText((percentage_growth_fund
                .equals("") ? "0" : percentage_growth_fund) + " % ");
        tv_smart_scholar_growth_fund_fmc.setText("1.35 % ");
        tv_smart_scholar_bond_fund_allocation.setText((percentage_bond_fund
                .equals("") ? "0" : percentage_bond_fund) + " % ");
        tv_smart_scholar_bond_plan_fmc.setText("1.00 % ");

        percentage_balanced_fund = (prsObj.parseXmlTag(input,
                "perInvBalancedFund"));
        tv_smart_scholar_balanced_fund_allocation
                .setText((percentage_balanced_fund.equals("") ? "0"
                        : percentage_balanced_fund) + " % ");
        tv_smart_scholar_balanced_fund_fmc.setText("1.25 % ");

        // String percentage_index_fund = prsObj.parseXmlTag(input,
        // "perInvIndexFund");
        percentage_top300_fund = (prsObj.parseXmlTag(input, "perInvTop300Fund"));
        percentage_bondOptimiserFund2 = (prsObj.parseXmlTag(input, "perInvbondOptimiserFund2"));
        percentage_pureFund = (prsObj.parseXmlTag(input, "perInvPureFund"));

        percentage_money_market_fund = (prsObj.parseXmlTag(input,
                "perInvMoneyMarketFund"));
        tv_smart_scholar_market_fund_allocation
                .setText((percentage_money_market_fund.equals("") ? "0"
                        : percentage_money_market_fund) + " % ");
        tv_smart_scholar_market_fund_fmc.setText("0.25 % ");

        tv_smart_scholar_top300_fund_allocation.setText((percentage_top300_fund
                .equals("") ? "0" : percentage_top300_fund) + " % ");
        tv_smart_scholar_top300_fund_fmc.setText("1.35 % ");

        tv_smart_scholer_optimiser_fund2_allocation.setText((percentage_bondOptimiserFund2
                .equals("") ? "0" : percentage_bondOptimiserFund2) + " % ");
        tv_smart_scholer_optimiser_fund2_fmc.setText("1.15 % ");

        tv_smart_scholer_pure_fund_allocation.setText((percentage_pureFund
                .equals("") ? "0" : percentage_pureFund) + " % ");
        tv_smart_scholer_pure_fund_fmc.setText("1.35 % ");


        // String percentage_pe_manage_fund = prsObj.parseXmlTag(input,
        // "perInvPEManagedFund");

        no_of_year_elapsed = prsObj.parseXmlTag(input, "noOfYrElapsed");
        tv_smart_scholar_no_of_years_elapsed.setText(no_of_year_elapsed
                + " Years ");

        String red_in_year_at_8_no_of_year = prsObj.parseXmlTag(output,
                "redInYieldNoYr");
        tv_smart_scholar_reduction_yield.setText(red_in_year_at_8_no_of_year
                + " % ");

        String red_in_year_at_8_maturity_age = prsObj.parseXmlTag(output,
                "redInYieldMat");
        tv_smart_scholar_reduction_yield2.setText(red_in_year_at_8_maturity_age
                + " % ");

        TextView tv_Company_policy_surrender_dec = (TextView) d
                .findViewById(R.id.tv_Company_policy_surrender_dec);


        String str_prem_freq = "";
        if (premFreq.equalsIgnoreCase("Single")) {
            str_prem_freq = "Single";
            Company_policy_surrender_dec = "Your SBI Life - Smart Scholar (111L073V03) is a "
                    + str_prem_freq
                    + " premium policy and you are required to pay premium once at the inception of the policy of Rs. "
                    + obj.getRound(obj.getStringWithout_E(Double
                    .valueOf((premiumAmount))))
                    + " .Your Policy Term is "
                    + policy_term
                    + " years,"
                    + " Premium Payment Term is Not Applicable and Sum Assured is Rs. "
                    +

                    getformatedThousandString(Double.valueOf(sum_assured)
                            .intValue());
        } else if (spnrPolicyTerm.getSelectedItem().toString().equalsIgnoreCase(spnrPremPayingTerm.getSelectedItem().toString())) {
            str_prem_freq = "Regular";
            Company_policy_surrender_dec = "Your SBI Life - Smart Scholar (111L073V03) is a "
                    + str_prem_freq
                    + " premium policy and you are required to pay "
                    + premFreq
                    + " Premium of Rs "
                    + obj.getRound(obj.getStringWithout_E(Double
                    .valueOf((premiumAmount))))
                    + " .Your Policy Term is "
                    + policy_term
                    + " years,"
                    + " Premium Payment Term is "
                    + premium_paying_term
                    + " years"
                    + " and Sum Assured is Rs. "
                    +

                    getformatedThousandString(Double.valueOf(sum_assured)
                            .intValue());
        } else {
            str_prem_freq = "Limited";
            Company_policy_surrender_dec = "Your SBI Life - Smart Scholar (111L073V03) is a "
                    + str_prem_freq
                    + " premium policy and you are required to pay "
                    + premFreq
                    + " Premium of Rs "
                    + obj.getRound(obj.getStringWithout_E(Double
                    .valueOf((premiumAmount))))
                    + " .Your Policy Term is "
                    + policy_term
                    + " years,"
                    + " Premium Payment Term is "
                    + premium_paying_term
                    + " years"
                    + " and Sum Assured is Rs. "
                    +

                    getformatedThousandString(Double.valueOf(sum_assured)
                            .intValue());
        }


        tv_Company_policy_surrender_dec.setText(Company_policy_surrender_dec);


        for (int i = 1; i <= Integer.parseInt(policy_term); i++) {
            String policy_year = prsObj
                    .parseXmlTag(output, "policyYr" + i + "");
            String premium = prsObj.parseXmlTag(output, "AnnPrem" + i + "");
            String premium_allocation_charge = prsObj.parseXmlTag(output,
                    "PremAllCharge" + i + "");
            String annulize_premium_allocation_charge = prsObj.parseXmlTag(output,
                    "AmtAviFrInv" + i + "");

            String PPWBCharges = prsObj.parseXmlTag(output,
                    "PPWBCharges" + i + "");
            String ADB_ATPDCharge = prsObj.parseXmlTag(output,
                    "ADB_ATPDCharge" + i + "");

           /* String amount_available_for_investment = prsObj.parseXmlTag(output,
                    "AmtAviFrInv" + i + "");*/
            String policy_administration_charge = prsObj.parseXmlTag(output,
                    "PolAdminChrg" + i + "");
            String mortality_charge1 = prsObj.parseXmlTag(output, "MortChrg4Pr"
                    + i + "");
            String total_charge1A = prsObj.parseXmlTag(output, "OtherCharges4Pr_PartA" + i
                    + "");
            String total_charge2A = prsObj.parseXmlTag(output, "OtherCharges4Pr_PartB" + i
                    + "");
            String total_charge1B = prsObj.parseXmlTag(output, "OtherCharges8Pr_PartA" + i
                    + "");
            String total_charge2B = prsObj.parseXmlTag(output, "OtherCharges8Pr_PartB" + i
                    + "");
            String service_tax_on_mortality_charge1 = prsObj.parseXmlTag(output,
                    "TotServTxOnCharg4Pr" + i + "");
            String AdditionsToTheFund4Pr = prsObj.parseXmlTag(output, "addFund4Pr"
                    + i + "");
            String fund_management_charge1 = prsObj.parseXmlTag(output,
                    "FundMgmtCharg4Pr" + i + "");
            String guranteed_addition1 = prsObj.parseXmlTag(output,
                    "loyalityAdd4Pr" + i + "");
            String fund_value_at_end1 = prsObj.parseXmlTag(output,
                    "FundValAtEnd4Pr" + i + "");
            String surrender_value1 = prsObj.parseXmlTag(output, "SurrVal4Pr"
                    + i + "");
            String death_benefit1 = prsObj.parseXmlTag(output, "DeathBen4Pr"
                    + i + "");
            String mortality_charge2 = prsObj.parseXmlTag(output, "MortChrg8Pr"
                    + i + "");
           /* String total_charge2 = prsObj.parseXmlTag(output, "TotCharg8Pr" + i
                    + "");*/
            String service_tax_on_mortality_charge2 = prsObj.parseXmlTag(output,
                    "TotServTxOnCharg8Pr" + i + "");
            String AdditionsToTheFund8Pr = prsObj.parseXmlTag(output, "addFund8Pr"
                    + i + "");
            String fund_management_charge2 = prsObj.parseXmlTag(output,
                    "FundMgmtCharg8Pr" + i + "");

            String fund_before_fmc1 = prsObj.parseXmlTag(output,
                    "FundBefFMC4Pr" + i + "");

            String fund_before_fmc2 = prsObj.parseXmlTag(output,
                    "FundBefFMC8Pr" + i + "");

            String guranteed_addition2 = prsObj.parseXmlTag(output,
                    "loyalityAdd8Pr" + i + "");
            String fund_value_at_end2 = prsObj.parseXmlTag(output,
                    "FundValAtEnd8Pr" + i + "");
            String surrender_value2 = prsObj.parseXmlTag(output, "SurrVal8Pr"
                    + i + "");
            String death_benefit2 = prsObj.parseXmlTag(output, "DeathBen8Pr"
                    + i + "");
            String commission = prsObj.parseXmlTag(output, "CommIfPay8Pr" + i
                    + "");

          /*  list_data.add(new M_BI_SmartScholarGrid_Adapter(policy_year, obj
					.getRound(obj.getStringWithout_E(Double.valueOf(premium)))
					+ "", premium_allocation_charge,
					amount_available_for_investment,
					policy_administration_charge, mortality_charge1,
					total_charge1, total_service_tax1, addition_to_fund1,
					fund_management_charge1, guranteed_addition1,
					fund_value_at_end1, surrender_value1, death_benefit1,
					mortality_charge2, total_charge2, total_service_tax2,
					addition_to_fund2, fund_management_charge2,
					guranteed_addition2, fund_value_at_end2, surrender_value2,
                    death_benefit2, commission));*/


            list_data.add(new M_BI_SmartScholarGrid_AdapterCommon(
                    policy_year,
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((premium.equals("") || premium == null) ? "0" : premium))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((mortality_charge1.equals("") || mortality_charge1 == null) ? "0" : mortality_charge1))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((total_charge1A.equals("") || total_charge1A == null) ? "0" : total_charge1A))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((service_tax_on_mortality_charge1.equals("") || service_tax_on_mortality_charge1 == null) ? "0" : service_tax_on_mortality_charge1))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((fund_value_at_end1.equals("") || fund_value_at_end1 == null) ? "0" : fund_value_at_end1))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((surrender_value1.equals("") || surrender_value1 == null) ? "0" : surrender_value1))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((death_benefit1.equals("") || death_benefit1 == null) ? "0" : death_benefit1))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((mortality_charge2.equals("") || mortality_charge2 == null) ? "0" : mortality_charge2))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((total_charge1B.equals("") || total_charge1B == null) ? "0" : total_charge1B))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((service_tax_on_mortality_charge2.equals("") || service_tax_on_mortality_charge2 == null) ? "0" : service_tax_on_mortality_charge2))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((fund_value_at_end2.equals("") || fund_value_at_end2 == null) ? "0" : fund_value_at_end2))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((surrender_value2.equals("") || surrender_value2 == null) ? "0" : surrender_value2))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((death_benefit2.equals("") || death_benefit2 == null) ? "0" : death_benefit2))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((commission.equals("") || commission == null) ? "0" : commission))) + ""));




         /*   list_data1.add(new M_BI_SmartPrivilegeAdapterCommon2(policy_year, obj
					.getRound(obj.getStringWithout_E(Double.valueOf(premium)))
					+ "", premium_allocation_charge,
                    annulize_premium_allocation_charge, mortality_charge2, service_tax_on_mortality_charge2, policy_administration_charge,total_charge2B, AdditionsToTheFund8Pr,
					guranteed_addition2, fund_before_fmc2, fund_management_charge2,
					fund_value_at_end2, surrender_value2, death_benefit2));*/


            list_data1.add(new M_BI_SmartScholarGrid_AdapterCommon2(
                    policy_year,
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((premium.equals("") || premium == null) ? "0" : premium))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((premium_allocation_charge.equals("") || premium_allocation_charge == null) ? "0" : premium_allocation_charge))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((annulize_premium_allocation_charge.equals("") || annulize_premium_allocation_charge == null) ? "0" : annulize_premium_allocation_charge))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((mortality_charge2.equals("") || mortality_charge2 == null) ? "0" : mortality_charge2))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((service_tax_on_mortality_charge2.equals("") || service_tax_on_mortality_charge2 == null) ? "0" : service_tax_on_mortality_charge2))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((policy_administration_charge.equals("") || policy_administration_charge == null) ? "0" : policy_administration_charge))) + "",
                    PPWBCharges, ADB_ATPDCharge,
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((total_charge2B.equals("") || total_charge2B == null) ? "0" : total_charge2B))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((AdditionsToTheFund8Pr.equals("") || AdditionsToTheFund8Pr == null) ? "0" : AdditionsToTheFund8Pr))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((guranteed_addition2.equals("") || guranteed_addition2 == null) ? "0" : guranteed_addition2))) + "",
                    //obj.getRound(obj.getStringWithout_E(Double.valueOf((fund_before_fmc2.equals("") || fund_before_fmc2 == null) ? "0": fund_before_fmc2)))+ "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((fund_before_fmc2.equals("") || fund_before_fmc2 == null) ? "0" : fund_before_fmc2))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((fund_management_charge2.equals("") || fund_management_charge2 == null) ? "0" : fund_management_charge2))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((fund_value_at_end2.equals("") || fund_value_at_end2 == null) ? "0" : fund_value_at_end2))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((surrender_value2.equals("") || surrender_value2 == null) ? "0" : surrender_value2))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((death_benefit2.equals("") || death_benefit2 == null) ? "0" : death_benefit2))) + ""));


		/*	list_data2.add(new M_BI_SmartPrivilegeAdapterCommon2(policy_year, obj
					.getRound(obj.getStringWithout_E(Double.valueOf(premium)))
					+ "", premium_allocation_charge,
                    annulize_premium_allocation_charge, mortality_charge1, service_tax_on_mortality_charge1, policy_administration_charge,total_charge1B, AdditionsToTheFund4Pr,
					guranteed_addition1, fund_before_fmc1, fund_management_charge1,
					fund_value_at_end1, surrender_value1, death_benefit1));*/

            list_data2.add(new M_BI_SmartScholarGrid_AdapterCommon2(
                    policy_year,
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((premium.equals("") || premium == null) ? "0" : premium))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((premium_allocation_charge.equals("") || premium_allocation_charge == null) ? "0" : premium_allocation_charge))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((annulize_premium_allocation_charge.equals("") || annulize_premium_allocation_charge == null) ? "0" : annulize_premium_allocation_charge))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((mortality_charge1.equals("") || mortality_charge1 == null) ? "0" : mortality_charge1))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((service_tax_on_mortality_charge1.equals("") || service_tax_on_mortality_charge1 == null) ? "0" : service_tax_on_mortality_charge1))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((policy_administration_charge.equals("") || policy_administration_charge == null) ? "0" : policy_administration_charge))) + "",
                    PPWBCharges, ADB_ATPDCharge,
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((total_charge2A.equals("") || total_charge2A == null) ? "0" : total_charge2A))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((AdditionsToTheFund4Pr.equals("") || AdditionsToTheFund4Pr == null) ? "0" : AdditionsToTheFund4Pr))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((guranteed_addition1.equals("") || guranteed_addition1 == null) ? "0" : guranteed_addition1))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((fund_before_fmc1.equals("") || fund_before_fmc1 == null) ? "0" : fund_before_fmc1))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((fund_management_charge1.equals("") || fund_management_charge1 == null) ? "0" : fund_management_charge1))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((fund_value_at_end1.equals("") || fund_value_at_end1 == null) ? "0" : fund_value_at_end1))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((surrender_value1.equals("") || surrender_value1 == null) ? "0" : surrender_value1))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((death_benefit1.equals("") || death_benefit1 == null) ? "0" : death_benefit1))) + ""));


        }
       /* Adapter_BI_SmartScholargrid adapter = new Adapter_BI_SmartScholargrid(
				BI_SmartScholarActivity.this, list_data);
		System.out.println(list_data);

		gv_userinfo.setAdapter(adapter);
		policy_term = policy_term.replaceAll("\\s+", "");

        GridHeight gh = new GridHeight();
        gh.getheight(gv_userinfo, policy_term);*/

        Adapter_BI_SmartScholargridCommon adapter = new Adapter_BI_SmartScholargridCommon(
                BI_SmartScholarActivity.this, list_data);
        gv_userinfo.setAdapter(adapter);

        Adapter_BI_SmartScholargridCommon2 adapter1 = new Adapter_BI_SmartScholargridCommon2(
                BI_SmartScholarActivity.this, list_data1);
        gv_userinfo2.setAdapter(adapter1);

        Adapter_BI_SmartScholargridCommon2 adapter2 = new Adapter_BI_SmartScholargridCommon2(
                BI_SmartScholarActivity.this, list_data2);
        gv_userinfo3.setAdapter(adapter2);


        GridHeight gh = new GridHeight();
        gh.getheight(gv_userinfo, policy_term);
        gh.getheight(gv_userinfo2, policy_term);
        gh.getheight(gv_userinfo3, policy_term);


        d.show();

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

    private void getInput(SmartScholarBean smartScholarBean) {
        inputVal = new StringBuilder();

        String LifeAssured_title = spnr_bi_smart_scholar_life_assured_title
                .getSelectedItem().toString();
        String LifeAssured_firstName = edt_bi_smart_scholar_life_assured_first_name
                .getText().toString();
        String LifeAssured_middleName = edt_bi_smart_scholar_life_assured_middle_name
                .getText().toString();
        String LifeAssured_lastName = edt_bi_smart_scholar_life_assured_last_name
                .getText().toString();
        String LifeAssured_DOB = btn_bi_smart_scholar_life_assured_date
                .getText().toString();
        String LifeAssured_age = spnr_Age.getSelectedItem().toString();

        String proposer_title = "";
        String proposer_firstName = "";
        String proposer_middleName = "";
        String proposer_lastName = "";
        String proposer_DOB = "";
        String proposer_age = "";
        String proposer_gender = "";

        if (!spnr_bi_smart_scholar_proposer_title.getSelectedItem().toString()
                .equals("")) {
            proposer_title = spnr_bi_smart_scholar_proposer_title
                    .getSelectedItem().toString();
            if (proposer_title.equals("Mr."))
                proposer_gender = "Male";
            else
                proposer_gender = "Female";
        }
        if (!edt_bi_smart_scholar_proposer_first_name.getText().toString()
                .equals(""))
            proposer_firstName = edt_bi_smart_scholar_proposer_first_name
                    .getText().toString();

        if (!edt_bi_smart_scholar_proposer_middle_name.getText().toString()
                .equals(""))
            proposer_middleName = edt_bi_smart_scholar_proposer_middle_name
                    .getText().toString();
        if (!edt_bi_smart_scholar_proposer_last_name.getText().toString()
                .equals(""))
            proposer_lastName = edt_bi_smart_scholar_proposer_last_name
                    .getText().toString();

        if (!btn_bi_smart_scholar_proposer_date.getText().toString()
                .equals("Select Date")) {
            Calendar present_date = Calendar.getInstance();
            int tDay = present_date.get(Calendar.DAY_OF_MONTH);
            int tMonth = present_date.get(Calendar.MONTH);
            int tYear = present_date.get(Calendar.YEAR);
            proposer_DOB = btn_bi_smart_scholar_proposer_date.getText()
                    .toString();
            proposer_age = commonForAllProd.calculateMyAge(tYear, tMonth + 1,
                    tDay, getDate1(proposer_DOB)) + "";
        }
        int age_of_child = smartScholarBean.getAgeOfChild();
        int age_of_parent = smartScholarBean.getAgeOfProposer();
        String gender = smartScholarBean.getGender();
        int policyTerm = smartScholarBean.getPolicyTerm_Basic();
        String PremPayingMode = smartScholarBean.getPremFreqMode();
        int premPayingTerm = smartScholarBean.getPremiumPayingTerm();
        double premiumAmount = smartScholarBean.getPremiumAmount();
        // boolean JandKResident = smartScholarBean.getIsJKResidentDiscOrNot();
        boolean isStaffOrNot = smartScholarBean.getIsForStaffOrNot();

        inputVal.append("<?xml version='1.0' encoding='utf-8' ?><smartscholar>");
        inputVal.append("<LifeAssured_title>").append(LifeAssured_title).append("</LifeAssured_title>");
        inputVal.append("<LifeAssured_firstName>").append(LifeAssured_firstName).append("</LifeAssured_firstName>");
        inputVal.append("<LifeAssured_middleName>").append(LifeAssured_middleName).append("</LifeAssured_middleName>");
        inputVal.append("<LifeAssured_lastName>").append(LifeAssured_lastName).append("</LifeAssured_lastName>");
        inputVal.append("<LifeAssured_DOB>").append(LifeAssured_DOB).append("</LifeAssured_DOB>");
        inputVal.append("<LifeAssured_age>").append(LifeAssured_age).append("</LifeAssured_age>");
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

        inputVal.append("<proposer_Is_Same_As_Life_Assured>"
                + proposer_Is_Same_As_Life_Assured
                + "</proposer_Is_Same_As_Life_Assured>");
        inputVal.append("<age>").append(age_of_child).append("</age>");
        inputVal.append("<gender>").append(gender).append("</gender>");
        inputVal.append("<ageProposer>").append(age_of_parent).append("</ageProposer>");

        inputVal.append("<policyTerm>").append(policyTerm).append("</policyTerm>");
        // inputVal.append("<isJKResident>" + JandKResident +
        // "</isJKResident>");
        inputVal.append("<isStaff>").append(isStaffOrNot).append("</isStaff>");
        inputVal.append("<str_kerla_discount>" + str_kerla_discount + "</str_kerla_discount>");
        inputVal.append("<premFreq>").append(PremPayingMode).append("</premFreq>");
        inputVal.append("<premiumAmount>").append(premiumAmount).append("</premiumAmount>");
        inputVal.append("<SAMF>").append(edt_SAMF.getText().toString()).append("</SAMF>");
        inputVal.append("<noOfYrElapsed>").append(edt_NoOfYearsElapsed.getText().toString()).append("</noOfYrElapsed>");

        inputVal.append("<perInvEquityFund>").append(edt_percent_EquityFund.getText().toString()).append("</perInvEquityFund>");

        inputVal.append("<perInvEquityOptimiserFund>").append(edt_percent_EquityOptimiserFund.getText().toString()).append("</perInvEquityOptimiserFund>");
        inputVal.append("<perInvgrowthFund>").append(edt_percent_GrowthFund.getText().toString()).append("</perInvgrowthFund>");

        inputVal.append("<perInvBondFund>").append(edt_percent_BondFund.getText().toString()).append("</perInvBondFund>");
        inputVal.append("<perInvBalancedFund>").append(edt_percent_BalancedFund.getText().toString()).append("</perInvBalancedFund>");
        // inputVal.append("<perInvIndexFund>"+percent_IndexFund.getText().toString()+"</perInvIndexFund>");
        inputVal.append("<perInvMoneyMarketFund>").append(edt_percent_MoneyMarketFund.getText().toString()).append("</perInvMoneyMarketFund>");

        inputVal.append("<perInvTop300Fund>").append(edt_percent_Top300Fund.getText().toString()).append("</perInvTop300Fund>");

        inputVal.append("<perInvbondOptimiserFund2>"
                + edt_bondOptimiserFund2.getText().toString()
                + "</perInvbondOptimiserFund2>");

        inputVal.append("<perInvPureFund>"
                + edt_pureFund.getText().toString()
                + "</perInvPureFund>");

        inputVal.append("<premPayingTerm>").append(premPayingTerm).append("</premPayingTerm>");

        //Added Tushar Kadam Kerla Applicable Tax 7-Jun-2019
        String str_kerla_discount = "N";
        if (cb_kerladisc.isChecked()) {
            str_kerla_discount = "Y";
        }

        inputVal.append("<KFC>" + str_kerla_discount + "</KFC>");
        //End Added Tushar Kadam Kerla Applicable Tax 7-Jun-2019

        inputVal.append("</smartscholar>");

    }

    // Store user input in Bean object
    private void addListenerOnSubmit() {
        // Insert data entered by user in an object
        smartScholarBean = new SmartScholarBean();
        if (isStaffDisc.isChecked()) {
            smartScholarBean.setIsForStaffOrNot(true);
        } else {
            smartScholarBean.setIsForStaffOrNot(false);
        }

        if (cb_kerladisc.isChecked()) {
            smartScholarBean.setKerlaDisc(true);

        } else {
            smartScholarBean.setKerlaDisc(false);
        }

        // removed by vrushali chaudhari
        // if(isBancAssuaranceDisc.isChecked())
        // {smartScholarBean.setIsBancAssuranceDiscOrNot(true);}
        // else{smartScholarBean.setIsBancAssuranceDiscOrNot(false);}

        smartScholarBean.setAgeOfChild(Integer.parseInt(spnr_Age
                .getSelectedItem().toString()));

        smartScholarBean.setGender(spnr_Gender.getSelectedItem().toString());

        smartScholarBean.setAgeOfProposer(Integer
                .parseInt(spnr_bi_smart_scholar_proposer_age.getSelectedItem()
                        .toString()));

        smartScholarBean.setPolicyTerm_Basic(Integer.parseInt(spnrPolicyTerm
                .getSelectedItem().toString()));

        if (spnrPremPayingMode.getSelectedItem().toString().equals("Single"))
            smartScholarBean.setPremiumPayingTerm(1);
        else
            smartScholarBean.setPremiumPayingTerm(Integer
                    .parseInt(spnrPremPayingTerm.getSelectedItem().toString()));

        smartScholarBean.setPremiumAmount(Double.parseDouble(edt_premiumAmount
                .getText().toString()));

        smartScholarBean.setSAMF(Double.parseDouble(edt_SAMF.getText()
                .toString()));

        smartScholarBean.setPremFreqMode(spnrPremPayingMode.getSelectedItem()
                .toString());

        smartScholarBean.setYearsOfDiscontinuance(Integer
                .parseInt(edt_NoOfYearsElapsed.getText().toString()));

        smartScholarBean.setEffectivePremium(getAnnualPremium());

        if (!edt_percent_EquityFund.getText().toString().equals(""))
            smartScholarBean.setPercentToBeInvested_EquityFund(Double
                    .parseDouble(edt_percent_EquityFund.getText().toString()));
        else
            smartScholarBean.setPercentToBeInvested_EquityFund(0);

        if (!edt_percent_EquityOptimiserFund.getText().toString().equals(""))
            smartScholarBean.setPercentToBeInvested_EquityOptimiserFund(Double
                    .parseDouble(edt_percent_EquityOptimiserFund.getText()
                            .toString()));
        else
            smartScholarBean.setPercentToBeInvested_EquityOptimiserFund(0);

        if (!edt_percent_GrowthFund.getText().toString().equals(""))
            smartScholarBean.setPercentToBeInvested_GrowthFund(Double
                    .parseDouble(edt_percent_GrowthFund.getText().toString()));
        else
            smartScholarBean.setPercentToBeInvested_GrowthFund(0);

        if (!edt_percent_BalancedFund.getText().toString().equals(""))
            smartScholarBean
                    .setPercentToBeInvested_BalancedFund(Double
                            .parseDouble(edt_percent_BalancedFund.getText()
                                    .toString()));
        else
            smartScholarBean.setPercentToBeInvested_BalancedFund(0);

        if (!edt_percent_BondFund.getText().toString().equals(""))
            smartScholarBean.setPercentToBeInvested_BondFund(Double
                    .parseDouble(edt_percent_BondFund.getText().toString()));
        else
            smartScholarBean.setPercentToBeInvested_BondFund(0);

        if (!edt_percent_MoneyMarketFund.getText().toString().equals(""))
            smartScholarBean.setPercentToBeInvested_MoneyMarketFund(Double
                    .parseDouble(edt_percent_MoneyMarketFund.getText()
                            .toString()));
        else
            smartScholarBean.setPercentToBeInvested_MoneyMarketFund(0);

        if (!edt_percent_Top300Fund.getText().toString().equals(""))
            smartScholarBean.setPercentToBeInvested_Top300Fund(Double
                    .parseDouble(edt_percent_Top300Fund.getText().toString()));
        else
            smartScholarBean.setPercentToBeInvested_Top300Fund(0);

        if (!edt_bondOptimiserFund2.getText().toString().equals(""))
            smartScholarBean.setPercentToBeInvested_BondOptimiserFund2(Double
                    .parseDouble(edt_bondOptimiserFund2.getText().toString()));
        else
            smartScholarBean.setPercentToBeInvested_BondOptimiserFund2(0);

        if (!edt_pureFund.getText().toString().equals(""))
            smartScholarBean.setPercentToBeInvested_PureFund(Double
                    .parseDouble(edt_pureFund.getText().toString()));
        else
            smartScholarBean.setPercentToBeInvested_PureFund(0);

        // removed by vrushali chaudhari
        // if(!edt_percent_IndexFund.getText().toString().equals(""))
        // SmartScholarBean.setPercentToBeInvested_IndexFund(Double.parseDouble(edt_percent_IndexFund.getText().toString()));
        // else
        // SmartScholarBean.setPercentToBeInvested_IndexFund(0);

        // if(!edt_percent_PEmanagedFund.getText().toString().equals(""))
        // SmartScholarBean.setPercentToBeInvested_PEmanagedFund(Double.parseDouble(edt_percent_PEmanagedFund.getText().toString()));
        // else
        // SmartScholarBean.setPercentToBeInvested_PEmanagedFund(0);

        // System.out.println("staff "+SmartScholarBean.getIsForStaffOrNot());
        // System.out.println("banca"+SmartScholarBean.getIsBancAssuranceDiscOrNot());
        // System.out.println("child age "+SmartScholarBean.getAgeOfChild());
        // System.out.println("proposer age "+SmartScholarBean.getAgeOfProposer());
        // System.out.println("policy term "+SmartScholarBean.getPolicyTerm_Basic());
        // System.out.println("frq mode"+SmartScholarBean.getPremFreqMode());
        // System.out.println("premium paying term "+SmartScholarBean.getPremiumPayingTerm());
        // System.out.println("SAMF "+SmartScholarBean.getSAMF());
        // System.out.println("premium "+SmartScholarBean.getPremiumAmount());
        // System.out.println("years elapsed "+SmartScholarBean.getYearsOfDiscontinuance());
        // System.out.println("gender"+SmartScholarBean.getGender());
        // System.out.println("balance "+SmartScholarBean.getPercentToBeInvested_BalancedFund());
        // System.out.println("bond "+SmartScholarBean.getPercentToBeInvested_BondFund());
        // System.out.println("equity"+SmartScholarBean.getPercentToBeInvested_EquityFund());
        // System.out.println("equity optimiser "+SmartScholarBean.getPercentToBeInvested_EquityOptimiserFund());
        // System.out.println("growth"+SmartScholarBean.getPercentToBeInvested_GrowthFund());
        // System.out.println("index "+SmartScholarBean.getPercentToBeInvested_IndexFund());
        // System.out.println("money market "+SmartScholarBean.getPercentToBeInvested_MoneyMarketFund());
        // System.out.println("pe managed "+SmartScholarBean.getPercentToBeInvested_PEmanagedFund());
        // System.out.println("top 300 "+SmartScholarBean.getPercentToBeInvested_Top300Fund());

        // Show Smart Scholar Output Screen
        showSmartScholarOutputPg(smartScholarBean);
    }

    /********************************** Output starts here **********************************************************/
    // Display Smart Scholar Output Screen
    private void showSmartScholarOutputPg(SmartScholarBean smartScholarBean) {

        // For BI
        retVal = new StringBuilder();

        String[] outputArr = getOutput("BI_Incl_Mort & Ser Tax",
                smartScholarBean);
        String[] outputReductionYield = getOutputReductionYield(
                "Reduction in Yeild_CAP", smartScholarBean);
        // Reset Values after each run
        SmartScholarBusinessLogic.loyaltyAddition_6Percent = 0;
        SmartScholarBusinessLogic.loyaltyAddition_10Percent = 0;

        /**** Added by Akshaya on 19-MAR-15 start */
        /***** Added by Akshaya on 19-MAR-15 start */String staffStatus;
        if (smartScholarBean.getIsForStaffOrNot())
            staffStatus = "sbi";
        else
            staffStatus = "none";
        /**** Added by Akshaya on 19-MAR-15 end */
        try {
            retVal.append("<?xml version='1.0' encoding='utf-8' ?><SmartScholar>");
            retVal.append("<errCode>0</errCode>");
            /**** Added by Akshaya on 19-MAR-15 start */
            retVal.append("<staffStatus>").append(staffStatus).append("</staffStatus>");
            /**** Added by Akshaya on 19-MAR-15 end */
            retVal.append("<maturityAgeofProposer>").append(smartScholarBean.getAgeOfProposer() + smartScholarBean
                    .getPolicyTerm_Basic()).append("</maturityAgeofProposer>");
            retVal.append("<maturityAgeofChild>").append(smartScholarBean.getAgeOfChild() + smartScholarBean
                    .getPolicyTerm_Basic()).append("</maturityAgeofChild>");
            retVal.append("<annPrem>").append(smartScholarBean.getEffectivePremium()).append("</annPrem>").append("<redInYieldMat>").append(outputReductionYield[0]).append("</redInYieldMat>").append("<redInYieldNoYr>").append(outputReductionYield[1]).append("</redInYieldNoYr>").append("<sumAssured>").append(outputArr[0]).append("</sumAssured>").append("<fundVal4>").append(outputArr[1]).append("</fundVal4>").append("<fundVal8>").append(outputArr[2]).append("</fundVal8>").append("<accBenSumAssured>").append(outputArr[4]).append("</accBenSumAssured>").append("<netYield4Pa>").append(outputReductionYield[2]).append("</netYield4Pa>").append("<netYield8Pa>").append(outputReductionYield[3]).append("</netYield8Pa>").append("<ppwbSumAssured>").append(outputArr[5]).append("</ppwbSumAssured>");

            int index = smartScholarBean.getPolicyTerm_Basic();
            String FundValAtEnd4Pr = prsObj.parseXmlTag(bussIll.toString(), "FundValAtEnd4Pr" + index + "");
            String FundValAtEnd8Pr = prsObj.parseXmlTag(bussIll.toString(), "FundValAtEnd8Pr" + index + "");

            retVal.append("<FundValAtEnd4Pr" + index + ">" + FundValAtEnd4Pr + "</FundValAtEnd4Pr" + index + ">");
            retVal.append("<FundValAtEnd8Pr" + index + ">" + FundValAtEnd8Pr + "</FundValAtEnd8Pr" + index + ">");

            retVal.append(bussIll.toString());
            retVal.append("</SmartScholar>");

        } catch (Exception e) {
            retVal.append("<?xml version='1.0' encoding='utf-8' ?><SmartScholar>" + "<errCode>1</errCode>" + "<errorMessage>").append(e.getMessage()).append("</errorMessage></SmartScholar>");
        }
        System.out.println("Final output in xml" + retVal.toString());
    }

    /****************************** Output ends here **********************************************************/

    /********************************** Calculations starts from here **********************************************************/

    private String[] getOutput(String sheetName,
                               SmartScholarBean smartScholarBean) {

        bussIll = new StringBuilder();


        String premFreqMode = smartScholarBean.getPremFreqMode();
        int premiumPayingTerm = smartScholarBean.getPremiumPayingTerm();
        double effectivePremium = getAnnualPremium(premFreqMode, smartScholarBean.getPremiumAmount(), premiumPayingTerm);
        int PF = getPF(premFreqMode);
        // Output Variable Declaration
        // double _oneHundredPercentOfCummulativePremium_AZ=0; //BY18
        // double _policyAdministrationCharge_R=0; //S18=0
        // double _fundValueAfterFMCandBeforeGA_AQ=0;
        // double _fundValueAtEnd_AD=0; //AQ18
        // double _fundValueAfterFMCAndBeforeGA_AA=0;
        // int _month_E=0;
        // int _year_F=0;
        // String _policyInForce_G="Y";
        // int _childsAge_H=0;
        // int _proposersAge_I=0,
        // month_BO=0,
        // _month_BO=0;
        // double _premium_J=0;
        // double _topUpPremium_K=0;
        // double _premiumAllocationCharge_L=0;
        // double _topUpCharges_M=0;
        // double _serviceTaxOnAllocation_N=0;
        // double _amountAvailableForInvestment_O=0;
        // double _sumAssuredRelatedCharges_P=0;
        // double _accidentCoverCharges_Q=0;
        // double _last2YearAvgFundValue_AB=0;
        // double _last2YearAvgFundValue_AR=0;
        // double _fundValueAtEnd_AT=0;
        // double _loyaltyAddition_AC=0;
        // double _loyaltyAddition_AS=0;
        // double _mortalityAndPPWBCharges_S=0;
        // double _totalCharges_T=0;
        // double _totalServiceTax_exclOfSTonFMC_U=0;
        // double _additionToFundIfAny_AM=0;
        // double _fundBeforeFMC_X=0;
        // double _fundManagementCharge_Y=0;
        // double _serviceTaxOnFMC_Z=0;
        // double _serviceTaxOnFMC_AP=0;
        // double _totalServiceTax_V=0;
        // double _surrenderCap_AY=0;
        // double _surrenderCharges_AE=0;
        // double _serviceTaxOnSurrenderCharges_AF=0;
        // double _surrenderValue_AG=0;
        // double _deathBenefit_AH=0;
        // double _mortalityAndPPWBCharges_AI=0;
        // double _totalCharges_AJ=0;
        // double _additionToFundIfAny_W=0;
        // double _fundBeforeFMC_AN=0;
        // double _fundManagementCharge_AO=0;
        // double _totalServiceTax_exclOfSTonFMC_AK=0;
        // double _deathBenefit_AX=0;
        // double _totalServiceTax_AL=0;
        // double _surrenderCharges_AU=0;
        // double _serviceTaxOnSurrenderCharges_AV=0;
        // double _surrenderValue_AW=0;

        // Temp Variable Declaretion
        int month_E = 0;
        int year_F = 0;
        String policyInForce_G = "Y";
        int childsAge_H = 0;
        int proposersAge_I = 0;
        double premium_J = 0;
        double topUpPremium_K = 0;
        double premiumAllocationCharge_L = 0;
        double topUpCharges_M = 0;
        double serviceTaxOnAllocation_N = 0;
        double amountAvailableForInvestment_O = 0, amountAvailableForInvestment_O1 = 0;
        double sumAssuredRelatedCharges_P = 0;
        double accidentCoverCharges_Q = 0, sum_AccidCoverCharge = 0, _sum_AccidCoverCharge = 0;
        double fundValueAtEnd_AT = 0;
        double policyAdministrationCharge_R = 0;
        double oneHundredPercentOfCummulativePremium_AZ = 0;
        double _oneHundredPercentOfCummulativePremium_AZ = 0;
        double mortalityAndPPWBCharges_S = 0, _mortalityAndPPWBCharges_S = 0;
        double totalCharges_T = 0;
        double totalServiceTax_exclOfSTonFMC_U = 0;
        double additionToFundIfAny_AM = 0;
        double surrenderCharges_AE = 0;
        double fundBeforeFMC_X = 0;
        double fundManagementCharge_Y = 0;
        double serviceTaxOnFMC_Z = 0;
        double serviceTaxOnFMC_AP = 0;
        double loyaltyAddition_AC = 0;
        double loyaltyAddition_AS = 0;
        double totalServiceTax_V = 0;
        double fundValueAfterFMCandBeforeGA_AQ = 0;
        double fundValueAtEnd_AD = 0;
        double surrenderCap_AY = 0;
        double serviceTaxOnSurrenderCharges_AF = 0;
        double surrenderValue_AG = 0;
        double deathBenefit_AH = 0;
        double mortalityAndPPWBCharges_AI = 0;
        double totalCharges_AJ = 0;
        double additionToFundIfAny_W = 0;
        double fundBeforeFMC_AN = 0;
        double fundManagementCharge_AO = 0;
        double totalServiceTax_exclOfSTonFMC_AK = 0;
        double fundValueAfterFMCAndBeforeGA_AA = 0;
        double deathBenefit_AX = 0;
        double last2YearAvgFundValue_AB = 0;
        double last2YearAvgFundValue_AR = 0;
        double totalServiceTax_AL = 0;
        double surrenderCharges_AU = 0;
        double serviceTaxOnSurrenderCharges_AV = 0;
        double surrenderValue_AW = 0, _reductionInYieldMaturityAt = 0, AccBenefitSumAssured = 0, PWBCharges = 0;
        /************* Added by Akshaya on 19-MAR-15 start ************************/
        double PPWBSumAssured = 0;
        /************* Added by Akshaya on 19-MAR-15 start ************************/
        // For BI
        double sum_I = 0,

                sum_O = 0, sum_O1 = 0, sum_R = 0,

                sum_S = 0, sum_V = 0, sum_Y = 0, sum_AA = 0, sum_AG = 0, sum_AH = 0, sum_AJ = 0, sum_AM = 0, sum_AP = 0, Commission_BL = 0, sum_J = 0, sum_T = 0, sum_AB = 0, sum_AC = 0, sum_AI = 0, sum_AL = 0, sum_AO = 0, _sum_AO = 0, sum_AS = 0, sum_FY_Premium = 0, sum_C = 0, sum_E = 0, sum_L = 0, _sum_L = 0, sum_Q = 0, sum_AT = 0, sum_AQ = 0, sum_W = 0, _sum_W = 0, sum_AD = 0, sum_of_prem = 0, sum_AE = 0, sum_AF = 0, sum_AU = 0, sum_AV = 0, sum_AW = 0;

        double _sum_Y = 0, _sum_AM = 0, _sum_J = 0, _sum_K = 0, _sum_I = 0, _cumulative_prem = 0, cumulative_prem = 0, Commission_AP = 0, otherCharges_PartA = 0, otherCharges_PartB = 0, otherCharges1_PartA = 0, otherCharges1_PartB = 0;

        double _sum_Q = 0, _sum_AI = 0, _sum_S = 0, _sum_AC = 0, sum_PW = 0, _sum_PW = 0,
                // _sum_O=0,
                _sum_R = 0,
                // _sum_AH=0,
                _sum_AX = 0, _sum_AT = 0, _sum_AD = 0,
                // _sum_AG=0,
                // _sum_AW=0,
                // _sum_AU=0,
                // _sum_AV=0,
                // _sum_AA=0,
                // _sum_AQ=0,
                _sum_AS = 0
                        // _sum_AE=0,
                        // _sum_AF=0,
                        // _sum_L=0,
                        // _sum_T=0,
                        // _sum_AJ=0,
                        // _sum_W=0,
                        // _sum_V=0,
                        // _sum_AO=0,
                        // _sum_AL=0,
                        // _Commission_AP=0
                        ;

        // From GUI Input
        boolean staffDisc = smartScholarBean.getIsForStaffOrNot();
        boolean bancAssuranceDisc = smartScholarBean
                .getIsBancAssuranceDiscOrNot();

        double SAMF = smartScholarBean.getSAMF();
        int ageOfChild = smartScholarBean.getAgeOfChild();
        int ageOfProposer = smartScholarBean.getAgeOfProposer();

        if (sheetName.equals("BI_Incl_Mort & Ser Tax")) {
            // Input Drop Downs from "BI_Incl_Mort & Ser Tax" sheet
            prop.allocationCharges = true; // *Sheet Name -> BI_Incl_Mort & Ser
            // Tax,*Cell ->B20
            prop.mortalityAndRiderCharges = true; // *Sheet Name -> BI_Incl_Mort
            // & Ser Tax,*Cell ->B21
            prop.administrationCharges = true; // *Sheet Name -> BI_Incl_Mort &
            // Ser Tax,*Cell ->B22
            prop.fundManagementCharges = true; // *Sheet Name -> BI_Incl_Mort &
            // Ser Tax,*Cell ->B23
            prop.mortalityCharges = true; // *Sheet Name -> BI_Incl_Mort & Ser
            // Tax,*Cell ->B27
            prop.optionCharges = true; // *Sheet Name -> BI_Incl_Mort & Ser
            // Tax,*Cell ->B29
            prop.topUpStatus = true; // *Sheet Name -> BI_Incl_Mort & Ser
            // Tax,*Cell ->B31
            prop.surrenderCharges = true; // *Sheet Name -> BI_Incl_Mort & Ser
            // Tax,*Cell ->B24
        }

        // Internally Calculated Input Fields
	/*	double effectivePremium = getAnnualPremium(premFreqMode,smartScholarBean.getPremiumAmount(),premiumPayingTerm);
		int PF = getPF(premFreqMode);*/
        int policyTerm = smartScholarBean.getPolicyTerm_Basic();
        //double serviceTax=smartScholarBean.getServiceTax();
        double serviceTax = 0;
        boolean isKerlaDisc = smartScholarBean.isKerlaDisc();
        double sumAssured = (effectivePremium * SAMF);
        // Declaration of method Variables/Object required for calculation
        SmartScholarBusinessLogic BIMAST = new SmartScholarBusinessLogic();
        String[] forBIArray = BIMAST
                .getForBIArr(prop.mortalityCharges_AsPercentOfofLICtable);
        int rowNumber = 0;
        double _prem_J = 0;
        for (int i = 0; i <= 24; i++) {
            if (i == 18) {
                BIMAST.arrFundValAfterFMCBeforeGA[i] = 23.00;
            } else {
                BIMAST.arrFundValAfterFMCBeforeGA[i] = 0;
            }
        }
        int currentElement = 25;
        for (int i = 0; i <= 24; i++) {
            if (i == 18) {
                BIMAST.arrFundValAfterFMCBeforeGA_AR[i] = 39.00;
            } else {
                BIMAST.arrFundValAfterFMCBeforeGA_AR[i] = 0;
            }
        }
        int currentElement_AR = 25;
        for (int i = 1; i < ((smartScholarBean.getPolicyTerm_Basic() * 12) + 1); i++)
        // for(int i=1;i<12;i++)
        {
            rowNumber++;
            // System.out.println("********************************************* "+i+" Row Output *********************************************");
            BIMAST.setMonth_E(rowNumber);
            month_E = Integer.parseInt(BIMAST.getMonth_E());
            // _month_E=month_E;
            // System.out.println("1.   Month_E : "+BIMAST.getMonth_E());

            BIMAST.setYear_F();
            year_F = Integer.parseInt(BIMAST.getYear_F());
            // _year_F=year_F;
            // System.out.println("2.   Year_F : "+BIMAST.getYear_F());

            if ((month_E % 12) == 0) {
                bussIll.append("<policyYr" + year_F + ">" + year_F
                        + "</policyYr" + year_F + ">");

            }
            if (isKerlaDisc == true && year_F <= 2) {
                serviceTax = 0.19;
            } else {
                serviceTax = 0.18;
            }
            policyInForce_G = BIMAST.getPolicyInForce_G();
            // _policyInForce_G=BIMAST.getPolicyInForce_G();
            // System.out.println("3.   PolicyInForce_G : "+BIMAST.getPolicyInForce_G());

            BIMAST.setChildsAge_H(ageOfChild);
            childsAge_H = Integer.parseInt(BIMAST.getChildsAge_H());
            // _childsAge_H=childsAge_H;
            // System.out.println("4.   ChildsAge_H : "+BIMAST.getChildsAge_H());

            BIMAST.setProposersAge_I(ageOfProposer);
            proposersAge_I = Integer.parseInt(BIMAST.getProposersAge_I());
            // _proposersAge_I=proposersAge_I;
            // System.out.println("5.   ProposersAge_I : "+BIMAST.getProposersAge_I());

            BIMAST.setPremium_J(premiumPayingTerm, PF, effectivePremium);
            premium_J = Double.parseDouble(BIMAST.getPremium_J());
            // _premium_J=premium_J;
            if (i != 0) {
                _prem_J = _prem_J + premium_J;
            }
            // System.out.println("6.   Premium_J : "+BIMAST.getPremium_J());

            sum_J += premium_J;
            sum_of_prem += premium_J;

            if ((month_E % 12) == 0) {
                // change
                _sum_J = sum_J;
                bussIll.append("<AnnPrem" + year_F + ">"
                        + commonForAllProd.getStringWithout_E(_sum_J)
                        + "</AnnPrem" + year_F + ">");

                sum_J = 0;
                // change
            }

            BIMAST.setOneHundredPercentOfCummulativePremium_AZ(_oneHundredPercentOfCummulativePremium_AZ);
            oneHundredPercentOfCummulativePremium_AZ = Double
                    .parseDouble(BIMAST
                            .getOneHundredPercentOfCummulativePremium_AZ());
            _oneHundredPercentOfCummulativePremium_AZ = oneHundredPercentOfCummulativePremium_AZ;
            // System.out.println("48.   OneHundredPercentOfCummulativePremium_AZ : "+oneHundredPercentOfCummulativePremium_AZ);

            BIMAST.setTopUpPremium_K(prop.topUpStatus, prop.effectiveTopUpPrem);
            topUpPremium_K = Double.parseDouble(BIMAST.getTopUpPremium_K());
            // _topUpPremium_K=topUpPremium_K;
            // System.out.println("7.   TopUpPremium_K : "+BIMAST.getTopUpPremium_K());
            _sum_K += topUpPremium_K;

            BIMAST.setPremiumAllocationCharge_L(staffDisc, bancAssuranceDisc,
                    premFreqMode);
            premiumAllocationCharge_L = Double.parseDouble(BIMAST
                    .getPremiumAllocationCharge_L());
            // _premiumAllocationCharge_L=premiumAllocationCharge_L;
            // System.out.println("8.   PremiumAllocationCharge_L : "+BIMAST.getPremiumAllocationCharge_L());

            sum_L += premiumAllocationCharge_L;
            _sum_L = sum_L;
            if ((month_E % 12) == 0) {
                // _sum_L=sum_L;
                bussIll.append("<PremAllCharge"
                        + year_F
                        + ">"
                        + commonForAllProd.getRoundUp(commonForAllProd
                        .getStringWithout_E(_sum_L)) + "</PremAllCharge"
                        + year_F + ">");
                sum_L = 0;
            }

            BIMAST.setTopUpCharges_M(prop.topUp);
            topUpCharges_M = Double.parseDouble(BIMAST.getTopUpCharges_M());
            // _topUpCharges_M=topUpCharges_M;
            // System.out.println("9.   TopUpCharges_M : "+BIMAST.getTopUpCharges_M());

            BIMAST.setServiceTaxOnAllocation_N(prop.allocationCharges,
                    serviceTax);
            serviceTaxOnAllocation_N = Double.parseDouble(BIMAST
                    .getServiceTaxOnAllocation_N());
            // _serviceTaxOnAllocation_N=serviceTaxOnAllocation_N;
            // System.out.println("10.   ServiceTaxOnAllocation_N : "+BIMAST.getServiceTaxOnAllocation_N());

            BIMAST.setAmountAvailableForInvestment_O();
            amountAvailableForInvestment_O = Double.parseDouble(BIMAST
                    .getAmountAvailableForInvestment_O());
            // _amountAvailableForInvestment_O=amountAvailableForInvestment_O;
            // System.out.println("11.   AmountAvailableForInvestment_O : "+BIMAST.getAmountAvailableForInvestment_O());
            sum_O += amountAvailableForInvestment_O;

            BIMAST.setAmountAvailableForInvestment_O1();
            amountAvailableForInvestment_O1 = Double.parseDouble(BIMAST
                    .getAmountAvailableForInvestment_O1());
            sum_O1 += amountAvailableForInvestment_O1;

            if ((month_E % 12) == 0) {
                // _sum_O=sum_O;
                // System.out.println(year_F+" sum_O : "+commonForAllProd.getRoundUp(String.valueOf(sum_O)));
                // _sum_O=Double.parseDouble(commonForAllProd.getRoundUp(sum_O+""));
                bussIll.append("<AmtAviFrInv"
                        + year_F
                        + ">"
                        + commonForAllProd.getRoundUp(commonForAllProd
                        .getStringWithout_E(sum_O1)) + "</AmtAviFrInv"
                        + year_F + ">");
                sum_O1 = 0;
            }
            BIMAST.setSumAssuredRelatedCharges_P(policyTerm, sumAssured, SAMF,
                    effectivePremium, prop.charge_SumAssuredBase,
                    prop.chargeSAren);
            sumAssuredRelatedCharges_P = Double.parseDouble(BIMAST
                    .getSumAssuredRelatedCharges_P());
            // _sumAssuredRelatedCharges_P=sumAssuredRelatedCharges_P;
            // System.out.println("12.   SumAssuredRelatedCharges_P : "+BIMAST.getSumAssuredRelatedCharges_P());

            BIMAST.setAccidentCoverCharges_Q(prop.optionCharges, premFreqMode,
                    sumAssured);
            accidentCoverCharges_Q = Double.parseDouble(BIMAST
                    .getAccidentCoverCharges_Q());
            sum_AccidCoverCharge += accidentCoverCharges_Q;
            _sum_AccidCoverCharge = sum_AccidCoverCharge;
            // _accidentCoverCharges_Q=accidentCoverCharges_Q;
            // System.out.println("13.   AccidentCoverCharges_Q : "+BIMAST.getAccidentCoverCharges_Q());

            BIMAST.setPolicyAdministrationCharge_R(policyAdministrationCharge_R);
            policyAdministrationCharge_R = Double.parseDouble(BIMAST
                    .getPolicyAdministrationCharge_R());
            // _policyAdministrationCharge_R=policyAdministrationCharge_R;
            // System.out.println("14.   PolicyAdministrationCharge_R : "+BIMAST.getPolicyAdministrationCharge_R());

            sum_R += policyAdministrationCharge_R;
            if ((month_E % 12) == 0) {
                _sum_R = Double
                        .parseDouble(commonForAllProd
                                .getRoundUp(commonForAllProd
                                        .getStringWithout_E(sum_R)));
                bussIll.append("<PolAdminChrg"
                        + year_F
                        + ">"
                        + commonForAllProd.getRoundUp(commonForAllProd
                        .getStringWithout_E(sum_R)) + "</PolAdminChrg"
                        + year_F + ">");
                // sum_R = 0;
            }


            PWBCharges = BIMAST.getPWBcharges(ageOfProposer, effectivePremium, premFreqMode, prop.PPWBstatus, premiumPayingTerm, PF, prop.mortalityCharges);

            sum_PW += PWBCharges;

            _sum_PW = sum_PW;

            if ((month_E % 12) == 0) {

                bussIll.append("<PPWBCharges" + year_F + ">" + Math.round(_sum_PW) + "</PPWBCharges" + year_F + ">");
                sum_PW = 0;
            }

            if ((month_E % 12) == 0) {

                bussIll.append("<ADB_ATPDCharge" + year_F + ">" + Math.round(_sum_AccidCoverCharge) + "</ADB_ATPDCharge" + year_F + ">");
                sum_AccidCoverCharge = 0;
            }

			/*BIMAST.setMortalityAndPPWBCharges_S(prop.PPWBstatus, premFreqMode,
					effectivePremium, forBIArray, ageOfProposer,
					premiumPayingTerm, policyTerm, prop.mortalityCharges,
					sumAssured, PF);
			mortalityAndPPWBCharges_S = Double.parseDouble(BIMAST
					.getMortalityAndPPWBCharges_S());*/

            BIMAST.setMortalityAndPPWBCharges_S(fundValueAtEnd_AD, policyTerm, forBIArray, sumAssured, prop.mortalityCharges);
            // _mortalityAndPPWBCharges_S=mortalityAndPPWBCharges_S;
            // System.out.println("15.   MortalityAndPPWBCharges_S : "+mortalityAndPPWBCharges_S);

			/*sum_Q += accidentCoverCharges_Q;
			sum_S += mortalityAndPPWBCharges_S;
			if ((month_E % 12) == 0) {
				_sum_Q = Double.parseDouble(commonForAllProd
						.getRoundOffLevel2(sum_Q + ""));
				_sum_S = Double.parseDouble(commonForAllProd
						.getRoundOffLevel2(sum_S + ""));
				// System.out.println(year_F+"  _sum_S : "+ _sum_S
				// +"   _sum_Q : "+_sum_Q + "  "+String.valueOf(_sum_S+_sum_Q));

                bussIll.append("<MortChrg4Pr"
                        + year_F
                        + ">"
                        + commonForAllProd.getRoundUp(commonForAllProd
                        .getStringWithout_E(_sum_S + _sum_Q))
                        + "</MortChrg4Pr" + year_F + ">");
				sum_S = 0;
			}*/

            mortalityAndPPWBCharges_S = Double.parseDouble(BIMAST.getMortalityAndPPWBCharges_S());
            _mortalityAndPPWBCharges_S = mortalityAndPPWBCharges_S;
            //System.out.println("14. mortalityCharges_R "+BIMAST.getMortalityCharges_R());
            //System.out.println("14. mortalityCharges_R "+mortalityCharges_R);

//
//			if(_year_F==25)
//			{
//				System.out.println("month "+ _month_E +"="+_mortalityCharges_R);
//			}


            _sum_S += _mortalityAndPPWBCharges_S;
            if ((month_E % 12) == 0) {

//				System.out.println("SUM="+sum_R);

                bussIll.append("<MortChrg4Pr" + year_F + ">" + Math.round(_sum_S) + "</MortChrg4Pr" + year_F + ">");
                _sum_S = 0;
            }

            BIMAST.setTotalCharges_T(ageOfProposer, effectivePremium, premFreqMode, prop.PPWBstatus, premiumPayingTerm, PF, policyTerm, prop.mortalityCharges);
            totalCharges_T = Double.parseDouble(BIMAST.getTotalCharges_T());
            // _totalCharges_T=totalCharges_T;
            // System.out.println("16.   TotalCharges_T : "+totalCharges_T);

            BIMAST.setTotalServiceTax_exclOfSTonFMC_U(serviceTax,
                    prop.mortalityAndRiderCharges, prop.administrationCharges,
                    prop.guaranteeCharges, ageOfProposer, effectivePremium, premFreqMode, prop.PPWBstatus, premiumPayingTerm, PF, prop.mortalityCharges);
            totalServiceTax_exclOfSTonFMC_U = Double.parseDouble(BIMAST
                    .getTotalServiceTax_exclOfSTonFMC_U());
            // _totalServiceTax_exclOfSTonFMC_U=totalServiceTax_exclOfSTonFMC_U;
            // System.out.println("17.   TotalServiceTax_exclOfSTonFMC_U : "+totalServiceTax_exclOfSTonFMC_U);

            sum_T += totalCharges_T;
			/*if ((month_E % 12) == 0) {
				// _sum_T=sum_T;
                bussIll.append("<TotCharg4Pr"
                        + year_F
                        + ">"
                        + commonForAllProd.getRoundUp(commonForAllProd
                        .getStringWithout_E(sum_T)) + "</TotCharg4Pr"
                        + year_F + ">");
				sum_T = 0;
			}*/


            BIMAST.setSurrenderCap_AY(premFreqMode, effectivePremium);
            surrenderCap_AY = Double.parseDouble(BIMAST.getSurrenderCap_AY());

            // _surrenderCap_AY=surrenderCap_AY;
            // System.out.println("47.   SurrenderCap_AY : "+surrenderCap_AY);

			/*BIMAST.setDeathBenefit_AH(sumAssured, policyTerm);
			deathBenefit_AH = Double.parseDouble(BIMAST.getDeathBenefit_AH());
			// _deathBenefit_AH=deathBenefit_AH;
			// System.out.println("30.   DeathBenefit_AH: "+deathBenefit_AH);
			// if((_month_E % 12) == 0)
			// {
			// _cumulative_prem=1.05*sum_of_pre;
			//
			// bussIll.append("<DeathBen4Pr"+ _year_F +">" +
			// commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(Math.max(sumAssured,
			// _cumulative_prem-sum_AD-sum_O-sum_R))) + "</DeathBen4Pr"+ _year_F
			// +">");
			//
			// }

			cumulative_prem = 1.05 * sum_of_prem;
			if ((month_E % 12) == 0) {
				_cumulative_prem = Double.parseDouble(commonForAllProd
						.getRoundUp(commonForAllProd
								.getStringWithout_E(cumulative_prem)));
				double _temp = 0;
				_temp = Double.parseDouble(""
						+ (_cumulative_prem - _sum_AD - sum_O - _sum_R));
				_sum_AX = Math.max(sumAssured, _temp);
                bussIll.append("<DeathBen4Pr"
                        + year_F
                        + ">"
                        + commonForAllProd.getRoundUp(commonForAllProd
                        .getStringWithout_E(_sum_AX)) + "</DeathBen4Pr"
                        + year_F + ">");
			}

			BIMAST.setMortalityAndPPWBCharges_AI(prop.PPWBstatus, premFreqMode,
					effectivePremium, forBIArray, ageOfProposer,
					premiumPayingTerm, policyTerm, prop.mortalityCharges,
					sumAssured, PF);
			mortalityAndPPWBCharges_AI = Double.parseDouble(BIMAST
					.getMortalityAndPPWBCharges_AI());
			// _mortalityAndPPWBCharges_AI=mortalityAndPPWBCharges_AI;
			// System.out.println("31.   MortalityAndPPWBCharges_AI : "+mortalityAndPPWBCharges_AI);

			sum_AI += mortalityAndPPWBCharges_AI;
			// System.out.println(month_E+" _sum_AI : "+ sum_AI
			// +"   _sum_Q : "+sum_Q);
			if ((month_E % 12) == 0) {
				_sum_AI = Double.parseDouble(commonForAllProd
						.getRoundOffLevel2(sum_AI + ""));
				_sum_Q = Double.parseDouble(commonForAllProd
						.getRoundOffLevel2(_sum_Q + ""));
				// System.out.println(year_F+"  _sum_AI : "+ sum_AI
				// +"   _sum_Q : "+sum_Q + "  "+String.valueOf(_sum_AI+_sum_Q));
				// System.out.println(month_E+" _sum_AI : "+ sum_AI
				// +"   _sum_Q : "+sum_Q);
                bussIll.append("<MortChrg8Pr"
                        + year_F
                        + ">"
                        + commonForAllProd.getRoundUp(commonForAllProd
                        .getStringWithout_E(_sum_AI + _sum_Q))
                        + "</MortChrg8Pr" + year_F + ">");
				sum_AI = 0;
				sum_Q = 0;
			}

			BIMAST.setTotalCharges_AJ(policyTerm);
			totalCharges_AJ = Double.parseDouble(BIMAST.getTotalCharges_AJ());
			// _totalCharges_AJ=totalCharges_AJ;
			// System.out.println("32.   TotalCharges_AJ : "+totalCharges_AJ);

			sum_AJ += totalCharges_AJ;
			if ((month_E % 12) == 0) {
				// _sum_AJ=sum_AJ;
                bussIll.append("<TotCharg8Pr"
                        + year_F
                        + ">"
                        + commonForAllProd.getRoundUp(commonForAllProd
                        .getStringWithout_E(sum_AJ)) + "</TotCharg8Pr"
                        + year_F + ">");
				sum_AJ = 0;
			}*/

            BIMAST.setAdditionToFundIfAny_W(policyTerm, fundValueAtEnd_AD);
            additionToFundIfAny_W = Double.parseDouble(BIMAST
                    .getAdditionToFundIfAny_W());
            // _additionToFundIfAny_W=additionToFundIfAny_W;
            // System.out.println("19.   AdditionToFundIfAny_W: "+additionToFundIfAny_W);

            sum_W += additionToFundIfAny_W;
            _sum_W = sum_W;
            if ((month_E % 12) == 0) {
                // _sum_W=sum_W;
                bussIll.append("<addFund4Pr"
                        + year_F
                        + ">"
                        + commonForAllProd.getRound(commonForAllProd
                        .getRoundOffLevel2(commonForAllProd
                                .getStringWithout_E(sum_W)))
                        + "</addFund4Pr" + year_F + ">");
                sum_W = 0;
            }
            BIMAST.setFundBeforeFMC_X(policyTerm, fundValueAtEnd_AD);
            fundBeforeFMC_X = Double.parseDouble(BIMAST.getFundBeforeFMC_X());
            // _fundBeforeFMC_X=fundBeforeFMC_X;
            // System.out.println("20.   FundBeforeFMC_X : "+fundBeforeFMC_X);

            BIMAST.setFundManagementCharge_Y(policyTerm, smartScholarBean
                            .getPercentToBeInvested_EquityFund(), smartScholarBean
                            .getPercentToBeInvested_EquityOptimiserFund(),
                    smartScholarBean.getPercentToBeInvested_GrowthFund(),
                    smartScholarBean.getPercentToBeInvested_BalancedFund(),
                    smartScholarBean.getPercentToBeInvested_BondFund(),
                    smartScholarBean.getPercentToBeInvested_MoneyMarketFund(),
                    smartScholarBean.getPercentToBeInvested_BondOptimiserFund2(),
                    smartScholarBean.getPercentToBeInvested_Top300Fund(),
                    smartScholarBean.getPercentToBeInvested_PureFund());
            fundManagementCharge_Y = Double.parseDouble(BIMAST
                    .getFundManagementCharge_Y());
            // _fundManagementCharge_Y=fundManagementCharge_Y;
            // System.out.println("21.   FundManagementCharge_Y : "+fundManagementCharge_Y);

            sum_Y += fundManagementCharge_Y;
            _sum_Y = sum_Y;
            if ((month_E % 12) == 0) {
//				_sum_Y = sum_Y;
                bussIll.append("<FundMgmtCharg4Pr"
                        + year_F
                        + ">"
                        + commonForAllProd.getRoundUp(commonForAllProd
                        .getStringWithout_E(_sum_Y))
                        + "</FundMgmtCharg4Pr" + year_F + ">");
                sum_Y = 0;
            }

            otherCharges_PartA = _sum_L + sum_R + _sum_PW + _sum_AccidCoverCharge + _sum_Y;

            if ((month_E % 12) == 0) {

                bussIll.append("<OtherCharges4Pr_PartA" + year_F + ">"
                        + commonForAllProd.getRound(String
                        .valueOf(otherCharges_PartA)) + "</OtherCharges4Pr_PartA" + year_F
                        + ">");
                otherCharges_PartA = 0;
            }

            if ((month_E % 12) == 0) {

                bussIll.append("<OtherCharges4Pr_PartB" + year_F + ">"
                        + commonForAllProd.getRound(String
                        .valueOf(otherCharges_PartB)) + "</OtherCharges4Pr_PartB" + year_F
                        + ">");
                otherCharges_PartB = 0;
            }

            BIMAST.setServiceTaxOnFMC_Z(prop.fundManagementCharges,
                    smartScholarBean.getPercentToBeInvested_EquityFund(),
                    smartScholarBean
                            .getPercentToBeInvested_EquityOptimiserFund(),
                    smartScholarBean.getPercentToBeInvested_GrowthFund(),
                    smartScholarBean.getPercentToBeInvested_BalancedFund(),
                    smartScholarBean.getPercentToBeInvested_BondFund(),
                    smartScholarBean.getPercentToBeInvested_MoneyMarketFund(),
                    smartScholarBean.getPercentToBeInvested_BondOptimiserFund2(),
                    smartScholarBean.getPercentToBeInvested_Top300Fund(),
                    smartScholarBean.getPercentToBeInvested_PureFund(), serviceTax);
            serviceTaxOnFMC_Z = Double.parseDouble(BIMAST
                    .getServiceTaxOnFMC_Z());
            // _serviceTaxOnFMC_Z=serviceTaxOnFMC_Z;
            // System.out.println("22.   ServiceTaxOnFMC_Z : "+serviceTaxOnFMC_Z);

            BIMAST.setTotalServiceTax_V(serviceTax);
            totalServiceTax_V = Double.parseDouble(BIMAST
                    .getTotalServiceTax_V());
            // _totalServiceTax_V=totalServiceTax_V;
            // System.out.println("18.   TotalServiceTax_V : "+totalServiceTax_V);

            sum_V += totalServiceTax_V;
            if ((month_E % 12) == 0) {
                // _sum_V=sum_V;
                bussIll.append("<TotServTxOnCharg4Pr"
                        + year_F
                        + ">"
                        + commonForAllProd.getRound(commonForAllProd
                        .getStringWithout_E(sum_V))
                        + "</TotServTxOnCharg4Pr" + year_F + ">");
                sum_V = 0;
            }


            BIMAST.setFundValueAfterFMCAndBeforeGA_AA(policyTerm);
            fundValueAfterFMCAndBeforeGA_AA = Double.parseDouble(BIMAST
                    .getFundValueAfterFMCAndBeforeGA_AA());
            // _fundValueAfterFMCAndBeforeGA_AA=fundValueAfterFMCAndBeforeGA_AA;
            BIMAST.arrFundValAfterFMCBeforeGA[currentElement] = fundValueAfterFMCAndBeforeGA_AA;
            currentElement++;
            // System.out.println("23.   FundValueAfterFMCAndBeforeGA_AA: "+fundValueAfterFMCAndBeforeGA_AA);
            sum_AA = Double
                    .parseDouble(commonForAllProd.getRound(commonForAllProd
                            .getStringWithout_E(fundValueAfterFMCAndBeforeGA_AA)));


			/*BIMAST.setTotalServiceTax_exclOfSTonFMC_AK(serviceTax,
					prop.mortalityAndRiderCharges, prop.administrationCharges,
					prop.guaranteeCharges);
			totalServiceTax_exclOfSTonFMC_AK = Double.parseDouble(BIMAST
					.getTotalServiceTax_exclOfSTonFMC_AK());*/
            // _totalServiceTax_exclOfSTonFMC_AK=totalServiceTax_exclOfSTonFMC_AK;
            // System.out.println("33.   TotalServiceTax_exclOfSTonFMC_AK : "+totalServiceTax_exclOfSTonFMC_AK);

			/*BIMAST.setDeathBenefit_AH(sumAssured, policyTerm);
			deathBenefit_AH = Double.parseDouble(BIMAST.getDeathBenefit_AH());
			// _deathBenefit_AH=deathBenefit_AH;
			// System.out.println("30.   DeathBenefit_AH: "+deathBenefit_AH);
			// if((_month_E % 12) == 0)
			// {
			// _cumulative_prem=1.05*sum_of_pre;
			//
			// bussIll.append("<DeathBen4Pr"+ _year_F +">" +
			// commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(Math.max(sumAssured,
			// _cumulative_prem-sum_AD-sum_O-sum_R))) + "</DeathBen4Pr"+ _year_F
			// +">");
			//
			// }

			cumulative_prem = 1.05 * sum_of_prem;
			if ((month_E % 12) == 0) {
				_cumulative_prem = Double.parseDouble(commonForAllProd
						.getRoundUp(commonForAllProd
								.getStringWithout_E(cumulative_prem)));
				double _temp = 0;
				_temp = Double.parseDouble(""
						+ (_cumulative_prem - _sum_AD - sum_O - _sum_R));
				_sum_AX = Math.max(sumAssured, _temp);
				bussIll.append("<DeathBen4Pr"
						+ year_F
						+ ">"
						+ commonForAllProd.getRoundUp(commonForAllProd
								.getStringWithout_E(_sum_AX)) + "</DeathBen4Pr"
						+ year_F + ">");
			}*/


			/*BIMAST.setTotalServiceTax_exclOfSTonFMC_AK(serviceTax,
					prop.mortalityAndRiderCharges, prop.administrationCharges,
					prop.guaranteeCharges);
			totalServiceTax_exclOfSTonFMC_AK = Double.parseDouble(BIMAST
					.getTotalServiceTax_exclOfSTonFMC_AK());*/


			/*cumulative_prem = 1.05 * sum_of_prem;
			if ((month_E % 12) == 0) {
				_cumulative_prem = Double.parseDouble(commonForAllProd
						.getRoundUp(commonForAllProd
								.getStringWithout_E(cumulative_prem)));
				double _temp = 0;
				_temp = Double.parseDouble(""
						+ (_cumulative_prem - _sum_AD - sum_O - _sum_R));
				_sum_AX = Math.max(sumAssured, _temp);
				bussIll.append("<DeathBen4Pr"
						+ year_F
						+ ">"
						+ commonForAllProd.getRoundUp(commonForAllProd
								.getStringWithout_E(_sum_AX)) + "</DeathBen4Pr"
						+ year_F + ">");
			}*/

			/*BIMAST.setMortalityAndPPWBCharges_AI(prop.PPWBstatus, premFreqMode,
					effectivePremium, forBIArray, ageOfProposer,
					premiumPayingTerm, policyTerm, prop.mortalityCharges,
					sumAssured, PF);
			mortalityAndPPWBCharges_AI = Double.parseDouble(BIMAST
					.getMortalityAndPPWBCharges_AI());*/
            // _mortalityAndPPWBCharges_AI=mortalityAndPPWBCharges_AI;
            // System.out.println("31.   MortalityAndPPWBCharges_AI : "+mortalityAndPPWBCharges_AI);

            BIMAST.setMortalityAndPPWBCharges_AI(fundValueAtEnd_AD, policyTerm, forBIArray, sumAssured, prop.mortalityCharges);

            mortalityAndPPWBCharges_AI = Double.parseDouble(BIMAST
                    .getMortalityAndPPWBCharges_AI());

            _sum_Q += mortalityAndPPWBCharges_AI;

            // System.out.println(month_E+" _sum_AI : "+ sum_AI
            // +"   _sum_Q : "+sum_Q);
			/*if ((month_E % 12) == 0) {
				_sum_AI = Double.parseDouble(commonForAllProd
						.getRoundOffLevel2(sum_AI + ""));
				_sum_Q = Double.parseDouble(commonForAllProd
						.getRoundOffLevel2(_sum_Q + ""));
				// System.out.println(year_F+"  _sum_AI : "+ sum_AI
				// +"   _sum_Q : "+sum_Q + "  "+String.valueOf(_sum_AI+_sum_Q));
				// System.out.println(month_E+" _sum_AI : "+ sum_AI
				// +"   _sum_Q : "+sum_Q);
				bussIll.append("<MortChrg8Pr"
						+ year_F
						+ ">"
						+ commonForAllProd.getRoundUp(commonForAllProd
								.getStringWithout_E(_sum_AI + _sum_Q))
						+ "</MortChrg8Pr" + year_F + ">");
				sum_AI = 0;
				sum_Q = 0;
			}*/

            if ((month_E % 12) == 0) {

//				System.out.println("SUM="+sum_R);

                bussIll.append("<MortChrg8Pr" + year_F + ">" + Math.round(_sum_Q) + "</MortChrg8Pr" + year_F + ">");
                _sum_Q = 0;
            }

            BIMAST.setTotalCharges_AJ(ageOfProposer, effectivePremium, premFreqMode, prop.PPWBstatus, premiumPayingTerm, PF, policyTerm, prop.mortalityCharges);
            totalCharges_AJ = Double.parseDouble(BIMAST.getTotalCharges_AJ());
            // _totalCharges_AJ=totalCharges_AJ;
            // System.out.println("32.   TotalCharges_AJ : "+totalCharges_AJ);

            BIMAST.setTotalServiceTax_exclOfSTonFMC_AK(serviceTax,
                    prop.mortalityAndRiderCharges, prop.administrationCharges,
                    prop.guaranteeCharges, ageOfProposer, effectivePremium, premFreqMode, prop.PPWBstatus, premiumPayingTerm, PF, prop.mortalityCharges);
            totalServiceTax_exclOfSTonFMC_AK = Double.parseDouble(BIMAST
                    .getTotalServiceTax_exclOfSTonFMC_AK());

            sum_AJ += totalCharges_AJ;
			/*if ((month_E % 12) == 0) {
				// _sum_AJ=sum_AJ;
				bussIll.append("<TotCharg8Pr"
						+ year_F
						+ ">"
						+ commonForAllProd.getRoundUp(commonForAllProd
								.getStringWithout_E(sum_AJ)) + "</TotCharg8Pr"
						+ year_F + ">");
				sum_AJ = 0;
			}*/

            BIMAST.setAdditionToFundIfAny_AM(fundValueAtEnd_AT, policyTerm);
            additionToFundIfAny_AM = Double.parseDouble(BIMAST
                    .getAdditionToFundIfAny_AM());
            // _additionToFundIfAny_AM=additionToFundIfAny_AM;
            // System.out.println("35.   AdditionToFundIfAny_AM : "+additionToFundIfAny_AM);

            sum_AM += additionToFundIfAny_AM;
            if ((month_E % 12) == 0) {
                // System.out.println("35.   AdditionToFundIfAny_AM : "+sum_AM);
                _sum_AM = sum_AM;
                bussIll.append("<addFund8Pr"
                        + year_F
                        + ">"
                        + commonForAllProd.getRound(commonForAllProd
                        .getRoundOffLevel2(commonForAllProd
                                .getStringWithout_E(_sum_AM)))
                        + "</addFund8Pr" + year_F + ">");
                sum_AM = 0;
            }

            BIMAST.setFundBeforeFMC_AN(policyTerm, fundValueAtEnd_AT);
            fundBeforeFMC_AN = Double.parseDouble(BIMAST.getFundBeforeFMC_AN());
            // _fundBeforeFMC_AN=fundBeforeFMC_AN;
            // System.out.println("36.   FundBeforeFMC_AN: "+fundBeforeFMC_AN);

            BIMAST.setFundManagementCharge_AO(policyTerm, smartScholarBean
                            .getPercentToBeInvested_EquityFund(), smartScholarBean
                            .getPercentToBeInvested_EquityOptimiserFund(),
                    smartScholarBean.getPercentToBeInvested_GrowthFund(),
                    smartScholarBean.getPercentToBeInvested_BalancedFund(),
                    smartScholarBean.getPercentToBeInvested_BondFund(),
                    smartScholarBean.getPercentToBeInvested_MoneyMarketFund(),
                    smartScholarBean.getPercentToBeInvested_BondOptimiserFund2(),
                    smartScholarBean.getPercentToBeInvested_Top300Fund(),
                    smartScholarBean.getPercentToBeInvested_PureFund());
            fundManagementCharge_AO = Double.parseDouble(BIMAST
                    .getFundManagementCharge_AO());
            // _fundManagementCharge_AO=fundManagementCharge_AO;
            // System.out.println("37.   FundManagementCharge_AO: "+fundManagementCharge_AO);

            sum_AO += fundManagementCharge_AO;
            _sum_AO = sum_AO;
            if ((month_E % 12) == 0) {

                // _sum_AO=sum_AO;
                bussIll.append("<FundMgmtCharg8Pr"
                        + year_F
                        + ">"
                        + commonForAllProd.getRoundUp(commonForAllProd
                        .getStringWithout_E(_sum_AO))
                        + "</FundMgmtCharg8Pr" + year_F + ">");
                sum_AO = 0;
            }

            otherCharges1_PartA = _sum_L + sum_R + _sum_PW + _sum_AccidCoverCharge + _sum_AO;

            if ((month_E % 12) == 0) {

                bussIll.append("<OtherCharges8Pr_PartA" + year_F + ">"
                        + commonForAllProd.getRound(String
                        .valueOf(otherCharges1_PartA)) + "</OtherCharges8Pr_PartA" + year_F
                        + ">");
                otherCharges1_PartA = 0;
            }

            if ((month_E % 12) == 0) {

                bussIll.append("<OtherCharges8Pr_PartB" + year_F + ">"
                        + commonForAllProd.getRound(String
                        .valueOf(otherCharges1_PartB)) + "</OtherCharges8Pr_PartB" + year_F
                        + ">");
                otherCharges1_PartB = 0;
            }


            BIMAST.setServiceTaxOnFMC_AP(prop.fundManagementCharges,
                    smartScholarBean.getPercentToBeInvested_EquityFund(),
                    smartScholarBean
                            .getPercentToBeInvested_EquityOptimiserFund(),
                    smartScholarBean.getPercentToBeInvested_GrowthFund(),
                    smartScholarBean.getPercentToBeInvested_BalancedFund(),
                    smartScholarBean.getPercentToBeInvested_BondFund(),
                    smartScholarBean.getPercentToBeInvested_MoneyMarketFund(),
                    smartScholarBean.getPercentToBeInvested_BondOptimiserFund2(),
                    smartScholarBean.getPercentToBeInvested_Top300Fund(),
                    smartScholarBean.getPercentToBeInvested_PureFund(), serviceTax);
            serviceTaxOnFMC_AP = Double.parseDouble(BIMAST
                    .getServiceTaxOnFMC_AP());
            // _serviceTaxOnFMC_AP=serviceTaxOnFMC_AP;
            // System.out.println("38.   ServiceTaxOnFMC_AP : "+serviceTaxOnFMC_AP);

            BIMAST.setTotalServiceTax_AL();
            totalServiceTax_AL = Double.parseDouble(BIMAST
                    .getTotalServiceTax_AL());
            // _totalServiceTax_AL=totalServiceTax_AL;
            // System.out.println("34.   TotalServiceTax_AL: "+totalServiceTax_AL);

            sum_AL += totalServiceTax_AL;
            if ((month_E % 12) == 0) {
                // _sum_AL=sum_AL;
                bussIll.append("<TotServTxOnCharg8Pr"
                        + year_F
                        + ">"
                        + commonForAllProd.getRoundUp(commonForAllProd
                        .getStringWithout_E(sum_AL))
                        + "</TotServTxOnCharg8Pr" + year_F + ">");
                sum_AL = 0;
            }

			/*BIMAST.setFundValueAfterFMCAndBeforeGA_AA(policyTerm);
			fundValueAfterFMCAndBeforeGA_AA = Double.parseDouble(BIMAST
					.getFundValueAfterFMCAndBeforeGA_AA());
			// _fundValueAfterFMCAndBeforeGA_AA=fundValueAfterFMCAndBeforeGA_AA;
			BIMAST.arrFundValAfterFMCBeforeGA[currentElement] = fundValueAfterFMCAndBeforeGA_AA;
			currentElement++;
			// System.out.println("23.   FundValueAfterFMCAndBeforeGA_AA: "+fundValueAfterFMCAndBeforeGA_AA);
			sum_AA = Double
					.parseDouble(commonForAllProd.getRoundUp(commonForAllProd
							.getStringWithout_E(fundValueAfterFMCAndBeforeGA_AA)));*/
            // _sum_AA =Double.parseDouble(sum_AA+"");

            BIMAST.setFundValueAfterFMCAndBeforeGA_AQ(policyTerm);
            fundValueAfterFMCandBeforeGA_AQ = Double.parseDouble(BIMAST
                    .getFundValueAfterFMCAndBeforeGA_AQ());
            // _fundValueAfterFMCandBeforeGA_AQ=fundValueAfterFMCandBeforeGA_AQ;
            BIMAST.arrFundValAfterFMCBeforeGA_AR[currentElement_AR] = fundValueAfterFMCandBeforeGA_AQ;
            currentElement_AR++;
            // System.out.println("39.   FundValueAfterFMCAndBeforeGA_AQ: "+fundValueAfterFMCandBeforeGA_AQ);

            sum_AQ = Double
                    .parseDouble(commonForAllProd.getRoundUp(commonForAllProd
                            .getStringWithout_E(fundValueAfterFMCandBeforeGA_AQ)));
            // _sum_AQ=Double.parseDouble(sum_AQ+"");

            BIMAST.setLast2YearAvgFundValue_AB(
                    BIMAST.arrFundValAfterFMCBeforeGA, i);
            last2YearAvgFundValue_AB = Double.parseDouble(BIMAST
                    .getLast2YearAvgFundValue_AB());
            // _last2YearAvgFundValue_AB=last2YearAvgFundValue_AB;
            // System.out.println("24.   Last2YearAvgFundValue_AB: "+last2YearAvgFundValue_AB);

            BIMAST.setLoyaltyAddition_AC(policyTerm);
            loyaltyAddition_AC = Double.parseDouble(BIMAST
                    .getLoyaltyAddition_AC());
            // _loyaltyAddition_AC=loyaltyAddition_AC;
            // System.out.println("25.   LoyaltyAddition_AC: "+loyaltyAddition_AC);

            sum_AC += loyaltyAddition_AC;
            _sum_AC = Double.parseDouble(commonForAllProd
                    .getRoundUp(commonForAllProd.getStringWithout_E(sum_AC)));
            if ((month_E % 12) == 0) {

                bussIll.append("<loyalityAdd4Pr"
                        + year_F
                        + ">"
                        + commonForAllProd.getRoundUp(commonForAllProd
                        .getStringWithout_E(_sum_AC))
                        + "</loyalityAdd4Pr" + year_F + ">");
            }

            BIMAST.setFundValueAtEnd_AD();
            fundValueAtEnd_AD = Double.parseDouble(BIMAST
                    .getFundValueAtEnd_AD());
            // _fundValueAtEnd_AD=fundValueAtEnd_AD;
            // System.out.println("26.   FundValueAtEnd_AD: "+fundValueAtEnd_AD);
            // sum_AA=Double.parseDouble(commonForAllProd.getRoundUp(sum_AA+""));
            _sum_AC = Double.parseDouble(commonForAllProd
                    .getRoundUp(commonForAllProd.getStringWithout_E(sum_AC)));
            sum_AD = Double.parseDouble("" + (sum_AA + _sum_AC));
            // System.out.println(" sum AD: "+ sum_AD +
            // " sum_AA : "+sum_AA+" sum_AC : "+sum_AC);
            if ((month_E % 12) == 0) {
                _sum_AD = Double
                        .parseDouble(commonForAllProd
                                .getRoundUp(commonForAllProd
                                        .getStringWithout_E(sum_AD)));
                bussIll.append("<FundValAtEnd4Pr"
                        + year_F
                        + ">"
                        + commonForAllProd.getRoundUp(commonForAllProd
                        .getStringWithout_E(_sum_AD))
                        + "</FundValAtEnd4Pr" + year_F + ">");
                sum_AA = 0;
                sum_AC = 0;
            }

            if ((month_E % 12) == 0) {
                bussIll.append("<FundBefFMC4Pr" + year_F + ">" + Math.round(Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(_sum_AD))) + _sum_Y) + "</FundBefFMC4Pr" + year_F + ">");
            }

            BIMAST.setSurrenderCharges_AE(effectivePremium, premFreqMode);
            surrenderCharges_AE = Double.parseDouble(BIMAST
                    .getSurrenderCharges_AE());
            // _surrenderCharges_AE=surrenderCharges_AE;
            // System.out.println("27.   SurrenderCharges_AE : "+surrenderCharges_AE);
            // sum_AE =Math.round( _surrenderCharges_AE);
            // _sum_AE=Double.parseDouble(commonForAllProd.getRoundUp(sum_AE+""));

            sum_AE = surrenderCharges_AE;
            // _sum_AE=sum_AE;

            BIMAST.setServiceTaxOnSurrenderCharges_AF(prop.surrenderCharges, serviceTax);
            serviceTaxOnSurrenderCharges_AF = Double.parseDouble(BIMAST
                    .getServiceTaxOnSurrenderCharges_AF());
            // _serviceTaxOnSurrenderCharges_AF=serviceTaxOnSurrenderCharges_AF;
            // System.out.println("28.   ServiceTaxOnSurrenderCharges_AF : "+serviceTaxOnSurrenderCharges_AF);

            sum_AF = Double
                    .parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getRoundOffLevel2(commonForAllProd
                            .getStringWithout_E(serviceTaxOnSurrenderCharges_AF))));
            // _sum_AF=Double.parseDouble(commonForAllProd.getRoundUp(sum_AF+""));

            BIMAST.setSurrenderValue_AG();
            surrenderValue_AG = Double.parseDouble(BIMAST
                    .getSurrenderValue_AG());
            // _surrenderValue_AG=surrenderValue_AG;
            // System.out.println("29.   SurrenderValue_AG: "+surrenderValue_AG);
            //
            // sum_AG=Double.parseDouble(""+(_sum_AD-_sum_AE-_sum_AF));
            sum_AG = Double.parseDouble("" + (_sum_AD - sum_AE - sum_AF));
            // _sum_AG=Double.parseDouble(commonForAllProd.getStringWithout_E(sum_AG));
            if ((month_E % 12) == 0) {
                bussIll.append("<SurrVal4Pr"
                        + year_F
                        + ">"
                        + commonForAllProd.getRound(commonForAllProd
                        .getStringWithout_E(sum_AG)) + "</SurrVal4Pr"
                        + year_F + ">");
            }
            //

            BIMAST.setDeathBenefit_AH(sumAssured, policyTerm);
            deathBenefit_AH = Double.parseDouble(BIMAST.getDeathBenefit_AH());
            // _deathBenefit_AH=deathBenefit_AH;
            // System.out.println("30.   DeathBenefit_AH: "+deathBenefit_AH);
            // if((_month_E % 12) == 0)
            // {
            // _cumulative_prem=1.05*sum_of_pre;
            //
            // bussIll.append("<DeathBen4Pr"+ _year_F +">" +
            // commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(Math.max(sumAssured,
            // _cumulative_prem-sum_AD-sum_O-sum_R))) + "</DeathBen4Pr"+ _year_F
            // +">");
            //
            // }

            cumulative_prem = 1.05 * sum_of_prem;
            if ((month_E % 12) == 0) {
                _cumulative_prem = Double.parseDouble(commonForAllProd
                        .getRoundUp(commonForAllProd
                                .getStringWithout_E(cumulative_prem)));
                double _temp = 0;
                _temp = Double.parseDouble(""
                        + (_cumulative_prem - _sum_AD - sum_O - _sum_R));
                _sum_AX = Math.max(sumAssured, _temp);
				/*bussIll.append("<DeathBen4Pr"
						+ year_F
						+ ">"
						+ commonForAllProd.getRoundUp(commonForAllProd
								.getStringWithout_E(deathBenefit_AH)) + "</DeathBen4Pr"
						+ year_F + ">");*/
                bussIll.append("<DeathBen4Pr"
                        + year_F
                        + ">"
                        + commonForAllProd.getRoundUp(commonForAllProd
                        .getStringWithout_E(deathBenefit_AH)) + "</DeathBen4Pr"
                        + year_F + ">");
            }


            BIMAST.setLast2YearAvgFundValue_AR(
                    BIMAST.arrFundValAfterFMCBeforeGA_AR, i);
            last2YearAvgFundValue_AR = Double.parseDouble(BIMAST
                    .getLast2YearAvgFundValue_AR());
            // _last2YearAvgFundValue_AR=last2YearAvgFundValue_AR;
            // System.out.println("40.   Last2YearAvgFundValue_AR: "+last2YearAvgFundValue_AR);

            BIMAST.setLoyaltyAddition_AS(policyTerm);
            loyaltyAddition_AS = Double.parseDouble(BIMAST
                    .getLoyaltyAddition_AS());
            // _loyaltyAddition_AS=loyaltyAddition_AS;
            // System.out.println("41.   LoyaltyAddition_AS: "+loyaltyAddition_AS);

            sum_AS += loyaltyAddition_AS;
            _sum_AS = Double.parseDouble(commonForAllProd
                    .getRound(commonForAllProd.getStringWithout_E(sum_AS)));
            if ((month_E % 12) == 0) {
                bussIll.append("<loyalityAdd8Pr"
                        + year_F
                        + ">"
                        + commonForAllProd.getRoundUp(commonForAllProd
                        .getStringWithout_E(_sum_AS))
                        + "</loyalityAdd8Pr" + year_F + ">");

            }

            BIMAST.setFundValueAtEnd_AT();
            fundValueAtEnd_AT = Double.parseDouble(BIMAST
                    .getFundValueAtEnd_AT());
            // _fundValueAtEnd_AT=fundValueAtEnd_AT;
            // System.out.println("42.   FundValueAtEnd_AT: "+fundValueAtEnd_AT);

			/*BIMAST.setDeathBenefit_AX(sumAssured, policyTerm);
			deathBenefit_AX = Double.parseDouble(BIMAST.getDeathBenefit_AX());
			// _deathBenefit_AX=deathBenefit_AX;
			// System.out.println("46.   DeathBenefit_AX: "+deathBenefit_AX);

			cumulative_prem = 1.05 * sum_of_prem;
			if ((month_E % 12) == 0) {
				_cumulative_prem = Double.parseDouble(commonForAllProd
						.getRoundUp(commonForAllProd
								.getStringWithout_E(cumulative_prem)));
				double _temp = 0;
				_temp = Double.parseDouble(""
						+ (_cumulative_prem - _sum_AT - sum_O - _sum_R));
				_sum_AX = Math.max(sumAssured, _temp);
                bussIll.append("<DeathBen8Pr"
                        + year_F
                        + ">"
                        + commonForAllProd.getRoundUp(commonForAllProd
                        .getStringWithout_E(_sum_AX)) + "</DeathBen8Pr"
                        + year_F + ">");
				sum_O = 0;
				sum_R = 0;
			}*/

            sum_AT = Double.parseDouble("" + (sum_AQ + _sum_AS));
            _sum_AT = Double.parseDouble(commonForAllProd
                    .getRoundUp(commonForAllProd.getStringWithout_E(sum_AT)));
            // System.out.println(" sum AT: "+ sum_AT +
            // " sum_AQ : "+sum_AQ+" sum_AS : "+sum_AS);

            if ((month_E % 12) == 0) {

                bussIll.append("<FundValAtEnd8Pr"
                        + year_F
                        + ">"
                        + commonForAllProd.getRoundUp(commonForAllProd
                        .getStringWithout_E(_sum_AT))
                        + "</FundValAtEnd8Pr" + year_F + ">");
                sum_AQ = 0;
                sum_AS = 0;
            }

            if ((month_E % 12) == 0) {
                bussIll.append("<FundBefFMC8Pr" + year_F + ">" + Math.round(Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(_sum_AT))) + _sum_AO) + "</FundBefFMC8Pr" + year_F + ">");
            }


            BIMAST.setSurrenderCharges_AU(effectivePremium, premFreqMode);
            surrenderCharges_AU = Double.parseDouble(BIMAST
                    .getSurrenderCharges_AU());
            // _surrenderCharges_AU=surrenderCharges_AU;
            // System.out.println("43.   SurrenderCharges_AU: "+surrenderCharges_AU);

            // sum_AU =
            // Double.parseDouble(""+(Math.round(_surrenderCharges_AU)));
            // _sum_AU=Double.parseDouble(""+sum_AU);
            // sum_AU =
            // Double.parseDouble(""+(Math.round(_surrenderCharges_AU)));

            sum_AU = Double.parseDouble("" + surrenderCharges_AU);

            BIMAST.setServiceTaxOnSurrenderCharges_AV(prop.surrenderCharges, serviceTax);
            serviceTaxOnSurrenderCharges_AV = Double.parseDouble(BIMAST
                    .getServiceTaxOnSurrenderCharges_AV());
            // _serviceTaxOnSurrenderCharges_AV=serviceTaxOnSurrenderCharges_AV;
            // System.out.println("44.   ServiceTaxOnSurrenderCharges_AV: "+serviceTaxOnSurrenderCharges_AV);
            sum_AV = Double
                    .parseDouble(""
                            + (commonForAllProd.getRoundUp(commonForAllProd.getRoundOffLevel2(commonForAllProd
                            .getStringWithout_E(serviceTaxOnSurrenderCharges_AV)))));
            // _sum_AV=Double.parseDouble(sum_AV+"");

            BIMAST.setSurrenderValue_AW();
            surrenderValue_AW = Double.parseDouble(BIMAST
                    .getSurrenderValue_AW());
            // _surrenderValue_AW=surrenderValue_AW;
            // System.out.println("45.   SurrenderValue_AW: "+surrenderValue_AW);

            // System.out.println("sum_AV: "+sum_AV+" sum_AU: "+sum_AU+" _sum_AT: "+_sum_AT+" _sum_AV: "+_sum_AV+" _sum_AU: "+_sum_AU);

            sum_AW = Double.parseDouble("" + (_sum_AT - sum_AU - sum_AV));
            // _sum_AW=Double.parseDouble(commonForAllProd.getRoundUp(sum_AW+""));
            if ((month_E % 12) == 0) {
                // _sum_AW=sum_AW;
                // System.out.println("sum_AV: "+sum_AV+" sum_AU: "+sum_AU+" _sum_AT: "+_sum_AT+" _sum_AV: "+_sum_AV+" _sum_AU: "+_sum_AU
                // +"  sum_AW "+commonForAllProd.getRound(String.valueOf(sum_AW)));
                bussIll.append("<SurrVal8Pr"
                        + year_F
                        + ">"
                        + commonForAllProd.getRound(commonForAllProd
                        .getStringWithout_E(sum_AW)) + "</SurrVal8Pr"
                        + year_F + ">");
            }

            BIMAST.setDeathBenefit_AX(sumAssured, policyTerm);
            deathBenefit_AX = Double.parseDouble(BIMAST.getDeathBenefit_AX());
            // _deathBenefit_AX=deathBenefit_AX;
            // System.out.println("46.   DeathBenefit_AX: "+deathBenefit_AX);

            cumulative_prem = 1.05 * sum_of_prem;
            if ((month_E % 12) == 0) {
                _cumulative_prem = Double.parseDouble(commonForAllProd
                        .getRoundUp(commonForAllProd
                                .getStringWithout_E(cumulative_prem)));
                double _temp = 0;
                _temp = Double.parseDouble(""
                        + (_cumulative_prem - _sum_AT - sum_O - _sum_R));
                _sum_AX = Math.max(sumAssured, _temp);
                bussIll.append("<DeathBen8Pr"
                        + year_F
                        + ">"
                        + commonForAllProd.getRoundUp(commonForAllProd
                        .getStringWithout_E(deathBenefit_AX)) + "</DeathBen8Pr"
                        + year_F + ">");
                sum_O = 0;
                sum_R = 0;
            }


            //
            if ((month_E % 12) == 0) {
                // System.out.println("aetfef"+BIMAST.getCommission_AP(_sum_J,_sum_K,smartScholarBean));
                Commission_AP = BIMAST.getCommission_AP(_sum_J, _sum_K,
                        smartScholarBean);
                // _Commission_AP=Commission_AP;
                // System.out.println(year_F+" Commission_AP = "+Commission_AP);
                bussIll.append("<CommIfPay8Pr"
                        + year_F
                        + ">"
                        + commonForAllProd.getRoundUp(commonForAllProd
                        .getStringWithout_E(Commission_AP))
                        + "</CommIfPay8Pr" + year_F + ">");
                Commission_AP = 0;
            }
        }

        AccBenefitSumAssured = BIMAST.getAccidentBenefitSA(premFreqMode,
                sumAssured);

        /********* Added by Akshaya on 19-MAR-15 start ************************/
        PPWBSumAssured = BIMAST.getPpwbSA(premFreqMode,
                smartScholarBean.getPremiumAmount());
        /********* Added by Akshaya on 19-MAR-15 end ************************/

        // String
        // T=commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(_fundValueAfterFMCAndBeforeGA_AA));
        // String
        // U=commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(_loyaltyAddition_AC));
        // //System.out.println("*T* -> "+T);
        // //System.out.println("*U* -> "+U);
        // String
        // AI=commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(_fundValueAfterFMCandBeforeGA_AQ));
        // String
        // AJ=commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(_loyaltyAddition_AS));
        // System.out.println("*AI* -> "+AI);
        // System.out.println("*AJ* -> "+AJ);
        // System.out.println("****************************** Final Output ****************************************");
        // System.out.println("Sum Assured -> "+(sumAssured));
        // System.out.println("Fund Value @6% [V] -> "+(Double.parseDouble(T)+Double.parseDouble(U)));
        // System.out.println("Fund Value @10% [AK] -> "+(Double.parseDouble(AI)+Double.parseDouble(AJ)));
        // System.out.println("***********************************************************************************");
        // Reset Values

        BIMAST.arrFundValAfterFMCBeforeGA = null;
        BIMAST.arrFundValAfterFMCBeforeGA_AR = null;
        // return new
        // String[]{(commonForAllProd.getStringWithout_E(sumAssured)),(commonForAllProd.getStringWithout_E(Double.parseDouble(T)+Double.parseDouble(U))),(commonForAllProd.getStringWithout_E(Double.parseDouble(AI)+Double.parseDouble(AJ))),(String.valueOf(smartScholarBean.getEffectivePremium()))};

        /********* Added by Akshaya on 19-MAR-15 start ************************/
        return new String[]{
                (commonForAllProd.getStringWithout_E(sumAssured)),
                commonForAllProd.getRoundUp(commonForAllProd
                        .getStringWithout_E(fundValueAtEnd_AD)),
                commonForAllProd.getRoundUp(commonForAllProd
                        .getStringWithout_E(fundValueAtEnd_AT)),
                (String.valueOf(smartScholarBean.getEffectivePremium())),
                commonForAllProd.getStringWithout_E(AccBenefitSumAssured),
                commonForAllProd.getStringWithout_E(PPWBSumAssured)};
        /********* Added by Akshaya on 19-MAR-15 end ************************/

    }

    private String[] getOutputReductionYield(String sheetName,
                                             SmartScholarBean smartScholarBean) {

        commonForAllProd = new CommonForAllProd();

        ArrayList<String> List_BB = new ArrayList<>();
        ArrayList<String> List_BC = new ArrayList<>();
        ArrayList<String> List_BG = new ArrayList<>();

        // Output Variable Declaration
        double _oneHundredPercentOfCummulativePremium_AZ = 0; //BY18
        // double _policyAdministrationCharge_R=0; //S18=0
        // double _fundValueAfterFMCandBeforeGA_AQ=0;
        // double _fundValueAtEnd_AD=0; //AQ18
        // double _fundValueAfterFMCAndBeforeGA_AA=0;
        // int _month_E=0;
        int _year_F = 0;
        // String _policyInForce_G="Y";
        // int _childsAge_H=0;
        // int _proposersAge_I=0,
        //
        // _month_BA=0;
        // double _premium_J=0;
        // double _topUpPremium_K=0;
        // double _premiumAllocationCharge_L=0;
        // double _topUpCharges_M=0;
        // double _serviceTaxOnAllocation_N=0;
        // double _amountAvailableForInvestment_O=0;
        // double _sumAssuredRelatedCharges_P=0;
        // double _accidentCoverCharges_Q=0;
        // double _last2YearAvgFundValue_AB=0;
        // double _last2YearAvgFundValue_AR=0;
        // double _fundValueAtEnd_AT=0;
        // double _loyaltyAddition_AC=0;
        // double _loyaltyAddition_AS=0;
        // double _mortalityAndPPWBCharges_S=0;
        // double _totalCharges_T=0;
        // double _totalServiceTax_exclOfSTonFMC_U=0;
        // double _additionToFundIfAny_AM=0;
        // double _fundBeforeFMC_X=0;
        // double _fundManagementCharge_Y=0;
        // double _serviceTaxOnFMC_Z=0;
        // double _serviceTaxOnFMC_AP=0;
        // double _totalServiceTax_V=0;
        // double _surrenderCap_AY=0;
        // double _surrenderCharges_AE=0;
        // double _serviceTaxOnSurrenderCharges_AF=0;
        // double _surrenderValue_AG=0;
        // double _deathBenefit_AH=0;
        // double _mortalityAndPPWBCharges_AI=0;
        // double _totalCharges_AJ=0;
        // double _additionToFundIfAny_W=0;
        // double _fundBeforeFMC_AN=0;
        // double _fundManagementCharge_AO=0;
        // double _totalServiceTax_exclOfSTonFMC_AK=0;
        // double _deathBenefit_AX=0;
        // double _totalServiceTax_AL=0;
        // double _surrenderCharges_AU=0;
        // double _serviceTaxOnSurrenderCharges_AV=0;
        // double _surrenderValue_AW=0;

        // Temp Variable Declaretion
        int month_E = 0, month_BA = 0;
        int year_F = 0;
        String policyInForce_G = "Y";
        int childsAge_H = 0;
        int proposersAge_I = 0;
        double premium_J = 0;
        double topUpPremium_K = 0;
        double premiumAllocationCharge_L = 0;
        double topUpCharges_M = 0;
        double serviceTaxOnAllocation_N = 0;
        double amountAvailableForInvestment_O = 0;
        double sumAssuredRelatedCharges_P = 0;
        double accidentCoverCharges_Q = 0;
        double fundValueAtEnd_AT = 0;
        double policyAdministrationCharge_R = 0;
        double oneHundredPercentOfCummulativePremium_AZ = 0;
        double mortalityAndPPWBCharges_S = 0;
        double totalCharges_T = 0;
        double totalServiceTax_exclOfSTonFMC_U = 0;
        double additionToFundIfAny_AM = 0;
        double surrenderCharges_AE = 0;
        double fundBeforeFMC_X = 0;
        double fundManagementCharge_Y = 0;
        double serviceTaxOnFMC_Z = 0;
        double serviceTaxOnFMC_AP = 0;
        double loyaltyAddition_AC = 0;
        double loyaltyAddition_AS = 0;
        double totalServiceTax_V = 0;
        double fundValueAfterFMCandBeforeGA_AQ = 0;
        double fundValueAtEnd_AD = 0;
        double surrenderCap_AY = 0;
        double serviceTaxOnSurrenderCharges_AF = 0;
        double surrenderValue_AG = 0;
        double deathBenefit_AH = 0;
        double mortalityAndPPWBCharges_AI = 0;
        double totalCharges_AJ = 0;
        double additionToFundIfAny_W = 0;
        double fundBeforeFMC_AN = 0;
        double fundManagementCharge_AO = 0;
        double totalServiceTax_exclOfSTonFMC_AK = 0;
        double fundValueAfterFMCAndBeforeGA_AA = 0;
        double deathBenefit_AX = 0;
        double last2YearAvgFundValue_AB = 0;
        double last2YearAvgFundValue_AR = 0;
        double totalServiceTax_AL = 0;
        double surrenderCharges_AU = 0;
        double serviceTaxOnSurrenderCharges_AV = 0;
        double surrenderValue_AW = 0, reductionYield_BU = 0, reductionYield_BQ = 0, irrAnnual_BQ = 0, irrAnnual_BU = 0, reductionInYieldMaturityAt = 0, reductionInYieldNumberOfYearsElapsedSinceInception = 0,
                // _reductionInYieldNumberOfYearsElapsedSinceInception=0,
                // _reductionInYieldMaturityAt=0,
                reductionYield_BB = 0,
                // _reductionYield_BB=0,
                reductionYield_BC = 0,
                // _reductionYield_BC=0,
                irrAnnual_BB = 0, irrAnnual_BC = 0,
                // _irrAnnual_BB=0,
                // _irrAnnual_BC=0,
                reductionYield_BG = 0,
                // _reductionYield_BG=0,
                // _irrAnnual_BG=0,
                irrAnnual_BG = 0, netYield4pa = 0, netYield8pa = 0;

        // From GUI Input
        boolean staffDisc = smartScholarBean.getIsForStaffOrNot();
        boolean bancAssuranceDisc = smartScholarBean
                .getIsBancAssuranceDiscOrNot();
        String premFreqMode = smartScholarBean.getPremFreqMode();
        int premiumPayingTerm = smartScholarBean.getPremiumPayingTerm();
        double SAMF = smartScholarBean.getSAMF();
        int ageOfChild = smartScholarBean.getAgeOfChild();
        int ageOfProposer = smartScholarBean.getAgeOfProposer();
        int noOfYearsElapsedSinceInception = smartScholarBean
                .getYearsOfDiscontinuance();
        //double serviceTax=smartScholarBean.getServiceTax();
        double serviceTax = 0;
        boolean isKerlaDisc = smartScholarBean.isKerlaDisc();
        if (sheetName.equals("Reduction in Yeild_CAP")) {
            // Input Drop Downs from "BI_Incl_Mort & Ser Tax" sheet
            prop.allocationCharges = false; // *Sheet Name -> BI_Incl_Mort & Ser
            // Tax,*Cell ->B20
            prop.mortalityAndRiderCharges = false; // *Sheet Name ->
            // BI_Incl_Mort & Ser
            // Tax,*Cell ->B21
            prop.administrationCharges = false; // *Sheet Name -> BI_Incl_Mort &
            // Ser Tax,*Cell ->B22
            prop.fundManagementCharges = false; // *Sheet Name -> BI_Incl_Mort &
            // Ser Tax,*Cell ->B23
            prop.mortalityCharges = false; // *Sheet Name -> BI_Incl_Mort & Ser
            // Tax,*Cell ->B27
            prop.optionCharges = false; // *Sheet Name -> BI_Incl_Mort & Ser
            // Tax,*Cell ->B29
            prop.topUpStatus = false; // *Sheet Name -> BI_Incl_Mort & Ser
            // Tax,*Cell ->B31
            prop.surrenderCharges = false; // *Sheet Name -> BI_Incl_Mort & Ser
            // Tax,*Cell ->B24
        }

        // Internally Calculated Input Fields
        double effectivePremium = getAnnualPremium(premFreqMode, smartScholarBean.getPremiumAmount(), premiumPayingTerm);
        int PF = getPF(premFreqMode);
        int policyTerm = smartScholarBean.getPolicyTerm_Basic();
        // System.out.println("policy term : "+policyTerm);
        double sumAssured = (effectivePremium * SAMF);
        // Declaration of method Variables/Object required for calculation
        SmartScholarBusinessLogic BIMAST = new SmartScholarBusinessLogic();
        String[] forBIArray = BIMAST
                .getForBIArr(prop.mortalityCharges_AsPercentOfofLICtable);
        int rowNumber = 0, monthNumber = 0;
        double _prem_J = 0;
        /*** Modified by Priyanka Warekar - 17-3-2015 ****/
        // for (int i = 0; i <= 24; i++)
        for (int i = 0; i <= 23; i++)
        /*** Modified end- Priyanka Warekar - 17-3-2015 ****/ {
            if (i == 18) {
                BIMAST.arrFundValAfterFMCBeforeGA[i] = 23.00;
            } else {
                BIMAST.arrFundValAfterFMCBeforeGA[i] = 0;
            }
        }
        /*** Modified by Priyanka Warekar - 17-3-2015 ****/
        // int currentElement=25;
        int currentElement = 24;
        // for (int i = 0; i <= 24; i++)
        for (int i = 0; i <= 23; i++)
        /*** Modified end- Priyanka Warekar - 17-3-2015 ****/ {
            if (i == 18) {
                BIMAST.arrFundValAfterFMCBeforeGA_AR[i] = 39.00;
            } else {
                BIMAST.arrFundValAfterFMCBeforeGA_AR[i] = 0;
            }
        }

        int currentElement_AR = 24;
        for (int i = 0; i <= (policyTerm * 12); i++)
        // for(int i=0; i <= 30; i++)
        {
            rowNumber++;
            // System.out.println("********************************************* "+i+" Row Output *********************************************");
            BIMAST.setMonth_E(rowNumber);
            month_E = Integer.parseInt(BIMAST.getMonth_E());
            // _month_E=month_E;
            // System.out.println("1.   Month_E : "+BIMAST.getMonth_E());

            BIMAST.setYear_F();
            year_F = Integer.parseInt(BIMAST.getYear_F());
            // _year_F=year_F;
            // System.out.println("2.   Year_F : "+BIMAST.getYear_F());
            if (isKerlaDisc == true && year_F <= 2) {
                serviceTax = 0.19;
            } else {
                serviceTax = 0.18;
            }
            policyInForce_G = BIMAST.getPolicyInForce_G();
            // _policyInForce_G=BIMAST.getPolicyInForce_G();
            // System.out.println("3.   PolicyInForce_G : "+BIMAST.getPolicyInForce_G());

            BIMAST.setChildsAge_H(ageOfChild);
            childsAge_H = Integer.parseInt(BIMAST.getChildsAge_H());
            // _childsAge_H=childsAge_H;
            // System.out.println("4.   ChildsAge_H : "+BIMAST.getChildsAge_H());

            BIMAST.setProposersAge_I(ageOfProposer);
            proposersAge_I = Integer.parseInt(BIMAST.getProposersAge_I());
            // _proposersAge_I=proposersAge_I;
            // System.out.println("5.   ProposersAge_I : "+BIMAST.getProposersAge_I());

            BIMAST.setPremium_J(premiumPayingTerm, PF, effectivePremium);
            premium_J = Double.parseDouble(BIMAST.getPremium_J());
            // _premium_J=premium_J;
            if (i != 0) {
                _prem_J = _prem_J + premium_J;
            }
            // System.out.println("6.   Premium_J : "+BIMAST.getPremium_J());

            BIMAST.setOneHundredPercentOfCummulativePremium_AZ(_oneHundredPercentOfCummulativePremium_AZ);
            oneHundredPercentOfCummulativePremium_AZ = Double
                    .parseDouble(BIMAST
                            .getOneHundredPercentOfCummulativePremium_AZ());
            // _oneHundredPercentOfCummulativePremium_AZ=oneHundredPercentOfCummulativePremium_AZ;
            // System.out.println("48.   OneHundredPercentOfCummulativePremium_AZ : "+oneHundredPercentOfCummulativePremium_AZ);

            BIMAST.setTopUpPremium_K(prop.topUpStatus, prop.effectiveTopUpPrem);
            topUpPremium_K = Double.parseDouble(BIMAST.getTopUpPremium_K());
            // _topUpPremium_K=topUpPremium_K;
            // System.out.println("7.   TopUpPremium_K : "+BIMAST.getTopUpPremium_K());

            BIMAST.setPremiumAllocationCharge_L(staffDisc, bancAssuranceDisc,
                    premFreqMode);
            premiumAllocationCharge_L = Double.parseDouble(BIMAST
                    .getPremiumAllocationCharge_L());
            // _premiumAllocationCharge_L=premiumAllocationCharge_L;
            // System.out.println("8.   PremiumAllocationCharge_L : "+BIMAST.getPremiumAllocationCharge_L());

            BIMAST.setTopUpCharges_M(prop.topUp);
            topUpCharges_M = Double.parseDouble(BIMAST.getTopUpCharges_M());
            // _topUpCharges_M=topUpCharges_M;
            // System.out.println("9.   TopUpCharges_M : "+BIMAST.getTopUpCharges_M());

            BIMAST.setServiceTaxOnAllocation_N(prop.allocationCharges,
                    serviceTax);
            serviceTaxOnAllocation_N = Double.parseDouble(BIMAST
                    .getServiceTaxOnAllocation_N());
            // _serviceTaxOnAllocation_N=serviceTaxOnAllocation_N;
            // System.out.println("10.   ServiceTaxOnAllocation_N : "+BIMAST.getServiceTaxOnAllocation_N());

            BIMAST.setAmountAvailableForInvestment_O();
            amountAvailableForInvestment_O = Double.parseDouble(BIMAST
                    .getAmountAvailableForInvestment_O());
            // _amountAvailableForInvestment_O=amountAvailableForInvestment_O;
            // System.out.println("11.   AmountAvailableForInvestment_O : "+BIMAST.getAmountAvailableForInvestment_O());

            BIMAST.setSumAssuredRelatedCharges_P(policyTerm, sumAssured, SAMF,
                    effectivePremium, prop.charge_SumAssuredBase,
                    prop.chargeSAren);
            sumAssuredRelatedCharges_P = Double.parseDouble(BIMAST
                    .getSumAssuredRelatedCharges_P());
            // _sumAssuredRelatedCharges_P=sumAssuredRelatedCharges_P;
            // System.out.println("12.   SumAssuredRelatedCharges_P : "+BIMAST.getSumAssuredRelatedCharges_P());

            BIMAST.setAccidentCoverCharges_Q(prop.optionCharges, premFreqMode,
                    sumAssured);
            accidentCoverCharges_Q = Double.parseDouble(BIMAST
                    .getAccidentCoverCharges_Q());
            // _accidentCoverCharges_Q=accidentCoverCharges_Q;
            // System.out.println("13.   AccidentCoverCharges_Q : "+BIMAST.getAccidentCoverCharges_Q());

            BIMAST.setMonth_BA(monthNumber);
            month_BA = Integer.parseInt(BIMAST.getMonth_BA());
            // _month_BA=month_BA;
            // System.out.println("month_bo "+month_BA);

            BIMAST.setReductionYield_BB(policyTerm, fundValueAtEnd_AD);
            reductionYield_BB = Double.parseDouble(BIMAST
                    .getReductionYield_BB());
            // _reductionYield_BB=reductionYield_BB;
            // System.out.println("reductionYield_BB "+reductionYield_BB);

            BIMAST.setReductionYield_BC(policyTerm, fundValueAtEnd_AT);
            reductionYield_BC = Double.parseDouble(BIMAST
                    .getReductionYield_BC());
            // _reductionYield_BC=reductionYield_BC;
            // System.out.println("reductionYield_BC "+reductionYield_BC);

            BIMAST.setReductionYield_BG(noOfYearsElapsedSinceInception,
                    fundValueAtEnd_AT);
            reductionYield_BG = Double.parseDouble(BIMAST
                    .getReductionYield_BG());
            // _reductionYield_BG=reductionYield_BG;
            // System.out.println("reductionYield_BU "+reductionYield_BU);

            BIMAST.setPolicyAdministrationCharge_R(policyAdministrationCharge_R);
            policyAdministrationCharge_R = Double.parseDouble(BIMAST
                    .getPolicyAdministrationCharge_R());
            // _policyAdministrationCharge_R=policyAdministrationCharge_R;
            // System.out.println("14.   PolicyAdministrationCharge_R : "+BIMAST.getPolicyAdministrationCharge_R());

			/*BIMAST.setMortalityAndPPWBChargesReductionYeild_S(prop.PPWBstatus,
					premFreqMode, effectivePremium, forBIArray, ageOfProposer,
					premiumPayingTerm, policyTerm, prop.mortalityCharges,
					sumAssured, PF);*/
            BIMAST.setMortalityAndPPWBChargesReductionYeild_S(fundValueAtEnd_AD, policyTerm, forBIArray, sumAssured, prop.mortalityCharges);
            mortalityAndPPWBCharges_S = Double.parseDouble(BIMAST
                    .getMortalityAndPPWBChargesReductionYeild_S());
            // _mortalityAndPPWBCharges_S=mortalityAndPPWBCharges_S;
            // System.out.println("15.   MortalityAndPPWBCharges_S : "+mortalityAndPPWBCharges_S);

            BIMAST.setTotalChargesReductionYeild_T(ageOfProposer, effectivePremium, premFreqMode, prop.PPWBstatus, premiumPayingTerm, PF, policyTerm, prop.mortalityCharges);
            totalCharges_T = Double.parseDouble(BIMAST
                    .getTotalChargesReductionYeild_T());
            // _totalCharges_T=totalCharges_T;
            // System.out.println("16.   TotalCharges_T : "+totalCharges_T);

            BIMAST.setTotalServiceTax_exclOfSTonFMC_U(serviceTax,
                    prop.mortalityAndRiderCharges, prop.administrationCharges,
                    prop.guaranteeCharges, ageOfProposer, effectivePremium, premFreqMode, prop.PPWBstatus, premiumPayingTerm, PF, prop.mortalityCharges);
            totalServiceTax_exclOfSTonFMC_U = Double.parseDouble(BIMAST
                    .getTotalServiceTax_exclOfSTonFMC_U());
            // _totalServiceTax_exclOfSTonFMC_U=totalServiceTax_exclOfSTonFMC_U;
            // System.out.println("17.   TotalServiceTax_exclOfSTonFMC_U : "+totalServiceTax_exclOfSTonFMC_U);

            BIMAST.setSurrenderCap_AY(premFreqMode, effectivePremium);
            surrenderCap_AY = Double.parseDouble(BIMAST.getSurrenderCap_AY());
            // _surrenderCap_AY=surrenderCap_AY;
            // System.out.println("47.   SurrenderCap_AY : "+surrenderCap_AY);

            BIMAST.setDeathBenefit_AH(sumAssured, policyTerm);
            deathBenefit_AH = Double.parseDouble(BIMAST.getDeathBenefit_AH());
            // _deathBenefit_AH=deathBenefit_AH;
            // System.out.println("30.   DeathBenefit_AH: "+deathBenefit_AH);

			/*BIMAST.setMortalityAndPPWBChargesReductionYeild_AI(prop.PPWBstatus,
					premFreqMode, effectivePremium, forBIArray, ageOfProposer,
					premiumPayingTerm, policyTerm, prop.mortalityCharges,
					sumAssured, PF);*/

            BIMAST.setMortalityAndPPWBChargesReductionYeild_AI(fundValueAtEnd_AD, policyTerm, forBIArray, sumAssured, prop.mortalityCharges);
            mortalityAndPPWBCharges_AI = Double.parseDouble(BIMAST
                    .getMortalityAndPPWBChargesReductionYeild_AI());
            // _mortalityAndPPWBCharges_AI=mortalityAndPPWBCharges_AI;
            // System.out.println("31.   MortalityAndPPWBCharges_AI : "+mortalityAndPPWBCharges_AI);

            BIMAST.setTotalChargesReductionYeild_AJ(ageOfProposer, effectivePremium, premFreqMode, prop.PPWBstatus, premiumPayingTerm, PF, policyTerm, prop.mortalityCharges);
            totalCharges_AJ = Double.parseDouble(BIMAST
                    .getTotalChargesReductionYeild_AJ());
            // _totalCharges_AJ=totalCharges_AJ;
            // System.out.println("32.   TotalCharges_AJ : "+totalCharges_AJ);

            BIMAST.setAdditionToFundIfAny_W(policyTerm, fundValueAtEnd_AD);
            additionToFundIfAny_W = Double.parseDouble(BIMAST
                    .getAdditionToFundIfAny_W());
            // _additionToFundIfAny_W=additionToFundIfAny_W;
            // System.out.println("19.   AdditionToFundIfAny_W: "+additionToFundIfAny_W);

            BIMAST.setFundBeforeFMC_X(policyTerm, fundValueAtEnd_AD);
            fundBeforeFMC_X = Double.parseDouble(BIMAST.getFundBeforeFMC_X());
            // _fundBeforeFMC_X=fundBeforeFMC_X;
            // System.out.println("20.   FundBeforeFMC_X : "+fundBeforeFMC_X);

            BIMAST.setFundManagementCharge_Y(policyTerm, smartScholarBean
                            .getPercentToBeInvested_EquityFund(), smartScholarBean
                            .getPercentToBeInvested_EquityOptimiserFund(),
                    smartScholarBean.getPercentToBeInvested_GrowthFund(),
                    smartScholarBean.getPercentToBeInvested_BalancedFund(),
                    smartScholarBean.getPercentToBeInvested_BondFund(),
                    smartScholarBean.getPercentToBeInvested_MoneyMarketFund(),
                    smartScholarBean.getPercentToBeInvested_BondOptimiserFund2(),
                    smartScholarBean.getPercentToBeInvested_Top300Fund(),
                    smartScholarBean.getPercentToBeInvested_PureFund());
            fundManagementCharge_Y = Double.parseDouble(BIMAST
                    .getFundManagementCharge_Y());
            // _fundManagementCharge_Y=fundManagementCharge_Y;
            // System.out.println("21.   FundManagementCharge_Y : "+fundManagementCharge_Y);

            BIMAST.setServiceTaxOnFMCReductionYeild_Z(
                    prop.fundManagementCharges, smartScholarBean
                            .getPercentToBeInvested_EquityFund(),
                    smartScholarBean
                            .getPercentToBeInvested_EquityOptimiserFund(),
                    smartScholarBean.getPercentToBeInvested_GrowthFund(),
                    smartScholarBean.getPercentToBeInvested_BalancedFund(),
                    smartScholarBean.getPercentToBeInvested_BondFund(),
                    smartScholarBean.getPercentToBeInvested_MoneyMarketFund(),
                    smartScholarBean.getPercentToBeInvested_BondOptimiserFund2(),
                    smartScholarBean.getPercentToBeInvested_Top300Fund(),
                    smartScholarBean.getPercentToBeInvested_PureFund(), serviceTax);
            serviceTaxOnFMC_Z = Double.parseDouble(BIMAST
                    .getServiceTaxOnFMCReductionYeild_Z());
            // _serviceTaxOnFMC_Z=serviceTaxOnFMC_Z;
            // System.out.println("22.   ServiceTaxOnFMC_Z : "+serviceTaxOnFMC_Z);

            BIMAST.setTotalServiceTaxReductionYeild_V();
            totalServiceTax_V = Double.parseDouble(BIMAST
                    .getTotalServiceTaxReductionYeild_V());
            // _totalServiceTax_V=totalServiceTax_V;
            // System.out.println("18.   TotalServiceTax_V : "+totalServiceTax_V);

            BIMAST.setTotalServiceTax_exclOfSTonFMC_AK(serviceTax,
                    prop.mortalityAndRiderCharges, prop.administrationCharges,
                    prop.guaranteeCharges, ageOfProposer, effectivePremium, premFreqMode, prop.PPWBstatus, premiumPayingTerm, PF, prop.mortalityCharges);
            totalServiceTax_exclOfSTonFMC_AK = Double.parseDouble(BIMAST
                    .getTotalServiceTax_exclOfSTonFMC_AK());
            // _totalServiceTax_exclOfSTonFMC_AK=totalServiceTax_exclOfSTonFMC_AK;
            // System.out.println("33.   TotalServiceTax_exclOfSTonFMC_AK : "+totalServiceTax_exclOfSTonFMC_AK);

            BIMAST.setAdditionToFundIfAny_AM(fundValueAtEnd_AT, policyTerm);
            additionToFundIfAny_AM = Double.parseDouble(BIMAST
                    .getAdditionToFundIfAny_AM());
            // _additionToFundIfAny_AM=additionToFundIfAny_AM;
            // System.out.println("35.   AdditionToFundIfAny_AM : "+additionToFundIfAny_AM);

            BIMAST.setFundBeforeFMC_AN(policyTerm, fundValueAtEnd_AT);
            fundBeforeFMC_AN = Double.parseDouble(BIMAST.getFundBeforeFMC_AN());
            // _fundBeforeFMC_AN=fundBeforeFMC_AN;
            // System.out.println("36.   FundBeforeFMC_AN: "+fundBeforeFMC_AN);

            BIMAST.setFundManagementCharge_AO(policyTerm, smartScholarBean
                            .getPercentToBeInvested_EquityFund(), smartScholarBean
                            .getPercentToBeInvested_EquityOptimiserFund(),
                    smartScholarBean.getPercentToBeInvested_GrowthFund(),
                    smartScholarBean.getPercentToBeInvested_BalancedFund(),
                    smartScholarBean.getPercentToBeInvested_BondFund(),
                    smartScholarBean.getPercentToBeInvested_MoneyMarketFund(),
                    smartScholarBean.getPercentToBeInvested_BondOptimiserFund2(),
                    smartScholarBean.getPercentToBeInvested_Top300Fund(),
                    smartScholarBean.getPercentToBeInvested_PureFund());
            fundManagementCharge_AO = Double.parseDouble(BIMAST
                    .getFundManagementCharge_AO());
            // _fundManagementCharge_AO=fundManagementCharge_AO;
            // System.out.println("37.   FundManagementCharge_AO: "+fundManagementCharge_AO);

            BIMAST.setServiceTaxOnFMCReductionYeild_AP(
                    prop.fundManagementCharges, smartScholarBean
                            .getPercentToBeInvested_EquityFund(),
                    smartScholarBean
                            .getPercentToBeInvested_EquityOptimiserFund(),
                    smartScholarBean.getPercentToBeInvested_GrowthFund(),
                    smartScholarBean.getPercentToBeInvested_BalancedFund(),
                    smartScholarBean.getPercentToBeInvested_BondFund(),
                    smartScholarBean.getPercentToBeInvested_MoneyMarketFund(),
                    smartScholarBean.getPercentToBeInvested_BondOptimiserFund2(),
                    smartScholarBean.getPercentToBeInvested_Top300Fund(),
                    smartScholarBean.getPercentToBeInvested_PureFund(), serviceTax);
            serviceTaxOnFMC_AP = Double.parseDouble(BIMAST
                    .getServiceTaxOnFMCReductionYeild_AP());
            // _serviceTaxOnFMC_AP=serviceTaxOnFMC_AP;
            // System.out.println("38.   ServiceTaxOnFMC_AP : "+serviceTaxOnFMC_AP);

            BIMAST.setTotalServiceTaxReductionYeild_AL();
            totalServiceTax_AL = Double.parseDouble(BIMAST
                    .getTotalServiceTaxReductionYeild_AL());
            // _totalServiceTax_AL=totalServiceTax_AL;
            // System.out.println("34.   TotalServiceTax_AL: "+totalServiceTax_AL);

            BIMAST.setFundValueAfterFMCAndBeforeGAReductionYeild_AA(policyTerm);
            fundValueAfterFMCAndBeforeGA_AA = Double.parseDouble(BIMAST
                    .getFundValueAfterFMCAndBeforeGAReductionYeild_AA());
            // _fundValueAfterFMCAndBeforeGA_AA=fundValueAfterFMCAndBeforeGA_AA;
            BIMAST.arrFundValAfterFMCBeforeGA[currentElement] = fundValueAfterFMCAndBeforeGA_AA;
            currentElement++;
            // System.out.println("23.   FundValueAfterFMCAndBeforeGA_AA: "+fundValueAfterFMCAndBeforeGA_AA);

            BIMAST.setFundValueAfterFMCAndBeforeGAReductionYeild_AQ(policyTerm);
            fundValueAfterFMCandBeforeGA_AQ = Double.parseDouble(BIMAST
                    .getFundValueAfterFMCAndBeforeGAReductionYeild_AQ());
            // _fundValueAfterFMCandBeforeGA_AQ=fundValueAfterFMCandBeforeGA_AQ;
            BIMAST.arrFundValAfterFMCBeforeGA_AR[currentElement_AR] = fundValueAfterFMCandBeforeGA_AQ;
            currentElement_AR++;
            // System.out.println("39.   FundValueAfterFMCAndBeforeGA_AQ: "+fundValueAfterFMCandBeforeGA_AQ);

            BIMAST.setLast2YearAvgFundValue_AB(
                    BIMAST.arrFundValAfterFMCBeforeGA, i);
            last2YearAvgFundValue_AB = Double.parseDouble(BIMAST
                    .getLast2YearAvgFundValue_AB());
            // _last2YearAvgFundValue_AB=last2YearAvgFundValue_AB;
            // System.out.println("24.   Last2YearAvgFundValue_AB: "+last2YearAvgFundValue_AB);

            BIMAST.setLoyaltyAddition_AC(policyTerm);
            loyaltyAddition_AC = Double.parseDouble(BIMAST
                    .getLoyaltyAddition_AC());
            // _loyaltyAddition_AC=loyaltyAddition_AC;
            // System.out.println("25.   LoyaltyAddition_AC: "+loyaltyAddition_AC);

            BIMAST.setFundValueAtEndReductionYeild_AD();
            fundValueAtEnd_AD = Double.parseDouble(BIMAST
                    .getFundValueAtEndReductionYeild_AD());
            // _fundValueAtEnd_AD=fundValueAtEnd_AD;
			/*System.out.println("26.   month_E: "+month_E);
			 System.out.println("26.   month_E: "+fundValueAtEnd_AD);*/

            BIMAST.setSurrenderChargesReductionYeild_AE(effectivePremium,
                    premFreqMode);
            surrenderCharges_AE = Double.parseDouble(BIMAST
                    .getSurrenderChargesReductionYeild_AE());
            // _surrenderCharges_AE=surrenderCharges_AE;
            // System.out.println("27.   SurrenderCharges_AE : "+surrenderCharges_AE);

            BIMAST.setServiceTaxOnSurrenderCharges_AF(prop.surrenderCharges, serviceTax);
            serviceTaxOnSurrenderCharges_AF = Double.parseDouble(BIMAST
                    .getServiceTaxOnSurrenderCharges_AF());
            // _serviceTaxOnSurrenderCharges_AF=serviceTaxOnSurrenderCharges_AF;
            // System.out.println("28.   ServiceTaxOnSurrenderCharges_AF : "+serviceTaxOnSurrenderCharges_AF);

            BIMAST.setSurrenderValueReductionYeild_AG();
            surrenderValue_AG = Double.parseDouble(BIMAST
                    .getSurrenderValueReductionYeild_AG());
            // _surrenderValue_AG=surrenderValue_AG;
            // System.out.println("29.   SurrenderValue_AG: "+surrenderValue_AG);

            BIMAST.setLast2YearAvgFundValue_AR(
                    BIMAST.arrFundValAfterFMCBeforeGA_AR, i);
            last2YearAvgFundValue_AR = Double.parseDouble(BIMAST
                    .getLast2YearAvgFundValue_AR());
            // _last2YearAvgFundValue_AR=last2YearAvgFundValue_AR;
            // System.out.println("40.   Last2YearAvgFundValue_AR: "+last2YearAvgFundValue_AR);

            BIMAST.setLoyaltyAddition_AS(policyTerm);
            loyaltyAddition_AS = Double.parseDouble(BIMAST
                    .getLoyaltyAddition_AS());
            // _loyaltyAddition_AS=loyaltyAddition_AS;
            // System.out.println("41.   LoyaltyAddition_AS: "+loyaltyAddition_AS);

            BIMAST.setFundValueAtEndReductionYeild_AT();
            fundValueAtEnd_AT = Double.parseDouble(BIMAST
                    .getFundValueAtEndReductionYeild_AT());
            // _fundValueAtEnd_AT=fundValueAtEnd_AT;
//			 System.out.println("42.   month_E: "+fundValueAtEnd_AT);

            BIMAST.setDeathBenefit_AX(sumAssured, policyTerm);
            deathBenefit_AX = Double.parseDouble(BIMAST.getDeathBenefit_AX());
            // _deathBenefit_AX=deathBenefit_AX;
            // System.out.println("46.   DeathBenefit_AX: "+deathBenefit_AX);

            BIMAST.setSurrenderChargesReductionYeild_AU(effectivePremium,
                    premFreqMode);
            surrenderCharges_AU = Double.parseDouble(BIMAST
                    .getSurrenderChargesReductionYeild_AU());
            // _surrenderCharges_AU=surrenderCharges_AU;
            // System.out.println("43.   SurrenderCharges_AU: "+surrenderCharges_AU);

            BIMAST.setServiceTaxOnSurrenderCharges_AV(prop.surrenderCharges, serviceTax);
            serviceTaxOnSurrenderCharges_AV = Double.parseDouble(BIMAST
                    .getServiceTaxOnSurrenderCharges_AV());
            // _serviceTaxOnSurrenderCharges_AV=serviceTaxOnSurrenderCharges_AV;
            // System.out.println("44.   ServiceTaxOnSurrenderCharges_AV: "+serviceTaxOnSurrenderCharges_AV);

            BIMAST.setSurrenderValueReductionYeild_AW();
            surrenderValue_AW = Double.parseDouble(BIMAST
                    .getSurrenderValueReductionYeild_AW());
            // _surrenderValue_AW=surrenderValue_AW;
            // System.out.println("45.   SurrenderValue_AW: "+surrenderValue_AW);

            monthNumber++;

            List_BB.add(commonForAllProd.roundUp_Level2(commonForAllProd
                    .getStringWithout_E(reductionYield_BB)));
            List_BC.add(commonForAllProd.roundUp_Level2(commonForAllProd
                    .getStringWithout_E(reductionYield_BC)));
            List_BG.add(commonForAllProd.roundUp_Level2(commonForAllProd
                    .getStringWithout_E(reductionYield_BG)));

            // System.out.println("List_BC " + month_BA+" : "+
            // commonForAllProd.roundUp_Level2(commonForAllProd.getStringWithout_E(_reductionYield_BC)));
        }

        double ans = BIMAST.irr(List_BB, 0.001);
        double ans1 = BIMAST.irr(List_BC, 0.01);
        double ans2 = BIMAST.irr(List_BG, 0.01);

        System.out.println("irr_BB : " + ans + " & irr_BC : " + ans1);

        BIMAST.setIRRAnnual_BB(ans);
        irrAnnual_BB = Double.parseDouble(BIMAST.getIRRAnnual_BB());
        // _irrAnnual_BB=irrAnnual_BB;
        // System.out.println("irrAnnual_BB "+irrAnnual_BB);

        BIMAST.setIRRAnnual_BC(ans1);
        irrAnnual_BC = Double.parseDouble(BIMAST.getIRRAnnual_BC());
        // _irrAnnual_BC=irrAnnual_BC;
        System.out.println("irrAnnual_BC " + irrAnnual_BC);

        BIMAST.setIRRAnnual_BG(ans2);
        irrAnnual_BG = Double.parseDouble(BIMAST.getIRRAnnual_BG());
        // _irrAnnual_BG=irrAnnual_BG;
        // System.out.println("irrAnnual_BG "+irrAnnual_BG);

        BIMAST.setReductionInYieldMaturityAt(prop.int2);
        reductionInYieldMaturityAt = Double.parseDouble(BIMAST
                .getReductionInYieldMaturityAt());
        // _reductionInYieldMaturityAt=reductionInYieldMaturityAt;
        System.out.println("reductionInYieldMaturityAt "
                + reductionInYieldMaturityAt);

        BIMAST.setReductionInYieldNumberOfYearsElapsedSinceInception(prop.int2);
        reductionInYieldNumberOfYearsElapsedSinceInception = Double
                .parseDouble(BIMAST
                        .getReductionInYieldNumberOfYearsElapsedSinceInception());
        // _reductionInYieldNumberOfYearsElapsedSinceInception=reductionInYieldNumberOfYearsElapsedSinceInception;
        // System.out.println("reductionInYieldNumberOfYearsElapsedSinceInception "+reductionInYieldNumberOfYearsElapsedSinceInception);

        BIMAST.setnetYieldAt4Percent();
        netYield4pa = Double.parseDouble(BIMAST.getnetYieldAt4Percent());

        BIMAST.setnetYieldAt8Percent();
        netYield8pa = Double.parseDouble(BIMAST.getnetYieldAt8Percent());

        // String
        // T=commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(_fundValueAfterFMCAndBeforeGA_AA));
        // String
        // U=commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(_loyaltyAddition_AC));
        // ////System.out.println("*T* -> "+T);
        // //System.out.println("*U* -> "+U);
        // String
        // AI=commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(_fundValueAfterFMCandBeforeGA_AQ));
        // String
        // AJ=commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(_loyaltyAddition_AS));
        // System.out.println("*AI* -> "+AI);
        // System.out.println("*AJ* -> "+AJ);
        // System.out.println("****************************** Final Output ****************************************");
        // System.out.println("Sum Assured -> "+(sumAssured));
        // System.out.println("Fund Value @6% [V] -> "+(Double.parseDouble(T)+Double.parseDouble(U)));
        // System.out.println("Fund Value @10% [AK] -> "+(Double.parseDouble(AI)+Double.parseDouble(AJ)));
        // System.out.println("***********************************************************************************");
        // Reset Values
        BIMAST.arrFundValAfterFMCBeforeGA = null;
        BIMAST.arrFundValAfterFMCBeforeGA_AR = null;
        // return new
        // String[]{(commonForAllProd.getStringWithout_E(sumAssured)),(commonForAllProd.getStringWithout_E(Double.parseDouble(T)+Double.parseDouble(U))),(commonForAllProd.getStringWithout_E(Double.parseDouble(AI)+Double.parseDouble(AJ))),(String.valueOf(smartScholarBean.getEffectivePremium()))};
        // return new
        // String[]{(commonForAllProd.getStringWithout_E(sumAssured)),commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(fundValueAtEnd_AD)),commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(fundValueAtEnd_AT)),(String.valueOf(smartScholarBean.getEffectivePremium()))};
        return new String[]{
                commonForAllProd.roundUp_Level2(commonForAllProd
                        .getStringWithout_E(reductionInYieldMaturityAt)),
                commonForAllProd
                        .roundUp_Level2(commonForAllProd
                        .getStringWithout_E(reductionInYieldNumberOfYearsElapsedSinceInception)),
                commonForAllProd.roundUp_Level2(commonForAllProd
                        .getStringWithout_E(netYield4pa)),
                commonForAllProd.roundUp_Level2(commonForAllProd
                        .getStringWithout_E(netYield8pa))};

    }

    // get PF used [used in used in Calculation]
    private int getPF() {
        if (spnrPremPayingMode.getSelectedItem().toString().equals("Single")
                || spnrPremPayingMode.getSelectedItem().toString()
                .equals("Yearly")) {
            return 1;
        } else if (spnrPremPayingMode.getSelectedItem().toString()
                .equals("Half Yearly")) {
            return 2;
        } else if (spnrPremPayingMode.getSelectedItem().toString()
                .equals("Quarterly")) {
            return 4;
        } else if (spnrPremPayingMode.getSelectedItem().toString()
                .equals("Monthly")) {
            return 12;
        } else {
            return 0;
        }
    }

    // get annual Premium [used in Calculation]and bean
    private double getAnnualPremium() {
        double temp = Math.max(getMinPremiumAmount(), getAnnualisedPremium()); // *Fixed
        // Input,*Sheet
        // Name
        // ->
        // Input,*Cell
        // ->
        // AA17
        if ((temp % prop.premMult) == 0) {
            return temp;
        } else {
            return (temp - (temp % prop.premMult));
        }
    }

    private int getPF(String premFreqMode) {

        if (premFreqMode.equals("Single") || premFreqMode.equals("Yearly")) {
            return 1;
        } else if (premFreqMode.equals("Half Yearly")) {
            return 2;
        } else if (premFreqMode.equals("Quarterly")) {
            return 4;
        } else if (premFreqMode.equals("Monthly")) {
            return 12;
        } else {
            return 0;
        }
    }

    //get annual Premium [used in  Calculation]
    private double getAnnualPremium(String premFreqMode, double premAmt, int premPayingTerm) {
        double temp = Math.max(getMinPremiumAmount(premFreqMode, premPayingTerm), getAnnualisedPremium(premFreqMode, premAmt));  //*Fixed Input,*Sheet Name -> Input,*Cell -> AA17
        if ((temp % prop.premMult) == 0) {
            return temp;
        } else {
            return (temp - (temp % prop.premMult));
        }
    }

    //[used in calculation] returns to getAnnualPremium()
    private double getAnnualisedPremium(String premFreqMode, double premAmt) {
        if (premFreqMode.equals("Single")) {
            return premAmt;
        } else {
            return premAmt * getPF(premFreqMode);
        }
    }

    //[used in calculation] returns to getAnnualPremium()
    private int getMinPremiumAmount(String premFreqMode, int premPayingTerm) {
        int minPremiumAmount = 0;
        if (premFreqMode.equals("Single")) {
            minPremiumAmount = prop.minPremForSingle;
        } else {
            if (premFreqMode.equals("Yearly")) {
                if (premPayingTerm < prop.PPT) {
                    minPremiumAmount = prop.minPremForYearly_PPTlessThan8;
                } else {
                    minPremiumAmount = prop.minPremForYearly_PPTeqOrgreaterThan8;
                }
            }
            if (premFreqMode.equals("Half Yearly")) {
                if (premPayingTerm < prop.PPT) {
                    minPremiumAmount = prop.minPremForHalfYearly_PPTlessThan8;
                } else {
                    minPremiumAmount = prop.minPremForHalfYearly_PPTeqOrgreaterThan8;
                }
            }
            if (premFreqMode.equals("Quarterly")) {
                if (premPayingTerm < prop.PPT) {
                    minPremiumAmount = prop.minPremForQuarterly_PPTlessThan8;
                } else {
                    minPremiumAmount = prop.minPremForQuarterly_PPTeqOrgreaterThan8;
                }
            }
            if (premFreqMode.equals("Monthly")) {
                if (premPayingTerm < prop.PPT) {
                    minPremiumAmount = prop.minPremForMonthly_PPTlessThan8;
                } else {
                    minPremiumAmount = prop.minPremForMonthly_PPTeqOrgreaterThan8;
                }
            }
        }
        return minPremiumAmount;
    }

    // [used in calculation] returns to getAnnualPremium()
    private double getAnnualisedPremium() {
        if (spnrPremPayingMode.getSelectedItem().toString().equals("Single")) {
            return Double.parseDouble(edt_premiumAmount.getText().toString());
        } else {
            return Double.parseDouble(edt_premiumAmount.getText().toString())
                    * getPF();
        }
    }

    // [used in calculation] returns to getAnnualPremium()
    private int getMinPremiumAmount() {
        int minPremiumAmount = 0;
        if (spnrPremPayingMode.getSelectedItem().toString().equals("Single")) {
            minPremiumAmount = prop.minPremForSingle;
        } else {
            if (spnrPremPayingMode.getSelectedItem().toString()
                    .equals("Yearly")) {
                if (Integer.parseInt(spnrPremPayingTerm.getSelectedItem()
                        .toString()) < prop.PPT) {
                    minPremiumAmount = prop.minPremForYearly_PPTlessThan8;
                } else {
                    minPremiumAmount = prop.minPremForYearly_PPTeqOrgreaterThan8;
                }
            }
            if (spnrPremPayingMode.getSelectedItem().toString()
                    .equals("Half Yearly")) {
                if (Integer.parseInt(spnrPremPayingTerm.getSelectedItem()
                        .toString()) < prop.PPT) {
                    minPremiumAmount = prop.minPremForHalfYearly_PPTlessThan8;
                } else {
                    minPremiumAmount = prop.minPremForHalfYearly_PPTeqOrgreaterThan8;
                }
            }
            if (spnrPremPayingMode.getSelectedItem().toString()
                    .equals("Quarterly")) {
                if (Integer.parseInt(spnrPremPayingTerm.getSelectedItem()
                        .toString()) < prop.PPT) {
                    minPremiumAmount = prop.minPremForQuarterly_PPTlessThan8;
                } else {
                    minPremiumAmount = prop.minPremForQuarterly_PPTeqOrgreaterThan8;
                }
            }
            if (spnrPremPayingMode.getSelectedItem().toString()
                    .equals("Monthly")) {
                if (Integer.parseInt(spnrPremPayingTerm.getSelectedItem()
                        .toString()) < prop.PPT) {
                    minPremiumAmount = prop.minPremForMonthly_PPTlessThan8;
                } else {
                    minPremiumAmount = prop.minPremForMonthly_PPTeqOrgreaterThan8;
                }
            }
        }
        return minPremiumAmount;
    }

    // effective premium
    private String getEffectivePremium() {
        if (!edt_premiumAmount.getText().toString().equals(""))
            return edt_premiumAmount.getText().toString();
        else
            return "0";
    }

    // returns min allowed SAMF [used in validation] returns to valSAMF()
   /* public double getMin_SAMF() {
		double a = 0;
		if (Integer.parseInt(spnr_bi_smart_scholar_proposer_age
				.getSelectedItem().toString()) < 45) {
			a = 10.0;
		} else {
			a = 7.0;
		}
		if (spnrPremPayingMode.getSelectedItem().toString().equals("Single")) {
			return 1.25;
		} else {
			return Math.max(a, (getMinSA() / getAnnualPremium()));
		}
	}

	// returns max allowed SAMF [used in validation] returns to valSAMF()
	private double getMax_SAMF() {
		if (spnrPremPayingMode.getSelectedItem().toString().equals("Single")) {
			if (Integer.parseInt(spnr_bi_smart_scholar_proposer_age
					.getSelectedItem().toString()) < 45) {
				return 5.0;
			} else {
				return 1.25;
			}
		} else {
			return Math.max(getMin_SAMF(), 20);
		}
	}
*/
    public double getMin_SAMF() {

        if (spnrPremPayingMode.getSelectedItem().toString().equals("Single")) {
            return 1.25;
        } else {
            return 10;
        }
    }

    // returns max allowed SAMF [used in validation] returns to valSAMF()
    public double getMax_SAMF() {
        if (spnrPremPayingMode.getSelectedItem().toString().equals("Single")) {
            return 1.25;
        } else {
            return 10;
        }
    }

    // returns min allowed sum assured, returns to getMin_SAMF()
    private double getMinSA() {
        double temp = 0;
        if (Integer.parseInt(spnr_bi_smart_scholar_proposer_age
                .getSelectedItem().toString()) < 45) {
            temp = 0.5;
        } else {
            temp = 0.25;
        }
        return getAnnualPremium()
                * Double.parseDouble(spnrPolicyTerm.getSelectedItem()
                .toString()) * temp;
    }

    /********************************** Calculations ends here **********************************************************/

    /* Basic Details Method */
    private boolean valBasicDetail() {

        if (edt_proposerdetail_basicdetail_contact_no.getText().toString()
                .equals("")) {
            commonMethods.dialogWarning(context, "Please Fill  Mobile No", true);
            edt_proposerdetail_basicdetail_contact_no.requestFocus();
            return false;
        } else if (edt_proposerdetail_basicdetail_contact_no.getText()
                .toString().length() != 10) {
            commonMethods.dialogWarning(context, "Please Fill 10 Digit Mobile No", true);
            edt_proposerdetail_basicdetail_contact_no.requestFocus();
            return false;
        }

		/*else if (emailId.equals("")) {
			commonMethods.dialogWarning(context,"Please Fill Email Id", true);
			edt_proposerdetail_basicdetail_Email_id.requestFocus();
			return false;

		}

		else if (ConfirmEmailId.equals("")) {

			commonMethods.dialogWarning(context,"Please Fill Confirm Email Id", true);
			edt_proposerdetail_basicdetail_ConfirmEmail_id.requestFocus();
			return false;
		} else if (!ConfirmEmailId.equals(emailId)) {
			commonMethods.dialogWarning(context,"Email Id Does Not Match", true);
			return false;
		}*/

        else if (!emailId.equals("")) {

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

        // else if(Integer.parseInt(proposer_age)!=age_ByButton)
        // {
        // dialog("Birth Does NOt Match With Bi Detail", true);
        // return false;
        // }
        // else if (validationFla1 == false || validationFlag2 == false
        // || validationFlag3 == false) {
        // dialog("Please Rectify The Error Field", true);
        // return false;
        // } else {
        // return true;
        // }
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
        } else if ((matcher.matches())) {
            validationFla1 = true;
        }
    }


    public boolean valDoYouBackdate() {
        if (!proposer_Backdating_WishToBackDate_Policy.equals("")) {
            return true;
        } else {
            showAlert.setMessage("Please Select Do you wish to Backdate ");
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // apply focusable method
                            setFocusable(rb_proposerdetail_personaldetail_backdating_yes);
                            rb_proposerdetail_personaldetail_backdating_yes
                                    .requestFocus();
                        }
                    });
            showAlert.show();

            return false;

        }
    }

    public boolean valBackdate() {
        if (proposer_Backdating_WishToBackDate_Policy.equals("y")) {

            if (proposer_Backdating_BackDate.equals("")) {
                showAlert.setMessage("Please Select Backdate ");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // apply focusable method
                                setFocusable(btn_proposerdetail_personaldetail_backdatingdate);
                                btn_proposerdetail_personaldetail_backdatingdate
                                        .requestFocus();

                            }
                        });
                showAlert.show();
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
                    showAlert.setMessage(error);
                    showAlert.setNeutralButton("OK",
                            new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // apply focusable method
                                    setFocusable(btn_proposerdetail_personaldetail_backdatingdate);
                                    btn_proposerdetail_personaldetail_backdatingdate
                                            .requestFocus();
                                }
                            });
                    showAlert.show();

                    return false;
                }

            }

        } catch (Exception e) {
            return false;
        }

        return true;

    }

    private boolean valLifeAssuredProposerDetail() {
        if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("Y")) {
            if (lifeAssured_Title.equals("")
                    || lifeAssured_First_Name.equals("")
                    || lifeAssured_Last_Name.equals("")) {

                showAlert.setMessage("Please Fill Name Detail For LifeAssured");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog,
                                                int which) {
                                if (lifeAssured_Title.equals("")) {
                                    // apply focusable method
                                    setFocusable(spnr_bi_smart_scholar_life_assured_title);
                                    spnr_bi_smart_scholar_life_assured_title
                                            .requestFocus();
                                } else if (lifeAssured_First_Name.equals("")) {
                                    setFocusable(edt_bi_smart_scholar_life_assured_first_name);
                                    edt_bi_smart_scholar_life_assured_first_name
                                            .requestFocus();
                                } else {
                                    setFocusable(edt_bi_smart_scholar_life_assured_last_name);
                                    edt_bi_smart_scholar_life_assured_last_name
                                            .requestFocus();
                                }
                            }
                        });
                showAlert.show();

                return false;
            } else if (lifeAssured_Title.equalsIgnoreCase("Mr.")
                    && gender.equalsIgnoreCase("Female")) {

                showAlert
                        .setMessage("Life Assured Title and Gender is not Valid");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // apply focusable method
                                setFocusable(spnr_bi_smart_scholar_life_assured_title);
                                spnr_bi_smart_scholar_life_assured_title
                                        .requestFocus();
                            }
                        });
                showAlert.show();

                return false;

            } else if (lifeAssured_Title.equalsIgnoreCase("Ms.")
                    && gender.equalsIgnoreCase("Male")) {

                showAlert
                        .setMessage("Life Assured Title and Gender is not Valid");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // apply focusable method
                                setFocusable(spnr_bi_smart_scholar_life_assured_title);
                                spnr_bi_smart_scholar_life_assured_title
                                        .requestFocus();
                            }
                        });
                showAlert.show();

                return false;

            } else if (lifeAssured_Title.equalsIgnoreCase("Mrs.")
                    && gender.equalsIgnoreCase("Male")) {

                showAlert
                        .setMessage("Life Assured Title and Gender is not Valid");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // apply focusable method
                                setFocusable(spnr_bi_smart_scholar_life_assured_title);
                                spnr_bi_smart_scholar_life_assured_title
                                        .requestFocus();
                            }
                        });
                showAlert.show();

                return false;
            } else if (proposer_Title.equals("")
                    || proposer_First_Name.equals("")
                    || proposer_Last_Name.equals("")) {

                showAlert.setMessage("Please Fill Name Detail For Proposer");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog,
                                                int which) {
                                if (proposer_Title.equals("")) {
                                    // apply focusable method
                                    setFocusable(spnr_bi_smart_scholar_proposer_title);
                                    spnr_bi_smart_scholar_proposer_title
                                            .requestFocus();
                                } else if (proposer_First_Name.equals("")) {
                                    setFocusable(edt_bi_smart_scholar_proposer_first_name);
                                    edt_bi_smart_scholar_proposer_first_name
                                            .requestFocus();
                                } else {
                                    setFocusable(edt_bi_smart_scholar_proposer_last_name);
                                    edt_bi_smart_scholar_proposer_last_name
                                            .requestFocus();
                                }
                            }
                        });
                showAlert.show();

                return false;
            } else if (proposer_Title.equalsIgnoreCase("Mr.")
                    && proposer_gender.equalsIgnoreCase("Female")) {

                showAlert
                        .setMessage("Proposer Title and Gender is not Valid");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // apply focusable method
                                setFocusable(spnr_bi_smart_scholar_proposer_title);
                                spnr_bi_smart_scholar_proposer_title
                                        .requestFocus();
                            }
                        });
                showAlert.show();

                return false;

            } else if (proposer_Title.equalsIgnoreCase("Ms.")
                    && proposer_gender.equalsIgnoreCase("Male")) {

                showAlert
                        .setMessage("Proposer Title and Gender is not Valid");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // apply focusable method
                                setFocusable(spnr_bi_smart_scholar_proposer_title);
                                spnr_bi_smart_scholar_proposer_title
                                        .requestFocus();
                            }
                        });
                showAlert.show();

                return false;

            } else if (proposer_Title.equalsIgnoreCase("Mrs.")
                    && proposer_gender.equalsIgnoreCase("Male")) {

                showAlert
                        .setMessage("Proposer Title and Gender is not Valid");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // apply focusable method
                                setFocusable(spnr_bi_smart_scholar_proposer_title);
                                spnr_bi_smart_scholar_proposer_title
                                        .requestFocus();
                            }
                        });
                showAlert.show();

                return false;
            } else {
                return true;
            }

        } else
            return true;
    }

    private boolean valDob() {

        if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("Y")) {

            if (lifeAssured_date_of_birth.equals("")
                    || lifeAssured_date_of_birth
                    .equalsIgnoreCase("select Date")) {
                showAlert
                        .setMessage("Please Select Valid Date Of Birth For LifeAssured");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // apply focusable method
                                setFocusable(btn_bi_smart_scholar_life_assured_date);
                                btn_bi_smart_scholar_life_assured_date
                                        .requestFocus();
                            }
                        });
                showAlert.show();

                return false;
            } else if (proposer_date_of_birth.equals("")
                    || proposer_date_of_birth.equalsIgnoreCase("select Date")) {
                showAlert
                        .setMessage("Please Select Valid Date Of Birth For Proposer");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // apply focusable method
                                setFocusable(btn_bi_smart_scholar_proposer_date);
                                btn_bi_smart_scholar_proposer_date
                                        .requestFocus();
                            }
                        });
                showAlert.show();

                return false;
            } else {
                return true;
            }
        } else
            return true;
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

        if (proposer_Backdating_WishToBackDate_Policy.equalsIgnoreCase("y")
                && !proposer_Backdating_BackDate.equals("")) {

            int Proposerage = calculateMyAge(mYear, Integer.parseInt(mont),
                    Integer.parseInt(day));

            String final_age = Integer.toString(Proposerage);
            spnr_Age.setSelection(getIndex(spnr_Age, final_age), false);
            // valMaturityAge();

        }

        String final_age = Integer.toString(age);

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
                    String lifeAssuredAge = final_age;
                    final_age = Integer.toString(age);
                    if (final_age.contains("-")) {
                        commonMethods.BICommonDialog(context, "Please fill Valid Bith Date For Child");
                    } else {
                        if (0 <= age && age <= 17) {

                            btn_bi_smart_scholar_life_assured_date.setText(date);

                            // spnr_Age.setSelection(getIndex(spnr_Age, final_age));

                            lifeAssured_date_of_birth = getDate1(date + "");
                            spnr_Age.setSelection(getIndex(spnr_Age, final_age),
                                    false);
                            updatePolicyTermLabel();
                            updatePremPayingTermLabel();

                            clearFocusable(btn_bi_smart_scholar_life_assured_date);
                            setFocusable(spnr_bi_smart_scholar_proposer_title);
                            spnr_bi_smart_scholar_proposer_title.requestFocus();
                        } else {
                            commonMethods.BICommonDialog(context, "The Age of Child should be between 0 & 17 yrs");
                            btn_bi_smart_scholar_life_assured_date
                                    .setText("Select Date");
                            lifeAssured_date_of_birth = "";

                            clearFocusable(btn_bi_smart_scholar_life_assured_date);
                            setFocusable(btn_bi_smart_scholar_life_assured_date);
                            btn_bi_smart_scholar_life_assured_date.requestFocus();
                        }
                    }
                    break;

                case 5:
                    String proposerAge = final_age;
                    final_age = Integer.toString(age);
                    if (final_age.contains("-")) {
                        commonMethods.BICommonDialog(context, "Please fill Valid Bith Date For Proposer");
                    } else {
                        if (18 <= age && age <= 57) {

                            btn_bi_smart_scholar_proposer_date.setText(date);

                            // spnr_Age.setSelection(getIndex(spnr_Age, final_age));

                            proposer_date_of_birth = getDate1(date + "");
                            spnr_bi_smart_scholar_proposer_age.setSelection(
                                    getIndex(spnr_bi_smart_scholar_proposer_age,
                                            final_age), false);

                            // valMaturityAge();
                            updatePolicyTermLabel();
                            updatePremPayingTermLabel();
                            updateSAMFlabel();

                            clearFocusable(btn_bi_smart_scholar_proposer_date);
                            setFocusable(edt_proposerdetail_basicdetail_contact_no);
                            edt_proposerdetail_basicdetail_contact_no
                                    .requestFocus();
                            /*
                             * setFocusable(spnrPremPayingMode);
                             * spnrPremPayingMode.requestFocus();
                             */
                        } else {
                            commonMethods.BICommonDialog(context, "The Age of Proposer should be between 18 & 57 yrs");
                            btn_bi_smart_scholar_proposer_date
                                    .setText("Select Date");
                            proposer_date_of_birth = "";

                            clearFocusable(btn_bi_smart_scholar_proposer_date);
                            setFocusable(btn_bi_smart_scholar_proposer_date);
                            btn_bi_smart_scholar_proposer_date.requestFocus();
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
    }

    private int calculateMyAge(int year, int month, int day) {
        Calendar nowCal = new GregorianCalendar(year, month, day);

        String[] ProposerDob = getDate(lifeAssured_date_of_birth).split("/");
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
        initialiseDateParameter(lifeAssured_date_of_birth, 0);
        DIALOG_ID = 4;
        showDialog(DATE_DIALOG_ID);

    }

    public void onClickProposerDob(View v) {
        initialiseDateParameter(proposer_date_of_birth, 35);
        DIALOG_ID = 5;
        showDialog(DATE_DIALOG_ID);

    }

    public void onClickBackDating(View v) {
        // setDefaultDate();
        DIALOG_ID = 6;
        if (lifeAssured_date_of_birth != null
                && !lifeAssured_date_of_birth.equals("")) {
            showDialog(DATE_DIALOG_ID);
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

    /**
     * Used To Change date From mm-dd-yyyy to dd-mm-yyyy
     */
    private String getDate(String OldDate) {
        String NewDate = "";
        try {
            DateFormat userDateFormat = new SimpleDateFormat("MM-dd-yyyy");
            DateFormat dateFormatNeeded = new SimpleDateFormat("dd/MM/yyyy");
            Date date = userDateFormat.parse(OldDate);

            NewDate = dateFormatNeeded.format(date);

        } catch (Exception e) {
            // TODO: handle exception
            Log.e("Integrated Service", "Error in Getting Date");
        }

        return NewDate;
    }

    /**
     * Used To Change date From dd-mm-yyyy to mm-dd-yyyy
     */
    private String getDate1(String OldDate) {
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

    // method to set a focusable a element
    public void setFocusable(View v) {
        // TODO Auto-generated method stub
        v.setFocusable(true);
        v.setFocusableInTouchMode(true);
    }

    // method to set a clearing a element
    public void clearFocusable(View v) {
        // TODO Auto-generated method stub
        v.setFocusable(false);
        v.setFocusableInTouchMode(false);
        // v.clearFocus();
    }

    private void windowmessagesgin() {

        d = new Dialog(BI_SmartScholarActivity.this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.BLACK));

        d.setContentView(R.layout.window_message_signature);
        final Button btn_save = d.findViewById(R.id.save);
        final Button btn_cancel = d.findViewById(R.id.cancel);

        Button btn_takeSign = d.findViewById(R.id.takesignature);

        btn_takeSign.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                btn_save.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(BI_SmartScholarActivity.this,
                        CaptureSignature.class);
                startActivityForResult(intent, SIGNATURE_ACTIVITY);

            }
        });

        btn_cancel.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                d.dismiss();
            }
        });
        d.show();

    }

    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        // TODO Auto-generated method stub
        if (v.getId() == edt_bi_smart_scholar_life_assured_first_name.getId()) {
            setFocusable(edt_bi_smart_scholar_life_assured_middle_name);
            edt_bi_smart_scholar_life_assured_middle_name.requestFocus();
        } else if (v.getId() == edt_bi_smart_scholar_life_assured_middle_name
                .getId()) {
            setFocusable(edt_bi_smart_scholar_life_assured_last_name);
            edt_bi_smart_scholar_life_assured_last_name.requestFocus();
        } else if (v.getId() == edt_bi_smart_scholar_life_assured_last_name
                .getId()) {
            clearFocusable(btn_bi_smart_scholar_life_assured_date);
            setFocusable(btn_bi_smart_scholar_life_assured_date);
            btn_bi_smart_scholar_life_assured_date.requestFocus();
        } else if (v.getId() == edt_bi_smart_scholar_proposer_first_name
                .getId()) {
            setFocusable(edt_bi_smart_scholar_proposer_middle_name);
            edt_bi_smart_scholar_proposer_middle_name.requestFocus();
        } else if (v.getId() == edt_bi_smart_scholar_proposer_middle_name
                .getId()) {
            setFocusable(edt_bi_smart_scholar_proposer_last_name);
            edt_bi_smart_scholar_proposer_last_name.requestFocus();
        } else if (v.getId() == edt_bi_smart_scholar_proposer_last_name.getId()) {
            setFocusable(btn_bi_smart_scholar_proposer_date);
            btn_bi_smart_scholar_proposer_date.requestFocus();
        } else if (v.getId() == edt_proposerdetail_basicdetail_contact_no.getId()) {
            // clearFocusable(edt_premiumAmount);
            setFocusable(edt_proposerdetail_basicdetail_Email_id);
            edt_proposerdetail_basicdetail_Email_id.requestFocus();
        } else if (v.getId() == edt_proposerdetail_basicdetail_Email_id.getId()) {
            // clearFocusable(edt_premiumAmount);
            setFocusable(edt_proposerdetail_basicdetail_ConfirmEmail_id);
            edt_proposerdetail_basicdetail_ConfirmEmail_id.requestFocus();
        } else if (v.getId() == edt_proposerdetail_basicdetail_ConfirmEmail_id
                .getId()) {
            // clearFocusable(edt_premiumAmount);
            clearFocusable(spnrPolicyTerm);
            setFocusable(spnrPolicyTerm);
            spnrPolicyTerm.requestFocus();
        } else if (v.getId() == edt_premiumAmount.getId()) {

            setFocusable(edt_SAMF);
            edt_SAMF.requestFocus();

        } else if (v.getId() == edt_SAMF.getId()) {

            setFocusable(edt_NoOfYearsElapsed);
            edt_NoOfYearsElapsed.requestFocus();

        } else if (v.getId() == edt_NoOfYearsElapsed.getId()) {

            setFocusable(edt_percent_EquityFund);
            edt_percent_EquityFund.requestFocus();

        } else if (v.getId() == edt_percent_EquityFund.getId()) {

            setFocusable(edt_percent_EquityOptimiserFund);
            edt_percent_EquityOptimiserFund.requestFocus();

        } else if (v.getId() == edt_percent_EquityOptimiserFund.getId()) {

            setFocusable(edt_percent_GrowthFund);
            edt_percent_GrowthFund.requestFocus();

        } else if (v.getId() == edt_percent_GrowthFund.getId()) {

            setFocusable(edt_percent_BalancedFund);
            edt_percent_BalancedFund.requestFocus();

        } else if (v.getId() == edt_percent_BalancedFund.getId()) {

            setFocusable(edt_percent_BondFund);
            edt_percent_BondFund.requestFocus();

        } else if (v.getId() == edt_percent_BondFund.getId()) {

            setFocusable(edt_percent_MoneyMarketFund);
            edt_percent_MoneyMarketFund.requestFocus();

        } else if (v.getId() == edt_percent_MoneyMarketFund.getId()) {

            setFocusable(edt_percent_Top300Fund);
            edt_percent_Top300Fund.requestFocus();

        } else if (v.getId() == edt_percent_Top300Fund.getId()) {

            setFocusable(edt_bondOptimiserFund2);
            edt_bondOptimiserFund2.requestFocus();
        } else if (v.getId() == edt_bondOptimiserFund2.getId()) {

            setFocusable(edt_pureFund);
            edt_pureFund.requestFocus();
        } else if (v.getId() == edt_pureFund.getId()) {
//            UtilityMain.hideKeyboard(BI_SmartScholarActivity.this);
        }
        return true;
    }

    /********************************** Validations starts here **********************************************************/
    // Policy Term Validation
    private boolean valPolicyTerm() {
        int minPolicyTerm = Math.max(
                (18 - Integer.parseInt(spnr_Age.getSelectedItem().toString())),
                8);
        int maxPolicyTerm = Math.max(Math.min((25 - Integer.parseInt(spnr_Age
                .getSelectedItem().toString())), (65 - Integer
                .parseInt(spnr_bi_smart_scholar_proposer_age.getSelectedItem()
                        .toString()))), minPolicyTerm);

        if (minPolicyTerm > Integer.parseInt(spnrPolicyTerm.getSelectedItem()
                .toString())
                || maxPolicyTerm < Integer.parseInt(spnrPolicyTerm
                .getSelectedItem().toString())) {

            showAlert.setMessage("Enter Policy Term between " + minPolicyTerm
                    + " and " + maxPolicyTerm);
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            showAlert.show();
            return false;
        }

        return true;
    }

    // Premium Paying Term Validation
    private boolean valPremiumPayingTerm() {
        if (!spnrPremPayingMode.getSelectedItem().toString().equals("Single")) {
            int minPolicyTerm = Math.max((18 - Integer.parseInt(spnr_Age
                    .getSelectedItem().toString())), 8);
            int maxPolicyTerm = Math.max(Math.min((25 - Integer
                            .parseInt(spnr_Age.getSelectedItem().toString())),
                    (65 - Integer.parseInt(spnr_bi_smart_scholar_proposer_age
                            .getSelectedItem().toString()))), minPolicyTerm);
            int minPremiumPayingTerm = Math.min(5, minPolicyTerm);
            int maxPremiumPayingTerm = Math.min((Math.min(25, maxPolicyTerm)),
                    Integer.parseInt(spnrPolicyTerm.getSelectedItem()
                            .toString()));

            if (spnrPremPayingTerm.getSelectedItem().toString().equals("")
                    || minPremiumPayingTerm > Integer
                    .parseInt(spnrPremPayingTerm.getSelectedItem()
                            .toString())
                    || maxPremiumPayingTerm < Integer
                    .parseInt(spnrPremPayingTerm.getSelectedItem()
                            .toString())) {
                showAlert
                        .setMessage("Enter Premium Paying Term between "
                                + minPremiumPayingTerm + " and "
                                + maxPremiumPayingTerm);
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog,
                                                int which) {
                            }
                        });
                showAlert.show();
                return false;

            }
        }
        return true;
    }

    // Loan Amount Validation
    private boolean valedt_premiumAmount() {
        String error = "";
        if (edt_premiumAmount.getText().toString().equals("")) {
            error = "Please enter Premium Amount in Rs. ";
        } else if (!(Double.parseDouble(edt_premiumAmount.getText().toString()) % 100 == 0)) {
            error = "Premium Amount should be multiple of 100";
        } else {
            int minPremiumAmount = getMinPremiumAmount();
            if (Integer.parseInt(edt_premiumAmount.getText().toString()) < minPremiumAmount) {
                error = "Premium Amount should not be less than Rs. "
                        + currencyFormat.format(minPremiumAmount);
            }

        }
        if (!error.equals("")) {
            showAlert.setMessage(error);
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            showAlert.show();
            return false;
        } else
            return true;

    }

    // Loan Amount Validation
    private boolean valSAMF() {
        if (edt_SAMF.getText().toString().equals("")
                || (getMin_SAMF() > Double.parseDouble(edt_SAMF.getText()
                .toString()))
                || (getMax_SAMF() < Double.parseDouble(edt_SAMF.getText()
                .toString()))) {
            showAlert
                    .setMessage("Sum Assured Multiple Factor (SAMF) should be in the range of "
                            + getMin_SAMF() + " to " + getMax_SAMF());
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            showAlert.show();
            return false;
        }

        return true;
    }

    // Addition of invested fund must be 100%
    public boolean valTotalAllocation() {
        double equityFund, equityOptimiserFund, growthFund, balancedFund, bondFund, moneyMarketFund,
                indexFund, top300Fund, peManagedFund, guaranteeFund, bondOptimiserFund2, pureFund;

        if (!edt_percent_EquityFund.getText().toString().equals(""))
            equityFund = Double.parseDouble(edt_percent_EquityFund.getText()
                    .toString());
        else
            equityFund = 0;

        if (!edt_percent_EquityOptimiserFund.getText().toString().equals(""))
            equityOptimiserFund = Double
                    .parseDouble(edt_percent_EquityOptimiserFund.getText()
                            .toString());
        else
            equityOptimiserFund = 0;

        if (!edt_percent_GrowthFund.getText().toString().equals(""))
            growthFund = Double.parseDouble(edt_percent_GrowthFund.getText()
                    .toString());
        else
            growthFund = 0;

        if (!edt_percent_BalancedFund.getText().toString().equals(""))
            balancedFund = Double.parseDouble(edt_percent_BalancedFund
                    .getText().toString());
        else
            balancedFund = 0;

        if (!edt_percent_BondFund.getText().toString().equals(""))
            bondFund = Double.parseDouble(edt_percent_BondFund.getText()
                    .toString());
        else
            bondFund = 0;

        if (!edt_percent_MoneyMarketFund.getText().toString().equals(""))
            moneyMarketFund = Double.parseDouble(edt_percent_MoneyMarketFund
                    .getText().toString());
        else
            moneyMarketFund = 0;

        if (!edt_percent_Top300Fund.getText().toString().equals(""))
            top300Fund = Double.parseDouble(edt_percent_Top300Fund.getText()
                    .toString());
        else
            top300Fund = 0;


        if (!edt_bondOptimiserFund2.getText().toString().equals(""))
            bondOptimiserFund2 = Double.parseDouble(edt_bondOptimiserFund2.getText()
                    .toString());
        else
            bondOptimiserFund2 = 0;

        if (!edt_pureFund.getText().toString().equals(""))
            pureFund = Double.parseDouble(edt_pureFund.getText()
                    .toString());
        else
            pureFund = 0;

        // Removed by vrushali chaudhari
        // if(!edt_percent_IndexFund.getText().toString().equals(""))
        // indexFund =
        // Double.parseDouble(edt_percent_IndexFund.getText().toString());
        // else
        // indexFund = 0;

        // if(!edt_percent_PEmanagedFund.getText().toString().equals(""))
        // peManagedFund =
        // Double.parseDouble(edt_percent_PEmanagedFund.getText().toString());
        // else
        // peManagedFund=0;
        // if((equityFund+equityOptimiserFund+growthFund+bondFund+balancedFund+indexFund+moneyMarketFund+top300Fund+peManagedFund)
        // !=100)

        if ((equityFund + equityOptimiserFund + growthFund + bondFund
                + balancedFund + moneyMarketFund + top300Fund + bondOptimiserFund2 + pureFund) != 100) {
            showAlert
                    .setMessage("Total sum of % to be invested for all fund should be equal to 100%");
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            showAlert.show();
            return false;

        }
        return true;
    }

    // Years Elapsed Since Inception Validation
    private boolean valYearsElapsedSinceInception() {
        if (edt_NoOfYearsElapsed.getText().toString().equals("")
                || Double
                .parseDouble(edt_NoOfYearsElapsed.getText().toString()) < 5
                || Double
                .parseDouble(edt_NoOfYearsElapsed.getText().toString()) > Double
                .parseDouble(spnrPolicyTerm.getSelectedItem()
                        .toString())) {

            showAlert
                    .setMessage("Enter No. of Years Elapsed Since Inception between 5 to "
                            + spnrPolicyTerm.getSelectedItem().toString()
                            + " Years");
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            showAlert.show();
            return false;
        }

        return true;

    }

    /****************************** Validations ends here **********************************************************/

    /********************************** Help starts here **********************************************************/

    // Policy Term Help
    private void updatePolicyTermLabel() {
        try {
            int minPolicyTerm = Math.max((18 - Integer.parseInt(spnr_Age
                    .getSelectedItem().toString())), 8);
            int maxPolicyTerm = Math.max(Math.min((25 - Integer
                            .parseInt(spnr_Age.getSelectedItem().toString())),
                    (65 - Integer.parseInt(spnr_bi_smart_scholar_proposer_age
                            .getSelectedItem().toString()))), minPolicyTerm);

            tvHelpPolicyTerm.setText("(" + minPolicyTerm + " to "
                    + maxPolicyTerm + ")");
        } catch (Exception ignored) {
        }
    }

    // premium paying term help
    private void updatePremPayingTermLabel() {
        try {
            if (!spnrPremPayingMode.getSelectedItem().toString()
                    .equals("Single")) {
                int minPolicyTerm = Math.max((18 - Integer.parseInt(spnr_Age
                        .getSelectedItem().toString())), 8);
                int maxPolicyTerm = Math.max(Math.min((25 - Integer
                                .parseInt(spnr_Age.getSelectedItem().toString())),
                        (65 - Integer
                                .parseInt(spnr_bi_smart_scholar_proposer_age
                                        .getSelectedItem().toString()))),
                        minPolicyTerm);
                int minPremiumPayingTerm = Math.min(5, minPolicyTerm);
                int maxPremiumPayingTerm = Math.min((Math
                        .min(25, maxPolicyTerm)), Integer
                        .parseInt(spnrPolicyTerm.getSelectedItem().toString()));

                tvHelpPremiumTerm.setText("(" + minPremiumPayingTerm + " to "
                        + maxPremiumPayingTerm + " years)");

            }
        } catch (Exception ignored) {
        }
    }

    // SAMF help
    private void updateSAMFlabel() {
        try {
            double minSAMF = getMin_SAMF();
            double maxSAMF = getMax_SAMF();

            tvHelpSAMF.setText("(" + minSAMF + " to " + maxSAMF + ")");
        } catch (Exception e) {
            tvHelpSAMF.setText("");
        }
    }

    // Premium Amount help
    private void updateedt_premiumAmountLabel() {
        try {
            tvHelpPremAmount.setText("(Min. Rs. "
                    + currencyFormat.format(getMinPremiumAmount()) + ")");
        } catch (Exception ignored) {
        }
    }

    // help for no of years elapsed since Inception
    private void updatenoOfYearsElapsedSinceInception() {
        try {
            tvHelpNoOfYearsElapsed.setText("(5 to "
                    + spnrPolicyTerm.getSelectedItem().toString() + " years)");
        } catch (Exception ignored) {
        }
    }

    private void createPdf() {
        try {

            ParseXML prsObj = new ParseXML();

            String maturityAgeofProposer = prsObj.parseXmlTag(output,
                    "maturityAgeofProposer");
            String maturityAgeofChild = prsObj.parseXmlTag(output,
                    "maturityAgeofChild");

            String annPrem = prsObj.parseXmlTag(output, "annPrem");
            String redInYieldMat = prsObj.parseXmlTag(output, "redInYieldMat");
            String redInYieldNoYr = prsObj
                    .parseXmlTag(output, "redInYieldNoYr");
            String sumAssured = prsObj.parseXmlTag(output, "sumAssured");
            String fundVal4 = prsObj.parseXmlTag(output, "fundVal4");
            String fundVal8 = prsObj.parseXmlTag(output, "fundVal8");
            String netYield4Pr = prsObj.parseXmlTag(output, "netYield4Pa");
            String netYield8Pr = prsObj.parseXmlTag(output, "netYield8Pa");
            String accBenSumAssured = prsObj.parseXmlTag(output,
                    "accBenSumAssured");
            int policyterm;

            policyterm = Integer.parseInt(policy_term);
            System.out.println(policyterm + "  " + maturityAgeofProposer + "  "
                    + maturityAgeofProposer + " " + annPrem + " " + sumAssured
                    + " " + netYield8Pr + " " + premFreq);
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
            Font small_bold2 = new Font(Font.FontFamily.TIMES_ROMAN, 4,
                    Font.BOLD);
            Font normal_italic = new Font(Font.FontFamily.TIMES_ROMAN, 12,
                    Font.ITALIC);
            Font normal_bolditalic = new Font(Font.FontFamily.TIMES_ROMAN, 12,
                    Font.BOLDITALIC);
            Font normal_underline = new Font(Font.FontFamily.TIMES_ROMAN, 12,
                    Font.UNDERLINE);
            Font normal_bold = new Font(Font.FontFamily.TIMES_ROMAN, 5,
                    Font.BOLD);
            Font normal = new Font(Font.FontFamily.TIMES_ROMAN, 5, Font.NORMAL);
            Font normal1_bold = new Font(Font.FontFamily.TIMES_ROMAN, 6,
                    Font.BOLD);
            Font normal1 = new Font(Font.FontFamily.TIMES_ROMAN, 6, Font.NORMAL);

            // File mypath = new File(folder, PropserNumber +
            // "Proposalno_p02.pdf");
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

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                    R.drawable.sbi_life_logo);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
            byte[] bitMapData = stream.toByteArray();
            Image image = Image.getInstance(bitMapData);
            image.setAlignment(Image.LEFT);
            image.scaleToFit(80, 50);

            document.open();

            PdfPTable table = new PdfPTable(3);
            table.setWidths(new float[]{2.5f, 8.5f, 2f});
            table.setWidthPercentage(100);
            table.getDefaultCell().setPadding(15);

            PdfPCell cell;

            cell = new PdfPCell(image);
            cell.setRowspan(4);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("SBI Life Insurance Co. Ltd",
                    headerBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("\n", headerBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setRowspan(4);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(
                    new Phrase(
                            "Registered & Corporate Office: 'Natraj', M.V.Road and Western Express Highway Junction, Andheri (East), Mumbai - 400069 ",
                            headerBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);

            table.addCell(cell);

            cell = new PdfPCell(new Phrase("IRDAI Registration No. 111 | Website: www.sbilife.co.in | Email: info@sbilife.co.in | CIN: L99999MH2000PLC129113",
                    headerBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);

            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Toll Free: 1800 267 9090 (Between 9.00 am & 9.00 pm)",
                    headerBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);

            table.addCell(cell);

            cell = new PdfPCell(new Phrase("\n", headerBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setRowspan(8);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(
                    new Phrase(
                            "Customised Benefit Illustration (CBI)",
                            headerBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("\n", headerBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setRowspan(8);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(
                    new Phrase(
                            "SBI Life - Smart Scholar  (UIN : 111L073V03)",
                            headerBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);

            table.addCell(cell);

            cell = new PdfPCell(
                    new Phrase(
                            "An Individual, Unit-linked, Non-Participating, Life Insurance Product",
                            headerBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    "IN THIS POLICY, THE INVESTMENT RISK IN INVESTMENT PORTFOLIO IS BORNE BY THE POLICYHOLDER.", headerBold));
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


            cell = new PdfPCell(new Phrase("\n", normal));
            cell.setColspan(3);
            cell.setBorder(Rectangle.BOTTOM);
            cell.setBorderWidth(1.2f);
            table.addCell(cell);

            LineSeparator ls = new LineSeparator();

            PdfPTable table_proposer_name = new PdfPTable(4);
            // float[] columnWidths_table_proposer_name = { 2f, 2f, 1f, 2f };
            // table_proposer_name.setWidths(columnWidths_table_proposer_name);
            table_proposer_name.setWidthPercentage(100);

            PdfPCell ProposalNumber_cell_1 = new PdfPCell(new Paragraph(
                    "Quotation Number", small_normal));
            PdfPCell ProposalNumber_cell_2 = new PdfPCell(new Paragraph(
                    QuatationNumber, normal1_bold));
            ProposalNumber_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell NameofProposal_cell_3 = new PdfPCell(new Paragraph(
                    "Channel / Intermediary :", small_normal));
            PdfPCell NameofProposal_cell_4 = new PdfPCell(new Paragraph(
                    userType, normal1_bold));
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

            // PdfPTable table = new PdfPTable(3);
            // table.setWidths(new float[] { 3f, 7f, 4f });
            // table.setWidthPercentage(100);
            // table.getDefaultCell().setPadding(15);
            //
            // PdfPCell cell;
            // cell = new PdfPCell(image);
            // cell.setRowspan(3);
            // cell.setBorder(Rectangle.NO_BORDER);
            // table.addCell(cell);
            //
            // cell = new PdfPCell(new Phrase("\n", headerBold));
            // cell.setColspan(2);
            // cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            // cell.setBorder(Rectangle.NO_BORDER);
            // table.addCell(cell);
            //
            // cell = new PdfPCell(
            // new Phrase(
            // "Proposer Name     : __________________________________________________________",
            // normal1_bold));
            // cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            // cell.setBorder(Rectangle.NO_BORDER);
            // cell.setColspan(2);
            // table.addCell(cell);
            //
            // cell = new PdfPCell(
            // new Phrase(
            // "Proposer Number : __________________________________________________________",
            // normal1_bold));
            // cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            // cell.setBorder(Rectangle.NO_BORDER);
            // cell.setColspan(2);
            // table.addCell(cell);
            //
            // cell = new PdfPCell(new Phrase("\n", normal));
            // cell.setColspan(3);
            // cell.setBorder(Rectangle.BOTTOM);
            // cell.setBorderWidth(1.2f);
            // table.addCell(cell);
            //
            // LineSeparator ls = new LineSeparator();
            Paragraph para3 = new Paragraph(
                    "Insurance Regulatory and Development authority of India (IRDAI) requires all life insurance companies operating in India to provide official illustrations to their customers. The illustrations are based on the investment rates of return set by the Insurance Regulatory and Development Authority of India (Unit Linked Insurance Products) Regulations, 2019 and is not intended to reflect the actual investment returns achieved or which may be achieved in future by SBI Life Insurance Company Limited.",
                    normal1);
            para3.setAlignment(Element.ALIGN_LEFT);
            Paragraph para4 = new Paragraph(
                    "The main objective of the illustration is that the client is able to appreciate the features of the product and the flow of benefits in different circumstances with some level of quantification. For further information on the product, its benefits and applicable charges please refer to the sales brochure and/or policy document. Further information will also be available on request.",
                    normal1);
            para4.setAlignment(Element.ALIGN_LEFT | Element.ALIGN_JUSTIFIED);
            Paragraph para5 = new Paragraph(
                    "Some benefits are guaranteed and some benefits are variable with returns based on the future fund performance of SBI Life Insurance Company Limited. If your policy offers guaranteed returns then the same will be clearly marked as “guaranteed” in the illustration table. If your policy offers variable returns then the illustration will show two different rates of assumed future investment returns. These assumed rates of return are not guaranteed and they are not the upper or lower limits of what you might get back, as the value of your policy is dependent on a number of factors including future fund investment performance.",
                    normal1);
            para5.setAlignment(Element.ALIGN_LEFT | Element.ALIGN_JUSTIFIED);
            Paragraph para6 = new Paragraph(
                    "Some benefits are guaranteed and some benefits are variable with returns based on the future performance of SBI Life Insurance Company Limited. If your policy offers guaranteed returns then the same will be clearly marked as 'guaranteed' in the illustration table on this page. If your policy offers variable returns then the illustration on this page will show two different rates of assumed future investment returns. These assumed rates of return are not guaranteed and they are not the upper or lower limits of what you might get back, as the value of your policy is dependent on a number of factors including future fund investment performance.",
                    normal1);
            para6.setAlignment(Element.ALIGN_LEFT | Element.ALIGN_JUSTIFIED);
            Paragraph new_line = new Paragraph("\n");

            // inputTable here -1

            PdfPTable input_table = new PdfPTable(2);
            input_table.setWidths(new float[]{3f, 2f});
            input_table.setWidthPercentage(80f);
            input_table.setHorizontalAlignment(Element.ALIGN_LEFT);

            cell = new PdfPCell(new Phrase("Proposer, Life Assured and Plan Details ", normal1_bold));
            cell.setColspan(2);
            //cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            input_table.addCell(cell);

            // 1st row
            cell = new PdfPCell(new Phrase("Name of the Life Assured", normal1));
            //cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            input_table.addCell(cell);

            cell = new PdfPCell(new Phrase(name_of_proposer, normal1));
            //cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            input_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Age of the Life Assured ", normal1));
            // cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            input_table.addCell(cell);

            cell = new PdfPCell(new Phrase(proposerageAtEntry + " Years",
                    normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            //cell.setBorder(Rectangle.NO_BORDER);
            input_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Name of the child", normal1));
            // cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            input_table.addCell(cell);

            cell = new PdfPCell(new Phrase(name_of_life_assured, normal1));
            //cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            input_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Age of the child ", normal1));
            //cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            input_table.addCell(cell);

            cell = new PdfPCell(new Phrase(age_entry + " Years", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            // cell.setBorder(Rectangle.NO_BORDER);
            input_table.addCell(cell);


            cell = new PdfPCell(new Phrase("Policy Term ", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            //cell.setBorder(Rectangle.NO_BORDER);
            input_table.addCell(cell);

            cell = new PdfPCell(new Phrase(policy_term + " Years", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            //cell.setBorder(Rectangle.NO_BORDER);
            input_table.addCell(cell);


            cell = new PdfPCell(new Phrase("Premium Payment Term ", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            //cell.setBorder(Rectangle.NO_BORDER);
            input_table.addCell(cell);

            premium_paying_term = prsObj.parseXmlTag(input, "premPayingTerm");

            if (premFreq.equalsIgnoreCase("Single")) {
                cell = new PdfPCell(new Phrase(" One time at the inception of the policy",
                        normal1));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                //cell.setBorder(Rectangle.NO_BORDER);
                input_table.addCell(cell);
            } else {
                cell = new PdfPCell(new Phrase(premium_paying_term + " Years",
                        normal1));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                //cell.setBorder(Rectangle.NO_BORDER);
                input_table.addCell(cell);
            }


            // 2 row
            cell = new PdfPCell(new Phrase("Maturity Age of the Proposer : ",
                    normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            // input_table.addCell(cell);

            cell = new PdfPCell(new Phrase(maturityAgeofProposer + " Years",
                    normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            //input_table.addCell(cell);

            // 2 row
            cell = new PdfPCell(new Phrase("Maturity Age of the Child : ",
                    normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            //input_table.addCell(cell);

            cell = new PdfPCell(new Phrase(maturityAgeofChild + " Years",
                    normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            //input_table.addCell(cell);

            String lifeAssuredGender = "";
            if (proposer_Title.equalsIgnoreCase("Mr.")) {
                lifeAssuredGender = "Male";
            } else if (proposer_Title.equalsIgnoreCase("Ms.")) {
                lifeAssuredGender = "Female";
            } else if (proposer_Title.equalsIgnoreCase("Mrs.")) {
                lifeAssuredGender = "Female";
            }
            // 3 row
            cell = new PdfPCell(new Phrase("Life Assured Gender : ", normal1));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            //input_table.addCell(cell);

            cell = new PdfPCell(new Phrase(lifeAssuredGender, normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            // input_table.addCell(cell);

            // 4 row


            // 5 row

            premiumAmount = obj.getRound(obj.getStringWithout_E(Double
                    .valueOf((premiumAmount))));
            // 5 row
            if (premPayingMode.equals("Single"))

                /*cell = new PdfPCell(new Phrase("Single Premium (SP)* : ",
                        normal1));*/
                cell = new PdfPCell(new Phrase("Amount of Installment Premium ",
                        normal1));
            else
                /*cell = new PdfPCell(new Phrase("Annualised Premium (LPPT)*  ",
                        normal1));*/
                cell = new PdfPCell(new Phrase("Amount of Installment Premium ",
                        normal1));
            //cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            input_table.addCell(cell);
            if (premPayingMode.equals("Single"))
                cell = new PdfPCell(new Phrase("Rs. "
                        + currencyFormat.format(Double.parseDouble(premiumAmount)),
                        normal1));
            else
                cell = new PdfPCell(new Phrase("Rs. "
                        + currencyFormat.format(Double.parseDouble(premiumAmount)),
                        normal1));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            // cell.setBorder(Rectangle.NO_BORDER);
            input_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Mode / Frequency of Premium Payment ", normal1));
            //cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            input_table.addCell(cell);

            premFreq = prsObj.parseXmlTag(input, "premFreq");
            premPayingMode = premFreq;

            if (premPayingMode.equalsIgnoreCase("Single")) {
                cell = new PdfPCell(new Phrase("One time at the inception of the policy", normal1));
                // cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                input_table.addCell(cell);


            } else {
                cell = new PdfPCell(new Phrase(premPayingMode, normal1));
                // cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                input_table.addCell(cell);
                //tv_premPaymentfrequency.setText(premFreq);
            }

            // 6 row
            cell = new PdfPCell(new Phrase("Sum Assured ", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            // cell.setBorder(Rectangle.NO_BORDER);
            input_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Rs. "
                    + currencyFormat.format(Double.parseDouble(sumAssured)),
                    normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            //cell.setBorder(Rectangle.NO_BORDER);
            input_table.addCell(cell);


            // 6 row
            cell = new PdfPCell(new Phrase("Rate of Applicable Taxes ", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            // cell.setBorder(Rectangle.NO_BORDER);
            input_table.addCell(cell);

            if (cb_kerladisc.isChecked()) {
                str_rate_of_applicable_tax = "19 %";
            } else {
                str_rate_of_applicable_tax = "18 %";
            }

            cell = new PdfPCell(new Phrase(str_rate_of_applicable_tax,
                    normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            //cell.setBorder(Rectangle.NO_BORDER);
            input_table.addCell(cell);

            //
            // //8 row
            // cell = new PdfPCell(new Phrase("Mode : ",normal1));
            // cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            // cell.setBorder(Rectangle.NO_BORDER);
            // input_table.addCell(cell);
            //
            // cell = new PdfPCell(new Phrase(mode+"",normal1));
            // cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            // cell.setBorder(Rectangle.NO_BORDER);
            // input_table.addCell(cell);

            // fund table here -3

            PdfPTable fund_table = new PdfPTable(4);
            fund_table.setWidths(new float[]{3f, 1.9f, 1f, 1.9f});
            fund_table.setWidthPercentage(80f);
            fund_table.setHorizontalAlignment(Element.ALIGN_RIGHT);

            // 1st row
            cell = new PdfPCell(new Phrase(
                    "Fund Name (SFIN Name) : ", normal1_bold));
            cell.setColspan(3);

            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            //fund_table.addCell(cell);

            // 2 row
            cell = new PdfPCell(new Phrase("Fund Name (SFIN Name)", normal1));

            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);
            cell = new PdfPCell(new Phrase("% Allocation", normal1));

            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);
            cell = new PdfPCell(new Phrase("FMC", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Risk Level", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);

            // 3 row
            cell = new PdfPCell(new Phrase("Equity Fund (SFIN:ULIF001100105EQUITY-FND111)", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            fund_table.addCell(cell);
            cell = new PdfPCell(new Phrase(
                    (percentage_equity_fund.equals("") ? "0"
                            : percentage_equity_fund) + " % ", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);
            cell = new PdfPCell(new Phrase("1.35%", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);

            cell = new PdfPCell(new Phrase("High", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);

            // 4 row
            cell = new PdfPCell(new Phrase("Equity Optimiser Fund (SFIN: ULIF010210108EQTYOPTFND111)", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            fund_table.addCell(cell);
            cell = new PdfPCell(new Phrase(
                    (percentage_equity_optimiser_fund.equals("") ? "0"
                            : percentage_equity_optimiser_fund) + " % ",
                    normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);
            cell = new PdfPCell(new Phrase("1.35%", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);
            cell = new PdfPCell(new Phrase("High", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);
            // 5 row
            cell = new PdfPCell(new Phrase("Growth Fund (SFIN:ULIF003241105GROWTH-FND111)", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            fund_table.addCell(cell);
            cell = new PdfPCell(new Phrase(
                    (percentage_growth_fund.equals("") ? "0"
                            : percentage_growth_fund) + " % ", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);
            cell = new PdfPCell(new Phrase("1.35%", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);
            cell = new PdfPCell(new Phrase("Medium to High", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);
            // 6 row
            cell = new PdfPCell(new Phrase("Balanced Fund (SFIN:ULIF004051205BALANCDFND111)", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            fund_table.addCell(cell);
            cell = new PdfPCell(new Phrase(
                    (percentage_balanced_fund.equals("") ? "0"
                            : percentage_balanced_fund) + " % ", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);
            cell = new PdfPCell(new Phrase("1.25%", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);
            cell = new PdfPCell(new Phrase("Medium", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);
            // 7 row
            cell = new PdfPCell(new Phrase("Bond Fund (SFIN:ULIF002100105BONDULPFND111)", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            fund_table.addCell(cell);
            cell = new PdfPCell(new Phrase(
                    (percentage_bond_fund.equals("") ? "0"
                            : percentage_bond_fund) + " % ", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);
            cell = new PdfPCell(new Phrase("1.00%", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);
            cell = new PdfPCell(new Phrase("Low to Medium", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);
            // 8 row
            cell = new PdfPCell(new Phrase("Money Market Fund (SFIN:ULIF005010206MONYMKTFND111)", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            fund_table.addCell(cell);
            cell = new PdfPCell(new Phrase(
                    (percentage_money_market_fund.equals("") ? "0"
                            : percentage_money_market_fund) + " % ", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);
            cell = new PdfPCell(new Phrase("0.25%", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);
            cell = new PdfPCell(new Phrase("Low", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);
            // 9 row
            cell = new PdfPCell(new Phrase("Top 300 Fund (SFIN: ULIF016070110TOP300-FND111)", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            fund_table.addCell(cell);
            cell = new PdfPCell(new Phrase(
                    (percentage_top300_fund.equals("") ? "0"
                            : percentage_top300_fund) + " % ", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);
            cell = new PdfPCell(new Phrase("1.35%", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);
            cell = new PdfPCell(new Phrase("High", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);

            if (STR_PLAN_AB.equals("A")) {
                cell = new PdfPCell(new Phrase("Bond Optimiser Fund II (SFIN : ULIF037160919BONDOPFND2111)", normal1));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                fund_table.addCell(cell);
            } else if (STR_PLAN_AB.equals("B")) {
                cell = new PdfPCell(new Phrase("Bond Optimiser Fund (SFIN : ULIF032290618BONDOPTFND111)", normal1));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                fund_table.addCell(cell);
            }

           /* cell = new PdfPCell(new Phrase("Bond Optimiser Fund II (SFIN : ULIF037160919BONDOPFND2111)", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            fund_table.addCell(cell);*/


            cell = new PdfPCell(new Phrase(
                    (percentage_bondOptimiserFund2.equals("") ? "0"
                            : percentage_bondOptimiserFund2) + " % ", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);
            cell = new PdfPCell(new Phrase("1.15%", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);
            cell = new PdfPCell(new Phrase("Low to Medium", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);
            cell = new PdfPCell(new Phrase("Pure Fund (SFIN : ULIF030290915PUREULPFND111)", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            fund_table.addCell(cell);
            cell = new PdfPCell(new Phrase(
                    (percentage_pureFund.equals("") ? "0"
                            : percentage_pureFund) + " % ", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);
            cell = new PdfPCell(new Phrase("1.35%", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);

            cell = new PdfPCell(new Phrase("High", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);

            // first year premium Table here -2

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


            PdfPTable FyPrem_table = new PdfPTable(2);
            FyPrem_table.setWidths(new float[]{3.2f, 2.0f});
            FyPrem_table.setWidthPercentage(80f);
            FyPrem_table.setHorizontalAlignment(Element.ALIGN_CENTER);

            // 1st row
            cell = new PdfPCell(new Phrase("Accident Benefit Sum Assurred : ",
                    normal1));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            //FyPrem_table.addCell(cell);
            cell = new PdfPCell(new Phrase("Rs. "
                    + currencyFormat.format(Double
                    .parseDouble(accBenSumAssured)), normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            //   FyPrem_table.addCell(cell);

            // 2nd row
            if (premPayingMode.equals("Single"))
                cell = new PdfPCell(new Phrase("Single Premium : ", normal1));
            else
                cell = new PdfPCell(new Phrase(premPayingMode + " Premium : ",
                        normal1));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            //FyPrem_table.addCell(cell);
            cell = new PdfPCell(new Phrase("Rs. "
                    + currencyFormat.format(Double.parseDouble(premiumAmount)),
                    normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            //  FyPrem_table.addCell(cell);
            // reduction in yeild for elapsed year since inception Table here
            // -4a

            PdfPTable reductionInYeild_table = new PdfPTable(2);
            reductionInYeild_table.setWidths(new float[]{3.5f, 2f});
            reductionInYeild_table.setWidthPercentage(80f);
            reductionInYeild_table.setHorizontalAlignment(Element.ALIGN_RIGHT);

            // 1st row
            cell = new PdfPCell(new Phrase(
                    "No of years elapsed since inception", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            //reductionInYeild_table.addCell(cell);
            cell = new PdfPCell(new Phrase("Reduction in Yeild @ 8%", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            // reductionInYeild_table.addCell(cell);

            // 2 row
            cell = new PdfPCell(new Phrase(no_of_year_elapsed + " Years",
                    normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            // reductionInYeild_table.addCell(cell);
            cell = new PdfPCell(new Phrase(redInYieldNoYr + "%", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            // reductionInYeild_table.addCell(cell);

            // reduction in yeild for maturity Table here -4b

            PdfPTable reductionInYeild_maturity_table = new PdfPTable(2);
            reductionInYeild_maturity_table.setWidths(new float[]{3.5f, 2f});
            reductionInYeild_maturity_table.setWidthPercentage(70f);
            reductionInYeild_maturity_table
                    .setHorizontalAlignment(Element.ALIGN_RIGHT);

            // 1st row
            cell = new PdfPCell(new Phrase("Maturity at", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            //reductionInYeild_maturity_table.addCell(cell);
            cell = new PdfPCell(new Phrase("Reduction in Yeild @ 8%", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            // reductionInYeild_maturity_table.addCell(cell);

            // 2 row
            cell = new PdfPCell(new Phrase(policyterm + " Years", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            //  reductionInYeild_maturity_table.addCell(cell);
            cell = new PdfPCell(new Phrase(redInYieldMat + "%", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            //  reductionInYeild_maturity_table.addCell(cell);

            // main table of 4 tables
            PdfPTable main_table = new PdfPTable(4);
            main_table.setWidths(new float[]{4f, 2.8f, 5f, 3.4f});
            main_table.setWidthPercentage(100);
            main_table.getDefaultCell().setPadding(20f);

            cell = new PdfPCell(input_table);
            cell.setRowspan(4);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setVerticalAlignment(Element.ALIGN_TOP);
            main_table.addCell(cell);

            cell = new PdfPCell(FyPrem_table);
            cell.setRowspan(4);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.NO_BORDER);

            main_table.addCell(cell);
            cell = new PdfPCell(fund_table);
            cell.setRowspan(4);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setVerticalAlignment(Element.ALIGN_TOP);
            main_table.addCell(cell);

            cell = new PdfPCell(new Phrase("\n"));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            main_table.addCell(cell);
            cell = new PdfPCell(reductionInYeild_table);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            main_table.addCell(cell);
            cell = new PdfPCell(reductionInYeild_maturity_table);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            main_table.addCell(cell);
            cell = new PdfPCell(new Phrase("\n"));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            main_table.addCell(cell);

            // Table here


            Paragraph base_para = new Paragraph(" *It is a base premium ",
                    normal1);
            base_para.setAlignment(Element.ALIGN_LEFT);

            Paragraph note = new Paragraph("Notes :", normal_bold);
            note.setAlignment(Element.ALIGN_LEFT);
            // notes Table here

            PdfPTable table2 = new PdfPTable(2);
            table2.setWidths(new float[]{0.3f, 9f});
            table2.setWidthPercentage(100);
            table2.setHorizontalAlignment(Element.ALIGN_LEFT);

            // 1st note
            cell = new PdfPCell(new Phrase("1)", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            " Refer the sales literature for explanation of terms used in this illustration.",
                            normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT
                    | Element.ALIGN_JUSTIFIED);
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);

            // 2 note
            cell = new PdfPCell(new Phrase("2)", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "Please read this benefit illustration in conjunction with Sales Brochure and the Policy Document to understand all Terms, Conditions & Exclusions carefully.",
                            normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);

            // 3 note
            cell = new PdfPCell(new Phrase("3)", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "Kindly note that above is only an illustration and does not in any way create any rights and/or obligations. The actual experience on the contract may be different from what is illustrated. The non-guaranteed low and high rate mentioned above relate to assumed investment returns at different rates and may vary depending upon market conditions. For more details on risk factors, terms and conditions please read sales brochure carefully.",
                            normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);
            // 4 note
            cell = new PdfPCell(new Phrase("4)", normal1));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            " The unit values may go up as well as down and past performance is no indication of future performance on the part of SBI Life Insurance Co. Ltd. We would request you to appreciate the associated risk under this plan vis-à-vis the likely future returns before taking your investment decision.",
                            normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);
            // 5 note
            cell = new PdfPCell(new Phrase("5)", normal1));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "It is assumed that the policy is in force throughout the term.",
                            normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);
            // 6 note
            cell = new PdfPCell(new Phrase("6)", normal1));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "Fund management charge is based on the specific  investment strategy / fund option(s) chosen",
                            normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);

            // 7 note
            cell = new PdfPCell(new Phrase("7)", normal1));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "Surrender Value equals the Fund Value at the end of the year minus Discontinuance Charges. Surrender value is available on or after 5th policy anniversary.",
                            normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);

            // 8 note
            cell = new PdfPCell(new Phrase("8)", normal1));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "Acceptance of proposal is subject to Underwriting decision. Mortality charges are for a healthy person. ",
                            normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT
                    | Element.ALIGN_JUSTIFIED);
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);

            // 9 note
            cell = new PdfPCell(new Phrase("9)", normal1));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "Applicable Taxes (including surcharge/cess etc), at the rate notified by the Central Government/ State Government / Union Territories of India from time to time and as per the provisions of the prevalent tax laws will be payable on premium/ or any other charges as per the product features.",
                            normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT
                    | Element.ALIGN_JUSTIFIED);
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);

            // 10 note
            cell = new PdfPCell(new Phrase("10)", normal1));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    "This policy provides guaranteed death benefit of Rs.  "
                            + currencyFormat.format(Double
                            .parseDouble(sumAssured)) + ".", normal1));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table2.addCell(cell);

            // 10 note
            cell = new PdfPCell(new Phrase("11)", normal1));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "Net Yield have been calculated after applying all the charges (except GST, mortality charges).",
                            normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);

            // 11 note
            cell = new PdfPCell(new Phrase("11)", normal1));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            //table2.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "This illustration has been prepared in compliance with the circular IRDAI (Linked Insurance Products) Regulations, 2013.",
                            normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            //table2.addCell(cell);

            // 12 note
            cell = new PdfPCell(new Phrase("12)", normal1));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            //table2.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "In case of accidental death or accidental total  and permanent disability, whichever  is earlier,  this policy provides additional guaranteed benefit of Rs. "
                                    + currencyFormat.format(Math.min(
                                    Double.parseDouble(sumAssured),
                                    5000000))
                                    + ". In case of accidental death Rs. "
                                    + currencyFormat.format(Math.min(
                                    Double.parseDouble(sumAssured),
                                    5000000))
                                    + " will be paid as lump sum where as in case of accidental total and permenant disability Rs. "
                                    + currencyFormat.format(Math.min(
                                    Double.parseDouble(sumAssured),
                                    5000000))
                                    + " would be paid in 10  equal annual installments  of Rs. "
                                    + currencyFormat.format(Math.min(
                                    Double.parseDouble(sumAssured),
                                    5000000) * 0.1)
                                    + ". \nThe benefit under Accident Benefit is subject to a Cap of Rs. 50 Lacs under all policies with SBI Life  for Accidental Death and Accidental TPD benefit for a single life.",
                            normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            //table2.addCell(cell);

            // 13 note
            cell = new PdfPCell(new Phrase("13)", normal1));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            //table2.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            " Col (24) gives the commission payable to the agent/ broker in respect of the base policy .  This amount is included in total charges mentioned in col (7) or col (16).",
                            normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            //table2.addCell(cell);

            // 14 note
            cell = new PdfPCell(new Phrase("14)", normal1));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            // table2.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            " I have understood and reviewed all charges including the level of mortality charge under this policy. I understand that as a result of these charges, I may get as maturity value, an amount which may be less than the total premiums paid during the term of the policy. This could be because of the high mortality charge deduction having an adverse impact on the value of my investments. I also understand that extra mortality charge has been levied on my policy.",
                            normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            // table2.addCell(cell);

            PdfPTable table3 = new PdfPTable(3);
            table3.setWidths(new float[]{0.5f, 3.2f, 9f});
            table3.setWidthPercentage(80);
            table3.setHorizontalAlignment(Element.ALIGN_LEFT);

            cell = new PdfPCell(new Phrase("Definition of Various Charges:",
                    normal1_bold));
            cell.setColspan(3);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);
            cell = new PdfPCell(new Phrase("", normal1));
            cell.setRowspan(7);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.addCell(cell);

            // 1st charges row
            cell = new PdfPCell(
                    new Phrase("1)Policy Administration Charges :", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "a charge of a fixed sum which is applied at the beginning of each policy month by cancelling units for equivalent amount, deducted for maintaning the policy.",
                            normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.addCell(cell);

            // 2 charges row
            cell = new PdfPCell(new Phrase("2)Premium Allocation Charge :",
                    normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "is the percentage of premium that would not be utilised to purchase units.",
                            normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.addCell(cell);

            // 3 charges row
            cell = new PdfPCell(new Phrase("3)Mortality Charges :", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "are the charges recovered for providing life insurance cover, deducted applied at the beginning of each policy month by cancelling units for equivalent amount.",
                            normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.addCell(cell);

            // 4 charges row
            cell = new PdfPCell(new Phrase("4)Fund Management Charge (FMC) :", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "is the deduction made from the fund at a stated percentage before the computation of the NAV of the fund.",
                            normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.addCell(cell);

            // 5 charges row
            cell = new PdfPCell(new Phrase("5)Premium Payor Waiver Benefit charges :", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "a level charge of a fixed sum which is applied at the beginning of each policy month by cancelling units for equivalent amount.",
                            normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.addCell(cell);

            // 6 charges row
            cell = new PdfPCell(new Phrase(
                    "6)Accident Benefit charges :", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "a charge of a fixed sum based on the sum assured chosen, which is applied at the beginning of each policy month by cancelling units for equivalent amount.",
                            normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.addCell(cell);

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
            document.add(table_proposer_name);
            // document.add(ls);
            document.add(para3);
            document.add(para4);
            document.add(para5);
            // document.add(para6);
            document.add(new_line);
            document.add(ls);
            document.add(new_line);

            document.add(main_table);
            document.add(new_line);
            document.add(BI_Pdftablereadunderstand);
            document.add(BI_Pdftable101);
            document.add(BI_Pdftable102);
            document.add(BI_Pdftable103);
            document.add(BI_Pdftable104);
            document.add(new_line);
            //document.add(table1);
            //  document.add(base_para);


            PdfPTable BI_PdftableOutputHeader = new PdfPTable(4);

            BI_PdftableOutputHeader.setWidthPercentage(100);
            float[] columnWidths = {2f, 6f, 6f, 1f};
            BI_PdftableOutputHeader.setWidths(columnWidths);
            PdfPCell partA = new PdfPCell(
                    new Paragraph("PART A", small_bold2));
            partA.setColspan(4);
            PdfPCell BI_PdftableBI_PdftableOutputHeader_cell1 = new PdfPCell(
                    new Paragraph("Amount in Rupees", small_bold2));
            PdfPCell BI_PdftableBI_PdftableOutputHeader_cell2 = new PdfPCell(
                    new Paragraph("At 4% p.a. Gross Investment return", small_bold2));
            PdfPCell BI_PdftableBI_PdftableOutputHeader_cell4 = new PdfPCell(
                    new Paragraph("At 8% p.a. Gross Investment return", small_bold2));
            PdfPCell BI_PdftableBI_PdftableOutputHeader_cell41 = new PdfPCell(
                    new Paragraph("", small_bold2));

            partA.setHorizontalAlignment(Element.ALIGN_LEFT);
            partA.setPadding(5);

            BI_PdftableBI_PdftableOutputHeader_cell1
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableBI_PdftableOutputHeader_cell1.setPadding(5);

            BI_PdftableBI_PdftableOutputHeader_cell2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableBI_PdftableOutputHeader_cell2.setPadding(5);


            BI_PdftableBI_PdftableOutputHeader_cell4
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableBI_PdftableOutputHeader_cell4.setPadding(5);
            BI_PdftableBI_PdftableOutputHeader_cell41
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableBI_PdftableOutputHeader_cell41.setPadding(5);

            BI_PdftableOutputHeader.addCell(partA);
            BI_PdftableOutputHeader
                    .addCell(BI_PdftableBI_PdftableOutputHeader_cell1);
            BI_PdftableOutputHeader
                    .addCell(BI_PdftableBI_PdftableOutputHeader_cell2);

            BI_PdftableOutputHeader
                    .addCell(BI_PdftableBI_PdftableOutputHeader_cell4);
            BI_PdftableOutputHeader
                    .addCell(BI_PdftableBI_PdftableOutputHeader_cell41);

            document.add(BI_PdftableOutputHeader);


            PdfPTable BI_Pdftableoutput = new PdfPTable(15);
            BI_Pdftableoutput.setWidthPercentage(100);

            PdfPCell BI_Pdftable_output_cell1 = new PdfPCell(new Paragraph(
                    "Policy year", small_bold2));
            PdfPCell BI_Pdftable_output_cell2 = new PdfPCell(new Paragraph(
                    "Annualized Premium", small_bold2));

            PdfPCell BI_Pdftable_output_cell3 = new PdfPCell(new Paragraph(
                    "Mortality charges", small_bold2));

            PdfPCell BI_Pdftable_output_cell4 = new PdfPCell(new Paragraph(
                    "Other Charges*",
                    small_bold2));
            PdfPCell BI_Pdftable_output_cell5 = new PdfPCell(new Paragraph(
                    "Applicable Taxes", small_bold2));

            PdfPCell BI_Pdftable_output_cell6 = new PdfPCell(new Paragraph(
                    "Fund at end of the year", small_bold2));

            PdfPCell BI_Pdftable_output_cell7 = new PdfPCell(new Paragraph(
                    "Surrender Value", small_bold2));
            PdfPCell BI_Pdftable_output_cell8 = new PdfPCell(new Paragraph(
                    "Death Benefit", small_bold2));
            PdfPCell BI_Pdftable_output_cell81 = new PdfPCell(new Paragraph(
                    "Mortality charges", small_bold2));

            PdfPCell BI_Pdftable_output_cell9 = new PdfPCell(new Paragraph(
                    "Other Charges*", small_bold2));

            PdfPCell BI_Pdftable_output_cell10 = new PdfPCell(new Paragraph(
                    "Applicable Taxes", small_bold2));
            PdfPCell BI_Pdftable_output_cell11 = new PdfPCell(new Paragraph(
                    "Fund at end of the year", small_bold2));


            PdfPCell BI_Pdftable_output_cell13 = new PdfPCell(new Paragraph(
                    "Surrender Value", small_bold2));
            PdfPCell BI_Pdftable_output_cell14 = new PdfPCell(new Paragraph(
                    "Death Benefit", small_bold2));

            PdfPCell BI_Pdftable_output_cell24 = new PdfPCell(new Paragraph(
                    "Commission payable to intermediary (Rs)", small_bold2));

            BI_Pdftableoutput.addCell(BI_Pdftable_output_cell1);
            BI_Pdftableoutput.addCell(BI_Pdftable_output_cell2);
            BI_Pdftableoutput.addCell(BI_Pdftable_output_cell3);

            BI_Pdftableoutput.addCell(BI_Pdftable_output_cell4);
            BI_Pdftableoutput.addCell(BI_Pdftable_output_cell5);
            BI_Pdftableoutput.addCell(BI_Pdftable_output_cell6);

            BI_Pdftableoutput.addCell(BI_Pdftable_output_cell7);
            BI_Pdftableoutput.addCell(BI_Pdftable_output_cell8);
            BI_Pdftableoutput.addCell(BI_Pdftable_output_cell81);
            BI_Pdftableoutput.addCell(BI_Pdftable_output_cell9);

            BI_Pdftableoutput.addCell(BI_Pdftable_output_cell10);
            BI_Pdftableoutput.addCell(BI_Pdftable_output_cell11);

            BI_Pdftableoutput.addCell(BI_Pdftable_output_cell13);
            BI_Pdftableoutput.addCell(BI_Pdftable_output_cell14);

            BI_Pdftableoutput.addCell(BI_Pdftable_output_cell24);
            document.add(BI_Pdftableoutput);

            PdfPTable BI_Pdftableoutput_no = new PdfPTable(15);
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
            document.add(BI_Pdftableoutput_no);

            for (int i = 0; i < Integer.parseInt(policy_term); i++) {

                PdfPTable BI_Pdftableoutput_row1 = new PdfPTable(15);
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

                PdfPCell BI_Pdftable_output_row1_cell5 = new PdfPCell(
                        new Paragraph(list_data.get(i)
                                .getTotal_charge1(), small_bold2));

                PdfPCell BI_Pdftable_output_row1_cell6 = new PdfPCell(
                        new Paragraph(list_data.get(i).getService_tax_on_mortality_charge1(),
                                small_bold2));

                PdfPCell BI_Pdftable_output_row1_cell7 = new PdfPCell(
                        new Paragraph(list_data.get(i).getFund_value_at_end1(),
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

                PdfPCell BI_Pdftable_output_row1_cell13 = new PdfPCell(
                        new Paragraph(list_data.get(i).getSurrender_value2(),
                                small_bold2));

                PdfPCell BI_Pdftable_output_row1_cell14 = new PdfPCell(
                        new Paragraph(list_data.get(i).getDeath_benefit2(),
                                small_bold2));


                PdfPCell BI_Pdftable_output_row1_cell24 = new PdfPCell(
                        new Paragraph(list_data.get(i).getCommission(),
                                small_bold2));

                BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell1);
                BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell2);
                BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell3);

                BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell5);
                BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell6);
                BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell7);
                BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell8);
                BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell81);
                BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell31);

                BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell51);
                BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell61);


                BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell12);

                BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell13);
                BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell14);

                BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell24);
                document.add(BI_Pdftableoutput_row1);

            }

            PdfPTable see_partB_Table = new PdfPTable(1);

            PdfPCell see_partB = new PdfPCell(
                    new Paragraph("* See Part B for details", small_bold2));
//            see_partB.setColspan(3);
            PdfPCell decl = new PdfPCell(
                    new Paragraph("IN THIS POLICY, THE INVESTMENT RISK IS BORNE BY THE POLICYHOLDER AND THE ABOVE INTEREST RATES ARE ONLY FOR ILLUSTRATIVE PURPOSE", small_bold2));
//            decl.setColspan(3);

            see_partB_Table
                    .addCell(see_partB);
            see_partB_Table
                    .addCell(decl);
            document.add((see_partB_Table));

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
                                    + "    have received the information with respect to the above and have understood the above statement before entering into a contract.",
                            small_bold));

            BI_Pdftable26_cell1.setPadding(5);

            BI_Pdftable26.addCell(BI_Pdftable26_cell1);
            document.add(BI_Pdftable26);
            document.add(BI_Pdftable26_cell1);
            document.add(new_line);

            PdfPTable BI_PdftablePolicyHolder = null;
            if (!bankUserType.equalsIgnoreCase("Y")) {
                BI_PdftablePolicyHolder = new PdfPTable(1);
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
                    new Paragraph(commonMethods.getAgentDeclaration(context), small_bold));

            agentDeclaration.setPadding(5);
            agentDeclarationTable.addCell(agentDeclaration);
            document.add(agentDeclarationTable);
            document.add(new_line);
            PdfPTable BI_PdftableMarketing = null, BI_PdftableMarketing_signature = null;
            if (!bankUserType.equalsIgnoreCase("Y")) {
                BI_PdftableMarketing = new PdfPTable(1);
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

                BI_PdftableMarketing_signature = new PdfPTable(3);

                BI_PdftableMarketing_signature.setWidthPercentage(100);

                PdfPCell BI_PdftableMarketing_signature_1 = new PdfPCell(
                        new Paragraph("Place :" + place2, small_normal));
                PdfPCell BI_PdftableMarketing_signature_2 = new PdfPCell(
                        new Paragraph("Date  :" + CurrentDate, small_normal));

                PdfPCell BI_PdftableMarketing_signature_3 = new PdfPCell();

                byte[] fbyt_agent = Base64.decode(agent_sign, 0);
                Bitmap Agentbitmap = BitmapFactory.decodeByteArray(fbyt_agent,
                        0, fbyt_agent.length);

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

            PdfPTable BI_PdftableOutputHeader8 = new PdfPTable(3);
            BI_PdftableOutputHeader8.setWidthPercentage(100);
            float[] columnWidths8 = {2f, 6f, 7f};
            BI_PdftableOutputHeader8.setWidths(columnWidths8);
          /*  PdfPCell see_partB = new PdfPCell(
                    new Paragraph("* See Part B for details", small_bold2));
            see_partB.setColspan(3);
            PdfPCell decl = new PdfPCell(
                    new Paragraph("IN THIS POLICY, THE INVESTMENT RISK IS BORNE BY THE POLICYHOLDER AND THE ABOVE INTEREST RATES ARE ONLY FOR ILLUSTRATIVE PURPOSE", small_bold2));
            decl.setColspan(3);*/
            PdfPTable BI_PdftableOutputHeader411 = new PdfPTable(1);
            BI_PdftableOutputHeader411.setWidthPercentage(100);

            PdfPCell PartB = new PdfPCell(
                    new Paragraph("PART B", small_bold2));
            BI_PdftableOutputHeader411.addCell(PartB);

            PdfPTable BI_PdftableOutputHeader41 = new PdfPTable(3);
            BI_PdftableOutputHeader41.setWidthPercentage(100);
            float[] columnWidths841 = {2f, 6f, 7f};
            BI_PdftableOutputHeader41.setWidths(columnWidths841);

            PdfPCell BI_PdftableBI_PdftableOutputHeader_cell18 = new PdfPCell(
                    new Paragraph("Amount in  Rs.", small_bold2));
            PdfPCell BI_PdftableBI_PdftableOutputHeader_cell28 = new PdfPCell(
                    new Paragraph("Gross Yield 8% pa",
                            small_bold2));


            PdfPCell BI_PdftableBI_PdftableOutputHeader_cell48 = new PdfPCell(
                    new Paragraph("Net Yield " + netYield8 + "%", small_bold2));

            see_partB.setHorizontalAlignment(Element.ALIGN_LEFT);
            PartB.setHorizontalAlignment(Element.ALIGN_LEFT);
            decl.setHorizontalAlignment(Element.ALIGN_CENTER);

            see_partB.setPadding(5);
            PartB.setPadding(5);
            decl.setPadding(5);
            BI_PdftableBI_PdftableOutputHeader_cell18
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableBI_PdftableOutputHeader_cell18.setPadding(5);

            BI_PdftableBI_PdftableOutputHeader_cell28
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableBI_PdftableOutputHeader_cell28.setPadding(5);


            BI_PdftableBI_PdftableOutputHeader_cell48
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableBI_PdftableOutputHeader_cell48.setPadding(5);


            BI_PdftableOutputHeader41.addCell(BI_PdftableBI_PdftableOutputHeader_cell18);
            BI_PdftableOutputHeader41.addCell(BI_PdftableBI_PdftableOutputHeader_cell28);
            BI_PdftableOutputHeader41.addCell(BI_PdftableBI_PdftableOutputHeader_cell48);


        /*    BI_PdftableOutputHeader8
                    .addCell(see_partB);
            BI_PdftableOutputHeader8
                    .addCell(decl);*/

            BI_PdftableOutputHeader8
                    .addCell(BI_PdftableBI_PdftableOutputHeader_cell28);

            BI_PdftableOutputHeader8
                    .addCell(BI_PdftableBI_PdftableOutputHeader_cell48);

            document.add(BI_PdftableOutputHeader8);

            PdfPTable BI_Pdftableoutput8 = new PdfPTable(17);
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

            PdfPCell BI_Pdftable_output_cell78112 = new PdfPCell(new Paragraph(
                    "PPWB charges", small_bold2));

            PdfPCell BI_Pdftable_output_cell7822 = new PdfPCell(new Paragraph(
                    "Add. ADB & ATPD Charges", small_bold2));

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
            BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell78112);
            BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell7822);
            BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell781);
            BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell88);
            BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell881);
            BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell98);

            BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell108);
            BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell118);
            BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell128);

            BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell138);

            document.add(BI_PdftableOutputHeader411);
            document.add(BI_PdftableOutputHeader41);
            document.add(BI_Pdftableoutput8);

            /*PdfPTable BI_Pdftableoutput_no8 = new PdfPTable(17);
            BI_Pdftableoutput_no8.setWidthPercentage(100);*/

            /*PdfPCell BI_Pdftable_output_no_cell18 = new PdfPCell(new Paragraph(
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
            PdfPCell BI_Pdftable_output_no_cell2486 = new PdfPCell(new Paragraph(
                    "16", small_bold2));
            PdfPCell BI_Pdftable_output_no_cell2487 = new PdfPCell(new Paragraph(
                    "17", small_bold2));

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
            BI_Pdftableoutput_no8.addCell(BI_Pdftable_output_no_cell2486);
            BI_Pdftableoutput_no8.addCell(BI_Pdftable_output_no_cell2487);*/
            //document.add(BI_Pdftableoutput_no8);

            for (int i = 0; i < Integer.parseInt(policy_term); i++) {

                PdfPTable BI_Pdftableoutput_row18 = new PdfPTable(17);
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
                PdfPCell BI_Pdftable_output_row1_cell5811 = new PdfPCell(
                        new Paragraph(list_data1.get(i)
                                .getPPWB_charges(), small_bold2));
                PdfPCell BI_Pdftable_output_row1_cell5822 = new PdfPCell(
                        new Paragraph(list_data1.get(i)
                                .getAdd_ADB_ATPD_Charges(), small_bold2));

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
                BI_Pdftableoutput_row18.addCell(BI_Pdftable_output_row1_cell5811);
                BI_Pdftableoutput_row18.addCell(BI_Pdftable_output_row1_cell5822);
                BI_Pdftableoutput_row18.addCell(BI_Pdftable_output_row1_cell118);
                BI_Pdftableoutput_row18.addCell(BI_Pdftable_output_row1_cell78);
                BI_Pdftableoutput_row18.addCell(BI_Pdftable_output_row1_cell881);

                BI_Pdftableoutput_row18.addCell(BI_Pdftable_output_row1_cell98);

                BI_Pdftableoutput_row18.addCell(BI_Pdftable_output_row1_cell108);
                BI_Pdftableoutput_row18.addCell(BI_Pdftable_output_row1_cell128);

                BI_Pdftableoutput_row18.addCell(BI_Pdftable_output_row1_cell138);
                BI_Pdftableoutput_row18.addCell(BI_Pdftable_output_row1_cell148);

                document.add(BI_Pdftableoutput_row18);

            }


            PdfPTable BI_PdftableNetYield = new PdfPTable(4);

            BI_PdftableNetYield.setWidthPercentage(100);
            float[] columnWidthsNetYield = {5f, 9f, 9f, 1f};
            BI_PdftableNetYield.setWidths(columnWidthsNetYield);
            PdfPCell BI_PdftableBI_PdftableNetYield_cell1 = new PdfPCell(
                    new Paragraph("", small_normal));
            PdfPCell BI_PdftableBI_PdftableNetYield_cell2 = new PdfPCell(
                    new Paragraph("Net Yield -  " + netYield8 + " %", small_bold2));

            PdfPCell BI_PdftableBI_PdftableNetYield_cell3 = new PdfPCell(
                    new Paragraph("Reduction in yield -  " + "" + " %",
                            small_bold2));

            PdfPCell BI_PdftableBI_PdftableNetYield_cell4 = new PdfPCell(
                    new Paragraph("", small_bold2));

            BI_PdftableBI_PdftableNetYield_cell1
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableBI_PdftableNetYield_cell1.setPadding(5);

            BI_PdftableBI_PdftableNetYield_cell2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableBI_PdftableNetYield_cell2.setPadding(5);

            BI_PdftableBI_PdftableNetYield_cell3
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableBI_PdftableNetYield_cell3.setPadding(5);

            BI_PdftableBI_PdftableNetYield_cell4
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableBI_PdftableNetYield_cell4.setPadding(5);

            BI_PdftableNetYield.addCell(BI_PdftableBI_PdftableNetYield_cell1);
            BI_PdftableNetYield.addCell(BI_PdftableBI_PdftableNetYield_cell2);
            BI_PdftableNetYield.addCell(BI_PdftableBI_PdftableNetYield_cell3);
            BI_PdftableNetYield.addCell(BI_PdftableBI_PdftableNetYield_cell4);

            //document.add(BI_PdftableNetYield);


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

            PdfPTable BI_Pdftableoutput4 = new PdfPTable(17);
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

            PdfPCell BI_Pdftable_output_cell7833 = new PdfPCell(new Paragraph(
                    "PPWB charges", small_bold2));

            PdfPCell BI_Pdftable_output_cell7844 = new PdfPCell(new Paragraph(
                    "Add. ADB & ATPD Charges", small_bold2));

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
            BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell7833);
            BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell7844);
            BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell7811);
            BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell8814);
            BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell8811);
            BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell981);

            BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell1081);
            BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell1181);
            BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell1281);

            BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell1381);

            document.add(BI_Pdftableoutput4);

            /*PdfPTable BI_Pdftableoutput_no4 = new PdfPTable(17);
            BI_Pdftableoutput_no4.setWidthPercentage(100);*/

            /*PdfPCell BI_Pdftable_output_no_cell181 = new PdfPCell(new Paragraph(
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
            PdfPCell BI_Pdftable_output_no_cell24812 = new PdfPCell(new Paragraph(
                    "16", small_bold2));
            PdfPCell BI_Pdftable_output_no_cell24813 = new PdfPCell(new Paragraph(
                    "17", small_bold2));

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
            BI_Pdftableoutput_no4.addCell(BI_Pdftable_output_no_cell24812);
            BI_Pdftableoutput_no4.addCell(BI_Pdftable_output_no_cell24813);*/
            //document.add(BI_Pdftableoutput_no4);

            for (int i = 0; i < Integer.parseInt(policy_term); i++) {

                PdfPTable BI_Pdftableoutput_row184 = new PdfPTable(17);
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
                PdfPCell BI_Pdftable_output_row1_cell58143 = new PdfPCell(
                        new Paragraph(list_data2.get(i)
                                .getPPWB_charges(), small_bold2));
                PdfPCell BI_Pdftable_output_row1_cell58145 = new PdfPCell(
                        new Paragraph(list_data2.get(i)
                                .getAdd_ADB_ATPD_Charges(), small_bold2));

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
                BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell58143);
                BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell58145);
                BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell1181);
                BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell781);
                BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell8811);

                BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell981);

                BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell1081);
                BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell1281);

                BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell1381);
                BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell1481);

                document.add(BI_Pdftableoutput_row184);

            }


            document.add(new_line);
            document.add(note);
            document.add(table2);
            document.add(new_line);
            document.add(ls);

            document.add(table3);
            document.add(new_line);

            // document.add(para7);
            document.add(new_line);
            PdfPTable BI_Pdftable_CompanysPolicySurrender21 = new PdfPTable(1);
            BI_Pdftable_CompanysPolicySurrender21.setWidthPercentage(100);
            PdfPCell BI_Pdftable_CompanysPolicySurrender21_cell = new PdfPCell(
                    new Paragraph(
                            "Important:",
                            small_normal));

            BI_Pdftable_CompanysPolicySurrender21_cell.setPadding(5);

            BI_Pdftable_CompanysPolicySurrender21
                    .addCell(BI_Pdftable_CompanysPolicySurrender21_cell);
            document.add(BI_Pdftable_CompanysPolicySurrender21);

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

            PdfPTable BI_Pdftable_CompanysPolicySurrender4 = new PdfPTable(1);
            BI_Pdftable_CompanysPolicySurrender4.setWidthPercentage(100);
            PdfPCell BI_Pdftable_CompanysPolicySurrender4_cell = new PdfPCell(
                    new Paragraph(
                            "If premium is greater than Rs 1 Lakh, You have to submit Proof of source of Fund",
                            small_normal));

            BI_Pdftable_CompanysPolicySurrender4_cell.setPadding(5);

            BI_Pdftable_CompanysPolicySurrender4
                    .addCell(BI_Pdftable_CompanysPolicySurrender4_cell);

            if (Double.parseDouble(premiumAmount) > 100000) {
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
            document.add(new_line);

            document.add(BI_Pdftable26);
            document.add(new_line);

            if (!bankUserType.equalsIgnoreCase("Y")) {

                if (BI_PdftablePolicyHolder != null) {
                    document.add(BI_PdftablePolicyHolder);
                }
            }

            document.add(BI_PdftablePolicyHolder_signature);
            document.add(new_line);

            document.add(agentDeclarationTable);
            document.add(new_line);

            if (!bankUserType.equalsIgnoreCase("Y")) {
                if (BI_PdftableMarketing != null) {
                    document.add(BI_PdftableMarketing);
                }
                if (BI_PdftableMarketing_signature != null) {
                    document.add(BI_PdftableMarketing_signature);
                }
            }

            document.close();

        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    /********************************** Help ends here **********************************************************/
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

    private boolean validateMaturityAge() {
        int childMaturityAge = Integer.parseInt(spnr_Age.getSelectedItem()
                .toString())
                + Integer.parseInt(spnrPolicyTerm.getSelectedItem().toString());
        int proposerMaturityAge = Integer
                .parseInt(spnr_bi_smart_scholar_proposer_age.getSelectedItem()
                        .toString())
                + Integer.parseInt(spnrPolicyTerm.getSelectedItem().toString());
        if (childMaturityAge > 25 || childMaturityAge < 18) {
            showAlert
                    .setMessage("Maturity Age of Child should be between 18 to 25 years and Maturity Age of proposer should be less than or equal to 65 years.");
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            showAlert.show();
            return false;
        } else if (proposerMaturityAge > 65) {
            showAlert
                    .setMessage("Maturity Age of Child should be between 18 to 25 years and Maturity Age of proposer should be less than or equal to 65 years.");
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            showAlert.show();
            return false;
        }
        return true;
    }

}
