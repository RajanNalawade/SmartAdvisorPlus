package sbilife.com.pointofsale_bancaagency.smartwealthbuilder;

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
import android.view.View.OnFocusChangeListener;
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
import android.widget.RadioGroup;
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
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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
import sbilife.com.pointofsale_bancaagency.Utility;
import sbilife.com.pointofsale_bancaagency.common.BIPdfMail;
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

public class BI_SmartWealthBuilderActivity extends AppCompatActivity implements
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
    private CheckBox isStaffDisc;
    private CheckBox selViewSFIN;
    private Spinner ageInYears;
    private Spinner selGender;
    private Spinner selPlan;
    private Spinner selPremiumFrequencyMode;
    private Spinner selPolicyTerm;
    private Spinner premPayingTerm;
    private TextView txtPremiumPayingTerm;
    private TextView txtPremiumFreqMode;
    private TextView help_policyTerm;
    private TextView help_premiumAmt;
    private TextView help_SAMF;
    private TextView help_noOfYearsElapsedSinceInception;
    private EditText premiumAmt;
    private EditText SAMF;
    private EditText noOfYearsElapsedSinceInception;
    private EditText percent_EquityFund;
    private EditText percent_EquityOptFund;
    private EditText percent_GrowthFund;
    private EditText percent_BalancedFund;
    private EditText percent_BondFund;
    private EditText percent_MoneyMktFund;
    private EditText percent_Top300Fund, percent_BondOptimiserFund,
            percent_CorporateBondFund, percent_MidCapFund, percent_PureFund;
    private Button btnSubmit;
    private Button back;
    private TableRow tbSFINEquityFund;
    private TableRow tbSFINBondFund;
    private TableRow tbSFINTop300Fund;
    private TableRow tbSFINEquityOptFund;
    private TableRow tbSFINGrowthFund;
    private TableRow tbSFINBalancedFund;
    private TableRow tbSFINMoneyMktFund, tbSFINBondOptimiserFund,
            tbSFINCorporateBondFund, tbSFINMidcapFund, tbSFINPureFund;

    private StringBuilder retVal;
    private StringBuilder bussIll = null;


    private String QuatationNumber = "";
    private String planName = "";
    private String premiumAmount = "";
    // class declaration
    private CommonForAllProd commonForAllProd;
    private SmartWealthBuilderProperties prop;
    private SmartWealthBuilderBean smartWealthBuilderBean;

    // variable declaration
    private DecimalFormat currencyFormat;
    private AlertDialog.Builder showAlert;
    private String effectivePremium;

    private Spinner spnr_bi_smart_wealth_builder_proposer_title, spinnerProposerGender;
    private EditText edt_bi_smart_wealth_builder_proposer_first_name;
    private EditText edt_bi_smart_wealth_builder_proposer_middle_name;
    private EditText edt_bi_smart_wealth_builder_proposer_last_name;
    private Button btn_bi_smart_wealth_builder_proposer_date;

    private EditText edt_bi_smart_wealth_builder_life_assured_first_name;
    private EditText edt_bi_smart_wealth_builder_life_assured_middle_name;
    private EditText edt_bi_smart_wealth_builder_life_assured_last_name;
    private EditText edt_bi_smart_wealth_builder_life_assured_age;
    private TextView textviewProposerAge;

    private Spinner spnr_bi_smart_wealth_builder_life_assured_title;

    private Button btn_bi_smart_wealth_builder_life_assured_date;

    private String lifeAssured_Title = "";
    private String lifeAssured_First_Name = "";
    private String lifeAssured_Middle_Name = "";
    private String lifeAssured_Last_Name = "";
    private String name_of_life_assured = "";
    private String lifeAssured_date_of_birth = "";

    private  LinearLayout linearlayoutProposerDetails;
    private String output = "";
    private String input = "";
    private String age_entry = "";

    private String gender = "";
    private String perInvBondOptimiserFund = "";
    private String perInvCorporateBondFund = "";
    private String perInvMidcapFund = "";
    private String perInvPureFund = "";

    private String planType = "";

    private String annPrem = "";
    private String netYield8pa = "";
    private String sumAssured = "";

    private List<M_BI_SmartWealthBuilderAdapterCommon> list_data;
    private List<M_BI_SmartWealthBuilderAdapterCommon2> list_data1;
    private List<M_BI_SmartWealthBuilderAdapterCommon2> list_data2;
    private String plan = "";
    private String premPayTerm = "";
    private final String premFreqMode = "";
    private String perInvEquityFund = "";
    private String perInvEquityOptimiserFund = "";
    private String perInvgrowthFund = "";
    private String perInvBalancedFund = "";
    private String perInvBondFund = "";
    private String perInvMoneyMarketFund = "";
    private String perInvTop300Fund = "";

    private String noOfYrElapsed = "";

    private String redInYieldNoYr = "";

    private String policyTerm = "";

    private String redInYieldMat = "";


    // For Bi Dialog
    private ParseXML prsObj;
    private String name_of_proposer = "";
    private String name_of_person = "";
    private String place2 = "";
    private String date1 = "";
    private String date2 = "";
    private String strProposerAge = "";
    private String agent_sign = "";
    private String proposer_sign = "";

    private File mypath;

    private String proposer_Title = "";
    private String proposer_First_Name = "";
    private String proposer_Middle_Name = "";
    private String proposer_Last_Name = "";
    private String proposer_Is_Same_As_Life_Assured = "";

    private Button btn_MarketingOfficalDate;
    private Button btn_PolicyholderDate;

    private ImageButton Ibtn_signatureofMarketing;
    private ImageButton Ibtn_signatureofPolicyHolders;

    private String gender_proposer = "";

    private Dialog d;
    private final int SIGNATURE_ACTIVITY = 1;
    private String latestImage = "";
    // Variable Used For Date Purpose
    private final int DATE_DIALOG_ID = 1;
    private int DIALOG_ID;
    private int mYear;
    private int mMonth;
    private int mDay;

    // for BI
    private StringBuilder inputVal;
    // EditText edt_bi_flexi_smart_proposer_age;
    private String proposer_date_of_birth = "";


    private CommonForAllProd obj;
    private int b;

    private LinearLayout llSmartWealthBuilderMain;
    private EditText edt_proposerdetail_basicdetail_contact_no;
    private final DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

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
    private EditText edt_proposerdetail_basicdetail_Email_id;
    private EditText edt_proposerdetail_basicdetail_ConfirmEmail_id;

    private String mobileNo = "";
    private String emailId = "";
    private String ConfirmEmailId = "";
    private boolean validationFla1 = false;
    private String ProposerEmailId = "";
    private Bitmap photoBitmap;
    private String product_Code, product_UIN, product_cateogory, product_type;
    private String bankUserType = "", transactionMode = "";

    private String Check = "";
    private Context context;
    private CommonMethods commonMethods;
    private StorageUtils mStorageUtils;
    private ImageButton imageButtonProposerPhotograph;

    private ImageButton Ibtn_signatureofLifeAssured;
    private String proposerAsLifeAssuredSign = "";
    private String Company_policy_surrender_dec = "";

    private CheckBox cb_kerladisc;
    private Double AnnulizedPremium = 0.0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.bi_smartwealthbuildermain);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        Utility utilityObj = new Utility();

        dbHelper = new DatabaseHelper(getApplicationContext());

        context = this;
        commonMethods = new CommonMethods();
        mStorageUtils = new StorageUtils();
        commonMethods.setActionbarLayout(this);

        NABIObj = new NeedAnalysisBIService(this);
        BIPdfMail objBIPdfMail = new BIPdfMail();
        prsObj = new ParseXML();
        Intent intent = getIntent();

        //Added Tushar Kadam Kerla Applicable Taxes 7-Jun-2019
        TableRow tablerowKerlaDiscount = findViewById(R.id.tablerowKerlaDiscount);
        cb_kerladisc = findViewById(R.id.cb_kerladisc);
        commonMethods.setKerlaDiscount(context, tablerowKerlaDiscount, cb_kerladisc);
        //End Added Tushar Kadam Kerla Applicable Taxes 7-Jun-2019

        String na_flag = intent.getStringExtra("NAFlag");

        if (na_flag != null) {
            if (na_flag.equalsIgnoreCase("1")) {
                needAnalysis_flag = 1;
                na_input = intent.getStringExtra("NaInput");
                na_output = intent.getStringExtra("NaOutput");
                String na_dob = intent.getStringExtra("custDOB");
                gender = intent.getStringExtra("custGender");
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

                    ProductInfo prodInfoObj = new ProductInfo(context);
                    planName = "Smart Wealth Builder";
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

        // con=this;
        initialiseDate();
        Date();
        // db = new newDBHelper(this);
        // Intent intent = getIntent();
        obj = new CommonForAllProd();
        llSmartWealthBuilderMain = findViewById(R.id.ll_bi_smart_wealth_builder_main);
        spnr_bi_smart_wealth_builder_proposer_title = findViewById(R.id.spnr_bi_smart_wealth_builder_proposer_title);
        spinnerProposerGender = findViewById(R.id.spinnerProposerGender);
        textviewProposerAge = findViewById(R.id.textviewProposerAge);
        textviewProposerAge.setClickable(false);
        edt_bi_smart_wealth_builder_proposer_first_name = findViewById(R.id.edt_bi_smart_wealth_builder_proposer_first_name);
        edt_bi_smart_wealth_builder_proposer_middle_name = findViewById(R.id.edt_bi_smart_wealth_builder_proposer_middle_name);
        edt_bi_smart_wealth_builder_proposer_last_name = findViewById(R.id.edt_bi_smart_wealth_builder_proposer_last_name);
        btn_bi_smart_wealth_builder_proposer_date = findViewById(R.id.btn_bi_smart_wealth_builder_proposer_date);
        btn_bi_smart_wealth_builder_life_assured_date = findViewById(R.id.btn_bi_smart_wealth_builder_life_assured_date);
        edt_bi_smart_wealth_builder_life_assured_age = findViewById(R.id.edt_bi_smart_wealth_builder_life_assured_age);

        spnr_bi_smart_wealth_builder_life_assured_title = findViewById(R.id.spnr_bi_smart_wealth_builder_life_assured_title);
        edt_bi_smart_wealth_builder_life_assured_first_name = findViewById(R.id.edt_bi_smart_wealth_builder_life_assured_first_name);
        edt_bi_smart_wealth_builder_life_assured_middle_name = findViewById(R.id.edt_bi_smart_wealth_builder_life_assured_middle_name);
        edt_bi_smart_wealth_builder_life_assured_last_name = findViewById(R.id.edt_bi_smart_wealth_builder_life_assured_last_name);
        linearlayoutProposerDetails = findViewById(R.id.linearlayoutProposerDetails);
        btnSubmit = findViewById(R.id.btnSubmit);
        back = findViewById(R.id.back);

        edt_proposerdetail_basicdetail_contact_no = findViewById(R.id.edt_smart_builider_contact_no);
        edt_proposerdetail_basicdetail_Email_id = findViewById(R.id.edt_smart_builder_Email_id);
        edt_proposerdetail_basicdetail_ConfirmEmail_id = findViewById(R.id.edt_smart_builder_ConfirmEmail_id);

        // currentRecordId = ProductHomePageActivity.currentRecord + "";
        // try {
        // List<M_Product_Detail> data = db
        // .getProductDetailByMasterKey(currentRecordId);
        // if (data.size() > 0) {
        // QuatationNumber = data.get(0).getQuotation_Number();
        // planName = data.get(0).getPlan_Name();
        // sr_Code = data.get(0).getAgent_Id();
        // }
        // } catch (Exception e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        //
        // ProductHomePageActivity.path.setText(" Benefit Illustrator");
        // ProductHomePageActivity.tv_QuatationNo2
        // .setText(ProductHomePageActivity.quotation_Number);

        prsObj = new ParseXML();
        list_data = new ArrayList<>();
        list_data1 = new ArrayList<>();
        list_data2 = new ArrayList<>();
        /*
         * title_list.add(4,"Gen."); title_list.add(5,"Lt.Gen.");
         * title_list.add(6,"Maj.Gen."); title_list.add(7,"Brig.");
         * title_list.add(8,"Col."); title_list.add(9,"Lt.Col.");
         * title_list.add(10,"Major"); title_list.add(11,"Capt.");
         * title_list.add(12,"Lt."); title_list.add(13,"Gr.Capt.");
         * title_list.add(14,"Fr."); title_list.add(15,"Dr.");
         */
        commonMethods.fillSpinnerValue(context, spnr_bi_smart_wealth_builder_proposer_title,
                Arrays.asList(getResources().getStringArray(R.array.arr_select_title)));
        commonMethods.fillSpinnerValue(context, spnr_bi_smart_wealth_builder_life_assured_title,
                Arrays.asList(getResources().getStringArray(R.array.arr_select_title)));

        // String frmDashboard = ProductHomePageActivity.frmDashboard;
        // if (frmDashboard.equals("FALSE")) {
        // if (getValueFromDatabase()) {
        // // Dialog();
        // }
        // }

        // For BI
        // retVal = new StringBuilder();
        // bussIll = new StringBuilder();

        // class declaration
        commonForAllProd = new CommonForAllProd();
        prop = new SmartWealthBuilderProperties();

        // variable declaration
        currencyFormat = new DecimalFormat("##,##,##,###");
        showAlert = new AlertDialog.Builder(this);

        // UI elements initialization
        TextView inputActivityHeader = findViewById(R.id.txt_input_activityheader);
        isStaffDisc = findViewById(R.id.cb_staffdisc);

        // Age
        ageInYears = findViewById(R.id.age);
        ageInYears.setEnabled(false);

        String[] ageList = new String[64];
        for (int i = 2; i <= 65; i++) {
            ageList[i - 2] = i + "";
        }
        ArrayAdapter<String> ageAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, ageList);
        ageAdapter.setDropDownViewResource(R.layout.spinner_item1);
        ageInYears.setAdapter(ageAdapter);
        ageAdapter.notifyDataSetChanged();

        // gender
        selGender = findViewById(R.id.selGender);

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item,
                getResources().getStringArray(R.array.gender_all_arrays));
        genderAdapter.setDropDownViewResource(R.layout.spinner_item1);
        selGender.setAdapter(genderAdapter);
        genderAdapter.notifyDataSetChanged();

        spinnerProposerGender.setAdapter(genderAdapter);
        genderAdapter.notifyDataSetChanged();

        // // proposer age
        // ProposerAge = (Spinner) findViewById(R.id.proposerAge);
        // String[] proposerAgeList = new String[73];
        // for (int i = 18; i <= 90; i++) {
        // proposerAgeList[i - 18] = i + "";
        // }
        // ArrayAdapter<String> proposerAgeAdapter = new ArrayAdapter<String>(
        // getApplicationContext(), R.layout.spinner_item, proposerAgeList);
        // proposerAgeAdapter.setDropDownViewResource(R.layout.spinner_item1);
        // ProposerAge.setAdapter(proposerAgeAdapter);
        // proposerAgeAdapter.notifyDataSetChanged();

        // plan type
        selPlan = findViewById(R.id.planType);
        String[] planList = new String[]{"Single", "Regular", "Limited"};
        ArrayAdapter<String> planAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, planList);
        planAdapter.setDropDownViewResource(R.layout.spinner_item1);
        selPlan.setAdapter(planAdapter);
        /* Changes done by vrushali on 9/4/2015 */
        // selPlan.setSelection(planAdapter.getPosition("Limited"));
        planAdapter.notifyDataSetChanged();

        // premium frequency mode
        txtPremiumFreqMode = findViewById(R.id.txt_premium_frequency_mode);
        selPremiumFrequencyMode = findViewById(R.id.premium_frequency_mode);
        String[] premFreq = new String[]{"Yearly"};
        ArrayAdapter<String> premFreqAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, premFreq);
        premFreqAdapter.setDropDownViewResource(R.layout.spinner_item1);
        selPremiumFrequencyMode.setAdapter(premFreqAdapter);
        premFreqAdapter.notifyDataSetChanged();

        // Premium Payment term
        txtPremiumPayingTerm = findViewById(R.id.txt_premiumPayingTerm);
        premPayingTerm = findViewById(R.id.premiumPayingTerm);
        //String[] premPayingTermList = new String[]{"5", "8", "10"};
        String[] premPayingTermList = new String[]{"7", "10", "12", "15"};
        ArrayAdapter<String> premPayingTermAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item,
                premPayingTermList);
        premPayingTermAdapter.setDropDownViewResource(R.layout.spinner_item1);
        premPayingTerm.setAdapter(premPayingTermAdapter);
        premPayingTermAdapter.notifyDataSetChanged();

        // policy Term
        selPolicyTerm = findViewById(R.id.policyTerm);
        String[] policyTermList = new String[26];
        for (int i = 5; i <= 30; i++) {
            policyTermList[i - 5] = i + "";
        }
        ArrayAdapter<String> policyTermAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, policyTermList);
        policyTermAdapter.setDropDownViewResource(R.layout.spinner_item1);
        selPolicyTerm.setAdapter(policyTermAdapter);
        /* Changes done by vrushali on 9/4/2015 */
        // selPolicyTerm.setSelection(policyTermAdapter.getPosition("10"));
        policyTermAdapter.notifyDataSetChanged();

        // Premium Amount
        premiumAmt = findViewById(R.id.premium_Amt);

        // SAMF
        SAMF = findViewById(R.id.samf);
        SAMF.setFilters(new InputFilter[]{new DesimalDigitsInputFilter(2)});
        TableRow tableRow6 = findViewById(R.id.tableRow6);
        TableRow tableRow7 = findViewById(R.id.tableRow7);

        // YearsElapsedSinceInception
        noOfYearsElapsedSinceInception = findViewById(R.id.years_elapsed_since_inception);

        // Funds
        percent_EquityFund = findViewById(R.id.equityFund);
        percent_EquityOptFund = findViewById(R.id.equityOptFund);
        percent_GrowthFund = findViewById(R.id.growthFund);
        percent_BalancedFund = findViewById(R.id.balancedFund);
        percent_BondFund = findViewById(R.id.bondFund);
        percent_MoneyMktFund = findViewById(R.id.moneyMktFund);
        percent_Top300Fund = findViewById(R.id.top300Fund);

        percent_BondOptimiserFund = findViewById(R.id.bondOptimiserFund);
        percent_CorporateBondFund = findViewById(R.id.corporateBondFund);
        percent_MidCapFund = findViewById(R.id.midCapFund);
        percent_PureFund = findViewById(R.id.pureFund);

        help_SAMF = findViewById(R.id.help_samf);
        help_premiumAmt = findViewById(R.id.help_premAmt);
        help_policyTerm = findViewById(R.id.help_policyterm);
        help_noOfYearsElapsedSinceInception = findViewById(R.id.help_years_elapsed_since_inception);

        // SFIN of Funds
        selViewSFIN = findViewById(R.id.selViewSFIN);
        tbSFINEquityFund = findViewById(R.id.tbSFINEquityFund);
        tbSFINBondFund = findViewById(R.id.tbSFINBondFund);
        tbSFINTop300Fund = findViewById(R.id.tbSFINTop300Fund);
        tbSFINEquityOptFund = findViewById(R.id.tbSFINEquityOptFund);
        tbSFINGrowthFund = findViewById(R.id.tbSFINGrowthFund);
        tbSFINBalancedFund = findViewById(R.id.tbSFINBalancedFund);
        tbSFINMoneyMktFund = findViewById(R.id.tbSFINMoneyMktFund);

        tbSFINBondOptimiserFund = findViewById(R.id.tbSFINBondOptimiserFund);
        tbSFINCorporateBondFund = findViewById(R.id.tbSFINCorporateBondFund);
        tbSFINMidcapFund = findViewById(R.id.tbSFINMidcapFund);
        tbSFINPureFund = findViewById(R.id.tbSFINPureFund);

        /* For Current Date */
        Date();
        // setBIInputGui();
        edt_bi_smart_wealth_builder_life_assured_first_name
                .setOnEditorActionListener(this);
        edt_bi_smart_wealth_builder_life_assured_middle_name
                .setOnEditorActionListener(this);
        edt_bi_smart_wealth_builder_life_assured_last_name
                .setOnEditorActionListener(this);
        edt_bi_smart_wealth_builder_proposer_first_name
                .setOnEditorActionListener(this);
        edt_bi_smart_wealth_builder_proposer_middle_name
                .setOnEditorActionListener(this);
        edt_bi_smart_wealth_builder_proposer_last_name
                .setOnEditorActionListener(this);

        premiumAmt.setOnEditorActionListener(this);
        SAMF.setOnEditorActionListener(this);
        noOfYearsElapsedSinceInception.setOnEditorActionListener(this);
        percent_EquityFund.setOnEditorActionListener(this);
        percent_EquityOptFund.setOnEditorActionListener(this);
        percent_GrowthFund.setOnEditorActionListener(this);
        percent_BalancedFund.setOnEditorActionListener(this);
        percent_BondFund.setOnEditorActionListener(this);
        percent_MoneyMktFund.setOnEditorActionListener(this);
        percent_Top300Fund.setOnEditorActionListener(this);
        percent_BondOptimiserFund.setOnEditorActionListener(this);
        percent_CorporateBondFund.setOnEditorActionListener(this);
        percent_MidCapFund.setOnEditorActionListener(this);
        percent_PureFund.setOnEditorActionListener(this);


        setFocusable(spnr_bi_smart_wealth_builder_life_assured_title);

        spnr_bi_smart_wealth_builder_life_assured_title.requestFocus();

        setSpinnerAndOtherListner();

        edt_proposerdetail_basicdetail_contact_no
                .addTextChangedListener(new TextWatcher() {

                    public void onTextChanged(CharSequence arg0, int arg1,
                                              int arg2, int arg3) {


                    }

                    public void beforeTextChanged(CharSequence arg0, int arg1,
                                                  int arg2, int arg3) {


                    }

                    public void afterTextChanged(Editable arg0) {

                        String abc = edt_proposerdetail_basicdetail_contact_no
                                .getText().toString();
                        mobile_validation(abc);

                    }
                });

        edt_proposerdetail_basicdetail_Email_id
                .addTextChangedListener(new TextWatcher() {

                    public void onTextChanged(CharSequence arg0, int arg1,
                                              int arg2, int arg3) {


                    }

                    public void beforeTextChanged(CharSequence arg0, int arg1,
                                                  int arg2, int arg3) {


                    }

                    public void afterTextChanged(Editable arg0) {

                        ProposerEmailId = edt_proposerdetail_basicdetail_Email_id
                                .getText().toString();
                        //email_id_validation(ProposerEmailId);

                    }
                });

        edt_proposerdetail_basicdetail_ConfirmEmail_id
                .addTextChangedListener(new TextWatcher() {

                    public void onTextChanged(CharSequence arg0, int arg1,
                                              int arg2, int arg3) {


                    }

                    public void beforeTextChanged(CharSequence arg0, int arg1,
                                                  int arg2, int arg3) {


                    }

                    public void afterTextChanged(Editable arg0) {

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
        TableRow tr_staff_disc = findViewById(R.id.tr_smart_builder_staff_disc);

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

    private void setSpinnerAndOtherListner() {


        selPremiumFrequencyMode
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {

                        if (selPlan.getSelectedItem().toString()
                                .equalsIgnoreCase("Limited")) {
                            setFocusable(premPayingTerm);
                            premPayingTerm.requestFocus();
                        } else {
                            setFocusable(selPolicyTerm);
                            selPolicyTerm.requestFocus();
                        }

                    }

                    public void onNothingSelected(AdapterView<?> arg0) {


                    }
                });

        // plan type
        selPlan.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
                                       long id) {


                if (selPlan.getSelectedItem().toString().equals("Regular")
                        || selPlan.getSelectedItem().toString()
                        .equals("Limited")) {
                    if (proposer_Is_Same_As_Life_Assured.equals("")) {
                        showAlert
                                .setMessage("Please Select Whether Proposer Is Same As Life Assured");
                        showAlert.setNeutralButton("OK",
                                new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                    }
                                });
                        showAlert.show();
                    } else if (Integer.parseInt(ageInYears.getSelectedItem()
                            .toString()) > 60) {

                        if (Integer
                                .parseInt(!textviewProposerAge
                                        .getText().toString().equals("") ? textviewProposerAge
                                        .getText().toString() : "0") < 18) {
                            showAlert
                                    .setMessage("Life Assured Date of Birth Must Be Less Than 60");

                            showAlert.setNeutralButton("OK",
                                    new DialogInterface.OnClickListener() {

                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                        }
                                    });
                            showAlert.show();
                            btn_bi_smart_wealth_builder_proposer_date
                                    .setText("Select Date");
                        } else {
                            showAlert
                                    .setMessage("Proposer Date of Birth Must Be Less Than 60");

                            showAlert.setNeutralButton("OK",
                                    new DialogInterface.OnClickListener() {

                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                        }
                                    });
                            showAlert.show();
                            btn_bi_smart_wealth_builder_life_assured_date
                                    .setText("Select Date");
                        }
                    } else {
                        clearFocusable(selPlan);
                        if (selPlan.getSelectedItem().toString()
                                .equals("Regular")) {
                            setFocusable(selPolicyTerm);
                            selPolicyTerm.requestFocus();
                        } else {
                            setFocusable(premPayingTerm);
                            premPayingTerm.requestFocus();
                        }

                    }
                    clearFocusable(selPlan);
                } else {
                    clearFocusable(selPlan);
                    setFocusable(selPolicyTerm);
                    selPolicyTerm.requestFocus();
                }

                updatePolicyTermLabel();
                updatePremiumAmtLabel();
                updateSAMFlabel();
                effectivePremium = getEffectivePremium();

                if (pos == 0) {
                    txtPremiumFreqMode.setVisibility(View.GONE);
                    selPremiumFrequencyMode.setVisibility(View.GONE);
                    txtPremiumPayingTerm.setVisibility(View.GONE);
                    premPayingTerm.setVisibility(View.GONE);
                } else if (pos == 1) {
                    txtPremiumFreqMode.setVisibility(View.VISIBLE);
                    selPremiumFrequencyMode.setVisibility(View.VISIBLE);
                    txtPremiumPayingTerm.setVisibility(View.GONE);
                    premPayingTerm.setVisibility(View.GONE);
                } else if (pos == 2) {
                    txtPremiumFreqMode.setVisibility(View.VISIBLE);
                    selPremiumFrequencyMode.setVisibility(View.VISIBLE);
                    txtPremiumPayingTerm.setVisibility(View.VISIBLE);
                    premPayingTerm.setVisibility(View.VISIBLE);
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {


            }

        });

        // premium paying term
        premPayingTerm.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
                                       long id) {

                updatePolicyTermLabel();
                clearFocusable(premPayingTerm);
                setFocusable(selPolicyTerm);
                selPolicyTerm.requestFocus();
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        // age in years
        ageInYears.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
                                       long id) {

                updatePolicyTermLabel();
                updateSAMFlabel();
            }

            public void onNothingSelected(AdapterView<?> arg0) {


            }

        });

        // policy term
        selPolicyTerm.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
                                       long id) {

                valMaturityAge();
                updateSAMFlabel();
                updatenoOfYearsElapsedSinceInception();
                noOfYearsElapsedSinceInception.setText(selPolicyTerm
                        .getSelectedItem().toString().trim());

                if (b == 1) {
                    edt_bi_smart_wealth_builder_life_assured_first_name
                            .requestFocus();
                    b = 0;
                } else {

                    clearFocusable(selPolicyTerm);

                    setFocusable(premiumAmt);
                    premiumAmt.requestFocus();

                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {


            }

        });

        // premium amount
        premiumAmt.setOnFocusChangeListener(new OnFocusChangeListener() {

            public void onFocusChange(View arg0, boolean arg1) {

                updatePolicyTermLabel();
                if (!premiumAmt.getText().equals("")) {
                    effectivePremium = getEffectivePremium();
                }
            }
        });

        selViewSFIN
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {


                        if (isChecked) {
                            tbSFINEquityFund.setVisibility(View.VISIBLE);
                            tbSFINBondFund.setVisibility(View.VISIBLE);
                            tbSFINTop300Fund.setVisibility(View.VISIBLE);
                            tbSFINEquityOptFund.setVisibility(View.VISIBLE);
                            tbSFINGrowthFund.setVisibility(View.VISIBLE);
                            tbSFINBalancedFund.setVisibility(View.VISIBLE);
                            tbSFINMoneyMktFund.setVisibility(View.VISIBLE);

                            tbSFINBondOptimiserFund.setVisibility(View.VISIBLE);
                            tbSFINCorporateBondFund.setVisibility(View.VISIBLE);
                            tbSFINMidcapFund.setVisibility(View.VISIBLE);
                            tbSFINPureFund.setVisibility(View.VISIBLE);
                        } else {
                            tbSFINEquityFund.setVisibility(View.GONE);
                            tbSFINBondFund.setVisibility(View.GONE);
                            tbSFINTop300Fund.setVisibility(View.GONE);
                            tbSFINEquityOptFund.setVisibility(View.GONE);
                            tbSFINGrowthFund.setVisibility(View.GONE);
                            tbSFINBalancedFund.setVisibility(View.GONE);
                            tbSFINMoneyMktFund.setVisibility(View.GONE);

                            tbSFINBondOptimiserFund.setVisibility(View.GONE);
                            tbSFINCorporateBondFund.setVisibility(View.GONE);
                            tbSFINMidcapFund.setVisibility(View.GONE);
                            tbSFINPureFund.setVisibility(View.GONE);

                        }

                    }
                });

        spnr_bi_smart_wealth_builder_proposer_title
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long id) {


                        if (position == 0) {
                            proposer_Title = "";
                        } else {
                            proposer_Title = spnr_bi_smart_wealth_builder_proposer_title
                                    .getSelectedItem().toString();

                            if (proposer_Title.equalsIgnoreCase("Mr.")) {
                                // selGender.setSelection(getIndex(selGender,
                                // "Male"));
                            } else if (proposer_Title.equalsIgnoreCase("Ms.")
                                    || proposer_Title.equalsIgnoreCase("Mrs.")) {
                                // selGender.setSelection(getIndex(selGender,
                                // "Female"));
                            }
                            setFocusable(edt_bi_smart_wealth_builder_proposer_first_name);

                            edt_bi_smart_wealth_builder_proposer_first_name
                                    .requestFocus();

                        }

                    }

                    public void onNothingSelected(AdapterView<?> arg0) {


                    }
                });

        spnr_bi_smart_wealth_builder_life_assured_title
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long id) {

                        if (position == 0) {
                            lifeAssured_Title = "";
                        } else {
                            lifeAssured_Title = spnr_bi_smart_wealth_builder_life_assured_title
                                    .getSelectedItem().toString();

                            clearFocusable(spnr_bi_smart_wealth_builder_life_assured_title);

                            setFocusable(edt_bi_smart_wealth_builder_life_assured_first_name);

                            edt_bi_smart_wealth_builder_life_assured_first_name
                                    .requestFocus();

                        }
                    }

                    public void onNothingSelected(AdapterView<?> arg0) {


                    }
                });

        // staff disc
        isStaffDisc.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {


                if (isStaffDisc.isChecked()) {
                    isStaffDisc.setChecked(true);
                    clearFocusable(spnr_bi_smart_wealth_builder_life_assured_title);
                    setFocusable(spnr_bi_smart_wealth_builder_life_assured_title);
                    spnr_bi_smart_wealth_builder_life_assured_title
                            .requestFocus();
                }

            }
        });

        // go button

        back.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {

                setResult(RESULT_OK);
                finish();

            }
        });

        btnSubmit.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {

                inputVal = new StringBuilder();
                retVal = new StringBuilder();
                bussIll = new StringBuilder();

                proposer_First_Name = edt_bi_smart_wealth_builder_proposer_first_name
                        .getText().toString().trim();
                proposer_Middle_Name = edt_bi_smart_wealth_builder_proposer_middle_name
                        .getText().toString().trim();
                proposer_Last_Name = edt_bi_smart_wealth_builder_proposer_last_name
                        .getText().toString().trim();

                gender_proposer = spinnerProposerGender.getSelectedItem().toString();
                gender = selGender.getSelectedItem().toString();

                name_of_proposer = proposer_Title + " "
                        + proposer_First_Name + " "
                        + proposer_Middle_Name + " " + proposer_Last_Name;

                lifeAssured_First_Name = edt_bi_smart_wealth_builder_life_assured_first_name
                        .getText().toString().trim();
                lifeAssured_Middle_Name = edt_bi_smart_wealth_builder_life_assured_middle_name
                        .getText().toString().trim();
                lifeAssured_Last_Name = edt_bi_smart_wealth_builder_life_assured_last_name
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


                if (valProposerSameAsLifeAssured()
                        && valLifeAssuredProposerDetail() && valDob()
                        && valLifeAssuredMinorDetail() && valBasicDetail()
                        && valPolicyTerm() && valPremiumAmt() && valSAMF()
                        && valTotalAllocation()
                        && valYearsElapsedSinceInception()) {
                    // progress dialog is displayed while calculating
                    if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {
                        proposer_Title = "";
                        proposer_First_Name = "";
                        proposer_Middle_Name = "";
                        proposer_Last_Name = "";
                        name_of_proposer = "";
                        proposer_date_of_birth = "";
                    }

                    addListenerOnSubmit();
                    getInput(smartWealthBuilderBean);


                    // long count = insertDataIntoDatabase();
                    //
                    // if (count > 0) {
                    if (needAnalysis_flag == 0) {
                        Intent i = new Intent(getApplicationContext(),
                                Success.class);

                        i.putExtra("ProductName",
                                "Product : SBI Life - Smart Wealth Builder (UIN:111L095V03)");

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

                        i.putExtra("header", "SBI Life - Smart Wealth Builder");
                        i.putExtra("header1", "(UIN:111L095V03)");
                        startActivity(i);
                    } else
                        Dialog();
                    // }
                    // }
                    // };
                    // t.start();
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

                        d.dismiss();

                    } else if (latestImage.equalsIgnoreCase("proposerSignOnLifeAssured")) {

                        Ibtn_signatureofLifeAssured
                                .setImageBitmap(ProposerCaptureSignature.scaled);

                        Bitmap signature = ProposerCaptureSignature.scaled;
                        if (signature != null) {
                            ByteArrayOutputStream out = new ByteArrayOutputStream();
                            signature.compress(Bitmap.CompressFormat.PNG, 100,
                                    out);
                            byte[] signByteArray = out.toByteArray();
                            proposerAsLifeAssuredSign = Base64.encodeToString(
                                    signByteArray, Base64.DEFAULT);
                        }
                    }

                }
            }
        } else if (requestCode == 3) {
            if (resultCode == RESULT_OK) {
                if (Check.equals("Photo")) {
                    try {
                        File Photo = commonMethods.galleryAddPic(context);
                        Bitmap bmp = BitmapFactory.decodeFile(Photo
                                .getAbsolutePath());

                        Bitmap b;
                        Uri imageUri;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            imageUri = commonMethods.getContentUri(context, new File(Photo.toString()));
                        } else {
                            imageUri = Uri.fromFile(new File(Photo.toString()));
                        }

                        b = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                        Bitmap mFaceBitmap = b != null ? b.copy(Bitmap.Config.RGB_565, true) : null;
                        assert b != null;
                        b.recycle();
                        if (mFaceBitmap != null) {
                            Bitmap scaled = Bitmap.createScaledBitmap(bmp, 230,
                                    200, true);
                            photoBitmap = scaled;
                            imageButtonProposerPhotograph.setImageBitmap(scaled);
                        } else {
                            photoBitmap = null;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } catch (Error e) {
                        e.printStackTrace();
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

        input = inputVal.toString();
        output = retVal.toString();

        d.setContentView(R.layout.layout_smart_wealth_builder_bi_grid_common);

        TextView tv_proposername = d
                .findViewById(R.id.tv_proposername);
        TextView tv_proposal_number = d
                .findViewById(R.id.tv_proposal_number);

        TextView tv_smart_wealth_builder_distribution_channel = d
                .findViewById(R.id.tv_smart_wealth_builder_distribution_channel);


        TextView tv_bi_smart_wealth_builder_age_entry = d
                .findViewById(R.id.tv_bi_smart_wealth_builder_age_entry);
        TextView tv_bi_smart_wealth_builder_maturity_age = d
                .findViewById(R.id.tv_bi_smart_wealth_builder_maturity_age);
        TextView tv_bi_smart_wealth_builder_life_assured_gender = d
                .findViewById(R.id.tv_bi_smart_wealth_builder_life_assured_gender);
        TextView tv_bi_smart_wealth_builder_policy_term = d
                .findViewById(R.id.tv_bi_smart_wealth_builder_policy_term);
        TextView tv_bi_smart_wealth_builder_annualised_premium = d
                .findViewById(R.id.tv_bi_smart_wealth_builder_annualised_premium);

        TextView tv_bi_smart_wealth_builder_premium_type_yearly = d
                .findViewById(R.id.tv_bi_smart_wealth_builder_premium_type_yearly);
        TextView tv_bi_smart_wealth_builder_annualised_premium_yearly = d
                .findViewById(R.id.tv_bi_smart_wealth_builder_annualised_premium_yearly);


        TextView tv_bi_smart_wealth_builder_sum_assured_main = d
                .findViewById(R.id.tv_bi_smart_wealth_builder_sum_assured_main);
        TextView tv_bi_smart_wealth_builder_mode = d
                .findViewById(R.id.tv_bi_smart_wealth_builder_mode);
        TextView tv_bi_smart_wealth_builder_premium_paying_term = d
                .findViewById(R.id.tv_bi_smart_wealth_builder_premium_paying_term);

        TextView tv_smart_wealth_builder_equity_fund_allocation = d
                .findViewById(R.id.tv_smart_wealth_builder_equity_fund_allocation);
        TextView tv_smart_wealth_builder_equity_optimiser_fund_allocation = d
                .findViewById(R.id.tv_smart_wealth_builder_equity_optimiser_fund_allocation);
        TextView tv_smart_wealth_builder_growth_fund_allocation = d
                .findViewById(R.id.tv_smart_wealth_builder_growth_fund_allocation);
        TextView tv_smart_wealth_builder_balanced_fund_allocation = d
                .findViewById(R.id.tv_smart_wealth_builder_balanced_fund_allocation);
        TextView tv_smart_wealth_builder_bond_fund_allocation = d
                .findViewById(R.id.tv_smart_wealth_builder_bond_fund_allocation);
        TextView tv_smart_wealth_builder_market_fund_allocation = d
                .findViewById(R.id.tv_smart_wealth_builder_market_fund_allocation);
        TextView tv_smart_wealth_builder_top_300_fund_allocation = d
                .findViewById(R.id.tv_smart_wealth_builder_top_300_fund_allocation);


        TextView tv_smart_wealth_builder_bond_optimiser_fund_allocation = d
                .findViewById(R.id.tv_smart_wealth_builder_bond_optimiser_fund_allocation);
        TextView tv_smart_wealth_builder_corporate_bond_fund_allocation = d
                .findViewById(R.id.tv_smart_wealth_builder_corporate_bond_fund_allocation);
        TextView tv_smart_wealth_builder_midcap_fund_allocation = d
                .findViewById(R.id.tv_smart_wealth_builder_midcap_fund_allocation);
        TextView tv_smart_wealth_builder_pure_fund_allocation = d
                .findViewById(R.id.tv_smart_wealth_builder_pure_fund_allocation);


        TableRow tv_submit_proof = d.findViewById(R.id.tv_submit_proof);
        final String premiumAmount = prsObj.parseXmlTag(input, "premiumAmount");

        if (Double.parseDouble(premiumAmt.getText().toString()) > 100000) {
            tv_submit_proof.setVisibility(View.VISIBLE);
        } else {
            tv_submit_proof.setVisibility(View.GONE);
        }

        TextView tv_smart_wealth_builder_equity_fund_fmc = d
                .findViewById(R.id.tv_smart_wealth_builder_equity_fund_fmc);
        TextView tv_smart_wealth_builder_equity_optimiser_fund_fmc = d
                .findViewById(R.id.tv_smart_wealth_builder_equity_optimiser_fund_fmc);
        TextView tv_smart_wealth_builder_growth_fund_fmc = d
                .findViewById(R.id.tv_smart_wealth_builder_growth_fund_fmc);
        TextView tv_smart_wealth_builder_balanced_fund_fmc = d
                .findViewById(R.id.tv_smart_wealth_builder_balanced_fund_fmc);
        TextView tv_smart_wealth_builder_bond_fund_fmc = d
                .findViewById(R.id.tv_smart_wealth_builder_bond_fund_fmc);
        TextView tv_smart_elite_market_fund_fmc = d
                .findViewById(R.id.tv_smart_wealth_builder_market_fund_fmc);
        TextView tv_smart_wealth_builder_top_300_fund_fmc = d
                .findViewById(R.id.tv_smart_wealth_builder_top_300_fund_fmc);
        TextView tv_smart_wealth_builder_bond_optimiser_fund_fmc = d
                .findViewById(R.id.tv_smart_wealth_builder_bond_optimiser_fund_fmc);
        TextView tv_smart_wealth_builder_corporate_bond_fund_fmc = d
                .findViewById(R.id.tv_smart_wealth_builder_corporate_bond_fund_fmc);
        TextView tv_smart_wealth_builder_midcap_fund_fmc = d
                .findViewById(R.id.tv_smart_wealth_builder_midcap_fund_fmc);
        TextView tv_smart_wealth_builder_pure_fund_fmc = d
                .findViewById(R.id.tv_smart_wealth_builder_pure_fund_fmc);
        TextView tv_smart_wealth_builder_no_of_years_elapsed = d
                .findViewById(R.id.tv_smart_wealth_builder_no_of_years_elapsed);
        TextView tv_smart_wealth_builder_reduction_yield = d
                .findViewById(R.id.tv_smart_wealth_builder_reduction_yield);
        TextView tv_smart_wealth_builder_maturity_at = d
                .findViewById(R.id.tv_smart_wealth_builder_maturity_at);
        TextView tv_smart_wealth_builder_reduction_yeild2 = d
                .findViewById(R.id.tv_smart_wealth_builder_reduction_yeild2);


        TextView tv_ppt_builder = d
                .findViewById(R.id.tv_ppt_builder);

        /*TextView tv_premium_heading = (TextView) d
                .findViewById(R.id.tv_premium_heading);*/


        final TextView edt_proposer_name =  d
                .findViewById(R.id.edt_proposer_name);
        final EditText edt_Policyholderplace = d
                .findViewById(R.id.edt_Policyholderplace);

        TextView tv_smart_wealth_builder_death_benefit = d
                .findViewById(R.id.tv_smart_wealth_builder_death_benefit);

        TextView tv_smart_wealth_builder_net_yield = d
                .findViewById(R.id.tv_smart_wealth_builder_net_yield);
        TextView tv_smart_wealth_builder_reduction_in_yield = d
                .findViewById(R.id.tv_smart_wealth_builder_reduction_in_yield);


        TableRow tr_staff_discount = d
                .findViewById(R.id.tr_staff_discount);

        /*if (userType.equalsIgnoreCase("BAP") || userType.equalsIgnoreCase("CAG")) {
            tr_staff_discount.setVisibility(View.GONE);
        }*/

        TextView tv_bi_is_Staff = d
                .findViewById(R.id.tv_bi_is_Staff);


        GridView gv_userinfo = d.findViewById(R.id.gv_userinfo);
        GridView gv_userinfo2 = d.findViewById(R.id.gv_userinfo2);
        GridView gv_userinfo3 = d.findViewById(R.id.gv_userinfo3);

        final CheckBox cb_statement = d
                .findViewById(R.id.cb_statement);
        cb_statement.setChecked(false);

        /* Need Analysis */
        final TextView edt_proposer_name_need_analysis = d
                .findViewById(R.id.edt_proposer_name_need_analysis);

        final CheckBox cb_statement_need_analysis = d
                .findViewById(R.id.cb_statement_need_analysis);
        cb_statement_need_analysis.setChecked(true);
        LinearLayout tr_need_analysis = d
                .findViewById(R.id.tr_need_analysis);

        final CheckBox checkboxAgentStatement = d.findViewById(R.id.checkboxAgentStatement);
        checkboxAgentStatement.setChecked(false);

        TextView tv_bi_smart_privilege_proposer_name = d
                .findViewById(R.id.tv_bi_smart_privilege_proposer_name);

        TextView tv_bi_smart_privilege_proposer_age = d
                .findViewById(R.id.tv_bi_smart_privilege_proposer_age);

        TextView tv_bi_smart_privilege_lifeassured_name = d
                .findViewById(R.id.tv_bi_smart_privilege_lifeassured_name);

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


        netYield8pa = prsObj.parseXmlTag(output, "netYield8pa");
        strProposerAge = prsObj.parseXmlTag(input, "proposer_age");

        if (!proposer_sign.equals("")) {
            String flg_needAnalyis = "";
            if (flg_needAnalyis.equals("1")) {
                tr_need_analysis.setVisibility(View.VISIBLE);
            } else {
                cb_statement_need_analysis.setChecked(true);
                tr_need_analysis.setVisibility(View.GONE);
            }
        }
        age_entry = prsObj.parseXmlTag(input, "age");
        if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {
            edt_proposer_name
                    .setText("I, "
                            + name_of_life_assured
                            + " have received the information with respect to the above and have understood the above statement before entering into a contract.");
            edt_proposer_name_need_analysis
                    .setText("I, "
                            + name_of_life_assured
                            + " have undergone the Need Analysis after having reviewed the SBI Life Product options, I have opted for SBI LIFE - Smart Wealth Builder.");

            // tv_life_assured_name.setText(name_of_life_assured);
            tv_proposername.setText(name_of_life_assured);
            tv_bi_smart_privilege_proposer_name.setText(name_of_life_assured);
            tv_bi_smart_privilege_lifeassured_name.setText(name_of_life_assured);
            tv_bi_smart_privilege_proposer_age.setText(age_entry+ " Years");
        } else if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("n")) {
            edt_proposer_name
                    .setText("I, "
                            + name_of_proposer
                            + " have received the information with respect to the above and have understood the above statement before entering into a contract.");
            edt_proposer_name_need_analysis
                    .setText("I, "
                            + name_of_proposer
                            + " have undergone the Need Analysis  after having reviewed the SBI Life Product options, I have opted for SBI LIFE - Smart Wealth Builder.");

            tv_proposername.setText(name_of_proposer);

            tv_bi_smart_privilege_proposer_name.setText(name_of_proposer);
            tv_bi_smart_privilege_lifeassured_name.setText(name_of_life_assured);
            tv_bi_smart_privilege_proposer_age.setText(strProposerAge + " Years");

            LinearLayout linearlayoutLifeAssuredSignature = d
                    .findViewById(R.id.linearlayoutLifeAssuredSignature);
            Ibtn_signatureofLifeAssured = d
                    .findViewById(R.id.Ibtn_signatureofLifeAssured);

            linearlayoutLifeAssuredSignature.setVisibility(View.GONE);
            if (proposerAsLifeAssuredSign != null
                    && !proposerAsLifeAssuredSign.equals("")) {
                byte[] signByteArray = Base64.decode(proposerAsLifeAssuredSign,
                        0);
                Bitmap bitmap = BitmapFactory.decodeByteArray(signByteArray, 0,
                        signByteArray.length);
                Ibtn_signatureofLifeAssured.setImageBitmap(bitmap);
            }

            Ibtn_signatureofLifeAssured
                    .setOnClickListener(new OnClickListener() {

                        public void onClick(View view) {

                            if (cb_statement.isChecked()
                                    && cb_statement_need_analysis.isChecked()) {
                                latestImage = "proposerSignOnLifeAssured";
                                commonMethods.windowmessageProposersgin(
                                        context, NeedAnalysisActivity.URN_NO
                                                + "_cust2sign");
                            } else {
                                commonMethods.dialogWarning(context, "Please Tick on I Agree Clause ", true);
                                setFocusable(cb_statement);
                                cb_statement.requestFocus();
                            }

                        }
                    });
        }

        TextView textviewAGentStatement = d.findViewById(R.id.textviewAGentStatement);
        textviewAGentStatement.setText(commonMethods.getAgentDeclaration(context));

        tv_proposal_number.setText(QuatationNumber);

        tv_smart_wealth_builder_distribution_channel.setText(userType);
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


        if (photoBitmap != null) {
            imageButtonProposerPhotograph.setImageBitmap(photoBitmap);
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

                            RadioGroup radioGroupDepositPayment = d
                                    .findViewById(R.id.radioGroupDepositPayment);
                            radioGroupDepositPayment.clearCheck();

                            RadioGroup radioGroupAppointee = d
                                    .findViewById(R.id.radioGroupAppointee);
                            radioGroupAppointee.clearCheck();

                            if (proposer_Is_Same_As_Life_Assured
                                    .equalsIgnoreCase("n")) {

                                String lifeassuredSignName = NeedAnalysisActivity.URN_NO
                                        + "_cust2sign.png";

                                File lifeassuredSignFile = mStorageUtils.createFileToAppSpecificDir(context,
                                        lifeassuredSignName);

                                if (lifeassuredSignFile.exists()) {
                                    lifeassuredSignFile.delete();
                                }

                                proposerAsLifeAssuredSign = "";
                                Ibtn_signatureofLifeAssured
                                        .setImageBitmap(null);
                            }
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

        if (transactionMode.equalsIgnoreCase("Manual")) {
            radioButtonTrasactionModeManual.setChecked(true);
        } else if (transactionMode.equalsIgnoreCase("Parivartan")) {
            radioButtonTrasactionModeParivartan.setChecked(true);
        }

        if (!TextUtils.isEmpty(place2)) {
            edt_Policyholderplace.setText(place2);
        }


        btn_proceed.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
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
										.isChecked() == true)
								&& ((radioButtonAppointeeYes.isChecked() == true && !appointeeSign
										.equals("")) || radioButtonAppointeeNo
										.isChecked() == true) && ((!proposerAsLifeAssuredSign
								.equals("") && proposer_Is_Same_As_Life_Assured
								.equalsIgnoreCase("n")) || proposer_Is_Same_As_Life_Assured
								.equalsIgnoreCase("y"))*/) && radioButtonTrasactionModeParivartan
                        .isChecked()) || radioButtonTrasactionModeManual
                        .isChecked())) {
                    NeedAnalysisActivity.str_need_analysis = "";

                    // String isActive = "0";
                    String productCode;
                    // 4 percent dialog
                    AnnulizedPremium = 0.0;
                    for (int i = 1; i <= Integer.parseInt(policyTerm); i++) {
                        String premium = prsObj.parseXmlTag(output, "AnnPrem" + i + "");
                        AnnulizedPremium = AnnulizedPremium + Double.parseDouble(premium);
                    }
                    policyTerm = policyTerm.replaceAll("\\s+", "");


                    if (smartWealthBuilderBean.getPlanType().equals("Regular"))
                        productCode = "UPS2RP0";
                    else
                        productCode = "UPS2LP0";

                    na_cbi_bean = new NA_CBI_bean(QuatationNumber, agentcode,
                            "", userType, "", lifeAssured_Title,
                            lifeAssured_First_Name, lifeAssured_Middle_Name,
                            lifeAssured_Last_Name, planName, obj
                            .getRound(sumAssured), obj
                            .getRound(premiumAmount), emailId,
                            mobileNo, agentEmail, agentMobile, na_input,
                            na_output, premFreqMode, Integer
                            .parseInt(policyTerm), 0, productCode,
                            getDate(lifeAssured_date_of_birth),
                            getDate(proposer_date_of_birth), inputVal
                            .toString(), retVal.toString().replace(
                            bussIll.toString(), ""));

                    name_of_person = name_of_life_assured;

                    if (radioButtonTrasactionModeParivartan.isChecked()) {
                        transactionMode = "Parivartan";
                    } else if (radioButtonTrasactionModeManual.isChecked()) {
                        transactionMode = "Manual";
                    }
                    dbHelper.AddNeedAnalysisDashboardDetails(new ProductBIBean(
                            "", QuatationNumber, planName, getCurrentDate(),
                            mobileNo, getCurrentDate(), dbHelper.GetUserCode(),
                            emailId, "", "", agentcode, "", userType, "",
                            lifeAssured_Title, lifeAssured_First_Name,
                            lifeAssured_Middle_Name, lifeAssured_Last_Name, obj
                            .getRound(sumAssured), obj
                            .getRound(premiumAmount), agentEmail,
                            agentMobile, na_input, na_output, premFreqMode,
                            Integer.parseInt(policyTerm), 0, productCode,
                            getDate(lifeAssured_date_of_birth),
                            getDate(proposer_date_of_birth), "", transactionMode, inputVal
                            .toString(), retVal.toString().replace(
                            bussIll.toString(), "")));

                    CreateSmartWealthBuilderBIPdf();

                    NABIObj.serviceHit(BI_SmartWealthBuilderActivity.this,
                            na_cbi_bean, newFile, needAnalysispath.getPath(),
                            mypath.getPath(), name_of_person, QuatationNumber, transactionMode);
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
                        setFocusable(checkboxAgentStatement);
                        checkboxAgentStatement.requestFocus();
                    } else if (!radioButtonTrasactionModeParivartan.isChecked()
                            && !radioButtonTrasactionModeManual.isChecked()) {
                        commonMethods.dialogWarning(context, "Please Select Transaction Mode", true);
                        setFocusable(linearlayoutTrasactionModeParivartan);
                        linearlayoutTrasactionModeParivartan.requestFocus();
                    } else if (photoBitmap == null) {
                        commonMethods.dialogWarning(context, "Please Capture the Photo", true);
                        setFocusable(imageButtonProposerPhotograph);
                        imageButtonProposerPhotograph.requestFocus();
                    } else if (proposer_Is_Same_As_Life_Assured
                            .equalsIgnoreCase("n")
                            && proposerAsLifeAssuredSign.equals("")) {
                        commonMethods.dialogWarning(context, "Please Make Signature for Life Assured ", true);
                        setFocusable(imageButtonProposerPhotograph);
                        imageButtonProposerPhotograph.requestFocus();
                    }
                }
            }
        });

        btn_PolicyholderDate.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {


                DIALOG_ID = 2;
                showDialog(DATE_DIALOG_ID);
            }
        });

        btn_MarketingOfficalDate.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                DIALOG_ID = 3;
                showDialog(DATE_DIALOG_ID);

            }
        });


        String staffdiscount = prsObj.parseXmlTag(input, "isStaff");

        if (staffdiscount.equalsIgnoreCase("true")) {
            // tr_staff_per.setVisibility(View.VISIBLE);
            tv_bi_is_Staff.setText("Yes");
            // tv_bi_flexi_smart_plus_staff_per.setText(staffdiscount_per);
        } else {

            // .setVisibility(View.GONE);
            tv_bi_is_Staff.setText("No");

        }

        age_entry = prsObj.parseXmlTag(input, "age");
        tv_bi_smart_wealth_builder_age_entry.setText(age_entry + " Years");

        String maturityAge = prsObj.parseXmlTag(output, "maturityAge");
        tv_bi_smart_wealth_builder_maturity_age.setText(maturityAge + " Years");
        // netYield8pa = prsObj.parseXmlTag(output, "netYield8pa");
        gender = prsObj.parseXmlTag(input, "gender");
        tv_bi_smart_wealth_builder_life_assured_gender.setText(gender);
        TextView tv_bi_smart_wealth_builder_premium_type = d
                .findViewById(R.id.tv_bi_smart_wealth_builder_premium_type);

        premPayTerm = prsObj.parseXmlTag(input, "premPayingTerm");
        tv_bi_smart_wealth_builder_premium_paying_term
                .setText(premPayTerm == null ? "" : premPayTerm + "Years");

        tv_smart_wealth_builder_net_yield.setText(netYield8pa + " %");

        redInYieldMat = prsObj.parseXmlTag(output, "redInYieldMat");

        tv_smart_wealth_builder_reduction_in_yield.setText(redInYieldMat + " %");

        planType = prsObj.parseXmlTag(input, "plan");

        /*
         * String payingTerm = ""; if (planType.equalsIgnoreCase("single")) {
         * payingTerm = "1 Year"; } else
         * if(planType.equalsIgnoreCase("Regular")) { payingTerm = policyTerm+
         * " Years"; } else { payingTerm = premPayingTerm + " Years"; }
         */

        // if (planType.equals("Single")) {
        // tv_bi_smart_wealth_builder_premium_type.setText("Single Premium");
        // tv_bi_smart_wealth_builder_premium_paying_term.setText(" 1 "
        // + "Year");
        //
        // }

        annPrem = prsObj.parseXmlTag(output, "annPrem");

        tv_bi_smart_wealth_builder_annualised_premium.setText("Rs."
                + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf((annPrem
                .equals("") || annPrem == null) ? "0" : annPrem))))));

        tv_bi_smart_wealth_builder_annualised_premium_yearly.setText("Rs."
                + obj.getRound(obj.getStringWithout_E(Double.valueOf((annPrem
                .equals("") || annPrem == null) ? "0" : annPrem))));

        sumAssured = prsObj.parseXmlTag(output, "sumAssured");
        tv_smart_wealth_builder_death_benefit.setText("Rs."
                + obj.getRound(obj.getStringWithout_E(Double.valueOf(sumAssured
                .equals("") ? "0" : sumAssured))));

        tv_bi_smart_wealth_builder_sum_assured_main.setText("Rs. "
                + getformatedThousandString(Integer.parseInt(obj.getRound(obj
                .getStringWithout_E(Double.valueOf(sumAssured
                        .equals("") ? "0" : sumAssured))))));

        String mode_of_policy = prsObj.parseXmlTag(output, "mode");
        tv_bi_smart_wealth_builder_mode.setText(mode_of_policy);

        // premPayTerm = prsObj.parseXmlTag(output, "premPayTerm");

        perInvEquityFund = (prsObj.parseXmlTag(input, "perInvEquityFund"));
        perInvEquityOptimiserFund = (prsObj.parseXmlTag(input,
                "perInvEquityOptimiserFund"));
        perInvgrowthFund = (prsObj.parseXmlTag(input, "perInvgrowthFund"));
        perInvBalancedFund = (prsObj.parseXmlTag(input, "perInvBalancedFund"));
        perInvBondFund = (prsObj.parseXmlTag(input, "perInvBondFund"));
        perInvMoneyMarketFund = (prsObj.parseXmlTag(input,
                "perInvMoneyMarketFund"));
        perInvTop300Fund = (prsObj.parseXmlTag(input, "perInvTop300Fund"));


        perInvBondOptimiserFund = (prsObj.parseXmlTag(input, "perInvBondOptimiserFund"));

        perInvCorporateBondFund = (prsObj.parseXmlTag(input, "perInvCorporateBondFund"));

        perInvMidcapFund = (prsObj.parseXmlTag(input, "perInvMidcapFund"));

        perInvPureFund = (prsObj.parseXmlTag(input, "perInvPureFund"));


        tv_smart_wealth_builder_equity_fund_allocation
                .setText((perInvEquityFund.equals("") ? "0" : perInvEquityFund)
                        + " %");

        tv_smart_wealth_builder_equity_optimiser_fund_allocation
                .setText((perInvEquityOptimiserFund.equals("") ? "0"
                        : perInvEquityOptimiserFund) + " %");
        tv_smart_wealth_builder_growth_fund_allocation
                .setText((perInvgrowthFund.equals("") ? "0" : perInvgrowthFund)
                        + " %");
        tv_smart_wealth_builder_balanced_fund_allocation
                .setText((perInvBalancedFund.equals("") ? "0"
                        : perInvBalancedFund) + " %");
        tv_smart_wealth_builder_bond_fund_allocation.setText((perInvBondFund
                .equals("") ? "0" : perInvBondFund) + " %");
        tv_smart_wealth_builder_market_fund_allocation
                .setText((perInvMoneyMarketFund.equals("") ? "0"
                        : perInvMoneyMarketFund) + " %");
        tv_smart_wealth_builder_top_300_fund_allocation
                .setText((perInvTop300Fund.equals("") ? "0" : perInvTop300Fund)
                        + " %");


        tv_smart_wealth_builder_bond_optimiser_fund_allocation
                .setText((perInvBondOptimiserFund.equals("") ? "0" : perInvBondOptimiserFund)
                        + " %");

        tv_smart_wealth_builder_corporate_bond_fund_allocation
                .setText((perInvCorporateBondFund.equals("") ? "0" : perInvCorporateBondFund)
                        + " %");

        tv_smart_wealth_builder_midcap_fund_allocation
                .setText((perInvMidcapFund.equals("") ? "0" : perInvMidcapFund)
                        + " %");

        tv_smart_wealth_builder_pure_fund_allocation
                .setText((perInvPureFund.equals("") ? "0" : perInvPureFund)
                        + " %");


        tv_smart_wealth_builder_equity_fund_fmc.setText("1.35 %");
        tv_smart_wealth_builder_equity_optimiser_fund_fmc.setText("1.35 %");
        tv_smart_wealth_builder_growth_fund_fmc.setText("1.35 %");
        tv_smart_wealth_builder_balanced_fund_fmc.setText("1.25 %");
        tv_smart_wealth_builder_bond_fund_fmc.setText("1.00 %");
        tv_smart_elite_market_fund_fmc.setText("0.25 %");
        tv_smart_wealth_builder_top_300_fund_fmc.setText("1.35 %");

        tv_smart_wealth_builder_bond_optimiser_fund_fmc.setText("1.15 %");
        tv_smart_wealth_builder_corporate_bond_fund_fmc.setText("1.15 %");
        tv_smart_wealth_builder_midcap_fund_fmc.setText("1.35 %");
        tv_smart_wealth_builder_pure_fund_fmc.setText("1.35 %");

        noOfYrElapsed = prsObj.parseXmlTag(input, "noOfYrElapsed");
        tv_smart_wealth_builder_no_of_years_elapsed.setText(noOfYrElapsed
                + " Years");

        redInYieldNoYr = prsObj.parseXmlTag(output, "redInYieldNoYr");
        tv_smart_wealth_builder_reduction_yield.setText(redInYieldNoYr + " %");

        policyTerm = prsObj.parseXmlTag(input, "policyTerm");
        tv_smart_wealth_builder_maturity_at.setText(policyTerm + " Years");
        tv_bi_smart_wealth_builder_policy_term.setText(policyTerm + " Years");


        String str_prem_paying_term = "";

        if (planType.equals("Single")) {
            tv_bi_smart_wealth_builder_premium_type.setText("Amount of Installment Premium");
            tv_bi_smart_wealth_builder_premium_paying_term.setText(" 1 "
                    + "Year");
            str_prem_paying_term = "1";

            tv_bi_smart_wealth_builder_premium_type_yearly.setText("Amount of Installment Premium");
            tv_ppt_builder.setText("One time at the inception of the policy");
            tv_bi_smart_wealth_builder_mode.setText("One time at the inception of the policy");

            //tv_premium_heading.setText("Single Premium");

        } else if (planType.equals("Regular")) {
            tv_bi_smart_wealth_builder_premium_paying_term.setText(policyTerm
                    + "Years");

            str_prem_paying_term = policyTerm;
            //tv_premium_heading.setText("Annualised Premium");
            tv_ppt_builder.setText("Same as Policy Term");
            tv_bi_smart_wealth_builder_mode.setText("Yearly");

        } else if (planType.equals("Limited")) {
            tv_bi_smart_wealth_builder_premium_paying_term.setText(premPayTerm
                    + "Years");
            str_prem_paying_term = premPayTerm;
            //tv_premium_heading.setText("Annualised Premium");
            tv_ppt_builder.setText(str_prem_paying_term);
            tv_bi_smart_wealth_builder_mode.setText("Yearly");

        }


        tv_smart_wealth_builder_reduction_yeild2.setText(redInYieldMat + " %");

        TextView tv_Company_policy_surrender_dec = d
                .findViewById(R.id.tv_Company_policy_surrender_dec);


        if (planType.equalsIgnoreCase("Single")) {
            Company_policy_surrender_dec = "Your SBI LIFE - Smart Wealth Builder (111L095V03) is a "
                    + planType
                    + " Premium policy and you are required to pay premium once at the inception of the policy of Rs. "
                    + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf(annPrem)))))
                    + " .Your Policy Term is "
                    + policyTerm
                    + "years, Premium Payment term is Not Applicable and Sum Assured is Rs. "
                    + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf(sumAssured
                    .equals("") ? "0" : sumAssured)))));
        } else if (planType.equalsIgnoreCase("Regular")) {
            Company_policy_surrender_dec = "Your SBI LIFE - Smart Wealth Builder(111L095V03) is a "
                    + planType
                    + " Premium Policy and you are required to  pay yearly premium of Rs "
                    + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf(annPrem)))))
                    + " .Your Policy Term is "
                    + policyTerm
                    + " years, Premium Payment Term is same as policy term and Sum Assured is Rs. "
                    + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf(sumAssured
                    .equals("") ? "0" : sumAssured)))));
        } else if (planType.equalsIgnoreCase("Limited")) {
            Company_policy_surrender_dec = "Your SBI LIFE - Smart Wealth Builder(111L095V03) is a "
                    + planType
                    + " Premium Policy and you are required to pay yearly premium  of Rs "
                    + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf(annPrem)))))
                    + " .Your Policy Term is "
                    + policyTerm
                    + " years"
                    + " , Premium Payment Term is "
                    + str_prem_paying_term
                    + " years"
                    + " and Sum Assured is Rs. "
                    + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf(sumAssured
                    .equals("") ? "0" : sumAssured)))));
        }

        tv_Company_policy_surrender_dec.setText(Company_policy_surrender_dec);


        for (int i = 1; i <= Integer.parseInt(policyTerm); i++) {
            String policy_year = prsObj
                    .parseXmlTag(output, "policyYr" + i + "");
            String premium = obj.getRound(obj.getStringWithout_E(Double.valueOf(prsObj.parseXmlTag(output, "AnnPrem" + i + ""))));

            String mortality_charge1 = obj.getRound(obj.getStringWithout_E(Double.valueOf(prsObj.parseXmlTag(output, "MortChrg4Pr"
                    + i + ""))));
            String OtherCharges4Pr_PartA = obj.getRound(obj.getStringWithout_E(Double.valueOf(prsObj.parseXmlTag(output, "OtherCharges4Pr_PartA" + i
                    + ""))));
            String OtherCharges4Pr_PartB = obj.getRound(obj.getStringWithout_E(Double.valueOf(prsObj.parseXmlTag(output, "OtherCharges4Pr_PartB" + i
                    + ""))));
            String service_tax_on_mortality_charge1 = obj.getRound(obj.getStringWithout_E(Double.valueOf(prsObj.parseXmlTag(
                    output, "TotServTax4Pr" + i + ""))));

            String fund_value_at_end1 = obj.getRound(obj.getStringWithout_E(Double.valueOf(prsObj.parseXmlTag(output,
                    "FundValAtEnd4Pr" + i + ""))));
            String surrender_value1 = obj.getRound(obj.getStringWithout_E(Double.valueOf(prsObj.parseXmlTag(output, "SurrVal4Pr"
                    + i + ""))));
            String death_benefit1 = obj.getRound(obj.getStringWithout_E(Double.valueOf(prsObj.parseXmlTag(output, "DeathBen4Pr"
                    + i + ""))));


            String mortality_charge2 = obj.getRound(obj.getStringWithout_E(Double.valueOf(prsObj.parseXmlTag(output, "MortChrg8Pr"
                    + i + ""))));
            String OtherCharges8Pr_PartA = obj.getRound(obj.getStringWithout_E(Double.valueOf(prsObj.parseXmlTag(output, "OtherCharges8Pr_PartA" + i
                    + ""))));
            String OtherCharges8Pr_PartB = obj.getRound(obj.getStringWithout_E(Double.valueOf(prsObj.parseXmlTag(output, "OtherCharges8Pr_PartB" + i
                    + ""))));
            String service_tax_on_mortality_charge2 = obj.getRound(obj.getStringWithout_E(Double.valueOf(prsObj.parseXmlTag(
                    output, "TotServTxOnCharg8Pr" + i + ""))));

            String fund_value_at_end2 = obj.getRound(obj.getStringWithout_E(Double.valueOf(prsObj.parseXmlTag(output,
                    "FundValAtEnd8Pr" + i + ""))));
            String surrender_value2 = obj.getRound(obj.getStringWithout_E(Double.valueOf(prsObj.parseXmlTag(output, "SurrVal8Pr"
                    + i + ""))));
            String death_benefit2 = obj.getRound(obj.getStringWithout_E(Double.valueOf(prsObj.parseXmlTag(output, "DeathBen8Pr"
                    + i + ""))));

            String premium_allocation_charge = obj.getRound(obj.getStringWithout_E(Double.valueOf(prsObj.parseXmlTag(output,
                    "PremAllCharge" + i + ""))));

            String annulise_premium_allocation_charge = obj.getRound(obj.getStringWithout_E(Double.valueOf(prsObj.parseXmlTag(output,
                    "AmtAviFrInv" + i + ""))));

            String policy_administration_charge = obj.getRound(obj.getStringWithout_E(Double.valueOf(prsObj.parseXmlTag(output,
                    "PolAdminChrg" + i + ""))));

            String str_AddToTheFund8Pr = obj.getRound(obj.getStringWithout_E(Double.valueOf(prsObj.parseXmlTag(
                    output, "AddToTheFund8Pr" + i + ""))));

            String fund_before_fmc2 = obj.getRound(obj.getStringWithout_E(Double.valueOf(prsObj.parseXmlTag(output,
                    "FundBefFMC8Pr" + i + ""))));
            String fund_management_charge2 = obj.getRound(obj.getStringWithout_E(Double.valueOf(prsObj.parseXmlTag(output,
                    "FundMgmtCharg8Pr" + i + ""))));
            String guranteed_addition2 = obj.getRound(obj.getStringWithout_E(Double.valueOf(prsObj.parseXmlTag(output,
                    "GuarntAdd8Pr" + i + ""))));

            String commission = obj.getRound(obj.getStringWithout_E(Double.valueOf(prsObj.parseXmlTag(output, "CommIfPay8Pr" + i
                    + ""))));

            String str_AddToTheFund4Pr = obj.getRound(obj.getStringWithout_E(Double.valueOf(prsObj.parseXmlTag(
                    output, "AddToTheFund4Pr" + i + ""))));

            String fund_before_fmc1 = obj.getRound(obj.getStringWithout_E(Double.valueOf(prsObj.parseXmlTag(output,
                    "FundBefFMC4Pr" + i + ""))));
            String fund_management_charge1 = obj.getRound(obj.getStringWithout_E(Double.valueOf(prsObj.parseXmlTag(output,
                    "FundMgmtCharg4Pr" + i + ""))));
            String guranteed_addition1 = obj.getRound(obj.getStringWithout_E(Double.valueOf(prsObj.parseXmlTag(output,
                    "GuarntAdd4Pr" + i + ""))));


            list_data.add(new M_BI_SmartWealthBuilderAdapterCommon(policy_year, premium, mortality_charge1, OtherCharges4Pr_PartA,
                    service_tax_on_mortality_charge1, fund_value_at_end1, surrender_value1, death_benefit1,
                    mortality_charge2, OtherCharges8Pr_PartA, service_tax_on_mortality_charge2, fund_value_at_end2,
                    surrender_value2, death_benefit2, commission));


            list_data1.add(new M_BI_SmartWealthBuilderAdapterCommon2(policy_year, premium, premium_allocation_charge,
                    annulise_premium_allocation_charge, mortality_charge2, service_tax_on_mortality_charge2, policy_administration_charge,
                    guranteed_addition2, OtherCharges8Pr_PartB, str_AddToTheFund8Pr, fund_before_fmc2, fund_management_charge2,
                    fund_value_at_end2, surrender_value2, death_benefit2));
            list_data2.add(new M_BI_SmartWealthBuilderAdapterCommon2(policy_year, premium, premium_allocation_charge,
                    annulise_premium_allocation_charge, mortality_charge1, service_tax_on_mortality_charge1, policy_administration_charge,
                    guranteed_addition1, OtherCharges4Pr_PartB, str_AddToTheFund4Pr, fund_before_fmc1, fund_management_charge1,
                    fund_value_at_end1, surrender_value1, death_benefit1));





        /*    list_data
                    .add(new M_BI_SmartWealthBuilderAdapterCommon(
                            policy_year,
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf((premium.equals("") || premium == null) ? "0"
                                            : premium)))
                                    + "",
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf((mortality_charge1.equals("") || mortality_charge1 == null) ? "0"
                                            : mortality_charge1)))
                                    + "",
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf((total_charge1.equals("") || total_charge1 == null) ? "0"
                                            : total_charge1)))
                                    + "",
                            obj.getRound(obj.getStringWithout_E(Double.valueOf((service_tax_on_mortality_charge1
                                    .equals("") || service_tax_on_mortality_charge1 == null) ? "0"
                                    : service_tax_on_mortality_charge1)))
                                    + "",
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf((fund_value_at_end1.equals("") || fund_value_at_end1 == null) ? "0"
                                            : fund_value_at_end1)))
                                    + "",
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf((surrender_value1.equals("") || surrender_value1 == null) ? "0"
                                            : surrender_value1)))
                                    + "",
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf((death_benefit1.equals("") || death_benefit1 == null) ? "0"
                                            : death_benefit1)))
                                    + "",
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf((mortality_charge2.equals("") || mortality_charge2 == null) ? "0"
                                            : mortality_charge2)))
                                    + "",
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf((total_charge2.equals("") || total_charge2 == null) ? "0"
                                            : total_charge2)))
                                    + "",
                            obj.getRound(obj.getStringWithout_E(Double.valueOf((service_tax_on_mortality_charge2
                                    .equals("") || service_tax_on_mortality_charge2 == null) ? "0"
                                    : service_tax_on_mortality_charge2)))
                                    + "",
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf((fund_value_at_end2.equals("") || fund_value_at_end2 == null) ? "0"
                                            : fund_value_at_end2)))
                                    + "",

                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf((surrender_value2.equals("") || surrender_value2 == null) ? "0"
                                            : surrender_value2)))
                                    + "",
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf((death_benefit2.equals("") || death_benefit2 == null) ? "0"
                                            : death_benefit2)))
                                    + "",

                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf((commission.equals("") || commission == null) ? "0"
                                            : commission)))
                                    + ""));*/

        }

        for (int i = 1; i <= Integer.parseInt(policyTerm); i++) {
            String policy_year = prsObj
                    .parseXmlTag(output, "policyYr" + i + "");
            String premium = prsObj.parseXmlTag(output, "AnnPrem" + i + "");
            String premium_allocation_charge = prsObj.parseXmlTag(output,
                    "PremAllCharge" + i + "");
            String policy_administration_charge = prsObj.parseXmlTag(output,
                    "PolAdminChrg" + i + "");

            String mortality_charge2 = prsObj.parseXmlTag(output, "MortChrg8Pr"
                    + i + "");
            String total_charge2 = prsObj.parseXmlTag(output, "TotCharg8Pr" + i
                    + "");
            String service_tax_on_mortality_charge2 = prsObj.parseXmlTag(
                    output, "TotServTxOnCharg8Pr" + i + "");

            String str_AddToTheFund8Pr = prsObj.parseXmlTag(
                    output, "AddToTheFund8Pr" + i + "");

            String fund_before_fmc2 = prsObj.parseXmlTag(output,
                    "FundBefFMC8Pr" + i + "");
            String fund_management_charge2 = prsObj.parseXmlTag(output,
                    "FundMgmtCharg8Pr" + i + "");
            String guranteed_addition2 = prsObj.parseXmlTag(output,
                    "GuarntAdd8Pr" + i + "");
            String fund_value_at_end2 = prsObj.parseXmlTag(output,
                    "FundValAtEnd8Pr" + i + "");
            String surrender_value2 = prsObj.parseXmlTag(output, "SurrVal8Pr"
                    + i + "");
            String death_benefit2 = prsObj.parseXmlTag(output, "DeathBen8Pr"
                    + i + "");

            String commission = prsObj.parseXmlTag(output, "CommIfPay8Pr" + i
                    + "");

        /*  list_data1
                    .add(new M_BI_SmartWealthBuilderAdapterCommon2(
                            policy_year,
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf((premium.equals("") || premium == null) ? "0"
                                            : premium)))
                                    + "",
                            obj.getRound(obj.getStringWithout_E(Double.valueOf((premium_allocation_charge
                                    .equals("") || premium_allocation_charge == null) ? "0"
                                    : premium_allocation_charge)))
                                    + "",
                            obj.getRound(obj.getStringWithout_E(Double.valueOf((premium_allocation_charge
                                    .equals("") || premium_allocation_charge == null) ? "0"
                                    : premium_allocation_charge)))
                                    + "",
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf((amount_for_investment.equals("") || amount_for_investment == null) ? "0"
                                            : amount_for_investment)))
                                    + "",
                            obj.getRound(obj.getStringWithout_E(Double.valueOf((policy_administration_charge
                                    .equals("") || policy_administration_charge == null) ? "0"
                                    : policy_administration_charge)))
                                    + "",
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf((mortality_charge2.equals("") || mortality_charge2 == null) ? "0"
                                            : mortality_charge2)))
                                    + "",
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf((total_charge2.equals("") || total_charge2 == null) ? "0"
                                            : total_charge2)))
                                    + "",
                            obj.getRound(obj.getStringWithout_E(Double.valueOf((service_tax_on_mortality_charge2
                                    .equals("") || service_tax_on_mortality_charge2 == null) ? "0"
                                    : service_tax_on_mortality_charge2)))
                                    + "",
                            obj.getRound(obj.getStringWithout_E(Double.valueOf((str_AddToTheFund8Pr
                                    .equals("") || str_AddToTheFund8Pr == null) ? "0"
                                    : str_AddToTheFund8Pr)))
                                    + "",
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf((fund_before_fmc2.equals("") || fund_before_fmc2 == null) ? "0"
                                            : fund_before_fmc2)))
                                    + "",
                            obj.getRound(obj.getStringWithout_E(Double.valueOf((fund_management_charge2
                                    .equals("") || fund_management_charge2 == null) ? "0"
                                    : fund_management_charge2)))
                                    + "",
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf((guranteed_addition2.equals("") || guranteed_addition2 == null) ? "0"
                                            : guranteed_addition2)))
                                    + "",
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf((fund_value_at_end2.equals("") || fund_value_at_end2 == null) ? "0"
                                            : fund_value_at_end2)))
                                    + "",
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf((surrender_value2.equals("") || surrender_value2 == null) ? "0"
                                            : surrender_value2)))
                                    + "",
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf((death_benefit2.equals("") || death_benefit2 == null) ? "0"
                                            : death_benefit2)))
                                    + "",
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf((commission.equals("") || commission == null) ? "0"
                                            : commission)))
                                    + ""));

        */
        }
        // tv_smart_wealth_builder_death_benefit.setText("Rs." + )
        policyTerm = policyTerm.replaceAll("\\s+", "");
        Adapter_BI_SmartWealthBuilderGridCommon adapter = new Adapter_BI_SmartWealthBuilderGridCommon(
                BI_SmartWealthBuilderActivity.this, list_data);
        gv_userinfo.setAdapter(adapter);
        Adapter_BI_SmartWealthBuilderGridCommon2 adapter1 = new Adapter_BI_SmartWealthBuilderGridCommon2(
                BI_SmartWealthBuilderActivity.this, list_data1);
        gv_userinfo2.setAdapter(adapter1);

        Adapter_BI_SmartWealthBuilderGridCommon2 adapter2 = new Adapter_BI_SmartWealthBuilderGridCommon2(
                BI_SmartWealthBuilderActivity.this, list_data2);
        gv_userinfo3.setAdapter(adapter2);


        GridHeight gh = new GridHeight();
        gh.getheight(gv_userinfo, policyTerm);
        gh.getheight(gv_userinfo2, policyTerm);
        gh.getheight(gv_userinfo3, policyTerm);


        d.show();

    }

    // FOr Date Dialog Box

    public void onClickDob(View v) {
        initialiseDateParameter(proposer_date_of_birth, 35);
        DIALOG_ID = 4;
        showDialog(DATE_DIALOG_ID);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DATE_DIALOG_ID) {
            return new DatePickerDialog(this, R.style.datepickerstyle,
                    mDateSetListener, mDay, mMonth, mYear);
        }
        return null;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        if (id == DATE_DIALOG_ID) {
            ((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
        }
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
                    String strProposerAge = final_age;
                    final_age = Integer.toString(age);
                    if (final_age.contains("-")) {
                        commonMethods.BICommonDialog(context, "Please fill Valid Birth Date");
                    } else {
                        if (18 <= age && age <= 100) {

                            btn_bi_smart_wealth_builder_proposer_date.setText(date);
                            textviewProposerAge
                                    .setText(final_age);
                            proposer_date_of_birth = getDate1(date + "");

                            clearFocusable(btn_bi_smart_wealth_builder_proposer_date);

                            /*
                             * setFocusable(selPlan); selPlan.requestFocus();
                             */

                            setFocusable(edt_proposerdetail_basicdetail_contact_no);
                            edt_proposerdetail_basicdetail_contact_no
                                    .requestFocus();

                            // ageInYears
                            // .setSelection(getIndex(ageInYears, final_age));
                            // updatePolicyTermLabel();
                            // updateSAMFlabel();
                        } else {
                            commonMethods.BICommonDialog(context, "Minimum Age should be 18yrs For Proposer");
                            btn_bi_smart_wealth_builder_proposer_date
                                    .setText("Select Date");
                            textviewProposerAge.setText("");
                            proposer_date_of_birth = "";
                            clearFocusable(btn_bi_smart_wealth_builder_proposer_date);
                            setFocusable(btn_bi_smart_wealth_builder_proposer_date);
                            btn_bi_smart_wealth_builder_proposer_date
                                    .requestFocus();

                        }
                    }
                    break;

                case 5:
                    String lifeAssuredAge = final_age;
                    final_age = Integer.toString(age);
                    if (final_age.contains("-")) {
                        commonMethods.BICommonDialog(context, "Please fill Valid Birth Date");
                    } else {

                        if ((selPlan.getSelectedItem().toString().equals("Limited") || selPlan
                                .getSelectedItem().toString().equals("Regular"))) {
                            if (2 <= age && age <= 55) {

                                btn_bi_smart_wealth_builder_life_assured_date
                                        .setText(date);
                                edt_bi_smart_wealth_builder_life_assured_age
                                        .setText(final_age);

                                if (Integer.parseInt(final_age) < 18) {
                                    linearlayoutProposerDetails
                                            .setVisibility(View.VISIBLE);
                                    proposer_Is_Same_As_Life_Assured = "n";

                                    clearFocusable(btn_bi_smart_wealth_builder_life_assured_date);

                                    /*
                                     * setFocusable(selPlan);
                                     * selPlan.requestFocus();
                                     */

                                    setFocusable(edt_proposerdetail_basicdetail_contact_no);
                                    edt_proposerdetail_basicdetail_contact_no
                                            .requestFocus();
                                } else {
                                    linearlayoutProposerDetails
                                            .setVisibility(View.GONE);
                                    proposer_Is_Same_As_Life_Assured = "y";

                                    clearFocusable(btn_bi_smart_wealth_builder_life_assured_date);
                                }

                                ageInYears.setSelection(
                                        getIndex(ageInYears, final_age), false);
                                valMaturityAge();
                                updatePolicyTermLabel();
                                updateSAMFlabel();
                                lifeAssured_date_of_birth = getDate1(date + "");

                            } else {
                                commonMethods.BICommonDialog(context, "Minimum Age should be 2 yrs and Maximum Age should be 55 yrs For LifeAssured");
                                btn_bi_smart_wealth_builder_life_assured_date
                                        .setText("Select Date");
                                edt_bi_smart_wealth_builder_life_assured_age
                                        .setText("");
                                lifeAssured_date_of_birth = "";
                                clearFocusable(btn_bi_smart_wealth_builder_life_assured_date);
                                setFocusable(btn_bi_smart_wealth_builder_life_assured_date);
                                btn_bi_smart_wealth_builder_life_assured_date
                                        .requestFocus();
                            }
                        } else if (selPlan.getSelectedItem().toString()
                                .equals("Single")) {
                            if (2 <= age && age <= 55) {

                                btn_bi_smart_wealth_builder_life_assured_date
                                        .setText(date);
                                edt_bi_smart_wealth_builder_life_assured_age
                                        .setText(final_age);

                                if (Integer.parseInt(final_age) < 18) {
                                    linearlayoutProposerDetails
                                            .setVisibility(View.VISIBLE);
                                    proposer_Is_Same_As_Life_Assured = "n";
                                    clearFocusable(btn_bi_smart_wealth_builder_life_assured_date);
                                    setFocusable(spnr_bi_smart_wealth_builder_proposer_title);
                                    spnr_bi_smart_wealth_builder_proposer_title
                                            .requestFocus();


                                } else {
                                    linearlayoutProposerDetails
                                            .setVisibility(View.GONE);
                                    proposer_Is_Same_As_Life_Assured = "y";
                                    clearFocusable(btn_bi_smart_wealth_builder_life_assured_date);

                                    /*
                                     * setFocusable(selPlan);
                                     * selPlan.requestFocus();
                                     */

                                    setFocusable(edt_proposerdetail_basicdetail_contact_no);
                                    edt_proposerdetail_basicdetail_contact_no
                                            .requestFocus();


                                }

                                ageInYears.setSelection(
                                        getIndex(ageInYears, final_age), false);
                                valMaturityAge();
                                updatePolicyTermLabel();
                                updateSAMFlabel();
                                lifeAssured_date_of_birth = getDate1(date + "");

                            } else {
                                commonMethods.BICommonDialog(context, "Minimum Age should be 2 yrs and Maximum Age should be 55 yrs For LifeAssured");
                                btn_bi_smart_wealth_builder_life_assured_date
                                        .setText("Select Date");
                                edt_bi_smart_wealth_builder_life_assured_age
                                        .setText("");
                                lifeAssured_date_of_birth = "";
                                clearFocusable(btn_bi_smart_wealth_builder_life_assured_date);
                                setFocusable(btn_bi_smart_wealth_builder_life_assured_date);
                                btn_bi_smart_wealth_builder_life_assured_date
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
        showDialog(DATE_DIALOG_ID);
    }

    public void onClickProposerDob(View v) {
        initialiseDateParameter(proposer_date_of_birth, 35);
        DIALOG_ID = 4;
        showDialog(DATE_DIALOG_ID);
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


    private void CreateSmartWealthBuilderBIPdf() {

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
            needAnalysispath = mStorageUtils.createFileToAppSpecificDir(context, "NA.pdf");
            // needAnalysis_Tabular_path = new File(folder, "NA_Tabular.pdf");
            newFile = mStorageUtils.createFileToAppSpecificDir(context, QuatationNumber + "P01.pdf");

            Rectangle rect = new Rectangle(594f, 792f);

            Document document = new Document(rect, 50, 50, 50, 50);
            // Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            @SuppressWarnings("unused")
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
            // c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            c1.setPadding(5);
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            headertable.addCell(c1);
            headertable.setHorizontalAlignment(Element.ALIGN_CENTER);
            Paragraph para_address = new Paragraph(
                    "SBI Life Insurance Co. Ltd",
                    small_bold);
            para_address.setAlignment(Element.ALIGN_CENTER);
            Paragraph para_address2 = new Paragraph(
                    "Corporate Office: 'Natraj', M.V.Road and Western Express Highway Junction, Andheri (East), Mumbai - 400069 ",
                    small_bold);
            para_address2.setAlignment(Element.ALIGN_CENTER);
            Paragraph para_address3 = new Paragraph(
                    "IRDAI Registration No. 111 | Website: www.sbilife.co.in | Email: info@sbilife.co.in | CIN: L99999MH2000PLC129113",
                    small_bold);
            para_address3.setAlignment(Element.ALIGN_CENTER);
            Paragraph para_address4 = new Paragraph(
                    "Toll Free: 1800 267 9090 (Between 9.00 am & 9.00 pm) ",
                    small_bold);
            para_address4.setAlignment(Element.ALIGN_CENTER);
            Paragraph para_address5 = new Paragraph(
                    "Customised Benefit Illustration (CBI)",
                    small_bold);
            para_address5.setAlignment(Element.ALIGN_CENTER);
            Paragraph para_address6 = new Paragraph(
                    "SBI Life - Smart Wealth Builder (111L095V03)",
                    small_bold);
            para_address6.setAlignment(Element.ALIGN_CENTER);
            Paragraph para_address7 = new Paragraph(
                    "An Individual, Unit-linked, Non-Participating, Life Insurance Product",
                    small_bold);
            para_address7.setAlignment(Element.ALIGN_CENTER);
            Paragraph para_address8 = new Paragraph(
                    "IN THIS POLICY, THE INVESTMENT RISK IN INVESTMENT PORTFOLIO IS BORNE BY THE POLICYHOLDER. ",
                    small_bold);
            para_address8.setAlignment(Element.ALIGN_CENTER);
            document.add(para_address);
            document.add(para_address2);
            document.add(para_address3);
            document.add(para_address4);
            document.add(para_address5);
            document.add(para_address6);
            document.add(para_address7);
            document.add(para_address8);
            document.add(para_img_logo_after_space_1);
            //document.add(headertable);
            // document.add(para_img_logo_after_space_1);
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
                    "Channel / Intermediary :", small_normal));
            PdfPCell NameofProposal_cell_4 = new PdfPCell(new Paragraph(
                    userType, small_bold1));
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
            //  document.add(BI_Pdftable4);


            PdfPTable BI_Pdftable6 = new PdfPTable(1);
            BI_Pdftable6.setWidthPercentage(100);
            PdfPCell BI_Pdftable6_cell6 = new PdfPCell(
                    new Paragraph(
                            "1)	Kindly note that this is an illustration only and does not in any way create any rights and/or obligations. The actual experience on the contract may be different from what is illustrated. The non-guaranteed low and high rate mentioned  relate to assumed investment returns at different rates and may vary depending upon market conditions. For more details on risk factors, terms and conditions please read sales brochure carefully before concluding purchase.",

                            small_normal));

            BI_Pdftable6_cell6.setPadding(5);

            BI_Pdftable6.addCell(BI_Pdftable6_cell6);
            //document.add(BI_Pdftable6);

            PdfPTable BI_Pdftable7 = new PdfPTable(1);
            BI_Pdftable7.setWidthPercentage(100);
            PdfPCell BI_Pdftable7_cell1 = new PdfPCell(
                    new Paragraph(
                            "2)The unit values may go up as well as down and past performance is no indication of future performance on the part of SBI Life Insurance Co. Ltd. We would request you to appreciate the associated risk under this plan vis-à-vis the likely future returns before taking your investment decision.",

                            small_normal));

            BI_Pdftable7_cell1.setPadding(5);

            BI_Pdftable7.addCell(BI_Pdftable7_cell1);
            //document.add(BI_Pdftable7);

            PdfPTable BI_Pdftable8 = new PdfPTable(1);
            BI_Pdftable8.setWidthPercentage(100);
            PdfPCell BI_Pdftable8_cell1 = new PdfPCell(
                    new Paragraph(
                            "3)	Please read this benefit illustration in conjunction with Sales Brochure and the Policy Document to understand all Terms, Conditions & Exclusions carefully.",

                            small_normal));

            BI_Pdftable8_cell1.setPadding(5);

            BI_Pdftable8.addCell(BI_Pdftable8_cell1);
            //document.add(BI_Pdftable8);

            PdfPTable BI_Pdftable9 = new PdfPTable(1);
            BI_Pdftable9.setWidthPercentage(100);
            PdfPCell BI_Pdftable9_cell1 = new PdfPCell(
                    new Paragraph(
                            "4)	This illustration has been prepared in compliance with the Insurance Regulatory and Development Authority of India (Unit Linked Insurance Products) Regulations, 2019",

                            small_normal));

            BI_Pdftable9_cell1.setPadding(5);

            BI_Pdftable9.addCell(BI_Pdftable9_cell1);
            //  document.add(BI_Pdftable9);

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

            String isStaff;
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

           /* if (userType.equalsIgnoreCase("BAP") || userType.equalsIgnoreCase("CAG")) {

            } else {

            }*/
            document.add(table_staff_disccount);

            document.add(para_img_logo_after_space_1);

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

            String str_proposer_name = "";
            String str_proposer_age = "";
            age_entry = prsObj.parseXmlTag(input, "age");

            if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {
                str_proposer_name = name_of_life_assured;
                str_proposer_age = age_entry;
            } else if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("n")) {
                str_proposer_name = name_of_proposer;
                str_proposer_age = strProposerAge;
            }

            PdfPTable table_lifeAssuredDetails1 = new PdfPTable(4);
            table_lifeAssuredDetails1.setWidthPercentage(100);

            PdfPCell cell_lifeAssuredAge11 = new PdfPCell(new Paragraph(
                    "Name of Proposer ", small_normal));
            cell_lifeAssuredAge11.setPadding(5);
            PdfPCell cell_lifeAssuredAge21 = new PdfPCell(new Paragraph(str_proposer_name, small_bold));
            cell_lifeAssuredAge21.setPadding(5);
            cell_lifeAssuredAge21.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_lifeAssuredAmaturityAge11 = new PdfPCell(
                    new Paragraph("Age of  Proposer", small_normal));
            cell_lifeAssuredAmaturityAge11.setPadding(5);
            PdfPCell cell_lifeAssuredAmaturityAge21 = new PdfPCell(
                    new Paragraph(str_proposer_age + " Years", small_bold));
            cell_lifeAssuredAmaturityAge21.setPadding(5);
            cell_lifeAssuredAmaturityAge21
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            table_lifeAssuredDetails1.addCell(cell_lifeAssuredAge11);
            table_lifeAssuredDetails1.addCell(cell_lifeAssuredAge21);

            table_lifeAssuredDetails1.addCell(cell_lifeAssuredAmaturityAge11);
            table_lifeAssuredDetails1.addCell(cell_lifeAssuredAmaturityAge21);

            document.add(table_lifeAssuredDetails1);

            PdfPTable table_lifeAssuredDetails = new PdfPTable(4);
            table_lifeAssuredDetails.setWidthPercentage(100);

            PdfPCell cell_lifeAssuredAge1 = new PdfPCell(new Paragraph(
                    "Name of the Life Assured", small_normal));
            cell_lifeAssuredAge1.setPadding(5);
            PdfPCell cell_lifeAssuredAge2 = new PdfPCell(new Paragraph(name_of_life_assured, small_bold));
            cell_lifeAssuredAge2.setPadding(5);
            cell_lifeAssuredAge2.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_lifeAssuredAmaturityAge1 = new PdfPCell(
                    new Paragraph("Age of the Life Assured" + "  ", small_normal));
            cell_lifeAssuredAmaturityAge1.setPadding(5);


            PdfPCell cell_lifeAssuredAmaturityAge2 = new PdfPCell(
                    new Paragraph(age_entry + " Years", small_bold));
            cell_lifeAssuredAmaturityAge2.setPadding(5);
            cell_lifeAssuredAmaturityAge2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            table_lifeAssuredDetails.addCell(cell_lifeAssuredAge1);
            table_lifeAssuredDetails.addCell(cell_lifeAssuredAge2);

            table_lifeAssuredDetails.addCell(cell_lifeAssuredAmaturityAge1);
            table_lifeAssuredDetails.addCell(cell_lifeAssuredAmaturityAge2);

            document.add(table_lifeAssuredDetails);

           /* PdfPTable table_lifeAssuredDetails = new PdfPTable(4);
            table_lifeAssuredDetails.setWidthPercentage(100);

            PdfPCell cell_lifeAssuredAge1 = new PdfPCell(new Paragraph(
                    "Life Assured Age at Entry", small_normal));
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

            table_lifeAssuredDetails.addCell(cell_lifeAssuredAge1);
            table_lifeAssuredDetails.addCell(cell_lifeAssuredAge2);

            table_lifeAssuredDetails.addCell(cell_lifeAssuredAmaturityAge1);
            table_lifeAssuredDetails.addCell(cell_lifeAssuredAmaturityAge2);

            document.add(table_lifeAssuredDetails);*/

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
                    "Policy Term" + "  ", small_normal));
            cell_lifeAssuredAge1.setPadding(5);
            PdfPCell cell_termsofPolicy2 = new PdfPCell(new Paragraph("  "
                    + policyTerm + " Years", small_bold));
            cell_termsofPolicy2.setPadding(5);
            cell_termsofPolicy2.setHorizontalAlignment(Element.ALIGN_CENTER);

            //table_lifeAssuredDetails_gender_policy_term
            //       .addCell(cell_lifeAssured_gender1);
            // table_lifeAssuredDetails_gender_policy_term
            //        .addCell(cell_lifeAssured_gender2);
            String payingTerm = "";
            if (planType.equalsIgnoreCase("single")) {
                payingTerm = "1 Year";
            } else if (planType.equalsIgnoreCase("Regular")) {
                payingTerm = policyTerm + " Years";
            } else {
                payingTerm = premPayTerm + " Years";
            }


            String PlanType = "";
            String str_prem_paying_term = "";
            String mode_frequency = "";
            if (plan.equalsIgnoreCase("Regular")) {

                PlanType = "Amount of Installment Premium ";
                str_prem_paying_term = "Same as Policy Term";
                mode_frequency = "Yearly";

            } else if (plan.equalsIgnoreCase("Limited")) {

                PlanType = "Amount of Installment Premium ";
                str_prem_paying_term = premPayTerm + " Years";
                mode_frequency = "Yearly";

            } else if (plan.equalsIgnoreCase("Single")) {
                PlanType = "Amount of Installment Premium ";
                str_prem_paying_term = "One time at the inception of the policy";
                mode_frequency = "One time at the inception of the policy";
            }


            PdfPCell cell_firstYearPremium1 = new PdfPCell(new Paragraph(
                    "Premium Payment Term", small_normal));
            cell_firstYearPremium1.setPadding(5);
            PdfPCell cell_firstYearPremium2 = new PdfPCell(new Paragraph(
                    str_prem_paying_term, small_bold));
            cell_firstYearPremium2.setPadding(5);
            cell_firstYearPremium2.setHorizontalAlignment(Element.ALIGN_CENTER);

            table_lifeAssuredDetails_gender_policy_term
                    .addCell(cell_termsofPolicy1);
            table_lifeAssuredDetails_gender_policy_term
                    .addCell(cell_termsofPolicy2);

            table_lifeAssuredDetails_gender_policy_term
                    .addCell(cell_firstYearPremium1);
            table_lifeAssuredDetails_gender_policy_term
                    .addCell(cell_firstYearPremium2);

            document.add(table_lifeAssuredDetails_gender_policy_term);


            PdfPTable table_lifeAssuredDetails_premium_sumAssured = new PdfPTable(
                    4);
            table_lifeAssuredDetails_premium_sumAssured.setWidthPercentage(100);

            PdfPCell cell_regularPremium1 = new PdfPCell(new Paragraph(
                    PlanType, small_normal));
            cell_lifeAssuredAmaturityAge1.setPadding(5);
            PdfPCell cell_regularPremium2 = new PdfPCell(
                    new Paragraph(
                            "Rs. "

                                    + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf((annPrem.equals("") || annPrem == null) ? "0"
                                            : annPrem))))), small_bold));
            cell_regularPremium2.setPadding(5);
            cell_regularPremium2.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_mode1 = new PdfPCell(new Paragraph("Mode / Frequency of Premium Payment ",
                    small_normal));
            cell_mode1.setPadding(5);
            PdfPCell cell_mode2 = new PdfPCell(new Paragraph(mode_frequency, small_bold));
            cell_mode2.setPadding(5);
            cell_mode2.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_sumAssured1 = new PdfPCell(new Paragraph(
                    "Sum Assured", small_normal));
            cell_lifeAssured_gender1.setPadding(5);
            PdfPCell cell_sumAssured2 = new PdfPCell(new Paragraph(
                    "Rs. "
                            + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                            .valueOf(sumAssured.equals("") ? "0"
                                    : sumAssured))))), small_bold));
            cell_sumAssured2.setPadding(5);
            cell_sumAssured2.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_sumAssured11 = new PdfPCell(new Paragraph(
                    "", small_normal));

            PdfPCell cell_sumAssured22 = new PdfPCell(new Paragraph(
                    "", small_bold));

            table_lifeAssuredDetails_premium_sumAssured
                    .addCell(cell_regularPremium1);
            table_lifeAssuredDetails_premium_sumAssured
                    .addCell(cell_regularPremium2);


            table_lifeAssuredDetails_premium_sumAssured
                    .addCell(cell_mode1);
            table_lifeAssuredDetails_premium_sumAssured
                    .addCell(cell_mode2);

            table_lifeAssuredDetails_premium_sumAssured
                    .addCell(cell_sumAssured1);
            table_lifeAssuredDetails_premium_sumAssured
                    .addCell(cell_sumAssured2);

            table_lifeAssuredDetails_premium_sumAssured
                    .addCell(cell_sumAssured11);
            table_lifeAssuredDetails_premium_sumAssured
                    .addCell(cell_sumAssured22);

            document.add(table_lifeAssuredDetails_premium_sumAssured);

            PdfPTable tablePolicyDetails2 = new PdfPTable(4);
            tablePolicyDetails2.setWidthPercentage(100);

            // tablePolicyDetails2.addCell(cell_mode1);
            //tablePolicyDetails2.addCell(cell_mode2);

            //tablePolicyDetails2.addCell(cell_firstYearPremium1);
            // tablePolicyDetails2.addCell(cell_firstYearPremium2);

            document.add(tablePolicyDetails2);
            document.add(para_img_logo_after_space_1);
            PdfPTable BI_PdftableFundDetails = new PdfPTable(2);
            BI_PdftableFundDetails.setWidthPercentage(100);

            PdfPCell BI_PdftableFundDetails_cell = new PdfPCell(new Paragraph(
                    "Rate of Applicable Taxes", small_bold));

            PdfPCell BI_PdftableFundDetails_cell2 = new PdfPCell(new Paragraph(
                    "18%", small_bold));

            BI_PdftableFundDetails_cell
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableFundDetails_cell2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableFundDetails_cell.setPadding(5);
            BI_PdftableFundDetails_cell2.setPadding(5);

            BI_PdftableFundDetails.addCell(BI_PdftableFundDetails_cell);
            BI_PdftableFundDetails.addCell(BI_PdftableFundDetails_cell2);
            document.add(BI_PdftableFundDetails);
            document.add(para_img_logo_after_space_1);

            PdfPTable BI_PdftableFundTypes = new PdfPTable(4);
            BI_PdftableFundTypes.setWidthPercentage(100);

          /*  PdfPCell investment1 = new PdfPCell(new Paragraph(
                    "Investment Strategy Opted For", small_bold1));
            investment1.setColspan(2);
            PdfPCell investment2 = new PdfPCell(new Paragraph(
                    "NA", small_bold1));
            investment2.setColspan(2);
            investment1
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            investment2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
*/
            PdfPCell BI_PdftableFundTypes_cell1 = new PdfPCell(new Paragraph(
                    "Fund Name (SFIN Name)", small_bold1));

            PdfPCell BI_PdftableFundTypes_cell2 = new PdfPCell(new Paragraph(
                    "% Allocation", small_bold1));

            PdfPCell BI_PdftableFundTypes_cell3 = new PdfPCell(new Paragraph(
                    "FMC", small_bold1));
            PdfPCell BI_PdftableFundTypes_cell11 = new PdfPCell(new Paragraph(
                    "Risk Level", small_bold1));

            BI_PdftableFundTypes_cell1
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableFundTypes_cell1.setPadding(5);
            BI_PdftableFundTypes_cell11
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableFundTypes_cell11.setPadding(5);

            BI_PdftableFundTypes_cell2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableFundTypes_cell2.setPadding(5);

            BI_PdftableFundTypes_cell3
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableFundTypes_cell3.setPadding(5);

            // BI_PdftableFundTypes.addCell(investment1);
            // BI_PdftableFundTypes.addCell(investment2);
            BI_PdftableFundTypes.addCell(BI_PdftableFundTypes_cell1);
            BI_PdftableFundTypes.addCell(BI_PdftableFundTypes_cell2);
            BI_PdftableFundTypes.addCell(BI_PdftableFundTypes_cell3);
            BI_PdftableFundTypes.addCell(BI_PdftableFundTypes_cell11);
            document.add(BI_PdftableFundTypes);

            String EquityFundFMC = "1.35%";
            if (perInvEquityFund.equalsIgnoreCase("")) {

                perInvEquityFund = "0";
            }

            PdfPTable BI_PdftableEquityFund = new PdfPTable(4);
            BI_PdftableEquityFund.setWidthPercentage(100);

            PdfPCell BI_PdftableEquityFund_cell1 = new PdfPCell(new Paragraph(
                    "Equity Fund (SFIN: ULIF001100105EQUITY-FND111)", small_normal));
            PdfPCell BI_PdftableEquityFund_cell11 = new PdfPCell(new Paragraph(
                    "High", small_normal));
            PdfPCell BI_PdftableEquityFund_cell2 = new PdfPCell(new Paragraph(
                    perInvEquityFund + " %", small_normal));

            PdfPCell BI_PdftableEquityFund_cell3 = new PdfPCell(new Paragraph(
                    EquityFundFMC, small_normal));

            BI_PdftableEquityFund_cell1
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableEquityFund_cell1.setPadding(5);
            BI_PdftableEquityFund_cell11
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableEquityFund_cell11.setPadding(5);

            BI_PdftableEquityFund_cell2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableEquityFund_cell2.setPadding(5);

            BI_PdftableEquityFund_cell3
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableEquityFund_cell3.setPadding(5);

            BI_PdftableEquityFund.addCell(BI_PdftableEquityFund_cell1);
            BI_PdftableEquityFund.addCell(BI_PdftableEquityFund_cell2);
            BI_PdftableEquityFund.addCell(BI_PdftableEquityFund_cell3);
            BI_PdftableEquityFund.addCell(BI_PdftableEquityFund_cell11);
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
            PdfPCell BI_PdftableTopEquityOptimiserFund_cell11 = new PdfPCell(
                    new Paragraph("High", small_normal));
            PdfPCell BI_PdftableEquityOptimiserFund_cell2 = new PdfPCell(
                    new Paragraph(perInvEquityOptimiserFund + " %",
                            small_normal));

            PdfPCell BI_PdftableTopEquityOptimiserFund_cell3 = new PdfPCell(
                    new Paragraph(EquityOptimiserFundFMC, small_normal));

            BI_PdftableTopEquityOptimiserFund_cell1
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableTopEquityOptimiserFund_cell1.setPadding(5);
            BI_PdftableTopEquityOptimiserFund_cell11
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableTopEquityOptimiserFund_cell11.setPadding(5);

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
                    .addCell(BI_PdftableTopEquityOptimiserFund_cell11);
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
            PdfPCell BI_PdftableTopGrowthFund_cell11 = new PdfPCell(
                    new Paragraph("Medium to High", small_normal));
            PdfPCell BI_PdftableGrowthFund_cell2 = new PdfPCell(new Paragraph(
                    perInvgrowthFund + " %", small_normal));

            PdfPCell BI_PdftableTopGrowthFund_cell3 = new PdfPCell(
                    new Paragraph(GrowthFundFMC, small_normal));

            BI_PdftableTopGrowthFund_cell1
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableTopGrowthFund_cell1.setPadding(5);
            BI_PdftableTopGrowthFund_cell11
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableTopGrowthFund_cell11.setPadding(5);

            BI_PdftableGrowthFund_cell2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableGrowthFund_cell2.setPadding(5);

            BI_PdftableTopGrowthFund_cell3
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableTopGrowthFund_cell3.setPadding(5);

            BI_PdftableGrowthFund.addCell(BI_PdftableTopGrowthFund_cell1);
            BI_PdftableGrowthFund.addCell(BI_PdftableGrowthFund_cell2);
            BI_PdftableGrowthFund.addCell(BI_PdftableTopGrowthFund_cell3);
            BI_PdftableGrowthFund.addCell(BI_PdftableTopGrowthFund_cell11);
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
            PdfPCell BI_PdftableBalancedFund_cell11 = new PdfPCell(
                    new Paragraph("Medium", small_normal));
            PdfPCell BI_PdftableBalancedFund_cell2 = new PdfPCell(
                    new Paragraph(perInvBalancedFund + " %", small_normal));

            PdfPCell BI_PdftableBalancedFund_cell3 = new PdfPCell(
                    new Paragraph(BalancedFundFMC, small_normal));

            BI_PdftableBalancedFund_cell1
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableBalancedFund_cell1.setPadding(5);
            BI_PdftableBalancedFund_cell11
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableBalancedFund_cell11.setPadding(5);

            BI_PdftableBalancedFund_cell2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableBalancedFund_cell2.setPadding(5);

            BI_PdftableBalancedFund_cell3
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableBalancedFund_cell3.setPadding(5);

            BI_PdftableBalancedFund.addCell(BI_PdftableBalancedFund_cell1);
            BI_PdftableBalancedFund.addCell(BI_PdftableBalancedFund_cell2);
            BI_PdftableBalancedFund.addCell(BI_PdftableBalancedFund_cell3);
            BI_PdftableBalancedFund.addCell(BI_PdftableBalancedFund_cell11);
            document.add(BI_PdftableBalancedFund);

            if (perInvMoneyMarketFund.equalsIgnoreCase("")) {

                perInvMoneyMarketFund = "0";
            }
            String MoneyMarketFund = "0.0";
            String MoneyMarketFundFMC = "0.25%";

            PdfPTable BI_PdftableMoneyMarketFund = new PdfPTable(4);
            BI_PdftableMoneyMarketFund.setWidthPercentage(100);

            PdfPCell BI_PdftableMoneyMarketFund_cell1 = new PdfPCell(
                    new Paragraph("Money Market Fund (SFIN: ULIF005010206MONYMKTFND111)", small_normal));
            PdfPCell BI_PdftableMoneyMarketFund_cell11 = new PdfPCell(
                    new Paragraph("Low", small_normal));
            PdfPCell BI_PdftableMoneyMarketFund_cell2 = new PdfPCell(
                    new Paragraph(perInvMoneyMarketFund + " %", small_normal));

            PdfPCell BI_PdftableMoneyMarketFund_cell3 = new PdfPCell(
                    new Paragraph(MoneyMarketFundFMC, small_normal));

            BI_PdftableMoneyMarketFund_cell1
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableMoneyMarketFund_cell1.setPadding(5);
            BI_PdftableMoneyMarketFund_cell11
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableMoneyMarketFund_cell11.setPadding(5);

            BI_PdftableMoneyMarketFund_cell2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableMoneyMarketFund_cell2.setPadding(5);

            BI_PdftableMoneyMarketFund_cell3
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableMoneyMarketFund_cell3.setPadding(5);

            BI_PdftableMoneyMarketFund
                    .addCell(BI_PdftableMoneyMarketFund_cell1);
            BI_PdftableMoneyMarketFund
                    .addCell(BI_PdftableMoneyMarketFund_cell2);
            BI_PdftableMoneyMarketFund
                    .addCell(BI_PdftableMoneyMarketFund_cell3);
            BI_PdftableMoneyMarketFund
                    .addCell(BI_PdftableMoneyMarketFund_cell11);
            document.add(BI_PdftableMoneyMarketFund);


            if (perInvTop300Fund.equalsIgnoreCase("")) {
                perInvTop300Fund = "0";
            }

            String Top300Fund = "0.0";
            String Top300FundFMC = "1.35%";

            PdfPTable BI_PdftableTop300Fund = new PdfPTable(4);
            BI_PdftableTop300Fund.setWidthPercentage(100);

            PdfPCell BI_PdftableTop300Fund_cell1 = new PdfPCell(new Paragraph(
                    "Top 300 Fund (SFIN: ULIF016070110TOP300-FND111)", small_normal));
            PdfPCell BI_PdftableTop300Fund_cell11 = new PdfPCell(new Paragraph(
                    "High", small_normal));
            PdfPCell BI_PdftableTop300Fund_cell2 = new PdfPCell(new Paragraph(
                    perInvTop300Fund + " %", small_normal));

            PdfPCell BI_PdftableTop300Fund_cell3 = new PdfPCell(new Paragraph(
                    Top300FundFMC, small_normal));

            BI_PdftableTop300Fund_cell1
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableTop300Fund_cell1.setPadding(5);
            BI_PdftableTop300Fund_cell11
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableTop300Fund_cell11.setPadding(5);

            BI_PdftableTop300Fund_cell2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableTop300Fund_cell2.setPadding(5);

            BI_PdftableTop300Fund_cell3
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableTop300Fund_cell3.setPadding(5);

            BI_PdftableTop300Fund.addCell(BI_PdftableTop300Fund_cell1);
            BI_PdftableTop300Fund.addCell(BI_PdftableTop300Fund_cell2);
            BI_PdftableTop300Fund.addCell(BI_PdftableTop300Fund_cell3);
            BI_PdftableTop300Fund.addCell(BI_PdftableTop300Fund_cell11);
            document.add(BI_PdftableTop300Fund);


            if (perInvBondOptimiserFund.equalsIgnoreCase("")) {
                perInvBondOptimiserFund = "0";
            }

            String BondOptimiserFund = "0.0";
            String BondOptimiserFundFMC = "1.15%";

            PdfPTable BI_PdftableTop300Fund1 = new PdfPTable(4);
            BI_PdftableTop300Fund1.setWidthPercentage(100);

            PdfPCell BI_PdftableBondFund_cell112 = new PdfPCell(new Paragraph(
                    "Bond Optimiser Fund : (SFIN: ULIF032290618BONDOPTFND111)", small_normal));
            PdfPCell BI_PdftableTop300Fund_cell111 = new PdfPCell(new Paragraph(
                    "Low to Medium", small_normal));
            PdfPCell BI_PdftableTop300Fund_cell21 = new PdfPCell(new Paragraph(
                    perInvBondOptimiserFund + " %", small_normal));

            PdfPCell BI_PdftableTop300Fund_cell31 = new PdfPCell(new Paragraph(
                    BondOptimiserFundFMC, small_normal));

            BI_PdftableBondFund_cell112
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableBondFund_cell112.setPadding(5);
            BI_PdftableTop300Fund_cell111
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableTop300Fund_cell111.setPadding(5);

            BI_PdftableTop300Fund_cell21
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableTop300Fund_cell21.setPadding(5);

            BI_PdftableTop300Fund_cell31
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableTop300Fund_cell31.setPadding(5);

            BI_PdftableTop300Fund1.addCell(BI_PdftableBondFund_cell112);

            BI_PdftableTop300Fund1.addCell(BI_PdftableTop300Fund_cell21);
            BI_PdftableTop300Fund1.addCell(BI_PdftableTop300Fund_cell31);
            BI_PdftableTop300Fund1.addCell(BI_PdftableTop300Fund_cell111);
            document.add(BI_PdftableTop300Fund1);

            if (perInvBondFund.equalsIgnoreCase("")) {

                perInvBondFund = "0";
            }

            String BondFund = "0.2";
            String BondFundFMC = "1.00%";

            PdfPTable BI_PdftableBondFund = new PdfPTable(4);
            BI_PdftableBondFund.setWidthPercentage(100);

            PdfPCell BI_PdftableBondFund_cell1 = new PdfPCell(new Paragraph(
                    "Bond Fund (SFIN: ULIF002100105BONDULPFND111)", small_normal));
            PdfPCell BI_PdftableBondFund_cell11 = new PdfPCell(new Paragraph(
                    "Low to Medium", small_normal));
            PdfPCell BI_PdftableBondFund_cell2 = new PdfPCell(new Paragraph(
                    perInvBondFund + " %", small_normal));

            PdfPCell BI_PdftableBondFund_cell3 = new PdfPCell(new Paragraph(
                    BondFundFMC, small_normal));

            BI_PdftableBondFund_cell1
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableBondFund_cell1.setPadding(5);
            BI_PdftableBondFund_cell11
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableBondFund_cell11.setPadding(5);

            BI_PdftableBondFund_cell2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableBondFund_cell2.setPadding(5);

            BI_PdftableBondFund_cell3
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableBondFund_cell3.setPadding(5);

            BI_PdftableBondFund.addCell(BI_PdftableBondFund_cell1);

            BI_PdftableBondFund.addCell(BI_PdftableBondFund_cell2);
            BI_PdftableBondFund.addCell(BI_PdftableBondFund_cell3);
            BI_PdftableBondFund.addCell(BI_PdftableBondFund_cell11);
            document.add(BI_PdftableBondFund);

            if (perInvMidcapFund.equalsIgnoreCase("")) {
                perInvMidcapFund = "0";
            }

            String MidcapFund = "0.0";
            String MidcapFundFMC = "1.35%";

            PdfPTable BI_PdftableTop300Fund3 = new PdfPTable(4);
            BI_PdftableTop300Fund3.setWidthPercentage(100);

            PdfPCell BI_PdftableTop300Fund_cell13 = new PdfPCell(new Paragraph(
                    "Midcap Fund: (SFIN: ULIF031290915MIDCAPFUND111)", small_normal));
            PdfPCell BI_PdftableTop300Fund_cell131 = new PdfPCell(new Paragraph(
                    "High", small_normal));
            PdfPCell BI_PdftableTop300Fund_cell23 = new PdfPCell(new Paragraph(
                    perInvMidcapFund + " %", small_normal));

            PdfPCell BI_PdftableTop300Fund_cell33 = new PdfPCell(new Paragraph(
                    MidcapFundFMC, small_normal));

            BI_PdftableTop300Fund_cell13
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableTop300Fund_cell13.setPadding(5);
            BI_PdftableTop300Fund_cell131
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableTop300Fund_cell131.setPadding(5);

            BI_PdftableTop300Fund_cell23
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableTop300Fund_cell23.setPadding(5);

            BI_PdftableTop300Fund_cell33
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableTop300Fund_cell33.setPadding(5);

            BI_PdftableTop300Fund3.addCell(BI_PdftableTop300Fund_cell13);

            BI_PdftableTop300Fund3.addCell(BI_PdftableTop300Fund_cell23);
            BI_PdftableTop300Fund3.addCell(BI_PdftableTop300Fund_cell33);
            BI_PdftableTop300Fund3.addCell(BI_PdftableTop300Fund_cell131);
            document.add(BI_PdftableTop300Fund3);


            if (perInvPureFund.equalsIgnoreCase("")) {
                perInvPureFund = "0";
            }

            String PureFund = "0.0";
            String PureFundFMC = "1.35%";

            PdfPTable BI_PdftableTop300Fund4 = new PdfPTable(4);
            BI_PdftableTop300Fund4.setWidthPercentage(100);

            PdfPCell BI_PdftableTop300Fund_cell14 = new PdfPCell(new Paragraph(
                    "Pure Fund (SFIN : ULIF030290915PUREULPFND111)", small_normal));
            PdfPCell BI_PdftableTop300Fund_cell141 = new PdfPCell(new Paragraph(
                    "High", small_normal));
            PdfPCell BI_PdftableTop300Fund_cell24 = new PdfPCell(new Paragraph(
                    perInvPureFund + " %", small_normal));

            PdfPCell BI_PdftableTop300Fund_cell34 = new PdfPCell(new Paragraph(
                    PureFundFMC, small_normal));

            BI_PdftableTop300Fund_cell14
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableTop300Fund_cell14.setPadding(5);
            BI_PdftableTop300Fund_cell141
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableTop300Fund_cell141.setPadding(5);

            BI_PdftableTop300Fund_cell24
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableTop300Fund_cell24.setPadding(5);

            BI_PdftableTop300Fund_cell34
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableTop300Fund_cell34.setPadding(5);

            BI_PdftableTop300Fund4.addCell(BI_PdftableTop300Fund_cell14);

            BI_PdftableTop300Fund4.addCell(BI_PdftableTop300Fund_cell24);
            BI_PdftableTop300Fund4.addCell(BI_PdftableTop300Fund_cell34);
            BI_PdftableTop300Fund4.addCell(BI_PdftableTop300Fund_cell141);
            document.add(BI_PdftableTop300Fund4);


            if (perInvCorporateBondFund.equalsIgnoreCase("")) {
                perInvCorporateBondFund = "0";
            }

            String CorporateBondFund = "0.0";
            String CorporateBondFundFMC = "1.15%";

            PdfPTable BI_PdftableTop300Fund2 = new PdfPTable(4);
            BI_PdftableTop300Fund2.setWidthPercentage(100);

            PdfPCell BI_PdftableTop300Fund_cell12 = new PdfPCell(new Paragraph(
                    "Corporate Bond Fund: (SFIN: ULIF033290618CORBONDFND111 )", small_normal));
            PdfPCell BI_PdftableTop300Fund_cell22 = new PdfPCell(new Paragraph(
                    perInvCorporateBondFund + " %", small_normal));

            PdfPCell BI_PdftableTop300Fund_cell32 = new PdfPCell(new Paragraph(
                    CorporateBondFundFMC, small_normal));

            PdfPCell BI_PdftableTop300Fund_cell321 = new PdfPCell(new Paragraph(
                    "Low to Medium", small_normal));

            BI_PdftableTop300Fund_cell12
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableTop300Fund_cell12.setPadding(5);

            BI_PdftableTop300Fund_cell22
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableTop300Fund_cell22.setPadding(5);

            BI_PdftableTop300Fund_cell32
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableTop300Fund_cell32.setPadding(5);

            BI_PdftableTop300Fund_cell321
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableTop300Fund_cell321.setPadding(5);

            BI_PdftableTop300Fund2.addCell(BI_PdftableTop300Fund_cell12);
            BI_PdftableTop300Fund2.addCell(BI_PdftableTop300Fund_cell22);
            BI_PdftableTop300Fund2.addCell(BI_PdftableTop300Fund_cell32);
            BI_PdftableTop300Fund2.addCell(BI_PdftableTop300Fund_cell321);
            document.add(BI_PdftableTop300Fund2);


            document.add(para_img_logo_after_space_1);
            document.add(BI_Pdftablereadunderstand);
            document.add(BI_Pdftable101);
            document.add(BI_Pdftable102);
            document.add(BI_Pdftable103);
            document.add(BI_Pdftable104);
            document.add(para_img_logo_after_space_1);

            PdfPTable BI_PdftableNoofyearselapsedsinceinception = new PdfPTable(
                    4);
            BI_PdftableNoofyearselapsedsinceinception.setWidthPercentage(100);

            PdfPCell BI_PdftableBI_PdftableNoofyearselapsedsinceinception_cell1 = new PdfPCell(
                    new Paragraph("No. of years elapsed since inception",
                            small_normal));
            PdfPCell BI_PdftableBI_PdftableNoofyearselapsedsinceinception_cell2 = new PdfPCell(
                    new Paragraph(noOfYrElapsed + " Years", small_bold));

            PdfPCell BI_PdftableBI_PdftableNoofyearselapsedsinceinception_cell3 = new PdfPCell(
                    new Paragraph("Reduction in Yield @ 8%", small_normal));

            PdfPCell BI_PdftableBI_PdftableNoofyearselapsedsinceinception_cell4 = new PdfPCell(
                    new Paragraph(redInYieldNoYr + " %", small_bold));

            BI_PdftableBI_PdftableNoofyearselapsedsinceinception_cell1
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableBI_PdftableNoofyearselapsedsinceinception_cell1
                    .setPadding(5);

            BI_PdftableBI_PdftableNoofyearselapsedsinceinception_cell2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableBI_PdftableNoofyearselapsedsinceinception_cell2
                    .setPadding(5);

            BI_PdftableBI_PdftableNoofyearselapsedsinceinception_cell3
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableBI_PdftableNoofyearselapsedsinceinception_cell3
                    .setPadding(5);

            BI_PdftableBI_PdftableNoofyearselapsedsinceinception_cell4
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableBI_PdftableNoofyearselapsedsinceinception_cell4
                    .setPadding(5);

            BI_PdftableNoofyearselapsedsinceinception
                    .addCell(BI_PdftableBI_PdftableNoofyearselapsedsinceinception_cell1);
            BI_PdftableNoofyearselapsedsinceinception
                    .addCell(BI_PdftableBI_PdftableNoofyearselapsedsinceinception_cell2);
            BI_PdftableNoofyearselapsedsinceinception
                    .addCell(BI_PdftableBI_PdftableNoofyearselapsedsinceinception_cell3);
            BI_PdftableNoofyearselapsedsinceinception
                    .addCell(BI_PdftableBI_PdftableNoofyearselapsedsinceinception_cell4);
            //document.add(BI_PdftableNoofyearselapsedsinceinception);

            PdfPTable BI_PdftableMaturityAt = new PdfPTable(4);
            BI_PdftableMaturityAt.setWidthPercentage(100);

            PdfPCell BI_PdftableBI_PdftableMaturityAt_cell1 = new PdfPCell(
                    new Paragraph("Maturity At", small_normal));
            PdfPCell BI_PdftableBI_PdftableMaturityAt_cell2 = new PdfPCell(
                    new Paragraph(policyTerm + " Years", small_bold));

            PdfPCell BI_PdftableBI_PdftableMaturityAt_cell3 = new PdfPCell(
                    new Paragraph("Reduction in Yield @ 8%", small_normal));

            PdfPCell BI_PdftableBI_PdftableMaturityAt_cell4 = new PdfPCell(
                    new Paragraph(redInYieldMat + " %", small_bold));

            BI_PdftableBI_PdftableMaturityAt_cell1
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableBI_PdftableMaturityAt_cell1.setPadding(5);

            BI_PdftableBI_PdftableMaturityAt_cell2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableBI_PdftableMaturityAt_cell2.setPadding(5);

            BI_PdftableBI_PdftableMaturityAt_cell3
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableBI_PdftableMaturityAt_cell3.setPadding(5);

            BI_PdftableBI_PdftableMaturityAt_cell4
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableBI_PdftableMaturityAt_cell4.setPadding(5);

            BI_PdftableMaturityAt
                    .addCell(BI_PdftableBI_PdftableMaturityAt_cell1);
            BI_PdftableMaturityAt
                    .addCell(BI_PdftableBI_PdftableMaturityAt_cell2);
            BI_PdftableMaturityAt
                    .addCell(BI_PdftableBI_PdftableMaturityAt_cell3);
            BI_PdftableMaturityAt
                    .addCell(BI_PdftableBI_PdftableMaturityAt_cell4);
            //document.add(BI_PdftableMaturityAt);
            document.add(para_img_logo_after_space_1);

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

            for (int i = 0; i < Integer.parseInt(policyTerm); i++) {

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


            PdfPTable t22 = new PdfPTable(1);
            t22.setWidthPercentage(100);
            PdfPCell cell22 = new PdfPCell(new Paragraph("* See Part B for details", small_bold2));
            t22.addCell(cell22);
            document.add(t22);

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
            Paragraph new_line = new Paragraph("\n");
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

            PdfPTable BI_Pdftableoutput8 = new PdfPTable(15);
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
            PdfPCell BI_Pdftable_output_cell88 = new PdfPCell(new Paragraph(
                    "Other charges*", small_bold2));
            PdfPCell BI_Pdftable_output_cell881 = new PdfPCell(new Paragraph(
                    "Additions to the fund*", small_bold2));
            PdfPCell BI_Pdftable_output_cell781 = new PdfPCell(new Paragraph(
                    "Guarantee Addition", small_bold2));
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

            BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell88);
            BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell881);
            BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell781);
            BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell98);

            BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell108);
            BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell118);
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
            document.add(BI_Pdftableoutput_no8);

            for (int i = 0; i < Integer.parseInt(policyTerm); i++) {

                PdfPTable BI_Pdftableoutput_row18 = new PdfPTable(15);
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

                PdfPCell BI_Pdftable_output_row1_cell78 = new PdfPCell(
                        new Paragraph(list_data1.get(i).getTotal_charge1(),
                                small_bold2));

                PdfPCell BI_Pdftable_output_row1_cell881 = new PdfPCell(
                        new Paragraph(list_data1.get(i)
                                .getStr_AddToTheFund4Pr(),
                                small_bold2));

                PdfPCell BI_Pdftable_output_row1_cell118 = new PdfPCell(
                        new Paragraph(
                                list_data1.get(i).getGuranteed_addition1(),
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

                BI_Pdftableoutput_row18.addCell(BI_Pdftable_output_row1_cell78);
                BI_Pdftableoutput_row18.addCell(BI_Pdftable_output_row1_cell881);
                BI_Pdftableoutput_row18.addCell(BI_Pdftable_output_row1_cell118);
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
                    new Paragraph("Net Yield -  " + netYield8pa + " %", small_bold2));

            PdfPCell BI_PdftableBI_PdftableNetYield_cell3 = new PdfPCell(
                    new Paragraph("Reduction in yield -  " + redInYieldMat + " %",
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

            // document.add(BI_PdftableNetYield);


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

            PdfPTable BI_Pdftableoutput4 = new PdfPTable(15);
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

            PdfPCell BI_Pdftable_output_cell8814 = new PdfPCell(new Paragraph(
                    "Other charges*", small_bold2));
            PdfPCell BI_Pdftable_output_cell8811 = new PdfPCell(new Paragraph(
                    "Additions to the fund*", small_bold2));

            PdfPCell BI_Pdftable_output_cell7811 = new PdfPCell(new Paragraph(
                    "Guarantee Addition", small_bold2));


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

            BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell8814);
            BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell8811);
            BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell7811);
            BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell981);

            BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell1081);
            BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell1181);
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
            document.add(BI_Pdftableoutput_no4);

            for (int i = 0; i < Integer.parseInt(policyTerm); i++) {

                PdfPTable BI_Pdftableoutput_row184 = new PdfPTable(15);
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


                PdfPCell BI_Pdftable_output_row1_cell781 = new PdfPCell(
                        new Paragraph(list_data2.get(i).getTotal_charge1(),
                                small_bold2));

                PdfPCell BI_Pdftable_output_row1_cell8811 = new PdfPCell(
                        new Paragraph(list_data2.get(i)
                                .getStr_AddToTheFund4Pr(),
                                small_bold2));

                PdfPCell BI_Pdftable_output_row1_cell1181 = new PdfPCell(
                        new Paragraph(
                                list_data2.get(i).getGuranteed_addition1(),
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

                BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell781);
                BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell8811);
                BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell1181);
                BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell981);

                BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell1081);
                BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell1281);

                BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell1381);
                BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell1481);

                document.add(BI_Pdftableoutput_row184);

            }




























           /* PdfPTable BI_Pdftable_base_premium = new PdfPTable(1);
            BI_Pdftable_base_premium.setWidthPercentage(100);
            PdfPCell BI_Pdftable_base_premium_cell1 = new PdfPCell(
                    new Paragraph("*It is a base premium", small_bold2));

            BI_Pdftable_base_premium_cell1.setPadding(5);

            BI_Pdftable_base_premium.addCell(BI_Pdftable_base_premium_cell1);
            document.add(BI_Pdftable_base_premium);*/

            document.add(para_img_logo_after_space_1);
            PdfPTable BI_Pdftable_note = new PdfPTable(1);
            BI_Pdftable_note.setWidthPercentage(100);
            PdfPCell BI_Pdftable_note_cell1 = new PdfPCell(new Paragraph(
                    "Notes", small_bold));
            BI_Pdftable_note_cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
            BI_Pdftable_note_cell1.setPadding(5);

            BI_Pdftable_note.addCell(BI_Pdftable_note_cell1);
            document.add(BI_Pdftable_note);


            PdfPTable BI_Pdftable10 = new PdfPTable(1);
            BI_Pdftable10.setWidthPercentage(100);
            PdfPCell BI_Pdftable10_cell1 = new PdfPCell(
                    new Paragraph(
                            "1) Refer the sales literature for explanation of terms used in this illustration",
                            small_normal));

            BI_Pdftable10_cell1.setPadding(5);

            BI_Pdftable10.addCell(BI_Pdftable10_cell1);
            document.add(BI_Pdftable10);

            PdfPTable BI_Pdftable11 = new PdfPTable(1);
            BI_Pdftable11.setWidthPercentage(100);
            PdfPCell BI_Pdftable11_cell1 = new PdfPCell(
                    new Paragraph(
                            "2)  Please read this benefit illustration in conjunction with Sales Brochure and the Policy Document to understand all Terms, Conditions & Exclusions carefully.",
                            small_normal));

            BI_Pdftable11_cell1.setPadding(5);

            BI_Pdftable11.addCell(BI_Pdftable11_cell1);
            document.add(BI_Pdftable11);

            PdfPTable BI_Pdftable12 = new PdfPTable(1);
            BI_Pdftable12.setWidthPercentage(100);
            PdfPCell BI_Pdftable12_cell1 = new PdfPCell(
                    new Paragraph(
                            "3)Kindly note that above is only an illustration and does not in any way create any rights and/or obligations. The actual experience on the contract may be different from what is illustrated. The non-guaranteed low and high rate mentioned above relate to assumed investment returns at different rates and may vary depending upon market conditions. For more details on risk factors, terms and conditions please read sales brochure carefully.",
                            small_normal));

            BI_Pdftable12_cell1.setPadding(5);

            BI_Pdftable12.addCell(BI_Pdftable12_cell1);
            document.add(BI_Pdftable12);

            PdfPTable BI_Pdftable13 = new PdfPTable(1);
            BI_Pdftable13.setWidthPercentage(100);
            PdfPCell BI_Pdftable13_cell1 = new PdfPCell(
                    new Paragraph(
                            "4)  The unit values may go up as well as down and past performance is no indication of future performance on the part of SBI Life Insurance Co. Ltd. We would request you to appreciate the associated risk under this plan vis-à-vis the likely future returns before taking your investment decision.",
                            small_normal));

            BI_Pdftable13_cell1.setPadding(5);

            BI_Pdftable13.addCell(BI_Pdftable13_cell1);
            document.add(BI_Pdftable13);


            PdfPTable BI_Pdftable14 = new PdfPTable(1);
            BI_Pdftable14.setWidthPercentage(100);
            PdfPCell BI_Pdftable14_cell1 = new PdfPCell(
                    new Paragraph(
                            "5)  It is assumed that the policy is in force throughout the term.",

                            small_normal));

            BI_Pdftable14_cell1.setPadding(5);

            BI_Pdftable14.addCell(BI_Pdftable14_cell1);
            document.add(BI_Pdftable14);

            PdfPTable BI_Pdftable145 = new PdfPTable(1);
            BI_Pdftable145.setWidthPercentage(100);
            PdfPCell BI_Pdftable145_cell1 = new PdfPCell(
                    new Paragraph(
                            "6)  Fund management charge is based on the specific investment strategy / Fund option(s) chosen",

                            small_normal));

            BI_Pdftable145_cell1.setPadding(5);

            BI_Pdftable145.addCell(BI_Pdftable145_cell1);
            document.add(BI_Pdftable145);

            PdfPTable BI_Pdftable146 = new PdfPTable(1);
            BI_Pdftable146.setWidthPercentage(100);
            PdfPCell BI_Pdftable146_cell1 = new PdfPCell(
                    new Paragraph(
                            "7)  Surrender Value equals the Fund Value at the end of the year minus Discontinuance Charges. Surrender value is available on or after 5th policy anniversary.",

                            small_normal));

            BI_Pdftable146_cell1.setPadding(5);

            BI_Pdftable146.addCell(BI_Pdftable146_cell1);
            document.add(BI_Pdftable146);


            PdfPTable BI_Pdftable148 = new PdfPTable(1);
            BI_Pdftable148.setWidthPercentage(100);
            PdfPCell BI_Pdftable148_cell1 = new PdfPCell(
                    new Paragraph(
                            "8)  Acceptance of proposal is subject to Underwriting decision. Mortality charges are for a healthy person.",

                            small_normal));

            BI_Pdftable148_cell1.setPadding(5);

            BI_Pdftable148.addCell(BI_Pdftable148_cell1);
            document.add(BI_Pdftable148);

            PdfPTable BI_Pdftable149 = new PdfPTable(1);
            BI_Pdftable149.setWidthPercentage(100);
            PdfPCell BI_Pdftable149_cell1 = new PdfPCell(
                    new Paragraph(
                            "9)  Applicable Taxes (including surcharge/cess etc), at the rate notified by the Central Government/ State Government / Union Territories of India from time to time and as per the provisions of the prevalent tax laws will be payable on premium/ or any other charges as per the product features.",

                            small_normal));

            BI_Pdftable149_cell1.setPadding(5);

            BI_Pdftable149.addCell(BI_Pdftable149_cell1);
            document.add(BI_Pdftable149);

            PdfPTable BI_Pdftable_TAX_FMC = new PdfPTable(1);
            BI_Pdftable_TAX_FMC.setWidthPercentage(100);
            PdfPCell BI_Pdftable_TAX_FMC_cell1 = new PdfPCell(new Paragraph(
                    "10)	This policy provides guaranteed death benefit of Rs. "
                            + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                            .valueOf(sumAssured.equals("") ? "0"
                                    : sumAssured))))),

                    small_normal));

            BI_Pdftable_TAX_FMC_cell1.setPadding(5);

            BI_Pdftable_TAX_FMC.addCell(BI_Pdftable_TAX_FMC_cell1);
            document.add(BI_Pdftable_TAX_FMC);

            PdfPTable BI_Pdftable1491 = new PdfPTable(1);
            BI_Pdftable1491.setWidthPercentage(100);
            PdfPCell BI_Pdftable1491_cell1 = new PdfPCell(
                    new Paragraph(
                            "11)  Net Yield have been calculated after applying all the charges (except GST, mortality charges).",

                            small_normal));

            BI_Pdftable1491_cell1.setPadding(5);

            BI_Pdftable1491.addCell(BI_Pdftable1491_cell1);
            document.add(BI_Pdftable1491);

       /*     PdfPTable BI_Pdftable15 = new PdfPTable(1);
            BI_Pdftable15.setWidthPercentage(100);
            PdfPCell BI_Pdftable15_cell1 = new PdfPCell(
                    new Paragraph(
                            "11) This illustration has been prepared in compliance with the IRDAI (Linked Products) Regulations, 2013.",
                            small_normal));

            BI_Pdftable15_cell1.setPadding(5);

            BI_Pdftable15.addCell(BI_Pdftable15_cell1);
            document.add(BI_Pdftable15);

            PdfPTable BI_Pdftable16 = new PdfPTable(1);
            BI_Pdftable16.setWidthPercentage(100);
            PdfPCell BI_Pdftable16_cell1 = new PdfPCell(
                    new Paragraph(
                            "12) Col (24) gives the commission payable to the agent/ broker in respect of the policy .",

                            small_normal));

            BI_Pdftable16_cell1.setPadding(5);

            BI_Pdftable16.addCell(BI_Pdftable16_cell1);
            document.add(BI_Pdftable16);*/

            document.add(para_img_logo_after_space_1);
            PdfPTable BI_Pdftable19 = new PdfPTable(1);
            BI_Pdftable19.setWidthPercentage(100);
            PdfPCell BI_Pdftable19_cell1 = new PdfPCell(new Paragraph(
                    "Definition of Various Charges", small_bold));
            BI_Pdftable19_cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
            BI_Pdftable19_cell1.setPadding(5);

            BI_Pdftable19.addCell(BI_Pdftable19_cell1);
            document.add(BI_Pdftable19);

            PdfPTable BI_Pdftable24 = new PdfPTable(1);
            BI_Pdftable24.setWidthPercentage(100);
            PdfPCell BI_Pdftable24_cell1 = new PdfPCell(
                    new Paragraph(
                            "Accident Benefit charges a charge of a fixed sum based on the sum assured chosen, which is applied at the beginning of each policy month by cancelling units for equivalent amount.",
                            small_normal));

            BI_Pdftable24_cell1.setPadding(5);

            BI_Pdftable24.addCell(BI_Pdftable24_cell1);
            //document.add(BI_Pdftable24);

            PdfPTable BI_Pdftable20 = new PdfPTable(1);
            BI_Pdftable20.setWidthPercentage(100);
            PdfPCell BI_Pdftable20_cell1 = new PdfPCell(
                    new Paragraph(
                            "1) Policy Administration Charges : a charge of a fixed sum which is applied at the beginning of each policy month by cancelling units for equivalent amount, deducted for maintaing the policy.",
                            small_normal));

            BI_Pdftable20_cell1.setPadding(5);

            BI_Pdftable20.addCell(BI_Pdftable20_cell1);
            document.add(BI_Pdftable20);

            PdfPTable BI_Pdftable21 = new PdfPTable(1);
            BI_Pdftable21.setWidthPercentage(100);
            PdfPCell BI_Pdftable21_cell1 = new PdfPCell(
                    new Paragraph(
                            "2)  Premium Allocation Charge : is the percentage of premium that would not be utilised to purchase units.",
                            small_normal));

            BI_Pdftable21_cell1.setPadding(5);

            BI_Pdftable21.addCell(BI_Pdftable21_cell1);
            document.add(BI_Pdftable21);

            PdfPTable BI_Pdftable22 = new PdfPTable(1);
            BI_Pdftable22.setWidthPercentage(100);
            PdfPCell BI_Pdftable22_cell1 = new PdfPCell(
                    new Paragraph(
                            "3)  Mortality Charges : are the charges recovered for providing life insurance cover, deducted applied at the beginning of each policy month by cancelling units for equivalent amount.",
                            small_normal));

            BI_Pdftable22_cell1.setPadding(5);

            BI_Pdftable22.addCell(BI_Pdftable22_cell1);
            document.add(BI_Pdftable22);

            PdfPTable BI_Pdftable23 = new PdfPTable(1);
            BI_Pdftable23.setWidthPercentage(100);
            PdfPCell BI_Pdftable23_cell1 = new PdfPCell(
                    new Paragraph(
                            "4)  Fund Management Charge (FMC) : is the deduction made from the fund at a stated percentage before the computation of the NAV of the fund.",
                            small_normal));

            BI_Pdftable23_cell1.setPadding(5);

            BI_Pdftable23.addCell(BI_Pdftable23_cell1);
            document.add(BI_Pdftable23);
            document.add(para_img_logo_after_space_1);


            PdfPTable BI_Pdftable191 = new PdfPTable(1);
            BI_Pdftable191.setWidthPercentage(100);
            PdfPCell BI_Pdftable191_cell1 = new PdfPCell(new Paragraph(
                    "Important", small_bold));
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


            PdfPTable BI_Pdftable_CompanysPolicySurrender4 = new PdfPTable(1);
            BI_Pdftable_CompanysPolicySurrender4.setWidthPercentage(100);
            PdfPCell BI_Pdftable_CompanysPolicySurrender4_cell = new PdfPCell(
                    new Paragraph(
                            "If premium is greater than Rs 1Lakh. You have to submit Proof of source of Fund",
                            small_normal));

            BI_Pdftable_CompanysPolicySurrender4_cell.setPadding(5);

            BI_Pdftable_CompanysPolicySurrender4
                    .addCell(BI_Pdftable_CompanysPolicySurrender4_cell);

            String premiumAmount = prsObj.parseXmlTag(input, "premiumAmount");


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

            document.add(para_img_logo_after_space_1);


            document.add(BI_Pdftable26);
            document.add(BI_Pdftable26_cell1);


            document.add(para_img_logo_after_space_1);

            if (!bankUserType.equalsIgnoreCase("Y")) {

                if (BI_PdftablePolicyHolder != null) {
                    document.add(BI_PdftablePolicyHolder);
                }
            }

            document.add(BI_PdftablePolicyHolder_signature);
            document.add(para_img_logo_after_space_1);

            document.add(agentDeclarationTable);
            document.add(new_line);

            if (!bankUserType.equalsIgnoreCase("Y")) {
                if(BI_PdftableMarketing !=null){
                    document.add(BI_PdftableMarketing);
                }
                if(BI_PdftableMarketing_signature !=null){
                    document.add(BI_PdftableMarketing_signature);
                }
            }

            document.close();

        } catch (Exception e) {
            Log.i(getLocalClassName(), e.toString() + "Error in creating pdf");
        }
    }

    private void windowmessagesgin() {

        d = new Dialog(BI_SmartWealthBuilderActivity.this);
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
                Intent intent = new Intent(BI_SmartWealthBuilderActivity.this,
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

    // Store user input in Bean object
    private void addListenerOnSubmit() {
        smartWealthBuilderBean = new SmartWealthBuilderBean();

        // System.out.println("********* 1 ***********");
        if (isStaffDisc.isChecked()) {
            smartWealthBuilderBean.setIsForStaffOrNot(true);
        } else {
            smartWealthBuilderBean.setIsForStaffOrNot(false);
        }

        if (cb_kerladisc.isChecked()) {
            smartWealthBuilderBean.setKerlaDisc(true);
            // smartWealthBuilderBean.setServiceTax(true);
        } else {
            smartWealthBuilderBean.setKerlaDisc(false);
            //smartWealthBuilderBean.setServiceTax(false);
        }

        // System.out.println("********* 2 ***********");
        smartWealthBuilderBean.setAgeAtEntry(Integer.parseInt(ageInYears
                .getSelectedItem().toString()));
        // System.out.println("********* 3 ***********");
        smartWealthBuilderBean
                .setGender(selGender.getSelectedItem().toString());
        // System.out.println("********* 4 ***********");
        smartWealthBuilderBean
                .setPlanType(selPlan.getSelectedItem().toString());

        if (selPlan.getSelectedItem().toString().equals("Single")) {
            // System.out.println("********* 4.1 ***********");
            smartWealthBuilderBean.setPremFreq("Single");
            // System.out.println("********* 4.2 ***********");
            smartWealthBuilderBean.setPremiumPayingTerm(1);
        } else if (selPlan.getSelectedItem().toString().equals("Regular")) {
            // System.out.println("********* 4.3 ***********");
            smartWealthBuilderBean.setPremFreq(selPremiumFrequencyMode
                    .getSelectedItem().toString());
            // System.out.println("********* 4.4 ***********");
            smartWealthBuilderBean.setPremiumPayingTerm(1);
        } else if (selPlan.getSelectedItem().toString().equals("Limited")) {
            // System.out.println("********* 4.5 ***********");
            smartWealthBuilderBean.setPremFreq(selPremiumFrequencyMode
                    .getSelectedItem().toString());
            smartWealthBuilderBean.setPremiumPayingTerm(Integer
                    .parseInt(premPayingTerm.getSelectedItem().toString()));
        }

        // System.out.println("********* 5 ***********");
        // if (Integer.parseInt(ageInYears.getSelectedItem().toString()) < 18) {
        // smartWealthBuilderBean.setAgeOfProposer(Integer
        // .parseInt(ProposerAge.getSelectedItem().toString()));
        // } else {
        // smartWealthBuilderBean.setAgeOfProposer(0);
        // }

        // System.out.println("********* 6 ***********");
        smartWealthBuilderBean.setPolicyTerm_Basic(Integer
                .parseInt(selPolicyTerm.getSelectedItem().toString()));

        // System.out.println("********* 7 ***********");
        smartWealthBuilderBean.setPremiumAmount(Double.parseDouble(premiumAmt
                .getText().toString()));

        // System.out.println("********* 8 ***********");
        smartWealthBuilderBean.setSAMF(Double.parseDouble(SAMF.getText()
                .toString()));

        // System.out.println("********* 9 ***********");
        smartWealthBuilderBean.setNoOfYearsElapsedSinceInception(Integer
                .parseInt(noOfYearsElapsedSinceInception.getText().toString()));

        // System.out.println("********* 10 ***********");
        smartWealthBuilderBean.setEffectivePremium(Double
                .parseDouble(getEffectivePremium()));

        // System.out.println("********* 11 ***********");
        smartWealthBuilderBean.setPF(1);

        if (!percent_EquityFund.getText().toString().equals(""))
            smartWealthBuilderBean.setPercentToBeInvested_EquityFund(Double
                    .parseDouble(percent_EquityFund.getText().toString()));
        else
            smartWealthBuilderBean.setPercentToBeInvested_EquityFund(0);

        if (!percent_EquityOptFund.getText().toString().equals(""))
            smartWealthBuilderBean.setPercentToBeInvested_EquityOptFund(Double
                    .parseDouble(percent_EquityOptFund.getText().toString()));
        else
            smartWealthBuilderBean.setPercentToBeInvested_EquityOptFund(0);

        if (!percent_GrowthFund.getText().toString().equals(""))
            smartWealthBuilderBean.setPercentToBeInvested_GrowthFund(Double
                    .parseDouble(percent_GrowthFund.getText().toString()));
        else
            smartWealthBuilderBean.setPercentToBeInvested_GrowthFund(0);

        if (!percent_BalancedFund.getText().toString().equals(""))
            smartWealthBuilderBean.setPercentToBeInvested_BalancedFund(Double
                    .parseDouble(percent_BalancedFund.getText().toString()));
        else
            smartWealthBuilderBean.setPercentToBeInvested_BalancedFund(0);

        if (!percent_BondFund.getText().toString().equals(""))
            smartWealthBuilderBean.setPercentToBeInvested_BondFund(Double
                    .parseDouble(percent_BondFund.getText().toString()));
        else
            smartWealthBuilderBean.setPercentToBeInvested_BondFund(0);

        if (!percent_MoneyMktFund.getText().toString().equals(""))
            smartWealthBuilderBean
                    .setPercentToBeInvested_MoneyMarketFund(Double
                            .parseDouble(percent_MoneyMktFund.getText()
                                    .toString()));
        else
            smartWealthBuilderBean.setPercentToBeInvested_MoneyMarketFund(0);

        if (!percent_Top300Fund.getText().toString().equals(""))
            smartWealthBuilderBean.setPercentToBeInvested_Top300Fund(Double
                    .parseDouble(percent_Top300Fund.getText().toString()));
        else
            smartWealthBuilderBean.setPercentToBeInvested_Top300Fund(0);


        if (!percent_BondOptimiserFund.getText().toString().equals(""))
            smartWealthBuilderBean.setPercentToBeInvested_BondOptimiserFund(Double
                    .parseDouble(percent_BondOptimiserFund.getText().toString()));
        else
            smartWealthBuilderBean.setPercentToBeInvested_BondOptimiserFund(0);

        if (!percent_CorporateBondFund.getText().toString().equals(""))
            smartWealthBuilderBean.setPercentToBeInvested_CorpBondFund(Double
                    .parseDouble(percent_CorporateBondFund.getText().toString()));
        else
            smartWealthBuilderBean.setPercentToBeInvested_CorpBondFund(0);

        if (!percent_MidCapFund.getText().toString().equals(""))
            smartWealthBuilderBean.setPercentToBeInvested_MidcapFund(Double
                    .parseDouble(percent_MidCapFund.getText().toString()));
        else
            smartWealthBuilderBean.setPercentToBeInvested_MidcapFund(0);


        if (!percent_PureFund.getText().toString().equals(""))
            smartWealthBuilderBean.setPercentToBeInvested_PureFund(Double
                    .parseDouble(percent_PureFund.getText().toString()));
        else
            smartWealthBuilderBean.setPercentToBeInvested_PureFund(0);


        // Show smart wealth builder Output Screen
        showSmartWealthBuilderOutputPg(smartWealthBuilderBean);
    }

    /******************************************* calculation part starts here ******************************************/
    private void showSmartWealthBuilderOutputPg(
            SmartWealthBuilderBean smartWealthBuilderBean) {
        try {
            retVal = new StringBuilder();

            String mode;
            if (smartWealthBuilderBean.getPlanType().equals("Single"))
                mode = "Single";
            else
                mode = "Yearly";

            String[] outputArr = getOutput("BI_Incl_Mort & Ser Tax",
                    smartWealthBuilderBean);
            String[] redinYieldArr = getOutputReductionInYield(
                    "Benefit Illustrator_CAP", smartWealthBuilderBean);

            retVal.append("<?xml version='1.0' encoding='utf-8' ?><smartWealthBuilder>");

            /* <netYield8pa> added by Akshaya on 18-Feb-2014 **/
            retVal.append("<errCode>0</errCode>" + "<maturityAge>").append(smartWealthBuilderBean.getAgeAtEntry() + smartWealthBuilderBean
                    .getPolicyTerm_Basic()).append("</maturityAge>")
                    .append("<annPrem>").append(smartWealthBuilderBean.getEffectivePremium()).append("</annPrem>")
                    .append("<premPayTerm>").append(smartWealthBuilderBean.getPremiumPayingTerm()).append("</premPayTerm>")
                    .append("<mode>").append(mode).append("</mode>")
                    .append("<redInYieldMat>").append(redinYieldArr[0]).append("</redInYieldMat>")
                    .append("<redInYieldNoYr>").append(redinYieldArr[1]).append("</redInYieldNoYr>")
                    .append("<netYield8pa>").append(redinYieldArr[2]).append("</netYield8pa>")
                    .append("<sumAssured>").append(Double.parseDouble(outputArr[0])).append("</sumAssured>")
                    .append("<fundVal4>").append(Double.parseDouble(outputArr[1])).append("</fundVal4>")
                    .append("<fundVal8>").append(Double.parseDouble(outputArr[2])).append("</fundVal8>");

            int index = smartWealthBuilderBean.getPolicyTerm_Basic();
            String matNonGuar4pa = prsObj.parseXmlTag(bussIll.toString(), "FundValAtEnd4Pr" + index + "");
            String matNonGuar8pa = prsObj.parseXmlTag(bussIll.toString(), "FundValAtEnd8Pr" + index + "");

            retVal.append("<FundValAtEnd4Pr").append(index).append(">").append(matNonGuar4pa).append("</FundValAtEnd4Pr").append(index).append(">");
            retVal.append("<FundValAtEnd8Pr").append(index).append(">").append(matNonGuar8pa).append("</FundValAtEnd8Pr").append(index).append(">");
            retVal.append(bussIll.toString());

            retVal.append("</smartWealthBuilder>");

        } catch (Exception e) {
            retVal.append("<?xml version='1.0' encoding='utf-8' ?><smartWealthBuilder>" + "<errCode>1</errCode>" + "<errorMessage>").append(e.getMessage()).append("</errorMessage></smartWealthBuilder>");
        }


        System.out.println("Final output in xml" + retVal.toString());


        // ArrayList<String[]> outputList;
        // String[] strArr=new String[3];
        // ArrayList<String> strList=new ArrayList<String>();
        // ArrayList<String[]> arrList=new ArrayList<String[]>();
        // String[]
        // outputArr=getOutput("BI_Incl_Mort & Ser Tax",smartWealthBuilderBean);
        // String[]
        // outputArrReductionYield=getOutputReductionInYield("Benefit Illustrator_CAP",
        // smartWealthBuilderBean);
        // arrList.add(outputArr);
        // arrList.add(outputArrReductionYield);
        //
        // outputList=arrList;
        //
        // System.out.println("size "+outputList.size());
        //
        // for(int i=0;i<outputList.size();i++)
        // {
        // strArr=(String[])outputList.get(i);
        // for(int j=0;j<strArr.length;j++)
        // {
        // strList.add(strArr[j]);
        // //str2[j]=str1[j];
        // // System.out.println("outputarr "+str1[j]);
        // }
        // }
        // System.out.println("str2 ="+strList.size());
        // for(int k=0;k<strList.size();k++)
        // {
        // System.out.println("str2 "+strList.get(k));
        // }
        //
        // Intent i=new Intent(getApplicationContext(),Success.class);
        // i.putExtra("op","Sum Assured is Rs." +
        // currencyFormat.format(Double.parseDouble(strList.get(0))));
        // i.putExtra("op1", "Fund Value @ 4% is Rs." +
        // currencyFormat.format(Double.parseDouble(strList.get(1))));
        // i.putExtra("op2","Fund Value @ 8% is Rs." +
        // currencyFormat.format(Double.parseDouble(strList.get(2))));
        // // i.putExtra("op3","Reduction in Yield at Maturity is " +
        // (strList.get(3))+"%");
        // //
        // i.putExtra("op4","Reduction in Yield at Number Of Years Elapsed Since Inception is "
        // + (strList.get(4))+"%");
        // i.putExtra("header", inputActivityHeader.getText().toString());
        //
        // startActivity(i);

    }

    /**********************
     * Output start ****************************** modified by Akshaya Mirajkar
     *
     */
    private void getInput(SmartWealthBuilderBean smartWealthBuilderBean) {

        inputVal = new StringBuilder();

        String LifeAssured_title = spnr_bi_smart_wealth_builder_life_assured_title
                .getSelectedItem().toString();
        String LifeAssured_firstName = edt_bi_smart_wealth_builder_life_assured_first_name
                .getText().toString();
        String LifeAssured_middleName = edt_bi_smart_wealth_builder_life_assured_middle_name
                .getText().toString();
        String LifeAssured_lastName = edt_bi_smart_wealth_builder_life_assured_last_name
                .getText().toString();
        String LifeAssured_DOB = btn_bi_smart_wealth_builder_life_assured_date
                .getText().toString();
        String LifeAssured_age = ageInYears.getSelectedItem().toString();

        String proposer_title = "";

        if (!spnr_bi_smart_wealth_builder_proposer_title.getSelectedItem()
                .toString().equals("")
                && !spnr_bi_smart_wealth_builder_proposer_title
                .getSelectedItem().toString().equals("Select Title")) {
            proposer_title = spnr_bi_smart_wealth_builder_proposer_title
                    .getSelectedItem().toString();

        }

        String proposer_DOB = "";
        String proposer_age = "";


        if (!btn_bi_smart_wealth_builder_proposer_date.getText().toString()
                .equals("Select Date")) {
            Calendar present_date = Calendar.getInstance();
            int tDay = present_date.get(Calendar.DAY_OF_MONTH);
            int tMonth = present_date.get(Calendar.MONTH);
            int tYear = present_date.get(Calendar.YEAR);
            proposer_DOB = btn_bi_smart_wealth_builder_proposer_date.getText()
                    .toString();
            proposer_age = commonForAllProd.calculateMyAge(tYear, tMonth + 1,
                    tDay, getDate1(proposer_DOB)) + "";
        }

        // From GUI Input
        boolean staffDisc = smartWealthBuilderBean.getIsForStaffOrNot();
        // boolean
        // bancAssuranceDisc=smartWealthBuilderBean.getBancAssuranceDisc();
        int policyTerm = smartWealthBuilderBean.getPolicyTerm_Basic();
        // String premFreq=smartWealthBuilderBean.getPremiumFreq();
        // int sumAssured=smartWealthBuilderBean.getSAMF();

        inputVal.append("<?xml version='1.0' encoding='utf-8' ?><smartWealthBuilder>");

        inputVal.append("<LifeAssured_title>").append(LifeAssured_title).append("</LifeAssured_title>");
        inputVal.append("<LifeAssured_firstName>").append(LifeAssured_firstName).append("</LifeAssured_firstName>");
        inputVal.append("<LifeAssured_middleName>").append(LifeAssured_middleName).append("</LifeAssured_middleName>");
        inputVal.append("<LifeAssured_lastName>").append(LifeAssured_lastName).append("</LifeAssured_lastName>");
        inputVal.append("<LifeAssured_DOB>").append(LifeAssured_DOB).append("</LifeAssured_DOB>");
        inputVal.append("<LifeAssured_age>").append(LifeAssured_age).append("</LifeAssured_age>");

        inputVal.append("<proposer_title>").append(proposer_title).append("</proposer_title>");
        inputVal.append("<proposer_firstName>").append(proposer_First_Name).append("</proposer_firstName>");
        inputVal.append("<proposer_middleName>").append(proposer_Middle_Name).append("</proposer_middleName>");
        inputVal.append("<proposer_lastName>").append(proposer_Last_Name).append("</proposer_lastName>");
        inputVal.append("<proposer_DOB>").append(proposer_DOB).append("</proposer_DOB>");
        inputVal.append("<proposer_age>").append(proposer_age).append("</proposer_age>");
        inputVal.append("<proposer_gender>").append(gender_proposer).append("</proposer_gender>");

        inputVal.append("<product_name>").append(planName).append("</product_name>");
        inputVal.append("<product_Code>").append(product_Code).append("</product_Code>");
        inputVal.append("<product_UIN>").append(product_UIN).append("</product_UIN>");
        inputVal.append("<product_cateogory>").append(product_cateogory).append("</product_cateogory>");
        inputVal.append("<product_type>").append(product_type).append("</product_type>");

        inputVal.append("<proposer_Is_Same_As_Life_Assured>").append(proposer_Is_Same_As_Life_Assured).append("</proposer_Is_Same_As_Life_Assured>");

        inputVal.append("<isStaff>").append(staffDisc).append("</isStaff>");
        inputVal.append("<age>").append(ageInYears.getSelectedItem().toString()).append("</age>");
        inputVal.append("<gender>").append(selGender.getSelectedItem().toString()).append("</gender>");

        double SAMF = smartWealthBuilderBean.getSAMF();
        inputVal.append("<SAMF>").append(SAMF).append("</SAMF>");
        plan = smartWealthBuilderBean.getPlanType();
        inputVal.append("<plan>").append(plan).append("</plan>");

        inputVal.append("<premFreqMode>").append(selPremiumFrequencyMode.getSelectedItem().toString()).append("</premFreqMode>");

        int premPayingTerm = smartWealthBuilderBean.getPremiumPayingTerm();
        inputVal.append("<premPayingTerm>").append(premPayingTerm).append("</premPayingTerm>");
        inputVal.append("<proposerAge>").append(ageInYears.getSelectedItem().toString()).append("</proposerAge>");
        inputVal.append("<policyTerm>").append(policyTerm).append("</policyTerm>");

        double premiumAmount = smartWealthBuilderBean.getPremiumAmount();
        inputVal.append("<premiumAmount>").append(premiumAmount).append("</premiumAmount>");

        inputVal.append("<noOfYrElapsed>").append(noOfYearsElapsedSinceInception.getText().toString()).append("</noOfYrElapsed>");
        inputVal.append("<perInvEquityFund>").append(percent_EquityFund.getText().toString()).append("</perInvEquityFund>");
        inputVal.append("<perInvEquityOptimiserFund>").append(percent_EquityOptFund.getText().toString()).append("</perInvEquityOptimiserFund>");
        inputVal.append("<perInvgrowthFund>").append(percent_GrowthFund.getText().toString()).append("</perInvgrowthFund>");
        inputVal.append("<perInvBalancedFund>").append(percent_BalancedFund.getText().toString()).append("</perInvBalancedFund>");
        inputVal.append("<perInvBondFund>").append(percent_BondFund.getText().toString()).append("</perInvBondFund>");
        inputVal.append("<perInvMoneyMarketFund>").append(percent_MoneyMktFund.getText().toString()).append("</perInvMoneyMarketFund>");
        inputVal.append("<perInvTop300Fund>").append(percent_Top300Fund.getText().toString()).append("</perInvTop300Fund>");

        inputVal.append("<perInvBondOptimiserFund>").append(percent_BondOptimiserFund.getText().toString()).append("</perInvBondOptimiserFund>");

        inputVal.append("<perInvCorporateBondFund>").append(percent_CorporateBondFund.getText().toString()).append("</perInvCorporateBondFund>");

        inputVal.append("<perInvMidcapFund>").append(percent_MidCapFund.getText().toString()).append("</perInvMidcapFund>");

        inputVal.append("<perInvPureFund>").append(percent_PureFund.getText().toString()).append("</perInvPureFund>");

        //Added Tushar Kadam Kerla Applicable Taxes 7-Jun-2019
        String str_kerla_discount = "N";
        if (cb_kerladisc.isChecked()) {
            str_kerla_discount = "Y";
        }

        inputVal.append("<KFC>").append(str_kerla_discount).append("</KFC>");
        //End Added Tushar Kadam Kerla Applicable Taxes 7-Jun-2019

        inputVal.append("<BIVERSION>" + "2" + "</BIVERSION>");


        inputVal.append("</smartWealthBuilder>");

    }

    private String[] getOutput(String sheetName,
                               SmartWealthBuilderBean smartWealthBuilderBean) {
        bussIll = new StringBuilder();

        SmartWealthBuilderProperties prop = new SmartWealthBuilderProperties();

        // ouput variable declaration
        int _month_E,
                _year_F,
                _age_H = 0;

        String _policyInforce_G = "Y";

        double _premium_I,
                _topUpPremium_J,
                _premiumAllocationCharge_K,
                _topUpCharges_L = 0,
                _ServiceTaxOnAllocation_M = 0,
                _amountAvailableForInvestment_N,
                _amountAvailableForInvestment_N1,
                _sumAssuredRelatedCharges_O = 0,
                _riderCharges_P = 0,
                _policyAdministrationCharges_Q = 0,
                _mortalityCharges_R,
                _totalCharges_S,
                _serviceTaxExclOfSTOnAllocAndSurr_T = 0,
                _totalServiceTax_U,
                _additionToFundIfAny_V,
                _fundBeforeFMC_W = 0,
                _fundManagementCharge_X,
                _serviceTaxOnFMC_Y = 0,
                _fundValueAfterFMCBerforeGA_Z = 0,
                _guaranteedAddition_AA,
                _fundValueAtEnd_AB = 0,
                _surrenderCharges_AC = 0,
                _serviceTaxOnSurrenderCharges_AD = 0,
                _surrenderValue_AE,
                _deathBenefit_AF,
                _mortalityCharges_AG,
                _totalCharges_AH,
                _serviceTaxExclOfSTOnAllocAndSurr_AI = 0,
                _totalServiceTax_AJ,
                _additionToFundIfAny_AK,
                _fundBeforeFMC_AL = 0,
                _fundManagementCharge_AM,
                _serviceTaxOnFMC_AN = 0,
                _fundValueAfterFMCBerforeGA_AO = 0,
                _guaranteedAddition_AP,
                _fundValueAtEnd_AQ = 0,
                _surrenderCharges_AR = 0,
                _serviceTaxOnSurrenderCharges_AS = 0,
                _surrenderValue_AT,
                _deathBenefit_AU,
                _surrenderCap_AV = 0,
                _oneHundredPercentOfCummulativePremium_AW = 0;

        // temp variable declaration.
        int month_E, year_F, age_H;

        String policyInforce_G;

        double premium_I,
                topUpPremium_J,
                premiumAllocationCharge_K,
                topUpCharges_L,
                ServiceTaxOnAllocation_M,
                amountAvailableForInvestment_N,
                amountAvailableForInvestment_N1,
                sumAssuredRelatedCharges_O,
                riderCharges_P = 0,
                policyAdministrationCharges_Q,
                mortalityCharges_R,
                totalCharges_S,
                serviceTaxExclOfSTOnAllocAndSurr_T,
                totalServiceTax_U,
                additionToFundIfAny_V,
                fundBeforeFMC_W,
                fundManagementCharge_X,
                serviceTaxOnFMC_Y,
                fundValueAfterFMCBerforeGA_Z,
                guaranteedAddition_AA,
                fundValueAtEnd_AB = 0,
                surrenderCharges_AC,
                serviceTaxOnSurrenderCharges_AD,
                surrenderValue_AE,
                deathBenefit_AF,
                mortalityCharges_AG,
                totalCharges_AH,
                serviceTaxExclOfSTOnAllocAndSurr_AI,
                totalServiceTax_AJ,
                additionToFundIfAny_AK,
                fundBeforeFMC_AL,
                fundManagementCharge_AM,
                serviceTaxOnFMC_AN,
                fundValueAfterFMCBerforeGA_AO,
                guaranteedAddition_AP,
                fundValueAtEnd_AQ = 0,
                surrenderCharges_AR,
                serviceTaxOnSurrenderCharges_AS,
                surrenderValue_AT,
                deathBenefit_AU,
                surrenderCap_AV,
                oneHundredPercentOfCummulativePremium_AW;

        // For BI
        double sum_I = 0,
                sum_K = 0,
                sum_N = 0,
                sum_N1 = 0,
                sum_Q = 0,
                sum_R = 0,
                sum_S = 0,
                sum_U = 0,
                sum_X = 0,
                sum_Y = 0,
                sum_Z = 0,
                sum_AA = 0,
                sum_AG = 0,
                sum_AH = 0,
                sum_AJ = 0,
                sum_AM = 0,
                sum_AP = 0,
                Commission_BL, sum_01, sum_02, sum_03, sum1_03,
                sum_J = 0, otherCharges_PartB = 0, otherCharges1_PartB = 0, otherCharges_PartA, otherCharges1_PartA;

        double _sum_X = 0,
                _sum_Y,
                _sum_AM = 0,
                _sum_J = 0,
                _sum_Z,
                _sum_I = 0;


        // from GUI inputs
        boolean StaffDisc = smartWealthBuilderBean.getIsForStaffOrNot();
        boolean bancAssuranceDisc = false;
        int ageAtEntry = smartWealthBuilderBean.getAgeAtEntry();
        int policyTerm = smartWealthBuilderBean.getPolicyTerm_Basic();
        String planType = smartWealthBuilderBean.getPlanType();
        String premFreq = smartWealthBuilderBean.getPremFreq();
        int premiumPayingTerm = smartWealthBuilderBean.getPremiumPayingTerm();

        //double serviceTax =smartWealthBuilderBean.getServiceTax();
        double serviceTax;
        boolean isKerlaDisc = smartWealthBuilderBean.isKerlaDisc();
        double SAMF = smartWealthBuilderBean.getSAMF();
        double percentToBeInvested_EquityFund = smartWealthBuilderBean
                .getPercentToBeInvested_EquityFund();
        double percentToBeInvested_EquityOptFund = smartWealthBuilderBean
                .getPercentToBeInvested_EquityOptFund();
        double percentToBeInvested_GrowthFund = smartWealthBuilderBean
                .getPercentToBeInvested_GrowthFund();
        double percentToBeInvested_BalancedFund = smartWealthBuilderBean
                .getPercentToBeInvested_BalancedFund();
        double percentToBeInvested_BondFund = smartWealthBuilderBean
                .getPercentToBeInvested_BondFund();
        double percentToBeInvested_MoneyMarketFund = smartWealthBuilderBean
                .getPercentToBeInvested_MoneyMarketFund();
        double percentToBeInvested_Top300Fund = smartWealthBuilderBean
                .getPercentToBeInvested_Top300Fund();

        double percentToBeInvested_BondOptimiserFund = smartWealthBuilderBean.getPercentToBeInvested_BondOptimiserFund();
        //double percentToBeInvested_CorporateFund=smartWealthBuilderBean.getPercentToBeInvested_CorporateFund();
        double percentToBeInvested_MidcapFund = smartWealthBuilderBean.getPercentToBeInvested_MidcapFund();
        double percentToBeInvested_PureFund = smartWealthBuilderBean.getPercentToBeInvested_PureFund();
        double percentToBeInvested_CorpBondFund = smartWealthBuilderBean.getPercentToBeInvested_CorpBondFund();

        String transferFundStatus = "No Transfer";
        String addTopUp = "No";

        double effectivePremium = smartWealthBuilderBean.getEffectivePremium();
        int PF = smartWealthBuilderBean.getPF();
        int PPT = calPPT(premiumPayingTerm);
        double effectiveTopUpPrem = smartWealthBuilderBean
                .getEffectiveTopUpPrem(); // *Sheet Name -> Input,*Cell -> Y70
        double sumAssured = (effectivePremium * SAMF);

        SmartWealthBuilderBusinessLogic BIMAST = new SmartWealthBuilderBusinessLogic();
        String[] forBIArray = BIMAST
                .getForBIArr(prop.mortalityCharges_AsPercentOfofLICtable);
        int rowNumber = 0;

        bussIll.append("<staffDiscPercentage>").append(BIMAST.getStaffPercentage(StaffDisc, planType, PPT)).append("</staffDiscPercentage>");
        bussIll.append("<staffDiscCode>").append(BIMAST.getStaffDiscCode(StaffDisc)).append("</staffDiscCode>");

        try {
            for (int i = 0; i < (policyTerm * 12); i++)
            // for(int i=0;i<1;i++)
            {
                rowNumber++;

                BIMAST.setMonth_E(rowNumber);
                month_E = Integer.parseInt(BIMAST.getMonth_E());
                _month_E = month_E;
                // System.out.println("1. month_E "+BIMAST.getMonth_E());
                // System.out.println("1. month_E "+month_E);

                BIMAST.setYear_F();
                year_F = Integer.parseInt(BIMAST.getYear_F());
                _year_F = year_F;
                // System.out.println("2. year_F "+BIMAST.getYear_F());
                // System.out.println("2. year_F "+year_F);

                if ((_month_E % 12) == 0) {
                    bussIll.append("<policyYr").append(_year_F).append(">").append(_year_F).append("</policyYr").append(_year_F).append(">");
                }
                if (isKerlaDisc && _year_F <= 2) {
                    serviceTax = 0.19;
                } else {
                    serviceTax = 0.18;
                }
                policyInforce_G = BIMAST.getPolicyInForce_G();
                _policyInforce_G = policyInforce_G;
                // System.out.println("3. policyInforce_G "+BIMAST.getPolicyInForce_G());
                // System.out.println("3. policyInforce_G "+policyInforce_G);

                BIMAST.setAge_H(ageAtEntry);
                age_H = Integer.parseInt(BIMAST.getAge_H());
                _age_H = age_H;
                // System.out.println("4. age_H "+BIMAST.getAge_H());
                // System.out.println("4. age_H "+age_H);

                BIMAST.setPremium_I(PPT, PF, effectivePremium);
                premium_I = Double.parseDouble(BIMAST.getPremium_I());
                _premium_I = premium_I;
                // System.out.println("5. premium_I "+BIMAST.getPremium_I());
                // System.out.println("5. premium_I "+premium_I);

                sum_I += _premium_I;
                if ((_month_E % 12) == 0) {

                    bussIll.append("<AnnPrem").append(_year_F).append(">").append(sum_I).append("</AnnPrem").append(_year_F).append(">");
                    _sum_I = sum_I;
                    sum_I = 0;
                }

                BIMAST.setTopUpPremium_J(prop.topUpStatus, effectiveTopUpPrem);
                topUpPremium_J = Double.parseDouble(BIMAST.getTopUpPremium_J());
                _topUpPremium_J = topUpPremium_J;
                // System.out.println("6. topUpPremium_J "+BIMAST.getTopUpPremium_J());
                // System.out.println("6. topUpPremium_J "+topUpPremium_J);

                sum_J += _topUpPremium_J;
                if ((_month_E % 12) == 0) {

//					bussIll.append("<AnnPrem"+ _year_F +">" + sum_J + "</AnnPrem"+ _year_F +">");
                    _sum_J = sum_J;
                    sum_J = 0;
                }

                BIMAST.setPremiumAllocationCharge_K(StaffDisc,
                        bancAssuranceDisc, planType, PPT, effectivePremium);
                premiumAllocationCharge_K = Double.parseDouble(BIMAST
                        .getPremiumAllocationCharge_K());
                _premiumAllocationCharge_K = premiumAllocationCharge_K;
                // System.out.println("7. premiumAllocationCharge_K "+BIMAST.getPremiumAllocationCharge_K());
                // System.out.println("7. premiumAllocationCharge_K "+premiumAllocationCharge_K);

                sum_K += _premiumAllocationCharge_K;
                sum_01 = sum_K;
                if ((_month_E % 12) == 0) {

                    bussIll.append("<PremAllCharge").append(_year_F).append(">").append(Math.round(sum_K)).append("</PremAllCharge").append(_year_F).append(">");
                    sum_K = 0;
                }

                BIMAST.setTopUpCharges_L(prop.topUp);
                topUpCharges_L = Double.parseDouble(BIMAST.getTopUpCharges_L());
                _topUpCharges_L = topUpCharges_L;
                // System.out.println("8. topUpCharges_L "+BIMAST.getTopUpCharges_L());
                // System.out.println("8. topUpCharges_L "+topUpCharges_L);

                BIMAST.setServiceTaxOnAllocation_M(prop.allocationCharges,
                        serviceTax);
                ServiceTaxOnAllocation_M = Double.parseDouble(BIMAST.getServiceTaxOnAllocation_M());
                _ServiceTaxOnAllocation_M = ServiceTaxOnAllocation_M;
                // System.out.println("9. ServiceTaxOnAllocation_M "+BIMAST.getServiceTaxOnAllocation_M());
                // System.out.println("9. ServiceTaxOnAllocation_M "+ServiceTaxOnAllocation_M);

                BIMAST.setAmountAvailableForInvestment_N();
                amountAvailableForInvestment_N = Double.parseDouble(BIMAST
                        .getAmountAvailableForInvestment_N());
                _amountAvailableForInvestment_N = amountAvailableForInvestment_N;
                // System.out.println("10. amountAvailableForInvestment_N "+BIMAST.getAmountAvailableForInvestment_N());
                // System.out.println("10. amountAvailableForInvestment_N "+amountAvailableForInvestment_N);

//				Added By Saurabh Jain on 14/10/2019 Start

                BIMAST.setAmountAvailableForInvestment_N1();
                amountAvailableForInvestment_N1 = Double.parseDouble(BIMAST.getAmountAvailableForInvestment_N1());
                _amountAvailableForInvestment_N1 = amountAvailableForInvestment_N1;
                sum_N1 += _amountAvailableForInvestment_N1;

//				Added By Saurabh Jain on 14/10/2019 End
                sum_N += _amountAvailableForInvestment_N;
                if ((_month_E % 12) == 0) {

                    bussIll.append("<AmtAviFrInv").append(_year_F).append(">").append(Math.round(sum_N1)).append("</AmtAviFrInv").append(_year_F).append(">");
                    sum_N1 = 0;
                }
                BIMAST.setSumAssuredRelatedCharges_O(policyTerm, sumAssured,
                        prop.charge_SumAssuredBase);
                sumAssuredRelatedCharges_O = Double.parseDouble(BIMAST.getSumAssuredRelatedCharges_O());
                _sumAssuredRelatedCharges_O = sumAssuredRelatedCharges_O;
                // System.out.println("11. sumAssuredRelatedCharges_O "+BIMAST.getSumAssuredRelatedCharges_O());
                // System.out.println("11. sumAssuredRelatedCharges_O "+sumAssuredRelatedCharges_O);

                // System.out.println("12. RiderCharges_P "+riderCharges_P);

                BIMAST.setPolicyAdministrationCharge_Q(_policyAdministrationCharges_Q, prop.charge_Inflation, prop.fixedMonthlyExp_SP, prop.fixedMonthlyExp_RP, prop.inflation_pa_SP, prop.inflation_pa_RP, planType);
                policyAdministrationCharges_Q = Double.parseDouble(BIMAST.getPolicyAdministrationCharge_Q());
                _policyAdministrationCharges_Q = policyAdministrationCharges_Q;
                // System.out.println("13. policyAdministrationCharges_Q "+BIMAST.getPolicyAdministrationCharge_Q());
                // System.out.println("13. policyAdministrationCharges_Q "+policyAdministrationCharges_Q);

                sum_Q += _policyAdministrationCharges_Q;
                sum_02 = sum_Q;
                if ((_month_E % 12) == 0) {

                    bussIll.append("<PolAdminChrg").append(_year_F).append(">").append(Math.round(sum_Q)).append("</PolAdminChrg").append(_year_F).append(">");
                    sum_Q = 0;
                }

                BIMAST.setOneHundredPercentOfCummulativePremium_AW(_oneHundredPercentOfCummulativePremium_AW);
                oneHundredPercentOfCummulativePremium_AW = Double.parseDouble(BIMAST.getOneHundredPercentOfCummulativePremium_AW());
                _oneHundredPercentOfCummulativePremium_AW = oneHundredPercentOfCummulativePremium_AW;
                // System.out.println("45. oneHundredPercentOfCummulativePremium_AW "+BIMAST.getOneHundredPercentOfCummulativePremium_AW());
                // System.out.println("45. oneHundredPercentOfCummulativePremium_AW "+oneHundredPercentOfCummulativePremium_AW);

                BIMAST.setMortalityCharges_R(_fundValueAtEnd_AB, policyTerm, forBIArray, sumAssured, prop.mortalityCharges);
                mortalityCharges_R = Double.parseDouble(BIMAST.getMortalityCharges_R());
                _mortalityCharges_R = mortalityCharges_R;
                // System.out.println("14. mortalityCharges_R "+BIMAST.getMortalityCharges_R());
                // System.out.println("14. mortalityCharges_R "+mortalityCharges_R);

                //
                // if(_year_F==25)
                // {
//					System.out.println("month "+ _month_E +"="+_mortalityCharges_R);
                // }


                sum_R += _mortalityCharges_R;
                if ((_month_E % 12) == 0) {

                    // System.out.println("SUM="+sum_R);

                    bussIll.append("<MortChrg4Pr").append(_year_F).append(">").append(Math.round(sum_R)).append("</MortChrg4Pr").append(_year_F).append(">");
                    sum_R = 0;
                }

                BIMAST.setTotalCharges_S(policyTerm, riderCharges_P);
                totalCharges_S = Double.parseDouble(BIMAST.getTotalCharges_S());
                _totalCharges_S = totalCharges_S;
                // System.out.println("15. totalCharges_S "+BIMAST.getTotalCharges_S());
                // System.out.println("15. totalCharges_S "+totalCharges_S);

                sum_S += _totalCharges_S;
                if ((_month_E % 12) == 0) {

                    bussIll.append("<TotCharg4Pr").append(_year_F).append(">").append(Math.round(sum_S)).append("</TotCharg4Pr").append(_year_F).append(">");
                    sum_S = 0;
                }

                BIMAST.setServiceTax_exclOfSTonAllocAndSurr_T(serviceTax,
                        prop.mortalityAndRiderCharges,
                        prop.administrationCharges);
                serviceTaxExclOfSTOnAllocAndSurr_T = Double.parseDouble(BIMAST.getServiceTax_exclOfSTonAllocAndSurr_T());
                _serviceTaxExclOfSTOnAllocAndSurr_T = serviceTaxExclOfSTOnAllocAndSurr_T;
                // System.out.println("16. serviceTaxExclOfSTOnAllocAndSurr_T "+BIMAST.getServiceTax_exclOfSTonAllocAndSurr_T());
                // System.out.println("16. serviceTaxExclOfSTOnAllocAndSurr_T "+serviceTaxExclOfSTOnAllocAndSurr_T);

                BIMAST.setAdditionToFundIfAny_V(_fundValueAtEnd_AB, policyTerm, riderCharges_P);
                additionToFundIfAny_V = Double.parseDouble(BIMAST.getAdditionToFundIfAny_V());
                _additionToFundIfAny_V = additionToFundIfAny_V;
                // System.out.println("18. additionToFundIfAny_V "+BIMAST.getAdditionToFundIfAny_V());
                // System.out.println("18. additionToFundIfAny_V "+additionToFundIfAny_V);

//				Added By Saurabh Jain on 16/10/2019 Start

                sum_Y += _additionToFundIfAny_V;
                if ((_month_E % 12) == 0) {
                    _sum_Y = Math.round(sum_Y);
                    bussIll.append("<AddToTheFund4Pr").append(_year_F).append(">").append(_sum_Y).append("</AddToTheFund4Pr").append(_year_F).append(">");
                    sum_Y = 0;
                }
//				Added By Saurabh Jain on 16/10/2019 End

                BIMAST.setFundBeforeFMC_W(_fundValueAtEnd_AB, policyTerm, riderCharges_P);
                fundBeforeFMC_W = Double.parseDouble(BIMAST.getFundBeforeFMC_W());
                _fundBeforeFMC_W = fundBeforeFMC_W;
                // System.out.println("19. fundBeforeFMC_W "+BIMAST.getFundBeforeFMC_W());
                // System.out.println("19. fundBeforeFMC_W "+fundBeforeFMC_W);


                BIMAST.setFundManagementCharge_X(policyTerm, percentToBeInvested_EquityFund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_BondFund, percentToBeInvested_MoneyMarketFund, percentToBeInvested_Top300Fund, percentToBeInvested_BondOptimiserFund, percentToBeInvested_MidcapFund, percentToBeInvested_PureFund, percentToBeInvested_CorpBondFund);
                fundManagementCharge_X = Double.parseDouble(BIMAST
                        .getFundManagementCharge_X());
                _fundManagementCharge_X = fundManagementCharge_X;
                // System.out.println("20. fundManagementCharge_X "+BIMAST.getFundManagementCharge_X());
                // System.out.println("20. fundManagementCharge_X "+fundManagementCharge_X);

                sum_X += _fundManagementCharge_X;
                sum_03 = sum_X;
                if ((_month_E % 12) == 0) {
                    _sum_X = Math.round(sum_X);
                    bussIll.append("<FundMgmtCharg4Pr").append(_year_F).append(">").append(_sum_X).append("</FundMgmtCharg4Pr").append(_year_F).append(">");
                    sum_X = 0;
                }

                otherCharges_PartA = sum_01 + sum_02 + sum_03;

                if ((_month_E % 12) == 0) {

                    bussIll.append("<OtherCharges4Pr_PartA").append(_year_F).append(">").append(commonForAllProd.getRound(String
                            .valueOf(otherCharges_PartA))).append("</OtherCharges4Pr_PartA").append(_year_F).append(">");
                    otherCharges_PartA = 0;
                }

                if ((_month_E % 12) == 0) {

                    bussIll.append("<OtherCharges4Pr_PartB").append(_year_F).append(">").append(commonForAllProd.getRound(String
                            .valueOf(otherCharges_PartB))).append("</OtherCharges4Pr_PartB").append(_year_F).append(">");
                    otherCharges_PartB = 0;
                }


                BIMAST.setServiceTaxOnFMC_Y(percentToBeInvested_EquityFund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_BondFund, percentToBeInvested_MoneyMarketFund, percentToBeInvested_Top300Fund, serviceTax, percentToBeInvested_BondOptimiserFund, percentToBeInvested_MidcapFund, percentToBeInvested_PureFund, percentToBeInvested_CorpBondFund);
                serviceTaxOnFMC_Y = Double.parseDouble(BIMAST.getServiceTaxOnFMC_Y());
                _serviceTaxOnFMC_Y = serviceTaxOnFMC_Y;
                // System.out.println("21. serviceTaxOnFMC_Y "+BIMAST.getServiceTaxOnFMC_Y());
                // System.out.println("21. serviceTaxOnFMC_Y "+serviceTaxOnFMC_Y);

                BIMAST.setFundValueAfterFMCAndBeforeGA_Z(policyTerm);
                fundValueAfterFMCBerforeGA_Z = Double.parseDouble(commonForAllProd.getRoundUp(BIMAST.getFundValueAfterFMCAndBeforeGA_Z()));
                _fundValueAfterFMCBerforeGA_Z = fundValueAfterFMCBerforeGA_Z;
                // System.out.println("22. fundValueAfterFMCBerforeGA_Z "+BIMAST.getFundValueAfterFMCAndBeforeGA_Z());
                // System.out.println("22. fundValueAfterFMCBerforeGA_Z "+fundValueAfterFMCBerforeGA_Z);

                BIMAST.setTotalServiceTax_U(riderCharges_P, serviceTax);
                totalServiceTax_U = Double.parseDouble(BIMAST
                        .getTotalServiceTax_U());
                _totalServiceTax_U = totalServiceTax_U;
                // System.out.println("17. totalServiceTax_U "+BIMAST.getTotalServiceTax_U());
                // System.out.println("17. totalServiceTax_U "+totalServiceTax_U);

                sum_U += _totalServiceTax_U;
                if ((_month_E % 12) == 0) {
                    bussIll.append("<TotServTax4Pr").append(_year_F).append(">").append(Math.round(sum_U)).append("</TotServTax4Pr").append(_year_F).append(">");
                    sum_U = 0;
                }

                BIMAST.setGuaranteedAddition_AA(planType, effectivePremium, PPT);
                guaranteedAddition_AA = Double.parseDouble(BIMAST
                        .getGuaranteedAddition_AA());
                _guaranteedAddition_AA = guaranteedAddition_AA;
                // System.out.println("23. guaranteedAddition_AA "+BIMAST.getGuaranteedAddition_AA());
                // System.out.println("23. guaranteedAddition_AA "+guaranteedAddition_AA);


                sum_AA += _guaranteedAddition_AA;
                if ((_month_E % 12) == 0) {
                    bussIll.append("<GuarntAdd4Pr").append(_year_F).append(">").append(Math.round(sum_AA)).append("</GuarntAdd4Pr").append(_year_F).append(">");
                    sum_AA = 0;
                }

                BIMAST.setFundValueAtEnd_AB();
                fundValueAtEnd_AB = Double.parseDouble(BIMAST
                        .getFundValueAtEnd_AB());
                _fundValueAtEnd_AB = fundValueAtEnd_AB;
                // System.out.println("24. fundValueAtEnd_AB "+BIMAST.getFundValueAtEnd_AB());
//				System.out.println(month_E + " fundValueAtEnd_AB "+fundValueAtEnd_AB);

                if ((_month_E % 12) == 0) {
                    bussIll.append("<FundValAtEnd4Pr").append(_year_F).append(">").append(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(_fundValueAtEnd_AB))).append("</FundValAtEnd4Pr").append(_year_F).append(">");

                }

                if ((_month_E % 12) == 0) {
                    //					double temp = Math.round(Double.parseDouble(commonForAllProd.getRoundUp(String.valueOf(_fundValueAtEnd_AC))) + _sum_Y);
                    //					System.out.println(commonForAllProd.getRoundUp(String.valueOf(_fundValueAtEnd_AC)) + " + " + _sum_Y + " = " + temp);
                    bussIll.append("<FundBefFMC4Pr").append(_year_F).append(">").append(Math.round(Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(_fundValueAtEnd_AB))) + _sum_X)).append("</FundBefFMC4Pr").append(_year_F).append(">");
                }


                BIMAST.setSurrenderCap_AV(effectivePremium, planType);
                surrenderCap_AV = Double.parseDouble(BIMAST.getSurrenderCap_AV());
                _surrenderCap_AV = surrenderCap_AV;
                // System.out.println("44. surrenderCap_AV "+BIMAST.getSurrenderCap_AV());
                // System.out.println("44. surrenderCap_AV "+surrenderCap_AV);

                BIMAST.setSurrenderCharges_AC(effectivePremium, PPT, planType);
                surrenderCharges_AC = Double.parseDouble(BIMAST.getSurrenderCharges_AC());
                _surrenderCharges_AC = surrenderCharges_AC;
                // System.out.println("25. surrenderCharges_AC "+BIMAST.getSurrenderCharges_AC());
                // System.out.println("25. surrenderCharges_AC "+surrenderCharges_AC);

                BIMAST.setServiceTaxOnSurrenderCharges_AD(serviceTax);
                serviceTaxOnSurrenderCharges_AD = Double.parseDouble(BIMAST.getServiceTaxOnSurrenderCharges_AD());
                _serviceTaxOnSurrenderCharges_AD = serviceTaxOnSurrenderCharges_AD;
                // System.out.println("26. serviceTaxOnSurrenderCharges_AD "+BIMAST.getServiceTaxOnSurrenderCharges_AD());
                // System.out.println("26. serviceTaxOnSurrenderCharges_AD "+serviceTaxOnSurrenderCharges_AD);

                BIMAST.setSurrenderValue_AE();
                surrenderValue_AE = Double.parseDouble(BIMAST
                        .getSurrenderValue_AE());
                _surrenderValue_AE = surrenderValue_AE;
                // System.out.println("27. surrenderValue_AE "+BIMAST.getSurrenderValue_AE());
                // System.out.println("27. surrenderValue_AE "+surrenderValue_AE);

                if ((_month_E % 12) == 0) {
                    bussIll.append("<SurrVal4Pr").append(_year_F).append(">").append(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(_surrenderValue_AE))).append("</SurrVal4Pr").append(_year_F).append(">");

                }

                BIMAST.setDeathBenefit_AF(policyTerm, sumAssured);
                deathBenefit_AF = Double.parseDouble(BIMAST
                        .getDeathBenefit_AF());
                _deathBenefit_AF = deathBenefit_AF;
                // System.out.println("28. deathBenefit_AF "+BIMAST.getDeathBenefit_AF());
                // System.out.println("28. deathBenefit_AF "+deathBenefit_AF);

                if ((_month_E % 12) == 0) {

                    bussIll.append("<DeathBen4Pr").append(_year_F).append(">").append(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(_deathBenefit_AF))).append("</DeathBen4Pr").append(_year_F).append(">");

                }

                BIMAST.setMortalityCharges_AG(_fundValueAtEnd_AQ, policyTerm, forBIArray, sumAssured, prop.mortalityCharges);
                mortalityCharges_AG = Double.parseDouble(BIMAST.getMortalityCharges_AG());
                _mortalityCharges_AG = mortalityCharges_AG;
                // System.out.println("29. mortalityCharges_AG "+BIMAST.getMortalityCharges_AG());
                // System.out.println("29. mortalityCharges_AG "+mortalityCharges_AG);


                sum_AG += _mortalityCharges_AG;
                if ((_month_E % 12) == 0) {

                    bussIll.append("<MortChrg8Pr").append(_year_F).append(">").append(Math.round(sum_AG)).append("</MortChrg8Pr").append(_year_F).append(">");
                    sum_AG = 0;
                }

                BIMAST.setTotalCharges_AH(policyTerm, riderCharges_P);
                totalCharges_AH = Double.parseDouble(BIMAST
                        .getTotalCharges_AH());
                _totalCharges_AH = totalCharges_AH;
                // System.out.println("30. totalCharges_AH "+BIMAST.getTotalCharges_AH());
                // System.out.println("30. totalCharges_AH "+totalCharges_AH);

                sum_AH += _totalCharges_AH;
                if ((_month_E % 12) == 0) {
                    bussIll.append("<TotCharg8Pr").append(_year_F).append(">").append(Math.round(sum_AH)).append("</TotCharg8Pr").append(_year_F).append(">");
                    sum_AH = 0;
                }

                BIMAST.setServiceTax_exclOfSTonAllocAndSurr_AI(serviceTax,
                        prop.mortalityAndRiderCharges,
                        prop.administrationCharges);
                serviceTaxExclOfSTOnAllocAndSurr_AI = Double.parseDouble(BIMAST.getServiceTax_exclOfSTonAllocAndSurr_AI());
                _serviceTaxExclOfSTOnAllocAndSurr_AI = serviceTaxExclOfSTOnAllocAndSurr_AI;
                // System.out.println("31. serviceTaxExclOfSTOnAllocAndSurr_AI "+BIMAST.getServiceTax_exclOfSTonAllocAndSurr_AI());
                // System.out.println("31. serviceTaxExclOfSTOnAllocAndSurr_AI "+serviceTaxExclOfSTOnAllocAndSurr_AI);

                BIMAST.setAdditionToFundIfAny_AK(_fundValueAtEnd_AQ, policyTerm, riderCharges_P);
                additionToFundIfAny_AK = Double.parseDouble(BIMAST.getAdditionToFundIfAny_AK());
                _additionToFundIfAny_AK = additionToFundIfAny_AK;
                // System.out.println("33. additionToFundIfAny_AK "+BIMAST.getAdditionToFundIfAny_AK());
                // System.out.println("33. additionToFundIfAny_AK "+additionToFundIfAny_AK);

//				Added By Saurabh Jain on 16/10/2019 Start

                sum_Z += _additionToFundIfAny_AK;
                if ((_month_E % 12) == 0) {
                    _sum_Z = Math.round(sum_Z);
                    bussIll.append("<AddToTheFund8Pr").append(_year_F).append(">").append(_sum_Z).append("</AddToTheFund8Pr").append(_year_F).append(">");
                    sum_Z = 0;
                }
//				Added By Saurabh Jain on 16/10/2019 End

                BIMAST.setFundBeforeFMC_AL(_fundValueAtEnd_AQ, policyTerm, riderCharges_P);
                fundBeforeFMC_AL = Double.parseDouble(BIMAST.getFundBeforeFMC_AL());
                _fundBeforeFMC_AL = fundBeforeFMC_AL;
                // System.out.println("34. fundBeforeFMC_AL "+BIMAST.getFundBeforeFMC_AL());
                // System.out.println("34. fundBeforeFMC_AL "+fundBeforeFMC_AL);

                BIMAST.setFundManagementCharge_AM(policyTerm, percentToBeInvested_EquityFund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_BondFund, percentToBeInvested_MoneyMarketFund, percentToBeInvested_Top300Fund, percentToBeInvested_BondOptimiserFund, percentToBeInvested_MidcapFund, percentToBeInvested_PureFund, percentToBeInvested_CorpBondFund);
                fundManagementCharge_AM = Double.parseDouble(BIMAST.getFundManagementCharge_AM());
                _fundManagementCharge_AM = fundManagementCharge_AM;
                // System.out.println("35. fundManagementCharge_AM "+BIMAST.getFundManagementCharge_AM());
                // System.out.println("35. fundManagementCharge_AM "+fundManagementCharge_AM);


                sum_AM += _fundManagementCharge_AM;
                sum1_03 = sum_AM;

                if ((_month_E % 12) == 0) {
                    _sum_AM = Math.round(sum_AM);
                    bussIll.append("<FundMgmtCharg8Pr").append(_year_F).append(">").append(_sum_AM).append("</FundMgmtCharg8Pr").append(_year_F).append(">");
                    sum_AM = 0;
                }

                otherCharges1_PartA = sum_01 + sum_02 + sum1_03;

                if ((_month_E % 12) == 0) {

                    bussIll.append("<OtherCharges8Pr_PartA").append(_year_F).append(">").append(commonForAllProd.getRound(String
                            .valueOf(otherCharges1_PartA))).append("</OtherCharges8Pr_PartA").append(_year_F).append(">");
                    otherCharges1_PartA = 0;
                }

                if ((_month_E % 12) == 0) {

                    bussIll.append("<OtherCharges8Pr_PartB").append(_year_F).append(">").append(commonForAllProd.getRound(String
                            .valueOf(otherCharges1_PartB))).append("</OtherCharges8Pr_PartB").append(_year_F).append(">");
                    otherCharges1_PartB = 0;
                }

                BIMAST.setServiceTaxOnFMC_AN(percentToBeInvested_EquityFund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_BondFund, percentToBeInvested_MoneyMarketFund, percentToBeInvested_Top300Fund, serviceTax, percentToBeInvested_BondOptimiserFund, percentToBeInvested_MidcapFund, percentToBeInvested_PureFund, percentToBeInvested_CorpBondFund);
                serviceTaxOnFMC_AN = Double.parseDouble(BIMAST.getServiceTaxOnFMC_AN());
                _serviceTaxOnFMC_AN = serviceTaxOnFMC_AN;
                // System.out.println("36. serviceTaxOnFMC_AN "+BIMAST.getServiceTaxOnFMC_AN());
                // System.out.println("36. serviceTaxOnFMC_AN "+serviceTaxOnFMC_AN);

                BIMAST.setFundValueAfterFMCAndBeforeGA_AO(policyTerm);
                fundValueAfterFMCBerforeGA_AO = Double.parseDouble(BIMAST.getFundValueAfterFMCAndBeforeGA_AO());
                _fundValueAfterFMCBerforeGA_AO = fundValueAfterFMCBerforeGA_AO;
                // System.out.println("37. fundValueAfterFMCBerforeGA_AO "+BIMAST.getFundValueAfterFMCAndBeforeGA_AO());
                // System.out.println("37. fundValueAfterFMCBerforeGA_AO "+fundValueAfterFMCBerforeGA_AO);

                BIMAST.setTotalServiceTax_AJ(riderCharges_P, serviceTax);
                totalServiceTax_AJ = Double.parseDouble(BIMAST
                        .getTotalServiceTax_AJ());
                _totalServiceTax_AJ = totalServiceTax_AJ;
                // System.out.println("32. totalServiceTax_AJ "+BIMAST.getTotalServiceTax_AJ());
                // System.out.println("32. totalServiceTax_AJ "+totalServiceTax_AJ);


                sum_AJ += _totalServiceTax_AJ;
                if ((_month_E % 12) == 0) {

                    bussIll.append("<TotServTxOnCharg8Pr").append(_year_F).append(">").append(Math.round(sum_AJ)).append("</TotServTxOnCharg8Pr").append(_year_F).append(">");
                    sum_AJ = 0;
                }

                BIMAST.setGuaranteedAddition_AP(planType, effectivePremium, PPT);
                guaranteedAddition_AP = Double.parseDouble(BIMAST
                        .getGuaranteedAddition_AP());
                _guaranteedAddition_AP = guaranteedAddition_AP;
                // System.out.println("38. guaranteedAddition_AP "+BIMAST.getGuaranteedAddition_AP());
                // System.out.println("38. guaranteedAddition_AP "+guaranteedAddition_AP);

                sum_AP += _guaranteedAddition_AP;
                if ((_month_E % 12) == 0) {
                    bussIll.append("<GuarntAdd8Pr").append(_year_F).append(">").append(Math.round(sum_AP)).append("</GuarntAdd8Pr").append(_year_F).append(">");
                    sum_AP = 0;
                }

                BIMAST.setFundValueAtEnd_AQ();
                fundValueAtEnd_AQ = Double.parseDouble(BIMAST
                        .getFundValueAtEnd_AQ());
                _fundValueAtEnd_AQ = fundValueAtEnd_AQ;
                // System.out.println("39. fundValueAtEnd_AQ "+BIMAST.getFundValueAtEnd_AQ());
                // System.out.println("39. fundValueAtEnd_AQ "+fundValueAtEnd_AQ);

                if ((_month_E % 12) == 0) {
                    bussIll.append("<FundValAtEnd8Pr").append(_year_F).append(">").append(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(_fundValueAtEnd_AQ))).append("</FundValAtEnd8Pr").append(_year_F).append(">");
                }

                if ((_month_E % 12) == 0) {
                    //					double temp = Math.round(Double.parseDouble(commonForAllProd.getRoundUp(String.valueOf(_fundValueAtEnd_AC))) + _sum_Y);
                    //					System.out.println(commonForAllProd.getRoundUp(String.valueOf(_fundValueAtEnd_AC)) + " + " + _sum_Y + " = " + temp);
                    bussIll.append("<FundBefFMC8Pr").append(_year_F).append(">").append(Math.round(Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(_fundValueAtEnd_AQ))) + _sum_AM)).append("</FundBefFMC8Pr").append(_year_F).append(">");
                }


                BIMAST.setSurrenderCharges_AR(effectivePremium, PPT, planType);
                surrenderCharges_AR = Double.parseDouble(BIMAST.getSurrenderCharges_AR());
                _surrenderCharges_AR = surrenderCharges_AR;
                // System.out.println("40. surrenderCharges_AR "+BIMAST.getSurrenderCharges_AR());
                // System.out.println("40. surrenderCharges_AR "+surrenderCharges_AR);

                BIMAST.setServiceTaxOnSurrenderCharges_AS(serviceTax);
                serviceTaxOnSurrenderCharges_AS = Double.parseDouble(BIMAST.getServiceTaxOnSurrenderCharges_AS());
                _serviceTaxOnSurrenderCharges_AS = serviceTaxOnSurrenderCharges_AS;
                // System.out.println("41. serviceTaxOnSurrenderCharges_AS "+BIMAST.getServiceTaxOnSurrenderCharges_AS());
                // System.out.println("41. serviceTaxOnSurrenderCharges_AS "+serviceTaxOnSurrenderCharges_AS);

                BIMAST.setSurrenderValue_AT();
                surrenderValue_AT = Double.parseDouble(BIMAST
                        .getSurrenderValue_AT());
                _surrenderValue_AT = surrenderValue_AT;
                // System.out.println("42. surrenderValue_AT "+BIMAST.getSurrenderValue_AT());
                // System.out.println("42. surrenderValue_AT "+surrenderValue_AT);

                if ((_month_E % 12) == 0) {
                    bussIll.append("<SurrVal8Pr").append(_year_F).append(">").append(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(_surrenderValue_AT))).append("</SurrVal8Pr").append(_year_F).append(">");
                }

                BIMAST.setDeathBenefit_AU(policyTerm, sumAssured);
                deathBenefit_AU = Double.parseDouble(BIMAST
                        .getDeathBenefit_AU());
                _deathBenefit_AU = deathBenefit_AU;
                // System.out.println("43. deathBenefit_AU "+BIMAST.getDeathBenefit_AU());
                // System.out.println("43. deathBenefit_AU "+deathBenefit_AU);

                if ((_month_E % 12) == 0) {

                    bussIll.append("<DeathBen8Pr").append(_year_F).append(">").append(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(_deathBenefit_AU))).append("</DeathBen8Pr").append(_year_F).append(">");

                }

                if ((_month_E % 12) == 0) {
                    Commission_BL = BIMAST.getCommission_BL(_sum_I, _sum_J,
                            smartWealthBuilderBean);
                    bussIll.append("<CommIfPay8Pr").append(_year_F).append(">").append(Math.round(Commission_BL)).append("</CommIfPay8Pr").append(_year_F).append(">");

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            // System.out.println("** error **" + e.getMessage());
        }
        return new String[]{
                (commonForAllProd.getStringWithout_E(sumAssured)),
                commonForAllProd.getRoundUp(commonForAllProd
                        .getStringWithout_E(fundValueAtEnd_AB)),
                commonForAllProd.getRoundUp(commonForAllProd
                        .getStringWithout_E(fundValueAtEnd_AQ))};
    }

    //* Output end

    /******************************************************* Start of Reduction in Yield **********************/

    private String[] getOutputReductionInYield(String SheetName,
                                               SmartWealthBuilderBean smartWealthBuilderBean) {
        commonForAllProd = new CommonForAllProd();
        SmartWealthBuilderProperties prop = new SmartWealthBuilderProperties();
        ArrayList<String> List_BD = new ArrayList<>();
        ArrayList<String> List_BI = new ArrayList<>();
        // output variable declaration
        int _month_E = 0, _year_F, _age_H = 0, _month_BB = 0;
        String _policyInforce_G = "Y";

        double _premium_I = 0,
                _topUpPremium_J = 0,
                _premiumAllocationCharge_K = 0,
                _topUpCharges_L = 0,
                _ServiceTaxOnAllocation_M = 0,
                _amountAvailableForInvestment_N = 0,
                _sumAssuredRelatedCharges_O = 0,
                _riderCharges_P = 0,
                _policyAdministrationCharges_Q = 0,
                _mortalityCharges_R = 0,
                _totalCharges_S = 0,
                _serviceTaxExclOfSTOnAllocAndSurr_T = 0,
                _totalServiceTax_U = 0,
                _additionToFundIfAny_V = 0,
                _fundBeforeFMC_W = 0,
                _fundManagementCharge_X = 0,
                _serviceTaxOnFMC_Y = 0,
                _fundValueAfterFMCBerforeGA_Z = 0,
                _guaranteedAddition_AA = 0,
                _fundValueAtEnd_AB = 0,
                _surrenderCharges_AC = 0,
                _serviceTaxOnSurrenderCharges_AD = 0,
                _surrenderValue_AE = 0,
                _deathBenefit_AF = 0,
                _mortalityCharges_AG = 0,
                _totalCharges_AH = 0,
                _serviceTaxExclOfSTOnAllocAndSurr_AI = 0,
                _totalServiceTax_AJ = 0,
                _additionToFundIfAny_AK = 0,
                _fundBeforeFMC_AL = 0,
                _fundManagementCharge_AM = 0,
                _serviceTaxOnFMC_AN = 0,
                _fundValueAfterFMCBerforeGA_AO = 0,
                _guaranteedAddition_AP = 0,
                _fundValueAtEnd_AQ = 0,
                _surrenderCharges_AR = 0,
                _serviceTaxOnSurrenderCharges_AS = 0,
                _surrenderValue_AT = 0,
                _deathBenefit_AU = 0,
                _surrenderCap_AV = 0,
                _oneHundredPercentOfCummulativePremium_AW = 0,
                _reductionYield_BI = 0,
                _reductionYield_BD = 0,
                _irrAnnual_BD = 0,
                _irrAnnual_BI = 0,
                _reductionInYieldMaturityAt = 0,
                _reductionInYieldNumberOfYearsElapsedSinceInception = 0;

        // temp variable declaration.
        int month_E, year_F, age_H, month_BB;

        String policyInforce_G;

        double premium_I,
                topUpPremium_J,
                premiumAllocationCharge_K,
                topUpCharges_L,
                ServiceTaxOnAllocation_M,
                amountAvailableForInvestment_N,
                sumAssuredRelatedCharges_O,
                riderCharges_P = 0,
                policyAdministrationCharges_Q,
                mortalityCharges_R,
                totalCharges_S,
                serviceTaxExclOfSTOnAllocAndSurr_T,
                totalServiceTax_U,
                additionToFundIfAny_V,
                fundBeforeFMC_W,
                fundManagementCharge_X,
                serviceTaxOnFMC_Y,
                fundValueAfterFMCBerforeGA_Z,
                guaranteedAddition_AA,
                fundValueAtEnd_AB,
                surrenderCharges_AC,
                serviceTaxOnSurrenderCharges_AD,
                surrenderValue_AE,
                deathBenefit_AF,
                mortalityCharges_AG,
                totalCharges_AH,
                serviceTaxExclOfSTOnAllocAndSurr_AI,
                totalServiceTax_AJ,
                additionToFundIfAny_AK,
                fundBeforeFMC_AL,
                fundManagementCharge_AM,
                serviceTaxOnFMC_AN,
                fundValueAfterFMCBerforeGA_AO,
                guaranteedAddition_AP,
                fundValueAtEnd_AQ,
                surrenderCharges_AR,
                serviceTaxOnSurrenderCharges_AS,
                surrenderValue_AT,
                deathBenefit_AU,
                surrenderCap_AV,
                oneHundredPercentOfCummulativePremium_AW,
                reductionYield_BI,
                reductionYield_BD,
                irrAnnual_BD,
                irrAnnual_BI,
                reductionInYieldMaturityAt = 0,
                reductionInYieldNumberOfYearsElapsedSinceInception;

        // from GUI inputs
        boolean StaffDisc = smartWealthBuilderBean.getIsForStaffOrNot();
        boolean bancAssuranceDisc = false;
        int ageAtEntry = smartWealthBuilderBean.getAgeAtEntry();
        int policyTerm = smartWealthBuilderBean.getPolicyTerm_Basic();
        String planType = smartWealthBuilderBean.getPlanType();
        String premFreq = smartWealthBuilderBean.getPremFreq();
        int premiumPayingTerm = smartWealthBuilderBean.getPremiumPayingTerm();
        //double serviceTax =smartWealthBuilderBean.getServiceTax();
        double serviceTax;
        boolean isKerlaDisc = smartWealthBuilderBean.isKerlaDisc();
        double SAMF = smartWealthBuilderBean.getSAMF();
        double percentToBeInvested_EquityFund = smartWealthBuilderBean
                .getPercentToBeInvested_EquityFund();
        double percentToBeInvested_EquityOptFund = smartWealthBuilderBean
                .getPercentToBeInvested_EquityOptFund();
        double percentToBeInvested_GrowthFund = smartWealthBuilderBean
                .getPercentToBeInvested_GrowthFund();
        double percentToBeInvested_BalancedFund = smartWealthBuilderBean
                .getPercentToBeInvested_BalancedFund();
        double percentToBeInvested_BondFund = smartWealthBuilderBean
                .getPercentToBeInvested_BondFund();
        double percentToBeInvested_MoneyMarketFund = smartWealthBuilderBean
                .getPercentToBeInvested_MoneyMarketFund();
        double percentToBeInvested_Top300Fund = smartWealthBuilderBean
                .getPercentToBeInvested_Top300Fund();
        int noOfYearsElapsedSinceInception = smartWealthBuilderBean
                .getNoOfYearsElapsedSinceInception();
        double percentToBeInvested_BondOptimiserFund = smartWealthBuilderBean.getPercentToBeInvested_BondOptimiserFund();
        double percentToBeInvested_MidcapFund = smartWealthBuilderBean.getPercentToBeInvested_MidcapFund();
        double percentToBeInvested_PureFund = smartWealthBuilderBean.getPercentToBeInvested_PureFund();
        double percentToBeInvested_CorpBondFund = smartWealthBuilderBean.getPercentToBeInvested_CorpBondFund();


        String transferFundStatus = "No Transfer";
        String addTopUp = "No";

        double effectivePremium = smartWealthBuilderBean.getEffectivePremium();
        int PF = smartWealthBuilderBean.getPF();
        int PPT = calPPT(premiumPayingTerm);
        double effectiveTopUpPrem = smartWealthBuilderBean
                .getEffectiveTopUpPrem(); // *Sheet Name -> Input,*Cell -> Y70
        double sumAssured = (effectivePremium * SAMF);

        SmartWealthBuilderBusinessLogic BIMAST = new SmartWealthBuilderBusinessLogic();
        String[] forBIArray = BIMAST
                .getForBIArr(prop.mortalityCharges_AsPercentOfofLICtable);
        // Added Akshaya on 18-Feb-2014
        double netYield8pa = 0;
        int rowNumber = 0, monthNumber = 0;

        try {
            for (int i = 0; i <= (policyTerm * 12); i++)
            // for(int i=0;i<1;i++)
            {
                rowNumber++;

                BIMAST.setMonth_E(rowNumber);
                month_E = Integer.parseInt(BIMAST.getMonth_E());
                _month_E = month_E;
                // System.out.println("1. month_E "+BIMAST.getMonth_E());
                // System.out.println("1. month_E "+month_E);

                BIMAST.setYear_F();
                year_F = Integer.parseInt(BIMAST.getYear_F());
                _year_F = year_F;
                // System.out.println("2. year_F "+BIMAST.getYear_F());
                // System.out.println("2. year_F "+year_F);
                if (isKerlaDisc && _year_F <= 2) {
                    serviceTax = 0.19;
                } else {
                    serviceTax = 0.18;
                }
                policyInforce_G = BIMAST.getPolicyInForce_G();
                _policyInforce_G = policyInforce_G;
                // System.out.println("3. policyInforce_G "+BIMAST.getPolicyInForce_G());
                // System.out.println("3. policyInforce_G "+policyInforce_G);

                BIMAST.setAge_H(ageAtEntry);
                age_H = Integer.parseInt(BIMAST.getAge_H());
                _age_H = age_H;
                // System.out.println("4. age_H "+BIMAST.getAge_H());
                // System.out.println("4. age_H "+age_H);

                BIMAST.setPremium_I(PPT, PF, effectivePremium);
                premium_I = Double.parseDouble(BIMAST.getPremium_I());
                _premium_I = premium_I;
                // System.out.println("5. premium_I "+BIMAST.getPremium_I());
                // System.out.println("5. premium_I "+premium_I);

                BIMAST.setTopUpPremium_J(prop.topUpStatusYield,
                        effectiveTopUpPrem);
                topUpPremium_J = Double.parseDouble(BIMAST.getTopUpPremium_J());
                _topUpPremium_J = topUpPremium_J;
                // System.out.println("6. topUpPremium_J "+BIMAST.getTopUpPremium_J());

                BIMAST.setPremiumAllocationCharge_K(StaffDisc,
                        bancAssuranceDisc, planType, PPT, effectivePremium);
                premiumAllocationCharge_K = Double.parseDouble(BIMAST.getPremiumAllocationCharge_K());
                _premiumAllocationCharge_K = premiumAllocationCharge_K;
                // System.out.println("7. premiumAllocationCharge_K "+BIMAST.getPremiumAllocationCharge_K());

                BIMAST.setTopUpCharges_L(prop.topUp);
                topUpCharges_L = Double.parseDouble(BIMAST.getTopUpCharges_L());
                _topUpCharges_L = topUpCharges_L;
                // System.out.println("8. topUpCharges_L "+topUpCharges_L);

                BIMAST.setServiceTaxOnAllocation_M(prop.allocationChargesYield,
                        serviceTax);
                ServiceTaxOnAllocation_M = Double.parseDouble(BIMAST.getServiceTaxOnAllocation_M());
                _ServiceTaxOnAllocation_M = ServiceTaxOnAllocation_M;
                // System.out.println("9. ServiceTaxOnAllocation_M "+ServiceTaxOnAllocation_M);

                BIMAST.setAmountAvailableForInvestment_N();
                amountAvailableForInvestment_N = Double.parseDouble(BIMAST.getAmountAvailableForInvestment_N());
                _amountAvailableForInvestment_N = amountAvailableForInvestment_N;
                // System.out.println("10. amountAvailableForInvestment_N "+amountAvailableForInvestment_N);

                BIMAST.setSumAssuredRelatedCharges_O(policyTerm, sumAssured,
                        prop.charge_SumAssuredBase);
                sumAssuredRelatedCharges_O = Double.parseDouble(BIMAST.getSumAssuredRelatedCharges_O());
                _sumAssuredRelatedCharges_O = sumAssuredRelatedCharges_O;
                // System.out.println("11. sumAssuredRelatedCharges_O "+sumAssuredRelatedCharges_O);

                // System.out.println("12. RiderCharges_P "+riderCharges_P);

                BIMAST.setPolicyAdministrationCharge_Q(
                        _policyAdministrationCharges_Q, prop.charge_Inflation,
                        prop.fixedMonthlyExp_SP, prop.fixedMonthlyExp_RP,
                        prop.inflation_pa_SP, prop.inflation_pa_RP, planType);
                policyAdministrationCharges_Q = Double.parseDouble(BIMAST
                        .getPolicyAdministrationCharge_Q());
                _policyAdministrationCharges_Q = policyAdministrationCharges_Q;
                // System.out.println("13. policyAdministrationCharges_Q "+policyAdministrationCharges_Q);

                BIMAST.setOneHundredPercentOfCummulativePremium_AW(_oneHundredPercentOfCummulativePremium_AW);
                oneHundredPercentOfCummulativePremium_AW = Double
                        .parseDouble(BIMAST
                                .getOneHundredPercentOfCummulativePremium_AW());
                _oneHundredPercentOfCummulativePremium_AW = oneHundredPercentOfCummulativePremium_AW;
                // System.out.println("45. oneHundredPercentOfCummulativePremium_AW "+oneHundredPercentOfCummulativePremium_AW);

                BIMAST.setMortalityCharges_R(_fundValueAtEnd_AB, policyTerm,
                        forBIArray, sumAssured, prop.mortalityChargesYield);
                mortalityCharges_R = Double.parseDouble(BIMAST.getMortalityCharges_R());
                _mortalityCharges_R = mortalityCharges_R;
                // System.out.println("14. mortalityCharges_R "+mortalityCharges_R);

                BIMAST.setTotalCharges_S(policyTerm, riderCharges_P);
                totalCharges_S = Double.parseDouble(BIMAST.getTotalCharges_S());
                _totalCharges_S = totalCharges_S;
                // System.out.println("15. totalCharges_S "+totalCharges_S);

                BIMAST.setServiceTax_exclOfSTonAllocAndSurr_T(serviceTax,
                        prop.mortalityAndRiderChargesYield,
                        prop.administrationChargesYield);
                serviceTaxExclOfSTOnAllocAndSurr_T = Double.parseDouble(BIMAST.getServiceTax_exclOfSTonAllocAndSurr_T());
                _serviceTaxExclOfSTOnAllocAndSurr_T = serviceTaxExclOfSTOnAllocAndSurr_T;
                // System.out.println("16. serviceTaxExclOfSTOnAllocAndSurr_T "+serviceTaxExclOfSTOnAllocAndSurr_T);

                BIMAST.setAdditionToFundIfAny_V(_fundValueAtEnd_AB, policyTerm,
                        riderCharges_P);
                additionToFundIfAny_V = Double.parseDouble(BIMAST.getAdditionToFundIfAny_V());
                _additionToFundIfAny_V = additionToFundIfAny_V;
                // System.out.println("18. additionToFundIfAny_V "+additionToFundIfAny_V);

                BIMAST.setFundBeforeFMC_W(_fundValueAtEnd_AB, policyTerm,
                        riderCharges_P);
                fundBeforeFMC_W = Double.parseDouble(BIMAST.getFundBeforeFMC_W());
                _fundBeforeFMC_W = fundBeforeFMC_W;
                // System.out.println("19. fundBeforeFMC_W "+fundBeforeFMC_W);

                BIMAST.setFundManagementCharge_X(policyTerm, percentToBeInvested_EquityFund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_BondFund, percentToBeInvested_MoneyMarketFund, percentToBeInvested_Top300Fund, percentToBeInvested_BondOptimiserFund, percentToBeInvested_MidcapFund, percentToBeInvested_PureFund, percentToBeInvested_CorpBondFund);
                fundManagementCharge_X = Double.parseDouble(BIMAST.getFundManagementCharge_X());
                _fundManagementCharge_X = fundManagementCharge_X;
                // System.out.println("20. fundManagementCharge_X "+fundManagementCharge_X);

                BIMAST.setServiceTaxOnFMCReductionYield_Y(serviceTax);
                serviceTaxOnFMC_Y = Double.parseDouble(BIMAST.getServiceTaxOnFMCReductionYield_Y());
                _serviceTaxOnFMC_Y = serviceTaxOnFMC_Y;
                // System.out.println("21. serviceTaxOnFMC_Y "+serviceTaxOnFMC_Y);

                BIMAST.setFundValueAfterFMCAndBeforeGAReductionYield_Z(policyTerm);
                fundValueAfterFMCBerforeGA_Z = Double.parseDouble(BIMAST.getFundValueAfterFMCAndBeforeGAReductionYield_Z());
                _fundValueAfterFMCBerforeGA_Z = fundValueAfterFMCBerforeGA_Z;
                // System.out.println("22. fundValueAfterFMCBerforeGA_Z "+fundValueAfterFMCBerforeGA_Z);

                BIMAST.setTotalServiceTaxReductionYield_U(riderCharges_P, serviceTax);
                totalServiceTax_U = Double.parseDouble(BIMAST.getTotalServiceTaxReductionYield_U());
                _totalServiceTax_U = totalServiceTax_U;
                // System.out.println("17. totalServiceTax_U "+totalServiceTax_U);

                BIMAST.setGuaranteedAddition_AA(planType, effectivePremium, PPT);
                guaranteedAddition_AA = Double.parseDouble(BIMAST.getGuaranteedAddition_AA());
                _guaranteedAddition_AA = guaranteedAddition_AA;
                // System.out.println("23. guaranteedAddition_AA "+guaranteedAddition_AA);

                BIMAST.setFundValueAtEndReductionYield_AB();
                fundValueAtEnd_AB = Double.parseDouble(BIMAST
                        .getFundValueAtEndReductionYield_AB());
                _fundValueAtEnd_AB = fundValueAtEnd_AB;
                // System.out.println("24. fundValueAtEnd_AB "+fundValueAtEnd_AB);

                BIMAST.setSurrenderCap_AV(effectivePremium, planType);
                surrenderCap_AV = Double.parseDouble(BIMAST.getSurrenderCap_AV());
                _surrenderCap_AV = surrenderCap_AV;
                // System.out.println("44. surrenderCap_AV "+surrenderCap_AV);

                BIMAST.setSurrenderChargesReductionYield_AC(effectivePremium, PPT, planType);
                surrenderCharges_AC = Double.parseDouble(BIMAST.getSurrenderChargesReductionYield_AC());
                _surrenderCharges_AC = surrenderCharges_AC;
                // System.out.println("25. surrenderCharges_AC "+surrenderCharges_AC);

                BIMAST.setServiceTaxOnSurrenderChargesReductionYield_AD(serviceTax);
                serviceTaxOnSurrenderCharges_AD = Double.parseDouble(BIMAST.getServiceTaxOnSurrenderChargesReductionYield_AD());
                _serviceTaxOnSurrenderCharges_AD = serviceTaxOnSurrenderCharges_AD;
                // System.out.println("26. serviceTaxOnSurrenderCharges_AD "+serviceTaxOnSurrenderCharges_AD);

                BIMAST.setSurrenderValueReductionYield_AE();
                surrenderValue_AE = Double.parseDouble(BIMAST.getSurrenderValueReductionYield_AE());
                _surrenderValue_AE = surrenderValue_AE;
                // System.out.println("27. surrenderValue_AE "+surrenderValue_AE);

                BIMAST.setDeathBenefitReductionYield_AF(policyTerm, sumAssured);
                deathBenefit_AF = Double.parseDouble(BIMAST.getDeathBenefitReductionYield_AF());
                _deathBenefit_AF = deathBenefit_AF;
                // System.out.println("28. deathBenefit_AF "+deathBenefit_AF);

                // System.out.println("\n @@@@@@@@@@@@@@@@@@@@@ 8% interest rate @@@@@@@@@@@@@@@@@@@@@ ");

                BIMAST.setMortalityCharges_AG(_fundValueAtEnd_AQ, policyTerm,
                        forBIArray, sumAssured, prop.mortalityChargesYield);
                mortalityCharges_AG = Double.parseDouble(BIMAST.getMortalityCharges_AG());
                _mortalityCharges_AG = mortalityCharges_AG;
                // System.out.println("29. mortalityCharges_AG "+mortalityCharges_AG);

                BIMAST.setTotalCharges_AH(policyTerm, riderCharges_P);
                totalCharges_AH = Double.parseDouble(BIMAST.getTotalCharges_AH());
                _totalCharges_AH = totalCharges_AH;
                // System.out.println("30. totalCharges_AH "+totalCharges_AH);

                BIMAST.setServiceTax_exclOfSTonAllocAndSurr_AI(serviceTax,
                        prop.mortalityAndRiderChargesYield,
                        prop.administrationChargesYield);
                serviceTaxExclOfSTOnAllocAndSurr_AI = Double.parseDouble(BIMAST.getServiceTax_exclOfSTonAllocAndSurr_AI());
                _serviceTaxExclOfSTOnAllocAndSurr_AI = serviceTaxExclOfSTOnAllocAndSurr_AI;
                // System.out.println("31. serviceTaxExclOfSTOnAllocAndSurr_AI "+serviceTaxExclOfSTOnAllocAndSurr_AI);

                BIMAST.setAdditionToFundIfAny_AK(_fundValueAtEnd_AQ,
                        policyTerm, riderCharges_P);
                additionToFundIfAny_AK = Double.parseDouble(BIMAST.getAdditionToFundIfAny_AK());
                _additionToFundIfAny_AK = additionToFundIfAny_AK;
                // System.out.println("33. additionToFundIfAny_AK "+additionToFundIfAny_AK);

                BIMAST.setFundBeforeFMC_AL(_fundValueAtEnd_AQ, policyTerm,
                        riderCharges_P);
                fundBeforeFMC_AL = Double.parseDouble(BIMAST.getFundBeforeFMC_AL());
                _fundBeforeFMC_AL = fundBeforeFMC_AL;
                // System.out.println("34. fundBeforeFMC_AL "+fundBeforeFMC_AL);

                BIMAST.setFundManagementCharge_AM(policyTerm, percentToBeInvested_EquityFund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_BondFund, percentToBeInvested_MoneyMarketFund, percentToBeInvested_Top300Fund, percentToBeInvested_BondOptimiserFund, percentToBeInvested_MidcapFund, percentToBeInvested_PureFund, percentToBeInvested_CorpBondFund);
                fundManagementCharge_AM = Double.parseDouble(BIMAST.getFundManagementCharge_AM());
                _fundManagementCharge_AM = fundManagementCharge_AM;
                // System.out.println("35. fundManagementCharge_AM "+fundManagementCharge_AM);

                BIMAST.setServiceTaxOnFMCReductionYield_AN(serviceTax);
                serviceTaxOnFMC_AN = Double.parseDouble(BIMAST.getServiceTaxOnFMCReductionYield_AN());
                _serviceTaxOnFMC_AN = serviceTaxOnFMC_AN;
                // System.out.println("36. serviceTaxOnFMC_AN "+serviceTaxOnFMC_AN);

                BIMAST.setFundValueAfterFMCAndBeforeGAReductionYield_AO(policyTerm);
                fundValueAfterFMCBerforeGA_AO = Double.parseDouble(BIMAST.getFundValueAfterFMCAndBeforeGAReductionYield_AO());
                _fundValueAfterFMCBerforeGA_AO = fundValueAfterFMCBerforeGA_AO;
                // System.out.println("37. fundValueAfterFMCBerforeGA_AO "+fundValueAfterFMCBerforeGA_AO);

                BIMAST.setTotalServiceTaxReductionYield_AJ(riderCharges_P, serviceTax);
                totalServiceTax_AJ = Double.parseDouble(BIMAST.getTotalServiceTaxReductionYield_AJ());
                _totalServiceTax_AJ = totalServiceTax_AJ;
                // System.out.println("32. totalServiceTax_AJ "+totalServiceTax_AJ);

                BIMAST.setGuaranteedAddition_AP(planType, effectivePremium, PPT);
                guaranteedAddition_AP = Double.parseDouble(BIMAST.getGuaranteedAddition_AP());
                _guaranteedAddition_AP = guaranteedAddition_AP;
                // System.out.println("38. guaranteedAddition_AP "+guaranteedAddition_AP);

                BIMAST.setMonth_BB(monthNumber);
                month_BB = Integer.parseInt(BIMAST.getMonth_BB());
                _month_BB = month_BB;
                // System.out.println("month_bb "+month_BB);

                BIMAST.setReductionYield_BI(noOfYearsElapsedSinceInception,
                        _fundValueAtEnd_AQ);
                reductionYield_BI = Double.parseDouble(BIMAST
                        .getReductionYield_BI());
                _reductionYield_BI = reductionYield_BI;
                // System.out.println("reductionYield_BU "+reductionYield_BI);

                BIMAST.setReductionYield_BD(policyTerm, _fundValueAtEnd_AQ);
                reductionYield_BD = Double.parseDouble(BIMAST
                        .getReductionYield_BD());
                _reductionYield_BD = reductionYield_BD;
                // System.out.println("reductionYield_BQ "+reductionYield_BD);

                BIMAST.setFundValueAtEndReductionYield_AQ();
                fundValueAtEnd_AQ = Double.parseDouble(BIMAST
                        .getFundValueAtEndReductionYield_AQ());
                _fundValueAtEnd_AQ = fundValueAtEnd_AQ;
                // System.out.println("39. fundValueAtEnd_AQ "+fundValueAtEnd_AQ);

                BIMAST.setSurrenderChargesReductionYield_AR(effectivePremium, PPT, planType);
                surrenderCharges_AR = Double.parseDouble(BIMAST.getSurrenderChargesReductionYield_AR());
                _surrenderCharges_AR = surrenderCharges_AR;
                // System.out.println("40. surrenderCharges_AR "+surrenderCharges_AR);

                BIMAST.setServiceTaxOnSurrenderChargesReductionYield_AS(serviceTax);
                serviceTaxOnSurrenderCharges_AS = Double.parseDouble(BIMAST.getServiceTaxOnSurrenderChargesReductionYield_AS());
                _serviceTaxOnSurrenderCharges_AS = serviceTaxOnSurrenderCharges_AS;
                // System.out.println("41. serviceTaxOnSurrenderCharges_AS "+serviceTaxOnSurrenderCharges_AS);

                BIMAST.setSurrenderValueReductionYield_AT();
                surrenderValue_AT = Double.parseDouble(BIMAST.getSurrenderValueReductionYield_AT());
                _surrenderValue_AT = surrenderValue_AT;
                // System.out.println("42. surrenderValue_AT "+surrenderValue_AT);

                BIMAST.setDeathBenefitReductionYield_AU(policyTerm, sumAssured);
                deathBenefit_AU = Double.parseDouble(BIMAST.getDeathBenefitReductionYield_AU());
                _deathBenefit_AU = deathBenefit_AU;
                // System.out.println("43. deathBenefit_AU "+deathBenefit_AU);

                monthNumber++;

                List_BD.add(commonForAllProd.roundUp_Level2(commonForAllProd
                        .getStringWithout_E(reductionYield_BD)));
                List_BI.add(commonForAllProd.roundUp_Level2(commonForAllProd
                        .getStringWithout_E(reductionYield_BI)));
            }
            // System.out.println("List_BD.size "+List_BD.size());
            // System.out.println("List_BD "+List_BD);
            // System.out.println("List_BI.size "+List_BI.size());
            // System.out.println("List_BI "+List_BI);
            double ans = BIMAST.irr(List_BD, 0.01);
            double ans1 = BIMAST.irr(List_BI, 0.01);
            // System.out.println("ans_BD "+ans);
            // System.out.println("ans1_BI "+ans1);

            BIMAST.setIRRAnnual_BD(ans);
            irrAnnual_BD = Double.parseDouble(BIMAST.getIRRAnnual_BD());
            _irrAnnual_BD = irrAnnual_BD;
            // System.out.println("irrAnnual_BQ "+irrAnnual_BD);

            BIMAST.setIRRAnnual_BI(ans1);
            irrAnnual_BI = Double.parseDouble(BIMAST.getIRRAnnual_BI());
            _irrAnnual_BI = irrAnnual_BI;
            // System.out.println("irrAnnual_BU "+irrAnnual_BI);

            BIMAST.setReductionInYieldMaturityAt(prop.int2);
            reductionInYieldMaturityAt = Double.parseDouble(BIMAST
                    .getReductionInYieldMaturityAt());
            _reductionInYieldMaturityAt = reductionInYieldMaturityAt;
            // System.out.println("reductionInYieldMaturityAt "+reductionInYieldMaturityAt);

            BIMAST.setReductionInYieldNumberOfYearsElapsedSinceInception(prop.int2);
            reductionInYieldNumberOfYearsElapsedSinceInception = Double
                    .parseDouble(BIMAST
                            .getReductionInYieldNumberOfYearsElapsedSinceInception());
            _reductionInYieldNumberOfYearsElapsedSinceInception = reductionInYieldNumberOfYearsElapsedSinceInception;
            // System.out.println("reductionInYieldNumberOfYearsElapsedSinceInception "+reductionInYieldNumberOfYearsElapsedSinceInception);

            BIMAST.setnetYieldAt8Percent();
            netYield8pa = Double.parseDouble(BIMAST.getnetYieldAt8Percent());

            // System.out.println("netYield8pa "+netYield8pa);

        } catch (Exception e) {
            e.printStackTrace();
            // System.out.println("** error **" + e.getMessage());
        }

        // System.out.println("reduction yield maturity at "+commonForAllProd.getRoundOffLevel2(commonForAllProd.getStringWithout_E(reductionInYieldMaturityAt)));
        // System.out.println("reduction yield number of years elapsed since inception  "+commonForAllProd.getRoundOffLevel2(commonForAllProd.getStringWithout_E(reductionInYieldNumberOfYearsElapsedSinceInception)));
        //return new String[]{(commonForAllProd.getStringWithout_E(sumAssured)),(commonForAllProd.getStringWithout_E(Double.parseDouble(commonForAllProd.getRoundUp(fundValueAtEnd_AC)))),(commonForAllProd.getStringWithout_E(Double.parseDouble(commonForAllProd.getRoundUp(fundValueAtEnd_AS))))};

        // netYield8pa Added by Akshaya on 18-Feb-2014
        return new String[]{commonForAllProd.getRoundOffLevel2(commonForAllProd.getStringWithout_E(reductionInYieldMaturityAt)), commonForAllProd.getRoundOffLevel2(commonForAllProd.getStringWithout_E(reductionInYieldMaturityAt)), commonForAllProd.getRoundOffLevel2(commonForAllProd.getStringWithout_E(netYield8pa))};

    }

    /******************************************************* End of Reduction in Yield **********************/

    private String getEffectivePremium() {
        double effPrem = 0;
        int PF = 1;
        double premium = 0;
        try {
            if (selPlan.getSelectedItem().toString().equals("Single")) {
                premium = 65000;
            } else if (selPlan.getSelectedItem().toString().equals("Limited")) {
                premium = 40000;
            } else if (selPlan.getSelectedItem().toString().equals("Regular")) {
                premium = 30000;
            }

            if (((Math.max(premium,
                    Double.parseDouble(premiumAmt.getText().toString())) * PF) % 100) == 0) {
                effPrem = (Math.max(premium,
                        Double.parseDouble(premiumAmt.getText().toString())) * PF);
            } else {
                effPrem = (Math.max(premium,
                        Double.parseDouble(premiumAmt.getText().toString()))
                        * PF - ((Math.max(premium,
                        Double.parseDouble(premiumAmt.getText().toString()))
                        * PF % 100)));
            }
        } catch (Exception ignored) {
        }
        return "" + effPrem;
    }

    private int getEffectiveTerm() {
        if (prop.isLT) {
            if (Integer.parseInt(ageInYears.getSelectedItem().toString()) <= 18) {
                return Math.min(getPolicyTermMaxMin("Max"), Integer
                        .parseInt(selPolicyTerm.getSelectedItem().toString()));
            } else {
                if (Integer.parseInt(ageInYears.getSelectedItem().toString())
                        + Integer.parseInt(selPolicyTerm.getSelectedItem()
                        .toString()) >= 75) {
                    return 75 - Integer.parseInt(ageInYears.getSelectedItem()
                            .toString());
                } else {
                    return Math.max(getPolicyTermMaxMin("Min"), Integer
                            .parseInt(selPolicyTerm.getSelectedItem()
                                    .toString()));
                }
            }
        } else {
            return 90 - Integer.parseInt(ageInYears.getSelectedItem()
                    .toString());
        }
    }

    private int getPolicyTermMaxMin(String minOrMax) {
        try {
            int minPolicyTerm, temp1, temp2, maxPolicyTerm;
            if (selPlan.getSelectedItem().toString().equals("Limited")) {
               /* if (premPayingTerm.getSelectedItem().toString().equals("10")) {
                    temp1 = 15;
                } else {
                    temp1 = 10;
                }*/
                if (premPayingTerm.getSelectedItem().toString().equals("15")) {
                    temp1 = 20;
                } else if (premPayingTerm.getSelectedItem().toString().equals("12") ||
                        premPayingTerm.getSelectedItem().toString().equals("10")) {
                    temp1 = 15;
                } else
                    temp1 = 12;
            } else {
                if (selPlan.getSelectedItem().toString().equals("Regular")) {
                    temp1 = 12;
                    /*if (Double.parseDouble(getEffectivePremium()) < 500000) {
                        // ////System.out.println("Less than 500000");
                        if (Double.parseDouble(getEffectivePremium()) < 100000) {
                            temp1 = 10;
                        } else {
                            temp1 = 7;
                        }
                    } else {
                        temp1 = 5;
                    }*/
                } else {
                    temp1 = 5;
                }
            }
            temp2 = 18 - Integer.parseInt(ageInYears.getSelectedItem()
                    .toString());
            minPolicyTerm = Math.max(temp1, temp2);
            maxPolicyTerm = Math.min(30, (70 - Integer.parseInt(ageInYears
                    .getSelectedItem().toString())));
            if (minOrMax.equals("Min")) {
                return minPolicyTerm;
            } else {
                return maxPolicyTerm;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    private int calPPT(int premPayingTerm) {
        if (!prop.isInforce) {
            return 0;
        } else {
            if (selPlan.getSelectedItem().toString().equals("Single")) {
                return 1;
            }
            if (selPlan.getSelectedItem().toString().equals("Regular")) {
                return getEffectiveTerm();
            } else {
                return premPayingTerm;
            }
        }
    }

    //calculation ends here/

    // help starts here
    private void updatePolicyTermLabel() {
        try {
            int minPolicyTerm, temp1, temp2, maxPolicyTerm;
            String message = "";

            if (selPlan.getSelectedItem().toString().equals("Limited")) {
                /*if (premPayingTerm.getSelectedItem().toString().equals("10")) {
                    temp1 = 15;
                } else {
                    temp1 = 10;
                }*/
                if (premPayingTerm.getSelectedItem().toString().equals("15")) {
                    temp1 = 20;
                } else if (premPayingTerm.getSelectedItem().toString().equals("12") ||
                        premPayingTerm.getSelectedItem().toString().equals("10")) {
                    temp1 = 15;
                } else
                    temp1 = 12;
            } else {
                if (selPlan.getSelectedItem().toString().equals("Regular")) {
                    temp1 = 12;
                    //temp1 = 10;
//                    if (Double.parseDouble(getEffectivePremium()) < 500000) {
//                        if (Double.parseDouble(getEffectivePremium()) < 100000) {
//                            temp1 = 10;
//                        } else {
//                            temp1 = 7;
//                        }
//                    } else {
//                        temp1 = 5;
//                    }
                } else {
                    temp1 = 5;
                }
            }
            temp2 = 18 - Integer.parseInt(ageInYears.getSelectedItem()
                    .toString());
            minPolicyTerm = Math.max(temp1, temp2);
            maxPolicyTerm = Math.min(30, (70 - Integer.parseInt(ageInYears
                    .getSelectedItem().toString())));

            if (selPlan.getSelectedItem().toString().equals("Regular")
                    || selPlan.getSelectedItem().toString().equals("Limited")) {

               /* if (minPolicyTerm == 10 && maxPolicyTerm == 15)
                    message = "(10 or 15 Years)";
                else if (minPolicyTerm == 10 && maxPolicyTerm > 15)
                    message = "(10 or 15 to " + maxPolicyTerm + " Years)";
                else if (minPolicyTerm > 10 && minPolicyTerm < 15
                        && maxPolicyTerm >= 15)
                    message = "(15 to " + maxPolicyTerm + " Years)";
                else if (minPolicyTerm >= 15 && maxPolicyTerm >= 15)
                    message = "(" + minPolicyTerm + " Years to "
                            + maxPolicyTerm + " Years)";
                else if (minPolicyTerm == 10 && maxPolicyTerm < 15)
                    message = "(10 Years)";*/

                if (Integer.parseInt(selPolicyTerm.getSelectedItem().toString()) < minPolicyTerm ||
                        Integer.parseInt(selPolicyTerm.getSelectedItem().toString()) > maxPolicyTerm) {
                    if (minPolicyTerm < 12 && maxPolicyTerm > 30)
                        message = "(Policy Term Should be" + minPolicyTerm + " Years to " + maxPolicyTerm + "Years)";
                    else if ((Integer.parseInt(selPolicyTerm.getSelectedItem().toString()) < minPolicyTerm) ||
                            (Integer.parseInt(selPolicyTerm.getSelectedItem().toString()) > maxPolicyTerm))
                        message = "(Policy Term should be in between " + minPolicyTerm + " Years to " + maxPolicyTerm + " Years)";

                }

            } else {
                message = "(" + minPolicyTerm + " to " + maxPolicyTerm
                        + " years)";
            }

            help_policyTerm.setText(message);
        } catch (Exception e) {/**/
        }
    }

    private void updatePremiumAmtLabel() {
        try {
            double minPremiumAmt = 0, maxPremiumAmt = prop.maxPremiumAmtLimit;
            double premium = 0;
            if (selPlan.getSelectedItem().toString().equals("Single")) {
                minPremiumAmt = 65000;
            } else if (selPlan.getSelectedItem().toString().equals("Limited")) {
                minPremiumAmt = 40000;
            } else if (selPlan.getSelectedItem().toString().equals("Regular")) {
                minPremiumAmt = 30000;
            }

            // help_premiumAmt.setText("(Rs."+currencyFormat.format(minPremiumAmt)+" to Rs."+currencyFormat.format(maxPremiumAmt)+")");
            help_premiumAmt.setText("(Rs. "
                    + currencyFormat.format(minPremiumAmt) + " to Rs. "
                    + currencyFormat.format(maxPremiumAmt) + ")");
        } catch (Exception ignored) {
        }
    }

    private void updateSAMFlabel() {
        double minSAMF = 0, maxSAMF = 0;

        if (selPlan.getSelectedItem().toString().equals("Single")) {
           /* if (Integer.parseInt(ageInYears.getSelectedItem().toString()) >= 0
                    && Integer
                    .parseInt(ageInYears.getSelectedItem().toString()) <= 44) {
                minSAMF = 1.25;
                maxSAMF = 3;
            } else if (Integer
                    .parseInt(ageInYears.getSelectedItem().toString()) >= 45
                    && Integer
                    .parseInt(ageInYears.getSelectedItem().toString()) <= 65) {
                minSAMF = 1.1;
                maxSAMF = 1.25;
            } else if (Integer
                    .parseInt(ageInYears.getSelectedItem().toString()) >= 66
                    && Integer
                    .parseInt(ageInYears.getSelectedItem().toString()) <= 70) {
                minSAMF = 1.1;
                maxSAMF = 1.25;
            } else if (Integer
                    .parseInt(ageInYears.getSelectedItem().toString()) >= 71
                    && Integer
                    .parseInt(ageInYears.getSelectedItem().toString()) <= 90) {
                minSAMF = 1.1;
                maxSAMF = 1.25;
            }*/

            minSAMF = 1.25;
            maxSAMF = 1.25;
        } else if (selPlan.getSelectedItem().toString().equals("Regular")) {
           /* if (Integer.parseInt(ageInYears.getSelectedItem().toString()) >= 0
                    && Integer
                    .parseInt(ageInYears.getSelectedItem().toString()) <= 44) {
                minSAMF = Math.max(10, (0.5 * Integer.parseInt(selPolicyTerm
                        .getSelectedItem().toString())));
                // minSAMF=10;
                maxSAMF = 20;
            } else if (Integer
                    .parseInt(ageInYears.getSelectedItem().toString()) >= 45
                    && Integer
                    .parseInt(ageInYears.getSelectedItem().toString()) <= 65) {
                minSAMF = Math.max(7, (0.25 * Integer.parseInt(selPolicyTerm
                        .getSelectedItem().toString())));
                // minSAMF=7;
                maxSAMF = 20;
            } else if (Integer
                    .parseInt(ageInYears.getSelectedItem().toString()) >= 66
                    && Integer
                    .parseInt(ageInYears.getSelectedItem().toString()) <= 70) {
                minSAMF = Math.max(7, (0.25 * Integer.parseInt(selPolicyTerm
                        .getSelectedItem().toString())));
                // minSAMF=7;
                maxSAMF = 20;
            } else if (Integer
                    .parseInt(ageInYears.getSelectedItem().toString()) >= 71
                    && Integer
                    .parseInt(ageInYears.getSelectedItem().toString()) <= 90) {
                minSAMF = Math.max(7, (0.25 * Integer.parseInt(selPolicyTerm
                        .getSelectedItem().toString())));
                // minSAMF=7;
                maxSAMF = 20;
            }*/
            minSAMF = 10;
            maxSAMF = 10;
        } else if (selPlan.getSelectedItem().toString().equals("Limited")) {
         /*   if (Integer.parseInt(ageInYears.getSelectedItem().toString()) >= 0
                    && Integer
                    .parseInt(ageInYears.getSelectedItem().toString()) <= 44) {
                minSAMF = Math.max(10, (0.5 * Integer.parseInt(selPolicyTerm
                        .getSelectedItem().toString())));
                maxSAMF = 15;
            } else if (Integer
                    .parseInt(ageInYears.getSelectedItem().toString()) >= 45
                    && Integer
                    .parseInt(ageInYears.getSelectedItem().toString()) <= 65) {
                minSAMF = Math.max(7, (0.25 * Integer.parseInt(selPolicyTerm
                        .getSelectedItem().toString())));
                maxSAMF = 15;
            } else if (Integer
                    .parseInt(ageInYears.getSelectedItem().toString()) >= 66
                    && Integer
                    .parseInt(ageInYears.getSelectedItem().toString()) <= 70) {
                minSAMF = Math.max(7, (0.25 * Integer.parseInt(selPolicyTerm
                        .getSelectedItem().toString())));
                maxSAMF = 15;
            } else if (Integer
                    .parseInt(ageInYears.getSelectedItem().toString()) >= 71
                    && Integer
                    .parseInt(ageInYears.getSelectedItem().toString()) <= 90) {
                minSAMF = Math.max(7, (0.25 * Integer.parseInt(selPolicyTerm
                        .getSelectedItem().toString())));
                maxSAMF = 15;
            }*/
            minSAMF = 10;
            maxSAMF = 10;
        }
        // help_SAMF.setText("Sum Assured Multiple Factor[SAMF](min. "+minSAMF+"% & Max. "+maxSAMF+"%)");
        help_SAMF.setText("(" + minSAMF + " to " + maxSAMF + ")");
        // SAMF.setText(minSAMF + "");

    }

    // help for no of yeras elapsed since Inception
    private void updatenoOfYearsElapsedSinceInception() {
        try {
            help_noOfYearsElapsedSinceInception.setText("(5 to "
                    + selPolicyTerm.getSelectedItem().toString() + " years)");
        } catch (Exception ignored) {
        }
    }

    // help ends here

    //validation starts here
    // premium amount validation
    private boolean valPremiumAmt() {
        double minPremiumAmt = 0, maxPremiumAmt = prop.maxPremiumAmtLimit;
        double premium = 0;
        String error = "";
        if (selPlan.getSelectedItem().toString().equals("Single")) {
            minPremiumAmt = 65000;
        } else if (selPlan.getSelectedItem().toString().equals("Limited")) {
            minPremiumAmt = 40000;
        } else if (selPlan.getSelectedItem().toString().equals("Regular")) {
            minPremiumAmt = 30000;
        }
        if (premiumAmt.getText().toString().equals("")) {
            error = "Please enter Premium Amount in Rs. ";
        } else if (!(Double.parseDouble(premiumAmt.getText().toString()) % 100 == 0)) {
            error = "Premium Amount should be multiple of 100";
        } else {
            if (Double.parseDouble(premiumAmt.getText().toString()) < minPremiumAmt
                    || Double.parseDouble(premiumAmt.getText().toString()) > maxPremiumAmt) {
                error = "Enter Premium Amount Rs. "
                        + currencyFormat.format(minPremiumAmt) + " and Rs. "
                        + currencyFormat.format(maxPremiumAmt);
            }

        }

        if (!error.equals("")) {
            showAlert.setMessage(error);
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {

                            premiumAmt.requestFocus();
                        }
                    });
            showAlert.show();

            return false;
        } else
            return true;
    }

    // private void setContentView(Class<layout> class1) {
    //
    //
    // }

    // SAMF validation

    private boolean valSAMF() {
        double minSAMF = 0, maxSAMF = 0;
        String error = "";

        if (SAMF.getText().toString().equals("")) {
            error = "Please enter Sum Assured Multiple Factor[SAMF]";
        } else {
            if (selPlan.getSelectedItem().toString().equals("Single")) {
              /*  if (Integer.parseInt(ageInYears.getSelectedItem().toString()) >= 0
                        && Integer.parseInt(ageInYears.getSelectedItem()
                        .toString()) <= 44) {
                    minSAMF = 1.25;
                    maxSAMF = 3;
                } else if (Integer.parseInt(ageInYears.getSelectedItem()
                        .toString()) >= 45
                        && Integer.parseInt(ageInYears.getSelectedItem()
                        .toString()) <= 65) {
                    minSAMF = 1.1;
                    maxSAMF = 1.25;
                } else if (Integer.parseInt(ageInYears.getSelectedItem()
                        .toString()) >= 66
                        && Integer.parseInt(ageInYears.getSelectedItem()
                        .toString()) <= 70) {
                    minSAMF = 1.1;
                    maxSAMF = 1.25;
                } else if (Integer.parseInt(ageInYears.getSelectedItem()
                        .toString()) >= 71
                        && Integer.parseInt(ageInYears.getSelectedItem()
                        .toString()) <= 90) {
                    minSAMF = 1.1;
                    maxSAMF = 1.25;
                }*/
                minSAMF = 1.25;
                maxSAMF = 1.25;
            } else if (selPlan.getSelectedItem().toString().equals("Regular")) {
               /* if (Integer.parseInt(ageInYears.getSelectedItem().toString()) >= 0
                        && Integer.parseInt(ageInYears.getSelectedItem()
                        .toString()) <= 44) {
                    minSAMF = Math.max(10, (0.5 * Integer
                            .parseInt(selPolicyTerm.getSelectedItem()
                                    .toString())));
                    // minSAMF=10;
                    maxSAMF = 20;
                } else if (Integer.parseInt(ageInYears.getSelectedItem()
                        .toString()) >= 45
                        && Integer.parseInt(ageInYears.getSelectedItem()
                        .toString()) <= 65) {
                    minSAMF = Math.max(7, (0.25 * Integer
                            .parseInt(selPolicyTerm.getSelectedItem()
                                    .toString())));
                    // minSAMF=7;
                    maxSAMF = 20;
                } else if (Integer.parseInt(ageInYears.getSelectedItem()
                        .toString()) >= 66
                        && Integer.parseInt(ageInYears.getSelectedItem()
                        .toString()) <= 70) {
                    minSAMF = Math.max(7, (0.25 * Integer
                            .parseInt(selPolicyTerm.getSelectedItem()
                                    .toString())));
                    // minSAMF=7;
                    maxSAMF = 20;
                } else if (Integer.parseInt(ageInYears.getSelectedItem()
                        .toString()) >= 71
                        && Integer.parseInt(ageInYears.getSelectedItem()
                        .toString()) <= 90) {
                    minSAMF = Math.max(7, (0.25 * Integer
                            .parseInt(selPolicyTerm.getSelectedItem()
                                    .toString())));
                    // minSAMF=7;
                    maxSAMF = 20;
                }*/
                minSAMF = 10;
                maxSAMF = 10;
            } else if (selPlan.getSelectedItem().toString().equals("Limited")) {
              /*  if (Integer.parseInt(ageInYears.getSelectedItem().toString()) >= 0
                        && Integer.parseInt(ageInYears.getSelectedItem()
                        .toString()) <= 44) {
                    minSAMF = Math.max(10, (0.5 * Integer
                            .parseInt(selPolicyTerm.getSelectedItem()
                                    .toString())));
                    maxSAMF = 15;
                } else if (Integer.parseInt(ageInYears.getSelectedItem()
                        .toString()) >= 45
                        && Integer.parseInt(ageInYears.getSelectedItem()
                        .toString()) <= 65) {
                    minSAMF = Math.max(7, (0.25 * Integer
                            .parseInt(selPolicyTerm.getSelectedItem()
                                    .toString())));
                    maxSAMF = 15;
                } else if (Integer.parseInt(ageInYears.getSelectedItem()
                        .toString()) >= 66
                        && Integer.parseInt(ageInYears.getSelectedItem()
                        .toString()) <= 70) {
                    minSAMF = Math.max(7, (0.25 * Integer
                            .parseInt(selPolicyTerm.getSelectedItem()
                                    .toString())));
                    maxSAMF = 15;
                } else if (Integer.parseInt(ageInYears.getSelectedItem()
                        .toString()) >= 71
                        && Integer.parseInt(ageInYears.getSelectedItem()
                        .toString()) <= 90) {
                    minSAMF = Math.max(7, (0.25 * Integer
                            .parseInt(selPolicyTerm.getSelectedItem()
                                    .toString())));
                    maxSAMF = 15;
                }*/
                minSAMF = 10;
                maxSAMF = 10;
            }
            if (Double.parseDouble(SAMF.getText().toString()) < minSAMF
                    || Double.parseDouble(SAMF.getText().toString()) > maxSAMF) {
                error = "Sum Assured Multiple Factor (SAMF) should be in the range of "
                        + minSAMF + " to " + maxSAMF;
            }
        }

        if (!error.equals("")) {
            showAlert.setMessage(error);
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {

                            SAMF.requestFocus();
                        }
                    });
            showAlert.show();

            return false;
        }
        return true;
    }

    // Validating policy term
    // public boolean valPolicyTerm() {
    // int minPolicyTerm = 0, temp1 = 0, temp2 = 0, maxPolicyTerm = 0;
    // String error = "";
    //
    // if (selPlan.getSelectedItem().toString().equals("Limited")) {
    // if (premPayingTerm.getSelectedItem().toString().equals("10")) {
    // temp1 = 15;
    // } else {
    // temp1 = 10;
    // }
    // } else {
    // if (selPlan.getSelectedItem().toString().equals("Regular")) {
    // if (Double.parseDouble(getEffectivePremium()) < 500000) {
    // if (Double.parseDouble(getEffectivePremium()) < 100000) {
    // temp1 = 10;
    // } else {
    // temp1 = 7;
    // }
    // } else {
    // temp1 = 5;
    // }
    // } else {
    // temp1 = 5;
    // }
    // }
    // temp2 = 18 - Integer.parseInt(ageInYears.getSelectedItem().toString());
    // minPolicyTerm = Math.max(temp1, temp2);
    // maxPolicyTerm = Math
    // .min(30, (70 - Integer.parseInt(ageInYears.getSelectedItem()
    // .toString())));
    //
    // if (selPlan.getSelectedItem().toString().equals("Regular")
    // || selPlan.getSelectedItem().toString().equals("Limited")) {
    //
    // if (Integer.parseInt(selPolicyTerm.getSelectedItem().toString()) <
    // minPolicyTerm
    // || Integer.parseInt(selPolicyTerm.getSelectedItem()
    // .toString()) > maxPolicyTerm
    // || (Integer.parseInt(selPolicyTerm.getSelectedItem()
    // .toString()) >= 11 && Integer
    // .parseInt(selPolicyTerm.getSelectedItem()
    // .toString()) <= 14)) {
    // if (minPolicyTerm == 10 && maxPolicyTerm == 15)
    // error = "Policy Term Should be 10 or 15 Years";
    // else if (minPolicyTerm == 10 && maxPolicyTerm > 15)
    // error = "Policy Term Should be 10 or between 15 to "
    // + maxPolicyTerm + " Years";
    // else if (minPolicyTerm > 10 && minPolicyTerm < 15
    // && maxPolicyTerm >= 15)
    // error = "Policy Term Should be between 15 to "
    // + maxPolicyTerm + " Years";
    // else if (minPolicyTerm >= 15 && maxPolicyTerm >= 15)
    // error = "Policy Term should be in between " + minPolicyTerm
    // + " Years to " + maxPolicyTerm + " Years";
    // else if (minPolicyTerm == 10 && maxPolicyTerm < 15)
    // error = "Policy term should be 10 Years";
    // }
    // } else {
    // if ((Integer.parseInt(selPolicyTerm.getSelectedItem().toString()) <
    // minPolicyTerm)
    // || (Integer.parseInt(selPolicyTerm.getSelectedItem()
    // .toString()) > maxPolicyTerm)) {
    // error = "Policy Term should be in between " + minPolicyTerm
    // + " Years to " + maxPolicyTerm + " Years";
    // }
    // }
    //
    // if (!error.toString().equals("")) {
    // showAlert.setMessage(error.toString());
    // showAlert.setNeutralButton("OK",
    // new DialogInterface.OnClickListener() {
    //
    // @Override
    // public void onClick(DialogInterface dialog, int which) {
    //
    //
    // }
    // });
    // showAlert.show();
    // selPolicyTerm.requestFocus();
    // return false;
    // } else
    // return true;
    // }

    // Validating policy term
    private boolean valPolicyTerm() {


        int minPolicyTerm, temp1, maxPolicyTerm;
        String error = "";


        if (selPlan.getSelectedItem().toString().equals("Single")) {
            temp1 = 5;
        } else {
            if (selPlan.getSelectedItem().toString().equals("Limited")) {
                if (premPayingTerm.getSelectedItem().toString().equals("15")) {
                    temp1 = 20;
                } else if (premPayingTerm.getSelectedItem().toString().equals("12") ||
                        premPayingTerm.getSelectedItem().toString().equals("10")) {
                    temp1 = 15;
                } else
                    temp1 = 12;
            } else {
                temp1 = 12;
            }

        }
        minPolicyTerm = Math.max(temp1, 18 - Integer.parseInt(ageInYears.getSelectedItem().toString()));
        maxPolicyTerm = Math.min(30, 70 - Integer.parseInt(ageInYears.getSelectedItem().toString()));


        if (selPlan.getSelectedItem().toString().equals("Regular") ||
                selPlan.getSelectedItem().toString().equals("Limited")) {

            if (Integer.parseInt(selPolicyTerm.getSelectedItem().toString()) < minPolicyTerm ||
                    Integer.parseInt(selPolicyTerm.getSelectedItem().toString()) > maxPolicyTerm) {
                if (minPolicyTerm < 12 && maxPolicyTerm > 30)
                    error = "Policy Term Should be" + minPolicyTerm + " Years to " + maxPolicyTerm + "Years";
                else if ((Integer.parseInt(selPolicyTerm.getSelectedItem().toString()) < minPolicyTerm) ||
                        (Integer.parseInt(selPolicyTerm.getSelectedItem().toString()) > maxPolicyTerm))
                    error = "Policy Term should be in between " + minPolicyTerm + " Years to " + maxPolicyTerm + " Years";

            }
        } else {
            if ((Integer.parseInt(selPolicyTerm.getSelectedItem().toString()) < minPolicyTerm) ||
                    (Integer.parseInt(selPolicyTerm.getSelectedItem().toString()) > maxPolicyTerm)) {
                error = "Policy Term should be in between " + minPolicyTerm + " Years to " + maxPolicyTerm + " Years";
            }
        }




        /*if (selPlan.getSelectedItem().toString().equals("Single")) {
            temp1 = 5;
        } else {
            if (premPayingTerm.getSelectedItem().toString().equals("10")) {
                temp1 = 15;
            } else
                temp1 = 10;
        }
        minPolicyTerm = Math.max(temp1, 18 - Integer.parseInt(ageInYears.getSelectedItem().toString()));
        maxPolicyTerm = Math.min(30, 70 - Integer.parseInt(ageInYears.getSelectedItem().toString()));


        if (selPlan.getSelectedItem().toString().equals("Regular") || selPlan.getSelectedItem().toString().equals("Limited")) {

            if (Integer.parseInt(selPolicyTerm.getSelectedItem().toString()) < minPolicyTerm || Integer.parseInt(selPolicyTerm.getSelectedItem().toString()) > maxPolicyTerm || (Integer.parseInt(selPolicyTerm.getSelectedItem().toString()) >= 11 && Integer.parseInt(selPolicyTerm.getSelectedItem().toString()) <= 14)) {
                if (minPolicyTerm == 10 && maxPolicyTerm == 15)
                    error = "Policy Term Should be 10 or 15 Years";
                else if (minPolicyTerm == 10 && maxPolicyTerm > 15)
                    error = "Policy Term Should be 10 or between 15 to " + maxPolicyTerm + " Years";
                else if (minPolicyTerm > 10 && minPolicyTerm < 15 && maxPolicyTerm >= 15)
                    error = "Policy Term Should be between 15 to " + maxPolicyTerm + " Years";
                else if (minPolicyTerm >= 15 && maxPolicyTerm >= 15)
                    error = "Policy Term should be in between " + minPolicyTerm + " Years to " + maxPolicyTerm + " Years";
                else if (minPolicyTerm == 10 && maxPolicyTerm < 15)
                    error = "Policy term should be 10 Years";
                else if ((Integer.parseInt(selPolicyTerm.getSelectedItem().toString()) < minPolicyTerm) || (Integer.parseInt(selPolicyTerm.getSelectedItem().toString()) > maxPolicyTerm))
                    error = "Policy Term should be in between " + minPolicyTerm + " Years to " + maxPolicyTerm + " Years";

            }
        } else {
            if ((Integer.parseInt(selPolicyTerm.getSelectedItem().toString()) < minPolicyTerm) || (Integer.parseInt(selPolicyTerm.getSelectedItem().toString()) > maxPolicyTerm)) {
                error = "Policy Term should be in between " + minPolicyTerm + " Years to " + maxPolicyTerm + " Years";
            }
        }*/


        if (!error.equals("")) {
            showAlert.setMessage(error);
            showAlert.setNeutralButton("OK", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {


                }
            });
            showAlert.show();
            return false;
        } else
            return true;


//             int minPolicyTerm=0,temp1=0,temp2=0,maxPolicyTerm=0;
//             String error="";
//
//             if(selPlan.getSelectedItem().toString().equals("Limited"))
//             {
//                 if(premPayingTerm.getSelectedItem().toString().equals("10"))
//                 {temp1=15;}
//                 else
//                 {temp1=10;}
//             }
//             else
//             {
//               if(selPlan.getSelectedItem().toString().equals("Regular"))
//               {
//                   if(Double.parseDouble(getEffectivePremium()) < 500000)
//                   {
//                       if(Double.parseDouble(getEffectivePremium()) < 100000)
//                       {temp1=10;}
//                       else
//                       {temp1=7;}
//                   }
//                   else
//                   {temp1=5;}
//               }
//               else
//               {temp1=5;}
//             }
//             temp2=18-Integer.parseInt(ageInYears.getSelectedItem().toString());
//             minPolicyTerm=Math.max(temp1, temp2);
//             maxPolicyTerm=Math.min(30,(70-Integer.parseInt(ageInYears.getSelectedItem().toString())));
//
//             if(selPlan.getSelectedItem().toString().equals("Regular") || selPlan.getSelectedItem().toString().equals("Limited"))
//            {
//
//               if(Integer.parseInt(selPolicyTerm.getSelectedItem().toString()) < minPolicyTerm || Integer.parseInt(selPolicyTerm.getSelectedItem().toString()) > maxPolicyTerm || (Integer.parseInt(selPolicyTerm.getSelectedItem().toString())>=11 && Integer.parseInt(selPolicyTerm.getSelectedItem().toString())<=14))
//               {
//                  if(minPolicyTerm ==10 && maxPolicyTerm ==15)
//                     error="Policy Term Should be 10 or 15 Years";
//                  else if(minPolicyTerm ==10 && maxPolicyTerm >15)
//                     error = "Policy Term Should be 10 or between 15 to " + maxPolicyTerm + " Years" ;
//                  else if(minPolicyTerm > 10 && minPolicyTerm < 15 && maxPolicyTerm >=15)
//                     error = "Policy Term Should be between 15 to " + maxPolicyTerm + " Years";
//                  else if( minPolicyTerm  >=15 && maxPolicyTerm >=15)
//                     error = "Policy Term should be in between "+minPolicyTerm+" Years to "+maxPolicyTerm+" Years";
//                  else if(minPolicyTerm ==10 && maxPolicyTerm <15)
//                     error = "Policy term should be 10 Years";
//               }
//            }
//            else
//            {
//               if((Integer.parseInt(selPolicyTerm.getSelectedItem().toString()) < minPolicyTerm) || (Integer.parseInt(selPolicyTerm.getSelectedItem().toString()) > maxPolicyTerm))
//               {
//                  error ="Policy Term should be in between "+minPolicyTerm+" Years to "+maxPolicyTerm+" Years";
//               }
//            }
//
//             if(!error.toString().equals(""))
//             {
//              showAlert.setMessage(error.toString());
//              showAlert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
//
//             @Override
//             public void onClick(DialogInterface dialog, int which) {
//
//
//             }
//          });
//              showAlert.show();
//              return false;
//             }
//             else
//              return true;

    }

    private void valMaturityAge() {
        int Age = Integer.parseInt(ageInYears.getSelectedItem().toString());
        int PolicyTerm = Integer.parseInt(selPolicyTerm.getSelectedItem()
                .toString());

        // if (selPlan.getSelectedItem().toString().equals("Single")) {
        if ((Age + PolicyTerm) > 70) {
            showAlert.setMessage("Max. Maturity age allowed is 70 years");
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            selPolicyTerm.setSelection(0, false);
                            // apply for focusable
                            setFocusable(btn_bi_smart_wealth_builder_life_assured_date);
                            btn_bi_smart_wealth_builder_life_assured_date
                                    .requestFocus();
                        }
                    });
            showAlert.show();

        }

        // }

    }

    // Addition of invested fund mub be 100%
    private boolean valTotalAllocation() {
        String error;
        double equityFund, equityOptFund, growthFund, balancedFund, bondFund, moneyMarketFund, top300Fund,
                bondOptimiserFund, corporateBondFund, midcapFund, pureFund;
       /* double equityFund, equityOptFund, growthFund, balancedFund, bondFund, moneyMarketFund, top300Fund,
                bondOptimiserFund, midcapFund, pureFund;*/

        if (!percent_EquityFund.getText().toString().equals(""))
            equityFund = Double.parseDouble(percent_EquityFund.getText()
                    .toString());
        else
            equityFund = 0;

        if (!percent_EquityOptFund.getText().toString().equals(""))
            equityOptFund = Double.parseDouble(percent_EquityOptFund.getText()
                    .toString());
        else
            equityOptFund = 0;

        if (!percent_GrowthFund.getText().toString().equals(""))
            growthFund = Double.parseDouble(percent_GrowthFund.getText()
                    .toString());
        else
            growthFund = 0;

        if (!percent_BalancedFund.getText().toString().equals(""))
            balancedFund = Double.parseDouble(percent_BalancedFund.getText()
                    .toString());
        else
            balancedFund = 0;

        if (!percent_BondFund.getText().toString().equals(""))
            bondFund = Double
                    .parseDouble(percent_BondFund.getText().toString());
        else
            bondFund = 0;

        if (!percent_MoneyMktFund.getText().toString().equals(""))
            moneyMarketFund = Double.parseDouble(percent_MoneyMktFund.getText()
                    .toString());
        else
            moneyMarketFund = 0;

        if (!percent_Top300Fund.getText().toString().equals(""))
            top300Fund = Double.parseDouble(percent_Top300Fund.getText()
                    .toString());
        else
            top300Fund = 0;


        if (!percent_BondOptimiserFund.getText().toString().equals(""))
            bondOptimiserFund = Double.parseDouble(percent_BondOptimiserFund.getText()
                    .toString());
        else
            bondOptimiserFund = 0;

        if (!percent_CorporateBondFund.getText().toString().equals(""))
            corporateBondFund = Double.parseDouble(percent_CorporateBondFund.getText()
                    .toString());
        else
            corporateBondFund = 0;

        if (!percent_MidCapFund.getText().toString().equals(""))
            midcapFund = Double.parseDouble(percent_MidCapFund.getText()
                    .toString());
        else
            midcapFund = 0;

        if (!percent_PureFund.getText().toString().equals(""))
            pureFund = Double.parseDouble(percent_PureFund.getText()
                    .toString());
        else
            pureFund = 0;


        if ((equityFund + equityOptFund + growthFund + balancedFund + bondFund
                + moneyMarketFund + top300Fund + bondOptimiserFund + corporateBondFund + midcapFund + pureFund) != 100) {
      /*  if ((equityFund + equityOptFund + growthFund + balancedFund + bondFund
                + moneyMarketFund + top300Fund + bondOptimiserFund + midcapFund + pureFund) != 100) {*/
            showAlert
                    .setMessage("Total sum of % to be invested for all fund should be equal to 100%");
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {

                            percent_EquityFund.requestFocus();

                        }
                    });
            showAlert.show();

            return false;
        } else
            return true;
    }

    // Years Elapsed Since Inception Validation
    private boolean valYearsElapsedSinceInception() {
        if (noOfYearsElapsedSinceInception.getText().toString().equals("")
                || Double.parseDouble(noOfYearsElapsedSinceInception.getText()
                .toString()) < 5
                || Double.parseDouble(noOfYearsElapsedSinceInception.getText()
                .toString()) > Double.parseDouble(selPolicyTerm
                .getSelectedItem().toString())) {

            showAlert
                    .setMessage("Enter No. of Years Elapsed Since Inception between 5 to "
                            + selPolicyTerm.getSelectedItem().toString()
                            + " Years");
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // apply for focusable
                            setFocusable(noOfYearsElapsedSinceInception);
                            noOfYearsElapsedSinceInception.requestFocus();
                        }
                    });
            showAlert.show();

            return false;
        }

        return true;

    }

    public boolean valLifeAssuredDetail() {

        if (btn_bi_smart_wealth_builder_proposer_date.getText().toString()
                .equals("")) {
            commonMethods.dialogWarning(context, "Please Select Proposer DOB First", true);
            return false;
        } else if ((Integer
                .parseInt(!textviewProposerAge.getText()
                        .toString().equals("") ? textviewProposerAge
                        .getText().toString() : "0")) < 18) {
            if (edt_bi_smart_wealth_builder_life_assured_age.getText()
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
            showAlert.setMessage("Please Fill The Detail For Name");
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            showAlert.show();
            return false;
        }
    }

    private boolean valProposerSameAsLifeAssured() {
        if (!proposer_Is_Same_As_Life_Assured.equals("")) {
            return true;
        } else {
            showAlert
                    .setMessage("Please Select Whether Proposer Is Same As Life Assured");
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            showAlert.show();

            return false;

        }
    }

    private boolean valLifeAssuredProposerDetail() {
        if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {
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
                                    setFocusable(spnr_bi_smart_wealth_builder_life_assured_title);
                                    spnr_bi_smart_wealth_builder_life_assured_title
                                            .requestFocus();
                                } else if (lifeAssured_First_Name.equals("")) {

                                    edt_bi_smart_wealth_builder_life_assured_first_name
                                            .requestFocus();
                                } else {
                                    edt_bi_smart_wealth_builder_life_assured_last_name
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
                                setFocusable(spnr_bi_smart_wealth_builder_life_assured_title);
                                spnr_bi_smart_wealth_builder_life_assured_title
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
                                setFocusable(spnr_bi_smart_wealth_builder_life_assured_title);
                                spnr_bi_smart_wealth_builder_life_assured_title
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
                                setFocusable(spnr_bi_smart_wealth_builder_life_assured_title);
                                spnr_bi_smart_wealth_builder_life_assured_title
                                        .requestFocus();
                            }
                        });
                showAlert.show();

                return false;
            } else {
                return true;
            }

        } else if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("n")) {

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
                                    setFocusable(spnr_bi_smart_wealth_builder_life_assured_title);
                                    spnr_bi_smart_wealth_builder_life_assured_title
                                            .requestFocus();
                                } else if (lifeAssured_First_Name.equals("")) {

                                    edt_bi_smart_wealth_builder_life_assured_first_name
                                            .requestFocus();
                                } else {
                                    edt_bi_smart_wealth_builder_life_assured_last_name
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
                                setFocusable(spnr_bi_smart_wealth_builder_life_assured_title);
                                spnr_bi_smart_wealth_builder_life_assured_title
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
                                setFocusable(spnr_bi_smart_wealth_builder_life_assured_title);
                                spnr_bi_smart_wealth_builder_life_assured_title
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
                                setFocusable(spnr_bi_smart_wealth_builder_life_assured_title);
                                spnr_bi_smart_wealth_builder_life_assured_title
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
                                    setFocusable(spnr_bi_smart_wealth_builder_proposer_title);
                                    spnr_bi_smart_wealth_builder_proposer_title
                                            .requestFocus();
                                } else if (proposer_First_Name.equals("")) {
                                    edt_bi_smart_wealth_builder_proposer_first_name
                                            .requestFocus();
                                } else {
                                    edt_bi_smart_wealth_builder_proposer_last_name
                                            .requestFocus();
                                }
                            }
                        });
                showAlert.show();

                return false;

            } else if (gender_proposer.equalsIgnoreCase("")) {
                commonMethods.dialogWarning(context, "Please Select Proposer Gender", true);

                return false;
            } else if (proposer_Title.equalsIgnoreCase("Mr.")
                    && gender_proposer.equalsIgnoreCase("Female")) {

                showAlert
                        .setMessage("Proposer Title and Gender is not Valid");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // apply focusable method
                                setFocusable(spnr_bi_smart_wealth_builder_proposer_title);
                                spnr_bi_smart_wealth_builder_proposer_title
                                        .requestFocus();
                            }
                        });
                showAlert.show();

                return false;

            } else if (proposer_Title.equalsIgnoreCase("Ms.")
                    && gender_proposer.equalsIgnoreCase("Male")) {

                showAlert
                        .setMessage("Proposer Title and Gender is not Valid");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // apply focusable method
                                setFocusable(spnr_bi_smart_wealth_builder_proposer_title);
                                spnr_bi_smart_wealth_builder_proposer_title
                                        .requestFocus();
                            }
                        });
                showAlert.show();

                return false;

            } else if (proposer_Title.equalsIgnoreCase("Mrs.")
                    && gender_proposer.equalsIgnoreCase("Male")) {

                showAlert
                        .setMessage("Proposer Title and Gender is not Valid");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // apply focusable method
                                setFocusable(spnr_bi_smart_wealth_builder_proposer_title);
                                spnr_bi_smart_wealth_builder_proposer_title
                                        .requestFocus();
                            }
                        });
                showAlert.show();

                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    private boolean valDob() {

        if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {

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
                                setFocusable(btn_bi_smart_wealth_builder_life_assured_date);
                                btn_bi_smart_wealth_builder_life_assured_date
                                        .requestFocus();
                            }
                        });
                showAlert.show();

                return false;
            } else {
                return true;
            }
        } else if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("n")) {

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
                                setFocusable(btn_bi_smart_wealth_builder_life_assured_date);
                                btn_bi_smart_wealth_builder_life_assured_date
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
                                setFocusable(btn_bi_smart_wealth_builder_proposer_date);
                                btn_bi_smart_wealth_builder_proposer_date
                                        .requestFocus();
                            }
                        });
                showAlert.show();

                return false;
            } else {
                return true;
            }

        } else {
            return false;
        }
    }

    private boolean valLifeAssuredMinorDetail() {
        if (linearlayoutProposerDetails.getVisibility() == View.VISIBLE) {
            if (proposer_Title.equals("") || proposer_First_Name.equals("")
                    || proposer_Last_Name.equals("")) {

                showAlert.setMessage("Please Fill Name Detail For Proposer");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog,
                                                int which) {
                                if (proposer_Title.equals("")) {
                                    spnr_bi_smart_wealth_builder_proposer_title
                                            .requestFocus();
                                } else if (proposer_First_Name.equals("")) {
                                    edt_bi_smart_wealth_builder_proposer_first_name
                                            .requestFocus();
                                } else {
                                    edt_bi_smart_wealth_builder_proposer_last_name
                                            .requestFocus();
                                }
                            }
                        });
                showAlert.show();
                return false;
            } else if (proposer_date_of_birth.equals("")
                    || proposer_date_of_birth.equals("Select Date")) {
                showAlert.setMessage("Please Select Valid DOB For Proposer");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // apply focusable method
                                setFocusable(btn_bi_smart_wealth_builder_proposer_date);
                                btn_bi_smart_wealth_builder_proposer_date
                                        .requestFocus();
                            }
                        });
                showAlert.show();
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    public boolean email_id_validation(String email_id, EditText et) {

        Pattern pattern;
        Matcher matcher;
        boolean validationFlag = false;
        final String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email_id);
        if (!(matcher.matches())) {
            et.setError("Please provide the correct email address");
            validationFlag = false;
        } else if ((matcher.matches())) {
            validationFlag = true;
        }
        return validationFlag;
    }

    private void Date() {
        Calendar c = Calendar.getInstance();
        //System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        // formattedDate have current date/time
        // Toast.makeText(this, formattedDate, Toast.LENGTH_SHORT).show();
        c.add(Calendar.DATE, 30);
        String expiredDate = df.format(c.getTime());
    }

    /************************* validation ends here **********************************************************************/

    private void setDefaultDate(int id) {
        Calendar present_date = Calendar.getInstance();
        present_date.add(Calendar.YEAR, -id);
        mDay = present_date.get(Calendar.DAY_OF_MONTH);
        mMonth = present_date.get(Calendar.MONTH);
        mYear = present_date.get(Calendar.YEAR);
    }

    //
    // public void onBackPressed() {
    //
    // super.onBackPressed();
    // d.dismiss();
    // }
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
    private void setFocusable(View v) {

        v.setFocusable(true);
        v.setFocusableInTouchMode(true);
    }

    // method to set a clearing a element
    private void clearFocusable(View v) {

        v.setFocusable(false);
        v.setFocusableInTouchMode(false);
        // v.clearFocus();
    }

    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        if (v.getId() == edt_bi_smart_wealth_builder_life_assured_first_name
                .getId()) {
            // clearFocusable(edt_bi_smart_wealth_builder_life_assured_first_name);
            setFocusable(edt_bi_smart_wealth_builder_life_assured_middle_name);
            edt_bi_smart_wealth_builder_life_assured_middle_name.requestFocus();
        } else if (v.getId() == edt_bi_smart_wealth_builder_life_assured_middle_name
                .getId()) {
            // clearFocusable(edt_bi_smart_wealth_builder_life_assured_middle_name);
            setFocusable(edt_bi_smart_wealth_builder_life_assured_last_name);
            edt_bi_smart_wealth_builder_life_assured_last_name.requestFocus();
        } else if (v.getId() == edt_bi_smart_wealth_builder_life_assured_last_name
                .getId()) {

            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(
                    edt_bi_smart_wealth_builder_life_assured_last_name
                            .getWindowToken(), 0);
            // clearFocusable(edt_bi_smart_wealth_builder_life_assured_last_name);
            setFocusable(btn_bi_smart_wealth_builder_life_assured_date);
            btn_bi_smart_wealth_builder_life_assured_date.requestFocus();
        } else if (v.getId() == edt_bi_smart_wealth_builder_proposer_first_name
                .getId()) {
            // clearFocusable(edt_bi_smart_wealth_builder_proposer_first_name);
            setFocusable(edt_bi_smart_wealth_builder_proposer_middle_name);
            edt_bi_smart_wealth_builder_proposer_middle_name.requestFocus();
        } else if (v.getId() == edt_bi_smart_wealth_builder_proposer_middle_name
                .getId()) {
            // clearFocusable(edt_bi_smart_wealth_builder_proposer_middle_name);
            setFocusable(edt_bi_smart_wealth_builder_proposer_last_name);
            edt_bi_smart_wealth_builder_proposer_last_name.requestFocus();
        } else if (v.getId() == edt_bi_smart_wealth_builder_proposer_last_name
                .getId()) {

            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(
                    edt_bi_smart_wealth_builder_proposer_last_name
                            .getWindowToken(), 0);
            // clearFocusable(edt_bi_smart_wealth_builder_proposer_last_name);
            setFocusable(btn_bi_smart_wealth_builder_proposer_date);
            btn_bi_smart_wealth_builder_proposer_date.requestFocus();
        } else if (v.getId() == edt_proposerdetail_basicdetail_contact_no.getId()) {
            // clearFocusable(premiumAmt);
            setFocusable(edt_proposerdetail_basicdetail_Email_id);
            edt_proposerdetail_basicdetail_Email_id.requestFocus();
        } else if (v.getId() == edt_proposerdetail_basicdetail_Email_id.getId()) {
            // clearFocusable(premiumAmt);
            setFocusable(edt_proposerdetail_basicdetail_ConfirmEmail_id);
            edt_proposerdetail_basicdetail_ConfirmEmail_id.requestFocus();
        } else if (v.getId() == edt_proposerdetail_basicdetail_ConfirmEmail_id
                .getId()) {
            // clearFocusable(premiumAmt);
            clearFocusable(selPlan);
            setFocusable(selPlan);
            selPlan.requestFocus();
        } else if (v.getId() == premiumAmt.getId()) {
            // clearFocusable(premiumAmt);
            setFocusable(SAMF);
            SAMF.requestFocus();
        } else if (v.getId() == SAMF.getId()) {
            // clearFocusable(SAMF);
            setFocusable(percent_EquityFund);
            percent_EquityFund.requestFocus();
        } else if (v.getId() == percent_EquityFund.getId()) {
            // clearFocusable(percent_EquityFund);
            setFocusable(percent_EquityOptFund);
            percent_EquityOptFund.requestFocus();
        } else if (v.getId() == percent_EquityOptFund.getId()) {
            // clearFocusable(percent_EquityOptFund);
            setFocusable(percent_GrowthFund);
            percent_GrowthFund.requestFocus();
        } else if (v.getId() == percent_GrowthFund.getId()) {
            // clearFocusable(percent_GrowthFund);
            setFocusable(percent_BalancedFund);
            percent_BalancedFund.requestFocus();
        } else if (v.getId() == percent_BalancedFund.getId()) {
            // clearFocusable(percent_BalancedFund);
            setFocusable(percent_BondFund);
            percent_BondFund.requestFocus();
        } else if (v.getId() == percent_BondFund.getId()) {
            // clearFocusable(percent_BondFund);
            setFocusable(percent_MoneyMktFund);
            percent_MoneyMktFund.requestFocus();
        } else if (v.getId() == percent_MoneyMktFund.getId()) {
            // clearFocusable(percent_MoneyMktFund);
            setFocusable(percent_Top300Fund);
            percent_Top300Fund.requestFocus();
        } else if (v.getId() == percent_Top300Fund.getId()) {
            // clearFocusable(percent_Top300Fund);
            setFocusable(percent_BondOptimiserFund);
            percent_BondOptimiserFund.requestFocus();
        } else if (v.getId() == percent_BondOptimiserFund.getId()) {
            // clearFocusable(percent_BondOptimiserFund);
            setFocusable(percent_CorporateBondFund);
            percent_CorporateBondFund.requestFocus();
        } else if (v.getId() == percent_CorporateBondFund.getId()) {
            // clearFocusable(percent_CorporateBondFund);
            setFocusable(percent_MidCapFund);
            percent_MidCapFund.requestFocus();
        } else if (v.getId() == percent_MidCapFund.getId()) {
            // clearFocusable(percent_MidCapFund);
            setFocusable(percent_PureFund);
            percent_PureFund.requestFocus();
        } else if (v.getId() == percent_PureFund.getId()) {
            // clearFocusable(percent_PureFund);
            setFocusable(noOfYearsElapsedSinceInception);
            noOfYearsElapsedSinceInception.requestFocus();
        } else if (v.getId() == noOfYearsElapsedSinceInception.getId()) {

            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(
                    noOfYearsElapsedSinceInception.getWindowToken(), 0);
        }
        return true;
    }

    @Override
    protected void onResume() {

        super.onResume();
        b = 1;

        boolean flagFocus = true;
        if (!flagFocus) {
            llSmartWealthBuilderMain.requestFocus();
        } else {
            edt_bi_smart_wealth_builder_life_assured_first_name.requestFocus();
        }

    }

    /* Basic Details Method */

    private boolean valBasicDetail() {

        if (gender.equalsIgnoreCase("")) {
            commonMethods.dialogWarning(context, "Please Select LifeAssured Gender", true);

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
            commonMethods.dialogWarning(context,"Email Id Does Not Match", true);
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

    /*@Override
    protected void onDestroy() {

        if(CaptureSignature.scaled !=null){
            CaptureSignature.scaled.recycle();
        }

        if(ProposerCaptureSignature.scaled !=null){
            ProposerCaptureSignature.scaled.recycle();
        }
        super.onDestroy();
    }*/
}
